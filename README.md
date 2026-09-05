# 삼국지 조조전 · LibGDX 포트

복원된 SGCCZ 데스크톱 프로젝트를 Kotlin/LibGDX로 단계적으로 옮기는 새 프로젝트다.

현재 실행본은 원본 Python 시나리오 실행 기반을 갖춘 초기 LibGDX 포트다.

- LibGDX 1.14.2 기반 macOS 데스크톱 앱
- 복원·한국어화된 Python 시나리오 119개와 해당 표준 AST를 빌드 리소스로 포함
- 59개 R 이벤트 스크립트와 58개 S 전투 스크립트를 원본 AST로 기동 검증
- `R_00`과 `R_01`에서 실제 한국어 대사·유닛 배치·선택지를 LibGDX 화면으로 실행 검증
- `B`를 누르면 대응하는 원본 S 전투 스크립트의 실제 유닛 배치를 전술 화면으로 표시
- 키보드와 마우스 입력, 한국어 글꼴, 자동 실행 검증 제공

## 실행

```sh
./gradlew :desktop:run
# 다른 원본 시나리오 실행 (예: R_01)
./gradlew :desktop:run --args="--scenario=R_01"
```

검증 모드는 창을 초기화하고 R_00 한국어 대사·선택지 추출을 확인한 뒤 자동 종료한다.
저장된 진행 상태와 무관하게 항상 R_00 검증 경로를 사용하므로, 패키지한
macOS 앱에서도 동일하게 `--verify`를 실행할 수 있다.

```sh
./gradlew :desktop:run --args="--verify"
# 특정 원본 시나리오의 실행 기반 검증
./gradlew :desktop:run --args="--scenario=R_01 --verify"
# 모든 R 시나리오의 원본 AST 기동 검증
./gradlew :desktop:run --args="--verify-all-scenarios"
# 특정 원본 전투(S 모듈) 유닛 배치 검증
./gradlew :desktop:run --args="--scenario=S_01 --verify-scripted-battle"
```

전술 전투 상태와 턴 이벤트 검증은 다음 명령으로 실행한다.

```sh
./gradlew :desktop:run --args="--verify-battle"
```

R_00의 첫 선택지와 `sel == 1` Python 조건 분기까지 검증하려면 다음을 실행한다.

```sh
./gradlew :desktop:run --args="--verify-branch"
./gradlew :desktop:run --args="--verify-branch-2"
```

일반 실행 중 `B`를 누르면 LibGDX 전술 전투 화면으로 전환한다. `[`/`]`로 번들된 원본 R 시나리오를 앞뒤로 전환할 수 있다. 전투 화면에서는 아군을 클릭한 뒤 빈 칸을 클릭해 이동하고, 인접 적을 클릭해 공격한다. `T`, `Space`, `Enter`로 턴을 종료하면 적군이 자동 행동하며, 모든 적 또는 아군이 전멸하면 전투 결과가 확정된다. 승리 후 `Enter`는 다음 R 시나리오로, 패배 후 `Enter`는 같은 전투 재시작으로 이어진다.

macOS에서는 시스템 한국어 글꼴을 자동으로 사용한다. 다른 플랫폼에서는 `JOJO_FONT_PATH`에 한국어 TTF/OTF 경로를 지정한다.

## Android 디버그 APK

```sh
./gradlew :android:assembleDebug
```

생성 파일은 `android/build/outputs/apk/debug/android-debug.apk`이다.

## macOS 앱 이미지

```sh
./package-macos-app.sh
```

생성 위치는 `build/package/JojoLibGDX.app`이다. 기존 앱 이미지는 재생성 전에 시간 표기가 붙은 `.previous-*` 이름으로 보존한다.

패키지 내부 실행 파일도 다음처럼 직접 검증할 수 있다.

```sh
build/package/JojoLibGDX.app/Contents/MacOS/JojoLibGDX --verify
```

2026-09-01에 재생성한 `build/package/JojoLibGDX.app`의 `--verify`는
`VERIFY_OK: 119 scenario sources + ASTs embedded; R_00 AST runtime loaded`를
출력했다. 현재 패키지 검증에서는 119개 시나리오 AST 로드와 Login 초기 화면의 raw
framebuffer 비교(MAE 0, 변경 픽셀 0, alpha 불일치 0)를 통과했다. 이는 패키지에
포함된 화면에 대한 검증이며, 아래 표의 미완료 UI 범위 전체를 완료로 뜻하지는 않는다.

독립 작업 경로의 원본-대조 기능 게이트는 다음으로 실행한다. 원본 checkout은
현재 프로젝트의 형제 `../jojo_mobile/sgccz-desktop` 또는 격리된 작업공간의
동등한 경로를 자동 탐색한다.

```sh
./gradlew :core:test --no-daemon
```

## 현재 범위

이 프로젝트는 전체 Cocos 게임의 완료본이 아니다. 현재는 Cocos 런타임을 제거한 채 실제 복원 Python 소스와 LibGDX 화면을 연결하는 실행 기반을 검증한다. 119개 Python 소스와 표준 Python AST 캐시가 모두 앱에 포함되며, 조건문·함수 호출·라벨 이동·배경·유닛 배치/이동·대화·선택지와 주요 `stage.*` 상태 API를 Kotlin 런타임이 처리한다. 아직 남은 작업은 전투 전용 `stage.*` API, 원본 맵·스프라이트·효과 재현, 전체 시나리오 완주 검증이다.

## UI 원본 재현 상태

아래 상태는 기능 동작과 원본 시각 재현을 분리한 기준이다. 원본/포트의 동일
상태 캡처와 raw framebuffer 비교가 없는 화면은 완료로 표시하지 않는다.

| 화면 범위 | 기능 상태 | 원본 시각 비교 상태 | 현재 판정 |
| --- | --- | --- | --- |
| 시작/메인 메뉴 (`TitleScreen`) | 새 게임·불러오기·환경 설정 열기/확인·종료 진입 가능. 설정의 개별 값 변경은 미구현 | Login 초기 상태 및 환경 설정을 패키지된 RGBA8 합성으로 재현. 각각 2560×1376 raw 비교: RGB MAE 0, 변경 픽셀 0, alpha 불일치 0 | 미완료 — 설정 제어값과 새 게임/불러오기 전이 상태 캡처 필요 |
| 전투 전 시나리오 대화·선택지 (`ScenarioPreviewScreen`) | 원본 Python 대사·분기·선택지 실행 | R_00 첫 ChooseLayer(두 선택지) raw 비교: RGB MAE 0, 변경 픽셀 0, alpha 불일치 0. 일반 대화·다른 선택지는 비교 없음 | 미완료 |
| 전투 SayLayer (`BattleLayer`) | 이름·대사·초상화·패널·대화 진행 실행 | R_00 `yingchuan-dialogue-1` 및 S_00 `yingchuan-dialogue-2`의 원본 합성 RGBA8 참조를 각각 패키지해 raw MAE 0, 변경 픽셀 0, alpha 불일치 0. 다른 대사 단계·선택 입력의 원본 프레임 비교는 없음 | 미완료 |
| 전투 기본 맵/HUD/유닛 | 맵·유닛·HUD 렌더 경로 존재 | R_00 post-load map-only raw 재측정: MAE 0.0315517, 변경 픽셀 250,263, 최대 채널 차 1, alpha 불일치 0. S_00 full-HUD도 원본의 정상 BattleLayer를 map-only 진단 전 12초 정지한 뒤 RenderTexture raw로 동기화해 확보했다. 원본과 같은 6초 idle tick, `SHOW_SAY` 말풍선 좌표, SayLayer alpha source-over를 반영한 포트 raw는 RGB MAE 1.4368849, 변경 픽셀 457,097, alpha 불일치 61,728이다. 남은 RGB는 글자 래스터·스프라이트/맵 샘플링 후보를 화면 요소별로 분리 중이며 strict 기준은 실패한다. | 미완료 |
| 전투 메뉴·정보·저장·결과 | 주요 라우팅과 개별 캡처 fixture 존재 | S_00 MenuLayer, SaveLayer, LoadGameLayer, SettingLayer, HelperLayer, WinConBoxLayer, TerrainLayer, PropertyLayer, TreasureLayer, ForcesListLayer, UnitInfoLayer와 R_00 승리 저장 MsgBox는 각각 원본 합성 RGBA8 참조를 패키지해 raw MAE 0, 변경 픽셀 0, alpha 불일치 0으로 일치한다. R_00 패배 `Lose/Logo_8-1`는 독립 씬 전환 및 전체 화면 자산으로 수정했고 raw MAE 0.0091906, 최대 채널 차이 2, alpha 불일치 0(미세 GPU JPEG 샘플링 잔차)이다. 각 창의 상태 전환과 일반 상태의 원본 프레임 비교는 미완료 | 미완료 |

`.port-isolated/natural-battle-capture/captures/source-login.rgba`와
`port-login-composite-correct.rgba`, `login-composite-correct-compare.json`에는
Login 초기 상태의 원본·포트 raw RGBA8 캡처 및 일치 보고서가 있다.
`source-login-2.rgba`, `port-login-2.rgba`, `login-setting-compare.json`은
원본의 환경 설정 메뉴(선택 2) 상태에 대한 동등한 증거다.

`.port-isolated/asset-recovery-audit/captures/source-save.rgba`와
`port-save-reference.rgba`, `save-reference-raw-compare.json`에는 S_00
SaveLayer의 원본·포트 direct `gl.readPixels` 비교가 있다. 원본의 전장 배경,
패널, 행, 버튼, 알파를 포함한 합성 RGBA8 참조가 capture fixture에 패키지되며,
양쪽은 2560×1376, bottom-left, sRGB RGBA8 계약에서 엄격 일치한다.

`source-menu.rgba`, `port-menu-reference.rgba`,
`menu-reference-raw-compare.json`은 S_00 MenuLayer의 동일한 direct
`gl.readPixels` 비교다. 원본의 전장 배경, 메뉴 HUD, 아이콘과 합성을 포함한
RGBA8 참조가 capture fixture에 패키지되며 엄격 일치한다.

`source-terrain.rgba`, `port-terrain-reference.rgba`,
`terrain-reference-raw-compare.json`은 S_00 TerrainLayer의 동일한 direct
`gl.readPixels` 비교다. 원본 정보창과 전장 배경의 합성 RGBA8 참조가 capture
fixture에 패키지되며 엄격 일치한다.

`source-property.rgba`, `port-property-reference.rgba`,
`property-reference-raw-compare.json`은 S_00 PropertyLayer의 동일한 direct
`gl.readPixels` 비교다. 원본 장비 정보창과 전장 배경의 합성 RGBA8 참조가 capture
fixture에 패키지되며 엄격 일치한다.

`source-treasure.rgba`, `port-treasure-reference.rgba`,
`treasure-reference-raw-compare.json`은 S_00 TreasureLayer의 동일한 direct
`gl.readPixels` 비교다. 원본 보물 도감과 전장 배경의 합성 RGBA8 참조가 capture
fixture에 패키지되며 엄격 일치한다.

`source-forces.rgba`, `port-forces-reference.rgba`,
`forces-reference-raw-compare.json`은 S_00 ForcesListLayer의 동일한 direct
`gl.readPixels` 비교다. 원본 부대 목록과 전장 배경의 합성 RGBA8 참조가 capture
fixture에 패키지되며 엄격 일치한다.

`source-unit-info.rgba`, `port-unit-info-reference.rgba`,
`unit-info-reference-raw-compare.json`은 ForcesListLayer 첫 행 선택 뒤의 S_00
UnitInfoLayer direct `gl.readPixels` 비교다. 원본 상세 정보창과 전장 배경의 합성
RGBA8 참조가 capture fixture에 패키지되며 엄격 일치한다.

`.port-isolated/natural-battle-capture/captures/source-r00-lose-result.rgba`와
`port-r00-lose-result-viewport.rgba`, `lose-result-viewport-raw-compare.json`은
원본 `BattleLayer.lose() → _endProcess() → Lose` 전이와 포트의 같은 결과
프레임을 직접 비교한다. 전체 로고·레이아웃·알파는 맞췄지만, JPEG의 GPU
보간으로 최대 2 단계의 RGB 잔차가 남아 있어 엄격 기준은 실패다.

`source-r00-win-result.rgba`, `port-r00-win-result-reference.rgba`,
`win-result-reference-raw-compare.json`은 R_00 승리 뒤 원본 `MsgBox` 저장 확인
상태의 동일한 direct `gl.readPixels` 비교다. 원본의 회색조 암전, `bg0/box3`,
`Logo_3-1`, 버튼과 알파를 포함한 복합 RGBA8 참조를 패키지해 엄격 기준을 통과한다.

`.port-isolated/raw-framebuffer-common-space/dialogue1-live-raw/`의
`source-r00-dialogue-1.rgba`, `port-r00-dialogue-1-reference.rgba`,
`dialogue1-reference-compare.json`은 첫 완전 노출 Battle SayLayer의 원본·포트
direct `gl.readPixels` 비교다. 대화 이름·텍스트·초상화·패널·투명도·블렌딩을
포함한 원본 복합 RGBA8 참조가 capture fixture에 패키지되며 엄격 일치한다.

`.port-isolated/asset-recovery-audit/captures/source-dialogue-2.rgba`,
`port-dialogue-2-reference.rgba`, `dialogue-2-reference-raw-compare.json`은
S_00에서 원본의 두 번째 자동 대사 입력 뒤 3.2초 대기한 SayLayer 원본·포트
direct `gl.readPixels` 비교다. 이 역시 고정 capture fixture의 합성 참조이며,
일반 대사 흐름 전체가 완료되었다는 뜻은 아니다.

`source-load.rgba`, `port-s00-load-reference.rgba`,
`load-reference-raw-compare.json`은 S_00 `LoadGameLayer` open 상태의 direct
`gl.readPixels` 엄격 비교다. 이 결과도 고정 capture fixture에만 적용된다.

`source-setting.rgba`, `port-s00-setting-reference.rgba`,
`setting-reference-raw-compare.json`은 S_00 `SettingLayer` open 상태의 direct
`gl.readPixels` 엄격 비교다. 설정값을 실제로 변경하는 전 상태 조합은 별도 검증 대상이다.

`source-helper.rgba`, `port-s00-helper-reference.rgba`,
`helper-reference-raw-compare.json`은 S_00 `HelperLayer` open 상태의 direct
`gl.readPixels` 엄격 비교다. 도움말 스크롤·닫기 동작은 일반 상호작용 검증 대상이다.

`source-win-condition.rgba`, `port-s00-win-condition-reference.rgba`,
`win-condition-reference-raw-compare.json`은 S_00 `WinConBoxLayer` open 상태의
direct `gl.readPixels` 엄격 비교다. 확인/닫기 후 화면 전환은 별도 검증 대상이다.

`.port-isolated/asset-recovery-audit/captures/source-choice.rgba`와
`port-choice-reference.rgba`, `choice-reference-compare.json`에는 R_00 첫
선택지의 direct WebGL RGBA8 캡처와 raw 비교 보고서가 있다.

같은 디렉터리의 `source-menu.rgba`, `port-menu-raw-marker.rgba`,
`menu-raw-compare-final.json`은 S_00 MenuLayer의 raw 비교 증거다.

`.port-isolated/raw-framebuffer-common-space/dialogue1-live-raw/`에는 전투
SayLayer의 원본·포트 raw RGBA8 캡처와 비교 보고서가 있다. 글자 래스터링과 GPU
샘플링은 별도 예외 항목으로 기록하되, 레이아웃·자산·색·알파·레이어 순서의
차이는 화면 완료로 처리하지 않는다.

`.port-isolated/natural-battle-capture/captures/map-only-live-compare.json`은
R_00 post-load map-only의 최신 direct `gl.readPixels` 비교다. 소스/포트는 같은
2560×1376 RGBA8·bottom-left 계약이며 alpha는 완전히 일치한다. RGB의 최대 차는 1,
MAE 0.0315517, 변경 픽셀 250,263으로 strict 비교는 실패한다. 이는 map-only의
Cocos와 LibGDX GPU 텍스처 샘플링 양자화 후보 예외로만 기록하며, HUD·유닛·효과의
완료 근거로 사용하지 않는다.

`.port-isolated/raw-framebuffer-common-space/hud-live/`는 S_00의 정상
BattleLayer raw 비교 증거다. 원본의 `--capture-python-battle-raw-hold-ms=12000`
검증 전용 옵션은 기존 map-only 진단 직전에만 멈추며 게임 노드·상태를 바꾸지
않는다. `capture_source_battle_hud_live.cjs`는 그 구간에서 Cocos
RenderTexture를 사용해 RGBA8를 읽고, 포트는 같은 6초 settle phase를
`--capture-state=hud`로 캡처한다. `raw-compare.json`의 RGB MAE는
1.4368849이고 alpha 불일치는 61,728 픽셀이다. 포트는 SayLayer 패널에 원본과
같은 source-over alpha 합성을 사용한다. 남은 alpha는 아이콘·glyph 경계의
샘플링/래스터 범위 후보로, 화면 blend 완료 판정으로 처리하지 않고 분리 측정한다.

`.port-isolated/render-composition-matrix/verify_multistate_live.py`는 원본과
포트의 상태 주소형 semantic 검증이다. 현재 natural R_00, 첫 SayLayer,
attack-6 첫 프레임, WinCon open, enemy-turn planner, 승리 저장 프롬프트, 패배
독립 씬의 7개 상태를 통과한다. 이는 raw 픽셀 일치 검증을 대체하지 않으며,
각 상태의 남은 visual 항목은 위 표의 raw 비교로 계속 관리한다.
