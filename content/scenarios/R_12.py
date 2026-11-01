# Source Generated with Decompyle++
# File: R_12.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 47)
    stage.showUnits([
        [
            0,
            71,
            52,
            3],
        [
            20,
            54,
            38,
            2],
        [
            22,
            48,
            38,
            2],
        [
            24,
            42,
            38,
            2],
        [
            21,
            36,
            38,
            2],
        [
            1,
            54,
            68],
        [
            15,
            48,
            68],
        [
            6,
            42,
            68],
        [
            5,
            36,
            68]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setEventName('성동격서')
    stage.setStageName('수춘  임시 승상부')
    stage.say('&0\n원술은 어디로 달아났는가?')
    stage.unit(20).setAction(0, 1)
    stage.say('&20\n잔병을 모아 여남으로 달아난 듯합니다.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n여남……?')
    stage.unit(22).setAction(0, 1)
    stage.say('&22\n참칭 황제 원술은 이미 이름만 남았습니다.\n더 추격할 필요는 없습니다.')
    stage.unit(22).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(0, 1)
    stage.say('&24\n그렇습니다.')
    stage.unit(24).setAction(0, 2)
    stage.say('&0\n음…….')
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n나머지 세 부대는 어찌 되었는가?')
    stage.unit(20).setAction(0, 1)
    stage.say('&20\n여포와 손책은 각자 영지로 돌아갔고\n유비만 수춘에 남아 있습니다.\n&0\n좋다. 유비를 소패로 보내게.\n&20\n소패라면 여포의 서주와 맞닿은 곳 말씀이십니까?\n&0\n음.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n여포, 그놈……절대로 용서하지 않겠다.')
    stage.unit(0).move(71, 56, 2)
    stage.say('&0\n유비를 여포에 맞설 전초 기지로 삼아야겠군.')
    stage.unit(0).move(71, 52, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n즉시 준비하라.')
    stage.unit(20).setAction(6, 1)
    stage.say('&20\n예.')
    stage.unit(0).setAction(17, 3)
    stage.say('&0\n좋다, 허도로 개선하자.')
    stage.effectSound(201, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 388, 169)
    stage.showHead(32, 452, 169)
    stage.bottomTxt('그렇게,', False, False, False)
    stage.delay(5)
    stage.head(0).move(337, 93)
    stage.bottomTxt('조조는 허도로 개선했고,', True, False, False)
    stage.delay(5)
    stage.head(32).move(432, 110)
    stage.bottomTxt('유비는 명을 받들어 소패로 향했다.', True, False, True)
    stage.bottomTxt('그 무렵 장안에서는…….', False, True, False)
    stage.delay(5)
    stage.showHead(131, 157, 91)
    stage.showHead(129, 221, 91)
    stage.bottomTxt('세력이 날로 쇠퇴하던 이각과 곽사는', True, True, False)
    stage.delay(5)
    stage.head(131).hide()
    stage.head(129).hide()
    stage.delay(5)
    stage.bottomTxt('끝내 반란을 일으킨 병사들에게 살해당했다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene1():
    '허도  승상부'
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
            5,
            64,
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
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n이각과 곽사가 반란을 일으킨 병사에게 죽었다더군.\n그것이 사실인가, 문약?\n&20\n예, 주군.\n그 병사를 어찌 처분하시겠습니까?')
    stage.effectSound(205, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('관직을 주어 후대한다\n충의를 모르는 자를 벤다', 0)
    if sel == 1:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.say('&0\n관직을 내려\n정식으로 장안 수비를 맡게 하라.\n문약, 즉시 처리하게!')
    elif sel == 2:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.say('&0\n이유가 무엇이든 제 주인을 배신하고 죽인 자는\n은혜를 모르는 불의한 자다.\n&0\n당장 참수하라!\n장안의 수비를 맡을 자는\n우리 군에서 뽑아 보내겠다.\n&0\n인선은 자네에게 맡기겠네, 문약.\n즉시 착수하게!')
    stage.unit(20).setAction(6)
    stage.say('&20\n예.')
    stage.showUnit(181, 95, 55, 3)
    stage.say('&181\n보고드립니다!')
    stage.unit(20).setAction(0)
    stage.unit(181).move(48, 55, 3)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 완성의 첩자가 보고를 보내 왔습니다.\n&0\n완성에서 왔다고?\n&181\n예.\n&181\n장수가 형주의 유표와 손을 잡고\n불온한 일을 꾀하는 듯하다고 합니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n뭐라고!?\n그게 사실이냐?\n&181\n틀림없는 사실입니다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('&22\n주군, 그렇다면 사태가 심각합니다.')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6, 2)
    stage.say('&21\n음.')
    stage.unit(21).setAction(0, 2)
    stage.say('&21\n유표는 동탁 토벌에 참여하지 않고\n오로지 자기 나라를 키우는 데 힘쓴 독선적인 인물입니다.')
    stage.say('&21\n손견마저 물리친 적이 있으니\n그 부대의 강함을 짐작할 만합니다.')
    stage.unit(24).setAction(6, 2)
    stage.say('&24\n우리가 먼저 손을 써야 합니다.\n즉시 완성을 공격하시지요.')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n저도 같은 생각입니다.\n날개가 자라기 전에 없애야 합니다.\n&0\n옳은 말이네…….')
    stage.unit(0).setAction(17, 1)
    stage.say('&0\n좋다! 전군 완성으로 향한다.\n출발하라!')
    stage.effectSound(205, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 346, 83)
    stage.bottomTxt('완성의 장수를 만나', False, False, False)
    stage.delay(5)
    stage.showHead(143, 248, 144)
    stage.delay(5)
    stage.bottomTxt('움직일 기회를 엿보기 시작했고,', True, False, False)
    stage.delay(7)
    stage.head(0).move(312, 104)
    stage.delay(7)
    stage.bottomTxt('조조군은 다시 완성을 공격했다.', True, True, True)
    stage.bgSound(15)
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
            21,
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
    stage.setStageName('완성 교외  조조군 본진')
    stage.say('&0\n완성에서는 이미 뼈아픈 일을 겪었다.\n대책을 세운 뒤 공격해야겠군……\n문약, 완성의 상황은 어떠한가?')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n크게 특별한 점은 없습니다만\n사방 성벽 중 동쪽 성벽의\n일부가 무너져 있습니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n뭐라고, 동쪽 성벽이라고?\n&20\n그렇습니다.')
    stage.unit(1).setAction(3, 1)
    stage.say('&1\n맹덕, 그렇다면 망설일 것 없네.\n전군은 동쪽 성벽으로 쳐들어가세.')
    stage.unit(1).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n아니다. 우리는 서쪽 성벽을 공격한다.')
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n뭐라고?! 어째서인가?')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n이를 성동격서라 합니다.\n&0\n음.')
    stage.unit(1).setAction(0, 1)
    stage.say('&1\n성동격서의 계책이 무엇인가?')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.say('&20\n장수에게 서쪽 성벽을 공격하려는 듯 보이는 것입니다.\n&20\n그러면 장수는 당연히 서쪽에 수비병을 모으겠지요.\n&20\n그 틈에 우리 정예 부대가\n수비가 허술해진 동쪽 성벽을 공격하는 것입니다.\n&1\n그런 것이었군…….\n&0\n바로 그렇네.')
    stage.unit(0).setAction(17, 0)
    stage.say('&0\n작전은 정해졌다.\n출발하라!')
    stage.unit(0).setAction(0, 0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 8)
    gvars[4076] = 0
    gvars[4057] = 12
    stage.setBattlePos([
        [
            11,
            9],
        [
            11,
            10],
        [
            10,
            8],
        [
            10,
            11],
        [
            10,
            9],
        [
            10,
            10],
        [
            11,
            8],
        [
            11,
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
            0]])
    return False


def scene3():
    if stage.unitClickTest(22):
        stage.say('&22\n주군, 정면으로 공격하는 편이 좋겠습니다.\n&0\n어째서인가?\n&22\n장수군에는 가후가 있습니다.\n우리의 성동격서 계책으로 그를 속일 수 있겠습니까?\n&0\n적에게 간파당할 것만 걱정하다가는\n아무것도 성공하지 못하네.\n&22\n예…….')
    if stage.unitClickTest(20):
        stage.say('&20\n전장의 지형을 말씀드리면\n완성은 사방이 높은 산으로 둘러싸여 있습니다.\n&0\n평지는 없는가?\n&20\n성벽에 가까운 곳이 조금 평탄할 뿐\n나머지는 좁은 오솔길뿐입니다.\n&0\n오솔길이라고……?')
    if stage.unitClickTest(21):
        stage.say('&21\n가후의 움직임도 신경 쓰이지만\n유표의 동향도 마찬가지입니다.\n&0\n음.')
    if stage.unitClickTest(24):
        stage.say('&24\n속전속결로 끝내야 합니다!')
    if stage.unitClickTest(15):
        stage.say('&15\n부디 저를 내보내 주십시오.')
    if stage.unitClickTest(1):
        stage.say('&1\n지난날 완성에서 죽은 동료들의 원수를 갚겠다.')
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.say('&22\n(성동격서라…성공할까?\n그 가후를 정말 속일 수 있을까……?)')
        stage.bgSound(-1)
        stage.clsUnit()
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
