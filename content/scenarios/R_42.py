# Source Generated with Decompyle++
# File: R_42.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 72, 131)
    stage.showHead(35, 8, 176)
    stage.bottomTxt('조조', False, False, False)
    stage.delay(5)
    stage.head(35).hide()
    stage.delay(5)
    stage.bottomTxt('촉군을 물리쳤고,', True, False, False)
    stage.delay(8)
    stage.head(0).move(29, 173)
    stage.delay(5)
    stage.bottomTxt('마침내 성도를 점령했다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('유비가 세운 촉은 이로써 멸망했다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 67)
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setEventName('촉의 멸망')
    stage.setStageName('성도 교외  산길')
    stage.showUnit(35, 49, 95)
    stage.unit(35).move(49, 85, 0)
    stage.showUnits([
        [
            69,
            45,
            95],
        [
            50,
            53,
            95]])
    stage.unitsMove([
        [
            stage.unit(35),
            49,
            45,
            0],
        [
            stage.unit(69),
            45,
            55,
            0],
        [
            stage.unit(50),
            53,
            55,
            0]])
    stage.delay(3)
    stage.unit(35).setAction(0, 2)
    stage.say('&35\n두 사람 모두 다쳤느냐?')
    stage.unit(69).setAction(6)
    stage.say('&69\n나는 괜찮다.')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unit(50).setAction(6)
    stage.say('&50\n흥, 부상 같은 것은 내게 있을 수 없다.')
    stage.unit(50).setAction(0)
    stage.say('&35\n그런가……?\n촉은 멸망했지만\n살아 있는 한 반드시 다시 기회는 온다.')
    stage.unit(69).setAction(6)
    stage.say('&69\n공명 대감, 앞으로 어찌해야 하겠습니까?')
    stage.unit(69).setAction(0)
    stage.say('&35\n따라오면 알게 될 것이다.\n생각이 하나 있구나……\n&69\n아……')
    stage.unit(69).setAction(0, 2)
    stage.delay(3)
    stage.unit(69).setAction(2)
    stage.say('&69\n그나저나 유선 주군은 참으로 볼품없이 죽었군.\n저런 주군도 너무 무능하지 않은가.')
    stage.unit(50).setAction(0, 2)
    stage.say('&50\n그렇지. 그런 자를 섬길 가치는 없네.')
    stage.unit(35).setAction(2, 2)
    stage.say('&35\n강유, 마속!\n유선 주군을 다시 헐뜯으면\n용서하지 않겠다!')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unit(69).setAction(0, 0)
    stage.unit(50).setAction(0, 0)
    stage.delay(5)
    stage.unit(35).setAction(0, 2)
    stage.say('&35\n모든 책임은 내게 있다.\n선주 유비께서 유선 주군을 내게 맡기셨으니.')
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n원망하려거든 나를 원망하라!')
    stage.unit(69).setAction(6)
    stage.say('&69\n공명 대감…….\n&50\n………\n&35\n(유비 주군…… 면목이 없습니다.)')
    stage.unit(35).setAction(0, 2)
    stage.say('&35\n가자.')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(35),
            49,
            5,
            0],
        [
            stage.unit(50),
            53,
            5,
            0],
        [
            stage.unit(69),
            45,
            5,
            0]])
    stage.unit(35).hide()
    stage.unit(50).hide()
    stage.unit(69).hide()
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            0,
            32,
            54,
            1],
        [
            1,
            50,
            70],
        [
            8,
            56,
            70],
        [
            4,
            62,
            70],
        [
            5,
            68,
            70],
        [
            12,
            74,
            70],
        [
            20,
            50,
            40,
            2],
        [
            19,
            56,
            40,
            2],
        [
            24,
            62,
            40,
            2],
        [
            21,
            68,
            40,
            2],
        [
            23,
            74,
            40,
            2]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.info('조조는 촉을 다스리는 데 힘쓰는 한편\n도주한 공명을 찾고 있었다.')
    stage.setStageName('성도  임시 승상부')
    stage.say('&0\n중덕, 이 문서를 몇 부 작성하여\n촉의 옛 관리들에게 보내게.\n복직시킬 만한 자는 계속 기용하겠네.')
    stage.unit(21).setAction(6)
    stage.say('&21\n예.')
    stage.unit(21).setAction(0)
    stage.say('&0\n순유, 백성들의 사정은 어떠한가?')
    stage.unit(24).setAction(6)
    stage.say('&24\n예.\n백성들 대부분이 아직 촉을 그리워하며\n일할 생각도 없이 온종일 울고 있습니다.')
    stage.unit(24).setAction(0)
    stage.say('&0\n음, 그럴 만도 하군…….')
    stage.unit(0).setAction(3)
    stage.say('&0\n좋다. 창고를 열어 백성에게 양곡을 나누어 주고\n촉의 백성들에게 한 해 동안 세금을 면해 주어라.')
    stage.unit(0).setAction(0)
    stage.say('&0\n이제 촉은 잊고\n위나라 백성으로 편히 살도록 하라.\n민심을 어루만져 우리에게 귀순하도록 해야 한다.')
    stage.unit(24).setAction(6)
    stage.say('&24\n예, 즉시 시행하겠습니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n치안도 미리 대비해 문제를 막은 듯합니다.')
    stage.unit(23).setAction(0)
    stage.say('&0\n음. 하지만 군기를 느슨히 해서는 안 된다.\n백성의 재물을 약탈하는 자는\n관직의 높낮이를 막론하고 모두 참수하라.')
    stage.unit(23).setAction(6)
    stage.say('&23\n예.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n좋다. 내정은 우선 이 정도로 해 두자.')
    stage.unit(0).setAction(0)
    stage.say('&0\n이제 남은 것은 공명뿐이다.\n사마의, 아직도 공명의 행방을 찾지 못했는가?')
    stage.unit(19).setAction(6)
    stage.say('&19\n예. 하지만 이미 많은 사람을 풀었으니\n곧 결과가 나올 것입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n오, 어찌 이리 오래 걸렸느냐!?')
    stage.showUnit(181, 90, 54, 3)
    stage.say('&181\n아룁니다.')
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 공명의 행방을 알아냈습니다.')
    stage.unit(0).setAction(3)
    model.setFace(0)
    stage.say('&0\n오! 소식을 알아냈느냐?\n그는 지금 어디에 있느냐?')
    stage.unit(0).setAction(0)
    stage.say('&181\n주군께 아룁니다.\n공명이 잔병을 수습하여 백제성에 틀어박혔습니다.\n&0\n백제성이라고!?\n그렇군.')
    stage.unit(20).setAction(6)
    stage.say('&20\n백제성은 유비가 숨을 거둔 곳이자\n가장 험준한 요충지입니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n음, 공명 그 어린놈이\n옛 주군의 망령과 함께 우리를 막겠다는 것인가?')
    stage.unit(0).setAction(3)
    stage.say('&0\n좋다.\n군사들에게 방금 맡긴 일을 서둘러 마치도록 전하라.\n나머지 군사는 출진을 준비하라.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n하루빨리 공명을 제거해야 한다.\n백제성으로 진군하라!')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 25, 175)
    stage.bottomTxt('성도가 함락된 뒤,', False, False, False)
    stage.delay(8)
    stage.showHead(35, 161, 217)
    stage.delay(5)
    stage.bottomTxt('제갈량을 비롯한 촉의 옛 신하들은 백제성으로 달아났다.', True, True, False)
    stage.delay(10)
    stage.head(0).move(97, 217)
    stage.delay(5)
    stage.bottomTxt('이에 조조군은 추격을 시작했다.', True, True, True)
    stage.delay(3)
    stage.bgSound(15)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
        [
            1,
            44,
            64,
            1],
        [
            15,
            44,
            58,
            1],
        [
            2,
            44,
            52,
            1],
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
            3]])
    stage.draw()
    stage.setStageName('백제성 밖  조조군 본진')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        19])
    gvars[4076] = 0
    gvars[4057] = 42
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


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출진하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 백제성의 구조를 설명하게.\n&20\n예.\n백제성은 성도와 마찬가지로 삼중 구조입니다.\n&20\n다만 성도보다 좁아서\n오히려 움직이기가 더 어렵습니다.')
    if stage.unitClickTest(23):
        stage.say('&23\n주작보옥은 책사 계열 부대만 장비할 수 있습니다.\n&23\n이 보물이 있으면\n최고의 화계 책략인 주작을 사용할 수 있습니다.')
    if stage.unitClickTest(15):
        stage.say('&15\n마속도 얕볼 놈은 아니군요.')
    if stage.unitClickTest(19):
        stage.say('&19\n백제성의 방위가 조금 이상합니다.\n&19\n예상보다 함락하기 어려울지도 모릅니다.')
    if stage.unitClickTest(2):
        stage.say('&2\n촉의 옛 신하들이 백제성에 있다고……?\n나라는 사라져도 그리움은 남는 법.\n참으로 마음이 아프구나.')
    if stage.unitClickTest(1):
        stage.say('&1\n이번 싸움에서 적을 모두 황천으로 보내 주겠다!')
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
