package com.jojo.game.presentation.battle.fight

import com.jojo.game.presentation.battle.FightActionPose
import com.jojo.game.presentation.battle.UnitSpriteSource
import com.jojo.game.presentation.battle.timeline.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/** Exact SpriteFrame/transform sampling for FightUnit's shipped animeFR. */
class FightSpriteTimeline private constructor(private val clips: Map<Int, Clip>) {
    /** Raw FightUnit `__cb1` payload at its authored animation time. */
    data class SoundEvent(val atSeconds: Float, val value: String)


    data class Frame(
        val source: UnitSpriteSource,
        val sourceY: Int,
        val sourceWidth: Int,
        val sourceHeight: Int,
        val pose: FightActionPose,
        val material: BattleCharacterMaterial,
        val materialValue: Float = 0f,
    )

    private data class SpriteKey(val atTicks: Float, val source: UnitSpriteSource, val index: Int)
    private data class ScalarKey(val atTicks: Float, val value: Float, val linear: Boolean)
    private data class PositionKey(val atTicks: Float, val x: Float, val y: Float, val linear: Boolean)
    private data class MaterialKey(
        val atTicks: Float,
        val material: BattleCharacterMaterial,
        val value: Float,
    )

    private data class SoundKey(val atTicks: Float, val value: String)
    private data class Clip(
        val totalTicks: Float,
        val sprites: List<SpriteKey>,
        val positions: List<PositionKey>,
        val scales: List<ScalarKey>,
        val opacities: List<ScalarKey>,
        val materials: List<MaterialKey>,
        val sounds: List<SoundKey>,
    )


    fun duration(action: Int): Float = requireClip(action).totalTicks / 24f


    fun pose(action: Int, elapsedSeconds: Float): FightActionPose {
        val clip = requireClip(action)
        val tick = sampleTick(clip, elapsedSeconds)
        val position = positionAt(clip.positions, tick)
        return FightActionPose(
            childX = position.first,
            childY = position.second,
            childScaleX = scalarAt(clip.scales, tick, 1f),
            opacity = scalarAt(clip.opacities, tick, 255f),
        )
    }


    fun frame(action: Int, elapsedSeconds: Float): Frame? {
        val clip = requireClip(action)
        val tick = sampleTick(clip, elapsedSeconds)
        val sprite = clip.sprites.lastOrNull { it.atTicks <= tick + EPSILON } ?: return null
        val (width, height) = when (sprite.source) {
            UnitSpriteSource.ATTACK -> 64 to 64
            UnitSpriteSource.MOVEMENT, UnitSpriteSource.SPECIAL -> 48 to 48
        }
        return Frame(
            source = sprite.source,
            sourceY = sprite.index * (height + 2) + 1,
            sourceWidth = width,
            sourceHeight = height,
            pose = pose(action, elapsedSeconds),
            material = clip.materials.lastOrNull { it.atTicks <= tick + EPSILON }?.material
                ?: BattleCharacterMaterial.DEFAULT,
            materialValue = clip.materials.lastOrNull { it.atTicks <= tick + EPSILON }?.value ?: 0f,
        )
    }

    /**
     * Returns every callback1 crossed by one forward animation step.
     *
     * Animation callbacks at frame zero fire once when play() starts.  The
     * explicit [includeStart] flag preserves that case without letting a
     * later sample at the same time replay it.
     */
    fun soundEventsCrossed(
        action: Int,
        fromExclusiveSeconds: Float,
        toInclusiveSeconds: Float,
        includeStart: Boolean = false,
    ): List<SoundEvent> {
        if (toInclusiveSeconds + EPSILON < fromExclusiveSeconds) return emptyList()
        return requireClip(action).sounds.mapNotNull { event ->
            val at = event.atTicks / 24f
            val startsNow = includeStart && event.atTicks <= EPSILON
            val crossed = at > fromExclusiveSeconds + EPSILON && at <= toInclusiveSeconds + EPSILON
            if (startsNow || crossed) SoundEvent(at, event.value) else null
        }
    }

    private fun requireClip(action: Int): Clip = requireNotNull(clips[action]) {
        "missing shipped FightUnit anime$action"
    }

    private fun sampleTick(clip: Clip, elapsedSeconds: Float): Float =
        (elapsedSeconds.coerceAtLeast(0f) * 24f).coerceAtMost((clip.totalTicks - EPSILON).coerceAtLeast(0f))

    private fun scalarAt(keys: List<ScalarKey>, tick: Float, default: Float): Float {
        val index = keys.indexOfLast { it.atTicks <= tick + EPSILON }
        if (index < 0) return default
        val key = keys[index]
        val next = keys.getOrNull(index + 1)
        if (!key.linear || next == null || next.atTicks <= key.atTicks) return key.value
        val ratio = ((tick - key.atTicks) / (next.atTicks - key.atTicks)).coerceIn(0f, 1f)
        return key.value + (next.value - key.value) * ratio
    }

    private fun positionAt(keys: List<PositionKey>, tick: Float): Pair<Float, Float> {
        val index = keys.indexOfLast { it.atTicks <= tick + EPSILON }
        if (index < 0) return 0f to 0f
        val key = keys[index]
        val next = keys.getOrNull(index + 1)
        if (!key.linear || next == null || next.atTicks <= key.atTicks) return key.x to key.y
        val ratio = ((tick - key.atTicks) / (next.atTicks - key.atTicks)).coerceIn(0f, 1f)
        return (key.x + (next.x - key.x) * ratio) to (key.y + (next.y - key.y) * ratio)
    }

    companion object {
        private const val EPSILON = 0.0001f
        private val cached by lazy {
            fromRoot(JsonReader().parse(Gdx.files.internal("maps/fight-anime.json")))
        }


        fun load(): FightSpriteTimeline = cached


        fun fromJson(json: String): FightSpriteTimeline = fromRoot(JsonReader().parse(json))

        private fun fromRoot(root: JsonValue): FightSpriteTimeline {
            val result = linkedMapOf<Int, Clip>()
            var clipNode = root.child
            while (clipNode != null) {
                val action = clipNode.name.removePrefix("anime").toIntOrNull()
                if (action != null) result[action] = parseClip(clipNode)
                clipNode = clipNode.next
            }
            return FightSpriteTimeline(result)
        }

        private fun parseClip(node: JsonValue): Clip {
            val sprites = mutableListOf<SpriteKey>()
            val positions = mutableListOf(PositionKey(0f, 0f, 0f, true))
            val scales = mutableListOf(ScalarKey(0f, 1f, true))
            val opacities = mutableListOf(ScalarKey(0f, 255f, true))
            val materials = mutableListOf(MaterialKey(0f, BattleCharacterMaterial.DEFAULT, 0f))
            val sounds = mutableListOf<SoundKey>()
            var at = 0f
            var entry = node.child
            while (entry != null) {
                entry.get("sprite")?.let { sprite ->
                    val source = when (sprite.getInt("t", -1)) {
                        0 -> UnitSpriteSource.ATTACK
                        1 -> UnitSpriteSource.MOVEMENT
                        2 -> UnitSpriteSource.SPECIAL
                        else -> null
                    }
                    val index = sprite.getInt("idx", -1)
                    if (source != null && index >= 0) sprites += SpriteKey(at, source, index)
                }
                entry.get("props")?.let { props ->
                    props.get("position")?.let { value ->
                        val vector = value.get(0)
                        positions += PositionKey(
                            at,
                            vector.get(0).asFloat(),
                            vector.get(1).asFloat(),
                            value.get(1)?.asInt() == 1,
                        )
                    }
                    props.get("scaleX")?.let { value ->
                        scales += ScalarKey(at, value.get(0).asFloat(), value.get(1)?.asInt() == 1)
                    }
                    props.get("opacity")?.let { value ->
                        // Most recovered animation properties are encoded as
                        // [value, linearFlag], but anime23 keeps its two
                        // opacity keys as bare numbers.  Cocos treats those
                        // scalar assignments as discrete keyframes.  Reading
                        // them as arrays crashed the first battle that used
                        // action 23 (S_02), even though earlier scenarios
                        // happened not to instantiate that clip.
                        if (value.isArray) {
                            opacities += ScalarKey(at, value.get(0).asFloat(), value.get(1)?.asInt() == 1)
                        } else {
                            opacities += ScalarKey(at, value.asFloat(), false)
                        }
                    }
                }
                var materialEvent = entry.get("events")?.get("2")?.child
                while (materialEvent != null) {
                    val value = materialEvent.asInt()
                    val material = when {
                        value >= 200 -> BattleCharacterMaterial.GRAY
                        value >= 100 -> BattleCharacterMaterial.HIGHLIGHT
                        else -> BattleCharacterMaterial.DEFAULT
                    }
                    materials += MaterialKey(at, material, if (value in 100..199) (value - 100) / 10f else 0f)
                    materialEvent = materialEvent.next
                }
                var soundEvent = entry.get("events")?.get("1")?.child
                while (soundEvent != null) {
                    // Cocos animation events preserve both numeric effect
                    // IDs and the special `yidong` move-sound token.
                    sounds += SoundKey(at, soundEvent.asString())
                    soundEvent = soundEvent.next
                }
                at += entry.getFloat("frame", 1f)
                entry = entry.next
            }
            return Clip(at, sprites, positions, scales, opacities, materials, sounds)
        }
    }
}
