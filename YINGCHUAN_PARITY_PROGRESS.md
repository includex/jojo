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
