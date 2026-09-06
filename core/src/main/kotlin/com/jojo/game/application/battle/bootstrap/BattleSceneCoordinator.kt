// Battle
package com.jojo.game.application.battle.bootstrap

import com.jojo.game.domain.battle.*

class BattleSceneCoordinator(
    private val factory: Factory,
    private val model: Model,
    private val manager: Manager,
    private val battleLayerResource: Any? = null,
    private val battleInitLayer: Any? = null,
    private val miniMapLayer: Any? = null,
    private val noticeInfoLayer: Any? = null,
) {

    interface BattleScreen {
        fun save(out: MutableMap<String, Any?>)
        fun filterUnits(flag: Int): List<Any?>
    }

    fun interface Model {
        fun save(out: MutableMap<String, Any?>)
    }

    fun interface Manager {
        fun saveGame(index: Int, json: String)
    }


    interface Factory {

        fun addBattleScreen(data: Any?): BattleScreen


        fun addForcesList(mine: List<Any?>, enemy: List<Any?>, flag: Int)
        fun stringify(value: Map<String, Any?>): String
    }


    data class SaveRequest(val index: Int, val onComplete: (() -> Unit)? = null)

    /** Layer: 레이어이며, 화면에 필요한 전투 정보를 만들고 표시한다. */
    enum class Layer(val id: Int) {
        BATTLE_LAYER(1), BATTLE_INIT_LAYER(2), TERRAIN_INFO_LAYER(3), BATTLE_UNIT_INFO_LAYER(4),
        COMMAND_LAYER(5), MINE_UNIT_INFO_LAYER(6), OTHER_UNIT_INFO_LAYER(7), SAY_LAYER(8),
        WIN_CONDITIONS_LAYER(9), ROUND_LAYER(10), MENU_LAYER(14), MAGICK_LIST_LAYER(15),
        FIGHT_LAYER(16), USE_PROPERTY_LAYER(17), REWARD_LAYER(18), WIN_CON_BOX_LAYER(20),
        EDIT_LAYER(22), EDIT_LAYER2(23), MINI_MAP_LAYER(24), NOTICE_INFO_LAYER(25),
        TUO_GUAN_LAYER(26), JI_QI_LAYER(27)
    }

    private var battleLayer: BattleScreen? = null

    /** onCreate: 입력 또는 이벤트를 반영해 전투 상태를 전환한다. */
    fun onCreate(data: Any?) {
        battleLayer = factory.addBattleScreen(data)
    }
    fun getResource(layer: Layer): Any? = when (layer) {
        Layer.BATTLE_LAYER -> battleLayerResource
        Layer.BATTLE_INIT_LAYER -> battleInitLayer
        Layer.MINI_MAP_LAYER -> miniMapLayer
        Layer.NOTICE_INFO_LAYER -> noticeInfoLayer
        else -> null
    }
    fun saveGame(request: SaveRequest) {
        val layer = requireNotNull(battleLayer) { "Battle.onCreate must run before SAVE_GAME" }
        val battleSave = linkedMapOf<String, Any?>()
        layer.save(battleSave)
        val modelSave = linkedMapOf<String, Any?>()
        model.save(modelSave)
        battleSave["model"] = modelSave
        manager.saveGame(request.index, factory.stringify(battleSave))
        request.onComplete?.invoke()
    }
    fun showCharacterList() {
        val layer = requireNotNull(battleLayer) { "Battle.onCreate must run before SHOW_CHARACTER_LIST" }
        factory.addForcesList(layer.filterUnits(1187), layer.filterUnits(1196), flag = 1)
    }
}
