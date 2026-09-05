# 조조전 원본 충실 포팅 — 개발 에이전트 최신 인수인계

> **2026-09-05 이후에는 `PORTING_AGENT_HANDOFF_CURRENT.md`를 먼저 읽는다.** 이 문서는
> 이전 작업의 상세 기록을 보존하기 위한 자료이며, 최신 통과/실패 상태와 실행 순서는 새 문서가
> 우선한다.

작성 기준: 2026-09-04  
포트 작업 공간: `/Users/ain/workspace/jojo`  
원본 작업 공간: `/Users/ain/workspace/jojo_mobile/sgccz-desktop`

## 0. 2026-09-04 후속 작업 상태

아래 본문의 SayLayer fresh-render 실패 기록은 당시 상태를 보존한 것이다. 그 이후
`drawScriptDialogue()`에 원본 `SayLayer._resetPos()`의 동적 화자 위치와 카메라 보정이
반영됐지만, 사용자의 지시에 따라 전체 논리 전환이 끝나기 전에는 스크린샷 게이트를 다시
실행하지 않았다. 따라서 이 항목은 **수정됨/최종 시각 검증 대기**로 읽어야 한다.

이번 후속 작업에서 다음 논리 누락을 발견하고 수정했다.

- `BATTLE_CAMP`를 기존 3종에서 원본 4종
  `MINE → FRIEND → ENEMY → REINFORCEMENTS`로 복원했다. 시나리오 적 유닛의 `yj != 0`은
  이제 별도 `Faction.REINFORCEMENTS`로 보존된다.
- camp 시작/종료, 라운드 스크립트의 사망 처리를 원본의
  `pre-script → serial unitHide → post-script → outcome` callback barrier로 정렬했다.
- 물리 공격 결과에 `physicalPasses`를 추가해 각 공격 회차의
  `primary → CTGJ targets → next pass` 순서와 대상별 MPFY/JQFY/XXGJ/QXL/XSJQ/FTSH/ZDSY를
  손실 없이 보존한다. `BattlePhysicalResultAdapter.kt`가 이를 렌더 callback plan으로 변환한다.
- FightLayer는 animeFR callback1 사운드, 정확한 gray/highlight shader, 128×160 초상화 mask,
  FIFO begin/complete/resume 로그와 S_01 명령 순서 검증까지 반영했다.

직렬 통합 검증 결과:

```text
:core:test (선택 6개 핵심 클래스) + :desktop:compileKotlin
BUILD SUCCESSFUL in 17s
```

아직 완료되지 않은 가장 중요한 항목은 `physicalPasses`를 `BattleLayer`의 실제 시간축에
연결하는 일이다. 모델의 순서는 맞아졌지만 기존 renderer의 scalar
primary/splash/follow-up/counter 큐가 남아 있어, 이 연결을 끝내기 전에는 전투 표현이 원본과
같다고 판단할 수 없다.

## 1. 반드시 먼저 읽을 결론

이 포팅은 **아직 100% 완료되지 않았다.** 현재 상태를 완료라고 선언하거나 기존의
`BUILD SUCCESSFUL` 기록만 근거로 작업을 끝내면 안 된다.

가장 최근 추가한 fresh 렌더 게이트가 전투 대화창의 실제 위치 차이를 검출하고 있다.
현재 재현되는 첫 실패는 다음과 같다.

```text
dialogue panel geometry source=(423, 440, 1725, 804)
port=(423, 275, 1726, 639)
delta y=165 pixels
```

재현 명령:

```bash
cd /Users/ain/workspace/jojo
./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyFreshBattleRenderParity --rerun-tasks
```

이 명령은 약 42초 후 대화 step 1 비교에서 실패했다. 허용 오차를 늘리거나 검사를
우회하지 말고 원본 `SayLayer._resetPos()`와 카메라 동작을 포트에 옮겨야 한다.

또한 117개 numbered scenario 중 production 화면과 production input으로 강하게 완주한
범위는 아직 극히 일부다. `R_00 -> S_00 -> R_01`은 검증됐고 `R_01` 이후를 확장하기 위한
기반만 구현됐다. 정적 파싱/fixture coverage를 전체 플레이 완료 증거로 취급하지 않는다.

기존 문서 `PORTING_HANDOFF_2026-09-04.md`에는 유용한 상세 기록이 많지만, 상단의 과거
green 상태는 현재 fresh gate 추가 전 결과다. **상태 판단은 이 문서를 우선한다.**

## 2. 사용자가 요구한 완료 기준

- 원본 JS/Python 실행이 유일한 동작 권위다. 포트 코드와 포트 테스트가 서로 일치해도
  원본과 다르면 둘 다 수정한다.
- 화면 캡처는 논리 전환을 모두 맞춘 마지막 단계에서 사용한다.
- 그 전에는 원본/포트 양쪽에서 좌표, 이미지/action ID, 프레임 또는 simulation timestamp,
  HP, 진영, 카메라, script state와 callback 순서를 로그로 남겨 비교한다.
- 글자 래스터링과 GPU sampling의 미세 차이는 무시할 수 있다. 하지만 강조/사망 표현,
  HP 바, 대사 패널 뒤 투명도·블렌딩, 위치와 크기, 애니메이션 타이밍은 맞아야 한다.
- 정상 흐름을 private 함수 호출, 필드 직접 대입, 강제 승리, 가짜 화면, delay skip으로
  우회한 테스트는 완료 증거가 아니다.
- 전투 모델 변경은 원본 callback 시점에 커밋한다. 이동은 move complete, 피해는 attack
  hit, 피격/사망은 각 reaction/death complete가 기준이다.
- 전체 포트 완료를 선언하려면 주요 한 전투만이 아니라 전 시나리오 route를 production
  screen/input으로 검증해야 한다.

## 3. 작업 공간과 주의사항

- `/Users/ain/workspace/jojo`는 Git worktree가 아니다. `git status`나 `git diff`에
  의존할 수 없다. 파일 내용, mtime, 테스트 결과와 별도 백업으로 변경을 추적한다.
- 원본 JS: `../jojo_mobile/sgccz-desktop/recovered-js/modules`
- 원본 시나리오 Python: `../jojo_mobile/sgccz-desktop/decompiled-python`
- 원본을 수정해도 된다는 사용자 허가가 있다. 단, 원본 gameplay 의미를 포트에 맞게
  바꾸지 말고 로그/캡처 instrumentation과 독립 실행 환경 구성에 한정하는 것이 안전하다.
- 여러 에이전트가 같은 파일시스템을 공유한다. 원본 감사와 서로 다른 파일의 구현은
  병렬화하되 **Gradle compile/test는 항상 한 프로세스씩 직렬로 실행한다.** 동시 Gradle
  때문에 공유 `build/classes`/JAR에서 EOF와 `NoClassDefFoundError`가 실제 발생했다.
- 현재 조조 게임/Electron 검증 프로세스는 실행 중이지 않다. 일반 Gradle/Kotlin daemon만
  남아 있으며 정상이다. 다른 프로젝트의 Java 프로세스를 종료하지 않는다.

프로세스 확인:

```bash
ps -axo pid,ppid,state,etime,command | \
  rg 'gradle|java|jojo|electron' | rg -v 'rg '
```

## 4. 최근 구현된 변경

### 4.1 자동 검증 환경의 preferences/save 격리

관련 파일:

- `core/src/main/kotlin/com/jojo/port/GamePreferences.kt` (신규)
- `core/src/main/kotlin/com/jojo/port/JojoGame.kt`
- `core/src/main/kotlin/com/jojo/port/CampaignStore.kt`
- `core/src/main/kotlin/com/jojo/port/TitleScreen.kt`
- `core/src/main/kotlin/com/jojo/port/ScenarioPreviewScreen.kt`
- `desktop/src/main/kotlin/com/jojo/port/desktop/DesktopLauncher.kt`
- `core/src/test/kotlin/com/jojo/port/GamePreferenceProviderTest.kt` (신규)

`automatedRun`에서는 game별 `InMemoryPreferences`를 사용하며 실제 사용자 preference/save를
읽거나 덮어쓰지 않는다. 동일 game 내부에서는 이름별 store가 공유되고 서로 다른 자동
실행끼리는 격리된다. interactive 실행은 기존 platform preferences를 유지한다.

### 4.2 원본 출진 인원 계약 복원

관련 파일:

- `core/src/main/kotlin/com/jojo/port/ScenarioRuntime.kt`
- `core/src/main/kotlin/com/jojo/port/CampaignStore.kt`
- `core/src/main/kotlin/com/jojo/port/ScenarioPreviewScreen.kt`
- `core/src/test/kotlin/com/jojo/port/ScenarioRuntimeTest.kt`

구현된 규칙:

- 조조 ID 0이 존재하고 excluded가 아니면 필수 명단 맨 앞에 추가한다.
- 존재하지 않거나 excluded와 충돌하는 필수 ID는 제거한다.
- UI 최대 인원은 `min(authored raw max, available, 20)`이다.
- UI 최소 인원은 `max(1, 2 * floor(max / 3))`이다.
- mandatory 인원 수가 authored raw max 이상일 때만 준비 화면 없이 direct battle로 간다.
- `R_01`은 4~7명, 필수 `[0, 1]`로 복원됐다.
- `R_00`의 별도 implicit single-unit 경로는 유지한다.

### 4.3 campaign production E2E 확장 기반

관련 파일:

- `core/src/main/kotlin/com/jojo/port/CampaignE2eTrace.kt`
- `core/src/main/kotlin/com/jojo/port/BattlePreparationScreen.kt`
- `desktop/src/main/kotlin/com/jojo/port/desktop/DesktopLauncher.kt`
- `tools/verify_campaign_route_e2e.py` (신규)

`CampaignE2eStopPoint`, generic stage 계산, scenario별 input context와
`BattlePreparationScreen`의 read-only 관측 상태가 추가됐다. 새 CLI:

```text
--campaign-e2e-stop=R_NN:sceneIndex
```

기본 stop point는 기존 계약과 동일한 `R_01:1`이다. 명시 stop point에서는 최대 simulation
시간 3600초를 사용하고 영천전투 전용 exact contract는 비활성화된다. 아직 `R_02:1`까지의
실제 확장 실행을 성공시키지 않았으므로 아래 명령부터 검증해야 한다.

### 4.4 fresh 렌더 증거 게이트

관련 파일:

- `desktop/build.gradle.kts`
- `tools/verify_fresh_battle_render_parity.mjs` (신규)
- `tools/compare_battle_render_frames.py` (신규)
- `tools/verify_yingchuan_actor_state.mjs`
- `tools/verify_yingchuan_dialogue_fixture.py`
- `tools/verify_render_parity_scope.py`
- `tools/render_parity_scope.json`
- `tools/test_verify_render_parity_scope.py`
- 원본 `electron/main.cjs`

이전에는 `desktop:check`가 오래된 PNG/JSONL을 재사용할 수 있었다. 이제
`verifyFreshBattleRenderParity`가 매 실행마다 artifact를 지우고 다음을 새로 생성한다.

- 영천 대화 step 1, 2, 3 source/port PNG
- battle dialogue blending JSONL/PNG
- HP camps, outline/highlight, hit, cleanup, death action, death hidden의 JSONL/PNG
- run-id marker/manifest, mtime과 artifact membership

`desktop:check`는 이 fresh gate와 scope gate에 의존한다. 이것이 현재 숨겨져 있던 실제
대화창 위치 차이를 검출했다. gate를 약화하지 않는다.

자연 full-battle callback 순간의 framebuffer 캡처는 아직 fixture 캡처다. 실제 전투 중
callback predicate/readback hook을 core에 추가하는 작업이 남아 있다.

## 5. 가장 가까운 blocker: 전투 SayLayer 위치와 카메라

포트의 문제 위치:

```text
core/src/main/kotlin/com/jojo/port/BattleLayer.kt
drawScriptDialogue(): 약 6654행
```

현재 ordinary dialogue는 다음 값을 고정한다.

```kotlin
dialoguePanelY = 428f
dialogueFaceY = 426f
dialogueTextY = 468.314f
```

이는 대화 step 3에 맞춰진 값이라 step 1/2에서 패널이 96 logical pixel 잘못 배치된다.
현재 주석의 “ordinary S_00은 항상 bg0 y=146” 가정도 틀렸다.

원본 권위 코드:

```text
../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SayLayer.js:180
```

```javascript
_resetPos(layer, unit) {
  layer.centerUnit(unit);
  const r = this.convertToWorldSpaceAR(unit.node);
  const n = this._bg.position;
  const half = this._bg.getContentSize().height / 2;
  n.x = 0;
  const sign = r.y < 0 ? 1 : -1;
  n.y = r.y + (half + 48 + 32) * sign;
  this._bg.position = cc.v3(n);
}
```

fresh 원본 관측값:

| 대화 | 화자 | 화자 screen rect | bg0 position | bg2 logical rect | face logical rect |
|---|---:|---|---|---|---|
| step 1/2 | 235 | `[352,192,96,96]` | `[0,50]` | `[245.650,332,796,212]` | `[1064.618,330,192,240]` |
| step 3 | 477 | `[832,288,96,96]` | `[0,146]` | `[245.650,428,796,212]` | `[1064.618,426,192,240]` |

두 표본 모두 패널 bottom은 화자 sprite 중심 Y + 92 logical pixel이다. 하지만 이를 특정
ID별 상수로 고치지 말고 원본 `centerUnit -> convertToWorldSpaceAR -> 위/아래 선택` 규칙을
일반화해야 한다. 화자가 화면 위쪽에 있을 때 패널을 아래에 놓는 반대 분기도 필요하다.

권장 구현 순서:

1. `currentDialogue.speakerId`가 바뀌는 순간을 update/sync 단계에서 감지한다.
2. visible `BattleUnit`을 찾아 원본처럼 `focusCameraOn(speaker)`를 한 번 호출한다.
3. `visualTile(unit)`과 현재 `battleCamera`를 사용해 화자 sprite의 실제 screen/world 중심을
   계산한다. draw 단계에서 카메라를 바꾸면 grid는 이전 카메라로 그려지므로 피한다.
4. source `_resetPos`의 좌표계와 sign 분기를 그대로 helper로 옮긴다.
5. 산출한 panel offset을 portrait, speaker label, RichText/cached texture에 모두 동일 적용한다.
6. step 1 -> 2 -> 3 순서로 fresh gate를 다시 실행한다. 하나를 고친 뒤 다음 실패의 폭/본문
   위치를 실제 로그로 보정한다.

현재 상대 좌표 참고값:

- face bottom = panel bottom - 2
- 일반 speaker label baseline은 관측상 `panel bottom + 189.40`
- speaker 477 추출 texture Y는 step 3에서 `panel bottom + 160.9`
- speaker 477 body texture Y는 step 3에서 `panel bottom + 108.4`

cached Cocos RichText의 `worldY`도 특정 캡처의 절대값일 가능성이 있으므로 panel offset을
반영하는지 확인해야 한다. 단순히 panel/portrait만 움직여 gate를 부분 통과시키지 않는다.

`battleDialogueRenderEventLog()` 근처의 step-1 전용 geometry는 별도 blending fixture 계약일
수 있으므로 무조건 함께 바꾸지 말고 route 의미를 먼저 확인한다.

## 6. 검증된 것과 검증되지 않은 것

최근 변경을 포함한 focused 검증은 통과했다.

```bash
./gradlew --no-daemon -Pkotlin.incremental=false \
  :core:test \
  --tests com.jojo.port.ScenarioRuntimeTest \
  --tests com.jojo.port.GamePreferenceProviderTest \
  :desktop:compileKotlin
```

결과: `BUILD SUCCESSFUL in 33s`, 142 actionable tasks.

그보다 앞선 기준에서 다음도 통과했다.

```text
:core:test                                  PASS (141 tasks, 당시 코드)
verify_battle_input_progress.mjs            PASS (1347 frames, rounds 1/2)
verifyYingchuanBattleRegression             PASS (PLAYER_VICTORY)
verifyCampaignScreenE2e                     PASS (Title -> R00 -> S00 -> R01)
:desktop:check                              PASS in 10m24s (fresh gate 추가 전)
```

중요: 마지막 `desktop:check`는 fresh 렌더 게이트와 최근 네 묶음의 변경을 모두 포함한 최종
green이 아니다. 현재 `verifyFreshBattleRenderParity`가 red이므로 최신 전체 상태는 red다.

아직 검증되지 않은 핵심 범위:

- 동적 SayLayer 위치와 화자 변경 시 카메라 focus
- fresh battle render gate 전체 통과
- `R_01 -> BattlePreparation -> S_01 -> R_02:scene1` production E2E
- 그 이후 numbered scenario 대부분의 production E2E
- 자연 full-battle callback 시점의 framebuffer 캡처
- 여러 전투/병종/무기/마법에 대한 일반화된 애니메이션 callback 계약
- 전체 논리 green 이후 사용자가 지정한 단계별 최종 screenshot 비교

## 7. 남은 작업과 실행 계획

### P0. 현재 red인 SayLayer를 원본식으로 수정

위 section 5의 순서대로 동적 화자 위치, 카메라, 패널/초상화/화자명/본문의 공통 transform을
구현한다. 허용 오차나 expected PNG를 포트에 맞춰 바꾸지 않는다.

수정 직후 빠른 검증:

```bash
./gradlew --no-daemon -Pkotlin.incremental=false \
  :core:test --tests com.jojo.port.BattleLayerTest \
  :desktop:compileKotlin
./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyFreshBattleRenderParity --rerun-tasks
```

테스트 클래스 이름이 실제로 다르면 `rg --files core/src/test | rg 'Battle'`로 찾는다.

### P1. R02까지 production E2E를 실제 실행

```bash
cd /Users/ain/workspace/jojo
./gradlew :desktop:run --no-daemon --args="\
--campaign-e2e-trace=/tmp/campaign-r02.json \
--campaign-e2e-stop=R_02:1 \
--full-battle-trace=/tmp/campaign-r02-battle.json \
--full-battle-time-scale=8 \
--full-battle-max-sim-seconds=3600"

python3 tools/verify_campaign_route_e2e.py \
  /tmp/campaign-r02.json R_02 1
```

기대 경로는 Title -> R00 -> S00 -> R01 -> BattlePreparation -> S01 -> R02다.
실패하면 trace의 마지막 production screen/state/input을 원본 Python dispatch 및 JS callback과
대조한다. driver가 private continuation을 호출하게 고치지 않는다.

잠재 위험: `driveBattle`의 `initialScene1Started` assertion 메시지는 S00 전용 문구지만 조건은
generic이다. S01의 opening script가 player move보다 먼저 시작하는 원본 계약인지 확인하고,
원본이 그렇다면 assertion을 시나리오별 계약으로 분리한다. 선택지는 현재 첫 CHOICE를
production input으로 고르므로 S01 초기 퇴각 선택을 원본 경로와 대조한다.

R02까지 통과하면 별도 Gradle verification task로 고정한 뒤 기존 default R01 계약을
회귀 실행한다.

### P2. 117개 numbered scenario E2E 확대

manifest 119개에는 numbered `R_00..R_58` 59개와 `S_00..S_57` 58개, 그리고 common/train이
포함된다. 현재 production 완료는 R00/S00, R01은 부분이다. 나머지 대부분은 아직 실제
완주 증거가 없다.

한 번에 전체를 돌리지 말고 checkpoint 단위로 확장한다.

1. `R_02:1`, `R_03:1`, ... 식으로 stop point를 한 단계씩 늘린다.
2. 각 전투 준비 화면의 min/max/mandatory와 선택 입력을 trace에 남긴다.
3. 각 전투는 production input driver로 플레이하고 terminal outcome, 다음 raw stage,
   reward/save prompt/branch를 확인한다.
4. random/choice가 있는 route는 원본 seed와 선택 script를 명시해 재현 가능하게 만든다.
5. checkpoint verifier가 screen class, transition Enter=0, stage monotonicity, 준비 화면과
   battle 순서를 검증하게 한다.
6. 실패 시 해당 scenario만 focused 실행하고 원본 Python의 `__dispatch__`, scene, 전투 종료
   callback을 비교한다.

병렬 에이전트 권장 분담:

- 에이전트 A: 다음 R/S 원본 Python route, 선택지, stage 증가, 승패 분기 감사
- 에이전트 B: 해당 전투의 JS callback/AI/카메라/애니메이션 계약과 로그 expected 작성
- 에이전트 C: E2E trace/verifier 확장과 production input deadlock 원인 조사
- 통합 담당: 충돌 검토, 코드 merge, **유일한 Gradle 실행자** 역할

각 에이전트는 서로 다른 파일을 맡기고 같은 `BattleLayer.kt` 동시 편집은 피한다.

### P3. 전투 표현 일반화 전수 감사

영천전투의 특정 actor ID나 특정 캡처 상수로만 통과한 코드를 찾아 원본 일반 규칙으로
바꾼다. 우선 검색어:

```bash
rg -n 'S_00|474|477|235|fixture|hardcod|special' \
  core/src/main/kotlin/com/jojo/port/BattleLayer.kt
```

전수 비교 항목:

- 이동 전/중/완료의 모델 좌표 및 visual interpolation 순서
- player/enemy turn이 실제 시간 동안 보이는지와 AI phase 순서
- attack start -> hit -> target reaction -> death action -> hidden callback
- 불/마법 effect의 frame index, duration, anchor, loop/cleanup
- 강조 shader/outline, 사망/숨김 표현, HP bar의 진영색과 변경 시점
- 이벤트별 camera center/follow와 화면 경계 clamp
- dialogue auto-close와 다음 scene 자동 연결; 추가 Enter가 필요하지 않은지
- 조조 선택/이동/공격을 실제 input processor로 수행할 수 있는지

테스트 로그는 실제 관측값을 append-only로 기록한다. expected 값을 runtime에 주입하거나
원본 결과를 포트 출력으로 복사하는 방식은 금지한다.

### P4. 자연 callback framebuffer 증거

현재 six character state PNG는 production renderer fixture이지 자연 전투 callback 순간의
캡처가 아니다. full battle trace에서 predicate가 만족하는 프레임(hit 직전/직후, reaction,
death action, hidden)을 readback하고 PNG와 trace frame ID를 함께 기록하는 hook을 추가한다.
원본 Electron도 같은 predicate/시점을 사용한다. 이 단계는 논리 callback 로그가 green인
뒤 수행한다.

### P5. 최종 회귀와 screenshot 비교

빠른 것부터 직렬 실행한다.

```bash
./gradlew --no-daemon -Pkotlin.incremental=false :core:test
node tools/verify_battle_input_progress.mjs
./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyCampaignScreenE2e --rerun-tasks
./gradlew --no-daemon -Pkotlin.incremental=false \
  :desktop:verifyFreshBattleRenderParity \
  :desktop:verifyRenderParityScope --rerun-tasks
./gradlew --no-daemon -Pkotlin.incremental=false :desktop:check
```

전체 논리/로그가 green이 된 뒤에만 사용자가 정한 순서로 화면을 비교한다.

1. 대화상자 기본 UI만
2. 초상화 추가
3. 화자명 추가
4. 대화 내용 추가
5. 배경 추가
6. 캐릭터 추가

각 단계의 source/port artifact가 **현재 run-id에서 fresh 생성됐는지** 확인한다.

## 8. 작업하면서 얻은 핵심 노하우와 교훈

### 원본의 상태 변경 시점을 옮겨야 한다

결과 좌표나 HP만 맞추는 것으로는 부족하다. 과거 “적 이동 결과가 먼저 보이고 그 뒤 이동
애니메이션이 재생되는” 문제는 모델을 애니메이션 전에 커밋해서 생겼다. 원본 callback
경계를 먼저 trace하고 포트도 같은 경계에서 상태를 바꿔야 한다.

### 화면에 안 보이는 즉시 처리도 논리 성공이 아니다

적군 턴, 카메라 이동, 공격 준비, 피격, 사망은 모두 사용자가 볼 수 있는 duration을 갖는다.
최종 HP/승패만 확인하는 테스트는 이런 회귀를 놓친다. phase별 frame/timestamp와 active
animation을 함께 assert한다.

### 테스트 우회는 회귀를 숨긴다

강제 승리, private `nextTurn()`, 화면 state 직접 설치는 route와 input 결함을 감췄다.
자동화도 설치된 production `InputProcessor`에 key/touch/drag를 전달해야 한다. 상태는
read-only adapter로만 관찰한다.

### stale artifact는 green이 아니다

기존 scope 검사는 전날 생성된 PNG/JSONL을 읽고도 통과했다. 그래서 run-id, start/end
timestamp, mtime, manifest membership, 실행 전 artifact 삭제가 필요하다. 새 fresh gate가
실제 UI 차이를 발견한 이유다.

### 특정 스크린샷 상수는 일반 포팅이 아니다

S00/특정 actor만 맞춘 절대 좌표는 다음 화자와 다음 전투에서 깨진다. 원본 node transform,
anchor, camera, callback 공식을 helper로 옮기고 fixture는 그 공식을 검증하는 데만 쓴다.

### timeScale과 callback 계약을 분리한다

full trace의 `timeScale=1`은 per-frame 대형 JSON 때문에 30분 이상 걸렸다. 공식 검증은
`timeScale=8`을 사용하되 simulation timestamp, source tick, callback 순서는 그대로
검증한다. 시간을 줄이려고 callback/delay를 건너뛰면 안 된다.

### 빌드 병렬화보다 조사 병렬화가 안전하다

에이전트는 원본 감사, 테스트 계약 감사, route 데이터 조사에 병렬 사용한다. shared Gradle
output 때문에 빌드만은 직렬화한다. 긴 `desktop:check`를 개발 중 반복하지 말고 focused
test -> 해당 fresh gate -> checkpoint E2E -> 마지막 full check 순으로 실행한다.

### 자동 실행은 사용자 상태를 오염시키면 안 된다

검증은 독립 in-memory preferences를 사용해야 한다. 실제 save/preference가 바뀌면 다음
실행의 시작 stage와 option이 달라져 재현성을 잃는다. 새 자동 CLI를 추가할 때
`automatedRun` 판정에도 반드시 포함한다.

## 9. 주요 코드/도구 지도

- 전투 렌더/입력/카메라/animation: `core/src/main/kotlin/com/jojo/port/BattleLayer.kt`
- 전투 상태: `core/src/main/kotlin/com/jojo/port/BattleState.kt`
- scenario runtime: `core/src/main/kotlin/com/jojo/port/ScenarioRuntime.kt`
- campaign route/store: `core/src/main/kotlin/com/jojo/port/CampaignStore.kt`
- 길거리/회관 scenario 화면: `core/src/main/kotlin/com/jojo/port/ScenarioPreviewScreen.kt`
- 출진 화면: `core/src/main/kotlin/com/jojo/port/BattlePreparationScreen.kt`
- production E2E driver/trace: `core/src/main/kotlin/com/jojo/port/CampaignE2eTrace.kt`
- full battle trace: `core/src/main/kotlin/com/jojo/port/FullBattleTrace.kt`
- 실행 인자: `desktop/src/main/kotlin/com/jojo/port/desktop/DesktopLauncher.kt`
- Gradle 검증 task: `desktop/build.gradle.kts`
- fresh 렌더 orchestrator: `tools/verify_fresh_battle_render_parity.mjs`
- 렌더 로그 strict compare: `tools/compare_render_logs.py`
- framebuffer compare: `tools/compare_battle_render_frames.py`
- campaign route verifier: `tools/verify_campaign_route_e2e.py`
- 원본 SayLayer: `../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SayLayer.js`
- 원본 BattleLayer: `../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/BattleLayer.js`
- 원본 검증 런처: `../jojo_mobile/sgccz-desktop/electron/main.cjs`

## 10. 포트 실행 방법

일반 포트 게임:

```bash
cd /Users/ain/workspace/jojo
./gradlew :desktop:run --no-daemon
```

원본 게임:

```bash
cd /Users/ain/workspace/jojo_mobile/sgccz-desktop
npm start
```

실행 전에 오래된 포트/Electron 프로세스가 없는지 확인한다. 사용자가 단순히 “실행”을
요청하면 background 검증 fixture가 아니라 조작 가능한 일반 포트 게임을 실행한다.

## 11. 완료 선언 체크리스트

- [ ] SayLayer 동적 위치/카메라와 step 1/2/3 fresh 비교 통과
- [ ] fresh battle dialogue/character JSONL 및 PNG 전체 통과
- [ ] R02까지 production E2E 통과 및 회귀 task 고정
- [ ] 이후 numbered scenario를 checkpoint 방식으로 전부 production 완주
- [ ] 전투별 이동/공격/피격/사망/마법/AI/camera callback 로그 일치
- [ ] 자연 full-battle callback framebuffer 증거 확보
- [ ] 자동 검증이 실제 사용자 save/preferences를 오염시키지 않음
- [ ] 최신 `:core:test` 통과
- [ ] 최신 `:desktop:check` 통과
- [ ] 마지막 단계별 source/port screenshot 비교 통과
- [ ] 일반 포트 실행에서 시작, 대화 자동 연결, 조조 조작, 적 턴, 다음 scenario 전환 수동 확인

이 체크리스트가 모두 끝나기 전에는 “원본과 100% 동일” 또는 “전체 전환 완료”라고 보고하지
않는다.
