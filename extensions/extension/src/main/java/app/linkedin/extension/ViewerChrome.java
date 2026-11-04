package app.linkedin.extension;

import java.util.ArrayList;
import java.util.List;

/**
 * Tap to hide in the video and document viewers, built from LinkedIn's own show/hide state.
 * <p>
 * Both viewers are a feed-update-screen-container. In the document viewer its top bar and bottom part already show
 * only while the "showControlsBinding" state is true, but nothing flips it: a tap on the document now does. In the
 * video viewer only the play button has such a state; the top bar and bottom part now follow it, so they show and
 * hide with the video's own controls.
 */
final class ViewerChrome {
    private static final String CONTAINER = "feed-update-screen-container";
    private static final String VIDEO_SOURCE = ".setupDataAndViewer";
    private static final String DOCUMENT_SOURCE = ".documentViewerScreenDataAware";

    private static final int TRIGGERS_FIELD = 25;
    private static final int VIEW_TRACKING_FIELD = 26;
    private static final int COMPACT_DATA_FIELD = 35;
    private static final int DISPLAYED_FIELD = 40;
    /** Trigger.click, and the CompactAny type of SetState. */
    private static final int TRIGGER_CLICK_FIELD = 2;
    private static final int SET_STATE_TYPE = 13335;
    /** BooleanExpression.bindableBoolean and .not; BindableBoolean.key. */
    private static final int BINDABLE_BOOLEAN_FIELD = 2;
    private static final int NOT_FIELD = 5;
    private static final int STATE_KEY_FIELD = 10;

    private ViewerChrome() {
    }

    interface ComponentEditor {
        /** Returns the edited component, or the same array when unchanged. */
        byte[] edit(byte[] component);
    }

    /** Called with every SDUI component; returns it, or an edited copy for the viewers. */
    static Object rewrite(Object component, String viewName, String source, byte[] bytes) {
        if (!CONTAINER.equals(viewName) || bytes == null) return component;
        try {
            byte[] edited = source.endsWith(VIDEO_SOURCE) ? video(bytes)
                    : source.endsWith(DOCUMENT_SOURCE) ? document(bytes) : null;
            if (edited == null) return component;
            Object copy = component.getClass().getMethod("parseFrom", byte[].class).invoke(null, (Object) edited);
            return copy != null ? copy : component;
        } catch (Throwable t) {
            Diagnostics.error("viewer tap to hide failed", t);
            return component;
        }
    }

    /** The top bar and bottom part get the play button's show/hide state. */
    static byte[] video(byte[] container) {
        byte[] playButton = null;
        for (byte[] child : children(container)) {
            if ("media-video-play-pause".equals(viewName(child))) playButton = field(child, DISPLAYED_FIELD);
        }
        if (playButton == null) return null;
        final byte[] displayed = playButton;
        final int[] count = {0};
        byte[] edited = mapChildren(container, child -> {
            if (field(child, DISPLAYED_FIELD) != null || viewName(child).startsWith("media-video-play")) return child;
            count[0]++;
            return withField(child, DISPLAYED_FIELD, displayed);
        });
        Settings.debugLog("viewer tap to hide: video, " + count[0] + " parts follow the play button");
        return count[0] > 0 ? edited : null;
    }

    /** A tap on the document flips the state its top bar and bottom part already follow. */
    static byte[] document(byte[] container) {
        byte[] displayed = null;
        for (byte[] child : children(container)) {
            byte[] d = field(child, DISPLAYED_FIELD);
            if (d != null && field(d, BINDABLE_BOOLEAN_FIELD) != null) displayed = d;
        }
        if (displayed == null) return null;
        final byte[] toggle = toggleTrigger(displayed);
        if (toggle == null) return null;
        final int[] count = {0};
        byte[] edited = mapChildren(container, child -> {
            if (!"feed-document-container".equals(viewName(child))) return child;
            count[0]++;
            return withClickTrigger(child, toggle);
        });
        Settings.debugLog("viewer tap to hide: document, " + count[0] + " tap targets");
        return count[0] > 0 ? edited : null;
    }

    /**
     * Trigger { action: { compactData: { SetState { state: { key, expression: { boolean: not(binding) } } } } },
     * click }. The binding is read once (not reactive), or the state would keep flipping itself.
     */
    static byte[] toggleTrigger(byte[] displayed) {
        byte[] bindable = field(displayed, BINDABLE_BOOLEAN_FIELD);
        byte[] key = bindable == null ? null : field(bindable, STATE_KEY_FIELD);
        if (key == null) return null;
        byte[] current = ProtoEdit.write(java.util.Collections.singletonList(
                new ProtoEdit.Field(BINDABLE_BOOLEAN_FIELD, 2, bindable)));
        ProtoEdit.Field state = ProtoEdit.message(3,
                new ProtoEdit.Field(STATE_KEY_FIELD, 2, key),
                ProtoEdit.message(13,
                        ProtoEdit.message(3,
                                ProtoEdit.message(NOT_FIELD,
                                        new ProtoEdit.Field(1, 2, current)))));
        ProtoEdit.Field trigger = ProtoEdit.message(TRIGGERS_FIELD,
                ProtoEdit.message(1,
                        ProtoEdit.message(5,
                                ProtoEdit.varintField(1, SET_STATE_TYPE),
                                ProtoEdit.message(2, state))),
                ProtoEdit.string(TRIGGER_CLICK_FIELD, ""));
        return trigger.data;
    }

    /** Replaces the component's click triggers with the given one, keeping its other triggers. */
    private static byte[] withClickTrigger(byte[] component, byte[] trigger) {
        List<ProtoEdit.Field> fields = ProtoEdit.parse(component);
        if (fields == null) return component;
        List<ProtoEdit.Field> out = new ArrayList<>();
        for (ProtoEdit.Field f : fields) {
            if (f.number == TRIGGERS_FIELD && f.wireType == 2 && field(f.data, TRIGGER_CLICK_FIELD) != null) continue;
            out.add(f);
        }
        out.add(new ProtoEdit.Field(TRIGGERS_FIELD, 2, trigger));
        return ProtoEdit.write(out);
    }

    private static byte[] withField(byte[] message, int number, byte[] data) {
        List<ProtoEdit.Field> fields = ProtoEdit.parse(message);
        if (fields == null) return message;
        fields.add(new ProtoEdit.Field(number, 2, data));
        return ProtoEdit.write(fields);
    }

    // region Component tree

    static String viewName(byte[] component) {
        byte[] tracking = field(component, VIEW_TRACKING_FIELD);
        byte[] name = tracking == null ? null : field(tracking, 1);
        return name == null ? "" : new String(name, java.nio.charset.StandardCharsets.UTF_8);
    }

    /** The first length-delimited field with this number, or null. */
    static byte[] field(byte[] message, int number) {
        List<ProtoEdit.Field> fields = ProtoEdit.parse(message);
        if (fields == null) return null;
        for (ProtoEdit.Field f : fields) if (f.number == number && f.wireType == 2) return f.data;
        return null;
    }

    static boolean isComponent(byte[] bytes) {
        byte[] compact = field(bytes, COMPACT_DATA_FIELD);
        if (compact == null) return false;
        List<ProtoEdit.Field> fields = ProtoEdit.parse(compact);
        if (fields == null) return false;
        for (ProtoEdit.Field f : fields) if (f.number == 1 && f.wireType == 0) return true;
        return false;
    }

    /** The components directly inside a component (in its compact data, through the messages that hold them). */
    static List<byte[]> children(byte[] component) {
        List<byte[]> out = new ArrayList<>();
        mapChildren(component, child -> {
            out.add(child);
            return child;
        });
        return out;
    }

    /** Edits the components directly inside a component; returns the component, rebuilt when one changed. */
    static byte[] mapChildren(byte[] component, ComponentEditor editor) {
        List<ProtoEdit.Field> fields = ProtoEdit.parse(component);
        if (fields == null) return component;
        boolean changed = false;
        for (ProtoEdit.Field f : fields) {
            if (f.number != COMPACT_DATA_FIELD || f.wireType != 2) continue;
            List<ProtoEdit.Field> compact = ProtoEdit.parse(f.data);
            if (compact == null) continue;
            for (ProtoEdit.Field c : compact) {
                if (c.number != 2 || c.wireType != 2) continue;
                byte[] payload = mapComponents(c.data, editor, 0);
                if (payload != c.data) {
                    c.data = payload;
                    f.data = ProtoEdit.write(compact);
                    changed = true;
                }
            }
        }
        return changed ? ProtoEdit.write(fields) : component;
    }

    private static byte[] mapComponents(byte[] message, ComponentEditor editor, int hops) {
        List<ProtoEdit.Field> fields = ProtoEdit.parse(message);
        if (fields == null) return message;
        boolean changed = false;
        for (ProtoEdit.Field f : fields) {
            if (f.wireType != 2 || f.data.length < 2) continue;
            byte[] edited = isComponent(f.data) ? editor.edit(f.data)
                    : hops < 4 ? mapComponents(f.data, editor, hops + 1) : f.data;
            if (edited != f.data) {
                f.data = edited;
                changed = true;
            }
        }
        return changed ? ProtoEdit.write(fields) : message;
    }

    // endregion
}
