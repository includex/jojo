// Battle
package com.jojo.game.presentation.battle.fight

import com.jojo.game.presentation.battle.unit.UnitSpriteSource
import com.jojo.game.presentation.battle.timeline.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/** FightSpriteTimeline: 전투 스프라이트 시간 흐름이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */
class FightSpriteTimeline private constructor(private val clips: Map<Int, Clip>) {
    /**
     * `SoundEvent`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class SoundEvent(val atSeconds: Float, val value: String)


    /** Frame: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
    data class Frame(
        /**
         * `source` (UnitSpriteSource,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val source: UnitSpriteSource,
        /**
         * `sourceY` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sourceY: Int,
        /**
         * `sourceWidth` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sourceWidth: Int,
        /**
         * `sourceHeight` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sourceHeight: Int,
        /**
         * `pose` (FightActionPose,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val pose: FightActionPose,
        /**
         * `material` (BattleCharacterMaterial,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val material: BattleCharacterMaterial,
        /**
         * `materialValue` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val materialValue: Float = 0f,
    )
    /**
     * `SpriteKey`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class SpriteKey(val atTicks: Float, val source: UnitSpriteSource, val index: Int)
    /**
     * `ScalarKey`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class ScalarKey(val atTicks: Float, val value: Float, val linear: Boolean)
    /**
     * `PositionKey`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class PositionKey(val atTicks: Float, val x: Float, val y: Float, val linear: Boolean)
    /**
     * `MaterialKey`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class MaterialKey(
        /**
         * `atTicks` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val atTicks: Float,
        /**
         * `material` (BattleCharacterMaterial,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val material: BattleCharacterMaterial,
        /**
         * `value` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Float,
    )
    /**
     * `SoundKey`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class SoundKey(val atTicks: Float, val value: String)
    /**
     * `Clip`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class Clip(
        /**
         * `totalTicks` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val totalTicks: Float,
        /**
         * `sprites` (List<SpriteKey>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sprites: List<SpriteKey>,
        /**
         * `positions` (List<PositionKey>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val positions: List<PositionKey>,
        /**
         * `scales` (List<ScalarKey>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scales: List<ScalarKey>,
        /**
         * `opacities` (List<ScalarKey>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val opacities: List<ScalarKey>,
        /**
         * `materials` (List<MaterialKey>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val materials: List<MaterialKey>,
        /**
         * `sounds` (List<SoundKey>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sounds: List<SoundKey>,
    )


    /**
     * `duration`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun duration(action: Int): Float = requireClip(action).totalTicks / 24f


    /**
     * `pose`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `frame`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
     * `soundEventsCrossed`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun soundEventsCrossed(
        action: Int,
        fromExclusiveSeconds: Float,
        toInclusiveSeconds: Float,
        includeStart: Boolean = false,
    ): List<SoundEvent> {
        if (toInclusiveSeconds + EPSILON < fromExclusiveSeconds) return emptyList()
        return requireClip(action).sounds.mapNotNull { event ->
            /**
             * `at` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val at = event.atTicks / 24f
            /**
             * `startsNow` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val startsNow = includeStart && event.atTicks <= EPSILON
            /**
             * `crossed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val crossed = at > fromExclusiveSeconds + EPSILON && at <= toInclusiveSeconds + EPSILON
            if (startsNow || crossed) SoundEvent(at, event.value) else null
        }
    }

    /**
     * `requireClip`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun requireClip(action: Int): Clip = requireNotNull(clips[action]) {
        "missing shipped FightUnit anime$action"
    }

    /**
     * `sampleTick`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun sampleTick(clip: Clip, elapsedSeconds: Float): Float =
        (elapsedSeconds.coerceAtLeast(0f) * 24f).coerceAtMost((clip.totalTicks - EPSILON).coerceAtLeast(0f))

    /**
     * `scalarAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun scalarAt(keys: List<ScalarKey>, tick: Float, default: Float): Float {
        val index = keys.indexOfLast { it.atTicks <= tick + EPSILON }
        if (index < 0) return default
        val key = keys[index]
        val next = keys.getOrNull(index + 1)
        if (!key.linear || next == null || next.atTicks <= key.atTicks) return key.value
        val ratio = ((tick - key.atTicks) / (next.atTicks - key.atTicks)).coerceIn(0f, 1f)
        return key.value + (next.value - key.value) * ratio
    }

    /**
     * `positionAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
        /**
         * `EPSILON` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private const val EPSILON = 0.0001f
        /**
         * `cached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private val cached by lazy {
            fromRoot(JsonReader().parse(Gdx.files.internal("maps/fight-anime.json")))
        }


        /**
         * `load`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun load(): FightSpriteTimeline = cached


        /**
         * `fromJson`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun fromJson(json: String): FightSpriteTimeline = fromRoot(JsonReader().parse(json))

        /**
         * `fromRoot`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

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

        /**
         * `parseClip`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

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
