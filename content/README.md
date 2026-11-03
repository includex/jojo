# 게임 콘텐츠 YAML

현재 실행 패키지는 **수호전 외전: 임충전**이다. 조조전의 인물·아이템·책략·전투·대사는 실행 콘텐츠에서 제거했다. 전투 계산에 필요한 공통 병과·효과·UI는 유지한다. 각 요소는 YAML 파일로 작성하며 실행용 인덱스 테이블은 빌드가 생성한다. [현재 패키지 경계와 실행 절차](../docs/content-package.html)를 먼저 확인한다.

| 디렉토리 | 요소 | 기존 테이블 | 현재 개수 |
| --- | --- | --- | ---: |
| `characters/` | 인물 | unit | 64 |
| `equipment/` | 장비 및 소비 아이템 | item | 29 |
| `skills/active/` | 능동 책략 | magic | 10 |
| `skills/passive/` | 패시브 효과 정의 | defineSkill | 125 |
| `skills/assignments/` | 인물·병종 스킬 배정 | unitPostsSkill | 0 |
| `skills/equipment/` | 장비 스킬 효과·성장 | itemSkills.define | 0 |
| `arms/` | 병과 | arms | 10 |
| `posts/` | 병종 | posts | 28 |
| `hitarea/` | 공격 범위 | hitarea | 33 |
| `effarea/` | 효과 범위 | effarea | 15 |
| `shop/` | 전투별 상점 | shop | 8 |

`config.yaml`과 `gameConfig.yaml`은 전역 설정 객체다. `effects/animations.yaml`은 64개 효과 애니메이션 원본이다. `resources/`에는 시스템 메시지 등 클래스패스 리소스를 둔다.

`schema.json`은 필드 이름·타입·필수 여부와 기존 런타임 필드의 대응을 정의한다. 런타임에 새 효과를 구현하지 않고 스키마에만 필드를 추가하면 그 효과가 생기는 것은 아니다.

## 편집·추가

1. 해당 디렉토리에서 가장 가까운 예제 파일을 복사한다.
2. `id`를 설정한다. 파일명보다 YAML 안의 ID가 기준이며, 파일명은 `0000.yaml` 형식을 권장한다. ID는 0 이상의 정수이며 중간 번호가 비어 있어도 된다. 빌드는 빈 인덱스를 JSON null로 생성하고 카탈로그는 이를 항목으로 표시하지 않는다. 현재 인물 512~575, 능동 기술 100~109의 번호를 유지한다. 기존 ID의 이동·재사용은 스크립트와 세이브의 참조 의미를 바꾸므로 피한다.
3. 의미가 있는 필드 이름으로 값을 작성한다. 정수 필드는 `1`처럼 쓰며 `true`, `"1"`, 소수는 허용하지 않는다. 생략한 선택 필드는 기존 런타임의 기본값을 사용한다. 명시한 `null`은 허용하지 않는다. 설명·이름은 문자열이다.
4. 아래 명령으로 검증·생성한다. 잘못된 YAML, 중복 필드/ID, 알 수 없는 필드, 타입 오류와 주요 스킬 참조 오류는 빌드에서 실패한다.

```sh
./gradlew :core:compileGameContent
python3 scripts/validate_linchong.py
python3 scripts/smoke_linchong.py
```

일반 빌드의 `:core:processResources`도 자동으로 컴파일한다. 출력은 `core/build/generated/game-content/data/*.bin`이고 번들에서는 `maps/data/`가 된다. 생성물은 편집하지 않는다. 기존 외부 프로젝트의 같은 데이터 테이블은 번들에서 제외하므로 YAML이 실제 실행 데이터의 원본이다. Python 시나리오·manifest·대사는 content/scenarios에서 관리한다.

## 예시

```yaml
# characters/0512.yaml — 현재 임충 항목의 일부
id: 512
name: "임충"
face: 600
mapAvatar: 601
battleAvatar: 297
posts: 42
attack: 27
defense: 15
spirit: 27
critical: 17
morale: 17
maxHitPoints: 96
maxMagicPoints: 22
```

```yaml
# equipment/0060.yaml — 현재 임충의 철창
id: 60
name: "철창"
itemType: 6
specialType: 0
price: 1
iconIndex: 10
value: 0
upgradeValue: 0
intro: "초기 무기. 추가 공격력은 없다."
```

`skills/assignments/`와 `skills/equipment/`는 현재 비어 있다. 새 배정이나 장비 효과를 추가할 때 `skillId`는 실제 `skills/passive/` 항목을 참조해야 한다.

`skills/active`의 `learnLevels`는 병종 ID → 습득 레벨이다. 키는 따옴표를 붙인 숫자 문자열로 작성한다.

```yaml
learnLevels:
  "24": 1
  "25": 5
```

스킬 배정의 `unit0`, `unit1`, `unit2`는 인물 ID, `posts`는 병종, `skillId`는 패시브 효과 ID다. 없는 선택 필드를 임의로 0으로 채우면 기존 기본값과 의미가 달라질 수 있다.

## 값의 의미와 현재 제약

- 인물 능력치 필드 이름은 현재 `UnitProfile`의 이름을 따른다. `speechGroup`은 치명타 대사 그룹이며 `criticalTexts`로 개별 대사 목록을 정의할 수 있다.
- `skillEffects`는 해당 파일 ID를 키로 하는 효과 그룹을 정의한다. 게임은 특수 장비의 `specialType`, 보조구의 `itemType`으로 그룹을 조회한다. 해당 파일 장비에 직접 부착하는 목록으로 해석하면 안 된다.
- `equipment`는 임충전의 장비·소비 아이템·증표를 포함한다. `iconIndex`는 원본 인덱스이며 현재 화면용 프로필은 여기에 1을 더한다. `upgradeValue`는 기존 8번 필드로 성장/병과 호환 경로에서 사용된다.
- 현재 소비 아이템 판별의 `150..254`, 255의 미지정 표식, 장비의 compact ID 변환 등은 기존 게임 규칙이다. YAML로 옮겼다고 이 제한을 제거한 것은 아니다. 새 ID의 기능을 확인하고 필요하면 core 규칙과 세이브 호환성을 함께 확장한다.
- `itemType`, `category`, `phase`, `incrementType`, `argument` 등은 기존 규칙의 숫자 코드다. 새 효과 종류는 데이터와 core의 해석 코드를 함께 추가해야 한다. 새 인물·아이템의 이미지도 별도로 준비해야 한다.
- 현재 콘텐츠의 이름과 설명은 YAML에서 직접 관리한다. 조조전 번역 카탈로그는 패키징하지 않는다.
- 엔진은 YAML이나 게임 ID를 알지 않는다. YAML 파싱과 테이블 생성은 `buildSrc`에서만 수행하며 Android/desktop 실행 의존성에는 YAML 파서를 넣지 않는다.

[기능별 이용 가이드](../docs/engine-core-feature-guide.html) · [스키마](schema.json)

처음 시작한다면 [단계별 YAML 실습](../docs/developer-onboarding.html)을 따른다.

## 시나리오

`scenarios/`는 임충전 Python 모듈 16개(R_90~R_97, S_90~S_97), `manifest.json`, `dialogue-text.json`을 소유한다. 원작 시나리오와 common/train은 번들하지 않는다. 대사 ID는 소스 줄 번호와 독립적이다.

```sh
./gradlew :core:exportScenarioAst :core:processResources
./gradlew :desktop:run --args="--scenario=R_90"
```

새 모듈은 Python 파일뿐 아니라 manifest에도 등록한다. `stage.say('@dialogue:R_90.scene0.new_dialogue')`처럼 대사 ID를 소스에 명시하고, 해당 ID의 한국어 텍스트를 `dialogue-text.json`의 `entries`에 추가한다. 기존 ID는 대사를 이동하거나 앞에 줄을 삽입해도 바꾸지 않는다. AST 내보내기는 누락되거나 중복된 ID와 직접 입력한 대사를 거부한다. 원본 비교용 도구는 별도 외부 원본을 유지할 수 있으며, 실행용 시나리오 작성 위치는 이 디렉토리다. YAML 컴파일러는 scenarios를 처리하지 않고 별도 AST 빌드가 담당한다.

고정 ID 작성 절차와 API 계약은 [시나리오 플랫폼 가이드](../docs/scenario-platform-guide.html)를 참고한다.

## 게임 패키지와 효과 매핑

`game.yaml`이 게임 제목·시작/복귀 장면·주인공·저장 namespace·전투 연결을 소유한다. `rules/skill-effects.yaml`은 패시브 효과 이름을 스킬 ID에, `rules/magic-effects.yaml`은 능동 책략의 type/category/spell 이름을 코드에 연결한다. 알고리즘을 새로 정의하는 언어는 아니며 기존 효과를 재배치하는 계약이다.

일반 빌드는 `assets/`의 로컬 자산을 패키징한다. `.rgba.gz`는 빌드에서 `.rgba`로 무손실 해제하고 나머지 파일은 그대로 복사한다. `resources/`는 시스템 문구·번역 등 리소스 경로를 보존한다. 외부 Cocos 프로젝트는 선택적인 원본 비교/수입 작업에만 필요하다.

[게임 패키지 작성 예제](../docs/game-package-guide.html)를 따라 설정·효과·자산을 수정하고 검증한다.

## 게임별 규칙 계약 v1

`rules/game-rules.yaml`의 version/resources/rules가 추가 규칙을 설정한다. 기본값은 비활성 목록이다. 새 Kotlin 핸들러는 RuleModule로 등록한다. [규칙 확장 가이드](../docs/game-rule-extension-guide.html)에 실제 후크 범위, 저장·난수 계약과 작성 절차를 정리했다.

## 이름 있는 시나리오 전역 변수

`variables/globals.yaml`의 `version: 1`, `variables` 매핑에 `namespace.snake_case` 이름과 `type`(`int`/`bool`), `default`, `description`을 선언한다. 기존 숫자 주소가 필요하면 선택 필드 `legacyId`를 연결한다. 이 파일의 이름은 저장 파일의 영속 키이므로 배포 후 변경하려면 마이그레이션이 필요하다.

시나리오는 `gvars["campaign.current_chapter"]`로 읽고 쓰며 정수에는 `+=`도 사용한다. 선언된 이름의 타입을 엄격히 검사하고 숫자 별칭과 동일 상태를 공유한다. 이름이 없는 기존 숫자 접근도 유지한다. 새 저장은 `namedGlobals` v1에 이름별 값을 기록하고 기존 숫자 저장은 별칭으로 복원한다. 게임 규칙의 `extensionState`와는 별개다.

[전역 변수 작성·시나리오·저장 호환성 가이드](../docs/global-variables-guide.html)에 YAML과 불리언 예제, 복원 오류 규칙, 검증 명령을 정리했다.


## 외부 Lua 전투 규칙

정적 설정으로 충분한 인물·장비는 기존 YAML만 사용한다. 동적 행동은 `rules/lua-rules.yaml`에 스크립트와 이벤트, 선택적인 `binding: {scope: character|equipment, id: ...}`를 등록한다. 바인딩 ID는 인물/장비의 실제 YAML ID이며 장비는 착용 중인 항목만 적용한다. 기본 등록부는 비어 있어 기존 게임 효과를 바꾸지 않는다.

`JOJO_RULES_DIR` 또는 `-Djojo.rules.dir`로 외부 등록부 디렉터리를 지정하면 Lua와 등록 YAML을 새 전투마다 다시 읽는다. 활성 전투는 소스 스냅샷을 유지한다. 전체 게임 데이터·시나리오를 외부에서 로딩하는 기능과는 별개다.

[Lua 작성·장수/장비 연결·실행 가이드](../docs/lua-rules-guide.html)를 참고한다.

대사 문자열이 `@dialogue:`로 시작하면 대사 카탈로그의 ID로 해석합니다. 그 외 문자열은 카탈로그 조회 없이 그대로 전달합니다. 예: `stage.say("@dialogue:S_90.B01_E02.line002")`, `stage.say("&513\n임 형제, 내가 왔네!")`. `&513`은 기존 화자 지정 문법이며 표시 문구에는 포함되지 않습니다.

시나리오 실행 원본은 `scenarios/*.lua`이며 [Lua 작성 가이드](../docs/lua-scenarios.html)를 참고한다. Lua는 원본 그대로 리소스에 포함되고, 같은 이름의 Python 모듈보다 우선한다. Python AST 생성은 Python 호환 모듈이 존재할 때만 필요하다.
