package app.loki.extension;

import java.util.ArrayList;
import java.util.List;

/** IMO: removes the voice-rooms tab from the Home tab list. Never throws; on any doubt the list is left alone. */
public final class LokiTabs {
    /** IMO's id for the voice-rooms ("clubhouse") tab; chats are 0, calls 4 and contacts 3. */
    private static final int VOICE = 2;

    private LokiTabs() {
    }

    /** The newer Home layout: its tab model holds enum values, and the voice-rooms one is named CLUBHOUSE. */
    public static List<?> withoutClubhouse(List<?> tabs) {
        try {
            if (tabs == null) return null;
            if (!LokiSwitches.imoOn(LokiSwitches.IMO_VOICE)) return tabs;
            ArrayList<Object> kept = new ArrayList<>(tabs.size());
            for (Object tab : tabs) {
                if (tab == null || !"CLUBHOUSE".equals(String.valueOf(tab))) kept.add(tab);
            }
            return kept.isEmpty() ? tabs : kept;
        } catch (Throwable error) {
            return tabs;
        }
    }

    /**
     * IMO's suggestion strip above the chat box (dice, lips, rose... one tap sends it). Every visibility change the
     * strip's own class makes comes through here: with the switch on, the strip (a LinearLayout) stays gone; anything
     * else, and everything with the switch off, is passed on unchanged. Never throws.
     */
    public static void suggestionVisibility(android.view.View view, int visibility) {
        int wanted = visibility;
        try {
            if (view instanceof android.widget.LinearLayout && LokiSwitches.imoOn(LokiSwitches.IMO_SUGGESTIONS)) {
                wanted = android.view.View.GONE;
            }
        } catch (Throwable ignored) {
            // Fall back to what IMO asked for.
        }
        if (view != null) view.setVisibility(wanted);
    }


    public static List<Integer> withoutVoice(List<Integer> tabs) {
        try {
            if (tabs == null) return null;
            if (!LokiSwitches.imoOn(LokiSwitches.IMO_VOICE)) return tabs;
            ArrayList<Integer> kept = new ArrayList<>(tabs.size());
            for (Integer tab : tabs) {
                if (tab == null || tab.intValue() != VOICE) kept.add(tab);
            }
            return kept.isEmpty() ? tabs : kept;
        } catch (Throwable error) {
            return tabs;
        }
    }
}
