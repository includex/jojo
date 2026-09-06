// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 장비 강화 fixture 검증: 강화 직전 장비 샘플과 소유자·대상 선택, 오버레이 요청 수명을 확인한다. */
class BattleItemUpgradeRouteFixtureControllerTest {
    /** 강화 경로 설치: 첫 표시 아군 소유자와 첫 표시 적군을 골라 단검 레벨업 직전 샘플을 전달한다. */
    @Test
    fun `장비 강화 경로에 소유자 대상과 레벨업 직전 샘플을 전달한다`() {
        val commands = RecordingCommands()

        assertTrue(BattleItemUpgradeRouteFixtureController().install(RuntimeBattleRoute.ITEM_UPGRADE, units(), commands))

        assertEquals(listOf("mark", "seed:owner:target:0:2:유비:1", "open", "opened"), commands.calls)
    }

    /** 후보 필터링: 숨겨진 아군과 숨겨진 적군은 강화 화면의 소유자·경험치 대상으로 고르지 않는다. */
    @Test
    fun `표시된 아군 소유자와 적군 대상만 선택한다`() {
        val commands = RecordingCommands()
        val routeUnits = listOf(
            unit(id = "hidden-owner", characterId = 1, visible = false, playerSide = true),
            unit(id = "owner", characterId = 2, playerSide = true),
            unit(id = "hidden-target", visible = false, enemySide = true),
            unit(id = "target", enemySide = true),
        )

        BattleItemUpgradeRouteFixtureController().install(RuntimeBattleRoute.ITEM_UPGRADE, routeUnits, commands)

        assertEquals("seed:owner:target:0:2:유비:1", commands.calls[1])
    }

    /** 중복·비지원 경로 방지: 다른 경로나 두 번째 설치에서는 캠페인 장비와 강화창을 다시 건드리지 않는다. */
    @Test
    fun `장비 강화 fixture를 한 번만 설치한다`() {
        val controller = BattleItemUpgradeRouteFixtureController()
        val commands = RecordingCommands()

        assertFalse(controller.install(RuntimeBattleRoute.MAGICK_LIST, units(), commands))
        assertTrue(controller.install(RuntimeBattleRoute.ITEM_UPGRADE, units(), commands))
        assertFalse(controller.install(RuntimeBattleRoute.ITEM_UPGRADE, units(), commands))
        assertEquals(listOf("mark", "seed:owner:target:0:2:유비:1", "open", "opened"), commands.calls)
    }

    /** 필수 대상 검증: 적격 장비 소유자나 적군 대상이 없으면 기존 경로 계약과 같은 오류를 발생시킨다. */
    @Test
    fun `강화 소유자와 적군 대상이 없으면 설치를 중단한다`() {
        val commands = RecordingCommands()

        val error = assertFailsWith<IllegalStateException> {
            BattleItemUpgradeRouteFixtureController().install(
                RuntimeBattleRoute.ITEM_UPGRADE,
                listOf(unit(id = "owner", characterId = 2, playerSide = true)),
                commands,
            )
        }

        assertEquals("ItemUpgrade actual route has no enemy target", error.message)
        assertEquals(listOf("mark"), commands.calls)
    }

    /** 기본 전장 샘플: 장비 소유자와 경험치 제공 적군을 포함한 fixture용 최소 전장 구성이다. */
    private fun units() = listOf(
        unit(id = "owner", characterId = 2, playerSide = true),
        unit(id = "target", enemySide = true),
    )

    /** 유닛 생성기: 테스트마다 필요한 표시·진영·캐릭터 조건만 간결하게 지정한다. */
    private fun unit(
        id: String,
        characterId: Int? = null,
        visible: Boolean = true,
        playerSide: Boolean = false,
        enemySide: Boolean = false,
    ) = BattleItemUpgradeRouteFixtureController.Unit(id, characterId, visible, playerSide, enemySide)

    /** 명령 기록기: 실제 캠페인 저장소와 오버레이 대신 fixture가 요청한 설정 순서만 보관한다. */
    private class RecordingCommands : BattleItemUpgradeRouteFixtureController.Commands {
        /** 호출 순서: fixture가 전송한 설치·장비 구성·오버레이 요청 목록이다. */
        val calls = mutableListOf<String>()

        override fun markRouteInstalled() {
            calls += "mark"
        }

        override fun seedUpgrade(
            owner: BattleItemUpgradeRouteFixtureController.Unit,
            target: BattleItemUpgradeRouteFixtureController.Unit,
            sample: BattleItemUpgradeRouteFixtureController.UpgradeSample,
        ) {
            calls += "seed:${owner.id}:${target.id}:${sample.itemId}:${sample.oldLevel}:${sample.ownerName}:${sample.gainedExperience}"
        }

        override fun openUpgrade() {
            calls += "open"
        }

        override fun upgradeOpened(): Boolean {
            calls += "opened"
            return true
        }
    }
}
