# Battle stage API surface audit

Scanned 58 S_*.py scripts, 42516 call sites, and 74 APIs.

Findings: **2 APIs** ({"unknown-handler-source-noop": 2}).

Blocking gate findings: **0 APIs** ({}).

| API | Calls | Runtime | Original | Source barrier | Port battle barrier | Eager mutation | Findings |
|---|---:|---|---|---|---|---|---|
| `stage.attackAction` | 28 | yes | BattleLayer.attackAction | yes | yes | yes | — |
| `stage.bgSound` | 146 | yes | StageLayer.bgSound | no | no | yes | — |
| `stage.center` | 18 | yes | BattleLayer.center | no | no | no | — |
| `stage.choice` | 72 | yes | BattleLayer.choice | yes | yes | no | — |
| `stage.countDir` | 2173 | yes | BattleLayer.countDir | no | no | yes | — |
| `stage.createEnemy` | 58 | yes | BattleLayer.createEnemy | no | no | yes | — |
| `stage.createFriend` | 22 | yes | BattleLayer.createFriend | no | no | yes | — |
| `stage.createMine` | 60 | yes | BattleLayer.createMine | no | no | yes | — |
| `stage.curCamp` | 208 | yes | BattleLayer.curCamp | no | no | no | — |
| `stage.delay` | 248 | yes | StageLayer.delay | yes | yes | no | — |
| `stage.draw` | 58 | yes | BattleLayer.draw | no | no | yes | — |
| `stage.effectSound` | 37 | yes | StageLayer.effectSound | no | no | yes | — |
| `stage.end` | 260 | yes | StageLayer.end | no | no | yes | — |
| `stage.getItem` | 286 | yes | BattleLayer.getItem | yes | yes | yes | — |
| `stage.guide` | 1 | NO | BattleLayer.guide | no | no | no | unknown-handler-source-noop |
| `stage.heightLight` | 41 | yes | BattleLayer.heightLight | yes | yes | no | — |
| `stage.incSceneIdx` | 58 | yes | StageLayer.incSceneIdx | no | no | yes | — |
| `stage.info` | 157 | yes | StageLayer.info | yes | yes | yes | — |
| `stage.initFight` | 58 | yes | BattleLayer.initFight | no | no | yes | — |
| `stage.isInPos` | 220 | yes | BattleLayer.isInPos | no | no | no | — |
| `stage.isInRect` | 196 | yes | BattleLayer.isInRect | no | no | no | — |
| `stage.isLose` | 58 | yes | BattleLayer.isLose | no | no | no | — |
| `stage.isNear` | 231 | yes | BattleLayer.isNear | no | no | no | — |
| `stage.itemVars` | 51 | yes | BattleLayer.itemVars | no | no | yes | — |
| `stage.jumpScene` | 5 | yes | StageLayer.jumpScene | no | no | yes | — |
| `stage.loadBg` | 58 | yes | BattleLayer.loadBg | yes | yes | yes | — |
| `stage.lose` | 125 | yes | BattleLayer.lose | no | no | yes | — |
| `stage.loseTest` | 58 | yes | BattleLayer.loseTest | no | no | no | — |
| `stage.maxRound` | 58 | yes | BattleLayer.maxRound | no | no | no | — |
| `stage.nearEvent` | 85 | yes | BattleLayer.nearEvent | no | no | yes | — |
| `stage.playMagicMeff` | 1 | yes | StageLayer.playMagicMeff | yes | yes | no | — |
| `stage.resumeCtrl` | 130 | yes | BattleLayer.resumeCtrl | no | no | no | — |
| `stage.retreatConfirm` | 1 | NO | BattleLayer.retreatConfirm | no | no | no | unknown-handler-source-noop |
| `stage.reward` | 155 | yes | BattleLayer.reward | yes | yes | yes | — |
| `stage.round` | 295 | yes | BattleLayer.round | no | no | no | — |
| `stage.say` | 3476 | yes | BattleLayer.say | yes | yes | no | — |
| `stage.sceneIndex` | 58 | yes | StageLayer.sceneIndex | no | no | no | — |
| `stage.setAI` | 99 | yes | BattleLayer.setAI | no | no | yes | — |
| `stage.setEnemyEquip` | 214 | yes | BattleLayer.setEnemyEquip | no | no | yes | — |
| `stage.setFire` | 55 | yes | BattleLayer.setFire | yes | yes | yes | — |
| `stage.setFires` | 77 | yes | BattleLayer.setFires | yes | yes | yes | — |
| `stage.setGlobalData` | 58 | yes | BattleLayer.setGlobalData | no | no | yes | — |
| `stage.setMaxRound` | 4 | yes | BattleLayer.setMaxRound | no | no | yes | — |
| `stage.setMenuVisible` | 116 | yes | BattleLayer.setMenuVisible | no | no | yes | — |
| `stage.setObject` | 139 | yes | BattleLayer.setObject | yes | yes | yes | — |
| `stage.setObjects` | 225 | yes | BattleLayer.setObjects | yes | yes | yes | — |
| `stage.setRectUnitHide` | 82 | yes | BattleLayer.setRectUnitHide | yes | yes | yes | — |
| `stage.setStageName` | 1 | yes | StageLayer.setStageName | yes | yes | yes | — |
| `stage.setUnitAbility` | 290 | yes | BattleLayer.setUnitAbility | no | no | yes | — |
| `stage.setUnitAttr` | 406 | yes | BattleLayer.setUnitAttr | yes | no | yes | — |
| `stage.setUnitStatus` | 74 | yes | BattleLayer.setUnitStatus | yes | yes | yes | — |
| `stage.setWinCondition` | 91 | yes | BattleLayer.setWinCondition | no | no | yes | — |
| `stage.showWinCondition` | 94 | yes | BattleLayer.showWinCondition | yes | yes | no | — |
| `stage.startFight` | 55 | yes | BattleLayer.startFight | yes | yes | yes | — |
| `stage.startOper` | 57 | yes | BattleLayer.startOper | no | no | yes | — |
| `stage.talk` | 96 | yes | BattleLayer.talk | yes | yes | no | — |
| `stage.totalRectUnit` | 119 | yes | BattleLayer.totalRectUnit | no | no | no | — |
| `stage.totalUnit` | 32 | yes | BattleLayer.totalUnit | no | no | no | — |
| `stage.unit` | 14800 | yes | BattleLayer.unit | no | no | no | — |
| `stage.unit().addLv` | 21 | yes | BattleUnit.addLv | no | no | yes | — |
| `stage.unit().heightLight` | 42 | yes | BattleUnit.heightLight | yes | yes | no | — |
| `stage.unit().hide` | 858 | yes | BattleUnit.hide | yes | yes | yes | — |
| `stage.unit().move` | 830 | yes | BattleUnit.move | yes | yes | yes | — |
| `stage.unit().retreatTxt` | 8 | yes | BattleUnit.retreatTxt | no | no | yes | — |
| `stage.unit().setAI` | 2146 | yes | BattleUnit.setAI | no | no | yes | — |
| `stage.unit().setAction` | 1298 | yes | BattleUnit.setAction | yes | yes | yes | — |
| `stage.unit().setDir` | 2417 | yes | BattleUnit.setDir | no | no | yes | — |
| `stage.unit().setPosts` | 4640 | yes | BattleUnit.setPosts | yes | yes | yes | — |
| `stage.unit().show` | 2490 | yes | BattleUnit.show | yes | yes | yes | — |
| `stage.unitAttr` | 522 | yes | BattleLayer.unitAttr | no | no | yes | — |
| `stage.unitStateTest` | 578 | yes | BattleLayer.unitStateTest | no | no | no | — |
| `stage.varOper` | 116 | yes | StageLayer.varOper | no | no | no | — |
| `stage.varTest` | 58 | yes | StageLayer.varTest | no | no | no | — |
| `stage.winTest` | 58 | yes | BattleLayer.winTest | no | no | no | — |

## Finding details

- `stage.guide` (1 calls): unknown-handler-source-noop; source signals: none
- `stage.retreatConfirm` (1 calls): unknown-handler-source-noop; source signals: none
