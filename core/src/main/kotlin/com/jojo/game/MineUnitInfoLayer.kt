package com.jojo.game

/** Stateful implementation of BattleScreen.showMineunitInfo -> Battle registry id6. */
class MineUnitInfoLayer {
    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(
        val name: String, val level: Int, val post: String,
        val hp: Int, val maxHp: Int, val mp: Int, val maxMp: Int,
        val exp: Int, val maxExp: Int, val weaponExp: Int, val armorExp: Int,
        val attached: Boolean, val completionDelay: Float,
    )

    private lateinit var current: View
    private var completion: (() -> Unit)? = null

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `unit` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `post` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `displayName` (`String=unit.name`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `completion` (`(`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(unit: BattleUnit, post: String, displayName: String = unit.name, completion: () -> Unit = {}): View {
        this.completion = completion
        return View(
            displayName, unit.level, post, unit.hitPoints, unit.maxHitPoints, unit.magicPoints, unit.maxMagicPoints,
            0, 100, 0, 0, true, .3f
        ).also { current = it }
    }

    /**
     * 공개 메서드 `complete`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun complete() {
        if (!current.attached) return; current = current.copy(attached = false); completion?.invoke()
    }

    /**
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view() = current

    /**
     * 공개 메서드 `valueAnimation`
     *
     * ### 파라미터
    - `entries` (`List<InfoBaseValueAnimation.Value>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun valueAnimation(entries: List<InfoBaseValueAnimation.Value>) = InfoBaseValueAnimation(entries)
}

/**
 * object  `MineUnitInfoRenderEvents`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object MineUnitInfoRenderEvents {
    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `v` (`MineUnitInfoLayer.View`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(v: MineUnitInfoLayer.View): String {
        require(v.attached)
        val p = "battle-mine-unit-info"
        val l = RenderEventLog()

        /**
         * 공개 메서드 `s`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `a` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun s(path: String, type: String, x: Float, y: Float, w: Float, h: Float, a: String) =
            l.draw(p, "MineUnitInfoLayer", path, type, x, y, w, h, a)

        /**
         * 공개 메서드 `t`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float=54.4f`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
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
