# Battle stage lifecycle call-site audit

AST-scanned **117** scripts (58 S + 59 R), **5627** target call sites, and **415** `stage.draw()` sites.

The report intentionally includes only `S_*.py` and `R_*.py`; prior inventories that include `train.py` are reconciled below.

## API summary

| API | Calls S/R | Draw phase (function / module lexical) | Callback possibility | Static argument patterns | Findings |
|---|---:|---|---:|---:|---|
| `stage.loadBg` | 416 (58/358) | fn: after=113, before=302, unknown=1; module: before=119, unknown=297 | 416 | 113 | R_route_uses_HallLayer_not_battle_lifecycle_contract=358, callback_can_be_required_at_runtime=416, draw_order_not_proven_through_branch_analysis=1 |
| `stage.setUnitAttr` | 441 (406/35) | fn: after=19, before=406, no_draw_in_function=16; module: after=3, before=407, unknown=31 | 0 | 8 | — |
| `stage.unit().setPosts` | 4640 (4640/0) | fn: before=4640; module: before=4640 | 4640 | 80 | callback_can_be_required_at_runtime=4640 |
| `stage.resumeCtrl` | 130 (130/0) | fn: no_draw_in_function=130; module: after=130 | 0 | 1 | lifecycle_contract_not_present=1 |

## Route and lifecycle authority

### `stage.loadBg`
- S: `BattleLayer.loadBg` (battle/BattleLayer.js:2948); signals=pause, resume, callback; contract hash matches
- R: `HallLayer.loadBg` (ui/HallLayer.js:1432); signals=pause, resume, callback; no contract hash for this route
- callback requirements: required_for_script_continuation=416
- top modules: `R_46`=24, `R_25`=16, `R_10`=15, `R_04`=14, `R_18`=14, `R_01`=13, `R_16`=13, `R_15`=10
- static argument patterns: `int:1|int:0`=94, `int:2|int:8`=43, `int:2|int:53`=42, `int:2|int:21`=33, `int:2|int:54`=13, `int:2|int:7`=11, `int:2|int:9`=7, `int:2|int:67`=6, `int:2|int:12`=5, `int:2|int:28`=5, `int:2|int:31`=5, `int:2|int:49`=5
### `stage.setUnitAttr`
- S: `BattleLayer.setUnitAttr` (battle/BattleLayer.js:11167); signals=assetLoad; contract hash matches
- R: `StageLayer.setUnitAttr` (ui/StageLayer.js:954); signals=none; contract hash matches
- callback requirements: not_triggered_by_static_attr=441
- top modules: `S_00`=7, `S_01`=7, `S_02`=7, `S_03`=7, `S_04`=7, `S_05`=7, `S_06`=7, `S_07`=7
- static argument patterns: `gvars[int:0]|int:17|gvars[int:3]`=92, `gvars[int:0]|int:20|gvars[int:21]`=58, `gvars[int:0]|int:21|gvars[int:24]`=58, `gvars[int:0]|int:22|gvars[int:23]`=58, `gvars[int:0]|int:23|gvars[int:22]`=58, `gvars[int:0]|int:24|gvars[int:24]`=58, `gvars[int:0]|int:25|gvars[int:23]`=58, `int:35|int:1|gvars[int:0]`=1
### `stage.unit().setPosts`
- S: `BattleUnit.setPosts` (battle/BattleUnit.js:2463); signals=pause, resume, callback, assetLoad; contract hash matches
- callback requirements: conditional_avatar_reload=4640
- top modules: `S_00`=80, `S_01`=80, `S_02`=80, `S_03`=80, `S_04`=80, `S_05`=80, `S_06`=80, `S_07`=80
- static argument patterns: `int:0`=58, `int:1`=58, `int:10`=58, `int:11`=58, `int:12`=58, `int:13`=58, `int:14`=58, `int:15`=58, `int:16`=58, `int:17`=58, `int:18`=58, `int:19`=58
### `stage.resumeCtrl`
- S: `BattleLayer.resumeCtrl` (battle/BattleLayer.js:5121); signals=callback; no contract hash for this route
- callback requirements: none=130
- top modules: `S_34`=5, `S_37`=5, `S_25`=4, `S_35`=4, `S_03`=3, `S_12`=3, `S_21`=3, `S_23`=3
- static argument patterns: `<none>`=130

## Findings

- `R_route_uses_HallLayer_not_battle_lifecycle_contract`: 358
- `callback_can_be_required_at_runtime`: 5056
- `draw_order_not_proven_through_branch_analysis`: 1
- `lifecycle_contract_not_present`: 1

## Prior-audit reconciliation

- `stage.loadBg`: prior=417, current S/R=416 — prior_inventory_includes_non_SR_sources_or_scope_differs
- `stage.resumeCtrl`: prior=131, current S/R=130 — prior_inventory_includes_non_SR_sources_or_scope_differs

Full per-call evidence (module/function/branch/line/draw proof/arguments/callback assessment) is in the machine JSON.
