package app.linkedin.patches.feed

import app.linkedin.patches.shared.Constants.COMPATIBILITY_LINKEDIN
import app.linkedin.patches.shared.markIncluded
import app.linkedin.patches.shared.sduiComponentFilterPatch
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val viewerTapToHidePatch = bytecodePatch(
    name = "Tap to hide in viewers",
    description = "Tap a video or document in full screen to hide the caption and buttons, like photos.",
    default = true
) {
    compatibleWith(COMPATIBILITY_LINKEDIN)

    // The viewer screens are edited in the SDUI component hook.
    dependsOn(sduiComponentFilterPatch)

    execute { markIncluded("isViewerTapToHideIncluded") }
}
