// Game
package com.jojo.game.presentation.shared.overlay

/** SettingAchievementsRoute: SettingLayer 도구 버튼 12(태그 7)의 동작을 구현한다. 도구 패널은 비밀 제목 동작 뒤 표시되며, 이후 업적 화면을 열거나 빈 저장 알림을 전달한다. */

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

/** AchievementsFlow: 원본 삽입 순서를 유지하는 업적 화면 상태이다. */
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

    /** 첫 번째 버튼의 터치 종료만 닫기 동작으로 처리한다. */
    fun touch(button: Int, touchEnd: Boolean): Boolean {
        if (!removed && touchEnd && button == 0) removed = true
        return removed
    }
}
