# Source Generated with Decompyle++
# File: R_34.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
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
    stage.effectSound(200, 0)
    stage.delay(3)
    if vars[311]:
        stage.setEventName('신의')
    if not vars[311]:
        stage.setEventName('정욱의 고백')
    stage.setStageName('합비  조조군 본진')
    stage.delay(3)
    stage.unit(11).setAction(0, 2)
    stage.say('&11\n동오가 한 달 넘게 움직이지 않는군.\n합비를 포기한 것일까?')
    stage.say('&2\n아닙니다. 손권은 이대로 돌아가지 않을 것입니다.\n&2\n그러고 보니 주군께서는 아직 한중에 계십니까?')
    stage.unit(10).setAction(0, 2)
    stage.say('&10\n그럴 리 없습니다. 지금쯤 허도로 돌아가고 계실 것입니다.\n&2\n좋다. 즉시 주군께 구원을 청하자.\n동오의 움직임은 도무지 예측할 수가 없군.')
    stage.unit(2).setAction(0, 1)
    stage.say('&2\n손권은 병력을 모아\n결사를 벌일 준비를 하는지도 모르겠소.\n&10\n그렇다면 제가 직접 허도로 가겠습니다.\n반드시 원군을 데려오겠습니다.')
    stage.unit(2).setAction(0, 0)
    stage.say('&2\n음, 장군께 부탁하오.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
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
            8,
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
            10,
            48,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n그런가?\n동오군이 한 달째 움직이지 않았다고?\n&10\n예.\n몰래 사람을 보내 살폈으나\n여전히 동오의 동향을 파악하지 못했습니다.\n&10\n병력을 모아\n언제든 대전을 치를 준비를 하는지도 모르기에\n주군께 원군을 청하러 왔습니다.\n&0\n좋다. 내가 직접 군사를 이끌고 합비로 가겠다.\n&10\n예. 감사합니다, 주군.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(2, 407, 167)
    stage.showHead(76, 471, 187)
    stage.showHead(0, 331, 87)
    stage.delay(5)
    stage.bottomTxt('조조가 이끄는 원군이', False, False, False)
    stage.delay(5)
    stage.head(0).move(471, 87)
    stage.head(0).move(471, 107)
    stage.delay(5)
    stage.bottomTxt('직접 합비로 가서 전투를 독려했다.', True, False, True)
    stage.delay(3)
    stage.loadBg(2, 53)
    if not vars[311]:
        stage.bgSound(19)
        stage.delay(3)
        stage.showUnit(9, 44, 52, 1)
        stage.draw()
        stage.setStageName('합비  조조군 본진')
        stage.showUnit(21, 54, 22, 2)
        stage.say('&21\n방덕 장군, 혼자이십니까?')
        stage.unit(21).move(54, 52, 2)
        stage.delay(3)
        stage.unit(21).setAction(0, 3)
        stage.say('&21\n마침 방덕 장군께 드릴 말씀이 있습니다.\n&9\n무슨 일이오?\n&21\n마등에 관한 일입니다……\n마등을 죽인 사람은 접니다.\n&9\n……!?')
        stage.unit(21).setAction(0, 2)
        stage.say('&21\n그때 마등은 주군을 해치려 했으나\n주군께서는 그를 해칠 뜻이 없으셨습니다.')
        stage.unit(21).setAction(0, 3)
        stage.say('&21\n마등을 죽이자고 한 것은 나일세.\n모든 책임은 내게 있네.\n&9\n어찌하여 마등을 죽였소?\n&21\n나는 주군의 군사다.\n군사의 임무는………\n주군의 안위를 위협하는 모든 요인을 없애는 것이지.')
        stage.unit(21).setAction(0, 2)
        stage.say('&21\n마등은 주군을 해치려는 음모에 가담했으니\n살려 둘 수가 없었네.\n그 정도 이치는 자네도 알겠지.\n&9\n오랫동안 전장을 누볐으니\n그 정도 이치는 알고 있소.')
        stage.unit(9).setAction(0, 3)
        stage.say('&9\n조 공께서 마등 주군의 죽음을 말씀하지 않으신 것은\n혼자 그 죄를 짊어지려 하셨기 때문인가?\n&9\n난세에서 살아남을 패자가 되려면\n사사로운 원한에 얽매이지 않는 방책을 택해야 하는 법.\n&9\n선악을 가리지 않고 모두 짊어지는 것,\n그것이 조 공의 진정한 모습이었는가?\n&9\n나 방덕을 거리낌 없이 받아 주신 것도\n부하를 믿는다는 증거겠지?\n&9\n그렇다면 나도 앞으로 힘을 다하여\n조 승상을 위해 견마지로를 다하겠다!')
        stage.unit(21).setAction(0, 3)
        stage.say('&21\n방덕 장군……')
        stage.delay(10)
        stage.bgSound(-1)
        stage.unit(21).setAction(0, 0)
        stage.say('&21\n오?\n모두 모이신 듯하군요.')
        stage.bgSound(15)
        stage.delay(3)
        stage.unit(21).move(64, 52, 3)
        stage.unit(9).setAction(0, 1)
        stage.showUnit(0, 54, 22, 2)
        stage.unit(0).move(54, 28, 2)
        stage.showUnit(1, 54, 22, 2)
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                34,
                2],
            [
                stage.unit(1),
                54,
                28,
                2]])
        stage.showUnit(15, 54, 22, 2)
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                40,
                2],
            [
                stage.unit(1),
                54,
                34,
                2],
            [
                stage.unit(15),
                54,
                28,
                2]])
        stage.showUnit(20, 54, 22, 2)
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                46,
                2],
            [
                stage.unit(1),
                54,
                40,
                2],
            [
                stage.unit(15),
                54,
                34,
                2],
            [
                stage.unit(20),
                54,
                28,
                2]])
        stage.showUnit(19, 54, 22, 2)
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                52,
                2],
            [
                stage.unit(1),
                54,
                46,
                2],
            [
                stage.unit(15),
                54,
                40,
                2],
            [
                stage.unit(20),
                54,
                34,
                2],
            [
                stage.unit(19),
                54,
                28,
                2]])
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                80,
                0],
            [
                stage.unit(1),
                44,
                64,
                1],
            [
                stage.unit(15),
                44,
                58,
                1],
            [
                stage.unit(20),
                64,
                64,
                3],
            [
                stage.unit(19),
                64,
                58,
                3]])
        stage.delay(10)
    if vars[311]:
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
                9,
                44,
                52,
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
        stage.setStageName('합비  조조군 본진')
    stage.bgSound(15)
    stage.delay(3)
    stage.showUnit(181, 54, 22, 2)
    stage.say('&181\n보고드립니다!')
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 동오군의 동향을 알아냈습니다.\n유수구 건너편에 대군을 집결시켰습니다.\n&0\n유수구라고……?\n손권, 그 어린 녀석이\n제 장기인 수전으로 결판을 낼 생각이로군.\n&0\n음, 수고했다.\n물러가 쉬도록 하라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.say('&0\n방금 모두 들었듯이\n시간이 흐를수록 동오의 병력은 날로 늘어날 것이다.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n어서 공세를 시작하는 편이 좋겠습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.say('&19\n하지만 곧장 공격하기에는 너무 위험합니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n음, 방덕.')
    stage.unit(9).setAction(0, 2)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('&9\n예……\n&0\n자네는 별동대를 이끌고 출전하게.\n내가 주력으로 손권을 붙잡아 두는 동안\n틈을 타 그의 부대를 기습하게.')
    if vars[311]:
        stage.say('&9\n저에게 별동대를 맡기신다고요!?\n&0\n음, 그렇다.\n이번 싸움은 그대만 믿겠다.\n&9\n어찌하여 저를 보내십니까?\n제가 돌아설까 두렵지 않으십니까?\n&0\n그대는 신의를 저버릴 사람이 아니네.\n&9\n(조 공께서는 내 사람됨을 아시고\n조금도 의심하지 않으시는구나.)\n&9\n(나는 이분께 미치지 못한다…….\n참으로 도량이 넓으신 분이야.)\n&9\n(돌이켜 보면 마등 주군의 불행도\n난세에서는 흔한 일이며\n어쩔 수 없는 것이었겠지.)\n&9\n(마등 주군, 용서해 주십시오!\n제 마음은 이미 조조께 향했습니다.)')
    if not vars[311]:
        stage.say('&9\n예, 명을 받들겠습니다.')
        stage.unit(0).setAction(17)
        stage.say('&0\n좋다, 출발하라!')
    stage.unit(0).setAction(0)
    stage.unit(9).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19,
        2,
        11,
        10,
        9])
    gvars[4076] = 0
    gvars[4057] = 34
    stage.setBattlePos([
        [
            12,
            8],
        [
            13,
            8],
        [
            11,
            19],
        [
            10,
            18],
        [
            12,
            18],
        [
            0,
            19],
        [
            0,
            20],
        [
            0,
            19],
        [
            0,
            20],
        [
            11,
            18],
        [
            10,
            17],
        [
            12,
            17],
        [
            11,
            17],
        [
            11,
            9],
        [
            14,
            9]])
    return False


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n지금 양군은 장강을 사이에 두고 대치하고 있습니다.\n&20\n무사히 건너편에 상륙할 수 있다면 좋겠지만\n상대가……\n&0\n동오의 수군이 걱정된다는 말인가?')
    if stage.unitClickTest(19):
        stage.say('&19\n손권은 매우 신중한 사람으로 보입니다.\n&19\n이번 싸움은 충분히 준비해야 합니다.')
    if stage.unitClickTest(21):
        stage.say('&21\n동오의 장수 가운데\n여몽과 육손은 주유에 필적하는 재능을 지녔습니다.')
    if stage.unitClickTest(9):
        stage.say('&0\n그대만 믿겠네, 방덕.\n&9\n염려 마십시오, 주군.')
    if stage.unitClickTest(15):
        stage.say('&15\n동오와는 늘 물 위에서만 싸우네요.\n산이라면 제가 가장 자신 있는데 말입니다.\n&0\n오?\n그럼 이번에는 허저를 쉬게 해야겠군.\n&15\n아이, 주군. 그런 말씀은 마십시오…….')
    if stage.unitClickTest(1):
        stage.say('&1\n손권은 참을성 있게 싸우는군.\n&0\n음, 아버지나 형과는 다른 점이지.')
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
