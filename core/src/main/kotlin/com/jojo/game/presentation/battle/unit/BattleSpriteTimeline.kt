package com.jojo.game.presentation.battle.unit

import com.jojo.game.presentation.battle.UnitSpriteSource
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/**
 * Direct Kotlin implementation of Battle.CreateAnime's SpriteFrame selection for BattleUnit.
 *
 * The original does not encode sprite rows in game logic.  It reads animeBR,
 * chooses one of Unit_atk/mov/spc, and creates a 24fps SpriteFrame timeline
 * using `y = index * (height + 2 * inset) + inset`.  Keeping that definition
 * as data prevents individual actions from drifting into hand-maintained row
 * tables.
 */
/**
 * class  `BattleSpriteTimeline`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleSpriteTimeline private constructor(private val clips: Map<String, List<Keyframe>>) {
    /**
     * enum class  `Atlas`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Atlas(val source: UnitSpriteSource, val width: Int, val height: Int) {
        ATTACK(UnitSpriteSource.ATTACK, 64, 64),
        MOVEMENT(UnitSpriteSource.MOVEMENT, 48, 48),
        SPECIAL(UnitSpriteSource.SPECIAL, 48, 48),
    }

    /**
     * data class  `Frame`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Frame(
        val source: UnitSpriteSource,
        val sourceY: Int,
        val sourceWidth: Int,
        val sourceHeight: Int,
        val flipX: Boolean,
        val offsetX: Float = 0f,
        val offsetY: Float = 0f,
    )

    private data class Keyframe(
        val ticks: Int,
        val atlas: Atlas?,
        val index: Int?,
        val scaleX: Int?,
        val offsetX: Int?,
        val offsetY: Int?,
        /** Cocos Animation event `hit`, used by BattleScreen._attack2. */
        val hit: Boolean,
    )

    /** The generated `_1` clip is a mirrored copy of the authored `_3`. */
    private fun resolvedClip(action: Int, direction: Int): Pair<List<Keyframe>, Boolean>? {
        val desired = "anime${action}_$direction"
        clips[desired]?.let { return it to false }
        if (direction == 1) clips["anime${action}_3"]?.let { return it to true }
        return clips["anime$action"]?.let { it to false }
    }

    /**
     * 공개 메서드 `duration`
     *
     * ### 파라미터
    - `action` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `direction` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Float`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun duration(action: Int, direction: Int): Float =
        resolvedClip(action, direction)?.first?.sumOf(Keyframe::ticks)?.div(24f) ?: 0f

    /** Raw original animeBR keys, exposed for headless resource conformance. */
    fun clipNames(): Set<String> = clips.keys

    /** Time of BattleUnit.setAction2's authored `hit` callback, if present. */
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
     * 공개 메서드 `frame`
     *
     * ### 파라미터
    - `action` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `direction` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `elapsed` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `loop` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Frame?`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
        private val cached: BattleSpriteTimeline by lazy {
            fromRoot(JsonReader().parse(Gdx.files.internal("maps/battle-anime.json")))
        }

        /**
         * 공개 메서드 `load`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `BattleSpriteTimeline`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun load(): BattleSpriteTimeline = cached

        /** Kept public for the headless renderer conformance suite. */
        fun fromJson(json: String): BattleSpriteTimeline = fromRoot(JsonReader().parse(json))

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
             * 공개 메서드 `prop`
             *
             * ### 파라미터
            - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Int?`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun prop(name: String): Int? = props?.get(name)?.get(0)?.let { raw ->
                when (raw.type()) {
                    JsonValue.ValueType.array -> raw.getInt(0)
                    else -> raw.asInt()
                }
            }

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
