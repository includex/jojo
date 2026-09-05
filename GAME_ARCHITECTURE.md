# 게임 아키텍처

작성일: 2026-09-05

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

## 검증 모듈 경계

검증 코드는 `core`의 보조 source set이 아니라 독립 JVM 모듈 `:verification`으로 분리한다. 최종 Gradle 의존성은 `verification -> core`, `desktop -> core`, `android -> core`의 단방향만 허용하며, 게임 모듈과 플랫폼 런처는 검증 모듈을 참조하지 않는다.

`core`에는 관찰자가 없어도 비용과 동작 변화가 없는 production-neutral observer/snapshot 계약만 둘 수 있다. fixture 라우팅, 원본 비교, 기대값 assertion, 자동 입력 정책, trace 파일 형식과 출력, 검증용 `Gdx.app.exit()`는 모두 `:verification`이 소유한다. 검증을 위해 `internal` 구현 전체를 공개하거나 Gradle friend path에 의존하지 않고, 필요한 경우 불변 snapshot 또는 좁은 read-only probe만 공개한다.

첫 이행 단위는 완료됐다. 403줄의 `ScenarioBatchVerificationScreen`을 제거하고 `ScenarioBatchVerificationApplication`, 실행 suite, 시나리오 catalog 검증기, 영천 route 검증기, 전투 catalog 검증기로 나눠 `:verification`으로 옮겼다. `GameEntryPoint.SCENARIO_BATCH`, `JojoGame`/`GameStartupCoordinator`의 batch callback, desktop의 `--verify-all-scenarios` 분기도 삭제했다. 따라서 production 시작 경로는 검증 타입을 알지 않으며 `core`/`desktop`/`android`에서 `verification`으로 향하는 의존성도 없다.

영천 검증은 콘텐츠가 선언한 순서인 round 1 일반 진입, round 1 camp 2 증원, round 2 화공과 `startOper`를 각각 실행한다. catalog materialization과 route 진행은 서로 다른 runtime을 사용해 검증 자체가 전투 상태를 오염시키지 않는다. 남은 E2E driver, adapter, trace harness와 capture fixture는 같은 모듈 경계로 후속 이동한다.

## 현재 대형 클래스의 목표 분해

300줄 초과 production 클래스의 전체 목록, 예외 판단, 실행 우선순위는 [`LARGE_CLASS_REFACTORING.md`](LARGE_CLASS_REFACTORING.md)에 기록한다.

| 현재 클래스 | 목표 역할 |
|---|---|
| `JojoGame` | `GameApplication`과 `ScreenNavigator`로 분리. 생성자 옵션은 `LaunchConfiguration` 하나로 통합 |
| `BattleScreen` | `BattleInputController`, `BattleSequencePlayer`, overlay renderer들로 추가 분리 |
| `Battle` | aggregate root 이름을 유지하고 `Battlefield`, `BattleMovementPlanner`, `BattleAttributeCalculator`, `PhysicalCombatResolver`, `MagicResolver`, `BattleAiPlanner`를 조립 |
| `ScenarioScreen` | `ScenarioPlaybackController`, `HallController`, 화면별 renderer로 추가 분리 |
| `ScenarioInterpreter` | evaluator와 `ScenarioCommandSink`로 분리 |
| `CampaignStore` | `CampaignRepository`와 `CampaignService`로 분리 |
| `GameDataCatalog` | `GameDataRepository`와 immutable catalog들로 분리 |
| `CampaignE2eTrace` | production 밖의 driver, observer, recorder로 분리 |

## 단계별 이행

1. 프로젝트 identity를 `com.jojo.game`과 `jojo-game`으로 통일한다.
2. 이름에 구현 역사만 나타내는 접미사를 제거하고 역할 이름으로 바꾼다.
3. 런처 인자와 `JojoGame`의 boolean/string 묶음을 Kotlin 설정 객체와 sealed run mode로 바꾼다.
4. trace harness와 fixture 전용 코드를 별도 verification source set 또는 모듈로 이동한다.
5. 전투 도메인에서 pathfinding, combat, magic, AI를 순수 서비스로 추출한다.
6. 화면별 controller/view-state/renderer 경계를 적용하고 LibGDX resource 수명을 `Disposable` 단위로 관리한다.

각 단계는 compile, unit test, 실제 `InputProcessor` 흐름 검증 순서로 확인한다. 과거 구현체와의 비교 도구가 필요해도 그 용어가 production API, 패키지, 리소스 정체성으로 다시 유입되어서는 안 된다.

현재 identity 이행은 완료됐다. production 패키지는 `com.jojo.game`, 프로젝트 이름은 `jojo-game`이며 저장 codec·마법 효과 모델·게임 데이터 변수·설정 저장소는 각각 역할 이름을 사용한다. preferences namespace는 `jojo-game-campaign`과 `jojo-game-settings`로 중앙화했고, 신규 게임이므로 이전 namespace fallback은 두지 않는다. 복구 소스 비교를 실제로 수행하는 도구와 계약 테스트만 `Source*`/`source_*` 어휘를 사용한다.

`BattleScreen`의 첫 presentation 이행도 완료됐다. 화면이 직접 보유하던 정적 UI texture, 동적 unit/effect/icon cache, UnitInfo lazy texture와 비교용 framebuffer는 역할별 `Disposable` 소유자로 이동했다. 자원 객체는 avatar 선택이나 전투 규칙을 판단하지 않고 이미 계산된 resource key만 받으며, 화면은 각 소유자를 한 번만 폐기한다.

Fight 연출은 mutable `FightPresentationState`를 매 frame 깊은 불변 snapshot으로 복사한 뒤 화면에서 이름·얼굴·avatar를 해석해 완성된 `FightPresentationView`를 만든다. `BattleFightRenderer`는 이 뷰와 빌려 쓴 LibGDX 자원만 읽으며 전투 aggregate, catalog, scenario runtime을 알지 않는다. slot은 builder에서 한 번 결정되고 최종 view에 중복 index를 남기지 않는다.

전투 도메인의 첫 규칙 경계도 완성됐다. `BattleState` 호환 별칭을 제거하고, 이동·능력치·누적 확률과 난수 판정을 각각 `BattleMovementPlanner`, `BattleAttributeCalculator`, `BattleProbabilityResolver`로 옮겼다. resolver는 LibGDX나 화면을 모르며 `Battle`은 판정 순서만 조립한다. `sourceCharacterId`, `sourceBattleSlot`, `sourceHarm` 같은 내부 모델 이름도 `characterId`, `battleSlot`, `resolvedHarm`으로 바로잡았으며 호환 alias는 두지 않았다.

`Battlefield`는 active/퇴각 연출 유닛의 두 ordered collection과 점유, 숨김·복귀, topology snapshot을 소유한다. `Battle`은 이 컬렉션의 backing map을 더 이상 직접 변경하지 않으며 외부에는 구조 변경 불가능한 live view만 제공한다. `BattleUnitMemento`와 `BattleActionTransaction`은 계산 시점의 깊은 snapshot/rollback과 애니메이션 callback 시점의 단계별 commit을 맡는다. 다음 경계는 이 transaction 위에서 물리 공격 결과 계산과 적용을 `PhysicalCombatResolver`로 분리하는 것이다.
