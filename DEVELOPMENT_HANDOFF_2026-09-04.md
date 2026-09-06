# 조조전 원본 충실 게임 개발 작업 인수인계 (2026-09-04)

> **2026-09-07 갱신.** 리팩터링·아키텍처 상태 판단은 [`REFACTORING_HANDOFF.md`](REFACTORING_HANDOFF.md)를 먼저 읽는다.
> 이 문서에 적힌 클래스 크기·분해 진행도는 2026-09-05 이전 값이며 실제보다 최대 3배 크다
> (`Battle` 787→288, `ScenarioScreen` 4,534→987, `BattleScreen` 9,578→7,094).
> 아래 내용 중 **원본 시각 재현·검증 게이트 기록**은 여전히 유효한 참고 자료다.

> **주의: 최신 상태와 재개 계획은 `DEVELOPMENT_STATUS_2026-09-04.md`를 먼저 읽는다.**
> 이 문서 상단의 과거 `desktop:check` 성공은 fresh 렌더 게이트 추가 전 결과이며,
> 현재 전투 SayLayer 위치 비교는 red다.

> **최신 인계 갱신(2026-09-04 오후): 이 단락이 아래의 과거 상태 설명보다 우선한다.**
>
> 아래 section 1/6.0에 적힌 standalone full-battle timeout blocker는 해결됐다.
> `ProductionBattleInputDriver`를 공용화해 campaign E2E와 standalone full-battle 모두
> 설치된 production `InputProcessor`로 대화, 메뉴, 턴 종료, 위임 toggle/confirm을
> 조작한다. private continuation, `nextTurn()` 강제 호출, 강제 승리는 사용하지 않는다.
> focused 영천전투 회귀와 campaign 화면 E2E는 최신 코드로 통과했다. 이어서 실행한 단독
> `:desktop:check`도 `BUILD SUCCESSFUL in 10m 24s`로 끝났다(314 actionable tasks:
> 299 executed, 15 up-to-date). 다만 이는 Title→R00→S00→R01 및 영천전투와 정적/fixture
> 범위의 강한 증거이지 119개 시나리오의 production 완주 증거는 아니므로, 전체 게임
> 100% 완료로 선언하면 안 된다.

## 0. 최신 작업 상태와 즉시 재개 지점

### 해결된 이전 blocker

- `core/src/main/kotlin/com/jojo/game/CampaignE2eTrace.kt`
  - 실제 UI 입력을 담당하는 `ProductionBattleInputDriver`를 추출했다.
  - `CampaignE2eBattleState`만 관찰하며 설치된 `Gdx.input.inputProcessor`에 key/touch/drag를
    전달한다.
  - 대화/선택지/모달/승리 조건, 전투 메뉴 열기, `END_ROUND`, 위임 toggle/confirm,
    보상/저장 질문을 production input으로 처리한다.
- `core/src/main/kotlin/com/jojo/game/JojoGame.kt`
  - campaign E2E가 없는 standalone full trace에도 같은 driver를 설치했다.
  - `super.render()` 뒤 실제 `BattleScreen.campaignE2eState()`를 관찰해 다음 입력을 보낸다.
  - 입력이 설치된 input processor에서 수락된 뒤에만 provenance를 trace에 기록한다.
- `core/src/main/kotlin/com/jojo/game/BattleScreen.kt`
  - `driveFullBattleTrace()`에서 private 대화/선택/모달/승리 continuation 호출을 제거했다.
  - 이 함수는 timeout/readiness/terminal flush만 담당한다.
  - standalone은 자연스러운 scenario 종료 뒤 안정 프레임 3개를 기다리고
    `finishFullBattleTrace("battle-end")`로 종료한다.
  - full trace가 개발자의 `jojo-auto-battle/TUOGUAN` preference를 읽거나 덮어쓰지 않게
    했다.
- `core/src/main/kotlin/com/jojo/game/FullBattleTrace.kt`
  - 폐기된 `externalUiDriver` 계약을 제거했다.
  - config에 `driver: production-input`, 결과에 append-only `inputs` provenance를 남긴다.
- `tools/verify_yingchuan_battle_regression.mjs`
  - terminal outcome, 연속 round, input provenance, 위임 false->true, player/enemy AI phase,
    이동 보간/완료 commit, cinematic 22/36 및 11/25 tick, 카메라/사망을 엄격히 검증한다.
- `desktop/build.gradle.kts`
  - 공식 full-battle trace는 `timeScale=8`을 사용한다. 이는 simulation timestamp와 callback
    순서를 유지하면서 wall-clock과 수백 MB JSON 누적 비용만 줄인다. assertion이나 원본
    tick 계약은 완화하지 않았다.

### 최신 확정 통과 결과

```text
node tools/verify_yingchuan_battle_regression.mjs
  PASS: 3268 frames, round 9, PLAYER_VICTORY, reason=battle-end

./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyYingchuanBattleRegression --rerun-tasks
  BUILD SUCCESSFUL in 1m15s, 3267 frames, PLAYER_VICTORY

./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyCampaignScreenE2e --rerun-tasks
  BUILD SUCCESSFUL in 5m15s
  Title -> R00 scene0/1/2/3 -> S00 -> round8 PLAYER_VICTORY ->
  result scene1 -> reward -> scene2 -> save prompt -> R01 scene0/1
  extra transition Enter=0, battle trace=15554 frames, reason=battle-end
```

최신 full-battle cinematic 표본:

- `474 -> 235`, anime21: start `1.846416`, hit `2.761433`(22 tick), complete
  `3.29476`(36 tick)
- `477 -> 334`, anime25: start `8.228059`, hit `8.628076`(11 tick), complete
  `9.1614`(25 tick)

### 최신 전체 검사 결과와 프로세스 확인

인계 작성 중 실행하던 프로세스는 정상 종료했다.

```text
./gradlew --no-daemon -Pkotlin.incremental=false :desktop:check
BUILD SUCCESSFUL in 10m 24s
314 actionable tasks: 299 executed, 15 up-to-date
```

다음 작업 전에 다른 Gradle/GUI 검증이 남아 있지 않은지 확인한다.

```bash
ps -axo pid,ppid,state,etime,command | rg 'gradle|java|jojo|electron' | rg -v 'rg '
```

이번 전체 check에서 확인된 상태:

- campaign E2E 통과
- R00~S55 choice fixture 전부 통과
- random low/high fixture 전부 통과, coverage task 통과
- win-condition pairwise `cases=3 steps=9` 통과
- actor-state 비교 통과: dialogue step 1/2/3의 speaker와 actor 19/19/18 일치
- full-battle regression 통과: 3267 frames, PLAYER_VICTORY
- modal capture 8종 통과
- selection render 통과: unit 210, tile `(10,17)`, move 30, attack 8

### 남은 작업 — 반드시 이 순서로 수행

1. **최신 unfiltered core suite 재실행**
   - production driver 이후 main compile은 검증됐지만 마지막 trace/preference 변경을
     포함한 명시적 최신 기록을 남긴다.
   ```bash
   ./gradlew --no-daemon -Pkotlin.incremental=false :core:test
   ```
2. **독립 input-progress verifier 실행**
   ```bash
   node tools/verify_battle_input_progress.mjs
   ```
3. **논리 검증이 모두 green인 뒤에만 최종 render gate**
   ```bash
   python3 tools/verify_render_parity_scope.py \
     --scope tools/render_parity_scope.json --repository .
   ```
   `desktop:check`에서 영향 캡처가 fresh가 아니었다면 다음을 단독 재실행한다.
   ```bash
   ./gradlew --no-daemon -Pkotlin.incremental=false \
     :desktop:verifyYingchuanActorState \
     :desktop:verifyYingchuanModalCaptures \
     :desktop:verifyYingchuanSelectionRender --rerun-tasks
   ```
4. **문서 갱신 및 최신 게임 실행**
   - `RUNTIME_ARCHITECTURE_AUDIT.md`, `RUNTIME_UI_ROUTE_CLASSIFICATION.md`에 최종 증거를 반영한다.
   - stale 실행을 정리한 뒤 `/Users/ain/workspace/jojo`에서
     `./gradlew :desktop:run --no-daemon`을 실행한다.
5. **완료 범위 감사**
   - 위 검증은 Title→R00→S00→R01 및 영천전투의 강한 production 증거다.
   - 119개 시나리오는 AST/fixture coverage가 있어도 production 입력으로 전부 완주한 것이
     아니다. 전체 게임 100%가 목표면 아래 section 6.G 방식으로 route별 E2E를 확대해야
     하며, 그 전에는 goal을 complete로 표시하지 않는다.

### 아직 손대지 않은 위험과 판단 기준

- 자동 trace 실행이 실제 사용자 save/preferences를 오염할 가능성을 별도 감사해야 한다.
  auto-battle preference는 이미 격리했지만 `CampaignStore.persist()`가 automated trace에서
  no-op인지 확인되지 않았다. 수정한다면 `CampaignStore`에 transient mode를 추가해 메모리
  state transition은 유지하고 disk write만 막는 방식이 안전하다. 광범위한 변경이므로
  먼저 구현과 테스트를 읽고 focused test를 추가한다.
- `timeScale=1` standalone full trace가 32분 이상 걸린 것은 gameplay deadlock이 아니라
  수만 행의 거대한 JSON을 매 frame 누적한 비용이었다. 공식 8x 결과가 source tick 계약을
  검증하므로 timeout만 키우거나 다시 1x 대형 trace를 무작정 돌리지 않는다.
- 첫 opening script 때문에 standalone에서는 수동 조조 이동 기회가 없을 수 있다.
  standalone verifier가 수동 선택 provenance를 억지로 요구하지 않는 이유다. 실제 수동
  opening move는 campaign E2E가 좌표 변화와 commit으로 검증한다.

### 작업 노하우 요약

- 원본 JS/Python callback 순서가 유일한 권위다. 게임 코드와 기존 테스트가 일치해도
  원본과 다르면 둘 다 고친다.
- 상태 변경 시점은 `move complete`, attack `hit`, reaction/death `complete`에 맞춘다.
  논리 위치/HP를 먼저 바꾸고 뒤늦게 애니메이션을 그리는 구현을 금지한다.
- 자동화는 production screen + 설치된 production input processor를 통한다. private
  method, field 직접 대입, 강제 승리, 가짜 screen으로 통과시키지 않는다.
- trace는 append-only 관측이어야 한다. `{collocation:true}` 같은 기대값 literal을
  기록하지 말고 실제 false->true 변화, actor from/to, phase, camera, HP를 기록한다.
- 기준 구현/현재 게임 GUI capture는 foreground 우연성에 의존하지 않는다. verify mode의 원본
  Electron은 `backgroundThrottling:false`, 자동 LWJGL 창은 `(0,0)` 위치를 쓴다.
- 여러 에이전트는 원본 감사/테스트 계약 감사/서로 다른 파일 조사에 병렬 사용한다.
  Gradle compile/test는 공유 `build/classes`와 JAR race 때문에 반드시 직렬 실행한다.
- screenshot은 논리·로그·callback 검증이 끝난 마지막 단계다. 사용자가 지정한 UI만 →
  portrait → 화자명 → 대사 → 배경 → 캐릭터 순서를 유지한다.

## 1. 과거 상태 스냅샷 (section 0이 대체함)

이 작업은 **아직 최종 완료 선언 전**이다. 영천전투와 그 전후 캠페인 흐름의 주요
문제, 일반 `BAI_TUI` 퇴각 대사/애니메이션, `stage.unit().show()` 부활 표현까지
구현됐고 focused test와 실제 화면 E2E도 통과했다.

2026-09-04 최신 비증분 `:core:test`는 전체 통과했다(`BUILD SUCCESSFUL in 12s`,
141 actionable tasks). 최신 단독 `:desktop:check`는 약 2시간 55분 동안 campaign E2E,
modal 8종, 선택지 fixture 전부, random fixture 전부, win-condition pairwise까지
통과한 뒤 마지막 `verifyYingchuanBattleRegression`에서 실패했다. 그러므로 현재의
정확한 상태는 **core green / desktop 전체 red 1건**이다.

실패는 production 전투 자체가 멈춘 것으로 단정할 수 없다. standalone full-battle
trace driver가 opening script를 닫은 뒤 `round=1`, `camp=0`, `PLAYER_INPUT`,
`collocation=false` 상태에서 위임을 켜거나 턴을 종료하지 않아 600 simulation seconds,
36,003 frames에서 timeout됐다. 같은 production 전투를 실제 input processor로 구동하는
campaign E2E는 round 8 `PLAYER_VICTORY`와 R_01 전환까지 통과했다. 다음 담당자가 가장
먼저 바로잡을 부분은 이 standalone 검증 driver다.

2026-09-04 인수인계 갱신 기준 실행 중인 게임/검증 프로세스는 없고 Gradle/Kotlin daemon만
남아 있다. daemon은 정상이며 종료할 필요가 없다.

따라서 기존 문서의 “최종”, 개별 focused test 성공, 과거 스크린샷 성공만 보고
100% 완료라고 말하면 안 된다.

또한 현재 강한 실행 증거의 범위는 `Title -> R_00 -> S_00 -> R_01`과 영천전투의
주요 상호작용이다. 119개 시나리오 소스/AST가 모두 로드된다는 검증은 있지만 119개를
실제 입력·실시간 애니메이션으로 처음부터 끝까지 완주했다는 증거는 없다. 사용자가 말한
“전체 게임 100%”를 문자 그대로 전체 게임으로 해석한다면, 아래 회귀를 통과한 뒤에도
전 시나리오 production E2E 확대가 별도 장기 작업으로 남는다.

## 2. 디렉터리와 권위 소스

- 게임: `/Users/ain/workspace/jojo`
- 복원 원본: `/Users/ain/workspace/jojo_mobile/sgccz-desktop`
- 원본 JS: `sgccz-desktop/recovered-js/modules`
- 원본 시나리오 Python: `sgccz-desktop/decompiled-python`
- 현재 셸의 기본 cwd는 `/Users/ain/workspace/jojo_mobile`일 수 있으므로 Gradle과
  검색은 반드시 `/Users/ain/workspace/jojo`에서 실행한다.
- `/Users/ain/workspace/jojo`는 Git worktree가 아니다. `git diff`에 의존하지 말고
  파일 내용, mtime, 테스트 산출물로 변경을 추적한다.

원본 비교에서 특히 중요한 파일:

- `recovered-js/modules/battle/BattleLayer.js`
  - `unitDeath`: 약 7118~7140
  - `unitHide`: 약 7033~7114
  - `_endProcess` 저장 질문: 약 9406~9445
  - `_ai2` 이동 뒤 스크립트: 약 9680~9692
  - Mine/AI 진영 제어: 약 10710~10820
- `recovered-js/modules/battle/BattleUnit.js`
  - `hide(type)`: 약 2454
- `recovered-js/modules/ui/HallLayer.js`
  - `_scriptOver`: 약 821 (`incStage` 후 BattleScene)
- `decompiled-python/R_00.py`
  - `scene0..3`, `__dispatch__`: 마지막 부분
- `decompiled-python/S_00.py`
  - 보스 146/147의 명시적 `hide(2)`와 승리 보상/종료 분기

## 3. 사용자가 정한 검증 원칙

- 원본 코드가 권위다. 게임의 기존 동작이나 기존 테스트 기대값을 권위로 삼지 않는다.
- 스크린샷은 논리/콜백/로그 일치가 끝난 뒤 마지막에 비교한다.
- 그 전에는 좌표, 이미지/액션 ID, 프레임/타임스탬프, HP, 진영, 카메라,
  script state를 로그로 남겨 원본과 비교한다.
- 테스트가 강제 승리 context, private state 설치, capture screen 교체, delay skip,
  가짜 화면 단계로 정상 흐름을 우회하면 그 테스트는 완료 증거가 아니다.
- 전투 계산 결과는 애니메이션보다 먼저 화면/모델에 반영되면 안 된다. 원본 callback
  (`move complete`, attack `hit`, reaction complete, death hide complete)을 기준으로
  상태를 커밋한다.
- 병렬 에이전트는 읽기/감사/서로 다른 파일 작업에 사용하되 **Gradle 빌드는 동시에
  돌리지 않는다**. 공유 `build/classes`와 core JAR이 섞여 EOF 및
  `NoClassDefFoundError`가 실제로 발생했다.

## 4. 이번에 바로잡은 핵심 문제

### 4.1 캠페인 실제 경로

과거 테스트의 경로는 틀렸다. 원본 경로는 다음과 같다.

`Title -> R_00 scene0 -> scene1 -> scene2 -> scene3 -> S_00 -> 승리 scene1
-> reward -> scene2 -> 저장 질문 -> (선택적 SaveLayer) -> R_01 scene0`

- R_00과 S_00 사이에 `BattlePreparationScreen`이 나오면 안 된다.
- 새 게임은 R_00 `scene1`이 아니라 `scene0`부터 시작해야 한다.
- R_00의 `__dispatch__`는 scene0~3을 연속 실행한다.
- `scene3`은 동기적으로 즉시 끝나 한 프레임도 노출되지 않을 수 있다. 테스트를 위해
  인위적으로 한 프레임 yield하지 말고 실제 `playback.start(sceneN)` 호출을 append-only
  관측값으로 기록한다.
- raw stage는 Hall 종료에서 `0 -> 1`, Battle 종료에서 `1 -> 2`가 된다.
- 승리 뒤 저장 질문을 생략하고 R_01로 바로 가면 안 된다.
- R_01도 `scene1`이 아니라 `scene0`부터 실행해야 한다.

주요 게임 파일:

- `core/src/main/kotlin/com/jojo/game/JojoGame.kt`
- `core/src/main/kotlin/com/jojo/game/ScenarioScreen.kt`
- `core/src/main/kotlin/com/jojo/game/CampaignE2eTrace.kt`
- `core/src/main/kotlin/com/jojo/game/BattleScreen.kt`
- `desktop/src/main/kotlin/com/jojo/game/desktop/DesktopLauncher.kt`
- `tools/verify_campaign_screen_e2e.py`
- `desktop/build.gradle.kts`

잘못된 `NaturalCampaignTransitionTest`와 그 Gradle task는 제거했다. 이 테스트는 실제
화면을 만들지 않고 R00 scene1, 가짜 BattlePreparation, 강제 battle context,
R01 scene1을 정답으로 고정하고 있었다.

### 4.2 실제 화면 캠페인 E2E

최신 통과 산출물:

- `desktop/build/reports/campaign-screen-e2e.json`
- `desktop/build/reports/campaign-screen-e2e-battle.json` (약 247 MB이므로 통째로 출력 금지)

검증된 내용:

- production `TitleScreen`, `ScenarioScreen`, `BattleScreen` 사용
- 설치된 `InputProcessor`를 통한 타이틀, 대사/모달, 전투 메뉴, 위임, 보상,
  저장 질문 입력
- 가짜 BattlePreparation 없음
- scene0~3, S00 scene1/result scene1/scene2, R01 scene0/1
- 장면 전환을 위한 추가 Enter 0회
- raw stage `[0, 1, 2]`
- 위임 상태 `false -> true`
- 위임 이후 camp 0 유닛의 실제 비영점 좌표 이동
- 15,442 frames, round 8, `reason=battle-end`, `PLAYER_VICTORY`

직접 재검증 명령:

```bash
cd /Users/ain/workspace/jojo
python3 tools/verify_campaign_screen_e2e.py \
  desktop/build/reports/campaign-screen-e2e.json \
  desktop/build/reports/campaign-screen-e2e-battle.json
```

주의: 현재 E2E는 `timeScale=1`이다. 원본 delay를 줄이지 않는다. 전체 재실행은 약
5분 이상 걸린다.

### 4.3 전투 애니메이션과 상태 커밋

- AI/플레이어 이동, 공격, 마법, 아이템 상태를 source callback까지 지연 커밋한다.
- 물리 공격은 원본 BRAnime 이벤트를 사용한다.
  - `anime21`: hit 22 tick, complete 36 tick
  - `anime25`: hit 11 tick, complete 25 tick
- 범위 공격, 마법 pass, 반격, 추격, 반격 추격도 순차 presentation 후 커밋한다.
- AI 이동 결과가 먼저 보인 뒤 이동 애니메이션이 재생되던 문제를 제거했다.
- 적군/우군 턴을 즉시 계산하지 않고 RoundLayer, camp script, actor별 이동/공격
  presentation 완료를 기다린다.
- 카메라는 원본 `centerUnit` 경계에 맞춰 이동/공격/피격/마법 대상에 포커스한다.
- 불 효과는 실제 map fire/effect frame sequence를 사용하도록 수정돼 있다.

주요 파일:

- `Battle.kt`
- `BattleScreen.kt`
- `BattleAttackSequence.kt`
- `BattleCharacterPresentation.kt`
- `FullBattleTrace.kt`
- 관련 core tests

### 4.4 `unitDeath`와 명시적 `hide(type)`

원본의 정확한 공통 순서:

1. 공격/마법/아이템 전체 exchange(반격/추격 포함) 완료
2. `unitDeath` 첫 `run_script` 완료까지 대기
3. 그 시점에 dying unit을 다시 조회
4. `100*y+x` 순서로 정렬
5. 각 유닛을 순차 hide
6. generic dying unit이 실제로 있었을 때만 두 번째 `run_script`

중요한 이유:

- 첫 script가 HP 0 유닛을 살리거나 명시적으로 숨길 수 있으므로 result에서 사망 수를
  미리 계산하면 틀린다.
- S00 146/147은 첫 script 안에서 `hide(2)`를 실행하므로 generic BAI_TUI와 중복하면
  안 된다.
- 일반 자동 퇴각은 `anime23`, Mine self master만 `anime24`다.
- S00 boss는 명시적 `hide(2)`이므로 `anime24`다.
- self master는 character id 0 하드코딩이 아니라
  `mine-${stage.mineMasterInstanceId}`로 판별한다.

이번 변경에서 `ScenarioInterpreter`은 BattleScreen가 external presentation을 명시적으로
enable했을 때만 `stage.unit().hide(type)`을 callback 대기로 바꾼다. 이 gate가 없으면
R 시나리오 HallUnit.hide와 headless scenario fixture가 영구 DELAY에 빠진다.

연관 파일:

- `ScenarioRuntime.kt`: `ScenarioUnitHideRequest`
- `ScenarioInterpreter.kt`: external battle presentation gate
- `Battle.kt`: scripted hidden unit 보존
- `BattleScreen.kt`: typed hide 및 generic unitDeath state machine
- `UnitDeathPresentation.kt`
- `ScenarioRuntimeTest.kt`, `BattleDefeatPresentationTest.kt`

마지막 별도 에이전트가 구현 완료한 부분:

- 원본 `config.retreatTxt[characterId]`
- createMine의 `DEATH_MSG` 기본 on
- `stage.unit().retreatTxt(False)` 동적 off
- BAI_TUI에서 메시지가 있으면 실제 SayLayer 입력 완료 후 anime23/24 시작

추가 구현 결과:

- 실제 `ScenarioInterpreter.currentDialogue`를 사용하는 외부 say4 gate
- 대사 dismiss 전에는 anime23/24가 시작되지 않음
- 다중 사망의 unit별 `say -> animation` 직렬 처리
- hide 완료 시 HP 복원, retreat flag/count, child node visibility, hidden object 보존
- self master를 만난 뒤 shared hide type이 2로 유지되는 원본의 미묘한 동작

focused test는 다음 명령으로 통과했다. 전체 검증은 아직 실행하지 않았다.

```bash
./gradlew core:test \
  --tests com.jojo.game.BattleDefeatPresentationTest \
  --tests 'com.jojo.game.ScenarioRuntimeTest.native battle say4 borrows the production dialogue state until input dismisses it'
```

그 뒤 별도 감사에서 아래 누락도 발견해 구현했다.

- generic death hide 완료 시 stage proxy도 invisible로 동기화
- 이미 invisible인 `hide()`와 이미 visible인 `show()`는 즉시 no-op
- invisible unit의 `stage.unit().show()`는 external presentation request를 만들고
  원본처럼 callback까지 script를 대기
- show 시 active roster 복귀, 위치/방향 복원, HP/MP 완전 회복, status/attribute lift,
  `RETREAT`, `hasMoved`, child node visibility 초기화
- show animation은 원본 `anime46`, 없으면 0.2초 fallback
- 퇴각 횟수를 CampaignState character attribute 15에서 load/write
- 패배 전환도 typed hide dialogue/hide/show가 끝날 때까지 대기

관련 추가 타입과 위치:

- `ScenarioRuntime.kt`: `ScenarioUnitShowRequest`
- `ScenarioInterpreter.kt`: invisible unit show의 external callback gate
- `Battle.kt`: presentation unit을 active roster로 복구하고 attr15 영속화
- `BattleScreen.kt`: typed show driver와 lose/combat busy gate

최신 변경 직후 focused 검증은 통과했다.

```bash
./gradlew --no-daemon -Pkotlin.incremental=false :core:test \
  --tests com.jojo.game.BattleDefeatPresentationTest \
  --tests com.jojo.game.ScenarioRuntimeTest
```

결과: `BUILD SUCCESSFUL in 27s`, 141 tasks. 단, JUnit 대상은 위 두 class로 필터됐다.

### 4.5 대화 UI와 캐릭터 표현

- 사용자 요청상 글자 rasterization/GPU sampling 차이는 제외한다.
- 대화 패널 Y가 원본보다 정확히 96 logical unit 낮던 문제를 수정했다.
  - 일반 panel y=428
  - portrait y=426
- 최종 검출값:
  - panel `(423,275)-(1791,639)`
  - body `(473,398)-(533,450)`
  - portrait `(1866,257)-(2098,410)`
- verifier는 panel body, tail silhouette, portrait geometry, opacity/blend/MAE를 검사한다.
- 강조/사망 표현, HP bar, 대사 패널 뒤 투명도·블렌딩 경로를 실제 BattleScreen에
  연결했다.
- 선택 overlay는 unit 210 at `(10,17)`, green move 30, red attack 8, cursor visible을
  확인했다.

연관 파일:

- `BattleScreen.kt`
- `tools/verify_yingchuan_dialogue_fixture.py`
- `tools/verify_yingchuan_actor_state.mjs`

### 4.6 랜덤 분기와 테스트 무결성

과거 랜덤 coverage는 29/31이었다. 실제 도달 가능한 R00 scene1 1376/1378을
unreachable로 처리하지 않고 low/high 4 fixture를 추가했다.

- 조건: `vars[1032]=1`, unit0 attr11>=40, info random 29 또는 41
- 현재 report: 31/31, 100%, `uncovered=[]`
- `tools/verify_scenario_random_coverage.py`는 uncovered 상세도 출력한다.

전체 `desktop:check`에서 한 번 `verifyR00Random1089High`가 기대 line 1089 대신
line 1232를 기록하며 실패했다. 게임 로직 결함이 아니라 fixture가 `lab1542`로 직접
진입하면서 원본이 label 전에 실행하는 `gvars[0]=0`을 건너뛴 것이 원인이었다.
이전 campaign run의 `gvars[0]=170`이 남아 loop를 통과해 다음 random site로 간 것이다.

수정:

- `desktop/build.gradle.kts`의 `scenarioRandomFixture.doFirst`에서 이전 trace 삭제
- `lab1542`, `lab1900` 직접 진입 fixture에 `--verify-globals=0:0` 명시

수정 뒤 아래 fresh coverage는 통과했다.

```text
SCENARIO_RANDOM_COVERAGE_OK fixtureTraces=62 declaredRandomSites=31
coveredRandomSites=31 uncoveredRandomSites=0 coveragePercent=100.0
BUILD SUCCESSFUL in 1m55s
```

무결성 게이트 최신 직접 실행 결과:

```text
RUNTIME_TEST_INTEGRITY_OK isolatedGates=5 trackedTypes=15
knownHarnessOnly=1 integrationDebt=0 isolatedContracts=1 resolved=-
```

`BattleSceneCoordinator` 1개는 reference contract로 격리되어 있고 runtime 완료 증거로 세면 안 된다.

## 5. 테스트를 우회해 놓쳤던 사례와 교훈

1. **기존 테스트 기대값도 의심한다.**
   `NaturalCampaignTransitionTest`가 원본에 없는 BattlePreparation을 정답으로 만들었다.
2. **production screen을 만들지 않는 테스트는 화면 전환 증거가 아니다.**
3. **boolean만으로 이벤트 순서를 증명하지 않는다.**
   `playerMoveCommitted=true`는 제자리 행동도 위장할 수 있었다. actor/from/to와 실제
   trace 좌표 변화를 함께 검증한다.
4. **trace 필드는 실제 상태를 기록해야 한다.**
   `collocation:true` literal은 UI에서 위임을 켰다는 증거가 아니었다. 실제 값의
   false->true 전환을 기록하도록 바꿨다.
5. **화면 좌표와 world 좌표를 섞지 않는다.**
   `ExtendViewport.unproject` 때문에 저장 질문/메뉴 클릭이 빗나갔다. 표시 sprite bounds와
   입력 hitbox도 서로 달랐고, 보이지 않는 좌표를 눌러 테스트를 통과시키지 않고
   production hitbox를 고쳤다.
6. **전투 outcome과 시나리오 end는 다르다.**
   round 8에서 PLAYER_VICTORY가 나도 `unitDeath -> run_script`가 빠지면 reward/end/save
   prompt로 가지 못한다.
7. **사망 애니메이션을 hit 시점에 미리 예약하지 않는다.**
   첫 scene1 대사가 끝난 뒤 script-owned hide가 시작될 수 있다.
8. **dead unit을 너무 일찍 모델에서 없애지 않는다.**
   첫 script가 attr7=0을 읽고 명시 hide/revive할 수 있도록 pending HP0 unit을 context에
   포함한다.
9. **동기 완료 장면을 프레임 관측만으로 판단하지 않는다.**
   scene3처럼 한 render 안에서 시작/완료/route될 수 있다.
10. **stale artifact를 지운 뒤 실행한다.**
    Gradle capture task의 `doFirst`에서 이전 JSON을 삭제해야 실패가 과거 결과로 통과하지
    않는다.
11. **공유 Gradle 빌드는 직렬화한다.**
    병렬 에이전트가 동시에 core JAR/classes를 쓰면 EOF/NoClassDef가 생겼다. 과거
    `desktop:check` 실패 중 하나는 `ScenarioCommand$SetUnitAction` ClassNotFound였으며
    production 실패가 아니라 빌드 race였지만, 유효한 전체 pass도 아니다.
12. **fixture 시작점의 선행 상태를 명시한다.**
    label 중간 진입은 원본이 label 전에 초기화하는 global/local을 건너뛴다. random
    fixture의 `gvars[0]` 누락처럼 이전 실행 상태가 섞이면 엉뚱한 분기까지도 정상처럼
    보일 수 있다.
13. **hide와 show를 한 쌍의 lifecycle로 감사한다.**
    모델 roster만 바꾸거나 sprite만 숨기면 stage proxy, HP/MP, status, 퇴각 횟수,
    callback 대기가 서로 어긋난다. 원본의 hide 후 재등장 시나리오까지 확인한다.
14. **병렬화는 조사와 서로 독립적인 편집에만 사용한다.**
    에이전트별로 원본 흐름 감사, campaign E2E 감사, death/show callback 감사를 나눴고,
    결과를 주 에이전트가 통합했다. 공유 Gradle compile/test는 반드시 한 프로세스로 한다.
15. **GUI 검증기는 foreground 우연성에 의존하지 않는다.**
    원본 Electron 창이 다른 창 뒤에 가자 rAF/timer가 throttling되어 actor-state 캡처가
    map-only 상태에 머물렀다. 원본 `electron/main.cjs`에서 verify mode에만
    `backgroundThrottling: false`를 적용했고, runner에는 Electron 180초/Gradle 600초
    timeout을 넣었다. 일반 실행은 기존 throttling 동작을 보존한다.
16. **수백 개의 짧은 LWJGL 프로세스는 OS/GLFW 경계도 드러낸다.**
    전체 check 중 macOS가 순간적으로 null primary monitor를 반환해 자동 창 중앙 정렬에서
    NPE가 났다. `DesktopLauncher`는 verify/capture/trace 실행에만 `(0,0)` 명시 위치를
    쓰고 일반 실행은 중앙 정렬을 유지한다. focused S13 fixture 재실행으로 확인했다.
17. **자동 전투 검증 driver도 production 입력을 사용해야 한다.**
    dialogue를 private method로 닫고 전투 상태를 직접 바꾸는 driver는 실제 메뉴/위임
    경로의 결함을 숨긴다. 설치된 `InputProcessor`에 실제 Enter/pointer 입력을 보내고,
    trace에서 `collocation false -> true`와 actor 이동을 확인한다.

## 6. 과거 작업 순서와 상세 참고 (실행 순서는 section 0 우선)

### 0. 해결된 과거 blocker: standalone full-battle driver

현재 실패 산출물은 다음과 같다.

- `desktop/build/reports/yingchuan-battle-regression-trace.json` (약 276 MB)
- `reason=timeout`, `frames=36003`, `summary.round=1`, `summary.outcome=null`
- 마지막 프레임도 `camp=0`, `phase=PLAYER_INPUT`, `dialogue=0`,
  `collocation=false`, script complete 상태다.

직접 원인은 `core/src/main/kotlin/com/jojo/game/BattleScreen.kt`의
`driveFullBattleTrace()`가 standalone mode에서 dialogue/choice/modal만 처리하고,
opening script가 끝난 뒤 player input을 통해 위임을 켜거나 턴을 넘기는 동작은 하지
않는다는 점이다. `DesktopLauncher.kt`는 campaign trace가 있을 때만
`externalUiDriver=true`로 만든다. campaign E2E driver는 실제 설치된
`Gdx.input.inputProcessor`를 통해 전투 메뉴와 위임을 조작하므로 정상 완주한다.

권장 수정 방법:

1. campaign E2E가 쓰는 production input 경로를 재사용한다. 현재 좌표/순서는
   battle menu `(1191,645)` -> END_ROUND `(656,550)` -> 위임 toggle `(458,434)` ->
   confirm `(791,434)`이며 모두 설치된 `Gdx.input.inputProcessor`로 보낸다.
2. `battle.collocation = true` 직접 대입, 강제 `nextTurn()`, 강제 승리 같은 state mutation은
   금지한다. 그런 수정은 사용자가 지적한 테스트 우회 문제를 다시 만든다.
3. `CampaignE2eTraceDriver.driveBattle()`의 input helper를 재사용 가능한 public-input
   driver로 추출하는 방식이 가장 안전하다. `JojoGame.render`에서
   `BattleScreen.campaignE2eState()`를 관찰해 standalone trace도 같은 driver로 구동한다.
4. opening dialogue/choice/modal/win-condition도 가능하면 private method 직접 호출 대신
   Enter/visible click으로 처리한다. `driveFullBattleTrace()`는 timeout, 기록, terminal
   flush 역할만 맡기는 방향이 좋다.
5. external/public driver 모드에서도 outcome 뒤 `finishFullBattleTrace("battle-end")`가
   실행되어야 한다. 현재 `externalUiDriver` outcome branch의 즉시 return을 그대로
   재사용하면 결과 파일을 끝내지 못한다.
6. trace에는 `collocation false -> true` 전환과 실제 camp0 actor 이동이 남아야 한다.
7. 먼저 verifier 자체 기본값인 timeScale 8로 빠르게 진행 여부를 확인한 뒤, 공식
   Gradle task(timeScale 1)를 실행한다.

살펴볼 파일과 위치:

- `core/src/main/kotlin/com/jojo/game/BattleScreen.kt`: `driveFullBattleTrace()` 약 2262행
- `core/src/main/kotlin/com/jojo/game/CampaignE2eTrace.kt`: 실제 화면 입력 driver
- `core/src/main/kotlin/com/jojo/game/FullBattleTrace.kt`: config/trace 계약
- `desktop/src/main/kotlin/com/jojo/game/desktop/DesktopLauncher.kt`: `externalUiDriver`
- `tools/verify_yingchuan_battle_regression.mjs`: 종료/AI/애니메이션 검증 계약
- `tools/verify_battle_input_progress.mjs`: deterministic driver가 end-turn을 보낸다는
  현재 주석은 실제 구현과 불일치하므로 구현과 assertion을 함께 바로잡는다.
- `desktop/build.gradle.kts`: capture/verify task의 공식 인자

빠른 재현/확인:

```bash
cd /Users/ain/workspace/jojo
node tools/verify_yingchuan_battle_regression.mjs
```

공식 focused gate:

```bash
./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyYingchuanBattleRegression --rerun-tasks
```

이 verifier는 terminal outcome뿐 아니라 적군 AI의 `FOCUS_DELAY -> MOVING ->
ACTION_DELAY -> ACTION -> COMPLETE`, 이동 전 logical position/피격 전 HP 유지,
move2 보간, 첫 cinematic 공격의 22/36틱 및 11/25틱, 카메라 clamp, 사망/퇴각 표현까지
검사하므로 assertion을 약화하거나 timeout만 늘려 통과시키면 안 된다.

### A. 최신 코드의 전체 core 회귀

가장 마지막 show/revival 변경까지 포함한 unfiltered core suite는 이미 단독 통과했다.
standalone driver 수정이 `BattleScreen.kt`를 건드리므로 수정 후 다시 실행한다.

```bash
cd /Users/ain/workspace/jojo
./gradlew --no-daemon -Pkotlin.incremental=false :core:test
```

실패하면 테스트 기대값을 임의로 게임에 맞추지 말고 원본 JS/Python의 호출 순서와
callback을 다시 확인한다.

### B. 빠른 정적/집중 검증

다른 Gradle 프로세스가 없는지 먼저 확인한다. 인수인계 작성 도중 아래의 오래된 게임
프로세스가 실행 중이었으나 최종 `ps` 재확인에서는 사라졌다. 다시 나타나면 최신 코드를
사용하지 않는 stale 실행이므로 전체 검증 전에 종료한다.

```text
gradlew :desktop:run --no-daemon --args=--battle --scenario=S_00
DesktopLauncher --battle --scenario=S_00
```

최종 빌드 전 종료하고, 전체 검증 뒤 최신 빌드로 다시 실행하는 것이 안전하다.

```bash
cd /Users/ain/workspace/jojo
python3 tools/verify_runtime_test_integrity.py
./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyScenarioRandomCoverage --rerun-tasks
```

확인 기대값:

- runtime integration debt 0
- random 31/31, 100%, uncovered 0

### C. 영천전투 집중 회귀

```bash
./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyYingchuanActorState \
  :desktop:verifyYingchuanModalCaptures \
  :desktop:verifyYingchuanSelectionRender \
  :desktop:verifyYingchuanBattleRegression
```

그 뒤 실제 화면 캠페인 E2E:

```bash
./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyCampaignScreenE2e
```

### D. 전체 desktop 검증 (반드시 단독)

```bash
./gradlew --no-daemon -Pkotlin.incremental=false :desktop:check
```

- 최신 unfiltered `core:test`는 141 actionable tasks, 12초에 통과했다.
- 최신 단독 `desktop:check`는 2시간 55분 27초 동안 312 tasks 중 297개를 실행했고,
  campaign/choice/random/win-condition을 통과한 뒤 마지막 full-battle timeout으로
  실패했다.
- 따라서 blocker focused gate를 먼저 통과시킨 뒤 **동일 최신 코드로 완전한 단독
  `desktop:check` 성공**을 남겨야 한다.
- `desktop:check`는 실제 캠페인 E2E와 매우 많은 짧은 LWJGL fixture를 포함하므로 현재
  약 3시간이 걸릴 수 있고 GUI window가 반복해서 뜬다.
- 실행 중 tool session을 60초 이하 간격으로 poll하여 사용자에게 진행 상황을 알린다.

### E. 로그가 모두 맞은 뒤 최종 스크린샷

사용자가 명시한 순서대로 논리가 모두 통과한 뒤에만 최종 screenshot 비교를 한다.

1. 대화상자 UI만
2. 초상화
3. 화자명
4. 대화 내용
5. 배경
6. 캐릭터
7. 전투 첫 피격/사망/fire/HP bar/blending 주요 frame

기존 캡처가 통과했어도 death/retire renderer 변경 뒤 영향 suite를 재실행해야 한다.

현재 canonical render report 162개는 아래 gate를 통과해 screenshot 단계 진입 조건은
열려 있다. 다만 이것은 모든 report를 최신 코드로 재생성했다는 뜻이 아니므로,
`desktop:check` 뒤 영천/대화/전투 영향 capture를 fresh 실행해야 한다.

```bash
python3 tools/verify_render_parity_scope.py \
  --scope tools/render_parity_scope.json --repository .
```

기존 결과: `SCREENSHOT_GATE_OPEN states=162 failures=0`.

### F. 문서와 실행

- `RUNTIME_ARCHITECTURE_AUDIT.md` section 12의 최종 실행 결과를 최신 전체 pass로 갱신한다.
- `RUNTIME_UI_ROUTE_CLASSIFICATION.md`에 실제 Title->R00->S00->save->R01 normal route와
  campaign E2E 증거를 추가한다.
- 완료 조건을 모두 증명한 뒤에만 goal을 complete로 표시한다.
- 마지막으로 stale 게임 프로세스를 종료하고 최신 게임를 실행한다.

예시:

```bash
cd /Users/ain/workspace/jojo
./gradlew :desktop:run --no-daemon
```

### G. 전체 게임 범위를 요구할 때의 후속 계획

현재 집중 범위를 통과한 뒤, “119개 전체 시나리오 완주”까지 요구되는지 먼저 작업 범위를
문서화한다. 전체 게임이 범위라면 각 `R_* -> S_* -> 다음 R_*` 구간에 대해 production
screen E2E를 데이터 주도 방식으로 확장한다.

1. 각 시나리오의 scene dispatch, 선택지, random site, stage API 호출 목록을 원본 AST에서
   추출한다.
2. headless `--verify-all-scenarios`는 로드/문법 smoke로만 유지한다.
3. 구간별로 실제 screen, 실제 input processor, 원본 delay/callback을 쓰는 E2E를 만든다.
4. 로그에서 좌표, sprite/action id, frame, HP/MP, camp, camera, modal, route를 비교한다.
5. 논리 로그가 일치한 구간만 최종 screenshot 비교 대상으로 올린다.
6. 모든 구간이 green일 때만 전체 게임 100%를 선언한다.

대략적인 남은 시간은 standalone driver 수정/집중 검증 1~3시간, core 1분 이내,
전체 desktop check 약 3시간, 영향 범위 최종 재캡처 15~60분이다. 추가 결함이 나오면
건당 30분~수 시간이 더 필요하다. 119개 전체 production E2E 확대는 현재
증거만으로 시간을 확정할 수 없는 별도 장기 범위다.

## 7. 완료 판정 체크리스트

- [x] 퇴각 대사 -> SayLayer 완료 -> typed hide 애니메이션 focused 회귀 통과
- [x] generic unitDeath first script/dying re-query/sorted hide/second script focused 회귀 통과
- [x] 최신 focused test에서 hide/show/revival/퇴각 상태 계약 통과
- [x] 최신 show 변경 전 실제 campaign E2E에서 새게임 R00 scene0~3 실행
- [x] 최신 show 변경 전 R00 -> S00 직접 전환, BattlePreparation 없음
- [x] 최신 show 변경 전 raw stage 0->1->2
- [x] 최신 show 변경 전 S00 실제 camp0 move 뒤 scene1
- [x] 최신 show 변경 전 round8 PLAYER_VICTORY -> reward -> scene2 -> save prompt -> R01 scene0
- [x] 최신 show 변경 전 transition extra Enter 0
- [x] random coverage 31/31 (random fixture fix 뒤 fresh 재검증)
- [x] runtime integrity debt 0 (최신 show 변경 전 unfiltered core run)
- [x] 최신 전체 core green (141 actionable tasks, 12초)
- [x] standalone full-battle driver가 production input으로 위임을 켜고 terminal 도달
- [x] `verifyYingchuanBattleRegression` fresh green
- [x] desktop 전체 green (병렬 빌드 없는 단독 run, 10m 24s)
- [x] 최신 코드로 적/우군 턴과 이동/공격/death/show presentation 순서 재확인
- [ ] 최신 코드로 논리 검증 후 최종 UI/전투 screenshot suite green
- [ ] 최신 게임 실행

위 항목 중 하나라도 증거가 없으면 “100% 완료” 또는 “전체 전환 완료”라고 보고하지 않는다.

## 8. 다음 개발 에이전트에게 전달할 시작 지시문

아래 내용을 그대로 전달해도 된다.

```text
/Users/ain/workspace/jojo/DEVELOPMENT_HANDOFF_2026-09-04.md를 처음부터 끝까지 읽고
section 0의 최신 상태와 남은 작업부터 수행해라. 원본은
/Users/ain/workspace/jojo_mobile/sgccz-desktop이며 원본 JS/Python이 권위다.

standalone full-battle production input blocker는 해결됐고 focused regression,
campaign E2E, 단독 desktop:check(10m 24s, 314 tasks)가 green이다. 다음으로 최신
unfiltered core:test와 verify_battle_input_progress.mjs를 실행하고, 둘 다 green이면
render parity scope 및 영향 capture를 최종 확인해라. 이후 runtime audit/route 문서를
갱신하고 최신 게임를 실행해라.

state 직접 대입, nextTurn 강제 호출, 강제 승리, assertion 완화, timeout 증가로
우회하지 마라. 여러 에이전트가 조사하는 것은 허용하지만 Gradle은 공유 산출물 race를
막기 위해 한 프로세스씩 실행해라. 논리/콜백/trace가 모두 통과한 뒤에만 screenshot을
비교한다. 119개 시나리오 production 완주는 아직 증명되지 않았으므로 전체 게임
100%라고 선언하지 말고 route별 E2E 확대 여부를 완료 범위로 감사해라.
```
