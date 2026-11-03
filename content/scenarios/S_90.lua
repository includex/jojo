-- Generated Linchong scenario. Edit the campaign generator to preserve changes.
local function __truth(value)
    return value ~= nil and value ~= false and value ~= 0 and value ~= ""
end

function scene0()
    stage.initFight()
    stage.setGlobalData(9, 0, -1, 512, 1, 1)
    if (not (gvars["linchong.gear_initialized_lin"] ~= 0 and gvars["linchong.gear_initialized_lin"] ~= nil and gvars["linchong.gear_initialized_lin"] ~= false)) then
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars["linchong.gear_initialized_lin"] = 1
    end
    stage.createMine({{["id"] = 512, ["i"] = 0, ["idx"] = 0, ["x"] = 5, ["y"] = 5, ["lv"] = 0, ["dir"] = 1, ["ai"] = 2}})
    stage.createEnemy({{["id"] = 522, ["i"] = 1, ["idx"] = 1, ["x"] = 6, ["y"] = 5, ["lv"] = 1, ["dir"] = 1, ["ai"] = 3, ["targetId"] = 512}})
    stage.createEnemy({{["id"] = 523, ["i"] = 2, ["idx"] = 2, ["x"] = 4, ["y"] = 5, ["lv"] = 1, ["dir"] = 1, ["ai"] = 3, ["targetId"] = 512}})
    stage.createEnemy({{["id"] = 528, ["i"] = 3, ["idx"] = 3, ["x"] = 9, ["y"] = 5, ["lv"] = 0, ["dir"] = 3, ["ai"] = 1}})
    stage.createEnemy({{["id"] = 529, ["i"] = 4, ["idx"] = 4, ["x"] = 10, ["y"] = 6, ["lv"] = 0, ["dir"] = 3, ["ai"] = 1}})
    stage.createEnemy({{["id"] = 527, ["i"] = 31, ["x"] = 6, ["y"] = 2, ["hide"] = 1, ["ai"] = 2, ["lv"] = 0}})
    stage.infoTransfer(4, "100\n1\n512\nwind_spear")
    stage.loadBg(58)
    stage.draw()
    stage.setWinCondition("승리 조건\n임충 생존; 임충이 탈출칸 (15,6) 또는 (15,7) 도달; 노지심 등장 이벤트 완료\n\n패배 조건\n임충 HP 0; 9턴 종료 시 미탈출")
    stage.say("@dialogue:S_90.prologue.line001")
    stage.say("@dialogue:S_90.prologue.line002")
    stage.say("@dialogue:S_90.prologue.line003")
    stage.say("@dialogue:S_90.prologue.line004")
    stage.say("@dialogue:S_90.prologue.line005")
    stage.say("@dialogue:S_90.prologue.line006")
    stage.say("@dialogue:S_90.prologue.line007")
    stage.say("@dialogue:S_90.prologue.line008")
    stage.say("@dialogue:S_90.prologue.line009")
    stage.say("@dialogue:S_90.B01_E00.line001")
    stage.say("@dialogue:S_90.B01_E00.line002")
    stage.say("@dialogue:S_90.B01_E00.line003")
    stage.say("@dialogue:S_90.B01_E00.line004")
    stage.say("@dialogue:S_90.B01_E00.line005")
    stage.say("@dialogue:S_90.B01_E00.line006")
    stage.say("@dialogue:S_90.B01_E00.line007")
    stage.say("@dialogue:S_90.B01_E00.line008")
    gvars["linchong.event_b01_e00"] = 1
    gvars["linchong.duel_win"] = 0
    gvars["linchong.exit_count"] = 0
    stage.setUnitStatus({{["unit"] = stage.unit(512), ["hp"] = -35}})
    stage.showWinCondition("노지심의 구원을 기다린 뒤 임충을 동쪽 탈출 지점으로 이동시키십시오.")
    stage.heightLight(14, 5, 14, 6)
    stage.center(5, 5)
    stage.startOper()
    return false
end

function scene1()
    if (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 9)) then
        stage.say("@dialogue:S_90.B01_E99.line001")
        stage["end"]()
        stage.lose()
        return true
    end
    if ((not (gvars["linchong.event_b01_e01"] ~= 0 and gvars["linchong.event_b01_e01"] ~= nil and gvars["linchong.event_b01_e01"] ~= false)) and ((stage.round() >= 1) and (stage.curCamp() == 0))) then
        gvars["linchong.event_b01_e01"] = 1
        stage.say("@dialogue:S_90.B01_E01.line001")
        gvars["linchong.b01_trap"] = 1
    end
    if ((not (gvars["linchong.event_b01_e02"] ~= 0 and gvars["linchong.event_b01_e02"] ~= nil and gvars["linchong.event_b01_e02"] ~= false)) and ((stage.round() >= 1) and (stage.curCamp() == 2))) then
        gvars["linchong.event_b01_e02"] = 1
        stage.say("@dialogue:S_90.B01_E02.line001")
        if (not (gvars["linchong.gear_initialized_lu"] ~= 0 and gvars["linchong.gear_initialized_lu"] ~= nil and gvars["linchong.gear_initialized_lu"] ~= false)) then
            stage.setJoinEquip(513, 63, 1, 35, 1, 0)
            gvars["linchong.gear_initialized_lu"] = 1
        end
        stage.createMine({{["id"] = 513, ["i"] = 5, ["idx"] = 5, ["x"] = 1, ["y"] = 7, ["lv"] = 3, ["dir"] = 1, ["ai"] = 2}})
        stage.infoTransfer(4, "102\n1\n513\nstaff_smash")
        stage.giveUnitItem(513, 216, 4)
        stage.center(1, 7)
        stage.say("@dialogue:S_90.B01_E02.line002")
        stage.unit(513).setAction(6, 1, false)
        stage.unit(513).setAction(7, 1, false)
        stage.unit(513).setAction(6, 1, false)
        stage.unit(513).setAction(0, 1, false)
        stage.say("@dialogue:S_90.B01_E02.line003")
        stage.say("@dialogue:S_90.B01_E02.line004")
        stage.say("@dialogue:S_90.B01_E02.line005")
        stage.say("@dialogue:S_90.B01_E02.line006")
        stage.setUnitStatus({{["unit"] = stage.unit(512), ["hp"] = 18}})
        gvars["linchong.b01_rescued"] = 1
        stage.showWinCondition("탈출할 수 있습니다. 임충을 동쪽 탈출 지점으로 이동시키십시오.")
        stage.heightLight(14, 5, 14, 6)
    end
    if ((not (gvars["linchong.event_b01_e04"] ~= 0 and gvars["linchong.event_b01_e04"] ~= nil and gvars["linchong.event_b01_e04"] ~= false)) and __truth(stage.isInRect(512, 10, 4, 15, 7))) then
        gvars["linchong.event_b01_e04"] = 1
        stage.say("@dialogue:S_90.B01_E04.line001")
        stage.say("@dialogue:S_90.B01_E04.line002")
        stage.createEnemy({{["id"] = 530, ["i"] = 6, ["idx"] = 6, ["x"] = 13, ["y"] = 3, ["lv"] = 1, ["dir"] = 2, ["ai"] = 1}})
        stage.createEnemy({{["id"] = 531, ["i"] = 7, ["idx"] = 7, ["x"] = 13, ["y"] = 8, ["lv"] = 1, ["dir"] = 2, ["ai"] = 1}})
        gvars["linchong.b01_pursuit"] = 1
    end
    if ((not (gvars["linchong.event_b01_e05"] ~= 0 and gvars["linchong.event_b01_e05"] ~= nil and gvars["linchong.event_b01_e05"] ~= false)) and __truth(stage.unitStateTest(512, 7, 29, 1))) then
        gvars["linchong.event_b01_e05"] = 1
        stage.say("@dialogue:S_90.B01_E05.line001")
    end
    if ((not (gvars["linchong.event_b01_e06"] ~= 0 and gvars["linchong.event_b01_e06"] ~= nil and gvars["linchong.event_b01_e06"] ~= false)) and (__truth(stage.isNear(513, 522, true)) or __truth(stage.isNear(513, 523, true)))) then
        gvars["linchong.event_b01_e06"] = 1
        stage.say("@dialogue:S_90.B01_E06.line001")
        stage.say("@dialogue:S_90.B01_E06.line002")
        stage.say("@dialogue:S_90.B01_E06.line003")
        gvars["linchong.b01_mercy"] = 1
    end
    if ((not (gvars["linchong.event_b01_e03"] ~= 0 and gvars["linchong.event_b01_e03"] ~= nil and gvars["linchong.event_b01_e03"] ~= false)) and (stage.round() >= 2)) then
        gvars["linchong.event_b01_e03"] = 1
        stage.say("@dialogue:S_90.B01_E03.line001")
        stage.say("@dialogue:S_90.B01_E03.line002")
    end
    if ((not (gvars["linchong.event_b01_e07"] ~= 0 and gvars["linchong.event_b01_e07"] ~= nil and gvars["linchong.event_b01_e07"] ~= false)) and __truth(stage.unitStateTest(528, 7, 0, 2))) then
        gvars["linchong.event_b01_e07"] = 1
        stage.say("@dialogue:S_90.B01_E07.line001")
        stage.getItem(200, 0, true, 512)
        gvars["linchong.b01_early_loot"] = 1
    end
    if ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and ((__truth(stage.isInPos(512, 14, 5)) or __truth(stage.isInPos(512, 14, 6))) and (gvars["linchong.b01_rescued"] ~= 0 and gvars["linchong.b01_rescued"] ~= nil and gvars["linchong.b01_rescued"] ~= false))) then
        stage.say("@dialogue:S_90.B01_E90.line001")
        stage.say("@dialogue:S_90.B01_E90.line002")
        stage.say("@dialogue:S_90.B01_E90.line003")
        stage.getItem(200, 0, true, 512)
        stage.getItem(200, 0, true, 512)
        stage.getItem(137, 0, true, 512)
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage["end"]()
        return true
    end
    return false
end

function win_condition()
    return ((not __truth(stage.unitStateTest(512, 7, 0, 2))) and ((__truth(stage.isInPos(512, 14, 5)) or __truth(stage.isInPos(512, 14, 6))) and (gvars["linchong.b01_rescued"] ~= 0 and gvars["linchong.b01_rescued"] ~= nil and gvars["linchong.b01_rescued"] ~= false)))
end

function lose_condition()
    return (__truth(stage.unitStateTest(512, 7, 0, 2)) or (stage.round() > 9))
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
