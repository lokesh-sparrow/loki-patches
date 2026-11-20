<!-- HEADER_START -->
# <img src="../../assets/icons/com.google.android.apps.photos.png" width="36" height="36" align="top" alt=""> Google Photos

![version](https://img.shields.io/badge/version-8.1.0.1023737660-2563eb?style=for-the-badge&labelColor=0d1117) ![patches](https://img.shields.io/badge/patches-4-14b8a6?style=for-the-badge&labelColor=0d1117) ![file](https://img.shields.io/badge/file-APKM%20%28Android%208%2B%29-0ea5e9?style=for-the-badge&labelColor=0d1117) ![menu](https://img.shields.io/badge/menu-Account%20menu-6e7681?style=for-the-badge&labelColor=0d1117)

[← All apps](../../README.md#apps)&nbsp;&nbsp;•&nbsp;&nbsp;`com.google.android.apps.photos`&nbsp;&nbsp;•&nbsp;&nbsp;[v3.4.0](https://github.com/lokesh-sparrow/loki-patches/releases/tag/v3.4.0)

**🎯 Supported versions:** `8.1.0.1023737660`
<!-- HEADER_END -->

## Features

| Feature | What you get |
|---|---|
| 💾 **Unlimited backup** | Photos and videos back up at original quality without counting against your Google storage. A newly backed-up photo reads "This item doesn't take up space in your account storage". |
| 📷 **Real camera folder** | Only `DCIM/Camera` counts as Camera. Screenshots and other apps' DCIM folders become ordinary folders you choose in Photos › Backup › Backup device folders. |
| 🔑 **Google sign-in** | Sign-in and notifications go through MicroG-RE; the "This device isn't Play Protect certified" screen is never shown. |
| 👯 **Next to the original** | Installs as `com.google.android.apps.photos.loki` beside the preinstalled Photos; nothing has to be uninstalled. |

**Where Loki Patches lives:** the account menu - tap your avatar in the top right.

## What to expect

- **Photos uploaded before the patch stay charged.** Google decides what an upload costs once, at upload time; existing photos are not re-evaluated.
- **The phone shows as a Pixel XL** in your Google account's device list. That is the identity every upload reports.
- **Creations Google renders on its servers** (memories, collages, animations) still use your quota.
- **Sign in inside the patched app only.** Uploads made by the stock app are charged as usual.
- **Background backup on Samsung:** set the patched Photos to *Unrestricted* battery use and keep it out of *Sleeping apps*, or backup may wait until you open the app.

## Questions

**Do I need MicroG-RE?**
Yes. Install MicroG-RE and sign in to the same Google account before opening the patched Photos.

**Is the photo file changed?**
No. The camera still writes your real phone model into each photo's EXIF data; only the upload handshake reports a Pixel XL.

## Patches

<!-- PATCHES_START -->
<!-- PATCHES_END -->
