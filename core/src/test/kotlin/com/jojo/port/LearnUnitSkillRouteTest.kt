package com.jojo.port
import kotlin.test.*
class LearnUnitSkillRouteTest{
 @Test fun `edit4 button4 gate uses touch end`(){val r=EditRosterLearnRoute(true);assertFalse(r.button(4,false));assertFalse(r.button(3,true));assertTrue(r.button(4,true));assertEquals(EditRosterLearnRoute.State.LEARN,r.state)}
 @Test fun `initial unavailable unit selection opens page20 and actual second row is 1001`(){val f=LearnUnitSkillFlow();assertEquals(LearnUnitSkillFlow.Effect.OpenSelectList(1024,20),f.panelButton(0,0).single());f.selectListResult(1001);assertEquals(listOf(LearnUnitSkillFlow.Effect.SetUnit0(1001)),f.save());assertEquals(1001,f.unit0)}
 @Test fun `negative list result is cancel and close returns parent`(){val f=LearnUnitSkillFlow();f.selectListResult(-1);assertTrue(f.save().isEmpty());assertEquals(listOf(LearnUnitSkillFlow.Effect.Close),f.close())}
}
