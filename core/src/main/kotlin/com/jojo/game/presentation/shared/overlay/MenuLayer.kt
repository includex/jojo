package com.jojo.game.presentation.shared.overlay

/** Testable state implementation of `ui/MenuLayer.js` onCreate/button wiring. */
class MenuLayer {
    /**
     * enum class  `Command`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Command { JSYX, CD, DD, XTSZ, WJYL, DJYL, DX, BW, HHJS, SLTJ, XDT, JSWCZBD, BJ, HELP }

    /**
     * enum class  `Weather`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Weather { QING, YIN, FENG, HAO_YU, XUE }

    /**
     * data class  `CreateData`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class CreateData(
        val weather: Weather, val round: Int, val maxRound: Int, val battleName: String,
        val editEnabled: Boolean = false, val flag: Int = 0, val switchWeather: Weather? = null,
    )

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(
        val weather: Weather, val round: Int, val maxRound: Int, val progress: Float, val battleName: String,
        val buttons: Map<Command, Boolean>, val editingButtonVisible: Boolean, val attached: Boolean,
        val weatherFrames: List<Int>,
        val switchWeather: Weather? = null,
    )

    private var view: View? = null

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `data` (`CreateData`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(data: CreateData): View {
        val max = data.maxRound.coerceAtLeast(1)
        val round = data.round.coerceAtMost(max)
        val buttons = Command.entries.associateWith { command ->
            when {
                data.switchWeather != null -> false
                data.flag and 1 != 0 -> command == Command.JSYX || command == Command.DD
                else -> true
            }
        }
        return View(
            weather = data.switchWeather ?: data.weather, round = round, maxRound = max,
            progress = round.toFloat() / max, battleName = data.battleName, buttons = buttons,
            editingButtonVisible = data.editEnabled, attached = true,
            weatherFrames = listOf(weatherSheet(data.weather), data.switchWeather?.let(::weatherSheet)).filterNotNull(),
            switchWeather = data.switchWeather,
        ).also { view = it }
    }

    /** Panel_cancel (priority 2) only removes; no callback in the JS normal branch. */
    fun onCancel(eventType: Int): View = update(eventType == TOUCH_END)

    /** All command buttons use priority 1 and remove before dispatching the command. */
    fun onCommand(command: Command, eventType: Int): Command? {
        val current = view ?: return null
        if (eventType != TOUCH_END || !current.buttons.getValue(command)) return null
        update(true)
        return command
    }

    /**
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view(): View = requireNotNull(view) { "MenuLayer.onCreate must run before rendering" }

    /** Source `createWeatherOver`: only after both asynchronous sheet loads, then 2s cross-fade. */
    fun switchWeatherLoadComplete(): Boolean {
        val current = requireNotNull(view)
        if (current.switchWeather == null) return false
        weatherLoadCount++
        return weatherLoadCount == 2
    }

    private var weatherLoadCount = 0
    private fun update(remove: Boolean): View {
        val next = requireNotNull(view).copy(attached = if (remove) false else view!!.attached)
        view = next
        return next
    }

    companion object {
        const val TOUCH_END = 2

        /** `cc.AnimationClip.createWithSpriteFrames(a, 6)` in MenuLayer.js. */
        const val WEATHER_FPS = 6f
        const val WEATHER_FRAME_COUNT = 4

        /** Config weather → original Game/Weather/Weather_n-1 sheet number. */
        fun weatherSheet(weather: Weather) = when (weather) {
            Weather.QING -> 1; Weather.YIN -> 2; Weather.FENG -> 3; Weather.HAO_YU -> 4; Weather.XUE -> 5
        }

        /** Source AnimationClip uses a looping frame sequence 0,1,2,3 at 6fps. */
        fun weatherFrameAt(secondsSinceCreate: Float): Int =
            ((secondsSinceCreate.coerceAtLeast(0f) * WEATHER_FPS).toInt() % WEATHER_FRAME_COUNT)
    }
}
