# 임충전 — generated from source PRD, original Jojo BattleScreen APIs.
def scene0():
    stage.initFight()
    stage.setGlobalData(10, 0, -1, 512, 1, 1)
    if not gvars['linchong.gear_initialized_lin']:
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars['linchong.gear_initialized_lin'] = 1
    if not gvars['linchong.gear_initialized_hu']:
        stage.setJoinEquip(514, 64, 1, 33, 1, 0)
        gvars['linchong.gear_initialized_hu'] = 1
    if not gvars['linchong.gear_initialized_jin']:
        stage.setJoinEquip(515, 65, 1, 36, 1, 0)
        gvars['linchong.gear_initialized_jin'] = 1
    if not gvars['linchong.gear_initialized_zhu']:
        stage.setJoinEquip(516, 65, 1, 36, 1, 0)
        gvars['linchong.gear_initialized_zhu'] = 1
    stage.createMine([{'id': 512, 'i': 0, 'idx': 0, 'x': 3, 'y': 6, 'lv': 0, 'dir': 2, 'ai': 2}])
    if gvars['linchong.recruited_hu']:
        stage.createMine([{'id': 514, 'i': 1, 'idx': 1, 'x': 2, 'y': 7, 'lv': 0, 'dir': 2, 'ai': 2}])
    if gvars['linchong.recruited_jin']:
        stage.createMine([{'id': 515, 'i': 2, 'idx': 2, 'x': 4, 'y': 7, 'lv': 0, 'dir': 2, 'ai': 2}])
    stage.createEnemy([{'id': 552, 'i': 4, 'idx': 4, 'x': 7, 'y': 1, 'lv': 4, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id': 553, 'i': 5, 'idx': 5, 'x': 5, 'y': 1, 'lv': 3, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id': 554, 'i': 6, 'idx': 6, 'x': 8, 'y': 2, 'lv': 3, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id': 555, 'i': 7, 'idx': 7, 'x': 2, 'y': 2, 'lv': 3, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id': 556, 'i': 8, 'idx': 8, 'x': 10, 'y': 1, 'lv': 3, 'dir': 2, 'ai': 1}])
    stage.createFriend([{'id': 516, 'i': 3, 'idx': 3, 'x': 11, 'y': 5, 'lv': 3, 'dir': 2, 'ai': 2}])
    stage.createEnemy([{'id':527,'i':31,'x':15,'y':0,'hide':1,'ai':2,'lv':0}])
    stage.infoTransfer(4, '100\n1\n512\nwind_spear')
    stage.infoTransfer(4, '103\n1\n514\niron_guard')
    stage.infoTransfer(4, '104\n1\n515\nquick_step')
    stage.infoTransfer(4, '106\n1\n516\nwater_stride')
    stage.infoTransfer(4, '110\n1\n512\nmeditation')
    stage.infoTransfer(4, '110\n1\n514\nmeditation')
    stage.infoTransfer(4, '110\n1\n515\nmeditation')
    stage.loadBg(63)
    stage.draw()
    stage.setWinCondition('승리 조건\n임충을 포함한 아군 2명 선착장 도착; 주귀 생존\n\n패배 조건\n임충 HP 0; 주귀 HP 0; 10턴 종료 시 미탈출')
    stage.info('풍설산신묘의 불길을 뒤로하고 임충은 창주를 떠났다.')
    stage.say('@dialogue:S_95.interlude.line002')
    if gvars['linchong.recruited_jin']:
        stage.say('@dialogue:S_95.interlude.line003')
    if gvars['linchong.recruited_hu']:
        stage.say('@dialogue:S_95.interlude.line004')
    stage.say('@dialogue:S_95.interlude.line005')
    stage.info('양산박 남쪽 수로, 갈대밭 사이에 작은 술집 하나가 외따로 서 있었다.')
    stage.say('@dialogue:S_95.B05_E00.line002')
    stage.say('@dialogue:S_95.B05_E00.line003')
    stage.say('@dialogue:S_95.B05_E00.line004')
    stage.say('@dialogue:S_95.B05_E00.line005')
    stage.say('@dialogue:S_95.B05_E00.line006')
    pass
    gvars['linchong.b05_arrived'] = 1
    gvars['linchong.event_b05_e00'] = 1
    gvars['linchong.duel_win'] = 0
    gvars['linchong.exit_count'] = 0
    stage.showWinCondition('임충을 포함한 아군 두 명을 선착장으로 이동시키고 주귀를 보호하십시오.')
    stage.heightLight(18, 7, 18, 8)
    stage.center(3, 6)
    stage.startOper()
    return False

def scene1():
    if (stage.unitStateTest(512, 7, 0, 2) or stage.round() > 10 or stage.unitStateTest(516, 7, 0, 2)):
        stage.say('@dialogue:S_95.B05_E99.line001')
        stage.end()
        stage.lose()
        return True
    gvars['linchong.exit_count'] = 0
    if (stage.isInPos(512, 18, 7) or stage.isInPos(512, 18, 8)) and not stage.unitStateTest(512, 7, 0, 2):
        gvars['linchong.exit_count'] += 1
    if (stage.isInPos(514, 18, 7) or stage.isInPos(514, 18, 8)) and not stage.unitStateTest(514, 7, 0, 2):
        gvars['linchong.exit_count'] += 1
    if (stage.isInPos(515, 18, 7) or stage.isInPos(515, 18, 8)) and not stage.unitStateTest(515, 7, 0, 2):
        gvars['linchong.exit_count'] += 1
    if (stage.isInPos(516, 18, 7) or stage.isInPos(516, 18, 8)) and not stage.unitStateTest(516, 7, 0, 2):
        gvars['linchong.exit_count'] += 1
    if not gvars['linchong.event_b05_e01'] and (stage.round() >= 1 and stage.curCamp() == 0):
        gvars['linchong.event_b05_e01'] = 1
        stage.say('@dialogue:S_95.B05_E01.line001')
        stage.say('@dialogue:S_95.B05_E01.line002')
        pass
        stage.unit(516).hide(1)
        stage.createMine([{'id': 516, 'i': 4, 'idx': 4, 'x': 11, 'y': 5, 'lv': 3, 'dir': 2, 'ai': 2}])
        # Existing-engine adaptation: set_commandable:zhu:true
        # Existing-engine adaptation: codex:zhu:COMMANDABLE_TEMP
        gvars['linchong.b05_guide'] = 1
    if not gvars['linchong.event_b05_e02'] and (stage.isInRect(512, 11, 5, 13, 7) or stage.isInRect(516, 11, 5, 13, 7)):
        gvars['linchong.event_b05_e02'] = 1
        stage.say('@dialogue:S_95.B05_E02.line001')
        stage.say('@dialogue:S_95.B05_E02.line002')
        pass
        # Existing-engine adaptation: buff:all_allies:move:+1:2turn
        gvars['linchong.b05_signal'] = 1
        # Existing-engine adaptation: play_fx:signal_arrow
    if not gvars['linchong.event_b05_e04'] and (stage.round() >= 4 and stage.curCamp() == 0):
        gvars['linchong.event_b05_e04'] = 1
        stage.say('@dialogue:S_95.B05_E04.line001')
        if gvars['linchong.recruited_hu']:
            stage.say('@dialogue:S_95.B05_E04.line002')
        if (not (gvars['linchong.recruited_hu'] and not stage.unitStateTest(514, 7, 0, 2))):
            stage.say('@dialogue:S_95.B05_E04.HU_ABSENT.line001')
        pass
        stage.createEnemy([{'id': 557, 'i': 9, 'idx': 9, 'x': 1, 'y': 10, 'lv': 3, 'dir': 2, 'ai': 1}])
        stage.createEnemy([{'id': 558, 'i': 10, 'idx': 10, 'x': 5, 'y': 11, 'lv': 3, 'dir': 2, 'ai': 1}])
    if not gvars['linchong.event_b05_e03'] and (stage.isInRect(512, 8, 10, 10, 12) or stage.isInRect(515, 8, 10, 10, 12) or stage.isInRect(516, 8, 10, 10, 12)):
        gvars['linchong.event_b05_e03'] = 1
        if gvars['linchong.recruited_jin']:
            stage.say('@dialogue:S_95.B05_E03.line001')
        stage.say('@dialogue:S_95.B05_E03.line002')
        stage.say('@dialogue:S_95.B05_E03.line003')
        if (not (gvars['linchong.recruited_jin'] and not stage.unitStateTest(515, 7, 0, 2))):
            stage.say('@dialogue:S_95.B05_E03.JIN_ABSENT.line001')
        pass
        gvars['linchong.b05_hidden_waterway'] = 1
        # Existing-engine adaptation: unlock_optional:EX02
        stage.getItem(212, 0, True, 512)
    if not gvars['linchong.event_b05_e05'] and stage.unitStateTest(552, 7, 52, 1):
        gvars['linchong.event_b05_e05'] = 1
        stage.say('@dialogue:S_95.B05_E05.line001')
        stage.say('@dialogue:S_95.B05_E05.line002')
        pass
        gvars['linchong.b05_captain_wounded'] = 1
    if not gvars['linchong.event_b05_e06'] and (stage.isInPos(512, 18, 7) or stage.isInPos(512, 18, 8)):
        gvars['linchong.event_b05_e06'] = 1
        stage.say('@dialogue:S_95.B05_E06.line001')
        stage.say('@dialogue:S_95.B05_E06.line002')
        pass
        gvars['linchong.b05_lin_dock'] = 1
        gvars['linchong.b05_lin_dock'] = 1
        # Existing-engine adaptation: check_objectives
    if not gvars['linchong.event_b05_e07'] and (gvars['linchong.exit_count'] >= 2 and gvars['linchong.b05_lin_dock']):
        gvars['linchong.event_b05_e07'] = 1
        stage.say('@dialogue:S_95.B05_E07.line001')
        stage.say('@dialogue:S_95.B05_E07.line002')
        pass
        gvars['linchong.b05_escape_ready'] = 1
        # Existing-engine adaptation: complete_objective:escape
    if (not stage.unitStateTest(512, 7, 0, 2) and (gvars['linchong.exit_count'] >= 2 and gvars['linchong.b05_lin_dock'] and not stage.unitStateTest(516, 7, 0, 2))):
        stage.say('@dialogue:S_95.B05_E90.line001')
        stage.say('@dialogue:S_95.B05_E90.line002')
        stage.info('물안개 너머로 양산박의 불빛이 모습을 드러냈다.')
        pass
        stage.getItem(256, 0, True, 512)
        # Existing-engine adaptation: expire_temp:zhu
        # Existing-engine adaptation: unlock:B06
        # Existing-engine adaptation: save_checkpoint
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        if gvars['linchong.b05_hidden_waterway']:
            sel = stage.choice('밀수선을 조사한다\n양산박으로 향한다', -1)
            if sel == 2:
                stage.end()
                stage.jumpScene(193)
        else:
            stage.end()
            stage.jumpScene(193)
        stage.end()
        return True
    return False

def win_condition():
    return not stage.unitStateTest(512, 7, 0, 2) and (gvars['linchong.exit_count'] >= 2 and gvars['linchong.b05_lin_dock'] and not stage.unitStateTest(516, 7, 0, 2))

def lose_condition():
    return stage.unitStateTest(512, 7, 0, 2) or stage.round() > 10 or stage.unitStateTest(516, 7, 0, 2)

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
