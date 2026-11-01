# 임충전 — generated from source PRD, original Jojo BattleScreen APIs.
def scene0():
    stage.initFight()
    stage.setGlobalData(8, 0, -1, 512, 1, 1)
    if not gvars['linchong.gear_initialized_lin']:
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars['linchong.gear_initialized_lin'] = 1
    if not gvars['linchong.gear_initialized_hu']:
        stage.setJoinEquip(514, 64, 1, 33, 1, 0)
        gvars['linchong.gear_initialized_hu'] = 1
    if not gvars['linchong.gear_initialized_jin']:
        stage.setJoinEquip(515, 65, 1, 36, 1, 0)
        gvars['linchong.gear_initialized_jin'] = 1
    if not gvars['linchong.gear_initialized_yang']:
        stage.setJoinEquip(518, 66, 1, 37, 1, 0)
        gvars['linchong.gear_initialized_yang'] = 1
    stage.createMine([{'id': 512, 'i': 0, 'idx': 0, 'x': 5, 'y': 7, 'lv': 0, 'dir': 2, 'ai': 2}])
    if gvars['linchong.recruited_hu']:
        stage.createFriend([{'id': 514, 'i': 1, 'idx': 1, 'x': 4, 'y': 8, 'lv': 0, 'dir': 2, 'ai': 2}])
    if gvars['linchong.recruited_jin']:
        stage.createFriend([{'id': 515, 'i': 2, 'idx': 2, 'x': 4, 'y': 6, 'lv': 0, 'dir': 2, 'ai': 2}])
    stage.createEnemy([{'id': 518, 'i': 3, 'idx': 3, 'x': 9, 'y': 7, 'lv': 6, 'dir': 2, 'ai': 3, 'targetId': 512}])
    stage.createFriend([{'id': 519, 'i': 4, 'idx': 4, 'x': 14, 'y': 4, 'lv': 5, 'dir': 2, 'ai': 2}])
    stage.createFriend([{'id': 565, 'i': 5, 'idx': 5, 'x': 13, 'y': 5, 'lv': 4, 'dir': 2, 'ai': 2}])
    stage.createFriend([{'id': 516, 'i': 6, 'idx': 6, 'x': 15, 'y': 5, 'lv': 3, 'dir': 2, 'ai': 2}])
    stage.infoTransfer(4, '100\n1\n512\nwind_spear')
    stage.infoTransfer(4, '103\n1\n514\niron_guard')
    stage.infoTransfer(4, '104\n1\n515\nquick_step')
    stage.infoTransfer(4, '108\n1\n518\nswift_blade')
    stage.loadBg(65)
    stage.draw()
    stage.setWinCondition('승리 조건\n양지 HP 35% 이하(비살상) 또는 정식 일기토 승리 또는 7턴까지 임충 생존; 임충 생존; 양지 생존\n\n패배 조건\n임충 HP 0; 양지가 사망 처리됨; 8턴 시작 전 결투 미종결')
    stage.say('@dialogue:S_97.interlude.line001')
    stage.say('@dialogue:S_97.interlude.line002')
    stage.say('@dialogue:S_97.interlude.line003')
    stage.say('@dialogue:S_97.interlude.line004')
    stage.say('@dialogue:S_97.B06_E00.line001')
    stage.say('@dialogue:S_97.B06_E00.line002')
    stage.say('@dialogue:S_97.B06_E00.line003')
    stage.say('@dialogue:S_97.B06_E00.line004')
    stage.say('@dialogue:S_97.B06_E00.line005')
    stage.say('@dialogue:S_97.B06_E00.line006')
    stage.say('@dialogue:S_97.B06_E00.line007')
    stage.say('@dialogue:S_97.B06_E00.line008')
    if ((not (gvars['linchong.recruited_hu'] and not stage.unitStateTest(514, 7, 0, 2))) and (not (gvars['linchong.recruited_jin'] and not stage.unitStateTest(515, 7, 0, 2)))):
        stage.say('@dialogue:S_97.B06_E00.HU_AND_JIN_ABSENT.line001')
    pass
    # Existing-engine adaptation: lock_player_input
    # Existing-engine adaptation: play_dialogue
    # Existing-engine adaptation: unlock_player_input
    gvars['linchong.event_b06_e00'] = 1
    gvars['linchong.duel_win'] = 0
    gvars['linchong.exit_count'] = 0
    stage.startOper()
    return False

def scene1():
    if (stage.unitStateTest(512, 7, 0, 2) or stage.round() > 7):
        stage.say('@dialogue:S_97.B06_E99.line001')
        stage.end()
        stage.lose()
        return True
    if not gvars['linchong.event_b06_e01'] and (stage.round() >= 1 and stage.curCamp() == 0):
        gvars['linchong.event_b06_e01'] = 1
        stage.say('@dialogue:S_97.B06_E01.line001')
        pass
        # Existing-engine adaptation: apply_status:lin:DUEL_ONLY
        # Existing-engine adaptation: apply_status:yang:NONLETHAL
        # Existing-engine adaptation: disable_attack_target:non_lin_to_yang
        # Existing-engine adaptation: tip:DUEL_ONLY
    if not gvars['linchong.event_b06_e02'] and (stage.isNear(512, 518, True) and True):
        gvars['linchong.event_b06_e02'] = 1
        stage.say('@dialogue:S_97.B06_E02.line001')
        stage.say('@dialogue:S_97.B06_E02.line002')
        stage.say('@dialogue:S_97.B06_E02.line003')
        stage.say('@dialogue:S_97.B06_E02.line004')
        stage.say('@dialogue:S_97.B06_E02.line005')
        pass
        fight = stage.startFight(512, 518, 5)
        fight.showStart()
        fight.attack1(True, 1, False)
        fight.attack1(False, 1, False)
        fight.end()
        gvars['linchong.duel_win'] = 1
        gvars['linchong.b06_duel_completed'] = 1
        pass
        gvars['linchong.b06_duel_resolved'] = 1
    if not gvars['linchong.event_b06_e04'] and ((stage.round() >= 2 and stage.curCamp() == 0) and gvars['linchong.b06_song_support']):
        gvars['linchong.event_b06_e04'] = 1
        if (gvars['linchong.ex02_song_support'] or gvars['linchong.b06_song_support']):
            stage.say('@dialogue:S_97.B06_E04.line001')
        stage.say('@dialogue:S_97.B06_E04.line002')
        stage.say('@dialogue:S_97.B06_E04.line003')
        pass
        stage.createMine([{'id': 521, 'i': 7, 'idx': 7, 'x': 12, 'y': 8, 'lv': 4, 'dir': 2, 'ai': 2}])
        # Existing-engine adaptation: codex:song:COMMANDABLE_TEMP
    if not gvars['linchong.event_b06_e05'] and stage.unitStateTest(518, 7, 64, 1):
        gvars['linchong.event_b06_e05'] = 1
        stage.say('@dialogue:S_97.B06_E05.line001')
        stage.say('@dialogue:S_97.B06_E05.line002')
        stage.say('@dialogue:S_97.B06_E05.line003')
        pass
        gvars['linchong.b06_duel_resolved'] = 1
        stage.setUnitStatus([{'unit':stage.unit(518),'hp':-9999}])
        # Existing-engine adaptation: complete_objective:duel
    if not gvars['linchong.event_b06_e06'] and ((stage.round() >= 7 and stage.curCamp() == 2) and (not gvars['linchong.b06_duel_resolved']) and (not gvars['linchong.b06_duel_completed'])):
        gvars['linchong.event_b06_e06'] = 1
        stage.say('@dialogue:S_97.B06_E06.line001')
        stage.say('@dialogue:S_97.B06_E06.line002')
        stage.say('@dialogue:S_97.B06_E06.line003')
        pass
        gvars['linchong.b06_endurance_clear'] = 1
        # Existing-engine adaptation: complete_objective:duel
    if not gvars['linchong.event_b06_e03'] and (stage.round() >= 4 and stage.curCamp() == 0):
        gvars['linchong.event_b06_e03'] = 1
        stage.say('@dialogue:S_97.B06_E03.line001')
        stage.say('@dialogue:S_97.B06_E03.line002')
        stage.say('@dialogue:S_97.B06_E03.line003')
        pass
        # Existing-engine adaptation: buff:yang:attack:+4:2turn
        gvars['linchong.b06_second_phase'] = 1
    if not gvars['linchong.event_b06_e07'] and stage.unitStateTest(512, 7, 38, 1):
        gvars['linchong.event_b06_e07'] = 1
        stage.say('@dialogue:S_97.B06_E07.line001')
        stage.say('@dialogue:S_97.B06_E07.line002')
        pass
        gvars['linchong.b06_hero_low'] = 1
    if not gvars['linchong.event_b06_e08'] and ((stage.round() >= 3 and stage.curCamp() == 0) and gvars['linchong.b04_evidence']):
        gvars['linchong.event_b06_e08'] = 1
        stage.say('@dialogue:S_97.B06_E08.line001')
        stage.say('@dialogue:S_97.B06_E08.line002')
        stage.say('@dialogue:S_97.B06_E08.line003')
        pass
        gvars['linchong.b06_truth_known'] = 1
    if (not stage.unitStateTest(512, 7, 0, 2) and (gvars['linchong.duel_win'] or stage.unitStateTest(518, 7, 64, 1) or stage.round() >= 7)):
        stage.say('@dialogue:S_97.B06_E90.line001')
        stage.say('@dialogue:S_97.B06_E90.line002')
        stage.say('@dialogue:S_97.B06_E90.line003')
        stage.say('@dialogue:S_97.B06_E90.line004')
        stage.say('@dialogue:S_97.B06_E90.line005')
        stage.say('@dialogue:S_97.B06_E90.line006')
        if gvars['linchong.b04_evidence']:
            stage.say('@dialogue:S_97.B06_E90.B04_EVIDENCE.line001')
            stage.say('@dialogue:S_97.B06_E90.B04_EVIDENCE.line002')
        if gvars['linchong.ex02_clear']:
            if gvars['linchong.ex02_song_support'] or gvars['linchong.b06_song_support']:
                stage.say('@dialogue:S_97.B06_E90.EX02_CLEAR.line001')
        pass
        stage.getItem(215, 0, True, 512)
        stage.infoTransfer(4, '107\n1\n512\ntiger_charge')
        # Existing-engine adaptation: set_title:lin:양산박 호걸
        # Existing-engine adaptation: codex:yang:ENCOUNTERED
        gvars['linchong.campaign_complete'] = 1
        # Existing-engine adaptation: expire_temp:song_support
        # Existing-engine adaptation: save_campaign_clear
        vars[0] = 1
        vars[600] = 1
        stage.reward()
        stage.say('@dialogue:S_97.epilogue.line001')
        stage.say('@dialogue:S_97.epilogue.line002')
        stage.say('@dialogue:S_97.epilogue.line003')
        stage.say('@dialogue:S_97.epilogue.line004')
        stage.end()
        return True
    return False

def win_condition():
    return not stage.unitStateTest(512, 7, 0, 2) and (gvars['linchong.duel_win'] or stage.unitStateTest(518, 7, 64, 1) or stage.round() >= 7)

def lose_condition():
    return stage.unitStateTest(512, 7, 0, 2) or stage.round() > 7

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
