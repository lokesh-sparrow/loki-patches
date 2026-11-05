## 1.2.1 (2026-11-06)

### What's New
- GmsCore support: a check at startup reports when microG RE is missing, too old, restricted by battery optimization or not running, and opens the download page or the relevant settings. Google sign-in no longer fails silently. The checks are adapted from Hoo-dles' MicroG integration.

### Fixes
- The icon is attached to every release.
- Anonymous profile viewing: the setting and the README describe what it does.
- Removed unused upstream branding and donation strings from the translations.

**22 patches in total.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).

## [1.2.1](https://github.com/lokesh-sparrow/loki-patches/compare/v1.2.0...v1.2.1) (2026-11-05)

### 🐛 Bug Fixes

* **GmsCore support:** tell the user when microG RE is missing, old, restricted or stopped ([315cc75](https://github.com/lokesh-sparrow/loki-patches/commit/315cc758d367da0c446f7ea4ba1bfbc9e6e89430))

## 1.2.0 (2026-11-06)

### What's New
- Anonymous profile viewing (experimental): drops the analytics events of profile pages, so visits are not listed in "Who viewed your profile" or in company page visitor lists. Setting: Privacy.
- AMOLED black bars (new patch): pure black top bar, bottom bar and side menu in dark mode. Requires Material You.
- Hide bottom tabs: switches to remove My Network and Jobs from the bottom bar.
- Swipe between feeds: also switches Grow and Catch up on My Network.
- Hide games: also hides the puzzle section headings.
- Loki settings pages follow AMOLED black in dark mode.
- Supports LinkedIn 4.1.1267 only.

### Fixes
- Who viewed your profile: viewer list restored, Premium cards hidden.
- Side menu Premium bar hidden on 4.1.1267.
- Dialog contrast in Loki pages on light screens in dark mode.

**22 patches in total.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).

## 1.1.1 (2026-11-04)

### Fixes
- Hide Premium upsells: side menu Premium bar hidden on LinkedIn 4.1.1267.

**20 patches in total.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).

## 1.1.0 (2026-11-04)

### What's New
- Swipe between feeds: switches For you and Network on the home feed. Ignored on carousels and multi-page documents.
- Tap to hide in viewers: toggles the caption and action buttons of the full-screen document and video viewers.
- Documents as PDF: saves a post's document or carousel as one PDF.
- Post menu items: Copy text, Download media and Save as PDF in the post menu.
- Copy text: long press to select and copy; long posts open in a scrollable sheet (switchable).
- Keep feed on reopen, Muted words, Hide AI suggestions.
- Supports LinkedIn 4.1.1267.

### Fixes
- Copy text: overlay alignment, scrolling and selection persistence.
- Dialog readability on all phones.
- Hide Premium upsells: compatible with 4.1.1267.
- Credits list only Loki Patches, Morphe and LinkedIn.

**20 patches in total.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).

## 1.0.0 (2026-11-03)

### What's New
- Initial release of Loki Patches for LinkedIn.
- Removes promoted posts and jobs, suggested posts and Premium upsells; optional hiding of counts, games and other feed elements.
- Photo and video download in full screen (single or all media of a post), profile photo and banner download.
- Copy text on long press.
- Open links directly, share link sanitizing, tracking request blocking.
- Messaging options, Material You theming, dedicated settings page.

**Built for LinkedIn 4.1.1261.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).
