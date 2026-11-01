# Source Generated with Decompyle++
# File: R_23.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 28)
    stage.showUnits([
        [
            181,
            58,
            70,
            1],
        [
            8,
            58,
            16,
            2]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setEventName('목표는 원담')
    stage.setStageName('업성 원상 저택 앞')
    stage.unit(8).move(58, 54, 2)
    stage.delay(3)
    stage.unit(8).setAction(0, 3)
    stage.say('&8\n오, 여기가 업성의 공관인가?\n으리으리하군.\n&8\n음…… 안을 조사하고 있는 건가?')
    stage.unit(181).move(58, 64, 0)
    stage.say('&181\n멈춰라! 들어갈 수 없다!')
    stage.unit(8).setAction(0, 2)
    stage.say('&8\n뭐라고?\n&181\n조 승상의 명령입니다.\n누구도 들어갈 수 없습니다!\n&8\n나도 알아보지 못하느냐!\n나는 조조의 적자 조비다!\n&181\n그…….\n&8\n이름 없는 졸개가 감히 나를 막겠느냐?\n계속 막아서면 베어 버리겠다!\n&181\n저……저는……')
    stage.unit(8).move(43, 54, 3)
    stage.say('&181\n예…….')
    stage.effectSound(205, 255)
    stage.clsUnit()
    stage.loadBg(2, 28)
    stage.showUnit(181, 58, 70, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('그날 저녁', 3)
    stage.showUnits([
        [
            0,
            58,
            16,
            2],
        [
            22,
            58,
            6,
            2]])
    stage.unitsMove([
        [
            stage.unit(0),
            58,
            54,
            2],
        [
            stage.unit(22),
            58,
            44,
            2]])
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(0, 0)
    stage.say('&181\n주군이셨군요!')
    stage.say('&0\n음, 수고했다.')
    stage.delay(5)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n음?')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n뭐지? 누군가 들어간 듯한데!')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('&0\n누구도 들이지 말라고\n내가 분명히 말하지 않았느냐!!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n예! 소인도 잊지 않았습니다…….\n&181\n조비 세자께서 들어가셨습니다…….')
    stage.unit(0).setAction(2)
    stage.say('&0\n뭐라고!?')
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('조비를 벌한다\n조비를 용서한다', 0)
    if sel == 1:
        vars[308] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.unit(0).setAction(0)
        stage.say('&0\n이놈…… 배짱 한번 두둑하군!')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(2)
        stage.say('&0\n봉효, 당장 자환을 체포하게.\n군법에 따라 처벌하겠다!!')
        stage.unit(22).setAction(6)
        stage.say('&22\n잠시만요, 주군.\n&22\n업성은 함락된 지 얼마 안 되었으니\n누군가 안에 숨어 있을 수 있습니다.\n&22\n적자인 조비 세자가\n주군께서 저택에 드시기 전에 살핀 것이라면\n잘못이라 할 수 없습니다.')
        stage.unit(22).setAction(0)
        stage.delay(3)
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('&0\n음……\n&0\n그 말도……좋다.\n이 일은 더는 묻지 않겠다.\n방금 내린 명령은 거두겠다.')
        stage.unit(22).setAction(6)
        stage.say('&22\n예.')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n봉효……')
        stage.unit(22).setAction(0)
        stage.say('&22\n예?\n&0\n원소의 전철을 밟고 싶지는 않으니\n적자에게 특권을 조금 주도록 하겠네.\n&22\n그것은…………')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n하하하.\n봉효, 들어가세!')
        stage.unit(22).setAction(6)
        stage.say('&22\n예, 예.')
    elif sel == 2:
        vars[308] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('&0\n그만두어라.\n&181\n예?\n&0\n업성이 막 함락되었으니\n수상한 자들이 아직 숨어 있을 수 있다.\n&0\n적자인 자환이\n내가 들어가기 전에 저택을 살펴보는 것도\n지나친 일은 아니지.')
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n봉효.')
        stage.unit(22).setAction(6)
        stage.say('&22\n예, 저도 같은 생각입니다.')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n하하하.\n봉효, 들어가세!')
        stage.unit(22).setAction(6)
        stage.say('&22\n예, 예.')
    model.setFace(0)
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            0,
            53,
            28,
            2],
        [
            1,
            68,
            48,
            3],
        [
            15,
            68,
            54,
            3],
        [
            8,
            68,
            60,
            3],
        [
            13,
            68,
            66,
            3],
        [
            2,
            68,
            72,
            3],
        [
            20,
            38,
            48,
            1],
        [
            22,
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
            1],
        [
            23,
            38,
            72,
            1],
        [
            192,
            53,
            48]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('업성이 함락되었다는 소식을 들은 원담은 조조에게 사신을 보냈다.')
    stage.setStageName('업@임시 승상부')
    stage.say('&192\n저희 주군께서 승상의 승리를 축하하십니다.\n&0\n과분한 인사로군. 오히려 내가 송구하오.\n&0\n참, 사신.\n원담에게 함께 출병하자고 하지 않았는가?\n어찌 병사를 보내지 않았는가?\n&192\n예? 그…… 그것은…….')
    stage.unit(192).setAction(6)
    stage.say('&192\n요즘 영지의 치안이 좋지 않아……\n승상을 도와 싸울 인원을 보낼 수 없습니다……')
    stage.unit(192).setAction(0)
    stage.say('&0\n그렇겠지. 나도 그렇게 생각했네.')
    stage.unit(0).setAction(3)
    stage.say('&0\n그러니………')
    stage.unit(0).setAction(0)
    stage.say('&0\n원담에게 군사를 보내\n그를 대신해 무도한 자들을 없애고 싶군.')
    stage.unit(192).setAction(6)
    stage.say('&192\n예?\n아……아닙니다……승상께 수고를 끼칠 수는 없습니다.\n&0\n사양할 것 없소.\n저 무도한 자들의 악명은\n이 조조도 진작 들었소.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n원담과 곽도, 신평이다.')
    stage.unit(192).setAction(0)
    stage.say('&192\n무슨 말씀이십니까……\n승상, 농담이시겠지요……!?')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n그대와 농담을 나눌 여유는 없네……')
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n역적을 도운 네놈도 목을 베겠다!!')
    stage.unit(192).setAction(3)
    stage.say('&192\n아!?')
    stage.unit(192).setAction(0)
    stage.say('&0\n어리석은 놈!\n내가 원담 같은 멍청이의 꾀에 넘어갈 줄 알았느냐?\n&0\n나를 이용해 권력을 차지하겠다고?\n꿈도 꾸지 마라!\n&0\n똑똑히 들어라! 돌아가 원담에게 전해라.\n목을 씻고 죽을 날이나 기다리라고!!')
    stage.unit(0).setAction(0)
    stage.say('&0\n네 목을 베어\n원담에게 돌려보내 주기를 바라느냐?')
    stage.unit(192).setAction(6)
    stage.say('&192\n아!\n승상, 살려 주십시오!')
    stage.unit(0).setAction(2)
    stage.say('&0\n그렇다면 어서 돌아가 전하라!\n아니면 네 목을 베어 돌려보내 주마!\n이 조맹덕을 얕보지 말라고 전해라!!\n&192\n예……예!')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).move(53, 98, 2)
    stage.unit(192).hide()
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.unit(1).setAction(0, 0)
    stage.say('&1\n저자는 겁에 질려 울기 직전이군.\n&1\n원담이 함께 싸울 군사를 보내지 않으리라는 것은\n진작 예상했네.\n맹덕, 자네도 참 대단하군.\n&0\n저 사자가 느낀 두려움은\n원담에게도 옮아갈 것이네.\n이 또한 계책이지.\n&1\n그렇군, 그렇고말고.')
    stage.unit(1).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n하하하.\n원담의 얼굴이 볼만하겠군!\n좋다! 남피로 진군하라!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(17)
    stage.say('&1\n예!')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(4)
    stage.say('&22\n으윽, 콜록, 콜록………')
    stage.unit(0).setAction(3)
    stage.say('&0\n봉효!')
    stage.unit(24).setAction(0, 0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n봉효!')
    stage.unit(0).setAction(0)
    stage.say('&0\n무슨 일인가, 봉효?')
    stage.unit(22).setAction(0)
    stage.say('&22\n아, 별일 아닙니다……\n가벼운 감기이니 곧 나을 것입니다.\n모두 걱정하지 마십시오.\n&0\n………\n(지난 싸움 때부터 기침을 멈추지 않더니\n설마………)')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 308, 45)
    stage.showHead(115, 372, 45)
    stage.showHead(113, 460, 28)
    stage.delay(8)
    stage.head(115).move(390, 30)
    stage.delay(8)
    stage.head(115).hide()
    stage.delay(5)
    stage.bottomTxt('원상이 업성을 버리고 달아난 뒤', False, False, False)
    stage.delay(5)
    stage.head(0).move(340, 45)
    stage.delay(5)
    stage.bottomTxt('조조는 이곳을 점령하고,', True, True, False)
    stage.delay(8)
    stage.head(0).move(396, 45)
    stage.delay(5)
    stage.bottomTxt('계속 진군하여 원담이 있는 남피를 공격했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 42)
    stage.showUnits([
        [
            113,
            54,
            70],
        [
            103,
            35,
            56,
            1],
        [
            112,
            71,
            56,
            3],
        [
            192,
            53,
            56,
            2,
            6]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.setStageName('남피  원담의 저택')
    stage.delay(3)
    stage.unit(113).setAction(3)
    stage.say('&113\n뭐…… 뭐라고!?\n조조가 나를 토벌한다고!')
    stage.say('&192\n예……예……\n매우 노하신 듯했습니다……')
    stage.unit(113).setAction(0)
    stage.say('&113\n으으음………\n설마 우리의 계책이 들통난 것인가……\n&113\n곽도!!')
    stage.unit(103).setAction(6)
    stage.say('&103\n예.')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(2)
    stage.say('&113\n네가 내놓은 어리석은 계책 때문에\n오늘 이 꼴이 되고 말았다!!\n어떻게 책임질 셈이냐!')
    stage.unit(113).setAction(0)
    stage.say('&103\n하……하지만……')
    stage.unit(113).setAction(2)
    stage.say('&113\n닥쳐라! 변명은 듣기 싫다!\n당장 끌어내어 목을 베어라!')
    stage.unit(112).setAction(0, 2)
    stage.say('&112\n잠시만 기다려 주십시오.')
    stage.unit(113).setAction(0)
    stage.say('&113\n무슨 일인가, 신평?')
    stage.unit(112).setAction(6)
    stage.say('&112\n주군,\n이제는 조조를 무찌르는 것 외에\n다른 길이 없습니다.\n&112\n지금 곽도를 죽이면\n조조만 기쁘게 할 뿐입니다.')
    stage.unit(113).setAction(0, 1)
    stage.say('&113\n뭐라고……?')
    stage.unit(113).setAction(0, 0)
    stage.say('&113\n으음……그 말이 옳다.\n빌어먹을, 수비를 강화하라!\n조조를 맞을 준비를 하라!\n&103\n예.\n&112\n예.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            1,
            38,
            54,
            1],
        [
            15,
            38,
            48,
            1],
        [
            6,
            38,
            42,
            1],
        [
            20,
            60,
            54,
            3],
        [
            22,
            60,
            48,
            3],
        [
            24,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('남피 교외  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다! 단숨에 원담을 섬멸하라!')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 13, [
        22,
        8])
    gvars[4076] = 0
    gvars[4057] = 23
    stage.setBattlePos([
        [
            2,
            16],
        [
            3,
            17],
        [
            6,
            16],
        [
            3,
            13],
        [
            5,
            15],
        [
            4,
            14],
        [
            5,
            17],
        [
            2,
            14],
        [
            4,
            16],
        [
            3,
            15],
        [
            6,
            18],
        [
            1,
            15],
        [
            4,
            18],
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
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n원담, 그 애송이가 번번이 우리를 얕보는군.\n맹덕, 내가 선봉을 맡겠네.')
    if stage.unitClickTest(20):
        stage.say('&20\n남피성은 규모가 매우 큽니다.\n병력 손실을 각오하셔야 합니다.')
    if stage.unitClickTest(22):
        stage.say('&22\n지난 싸움을 치른 뒤\n저희는 조비 세자의 용맹이\n주군과 닮아 감탄했습니다.\n&0\n음, 그 아이도 아직 경험을 더 쌓아야 하네.')
    if stage.unitClickTest(6):
        stage.say('&6\n성을 공격할 때는\n저희 같은 원거리 공격 부대가 효과적입니다.')
    if stage.unitClickTest(15):
        stage.say('&15\n원담은 별로 머리가 좋지 않다더군요.\n&0\n그렇지.\n&15\n제가 콧대를 꺾어 주겠습니다.')
    if stage.unitClickTest(24):
        stage.say('&24\n원담의 진영에는 포차 등 대규모 부대가 배치되어\n수비가 매우 견고합니다.\n&0\n포차대라고……?\n조금은 까다롭겠군.')
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
