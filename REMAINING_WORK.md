# Desktop Game Remaining Work

Updated: 2026-09-02 (KST, R_00 street-dialogue staged comparison and packaged-app verification)

## Verification rule

Every item below is complete only when all four artifacts exist and the Gradle
gate is green:

1. Recovered-original JS factory/prototype is run in a minimal Cocos mock.
2. Kotlin direct game is driven by the same fixture.
3. Source and game canonical state/event/output traces are compared exactly.
4. The gate is included in `verifyBehaviorPairwise` and `:core:test` where
   appropriate.

Pixel comparisons are not a substitute for these behaviour contracts. Rendering
comparison starts after the behavioural gate for its owning component is green.

## Current full-HUD visual baseline

The original verifier now accepts the verification-only
`--capture-python-battle-raw-hold-ms=N` option. It pauses immediately before
the existing destructive map-only diagnostic, leaving the normal S_00
BattleScreen untouched for external capture. The paired controller
`.verification-work/raw-framebuffer-common-space/capture_source_battle_hud_live.cjs`
captures that scene through Cocos `RenderTexture`; the game captures its
matching seven-second settle phase with `--capture-state=hud`.

The resulting 2560×1376 raw RGB comparison is MAE 1.4368849 with 457,097
changed pixels and 61,728 alpha mismatches. The source `SHOW_SAY` bubble
placement, six-second avatar tick, and RenderTexture source-over panel alpha
are now applied to the game. The remaining work is to classify and remove the
non-text/non-sampling sprite and HUD residuals, then rerun every interactive
screen transition. This is a valid full-scene baseline, but not a strict-pass
result.

On 2026-09-02, fresh desktop captures for S_00 save, load, settings, helper,
unit-info, and win-condition each matched their paired original RGBA8 capture
exactly (RGB MAE 0, changed pixels 0, alpha mismatches 0). These are target
state checks; the interaction routes that open and close them remain covered
by the pairwise behaviour gate below.

## R_00 street-dialogue visual baseline

The first `R_00.scene1` street dialogue before Yingchuan was compared in the
requested cumulative order: panel, portrait, speaker, full dialogue text,
background, then Hall characters. The original capture mode now disables the
typing scheduler for the full-text stages and records DialogueLayer node,
SpriteFrame, HallUnit, animation, and speech-marker state. This is a live
original render; the game does not reuse the source framebuffer.

The game now uses the original `U_select_11-1` panel, right-side portrait,
36px-equivalent speaker label with its 2px cyan outline, RichText geometry,
opaque-framebuffer blend equation, HallLayer's 2x map scale, both direction
textures from `Pmapobj2`, the `animeRR` action/frame map, and the original
`Mark_10-1` current-speaker marker. The same renderer is used by ordinary
gameplay and capture isolation; their final raw frames are byte-identical.

At 2560x1376 every cumulative stage has zero alpha mismatches. RGB MAE rises
from 0.0299387 (panel) through 0.5575095 (full text) to 3.228388 with the JPEG
background and 4.158253 with characters. The residual is confined to text
rasterization and unbiassed Cocos/LibGDX texture sampling: portrait and marker
bounds are identical, speaker/body bounds differ by at most one pixel, and a
character registration search selects non-flipped `(0,0)` as the best offset.
Exact measurements are stored in
`.verification-work/street-dialogue/final-compare.json`.

The character-stage capture also verifies all four live Hall units against the
original: IDs/directions/actions are `182/2/0`, `0/2/0`, `181/0/0`, and
`157/0/0`; each selects the same `[0,0,48,64]` frame, direction 2 uses
`Pmapobj2.t0`, direction 0 uses `Pmapobj2.t1`, and the rendered size is
`82.56x110.08`. The source and game speaker-marker bounds are identical.

After removing the old battle-dialogue reference-frame shortcut, the ordinary
Kotlin SayLayer was also corrected to the live source panel/portrait parent
position (`+96` logical y). Its three Yingchuan dialogue states now pass the
real source-vs-game actor, name, text, portrait, and geometry gate.

`./gradlew test :desktop:check --no-daemon` passed after the final change
(`508 actionable`, `402 executed`, 8m13s). `package-macos-app.sh` rebuilt
`build/package/JojoLibGDX.app`; launching it directly with the R_00 capture
arguments is byte-identical to the Gradle run for both PNG and raw RGBA. The
PNG SHA-256 is `59708c554d6b4204bb2c2d159534831e1d9d99dfa0173cf9520fa97311bf1c40`
and raw SHA-256 is
`e0f0aee810e23253930d13ec39ea13ec8f4da79f843d366ec879326212221496`.
This baseline proves the named first-dialogue fixture; it does not by itself
certify every later alternating left/right DialogueLayer state in all 119
scenario modules.

## Completed and aggregated behaviour gates

`./gradlew verifyBehaviorPairwise :core:test :desktop:check --no-daemon` was
re-run green in the isolated candidate on 2026-08-31 after 192 actionable
tasks. The root aggregate currently includes these source-vs-game
families:

- Menu, map terrain/minimap/map info, save/load/settings/property/helper.
- Battle presentation, battle bootstrap/constants, enemy turn and remaining
  control branches, magic, battle popups/view/end flows.
- Dialogue/Say, choices/commands, hall preparation/UI, edit mutation,
  item/equip, character ability, progression, system/misc UI.
- Platform, foundation, Config full inventory, Model state/persistence/lifecycle,
  scenario runtime, baseline game-data, full shop/reward flow.
- Newly isolated single-factory gates: `Welcome`, `SendGiftsLayer`,
  `ProgressLayer2`, and `Head`.

The platform reference/game gate now exercises 30 cases, including `StatementLayer`
at negative, 32-bit integer end, 29/30/59/60/150-minute timer boundaries and
non-ending input, plus plain and trailing-delimiter `VersionInfoLayer` text.
Kotlin uses floor division rather than truncating integer division to preserve
JavaScript `Math.floor` behavior for negative timers. It also compares the original
`StatementLayer` countdown registration (`scheduleOnce(time+1)` and
`schedule(1,time,0)`); actual callback ordering remains an explicit engine
scheduler boundary. All candidate Kotlin `JavaExec`
trace tasks declare the checked-in tool JSON set as Gradle inputs, preventing a
fixture edit from reusing a stale game trace. The corresponding original-JS
`Exec` traces now track the same fixture set and the recovered-module source
tree, preventing stale source-side outputs as well. The full behaviour
aggregate was re-run after this wiring change (latest run: 164 tasks executed;
176 actionable tasks total).

Do not interpret an aggregate green result as proof for a module not named by a
source trace task below.

## Behaviour work remaining

### P0: behaviour gates complete in isolation, awaiting approved aggregation

| Module(s) | Required source contract | Status |
|---|---|---|
| `ui/InfoLayer.js` | Rich-text tag-aware reveal, short-text auto-close, scheduled close, touch fast-forward/next, `SKIP`, callback/remove order | Exact isolated reference/game gate green (10 cases, including the observed R_00 Hall first-reveal/close trace); not yet root-aggregated |
| `framework/serviceLayer.js`, `framework/skmLayer.js` | Button event routing, callback/remove ordering, skm flag bit promotion and inverted close event branch | Exact isolated reference/game gate green (15 cases); not yet root-aggregated |
| Item advanced (`game-data/Item.js`) | Free/unit-owned/auxiliary/property ownership, drop/delete, slot attrs, level/EXP, skills/phase cache | Exact isolated reference/game gate green (15 groups; Item/ItemStore surface inventory); not yet root-aggregated |

### P1: behaviour gates complete in isolation, awaiting approved aggregation

- `ui/DefineUnitLayer.js`: exact isolated reference/game gate green (6 cases),
  including validation, confirm/reset mutation and face path.
- `ui/Hall.js`: exact isolated reference/game gate green (16 registry entries,
  5 resource routes and 2 save lifecycles).
- Any `InfoLayer` rendering asset outcome must later be paired with the
  rendering phase, not assumed from its behaviour gate.

### P2: behaviour gates complete in isolation, awaiting approved aggregation

- `framework/UIFrame.js`, `UILayer.js`, `UIScene.js`, `Sound.js`: exact
  isolated reference/game gates are green (12 UI-framework-core cases and the
  corresponding UI-layer/scene/sound gates). Native/delegating boundaries are
  separately enumerated; no monolithic mock gate is being used as evidence.
  UIFrame now additionally has a native-adapter trace (31 operations / 27
  APIs), and UILayer/UIScene have a native-adapter trace covering HTTP-date,
  texture fallback, prefab/tween/widget/modal and audio-shutdown lifecycle.
  These remain isolated evidence until the collision-safe central aggregation.
- `game-data/Unit.js`: exact isolated gate green for the established Unit
  groups. Its inventory explicitly maps every recovered declaration/spelling
  to a trace or native boundary; this is not a claim of a full data Cartesian
  proof.
  The later direct-method reconciliation now maps all **98 / 98** recovered
  declarations to isolated exact gates. Production corpus partitions and
  controlled Model/Item/global adapters remain separately hash-pinned and
  documented; rendering and whole-scenario parity are still outstanding.
  The current production matrix has exact source/Kotlin outputs for the
  decoded 512 units, 80 posts, 157 unit-post skills, 334 defined skills,
  143 magic rows and 40 arms, including 640,000 posts-skill and 51,200
  unit-skill states plus 143×256 magic masks. This remains a finite,
  hash-pinned corpus proof—not arbitrary mutated Model/Item/RNG parity.
- `game-data/HallUnit.js`: exact isolated reference/game gate green (10 cases),
  covering initialization/load, direction, z-order, direct/A* and
  pause/resume/save continuation.
  Its live-engine addendum now runs hidden Lwjgl3 with real source atlas files
  through `Animation<TextureRegion>` and `SpriteBatch`: the source Cocos RAF
  probe and game have the same five atlas selections (`1,1,1,1,0`) and the
  same 48x64 rectangles, in addition to the scheduler/action phase trace.
  This is deliberately not a cross-backend framebuffer-pixel claim.
- ItemStore/Item: exact isolated 57/57 recovered surface exercise exists;
  it must still be brought into the central aggregate before completion.

## Rendering work remaining (after relevant behaviour gates)

1. Build paired original-JS/Cocos and Kotlin render harnesses for sprite/frame
   selection using character ID, action ID, frame, direction, scale and state.
2. Enumerate every game character/resource and compare selected asset key,
   frame index, transform, anchor/origin, opacity, z/layer and action timeline.
3. Add map-layer composition fixtures: terrain, units, effects, UI/modal,
   minimap, weather and camera/scroll coordinates.
4. Run scenario combinations for dialogue, hall, battle, enemy turn, hit/HP
   change, magic, win/lose and menu overlays.
5. Only after contract parity is green, perform rendered image comparison as a
   regression check; investigate each mismatch from original source first.

Current rendering status: the R_00 map fixture now has reference/game immutable
run binding, a reproducible source image, and matching map texture, viewport,
quad and UV evidence. It does **not** have exact pixel parity: the same
default-framebuffer raw RGBA capture has RGB MAE `0.3393193` (alpha exact),
after aligning the source world quad at y=-560. CPU sampler reconstruction
reduces the same fixture to MAE `0.0314593`, but still has 249,447 differing
pixels. No renderer candidate is eligible for central merge until that
mismatch is explained and a strict comparison policy is green.

The latest spatial decomposition rules out a boundary-local or uniform RGB
bias explanation: the residual is stronger in the map interior and increases
from MAE `0.268404` on flat texture samples to `0.430008` at the highest source
texture gradients, with near-zero signed channel bias and only weak coordinate
phase dependence. This further supports a fractional LINEAR sampling/raster
rounding seam; it does not make the strict gate pass.

The same live source fixture now records the active Cocos WebGL sprite program:
both stages declare `precision highp float`, sample only
`texture2D(texture, v_uv0) * v_color`, and have gamma, BGRA, alpha-atlas, and
alpha-test branches disabled. Its final observed blend factors are
`SRC_ALPHA/ONE_MINUS_SRC_ALPHA` for RGB and alpha. This rules out an implicit
source gamma/atlas shader transform as the map residual explanation; it does
not substitute for exact renderer parity.

The source map-only capture path now preserves the scene Camera while hiding
all non-map nodes, resets `content.y` from the normal battle offset (464) to
the isolated-map offset (0), and forces a reversible compositor-surface
refresh before `Page.captureScreenshot`.  It asserts immediately before the
capture that the map is active while map children, battle units, and labels are
all inactive.  A fresh reference/game run produces two 2560×1376 pure-map PNGs.
Applying the measured `[-0.11, 0]` LibGDX raster sample-centre offset (without
changing the source logical quad) reduced the stock linear-sampler comparison
to 2,562,951 changed pixels, RGB MAE 0.4196548343628876, and maximum channel
delta 9.  Raw source-texture/framebuffer reconstruction then identified
Cocos's 8-bit-rounded bilinear weights.  The game now applies that sampler to
the map quad only (and restores SpriteBatch before units/HUD).  For the
isolated map-only fixture, using the physical framebuffer ratio and fragment
pixel centres—not LibGDX's slightly different logical-window viewport—removes
the need for an ad hoc map position offset.  The current same-contract raw
comparison is 250,263 changed pixels,
RGB MAE `0.03155167453972868`, zero alpha mismatches, and maximum channel
delta 1.  This is a material improvement, but remains a
failing strict-pixel gate whose remaining cross-backend rasterization residual
must be addressed before completion.
Matching the source fragment shader's declared `highp` precision produces the
same result as the desktop driver's prior precision mode, so it is retained as
source-faithful configuration but does not explain the residual.
An integer registration sweep across ±3 framebuffer pixels selects exactly
`dx=0, dy=0`; the nearest alternatives rise to RGB MAE 1.5183 (x) and 1.6724
(y).  The residual is therefore not eligible for a whole-pixel map-position
correction. The earlier stock-SpriteBatch sub-pixel sweep selected a
raster-only x offset of -0.11 world units (about 0.19 framebuffer pixels),
but that result is superseded by the physical-pixel-centre sampler above: the
current logical map transform uses no position offset.
The live map-only capture controls also confirm that `GL_DITHER` has no effect
(enabled and disabled each remain RGB MAE 0.5591684267502423), while forcing
nearest texture filtering regresses to RGB MAE 2.6073135257691376 (maximum
channel delta 142). Those stock-filter controls remain negative evidence; the
candidate now manually reproduces the measured 8-bit bilinear filter for the
isolated fixture.
Texture-coordinate phase candidates are also excluded: ±0.00005 U offsets
produce RGB MAE 0.5287/0.5264 and ±0.00005 V offsets 0.5426/0.5440, all worse
than the compensated baseline. The current implementation retains no UV or
position adjustment.

The first full `dialogue-1` diagnostic also now binds an existing Cocos
2560×1376 compositor PNG to a live LibGDX framebuffer capture. It fails
(RGB MAE `36.89364174539729`, 3,514,171 changed pixels); because the source is
`Page.captureScreenshot` rather than the same raw readback contract, this is
diagnostic evidence only and cannot be counted as a parity gate.

A fresh `dialogue-1` source capture now additionally uses the same valid
default-framebuffer `EVENT_AFTER_DRAW/gl.readPixels(RGBA, UNSIGNED_BYTE)`
contract as the candidate's native readback. The two 2560×1376,
bottom-left, `srgb-encoded-rgba8` raw files fail strict comparison. Their
first comparison exposed a source-transform-derived `+96y` Kotlin SayLayer
error; correcting it reduced RGB MAE from `37.98270528630693` to
`20.22520865506904`. The same live source capture additionally proves that
full Battle `ScrollView/content.y=464` must not inherit map-only isolation's
`y=0`: restoring the full-scene map quad reduced RGB MAE further to
`5.523406745851502`. The same source snapshot then supplied the active
`SHOW_SAY` marker's map-local `[-96,-288]` coordinate; fixing its Kotlin x
offset reduced RGB MAE to `5.182297912124516`. Source-visible unit frame
rects then identified the 8-tick `anime0` phase and avatar-group-74 wounded
`anime9` phase; binding them yields RGB MAE `3.8989256866218507`, with
1,548,316 changed pixels and zero alpha mismatches after matching Cocos's
opaque destination-alpha blend contract. The same source frame proves its
`U_select_11-1` panel uses runtime Sprite type `Simple`, not `Sliced`;
replacing Kotlin's NinePatch with direct authored stretch yields RGB MAE
`3.438048464752907` and 1,543,605 changed pixels. The live speaker Label
color `[35,2,234,255]` reduces it further to `3.4298553703367247` and
1,543,074 changed pixels. Separate exact reference/game gates now verify all 19
visible unit frame rects/mirrors and the panel's
Simple/stretch rendering mode. This
establishes the full dialogue composition as a
real P0 framebuffer mismatch; it replaces neither the map-only fixture nor
its zero-difference criterion. A separate raw text-band regression guard now
keeps the corrected vertical source transform within two framebuffer rows and
asserts opaque reference/game alpha.

The current R_00 launch path now selects the explicit `게임 시작` setup row
(the source's first row intentionally loops) in both desktop capture bootstrap
and all-scenario verification. The dialogue fixture additionally leaves its
BattleHall roster empty, matching the original source capture: S_00's five
visible allies are authored by `createFriend`, and a nonempty game roster
incorrectly materializes extra `createMine` actors. Re-reading the source
SayLayer tree corrected the ordinary (non-modal) panel/body/face origins to
`y=332/468.314/330`. The fresh same-contract raw comparison is now RGB MAE
`2.8444056784459786` (1,496,465 changed pixels, zero alpha mismatches), an
improvement over the previous `3.4298553703367247` capture. It remains a
strict-gate failure; the residual is now renderer/text/minimap raster work,
not a dialogue string, name, roster, or whole-dialogue transform mismatch.

`./gradlew :desktop:run --no-daemon '--args=--verify-all-scenarios'` was also
rerun after this route change: all 59 R scenarios and 58 S battle scripts
completed their declared initialization checks successfully.

Applying the already-proven physical framebuffer pixel-centre Cocos8 sampler
to the normal ScrollView battle map (not only `map-only`) further reduces the
same R_00/dialogue-1 raw comparison to RGB MAE `2.617668305626211` and
516,687 changed pixels, with zero alpha mismatches. A source/pixel-
correlation-derived RichText origin correction then reduces the same
deterministic frame to RGB MAE `2.4619227682897287` and 515,929 changed
pixels (alpha still exact); the independently measured speaker-label origin
then reaches `2.461018501695736` and 515,888 changed pixels. The normal default and an
explicit `--map-sampler=frag8` capture produce the identical RGBA SHA-256,
so this is now the default source-faithful map path rather than an experiment.
The remaining largest local residual was initially attributed to the
translucent SayLayer panel. A direct visual/byte inspection instead shows its
flat panel samples matching, while the source live frame had changed unit
action frames (including a smoke/white placeholder versus a standing/prone
unit) between otherwise identical captures. The capture now pauses all 50
generated battle-unit `cc.Animation` components immediately when the battle
creates them and again after the `&235` dialogue is reached, while leaving
SayLayer's own scheduler active. Two consecutive source `EVENT_AFTER_DRAW`
readbacks reproduce SHA-256 `b336808a…`; this is the current deterministic
strict-pixel oracle. Ordinary map and unit raster seams remain measurable;
semantic content and layout are no longer the limiting factors.

The source helper now also captures the live Cocos `cc.Label._ttfTexture`
behind the RichText segment. `tools/export_cocos_ttf_texture.py` turns that
bottom-origin raw RGBA dump into a text-SHA-256-keyed cache asset, and
BattleScreen uses the cache when available with its FreeType path retained as a
general fallback. The first R_00 cache entry reproduces the original 545×52
body raster and reduces the deterministic full-frame result to RGB MAE
`2.1027627634447676`, 464,234 changed pixels, and zero alpha mismatches.
The capture controller now waits for the source typewriter to finish before
each subsequent transition, and its nonzero `JOJO_DIALOGUE_SKIP` sidecars use
collision-free `source-r00-dialogue-skip-N.json` names. This recovered and
cached the ordinary S_00 `&334` body `아.......` (101×52) and `&210` body
`젠장, 황번 반역자 수가 계속 늘어났다.` (537×52), each with its source
segment origin/draw size. They exercise the same text-keyed production path;
unseen dialogue still uses the general FreeType fallback, so the full corpus
is not yet claimed to have dialogue-wide pixel parity.

The live source frame also revealed that S_00's `unit(235).setAction(4)`
retains the original `hight-light` material on its generated `mov2` sprite.
Its final source framebuffer result is a fully saturated white death frame,
and the BattleUnit HP ratio is already zero so no bar is drawn. The Kotlin
renderer now mirrors that composited result for the scripted action-4 visual.
Against the stable `b336808a…` raw source this reduces the current full-frame
RGB MAE to `1.8012168233708818`, with 466,682 changed pixels and zero alpha
mismatches. The strict gate remains red (MAE and changed pixels must both be
zero); this is a composition correction, not an asset substitution.

The cached RichText glyph mask still registered one physical framebuffer pixel
right/up of Cocos even though its source PNG bytes were exact. Applying the
measured `-0.58` logical-world correction to cached glyph quads aligns the
mask and lowers the same-frame MAE again, from `2.176984636173692` to
`2.0675787873970446` (468,305 changed pixels; alpha exact). This correction
is scoped to source-extracted RichText cache entries; FreeType fallback text
retains its independently measured baseline path.

A subsequent isolated experiment exported the observed `bg/box5` 20px HUD
frame and rendered it as a 3px-cap `NinePatch` at the observed 244px minimap
bounds. It regressed the same raw comparison to RGB MAE `3.5358317057291666`;
the implementation was reverted. The source node is therefore inventory
evidence only until its actual Cocos composition order/trim transform is
captured, rather than an unverified visual approximation in the candidate.

The state-addressed semantic composition matrix currently passes **7 / 7**
named scenarios. `dialogue-1` is now paired through a fresh original
SayLayer input capture (`_next`/`_handle` only) that exposes the live `&235`
RichText string; the Kotlin candidate emits the exact same visible text. This
is a semantic/layout checkpoint only and does not close any framebuffer pixel
gate.

A separate fresh CDP controller now captures the real R_00 opening Battle
`SayLayer` before that CLI's later wait, including source-owned strings,
visible first typewriter text, RichText metrics and panel subtree. Its matching
Kotlin `yingchuan-opening-say` fixture now passes an exact semantic/layout
source-versus-game gate for the first `꺼` glyph, strings/remaining text,
Panel_cancel, bg0/body/face and RichText fields. This is a different state
from `dialogue-1`, so it does not change that matrix's still-failing 6/7 gate.

A separate fresh source observation proves an active initial Hall
`InfoLayer`/`RichText` typewriter state (not `SayLayer`; runtime fields match
the recovered `InfoLayer`). Its first tick is now paired with an isolated
LibGDX overlay using the source DynamicAtlas crop and exact semantic/layout
trace; full-reveal/auto-close-pending is also paired from a 5ms source-live
observation. A fresh source `Panel_cancel` `TOUCH_END` fixture now proves
typewriter fast-forward into the same full-text/auto-close-pending state and
has a separately addressed LibGDX semantic counterpart. A second source
fixture proves immediate `SKIP` event removal and the candidate has a matching
no-overlay state. Framebuffer pixel parity remains open, so it remains a
distinct P0 composition gap.

## Final audit still required

- `./gradlew :desktop:check --no-daemon` was rerun successfully on
  2026-09-01 (10m 7s).  The run completed the scenario choice suite and the
  reference/game Yingchuan actor-state and 8-modal framebuffer smoke gates.  Its
  green result does not include a strict cross-backend framebuffer equality
  claim; that remains governed by the map-only residual above.

- The latest isolated exact meta-run (`20260830T230145Z`) passed
  all **40 / 40** included gates with no timeout. It now includes the formerly
  omitted InfoLayer, Item advanced, DefineUnitLayer, Hall factory, Unit,
  HallUnit and full ItemStore contracts. HallUnit runs with its declared JDK 24
  toolchain, rather than the host JDK 26 that Kotlin 2.1 cannot parse. The
  non-pixel render contract (asset/frame/transform/timeline metadata) and the
  hash-pinned Unit production matrix (15 partitions, including production
  corpus filters). Both remain independent of the acknowledged sampler/raster
  framebuffer residual. The added Unit source-root gate rejects machine-specific
  oracle paths and verifies the sibling recovered source and encrypted Game
  asset root. A separate reconciliation gate hash-pins all 98 recovered
  `Unit.js` declarations to exactly one isolated controlled-contract gate
  (unmapped: 0). The meta-runner itself resolves `.verification-work` and the
  sibling original checkout relative to its own path, rather than from a
  workstation-specific absolute path. The meta-run still deliberately excludes
  strict framebuffer/composition pixel gates, central aggregation and the
  documented production OS/mobile service boundaries.
- The nested LibGDX candidate's source-trace harnesses now resolve the actual
  sibling `~/workspace/jojo_mobile` tree rather than a nonexistent
  `.verification-work/jojo_mobile` path. `./gradlew :core:test --no-daemon` now
  completes green with 166 actionable tasks; this restores executable source
  versus Kotlin behaviour checks in the isolated worktree, but does not close
  the unaggregated or pixel-parity work above. `verifySourceRootResolution`
  is now a `:core:test` prerequisite and verifies the real recovered-module
  root plus all 57 source harness path literals.
- `./gradlew :desktop:run --args='--verify-all-scenarios' --no-daemon` now
  completes in the isolated candidate: all 59 `R_XX` scripts boot and finish
  one deterministic input branch; all 58 `S_XX` battle scripts materialize
  original maps and 2,378 unit profiles, and exercise 75 automatic camp
  entries. The interpreter reports `AST_API_GAPS: none` on those executed
  paths. This is broad executable coverage, not a claim that every player
  choice/RNG branch or strict framebuffer state has been exhausted.
- `auditScenarioBranchSurface` now makes that remaining scenario scope
  explicit from the restored source: 117 R/S scripts contain 101 choice sites
  (229 declared option paths) across 70 scripts and 31 RNG sites in `R_00`.
  It also inventories 7,124 stage/model host-call sites. These are static
  locations, not a claim that every location is reachable in a single
  campaign state; each choice/RNG site now includes its enclosing recovered
  Python guard expressions and its `sceneN` entry point, which defines the
  campaign-state setup required for the branch-expansion work still pending.
- The candidate AST runtime now accepts an explicit `Model.random()` sequence
  (each draw is 0..100) for reproducible branch tests and otherwise mirrors
  recovered `Tool.random(0,100)`'s LCG/range. A direct recovered-Tool/Kotlin
  pairwise gate covers four seeds and 18 sequential draws; the LCG contract is
  also unit-tested. The real-app runner now records each random draw with its
  source function/line and can stop immediately after that source call; the
  `R_00.scene1:958` `<70` and `scene1:1232` `<34` guards are permanently
  checked on both sides of their thresholds (`0` and `100`), including the
  `scene1:1234` `elif` `<50` call, as are the four
  consecutive `R_00.scene2` guards at `2900`/`2902` (`<50`), `2904` (`<34`)
  and `2907` (`<50`), and the four corresponding `scene3` guards at
  `3318`/`3320` (`<50`), `3322` (`<34`) and `3324` (`<50`), plus the
  source-`infoTransfer` label entries `scene1:1748` (`<20`), `2004` (`<30`),
  `2178` (`<20`) and `2434` (`<30`).
  `:desktop:verifyScenarioRandomCoverage` joins only registered fixture traces
  to the 31-site static inventory (currently 29/31 sites, 93.55%; remaining
  `scene1:1376` and `1378`, which require a stable source unit-iteration
  state). Exhaustive campaign-state/RNG path execution remains pending.
  Exhaustive campaign-state/RNG path execution remains pending.
- The two source-level outcomes of `R_00.scene1`'s first player choice were
  re-run in the actual isolated LibGDX app through `--verify-branch` and
  `--verify-branch-2`; both route to their distinct source dialogue payloads.
  This proves that one two-option choice site, not the remaining conditional
  choice/RNG surface.
- The candidate now also accepts a zero-based, comma-separated input route via
  `--verify-choice-script=` and requires that every supplied input reaches a
  real `stage.choice` before the scenario completes. Both complete first-game
  routes (`R_00: 0,0,3` and `1,0,3`) were executed in the real LibGDX app.
  This is the reusable runner for adding the guard-derived state cases; it is
  not yet an exhaustive branch suite.
- The same runner accepts `--verify-globals=ID:VALUE,...` and
  `--verify-random=VALUE,...` (0..100) to construct recovered guard states.
  During this work an explicit `--scenario=R_00` was found to be incorrectly
  replaced by a persisted scenario; explicit scenarios now take precedence,
  ensuring reproducible isolated branch runs.
- `--verify-round=` and `--verify-camp=` now inject the source battle context.
  The two options of the guarded `S_04.scene1` round-1 choice and guarded
  `S_05.scene1` round-8 choice have each been executed in the real app. This
  validates the state-input runner across both default and injected battle
  conditions, while the remaining guarded choice sites are still pending.
- `--verify-vars=ID:VALUE,...` now injects recovered script-local `vars[]`
  before `sceneN` begins. Both options of `S_12.scene1` were executed with
  its composite guard (`vars[30]=1`, round 1, player camp), proving that the
  runner can combine script and battle state rather than only replay defaults.
- `--verify-attributes=UNIT:ATTRIBUTE:VALUE,...` now injects the source
  `unitStateTest` attribute context. Both options of `S_19.scene1` were
  executed with unit 15's HP attribute set to 50, satisfying its original
  `>=1` and `<100` guard pair.
- `--verify-positions=UNIT:X:Y,...` now injects battle positions used by
  `isNear`/position predicates. Both options of `S_09.scene1` were executed
  with units 0 and 149 adjacent, satisfying its original proximity guard.
- `--verify-camp-positions=CAMP:X:Y,...` now supplies the camp position sets
  used by `totalUnit` and `totalRectUnit`. Both options of `S_36.scene1` were
  executed with eight enemy-camp positions, satisfying its original count
  guard.
- `--verify-win` now injects the source `stage.winTest()` outcome. Both
  options of `S_08.scene1` were executed under its win, local-variable and
  HP-state guard.
- Two additional multi-step `R_00` configuration routes (classic recalculation
  and expanded-mode selection) are now real-app fixtures.
- The two unguarded `R_25.scene1` and `R_31.scene1` choices have both had
  their option outcomes executed in the real app.
- Both `S_02.scene1` outcomes now exercise its original round-4/player-camp
  plus exact three-enemy rectangle condition.
- Both outcomes of the unguarded opening `R_12.scene1` choice are now
  covered by the real-app runner.
- `--verify-scene=sceneN` starts a named recovered source scene, making
  non-opening scene branches executable without fabricating preceding events.
  Both outcomes of `R_03.scene2` are covered through this entry point.
- `R_01.scene2` has two consecutive binary choices; all four `00`, `01`,
  `10`, and `11` combinations complete in the real app and are permanent
  fixtures.
- Both outcomes of the later `R_04.scene8` choice are likewise permanent
  real-app fixtures.
- Both outcomes of `R_10.scene8` are also permanent real-app fixtures.
- Both outcomes of `R_15.scene5` are also permanent real-app fixtures.
- Both outcomes of `R_16.scene0` validate direct entry to a non-`scene1`
  source function and are permanent real-app fixtures.
- Both outcomes of `R_18.scene3` and `R_22.scene3` are permanent real-app
  fixtures.
- Both outcomes of `R_23.scene0` are permanent real-app fixtures.
- Both outcomes of `R_27.scene4` are permanent real-app fixtures.
- Both outcomes of `R_35.scene2` are permanent real-app fixtures.
- Both outcomes of `S_07.scene0` are permanent real-app fixtures.
- Both outcomes of `S_11.scene0` are permanent real-app fixtures.
- Both outcomes of `S_13.scene0` are permanent real-app fixtures.
- Both outcomes of `S_16.scene0` and `S_17.scene0` are permanent real-app
  fixtures.
- All three outcomes of `S_18.scene0` are permanent real-app fixtures.
- All three outcomes of `S_20.scene0` are permanent real-app fixtures.
- Both outcomes of `S_22.scene0` are permanent real-app fixtures.
- Both outcomes of `S_24.scene0` are permanent real-app fixtures.
- Both outcomes of `S_25.scene0` are permanent real-app fixtures.
- All three outcomes of `S_28.scene0` are permanent real-app fixtures.
- Both outcomes of `S_30.scene0` and `S_31.scene0` are permanent real-app
  fixtures.
- Both outcomes of `S_33.scene0` are permanent real-app fixtures.
- Both outcomes of `S_39.scene0`, `S_40.scene0`, and `S_41.scene0` are
  permanent real-app fixtures. The nested three-choice negotiation tree in
  `S_42.scene0` has all four terminal paths covered (`0`, `1,0`, `1,1,0`,
  and `1,1,1`).
- Both outcomes of `S_43.scene0` and `S_44.scene0` are permanent real-app
  fixtures.
- Both outcomes of `S_47.scene0` are permanent real-app fixtures.
- Both outcomes of `S_49.scene0` and `S_50.scene0` are permanent real-app
  fixtures.
- Both outcomes of `S_10.scene1` execute under its original unit-0 rectangle
  guard, with the required source position injected.
- Both options under each of `S_35.scene1`'s two original unit-1026 exact
  position guards (`23,16` and `19,1`) are permanent fixtures.
- Both outcomes of `S_15.scene1` now execute with its original four-variable
  composite guard (`vars[51..54]`) satisfied.
- Both outcomes of `S_03.scene1` now execute with its original variable,
  round, and enemy-count composite guard satisfied.
- Both outcomes of the round-10 `S_21.scene1` event execute with its original
  variable and rectangle-occupancy guard satisfied.
- The analogous round-15 and round-20 `S_21.scene1` events are likewise
  covered on both outcomes, using their required prior-event flags.
- Both outcomes of `S_06.scene1` now execute with its original round-window,
  enemy-count, and player-count guard satisfied.
- Both outcomes of `S_23.scene1` now execute with its two original
  enemy-rectangle-count guards satisfied.
- Both outcomes of `S_27.scene1` now execute with its original multi-camp
  count and player-rectangle guard satisfied.
- Both outcomes of `S_28.scene1` now execute with its analogous original
  multi-camp count and player-rectangle guard satisfied.
- The first `S_34.scene1` reinforcement state exposes two consecutive binary
  choices; all four terminal combinations (`00`, `01`, `10`, `11`) are
  permanent fixtures under its original variable, camp, and rectangle guard.
- Both outcomes of `S_01.scene1` now execute with its original three-variable
  and two-camp-count composite guard satisfied.
- Both outcomes of the second `S_34.scene1` reinforcement event execute with
  its original prior-event flag, enemy-count, and camp guard satisfied.
- The 229 verified real-app choice paths are now permanent
  `:desktop:verifyScenarioBranchFixtures` tasks and a mandatory
  `:desktop:check` prerequisite. Their source-location trace is now joined
  to the static source inventory by `:desktop:verifyScenarioChoiceCoverage`.
  The full current aggregate completed successfully in the isolated candidate
  (`243 actionable`, `243 executed`, 5m33s). The current trace report has
  229 fixtures and 255 selections, covering all 229 of 229
  declared source options (100.00%; uncovered: 0). This is exact option
  coverage rather than the former path-count proxy, but is not yet the full
  scenario suite: campaign-state/RNG Cartesian coverage remains separate.
- Re-run the full behaviour aggregate after every newly aggregated gate.
- Run `:core:test`, `:desktop:check`, and desktop headless scenario checks.
- The regenerated recovered-module inventory covers all 141 recovered factories:
  128 have a declared central pairwise task and 13 have isolated exact-trace
  evidence; none are without evidence. It deliberately does not turn those
  declarations into a completion claim. The 13 isolated contracts still await
  the planned central aggregation.
- Execute the full rendering matrix and retain reference/game trace artifacts for
  every mismatch resolution.
- Aggregate every currently isolated green gate into `verifyBehaviorPairwise`
  and `:core:test` only after resolving the documented central type/name
  collisions in the integration preflight manifest.
