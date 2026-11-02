# 수호전 외전: 임충전 — 조조전 콘텐츠 모드

[플레이 가이드](../../../docs/linchong-campaign.html) · [원안 감사](QA_AUDIT.md)

기존 조조전의 시나리오 실행기·BattleScreen·이동·전투·행동 메뉴를 사용하는 이야기 모드입니다. 별도 캠페인 전투 엔진이나 별도 전투 UI를 사용하지 않습니다.

```sh
./gradlew :desktop:run --args='--scenario=R_90'
```

R_90~R_97은 경로, S_90~S_97은 전투입니다. 순서대로 B01, B02, EX01, B03, B04, B05, EX02, B06에 대응합니다. 원안의 장비·인물·기술을 기존 콘텐츠 YAML에 등록하고, 기존 Python AST 시나리오로 장면을 재생합니다.

## 보존 자료와 제작 결과

| 파일 | 역할 |
|---|---|
| `source.md` | 제공된 전체 PRD 원문 |
| `campaign.json` | 원안의 구조화한 보존 데이터. 게임의 별도 실행기 형식이 아님 |
| `runtime_rules.json` | 원안 승패·지형과 제작 보완을 구조화한 중간 자료 |
| `mechanics.json` | 원안의 무공·장비 효과와 명시한 보완 수치 |
| `supplements.json` | 신규 작성한 연결·선택·동행 거절 대사 |
| `mod-ids.json` | 기존 조조전의 장수·오브젝트·물건·무공·맵·시나리오 ID 매핑 |
| `../../scenarios/R_90.py`~`S_97.py` | 기존 실행기가 처리하는 실제 모드 시나리오 |
| `assets/` | 신규 지도 8장, 초상 24인, 전장 말 16인과 생성 기록 |

진행: `B01 → B02 → [EX01] → B03 → B04 → B05 → [EX02] → B06`. 선택 전투 생략과 영입 거절이 본편을 막아서는 안 됩니다. 양지는 영구 동료가 되지 않습니다.

## 기존 조조전으로 옮긴 규칙

- 목표 안내는 `scripts/build_linchong_mod.py`의 `OBJECTIVE_GUIDES`와 `EVENT_GUIDES`에서 관리합니다. 좌표는 `campaign.json`의 오브젝트 위치를 사용합니다. 시작 시 탈출·보호·확보 지점을 안내하고 `stage.heightLight`로 카메라 이동과 점멸을 재생한 뒤 임충의 시작 위치로 돌아옵니다.
- 야저림·선착장의 탈출 지점은 목표 임무를 안내할 때만 강조합니다. 야저림은 노지심 등장으로 탈출 목표가 활성화될 때 한 번 더 안내하며, 턴 전환이나 일반 행동으로는 강조·카메라 연출을 반복하지 않습니다. 허정 구출과 산신묘의 진오·증거 발견 시에는 해당 위치를 보여줍니다. 표시는 2.4초간의 기존 연출이며 상시 마커가 아닙니다.
- 1-base 좌표를 0-base로 변환합니다.
- 수레·창고·장물 상자는 정지 우군 개체로 표현합니다.
- 조사·구조·결투 신청은 기존 근접 이벤트로 처리합니다. 일기토는 기존 연출 API를 사용합니다.
- 임시 아군은 해당 전장에 직접 배치하고, 영구 동료만 기존 roster에 합류시킵니다.
- 성장·명중·피해·장비·도구는 기존 조조전 규칙과 UI를 사용합니다. PRD의 독립 숙련·도감·무공 슬롯·새 정비 패널은 원안 자료로 남습니다.
- 초기 포박·날씨의 수치 효과 등 원안의 전용 상태를 기존 API가 지원하지 않는 경우 연출과 목표로 각색합니다. 생성 시나리오의 `Existing-engine adaptation` 주석에서 매핑되지 않은 원안 명령을 확인할 수 있습니다.
- 일반 유효 적중 등 직접 콜백이 없는 일부 튜토리얼은 턴 조건으로 옮깁니다. B06의 원안 전용 공격 제한은 동료를 비조작 우군으로 배치해 표현합니다.

## 재생성과 검사

저장소 루트에서 실행합니다.

```sh
python3 scripts/import_linchong.py
python3 scripts/validate_linchong.py
python3 scripts/build_linchong_mod_content.py
python3 scripts/package_linchong_assets.py
python3 tools/verify_linchong_sprites.py
python3 scripts/build_linchong_mod.py
python3 tools/export_python_ast.py content/scenarios /tmp/linchong-native-ast
```

가져오기는 원안 8전투·70개 배치·79이벤트·217이벤트 대사·13조건 대사·34장간 대사를 보존합니다. 모드 생성기는 원안 대사 264개를 기존 고정 대사 카탈로그에 병합하고 R/S 시나리오·manifest·명명 전역 변수·맵 지형을 생성합니다. 생성 뒤 기존 콘텐츠 컴파일과 실제 플레이 검증이 필요합니다. 원안 데이터 검사 통과를 전투 완주 통과로 해석하지 않습니다.

`assets/provenance.json`과 `assets/provenance-support.json`은 실제 생성 도구·프롬프트·파일 경로·아틀라스 순서를 기록합니다. 전투 이미지의 현재 출처와 선정 기록은 `assets/release-sprites/selection.json`에서 관리합니다.

## 구현 경계와 그래픽

`engine/`, `core/`, `desktop/` 및 콘텐츠 컴파일러를 수정하지 않습니다. 실행 내용은 기존 `content/characters`, `equipment`, `skills/active`, `scenarios`, `assets`, `ui/dialogue.yaml`, `game.yaml`에 있습니다. 이 폴더는 원안·생성 원본·변환 기록을 보관하는 제작 자료이며 별도 게임 실행기가 아닙니다.

현재 전투 캐릭터는 `assets/release-sprites/selection.json`에 배정한 사용자 제공 배포 에셋을 사용합니다. 공격 64×792(12프레임), 이동 48×550(11프레임), 특수 48×250(5프레임) PNG를 아바타 601–616에 바이트 그대로 복사합니다. 선정 기준·원본 파일·재적용 명령은 [배포 이미지 가이드](assets/release-sprites/README.md)에 기록했습니다. 검토 페이지는 `docs/linchong-sprite-review.html`이며, 617–620번 물체는 정적 아틀라스를 유지합니다.

검증 명령:

```sh
./gradlew :verification:campaignUnitTest --tests "*LinchongNativeScenarioTest"
python3 scripts/smoke_linchong.py
```

첫 명령은 실제 시나리오 실행기를 이용한 조건·보상·분기 검증입니다. 두 번째는 원래 게임 화면으로 8개 전투 진입과 첫 전투의 적군 턴·노지심 증원까지 확인하고 `verification/build/linchong-smoke`에 화면을 저장합니다. 전 전투의 수동 완주나 난이도 균형 검증을 대체하지 않습니다.

게임의 시스템 안내 9개는 기존 기능에 맞게 각색했습니다. 별도 숙련, 눈길 면역, 장비 교체 행동 비용, 미구현 후속 캠페인 이월을 약속하지 않습니다. 원문은 source.md와 campaign.json에 그대로 보존하며 변환문은 생성기의 NATIVE_HELP에서 확인할 수 있습니다.

배포 스프라이트는 원본 좌표와 투명 여백을 그대로 보존합니다. 이동·특수 48px, 공격 64px의 기준점 차이는 기존 렌더러가 처리하므로 개별 프레임을 다시 맞추거나 확대·축소하지 않습니다. 패키징은 파일 해시·프레임 수·투명 여백·원본과의 바이트 일치를 검사합니다.

3차 시트는 사용자가 제공한 조조전 화면(`../../../docs/assets/jojo-style-reference.png`)을 작화 기준으로 삼았습니다. 작은 머리와 약 3~3.5등신 비율, 절제된 색, 얇은 윤곽선을 공통으로 사용합니다. `style-prompt.txt`와 `provenance-*.json`에 생성 프롬프트와 원본 경로를 보존합니다. 2·3차 생성 시트는 제작 이력으로만 보관하며 현재 런타임에서는 사용자 제공 release 세트를 사용합니다.

턴 제한은 현재 게임 화면의 표시값을 포함하는 기준입니다. 예를 들어 9턴 제한이면 9턴의 모든 진영 행동까지 진행하고, 10턴으로 넘어갈 때 시간 초과 패배로 판정합니다. 시나리오 생성기는 표시값·패배 조건 문구·판정에 같은 값을 사용합니다. 원안 PRD의 턴수는 제작 자료에 그대로 보존합니다.
