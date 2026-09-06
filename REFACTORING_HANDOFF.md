# 리팩터링 인수인계

기준일: 2026-09-07
작업 루트: `/Users/ain/workspace/jojo`
참고 구현: `/Users/ain/workspace/jojo_mobile/sgccz-desktop`

이 문서는 다른 AI 에이전트가 현재 작업 트리에서 바로 이어서 작업하기 위한 실행 문서다. 과거 구현은 동작을 확인하는 참고 자료일 뿐이며, 신규 게임의 이름과 구조를 결정하지 않는다.

모든 수치는 2026-09-07 HEAD(`5a03d03`) 실측이다. 추정값이나 과거 스냅샷을 옮겨 적지 않는다.

## 목표와 완료 조건

최종 목표는 다음 세 조건을 모두 만족하는 것이다.

1. production 코드·패키지·설정·리소스에서 이전 이식 프로젝트의 정체성을 제거하고 신규 게임의 역할 이름을 사용한다. **달성**
2. 다른 프레임워크의 객체 구조를 그대로 흉내 낸 거대 객체를 Kotlin/LibGDX에 맞는 domain, application, presentation, infrastructure, verification 경계로 재구성한다. **패키지 분할은 달성, 의존 방향은 미달성** (아래 "패키지 경계 위반" 참고)
3. 특별한 응집성 사유가 없는 300줄 초과 클래스는 실제 상태 또는 규칙 소유권 단위로 분리한다. 줄 수만 옮기는 wrapper/context/gateway는 완료로 인정하지 않는다. **6개 파일 남음**

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

2026-09-07 실측.

| 항목 | 결과 |
|---|---|
| `:core:compileKotlin` | 성공 |
| Core JUnit | 989개, 실패 0 |
| Python 도구 테스트 | 121개 중 **3개 실패** (`test_package_boundaries`) |
| `:verification:verifyAllHeadless` | 미실행 (다음 작업자가 확인) |
| Desktop/Android compile | 미실행 (다음 작업자가 확인) |

Python 3건 실패는 도구 결함이 아니라 **실제 아키텍처 위반**이다. 아래 "패키지 경계 위반"이 최우선 과제다.

## 미커밋 주석 diff (선행 확인 필요)

작업 트리에 516파일 / +40,762줄의 미커밋 변경이 있다. 전량 자동 생성된 한글 주석이며 다음 문제를 안고 있다.

- 새로 추가된 블록 주석 8,036개 중 **8,035개가 주석과 선언 사이에 빈 줄**이 있어 KDoc으로 연결되지 않는다 (508개 파일).
- 내용이 정형문 반복이다. `보관한다` 4,264회, `반영된다` 3,711회, `수행한다` 1,573회.
- 이 주석 때문에 300줄 초과 production 파일이 **6개 → 78개**로 늘고 `BattleScreen`은 7,094 → 11,471줄이 된다. 아래 감사표를 작업 트리에서 재계측하면 값이 맞지 않는다.
- 순수 주석 diff인데 179줄의 코드 재포맷이 섞여 있다.

컴파일은 통과한다. **이 문서의 모든 수치는 HEAD 기준이므로, 크기를 재계측할 때는 `git show HEAD:<path>`를 사용한다.**

## 완료된 작업

### 프로젝트 identity와 명명

- package: 이전 package namespace -> `com.jojo.game`
- project name: `jojo-game`
- preferences: `jojo-game-campaign`, `jojo-game-settings`
- `OriginalSaveCodec` -> `CampaignSaveCodec`
- `OriginalMagicEffect` -> `MagicEffectDefinition`
- `BattleState = Battle` typealias와 71개 호출자 제거 (현재 잔존 0건)
- 전투 모델 명명: `sourceCharacterId` -> `characterId`, `sourceBattleSlot` -> `battleSlot`, `SourceBattleSlots` -> `BattleSlotLayout`, `sourceTileX/YAuthored` -> `hasAuthoredTileX/Y`, `applySourceAttributeLift` -> `applyAttributeLift`, old harm-source parameter -> `resolvedHarm`
- 외부 비교 trace schema의 JSON key `sourceCharacterId` 한 건은 의도적으로 유지한다. 현재 잔존은 정확히 이 1건뿐이다.

legacy identity 잔존은 **0건**이다.

### 도메인·애플리케이션 분해 (완료)

문서 2026-09-05판이 "다음 순서"로 남겨 둔 1·2단계는 모두 완료됐다.

| 클래스 | 09-05 문서 | 현재 HEAD | 상태 |
|---|---:|---:|---|
| `Battle` | 787 | **288** | 완료 |
| `ScenarioStage` (`ScenarioRuntime.kt`) | 468 | **293** | 완료 |
| `ScenarioInterpreter` | 277 | **268** | 완료 |
| `GameDataCatalog` | 823 | **246** | 완료 (catalog 15파일로 분할) |
| `JojoGame` | 252 | **219** | 완료 |
| `CampaignState` | 247 | **225** | 완료 |
| `BattleCampaignE2eAdapter` | 583 | 삭제 | verification 이전 완료 |
| `CampaignE2eDriver` | 320 | 삭제 | verification 이전 완료 |
| `BattleScenarioFactory` | 302 | **47** | 완료 |

`Battle`은 `application/battle` 아래 18개 파일과 `ai`/`bootstrap`/`combat`/`experience`/`movement`/`presentation`/`round` 하위 패키지로, 전투 규칙은 `domain/battle`의 23파일 + `combat`/`command`/`magic`/`settlement`/`turn` 하위 패키지로 분리됐다.

### 모듈 경계

`verification`은 168개 production 파일을 가진 독립 모듈이다. core 474 / desktop 2 / android 1 / verification 168. production에서 `verification`으로 향하는 Gradle 의존성은 0건이다.

## 남은 과제

### P0 — 패키지 경계 위반 (`tools/test_package_boundaries.py` 3건 실패)

의존 방향이 세 곳에서 역류하고 있으며, 그중 하나는 **순환**이다.

**(1) domain -> infrastructure, 6건**

```
domain/campaign/CampaignState.kt:4                     import ...infrastructure.data.GameDataCatalog
domain/campaign/CampaignEquipmentProgression.kt:4      import ...infrastructure.data.GameDataCatalog
domain/campaign/CampaignInventory.kt:4                 import ...infrastructure.data.GameDataCatalog
domain/campaign/CampaignInventoryEquipmentManager.kt:4 import ...infrastructure.data.GameDataCatalog
domain/battle/BattleUnit.kt:3                          import ...infrastructure.data.GameDataCatalog
domain/battle/BattleAvatarResolver.kt:4                import ...infrastructure.data.GameDataCatalog
```

**(2) infrastructure -> presentation/application, 3건**

```
infrastructure/data/GameDataCatalog.kt:4           import ...presentation.scenario.overlay.*
infrastructure/data/GameDataCatalogUnitDomain.kt:4 import ...presentation.shared.overlay.TerrainLayer
infrastructure/audio/GameAudioPlayer.kt:7          import ...application.scenario.ScenarioStage
```

(1)+(2)가 `domain -> infrastructure -> presentation` 순환을 만든다. `GameDataCatalog`가 매듭이다.

**(3) presentation -> infrastructure, 22건 / 18파일**

`ScenarioScreen`(3), `BattleScreen`(3), hall 계열 6파일, battle combat/timeline/preparation/assets 계열이 `infrastructure.data`를 직접 import한다.

권장 순서:
1. domain이 필요로 하는 조회만 담은 read-only 조회 interface를 `domain`에 정의하고 `GameDataCatalog`가 이를 구현하게 해 (1)을 끊는다.
2. `GameDataCatalog`가 참조하는 `presentation.*.overlay` 타입(`TerrainLayer` 등)을 domain 또는 중립 데이터 타입으로 옮겨 (2)를 끊는다. `GameAudioPlayer`의 `ScenarioStage` 의존은 좁은 콜백/interface로 역전한다.
3. presentation은 application이 조립해 넘긴 조회 interface만 받게 해 (3)을 줄인다.

각 단계 후 `python3 -m unittest tools.test_package_boundaries`가 통과해야 한다.

### P0 — `BattleScreen` 분리 (7,094줄, 단일 클래스)

파일 전체가 하나의 `class BattleScreen` 선언이다. 멤버 함수 209개, 프로퍼티 245개, 생성자 파라미터 6개.

이미 자원 수명(`assets/` 7파일), Fight 렌더(`fight/` 10파일), overlay(`overlay/` 29파일), render(`render/` 11파일), timeline(`timeline/` 9파일), input(`input/` 3파일)이 분리돼 있다. 즉 협력 객체는 충분히 만들어졌고, **화면이 여전히 그 조립과 진행 제어를 전부 쥐고 있는 것**이 남은 문제다.

다음 tranche 후보:
1. `BattleInputController` — 터치/키 입력 해석과 커서·선택 상태 소유
2. `BattlePresentationCoordinator` — 프레임별 연출 큐 진행과 timeline 전환 소유
3. `BattleSceneRenderer` — 남은 draw 순서 소유
4. verification observer 잔여분을 `:verification`으로 이전

### P1 — `ScenarioScreen` 분리 (987줄, 단일 클래스)

멤버 함수 66개, 프로퍼티 82개, **생성자 파라미터 19개**. hall 기능은 `presentation/scenario/hall`의 51+12파일로 이미 분리됐다. 남은 문제는 생성자 파라미터 19개가 드러내는 조립 책임 과다다. `ScenarioScreenDependencies` 같은 묶음 타입이 아니라, 재생 제어와 회관 진입 라우팅을 실제 소유자에게 넘겨 파라미터 자체를 줄인다.

### P2 — 300줄 경계 조율

HEAD 기준 300줄 초과 production 파일은 6개뿐이며, 그중 4개는 경계선이다.

| 파일 | HEAD | 판단 |
|---|---:|---|
| `presentation/battle/BattleScreen.kt` | 7,094 | P0 |
| `presentation/scenario/ScenarioScreen.kt` | 987 | P1 |
| `presentation/battle/fight/FightPresentationState.kt` | 343 | 응집된 상태 머신, 예외 유지 |
| `application/scenario/ScenarioTacticalActionDispatcher.kt` | 314 | dispatch 분기, 콘텐츠 증가 시 분리 |
| `domain/battle/combat/PhysicalCombatResolver.kt` | 313 | 패스 오케스트레이션, 예외 인정 |
| `domain/battle/magic/MagicTargetResolver.kt` | 302 | 경계선, 유지 |

### P3 — 미커밋 주석 diff 정리

위 "미커밋 주석 diff" 참고. 커밋 전 최소한 주석-선언 사이 빈 줄 8,035곳을 제거해 KDoc이 실제로 연결되게 하고, 정형문 주석은 정보가 있는 곳만 남긴다.

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

`BattleActionTransaction` 추출 중 shared `BattleUnit` 객체의 현재 좌표를 before/after에서 비교해 이동 여부가 항상 false가 될 수 있던 오류를 발견했다. 현재 코드는 memento에 캡처된 좌표를 비교하며 `commitAll()` 단독 경로 테스트가 이를 고정한다.

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

패키지 경계만 빠르게 확인:

```bash
python3 -m unittest tools.test_package_boundaries
```

크기 감사는 미커밋 주석 diff의 영향을 피하기 위해 HEAD를 대상으로 한다.

```bash
git ls-tree -r HEAD --name-only \
  | grep -E '^(core|desktop|android)/src/main/.*\.kt$' \
  | while read f; do echo "$(git show HEAD:$f | wc -l) $f"; done \
  | sort -rn | awk '$1>300'
```

identity 감사. 이전 명령은 hexagonal `Port` 역할 이름 387건을 오탐하므로 대소문자와 package 형태로 좁힌다.

```bash
legacy_identity='p''ort'
rg -n "com\\.jojo\\.${legacy_identity}\\b|\\b${legacy_identity}ing\\b" \
  settings.gradle.kts core/src desktop/src android/src verification/src tools *.md
```

기존 전투 모델 이름 잔존. `sourceCharacterId` JSON schema 한 건만 허용한다.

```bash
rg -n 'sourceCharacterId|sourceBattleSlot|sourceTileXAuthored|sourceTileYAuthored|SourceBattleSlots' \
  core/src/main/kotlin core/src/test/kotlin verification/src/main/kotlin
```

## 주의 사항

- 저장 namespace fallback을 추가하지 않는다. 이 프로젝트는 신규 게임이므로 이전 `jojo-original-*` 저장소 migration은 요구사항이 아니다.
- 오래 실행 중인 사용자 `:desktop:run` 프로세스가 있을 수 있다. 리팩터링 검증을 위해 임의 종료하지 않는다.
- build 산출물에는 이전 package 문자열이 남을 수 있다. identity 완료 판정은 먼저 source/config/resource를 대상으로 하고, 마지막 단계에서 안전하게 clean build를 수행한다.
- 과거 구현의 이름이나 coroutine callback 순서를 production API 이름으로 복제하지 않는다. 동작 순서는 characterization test로만 보존한다.
- 크기·경계 수치를 문서에 적을 때는 반드시 재계측한다. 2026-09-05판 문서는 실제보다 최대 3배 큰 값을 담고 있었다.

## 관련 문서

- [`LARGE_CLASS_REFACTORING.md`](LARGE_CLASS_REFACTORING.md) — 300줄 초과 클래스 전수 감사와 예외 판단
- [`GAME_ARCHITECTURE.md`](GAME_ARCHITECTURE.md) — 목표 의존 방향과 현재 위반 목록
