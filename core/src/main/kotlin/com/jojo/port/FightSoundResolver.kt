package com.jojo.port

/** Result of FightUnit.__cb1 after resolving its optional `yidong` token. */
data class FightSoundDispatch(
    val resolvedId: Int,
    val effectId: Int? = null,
    val backgroundId: Int? = null,
)

object FightSoundResolver {
    /**
     * Recovered BattleUnit.moveSound/FightUnit.__cb1 contract.
     * ARM MOVESOUND 0/1/2 selects hoof/wheel/walk; 3 and negative values are
     * silent, while values above 3 fall back to hoof. Callback IDs above 300
     * address the background track after subtracting 300.
     */
    fun resolve(value: String, armMoveSound: Int = -1): FightSoundDispatch {
        val resolved = if (value == "yidong") {
            when (if (armMoveSound > 3) 0 else armMoveSound) {
                0 -> 24
                1 -> 25
                2 -> 23
                else -> -1
            }
        } else value.toIntOrNull() ?: -1
        return when {
            resolved < 0 -> FightSoundDispatch(resolved)
            resolved > 300 -> FightSoundDispatch(resolved, backgroundId = resolved - 300)
            else -> FightSoundDispatch(resolved, effectId = resolved)
        }
    }
}
