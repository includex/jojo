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
