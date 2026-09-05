package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class TerminalSceneFlowTest {
    @Test
    fun `welcome root and create route preserve source flag`() {
        val flow = TerminalSceneFlow(TerminalSceneFlow.Kind.WELCOME)
        assertEquals("Welcome", flow.root.scene)
        assertEquals(
            listOf(TerminalSceneFlow.Drawable("Canvas/Logo_1-1", 1280, 800, "Logo_1-1")),
            flow.root.authoredDrawables,
        )
        flow.onCreate()
        assertEquals(listOf(TerminalSceneFlow.ReplaceScene(flag = 1)), flow.drainRequests())
    }

    @Test
    fun `welcome accepts phases three and five only`() {
        val flow = TerminalSceneFlow(TerminalSceneFlow.Kind.WELCOME)
        listOf(0, 1, 2, 4).forEach(flow::onEvent)
        assertEquals(emptyList(), flow.drainRequests())
        flow.onEvent(3); flow.onEvent(5)
        assertEquals(listOf(
            TerminalSceneFlow.ReplaceScene(flag = 1),
            TerminalSceneFlow.ReplaceScene(flag = 1),
        ), flow.drainRequests())
    }

    @Test
    fun `end root is renderer empty and uses default login replace`() {
        val flow = TerminalSceneFlow(TerminalSceneFlow.Kind.END)
        assertEquals("End", flow.root.scene)
        assertEquals(emptyList(), flow.root.authoredDrawables)
        flow.onCreate()
        assertEquals(listOf(TerminalSceneFlow.ReplaceScene()), flow.drainRequests())
    }

    @Test
    fun `end accepts phase three but not welcome back phase five`() {
        val flow = TerminalSceneFlow(TerminalSceneFlow.Kind.END)
        flow.onEvent(5)
        assertEquals(emptyList(), flow.drainRequests())
        flow.onEvent(3)
        assertEquals(listOf(TerminalSceneFlow.ReplaceScene()), flow.drainRequests())
    }
}
