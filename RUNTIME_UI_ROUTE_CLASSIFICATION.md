# Runtime UI route classification

This inventory separates normal player input routes from `JojoGame.screenshotState`
render oracles. A source/port pixel or pairwise match in the latter category is not
runtime reachability evidence.

## Normal routes

| Feature | Production route | Evidence boundary |
|---|---|---|
| Title buttons | `TitleScreen` pointer -> `TitleInteraction` -> new game/load/settings/exit callback | `TitleInteractionTest` sends authored pointer coordinates through the same dispatch code. |
| Settings | `TitleScreen` pointer -> `TitleInteraction.settingActionAt/applySetting` -> `SettingLayer` | The natural-input test opens Settings, changes flag/radio/background/speed, and closes it. The composite title framebuffer is not used as evidence. |
| Hall/street dialogue | `ScenarioPreviewScreen` + `PythonAstRuntime` + `ScenarioStage` | The duplicate `SayDialogueLayer` and its dedicated harness were deleted. |
| Battle dialogue | `BattleLayer` + `PythonAstRuntime` | Same separation as Hall dialogue. |
| Battle unit/terrain information | `BattleLayer` -> `MineUnitInfoLayer`/`OtherUnitInfoLayer` and terrain overlay | The duplicate `BattleInfoPopup` and its dedicated harness were deleted. |
| Stage script overlays | `ScenarioStage` + `ScenarioPreviewScreen` | The duplicate `StageLayerPort` and its dedicated harness were deleted. |
| Victory conditions | `BattleLayer` -> compact/full victory-condition layers | The unused parallel `WinConditionRouteFlow` was deleted. |
| Return to Hall/current R script | `JojoGame.showCampaignHall` -> `showScenario` | The invented blue `CampaignHallScreen` was deleted. |

## Explicit capture-only routes

The following selectors occur before normal routing in `JojoGame.create`, and only
exist when the desktop launcher supplies `screenshotState`:

- `AchievementsFixtureScreen`, `AttributeFixtureScreen`, `GenericListFixtureScreen`
- `ModalLoadRouteScreen`, `RaffleGateRouteScreen`, login optional overlay render routes
- `CmdRouteScreen`, `TerminalSceneRouteScreen`, `LearnUnitSkillRouteScreen`
- `DefineUnitRouteScreen`, `BattleUnitEditRouteScreen`, `EditRosterRouteScreen`
- sprite, info, notice, reward, dialogue, choice, input-box, quantity and system-overlay fixtures
- battle-preparation visual states entered with `start-battle-*-fixture`

Some of these reuse production models, but none is a substitute for reaching the
feature from Title, an authored R script, battle preparation, or `BattleLayer`.
They remain useful only as renderer oracles.

## Registered or platform-only layers

| Type | Classification | Source/runtime finding |
|---|---|---|
| InputBox prefab | source inventory + capture oracle | Registered in `Instance.LAYER`, but no recovered production caller was found. The production-looking port class/test were deleted; only private capture state remains. |
| Progress prefab | source inventory + capture oracle | Registered prefab only; no recovered production caller was found. The production-looking port class/test were deleted; only private capture state remains. |
| `LoadingLayer` | connected with explicit platform substitution | `TitleScreen` now consumes `CHECK_REGISTER`, attaches Loading, waits for the callback, then detaches it. `JojoGame` documents that network `registerCheck`/hot update is unsupported and asynchronously returns no registration payload. |
| login SignIn/Version render states | capture oracle | Version has no recovered caller. SignIn is behind the source desktop `supportAd >= 8` gate, while the recovered desktop helper supplies no usable numeric support code. The unused `LoginOptionalOverlayFlow` reconstruction was deleted; capture rendering remains explicit. |

## Conditional Setting features

The production `SettingLayer` now owns the recovered button 7/8/9 state machine.
Button 7 opens `AchievementsLayerPort` only when saved rewards exist. Buttons 8 and
9 require `supportAd >= 8`; raffle additionally requires the current scene to be
`Hall` or `Battle`, while sign-in has no scene restriction. `TitleScreen` supplies
the Login context and `BattleLayer` supplies the Battle context. The desktop helper
still reports no entitlement (`0`), so these remain correctly gated in ordinary
desktop execution, while input tests exercise both sides of the original condition.

`ResetLayerPort` was deleted because recovered source contains only prefab
registration and no `addLayer` caller. `EditGlobalFlow` was moved out of production:
the recovered caller chain is guarded by `ENABLED_FEATURE.EDIT`, so its behavior is
retained only as a private source-inventory oracle in `EditMutationTraceHarness`.

## Verification policy

The dead Say/Stage/BattleInfo dedicated pairwise tasks were removed rather than
retained as completion evidence. Normal dialogue/stage/battle-info coverage now comes
from the actual scenario and battle runtime tests. `verifyRuntimeTestIntegrity`
continues to prevent fixture-only implementations from being promoted.
