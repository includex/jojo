package com.jojo.game

/**
 * Coordinates the battle scene lifecycle recovered from `battle/Battle.js`.
 *
 * The source module is a UIScene container, rather than the tactical rules
 * model represented by [Battle].  Keeping this contract separate lets its
 * event and layer behaviour be tested without Cocos or JSON globals.
 */
/**
 * class  `BattleSceneCoordinator`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleSceneCoordinator(
    private val factory: Factory,
    private val model: Model,
    private val manager: Manager,
    /** Inspector-assigned source prefabs returned directly by getResource. */
    private val battleLayerResource: Any? = null,
    private val battleInitLayer: Any? = null,
    private val miniMapLayer: Any? = null,
    private val noticeInfoLayer: Any? = null,
) {
    /**
     * interface  `BattleScreen`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface BattleScreen {
        /** BattleScreen.save(out). */
        fun save(out: MutableMap<String, Any?>)

        /** BattleScreen.filterUnits(1187/1196). */
        fun filterUnits(flag: Int): List<Any?>
    }

    fun interface Model {
        fun save(out: MutableMap<String, Any?>)
    }

    fun interface Manager {
        fun saveGame(index: Int, json: String)
    }

    /**
     * interface  `Factory`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface Factory {
        /**
         * 공개 메서드 `addBattleScreen`
         *
         * ### 파라미터
        - `data` (`Any?`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `BattleScreen`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun addBattleScreen(data: Any?): BattleScreen

        /**
         * 공개 메서드 `addForcesList`
         *
         * ### 파라미터
        - `mine` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `enemy` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `flag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun addForcesList(mine: List<Any?>, enemy: List<Any?>, flag: Int)

        /** JSON.stringify payload; deliberately injectable for exact tests. */
        fun stringify(value: Map<String, Any?>): String
    }

    /**
     * data class  `SaveRequest`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class SaveRequest(val index: Int, val onComplete: (() -> Unit)? = null)

    /** Source static `Battle.LAYER` IDs, including aliases used by getResource. */
    enum class Layer(val id: Int) {
        BATTLE_LAYER(1), BATTLE_INIT_LAYER(2), TERRAIN_INFO_LAYER(3), BATTLE_UNIT_INFO_LAYER(4),
        COMMAND_LAYER(5), MINE_UNIT_INFO_LAYER(6), OTHER_UNIT_INFO_LAYER(7), SAY_LAYER(8),
        WIN_CONDITIONS_LAYER(9), ROUND_LAYER(10), MENU_LAYER(14), MAGICK_LIST_LAYER(15),
        FIGHT_LAYER(16), USE_PROPERTY_LAYER(17), REWARD_LAYER(18), WIN_CON_BOX_LAYER(20),
        EDIT_LAYER(22), EDIT_LAYER2(23), MINI_MAP_LAYER(24), NOTICE_INFO_LAYER(25),
        TUO_GUAN_LAYER(26), JI_QI_LAYER(27)
    }

    private var battleLayer: BattleScreen? = null

    /** Battle.onCreate: only BattleScreen is eagerly constructed. */
    fun onCreate(data: Any?) {
        battleLayer = factory.addBattleScreen(data)
    }

    /** Battle.getResource: only these four prefabs are exposed by the source. */
    fun getResource(layer: Layer): Any? = when (layer) {
        // Battle.js returns the inspector prefab (`battleLayer`), not the
        // private runtime instance (`_battleLayer`) created in onCreate.
        Layer.BATTLE_LAYER -> battleLayerResource
        Layer.BATTLE_INIT_LAYER -> battleInitLayer
        Layer.MINI_MAP_LAYER -> miniMapLayer
        Layer.NOTICE_INFO_LAYER -> noticeInfoLayer
        else -> null
    }

    /** Registered SAVE_GAME listener. */
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

    /** Registered SHOW_CHARACTER_LIST listener. */
    fun showCharacterList() {
        val layer = requireNotNull(battleLayer) { "Battle.onCreate must run before SHOW_CHARACTER_LIST" }
        factory.addForcesList(layer.filterUnits(1187), layer.filterUnits(1196), flag = 1)
    }
}
