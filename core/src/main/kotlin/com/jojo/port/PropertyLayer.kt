package com.jojo.port

/**
 * Behavioural port of `ui/PropertyLayer.js`.
 *
 * `itemType` is ITEM_EQUIP_TYPE (not the four UI-tab index): 0..19 are
 * weapons, 20..25 armour, 26..45 consumables and values above that are
 * auxiliary equipment.  Keeping that distinction here is important because
 * the original Item.type() derives its UI category from this raw value.
 */
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

    /** `onCreate` ends with the original `_currentSel(0)`. */
    fun onCreate(): List<Row> { attached = true; selected = Tab.WEAPON; scrollRow = 0; return rows() }
    fun select(tab: Tab): List<Row> {
        selected = tab
        val result = rows()
        // ScrollView clamps its offset when the source replaces content rows.
        scrollRow = scrollRow.coerceIn(0, (result.size - 1).coerceAtLeast(0))
        return result
    }
    /** Source toggle listener only reacts to TOUCH_END (2). */
    fun onTabTouch(tab: Tab, event: Int): List<Row> = if (event == 2) select(tab) else rows()
    /** Source close-button listener only removes on TOUCH_END (2). */
    fun onCancel(event: Int) { if (event == 2) attached = false }
    /** ScrollView owns the physical scrolling; expose its clamped row state. */
    fun onScroll(row: Int): Int { scrollRow = row.coerceIn(0, (rows().size - 1).coerceAtLeast(0)); return scrollRow }
    /** `_onEquipOnClick` adds ItemLayer only for TOUCH_END. */
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
        // PropertyLayer's switch assigns lv() to both WEAPONS and ARMOR;
        // only AUXILIARY keeps the initial "---" value.
        val level = if (item.isAuxiliary()) "---" else item.level.toString()
        val nameProperty = item.typeName ?: when { item.isAuxiliary() -> "보조"; item.isArmor() -> "방어구"; else -> "무기" }
        Row(item, inventory[item.id] ?: 0, listOf(item.name, owner, nameProperty, level, if (item.exp >= item.expLimit) "MAX" else item.exp.toString()))
    }
    private fun Item.isWeapon() = itemType in 0..19
    private fun Item.isArmor() = itemType in 20..25
    private fun Item.isProperty() = itemType in 26..45
    private fun Item.isAuxiliary() = !isWeapon() && !isArmor() && !isProperty()
    companion object {
        fun fromOriginal(data: OriginalGameData, inventory: Map<Int, Int>): PropertyLayer = PropertyLayer(
            // Model.itemIter feeds only the player's item-store entries to
            // ui/PropertyLayer.  Keep the class itself generic for its
            // direct source-trace contract; narrow the live battle model at
            // this boundary instead of displaying every catalogue entry.
            data.allEquipmentProfiles().filter { (inventory[it.id] ?: 0) > 0 }
                .map { Item(it.id, it.name, it.itemType, it.icon) }, inventory
        )
    }
}
