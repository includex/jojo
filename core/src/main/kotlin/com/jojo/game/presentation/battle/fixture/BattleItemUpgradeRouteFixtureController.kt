// Battle Fixture
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.application.runtime.RuntimeBattleRoute

/** 장비 강화 경로 fixture 조정기: 캡처에 필요한 장비 직전 경험치·소유자·대상 유닛을 한 번만 구성한다. */
internal class BattleItemUpgradeRouteFixtureController {
    /** 설치 여부: 매 프레임 장비 경험치와 강화창이 다시 초기화되는 것을 막는다. */
    private var installed = false

    /** 경로 설치: 장비 강화 캡처 경로에서만 적격 유닛을 찾아 고정 장비 샘플과 강화 요청을 화면에 전달한다. */
    fun install(route: RuntimeBattleRoute?, units: Collection<Unit>, commands: Commands): Boolean {
        if (route != RuntimeBattleRoute.ITEM_UPGRADE || installed) return false
        installed = true
        commands.markRouteInstalled()
        val owner = units.firstOrNull { it.visible && it.playerSide && it.characterId != null }
            ?: error("ItemUpgrade actual route has no player equipment owner")
        val target = units.firstOrNull { it.visible && it.enemySide }
            ?: error("ItemUpgrade actual route has no enemy target")
        commands.seedUpgrade(owner, target, UPGRADE_SAMPLE)
        commands.openUpgrade()
        check(commands.upgradeOpened()) { "ItemUpgrade actual route did not level equipment" }
        return true
    }

    /** 전장 유닛: 장비 소유자와 경험치 제공 대상을 고르기 위한 최소 fixture 정보다. */
    data class Unit(
        /** 유닛 ID: 장비 경험치를 적용할 실제 전장 유닛 식별자다. */
        val id: String,
        /** 캐릭터 ID: 캠페인 장비와 이름을 갱신할 소유자 식별자다. */
        val characterId: Int?,
        /** 표시 여부: 숨겨진 유닛은 강화 화면의 소유자·대상 후보에서 제외한다. */
        val visible: Boolean,
        /** 아군 여부: 장비를 장착할 아군 소유자 후보인지 나타낸다. */
        val playerSide: Boolean,
        /** 적군 여부: 장비 경험치를 제공할 전투 대상 후보인지 나타낸다. */
        val enemySide: Boolean,
    )

    /** 강화 샘플: 단검 레벨업 화면에 표시할 고정 장비·이름·획득 경험치 조건이다. */
    data class UpgradeSample(
        /** 장비 ID: 강화할 단검의 데이터 카탈로그 식별자다. */
        val itemId: Int,
        /** 기존 레벨: 레벨업 직전 상태로 맞출 무기 레벨이다. */
        val oldLevel: Int,
        /** 무기 저장 ID: 스테이지 저장 형식에서 단검을 나타내는 압축 식별자다. */
        val weaponStorageId: Int,
        /** 기본 방어구 ID: 소유자 장비가 없을 때 생성할 샘플 방어구 식별자다. */
        val fallbackArmor: Int,
        /** 기본 방어구 레벨: 소유자 장비가 없을 때 생성할 샘플 방어구 레벨이다. */
        val fallbackArmorLevel: Int,
        /** 기본 보조 장비 ID: 소유자 장비가 없을 때 생성할 샘플 보조 장비 식별자다. */
        val fallbackAuxiliary: Int,
        /** 소유자 이름: 강화창에 표시할 fixture 무장 이름이다. */
        val ownerName: String,
        /** 획득 경험치: 레벨업을 발생시킬 전투 경험치 증가량이다. */
        val gainedExperience: Int,
    )

    /** 화면 명령: fixture 정책이 실제 캠페인 저장소·전투 경험치·오버레이 구현에 직접 의존하지 않는 연결 경계다. */
    internal interface Commands {
        /** 설치 기록: 같은 장비 강화 경로가 화면 갱신에서 다시 설치되지 않도록 표시한다. */
        fun markRouteInstalled()

        /** 강화 직전 상태 구성: 실제 캠페인 장비와 소유자 이름, 전투 경험치 적용 대상을 준비한다. */
        fun seedUpgrade(owner: Unit, target: Unit, sample: UpgradeSample)

        /** 강화창 열기: 준비된 장비 경험치 결과를 실제 오버레이 흐름으로 변환한다. */
        fun openUpgrade()

        /** 강화창 확인: 레벨업 결과가 실제 오버레이에 전달됐는지 검증한다. */
        fun upgradeOpened(): Boolean
    }

    private companion object {
        /** 강화 고정값: 단검 레벨 2의 경험치를 한 단계 직전으로 맞추는 원본 캡처 샘플이다. */
        val UPGRADE_SAMPLE = UpgradeSample(
            itemId = 0,
            oldLevel = 2,
            weaponStorageId = 2,
            fallbackArmor = 72,
            fallbackArmorLevel = 1,
            fallbackAuxiliary = 111,
            ownerName = "유비",
            gainedExperience = 1,
        )
    }
}
