package com.jojo.game
import com.jojo.game.presentation.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleSaveLoadOverlayRendererTest {
    @Test
    fun `save and load snapshots preserve independent modal state`() {
        val rows = listOf(BattleSaveLoadRowView("No.  1", "전역1", "첫 전투"))
        val save = BattleSaveLoadOverlayView(
            kind = BattleSaveLoadOverlayKind.SAVE,
            rows = rows,
            firstRow = 0,
            pendingSave = true,
            saveConfirmation = "진행도 No.1: 저장할 수 있나요?",
        )
        val load = BattleSaveLoadOverlayView(
            kind = BattleSaveLoadOverlayKind.LOAD,
            rows = rows,
            firstRow = 0,
            loadConfirmation = "진행도 No.1: 불러올 수 있나요?",
            loadNotice = "저장 파일이 손실되었습니다!",
        )

        assertEquals(BattleSaveLoadOverlayKind.SAVE, save.kind)
        assertEquals("진행도 No.1: 저장할 수 있나요?", save.saveConfirmation)
        assertEquals(BattleSaveLoadOverlayKind.LOAD, load.kind)
        assertEquals("진행도 No.1: 불러올 수 있나요?", load.loadConfirmation)
        assertEquals("저장 파일이 손실되었습니다!", load.loadNotice)
        assertEquals(rows, save.rows)
        assertEquals(rows, load.rows)
    }
}
