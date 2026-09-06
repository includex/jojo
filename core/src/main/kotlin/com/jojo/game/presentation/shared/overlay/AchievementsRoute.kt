// Game
package com.jojo.game.presentation.shared.overlay

/** SettingAchievementsRoute: SettingLayer 도구 버튼 12(태그 7)의 동작을 구현한다. 도구 패널은 비밀 제목 동작 뒤 표시되며, 이후 업적 화면을 열거나 빈 저장 알림을 전달한다. */

class SettingAchievementsRoute(
    /** `rewards` (Map<Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val rewards: Map<Int, StageAchievement>,
) {
    /**
     * `Effect`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    sealed interface Effect {
        /**
         * `OpenAchievements`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object OpenAchievements : Effect


        /**
         * `Toast`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Toast(val text: String) : Effect
    }

    /**
     * `toolsPanelVisible` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var toolsPanelVisible: Boolean = false
        private set


    /**
     * `exposeToolsPanel`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun exposeToolsPanel() {
        toolsPanelVisible = true
    }


    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


/**
 * `StageAchievement`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class StageAchievement(
    val round: Int,
    val level: Int,
    val gold: Int,
    val stars: Int,
)


/**
 * `AchievementRow`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

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
    /**
     * `rows` (List<AchievementRow>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rows: List<AchievementRow> = rewards.map { (battleId, reward) ->
        AchievementRow(
            title = "${reward.round} ${battleName(battleId)}",
            summary = "Lv:${reward.level} Gold:${reward.gold}",
            stars = (0..2).joinToString("  ") { bit ->
                if (reward.stars and (1 shl bit) != 0) "★" else "☆"
            },
        )
    }

    /**
     * `removed` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var removed: Boolean = false
        private set

    /** 첫 번째 버튼의 터치 종료만 닫기 동작으로 처리한다. */
    fun touch(button: Int, touchEnd: Boolean): Boolean {
        if (!removed && touchEnd && button == 0) removed = true
        return removed
    }
}
