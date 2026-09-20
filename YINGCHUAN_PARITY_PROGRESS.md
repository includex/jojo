# 영천전투 검증 기록

2026-09-20: 사용자 요청에 따라 S_00 영천전투로 진행 범위를 옮겼다.
우선순위는 진행 중단/입력 → 캐릭터 이동·공격·피격·사망과 카메라 → 기능 UI → UI 표현 → 미세한 글자 차이다.

## 우선순위와 현재 근거

| 우선순위 | 항목 | 확인 상태 |
| --- | --- | --- |
| P0 | 행동 정산 중 턴 종료가 새 정산을 시작해 예외 발생 | 정상 속도와 8배속 실제 입력 실행 모두 2턴에 재현. controller와 확인창에 정산 완료 조건이 빠진 입력 경로를 수정. 실제 재실행에서 기존 중단 지점을 통과하고 3턴까지 진행. |
| P1 | 초반 피격 자세 유지 | unit235 정상 속도 비교에서 차이를 확인하고 scripted32의 마지막 자세 유지, 다음 명령 전환을 수정·검증. |
| P1 | 플레이어 선택 후 이동·공격 입력과 명령 UI | 정상 속도 기록의 선택 해제는 공격 성공 때문이었다: 135.990초 enemy-11 HP 97→55, mine-0 행동 완료. 이동 실패 의심은 철회. 별도 실제 이동/공격 UI 연속 진행은 계속 확인한다. |
| P1 | 등장·이동·공격·피격·사망과 카메라 | 첫 일반 교전부터 적483까지 정상 속도 동작을 순차 대조 중. 도착 방향, 정산 후 완료 자세 시점, 약한 공격 피해를 수정했다. 임의의 카메라 지연 추가는 근거가 없어 보류. 애니메이션 RGB 누락을 수정해 행동 완료 회색을 실제 재생에서 확인했다. |
| P2 | 정산 UI 표현 | 막대 Linear 필터, HP창 대상 순서, 대상 유닛 기준 창 위치를 수정하고 각각 정상 속도 재생에서 확인했다. 첫477/210 두 창의 배치를 원본과 대조했다. |
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


## 이동 완료 정리와 도착 방향 수정

- 새 스크립트 context가 경로를 비운 경우 완료 분기의 마지막 카메라 샘플링만 건너뛴다. visual20 제거, cursor 제거, 방향 반영은 항상 실행하며 실제 이동 중 경로 검증은 그대로 유지한다. 기존 카메라/시나리오 이동 테스트는 통과했으나 이 Screen 예외를 직접 재현하는 단위 테스트로 분류하지 않는다. 검증 근거는 실제 실패 stack, reseed 호출 경로, Astra 검토와 수정 후 재생이다.
- AI 이동 완료 시 계산 상태를 먼저 커밋하고 마지막 이동 구간 방향을 이후 적용한다. 실제 BattleActionTransaction(after.direction=2)과 BattleUnitMoveTimeline(last.direction=1)을 사용하는 회귀 테스트에서 도착 후 지연 동안1 유지, 공격 시 별도 방향 전환을 검증했다.
- 두 수정 후 정상 속도60초 재실행 성공: `verification/build/verification/yingchuan-enemy-first-combat-fixed/`, full trace3,598프레임, 제한 종료. 474 도착43.977초와44.144초 실제 캡처는 idle0 방향1,44.294초 공격25도 방향1이다. 234 HP16→0 피격32, 사망23, 숨김을 지나477 공격/반격과483 행동까지 진행했다. 기존 간헐적 예외가 이번 재생에서 재발하지 않았으며 전체 정상 속도 전투의 완료/시각적 일치를 의미하지 않는다.


## 후속 적군 정산 순서와 약한 공격 피해

- 477→210 공격/반격의 HP 변화(210:104→83, 477:97→77)는 같지만, 포트는 210의 HP창을 먼저 처리해477 완료 자세가 피격 종료2.884초 뒤로 밀렸다. 원본은1.450초다. 원본 실제 두 패널 `build/reports/yingchuan-source-477-settlement-20260920/`: 첫 창 황건군 HP97→77, 두 번째 창 보병104→83. capture mode `477-settlement-order`는 패널의 실제 labels/value/bar와 node identity를 기록한다.
- 포트 물리 정산은 고정 유닛 목록 대신 실제 physicalPasses에서 공격자 공적→피해 대상의 최초 등장 순서를 보존하도록 수정했다. 반격 중복과 수동 공격 fallback을 포함한3개 순서 테스트 및 기존 operation coordinator 테스트가 통과했다. 이번 변경은 HP/MP가 변한 유닛 순서이며, 성장만 있는 유닛까지 모든 원본 journal 순서가 검증된 것은 아니다.
- 다음483→3 공격은 원본 HP156→155, 포트156→152로 처음 피해가 달랐다. 원본 `BattleUnit.countBaseHarm`에는 기본 피해1의 하한만 있고, 인원수 기반 최소 피해는 모든 배율 뒤 `count_attackHarm`에서 유명 아군 수로 한 번 적용한다. 포트는 기본 피해에도 전체 표시 플레이어 수로 하한을 적용하고 있었다.
- 원본에 없는 기본 피해의 조기 하한을 제거했다. 실제483/3 기본 능력치 회귀와 최종 유명 아군4명/HP400일 때 최소16 유지 테스트가 통과했고 Astra가 원본 산식과 독립 대조했다. 전체 실제 배율을 포함한 최종 피해는 재생으로 별도 확인한다.

- 수정 후 정상 속도60초 재생 `verification/build/verification/yingchuan-477-settlement-fixed/` 성공(3,598프레임, timeout). 실제 PNG03은 첫477 HP창, PNG09는 두 번째210 HP창을 보여준다. 477 hasActed51.314888→완료39 52.764854로1.449966초이며 원본과 같은 약1.45초다. 483 공격은59.064850초 unit3 HP156→155로 실제 피해1을 확인했다. 이전 중단 구간도 통과했다.
- 별도 UI 위치 후보: 첫477 HP창은 카메라/맵이 맞는 실제 화면에서 원본보다 포트가 왼쪽 아래에 있다. 원본/포트의 창 위치 계산을 다음에 확인한다. 작은 글자·font 차이는 계속 후순위다.


## 대상 유닛 기준 정산창 배치

- 원본 `InfoBaseLayer._setpos`는 대상 unit.node 옆에 창을 배치하고 아래 넘침 반전→오른쪽 넘침 반전→위쪽 제한 순서로 보정한다. 포트는 모든 정산창을 고정(736,96)에 그려 첫477 HP창의 위치가 틀렸다.
- 배경 크기는 실제 prefab의 OTHER471×193.5 / MINE471×258이다. root prefab1280×800은 고정 runtime 경계가 아니다. `UIScene._onLoadLayer`가 네 방향 Widget 정렬을 갱신한 뒤 onCreate를 부르므로 실제 viewport 경계를 사용한다.
- 실제 visualTile의 node 중심으로 생성 시 위치를 계산하고, unitId/startedAt별로 보관한다. SpriteBatch 공통 이동으로 배경·막대·아이콘·숫자·이름을 함께 옮기고 finally에서 transform을 복원한다. 작은 글꼴 차이는 이번 수정 범위가 아니다.
- 대상 이동, 아래/오른쪽 반전, MINE/OTHER의 위쪽 제한을 확인하는3개 배치 테스트가 통과했다. Astra가 좌표 변환과 원본 로딩 순서를 독립 검토했다.

- 수정 후 정상 속도60초 `verification/build/verification/yingchuan-477-placement-fixed/` 성공. 첫477 HP창(PNG03)은 원본처럼 유닛 오른쪽 위, 다음210 HP창(PNG09)은 그 유닛 옆 아래로 이동한다. 양쪽 실제 PNG를 대조했다. 이는 두 OTHER 창의 위치 검증이며 전체 글자/막대 애니메이션 프레임의 픽셀 일치 주장이 아니다. MINE 가장자리 공식은 단위 테스트 범위다.
- 다음 캐릭터 후보: 원본 두 번째 정산창 화면에서 행동 완료한477/474가 어두워지지만 포트는 같은 완료 자세에도 정상 밝기로 보인다. 색상 적용 경로를 확인한다.


## 캐릭터 애니메이션 색상 전달

- 원본 action39의 `props.color=8355711(0x7f7f7f)`를 timeline parser가 버려 완료 병사가 정상 밝기로 남았다. RGB를 timeline→UnitSpriteFrame→BattleActorRenderUnit→SpriteBatch 본체 렌더까지 전달하고 기존 alpha는 유지했다. 본체 이후 WHITE 복원으로 HP막대·아이콘에 색이 번지지 않게 한다.
- 원본 `UIFrame.CreateAnime`는 새 clip 첫 색이 없으면 흰색을 넣고, 이후 색 없는 key는 이전 색을 유지한다. 현재 battle 카탈로그의 색상 키는 모두 constant다. 실제39 방향별 회색,40의16틱 녹색→회색,36의중간키상속/흰색전환,0의흰색복귀와 composer RGB전달 테스트가 통과했다. 향후 선형색상 데이터 지원까지 검증한 것으로 확대하지 않는다.
- 정상 속도60초 `verification/build/verification/yingchuan-477-rgb-fixed/` 성공. 두 번째 정산창 PNG09에서 완료한474/477의 어두운 몸체를 원본 PNG와 대조했고, 미행동 병사·HP막대는 기존 색을 유지한다. 정산창 배치와 교전 진행도 유지됐다.


## 첫 적군 턴 종료와 2턴 진입

- 정상 속도90초 원본 `build/reports/yingchuan-source-first-round-end-20260920/`(5,795프레임)과 포트 `verification/build/verification/yingchuan-first-round-end-20260920/`(5,402프레임)는 모두 제한 종료까지 진행했다. 원본 직접 S_00 진입 및 현재 작업 트리 실행이며 전체 캠페인/깨끗한 HEAD 검증은 아니다.
- 마지막484 이동·공격은 unit3 HP155→154, 반격484 HP97→49. 485는 이동만 수행하고,475/476은 제자리 공격·반격으로 각각 HP70→50 /41→21, 대상211/210은104→83 /83→62가 된다. 위치·방향과 이 결과가 양쪽에서 일치한다.
- 첫 round2 프레임의 공통26유닛 x/y/HP/MP/방향/표시 비교 결과는 `round2-boundary-comparison.json`에 보존했다. 유일한 차이는 양쪽 모두 숨겨진 bootstrap unit0의 미지정 y(null 대0)이며, 보이는 유닛의 비교 필드는 모두 같다. 원본79.0641초/포트77.915054초로 진입 절대 시간에는 약1.149초 차이가 있어 전체 프레임 타이밍 일치를 주장하지 않는다.
- 양쪽7장의 캡처와 full trace를 확보했다. 원본은 실제 action clip, 포트는 domain hasActed 커밋을 캡처하므로 같은 공격 프레임으로 간주하지 않는다. round2 시작과 대화 캡처는 같은 대화 구간이며, 별도 턴 배너는 관찰하지 못했다. 90초 종료 시 양쪽 모두2턴 스크립트 도중이므로 플레이어 조작 인계까지 확인한 것은 아니다.
- 캡처 도구 `first-round-end` 모드를 추가했다. 원본은 이 모드만90초 상한을 허용하고 미충족 gate도 partial manifest로 남긴다. 포트는 정상 속도만 허용하고 중복 없는 상태 경계 캡처를 사용한다. 원본 node 구문 검사와 포트 실제90초 실행이 통과했다.
- 다음 우선순위는2턴 화공 연출, 등장 인물 이동과 카메라, 대화 종료 후 조작 인계다. 사소한 글자 차이는 계속 후순위다.

## 2턴 화공·증원·조작 인계 검증

- 원본 정상1배속150초 단일 실행 `build/reports/yingchuan-source-round2-handoff-20260920/`: timeout으로9,753프레임과8장을 보존했다. 화공3개에5묶음24개가 추가된 총27개 위치를 full trace에서 확인했다. 484 동작8,0/258/259 등장과 초기 배치, 승리조건 창을 실제 PNG로 확인했다.
- 원본157 첫 대사는 필드 등장 명령이 아니다. trace에는87.253초 화자157 대사가 있지만 도구의 speaker accessor가null이라 해당 PNG는 누락됐다. partial manifest를 유지하고 재실행하지 않았다. 향후 gate만 실제 대사 문구로 수정했다. 조건창 뒤 camp0 캡처에는 실제 턴 배너가 보이며, 이것만으로 조작 인계를 주장하지 않는다.
- 원본 fire node는local48×48, 부모map scale2로world96×96이다. 포트96타일 렌더와 크기가 맞는다. 반복은 양쪽4프레임·각1/3초다. 개별 비동기 로드 callback의 효과음·카메라 순서까지 같은지는 별도 범위다.
- 스크립트 요청0(10,6),32(9,6)은 적484/483이 점유한다. 원본findEmptyPos와 포트findScriptedDestination 보정 후 실제 도착은0(10,5),32(9,5)다. 258/259는(9,7)/(10,7)로 도착한다. 요청 좌표를 실제 도착의 정답으로 잘못 사용하지 않도록 캡처 gate를 수정했다.
- 포트 첫 실행 `verification/build/verification/yingchuan-round2-handoff-20260920/`은 승리조건 창과PLAYER_INPUT122.478초, 실제선택122.494초까지11장을 저장했지만, 도구의 조기Gdx.app.exit가 full trace 쓰기 경로를 건너뛰어 Gradle 검증이 실패했다. 게임 진행 실패와 구분하며 증거를 보존했다. 조기 종료를 제거하고 설정한150초 정상timeout을 사용하도록 고쳤다.
- 혼란 물음표의 좌우 차이는 고정 위치 오류가 아니다. 원본도 같은 아이콘을 왼쪽/오른쪽 각1/3초로 반복하며 포트 규칙도 같다. 양쪽 승리조건/후속 화면에서 양 위치가 관찰된다. 서로 다른 캡처 시점의 프레임 차이를 버그로 수정하지 않았다.
- 승리조건 창의 글꼴·기준선 차이는 남아 있으며 글자 수정보다 조작/동작 검증을 먼저 진행한다. 현재 검증은 작업 트리 기준이며 전체 전투 또는 깨끗한 HEAD의 완전 일치 주장이 아니다.

- 도구 수정 후 정상150초 재실행 `verification/build/verification/yingchuan-round2-handoff-final-20260920/` 성공: 9,003프레임, timeout,12장. 실제 목적지115.629초, 승리조건115.896초, PLAYER_INPUT122.766초, 실제 유닛 선택122.782초를 보존했다. 원본 node 구문 검사와 포트 compile/실제 실행이 통과했다.
- `build/reports/yingchuan-round2-comparison-20260920/player-boundary.json`: 원본 round2 camp0 첫 상태와 포트 PLAYER_INPUT 첫 상태의 공통26유닛 x/y/HP/MP/방향/표시/AI/AI값은 차이0이다. 카메라는 원본(215.813953,-112),포트(215.813965,-112)로 부동소수점 오차 수준이다. 원본은 아직 턴 배너 경계이며 자동 대화 입력 주기도 다르므로 절대 도달 시간 차이를 애니메이션 오류로 판정하지 않는다. 포트 trace의 mapObjects는 화염을 포함하지 않아 총27개 포트 좌표의 기계적 일치까지 검증한 것은 아니다.
- 마지막 동작 검수에서33 화공 action6의 완료 자세 유지 차이를 확인했다. 원본은 clip 완료 뒤64×64 마지막 자세를 명시적 action0까지 약1.13초 유지하지만 포트는 즉시48×48 대기 자세로 돌아간다. 서로 다른 atlas 좌표 숫자 때문이 아니라 실제 프레임 크기/완료 처리 차이다. 다음 우선 수정은 이 scripted 시전 자세 유지이며, 부대 상태 일치를 전체 동작 일치로 확대하지 않는다.
- 이후 수동 조작 단위는 양쪽 자동 전략 대신 같은 실제 입력으로 조조0 선택→합법 이동 영역 확인→(11,6) 이동→484 공격→반격/정산→입력 복귀다. 양쪽에서 해당 이동/공격이 허용되는지 먼저 확인하며, 한쪽 거부를 다른 행동으로 우회하지 않는다.

## 화공 시전 완료 자세 유지 수정

- 원본 `BattleUnit.setAction2`의 FINISHED는 스크립트를 재개하면서 마지막 프레임을 남긴다. 포트 callback의 자동 defaultAction/visual 해제를 제거했다. 6/25/48의 일시적인 source-action 채널 아래에도 같은 클립을 두어 완료 뒤 마지막 프레임을 계속 표시한다. 일반 scripted 비반복 동작에도 같은 원본 규칙을 적용한다.
- 원본 일반 `setDir`는 defaultAction(dir)이므로 명시적 방향 지시에서 해당 유닛의 일시 동작과 유지 프레임을 함께 해제한다. 원본에서 CONTROL flag 전용인7은 새 해제 처리에서 제외했다. 기존 direction7 모델링 전체 및 action-1 지원까지 고친 것으로 확대하지 않는다. 일반 교전 피격 및 scripted 공격의 방어26 복귀 경로는 변경하지 않았다.
- coordinator/lifecycle 집중 테스트9개와 sprite resolver 테스트가 통과했다. 완료 뒤 프레임 유지, 스크립트 재개, 다음 명시적 action0의 해제를 확인했다.
- 수정 후 정상90초 `verification/build/verification/yingchuan-fire-cast-hold-fixed-20260920/` 성공(5,400프레임, timeout). 33은80.586685초 시전6 시작 후 완료 뒤에도64×64 마지막 프레임을 유지하고82.469986초 명시적0에서 복귀한다. 완료 후 유지 약1.13초가 원본과 맞으며 실제 PNG03에서도 원본 무기 자세를 확인했다. 3도84.136673초 시전→87.053322초 명시적0까지 유지한다. 이후 방향 지시와 대화 진행도 확인했다.
- 이 수정 후 실제 재검증은90초 화공 구간까지다. 이전150초 조작 인계 증거와 구분하며 다음 수동 조작 구간에서 이후 동작도 계속 확인한다.

## 2턴 실제 이동·공격 및 명령창 배치

- 우선순위는 진행 차단, 캐릭터 동작, UI 표현, 사소한 글자 차이 순서다. 실제 입력 계획의 (11,6)은 양쪽 모두 진입 불가인 울타리 지형이다. 합법 목적지 (11,5)로 사례를 명시적으로 변경했다.
- 원본 `build/reports/yingchuan-source-single-player-action-11-5-20260920/` 정상180초 실행: 실제 포인터 입력으로0 선택→(11,5) 이동→공격 버튼→484(10,6) 공격 성공. 484 HP49→7,0 HP123/MP36 유지. 완료 뒤 모든 부대가 행동했으므로 원본은 턴 종료 MsgBox를 표시한다. 기존 도구가 일반 입력 대기를 요구해 manifest는 PARTIAL이며, 후속 읽기 전용 UI 관찰로 정상 확인창을 확인했다. 기존 증거를 PASS로 바꾸지 않는다.
- 포트 `verification/build/verification/yingchuan-single-player-action-11-5-20260920/` 정상180초 실행 성공(10,803프레임,7장). 동일 이동·공격과 피해42를 확인했다. 마지막 `input-ready` 캡처 이름과 달리 실제로는 자동 턴 종료 확인창이 열린 경계이며 자유 입력 복귀를 뜻하지 않는다.
- 재현 도구는 실제 Command 버튼 좌표와 정산 완료 probe를 사용한다. 실패 시 추가 행동 없이 자연 제한 종료까지 trace를 보존한다. 첫 불법 목적지 검증에서 예외가 trace 저장을 막은 도구 문제는 별도로 수정했다.
- 현재 증거는 직접 S_00 진입 및 기존 변경을 포함한 작업 트리 실행이다. 전체 캠페인/전체 전투/깨끗한 HEAD 완전 일치 검증은 아니다.
- 명령창 고정 위치를 원본 InfoBaseLayer 배치 규칙으로 변경했다. 유닛 옆 배치, 우측/하단 넘침 반전, 상단 제한을 적용하고 렌더·클릭 판정·검증 probe에 같은 변위를 사용한다. 하위 행동 창 진입 및 재개 시 배치 캐시를 초기화한다.
- 실제 원본 unit0 중심(1104,656), 화면1488.372×800에서 배경 좌하단(658.8,381.5), 공격 중심(726.4,636.675)을 확인했다. 포트 command-open PNG도 같은 배치이며 실제 공격 클릭이 성공했다. 경계 배치·이동한 버튼 판정·원본 좌표 회귀 테스트와 probe 테스트가 통과했다.
- 남은 UI 차이: 자동 턴 종료는 원본 MsgBox인데 포트는 위임 체크박스가 있는 PROMPT를 재사용한다. 비활성 명령 아이콘도 원본의 완전 회색조 대신 색상 곱셈을 사용한다. 글꼴의 사소한 차이보다 이 동작·표현을 먼저 처리한다.
- 원본 도구 종점 수정 후 `build/reports/yingchuan-source-single-action-terminal-20260920/` 정상180초 재실행 성공:11,480프레임,5장,실제 입력4회. 추가 확인 입력 없이 정산 완료 뒤 authored MsgBox를 정상 종점으로 캡처했다. 자동 확인창은 예/비 두 버튼과 배경 취소이며 숨겨진 무시 라벨은 활성 버튼이 아니다.
- 기본 자세 복귀의 첫 보완은 일반 정산 경계만 처리해 불충분했다. `verification/build/verification/yingchuan-script-default-reset-20260920/` 정상180초(10,803프레임)는 이동·공격 성공이나 camp0에서146/484의4/8 자세가 남고 피격 후에도8이 잠시 재노출된다. 실패 관측을 보존하고 별도 스크립트 상태 정산 및 일반 피격 완료 경계를 보완한다. 시전33/3 자세 유지 자체는 각1.883/2.917초로 유지됐다.
- 두 번째 자세 재검증은 병행 테스트 빌드가 실행 중 게임의 core.jar를 다시 써서39초에 ZIP asset 읽기 오류로 중단됐다. `yingchuan-runtime-build-overlap-failed-20260920/`에 보존한다. 게임 회귀 검증 결과로 사용하지 않으며 이후 빌드/테스트와 실제 실행을 직렬화한다.
- 별도 상태 비교에서 종료 시22개 표시 유닛의 x/y/HP/MP/방향/표시는 일치했다. FRIEND210/211의 AIValue만 원본0,포트16이다. 원본 PLAYER 시작은 같은 편 전체를 초기화하지만 포트는 현재 진영만 초기화한다. 다음 FRIEND 시작에는 포트도 초기화하므로 이번 기록에서 실제 행동 차이까지 입증되지는 않았으며 동작/UI 수정 뒤 확인할 후보로 남긴다.
- 최종 정상180초 단독 실행 `verification/build/verification/yingchuan-pose-and-auto-prompt-fixed-20260920/` 성공:10,802프레임,7장. `build/reports/yingchuan-script-default-reset-20260920/final-checks.json`의6개 검사가 모두 통과했다. camp0에서146/484는0으로 복귀하고,484 일반 피격 종료 직후 낮은HP 기본 자세9로 돌아온다.33/3 시전 자세는 명시적 다음 동작까지 각1.900/2.900초 유지한다.
- 수정 경계는 일반 정산 Default/Refresh, 사각형을 포함한 스크립트 상태 정산 완료, 일반 피격 완료다. 실제 영향 대상을 타이머에 보존하고 resume 전에 기본 자세를 적용하며 이전 반응 callback은 식별자로 차단한다. scripted playAtkAnime32의 종료 자세 유지와26 별도 정책은 보존한다. 범용 renderer default 조회나 camp 전체에 무조건 초기화를 넣지 않았다. coordinator/timeline/lifecycle/hit scheduler 회귀 테스트가 통과했다.
- 마지막 캡처 이름은 `settlement-complete-terminal`로 정정했다. 자동 확인창을 자유 입력 복귀로 오해하지 않도록 하며, 위 최종 실행 역시 확인창에 도달한 한 행동 단위 검증이지 그 이후 턴의 입력 검증은 아니다.

## 자동 턴 종료 확인창 분리

- 모든 플레이어 부대가 행동한 자동 경로는 plain MsgBox를 연다. 수동 메뉴/NOACTION 경로의 MsgBox4는 위임 선택을 유지한다. 자동 확인은 저장된 위임 설정을 적용하거나 변경하지 않으며 토글을 허용하지 않는다.
- 자동 창은 원본 bg0 타일·box3 NinePatch·독수리 로고, 파란 왼쪽 정렬 본문,180×50 버튼을 사용한다. 버튼 중심은 비(644.186,296.285),예(844.186,296.285)다. 판정과 probe가 같은 기하를 사용한다. bg0에도 BlockInputEvents가 있으므로 창 본문은 배경 취소를 차단하고 창 밖은 취소한다. box3 자식만 조회하면 차단 컴포넌트를 놓칠 수 있어 packed prefab의 부모 bg0까지 확인했다.
- 위 최종180초 실행의 마지막 PNG에서 위임 체크박스 제거, 두 버튼의 배치, 얇은 테두리와 본문 색/정렬을 원본 최종 PNG와 대조했다. 글자 모양·줄바꿈 차이는 남아 있고 후순위다. 확인 버튼을 눌러 다음 턴으로 진행하는 실제 입력은 다음 작업 단위다.
- 자동/수동 위임 동작 및 클릭 기하9개 테스트가 통과했다. 실행 이후 추가한 prompt별 확인 중심 probe도 이 테스트에 포함했으며 별도 게임 재실행은 하지 않았다. 기존 미커밋 MsgBox4 글꼴 변경은 이 커밋에 포함하지 않았다.

## 2턴 자동 확인 후 유비 첫 공격 대사

- `round2-followup`는 같은 조조0 이동·공격 뒤 자동 턴 종료의 예를 실제 포인터로 한 번 누르고 다음 진영을 관찰한다. 원본 정상180초 `build/reports/yingchuan-source-round2-followup-20260920/` 11,438프레임, 포트 `verification/build/verification/yingchuan-round2-followup-20260920/` 10,803프레임을 확보했다.
- 양쪽 모두 유비32의 첫 필살 대사 `이것은 만민의 분노입니다!`에서 멈췄다. 카메라 위치와 표시 부대22개의 위치·HP·MP·방향은 일치한다. 유비 AIValue는 원본155, 포트0으로 계획/확정 시점 차이가 남는다. 원본 trace에 공격 대상이 없으므로 이 대사 경계만으로 원본의 다음 대상이나 피해를 단정하지 않는다.
- 다음 검증 모드는 이 대사가 자연스럽게 완성된 뒤 실제 클릭으로 한 번 닫고 첫 AI 행동 완료 또는 후속 대사까지 관찰한다. 완료는 단순 hasActed 전환이 아니라 정산·사망 callback 이후 경계로 판정한다. 포트에 읽기 전용 완료 횟수/배우와 대사 진행 probe를 추가했다.
- 명령창 비활성 아이콘의 원본 material은 RGB 가중치0.2126/0.7152/0.0722의 회색조다. 원본 bg0의 opacity200은 자식 프레임·라벨·아이콘에도 상속된다. 이를 포트에 반영하고 실제 자연 진행 캡처로 재검증한다. 빠른 `battle-command-disabled-fixture` 실행은 명령창이 보이지 않는 이미지를 출력했으므로 UI 검증 증거로 쓰지 않는다.
- 명령창 수정 후 포트 정상180초 `verification/build/verification/yingchuan-round2-first-combat-20260920/`의 command-open PNG를 원본과 대조했다. 비활성 아이템·장비교환·포위공격 아이콘이 회색조로 표시되고 프레임/아이콘/라벨의 투명도를 적용한다. 불투명 내부 픽셀 표본의 최대 색차(RGB 최대-최소)는 원본8/12/5, 포트4/12/4로 배경이 비치는 회색조다. 활성 공격·마법·대기 아이콘은 색상을 유지한다. 글자 크기/줄바꿈과 서로 다른 불꽃 프레임까지 픽셀 일치한 것으로 확대하지 않는다. `029ee88`로 push했다.

## 2턴 유비 첫 필살 공격 검증

- 첫 원본 `yingchuan-source-round2-first-combat-20260920`은 화자 조회를 `constructor.s_lastId`로 잘못 구현해 PARTIAL이다. 실제 게임은 정상 대사에 도달했으며 추가 클릭 없이 자연180초 trace(11,501프레임)를 보존했다. 원본 full-trace와 같은 `_strings`/`_index`의 화자 marker 조회로 도구를 고쳤다. PARTIAL은 이제 CLI에서도 실패 코드로 반환한다.
- 포트 위 정상180초 실행은10,804프레임,11장이고 대사 완성 후 실제 close 입력1회 및 첫 행동 완료가 확인됐다.32가 제자리(9,5)에서484(10,6)를 action21로 공격한다.484는 HP7→0, 피격32→낮은HP 기본9→퇴각23을 거쳐 숨겨지고 다음 actor258로 넘어간다. 이 관측만으로 원본과 같다고 결론내리지 않으며 원본 재실행과 대조한다.
- 이 실행의 `followup-first-ai-critical-clip-21` 이미지는 후속 필살이 아니라 초기 연출 frame110을 잡았다. 후속 필살의 시각 증거로 사용하지 않는다. 도구 캡처 조건을 필수 대사 close 전송 이후+실제 character32+action21/49로 제한했다. 실제 후속 공격은 전체 trace의 frame7784 이후에 기록돼 있다.
- 원본 speaker-fixed 재실행도 PARTIAL이다. 확인창 첫 layout 전의 예 버튼 중심944.186을 보존한 뒤 실제844.186으로 배치된 버튼에 옛 좌표를 클릭해 다음 camp에 진입하지 못했다. 게임 버그로 분류하지 않는다. 최신 geometry가 서로 다른 두 프레임에서 같음을 확인한 뒤 fresh 좌표로 클릭하도록 확인창과 대사 입력을 보완했다. 실패 trace11,457프레임을 보존한다.
- 안정화 후 원본 `build/reports/yingchuan-source-round2-first-combat-stable-input-20260920/` 정상180초 성공:11,768프레임,10장,실제 입력6회. 화자32의 자연 완성 대사를 한 번 닫고 실제 `_srcTarget=32`, `_dscTarget=484`를 확인했다. 필살21_1→484 피격32_3/HP0→기본9→퇴각23→visible=false→다음 actor258의 순서가 포트와 같다. 관측된 공격 시작부터 피격까지 원본0.939초, 포트0.934초다. 서로 다른 캡처 정지/관측 간격 때문에 모든 clip 길이까지 완전히 일치한다고 단정하지 않는다.
- 입력을 더 보내지 않은 전체 trace의 후속 순서도32→258→259→211→210→479→210 반격→480으로 같다. 종료 시 양쪽 round3/camp0이며 표시 부대20개의 x/y/HP/MP/방향/visible 비교 차이는 없다. 이것은 후속 논리 상태 비교이며 모든 후속 UI/애니메이션을 검수한 결과는 아니다.
- 새 우선 문제는 3턴 카메라다. 원본[96,-176],포트[96,-80]으로 한 칸 차이가 남는다. 다음 수정은 이 카메라 이행이며 글자 차이는 계속 후순위다. 비교 자료는 `build/reports/yingchuan-round2-first-combat-20260920/attack-order-and-round3.json`이다.
- AI 완료 counter 회귀 테스트와 verification 컴파일이 통과했다. 현재 실행 증거는 기존 미커밋 변경도 포함한 작업 트리 기준이며 clean HEAD 전체 캠페인 통과로 확대하지 않는다.
- Astra의 clip clock 검수에서 이번 피격/퇴각 길이 회귀는 확인되지 않았다. 포트 첫 피격 관측은 이미 elapsed0.016541이며0.583115 뒤 기본자세로 복귀해 자산 길이14/24초와 맞는다. 원본 퇴각은 첫 row 뒤 elapsed0.1334로 뛰고1.25 다음 프레임에 숨겨져 자산 길이30/24초와 맞는다. wall timestamp 구간 차이를 animation 길이 버그로 오인해 수정하지 않는다.

## 3턴 플레이어 진입 카메라 보정

- 첫 차이는 3턴 PLAYER 진입이다. 양쪽2턴 종료 camera는[96,-80]인데 원본은 PLAYER 진입 시[96,-176]으로 바뀌고 포트는 그대로 남았다. 원본은 진영 상태/사망 처리 및 종료·조작 가능 여부 검사 뒤 `centerUnit(_firstUnit(camp))`를 호출한다. 포트는 AI 진입과 bootstrap에만 이 연결이 있었다.
- `BattleTurnController`의 일반 PLAYER_INPUT 직전에 카메라 보정 callback을 연결한다. 사망/종료/idle-skip 이후이며 기존 `focusFirstCampCameraUnit(PLAYER)`와 ensureVisible을 재사용한다. 강제 중앙정렬이나 clamp 계산 변경이 아니다. 호출 순서/정상1회/AI중복 없음/idle-skip 없음 관련 controller 테스트15개가 통과했고 Astra의 원본 순서 검토도 통과했다.
- 최종 정상180초 `verification/build/verification/yingchuan-round3-camera-fixed-20260920/` 성공:10,805프레임,11장. 3턴 PLAYER_INPUT camera가 원본과 같은[96,-176]이며 표시20개 부대의 x/y/HP/MP/방향/visible 차이가 없다. 자동 확인·필살 대사 실제 close·첫 공격 정산도 다시 통과했다. `final-comparison.json`에 기계 비교를 보존한다.
- 이번 `followup-first-ai-critical-clip-21`은 대사 close frame7782 다음 실제 필살 frame7783을 캡처했다. 초기 clip을 잘못 잡았던 이전 이미지와 구분하며, 원본 필살 PNG의 유비 무기를 든 자세와 대조했다. 이후 진행 대상은 3턴 실제 플레이어 조작이다. 전체 영천전투 완료나 모든 후속 연출의 완전 일치를 선언하지 않는다.

## 3턴 실제 이동·조조 필살 공격

- 이전 goal turn은 명령창 회색조/투명도와 PLAYER 진입 카메라 수정 및 push로 진행됐다. 이번 범위는 조조0(11,5)→(10,5) 실제 이동 후483(9,6) 공격→정산/퇴각→자동 확인창이다. (10,5)는 빈 평원이며 실제 reachable과 enabled ATTACK을 별도로 검증한다. 점유된(10,6)로 우회하지 않는다.
- 첫 원본 정상210초 `build/reports/yingchuan-source-round3-player-action-20260920/`는 실제 입력10회 후 speaker0의 `내 이 기술을 받아라! 이것이 바로 황천지검이다!`에서 멈췄다.13,598프레임을 보존했으며 공격/피해 이전이므로 PARTIAL이다. 대사 도달을 공격 완료로 표시하지 않는다.
- 원본에서 확인한 exact 화자/전체본문과 자연 타이핑 완료를 조건으로 한 번만 닫는 `round3-first-combat` 모드를 추가했다. 기존 blocking 모드는 유지한다. 후속/예상외 대사에는 추가 입력을 보내지 않으며 실제 close 증거가 없으면 완료될 수 없다. 포트 status창 캡처 probe는 정산 lifecycle 활성 여부가 아니라 실제 info/info2 view의 존재를 조회한다.
- 원본 정상210초 `build/reports/yingchuan-source-round3-first-combat-20260920/` 성공:13,581프레임,10장,실제 입력11회. 조조0 이동20→필살21→483 피격32/HP19→0→기본9→조조 경험치 정산→483 퇴각23/숨김→자동 MsgBox의 순서를 확인했다.0 HP123/MP36 유지, 경험치6→30. MineUnitInfoLayer 원본 labels/bars와 경험치 보간값도 기록했다. 마지막 확인창은 누르지 않았다.
- 포트 정상210초 `verification/build/verification/yingchuan-round3-first-combat-20260920/`도 입력11회·캡처10장으로 자동 확인창에 도달했다(12,605프레임). 이것은 입력 시나리오 완료이며 원본 일치 판정이 아니다. 최종 표시 부대 상태는 같지만, 필살21 시작부터 대상483 HP0 및 조조 acted/경험치30이 조기에 반영됐다. 원본은 피격32에서 HP0, 공격 완료에서 acted/경험치를 반영한다. 첫 정산창도 원본 조조 경험치 창과 달리 포트는 황건군 HP19 창이다. 이 동작·UI 차이를 다음 수정 단위로 우선 처리한다.
- 전체 부대 비교에서는 숨겨진157의 능력치 두 항목에 각각+60 차이가 남는다. 현재 표시 부대 결과와 구분해 추적하며 이번 입력 도구 추가를 전체 전투 일치로 확대하지 않는다.

## 3턴 필살 공격 확정 시점·정산 순서 수정

- 수동 필살 대사를 닫은 같은 update에서 animation21을 시작한 뒤 script COMPLETE만 보고 `commitAll`을 호출한 것이 조기 반영 원인이다. full commit은 전투 표현이 끝난 뒤 허용하며, 예약된 피격 `commitVitals`는 그대로 둔다. 실제 transaction을 사용하는 회귀 테스트에서 공격 준비→타격 HP→최종 경험치/행동확정을 분리해 확인했다.
- 원본 정산 관측은 Mine0(frame10371)→Other483(frame10483)이다. 사망한 대상의 상태창을 생략하는 규칙이 아니다. 포트는 vitals 목록을 먼저, 성장만 있는 actor를 뒤에 처리해 순서가 뒤집혔다. 기존 물리 공격의 sourceUnitOrder를 coordinator에 전달하고 두 목록을 같은 유닛 순서로 순회한다. 별도 순서가 없는 기존 진영 정산은 기본 순서를 유지한다. 관련 정책·coordinator 테스트가 통과했다.
- 수정 후 정상210초 `verification/build/verification/yingchuan-round3-commit-order-fixed-20260920/` 성공:12,605프레임·10장. `build/reports/yingchuan-round3-comparison-20260920/boundaries-after.json` 6개 경계 검사 모두 통과(수정 전4개 실패). 공격21 frame10299→HP0/피격32 frame10355→기본자세 및 경험치30/행동확정 frame10390→퇴각23 frame10586→숨김 frame10662→자동 확인창이다. 실제 PNG09는 조조 경험치6/100 창이며 원본의 첫 패널과 같다. 전체 패널 시퀀스는 coordinator 회귀 테스트로 확인했고 런타임 PNG는 첫 패널만 캡처했다.
- 최종 카메라[96,-176] 및 표시 부대 상태가 원본과 같다. 숨김157의 기존 능력치 차이는 그대로 남아 있다. 원본/포트 공격 시작부터 피격까지 관측 간격은0.939/0.933초이며 wall timestamp만으로 모든 animation 길이를 단정하지 않는다. 이번에도 작업 트리 실행이며 전체 영천전투 완료 검증이 아니다.
- 다음 UI 후보: 3턴 명령창 교환 버튼이 원본에서는 비활성, 포트에서는 활성이다. 원본 checkCanSwap은 인접 유닛의 effective camp가 같은지 확인하지만 포트는 아군 및 같은 armId를 검사한다. 경험치창 첫 프레임도 숫자6/100에 비해 포트 bar가 약20%로 보이는 차이가 있어 보간/표시값 동기화를 확인한다. 사소한 글꼴 차이는 계속 후순위다.

## 3턴 명령창 교환·경험치 막대 UI

- 원본 `checkCanSwap`은 `isMine()`과 인접 BU_BING 영역 내 살아있는 동일 effective camp(`type()`)를 요구한다. 무기 종류는 조건이 아니다. 포트의 아군 전체+동일armId 조건을 제거하고 원본 영역1 프로파일과 effective camp를 사용한다. PLAYER 조조와 FRIEND 유비가 같은 무기를 써도 교환은 비활성이다. 같은 PLAYER의 다른 무기, 이탈 상태, 사망한 대상 조건의 회귀 테스트가 통과했다.
- 경험치 표시 숫자는6/100인데 막대가 약20%였던 원인은 max에 경험치 한도 대신 oldExperience+gained=30을 넣은 것이다. 원본 MineUnitInfoLayer의 현재 `expLimit()`처럼 결과 level의 경험치 한도100으로 고쳤다. 이전값/증가량 보간은 유지한다. 일반 증가 및 level 변경 시 scale 테스트2개가 통과했고 Astra의 원본 코드 대조도 통과했다.
- 최종 정상210초 `verification/build/verification/yingchuan-round3-ui-fixed-20260920/` 성공:12,606프레임·10장. PNG03의 교환 아이콘/라벨이 원본처럼 비활성 회색으로 표시된다. PNG09는6/100 숫자와 약6% 막대이며, 같은 행의 분홍 픽셀 길이가 수정 전128에서39로 줄었다(원본46). 작은 캡/테두리 렌더링 차이까지 픽셀 일치한 것으로 확대하지 않는다.
- `boundaries-ui-final.json` 6개 공격·피해·행동확정 검사 모두 통과했고 `comparison-ui-final.json` 최종 부대 차이는 기존 숨김157만 남았다. 마지막 캡처의 autoBattleOverlay는 PROMPT다. driver completionKind는 update 중 prompt가 열리기 전 경계를 읽어 free-player-input으로 남지만 실제 캡처는 자동 확인창이다. 다음 구간의 확인 입력은 반드시 실제 PROMPT와 안정된 버튼 geometry를 기준으로 보낸다.
- 다음 진행 단위는 3턴 자동 확인창의 예를 한 번 누른 뒤 FRIEND의 첫 실제 행동·정산·퇴각 완료 또는 새 대사까지다. actor/target과 새 대사 본문은 원본에서 관측한 뒤 확정하고 예상하지 않은 대사는 닫지 않는다.

## 3턴 종료 후 유비 행동 검증

- 앞선 goal turn은 공격 시점·정산 순서·교환 조건·경험치 막대 수정 및3회 push로 진행됐다. 이번 `round3-followup`은 기존 조조 공격 완료 뒤 실제 자동 확인창 예를1회 누르고 새 대사에는 입력하지 않는다. 원본 AI 시작 기록의 확인 직전 offset과 포트 정산/사망 callback 뒤 완료 counter를 사용한다. 관측 종점과 실제 행동 완료를 구분하며210초 timeout 자체는 성공이 아니다.
- 원본 정상210초 `build/reports/yingchuan-source-round3-followup-20260920/` 성공:13,713프레임·11장·실제입력12회. 새 AI 기록 offset19 이후32→259가 관측됐다.32가(9,5)→(9,8) 이동해480(8,8)을 필살21_3으로 공격, HP97→0/피격32_1→낮은HP기본9→Other480 정산→퇴각23→숨김이다. 유비는 HP149/MP44 유지, 경험치8→16. FRIEND 성장 전용 Mine창은 없다.
- 포트 정상210초 `verification/build/verification/yingchuan-round3-followup-20260920/` 성공:12,606프레임·7장. 확인 입력1회 뒤 friend-0의 실제 행동 완료가 기록됐다. `build/reports/yingchuan-round3-followup-comparison-20260920/comparison.json`의8개 이동·타격·경험치/행동확정·퇴각 검사가 모두 통과했다. 공격 PNG의 유비 자세와 Other480 정산창 종류/위치도 대조했다. target480 숨김 경계 부대 상태 차이는 기존 숨김157 능력치만 남는다.
- source의 다음actor 시작과 port의 이전actor 완료는 정확히 같은 프레임이 아니므로 종점 framebuffer 전체 일치로 확대하지 않는다. 이동 캡처의 source battleTargets에는 이전0→483이 남아 있어 대상을 뜻하지 않으며 공격 캡처의 갱신된32→480을 근거로 사용한다.
- 전체 trace의 이후 자연 진행도 양쪽 모두 speaker146 장보의 `후우후……!`에서 기다린다. 카메라[96,16]와 표시 부대 상태도 일치한다.258 공격→146 방어26/HP105유지→258 중독 기본36 뒤의 반격 후보 대사이며 사망 대사가 아니다. 해당 이후 UI/애니메이션 전체를 검수한 것은 아니고 다음 별도 입력 구간으로 남긴다.
- 우선 수정할 새 UI 차이: source10 정산 화면에서 쓰러진480의 지도상 HP막대는 없지만 port06에서는 가득 찬 주황색 막대가 남는다. 정산 패널의 이전값97/97 보간과 지도상 체력 표시를 구분해 원인을 확인한다. 글자 모양은 계속 후순위다.

## 정산창과 지도 체력 표시 분리

- 쓰러진480의 지도 HP막대가 정산 중 가득 차 보이던 원인은 UnitInfo 시작 시 popup의 이전값→새값을 지도 healthTimeline에도 예약한 결합이었다. 같은 경로는 살아남은 유닛49→7도 이전값으로 되돌릴 수 있다. 최종 수정은 HP0만 숨기는 예외가 아니라 UnitInfo 시작 시 해당 지도 timeline/hold를 해제하여 live HP를 사용하게 하는 것이다. popup InfoBaseValueAnimation은 유지한다.
- BattleHealthPresentation의 living49→7/dead97→0 회귀와 기존 composer/timeline 테스트가 통과했다. 최종 정상240초 `verification/build/verification/yingchuan-round3-counterattack-20260920/` PNG01에서 죽은480의 주황색 지도 체력 막대가 사라졌고 popup97/97은 유지됐다. PNG05에서는258의 지도 막대가 실제24/107 비율인 채 정산창55/107 보간을 표시해 살아 있는 유닛도 확인했다.

## 장보 방어 후 필살 반격 검증

- `round3-counterattack`은240초 상한이며 앞선 실제 입력을 재사용하고 speaker146의 정확한 `후우후……!` 자연 완료 뒤 한 번만 닫는다. 이후 새 대사에는 입력하지 않는다. 원본은 close 직전 진행 actor/AI 시작 offset을, 포트는 실제 완료 counter를 사용하며 필수 대사 입력 없이는 성공할 수 없다.
- 원본 정상240초 `build/reports/yingchuan-source-round3-counterattack-20260920/` 성공:15,591프레임·8장·실제입력13회.146의 물리 필살21_3→258 피격32_1/HP55→24→중독 기본36→정산→현재 actor258 완료→다음210 시작이다.146 HP105/MP47 유지,258 경험치9→25,146 경험치4→8.
- 포트 정상240초도 성공:14,405프레임·6장. 필수 대사 닫기1회와 friend-6(258)의 실제 정산 완료를 확인했다. `build/reports/yingchuan-round3-counterattack-comparison-20260920/comparison.json`의8개 위치·피해·행동확정·최종경험치 검사가 양쪽 모두 통과했다. 이 검사는 모든 중간 경험치 반영 시점까지 같음을 뜻하지 않는다.
- 남은 시점 차이: 원본146 경험치4→8은258 정산창 종료 후, 포트는258 경험치/행동확정과 함께 반영된다. 이번 수치는 레벨업이 없으며 화면 영향은 별도 미검증이다. 다음 수정 후보로 보존한다.
- 원본 screenshot observer의 allowlist가146/147을 누락해 이번 counterAttackCaptured는 false였다. 실제146 공격은 full trace에 정상 기록되어 있다. 다음 실행용 allowlist 보완은 node 검사만 통과했으며 이 실행에 소급 적용하지 않는다. `_srcTarget/_dscTarget`은 반격 때 이전258→146 할당을 유지하므로 현재 반격 actor/target 근거로 쓰지 않는다.
- 이후 무입력 전체 기록은 양쪽 모두 speaker210 `하아……!`에서 기다린다. 카메라[96,368] 및 표시 부대의 최종 위치·HP·MP·방향·행동·성장·상태가 같고, 기존 숨김157 능력치 차이만 남는다.210 대사는 아직 닫지 않았고 이후 구간은 미검증이다.

## 사용자 요청으로 작업 중지 — 2026-09-20

- 사용자 요청에 따라 현재 상태를 `YINGCHUAN_WORK_HANDOFF.md`에 정리하고 작업을 중지한다. 게임 실행과 빌드는 모두 종료됐고 하위 에이전트도 완료 상태다.
- 영천전투 전체 또는 전체 캠페인 완전 일치를 완료했다고 선언하지 않는다. 실행 근거는 기존 미커밋 변경이 포함된 작업 트리 기준이다. 기존 작업 중인 변경은 유지하며 이번 검증 완료 변경과 기록만 분리해서 커밋한다.

## 반격자 경험치 확정 시점 — 내부 차이로 확정 (수정 없음)

- 원본 비압축 복원 소스 `../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/BattleLayer.js`를 근거로 확인했다. 공격 `_attack2(..., ZHUDONG)`와 반격 `_attack2(..., FAN_JI)`가 각각 `:6307 setCharInfoBykey(..., EXP_ADD, ...)`로 같은 `g_charinfo`에 경험치를 쌓고, 행동 전체가 끝난 뒤 `:10616 _jiesuan(r, l.g_charinfo)`이 `index` 순서로 한 번 정산한다. 146의 `:6678 unitAddExp`는 루프가 146 차례에 도달할 때 실행되므로 258 정산창이 닫힌 뒤가 된다. 별도 콜백이나 두 번째 jiesuan이 아니다.
- 포트는 같은 행동의 두 pass를 `BattleActionTransaction.kt:205 commitAll`의 `completionSideEffects`로 한 번에 flush하고, 표시 순서만 `BattleSettlementOperationCoordinator`의 `sourceUnitOrder`로 따로 재현한다. 그래서 146 성장이 258과 같은 commit에 들어간다.
- **화면 영향은 없다.** 원본 `:6752 showOtherunitInfo`는 HP 또는 MP가 변한 경우에만 열리는데 146은 HP105/MP47을 유지한다. `:6646`의 MINE 분기와 `:6753-6772` 안내도 해당하지 않는다. 포트도 `BattleSettlementOperationCoordinator.kt:163-179`에서 경험치 전용 창을 PLAYER로만 제한한다. 양쪽 모두 `Focus(146)`(원본 `centerUnit`)만 낸다. 이번 구간에는 레벨업도 없다.
- 패널에 표시되는 값은 기록된 `SettlementGrowthGrant`/transaction 전후 snapshot에서 오고 렌더 시점의 live 상태를 읽지 않으므로, 확정 시점이 패널 숫자를 바꿀 경로도 찾지 못했다. staged closure의 상대 순서도 258→146으로 원본과 같아 `averageLv()`/`expLimit()` 입력이 동일하다.
- 따라서 **수정하지 않고 내부 전용 순서 차이로 확정**한다. 원본 순서를 문자 그대로 맞추려면 성장 grant 계산과 상태 변이를 분리해야 하는데(`BattleScreen.kt:7153-7154`가 `commitAll` 이후 `consumeActionGrowth`를 전제), 화면 이득 없이 방금 검증한 정산 순서를 깨뜨릴 위험이 크다.
- 향후 반격자의 레벨업이 공격자 패널보다 먼저 보이는 사례가 관측되면 재검토한다. 그때의 최소 형태는 staged effect에 수신 유닛 id를 붙이고 `commitGrowthFor(unitId)`를 정산 루프의 유닛 분기에서 호출하는 것이다. 현재는 그런 사례를 구성하지 못했다.

## 숨김157 능력치 +60 — 내부 차이로 확정 (수정 보류)

- 차이 항목을 확정했다. `growth.abilities`의 defense `131→191`, spirit `198→258`이며 attack61/critical92/morale92와 hp/mp/위치/방향/표시/레벨31/posts29/arm9는 모두 같다. 세 comparison json의 `unitDifferences["157"]`이 모두 같은 값이다.
- 원본 규칙을 복원 소스에서 확인했다. `recovered-js/modules/game-data/Unit.js:824-856 _baseBility`는 `t <= UNIT_ATTR_NAME2.SPR`(ATT/DEF/SPR)에만 `_equips[slot].value()`를 더한다. CRI/MOR에는 더하지 않으며 이는 관측된 차이 항목과 정확히 일치한다. `_equips`는 `equipItem()(:647-685)`으로만 채워지고, 그 호출은 `equipDefaultWeapon()(:162-182)` → `countDefEquip()(:277-320)` 뿐이며, 이는 `core/Model.js:1925-1937 createUnitById`에서만 불린다.
- 게이트는 `battle/BattleLayer.js:3966-3988 _createBattleUnit`의 `if (n = Model.unit(i)) { if (!(camp > BATTLE_CAMP.FRIEND)) break; ... }`다. FRIEND 진영에서 해당 character의 `Unit`이 이미 해석돼 있으면 `equipDefaultWeapon`을 다시 돌리지 않고 그대로 재사용한다. 원본은 기본 장비를 최초 1회만 계산해 기억한다.
- 포트에는 그 기억이 없다. `BattleScenarioAssembler.kt:47`이 `BattleUnitProjector.project()`를 모든 유닛에 대해 매 전투 materialization마다 다시 돌리고, 장비 기록이 없으면 `GameDataCatalogEquipmentDomain.kt:146-161 defaultEquipment()`가 posts/level에서 무장을 합성해 `equipmentBonus()(:114-131)`를 더한다. itemType 20/22/24→defense, 14/16→spirit 매핑이 관측된 +60/+60과 맞는다.
- 다음 가설은 근거로 기각했다. **숨김 유닛 제외**: 같은 전투 내내 숨겨진 334(posts24)는 차이가 없다. **posts29가 장비 불가**: 원본 `posts.bin`/`item.bin`을 포트의 `EncryptedGameDataCodec` 키로 복호해 확인한 결과 posts29의 후보 타입 `[16,17,22,23]`에 실제 가격/값을 가진 아이템이 존재한다. 그리고 **동일한 후보 집합을 갖는 posts30의 146/147은 양쪽이 일치**한다. 즉 합성 알고리즘 자체는 옳으며, 차이는 원본에서 그 1회 계산이 애초에 일어났는지 여부에 달려 있다.
- **화면 영향은 없다.** `S_00.py:897-899`의 `show(); guide(); hide()`에서 `BattleLayer.prototype.guide`는 `recovered-js/modules/battle/BattleLayer.js:948`의 **빈 함수**다. 패널이 전혀 뜨지 않으며 show/hide 사이에 프레임도 없다. 157은 이 전투에서 다시 보이지 않고 싸우지도 않는다.
- 따라서 **이번에는 수정하지 않는다.** 제안된 수정은 최초 계산 결과를 campaign 영속 상태에 기억시키는 것인데, 원본에서 `Model.unit(157)`이 왜 이미 해석돼 있는지는 정적 독해로 증명되지 않았고(세션/세이브 상태), 영속화는 모든 유닛의 능력치 경로에 영향을 주는 변경이다. 화면 이득 0에 회귀 위험이 크다. 157이 이후 스테이지에서 보이거나 전투에 참여하면 그때 재검토한다.

## 3턴 210 필살 공격과 474 격파 — `round3-210`

- 새 `round3-210` 모드는 `round3-counterattack`의 입력을 그대로 재사용하고, 화자210의 `하아……!`가 자연 완성된 뒤 한 번만 닫는다. 이후 추가 입력은 없다. 상한 300초·정상 속도 전용이다. 원본 cjs 6곳, 포트 launcher 31곳, gradle 9곳의 모드 열거를 모두 보완했다.
- 원본 `build/reports/yingchuan-source-round3-210-20260920/` 성공: 19,145프레임·5장·실제 입력14회, `complete:true`. 종점은 frame11795 camp2, `completedActorId:210`이다. 이번 실행으로 이전에 node 검사만 통과했던 **146/147 allowlist 보완이 런타임에서 검증**됐다(`counterAttackCaptured:true`).
- 포트 `verification/build/verification/yingchuan-round3-210-20260920/` 성공: 18,011프레임·7장. `round3Speaker210Phase:COMPLETE`, close 전송과 friend-3 실제 행동 완료를 확인했다.
- `build/reports/yingchuan-round3-210-comparison-20260920/comparison.json`의 14개 검사가 **양쪽 모두 통과**하고 불일치는 0이다. 210은 (10,16)에서 이동 없이 `anime21`로 (9,17)의 474를 공격해 HP39→0, 피격32→낮은HP 기본9→퇴각23→숨김, 210은 HP41/MP11 유지에 경험치45→81이다.
- **다만 타이밍 비교에서 새 결함을 찾았다.** 행동확정→완료자세39가 원본1.6591초, 포트0.0333초다. 공격→타격(0.9259/0.9331)과 타격→행동확정(0.6153/0.5832)은 맞는다. 같은 결함이 `round3-followup`의 32→480 격파에서도 재현된다(원본1.5253초, 포트0.0333초). 대상이 살아남는 기존 구간(477의1.449/1.450, 이전210의1.429/1.441)은 정상이므로 **대상이 죽는 경로만 유지 규칙을 건너뛴다.**
- 부수 차이로 대상의 낮은HP 기본9→퇴각23 간격이 원본1.8332초, 포트1.4497초다. 같은 원인인지는 함께 확인한다.
- 이번 검사들이 통과했다는 것은 위치·피해·성장·순서가 같다는 뜻이며, 위 타이밍 차이가 남아 있으므로 이 구간의 시각적 일치를 주장하지 않는다.

## 격파 시 행동자 완료 자세 유지 수정

- 원본 규칙을 복원 소스에서 확인했다. 완료 자세39는 `recovered-js/modules/battle/BattleUnit.js:2266 defaultAction()`이 `:2302`에서 `isAction()`(XD 상태)일 때 `BATTLE_ACTION.STAND_UP_ACTION`(=39, `BattleConfg.js:104`)로 고르는 값이다. `_jiesuan` 안에서 `defaultAction()`이 불리는 자리는 셋뿐이다. `:6773`(case 10)은 해당 유닛 패널 직후 `HP in O`일 때, `:6804`/`:6872`(case 14/26)는 성장 블록 안인데 `:6798 if (!(R && et > 0)) return`으로 막히며, `:7009-7010`(case 49)은 `e.index` 루프 전체가 끝난 뒤의 일괄 처리다.
- 따라서 격파한 FRIEND 공격자는 HP 변화도 없고 MINE도 아니며 승격도 없으므로 앞의 두 자리에 해당하지 않고, 죽은 대상의 `showOtherunitInfo`가 닫힌 뒤 case 49까지 이전 자세를 유지한다. 퇴각 `unitDeath`는 `_jiesuan`이 반환된 뒤(`:10614-10628`, AI `:9636-9644`) 이어진다.
- 포트는 `BattleSettlementOperationCoordinator`가 성장 흐름의 `DefaultAction`을 진영·승격 여부와 무관하게 냈다. 행동자가 `sourceUnitOrder`의 앞이라 대상 패널보다 먼저 `Effect.Default(actor)`가 나가고 완료 자세가 commit 한 프레임 뒤에 적용됐다. 지연 처리 자체와 case 49에 대응하는 `Effect.Finished` 경로는 이미 올바랐다.
- 수정은 두 가지다. 성장 흐름의 `DefaultAction`을 원본 `:6798`과 같이 PLAYER 진영이면서 실제 승격/능력 상승 단계가 있을 때로 제한하고, 경험치만 있는 비-MINE 유닛에 원본 `:6733-6739`의 `centerUnit` 뒤 0.1초 대기를 복원했다. 포트가 이 대기를 건너뛰고 있었다. 임의의 지연 상수는 넣지 않았다.
- 대상이 살아남는 기존 경로는 영향이 없다. 477(1.449/1.450)과 이전210(1.429/1.441)은 반격 피해를 받으므로 자세가 성장 단계가 아니라 `UnitInfo`→`Default`, 즉 원본 `:6773` case 10 경로에서 나온다. 실제로 승격 연출을 재생하는 MINE 유닛은 성장 흐름의 `Default`를 그대로 유지한다.
- `BattleSettlementOperationCoordinatorTest` 6개가 통과했다. 격파한 공격자가 대상 상태창 전에 완료 자세를 적용하지 않는 것, 살아남은 대상 경로 불변, 승격을 재생한 MINE 유닛의 성장 흐름 유지를 포함한다. `BattleActedAppearanceTest`·`PhysicalSettlementUnitOrderTest`·`BattleSettlementPresentationControllerTest`도 `--rerun-tasks` 전체 재컴파일에서 함께 통과했다.
- 수정 후 정상300초 `verification/build/verification/yingchuan-round3-210-pose-fixed-20260920/` 성공. 행동확정→완료자세39가 **0.0333초에서 1.5665초**가 되어 원본1.6591초와 0.0926초 차이다. 14개 검사와 불일치0은 유지된다.

### 남은 0.09초와 퇴각 순서

- 이 잔차는 원본 캡처의 샘플링 왜곡이 아니다. 해당 구간 88개 기록에서 정체 프레임 두 개가 engine `dt`를 각각 0.1667/0.1001로 함께 보고하며, 누적 dt 1.6494가 wall 1.6591과 일치한다. 중앙값 dt는 0.0167이다. 원본이 실제로 그 시간을 쓴다.
- 포트의 `acted_to_pose39_s`와 `target_lowpose_to_retreat_s`가 둘 다 1.5665로 **같다**. 포트는 완료 자세와 대상 퇴각을 같은 순간에 낸다. 원본은 자세205.0175 뒤 퇴각205.1757로 0.158초 떨어져 있다. 두 차이 모두 `_jiesuan` 꼬리(case 49)에서 `unitDeath` 인계까지의 구간을 가리키며 별도 작업 단위로 확인한다.
- 대조 스크립트에 교차 타이밍 판정(허용0.06초)을 추가했다. 이전 실행은 14개 검사가 모두 통과하면서도 이 1.63초 오차를 놓쳤다. 한쪽씩의 검사만으로는 불충분하다.

## 적 턴 210 피격·반격·사망 — 무입력 자연 진행

- 새 실행이 아니라 위 300초 기록의 종점(camp 전환) 이후를 읽었다. 양쪽 모두 무입력 자연 진행이며 `build/reports/yingchuan-round3-210-death-comparison-20260920/`에 보존한다.
- 순서가 일치한다. 477(11,15) HP77이 210을 공격해 HP41→20 피격32, 210이 `anime25`로 반격하고 477은 `anime26`으로 방어해 HP77을 유지한다. 이어 479(10,15) HP77이 `anime25`로 공격해 210 HP20→0, 저HP 기본9→퇴각23→숨김이다. 210은 (10,16)에서 끝까지 움직이지 않는다.
- 14개 검사가 양쪽 모두 통과하고 불일치는 0이다. 간격도 피격1→반격 -0.049초, 반격→격파공격 -0.017초(3.4초 구간), 격파→피격2 -0.018초, 피격2→저HP자세 -0.034초로 모두 허용 안이다.
- **승인 잔차가 독립 구간에서 재현됐다.** 저HP→퇴각 -0.1631초(앞 구간 승인값 -0.1634), 퇴각→숨김 +0.1294초(승인값 +0.1290)로 각각 0.001초 이내다. 두 값이 잡음이 아니라 체계적인 엔진 수준 차이임을 뒷받침한다.
- 승인 잔차는 대조 스크립트에 기대 델타와 근거를 함께 기록했다. 허용치 안으로 들어와서가 아니라 알려진 값과 맞아서 통과하며, 값이 달라지면 다시 실패한다.
- 원본 f13542에는 반격25 직전 한 프레임(0.016초) 동안 방향3의 `anime9`가 기록되지만 포트에는 없다. 이전 완료 자세44 사례와 같은 callback 경계의 순간 기록으로 보고 화면 누락으로 확정하지 않는다.
- 이 구간은 무입력 진행의 논리·타이밍 비교이며 모든 UI/패널의 시각 검수는 아니다.

### 남은 두 차이의 성격 확정

- 행동확정→완료자세39의 0.09초는 원본이 `OtherUnitInfoLayer` prefab을 인스턴스화하는 한 프레임의 비용이다(`BattleLayer.js:4091`, 원본 f11572가 중앙값0.0167 대비 dt=0.1667 보고). 산식 1.50초에 이 정체 프레임 하나를 더하면 약1.65초로 Σdt 1.6833과 맞는다. 스케줄된 규칙이 아니므로 포트가 재현하지 않는다.
- 완료 자세와 대상 퇴각이 포트에서 같은 순간인 것은 순서 차이다. 원본 `BattleLayer.js:7118-7130`의 `unitDeath` case0은 `getDyingUnits()`/`unitHide` 전에 `run_script`(`:3011-3016`)를 기다리고 `unitHide`(`:7033`)는 `:7048 centerUnit` 뒤 `:7070`에서 퇴각을 시작한다. 포트는 그 script 패스를 `AiPresentationCoordinator.kt:417-421`과 `BattleScreen.kt:5126-5137`에서 정산 **앞**에 돌린다.
- **이번에는 순서를 바꾸지 않는다.** 그 패스는 현재 약0.033초이고 측정 창 안에 있어 옮기면 행동확정→완료자세가 1.5665에서 1.5335로 오히려 멀어진다. `BattleScreen.kt:5133-5135`는 원본 순서를 알고도 앞에 둔 의도적 결정이며 `battleEndedByScript()`가 여기에 걸려 있다. 근거 없는 재배치로 방금 검증한 결과를 되돌리지 않는다. 위치와 근거를 남기고 후보로 보존한다.

## 라운드 전환 날씨 안내 복원

- 라운드 롤오버에서 원본이 3.720초(3턴)/3.600초(4턴)를 쓰는데 포트는 0.02초에 지나갔다. 그 동안 원본 trace에는 카메라·대사·스크립트·맵객체·유닛 변화가 하나도 없고 camp3에 속한 유닛도 없다. 진영 카드가 아니라 별개의 연출이었다.
- 원인은 **날씨 전환**이다. 원본 `BattleLayer.js:10898-10901`이 `addRound()` 뒤 `:10914-10920`에서 `weather()`와 `_countCurrentWeather()`를 비교해 다르면 `_switchWeather`(`:5485`)로 루프를 막고, 이것이 `MenuLayer`에 `{round, max_round, weather, switch_weather, fn}`을 넘긴다. `ui/MenuLayer.js:74-116`의 시퀀스는 `delayTime(1) → 소리 → delayTime(2)` 뒤 제거·콜백이며 크로스페이드는 `fadeOut(2)/fadeIn(2)`다. 결정적 구간은 **3.000초**다.
- S_00은 `S_00.py:21 setGlobalData(20,-2,-1,0,1,1)`로 weatherType 1이라 `[맑음,맑음,맑음,흐림,바람,폭우]`에 offset1, index `(round+1)%6`이다. 2턴 흐림, 3턴 바람, 4턴 폭우로 **모든 롤오버에서 날씨가 바뀐다.** 그래서 매 롤오버가 지연된다.
- 독립 증거: 원본 `screens.json`의 `menu` 플래그가 113.41→116.44초로 3.03초 동안 참이며 camp0 진입(trace 116.33)에 정확히 끝난다.
- 포트 `BattleTurnController`에는 이미 `WEATHER` 단계와 async `presentWeather`/`completeWeatherPresentation`이 있었다. 단락은 표현 쪽이었고 `BattleScreen.kt:3703`이 `eventMessage`만 세우고 같은 프레임에 `true`를 반환했다.
- `WeatherTransitionLayer`를 추가해 원본 시퀀스를 그대로 옮겼다. `SOUND_DELAY_SECONDS=1f`, `HOLD_SECONDS=1f+2f`, `FADE_SECONDS=2f`, `progress=min(round,max)/max`는 모두 `MenuLayer.js`에서 전사했고 소리 인덱스 111/109/110/135/136은 `Config.js:244-248`이다. 조정한 상수는 없다. 안내 중 입력 차단은 원본의 `interactable=false`에 대응한다.
- `WeatherTransitionLayerTest` 3개, `BattleTurnControllerTest` 16개(날씨 미변화 시 미개방 포함), `RoundLayerTest` 2개가 `--rerun-tasks`에서 통과했다.
- 수정 후 정상300초 `verification/build/verification/yingchuan-round3-210-weather-20260920/` 성공. 멈춤 없이 완주했고 `WEATHER` 단계가 3턴 2.99초·4턴 3.02초 돈다. 롤오버 전체는 원본3.720 대비 포트3.01초로 **오차가 3.70초에서 0.71초로 줄었다.** 남은 0.7초는 원본의 비동기 prefab·날씨 텍스처 로딩이며 상수로 메우지 않았다.
- 기존 두 구간 대조가 그대로 통과한다. `round3-210` 14개 검사와 210 사망 구간 14개 검사 모두 양쪽 통과, 불일치0이다. `PLAYER_INPUT` 절대 시각은 롤오버마다 약3초씩 뒤로 밀렸으나 이 프로젝트는 절대 시간이 아니라 실제 행동 경계로 대조하므로 영향이 없고, 방향은 원본 쪽이다.
- **남은 시각 차이:** 그 3초 동안 원본은 MenuLayer 패널(라운드 진행 막대, 전투 이름, 날씨 스프라이트 크로스페이드)을 보여주지만 포트는 아무것도 그리지 않고 지도와 기존 toast만 유지한다. 타이밍은 맞고 그림은 비어 있다. 다음 작업 단위로 처리한다.

## 라운드 전환 날씨 패널 그리기

- 원본 `MenuLayer`의 `switch_weather`는 모달 팝업이 아니라 **전투 화면 하단 바 전체**다. 실제 원본 프레임 `build/reports/yingchuan-source-round3-210-weather-20260920/source-01-round3-210-weather.png`(round2 camp3, wall 113.47초)로 확인했다. 명령 아이콘 13개, 전투명 `영천의 전투`, `턴 수 2 / 20`과 진행 막대, 오른쪽 날씨 그림으로 구성된다.
- 같은 prefab `Battle/scene/MenuLayer`(`assets/resources/import/93/938f52a8-….json`)를 쓰며 플래그는 단추를 모두 비활성화하고 날씨 노드를 하나 더 붙일 뿐이다. `Panel_cancel` 불투명도30 검정 스크림, `bg` 1280×212→1488.372 확대, `progressBar` `_N$totalLength:300` SLICED 좌→우, 라벨 3개 모두 `fontSize 30`·`_styleFlags 1`(굵게)·색 지정 없음(엔진 기본 흰색)이다.
- 날씨 스프라이트는 `Game/Weather/Weather_<n>-1` 216×200을 216×50 네 행으로 6fps 순환한다. `tools/export_map_assets.py:769-818`이 이미 내보내고 있어 새 자산이 필요 없다.
- `button12`는 prefab에서 비활성이고 `cc.Button`에 `_N$transition`이 없어 `interactable=false`는 시각 변화를 만들지 않는다. 실제 프레임에서 단추 영역의 평균 채도비가 `button6`를 포함해 0.56~0.62로 균일해 회색 처리는 일어나지 않음을 확인했다. 그 균일한 0.60은 스크림 불투명도30과 일치한다.
- 첫 구현의 두 결함을 실제 프레임 측정으로 잡았다. **(1) 9-slice**: 원본 진행 판은 밝은 한 줄 뒤 평탄한데 포트는 긴 그라데이션이었다. SpriteFrame 자산의 `capInsets:[1,3,1,3]`(Cocos 순서 left,top,right,bottom)로 세로 3px이며, `title-bar.png`의 3~5행이 동일해 늘리면 평탄해진다. LibGDX `NinePatch` 생성자 순서는 (left,right,top,bottom)로 달라 변환이 필요하다. **(2) 라벨 정렬**: `2 / 20`이 판 밖으로 나갔다. prefab 앵커가 라벨마다 달라 `bg0/label` 중앙193, `progressBar/label` 좌431.853, `progressBar/label0` **우723.131**이다. 고정 좌표 대신 앵커를 쓰면 다른 전투명이나 `20 / 20`에서도 맞는다.
- 수정 후 실제 프레임 대조: 진행 판 세로 표본이 원본 `169,94,86,86,86,86` 대 포트 `169,86,86,86,86,86`, 흰 글자 오른쪽 끝이 **양쪽 1253으로 일치**한다. 영역별 평균 채널차는 아이콘 행1.45(32 초과 0.19%), 날씨 그림9.95, 전투명 판17.36, 턴 판23.97이다. 남은 차이는 판 배경이 아니라 **글리프 모양**에 몰려 있으며 이는 계속 후순위다. 날씨 그림의 차이는 4프레임 순환과 크로스페이드 위상이 두 캡처에서 다른 탓이다.
- `WeatherTransitionLayoutTest` 8개, `WeatherTransitionLayerTest` 3개, `MenuLayerTest` 6개가 통과했다. 그중 하나는 내보낸 `title-bar.png`/`progress-bar.png`를 직접 읽어 3~5행이 한 색이고 2·6행이 다름을 확인해 3px 캡이 실제로 평탄한 띠를 만든다는 것을 검사한다.

### 캡처 도구의 색 프로파일 누락

- `tools/capture_yingchuan_source_walkthrough.cjs`가 Electron에 `--force-color-profile=srgb`를 넘기지 않고 있었다. 이 도구는 영천전투 검증 전체를 구동하므로 **이 날짜 이전 `build/reports/yingchuan-source-*` 프레임으로 내린 색 판정은 모두 재확인 대상**이다. 기록에 남은 비활성 아이콘 회색조 색차 비교도 여기에 해당한다.
- 증상은 순수 파랑에서 R·G가 0에서 떠오르는 것이다. 포트가 `(0,0,86)`으로 그린 판이 원본 캡처에서 `(7,7,75)`로, 회색 바가 `197,197,197` 대 포트 `204,204,204`로 읽혔다. 플래그를 넣은 뒤 같은 픽셀이 양쪽 `(0,0,86)`으로 정확히 일치한다.
- 구조 차이(평탄 대 그라데이션, 글자 범위)는 이 왜곡의 영향을 받지 않으므로 위 두 결함의 판정은 유효하다.

### 승인 잔차를 원본 자체 편차로 대체

- 앞서 승인 잔차로 고정했던 값들이 **원본의 실행 간 편차**였음을 확인했다. 같은 구간을 두 번 실행한 원본이 스스로 `acted_to_pose39_s` 1.6591 대 1.5551(0.104), `target_lowpose_to_retreat_s` 1.8332 대 1.6796(0.154), `target_retreat_to_hidden_s` 1.1006 대 1.1686(0.068)로 어긋난다. 정산 패널 prefab 인스턴스화 프레임의 비용이 실행마다 다르기 때문이다.
- 단일 실행 값을 상수로 박은 것은 노이즈를 맞춘 것이므로 제거하고, 측정된 원본 편차를 허용치로 쓴다. 새 원본 기준 포트의 `acted_to_pose39_s` 델타는 **+0.0115초**로 사실상 일치한다.
- 관문의 감지력은 유지된다. 수정 전 포트 trace를 새 기준으로 다시 돌리면 `acted_to_pose39_s` -1.5218로 명확히 실패한다.
- 사망 구간은 원본 편차(0.05/0.036)보다 포트 델타(0.11/0.094)가 커서 노이즈로 설명되지 않는다. 이를 `openDifferences`로 분리해 크기와 근거를 함께 기록하고, **그 크기로는 통과하되 커지면 실패**하도록 했다. `retreat_to_hidden_s`는 원본의 wall과 dt 측정이 서로 어긋나 어느 쪽이 맞는지 아직 확정하지 못했다. 포트를 wall 값에 맞추지 않는다.
- 따라서 `allPass`는 "동일"이 아니라 "회귀 없음"을 뜻하며 미해결 차이는 항상 드러난다.

### 남긴 관측

- `BattleHudAssets`의 기존 `menuButtonPatch = NinePatch(it, 9, 7, 9, 11)`은 Cocos 배열 `box3 [9,7,9,11]`을 LibGDX의 다른 인자 순서에 그대로 넘긴다. `9, 9, 7, 11`이어야 한다. 공용 메뉴 경로라 이번에 건드리지 않고 기록만 한다.
- `battle-menu` 렌더 패리티 게이트는 선언·배선·재실행이 모두 살아 있으면서도 보증하는 것이 없다. `BattleScreen.kt:9056`이 `drawBattleMenu()`를 부르지 않는 손으로 적은 리터럴 표를 반환하고, `compare_render_logs.py`의 `SEMANTIC_FIELDS`에 색이 없어 검정 대 흰색을 볼 수 없다. `tools/verify_battle_menu_render.py`와 `verify_battle_menu_assets.py`는 저장소 어디에서도 참조되지 않으며, 선언된 freshness manifest는 존재하지 않아 오늘 돌리면 `BLOCKED`가 난다. 메뉴 경로의 검정 라벨 수정은 이 증거 경로를 함께 고치는 별도 작업 단위다.

## 첫 일반 교전 구간 재검증 — 색 정확한 원본과 교차 타이밍

- 기존 초반 검증은 색 프로파일이 빠진 캡처와 한쪽씩의 검사로 이뤄졌다. 두 결함 모두 이번에 찾았으므로 앞 구간부터 다시 확인한다.
- 색 왜곡의 크기를 측정했다. 같은 `first-normal-combat` 원본을 플래그 전후로 떠서 비교하면 네 장 모두 평균 채널차 12.5~14.2다. 특히 `enemy476-reaction`은 최대 50에 평균 12.46으로 구조 차이 없이 **순수 색 편차만 12.46**이다. 기존 픽셀 게이트의 허용치 14.0/12.0과 같은 크기이며, 그 허용치가 이 왜곡을 감싸도록 잡혀 있었다는 기록과 맞는다.
- 새 `build/reports/yingchuan-first-combat-comparison-20260920/compare.py`는 14개 검사와 **6개 교차 타이밍**을 함께 본다. 원본 대 원본 자기검사를 통과한다.
- 원본 `build/reports/yingchuan-source-first-combat-srgb-20260920/`(2,941프레임), 포트 `verification/build/verification/yingchuan-first-combat-labelfix-20260920/`(12장). 순서는 210 (10,17)→(10,16) 이동→공격25→476 피격32 HP97→70→476 반격25→210 피격32 HP119→104→행동확정·경험치0→9→완료자세39다.
- **14개 검사가 양쪽 모두 통과하고 불일치는 0이며 6개 교차 타이밍이 모두 0.031초 이내다.** 이동→공격 +0.025, 공격→타격 -0.015, 타격→반격 -0.030, 반격→반격타격 -0.018, 반격타격→행동확정 +0.001, 행동확정→완료자세 **-0.009**다. 이 구간이 교차 타이밍 판정을 받은 것은 처음이며 초반 작업이 튼튼했음을 뒷받침한다.
- 원본 자체 편차도 다시 확인됐다. 같은 구간의 행동확정→완료자세가 기록의 1.429초와 이번 1.453초로 0.024 다르다. 단일 실행 값을 정답으로 고정하지 않는다.

## 정산 패널 값 라벨의 세로 위치

- 실제 프레임 측정에서 값 숫자가 자기 막대 중심 대비 원본 -17.5, 포트 -26.5로 어긋났다. 글자 높이는 48 대 49로 같아 크기가 아니라 위치 문제였다. 패널이 서로 다른 유닛에 붙어 화면 위치가 달라도 각자의 막대 대비 상대값이라 판정이 성립한다.
- 원본 prefab을 해독했다. `config.a497b.json`의 `paths[206] Battle/scene/OtherUnitInfoLayer`, `paths[330] MineUnitInfoLayer`를 거쳐 `assets/resources/import/60/60e799d9-….json`, `import/dd/dd2699f7-….json`을 읽었다. 값 라벨은 막대 노드의 자식이며 `_trs y=12`, `_contentSize` 67.77×**54.4**, `cc.Label` vAlign CENTER에 `cc.LabelOutline _width=2`다. `_fontSize`/`_lineHeight`는 직렬화돼 있지 않아 `cc.Label` 기본값 40/40이며, 이 값이 저장된 노드 높이 54.4와 50.4를 정확히 재현한다.
- 원인은 `BattleScreen.kt:8372-8404`와 `:8447`이 라벨 y를 `nodeBottom + 42f`로 둔 것이다. **42는 Cocos `_calculateFillTextStartPosition`이 canvas 상단 기준으로 내는 값**인데 포트는 이를 노드 하단 기준으로 썼다. 올바른 값은 `54.4 - 42 = 12.4`이고 그마저 baseline이라 LibGDX `BitmapFont.draw`의 cap line 기준으로 `capHeight`를 더해야 한다. 외곽선이 없는 무기·방어구 행은 노드 높이 50.4라 10.4다.
- `CocosLabelBaseline`이 `ttf.js`의 `_calculateSize`/`_calculateFillTextStartPosition`을 옮긴다. 노드 하단 값들(245.8/191.8/140.8/97.8/294.5/174.55/116.55/226.85)은 원래 맞았으므로 건드리지 않았다. 이름 행과 MP 행도 같은 크기의 같은 결함이며 같은 수정으로 함께 고쳐진다.
- `SettlementInfoLabelBaselineTest` 7개와 기존 `SettlementInfoPlacementTest` 3개가 통과했다. `SettlementInfoRenderContract`는 한 글자도 바뀌지 않아 대상 유닛 기준 패널 배치는 회귀하지 않는다.
- 수정 후 실제 프레임: HP 숫자 오프셋이 -26.5에서 **-20.5**(원본 -18.0), MP가 -23.0에서 **-20.0**(원본 -16.5)이 됐다. 패널 위치는 양쪽 `x=1270, y=884`로 동일하다.
- **남은 약1.5~2 논리px는 글꼴 대체다.** 두 prefab 모두 `_N$fontFamily`를 직렬화하지 않아 원본은 Chromium 기본 Arial로 숫자를 그리고(잉크 0…0.716em) 포트는 Apple SD Gothic Neo를 쓴다(0.030…0.724em). 상수로 메우지 않았고 글꼴 차이는 계속 후순위다. 포트 막대가 원본보다 2px 낮은 것도 함께 남긴다.
- 미커밋 작업 중인 `BattleUnitInfoPopup.kt`의 `LABEL_TEXT_TOP = 42f - 27.2f`에도 같은 결함이 있다. 범위 밖이라 건드리지 않고 기록만 한다.

- 이번 커밋에는 `CocosLabelBaseline`과 그 단위 테스트만 담는다. `BattleScreen`의 baseline 배선은 진행 중인 정산 렌더링 재작업(글꼴 배율, 이름 굵게, 무기·방어구 행 처리)과 같은 hunk에 얽혀 있어 분리하면 남의 미완성 변경을 함께 커밋하게 된다. HEAD의 해당 상수는 `+ 34f`이고 미커밋 작업이 이를 `+ 42f`로 바꿔 둔 상태였으므로, 위 측정 -26.5는 그 작업 트리 기준이다. 배선은 작업 트리에 남기고 재개 시 정산 재작업과 함께 커밋한다.

## 후속 아군 교전과 특수공격 재검증 — `next-normal-actions`

- 원본 `build/reports/yingchuan-source-next-actions-srgb-20260920/`(3,946프레임·8장), 포트 `verification/build/verification/yingchuan-next-actions-60s-20260920/`. 대조는 `build/reports/yingchuan-next-actions-comparison-20260920/`이며 17개 검사와 8개 교차 타이밍을 본다. 원본 대 원본 자기검사를 먼저 통과시킨다.
- 순서는 211 (9,18)→(9,16) 이동→공격25→475 피격32 HP97→70→475 반격25→211 피격32 HP119→104→행동확정·경험치0→9→완료자세39, 이어 234 (12,17)→(10,17) 이동→특수공격48→476 HP70→41이다. 234의 MP는 11로 유지된다.
- **17개 검사가 양쪽 모두 통과하고 불일치는 0이며 8개 교차 타이밍이 모두 0.047초 이내다.** 234 특수공격→타격은 0.849 대 0.850으로 0.001초다.
- **포트 기본 30초가 구간을 잘라 먹는 함정을 찾았다.** 첫 실행은 기본값으로 돌아 211의 행동확정 29.15초 직후 30.02초에 끝났고 완료 자세(약30.6초)와 234 특수공격(33.2초) 전체가 관측 범위 밖이었다. `-Pjojo.yingchuanWalkthrough.maxSimSeconds=60`으로 다시 돌려야 원본 60초 구간을 담는다. 이 모드에는 gradle 쪽 기간 강제가 없다.
- 대조 스크립트가 이를 통과로 세지 않고 예외로 멈춘 점이 중요하다. 관측하지 못한 경계를 조용히 건너뛰면 없는 합격이 만들어진다. 이후 구간 스크립트도 경계를 찾지 못하면 실패하도록 유지한다.
- 원본 자체 편차가 또 확인됐다. 같은 구간의 행동확정→완료자세가 기록의 1.439초와 이번 1.4175초로 0.022 다르다.

## 첫 적군 교전 재검증 — `enemy-first-combat`

- 원본 도구가 이 모드에서 회귀해 있어 새 캡처가 실패했다. 6개 중 `enemy474-move-start`, `enemy474-last-leg`만 관측하고 60초 상한에 걸린다. 과거 `build/reports/yingchuan-source-enemy-first-combat-20260920-final/`은 같은 60초에서 6장을 모두 받았고 그 뒤 게이트 이름이 바뀌었다(`enemy474-attack`→`enemy474-post-attack` 등). 도착 게이트가 여섯 조건의 동시 성립을 8ms 폴링으로 요구하는 경합이다.
- **실패 시 trace가 유실되는 문제도 같이 확인했다.** `await childExit`가 `throw` 뒤에 있어 Electron 자식이 회수되지 않고 `source-full-trace.json`이 쓰이지 않는다. 이 프로젝트가 명시한 "실패해도 trace는 보존한다" 원칙과 어긋나며, 실제로 이번 실패 진단을 훨씬 어렵게 만들었다. 도구 수정은 별도로 진행한다.
- **trace에는 색이 없다.** 색 프로파일 문제는 PNG에만 영향을 주므로 타이밍·상태 대조는 기존 성공 실행의 trace로 지금 할 수 있다. 프레임 이미지 대조만 새 캡처를 기다린다.
- 대조 `build/reports/yingchuan-enemy-first-comparison-20260920/`는 13개 검사와 7개 교차 타이밍을 본다. 원본 대 원본 자기검사를 통과한다. 원본의 완료 자세 `anime39`가 두 묶음(46.007~46.056, 46.540~46.633)으로 기록되고 사이에 clip이 null이라 "첫 등장"을 기준으로 삼지 않고 퇴각 직전의 마지막 등장을 쓴다.
- 순서는 474 (6,16)에서 `anime20` 이동→(9,17) 도착 방향1 idle→공격25→234 HP16→0 피격32→저HP 기본9→퇴각23→숨김이다. 474는 HP97을 유지한다.
- **13개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 이동→도착 -0.005, 도착→공격 +0.024, 공격→타격 -0.016, 타격→저HP자세 -0.017로 모두 허용 안이다. 도착 방향1과 idle도 양쪽에서 확인된다.
- 나머지 둘은 기록된 미해결 차이다. 저HP자세→퇴각 -0.248, 퇴각→숨김 +0.098이다.
- **이 값들의 정밀 일치를 독립 확인으로 해석하면 안 된다.** 이 스크립트의 기대값은 같은 쌍을 손으로 계산해 넣은 것이라 순환이다. 구간별 실측은 저HP자세→퇴각이 -0.11에서 -0.28 사이로 흔들린다(round3-210 -0.113/-0.283, 210 사망 -0.112/-0.163, 이번 -0.248). 원본 자체 편차와 실행 간 차이가 섞여 있으므로 단일 값으로 고정하지 않는다.
- 관측으로 남길 것: 같은 구간에서 원본의 `anime39` 표본이 21개(두 묶음)인데 포트는 76개로 연속이다. trace의 clip 기록 방식 차이일 수 있어 화면 차이로 단정하지 않는다. 별도로 확인한다.

## 정정 — 저HP자세→퇴각 차이는 원본 자체 편차였다

- 원본 캡처 도구를 고친 뒤 같은 구간을 다시 떠서 대조하니 `lowpose_to_retreat_s` 델타가 **-0.248에서 +0.030으로** 바뀌었다. 원본의 같은 지표가 실행 A 2.1812초, 실행 B 1.9034초로 **0.278초** 흔들린다.
- 따라서 이 항목을 `unitDeath` 순서 차이의 증거로 기록한 것은 **틀렸다**. 원본 자체 편차가 관측된 델타보다 크므로 포트 결함을 주장할 수 없다. 앞선 구간들에서 -0.11에서 -0.28 사이로 흔들린 것도 같은 이유로 본다. `BattleLayer.js:7118-7130`의 순서 자체는 코드에서 확인한 사실이지만, 그것이 화면 타이밍 차이를 만든다는 측정 근거는 없다. 가설로 되돌린다.
- 앞선 대조 스크립트가 이 값을 미해결 차이로 "통과"시킨 것도 문제였다. 기대값을 같은 쌍에서 손으로 계산해 넣었으니 순환이었다. 이제 허용치는 **원본을 두 번 실행해 측정한 편차**에서만 온다.
- **반면 `retreat_to_hidden_s`는 실제 차이다.** 원본은 1.1519와 1.1616으로 편차 0.0097의 안정적인 값이고 포트는 1.25로 일관되게 약 +0.09 길다. 세 구간에서 +0.098/+0.088/+0.129로 재현된다. 포트의 1.25는 퇴각 클립 자산 길이 30/24와 정확히 같으므로, **포트는 클립을 끝까지 재생하고 원본은 그보다 먼저 숨긴다**는 뜻이다. 원본의 숨김 규칙을 확인하기 전에는 포트를 wall 값에 맞추지 않는다.
- 교훈으로 남긴다. 한 번의 원본 실행으로 얻은 값을 기대값으로 고정하면 노이즈를 규칙으로 굳힌다. 미해결 차이로 분류하기 전에 **원본을 두 번 이상 실행해 그 지표의 자체 편차부터 재야 한다.**

## 원본 캡처 도구 수정 — 도착 창과 trace 보존

- 실패 원인은 게이트가 아니라 캡처 비용이었다. 2560×1376에서 `Page.captureScreenshot`이 일관되게 약 690ms 걸리고(측정 716/693/685/691), 도착 상태는 frame2726~2742 즉 **293ms**만 유지된다. 이동 시작·마지막 구간·도착 세 장을 1.05초 안에 담아야 하는데 690ms짜리 셋은 물리적으로 들어가지 않는다. 폴링 간격은 원인이 아니었다.
- 게이트 조건은 한 글자도 바꾸지 않았다. `optimizeForSpeed`를 켜고(지원되지 않으면 한 번만 감지해 원래 요청으로 영구 복귀) 폴링을 4ms로 줄였다. PNG는 무손실이라 픽셀과 색 프로파일은 그대로다.
- 재실행 성공: `build/reports/yingchuan-source-enemy-first-srgb2-20260920/` 6장 전부. 도착 캡처가 wall 43.22초로 도착 창 43.057~43.350의 한가운데다. 캡처 간격도 0.24~0.52초로 줄었다.
- **과거 "성공" 실행의 도착 PNG는 실제로 도착이 아니었다.** frame2809, `action=1`, 234는 이미 사망 상태의 공격 후 idle이었다. 게이트 이름을 엄격하게 바꾼 것이 잘못된 증거를 드러낸 것이다.
- 실패 시 trace 유실도 고쳤다. `await childExit`와 존재 확인을 throw 앞으로 옮기고 `tracePreserved`를 실패 JSON에 담는다. `477-settlement-order`도 같은 모양이라 함께 고쳤다.
- 남은 도구 문제를 기록한다. `first-normal-combat`, `next-normal-actions`, `enemy-arrival-only`은 실패 시 `screens.json`조차 쓰기 전에 throw해 trace와 기록을 모두 잃는다. 계약에 `missingCaptures`/`complete`가 없어 수정 모양이 달라 손대지 않았다. `235-hit-hold`는 어느 경로에서도 `childExit`를 기다리지 않는다. `first-round-end`와 `round2-handoff`는 **PARTIAL인데 종료 코드 0**을 낸다. 부분 성공이 성공 코드를 내면 자동 게이트가 이를 통과로 센다.

## 정정 — 퇴각→숨김 차이도 포트 결함이 아니다

- trace의 유닛 tuple 인덱스15에 `cc.AnimationState.time`, 즉 클립 자체 시각이 들어 있다(`electron/full-battle-trace-renderer.js:404`). 이 값으로 다시 재면 **양쪽 모두 클립 완료에서 숨긴다.** 원본은 클립 시각 **1.2500**에서 아직 보이고 다음 프레임에 숨으며 wall 구간은 1.1616초다. 포트는 클립 시각 1.2333에서 숨고 wall 구간은 1.2500초다.
- 원본의 클립 시계는 퇴각 구간에서 뛴다. 해당 구간 `dt`에 3~22틱짜리 spike가 있어 1.25초짜리 애니메이션이 wall 1.16초에 소화된다. 따라서 wall로 잰 +0.088초는 **원본 캡처의 프레임 pacing**이지 게임 동작 차이가 아니다.
- 클립 시계 기준으로는 오히려 포트가 1틱(16.7ms) **먼저** 숨긴다. 부호가 반대다. 원인은 `BattleDeathPresentationTimeline.kt:250`이 `now >= endsAt`로 완료하고 Cocos 2.x는 `time > duration`에서 FINISHED를 내기 때문이다. 다만 포트의 모든 애니메이션 완료 경로가 같은 `>=` 규약을 쓰므로 여기만 뒤집으면 죽음 타임라인이 나머지와 어긋난다. 16.7ms를 위해 검증된 숨김 시점들을 위험에 빠뜨리지 않는다. 기록만 한다.
- 원본 클립 길이도 확인했다. `UIFrame.js:770-774`가 `sample=24`, `_duration = 홀드 합/24`이며 `anime23`은 6개 항목의 홀드 합 30으로 정확히 30/24 = 1.2500초다.
- **대조 스크립트를 고쳤다.** 애니메이션 길이는 wall이 아니라 클립 시계로 판정한다. `retreat_to_hidden_s`의 wall 델타는 보고만 하고 판정하지 않으며, 대신 숨김 직전의 클립 시각을 기록한다. 이 구간의 미해결 차이는 **0이 되었다.**
- **이 교훈은 이 프로젝트가 이미 배운 것이었다.** 위 `2턴 유비 첫 필살 공격 검증` 절에 "wall timestamp 구간 차이를 animation 길이 버그로 오인해 수정하지 않는다"가 남아 있는데 다시 걸어 들어갔다. 세션을 넘겨 남도록 별도 메모리에 기록했다.

## 477 정산 순서 재검증과 — 측정 오염의 발견

- 원본 `build/reports/yingchuan-source-477-srgb-20260920/`(3,913프레임·2장), 포트 `verification/build/verification/yingchuan-477-srgb-20260920/`. 포트 쪽 대응 모드 이름은 `enemy-settlement`다. 원본의 `477-settlement-order`와 이름이 다르므로 대응표를 남긴다.
- 원본 캡처 기록으로 패널 순서를 확인했다. 첫 패널 `97 / 97`(477, Node.2198, wall 51.511), 두 번째 `104 / 119`(210, Node.2218, wall 53.154)로 **477이 먼저**다.
- 13개 검사가 양쪽 모두 통과하고 불일치는 0이다. 이동→공격 +0.023, 공격→타격 -0.016, 타격→반격 -0.046, 반격→타격 -0.021, 타격→행동확정 -0.013이다.
- **`acted_to_pose39_s`에서 제 판단이 또 틀렸다.** 신선한 원본 두 샘플이 1.6375/1.6043으로 좁게 모였고 포트는 1.4333이라 실제 차이로 의심했으나, **두 원본 창 안에 각각 0.48초짜리 정체 프레임이 정확히 하나씩** 있었고 포트에는 없었다. 그 프레임은 이 캡처 도구가 새 패널 경계에서 `Page.captureScreenshot`을 찍는 지점이다. Cocos `CallbackTimer.update`가 반복 발화 때 `_elapsed`를 0으로 되돌려 초과분을 버리므로 0.48초 프레임 하나가 wall 약 0.19초를 더한다. `1.6375 - 0.188 = 1.4495`로 기록에 남아 있던 1.450과 맞는다.
- 원본의 규칙을 코드에서 도출하면 총 길이는 **1.4초**다. `ui/InfoBaseLayer.js:95 _getValues`는 `a=|src-dsc|`, `s=max(trunc(a/5),1)`로 돌되 루프 조건이 `l < 4 && a > 0`이라 **틱 수는 `min(|Δ|,5)`다**(아래 정정 절 참고. 이 97→77 사례가 5틱인 것은 맞지만 항상 5틱은 아니다)(97→77이면 `[93,89,85,81,77]`, 원본 캡처의 `pendingValues`와 일치). 여기에 `:79-80`의 0.2초 간격, `:70-72`의 `_over` 0.3초, `BattleLayer.js:6737-6739`의 `centerUnit` 0.1초를 더해 `.1 + 5×.2 + .3 = 1.4`다.
- 포트의 계산도 같다. `SettlementPlanModels.kt:264`가 `minOf(abs(after-before),5)`로 같은 틱 수를 내고(`a≤4`면 `a`, `a≥5`면 5로 `_getValues`와 동일), `:270`이 `tickCount*.2`, `:313-324`가 `.1 + Σticks + .3`을 쌓는다. **규칙 차이 없음.**
- 같은 오염이 앞선 기록에도 있었다. `round3-210`의 원본 구 실행 창에는 0.1667/0.1001 정체 프레임이 있어 1.6591이 나왔고 srgb 재실행에는 정체가 없어 1.5551이다. 포트는 1.5666으로 **깨끗한 원본과 +0.0115** 차이다. 내가 "원본 자체 편차 0.104"로 적었던 것은 편차가 아니라 한쪽의 오염이었다.

### 대조 스크립트를 오염에 견디게 고쳤다

- 모든 측정 창을 `dt > 0.05` 프레임으로 훑어 발견되면 그 지표를 **판정하지 않고 정체 값과 함께 드러낸다.** 넓힌 허용치로 덮으면 오염이 숨고 존재하지 않는 편차를 만들어 낸다.
- `round3-210`의 부풀린 허용치(0.16/0.20/0.12)를 모두 걷어내 표준 0.06으로 되돌렸다. 그 아래에서도 `acted_to_pose39_s`는 +0.0115로 통과한다.
- 애니메이션 길이인 `target_retreat_to_hidden_s`는 클립 시계로 본다. 원본 1.2496, 포트 1.249954로 둘 다 30/24=1.25에서 숨긴다.
- `target_lowpose_to_retreat_s`는 이 구간 -0.113, `enemy-first` 구간 +0.030으로 부호가 반대다. 양쪽 다 정체가 없다. 체계적 포트 결함이 아니며 아직 설명이 없으므로 판정하지 않고 드러낸다. 표본이 더 필요하다.
- 앞선 두 구간도 다시 훑었다. `first-combat` 원본 1.4532(정체 없음)/포트 1.4441, `next-actions` 원본 1.4175/포트 1.4333으로 모두 도출된 1.4초 규칙과 맞는다.
- **도구 쪽 개선 여지를 남긴다.** 캡처를 시간이 재어지는 창 밖에서 찍거나 정체 프레임을 보정하면 앞으로의 원본 측정이 부풀지 않는다. 이번에는 대조 쪽에서 검출하는 것으로 처리했다.

## 첫 적군 턴 종료 재검증 — `first-round-end`

- 원본 `build/reports/yingchuan-source-first-round-end-srgb-20260920/`(5,786프레임·7장), 포트 `verification/build/verification/yingchuan-first-round-end-srgb-20260920/`. 대조는 13개 검사와 8개 교차 타이밍이며 원본 대 원본 자기검사를 통과한다.
- 순서는 484 (12,10)→(10,6) 이동→공격25→조조3 HP155→154 피격→조조 반격25→484 HP97→49, 485는 이동만, 475 제자리 공격→211 HP83·반격으로 475 HP70→50, 476 제자리 공격→210 HP62·반격으로 476 HP41→21이다. 조조는 154를 유지한다.
- **13개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 판정된 다섯 타이밍은 +0.024, -0.016, -0.029, -0.021, -0.016으로 모두 0.029초 이내다.
- **정체 검출이 스스로를 입증했다.** 원본 창 세 곳에 0.0813/0.4167/0.4167 정체가 잡혀 판정에서 빠졌고, 그 세 지표의 델타가 -0.098/-0.109/-0.042로 깨끗한 다섯보다 2~5배 크다. 원본이 부풀려진 방향과 정확히 일치한다. 검출이 없었다면 같은 종류의 공격인데 `e476_attack_to_hit`만 0.5753(다른 공격은 0.4828)인 것을 보고 결함으로 오인했을 것이다.

## 캡처 도구의 실패 경로를 모든 모드에서 보존하게 했다

- 이전에는 여러 블록이 자식 프로세스를 회수하기 전에, 그리고 `screens.json`을 쓰기 전에 throw해 **trace와 관측 기록을 모두 잃었다.** 실제로 이번 `enemy-first-combat` 실패에서 trace가 없어 진단이 크게 어려웠다.
- `first-normal-combat`, `next-normal-actions`, `enemy-arrival-only`, `235-hit-hold` 네 블록을 이미 올바른 `enemy-first-combat`/`477-settlement-order`와 같은 모양으로 맞췄다. 실패 시 `complete`와 `missingCaptures`를 담은 `screens.json`을 먼저 쓰고, 자식을 회수한 뒤 `tracePreserved`를 실패 JSON에 담아 throw한다.
- `235-hit-hold`의 성공 경로는 한 글자도 바꾸지 않았다. 성공 시 자식을 기다리게 하면 빠른 성공 실행이 매번 최대 대기 시간을 쓰게 되어 성공 경로의 타이밍을 바꾼다.
- PARTIAL이 종료 코드 0을 내던 `first-round-end`와 `round2-handoff`도 막았다. 이제 미완료 경로는 모두 throw하거나 명시적으로 비정상 종료를 낸다.
- 게이트 조건, 캡처 조건, 폴링 간격, 상한 시간, 성공 로그는 전부 그대로다. 짧은 `first-normal-combat` 재실행으로 성공 경로가 살아 있음을 확인했다(`complete:true`, `missingCaptures:[]`).

## 2턴 화공·증원·조작 인계 재검증 — `round2-handoff`

- 원본 `build/reports/yingchuan-source-round2-handoff-srgb-20260920/`(9장, `complete:true`), 포트 `verification/build/verification/yingchuan-round2-handoff-fire2-20260920/`. 대조는 10개 검사와 화염 좌표 집합 비교다.
- 이전에 누락됐던 `speaker157-dialogue` 캡처도 이번에는 받았다. 기록에 남아 있던 도구의 speaker accessor 문제가 해소된 상태다.
- 증원은 258이 (9,7), 259가 (10,7)에 도착하고 259는 이후 t=133.2에 (10,6)으로 움직인다. 도착 좌표와 이후 이동을 구분해 검사한다. 스크립트 요청 좌표 0(10,6)·32(9,6)이 점유돼 실제 도착은 (10,5)·(9,5)이며, **요청 좌표가 아니라 실제 도착으로** 검사한다.
- **10개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 258→259 도착 간격은 -0.0001초다.
- **화염 27개 좌표가 완전히 일치한다.** 집합 차집합이 양쪽 모두 비어 있다. 기록에 "총 27개 포트 좌표의 기계적 일치까지 검증한 것은 아니다"로 남아 있던 공백을 메웠다.

### 화염이 포트 trace에 기록되지 않던 이유

- 포트는 화염을 그리면서도 trace에는 0개를 기록했다. 화면에는 분명히 그려지므로 렌더링 문제가 아니라 **검증 공백**이었고, 포트가 화염을 엉뚱한 타일에 그려도 어떤 게이트도 잡지 못하는 상태였다.
- 원본 형식은 `[TYPE, TERRAIN, X, Y]`다(`electron/full-battle-trace-renderer.js:296-313`, `core/Config.js:1404-1408`). `TYPE===65535`는 빈 슬롯이며 비활성은 "행이 없음"이다. 화공은 `TYPE=0, TERRAIN=26`이고, 이 26은 스크립트 인자가 아니라 `BattleLayer.setObject2(:1250-1254)`가 `TYPE<3`에서 `[26,27,26]`으로 덮어쓴 값이다.
- **근본 원인은 포트가 원본의 한 구조를 둘로 쪼갠 것이다.** 원본은 `setFire`(`BattleLayer.js:1597`)가 `setObject2`를 거쳐 같은 gate 배열에 넣지만, 포트는 `stage.fires`와 `stage.mapObjects`를 따로 둔다(`ScenarioStageWorldState.kt:135,168,176`). `mapObjects`만 읽으면 화염이 비어 보인다.
- `BattleScreen.kt:5691-5692`는 그 자리에 리터럴 `0, "null"`을 넣고 있었다. 이제 두 맵을 타일 기준으로 합쳐 원본의 단일 슬롯을 재현하고 `RuntimeBattleTraceMapObjectJournal`이 원본 규칙(비활성 제외, x/y/type/terrain 정렬, 변화 시에만 기록, `TYPE<3` 지형 덮어쓰기)을 적용한다.
- **고아 구현을 하나 제거했다.** `verification/.../FullBattleTraceEvidence.kt:151 mapSnapshot`이 같은 규칙을 구현하고 자기 테스트로 초록이면서 **프로덕션 호출자가 0개**였다. core→verification 경계가 이미 투영된 view만 넘기므로 stage 상태에 닿을 수 없어 구조적으로 도달 불가였다. 삭제하고 core 쪽 하나로 합쳤으며, 남은 쪽은 `mapSnapshot`에 없던 지형 덮어쓰기를 갖는다.
- 새 테스트는 **배선 자체**를 검사한다. 실제 `ScenarioStageWorldState`를 스크립트 인터페이스(`setFires`/`setMapObjects`/`setFire`)로 구동해 observer가 실제로 받는 프레임의 `mapObjectsJson`을 확인하므로, 생산자가 호출을 멈추거나 엉뚱한 stage 필드를 읽으면 실패한다. 기존의 순수 함수 테스트는 그것을 잡지 못했다.
- 남은 고아를 기록한다. `FullBattleTraceFrameInput`/`FullBattleTraceFrameProjector`와 `FullBattleTraceEvidence.mapObjectCallObservations`는 모두 테스트 전용이며 프로덕션 호출자가 없다. 이번 범위 밖으로 둔다.
- 한계도 남긴다. 한 타일에 지도 객체와 화염이 동시에 있으면 원본은 공유 슬롯의 마지막 쓰기가 이기지만 포트는 두 맵에 공유 순서가 없다. S_00 2턴에는 그런 타일이 없어 이번 대조는 유효하나 그런 스테이지에서는 순서가 갈릴 수 있다. 지형 덮어쓰기도 trace 시점 변환이며 포트가 저장하는 값 자체는 바꾸지 않았다.

## 2턴 조조 실제 이동·공격 재검증 — `single-player-action`

- 원본 `build/reports/yingchuan-source-single-action-srgb-20260920/`(11,480프레임·5장·실제 입력4회, `complete:true`)와 둘째 샘플 `...-b-20260920`, 포트 `verification/build/verification/yingchuan-single-action-srgb-20260920/`. 이전 기록에 **PARTIAL**로 남아 있던 이 구간이 이제 완전히 통과한다.
- 순서는 조조0 (10,5)→(11,5) 이동→공격25→484 (10,6) HP49→7 피격32→저HP 기본9→행동확정→완료자세39다. 조조는 HP123/MP36을 유지하고 484는 반격하지 않는다.
- **11개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 공격→타격 -0.023, 타격→행동확정 -0.030이다.

### 수동 조작 구간에서 판정하면 안 되는 간격

- 이동→공격이 원본0.769/포트0.833으로 AI 구간의 0.29초보다 훨씬 길다. 이 간격은 **조작기가 이동 완료를 감지하고 명령창을 열어 공격을 누르기까지의 반응 시간**을 포함한다. 원본은 CDP 포인터, 포트는 자체 InputProcessor라 애초에 같을 수 없다. 값은 드러내되 판정하지 않는다. 이후 수동 조작 구간에도 같은 구분이 필요하다.

### 행동확정→완료자세의 0.10초는 타이머 양자화다

- 원본 두 샘플이 3.3759/3.3593으로 편차 0.0166이고 포트는 3.2663으로 일관되게 약0.10초 짧다. 양쪽 창 모두 정체 프레임이 없어 캡처 오염이 아니다. 기준을 통과한 첫 타이밍 차이였다.
- **그런데 양쪽 예산은 항목별로 같다.** 조조의 MINE 패널은 경험치 5틱과 무기경험치 2틱으로 7틱이라 `.1 + 7×.2 + .3 = 1.8`초, 484의 OTHER는 5틱이라 1.4초, 합 **3.2초**다. 포트도 `BattleSettlementOperationCoordinator.kt:154-176`의 계획과 `BattleSettlementPresentationController.kt:291-293`, `SettlementPlanModels.kt:264,313-324`로 같은 3.2초를 쌓는다.
- **내가 에이전트에게 준 전제 두 개가 틀렸다.** (a) "MINE은 2.4초"가 아니다. MINE이 본래 긴 것이 아니라 HP 행과 경험치가 함께 있을 때 2.4가 되는 것이고 이번처럼 경험치만 있으면 1.8이다. (b) "`_getValues`는 항상 5틱"이 아니다. 루프 조건이 `l < 4 && a > 0`이므로 `min(|Δ|,5)`이며, 포트의 `minOf(abs(after-before),5)`가 이미 정확히 같다. 위 477 절의 해당 서술을 정정했다.
- 남은 0.10초의 정체는 **타이머 양자화**다. 원본은 이 3.2초를 `scheduleOnce(.1)` 2개, `schedule(.2)` 12개, `scheduleOnce(.3)` 2개 등 **16개의 개별 Cocos 타이머**로 쓴다. `CCTimer`는 interval을 넘긴 첫 프레임에 발화하고 `_elapsed`를 0으로 되돌려 초과분을 버리므로 타이머마다 조금씩 늘어난다. 포트는 **2개의 절대 마감**을 쓴다. 원본의 실제 dt 흐름에 그 스케줄러를 모사하면 3.3035(A)/3.2538(B)로 이상값 3.2보다 그만큼 길어져 관측된 격차를 설명한다.
- **고치지 않는다.** 이것은 상수가 아니라 실제 기구지만 효과가 타이머 개수에 비례해 모든 정산이 함께 움직인다. 포트가 이미 더 긴 `round3-210`(1.5666 대 1.5551)은 오히려 멀어진다. 포트의 틱을 프레임 양자화로 바꾸는 것이 다음 후보이나 실제 캡처로 재고 나서 판단할 일이다. 값을 드러내되 판정하지 않는다.
- 미확인으로 남긴다. MINE 두 번째 행이 `:6308 WQ_EXP_ADD`라는 것은 틱 수에서 역산한 추론이며 `O`를 직접 덤프해 읽은 것이 아니다. 양자화 모사도 이 빌드의 엔진 코드를 읽은 것이 아니라 `CCTimer` 의미를 가정했다.

## 자동 턴 종료 확인 후 유비 첫 대사 재검증 — `round2-followup`

- 원본 `build/reports/yingchuan-source-round2-followup-srgb-20260920/`(11,452프레임·7장·실제 입력5회, `complete:true`), 포트 `verification/build/verification/yingchuan-round2-followup-srgb-20260920/`. 과거 이 구간의 원본은 확인창의 첫 layout 전 좌표를 캐시해 실제 배치된 자리를 놓쳐 PARTIAL로 끝난 이력이 있다. 이번 실행으로 그 수정이 유효함이 확인된다.
- 조조 행동 뒤 자동 턴 종료 MsgBox의 예를 실제 포인터로 한 번 누르고, camp0→camp1 전환과 유비32의 대사 `이것은 만민의 분노입니다!`까지 관찰한다. 이후 추가 입력은 없다.
- **5개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 대사 경계에서 **표시 부대 22개 전부의 위치·HP·MP·방향·표시 상태 차이가 0**이고 카메라 델타도 `[0.0, 0]`이다. 이 구간에서 가장 강한 근거다.
- 타이밍 두 개는 판정하지 않는다. `camp0→camp1`은 조작기가 확인창을 감지하고 좌표를 안정화한 뒤 누르기까지라 조작기 반응 시간이다(원본9.9115/포트7.7996). `camp1→대사`는 원본 창에만 정체 프레임 0.152와 0.1648이 있어 오염이다(델타 -0.1732가 그 부풀림과 맞는다). 값은 모두 기록에 드러낸다.

## 유비 필살로 484 격파 재검증 — `round2-first-combat`

- 원본 `build/reports/yingchuan-source-round2-fc-srgb-20260920/`(10장·실제 입력6회, `complete:true`), 포트 `verification/build/verification/yingchuan-round2-fc-srgb-20260920/`. 유비32의 대사를 실제 포인터로 한 번 닫은 뒤 484 격파까지 본다.
- 순서는 32 (9,5) `anime21` 필살→484 (10,6) HP7→0 피격32→저HP 기본9→행동확정→완료자세39→퇴각23→숨김이다. 32는 HP149를 유지하고 484는 반격하지 않는다.
- **12개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 공격→타격 -0.008, 타격→행동확정 -0.046이다.
- **행동확정→완료자세가 +0.023으로 잘 맞는다.** 같은 계열인 `round3-210`의 +0.0115와 일관된다. `single-player-action`의 -0.11은 MINE과 OTHER 두 패널이라 타이머가 16개로 늘어 양자화가 누적된 경우였고, 여기처럼 OTHER 한 패널(타이머 7개)이면 차이가 거의 없다. **양자화 설명이 독립 구간에서 뒷받침된다.**
- 퇴각 길이는 wall로 재면 +0.1306이지만 **클립 시계로는 원본 1.248, 포트 1.249863으로 양쪽 다 30/24=1.25에서 끝난다.** wall 차이는 원본 캡처의 프레임 pacing이다.

## 3턴 조조 이동·필살 재검증 — `round3-first-combat`

- 원본 `build/reports/yingchuan-source-round3-fc-srgb-20260920/`(10장·실제 입력11회, `complete:true`), 포트 `verification/build/verification/yingchuan-round3-fc-srgb-20260920/`. 기록상 가장 많은 수정(확정 시점, 정산 순서, 교환 조건, 경험치 막대)이 들어간 구간이다.
- 순서는 0 (11,5)→(10,5) 이동→필살21→483 (9,6) HP19→0 피격32→저HP 기본9→행동확정과 경험치6→30→완료자세39→퇴각23→숨김이다. 조조는 HP123을 유지한다.
- **13개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 그중 둘은 **확정 시점**을 직접 본다. `exp6_until_hit`은 공격 시작부터 타격까지 경험치가 6이어야 하고, `exp30_at_acted`는 행동확정 프레임에 30이어야 한다. 최종 수치가 아니라 **어느 프레임에 바뀌는지**를 보므로 과거의 조기 반영이 되돌아오면 즉시 실패한다.
- 판정된 타이밍은 공격→타격 -0.008, 타격→행동확정 -0.047이다. 퇴각은 클립 시계로 원본1.2335/포트1.2500으로 양쪽 다 끝까지 재생한다.

### 정체 프레임은 타이머 구동 구간만 부풀린다

- 이 모드는 원본이 10장을 찍어 **모든 측정 창에 정체가 들어갔고**, 처음에는 다섯 지표가 전부 판정에서 빠져 타이밍 비교의 힘이 사라졌다.
- 그런데 데이터가 정정을 알려 줬다. `attack_to_hit`은 원본 창에 0.1초 정체가 있는데도 원본0.9401 대 포트0.9319로 **델타 -0.008**이다. `hit_to_acted`도 0.135초 정체에 -0.047이다.
- 이유는 구동 주체가 다르기 때문이다. **타이머 구동 구간**은 Cocos 타이머가 초과분을 버려 정체마다 늘어나지만, **클립 구동 구간**은 정체 동안 클립도 `dt`만큼 함께 진행해 wall이 부풀지 않는다.
- 따라서 정체를 이유로 판정에서 빼는 것은 **타이머 구동 구간에 한정**한다. 그러지 않으면 캡처가 촘촘한 모드에서 판정이 전부 무력화된다. 앞선 구간들의 스크립트도 같은 구분을 적용할 후보이며, 그 구간들은 이미 판정된 지표가 충분해 결론은 바뀌지 않는다.

## 3턴 종료 뒤 유비 행동 재검증 — `round3-followup`

- 원본 `build/reports/yingchuan-source-round3-fu-srgb-20260920/`(11장·실제 입력12회, `complete:true`), 포트 `verification/build/verification/yingchuan-round3-fu-srgb-20260920/`.
- 순서는 32 (9,5)→(9,8) 이동→필살21→480 (8,8) HP97→0 피격32→저HP 기본9→행동확정과 경험치8→16→완료자세39→퇴각23→숨김이다. 32는 HP149를 유지하고 480은 반격하지 않는다.
- **14개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 확정 시점 검사 둘(`exp8_until_hit`, `exp16_at_acted`)을 포함한다.
- 판정된 타이밍이 셋 다 깨끗하다. 이동→공격 **+0.014**, 공격→타격 -0.017, 타격→행동확정 -0.047이다. 퇴각 클립 시각은 원본1.25/포트1.249878로 양쪽 다 끝까지 재생한다.
- 이 구간의 이동·공격은 AI가 구동하므로 이동→공격도 판정 대상이다. **AI 구동 구간의 이동→공격은 일관되게 0.29~0.32초**다(first-normal-combat 0.2922, next-normal-actions 0.2940, enemy-first-combat 0.2929, 477 0.2938, 이번 0.3028). 반면 조작기가 구동하는 구간은 0.769(single-player-action)와 3.370(round3-first-combat)으로 흩어진다. 조작기 구간을 판정에서 빼는 근거가 실제 데이터에 있으며 편의로 느슨하게 한 것이 아니다.

## 장보 방어·필살 반격 재검증 — `round3-counterattack` (12/12 완료)

- 원본 `build/reports/yingchuan-source-round3-ca-srgb-20260920/`와 둘째 샘플 `...-b-`(각 9장·입력13회, `complete:true`), 포트 `verification/build/verification/yingchuan-round3-ca-srgb-20260920/`.
- 258이 146을 공격하고 146이 방어26 뒤 필살21로 반격해 258 HP55→24. 146은 HP105/MP47을 유지하고 258은 중독 기본36을 띤다.
- **12개 검사가 양쪽 모두 통과하고 불일치는 0이다.** 판정된 타이밍은 공격→방어 -0.017, 반격→타격 +0.009, 반격타격→행동확정 -0.030이다.

### 다섯 번째 정정 — "편차가 좁으니 실제 차이"라는 추론이 틀렸다

- 방어→반격이 원본1.7948/1.7951(편차 **0.0003**), 포트1.1699로 **-0.625초**였다. 원본이 극히 일관되고, 정체 프레임에서 클립 시각이 dt만큼 함께 진행하는 것을 확인했기에 나는 이를 정체에 면역인 실제 차이로 판단했다. **틀렸다.**
- **이 창의 끝은 클립이 아니라 146의 필살 대사 `후우후……!`가 닫히는 시점이 정한다.** 대사는 dt를 누적하는 타이머와 조작기가 끝내므로 정체의 dt가 `t`에 그대로 더해진다. 클립이 dt만큼 진행했다는 사실은 창의 끝과 무관하다.
- 그리고 캡처가 **매 실행 바로 그 대사에서** 스크린샷을 찍는다(`source-05-round3-counter-dialogue-complete.png`). 매번 같은 지점이라 두 실행이 똑같이 부풀려지고, 그래서 **편차0.0003이 오염을 배제하지 못한다.**
- **같은 실행 안에서 비교하면 드러난다.** 이 구간에는 필살 대사가 셋 있다. 원본의 깨끗한 둘은 글자당 0.058초(14글자)와 0.055초(27글자)인데 문제의 6글자 대사만 **0.173초**이고, 최대 dt도 그 대사만 0.449/0.451이며 나머지는 0.018 이하다. 두 깨끗한 점의 직선은 6글자를 **0.407초**로 예측하고 포트는 **0.450초**다. 포트는 자기 실행의 다른 대사들과 정합하며 **원본 측정이 이상값이다.**
- 포트는 이 규칙을 이미 충실히 구현한다. `BattleCombatEnvironmentBuilder.kt:90-119`의 `criticalSpeechChecks % 2 == 0`은 원본 `BattleUnit.js:1682 checkCrit()`의 교대를 그대로 옮긴 것이고, `GameDataCatalogUnitDomain.kt:113-155`가 `getCritTxt()`, `SayLayerAutoClose.kt`가 1초 자동닫기, `DialogueSession.kt:455,578`이 0.04초/글자 타자에 대응한다. 원본 `BattleLayer.js:5929-5935`는 `T.flag & CRIT`일 때 `say4`로 외치고 기다린다.
- **포트 결함이 아니다.** 이 지표를 판정에서 빼고 근거를 스크립트와 기록에 남긴다.
- 방어 클립 자체의 0.733 대 0.700(2프레임) 차이는 별개이며 범위 밖으로 남긴다.

### 새 규칙 — 실행 간 편차가 좁아도 오염일 수 있다

- 캡처는 **같은 의미 지점**에서 찍히므로 오염이 **결정적**이다. 매 실행 같은 창을 같은 크기로 부풀린다. 따라서 "원본을 두 번 돌려 편차를 재라"는 규칙만으로는 부족하다.
- 대신 **같은 실행 안의 동종 측정과 비교**한다. 같은 종류의 대사·패널·클립이 그 실행에 여럿 있으면, 하나만 튀는지 보면 된다. 이번에는 글자당 시간이 3배로 튀었고 최대 dt도 그 창만 25배였다.
- 그리고 "클립이 dt만큼 진행하니 면역"은 **창의 끝이 그 클립의 완료일 때만** 참이다. 끝이 타이머나 대사 닫힘이면 정체의 dt가 그대로 더해진다.

## 메뉴 바 라벨과 NinePatch 인자 순서

- 전투 화면 하단 메뉴 바는 날씨 전환과 **같은 prefab**(`Battle/scene/MenuLayer`)의 같은 라벨 노드를 쓴다. 그 prefab이 `_color`를 지정하지 않아 엔진 기본 흰색이고 `_styleFlags 1`로 굵으며, 실제 원본 프레임에서 흰색 굵은 글씨를 확인했다. 그런데 `drawBattleMenu`는 세 라벨을 **검정 36px**로 그리고 위치도 prefab 앵커가 아닌 고정 좌표를 썼다.
- 두 그리기 경로가 다시 어긋나지 않도록 `drawMenuBarLabels`로 묶고 `drawBattleMenu`와 `drawWeatherLayer`가 함께 쓰도록 했다. 기하 상수도 `WeatherTransitionLayout` 하나에서 읽는다.
- **같은 전치 오류를 세 곳에서 찾았다.** Cocos `capInsets`는 (left, top, right, bottom)인데 LibGDX `NinePatch`는 (left, right, top, bottom)이다. `box3`의 `[9,7,9,11]`은 `9, 9, 7, 11`이어야 한다. `menuButtonPatch`와 `winConditionBoxPatch`가 `9,7,9,11`을 그대로 넘기고 있었고, `unitInfoButtonPatch`는 같은 box3 PNG에 `3,3,3,3`을 쓰고 있었다.
- 원본의 모든 `capInsets`를 훑고 포트의 `NinePatch` 호출 57개를 전수 확인했다. 대칭값(3,3,3,3 등)은 순서와 무관하다. `box4 [7,7,8,7]`→`7,8,7,7`과 `vline [0,2,0,1]`→`0,0,2,1`은 이미 올바랐다. Title 화면의 `loadVlinePatch(0,6,37,2)`와 `settingSliderPatch(10,10,7,4)`는 원본에서 대응하는 `capInsets`를 찾지 못해 전치 여부를 판단할 수 없어 **건드리지 않고 표시만 한다**.

### 검증된 것과 되지 않은 것

- **날씨 패널은 픽셀 단위로 불변이다.** 수정 전후 하단 바 영역 차이가 `mean=0.000, max=0`이고 원본 대비 값도 9.688로 동일하다. 공용 헬퍼 리팩터가 이미 검증된 화면을 건드리지 않았다.
- **회귀 없음.** `round2-handoff` 재실행에서 화염 27좌표 일치와 10개 검사 통과를 유지한다.
- **다음은 캡처로 행사되지 않았다.** 메뉴 바의 흰색 라벨(메뉴를 여는 캡처 구간이 없다), `menuButtonPatch`, `winConditionBoxPatch`(이 구간의 승리조건 화면은 box3 판이 없는 텍스트 표시다), `unitInfoButtonPatch`(전투 준비 화면은 영천전투 구간 밖이다). 근거는 prefab과 자산에서 나온 사실이지만 "고쳤으니 화면이 맞다"까지는 아직 아니다.

### 렌더 이벤트 스키마에 색을 넣었다

- `battle-menu` 게이트가 검정 대 흰색을 보지 못한 이유는 스키마에 색이 없었기 때문이다. `RenderEventLog`에 `color`를 넣고 `tools/compare_render_logs.py`의 `SEMANTIC_FIELDS`에 추가했으며 `#rgb`/`#rrggbb`/`#rrggbbaa`/`{r,g,b,a}`/배열을 정규화한다.
- **아직 절반이다.** 한쪽만 색을 실으면 비교를 건너뛰도록 해 기존 생산자가 깨지지 않게 했는데, Cocos harness가 노드 색을 기록하지 않아 원본 쪽이 비어 있다. 나머지 절반은 harness 수정이며 그때 이런 수정이 캡처 없이도 게이트에 걸린다.
- `BattleMenuRenderEvents`는 여전히 `drawBattleMenu`의 출력이 아니라 표다. 다만 이제 두 그리기 경로와 로그가 같은 상수와 같은 색 심볼을 읽어 기하·색이 어긋날 수 없다. 완전한 해소는 그 메서드의 draw 호출에 sink를 꿰는 별도 작업이다.
- 고아 스크립트 권고를 남긴다. `verify_battle_menu_assets.py`는 결정적이고 `verifyTerrainLayerAssets`라는 선례가 있어 배선할 가치가 있으나 MenuLayer 노드 스냅샷 입력을 만드는 곳이 저장소에 없다. `verify_battle_menu_render.py`는 파이프라인이 만들지 않는 2배 픽셀 캡처를 요구하고 아이콘 사각형만 비교해 앞의 것이 포괄하므로 삭제를 권한다.

## 색 비교의 나머지 절반 — Cocos harness가 노드 색을 기록한다

- 포트 쪽에 `color`를 넣었어도 원본 쪽이 비어 있으면 비교가 건너뛰어진다. `electron/main.cjs`의 `append`가 draw 행을 만드는 지점에 노드 색을 실어 비교를 완성했다.
- 무엇이 그려지는 색을 정하는지 먼저 확인했다. 이 Cocos 2.4 빌드에서 `cc.Sprite`/`cc.Label`은 자체 색을 갖지 않고 **노드 색**이 tint를 정한다. harness 저자도 이미 `main.cjs:471`에서 `node.color`를 색의 기준으로 다루고 `colorInt(node.color)`를 쓰고 있었다. prefab이 `_color`를 지정하지 않으면 엔진 기본 흰색이다.
- **알파는 넣지 않는다.** 행에 이미 `opacity`(`node.opacity/255`)가 따로 있고 Cocos 2.4는 `node.color.a`를 255로 둔다. `#rrggbb`를 내면 `compare_render_logs.py`의 `_color()`가 `#rrggbbff`로 정규화해 포트의 `#ffffffff`와 같은 값이 된다. 알파를 양쪽에 중복 세지 않는다.
- 이 파일은 **git 밖**이라 수정을 여기 기록한다. 백업은 `electron/main.cjs.before-color-20260920-173440`이다. 추가된 것은 `colorHex` 도우미와 `append`의 기록 리터럴에 `color: colorHex(node.color),` 한 줄이며 14줄 추가, 기존 필드·게이트·트리거·타이밍은 건드리지 않았다. `node --check`는 출력 없이 종료 코드 0이다.

### `battle-menu` 루트로 메뉴 라벨 수정을 검증했다

- `node tools/verify_render_parity_routes.mjs battle-menu`을 `JOJO_VERIFICATION_CLASSPATH`를 주고 실행해 **`RENDER_PARITY_ROUTE_OK battle-menu`**를 받았다.
- 통과가 "비교했는데 같다"인지 "여전히 건너뛴다"인지 구분해 확인했다. 세 라벨이 경로·본문·색까지 대응하고 **양쪽 모두 실제로 비교되어** 통과한다. `Canvas/Layer/bg/bg0/label`(영천의 전투), `bg/progressBar/label`(턴 수), `bg/progressBar/label0`(1 / 20)이 원본 `#ffffff`, 포트 `#ffffffff`다.
- 원본의 검정 행 하나는 `Canvas/Layer/Panel_cancel`(불투명도30 스크림)이고 포트도 색을 싣지 않아 한쪽뿐이라 건너뛴다.
- **따라서 메뉴 라벨 수정은 캡처가 아니라 게이트로 검증됐다.** 수정 전이었다면 이 게이트가 검정 대 흰색으로 실패했을 것이다.
- **한계를 분명히 한다.** 포트는 지금 `BattleMenuRenderEvents`의 세 라벨에만 색을 싣는다. 다른 모든 행은 한쪽뿐이라 건너뛰어지므로 **색 비교는 이 fixture 하나에만 무장돼 있다.** 다른 상태로 넓히려면 각 recorder가 색을 실어야 한다.

## 정산 패널의 색 기록과 MAX 규칙

- 색 비교를 정산 패널로 넓히려면 "이 라벨이 무슨 색으로 그려지는가"를 코드에서 추적해야 했고, **그 과정에서 색이 아닌 결함이 드러났다.**
- 원본 `recovered-js/modules/ui/MineUnitInfoLayer.js:159`는 `F = [[hp,hpMax],[mp,mpMax],[exp,expLimit],[E,I],[N,L]]`을 만들고 `:173-176`에서 `T >= 3 && A[0] == A[1]`이면 라벨을 `"MAX"`로 바꾸고 `cc.color(17,17,251)`(`#1111fb`, 파랑)로 칠한다. 대상은 index 3(label3, 무기 경험치)과 4(label4, 방어구 경험치)다. `>=`가 아니라 `==`이며 그대로 옮겼다.
- 포트의 실제 경로는 항상 흰색 숫자를 그렸고 한계 검사가 없었다. 더구나 **한계를 그 자리에서 100으로 하드코딩**하고 있어 비교 자체가 불가능했다. `GameDataCatalog.equipmentExperienceLimit`과 `campaign.inventory.equippedItems()`로 실제 한계를 끌어오도록 배선했다. 장비가 없으면 원본 `:77,83`처럼 100으로 떨어진다.
- 색은 그리기와 기록이 **같은 상수**를 읽는다. `SettlementInfoRenderContract`에 `LABEL_COLOR = "#ffffffff"`와 `MAX_LABEL_COLOR = "#1111fbff"`, 그리고 `equipmentExperienceMaxed/Text/Color` 도우미를 두어 두 경로가 갈라질 수 없게 했다.
- **스프라이트 행에는 색을 싣지 않는다.** 포트는 텍스처의 제 색을 그대로 그리고 tint를 적용하지 않으므로 `#ffffffff`를 적는 것은 우연히 맞는 거짓말이다. 비교기는 한쪽뿐인 행을 건너뛰므로 이 쪽이 정직하다.
- 두 prefab(`import/dd/dd2699f7-…`, `import/60/60e799d9-…`)을 해독해 `_color`를 가진 노드는 `Panel_cancel`(검정, 불투명도0) 하나뿐임을 확인했다. **정적 prefab 대비 색 불일치는 없다.** 이번 결함은 prefab이 아니라 런타임 규칙이다.

### 영천전투에서는 관측할 수 없다

- 실제 `maps/data/config.bin`은 `comEquip {lvLimit 3, expLimit 100, upgrade 5}`, `speEquip {lvLimit 9, expLimit 100, upgrade 7}`이다. `R_00.py:2471-2472`의 `setJoinEquip(0, 33, 0, 0, 0, 0)`으로 조조는 무기 33(의천검, speEquip)만 들고 **방어구는 장착하지 않는다.** 방어구 경험치는 애초에 움직이지 않는다.
- 무기 MAX는 레벨9에 경험치150, 누적 약1000이 필요해 공격 334회 이상이고 S_00의 20턴 상한과 플레이어 유닛 하나로는 닿지 않는다. 직접 전투 경로(단검·가죽 갑옷, comEquip)로도 약67회가 필요하다.
- **따라서 이 분기는 캡처로 검증할 수 없고 단위 테스트와 렌더 이벤트 기록만이 근거다.** 이 구분을 남겨 다음 사람이 "검증됐다"고 오해하지 않게 한다.

### 회귀 확인

- 한계를 하드코딩 100에서 실제 값으로 바꾼 것이 막대 길이를 바꿀 위험이 있었다. `SettlementRow.max`는 라벨 텍스트에만 쓰이고 막대 비율은 `maxHitPoints`/`maxMagicPoints`와 성장 grant에서 오며, diff의 `progress`는 주석 한 줄뿐이다. WQ/HJ 행은 max를 출력하지 않는다.
- 실제 재실행에서 OTHER 패널의 차이는 HP 숫자가 애니메이션 중 다른 시점에 잡힌 `119/119` 대 `116/119`뿐이고 막대 길이는 같다. OTHER 패널에는 label3/label4가 없어 애초에 영향 밖이다.
- **`BattleScreen`의 그리기 변경은 커밋하지 않는다.** 진행 중인 정산 재작업과 같은 hunk에서 세 번 충돌해 분리하면 남의 미완성 변경을 함께 담게 된다. 이번 커밋에는 `SettlementInfoRenderContract`의 상수·도우미와 두 recorder, 그리고 테스트만 담는다.

### 정산 패널 루트에서 색 비교가 실제로 켜졌다

- `mine-unit-info`와 `other-unit-info` 두 루트가 모두 `RENDER_PARITY_ROUTE_OK`다. 통과가 비교한 결과인지 건너뛴 결과인지 행 단위로 확인했다.
- `mine-unit-info`는 포트 30행 중 15행에 색이 실리고 그 **15행 전부가 원본에도 색이 있어 실제로 비교되어** 통과한다. `Canvas/Layer/bg/p0/label0,label1,label`, `p1/*`, `p2/*` 등 값 라벨들이 포트 `#ffffffff`, 원본 `#ffffff`로 정규화 후 일치한다. 나머지 15행은 스프라이트라 포트가 색을 싣지 않아 한쪽뿐이고 건너뛴다.
- 색 비교가 무장된 화면은 이제 `battle-menu`(3행), `mine-unit-info`(15행), `other-unit-info`(10행)다. 이 라벨들의 색을 누가 바꾸면 캡처 없이 게이트가 실패한다.

## 명령창 색 기록과 포위 공격 결함

- `BattleCommandRenderEventRecorder`에 색을 실었다. 스프라이트·타일·슬라이스 행은 `#ffffff`(그리기의 `batch.color = Color.WHITE`, 원본 세 prefab이 해당 노드에 `_color`를 두지 않아 엔진 기본 흰색), 라벨과 `Panel_cancel`은 `#000000`(그리기의 `Color.BLACK`, 원본 prefab의 `_color` 패킹값 4278190080), 비활성 명령 라벨은 `#a0a0a0`(`BattleCommandRenderModel.DISABLED_COMPONENT = 160f/255f`, 원본 `CommandLayer.js:60`의 `cc.color(10526880)`)다.
- **회색조는 이 필드로 검사할 수 없고, 그게 맞다.** 원본 `CommandLayer.js:62`는 `setMaterial(0, this.huise)`로 **머티리얼만 바꾸고** `node.color`는 건드리지 않는다. harness의 `colorHex`는 `node.color`의 RGB만 내므로 회색 아이콘에 대해 `#ffffff`를 보고한다. 포트가 `0.2126/0.7152/0.0722` 휘도를 계산해 적으면 픽셀은 같은데 모든 비활성 아이콘에서 거짓 불일치가 난다. 포트도 같은 자리에서 셰이더로 처리하므로 아이콘은 양쪽 `#ffffff`로 둔다. **대신 라벨은 원본이 노드 색을 바꾸므로 비교 가능해진다** — 명령창 수정 중 검사 가능한 절반이 그쪽이다.
- 불투명도는 색에 접지 않는다. harness는 알파를 내지 않고 비교기가 6자리를 `…ff`로 채우므로 `c8`(200/255)을 실으면 모든 행이 어긋난다. 행에 `opacity`가 따로 있다.
- 다섯 루트(`battle-command-initial/-disabled/-cancel/-magick/-property`)가 모두 통과한다. `battle-command-disabled`는 포트 30행 전부에 색이 실리고 **30행 전부가 양쪽 비교되어 불일치 0**이다.

### 포위 공격이 포트에서 항상 비활성이다

- `BattleScreen.battleCommandMask`는 `ATTACK_BIT`, `MAGICK_BIT`, `PROPERTY_BIT`, `SWAP_BIT`만 세우고 **`SIEGE_BIT`를 세우는 곳이 없다.** `SIEGE`는 선택 뒤 안내 문구(`BattleScreen.kt:7926`)에만 나온다. 원본은 `BattleLayer.js:2117`이 `checkCanSiege`(`:3769`, `canSiegle()`와 인접 대상 검사)로 비트를 켠다.
- **게이트는 통과한다.** 이 fixture의 유닛이 포위 조건을 만족하지 않아 원본에서도 비활성이고 양쪽 다 `#a0a0a0`이기 때문이다. 색 비교가 이 결함을 드러내려면 포위 가능한 유닛이 잡힌 fixture가 필요하다.
- **영천전투 구간 대조로도 잡히지 않는다.** 우리가 검증한 시나리오들은 이동·공격·대기만 쓰고 포위를 시도한 적이 없다. 일어나지 않은 일은 아무리 정밀하게 대조해도 드러나지 않는다.
- 수정은 별도 단위다. `canSiegle()`과 인접 대상 판정을 옮겨야 하며 이번 범위 밖이다.
- 남은 한계도 적는다. 이 recorder는 여전히 `drawBattleCommandLayer`의 출력이 아니라 손으로 적은 표다. 색을 정직하게 만들었을 뿐이므로 **색이 통과해도 이 패널의 기하를 보증하지 않는다.**

## 포위 공격은 결함이 아니었다 — 원본에서도 죽은 분기다

- 앞 절에서 `battleCommandMask`가 `SIEGE_BIT`를 세우지 않는 것을 결함으로 기록했다. **틀렸다. 고쳤다면 포트가 원본에서 멀어졌을 것이다.**
- 원본 규칙은 `BattleLayer.js:2117`이 `checkCanSiege`(`:3769`)로 비트를 켜는 것이고, 그 함수는 `:3774`에서 `t.unit().canSiegle()`부터 본다. `canSiegle`(`game-data/Unit.js:327`)은 `armAttr2(arm(), ARM_ATTR_NAME2.SIEGE)`이며 arms 테이블의 exdata **컬럼 11**(`core/Config.js:427`)에서 온다. 컬럼이 없으면 기본값 0이다(`Model.js:709-711`).
- **출하된 arms 테이블에 컬럼 11이 없다.** 저장소의 `EncryptedGameDataCodec` 규칙(키 `ccz65Sha08GeZ1Fu`)을 그대로 옮겨 `core/build/generated/map-assets/data/arms.bin`을 직접 복호했다. MD5 검증 통과, 40행, 컬럼 키는 `0`~`10`뿐이고 `11`을 가진 행은 0개다. 원본 자산 `assets/Game/native/28/285d793d-….1e55f.bin`도 같다. 런타임에서 `setArmAttr2`를 쓰는 곳은 `ui/StageLayer.js:816`뿐이고 지형 슬롯만 건드린다.
- 따라서 **원본도 모든 arm에 대해 `canSiegle()`이 0**이라 `checkCanSiege`가 항상 `:3774`에서 빠져나가고 button4를 켜지 않는다. 포트의 현재 동작이 이미 원본과 같다.
- **캡처된 프레임도 바뀌지 않는다.** `single-player-action`과 `round3-first-combat`을 포함해 모든 영천전투 캡처에서 button4는 양쪽 다 비활성 `#a0a0a0`이다. `battle-command-disabled` fixture도 약한 표본이 아니었다 — 모든 유닛이 포위 불가 유닛이다.
- **가드 테스트를 넣었다.** `SourceArmProfileContractTest`의 `arms table has no siege column so canSiegle is always false`가 프로덕션 로더(`GameDataRepository(...).load().arms`)로 읽어 어떤 행에도 `"11"`이 없음을 확인한다. 이 결론은 전적으로 데이터에 의존하므로, 테이블이 다시 패치되면 조용히 틀려지는 대신 이 테스트가 걸린다. 실패 메시지가 배선 경로(`ArmProfile.siege` → `GameDataCatalogUnitDomain.armProfile` → `BattleUnit` → `battleCommandMask`)를 알려 준다.
- 미확인으로 남긴다. 이 결론은 데스크톱 자산 번들 기준이다. Android `com.hgkj.sgccz.xapk` 페이로드는 해독하지 않았다.

### 교훈 — 규칙이 있다는 것과 실행된다는 것은 다르다

- 앞선 다섯 번의 정정은 "측정이 잘못됐다"였는데 이번은 **"포트가 이미 맞는데 내가 원본을 잘못 읽었다"**이다. 코드에 `checkCanSiege`가 있으니 그 분기가 살아 있다고 가정했지만 데이터가 죽여 놓았다.
- 이번 goal turn에 같은 형태를 여러 번 봤다. `FullBattleTraceEvidence.mapSnapshot`은 올바른 구현이 호출자 없이 잠들어 있었고, `BattleMenuRenderEvents`는 표가 렌더러 대신 로그를 냈으며, `verify_battle_menu_render.py`와 `verify_battle_menu_assets.py`는 저장소 어디에서도 참조되지 않는다. **존재하는 코드가 도달 가능한지 먼저 확인한다.**

## 라운드 배너 턴 수 그림자 색 — 게이트가 잡아낸 첫 결함

- 색 비교를 자동전투 프롬프트·미니맵·라운드 배너로 넓히는 과정에서 불일치가 하나 드러났다.
- 원본 프리팹 `Battle/scene/RoundLayer`(`assets/resources/import/5d/5ddb08c6-…44132.json`)의 `label12`(턴 수 그림자) `_color`는 **4286545795 = (131,127,127), 따뜻한 회색**이다. 포트는 `Color(1f, .5f, .5f, 1f)` = **(255,128,128)**로 그리고 있었다.
- **이 저장소의 기록과 세션 메모리가 둘 다 틀렸다.** 양쪽 다 "RoundLayer는 흰 글자 뒤에 빨간 그림자"로 적어 두었으나, 빨강은 단계 라벨 `label02`/`label22`(`_color` 4278190335 = (255,0,0))뿐이고 턴 수 그림자는 회색이다. 메모리 파일을 정정했다.
- **게이트가 정확히 한 행을 이름과 값으로 지목했다.** `field Canvas/Layer/label12#0.color: expected='#837f7fff' actual='#ff8080ff'`. 색을 표현할 수 없던 게이트가 실제 색 결함을 잡은 첫 사례다.
- 포트를 `ROUND_TURN_SHADOW = #837f7fff`로 고치고 recorder 상수와 테스트 기대값을 함께 맞췄다. 수정 후 `round-normal`, `round-final`, `round-enemy`, `mini-map-shown`, `mini-map-hidden`, `auto-battle-prompt-on` 여섯 루트가 모두 통과한다.

### 세 화면의 색 기록

- **자동전투 프롬프트**(MsgBox4, `import/9b/9bdd4d86-…e1ded.json`): 본문 `#936100`, 위임 토글 `#0005ff`, 비 버튼 `#fc0000`, 예 버튼 `#026e00`이 모두 프리팹 `_color`와 포트 글꼴 색에서 일치한다. 스프라이트와 위임 배너 3행은 `#ffffff`다.
- **미니맵**(`import/1e/1e1e9ef6-…6c573.json`): 어떤 노드에도 `_color`가 없어 전부 `#ffffff`다. `bg/map` 불투명도 168과 `bg/weather` 127은 색이 아니라 `opacity` 필드가 이미 싣는다. `bg/btn`의 `_N$normalColor`는 `_N$transition`이 없어 무효다.
- **라운드 배너**: `Panel_cancel` 검정 불투명도 80, 단계 그림자 빨강, 본 라벨 흰색, 턴 수 그림자 회색(위 수정).
- **비교 대상이 아닌 차이도 기록한다.** MsgBox4의 외곽선 색이 포트 (255,250,110)/(124,255,153) 대 프리팹 (255,226,110)/(124,243,153)로 다르다. 외곽선은 `cc.LabelOutline` 컴포넌트라 `node.color` 기반 비교가 볼 수 없지만 픽셀은 다르다. 별도 수정 단위다.
- 남은 한계. `drawRoundLayer`와 MsgBox4 글꼴 블록이 진행 중인 작업 hunk 안에 있어 recorder의 색 상수를 그리기 코드와 공유하지 못하고 각자 두되 출처 줄을 KDoc에 적었다. 두 곳이 갈라질 위험이 남는다.

## 라벨 외곽선 색을 비교 가능하게 만들고 MsgBox4를 고친다

- 외곽선은 `cc.LabelOutline`이라는 **별도 컴포넌트**다. 지금까지 실은 `color`는 `node.color`이므로 외곽선이 틀려도 어떤 게이트도 볼 수 없었다. 그 구멍이 이 결함을 숨겼다.
- MsgBox4 프리팹(`assets/resources/import/9b/9bdd4d86-…e1ded.json`)의 라벨 네 개는 모두 `_width` 2의 외곽선을 갖는다. 값을 전수 확인했다.

| 라벨 | 프리팹 packed | RGB | 포트(수정 전) | 일치 |
| --- | --- | --- | --- | --- |
| `bg0/label` 본문 | 4285457151 | (255,226,110) | (255,250,110) | **아니오** |
| `btns/tuoguan/label` 위임 | 4294962803 | (115,238,255) | 같음 | 예 |
| `button1/…/Label` 비 | 4292138239 | (255,212,212) | 같음 | 예 |
| `button2/…/Label` 무시 | 4292138239 | (255,212,212) | 같음(노드 비활성) | 예 |
| `button0/…/Label` 예 | 4288279420 | (124,243,153) | (124,255,153) | **아니오** |

- 스키마를 넓힌 근거. harness는 이미 `rendererSnapshot`에서 `labelComponents[i].outline`을 잡아 `append`에 `extras.outline`으로 넘기고 있었다. 즉 한 줄로 내보낼 수 있고 없는 값을 지어내지 않는다. `RenderEventLog`에 `outline`을 넣고 `compare_render_logs.py`의 `SEMANTIC_FIELDS`와 `ONE_SIDED_FIELDS`에 더해 한쪽만 있는 행은 건너뛰게 했다. `color`와 같은 규율을 지킨다 — RGB만, 불투명도 접지 않기, 포트가 모르는 값은 싣지 않기.
- harness 수정은 무버전이므로 여기 기록한다. `electron/main.cjs`의 `append`에 `outline: colorHex(extras?.outline?.color)` 한 줄을 더했다. 백업은 `electron/main.cjs.bak-outline-20260920`이다.
- **포트의 외곽선 색 수정은 커밋하지 않는다.** MsgBox4 글꼴 블록이 진행 중인 작업 hunk(`@@ -182,6 +182,21 @@`) 안이라 분리하면 남의 미완성 변경을 담게 된다. 상수 네 개와 그 적용은 작업 트리에 남긴다. 이번 커밋에는 스키마·비교기·recorder·테스트만 담는다.
- `_width` 2는 스키마가 싣지 않는다. 포트에 대응 필드가 없어 색만 비교한다.

### 내가 깨뜨린 golden 테스트 두 개

- 앞서 `RenderEventLog`에 `color`를 넣으면서 `ScenarioStoryEvidenceRecorderTest`와 `ScenarioEquipConfirmationEvidenceRecorderTest`의 SHA-256 기대값을 갱신하지 않아 **HEAD가 빨간 상태였다.** 집중 테스트만 돌리고 커밋해서 놓쳤다.
- `RenderEventLog`처럼 **여러 recorder가 공유하는 직렬화 계약**을 바꾸면 그 계약의 golden을 가진 모든 테스트가 영향을 받는다. 관련 범위를 스스로 좁게 판단하지 말고 최소한 해당 모듈의 테스트를 돌려야 한다.
- 이번 커밋에서 세 golden을 모두 갱신해 초록으로 되돌렸다. 바이트가 바뀐 이유는 스키마에 `color`와 `outline`이 더해진 것뿐이며 각 테스트 파일에 그 사실을 주석으로 남겼다.

## 선행 실패 하나를 찾아 고친다 — `FightPresentationStateTest`

- 공유 직렬화 계약을 바꾸고 golden을 놓친 일을 계기로 `:core:test` 전체를 돌렸다. 1,148개 중 1개가 실패했다. `S01 actual AST duel stays FIFO through delay20 and continues past End automatically`가 `java.util.NoSuchElementException: ArrayDeque is empty`로 죽는다.
- **이번 turn의 작업과 무관한 선행 실패다.** 현재 작업 트리, `HEAD`(작업 트리 stash 후), 그리고 이 세션이 시작한 `ece0d34`에서 모두 같은 실패가 난다.
- **테스트가 틀렸고 프로덕션이 맞았다.** 깨뜨린 커밋은 `2a9156e` "fix: match source stage delay precision and timer priming"(이번 세션 시작 직전)이다. 그 커밋이 `stage.delay`를 단순 Float 카운트다운에서 Double 누적과 **prime frame**으로 바꿨는데, 옛 테스트가 이전 모델을 그대로 박아 두고 2.0초를 `1.999 + 0.002`로 소진하려 한다.
- 원본 근거가 있다. `recovered-js/modules/ui/StageLayer.js:291`의 `delay(t)`는 `scheduleOnce(resume, .1 * t)`이므로 `delay(20)`은 2.0초이고, `cc.Scheduler`의 `CallbackTimer._elapsed`는 `-1`로 시작해 첫 `update`에 `0`이 되어 **그 프레임의 delta를 버린다.** 포트의 prime frame이 충실한 이식이며 `2a9156e`가 실제 원본 타이머를 감싼 fixture(`core/src/test/resources/parity/stage-delay-source.json`)로 도출했다.
- 실패 지점은 테스트 자신의 `pending` 큐다. 대기 중 `consumeFightCommands()`가 비어 있는데 `removeFirst()`를 불러 터진다. 수정은 1/60초 prime frame 한 번을 넣고 그 사이 런타임이 `DELAY`로 남아 있음을 함께 단언하는 6줄이며, 그 결과 prime 의미 자체가 이 duel 경로에서도 고정된다.
- `FightPresentationStateTest` 14개, `ScenarioRuntimeTest` 158개, `StageDelaySourceFixtureTest` 1개가 통과한다. 테스트 파일 하나만 바뀌어 진행 중인 작업과 얽히지 않는다.

### 새 유형 — "빨간데 아무도 안 봄"

- 이 저장소에서 반복해 나온 형태가 지금까지 셋이었다. 존재하는데 호출되지 않는 구현(`mapSnapshot`, 고아 스크립트 둘), 돌지만 아무것도 보지 않는 게이트(색을 표현 못 하던 `battle-menu`, 손으로 적은 표), 데이터가 죽여 놓은 분기(원본 `checkCanSiege`).
- 이번 것은 네 번째다. **실패하는데 아무도 보지 않는 테스트**가 세션 시작 전부터 빨간 채로 있었다. 앞의 셋이 "통과하는데 의미 없음"이라면 이건 "실패하는데 안 보임"이고, **집중 테스트만 돌리는 습관이 양쪽을 다 만든다.**
- 나 자신도 같은 습관으로 `RenderEventLog`에 `color`를 넣으며 golden 둘을 깨뜨리고 모른 채 커밋했다. 공유 계약을 바꿀 때는 해당 모듈 전체를 돌린다.

## 색 비교의 공백을 메우고, 그 과정에서 포트 결함 하나를 찾았다 (2026-09-20)

`color` 항목을 스키마에 넣은 뒤에도 대부분의 행은 한쪽이 비어 **비교에서 통째로
빠지고 있었다**. 비교기는 한쪽이 null이면 그 항목을 건너뛰므로, 색조가 들어가도
어떤 게이트도 떨어지지 않는 상태였다.

| 경로 | 전 | 후 |
| --- | --- | --- |
| `battle-menu` | 3/39 | 39/39 |
| `mine-unit-info` | 15/30 | 30/30 |
| `other-unit-info` | 10/20 | 20/20 |

**포트가 실제로 아는 색만 적는다**는 규율은 유지했다. 확인한 근거는 그리기
코드다: `drawBattleMenu`·`drawSettlementOverlays`는 첫 줄에서
`batch.color = Color.WHITE`를 세우고 `batch.end()`까지 바꾸지 않으며(중간에
부르는 라벨 함수들도 `font.color`만 만진다), 흐림막만 `Color(0f, 0f, 0f, …)`로
채운다. 따라서 흰색은 짐작이 아니라 포트가 세운 값이다. 게이트가 살아 있는지
음성 대조로 확인했다 — 스프라이트 한 행의 색을 바꾸면 비교기가 exit 1로 떨어진다.

### 찾은 결함: 경험치 아이콘이 2px 크다

`SettlementInfoRenderContract.stat`이 아이콘 높이를 "HP면 40, 나머지는 48"이라는
손으로 적은 분기로 정하고 있었다. 원본 노드는 자산을 2배로 놓으므로 높이는 그림
높이의 두 배이고, mark9(경험치)는 24x23이라 **46**이어야 한다.

이 결함이 그때까지 드러나지 않은 이유가 중요하다. 증거 쪽 `MineUnitInfoLayer`는
46을 적고 있었고 그리기 쪽 계약은 48을 갖고 있었다. **같은 화면을 두 곳에 적어
두었기 때문에** 각자 자기 값과 일관됐고 게이트는 초록이었다. 이것은 이 저장소의
되풀이되는 실패 형태 중 "정직하게 돌지만 아무것도 검증하지 않는 게이트"다.

그래서 증거가 계약의 스프라이트를 **순서대로 꺼내 쓰도록** 재배선했다. 값에 따라
길이가 변하는 막대 셋만 비율을 주고, 다 쓰지 못한 스프라이트가 남으면 터진다.
결합을 음성 대조로 확인했다: 계약의 아이콘 높이를 48로 되돌리면 `mine-unit-info`가
rect 차이로 떨어진다. 재배선 전에는 같은 오차가 초록이었다.

## `auto-battle-active`의 초록은 진짜가 아니었다 (2026-09-20)

전투 경로 25개를 다시 돌려 **`auto-battle-active` 하나가 빨간 것**을 찾았다. 포트는
위임 배너(`img2`/`img3`) 대신 확인창(MsgBox4)을 기록하고 있었다.

원인은 픽스처다. 전투 첫 프레임에 메뉴를 열고 확인을 누르는데, 그때 전투는 아직
여는 각본이 돌고 있다. 직접 실행에 진단을 넣어 확인했다:

```
DIAG enter answerAutoBattle tag=0 overlay=PROMPT checked=true offers=true
DIAG script=DELAY combatBusy=false outcomePending=false phase=BOOTSTRAP ...
```

원본 `BattleLayer.js:1040`은 `if (0 == (1 & t) && r._ctrlHelper)` — 아군 조작 구간에서만
END_ROUND를 받는다. 포트도 `answerAutoBattle`이 `canEndPlayerTurn()`으로 같은 조건을
본다. **확인은 정당하게 무시된 것이고 포트 가드는 원본에 충실하다.**

중요한 것은 그 다음이다. `947f9aa`(2026-09-20 07:12)가 그 가드를 넣기 **전에는** 가드가
없어 부트스트랩 중에도 배너가 떴고, 경로는 화면이 맞아서가 아니라 **포트가 조건을 안 봐서**
통과하고 있었다. 9월 3일의 `auto-battle-active-repeat-diff.json`이 3행 PASS를 기록한 것도
그 상태다. 이 저장소의 "정직하게 돌지만 아무것도 검증하지 않는 게이트"와 "통조림 증거"가
겹친 형태다.

고친 것은 둘이다. 픽스처는 확인이 먹지 않으면 `check`로 멈추고, 캡처는 고정 시각이 아니라
**경로가 이름한 화면에 도달했을 때만** 찍는다. 확인창 두 경로는 그대로 통과하고,
`auto-battle-active`는 이제 원인 지점에서 실패한다.

### 남긴 것
이 픽스처의 전투를 조작 구간까지 진행시키는 일. `readyForEndRound()`로 설치를 미뤄
봤더니 세 경로 모두 걸렸다 — 이 픽스처의 전투는 조작 구간에 **아예 들어가지 않는다**.
확인창 두 경로도 같은 이유로 "원본이라면 열 수 없는 시점에 연 메뉴"를 찍고 있다는 뜻이다.

## "증거에만 있고 화면엔 없다" — 같은 형태로 네 건을 더 찾았다 (2026-09-20)

색 비교 공백을 메우는 과정에서 같은 실패 형태가 되풀이해 나왔다. **같은 화면의 값이
그리기와 증거 두세 곳에 따로 적혀 있어, 각자 자기 값과 일관되면 게이트가 초록**이다.

| 화면 | 포트가 틀렸던 것 | 원본 근거 |
| --- | --- | --- |
| 아이템 상세 | "장착 가능한 부대" 13×3 표와 머리띠 3장, **55개 노드가 통째로 없음** | `ui/ItemLayer.js:122-131` |
| 지형 정보창 | 안쪽 패널이 (288,184,993×460), 원본은 (285.538,183.098,1001.1×459.3). 열 간격 53 대 60 | 원본 하네스 로그 |
| 지형 정보창 | 등급 색 네 단계가 채널마다 1씩 어긋나고 `--` 단계가 (199,199,199) 대 (32,32,32) | `battle/TerrainLayer.js:110` |
| 승리 조건 창 | 본문 `#003fff` 대 `#0e01de`, 단추 `#00d100` 대 `#025b00` | 원본 하네스 로그 |

앞의 두 건은 증거가 원본과 맞고 그리기만 틀렸다. 뒤의 두 건은 증거가 **색을 적지 않아**
비교기가 행을 통째로 건너뛰었다 — 비교기는 한쪽이 null이면 그 항목을 보지 않는다.

### 고친 방법과 검수 절차

화면마다 계약 object 하나를 단일 출처로 두고 그리기와 증거가 **둘 다 그것을 읽게** 했다.
값은 언제나 **원본과 일치하는 쪽**을 정본으로 삼았다. 그리고 매번 음성 대조를 했다 —
계약 값을 하나 바꿔 경로가 **실제로 떨어지는지** 확인하고 되돌린다. 떨어지지 않으면
배선이 안 된 것이다. 이 절차로 배선이 겉돌던 경우를 걸렀다.

색 비교를 받는 행이 늘어난 결과:

| 경로 | 전 | 후 |
| --- | --- | --- |
| `battle-menu` | 3/39 | 39/39 |
| `mine-unit-info` | 15/30 | 30/30 |
| `other-unit-info` | 10/20 | 20/20 |
| `battle-terrain-layer` | 0/216 | 131/216 |
| `use-property-detail` | 0/99 | 40/99 |
| `win-condition-compact` | 0/25 | 2/25 |

### 남긴 것

- `postsCanEquip`의 WEAPONS·ARMOR·AUXILIARY 갈래. posts 표의 EQUIP 비트와 아이템의
  UPGRADE_ARM 목록이 포트의 표 접근자에 아직 없다. 값을 지어내지 않고 기본 갈래로
  떨어뜨리며 함수 주석에 원본 줄번호와 함께 적어 두었다.
- 전체 승리 조건 창(`richtext1`/`richtext2`)을 포트가 그리지 않는다. 게다가 증거 입력이
  `listOf("승리 조건", "장보와 장량을", "격퇴하십시오.", ...)`로 **영천전투 문구를 하드코딩**한다.
- 지형 정보창의 특기 칸: 원본은 `skill/skill_0..3` **스프라이트**에 회색 material을 씌우는데
  포트는 `●`/`○` **글자**를 그린다. 증거는 스프라이트로 적고 있어 그리기만 다르다.

## 영천 전투 경로 27개: 게이트 전부 초록, 색 비교 100% (2026-09-20)

`auto-battle-active`까지 포함해 **전투 경로 27개가 모두 통과**하고, 909행 전부가 색을
비교받는다. 시작 시점에는 28행뿐이었다.

### `auto-battle-active`는 실제로 도달시켰다

앞 절에서 "초록이 진짜가 아니었다"고 적은 경로다. 원인은 둘이었다.

- **여는 대사를 넘겨 줄 주체가 없었다.** 각본이 대사 하나에서 멈춰 전투가 아군 조작
  구간에 영원히 들어가지 않았다(`script=DIALOGUE phase=SCENE0`이 45초간 그대로).
  픽스처 구동기가 `FullBattleTraceDriver`와 같은 방식으로 대사와 승리 조건 안내를
  넘기게 했다.
- **픽스처가 첫 프레임에 눌러 버렸다.** 원본은 `_ctrlHelper`가 있을 때만 확인을 받고
  하네스도 최대 3초를 기다린다. 위임 경로만 조작 구간을 기다렸다 누르게 했다.

이제 위임 배너 세 노드를 실제로 찍어 원본과 일치한다. 도달까지 약 2분이 걸리는데,
영천 1턴은 조조가 `hide: 1`이라 아군 조작을 건너뛰고 2턴에야 조작 구간이 열리기 때문이다.

### 색을 채우며 드러난 포트 결함

| 화면 | 포트 | 원본 | 근거 |
| --- | --- | --- | --- |
| 마법 카드(비활성) | (128,128,128) | `0x7f7f7f` | `MagickListLayer.js:66` |
| 마법 카드(MP 부족 비용) | 회색 | (139,33,33) | `MagickListLayer.js:67` |
| 지형 등급 색 네 단계 | 채널마다 ±1 | `TerrainLayer.js:110` | 같은 줄 |
| 지형 `--` 단계 | (199,199,199) | (32,32,32) | 같은 줄 |
| 지형 이름 | 크림/회색 | 검정(프리팹 기본) | `TerrainLayer.js:102` |
| 승리 조건 본문 | `#003fff` | `#0e01de` | 하네스 로그 |
| 「짐이 알겠다.」 | `#00d100` | `#025b00` | 하네스 로그 |
| 지형 특기 칸 | `●`/`○` 글자 | 30×30 아이콘 | `TerrainLayer.js:109` |

### 색을 채울 때 지킨 규율

**포트가 실제로 아는 색만 적는다.** 화면마다 그리기 코드를 먼저 읽어
`batch.color`/`font.color`가 어디서 세워지고 어디서 바뀌는지 확인한 뒤에만 기본값을
넣었다. 원본이 `#ffffff`를 낸다는 사실만으로 흰색을 적으면 우연히 맞는 거짓말이 된다.

그리고 매번 음성 대조를 했다 — 값을 하나 바꿔 경로가 **실제로 떨어지는지** 확인하고
되돌린다. 이 절차로 배선이 겉도는 경우를 여러 번 걸렀다.

## 구간 타이밍 대조: 다섯 구간 재확인과 판정 규칙 수정 (2026-09-20)

화면 정적 비교를 마친 뒤 **전투 진행 타이밍**으로 넘어가, 원본과 포트 양쪽에서 trace를
새로 떠 다섯 구간을 대조했다.

| 구간 | 결과 |
| --- | --- |
| `first-normal-combat` | 통과 |
| `next-normal-actions` | 통과 |
| `enemy-first-combat` | 통과 |
| `477-settlement-order` | 통과 |
| `round3-210` | 통과 |

### 처음엔 두 창이 떨어졌고, 원인은 포트가 아니었다

첫 구간에서 `attack_to_hit_s`(-0.107)와 `acted_to_pose39_s`(-0.176)가 허용치를 넘었다.
규율대로 원본을 두 번 돌려 자체 편차를 재니 **±0.003**에 불과해 실행 변동이 아니었다.

원인은 원본 캡처의 **정체**였다. 두 창 안에 각각 0.34초·0.48초짜리 프레임이 있었고
(캡처 스크린샷 비용) 포트에는 없었다. 정체는 창의 참값을 바꾸지 않지만 경계 프레임을
놓치게 해 창을 **길게 보이게** 한다. 클립 시계가 이를 뒷받침한다 — 공격 클립이 적중
시점에 원본 0.5499 / 포트 0.4667이고, 원본의 정체 0.34초를 감안하면 포트 값은 원본의
불확실 구간 안에 들어간다.

허용치를 **"고정 폭 + 양쪽 창의 정체 폭"**으로 바꿨다. 정체는 한 프레임이 0.05초를
넘는 경우만 센다 — 프레임마다의 작은 흔들림까지 더하면 체계적으로 느린 쪽을 흡수해
판정력이 사라진다. 정체가 없는 창은 허용치가 정확히 0.06으로 남는다.

이 규칙은 `477.py`·`first-round-end.py` 등이 손으로 들고 있던 `contaminated` 목록을
대신한다. 그 목록은 어느 실행에 정체가 있었는지에 따라 손으로 고쳐야 했고, 정체가
없는 실행에서도 그 창을 영영 보지 않았다.

**음성 대조**로 판정력을 확인했다: 정체를 만들지 않고 포트 시간을 프레임당 0.004초씩
늘리면 세 스크립트가 모두 떨어진다.

### 되돌린 변경 하나

`round3-210.py`는 `summarize`가 세 번 거듭 정의되어 "앞의 둘은 버려진다"고 보고 지웠다가
`NameError`로 깨뜨렸다. **마지막 판정부가 첫 블록의 `WALL_ONLY`를 읽는다.** 되돌리고
파일 머리에 경고를 적었다. 정리하려면 합치기 전후 출력이 같은 trace 쌍에서 바이트 단위로
같은지 먼저 확인해야 한다.

### 작업 환경

디스크가 가득 차 캡처가 실패했다. 사용자 승인을 받아 `verification/build/verification/`의
오래된 캡처 디렉터리 80개를 지워 9.9GB를 확보했다(최신 6개는 남겼다). 한 구간 trace가
150~390MB라 구간을 이어 돌리려면 공간을 계속 살펴야 한다.

## 찾은 포트 결함: 피격 뒤 반격이 한 프레임 이르다 (2026-09-20)

`first-round-end` 구간 대조에서 `a211_counter_to_hit_s`가 **정체가 전혀 없는 창**에서
-0.0666 (허용치 0.06)으로 떨어졌다. 규율대로 추적했다.

- **원본 자체 편차**: 두 번 돌려 1.0999 / 1.0831 → 0.0168. 차이 0.0666은 그 4배다.
- **클립 시계로 쪼갠 결과**: 창은 두 토막이다.

| 토막 | 원본 | 포트 | 차이 |
| --- | --- | --- | --- |
| 피격 → 반격 시작 | 0.6295 | 0.5833 | **0.046** |
| 반격 시작 → 반격 적중 | 0.4704 | 0.4500 | 0.020 (클립 시계 0.4665 vs 0.4583, 한 프레임 안) |

차이의 대부분이 앞 토막에 있다. 클립 전이를 그대로 찍어 보면 원인이 드러난다.

```
원본: +0.0000 anime32(피격)  +0.6165 anime0(대기)  +0.6295 anime25(반격)
포트: +0.0000 anime32(피격)  +0.5833 anime25(반격)          ← anime0 이 없다
```

원본은 피격 클립을 **끝까지 돌린 뒤 대기 자세를 한 번 거쳐** 반격에 들어간다. 포트는
피격 클립을 약 한 프레임 일찍 끊고(마지막 시계 0.575) 곧장 반격으로 넘어간다.

**다른 구간에서도 같은 방향이다.** `next-normal-actions`의 `a211_hit_to_counter_s`는
-0.0412로 허용치 안이지만 부호와 크기가 같다. 한 구간의 변동이 아니라 계통적이다.

### 기전까지 짚었다

포트는 `BattleScreen`이 반격 시작을 `reactionEndsAt = hitAt + requireSourceActionDuration(32, dir)`
로 잡는다. `BattleSpriteTimeline.duration`은 `틱 합 / 24`이므로 anime32는 정확히 0.5833초
(14틱)이고, 반격은 그 **정확한 시각**에 시작한다.

원본은 다르다. `battle/BattleUnit.js:1921-1927`이 클립을 `cc.Animation`으로 재생하고
`cc.Animation.EventType.FINISHED`에 콜백을 건다. Cocos는 이 이벤트를 클립의 마지막 프레임을
**지난 다음 update에서** 쏘므로, 콜백이 도는 시각은 클립 길이보다 한 업데이트 이상 뒤다.
그 콜백이 다음 상태를 세우기 때문에 그 사이 한 프레임이 기본 자세(`anime0`)로 남는다.
관측값이 그대로 이 모양이다 — 피격 0.0000, 대기 0.6165, 반격 0.6295.

이 저장소에 이미 기록된 "원본이 씬 그래프에서 거저 얻는 동작" 부류다. 고치려면 클립 종료를
정확한 시각이 아니라 **클립이 끝난 뒤 첫 업데이트**로 잡고, 그 한 프레임 동안 기본 자세를
보여 줘야 한다.

고치지 않고 기록만 남긴다 — 이 전이는 `scheduleHitReaction` 호출부 여덟 군데가 모두
`reactionEndsAt`을 공유하므로 한 곳만 바꿀 수 없고, 바꾸면 여덟 구간을 다시 돌려 회귀를
봐야 한다. 확인 없이 끝날 위험이 커서 다음 작업으로 넘긴다.
