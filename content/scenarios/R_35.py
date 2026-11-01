# Source Generated with Decompyle++
# File: R_35.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
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
            9,
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
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('같은 하늘 아래')
    stage.setStageName('유수구  조조군 본진')
    if vars[734]:
        stage.unit(1).setAction(0, 2)
        stage.say('&1\n맹덕, 이번에는 어렵사리 위기를 벗어났지만\n우리 군의 피해가 예상보다 훨씬 크네.')
        stage.unit(1).setAction(3)
        stage.say('&1\n동오가 다시 군사를 일으켜 공격한다면\n우리는 막아 내기 어려울 걸세!')
        stage.unit(1).setAction(0)
        stage.say('&0\n걱정할 것 없네.')
        stage.unit(1).setAction(2)
        stage.say('&1\n뭐라고!?\n무슨 소리를 하는가?\n지금 우리 군의 꼴을 보게나…….')
        stage.say('&0\n원양, 이제 위와 오, 촉 모두 뒤를 걱정해야 할 처지일세.')
        stage.unit(1).setAction(0)
        stage.say('&1\n누구도 전력을 다해 승부를 걸 수 없다는 말인가?\n&0\n음. 지금은 세 나라가 서로 견제하고 있어\n누구도 함부로 움직일 수 없네.')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n촉이 있는 한\n손권도 우리와 끝없이 싸우지는 못할 것이다.')
        stage.unit(1).setAction(2)
        stage.say('&1\n그럼 이제 어찌할 텐가?')
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n기다리는 수밖에 없다.\n손권이 휴전을 청할 사신을 보낼 때까지.')
        stage.unit(1).setAction(0)
        stage.say('&1\n그가 사람을 보내겠는가……?')
        stage.showUnit(181, 54, 22, 2)
        stage.say('&181\n보고드립니다!')
        stage.delay(3)
        stage.unit(181).setAction(11)
        stage.say('&181\n주군, 동오의 사신이 왔습니다.')
        stage.unit(1).setAction(0, 1)
        stage.say('&1\n!!\n&0\n그런가? 예상보다 이르군.\n좋다, 들어오라 하라.\n&181\n예.')
        stage.unit(181).hide()
        stage.delay(5)
        stage.showUnit(193, 55, 22, 2)
        stage.say('&193\n실례했습니다.')
        stage.unit(193).move(54, 64, 2)
        stage.say('&0\n내가 한의 승상 조조다.\n동오의 사신인가?\n무슨 일로 왔느냐?')
        stage.unit(193).setAction(6)
        stage.say('&193\n예. 주군의 명으로 휴전을 청하러 왔습니다.\n&0\n오, 휴전이라고?\n&193\n예. 주군께서는 양군이 철수하고\n앞으로 우호를 깊이 하기를 바라십니다.')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n손권의 생각이야 편리하기 짝이 없군.\n애초에 싸움을 건 것은 그대들 아닌가?\n&193\n예.\n대인의 말씀이 지당하오나\n이번만은 부디……')
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n좋다!\n휴전 요청을 받아들이겠다.\n돌아가 손권에게 전하라.\n&193\n예, 감사합니다.\n그럼 이만 물러가겠습니다!')
        stage.unit(193).setAction(0)
        stage.delay(3)
        stage.unit(193).move(54, 22, 0)
        stage.unit(193).hide()
        stage.effectSound(208, 255)
        stage.clsUnit()
    if not vars[734]:
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('&19\n동오가 물러나기는 했지만\n우리 군의 피해 또한 막심합니다.')
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n더 싸우기에는 무리입니다.')
        stage.unit(20).setAction(0)
        stage.say('&0\n음, 어쩔 수 없지.\n우선 허도로 물러나자!\n&0\n문약, 자네는 사자가 되어 동오 진영으로 가게.\n손권과 휴전 문제를 논의하도록 하게.')
        stage.unit(20).setAction(6)
        stage.say('&20\n예, 즉시 출발하겠습니다.')
        stage.unit(20).setAction(0)
        stage.say('&0\n좋다. 휴전 협상이 타결되는 대로\n군사를 거두어 조정으로 돌아가겠다.')
        stage.effectSound(208, 255)
        stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 426, 161)
    stage.showHead(76, 490, 200)
    stage.bottomTxt('유수구의 결전 뒤,', False, False, False)
    stage.delay(5)
    stage.bottomTxt('조조는 손권과 화목하게 지내며', True, False, False)
    stage.delay(10)
    stage.head(0).move(337, 100)
    stage.delay(8)
    stage.head(76).move(507, 218)
    stage.delay(5)
    stage.bottomTxt('양군은 각자 군사를 거두어 본국으로 돌아갔다.', True, True, True)
    stage.delay(5)
    stage.bottomTxt('그리하여…….', False, False, False)
    stage.delay(5)
    stage.bottomTxt('위의 조조와 동오의 손권,', True, False, False)
    stage.delay(5)
    stage.showHead(32, 26, 172)
    stage.delay(5)
    stage.bottomTxt('그리고 촉의 유비, 이 세 나라가', True, False, False)
    stage.delay(5)
    stage.bottomTxt('각자 다스리는 영토를 굳게 다졌고,', True, True, True)
    stage.delay(10)
    stage.bottomTxt('역사에 없던 삼국 정립의 시대가 열렸다.', False, False, False)
    stage.delay(20)
    stage.bgSound(-1)
    return False


def scene1():
    stage.loadBg(2, 28)
    stage.showUnits([
        [
            19,
            68,
            42,
            1],
        [
            181,
            52,
            79,
            1],
        [
            182,
            54,
            46,
            1]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부 앞')
    stage.showUnit(0, 68, 94)
    stage.unit(0).move(68, 65, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n수고했네.')
    stage.unit(181).setAction(11)
    stage.unit(182).setAction(11)
    stage.say('&181\n예.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n음, 저기 있는 자는 중달인가?\n&182\n예. 군사께서 아까부터 하늘을 바라보고 계십니다.\n&181\n전에도 저렇게 꼼짝도 하지 않으셨지요……\n&0\n허허!\n여전히 한결같은 자로군.')
    stage.unit(0).move(68, 50, 0)
    stage.unit(0).setAction(0)
    stage.unit(181).setAction(0)
    stage.unit(182).setAction(0)
    stage.say('&0\n중달.')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n아, 주군이셨군요.\n&0\n또 천문을 살피며 천하의 형세를 점치고 있었는가?\n&19\n제 유일한 낙입니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n취미라?\n천하가 넓다 한들 난세의 향방을 점치는 일을\n취미로 삼는 자는 극히 드물겠군.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n하지만 그런 관심을 가볍게 여길 수는 없지.\n그대가 앞서 말했듯이\n천하가 이미 셋으로 갈라졌으니 말이야.\n&19\n그렇습니다.\n다만 이 구도도 머지않아 무너질 것입니다.\n&0\n삼국이 맞선 형세가 오래가지 않으리라는 말인가?\n&19\n그렇습니다.\n세 나라 가운데 한 나라가 반드시 천하를 통일할 것입니다.\n&0\n어느 나라인가?\n우리 위나라인가?\n&19\n그것까지는 아직 알 수 없습니다.\n다만…… 저는 위나라가 되리라 믿습니다.')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하.\n천상이 무엇을 예고하든 무슨 상관인가.\n패업을 이루는 데 힘쓰면 그만이지.')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 1)
    stage.say('&19\n주군, 유비의 촉과 손권의 오도\n우리와 같은 하늘 아래 있습니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n촉과 오라……?')
    stage.delay(10)
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.sectionName(4, '천하통일')
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            23,
            48,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setEventName('한중을 지켜라')
    stage.setStageName('허도  승상부')
    stage.say('&23\n주군, 저를 찾으셨습니까?\n&0\n오, 문화. 기다리고 있었네.\n상의할 것이 있네. 조비를 어떻게 보는가?\n&23\n예?\n갑자기 어찌 그런 말씀을 하십니까?\n&0\n내 후계자로 적합하다고 보는가?\n&23\n……어찌하여 제게 그런 질문을 하십니까?')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n순욱과 정욱은 내 아들들을 너무도 잘 알고 있네.\n게다가 각자 편애하는 까닭도 있지.\n&0\n사마의는 내게 온 지 얼마 되지 않았으니\n이 일은 자네에게 묻는 수밖에 없겠네.')
    stage.unit(23).setAction(0)
    stage.say('&23\n그럼 주군께서는 누가 적합하다고 보십니까?')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음………')
    stage.effectSound(205, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('조비가 적합하다\n조식이 적합하다', 0)
    if sel == 1:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.say('&0\n음,\n역시 적자인 조비가 뒤를 잇는 것이 옳겠군.')
        stage.unit(23).setAction(6)
        stage.say('&23\n오, 주군. 저도 찬성합니다.\n예부터 적자가 아버지의 뒤를 잇는 법입니다.\n&0\n음, 나도 그렇게 생각하네.\n다만 가신 중에는 조식을 지지하는 자도 있네.')
        stage.unit(23).setAction(0)
        stage.say('&23\n절대로 안 됩니다.\n원소의 아들들을 잊으셨습니까?\n&23\n원소가 셋째 아들을 후계자로 정한 탓에\n원씨 가문이 갈라져 멸망한 것입니다.')
        stage.unit(23).setAction(6)
        stage.say('&23\n주군, 원소의 전철을 밟아서는 안 됩니다.\n&0\n음, 그대 말이 옳다.\n좋다, 결정했다.\n내 대업은 조비에게 잇도록 하겠다.\n&23\n예.')
        vars[312] = 1
    elif sel == 2:
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.say('&0\n맏아들 조비는 재주는 상당하나\n사람들을 통솔할 기백이 부족하다.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n셋째 조식은 나라를 이끌 재능을 지녔으니\n그 아이에게 뒤를 맡기는 편이 좋을지도 모르겠군.')
        stage.unit(23).setAction(6)
        stage.say('&23\n주군, 절대로 안 됩니다.\n원소의 일을 잊으셨습니까?\n&23\n원소가 셋째 아들을 후계자로 정한 탓에\n원씨 가문이 갈라져 멸망한 것입니다.\n&23\n주군께서는 원소의 전철을 밟지 마십시오.')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n음, 그대 말이 옳다.\n좋다, 결정했다.\n내 대업은 조비에게 잇도록 하겠다.\n&23\n예.')
        vars[312] = 0
    stage.effectSound(205, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 333, 96)
    stage.bottomTxt('바로 그때…….', False, False, False)
    stage.delay(8)
    stage.showHead(32, 24, 166)
    stage.delay(5)
    stage.bottomTxt('유비는 국력을 쌓고 힘을 길렀으며,', True, True, False)
    stage.delay(5)
    stage.head(32).move(88, 90)
    stage.delay(5)
    stage.bottomTxt('마침내 한중을 공격할 군사를 일으켰다.', True, False, False)
    stage.delay(10)
    stage.showHead(12, 152, 75)
    stage.delay(5)
    stage.bottomTxt('한중을 지키던 조홍은', True, True, False)
    stage.delay(5)
    stage.bottomTxt('촉군의 기세가 대단한 것을 보고', True, False, False)
    stage.delay(5)
    stage.bottomTxt('허도의 조조에게 구원을 청했다.', True, False, True)
    stage.bgSound(-1)
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
            6,
            58,
            70],
        [
            15,
            64,
            70],
        [
            9,
            70,
            70],
        [
            13,
            76,
            70],
        [
            20,
            52,
            40,
            2],
        [
            19,
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
            2],
        [
            23,
            76,
            40,
            2],
        [
            181,
            48,
            55,
            3,
            11]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n뭐라고!\n촉이 대군을 일으켜 한중을 공격한다고!?')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&181\n예. 조홍 장군께서 즉시 원군을 보내 달라 하셨습니다.')
    stage.unit(19).setAction(6)
    stage.say('&19\n공명이 마침내 움직였습니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n한중 수비군은 며칠 버티지 못합니다.\n즉시 지원해야 합니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다! 즉시 대군을 이끌고 한중을 지원한다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n허나 주군, 대군이 함께 움직이면\n구원이 늦을까 염려됩니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n음, 발 빠른 선봉대를 보내야겠군.\n묘재!')
    stage.unit(6).setAction(0, 3)
    stage.say('&6\n예.\n&0\n그대가 선봉을 지휘하여\n즉시 한중을 지원하러 가게.\n나는 대군을 이끌고 뒤따라가겠다.')
    stage.unit(6).setAction(6)
    stage.say('&6\n오, 맹덕. 걱정 마시게.')
    stage.unit(6).setAction(0)
    stage.say('&0\n하후연, 자네만 믿겠네.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            6,
            54,
            80],
        [
            7,
            44,
            64,
            1]])
    stage.draw()
    stage.setStageName('한중  조조군 본진')
    stage.say('&6\n주군의 주력이 곧 도착한다.\n어떻게든 이곳을 사수해야 한다.')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        6,
        7,
        12], [
        0])
    gvars[4076] = 0
    gvars[4057] = 35
    stage.setBattlePos([
        [
            23,
            16],
        [
            22,
            17],
        [
            6,
            4],
        [
            4,
            6],
        [
            12,
            16],
        [
            19,
            26],
        [
            21,
            16],
        [
            23,
            18],
        [
            12,
            15],
        [
            19,
            25],
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


def scene5():
    if stage.unitClickTest(7):
        stage.say('&7\n전장 전체가 산지여서\n지형이 매우 복잡하니\n우선 병력을 움직이지 말자.')
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&6\n출발하라!')
        stage.bgSound(-1)
        return True

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
