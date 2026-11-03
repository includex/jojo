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
    if (not (gvars["linchong.gear_initialized_hu"] ~= 0 and gvars["linchong.gear_initialized_hu"] ~= nil and gvars["linchong.gear_initialized_hu"] ~= false)) then
        stage.setJoinEquip(514, 64, 1, 33, 1, 0)
        gvars["linchong.gear_initialized_hu"] = 1
    end
    if (not (gvars["linchong.gear_initialized_jin"] ~= 0 and gvars["linchong.gear_initialized_jin"] ~= nil and gvars["linchong.gear_initialized_jin"] ~= false)) then
        stage.setJoinEquip(515, 65, 1, 36, 1, 0)
        gvars["linchong.gear_initialized_jin"] = 1
    end
    if (not (gvars["linchong.gear_initialized_yang"] ~= 0 and gvars["linchong.gear_initialized_yang"] ~= nil and gvars["linchong.gear_initialized_yang"] ~= false)) then
        stage.setJoinEquip(518, 66, 1, 37, 1, 0)
        gvars["linchong.gear_initialized_yang"] = 1
    end
    stage.createMine({{["id"] = 512, ["i"] = 0, ["idx"] = 0, ["x"] = 5, ["y"] = 7, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
        stage.createFriend({{["id"] = 514, ["i"] = 1, ["idx"] = 1, ["x"] = 4, ["y"] = 8, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    end
    if (gvars["linchong.recruited_jin"] ~= 0 and gvars["linchong.recruited_jin"] ~= nil and gvars["linchong.recruited_jin"] ~= false) then
        stage.createFriend({{["id"] = 515, ["i"] = 2, ["idx"] = 2, ["x"] = 4, ["y"] = 6, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    end
    stage.createEnemy({{["id"] = 518, ["i"] = 3, ["idx"] = 3, ["x"] = 9, ["y"] = 7, ["lv"] = 6, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 512}})
    stage.createFriend({{["id"] = 519, ["i"] = 4, ["idx"] = 4, ["x"] = 14, ["y"] = 4, ["lv"] = 5, ["dir"] = 2, ["ai"] = 2}})
    stage.createFriend({{["id"] = 565, ["i"] = 5, ["idx"] = 5, ["x"] = 13, ["y"] = 5, ["lv"] = 4, ["dir"] = 2, ["ai"] = 2}})
    stage.createFriend({{["id"] = 516, ["i"] = 6, ["idx"] = 6, ["x"] = 15, ["y"] = 5, ["lv"] = 3, ["dir"] = 2, ["ai"] = 2}})
    stage.infoTransfer(4, "100\n1\n512\nwind_spear")
    stage.infoTransfer(4, "103\n1\n514\niron_guard")
    stage.infoTransfer(4, "104\n1\n515\nquick_step")
    stage.infoTransfer(4, "108\n1\n518\nswift_blade")
    stage.loadBg(65)
    stage.draw()
    stage.setWinCondition("승리 조건\n양지 HP 35% 이하(비살상) 또는 정식 일기토 승리 또는 7턴까지 임충 생존; 임충 생존; 양지 생존\n\n패배 조건\n임충 HP 0; 양지가 사망 처리됨; 8턴 종료 시 결투 미종결")
    stage.say("@dialogue:S_97.interlude.line001")
    stage.say("@dialogue:S_97.interlude.line002")
    stage.say("@dialogue:S_97.interlude.line003")
    stage.say("@dialogue:S_97.interlude.line004")
    stage.say("@dialogue:S_97.B06_E00.line001")
    stage.say("@dialogue:S_97.B06_E00.line002")
    stage.say("@dialogue:S_97.B06_E00.line003")
    stage.say("@dialogue:S_97.B06_E00.line004")
    stage.say("@dialogue:S_97.B06_E00.line005")
    stage.say("@dialogue:S_97.B06_E00.line006")
    stage.say("@dialogue:S_97.B06_E00.line007")
    stage.say("@dialogue:S_97.B06_E00.line008")
    if ((not ((gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) and (not __truth(stage.unitStateTest(514, 7, 0, 2))))) and (not ((gvars["linchong.recruited_jin"] ~= 0 and gvars["linchong.recruited_jin"] ~= nil and gvars["linchong.recruited_jin"] ~= false) and (not __truth(stage.unitStateTest(515, 7, 0, 2)))))) then
        stage.say("@dialogue:S_97.B06_E00.HU_AND_JIN_ABSENT.line001")
    end
    gvars["linchong.event_b06_e00"] = 1
    gvars["linchong.duel_win"] = 0
    gvars["linchong.exit_count"] = 0
    stage.startOper()
    return false
end

function scene1()
    if (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 8)) then
        stage.say("@dialogue:S_97.B06_E99.line001")
        stage["end"]()
        stage.lose()
        return true
    end
    if ((not (gvars["linchong.event_b06_e01"] ~= 0 and gvars["linchong.event_b06_e01"] ~= nil and gvars["linchong.event_b06_e01"] ~= false)) and ((stage.round() >= 1) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b06_e01"] = 1
        stage.say("@dialogue:S_97.B06_E01.line001")
    end
    if ((not (gvars["linchong.event_b06_e02"] ~= 0 and gvars["linchong.event_b06_e02"] ~= nil and gvars["linchong.event_b06_e02"] ~= false)) and (__truth(stage.isNear(512, 518, true)) and true)) then
        gvars["linchong.event_b06_e02"] = 1
        stage.say("@dialogue:S_97.B06_E02.line001")
        stage.say("@dialogue:S_97.B06_E02.line002")
        stage.say("@dialogue:S_97.B06_E02.line003")
        stage.say("@dialogue:S_97.B06_E02.line004")
        stage.say("@dialogue:S_97.B06_E02.line005")
        local fight = stage.startFight(512, 518, 5)
        fight.showStart()
        fight.attack1(true, 1, false)
        fight.attack1(false, 1, false)
        fight["end"]()
        gvars["linchong.duel_win"] = 1
        gvars["linchong.b06_duel_completed"] = 1
        gvars["linchong.b06_duel_resolved"] = 1
    end
    if ((not (gvars["linchong.event_b06_e04"] ~= 0 and gvars["linchong.event_b06_e04"] ~= nil and gvars["linchong.event_b06_e04"] ~= false)) and (((stage.round() >= 2) and (stage.curCamp() == 0)) and (gvars["linchong.b06_song_support"] ~= 0 and gvars["linchong.b06_song_support"] ~= nil and gvars["linchong.b06_song_support"] ~= false))) then
        gvars["linchong.event_b06_e04"] = 1
        if ((gvars["linchong.ex02_song_support"] ~= 0 and gvars["linchong.ex02_song_support"] ~= nil and gvars["linchong.ex02_song_support"] ~= false) or (gvars["linchong.b06_song_support"] ~= 0 and gvars["linchong.b06_song_support"] ~= nil and gvars["linchong.b06_song_support"] ~= false)) then
            stage.say("@dialogue:S_97.B06_E04.line001")
        end
        stage.say("@dialogue:S_97.B06_E04.line002")
        stage.say("@dialogue:S_97.B06_E04.line003")
        stage.createMine({{["id"] = 521, ["i"] = 7, ["idx"] = 7, ["x"] = 12, ["y"] = 8, ["lv"] = 4, ["dir"] = 2, ["ai"] = 2}})
    end
    if ((not (gvars["linchong.event_b06_e05"] ~= 0 and gvars["linchong.event_b06_e05"] ~= nil and gvars["linchong.event_b06_e05"] ~= false)) and __truth(stage.unitStateTest(518, 7, 64, 1))) then
        gvars["linchong.event_b06_e05"] = 1
        stage.say("@dialogue:S_97.B06_E05.line001")
        stage.say("@dialogue:S_97.B06_E05.line002")
        stage.say("@dialogue:S_97.B06_E05.line003")
        gvars["linchong.b06_duel_resolved"] = 1
        stage.setUnitStatus({{["unit"] = stage.unit(518), ["hp"] = -9999}})
    end
    if ((not (gvars["linchong.event_b06_e06"] ~= 0 and gvars["linchong.event_b06_e06"] ~= nil and gvars["linchong.event_b06_e06"] ~= false)) and (((stage.round() >= 7) and (stage.curCamp() == 2)) and (not (gvars["linchong.b06_duel_resolved"] ~= 0 and gvars["linchong.b06_duel_resolved"] ~= nil and gvars["linchong.b06_duel_resolved"] ~= false)) and (not (gvars["linchong.b06_duel_completed"] ~= 0 and gvars["linchong.b06_duel_completed"] ~= nil and gvars["linchong.b06_duel_completed"] ~= false)))) then
        gvars["linchong.event_b06_e06"] = 1
        stage.say("@dialogue:S_97.B06_E06.line001")
        stage.say("@dialogue:S_97.B06_E06.line002")
        stage.say("@dialogue:S_97.B06_E06.line003")
        gvars["linchong.b06_endurance_clear"] = 1
    end
    if ((not (gvars["linchong.event_b06_e03"] ~= 0 and gvars["linchong.event_b06_e03"] ~= nil and gvars["linchong.event_b06_e03"] ~= false)) and ((stage.round() >= 4) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b06_e03"] = 1
        stage.say("@dialogue:S_97.B06_E03.line001")
        stage.say("@dialogue:S_97.B06_E03.line002")
        stage.say("@dialogue:S_97.B06_E03.line003")
        gvars["linchong.b06_second_phase"] = 1
    end
    if ((not (gvars["linchong.event_b06_e07"] ~= 0 and gvars["linchong.event_b06_e07"] ~= nil and gvars["linchong.event_b06_e07"] ~= false)) and __truth(stage.unitStateTest(512, 7, 38, 1))) then
        gvars["linchong.event_b06_e07"] = 1
        stage.say("@dialogue:S_97.B06_E07.line001")
        stage.say("@dialogue:S_97.B06_E07.line002")
        gvars["linchong.b06_hero_low"] = 1
    end
    if ((not (gvars["linchong.event_b06_e08"] ~= 0 and gvars["linchong.event_b06_e08"] ~= nil and gvars["linchong.event_b06_e08"] ~= false)) and (((stage.round() >= 3) and (stage.curCamp() == 0)) and (gvars["linchong.b04_evidence"] ~= 0 and gvars["linchong.b04_evidence"] ~= nil and gvars["linchong.b04_evidence"] ~= false))) then
        gvars["linchong.event_b06_e08"] = 1
        stage.say("@dialogue:S_97.B06_E08.line001")
        stage.say("@dialogue:S_97.B06_E08.line002")
        stage.say("@dialogue:S_97.B06_E08.line003")
        gvars["linchong.b06_truth_known"] = 1
    end
    if ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and ((gvars["linchong.duel_win"] ~= 0 and gvars["linchong.duel_win"] ~= nil and gvars["linchong.duel_win"] ~= false) or __truth(stage.unitStateTest(518, 7, 64, 1)) or (stage.round() >= 7))) then
        stage.say("@dialogue:S_97.B06_E90.line001")
        stage.say("@dialogue:S_97.B06_E90.line002")
        stage.say("@dialogue:S_97.B06_E90.line003")
        stage.say("@dialogue:S_97.B06_E90.line004")
        stage.say("@dialogue:S_97.B06_E90.line005")
        stage.say("@dialogue:S_97.B06_E90.line006")
        if (gvars["linchong.b04_evidence"] ~= 0 and gvars["linchong.b04_evidence"] ~= nil and gvars["linchong.b04_evidence"] ~= false) then
            stage.say("@dialogue:S_97.B06_E90.B04_EVIDENCE.line001")
            stage.say("@dialogue:S_97.B06_E90.B04_EVIDENCE.line002")
        end
        if (gvars["linchong.ex02_clear"] ~= 0 and gvars["linchong.ex02_clear"] ~= nil and gvars["linchong.ex02_clear"] ~= false) then
            if ((gvars["linchong.ex02_song_support"] ~= 0 and gvars["linchong.ex02_song_support"] ~= nil and gvars["linchong.ex02_song_support"] ~= false) or (gvars["linchong.b06_song_support"] ~= 0 and gvars["linchong.b06_song_support"] ~= nil and gvars["linchong.b06_song_support"] ~= false)) then
                stage.say("@dialogue:S_97.B06_E90.EX02_CLEAR.line001")
            end
        end
        stage.getItem(215, 0, true, 512)
        stage.infoTransfer(4, "107\n1\n512\ntiger_charge")
        gvars["linchong.campaign_complete"] = 1
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage.say("@dialogue:S_97.epilogue.line001")
        stage.say("@dialogue:S_97.epilogue.line002")
        stage.say("@dialogue:S_97.epilogue.line003")
        stage.say("@dialogue:S_97.epilogue.line004")
        stage["end"]()
        return true
    end
    return false
end

function win_condition()
    return ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and ((gvars["linchong.duel_win"] ~= 0 and gvars["linchong.duel_win"] ~= nil and gvars["linchong.duel_win"] ~= false) or __truth(stage.unitStateTest(518, 7, 64, 1)) or (stage.round() >= 7)))
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
