package com.jojo.port

/** Source-faithful state contract for ui/SettingLayer.js. */
class SettingLayer(
    private val store: Store,
    private val sound: Sound = Sound.NONE,
    private val featureEnvironment: () -> FeatureEnvironment = { FeatureEnvironment() },
    private val applyGameSpeed: () -> Unit = {},
) {
    interface Store { fun getInt(key: String, default: Int = 0): Int; fun putInt(key: String, value: Int) }
    interface Sound { fun music(on: Boolean); fun effect(on: Boolean); companion object { val NONE = object : Sound { override fun music(on:Boolean)=Unit; override fun effect(on:Boolean)=Unit } } }
    data class View(val flags:Int, val msgSpeed:Int, val notifyLevel:Int, val background:Int, val speed:Float, val attached:Boolean)
    data class FeatureEnvironment(
        val sceneName: String = "Login",
        val supportAdCode: Int = 0,
        val achievements: Map<Int, List<Any>> = emptyMap(),
        val signInCount: Int = 0,
        val signInDays: MutableList<Int> = mutableListOf(),
        val nowSeconds: Int = 0,
        val raffleVideoCount: Int = 0,
        val luckyCoins: Int = 0,
    )
    sealed interface FeatureResult {
        data class Opened(val name: String) : FeatureResult
        data class Toast(val text: String) : FeatureResult
        data object Gated : FeatureResult
        data object Ignored : FeatureResult
    }
    var activeFeature: Any? = null
        private set
    private var flags=0; private var speed=0f; private var speedChanged=false; private var attached=false
    fun onCreate(): View { flags=store.getInt(GAME_SETTING, BG_SOUND or EFFECT_SOUND or MINI_MAP); speed=store.getInt(GAME_SPEED,0)/100f; attached=true; return view() }
    /** Toggle check event: all flag writes immediate; bits 0/1 also reconfigure Sound. */
    fun check(bit:Int, checked:Boolean) { require(bit in 0..6); flags=if(checked) flags or (1 shl bit) else flags and (1 shl bit).inv(); store.putInt(GAME_SETTING,flags); if(bit==0)sound.music(checked); if(bit==1)sound.effect(checked) }
    /** check2 tags E<<8|N; source persists MSG_SPEED and NOTIFY_LV immediately. */
    fun check2(panel:Int, selection:Int) { require(panel in 0..2 && selection>=0); if(panel!=1) store.putInt(if(panel==0) MSG_SPEED else NOTIFY_LV,selection) }
    fun selectBackground(index:Int) { require(index in 0..3); store.putInt(BG_INDEX,index) }
    fun onSlider(progress:Float) { speed=progress.coerceIn(0f,1f); speedChanged=true }
    /** The source close listener only detaches on TOUCH_END; persistence belongs to onDestroy. */
    fun dismiss(eventType:Int): Boolean { if(eventType!=TOUCH_END||!attached)return false; attached=false; return true }
    /** Source onDestroy commits GAME_SPEED2 only after onSlider set its dirty flag. */
    fun onDestroy() { if (speedChanged) { store.putInt(GAME_SPEED,(speed*100).toInt()); applyGameSpeed() } }
    /** Recovered optional buttons 7/8/9: achievements, raffle and sign-in. */
    fun featureButton(tag: Int, eventType: Int): FeatureResult {
        if (!attached || eventType != TOUCH_END || tag !in 7..9) return FeatureResult.Ignored
        val env = featureEnvironment()
        return when (tag) {
            7 -> if (env.achievements.isEmpty()) {
                FeatureResult.Toast("저장된 게임에서 다시 확인해 주세요./현재 업적이 없습니다.")
            } else {
                activeFeature = AchievementsLayerPort(env.achievements)
                FeatureResult.Opened("AchievementsLayer")
            }
            8 -> when {
                env.supportAdCode < 8 -> FeatureResult.Gated
                env.sceneName !in setOf("Hall", "Battle") ->
                    FeatureResult.Toast("전투 준비/전투 중일 때만 뽑기가 가능합니다!")
                else -> {
                    activeFeature = RaffleLayerPort(env.raffleVideoCount, env.luckyCoins)
                    FeatureResult.Opened("RaffleLayer")
                }
            }
            else -> if (env.supportAdCode < 8) FeatureResult.Gated else {
                activeFeature = SignInLayerPort(env.signInCount, env.signInDays, env.nowSeconds)
                FeatureResult.Opened("SignInLayer")
            }
        }
    }
    /** Compatibility entry point used by the game shell: detach then dispose immediately. */
    fun close(eventType:Int): Boolean { val removed=dismiss(eventType); if(removed) onDestroy(); return removed }
    fun view()=View(flags,store.getInt(MSG_SPEED,1),store.getInt(NOTIFY_LV,1),store.getInt(BG_INDEX,0),speed,attached)
    companion object {
        const val TOUCH_END = 2
        const val GAME_SETTING = "GAME_SETTING"
        const val MSG_SPEED = "MSG_SPEED"
        const val GAME_SPEED = "GAME_SPEED2"
        const val NOTIFY_LV = "NOTIFY_LV"
        const val BG_INDEX = "BG_IDX"
        const val BG_SOUND = 1
        const val EFFECT_SOUND = 2
        const val MINI_MAP = 4
        const val AUTO_CLOSE = 8
        const val BOARD_BAR_TOP = 16
        const val R_IDX_INC = 32
        const val R_ASPECT_RATIO = 64
    }
}
