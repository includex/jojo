# Source Generated with Decompyle++
# File: R_43.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.setMenuVisible(False)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 127, 213)
    stage.bottomTxt('조조', False, False, False)
    stage.delay(5)
    stage.bottomTxt('백제성에서 촉군의 잔당을 크게 무찔렀다.', True, False, False)
    stage.delay(8)
    stage.showHead(35, 191, 213)
    stage.delay(5)
    stage.bottomTxt('제갈량', True, True, False)
    stage.delay(5)
    stage.head(35).move(277, 227)
    stage.head(35).hide()
    stage.delay(5)
    stage.bottomTxt('무리를 이끌고 동오로 달아났다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 23)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('다시 만난 적벽')
    stage.setStageName('백제성 교외  산길')
    stage.showUnit(35, 5, 52, 1)
    stage.unit(35).move(50, 52, 1)
    stage.unit(35).setAction(0)
    stage.say('&35\n마침내 백제성에서 위군을 막았군.\n&35\n지금 동오군은 적벽을 지키고 있을 테니\n어서 가서 합류하자.')
    stage.unit(35).setAction(0, 3)
    stage.say('&35\n대의를 위해 목숨을 바친 촉의 장병들이여.\n이 공명은 그대들의 죽음을 헛되게 하지 않겠다.\n&35\n반드시 그 원한을 갚으리라…….')
    stage.delay(5)
    stage.unit(35).move(95, 52, 1)
    stage.unit(35).hide()
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    model.initLocalVar()
    stage.clsUnit()
    stage.bgSound(0)
    stage.delay(3)
    stage.setMenuVisible(False)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
        [
            4,
            44,
            52,
            1],
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
            3]])
    stage.draw()
    stage.setStageName('백제성  조조군 본진')
    stage.say('&0\n공명, 그 애송이를 또 놓쳤군.')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n마치 싸우기 전부터 정해 둔 것처럼\n패하자마자 곧장 달아났습니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n음, 저도 같은 생각입니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n그렇습니다.\n우리를 이기는 것이 아니라\n우리의 발을 묶는 것이 목적입니다…….')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n설마 동오인가!?\n틀림없이 미리 동오가 움직이도록 손을 썼겠군.')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n그렇다면 공명은 시간을 벌어\n동오가 방비를 강화하도록 하려는 것입니까?')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.say('&0\n음, 틀림없네.')
    stage.unit(0).setAction(17)
    stage.say('&0\n출전하라! 적에게 더는 시간을 줄 수 없다!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n하지만 맹덕, 출전한다니\n우리는 어디를 공격해야 하겠나?\n&0\n동오가 우리를 맞아 싸울 만한 곳은\n그곳 말고는 없네.')
    stage.unit(1).setAction(3)
    stage.say('&1\n어디에 있는가?')
    stage.delay(5)
    stage.unit(20).setAction(0, 3)
    stage.say('&20\n……적벽.')
    stage.delay(3)
    stage.unit(1).setAction(0, 1)
    stage.unit(19).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 43
    stage.setBattlePos([
        [
            9,
            9],
        [
            10,
            8],
        [
            9,
            10],
        [
            10,
            9],
        [
            11,
            8],
        [
            8,
            10],
        [
            8,
            9],
        [
            9,
            8],
        [
            10,
            7],
        [
            7,
            9],
        [
            8,
            8],
        [
            9,
            7],
        [
            7,
            8],
        [
            8,
            7],
        [
            9,
            6]])
    return False


def scene2():
    if stage.battleTest():
        stage.say('&0\n출진하라!')
        stage.clsUnit()
        stage.setMenuVisible(False)
        model.setFace(128)
        stage.loadBg(1, 0)
        stage.draw()
        stage.showHead(0, 157, 212)
        stage.bottomTxt('조조군', False, False, False)
        stage.delay(5)
        stage.head(0).move(280, 227)
        stage.delay(5)
        stage.bottomTxt('인연 깊은 적벽으로 향했다.', True, False, True)
        stage.loadBg(2, 57)
        stage.showUnits([
            [
                76,
                66,
                52,
                3],
            [
                88,
                52,
                41,
                2],
            [
                78,
                46,
                41,
                2],
            [
                79,
                40,
                41,
                2],
            [
                81,
                52,
                63],
            [
                86,
                46,
                63],
            [
                85,
                40,
                63],
            [
                35,
                52,
                52,
                1]])
        stage.draw()
        stage.setStageName('적벽  손권군 본진')
        stage.say('&76\n공명 선생, 그대의 지시대로\n적벽에 대군을 배치했네.\n그런데 이것으로 조조를 이길 수 있겠는가?')
        stage.unit(35).setAction(6)
        stage.say('&35\n육손 장군께서 도와주신다면\n반드시 이길 수 있습니다.')
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(81).setAction(6)
        stage.say('&81\n만반의 준비를 마쳤습니다.\n안심하십시오.')
        stage.unit(81).setAction(0)
        stage.delay(3)
        stage.unit(78).setAction(6)
        stage.say('&78\n하지만……우리가 실패한다면요?')
        stage.unit(78).setAction(0)
        stage.delay(3)
        stage.unit(88).setAction(6)
        stage.say('&88\n그렇지 않은가.\n위군에게 여러 번 패하지 않았소?')
        stage.say('&88\n그 무모한 싸움들 때문에\n촉의 멸망이 앞당겨졌거늘\n더는 큰소리치지 마시오.')
        stage.unit(35).setAction(0, 0)
        stage.say('&35\n그대가 장소 대감이시지요?\n그럼 이런 상황에서\n어찌하실 생각입니까?')
        stage.unit(88).setAction(0)
        stage.say('&88\n당연한 것 아니오!\n위나라와 화친하여\n동오의 안녕을 지켜야지요.')
        stage.unit(35).setAction(11)
        stage.say('&35\n하하하.')
        stage.unit(88).setAction(6)
        stage.say('&88\n무엇이 우스운가!')
        stage.unit(88).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0)
        stage.say('&35\n장소 대인, 그대는 결국 문관일 뿐이라\n지금 형세를 전혀 꿰뚫지 못하시는군요.')
        stage.unit(88).setAction(6)
        stage.say('&88\n뭐라고……너무 방자하구나!')
        stage.unit(88).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(6)
        stage.say('&35\n묻고 싶습니다.\n지금 위나라와 화친하면\n동오가 정녕 평안할 수 있겠습니까?')
        stage.unit(35).setAction(0)
        stage.say('&35\n이미 삼국 정립의 형세가 무너진 마당에\n화친은 동오를 조조에게 바치는 매국 행위입니다.')
        stage.say('&35\n정말로 화친한다면\n그대는 신하로서 제 몸을 보전할 수 있겠지만\n손권 대인께서는 어찌 되겠습니까?')
        stage.say('&35\n틀림없이 사형에 처해질 것입니다.\n그런데도 조위와의 화친을 주장하시니\n충의의 마음이라고는 없으시군요.')
        stage.unit(88).setAction(6)
        stage.say('&88\n흥……')
        stage.say('&35\n오는 땅이 비옥하고 풍요로우며\n손씨가 삼 대에 걸쳐 다스려\n국력이 매우 튼튼합니다.\n&35\n게다가 지키기 쉽고 공격하기 어려운 장강의 천험까지 있지요.')
        stage.unit(35).setAction(6)
        stage.say('&35\n이런 나라를 한 번 싸워 보지도 않고\n그대로 조조에게 넘기시겠습니까?')
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0, 1)
        stage.say('&35\n촉의 산간 지역은 농업이 뒤처져 있었고\n나라를 세운 지도 얼마 되지 않았으니\n아직 신흥 국가입니다.')
        stage.unit(35).setAction(0, 0)
        stage.say('&35\n그런데도 후주 유선과 장병들은\n강대한 위를 상대로 끝까지 맞섰습니다.')
        stage.unit(35).setAction(2)
        stage.say('&35\n그러니 너는 후주 유선과\n촉 장병들의 분전을 논할 자격이 없다…….')
        stage.unit(88).setAction(0)
        stage.say('&88\n으윽!')
        stage.unit(76).setAction(3)
        stage.say('&76\n공명 대인, 알겠소.\n끝까지 싸우는 수밖에 없겠군.')
        stage.unit(76).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0, 1)
        stage.say('&35\n그렇습니다.')
        stage.unit(35).setAction(6)
        stage.say('&35\n그럼 전투 준비를 하러 가겠소.\n이만 물러가겠소.\n&76\n음.')
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(35).move(40, 52, 3)
        stage.delay(3)
        stage.say('&79\n공명…….')
        stage.unit(35).setAction(0, 0)
        stage.delay(3)
        stage.unit(35).setAction(6)
        stage.say('&35\n형님, 부디 몸조심하십시오.')
        stage.say('&79\n그대도 몸조심하게.')
        stage.delay(5)
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(35).move(16, 52, 3)
        stage.unit(35).hide()
        stage.say('&76\n공명이라…….\n육손, 그를 믿어도 되겠는가?')
        stage.unit(81).setAction(0, 1)
        stage.delay(3)
        stage.unit(81).setAction(6)
        stage.say('&81\n무어라 해야 좋을까요?')
        stage.unit(81).setAction(0)
        stage.say('&81\n주군, 지금 조조가 가장 두려워하는 이는 공명입니다.\n동오를 구할 사람도 공명뿐입니다.\n&76\n음…….')
        stage.bgSound(-1)
        stage.clsUnit()
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n이번 싸움은 전과 달리\n동오 쪽 강기슭이 주 전장이 됩니다.\n&20\n동오와 공명은 이미 강기슭에 진을 쳤을 것입니다.')
    if stage.unitClickTest(15):
        stage.say('&15\n백제성에서 백호보옥을 찾았잖습니까?\n&15\n청룡보옥이나 주작보옥과는 좀 다르지요?\n&21\n그렇습니다.\n이 보옥은 앞의 두 보옥과 달리\n풍수사 계열 부대가 사용합니다.\n&15\n오, 그럼 백호는\n회복 책략 중에서 가장 강력하단 말입니까?\n&19\n그렇습니다. 구체적으로는\n모든 부대의 체력을 회복시키고\n이상 상태도 치료합니다.\n&15\n이거 정말 대단한 물건이군요!')
    if stage.unitClickTest(1):
        stage.say('&1\n다시 적벽에서 싸우게 되었군.\n그때는 잠시 동오에게 당했지만\n이번에는 반드시 설욕하겠네.')
    if stage.unitClickTest(4):
        stage.say('&4\n적벽대전 때는 아직 군에 들지 않았지만\n여러분께 자주 들었습니다.\n&4\n우리 군과 인연이 깊은 곳이지요.')
    if stage.unitClickTest(19):
        stage.say('&19\n공명, 수전에서는 무슨 재주를 부릴지 보자.')
    if stage.unitClickTest(21):
        stage.say('&21\n어쨌든 뭍에 오르면 승산이 있습니다.\n이번에는 동오 수군이 어떤 전술을 쓸지 모르겠군요.')
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
