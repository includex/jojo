// Battle Render
package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Texture
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.presentation.battle.assets.MagicEffectDefinition
import com.jojo.game.presentation.battle.timeline.MagicEffectAnimation
import com.jojo.game.presentation.battle.timeline.UnitActionAnimation
import com.jojo.game.presentation.battle.timeline.UnitAnimationKind
import com.jojo.game.presentation.battle.unit.BattleUnitAttributeStatusRender
import com.jojo.game.presentation.battle.unit.BattleUnitPresentationState
import com.jojo.game.presentation.battle.unit.BattleUnitStateAnimation
import com.jojo.game.presentation.battle.unit.BattleUnitStateRender
import com.jojo.game.presentation.battle.unit.ScriptedUnitVisual
import com.jojo.game.presentation.battle.unit.UnitSpriteFrame
import com.jojo.game.presentation.battle.unit.UnitSpriteSource

/** BattleActorEffectViewComposer: live Screen 상태를 renderer가 소비할 actor·effect·say-marker view로 조립한다. */
internal class BattleActorEffectViewComposer(
    /** `port` (Port): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val port: Port,
) {
    /** Port: composer가 필요한 live unit, frame, terrain, animation, asset 조회만 노출한다. */
    internal interface Port {
        /**
         * `boardLeft`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun boardLeft(): Float
        /**
         * `boardBottom`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun boardBottom(): Float
        /**
         * `tileSize`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun tileSize(): Float
        /**
         * `animationClock`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun animationClock(): Float
        /**
         * `stateEffectAnimationClock`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun stateEffectAnimationClock(): Float
        /**
         * `dialogueBlendRoute`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun dialogueBlendRoute(): Boolean
        /**
         * `battleMenuOpen`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun battleMenuOpen(): Boolean
        /**
         * `sourceScenario`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun sourceScenario(): String
        /**
         * `spriteFrame`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun spriteFrame(unit: BattleUnit): UnitSpriteFrame
        /**
         * `activeAction`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun activeAction(unitId: String, now: Float): UnitActionAnimation?
        /**
         * `deathAnimationActive`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun deathAnimationActive(unitId: String, now: Float): Boolean
        /**
         * `scriptedVisual`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun scriptedVisual(unitId: String): ScriptedUnitVisual?
        /**
         * `texture`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun texture(unit: BattleUnit, source: UnitSpriteSource): Texture?
        /**
         * `visualTile`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun visualTile(unit: BattleUnit): Pair<Float, Float>
        /**
         * `terrainAt`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun terrainAt(unit: BattleUnit): Int
        /**
         * `terrainMask`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun terrainMask(terrain: Int): Texture?
        /**
         * `hpTexture`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun hpTexture(unit: BattleUnit): Texture?
        /**
         * `hpRatio`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun hpRatio(unit: BattleUnit, now: Float): Float
        /**
         * `attributeStatuses`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun attributeStatuses(unit: BattleUnit): Map<BattleAttribute, BattleUnitPresentationState.AttributeStatusIcon>
        /**
         * `otherNodesVisible`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun otherNodesVisible(unit: BattleUnit): Boolean
        /**
         * `stateEffect`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun stateEffect(unit: BattleUnit): BattleUnitStateAnimation.Effect?
        /**
         * `stateTexture`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun stateTexture(textureIndex: Int): Texture?
        /**
         * `magicEffectAnimations`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun magicEffectAnimations(): List<MagicEffectAnimation>
        /**
         * `magicEffect`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun magicEffect(effectId: Int): MagicEffectDefinition?
        /**
         * `magicEffectTexture`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun magicEffectTexture(effectId: Int): Texture?
        /**
         * `presentationUnit`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun presentationUnit(unitId: String): BattleUnit?
        /**
         * `sayTexture`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun sayTexture(): Texture?
        /**
         * `dialogueSpeakerId`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun dialogueSpeakerId(): Int?
    }

    /**
     * `stateAnimationStarts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val stateAnimationStarts = mutableMapOf<String, Pair<List<Int>, Float>>()

    /** compose: 현재 actors와 마법 효과, 대화 표식을 기존 renderer view 형식으로 고정한다. */
    fun compose(visibleUnits: List<BattleUnit>): BattleActorEffectRenderView {
        val now = port.animationClock()
        return BattleActorEffectRenderView(
            boardLeft = port.boardLeft(),
            boardBottom = port.boardBottom(),
            tileSize = port.tileSize(),
            actors = visibleUnits.map { actor(it, now) },
            effects = effects(now),
            sayMarker = sayMarker(visibleUnits),
        )
    }

    /** actor: 한 유닛의 sprite·HP·attribute/state effect draw 값을 조립한다. */
    private fun actor(unit: BattleUnit, now: Float): BattleActorRenderUnit {
        val frame = port.spriteFrame(unit)
        val action = port.activeAction(unit.id, now)
        val scripted = action?.let { null } ?: port.scriptedVisual(unit.id)
        val tileSize = port.tileSize()
        val boardLeft = port.boardLeft()
        val boardBottom = port.boardBottom()
        val (visualX, visualY) = port.visualTile(unit)
        val stateCommand = stateCommand(unit, visualX, visualY, now)
        val sourceHighlight = !port.dialogueBlendRoute() && port.sourceScenario() == "S_00" && scripted?.action == 4
        return BattleActorRenderUnit(
            id = unit.id,
            tileX = visualX,
            tileY = visualY,
            texture = port.texture(unit, frame.source),
            sourceY = frame.sourceY,
            sourceWidth = frame.sourceWidth,
            sourceHeight = frame.sourceHeight,
            size = if (frame.source == UnitSpriteSource.ATTACK) tileSize * 4f / 3f else tileSize,
            offsetX = frame.offsetX,
            offsetY = frame.offsetY,
            flipX = frame.flipX || (action?.kind == UnitAnimationKind.ATTACK && action.direction == 1),
            terrainMask = port.terrainMask(port.terrainAt(unit)),
            sourceHighlight = sourceHighlight,
            hpTexture = port.hpTexture(unit),
            hpRatio = port.hpRatio(unit, now),
            showHpBar = !port.deathAnimationActive(unit.id, now) &&
                !(!port.dialogueBlendRoute() && port.sourceScenario() == "S_00" && port.scriptedVisual(unit.id)?.action == 4),
            attributeStatuses = BattleUnitAttributeStatusRender.commands(
                port.attributeStatuses(unit),
                port.otherNodesVisible(unit),
                boardLeft + visualX * tileSize,
                boardBottom - visualY * tileSize,
                tileSize,
            ),
            state = stateCommand,
            stateTexture = stateCommand?.let { port.stateTexture(it.textureIndex) },
        )
    }

    /** stateCommand: 동일 effect frame은 시작 시점을 유지하고, inactive effect는 composer 상태에서 제거한다. */
    private fun stateCommand(unit: BattleUnit, visualX: Float, visualY: Float, now: Float): BattleUnitStateRender.Command? {
        val effect = port.stateEffect(unit)
        if (effect == null || !effect.active) {
            stateAnimationStarts.remove(unit.id)
            return null
        }
        val previous = stateAnimationStarts[unit.id]
        val startedAt = if (previous == null || previous.first != effect.textureIndices) {
            now.also { stateAnimationStarts[unit.id] = effect.textureIndices.toList() to it }
        } else {
            previous.second
        }
        return BattleUnitStateRender.command(
            effect,
            port.stateEffectAnimationClock() - startedAt,
            port.boardLeft() + visualX * port.tileSize(),
            port.boardBottom() - visualY * port.tileSize(),
            port.tileSize(),
        )
    }

    /** effects: 현재 재생 중인 magic effect animation을 texture crop·alpha render view로 변환한다. */
    private fun effects(now: Float): List<BattleEffectRender> = port.magicEffectAnimations()
        .filter { now in it.startedAt..<it.endsAt }
        .flatMap { animation ->
            /**
             * `effect` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val effect = port.magicEffect(animation.effectId) ?: return@flatMap emptyList()
            /**
             * `frame` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val frame = effect.frameAt(now - animation.startedAt) ?: return@flatMap emptyList()
            if (frame.sourceIndex < 0) return@flatMap emptyList()
            /**
             * `texture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val texture = port.magicEffectTexture(animation.effectId) ?: return@flatMap emptyList()
            animation.targetIds.mapNotNull { id ->
                port.presentationUnit(id)?.takeIf { it.visible }?.let { target ->
                    /**
                     * `tileSize` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val tileSize = port.tileSize()
                    /**
                     * `width` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val width = effect.frameWidth / 48f * tileSize
                    /**
                     * `height` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val height = effect.frameHeight / 48f * tileSize
                    BattleEffectRender(
                        texture = texture,
                        x = port.boardLeft() + target.tileX * tileSize + (tileSize - width) / 2 + frame.offsetX / 48f * tileSize,
                        y = port.boardBottom() - target.tileY * tileSize + (tileSize - height) / 2 - frame.offsetY / 48f * tileSize,
                        width = width,
                        height = height,
                        sourceX = 0,
                        sourceY = (frame.sourceIndex * effect.frameHeight).coerceAtMost((texture.height - effect.frameHeight).coerceAtLeast(0)),
                        sourceWidth = minOf(effect.frameWidth, texture.width),
                        sourceHeight = minOf(effect.frameHeight, texture.height),
                        alpha = (frame.alpha + 24).coerceIn(0, 32) / 32f,
                    )
                }
            }
        }

    /** sayMarker: 메뉴가 닫힌 대화 중 현재 화면의 화자에게 기존 marker view를 부여한다. */
    private fun sayMarker(visibleUnits: List<BattleUnit>): BattleSayMarkerRender? {
        if (port.battleMenuOpen()) return null
        val speakerId = port.dialogueSpeakerId() ?: return null
        val texture = port.sayTexture() ?: return null
        val speaker = visibleUnits.firstOrNull { it.characterId == speakerId } ?: return null
        val (x, y) = port.visualTile(speaker)
        return BattleSayMarkerRender(texture, x, y)
    }
}
