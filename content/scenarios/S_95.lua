-- Generated Linchong scenario. Edit the campaign generator to preserve changes.
local function __truth(value)
    return value ~= nil and value ~= false and value ~= 0 and value ~= ""
end

function scene0()
    stage.initFight()
    stage.setGlobalData(10, 0, -1, 512, 1, 1)
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
    if (not (gvars["linchong.gear_initialized_zhu"] ~= 0 and gvars["linchong.gear_initialized_zhu"] ~= nil and gvars["linchong.gear_initialized_zhu"] ~= false)) then
        stage.setJoinEquip(516, 65, 1, 36, 1, 0)
        gvars["linchong.gear_initialized_zhu"] = 1
    end
    stage.createMine({{["id"] = 512, ["i"] = 0, ["idx"] = 0, ["x"] = 3, ["y"] = 6, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
        stage.createMine({{["id"] = 514, ["i"] = 1, ["idx"] = 1, ["x"] = 2, ["y"] = 7, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    end
    if (gvars["linchong.recruited_jin"] ~= 0 and gvars["linchong.recruited_jin"] ~= nil and gvars["linchong.recruited_jin"] ~= false) then
        stage.createMine({{["id"] = 515, ["i"] = 2, ["idx"] = 2, ["x"] = 4, ["y"] = 7, ["lv"] = 0, ["dir"] = 2, ["ai"] = 2}})
    end
    stage.createEnemy({{["id"] = 552, ["i"] = 4, ["idx"] = 4, ["x"] = 7, ["y"] = 1, ["lv"] = 4, ["dir"] = 2, ["ai"] = 1}})
    stage.createEnemy({{["id"] = 553, ["i"] = 5, ["idx"] = 5, ["x"] = 5, ["y"] = 1, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1}})
    stage.createEnemy({{["id"] = 554, ["i"] = 6, ["idx"] = 6, ["x"] = 8, ["y"] = 2, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1}})
    stage.createEnemy({{["id"] = 555, ["i"] = 7, ["idx"] = 7, ["x"] = 2, ["y"] = 2, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1}})
    stage.createEnemy({{["id"] = 556, ["i"] = 8, ["idx"] = 8, ["x"] = 10, ["y"] = 1, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1}})
    stage.createFriend({{["id"] = 516, ["i"] = 3, ["idx"] = 3, ["x"] = 11, ["y"] = 5, ["lv"] = 3, ["dir"] = 2, ["ai"] = 2}})
    stage.createEnemy({{["id"] = 527, ["i"] = 31, ["x"] = 15, ["y"] = 0, ["hide"] = 1, ["ai"] = 2, ["lv"] = 0}})
    stage.infoTransfer(4, "100\n1\n512\nwind_spear")
    stage.infoTransfer(4, "103\n1\n514\niron_guard")
    stage.infoTransfer(4, "104\n1\n515\nquick_step")
    stage.infoTransfer(4, "106\n1\n516\nwater_stride")
    stage.loadBg(63)
    stage.draw()
    stage.setWinCondition("승리 조건\n임충을 포함한 아군 2명 선착장 도착; 주귀 생존\n\n패배 조건\n임충 HP 0; 주귀 HP 0; 10턴 종료 시 미탈출")
    stage.say("@dialogue:S_95.interlude.line001")
    stage.say("@dialogue:S_95.interlude.line002")
    if (gvars["linchong.recruited_jin"] ~= 0 and gvars["linchong.recruited_jin"] ~= nil and gvars["linchong.recruited_jin"] ~= false) then
        stage.say("@dialogue:S_95.interlude.line003")
    end
    if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
        stage.say("@dialogue:S_95.interlude.line004")
    end
    stage.say("@dialogue:S_95.interlude.line005")
    stage.say("@dialogue:S_95.B05_E00.line001")
    stage.say("@dialogue:S_95.B05_E00.line002")
    stage.say("@dialogue:S_95.B05_E00.line003")
    stage.say("@dialogue:S_95.B05_E00.line004")
    stage.say("@dialogue:S_95.B05_E00.line005")
    stage.say("@dialogue:S_95.B05_E00.line006")
    gvars["linchong.b05_arrived"] = 1
    gvars["linchong.event_b05_e00"] = 1
    gvars["linchong.duel_win"] = 0
    gvars["linchong.exit_count"] = 0
    stage.showWinCondition("임충을 포함한 아군 두 명을 선착장으로 이동시키고 주귀를 보호하십시오.")
    stage.heightLight(18, 7, 18, 8)
    stage.center(3, 6)
    stage.startOper()
    return false
end

function scene1()
    if (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 10) or __truth(stage.unitStateTest(516, 7, 0, 2))) then
        stage.say("@dialogue:S_95.B05_E99.line001")
        stage["end"]()
        stage.lose()
        return true
    end
    gvars["linchong.exit_count"] = 0
    if ((__truth(stage.isInPos(512, 18, 7)) or __truth(stage.isInPos(512, 18, 8))) and (not __truth(stage.unitStateTest(512, 7, 0, 2)))) then
        gvars["linchong.exit_count"] = gvars["linchong.exit_count"] + 1
    end
    if ((__truth(stage.isInPos(514, 18, 7)) or __truth(stage.isInPos(514, 18, 8))) and (not __truth(stage.unitStateTest(514, 7, 0, 2)))) then
        gvars["linchong.exit_count"] = gvars["linchong.exit_count"] + 1
    end
    if ((__truth(stage.isInPos(515, 18, 7)) or __truth(stage.isInPos(515, 18, 8))) and (not __truth(stage.unitStateTest(515, 7, 0, 2)))) then
        gvars["linchong.exit_count"] = gvars["linchong.exit_count"] + 1
    end
    if ((__truth(stage.isInPos(516, 18, 7)) or __truth(stage.isInPos(516, 18, 8))) and (not __truth(stage.unitStateTest(516, 7, 0, 2)))) then
        gvars["linchong.exit_count"] = gvars["linchong.exit_count"] + 1
    end
    if ((not (gvars["linchong.event_b05_e01"] ~= 0 and gvars["linchong.event_b05_e01"] ~= nil and gvars["linchong.event_b05_e01"] ~= false)) and ((stage.round() >= 1) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b05_e01"] = 1
        stage.say("@dialogue:S_95.B05_E01.line001")
        stage.say("@dialogue:S_95.B05_E01.line002")
        stage.unit(516).hide(1)
        stage.createMine({{["id"] = 516, ["i"] = 4, ["idx"] = 4, ["x"] = 11, ["y"] = 5, ["lv"] = 3, ["dir"] = 2, ["ai"] = 2}})
        gvars["linchong.b05_guide"] = 1
    end
    if ((not (gvars["linchong.event_b05_e02"] ~= 0 and gvars["linchong.event_b05_e02"] ~= nil and gvars["linchong.event_b05_e02"] ~= false)) and (__truth(stage.isInRect(512, 11, 5, 13, 7)) or __truth(stage.isInRect(516, 11, 5, 13, 7)))) then
        gvars["linchong.event_b05_e02"] = 1
        stage.say("@dialogue:S_95.B05_E02.line001")
        stage.say("@dialogue:S_95.B05_E02.line002")
        gvars["linchong.b05_signal"] = 1
    end
    if ((not (gvars["linchong.event_b05_e04"] ~= 0 and gvars["linchong.event_b05_e04"] ~= nil and gvars["linchong.event_b05_e04"] ~= false)) and ((stage.round() >= 4) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b05_e04"] = 1
        stage.say("@dialogue:S_95.B05_E04.line001")
        if (gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) then
            stage.say("@dialogue:S_95.B05_E04.line002")
        end
        if (not ((gvars["linchong.recruited_hu"] ~= 0 and gvars["linchong.recruited_hu"] ~= nil and gvars["linchong.recruited_hu"] ~= false) and (not __truth(stage.unitStateTest(514, 7, 0, 2))))) then
            stage.say("@dialogue:S_95.B05_E04.HU_ABSENT.line001")
        end
        stage.createEnemy({{["id"] = 557, ["i"] = 9, ["idx"] = 9, ["x"] = 1, ["y"] = 10, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1}})
        stage.createEnemy({{["id"] = 558, ["i"] = 10, ["idx"] = 10, ["x"] = 5, ["y"] = 11, ["lv"] = 3, ["dir"] = 2, ["ai"] = 1}})
    end
    if ((not (gvars["linchong.event_b05_e03"] ~= 0 and gvars["linchong.event_b05_e03"] ~= nil and gvars["linchong.event_b05_e03"] ~= false)) and (__truth(stage.isInRect(512, 8, 10, 10, 12)) or __truth(stage.isInRect(515, 8, 10, 10, 12)) or __truth(stage.isInRect(516, 8, 10, 10, 12)))) then
        gvars["linchong.event_b05_e03"] = 1
        if (gvars["linchong.recruited_jin"] ~= 0 and gvars["linchong.recruited_jin"] ~= nil and gvars["linchong.recruited_jin"] ~= false) then
            stage.say("@dialogue:S_95.B05_E03.line001")
        end
        stage.say("@dialogue:S_95.B05_E03.line002")
        stage.say("@dialogue:S_95.B05_E03.line003")
        if (not ((gvars["linchong.recruited_jin"] ~= 0 and gvars["linchong.recruited_jin"] ~= nil and gvars["linchong.recruited_jin"] ~= false) and (not __truth(stage.unitStateTest(515, 7, 0, 2))))) then
            stage.say("@dialogue:S_95.B05_E03.JIN_ABSENT.line001")
        end
        gvars["linchong.b05_hidden_waterway"] = 1
        stage.getItem(212, 0, true, 512)
    end
    if ((not (gvars["linchong.event_b05_e05"] ~= 0 and gvars["linchong.event_b05_e05"] ~= nil and gvars["linchong.event_b05_e05"] ~= false)) and __truth(stage.unitStateTest(552, 7, 52, 1))) then
        gvars["linchong.event_b05_e05"] = 1
        stage.say("@dialogue:S_95.B05_E05.line001")
        stage.say("@dialogue:S_95.B05_E05.line002")
        gvars["linchong.b05_captain_wounded"] = 1
    end
    if ((not (gvars["linchong.event_b05_e06"] ~= 0 and gvars["linchong.event_b05_e06"] ~= nil and gvars["linchong.event_b05_e06"] ~= false)) and (__truth(stage.isInPos(512, 18, 7)) or __truth(stage.isInPos(512, 18, 8)))) then
        gvars["linchong.event_b05_e06"] = 1
        stage.say("@dialogue:S_95.B05_E06.line001")
        stage.say("@dialogue:S_95.B05_E06.line002")
        gvars["linchong.b05_lin_dock"] = 1
        gvars["linchong.b05_lin_dock"] = 1
    end
    if ((not (gvars["linchong.event_b05_e07"] ~= 0 and gvars["linchong.event_b05_e07"] ~= nil and gvars["linchong.event_b05_e07"] ~= false)) and ((gvars["linchong.exit_count"] >= 2) and (gvars["linchong.b05_lin_dock"] ~= 0 and gvars["linchong.b05_lin_dock"] ~= nil and gvars["linchong.b05_lin_dock"] ~= false))) then
        gvars["linchong.event_b05_e07"] = 1
        stage.say("@dialogue:S_95.B05_E07.line001")
        stage.say("@dialogue:S_95.B05_E07.line002")
        gvars["linchong.b05_escape_ready"] = 1
    end
    if ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and ((gvars["linchong.exit_count"] >= 2) and (gvars["linchong.b05_lin_dock"] ~= 0 and gvars["linchong.b05_lin_dock"] ~= nil and gvars["linchong.b05_lin_dock"] ~= false) and (not __truth(stage.unitStateTest(516, 7, 0, 2))))) then
        stage.say("@dialogue:S_95.B05_E90.line001")
        stage.say("@dialogue:S_95.B05_E90.line002")
        stage.say("@dialogue:S_95.B05_E90.line003")
        stage.getItem(256, 0, true, 512)
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        if (gvars["linchong.b05_hidden_waterway"] ~= 0 and gvars["linchong.b05_hidden_waterway"] ~= nil and gvars["linchong.b05_hidden_waterway"] ~= false) then
            local sel = stage.choice("밀수선을 조사한다\n양산박으로 향한다", -1)
            if (sel == 2) then
                stage["end"]()
                stage.jumpScene(193)
            end
        else
            stage["end"]()
            stage.jumpScene(193)
        end
        stage["end"]()
        return true
    end
    return false
end

function win_condition()
    return ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and ((gvars["linchong.exit_count"] >= 2) and (gvars["linchong.b05_lin_dock"] ~= 0 and gvars["linchong.b05_lin_dock"] ~= nil and gvars["linchong.b05_lin_dock"] ~= false) and (not __truth(stage.unitStateTest(516, 7, 0, 2)))))
end

function lose_condition()
    return (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 10) or __truth(stage.unitStateTest(516, 7, 0, 2)))
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
