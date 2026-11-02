# 임충전 — generated from source PRD, original Jojo BattleScreen APIs.
def scene0():
    stage.initFight()
    stage.setGlobalData(8, 0, -1, 512, 1, 1)
    if not gvars['linchong.gear_initialized_lin']:
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars['linchong.gear_initialized_lin'] = 1
    if not gvars['linchong.gear_initialized_hong']:
        stage.setJoinEquip(517, 61, 1, 38, 1, 0)
        gvars['linchong.gear_initialized_hong'] = 1
    stage.createMine([{'id': 512, 'i': 0, 'idx': 0, 'x': 4, 'y': 5, 'lv': 0, 'dir': 2, 'ai': 2}])
    stage.createEnemy([{'id': 517, 'i': 1, 'idx': 1, 'x': 8, 'y': 5, 'lv': 2, 'dir': 2, 'ai': 3, 'targetId': 512}])
    stage.createFriend([{'id': 520, 'i': 2, 'idx': 2, 'x': 6, 'y': 1, 'lv': 4, 'dir': 2, 'ai': 2}])
    stage.infoTransfer(4, '100\n1\n512\nwind_spear')
    stage.infoTransfer(4, '109\n1\n517\nstaff_combo')
    stage.loadBg(59)
    stage.draw()
    stage.setWinCondition('승리 조건\n홍교두의 HP를 35% 이하로 만들거나 정식 일기토 승리; 임충 생존\n\n패배 조건\n임충 HP 0(대련 패배); 8턴 종료 시 무승부')
    stage.say('@dialogue:S_91.interlude.line001')
    stage.say('@dialogue:S_91.interlude.line002')
    stage.say('@dialogue:S_91.interlude.line003')
    stage.say('@dialogue:S_91.interlude.line004')
    stage.say('@dialogue:S_91.B02_E00.line001')
    stage.say('@dialogue:S_91.B02_E00.line002')
    stage.say('@dialogue:S_91.B02_E00.line003')
    stage.say('@dialogue:S_91.B02_E00.line004')
    stage.say('@dialogue:S_91.B02_E00.line005')
    stage.say('@dialogue:S_91.B02_E00.line006')
    stage.say('@dialogue:S_91.B02_E00.line007')
    stage.say('@dialogue:S_91.B02_E00.line008')
    pass
    # Existing-engine adaptation: lock_player_input
    # Existing-engine adaptation: play_dialogue
    # Existing-engine adaptation: unlock_player_input
    gvars['linchong.event_b02_e00'] = 1
    gvars['linchong.duel_win'] = 0
    gvars['linchong.exit_count'] = 0
    stage.startOper()
    return False

def scene1():
    if (stage.unitStateTest(512, 7, 0, 2) or stage.round() > 8):
        stage.say('@dialogue:S_91.B02_E99.line001')
        stage.say('@dialogue:S_91.B02_E99.line002')
        stage.end()
        stage.lose()
        return True
    if not gvars['linchong.event_b02_e01'] and (stage.round() >= 1 and stage.curCamp() == 0):
        gvars['linchong.event_b02_e01'] = 1
        stage.say('@dialogue:S_91.B02_E01.line001')
        pass
        # Existing-engine adaptation: apply_status:lin:SPARRING
        # Existing-engine adaptation: apply_status:hong:SPARRING
        gvars['linchong.b02_nonlethal'] = 1
    if not gvars['linchong.event_b02_e04'] and (stage.isNear(512, 517, True) and True):
        gvars['linchong.event_b02_e04'] = 1
        stage.say('@dialogue:S_91.B02_E04.line001')
        stage.say('@dialogue:S_91.B02_E04.line002')
        stage.say('@dialogue:S_91.B02_E04.line003')
        stage.say('@dialogue:S_91.B02_E04.line004')
        stage.say('@dialogue:S_91.B02_E04.line005')
        pass
        fight = stage.startFight(512, 517, 5)
        fight.showStart()
        fight.attack1(True, 1, False)
        fight.attack1(False, 1, False)
        fight.end()
        gvars['linchong.duel_win'] = 1
        gvars['linchong.b02_duel_completed'] = 1
    if not gvars['linchong.event_b02_e02'] and (stage.round() >= 3 and stage.curCamp() == 0):
        gvars['linchong.event_b02_e02'] = 1
        stage.say('@dialogue:S_91.B02_E02.line001')
        stage.say('@dialogue:S_91.B02_E02.line002')
        pass
        # Existing-engine adaptation: buff:hong:ATTACK:+3:2turn
        # Existing-engine adaptation: play_fx:stance
    if not gvars['linchong.event_b02_e05'] and stage.unitStateTest(517, 7, 43, 1):
        gvars['linchong.event_b02_e05'] = 1
        stage.say('@dialogue:S_91.B02_E05.line001')
        stage.say('@dialogue:S_91.B02_E05.line002')
        stage.say('@dialogue:S_91.B02_E05.line003')
        pass
        gvars['linchong.b02_practice_win'] = 1
        stage.setUnitStatus([{'unit':stage.unit(517),'hp':-9999}])
    if not gvars['linchong.event_b02_e03'] and (stage.isInRect(512, 1, 4, 3, 6) or stage.isInRect(512, 9, 4, 11, 6)):
        gvars['linchong.event_b02_e03'] = 1
        stage.say('@dialogue:S_91.B02_E03.line001')
        stage.say('@dialogue:S_91.B02_E03.line002')
        pass
        gvars['linchong.b02_wood_staff'] = 1
        # Existing-engine adaptation: bonus:lin:accuracy:+10:rest_of_battle
    if (not stage.unitStateTest(512, 7, 0, 2) and (gvars['linchong.duel_win'] or stage.unitStateTest(517, 7, 43, 1))):
        stage.say('@dialogue:S_91.B02_E90.line001')
        stage.say('@dialogue:S_91.B02_E90.line002')
        stage.say('@dialogue:S_91.B02_E90.line003')
        stage.say('@dialogue:S_91.B02_E90.line004')
        pass
        stage.getItem(202, 0, True, 512)
        stage.getItem(59, 0, True, 512)
        stage.infoTransfer(4, '101\n1\n512\ncircling_spear')
        # Existing-engine adaptation: unlock_optional:EX01
        # Existing-engine adaptation: unlock:B03
        # Existing-engine adaptation: save_checkpoint
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        sel = stage.choice('장원 야습에 맞선다\n창주로 향한다', -1)
        if sel == 2:
            stage.end()
            stage.jumpScene(185)
        stage.end()
        return True
    return False

def win_condition():
    return not stage.unitStateTest(512, 7, 0, 2) and (gvars['linchong.duel_win'] or stage.unitStateTest(517, 7, 43, 1))

def lose_condition():
    return stage.unitStateTest(512, 7, 0, 2) or stage.round() > 8

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
