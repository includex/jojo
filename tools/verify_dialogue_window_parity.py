#!/usr/bin/env python3
"""대화창(SayLayer 말풍선) 렌더링만 분리해 원본 캡처와 대조한다.

거리 대사 캡처의 panel/portrait/speaker/text 단계는 검은 배경 위에 대화창
구성요소만 누적해서 그린다. 따라서 이 네 단계의 프레임은 그 자체가 "대화상자
렌더링만 떼어낸" 이미지이며, 연속 단계를 차분하면 구성요소별 픽셀이 분리된다.

원본 캡처:  <captures>/source-street-<stage>.rgba
게임 캡처:  ./gradlew :verification:captureDialogueStages 산출물

좌표는 캡처 픽셀(2560x1376) -> 논리(1280x688) -> 원본 설계(1488.372x800)로
환산해 보고한다. 설계 공간이 원본 Cocos 노드 좌표와 같은 단위다.
"""
from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

from PIL import Image, ImageChops

WIDTH, HEIGHT = 2560, 1376
CAPTURE_SCALE = 2.0          # 캡처 픽셀 / 논리 픽셀
DESIGN_SCALE = 0.86          # 논리 픽셀 / 설계 픽셀
STAGES = ("panel", "portrait", "speaker", "text")
INK_THRESHOLD = 32           # 안티에일리어싱 헤일로를 제외한 실제 글자/그림 임계값

# 위치 허용 오차의 근거: 캡처 1픽셀 = 0.581 설계 단위. 원본 Cocos는 글자 가장자리를
# 여러 픽셀에 걸쳐 부드럽게 램프시키고 게임의 FreeType 래스터는 경계가 단단하다.
# 같은 좌표에 그려도 임계값 기반 bbox가 3픽셀(약 1.7 설계 단위)까지 어긋난다.
# 따라서 글자 구성요소는 이 범위를 레이아웃 오차로 판정하지 않는다.
DEFAULT_TOLERANCE = 2.0

# 검증 대상별 원본/게임 캡처 위치와 파일 이름 규칙이다.
TARGETS = {
    "street": {
        "captures": Path("/Users/ain/workspace/jojo 복사본/.port-isolated/asset-recovery-audit/captures"),
        "source": "source-street-{stage}.rgba",
        "game": Path("verification/build/verification/dialogue-stages"),
        "gameFile": "game-{stage}.rgba",
        "captureTask": ":verification:captureDialogueStages",
    },
    "battle": {
        "captures": Path(".verification-work/raw-framebuffer-common-space/dialogue-components"),
        "source": "source-{stage}.rgba",
        "game": Path("verification/build/verification/battle-dialogue-stages"),
        "gameFile": "game-{stage}.rgba",
        "captureTask": ":verification:captureBattleDialogueStages",
    },
}


def load(path: Path) -> Image.Image:
    """좌하단 원점 RGBA 원본을 좌상단 원점 이미지로 읽는다."""
    data = path.read_bytes()
    expected = WIDTH * HEIGHT * 4
    if len(data) != expected:
        raise SystemExit(f"{path}: {len(data)} bytes, expected {expected} (2560x1376 RGBA)")
    return Image.frombytes("RGBA", (WIDTH, HEIGHT), data).transpose(Image.FLIP_TOP_BOTTOM)


def to_design(x0: int, y0: int, x1: int, y1: int) -> dict[str, float]:
    """캡처 픽셀 bbox(좌상단 원점)를 설계 좌표(좌하단 원점)로 환산한다."""
    unit = CAPTURE_SCALE * DESIGN_SCALE
    return {
        "x": round(x0 / unit, 3),
        "y": round((HEIGHT - y1) / unit, 3),
        "w": round((x1 - x0) / unit, 3),
        "h": round((y1 - y0) / unit, 3),
    }


def ink_bbox(image: Image.Image) -> tuple[int, int, int, int] | None:
    """임계값 이상 픽셀만 남긴 bbox를 돌려준다."""
    mask = image.convert("L").point(lambda v: 255 if v > INK_THRESHOLD else 0)
    return mask.getbbox()


def ink_centroid(image: Image.Image, box: tuple[int, int, int, int] | None) -> float | None:
    """구성요소 잉크의 강도 가중 수평 무게중심을 설계 단위로 돌려준다.

    첫 글자의 좌측 베어링만 보는 bbox와 달리 문자열 전체에 대해 평균을 내므로
    글꼴별 자간 차이에 덜 민감하다.
    """
    if box is None:
        return None
    pixels = image.convert("L").load()
    x0, y0, x1, y1 = box
    total = 0.0
    weighted = 0.0
    for x in range(x0, x1):
        column = sum(pixels[x, y] for y in range(y0, y1))
        total += column
        weighted += column * x
    if not total:
        return None
    return round(weighted / total / (CAPTURE_SCALE * DESIGN_SCALE), 3)


def component(current: Image.Image, previous: Image.Image | None) -> Image.Image:
    """단계 차분으로 해당 구성요소가 더한 픽셀만 남긴다."""
    if previous is None:
        return current.convert("RGB")
    return ImageChops.difference(current, previous).convert("RGB")


def metrics(source: Image.Image, game: Image.Image) -> dict[str, float]:
    """두 프레임의 RGB 평균 절대 오차와 변경 픽셀 수를 계산한다."""
    diff = ImageChops.difference(source.convert("RGB"), game.convert("RGB"))
    total = 0
    changed = 0
    peak = 0
    for pixel in diff.getdata():
        high = max(pixel)
        total += pixel[0] + pixel[1] + pixel[2]
        if high:
            changed += 1
        peak = max(peak, high)
    return {
        "rgbMae": round(total / (WIDTH * HEIGHT * 3), 6),
        "changedPixels": changed,
        "maxChannelAbs": peak,
    }


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--target", choices=sorted(TARGETS), default="street", help="검증할 대화창 (street: 거리/회관, battle: 전투)")
    parser.add_argument("--captures", type=Path, help="원본 단계별 캡처 디렉터리 (기본값은 --target 별 경로)")
    parser.add_argument("--game", type=Path, help="게임 단계별 캡처 디렉터리 (기본값은 --target 별 경로)")
    parser.add_argument("--report", type=Path, help="JSON 보고서 출력 경로")
    parser.add_argument("--tolerance", type=float, default=DEFAULT_TOLERANCE, help="구성요소 위치 허용 오차(설계 단위)")
    args = parser.parse_args()
    target = TARGETS[args.target]
    captures = args.captures or target["captures"]
    game_dir = args.game or target["game"]

    source_paths = {stage: captures / target["source"].format(stage=stage) for stage in STAGES}
    game_paths = {stage: game_dir / target["gameFile"].format(stage=stage) for stage in STAGES}
    missing = [path for path in (*source_paths.values(), *game_paths.values()) if not path.exists()]
    if missing:
        print("캡처 파일이 없다:", file=sys.stderr)
        for path in missing:
            print(f"  {path}", file=sys.stderr)
        print(f"\n게임 캡처는 ./gradlew {target['captureTask']} 로 생성한다.", file=sys.stderr)
        return 2

    source = {stage: load(source_paths[stage]) for stage in STAGES}
    game = {stage: load(game_paths[stage]) for stage in STAGES}

    rows: list[dict[str, object]] = []
    previous_source: Image.Image | None = None
    previous_game: Image.Image | None = None
    for stage in STAGES:
        source_component = component(source[stage], previous_source)
        game_component = component(game[stage], previous_game)
        source_box = ink_bbox(source_component)
        game_box = ink_bbox(game_component)
        row: dict[str, object] = {"component": stage}
        if source_box and game_box:
            source_design = to_design(*source_box)
            game_design = to_design(*game_box)
            delta = {key: round(game_design[key] - source_design[key], 3) for key in ("x", "y", "w", "h")}
            source_centroid = ink_centroid(source_component, source_box)
            game_centroid = ink_centroid(game_component, game_box)
            row.update(
                source=source_design,
                game=game_design,
                delta=delta,
                relative=None,
                sourceCentroidX=source_centroid,
                gameCentroidX=game_centroid,
                centroidDeltaX=(
                    round(game_centroid - source_centroid, 3)
                    if source_centroid is not None and game_centroid is not None
                    else None
                ),
                withinTolerance=all(abs(delta[key]) <= args.tolerance for key in ("x", "y")),
            )
        else:
            row.update(source=source_box, game=game_box, withinTolerance=False)
        rows.append(row)
        previous_source, previous_game = source[stage], game[stage]

    # 대화창은 화자를 따라 통째로 움직인다. 창 자체의 기하는 패널 기준 상대 위치로 판정하고,
    # 창을 어디에 놓았는지는 별도 항목으로 보고한다.
    panel = next((row for row in rows if row["component"] == "panel"), None)
    panel_ok = isinstance(panel, dict) and isinstance(panel.get("source"), dict)
    if panel_ok:
        for row in rows:
            if not isinstance(row.get("source"), dict):
                continue
            row["relative"] = {
                key: round(
                    (row["game"][key] - panel["game"][key]) - (row["source"][key] - panel["source"][key]),
                    3,
                )
                for key in ("x", "y")
            }

    frame = metrics(source["text"], game["text"])

    print(f"[{args.target}] 대화창 내부 기하 (패널 왼쪽 아래 기준 상대 위치, 설계 단위)")
    print(f"{'구성요소':<10}{'원본 dx':>10}{'게임 dx':>10}{'Δ':>8}{'원본 dy':>10}{'게임 dy':>10}{'Δ':>8}   판정")
    print("-" * 78)
    failures = []
    for row in rows:
        if not isinstance(row.get("source"), dict):
            print(f"{row['component']:<10}{'차분 결과 없음':>60}")
            failures.append(row["component"])
            continue
        rel = row.get("relative")
        if rel is None:
            continue
        source_dx = round(row["source"]["x"] - panel["source"]["x"], 3)
        source_dy = round(row["source"]["y"] - panel["source"]["y"], 3)
        game_dx = round(row["game"]["x"] - panel["game"]["x"], 3)
        game_dy = round(row["game"]["y"] - panel["game"]["y"], 3)
        within = abs(rel["x"]) <= args.tolerance and abs(rel["y"]) <= args.tolerance
        row["withinTolerance"] = within
        if not within:
            failures.append(row["component"])
        print(
            f"{row['component']:<10}{source_dx:>10.3f}{game_dx:>10.3f}{rel['x']:>8.3f}"
            f"{source_dy:>10.3f}{game_dy:>10.3f}{rel['y']:>8.3f}   {'OK' if within else '불일치'}"
        )

    if panel_ok:
        print()
        print("대화창 전체 위치 (원본 캡처 당시의 화자·카메라 상태에 따라 달라진다)")
        print(f"  원본 패널 왼쪽 아래: ({panel['source']['x']:.3f}, {panel['source']['y']:.3f})")
        print(f"  게임 패널 왼쪽 아래: ({panel['game']['x']:.3f}, {panel['game']['y']:.3f})")
        print(f"  Δ: ({panel['delta']['x']:+.3f}, {panel['delta']['y']:+.3f})")

    print()
    print("잉크 무게중심과 폭 (설계 단위)")
    print(f"{'구성요소':<10}{'원본 중심':>12}{'게임 중심':>12}{'Δ중심':>9}{'원본 폭':>10}{'게임 폭':>10}{'Δ폭':>8}")
    print("-" * 71)
    for row in rows:
        if not isinstance(row.get("source"), dict):
            continue
        sc, gc = row.get("sourceCentroidX"), row.get("gameCentroidX")
        if sc is None or gc is None:
            continue
        print(
            f"{row['component']:<10}{sc:>12.3f}{gc:>12.3f}{row['centroidDeltaX']:>9.3f}"
            f"{row['source']['w']:>10.3f}{row['game']['w']:>10.3f}{row['delta']['w']:>8.3f}"
        )

    print()
    print("대화창 전체 프레임(text 단계, 검은 배경 위 대화창만) 픽셀 비교")
    print(f"  RGB MAE          : {frame['rgbMae']}")
    print(f"  변경 픽셀        : {frame['changedPixels']:,} / {WIDTH * HEIGHT:,}")
    print(f"  최대 채널 차이   : {frame['maxChannelAbs']}")
    print()
    print(f"위치 허용 오차: ±{args.tolerance} 설계 단위 (캡처 1픽셀 = 0.581 설계 단위)")
    print("판정은 패널 기준 상대 위치로 한다. 대화창은 화자를 따라 통째로 움직이므로")
    print("창의 절대 위치는 캡처 당시 화자·카메라 상태에 따라 달라진다.")
    print("글꼴 래스터라이저가 달라 글자 획 자체의 픽셀 일치는 기대하지 않는다.")
    print("패널과 초상화는 같은 텍스처를 쓰므로 Δ 0.000 을 요구한다.")

    report = {
        "contract": "dialogue-window-component-parity",
        "target": args.target,
        "captureDimensions": [WIDTH, HEIGHT],
        "designSpace": [round(WIDTH / (CAPTURE_SCALE * DESIGN_SCALE), 3), round(HEIGHT / (CAPTURE_SCALE * DESIGN_SCALE), 3)],
        "inkThreshold": INK_THRESHOLD,
        "toleranceDesignUnits": args.tolerance,
        "components": rows,
        "panelOffset": panel["delta"] if panel_ok else None,
        "frame": frame,
        "pass": not failures,
    }
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
        print(f"\n보고서: {args.report}")

    if failures:
        print(f"\n실패한 구성요소: {', '.join(failures)}")
        return 1
    print("\n모든 구성요소가 허용 오차 안에 있다.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
