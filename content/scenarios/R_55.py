# Source Generated with Decompyle++
# File: R_55.marshal (Python 3.9)


def scene0():
    '''제단을 찾아라!'''
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(16)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 166, 226)
    stage.showHead(50, 13, 190)
    stage.bottomTxt('촉군은 성도에서 북상하여', False, False, False)
    stage.delay(5)
    stage.head(50).move(76, 147)
    stage.delay(5)
    stage.bottomTxt('검각을 사수했다.', True, False, False)
    stage.delay(10)
    stage.head(0).move(140, 187)
    stage.delay(5)
    stage.bottomTxt('조조군은 승기를 타고 추격하여,', True, True, False)
    stage.delay(5)
    stage.head(50).hide()
    stage.delay(5)
    stage.head(0).move(108, 167)
    stage.bottomTxt('치열한 전투 끝에 검각을 돌파했다.', True, False, True)
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
    stage.setEventName('제단을 찾아라!')
    stage.setStageName('검각  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n가증스러운 공명! 검각에 없다면\n틀림없이 다른 제단으로 갔을 것이다!')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(2)
    stage.say('&1\n빌어먹을! 공명, 그놈은 대체 어디로 갔단 말인가!?')
    stage.unit(1).setAction(0)
    stage.delay(5)
    stage.showUnit(19, 54, 22, 2)
    stage.say('&19\n주군, 제단의 위치를 알아냈습니다!')
    stage.unit(0).setAction(3)
    stage.say('&0\n마침내 알아냈느냐?\n참으로 오래 기다렸구나.')
    stage.unit(0).setAction(0)
    stage.unit(19).move(54, 58, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n고서에 따르면 네 제단은\n동서남북 네 방위에 있어야 합니다.\n&19\n우선 적벽과 백제성의 위치를 보면\n적벽은 남쪽, 백제성은 서쪽이 틀림없습니다.')
    stage.unit(19).setAction(0)
    stage.say('&19\n적벽과 백제성의 위치를 기준으로 방위를 계산하면……')
    stage.unit(19).setAction(6)
    stage.say('&19\n북쪽 제단은 장안 일대에,\n동쪽 제단은 허도 부근에 있을 것으로 보입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n장안과 허도라면 안심해도 되겠군.\n제단을 짓는 것을 우리 사람들이 놓칠 리 없지!')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n하지만 장군, 공명의 계략을 경계하셔야 합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n주군, 즉시 사람을 보내 조사해야 한다고 생각합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n그렇습니다. 더는 지체할 수 없습니다!')
    stage.unit(19).setAction(0)
    stage.say('&0\n음.\n좋다……가후.')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n여기 있습니다.')
    stage.unit(23).setAction(0)
    stage.say('&0\n여기서는 허도보다 장안이 가깝다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n즉시 대군을 장안으로 보내려 했으나\n대군으로는 움직임이 너무 느릴 듯하군.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n그대는 장안 일대의 지리에 밝으니\n소수의 병력을 이끌고 장안으로 가서\n제단의 위치를 조사하도록 하라!')
    stage.unit(23).setAction(6)
    stage.say('&23\n예. 안심하십시오, 주군.')
    stage.unit(23).setAction(0)
    stage.say('&0\n……초선!')
    stage.unit(17).setAction(0, 2)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n예.')
    stage.say('&0\n허도로 가서 조사하도록 하게.\n유비의 유서를 전했던 두 조력자도 있겠지?\n&17\n예. 그분들이 소녀를 도와주실 것입니다.\n&17\n그럼 소녀는 즉시 출발하겠습니다.')
    stage.unit(17).setAction(0)
    stage.unit(17).move(54, 52, 1)
    stage.unitsMove([
        [
            stage.unit(17),
            54,
            40,
            0],
        [
            stage.unit(23),
            54,
            52,
            3]])
    stage.unitsMove([
        [
            stage.unit(17),
            54,
            22,
            0],
        [
            stage.unit(23),
            54,
            34,
            0]])
    stage.unit(17).hide()
    stage.unit(23).move(54, 22, 0)
    stage.unit(23).hide()
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다. 우리도 진군을 개시한다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n하지만 주군, 장안으로 가려면 양평관을 지나야 합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n그렇습니다.\n양평관과 검각은 촉의 양대 관문으로 불립니다.\n&19\n공명도 우리가 순순히 통과하도록 두지는 않을 것입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n이렇게 된 이상 결코 물러설 수 없다.\n모든 장애를 돌파하고 나아갈 뿐이다!')
    stage.unit(0).setAction(0)
    stage.unit(19).move(64, 58, 3)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [], [
        17,
        23])
    gvars[4076] = 0
    gvars[4057] = 55
    stage.setBattlePos([
        [
            9,
            35],
        [
            8,
            35],
        [
            10,
            35],
        [
            7,
            35],
        [
            11,
            35],
        [
            9,
            35],
        [
            8,
            35],
        [
            10,
            35],
        [
            7,
            35],
        [
            11,
            35],
        [
            9,
            35],
        [
            8,
            35],
        [
            10,
            35],
        [
            7,
            35],
        [
            11,
            35]])
    return False


def scene1():
    if stage.battleTest():
        stage.say('&0\n출발하라!')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n양평관에는 남쪽과 동쪽, 두 관문이 있습니다.\n&0\n그렇다면 우리는 남쪽 관문을 공격하게 되겠군?\n&20\n그렇습니다.')
    if stage.unitClickTest(19):
        stage.say('&19\n도사 계열 부대는 현무보옥을 사용할 수 있습니다.\n&19\n이 보옥은 모든 부대의 상태를 바꿉니다.\n&0\n상태를 바꾼다고……?\n각종 능력을 높이거나 낮추는 것인가?\n그런데 모든 부대라면………\n&19\n예. 적과 아군을 가리지 않고 작용합니다.\n&3\n그렇다면 적의 능력은 올라가고\n아군의 능력은 내려갈 수도 있다는 말이오?\n&19\n그렇습니다.\n&3\n참으로 위험한 힘이로군!')
    if stage.unitClickTest(3):
        stage.say('&3\n검각과 양평관은 모두 위를 막으려고 세운 곳이다.\n참으로 운명의 장난이로군!')
    if stage.unitClickTest(1):
        stage.say('&1\n백제성으로 진군할 때와 같은 꼴이 나면\n큰일이 아닌가.\n맹덕, 어서 출발하세.\n&0\n음.')
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
