# 임충전 — generated from source PRD, original Jojo BattleScreen APIs.
def scene0():
    stage.initFight()
    stage.setGlobalData(11, 0, -1, 512, 1, 1)
    if not gvars['linchong.gear_initialized_lin']:
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars['linchong.gear_initialized_lin'] = 1
    if not gvars['linchong.gear_initialized_hu']:
        stage.setJoinEquip(514, 64, 1, 33, 1, 0)
        gvars['linchong.gear_initialized_hu'] = 1
    if not gvars['linchong.gear_initialized_jin']:
        stage.setJoinEquip(515, 65, 1, 36, 1, 0)
        gvars['linchong.gear_initialized_jin'] = 1
    stage.createMine([{'id': 512, 'i': 0, 'idx': 0, 'x': 4, 'y': 5, 'lv': 0, 'dir': 2, 'ai': 2}])
    if gvars['linchong.recruited_hu']:
        stage.createMine([{'id': 514, 'i': 1, 'idx': 1, 'x': 5, 'y': 7, 'lv': 0, 'dir': 2, 'ai': 2}])
    stage.createEnemy([{'id': 524, 'i': 3, 'idx': 3, 'x': 15, 'y': 6, 'lv': 5, 'dir': 2, 'ai': 1, 'hide': 1}])
    stage.createEnemy([{'id': 525, 'i': 4, 'idx': 4, 'x': 16, 'y': 5, 'lv': 3, 'dir': 2, 'ai': 3, 'targetId': 512, 'hide': 1}])
    stage.createEnemy([{'id': 548, 'i': 5, 'idx': 5, 'x': 12, 'y': 8, 'lv': 3, 'dir': 2, 'ai': 1, 'hide': 1}])
    stage.createEnemy([{'id': 549, 'i': 6, 'idx': 6, 'x': 14, 'y': 9, 'lv': 3, 'dir': 2, 'ai': 1, 'hide': 1}])
    stage.createEnemy([{'id': 550, 'i': 7, 'idx': 7, 'x': 17, 'y': 9, 'lv': 3, 'dir': 2, 'ai': 1, 'hide': 1}])
    stage.createFriend([{'id': 515, 'i': 2, 'idx': 2, 'x': 8, 'y': 8, 'lv': 2, 'dir': 2, 'ai': 2}])
    stage.infoTransfer(4, '100\n1\n512\nwind_spear')
    stage.infoTransfer(4, '103\n1\n514\niron_guard')
    stage.infoTransfer(4, '104\n1\n515\nquick_step')
    stage.loadBg(62)
    stage.draw()
    stage.setWinCondition('승리 조건\n육겸 격퇴; 임충 생존\n\n패배 조건\n임충 HP 0; 불길에 갇힌 임충에게 탈출 경로 없음; 11턴 종료')
    stage.say('@dialogue:S_94.interlude.line001')
    stage.say('@dialogue:S_94.interlude.line002')
    if gvars['linchong.recruited_hu']:
        stage.say('@dialogue:S_94.interlude.line003')
    stage.say('@dialogue:S_94.interlude.line004')
    stage.say('@dialogue:S_94.B04_E00.line001')
    stage.say('@dialogue:S_94.B04_E00.line002')
    stage.say('@dialogue:S_94.B04_E00.line003')
    stage.say('@dialogue:S_94.B04_E00.line004')
    if (not (gvars['linchong.recruited_hu'] and not stage.unitStateTest(514, 7, 0, 2))):
        stage.say('@dialogue:S_94.B04_E00.HU_ABSENT.line001')
    pass
    # Existing-engine adaptation: start_weather:BLIZZARD_VISUAL
    stage.unit(524).setAI(2)
    stage.unit(525).setAI(2)
    stage.unit(548).setAI(2)
    stage.unit(549).setAI(2)
    stage.unit(550).setAI(2)
    gvars['linchong.event_b04_e00'] = 1
    gvars['linchong.duel_win'] = 0
    gvars['linchong.exit_count'] = 0
    stage.setUnitStatus([{'unit':stage.unit(515),'hp':-31}])
    stage.startOper()
    return False

def scene1():
    if (stage.unitStateTest(512, 7, 0, 2) or stage.round() > 10):
        stage.say('@dialogue:S_94.B04_E99.line001')
        stage.end()
        stage.lose()
        return True
    if not gvars['linchong.event_b04_e02'] and (stage.round() >= 3 and stage.curCamp() == 0):
        gvars['linchong.event_b04_e02'] = 1
        stage.say('@dialogue:S_94.B04_E02.line001')
        stage.say('@dialogue:S_94.B04_E02.line002')
        pass
        # Existing-engine adaptation: apply_weather:SNOW_MOVE_MINUS_1
        # Existing-engine adaptation: enable_interact:jin
        # Existing-engine adaptation: tip:WEATHER
    if not gvars['linchong.event_b04_e03'] and ((stage.round() >= 3 and stage.curCamp() == 0) and gvars['linchong.event_b04_e02']):
        gvars['linchong.event_b04_e03'] = 1
        stage.say('@dialogue:S_94.B04_E03.line001')
        stage.say('@dialogue:S_94.B04_E03.line002')
        stage.say('@dialogue:S_94.B04_E03.line003')
        stage.say('@dialogue:S_94.B04_E03.line004')
        stage.say('@dialogue:S_94.B04_E03.line005')
        pass
        stage.setFires(True, [[13, 4], [14, 4], [13, 5]])
        # Existing-engine adaptation: set_blocked:charred_letter:false
        stage.unit(524).show()
        stage.unit(524).setAI(1, 512)
        stage.unit(525).show()
        stage.unit(525).setAI(1, 512)
        stage.unit(548).show()
        stage.unit(548).setAI(1, 512)
        stage.unit(549).show()
        stage.unit(549).setAI(1, 512)
        stage.unit(550).show()
        stage.unit(550).setAI(1, 512)
        # Existing-engine adaptation: remove_status:all_enemies:HIDDEN
        gvars['linchong.b04_fire_revealed'] = 1
        # Existing-engine adaptation: play_fx:warehouse_fire
    if not gvars['linchong.event_b04_e04'] and ((stage.isNear(512, 515, True) or stage.isNear(514, 515, True)) and stage.round() >= 3):
        gvars['linchong.event_b04_e04'] = 1
        stage.say('@dialogue:S_94.B04_E04.line001')
        stage.say('@dialogue:S_94.B04_E04.line002')
        stage.say('@dialogue:S_94.B04_E04.line003')
        stage.say('@dialogue:S_94.B04_E04.line004')
        pass
        stage.unit(515).hide(1)
        stage.createMine([{'id': 515, 'i': 3, 'idx': 3, 'x': 8, 'y': 8, 'lv': 2, 'dir': 2, 'ai': 2}])
        # Existing-engine adaptation: set_commandable:jin:true
        # Existing-engine adaptation: remove_status:jin:INJURED
        stage.setUnitStatus([{'unit':stage.unit(515),'hp':20}])
        gvars['linchong.b04_jin_rescued'] = 1
        # Existing-engine adaptation: codex:jin:COMMANDABLE
    if not gvars['linchong.event_b04_e05'] and ((stage.round() >= 5 and stage.curCamp() == 0) and (not gvars['linchong.b04_boss_down'])):
        gvars['linchong.event_b04_e05'] = 1
        stage.say('@dialogue:S_94.B04_E05.line001')
        stage.say('@dialogue:S_94.B04_E05.line002')
        pass
        stage.createEnemy([{'id': 551, 'i': 8, 'idx': 8, 'x': 17, 'y': 12, 'lv': 3, 'dir': 2, 'ai': 1}])
        stage.setFires(True, [[12, 4], [15, 4], [12, 5], [14, 5], [13, 6], [14, 6]])
    if not gvars['linchong.event_b04_e07'] and stage.unitStateTest(524, 7, 64, 1):
        gvars['linchong.event_b04_e07'] = 1
        stage.say('@dialogue:S_94.B04_E07.line001')
        stage.say('@dialogue:S_94.B04_E07.line002')
        pass
        # Existing-engine adaptation: buff:luk:attack:+5:2turn
        gvars['linchong.b04_luqian_desperate'] = 1
    if not gvars['linchong.event_b04_e08'] and stage.unitStateTest(524, 7, 0, 2):
        gvars['linchong.event_b04_e08'] = 1
        stage.say('@dialogue:S_94.B04_E08.line001')
        stage.say('@dialogue:S_94.B04_E08.line002')
        stage.say('@dialogue:S_94.B04_E08.line003')
        pass
        gvars['linchong.b04_boss_down'] = 1
        # Existing-engine adaptation: stop_fire_expansion
    if not gvars['linchong.event_b04_e06'] and (stage.isInRect(512, 13, 6, 15, 8) and gvars['linchong.b04_fire_revealed']):
        gvars['linchong.event_b04_e06'] = 1
        stage.say('@dialogue:S_94.B04_E06.line001')
        stage.say('@dialogue:S_94.B04_E06.line002')
        pass
        stage.getItem(210, 0, True, 512)
        gvars['linchong.b04_evidence'] = 1
    if not gvars['linchong.event_b04_e10'] and stage.isInRect(512, 2, 3, 4, 5):
        gvars['linchong.event_b04_e10'] = 1
        stage.say('@dialogue:S_94.B04_E10.line001')
        stage.say('@dialogue:S_94.B04_E10.line002')
        pass
        stage.getItem(149, 0, True, 512)
        gvars['linchong.b04_early_boots'] = 1
    if not gvars['linchong.event_b04_e01'] and (stage.round() >= 2 and stage.curCamp() == 0):
        gvars['linchong.event_b04_e01'] = 1
        if gvars['linchong.recruited_hu']:
            stage.say('@dialogue:S_94.B04_E01.line001')
        stage.say('@dialogue:S_94.B04_E01.line002')
        pass
        gvars['linchong.b04_whisper'] = 1
        # Existing-engine adaptation: reveal:footprints
    if not gvars['linchong.event_b04_e09'] and (stage.round() >= 5 or stage.round() >= 5):
        gvars['linchong.event_b04_e09'] = 1
        stage.say('@dialogue:S_94.B04_E09.line001')
        pass
        # Existing-engine adaptation: tip:FIRE_DANGER
    if (not stage.unitStateTest(512, 7, 0, 2) and stage.unitStateTest(524, 7, 0, 2)):
        stage.say('@dialogue:S_94.B04_E90.line001')
        if gvars['linchong.recruited_hu']:
            stage.say('@dialogue:S_94.B04_E90.line002')
        stage.say('@dialogue:S_94.B04_E90.line003')
        stage.say('@dialogue:S_94.B04_E90.line004')
        stage.say('@dialogue:S_94.B04_E90.line005')
        if (not (gvars['linchong.recruited_hu'] and not stage.unitStateTest(514, 7, 0, 2))):
            stage.say('@dialogue:S_94.B04_E90.HU_ABSENT.line001')
        if (not gvars['linchong.b04_jin_rescued']):
            stage.say('@dialogue:S_94.B04_E90.JIN_NOT_RESCUED.line001')
        pass
        stage.getItem(208, 0, True, 512)
        if not gvars['linchong.b04_early_boots']:
            pass
            stage.getItem(149, 0, True, 512)
        stage.infoTransfer(4, '105\n1\n512\nsnow_resolve')
        # Existing-engine adaptation: set_title:lin:표자두
        if gvars['linchong.b04_jin_rescued']:
            pass
            gvars['linchong.recruited_jin'] = 1
            model.unitJoin(515, 0, 0)
        if gvars['linchong.b04_jin_rescued']:
            pass
            # Existing-engine adaptation: codex:jin:PERMANENT
        # Existing-engine adaptation: unlock:B05
        # Existing-engine adaptation: save_checkpoint
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage.end()
        return True
    return False

def win_condition():
    return not stage.unitStateTest(512, 7, 0, 2) and stage.unitStateTest(524, 7, 0, 2)

def lose_condition():
    return stage.unitStateTest(512, 7, 0, 2) or stage.round() > 10

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
