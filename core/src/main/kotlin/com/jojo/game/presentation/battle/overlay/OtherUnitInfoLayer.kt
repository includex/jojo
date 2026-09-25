// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.i18n.GameText
import com.jojo.game.presentation.shared.InfoBaseValueAnimation
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.domain.battle.*


/** 적 유닛의 전투 능력치와 표시 이름을 정보 패널 상태로 만들고 종료 콜백을 호출한다. */
class OtherUnitInfoLayer {

    /** 적 유닛의 이름·직책·HP·MP와 패널 연결 상태를 렌더링 값으로 제공한다. */
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
            displayName, unit.level, post,
            unit.hitPoints, unit.maxHitPoints, unit.magicPoints, unit.maxMagicPoints,
            attached = true, completionDelay = .3f,
        ).also { current = it }
    }


    /**
     * `complete`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun complete() {
        if (!current.attached) return
        current = current.copy(attached = false)
        completion?.invoke()
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): View = current


    /**
     * `valueAnimation`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun valueAnimation(entries: List<InfoBaseValueAnimation.Value>) = InfoBaseValueAnimation(entries)
}
/**
 * `OtherUnitInfoRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object OtherUnitInfoRenderEvents {

    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun jsonl(view: OtherUnitInfoLayer.View): String {
        require(view.attached)
        val phase = "battle-other-unit-info"
        val log = RenderEventLog()


        /**
         * `sprite`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        // 막대 비율: 원본 `cc.ProgressBar.progress`와 같은 0~1이다.
        fun progress(value: Int, max: Int): Float =
            (value.coerceAtLeast(0).toFloat() / max.coerceAtLeast(1)).coerceIn(0f, 1f)

        /**
         * 스프라이트 기하는 그리기 쪽 `drawSettlementOverlays`가 읽는 것과 **같은 계약**에서
         * 순서대로 꺼낸다. 앞서는 이 증거표가 좌표를 따로 적어 두어, 두 값이 각자 맞으면
         * 어긋남이 드러나지 않았다. 순서나 개수가 맞지 않으면 여기서 터진다.
         */
        val geometry = SettlementInfoRenderContract
            .sprites(SettlementInfoRenderContract.Panel.OTHER).iterator()

        /**
         * `sprite`: 타입의 핵심 동작을 수행한다.
         *
         * `ratio`는 값에 따라 길이가 변하는 막대만 준다. 계약이 든 폭은 가득 찼을 때의 길이다.
         */
        fun sprite(path: String, type: String, asset: String, ratio: Float? = null) {
            val g = geometry.next()
            val w = if (ratio == null) g.width else g.width * ratio
            log.draw(
                phase, "OtherUnitInfoLayer", path, type, g.x, g.y, w, g.height, asset,
                color = SettlementInfoRenderContract.SPRITE_WHITE,
            )
        }


        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun label(path: String, x: Float, y: Float, w: Float, text: String) =
            log.draw(
                phase, "OtherUnitInfoLayer", path, "label", x, y, w, 54.4f,
                blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), text = text,
                // 그리기 쪽 `drawSettlementOverlays`가 글꼴에 넣는 것과 같은 상수다.
                color = SettlementInfoRenderContract.LABEL_COLOR,
            )

        // 배경 지도와 메뉴 단추도 색조가 없다. 지도는 `BattleMapRenderer`가, 단추는
        // `drawBattleHudChrome`이 각각 `batch.color = Color.WHITE`로 그린다.
        log.draw(
            phase, "HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite",
            -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
            color = SettlementInfoRenderContract.SPRITE_WHITE,
        )
        log.draw(
            phase, "HallLayer", "Canvas/Layer/menu_button/Background", "sprite",
            1353.953f, 8f, 60f, 60f, "menu", color = SettlementInfoRenderContract.SPRITE_WHITE,
        )
        sprite("Canvas/Layer/bg", "sprite", "bg2")
        sprite("Canvas/Layer/bg/box3", "sliced-sprite", "box1")
        sprite("Canvas/Layer/bg/terrain0", "sprite", "Mark_7-1")
        sprite("Canvas/Layer/bg/p0", "sliced-sprite", "default_scrollbar_bg")
        sprite("Canvas/Layer/bg/p0/bar", "sliced-sprite", "Mark_3-1", ratio = progress(view.hp, view.maxHp))
        label("Canvas/Layer/bg/p0/label0", 906.73f, 174.55f, 67.77f, view.hp.toString())
        label("Canvas/Layer/bg/p0/label1", 1016.5f, 174.55f, 67.77f, view.maxHp.toString())
        label("Canvas/Layer/bg/p0/label", 987.945f, 174.55f, 15.11f, "/")
        sprite("Canvas/Layer/bg/terrain0", "sprite", "Mark_8-1")
        sprite("Canvas/Layer/bg/p1", "sliced-sprite", "default_scrollbar_bg")
        sprite("Canvas/Layer/bg/p1/bar", "sliced-sprite", "Mark_2-1", ratio = progress(view.mp, view.maxMp))
        label("Canvas/Layer/bg/p1/label", 987.945f, 116.55f, 15.11f, "/")
        label("Canvas/Layer/bg/p1/label0", 928.98f, 116.55f, 45.52f, view.mp.toString())
        label("Canvas/Layer/bg/p1/label1", 1016.5f, 116.55f, 45.52f, view.maxMp.toString())
        label("Canvas/Layer/bg/label0", 744.9f, 226.85f, 148.3f, view.name)
        label("Canvas/Layer/bg/label", 912.256f, 226.815f, 46.25f, "Lv")
        label("Canvas/Layer/bg/label1", 1005.002f, 226.815f, 26.25f, view.level.toString())
        label("Canvas/Layer/bg/label2", 1049.3f, 226.85f, 147.6f, view.post)
        require(!geometry.hasNext()) { GameText.S_3098F076B7 }
        return log.jsonl()
    }
}
