package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattleUnitDefaultActionTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleUnitDefaultActionTest {
    private fun unit(hp: Int = 100, famous: Boolean = false, acted: Boolean = false, states: Set<BattleStatus> = emptySet()) =
        BattleUnit("u", "u", Faction.PLAYER, 0, 0, hitPoints = hp, maxHitPoints = 100, famous = famous, hasActed = acted,
            statuses = states.associateWithTo(linkedMapOf()) { 1 })

    private fun BattleUnit.presentationAction() = presentation.defaultAction(
        BattleUnitPresentationState.DefaultActionInput(
            visible = visible,
            hitPoints = hitPoints,
            maxHitPoints = maxHitPoints,
            famous = famous,
            hasActed = hasActed,
            poisoned = BattleStatus.POISON in statuses,
            paralyzed = BattleStatus.PARALYSIS in statuses,
        ),
    )

    @Test
    fun `defaultAction selects every normal HP source BRAnime combination`() {
        assertEquals(BattleUnitPresentationState.DefaultAction(0, true), unit().presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(39, false), unit(acted = true).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(40, true), unit(acted = true, states = setOf(BattleStatus.POISON)).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(36, true), unit(states = setOf(BattleStatus.PARALYSIS)).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(37, true), unit(states = setOf(BattleStatus.POISON)).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(38, true), unit(states = setOf(BattleStatus.POISON, BattleStatus.PARALYSIS)).presentationAction())
    }

    @Test
    fun `defaultAction selects every low HP source BRAnime combination and famous threshold`() {
        assertEquals(BattleUnitPresentationState.DefaultAction(9, true), unit(hp = 19).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(44, false), unit(hp = 19, acted = true).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(45, true), unit(hp = 19, acted = true, states = setOf(BattleStatus.POISON)).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(41, true), unit(hp = 19, states = setOf(BattleStatus.POISON)).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(42, true), unit(hp = 19, states = setOf(BattleStatus.PARALYSIS)).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(43, true), unit(hp = 19, states = setOf(BattleStatus.POISON, BattleStatus.PARALYSIS)).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(9, true), unit(hp = 39, famous = true).presentationAction())
        assertEquals(BattleUnitPresentationState.DefaultAction(0, true), unit(hp = 39, famous = false).presentationAction())
    }
}
