# 리팩터링 인수인계

기준일: 2026-09-05
작업 루트: `/Users/ain/workspace/jojo`
참고 구현: `/Users/ain/workspace/jojo_mobile/sgccz-desktop`

이 문서는 다른 AI 에이전트가 현재 작업 트리에서 바로 이어서 작업하기 위한 실행 문서다. 과거 구현은 동작을 확인하는 참고 자료일 뿐이며, 신규 게임의 이름과 구조를 결정하지 않는다.

## 목표와 완료 조건

최종 목표는 다음 세 조건을 모두 만족하는 것이다.

1. production 코드·패키지·설정·리소스에서 이전 이식 프로젝트의 정체성을 제거하고 신규 게임의 역할 이름을 사용한다.
2. 다른 프레임워크의 객체 구조를 그대로 흉내 낸 거대 객체를 Kotlin/LibGDX에 맞는 domain, application, presentation, infrastructure, verification 경계로 재구성한다.
3. 특별한 응집성 사유가 없는 300줄 초과 클래스는 실제 상태 또는 규칙 소유권 단위로 분리한다. 줄 수만 옮기는 wrapper/context/gateway는 완료로 인정하지 않는다.

완료 판정 전에는 production 전체 검색, 클래스 선언 단위 크기 감사, Core 전체 테스트, headless 검증, Desktop/Android 컴파일, Python 도구 테스트가 모두 필요하다.

## 아키텍처 원칙

```text
desktop / android launchers
             |
         application
  startup, navigation, composition
          /          \
      domain       presentation
 campaign/battle   screen/input/render/assets
 scenario/data          |
          ^              |
          +---- infrastructure
              files/preferences/audio

verification ---> public application/domain observation contracts
```

- domain은 LibGDX `Screen`, renderer, asset, verification 타입을 참조하지 않는다.
- 화면은 규칙을 다시 계산하지 않고 domain command를 호출한 뒤 immutable view를 그린다.
- LibGDX 자원은 역할별 `Disposable` 소유자가 생성과 폐기를 함께 책임진다.
- mutable collection은 private backing collection과 의도 기반 command로 보호한다.
- boolean 묶음보다 enum, sealed interface, data class로 유효한 상태를 표현한다.
- `Battle`은 aggregate root로 유지하되 규칙 계산, topology, transaction, AI, 턴 정산을 collaborator에 위임한다.
- 비교 trace나 복구 소스 도구에서 실제 출처를 뜻하는 `source`는 허용하지만, 게임 모델 identity로 쓰이는 `source*` 이름은 금지한다.
- 과거 이름을 위한 typealias, forwarding method, compatibility wrapper는 새로 만들지 않는다.

## 현재 검증 기준선

마지막으로 **전체 검증이 완료된 기준선**은 `PhysicalDamageCalculator`, `PhysicalTargetResolver`, `PhysicalAttackAreaResolver`, `PhysicalCombatAccumulator`, `PhysicalCombatResolver`, `BattlePropertyResolver` 추출 및 순수 단위 테스트, 전체 회귀 완료 직후다.

- Core JUnit: 820개, 실패 0
- `:verification:verifyAllHeadless`: 성공
- 주요 marker: `VERIFY_ALL_SCENARIOS_OK`, `VERIFY_YINGCHUAN_ROUTE_OK`, `VERIFY_ALL_BATTLES_OK`, `AST_API_GAPS: none`
- Desktop/Android Kotlin compile: 성공
- Python 도구 테스트: 112개, 성공

## 완료된 작업

### 프로젝트 identity와 명명

- package: 이전 package namespace -> `com.jojo.game`
- project name: `jojo-game`
- preferences: `jojo-game-campaign`, `jojo-game-settings`
- `OriginalSaveCodec` -> `CampaignSaveCodec`
- `OriginalMagicEffect` -> `MagicEffectDefinition`
- `BattleState = Battle` typealias와 71개 호출자 제거
- 전투 모델 명명:
  - `sourceCharacterId` -> `characterId`
  - `sourceBattleSlot` -> `battleSlot`
  - `SourceBattleSlots` -> `BattleSlotLayout`
  - `sourceTileX/YAuthored` -> `hasAuthoredTileX/Y`
  - `applySourceAttributeLift` -> `applyAttributeLift`
  - `sourceHarm` -> `resolvedHarm`
- 외부 비교 trace schema의 JSON key `sourceCharacterId` 한 건은 의도적으로 유지한다.

현재 targeted identity 검색은 0건이어야 한다.

```bash
legacy_identity='p''ort'
rg -n "\\b${legacy_identity}(ing)?\\b|com\\.jojo\\.${legacy_identity}" \
  settings.gradle.kts core/src desktop/src android/src verification/src tools *.md
```

### 이미 분리된 주요 경계

| 기존 책임 | 현재 경계 | 상태 |
|---|---|---|
| 게임 데이터 I/O·복호화 | `GameDataRepository`, `EncryptedGameDataCodec` | 완료 |
| 캠페인 inventory/equipment/roster | `CampaignInventory`, `CampaignEquipmentProgression`, `CampaignRoster` | 완료 |
| 시작 라우팅 | `GameStartupCoordinator`, `CaptureFixtureStartupRouter` | 1차 완료 |
| 타이틀 화면 | controller/view/renderer/assets/recorder | 완료 |
| 전투 준비 화면 | controller/view/renderer/assets/recorder | 완료 |
| 전투 유닛 표시 상태 | `BattleUnitPresentationState` | 완료 |
| 이동 규칙 | `BattleMovementPlanner` (213줄) | 완료 |
| 능력치 규칙 | `BattleAttributeCalculator` (79줄) | 완료 |
| 확률·난수 판정 | `BattleProbabilityResolver` (214줄), `BattleRateGauge` | 완료 |
| active/퇴각 연출 topology | `Battlefield` (201줄) | 완료 |
| 유닛 깊은 snapshot | `BattleUnitMemento` (127줄) | 완료 |
| 계산/애니메이션 commit | `BattleActionSnapshot`, `BattleActionTransaction` (210줄) | 완료 |
| 물리 피해·수치 계산 규칙 | `PhysicalDamageCalculator` (234줄), `PhysicalDamageCalculatorTest` (278줄) | 완료 |
| 물리 단일 대상 효과 해결 | `PhysicalTargetResolver` (284줄), `PhysicalTargetResolverTest` (215줄) | 완료 |
| 물리 범위·스플래시·피해전이 | `PhysicalAttackAreaResolver` (107줄), `PhysicalAttackAreaResolverTest` (106줄) | 완료 |
| 물리 전투 다단계 정산 누적 | `PhysicalCombatAccumulator` (120줄) | 완료 |
| 물리 전투 패스 오케스트레이션 | `PhysicalCombatResolver` (334줄, object 선언 298줄) | 완료 |
| 전투 소모품·속성 아이템 효과 | `BattlePropertyResolver` (73줄), `BattlePropertyResolverTest` (115줄) | 완료 |
| 전투 화면 자원 수명 | 역할별 `Battle*Assets`, `BattleDynamicTextureRepository` | 1차 완료 |
| Fight 렌더링 | `FightPresentationView`, `BattleFightRenderer` | 완료 |
| batch 검증 화면 | production에서 삭제, `:verification` headless app으로 이동 | 1차 완료 |

`BattleActionTransaction` 추출 중 shared `BattleUnit` 객체의 현재 좌표를 before/after에서 비교해 이동 여부가 항상 false가 될 수 있던 오류를 발견했다. 현재 코드는 memento에 캡처된 좌표를 비교하며 `commitAll()` 단독 경로 테스트가 이를 고정한다.

## 현재 파일 상태

- `Battle.kt`: 787줄 (기존 3,468줄에서 2,681줄 감축, 20개 순수 Kotlin SRP 협력 객체 분리 완료)
- `ScenarioInterpreter.kt`: 277줄 (기존 1,844줄에서 1,567줄 감축, 19개 순수 Kotlin SRP 협력 객체 분리 완료, 300줄 이하 엄수)
- `ScenarioRuntime.kt` (`ScenarioStage`): 468줄 (기존 1,082줄에서 614줄 감축, 5개 협력 객체 분리 완료)
- 전체 회귀: Core JUnit 841개 올그린, headless 전체 통과, Python 112개 통과, Desktop/Android 컴파일 성공

## 다음 리팩터링 순서

### 1단계: ScenarioStage 최종 300줄 이하 진입 (현재 468줄)
1. `ScenarioStageMapObjectsManager`: `mapObjects`, `mapObjectsCallJournal`, `setMapObjects`, `fires` 관리 위임 (~80줄)
2. `ScenarioStageWeatherEnvironment`: `battleWeatherSchedule`, `initialBattleWeather`, `setBattleGlobalData` 관리 위임 (~50줄)
3. 완료 시 `ScenarioStage` 250줄 이하로 진입하여 목표 완수.

### 2단계: Battle 최종 300줄 이하 진입 (현재 787줄)
1. `BattleConfiguration`: 전투 시나리오 메타데이터, 보상/승리 조건, 글로벌 플래그 설정 캡슐화 (~150줄)
2. `BattleStateJournal`: 전투 턴/페이즈/진행 로그 및 히스토리 관리 위임 (~150줄)
3. 완료 시 `Battle` 오케스트레이터 300줄 이하로 진입하여 목표 완수.

### 3단계: 대형 presentation 화면 분리
1. `BattleScreen` (9,578줄): `BattleInputController`, `BattlePresentationCoordinator`, `BattleSceneRenderer`, verification observer
2. `ScenarioScreen` (4,534줄): `ScenarioPlaybackController`, `HallController`, `ScenarioRenderer`, `HallRenderer`, 화면별 input handler

## 현재 300줄 초과 production 클래스 인벤토리

| 클래스 | 현재 크기 | 판단 |
|---|---:|---|
| `BattleScreen` | 9,578 | 분리 계속 필요 |
| `ScenarioScreen` | 4,534 | 분리 필요 |
| `GameDataCatalog` | 823 | catalog별 분리 필요 |
| `Battle` | 787 | 300줄 이하 최종 조율 (기존 3,468에서 대폭 감축) |
| `BattleCampaignE2eAdapter` | 583 | verification 이동 필요 |
| `ScenarioStage` (`ScenarioRuntime.kt`) | 468 | 300줄 이하 최종 조율 (기존 1,082에서 대폭 감축) |
| `FightPresentationState` | 약 431 | 응집된 상태 머신 예외 |
| `CampaignE2eDriver` | 약 320 | verification 내부 분리 필요 |
| `BattleScenarioFactory` | 약 302 | 선언적 데이터 예외 |

파일이 300줄을 넘어도 개별 클래스가 작은 경우는 클래스 감사 대상에서 제외한다. 자세한 기존 분석은 [`LARGE_CLASS_REFACTORING.md`](LARGE_CLASS_REFACTORING.md), 의존 방향은 [`GAME_ARCHITECTURE.md`](GAME_ARCHITECTURE.md)를 함께 읽는다.

## 전투 불변식

- `Battlefield`만 active/retained backing `LinkedHashMap`을 소유한다.
- `Battle.units`는 구조 변경 불가능한 live view다.
- active lookup이 retained보다 우선한다.
- 퇴각은 active에서 제거하고 retained 끝에 추가한다. 복귀는 retained에서 제거하고 active 끝에 추가한다.
- 기존에는 같은 ID가 active와 retained 양쪽에 존재할 수 있다. 이 특이 동작은 테스트로 고정돼 있으므로 별도 정책 변경 없이 정상화하지 않는다.
- runtime snapshot은 active를 먼저, retained를 나중에 합쳐 duplicate ID에서 retained memento가 이긴다.
- `BattleActionTransaction.commitAll()`은 final snapshot restore, 남은 hit effect, completion effect 순서를 한 번만 실행한다.
- 계산 중 외부 campaign callback은 staging하고 animation lifecycle에 맞춰 commit한다.
- `BattleProbabilityResolver`의 일반 RNG와 flag RNG 채널 및 gauge 소비 순서를 바꾸지 않는다.
- skill 47 follow-up short-circuit, skill 269 magic critical gauge 우회를 유지한다.
- 일반·반격·강제 공격의 private-defense 적용과 splash/magic의 비적용 차이를 유지한다.

## 작업 방식

사용자가 지정한 역할 분담을 유지한다.

- SOL 메인 에이전트: 현재 상태 분석, 아키텍처와 tranche 범위 설계, TERRA diff 리뷰, 독립 검증, 전체 순서 조율
- TERRA 에이전트: production/test 코드 수정과 해당 tranche 검증

한 tranche는 한 책임만 옮기고 다음 순서로 승인한다.

1. SOL이 소유할 상태/규칙, 허용 의존성, 금지 의존성, 불변식을 명시한다.
2. TERRA가 구현하고 순수 단위 테스트를 추가한다.
3. SOL이 wrapper/alias, mutable collection 노출, 호출 순서, 클래스 크기, 금지 의존성을 검색한다.
4. targeted 테스트를 통과한다.
5. 전체 회귀를 통과한 뒤 문서의 완료 상태와 줄 수를 갱신한다.

공유 작업 트리이므로 두 에이전트가 같은 파일을 동시에 수정하지 않는다. Gradle도 동시에 두 번 실행하지 않는다.

## 검증 명령

```bash
./gradlew --no-daemon -Pkotlin.incremental=false \
  :core:test \
  :verification:verifyAllHeadless \
  :desktop:compileKotlin \
  :android:compileDebugKotlin

python3 -m unittest discover -s tools -p 'test_*.py'
```

추가 감사:

```bash
# identity
legacy_identity='p''ort'
rg -n "\\b${legacy_identity}(ing)?\\b|com\\.jojo\\.${legacy_identity}" \
  settings.gradle.kts core/src desktop/src android/src verification/src tools *.md

# 현재 물리 계산기 금지 의존
rg -n 'Battle\b|Battlefield|Screen|Gdx|GameDataCatalog|Campaign|Random|SourceRandomStreams' \
  core/src/main/kotlin/com/jojo/game/PhysicalDamageCalculator.kt

# 기존 전투 모델 이름 잔존. sourceCharacterId JSON schema 한 건만 허용
rg -n 'sourceCharacterId|sourceBattleSlot|sourceTileXAuthored|sourceTileYAuthored|sourceHarm|SourceBattleSlots' \
  core/src/main/kotlin core/src/test/kotlin verification/src/main/kotlin
```

## 주의 사항

- 저장 namespace fallback을 추가하지 않는다. 이 프로젝트는 신규 게임이므로 이전 `jojo-original-*` 저장소 migration은 요구사항이 아니다.
- 오래 실행 중인 사용자 `:desktop:run` 프로세스가 있을 수 있다. 리팩터링 검증을 위해 임의 종료하지 않는다.
- build 산출물에는 이전 package 문자열이 남을 수 있다. identity 완료 판정은 먼저 source/config/resource를 대상으로 하고, 마지막 단계에서 안전하게 clean build를 수행한다.
- 현재 저장소에는 Git metadata가 없을 수 있다. diff에 의존하지 말고 검색, 파일 내용, 테스트 결과를 근거로 상태를 판단한다.
- 과거 구현의 이름이나 coroutine callback 순서를 production API 이름으로 복제하지 않는다. 동작 순서는 characterization test로만 보존한다.
