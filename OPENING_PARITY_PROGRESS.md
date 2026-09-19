# Opening parity — 2026-09-19

전체 원본/포트 일치는 아직 미완료. 초반을 작은 실행 단위로 검증한다.
Astra가 계획·검수를, Sol이 캠페인 배치 런처 수정을 담당했다.

## 이번에 확인한 범위

- 현재 작업 트리의 실제 InputProcessor로 Title 새 게임 → R_00 scene1 진입.
- Welcome 원본/포트 fixture 2개 일치. 이것은 화면 전체 일치 증거가 아니다.
- R_00 scene1의 첫 3페이지 화자·본문이 복원 Python 원본과 일치:
  181 `대장님, 서둘러야 해요!`, 0 `알아!`, 157 `잠시만 기다려 주세요!`.
- source SHA-256: `39ec99b2b9ef41645987b045a147518d654ad55808c387b3ef9ae13b42e4a91f`.
- 관측 배경 ID 71, 인물 ID 0/157/181/182. 이 값의 기록만으로 렌더 일치를 주장하지 않는다.
- 캠페인 정책 테스트 43개, 배치 도구 테스트 9개, 대사 대조 도구 테스트 4개 통과.

## 재현

```sh
python3 tools/run_game_campaign_checkpoint_batch.py --checkpoint R_00 --scene-index 1 --timeout-seconds 30 --output build/opening-check/manifest.json
./gradlew :verification:campaignUnitTest
./gradlew :verification:campaignE2e '-PcampaignE2eArgs=--stop=R_00:1 --stop-dialogue-pages=3 --max-seconds=25' -PcampaignE2eOutput=build/opening-dialogue.json
python3 tools/verify_opening_dialogue_prefix.py verification/build/opening-dialogue.json
python3 -m unittest discover -s tools -p 'test_verify_opening_dialogue_prefix.py'
```

`campaignE2eOutput`은 절대 경로를 권장한다. 상대 경로는 JavaExec 작업 디렉터리인
verification 기준이다. 앱의 25초 제한과 별개로 이번 통합 실행은 외부 프로세스
180초 제한을 걸었고, 빌드·테스트·실행이 20초에 종료됐다. 기존 기본 scene 중단은 유지된다.

이번 로컬 원시 증거: `build/reports/opening-20260919/`의
`opening-run.log`, `campaign-r00-1.json`, `dialogue-run.log`, `dialogue-prefix.json`.
원시 파일은 build 산출물이므로 Git에 포함하지 않는다.

## 다음 검증 단위

동일한 첫 대사 구간에서 초상화·화자명·본문·배경·인물 렌더와 프레임 안의 콜백 순서를
대조한다. 이를 맞추기 전에 S00 전체 전투나 다음 장으로
확장하지 않는다. 첫 3페이지 이후 대사, 선택 분기, 전투, 엔딩은 완료 증거가 없다.
기존 작업 트리의 전투·폰트 수정은 이번 화자/본문 검증만으로 검수 완료 처리하지 않는다.

## 첫 이동 관측 및 기존 걷기 증거 정정

기존 `hall_walk_render_events.py`는 동일한 생성 데이터를 원본·게임 파일에 모두 썼다.
이 경로로 만든 과거 green은 실제 포트 이동의 증거로 계산하지 않는다. 이제 원본 참고
데이터만 출력하며, 비교기는 빈 로그와 같은 개수의 서로 다른 로그를 거부한다.
`verify_hall_walk_frame_trace.py`는 checkpoint 사이를 재구성할 뿐 실제 중간 프레임이나
콜백을 관측하지 않는다. 보고서에 `evidenceKind=reconstructed-checkpoints`를 표시한다.
해당 수정 회귀 테스트 5개 통과, Astra 검수 후 `9b8e7fe` push.

새 `scenarioFrames`는 실제 ScenarioScreen probe에서 매 프레임 논리·시각 좌표,
방향·action·visible·이동 시간을 복사한다. 기존 dialogue prefix 제한 안에서만 기록한다.
새 실행 `build/reports/opening-motion-20260919/live.json`에는 196프레임이 있으며,
첫 181번 인물의 단독 이동은 25프레임으로 관측됐다.

- 원본 `R_00.scene1`의 `(40,5)→(40,15)` 직선 이동과 `HallUnit._move2`의 칸당 .04초 계약 대조.
- 관측 완료 0.40000688초, 방향 2/action 20, 보간 중 논리 좌표 `(40,5)` 유지.
- 0·157이 보이는 첫 프레임에서 181의 논리·시각 좌표 `(40,15)` 확정.
- 첫 3페이지 대사 재검증 통과. 캠페인 테스트 44개, opening Python 테스트 7개 통과.
- 빌드·테스트·실제 실행은 외부 120초 제한 아래 14초에 종료.

```sh
# 위 campaignE2e 명령의 출력 파일을 그대로 사용한다.
python3 tools/verify_opening_first_move.py verification/build/opening-dialogue.json
python3 -m unittest discover -s tools -p 'test_verify_opening*.py'
```

**후속 정정 (2026-09-20): 아래의 당시 판정과 위 CLI는 이 문서 끝의 자연 first-tick 검증으로 대체한다. 실제 Cocos 계측에서 첫 프레임 delta 폐기가 확인됐고, 당시 검증의 즉시 누적 가정은 틀렸다.**

첫 직선 이동에서 확정된 게임 동작 차이는 발견하지 못했다. 이 검증은 실제 Cocos의
프레임 스케줄과 직접 동기화한 비교가 아니며, 프레임 안의 idle 콜백·z 순서·픽셀,
이후 그룹의 우회 경로는 아직 증명하지 않는다.

## 첫 대사 이전 그룹 이동 대조

새 원본 도구는 recovered `HallLayer.AStar`와 `HallUnit._move2`를 직접 호출하고
원본 Pmap30 장애물 데이터로 Cocos 이동 명령 목록을 만든다. 실제 Cocos 화면을
실행하는 도구는 아니며, nominal action/경로 계약을 독립적으로 도출한다.
R00의 연속된 원본 구문, 지도 UUID·버전 및 사용 파일 hash로 출처를 확인한다.

`build/reports/opening-group-20260919/`의 fresh `source.json`과 `live.json` 대조:

- 단독 이동: 26프레임, 원본 0.4초.
- 첫 3인 그룹: 23프레임, 모두 원본 0.4초.
- 두 번째 4인 그룹: 52프레임. 181/157은 20칸·0.8초,
  0/182는 앞 인물의 출발 위치를 우회해 22칸·0.88초.
- 원본 특유의 모서리 보간, 이동 방향, 개별 논리 좌표 확정, 전체 완료 후 재개,
  최종 시각 목적지·가시성·idle·스크립트 지정 방향 확인.
- `stage.delay(3)`의 nominal 0.3초 뒤 대사 진입. 이번 관측은 다음 렌더 프레임인
  0.3166663초에 대사로 바뀌었다. 실제 프레임 간격을 기준으로 검사한다.
- 원본 실행/변형 테스트 2개와 opening Python 테스트 10개 통과.
  실제 게임은 첫 대사에서 멈췄고 Gradle 포함 3초에 종료(외부 제한 90초).

```sh
node tools/r00_opening_source_move_harness.js build/opening-source.json
./gradlew :verification:campaignE2e '-PcampaignE2eArgs=--stop=R_00:1 --stop-dialogue-pages=1 --max-seconds=15' -PcampaignE2eOutput=build/opening-group.json
python3 tools/verify_opening_group_moves.py build/opening-source.json verification/build/opening-group.json
```

현재 첫 대사 이전의 세 이동 단위에서 확정된 게임 동작 차이는 없다. 대사 이후 그룹,
실제 Cocos/LibGDX 프레임 스케줄 동기화, 프레임 안 callback, 픽셀 및 음향은
이번 검증에 포함되지 않는다. 이전 절의 “이후 그룹 미증명”은 그 실행 당시의 범위다.

## 첫 대사 패널의 실제 픽셀 대조

기존 `street-panel` 포트 캡처는 임의 회관 장면과 화자 0을 설치했다. 원본 첫 대사는
병사 181이므로 이 두 캡처는 꼬리 방향부터 달랐다. 새 `opening-panel`은 실제 R00의
첫 DIALOGUE를 기다린 뒤 장면을 교체하지 않고 패널만 분리한다. 첫 관측 프레임을
건너뛰어 표시 설정이 적용된 다음 렌더 결과를 읽는다. 포트 캡처 태스크 제한은 30초다.

원본 역시 실제 첫 대사 `대장님, 서둘러야 해요!`의 DialogueLayer에서 다른 구성요소를
숨긴 뒤 WebGL `readPixels`로 캡처했다. 이것은 패널 분리 대조이며 화면 전체 대조가 아니다.

- 원본·포트 모두 2560×1376 bottom-left RGBA8.
- 변경 픽셀 0, R/G/B/A 모두 오차 0.
- 두 raw 파일 SHA256:
  `553aa2207f24cb470c53e15a2c0fb29e2a06467b20a37386762f887939f7e3d7`.
- 로컬 결과: `build/reports/opening-visual-20260919/panel-comparison.json`.
  최종 원본 캡처 메타데이터는 `build/reports/opening-source-panel-final2/`,
  포트 캡처는 `verification/build/verification/opening-panel/`.
- 캠페인 정책 테스트 45개, geometry gate 회귀 5개, RGBA comparator 회귀 2개 통과.
- 추적되는 독립 원본 스크립트 재실행 5.5초, 포트 재실행 3초.
  원본은 새 프로세스의 CDP 페이지와 자연 `EVENT_AFTER_DRAW`를 확인한다.
  양쪽 모두 실패 전에 오래된 캡처 출력을 지워 이전 결과 재사용을 막는다.

```sh
node tools/capture_opening_source_panel.cjs build/opening-source-panel
./gradlew :verification:captureOpeningDialoguePanel
python3 tools/verify_opening_panel_pixels.py build/opening-source-panel/source-street-panel.rgba verification/build/verification/opening-panel/game-panel.rgba
```

기존 geometry 도구는 패널의 절대 위치·크기를 무시하는 false green을 수정했다
(`dbcf6c3`). 그 도구의 통과는 위치·크기만 의미하며 픽셀 일치는 위 strict RGBA
comparator로 따로 판정한다. 전체 자연 화면 합성, 초상화, 텍스트, 음향은 여전히 남아 있다.


## 첫 대사 패널 + 병사 초상화의 실제 픽셀 대조

`opening-portrait`은 자연스럽게 도달한 첫 R00 대사에서 패널과 초상화를 함께
분리한다. 원본은 병사 181의 frame 이름, 원본 크기 192×240, 텍스처 로딩 및
노드 크기 96×120을 확인한 후 자연 `EVENT_AFTER_DRAW`에서 읽는다.
기본 prefab 초상화가 남은 로딩 중 프레임은 거부한다.

- 원본·포트 2560×1376 bottom-left RGBA8, 변경 픽셀 0(R/G/B/A 모두 동일).
- 두 raw SHA256: `f17faae819ca2b4e9753f51ece04e01fd6b2ebe8841a28b7fd707c13c0785a78`.
- 원본 메타데이터와 비교 결과: `build/reports/opening-portrait-20260919/`.
- 포트 캡처: `verification/build/verification/opening-portrait/`.
- 원본 새 실행 약 2.5초, 포트 캡처와 캠페인 테스트 실행 4초.
- 원본 기본 panel 모드 회귀 캡처의 SHA256은 기존 `553aa220…e3d7`과 동일.
- 캠페인 정책 테스트 및 strict RGBA comparator 테스트 통과.

```sh
JOJO_CAPTURE_STAGE=portrait node tools/capture_opening_source_panel.cjs build/opening-source-portrait
./gradlew :verification:captureOpeningDialoguePortrait :verification:campaignUnitTest
python3 tools/verify_opening_panel_pixels.py build/opening-source-portrait/source-street-portrait.rgba verification/build/verification/opening-portrait/game-portrait.rgba --stage portrait
```

이번 단위는 캡처·검증 확장이다. 이 범위에서 렌더 차이는 발견되지 않았다.
첫 대사의 화자명·본문 픽셀, 전체 화면 합성 및 음향 대조는 아직 남아 있다.


## 첫 화자명 차이 확인 및 선형 필터 수정 (2026-09-20)

첫 자연 대사의 `opening-speaker` 누적 단계(패널+초상화+화자명)를 추가했다.
원본은 `병사 ` 문자열과 실제 GL 업로드된 라벨 텍스처를 확인한 뒤 캡처한다.
원본 Label은 Arial 36px, lineHeight 40, outline 2px, center 정렬이며,
노드는 76.28×54.4, 캔버스 텍스처는 76×54다.

화자명은 **아직 픽셀 일치하지 않는다**. 원본 Cocos Texture2D는 기본 선형
필터인데 포트의 streetSpeakerFont atlas는 기본 최근접 필터였다. 해당 atlas만
선형 필터로 수정했다. Astra가 원본 CCLabel/CCTexture2D 경로를 검수했다.

- 원본 raw SHA256: `bdd41d0e9e094fa5661ed6963d1c63d2d01572b55a92ef22ab2da642b61d650d`.
- 수정 전/후 RGBA 절대오차 합: 485018 → 434885 (약 10.3% 감소).
- 수정 전/후 RGBA 제곱오차 합: 46695288 → 33848503 (약 27.5% 감소).
- 서로 다른 픽셀 수는 11624 → 11863. 선형 보간으로 작은 차이가 생긴 픽셀까지
  포함되므로 일치율이 개선됐다고 주장하지 않는다. strict gate는 계속 실패한다.
- 차이 범위는 bottom-left [727,343,857,436), 원본 라벨 사각형 안이다.
- 원본 ttf.js는 outline 색으로 alpha 약 1/255의 배경을 먼저 칠한다.
  수정 전 6786픽셀은 원본 (178,179,179), 포트 (179,179,179)의 배경 차이다.
- fresh 패널+초상화 회귀는 픽셀 차이 0, 기존 SHA 유지.
- 캠페인 정책 테스트와 RGBA comparator 3개 테스트 통과.
- 로컬 증거: `build/reports/opening-speaker-20260919/`의 source metadata,
  before.json, linear.json, error-metrics.json, 전후 crop.
  원본/포트 캡처는 각각 수초, 재빌드 포함 최대 10초에 종료했다.

```sh
JOJO_CAPTURE_STAGE=speaker node tools/capture_opening_source_panel.cjs build/opening-source-speaker
./gradlew :verification:captureOpeningDialogueSpeaker :verification:campaignUnitTest
# 현재 불일치를 올바르게 보고하며 exit 1을 반환한다.
python3 tools/verify_opening_panel_pixels.py build/opening-source-speaker/source-street-speaker.rgba verification/build/verification/opening-speaker/game-speaker.rgba --stage speaker
```

다음 수정은 원본 해상도의 문자열 래스터·기준선·외곽선·저알파 배경을 일반
라벨 렌더 경로로 재현하는 것이다. 현재 31px와 X축 경험 보정은 아직 남아 있다.
캡처한 글자 이미지를 특정 대사에 덮어쓰는 방법은 사용하지 않았다.
본문, 전체 화면 합성, 음향 및 이후 게임 전체의 동등성도 계속 미완료다.


## 원본 Canvas 규칙으로 첫 화자명 픽셀 일치 (2026-09-20)

앞 절에 남았던 첫 화자명 불일치를 수정했다. 단일 FreeType 글꼴에 36px와
0.86 배율만 적용한 실험은 오차가 커져 채택하지 않았다. 원본은 `36px Arial`에서
한글 fallback과 Arial 공백 폭을 혼합하므로 문자열 전체의 Canvas 래스터가 필요하다.

새 `export_street_speaker_labels.cjs`는 원본 unit.bin에서 숫자 이후를 제거한
214개 고유 화자명을 동일한 Canvas 규칙으로 생성한다. 화면 캡처나 잘라낸 글자
이미지를 입력으로 쓰지 않는다. 임의의 단일 행 이름 카탈로그에도 같은 생성 규칙을
적용한다. 글자별·화자별 좌표 보정도 없다.

- 36px Arial, lineHeight 40, outline 2px, center 정렬과 원본 저알파 배경.
- 원본은 canvas 크기 재할당으로 context 상태가 초기화되어 실제 lineJoin이
  `miter`다. 초기 pool의 `round` 설정을 그대로 쓰면 외곽선이 달라졌다.
- Canvas→WebGL 업로드(false premultiply/flip) 후 RGBA를 PNG로 직접 인코딩한다.
  Canvas.toDataURL은 원본 GL 텍스처와 8픽셀의 red ±1 반올림 차이가 있었으며,
  이 GPU 업로드 경로로 같은 바이트를 얻었다.
- float 노드 76.28×54.4에 anchor(0,.5)를 적용하되, 실제 quad는 정수 캔버스
  76×54를 사용한다. 양쪽 Label 노드 위치는 원본 runtime에서 별도로 확인했다.
- 런타임은 화자명별 텍스처를 지연 로드하고 화면 종료 시 해제한다.
  생성 목록 밖의 이름은 로그를 남기고 기존 글꼴을 사용하므로 그 경우는 미검증이다.
- 생성기는 검증된 OS/아키텍처·Electron·폰트 및 원본 코드 hash가 달라지면 실패한다.
  Gradle도 원본 코드·생성 계약·폰트·실행 환경을 입력으로 추적한다.

최종 fresh 캡처 결과:

- 첫 대사의 패널+초상화+화자명 2560×1376 RGBA 차이 **0픽셀**.
- 원본·포트 SHA256: `bdd41d0e9e094fa5661ed6963d1c63d2d01572b55a92ef22ab2da642b61d650d`.
- 병사 라벨의 생성 PNG를 디코딩한 RGBA도 실제 원본 GL 텍스처와 바이트 단위 동일.
- 패널+초상화 회귀 역시 0픽셀, 기존 `f17faae8…5a78` 유지.
- 캠페인 정책 테스트, 214개 PNG의 크기·고유 이름·hash 검사 통과.
- 생성 계약 변경 시 기존 출력 보존/실패 테스트(원본 hash·폰트 hash 변조) 통과.
- Astra 최종 검수 승인. 생성+게임 캡처+캠페인 테스트는 최종 16초에 완료.
- 로컬 증거: `build/reports/opening-speaker-20260920/final.json`,
  `portrait-regression.json`, `source-label-contract.json`, `source-texture/`.

```sh
./gradlew :core:exportStreetSpeakerLabels :verification:captureOpeningDialogueSpeaker :verification:campaignUnitTest
python3 tools/verify_opening_panel_pixels.py build/reports/opening-speaker-20260919/source/source-street-speaker.rgba verification/build/verification/opening-speaker/game-speaker.rgba --stage speaker
python3 -m unittest discover -s tools -p test_street_speaker_label_contract.py
```

214개 이름을 생성한 것은 214개 실제 대사 화면 전체의 일치를 검증했다는 뜻은 아니다.
다른 화자·좌우 배치의 실제 대사, 목록 밖 변경 이름, 본문, 전체 화면 합성,
음향 및 이후 게임은 계속 검증해야 한다. 다음 단위는 첫 대사의 본문이다.


## 첫 본문의 자연 공개 완료 캡처와 필터 수정 (2026-09-20)

`opening-text` 누적 캡처를 추가했다. 기존 STREET 표시 모드는 본문을 즉시 공개하므로
그 모드부터 켜면 자연 글자 공개 완료를 증명할 수 없었다. 새 runtime 읽기 필드로
실제 `dialogueVisibleText`와 세션 `textComplete`를 전달하고, 캡처 driver는 자연 완료
전까지 표시 전환 명령을 보내지 않는다. Observer도 전체 본문을 검사한 뒤 한 프레임을
건너뛰고 분리 렌더를 읽는다. 임의 입력이나 RevealAll로 완료시키지 않는다.

포트 실행 로그의 자연 완료는 경과 약 2.52초, 본문 `대장님, 서둘러야 해요!`다.
원본은 실제 RichText 문자열과 child Label 문자열 결합, GL 업로드 상태를 확인한다.
기존 `--verify-python-source` 자동 진행은 남은 글자를 즉시 공개하는 것으로 확인됐다.
새 text 캡처는 자동 진행이 없는 `--verify-desktop`의 새 프로필에서 원본 Login
시작 이벤트를 한 번 보낸다(기존 bootstrap과 같은 entitlement gate 우회 포함).
최종 fresh 실행은 그 뒤 R00가 자동 시작되어 시나리오 행 클릭은 0회였다.
Hall이라는 씬 이름만으로 선택 대기 상태를 단정하지 않고 실제 대사 노드로 진입을 판정한다.
대사에는 입력하지 않고 `_handle == null`, `_nextString == ""`, 전체 본문 일치를 기다린다.
원본 검증기의 5초 뒤 자체 screenshot/exit는 별도 Electron entry wrapper에서
capturePage 완료만 유예한다. Cocos 타이머·게임 입력 처리는 바꾸지 않으며 외부
55초 종료 제한은 유지한다. 기존 강제 공개 캡처와 새 자연 완료 캡처의 픽셀이 같더라도
완료 경로의 근거는 구분한다.
이것은 전체 공개된 시점의 픽셀 검증이며, 중간 글자 공개 속도가 같다는 증거는 아니다.

원본 첫 본문은 Arial 36px·lineHeight 42·검정색·outline 없음인 Label segment 하나다.
RichText anchor는 (0,1), segment anchor는 (0,0)이다. segment node는 320.27×52.92,
실제 texture는 320×52이며 선형 필터를 사용한다. 포트 본문 atlas의 최근접 필터를
선형 필터로 수정했다. 기존 31px 및 경험적 X/Y 배율은 아직 남아 있다.

- 원본 full dialogue SHA256: `b4b95be614f7a57260628d5b65e45a6d768cff34b475568f58542a194f5daaf3`.
- 본문은 **미일치**: 수정 전/후 다른 픽셀 수 48286 → 48634.
- RGBA 절대오차 합 5036685 → 4720092 (약 6.3% 감소).
- RGBA 제곱오차 합 696052617 → 586264860 (약 15.8% 감소).
- 차이 범위 bottom-left [682,265,1233,354), 본문 segment 영역 안이다.
  Strict RGBA gate는 계속 exit 1을 반환한다. 오차 감소를 완전 일치로 취급하지 않는다.
- 첫 패널+초상화+화자명 회귀는 0픽셀, `bdd41d0e…650d` 유지.
- 캡처 정책 테스트는 DELAY·공개 중에 명령 없음, 자연 완료 후에만 빈 fixture의
  Present(TEXT)를 한 번 보내는 것을 확인한다. 캠페인 정책 및 comparator 테스트 통과.
- 캡처/캠페인 테스트 실행 11~12초, 화자명 회귀 3초(외부 제한 각 60초).
- 로컬 결과: `build/reports/opening-text-20260920/`의 before.json, linear.json,
  source metadata, linear.log, source-body.png, linear-body.png, speaker-regression.json.

```sh
JOJO_CAPTURE_STAGE=text node tools/capture_opening_source_panel.cjs build/opening-source-text
./gradlew :verification:captureOpeningDialogueText :verification:campaignUnitTest
# 현재 본문 불일치를 보고하며 exit 1을 반환한다.
python3 tools/verify_opening_panel_pixels.py build/opening-source-text/source-street-text.rgba verification/build/verification/opening-text/game-text.rgba --stage text
```

다음에는 화자명에서 검증한 Canvas→WebGL→PNG 경로를 본문의 RichText 규칙으로
확장해야 한다. 표시 중인 prefix마다 줄바꿈과 segment를 계산해야 하므로 완성된
문장 이미지를 단순히 잘라 공개하는 방식은 사용하지 않는다. 본문 완전 일치,
중간 공개 과정, 다른 대사, 전체 화면·음향·이후 게임 검증은 계속 남아 있다.


## 첫 본문 RichText 자산 경로와 RGBA 일치 (2026-09-20)

첫 대사의 자연 공개 완료 뒤 패널·초상화·화자명·본문을 합친 분리 화면이
2560×1376 bottom-left RGBA8 전체에서 일치했다. source/game SHA256은 모두
`b4b95be614f7a57260628d5b65e45a6d768cff34b475568f58542a194f5daaf3`이다.
본문 변경 픽셀·절대오차·제곱오차는 모두 0이며 strict comparator exit 0이다.
직전 Linear-only 결과의 변경 픽셀 48634 / 절대오차 4720092가 해결됐다.
기존 패널+초상화+화자명 회귀도 변경 픽셀 0, `bdd41d0e…650d`를 유지했다.

`export_street_body_labels.cjs`는 R_00 scene1의 처음 두 say 호출에 포함된 첫 세 페이지를
AST에서 추출한다. 초기 적용 범위는 `대장님, 서둘러야 해요!`, `알아!`,
`잠시만 기다려 주세요!`의 모든 28개 비어 있지 않은 공개 prefix다.
각 prefix를 독립적으로 원본 실행 엔진의 cc.RichText에 전달해 segment 배치와
업로드된 Label 텍스처를 생성한다. 완료 이미지 crop이나 화면 캡처를 자산으로 쓰지 않는다.
PNG는 GL readPixels의 RGBA를 손실 없이 저장하며 파일 digest로 중복을 제거한다.
원본 분석 소스뿐 아니라 실제 실행 bundle `web/cocos2d-js.js`, 폰트·Electron·OS도
생성 계약으로 고정한다. 생성 제한은 55초, Gradle 태스크 제한은 60초다.

포트는 RichText top-left 기준 segment 좌표에 .86 배율을 적용하고 integer texture
크기를 그대로 그린다. float node 크기로 texture를 늘리지 않는다. 첫 segment는
상대 위치 (0,-52.92), node 320.27×52.92, texture 320×52다.
생성 자산은 필요할 때 로드하고 화면 종료 시 해제한다. 자산에 없는 prefix는 로그를
남기고 기존 글꼴로 표시하므로 해당 범위의 픽셀 일치는 아직 보장하지 않는다.

본문·화자명 캡처와 campaignUnitTest는 함께 19초에 끝났다. 본문 자연 완료 로그는
약 2.53초다. 로컬 증거는 `build/reports/opening-body-canvas-20260920/`의
`capture.log`, `text.json`, `speaker.json`이다.
추가 생성기 검사에서 폭 80의 `가나AB 다라`는 세 줄(높이 136.92)로 나뉘며
segment bounds가 폭 안에 있음을 확인했다. 결과는
`build/reports/street-body-label-width80-probe/manifest.json`에 있다.
현재 생성기는 plain BMP 문자 입력만 지원하며 markup·surrogate 입력은 명시적으로 거부한다.

```sh
./gradlew :verification:captureOpeningDialogueText :verification:captureOpeningDialogueSpeaker :verification:campaignUnitTest
python3 tools/verify_opening_panel_pixels.py build/reports/opening-text-20260920/source/source-street-text.rgba verification/build/verification/opening-text/game-text.rgba --stage text
```

이 결과는 **첫 문장의 자연 완료 시점에 분리한 대사창**에 한정된다. 모든 prefix를
생성했다는 사실은 실제 중간 공개 프레임·공개 속도·두 번째와 세 번째 화면의 일치 증거가
아니다. 다음 단위는 첫 문장의 중간 공개와 다음 대사 화면 검증이며, 전체 배경·인물 합성,
음향·입력·이후 게임 전체의 동등성은 계속 확인해야 한다.

## 첫 문장의 자연 공개 중간 프레임과 정점 정밀도 (2026-09-20)

`STREET_NATURAL`은 실제 첫 대사에 도달한 뒤 화면만 분리한다. 기존 STREET fixture의
즉시 공개, DELAY/MODAL 건너뛰기, 애니메이션 완료 처리를 실행하지 않는다.
포트 observer는 렌더 후 probe에서 이 표시 모드와 실제 문자열·완료 여부를 확인한다.
원본은 Login 시작 전에 frame hook을 설치하고 실제 Label 업로드와 문구를 확인한 뒤
EVENT_AFTER_DRAW에서 읽는다. 양쪽 모두 대사 입력은 0회다. 원본의 Login 준비 후
1.5초 대기는 시작 이벤트 전 bootstrap 안정화를 위한 것이며 대사 timer는 바꾸지 않는다.

첫 문장 13글자 중 길이 5(끝 공백 포함), 9, 13의 세 표본을 수집했다.
원본 5/9 표본은 scheduler active 및 남은 suffix를, 13은 handle null·빈 suffix를
확인했다. 비교기는 표본 누락/중복, 순서, SHA, natural 표시 모드, source scheduler와
실제 RichText/segment 내용까지 검사한다. 강제 공개 결과는 통과시키지 않는다.

- 길이 5 `대장님, `: 변경 픽셀 0, SHA `6d2f445af9ead2962a01c181056fe82c57c122abc75856f13fed9ce717d7e37d`.
- 길이 9 `대장님, 서둘러야`: **미일치**. 최초 22픽셀·RGBA 절대오차 66에서
  수정 후 13픽셀·절대오차 39로 감소했다. 잔여 범위 bottom-left [831,321,832,334),
  RGB 각 채널 차이 1이며 strict comparator는 계속 exit 1이다.
- 길이 13: 변경 픽셀 0, 완료 화면 SHA `b4b95be6…af3` 유지.

원본에서 직접 읽은 9글자 GL texture(237×52)는 생성 PNG를 해제한 RGBA와 바이트 단위로
같다(raw SHA `2954017d624ccf2fb1e996abd3600d778b2bfdb24784eb3117f0e019177132a1`).
따라서 잔여 문제를 글자 자산이 아닌 화면 변환·샘플링 단계로 좁혔다.
원본 actual assembler는 정수 texture 크기를 double world 위치에 더한 뒤 각 corner를
Float32로 저장한다. 이 계산 순서를 포트에서도 유지하고, 원본의 BR↔TL 대각선으로
quad를 나누도록 수정했다. 특정 문자열·폭·픽셀에 대한 보정값은 사용하지 않는다.
스케일은 batch의 combined matrix에 반영하며 원래 transform을 finally에서 복원한다.

원본 실제 shader uniform의 X/Y 계수는 .0013437500456348062/.0024999999441206455,
translation은 -1/-1이다. source sampler는 LINEAR/CLAMP_TO_EDGE이며 shader는 highp다.
AFTER_DRAW current program은 마지막 body draw임이 입증되지 않았으므로 generic sprite
shader 상태 증거로만 사용한다. 13픽셀의 세부 원인은 아직 미확정이다.

증거 디렉터리는 `build/reports/opening-prefixes-20260920/`이다.
`comparison.json`은 수정 전, `source-coordinates-comparison.json`은 최종 결과다.
`source/`, `source-vertices-camera/`, `source-uniforms/`, `source-shaders/`에 실제 원본
프레임·texture·정점·카메라·shader 자료를 보존했다. 포트 표본은
`verification/build/verification/opening-prefixes/`에 있다.
소스 캡처는 약 9초, 포트 캡처는 빌드 포함 3~11초였으며 외부 60초 제한을 유지했다.
캡처 자체가 실행 간격에 영향을 주므로 시간 기록을 양쪽 공개 속도의 동등성 증거로 쓰지 않는다.
최종 화자명 회귀는 0픽셀 차이를 유지했고 campaignUnitTest 및 opening Python 검사 17개가 통과했다.

```sh
node tools/capture_opening_source_prefixes.cjs build/opening-source-prefixes
./gradlew :verification:captureOpeningDialoguePrefixes
# 현재 9글자 표본의 13픽셀 차이 때문에 exit 1이 정상적으로 반환된다.
python3 tools/verify_opening_prefix_pixels.py build/opening-source-prefixes/source-prefixes.json verification/build/verification/opening-prefixes/game-prefixes.json
python3 -m unittest discover -s tools -p test_verify_opening_prefix_pixels.py
```

다음 단위는 9글자 표본의 남은 화면 샘플링 차이를 분리하는 것이다. 해결 후 첫 문장
전체 공개 단계와 다음 두 대사 화면으로 확장한다. 모든 prefix·공개 속도·전체 화면·음향·
게임 전체의 일치는 여전히 미검증이다.

## RGBA8 장면 합성으로 중간 프레임 13픽셀 차이 해결 (2026-09-20)

이전 절에서 남았던 9글자 표본의 13픽셀 차이를 해결했다. 첫 문장의 길이 5·9·13
세 표본 모두 2560×1376 RGBA 전체가 원본과 일치하며 strict comparator exit 0이다.
9글자의 source/game SHA는 `4a5f98b4663247280daa0862ea47f1751acee8ddbb9f7ae13185ca484792bdc7`이다.
화자명 회귀도 0픽셀 차이를 유지했다.

원인 분리는 다음 근거로 진행했다. 본문에만 흰색/texture-only 진단 shader를 적용해도
차이는 같았으므로 vertex 색 곱·alpha correction을 원인으로 취급하지 않았다.
원래 본문 draw 직후 default framebuffer의 문제 구간은 RGB 54였다.
동일 정점·texture·shader를 RGBA8 FBO에 같은 RGB 179 배경과 합성하면 원본처럼 53이었다.
후속 draw가 아니라 render target에 따라 관측되는 래스터 결과 차이로 좁혔다.
이것은 GPU 내부 구현의 세부 원인까지 확정했다는 뜻은 아니다.

`ScenarioFrameTarget`은 모든 ScenarioScreen 장면을 물리 backbuffer 크기의 RGBA8 FBO에
먼저 합성한다. 결과는 NEAREST·1:1 크기·blending 없이 화면에 복사한다. 화면 캡처나
특정 대사의 보정 이미지를 사용하지 않으며, 실제 장면의 정상 렌더러가 FBO에 그린다.
특정 문자열·237px 폭·문제 픽셀에 대한 분기가 없다. 임시 shader/ROI/native replay
계측 코드는 production에서 제거했다. 렌더 타깃 하나와 전체 화면 복사 pass가 추가된다.

FrameBuffer 크기가 달라지면 새 자원을 만든 뒤 이전 자원을 해제하고, 화면 종료 시
FBO와 복사용 SpriteBatch를 해제한다. 기존 framebuffer/viewport는 생성 전에 저장하고
생성·그리기·복사 실패 경로에서도 복원한다. 원래 장면의 viewport 배치와 입력 계산은
유지하며 FrameBuffer texture의 Y축 방향은 복사 시 처리한다.

실제 창 크기를 줄였다 복원하는 `captureOpeningDialoguePrefixesAfterResize`를 추가했다.
관측된 backbuffer는 1280×688 → 2560×1376이며, 복원 후 세 표본 모두 원본과 0픽셀 차이다.
일반 prefix·화자명·campaign 검사는 14초, resize 검사는 빌드 포함 11초에 끝났다.
관련 Python 검사 17개도 통과했다. 각 실행은 외부 60초 제한을 유지했다.

원본 계측에는 `JOJO_CAPTURE_DRAW_STATE=1` opt-in을 추가했다. 실제 본문 texture identity와
9글자 문자열을 함께 확인한 draw call에서 uniform·shader·blend·sampler 상태를 기록한다.
기존 AFTER_DRAW 마지막 program 관측의 한계를 보완하며 기본 캡처에서는 hook이 비활성이다.

로컬 증거는 `build/reports/opening-sampling-20260920/`의 `final-comparison.json`,
`resize-comparison.json`, `speaker-regression.json`, `game-body-immediate.rgba`,
`game-replayed-blend.rgba` 및 실행 로그다. 원본 draw 상태는
`build/reports/opening-prefixes-20260920/source-draw-state/`에 있다.

```sh
./gradlew :verification:captureOpeningDialoguePrefixes :verification:captureOpeningDialoguePrefixesAfterResize :verification:captureOpeningDialogueSpeaker :verification:campaignUnitTest
python3 tools/verify_opening_prefix_pixels.py build/reports/opening-prefixes-20260920/source/source-prefixes.json verification/build/verification/opening-prefixes/game-prefixes.json
python3 tools/verify_opening_prefix_pixels.py build/reports/opening-prefixes-20260920/source/source-prefixes.json verification/build/verification/opening-prefixes-resize/game-prefixes.json
```

다음은 첫 문장의 나머지 공개 단계와 다음 두 대사 화면이다. 이번 세 표본과 resize
회귀의 일치를 모든 prefix·타이핑 속도·다른 시나리오 화면·음향·전투·게임 전체의
동등성으로 확대하지 않는다. 전체 목표는 계속 진행 중이다.

## 첫 대사 전체 13개 자연 출력 문자열 검증 (2026-09-20)

첫 병사의 `대장님, 서둘러야 해요!`가 공개되는 1~13글자 모든 비어 있지 않은
문자열을 실제 원본과 포트에서 관찰했다. 2560×1376 bottom-left RGBA8 비교 결과
13개 모두 변경 픽셀 0이며 SHA-256도 각각 같다. 공백으로 끝나는 5·10글자도 포함한다.
기존 RGBA8 장면 합성 수정 이후 추가 production 보정 없이 통과했다.

원본은 `JOJO_CAPTURE_ALL_PREFIXES=1`, 포트는
`:verification:captureOpeningDialoguePrefixesAll`로 전체 범위를 선택한다.
기본 5·9·13 표본 경로는 유지한다. 양쪽 manifest에 `sampleLengths`를 기록하며,
비교기는 양쪽 범위 일치·정확한 행 순서·누락·중복·완성 상태와 기존 자연 재생
provenance를 확인한다. `--require-all`은 3개 표본만 있는 결과를 거부한다.
첫 글자 삭제, 공백 prefix 삭제, 중복, 역순, 표본만 있는 manifest, 양쪽 범위 불일치
검사를 추가했고 관련 opening Python 검사 19개가 통과했다.

원본 캡처는 11.45초, 포트는 빌드 포함 11초에 끝났다. 원본 frame 350~362,
포트 frame 119~149에서 각각 13개를 수집했다. 강제 텍스트 설정·공개·대사 입력이나
clock 정지 없이 자연 scheduler를 관찰했다. 각 실행은 기존 외부 60초 제한을 유지했다.
캡처 readback의 부하가 실행 간격에 영향을 주므로 두 frame/시간열은 타이핑 속도
동등성의 증거가 아니다. 검증 대상은 첫 대사의 13개 문자열별 고립 렌더 결과다.

증거는 `build/reports/opening-all-prefixes-20260920/`의 `source/`, `game-capture.log`,
`comparison.json`과 `verification/build/verification/opening-prefixes-all/`에 있다.

```sh
JOJO_CAPTURE_ALL_PREFIXES=1 node tools/capture_opening_source_prefixes.cjs build/reports/opening-all-prefixes-20260920/source
./gradlew :verification:captureOpeningDialoguePrefixesAll
python3 tools/verify_opening_prefix_pixels.py build/reports/opening-all-prefixes-20260920/source/source-prefixes.json verification/build/verification/opening-prefixes-all/game-prefixes.json --require-all --report build/reports/opening-all-prefixes-20260920/comparison.json
python3 -m unittest discover -s tools -p 'test_verify_opening*py'
```

다음 단위는 정상 입력으로 넘긴 다음 두 페이지의 화자·본문·배치다. 첫 대사의
문자열별 일치를 입력 전환·타이핑 속도·전체 화면·음향·전투·게임 전체의 일치로
확대하지 않는다. 전체 목표는 계속 진행 중이다.

## 정상 진행 입력 뒤 첫 세 페이지 비교와 초상화 잔차 발견 (2026-09-20)

첫 대사 자연 완료 뒤 정상 입력으로 두 페이지를 더 열어 비교했다. 첫 페이지
`대장님, 서둘러야 해요!`와 두 번째 `알아!`는 각각 전체 RGBA 0픽셀 차이다.
세 번째 `잠시만 기다려 주세요!`는 초상화 영역에 92픽셀 차이가 남는다.
차이 bbox는 bottom-left `[2106,121,2223,478]`, RGBA 절대오차 합은 150이며,
차이가 난 채널은 각각 1 차이다. comparator는 이를 실패(exit 1)로 반환한다.

- page 1 source/game: `b4b95be614f7a57260628d5b65e45a6d768cff34b475568f58542a194f5daaf3`
- page 2 source/game: `2c6e8d48d59b18ddb189ae0d150394d8cfa155479ff4649bbacccf321952a330`
- page 3 source: `246313e50fca4eca920063c4734d283dbeebab95255457e884449b043e9e118f`
- page 3 game: `ca2512b3f22111187a6333ec44d1aeb52207444b7d737c2171fc35a2626d8f72`

원본은 실제 Panel_cancel 중앙을 CDP pointer로 눌렀고 포트는 설치된 실제
InputProcessor에 SPACE keyDown/keyUp을 보냈다. 양쪽 모두 완료 화면을 캡처한 뒤
다음 프레임 이후에만 입력하며 전체 입력은 2회다. 각 페이지에서 자연 부분 문자열을
관찰하고 강제 RevealAll·interpreter 직결·이동 완료·delay skip을 사용하지 않는다.
2페이지 뒤 이동과 지연을 거쳐 3페이지에 도달한다. 이것은 두 게임의 입력 지연이나
동작 시간 동등성을 검증했다는 뜻은 아니다.

Astra 검수에서 원본 화자 ID의 기대값 기록과 새 DialogueLayer isolation 문제를
찾아 보강했다. ID는 actual constructor.s_lastId를 관측하고 표시명·face도 assert한다.
원본 새 layer를 감지하면 isolation을 재적용한 뒤 이후 AFTER_DRAW에서 캡처한다.
양쪽 bg를 보존해 원본 자체의 right→left→right 전환을 유지한다. 포트도 실제
viewState의 좌우·상하 배치를 읽기 전용 probe로 기록한다. 비교기는 실제 화자
181→0→157, side right→left→right, 3페이지 coverage, 부분 문자열, 완료 상태,
2회 입력의 프레임·시간 순서와 구체적인 입력 경로를 검사한다.

진단으로 초상화 quad의 대각선과 Cocos world Float32 정점→camera scale 순서를
각각 맞춰 봤으나 92픽셀 차이가 변하지 않았다. 효과 없는 renderer 실험은 모두
되돌렸다. 원본 face214는 atlas rect `[1129,2,192,240]`를 사용하고 포트는 별도
head PNG를 사용하므로 다음 단위는 실제 atlas texture/UV와 개별 PNG 샘플링을
분리한다. 현재 이 차이를 원인으로 확정한 것은 아니다.

최종 source 증거는 `build/reports/opening-pages-20260920/source-face-geometry/`,
포트는 `verification/build/verification/opening-pages/`, 비교 보고서는
`build/reports/opening-pages-20260920/final-comparison.json`이다.
포트 최종 캡처와 campaignUnitTest는 13초에 통과했다. 관련 Python 검사 24개도
통과했다. 픽셀 비교는 위 잔차 때문에 실패 상태를 유지한다. 각 구동은 외부 60초,
포트 task 30초·재생시간 20초, 원본 deadline 55초로 제한한다.

```sh
node tools/capture_opening_source_pages.cjs build/opening-source-pages
./gradlew :verification:captureOpeningDialoguePages
# 세 번째 페이지 92픽셀 잔차로 현재 exit 1
python3 tools/verify_opening_page_pixels.py build/opening-source-pages/source-pages.json verification/build/verification/opening-pages/game-pages.json --report build/opening-page-comparison.json
python3 -m unittest discover -s tools -p 'test_verify_opening*py'
```

첫 세 페이지의 분리 렌더 외에 중간 이동 화면·전체 화면·타이핑 속도·음향·전투·
게임 전체의 일치는 아직 미검증이다. 다음에는 발견한 초상화 잔차를 해결한다.

## 초상화 잔차의 atlas 배치 의존성 분리 (2026-09-20)

세 번째 초상화의 92픽셀 차이를 이미지 내용과 atlas 배치로 나누어 조사했다.
원본 DynamicAtlas의 실제 GPU texture에서 face214 내부와 주변 1px를 읽었다.
내부 192×240은 원본/포트 Head PNG와 byte-exact이며 네 방향 1px strip은 원본
edge를 정확히 복제한다. LINEAR/LINEAR 및 CLAMP_TO_EDGE도 확인했다.
이는 파일 decode나 atlas 내부 픽셀 변환의 차이를 원인에서 제외하는 근거다.

- native PNG SHA: `44722cbd404558030b770fcee41eba20c2015b1bd1ec3fc41f89738cabc4c274`
- native RGBA SHA: `cf9a88cb7ffaa1e1ebf3c458c1fc0fcc02df4bb326aee0f39c5c6238f031434f`
- GPU crop `[1128,1,194,242]` SHA: `b4d96db421e8da3df56edb8c9fb4792e1ad0adb707b1e610ba16ee13eecb60af`

임시 포트 실험에서는 같은 native PNG를 2048² RGBA8888 texture에 넣고 Cocos와
같은 x±1/y±1 복제 후 중앙 복사, LINEAR 필터로 그렸다. 원본 rect `[1129,2,192,240]`와
UV를 사용하면 세 페이지 모두 픽셀 차이 0이다. 이미지·크기·테두리는 유지하고 위치만
`[2,2,192,240]`으로 바꾸면 원래와 동일한 92픽셀 차이/SHA가 재현된다.
따라서 atlas 배치·UV 표현과 관련된 샘플링 경로 의존성으로 좁혔다.
Float32 정밀도가 유일한 원인이라고 단정하지 않는다.

원본 atlas manager의 실제 삽입도 추적했다. 폭과 x는 순서대로
`bg 19@2 → Mark_10-1 24@23 → U_select_11-1 344@49 → face181 192@395 →
U_select_10-1 344@589 → face1 192@935 → face214 192@1129`이다.
모두 y=2이며 원본 2px 간격 shelf allocator로 모든 위치가 정확히 도출된다.
각 source texture URL·UUID·크기·삽입 순서·결과 atlas ID는 manifest에 남겼다.
`bg`는 공용 InfoLayer 배경이고 `Mark_10-1`은 Hall qipao 표식이다. 두 말풍선
패널은 DialogueLayer bg1/bg0에 속한다. 앞 두 자산의 삽입은 R00 시작 UI 상태에
의존하므로 일반 대사창 생성 시 무조건 선삽입하는 방식은 아직 근거가 부족하다.
이것은 특정 초상화 좌표를 production 상수로 고정할 근거가 아니라, 일반 배치 규칙과
실제 자산 렌더 순서를 재현할 다음 구현의 근거다.

source 도구의 `JOJO_CAPTURE_FACE_ATLAS=1`은 GPU crop·sampler·packing trace를 추가한다.
기본 캡처에는 이 계측과 추가 필드가 없다. 새 `verify_portrait_atlas_crop.py`는 raw
길이/SHA, native 크기, 내부·네 방향 edge strip과 packing trace를 검증한다.
crop의 실제 atlas ID·크기·rect를 packing 기록과 연결하며 다른 atlas의 같은 좌표가
통과하지 못하도록 검사한다. 원본 allocator의 행 넘김·texture 재사용도 테스트한다. 코너 padding은 검증 범위에서
제외하며, texel 검증 통과 자체를 최종 화면 일치로 취급하지 않는다.

실험용 portrait214 분기·고정좌표는 production에서 모두 제거했다. 원복 후 캡처도
다시 비교해 앞 두 페이지 0, 세 번째 92픽셀 실패가 기존과 동일함을 확인했다.
이번 커밋은 진단 도구와 원인 분리 근거이며 아직 게임 수정 완료가 아니다.
다음은 원본 자산의 실제 렌더 요청 순서에 따른 일반 atlas 배치를 구현하는 것이다.

증거 디렉터리: `build/reports/opening-atlas-20260920/`. `source/`는 최신 원본,
`atlas-texels.json`은 내부/edge/packing 검사, `atlas-comparison.json`은 동일 위치,
`relocated-comparison.json`은 위치 변경, `restored-comparison.json`은 production 원복
결과다. `experiment.json`에는 source bundle/allocator SHA·필터·복제 순서가 있고,
`matched.patch`·`relocated.patch`에 임시 실험의 정확한 diff를 보관했다.
각 실행은 외부 60초 제한 내 10~11초에 끝났고 Python 검사 29개가 통과했다.

```sh
JOJO_CAPTURE_FACE_ATLAS=1 node tools/capture_opening_source_pages.cjs build/opening-atlas-source
python3 tools/verify_portrait_atlas_crop.py build/opening-atlas-source/source-pages.json core/build/generated/map-assets/heads/214.png --report build/opening-atlas-texels.json
python3 -m unittest discover -s tools -p test_verify_portrait_atlas_crop.py
```

## 실제 UI 렌더 순서에 따른 atlas 처리로 92픽셀 차이 해결 (2026-09-20)

남아 있던 세 번째 초상화의 92픽셀 차이를 production 렌더 경로에서 해결했다.
정상 진행 입력으로 얻은 첫 세 페이지 모두 strict RGBA 비교 차이 0이다.
첫 대사 전체 13개 문자열과 창 크기 변경 후 5·9·13 표본도 모두 0픽셀 차이를 유지한다.
세 번째 페이지 SHA는 원본과 같은
`246313e50fca4eca920063c4734d283dbeebab95255457e884449b043e9e118f`이다.

앞 단위에서 미확정이었던 InfoLayer 선삽입 이유를 실제 stack과 활성 node 경로로
확인했다. R00 scene1 `stage.setEventName('재능의 첫 징후')`가
HallLayer.setEventName → base.info를 호출해 안내창을 먼저 렌더한다.
`Hall/Canvas/Layer/bg`는 frame145/game2474.8ms에 삽입됐다. 첫 SHOW_SAY는
HallUnit.showQiPao를 통해 `Hall/Canvas/Layer/map/pmapobj/img0`을 활성화한다.
Mark·오른쪽 패널은 frame355, 첫 초상화는 frame356에 삽입됐다. 이는 Login 잔여
상태나 검증 fixture가 아니라 원래 장 시작 안내와 화자 표시의 수명주기다.

`SourceSpriteAtlas`는 시나리오 자산 소유자가 관리한다. 안내창 NinePatch, 실제 화자
표식 draw, 현재 방향의 말풍선, 초상화를 그릴 때 자산을 등록한다. 오른쪽·왼쪽
패널은 원본의 서로 다른 native texture를 유지하고 같은 자산의 요청은 재사용한다.
현재 크기의 2048² atlas에 2px 간격 shelf 배치를 적용하고 원본의 shifted edge
복사·LINEAR/CLAMP 필터로 그린다. 이미지 내용이나 특정 화자·관측 좌표를 기준으로
분기하지 않는다. 앞선 자산을 미리 등록하거나 빈 폭을 넣지도 않는다.

원본처럼 큰 이미지/atlas 수 한계에 도달한 신규 자산은 개별 texture region으로
계속 그린다. 원본 manager가 여섯 번째 atlas 생성 직후 신규 삽입을 막는 경계도
반영했다. 기존 region은 재사용하며 모든 atlas와 개별 fallback texture는 시나리오
자산 해제 때 함께 해제한다. atlas 하나당 RGBA GPU 저장 공간 16MiB가 추가된다.
본문·화자명 라벨과 전투 자산을 이 경로에 일괄 등록하지 않는다.

검증 driver는 원본 AFTER_DRAW isolation처럼 첫 실제 대사 화면을 한 프레임 그린
뒤 분리 표시로 바꾼다. 따라서 화자 표식의 정상 렌더 요청이 검증 편의 때문에
사라지지 않는다. 기존 정책 검사는 이 순서를 기대하도록 갱신했다. 자연 부분 문자와
완료 후 입력 검사는 그대로 유지했다.

검증: `SourceAtlasShelfTest` 3개(관측 자산 크기에서 위치 도출, 행 넘김/용량 한계,
새 scene의 초기화), campaignUnitTest 46개, Python 검사 29개가 통과했다.
최종 세 페이지 캡처와 Kotlin 검사는 빌드 포함 13초에 끝났다. 각각 외부 60초 제한을
유지했다. 초기 campaign 검사의 즉시 isolation 기대값 한 건을 새 실제 렌더 순서에
맞춰 고친 뒤 최종 실행이 통과했다.

증거는 `build/reports/opening-atlas-lifecycle-20260920/`의 `source/`,
`final-comparison.json`, `prefix-comparison.json`, `resize-comparison.json`,
`final-tests.log`에 있다. source opt-in packing trace에는 삽입 시점·활성 node 경로와
assembler stack이 추가됐다. 안내창에 사용한 19×17 raw asset도 원본 native PNG의
decoded RGBA와 0byte 차이임을 확인했다.

```sh
JOJO_CAPTURE_FACE_ATLAS=1 node tools/capture_opening_source_pages.cjs build/opening-atlas-source
./gradlew :verification:captureOpeningDialoguePages :verification:captureOpeningDialoguePrefixesAll :verification:captureOpeningDialoguePrefixesAfterResize :verification:campaignUnitTest :core:test --tests com.jojo.game.SourceAtlasShelfTest
python3 tools/verify_opening_page_pixels.py build/opening-atlas-source/source-pages.json verification/build/verification/opening-pages/game-pages.json
```

이번 일치는 첫 세 페이지의 분리 렌더와 첫 대사의 문자열별 결과다. 안내창·화자 표식은
atlas 기반 region 렌더로 바뀌었지만 그 전체 화면 픽셀과 타이밍까지 검증한 것은 아니다.
다음 단위에서 장 시작 안내창과 초반 전체 화면을 확인하며, 전체 게임 목표는 계속 진행한다.


## 첫 EVENT 안내창 복원과 자연 재생 비교 추가 (2026-09-20)

R00 scene1의 `재능의 첫 징후` 안내창이 포트에서 생략되던 조건을 수정했다.
원본 HallLayer.setEventName은 명시적인 stage.draw 이전에도 draw와 base.info를
호출한다. 포트는 battleDrawRequested를 요구해 이 안내를 건너뛰었다.
R_ 모듈의 EVENT는 draw 전에도 표시하고, skip 및 비 R_ 모듈의 기존 조건은 유지한다.

이 발견으로 앞 단위의 인과관계 설명을 정정한다. 원본의 InfoLayer 선행 렌더는
확인했지만, 당시 포트에서도 같은 안내창 수명주기와 atlas 요청 순서가 실행됐다고
볼 근거는 없었다. 앞서 얻은 대사 픽셀 일치 결과는 유효하나, 그것만으로 atlas 배치
이력이나 전체 장면의 동등성을 증명하지 않는다.

원본 실제 InfoLayer의 자연 타이핑 완료 AFTER_DRAW와 다음 분리 프레임을 각각
계측한다. 타이핑 핸들 종료, 자동 닫기 핸들 유지, 완성 문자열, opacity와 중간 문자열을
기록한다. 포트도 정상 모달의 완성 프레임 다음에 배경만 분리하며 진행 입력은 0회다.
비교 도구는 RGBA 길이/SHA, 프레임 순서와 문자열·상태 계약을 확인한다.

원본 RichText exporter를 InfoLayer의 Arial 40 / lineHeight 50 계약으로 확장했다.
R00 AST와 Python에서 문구를 읽고 빈 문자열 및 8개 prefix의 glyph texture와 크기를
생성한다. 화면 캡처를 게임 텍스처로 사용하지 않는다. 기존 본문 exporter의 28개 PNG와
계약은 재생성 후 동일했다. 포트는 측정된 label 크기와 prefab padding/anchor를 사용하며,
아직 생성하지 않은 INFO/EVENT 문구는 기존 글꼴로 표시한다.

현재 strict 비교는 실패 상태를 그대로 보존한다. 안내창 분리 영역 차이는 기존
48,856픽셀에서 7,738픽셀로 감소했고, 전체 프레임은 91,428에서 50,310픽셀로 감소했다.
남은 안내창 렌더 차이와 배경 차이는 후속 단위에서 조사한다. 타이핑 속도와 자동 닫기
시간의 동등성, 안내창 각 prefix의 화면 일치는 아직 검증하지 않았다.

검증: Kotlin core 6개 및 campaign 47개, Python opening 30개와 atlas 5개가 통과했다.
수정 후 첫 세 대사 페이지, 첫 대사의 13개 prefix, resize 후 3개 표본은 모두 0픽셀 차이다.
Gradle 회귀 실행은 60초 제한 내 18초에 끝났다. Node 구문과 git diff 검사도 통과했다.
증거: `build/reports/opening-event-20260920/`의 `source/source-event.json`,
`initial-comparison.json`, `label-comparison.json`, `pages-regression.json`,
`prefix-regression.json`, `resize-regression.json`, `regression.log`.

```sh
./gradlew :verification:captureOpeningEvent
python3 tools/verify_opening_event_pixels.py build/reports/opening-event-20260920/source/source-event.json verification/build/verification/opening-event/game-event.json --report build/reports/opening-event-20260920/label-comparison.json
```

이번 단위는 누락된 안내 복원과 렌더 개선, 남은 차이를 검출하는 도구 추가다.
전체 게임 동일성 목표와 안내창 픽셀 일치 작업은 계속 진행한다.


## 안내창 sliced 테두리의 원본 규칙 적용 (2026-09-20)

첫 안내창의 7,738픽셀 차이를 공간별로 분리하니 글자 영역은 이미 동일했고 차이는
전부 패널 테두리에 있었다. 기존 코드는 패널 전체 크기에만 .86 배율을 적용하고
NinePatch의 고정 테두리는 포트 단위로 그렸다. 패널도 원본 좌표 변환 안에서 그리자
차이가 403픽셀로 감소했다.

원본 bg SpriteFrame의 capInsets와 실제 업로드 정점을 추가 계측했다.
실제 inset은 left7/right6/top6/bottom6이며 기존 8/8/7/7과 달랐다.
`SourceSlicedPatch`는 원본 경계 UV, BR↔TL 삼각형 대각선, 작은 패널에서의 테두리
비례 축소를 적용한다. LibGDX NinePatch의 늘어나는 영역 half-texel 보정을 제거하고,
원본 Double 좌표를 정점 제출 시 Float32로 변환한다. 실제 atlas region을 사용한다.

최종 안내창 분리 비교는 259픽셀 차이로 아직 실패다. 차이는 왼쪽 테두리의
bottom-left bbox [1054,654,1056,785]에만 남으며 채널별 절댓값 차이는 모두 1이다.
전체 화면은 42,790픽셀 차이다. Double 좌표 및 같은 대각선의 삼각형 순서 변경은
최종 픽셀 결과를 바꾸지 않았다. 이 결과를 완전 일치로 취급하지 않는다.

패널 밖의 42,531픽셀 차이는 42,488개가 최대 채널 오차 1, 43개가 오차 2였다.
배경은 원본 JPEG와 포트 maps/71.jpg의 파일 바이트가 동일함을 확인했다.
다음 단위에서는 JPEG 디코딩 결과와 실제 GPU 텍스처를 비교해 배경 차이를 분리한다.

증거: `build/reports/opening-event-20260920/`의 `scaled-comparison.json`,
`sliced-comparison.json`, `double-comparison.json`, `triangle-comparison.json`.
`source/source-event.json`에 실제 panel world matrix, vertices, UV와 insets를 보관했다.
원본 재캡처 SHA는 기존과 동일했다. 픽셀 비교기는 차이가 남은 상태를 exit 1로 보고한다.

수정 후 첫 세 대사·13개 prefix·resize 3개 표본은 모두 strict 0픽셀 차이를 유지했다.
campaignUnitTest 47개와 Python opening 30개가 통과했고 Astra 코드 리뷰에서 blocking
문제는 없었다. 원본과 같은 규칙을 적용했지만 부동소수점 연산 순서까지 동일하다는
주장은 하지 않는다. 회귀 증거는 `sliced-regression.log`, `sliced-pages.json`,
`sliced-prefixes.json`, `sliced-resize.json`이다.


## 원본 브라우저 배경 디코딩으로 전체 화면 차이 축소 (2026-09-20)

첫 EVENT 배경의 차이는 JPEG 디코딩에서 발생했다. 동일한 640×400 원본 JPEG
(SHA `39a430d7602c3c0aaa102ecfa404c631cd71e1e9c33c34c539368a4293791505`)에 대해,
원본 browser Image→Canvas와 실제 GPU 텍스처는 모두
`f737f8bea044fd85f8ff8363d5f914abbeccff2c79f659db8d3e642f961675ab`이었다.
포트 Pixmap 디코딩과 실제 렌더에 사용된 캐시 텍스처는 모두
`a62f402301bf56e24d1a2a4be5531036bfe8e485ed9f80770bb1c66a667270ed`이었다.
양쪽 GPU 텍스처 차이는 2,583픽셀, 최대 채널 오차 2였다. 각 엔진 내부 CPU/GPU는
동일하므로 업로드가 아니라 디코딩 결과 차이로 분리했다.

원본 캡처 opt-in `JOJO_CAPTURE_BACKGROUND_TEXTURE=1`은 실제 Hall map Sprite의
GL 텍스처와 브라우저 이미지 Canvas 값을 저장한다. 포트 opt-in은 실제 ScenarioScreen
캐시 Texture를 읽기 전용으로 찾아 임시 FBO에서 읽고, 그 TextureData의 파일을
Pixmap으로 별도 디코드한다. 양쪽 모두 이전 FBO를 복원하며 게임 텍스처를 다시
생성하거나 consume하지 않는다. 이 계측 전후 실제 화면 SHA는 동일했다.

새 `exportSourceMapTextures`는 mapSources의 원본 JPEG를 설치된 원본 Electron의
browser Image→Canvas로 디코드하고 PNG와 provenance manifest를 생성한다.
385개 Mmap 자산 전체에 같은 변환을 적용한다. 특정 배경의 보정표나 캡처 화면은
사용하지 않는다. manifest는 원본/PNG/RGBA SHA, 크기와 Electron 버전을 기록하며,
빈 카탈로그 및 maps와 키집합이 다른 mapSources는 실패한다. 별도 generated 디렉터리와
resource 경로를 쓰고 ScenarioSceneAssets 배경을 이 PNG에서 로드한다.

변경 후 포트 PNG의 CPU 및 실제 GPU 텍스처는 원본과 strict 0픽셀 차이다.
전체 EVENT 화면 차이는 42,790→259픽셀로 감소했다. 남은 전체 화면/분리 안내창 차이는
모두 동일한 왼쪽 테두리 [1054,654,1056,785]이며 전체 화면 일치는 아직 실패다.
385개 PNG의 input SHA·출력 SHA·decode RGBA를 검사했지만 다른 배경의 실제 게임
렌더까지 검증했다는 뜻은 아니다. 자동 닫기·타이핑 시간도 별도 미검증 범위다.

회귀: 첫 세 대사 페이지, 13개 prefix, resize 후 3개 표본 모두 0픽셀 차이 유지.
campaignUnitTest 47개와 Python opening 34개가 통과했다. Astra 리뷰에서 나온 빈/누락
카탈로그 거부를 추가했고 두 실패 사례를 실제 실행으로 확인했다.
증거: `build/reports/opening-background-20260920/`의 `source/`, `game/`,
`initial-texture-comparison.json`, `png-texture-comparison.json`, `png-frame-comparison.json`,
`pages-regression.json`, `prefixes-regression.json`, `resize-regression.json`, `regression.log`.

```sh
JOJO_CAPTURE_BACKGROUND_TEXTURE=1 node tools/capture_opening_source_event.cjs build/reports/opening-background-20260920/source
JOJO_CAPTURE_BACKGROUND_TEXTURE=1 ./gradlew :verification:captureOpeningEvent
python3 tools/verify_opening_background_pixels.py build/reports/opening-background-20260920/source/source-event.json verification/build/verification/opening-event/game-background.json
```

다음 단위는 안내창 왼쪽 테두리의 잔여 259픽셀과 초기 안내의 시간 동작이다.
전체 게임 동등성 목표는 계속 진행한다.


## EVENT 타이핑 중 입력 처리 수정 (2026-09-20)

원본 InfoLayer는 타이핑 중 첫 클릭에서 문구를 모두 공개하고, 완성 후 다음 클릭에서
닫는다. 포트의 reveal 우선 분기에 EVENT가 빠져 첫 클릭에 바로 닫히던 동작을 수정했다.
기존 INFO/MAP_INFO 분기에 EVENT를 포함했고 타이밍 계산은 이번 단위에서 바꾸지 않았다.
실제 R00 scene1을 실행하는 회귀 테스트가 첫 입력의 문구 완성·모달 유지와 두 번째
입력의 닫힘을 확인한다. ScenarioPlaybackControllerTest와 Astra 검수가 통과했다.
실제 OS 입력 경로나 자연 타이핑·닫힘 시간의 동등성을 입증한 것은 아니다.


## 첫 EVENT 전체 화면과 자연 8개 문자열 strict 일치 (2026-09-20)

남은 안내창 왼쪽 테두리 259픽셀을 해결했다. 완성 문구의 전체 화면과 분리 안내창,
자연 타이핑으로 관측한 8개 비어 있지 않은 prefix의 전체 화면 모두 strict RGBA 차이 0이다.
전체 화면 SHA는 원본과 같은
`1c73f7ceea33a8e5391f1fd6bf0f8212b4c4a2bec0499e3d08a0dee634e4ade3`,
분리 화면은 `44b1aa971446d648c79e28d9e35b3cc7c6741d3918d2e1453911f11c48be1188`이다.

원인 분리를 위해 양쪽 실제 GPU InfoLayer atlas [1,1,21,19]를 읽었다. 원본 19×17
영역과 1px padding을 포함한 1,596byte 전체가 동일했으며 SHA는
`f92e90ab9655a150ba755f2bbcadb8b9b357539baaa22f06e97418ee323e16f0`이다.
원본 현재 GL program의 실제 cc_matViewProj XY 항과 포트의 합성 행렬 XY 항도
Float32 값이 같았다. 원본 sliced 정점·UV·indices와 shader 원문도 계측했다.
삼각형 순서 실험은 이번에 실제 코드 치환을 확인하고 다시 수행해 259픽셀 유지로
확인했다. 앞 단위의 치환이 적용되지 않은 실험은 해당 결론의 근거에서 제외한다.

좌표 반올림을 분리한 대조 실험에서 Float 곱셈·덧셈을 각각 반올림하면 259픽셀 차이가
재현됐고, 입력 정점과 행렬은 Float32로 유지하되 곱셈·덧셈을 Double로 계산한 뒤
clip 좌표를 Float32로 한 번 반올림하면 차이가 없어졌다. 특정 픽셀/문자에 대한
보정 상수는 없다. 원본 GPU의 FMA 명령을 완전히 재현했다고 단정하지 않는다.

SourceSlicedPatch는 유한한 축 정렬 2D 직교 행렬에만 이 CPU 투영을 적용한다.
GPU에 identity XY로 제출하고 constant Z는 원래 합성행렬 M23을 유지한다.
회전·전단·원근·깊이 변화 등 지원하지 않는 행렬은 기존 GPU 투영 경로를 사용한다.
행렬 설정과 복원은 try/finally로 감싸고 좌표/UV/행렬 저장 공간은 재사용한다.

자연 prefix 캡처는 강제 reveal이나 입력 없이 실제 각 문자열을 관측한다. 원본은
AFTER_DRAW, content/joined/remaining/typingHandle, opacity 255 및 texture upload를
확인하며 포트는 실제 modal complete와 isolation 상태를 기록한다. 비교기는 8개
문자열의 누락·순서·프레임·시간·raw SHA·상태를 검증한다. 캡처 비용이 시간 흐름에
영향을 주므로 이 결과를 타이핑/닫힘 시간 동등성의 근거로 사용하지 않는다.

최종 회귀: core 5개(입력/투영 수학), campaign 47개, Python opening 35개 통과.
첫 세 대사 페이지, 13개 대사 prefix, resize 후 3개 표본도 모두 0픽셀 차이 유지.
최종 Gradle 실행은 외부 60초 제한 내 28초에 끝났다. Astra 코드 검수의 지원 범위,
Z 보존 및 포트 prefix 상태 기록 요구를 반영했다.

증거: `build/reports/opening-panel-residual-20260920/`의 `source/`, `prefix-source/`,
`final-game/`, `texel-comparison.json`, `cpu-float-comparison.json`,
`cpu-clip-comparison.json`, `final-prefix-comparison.json`, `pages-regression.json`,
`prefixes-regression.json`, `resize-regression.json`, `final-regression.log`.

```sh
JOJO_CAPTURE_PANEL_TEXTURE=1 node tools/capture_opening_source_event.cjs build/reports/opening-panel-residual-20260920/source
JOJO_CAPTURE_EVENT_PREFIXES=1 node tools/capture_opening_source_event.cjs build/reports/opening-panel-residual-20260920/prefix-source
JOJO_CAPTURE_EVENT_PREFIXES=1 JOJO_CAPTURE_PANEL_TEXTURE=1 ./gradlew :verification:captureOpeningEvent
python3 tools/verify_opening_event_pixels.py build/reports/opening-panel-residual-20260920/prefix-source/source-event.json verification/build/verification/opening-event/game-event.json --require-prefixes
```

이번 일치는 관측한 8개 문자열 상태와 완성 프레임에 한정된다. 빈 문구의 초기 상태,
타이핑 주기, 자동 닫기와 후속 스크립트 재개 시점은 다음 단위에서 확인한다.
전체 게임 동등성 목표는 계속 진행한다.


## 첫 EVENT 자연 타이핑과 자동 닫힘 규칙 일치 (2026-09-20)

EVENT/INFO의 타이핑 상태를 ScenarioModalController 한 곳에서 관리한다. 원본처럼
첫 scheduler update는 초기화에만 쓰고, 이후 0.04초 이상 누적되면 한 글자 단위만
공개한 뒤 초과 시간을 버린다. 완성 뒤 1초 닫힘 타이머를 예약하며 완성 프레임의
시간을 다시 세지 않는다. 입력으로 완성시킨 경우에는 다음 update가 닫힘 타이머의
초기화 단계가 된다. 이전 총 수명 근사식의 추가 대기 0.35초를 제거했다.
시나리오와 전투 INFO 화면 모두 이 상태를 읽도록 연결했다. 전투 화면의 픽셀
동등성까지 이번 검증으로 주장하지 않는다.

원본 계측은 시작 전 InfoLayer lifecycle과 Scheduler.update를 감싸고 실제 delta와
프레임을 기록한다. getTotalTime의 벽시계 시간을 scheduler 시간으로 사용하지 않는다.
강제 reveal, 입력, framebuffer 캡처 없이 첫 EVENT부터 첫 대사까지 관측했다.
비교기는 각 실행의 실제 delta 열로 타이머 규칙을 재생하고 8개 공개 callback 및
닫힘 프레임이 정확히 일치하는지 검사한다. 원본은 prefix 144/147/150/153/156/159/
162/165, 닫힘 226; 포트는 2/5/8/11/14/17/20/23, 닫힘 83으로 각 예측과 일치했다.
완성 후 닫힘은 원본 1.0167초, 포트 1.000038초로 각각의 프레임 간격에 맞는다.
수정 전 포트의 완성 후 대기는 약 1.35초였고 같은 비교에서 실패했다.

검증: 관련 core 161개, campaign 47개, Python opening 40개 통과.
EVENT 완성 전체/분리 화면 및 자연 8개 prefix, 첫 대사 3페이지, 대사 13개 prefix,
resize 후 3개 표본의 strict RGBA 차이 0 유지. 최종 입력 타이머 초기화 수정 후
core 161개와 자연 타이밍 캡처를 다시 통과했다.

증거: `build/reports/opening-event-timing-20260920/`의 `source/source-event-timing.json`,
`game-baseline.json`, `game-final.json`, `baseline-timing-comparison.json`,
`final-timing-comparison.json`, `final-regression.log`, `manual-timer-regression.log`,
`event-pixels-regression.json`, `pages-regression.json`, `prefixes-regression.json`,
`resize-regression.json`.

```sh
node tools/capture_opening_source_event_timing.cjs build/reports/opening-event-timing-20260920/source
./gradlew :verification:captureOpeningEventTiming
python3 tools/verify_opening_event_timing.py build/reports/opening-event-timing-20260920/source/source-event-timing.json verification/build/verification/opening-event-timing/game-event-timing.json
```

닫힘부터 첫 대사까지는 원본 2.1333초, 포트 2.048322초로 아직 차이가 있다.
후속 이동/대기/대사 시작 시점과 빈 EVENT의 초기 framebuffer는 다음 검증 범위다.
전체 게임 동등성 목표는 계속 진행한다.


## 첫 Hall 이동의 초기 프레임과 완료 대기 수정 (2026-09-20)

실제 원본의 첫 병사 181 이동은 첫 ActionInterval update에서 elapsed=0으로 초기화하고
그 프레임의 delta를 버린다. 포트는 같은 delta를 즉시 더해 baseline에서 최대
0.4156929칸 먼저 움직였다. 완료 프레임만 비교하면 이번 baseline처럼 우연히 같을 수
있으므로 프레임별 위치와 logical commit도 함께 검사한다. 이전 정적 .4초 계약 기반
first-move 검증은 이 차이를 놓쳤으며 이번 실제 원본 관측으로 대체한다.

Hall animator의 첫 tick에서 delta를 버리고, 단독/그룹 이동 모두 실제 대상 유닛의
이동 완료 후 스크립트를 재개하도록 변경했다. 독립 duration countdown을 없앴으며
reset/skip/외부 재개 시 대기 상태를 정리한다. 전투 이동과 일반 delay는 유지한다.
Astra가 설계와 구현을 검수했고 Sol이 production과 집중 테스트를 구현했다.

원본 도구는 시작 전 pass-through hook으로 Hall/Stage 흐름과 첫 이동의 AFTER_DRAW
node 좌표를 기록한다. 별도로 HallLayer.turnPos에서 시작/끝 기준점을 읽어 실측 이동
양 끝으로 스스로 좌표계를 보정하지 않는다. 원본 path 배열도 생략 없이 보존한다.
비교기는 각각의 실제 delta로 첫 tick 폐기와 Cocos sequence 진행을 계산한다.
Cocos의 연속된 두 zero-duration CallFunc가 만드는 FLT_EPSILON=1.192092896e-7초도
원본 oracle에는 반영한다.

최종 fresh source 25개 표본과 port 26개 표본에서 완료/재개 프레임은 각 예측과 동일
(source251, port109)하다. 원본 최대 좌표 오차는 2.13e-14칸, 포트는 4.24e-6칸이며
검사의 좌표 허용오차는 2e-5칸이다. 픽셀 동등성이나 모든 delta에서의 동등성 증거가
아니다. **포트의 sequence epsilon은 아직 미반영**이므로 .4초와 .4+epsilon 사이의
누적 delta에서는 완료 프레임 차이가 남을 수 있다. 이를 report의 knownUnmatchedSemantics에
명시했다. 다음 단위에서 정밀도와 유닛 비동기 준비 경계를 계속 맞춘다.

검증: core 168개, campaign 47개, Python opening 45개 통과. 자연 EVENT 타이머 규칙
회귀 통과, 첫 3페이지 분리 대사 화면의 strict RGBA 차이 0 유지. 모든 실행은 외부
60초 제한 이내, 원본 캡처는 내부 15초 제한에서 약 7초에 끝났다.

증거: `build/reports/opening-post-event-20260920/`의 `source/source-event-timing.json`,
`game-baseline.json`, `game-fixed.json`, `baseline-first-move.json`, `fixed-first-move.json`,
`event-timer-regression.json`, `pages-regression.json`, `fixed-game.log`, `pages-regression.log`.

```sh
node tools/capture_opening_source_event_timing.cjs build/reports/opening-post-event-20260920/source
./gradlew :verification:captureOpeningEventTiming
python3 tools/verify_opening_first_move.py build/reports/opening-post-event-20260920/source/source-event-timing.json verification/build/verification/opening-event-timing/game-event-timing.json
```

EVENT 종료부터 첫 글자까지 fresh 관측은 source 2.1초, port 2.150788초였다. 이 구간에는
비동기 showUnit(s), 여러 이동, 명시적 delay와 대사 typing이 섞여 있어 총량만으로
일치 여부를 판정하지 않는다. 전체 게임 동등성 목표는 계속 진행한다.


## Hall 이동의 Double 시간 및 zero-duration 경계 일치 (2026-09-20)

이전 단위의 미반영 sequence epsilon을 수정했다. 원본 HallUnit._move2의 action 순서를
만들고 cc.sequence의 왼쪽 결합 규칙으로 시간을 계산한다. 직선은 처음 두 CallFunc의
duration 합이 0이어서 `1.192092896e-7`초가 추가되며, 꺾인 경로는 첫 결합부터 양수라
추가되지 않는다. Hall의 elapsed/duration은 Double을 실제 판정 기준으로 쓰고 표시
좌표만 Float로 내보낸다. planner와 animator도 같은 원본 duration을 사용한다.

동일 목적지의 1점 경로도 원본 AStar가 실제 반환할 수 있다. 원본은 Call→MoveTo(0)→Call을
실행하며 MoveTo의 0이 epsilon으로 바뀐다. 포트의 즉시 완료를 이 짧은 액션과 실제 완료
대기로 변경했다. Float epsilon 두 반쪽의 합조차 원본 Double epsilon보다 조금 작아
미완료로 남는 경계도 실제 source 결과와 같다. 전투 이동의 기존 시간 규칙은 유지했다.

새 `export_source_hall_move_steps.cjs`는 Electron 원본 엔진에서 production `_move2`가
생성한 실제 cc.Sequence를 `startWithTarget`/`step`으로 실행한다. identity turnPos와
명시적 delta를 쓰는 controlled evidence이며 자연 실행이나 화면 캡처로 표시하지 않는다.
직선, 실제 R00 두 번째 그룹의 꺾인 경로, 제자리 경로에서 22개 schedule을 관측했다.
원본 .4 전후/정확값, Float 입력, 회전 .36/.4/.84, 완료 .88 및 분할 입력을 포함한다.
모든 Float 입력은 왕복 변환으로 delta가 변하지 않는지 검사한다.

실제 원본 fixture를 `core/src/test/resources/parity/hall-move-source.json`에 저장했고
HallMoveSourceFixtureTest가 duration, 각 표본의 Float32 좌표, callback 방향, 완료 여부를
대조한다. Float schedule은 production animator의 logical commit까지 검사한다.
22개 schedule 모두 통과했다. callbackDirection=-1은 원본 호출 인자 관측이며 최종
렌더 방향 전체가 같다는 의미는 아니다.

자연 첫 이동 비교도 기존 2e-5칸 허용오차에서 **Float32로 표현한 좌표의 정확한 일치**로
강화했다. 실제 Hall Double duration과 프레임 delta 합도 검사한다. source 25개/port 26개
표본, 완료·재개 frame251/109가 각 실행의 예상과 일치했다. 포트의 최대 raw Double 대비
좌표 오차 3.95e-7칸은 Float 변환 후 동일하다. 전체 이동 framebuffer의 픽셀 동등성은
이 결과만으로 주장하지 않는다.

검증: core 179개, campaign 47개, Python opening 47개 통과. EVENT 자연 타이머 규칙
회귀 통과, 첫 세 분리 대사 화면 strict RGBA 0픽셀 차이 유지. 최종 Gradle 실행은
외부 60초 제한에서 14초에 끝났다. Astra가 설계/production/fixture와 Float 입력
정확성을 검수했고 Sol이 production 및 경계 테스트를 구현했다.

증거: `build/reports/opening-hall-precision-20260920/`의
`source/source-hall-move-steps.json` (SHA-256
`9ea4600634ae71b830e7375b1f3ad9b3e4f665bda3884e25f9abfe4cbf53dec0`),
`game-final.json`, `natural-first-move.json`, `event-timer-regression.json`,
`pages-regression.json`, `final-regression.log`.

```sh
node tools/export_source_hall_move_steps.cjs build/reports/opening-hall-precision-20260920/source
./gradlew :core:test --tests com.jojo.game.HallMoveSourceFixtureTest
./gradlew :verification:captureOpeningEventTiming
python3 tools/verify_opening_first_move.py build/reports/opening-post-event-20260920/source/source-event-timing.json verification/build/verification/opening-event-timing/game-event-timing.json
```

유닛 비동기 준비, 이동 중 zIndex scheduler와 전체 화면, 후속 대사 시작 타이밍은
계속 검증해야 한다. 현재 EVENT 종료→첫 글자 관측은 source 2.1초, port 2.167357초이며
전체 구간 일치를 의미하지 않는다. 전체 게임 동등성 목표는 계속 진행한다.


## Hall 유닛의 두 텍스처 준비 후 등록·재개 (2026-09-20)

원본은 신규 유닛의 R_AVATAR에 해당하는 두 Pmapobj2 텍스처를 순차 로드하고 anime를
준비한 뒤 유닛을 등록·재개한다. 포트는 이동을 먼저 시작하고 draw에서 현재 방향의
텍스처 한 장만 읽었다. 수정 전 실제 첫 등장에는 181의364, 0의2, 157의315,
182의366이 각각 준비되지 않았다.

ScenarioScreen의 Hall 실행에는 명시적 asset-readiness 모드를 켠다. 신규 showUnit은
등록을 보류하고, showUnits는 entry별로 준비된 유닛을 등록하되 마지막 entry 완료에서만
재개한다. 기존 showUnit의 재배치는 즉시 처리한다. token/index로 중복·이전 장면의
callback을 거부하고, skipDelay 및 외부 resume도 준비 대기를 우회하지 못하게 했다.
Headless의 기존 즉시 논리 실행 모드는 유지하며, 새 회귀 테스트는 readiness 모드를
명시적으로 켜고 완료 신호를 전달한다.

실제 Pixmap decode는 두 worker에서 수행하고 Texture 생성/upload는 render thread에서
한다. 첫 텍스처 완료 후 두 번째를 요청하며 GPU upload 완료 callback만 안전 큐로
넘긴다. Texture는 기존 unitTextures cache만 소유하고, 진행 중 중복 decode/load와
종료 시 Pixmap 누수를 막았다. 파일 누락은 준비 완료로 처리하지 않는다.
pre/post playback 두 지점에서 요청과 완료 큐를 처리하며, 캐시가 준비된 연속 요청은
같은 안전 지점에서 처리한다. 고정 시간 또는 고정 한 프레임 대기를 넣지 않았다.

원본 hook은 load request/callback, onInit, show 완료를 pass-through 관측한다.
GPU 준비는 실제 GL handle로 판정하고 decoded·anime 상태와 구분한다. 병렬 group의
0/157 완료 순서는 강제하지 않는다. 이번 fresh source는 157이 먼저 준비됐다.
포트 역시 실제 GPU handle을 가진 cache 항목만 첫 등장 표본에 기록한다.
네 유닛 모두 필요한 두 장이 준비된 결과로 바뀌었다.

첫 이동 검증은 별도 Screen 등록 observer의 pre/post playback phase를 기대 tick의
근거로 사용한다. moveJustStarted flag는 그 phase와 교차 검사하며 정답 선택의 근거로
쓰지 않는다. 그룹 asset 대기 중에는 첫 이동 완료 후 idle action0이 정상이며,
스크립트 재개는 후속 group 준비 요청이 생성된 프레임으로 확인한다.
이번 source/port 첫 이동 완료·재개는 각 delta 예측과 같은 frame253/109였고,
Float32 좌표도 정확히 일치했다.

검증: core 181개, campaign 47개, Python opening 51개 통과. EVENT 자연 타이머 규칙
회귀와 첫 세 분리 대사 화면 strict RGBA 0픽셀 차이를 유지했다. 최종 Gradle 실행은
외부 60초 제한에서 22초에 완료했다. Astra가 자산 소유권·callback 경계·독립 phase
검증을 검수했고 Sol이 runtime/asset bridge를 구현했다.

증거: `build/reports/opening-unit-ready-20260920/`의 `source/source-event-timing.json`,
`game-baseline.json`, `game-final.json`, `baseline-ready.json`, `fixed-ready.json`,
`first-move-regression.json`, `event-timer-regression.json`, `pages-regression.json`,
`final-regression.log`.

```sh
node tools/capture_opening_source_event_timing.cjs build/reports/opening-unit-ready-20260920/source
./gradlew :verification:captureOpeningEventTiming
python3 tools/verify_opening_unit_ready.py build/reports/opening-unit-ready-20260920/source/source-event-timing.json verification/build/verification/opening-event-timing/game-event-timing.json
python3 tools/verify_opening_first_move.py build/reports/opening-unit-ready-20260920/source/source-event-timing.json verification/build/verification/opening-event-timing/game-event-timing.json
```

GPU 준비 표본은 첫 등장 프레임의 두 자산 존재를 입증하며, 그 표본만으로 프레임 내부
전체 callback 순서나 로드 지연 시간까지 같다고 주장하지 않는다. EVENT 종료→첫 글자
관측은 source 2.1499초, port 2.137342초이며 총량 동등성은 여전히 미검증이다.
후속 delay·대사 시작 경계와 이동 중 zIndex/전체 화면 검증을 계속 진행한다.
전체 게임 동등성 목표는 계속 진행한다.


## 첫 stage.delay의 정밀도와 등록 프레임 (2026-09-20)

원본 StageLayer.delay(3)은 JavaScript Double의 `.1 * 3`초를 CallbackTimer로
누적한다. 기존 포트의 Float 차감은 `.1f` 세 번 뒤 작은 양의 잔여 시간을 남겨
네 번째 update까지 기다릴 수 있었다. stage.delay만 Double 누적으로 분리하고,
update 밖 등록은 다음 update에서 elapsed 0으로 prime하며, update 안 등록은
그 프레임에 이미 prime된 상태로 다음 프레임부터 누적한다. 완료 delta의 잔여분은
다음 delay에 넘기지 않는다. 일반 Float 지연과 Hall 이동 타이머는 유지했다.

원본의 실제 CallbackTimer.update를 감싸 직접 관측했다. scheduler 바깥의 다음
update를 첫 timer update로 추정하는 초기 계측은 폐기했다. 실제 첫 delay는 Hall
이동 완료와 같은 frame329에서 elapsed -1→0으로 prime했고 frame347에서 실행됐다.
따라서 Hall 완료 뒤 별도의 prime 프레임을 추가하지 않는다.

`export_source_stage_delay_steps.cjs`는 실제 원본 StageLayer.delay와 격리한
cc.Scheduler에 delta를 넣어 외부 등록, update 내부 등록, callback 연속 등록
세 사례를 추출한다. 첫 delta10은 모두 prime으로 버리고, Float32 `.1` 세 번 후
resume하며 연속 delay는 step3과6에 resume한다. fixture SHA-256:
`730887725c4ef7399de159fb7b660bb30fa34e658ec64d8371dbb36d55b8ea7c`.

자연 재생에서는 source329→347, port190→208의 delay 완료가 각 실행의 delta로
계산한 임계 프레임과 일치했다. 수정 전 자연 표본도 통과했으므로 자연 표본만으로
정밀도 결함을 입증하지 않는다. Float32 경계 반례와 실제 원본 controlled fixture가
그 결함의 회귀 근거다. EVENT, 첫 이동, 네 유닛 텍스처 준비 검증 및 첫 세 분리 대사
화면 strict RGBA 0픽셀 차이를 유지했다. campaign47, Python opening53개 통과.

증거: `build/reports/opening-delay-boundary-20260920/`의 source, controlled,
`game-final.json`, `final-delay.json`, `first-move-regression.json`,
`ready-regression.json`, `event-regression.json`, `pages-regression.json`, `regression.log`.
새 verifier는 첫 명시적 delay만 판정하며 대사 첫 글자 타이머와 전체 지연 동등성을
주장하지 않는다. EVENT 종료→첫 글자 관측은 source2.1166초, port2.153527초다.
전체 게임 동등성 목표는 계속 진행한다.


## 첫 Hall 대사의 자연 글자 타이머 (2026-09-20)

실제 표시 경로인 DialogueSession의 DialogueTextReveal은 생성 프레임 delta부터
Float로 누적하고 while로 여러 글자를 따라잡으며 잔여 시간을 보존했다. 원본
DialogueLayer의 CallbackTimer는 첫 update에서 elapsed를 0으로 초기화하고 이후
Double `.04` 이상일 때 한 단위만 표시한 뒤 elapsed를 0으로 버린다.

수정 전 자연 표본은 frame208 생성 후209에 첫 글자를 표시했다. 생성 프레임을
제외한 실제 delta로는210이 첫 공개여야 했다. 전체13글자도 조기에 완료했다.
ScenarioDialogueSessionAdapter가 원본 timer 정책을 선택하게 하여 creation prime,
Double literal .04, update당 한 단위, 잔여 시간 폐기를 적용했다. 전투와 일반 모달의
타이머 정책은 변경하지 않았다. 별도로 새 revision의 같은 본문도 다시 타이핑하도록
공통 setSource 초기화 계약을 바로잡았다.

자연 캡처는 첫 비어 있지 않은 대사에서 멈추던 것을 첫 문장 완료까지 확장했다.
원본 hook의 초기 callback 대체 방식이 unschedule(_handle)의 함수 identity를 깨뜨리는
문제도 발견했다. 원래 callback을 그대로 등록하고 실제 timer.trigger/update만
관측하도록 수정했다. 최종 verifier는 identity 보존, 마지막 update의 scheduler registry
제거, 완료 이후 update 없음,13개 after-draw prefix와 callback 일치를 모두 확인한다.
기존 DialogueLayer.firstText 이벤트도 유지해 이전 회귀 verifier와 호환된다.

최종 자연 source는352 생성→355 첫 글자→391 완료, port는209 생성→212 첫 글자→248
완료였다. 각 실행의 실제 delta로 독립 예측한13개 callback frame이 모두 일치했다.
수정 전 baseline은 같은 oracle에서 실패한다. 절대 frame 번호나 총 로딩 시간이
서로 같다는 의미는 아니다.

Controlled fixture는 실제 DialogueLayer._next와 cc.Scheduler를 실행한다. UI 입력은
continuation 본문 초기화 상태와 label sink로 제한하며 첫 onCreate 재현을 주장하지
않는다. 큰 첫 delta10 prime, Float .04가 Double .04보다 작은 경계, .1f에서도 한 글자,
초과 시간 폐기,13글자 완료·timer 제거·이후 무변화를 포함한다. 모든 입력 delta는
Float roundtrip이 가능하며 Kotlin production session에서 원본 step을 재생한다.
fixture SHA-256:
`a9a311a51fbafd17823539df462e9de89c4b6bc7fb9cef324fef492dbdd4d0de`.

검증: core191개 및 campaign47개, Python opening57개 통과. 추가 source fixture 소비
테스트도 별도 집중 실행에서 통과했다.13개 prefix와 첫 세 분리
대사 화면 strict RGBA 0픽셀 차이 유지. EVENT, 첫 이동, 네 유닛 자산 준비, stage.delay
자연 규칙 회귀 모두 통과. Gradle 캡처·회귀 묶음은60초 제한 안에서20초 완료했다.
Astra가 계획과 production 검수를 맡았고 Sol이 production과 fixture 소비 테스트를
구현했다.

증거: `build/reports/opening-dialogue-timing-20260920/`의 source, controlled,
`game-baseline.json`, `game-final.json`, `baseline-dialogue.json`, `fixed-dialogue.json`,
`prefix-regression.json`, `pages-regression.json`, 각 timing regression과 `regression.log`.
원본 자연 artifact SHA-256:
`712374486a668071e167cb31521c3201792cfb1040baa1c09066e6f880064cfd`.

대사 자동 넘김은 원본1.6초와 포트1초의 차이가 별도로 남아 있다. 초상화 준비 순서,
이동 중 전체 화면, 이후 대사·게임 전체는 이번 타이핑 검증 범위가 아니다.
EVENT 종료→첫 글자 관측은 source2.1314초, port2.162935초이며 전체 구간 동등성은
계속 검증해야 한다. 전체 게임 동등성 목표는 계속 진행한다.


## 첫 Hall 대사 자동 넘김과 다음 대사 prime (2026-09-20)

활성 DialogueSession 경로의 자동 넘김은1초 Float 지연이었다. 원본 DialogueLayer는
자동 닫기 설정을 대사 생성 때 flag로 저장하고, 글자 완료 뒤 scheduleOnce(1.6)를
등록한다. 자연 글자 완료 callback 안에서 등록하면 같은 scheduler 순회에서 prime하며,
입력으로 전체 공개하면 다음 update에서 prime한다. 단순히1초를1.6초로 바꾸는 것에
더해 Double 누적, 등록 phase, one-shot, 취소와 다음 페이지 초기화를 함께 맞췄다.

source-policy 세션은 첫 update의 자동 닫기 설정을 revision 동안 저장한다. 자연 완료는
현재 delta를 재사용하지 않고 primed 상태로 시작하며, 수동 Confirm/RevealAll은 다음
update delta를 초기화에만 쓴다. clear/새 revision은 예약을 지우고 이미 발화한 timer는
다시 실행하지 않는다. 기본 전투·일반 모달의 기존 timer 정책은 유지했다.
자동 callback 후 controller는 다음 revision을 즉시 synchronize하고 delta0으로 prime한다.
그 결과 다음 페이지 glyph가 한 프레임 늦어지는 것도 막았다.

실제 R_00 첫 대사를 쓰는 controller 회귀는 수정 전1개 실패, 수정 후 통과했다.
원본 production _enabledAutoClose/_disAutoClose/_next와 cc.Scheduler를 사용한
controlled fixture는 자연 완료, 수동 공개, flag 비활성, 취소, 두 번째 클릭의5개 사례를
담는다. Float delta를 실제 source-policy session에 재생한다. disabled/cancel 사례는
원본 fixture가 내부 helper를 직접 호출하는 한계를 명시하고 public session의 설정·clear
계약으로 검증한다. removeFromParent mock 이후 component 생명주기는 증명하지 않는다.
fixture SHA-256:
`c448b215d3423e3aa8dcb100dadda881286f268beda4b44668c23f067125b408`.

자연 source는 fresh 검증 프로필에서만 GAME_SETTING bit8을 켜고 실제 UI를 재생했다.
포트는 별도 verification launcher에서 automatedRun의 메모리 설정에 bit8을 켜며,
자동 넘김을 끄는 기존 관찰 모드를 사용하지 않는다. 양쪽 모두 대사 입력·화면 캡처·
격리 없이 첫 문장 완료부터 다음 화자0의 첫 글자까지 관측한다. 사용자 설정 파일은
수정하지 않는다. 원본 callback identity를 유지하며 실제 timer update와 registry 제거를
검사한다.

자연 source 완료389→자동486→다음 glyph489, port 완료246→자동342→다음 glyph345가
각 실행의 실제 delta로 계산한1.6/.04 임계 프레임과 일치했다. 원본의 완료389 프레임에
auto timer가 prime됐고, callback486 프레임에 다음 glyph timer도 prime된 것을 직접
관측했다. 자연 source artifact SHA-256:
`11e8257926813bb18e9f6166431dd5ea0fa805e37a5425651ad5c2be5366d73b`.

검증: core200개, campaign47개, Python opening62개 통과. 자동 넘김을 끈 자연 첫 문장
13개 typing, stage.delay, EVENT, 첫 이동, 네 유닛 준비 규칙 회귀와 첫 세 분리 대사 화면
strict RGBA 0픽셀 차이 유지. 자연 포트 실행8초, 회귀 캡처 묶음14초, 최종 core1초로
모두 외부60초 제한 안에 끝났다. Astra가 설계·검수, Sol이 production·fixture 소비를 맡았다.
증거는 `build/reports/opening-dialogue-auto-20260920/`의 source, controlled,
`game-final.json`, `natural-auto-comparison.json`, `baseline-controller.log`,
`natural-game.log`, `regression.log`, `core-final.log`, 각 regression JSON에 있다.

이번 결과는 설정을 켠 일반 Hall say의 첫 자동 전이와 다음 첫 글자를 증명한다.
명시flag0 설정 override, 동일 원본 DialogueLayer 여러 페이지 사이 설정 변경,
전체 로딩 지연·초상화 준비·이동 중 전체 화면은 별도 검증 대상이다.
전체 게임 동등성 목표는 계속 진행한다.


## 첫 세 대사의 초상화 준비 프레임 (2026-09-20)

완성된 대사 화면의 픽셀 일치와 별개로, 초상화 첫 표시 순서의 차이를 발견했다.
원본 DialogueLayer는 패널·화자·본문을 먼저 만든 뒤 초상화를 비동기로 요청한다.
포트는 첫 draw의 portraitRegion 호출에서 Pixmap decode와 atlas upload를 동기 실행해
대사 생성 프레임부터 초상화를 표시했다. 수정 전 첫 세 페이지 모두 이 차이가 있었다.

원본의 실제 loadByUrl 요청/캐시/콜백과 매 AFTER_DRAW의 양쪽 face를 관측했다.
첫181은 layer Node.298에서 frame349 활성 오른쪽 null→350 frame181 GL-ready였다.
둘째0은 같은 layer·face nodes를 사용하며 frame390 활성 왼쪽 null→391 frame1 ready,
비활성 오른쪽에는181이 남았다. 셋째157은 새 layer Node.312로 바뀌어 양쪽 face가
초기화됐고 frame459 활성 오른쪽 null→460 frame214 ready였다. 따라서 셋째에서
이전 오른쪽181을 임의로 유지하지 않는다. 완료·초상화 준비 후에만 원본 Panel_cancel
실제 클릭 두 번으로 진행했다. 원본 자연 artifact SHA-256:
`e0a875ebb3e22ab00b2ff6d89882506ff5a383c259aba56e29469eb5bd81b891`.

ScenarioSceneAssets의 portraitRegion은 첫 요청에서 worker에 Pixmap decode를 맡기고
null을 반환한다. 완료된 Pixmap만 render thread의 pre/post playback 안전 지점에서
기존 SourceSpriteAtlas.insert로 업로드한다. 동일ID 요청은 병합하며 pending 상태에서
renderer의 portraitTexture 동기 fallback을 막는다. 고정 한 프레임 지연은 넣지 않았다.
실패는 한 번 기록하고 terminal 상태로 남겨 화면 예외·재요청 loop·동기 fallback을
막는다. executor 종료 후 미소비 Pixmap을 정리하며 업로드한 Pixmap도 finally에서 해제한다.

검증용 null 기본 observer는 실제 반환된 region과 요청 전 cache hit를 기록한다.
비로딩 peek만 사용하므로 관측이 자산 준비를 앞당기지 않는다. 포트 standalone launcher는
첫 세 자연 완료 대사와 정상 SPACE 입력 두 번을 거치며 framebuffer readback·격리를
하지 않는다. 요청 portraitId만으로 ready를 판정하지 않고 실제 region의 GL handle,
크기와 atlas 좌표를 사용한다.

수정 후 포트는208→209,248→249,318→319에서 각각 빈 상태→ready였다. 원본과
각 portrait의 atlas rect `[395,2,192,240]`, `[935,2,192,240]`, `[1129,2,192,240]`가
일치한다. comparator는 수정 전 세 페이지 모두 실패, 수정 후 모두 통과했다.
이 결과는 특정 한 프레임 로드 시간이 항상 같다는 주장과 구분한다.

검증: core203개, campaign47개, Python opening66개 통과. 첫 세 완성 대사 화면 및
13개 글자 단계 strict RGBA 0픽셀 차이 유지. EVENT, 첫 이동, 유닛 준비, stage.delay,
첫 대사 typing 규칙 회귀 모두 통과. native 캡처·회귀 묶음은 외부60초 제한 안에서
25초, 최종 core는1초에 완료했다. Astra가 계획·검수를, Sol이 준비 경로를 맡았다.
증거는 `build/reports/opening-portrait-ready-20260920/`의 source, `game-baseline.json`,
`game-fixed.json`, `baseline-readiness.json`, `fixed-readiness.json`, pixel/timing regression,
`fixed-regression.log`, `core-final.log`에 있다.

비활성 side의 일반 재사용, cache-hit 자산의 callback 순서, 화면을 떠난 자산과 일반 atlas
packing 순서, 실패 후 재요청 정책, 전체 프레임 시각 일치는 추가 검증 대상이다.
이번 검증은 첫 세 정상 자산의 활성 face 준비 전이에 한정한다.
전체 게임 동등성 목표는 계속 진행한다.

### 2026-09-20 첫 자연 완료 대사의 전체 framebuffer 검증 추가

첫181 대사 `대장님, 서둘러야 해요!`가 자연 완료된 직후의 전체 화면을 새 검증 단위로
추가했다. source는 EVENT_AFTER_DRAW, 포트는 정상 render observer에서2560×1376
bottom-left RGBA8을 읽는다. 대사 입력·강제 reveal·장면 격리를 하지 않는다.
본문 완료 뒤 portrait 준비를 별도로 기다리지 않으며, capture 시점의4개 유닛 상태와
원본 sprite/texture/좌표 metadata도 기록한다. 포트 renderPlanAtCapture는 캡처 후
계산한 render view이며 실제 GPU 제출 관측으로 주장하지 않는다.

`capture_opening_source_full_scene.cjs`, `captureOpeningFullScene` task,
`verify_opening_full_scene.py`를 추가했다. comparator는 완료 상태·배경·actor 상태·
크기·origin·raw SHA를 검증한 뒤 알파를 포함한 모든 픽셀을 제외 영역 없이 비교한다.
원본을 새로 두 번 실행한 결과 raw SHA가 모두
`64e7d158f61c1547bcfee06f9c7987c9088124657d1792ba478241414bbb4dfc`였다.

수정 전 포트는11,764픽셀 차이로 새 검증에 실패했다. 채널별 차이는R394/G276/B213/
A11,305이며 원본 alpha는255, 포트는191..255였다. 배경과 대사 UI는 일치했다.
이 검증 추가 자체는 전체 화면 일치 달성을 뜻하지 않으며 production 수정은 별도 단위로
진행한다. Astra가 캡처·비교기 read-only 검수를 마쳤고, Python opening comparator
기존64개 테스트 및 Node 문법 검사 통과. 증거는
`build/reports/opening-full-scene-20260920/`의 source, source-repeat, game-baseline,
game-current, baseline-full-scene.json에 있다. 전체 게임 동등성 목표는 계속 진행한다.

### 2026-09-20 Hall 유닛 합성의 alpha 보존 수정

ScenarioBattlefieldRenderer의 actor/head batch에서 RGB blend는 기존
SRC_ALPHA/ONE_MINUS_SRC_ALPHA를 유지하고 alpha는 ONE/ONE_MINUS_SRC_ALPHA로
합성하도록 수정했다. 불투명 배경 위 반투명 유닛 가장자리의 출력 alpha를 원본과
같이255로 보존한다. 이전 네 blend factor를 저장하고 begin/draw/end 예외 경로에서도
finally로 복원하며 색상도 WHITE로 되돌린다.

첫 자연 완료 전체 화면의 차이는11,764→604픽셀로 감소했다. alpha 차이는11,305→0,
나머지는 유닛182 영역 `[1772,1145,1877,1344]`의 RGB 값1 차이이다. 수정 후 raw SHA는
`5449f5d32a5753a2e2623bc36eb7aa26f8ac90cf0a7112baf1521af4e4347c74`.
전체 화면 비교기는 여전히 실패하며604픽셀을 허용하거나 제외하지 않는다.
CPU projection 실험은 결과 bytes가 동일해 모두 되돌렸고 별도 helper는 남기지 않았다.
남은 RGB 차이의 원인은 다음 작업 단위에서 source의 texture/filter/blend 제출 경로로
범위를 좁혀 확인한다.

검증: SourceSlicedPatchClipMath/ScenarioBattlefieldRenderGeometry/ScenarioStoryRenderer
기존6개 테스트 통과. 첫 세 완성 대사 UI와 첫 대사13개 글자 단계 모두 strict RGBA
0픽셀 차이를 유지했다. 캡처를 포함한 회귀 묶음은 외부60초 제한 안에서19초 완료.
Astra가 blend 및 상태 복원을 검수했고 Sol이 production 수정을 맡았다. 증거는 같은
보고서 디렉터리의 game-separate-alpha, separate-alpha-full-scene.json,
regression.log, pages-regression.json, prefixes-regression.json,
game-alpha-final, alpha-final.log에 있다. 첫 전체 화면과 전체 게임의 완전 일치는
아직 달성하지 않았으며 목표는 계속 진행한다.

### 2026-09-20 첫 자연 완료 전체 화면 strict RGBA 일치

남아 있던 actor182 RGB604픽셀 차이의 원인을 실제GPU 텍스처와 assembler 자료로
분리했다. source 도구의 `JOJO_CAPTURE_ACTOR_GPU=1` 진단은 자연 AFTER_DRAW 이후
보존된 SimpleSpriteAssembler vDatas/uintVDatas/indices, renderer view pool 행렬 및
기존 GLtexture의 전체RGBA를 기록한다. gl.uniform 호출 자체를 가로챈 자료는 아니다.
포트도 정상 전체 framebuffer를 먼저 저장한 뒤 기존 unitTextures cache의 GPUtexture를
FBO에 붙여 읽는다. 양쪽 모두 FBO binding을 복원하고 새 asset을 로드하지 않는다.
`verify_opening_unit_textures.py`로 source/port181·182의 전체48×1280 texel이
각각0픽셀 차이임을 확인했다. 따라서 decode/upload 차이가 원인이 아니었다.

원본182 assembler 정점은x1012.465087890625..1108.465087890625,
y664..792, UV의 아래v는0.05000000074505806이었다. 기존 포트는688높이 좌표에서
Float 산술을 먼저 수행해 y571.0400390625..681.1200561523438을 만들었다.
이전 CPU projection 실험은 이미 반올림된 이 좌표를 유지했기 때문에 효과가 없었다.

SourceWorldQuad는800높이 원본 world 좌표에서 Double geometry를 계산하고
정점을Float로 변환한 다음 원본 스케일의 projection을 적용한다. 일반 Hall 좌표식과
유닛96×128/말풍선48×48 크기를 사용하며 특정actor나 캡처 위치 보정은 없다.
축 정렬 orthographic 외 행렬은 기존 SpriteBatch 경로를 사용한다. 재사용 buffer/matrix와
begin/end 예외 시 두 행렬 복원을 적용했다. production은 기존 원본 sprite assets로 그리며
캡처 framebuffer를 재생하지 않는다.

수정 후 첫181 대사 자연 완료 전체 화면은2560×1376의 모든RGBA에서0픽셀 차이다.
source와 포트 raw SHA-256은 모두
`64e7d158f61c1547bcfee06f9c7987c9088124657d1792ba478241414bbb4dfc`.
source GPU 진단 on/off도 같은 전체 화면 hash이며, 최종 예외 복원 보강 후 재캡처에서도
일치를 유지했다. 기존 첫 세 완성 대사 UI·첫 대사13개 prefix도 모두strict0 유지.
관련 core6개와 기존 Python comparator64개 통과, Node 문법·diff검사 통과.
회귀 묶음12초, 마지막 core+전체 캡처14초로 외부60초 제한 내 완료했다.

Astra 계획·검수, Sol production 수정, source agent GPU 진단을 사용했다. 증거는
`build/reports/opening-unit-gpu-20260920/`의 source, game-baseline, game-fixed,
game-final, texture-comparison.json, fixed-full-scene.json, final-full-scene.json,
pages-regression.json, prefixes-regression.json, regression.log, final.log에 있다.
이번 전체 화면 일치는첫 자연 완료 프레임에 한정한다. 이동 중 subpixel 좌표,
다른 종횡비 및 이어지는 대사의 전체 화면·이후 게임 흐름은 추가검증 대상이며
전체 게임 동등성 목표는 계속 진행한다.

### 2026-09-20 첫 세 자연 완료 대사의 전체 화면 검증

첫 자연 완료 전체 화면 검증을 정상 입력 두 번으로 이어지는 첫 세 대사까지 확장했다.
새 source full-pages 도구와 OpeningFullPagesDesktopLauncher/captureOpeningFullPages는
장면 격리·강제 reveal·portrait 준비 대기 없이 첫 본문 자연 완료 시점의 모든RGBA를
저장한다. 원본은 최초 semantic completion frame과 AFTER_DRAW capture frame가 같음을
assert한다. 포트는 정상 render observer의 최초 완료에서 캡처한다.

원본 CDP pointer 요청과 실제 DialogueLayer._next 처리 시점을 구분했다. _next는
pass-through 관측만 하며 정상 Panel_cancel pointer 경로가 실행한다. inputs의 frame/time은
실제 처리값이고 요청값은 inputRequests에 별도 보존한다. 포트는 캡처 다음 observer frame에
InputProcessor.keyDown/keyUp(SPACE)를 보낸다. 두 실행의 입력 지연을 같다고 주장하지 않고
완료 캡처 이후·다음 대사 캡처 이전의 정상 입력 두 건임을 검증한다.

verify_opening_full_pages.py는 자연 partial prefix, 최초 완료 endpoint, 실제 입력 순서,
화자·본문·side, source layer lifecycle, port revision, actor 위치·방향·행동과 source말풍선을
검증한 뒤 모든RGBA를 비교한다. 첫 두 대사의 source layer는같고 셋째는새layer이며,
셋째는 중간 이동 이후의181(40,60),0(40,50),182(40,40),157(54,50) 배치와방향을검증한다.
어느 페이지의 어떤 channel도 제외하지 않는다.

세 전체 화면이 모두0픽셀 차이로 일치했다. source/port 각각 동일한 raw SHA-256:
- 181 `대장님, 서둘러야 해요!`: `64e7d158f61c1547bcfee06f9c7987c9088124657d1792ba478241414bbb4dfc`
- 0 `알아!`: `71075a2010075b03f8702aa5cdcf2065df085e6e0e942a176114cfe5f148791d`
- 157 `잠시만 기다려 주세요!`: `9496cea99cf6c1382604596d5f3a84cc1a222f420526ea91114c4834471c0977`

source fresh실행 약8초, port baseline8초/metadata보강후final7초로 bounded 실행을 마쳤다.
기존 Python comparator64개 통과, 새 계약의6개 잘못된 metadata 사례(늦은캡처,
완료전입력, 격리, partial누락, stale revision, 이전이동위치)는모두거부했다.
Node 구문·diff검사 통과. Astra 계획·검수, Sol 포트 캡처, source agent 원본 캡처를사용했다.
이번 단위는 production 변경 없이 기존 수정의 전체 화면 검증 범위를 확장한 것이다.
증거는 `build/reports/opening-full-pages-20260920/`의 source, game-baseline, game-final,
full-pages.json, final-full-pages.json, negative-contract-checks.json, game-final.log에 있다.
이동 중 프레임 및 네 번째 이후 대사·후속 게임 흐름은 아직추가검증 대상이며 목표는계속한다.

### 2026-09-20 동일 Float32 시계의 첫 이동 화면 검증

실행별 자연 delta가다른 상태에서 서로다른 이동 순간의 pixel을비교하지 않도록,
검증 전용으로 양쪽에 Float32 `1f/60f`(`0.01666666753590107`, bits1015580809)를
공급했다. source는 Director.calculateDeltaTime 원본호출 뒤_deltaTime만고정하고
component/scheduler/action/animation/render를원래경로로실행한다. 포트는 verification의
Graphics proxy가 getDeltaTime/getRawDeltaTime만바꾸며 game.render()를그대로호출한다.
proxy는각render의finally에서복원한다. production hook·수동 pose·입력·격리는없다.
이 결과는 자연 wall-clock 실행 프레임 동등성 주장이 아닌 controlled-clock 검증이다.

첫181(40,5→15)의실제 ActionManager Sequence 상태로 prime ordinal0을정렬했다.
0..24의25tick을기록하고1/6/12/13/18/19/24의7개 전체RGBA를캡처했다.
source root action의elapsed/firstTick/duration/done을기록해 geometricProgress로
시계를대체하지않았다. ordinal24 elapsed는0.40000002086162567로 실제duration
0.40000011920928963보다작다. 완료직후다음유닛들의비동기등록이개입하므로 완료frame은
이번pixel범위에서제외했다. 기존자연이동완료 timing검증을대체하지않는다.

source world canvas는800높이이므로 초기1/6의유닛은화면밖이다. 12/13/18/19/24는
유닛일부가보이며,13/19는소수좌표이동을검사한다. sourceassembler 정점/UV/행렬과
실제스프라이트row도각tick에기록했다. port renderPlan은관측후계산한renderer선택값이다.

verify_opening_first_move_pixels.py는고정시계bit·실제actionclock·prime·연속tick·
캡처와tick의동일성·sourcepath를검증한다. source node위치에서역산한Float32격자좌표와
port좌표, 스프라이트row는25tick모두일치했다. 모든7개전체RGBA도0픽셀차이였다.
초기1/6은배경만비교한다는한계를보고서에명시했다. production수정은추가하지않았다.

sourcefresh약7초, port추가subpixel표본캡처3초로bounded실행을마쳤다. 기존Python
comparator64개통과, 잘못된시계·미완료prime·잘못된actionelapsed·표본누락·다른tick캡처·
조기완료의6개계약위반을거부했다. Node구문·diff검사통과. Astra계획/검수,
Sol포트검증launcher, source agent원본도구를사용했다. 증거는
`build/reports/opening-first-move-pixels-20260920/`의source,game-baseline,game-subpixel,
first-move-pixels.json,negative-contract-checks.json에있다.
다음은첫group이동의각유닛prime과화면안쪽이동을검증한다. 전체게임목표는계속한다.

### 2026-09-20 첫 3인 group 이동의 전체 화면 검증

동일Float32 1/60 시계검증을 첫unitsMove의181(40,15→25),0(40,5→15),
157(54,95→85)로확장했다. source실제AStar경로는모두11점의직선이며 실제rootSequence
기간은각0.40000011920928963이었다. source각_move2등록을관측하고 각actor의
실제elapsed0/firstTick=false/action20을확인해동시prime임을검증했다.
포트도세개의새이동경로와elapsed0/moveJustStarted=false가같은frame에관측됐다.
포트firstObserved*는render후최초관측값이며 registrationcallback시점으로주장하지않는다.
비동기texture준비순서나이동시작을검증도구가강제하지않았다.

source_first_group 도구와OpeningFirstGroupFramesDesktopLauncher/
captureOpeningFirstGroupFrames를추가했다. pose나장면을수정하지않고 각실제이동의
0..24tick, 전체RGBA ordinal1/6/12/13/18/19/24를기록했다. 유닛집합은정확히
0/157/181이고182는없다. 완료프레임에는다음182의비동기생성이개입하므로제외했다.

verify_opening_first_group_pixels.py는source실제경로·기간·rootactionclock,
각prime·tick연속성·원점논리좌표·행동·방향·texture짝과sprite row를검증한다.
source node의두축을독립적으로역투영해격자X/Y를복원하므로경로수직방향오차도검출한다.
25tick×3actor의75개상태에서Float32격자좌표및sprite row가일치했고,
7개전체RGBA도모두0픽셀차이였다. 부분적으로만보였던첫단독이동보다이번에는
181과157의몸체를화면안쪽에서더검증했다. 자연wall-clock이나subframe callback동등성은
이번controlledframe결과의범위가아니다.

sourcefresh약7초, portcapture및metadata보강후재캡처각3초완료. 기존Python
comparator64개통과. prime불일치·182조기등장·actor시계오차·texture오류·source경로변경
등6개계약위반과캡처없는tick의source수직방향위치오차1개를모두거부했다.
Node구문·diff검사통과. Astra계획/검수에서역투영gate를보강했고Sol포트도구,
source agent원본도구를사용했다. production변경없이검증범위를확장했다.
증거는 `build/reports/opening-first-group-pixels-20260920/`의source,game-baseline,
baseline-pixel-diagnostic.json,first-group-pixels.json,negative-contract-checks.json에있다.
다음은첫대사직전4인 group이동이며, 전체게임동등성목표는계속진행한다.

### 2026-09-20 최종 4인 이동의 Hall 애니메이션 시간 정밀도 수정

첫 대사 직전 마지막 unitsMove를 동일 Float32 1/60 시계로 검증했다.
181·157은 20칸 직선(0.8000001192092896초)이지만, 0·182는 점유된 칸을
피하는 22칸 우회 경로(0.88초)였다. 실제 source 경로와 root Sequence를 기록했다.
네 유닛의 실제 prime을 ordinal0으로 정렬했으며, 직선 유닛은49, 우회 유닛은53에서
완료한다. source의 그룹 resume와 delay(3)도 마지막 완료53에서 발생했다.

55개 tick의 220개 actor 상태를 비교하면서 ordinal45의 157·181 walking row가
원본1, 포트2인 차이를 발견했다. source AnimationState의 Double 시간은
0.7500000391155481초지만 포트 Float 누적은0.749999940초였다.
Hall 전용 Double 애니메이션 시계를 추가하여 누적부터 sprite row 선택까지 유지하고,
이동 시작과 방향 전환에서 reset한다. 기존 battle Float 누적 분기는 유지한다.
실제 animator의45tick 회귀 테스트와 row 경계 테스트를 추가했다.

검증 도구는 모든55개 전체RGBA 프레임의 SHA-256을 기록하고, 그중13개 rawRGBA를
보존·직접 비교한다. 수정 전 ordinal45는36,379픽셀이 달랐고 수정 후0픽셀이 되었다.
수정 전후55개 중 바뀐 프레임은45 하나이며 다른54개는 그대로다.
수정 후220개 상태와13개 raw 표본은 모두 일치한다.

그러나 전체55개 hash gate는 아직 실패한다. 수정 전 불일치 [2,45,47] 중
45만 해결되었고 [2,47]은 다음 작업 단위의 미해결 항목이다. 선택 표본의 성공을
전체 이동의 성공으로 주장하지 않으며 comparator도 exit1을 유지한다.
원본/포트의 자연 wall-clock 및 등록 callback의 subframe 동등성도 이번 범위 밖이다.

기존 Python comparator64개 통과, digest누락·조기 actor완료·조기 그룹resume·
raw/digest불일치의4개 계약 위반을 거부했다. Node구문검사 통과.
Astra는 Hall Double 수정에 blocker 없음을 검수했다.
증거는 `build/reports/opening-final-group-pixels-20260920/`의 source,
game-baseline, game-current, baseline-final-group.json, fixed-final-group.json,
negative-contract-checks.json에 있다. 전체 게임 동등성 목표는 계속 진행한다.

수정 후 HallUnitRenderTest·ScenarioRuntimeTest 통과. 새 캡처로 첫 단독 이동7프레임,
첫3인 이동7프레임, 첫 대사3페이지 전체 화면을 재검증하여17개 모두0픽셀 차이를 확인했다.
