package com.jojo.game
import com.jojo.game.application.battle.LearnUnitSkillFlow
import com.jojo.game.application.battle.LearnUnitSkillRoute
import com.jojo.game.application.battle.EditRosterLearnRoute
import com.jojo.game.presentation.battle.edit.LearnUnitSkillRouteScreen
import com.jojo.game.presentation.battle.edit.evidence.LearnUnitSkillRenderEvents
import kotlin.test.*
/**
 * class  `LearnUnitSkillRouteTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class LearnUnitSkillRouteTest{
 @Test fun `edit4 button4 gate uses touch end`(){val r=EditRosterLearnRoute(true);assertFalse(r.button(4,false));assertFalse(r.button(3,true));assertTrue(r.button(4,true));assertEquals(EditRosterLearnRoute.State.LEARN,r.state)}
 @Test fun `initial unavailable unit selection opens page20 and actual second row is 1001`(){val f=LearnUnitSkillFlow();assertEquals(LearnUnitSkillFlow.Effect.OpenSelectList(1024,20),f.panelButton(0,0).single());f.selectListResult(1001);assertEquals(listOf(LearnUnitSkillFlow.Effect.SetUnit0(1001)),f.save());assertEquals(1001,f.unit0)}
 @Test fun `negative list result is cancel and close returns parent`(){val f=LearnUnitSkillFlow();f.selectListResult(-1);assertTrue(f.save().isEmpty());assertEquals(listOf(LearnUnitSkillFlow.Effect.Close),f.close())}
}
