# Source Generated with Decompyle++
# File: R_28.marshal (Python 3.9)


def scene0():
    stage.bgSound(0)
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.delay(3)
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
            7,
            44,
            52,
            1],
        [
            20,
            64,
            64,
            3],
        [
            23,
            64,
            58,
            3],
        [
            24,
            64,
            52,
            3]])
    stage.draw()
    stage.info('황개의 투항을 받아들인 지 사흘 뒤,\n마침내 약속한 날이 왔다.')
    stage.setStageName('오림  조조군 본진')
    stage.unit(0).setAction(17)
    stage.say('&0\n오늘은 황개와 약속한 날이다.\n청룡기를 단 배가 도착하는 대로\n출발한다!')
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n맹덕!')
    stage.unit(0).setAction(0)
    stage.say('&0\n무슨 일인가, 원양?')
    stage.unit(1).setAction(3)
    stage.say('&1\n맹덕, 방금 밖을 순찰하다 보니\n바람의 방향이 바뀌었더군.')
    stage.unit(1).setAction(0)
    stage.say('&1\n순풍이던 바람이 이제 역풍으로 바뀌었군.\n어쩐지 불길한 예감이 드네.\n&0\n원양, 자네답지 않군.\n바람 따위는 신경 쓰지 말게.')
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다! 모두 출진을 준비하라!')
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        2,
        21])
    gvars[4076] = 0
    gvars[4057] = 28
    stage.setBattlePos([
        [
            16,
            21],
        [
            8,
            20],
        [
            15,
            21],
        [
            16,
            20],
        [
            25,
            22],
        [
            25,
            23],
        [
            17,
            8],
        [
            18,
            8],
        [
            7,
            10],
        [
            10,
            13],
        [
            27,
            15],
        [
            25,
            5],
        [
            9,
            19],
        [
            24,
            21],
        [
            23,
            20]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n맹덕, 자꾸 불길한 예감이 드는군.')
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 전장의 지형은 어떠한가?\n&20\n예. 전장은 거의 전부 물입니다.\n&0\n남쪽은 배, 북쪽은 말이라는 말이 틀리지 않군.\n허나 지형이 대부분 물로 바뀌었다 해도\n전체 형세는 여전히 우리에게 유리하다.')
    if stage.unitClickTest(15):
        stage.say('&15\n물속에서 싸운다고요? 에휴!\n&1\n허저 장군, 무슨 일인가?\n왜 한숨을 쉬는가?\n&15\n예? 아…… 아무것도 아닙니다……!\n&1\n설마 허저 장군은 헤엄을 못 치는 건가…….\n&15\n아…… 아닙니다!! 그렇습니다!!')
    if stage.unitClickTest(7):
        stage.say('&7\n이상하군. 어째서 바람이 바뀌었지?\n이 계절에는 북서풍이 불어야 하는데……')
    if stage.unitClickTest(24):
        stage.say('&24\n건너편에 유비군의 깃발이 보이지 않습니다.\n유비와 공명은 강 건너 불구경을 할 생각인 듯합니다.')
    if stage.unitClickTest(23):
        stage.say('&23\n황개가 진심으로 귀순하는 것 같지 않습니다.\n&0\n하하, 문화.\n의심만 해서는 싸움을 할 수 없네.')
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
