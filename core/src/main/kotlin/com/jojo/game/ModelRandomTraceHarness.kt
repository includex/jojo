package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import java.nio.file.Files
import java.nio.file.Path

/** Direct recovered Tool.random(0,100) contract trace for AST scenario RNG. */
object ModelRandomTraceHarness {
    @JvmStatic
            /**
             * 공개 메서드 `main`
             *
             * ### 파라미터
            - `args` (`Array<String>`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
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
                    val next = ScenarioInterpreter.toolRandomFromSeed(seed)
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
