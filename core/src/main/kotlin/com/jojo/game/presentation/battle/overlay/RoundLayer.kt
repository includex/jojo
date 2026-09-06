// Battle
package com.jojo.game.presentation.battle.overlay

/** 현재 턴과 최대 턴 입력을 화면 문구로 변환하고 일정 시간이 지나면 제거를 알린다. */

class RoundLayer(
    private val remove: () -> Unit,
    private val complete: () -> Unit,
) {

    data class CreateArgs(
        val roundPresent: Boolean,
        val round: Int = 0,
        val max: Int? = null,
        val maxPresent: Boolean = max != null,
    )


    /** 턴 문구와 진영 문구의 표시 여부를 렌더링 입력으로 제공한다. */
    data class View(
        val roundLabelsVisible: Boolean,
        val campLabelsVisible: Boolean,
        val roundText: String,
    )

    private var finished = false
    var view: View = View(roundLabelsVisible = false, campLabelsVisible = true, roundText = "")
        private set

    /** 현재 턴·최대 턴 입력을 일반 턴 또는 최종 턴 문구로 변환한다. */
    fun onCreate(args: CreateArgs) {
        val isFinal = args.maxPresent && args.round > (args.max ?: 0)
        val text = if (!args.roundPresent) "" else if (isFinal) "최종 턴" else "제${args.round}턴"
        view = View(
            roundLabelsVisible = args.roundPresent,
            campLabelsVisible = !args.roundPresent,
            roundText = text,
        )
    }

    /** nullable 턴 입력을 CreateArgs로 변환해 라운드 표시를 초기화한다. */
    fun onCreate(round: Int?, max: Int?) = onCreate(
        CreateArgs(roundPresent = round != null, round = round ?: 0, max = max),
    )
    fun elapsed(seconds: Float) {
        if (!finished && seconds >= DISPLAY_SECONDS) {
            finished = true
            remove()
            complete()
        }
    }

    companion object {
        const val DISPLAY_SECONDS = 2f
    }
}
