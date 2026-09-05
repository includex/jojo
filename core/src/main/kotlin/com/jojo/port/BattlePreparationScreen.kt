package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport

internal data class CampaignE2eBattlePreparationState(
    val returnScenario: String,
    val sourceScenario: String,
    val campaignStage: Int,
    val selectedCount: Int,
    val minimum: Int,
    val maximum: Int,
    val cursorSelected: Boolean,
    val canStart: Boolean,
)

/** Direct visual/interaction port of Hall/scene/StartBattleLayer. */
class BattlePreparationScreen(
    private val game: JojoGame,
    private val returnScenario: String,
    private val sourceScenario: String,
    private val limit: ScenarioJoinBattleLimit,
    private val campaign: CampaignState,
    private val backgroundId: Int,
) : ScreenAdapter() {
    private companion object { const val ATTR_POSTS = 17; const val ATTR_LEVEL = 18; const val ATTR_EXP = 19 }
    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    private val batch = SpriteBatch()
    private val data = OriginalGameData.load()
    private val available = (campaign.joinedUnits + limit.requiredUnitIds)
        .filterNot { it in limit.excludedUnitIds }.distinct()
        .sortedWith(compareBy<Int> { data.unitProfile(it)?.armId ?: Int.MAX_VALUE }.thenBy { it })
    private val selection = limit.requiredUnitIds.filter { it in available }.distinct().toMutableList()
    private var cursor = available.indexOf(selection.firstOrNull()).coerceAtLeast(0)

    private val glyphs = "Lv.EXPHPMP무력민첩성지력운기지휘공격방어정신폭발사기이동무장정보출진부대속성결정취소필수최소최대없음군웅조조병사허자장0123456789-/: " +
        "열전특성능력장비마법상태현금인물정상입니다모든특기보기기본소개출진횟수퇴각이전다음" +
        available.joinToString("") { data.unitProfile(it)?.name.orEmpty() } +
        available.joinToString("") { data.armProfile(data.unitProfile(it)?.armId ?: -1)?.name.orEmpty() }
    private val font = KoreanFont.create(31, glyphs, fillColor = Color.BLACK)
    private val rosterFont = KoreanFont.create(32, glyphs)
    private val rosterNameFont = KoreanFont.create(31, glyphs, 1.6f, Color.RED, Color.WHITE)
    private val layout = GlyphLayout()

    private val textures = mutableListOf<Texture>()
    private fun texture(path: String): Texture? = Gdx.files.internal(path).takeIf { it.exists() }?.let { file ->
        Texture(file).also { it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); textures += it }
    }
    private val background = texture("maps/$backgroundId.jpg") ?: texture("maps/71.jpg")
    private val logo9 = texture("maps/ui/start-battle/logo9.png")
    private val roster = texture("maps/ui/start-battle/roster.png")
    private val selected = texture("maps/ui/start-battle/selected.png")
    private val box2 = texture("maps/ui/start-battle/box2.png")
    private val slotOpen = texture("maps/ui/start-battle/slot-open.png")
    private val slotRequired = texture("maps/ui/start-battle/slot-required.png")
    private val slotMinimum = texture("maps/ui/start-battle/slot-minimum.png")
    private val button = texture("maps/ui/start-battle/button.png")
    private val box1 = texture("maps/ui/start-battle/box1.png")
    private val title = texture("maps/ui/start-battle/title.png")
    private val unitInfoBox1 = texture("maps/ui/unit-info/box1.png")
    private val unitInfoBox3 = texture("maps/ui/unit-info/box3.png")
    private val unitInfoBg1 = texture("maps/ui/unit-info/bg1.png")
    private val dim = Pixmap(1, 1, Pixmap.Format.RGBA8888).let { pixmap ->
        pixmap.setColor(Color.BLACK); pixmap.fill()
        Texture(pixmap).also { textures += it }.also { pixmap.dispose() }
    }
    private val outerPatch = button?.let { NinePatch(it, 9, 9, 7, 11) }
    private val box1Patch = box1?.let { NinePatch(it, 3, 3, 3, 3) }
    private val titlePatch = title?.let { NinePatch(it, 5, 5, 5, 5) }
    private val avatarTextures = mutableMapOf<Int, Texture>()
    private val faceTextures = mutableMapOf<Int, Texture>()
    private val unitInfoFixture = game.requestedCaptureState() == "start-battle-unit-info-fixture"
    private val battleViewFixture = game.requestedCaptureState() == "battle-view-fixture"
    private val battleSortState = game.requestedCaptureState()?.removeSuffix("-fixture")?.takeIf { it.startsWith("start-battle-sort-") }
    private val battleSort = StartBattleSortRoute()
    private val battleView = BattleViewLayer().also {
        if (battleViewFixture) it.onCreate(0, listOf(4 to 4, 5 to 4, 6 to 4, 7 to 4))
    }
    private val battleViewMap = texture("maps/battle-maps/1.png")

    init {
        if (battleSortState != null) {
            battleSort.openFromButton(865.186f, 321f, 50f, true)
            when (battleSortState) {
                "start-battle-sort-select" -> battleSort.select(2, true)
                "start-battle-sort-cancel" -> battleSort.cancel(true)
            }
        }
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun keyDown(keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.LEFT -> moveCursor(-1)
                    Input.Keys.RIGHT -> moveCursor(1)
                    Input.Keys.UP -> moveCursor(-6)
                    Input.Keys.DOWN -> moveCursor(6)
                    Input.Keys.SPACE -> toggle(available.getOrNull(cursor))
                    Input.Keys.ENTER -> startBattle()
                    Input.Keys.ESCAPE -> game.showScenario(returnScenario)
                }
                return true
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, mouseButton: Int): Boolean {
                val point = viewport.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
                if (battleSort.open) {
                    if (point.x !in 658f..831f || point.y !in 32f..276f) battleSort.cancel(true)
                    else battleSort.select(((276f - point.y) / 46.4f).toInt().coerceIn(0, 4), true)
                    return true
                }
                available.forEachIndexed { index, id ->
                    val cx = (233.686f + index % 6 * 133f) * .86f
                    val cy = (667.5f - index / 6 * 144f) * .86f
                    if (point.x in cx - 57.2f..cx + 57.2f && point.y in cy - 61.9f..cy + 61.9f) {
                        cursor = index; toggle(id); return true
                    }
                }
                selection.forEachIndexed { index, id ->
                    val cx = (217.336f + index * 100f) * .86f
                    if (point.x in cx - 43f..cx + 43f && point.y in 183f..278f) {
                        cursor = available.indexOf(id).coerceAtLeast(0); toggle(id); return true
                    }
                }
                if (point.y in 49f..93f && point.x in 954f..1040f) { startBattle(); return true }
                if (point.y in 49f..93f && point.x in 1048f..1135f) { game.showScenario(returnScenario); return true }
                if (point.x in 658f..831f && point.y in 276f..320f) {
                    battleSort.openFromButton(865.186f, 321f, 50f, true)
                    return true
                }
                return true
            }
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        background?.let { batch.draw(it, 0f, 0f, 1280f, 688f) }
        batch.color = Color(1f, 1f, 1f, 30f / 255f)
        batch.draw(dim, 0f, 0f, 1280f, 688f)
        if (battleViewFixture) {
            drawBattleViewOverlay()
            batch.end()
            game.writeRenderEventLogIfRequested()
            return
        }
        batch.color = Color.WHITE
        // Logo_9-1 is a Cocos TILED sprite. Stretching its 96px tile created
        // the huge blurry emblems that made the preparation UI visibly unlike
        // the source.
        logo9?.let { drawTiled(it, 138.061f, 43f, 1003.878f, 602f) }
        // ScrollView content is 736 high with a top anchor, while its view is
        // only 363 high.  The source therefore exposes the upper half of the
        // U_select_4 frame; scaling the whole frame made the blue panel much
        // brighter and displaced its relief artwork.
        roster?.let { batch.draw(it, 143.78f, 323.79f, 688f, 312.18f, 0, 0, 400, 146, false, false) }
        selected?.let { batch.draw(it, 143.91f, 52.57f, 688f, 220.16f) }
        outerPatch?.draw(batch, 138.061f, 43f, 1003.878f, 602f)
        box1Patch?.draw(batch, 834.575f, 96.793f, 298.85f, 479.966f)
        titlePatch?.draw(batch, 857.565f, 557.487f, 139.062f, 34.658f)
        drawRoster()
        drawSelectedSlots()
        drawUnitInfo(available.getOrNull(cursor) ?: selection.firstOrNull())
        drawButton(954.76f, 49.88f, "결정", selection.size in limit.minimum..limit.maximum)
        drawButton(1049.36f, 49.88f, "취소", true)
        if (battleSort.open) drawBattleSortMenu()
        if (unitInfoFixture) drawUnitInfoFixtureOverlay()
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    /** The UnitInfoLayer reached by holding the first StartBattle roster row. */
    private fun drawUnitInfoFixtureOverlay() {
        batch.color = Color(1f, 1f, 1f, 100f / 255f)
        batch.draw(dim, 0f, 0f, 1280f, 688f)
        batch.color = Color.WHITE
        logo9?.let { drawTiled(it, 197.186f * .86f, 12f * .86f, 1094f * .86f, 776f * .86f) }
        unitInfoBg1?.let { batch.draw(it, 197.186f * .86f, 738f * .86f, 1094f * .86f, 50f * .86f) }
        face(0)?.let { batch.draw(it, 230.186f * .86f, 490.956f * .86f, 192f * .86f, 240f * .86f) }
        val patch = unitInfoBox1?.let { NinePatch(it, 3, 3, 3, 3) }
        patch?.draw(batch, 454.186f * .86f, 509.642f * .86f, 358f * .86f, 144f * .86f)
        patch?.draw(batch, 454.186f * .86f, 339.359f * .86f, 358f * .86f, 144f * .86f)
        patch?.draw(batch, 821.986f * .86f, 71.95f * .86f, 457f * .86f, 580.5f * .86f)
        val buttonPatch = unitInfoBox3?.let { NinePatch(it, 3, 3, 3, 3) }
        listOf(
            Triple(825.923f, 712.65f, "무장 열전"), Triple(1014.008f, 712.65f, "부대 특성"),
            Triple(826.481f, 651.471f, "능력"), Triple(956.444f, 651.471f, "장비"), Triple(1086.444f, 651.471f, "마법"),
        ).forEach { (x, y, value) ->
            buttonPatch?.draw(batch, x * .86f, y * .86f, (if (value.length > 2) 190f else 130f) * .86f, 60f * .86f)
            font.color = Color.BLACK
            font.draw(batch, value, (x + 12f) * .86f, (y + 43f) * .86f)
        }
        font.color = Color.BLACK
        font.draw(batch, "무장 정보", 202.186f * .86f, 780f * .86f)
        font.draw(batch, "조조", 455.186f * .86f, 716f * .86f)
        font.draw(batch, "부대 속성", 475.267f * .86f, 670f * .86f)
        font.draw(batch, "군웅        Lv     3", 466.186f * .86f, 620f * .86f)
        font.draw(batch, "Exp                  0/100", 466.186f * .86f, 561f * .86f)
        font.draw(batch, "상태", 476.844f * .86f, 496f * .86f)
        font.draw(batch, "HP                 123/123", 468.186f * .86f, 451f * .86f)
        font.draw(batch, "MP                 36/36", 468.186f * .86f, 395f * .86f)
        font.draw(batch, "기본 능력", 853.036f * .86f, 644f * .86f)
        font.draw(batch, "무력 82       민첩성 80\n지력 92       운기 84\n지휘 98", 848.106f * .86f, 588f * .86f)
        batch.color = Color.WHITE
    }

    fun renderEventLog(): String = when {
        battleViewFixture -> battleViewRenderEventLog()
        battleSortState != null -> battleSortRenderEventLog()
        else -> RenderEventLog().also { appendStartBattleRenderEvents(it, unitInfoFixture) }.jsonl()
    }

    private fun drawBattleSortMenu() {
        batch.color = Color(1f, 1f, 1f, 60f / 255f)
        batch.draw(dim, 0f, 0f, 1280f, 688f)
        batch.color = Color.WHITE
        box1Patch?.draw(batch, 658.06f, 32.25f, 172f, 243.81f)
        listOf("부대 속성", "공격력", "정신력", "방어력", "레벨").forEachIndexed { index, value ->
            outerPatch?.draw(batch, 665.64f, 40.42f + (4 - index) * 46.44f, 155.57f, 43f)
            centered(font, value, 743.43f, 70f + (4 - index) * 46.44f)
        }
    }

    private fun battleSortRenderEventLog(): String {
        val route = requireNotNull(battleSortState)
        val phase = "hall-$route-stable"
        val open = route.endsWith("open")
        val base = RenderEventLog().also {
            appendStartBattleRenderEvents(it, false, phase, 1f, spiritSorted = route.endsWith("select"))
        }.jsonl().let { json -> if (open) json.replace("\"layer\":\"StartBattleLayer\"", "\"layer\":\"BattleSortLayer\"") else json }
        if (!open) return base
        val extra = RenderEventLog(sequenceOffset = 97)
        extra.draw(phase, "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 60f / 255f)
        extra.draw(phase, "HallLayer", "Canvas/Layer/menu", "sliced-sprite", 765.186f, 37.5f, 200f, 283.5f, "bg1")
        extra.draw(phase, "HallLayer", "Canvas/Layer/menu/bg1", "tiled-sprite", 765.186f, 37.5f, 200f, 283.5f, "box3")
        val labels = listOf("부대 속성", "공격력", "정신력", "방어력", "레벨")
        val labelX = listOf(782.448f, 799.448f, 799.448f, 799.448f, 814.448f)
        val labelW = listOf(164f, 130f, 130f, 130f, 100f)
        labels.forEachIndexed { index, value ->
            val y = 263.001f - index * 54f
            extra.draw(phase, "HallLayer", "Canvas/Layer/menu/button1_$index/Background", "sliced-sprite", 773.998f, y, 180.9f, 50f, "box3")
            extra.draw(phase, "HallLayer", "Canvas/Layer/menu/button1_$index/Background/Label", "label", labelX[index], y + 8f, labelW[index], 40f, opacity = 1f, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), text = value)
        }
        return base + extra.jsonl()
    }

    private fun drawBattleViewOverlay() {
        val scale=.86f
        batch.color=Color(1f,1f,1f,.667f); batch.draw(dim,1008.372f*scale,320f*scale,480f*scale,480f*scale)
        batch.color=Color.WHITE
        battleViewMap?.let { batch.draw(it,1008.372f*scale,320f*scale,480f*scale,480f*scale) }
        battleView.markers().forEachIndexed { index, _ ->
            outerPatch?.draw(batch,(1104.372f+index*24f)*scale,680f*scale,24f*scale,24f*scale)
            font.color=Color.BLACK;font.data.setScale(.55f);font.draw(batch,(index+1).toString(),(1110.644f+index*24f)*scale,699f*scale);font.data.setScale(1f)
        }
        outerPatch?.draw(batch,1008.372f*scale,320f*scale,480f*scale,480f*scale)
    }

    private fun battleViewRenderEventLog(): String {
        val log=RenderEventLog();val phase="hall-battle-view-stable"
        fun d(layer:String,path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,opacity:Float=1f,text:String="",blend:Any=listOf(770,771),visible:Boolean=true)=
            log.draw(phase,layer,path,type,x,y,w,h,asset,opacity,blend,visible,text)
        d("HallLayer","Canvas/Layer/map","sprite",0f,0f,1488.372f,800f,"assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>")
        d("BattleViewLayer","Canvas/Layer/bg","sprite",1008.372f,320f,480f,480f,"default_sprite_splash",.667f)
        d("BattleViewLayer","Canvas/Layer/bg/map/view/content/map1","sprite",1008.372f,320f,480f,480f,"assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#HM_1-1")
        repeat(4){i->val x=1104.372f+i*24f;val opacity=if(i==1)1f else .502f
            d("BattleViewLayer","Canvas/Layer/bg/map/view/content/map1/box6","sliced-sprite",x,680f,24f,24f,"Mark_47-1",opacity)
            d("BattleViewLayer","Canvas/Layer/bg/map/view/content/map1/box6/box3","sprite",x,680f,24f,24f,"box3")
            d("BattleViewLayer","Canvas/Layer/bg/map/view/content/map1/box6/label","label",x+6.272f,681.13f,10.01f,22.68f,text=(i+1).toString(),blend=listOf("SRC_ALPHA","ONE_MINUS_SRC_ALPHA"))
        }
        d("BattleViewLayer","Canvas/Layer/bg/box3","sliced-sprite",1008.372f,320f,480f,480f,"box5")
        return log.jsonl()
    }

    private fun drawRoster() {
        available.forEachIndexed { index, id ->
            val profile = data.unitProfile(id) ?: return@forEachIndexed
            val cx = (233.686f + index % 6 * 133f) * .86f
            val cy = (682.202f - index / 6 * 144f) * .86f
            avatar(id)?.let { texture ->
                batch.color = if (id in selection) Color(.5f, .5f, .5f, 1f) else Color.WHITE
                batch.draw(texture, cx - 49.536f, cy - 49.536f, 99.072f, 99.072f, 0, 301, 48, 48, false, false)
            }
            batch.color = Color.WHITE
            rosterFont.color = Color.WHITE
            rosterFont.draw(batch, "Lv.", (168.744f + index % 6 * 133f) * .86f, (669.555f - index / 6 * 144f) * .86f + 14f)
            right(rosterFont, level(id).toString(), (299.048f + index % 6 * 133f) * .86f, (667.398f - index / 6 * 144f) * .86f + 14f)
            rosterNameFont.color = Color.WHITE
            centered(rosterNameFont, displayName(profile), cx, (625.287f - index / 6 * 144f) * .86f + 13f)
        }
        font.color = Color.BLACK
        font.draw(batch, "출진 무장 - ${selection.size}/${limit.maximum}", 144f, 315f)
        drawButton(657.56f, 278.64f, "부대 속성", true, 172f)
    }

    private fun drawSelectedSlots() {
        repeat(limit.maximum) { index ->
            // bg/bg/scrollview/content/node is centred at source x=217.336.
            // Its frame child is 2x; the old code accidentally used the
            // unscaled child half-width as the node origin and shifted the
            // whole selected roster to the right.
            val centerX = (217.336f + index * 100f) * .86f
            val frame = when {
                index < limit.requiredUnitIds.size -> slotRequired
                index < limit.minimum -> slotMinimum
                else -> slotOpen
            }
            val frameHeight = if (index < limit.requiredUnitIds.size) 51.6f else 55.04f
            frame?.let { batch.draw(it, centerX - 43f, 194.915f - frameHeight / 2f, 86f, frameHeight) }
            selection.getOrNull(index)?.let { id ->
                avatar(id)?.let { texture ->
                    batch.color = if (id in limit.requiredUnitIds) Color(.5f, .5f, .5f, 1f) else Color.WHITE
                    batch.draw(texture, centerX - 41.28f, 193.516f, 82.56f, 82.56f, 0, 301, 48, 48, false, false)
                    batch.color = Color.WHITE
                }
            }
        }
    }

    private fun drawUnitInfo(id: Int?) {
        val profile = id?.let(data::unitProfile) ?: return
        val level = level(id)
        val posts = campaign.unitAttribute(id, ATTR_POSTS, profile.posts)
        val battle = data.battleProfile(id, level - 1, posts) ?: return
        font.color = Color.BLACK
        font.draw(batch, displayName(profile), 842.228f, 630f)
        right(font, battle.arm.name, 1130.73f, 632f)
        centered(font, "무장 정보", 927f, 590f)
        face(id)?.let { batch.draw(it, 848.51f, 372.86f, 140.35f, 175.44f) }
        val values = listOf(
            "Lv" to level, "EXP" to campaign.unitAttribute(id, ATTR_EXP, 0),
            "HP:" to battle.maxHitPoints, "MP:" to battle.maxMagicPoints,
        )
        values.forEachIndexed { index, (label, value) ->
            val y = (613.15f - index * 51f) * .86f + 13f
            font.draw(batch, label, listOf(1026.3f, 993.6f, 1004.8f, 1001.4f)[index], y)
            right(font, value.toString(), 1119.88f, y)
        }
        val traits = listOf(
            "무력" to profile.attack * 2, "민첩성" to profile.critical * 2,
            "지력" to profile.spirit * 2, "운기" to profile.morale * 2,
            "지휘" to profile.defense * 2, "" to 0,
            "공격" to (battle.attack + (data.defaultEquipmentBonus(posts, level)?.attack ?: 0)),
            "방어" to (battle.defense + (data.defaultEquipmentBonus(posts, level)?.defense ?: 0)),
            "정신" to battle.spirit, "폭발" to battle.critical,
            "사기" to battle.morale, "이동" to battle.movement,
        )
        traits.chunked(2).forEachIndexed { row, pair ->
            val y = (411.15f - row * 50.5f) * .86f + 13f
            pair.forEachIndexed pairLoop@{ column, (label, value) ->
                if (label.isEmpty()) return@pairLoop
                font.draw(batch, label, if (column == 0) 845.54f else 993.62f, y)
                right(font, value.toString(), if (column == 0) 981.42f else 1119.88f, y)
            }
        }
    }

    private fun drawButton(x: Float, y: Float, text: String, enabled: Boolean, width: Float = 86f) {
        batch.color = if (enabled) Color.WHITE else Color(.55f, .55f, .55f, 1f)
        outerPatch?.draw(batch, x, y, width, 43f)
        batch.color = Color.WHITE
        font.color = if (enabled) Color.BLACK else Color.DARK_GRAY
        centered(font, text, x + width / 2f, y + 31f)
    }

    private fun drawTiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
        val tileWidth = texture.width * .86f
        val tileHeight = texture.height * .86f
        var dy = 0f
        while (dy < height - .01f) {
            val drawnHeight = minOf(tileHeight, height - dy)
            val sourceHeight = (drawnHeight / .86f).toInt().coerceIn(1, texture.height)
            var dx = 0f
            while (dx < width - .01f) {
                val drawnWidth = minOf(tileWidth, width - dx)
                val sourceWidth = (drawnWidth / .86f).toInt().coerceIn(1, texture.width)
                batch.draw(texture, x + dx, y + dy, drawnWidth, drawnHeight, 0, 0, sourceWidth, sourceHeight, false, false)
                dx += tileWidth
            }
            dy += tileHeight
        }
    }

    /** Actual submitted preparation-screen geometry for source frame-log comparison. */
    fun compositionTrace(): String {
        fun f(value: Float) = "%.3f".format(java.util.Locale.US, value)
        fun rect(x: Float, y: Float, width: Float, height: Float) =
            "[${f(x)},${f(y)},${f(width)},${f(height)}]"
        val rosterEntries = available.mapIndexed { index, id ->
            val cx = (233.686f + index % 6 * 133f) * .86f
            val cy = (682.202f - index / 6 * 144f) * .86f
            "{\"id\":$id,\"selected\":${id in selection},\"avatarRect\":${rect(cx - 49.536f, cy - 49.536f, 99.072f, 99.072f)}}"
        }.joinToString(",")
        val slots = (0 until limit.maximum).joinToString(",") { index ->
            val centerX = (217.336f + index * 100f) * .86f
            val kind = when {
                index < limit.requiredUnitIds.size -> "required"
                index < limit.minimum -> "minimum"
                else -> "open"
            }
            val id = selection.getOrNull(index) ?: -1
            val frameHeight = if (index < limit.requiredUnitIds.size) 51.6f else 55.04f
            "{\"index\":$index,\"kind\":\"$kind\",\"id\":$id," +
                "\"frameRect\":${rect(centerX - 43f, 194.915f - frameHeight / 2f, 86f, frameHeight)}}"
        }
        val selectedId = available.getOrNull(cursor) ?: selection.firstOrNull() ?: -1
        return "{\"state\":\"start-battle\",\"viewport\":[1280,688],\"backgroundId\":$backgroundId," +
            "\"outerRect\":${rect(138.061f,43f,1003.878f,602f)}," +
            "\"rosterClipRect\":${rect(143.78f,323.79f,688f,312.18f)}," +
            "\"selectedPanelRect\":${rect(143.91f,52.57f,688f,220.16f)}," +
            "\"infoPanelRect\":${rect(834.575f,96.793f,298.85f,479.966f)}," +
            "\"infoTitleRect\":${rect(857.565f,557.487f,139.062f,34.658f)}," +
            "\"selectedUnitId\":$selectedId,\"faceRect\":${rect(848.51f,372.86f,140.35f,175.44f)}," +
            "\"confirmRect\":${rect(954.76f,49.88f,86f,43f)},\"cancelRect\":${rect(1049.36f,49.88f,86f,43f)}," +
            "\"roster\":[$rosterEntries],\"slots\":[$slots]}"
    }

    private fun centered(usedFont: BitmapFont, text: String, centerX: Float, baselineY: Float) {
        layout.setText(usedFont, text)
        usedFont.draw(batch, text, centerX - layout.width / 2f, baselineY)
    }

    private fun right(usedFont: BitmapFont, text: String, rightX: Float, baselineY: Float) {
        layout.setText(usedFont, text)
        usedFont.draw(batch, text, rightX - layout.width, baselineY)
    }

    private fun level(id: Int) = campaign.unitAttribute(id, ATTR_LEVEL, data.unitProfile(id)?.level ?: 1)
    private fun displayName(profile: OriginalGameData.UnitProfile): String =
        if (!profile.famous) profile.name.trim().replace(Regex("\\s*\\d+$"), "") else profile.name.trim()

    private fun avatar(id: Int): Texture? {
        avatarTextures[id]?.let { return it }
        val profile = data.unitProfile(id) ?: return null
        val posts = campaign.unitAttribute(id, ATTR_POSTS, profile.posts)
        val armId = if (posts < 60) posts / 3 else posts - 40
        val avatar = BattleAvatarResolver.resolve(data, id, posts, armId, Faction.PLAYER) ?: return null
        val handle = Gdx.files.internal("maps/units/mov2/$avatar.png").takeIf { it.exists() }
            ?: Gdx.files.internal("maps/units/mov/$avatar.png")
        return handle.takeIf { it.exists() }?.let(::Texture)?.also {
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); avatarTextures[id] = it
        }
    }

    private fun face(id: Int): Texture? {
        faceTextures[id]?.let { return it }
        val raw = data.unitProfile(id)?.face ?: return null
        val headId = if (id == 0 && raw <= 3) raw + 1 else raw + 8
        return Gdx.files.internal("maps/heads/$headId.png").takeIf { it.exists() }?.let(::Texture)?.also {
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); faceTextures[id] = it
        }
    }

    private fun moveCursor(offset: Int) { if (available.isNotEmpty()) cursor = Math.floorMod(cursor + offset, available.size) }
    private fun toggle(id: Int?) {
        id ?: return
        if (id in limit.requiredUnitIds) return
        if (id in selection) selection.remove(id) else if (selection.size < limit.maximum) selection += id
    }
    private fun startBattle() { if (campaign.setBattleRoster(selection, limit)) game.showBattleSandbox(sourceScenario, returnScenario) }

    /** Read-only state for production E2E; all changes still use this screen's InputProcessor. */
    internal fun campaignE2eState() = CampaignE2eBattlePreparationState(
        returnScenario = returnScenario,
        sourceScenario = sourceScenario,
        campaignStage = game.campaignStage(),
        selectedCount = selection.size,
        minimum = limit.minimum,
        maximum = limit.maximum,
        cursorSelected = available.getOrNull(cursor) in selection,
        canStart = selection.size in limit.minimum..limit.maximum,
    )

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    override fun dispose() {
        Gdx.input.inputProcessor = null
        font.dispose(); rosterFont.dispose(); rosterNameFont.dispose(); batch.dispose()
        (textures + avatarTextures.values + faceTextures.values).distinct().forEach(Texture::dispose)
    }
}
