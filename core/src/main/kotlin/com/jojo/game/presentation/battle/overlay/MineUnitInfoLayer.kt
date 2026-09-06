// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.InfoBaseValueAnimation
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.domain.battle.*


/** 아군 유닛의 이름·직책·능력치·상태를 정보 패널용 값으로 변환한다. */
class MineUnitInfoLayer {

    /** 아군 유닛의 이름, 능력치, 장비·기기 행을 렌더링 값으로 제공한다. */
    data class View(
        /**
         * `name` (String, val level: Int, val post: String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String, val level: Int, val post: String,
        /**
         * `hp` (Int, val maxHp: Int, val mp: Int, val maxMp: Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hp: Int, val maxHp: Int, val mp: Int, val maxMp: Int,
        /**
         * `exp` (Int, val maxExp: Int, val weaponExp: Int, val armorExp: Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val exp: Int, val maxExp: Int, val weaponExp: Int, val armorExp: Int,
        /**
         * `attached` (Boolean, val completionDelay: Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attached: Boolean, val completionDelay: Float,
    )

    /**
     * `current` (View): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private lateinit var current: View
    /**
     * `completion` ((() -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var completion: (() -> Unit)? = null


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(unit: BattleUnit, post: String, displayName: String = unit.name, completion: () -> Unit = {}): View {
        this.completion = completion
        return View(
            displayName, unit.level, post, unit.hitPoints, unit.maxHitPoints, unit.magicPoints, unit.maxMagicPoints,
            0, 100, 0, 0, true, .3f
        ).also { current = it }
    }


    /**
     * `complete`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun complete() {
        if (!current.attached) return; current = current.copy(attached = false); completion?.invoke()
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() = current


    /**
     * `valueAnimation`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun valueAnimation(entries: List<InfoBaseValueAnimation.Value>) = InfoBaseValueAnimation(entries)
}
/**
 * `MineUnitInfoRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object MineUnitInfoRenderEvents {

    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun jsonl(v: MineUnitInfoLayer.View): String {
        require(v.attached)
        val p = "battle-mine-unit-info"
        val l = RenderEventLog()


        /**
         * `s`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun s(path: String, type: String, x: Float, y: Float, w: Float, h: Float, a: String) =
            l.draw(p, "MineUnitInfoLayer", path, type, x, y, w, h, a)


        /**
         * `t`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun t(path: String, x: Float, y: Float, w: Float, h: Float = 54.4f, text: String) = l.draw(
            p,
            "MineUnitInfoLayer",
            path,
            "label",
            x,
            y,
            w,
            h,
            blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
            text = text
        )
        l.draw(
            p,
            "HallLayer",
            "Canvas/Layer/ScrollView/view/content/map",
            "sprite",
            -320f,
            -96f,
            1920f,
            1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>"
        )
        l.draw(p, "HallLayer", "Canvas/Layer/menu_button/Background", "sprite", 1353.953f, 8f, 60f, 60f, "menu")
        s("Canvas/Layer/bg", "sprite", 736f, 96f, 471f, 258f, "bg2"); s(
            "Canvas/Layer/bg/box3",
            "sliced-sprite",
            736f,
            96f,
            471f,
            257.5f,
            "box1"
        )
        s("Canvas/Layer/bg/terrain0", "sprite", 747.5f, 251f, 48f, 40f, "Mark_7-1"); s(
            "Canvas/Layer/bg/p0",
            "sliced-sprite",
            805.5f,
            249f,
            374f,
            24f,
            "default_progressbar_bg"
        ); s("Canvas/Layer/bg/p0/bar", "sliced-sprite", 807.5f, 251f, 370f, 20f, "Mark_3-1")
        t(
            "Canvas/Layer/bg/p0/label1",
            1015.5f,
            245.8f,
            67.77f,
            text = v.maxHp.toString()
        ); t(
            "Canvas/Layer/bg/p0/label0",
            901.73f,
            245.8f,
            67.77f,
            text = v.hp.toString()
        ); t("Canvas/Layer/bg/p0/label", 984.945f, 245.8f, 15.11f, text = "/")
        s("Canvas/Layer/bg/terrain0", "sprite", 747.5f, 200f, 48f, 48f, "Mark_8-1"); s(
            "Canvas/Layer/bg/p1",
            "sliced-sprite",
            805.5f,
            198f,
            374f,
            24f,
            "default_progressbar_bg"
        ); s("Canvas/Layer/bg/p1/bar", "sliced-sprite", 807.5f, 200f, 370f, 20f, "Mark_2-1")
        t("Canvas/Layer/bg/p1/label", 984.945f, 191.8f, 15.11f, text = "/"); t(
            "Canvas/Layer/bg/p1/label0",
            923.98f,
            191.8f,
            45.52f,
            text = v.mp.toString()
        ); t("Canvas/Layer/bg/p1/label1", 1015.5f, 191.8f, 45.52f, text = v.maxMp.toString())
        s("Canvas/Layer/bg/terrain0", "sprite", 747.5f, 149f, 48f, 46f, "Mark_9-1"); s(
            "Canvas/Layer/bg/p2",
            "sliced-sprite",
            805.5f,
            147f,
            374f,
            24f,
            "default_progressbar_bg"
        ); s("Canvas/Layer/bg/p2/bar", "sliced-sprite", 807.5f, 149f, 0f, 20f, "Mark_6-1")
        t("Canvas/Layer/bg/p2/label", 984.945f, 140.8f, 15.11f, text = "/"); t(
            "Canvas/Layer/bg/p2/label0",
            943.25f,
            140.8f,
            26.25f,
            text = v.exp.toString()
        ); t("Canvas/Layer/bg/p2/label1", 1015.5f, 140.8f, 70.74f, text = v.maxExp.toString())
        t("Canvas/Layer/bg/label0", 744.4f, 294.5f, 146.2f, text = v.name); t(
            "Canvas/Layer/bg/label",
            911.105f,
            294.518f,
            46.25f,
            text = "Lv"
        ); t(
            "Canvas/Layer/bg/label1",
            1004.622f,
            294.518f,
            26.25f,
            text = v.level.toString()
        ); t("Canvas/Layer/bg/label2", 1045.55f, 294.5f, 153.5f, text = v.post)
        s("Canvas/Layer/bg/Mark_61-1", "sprite", 769.5f, 108f, 30f, 30f, "Mark_61-1"); t(
            "Canvas/Layer/bg/label3",
            810.5f,
            97.8f,
            22.25f,
            50.4f,
            v.weaponExp.toString()
        )
        s("Canvas/Layer/bg/Mark_62-1", "sprite", 919.5f, 107f, 32f, 32f, "Mark_62-1"); t(
            "Canvas/Layer/bg/label4",
            958.5f,
            97.8f,
            22.25f,
            50.4f,
            v.armorExp.toString()
        )
        return l.jsonl()
    }
}
