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
    private val port: Port,
) {
    /** Port: composer가 필요한 live unit, frame, terrain, animation, asset 조회만 노출한다. */
    internal interface Port {
        fun boardLeft(): Float
        fun boardBottom(): Float
        fun tileSize(): Float
        fun animationClock(): Float
        fun stateEffectAnimationClock(): Float
        fun dialogueBlendRoute(): Boolean
        fun battleMenuOpen(): Boolean
        fun sourceScenario(): String
        fun spriteFrame(unit: BattleUnit): UnitSpriteFrame
        fun activeAction(unitId: String, now: Float): UnitActionAnimation?
        fun deathAnimationActive(unitId: String, now: Float): Boolean
        fun scriptedVisual(unitId: String): ScriptedUnitVisual?
        fun texture(unit: BattleUnit, source: UnitSpriteSource): Texture?
        fun visualTile(unit: BattleUnit): Pair<Float, Float>
        fun terrainAt(unit: BattleUnit): Int
        fun terrainMask(terrain: Int): Texture?
        fun hpTexture(unit: BattleUnit): Texture?
        fun hpRatio(unit: BattleUnit, now: Float): Float
        fun attributeStatuses(unit: BattleUnit): Map<BattleAttribute, BattleUnitPresentationState.AttributeStatusIcon>
        fun otherNodesVisible(unit: BattleUnit): Boolean
        fun stateEffect(unit: BattleUnit): BattleUnitStateAnimation.Effect?
        fun stateTexture(textureIndex: Int): Texture?
        fun magicEffectAnimations(): List<MagicEffectAnimation>
        fun magicEffect(effectId: Int): MagicEffectDefinition?
        fun magicEffectTexture(effectId: Int): Texture?
        fun presentationUnit(unitId: String): BattleUnit?
        fun sayTexture(): Texture?
        fun dialogueSpeakerId(): Int?
    }

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
            val effect = port.magicEffect(animation.effectId) ?: return@flatMap emptyList()
            val frame = effect.frameAt(now - animation.startedAt) ?: return@flatMap emptyList()
            if (frame.sourceIndex < 0) return@flatMap emptyList()
            val texture = port.magicEffectTexture(animation.effectId) ?: return@flatMap emptyList()
            animation.targetIds.mapNotNull { id ->
                port.presentationUnit(id)?.takeIf { it.visible }?.let { target ->
                    val tileSize = port.tileSize()
                    val width = effect.frameWidth / 48f * tileSize
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
