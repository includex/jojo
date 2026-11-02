# 배포 전투 이미지 배정

사용자가 제공한 `jojo_asset/release` 및 `release_character_descriptions.csv`에서 선정했습니다. 공격·이동·특수 PNG는 같은 set ID의 세트를 사용하며, 픽셀·투명도·프레임 순서를 변경하지 않습니다. 원본 게시글 제목과 CSV 설명, SHA-256은 `selection.json`에 기록합니다. CSV 분류는 참고 정보이며 실제 이미지도 확인했습니다.

| 게임 인물/역할 | 아바타 | 배포 세트 | 선정 이유 |
|---|---|---|---|
| 임충 | 601 | `set_5fa8a797cabdb3c647e7` | 원본 게시글이 임충으로 지정한 전용 조형. 고유 복장과 방향별 동작을 한 세트로 사용. |
| 노지심 | 602 | `set_b782ee65c555db321396` | 노지심 전용 선장·승복 조형. |
| 허정 | 603 | `set_ac90025cdd64cef7547c` | 수수한 갈색 평민 복장으로 보급 인부 역할 표현. |
| 진오 | 604 | `set_1321c283632d9ee6301e` | 가벼운 청색 복장의 청년 보병. |
| 주귀 | 605 | `set_c13873bc5f8784f57695` | 두건과 가벼운 무장으로 수로의 호걸 표현. |
| 홍교두 | 606 | `set_6f4dd90db9ef4d0f2626` | 긴 무기와 무인 복장으로 무예 교두 표현. |
| 양지 | 607 | `set_6615d2c0cefbd2d6852a` | 양지 전용 보병 조형. |
| 왕륜 | 608 | `set_ef97c41d7e33d382d877` | 문무를 겸한 산채 두령의 관모·장포 복장. |
| 시진 | 609 | `set_5eb567e9446dcbe538ab` | 시진 전용 조형. |
| 송만 | 610 | `set_f44d2fdb988dfdf80fb9` | 수호지 호걸 조형을 산채 무장에 배정. |
| 동초 | 611 | `set_3e085928d0e944a5db83` | 청색 투구와 방패를 갖춘 호송 관원. |
| 설패 | 612 | `set_b95290f8294d7ec0f1e7` | 동초와 같은 계열의 붉은색 호송 관원. |
| 육겸 | 613 | `article179924_variant04` | 갑주와 검을 갖춘 관군 무장. |
| 부안 | 614 | `set_20c866cc244b07858d09` | 붉은 경장 보병으로 추격 수하 표현. |
| 관군 | 615 | `article179924_variant01` | 일반 관군용 갑주·방패 보병. |
| 산적 | 616 | `set_bf4957c56fc5155b3ae0` | 산적 전용 두건·검 보병 1단계. |

## 신규 캐릭터 추가 원칙

전투 캐릭터 이미지는 직접 생성하지 않고 `jojo_asset/release`의 완전한 공격·이동·특수 세트를 사용합니다. `release_character_descriptions.csv`와 실제 프레임을 함께 확인해 선정하고 출처와 해시를 기록합니다. 개별 동작에 다른 캐릭터의 세트를 섞지 않습니다. 대화 초상화는 이 교체 정책의 대상이 아닙니다.

## 다시 적용하기

저장소에 선택한 48개 원본 PNG를 함께 보관하므로 다른 컴퓨터에서도 외부 경로 없이 패키징할 수 있습니다.

```sh
python3 scripts/package_linchong_assets.py
python3 tools/verify_linchong_sprites.py
python3 scripts/smoke_linchong.py
```

다른 세트로 교체할 때 `selection.json`의 `setId`와 선정 이유를 수정한 뒤 다음 명령으로 CSV 메타데이터·해시·원본 파일을 갱신하고 위 패키징을 실행합니다.

```sh
python3 scripts/import_linchong_release_sprites.py --source-root /Users/ain/workspace/jojo_asset
```

`release`에는 전투용 세트만 있으므로 대화 초상화와 지도, 수레·상자 이미지는 기존 자료를 유지합니다. 엔진/core, 타임라인, 캐릭터 ID, 능력치와 전투 규칙은 변경하지 않습니다.
