# 300줄 초과 클래스 감사와 분해 순서

기준일: 2026-09-07 (HEAD `5a03d03` 실측)

다른 에이전트가 이어서 작업할 때는 현재 작업 트리와 미완료 tranche까지 정리한 [`REFACTORING_HANDOFF.md`](REFACTORING_HANDOFF.md)를 먼저 읽는다.

> **계측 주의.** 작업 트리에 516파일 / +40,762줄의 미커밋 자동 생성 주석이 있어 파일 크기가 최대 3배까지 부풀어 보인다 (`BattleScreen` 7,094 → 11,471). 이 문서의 모든 수치는 HEAD 기준이며, 재계측할 때도 반드시 HEAD를 대상으로 한다.
>
> ```bash
> git ls-tree -r HEAD --name-only \
>   | grep -E '^(core|desktop|android)/src/main/.*\.kt$' \
>   | while read f; do echo "$(git show HEAD:$f | wc -l) $f"; done \
>   | sort -rn | awk '$1>300'
> ```

## 판단 기준

- 줄 수만 맞추기 위한 위임 클래스는 만들지 않는다.
- 상태와 행동이 함께 있어야 하는 응집된 상태 머신은 300줄을 넘더라도 유지할 수 있다.
- 렌더링, 입력, 도메인 규칙, 저장, 검증 I/O 중 둘 이상을 소유하면 분리 대상이다.
- 추출한 도메인 객체는 LibGDX 없이 단위 테스트할 수 있어야 한다.
- presentation은 도메인 command와 immutable view state만 사용하며 규칙을 다시 계산하지 않는다.
- verification 전용 코드는 production 클래스패스에서 제거한다.
- 파일이 300줄을 넘어도 개별 클래스 선언이 작은 경우는 감사 대상에서 제외한다.

## production 300줄 초과 전수 (6개)

`core`/`desktop`/`android`의 production Kotlin 소스 477파일 중 300줄을 넘는 것은 6개다.

| 파일 | HEAD | 클래스 구성 | 판단 | 우선순위 |
|---|---:|---|---|---:|
| `presentation/battle/BattleScreen.kt` | 7,094 | 단일 `class BattleScreen` (fun 209, 프로퍼티 245) | 협력 객체는 이미 충분히 추출됐고, 화면이 그 **조립과 진행 제어**를 전부 쥔 것이 남은 문제다 | P0 |
| `presentation/scenario/ScenarioScreen.kt` | 987 | 단일 `class ScenarioScreen` (fun 66, 프로퍼티 82, **생성자 파라미터 19**) | hall 기능은 분리 완료. 생성자 파라미터 19개가 조립 책임 과다를 드러낸다 | P1 |
| `presentation/battle/fight/FightPresentationState.kt` | 343 | 단일 연출 상태 머신 | **예외 인정.** 상태와 전이가 함께 있어야 한다 | P3 |
| `application/scenario/ScenarioTacticalActionDispatcher.kt` | 314 | dispatch 분기 | 경계선. 시나리오 액션이 늘면 분리 | P3 |
| `domain/battle/combat/PhysicalCombatResolver.kt` | 313 | 다단계 패스 오케스트레이션 | **예외 인정.** 계산은 이미 5개 협력 객체로 분리됨 | P3 |
| `domain/battle/magic/MagicTargetResolver.kt` | 302 | 마법 대상 해결 | 경계선. 현 상태 유지 | P3 |

### 감시 대상 (250~300줄)

경계에 근접해 있어 추가 기능이 붙으면 바로 초과한다.

```
295 application/scenario/battle/ScenarioStageBattleSetup.kt
293 application/scenario/ScenarioRuntime.kt
288 application/battle/Battle.kt
286 application/battle/ai/BattleAiTurnResolver.kt
285 domain/battle/combat/PhysicalTargetResolver.kt
272 presentation/battle/timeline/BattleCharacterPresentation.kt
270 presentation/battle/fixture/BattleCharacterRouteFixtureController.kt
268 application/scenario/ScenarioInterpreter.kt
255 presentation/battle/preparation/BattlePreparationRenderer.kt
255 domain/battle/BattleUnit.kt
254 presentation/battle/overlay/BattleInformationOverlayController.kt
250 application/battle/movement/BattleMovementCoordinator.kt
```

### `:verification` 모듈 (production 감사 대상 아님)

검증 모듈에도 300줄 초과가 7개 있다. production 판정에는 포함하지 않지만 `CampaignE2eTrace`(911줄)는 별도 분해가 필요하다.

```
911 verification/campaign/CampaignE2eTrace.kt
366 presentation/battle/edit/BattleUnitEditLayer.kt
362 verification/campaign/BattleScenarioVerificationPolicies.kt
344 presentation/overlay/fixture/SystemOverlayFixtureScreen.kt
330 verification/ProgressionLayerTraceHarness.kt
326 verification/trace/FullBattleTraceFrameProjection.kt
316 presentation/battle/edit/EditAdminFlows.kt
```

## 완료된 분해

2026-09-05판 문서가 "분리 필요"로 남겨 둔 항목은 대부분 완료됐다. 아래는 09-05 문서의 주장값과 현재 HEAD 실측의 대조다.

| 클래스 | 09-05 문서 | HEAD | 결과 |
|---|---:|---:|---|
| `Battle` | 787 | **288** | `application/battle` 18파일 + 7개 하위 패키지, `domain/battle` 23파일 + 5개 하위 패키지로 분해 |
| `ScenarioStage` (`ScenarioRuntime.kt`) | 468 | **293** | 맵오브젝트/날씨/유닛 레지스트리 등 분리 완료 |
| `ScenarioInterpreter` | 277 | **268** | `application/scenario` 44파일로 분해 |
| `GameDataCatalog` | 823 | **246** | `infrastructure/data` 15파일로 분할 |
| `JojoGame` | 252 | **219** | startup/navigation/runtime 분리 |
| `CampaignState` | 247 | **225** | inventory/equipment/roster 분리 |
| `BattleScenarioFactory` | 302 | **47** | 선언적 데이터 외부화 |
| `BattleCampaignE2eAdapter` | 583 | 삭제 | `:verification` 이전 |
| `CampaignE2eDriver` | 320 | 삭제 | `:verification` 이전 |
| `ScenarioBatchVerificationScreen` | 삭제 | 삭제 | headless application으로 이전 |
| `TitleScreen` | 185 | 185 이하 | controller/view/renderer/assets 경계 유지 |
| `BattlePreparationScreen` | 172 | 172 이하 | controller/view/renderer/assets 경계 유지 |
| `BattleUnit` | 219 | **255** | 표시 상태는 `BattleUnitPresentationState`로 분리 유지 |
| `ScenarioScreen` | 4,534 | **987** | hall 63파일 분리 완료, 조립 책임 잔존 |
| `BattleScreen` | 9,578 | **7,094** | assets/fight/overlay/render/timeline/input 분리, 조립·진행 제어 잔존 |

## 현재 패키지 구조 (core production 474파일)

```
application/    battle(18) battle/ai(5) battle/bootstrap(2) battle/combat(5)
                battle/experience(3) battle/movement(3) battle/presentation(4)
                battle/round(3) campaign(1) hall(1) navigation(2) platform(1)
                runtime(20) scenario(44) scenario/battle(3)

domain/         battle(23) battle/combat(6) battle/command(5) battle/magic(5)
                battle/settlement(4) battle/turn(2) campaign(9) scenario(11)

infrastructure/ audio(1) data(15) preferences(1) security(1)

presentation/   battle(2) battle/ai(2) battle/assets(7) battle/bootstrap(2)
                battle/combat(5) battle/edit(2) battle/edit/evidence(5)
                battle/evidence(13) battle/fight(10) battle/fixture(14)
                battle/input(3) battle/outcome(3) battle/overlay(29)
                battle/preparation(7) battle/render(11) battle/route(1)
                battle/script(7) battle/settlement(3) battle/timeline(9)
                battle/trace(4) battle/unit(9) battle/verification(2)
                hall/evidence(1) scenario(9) scenario/assets(1)
                scenario/hall(51) scenario/hall/render(12) scenario/input(5)
                scenario/overlay(7) scenario/render(3) scenario/story(2)
                scenario/trace(1) shared(4) shared/evidence(1)
                shared/overlay(21) title(6) title/assets(1)
```

패키지 분할 자체는 목표 구조에 도달했다. 남은 문제는 **크기가 아니라 의존 방향**이다. `domain -> infrastructure -> presentation` 순환을 포함한 위반 31건은 [`GAME_ARCHITECTURE.md`](GAME_ARCHITECTURE.md)와 `tools/test_package_boundaries.py`에 기록돼 있다.

## 다음 실행 순서

1. **패키지 경계 위반 해소** — `tools/test_package_boundaries` 3건 실패. `GameDataCatalog` 매듭부터 끊는다. 상세는 `REFACTORING_HANDOFF.md`의 P0 항목.
2. **`BattleScreen` 조립 책임 분리** — `BattleInputController`, `BattlePresentationCoordinator`, `BattleSceneRenderer`, verification observer 잔여분 이전.
3. **`ScenarioScreen` 생성자 파라미터 축소** — 묶음 DTO가 아니라 실제 소유자에게 책임을 넘겨 19개를 줄인다.
4. **`CampaignE2eTrace`(911줄) 분해** — verification 모듈 내부에서 auto-player와 recorder로 분리.

각 단계는 기존 공개 동작을 characterization test로 고정하고, 추출된 객체의 단위 테스트, core 전체 테스트, `test_package_boundaries`, desktop/android compile 순서로 검증한다. 추출한 새 객체도 300줄 미만을 유지하며, 줄 수만 옮긴 context/gateway 객체는 완료로 인정하지 않는다.
