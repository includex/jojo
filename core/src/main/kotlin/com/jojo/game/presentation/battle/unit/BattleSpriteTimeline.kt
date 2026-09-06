// Battle
package com.jojo.game.presentation.battle.unit

import com.jojo.game.domain.battle.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/** BattleSpriteTimeline: 전투 스프라이트 시간 흐름이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */

class BattleSpriteTimeline private constructor(private val clips: Map<String, List<Keyframe>>) {

    /** Atlas: 전투 화면 표시에 사용할 이미지와 자원 경로를 보관한다. */
    enum class Atlas(val source: UnitSpriteSource, val width: Int, val height: Int) {
        ATTACK(UnitSpriteSource.ATTACK, 64, 64),
        MOVEMENT(UnitSpriteSource.MOVEMENT, 48, 48),
        SPECIAL(UnitSpriteSource.SPECIAL, 48, 48),
    }


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
         * `flipX` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val flipX: Boolean,
        /**
         * `offsetX` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offsetX: Float = 0f,
        /**
         * `offsetY` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offsetY: Float = 0f,
    )
    /**
     * `Keyframe`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class Keyframe(
        /**
         * `ticks` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val ticks: Int,
        /**
         * `atlas` (Atlas?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val atlas: Atlas?,
        /**
         * `index` (Int?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val index: Int?,
        /**
         * `scaleX` (Int?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scaleX: Int?,
        /**
         * `offsetX` (Int?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offsetX: Int?,
        /**
         * `offsetY` (Int?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offsetY: Int?,
        /**
         * `hit` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hit: Boolean,
    )

    /** resolvedClip: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    private fun resolvedClip(action: Int, direction: Int): Pair<List<Keyframe>, Boolean>? {
        val desired = "anime${action}_$direction"
        clips[desired]?.let { return it to false }
        if (direction == 1) clips["anime${action}_3"]?.let { return it to true }
        return clips["anime$action"]?.let { it to false }
    }


    /**
     * `duration`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun duration(action: Int, direction: Int): Float =
        resolvedClip(action, direction)?.first?.sumOf(Keyframe::ticks)?.div(24f) ?: 0f
    /**
     * `clipNames`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun clipNames(): Set<String> = clips.keys
    /**
     * `hitTime`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hitTime(action: Int, direction: Int): Float? {
        val clip = resolvedClip(action, direction)?.first ?: return null
        var ticks = 0
        clip.forEach { keyframe ->
            if (keyframe.hit) return ticks / 24f
            ticks += keyframe.ticks
        }
        return null
    }


    /**
     * `frame`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun frame(action: Int, direction: Int, elapsed: Float, loop: Boolean = false): Frame? {
        val (clip, mirror) = resolvedClip(action, direction) ?: return null
        val total = clip.sumOf(Keyframe::ticks).coerceAtLeast(1)
        var tick = (elapsed.coerceAtLeast(0f) * 24f).toInt()
        if (loop) tick %= total else tick = tick.coerceAtMost(total - 1)
        var used = 0
        var atlas: Atlas? = null
        var index: Int? = null
        var scaleX = 1
        var offsetX = 0
        var offsetY = 0
        for (key in clip) {
            key.atlas?.let { atlas = it }
            key.index?.let { index = it }
            key.scaleX?.let { scaleX = it }
            key.offsetX?.let { offsetX = it }
            key.offsetY?.let { offsetY = it }
            used += key.ticks
            if (tick < used) break
        }
        val selectedAtlas = atlas ?: return null
        val selectedIndex = index ?: return null
        val inset = 1
        return Frame(
            source = selectedAtlas.source,
            sourceY = selectedIndex * (selectedAtlas.height + inset * 2) + inset,
            sourceWidth = selectedAtlas.width,
            sourceHeight = selectedAtlas.height,
            flipX = mirror.xor(scaleX < 0),
            offsetX = offsetX.toFloat() * 2f,
            offsetY = offsetY.toFloat() * 2f,
        )
    }

    companion object {
        /**
         * `cached` (BattleSpriteTimeline by lazy): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private val cached: BattleSpriteTimeline by lazy {
            fromRoot(JsonReader().parse(Gdx.files.internal("maps/battle-anime.json")))
        }


        /**
         * `load`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun load(): BattleSpriteTimeline = cached
        /**
         * `fromJson`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun fromJson(json: String): BattleSpriteTimeline = fromRoot(JsonReader().parse(json))

        /**
         * `fromRoot`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        private fun fromRoot(root: JsonValue): BattleSpriteTimeline {
            val parsed = linkedMapOf<String, List<Keyframe>>()
            var clip = root.child
            while (clip != null) {
                val frames = mutableListOf<Keyframe>()
                var entry = clip.child
                while (entry != null) {
                    frames += parseKeyframe(entry)
                    entry = entry.next
                }
                parsed[clip.name] = frames
                clip = clip.next
            }
            return BattleSpriteTimeline(parsed)
        }

        /**
         * `parseKeyframe`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        private fun parseKeyframe(value: JsonValue): Keyframe {
            val sprite = value.get("sprite")
            val atlas = sprite?.getInt("t", -1)?.let { ordinal -> Atlas.entries.getOrNull(ordinal) }
            val props = value.get("props")
            var hitEvent = false
            var event = value.get("events")?.get("0")?.child
            while (event != null) {
                if (event.asString() == "hit") hitEvent = true
                event = event.next
            }

            /**
             * `prop`: 타입의 핵심 동작을 수행한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
             */

            fun prop(name: String): Int? = props?.get(name)?.get(0)?.let { raw ->
                when (raw.type()) {
                    JsonValue.ValueType.array -> raw.getInt(0)
                    else -> raw.asInt()
                }
            }

            /**
             * `position` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val position = props?.get("position")?.get(0)
            return Keyframe(
                ticks = value.getInt("frame", 1),
                atlas = atlas,
                index = sprite?.getInt("idx", -1)?.takeIf { it >= 0 },
                scaleX = prop("scaleX"),
                offsetX = position?.getInt(0),
                offsetY = position?.getInt(1),
                hit = hitEvent,
            )
        }
    }
}
