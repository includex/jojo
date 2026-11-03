-- Generated Linchong scenario. Edit the campaign generator to preserve changes.
local function __truth(value)
    return value ~= nil and value ~= false and value ~= 0 and value ~= ""
end

function scene0()
    stage.initFight()
    stage.setGlobalData(8, 0, -1, 512, 1, 1)
    if (not (gvars["linchong.gear_initialized_lin"] ~= 0 and gvars["linchong.gear_initialized_lin"] ~= nil and gvars["linchong.gear_initialized_lin"] ~= false)) then
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars["linchong.gear_initialized_lin"] = 1
    end
    if (not (gvars["linchong.gear_initialized_hong"] ~= 0 and gvars["linchong.gear_initialized_hong"] ~= nil and gvars["linchong.gear_initialized_hong"] ~= false)) then
        stage.setJoinEquip(517, 61, 1, 38, 1, 0)
        gvars["linchong.gear_initialized_hong"] = 1
    end
    stage.createMine({{["id"] = 512, ["i"] = 0, ["idx"] = 0, ["x"] = 4, ["y"] = 5, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    stage.createEnemy({{["id"] = 517, ["i"] = 1, ["idx"] = 1, ["x"] = 8, ["y"] = 5, ["lv"] = 2, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 512}})
    stage.createFriend({{["id"] = 520, ["i"] = 2, ["idx"] = 2, ["x"] = 6, ["y"] = 1, ["lv"] = 4, ["dir"] = 2, ["ai"] = 2}})
    stage.infoTransfer(4, "100\n1\n512\nwind_spear")
    stage.infoTransfer(4, "109\n1\n517\nstaff_combo")
    stage.loadBg(59)
    stage.draw()
    stage.setWinCondition("승리 조건\n홍교두의 HP를 35% 이하로 만들거나 정식 일기토 승리; 임충 생존\n\n패배 조건\n임충 HP 0(대련 패배); 8턴 종료 시 무승부")
    stage.say("@dialogue:S_91.interlude.line001")
    stage.say("@dialogue:S_91.interlude.line002")
    stage.say("@dialogue:S_91.interlude.line003")
    stage.say("@dialogue:S_91.interlude.line004")
    stage.say("@dialogue:S_91.B02_E00.line001")
    stage.say("@dialogue:S_91.B02_E00.line002")
    stage.say("@dialogue:S_91.B02_E00.line003")
    stage.say("@dialogue:S_91.B02_E00.line004")
    stage.say("@dialogue:S_91.B02_E00.line005")
    stage.say("@dialogue:S_91.B02_E00.line006")
    stage.say("@dialogue:S_91.B02_E00.line007")
    stage.say("@dialogue:S_91.B02_E00.line008")
    gvars["linchong.event_b02_e00"] = 1
    gvars["linchong.duel_win"] = 0
    gvars["linchong.exit_count"] = 0
    stage.startOper()
    return false
end

function scene1()
    if (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 8)) then
        stage.say("@dialogue:S_91.B02_E99.line001")
        stage.say("@dialogue:S_91.B02_E99.line002")
        stage["end"]()
        stage.lose()
        return true
    end
    if ((not (gvars["linchong.event_b02_e01"] ~= 0 and gvars["linchong.event_b02_e01"] ~= nil and gvars["linchong.event_b02_e01"] ~= false)) and ((stage.round() >= 1) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b02_e01"] = 1
        stage.say("@dialogue:S_91.B02_E01.line001")
        gvars["linchong.b02_nonlethal"] = 1
    end
    if ((not (gvars["linchong.event_b02_e04"] ~= 0 and gvars["linchong.event_b02_e04"] ~= nil and gvars["linchong.event_b02_e04"] ~= false)) and (__truth(stage.isNear(512, 517, true)) and true)) then
        gvars["linchong.event_b02_e04"] = 1
        stage.say("@dialogue:S_91.B02_E04.line001")
        stage.say("@dialogue:S_91.B02_E04.line002")
        stage.say("@dialogue:S_91.B02_E04.line003")
        stage.say("@dialogue:S_91.B02_E04.line004")
        stage.say("@dialogue:S_91.B02_E04.line005")
        local fight = stage.startFight(512, 517, 5)
        fight.showStart()
        fight.attack1(true, 1, false)
        fight.attack1(false, 1, false)
        fight["end"]()
        gvars["linchong.duel_win"] = 1
        gvars["linchong.b02_duel_completed"] = 1
    end
    if ((not (gvars["linchong.event_b02_e02"] ~= 0 and gvars["linchong.event_b02_e02"] ~= nil and gvars["linchong.event_b02_e02"] ~= false)) and ((stage.round() >= 3) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b02_e02"] = 1
        stage.say("@dialogue:S_91.B02_E02.line001")
        stage.say("@dialogue:S_91.B02_E02.line002")
    end
    if ((not (gvars["linchong.event_b02_e05"] ~= 0 and gvars["linchong.event_b02_e05"] ~= nil and gvars["linchong.event_b02_e05"] ~= false)) and __truth(stage.unitStateTest(517, 7, 43, 1))) then
        gvars["linchong.event_b02_e05"] = 1
        stage.say("@dialogue:S_91.B02_E05.line001")
        stage.say("@dialogue:S_91.B02_E05.line002")
        stage.say("@dialogue:S_91.B02_E05.line003")
        gvars["linchong.b02_practice_win"] = 1
        stage.setUnitStatus({{["unit"] = stage.unit(517), ["hp"] = -9999}})
    end
    if ((not (gvars["linchong.event_b02_e03"] ~= 0 and gvars["linchong.event_b02_e03"] ~= nil and gvars["linchong.event_b02_e03"] ~= false)) and (__truth(stage.isInRect(512, 1, 4, 3, 6)) or __truth(stage.isInRect(512, 9, 4, 11, 6)))) then
        gvars["linchong.event_b02_e03"] = 1
        stage.say("@dialogue:S_91.B02_E03.line001")
        stage.say("@dialogue:S_91.B02_E03.line002")
        gvars["linchong.b02_wood_staff"] = 1
    end
    if ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and ((gvars["linchong.duel_win"] ~= 0 and gvars["linchong.duel_win"] ~= nil and gvars["linchong.duel_win"] ~= false) or __truth(stage.unitStateTest(517, 7, 43, 1)))) then
        stage.say("@dialogue:S_91.B02_E90.line001")
        stage.say("@dialogue:S_91.B02_E90.line002")
        stage.say("@dialogue:S_91.B02_E90.line003")
        stage.say("@dialogue:S_91.B02_E90.line004")
        stage.getItem(202, 0, true, 512)
        stage.getItem(59, 0, true, 512)
        stage.infoTransfer(4, "101\n1\n512\ncircling_spear")
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        local sel = stage.choice("장원 야습에 맞선다\n창주로 향한다", -1)
        if (sel == 2) then
            stage["end"]()
            stage.jumpScene(185)
        end
        stage["end"]()
        return true
    end
    return false
end

function win_condition()
    return ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and ((gvars["linchong.duel_win"] ~= 0 and gvars["linchong.duel_win"] ~= nil and gvars["linchong.duel_win"] ~= false) or __truth(stage.unitStateTest(517, 7, 43, 1))))
end

function lose_condition()
    return (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 8))
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
