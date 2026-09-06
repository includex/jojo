// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.domain.battle.Faction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 전투 명령 fixture 검증: 경로별 재고·적격 아군 선택·명령 후속 입력이 화면 명령으로 한 번씩 전달되는지 확인한다. */
class BattleCommandRouteFixtureControllerTest {
    /** 초기와 아이템 경로: 재고를 비운 뒤 보충하고 현재 진영 아군을 선택해 명령과 아이템 입력을 연다. */
    @Test
    fun `초기와 아이템 경로는 재고 보충 후 현재 진영 유닛을 선택한다`() {
        val initial = RecordingCommands()
        val property = RecordingCommands()

        assertTrue(BattleCommandRouteFixtureController().install(
            RuntimeBattleRoute.COMMAND_INITIAL,
            mixedUnits(),
            Faction.FRIEND,
            initial,
        ))
        assertTrue(BattleCommandRouteFixtureController().install(
            RuntimeBattleRoute.COMMAND_PROPERTY,
            mixedUnits(),
            Faction.FRIEND,
            property,
        ))

        assertEquals(listOf("clear", "seed", "open:friend"), initial.calls)
        assertEquals(listOf("clear", "seed", "open:friend", "property"), property.calls)
    }

    /** 마법 경로: 마법 보유 아군만 후보로 삼고 재고 보충 없이 마법 입력을 마지막에 전달한다. */
    @Test
    fun `마법 경로는 마법 보유 아군을 선택한다`() {
        val commands = RecordingCommands()

        BattleCommandRouteFixtureController().install(
            RuntimeBattleRoute.COMMAND_MAGICK,
            listOf(unit("no-magic", Faction.PLAYER), unit("magic", Faction.FRIEND, hasMagic = true)),
            Faction.PLAYER,
            commands,
        )

        assertEquals(listOf("clear", "open:magic", "magic"), commands.calls)
    }

    /** 취소와 비활성 경로: 공통 선택까지 마친 뒤 취소만 보내거나 명령 레이어를 그대로 유지한다. */
    @Test
    fun `취소와 비활성 경로는 각자 필요한 마지막 입력만 보낸다`() {
        val cancel = RecordingCommands()
        val disabled = RecordingCommands()

        BattleCommandRouteFixtureController().install(RuntimeBattleRoute.COMMAND_CANCEL, mixedUnits(), Faction.PLAYER, cancel)
        BattleCommandRouteFixtureController().install(RuntimeBattleRoute.COMMAND_DISABLED, mixedUnits(), Faction.PLAYER, disabled)

        assertEquals(listOf("clear", "open:player", "cancel"), cancel.calls)
        assertEquals(listOf("clear", "open:player"), disabled.calls)
    }

    /** 후보 우선순위: 현재 진영 적격 유닛을 우선하며 없으면 먼저 발견한 아군으로 대체한다. */
    @Test
    fun `현재 진영을 우선하고 없으면 첫 아군을 선택한다`() {
        val preferred = RecordingCommands()
        val fallback = RecordingCommands()

        BattleCommandRouteFixtureController().install(
            RuntimeBattleRoute.COMMAND_INITIAL,
            mixedUnits(),
            Faction.FRIEND,
            preferred,
        )
        BattleCommandRouteFixtureController().install(
            RuntimeBattleRoute.COMMAND_INITIAL,
            listOf(unit("friend", Faction.FRIEND), unit("player", Faction.PLAYER)),
            Faction.REINFORCEMENTS,
            fallback,
        )

        assertEquals("open:friend", preferred.calls.last())
        assertEquals("open:friend", fallback.calls.last())
    }

    /** 예외와 설치 수명 주기: 적격 아군이 없으면 기존 오류를 유지하고 한 번 설치한 fixture는 다시 실행하지 않는다. */
    @Test
    fun `적격 유닛이 없으면 실패하고 fixture는 한 번만 설치한다`() {
        val missing = assertFailsWith<IllegalStateException> {
            BattleCommandRouteFixtureController().install(
                RuntimeBattleRoute.COMMAND_MAGICK,
                listOf(unit("enemy", Faction.ENEMY, hasMagic = true)),
                Faction.PLAYER,
                RecordingCommands(),
            )
        }
        assertEquals("Battle command actual route has no eligible allied unit", missing.message)

        val controller = BattleCommandRouteFixtureController()
        val commands = RecordingCommands()
        assertFalse(controller.install(RuntimeBattleRoute.MAGICK_LIST, mixedUnits(), Faction.PLAYER, commands))
        assertTrue(controller.install(RuntimeBattleRoute.COMMAND_INITIAL, mixedUnits(), Faction.PLAYER, commands))
        assertFalse(controller.install(RuntimeBattleRoute.COMMAND_CANCEL, mixedUnits(), Faction.PLAYER, commands))
        assertEquals(listOf("clear", "seed", "open:player"), commands.calls)
    }

    /** 혼합 유닛: 숨김·적군·아군·현재 진영 후보를 함께 두어 실제 적격 조건을 검증한다. */
    private fun mixedUnits() = listOf(
        unit("hidden", Faction.PLAYER, visible = false),
        unit("enemy", Faction.ENEMY),
        unit("player", Faction.PLAYER),
        unit("friend", Faction.FRIEND),
    )

    /** fixture 유닛: 테스트에서 필요한 진영·표시·마법 조건만 지정해 후보 목록을 만든다. */
    private fun unit(
        id: String,
        faction: Faction,
        visible: Boolean = true,
        hasMagic: Boolean = false,
    ) = BattleCommandRouteFixtureController.Unit(id, faction, visible, hasMagic)

    /** 명령 기록기: 화면 대신 fixture가 선택한 준비와 입력 순서를 보관한다. */
    private class RecordingCommands : BattleCommandRouteFixtureController.Commands {
        /** 호출 순서: fixture 정책이 화면에 전달한 인벤토리·명령 입력 목록이다. */
        val calls = mutableListOf<String>()

        override fun clearInventory() = calls.add("clear").let { Unit }
        override fun seedInventory() = calls.add("seed").let { Unit }
        override fun openCommand(unit: BattleCommandRouteFixtureController.Unit) = calls.add("open:${unit.id}").let { Unit }
        override fun cancelCommand() = calls.add("cancel").let { Unit }
        override fun openMagicCommand() = calls.add("magic").let { Unit }
        override fun openPropertyCommand() = calls.add("property").let { Unit }
    }
}
