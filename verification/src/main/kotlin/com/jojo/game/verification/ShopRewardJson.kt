// Verification
package com.jojo.game.verification

/** ShopRewardJson: 원본 계약 추적에서 사용하는 작은 JSON 조각 작성기이다. */
object ShopRewardJson {
    /** escape: JSON 특수 문자를 이스케이프해 안전한 문자열을 만든다. */
    fun escape(value: String) = value.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"")
    /** string: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
    fun string(value: String) = "\"${escape(value)}\""
    /** array: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
    fun array(values: List<String>) = "[${values.joinToString(",")}]"
    /** objectValue: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
    fun objectValue(values: List<Pair<String, String>>) =
        "{${values.joinToString(",") { string(it.first) + ":" + it.second }}}"
}
