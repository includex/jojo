package com.jojo.game

/**
 * Production behaviour of SettingLayer's authored tools button 12 (tag 7).
 *
 * The released prefab keeps the tools panel hidden until its secret-title gesture
 * is used.  Once exposed, the same button either opens Global142 or emits the
 * recovered empty-save notice.
 */
class SettingAchievementsRoute(
    private val rewards: Map<Int, StageAchievement>,
) {
    sealed interface Effect {
        data object OpenAchievements : Effect
        data class Toast(val text: String) : Effect
    }

    var toolsPanelVisible: Boolean = false
        private set

    fun exposeToolsPanel() {
        toolsPanelVisible = true
    }

    fun touch(tag: Int, touchEnd: Boolean): List<Effect> {
        if (!toolsPanelVisible || !touchEnd || tag != 7) return emptyList()
        toolsPanelVisible = false
        return if (rewards.isEmpty()) {
            listOf(Effect.Toast("저장된 게임에서 다시 확인해 주세요./현재 업적이 없습니다."))
        } else {
            listOf(Effect.OpenAchievements)
        }
    }
}

data class StageAchievement(
    val round: Int,
    val level: Int,
    val gold: Int,
    val stars: Int,
)

data class AchievementRow(
    val title: String,
    val summary: String,
    val stars: String,
)

/** Renderer-independent state of Global142, preserving source insertion order. */
class AchievementsFlow(
    rewards: Map<Int, StageAchievement>,
    battleName: (Int) -> String,
) {
    val rows: List<AchievementRow> = rewards.map { (battleId, reward) ->
        AchievementRow(
            title = "${reward.round} ${battleName(battleId)}",
            summary = "Lv:${reward.level} Gold:${reward.gold}",
            stars = (0..2).joinToString("  ") { bit ->
                if (reward.stars and (1 shl bit) != 0) "★" else "☆"
            },
        )
    }

    var removed: Boolean = false
        private set

    /** Only Logo_12-1/button0 on touch-end closes; button1 is deliberately inert. */
    fun touch(button: Int, touchEnd: Boolean): Boolean {
        if (!removed && touchEnd && button == 0) removed = true
        return removed
    }
}
