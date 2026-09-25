// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.i18n.SystemMessage
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
         * `maxWeaponExp`/`maxArmorExp`: 원본 `MineUnitInfoLayer.js:77,83`의 `I`/`L`이다.
         * 장비의 `expLimit()`이며 장비가 없으면 100이다. 값이 상한과 같은 줄만 "MAX"로 바뀐다.
         */

        val maxWeaponExp: Int, val maxArmorExp: Int,
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
            0, 100, 0, 0, 100, 100, true, .3f
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

        /**
         * 스프라이트 기하는 그리기 쪽 `drawSettlementOverlays`가 읽는 것과 **같은 계약**에서
         * 순서대로 꺼낸다. 앞서는 이 증거표가 좌표를 따로 적어 두어, 그리기가 경험치 아이콘을
         * 2px 크게 그려도 두 값이 각자 맞아 어떤 게이트도 떨어지지 않았다. 순서가 어긋나거나
         * 개수가 맞지 않으면 여기서 터진다.
         */
        // 막대 비율: 원본 `cc.ProgressBar.progress`와 같은 0~1이다.
        fun progress(value: Int, max: Int): Float =
            (value.coerceAtLeast(0).toFloat() / max.coerceAtLeast(1)).coerceIn(0f, 1f)

        val geometry = SettlementInfoRenderContract
            .sprites(SettlementInfoRenderContract.Panel.MINE).iterator()

        /**
         * `s`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         *
         * `ratio`는 값에 따라 길이가 변하는 막대만 준다. 계약이 든 폭은 가득 찼을 때의
         * 길이이고, 그리기 쪽도 그 막대들만 계약의 폭을 쓰지 않고 따로 덮는다.
         */
        fun s(path: String, type: String, a: String, ratio: Float? = null) {
            val g = geometry.next()
            val w = if (ratio == null) g.width else g.width * ratio
            // 색: 그리기 쪽은 `batch.color = Color.WHITE`를 세우고 패널이 끝날 때까지 바꾸지
            // 않으므로 흰 색조는 포트가 실제로 아는 값이다. 원본 프리팹의 이 노드들도 `_color`가
            // 없어 엔진 기본 흰색이다.
            l.draw(p, "MineUnitInfoLayer", path, type, g.x, g.y, w, g.height, a, color = SettlementInfoRenderContract.SPRITE_WHITE)
        }


        /**
         * `t`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun t(
            path: String, x: Float, y: Float, w: Float, h: Float = 54.4f, text: String,
            // 무기·방어구 경험치 줄만 상한에 닿았을 때 색이 바뀐다.
            color: String = SettlementInfoRenderContract.LABEL_COLOR,
        ) = l.draw(
            p,
            "MineUnitInfoLayer",
            path,
            "label",
            x,
            y,
            w,
            h,
            blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
            text = text,
            // 그리기 쪽 `drawSettlementOverlays`가 글꼴에 넣는 것과 같은 상수다.
            color = color,
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
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
            // 배경 지도와 메뉴 단추도 색조가 없다. 지도는 `BattleMapRenderer`가, 단추는
            // `drawBattleHudChrome`이 각각 `batch.color = Color.WHITE`로 그린다.
            color = SettlementInfoRenderContract.SPRITE_WHITE,
        )
        l.draw(
            p, "HallLayer", "Canvas/Layer/menu_button/Background", "sprite", 1353.953f, 8f, 60f, 60f, "menu",
            color = SettlementInfoRenderContract.SPRITE_WHITE,
        )
        s("Canvas/Layer/bg", "sprite", "bg2"); s("Canvas/Layer/bg/box3", "sliced-sprite", "box1")
        s("Canvas/Layer/bg/terrain0", "sprite", "Mark_7-1"); s("Canvas/Layer/bg/p0", "sliced-sprite", "default_progressbar_bg"); s("Canvas/Layer/bg/p0/bar", "sliced-sprite", "Mark_3-1", ratio = progress(v.hp, v.maxHp))
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
        s("Canvas/Layer/bg/terrain0", "sprite", "Mark_8-1"); s("Canvas/Layer/bg/p1", "sliced-sprite", "default_progressbar_bg"); s("Canvas/Layer/bg/p1/bar", "sliced-sprite", "Mark_2-1", ratio = progress(v.mp, v.maxMp))
        t("Canvas/Layer/bg/p1/label", 984.945f, 191.8f, 15.11f, text = "/"); t(
            "Canvas/Layer/bg/p1/label0",
            923.98f,
            191.8f,
            45.52f,
            text = v.mp.toString()
        ); t("Canvas/Layer/bg/p1/label1", 1015.5f, 191.8f, 45.52f, text = v.maxMp.toString())
        s("Canvas/Layer/bg/terrain0", "sprite", "Mark_9-1"); s("Canvas/Layer/bg/p2", "sliced-sprite", "default_progressbar_bg"); s("Canvas/Layer/bg/p2/bar", "sliced-sprite", "Mark_6-1", ratio = progress(v.exp, v.maxExp))
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
        s("Canvas/Layer/bg/Mark_61-1", "sprite", "Mark_61-1"); t(
            "Canvas/Layer/bg/label3",
            810.5f,
            97.8f,
            22.25f,
            50.4f,
            SettlementInfoRenderContract.equipmentExperienceText(v.weaponExp, v.maxWeaponExp),
            SettlementInfoRenderContract.equipmentExperienceColor(v.weaponExp, v.maxWeaponExp),
        )
        s("Canvas/Layer/bg/Mark_62-1", "sprite", "Mark_62-1"); t(
            "Canvas/Layer/bg/label4",
            958.5f,
            97.8f,
            22.25f,
            50.4f,
            SettlementInfoRenderContract.equipmentExperienceText(v.armorExp, v.maxArmorExp),
            SettlementInfoRenderContract.equipmentExperienceColor(v.armorExp, v.maxArmorExp),
        )
        // 계약이 든 스프라이트를 하나도 남기지 않고 다 썼는지 확인한다.
        require(!geometry.hasNext()) { SystemMessage.S_3098F076B7 }
        return l.jsonl()
    }
}
