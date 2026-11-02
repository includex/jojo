# 임충전 — generated from source PRD, original Jojo BattleScreen APIs.
def scene0():
    stage.initFight()
    stage.setGlobalData(9, 0, -1, 512, 1, 1)
    if not gvars['linchong.gear_initialized_lin']:
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars['linchong.gear_initialized_lin'] = 1
    stage.createMine([{'id': 512, 'i': 0, 'idx': 0, 'x': 3, 'y': 5, 'lv': 0, 'dir': 2, 'ai': 2}])
    stage.createEnemy([{'id': 522, 'i': 1, 'idx': 1, 'x': 6, 'y': 5, 'lv': 1, 'dir': 2, 'ai': 3, 'targetId': 512}])
    stage.createEnemy([{'id': 523, 'i': 2, 'idx': 2, 'x': 5, 'y': 6, 'lv': 1, 'dir': 2, 'ai': 3, 'targetId': 512}])
    stage.createEnemy([{'id': 528, 'i': 3, 'idx': 3, 'x': 9, 'y': 5, 'lv': 0, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id': 529, 'i': 4, 'idx': 4, 'x': 10, 'y': 6, 'lv': 0, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id':527,'i':31,'x':6,'y':2,'hide':1,'ai':2,'lv':0}])
    stage.infoTransfer(4, '100\n1\n512\nwind_spear')
    stage.loadBg(58)
    stage.draw()
    stage.setWinCondition('승리 조건\n임충 생존; 임충이 탈출칸 (15,6) 또는 (15,7) 도달; 노지심 등장 이벤트 완료\n\n패배 조건\n임충 HP 0; 9턴 종료 시 미탈출')
    stage.say('@dialogue:S_90.prologue.line001')
    stage.say('@dialogue:S_90.prologue.line002')
    stage.say('@dialogue:S_90.prologue.line003')
    stage.say('@dialogue:S_90.prologue.line004')
    stage.say('@dialogue:S_90.prologue.line005')
    stage.say('@dialogue:S_90.prologue.line006')
    stage.say('@dialogue:S_90.prologue.line007')
    stage.say('@dialogue:S_90.prologue.line008')
    stage.say('@dialogue:S_90.prologue.line009')
    stage.say('@dialogue:S_90.B01_E00.line001')
    stage.say('@dialogue:S_90.B01_E00.line002')
    stage.say('@dialogue:S_90.B01_E00.line003')
    stage.say('@dialogue:S_90.B01_E00.line004')
    stage.say('@dialogue:S_90.B01_E00.line005')
    stage.say('@dialogue:S_90.B01_E00.line006')
    stage.say('@dialogue:S_90.B01_E00.line007')
    stage.say('@dialogue:S_90.B01_E00.line008')
    pass
    # Existing-engine adaptation: lock_player_input
    # Existing-engine adaptation: play_dialogue
    # Existing-engine adaptation: unlock_player_input
    gvars['linchong.event_b01_e00'] = 1
    gvars['linchong.duel_win'] = 0
    gvars['linchong.exit_count'] = 0
    stage.setUnitStatus([{'unit':stage.unit(512),'hp':-35}])
    stage.showWinCondition('노지심의 구원을 기다린 뒤 임충을 동쪽 탈출 지점으로 이동시키십시오.')
    stage.heightLight(14, 5, 14, 6)
    stage.center(3, 5)
    stage.startOper()
    return False

def scene1():
    if (stage.unitStateTest(512, 7, 0, 2) or stage.round() > 9):
        stage.say('@dialogue:S_90.B01_E99.line001')
        stage.end()
        stage.lose()
        return True
    if not gvars['linchong.event_b01_e01'] and (stage.round() >= 1 and stage.curCamp() == 0):
        gvars['linchong.event_b01_e01'] = 1
        stage.say('@dialogue:S_90.B01_E01.line001')
        pass
        # Existing-engine adaptation: apply_status:lin:BOUND
        # Existing-engine adaptation: apply_status:lin:DISARMED
        gvars['linchong.b01_trap'] = 1
    if not gvars['linchong.event_b01_e02'] and (stage.round() >= 1 and stage.curCamp() == 2):
        gvars['linchong.event_b01_e02'] = 1
        stage.say('@dialogue:S_90.B01_E02.line001')
        stage.say('@dialogue:S_90.B01_E02.line002')
        stage.say('@dialogue:S_90.B01_E02.line003')
        stage.say('@dialogue:S_90.B01_E02.line004')
        stage.say('@dialogue:S_90.B01_E02.line005')
        stage.say('@dialogue:S_90.B01_E02.line006')
        pass
        if not gvars['linchong.gear_initialized_lu']:
            stage.setJoinEquip(513, 63, 1, 35, 1, 0)
            gvars['linchong.gear_initialized_lu'] = 1
        stage.createMine([{'id': 513, 'i': 5, 'idx': 5, 'x': 1, 'y': 7, 'lv': 3, 'dir': 2, 'ai': 2}])
        stage.infoTransfer(4, '102\n1\n513\nstaff_smash')
        # Existing-engine adaptation: remove_status:lin:BOUND
        # Existing-engine adaptation: remove_status:lin:DISARMED
        stage.setUnitStatus([{'unit':stage.unit(512),'hp':18}])
        # Existing-engine adaptation: set_commandable:lu:true
        # Existing-engine adaptation: codex:lu:COMMANDABLE_TEMP
        gvars['linchong.b01_rescued'] = 1
        # Existing-engine adaptation: play_fx:staff_strike
        stage.showWinCondition('탈출할 수 있습니다. 임충을 동쪽 탈출 지점으로 이동시키십시오.')
        stage.heightLight(14, 5, 14, 6)
    if not gvars['linchong.event_b01_e04'] and stage.isInRect(512, 10, 4, 15, 7):
        gvars['linchong.event_b01_e04'] = 1
        stage.say('@dialogue:S_90.B01_E04.line001')
        stage.say('@dialogue:S_90.B01_E04.line002')
        pass
        stage.createEnemy([{'id': 530, 'i': 6, 'idx': 6, 'x': 13, 'y': 3, 'lv': 1, 'dir': 2, 'ai': 1}])
        stage.createEnemy([{'id': 531, 'i': 7, 'idx': 7, 'x': 13, 'y': 8, 'lv': 1, 'dir': 2, 'ai': 1}])
        gvars['linchong.b01_pursuit'] = 1
    if not gvars['linchong.event_b01_e05'] and stage.unitStateTest(512, 7, 29, 1):
        gvars['linchong.event_b01_e05'] = 1
        stage.say('@dialogue:S_90.B01_E05.line001')
        pass
        # Existing-engine adaptation: tutorial:consume_item
    if not gvars['linchong.event_b01_e06'] and (stage.isNear(513, 522, True) or stage.isNear(513, 523, True)):
        gvars['linchong.event_b01_e06'] = 1
        stage.say('@dialogue:S_90.B01_E06.line001')
        stage.say('@dialogue:S_90.B01_E06.line002')
        stage.say('@dialogue:S_90.B01_E06.line003')
        pass
        gvars['linchong.b01_mercy'] = 1
    if not gvars['linchong.event_b01_e03'] and stage.round() >= 2:
        gvars['linchong.event_b01_e03'] = 1
        stage.say('@dialogue:S_90.B01_E03.line001')
        stage.say('@dialogue:S_90.B01_E03.line002')
        pass
        # Existing-engine adaptation: tip:SKILL_GROWTH
    if not gvars['linchong.event_b01_e07'] and stage.unitStateTest(528, 7, 0, 2):
        gvars['linchong.event_b01_e07'] = 1
        stage.say('@dialogue:S_90.B01_E07.line001')
        pass
        stage.getItem(200, 0, True, 512)
        gvars['linchong.b01_early_loot'] = 1
    if (not stage.unitStateTest(512, 7, 0, 2) and ((stage.isInPos(512, 14, 5) or stage.isInPos(512, 14, 6)) and gvars['linchong.b01_rescued'])):
        stage.say('@dialogue:S_90.B01_E90.line001')
        stage.say('@dialogue:S_90.B01_E90.line002')
        stage.say('@dialogue:S_90.B01_E90.line003')
        pass
        stage.getItem(200, 0, True, 512)
        stage.getItem(200, 0, True, 512)
        stage.getItem(137, 0, True, 512)
        # Existing-engine adaptation: expire_temp:lu
        # Existing-engine adaptation: unlock:B02
        # Existing-engine adaptation: save_checkpoint
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage.end()
        return True
    return False

def win_condition():
    return not stage.unitStateTest(512, 7, 0, 2) and ((stage.isInPos(512, 14, 5) or stage.isInPos(512, 14, 6)) and gvars['linchong.b01_rescued'])

def lose_condition():
    return stage.unitStateTest(512, 7, 0, 2) or stage.round() > 9

def scene2():
    return False

def __dispatch__():
    label('__dispatch_loop')
    idx = stage.sceneIndex()
    if idx == 0:
        scene0()
        stage.incSceneIdx()
        return None
    if idx == 1:
        if scene1():
            stage.incSceneIdx()
        return None
    return None
