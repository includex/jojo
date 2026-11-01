# Source Generated with Decompyle++
# File: R_02.marshal (Python 3.9)


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
            55,
            1],
        [
            132,
            50,
            40,
            2],
        [
            119,
            58,
            40,
            2],
        [
            192,
            50,
            70],
        [
            193,
            58,
            70],
        [
            181,
            50,
            55,
            3,
            11]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('호로관의 결전')
    stage.setStageName('낙양  승상부')
    stage.unit(127).setAction(3)
    stage.say('&127\n뭐라고!! 화웅이 죽었다고!?')
    stage.unit(127).setAction(0)
    stage.say('&181\n그렇습니다. 연합군이 지금 낙양으로 진군하기 시작했습니다.')
    stage.say('&127\n흥…….')
    stage.unit(132).setAction(6)
    stage.say('&132\n그렇다면 지금으로서는………\n호로관에서 적을 격파하는 수밖에 없습니다.')
    stage.unit(132).setAction(0)
    stage.say('&127\n음…….')
    stage.unit(127).setAction(17)
    stage.say('&127\n여포, 네가 나설 때다.\n호로관을 반드시 사수하라!')
    stage.unit(127).setAction(0)
    stage.delay(5)
    stage.say('&119\n예!')
    stage.unit(127).setAction(3)
    stage.say('&127\n나도 네 무용을 보러 가겠다.')
    stage.unit(127).setAction(0)
    stage.say('&119\n염려 마십시오.')
    stage.unit(119).setAction(17)
    stage.say('&119\n조조, 원소. 너희 솜씨를 어디 한번 보자!')
    stage.unit(119).setAction(0)
    stage.delay(3)
    stage.unit(119).move(61, 40, 1)
    stage.unit(119).move(61, 55, 2)
    stage.unit(119).move(91, 55, 1)
    stage.unit(119).hide()
    stage.delay(5)
    stage.say('&127\n이 동탁의 휘하에는 용맹한 장수들이 가득하다.')
    stage.unit(127).setAction(17)
    stage.say('&127\n이유! 출발을 준비하라!')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(132).setAction(6)
    stage.say('&132\n예!')
    stage.unit(132).setAction(0)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            101,
            54,
            37,
            2],
        [
            0,
            48,
            45,
            2],
        [
            145,
            42,
            52,
            1],
        [
            136,
            42,
            60,
            1],
        [
            161,
            67,
            52,
            3],
        [
            32,
            67,
            60,
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('낙양 교외  연합군 본진')
    stage.unit(101).setAction(3)
    stage.say('&101\n모두 훌륭히 싸웠소.\n이 기세를 몰아 단숨에 낙양을 되찾읍시다.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(136).setAction(3)
    stage.say('&136\n이제 동탁도 우리 위력을 알았겠지.\n히히.')
    stage.unit(136).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n그렇게 낙관하기에는 아직 이르지 않겠는가.')
    stage.unit(0).setAction(0)
    stage.say('&136\n음? 어째서인가?')
    stage.unit(0).setAction(6)
    stage.say('&0\n동탁이 아직 여포를 내보내지 않았군.\n여포가 있는 한 우리는…….')
    stage.unit(0).setAction(0)
    stage.say('&101\n……여포? 그렇지…….\n그의 무예는 화웅보다 백 배나 뛰어나다.')
    stage.unit(136).setAction(3)
    stage.say('&136\n여포 따위는 시골의 필부에 불과하네.\n그자가 그토록 두려운가?')
    stage.unit(136).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n그렇다면 세력이 막강하신 원술 대인께서\n여포를 상대하셔도 문제없겠군요.')
    stage.unit(0).setAction(0)
    stage.say('&136\n흥!')
    stage.delay(5)
    stage.unit(136).setAction(2)
    stage.say('&136\n촌놈은 역시 촌놈에게 맡겨야겠군.')
    stage.unit(136).setAction(0)
    stage.delay(3)
    stage.unit(136).setAction(0, 2)
    stage.say('&136\n그렇지, 손견에게 시키면…….')
    stage.unit(136).setAction(3)
    stage.say('&136\n히히.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n……。')
    stage.unit(32).setAction(6)
    if vars[701]:
        stage.say('&32\n그러고 보니 손 장군은 무사히 빠져나가셨을까?')
        stage.unit(32).setAction(0)
        stage.delay(3)
        stage.unit(136).setAction(0, 1)
        stage.say('&136\n그럴 리 없지.\n이만큼 시간이 지났으니 분명 죽었을 것이오.')
    if not vars[701]:
        stage.say('&32\n손 장군이 좀처럼 오지 않으니\n혹시 무슨 일이 생긴 것은 아닐까?')
        stage.unit(32).setAction(0)
        stage.delay(3)
        stage.unit(136).setAction(0, 1)
        stage.say('&136\n아마 싸움에 패했겠지.\n그놈도 이제 목숨이 없을 게야!')
    stage.unit(136).setAction(3, 1)
    stage.say('&136\n히히히.')
    stage.unit(136).setAction(0, 1)
    stage.say('&99\n나는 아직 살아 있소.')
    stage.unit(136).setAction(3, 1)
    stage.say('&136\n아!')
    stage.unit(136).setAction(0, 2)
    stage.unit(0).setAction(0, 2)
    stage.unit(145).setAction(0, 2)
    stage.unit(143).setAction(0, 2)
    stage.unit(161).setAction(0, 2)
    stage.unit(32).setAction(0, 2)
    stage.showUnit(99, 54, 94)
    stage.unit(99).move(54, 60, 0)
    stage.unit(32).setAction(0, 3)
    stage.unit(136).setAction(0, 1)
    stage.unit(145).setAction(0, 1)
    stage.unit(143).setAction(0, 3)
    stage.unit(161).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n오, 손 장군!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(3)
    stage.say('&32\n무사한가!')
    stage.unit(32).setAction(0)
    stage.delay(5)
    stage.unit(99).setAction(0, 2)
    stage.say('&99\n……조 공!')
    stage.unit(0).setAction(6)
    stage.say('&0\n왜 그러는가?')
    stage.unit(99).setAction(0, 0)
    stage.say('&99\n군량 보급은 누가 맡았소?\n설마 그대와 원소 대인이오?')
    stage.say('&136\n큰일이군.')
    stage.unit(0).setAction(0)
    stage.say('&0\n그…군량은 모두 공로가 맡고 있습니다.')
    stage.unit(99).setAction(0, 2)
    stage.say('&99\n역시 그랬군…….')
    stage.unit(99).move(50, 60, 3)
    stage.delay(3)
    stage.unit(99).setAction(2)
    stage.say('&99\n원술! 역시 네놈이었구나!\n&99\n지난 군의에서 나를 시기하더니\n해칠 마음을 품은 것이었군!')
    stage.unit(145).setAction(0, 2)
    stage.say('&99\n나도 연합군의 일원인데\n감히 군량 보급을 끊다니!\n못된 놈!')
    stage.unit(99).setAction(2)
    stage.say('&99\n오늘은 반드시 네놈을 죽이겠다!')
    stage.unit(136).move(36, 60, 1)
    stage.unit(136).setAction(3)
    stage.say('&136\n아, 잠깐! 손을 거두시오. 오해요!')
    stage.unit(136).setAction(0)
    stage.unit(99).move(44, 60, 3)
    stage.delay(3)
    stage.unit(99).setAction(2)
    stage.say('&99\n변명은 듣지 않겠다. 네놈 때문에\n충성스럽고 용감한 부하들을 수없이 잃었다.\n&99\n네 피로 그들의 넋을 달래겠다.')
    stage.unit(136).setAction(9)
    stage.say('&136\n아, 모두 내 부하가 멋대로 한 일이오.')
    stage.unit(99).setAction(0)
    stage.say('&136\n내 곧 그놈을 그대 진영으로 압송하겠소!\n마음대로 처분하셔도 좋으니 노여워하지 마시오!')
    stage.unit(99).setAction(2)
    stage.say('&99\n부하에게 책임을 떠넘기다니!\n그러고도 한 군의 장수라 할 수 있느냐!?\n목숨을 내놓아라!!')
    stage.unit(32).setAction(6)
    stage.say('&32\n손 장군, 심정은 이해하오.\n허나 동탁의 대군과 결전을 앞두고 있으니\n우선 참아 주시오.')
    stage.unit(99).setAction(2, 1)
    stage.say('&99\n타이르지도, 막지도 마라!')
    stage.unit(32).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(6)
    stage.say('&0\n손 장군, 그렇게 하시면……\n죽은 부하들도 슬퍼할 것입니다.')
    stage.unit(99).setAction(2, 0)
    stage.say('&99\n무슨 뜻이오?')
    stage.unit(0).setAction(0)
    stage.say('&0\n지금 원술을 죽이면 동맹군이 무너집니다.\n그럼 부하들의 죽음도 헛되이 되고 맙니다.')
    stage.unit(0).setAction(6)
    stage.say('&0\n정녕 그렇게 하겠다는 것이냐!?')
    stage.delay(5)
    stage.unit(99).setAction(0)
    stage.say('&99\n……음!')
    stage.delay(10)
    stage.unit(99).setAction(0, 2)
    stage.say('&99\n……좋소. 일단 참아 두겠소.')
    stage.unit(0).setAction(0)
    stage.delay(8)
    stage.unit(99).setAction(0, 3)
    stage.say('&99\n원술, 동탁을 없앤 뒤\n네 목도 거두러 오겠다.')
    stage.unit(136).setAction(1)
    stage.say('&136\n후우!')
    stage.say('&0\n…….(부하를 위해 저토록 격분하다니,\n손견은 참으로 사내대장부로군…….)')
    stage.unit(101).setAction(3)
    stage.say('&101\n됐으니 모두 자리에 앉아라.\n큰 싸움이 코앞이다.')
    stage.unit(101).setAction(0)
    stage.delay(5)
    stage.unit(99).setAction(0, 0)
    stage.say('&99\n……좋소.')
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(99),
            42,
            68,
            1],
        [
            stage.unit(136),
            42,
            60,
            1]])
    stage.unit(145).setAction(0, 1)
    stage.delay(3)
    stage.unit(101).setAction(3)
    stage.say('&101\n조조, 앞으로의 전황을 설명해 보아라.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n예.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n……먼저 동탁군의 동향을 설명하겠소.\n&0\n낙양의 관문은 두 곳이오.\n사수관은 이미 우리가 함락했으니,\n동탁은 필시 호로관에 모든 병력을 집결시킬 것이오!')
    stage.unit(0).setAction(17)
    stage.say('&0\n호로관이 바로 동탁과 결판을 낼 곳이다.')
    stage.unit(0).setAction(0)
    stage.say('&101\n좋다. 적의 기세가 이리 거세니……')
    stage.unit(101).setAction(3)
    stage.say('&101\n도 대인, 이번에는 그대도 출전하시오.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(145).setAction(6)
    stage.say('&145\n알겠소.')
    stage.unit(145).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('&101\n좋다. 모두 각자 진영으로 돌아가 출발을 준비하라.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
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
    stage.delay(3)
    stage.setStageName('사수관  조조군 본진')
    stage.say('&0\n동탁은 틀림없이 호로관에 대군을 집결시켰을 것이며\n여포도 출전할 것이다. 참혹한 싸움이 될 터이니\n모두 만전을 기하도록 하라.')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        1])
    gvars[4076] = 0
    gvars[4057] = 2
    stage.setBattlePos([
        [
            17,
            9],
        [
            16,
            8],
        [
            16,
            11],
        [
            16,
            9],
        [
            16,
            10],
        [
            17,
            8],
        [
            17,
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


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n원술 그놈은 정말 못된 작자로군!')
    if stage.unitClickTest(6):
        stage.say('&6\n기병은 보병에 강하지만 궁병에 약하고\n보병은 궁병에 강하지만 기병에 약하다…….\n&0\n하후연, 병법을 익히는 중인가?\n&6\n예, 외우기 어렵군요……어디 보자…\n궁병은 기병에 강하지만\n보병에 약하다고 했지요…….')
    if stage.unitClickTest(5):
        stage.say('&5\n동탁 따위는 대단할 것 없습니다.\n&0\n이봐, 자효. 방심해서는 안 되네.')
    if stage.unitClickTest(12):
        stage.say('&12\n여포와 싸울 참이니\n도구점에서 「회복용 콩」을 사 두는 게 좋겠군.')
    if stage.unitClickTest(10):
        stage.say('&10\n여포는 서량기병이었던 것으로 기억합니다.\n&10\n공격과 방어가 모두 뛰어난 데다\n대각선 공격까지 할 수 있지요…\n후우, 참으로 강한 적입니다.')
    if stage.unitClickTest(11):
        stage.say('&11\n여포의 애마는 「적토마」라는데\n적 부대와 인접해 있어도 자유롭게 이동할 수 있다더군요.\n&11\n또 그가 쓰는 「방천화극」은\n적 부대 하나를 퇴각시키면 다른 적을 다시 공격할 수 있답니다.\n&1\n이봐, 그건 너무하지 않나!\n혼자 반칙을 하는 셈이잖아!')
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
