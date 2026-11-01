# Source Generated with Decompyle++
# File: R_26.marshal (Python 3.9)


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
    stage.setEventName('조조의 남하')
    stage.delay(5)
    stage.showHead(1, 296, 127)
    stage.showHead(32, 232, 171)
    stage.delay(5)
    stage.bottomTxt('조조의 대군은 공명의 계책에 걸려 대패했고', False, False, False)
    stage.delay(8)
    stage.head(1).move(330, 99)
    stage.delay(5)
    stage.bottomTxt('하후돈은 패잔병을 수습하여 허도로 돌아갈 수밖에 없었다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    model.setFace(4)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            15,
            52,
            70],
        [
            2,
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
            2],
        [
            1,
            52,
            55,
            3,
            12],
        [
            14,
            58,
            52,
            3,
            11],
        [
            11,
            58,
            58,
            3,
            11]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n원양, 어쩌다 이 지경이 되었는가?\n&1\n호언장담하고 나섰다가 참패하고 돌아왔으니\n더는 승상을 뵐 면목이 없네.\n처분을 내려 주게.\n&0\n어찌 패한 것인가?\n공명의 화공에 당했다던데?')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n좁은 길에서는 화공을 경계해야 한다. 병법에 밝고\n백전을 겪은 자네가 그것마저 잊었는가?\n&1\n변명할 말이 없네. 죽음으로 죄를 갚겠네.')
    stage.unit(0).setAction(0, 1)
    stage.effectSound(201, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('승패는 병가지상사이니 이번에는 용서하겠다.\n패장은 마땅히 죽어야 한다. 절대 용서할 수 없다.', 0)
    if sel == 1:
        vars[310] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('&0\n원양, 돌아가 쉬게.\n문칙, 만성도 수고했네.\n물러가 쉬도록 하게.\n&1\n맹덕…… 이렇게 나를 용서하겠다는 건가?\n&0\n음, 이기고 지는 것은 병가의 상사일세.\n다음 싸움에서 반드시 공을 세워 죄를 씻도록 하게.\n&1\n나를 용서하면 군기를 어찌 바로 세우겠는가?\n차라리 내 목을 베게!!')
        stage.say('&0\n그대의 언행으로 이미 군기는 바로 섰네.\n그렇지 않은가, 문약?')
        stage.unit(20).setAction(6)
        stage.say('&20\n그렇습니다. 주군의 말씀대로입니다.')
        stage.unit(20).setAction(0)
        stage.say('&1\n………\n&0\n자네는 이미 군기를 세웠네.\n장수인 만큼 앞으로 전장에서 치욕을 씻도록 하게.\n&1\n하지만………')
        stage.unit(0).setAction(0, 3)
        stage.say('&0\n원양, 그대의 죽음을 헛되게 하지 않겠네.\n&1\n……예.')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n좋다! 너희 셋은 물러가 쉬어라.\n&1\n예.\n&14\n예.\n&11\n예.')
    elif sel == 2:
        vars[310] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('&0\n원양, 자네 말이 옳네.\n이번 참패는 참으로 용서할 수 없는 일이야.\n&1\n알겠네……')
        stage.unit(0).setAction(17)
        stage.say('&0\n좋다! 군법대로 처결하겠다.\n하후돈의 목을 베어라!!')
        stage.unit(15).setAction(3)
        stage.say('&15\n！！')
        stage.unit(2).setAction(6)
        stage.say('&2\n뭐라고!!')
        stage.unit(20).setAction(6)
        stage.say('&20\n주군, 잠시 기다리십시오.')
        stage.unit(15).setAction(0)
        stage.unit(2).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        stage.say('&0\n무슨 일인가, 문약?\n&20\n주군, 하후 장군이 큰 죄를 지은 것은 사실이나\n부디 죽음만은 면하게 해 주십시오.')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n안 된다! 그래서는 어찌 군기를 바로 세우겠는가?\n&20\n장군께서 죽음까지 각오하셨으니\n이미 군기는 바로 섰습니다.\n부디 주군께서는 목숨만은 살려 주십시오.')
        stage.unit(2).setAction(6)
        stage.say('&2\n저도 장군의 선처를 청합니다.\n우리 군에 하후 장군 같은 대장이 없어서야 되겠습니까!')
        stage.unit(13).setAction(6)
        stage.say('&13\n주군, 부디 하후 장군을 살려 주십시오.')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('&0\n음, 좋다.\n자네들이 이토록 간청하니\n이번 한 번은 용서하겠다.')
        stage.unit(0).setAction(17)
        stage.say('&0\n하지만 원양,\n다음 싸움에서는 반드시 오늘의 치욕을 씻어야 하네!\n&1\n……알겠네!')
        stage.unit(0).setAction(0)
        stage.say('&0\n그럼 물러가 쉬게.\n문칙과 만성, 자네들도 물러가게.\n&1\n예.\n&14\n예.\n&11\n예.')
    stage.unit(14).setAction(0)
    stage.unit(11).setAction(0)
    stage.unitsMove([
        [
            stage.unit(11),
            98,
            58,
            1],
        [
            stage.unit(14),
            98,
            52,
            1],
        [
            stage.unit(1),
            92,
            55,
            1]])
    stage.unit(14).hide()
    stage.unit(11).hide()
    stage.unit(1).move(98, 55, 1)
    stage.unit(1).hide()
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n으음……\n공명에게 이토록 뛰어난 재주가 있을 줄이야……')
    stage.unit(21).setAction(6)
    stage.say('&21\n공명의 지략은 참으로 두렵습니다.')
    stage.unit(21).setAction(0)
    stage.say('&0\n그렇다고 유비가 날뛰도록 내버려 둘 수는 없다.\n&0\n당장 놈을 없애 버리겠다…….')
    stage.unit(0).setAction(3)
    stage.say('&0\n좋다! 이 기회에 남방을 평정하자.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n전군에 전하라. 남방을 평정하러 진군할 준비를 하라고!')
    stage.unit(0).setAction(17)
    stage.say('&0\n형주의 유표와 유비,\n그리고 동오의 손권을 모두 제거하겠다!!')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 335, 84)
    stage.delay(5)
    stage.showHead(32, 252, 155)
    stage.delay(5)
    stage.bottomTxt('신야의 유비와', False, False, False)
    stage.delay(5)
    stage.showHead(150, 316, 195)
    stage.delay(5)
    stage.bottomTxt('형주의 유표,', True, False, False)
    stage.delay(5)
    stage.showHead(76, 575, 216)
    stage.delay(5)
    stage.bottomTxt('동오의 손권은 강남에 자리 잡고 있었다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('조조가 남방 평정을 위한 첫걸음으로 택한 것은', True, True, False)
    stage.delay(5)
    stage.head(0).move(316, 84)
    stage.delay(5)
    stage.bottomTxt('신야에 있는 유비를 공격하는 것이었다.', True, False, True)
    stage.bottomTxt('조조군의 기습을 받은 유비는 신야를 버리고 달아났다.', False, False, False)
    stage.delay(5)
    stage.head(32).hide()
    stage.delay(5)
    stage.bottomTxt('조조군은 거의 공격받지 않은 채', True, True, False)
    stage.delay(5)
    stage.head(0).move(316, 115)
    stage.delay(5)
    stage.bottomTxt('남하하여 유표가 있는 양양 근교에 이르렀다.', True, False, True)
    stage.bgSound(15)
    stage.loadBg(2, 53)
    model.setFace(128)
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
    stage.setStageName('양양 근교  조조군 본진')
    stage.say('&0\n너무 쉽게 이곳까지 왔군.\n유표는 무슨 생각을 하는 것인가……')
    stage.unit(24).setAction(0, 2)
    stage.say('&24\n이상합니다. 우리를 깊숙이 유인하려는 것이라면\n저항하는 시늉이라도 해야 할 텐데요.\n&0\n음.')
    stage.showUnit(181, 54, 22, 2)
    stage.say('&181\n보고드립니다.')
    stage.unit(24).setAction(0, 3)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 양양에서 항복 사신이 왔습니다.\n&0\n뭐라고!?\n항복 사신이라고!?\n……좋다, 데려오라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.unit(23).setAction(0, 2)
    stage.say('&23\n화살 한 발 쏘지 않고 항복한다고……?\n유표는 무슨 생각인지 모르겠군요.')
    stage.showUnit(27, 54, 22, 2)
    stage.say('&27\n안녕하십니까.')
    stage.unit(23).setAction(0, 3)
    stage.unit(27).move(54, 64, 2)
    stage.delay(3)
    stage.unit(27).setAction(11)
    stage.say('&27\n처음 뵙겠습니다, 승상.\n소장은 문빙, 자는 중업이라 합니다.\n&0\n음, 내가 조조다.\n항복을 청하러 온 사자겠지!\n&27\n그렇습니다.\n&0\n유표는 어찌 싸우기도 전에\n항복하기로 했느냐?\n&27\n……주군께서는 며칠 전 세상을 떠나셨습니다.\n&27\n뒤를 이은 유종께서 형주를 전란에서 지키고자\n영토를 승상께 바치기로 하셨습니다.\n&27\n부디 자비를 베푸시어\n저희의 청을 받아 주십시오.\n&0\n좋다! 받아들이겠다.\n&27\n감사합니다, 승상.')
    stage.say('&0\n문빙, 물을 것이 있네.\n신야에 있던 유비는 지금 어디 있는가?\n&27\n……유비는 항복에 반대하여\n지금 동오로 달아나고 있습니다.\n&0\n그런가?\n좋다! 유종은 계속 형주를 지키게 하라.\n&0\n우리 군은 유비 추격을 계속한다.\n&0\n하후돈, 장합!\n그대 둘을 선봉으로 삼겠다.\n속히 유비를 추격하라.')
    if vars[310]:
        stage.unit(1).setAction(6)
        stage.say('&1\n예.')
        stage.unit(7).setAction(6)
        stage.say('&7\n예.')
        stage.unit(1).setAction(0)
        stage.unit(7).setAction(0)
    if not vars[310]:
        stage.say('&0\n원양, 지난날의 치욕을 씻어야 하네!')
        stage.unit(1).setAction(3)
        stage.say('&1\n예!')
        stage.unit(1).setAction(0)
    stage.delay(5)
    stage.showUnit(28, 55, 22, 2)
    stage.unit(28).move(55, 26, 2)
    stage.showUnit(26, 55, 22, 2)
    stage.unitsMove([
        [
            stage.unit(28),
            50,
            56,
            2],
        [
            stage.unit(26),
            58,
            56,
            2]])
    stage.unit(26).setAction(0)
    stage.unit(28).setAction(0)
    stage.say('&26\n주군!')
    stage.unit(0).setAction(3)
    stage.say('&0\n오, 하후은과 하후걸인가?')
    stage.unit(0).setAction(0)
    stage.say('&28\n주군, 저희도 이번 싸움에 참여하게 해 주십시오!\n&26\n저희 둘은 아직 전장에 나선 적이 없으니\n기회를 주십시오.\n&0\n음, 좋은 말이다.\n좋다. 주력 부대를 지원하는 임무를 맡기겠다.\n두 사람 모두 힘써 공을 세워라.\n&28\n예.\n&26\n감사합니다, 주군.\n그럼 출발할 준비를 하겠습니다.')
    stage.unit(26).move(55, 56, 3)
    stage.unitsMove([
        [
            stage.unit(26),
            55,
            52,
            0],
        [
            stage.unit(28),
            55,
            56,
            2]])
    stage.unitsMove([
        [
            stage.unit(26),
            55,
            22,
            0],
        [
            stage.unit(28),
            55,
            26,
            0]])
    stage.unit(26).hide()
    stage.unit(28).move(55, 22, 0)
    stage.unit(28).hide()
    stage.say('&0\n문빙 장군, 우리 군은 형주의 지리에 어두우니\n선도 부대를 맡아 주게.\n&27\n이미 귀순한 이상\n제 주군은 승상이십니다.\n명을 받드는 것은 당연한 일입니다.\n&27\n즉시 준비하겠습니다. 이만 물러가겠습니다.\n&0\n음! 전장에서 보세.')
    stage.unit(27).setAction(0)
    stage.delay(3)
    stage.unit(27).move(54, 22, 0)
    stage.unit(27).hide()
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다, 출발하라!')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        1,
        7], [
        22])
    gvars[4076] = 0
    gvars[4057] = 26
    stage.setBattlePos([
        [
            2,
            2],
        [
            0,
            1],
        [
            1,
            0],
        [
            2,
            3],
        [
            3,
            2],
        [
            1,
            3],
        [
            3,
            1],
        [
            1,
            1],
        [
            0,
            2],
        [
            2,
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


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n공명!\n이번에는 반드시 원수를 갚겠다.')
    if stage.unitClickTest(7):
        stage.say('&0\n장 장군, 그대도 출전하게.\n&7\n예.')
    if stage.unitClickTest(15):
        stage.say('&15\n예전부터 유표군이 강하다는 말을 들었지요.\n한번 겨뤄 보고 싶습니다.\n&0\n나도 그럴 생각이다.')
    if stage.unitClickTest(23):
        stage.say('&0\n문화, 유종은 어떤 자인지 아는가?\n&23\n어린아이에 지나지 않습니다.\n그리 뛰어나지도 않은 듯합니다.\n&0\n흥, 젖내 나는 애송이였군.\n&23\n그렇습니다.')
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 전장은 어느 쪽인가?\n&20\n장판파 근처일 듯합니다.\n&0\n장판파라…… 그곳에는 강이 있었지.\n&20\n예, 중앙 부근에 흐르나\n그리 큰 강은 아닙니다.')
    if stage.unitClickTest(24):
        stage.say('&24\n주군, 방금 보고를 받았습니다.\n유비가 갓난아이도 데리고 있다 합니다.\n&0\n갓난아이라고?\n유비의 아이인가?\n&24\n예.\n아두라는 사내아이입니다.\n&0\n아두?\n참 이상한 이름이군.\n&24\n그 어미가 북두칠성을 삼키는 꿈을 꾸고\n아이를 가졌다 하여\n그리 이름 지었다 합니다.\n&0\n오?\n북두칠성을 삼켰다니…….')
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
