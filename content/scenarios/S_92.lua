-- Generated Linchong scenario. Edit the campaign generator to preserve changes.
local function __truth(value)
    return value ~= nil and value ~= false and value ~= 0 and value ~= ""
end

function scene0()
    stage.initFight()
    stage.setGlobalData(7, 0, -1, 512, 1, 1)
    if (not (gvars["linchong.gear_initialized_lin"] ~= 0 and gvars["linchong.gear_initialized_lin"] ~= nil and gvars["linchong.gear_initialized_lin"] ~= false)) then
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars["linchong.gear_initialized_lin"] = 1
    end
    stage.createMine({{["id"] = 512, ["i"] = 0, ["idx"] = 0, ["x"] = 5, ["y"] = 7, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    stage.createMine({{["id"] = 517, ["i"] = 1, ["idx"] = 1, ["x"] = 9, ["y"] = 7, ["lv"] = 2, ["dir"] = 2, ["ai"] = 2}})
    stage.createMine({{["id"] = 532, ["i"] = 2, ["idx"] = 2, ["x"] = 6, ["y"] = 6, ["lv"] = 1, ["dir"] = 2, ["ai"] = 2}})
    stage.createEnemy({{["id"] = 533, ["i"] = 3, ["idx"] = 3, ["x"] = 7, ["y"] = 2, ["lv"] = 2, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 568}})
    stage.createEnemy({{["id"] = 534, ["i"] = 4, ["idx"] = 4, ["x"] = 4, ["y"] = 2, ["lv"] = 1, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 568}})
    stage.createEnemy({{["id"] = 535, ["i"] = 5, ["idx"] = 5, ["x"] = 10, ["y"] = 2, ["lv"] = 1, ["dir"] = 2, ["ai"] = 3, ["targetId"] = 568}})
    stage.createEnemy({{["id"] = 536, ["i"] = 6, ["idx"] = 6, ["x"] = 12, ["y"] = 4, ["lv"] = 1, ["dir"] = 2, ["ai"] = 1}})
    stage.createFriend({{["id"] = 568, ["i"] = 9, ["x"] = 7, ["y"] = 7, ["ai"] = 2, ["lv"] = 0}})
    stage.infoTransfer(4, "100\n1\n512\nwind_spear")
    stage.infoTransfer(4, "110\n1\n512\nmeditation")
    stage.infoTransfer(4, "110\n1\n517\nmeditation")
    stage.infoTransfer(4, "110\n1\n532\nmeditation")
    stage.loadBg(60)
    stage.draw()
    stage.setWinCondition("승리 조건\n6턴까지 창고 상자 HP가 1 이상; 도적 두목 처치; 임충 생존\n\n패배 조건\n임충 HP 0; 창고 상자 HP 0; 7턴 종료 시")
    stage.say("@dialogue:S_92.EX01_E00.line001")
    stage.say("@dialogue:S_92.EX01_E00.line002")
    stage.say("@dialogue:S_92.EX01_E00.line003")
    stage.say("@dialogue:S_92.EX01_E00.line004")
    gvars["linchong.ex01_started"] = 1
    gvars["linchong.event_ex01_e00"] = 1
    gvars["linchong.duel_win"] = 0
    gvars["linchong.exit_count"] = 0
    stage.showWinCondition("이 창고 상자를 지키며 도적 두목을 격퇴하십시오.")
    stage.heightLight(7, 7, 7, 7)
    stage.center(5, 7)
    stage.startOper()
    return false
end

function scene1()
    if (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 7) or __truth(stage.unitStateTest(568, 7, 0, 2))) then
        stage.say("@dialogue:S_92.EX01_E99.line001")
        stage.say("@dialogue:S_92.EX01_E99.line002")
        stage["end"]()
        stage.lose()
        return true
    end
    if ((not (gvars["linchong.event_ex01_e01"] ~= 0 and gvars["linchong.event_ex01_e01"] ~= nil and gvars["linchong.event_ex01_e01"] ~= false)) and ((stage.round() >= 2) and (stage.curCamp() == 0))) then
        gvars["linchong.event_ex01_e01"] = 1
        stage.say("@dialogue:S_92.EX01_E01.line001")
    end
    if ((not (gvars["linchong.event_ex01_e02"] ~= 0 and gvars["linchong.event_ex01_e02"] ~= nil and gvars["linchong.event_ex01_e02"] ~= false)) and ((stage.round() >= 3) and (stage.curCamp() == 0))) then
        gvars["linchong.event_ex01_e02"] = 1
        stage.say("@dialogue:S_92.EX01_E02.line001")
        stage.say("@dialogue:S_92.EX01_E02.line002")
        stage.createEnemy({{["id"] = 537, ["i"] = 7, ["idx"] = 7, ["x"] = 2, ["y"] = 5, ["lv"] = 1, ["dir"] = 2, ["ai"] = 1}})
        stage.createEnemy({{["id"] = 538, ["i"] = 8, ["idx"] = 8, ["x"] = 12, ["y"] = 6, ["lv"] = 1, ["dir"] = 2, ["ai"] = 1}})
    end
    if ((not (gvars["linchong.event_ex01_e03"] ~= 0 and gvars["linchong.event_ex01_e03"] ~= nil and gvars["linchong.event_ex01_e03"] ~= false)) and (__truth(stage.isInRect(512, 5, 3, 7, 5)) or __truth(stage.isInRect(517, 5, 3, 7, 5)))) then
        gvars["linchong.event_ex01_e03"] = 1
        stage.say("@dialogue:S_92.EX01_E03.line001")
        stage.say("@dialogue:S_92.EX01_E03.line002")
    end
    if ((not (gvars["linchong.event_ex01_e04"] ~= 0 and gvars["linchong.event_ex01_e04"] ~= nil and gvars["linchong.event_ex01_e04"] ~= false)) and __truth(stage.unitStateTest(533, 7, 34, 1))) then
        gvars["linchong.event_ex01_e04"] = 1
        stage.say("@dialogue:S_92.EX01_E04.line001")
        gvars["linchong.ex01_boss_weak"] = 1
    end
    if ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and (__truth(stage.unitStateTest(533, 7, 0, 2)) and (not __truth(stage.unitStateTest(568, 7, 0, 2))))) then
        stage.say("@dialogue:S_92.EX01_E90.line001")
        stage.say("@dialogue:S_92.EX01_E90.line002")
        stage.say("@dialogue:S_92.EX01_E90.line003")
        stage.getItem(141, 0, true, 512)
        stage.getItem(205, 0, true, 512)
        gvars["linchong.ex01_clear"] = 1
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage["end"]()
        return true
    end
    return false
end

function win_condition()
    return ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and (__truth(stage.unitStateTest(533, 7, 0, 2)) and (not __truth(stage.unitStateTest(568, 7, 0, 2)))))
end

function lose_condition()
    return (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 7) or __truth(stage.unitStateTest(568, 7, 0, 2)))
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
