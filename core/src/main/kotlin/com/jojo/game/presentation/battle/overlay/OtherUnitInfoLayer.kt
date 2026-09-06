// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.InfoBaseValueAnimation
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.domain.battle.*


/** 적 유닛의 전투 능력치와 표시 이름을 정보 패널 상태로 만들고 종료 콜백을 호출한다. */
class OtherUnitInfoLayer {

    /** 적 유닛의 이름·직책·HP·MP와 패널 연결 상태를 렌더링 값으로 제공한다. */
    data class View(
        val name: String, val level: Int, val post: String,
        val hp: Int, val maxHp: Int, val mp: Int, val maxMp: Int,
        val attached: Boolean, val completionDelay: Float,
    )

    private lateinit var current: View
    private var completion: (() -> Unit)? = null


    fun onCreate(unit: BattleUnit, post: String, displayName: String = unit.name, completion: () -> Unit = {}): View {
        this.completion = completion
        return View(
            displayName, unit.level, post,
            unit.hitPoints, unit.maxHitPoints, unit.magicPoints, unit.maxMagicPoints,
            attached = true, completionDelay = .3f,
        ).also { current = it }
    }


    fun complete() {
        if (!current.attached) return
        current = current.copy(attached = false)
        completion?.invoke()
    }


    fun view(): View = current


    fun valueAnimation(entries: List<InfoBaseValueAnimation.Value>) = InfoBaseValueAnimation(entries)
}
object OtherUnitInfoRenderEvents {

    fun jsonl(view: OtherUnitInfoLayer.View): String {
        require(view.attached)
        val phase = "battle-other-unit-info"
        val log = RenderEventLog()


        fun sprite(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String) =
            log.draw(phase, "OtherUnitInfoLayer", path, type, x, y, w, h, asset)


        fun label(path: String, x: Float, y: Float, w: Float, text: String) =
            log.draw(
                phase, "OtherUnitInfoLayer", path, "label", x, y, w, 54.4f,
                blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), text = text
            )

        log.draw(
            phase, "HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite",
            -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>"
        )
        log.draw(
            phase, "HallLayer", "Canvas/Layer/menu_button/Background", "sprite",
            1353.953f, 8f, 60f, 60f, "menu"
        )
        sprite("Canvas/Layer/bg", "sprite", 736f, 96f, 471f, 193.5f, "bg2")
        sprite("Canvas/Layer/bg/box3", "sliced-sprite", 736f, 96f, 471f, 193f, "box1")
        sprite("Canvas/Layer/bg/terrain0", "sprite", 747.5f, 179.75f, 48f, 40f, "Mark_7-1")
        sprite("Canvas/Layer/bg/p0", "sliced-sprite", 808.5f, 177.75f, 374f, 24f, "default_scrollbar_bg")
        sprite("Canvas/Layer/bg/p0/bar", "sliced-sprite", 810.5f, 179.75f, 370f, 20f, "Mark_3-1")
        label("Canvas/Layer/bg/p0/label0", 906.73f, 174.55f, 67.77f, view.hp.toString())
        label("Canvas/Layer/bg/p0/label1", 1016.5f, 174.55f, 67.77f, view.maxHp.toString())
        label("Canvas/Layer/bg/p0/label", 987.945f, 174.55f, 15.11f, "/")
        sprite("Canvas/Layer/bg/terrain0", "sprite", 746.5f, 121.75f, 48f, 48f, "Mark_8-1")
        sprite("Canvas/Layer/bg/p1", "sliced-sprite", 808.5f, 119.75f, 374f, 24f, "default_scrollbar_bg")
        sprite("Canvas/Layer/bg/p1/bar", "sliced-sprite", 810.5f, 121.75f, 370f, 20f, "Mark_2-1")
        label("Canvas/Layer/bg/p1/label", 987.945f, 116.55f, 15.11f, "/")
        label("Canvas/Layer/bg/p1/label0", 928.98f, 116.55f, 45.52f, view.mp.toString())
        label("Canvas/Layer/bg/p1/label1", 1016.5f, 116.55f, 45.52f, view.maxMp.toString())
        label("Canvas/Layer/bg/label0", 744.9f, 226.85f, 148.3f, view.name)
        label("Canvas/Layer/bg/label", 912.256f, 226.815f, 46.25f, "Lv")
        label("Canvas/Layer/bg/label1", 1005.002f, 226.815f, 26.25f, view.level.toString())
        label("Canvas/Layer/bg/label2", 1049.3f, 226.85f, 147.6f, view.post)
        return log.jsonl()
    }
}
