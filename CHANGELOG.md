## 1.2.0 (2026-11-06)

### What's New
- Anonymous profile viewing now works. With it on (Settings, Privacy), the profiles you open no longer show you in the other person's "Who viewed your profile" list or in a company page's visitor list. LinkedIn counts a visit from the analytics of the profile page, which Loki now leaves out. It is marked experimental because LinkedIn could change how it counts visits in a future update.
- AMOLED black bars: a new patch that makes the dark top bar, bottom bar and side menu pure black, next to the feed that AMOLED black already covered. Select it with Material You when patching.
- Hide bottom tabs: switches in Loki settings to take My Network and Jobs out of the bottom bar. Restart LinkedIn after changing them.
- Swipe between feeds also works on My Network: swipe left for Catch up and right for Grow.
- Hide games now also hides the heading above the puzzle cards, "Wind down with a puzzle" and "Today's puzzles".
- Loki's own settings page follows AMOLED black in dark mode.
- This release is made for LinkedIn 4.1.1267, the latest version.

### Fixes
- Who viewed your profile shows the list of viewers again, and hides the Premium cards between the rows.
- The Premium bar in the side menu is hidden on LinkedIn 4.1.1267.
- Dialogs in Loki's pages are readable on LinkedIn's light screens in dark mode.

**22 patches in total.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).

## [1.2.0](https://github.com/lokesh-sparrow/loki-patches/compare/v1.1.1...v1.2.0) (2026-11-05)

### 🐛 Bug Fixes

* **Anonymous profile viewing:** drop every analytics event of a profile page ([2cd1f0c](https://github.com/lokesh-sparrow/loki-patches/commit/2cd1f0c53863486dea936922818d5a3c6cb317c0))
* **Anonymous profile viewing:** drop the profile view event where every event goes through ([517856f](https://github.com/lokesh-sparrow/loki-patches/commit/517856fb09b1d2a6409b06d093fbac78eb3c57f4))
* **Anonymous profile viewing:** request profile screens as a prefetch ([f31cecc](https://github.com/lokesh-sparrow/loki-patches/commit/f31ceccb6dc5b4245b259399e1f19428ad2de9d3))
* **Hide Premium upsells:** hide the small cards in Who viewed your profile, keep the sheet parts ([d44010f](https://github.com/lokesh-sparrow/loki-patches/commit/d44010f6ad3363d84c73ae7a95844f959c44e843))
* **Hide Premium upsells:** keep the viewer list in Who viewed your profile ([a29e166](https://github.com/lokesh-sparrow/loki-patches/commit/a29e1667b3401ccab7203daede47c1f6a7b8b725))
* **LinkedIn:** hide the games heading, find the My Network tabs when they are not Compose ([491328d](https://github.com/lokesh-sparrow/loki-patches/commit/491328d446a93c10923cd5ef31c001d19a785f76))
* **Settings:** Loki's pages follow AMOLED black on dark ([9a51fff](https://github.com/lokesh-sparrow/loki-patches/commit/9a51fff34c9d85fc8a6a8166d4624d96b885ef8f))
* **Swipe between feeds:** swipe right from Catch up to Grow on My Network ([a666b87](https://github.com/lokesh-sparrow/loki-patches/commit/a666b8718f23f8425992f40884b496e92f7af7ba))

### ✨ New Features

* **LinkedIn:** AMOLED black bars ([5915ed1](https://github.com/lokesh-sparrow/loki-patches/commit/5915ed15c2b2894312531e4e395018c132c2c2bf))
* **LinkedIn:** Hide bottom tabs, swipe between Grow and Catch up ([48bd678](https://github.com/lokesh-sparrow/loki-patches/commit/48bd678dab6b4cf37e56b9ca82dcd8eee855143e))

## 1.1.1 (2026-11-04)

### Fixes
- The "Activate Premium" bar at the bottom of the side menu is hidden again on LinkedIn 4.1.1267. Hide Premium upsells removed it on 4.1.1261, but LinkedIn moved that code in 4.1.1267, so the bar came back.

**20 patches in total.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).

## 1.1.0 (2026-11-04)

### What's New
- Swipe between feeds: on the home feed, swipe left for Network and right for For you, like on X. It works while the For you and Network tabs are on screen. Swipes on photo carousels and on documents with many pages still flip through them as before.
- Tap to hide in viewers: tap a document or a video in full screen to hide the caption and the like, comment and share buttons, and tap again to bring them back, the way photos already worked.
- Documents as PDF: save a post's document or carousel as one PDF from the download button or the three-dot menu. LinkedIn's own download button in the document viewer is hidden, so there is one button on photos, videos and documents.
- Post menu items: the three-dot menu of a post now has Copy text, Download photos and videos, and Save document as PDF.
- Copy text: long press any text to select and copy it, like on Reddit. Long posts open in a scrollable sheet with the whole text, and there is a switch if you prefer to select them in place.
- Keep feed on reopen, muted words, anonymous profile viewing (experimental) and hiding of AI suggestions are included.
- LinkedIn 4.1.1267 is supported, next to 4.1.1255.1, 4.1.1259 and 4.1.1261.

### Fixes
- LinkedIn could close right after opening with an earlier test build. This release opens normally.
- Copy text lines up with the post, scrolls with it and stays selected while you scroll.
- Loki's dialogs, such as Choose media, are readable on every phone, also on light LinkedIn screens in dark mode.
- Hide Premium upsells works on LinkedIn 4.1.1267.
- The credits in Loki settings list only Loki Patches, Morphe and LinkedIn.

**20 patches in total.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).

## 1.0.0 (2026-11-03)

### What's New
- The first release of Loki Patches, a set of LinkedIn patches for Morphe Manager. No ads, no clutter.
- Removes promoted posts, promoted jobs, suggested posts and Premium upsells, and can hide the counts, the games section and other feed clutter.
- Downloads photos and videos in full screen, one or all of a post, plus profile photos and banners.
- Copy text: long press any text to select and copy it.
- Opens links directly without LinkedIn's warning page, cleans tracking from shared links and blocks tracking requests.
- Messaging options, a Material You look and a settings page of its own, opened from the LinkedIn icon's long press menu.

**Made for LinkedIn 4.1.1261.** Full list in the [README](https://github.com/lokesh-sparrow/loki-patches#patches).
