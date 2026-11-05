package app.linkedin.extension;

/** Injection points of Keep feed on reopen and Anonymous profile viewing. */
@SuppressWarnings("unused")
public final class LokiPatches {
    private LokiPatches() {
    }

    /**
     * Injection point, where the feeds read when the app last went to the background. With Keep feed on reopen
     * the answer is "just now", so neither the server driven feed's rule nor the classic 7 minute rule reloads it.
     */
    public static long feedBackgroundTime(long timestamp) {
        return Settings.keepFeed() && timestamp > 0 ? System.currentTimeMillis() : timestamp;
    }

    /**
     * Injection point, before each return of HomeNavTabsManager.getHomeNavTabs(): the tabs of the bottom bar.
     *
     * @return the tabs without My Network and Jobs when they are hidden. The list is returned as it was when it
     * would end up with fewer than three tabs or the tabs can't be told apart, so the bar can't break.
     */
    public static java.util.List<?> bottomTabs(java.util.List<?> tabs) {
        try {
            boolean network = Settings.hideTabNetwork();
            boolean jobs = Settings.hideTabJobs();
            if (tabs == null || (!network && !jobs)) return tabs;
            java.util.List<Object> kept = new java.util.ArrayList<>();
            for (Object tab : tabs) {
                java.lang.reflect.Field f = tab.getClass().getDeclaredField("trackingControlName");
                f.setAccessible(true);
                Object name = f.get(tab);
                if (network && "nav_people".equals(name)) continue;
                if (jobs && "nav_jobs".equals(name)) continue;
                kept.add(tab);
            }
            return kept.size() >= 3 ? kept : tabs;
        } catch (Throwable t) {
            Diagnostics.error("bottomTabs failed", t);
            return tabs;
        }
    }

    /**
     * Injection point, at the start of ProfileTrackingFeatureImpl.sendCustomProfileViewEvent: the event behind
     * "Who viewed your profile". Skipping it doesn't send the visit, so it doesn't change your own privacy mode.
     */
    public static boolean skipProfileViewEvent() {
        boolean skip = Settings.anonymousProfileViewing();
        Settings.debugLog(skip ? "profile view event not sent"
                : "profile view event sent (Anonymous profile viewing is off)");
        return skip;
    }
}
