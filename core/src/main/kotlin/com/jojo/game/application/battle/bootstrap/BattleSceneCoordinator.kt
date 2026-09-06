// Battle
package com.jojo.game.application.battle.bootstrap

import com.jojo.game.domain.battle.*

/**
 * `BattleSceneCoordinator` 클래스: bootstrap 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class BattleSceneCoordinator(
    /**
     * `factory` (Factory,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val factory: Factory,
    /**
     * `model` (Model,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val model: Model,
    /**
     * `manager` (Manager,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val manager: Manager,
    /**
     * `battleLayerResource` (Any?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val battleLayerResource: Any? = null,
    /**
     * `battleInitLayer` (Any?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val battleInitLayer: Any? = null,
    /**
     * `miniMapLayer` (Any?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val miniMapLayer: Any? = null,
    /**
     * `noticeInfoLayer` (Any?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val noticeInfoLayer: Any? = null,
) {

    /**
     * `BattleScreen` 계약 인터페이스: bootstrap 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    interface BattleScreen {
        /**
         * `save`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun save(out: MutableMap<String, Any?>)
        /**
         * `filterUnits`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun filterUnits(flag: Int): List<Any?>
    }

    /**
     * `interface`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun interface Model {
        /**
         * `save`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun save(out: MutableMap<String, Any?>)
    }

    /**
     * `interface`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun interface Manager {
        /**
         * `saveGame`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun saveGame(index: Int, json: String)
    }


    /**
     * `Factory` 계약 인터페이스: bootstrap 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    interface Factory {

        /**
         * `addBattleScreen`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun addBattleScreen(data: Any?): BattleScreen


        /**
         * `addForcesList`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun addForcesList(mine: List<Any?>, enemy: List<Any?>, flag: Int)
        /**
         * `stringify`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun stringify(value: Map<String, Any?>): String
    }


    /**
     * `SaveRequest` 클래스: bootstrap 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

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

    /**
     * `battleLayer` (BattleScreen?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var battleLayer: BattleScreen? = null

    /** onCreate: 입력 또는 이벤트를 반영해 전투 상태를 전환한다. */
    fun onCreate(data: Any?) {
        battleLayer = factory.addBattleScreen(data)
    }
    /**
     * `getResource`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun getResource(layer: Layer): Any? = when (layer) {
        Layer.BATTLE_LAYER -> battleLayerResource
        Layer.BATTLE_INIT_LAYER -> battleInitLayer
        Layer.MINI_MAP_LAYER -> miniMapLayer
        Layer.NOTICE_INFO_LAYER -> noticeInfoLayer
        else -> null
    }
    /**
     * `saveGame`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
    /**
     * `showCharacterList`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showCharacterList() {
        val layer = requireNotNull(battleLayer) { "Battle.onCreate must run before SHOW_CHARACTER_LIST" }
        factory.addForcesList(layer.filterUnits(1187), layer.filterUnits(1196), flag = 1)
    }
}
