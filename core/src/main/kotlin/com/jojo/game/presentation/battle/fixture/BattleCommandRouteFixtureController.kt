// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.isPlayerSide

/** 전투 명령 경로 fixture 조정기: 명령 캡처가 요구하는 인벤토리, 사용 유닛, 메뉴 후속 입력을 실제 화면 명령으로 조립한다. */
internal class BattleCommandRouteFixtureController {
    /** 설치 여부: 같은 캡처 프레임에서 유닛 선택과 명령 입력이 다시 실행되는 것을 막는다. */
    private var installed = false

    /** 경로 설치: 경로 정책에 맞춰 인벤토리와 적격 아군을 준비하고 필요한 명령 후속 동작을 요청한다. */
    fun install(
        route: RuntimeBattleRoute?,
        units: Collection<Unit>,
        activeFaction: Faction,
        commands: Commands,
    ): Boolean {
        if (route !in SUPPORTED_ROUTES || installed) return false
        installed = true
        commands.clearInventory()
        if (route in INVENTORY_ROUTES) commands.seedInventory()

        /**
         * `needsMagic` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val needsMagic = route == RuntimeBattleRoute.COMMAND_MAGICK
        /**
         * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unit = units
            .asSequence()
            .filter { it.visible && it.faction.isPlayerSide() && (!needsMagic || it.hasMagic) }
            .firstOrNull { it.faction == activeFaction }
            ?: units.firstOrNull { it.visible && it.faction.isPlayerSide() && (!needsMagic || it.hasMagic) }
            ?: error("Battle command actual route has no eligible allied unit")
        commands.openCommand(unit)
        when (route) {
            RuntimeBattleRoute.COMMAND_CANCEL -> commands.cancelCommand()
            RuntimeBattleRoute.COMMAND_MAGICK -> commands.openMagicCommand()
            RuntimeBattleRoute.COMMAND_PROPERTY -> commands.openPropertyCommand()
            RuntimeBattleRoute.COMMAND_INITIAL,
            RuntimeBattleRoute.COMMAND_DISABLED,
            -> Unit

            else -> error("지원하지 않는 전투 명령 fixture 경로: $route")
        }
        return true
    }

    /** 명령 fixture 유닛: 적격 아군과 현재 진영 우선순위를 계산하는 데 필요한 최소 전장 정보다. */
    data class Unit(
        /** 유닛 ID: 화면 명령이 실제 전장 유닛을 다시 찾는 키다. */
        val id: String,
        /** 유닛 진영: 현재 조작 진영 우선순위와 아군 적격 여부를 판별한다. */
        val faction: Faction,
        /** 표시 여부: 숨겨진 유닛은 명령 캡처의 선택 후보에서 제외한다. */
        val visible: Boolean,
        /** 마법 보유 여부: 마법 명령 경로가 선택할 수 있는 유닛인지 결정한다. */
        val hasMagic: Boolean,
    )

    /** 화면 명령: fixture 정책이 실제 전투·입력·레이어 구현에 직접 의존하지 않도록 제공하는 연결 경계다. */
    internal interface Commands {
        /** 인벤토리 비우기: 이전 경로의 소비 아이템이 명령 화면에 남지 않도록 정리한다. */
        fun clearInventory()

        /** 인벤토리 채우기: 아이템 명령과 아이템 수치가 필요한 경로의 초기 재고를 만든다. */
        fun seedInventory()

        /** 명령 열기: 실제 진영 전환, 유닛 초기화, 타일 선택 두 번 입력으로 명령 레이어를 연다. */
        fun openCommand(unit: Unit)

        /** 명령 취소: 실제 취소 버튼 입력을 명령 흐름에 전달한다. */
        fun cancelCommand()

        /** 마법 명령 열기: 실제 마법 버튼 입력과 fixture 마법 목록 레이어 생성을 요청한다. */
        fun openMagicCommand()

        /** 아이템 명령 열기: 실제 아이템 버튼 입력을 명령 흐름에 전달한다. */
        fun openPropertyCommand()
    }

    private companion object {
        /** 지원 경로: 명령 레이어의 초기·비활성·취소·마법·아이템 캡처 상태다. */
        val SUPPORTED_ROUTES = setOf(
            RuntimeBattleRoute.COMMAND_INITIAL,
            RuntimeBattleRoute.COMMAND_DISABLED,
            RuntimeBattleRoute.COMMAND_CANCEL,
            RuntimeBattleRoute.COMMAND_MAGICK,
            RuntimeBattleRoute.COMMAND_PROPERTY,
        )

        /** 인벤토리 경로: 캡처에 소비 아이템 버튼과 수량을 표시해야 하는 명령 상태다. */
        val INVENTORY_ROUTES = setOf(
            RuntimeBattleRoute.COMMAND_INITIAL,
            RuntimeBattleRoute.COMMAND_PROPERTY,
        )
    }
}
