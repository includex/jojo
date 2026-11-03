-- Generated Linchong scenario. Edit the campaign generator to preserve changes.
local function __truth(value)
    return value ~= nil and value ~= false and value ~= 0 and value ~= ""
end

function scene0()
    stage.initFight()
    stage.setGlobalData(11, 0, -1, 512, 1, 1)
    if (not (gvars["linchong.gear_initialized_lin"] ~= 0 and gvars["linchong.gear_initialized_lin"] ~= nil and gvars["linchong.gear_initialized_lin"] ~= false)) then
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars["linchong.gear_initialized_lin"] = 1
    end
    if (not (gvars["linchong.gear_initialized_hu"] ~= 0 and gvars["linchong.gear_initialized_hu"] ~= nil and gvars["linchong.gear_initialized_hu"] ~= false)) then
        stage.setJoinEquip(514, 64, 1, 33, 1, 0)
        gvars["linchong.gear_initialized_hu"] = 1
    end
    if (not (gvars["linchong.gear_initialized_jin"] ~= 0 and gvars["linchong.gear_initialized_jin"] ~= nil and gvars["linchong.gear_initialized_jin"] ~= false)) then
        stage.setJoinEquip(515, 65, 1, 36, 1, 0)
        gvars["linchong.gear_initialized_jin"] = 1
    end
    stage.createMine({{["id"] = 512, ["i"] = 0, ["idx"] = 0, ["x"] = 4, ["y"] = 5, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
        stage.createMine({{["id"] = 514, ["i"] = 1, ["idx"] = 1, ["x"] = 5, ["y"] = 7, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    end
    stage.createEnemy({{["id"] = 524, ["i"] = 3, ["idx"] = 3, ["x"] = 15, ["y"] = 6, ["lv"] = 5, ["dir"] = 2, ["ai"] = 1, ["hide"] = 1}})
    stage.createEnemy({{["id"] = 525, ["i"] = 4, ["idx"] = 4, ["x"] = 16, ["y"] = 5, ["lv"] = 3, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 512, ["hide"] = 1}})
    stage.createEnemy({{["id"] = 548, ["i"] = 5, ["idx"] = 5, ["x"] = 12, ["y"] = 8, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1, ["hide"] = 1}})
    stage.createEnemy({{["id"] = 549, ["i"] = 6, ["idx"] = 6, ["x"] = 14, ["y"] = 9, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1, ["hide"] = 1}})
    stage.createEnemy({{["id"] = 550, ["i"] = 7, ["idx"] = 7, ["x"] = 17, ["y"] = 9, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1, ["hide"] = 1}})
    stage.createFriend({{["id"] = 515, ["i"] = 2, ["idx"] = 2, ["x"] = 8, ["y"] = 8, ["lv"] = 2, ["dir"] = 2, ["ai"] = 2}})
    stage.infoTransfer(4, "100\n1\n512\nwind_spear")
    stage.infoTransfer(4, "103\n1\n514\niron_guard")
    stage.infoTransfer(4, "104\n1\n515\nquick_step")
    stage.loadBg(62)
    stage.draw()
    stage.setWinCondition("승리 조건\n육겸 격퇴; 임충 생존\n\n패배 조건\n임충 HP 0; 불길에 갇힌 임충에게 탈출 경로 없음; 11턴 종료")
    stage.say("@dialogue:S_94.interlude.line001")
    stage.say("@dialogue:S_94.interlude.line002")
    if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
        stage.say("@dialogue:S_94.interlude.line003")
    end
    stage.say("@dialogue:S_94.interlude.line004")
    stage.say("@dialogue:S_94.B04_E00.line001")
    stage.say("@dialogue:S_94.B04_E00.line002")
    stage.say("@dialogue:S_94.B04_E00.line003")
    stage.say("@dialogue:S_94.B04_E00.line004")
    if (not ((gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) and (not __truth(stage.unitStateTest(514, 7, 0, 2))))) then
        stage.say("@dialogue:S_94.B04_E00.HU_ABSENT.line001")
    end
    stage.unit(524).setAI(2)
    stage.unit(525).setAI(2)
    stage.unit(548).setAI(2)
    stage.unit(549).setAI(2)
    stage.unit(550).setAI(2)
    gvars["linchong.event_b04_e00"] = 1
    gvars["linchong.duel_win"] = 0
    gvars["linchong.exit_count"] = 0
    stage.setUnitStatus({{["unit"] = stage.unit(515), ["hp"] = -31}})
    stage.startOper()
    return false
end

function scene1()
    if (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 11)) then
        stage.say("@dialogue:S_94.B04_E99.line001")
        stage["end"]()
        stage.lose()
        return true
    end
    if ((not (gvars["linchong.event_b04_e02"] ~= 0 and gvars["linchong.event_b04_e02"] ~= nil and gvars["linchong.event_b04_e02"] ~= false)) and ((stage.round() >= 3) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b04_e02"] = 1
        stage.say("@dialogue:S_94.B04_E02.line001")
        stage.say("@dialogue:S_94.B04_E02.line002")
    end
    if ((not (gvars["linchong.event_b04_e03"] ~= 0 and gvars["linchong.event_b04_e03"] ~= nil and gvars["linchong.event_b04_e03"] ~= false)) and (((stage.round() >= 3) and (stage.curCamp() == 0)) and (gvars["linchong.event_b04_e02"] ~= 0 and gvars["linchong.event_b04_e02"] ~= nil and gvars["linchong.event_b04_e02"] ~= false))) then
        gvars["linchong.event_b04_e03"] = 1
        stage.say("@dialogue:S_94.B04_E03.line001")
        stage.say("@dialogue:S_94.B04_E03.line002")
        stage.say("@dialogue:S_94.B04_E03.line003")
        stage.say("@dialogue:S_94.B04_E03.line004")
        stage.say("@dialogue:S_94.B04_E03.line005")
        stage.setFires(true, {{13, 4}, {14, 4}, {13, 5}})
        stage.unit(524).show()
        stage.unit(524).setAI(1, 512)
        stage.unit(525).show()
        stage.unit(525).setAI(1, 512)
        stage.unit(548).show()
        stage.unit(548).setAI(1, 512)
        stage.unit(549).show()
        stage.unit(549).setAI(1, 512)
        stage.unit(550).show()
        stage.unit(550).setAI(1, 512)
        gvars["linchong.b04_fire_revealed"] = 1
        stage.showWinCondition("진오를 구출할 위치와 증거를 조사할 위치를 확인하십시오.")
        stage.heightLight(8, 8, 8, 8)
        stage.heightLight(14, 7, 14, 7)
    end
    if ((not (gvars["linchong.event_b04_e04"] ~= 0 and gvars["linchong.event_b04_e04"] ~= nil and gvars["linchong.event_b04_e04"] ~= false)) and ((__truth(stage.isNear(512, 515, true)) or __truth(stage.isNear(514, 515, true))) and (stage.round() >= 3))) then
        gvars["linchong.event_b04_e04"] = 1
        stage.say("@dialogue:S_94.B04_E04.line001")
        stage.say("@dialogue:S_94.B04_E04.line002")
        stage.say("@dialogue:S_94.B04_E04.line003")
        stage.say("@dialogue:S_94.B04_E04.line004")
        stage.unit(515).hide(1)
        stage.createMine({{["id"] = 515, ["i"] = 3, ["idx"] = 3, ["x"] = 8, ["y"] = 8, ["lv"] = 2, ["dir"] = 2, ["ai"] = 2}})
        stage.setUnitStatus({{["unit"] = stage.unit(515), ["hp"] = 20}})
        gvars["linchong.b04_jin_rescued"] = 1
    end
    if ((not (gvars["linchong.event_b04_e05"] ~= 0 and gvars["linchong.event_b04_e05"] ~= nil and gvars["linchong.event_b04_e05"] ~= false)) and (((stage.round() >= 5) and (stage.curCamp() == 0)) and (not (gvars["linchong.b04_boss_down"] ~= 0 and gvars["linchong.b04_boss_down"] ~= nil and gvars["linchong.b04_boss_down"] ~= false)))) then
        gvars["linchong.event_b04_e05"] = 1
        stage.say("@dialogue:S_94.B04_E05.line001")
        stage.say("@dialogue:S_94.B04_E05.line002")
        stage.createEnemy({{["id"] = 551, ["i"] = 8, ["idx"] = 8, ["x"] = 17, ["y"] = 12, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1}})
        stage.setFires(true, {{12, 4}, {15, 4}, {12, 5}, {14, 5}, {13, 6}, {14, 6}})
    end
    if ((not (gvars["linchong.event_b04_e07"] ~= 0 and gvars["linchong.event_b04_e07"] ~= nil and gvars["linchong.event_b04_e07"] ~= false)) and __truth(stage.unitStateTest(524, 7, 64, 1))) then
        gvars["linchong.event_b04_e07"] = 1
        stage.say("@dialogue:S_94.B04_E07.line001")
        stage.say("@dialogue:S_94.B04_E07.line002")
        gvars["linchong.b04_luqian_desperate"] = 1
    end
    if ((not (gvars["linchong.event_b04_e08"] ~= 0 and gvars["linchong.event_b04_e08"] ~= nil and gvars["linchong.event_b04_e08"] ~= false)) and __truth(stage.unitStateTest(524, 7, 0, 2))) then
        gvars["linchong.event_b04_e08"] = 1
        stage.say("@dialogue:S_94.B04_E08.line001")
        stage.say("@dialogue:S_94.B04_E08.line002")
        stage.say("@dialogue:S_94.B04_E08.line003")
        gvars["linchong.b04_boss_down"] = 1
    end
    if ((not (gvars["linchong.event_b04_e06"] ~= 0 and gvars["linchong.event_b04_e06"] ~= nil and gvars["linchong.event_b04_e06"] ~= false)) and (__truth(stage.isInRect(512, 13, 6, 15, 8)) and (gvars["linchong.b04_fire_revealed"] ~= 0 and gvars["linchong.b04_fire_revealed"] ~= nil and gvars["linchong.b04_fire_revealed"] ~= false))) then
        gvars["linchong.event_b04_e06"] = 1
        stage.say("@dialogue:S_94.B04_E06.line001")
        stage.say("@dialogue:S_94.B04_E06.line002")
        stage.getItem(210, 0, true, 512)
        gvars["linchong.b04_evidence"] = 1
    end
    if ((not (gvars["linchong.event_b04_e10"] ~= 0 and gvars["linchong.event_b04_e10"] ~= nil and gvars["linchong.event_b04_e10"] ~= false)) and __truth(stage.isInRect(512, 2, 3, 4, 5))) then
        gvars["linchong.event_b04_e10"] = 1
        stage.say("@dialogue:S_94.B04_E10.line001")
        stage.say("@dialogue:S_94.B04_E10.line002")
        stage.getItem(149, 0, true, 512)
        gvars["linchong.b04_early_boots"] = 1
    end
    if ((not (gvars["linchong.event_b04_e01"] ~= 0 and gvars["linchong.event_b04_e01"] ~= nil and gvars["linchong.event_b04_e01"] ~= false)) and ((stage.round() >= 2) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b04_e01"] = 1
        if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
            stage.say("@dialogue:S_94.B04_E01.line001")
        end
        stage.say("@dialogue:S_94.B04_E01.line002")
        gvars["linchong.b04_whisper"] = 1
    end
    if ((not (gvars["linchong.event_b04_e09"] ~= 0 and gvars["linchong.event_b04_e09"] ~= nil and gvars["linchong.event_b04_e09"] ~= false)) and ((stage.round() >= 5) or (stage.round() >= 5))) then
        gvars["linchong.event_b04_e09"] = 1
        stage.say("@dialogue:S_94.B04_E09.line001")
    end
    if ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and __truth(stage.unitStateTest(524, 7, 0, 2))) then
        stage.say("@dialogue:S_94.B04_E90.line001")
        if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
            stage.say("@dialogue:S_94.B04_E90.line002")
        end
        stage.say("@dialogue:S_94.B04_E90.line003")
        stage.say("@dialogue:S_94.B04_E90.line004")
        stage.say("@dialogue:S_94.B04_E90.line005")
        if (not ((gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) and (not __truth(stage.unitStateTest(514, 7, 0, 2))))) then
            stage.say("@dialogue:S_94.B04_E90.HU_ABSENT.line001")
        end
        if (not (gvars["linchong.b04_jin_rescued"] ~= 0 and gvars["linchong.b04_jin_rescued"] ~= nil and gvars["linchong.b04_jin_rescued"] ~= false)) then
            stage.say("@dialogue:S_94.B04_E90.JIN_NOT_RESCUED.line001")
        end
        stage.getItem(208, 0, true, 512)
        if (not (gvars["linchong.b04_early_boots"] ~= 0 and gvars["linchong.b04_early_boots"] ~= nil and gvars["linchong.b04_early_boots"] ~= false)) then
            stage.getItem(149, 0, true, 512)
        end
        stage.infoTransfer(4, "105\n1\n512\nsnow_resolve")
        if (gvars["linchong.b04_jin_rescued"] ~= 0 and gvars["linchong.b04_jin_rescued"] ~= nil and gvars["linchong.b04_jin_rescued"] ~= false) then
            gvars["linchong.recruited_jin"] = 1
            model.unitJoin(515, 0, 0)
        end
        if (gvars["linchong.b04_jin_rescued"] ~= 0 and gvars["linchong.b04_jin_rescued"] ~= nil and gvars["linchong.b04_jin_rescued"] ~= false) then
        end
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage["end"]()
        return true
    end
    return false
end

function win_condition()
    return ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and __truth(stage.unitStateTest(524, 7, 0, 2)))
end

function lose_condition()
    return (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 11))
end

function scene2()
    return false
end

function __dispatch__()
    local idx = stage.sceneIndex()
    if (idx == 0) then
        scene0()
        stage.incSceneIdx()
        return nil
    end
    if (idx == 1) then
        if __truth(scene1()) then
            stage.incSceneIdx()
        end
        return nil
    end
    return nil
end
