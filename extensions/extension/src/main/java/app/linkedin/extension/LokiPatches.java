package app.linkedin.extension;

/** Injection points of Show full posts and Anonymous profile viewing. */
@SuppressWarnings("unused")
public final class LokiPatches {
    private LokiPatches() {
    }

    private static Object expanded;
    /** Whether the text being composed belongs to the screen around it (a caption in the media viewer). */
    private static boolean leaveCollapsed;

    /**
     * Injection point, where the feeds read when the app last went to the background. With Keep feed on reopen
     * the answer is "just now", so neither the server driven feed's rule nor the classic 7 minute rule reloads it.
     */
    public static long feedBackgroundTime(long timestamp) {
        return Settings.keepFeed() && timestamp > 0 ? System.currentTimeMillis() : timestamp;
    }

    /**
     * Injection point, at the start of ExpandableTextState's constructor, with its initial ExpansionBehaviorState.
     *
     * @return EXPANDED instead of "...more" when Show full posts is on, otherwise the state unchanged.
     */
    public static Object expansionState(Object state) {
        try {
            if (state == null || !Settings.showFullText()) return state;
            if (leaveCollapsed) {
                leaveCollapsed = false;
                return state;
            }
            if (expanded == null) expanded = state.getClass().getField("EXPANDED").get(null);
            return expanded;
        } catch (Throwable t) {
            Diagnostics.error("expansionState failed", t);
            return state;
        }
    }

    /**
     * Injection point, at the start of TextBlock's ExpandableText composable, before it creates its state.
     * <p>
     * Texts with their own "show more" action or a shared expansion state are part of the screen around them: the
     * caption of the photo, video and document viewer. Opening one also runs that action, which keeps the caption
     * and the like and comment bar on screen, so these are left as LinkedIn shows them.
     */
    public static void expandableText(Object textBlock) {
        leaveCollapsed = false;
        try {
            if (textBlock == null || !Settings.showFullText()) return;
            Class<?> c = textBlock.getClass();
            leaveCollapsed = c.getField("showMoreActionListViewData").get(textBlock) != null
                    || c.getField("expansionState").get(textBlock) != null;
            if (leaveCollapsed) Settings.debugLog("show full posts: left a viewer caption collapsed");
        } catch (Throwable t) {
            Diagnostics.error("expandableText failed", t);
        }
    }

    /**
     * Injection point, at the start of ProfileTrackingFeatureImpl.sendCustomProfileViewEvent: the event behind
     * "Who viewed your profile". Skipping it doesn't send the visit, so it doesn't change your own privacy mode.
     */
    public static boolean skipProfileViewEvent() {
        boolean skip = Settings.anonymousProfileViewing();
        if (skip) Settings.debugLog("profile view event not sent");
        return skip;
    }
}
