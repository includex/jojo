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
    stage.createMine({{["id"] = 512, ["i"] = 0, ["idx"] = 0, ["x"] = 3, ["y"] = 7, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    stage.createEnemy({{["id"] = 540, ["i"] = 3, ["idx"] = 3, ["x"] = 15, ["y"] = 6, ["lv"] = 3, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 566}})
    stage.createEnemy({{["id"] = 541, ["i"] = 4, ["idx"] = 4, ["x"] = 11, ["y"] = 5, ["lv"] = 2, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 566}})
    stage.createEnemy({{["id"] = 542, ["i"] = 5, ["idx"] = 5, ["x"] = 12, ["y"] = 7, ["lv"] = 2, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 566}})
    stage.createEnemy({{["id"] = 543, ["i"] = 6, ["idx"] = 6, ["x"] = 16, ["y"] = 3, ["lv"] = 2, ["dir"] = 2, ["ai"] = 1}})
    stage.createEnemy({{["id"] = 544, ["i"] = 7, ["idx"] = 7, ["x"] = 16, ["y"] = 9, ["lv"] = 2, ["dir"] = 2, ["ai"] = 1}})
    stage.createEnemy({{["id"] = 545, ["i"] = 8, ["idx"] = 8, ["x"] = 9, ["y"] = 10, ["lv"] = 2, ["dir"] = 2, ["ai"] = 1}})
    stage.createFriend({{["id"] = 539, ["i"] = 1, ["idx"] = 1, ["x"] = 4, ["y"] = 5, ["lv"] = 1, ["dir"] = 2, ["ai"] = 2}})
    stage.createFriend({{["id"] = 514, ["i"] = 2, ["idx"] = 2, ["x"] = 10, ["y"] = 9, ["lv"] = 1, ["dir"] = 2, ["ai"] = 2}})
    stage.createFriend({{["id"] = 566, ["i"] = 9, ["x"] = 5, ["y"] = 6, ["ai"] = 2, ["lv"] = 0}})
    stage.createFriend({{["id"] = 567, ["i"] = 10, ["x"] = 7, ["y"] = 6, ["ai"] = 2, ["lv"] = 0}})
    stage.infoTransfer(4, "100\n1\n512\nwind_spear")
    stage.infoTransfer(4, "103\n1\n514\niron_guard")
    stage.loadBg(61)
    stage.draw()
    stage.setWinCondition("승리 조건\n산적두목 격퇴; 수레 2대 중 1대 이상 생존; 임충 생존\n\n패배 조건\n임충 HP 0; 두 수레 HP 0; 11턴 종료")
    stage.say("@dialogue:S_93.interlude.line001")
    stage.say("@dialogue:S_93.interlude.line002")
    stage.say("@dialogue:S_93.interlude.line003")
    stage.say("@dialogue:S_93.interlude.line004")
    stage.say("@dialogue:S_93.B03_E00.line001")
    stage.say("@dialogue:S_93.B03_E00.line002")
    stage.say("@dialogue:S_93.B03_E00.line003")
    stage.say("@dialogue:S_93.B03_E00.line004")
    stage.say("@dialogue:S_93.B03_E00.line005")
    stage.say("@dialogue:S_93.B03_E00.line006")
    gvars["linchong.b03_convoy_started"] = 1
    gvars["linchong.event_b03_e00"] = 1
    gvars["linchong.duel_win"] = 0
    gvars["linchong.exit_count"] = 0
    stage.setUnitStatus({{["unit"] = stage.unit(514), ["hp"] = -35}})
    stage.showWinCondition("수레 두 대 중 적어도 한 대를 지키며 산적두목을 격퇴하십시오.")
    stage.heightLight(5, 6, 5, 6)
    stage.heightLight(7, 6, 7, 6)
    stage.center(3, 7)
    stage.startOper()
    return false
end

function scene1()
    if (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 11) or (__truth(stage.unitStateTest(566, 7, 0, 2)) and __truth(stage.unitStateTest(567, 7, 0, 2)))) then
        stage.say("@dialogue:S_93.B03_E99.line001")
        stage.say("@dialogue:S_93.B03_E99.line002")
        stage["end"]()
        stage.lose()
        return true
    end
    if ((not (gvars["linchong.event_b03_e03"] ~= 0 and gvars["linchong.event_b03_e03"] ~= nil and gvars["linchong.event_b03_e03"] ~= false)) and (__truth(stage.isNear(512, 514, true)) and (stage.round() >= 3))) then
        gvars["linchong.event_b03_e03"] = 1
        stage.say("@dialogue:S_93.B03_E03.line001")
        stage.say("@dialogue:S_93.B03_E03.line002")
        stage.say("@dialogue:S_93.B03_E03.line003")
        stage.say("@dialogue:S_93.B03_E03.line004")
        stage.unit(514).hide(1)
        stage.createMine({{["id"] = 514, ["i"] = 2, ["idx"] = 2, ["x"] = 10, ["y"] = 9, ["lv"] = 1, ["dir"] = 2, ["ai"] = 2}})
        gvars["linchong.b03_hu_rescued"] = 1
        stage.infoTransfer(4, "103\n1\n514\niron_guard")
    end
    if ((not (gvars["linchong.event_b03_e02"] ~= 0 and gvars["linchong.event_b03_e02"] ~= nil and gvars["linchong.event_b03_e02"] ~= false)) and ((stage.round() >= 3) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b03_e02"] = 1
        stage.say("@dialogue:S_93.B03_E02.line001")
        stage.say("@dialogue:S_93.B03_E02.line002")
        stage.say("@dialogue:S_93.B03_E02.line003")
        stage.createEnemy({{["id"] = 546, ["i"] = 9, ["idx"] = 9, ["x"] = 17, ["y"] = 5, ["lv"] = 2, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 566}})
        stage.createEnemy({{["id"] = 547, ["i"] = 10, ["idx"] = 10, ["x"] = 17, ["y"] = 8, ["lv"] = 2, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 566}})
        stage.showWinCondition("허정 가까이 다가가 구출하십시오.")
        stage.heightLight(10, 9, 10, 9)
    end
    if ((not (gvars["linchong.event_b03_e08"] ~= 0 and gvars["linchong.event_b03_e08"] ~= nil and gvars["linchong.event_b03_e08"] ~= false)) and __truth(stage.unitStateTest(543, 7, 0, 2))) then
        gvars["linchong.event_b03_e08"] = 1
        stage.say("@dialogue:S_93.B03_E08.line001")
        stage.say("@dialogue:S_93.B03_E08.line002")
        stage.getItem(101, 0, true, 512)
        gvars["linchong.b03_live_gear"] = 1
    end
    if ((not (gvars["linchong.event_b03_e01"] ~= 0 and gvars["linchong.event_b03_e01"] ~= nil and gvars["linchong.event_b03_e01"] ~= false)) and ((stage.round() >= 2) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b03_e01"] = 1
        stage.say("@dialogue:S_93.B03_E01.line001")
        stage.say("@dialogue:S_93.B03_E01.line002")
    end
    if ((not (gvars["linchong.event_b03_e04"] ~= 0 and gvars["linchong.event_b03_e04"] ~= nil and gvars["linchong.event_b03_e04"] ~= false)) and (__truth(stage.unitStateTest(566, 7, 81, 1)) or __truth(stage.unitStateTest(567, 7, 81, 1)))) then
        gvars["linchong.event_b03_e04"] = 1
        stage.say("@dialogue:S_93.B03_E04.line001")
        stage.say("@dialogue:S_93.B03_E04.line002")
        gvars["linchong.b03_cart_danger"] = 1
    end
    if ((not (gvars["linchong.event_b03_e05"] ~= 0 and gvars["linchong.event_b03_e05"] ~= nil and gvars["linchong.event_b03_e05"] ~= false)) and __truth(stage.unitStateTest(540, 7, 55, 1))) then
        gvars["linchong.event_b03_e05"] = 1
        stage.say("@dialogue:S_93.B03_E05.line001")
        stage.say("@dialogue:S_93.B03_E05.line002")
    end
    if ((not (gvars["linchong.event_b03_e06"] ~= 0 and gvars["linchong.event_b03_e06"] ~= nil and gvars["linchong.event_b03_e06"] ~= false)) and (__truth(stage.unitStateTest(566, 7, 0, 2)) or __truth(stage.unitStateTest(567, 7, 0, 2)))) then
        gvars["linchong.event_b03_e06"] = 1
        stage.say("@dialogue:S_93.B03_E06.line001")
        stage.say("@dialogue:S_93.B03_E06.line002")
        gvars["linchong.b03_one_cart_lost"] = 1
    end
    if ((not (gvars["linchong.event_b03_e07"] ~= 0 and gvars["linchong.event_b03_e07"] ~= nil and gvars["linchong.event_b03_e07"] ~= false)) and __truth(stage.unitStateTest(540, 7, 0, 2))) then
        gvars["linchong.event_b03_e07"] = 1
        stage.say("@dialogue:S_93.B03_E07.line001")
        stage.say("@dialogue:S_93.B03_E07.line002")
        gvars["linchong.b03_boss_down"] = 1
    end
    if ((not (gvars["linchong.event_b03_e91"] ~= 0 and gvars["linchong.event_b03_e91"] ~= nil and gvars["linchong.event_b03_e91"] ~= false)) and (((not __truth(stage.unitStateTest(512, 7, 0, 2))) and (__truth(stage.unitStateTest(540, 7, 0, 2)) and ((not __truth(stage.unitStateTest(566, 7, 0, 2))) or (not __truth(stage.unitStateTest(567, 7, 0, 2)))))) and (not (gvars["linchong.b03_hu_rescued"] ~= 0 and gvars["linchong.b03_hu_rescued"] ~= nil and gvars["linchong.b03_hu_rescued"] ~= false)))) then
        gvars["linchong.event_b03_e91"] = 1
        stage.say("@dialogue:S_93.B03_E91.line001")
        gvars["linchong.b03_hu_missed"] = 1
    end
    if ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and (__truth(stage.unitStateTest(540, 7, 0, 2)) and ((not __truth(stage.unitStateTest(566, 7, 0, 2))) or (not __truth(stage.unitStateTest(567, 7, 0, 2)))))) then
        stage.say("@dialogue:S_93.B03_E90.line001")
        if (not (gvars["linchong.b03_hu_rescued"] ~= 0 and gvars["linchong.b03_hu_rescued"] ~= nil and gvars["linchong.b03_hu_rescued"] ~= false)) then
            stage.say("@dialogue:S_93.B03_E90.HU_NOT_RESCUED.line001")
        end
        gvars["linchong.b03_clear"] = 1
        if (gvars["linchong.b03_hu_rescued"] ~= 0 and gvars["linchong.b03_hu_rescued"] ~= nil and gvars["linchong.b03_hu_rescued"] ~= false) then
            local sel = stage.choice("함께 가시오\n스스로 길을 찾으시오", -1)
            if (sel == 1) then
                gvars["linchong.recruited_hu"] = 1
                model.unitJoin(514, 0, 0)
            else
                gvars["linchong.hu_friend_only"] = 1
            end
        end
        stage.getItem(100, 0, true, 512)
        stage.getItem(207, 0, true, 512)
        if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
            stage.say("@dialogue:S_93.B03_E90.afterChoice.line001")
        end
        if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
            stage.say("@dialogue:S_93.B03_E90.afterChoice.line002")
        end
        if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
            stage.say("@dialogue:S_93.B03_E90.afterChoice.line003")
        end
        if ((gvars["linchong.b03_hu_rescued"] ~= 0 and gvars["linchong.b03_hu_rescued"] ~= nil and gvars["linchong.b03_hu_rescued"] ~= false) and (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false)) then
            stage.say("@dialogue:S_93.B03_E90.HU_RESCUED_AND_ACCEPTED.line001")
            stage.say("@dialogue:S_93.B03_E90.HU_RESCUED_AND_ACCEPTED.line002")
        end
        if (gvars["linchong.hu_friend_only"] ~= 0 and gvars["linchong.hu_friend_only"] ~= nil and gvars["linchong.hu_friend_only"] ~= false) then
            stage.say("@dialogue:S_93.B03_E90.HU_RESCUED_AND_DECLINED.line001")
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
    return ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and (__truth(stage.unitStateTest(540, 7, 0, 2)) and ((not __truth(stage.unitStateTest(566, 7, 0, 2))) or (not __truth(stage.unitStateTest(567, 7, 0, 2))))))
end

function lose_condition()
    return (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 11) or (__truth(stage.unitStateTest(566, 7, 0, 2)) and __truth(stage.unitStateTest(567, 7, 0, 2))))
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
