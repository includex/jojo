# Source Generated with Decompyle++
# File: R_14.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 31)
    stage.showUnits([
        [
            119,
            5,
            54,
            1],
        [
            126,
            5,
            46,
            1],
        [
            2,
            5,
            50,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('여포와 초선')
    stage.setStageName('서주 교외  산길')
    stage.unit(2).move(15, 50, 1)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(2),
            70,
            50,
            1],
        [
            stage.unit(119),
            60,
            54,
            1],
        [
            stage.unit(126),
            60,
            46,
            1]])
    stage.unit(126).setAction(0)
    stage.unit(119).setAction(0)
    stage.unit(2).setAction(0)
    stage.say('&126\n후우.\n주군, 조조의 추격을 따돌린 듯합니다.\n&119\n음…….')
    stage.unit(126).setAction(0, 2)
    stage.say('&126\n우선 몸을 의탁할 곳을 찾아\n전열을 다시 가다듬으시지요.\n&119\n……하비로.\n하비로 가세.')
    stage.unit(2).setAction(0, 2)
    stage.delay(5)
    stage.say('&126\n……알겠습니다.\n하비에서 조조와 결전을 벌이시지요.\n&119\n음.(초선이 하비에 있다. 초선만은 죽게 둘 수 없어.)')
    stage.unit(2).setAction(0, 1)
    stage.say('&2\n(내 일생이 이렇게 끝나는 것인가?\n이제부터는 무장의 혼을 지니고\n목숨을 걸어 싸우자.)')
    stage.unitsMove([
        [
            stage.unit(2),
            95,
            50,
            1],
        [
            stage.unit(119),
            85,
            54,
            1],
        [
            stage.unit(126),
            85,
            46,
            1]])
    stage.unit(2).hide()
    stage.unitsMove([
        [
            stage.unit(119),
            95,
            54,
            1],
        [
            stage.unit(126),
            95,
            46,
            1]])
    stage.unit(119).hide()
    stage.unit(126).hide()
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    '하비  초선의 거처'
    stage.bgSound(17)
    stage.delay(3)
    stage.loadBg(2, 2)
    stage.showUnit(17, 53, 53, 1)
    stage.draw()
    stage.setStageName('하비  초선의 거처')
    stage.showUnit(119, 53, 27, 2)
    stage.say('&119\n초선!')
    stage.unit(17).setAction(0, 0)
    stage.unit(119).move(53, 45, 2)
    stage.say('&17\n봉선…….\n&119\n초선…….')
    stage.unit(17).move(53, 57, 2)
    stage.say('&17\n모두 들었어요.\n조조군이 이곳을 공격하러 온다면서요?\n&119\n알고 있었군……\n&17\n방금 진 대인께 여쭈었어요.\n&119\n미안하네………\n어쩌면 우리 모두 이번 싸움에서 죽을지도 모르겠어.\n&17\n…………….')
    if vars[302]:
        stage.unit(119).move(53, 47, 2)
        stage.say('&119\n초선! 지금 당장 이곳에서 피하게.')
        stage.unit(17).setAction(0, 0)
        stage.say('&17\n네?')
        stage.unit(119).setAction(3, 2)
        stage.say('&119\n조조에게 가도록 하게.\n&17\n조조에게요……?')
        stage.unit(119).setAction(0, 2)
        stage.say('&119\n조조는 자네를 무척 마음에 들어 하니\n분명 각별히 대우해 줄 것이네.')
        stage.unit(119).setAction(0, 0)
        stage.say('&119\n나는 아무래도 이 난세에서 살아남지 못할 듯하구나.\n하지만 너만은 살아야 한다………….')
        stage.delay(10)
        stage.unit(17).setAction(0, 1)
        stage.say('&17\n……봉선 님, 저는 가지 않겠어요.')
        stage.unit(119).setAction(0, 2)
        stage.say('&119\n어째서냐!?')
        stage.unit(17).setAction(0, 0)
        stage.say('&17\n저도 함께 싸우겠어요.')
    if not vars[302]:
        stage.delay(10)
        stage.unit(119).move(53, 47, 2)
        stage.say('&119\n초선, 당장 이 성을 떠나라.\n&119\n무슨 일이 있어도 살아남아야 한다.')
        stage.unit(119).setAction(3, 2)
        stage.say('&119\n아직은 달아날 수 있다.\n너만은 반드시 살아남아라!')
        stage.unit(119).setAction(0, 2)
        stage.delay(10)
        stage.unit(17).setAction(0, 0)
        stage.say('&17\n……봉선, 저는 가지 않겠어요.\n&119\n뭐라고!?\n&17\n저도 함께 싸우겠어요.')
        stage.delay(3)
    stage.unit(17).setAction(0, 2)
    stage.say('&17\n봉선, 우리는 아직 지지 않았어요.\n&119\n초선……하지만…….')
    stage.unit(17).setAction(0, 0)
    stage.delay(3)
    stage.unit(17).move(53, 48, 0)
    stage.say('&17\n그리 풀이 죽어 있으니\n당신답지 않네요.')
    stage.unit(17).setAction(6, 0)
    stage.say('&17\n마지막 순간까지\n봉선 님과 함께 있겠어요.\n&17\n더는 떠나라는 말씀 마세요.\n&119\n……알겠소.')
    stage.unit(17).setAction(0, 0)
    stage.delay(5)
    stage.unit(119).move(53, 42, 0)
    stage.delay(3)
    stage.unit(119).setAction(0, 2)
    stage.say('&119\n이 여포의 천하무쌍한 용맹을\n조조에게 똑똑히 보여 주겠다.\n&17\n예.')
    stage.unit(119).move(53, 27, 0)
    stage.unit(119).hide()
    stage.delay(3)
    stage.unit(17).setAction(0, 1)
    stage.say('&17\n……봉선, 고마워요.')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 414, 73)
    stage.showHead(119, 478, 113)
    stage.showHead(32, 414, 153)
    stage.bottomTxt('여포는 서주에서 조조·유비 연합군에게 패했고', False, False, False)
    stage.delay(5)
    stage.head(119).move(546, 153)
    stage.delay(5)
    stage.bottomTxt('이후 하비로 패주하여 성을 굳게 지켰다.', True, False, False)
    stage.delay(8)
    stage.head(32).move(482, 193)
    stage.head(0).move(482, 113)
    stage.delay(5)
    stage.bottomTxt('조조는 여포에게 쉴 틈을 주지 않고 공격을 이어 갔다.', True, True, True)
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
            21,
            60,
            42,
            3],
        [
            32,
            49,
            52,
            2]])
    stage.draw()
    stage.setStageName('하비 교외  조조군 본진')
    stage.say('&0\n현덕, 마침내 여포를 가두었군.\n&32\n그렇군요.')
    stage.unit(0).setAction(3)
    stage.say('&0\n현덕 공, 군사를 세 갈래로 나누어\n북문과 동문, 서문을 각각 공격할 생각이오.')
    stage.unit(0).setAction(0)
    stage.say('&0\n그러니 그대의 부대가 남문을 공격해 주기 바라오.')
    stage.unit(32).setAction(6)
    stage.say('&32\n좋다.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(32).move(49, 16, 0)
    stage.unit(32).hide()
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n이번에는 반드시 여포를 없애겠다.')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 14
    stage.setBattlePos([
        [
            13,
            1],
        [
            26,
            15],
        [
            1,
            18],
        [
            14,
            1],
        [
            26,
            16],
        [
            1,
            19],
        [
            12,
            2],
        [
            26,
            14],
        [
            1,
            17],
        [
            15,
            2],
        [
            26,
            17],
        [
            1,
            20],
        [
            13,
            2],
        [
            27,
            15],
        [
            0,
            18]])
    return False


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n하비성에는 사방에 성문이 있습니다.\n&20\n북쪽에서 동쪽으로 강이 흐르며\n서쪽에서 남쪽으로는 산과 숲이 둘러싸고 있습니다.')
    if stage.unitClickTest(22):
        stage.say('&22\n하비성 밖에는 눈이 많이 쌓인 듯합니다.\n&15\n오! 눈이 쌓였다고요!?\n&20\n허 장군, 기뻐할 일이 아닙니다.\n&20\n설원에서는 화계 책략을 쓸 수 없습니다.\n&15\n그……그렇습니까!? 그럼 곤란하군요…….\n&22\n아직도 눈이 내리는 듯하니\n성 안에서도 쓰지 못할 것입니다.\n&15\n……정말 곤란하군요.')
    if stage.unitClickTest(6):
        stage.say('&6\n상대가 여포인 만큼\n장비도 철저히 갖추어야 한다.')
    if stage.unitClickTest(15):
        stage.say('&15\n여포, 저놈은 참으로 상대하기 어렵군.')
    if stage.unitClickTest(1):
        if vars[302]:
            stage.say('&1\n참. 아까 하비성 위를 보니\n여장수가 있는 듯하더군.\n&0\n여장수?\n&1\n그래. 제법 아름답더군.\n그런데 예전에 여포와 싸울 때는\n한 번도 본 적이 없네.\n&0\n(여장수라……설마 초선인가…….)')
        if not vars[302]:
            stage.say('&1\n성에 틀어박혀 지키는 건 여포답지 않군.')
    if stage.unitClickTest(21):
        stage.say('&21\n장료는 장수의 재목입니다.\n여기서 죽게 두기에는 너무 아깝습니다.\n&0\n음.')
    if stage.unitClickTest(32):
        stage.say('&32\n남문은 제게 맡겨 주십시오.')
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
