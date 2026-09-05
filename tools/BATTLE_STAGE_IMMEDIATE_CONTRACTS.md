# Battle stage immediate-command contracts

This note covers the source surface counted by the API audit as `center(18)`,
`setMaxRound(4)`, and `unit().addLv(21)`. The numbers are occurrence counts,
not arguments.

## `stage.center(x, y)`

`BattleLayer.center` is synchronous. It computes the absolute ScrollView
content position, assigns it immediately, then synchronously dispatches
`MAP_SCROLLING`. It does not call `pause`, schedule an action, accept a
callback, or resume the Python script.

With `TITLE_SIZE * 2 == 96`, its exact contract is:

```text
mapPixelWidth  = map.node.width  * 2
mapPixelHeight = map.node.height * 2
x = clamp(mapPixelWidth/2 - tileX*96,
          -(mapPixelWidth-viewportWidth)/2,
          +(mapPixelWidth-viewportWidth)/2)
y = clamp(tileY*96 - mapPixelHeight/2,
          -(mapPixelHeight-viewportHeight)/2,
          +(mapPixelHeight-viewportHeight)/2)
content.position = (x,y)
dispatch MAP_SCROLLING(x,y)
```

All 18 calls are in the two mutually exclusive `S_57.scene1` branches. Each
branch has the same nine-call sequence:

```text
center(5,20), center(11,20), center(13,20), center(11,20),
center(13,20), center(11,20), center(13,20), center(11,20), center(13,20)
```

S_57 addresses the full `0..39` rectangle, hence its map is 40x40 tiles. At
the full-trace viewport `1488.372093x800`, the synchronous camera log is
`center(5,20) -> (1175.813954,0)`, `center(11,20) -> (864,0)`, and
`center(13,20) -> (672,0)`. Repeated calls must still dispatch
`MAP_SCROLLING`, even when the assigned position equals the current position.

Every center returns immediately. The following authored `delay` owns the
pause: `5s`, then `5s`, six alternating `1s` delays, then `5s`. A port must not
invent a camera tween, callback barrier, or an implicit delay for `center`.

## `stage.setMaxRound(value)`

`BattleLayer.setMaxRound` is also synchronous and camera-neutral. It adds four
when `eFlag & ENABLED_FEATURE.ZJHH(8)` is set, then calls `setProperty`. The
property proxy is updated immediately; `setProperty` suppresses the write if
the effective value is unchanged. There is no event, pause, callback, or
presentation barrier.

Expected scenario values are:

| Scenario | Requested | Normal | ZJHH enabled |
| --- | ---: | ---: | ---: |
| `S_11.scene1` | 12 | 12 | 16 |
| `S_38.scene1`, first transition | 30 | 30 | 34 |
| `S_38.scene1`, later transition | 30 | 30 | 34 |
| `S_44.scene1` | 15 | 15 | 19 |

In each case the subsequent `showWinCondition` owns the modal pause. The second
S_38 call may be a no-op write when the effective value is already 30/34, but
the Python command still returns synchronously.

## `stage.unit(id).addLv(1)`

The 21 calls occur consecutively in `S_04.scene1`, choice `sel == 2`, for IDs
`474..480`, `483..489`, and `492..498`. `BattleUnit.addLv` delegates directly
to its persistent model `Unit.addLv`, and `BattleUnit.lv()` reads that same
model, so the new level is visible immediately to later commands and battle
calculations.

`Unit.setLevel` clamps to `[1, Unit.lvLimit()]`. If the clamped level is
unchanged it performs no mutation. Otherwise its synchronous order is level
write, ability adjustment (or full phase recalculation for the configured Mine
case), equipment-skill reset, unit-skill reset, posts-skill reset, and magic
cache invalidation. There is no camera operation, pause, callback, battle
animation, or implicit HP/MP refill. The next `stage.say` is the next suspension
point.

The machine-readable expected log is
`tools/battle_stage_immediate_contracts.json`. Run the independent source
oracle with:

```sh
python3 tools/verify_battle_stage_immediate_contracts.py
```
