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

마지막으로 **전체 검증이 완료된 기준선**은 `BattleActionTransaction` 추출 직후다.

- Core JUnit: 797개, 실패 0
- `:verification:verifyAllHeadless`: 성공
- 주요 marker: `VERIFY_ALL_SCENARIOS_OK`, `VERIFY_YINGCHUAN_ROUTE_OK`, `VERIFY_ALL_BATTLES_OK`, `AST_API_GAPS: none`
- Desktop/Android Kotlin compile: 성공
- Python 도구 테스트: 112개, 성공

현재 작업 트리에는 그 이후의 `PhysicalDamageCalculator` 연결 작업이 포함돼 있다. 이 상태에서 다음 명령은 성공했다.

```bash
./gradlew --no-daemon -Pkotlin.incremental=false :core:compileKotlin :core:compileTestKotlin
```

그러나 현재 물리 피해 tranche는 순수 단위 테스트와 전체 회귀를 아직 완료하지 않았다. 따라서 797개 기준선을 현재 코드 전체의 최종 승인으로 간주하면 안 된다.

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
| 이동 규칙 | `BattleMovementPlanner` | 완료 |
| 능력치 규칙 | `BattleAttributeCalculator` | 완료 |
| 확률·난수 판정 | `BattleProbabilityResolver`, `BattleRateGauge` | 완료 |
| active/퇴각 연출 topology | `Battlefield` | 완료 |
| 유닛 깊은 snapshot | `BattleUnitMemento` | 완료 |
| 계산/애니메이션 commit | `BattleActionSnapshot`, `BattleActionTransaction` | 완료 |
| 전투 화면 자원 수명 | 역할별 `Battle*Assets`, `BattleDynamicTextureRepository` | 1차 완료 |
| Fight 렌더링 | `FightPresentationView`, `BattleFightRenderer` | 완료 |
| batch 검증 화면 | production에서 삭제, `:verification` headless app으로 이동 | 1차 완료 |

`BattleActionTransaction` 추출 중 shared `BattleUnit` 객체의 현재 좌표를 before/after에서 비교해 이동 여부가 항상 false가 될 수 있던 오류를 발견했다. 현재 코드는 memento에 캡처된 좌표를 비교하며 `commitAll()` 단독 경로 테스트가 이를 고정한다.

## 현재 진행 중인 작업: 물리 피해 계산

### 현재 파일 상태

- `Battle.kt`: 3,467줄
- `PhysicalDamageCalculator.kt`: 208줄
- `PhysicalDamageCalculatorTest.kt`: 아직 없음
- Core main/test compile: 성공
- 전체 회귀: 아직 미실행

`PhysicalDamageCalculator`는 callback 없이 순수 Kotlin 규칙을 소유한다. 현재 DTO는 다음과 같다.

- `BasePhysicalDamageContext`
- `FlatPhysicalDamageContext`
- `PhysicalDamageRateContext`
- `PhysicalCriticalRateContext`
- `PhysicalDefenseRule.ATTACKER_AWARE / INTRINSIC`

일반·반격·강제 공격은 attacker-aware defense를 사용하고, splash는 intrinsic defense를 사용하는 기존 차이를 보존해야 한다. topology, RNG, skill temp 소비는 `Battle`의 context builder가 기존 호출 시점에 계산하고 calculator는 전달받은 값만 읽는다.

### 바로 이어서 할 일

1. `Battle.kt`의 모든 물리 피해 경로를 감사한다.
   - primary, follow-up, counter, counter follow-up, forced attack, splash, AI preview
   - `PhysicalDamageCalculator`를 거치지 않는 중복 수식이 없어야 한다.
2. 아래 옛 helper 선언이 `Battle`에 남지 않았는지 확인한다.

```bash
rg -n 'private fun (armorPiercingMinimumDamage|cappedPhysicalDamage|physicalMinimumDamage|physicalFlatSkillDamage|physicalDamageRate|physicalCriticalRate|physicalArmRestraint|basePhysicalDamage)' \
  core/src/main/kotlin/com/jojo/game/Battle.kt
```

3. 단순 위임용 `Battle.basePhysicalDamage` wrapper를 남기지 않는다.
4. RNG 순서를 확인한다.
   - skill 292의 `flagRandom(0, 5)`는 기존 `physicalDamageRate` 호출 위치에서 정확히 한 번만 소비한다.
   - counter skill 46 temp는 기존 critical-rate 계산 시점에 한 번만 소비한다.
   - MRSP, hit, critical, continuous attack 순서는 변경하지 않는다.
5. `PhysicalDamageCalculatorTest`를 추가한다.
   - terrain/splash/base floor
   - `PhysicalDefenseRule` 두 경로
   - skills 316/133 arm restraint
   - skill 174 armor-piercing minimum, skill 242 cap, enemy famous-unit floor
   - flat additions와 context 값
   - damage-rate의 상태·방향·근접·back-position 분기
   - critical/counter/continuous/splash와 skill 217 방향 분기
6. targeted 테스트 후 전체 검증 명령을 실행한다.

중단 시점에 primary inline base damage와 AI preview wrapper를 calculator로 직접 연결하라는 SOL 리뷰가 반영됐다. 현재 검색상 calculator 호출은 존재하지만, 테스트 전이므로 의미 보존을 다시 대조해야 한다.

## 다음 리팩터링 순서

### P0: `Battle` 완성

1. 현재 `PhysicalDamageCalculator` tranche를 검증 완료한다.
2. `PhysicalCombatResolver`
   - immutable request를 받고 attack pass/result/domain effect를 반환한다.
   - 캠페인 경험치·장비 경험치 callback을 직접 호출하지 않는다.
   - HP/MP/status 적용은 transaction/effect 경계를 통해 수행한다.
3. `BattlePropertyResolver`
   - consumable 선택/효과/자동 사용과 inventory callback을 분리한다.
4. `MagicResolver`
   - 마법 조건, 범위, 피해/회복, 상태, terrain/weather modifier를 분리한다.
5. `BattleAiPlanner`
   - read-only battlefield view를 받고 command를 반환한다.
   - 점수 계산을 위해 live state를 변경했다가 rollback하는 흐름을 제거한다.
6. `TurnSettlementService`
   - camp start/end, round, weather, poison/status/lift 만료를 소유한다.

### P0: 대형 presentation과 scenario

1. `BattleScreen` 9,578줄
   - `BattleInputController`
   - `BattlePresentationCoordinator` 또는 `BattleSequencePlayer`
   - `BattleSceneRenderer`와 overlay별 renderer
   - trace/capture observer를 `:verification`으로 이동
2. `ScenarioScreen` 4,534줄
   - `ScenarioPlaybackController`, `ScenarioRenderer`
   - `HallController`, `HallRenderer`
   - 화면별 input handler
   - 약 23개 verification/fixture 생성자 옵션을 `ScenarioLaunchConfiguration`으로 통합하고 production 밖으로 이동
3. `ScenarioInterpreter` 1,843줄
   - `ScenarioStatementExecutor`
   - `ScenarioExpressionEvaluator`
   - `ScenarioFunctionRegistry`
   - 좁은 `ScenarioCommandSink`
4. `ScenarioStage` 1,082줄
   - `ScenarioVariables`
   - `ScenarioCommandBuffer`
   - `ScenarioBattleContext`
   - 역할별 command interface

### P1: 데이터와 verification 경계

- `GameDataCatalog` 823줄을 `UnitCatalog`, `EquipmentCatalog`, `MagicCatalog`, `TerrainCatalog`로 나눈다.
- `BattleCampaignE2eAdapter` 현재 583줄을 production에서 `:verification`으로 이동하고 projection mapper를 분리한다.
- `CampaignE2eDriver` 약 320줄을 `CampaignAutoPlayer`와 `CampaignTraceRecorder`로 분리한다.
- `CaptureFixtureStartupRouter`와 남은 trace harness를 production source set 밖으로 옮긴다.
- `JojoGame`은 lifecycle/composition/navigation만 남기고 `ScreenNavigator` 경계를 완성한다.

### 유지가 허용된 300줄 초과 예외

- `FightPresentationState` 약 431줄: 하나의 deterministic 연출 상태 머신으로 응집돼 있어 현재는 유지한다. 모델 타입 또는 timeline builder가 독립 책임으로 성장할 때만 분리한다.
- `BattleScenarioFactory` 약 302줄: 작은 factory와 선언적 기본 데이터가 대부분이다. 콘텐츠가 늘 때 시나리오별 factory로 나눈다.

## 현재 300줄 초과 production 클래스 인벤토리

| 클래스 | 현재 크기 | 판단 |
|---|---:|---|
| `BattleScreen` | 9,578 | 분리 계속 필요 |
| `ScenarioScreen` | 4,534 | 분리 필요 |
| `Battle` | 3,467 | 분리 진행 중 |
| `ScenarioInterpreter` | 1,843 | 분리 필요 |
| `ScenarioStage` | 약 1,012, 파일 1,082 | 분리 필요 |
| `GameDataCatalog` | 823 | catalog별 분리 필요 |
| `BattleCampaignE2eAdapter` | 583 | verification 이동 필요 |
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
