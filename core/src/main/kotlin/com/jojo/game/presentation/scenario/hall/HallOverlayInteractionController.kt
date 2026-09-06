// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallInfoInputKind: 거점 Info 입력 Kind이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal enum class HallInfoInputKind { FORCES, PROPERTY, TERRAIN, TREASURE, HELPER }
/**
 * `HallInfoInputIntent`: 관련 상태와 동작을 묶는 interface다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal sealed interface HallInfoInputIntent {
    /**
     * `None`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object None : HallInfoInputIntent
    /**
     * `Close`: 관련 상태와 동작을 묶는 object다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data object Close : HallInfoInputIntent
    /**
     * `OpenForcesRow`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class OpenForcesRow(val row: Int) : HallInfoInputIntent
    /**
     * `SelectPropertyTab`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class SelectPropertyTab(val tab: Int) : HallInfoInputIntent
    /**
     * `OpenPropertyRow`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class OpenPropertyRow(val row: Int) : HallInfoInputIntent
    /**
     * `SelectTerrainTab`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class SelectTerrainTab(val index: Int) : HallInfoInputIntent
    /**
     * `OpenTreasureRow`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class OpenTreasureRow(val row: Int) : HallInfoInputIntent
}
/**
 * `HallLayerTapIntent`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class HallLayerTapIntent { NONE, PRIMARY, SECONDARY, CLOSE, CANCEL }

/**
 * `HallOverlayInteractionController`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class HallOverlayInteractionController {
    /**
     * `exclusiveTap`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun exclusiveTap(x: Float, y: Float): HallLayerTapIntent {
        val sx=x/.86f; val sy=y/.86f
        return when { sx in 147.282f..347.282f && sy in 54.533f..108.533f -> HallLayerTapIntent.PRIMARY; sx in 354.241f..554.241f && sy in 54.533f..108.533f -> HallLayerTapIntent.SECONDARY; sx in 1141.864f..1341.864f && sy in 54.533f..108.533f || sx !in 136.186f..1352.186f || sy !in 47f..753f -> HallLayerTapIntent.CLOSE; else -> HallLayerTapIntent.NONE }
    }
    /**
     * `magicTap`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun magicTap(x: Float, y: Float): HallLayerTapIntent { val sx=x/.86f; val sy=y/.86f; return if (sx in 874.764f..1022.364f && sy in 144.022f..194.022f || sx !in 452.686f..1035.686f || sy !in 130f..670f) HallLayerTapIntent.CLOSE else HallLayerTapIntent.NONE }
    /**
     * `unitInfoTap`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun unitInfoTap(x: Float,y:Float): HallLayerTapIntent = when { x in 505f..655f && y in 36f..83f -> HallLayerTapIntent.PRIMARY; x !in 169f..1162f || y !in 10f..678f -> HallLayerTapIntent.CLOSE; else -> HallLayerTapIntent.NONE }
    /**
     * `featsTap`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun featsTap(x: Float,y:Float,helpOpen:Boolean): HallLayerTapIntent { val sx=x/.86f; val sy=y/.86f; return when { helpOpen && sx in 654.186f..834.186f && sy in 271.285f..321.285f -> HallLayerTapIntent.PRIMARY; helpOpen -> HallLayerTapIntent.NONE; sx in 1059.386f..1206.986f && sy in 96f..152f -> HallLayerTapIntent.CLOSE; sx in 904.386f..1051.986f && sy in 96f..152f -> HallLayerTapIntent.SECONDARY; sx !in 267.686f..1220.686f || sy !in 83.5f..716.5f -> HallLayerTapIntent.CANCEL; else -> HallLayerTapIntent.NONE } }
    /**
     * `infoTap`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun infoTap(kind: HallInfoInputKind, x: Float, y: Float): HallInfoInputIntent {
        if (closeHit(kind, x, y)) return HallInfoInputIntent.Close
        return when (kind) {
            HallInfoInputKind.FORCES -> rowAt(x, y, 147f..1134f, 469.63f, 53.32f, 7)?.let(HallInfoInputIntent::OpenForcesRow) ?: HallInfoInputIntent.None
            HallInfoInputKind.PROPERTY -> when {
                y in 40f..100f -> ((x - 226f) / 150f).toInt().takeIf { it in 0..3 }?.let(HallInfoInputIntent::SelectPropertyTab) ?: HallInfoInputIntent.None
                else -> rowAt(x, y, 217f..1062f, 481.58f, 67.08f, 7, 65.36f)?.let(HallInfoInputIntent::OpenPropertyRow) ?: HallInfoInputIntent.None
            }
            HallInfoInputKind.TERRAIN -> when { y !in 91f..145f -> HallInfoInputIntent.None; x in 246f..411f -> HallInfoInputIntent.SelectTerrainTab(0); x in 420f..620f -> HallInfoInputIntent.SelectTerrainTab(1); else -> HallInfoInputIntent.None }
            HallInfoInputKind.TREASURE -> (0 until 6).firstOrNull { index -> val cx=232.10f+index%2*410.22f; val cy=413.23f-index/2*165.98f; x in cx..(cx+405.06f)&&y in cy..(cy+163.40f) }?.let(HallInfoInputIntent::OpenTreasureRow) ?: HallInfoInputIntent.None
            HallInfoInputKind.HELPER -> HallInfoInputIntent.None
        }
    }
    /**
     * `closeHit`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun closeHit(kind: HallInfoInputKind,x:Float,y:Float):Boolean=when(kind){HallInfoInputKind.FORCES->x in 973.5f..1128.5f&&y in 73f..125f;HallInfoInputKind.PROPERTY->x in 932f..1058f&&y in 45f..98f;HallInfoInputKind.TERRAIN->x in 979f..1105f&&y in 91f..145f;HallInfoInputKind.TREASURE->x in 912f..1048f&&y in 77f..132f;HallInfoInputKind.HELPER->x in 1008f..1144f&&y in 27f..82f}
    /**
     * `rowAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun rowAt(x:Float,y:Float,xRange:ClosedFloatingPointRange<Float>,firstY:Float,step:Float,count:Int,height:Float=step):Int?=if(x !in xRange)null else (0 until count).firstOrNull { index -> y in (firstY-index*step)..(firstY-index*step+height) }
}
