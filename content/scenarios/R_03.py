# Source Generated with Decompyle++
# File: R_03.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            127,
            32,
            53,
            1],
        [
            132,
            32,
            44,
            1],
        [
            192,
            50,
            40,
            2],
        [
            193,
            58,
            40,
            2],
        [
            181,
            50,
            70],
        [
            182,
            58,
            70]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('낙양의 대화재')
    stage.setStageName('낙양  승상부')
    stage.unit(127).setAction(0, 0)
    stage.say('&127\n이유, 연합군이 벌써 낙양에 다가왔다.')
    stage.unit(132).setAction(0, 2)
    stage.say('&132\n그렇습니다.')
    stage.unit(127).setAction(3)
    stage.say('&127\n무슨 좋은 생각이 있는가?')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(132).setAction(6)
    stage.say('&132\n낙양 주변은 탁 트인 평지라 수비에 불리합니다.')
    stage.say('&132\n그러니 낙양을 포기하고\n천연의 요새인 장안으로 천도해 재기하시지요.')
    stage.unit(132).setAction(0)
    stage.say('&127\n음, 천도하자는 말인가?')
    stage.unit(127).setAction(0, 3)
    stage.say('&127\n그리하는 수밖에 없겠군.')
    stage.unit(192).setAction(3)
    stage.say('&192\n뭐……뭐라고요!?')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('&193\n자……잠깐만 기다려 주십시오!\n그 일만은 절대로 안 됩니다.')
    stage.unit(127).setAction(0, 1)
    stage.say('&127\n음? 무어냐? 누구 이견이 있느냐?')
    stage.unit(192).setAction(6)
    stage.say('&192\n이 낙양은 유서 깊은 도읍이지요.')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(2)
    stage.say('&127\n못난 놈! 연합군이 곧 들이닥칠 판에\n무슨 옛날이야기란 말이냐?')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('&193\n섣불리 도읍을 옮기면 백성이 고통받을 것입니다.')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(6)
    stage.say('&192\n그렇습니다. 농민은 그 때문에 땅을 잃고\n상인은 가게를 잃을 것입니다.')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.say('&127\n그 때문에 백성이 고통받을 것이라고?')
    stage.unit(127).setAction(0, 3)
    stage.say('&127\n음, 그렇군.')
    stage.unit(127).setAction(-1, 0)
    stage.say('&127\n이유!')
    stage.unit(132).setAction(6)
    stage.say('&132\n예.')
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(3)
    stage.say('&127\n낙양성에 불을 질러 모조리 태워라.\n백성들의 재산도 남김없이 몰수하라.')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(0, 1)
    stage.say('&127\n모든 것을 없애 버리면 땅도 가게도 필요 없지.')
    stage.unit(127).setAction(3)
    stage.say('&127\n하하하하하!!')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(132).setAction(6)
    stage.say('&132\n과연 승상께서는 영명하십니다. 즉시 시행하겠습니다.')
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(0, 3)
    stage.say('&127\n백성들은 그저 일편단심으로 나를 위해 목숨을 바치면 된다.')
    stage.unit(127).setAction(3)
    stage.say('&127\n하하하.')
    stage.unit(193).setAction(6)
    stage.say('&193\n아아!')
    stage.unit(192).setAction(6)
    stage.say('&192\n……악귀나 할 짓이로구나.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 39)
    stage.showUnits([
        [
            153,
            69,
            60],
        [
            17,
            69,
            50,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('낙양  왕윤의 저택')
    stage.unit(153).setAction(3)
    stage.say('&153\n초선아, 좋은 소식이다!\n마침내 연합군이 호로관을 함락했다는구나.')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(3)
    stage.say('&17\n정말인가요? 그럼 곧 낙양으로 쳐들어갈 수 있겠네요?\n아버님?')
    stage.unit(17).setAction(0)
    stage.say('&153\n음!')
    stage.unit(153).setAction(0, 1)
    stage.say('&153\n동탁의 기수도 다했구나.')
    stage.delay(5)
    stage.say('&153\n그러고 보니, 초선아.')
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n동맹군의 참모가 그 조조라더군.')
    if vars[302]:
        stage.unit(17).setAction(3)
        stage.say('&17\n아, 그 조조 대인이셨군요!')
        stage.unit(17).setAction(0)
        stage.say('&153\n음.')
        stage.unit(153).setAction(0, 1)
        stage.say('&153\n과연 기대를 저버리지 않고 반동탁 연합을 성사시켰으니\n이제 마침내 동탁을 없애겠구나.')
        stage.unit(153).setAction(3)
        stage.say('&153\n이 젊은이는 과연 보통 인물이 아니로군.')
        stage.unit(153).setAction(0)
        stage.delay(5)
        stage.unit(153).setAction(0, 0)
        stage.say('&153\n참, 조조가 너를 무척 높이 사더구나.')
        stage.unit(17).setAction(6)
        stage.unit(17).setAction(0, 0)
        stage.say('&17\n아버님도 또 농담을 하시는군요.')
        stage.unit(17).setAction(0, 2)
        stage.delay(3)
        stage.unit(17).setAction(6)
        stage.say('&17\n그런 게 아니에요!')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('&153\n하하하, 이 아비가 말이 많았구나. 하하하.')
        stage.unit(153).setAction(0)
    if not vars[302]:
        stage.say('&17\n……。')
        stage.delay(5)
        stage.say('&17\n……조조 대인?')
        stage.say('&153\n왜 그러시오? 잊으셨소?')
        stage.say('&153\n예전에 태위 대감과 함께 우리 집에 왔었지.')
        stage.unit(17).setAction(6)
        stage.say('&17\n아, 그분이군요?')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('&153\n하하하, 내 딸아!\n자, 조조가 개선할 날을 기다리자꾸나.')
        stage.unit(153).setAction(0)
    stage.delay(5)
    stage.showUnit(181, 55, 30, 2)
    stage.say('&181\n보고드립니다!')
    stage.delay(5)
    stage.unit(17).setAction(0, 0)
    stage.delay(3)
    stage.say('&181\n사도 왕윤 대인께 승상의 명을 전하러 왔다.')
    stage.say('&153\n음? 대체 무슨 일인가?')
    stage.say('&181\n오늘 승상께서 장안 천도를 상주하셨고\n폐하께서도 허락하셨습니다.\n&181\n사도 대인께서는 즉시 장안으로 가 주십시오.')
    stage.unit(153).setAction(3)
    stage.say('&153\n뭐……뭐라고? 천도라니?')
    stage.unit(153).setAction(0)
    stage.delay(5)
    stage.unit(153).setAction(0, 1)
    stage.delay(5)
    stage.say('&153\n(동탁, 이 짐승 같은 놈……!)')
    stage.delay(10)
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n……알겠소. 즉시 준비하겠소.')
    stage.say('&181\n즉시 승상께 돌아가 보고하겠습니다.')
    stage.delay(3)
    stage.unit(181).hide()
    stage.delay(8)
    stage.unit(153).setAction(0, 1)
    stage.say('&153\n제길…… 동탁을 얕본 모양이군.')
    stage.unit(17).setAction(0, 2)
    stage.delay(5)
    stage.unit(17).setAction(6)
    stage.say('&17\n이제 낙양에 머물 수 없는 건가요?')
    stage.unit(17).setAction(0)
    stage.delay(5)
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n안 되오. 성지를 따라야 하오.')
    stage.unit(153).setAction(0, 1)
    stage.say('&153\n또한 폐하를 동탁의 마수에서 지켜야 하오!')
    stage.unit(17).setAction(6)
    stage.say('&17\n아버님…….')
    stage.delay(5)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 361, 45)
    stage.showHead(101, 425, 45)
    stage.showHead(161, 489, 45)
    stage.showHead(136, 361, 125)
    stage.showHead(99, 425, 125)
    stage.showHead(145, 489, 125)
    stage.delay(5)
    stage.showHead(127, 280, 78)
    stage.bottomTxt('동탁은 연합군을 피해,', False, True, False)
    stage.delay(5)
    stage.head(127).move(191, 78)
    stage.bottomTxt('낙양을 불태운 뒤 장안으로 천도했다.', True, True, False)
    stage.delay(5)
    stage.bottomTxt('낙양은 도읍으로서의 역사를 마감했다.', True, True, True)
    stage.bottomTxt('연합군은 큰불을 보고 몹시 놀라', False, True, False)
    stage.head(0).move(280, 45)
    stage.head(101).move(344, 45)
    stage.head(161).move(408, 45)
    stage.head(136).move(280, 125)
    stage.head(99).move(344, 125)
    stage.head(145).move(408, 125)
    stage.bottomTxt('이에 행군을 서둘러 낙양성에 들어갔다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            101,
            54,
            37,
            2],
        [
            0,
            44,
            46,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('낙양  연합군 본진')
    stage.say('&0\n원소 대인, 방금 들어온 소식에 따르면\n동탁이 장안으로 천도하고 있습니다.')
    stage.say('&101\n음? 도읍을 옮긴다고……?')
    stage.say('&0\n그들은 장안으로 가는 중일세…….')
    stage.unit(0).setAction(17)
    stage.say('&0\n지금이 기회다. 즉시 추격해야 한다.')
    stage.unit(0).setAction(0)
    stage.say('&101\n아니다. 힘을 기르며 때를 기다리자.\n어쨌든 낙양은 이미 차지했으니.')
    stage.say('&0\n원소 대인, 우리의 목적은\n이 폐허를 차지하는 것이 아닙니다.')
    stage.delay(5)
    stage.unit(101).setAction(0, 0)
    stage.say('&101\n조조, 지금 무슨 말을 하는지 알고 있나?')
    stage.delay(3)
    stage.unit(101).setAction(0, 2)
    stage.say('&101\n동탁이 우리에게 아무 조치도 취하지 않고\n조용히 장안으로 천도할 것 같은가?')
    stage.unit(101).setAction(3)
    stage.say('&101\n지금 동탁을 추격하는 것은 너무 위험하다!')
    stage.unit(101).setAction(17)
    stage.say('&101\n총대장은 나다. 진격을 멈출 권한도 내게 있다.')
    stage.unit(101).setAction(0)
    stage.say('&0\n으음…….')
    stage.effectSound(203, 255)
    stage.delay(3)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('직접 동탁을 추격한다\n동탁 추격을 포기한다', -1)
    if sel == 1:
        vars[303] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.say('&0\n……좋다.')
        stage.delay(5)
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n나는 직접 추격하겠소. 그대는 낙양에서 승전보나 기다리시오.')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(101).setAction(3)
        stage.say('&101\n그대……지금 무슨 말을……?')
        stage.unit(101).setAction(0)
        stage.say('&101\n조조, 어찌 이리 고집을 부리는가?')
        stage.say('&0\n원소 공, 후회하지 마시오.\n지금이 천하에 무위를 떨칠 때인데\n그대는 이 절호의 기회를 놓치려 하는구려.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n그럼 이만 가 보겠소, 원소 공.')
        stage.unit(0).move(44, 98, 2)
        stage.unit(0).hide()
        stage.delay(5)
        stage.unit(101).setAction(0, 1)
        stage.say('&101\n조조, 이놈은 무엇을 하려는가……?')
        stage.effectSound(203, 255)
        stage.clsUnit()
        return False
    if sel == 2:
        vars[303] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.say('&0\n……좋다. 추격을 멈추어라.')
        stage.unit(101).setAction(3)
        stage.say('&101\n오, 그러한가.')
        stage.unit(101).setAction(0)
        stage.say('&0\n원소 공, 오해하지 마시오.\n그대를 따르기 때문에 이러는 것이 아니오.')
        stage.say('&101\n……。')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n이런 호기조차 잡지 못하다니\n원소의 그릇도 알 만하군.')
        stage.unit(101).setAction(2)
        stage.say('&101\n너, 뭐라고 했느냐!')
        stage.unit(101).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.delay(5)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n하하하하하, 너무 노여워하지 마시오.')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('&0\n병사들에게 쉬라고 명하라.')
        stage.unit(0).setAction(0, 2)
        stage.delay(5)
        stage.say('&0\n(천하에 이름을 떨치고 위엄을 세울\n절호의 기회를 놓치고 말았군.)')
        stage.unit(0).move(44, 98, 2)
        stage.unit(0).hide()
        stage.delay(5)
        stage.unit(101).setAction(0, 1)
        stage.say('&101\n쳇! 조조 그놈은 대체 무슨 생각이지?')
        stage.effectSound(203, 255)
        stage.clsUnit()
        stage.jumpScene(7)
    return False


def scene3():
    stage.bgSound(15)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            66],
        [
            1,
            39,
            52,
            1],
        [
            5,
            39,
            46,
            1],
        [
            10,
            39,
            40,
            1],
        [
            6,
            60,
            52,
            3],
        [
            12,
            60,
            46,
            3],
        [
            11,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('낙양  조조군 본진')
    stage.say('&0\n우리 군만으로 동탁을 추격하겠다.\n모두 반드시 이긴다는 믿음을 품어라.')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        1])
    gvars[4076] = 0
    gvars[4057] = 3
    stage.setBattlePos([
        [
            0,
            6],
        [
            1,
            5],
        [
            1,
            8],
        [
            1,
            6],
        [
            1,
            7],
        [
            0,
            5],
        [
            0,
            8],
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
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n원소, 저 겁쟁이 같으니.\n애초에 맹덕이 맹주를 맡았어야 했는데.\n&0\n원양, 그만 말하게.')
    if stage.unitClickTest(6):
        stage.say('&6\n싸움은 무턱대고 공격만 하는 것이 아니라\n시기를 잡는 것이 중요하다…….\n&0\n하후연, 또 병법을 익히고 있나? 참으로 부지런하군.\n&6\n아닙니다. 조금이라도 배워야지요.')
    if stage.unitClickTest(5):
        stage.say('&5\n흥! 동탁도 별것 아니군.\n&0\n이봐, 절대 방심하지 말게…….')
    if stage.unitClickTest(12):
        stage.say('&12\n동탁군은 백성을 끌고 가니 행군이 느릴 수밖에 없습니다…\n형양 부근에서 따라잡을 수도 있겠습니다.')
    if stage.unitClickTest(10):
        stage.say('&10\n이번에는 반드시 실력을 보여 주겠다.')
    if stage.unitClickTest(11):
        stage.say('&11\n동탁의 군사 이유는 계략에 능합니다.\n형양은 매복하기 좋은 곳이니, 어쩌면…….')
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
