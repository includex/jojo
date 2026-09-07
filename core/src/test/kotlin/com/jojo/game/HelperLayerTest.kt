// Test
package com.jojo.game
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.domain.campaign.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** HelperLayerTest: HelperLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class HelperLayerTest {
    @Test fun `onCreate retains authored four RichText colours then calls replacement 15`() {
        var flags = -1
        val layer = HelperLayer(object : HelperLayer.Model {
            override fun getInfo() = listOf(
                HelperLayer.Info(1, text = "a"), HelperLayer.Info(2, text = "b"),
                HelperLayer.Info(3, text = "c"), HelperLayer.Info(4, text = "d"), HelperLayer.Info(9, text = "skip")
            )
            override fun replaceSpeInfo(text: String, flagsValue: Int): String { flags = flagsValue; return text }
        })
        val view = layer.onCreate()
        assertEquals(15, flags)
        assertEquals("<color=#000000>a</color><br/><color=#ff0000>b</color><br/><color=#0000ff>c</color><br/><color=#f000f0>d</color><br/><br/>", view.richText)
        assertEquals("Logo_12-1/scrollview/view/content/richtext", view.prefab.richTextPath)
        assertEquals(1, view.prefab.listenerPriority)
        assertTrue(layer.onButtonTouch(1).attached)
        assertFalse(layer.onButtonTouch(HelperLayer.TOUCH_END).attached)
    }

    @Test fun `replaceSpeInfo 15 follows source unit global line and colour substitutions`() {
        assertEquals(
            "&7<br/>관우 12 <color=#ff0000>적</color>",
            SourceInfoText.replace("&*.7\n*.9 *12 [C01적]", unitName = { "관우" }, global = { 12 }, colors = listOf("#0000ff", "#ff0000"))
        )
    }

    @Test fun `choice rows render R_00 setup menu the way the source ChooseLayer does`() {
        // R_00.py:374의 원본 문자열이다. 원본은 gvars[4054]=1000..1002 뒤의 infoTransfer(0, ...)로
        // 세 이름을 기록해 두고, ChooseLayer가 replaceSpeInfo로 치환해 보여 준다.
        val names = mapOf(1000 to "과일 재배 방식", 1001 to "여가", 1002 to "닫기 1")
        val plain = { line: String ->
            SourceInfoText.plain(
                line,
                unitName = { id -> names[id].orEmpty().takeWhile { !it.isDigit() } },
                global = { id -> if (id == 2) 100 else 0 },
            )
        }
        assertEquals("훈련 모드 [과일 재배 방식]", plain("훈련 모드 [[C28*.1000]]"))
        assertEquals("난이도 설정 [여가100%]", plain("난이도 설정 [[C28*.1001*/2%]]"))
        assertEquals("능력 재계산 [닫기  재계산]", plain("능력 재계산 [[C28*.1002 재계산]]"))
        // 원본 정규식의 두 번째 그룹은 색 코드 뒤 공백까지 포함하므로 앞 공백이 남는다.
        assertEquals(" 게임 시작", plain("[C3A 게임 시작]"))
    }

    @Test fun `campaign info replaces all currently open original rows`() {
        val campaign = CampaignState()
        campaign.info(2, "첫\n줄")
        campaign.info(3, "둘째")
        assertEquals(1, campaign.extraInfo.size)
        assertEquals(2, campaign.extraInfo.single().type)
        assertEquals("둘째", campaign.extraInfo.single().text)
    }
}
