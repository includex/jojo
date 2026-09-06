// Verification
package com.jojo.game.verification

import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.domain.scenario.ScenarioRandomSequence
import java.nio.file.Files
import java.nio.file.Path

/** ModelRandomTraceHarness: 외부 비교를 위해 결정적인 시나리오 난수 계약을 출력한다. */
object ModelRandomTraceHarness {
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val cases = JsonReader().parse(Files.readString(Path.of(args[0]))).get("cases")
        val result = buildString {
            append('{')
            var firstCase = true
            var entry = cases.child
            while (entry != null) {
                if (!firstCase) append(',')
                firstCase = false
                var seed = entry.getDouble("seed")
                val values = mutableListOf<Int>()
                repeat(entry.getInt("draws")) {
                    val next = ScenarioRandomSequence.nextFromSeed(seed)
                    seed = next.first
                    values += next.second
                }
                append('"').append(entry.getString("id")).append("\":{\"values\":[")
                append(values.joinToString(","))
                append("]}")
                entry = entry.next
            }
            append('}')
        }
        val output = Path.of(args[1])
        Files.createDirectories(output.parent)
        Files.writeString(output, result)
    }
}
