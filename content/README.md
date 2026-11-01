# 게임 콘텐츠 YAML

인물·아이템·책략·스킬의 작성 원본이다. 각 요소는 하나의 YAML 파일로 정의한다. 기존 ID와 데이터는 그대로 옮겼으며, 실행 시 사용하는 인덱스형 테이블은 빌드가 생성한다.

| 디렉토리 | 요소 | 기존 테이블 | 현재 개수 |
| --- | --- | --- | ---: |
| `characters/` | 인물 | unit | 512 |
| `equipment/` | 장비 및 소비 아이템 | item | 256 |
| `skills/active/` | 능동 책략 | magic | 143 |
| `skills/passive/` | 패시브 효과 정의 | defineSkill | 334 |
| `skills/assignments/` | 인물·병종 스킬 배정 | unitPostsSkill | 157 |
| `skills/equipment/` | 장비 스킬 효과·성장 | itemSkills.define | 252 |
| `arms/` | 병과 | arms | 40 |
| `posts/` | 병종 | posts | 80 |
| `hitarea/` | 공격 범위 | hitarea | 33 |
| `effarea/` | 효과 범위 | effarea | 15 |
| `shop/` | 전투별 상점 | shop | 99 |

`config.yaml`과 `gameConfig.yaml`은 전역 설정 객체다. `effects/animations.yaml`은 64개 효과 애니메이션 원본이다. `resources/`에는 번역 문구, 시스템 메시지, 대사 이미지 등 클래스패스 리소스를 둔다.

`schema.json`은 필드 이름·타입·필수 여부와 기존 런타임 필드의 대응을 정의한다. 런타임에 새 효과를 구현하지 않고 스키마에만 필드를 추가하면 그 효과가 생기는 것은 아니다.

## 편집·추가

1. 해당 디렉토리에서 가장 가까운 예제 파일을 복사한다.
2. `id`를 설정한다. 파일명보다 YAML 안의 ID가 기준이며, 파일명은 `0000.yaml` 형식을 권장한다. 각 디렉토리의 ID는 0부터 연속되어야 한다. 새 항목은 마지막 ID 다음에 추가한다. 기존 ID를 이동·삭제·재사용하면 스크립트와 세이브의 참조 의미가 바뀐다.
3. 의미가 있는 필드 이름으로 값을 작성한다. 정수 필드는 `1`처럼 쓰며 `true`, `"1"`, 소수는 허용하지 않는다. 생략한 선택 필드는 기존 런타임의 기본값을 사용한다. 명시한 `null`은 허용하지 않는다. 설명·이름은 문자열이다.
4. 아래 명령으로 검증·생성한다. 잘못된 YAML, 중복 필드/ID, 빠진 ID, 알 수 없는 필드, 타입 오류와 주요 스킬 참조 오류는 빌드에서 실패한다.

```sh
./gradlew :core:compileGameContent
./gradlew :core:test :desktop:classes :android:compileDebugKotlin
./gradlew :verification:verifyAllHeadless
```

일반 빌드의 `:core:processResources`도 자동으로 컴파일한다. 출력은 `core/build/generated/game-content/data/*.bin`이고 번들에서는 `maps/data/`가 된다. 생성물은 편집하지 않는다. 기존 외부 프로젝트의 같은 데이터 테이블은 번들에서 제외하므로 YAML이 실제 실행 데이터의 원본이다. Python 시나리오·manifest·대사는 content/scenarios에서 관리한다.

## 예시

```yaml
# characters/0000.yaml — 실제 기존 항목 일부
id: 0
name: "조조"
face: 0
mapAvatar: 0
battleAvatar: 1
posts: 0
attack: 41
defense: 49
spirit: 46
critical: 40
morale: 42
maxHitPoints: 102
maxMagicPoints: 30
```

```yaml
# equipment/0026.yaml — skillEffects는 skills/equipment의 ID 목록이다.
id: 26
name: "철제 보검"
itemType: 16
price: 30
iconIndex: 20
value: 75
upgradeValue: 10
skillEffects: [106]
```

```yaml
# skills/equipment/0106.yaml의 구조 예 (실제 값은 해당 파일 참고)
id: 106
skillId: 146
# skillId는 skills/passive의 ID이며, phase/effectValue/upgrade는 선택 필드다.
```

`skills/active`의 `learnLevels`는 병종 ID → 습득 레벨이다. 키는 따옴표를 붙인 숫자 문자열로 작성한다.

```yaml
learnLevels:
  "24": 1
  "25": 5
```

스킬 배정의 `unit0`, `unit1`, `unit2`는 인물 ID, `posts`는 병종, `skillId`는 패시브 효과 ID다. 없는 선택 필드를 임의로 0으로 채우면 기존 기본값과 의미가 달라질 수 있다.

## 값의 의미와 현재 제약

- 인물 능력치 필드 이름은 현재 `UnitProfile`의 이름을 따른다. `speechGroup`은 치명타 대사 그룹이며 `criticalTexts`로 개별 대사 목록을 정의할 수 있다.
- `skillEffects`는 해당 파일 ID를 키로 하는 효과 그룹을 정의한다. 게임은 특수 장비의 `specialType`, 보조구의 `itemType`으로 그룹을 조회한다. 예를 들어 장비 90은 specialType 26 → equipment/0026.yaml의 그룹 → 효과 106 → 패시브 146으로 연결된다. 해당 파일 장비에 직접 부착하는 목록으로 해석하면 안 된다.
- `equipment`는 소비 아이템까지 포함한 기존 item 테이블 전체다. `iconIndex`는 원본 인덱스이며 현재 화면용 프로필은 여기에 1을 더한다. `upgradeValue`는 기존 8번 필드로 성장/병과 호환 경로에서 사용된다.
- 현재 소비 아이템 판별의 `150..254`, 255의 미지정 표식, 장비의 compact ID 변환 등은 기존 게임 규칙이다. YAML로 옮겼다고 이 제한을 제거한 것은 아니다. 새 ID의 기능을 확인하고 필요하면 core 규칙과 세이브 호환성을 함께 확장한다.
- `itemType`, `category`, `phase`, `incrementType`, `argument` 등은 기존 규칙의 숫자 코드다. 새 효과 종류는 데이터와 core의 해석 코드를 함께 추가해야 한다. 새 인물·아이템의 이미지도 별도로 준비해야 한다.
- 테이블 문구에는 기존 `ReviewedKoreanText` 번역 처리가 계속 적용된다. 기존 문구와 동일한 번역 키라면 검수 번역이 표시될 수 있다. 새 문구와 문구별 검수는 localization 리소스와 함께 관리한다.
- 엔진은 YAML이나 게임 ID를 알지 않는다. YAML 파싱과 테이블 생성은 `buildSrc`에서만 수행하며 Android/desktop 실행 의존성에는 YAML 파서를 넣지 않는다.

[기능별 이용 가이드](../docs/engine-core-feature-guide.html) · [스키마](schema.json)

처음 시작한다면 [단계별 YAML 실습](../docs/developer-onboarding.html)을 따른다.

## 시나리오

`scenarios/`는 Python 시나리오 119개, `manifest.json`, `dialogue-text.json`을 소유한다. R_XX는 시나리오, S_XX는 전투이며 common/train 모듈도 함께 번들한다. 대사 ID는 소스 줄 번호와 독립적이다. 이전 줄 번호 키는 `dialogue-text.json`의 `legacyKeys`에 보관한다.

```sh
./gradlew :core:exportScenarioAst :core:processResources
./gradlew :desktop:run --args="--scenario=R_01"
```

새 모듈은 Python 파일뿐 아니라 manifest에도 등록한다. `stage.say('@dialogue:R_00.scene1.dialogue0001')`처럼 대사 ID를 소스에 명시하고, 해당 ID의 한국어 텍스트를 `dialogue-text.json`의 `entries`에 추가한다. 기존 ID는 대사를 이동하거나 앞에 줄을 삽입해도 바꾸지 않는다. AST 내보내기는 누락되거나 중복된 ID와 직접 입력한 대사를 거부한다. AST 생성은 scenarios/compatibility의 로컬 바이트코드로 복원 분기를 보정한다. 원본 비교용 도구는 별도 외부 원본을 유지할 수 있으며, 실행용 시나리오 작성 위치는 이 디렉토리다. YAML 컴파일러는 scenarios를 처리하지 않고 별도 AST 빌드가 담당한다.

고정 ID 작성 절차와 API 계약은 [시나리오 플랫폼 가이드](../docs/scenario-platform-guide.html)를 참고한다.

## 게임 패키지와 효과 매핑

`game.yaml`이 게임 제목·시작/복귀 장면·주인공·저장 namespace·전투 연결을 소유한다. `rules/skill-effects.yaml`은 패시브 효과 이름을 스킬 ID에, `rules/magic-effects.yaml`은 능동 책략의 type/category/spell 이름을 코드에 연결한다. 알고리즘을 새로 정의하는 언어는 아니며 기존 효과를 재배치하는 계약이다.

일반 빌드는 `assets/`의 로컬 자산을 패키징한다. `.rgba.gz`는 빌드에서 `.rgba`로 무손실 해제하고 나머지 파일은 그대로 복사한다. `resources/`는 시스템 문구·번역 등 리소스 경로를 보존한다. 외부 Cocos 프로젝트는 선택적인 원본 비교/수입 작업에만 필요하다. 복원 시나리오 보정 입력은 `scenarios/compatibility/`에 있다.

[게임 패키지 작성 예제](../docs/game-package-guide.html)를 따라 설정·효과·자산을 수정하고 검증한다.

## 게임별 규칙 계약 v1

`rules/game-rules.yaml`의 version/resources/rules가 추가 규칙을 설정한다. 기본값은 비활성 목록이다. `examples/yitian-rules.yaml`은 캐릭터별 내공 회복을 켜는 복사 예제이며 자동 적용되지 않는다. 새 Kotlin 핸들러는 RuleModule로 등록한다. [규칙 확장 가이드](../docs/game-rule-extension-guide.html)에 실제 후크 범위, 저장·난수 계약과 작성 절차를 정리했다.

## 이름 있는 시나리오 전역 변수

`variables/globals.yaml`의 `version: 1`, `variables` 매핑에 `namespace.snake_case` 이름과 `type`(`int`/`bool`), `default`, `description`을 선언한다. 기존 숫자 주소가 필요하면 선택 필드 `legacyId`를 연결한다. 이 파일의 이름은 저장 파일의 영속 키이므로 배포 후 변경하려면 마이그레이션이 필요하다.

시나리오는 `gvars["campaign.current_chapter"]`로 읽고 쓰며 정수에는 `+=`도 사용한다. 선언된 이름의 타입을 엄격히 검사하고 숫자 별칭과 동일 상태를 공유한다. 이름이 없는 기존 숫자 접근도 유지한다. 새 저장은 `namedGlobals` v1에 이름별 값을 기록하고 기존 숫자 저장은 별칭으로 복원한다. 게임 규칙의 `extensionState`와는 별개다.

[전역 변수 작성·시나리오·저장 호환성 가이드](../docs/global-variables-guide.html)에 YAML과 불리언 예제, 복원 오류 규칙, 검증 명령을 정리했다.


## 외부 Lua 전투 규칙

정적 설정으로 충분한 인물·장비는 기존 YAML만 사용한다. 동적 행동은 `rules/lua-rules.yaml`에 스크립트와 이벤트, 선택적인 `binding: {scope: character|equipment, id: ...}`를 등록한다. 바인딩 ID는 인물/장비의 실제 YAML ID이며 장비는 착용 중인 항목만 적용한다. 기본 등록부는 비어 있어 기존 게임 효과를 바꾸지 않는다.

`JOJO_RULES_DIR` 또는 `-Djojo.rules.dir`로 외부 등록부 디렉터리를 지정하면 Lua와 등록 YAML을 새 전투마다 다시 읽는다. `examples/lua/`는 수정해 실행할 수 있는 독립 예제다. 활성 전투는 소스 스냅샷을 유지한다. 전체 게임 데이터·시나리오를 외부에서 로딩하는 기능과는 별개다.

[Lua 작성·장수/장비 연결·실행 가이드](../docs/lua-rules-guide.html)를 참고한다.
