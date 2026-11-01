# Source Generated with Decompyle++
# File: R_17.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 18)
    stage.showUnits([
        [
            101,
            35,
            56,
            1],
        [
            32,
            50,
            45,
            2],
        [
            109,
            50,
            67]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('의혹')
    stage.setStageName('백마  원소군 본진')
    stage.say('&101\n안량이 목숨을 잃을 줄이야…….')
    stage.unit(101).setAction(2)
    stage.say('&101\n조조, 이 간악한 도적놈!')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(109).setAction(0, 1)
    stage.say('&109\n안량 형님께서 이런 곳에서 돌아가실 줄이야!')
    stage.unit(109).setAction(2)
    stage.say('&109\n분하다!')
    stage.unit(109).setAction(0)
    stage.delay(5)
    stage.say('&101\n참, 유비 공.')
    stage.unit(101).setAction(0, 3)
    stage.say('&101\n부하의 보고를 받았소.\n관 장군이 백마의 전장에 나타났다고 하더군.')
    stage.unit(109).setAction(0, 0)
    stage.delay(3)
    stage.unit(109).setAction(3)
    stage.say('&109\n관우!?')
    stage.unit(32).setAction(3)
    stage.say('&32\n오!!')
    stage.unit(109).setAction(0)
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.say('&101\n붉은 얼굴에 수염이 긴 장한이라더군.')
    stage.unit(101).setAction(0, 1)
    stage.say('&101\n유비, 어찌 된 일이오?')
    stage.unit(32).setAction(3)
    stage.say('&32\n저……정말입니까?')
    stage.unit(32).setAction(0)
    stage.say('&32\n운장이 간적 조조에게 목숨을 바칠 리 없습니다.')
    stage.unit(109).setAction(2)
    stage.say('&109\n변명하지 마라……!\n&101\n문추, 경솔하게 굴지 마라.')
    stage.unit(109).setAction(0)
    stage.say('&101\n유비 대인……\n우리를 속이는 것은 아니겠지요?')
    stage.unit(32).setAction(6)
    stage.say('&32\n원 공, 조조군에는 맹장이 많다고 합니다.\n&32\n그중에는 제 둘째 아우와 닮은 자도 있겠지요.\n&32\n조조는 본래 계략에 능한 자이니\n틀림없이 우리 사이를 갈라놓으려는\n간계일 것입니다.')
    stage.unit(32).setAction(0)
    stage.say('&101\n음……그렇군.\n조조라면 충분히 그럴 수 있지.')
    stage.unit(101).setAction(2)
    stage.say('&101\n조조, 가증스러운 놈.\n감히 이런 잔꾀를 부리다니.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(109).setAction(2)
    stage.say('&109\n주군, 형님의 원수를 갚게 해 주십시오!\n&101\n음, 그 마음이 참으로 기쁘구나.\n다음 싸움에서는 네가 선봉을 맡거라.')
    stage.unit(109).setAction(0)
    stage.say('&109\n예!')
    stage.unit(32).setAction(0, 1)
    stage.say('&32\n(둘째가 아직 살아 있을지도 모른다…….\n게다가 조조군의 장수가 되었단 말인가……!)')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene1():
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
            3,
            49,
            54,
            2]])
    stage.draw()
    stage.setStageName('백마  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n운장, 이번 싸움에서 참으로 훌륭했네!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('&3\n이런 작은 일로는 승상의 큰 은혜에 보답할 수 없습니다.')
    stage.say('&0\n음……그래.\n참으로 수고했네. 물러가 쉬게.\n&3\n알겠소.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).move(49, 16, 0)
    stage.unit(3).hide()
    stage.delay(3)
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n주군.\n이번에는 국지전에서 이겼을 뿐입니다.')
    stage.unit(21).setAction(0, 2)
    stage.say('&21\n그렇습니다. 큰 형세는 여전히 원소군에 유리합니다.\n&0\n음, 알고 있다.')
    stage.showUnit(181, 49, 16, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 원소가 연진을 공격했습니다!')
    stage.unit(22).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n뭐라고!?\n&22\n큰일이군요.')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n연진이 함락되면\n백마를 지키기 어려울 뿐 아니라\n허도마저 위험해집니다.')
    stage.unit(0).setAction(0)
    stage.say('&0\n음, 원소군의 선봉은 누구냐?\n&181\n대장 문추입니다.\n&0\n문추라고?\n또 용장이 나섰군.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n예.\n안량보다도 강한 맹장이라 합니다.')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n그렇다 해도\n그자는 용맹할 뿐 지략이 부족하니…….')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n알고 보니 용맹만 있고 지모는 없는 장수로군.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n좋다.\n상황이 급박하니\n연진으로 가서 원소를 맞아 싸울 수밖에.')
    stage.unit(0).setAction(17)
    stage.say('&0\n출발을 준비하라!\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(22).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        2])
    gvars[4076] = 0
    gvars[4057] = 17
    stage.setBattlePos([
        [
            11,
            21],
        [
            15,
            14],
        [
            20,
            14],
        [
            4,
            16],
        [
            3,
            15],
        [
            5,
            15],
        [
            15,
            13],
        [
            20,
            13],
        [
            15,
            12],
        [
            20,
            12]])
    return False


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(3):
        stage.say('&3\n문추를 상대하는 일은 이 관우에게 맡기시오.')
    if stage.unitClickTest(1):
        stage.say('&1\n원소군에서 가장 강한 자가 안량과 문추지.\n안량을 없앴으니 문추까지 베어 버리면\n앞으로 싸우기가 한결 쉬울 걸세.\n&0\n바로 그 말일세.')
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 전장의 지형을 설명해 보게.\n&20\n예.\n지난 백마와 비교하면\n크게 다른 점은 없습니다.\n&20\n이번에도 승패의 관건은\n다리를 점령할 수 있느냐에 달려 있습니다.\n&0\n음.')
    if stage.unitClickTest(22):
        stage.say('&22\n원소가 백마를 차지하지 않고\n방향을 돌려 연진을 공격할 줄이야…….\n&0\n그를 다시 보게 되었나?\n&22\n……아닙니다. 그 정도는 아닙니다.\n&0\n자네도 참 고집스럽군…….')
    if stage.unitClickTest(21):
        stage.say('&21\n무기점에 새 갑옷과 옷이 들어온 듯합니다.\n이런 물건도 차차 갖추어야겠지요.')
    if stage.unitClickTest(15):
        stage.say('&15\n콩주머니는 편리하지요. 하지만…….\n공격받을 때마다 회복용 콩을 써 버리니\n돈이 제법 듭니다.')
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
