// Verification
package com.jojo.game.verification

/** ShopRewardItem: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
data class ShopRewardItem(
    /** id: 상점 항목 식별자를 담는다. */
    val id: Int,
    /** name: 검증 대상의 표시 이름을 담는다. */
    val name: String,
    /** type: 검증 대상의 종류를 담는다. */
    val type: String,
    /** price: 상점 항목 가격을 담는다. */
    val price: Int,
    /** sell: 판매 가능 여부를 담는다. */
    val sell: Int,
    /** level: 검증 대상 레벨을 담는다. */
    val level: Int,
    /** experience: 검증 대상 경험치를 담는다. */
    val experience: Int
)

/** ShopRewardFixture: 원본 동작을 재현하는 검증 픽스처 타입이다. */
data class ShopRewardFixture(
    /** name: 검증 대상의 표시 이름을 담는다. */
    val name: String,
    /** money: 보유 금액을 담는다. */
    val money: Int,
    /** owned: 보유 여부를 담는다. */
    val owned: Int,
    /** unitId: 무장 식별자를 담는다. */
    val unitId: Int,
    /** rewardEnd: 보상 종료 여부를 담는다. */
    val rewardEnd: Boolean,
    /** rewardMoney: 보상 금액을 담는다. */
    val rewardMoney: Int,
    /** rewardItems: 보상 아이템 목록을 담는다. */
    val rewardItems: Int,
    /** items: 검증 대상 목록을 담는다. */
    val items: List<ShopRewardItem>,
    /** events: 검증 이벤트 목록을 담는다. */
    val events: List<String>
)

/** ShopRewardFixtureParser: JSON 라이브러리 의존성 없이 픽스처 형식을 읽는다. */
object ShopRewardFixtureParser {
    /** parse: 외부 입력을 검증용 값으로 변환한다. */
    fun parse(input: String): List<ShopRewardFixture> = parseBodies(input).map { (name, body) ->
        ShopRewardFixture(
            name = name,
            money = field(body, "money"),
            owned = field(body, "owned"),
            unitId = field(body, "unitId"),
            rewardEnd = Regex("\\\"reward\\\"\\s*:\\s*\\{\\s*\\\"end\\\"\\s*:\\s*(true|false)")
                .find(body)!!.groupValues[1] == "true",
            rewardMoney = Regex("\\\"reward\\\"\\s*:\\s*\\{.*?\\\"money\\\"\\s*:\\s*(\\d+)")
                .find(body)!!.groupValues[1].toInt(),
            rewardItems = Regex(
                "\\\"reward\\\"\\s*:\\s*\\{.*?\\\"items\\\"\\s*:\\s*\\[([^]]*)]",
                RegexOption.DOT_MATCHES_ALL
            ).find(body)?.groupValues?.get(1)?.let { Regex("\\d+").findAll(it).count() } ?: 0,
            items = parseItems(body),
            events = parseEvents(body)
        )
    }

    /** parseBodies: 외부 문자열을 검증 모델로 해석한다. */
    private fun parseBodies(input: String): List<Pair<String, String>> {
        val starts = Regex("\\{\\s*\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").findAll(input).toList()
        return starts.mapIndexed { index, match ->
            match.groupValues[1] to input.substring(
                match.range.first,
                if (index + 1 < starts.size) starts[index + 1].range.first else input.length
            )
        }
    }

    /** field: 입력 데이터에서 지정한 블록을 추출한다. */
    private fun field(input: String, name: String): Int =
        Regex("\\\"$name\\\"\\s*:\\s*(\\d+)").find(input)!!.groupValues[1].toInt()

    /** parseItems: 외부 문자열을 검증 모델로 해석한다. */
    private fun parseItems(input: String): List<ShopRewardItem> = Regex(
        "\\{\\s*\\\"id\\\"\\s*:\\s*(\\d+).*?\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*?" +
            "\\\"type\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*?\\\"price\\\"\\s*:\\s*(\\d+).*?" +
            "\\\"sell\\\"\\s*:\\s*(\\d+).*?\\\"lv\\\"\\s*:\\s*(\\d+).*?" +
            "\\\"exp\\\"\\s*:\\s*(\\d+)"
    ).findAll(input).map { match ->
        ShopRewardItem(
            match.groupValues[1].toInt(), match.groupValues[2], match.groupValues[3],
            match.groupValues[4].toInt(), match.groupValues[5].toInt(),
            match.groupValues[6].toInt(), match.groupValues[7].toInt()
        )
    }.toList()

    /** parseEvents: 입력 데이터에서 검증 이벤트 목록을 추출한다. */
    private fun parseEvents(input: String): List<String> =
        Regex("\\\"events\\\"\\s*:\\s*\\[(.*?)]").find(input)?.groupValues?.get(1)
            ?.let { Regex("\\\"([^\\\"]+)\\\"").findAll(it).map { event -> event.groupValues[1] }.toList() }
            ?: emptyList()
}
