// Presentation
package com.jojo.game.presentation.shared.overlay

/** MenuLayer: 전투의 날씨·라운드·명령 활성 조건을 계산해 메뉴 화면 모델로 제공한다. */
class MenuLayer {
    /** 메뉴에서 선택할 수 있는 명령 종류입니다. */
    enum class Command { JSYX, CD, DD, XTSZ, WJYL, DJYL, DX, BW, HHJS, SLTJ, XDT, JSWCZBD, BJ, HELP }

    /** 전투 날씨 종류입니다. */
    enum class Weather { QING, YIN, FENG, HAO_YU, XUE }

    /** 메뉴 생성에 필요한 초기 상태입니다. */
    data class CreateData(
        val weather: Weather, val round: Int, val maxRound: Int, val battleName: String,
        val editEnabled: Boolean = false, val flag: Int = 0, val switchWeather: Weather? = null,
    )

    /** 메뉴를 그리는 데 필요한 현재 상태입니다. */
    data class View(
        val weather: Weather, val round: Int, val maxRound: Int, val progress: Float, val battleName: String,
        val buttons: Map<Command, Boolean>, val editingButtonVisible: Boolean, val attached: Boolean,
        val weatherFrames: List<Int>,
        val switchWeather: Weather? = null,
    )

    private var view: View? = null

    /** 입력 데이터로 메뉴 상태를 생성합니다. */
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

    /** 취소 버튼의 터치 종료를 메뉴 닫기로 처리합니다. */
    fun onCancel(eventType: Int): View = update(eventType == TOUCH_END)

    /** 활성화된 명령 버튼을 선택하고 메뉴를 닫습니다. */
    fun onCommand(command: Command, eventType: Int): Command? {
        val current = view ?: return null
        if (eventType != TOUCH_END || !current.buttons.getValue(command)) return null
        update(true)
        return command
    }

    /** 현재 메뉴 상태를 반환합니다. */
    fun view(): View = requireNotNull(view) { "MenuLayer.onCreate must run before rendering" }

    /** 날씨 시트 두 개가 로드되었는지 확인합니다. */
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

        /** 날씨 애니메이션의 초당 프레임 수입니다. */
        const val WEATHER_FPS = 6f
        const val WEATHER_FRAME_COUNT = 4

        /** 날씨 종류를 원본 시트 번호로 변환합니다. */
        fun weatherSheet(weather: Weather) = when (weather) {
            Weather.QING -> 1; Weather.YIN -> 2; Weather.FENG -> 3; Weather.HAO_YU -> 4; Weather.XUE -> 5
        }

        /** 경과 시간에 해당하는 반복 날씨 프레임을 반환합니다. */
        fun weatherFrameAt(secondsSinceCreate: Float): Int =
            ((secondsSinceCreate.coerceAtLeast(0f) * WEATHER_FPS).toInt() % WEATHER_FRAME_COUNT)
    }
}
