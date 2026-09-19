# 영천전투 검증 기록

2026-09-20: 사용자 요청에 따라 S_00 영천전투로 진행 범위를 옮겼다.
우선순위는 진행 중단/입력 → 캐릭터 이동·공격·피격·사망과 카메라 → 기능 UI → UI 표현 → 미세한 글자 차이다.

## 우선순위와 현재 근거

| 우선순위 | 항목 | 확인 상태 |
| --- | --- | --- |
| P0 | 행동 정산 중 턴 종료가 새 정산을 시작해 예외 발생 | 정상 속도와 8배속 실제 입력 실행 모두 2턴에 재현. controller와 확인창에 정산 완료 조건이 빠진 입력 경로를 수정. 실제 재실행에서 기존 중단 지점을 통과하고 3턴까지 진행. |
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
- 실행은 S_00 직접 전투 진입과 실제 입력을 사용한다. R_00부터 캠페인 전체를 통과한 증거나 전투 승리 증거가 아니다. 포트 수동 조작기는 기존 작업 트리의 미커밋 도구를 사용한다.
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
