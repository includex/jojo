package com.jojo.game.verification

data class ShopRewardItem(
    val id: Int,
    val name: String,
    val type: String,
    val price: Int,
    val sell: Int,
    val level: Int,
    val experience: Int
)

data class ShopRewardFixture(
    val name: String,
    val money: Int,
    val owned: Int,
    val unitId: Int,
    val rewardEnd: Boolean,
    val rewardMoney: Int,
    val rewardItems: Int,
    val items: List<ShopRewardItem>,
    val events: List<String>
)

/** Reads the fixture schema without introducing a dependency on a JSON library. */
object ShopRewardFixtureParser {
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

    private fun parseBodies(input: String): List<Pair<String, String>> {
        val starts = Regex("\\{\\s*\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").findAll(input).toList()
        return starts.mapIndexed { index, match ->
            match.groupValues[1] to input.substring(
                match.range.first,
                if (index + 1 < starts.size) starts[index + 1].range.first else input.length
            )
        }
    }

    private fun field(input: String, name: String): Int =
        Regex("\\\"$name\\\"\\s*:\\s*(\\d+)").find(input)!!.groupValues[1].toInt()

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

    private fun parseEvents(input: String): List<String> =
        Regex("\\\"events\\\"\\s*:\\s*\\[(.*?)]").find(input)?.groupValues?.get(1)
            ?.let { Regex("\\\"([^\\\"]+)\\\"").findAll(it).map { event -> event.groupValues[1] }.toList() }
            ?: emptyList()
}
