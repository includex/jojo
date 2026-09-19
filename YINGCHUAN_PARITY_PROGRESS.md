# 영천전투 검증 기록

2026-09-20: 사용자 요청에 따라 S_00 영천전투로 진행 범위를 옮겼다.
우선순위는 진행 중단/입력 → 캐릭터 이동·공격·피격·사망과 카메라 → 기능 UI → UI 표현 → 미세한 글자 차이다.

## 우선순위와 현재 근거

| 우선순위 | 항목 | 확인 상태 |
| --- | --- | --- |
| P0 | 행동 정산 중 턴 종료가 새 정산을 시작해 예외 발생 | 정상 속도와 8배속 실제 입력 실행 모두 2턴에 재현. controller와 확인창에 정산 완료 조건이 빠진 입력 경로 확인. |
| P1 | 플레이어 선택 후 이동·공격 입력과 명령 UI | 선택 직후 선택 해제 기록이 있음. 자동 조작기의 목적지 선택 문제인지 실제 입력 문제인지 조사 중. 아직 이동 버그로 확정하지 않음. |
| P1 | 등장·이동·공격·피격·사망과 카메라 | 원본/포트의 초반 연출, 유비·관우·장비 등장과 2턴 진입 확인. 이동 중 카메라 급이동 의심은 현재 기록에서 재현되지 않아 철회. 이동 완료 후 카메라 전환 시점 차이는 호출 경로 확인 전까지 후보로만 유지. |
| P2 | 정산 HP/MP 막대의 계단형 색상 | 원본은 Linear, 포트 정산 자원은 기본 Nearest 필터. 작은 막대 텍스처 확대 시 차이 확인. |
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
