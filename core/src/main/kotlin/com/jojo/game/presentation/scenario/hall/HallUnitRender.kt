// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.application.scenario.HallMoveTimeline

/** HallUnitSpriteFrame: 거점 유닛을 그릴 때 사용할 스프라이트 원본 영역과 방향 정보를 나타낸다. */

data class HallUnitSpriteFrame(
    val textureAssetId: Int,
    val row: Int,
    val flipX: Boolean,
)


object HallUnitRender {

    fun frame(mapAvatar: Int, action: Int, direction: Int, elapsedSeconds: Float): HallUnitSpriteFrame {
        val normalizedDirection = direction.takeIf { it in 0..3 } ?: 0
        val row = when (action) {
            0 -> 0
            20 -> 1 + ((elapsedSeconds / .125f).toInt() and 1)
            21 -> 1 + if (elapsedSeconds >= .125f) 1 else 0
            in 1..17 -> action + 2
            18 -> 1
            19 -> 2
            else -> 0
        }
        return HallUnitSpriteFrame(
            textureAssetId = 1 + mapAvatar * 2 + if (normalizedDirection == 0 || normalizedDirection == 3) 1 else 0,
            row = row,
            flipX = normalizedDirection == 1 || normalizedDirection == 3,
        )
    }

    /** walkingRenderEventLog: 거점 유닛 이동을 검증하기 위해 프레임별 렌더링 이벤트를 기록한다. */
    fun walkingRenderEventLog(): String {

        data class Path(val direction: Int, val x: Int, val y: Int, val dx: Int, val dy: Int)

        val paths = listOf(Path(1, 45, 48, 1, 0), Path(3, 51, 48, -1, 0), Path(2, 45, 48, 0, 1), Path(0, 45, 54, 0, -1))
        var sequence = 0
        return buildString {
            paths.forEach { path ->
                listOf(0f, .04f, .08f, .12f, .16f, .20f, .24f).forEachIndexed { tick, time ->
                    val gridX = path.x + path.dx * tick
                    val gridY = path.y + path.dy * tick
                    val selected = frame(0, 20, path.direction, time)
                    val x = (gridX - gridY + 42) * 16f - 41.28f
                    val y = 1073.28f - (gridX + gridY) * 6.88f - 55.04f
                    append(
                        "{\"sequence\":${sequence++},\"frame\":$tick,\"timestamp\":${
                            "%.2f".format(
                                java.util.Locale.ROOT,
                                time
                            )
                        },"
                    )
                    append("\"phase\":\"hall-street-walk\",\"layer\":\"HallLayer\",\"nodePath\":\"Canvas/Layer/map/pmapobj/walk-${path.direction}/anime\",")
                    append(
                        "\"drawType\":\"sprite\",\"x\":${
                            "%.3f".format(
                                java.util.Locale.ROOT,
                                x
                            )
                        },\"y\":${"%.3f".format(java.util.Locale.ROOT, y)},\"w\":82.560,\"h\":110.080,"
                    )
                    val textureIndex = if (path.direction == 0 || path.direction == 3) 1 else 0
                    append("\"assetFrameId\":\"Game/Pmapobj2/${selected.textureAssetId}#t=$textureIndex;row=${selected.row};flipX=${selected.flipX}\",")
                    append("\"opacity\":1,\"blend\":[770,771],\"visible\":true,\"text\":\"grid=$gridX,$gridY;dir=${path.direction};action=20\"}\n")
                }
            }
        }
    }

    /** walkingMotionRenderEventLog: 거점 유닛의 보간 이동 좌표를 검증용 이벤트 목록으로 기록한다. */
    fun walkingMotionRenderEventLog(): String {
        val path = listOf(45 to 48, 46 to 48, 47 to 48, 47 to 49, 47 to 50)
        return buildString {
            (0..16).forEach { frame ->
                val time = frame * .01f
                val sample = HallMoveTimeline.sample(path, time)
                val clipTime = if (sample.direction == 2) time - .08f else time
                val selected = frame(0, 20, sample.direction, clipTime.coerceAtLeast(0f))
                val x = (sample.x - sample.y + 42f) * 16f - 41.28f
                val y = 1073.28f - (sample.x + sample.y) * 6.88f - 55.04f
                val localX = ((x + 41.28f) - 640f) / 1.72f
                val localY = ((y + 55.04f) - 344f) / 1.72f


                fun number(value: Float) =
                    "%.4f".format(java.util.Locale.ROOT, value).trimEnd('0').trimEnd('.').ifEmpty { "0" }
                append(
                    "{\"sequence\":$frame,\"frame\":$frame,\"timestamp\":${
                        "%.2f".format(
                            java.util.Locale.ROOT,
                            time
                        )
                    },"
                )
                append("\"phase\":\"hall-street-walk\",\"layer\":\"HallLayer\",\"nodePath\":\"Canvas/Layer/map/pmapobj/walk-${sample.direction}/anime\",\"drawType\":\"sprite\",")
                append(
                    "\"x\":${"%.3f".format(java.util.Locale.ROOT, x)},\"y\":${
                        "%.3f".format(
                            java.util.Locale.ROOT,
                            y
                        )
                    },\"w\":82.56,\"h\":110.08,"
                )
                append("\"assetFrameId\":\"Game/Pmapobj2/${selected.textureAssetId}#t=0;row=${selected.row};flipX=${selected.flipX}\",\"opacity\":1,\"blend\":[770,771],\"visible\":true,")
                append("\"text\":\"local=${number(localX)},${number(localY)};z=${number(sample.zIndex)};dir=${sample.direction};action=20\"}\n")
            }
        }
    }
}
