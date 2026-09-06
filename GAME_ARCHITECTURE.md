# 게임 아키텍처

기준일: 2026-09-07 (HEAD `5a03d03` 실측)

현재 구현 상태, 검증 기준선과 다음 명령은 [`REFACTORING_HANDOFF.md`](REFACTORING_HANDOFF.md)를 우선 참고한다.

이 프로젝트는 독립적인 Kotlin/LibGDX 게임이다. 과거 구현체는 게임 규칙과 콘텐츠를 확인하는 참고 자료일 뿐, 현재 코드의 클래스·모듈·패키지 구조를 결정하지 않는다.

## 설계 원칙

- 도메인은 LibGDX에 의존하지 않는다. 전투, 캠페인, 아이템, 시나리오 상태는 순수 Kotlin 객체로 표현한다.
- LibGDX `Screen`, `InputProcessor`, 렌더러와 asset 수명은 presentation 계층이 소유한다.
- 화면 전환과 게임 시작 설정은 application 계층이 조립한다.
- 파일, preferences, trace, framebuffer I/O는 infrastructure 또는 verification 계층에 둔다.
- 검증 harness와 fixture 화면은 production 런타임 클래스패스에서 분리한다.
- nullable flag 묶음보다 sealed interface, enum, data class를 사용해 유효한 상태만 표현한다.
- 거대한 화면 객체가 규칙을 재계산하지 않는다. 화면은 도메인 command를 전달하고 immutable view state를 렌더링한다.

## 목표 의존 방향

```text
desktop / android launchers
            │
       application
    GameApplication
    ScreenNavigator
    LaunchConfiguration
       │          │
   domain      presentation
 campaign      LibGDX screens
 battle        renderers/input
 scenario      asset lifecycles
       ▲          │
       └── infrastructure
           preferences/files/assets

verification ──> public application/domain contracts
```

의존 방향은 항상 바깥쪽에서 안쪽으로 향한다. `domain`은 `presentation`, `verification`, LibGDX 타입을 참조하지 않는다.

이 규칙은 `tools/test_package_boundaries.py`가 강제한다.

## 현재 의존 방향 위반 (31건, 미해결)

패키지 분할 자체는 목표 구조에 도달했다. 그러나 **의존 방향은 세 곳에서 역류하고 있으며 그중 하나는 순환이다.** `python3 -m unittest tools.test_package_boundaries`가 현재 3건 실패한다. 이것이 최우선 과제다.

### (1) domain → infrastructure (6건)

```
domain/campaign/CampaignState.kt:4                     import ...infrastructure.data.GameDataCatalog
domain/campaign/CampaignEquipmentProgression.kt:4      import ...infrastructure.data.GameDataCatalog
domain/campaign/CampaignInventory.kt:4                 import ...infrastructure.data.GameDataCatalog
domain/campaign/CampaignInventoryEquipmentManager.kt:4 import ...infrastructure.data.GameDataCatalog
domain/battle/BattleUnit.kt:3                          import ...infrastructure.data.GameDataCatalog
domain/battle/BattleAvatarResolver.kt:4                import ...infrastructure.data.GameDataCatalog
```

도메인 규칙이 데이터 로딩 구현을 직접 안다. 도메인이 실제로 필요로 하는 조회만 담은 read-only interface를 `domain`에 정의하고 `GameDataCatalog`가 이를 구현하게 해서 방향을 역전한다.

### (2) infrastructure → presentation / application (3건)

```
infrastructure/data/GameDataCatalog.kt:4           import ...presentation.scenario.overlay.*
infrastructure/data/GameDataCatalogUnitDomain.kt:4 import ...presentation.shared.overlay.TerrainLayer
infrastructure/audio/GameAudioPlayer.kt:7          import ...application.scenario.ScenarioStage
```

(1)과 (2)가 합쳐져 **`domain → infrastructure → presentation` 순환**을 만든다. `GameDataCatalog`가 매듭이다. `TerrainLayer` 같은 UI 레이어 타입을 카탈로그가 참조하는 것이 직접 원인이므로, 해당 데이터 타입을 domain 또는 중립 타입으로 옮긴다. `GameAudioPlayer`의 `ScenarioStage` 의존은 좁은 콜백 interface로 역전한다.

### (3) presentation → infrastructure (22건 / 18파일)

```
3  presentation/scenario/ScenarioScreen.kt
3  presentation/battle/BattleScreen.kt
1  presentation/shared/overlay/PropertyLayer.kt
1  presentation/scenario/ScenarioRuntimeSnapshotProjector.kt
1  presentation/scenario/ScenarioNavigationCoordinator.kt
1  presentation/scenario/hall/HallPropertyViewProjector.kt
1  presentation/scenario/hall/HallManagementViewFactory.kt
1  presentation/scenario/hall/HallManagementCoordinator.kt
1  presentation/scenario/hall/HallInformationCoordinator.kt
1  presentation/scenario/hall/HallEquipViewProjector.kt
1  presentation/scenario/hall/EquipConfirmationFlow.kt
1  presentation/battle/timeline/BattleMagicPresentation.kt
1  presentation/battle/preparation/BattlePreparationViewState.kt
1  presentation/battle/preparation/BattlePreparationScreen.kt
1  presentation/battle/combat/BattleMagicPresentationPlanner.kt
1  presentation/battle/combat/BattleCombatPresentationQueueCoordinator.kt
1  presentation/battle/combat/BattleCombatPresentationModels.kt
1  presentation/battle/assets/MagicEffectCatalog.kt
```

화면이 데이터 계층 구현을 직접 import한다. application이 조립해 넘긴 조회 interface만 받게 바꾼다. (1)의 조회 interface를 먼저 만들면 여기서도 재사용할 수 있다.

### 해소 순서

1. domain 조회 interface 도입 → (1) 해소
2. `GameDataCatalog`의 presentation 타입 의존 제거, `GameAudioPlayer` 콜백 역전 → (2) 해소, 순환 제거
3. presentation을 조회 interface로 전환 → (3) 해소

각 단계 후 `python3 -m unittest tools.test_package_boundaries`가 통과해야 한다.

## 모듈 경계

검증 코드는 `core`의 보조 source set이 아니라 독립 JVM 모듈 `:verification`으로 분리돼 있다. 현재 파일 수는 core 474 / desktop 2 / android 1 / verification 168이며, Gradle 의존성은 `verification -> core`, `desktop -> core`, `android -> core`의 단방향만 존재한다. production에서 `verification`으로 향하는 의존성은 0건이다.

`core`에는 관찰자가 없어도 비용과 동작 변화가 없는 production-neutral observer/snapshot 계약만 둘 수 있다. fixture 라우팅, 원본 비교, 기대값 assertion, 자동 입력 정책, trace 파일 형식과 출력, 검증용 `Gdx.app.exit()`는 모두 `:verification`이 소유한다. 검증을 위해 `internal` 구현 전체를 공개하거나 Gradle friend path에 의존하지 않고, 필요한 경우 불변 snapshot 또는 좁은 read-only probe만 공개한다.

`ScenarioBatchVerificationScreen`, `BattleCampaignE2eAdapter`, `CampaignE2eDriver`와 capture fixture, trace harness는 모두 `:verification`으로 이전 완료했다. `GameEntryPoint.SCENARIO_BATCH`, batch callback, desktop의 `--verify-all-scenarios` 분기도 삭제됐다. production 시작 경로는 검증 타입을 알지 않는다.

영천 검증은 콘텐츠가 선언한 순서인 round 1 일반 진입, round 1 camp 2 증원, round 2 화공과 `startOper`를 각각 실행한다. catalog materialization과 route 진행은 서로 다른 runtime을 사용해 검증 자체가 전투 상태를 오염시키지 않는다.

## 현재 대형 클래스 상태

300줄 초과 production 파일은 **6개**로 줄었다. 전체 목록, 예외 판단, 실행 우선순위는 [`LARGE_CLASS_REFACTORING.md`](LARGE_CLASS_REFACTORING.md)에 기록한다.

| 클래스 | 09-05 문서 | HEAD | 상태 |
|---|---:|---:|---|
| `JojoGame` | 252 | 219 | 완료. startup/navigation/runtime 분리 |
| `Battle` | 787 | 288 | 완료. aggregate root가 collaborator 조립만 담당 |
| `ScenarioInterpreter` | 277 | 268 | 완료. evaluator/dispatcher 분리 |
| `ScenarioStage` | 468 | 293 | 완료 |
| `GameDataCatalog` | 823 | 246 | 크기는 완료. **의존 방향 위반의 매듭으로 남음** |
| `CampaignState` | 247 | 225 | 완료. inventory/equipment/roster 분리 |
| `ScenarioScreen` | 4,534 | 987 | hall 63파일 분리 완료. 생성자 파라미터 19개 잔존 |
| `BattleScreen` | 9,578 | 7,094 | 협력 객체 추출 완료. 조립·진행 제어 잔존 |

## 단계별 이행 상태

1. 프로젝트 identity를 `com.jojo.game`과 `jojo-game`으로 통일한다. **완료** — legacy identity 잔존 0건. 외부 비교 trace schema의 `sourceCharacterId` JSON key 1건만 계약으로 유지한다.
2. 이름에 구현 역사만 나타내는 접미사를 제거하고 역할 이름으로 바꾼다. **완료**
3. 런처 인자와 `JojoGame`의 boolean/string 묶음을 Kotlin 설정 객체와 sealed run mode로 바꾼다. **완료**
4. trace harness와 fixture 전용 코드를 별도 verification 모듈로 이동한다. **완료**
5. 전투 도메인에서 pathfinding, combat, magic, AI를 순수 서비스로 추출한다. **완료**
6. 화면별 controller/view-state/renderer 경계를 적용하고 LibGDX resource 수명을 `Disposable` 단위로 관리한다. **부분 완료** — title, battle preparation, fight, hall은 완료. `BattleScreen`/`ScenarioScreen`의 조립 책임이 남았다.
7. **의존 방향 정상화.** 패키지는 나뉘었으나 domain/infrastructure/presentation 사이 import 방향이 31건 역류한다. **미완료, 최우선.**

각 단계는 compile, unit test, `test_package_boundaries`, 실제 `InputProcessor` 흐름 검증 순서로 확인한다. 과거 구현체와의 비교 도구가 필요해도 그 용어가 production API, 패키지, 리소스 정체성으로 다시 유입되어서는 안 된다.

## 구현 완료 기록

`BattleScreen`의 presentation 이행: 화면이 직접 보유하던 정적 UI texture, 동적 unit/effect/icon cache, UnitInfo lazy texture와 비교용 framebuffer는 역할별 `Disposable` 소유자로 이동했다. 자원 객체는 avatar 선택이나 전투 규칙을 판단하지 않고 이미 계산된 resource key만 받으며, 화면은 각 소유자를 한 번만 폐기한다.

Fight 연출: mutable `FightPresentationState`를 매 frame 깊은 불변 snapshot으로 복사한 뒤 화면에서 이름·얼굴·avatar를 해석해 완성된 `FightPresentationView`를 만든다. `BattleFightRenderer`는 이 뷰와 빌려 쓴 LibGDX 자원만 읽으며 전투 aggregate, catalog, scenario runtime을 알지 않는다.

전투 도메인: `BattleState` 호환 별칭을 제거하고 이동·능력치·누적 확률과 난수 판정을 각각 `BattleMovementPlanner`, `BattleAttributeCalculator`, `BattleProbabilityResolver`로 옮겼다. `Battlefield`는 active/퇴각 연출 유닛의 두 ordered collection과 점유, 숨김·복귀, topology snapshot을 소유한다. `BattleUnitMemento`와 `BattleActionTransaction`은 계산 시점의 깊은 snapshot/rollback과 애니메이션 callback 시점의 단계별 commit을 맡는다. 물리 전투는 `PhysicalDamageCalculator`, `PhysicalTargetResolver`, `PhysicalAttackAreaResolver`, `PhysicalCombatAccumulator`, `PhysicalCombatResolver`로, 마법은 `domain/battle/magic`으로, 턴 정산은 `domain/battle/settlement`와 `domain/battle/turn`으로 분리됐다.
