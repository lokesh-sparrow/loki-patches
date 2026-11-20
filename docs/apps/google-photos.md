# Google Photos

**Patch file:** APKM (bundle — base plus ABI and density splits)
**Supported versions:** 8.1.0 (others can be tried with *Experimental*)
**Where Loki Patches lives:** the account menu — tap your avatar in the top right

## Patches

| Patch | Default | Switch | What it does |
|---|---|---|---|
| Unlimited backup (Pixel XL) | On | Yes (one switch for both halves) | One switch, two halves that only work together: Photos is told it holds the original Pixel XL entitlement, **and** every upload reports this phone as a Pixel XL, so Google marks it free. A newly backed-up photo then reads "This item doesn't take up space in your account storage". Google lists the phone as a Pixel XL under your account's devices. Creations Google renders on its servers (memories, collages) still use your quota |
| Only real camera photos count as Camera | On | Yes | Photos treats everything under `DCIM` as camera roll, so screenshots and other apps' DCIM folders are uploaded with Camera and cannot be switched off. With this on, only `DCIM/Camera` counts and the rest become ordinary folders you choose in Photos › Backup › Backup device folders |
| GmsCore support (Google Photos) | On | No | Sign-in and push through MicroG-RE, and the "This device isn't Play Protect certified" screen is never shown. MicroG-RE must be installed and signed in to the same Google account |
| Install next to the original | On | No | The patched app installs as `com.google.android.apps.photos.loki` beside the preinstalled Photos. Providers and permissions are renamed so Android accepts both; nothing has to be uninstalled and the stock app keeps working |
| Remove ads and trackers | Off | — | General ad and tracker removal, shared by every app in the bundle |
| Block advertising ID | Off | — | Answers the advertising-id call with zeroes |
| Remove analytics and crash reporting | Off | — | Stops the app's own analytics and crash uploads |
| Change package name | Off | — | Installs beside the original app (already the case here; the clone patch uses it) |

## What to expect

- **Photos uploaded before the switch was on stay charged.** Google decides what an upload costs once, at upload time.
- **Existing photos already in your account** are not re-evaluated.
- **The phone shows as a Pixel XL** in your Google account's device list. That is the identity every upload reports.
- **Camera folders:** with the camera patch on, Screenshots and other DCIM folders appear as separate folders in Photos' own backup settings instead of being uploaded as Camera.
- **The patched app does not replace the stock one**, so both can be installed. Sign in inside the patched app only; using both to upload will charge the stock app's uploads.

## Not included, on purpose

- **Server-side storage waivers** for creations Google renders (memories, collages, animations).
- **Build-property spoofing** of the whole system: the patch only changes what Photos itself reads and sends.
