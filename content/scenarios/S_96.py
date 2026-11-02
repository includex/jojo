# 임충전 — generated from source PRD, original Jojo BattleScreen APIs.
def scene0():
    stage.initFight()
    stage.setGlobalData(9, 0, -1, 512, 1, 1)
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
    stage.createMine([{'id': 512, 'i': 0, 'idx': 0, 'x': 2, 'y': 6, 'lv': 0, 'dir': 2, 'ai': 2}])
    if gvars['linchong.recruited_hu']:
        stage.createMine([{'id': 514, 'i': 1, 'idx': 1, 'x': 1, 'y': 7, 'lv': 0, 'dir': 2, 'ai': 2}])
    if gvars['linchong.recruited_jin']:
        stage.createMine([{'id': 515, 'i': 2, 'idx': 2, 'x': 3, 'y': 7, 'lv': 0, 'dir': 2, 'ai': 2}])
    stage.createMine([{'id': 516, 'i': 3, 'idx': 3, 'x': 4, 'y': 6, 'lv': 3, 'dir': 2, 'ai': 2}])
    stage.createEnemy([{'id': 559, 'i': 4, 'idx': 4, 'x': 13, 'y': 6, 'lv': 4, 'dir': 2, 'ai': 0}])
    stage.createEnemy([{'id': 560, 'i': 5, 'idx': 5, 'x': 9, 'y': 6, 'lv': 3, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id': 561, 'i': 6, 'idx': 6, 'x': 10, 'y': 8, 'lv': 3, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id': 562, 'i': 7, 'idx': 7, 'x': 14, 'y': 5, 'lv': 3, 'dir': 2, 'ai': 1}])
    stage.createEnemy([{'id': 563, 'i': 8, 'idx': 8, 'x': 12, 'y': 8, 'lv': 3, 'dir': 2, 'ai': 0}])
    stage.createEnemy([{'id':527,'i':31,'x':11,'y':4,'hide':1,'ai':2,'lv':0}])
    stage.createFriend([{'id': 569, 'i': 12, 'x': 12, 'y': 7, 'ai': 2, 'lv': 0}])
    stage.createFriend([{'id': 570, 'i': 13, 'x': 14, 'y': 7, 'ai': 2, 'lv': 0}])
    stage.infoTransfer(4, '100\n1\n512\nwind_spear')
    stage.infoTransfer(4, '103\n1\n514\niron_guard')
    stage.infoTransfer(4, '104\n1\n515\nquick_step')
    stage.infoTransfer(4, '106\n1\n516\nwater_stride')
    stage.loadBg(64)
    stage.draw()
    stage.setWinCondition('승리 조건\n장물 상자 2개 중 1개 이상 확보; 밀수선 두목 격퇴; 임충 생존\n\n패배 조건\n임충 HP 0; 모든 장물 상자 파괴; 9턴 종료')
    stage.say('@dialogue:S_96.EX02_E00.line001')
    if gvars['linchong.recruited_jin']:
        stage.say('@dialogue:S_96.EX02_E00.line002')
    stage.say('@dialogue:S_96.EX02_E00.line003')
    stage.say('@dialogue:S_96.EX02_E00.line004')
    pass
    gvars['linchong.ex02_started'] = 1
    gvars['linchong.event_ex02_e00'] = 1
    gvars['linchong.duel_win'] = 0
    gvars['linchong.exit_count'] = 0
    stage.showWinCondition('임충을 상자 가까이 이동시켜 하나 이상 확보하고 밀수선 두목을 격퇴하십시오.')
    stage.heightLight(12, 7, 12, 7)
    stage.heightLight(14, 7, 14, 7)
    stage.center(2, 6)
    stage.startOper()
    return False

def scene1():
    if (stage.unitStateTest(512, 7, 0, 2) or stage.round() > 9 or (stage.unitStateTest(569, 7, 0, 2) and stage.unitStateTest(570, 7, 0, 2))):
        stage.say('@dialogue:S_96.EX02_E99.line001')
        stage.end()
        stage.lose()
        return True
    if not gvars['linchong.event_ex02_e04'] and ((stage.round() >= 2 and stage.curCamp() == 0) and gvars['linchong.b05_signal']):
        gvars['linchong.event_ex02_e04'] = 1
        if (gvars['linchong.ex02_song_support'] or gvars['linchong.b06_song_support']):
            stage.say('@dialogue:S_96.EX02_E04.line001')
        stage.say('@dialogue:S_96.EX02_E04.line002')
        stage.say('@dialogue:S_96.EX02_E04.line003')
        pass
        stage.createMine([{'id': 521, 'i': 10, 'idx': 10, 'x': 6, 'y': 6, 'lv': 4, 'dir': 2, 'ai': 2}])
        gvars['linchong.ex02_song_support'] = 1
        # Existing-engine adaptation: codex:song:COMMANDABLE_TEMP
    if not gvars['linchong.event_ex02_e01'] and (stage.round() >= 2 and stage.curCamp() == 0):
        gvars['linchong.event_ex02_e01'] = 1
        stage.say('@dialogue:S_96.EX02_E01.line001')
        pass
        gvars['linchong.ex02_bridge_active'] = 1
    if not gvars['linchong.event_ex02_e02'] and (stage.isInRect(512, 11, 6, 13, 8) or stage.isInRect(512, 13, 6, 15, 8)):
        gvars['linchong.event_ex02_e02'] = 1
        stage.say('@dialogue:S_96.EX02_E02.line001')
        stage.say('@dialogue:S_96.EX02_E02.line002')
        pass
        gvars['linchong.ex02_one_cargo'] = 1
        gvars['linchong.ex02_one_cargo'] = 1
    if not gvars['linchong.event_ex02_e03'] and (stage.round() >= 4 and stage.curCamp() == 0):
        gvars['linchong.event_ex02_e03'] = 1
        stage.say('@dialogue:S_96.EX02_E03.line001')
        stage.say('@dialogue:S_96.EX02_E03.line002')
        pass
        stage.createEnemy([{'id': 564, 'i': 9, 'idx': 9, 'x': 15, 'y': 8, 'lv': 3, 'dir': 2, 'ai': 1}])
    if not gvars['linchong.event_ex02_e05'] and stage.unitStateTest(559, 7, 0, 2):
        gvars['linchong.event_ex02_e05'] = 1
        stage.say('@dialogue:S_96.EX02_E05.line001')
        pass
        gvars['linchong.ex02_boss_down'] = 1
    if (not stage.unitStateTest(512, 7, 0, 2) and (stage.unitStateTest(559, 7, 0, 2) and gvars['linchong.ex02_one_cargo'])):
        stage.say('@dialogue:S_96.EX02_E90.line001')
        if (gvars['linchong.ex02_song_support'] or gvars['linchong.b06_song_support']):
            stage.say('@dialogue:S_96.EX02_E90.line002')
        stage.say('@dialogue:S_96.EX02_E90.line003')
        pass
        gvars['linchong.ex02_clear'] = 1
        stage.getItem(257, 0, True, 512)
        stage.getItem(214, 0, True, 512)
        if gvars['linchong.ex02_song_support']:
            pass
            gvars['linchong.b06_song_support'] = 1
        # Existing-engine adaptation: expire_temp:song
        # Existing-engine adaptation: expire_temp:zhu
        # Existing-engine adaptation: unlock:B06
        # Existing-engine adaptation: save_checkpoint
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage.end()
        return True
    return False

def win_condition():
    return not stage.unitStateTest(512, 7, 0, 2) and (stage.unitStateTest(559, 7, 0, 2) and gvars['linchong.ex02_one_cargo'])

def lose_condition():
    return stage.unitStateTest(512, 7, 0, 2) or stage.round() > 9 or (stage.unitStateTest(569, 7, 0, 2) and stage.unitStateTest(570, 7, 0, 2))

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
