// Presentation
package com.jojo.game.presentation.shared.overlay

import com.jojo.game.*
import com.jojo.game.application.campaign.DailySignInFlow
import com.jojo.game.application.campaign.RaffleFlow
import com.jojo.game.domain.battle.*


/** SettingLayer: 음향·메시지·배경·속도 설정을 편집하고 조건에 맞는 부가 기능 화면을 여는 상태다. */
class SettingLayer(
    /** `store` (Store): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val store: Store,
    /** `sound` (Sound): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val sound: Sound = Sound.NONE,
    /** `featureEnvironment` (() -> FeatureEnvironment): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val featureEnvironment: () -> FeatureEnvironment = { FeatureEnvironment() },
    /** `applyGameSpeed` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val applyGameSpeed: () -> Unit = {},
) {

    /**
     * `Store`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    interface Store {
        /**
         * `getInt`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun getInt(key: String, default: Int = 0): Int
        /**
         * `putInt`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun putInt(key: String, value: Int)
    }


    /**
     * `Sound`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    interface Sound {
        /**
         * `music`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun music(on: Boolean)
        /**
         * `effect`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun effect(on: Boolean)

        companion object {
            /**
             * `NONE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val NONE = object : Sound {
                /**
                 * `music`: 타입의 핵심 동작을 수행한다.
                 * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
                 */

                override fun music(on: Boolean) = Unit
                /**
                 * `effect`: 타입의 핵심 동작을 수행한다.
                 * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
                 */

                override fun effect(on: Boolean) = Unit
            }
        }
    }


    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(
        /**
         * `flags` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val flags: Int,
        /**
         * `msgSpeed` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val msgSpeed: Int,
        /**
         * `notifyLevel` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val notifyLevel: Int,
        /**
         * `background` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val background: Int,
        /**
         * `speed` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val speed: Float,
        /**
         * `attached` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attached: Boolean
    )


    /**
     * `FeatureEnvironment`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class FeatureEnvironment(
        /**
         * `sceneName` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sceneName: String = "Login",
        /**
         * `supportAdCode` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val supportAdCode: Int = 0,
        /**
         * `achievements` (Map<Int, StageAchievement>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val achievements: Map<Int, StageAchievement> = emptyMap(),
        /**
         * `battleName` ((Int) -> String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleName: (Int) -> String = { "B$it" },
        /**
         * `signInCount` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val signInCount: Int = 0,
        /**
         * `signInDays` (MutableList<Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val signInDays: MutableList<Int> = mutableListOf(),
        /**
         * `nowSeconds` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val nowSeconds: Int = 0,
        /**
         * `raffleVideoCount` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val raffleVideoCount: Int = 0,
        /**
         * `luckyCoins` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val luckyCoins: Int = 0,
    )

    /**
     * `FeatureResult`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    sealed interface FeatureResult {

        /**
         * `Opened`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Opened(val name: String) : FeatureResult


        /**
         * `Toast`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Toast(val text: String) : FeatureResult
        /**
         * `Gated`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Gated : FeatureResult
        /**
         * `Ignored`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Ignored : FeatureResult
    }

    /**
     * `activeFeature` (Any?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var activeFeature: Any? = null
        private set
    /**
     * `flags` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var flags = 0
    /**
     * `speed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var speed = 0f
    /**
     * `speedChanged` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var speedChanged = false
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = false


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `selectBackground`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun selectBackground(index: Int) {
        require(index in 0..3); store.putInt(BG_INDEX, index)
    }


    /**
     * `onSlider`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() =
        View(flags, store.getInt(MSG_SPEED, 1), store.getInt(NOTIFY_LV, 1), store.getInt(BG_INDEX, 0), speed, attached)

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
        /**
         * `GAME_SETTING` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val GAME_SETTING = "GAME_SETTING"
        /**
         * `MSG_SPEED` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MSG_SPEED = "MSG_SPEED"
        /**
         * `GAME_SPEED` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val GAME_SPEED = "GAME_SPEED2"
        /**
         * `NOTIFY_LV` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val NOTIFY_LV = "NOTIFY_LV"
        /**
         * `BG_INDEX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val BG_INDEX = "BG_IDX"
        /**
         * `BG_SOUND` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val BG_SOUND = 1
        /**
         * `EFFECT_SOUND` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val EFFECT_SOUND = 2
        /**
         * `MINI_MAP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MINI_MAP = 4
        /**
         * `AUTO_CLOSE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val AUTO_CLOSE = 8
        /**
         * `BOARD_BAR_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val BOARD_BAR_TOP = 16
        /**
         * `R_IDX_INC` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val R_IDX_INC = 32
        /**
         * `R_ASPECT_RATIO` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val R_ASPECT_RATIO = 64
    }
}
