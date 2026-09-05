# 포팅 런타임 및 테스트 우회 전수 검사

작성일: 2026-09-04 (KST)

## 목적

원본 Cocos 게임과 LibGDX 포트의 전투 표현 차이를 조사하면서 발견한
`상태를 먼저 확정한 뒤 애니메이션만 뒤늦게 재생하는 문제`를 기록하고,
같은 방식으로 포팅이 누락되거나 테스트가 실제 플레이 경로를 우회하는 사례가
다른 영역에도 존재하는지 검사한 결과를 정리한다.

이 문서의 `확인됨`은 코드 참조 또는 실행 로그로 증명된 항목이다.
`검증 공백`은 현재 테스트로 정상 동작을 증명할 수 없다는 뜻이며, 해당 기능이
반드시 고장 났다는 뜻은 아니다.

> 최종 상태(2026-09-04): 아래 1~3절은 감사 당시 발견된 결함을 보존한 기록이다.
> 이번 작업 범위의 전투 선반영, 적 턴 표시, 플레이어 조작, 카메라 포커스,
> 대사 자동 전환, 영천 대화 UI 위치 및 테스트 우회는 모두 수정됐다. 최종 검증
> 결과와 남은 경계는 12절에 기록한다.

## 검사 범위

- `core/src/main/kotlin`: Kotlin 파일 208개
- `core/src/test/kotlin`: 테스트 파일 118개
- 메인 소스에 포함된 `TraceHarness`/`Harness`: 54개
- `JojoGame`의 일반 실행 및 캡처 전용 화면 전환
- `BattleLayer`, `ScenarioPreviewScreen`, `ScenarioBatchVerificationScreen`
- `core`와 `desktop` Gradle 검증 게이트
- 테스트 전용 구현과 실제 런타임 구현 사이의 참조 관계

검사 시점에 `./gradlew :core:test --no-daemon`은 성공했다. JUnit 테스트는
507개, 실패는 0개였다. 그러나 아래 런타임 및 검증 구조 문제는 이 성공과
동시에 존재한다.

## 1. 전투 화면에서 확인된 핵심 결함

### 1.1 AI 이동 결과가 애니메이션보다 먼저 모델에 반영됨

확인됨.

포트의 AI 처리에서는 `Battle.resolveAiTurn()`이 한 유닛의 이동, 공격, HP 변화,
사망 및 제거 결과를 먼저 모델에 반영한다. 그 후 `BattleLayer`가 반환된 결과를
이용해 `FOCUS_DELAY -> MOVING -> ACTION_DELAY -> ACTION -> COMPLETE` 표현을
재생한다.

그 결과 화면에서는 다음 역전 현상이 발생한다.

1. 포커스 단계에서 유닛의 논리 좌표가 이미 목적지로 변경된다.
2. 이동 단계가 시작되면 시각 좌표가 출발점으로 되돌아간다.
3. 이후 목적지까지 이동 애니메이션이 재생된다.

실제 영천전투 포트 로그에서 캐릭터 210의 이동은 다음과 같이 관찰됐다.

- 프레임 53~55, `FOCUS_DELAY`: 모델과 시각 좌표가 이미 `(10,16)`
- 프레임 56, `MOVING`: 시각 좌표가 출발점 `(10,17)`로 되돌아감
- 이후 `(10,16)`으로 이동 보간

이는 사용자가 관찰한 “적군의 이동 결과가 먼저 보인 뒤 이동 애니메이션이
출력되는 현상”과 일치한다.

### 1.2 공격 대상 HP와 사망이 피격 표현보다 먼저 확정됨

확인됨.

동일한 선반영 때문에 공격자의 공격 모션과 대상의 피격 모션이 시작되기 전에
대상의 HP가 변경될 수 있다. 로그에서 캐릭터 476의 HP는 프레임 52의 97에서
프레임 53 `FOCUS_DELAY`의 70으로 먼저 변경됐다.

사망하는 공격에서는 모델 컬렉션의 유닛 수가 `FOCUS_DELAY` 단계에서 이미
감소하는 사례도 반복적으로 관찰됐다. 따라서 사망 애니메이션은 살아 있는
모델 상태의 마지막 단계가 아니라 이미 제거된 결과를 별도 표현 객체로 흉내 내는
구조가 된다. HP 바, 강조/피격 표현, 사망 모션, 카메라 추적이 서로 다른 상태를
참조할 위험이 있다.

### 1.3 원본의 콜백 순서와 다름

확인됨.

원본은 `BattleLayer._ai2`와 `BattleUnit.move2`의 콜백을 통해 다음 단계로
진행한다.

1. 카메라 포커스
2. 이동 애니메이션
3. 이동 완료 콜백에서 위치 확정
4. 공격 애니메이션 시작
5. 공격 클립의 `hit` 이벤트에서 피격 시작 및 피해 반영
6. 피격/사망 표현 완료
7. 다음 행동 또는 다음 유닛 진행

포트는 결과 계산과 화면 표현을 분리하면서 이 콜백 경계를 보존하지 못했다.
단순히 단계 이름과 대기시간을 추가하는 방식으로는 해결되지 않으며, 모델
mutation 자체를 원본 콜백 지점으로 옮겨야 한다.

## 2. 같은 유형으로 확인된 다른 전투 포팅 누락

### 2.1 테스트된 전투 시퀀스가 실게임에서 사용되지 않음

해결됨.

다음 클래스는 원본 콜백 구조에 가까운 독립 구현과 테스트를 가지고 있지만
실제 `BattleLayer`의 전투 실행 경로에는 연결되지 않았다.

- `BattleExchangeSequence`
- `BattleMagicEffectSequence`
- `BattleUnitActionController`
- `BattleScenePort`
- `BattleUnitHideSequence.hide()`
- `BattleAttackSequence.start()`

`BattleLayer`는 `BattleAttackSequence.selectAttackAction()`처럼 일부 정적 선택
함수만 재사용하고, 실제 공격/피격/완료 콜백 시퀀스는 별도로 다시 구현한다.
따라서 이 클래스들의 단위 테스트가 통과해도 실제 전투의 공격, 반격, 연속 공격,
마법, 퇴각 및 사망 순서는 검증되지 않는다.

해결 후에는 `BattleLayer`의 실제 AI 경로가 이동 완료, authored `hit`, 피격 완료,
사망 완료 콜백에서 모델을 순차 커밋하고 이 경로를 영천전투 전체 로그로 검증한다.
동일 상태를 별도로 변경하던 `BattleExchangeSequence`,
`BattleMagicEffectSequence`, `BattleUnitActionController`,
`BattleUnitHideSequence.hide()` 및 `BattleAttackSequence.start()`는 중복 구현과
전용 테스트를 제거했다. `BattleAttackSequence`에는 실제 런타임에서 공유하는
공격 action 선택만 남겼다. `BattleScenePort`는 전술 런타임 구현이 아니라 원본
`Battle.js` UIScene 컨테이너의 독립 계약으로 재분류했다.

### 2.2 테스트된 Control 구현이 실제 AI와 분리됨

해결됨.

다음 구현은 테스트에서는 직접 검증되지만 일반 전투 런타임에서 참조되지 않는다.

- `ControlBasePort`
- `ControlPathPlanner`
- `ControlResumeTerrain`
- `ControlRetreatPolicy`

실제 전투는 별도의 `Battle`/컨트롤러 로직을 사용한다. 따라서 원본 `Control.js`
대응 테스트가 통과해도 실제 적의 경로 선택, 목표 선택, 퇴각 및 점유 타일 보정이
동일하다는 증거가 되지 않는다.

`ControlBasePort`, `ControlPathPlanner`, `ControlResumeTerrain`,
`ControlRetreatPolicy`는 실제 `Battle`/`ControlManager` 경로와 중복된 독립
구현이므로 전용 테스트와 함께 제거했다. 원본 경계 검증은 실제 AI가 사용하는
`ControlManager`, `ControlControllerFactory`, `Battle.resolveAiTurn` 및 영천전투
전체 로그를 대상으로 수행한다.

## 3. 시나리오 및 전체 플레이 테스트의 우회

### 3.1 전체 시나리오 검증이 실제 입력과 시간을 생략함

확인됨. 대상:
`core/src/main/kotlin/com/jojo/port/ScenarioBatchVerificationScreen.kt`

전체 시나리오 검증은 각 상태를 다음 API로 즉시 통과시킨다.

- 대사: `advanceDialogue()`
- 선택지: `confirmChoice()`
- 딜레이: `skipDelay()`
- 모달: `resumeModal()`

따라서 다음 항목은 검증하지 않는다.

- 대사 자동 닫힘 후 다음 장면으로의 자동 연결
- 클릭/키보드 입력 차단과 해제 시점
- 이동과 페이드 완료 콜백
- 장면 전환 전 모달 제거 순서
- 이벤트에 따른 카메라 이동과 포커싱
- 일반 프레임 시간에서의 애니메이션 순서

### 3.2 영천전투 완주 검증이 실제 승리 과정을 대체함

확인됨.

배치 인원은 검증 코드가 첫 슬롯 기준으로 직접 구성하며, 보스 146과 147에게
각각 최대 256회의 `forcedAttack()`을 호출해 사망 상태를 만든다. 이후
`BattleScriptContext`에 HP 0 상태를 주입하고 승리 분기와 다음 장면 목적지만
검사한다.

이 방식은 결과 분기 검증에는 유효하지만 실제 플레이에서 다음 항목을 검증하지
않는다.

- 플레이어 조작 가능 여부
- 턴 진행과 적군 행동의 표시
- 승리 조건이 정상 공격 결과로 발동되는 시점
- 승리 연출과 결과 UI
- 결과 UI 이후 다음 시나리오로의 자연 전환

### 3.3 상태 주소 지정 시나리오 fixture의 한계

확인됨.

분기 및 난수 검증은 `--scenario`, `--verify-scene`, `--verify-label`,
`--verify-vars`, `--verify-random` 등으로 검사 지점의 상태를 직접 지정한다.
이는 특정 조건문의 결과는 검사하지만, 이전 장면에서 그 상태까지 정상적으로
도달했는지는 보장하지 않는다.

## 4. 캡처 전용 경로가 일반 실행을 대체하는 문제

### 4.1 일반 실행과 분리된 FixtureScreen/RouteScreen

확인됨. 대상: `core/src/main/kotlin/com/jojo/port/JojoGame.kt`

`screenshotState`에 따라 일반 화면 전환을 거치지 않고 직접 생성되는 캡처 전용
화면이 최소 20개 존재한다. 대표 범위는 다음과 같다.

- 업적, 속성, 목록
- 입력 상자
- 진행률 및 로딩
- 보상, Choose2, MsgBox3
- 알림
- 캐릭터/전투 편집
- 추첨과 선택적 로그인 오버레이

이 캡처가 통과하더라도 실제 메뉴에서 해당 화면을 열고, 입력하고, 상태를
반영하고, 닫은 뒤 이전 화면으로 복귀하는 전체 흐름은 검증되지 않는다.

### 4.2 캡처 상태가 실게임 상태를 직접 설치함

확인됨.

`BattleLayer`에는 capture/fixture/verify 관련 참조가 약 280곳,
`ScenarioPreviewScreen`에는 약 105곳 있다. 주요 예는 다음과 같다.

- 캡처 요청 시 메뉴, 정보창 또는 모달을 직접 열기
- 전투/캠페인 병력을 검증 코드에서 직접 구성
- 특정 대사 단계까지 자동 진행
- 캡처 전용 카메라 위치와 대기시간 적용
- 마법 목록에 실제 선택 유닛 데이터가 아닌 `fixtureMagics()` 사용

이는 대상 UI의 정지 화면을 비교하는 데는 유효하지만, 그 상태에 도달하는 일반
게임 로직의 정확성을 증명하지 않는다.

### 4.3 원본 합성 프레임을 화면 자산으로 사용하는 검증

확인됨.

`core/build.gradle.kts`의 `exportTitleLoginReference`와
`exportScenarioChoiceReference`는 원본의 합성 RGBA 프레임을 포트 자산으로
패키징한다. `TitleScreen`은 이 합성 배경과 추출된 버튼 이미지를 그린다.

정지 화면 픽셀은 정확히 맞출 수 있지만 다음 항목의 구현 완료 증거는 아니다.

- 버튼 눌림/비활성 상태
- 레이어 생성 및 제거 순서
- 설정값 변경
- 애니메이션
- 화면 전환과 상태 보존

## 5. 자체 충족형 Trace Harness

### 5.1 입력과 무관하거나 예상 결과를 직접 출력하는 하네스

확인됨.

- `HeadTraceHarness`: fixture 내용을 실제 구현에 적용하지 않고 예상 JSON 전체를 출력
- `CoreBoundaryTraceHarness`: 엔진 상수와 48개 레이어 레지스트리를 문자열 상수로 출력
- `BattleControlFullTraceHarness`: fixture 종류별 예상 JSON을 `when`에서 직접 반환
- `BattleBootstrapTraceHarness`: 전투 상수와 레이어 레지스트리를 거대한 문자열 상수로 보관
- `ProgressionLayerTraceHarness`: 업적, 출석, 추첨 등의 예상 스냅샷을 직접 작성
- `EditMutationTraceHarness`: 실제 편집 레이어 대신 로컬 변수와 맵으로 동작을 재구현
- `UnitListInfoLayerTraceHarness`: 실제 화면 대신 별도의 data-only 구현을 다시 작성

이 하네스들은 원본 결과와 동일한 JSON이 출력되는지는 확인하지만, 일반 실행
코드가 해당 동작을 수행하는지는 확인하지 않는다. 구현 코드와 테스트용 재구현이
함께 같은 실수를 포함하거나, 테스트 결과 자체가 하드코딩되면 계속 통과한다.

## 6. 실제 런타임 연결을 증명하지 못하는 구현

정적 참조 검사 결과 다음 구현은 자신의 파일, 테스트 또는 하네스/fixture 외의
일반 런타임 참조가 없거나 독립 캡처 경로에서만 사용된다. 이후 분류 및 정리 결과는
`RUNTIME_UI_ROUTE_CLASSIFICATION.md`에 기록했다.

- `CampaignHallScreen` (삭제: 실제 `showCampaignHall -> showScenario`와 중복)
- `SayDialogueLayer` (삭제: 실제 scenario/battle 대사 구현과 중복)
- `BattleInfoPopup` (삭제: 실제 battle 정보창 구현과 중복)
- `StageLayerPort` (삭제: 실제 `ScenarioStage` 구현과 중복)
- `WinConditionRouteFlow` (삭제: 실제 `BattleLayer` 경로와 중복)
- `LoginOptionalOverlayFlow` (삭제: 도달 불가능한 별도 재구현)
- `InputBoxLayer` (삭제: 원본에도 등록 외 정상 caller 없음; 캡처 내부 상태만 유지)
- `ProgressLayer` (삭제: 원본에도 등록 외 정상 caller 없음; 캡처 내부 상태만 유지)
- `LoadingLayer` (연결: `TitleScreen`의 `CHECK_REGISTER` 표시/콜백/숨김)

일부 기능은 `BattleLayer` 또는 `ScenarioPreviewScreen` 내부에 별도로 다시 구현되어
있을 수 있다. 그러므로 위 목록은 모두 고장났다는 판정이 아니라, 해당 클래스의
테스트를 실게임 구현 완료의 증거로 사용할 수 없다는 판정이다.

네트워크 `registerCheck`와 hot update 자체는 포트 정책상 지원하지 않는다.
`JojoGame.requestRegistrationCheck`는 이 차이를 코드에 명시하고 비동기 실패로
완료하지만, 원본의 Loading attach -> callback -> detach 순서와 입력 차단은 유지한다.

추가 정리에서 `SettingLayer`의 원본 button 7/8/9 조건을 정상 상태 머신에 연결했다.
업적은 저장 보상 존재 여부, 뽑기/출석은 `supportAd >= 8`, 뽑기는 추가로
Hall/Battle scene 조건을 검사한다. 현재 데스크톱 helper 값 0에서도 조건부 경로
자체는 실제 Title/Battle Setting 인스턴스에 연결되어 있다. `ResetLayerPort`는
원본 caller가 없어 삭제했고, EDIT 개발 플래그 전용 `EditGlobalFlow`는 production
타입에서 제거하여 private source-inventory oracle로만 유지했다. 이에 따라
`runtimeIntegrationDebt` 기준선은 0이 되었다.

## 7. Gradle 검증 게이트의 공백

### 7.1 실제 전체 전투 회귀 검사가 기본 check에 포함되지 않음

확인됨.

`tools/verify_yingchuan_battle_regression.mjs`는 다음과 같은 실제 프레임 조건을
검사한다.

- AI 표현 단계 노출
- 이동 보간
- 공격과 피격의 원본 tick 간격
- 카메라 범위
- 뒤로 걷는 방향 오류
- 조기 게임 오버

그러나 이 스크립트는 `:core:test`나 `:desktop:check`의 의존 작업으로 연결되어
있지 않다.

### 7.2 정적 문자열 존재 검사를 동작 검증으로 사용함

확인됨.

`tools/verify_battle_presentation_paths.py`는 `BattleLayer.kt`에 단계 이름이나
호출 문자열이 존재하는지, `battle.resolveAiTurn()` 등의 호출 개수가 예상과
같은지를 검사한다. 실제 실행 순서, mutation 시점, 콜백 완료 여부는 실행하지
않는다.

따라서 단계 이름이 모두 존재해도 모델을 먼저 변경하는 현재 결함을 검출하지
못했다.

## 8. 심각도 판정

### P0 — 런타임 동작을 직접 왜곡

1. AI 이동/공격/HP/사망 상태 선반영
2. 공격, 피격, 마법, 사망 콜백 시퀀스의 실게임 미연결
3. Control 포트와 실제 AI 실행 경로의 분리
4. 전체 플레이 검증의 대사·딜레이·모달 즉시 통과
5. 영천전투 승리 상태 강제 생성

### P1 — 테스트 통과가 구현 완료로 오인됨

1. 하드코딩되거나 별도로 재구현된 Trace Harness
2. 캡처 전용 화면과 정상 플레이 경로의 분리
3. 상태/병력/카메라를 직접 설치하는 캡처 fixture
4. 전체 전투 회귀 스크립트의 기본 `check` 미연결
5. 테스트 전용 포트 클래스와 실제 화면 구현의 중복

### P2 — 시각적 완료로 오인될 위험

1. 원본 합성 framebuffer를 포트 자산으로 사용
2. 한 상태의 픽셀 일치를 전체 상호작용 완료로 간주
3. 글자/스프라이트 비교 전에 정상 전환과 콜백을 검증하지 않음

## 9. 수정 및 재검증 원칙

### 9.1 단일 실행 경로

원본 대응 구현, 테스트 대상, 실제 게임 화면이 같은 코드를 사용해야 한다.
실게임과 연결되지 않은 `*Sequence`, `*Port`, UI 모델을 연결하거나 제거하고,
`BattleLayer` 내부의 중복 구현을 없앤다.

### 9.2 원본 이벤트 시점에서 상태 변경

모델 상태는 최종 결과 계산 시점이 아니라 원본 콜백과 같은 시점에 변경한다.

- 위치: 이동 완료 콜백
- HP: 공격 클립의 `hit` 이벤트
- 피격 종료: 피격 애니메이션 완료 콜백
- 유닛 제거: 사망/퇴각 애니메이션 완료 콜백
- 다음 행동: 이전 표현의 완료 콜백

### 9.3 정상 플레이 로그를 최우선 증거로 사용

원본과 포트 양쪽에서 다음 정보를 동일 형식으로 기록한다.

- 프레임/논리 tick과 경과 시간
- 현재 장면, 스크립트 함수 및 라벨
- 입력 가능 여부와 대기 중인 콜백
- 카메라 위치와 포커스 대상
- 유닛의 모델 좌표와 화면 좌표
- action, direction, frame, opacity, visibility
- HP/MP와 HP 바 표시값
- 공격 hit, 피격 완료, 사망 완료 이벤트
- 화면/레이어 push, pop, replace 순서

fixture가 아닌 다음 자연 경로를 양쪽에서 실행해 비교해야 한다.

`Title -> R 시나리오 -> 전투 준비 -> S 전투 -> 결과 -> 다음 R 시나리오`

### 9.4 캡처는 논리 검증 완료 후 수행

모든 이벤트와 상태 로그가 타이밍 허용 범위 안에서 일치한 후에만 스크린샷
비교를 수행한다. 캡처 전용 상태 설치 코드는 정상 플레이 검증을 대체할 수 없다.

### 9.5 필수 CI 게이트

1. `verify_yingchuan_battle_regression.mjs`를 `desktop:check`에 연결
2. 정상 플레이 전체 전환 trace를 source/port 쌍으로 비교
3. 모델 mutation이 허용된 이벤트 이전에 발생하면 실패
4. 테스트/하네스에서만 참조되는 production 클래스 탐지
5. 예상 JSON 전체를 하드코딩하는 pairwise 하네스 금지
6. 캡처 모드와 일반 모드가 다른 전투/시나리오 코드를 사용하면 실패

## 10. 완료 판정 기준

다음 조건을 모두 만족하기 전에는 포팅 완료로 판정하지 않는다.

- 일반 실행 경로에서 처음부터 다음 시나리오까지 자동 전환 가능
- 플레이어 선택, 이동, 공격, 마법, 아이템 및 턴 종료 가능
- FRIEND/ENEMY 행동이 원본 순서와 시간으로 화면에 표시됨
- 공격, hit, 피격, HP 바, 사망 및 제거 순서가 원본 로그와 일치
- 카메라 포커싱과 이동 범위가 원본 로그와 일치
- 모달과 대사가 추가 Enter 입력 없이 원본과 같은 조건에서 완료
- 테스트가 실게임에서 사용되는 구현을 직접 실행
- 캡처 전용 상태를 사용하지 않은 전체 플레이 회귀 테스트 통과
- 마지막 단계의 원본/포트 스크린샷 비교 통과

## 11. 2026-09-04 테스트 무결성 조치 상태

부분 해결 및 격리됨.

- `ProgressionLayerTraceHarness`는 고정 예상 스냅샷 출력 대신
  `AchievementsLayerPort`, `SignInLayerPort`, `RaffleLayerPort`,
  `ResetLayerPort`, `RegisterLayerPort`를 fixture 이벤트로 직접 호출한다.
- `EditMutationTraceHarness`의 battle/global/roster 경로는 각각
  `BattleEditLayer2`, `EditGlobalFlow`, `EditRosterFlow`를 직접 호출한다.
  이 전환 과정에서 원본의 제거 후 retained callback 동작에 맞게
  `BattleEditLayer2`의 이벤트 처리를 수정했다.
- `UnitListInfoLayerTraceHarness`는 목록 선택에 실제
  `HallUnitListLayer`를 사용하고, 정보창 생성도 `MineUnitInfoLayer`와
  `OtherUnitInfoLayer`를 통과한다. `InfoBase` 값 애니메이션도 production
  `InfoBaseValueAnimation`으로 옮겨 두 정보 레이어와 하네스가 같은 큐와
  retained callback 동작을 사용한다.
- `HeadTraceHarness`는 `ScenarioStage`의 `showHead`, `moveHead`,
  `updateAnimations` 상태를 사용한다. 자산 로더 및 Cocos action log는
  fixture 어댑터이므로 전체 화면 진입 검증으로 간주하지 않는다.
- 하드코딩 Kotlin 결과를 출력하던 `CoreBoundaryTraceHarness`와
  `BattleControlFullTraceHarness`는 제거했다. 전자는 EngineCfg/Instance의
  `source inventory` 감사로, 후자는 helper를 override한 원본 fixture branch의
  `source inventory` 감사로 이름과 출력 의미를 변경했다. 두 감사는 Kotlin
  동작 또는 runtime parity를 주장하지 않는다.
- `BattleBootstrapTraceHarness`는 상수와 레지스트리 출력을 제거하고 실제
  `BattleScenePort`의 resource/save/forces API를 호출한다. 상수 127개와 레이어
  22개는 별도 `auditBattleBootstrapSourceInventory`에서 원본 목록만 감사한다.
- 위 격리 UI 계약 작업은 기본 `core:test` 및 `verifyBehaviorPairwise`에서
  제거하고 `verifyIsolatedFixtureOracles`로 분리했다. source-only 목록 감사는
  별도 `auditRecoveredSourceInventories`에 속한다.
- `verifyRuntimeTestIntegrity`를 기본 `core:test`에 연결했다. 이 게이트는
  위 격리 작업이 일반 검증 aggregate로 재유입되는 것을 차단하고, 감사 대상
  production 타입이 test/harness/fixture에서만 참조되는 상태를 기준선 대비
  탐지한다. 기준선은 `runtimeIntegrationDebt`, `isolatedSourceContracts`,
  `obsoleteDuplicates`로 분류하며 obsolete 항목의 기준선 등록은 실패한다.
  기존 중복 `ControlPathPlanner`, `ControlResumeTerrain`, `ControlRetreatPolicy`는
  실제 `Battle` controller 경로에 같은 기능이 통합되어 구현과 전용 테스트를
  제거했다. 전투 흐름 정리 과정에서는 `ControlBasePort`,
  `BattleUnitHideSequence`, `BattleExchangeSequence`,
  `BattleMagicEffectSequence`, `BattleUnitActionController` 중복 구현도 제거됐다.

명시적 재검증은 Progression 11건, EditMutation 6건, UnitList/Info 3건,
Head 3건과 `BattleScenePort` 동작 1건에서 통과했다. source inventory 감사는
CoreBoundary 19개 엔진 값/48개 레이어, BattleControl fixture branch 15개,
Battle bootstrap 127개 상수/22개 레이어를 확인한다.
최종 `./gradlew -Pkotlin.incremental=false :core:test --no-daemon` 전체 실행은
141개 task가 성공했다. 무결성 ratchet은 현재 production 추적 타입 15개를
검사하며 `runtimeIntegrationDebt`와 `obsoleteDuplicates`는 모두 0개다.
일반 화면 진입과 분리된 `BattleScenePort` 1개만
`isolatedSourceContracts`로 남아 있고, 이는 포팅 완료 증거로 계산하지 않는다.

### 11.1 최종 교차 감사

과거 `NaturalCampaignTransitionTest`는 `ScenarioPreviewScreen`/`BattleLayer`를
생성하지 않고 전투 승리 context와 잘못된 BattlePreparation 경로를 직접 설치했으므로
제거했다. 전체 캠페인 증거는 아래 production 화면 E2E만 사용한다. 같은 이유로
`titleUiRuntimeTest`는 실제 `TitleScreen` 입력 루프가 아닌
`TitleInteraction`/overlay production 계약 검증이므로
`titleInteractionContractTest`로 변경했다. 두 task는 선택된 테스트가 0개여도
통과하지 않도록 `isFailOnNoMatchingTests`를 명시한다.

영천 전체 전투 회귀는 캡처 fixture가 아니라 `DesktopLauncher`가 production
`BattleLayer`를 끝까지 실행한다. 단, 자연스러운 Title→R_00 화면 진입이 아니라
결정적 roster bootstrap을 사용하는 S_00 직접 실행이므로 그 범위를 설명에
명시했다. 실행 직전 이전 trace를 삭제하여 새 trace가 생성되지 않았을 때 과거
결과로 통과할 수 없게 했고, verifier는 trace의 `engine=libgdx-port`, 프레임,
종료 결과와 콜백 단계들을 검사한다. `verifyRuntimeTestIntegrity`는 이 연결과
fixture/강제승리 option 부재 및 `desktop:check` 의존성을 계속 감사한다.

## 12. 2026-09-04 최종 해결 및 재검증

### 12.1 전투 콜백과 표현 순서

- AI뿐 아니라 플레이어의 이동, 공격, 마법, 아이템도 계산 결과를 snapshot으로
  보관하고 원본의 이동 완료, 공격 `hit`, 피격/사망 완료, 아이템 연출 완료
  콜백에서 순차 커밋한다.
- 범위 공격, 마법, 반격, 추격 및 반격 추격도 동일한 지연 커밋 경로를 사용한다.
- HP/MP, 좌표, 행동 완료, 사망 제거 및 인벤토리 소비가 해당 연출보다 먼저
  확정되는 production 직접 호출은 0개다.
- 공격 대상, 범위 대상, 마법 효과 그룹, 반격/추격 대상의 원본 `centerUnit`
  경계에 카메라 포커스를 연결했다.
- 실제 영천전투는 새 trace 2,849프레임에서 `PLAYER_VICTORY`로 끝났으며 이동
  방향 불일치는 0개였다. 첫 연출 `anime21`은 hit 22틱/완료 36틱,
  `anime25`는 hit 11틱/완료 25틱으로 원본 이벤트 경계를 유지했다.

### 12.2 대사와 장면 전환

- 설정의 자동 닫힘 비트를 실제 `PythonAstRuntime` 업데이트에 전달한다.
- S 전투의 최초 scene1, 승리 scene1/보상/종료, scene2, 다음 R 시나리오 연결은
  추가 Enter 입력 없이 production 전환 결정으로 진행한다.
- `verifyCampaignScreenE2e`는 실제 `DesktopLauncher`/`JojoGame` 화면과 설치된
  `InputProcessor`만으로 Title→R_00 scene0..3→S_00→승리/보상/scene2→저장 질문
  →R_01 scene0/1을 통과한다. 강제 승리 context나 캡처 화면을 쓰지 않으며,
  raw stage 0→1→2와 장면 전환용 추가 Enter 0회를 함께 검증한다.

### 12.3 영천 대화 UI 최종 캡처

- 원본 `SayLayer` snapshot의 node transform에 맞춰 패널 하단을 428, 초상화
  하단을 426 논리 좌표로 수정했다. 기존 포트 값은 각각 332/330으로 정확히
  96 논리 단위 아래에 있었다.
- 최종 framebuffer 검출값은 패널 `(423,275)-(1791,639)`, 본문
  `(473,398)-(533,450)`, 초상화 `(1866,257)-(2098,410)`이다.
- 검증기는 화자/본문뿐 아니라 패널 본체, 말꼬리 실루엣, 초상화 위치 및
  opacity/blend 합성 오차를 검사한다. 배경과 GPU sampling에 민감한 X 끝점만
  허용 오차를 두고, 과거의 165px Y 오차는 항상 실패한다.
- 영천 대화 3단계 인물/방향/UI 비교, 모달 8종, 배치 선택 화면이 모두 통과했다.

### 12.4 테스트 우회 방지와 잔여 경계

- `runtimeIntegrationDebt=0`, `obsoleteDuplicates=0`이다. fixture/harness에서만
  참조되던 중복 전투/Control/UI 구현은 실제 runtime에 연결하거나 제거했다.
- `BattleScenePort` 1개만 원본 Cocos `UIScene` 컨테이너의 독립 source contract로
  격리되어 있다. LibGDX 일반 실행에서는 `JojoGame`/`BattleLayer`가 그 엔진
  경계를 대체하므로 전술 runtime 완료 증거로 계산하지 않는다.
- 영천 전체 전투 검증은 `DesktopLauncher -> JojoGame -> BattleLayer`를 실제로
  실행하지만 Title부터 들어가는 E2E가 아니라 결정적 S_00 roster bootstrap이다.
  이전 trace 삭제, `engine=libgdx-port`, 실제 콜백 단계 및 승패 결과 검증으로
  stale/강제승리 우회를 차단했다.

최종 실행 결과:

- 비증분 `:core:test`, 전환/타이틀 contract, desktop compile: 성공(146 tasks)
- `:desktop:verifyYingchuanActorState`: 성공
- `:desktop:verifyYingchuanModalCaptures`: 성공(8 fixtures)
- `:desktop:verifyYingchuanSelectionRender`: 2회 연속 성공
- `:desktop:verifyYingchuanBattleRegression`: 성공(최종 2,849 frames)
- 전체 `:desktop:check`는 308개 중 앞선 307개가 성공한 뒤 선택 fixture의 조기
  패배 전환을 발견해 실패했다. 해당 원인을 수정한 뒤 선택 검증과 그 영향권의
  네 실화면 검증을 다시 실행해 모두 성공했다.

## 13. 2026-09-04 최신 인수인계 시점의 검증 상태

Section 12의 full-battle 성공은 과거 코드/산출물의 증거이며 최신 전체 green으로
해석하면 안 된다. 이후 hide/show/revival 및 fixture 안정화 변경을 모두 합친 상태에서
다시 수행한 결과는 다음과 같다.

- 비증분 `:core:test`: 성공, 481 tests, failures/errors/skipped 0,
  `BUILD SUCCESSFUL in 12s` (141 actionable tasks)
- `:desktop:verifyYingchuanActorState --rerun-tasks`: 성공, dialogue actor states 3개,
  `BUILD SUCCESSFUL in 1m44s`
- `:desktop:verifyS13SceneZeroChoiceOne --rerun-tasks`: 성공,
  `BUILD SUCCESSFUL in 19s`
- 단독 `:desktop:check`: campaign E2E(round 8 PLAYER_VICTORY, transition Enter 0), modal,
  전체 choice/random fixture 및 win-condition pairwise 통과 후 마지막
  `verifyYingchuanBattleRegression` timeout으로 실패, `BUILD FAILED in 2h55m27s`

최신 실패 trace는 `desktop/build/reports/yingchuan-battle-regression-trace.json`이며
36,003 frames, 약 276 MB다. opening script 완료 뒤 round 1의 PLAYER_INPUT에서
`collocation=false`인 채 멈췄다. campaign E2E에서는 실제 InputProcessor로 위임을 켜
같은 production 전투가 끝나므로, 우선 수정 대상은 production 전투 계산이 아니라
standalone full-battle 자동 입력 driver다. 상세 구현 계획과 금지할 우회 방식은
`PORTING_HANDOFF_2026-09-04.md` section 6.0을 따른다.

검증 안정화를 위해 원본 Electron verify mode에만 `backgroundThrottling=false`를
적용했고 actor-state runner에 Electron/Gradle timeout을 추가했다. 포트 LWJGL 자동 실행은
macOS null primary monitor 경계를 피하도록 `(0,0)` 위치를 명시하되 일반 실행의 중앙
정렬은 유지한다.

## 14. 2026-09-05 S_22 6라운드 최초 산술 차이의 확정 원인

원본과 포트의 캠 종료 로그를 비교했을 때 처음 남은 전술 차이는 6라운드 적군 턴의
217→5 공격이었다. 원본은 유닛 5의 HP를 `34→24`, AI 값을 10으로 기록했지만 포트는
`34→25`, AI 값 9를 기록했다. 난이도 보정이나 병종 상성 반올림을 추측해 맞추지 않고,
원본을 같은 seed(`1000`), Math seed(`305419896`), time scale(`16`)로 다시 실행해 실제
계산 단계를 계측했다.

원본의 확정 계산은 다음과 같다.

- 유닛 217은 앞선 일반 공격·격파·반격에서 경험치를 누적해 문제의 공격 전에
  레벨 1에서 2로 상승한다.
- 레벨 상승 뒤 ATT 기본치는 32→34, 장비 포함 ATT는 42→44, CRI는 32→34가 된다.
- 대상의 DEF 57과 지형 110%는 62, 기초 피해는
  `trunc((44-62)/2)+25+2 = 18`이다.
- 병종 상성 60%를 적용하면 `trunc(18*60/100) = 10`이며 나머지 피해율·추가 피해·
  치명 배율은 각각 100%·0·100%라 최종 피해도 10이다.
- 명중률은 76이고 AI 평가는
  `floor(floor(18*100/121)*76/100) = 10`이다.

포트는 당시 유닛 217을 계속 레벨 1, ATT 42, CRI 32로 유지해 기초 피해 16·명중률 73·
최종 피해/AI 값 9를 만들었다. 원본은 `BattleLayer._attack2` 완료 시 일반 공격, 반격,
연속 공격의 공격자에게 진영과 무관하게 `count_exp` 결과를 누적하고 `_jiesuan`에서
`unitAddExp`와 파생 능력치 갱신을 수행한다. 포트는 적 격파 시 플레이어 승자에게만
경험치를 주었고, 전투 로컬 적군 경험치 자체가 없었다. 따라서 직접 수정 대상은 피해
공식이 아니라 **모든 진영의 전투 중 공격 경험치, 레벨업 및 파생 능력치 갱신 경계**다.

보존 증거:

- 원본 진단 trace: `../jojo_mobile/sgccz-desktop/build/diagnostic-s22-harm-r8/traces/S_22.json`
- 원본 runtime log: `../jojo_mobile/sgccz-desktop/electron/build/electron-verification-profile-16972/runtime.log`
- 캠 비교 기준: `build/reports/s22-source-v12-port-v29-camp-boundaries.json`

7라운드 상태/행동 순서와 8라운드 unit22 행동 차이는 이 최초 차이 이후의 파생일 수
있으므로, 경험치 수정 뒤 전체 로그를 다시 생성하기 전에는 각 후속 증상을 독립적인
bookkeeping 결함으로 고치지 않는다.
