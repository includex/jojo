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
