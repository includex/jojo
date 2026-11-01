# 임충전 — generated from source PRD, original Jojo BattleScreen APIs.
def scene0():
    stage.initFight()
    stage.setGlobalData(7, 0, -1, 512, 1, 1)
    if not gvars['linchong.gear_initialized_lin']:
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars['linchong.gear_initialized_lin'] = 1
    stage.createMine([{'id': 512, 'i': 0, 'idx': 0, 'x': 5, 'y': 7, 'lv': 0, 'dir': 2, 'ai': 2}])
    stage.createMine([{'id': 517, 'i': 1, 'idx': 1, 'x': 9, 'y': 7, 'lv': 2, 'dir': 2, 'ai': 2}])
    stage.createMine([{'id': 532, 'i': 2, 'idx': 2, 'x': 6, 'y': 6, 'lv': 1, 'dir': 2, 'ai': 2}])
    stage.createEnemy([{'id': 533, 'i': 3, 'idx': 3, 'x': 7, 'y': 2, 'lv': 2, 'dir': 2, 'ai': 3, 'targetId': 568}])
    stage.createEnemy([{'id': 534, 'i': 4, 'idx': 4, 'x': 4, 'y': 2, 'lv': 1, 'dir': 2, 'ai': 3, 'targetId': 568}])
    stage.createEnemy([{'id': 535, 'i': 5, 'idx': 5, 'x': 10, 'y': 2, 'lv': 1, 'dir': 2, 'ai': 3, 'targetId': 568}])
    stage.createEnemy([{'id': 536, 'i': 6, 'idx': 6, 'x': 12, 'y': 4, 'lv': 1, 'dir': 2, 'ai': 1}])
    stage.createFriend([{'id': 568, 'i': 9, 'x': 7, 'y': 7, 'ai': 2, 'lv': 0}])
    stage.infoTransfer(4, '100\n1\n512\nwind_spear')
    stage.loadBg(60)
    stage.draw()
    stage.setWinCondition('승리 조건\n6턴까지 창고 상자 HP가 1 이상; 도적 두목 처치; 임충 생존\n\n패배 조건\n임충 HP 0; 창고 상자 HP 0; 7턴 시작')
    stage.say('@dialogue:S_92.EX01_E00.line001')
    stage.say('@dialogue:S_92.EX01_E00.line002')
    stage.say('@dialogue:S_92.EX01_E00.line003')
    stage.say('@dialogue:S_92.EX01_E00.line004')
    pass
    gvars['linchong.ex01_started'] = 1
    gvars['linchong.event_ex01_e00'] = 1
    gvars['linchong.duel_win'] = 0
    gvars['linchong.exit_count'] = 0
    stage.startOper()
    return False

def scene1():
    if (stage.unitStateTest(512, 7, 0, 2) or stage.round() > 6 or stage.unitStateTest(568, 7, 0, 2)):
        stage.say('@dialogue:S_92.EX01_E99.line001')
        stage.say('@dialogue:S_92.EX01_E99.line002')
        stage.end()
        stage.lose()
        return True
    if not gvars['linchong.event_ex01_e01'] and (stage.round() >= 2 and stage.curCamp() == 0):
        gvars['linchong.event_ex01_e01'] = 1
        stage.say('@dialogue:S_92.EX01_E01.line001')
        pass
        # Existing-engine adaptation: set_objective_hint:supply
        # Existing-engine adaptation: tip:GUARD_OBJECT
    if not gvars['linchong.event_ex01_e02'] and (stage.round() >= 3 and stage.curCamp() == 0):
        gvars['linchong.event_ex01_e02'] = 1
        stage.say('@dialogue:S_92.EX01_E02.line001')
        stage.say('@dialogue:S_92.EX01_E02.line002')
        pass
        stage.createEnemy([{'id': 537, 'i': 7, 'idx': 7, 'x': 2, 'y': 5, 'lv': 1, 'dir': 2, 'ai': 1}])
        stage.createEnemy([{'id': 538, 'i': 8, 'idx': 8, 'x': 12, 'y': 6, 'lv': 1, 'dir': 2, 'ai': 1}])
    if not gvars['linchong.event_ex01_e03'] and (stage.isInRect(512, 5, 3, 7, 5) or stage.isInRect(517, 5, 3, 7, 5)):
        gvars['linchong.event_ex01_e03'] = 1
        stage.say('@dialogue:S_92.EX01_E03.line001')
        stage.say('@dialogue:S_92.EX01_E03.line002')
        pass
        # Existing-engine adaptation: reveal:ambush
        # Existing-engine adaptation: buff:all_allies:accuracy:+5:2turn
    if not gvars['linchong.event_ex01_e04'] and stage.unitStateTest(533, 7, 34, 1):
        gvars['linchong.event_ex01_e04'] = 1
        stage.say('@dialogue:S_92.EX01_E04.line001')
        pass
        gvars['linchong.ex01_boss_weak'] = 1
    if (not stage.unitStateTest(512, 7, 0, 2) and (stage.unitStateTest(533, 7, 0, 2) and not stage.unitStateTest(568, 7, 0, 2))):
        stage.say('@dialogue:S_92.EX01_E90.line001')
        stage.say('@dialogue:S_92.EX01_E90.line002')
        stage.say('@dialogue:S_92.EX01_E90.line003')
        pass
        stage.getItem(141, 0, True, 512)
        stage.getItem(205, 0, True, 512)
        # Existing-engine adaptation: expire_temp:hong_ally
        gvars['linchong.ex01_clear'] = 1
        # Existing-engine adaptation: return_next:B03
        # Existing-engine adaptation: save_checkpoint
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage.end()
        return True
    return False

def win_condition():
    return not stage.unitStateTest(512, 7, 0, 2) and (stage.unitStateTest(533, 7, 0, 2) and not stage.unitStateTest(568, 7, 0, 2))

def lose_condition():
    return stage.unitStateTest(512, 7, 0, 2) or stage.round() > 6 or stage.unitStateTest(568, 7, 0, 2)

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
