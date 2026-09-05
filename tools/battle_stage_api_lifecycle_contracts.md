# Battle stage API lifecycle contracts

Recovered-JS callback contracts for the six APIs currently blocked by the surface audit. Line numbers and body hashes in the JSON make source drift detectable.

## `stage.info`

Source functions: `ui/StageLayer.js:311 info()`, `ui/StageLayer.js:323 _info5()`, `ui/StageLayer.js:444 info2()`

Eager mutation:

- Reads INFO_CTRL and deletes that global before deciding whether presentation is needed.

Pause: Only when !_skip and INFO_CTRL is zero; calls _script.pause() before _info5.

Resume: _info5 waits for each InfoLayer callback, then its final callback calls _script.resume(). Remaining chunks are skipped if _skip becomes true, but the final callback still runs.

Asset/failure path: No asset load is initiated by these functions. If InfoLayer never invokes func, its Promise remains pending and resume is unreachable.

Branches:

- _skip: no pause or presentation
- INFO_CTRL != 0: delegates to model.info without pausing
- normal: sequential <=~100-character InfoLayer chunks under one pause

## `stage.loadBg`

Source functions: `battle/BattleLayer.js:2948 loadBg()`, `battle/BattleLayer.js:2848 _loadBg()`

Eager mutation:

- If t < 0 or JUMP_OFFSET != 0, clears JUMP_OFFSET and rewrites the map index before pausing.
- After MiniMapLayer callback, writes BG_INDEX before JSON/texture loads complete.

Pause: Unconditional at entry, before _loadBg.

Resume: Only the success tail of _loadBg invokes the supplied callback; loadBg's callback calls resume(). Success requires MiniMapLayer callback, map JSON, map texture, and (flag 1) Promise.all unit avatar initialization.

Asset/failure path: JSON err returns break with status 1 and texture err returns break with status 2 before the callback, so the script remains paused. A missing MiniMapLayer callback or rejected avatar Promise also prevents resume; there is no finally fallback.

Branches:

- jump-offset normalization occurs before pause
- flag 1 refreshes all unit avatars before callback

## `stage.setRectUnitHide`

Source functions: `battle/BattleLayer.js:5474 setRectUnitHide()`, `battle/BattleLayer.js:9732 ctrlUnitHide()`, `battle/BattleLayer.js:7033 unitHide()`

Eager mutation:

- No unit mutation occurs before pause. During the paused coroutine each unit is centered, marked RETREAT, HP is set to zero, and the authored hide action starts; after its callback visibility is cleared, HP restored, and retreat count may increment.

Pause: Only when the rectangle search returns at least one unit; empty selection returns synchronously.

Resume: ctrlUnitHide invokes its completion callback after serial unitHide completion for every unit. Each unit advances on optional retreat-dialogue callback and setAction2 completion callback.

Asset/failure path: No asset load. Missing dialogue/action callback, generator exception, or rejected child operation prevents the final callback; there is no finally resume.

Branches:

- multiple units are sorted before pause
- BAI_TUI can wait for retire dialogue and converts the self master to death animation

## `stage.setStageName`

Source functions: `ui/StageLayer.js:237 setStageName()`, `ui/StageLayer.js:444 info2()`

Eager mutation:

- Model.setStageName(t) runs first, before any presentation or pause.

Pause: Only when !_skip && _isDraw.

Resume: InfoLayer receives resume.bind(this) as func; resume occurs only when that layer invokes the callback.

Asset/failure path: No direct asset load. Failure to create/complete InfoLayer or invoke func leaves the script paused; no fallback is present.

Branches:

- skip or not-yet-drawn: model mutation only, synchronous return

## `stage.setUnitAttr`

Source functions: `battle/BattleLayer.js:11167 setUnitAttr()`, `ui/StageLayer.js:954 setUnitAttr()`, `battle/BattleLayer.js:11246 _loadAvatar()`, `battle/BattleUnit.js:290 loadAvatar()`, `framework/UILayer.js:244 loadUnitPicture()`

Eager mutation:

- Battle-specific X/Y/action/direction/HP/MP/status branches mutate the live BattleUnit synchronously.
- The default branch first calls StageLayer.setUnitAttr, which updates model/base or additive attributes synchronously; equipment is then updated synchronously.
- S_AVATAR/POSTS call _loadAvatar only after the model mutation and only when a live battle unit exists.

Pause: Only the live-unit S_AVATAR/POSTS path pauses, inside _loadAvatar before awaiting BattleUnit.loadAvatar. Other attributes and absent live units return without pause.

Resume: _loadAvatar resumes immediately after the awaited loadAvatar resolves; it does not use try/finally.

Asset/failure path: Normal cc.loader/loadByUrl errors are converted by loadUnitPicture into resolved {code,data} entries, so loadAvatar still sets the avatar (possibly from a partial/empty array), runs defaultAction, and _loadAvatar resumes. A thrown/rejected Promise bypasses resume and strands the pause.

Branches:

- INDEX is ignored
- equipment WQ/HJ/FZ updates equipItem
- only S_AVATAR/POSTS trigger avatar refresh

## `stage.unit().setPosts`

Source functions: `battle/BattleUnit.js:2463 setPosts()`, `battle/BattleUnit.js:285 testAvatar()`, `battle/BattleUnit.js:290 loadAvatar()`, `framework/UILayer.js:244 loadUnitPicture()`

Eager mutation:

- _unit.setPosts(t,e) always mutates model state before avatar testing, pause, or load.

Pause: Only when flag bit 16 is set and testAvatar() reports an attached node whose computed avatar differs. Bit 16 with no change/node returns after mutation without pause; other flags start loadAvatar without waiting or pausing.

Resume: On the paused branch, loadAvatar's callback resumes after loadUnitPicture resolves, _setAvater runs, and defaultAction is restored.

Asset/failure path: loadUnitPicture converts normal loader errors to resolved code entries and returns available frames, so the callback/resume still occurs (possibly with partial/empty frames). Unexpected rejection/throw prevents callback and resume. testAvatar guards the no-node/already-current cases before pausing.

Branches:

- flag 16 + avatar change: blocking reload
- flag 16 + no change: synchronous
- without flag 16: nonblocking fire-and-forget reload
