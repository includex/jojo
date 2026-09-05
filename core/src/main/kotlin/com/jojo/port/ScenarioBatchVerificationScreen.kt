package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.utils.JsonReader

/**
 * Boots every recovered R scenario through the same interpreter used by the
 * game screen.  This is deliberately a windowed check: scenario resources
 * are accessed through LibGDX's production file backend, not the host FS.
 */
class ScenarioBatchVerificationScreen : ScreenAdapter() {
    private var ran = false

    override fun render(delta: Float) {
        if (ran) return
        ran = true

        val modules = ScenarioCatalog.rModuleNames()
        check(modules.isNotEmpty()) { "R 시나리오가 번들에 없습니다." }
        val tableNames = listOf("unit", "arms", "posts", "unitPostsSkill", "magic", "item", "itemSkills", "config", "hitarea", "effarea", "defineSkill")
        tableNames.forEach { name ->
            val payload = OriginalDataTableCodec.decode(Gdx.files.internal("maps/data/$name.bin").readBytes())
                ?: error("원본 $name.bin 복호화 또는 MD5 검증에 실패했습니다.")
            check(JsonReader().parse(payload) != null) { "원본 $name.bin JSON을 읽지 못했습니다." }
        }
        var sceneOneCount = 0
        var dialogueCount = 0
        var choiceCount = 0
        var modalCount = 0
        var completedCount = 0
        var fullyPlayedCount = 0
        var mappedEventScenes = 0
        val unhandledCalls = linkedMapOf<String, Int>()
        modules.forEach { module ->
            val runtime = PythonAstRuntime.load(module)
            if ("scene1" !in runtime.functionNames) return@forEach
            sceneOneCount++
            runtime.start("scene1")
            // Head fades and HallUnit moves are source-owned suspensions.
            // Settle only timed animation here; retain the first real input
            // or modal state for an honest presentation-surface count.
            var settleSteps = 0
            while (runtime.state == PlaybackState.DELAY && settleSteps++ < 10_000) runtime.skipDelay()
            if (runtime.stage.backgroundId != 0) {
                check(Gdx.files.internal("maps/${runtime.stage.backgroundId}.jpg").exists()) {
                    "$module 원본 이벤트 배경이 없습니다: ${runtime.stage.backgroundId}"
                }
                mappedEventScenes++
            }
            when (runtime.state) {
                PlaybackState.DIALOGUE -> dialogueCount++
                PlaybackState.CHOICE -> choiceCount++
                PlaybackState.MODAL -> modalCount++
                PlaybackState.COMPLETE -> completedCount++
                PlaybackState.DELAY -> error("$module timed Hall state did not settle")
            }
            // Exercise the same runtime past every dialogue/choice boundary.
            // The first option is deterministic; branch-specific verification
            // remains covered separately by the R_00 branch tests.
            var inputSteps = 0
            while (runtime.state != PlaybackState.COMPLETE && inputSteps++ < 10_000) {
                when (runtime.state) {
                    PlaybackState.DIALOGUE -> runtime.advanceDialogue()
                    PlaybackState.CHOICE -> {
                        // R_00's setup choice loops on row zero.  The source
                        // verifier enters its explicit game-start row before
                        // continuing with the remaining default choices.
                        runtime.currentChoice?.options
                            ?.indexOfFirst { it.contains("게임 시작") }
                            ?.takeIf { it >= 0 }
                            ?.let(runtime::selectChoice)
                        runtime.confirmChoice()
                    }
                    PlaybackState.DELAY -> runtime.skipDelay()
                    PlaybackState.MODAL -> runtime.resumeModal()
                    PlaybackState.COMPLETE -> Unit
                }
            }
            check(runtime.state == PlaybackState.COMPLETE) { "$module 이벤트 재생이 종료되지 않았습니다." }
            fullyPlayedCount++
            runtime.unhandledCalls.forEach { (call, count) -> unhandledCalls[call] = (unhandledCalls[call] ?: 0) + count }
        }
        check(sceneOneCount == modules.size) { "scene1 누락: ${modules.size - sceneOneCount}개" }
        check(dialogueCount + choiceCount + modalCount + completedCount == modules.size) { "시나리오 기동 수 집계가 일치하지 않습니다." }
        Gdx.app.log(
            "JojoPort",
            "VERIFY_ALL_SCENARIOS_OK: ${modules.size} R scenarios booted; " +
                "first settled states dialogue=$dialogueCount choice=$choiceCount modal=$modalCount complete=$completedCount; " +
                "$fullyPlayedCount completed one full input branch, $mappedEventScenes loaded original Mmap backgrounds; ${tableNames.size} original gameplay tables decoded"
        )
        val battleModules = ScenarioCatalog.sModuleNames()
        val originalGameData = OriginalGameData.load()
        // Validate the production resolver across every original unit/profile
        // combination before scenario construction selects any battle atlas.
        BattleAvatarConformance.verify()
        val magicProfiles = originalGameData.allMagicProfiles()
        Gdx.app.log(
            "JojoPort",
            "ORIGINAL_MAGIC_COVERAGE: ${magicProfiles.size} strategies; types=" +
                magicProfiles.groupingBy { it.type }.eachCount().toSortedMap().entries.joinToString { "${it.key}:${it.value}" } +
                "; categories=" + magicProfiles.groupingBy { it.category }.eachCount().toSortedMap().entries.joinToString { "${it.key}:${it.value}" },
        )
        Gdx.app.log(
            "JojoPort",
            "ORIGINAL_MAGIC_SPECIAL_TYPES: " + magicProfiles.filter { it.type >= 21 }
                .groupBy { it.type }.toSortedMap().entries.joinToString("; ") { (type, spells) ->
                    "$type=${spells.joinToString { "${it.id}:${it.name}/c${it.category}" }}"
                },
        )
        Gdx.app.log("JojoPort", "ORIGINAL_TERRAIN_MAGIC_FLAGS: " + (0..20).joinToString { "$it:${originalGameData.terrainMagicFlag(it)}" })
        Gdx.app.log("JojoPort", "ORIGINAL_CONFIG_KEYS: ${originalGameData.configTopLevelKeys()}")
        Gdx.app.log("JojoPort", "ORIGINAL_MOVEMENT_OFFSETS: ${originalGameData.hitAreaProfile(0)?.offsets}")
        // Exercise the actual first-campaign route, rather than inspecting
        // S_00 with an empty Model roster. R_00 establishes the persistent
        // party that createMine(idx) resolves in the Yingchuan battle.
        val yingchuanCampaign = CampaignState()
        val yingchuanPrelude = PythonAstRuntime.load("R_00", yingchuanCampaign).apply { start("scene1") }
        var yingchuanInputSteps = 0
        while (yingchuanPrelude.state != PlaybackState.COMPLETE && yingchuanInputSteps++ < 10_000) {
            when (yingchuanPrelude.state) {
                PlaybackState.DIALOGUE -> yingchuanPrelude.advanceDialogue()
                PlaybackState.CHOICE -> {
                    // The R_00 setup menu's first row deliberately loops to
                    // configure training mode.  Continue through the source
                    // campaign route by choosing its explicit game-start row.
                    yingchuanPrelude.currentChoice?.options
                        ?.indexOfFirst { it.contains("게임 시작") }
                        ?.takeIf { it >= 0 }
                        ?.let(yingchuanPrelude::selectChoice)
                    yingchuanPrelude.confirmChoice()
                }
                PlaybackState.DELAY -> yingchuanPrelude.skipDelay()
                PlaybackState.MODAL -> yingchuanPrelude.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
        check(yingchuanPrelude.state == PlaybackState.COMPLETE) { "R_00 영천 진입 이벤트를 끝까지 재생하지 못했습니다." }
        // The source opens BattlePreparation after R_00; joining a character
        // and assigning a battle-roster slot are intentionally separate.
        // Mirror the preparation screen's deterministic first-slot choice.
        check(yingchuanCampaign.joinedUnits.isNotEmpty()) { "R_00 영입 유닛이 생성되지 않았습니다." }
        if (yingchuanCampaign.battleRoster.isEmpty()) {
            yingchuanCampaign.battleRoster += yingchuanCampaign.joinedUnits.take(15)
        }
        val yingchuanRuntime = PythonAstRuntime.load("S_00", yingchuanCampaign).apply { start("scene0") }
        fun drainYingchuan(runtime: PythonAstRuntime) {
            var steps = 0
            while (runtime.state != PlaybackState.COMPLETE && steps++ < 10_000) {
                when (runtime.state) {
                    PlaybackState.DIALOGUE -> runtime.advanceDialogue()
                    PlaybackState.CHOICE -> runtime.confirmChoice()
                    PlaybackState.DELAY -> runtime.skipDelay()
                    PlaybackState.MODAL -> runtime.resumeModal()
                    PlaybackState.COMPLETE -> Unit
                }
            }
            check(runtime.state == PlaybackState.COMPLETE) { "영천 원본 이벤트 대사가 종료되지 않았습니다." }
        }
        // Preserve the production lifecycle: start() replaces the current
        // AST stack, so scene1 must never be started while scene0 is paused.
        drainYingchuan(yingchuanRuntime)
        yingchuanRuntime.setBattleContext(PythonAstRuntime.BattleScriptContext(round = 1, camp = -1))
        // The battle does not accept input until scene1's source startOper().
        // Materialize BattleState after that exact opening sequence, so its
        // positions, visibility and starting HP come from the live stage.
        yingchuanRuntime.start("scene1")
        drainYingchuan(yingchuanRuntime)
        check(yingchuanRuntime.stage.battleOperationStarted) { "S_00 startOper 이전에 전술 입력이 열렸습니다." }
        val yingchuanTerrain = BattleTerrainGrid.load(yingchuanRuntime.stage.battleMapIndex)
        val yingchuanBattle = BattleScenarioFactory.fromScriptedUnits(
            yingchuanRuntime.stage.battleUnits.values,
            yingchuanRuntime.stage.mapObjects.values.filter { it.enabled && it.objectId > 3 }.mapTo(linkedSetOf()) { it.x to it.y },
            originalGameData,
            yingchuanTerrain,
            yingchuanRuntime.stage.enemyMasterInstanceId,
            yingchuanRuntime.stage.initialBattleWeather(),
            yingchuanRuntime.stage.battleWeatherSchedule(),
            yingchuanRuntime.stage.battleWeatherOffset,
            yingchuanRuntime.stage.enemyEquipment,
            yingchuanCampaign,
        )
        check(yingchuanBattle.units.values.any { it.faction == Faction.PLAYER }) { "R_00→S_00 플레이어 전투 유닛이 생성되지 않았습니다." }
        // Advance FRIEND, ENEMY, and (when populated) REINFORCEMENTS once
        // through the production AI resolver.
        // resolver. Player input is intentionally not fabricated here.
        var yingchuanAiActions = 0
        for (ignored in 0 until 3) {
            val turn = yingchuanBattle.endTurn()
            if (turn.activeFaction == Faction.PLAYER) break
            val ai = yingchuanBattle.resolveAiTurn()
            yingchuanAiActions += ai.moves + ai.attacks + ai.holds
        }
        // S_00 opening placements deliberately give its first automated
        // camps no reachable target; zero actions is a valid source wait.
        check(yingchuanBattle.units.values.any { it.faction == Faction.FRIEND } &&
            yingchuanBattle.units.values.any { it.faction.isEnemySide() }) {
            "영천 전투 자동 진영이 생성되지 않았습니다."
        }
        // S_00 wins only after Zhang Bao (146) and Zhang Liang (147) are
        // defeated. Drive their HP through BattleState's source-equivalent
        // scripted attack resolver, then transfer the resulting live state
        // into StageLayer.unitStateTest (attribute 7 = HP). This deliberately
        // avoids fabricating a zero-HP script context.
        val bossIds = listOf(146, 147)
        // S_00 starts Cao Cao's Mine roster hidden; the units which its
        // opening scene exposes for this battle are Liu Bei's allied force.
        // Both are allied to the player in the original camp model.
        val attacker = yingchuanBattle.units.values.firstOrNull {
            it.visible && (it.faction == Faction.PLAYER || it.faction == Faction.FRIEND)
        } ?: error("영천 전투 시작 후 사용할 수 있는 아군 전투 유닛이 없습니다.")
        bossIds.forEach { bossId ->
            repeat(256) {
                val boss = yingchuanBattle.units.values.firstOrNull { it.sourceCharacterId == bossId } ?: return@repeat
                yingchuanBattle.forcedAttack(attacker.id, boss.id)
            }
            check(yingchuanBattle.units.values.none { it.sourceCharacterId == bossId }) {
                "영천 보스 $bossId 가 실제 전투 피해로 격파되지 않았습니다."
            }
        }
        val defeatedBossAttributes = bossIds.associateWith { bossId ->
            val hp = yingchuanBattle.units.values.firstOrNull { it.sourceCharacterId == bossId }?.hitPoints ?: 0
            mapOf(7 to hp)
        }
        yingchuanRuntime.setBattleContext(
            PythonAstRuntime.BattleScriptContext(round = 2, camp = 2, attributes = defeatedBossAttributes),
        )
        yingchuanRuntime.start("scene1")
        drainYingchuan(yingchuanRuntime)
        check(yingchuanRuntime.stage.unit(146).visible.not() && yingchuanRuntime.stage.unit(147).visible.not()) {
            "영천 보스 격파 상태가 원본 stage.unitStateTest 분기에 반영되지 않았습니다."
        }
        yingchuanRuntime.setBattleContext(
            PythonAstRuntime.BattleScriptContext(round = 2, camp = 2, attributes = defeatedBossAttributes),
        )
        yingchuanRuntime.start("scene1")
        drainYingchuan(yingchuanRuntime)
        check(yingchuanRuntime.stage.scriptedBattleOutcome == BattleOutcome.PLAYER_VICTORY && yingchuanRuntime.stage.battleEndedByScript) {
            "영천 원본 승리 조건(146/147 격파) 이후 reward/end에 도달하지 못했습니다."
        }
        val yingchuanVisibleCamps = Faction.entries.joinToString { faction ->
            "$faction=${yingchuanBattle.units.values.count { it.visible && it.faction == faction }}"
        }
        Gdx.app.log("JojoPort", "VERIFY_YINGCHUAN_ROUTE_OK: roster=${yingchuanCampaign.battleRoster.size}, units=${yingchuanBattle.units.size}, visible=[$yingchuanVisibleCamps], aiActions=$yingchuanAiActions, bossWin=end")
        var initializedBattles = 0
        var mappedBattles = 0
        var liveBattleEvents = 0
        var routedBattleEndings = 0
        var materializedBattleUnits = 0
        var equippedBattleUnits = 0
        var automatedCampResolutions = 0
        battleModules.forEach { module ->
            val runtime = PythonAstRuntime.load(module)
            check("scene0" in runtime.functionNames) { "$module scene0이 없습니다." }
            runtime.start("scene0")
            runtime.unhandledCalls.forEach { (call, count) -> unhandledCalls[call] = (unhandledCalls[call] ?: 0) + count }
            if (runtime.stage.battleUnits.isNotEmpty()) initializedBattles++
            val terrain = BattleTerrainGrid.load(runtime.stage.battleMapIndex)
            val tacticalState = BattleScenarioFactory.fromScriptedUnits(
                runtime.stage.battleUnits.values,
                runtime.stage.mapObjects.values.filter { it.enabled }.mapTo(linkedSetOf()) { it.x to it.y },
                originalGameData,
                terrain,
                runtime.stage.enemyMasterInstanceId,
                runtime.stage.initialBattleWeather(),
                runtime.stage.battleWeatherSchedule(),
                runtime.stage.battleWeatherOffset,
                runtime.stage.enemyEquipment,
            )
            check(tacticalState.units.size == runtime.stage.battleUnits.size) { "$module 원본 전투 유닛 변환 수가 다릅니다." }
            runtime.stage.battleUnits.values.forEach { scripted ->
                val profile = requireNotNull(originalGameData.unitProfile(scripted.characterId)) {
                    "$module 원본 캐릭터 데이터가 없습니다: ${scripted.characterId}"
                }
                val battleProfile = requireNotNull(originalGameData.battleProfile(scripted.characterId, scripted.level)) {
                    "$module 원본 전투 능력치를 만들지 못했습니다: ${scripted.characterId}"
                }
                val tactical = requireNotNull(tacticalState.units[scripted.battleId])
                val equippedValues = runtime.stage.enemyEquipment[scripted.characterId].orEmpty()
                val scriptedEquipment = originalGameData.equipmentBonus(equippedValues, battleProfile.level)
                val defaultEquipment = originalGameData.defaultEquipmentBonus(battleProfile.posts, battleProfile.level)
                val equipment = OriginalGameData.EquipmentBonus(
                    attack = if (equippedValues.getOrElse(0) { 0 } > 1) scriptedEquipment.attack else defaultEquipment.attack,
                    defense = if (equippedValues.getOrElse(2) { 0 } > 1) scriptedEquipment.defense else defaultEquipment.defense,
                    spirit = if (equippedValues.getOrElse(0) { 0 } > 1) scriptedEquipment.spirit else defaultEquipment.spirit,
                )
                val mergedSkills = originalGameData.mergeSkills(
                    originalGameData.skillsForUnit(scripted.characterId, battleProfile.posts),
                    originalGameData.equipmentSkills(equippedValues, battleProfile.level),
                )
                val rangeSkill = mergedSkills[258]?.and(255) ?: 255
                val sourceHitArea = rangeSkill.takeIf { it != 255 }
                    ?.let(originalGameData::hitAreaProfile)
                    ?: battleProfile.hitArea
                val expectedHitArea = if ((mergedSkills[260]?.and(255) ?: 255) != rangeSkill) {
                    originalGameData.hitAreaProfile(sourceHitArea.upgradeId) ?: sourceHitArea
                } else sourceHitArea
                fun expectedAbility(base: Int, sourceBase: Int, passiveSkill: Int): Int {
                    val smft = mergedSkills[190]?.and(255)?.takeIf { it != 255 } ?: 0
                    return originalGameData.passiveAbility(maxOf(base, sourceBase + smft * battleProfile.level), passiveSkill, mergedSkills)
                }
                val conversionMatches =
                    tactical.id == scripted.battleId &&
                        tactical.sourceCharacterId == scripted.characterId &&
                        tactical.faction == when (scripted.faction) {
                            ScenarioUnitFaction.MINE -> Faction.PLAYER
                            ScenarioUnitFaction.FRIEND -> Faction.FRIEND
                            ScenarioUnitFaction.ENEMY -> if (scripted.reinforcement) Faction.REINFORCEMENTS else Faction.ENEMY
                        } &&
                        tactical.tileX == scripted.x && tactical.tileY == scripted.y &&
                        tactical.direction == scripted.direction && tactical.visible == !scripted.hidden &&
                        tactical.ai == scripted.ai &&
                        tactical.aiTargetCharacterId == scripted.aiTargetId &&
                        tactical.aiTargetX == scripted.aiTargetX && tactical.aiTargetY == scripted.aiTargetY &&
                        tactical.name == profile.name &&
                        tactical.maxHitPoints == originalGameData.passiveAbility(battleProfile.maxHitPoints, 52, mergedSkills) &&
                        tactical.maxMagicPoints == originalGameData.passiveAbility(battleProfile.maxMagicPoints, 53, mergedSkills) &&
                        tactical.attack == expectedAbility(battleProfile.attack + equipment.attack, profile.attack, 65) &&
                        tactical.defense == expectedAbility(battleProfile.defense + equipment.defense, profile.defense, 61) &&
                        tactical.spirit == expectedAbility(battleProfile.spirit + equipment.spirit, profile.spirit, 68) &&
                        tactical.critical == expectedAbility(battleProfile.critical, profile.critical, 54) &&
                        tactical.morale == expectedAbility(battleProfile.morale, profile.morale, 73) &&
                        tactical.movement == battleProfile.movement + (mergedSkills[77]?.and(255)?.takeIf { it != 255 } ?: 0) &&
                        tactical.attackOffsets == expectedHitArea.offsets &&
                        tactical.attackAllScreen == expectedHitArea.allScreen &&
                        tactical.magic.map { it.id } == battleProfile.magic.map { it.id } &&
                        mergedSkills.all { (skillId, effect) -> tactical.skills[skillId] == effect } &&
                        tactical.armRestraints[battleProfile.arm.id] == battleProfile.arm.restraintAgainst(battleProfile.arm.id)
                check(conversionMatches) {
                    "$module 원본 능력치 변환이 일치하지 않습니다: ${scripted.characterId}; " +
                        "actual=hp${tactical.maxHitPoints}/mp${tactical.maxMagicPoints}/att${tactical.attack}/def${tactical.defense}/" +
                        "spr${tactical.spirit}/cri${tactical.critical}/mor${tactical.morale}/mov${tactical.movement}/" +
                        "magic${tactical.magic.map { it.id }}/offsets${tactical.attackOffsets}; " +
                        "expected=hp${originalGameData.passiveAbility(battleProfile.maxHitPoints, 52, mergedSkills)}/" +
                        "mp${originalGameData.passiveAbility(battleProfile.maxMagicPoints, 53, mergedSkills)}/" +
                        "att${expectedAbility(battleProfile.attack + equipment.attack, profile.attack, 65)}/" +
                        "def${expectedAbility(battleProfile.defense + equipment.defense, profile.defense, 61)}/" +
                        "spr${expectedAbility(battleProfile.spirit + equipment.spirit, profile.spirit, 68)}/" +
                        "cri${expectedAbility(battleProfile.critical, profile.critical, 54)}/" +
                        "mor${expectedAbility(battleProfile.morale, profile.morale, 73)}/" +
                        "mov${battleProfile.movement + (mergedSkills[77]?.and(255)?.takeIf { it != 255 } ?: 0)}/" +
                        "magic${battleProfile.magic.map { it.id }}/offsets${expectedHitArea.offsets}"
                }
                if (runtime.stage.enemyEquipment.containsKey(scripted.characterId)) equippedBattleUnits++
                materializedBattleUnits++
            }
            // Enter every automatic camp through the production resolver.
            // This remains headless and does not invent player commands;
            // it proves that each recovered script's initialized AI fields,
            // positions, ranges and unit resources can enter Control.js's
            // port without an exception or a synthetic UI dependency.
            for (ignored in 0 until 3) {
                val turn = tacticalState.endTurn()
                if (turn.activeFaction == Faction.PLAYER) break
                tacticalState.resolveAiTurn()
                automatedCampResolutions++
            }
            val mapId = runtime.stage.battleMapIndex + 1
            if (sequenceOf("jpg", "png", "webp").any { Gdx.files.internal("maps/battle-maps/$mapId.$it").exists() } && terrain.width > 0 && terrain.height > 0) mappedBattles++
            if ("scene1" in runtime.functionNames) {
                runtime.setBattleContext(PythonAstRuntime.BattleScriptContext(round = 1, camp = 1))
                runtime.start("scene1")
                if (runtime.state == PlaybackState.DIALOGUE || runtime.state == PlaybackState.CHOICE) liveBattleEvents++
                runtime.unhandledCalls.forEach { (call, count) -> unhandledCalls[call] = (unhandledCalls[call] ?: 0) + count }
            }
            if ("scene2" in runtime.functionNames) {
                runtime.start("scene2")
                var inputSteps = 0
                while (runtime.state != PlaybackState.COMPLETE && inputSteps++ < 1_000) {
                    when (runtime.state) {
                        PlaybackState.DIALOGUE -> runtime.advanceDialogue()
                        PlaybackState.CHOICE -> runtime.confirmChoice()
                        PlaybackState.DELAY -> runtime.skipDelay()
                        PlaybackState.MODAL -> runtime.resumeModal()
                        PlaybackState.COMPLETE -> Unit
                    }
                }
                check(runtime.state == PlaybackState.COMPLETE) { "$module 전투 종료 전환이 완료되지 않았습니다." }
                if (runtime.stage.sceneJumpTarget != null) routedBattleEndings++
                runtime.unhandledCalls.forEach { (call, count) -> unhandledCalls[call] = (unhandledCalls[call] ?: 0) + count }
            }
        }
        check(initializedBattles == battleModules.size) {
            "유닛을 생성하지 못한 S 전투 스크립트: ${battleModules.size - initializedBattles}개"
        }
        check(mappedBattles == battleModules.size) {
            "원본 맵 이미지를 찾지 못한 S 전투 스크립트: ${battleModules.size - mappedBattles}개"
        }
        Gdx.app.log(
            "JojoPort",
            "VERIFY_ALL_BATTLES_OK: ${battleModules.size} S battle scripts initialized units and original maps; " +
                "$liveBattleEvents emitted a live round-1 dialogue/choice event; " +
                "$routedBattleEndings exercised original scene2 jump routes; " +
                "$materializedBattleUnits units received verified original profiles; " +
                "$equippedBattleUnits scripted equipment profiles were applied; " +
                "$automatedCampResolutions automatic FRIEND/ENEMY/REINFORCEMENTS camp entries resolved"
        )
        val topUnhandled = unhandledCalls.entries.sortedByDescending { it.value }.take(12)
            .joinToString { "${it.key}=${it.value}" }
        Gdx.app.log("JojoPort", "AST_API_GAPS: ${if (topUnhandled.isBlank()) "none" else topUnhandled}")
        Gdx.app.exit()
    }
}
