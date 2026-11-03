package com.jojo.game.presentation.battle.settlement

import com.jojo.game.presentation.shared.InfoBaseValueAnimation.Value
import kotlin.test.*

class SettlementValueAnimationTest {
    @Test fun `damage and armor experience start together while HP remains faster`() {
        val values = listOf(Value(0,100,40,100), Value(4,10,20,100))
        val early = SettlementValueAnimation.frame(values,.21f)
        assertTrue(early.numbers.getValue(0)<100)
        assertTrue(early.numbers.getValue(4)>10)
        assertTrue(early.ratios.getValue(0)<1f)
        assertTrue(early.ratios.getValue(4)>.1f)
        val hpDone = SettlementValueAnimation.frame(values,.7f)
        assertEquals(40,hpDone.numbers[0])
        assertEquals(.4f,hpDone.ratios.getValue(0),.0001f)
        assertTrue(hpDone.numbers.getValue(4)<20)
        val done = SettlementValueAnimation.frame(values,1.01f)
        assertEquals(20,done.numbers[4])
        assertEquals(.2f,done.ratios.getValue(4),.0001f)
    }
    @Test fun `healing MP and both equipment rows animate in parallel`() {
        val values=listOf(Value(0,10,70,100),Value(1,5,15,20),Value(3,10,20,100),Value(4,20,30,100))
        val frame=SettlementValueAnimation.frame(values,.21f)
        for (value in values) assertTrue(frame.numbers.getValue(value.index)>value.source)
        val initial=SettlementValueAnimation.frame(values,-.1f)
        for(value in values) assertEquals(value.source,initial.numbers[value.index])
    }
}
