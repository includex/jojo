# Source Generated with Decompyle++
# File: R_21.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.bgSound(-1)
    stage.loadBg(2, 67)
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('원소의 아들은')
    stage.setStageName('양산  산길')
    stage.showUnits([
        [
            32,
            57,
            5,
            2],
        [
            33,
            47,
            5,
            2],
        [
            3,
            52,
            5,
            2]])
    stage.unitsMove([
        [
            stage.unit(32),
            57,
            17,
            2],
        [
            stage.unit(33),
            47,
            17,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(32),
            57,
            47,
            2],
        [
            stage.unit(33),
            47,
            47,
            2],
        [
            stage.unit(3),
            52,
            35,
            2]])
    stage.say('&32\n서둘러라. 조조군이 쫓아온다.')
    stage.unit(33).setAction(2)
    stage.say('&33\n흥, 간적 조조 놈!')
    stage.unit(33).setAction(0)
    stage.say('&3\n형님, 이제 어찌하시겠습니까?')
    stage.unit(33).setAction(0, 0)
    stage.delay(3)
    stage.unit(32).setAction(0, 0)
    stage.say('&32\n어쩔 수 없구나. 형주의 유표에게 의탁하자.\n&3\n형주 말씀이십니까……?\n&32\n음. 형주는 땅이 비옥하고\n유표는 나와 같은 유씨이니\n반드시 도와줄 것이다.')
    stage.unit(3).setAction(6)
    stage.say('&3\n알겠습니다.\n형님과 셋째는 먼저 형주로 가십시오.')
    stage.unit(3).setAction(0)
    stage.say('&3\n내가 조조를 막겠다.')
    stage.unit(33).setAction(3)
    stage.say('&33\n둘째 형님, 나도 돕겠소!\n&3\n셋째야, 형님을 지켜라!')
    stage.unit(33).setAction(0)
    stage.say('&33\n하지만………\n&32\n운장, 혼자인가……?')
    stage.unit(3).setAction(6)
    stage.say('&3\n형님, 안심하십시오.')
    stage.say('&32\n……좋다! 자네에게 맡기겠다.\n각별히 조심하게.\n&3\n예.')
    stage.unit(3).setAction(0)
    stage.say('&3\n어서 가자!')
    stage.unit(32).setAction(0, 3)
    stage.say('&32\n음. 가자, 셋째야!')
    stage.unit(33).setAction(0, 1)
    stage.delay(3)
    stage.unit(33).setAction(17)
    stage.say('&33\n오!')
    stage.unit(33).setAction(0)
    stage.unitsMove([
        [
            stage.unit(32),
            57,
            98,
            2],
        [
            stage.unit(33),
            47,
            95,
            2],
        [
            stage.unit(3),
            52,
            80,
            2]])
    stage.unit(3).setAction(0)
    stage.unit(32).hide()
    stage.unit(33).hide()
    stage.effectSound(203, 255)
    stage.effectSound(207, 0)
    stage.delay(3)
    stage.showUnits([
        [
            0,
            56,
            5,
            2],
        [
            22,
            48,
            5,
            2],
        [
            181,
            57,
            5,
            2],
        [
            182,
            52,
            5,
            2],
        [
            183,
            47,
            5,
            2]])
    stage.say('&0\n서둘러라! 유비를 놓치지 마라!')
    stage.unit(3).setAction(0, 0)
    stage.say('&3\n……왔는가?')
    stage.unitsMove([
        [
            stage.unit(0),
            56,
            13,
            2],
        [
            stage.unit(22),
            48,
            13,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(0),
            56,
            66,
            2],
        [
            stage.unit(22),
            48,
            66,
            2],
        [
            stage.unit(181),
            57,
            58,
            2],
        [
            stage.unit(182),
            52,
            58,
            2],
        [
            stage.unit(183),
            47,
            58,
            2]])
    stage.unit(0).setAction(0)
    stage.unit(22).setAction(0)
    stage.unit(181).setAction(0)
    stage.unit(182).setAction(0)
    stage.unit(183).setAction(0)
    model.setFace(1)
    stage.say('&0\n운장?')
    stage.unit(3).setAction(6)
    stage.say('&3\n승상, 오랜만이오.')
    stage.unit(3).setAction(0)
    model.setFace(0)
    stage.say('&0\n……그대가 후미를 맡겠는가?\n&3\n그렇습니다.')
    stage.unit(3).setAction(3)
    stage.say('&3\n지나가게 둘 수 없소.\n무기를 드시오.')
    stage.unit(3).setAction(0)
    stage.say('&0\n…….(관우가 아무리 용맹하다 한들,\n홀로 적진에 들면 살아남지 못할 텐데.\n어찌하면 좋을까…….)')
    stage.effectSound(207, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('관우를 공격한다\n공격을 멈춘다', 0)
    if sel == 1:
        stage.addAmbition(5)
        vars[307] = 1
        stage.bgSound(-1)
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.say('&0\n운장, 우리를 보내 주게.\n&3\n이 관우가 있는 한\n단 한 발짝도 넘지 못한다!')
        stage.unit(0).setAction(17)
        stage.say('&0\n상대는 한 명뿐이다. 공격하라!')
        stage.unit(0).setAction(0)
        stage.say('&181\n과……관우다……!?\n&182\n아……안 된다……\n이길 수 없어.')
        stage.unit(22).setAction(0, 0)
        stage.say('&22\n무엇이 두렵느냐!? 어서 가라.\n&181\n구, 군사님, 상대는 관우입니다.\n이 정도 병력으로는 당해 낼 수 없습니다……\n&183\n아……안 됩니다.\n우선 목숨부터 구해야……\n&22\n으으윽……')
        stage.unit(22).setAction(0, 1)
        stage.say('&22\n주군, 안 되겠습니다.\n병사들이 모두 관우를 두려워합니다.')
        stage.unit(22).setAction(6)
        stage.say('&22\n아무래도 관우를 상대할 수 없겠군.')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(181),
                57,
                54,
                0],
            [
                stage.unit(182),
                52,
                54,
                0],
            [
                stage.unit(183),
                47,
                54,
                0]])
        stage.delay(3)
        stage.unit(181).setAction(0, 2)
        stage.unit(182).setAction(0, 2)
        stage.unit(183).setAction(0, 2)
        stage.delay(10)
        stage.unit(0).setAction(2)
        stage.say('&0\n그런 일이 있었단 말인가!')
        stage.unit(0).setAction(0)
        stage.say('&0\n분하군……\n어쩔 수 없지.')
        stage.delay(3)
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n운장, 다시 만날 날을 기약하세.')
        stage.unitsMove([
            [
                stage.unit(0),
                56,
                5,
                0],
            [
                stage.unit(22),
                48,
                5,
                0],
            [
                stage.unit(181),
                57,
                5,
                0],
            [
                stage.unit(182),
                52,
                5,
                0],
            [
                stage.unit(183),
                47,
                5,
                0]])
        stage.unit(0).hide()
        stage.unit(22).hide()
        stage.unit(181).hide()
        stage.unit(182).hide()
        stage.unit(183).hide()
        stage.unit(3).setAction(0, 2)
        stage.say('&3\n…………(나도 형주로 가야겠군.)')
        stage.unit(3).move(52, 98, 2)
        stage.unit(3).hide()
    elif sel == 2:
        vars[307] = 0
        stage.addAmbition(-10)
        stage.bgSound(19)
        stage.say('&0\n……곽가.\n퇴각하라!')
        stage.unit(22).setAction(0, 1)
        stage.say('&22\n뭐…… 뭐라고요! 어째서입니까!?\n천재일우의 기회입니다!\n&0\n……그만두게.')
        stage.unit(22).setAction(6)
        stage.say('&22\n관우와의 옛일이 떠오르십니까?\n주군, 마음이 너무 여리십니다!')
        stage.unit(0).setAction(0, 3)
        stage.say('&0\n봉효.')
        stage.unit(22).setAction(0)
        stage.say('&22\n예.\n&0\n이런 자리에서 관우를 죽이고 싶지는 않네.')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n이런 자그마한 전장이 영웅이 목숨을 바칠 곳은 아니지.')
        stage.unit(22).setAction(6)
        stage.say('&22\n주군, 전쟁에는 크고 작은 싸움의 구별이 없습니다!')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(17, 0)
        stage.say('&0\n퇴각하라! 명령이다.')
        stage.unit(22).setAction(0)
        stage.say('&22\n주군………')
        stage.unit(0).setAction(0, 0)
        stage.unitsMove([
            [
                stage.unit(0),
                56,
                5,
                0],
            [
                stage.unit(22),
                48,
                5,
                0],
            [
                stage.unit(181),
                57,
                5,
                0],
            [
                stage.unit(182),
                52,
                5,
                0],
            [
                stage.unit(183),
                47,
                5,
                0]])
        stage.unit(0).hide()
        stage.unit(22).hide()
        stage.unit(181).hide()
        stage.unit(182).hide()
        stage.unit(183).hide()
        stage.say('&3\n………\n(서로를 알아주는 이 마음,\n감사하기 그지없소.)')
    stage.effectSound(203, 255)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 398, 119)
    stage.showHead(32, 368, 199)
    stage.bottomTxt('양산에서 패한 유비는', False, False, False)
    stage.delay(5)
    stage.showHead(150, 282, 222)
    stage.delay(5)
    stage.bottomTxt('무리를 이끌고 의탁하러 갔다.', True, False, False)
    stage.delay(5)
    stage.head(32).move(346, 222)
    stage.delay(5)
    stage.bottomTxt('형주의 유표였다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('조조의 대군', True, True, False)
    stage.delay(5)
    stage.head(0).move(333, 95)
    stage.delay(5)
    stage.bottomTxt('허도로 개선한 뒤,', True, False, False)
    stage.delay(5)
    stage.bottomTxt('하북 평정을 준비하며 병사 훈련에 힘썼다.', True, False, True)
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
            15,
            58,
            70],
        [
            2,
            64,
            70],
        [
            23,
            70,
            70],
        [
            20,
            52,
            40,
            2],
        [
            22,
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
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n문약, 군마 준비는 어찌 되어 가는가?')
    stage.unit(20).setAction(6)
    stage.say('&20\n준비를 모두 마쳤습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('&22\n주군, 군량도 모두 갖추었으니\n이제 출병하여 하북을 평정할 수 있습니다.')
    stage.unit(22).setAction(0)
    stage.say('&0\n음………')
    stage.unit(24).setAction(6)
    stage.say('&24\n셋째 아들 원상이 대업을 잇기는 했으나\n원씨의 옛 부하들을 하나로 모으지 못하고 있습니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n게다가 장남 원담 일파는\n이미 원상과 원수가 되었습니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n좋은 기회입니다!')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다! 출병하라. 하북을 평정한다!')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 334, 133)
    stage.showHead(115, 353, 30)
    stage.bottomTxt('조조군', False, False, False)
    stage.delay(5)
    stage.head(0).move(289, 133)
    stage.head(0).move(289, 84)
    stage.delay(5)
    stage.bottomTxt('황하를 건너 북상하여', True, False, False)
    stage.delay(5)
    stage.bottomTxt('여양 부근으로 진군했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            115,
            53,
            28,
            2],
        [
            106,
            38,
            48,
            1],
        [
            102,
            38,
            54,
            1],
        [
            103,
            38,
            60,
            1],
        [
            113,
            68,
            48,
            3],
        [
            114,
            68,
            54,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('업성@원상 저택')
    stage.say('&115\n방금 보고를 받았다.\n조조군이 여양 근처까지 진군했다고 한다.\n&115\n조조가 아버님께서 하북에 세우신 기반을\n짓밟는 꼴은 결코 볼 수 없다.\n즉시 전투를 준비하라.')
    stage.unit(106).setAction(6)
    stage.say('&106\n상대는 조조다. 지략으로 이겨야 한다.')
    stage.unit(106).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(0, 1)
    stage.say('&113\n흥! 전군이 함께 조조에게 맞서면 되지\n무슨 계책이 필요하단 말이냐.\n&115\n형님, 그렇게 해서는\n조조의 상대가 되지 못할 것입니다.')
    stage.unit(113).setAction(0, 3)
    stage.delay(3)
    stage.unit(113).setAction(2, 3)
    stage.say('&113\n뭐라고!?\n&102\n저도 같은 생각입니다.')
    stage.unit(113).setAction(0)
    stage.say('&113\n으으으………\n&115\n우선 맏형님과 둘째 형님,\n그리고 곽도와 봉기 네 분께서\n군사를 이끌고 조조군과 싸워 주십시오.')
    stage.unit(102).setAction(0, 0)
    stage.say('&102\n나도 가야 하느냐?\n&115\n그렇습니다.')
    stage.unit(102).setAction(0, 1)
    stage.say('&102\n……좋다.\n&113\n원상, 그러면 너는 무엇을 할 셈이냐?\n&115\n저와 심배가 형님들을 지원하겠습니다.')
    stage.unit(113).setAction(0, 1)
    stage.say('&113\n흥!\n(꼴도 보기 싫군! 괘씸한 원상,\n어찌 그런 짓을 한단 말이냐?)')
    stage.unit(113).setAction(0, 3)
    stage.delay(3)
    stage.unit(103).setAction(6)
    stage.say('&103\n(원 장군, 잠시만 참으십시오.)')
    stage.unit(103).setAction(0)
    stage.say('&113\n(좋다!)\n&115\n둘째 형님도 괜찮으시겠지요.\n&114\n그래.\n&115\n좋습니다! 네 분은 즉시 출전하십시오.\n반드시 조조를 무찔러 주십시오!')
    stage.effectSound(200, 255)
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
            24,
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
            21,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('여양 교외  조조군 본진')
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n주군, 하북의 군대가 여양에서 우리를 기다리고 있습니다.\n&0\n음, 좋다.\n원소의 아들들이\n얼마나 재주가 있는지 지켜보겠다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n출발하라!')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 11, [
        22])
    gvars[4076] = 0
    gvars[4057] = 21
    stage.setBattlePos([
        [
            2,
            36],
        [
            1,
            37],
        [
            3,
            37],
        [
            3,
            35],
        [
            4,
            36],
        [
            1,
            35],
        [
            4,
            38],
        [
            3,
            39],
        [
            0,
            36],
        [
            2,
            38],
        [
            0,
            38],
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
    if stage.unitClickTest(20):
        stage.say('&20\n여양의 지형에는 특별한 점이 없습니다만\n곳곳에 울타리가 설치되어 있습니다.\n&0\n오?\n원씨 집안의 어린놈도 제법이군.')
    if stage.unitClickTest(22):
        stage.say('&22\n원담 일당은 진지를 굳히려는 모양입니다.\n방비가 상당히 견고하군요.\n&0\n음, 그 점은 원소를 닮았군.')
    if stage.unitClickTest(21):
        stage.say('&21\n원소가 죽은 지금\n하북에는 병력이 얼마나 남았을까요?')
    if stage.unitClickTest(1):
        stage.say('&1\n맹덕, 그 애송이들이 창정으로 출병했네.\n&0\n음.\n&1\n내가 보기에는 원소에 비해서도 한참 못 미치는군……')
    if stage.unitClickTest(24):
        stage.say('&24\n원담과 원희는 지금 전선에 있고\n원상은 아직 후방에 있는 듯합니다.\n&0\n오?')
    if stage.unitClickTest(15):
        stage.say('&15\n솔직히 저는 사람 죽이는 일이 싫습니다.\n&15\n아! 그……걱정하지 마십시오.\n용감히 싸워 적을 무찌르겠습니다.')
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
