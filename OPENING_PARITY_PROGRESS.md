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
