// Shared
package com.jojo.game.presentation.shared.overlay
import com.jojo.game.infrastructure.data.GameDataCatalog

/** PropertyLayer: ui/PropertyLayer.js의 동작을 구현한다. itemType은 네 UI 탭 인덱스가 아닌 ITEM_EQUIP_TYPE 원시 값이며, 원본 Item.type()은 이 값에서 화면 분류를 파생한다. */

class PropertyLayer(private val items: List<Item>, private val inventory: Map<Int, Int>) {

    data class Item(
        val id: Int, val name: String, val itemType: Int, val icon: Int,
        val level: Int = 0, val owner: String? = null, val exp: Int = 0,
        val expLimit: Int = 0, val typeName: String? = null,
    )


    enum class Tab { WEAPON, ARMOR, AUXILIARY, PROPERTY }


    data class Row(val item: Item, val quantity: Int, val labels: List<String>)

    var selected = Tab.WEAPON; private set
    var attached = true; private set
    var scrollRow = 0; private set
    private var propertyPanelInitialized = false

    /** 생성 직후 첫 번째 항목을 선택한다. */
    fun onCreate(): List<Row> {
        attached = true; selected = Tab.WEAPON; scrollRow = 0; return rows()
    }


    fun select(tab: Tab): List<Row> {
        selected = tab
        val result = rows()
        // 원본이 내용 행을 교체하면 스크롤 뷰가 오프셋을 제한한다.
        scrollRow = scrollRow.coerceIn(0, (result.size - 1).coerceAtLeast(0))
        return result
    }

    /** 전환 입력은 터치 종료 시에만 처리한다. */
    fun onTabTouch(tab: Tab, event: Int): List<Row> = if (event == 2) select(tab) else rows()

    /** 닫기 버튼은 터치 종료 시에만 화면을 제거한다. */
    fun onCancel(event: Int) {
        if (event == 2) attached = false
    }

    /** 스크롤 범위가 제한된 행 상태를 제공한다. */
    fun onScroll(row: Int): Int {
        scrollRow = row.coerceIn(0, (rows().size - 1).coerceAtLeast(0)); return scrollRow
    }

    /** 장비 선택 화면은 터치 종료 시에만 연다. */
    fun onRowTouch(index: Int, event: Int): Int? = if (event == 2) rows().getOrNull(index)?.item?.id else null


    fun panelInitialized(): Boolean = propertyPanelInitialized


    fun rows(): List<Row> = when (selected) {
        Tab.PROPERTY -> {
            propertyPanelInitialized = true
            items.filter { it.isProperty() && (inventory[it.id] ?: 0) > 0 }.sortedBy { it.id }.map { item ->
                Row(item, inventory[item.id] ?: 0, listOf(item.name, (inventory[item.id] ?: 0).toString()))
            }
        }

        Tab.WEAPON -> equipment { it.isWeapon() }
        Tab.ARMOR -> equipment { it.isArmor() }
        Tab.AUXILIARY -> equipment { it.isAuxiliary() }
    }

    private fun equipment(filter: (Item) -> Boolean) = items.filter(filter).sortedBy { it.id }.map { item ->
        val owner = item.owner ?: "창고"
        // 무기와 방어구에는 lv()를 적용하고 보조 장비만 초기 "---" 값을 유지한다.
        val level = if (item.isAuxiliary()) "---" else item.level.toString()
        val nameProperty = item.typeName ?: when {
            item.isAuxiliary() -> "보조"; item.isArmor() -> "방어구"; else -> "무기"
        }
        Row(
            item,
            inventory[item.id] ?: 0,
            listOf(item.name, owner, nameProperty, level, if (item.exp >= item.expLimit) "MAX" else item.exp.toString())
        )
    }

    private fun Item.isWeapon() = itemType in 0..19
    private fun Item.isArmor() = itemType in 20..25
    private fun Item.isProperty() = itemType in 26..45
    private fun Item.isAuxiliary() = !isWeapon() && !isArmor() && !isProperty()

    companion object {

        fun fromCatalog(data: GameDataCatalog, inventory: Map<Int, Int>): PropertyLayer = PropertyLayer(
            // 아이템 순회는 플레이어 저장소 항목만 상세 화면에 전달한다. 원본 추적
            // 계약을 위해 클래스는 일반화하고, 이 경계에서만 전투 모델을 제한한다.
            data.allEquipmentProfiles().filter { (inventory[it.id] ?: 0) > 0 }
                .map { Item(it.id, it.name, it.itemType, it.icon) }, inventory
        )
    }
}
