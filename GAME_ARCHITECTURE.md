# 엔진과 게임 아키텍처

현재 기준: 엔진은 `:engine`, 게임 구현과 콘텐츠는 `:core`에 둔다. 엔진은 게임 모듈을 참조할 수 없으며, 게임이 정책·데이터·호출 처리를 엔진에 전달한다.

```text
desktop ──────┐
              ├──> core (게임) ───> engine (범용 실행·렌더링)
android ──────┘          ▲
                        │
verification ───────────┘
```

`engine`은 Kotlin/JVM과 LibGDX의 공통 API를 사용한다. 윈도·입력·오디오 장치 등의 플랫폼 백엔드와 네이티브 라이브러리는 desktop/android가 구성한다. core는 `api(project(":engine"))`로 엔진의 공개 타입을 게임 호출자에게 노출한다.

## 엔진이 소유하는 것

경로: `engine/src/main/kotlin/com/jojo/engine`.

| 패키지 | 책임 | 게임에서 전달할 입력 |
|---|---|---|
| `pathfinding` | 직사각형 격자, 결정적인 가중 경로 탐색, 이동 방향 계산 | 격자·점유 좌표·이동 비용 |
| `script` | AST 접근, 값 변환, 표현식·문장 실행, 호출 프레임과 레이블 점프, 내장 연산 | 변수 바인딩·호스트 함수·게임의 중단 조건 |
| `text` | 태그를 포함한 글자 공개, 프레임 시간 소비, 공개 완료·초기화 | 원문·시간 간격·타이밍 정책 |
| `graphics` | sliced patch 정점/UV 계산, 투영 정밀도, 이미지 크기·중심 계산 | 배치·텍스처 영역·테두리·배치 치수 |

엔진에는 전투 규칙, 캐릭터·아이템·병종, 조조전 시나리오 이름, 한국어 번역, 게임 리소스 경로가 없다. AST는 LibGDX `JsonValue`로 전달하지만 파일을 찾거나 시나리오를 고르는 것은 엔진의 책임이 아니다.

전투·거리·궁전의 대사, 선택지와 모달은 `RuntimeDialogueSessionAdapter`가 스크립트 상태를 변환하고 `DialogueSession`이 글자 공개·확인 입력·자동 진행을 소유한다. 전투의 별도 글자 공개 상태와 자동 닫기 타이머는 제거했다. `ScenarioDialogueSessionAdapter`는 시나리오 배치 정책만 연결한다. 전투는 누적 시간 방식과 공개 후 1초 자동 진행을, 시나리오는 원본 콜백 타이머 방식과 자동 진행 규칙을 유지한다.

화면별 자동 진행, 전투 연출 순서, 시나리오 선택지와 모달의 의미도 게임 정책이다. 범용 글자 공개 기능을 사용하되 `DialogueSession`과 화면 어댑터가 이 정책을 결정한다.

## 게임이 소유하는 것

경로: `core/src/main/kotlin/com/jojo/game`.

| 계층 | 책임 | 대표 코드 |
|---|---|---|
| `domain` | 전투·캠페인·성장·장비 규칙과 게임 상태 | `BattleUnit`, 캠페인 장비/인벤토리 모델 |
| `application` | 게임 진행, 스크립트와 게임 API 연결, 전투 명령, 화면 전환 | `ScenarioInterpreter`, `ScenarioCallCoordinator`, `ScenarioStageMovementPlanner` |
| `presentation` | 화면·입력·대화 모달·HUD·캐릭터 연출과 자원 수명 | `BattleScreen`, `ScenarioScreen`, `DialogueSession` |
| `infrastructure` | 게임 파일·테이블·저장·오디오·설정·번역 접근 | `HallPathGridLoader`, `GameDataRepository`, `ReviewedKoreanText` |

게임의 이동 비용 `1/767/511/15`, 홀의 100×100 지도 규격, `maps/pmaps/...` 경로는 core가 소유한다. 엔진의 `PathGrid`와 `GridPathfinder`에는 다른 크기의 지도를 전달할 수 있다.

게임의 `vars`, `gvars`, `pvars`는 `ScenarioCallCoordinator`가 이름과 실제 맵을 바인딩한다. `stage.*`, 대사·선택 입력·전투 대기 등은 게임이 처리하며, 엔진은 콜백으로 그 처리를 요청하거나 실행을 중단한다.

한국어 글꼴 선택, `JOJO_FONT_PATH`, `JOJO_FONT_HINTING`, 번역 카탈로그도 core의 책임이다. 엔진 렌더링은 텍스처·글꼴 선택이나 콘텐츠를 결정하지 않는다.

## 인물·장비·스킬 작성 원본

`content/characters`, `content/equipment`, `content/skills/{active,passive,assignments,equipment}`에서 각 요소를 YAML 파일 하나로 정의한다. `content/schema.json`이 이름 있는 필드와 기존 런타임 테이블의 대응을 명시한다. `:core:compileGameContent`는 빌드 시 YAML·ID·필드 타입과 주요 스킬 참조를 검증하고 여섯 테이블을 `core/build/generated/game-content/data`에 생성한다. `processResources`는 이 테이블만 번들하며 외부 원본의 동일한 테이블은 제외한다. 화자 이름 라벨 생성도 YAML에서 컴파일한 인물 표를 읽는다.

인덱스형 런타임과 저장 호환성을 유지하기 위해 기존 ID를 보존한다. 다른 병종·지형·상점·이미지·시나리오 입력은 아직 기존 경로를 사용한다. YAML 파서는 buildSrc에만 있으며 engine/core 런타임 의존을 늘리지 않는다. 편집 방법과 ID 제약은 [콘텐츠 안내](content/README.md)를 따른다.

## 경계를 유지하는 방법

- 엔진에 게임 기능을 추가하려면 먼저 게임 이름·리소스·규칙을 제거하고 입력이나 호스트 계약으로 표현한다.
- 게임에서 엔진으로의 호출은 공개 API를 직접 사용한다. 옛 패키지의 `typealias`나 전달용 호환 클래스를 남기지 않는다.
- 엔진 단위 테스트는 `engine/src/test`에 둔다. 실제 시나리오·전투·화면 연동 회귀 검증은 core에 둔다.
- `:engine:verifyEngineBoundaries`는 엔진의 다른 프로젝트 의존, 게임/런처/백엔드 참조, 고정된 게임 콘텐츠 경로를 거부한다. `:engine:test`에 연결되어 있다.
- 엔진의 main/test 리소스에 게임 콘텐츠를 추가하지 않는다. 엔진 테스트는 외부 시나리오 폴더 없이 실행 가능해야 한다.

```sh
# 독립 엔진 검증
./gradlew :engine:test

# 게임 동작과 모든 호출 모듈의 컴파일 검증
./gradlew :engine:test :core:test :desktop:classes :verification:compileKotlin :android:compileDebugKotlin
```

## 남은 게임 내부의 결합

엔진 분리와 core 내부 계층 정리는 다른 경계다. core에는 일부 도메인 모델의 `GameDataCatalog` 직접 참조, 도메인에서 표시 문구를 참조하는 코드, 큰 화면 클래스가 남아 있다. 이 코드는 게임 쪽에 있으며 엔진을 역참조하게 만들지 않는다. 후속 정리에서는 읽기 전용 게임 데이터 계약과 표시 모델을 분리하고, 화면 상태의 소유권을 기준으로 클래스를 나눈다.

`REFACTORING_HANDOFF.md`, `RUNTIME_ARCHITECTURE_AUDIT.md`의 과거 파일 수·의존 관계는 당시 기록이다. 현재 모듈 경계와 작업 기준은 이 문서를 따른다.

## 이번 분리의 검증 결과

- 엔진 단위 테스트 24개, 게임 회귀 테스트 1,151개 통과.
- desktop·android·verification 컴파일 통과.
- 59개 R 시나리오의 기동 및 한 입력 분기 진행, 58개 S 전투의 초기화와 1턴 이벤트 검증 통과.
- 영천 진행 경로와 전투 데이터 적합성 검증 통과. AST API 누락 없음.

이는 전체 캠페인의 모든 분기 완주나 화면 픽셀 비교를 뜻하지 않는다.

## 사용하지 않는 코드 정리

실제 실행 경로는 `ScenarioInterpreter`와 엔진 AST 실행기로 통일했다. 이전 `ScenarioSource`, `ProgramPlayback`, `ScenarioPlayback`, `ScenarioProgramLoader`, `ScenarioScript`·`ScenarioTimeline` 경로와 그 경로만 검사하던 테스트를 제거했다. 실제로 쓰는 `ScenarioMetadataReader.loadLastJoinBattleLimit`는 유지한다.

호출자가 없는 Scene2D 대화 호스트·뷰, 옛 거점 렌더러, 보상 모형, 프레임워크 장면·오디오 모형, 전투 전환 헬퍼와 자료형도 제거했다. 실제 선택지 `ChoiceScene2dLayer`와 대화 렌더러는 계속 사용한다.

옛 SDK 비교 도구만 사용하는 서비스·종료 화면 모형은 `verification/fixtures/framework`로 옮겼다. 게임 런타임에 포함되지 않으며 검증 코드가 core에 역으로 유입되지 않는다.
