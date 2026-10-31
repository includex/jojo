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

일반 빌드의 `:core:processResources`도 자동으로 컴파일한다. 출력은 `core/build/generated/game-content/data/*.bin`이고 번들에서는 `maps/data/`가 된다. 생성물은 편집하지 않는다. 기존 외부 프로젝트의 같은 여섯 테이블은 번들에서 제외하므로 YAML이 실제 실행 데이터의 원본이다. 다른 병종·지형·상점·이미지·음원 및 Python 시나리오는 아직 기존 외부 입력을 사용한다.

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
- `equipment`는 소비 아이템까지 포함한 기존 item 테이블 전체다. `iconIndex`는 원본 인덱스이며 현재 화면용 프로필은 여기에 1을 더한다. `upgradeValue`는 기존 8번 필드로 성장/병과 호환 경로에서 사용된다.
- 현재 소비 아이템 판별의 `150..254`, 255의 미지정 표식, 장비의 compact ID 변환 등은 기존 게임 규칙이다. YAML로 옮겼다고 이 제한을 제거한 것은 아니다. 새 ID의 기능을 확인하고 필요하면 core 규칙과 세이브 호환성을 함께 확장한다.
- `itemType`, `category`, `phase`, `incrementType`, `argument` 등은 기존 규칙의 숫자 코드다. 새 효과 종류는 데이터와 core의 해석 코드를 함께 추가해야 한다. 새 인물·아이템의 이미지도 별도로 준비해야 한다.
- 테이블 문구에는 기존 `ReviewedKoreanText` 번역 처리가 계속 적용된다. 기존 문구와 동일한 번역 키라면 검수 번역이 표시될 수 있다. 새 문구와 문구별 검수는 localization 리소스와 함께 관리한다.
- 엔진은 YAML이나 게임 ID를 알지 않는다. YAML 파싱과 테이블 생성은 `buildSrc`에서만 수행하며 Android/desktop 실행 의존성에는 YAML 파서를 넣지 않는다.

[기능별 이용 가이드](../docs/engine-core-feature-guide.html) · [스키마](schema.json)
