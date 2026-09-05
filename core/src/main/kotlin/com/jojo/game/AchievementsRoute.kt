package com.jojo.game
import com.jojo.game.presentation.shared.overlay.*

/**
 * Production behaviour of SettingLayer's authored tools button 12 (tag 7).
 *
 * The released prefab keeps the tools panel hidden until its secret-title gesture
 * is used.  Once exposed, the same button either opens Global142 or emits the
 * recovered empty-save notice.
 */
/**
 * class  `SettingAchievementsRoute`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SettingAchievementsRoute(
    private val rewards: Map<Int, StageAchievement>,
) {
    sealed interface Effect {
        data object OpenAchievements : Effect

        /**
         * data class  `Toast`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Toast(val text: String) : Effect
    }

    var toolsPanelVisible: Boolean = false
        private set

    /**
     * 공개 메서드 `exposeToolsPanel`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun exposeToolsPanel() {
        toolsPanelVisible = true
    }

    /**
     * 공개 메서드 `touch`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Effect>`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
 * data class  `StageAchievement`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class StageAchievement(
    val round: Int,
    val level: Int,
    val gold: Int,
    val stars: Int,
)

/**
 * data class  `AchievementRow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
