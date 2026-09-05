# Battle stage API surface audit

Scanned 58 S_*.py scripts, 42516 call sites, and 74 APIs.

Findings: **2 APIs** ({"unknown-handler-source-noop": 2}).

Blocking gate findings: **0 APIs** ({}).

| API | Calls | Runtime | Reference | Source barrier | Game battle barrier | Eager mutation | Findings |
|---|---:|---|---|---|---|---|---|
| `stage.attackAction` | 28 | yes | BattleScreen.attackAction | yes | yes | yes | — |
| `stage.bgSound` | 146 | yes | StageLayer.bgSound | no | no | yes | — |
| `stage.center` | 18 | yes | BattleScreen.center | no | no | no | — |
| `stage.choice` | 72 | yes | BattleScreen.choice | yes | yes | no | — |
| `stage.countDir` | 2173 | yes | BattleScreen.countDir | no | no | yes | — |
| `stage.createEnemy` | 58 | yes | BattleScreen.createEnemy | no | no | yes | — |
| `stage.createFriend` | 22 | yes | BattleScreen.createFriend | no | no | yes | — |
| `stage.createMine` | 60 | yes | BattleScreen.createMine | no | no | yes | — |
| `stage.curCamp` | 208 | yes | BattleScreen.curCamp | no | no | no | — |
| `stage.delay` | 248 | yes | StageLayer.delay | yes | yes | no | — |
| `stage.draw` | 58 | yes | BattleScreen.draw | no | no | yes | — |
| `stage.effectSound` | 37 | yes | StageLayer.effectSound | no | no | yes | — |
| `stage.end` | 260 | yes | StageLayer.end | no | no | yes | — |
| `stage.getItem` | 286 | yes | BattleScreen.getItem | yes | yes | yes | — |
| `stage.guide` | 1 | NO | BattleScreen.guide | no | no | no | unknown-handler-source-noop |
| `stage.heightLight` | 41 | yes | BattleScreen.heightLight | yes | yes | no | — |
| `stage.incSceneIdx` | 58 | yes | StageLayer.incSceneIdx | no | no | yes | — |
| `stage.info` | 157 | yes | StageLayer.info | yes | yes | yes | — |
| `stage.initFight` | 58 | yes | BattleScreen.initFight | no | no | yes | — |
| `stage.isInPos` | 220 | yes | BattleScreen.isInPos | no | no | no | — |
| `stage.isInRect` | 196 | yes | BattleScreen.isInRect | no | no | no | — |
| `stage.isLose` | 58 | yes | BattleScreen.isLose | no | no | no | — |
| `stage.isNear` | 231 | yes | BattleScreen.isNear | no | no | no | — |
| `stage.itemVars` | 51 | yes | BattleScreen.itemVars | no | no | yes | — |
| `stage.jumpScene` | 5 | yes | StageLayer.jumpScene | no | no | yes | — |
| `stage.loadBg` | 58 | yes | BattleScreen.loadBg | yes | yes | yes | — |
| `stage.lose` | 125 | yes | BattleScreen.lose | no | no | yes | — |
| `stage.loseTest` | 58 | yes | BattleScreen.loseTest | no | no | no | — |
| `stage.maxRound` | 58 | yes | BattleScreen.maxRound | no | no | no | — |
| `stage.nearEvent` | 85 | yes | BattleScreen.nearEvent | no | no | yes | — |
| `stage.playMagicMeff` | 1 | yes | StageLayer.playMagicMeff | yes | yes | no | — |
| `stage.resumeCtrl` | 130 | yes | BattleScreen.resumeCtrl | no | no | no | — |
| `stage.retreatConfirm` | 1 | NO | BattleScreen.retreatConfirm | no | no | no | unknown-handler-source-noop |
| `stage.reward` | 155 | yes | BattleScreen.reward | yes | yes | yes | — |
| `stage.round` | 295 | yes | BattleScreen.round | no | no | no | — |
| `stage.say` | 3476 | yes | BattleScreen.say | yes | yes | no | — |
| `stage.sceneIndex` | 58 | yes | StageLayer.sceneIndex | no | no | no | — |
| `stage.setAI` | 99 | yes | BattleScreen.setAI | no | no | yes | — |
| `stage.setEnemyEquip` | 214 | yes | BattleScreen.setEnemyEquip | no | no | yes | — |
| `stage.setFire` | 55 | yes | BattleScreen.setFire | yes | yes | yes | — |
| `stage.setFires` | 77 | yes | BattleScreen.setFires | yes | yes | yes | — |
| `stage.setGlobalData` | 58 | yes | BattleScreen.setGlobalData | no | no | yes | — |
| `stage.setMaxRound` | 4 | yes | BattleScreen.setMaxRound | no | no | yes | — |
| `stage.setMenuVisible` | 116 | yes | BattleScreen.setMenuVisible | no | no | yes | — |
| `stage.setObject` | 139 | yes | BattleScreen.setObject | yes | yes | yes | — |
| `stage.setObjects` | 225 | yes | BattleScreen.setObjects | yes | yes | yes | — |
| `stage.setRectUnitHide` | 82 | yes | BattleScreen.setRectUnitHide | yes | yes | yes | — |
| `stage.setStageName` | 1 | yes | StageLayer.setStageName | yes | yes | yes | — |
| `stage.setUnitAbility` | 290 | yes | BattleScreen.setUnitAbility | no | no | yes | — |
| `stage.setUnitAttr` | 406 | yes | BattleScreen.setUnitAttr | yes | no | yes | — |
| `stage.setUnitStatus` | 74 | yes | BattleScreen.setUnitStatus | yes | yes | yes | — |
| `stage.setWinCondition` | 91 | yes | BattleScreen.setWinCondition | no | no | yes | — |
| `stage.showWinCondition` | 94 | yes | BattleScreen.showWinCondition | yes | yes | no | — |
| `stage.startFight` | 55 | yes | BattleScreen.startFight | yes | yes | yes | — |
| `stage.startOper` | 57 | yes | BattleScreen.startOper | no | no | yes | — |
| `stage.talk` | 96 | yes | BattleScreen.talk | yes | yes | no | — |
| `stage.totalRectUnit` | 119 | yes | BattleScreen.totalRectUnit | no | no | no | — |
| `stage.totalUnit` | 32 | yes | BattleScreen.totalUnit | no | no | no | — |
| `stage.unit` | 14800 | yes | BattleScreen.unit | no | no | no | — |
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
| `stage.unitAttr` | 522 | yes | BattleScreen.unitAttr | no | no | yes | — |
| `stage.unitStateTest` | 578 | yes | BattleScreen.unitStateTest | no | no | no | — |
| `stage.varOper` | 116 | yes | StageLayer.varOper | no | no | no | — |
| `stage.varTest` | 58 | yes | StageLayer.varTest | no | no | no | — |
| `stage.winTest` | 58 | yes | BattleScreen.winTest | no | no | no | — |

## Finding details

- `stage.guide` (1 calls): unknown-handler-source-noop; source signals: none
- `stage.retreatConfirm` (1 calls): unknown-handler-source-noop; source signals: none
