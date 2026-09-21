// Battle
package com.jojo.game.presentation.battle.render

import com.jojo.game.infrastructure.data.GameDataCatalog

/**
 * `UsePropertyDetailRenderContract`: 전투 중 아이템 상세(`use-property-detail`) 화면의
 * "장착 가능한 부대" 표와 세 머리띠를 그리기와 증거가 **같은 숫자**로 읽는 계약이다.
 *
 * 이 계약이 생기기 전에는 표가 증거 기록기에만 있었다. 기록기는 39칸을 적는데 화면에는
 * 아무것도 없었으므로, 파리티 게이트는 화면에 없는 것을 그린다고 주장하며 초록이었다.
 * 그리기와 증거가 한 곳을 읽게 만들어 그 간극을 닫는다.
 *
 * 값의 출처는 상수마다 적는다. 원본 근거는 두 갈래다.
 * - 배치: 프리팹 `ItemLayer`의 행 카드/라벨 노드 기하.
 * - 내용과 색: `recovered-js/modules/ui/ItemLayer.js:122-131`.
 */
internal object UsePropertyDetailRenderContract {
    /** 머리띠: 문구 뒤에 깔리는 `bg1` 스프라이트 한 장의 노드 경로와 사각형이다. */
    data class Headband(val nodePath: String, val x: Float, val y: Float, val width: Float, val height: Float)

    /** 머리띠 자산: 세 장 모두 같은 `bg1` 스프라이트 프레임을 쓴다(원본 캡처의 `assetFrameId`). */
    const val HEADBAND_ASSET = "bg1"

    /** "효과" 머리띠: 왼쪽 아래 효과 상자(`bg1/bg1`)의 제목 배경이다. 좌표는 원본 캡처 값이다. */
    val EFFECT_HEADBAND = Headband("Canvas/Layer/bg1/bg1/bg1", 470.286f, 447.7f, 83.8f, 40f)

    /** "설명" 머리띠: 오른쪽 아래 설명 상자(`bg1/bg2`)의 제목 배경이다. */
    val INTRO_HEADBAND = Headband("Canvas/Layer/bg1/bg2/bg1", 943.336f, 369.55f, 89.7f, 40.9f)

    /** "장착 가능한 부대입니다." 머리띠: 오른쪽 위 표 상자(`bg1/bg3`)의 제목 배경이다. */
    val POSTS_HEADBAND = Headband("Canvas/Layer/bg1/bg3/bg1", 871.686f, 664.273f, 245f, 45f)

    /** 표 행 카드의 노드 경로: 13개 행이 모두 같은 경로로 기록된다(프리팹 사본이 한 이름을 공유한다). */
    const val ROW_NODE_PATH = "Canvas/Layer/bg1/bg3/scrollview/view/content/item"

    /** 행 카드 왼쪽 끝: 표 상자 `bg1/bg3`(x=770.186) 안쪽으로 2 들어간 자리다. */
    const val ROW_X = 772.186f

    /** 첫 행 카드의 아래쪽 y: 원본 캡처의 0번 행 값이다. 아래로 갈수록 [ROW_STEP]만큼 내려간다. */
    const val ROW_TOP_Y = 609.55f

    /** 행 카드 폭: 프리팹 444. */
    const val ROW_WIDTH = 444f

    /** 행 카드 높이: 프리팹 50. 칸 사이 간격 2는 [ROW_STEP]과의 차이로 생긴다. */
    const val ROW_HEIGHT = 50f

    /** 행 세로 간격: 프리팹 52(카드 50 + 틈 2). */
    const val ROW_STEP = 52f

    /** 열 수: 한 행이 승급 3단계를 나란히 적는다(`ItemLayer.js:129`의 `k %= 3`). */
    const val COLUMNS = 3

    /** 행 카드의 가로 중심: 열 중심은 모두 여기서 잰다. */
    const val ROW_CENTER_X = ROW_X + ROW_WIDTH / 2f

    /** 열 중심의 카드 중심 기준 오프셋: 프리팹 -143 / 0 / +144. 좌우가 1 다른 것도 원본 그대로다. */
    val COLUMN_OFFSETS = listOf(-143f, 0f, 144f)

    /** 라벨 노드의 카드 바닥 기준 세로 오프셋: 프리팹 4.84. */
    const val LABEL_OFFSET_Y = 4.84f

    /** 라벨 노드 높이: 프리팹 40.32. */
    const val LABEL_HEIGHT = 40.32f

    /** 짝수 행 배경 프레임: 원본 캡처의 자산 UUID 그대로다. */
    const val ROW_EVEN_ASSET = "885a69b4-08ed-4c78-8896-ffb04eb2bd20"

    /** 홀수 행 배경 프레임: 원본 캡처의 `bg2`. */
    const val ROW_ODD_ASSET = "bg2"

    /** 장착 가능한 직위의 글자색: `ItemLayer.js:122`의 `cc.color(0,0,0)`. */
    const val EQUIPPABLE_COLOR = "#000000"

    /** 장착 불가 직위의 글자색: `ItemLayer.js:122`의 `cc.color(80,80,80)`. */
    const val BLOCKED_COLOR = "#505050"

    /** 글자 한 자의 폭: 원본 캡처의 라벨 폭을 글자 수로 나눈 값(27.68)이다. */
    private const val GLYPH_WIDTH = 27.68f

    /** 공백 한 칸의 폭: 같은 캡처에서 얻은 값(8.89)이다. 한 자 폭과 다르므로 따로 센다. */
    private const val SPACE_WIDTH = 8.89f

    /**
     * 포트 글꼴 배율: 화면의 다른 라벨은 40픽셀 글꼴을 26픽셀 기준 글꼴에 맞춰 `40/26`으로 그린다.
     * 이 표의 라벨은 한 자 폭이 27.68로, 머리띠 라벨(34.6)의 0.8배다 → 32픽셀 글꼴에 해당한다.
     */
    const val LABEL_FONT_SCALE = 32f / 26f

    /**
     * 포트 글자 기준선 보정 비율: 원본 기록의 y는 노드 바닥이고 LibGDX `font.draw`의 y는 글줄 위다.
     * 이 화면의 다른 라벨들이 이미 쓰고 있는 보정(높이 50.4에 42.2~42.5)에서 얻은 0.84다.
     */
    private const val LABEL_DRAW_RISE = 0.84f

    /** 행 수: 표 길이에서 온다. 39는 상수가 아니라 `posts` 표 13×3의 결과다. */
    fun rowCount(nameCount: Int): Int = (nameCount + COLUMNS - 1) / COLUMNS

    /** 행 카드의 아래쪽 y. */
    fun rowY(row: Int): Float = ROW_TOP_Y - row * ROW_STEP

    /** 행 배경 프레임: 짝수 행과 홀수 행이 번갈아 나온다. */
    fun rowAsset(row: Int): String = if (row % 2 == 0) ROW_EVEN_ASSET else ROW_ODD_ASSET

    /** 열 중심 x. */
    fun columnCenterX(column: Int): Float = ROW_CENTER_X + COLUMN_OFFSETS[column]

    /** 글자 폭: 공백과 한 글자의 원본 폭 규칙으로 직위명 라벨의 폭을 잰다. */
    fun measuredWidth(value: String): Float =
        value.count { it != ' ' } * GLYPH_WIDTH + value.count { it == ' ' } * SPACE_WIDTH

    /** 라벨 왼쪽 x: 잰 폭을 열 중심에 가운데 맞춘다. */
    fun labelX(column: Int, value: String): Float = columnCenterX(column) - measuredWidth(value) / 2f

    /** 라벨 노드 바닥 y. */
    fun labelY(row: Int): Float = rowY(row) + LABEL_OFFSET_Y

    /** 포트 글자 기준선 y: 라벨 노드 바닥에서 [LABEL_DRAW_RISE]만큼 올린 자리다. */
    fun labelDrawY(row: Int): Float = labelY(row) + LABEL_HEIGHT * LABEL_DRAW_RISE

    /** 아이템 종류: 원본 `ITEM_TYPE`과 같은 번호다. 포트는 `GameDataCatalog.equipmentCategory`가 준다. */
    const val CATEGORY_WEAPONS = 0
    /** 원본 `ITEM_TYPE.ARMOR`. */
    const val CATEGORY_ARMOR = 1
    /** 원본 `ITEM_TYPE.AUXILIARY`. */
    const val CATEGORY_AUXILIARY = 2
    /** 원본 `ITEM_TYPE.PROPERTY`. 전투 중 아이템 상세가 여는 소지품이 이 종류다. */
    const val CATEGORY_PROPERTY = 3

    /** 직위 이름: `posts` 표를 행·열 순서대로 훑는다(`ItemLayer.js:124-130`의 `x++`). */
    fun postsIndex(row: Int, column: Int): Int = row * COLUMNS + column

    /** 글자색: 장착 가능이면 검정, 아니면 회색. 두 가지뿐이다(`ItemLayer.js:126`). */
    fun labelColor(itemCategory: Int, postsId: Int): String =
        if (postsCanEquip(itemCategory, postsId)) EQUIPPABLE_COLOR else BLOCKED_COLOR

    /**
     * `postsCanEquip` 대응: 아이템 한 개를 직위 [postsId]가 장착할 수 있는지 답한다.
     * 원본은 `recovered-js/modules/game-data/Item.js:282-305`이며 `switch (this._type)`의
     * 갈래는 셋뿐이다. 어느 갈래에도 걸리지 않는 타입(PROPERTY 등)은 `e = !1` 그대로
     * 돌아가므로 **언제나 false**다. 소지품 상세 화면이 39칸을 모두 회색으로 그리는 이유다.
     *
     * [itemCategory]는 포트의 `GameDataCatalog.equipmentCategory`가 주는 값으로,
     * 원본 `ITEM_TYPE`과 같은 번호다(0=WEAPONS, 1=ARMOR, 2=AUXILIARY, 3=PROPERTY).
     *
     * 이 인자 두 개짜리 호환 함수에는 아이템 ID와 원본 표가 없으므로 PROPERTY에만
     * 정확하다. 장비는 아래의 카탈로그 입력을 받는 오버로드를 사용한다.
     */
    fun postsCanEquip(itemCategory: Int, postsId: Int): Boolean {
        require(postsId >= 0) { "직위 번호는 0 이상이어야 한다: $postsId" }
        return when (itemCategory) {
            // 이 오버로드에는 해당 아이템과 직위의 장비 표가 없다.
            CATEGORY_WEAPONS, CATEGORY_ARMOR, CATEGORY_AUXILIARY -> false
            // 원본 `switch`에 case가 없는 타입(PROPERTY 등)은 초기값 `!1` 그대로 돌아간다.
            else -> false
        }
    }

    /**
     * 원본 `Item.js:282-305`의 장비 세 갈래. [equippableTypes]는 해당 직위의
     * `posts.EQUIPS` 열, [upgradeArms]는 아이템의 `UPGRADE_ARM`와 `ARMS` 열에서 온다.
     * 실제 테이블 접근은 호출자가 제공하므로 없는 값을 장착 불가로 오인하지 않는다.
     */
    fun postsCanEquip(
        itemCategory: Int,
        postsId: Int,
        itemType: Int,
        equippableTypes: Set<Int>,
        upgradeArms: List<Int>,
    ): Boolean {
        require(postsId >= 0) { "직위 번호는 0 이상이어야 한다: $postsId" }
        return when (itemCategory) {
            CATEGORY_WEAPONS, CATEGORY_ARMOR -> itemType in equippableTypes
            CATEGORY_AUXILIARY -> {
                val arm = if (postsId < 60) postsId / 3 else postsId - 40
                upgradeArms.firstOrNull() == 255 || arm in upgradeArms.takeWhile { it != 255 }
            }
            else -> false
        }
    }

    /** 실제 복호화 테이블을 사용해 아이템과 직위의 장착 가능 여부를 구한다. */
    fun postsCanEquip(catalog: GameDataCatalog, item: GameDataCatalog.EquipmentProfile, postsId: Int): Boolean =
        postsCanEquip(
            catalog.equipmentCategory(item), postsId, item.itemType,
            catalog.postsEquipmentTypes(postsId), catalog.itemUpgradeArms(item.id),
        )

    fun labelColor(catalog: GameDataCatalog, item: GameDataCatalog.EquipmentProfile, postsId: Int): String =
        if (postsCanEquip(catalog, item, postsId)) EQUIPPABLE_COLOR else BLOCKED_COLOR
}
