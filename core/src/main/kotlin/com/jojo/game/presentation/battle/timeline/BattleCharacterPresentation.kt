// Battle
package com.jojo.game.presentation.battle.timeline
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.domain.battle.*

import com.jojo.game.*
import java.util.*
/**
 * `BattleHideType`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class BattleHideType { CHE_LI, BAI_TUI, SI_WANG }
/**
 * `BattleCharacterCamp`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class BattleCharacterCamp(val hpFrame: String) {
    MINE("Mark_5-1"),
    FRIEND("Mark_3-1"),
    ENEMY("Mark_68-1"),
    FAMOUS_ENEMY("Mark_2-1"),
}
/**
 * `BattleCharacterMaterial`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class BattleCharacterMaterial(val sourceId: String) {
    DEFAULT("builtin-2d-sprite (Instance)"),
    OUTLINE("edgeHighlight (Instance)"),
    HIGHLIGHT("hight-light (Instance)"),
    GRAY("gray"),
}


/** BattleCharacterStrictState: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
enum class BattleCharacterStrictState(val route: String) {
    HP_CAMPS_PARTIAL("hp-camps-partial"),
    OUTLINE_HIGHLIGHT("outline-highlight"),
    HIT_IMPACT("hit-impact"),
    CLEANUP("cleanup"),
    DEATH_ACTION("death-action"),
    DEATH_HIDDEN("death-hidden"),
}

/**
 * `BattleCharacterPresentation`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class BattleCharacterPresentation(
    val unitId: String,
    val camp: BattleCharacterCamp,
    val maxHp: Int,
    hp: Int = maxHp,
) {
    /**
     * `HarmLabel`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class HarmLabel(
        /**
         * `value` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Int,
        /**
         * `isHp` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val isHp: Boolean,
        /**
         * `xOffset` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val xOffset: Int = if (isHp) -24 else 24,
        /**
         * `yOffset` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val yOffset: Int = 24,
        /**
         * `width` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val width: Int = 48,
        /**
         * `height` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val height: Int = 24,
        /**
         * `fontSize` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val fontSize: Int = 24,
        /**
         * `bold` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val bold: Boolean = true,
        /**
         * `colorRgb` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val colorRgb: Int = if (isHp) 0xffffff else 0xe0e000,
        /**
         * `outlineRgb` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val outlineRgb: Int = 9_212_044,
        /**
         * `outlineWidth` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val outlineWidth: Int = 1,
        /**
         * `zIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zIndex: Int = 999,
    )

    /**
     * `hp` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var hp: Int = hp.coerceIn(0, maxHp.coerceAtLeast(0))
        private set
    /**
     * `action` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var action: Int = STAND
        private set
    /**
     * `material` (BattleCharacterMaterial): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var material: BattleCharacterMaterial = BattleCharacterMaterial.DEFAULT
        private set
    /**
     * `materialValue` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var materialValue: Float? = null
        private set
    /**
     * `visible` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var visible: Boolean = true
        private set
    /**
     * `infoVisible` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var infoVisible: Boolean = true
        private set
    /**
     * `statusVisible` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var statusVisible: Boolean = true
        private set
    /**
     * `retreatFlag` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var retreatFlag: Boolean = false
        private set
    /**
     * `retreatCount` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var retreatCount: Int = 0
        private set
    /**
     * `harm` (HarmLabel?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var harm: HarmLabel? = null
        private set

    /**
     * `restoreHp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var restoreHp = this.hp
    /**
     * `hideType` (BattleHideType?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var hideType: BattleHideType? = null

    /**
     * `hpProgress` (Float get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val hpProgress: Float get() = if (maxHp <= 0) 0f else hp.toFloat() / maxHp

    /** beginAttack: 전투 단계의 시작 상태를 만들고 필요한 값을 초기화한다. */
    fun beginAttack(delayed: Boolean = false) {
        action = if (delayed) GONG_JI_DELAY else GONG_JI2
        material = BattleCharacterMaterial.OUTLINE
        materialValue = null
    }
    /**
     * `animationMaterialEvent`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun animationMaterialEvent(value: Int) {
        when {
            value == 0 -> {
                material = BattleCharacterMaterial.DEFAULT
                materialValue = null
            }

            value >= 100 -> {
                material = BattleCharacterMaterial.HIGHLIGHT
                materialValue = (value - 100) / 10f
            }

            else -> materialValue = value / 10f
        }
    }


    /**
     * `finishAttack`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun finishAttack() {
        action = STAND
        material = BattleCharacterMaterial.DEFAULT
        materialValue = null
    }
    /**
     * `hitImpact`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hitImpact(amount: Int, isHp: Boolean = true) {
        if (isHp) hp = (hp - kotlin.math.abs(amount)).coerceAtLeast(0)
        action = SHOU_GONG_JI3
        harm = HarmLabel(kotlin.math.abs(amount), isHp)
    }

    /** finishHit: 진행 중인 전투 처리를 완료하고 후속 상태를 반영한다. */
    fun finishHit() {
        harm = null
        action = STAND
    }

    /** beginHide: 전투 단계의 시작 상태를 만들고 필요한 값을 초기화한다. */
    fun beginHide(type: BattleHideType) {
        restoreHp = hp
        hideType = type
        retreatFlag = true
        hp = 0
        infoVisible = false
        statusVisible = false
        action = when (type) {
            BattleHideType.CHE_LI -> CHETUI
            BattleHideType.BAI_TUI -> RETREAT
            BattleHideType.SI_WANG -> DEATH
        }
    }

    /** finishHide: 진행 중인 전투 처리를 완료하고 후속 상태를 반영한다. */
    fun finishHide() {
        val type = requireNotNull(hideType) { "beginHide must precede finishHide" }
        if (type != BattleHideType.CHE_LI) retreatCount++
        visible = false
        hp = restoreHp
        hideType = null
    }

    companion object {
        /**
         * `STAND` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val STAND = 0
        /**
         * `RETREAT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val RETREAT = 23
        /**
         * `DEATH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val DEATH = 24
        /**
         * `GONG_JI2` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val GONG_JI2 = 25
        /**
         * `SHOU_GONG_JI3` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SHOU_GONG_JI3 = 32
        /**
         * `CHETUI` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHETUI = 47
        /**
         * `GONG_JI_DELAY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val GONG_JI_DELAY = 48
    }
}

/**
 * `BattleCharacterDrawEvent`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class BattleCharacterDrawEvent(
    val nodePath: String,
    val drawType: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val assetFrameId: String? = null,
    val sourceRect: List<Int>? = null,
    val flipX: Boolean? = null,
    val flipY: Boolean? = null,
    val text: String? = null,
    val materialId: String = "SpriteBatch/linear",
    val materialValue: Float? = null,
    val opacity: Float = 1f,
    val blend: List<Any> = listOf(770, 771),
    val visible: Boolean = true,
    val colorRgb: Int? = 0xffffff,
    val outlineRgb: Int? = null,
    val outlineWidth: Int? = null,
    val zIndex: Int = 0,
)


/** BattleCharacterStateRenderer: 전투 화면에 필요한 표시 정보와 그리기 규칙을 담당한다. */
object BattleCharacterStateRenderer {
    /**
     * `UNIT_PATH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val UNIT_PATH = "Canvas/Layer/ScrollView/view/content/map/unit"
    /**
     * `commands`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun commands(
        state: BattleCharacterPresentation,
        unitLeft: Float,
        unitBottom: Float,
        avatarFrameId: String,
        barOnTop: Boolean = false,
        avatarWidth: Float = 96f,
        avatarHeight: Float = 96f,
        avatarOffsetX: Float = 0f,
        avatarOffsetY: Float = 0f,
        avatarSourceRect: List<Int>? = null,
        avatarFlipX: Boolean? = null,
        avatarFlipY: Boolean? = null,
    ): List<BattleCharacterDrawEvent> = buildList {
        if (!state.visible) return@buildList
        add(
            BattleCharacterDrawEvent(
                nodePath = "$UNIT_PATH/mask/node",
                drawType = "sprite",
                x = unitLeft + (96f - avatarWidth) / 2f + avatarOffsetX,
                y = unitBottom + (96f - avatarHeight) / 2f + avatarOffsetY,
                width = avatarWidth,
                height = avatarHeight,
                assetFrameId = avatarFrameId,
                sourceRect = avatarSourceRect,
                flipX = avatarFlipX,
                flipY = avatarFlipY,
                materialId = state.material.sourceId,
                materialValue = state.materialValue,
            )
        )
        if (state.infoVisible && state.hpProgress > 0f) {
            add(
                BattleCharacterDrawEvent(
                    nodePath = "$UNIT_PATH/info/bar2/sprite",
                    drawType = "sliced-sprite",
                    x = unitLeft + 4f,
                    y = unitBottom + if (barOnTop) 91f else -1f,
                    width = 88f * state.hpProgress,
                    height = 6f,
                    assetFrameId = state.camp.hpFrame,
                )
            )
        }
        state.harm?.let { label ->
            // 맵 아래의 48×24 로컬 형제 노드는 96×48 드로 쿼드가 된다.
            val x = unitLeft + if (label.isHp) 0f else 96f
            add(
                BattleCharacterDrawEvent(
                    nodePath = "Canvas/Layer/ScrollView/view/content/map/harmNum",
                    drawType = "label",
                    x = x,
                    y = unitBottom + 72f,
                    width = 96f,
                    height = 48f,
                    text = label.value.toString(),
                    materialId = "Label/alpha",
                    colorRgb = label.colorRgb,
                    outlineRgb = label.outlineRgb,
                    outlineWidth = label.outlineWidth,
                    zIndex = label.zIndex,
                )
            )
        }
    }


    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun jsonl(route: BattleCharacterStrictState, events: List<BattleCharacterDrawEvent>): String =
        events.mapIndexed { sequence, event -> event.toJson(sequence, route.route) }
            .joinToString(separator = "\n", postfix = if (events.isEmpty()) "" else "\n")

    /**
     * `BattleCharacterDrawEvent`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun BattleCharacterDrawEvent.toJson(sequence: Int, phase: String): String = "{" +
            "\"sequence\":$sequence,\"frame\":0,\"timestamp\":0,\"phase\":\"battle-character-$phase\"," +
            "\"layer\":\"HallLayer\",\"nodePath\":\"$nodePath\",\"drawType\":\"$drawType\"," +
            "\"x\":${num(x)},\"y\":${num(y)},\"w\":${num(width)},\"h\":${num(height)}," +
            "\"assetFrameId\":${assetFrameId?.let(::quote) ?: "null"},\"opacity\":${num(opacity)}," +
            "\"sourceRect\":${sourceRect?.joinToString(prefix = "[", postfix = "]") ?: "null"}," +
            "\"flipX\":${flipX ?: "null"},\"flipY\":${flipY ?: "null"}," +
            "\"blend\":[${blend.joinToString { if (it is String) quote(it) else it.toString() }}],\"visible\":$visible,\"text\":${
                text?.let(
                    ::quote
                ) ?: "null"
            }," +
            "\"materialId\":${quote(materialId)},\"materialValue\":${materialValue?.let(::num) ?: "null"}," +
            "\"colorRgb\":${colorRgb ?: "null"},\"outlineRgb\":${outlineRgb ?: "null"}," +
            "\"outlineWidth\":${outlineWidth ?: "null"},\"zIndex\":$zIndex}"

    /**
     * `num`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun num(value: Float) = String.format(Locale.US, "%.3f", value)
    /**
     * `quote`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun quote(value: String) = "\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\""
}
