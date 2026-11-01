# Source Generated with Decompyle++
# File: R_54.marshal (Python 3.9)


def scene0():
    '촉군의 북상'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(50, 130, 221)
    stage.showHead(0, 194, 221)
    stage.bottomTxt('조조는 촉군을 격파하고,', False, False, False)
    stage.delay(5)
    stage.head(50).hide()
    stage.head(0).move(162, 221)
    stage.delay(5)
    stage.bottomTxt('백제성으로 들어갔다.', True, False, True)
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
            21,
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
    stage.setEventName('촉군의 북상')
    stage.setStageName('백제성  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n끝내 늦고 말았군!\n공명이 벌써 기도를 마치다니!?')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(2)
    stage.say('&1\n쳇, 어복포에서 지체하지만 않았어도\n이번에는 공명을 놓치지 않았을 텐데!')
    stage.unit(1).setAction(0)
    stage.say('&0\n중달,\n다른 제단의 위치는 아직 알아내지 못했는가?')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n예. 용서해 주십시오, 주군.\n&19\n하지만 적벽과 백제성의 제단을 찾았으니\n시간을 조금만 더 주신다면……\n&0\n음.\n중달, 수고해 주게.\n&19\n예.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n다음으로 살펴야 할 것은 촉군의 동향이로군…….')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n첩자의 보고가 곧 도착할 것입니다.')
    stage.unit(20).setAction(0)
    stage.delay(5)
    stage.showUnit(181, 54, 22, 2)
    stage.say('&181\n주군!\n첩자의 급보입니다!')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n소식이 왔느냐!\n촉군은 지금 어디에 있느냐?')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n촉군이 성도의 대군을 움직여\n북쪽 검각으로 집결시키고 있습니다.')
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n검각은 산세를 따라 세운 거대한 관문으로\n지키기는 쉽고 공격하기는 어렵습니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n저도 이 관문에 관해서는 들은 적이 있습니다.\n함락하기가 매우 어려울 것입니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n음……\n허나 지금 공격하지 않을 수는 없다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다. 전군에 명하라!\n검각으로 향한다!\n&181\n명을 받들겠습니다!')
    stage.unit(181).hide()
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 54
    stage.setBattlePos([
        [
            35,
            20],
        [
            35,
            21],
        [
            35,
            19],
        [
            35,
            22],
        [
            36,
            20],
        [
            36,
            21],
        [
            36,
            19],
        [
            36,
            22],
        [
            37,
            20],
        [
            37,
            21],
        [
            37,
            19],
        [
            37,
            22],
        [
            38,
            20],
        [
            38,
            21],
        [
            38,
            19]])
    return False


def scene1():
    if stage.battleTest():
        stage.say('&0\n출발하라!')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n검각은 북관, 동관, 서관 세 관문으로 이루어져 있습니다.\n&20\n우리가 이곳에서 진격하면\n먼저 동관의 저항을 받게 됩니다.')
    if stage.unitClickTest(21):
        stage.say('&21\n주작보옥은 책사 계열 부대만 장비할 수 있습니다.\n&21\n보옥에 깃든 주작의 신력으로\n거센 불길을 일으켜 적군을 불태울 수 있지요.')
    if stage.unitClickTest(3):
        stage.say('&3\n선봉을 맡겨 주시오.\n반드시 승상을 위해 공을 세우겠소.')
    if stage.unitClickTest(19):
        stage.say('&19\n(공명이 성도에서 북상한다는 것은…….\n남은 두 제단이 북쪽에 있다는 뜻이다.)\n&19\n(설마 허도에 정말 또 하나가…….)')
    if stage.unitClickTest(1):
        stage.say('&1\n촉은 산속이라 추울 줄 알았는데\n생각보다 따뜻하군.')
    if stage.unitClickTest(17):
        stage.say('&17\n검각은 산중에 있지요?\n허저 장군께서 출전하시면 분명 공을 세우실 거예요.')
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
