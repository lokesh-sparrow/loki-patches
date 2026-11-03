package app.linkedin.patches.feed

import app.linkedin.patches.shared.Constants.COMPATIBILITY_LINKEDIN
import app.linkedin.patches.shared.Constants.EXTENSION_PACKAGE
import app.linkedin.patches.shared.markIncluded
import app.linkedin.patches.shared.settingsPatch
import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

private const val EXPANSION_STATE = "Lcom/linkedin/sdui/viewdata/text/ExpansionBehaviorState;"

/**
 * ExpandableTextState(initialText, ellipsisText, collapsedText, collapsedMaxLines, onlyShowMoreWhenNecessary,
 * initial ExpansionBehaviorState): the "...more" text of the server driven UI.
 */
private object ExpandableTextStateFingerprint : Fingerprint(
    definingClass = "Lcom/linkedin/android/infra/sdui/components/text/expandableText/ExpandableTextState;",
    name = "<init>",
    custom = { method, _ -> method.parameterTypes.lastOrNull()?.toString() == EXPANSION_STATE },
)

@Suppress("unused")
val showFullTextPatch = bytecodePatch(
    name = "Show full posts",
    description = "Shows the whole text of posts and comments instead of cutting it off with \"...more\".",
    default = true
) {
    compatibleWith(COMPATIBILITY_LINKEDIN)
    dependsOn(settingsPatch)

    execute {
        markIncluded("isShowFullTextIncluded")

        ExpandableTextStateFingerprint.method.apply {
            val state = "p${parameterTypes.size}"
            // The constructor has no local registers to spare (v0 is `this`), so the state goes through the
            // extension and comes back in its own register.
            addInstructions(
                0,
                """
                    invoke-static/range { $state .. $state }, $EXTENSION_PACKAGE/LokiPatches;->expansionState(Ljava/lang/Object;)Ljava/lang/Object;
                    move-result-object $state
                    check-cast $state, $EXPANSION_STATE
                """
            )
        }
    }
}
