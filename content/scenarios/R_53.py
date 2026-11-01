# Source Generated with Decompyle++
# File: R_53.marshal (Python 3.9)


def scene0():
    '백색 제단'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
        [
            20,
            64,
            64,
            3],
        [
            19,
            64,
            58,
            3],
        [
            23,
            64,
            52,
            3],
        [
            1,
            44,
            64,
            1],
        [
            3,
            44,
            58,
            1],
        [
            17,
            44,
            52,
            1]])
    stage.draw()
    stage.setEventName('백색 제단')
    stage.setStageName('어복포  조조군 본진')
    model.setFace(1)
    stage.say('&0\n마왕에게 이토록 막강한 법력이 있을 줄이야.\n이 때문에 진군이 너무 오래 지체되었다.')
    model.setFace(0)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n백제성 도착이\n예상보다 많이 늦어진 듯합니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n다만 공명이 길목마다 함정을 놓아 막아서는 것은\n우리가 백제성에 접근하지 못하게 하려는 것일 터.\n백제성 안에 분명 무언가 있겠군.')
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다, 이 기세로 백제성을 공격하라!')
    stage.unit(0).setAction(0)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10)
    gvars[4076] = 0
    gvars[4057] = 53
    stage.setBattlePos([
        [
            13,
            20],
        [
            14,
            20],
        [
            12,
            20],
        [
            15,
            20],
        [
            13,
            21],
        [
            14,
            21],
        [
            12,
            21],
        [
            15,
            21],
        [
            13,
            22],
        [
            14,
            22],
        [
            0,
            0],
        [
            0,
            0],
        [
            0,
            0],
        [
            0,
            0],
        [
            0,
            0]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n전에 아뢰었듯이\n백제성은 안팎으로 성벽이 세 겹이며\n매우 견고하게 지어졌습니다.')
    if stage.unitClickTest(19):
        stage.say('&19\n공명이 우리 접근을 막는 까닭은…….\n주군, 한 가지 가능성만이 떠오릅니다…….\n&0\n……두 번째 제단!')
    if stage.unitClickTest(3):
        stage.say('&3\n형님…….\n운장이 다시 백제성에 돌아왔습니다.')
    if stage.unitClickTest(17):
        stage.say('&17\n어복포에서 청룡보옥을 얻었는데\n무슨 힘을 지녔을까요?\n&20\n초선, 이름 그대로\n사신 중 하나인 청룡의 힘이 담긴 보옥일세.\n&20\n청룡의 힘은 뇌전이라 전해지지.\n&17\n과연 순욱 선생님은 견문이 넓으시군요.')
    if stage.unitClickTest(23):
        stage.say('&23\n청룡보옥을 장비할 수 있는 분은\n기마책사 계열인 사마의 선생뿐인 듯합니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n촉의 장수들은 공명이 마왕에게 씐 것을 아는가?\n&0\n아직 모르겠지.\n지금까지도 공명에게 속고 있을 것이다!\n&3\n……………')
    return False

def __dispatch__():
    label('__dispatch_loop')
    if stage.menuVisible():
        name = f"scene{stage.sceneIndex()}"
        if call(name):
            stage.setMenuVisible(False)
            stage.incSceneIdx()
        else:
            return None
    name = f"scene{stage.sceneIndex()}"
    if not hasFunc(name):
        stage.end()
        return None
    call(name)
    stage.incSceneIdx()
    if stage.menuVisible():
        return None
    goto('__dispatch_loop')
