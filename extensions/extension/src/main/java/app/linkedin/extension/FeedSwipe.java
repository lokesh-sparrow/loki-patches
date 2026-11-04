package app.linkedin.extension;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

/**
 * Swipe between feeds: on the home feed, swiping left opens Network and swiping right opens For you, like the tabs
 * of X. The tab is tapped through accessibility, so LinkedIn switches it exactly as when it is tapped.
 * <p>
 * Swipes that start on something that scrolls sideways (photo carousels, documents with many pages) are left to it.
 */
@SuppressWarnings("unused")
public final class FeedSwipe {
    private static final long MAX_DURATION = 600;
    /** Pages of a document or photos of a carousel: "Page 3 of 11", "3 of 11" (the feed tabs are "1 of 2"). */
    private static final java.util.regex.Pattern PAGES =
            java.util.regex.Pattern.compile("(?i)\\bpage\\s+\\d+|\\b\\d+\\s+of\\s+(?:[3-9]|\\d{2,})\\b");

    private static float downX;
    private static float downY;
    private static long downTime;
    private static boolean multiTouch;

    private FeedSwipe() {
    }

    /** Injection point, at the start of AndroidComposeView.dispatchTouchEvent. Only watches the touch. */
    public static void onTouch(View view, MotionEvent event) {
        try {
            if (!Settings.swipeFeeds() || !SduiMediaDownload.resumedScreen().endsWith(".MainFeed")) return;
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getRawX();
                    downY = event.getRawY();
                    downTime = event.getEventTime();
                    multiTouch = false;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    multiTouch = true;
                    break;
                case MotionEvent.ACTION_UP:
                    if (multiTouch || TextSelectionOverlay.isActive()) return;
                    float dx = event.getRawX() - downX;
                    float dy = event.getRawY() - downY;
                    if (event.getEventTime() - downTime > MAX_DURATION) return;
                    if (Math.abs(dx) < view.getRootView().getWidth() * 0.25f || Math.abs(dx) < Math.abs(dy) * 2.5f) {
                        return;
                    }
                    // Swiping left goes to the tab on the right, like turning a page.
                    switchTo(view, dx < 0 ? 1 : 0, (int) downX, (int) downY);
                    break;
                default:
                    break;
            }
        } catch (Throwable t) {
            Diagnostics.error("feed swipe failed", t);
        }
    }

    /** Taps tab 0 (For you) or 1 (Network), unless the swipe started on a carousel or the tab is open already. */
    private static void switchTo(View view, int tab, int startX, int startY) throws ReflectiveOperationException {
        AccessibilityNodeProvider provider = CopyTextPatch.provider(view);
        int[] ids = CopyTextPatch.semanticsNodeIds(view);
        if (provider == null || ids == null) return;
        Rect bounds = new Rect();
        int target = -1;
        boolean targetSelected = false;
        for (int id : ids) {
            AccessibilityNodeInfo info = provider.createAccessibilityNodeInfo(id);
            if (info == null) continue;
            if (scrollsSideways(info) || PAGES.matcher(label(info)).find()) {
                info.getBoundsInScreen(bounds);
                if (bounds.contains(startX, startY)) {
                    Settings.debugLog("feed swipe: left to a carousel or document");
                    return;
                }
            }
            int index = tabIndex(info);
            if (index == tab && info.isClickable() && info.isVisibleToUser()) {
                target = id;
                targetSelected = isSelected(info);
            }
        }
        if (target == -1) {
            Settings.debugLog("feed swipe: tabs not on screen");
            return;
        }
        if (targetSelected) return;
        boolean clicked = provider.performAction(target, AccessibilityNodeInfo.ACTION_CLICK, null);
        Settings.debugLog("feed swipe: " + (tab == 0 ? "For you" : "Network") + (clicked ? "" : " (not clicked)"));
    }

    /** 0 for the For you tab, 1 for Network, -1 for anything else. Their descriptions: "For you, 1 of 2". */
    private static int tabIndex(AccessibilityNodeInfo info) {
        String label = label(info);
        if (label.startsWith("For you") || label.contains(", 1 of 2")) return 0;
        if (label.startsWith("Network") || label.contains(", 2 of 2")) return 1;
        return -1;
    }

    private static boolean isSelected(AccessibilityNodeInfo info) {
        return info.isSelected() || info.isChecked() || label(info).contains("selected");
    }

    private static String label(AccessibilityNodeInfo info) {
        CharSequence description = info.getContentDescription();
        CharSequence text = info.getText();
        return description != null ? description.toString() : text != null ? text.toString() : "";
    }

    private static boolean scrollsSideways(AccessibilityNodeInfo info) {
        if ("android.widget.HorizontalScrollView".contentEquals(info.getClassName() == null ? "" : info.getClassName())) {
            return true;
        }
        for (AccessibilityNodeInfo.AccessibilityAction action : info.getActionList()) {
            int id = action.getId();
            if (id == android.R.id.accessibilityActionScrollLeft || id == android.R.id.accessibilityActionScrollRight
                    || id == android.R.id.accessibilityActionPageLeft
                    || id == android.R.id.accessibilityActionPageRight) {
                return true;
            }
        }
        return false;
    }
}
