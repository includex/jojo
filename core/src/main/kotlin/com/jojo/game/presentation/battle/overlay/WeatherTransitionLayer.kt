// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.BattleWeather

/**
 * WeatherTransitionLayer: 라운드 전환에서 열리는 날씨 교체 안내를 담당한다.
 *
 * 원본은 `BattleLayer._switchWeather`(recovered-js/modules/battle/BattleLayer.js:5485)에서
 * `MenuLayer`를 `switch_weather` 인자와 함께 띄우고, 콜백이 올 때까지 진영 순회를 멈춘다.
 * `MenuLayer.onCreate`(recovered-js/modules/ui/MenuLayer.js:74~116)는 이전/이후 날씨 노드를
 * 모두 만든 뒤 `cc.fadeOut(2)`/`cc.fadeIn(2)`를 걸고
 * `cc.sequence(cc.delayTime(1), cc.callFunc(소리), cc.delayTime(2), cc.callFunc(제거+fn))`을
 * 실행한다. 즉 화면에 머무는 시간은 1초 + 2초 = 3초이며 교차 페이드는 그 앞 2초를 차지한다.
 * 여기의 상수는 모두 그 시퀀스에서 그대로 옮긴 값이고 맞추기 위해 고른 값이 아니다.
 */
class WeatherTransitionLayer(
    /** `remove` (() -> Unit): 레이어를 화면에서 떼어내는 처리를 위임받는다. */
    private val remove: () -> Unit,
    /** `complete` (() -> Unit): 원본 `fn` 콜백에 대응해 라운드 순회를 재개시킨다. */
    private val complete: () -> Unit,
) {

    /** 이전·이후 날씨와 진행 중인 교차 페이드 비율을 렌더링 입력으로 제공한다. */
    data class View(
        /** `previous` (BattleWeather): 사라지는 쪽 날씨다. */
        val previous: BattleWeather,
        /** `current` (BattleWeather): 나타나는 쪽 날씨다. */
        val current: BattleWeather,
        /** `round` (Int): 진행 막대에 쓰는 현재 턴이다. */
        val round: Int,
        /** `maxRound` (Int): 진행 막대에 쓰는 최대 턴이다. */
        val maxRound: Int,
        /** `progress` (Float): 원본 `ProgressBar.progress`와 같은 round/max_round 값이다. */
        val progress: Float,
        /** `fade` (Float): 0이면 이전 날씨만, 1이면 이후 날씨만 보이는 교차 페이드 비율이다. */
        val fade: Float,
    )

    /** `finished` (Boolean): 완료 콜백을 한 번만 보내기 위한 표식이다. */
    private var finished = false

    /** `soundPlayed` (Boolean): 원본이 `delayTime(1)` 뒤 한 번만 내는 날씨 소리 표식이다. */
    private var soundPlayed = false

    /** `view` (View?): 현재 렌더링 입력이며, 아직 만들어지지 않았으면 null이다. */
    var view: View? = null
        private set

    /**
     * `onCreate`: 원본 `MenuLayer.onCreate`가 받는 round/max_round/weather/switch_weather에 대응한다.
     * 원본은 `t.round = Math.min(t.round, t.max_round)` 뒤 `progress = round / max_round`를 쓴다.
     */
    fun onCreate(previous: BattleWeather, current: BattleWeather, round: Int, maxRound: Int) {
        val safeMax = maxRound.coerceAtLeast(1)
        val shown = round.coerceAtMost(safeMax)
        view = View(
            previous = previous,
            current = current,
            round = shown,
            maxRound = safeMax,
            progress = shown.toFloat() / safeMax.toFloat(),
            fade = 0f,
        )
    }

    /**
     * `elapsed`: 경과 시간을 받아 교차 페이드를 진행하고 3초에 제거·완료를 알린다.
     * 반환값은 이번 호출에서 날씨 소리를 내야 하는지다(원본의 `delayTime(1)` 뒤 `callFunc`).
     */
    fun elapsed(seconds: Float): Boolean {
        view = view?.copy(fade = (seconds / FADE_SECONDS).coerceIn(0f, 1f))
        var playSound = false
        if (!soundPlayed && seconds >= SOUND_DELAY_SECONDS) {
            soundPlayed = true
            playSound = true
        }
        if (!finished && seconds >= HOLD_SECONDS) {
            finished = true
            remove()
            complete()
        }
        return playSound
    }

    companion object {
        /** `FADE_SECONDS`: 원본 `cc.fadeOut(2)`/`cc.fadeIn(2)`의 지속 시간이다. */
        const val FADE_SECONDS = 2f

        /** `SOUND_DELAY_SECONDS`: 원본 시퀀스 첫 `cc.delayTime(1)`이다. */
        const val SOUND_DELAY_SECONDS = 1f

        /** `HOLD_SECONDS`: 원본 시퀀스 `delayTime(1) + delayTime(2)`의 합이다. */
        const val HOLD_SECONDS = SOUND_DELAY_SECONDS + 2f
    }
}
