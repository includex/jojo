# 300줄 초과 클래스 감사와 분해 순서

기준일: 2026-09-05

다른 에이전트가 이어서 작업할 때는 현재 작업 트리와 미완료 tranche까지 정리한 [`REFACTORING_HANDOFF.md`](REFACTORING_HANDOFF.md)를 먼저 읽는다.

`core`, `desktop`, `android`의 production Kotlin 소스를 클래스 선언 경계로 계측했다. 파일이 300줄을 넘더라도 여러 작은 타입을 모아 둔 경우는 대상에서 제외했다. 반대로 한 클래스 선언이 300줄을 넘으면 아래 표에 모두 포함했다.

## 판단 기준

- 줄 수만 맞추기 위한 위임 클래스는 만들지 않는다.
- 상태와 행동이 함께 있어야 하는 응집된 상태 머신은 300줄을 넘더라도 유지할 수 있다.
- 렌더링, 입력, 도메인 규칙, 저장, 검증 I/O 중 둘 이상을 소유하면 분리 대상이다.
- 추출한 도메인 객체는 LibGDX 없이 단위 테스트할 수 있어야 한다.
- presentation은 도메인 command와 immutable view state만 사용하며 규칙을 다시 계산하지 않는다.
- verification 전용 코드는 production 클래스패스에서 제거한다.

## 전수 감사

| 클래스 | 약식 크기 | 현재 책임과 판단 | 목표 구조 | 우선순위 |
|---|---:|---|---|---:|
| `BattleScreen` | 9,578 | **자원 수명과 Fight 렌더 경계 분리 완료.** 여전히 입력, 전투 진행, 다른 overlay draw와 trace를 소유해 추가 분리가 필수다. 정적 HUD·overlay·UnitInfo, 동적 texture cache와 capture reference는 162줄 이하 `Disposable` 소유자로 이동했고 Fight draw는 204줄 renderer가 맡는다. | `BattleInputController`, `BattlePresentationCoordinator`, `BattleSceneRenderer`, 나머지 overlay renderer, verification observer | P0 |
| `ScenarioScreen` | 4,534 | 시나리오 재생과 회관의 구매·판매·장비·정보 UI, 입력, render log가 결합돼 있다. | `ScenarioPlaybackController`, `HallController`, `ScenarioRenderer`, `HallRenderer`, 화면별 input handler | P0 |
| `Battle` | 787 | **도메인 엔진 분해 대폭 완료.** 전투 상태, 물리 공격, 마법, AI 계획/점수, 턴 정산, 이동, 경험치, 전장 환경 등이 20개의 순수 Kotlin 협력 객체로 분리됐다. (3,468줄 → 787줄) | `Battle` (최종 300줄 이하 조율: 상태/설정 분리) | P1 |
| `ScenarioInterpreter` | 277 | **완료.** AST 순회, 표현식 평가, 문장 실행, 조건식, 모달/대화/선택/전투 디스패치가 19개의 단일 책임 협력 객체로 분리됐다. (1,844줄 → 277줄, 전 객체 <= 300줄 엄수) | 현재 분해 구조 유지 | 완료 |
| `ScenarioStage` | 468 | **1차 분리 완료.** `ScenarioRuntime.kt`에서 유닛 레지스트리, 이동 코디네이터, 연출 코디네이터, 일기토 코디네이터, 시나리오 자율 재생기가 추출됐다. (1,082줄 → 468줄) | `ScenarioStage` (최종 300줄 이하 조율: 맵오브젝트/날씨 분리) | P1 |
| `GameDataCatalog` | 823 | 리소스 I/O·복호화·파싱은 `GameDataRepository`로 분리됐다. 아직 여러 도메인 catalog 조회가 한 객체에 모여 있다. | `UnitCatalog`, `EquipmentCatalog`, `MagicCatalog`, `TerrainCatalog`로 추가 분리 | P1 |
| `CampaignState` | 247 | **완료.** unit/global/info/talent/level 상태만 유지한다. 인벤토리·장비는 287줄의 `CampaignInventory`, 장비 성장은 95줄의 `CampaignEquipmentProgression`, 전투 명단은 84줄의 `CampaignRoster`로 분리했다. 각 collection은 read-only view와 의도 기반 command로 캡슐화했다. | 현재 aggregate와 세 collaborator 경계 유지 | 완료 |
| `TitleScreen` | 185 | **완료.** lifecycle·입력·navigation만 유지한다. 73줄의 `TitleSceneAssets`가 자원 수명을, 205줄의 `TitleSceneRenderer`가 draw를, 138줄의 LibGDX 비의존 `TitleRenderEventRecorder`가 검증 로그를 담당한다. renderer와 recorder는 34줄의 immutable `TitleViewState`만 읽는다. | 현재 controller/view/renderer/assets/verification 경계 유지 | 완료 |
| `BattleCampaignE2eAdapter` | 583 | production 상태를 대규모 검증 DTO로 투영한다. 게임 기능이 아니다. | verification 모듈로 이동하고 `BattleStateProbe` + projection mapper로 분리 | P0 |
| `JojoGame` | 252 | **1차 완료.** startup 정책과 capture fixture 분기를 각각 `GameStartupCoordinator`, `CaptureFixtureStartupRouter`로 분리해 LibGDX lifecycle·조립·화면 전환 facade만 남겼다. capture router는 verification source set 이동 전의 명시적 경계다. | 검증 router를 별도 launcher/source set으로 이동하고 남은 캠페인 facade·artifact 위임을 정리 | P1 |
| `BattlePreparationScreen` | 172 | **완료.** lifecycle·입력·화면 전환만 유지한다. 선택 규칙은 74줄의 순수 Kotlin `BattlePreparationController`, draw는 215줄의 `BattlePreparationRenderer`, 자원 수명은 76줄의 `BattlePreparationAssets`, 검증 로그는 97줄의 LibGDX 비의존 `BattlePreparationTraceRecorder`가 맡는다. `show`가 입력을 설치하고 `hide`/`dispose`는 자신이 현재 processor일 때만 해제한다. | 현재 controller/view/renderer/assets/verification 경계 유지; recorder는 verification 모듈 이동 시 함께 이전 | 완료 |
| `FightPresentationState` | 431 | 하나의 deterministic 연출 상태 머신으로 응집도가 높다. 크기 자체는 허용 가능하다. | 모델 타입은 별도 파일로 옮기고, 상태 머신은 유지; 필요할 때 timeline builder만 추출 | P3 |
| `ScenarioBatchVerificationScreen` | 삭제 | **완료.** production의 403줄 screen과 batch 시작 분기를 제거했다. `:verification`의 `ScenarioBatchVerificationApplication`(32), suite(21), catalog/route 검증기(204 이하)로 분리했으며 `ApplicationAdapter`가 headless 수명을 소유한다. | `verification → core` 단방향과 작은 검증기 경계 유지 | 완료 |
| `BattleUnit` | 219 | **완료.** 전투 도메인 수치만 유지하고 animation, HP bar, harm number/bar, 상태 icon, idle action 정책을 147줄의 `BattleUnitPresentationState`로 분리했다. 두 객체 사이에는 불변 입력 DTO만 사용해 presentation에서 `BattleUnit` 구체 타입을 참조하지 않는다. | 현재 경계 유지; 전투 aggregate 재구성 시 명칭을 `BattleUnitState`로 통일할지 결정 | 완료 |
| `CampaignE2eDriver` | 320 | 자동 입력 정책, 진행 상태, trace 기록을 함께 가진 검증 driver다. | verification 모듈에서 `CampaignAutoPlayer`, `CampaignTraceRecorder`로 분리 | P1 |
| `BattleScenarioFactory` | 302 | 작은 factory 메서드와 선언적 기본 전투 데이터가 대부분이다. 현재 크기는 특별 사유가 인정된다. | 유지하되 시나리오가 늘면 콘텐츠별 factory로 분리 | P3 |

`BattleTurnController.kt`, `BattleSettlementPlan.kt`, `BattleScenarioVerificationPolicies.kt`, `BattleScreenPresentationModels.kt`는 파일은 300줄을 넘지만 개별 클래스가 300줄을 넘지 않는다. 파일명과 패키지 응집도는 후속 정리 대상이나, 불필요한 클래스 생성으로 줄 수만 줄이지 않는다.

## 목표 패키지와 의존성

```text
com.jojo.game.application
  GameApplication, ScreenNavigator, GameLaunchConfiguration

com.jojo.game.domain.battle
  Battle
  Battlefield
  movement/BattleMovementPlanner
  combat/BattleAttributeCalculator, PhysicalCombatResolver, MagicResolver
  turn/BattleTurnController, TurnSettlementService
  ai/BattleAiPlanner

com.jojo.game.domain.campaign
  CampaignState, CampaignService, CampaignRepository

com.jojo.game.domain.scenario
  ScenarioProgram, ScenarioInterpreter, ScenarioCommand

com.jojo.game.presentation
  title/*, scenario/*, battle/*

com.jojo.game.infrastructure
  data/*, preferences/*, files/*, audio/*

com.jojo.game.verification
  별도 source set 또는 모듈의 driver, probe, recorder, fixture
```

도메인은 presentation과 LibGDX를 참조하지 않는다. application은 화면 생성과 전환만 조립하고, infrastructure 구현은 interface 뒤에서 주입한다. verification은 application/domain의 공개 관찰 계약만 사용한다.

## 실행 순서

1. `BattleMovementPlanner`를 추출해 `Battle`의 첫 순수 규칙 경계를 만든다. **완료** — 이동 영역, 안정 weighted path, scripted destination, empty-position 탐색을 213줄의 순수 Kotlin 객체로 분리하고 35개 관련 테스트를 통과했다.
2. 단방향 `verification -> core` 의존성을 갖는 독립 JVM 모듈을 만들고 E2E driver, adapter, batch screen, trace harness를 옮긴다. **1단계 완료** — batch screen은 작은 검증기와 headless application으로 분리해 이동했고 production의 batch 시작 경로를 제거했다. 남은 E2E driver, adapter, trace harness와 capture fixture를 계속 이전한다.
3. `JojoGame`에서 화면 전환을 `ScreenNavigator`로, 캠페인 작업을 facade로 옮긴다.
4. `BattleScreen`의 trace/capture와 asset 저장소를 먼저 떼고, 입력과 연출 coordinator를 분리한다.
5. `ScenarioScreen`에서 회관 기능을 독립 controller/renderer로 분리한다.
6. `Battle`의 combat, magic, AI, settlement를 순수 서비스로 순차 추출한다.
7. `ScenarioStage`와 `ScenarioInterpreter`를 command/evaluator 경계로 재구성한다.
8. 데이터와 캠페인 aggregate를 작은 repository/service로 나눈 뒤 최종 의존성 감사를 수행한다.

## `Battle` 분해 설계

`Battle`은 공개 command를 받는 aggregate root로 유지한다. 이름만 바꾼 거대한 `BattleState`를 새로 만들거나 기존 구현 전체를 감싼 facade는 만들지 않는다. 내부 책임은 다음 순서로 실제 데이터와 규칙 소유권을 옮긴다.

1. **완료.** 과도기 `BattleState = Battle` typealias와 모든 호출자를 제거한다. 신규 게임 코드에는 이전 이름을 위한 호환 계층을 두지 않는다.
2. **완료.** 능력치 buff, private defense, 최종 이동력, 물리 저항을 `BattleAttributeCalculator`의 순수 계산으로 옮긴다. 이 객체는 aggregate나 LibGDX를 참조하지 않고 `BattleUnit` 값과 명시적 날씨만 받는다.
3. **완료.** 누적 확률 gauge, 명중·치명타·연속 공격·상태 지속시간의 난수 소비를 `BattleProbabilityResolver`로 옮긴다. gauge 종류는 숫자 상수 대신 `BattleRateGauge`로 표현하고, aggregate는 판정 순서만 조립한다.
4. **완료.** active/presentation unit collection, 점유 조회와 퇴각/복귀를 `Battlefield` 한 곳으로 옮긴다. 이후 전투 resolver가 `Battle`의 map 두 개를 직접 조작하지 않게 한다.
5. **완료.** 계산 후 애니메이션 callback 시점에 적용하는 snapshot/restore 책임을 `BattleActionTransaction`으로 옮긴다. transaction은 전투 규칙을 계산하지 않고 memento와 staged domain effect만 관리한다.
6. **진행 중.** 피해·상태 부여를 `PhysicalCombatResolver` 계열(`PhysicalDamageCalculator`, `PhysicalTargetResolver`, `PhysicalAttackAreaResolver`, `PhysicalCombatAccumulator`, `PhysicalCombatResolver`), 아이템 효과를 `BattlePropertyResolver`로 분리 완료했다. 다음으로 마법을 `MagicResolver`로 분리한다. 각 resolver는 immutable request를 받고 결과와 domain effect를 반환하며 캠페인 저장 callback을 직접 호출하지 않는다. 현재 `Battle`은 2,621줄로 줄었다.
7. AI는 read-only `BattleAiPlanner`가 이동/행동 결정을 만들고 aggregate가 그 command를 실행하게 한다. AI 점수 계산 중 전투 상태를 미리 변경했다가 복구하는 현재 결합은 transaction 경계가 만들어진 뒤 제거한다.
8. camp 시작/종료, round·weather·status 처리는 `TurnSettlementService`로 옮긴다. `Battle`에는 command 순서와 불변식만 남긴다.

각 추출은 새 클래스도 300줄 미만으로 유지한다. 단, 줄 수를 옮기기만 한 context/gateway 객체는 인정하지 않으며 새 객체가 독립 테스트 가능한 규칙 또는 명확한 상태 소유권을 가져야 한다.

첫 세 단계는 완료했다. `BattleState` typealias와 71개 production/test 참조를 직접 `Battle`로 바꿨고 호환 API는 남기지 않았다. 79줄의 `BattleAttributeCalculator`는 skill 157 지원치와 lift 순서, skill 165 최저 방어, 날씨별 이동력, 원거리 저항을 순수 계산한다. 일반·반격·강제 공격에는 `defenseAgainst`를 사용하되 splash와 magic에는 private-defense skill을 적용하지 않는 기존 규칙 차이도 명시적으로 유지했다. 214줄의 `BattleProbabilityResolver`는 8개 누적 gauge와 일반/flag 난수 채널, 물리·마법 명중, 치명타, 연속 공격, 상태 지속시간을 소유한다. skill 47 후속 공격의 short-circuit와 skill 269의 마법 치명타 gauge 우회를 포함해 난수 소비 순서를 그대로 유지했다. `Battle`은 3,685줄로 줄었고 core JUnit 784개, 전체 headless suite, desktop/android compile과 Python 112개가 통과했다.

도메인 이름 정리도 완료했다. 전투 모델의 구현 역사형 `sourceCharacterId`, `sourceBattleSlot`, `sourceHarm`은 각각 `characterId`, `battleSlot`, `resolvedHarm`으로 바꾸고, 슬롯 배치와 기본 물리 피해 계산도 `BattleSlotLayout`, `basePhysicalDamage`라는 역할 이름을 사용한다. 좌표의 콘텐츠 작성 여부는 `hasAuthoredTileX/Y`, 능력치 변화 command는 `applyAttributeLift`로 명확히 했다. 호환 alias는 만들지 않았으며 외부 비교 trace schema의 `sourceCharacterId` JSON key 한 건만 계약으로 유지한다.

유닛 topology 소유권 분리도 완료했다. 93줄의 `Battlefield`가 active와 퇴각 후 연출 유지 컬렉션, 점유 조회, 숨김·복귀, topology snapshot/restore를 전담한다. `Battle.units`는 구조 변경이 차단되면서 내부 추가를 즉시 반영하는 live read-only view다. 동일 ID가 두 컬렉션에 함께 남는 기존의 특이한 흐름, active 우선 조회, retained가 이기는 runtime memento까지 8개 순수 테스트로 고정했으며 `Battle`의 유닛 map 직접 구조 변경은 0건이다. `Battle`은 3,658줄로 줄었고 core JUnit 792개와 전체 검증이 통과했다.

action transaction 기반의 첫 단계도 완료했다. 99줄의 `BattleUnitMemento`가 26개 mutable 전투 필드와 collection의 깊은 capture, 같은 객체 identity로의 restore, presentation 상태 갱신을 소유한다. `Battle.RuntimeUnitState`와 aggregate 내부의 장황한 필드별 복원 대입은 제거됐고 `Battle`은 3,591줄로 줄었다. 순수 memento 테스트 2개를 추가해 core JUnit 794개와 전체 검증을 통과했다.

action transaction lifecycle도 완료했다. 142줄의 `BattleActionTransaction`이 부분 movement/vitals/status/economy commit, hit side-effect cursor, 최종 snapshot restore와 completion 순서를 소유하고, 13줄의 `BattleActionSnapshot`은 내부 memento 형식을 정의한다. `Battle` 구체 타입 대신 정확히 5개의 좁은 함수 의존만 주입하며 화면·catalog·campaign·RNG·LibGDX를 참조하지 않는다. 이 과정에서 before/after가 같은 객체를 참조해 이동 판정이 항상 false가 될 수 있던 결함을 캡처 좌표 비교로 수정했다. `Battle`은 3,470줄, core JUnit은 797개가 됐으며 전체 검증이 통과했다.

데이터 단계의 선행 작업도 완료했다. `GameDataRepository`가 13개 테이블의 classpath/LibGDX 리소스 접근, 복호화, JSON 파싱과 형식 검증을 맡고, `GameDataCatalog`는 검증된 immutable bundle을 받아 조회한다. 암호화 포맷 구현은 역할에 맞게 `EncryptedGameDataCodec`으로 명명했다.

전투 유닛 단계도 완료했다. `BattleUnit` 파일은 371줄에서 247줄로 줄었고 실제 클래스 선언은 219줄이다. 표시 상태는 `BattleUnitPresentationState`의 명시적 API로만 접근하며 구 API 호환 래퍼는 남기지 않았다. 이 과정의 회귀 테스트가 앞서 추출한 이동 플래너의 실시간 차단 타일 캡처 오류를 발견해 수정했으며, presentation·harm bar·scenario runtime 관련 테스트 158개가 통과했다.

애플리케이션 시작 단계도 1차 완료했다. `JojoGame` 클래스는 약 462줄에서 252줄로 줄었다. 일반 시작 순서와 캠페인 bootstrap은 122줄의 `GameStartupCoordinator`, capture·fixture·sprite 진입은 136줄의 `CaptureFixtureStartupRouter`로 분리했다. reset → capture early return → direct battle 준비 → globals → 최종 진입점이라는 기존 순서는 순수 라우팅 테스트로 고정했다. 다음 verification 단계에서는 capture router 자체를 production source set 밖으로 옮긴다.

캠페인 aggregate 단계도 완료했다. `CampaignState`는 634줄에서 247줄로 줄었고, `CampaignInventory`·`CampaignEquipmentProgression`·`CampaignRoster`가 데이터 소유권과 규칙을 나눠 맡는다. 저장 JSON key와 collection 순서, 장비 instance LIFO, roster fast-path는 유지했다. SOL 검토에서 발견한 mutable collection 노출은 private backing collection과 unmodifiable view, startup/restore/fixture 전용 command로 보완했으며 관련 테스트 191개가 통과했다.

타이틀 화면 단계도 완료했다. `TitleScreen`은 615줄에서 185줄로 줄고 lifecycle·입력·navigation만 남았다. 자원 수명, 실제 draw, 검증 render-event 기록을 각각 분리했으며 renderer/recorder는 mutable UI layer 대신 동일한 immutable `TitleViewState`를 읽는다. 미사용 `ShapeRenderer`도 제거했고 관련 테스트 15개가 통과했다.

전투 준비 화면 단계도 완료했다. `BattlePreparationScreen`은 497줄에서 172줄로 줄었다. roster 선택·필수 출전·인원 제한·터치 우선순위는 LibGDX와 캠페인 저장소를 모르는 순수 Kotlin controller로 옮겼고, 화면은 controller가 반환한 불변 선택 ID를 캠페인 command와 navigation에 연결한다. renderer와 trace recorder는 같은 immutable `BattlePreparationViewState`를 읽으며, texture/font/NinePatch의 수명은 assets 객체 한 곳에서 관리한다. 화면 소유 input processor는 `show`에서 설치하고 `hide`/`dispose`가 현재 processor의 identity를 확인한 뒤 해제하므로 다음 화면의 입력을 지우지 않는다.

검증 모듈 1단계도 완료했다. 독립 `:verification` 모듈이 59개 시나리오, 512개 유닛의 4,488개 avatar 확인, 영천 route, 58개 전투 catalog와 AST gap을 headless로 검증한다. production JAR의 제거 대상 batch 클래스와 production에서 `verification`으로 향하는 Gradle 의존성은 모두 0건이다. 이 단계 이후 core JUnit 772개와 Python 도구 테스트 112개가 통과했고 core/desktop/android compile 및 전체 headless suite가 성공했다.

전투 화면 자원 수명 1단계도 완료했다. `BattleScreen`은 10,245줄에서 9,701줄로 줄고 `BattleDynamicTextureRepository`, `BattleHudAssets`, `BattleOverlayAssets`, `BattleUnitInfoAssets`, `BattleCaptureReferenceAssets`가 생성·필터·fallback·lazy 초기화·폐기를 소유한다. 일반 이동 atlas의 3단계 fallback과 Fight action의 2단계 fallback은 별도 API와 순수 경로 테스트로 고정했다. 동적 저장소는 `BattleUnit`이나 `GameDataCatalog`를 알지 않고 계산된 avatar ID만 받는다. SOL 검토에서 작은 reward 전용 소유자는 과분할로 판단해 overlay 소유자에 합쳤다. 이전 화면에서 빠졌던 UnitInfo lazy texture, terrain mask, lose/reference framebuffer의 폐기도 새 소유권 경계에서 회수된다. core JUnit 774개, headless catalog suite, desktop/android compile과 Python 112개가 모두 통과했다.

Fight 연출 렌더 경계도 완료했다. `BattleScreen`은 9,578줄로 줄고 좌표·scissor·shader·font·draw 순서는 204줄의 `BattleFightRenderer`가 담당한다. mutable `FightPresentationState`는 매 frame 151줄 파일의 깊은 불변 snapshot/view로 투영되며 화면이 이름·얼굴·avatar identity를 해석한 뒤 renderer에 전달한다. renderer에는 `Battle`, `BattleUnit`, `ScenarioRuntime`, `JojoGame`, `GameDataCatalog`, mutable presentation state 의존이 없다. SOL 검토에서 렌더에 쓰지 않는 `enemyIndex`, `dead`와 최종 view의 중복 `mineIndex`를 제거하고 slot 불변식을 강제했다. core JUnit 776개, Fight pairwise trace, 전체 headless suite, desktop/android compile과 Python 112개가 모두 통과했다.

각 단계는 기존 공개 동작을 characterization test로 고정하고, 추출된 객체의 단위 테스트, core 전체 테스트, desktop/android compile 순서로 검증한다.
