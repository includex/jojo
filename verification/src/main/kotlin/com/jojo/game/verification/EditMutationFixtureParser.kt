package com.jojo.game.verification

data class EditMutationCase(
    val id: String,
    val owner: String,
    val flag: Int,
    val events: List<String>
)

/** Parses the small, deliberately JSON-shaped fixture used by the Edit trace. */
object EditMutationFixtureParser {
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

    private fun string(input: String, key: String): String =
        Regex("\\\"$key\\\"\\s*:\\s*\\\"([^\"]*)").find(input)!!.groupValues[1]

    private fun number(input: String, key: String): Int =
        Regex("\\\"$key\\\"\\s*:\\s*(\\d+)").find(input)!!.groupValues[1].toInt()

    private fun block(input: String, key: String): String {
        val start = input.indexOf('[', input.indexOf("\"$key\""))
        return balanced(input, start)
    }
}
