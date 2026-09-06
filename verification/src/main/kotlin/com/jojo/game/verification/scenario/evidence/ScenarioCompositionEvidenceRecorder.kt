// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import java.util.*

/** ScenarioCompositionEvidenceRecorder: 화면 의존성 없이 원본 비교용 구성 계약을 직렬화한다. */
internal class ScenarioCompositionEvidenceRecorder {
    fun record(view: ScenarioEvidenceView): String {
        val units = view.units.joinToString(",") { unit ->
            val x = mapX(unit.scriptX, unit.scriptY)
            val y = mapY(unit.scriptX, unit.scriptY)
            val asset = 1 + unit.avatarId * 2 + if (unit.direction == 0 || unit.direction == 3) 1 else 0
            "{\"id\":${unit.id},\"script\":[${f(unit.scriptX)},${f(unit.scriptY)}]," +
                    "\"direction\":${unit.direction},\"action\":${unit.action},\"asset\":$asset," +
                    "\"rect\":${rect(x - 41.28f, y - 55.04f, 82.56f, 110.08f)}}"
        }
        val heads = view.heads.joinToString(",") { head ->
            val centerX = head.scriptX * 2f + 55.04f
            val centerY = 688f - head.scriptY * 1.72f - 68.8f
            "{\"id\":${head.characterId},\"script\":[${f(head.scriptX)},${f(head.scriptY)}]," +
                    "\"opacity\":${f(head.opacity)},\"rect\":${
                        rect(
                            centerX - 55.04f,
                            centerY - 68.8f,
                            110.08f,
                            137.6f
                        )
                    }}"
        }
        val dialogue = view.dialogue?.let { value ->
            val left = value.side == 0
            val dialogueY = if (value.atTop) 373.24f else 0f
            val panelX = if (left) 274.54054f else 316.40878f
            val faceX = if (left) 84.8199f else 1030.2742f
            val speakerX = if (left) 323.44676f else 365.315f
            val textX = if (left) 328.93882f else 370.80706f
            "{\"side\":${value.side},\"top\":${value.atTop}," +
                    "\"speakerId\":${value.speakerId ?: -1}," +
                    "\"panelRect\":${rect(panelX, 55.47f + dialogueY, 686.28f, 164.26f)}," +
                    "\"faceRect\":${rect(faceX, 53.32f + dialogueY, 165.12f, 206.4f)}," +
                    "\"speakerBaseline\":[${f(speakerX)},${f(202.5f + dialogueY)}]," +
                    "\"textBaseline\":[${f(textX)},${f(163.5f + dialogueY)}],\"text\":\"${escape(value.visibleText)}\"}"
        } ?: "null"
        val modal = view.modal?.let { value ->
            "{\"kind\":\"${value.kind}\",\"text\":\"${escape(value.text)}\",\"screenRect\":${
                rect(
                    0f,
                    0f,
                    1280f,
                    688f
                )
            },\"contentCenter\":[640,344]}"
        } ?: "null"
        val hallMenu = view.hallMenu?.let { value ->
            val buttonCenters =
                floatArrayOf(55.107f, 143.365f, 231.846f, 320.74f, 423.317f, 511.575f, 600.056f, 690.441f, 789.44f)
            val buttons =
                buttonCenters.joinToString(",") { sourceX -> rect(sourceX * .86f - 37.84f, 44.30f, 75.68f, 75.68f) }
            "{\"panelRect\":${rect(0f, 0f, 1280f, 125.56f)},\"buttons\":[$buttons]," +
                    "\"eventRect\":${rect(99.72f, 4.25f, 261.44f, 37.84f)}," +
                    "\"stageRect\":${rect(366.95f, 4.23f, 278.64f, 37.84f)}," +
                    "\"barRect\":${rect(717.4f, 16.70f, 258f, 12.9f)}," +
                    "\"valueWidth\":${f(258f * value.displayedAmbition.coerceIn(0f, 100f) / 100f)}," +
                    "\"from\":${value.ambitionFrom},\"to\":${value.ambitionTo}}"
        } ?: "null"
        val hallCommand = if (view.hallCommandVisible) {
            "{\"menuRect\":${rect(31f, 318.2f, 51.6f, 51.6f)}," +
                    "\"battleRect\":${rect(895.58f, 1.72f, 82.56f, 82.56f)}," +
                    "\"equipRect\":${rect(978.14f, 1.72f, 82.56f, 82.56f)}," +
                    "\"buyRect\":${rect(1060.70f, 1.72f, 82.56f, 82.56f)}," +
                    "\"sellRect\":${rect(1143.26f, 1.72f, 82.56f, 82.56f)}}"
        } else "null"
        val hallManagement = view.hallManagement?.let { kind ->
            val geometry = when (kind) {
                ScenarioEvidenceHallManagement.EQUIP -> rect(118.84f, 28.81f, 1042.32f, 630.38f)
                ScenarioEvidenceHallManagement.BUY -> rect(168.72f, 28.81f, 943.42f, 630.38f)
                ScenarioEvidenceHallManagement.SELL -> rect(267.84f, 65.36f, 744.76f, 557.28f)
            }
            "{\"kind\":\"${kind.name.lowercase()}\",\"rootRect\":$geometry}"
        } ?: "null"
        val hallInfo = view.hallInfo?.let { info ->
            val geometry = when (info.kind) {
                "forces" -> rect(142.49f, 68.37f, 995.02f, 551.26f)
                "property" -> rect(212.42f, 40.42f, 854.84f, 607.16f)
                "terrain" -> rect(235.84f, 86f, 878.15f, 516f)
                "treasure" -> rect(222.9f, 72.24f, 834.2f, 543.52f)
                "helper" -> rect(127f, 21.07f, 1025.98f, 645.86f)
                else -> error("Unknown Hall evidence kind: ${info.kind}")
            }
            val rows = info.contentRects.joinToString(",") { row -> rect(row.x, row.y, row.width, row.height) }
            "{\"kind\":\"${info.kind}\",\"rootRect\":$geometry,\"contentRects\":[$rows]}"
        } ?: "null"
        return "{\"state\":\"${view.moduleName}/${view.playbackState}\",\"viewport\":[1280,688]," +
                "\"backgroundId\":${view.backgroundId},\"units\":[$units],\"heads\":[$heads],\"dialogue\":$dialogue,\"modal\":$modal,\"hallCommand\":$hallCommand,\"hallMenu\":$hallMenu," +
                "\"hallManagement\":$hallManagement,\"hallInfo\":$hallInfo}"
    }

    private fun f(value: Float): String = "%.3f".format(Locale.US, value)
    private fun rect(x: Float, y: Float, width: Float, height: Float): String =
        "[${f(x)},${f(y)},${f(width)},${f(height)}]"

    private fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    private fun mapX(x: Float, y: Float): Float = (x - y + 42f) * 16f
    private fun mapY(x: Float, y: Float): Float = 1073.28f - (x + y) * 6.88f
}
