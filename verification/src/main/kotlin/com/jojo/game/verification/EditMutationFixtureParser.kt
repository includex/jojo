// Verification
package com.jojo.game.verification

/** EditMutationCase: edit mutation case 관련 검증 상태와 동작을 제공하는 타입이다. */
data class EditMutationCase(
    /** id: 검증 흐름에서 사용하는 값을 담는다. */
    val id: String,
    /** owner: 검증 흐름에서 사용하는 값을 담는다. */
    val owner: String,
    /** flag: 검증 흐름에서 사용하는 값을 담는다. */
    val flag: Int,
    /** events: 검증 이벤트 또는 추적 결과를 담는다. */
    val events: List<String>
)

/** EditMutationFixtureParser: Edit 추적에 사용하는 단순 JSON 형태 픽스처를 해석한다. */
object EditMutationFixtureParser {
    /** parse: 외부 입력을 검증용 값으로 변환한다. */
    fun parse(input: String): List<EditMutationCase> = objects(block(input, "cases")).map { value ->
        EditMutationCase(
            string(value, "id"),
            string(value, "owner"),
            number(value, "flag"),
            Regex("\\\"([^\"]*)\\\"")
                .findAll(block(value, "events"))
                .map { match -> match.groupValues[1] }
                .toList()
        )
    }

    /** balanced: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun balanced(input: String, start: Int): String {
        val open = input[start]
        val close = if (open == '{') '}' else ']'
        var depth = 0
        var quoted = false
        var escaped = false
        for (index in start until input.length) {
            val character = input[index]
            if (quoted) {
                if (escaped) escaped = false
                else if (character == '\\') escaped = true
                else if (character == '"') quoted = false
            } else if (character == '"') quoted = true
            else if (character == open) depth++
            else if (character == close && --depth == 0) return input.substring(start, index + 1)
        }
        error("json")
    }

    /** objects: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun objects(input: String): List<String> {
        val result = mutableListOf<String>()
        var index = 0
        while (index < input.length) {
            if (input[index] == '{') {
                val value = balanced(input, index)
                result += value
                index += value.length
            } else index++
        }
        return result
    }

    /** string: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun string(input: String, key: String): String =
        Regex("\\\"$key\\\"\\s*:\\s*\\\"([^\"]*)").find(input)!!.groupValues[1]

    /** number: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun number(input: String, key: String): Int =
        Regex("\\\"$key\\\"\\s*:\\s*(\\d+)").find(input)!!.groupValues[1].toInt()

    /** block: JSON 입력에서 지정한 필드 블록을 추출한다. */
    private fun block(input: String, key: String): String {
        val start = input.indexOf('[', input.indexOf("\"$key\""))
        return balanced(input, start)
    }
}
