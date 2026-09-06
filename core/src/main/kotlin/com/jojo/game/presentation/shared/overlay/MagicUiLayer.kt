// Shared
package com.jojo.game.presentation.shared.overlay

/** MagicUiList: 마법 목록과 마법 화면의 프레임워크 독립 UI 상태를 구현한다. */
class MagicUiList(val mp: Int, val maxMp: Int, magics: List<Magic>, uses: Map<Int, Int>) {

    data class Magic(
        val id: Int,
        val name: String,
        val cost: Int,
        val power: Int?,
        val icon: Int,
        val hit: Int,
        val eff: Int,
        val intro: String
    )

    var uses = uses.toMutableMap(); private set
    val rows =
        magics.filter { it.cost != 255 }.sortedWith(compareByDescending<Magic> { uses[it.id] ?: 0 }.thenBy { it.id })

    // 원본 목록의 두 번째 진행 막대는 터치 처리나 길게 누르기 완료 때만 갱신되며,
    // 프리팹의 초기값은 0이다.
    var preview = 0f; private set
    var attached = true; private set
    private var pending: Magic? = null
    val events = mutableListOf<String>()


    fun enabled(index: Int) = rows.getOrNull(index)?.let { mp >= it.cost } == true


    fun start(index: Int) {
        val magic = rows.getOrNull(index) ?: return; if (mp < magic.cost) return; preview =
            (mp - magic.cost).coerceAtLeast(0).toFloat() / maxMp.coerceAtLeast(1); pending = magic
    }


    fun end(index: Int) {
        val magic = rows.getOrNull(index) ?: return; if (pending !== magic) return; attached = false; uses[magic.id] =
            (uses[magic.id] ?: 0) + 1; events += "remove"; events += "selected:${magic.id}"; pending = null
    }


    fun tick(): Magic? {
        val magic = pending ?: return null; pending = null; preview =
            mp.toFloat() / maxMp.coerceAtLeast(1); events += "layer:MagicLayer:${magic.id}"; return magic
    }


    fun cancel(event: Int) {
        if (event == 2) {
            attached = false; events += "remove"; events += "cancelled"
        }
    }

    companion object {
        const val TOUCH_END = 2
    }
}


class MagicInfoLayer(val magic: MagicUiList.Magic) {
    var attached = true; private set
    val assets = listOf(
        "asset:Game/Magic/${magic.icon + 1}-1",
        "asset:Game/Hitarea/${magic.hit + 1}-1",
        "asset:Game/Effarea/${magic.eff + 1}-1"
    )


    fun close(event: Int) {
        if (event == 2) attached = false
    }
}
