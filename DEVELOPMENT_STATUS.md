# 조조전 원본 충실 게임 개발 — 현재 개발 인수인계

> **2026-09-07 갱신.** 리팩터링·아키텍처 상태 판단은 [`REFACTORING_HANDOFF.md`](REFACTORING_HANDOFF.md)를 먼저 읽는다.
> 이 문서에 적힌 클래스 크기·분해 진행도는 2026-09-05 이전 값이며 실제보다 최대 3배 크다
> (`Battle` 787→288, `ScenarioScreen` 4,534→987, `BattleScreen` 9,578→7,094).
> 아래 내용 중 **원본 시각 재현·검증 게이트 기록**은 여전히 유효한 참고 자료다.

작성 기준: 2026-09-05 KST  
게임 작업 공간: `/Users/ain/workspace/jojo`
원본 작업 공간: `/Users/ain/workspace/jojo_mobile/sgccz-desktop`

> 이 문서를 상태 판단의 최우선 문서로 사용한다. 기존
> `DEVELOPMENT_STATUS_2026-09-04.md`, `DEVELOPMENT_HANDOFF_2026-09-04.md`에는 당시에는
> 유효했지만 현재 수정되었거나 아직 완료로 오해할 수 있는 기록이 섞여 있다.

## 1. 최종 목표와 완료 정의

목표는 원본 게임을 LibGDX 게임에서 처음부터 엔딩까지 플레이할 수 있게 만드는 것이며,
단순히 비슷하게 동작하는 재구현이 아니라 원본 JS/Python의 동작과 표현을 충실하게 옮기는
것이다.

완료는 아래 조건을 모두 만족할 때만 선언한다.

1. Title에서 새 게임을 시작해 마지막 엔딩까지 production screen과 실제 InputProcessor만으로
   자연스럽게 진행한다.
2. `R_00..R_58`, `S_00..S_57`의 실제 route, 선택지, 전투 준비, 승패, 보상, 저장 질문,
   다음 장면 전환을 검증한다.
3. 전투 이동/공격/피격/사망/마법/상태/카메라/AI 턴의 상태 커밋과 callback 순서가 원본과
   일치한다.
4. 궁정·길거리·전투의 공통 UI가 원본 asset, 위치, 크기, anchor, opacity, blend, z-order를
   사용한다.
5. 논리·로그 비교가 모두 통과한 뒤에만 최종 스크린샷 비교를 수행한다.
6. 글자 래스터링과 GPU sampling의 미세 차이는 제외할 수 있지만, 패널/아이콘/HP 바,
   강조·사망 표현, 투명도·블렌딩, 애니메이션 타이밍 차이는 제외하지 않는다.

금지되는 완료 근거:

- AST를 읽을 수 있음, asset 파일이 존재함, 단위 테스트가 green이라는 사실만으로 완료 선언
- private continuation 호출, 상태 직접 대입, 강제 승리/패배, 화면 가짜 fixture로 자연 진행 대체
- 오래된 trace/PNG 재사용
- verifier 조건 완화, 허용 오차 확대, 이벤트 생략으로 실패를 숨김

## 2. 현재 사실 기반 진행 상태

### 2.1 전체 인벤토리

- numbered scenario는 117개다.
  - 전투 모듈 `S_00..S_57`: 58개
  - 대화/이벤트 모듈 `R_00..R_58`: 59개
- `common`, `train`을 합친 embedded manifest는 119개다.
- `S_*`에는 `scene*` 함수 174개와 전투 배경 load 58회가 있다.
- `R_*`에는 `scene*` 함수 304개와 `loadBg` 전환 358회가 있다.
- `loadBg` type은 궁정/길거리 semantic enum이 아니다. 같은 type에 궁전·저택·거리·산길·야영지가
  섞이므로 304개 장면을 asset/location 기준으로 다시 분류해야 정확한 궁정/길거리 분모가 나온다.

### 2.2 강한 실행 검증

- 자연 full-battle 통과: **4/58**
  - `S_00`, `S_01`, `S_22`, `S_52`
- `S_57`: 아직 실패. v22는 `ENEMY_VICTORY`, 5,810 frames이며 첫 방 leader 162가 HP 9에서
  살아남고 `setUnitStatus` authored gate가 0회였다.
- campaign production route:
  - `Title -> R_00 -> S_00 -> R_01`은 통과 증거가 있다.
  - `R_01 -> S_01 -> R_02`는 구성 요소와 S01 standalone은 통과했지만 최신 전체 route 재검증이
    필요하다.
  - 엔딩까지 연속 실행한 증거는 없다.
- 대화/이벤트:
  - `R_00` 완주 검증
  - `R_01` 부분 검증
  - 길거리 강한 렌더/동작 검증은 `R_00.scene1` 영천전투 전 장면 1개
  - 궁정 전체 장면의 강한 완주 검증은 아직 0개다. 개별 hall UI fixture와 render event 검증을
    전체 장면 완주로 세지 않는다.

### 2.3 최신 빌드 증거

마지막 통합 빌드:

```text
:core:test --tests BattleInteractiveInputTest
           --tests BattleCommandFlowTest
           --tests BattleUiAssetsTest
+ :desktop:installDist
BUILD SUCCESSFUL in 23s, 145 actionable tasks
```

이 빌드 뒤 S57 v22가 실패했고, 그 원인을 반영한 **v23 후보 코드는 구현됐지만 아직 Gradle과
실행 검증 전**이다. 다음 담당자는 반드시 7장의 첫 명령부터 실행해야 한다.

## 3. 지금까지 한 핵심 작업

### 3.1 전투 상태/연출 순서

- 원본 4 camp 순서 `MINE -> FRIEND -> ENEMY -> REINFORCEMENTS` 복원
- 이동은 move-complete, 피해는 hit, 피격/사망은 reaction/death-complete 뒤에 상태를 커밋하도록
  정렬
- 적 턴을 즉시 상태 변경으로 처리하지 않고 실제 이동/공격 presentation 뒤에 확정
- 공격/피격/사망, 반격/추격, 카메라 focus, fire/object animation의 원본 callback 경계 다수 복원
- S00 최종 AI actor 뒤에 남던 `pendingAiUnitDeathScriptPass == 2`를 정리해 승리 결과 전환 정지 해결
- 결과 scene 관측과 loss prompt 실제 입력/Title 복귀 및 trace flush 추가

### 3.2 검증된 전투

- S22 source v13 ↔ game v41 camp boundary 114개 exact, mismatch 0
  - `build/reports/s22-source-v13-game-v41-camp-boundaries.json`
- S52 authored prefix/objects/center/setUnitStatus 요구 통과
  - `build/game-full-battle-late-v12/manifest.json`
- S01 v2 round-1 정지 원인 수정
  - standalone `manualMoveAttemptLimit=0`가 S01의 실제 Mine 입력까지 막고 있었다.
  - `productionManualMoveAllowed()`로 S01의 실제 UI-driven 수동 턴만 허용
  - 수정 후 `build/game-full-battle-fixes-v21`에서 3,301 frames, natural battle-end 통과

### 3.3 S57 반복 분석과 최신 미검증 수정

v20:

- 1,251회 select/move 후 모두 WAIT, player attack command 0회
- 원인: guard 제거 뒤 한 번의 이동으로 leader staging에 닿아야 한다는 지나치게 엄격한 조건
- 2-move counterfactual route probe로 수정

v21:

- 실제 attack UI 입력 발생, leader 162 HP `132 -> 9`
- 마지막 escort 사망 뒤 source0가 rear/WAIT에 남아 leader를 마무리하지 못함

v22:

- critical finisher를 넣었지만 leader HP 흐름이 v21과 동일하고 source0가 더 빨리 사망
- `build/reports/S_57-v22-summary-compact.json`
- `build/game-full-battle-s57-v22/manifest.json`

현재 v23 후보 변경, **미검증**:

- `Battle.previewPhysicalDamage()` read-only 계산 추가
- source0 예상 물리 피해가 focused leader HP 이상이고, 이번 턴 합법 leader 공격 타일까지 도달할
  수 있을 때만 finisher 활성화
- finisher 활성 중 source0를 sole/top candidate로 고정하고 guard fallback을 막아 leader만 공격
- 관련 파일:
  - `core/src/main/kotlin/com/jojo/game/Battle.kt`
  - `core/src/main/kotlin/com/jojo/game/BattleScreen.kt`
  - `core/src/test/kotlin/com/jojo/game/BattleInteractiveInputTest.kt`

원본 S57의 중요한 계약:

- 첫 방 leader 165/162/169가 실제 death callback을 완료해야 vars56/57/58이 설정된다.
- 각 leader 사망 시 해당 guard ring이 hide된다.
- round>=4 guard re-show는 vars70으로 보호되는 one-shot이다.
- 세 leader가 모두 사망해야 다음 방이 열리고, 그 전에는 second-room attrition/gate를 시도하면 안 된다.

### 3.4 로그 처리 도구

원시 battle trace를 모델 context에 직접 넣지 않도록 streaming 요약기를 만들었다.

- `tools/summarize_battle_trace.py`
- `tools/test_summarize_battle_trace.py`
- cumulative `frame.actions`는 매 frame 전체를 재집계하지 않고 새 suffix만 소비한다.
- `driverInputMarkers`는 input marker/frame count이며 실제 공격 성공을 뜻하지 않는다고 분리한다.
- leader HP, deaths, revivals, player counts, terminal, authored sequence를 compact JSON으로 낸다.

사용 예:

```bash
python3 tools/summarize_battle_trace.py \
  --trace build/game-full-battle-s57-v22/traces/S_57.json \
  --scenario S_57 \
  --leaders 162,169,165 \
  --output build/reports/S_57-v22-summary-compact.json
```

### 3.5 다이얼로그/UI 감사 및 반영

구현 후 마지막 빌드에서 통과한 항목:

- CommandLayer의 원본 command1..6 UUID를 native SpriteFrame까지 추적하여 아이콘 복원
- 여섯 120×120 버튼과 cancel 181.9×50, dual img0/img1, disabled gray/label 색 복원
- MagickList의 실제 magic icon, 현재/최대 MP, Mark_1/Mark_2 fill 복원
- Magic 상세의 Magic/Hitarea/Effarea 이미지 복원
- 원본 dynamic asset family export
  - magic 49개, hitarea 33개, effarea 12개
- 전투 하단 작은 HP bar는 원본 44×3의 2배인 현 88×6이 맞아 변경하지 않았다.

구현됐지만 아직 production renderer에 연결되지 않은 groundwork:

- Terrain outer `box1`, title `bg1` exact atlas export 및 `TerrainLayerChromeRenderContract`
- Mine/Other 정보 패널 native frames 및 `SettlementInfoRenderContract`
- 관련 파일:
  - `tools/export_map_assets.py`
  - `core/src/main/kotlin/com/jojo/game/TerrainLayerChromeRenderContract.kt`
  - `core/src/main/kotlin/com/jojo/game/SettlementInfoRenderContract.kt`

확정된 UI 결함:

1. 실제 승리 save MsgBox가 source 635×296 panel/logo/버튼 대신 840×300/680×230 hand-drawn
   win-condition substitute를 사용한다.
2. Mine/Other 실제 정보 패널이 generic box/progress로 대체되어 374×24 bars와 장비 marker가 없다.
3. Terrain production draw가 outer frame과 title strip을 누락한다.
4. MsgBox3는 상태 로직만 있고 533.7×228.4 입력 dialog의 실제 draw path가 없다.

중요한 정정:

- `WinConditionsLayer` 원본은 framed panel이 아니라 `default_sprite_splash + RichText 2개`다.
  WinConBox asset을 연결하면 오히려 잘못이다.
- `choice-panel.png`는 DialogueLayer `U_select_10`으로 확인되었고 ChooseLayer 자산이라는 증거가
  없다. 이름이 비슷하다는 이유로 연결하지 않는다.

감사 자료:

- `build/reports/dialog-ui-static-audit.md`
- `build/reports/battle-command-hud-source-manifest.md`
- `build/reports/dialog-ui-source-manifest.md`
- `build/reports/remaining-dialog-ui-audit.md`
- `build/reports/result-msgbox-source-manifest.md`
- `build/reports/command-icon-uuid-map.md`

## 4. 가장 중요한 교훈

### 4.1 게임 내부 일치와 원본 일치는 다르다

게임 모델과 게임 테스트가 서로 green이어도 원본 callback/asset/geometry와 다르면 실패다.
항상 원본 recovered JS, prefab JSON, decompiled Python을 권위로 둔다.

### 4.2 상태 결과보다 시간축을 먼저 비교한다

최종 좌표와 HP만 같아도 이동 결과가 먼저 보이고 animation이 나중에 재생될 수 있다.
`intent -> animation start -> hit/move callback -> state commit -> death callback -> script resume`를
각각 로그로 비교해야 한다.

### 4.3 자동 플레이 전략과 원본 엔진 의미를 섞지 않는다

S01/S57의 production trace driver는 실제 UI를 조작하기 위한 test policy일 뿐 원본 gameplay
규칙이 아니다. driver의 생존/target 우선순위를 `Battle` 규칙이나 scenario state에 직접 심으면
안 된다.

### 4.4 asset 존재 검사는 렌더 검증이 아니다

파일이 export돼도 production draw가 generic texture/ShapeRenderer를 사용하면 화면은 깨진다.
반드시 `source prefab node -> UUID/SpriteFrame -> exported PNG -> production draw call` 전체 chain을
확인한다.

### 4.5 compact 로그도 의미를 잘못 읽을 수 있다

`actionAttempts`가 ENEMY만 기록한다고 player 공격이 없다고 단정하면 안 된다. 실제 UI driver
marker, HP delta, target/death도 함께 본다. marker count 역시 touch frame 수일 수 있어 실제 공격
횟수로 단정하지 않는다.

### 4.6 terminal은 성공이 아니다

`battle-end`가 떠도 ENEMY_VICTORY이거나 authored coverage가 부족할 수 있다. manifest의
`passed`, `error`, outcome, required/observed sequence를 모두 확인한다.

### 4.7 원본의 one-shot/guard flag를 보존한다

round 조건만 옮기고 vars guard를 빠뜨리면 증원, 카메라, 대사가 반복된다. 조건식뿐 아니라 flag
설정과 callback 시점을 함께 옮긴다.

## 5. 효율적으로 움직이는 방법

권장 역할 분담:

- Luna: 큰 trace streaming 요약, 정적 인벤토리와 prefab 숫자/UUID 추출
- Terra: compact 로그와 원본 코드 기반의 bounded 원인 분석, 독립 파일/계약 구현
- Sol/통합 담당: 원본 근거 승인, 공유 파일 충돌 관리, 단일 Gradle 빌드, 실행 판정

병렬화 원칙:

1. 원본 분석, trace 요약, 서로 다른 파일의 render-contract 작성은 병렬화한다.
2. `BattleScreen.kt`, `tools/export_map_assets.py`처럼 공유 hot file은 동시에 편집하지 않는다.
3. hot file 편집 전 다른 에이전트에게 audit/report 또는 별도 model/test만 맡긴다.
4. 모든 agent 편집이 끝난 뒤 통합 담당만 Gradle을 한 번 실행한다.
5. 실패 trace는 Luna가 compact로 줄이고, Terra는 compact+현재 코드만 분석한다.
6. 한 번에 하나의 첫 불일치를 고치고 동일 seed로 다시 실행한다.

각 반복의 가장 효율적인 순서:

```text
source contract 추출
  -> game/reference log의 첫 불일치 식별
  -> pure/read-only helper와 작은 test 작성
  -> production path에 연결
  -> targeted Gradle test + installDist 한 번
  -> S/R scenario를 --jobs로 병렬 실행
  -> manifest 판정
  -> 실패 trace streaming compact 요약
```

UI 작업 순서:

```text
prefab node/geometry 확인
  -> UUID/SpriteFrame/native rect 추적
  -> exact PNG export
  -> render-contract model/test
  -> production draw 연결
  -> render-event 로그 비교
  -> 전체 논리 완료 후 screenshot 비교
```

## 6. 하면 비효율적이거나 위험한 일

- 수십~수백 MB 원시 JSON trace를 `cat`, 전체 `jq`, 모델 context로 직접 읽기
- cumulative action array를 매 frame 다시 펼쳐 수 GB 보고서 만들기
- 매 작은 patch마다 전체 `desktop:check` 또는 screenshot suite 실행
- 여러 Gradle/게임 프로세스를 같은 build tree에서 동시에 실행
- 여러 agent가 `BattleScreen.kt`나 exporter를 동시에 편집
- 실패 원인을 모른 채 AI level/stat를 올리거나 state를 직접 변경해 승리시키기
- production 경로 대신 fixture/raw captured framebuffer만 고쳐 화면을 맞추기
- asset 이름을 추측해 비슷한 PNG 연결
- 오래된 README의 `완료` 문구나 stale manifest를 최신 증거로 사용
- `git status`/`git diff`에 의존: 이 작업 공간은 Git repository가 아니다.
- S57처럼 긴 전투 실패 때 매번 screenshot을 찍기: 먼저 로그로 논리를 닫는다.

## 7. 다음 담당자가 즉시 할 일

### 7.1 첫 번째: 최신 변경 컴파일과 S57 v23 검증

다른 agent가 Kotlin을 편집 중이지 않은지 확인한 뒤 실행한다.

```bash
cd /Users/ain/workspace/jojo

./gradlew --no-daemon --console=plain \
  :core:test \
  --tests com.jojo.game.BattleInteractiveInputTest \
  --tests com.jojo.game.BattleCommandFlowTest \
  --tests com.jojo.game.BattleUiAssetsTest \
  --tests com.jojo.game.TerrainLayerTest \
  --tests com.jojo.game.MineUnitInfoLayerTest \
  --tests com.jojo.game.OtherUnitInfoLayerTest \
  :desktop:installDist

python3 tools/run_game_full_battle_batch.py \
  --scenario S_57 \
  --output build/game-full-battle-s57-v23/manifest.json \
  --runner desktop/build/install/desktop/bin/desktop \
  --time-scale 16 \
  --max-sim-seconds 1800 \
  --timeout-seconds 900 \
  --seed 1000 \
  --math-seed 305419896 \
  --jobs 1
```

통과하지 않으면 원시 trace를 직접 읽지 말고:

```bash
python3 tools/summarize_battle_trace.py \
  --trace build/game-full-battle-s57-v23/traces/S_57.json \
  --scenario S_57 \
  --leaders 162,169,165,166,167,168 \
  --output build/reports/S_57-v23-summary-compact.json
```

확인할 것:

- 162가 HP 9에서 실제 death/hide callback까지 가는가
- 다음 focus가 169/165로 이동하는가
- vars56/57/58 뒤 door/object removal과 second-room reveal 순서가 원본과 같은가
- final authored center 9회와 setUnitStatus branch가 canonical sequence와 같은가

### 7.2 두 번째: 확정 UI P0를 production renderer에 연결

순서:

1. Terrain renderer가 `TerrainLayerChromeRenderContract`의 outer/title/inner 순서 사용
2. Mine/Other renderer가 `SettlementInfoRenderContract`의 서로 다른 panel/bar/icon 구조 사용
3. 실제 victory save prompt를 `result-msgbox-source-manifest.md`의 635×296 MsgBox로 교체
4. MsgBox3의 실제 533.7×228.4 edit dialog draw/input route 구현

각 단계는 source render-event 로그와 production draw-command 로그를 먼저 비교한다. 최종 screenshot은
아직 실행하지 않는다.

### 7.3 세 번째: campaign을 R02 이후로 확장

S57만 붙들고 전체 route를 미루지 말고, S57 담당과 campaign checkpoint 담당을 분리한다.

1. 최신 `Title -> R00 -> S00 -> R01 -> S01 -> R02:1` 재실행
2. 성공하면 `R03:1`, `R04:1`처럼 stop point를 한 단계씩 확장
3. 각 새 S battle은 isolated full-battle로 먼저 재현하고 natural terminal을 확보
4. 전투 통과 후 campaign edge, reward/save prompt, 다음 R stage를 검증기에 고정
5. choice/random route는 seed와 선택 script를 manifest에 명시

검증 도구:

- `tools/run_game_campaign_checkpoint_batch.py`
- `tools/verify_campaign_route_e2e.py`
- `tools/run_game_full_battle_batch.py`
- `tools/verify_full_battle_trace_order.py`

### 7.4 네 번째: 궁정/길거리 장면 분류와 검증률 관리

`R_*`의 304 scene 함수를 다음 데이터로 분류하는 script/report를 만든다.

- `stage.loadBg(type, variant)`가 실제 가리키는 Mmap/Pmap asset
- stage/event name
- path grid 존재 여부
- 첫/중간 background transition

분류는 `court/interior`, `street/outdoor`, `special-CG`, `inherit/unknown`으로 만들고 unknown을 임의
분류하지 않는다. 그 뒤 각 scene에 `AST`, `logic-log`, `production-route`, `render-log`,
`screenshot-final`의 독립 상태를 기록한다.

### 7.5 마지막: 화면 캡처 비교

모든 논리 route와 render-event contract가 통과한 후 다음 순서로 진행한다.

1. 기본 UI만
2. 초상화
3. 화자명
4. 대사 본문
5. 배경
6. 캐릭터
7. 전투 animation key frame

각 단계에서 위치/크기/alpha/blend/z-order를 먼저 비교하고 글자 raster/GPU sampling 잔차는 마지막에
분리한다.

## 8. 상태 보고 시 지켜야 할 표현

- "구현됨": 코드는 작성됐지만 아직 실행 검증 전일 수 있다.
- "단위 검증됨": focused tests만 통과했다.
- "isolated battle 통과": 해당 S battle만 natural terminal과 authored coverage를 통과했다.
- "campaign edge 통과": 실제 이전 R부터 다음 R까지 production input으로 연결됐다.
- "시각 검증됨": fresh reference/game render 비교까지 통과했다.
- "완료": 처음부터 엔딩까지 위 모든 gate가 통과한 경우에만 사용한다.

현재 전체 상태는 **완료 아님**이다.
