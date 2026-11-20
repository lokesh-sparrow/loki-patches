/*
 * Copyright (C) 2026 Lokesh Sparrow
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package app.linkedin.patches.feed

import app.linkedin.patches.shared.Constants.COMPATIBILITY_LINKEDIN
import app.linkedin.patches.shared.Constants.EXTENSION_PACKAGE
import app.linkedin.patches.shared.markIncluded
import app.linkedin.patches.shared.sduiComponentFilterPatch
import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel

private const val SETTINGS = "$EXTENSION_PACKAGE/Settings;"
private const val UPSELL_SLOT =
    "Lcom/linkedin/android/pegasus/dash/gen/voyager/dash/premium/PremiumUpsellSlotContent;"

/*
 * Nothing is hooked for these three: the extension's SduiComponentFilter already drops the components, and
 * these patches only switch that part of the filter on.
 */

@Suppress("unused")
val hideSuggestedPostsPatch = bytecodePatch(
    name = "Hide suggested posts",
    description = "Drops the \"Suggested\" posts from people outside your network.",
    default = true
) {
    compatibleWith(COMPATIBILITY_LINKEDIN)
    dependsOn(sduiComponentFilterPatch)
    execute { markIncluded("isHideSuggestedIncluded") }
}

@Suppress("unused")
val hidePromotedJobsPatch = bytecodePatch(
    name = "Hide promoted jobs",
    description = "Drops promoted job listings from the Jobs tab and from job searches.",
    default = true
) {
    compatibleWith(COMPATIBILITY_LINKEDIN)
    dependsOn(sduiComponentFilterPatch)
    execute { markIncluded("isHidePromotedJobsIncluded") }
}

@Suppress("unused")
val feedFiltersPatch = bytecodePatch(
    name = "Feed filters",
    description = "Adds a set of filters that are off until turned on in Loki Patches: focus mode, celebrations, " +
        "job cards, reposts, posts from other people's activity, \"Recommended for you\", video posts, games, " +
        "the \"New posts\" pill and \"See translation\".",
    default = true
) {
    compatibleWith(COMPATIBILITY_LINKEDIN)
    dependsOn(sduiComponentFilterPatch)
    execute { markIncluded("isFeedFiltersIncluded") }
}

/**
 * HomeNavPanelAggregateResponse(PremiumUpsellSlotContent, ...): the response that builds the Me panel, whose
 * "Try Premium" card comes from the upsell slot. Passing null leaves free members without it, while actual
 * Premium members still get their "Access my Premium" row.
 */
private val mePanelResponse = Fingerprint(
    definingClass = "Lcom/linkedin/android/home/navpanel/HomeNavPanelAggregateResponse;",
    name = "<init>",
    custom = { method, _ ->
        method.parameterTypes.size == 4 && method.parameterTypes[1].toString() == UPSELL_SLOT
    },
)

/**
 * UniversalNavigationController.navigate(int, Bundle, NavOptions): every in-app navigation, including the server
 * driven full screen Premium page (premium_upsell_full_page_takeover) that LinkedIn opens on its own after launch.
 */
private val universalNavigate = Fingerprint(
    definingClass = "Lcom/linkedin/android/infra/navigation/UniversalNavigationController;",
    name = "navigate",
    returnType = "V",
    parameters = listOf("I", "Landroid/os/Bundle;", "Lcom/linkedin/android/infra/navigation/NavOptions;"),
)

@Suppress("unused")
val hidePremiumUpsellsPatch = bytecodePatch(
    name = "Hide Premium upsells",
    description = "Drops the Premium and AI upsell cards and banners, on profiles and in the Me panel too.",
    default = true
) {
    compatibleWith(COMPATIBILITY_LINKEDIN)
    dependsOn(sduiComponentFilterPatch)
    execute {
        markIncluded("isHidePremiumIncluded")

        // Clearing the slot here still works after LinkedIn moved the code that reads it (4.1.1267). The
        // extension does the work because the constructor has no register free for it.
        mePanelResponse.method.addInstructions(
            0,
            """
                invoke-static/range { p2 .. p2 }, $SETTINGS->navPanelUpsellSlot(Ljava/lang/Object;)Ljava/lang/Object;
                move-result-object p2
                check-cast p2, $UPSELL_SLOT
            """
        )

        // Never open the full screen Premium page: with its only component hidden it would flash an empty
        // (black in dark mode) page before PremiumTakeover closes it again.
        universalNavigate.method.apply {
            addInstructionsWithLabels(
                0,
                """
                    invoke-static/range { p2 .. p2 }, $EXTENSION_PACKAGE/PremiumTakeover;->blocksNavigation(Landroid/os/Bundle;)Z
                    move-result v0
                    if-eqz v0, :navigate
                    return-void
                """,
                ExternalLabel("navigate", getInstruction(0))
            )
        }
    }
}
