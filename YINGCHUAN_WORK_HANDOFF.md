# 영천전투 작업 중지 및 재개 안내

## 2026-09-21 추가 진행

- `postsCanEquip`의 WEAPONS·ARMOR·AUXILIARY 갈래를 원본 `Item.js:282-305`에 맞춰
  구현했다. `posts` EQUIPS(10), `item` ARMS(7)·UPGRADE_ARM(8) 원자료 접근자를 추가하고
  실제 카탈로그를 화면 그리기와 렌더 기록기에 연결했다 (`3776a93`, `ead21ff`, `f0dd532`).
  소모품 기본 갈래는 그대로 유지된다.
- 장착 가능·불가 직위 색의 기록기 테스트, `use-property-detail` 원본 대조,
  `:core:test :verification:test`가 통과했다. 세 단위를 모두 `origin/main`에 push했다.
- 이전의 미완 작업 파일은 스테이징하지 않았다. `BattleScreen.kt`는 새 수정 네 hunk만
  따로 스테이징했다. 다음 우선순위는 남은 전투 렌더 기록기와 전당 `battle-view` 색 비교다.

---

## 2026-09-21 재개 결과

이 아래의 2026-09-20 상태·"남은 일"은 당시 기록이다. 이번 재개 작업으로 다음을 완료해
모두 `origin/main`에 push했다.

- 피격 뒤 다음 물리 패스가 바로 시작하던 경로를 수정했다 (`c2f9e43`). 피격 종료가 실제
  화면에 한 번 반영된 뒤 다음 패스를 시작한다. `first-round-end`의 피격→반격 간격은
  포트 0.5833초에서 0.6000초로 바뀌어 원본 0.6329초와의 차이가 0.0329초가 됐다.
  `first-normal-combat`, `next-normal-actions`, `first-round-end` 세 구간 모두 통과했다.
- 남았던 구간 7개(`round2-handoff`, `single-player-action`, `round2-followup`,
  `round2-first-combat`, `round3-first-combat`, `round3-followup`, `round3-counterattack`)를
  원본·포트 trace로 모두 대조해 통과했다. 뒤의 세 구간은 가장 긴
  `round3-counterattack` trace 한 쌍에서 각각 검사했다. 비교 스크립트 네 곳의 빠진
  `timing` 계산을 복구했고, `round3-followup`의 이동 완료→공격 시작 경계에 있던 원본
  정체 프레임(0.1251초)을 판정 허용치에 반영했다 (`5650c3f`, `eaf94f7`, `4749c5d`).
- 손으로 적힌 렌더 기록기 세 곳을 실제 그리기 값과 연결했다. 패배 확인창의 "비" 라벨
  (`1dd2c20`), 미니맵 화면 범위 상자와 비교기의 캔버스 크기 (`44a7d7a`), 전장 편집창
  제목 띠 (`8d0efae`)다. 각 경로는 정상 대조와 값 변경 음성 대조를 통과했다.
- 위 수정 뒤 `:core:test :verification:test`는 통과했다. 기존 미완 작업 파일은 그대로
  작업 트리에 남겼고, 커밋은 해당 hunk만 골라 만들었다.

다음 우선순위는 아직 고정 표를 쓰는 나머지 전투 렌더 기록기, `postsCanEquip`의
WEAPONS·ARMOR·AUXILIARY 갈래, 전당 `battle-view` 색 비교다. 장시간 캡처의 원본·포트
trace는 `/tmp/src-*`와 `verification/build/verification/yingchuan-walkthrough/`에 있고
영구 보관 자료가 아니므로 재현 시 다시 캡처해야 한다.

---

기록일: 2026-09-20 (갱신)

상태: **사용자 요청으로 중지. 모든 커밋은 `origin/main`에 push 완료.**

이 문서는 **다음 세션이 바로 이어받도록** 쓴 것이다. 세 문서의 역할은 이렇다.

| 문서 | 내용 |
| --- | --- |
| `YINGCHUAN_WORK_HANDOFF.md` (이 문서) | 지금 상태, 재개 절차, 명령, 노하우 |
| `YINGCHUAN_SESSION_SUMMARY_20260920.md` | 이번 작업의 한 장 요약 |
| `YINGCHUAN_PARITY_PROGRESS.md` | 전체 경위(가장 자세함) |

---

## 1. 지금 상태

### 저장소

- `main`이 `origin/main`과 같다. **미push 커밋 없음.**
- **작업 트리에 미완 작업 19개 파일이 커밋되지 않은 채 남아 있다.** 건드리지 말 것.
  - `core/.../battle/BattleScreen.kt` (+417/−56)가 그 중심이고, 다른 파일들이 그 변경과
    맞물려 있어 **따로 떼어 커밋하면 컴파일되지 않는다**(실제로 확인했다).
  - 이번 세션은 이 미완 작업을 피해 `git apply --cached`로 **자기 hunk만 골라** 커밋했다.
    같은 방법은 아래 §4에 적었다.

### 검증 상태

| 항목 | 상태 |
| --- | --- |
| 영천 전투 렌더 경로 27개 | **전부 통과** |
| 색 비교 | **909행 전부** (시작 시 28행) |
| 구간 타이밍 대조 13구간 | **6구간 실행** — 5건 통과, `first-round-end`에서 결함 발견 |
| `:core:test` / `:verification:test` | 둘 다 초록 |

### 실행하지 않은 구간 7개

`round2-handoff`(150) · `single-player-action`(180) · `round2-followup`(180) ·
`round2-first-combat`(180) · `round3-first-combat`(210) · `round3-followup`(210) ·
`round3-counterattack`(240). 괄호는 필요한 초 수다. 스크립트는 모두 새 판정 규칙으로
고쳐 두었으니 **캡처해서 돌리기만** 하면 된다(§3).

---

## 2. 가장 먼저 할 일: 미수정 결함 하나

**피격 뒤 반격이 한 프레임 이르다.** 증거와 기전이 모두 확보돼 있다.

```
원본: +0.0000 anime32(피격)  +0.6165 anime0(대기)  +0.6295 anime25(반격)
포트: +0.0000 anime32(피격)  +0.5833 anime25(반격)          ← 대기가 없다
```

- 포트는 반격 시작을 `reactionEndsAt = hitAt + requireSourceActionDuration(32, dir)`,
  곧 클립 길이(14틱/24 = 0.5833초)가 끝나는 **정확한 시각**으로 잡는다.
- 원본 `battle/BattleUnit.js:1921-1927`은 `cc.Animation.EventType.FINISHED` 콜백으로 다음
  상태를 세운다. Cocos는 그 이벤트를 마지막 프레임을 **지난 다음 update에서** 쏘므로 한
  프레임이 기본 자세로 남는다.
- `first-round-end`에서 -0.0666(원본 자체 편차 0.0168), `next-normal-actions`에서 -0.0412로
  부호·크기가 같아 **계통적**이다.

**고칠 때 주의**: `scheduleHitReaction` 호출부가 `BattleScreen.kt`에 여덟 군데이고 모두
`reactionEndsAt`을 공유한다. 한 곳만 바꿀 수 없다. 바꾼 뒤에는 최소 `first-round-end`,
`next-normal-actions`, `first-normal-combat` 세 구간을 다시 돌려 회귀를 봐야 한다.

**하지 말 것**: "2 프레임 더하기" 같은 상수를 박는 것. 이 저장소가 되풀이해 당한 형태다
(하드코딩 대역). 클립 종료를 **클립이 끝난 뒤 첫 업데이트**로 잡는 구조로 바꿔야 한다.

---

## 3. 재개 절차 (그대로 따라 하면 된다)

### 3.1 검증 classpath 만들기 (렌더 경로용)

```bash
cd /Users/ain/workspace/jojo
cat > /tmp/cp.init.gradle.kts <<'EOF'
gradle.projectsEvaluated {
    rootProject.project(":verification").tasks.register("printVerificationCp") {
        doLast {
            val ss = project.extensions.getByType(org.gradle.api.tasks.SourceSetContainer::class.java)
            println("CLASSPATH=" + ss.named("main").get().runtimeClasspath.asPath)
        }
    }
}
EOF
./gradlew -I /tmp/cp.init.gradle.kts :verification:printVerificationCp :verification:classes -q \
  | grep '^CLASSPATH=' | sed 's/^CLASSPATH=//' > /tmp/cp.txt
export JOJO_VERIFICATION_CLASSPATH="$(cat /tmp/cp.txt)"
```

### 3.2 렌더 경로 돌리기

```bash
node tools/verify_render_parity_routes.mjs battle-menu magic-list use-property-detail
# 경로 이름은 tools/render_parity_routes.json 에 있다. 전투 관련은 앞 28개.
```

경로를 고친 뒤에는 **반드시** `./gradlew :verification:classes` 를 먼저 돌려야 한다.
그러지 않으면 낡은 클래스로 돌아 초록이 나온다(실제로 한 번 속았다).

### 3.3 구간 타이밍 대조 (한 구간 = 캡처 2번 + 비교 1번)

```bash
# 포트 쪽 (--rerun-tasks 없으면 UP-TO-DATE로 건너뛴다)
./gradlew :verification:captureYingchuanWalkthrough --rerun-tasks \
  -Pjojo.yingchuanWalkthrough.captureMode=round2-handoff \
  -Pjojo.yingchuanWalkthrough.maxSimSeconds=150
# 결과: verification/build/verification/yingchuan-walkthrough/yingchuan-manual-trace.json

# 원본 쪽 (인자 순서: sourceRoot outputDir maxWallMs mode)
node tools/capture_yingchuan_source_walkthrough.cjs \
  ../jojo_mobile/sgccz-desktop /tmp/src-r2-handoff 150000 round2-handoff
# 결과: /tmp/src-r2-handoff/source-full-trace.json

# 비교
python3 tools/yingchuan-comparisons/round2-handoff.py \
  /tmp/src-r2-handoff/source-full-trace.json \
  verification/build/verification/yingchuan-walkthrough/yingchuan-manual-trace.json
```

구간별 모드 이름·초 수·스크립트 대응은 `tools/yingchuan-comparisons/README.md` 표에 있다.
**포트의 기본 시뮬레이션 시간은 30초라 대부분의 구간을 잘라 먹는다.** 표의 값을 꼭 줄 것.

---

## 4. 미완 작업을 건드리지 않고 커밋하는 법

`BattleScreen.kt` 같은 파일에 내 변경과 남의 미완 변경이 섞여 있을 때 쓴다.

```bash
git diff -U3 <파일> > /tmp/full.patch
# /tmp/full.patch 를 @@ 단위로 쪼개서 내 변경이 들어 있는 hunk만 고른 뒤
git apply --cached --check /tmp/mine.patch && git apply --cached /tmp/mine.patch
git diff --cached --numstat   # 담긴 양을 눈으로 확인한다
```

**반드시 `git diff --cached`로 무엇이 담겼는지 확인할 것.** 이번 세션에서 hunk 선택이
남의 변경을 끌어온 적이 있고, 그 확인으로 걸렀다.

테스트는 미완 작업이 섞인 상태로 돌릴 수밖에 없다(떼어내면 컴파일 실패). 그 한계를
알고 돌린다.

---

## 5. 작업 노하우 (이번 세션에서 값을 치르고 배운 것)

### 5.1 초록을 믿지 않는다 — 음성 대조

게이트가 통과했다고 검증됐다는 뜻이 아니다. **값을 하나 바꿔 실제로 떨어지는지 확인하고
되돌린다.** 떨어지지 않으면 배선이 겉도는 것이다. 이번에 이 절차로 여러 번 걸렀다.

```bash
sed -i.bak 's/const val X = 52f/const val X = 53f/' <계약파일>
./gradlew :verification:classes -q && node tools/verify_render_parity_routes.mjs <경로>
mv <계약파일>.bak <계약파일>    # 반드시 되돌린다
```

### 5.2 같은 값을 두 곳에 적지 않는다

이번에 고친 결함 **대부분**이 같은 이유로 숨어 있었다.

> 같은 화면의 값이 **그리기와 증거 두세 곳에 따로** 적혀 있으면, 각자 자기 값과 일관될 때
> 게이트가 정직하게 돌면서도 아무것도 검증하지 못한다.

고치는 방법은 화면마다 계약 object 하나를 단일 출처로 두고 그리기와 증거가 **둘 다 읽게**
하는 것이다. 값은 언제나 **원본과 일치하는 쪽**을 정본으로 삼는다.

전투 렌더 기록기 16개 중 아직 7개가 손으로 적은 표를 들고 있다(미니맵 `bg/box`, 편집
레이어 머리띠, 패배 프롬프트 `"비"` vs `"아니오"` 등). 배선하면 곧바로 빨갛게 떨어질 것들이
섞여 있다.

### 5.3 비교기는 한쪽이 null인 항목을 건너뛴다

`compare_render_logs.py`는 `color`/`outline` 중 한쪽이 null이면 **그 항목을 통째로 건너뛴다.**
증거가 색을 적지 않으면 포트가 눈에 띄게 다른 색을 그려도 게이트가 초록이다. 영천 전투
경로는 지금 909행 전부가 색을 비교받는다. **이 상태가 깨지면 회귀다.**

채울 때 규율: **포트가 실제로 아는 색만 적는다.** 원본 하네스가 `#ffffff`를 낸다는 이유만으로
흰색을 적으면 우연히 맞는 거짓말이다. 그리기 코드에서 `batch.color`/`font.color`가 어디서
세워지고 어디서 바뀌는지 먼저 읽는다.

### 5.4 타이밍 차이를 만나면 순서를 지킨다

1. **원본을 두 번 돌려 자체 편차부터 잰다.** 어떤 지표는 원본이 스스로 0.278초 흔들린다.
2. **편차가 좁아도 안심하지 않는다.** 캡처는 같은 의미 지점에서 찍히므로 오염이 **결정적**
   이다. 매 실행 같은 창을 같은 크기로 부풀린다.
3. **창 안의 정체 프레임(dt > 0.05)을 본다.** 정체는 창의 참값을 바꾸지 않지만 경계 프레임을
   놓치게 해 창을 길게 **보이게** 한다. 지금은 대조 스크립트가 이를 자동으로 허용치에
   반영한다(고정 폭 + 양쪽 정체 폭).
4. **클립 시계(trace 유닛 tuple 인덱스 15 = `cc.AnimationState.time`)로 다시 잰다.**
   애니메이션 길이는 벽시계가 아니라 클립 시계로 본다.
5. **같은 실행 안의 동종 측정과 비교한다.** 한 구간의 대사 셋 중 둘이 글자당 0.055초인데
   하나만 0.173초면 그 하나가 이상값이다.

이 순서를 건너뛰어 존재하지 않는 포트 결함을 보고할 뻔한 적이 여러 번 있다.

### 5.5 경계를 못 찾으면 통과가 아니라 실패다

대조 스크립트가 `StopIteration`으로 멈추게 둔다. 관측하지 못한 것을 조용히 건너뛰면
없는 합격이 만들어진다.

### 5.6 "중복으로 보이는 코드"를 확인 없이 지우지 않는다

`tools/yingchuan-comparisons/round3-210.py`는 `summarize`가 세 번 정의된다. "앞의 둘은
버려진다"고 보고 지웠다가 `NameError`로 깨뜨렸다 — **마지막 판정부가 첫 블록의 `WALL_ONLY`를
읽는다.** 되돌렸고 파일 머리에 경고를 적어 두었다. 정리하려면 합치기 전후 출력이 같은
trace 쌍에서 **바이트 단위로 같은지** 먼저 확인해야 한다.

### 5.7 공유 직렬화를 바꾸면 모듈 전체 테스트를 돌린다

`RenderEventLog`에 필드 하나를 더했다가 golden 테스트 둘을 깨뜨렸고, 집중 테스트만 돌려
눈치채지 못했다. 공유 계약을 건드렸으면 `:core:test :verification:test`를 통째로 돌린다.

---

## 6. 환경 주의

- **원본과 포트를 동시에 돌리지 않는다.** 포트 실행 중 Gradle 빌드를 돌리면 `core.jar`가
  다시 쓰여 자산 읽기가 실패한 이력이 있다.
- **디스크**: 한 구간 trace가 150~390MB다. 볼륨이 98%(9.6GB 여유)다. `build/`가 47GB,
  `verification/build/verification/`이 캡처 디렉터리를 쌓는다. 이번에 사용자 승인을 받아
  오래된 캡처 80개를 지워 9.9GB를 확보했고 최신 6개만 남겼다. **구간을 이어 돌리려면
  공간을 계속 살펴야 한다.**
- **`npm`이 PATH에 있어야 한다.** 원본 캡처 도구가 `npm exec -- electron`을 spawn한다.
- 원본 캡처 도구 인자는 **위치 인자**다: `sourceRoot outputDir maxWallMs mode`.
  `--mode=` 같은 플래그를 주면 sourceRoot로 해석돼 엉뚱한 곳에서 spawn한다.
- 이번 세션의 trace 파일은 세션 scratchpad에 있어 **세션이 닫히면 사라진다.** 다시 캡처해야
  한다(§3.3).

---

## 7. 남은 일 (우선순위 순)

1. **반격 타이밍 결함 수정** (§2). 증거와 기전이 다 있다.
2. **구간 대조 7개 실행** (§1). 스크립트는 준비됐다.
3. **전투 기록기 7개의 손으로 적은 표를 계약에 배선** (§5.2). 배선하면 곧바로 떨어질
   것들이 섞여 있다 — 그게 결함을 드러낸다.
4. `postsCanEquip`의 WEAPONS·ARMOR·AUXILIARY 갈래. posts 표의 EQUIP 비트와 아이템의
   UPGRADE_ARM 목록이 포트 데이터 접근자에 없다. **값을 지어내지 않고** 함수 주석에 원본
   줄번호와 함께 남겨 두었다(`UsePropertyDetailRenderContract.postsCanEquip`).
5. `battle-view` 색 비교 0/16 — 전당 화면이라 영천 범위 밖.
6. 글꼴 치환 ~2px 오차 — 원본은 Chromium Arial, 포트는 Apple SD Gothic Neo. 가장 낮음.
