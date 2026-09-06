// Verification
package com.jojo.game

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider

import com.jojo.game.presentation.scenario.overlay.*
import com.jojo.game.presentation.shared.KoreanFont
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport

/** MsgBox3FixtureScreen: 실제 Buy/Sell 어댑터를 통해 MsgBox3 경로를 검증하는 화면이다. */
class MsgBox3FixtureScreen(private val game: JojoGame, private val state: String) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** viewport: 검증 화면의 좌표계와 카메라 상태를 담는다. */
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    /** batch: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val batch = SpriteBatch()
    /** textures: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val textures = mutableListOf<Texture>()
    /** texture: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun texture(path: String): Texture? = Gdx.files.internal(path).takeIf { it.exists() }
        ?.let(::Texture)?.also(textures::add)

    /** background: 화면 배경 리소스를 담는다. */
    private val background = texture("maps/71.jpg")
    /** logo9: 검증 흐름에서 사용하는 값을 담는다. */
    private val logo9 = texture("maps/ui/unit-info/logo9.png")
    /** bg1: 검증 흐름에서 사용하는 값을 담는다. */
    private val bg1 = texture("maps/ui/input-box/bg1.png")
    /** box1: 검증 흐름에서 사용하는 값을 담는다. */
    private val box1 = texture("maps/ui/input-box/box1.png")?.let { NinePatch(it, 3, 3, 3, 3) }
    /** button: 검증 흐름에서 사용하는 값을 담는다. */
    private val button = texture("maps/ui/input-box/box3.png")?.let { NinePatch(it, 9, 9, 7, 11) }
    /** font: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val font: BitmapFont = KoreanFont.create(40, "수치를 입력하세요구매 수량판매 수량구매하기판매하기취소1237")
    /** item: 검증 흐름에서 사용하는 값을 담는다. */
    private val item = ShopItem(150, "회복의 콩", "property", price = 10, sell = 5)
    /** model: 검증 흐름에서 사용하는 값을 담는다. */
    private val model: MsgBox3Layer

    init {
        /**
         * `buy` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buy = ShopPurchaseModel(listOf(item), money = 70, owned = 0, capacity = 99)
        model = if (state == "quantity-buy-initial") {
            buy.openPropertyQuantity(item.id)
        } else {
            // 원본과 같이 BuyLayer에서 세 번째 항목을 확인한 뒤 SellLayer로 진입한다.
            buy.openPropertyQuantity(item.id).also {
                it.textChanged("3")
                it.touchButton(0, 2)
            }
            ShopSaleModel(listOf(item), money = buy.money, owned = buy.owned)
                .openPropertyQuantity(item.id)
                .also { it.textChanged("12") }
        }
    }

    /** show: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        Gdx.input.inputProcessor = object : InputAdapter() {
            /** touchUp: 검증 입력을 현재 화면 상태에 반영한다. */
            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, buttonCode: Int): Boolean {
                val p = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                when {
                    p.x in 524.186f..724.186f && p.y in 312f..362f -> model.touchButton(0, 2)
                    p.x in 764.186f..964.186f && p.y in 312f..362f -> model.touchButton(1, 2)
                    else -> model.touchOutside()
                }
                return true
            }
        }
    }

    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        background?.let { batch.draw(it, 0f, 0f, viewport.worldWidth, 800f) }
        if (model.attached) drawModal()
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    /** drawModal: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    private fun drawModal() {
        logo9?.let { batch.draw(it, 477.336f, 285.8f, 533.7f, 228.4f) }
        bg1?.let { batch.draw(it, 477.336f, 464.2f, 533.7f, 50f) }
        font.color = Color.BLACK
        font.draw(batch, "수치를 입력하세요", 482.336f, 506f, 287.91f, Align.center, false)
        box1?.draw(batch, 477.336f, 285.8f, 533.7f, 228.4f)
        font.draw(batch, model.title, 519.876f, 434f, 267.31f, Align.center, false)
        box1?.draw(batch, 776.886f, 386.5f, 222.6f, 61f)
        font.draw(batch, model.editText, 785.786f, 434f, 206.8f, Align.left, false)
        button?.draw(batch, 524.186f, 312f, 200f, 50f)
        font.draw(batch, model.confirmLabel, 574.186f, 356f, 100f, Align.center, false)
        button?.draw(batch, 764.186f, 312f, 200f, 50f)
        font.draw(batch, "취소", 814.186f, 356f, 100f, Align.center, false)
    }


    /** renderEventLog: 구매·판매 대화 상자의 렌더 이벤트를 비교용 문자열로 반환한다. */
    fun renderEventLog(): String = MsgBox3RenderEvents.jsonl(state, model)
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()

    /** resize: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        batch.dispose()
        font.dispose()
        textures.distinct().forEach(Texture::dispose)
    }
}


/** MsgBox3RenderEvents: MsgBox3 상태를 원본 그리기 이벤트 형식으로 직렬화한다. */
object MsgBox3RenderEvents {

    /** jsonl: MsgBox3 모델의 현재 상태를 JSONL 한 줄로 만든다. */
    fun jsonl(state: String, model: MsgBox3Layer): String {
        if (!model.attached) return ""
        val log = RenderEventLog()
        val phase = "hall-$state-stable"
        /** draw: 검증 대상의 현재 렌더 이벤트를 출력한다. */
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", layer: String = "MsgBox3"
        ) =
            log.draw(
                phase, layer, path, type, x, y, w, h, asset,
                blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
                text = text
            )
        draw(
            "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>", layer = "HallLayer"
        )
        draw("Canvas/Layer/bg0", "tiled-sprite", 477.336f, 285.8f, 533.7f, 228.4f, "Logo_9-1")
        draw("Canvas/Layer/bg0/bg1", "sprite", 477.336f, 464.2f, 533.7f, 50f, "bg1")
        draw("Canvas/Layer/bg0/bg1/label", "label", 482.336f, 464f, 287.91f, 50.4f, text = "수치를 입력하세요")
        draw("Canvas/Layer/bg0/box3", "sliced-sprite", 477.336f, 285.8f, 533.7f, 228.4f, "box1")
        draw("Canvas/Layer/bg0/label", "label", 519.876f, 391.8f, 267.31f, 50.4f, text = model.title)
        draw("Canvas/Layer/bg0/box1", "sliced-sprite", 776.886f, 386.5f, 222.6f, 61f, "box1")
        draw("Canvas/Layer/bg0/box1/editbox/TEXT_LABEL", "label", 785.786f, 392f, 206.8f, 50f, text = model.editText)
        draw("Canvas/Layer/bg0/button0/Background", "sliced-sprite", 524.186f, 312f, 200f, 50f, "box3")
        draw(
            "Canvas/Layer/bg0/button0/Background/Label",
            "label",
            574.186f,
            317.844f,
            100f,
            40f,
            text = model.confirmLabel
        )
        draw("Canvas/Layer/bg0/button1/Background", "sliced-sprite", 764.186f, 312f, 200f, 50f, "box3")
        draw("Canvas/Layer/bg0/button1/Background/Label", "label", 814.186f, 317.844f, 100f, 40f, text = "취소")
        return log.jsonl()
    }
}
