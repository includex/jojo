// Presentation
package com.jojo.game.presentation.shared.overlay

import com.jojo.game.*
import com.jojo.game.application.campaign.DailySignInFlow
import com.jojo.game.application.campaign.RaffleFlow
import com.jojo.game.domain.battle.*


/** SettingLayer: 음향·메시지·배경·속도 설정을 편집하고 조건에 맞는 부가 기능 화면을 여는 상태다. */
class SettingLayer(
    private val store: Store,
    private val sound: Sound = Sound.NONE,
    private val featureEnvironment: () -> FeatureEnvironment = { FeatureEnvironment() },
    private val applyGameSpeed: () -> Unit = {},
) {

    interface Store {
        fun getInt(key: String, default: Int = 0): Int
        fun putInt(key: String, value: Int)
    }


    interface Sound {
        fun music(on: Boolean)
        fun effect(on: Boolean)

        companion object {
            val NONE = object : Sound {
                override fun music(on: Boolean) = Unit
                override fun effect(on: Boolean) = Unit
            }
        }
    }


    data class View(
        val flags: Int,
        val msgSpeed: Int,
        val notifyLevel: Int,
        val background: Int,
        val speed: Float,
        val attached: Boolean
    )


    data class FeatureEnvironment(
        val sceneName: String = "Login",
        val supportAdCode: Int = 0,
        val achievements: Map<Int, StageAchievement> = emptyMap(),
        val battleName: (Int) -> String = { "B$it" },
        val signInCount: Int = 0,
        val signInDays: MutableList<Int> = mutableListOf(),
        val nowSeconds: Int = 0,
        val raffleVideoCount: Int = 0,
        val luckyCoins: Int = 0,
    )

    sealed interface FeatureResult {

        data class Opened(val name: String) : FeatureResult


        data class Toast(val text: String) : FeatureResult
        data object Gated : FeatureResult
        data object Ignored : FeatureResult
    }

    var activeFeature: Any? = null
        private set
    private var flags = 0
    private var speed = 0f
    private var speedChanged = false
    private var attached = false


    fun onCreate(): View {
        flags = store.getInt(GAME_SETTING, BG_SOUND or EFFECT_SOUND or MINI_MAP); speed =
            store.getInt(GAME_SPEED, 0) / 100f; attached = true; return view()
    }

    /** check: 설정 비트를 켜거나 끄고 배경음·효과음 선택은 즉시 음향 서비스에 반영한다. */
    fun check(bit: Int, checked: Boolean) {
        require(bit in 0..6); flags = if (checked) flags or (1 shl bit) else flags and (1 shl bit).inv(); store.putInt(
            GAME_SETTING,
            flags
        ); if (bit == 0) sound.music(checked); if (bit == 1) sound.effect(checked)
    }

    /** check2: 메시지 속도 또는 알림 수준 선택값을 해당 환경설정 키에 저장한다. */
    fun check2(panel: Int, selection: Int) {
        require(panel in 0..2 && selection >= 0); if (panel != 1) store.putInt(
            if (panel == 0) MSG_SPEED else NOTIFY_LV,
            selection
        )
    }


    fun selectBackground(index: Int) {
        require(index in 0..3); store.putInt(BG_INDEX, index)
    }


    fun onSlider(progress: Float) {
        speed = progress.coerceIn(0f, 1f); speedChanged = true
    }

    /** dismiss: 터치 종료 입력일 때만 설정 화면을 닫고 처리 여부를 반환한다. */
    fun dismiss(eventType: Int): Boolean {
        if (eventType != TOUCH_END || !attached) return false; attached = false; return true
    }

    /** onDestroy: 슬라이더로 바뀐 게임 속도를 저장하고 실행 중인 속도 설정을 갱신한다. */
    fun onDestroy() {
        if (speedChanged) {
            store.putInt(GAME_SPEED, (speed * 100).toInt()); applyGameSpeed()
        }
    }

    /** featureButton: 해금 조건과 현재 장면을 검사해 업적·추첨·출석부 화면을 열거나 안내 결과를 만든다. */
    fun featureButton(tag: Int, eventType: Int): FeatureResult {
        if (!attached || eventType != TOUCH_END || tag !in 7..9) return FeatureResult.Ignored
        val env = featureEnvironment()
        return when (tag) {
            7 -> if (env.achievements.isEmpty()) {
                FeatureResult.Toast("저장된 게임에서 다시 확인해 주세요./현재 업적이 없습니다.")
            } else {
                activeFeature = AchievementsFlow(env.achievements, env.battleName)
                FeatureResult.Opened("AchievementsLayer")
            }

            8 -> when {
                env.supportAdCode < 8 -> FeatureResult.Gated
                env.sceneName !in setOf("Hall", "Battle") ->
                    FeatureResult.Toast("전투 준비/전투 중일 때만 뽑기가 가능합니다!")

                else -> {
                    activeFeature = RaffleFlow(env.raffleVideoCount, env.luckyCoins)
                    FeatureResult.Opened("RaffleLayer")
                }
            }

            else -> if (env.supportAdCode < 8) FeatureResult.Gated else {
                activeFeature = DailySignInFlow(env.signInCount, env.signInDays, env.nowSeconds)
                FeatureResult.Opened("SignInLayer")
            }
        }
    }

    /** close: 화면 닫기 입력을 처리한 뒤 필요한 속도 저장 정리까지 함께 실행한다. */
    fun close(eventType: Int): Boolean {
        val removed = dismiss(eventType); if (removed) onDestroy(); return removed
    }


    fun view() =
        View(flags, store.getInt(MSG_SPEED, 1), store.getInt(NOTIFY_LV, 1), store.getInt(BG_INDEX, 0), speed, attached)

    companion object {
        const val TOUCH_END = 2
        const val GAME_SETTING = "GAME_SETTING"
        const val MSG_SPEED = "MSG_SPEED"
        const val GAME_SPEED = "GAME_SPEED2"
        const val NOTIFY_LV = "NOTIFY_LV"
        const val BG_INDEX = "BG_IDX"
        const val BG_SOUND = 1
        const val EFFECT_SOUND = 2
        const val MINI_MAP = 4
        const val AUTO_CLOSE = 8
        const val BOARD_BAR_TOP = 16
        const val R_IDX_INC = 32
        const val R_ASPECT_RATIO = 64
    }
}
