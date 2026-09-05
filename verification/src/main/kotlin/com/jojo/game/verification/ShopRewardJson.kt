package com.jojo.game.verification

/** Small JSON fragment writer used by the source-contract trace. */
object ShopRewardJson {
    fun escape(value: String) = value.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"")
    fun string(value: String) = "\"${escape(value)}\""
    fun array(values: List<String>) = "[${values.joinToString(",")}]"
    fun objectValue(values: List<Pair<String, String>>) =
        "{${values.joinToString(",") { string(it.first) + ":" + it.second }}}"
}
