# 영천전투 검증 기록

2026-09-20: 사용자 요청에 따라 S_00 영천전투로 진행 범위를 옮겼다.
우선순위는 진행 중단/입력 → 캐릭터 이동·공격·피격·사망과 카메라 → 기능 UI → UI 표현 → 미세한 글자 차이다.

## 우선순위와 현재 근거

| 우선순위 | 항목 | 확인 상태 |
| --- | --- | --- |
| P0 | 행동 정산 중 턴 종료가 새 정산을 시작해 예외 발생 | 정상 속도와 8배속 실제 입력 실행 모두 2턴에 재현. controller와 확인창에 정산 완료 조건이 빠진 입력 경로를 수정. 실제 재실행에서 기존 중단 지점을 통과하고 3턴까지 진행. |
| P1 | 초반 피격 자세 유지 | unit235 정상 속도 비교에서 차이를 확인하고 scripted32의 마지막 자세 유지, 다음 명령 전환을 수정·검증. |
| P1 | 플레이어 선택 후 이동·공격 입력과 명령 UI | 정상 속도 기록의 선택 해제는 공격 성공 때문이었다: 135.990초 enemy-11 HP 97→55, mine-0 행동 완료. 이동 실패 의심은 철회. 별도 실제 이동/공격 UI 연속 진행은 계속 확인한다. |
| P1 | 등장·이동·공격·피격·사망과 카메라 | 원본/포트의 초반 연출, 유비·관우·장비 등장과 2턴 진입 확인. 이동 중 카메라 급이동 의심은 현재 기록에서 재현되지 않아 철회. 이동 완료 후 카메라 전환 시점 차이는 호출 경로 확인 전까지 후보로만 유지. |
| P2 | 정산 HP/MP 막대의 계단형 색상 | 원본 Linear 필터에 맞춰 정산 자원의 기본 Nearest를 수정. 정상 속도 재캡처에서 매끄러운 색상 변화 확인. |
| P3 | 글자의 사소한 차이 | 기존 오프닝 1 LSB 차이 등은 보류. |

## 실행 근거와 한계

- 원본 정상 속도: `build/reports/yingchuan-source-screens-20260920-final/`, `build/reports/yingchuan-source-screens-extended-20260920/`. 각각 5초/10초 간격의 실제 화면과 `screens.json`. 연속 애니메이션 전체를 증명하는 자료는 아니다.
- 원본 8배속 기능 기록: `build/reports/yingchuan-source-walkthrough-20260920/traces/S_00.json`. 2턴까지 진행. 정상 속도 타이밍 비교에 사용하지 않는다.
- 포트 정상 속도: `verification/build/verification/yingchuan-walkthrough-{30,90,120}sim/`. 120초에서 2턴 스크립트까지 진행. 단순 시간 제한 종료를 멈춤으로 분류하지 않는다. 90초 기록의 AI는 실제로 계속 행동했다.
- 포트 정상 속도 실패: `verification/build/verification/yingchuan-walkthrough-normal-failure/`. 135.557초 PLAYER_INPUT, 137.924초 종료 확인창, 약 138.34초 확인 입력에서 같은 정산 중복 예외. 가속 전용 오류가 아니다.
- 포트 8배속 실패: `verification/build/verification/yingchuan-walkthrough-functional-failure/yingchuan-walkthrough.json`. 1172프레임, 약 19.74초 실제 시간. PLAYER_INPUT → 선택 → 메뉴 → 종료 확인에서 `overlapping BattleScreen._jiesuan presentations`. 예외 전 캡처는 보존했으나 전체 전투 trace는 미완성이다.
- 실행은 S_00 직접 전투 진입과 실제 입력을 사용한다. R_00부터 캠페인 전체를 통과한 증거나 전투 승리 증거가 아니다. 초기 포트 기록은 기존 작업 트리의 미커밋 조작기를 사용했다. 아래 최종 전용 조작기는 해당 도구에 의존하지 않는다.
- 원본 화면 도구는 `node tools/capture_yingchuan_source_walkthrough.cjs ../jojo_mobile/sgccz-desktop OUTPUT 30000`으로 실행한다. 원본 파일을 수정하지 않으며 종료 시 프로세스를 정리한다. `source-full-trace.json`은 조기 종료 때문에 없을 수 있으므로 화면 도구의 필수 산출물로 간주하지 않는다.

각 수정은 해당 구간을 재실행하고 작업 단위로 커밋·push한다. 전투 전체 일치 여부는 아직 미확인이다.

## 정산 중 턴 종료 수정

- `BattleTurnController.canEndPlayerTurn`이 스크립트·행동 연출·정산·후속 콜백 완료 여부를 공통 검사한다. 수동 종료, 위임, Runtime EndTurn 모두 적용한다.
- 확인창 OK는 overlay/설정/위임 상태 변경 전에 검사하며 Cancel은 허용한다. 정산 중복 예외 검사는 유지한다.
- 실제 local settlement를 사용한 수동/위임 회귀 2개와 기존 턴/확인창 테스트를 포함한 20개 테스트 통과.
- `verification/build/verification/yingchuan-walkthrough-functional-fixed/`: 8배속 600시뮬레이션초 실행에서 기존 중단 지점 통과, 다음 진영들과 3턴 PLAYER_INPUT 및 실제 이동 `0:10,5->10,8`의 COMMAND 단계 확인. 설정한 시간 제한으로 끝났으며 전투 승리는 아직 확인하지 않았다.

## 정산 UI 텍스처 필터 수정

- 원본 `cocos-engine-web/cocos2d/core/assets/CCTexture2D.js`의 min/mag Linear 기본값에 맞춰 `BattleSettlementInfoAssets` 캐시 텍스처에 Linear를 지정했다. 막대 크기나 HP/MP 값은 바꾸지 않았다.
- 정상 속도 30초 재실행 성공: `verification/build/verification/yingchuan-walkthrough-linear-bars/`. 25초 정산창을 원본 화면과 비교해 거친 색상 단계가 사라진 것을 확인했다. 해당 화면의 HP 수치와 애니메이션 시점은 서로 달라 전체 픽셀 일치 자료로 취급하지 않는다.

## 전용 입력 조작기로 전투 진행

- `captureYingchuanWalkthrough`는 기본 정상 속도 30초, 최대 실행 시간을 지정하는 bounded task다. `-Pjojo.yingchuanWalkthrough.maxSimSeconds=600 -Pjojo.yingchuanWalkthrough.timeScale=8`로 기능 검증한다.
- 전용 `YingchuanWalkthroughDriver`는 기존 `BattleRuntimeProbe.screenPoint`와 실제 InputProcessor를 사용한다. 이동 후 COMMAND 창의 대기 입력을 추가해 검증 조작기의 멈춤을 해결했다. 실제 입력 시도와 시뮬레이션 시간을 별도 journal로 남긴다.
- `verification/build/verification/yingchuan-walkthrough-dedicated-driver-final/`: 8배속에서 **7턴 PLAYER_VICTORY**, full trace 종료 사유 `battle-end`. 174개 입력 시도와 12개 의미별 화면 기록을 확보했다. 이는 기능 진행 증거이며 정상 속도 애니메이션 전체 일치 증거는 아니다.
- 전용 도구는 `ManualBattleDriver`, 미커밋 parser 옵션 및 tileScreenPoint 추가 필드에 의존하지 않는다. 기존 게임 작업 트리에서 실행한 기록이므로 다른 미커밋 게임 변경을 포함한 상태라는 점은 유지한다.

## 초반 피격 자세 차이

- 원본 정상 속도 `build/reports/yingchuan-source-235-hit-hold-20260920/`: unit235 (7,16), anime32_3 종료 후 SpriteFrame rect `[1268,153,48,48]`를 다음 명시적 action 전까지 유지한다.
- 포트 수정 전 정상 속도 `verification/build/verification/yingchuan-walkthrough-normal-hit-hold/`: 3.805945초 같은 유닛이 anime9_3의 부상 자세로 되돌아가 있다. 양쪽 실제 화면으로 차이를 확인했다.
- 원본의 `playAtkAnime`는 비방어 피격32만 자세를 유지하며, 방어26은 이전 방향의 기본 자세로 복귀한다. 이 차이를 구분해 수정했다. 일반 전투의 기존 복귀 정책은 유지한다.

- 수정 후 정상 속도 10초 기록: `verification/build/verification/yingchuan-walkthrough-hit-hold-fixed/`. 3.8초 실제 화면에서 원본과 같은 피격 자세 유지 확인. trace는 2.866166초 anime32_3 → 4.466158초 명시적 anime4_3 → 5.049490초 사망23 → 6.299496초 숨김을 확인한다.
- 새로운 action/clear 뒤 늦게 도착하는 예약은 revision으로 무효화한다. 다음 공격 시작과 방어26 피격 시 이전 유지 자세를 해제해 재노출을 막는다.
- lifecycle, scripted coordinator, 피격 방향, sprite resolver의 관련 29개 테스트 통과. 자세 유지가 스크립트 진행을 막지 않는 것도 정상 속도 실행에서 확인했다.

다음 확인은 일반 전투의 이동·공격·피격·사망 연결과 명령/정산 UI의 정상 속도 동작이다. 7턴 승리 기록은 이번 자세 수정 전 기능 검증이며, 마지막 자세 수정은 해당 초반 10초를 재검증했다. 전투 전체의 정상 속도 시각적 일치는 아직 주장하지 않는다.

## 첫 일반 교전 정상 속도 검증

- 원본 `build/reports/yingchuan-source-first-combat-20260920/`: 실제 공격·피격·반격·정산 4장과 정상 속도 full trace 2,895프레임. 도구가 원본의 45초 제한 종료를 기다려 trace를 보존한다.
- 포트 수정 전 `verification/build/verification/yingchuan-first-normal-combat/`: 첫 FRIEND AI 진입부터 0.3초 간격 12장과 30초 trace.
- 양쪽 모두 210 이동·공격 → 476 피격(97→70) → 476 반격 → 210 피격(119→104) 순서와 방향이 일치한다. 일반 피격32는 이전 방향, 방어26은 피격 방향으로 복귀하므로 scripted 공격 정책과 구분한다.
- 원본 캡처: `node tools/capture_yingchuan_source_walkthrough.cjs ../jojo_mobile/sgccz-desktop OUTPUT 45000 first-normal-combat`.
- 포트 캡처: `./gradlew :verification:captureYingchuanWalkthrough -Pjojo.yingchuanWalkthrough.captureMode=first-normal-combat`. 이 모드는 정상 속도만 허용한다. 캡처 간에 관찰하지 못한 세부 프레임이나 절대 시간 전체 일치를 주장하지 않는다.
- 추가 차이 확인: 원본은 논리 hasActed가 바뀐 뒤 약 1.429초 동안 idle을 유지하고 해당 정산 Default에서 행동 완료 자세39로 바뀐다. 수정 전 포트는 hasActed와 동시에39로 바뀌었다. 표시용 acted 상태를 해당 유닛 Default까지 유지하도록 수정했다. HP·상태이상·논리적 행동 완료 판정은 계속 갱신된다.
- 자세 유지 수정의 첫 재실행에서는 전환까지 2.442초가 걸렸다. OTHER 패널에 표시되지 않는 경험치 5틱×0.2초를 더하는 별도 오류를 찾았으며, 실제 성장 지급을 유지하면서 MINE에만 표시용 경험치 행과 대기를 반영하도록 수정했다.

- 최종 정상 속도 30초 실행: `verification/build/verification/yingchuan-first-normal-combat-final/`. 210은 23.243845초 hasActed=true 후 idle 유지, 24.685398초 해당 Default에서39로 전환했다. 간격 **1.441553초**로 원본 **1.429초**와 한 프레임 이내다. HP104와 공격·반격 순서는 유지됐다.
- 관련 테스트 15개 통과. 다른 유닛의 Default 무영향, 피해에 따른 자세 갱신 유지, 빈 정산 cleanup, OTHER 1.4초/MINE 2.4초 표시 시간 구분을 포함한다. 마지막 검증은 이 첫 교전 구간에 한정하며 전투 전체의 정상 속도 일치를 뜻하지 않는다.


## 후속 아군 교전과 첫 적군 교전

- 정상 속도 211/234 구간: 원본 `build/reports/yingchuan-source-next-actions-20260920/`, 포트 `verification/build/verification/yingchuan-next-normal-actions/`. 211 이동·공격·반격 피격 순서와 HP(475:97→70, 211:119→104)가 일치한다. 211의 hasActed부터 완료 자세39까지 원본 1.439초, 포트 1.450초로 두 번째 유닛에서도 정산 후 자세 전환을 확인했다.
- 234 특수공격48은 MP 소모 없이 476 HP70→41을 만든다. source에서 잠시 기록된 완료 자세44는 이전 SpriteFrame을 유지하는 callback 경계이므로, 포트의 화면상 누락으로 확정하지 않는다.
- 적474 정상 속도 도착 방향 차이: 원본 `build/reports/yingchuan-source-enemy-arrival-20260920/`의 실제 도착 PNG는 (9,17) idle0 방향1. 포트 수정 전 `verification/build/verification/yingchuan-enemy-first-combat-success-2/`는 도착 후 약0.3초 동안 방향2였다가 공격 때1로 바뀐다.
- 같은 포트60초 실행의 첫 시도는 약42.906초에 `move2 needs a start and destination point`로 중단했다. `.../yingchuan-enemy-first-combat-failure-1/`에 부분 manifest와 stack을 보존했으며, 동일한 두 번째 실행은 성공했다. active 이동 중 잘못된 경로가 아니라, 새 script context의 reseed가 path를 지운 뒤 남은 visual20/cursor를 정리하는 분기에서 발생한 예외다.
- 캡처 도구에 `next-normal-actions`, `enemy-first-combat`를 추가했다. 포트의 상세 교전 모드는 정상 배속만 허용한다. 원본 `enemy-arrival-only`는 도착 장면 한 장을 캡처하며, 원본 프로세스의 제한 종료까지 기다려 trace를 보존한다. source/port의 spriteRect는 서로 다른 texture 좌표계이므로 숫자 차이만으로 sprite 오류를 판정하지 않는다.
