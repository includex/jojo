# Platform factory routing

The recovered platform code is retained as the behavioral authority.  Desktop
does not fabricate Android SDK behavior:

- `PrivacyLayer`: `initAdSdk` is an Android-side boundary; the desktop trace
  records only the original `Privacy.txt` write, callback, and END_GAME route.
- `StatementLayer`, `VersionInfoLayer`: fully desktop-reachable and traced.
- `InstallLayer`: its Android-only `_canLaunch` branch is false on desktop;
  native directory/install/unzip actions remain unreachable.  On-create button
  state and remove lifecycle are traced.
- `HotUpdateLayer`: downloader/assets-manager callbacks are native boundary
  paths.  The deterministic button bitmask, INI parsing, day comparison, and
  `UPDATE_SUCC`/remove lifecycle are traced; no fake updater is introduced.
- `Login`: desktop has no SDK login (`haveLogin=false`), so device-id and SDK
  login calls are boundaries.  The recovered floor map and access gate are
  traced directly; Android interstitial reflection is unreachable.
- `SdkBase`: desktop is *not* a no-op: Win32 calls the original `getMac1`
  helper boundary, filters VMware-style addresses and saves the MD5 device id.
  Cached id and helper-error paths are also traced.
- `Taptap`: its class reports `haveLogin=true`, but Manager's desktop startup
  creates `SdkBase`, not `Taptap`; the factory contract is traced separately.
- `VideoLayer`: desktop-reachable overlay.  The original URL loader remains a
  boundary; both load failure and actual VideoPlayer clip/play plus end-event
  callback/remove paths are traced.
