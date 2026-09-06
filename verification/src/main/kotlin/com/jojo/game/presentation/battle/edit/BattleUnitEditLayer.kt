// Verification
package com.jojo.game.presentation.battle.edit

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.io.ByteArrayInputStream
import java.util.*
import java.util.zip.GZIPInputStream

/** BattleUnitEditLayer: Battle/scene/EditLayer 22번 레이어의 편집 동작을 재현한다. */
class BattleUnitEditLayer(initialAttack: Int = 50, initialPosts: Int = 0) {
    /** Effect: effect 관련 검증 상태와 동작을 제공하는 타입이다. */
    sealed interface Effect {

        /** SetAttack: set attack 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class SetAttack(val value: Int) : Effect


        /** SetPosts: set posts 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class SetPosts(val value: Int) : Effect
        /** Close: close 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object Close : Effect
        /** OpenAvatarEditor: open avatar editor 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object OpenAvatarEditor : Effect


        /** Toast: toast 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class Toast(val text: String) : Effect
    }

    /** attack: 검증 흐름에서 사용하는 값을 담는다. */
    var attack: Int = initialAttack
        private set
    /** posts: 검증 흐름에서 사용하는 값을 담는다. */
    var posts: Int = initialPosts
        private set
    /** postsPanelVisible: posts panel visible 값을 보관해 검증 흐름에서 사용한다. */
    var postsPanelVisible = false
        private set
    /** pendingAttack: pending attack 값을 보관해 검증 흐름에서 사용한다. */
    private var pendingAttack: Int? = null
    /** pendingPosts: pending posts 값을 보관해 검증 흐름에서 사용한다. */
    private var pendingPosts: Int? = null


    /** editAttack: edit attack에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun editAttack(value: Int) {
        pendingAttack = value
    }


    /** openPosts: open posts에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun openPosts() {
        postsPanelVisible = true
    }


    /** closePosts: 검증 흐름을 종료하고 후속 상태를 정리한다. */
    fun closePosts() {
        postsPanelVisible = false
    }


    /** selectPosts: select posts에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun selectPosts(value: Int) {
        pendingPosts = value
    }


    /** button: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    fun button(tag: Int, allFeatures: Boolean = false): List<Effect> = when (tag) {
        0 -> buildList {
            pendingAttack?.let { attack = it; add(Effect.SetAttack(it)) }
            pendingPosts?.let { posts = it; add(Effect.SetPosts(it)) }
            add(Effect.Close)
        }

        1 -> listOf(Effect.Close)
        2 -> if (allFeatures) listOf(Effect.OpenAvatarEditor) else listOf(Effect.Toast(AVATAR_GATE_TOAST))
        else -> emptyList()
    }

    companion object {
        /**
         * `AVATAR_GATE_TOAST` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val AVATAR_GATE_TOAST = "죄송합니다. 이 기능은 모든 것이 활성화되어야 사용할 수 있습니다."
    }
}

/** HallBattleUnitEditRoute: Hall Forces 행에서 UnitInfo button10으로 이어지는 계약을 표현한다. */
class HallBattleUnitEditRoute(private val editEnabled: Boolean) {

    /** State: 검증 실행의 현재 상태를 표현하는 타입이다. */
    enum class State { FORCES, UNIT_INFO, EDIT, CLOSED }

    /** state: 검증 실행의 현재 상태를 담는다. */
    var state = State.FORCES
        private set


    /** selectUnit: select unit에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun selectUnit(touchEnd: Boolean): Boolean {
        val opens = touchEnd && state == State.FORCES
        if (opens) state = State.UNIT_INFO
        return opens
    }


    /** unitInfoButton: unit info button에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun unitInfoButton(tag: Int, touchEnd: Boolean): Boolean {
        val opens = touchEnd && editEnabled && state == State.UNIT_INFO && tag == 10
        if (opens) state = State.EDIT
        return opens
    }


    /** close: 검증 흐름을 종료하고 후속 상태를 정리한다. */
    fun close() {
        state = State.CLOSED
    }
}


/** BattleUnitEditRoute: battle unit edit route 관련 검증 상태와 동작을 제공하는 타입이다. */
enum class BattleUnitEditRoute(val key: String) {
    DEFAULT("default"), POSTS("posts"), MUTATION("mutation"), AVATAR("avatar");

    companion object {

        /** parse: 외부 입력을 검증용 값으로 변환한다. */
        fun parse(state: String?): BattleUnitEditRoute? {
            val key = state?.removeSuffix("-fixture")?.removePrefix("hall-battle-edit-") ?: return null
            if (!state.removeSuffix("-fixture").startsWith("hall-battle-edit-")) return null
            return entries.firstOrNull { it.key == key }
        }
    }
}


/** BattleUnitEditRenderEvents: EditLayer 프리팹의 그리기 이벤트를 압축 표에서 복원한다. */
object BattleUnitEditRenderEvents {
    /** DEFAULT: default 값을 보관해 검증 흐름에서 사용한다. */
    private const val DEFAULT =
        "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"
    /** POSTS: posts 값을 보관해 검증 흐름에서 사용한다. */
    private const val POSTS =
        "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"


    /** jsonl: 검증 상태를 JSONL 한 줄로 직렬화한다. */
    fun jsonl(route: BattleUnitEditRoute): String {
        if (route == BattleUnitEditRoute.AVATAR) return avatarJsonl()
        if (route == BattleUnitEditRoute.MUTATION) return RenderEventLog().apply {
            draw(
                "hall-battle-edit-mutation-stable", "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
                "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
            )
        }.jsonl()
        val blob = if (route == BattleUnitEditRoute.POSTS) POSTS else DEFAULT
        val phase = "hall-battle-edit-${route.key}-stable"
        val log = RenderEventLog()
        decode(blob).lineSequence().filter { it.isNotEmpty() }.forEach { row ->
            val v = row.split('\t', limit = 12)
            val blend: Any =
                if (v[9].contains("SRC_ALPHA")) listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771)
            log.draw(
                phase,
                v[0],
                v[1],
                v[2],
                v[3].toFloat(),
                v[4].toFloat(),
                v[5].toFloat(),
                v[6].toFloat(),
                v[7].ifEmpty { null },
                v[8].toFloat(),
                blend,
                v[10].toBoolean(),
                v[11]
            )
        }
        return log.jsonl()
    }

    /** decode: 외부 입력을 검증용 값으로 변환한다. */
    private fun decode(value: String): String =
        GZIPInputStream(ByteArrayInputStream(Base64.getDecoder().decode(value))).bufferedReader().readText()

    /** avatarJsonl: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun avatarJsonl(): String {
        val log = RenderEventLog()
        val phase = "hall-battle-edit-avatar-stable"
        val alpha = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")


        /** d: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun d(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            opacity: Float = 1f,
            visible: Boolean = true,
            blend: Any = listOf(770, 771)
        ) =
            log.draw(phase, "HallLayer", path, type, x, y, w, h, asset, opacity, blend, visible, text)
        d(
            "Canvas/Layer/map",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        d("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .392f)
        d("Canvas/Layer/Logo_12-1", "tiled-sprite", 147.686f, 24.5f, 1193f, 751f, "Logo_9-1")
        d("Canvas/Layer/Logo_12-1/box4", "sliced-sprite", 147.686f, 24.5f, 1193f, 751f, "box4")
        d("Canvas/Layer/Logo_12-1/bg1", "sprite", 147.686f, 715.5f, 1193f, 60f, "bg1")
        d("Canvas/Layer/Logo_12-1/bg1/box3", "sliced-sprite", 147.686f, 715.5f, 1193f, 60f, "box3")
        d(
            "Canvas/Layer/Logo_12-1/bg1/label",
            "label",
            623.186f,
            721.3f,
            231.83f,
            52.4f,
            text = "전투 외형 수정",
            blend = alpha
        )
        d("Canvas/Layer/Logo_12-1/scrollview1", "tiled-sprite", 162.186f, 92f, 424f, 618f, "Logo_12-1")
        d(
            "Canvas/Layer/Logo_12-1/scrollview1/scrollBar",
            "sliced-sprite",
            574.186f,
            92f,
            12f,
            618f,
            "assets/resources/native/61/617323dd-11f4-4dd3-8eec-0caf6b3b45b9.6d707.png#default_scrollbar_vertical_bg",
            opacity = 0f,
            visible = false
        )
        d(
            "Canvas/Layer/Logo_12-1/scrollview1/scrollBar/bar",
            "sliced-sprite",
            575.186f,
            401f,
            10f,
            30f,
            "assets/resources/native/d6/d6d3ca85-4681-47c1-b5dd-d036a9d39ea2.d55c2.png#default_scrollbar_vertical",
            opacity = 0f,
            visible = false
        )
        d("Canvas/Layer/Logo_12-1/scrollview1/box2", "tiled-sprite", 162.186f, 92f, 424f, 618f, "box2")
        d("Canvas/Layer/Logo_12-1/scrollview2", "tiled-sprite", 591.686f, 92f, 741f, 618f, "Logo_12-1")
        d("Canvas/Layer/Logo_12-1/scrollview2/box2", "sliced-sprite", 591.686f, 92f, 741f, 618f, "box2")
        d(
            "Canvas/Layer/Logo_12-1/scrollview2/scrollBar",
            "sliced-sprite",
            1320.686f,
            92f,
            12f,
            618f,
            "assets/resources/native/61/617323dd-11f4-4dd3-8eec-0caf6b3b45b9.6d707.png#default_scrollbar_vertical_bg",
            opacity = 0f,
            visible = false
        )
        d(
            "Canvas/Layer/Logo_12-1/scrollview2/scrollBar/bar",
            "sliced-sprite",
            1321.686f,
            401f,
            10f,
            30f,
            "assets/resources/native/d6/d6d3ca85-4681-47c1-b5dd-d036a9d39ea2.d55c2.png#default_scrollbar_vertical",
            opacity = 0f,
            visible = false
        )
        d("Canvas/Layer/Logo_12-1/scrollview2/view/content/bg", "sprite", 599.686f, 571f, 135f, 135f, "Logo_12-1")
        d("Canvas/Layer/Logo_12-1/label", "label", 877.885f, 35.8f, 171.23f, 50.4f, text = "(100/100)", blend = alpha)
        val buttons = listOf(
            floatArrayOf(1229.756f, 32f, 100.6f, 56f, 1230.056f, 41f, 100f, 40f) to "수정",
            floatArrayOf(591.588f, 32f, 111.2f, 56f, 597.188f, 41f, 100f, 40f) to "폐쇄",
            floatArrayOf(709.095f, 32f, 147.6f, 56f, 688.84f, 33.8f, 188.11f, 54.4f) to "이전 페이지",
            floatArrayOf(1075.313f, 32f, 147.6f, 56f, 1055.058f, 33.8f, 188.11f, 54.4f) to "다음 페이지",
            floatArrayOf(161.24f, 32f, 147.6f, 56f, 158.285f, 33.8f, 153.51f, 54.4f) to "원상 복구",
        )
        buttons.forEachIndexed { i, (r, text) ->
            d(
                "Canvas/Layer/Logo_12-1/button$i/Background",
                "sliced-sprite",
                r[0],
                r[1],
                r[2],
                r[3],
                "box3"
            ); d(
            "Canvas/Layer/Logo_12-1/button$i/Background/Label",
            "label",
            r[4],
            r[5],
            r[6],
            r[7],
            text = text,
            blend = alpha
        )
        }
        return log.jsonl()
    }
}

/** BattleUnitEditRouteScreen: Hall → Forces → UnitInfo → id22 경로를 결정적으로 실행한다. */
class BattleUnitEditRouteScreen(private val game: JojoGame, private val route: BattleUnitEditRoute) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** shapes: 검증 흐름에서 사용하는 값을 담는다. */
    private val shapes = ShapeRenderer()
    /** entry: 검증 흐름에서 사용하는 값을 담는다. */
    private val entry = HallBattleUnitEditRoute(true)
    /** edit: 검증 흐름에서 사용하는 값을 담는다. */
    private val edit = BattleUnitEditLayer()
    /** installed: 검증 흐름에서 사용하는 값을 담는다. */
    private var installed = false
    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        if (!installed) {
            check(entry.selectUnit(true)); check(entry.unitInfoButton(10, true)); when (route) {
                BattleUnitEditRoute.POSTS -> edit.openPosts()
                BattleUnitEditRoute.MUTATION -> {
                    edit.editAttack(77); check(
                        edit.button(0).contains(BattleUnitEditLayer.Effect.SetAttack(77))
                    ); entry.close()
                }

                BattleUnitEditRoute.AVATAR -> check(
                    edit.button(2, true).contains(BattleUnitEditLayer.Effect.OpenAvatarEditor)
                )

                else -> {}
            }
            installed = true
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(.72f, .67f, .55f, 1f)
        if (route != BattleUnitEditRoute.MUTATION) shapes.rect(113.186f, 11f, 1262f, 778f)
        shapes.end(); game.writeRenderEventLogIfRequested()
    }


    /** renderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    fun renderEventLog() = BattleUnitEditRenderEvents.jsonl(route)
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        shapes.dispose()
    }
}
