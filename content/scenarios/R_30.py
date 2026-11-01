# Source Generated with Decompyle++
# File: R_30.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            0,
            53,
            29,
            2],
        [
            1,
            68,
            48,
            3],
        [
            2,
            68,
            54,
            3],
        [
            15,
            68,
            60,
            3],
        [
            5,
            68,
            66,
            3],
        [
            20,
            38,
            48,
            1],
        [
            23,
            38,
            54,
            1],
        [
            24,
            38,
            60,
            1],
        [
            21,
            38,
            66,
            1]])
    if vars[724]:
        stage.showUnit(22, 38, 72, 1)
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('장료의 격파')
    stage.setStageName('남군  조조의 저택')
    if vars[724] and vars[727]:
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('&0\n빌어먹을, 동오를 너무 얕보았구나!')
        stage.unit(20).setAction(6)
        stage.say('&20\n아군의 손실이 예상보다 심각한 듯합니다.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(6)
        stage.say('&22\n허도로 돌아가 전열을 정비해야 합니다.')
        stage.unit(22).setAction(0)
        stage.delay(3)
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('&0\n아무래도 다른 방도는 없겠군.')
        stage.say('&0\n문원!')
        stage.unit(2).setAction(6)
        stage.say('&2\n예.\n&0\n그대는 악진, 이전과 함께 합비를 지키게.\n&0\n합비는 동오와 허도를 잇는 요충지이니\n무슨 일이 있어도 지켜야 한다.\n&2\n예.\n명을 받들겠습니다.')
        stage.unit(2).setAction(0)
        stage.say('&0\n나머지 장수들은 나와 함께 허도로 돌아간다.')
    if vars[728] and not vars[724]:
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('&0\n주유, 이 가증스러운 애송이!\n그놈만 아니었다면\n동오는 벌써 내 손에 들어왔을 것을.')
        stage.unit(20).setAction(6)
        stage.say('&20\n아군의 손실이 예상보다 심각한 듯합니다.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('&21\n허도로 돌아가 전열을 정비해야 합니다.')
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('&0\n아무래도 다른 방도는 없겠군.')
        stage.say('&0\n문원!')
        stage.unit(2).setAction(6)
        stage.say('&2\n예.\n&0\n그대는 악진, 이전과 함께 합비를 지키게.\n&0\n합비는 동오와 허도를 잇는 요충지이니\n무슨 일이 있어도 지켜야 한다.\n&2\n예.\n명을 받들겠습니다.')
        stage.unit(2).setAction(0)
        stage.say('&0\n나머지 장수들은 나와 함께 허도로 돌아간다.')
    if vars[724] and not vars[727]:
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('&0\n빌어먹을! 동오를 너무 얕보았다.\n그렇지 않았다면 이토록 참패하지는 않았을 터!')
        stage.unit(20).setAction(6)
        stage.say('&20\n아군의 손실이 예상보다 심각한 듯합니다.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(6)
        stage.say('&22\n허도로 돌아가 전열을 정비해야 합니다.')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('&0\n아무래도 다른 방도는 없겠군.')
        stage.say('&0\n문원!')
        stage.unit(2).setAction(6)
        stage.say('&2\n예.\n&0\n그대는 악진, 이전과 함께 합비를 지키게.\n&0\n합비는 동오와 허도를 잇는 요충지이니\n무슨 일이 있어도 지켜야 한다.\n&2\n예.\n명을 받들겠습니다.')
        stage.unit(2).setAction(0)
        stage.say('&0\n나머지 장수들은 나와 함께 허도로 돌아간다.')
    if not vars[724] and not vars[728]:
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('&0\n원통하구나. 봉효가 살아 있었다면\n이 조조가 어찌 이토록 참패했겠는가.')
        stage.unit(20).setAction(6)
        stage.say('&20\n아군의 손실이 예상보다 심각한 듯합니다.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('&21\n허도로 돌아가 전열을 정비해야 합니다.')
        stage.unit(21).setAction(0)
        stage.delay(3)
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('&0\n아무래도 다른 방도는 없겠군.')
        stage.say('&0\n문원!')
        stage.unit(2).setAction(6)
        stage.say('&2\n예.\n&0\n그대는 악진, 이전과 함께 합비를 지키게.\n&0\n합비는 동오와 허도를 잇는 요충지이니\n무슨 일이 있어도 지켜야 한다.\n&2\n예.\n명을 받들겠습니다.')
        stage.unit(2).setAction(0)
        stage.say('&0\n나머지 장수들은 나와 함께 허도로 돌아간다.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 264, 238)
    stage.showHead(32, 328, 238)
    stage.showHead(76, 392, 238)
    stage.bottomTxt('적벽 전투 뒤,', False, False, False)
    stage.delay(5)
    stage.showHead(2, 443, 129)
    stage.delay(5)
    stage.bottomTxt('조조는 장료에게 합비를 지키게 하고,', True, False, False)
    stage.delay(5)
    stage.head(0).move(264, 158)
    stage.head(0).move(330, 111)
    stage.delay(5)
    stage.bottomTxt('자신은 허도로 돌아갔다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('유비는 공명의 묘책에 따라', True, True, False)
    stage.delay(5)
    stage.head(32).move(290, 223)
    stage.delay(5)
    stage.bottomTxt('형주를 빼앗았다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('한편,', True, True, False)
    stage.delay(5)
    stage.head(76).move(472, 209)
    stage.delay(5)
    stage.bottomTxt('손권은 대군을 일으켜 장료가 지키는 합비를 공격했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(0, 35)
    stage.loadBg(2, 57)
    stage.showUnits([
        [
            76,
            65,
            52,
            3],
        [
            78,
            52,
            42,
            2],
        [
            88,
            52,
            62]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('합비 근교  오군 본진')
    stage.say('&76\n공근이 병으로 쓰러졌다고?')
    stage.unit(78).setAction(0, 1)
    stage.delay(3)
    stage.unit(78).setAction(6)
    stage.say('&78\n예. 의원은 심신이 지친 탓이라 했습니다.\n허, 모두 제가 무능한 탓입니다……')
    stage.unit(76).setAction(2)
    stage.say('&76\n으윽, 어째서 이런 중대한 때에……')
    stage.unit(76).setAction(0)
    stage.say('&76\n노숙, 공근을 잃을 수는 없다.\n가장 뛰어난 의원을 불러 치료하게 하라.\n&78\n예.')
    stage.unit(78).setAction(0)
    stage.say('&78\n주군, 합비는 어찌하시겠습니까?\n주 도독이 안 계시면……\n&76\n음……')
    stage.showUnit(182, 16, 52, 1)
    stage.say('&182\n보고드립니다!')
    stage.unit(182).move(52, 52, 1)
    stage.say('&182\n합비에서 사람을 보내 편지를 전해 왔습니다.\n&76\n편지라고? 어디 보자.\n&182\n예.')
    stage.unit(182).move(57, 52, 1)
    stage.unit(182).setAction(0)
    stage.delay(3)
    stage.unit(76).setAction(5)
    stage.delay(5)
    stage.unit(182).move(52, 52, 1)
    stage.unit(182).setAction(0)
    stage.delay(10)
    stage.unit(76).setAction(2)
    stage.say('&76\n너무 방자하구나!')
    stage.unit(88).setAction(0, 1)
    stage.delay(3)
    stage.unit(88).setAction(6)
    stage.say('&88\n서찰에 무엇이라 쓰였는가?')
    stage.unit(76).setAction(2, 2)
    stage.say('&76\n직접 보아라!')
    stage.unit(88).setAction(0)
    stage.delay(3)
    stage.unit(88).move(57, 62, 1)
    stage.unit(88).move(57, 52, 0)
    stage.delay(3)
    stage.unit(88).setAction(0, 1)
    stage.delay(3)
    stage.say('&88\n성 가까이까지 와서도 감히 싸우지 못하니\n오군은 과연 담력 없는 쥐새끼들이로다.\n싸울 뜻이 없거든 일찍 항복하라.\n&88\n……감히 이토록 불손한 말을!')
    stage.unit(76).setAction(2)
    stage.say('&76\n가증스러운 장료!\n감히 나를 쥐새끼라 부르다니!')
    stage.unit(76).setAction(2, 3)
    stage.delay(3)
    stage.unit(76).setAction(17)
    stage.say('&76\n좋다! 내일 싸움을 건다!\n공근을 대신해 내가 직접 전군을 지휘하겠다!')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            1,
            52,
            70],
        [
            15,
            58,
            70],
        [
            13,
            64,
            70],
        [
            20,
            52,
            40,
            2],
        [
            23,
            58,
            40,
            2],
        [
            24,
            64,
            40,
            2],
        [
            21,
            70,
            40,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.unit(1).setAction(2)
    stage.say('&1\n뭐라고!?\n합비가 동오군에 포위되었다고!')
    stage.unit(20).setAction(6)
    stage.say('&20\n그렇습니다. 방금 보고를 받았습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 3)
    stage.say('&1\n맹덕, 큰일일세.\n즉시 원군을 보내야 하네!\n&0\n기다리게.\n문약, 동오군을 지휘하는 자는 주유인가?')
    stage.unit(20).setAction(6)
    stage.say('&20\n아닙니다. 주유가 병으로 쓰러져\n손권이 직접 군사를 이끈다고 합니다.')
    stage.unit(20).setAction(0)
    model.setFace(3)
    stage.say('&0\n주유가 병으로 쓰러졌다고?\n음, 그런 것이었군.\n그렇다면 걱정할 것 없겠어.')
    stage.unit(1).setAction(2)
    stage.say('&1\n맹덕, 지금이 어떤 때인데\n그토록 태연한가?')
    stage.unit(1).setAction(0)
    model.setFace(0)
    stage.say('&0\n원양, 내가 아무 생각도 없이……\n합비를 문원에게 맡겼다고 생각하는가?')
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n손권은 젊은 혈기에 휩쓸리기 쉽네.\n주유만 없다면\n문원의 상대가 되지 못할 것이야.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n남쪽으로 원정을 떠나느라\n오랫동안 허도를 돌보지 못했다.\n그러니 아직 허도를 떠날 수 없다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n원군을 합비로 보내라.\n모든 군사는 문원의 지휘를 따르게 하라.\n그가 있다면 탈이 없을 것이다.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            2,
            54,
            80],
        [
            10,
            44,
            64,
            1],
        [
            11,
            64,
            64,
            3]])
    stage.draw()
    stage.setStageName('합비  조조군 본진')
    stage.say('&2\n도발하는 내 서신을 보면\n손권이 몹시 노할 것이다.')
    stage.unit(2).setAction(17)
    stage.say('&2\n신중히 대처하면\n반드시 손권을 물리칠 수 있습니다.')
    stage.unit(2).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        2,
        10,
        11], [
        0])
    gvars[4076] = 0
    gvars[4057] = 30
    stage.setBattlePos([
        [
            14,
            5],
        [
            13,
            4],
        [
            11,
            5],
        [
            8,
            2],
        [
            8,
            7],
        [
            12,
            11],
        [
            16,
            11],
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


def scene4():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&2\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(10):
        stage.say('&10\n그런데 그 편지는 참으로 놀라웠습니다.\n장 장군께서 그런 수까지 쓰실 줄은 몰랐습니다.\n&2\n별말씀을. 주군의 전술을 본받았을 뿐입니다.')
    if stage.unitClickTest(11):
        stage.say('&11\n동오군 진영의 위치로 보아\n서문이나 남문을 공격할 듯합니다.')
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
