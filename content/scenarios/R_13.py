# Source Generated with Decompyle++
# File: R_13.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(16)
    stage.setMenuVisible(False)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(143, 251, 146)
    stage.showHead(0, 315, 146)
    stage.bottomTxt('조조는 장수 토벌에 다시 실패하여,', False, False, False)
    stage.delay(5)
    stage.head(0).move(342, 111)
    stage.delay(5)
    stage.bottomTxt('허도로 돌아가 서둘러 병사를 훈련할 수밖에 없었다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('이때,', True, True, False)
    stage.delay(5)
    stage.showHead(101, 390, 22)
    stage.delay(5)
    stage.bottomTxt('하북의 원소는', True, False, False)
    stage.delay(5)
    stage.showHead(161, 471, 1)
    stage.delay(5)
    stage.bottomTxt('북평의 공손찬을 공격하기로 하고,', True, False, False)
    stage.delay(8)
    stage.bottomTxt('이에 사신을 보내 조조에게 지원을 청했다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1,
            5],
        [
            20,
            52,
            40,
            2],
        [
            22,
            58,
            40,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('여포를 쓰러뜨려라!')
    stage.setStageName('허도  승상부')
    stage.say('&0\n원소가 순진하게도 나를 찾아와서는\n병사와 군량을 빌려 달라더군.\n&20\n병사와 군량을 빌려 달라고요……?')
    stage.unit(0).setAction(0)
    stage.say('&0\n북평의 공손찬을 공격하기 위해서라더군.\n&22\n그런 사사로운 다툼 때문에……\n우리에게 이토록 파렴치한 요구를 하다니요.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n그자는 늘 그런 식이지.\n&20\n그렇습니다……. 주군께서는 뭐라 답하시겠습니까?\n&0\n음.')
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('참고 받아들인다\n요구를 거절한다', -1)
    if sel == 1:
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n그의 요구를 받아들이게.\n&20\n예.\n&0\n아직 원소와 맞설 때는 아니야.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n문약, 최대한 관대한 모습을 보이게.\n그러면 원소도 북평에만 눈을 돌릴 것이네.')
        stage.unit(0).setAction(17, 2)
        stage.say('&0\n이 기회를 이용해 외적을 소탕해야 한다.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n이 빚은 훗날 원소에게 갚아 주겠다.')
        stage.unit(20).setAction(6, 2)
        stage.say('&20\n분부대로 하겠습니다.')
        vars[305] = 1
    elif sel == 2:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n그 요구는 거절하라!')
        stage.unit(20).setAction(6)
        stage.say('&20\n주군, 다시 생각해 주십시오.\n지금은 참고 원소의 요구를 받아들이는 것이 상책입니다.')
        stage.unit(22).setAction(6)
        stage.say('&22\n그렇습니다.\n그렇지 않았다면 원소군은 북평이 아니라\n허도로 곧장 쳐들어왔을 것입니다.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n원소는 오지 않을 것이네.\n&22\n하지만…….')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n원소를 얕보지 마라.\n북평이라는 후환이 남아 있는데\n나를 공격할 만큼 어리석지는 않다.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n가증스러운 자로군.\n분명 나를 시험하는 것이야!')
        stage.unit(20).setAction(0)
        stage.say('&20\n주군을 시험한다고요?\n&0\n그렇네. 자네들처럼……\n내게도 대세를 보는 눈이 있는지 확인하려는 것이지.')
        stage.unit(22).setAction(0)
        stage.say('&22\n……………。')
        stage.unit(0).move(34, 59, 2)
        stage.say('&0\n내가 욱해서 청을 거절한다면\n그는 내가 그런 인물이라고 여길 것이네.\n&20\n그런 뜻이셨군요…….')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n문약, 사자를 호되게 꾸짖어 돌려보내게.')
        stage.unit(0).move(34, 55, 0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n우리 주변에는 아직 장수와 여포를 비롯해\n쓰러뜨려야 할 적이 많다.\n&0\n그들을 모두 제거한 뒤에\n원소와 결전을 벌이겠다.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n그때까지는\n최대한 나를 얕보도록 두겠네.')
        stage.unit(20).setAction(6)
        stage.say('&20\n예.')
        vars[305] = 0
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            101,
            53,
            28,
            2],
        [
            113,
            68,
            48,
            3],
        [
            114,
            68,
            54,
            3],
        [
            115,
            68,
            60,
            3],
        [
            109,
            68,
            66,
            3],
        [
            106,
            38,
            48,
            1],
        [
            110,
            38,
            54,
            1],
        [
            102,
            38,
            60,
            1],
        [
            103,
            38,
            66,
            1],
        [
            193,
            53,
            48,
            0,
            6]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('며칠 뒤…….', 3)
    stage.setStageName('신도  원소 저택')
    if vars[305]:
        stage.say('&101\n오, 조조가 내 요구를 받아들였다고?\n&193\n예.\n저희가 요구한 것보다 더 많은 병사와 군량을 제공했습니다.\n&106\n조조는 생각보다도 더 순박한 자인 모양입니다.')
        stage.unit(101).setAction(0, 1)
        stage.say('&101\n으음.\n조금 의외로군.')
        stage.unit(101).setAction(0, 2)
        stage.delay(3)
        stage.unit(101).setAction(3)
        stage.say('&101\n좋다. 원래 계획대로 북평을 공격한다.')
        stage.unit(101).setAction(0)
        stage.delay(3)
        stage.unit(101).setAction(17)
        stage.say('&101\n공손찬을 없애고 나면\n하북 전체가 내 것이 된다!')
    if not vars[305]:
        stage.say('&101\n오, 조조 그놈이 내 요구를 거절했다고?\n&193\n예.\n게다가 차마 듣기 힘든 욕설까지 퍼부었습니다…….\n&106\n조조도 생각보다 감정적인 인간인 듯합니다.')
        stage.unit(101).setAction(0, 1)
        stage.say('&101\n으음.\n조금 의외로군.')
        stage.unit(101).setAction(0, 2)
        stage.say('&101\n음, 먼 길 오느라 수고했소.')
        stage.unit(101).setAction(0, 0)
        stage.say('&101\n애초에 진심으로 요구한 것도 아니니\n원래 계획대로 북평을 공격하겠다.')
        stage.unit(101).setAction(0, 2)
        stage.delay(3)
        stage.unit(101).setAction(17, 2)
        stage.say('&101\n공손찬을 없애고 나면\n하북 전체가 내 것이 된다!')
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, -1)
    stage.draw()
    stage.showHead(0, 342, 111)
    stage.showHead(101, 390, 22)
    stage.showHead(161, 471, 0)
    stage.bottomTxt('조조의 답을 받은 원소는,', False, False, False)
    stage.delay(5)
    stage.head(101).move(407, 0)
    stage.delay(5)
    stage.bottomTxt('군사를 일으켜 북평을 공격했다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene2():
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
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n방금 보고를 받았네.\n원소가 북평으로 출병했다는군.\n&22\n우리에게도 좋은 기회입니다.\n&0\n음.')
    stage.unit(0).setAction(17)
    stage.say('&0\n원소가 북평에 눈을 돌린 틈을 타\n우선 외적부터 소탕하자.')
    stage.unit(0).setAction(0)
    stage.say('&0\n가장 먼저 상대할 적은 여포와 장수로군…….\n&0\n공달, 장수 쪽에 무슨 움직임이 있는가?')
    stage.unit(24).setAction(6)
    stage.say('&24\n지금은 별다른 움직임이 없습니다.')
    stage.unit(24).setAction(0)
    stage.say('&0\n음, 우리 공격을 두 번이나 막아 냈지만\n지금은 수세를 취하겠지.\n&0\n내 영토를 공격해 오지는 않을 것이네.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n그렇다면 눈엣가시인 여포를 공격하자.')
    stage.unit(0).setAction(17, 2)
    stage.say('&0\n이번 기회를 놓쳐서는 안 된다.\n반드시 여포를 토벌해야 한다.')
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n문약, 소패의 유비에게 사자를 보내\n우리의 서주 공격에 협력하라고 전하게.')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n예.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(17, 1)
    stage.say('&0\n좋다, 출발하라!')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 6)
    stage.showUnits([
        [
            119,
            69,
            55,
            3],
        [
            126,
            52,
            55,
            1]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('서주  여포 저택')
    stage.delay(3)
    stage.unit(119).setAction(2)
    stage.say('&119\n뭐라고! 조조가 유비와 손잡고 나를 치겠다고!?\n&126\n그렇습니다.')
    stage.unit(119).setAction(0)
    stage.say('&119\n공대, 틀림없는가?\n&126\n틀림없습니다.\n&126\n어제 수상한 사내 하나를 붙잡았는데\n심문해 보니 조조의 사자였습니다.')
    stage.unit(119).setAction(2)
    stage.say('&119\n으으음…… 결코 용서하지 않겠다!\n겉으로는 나와 화해해 놓고\n뒤에서는 나를 노리다니…….\n&126\n이제 두 군대가 손을 잡았으니\n우리에게 매우 불리합니다.')
    stage.unit(119).setAction(0)
    stage.say('&119\n알고 있다!')
    stage.unit(119).move(69, 51, 0)
    stage.say('&119\n조조가 대군을 이끌고 여기 오려면\n아직 시간이 걸리겠지?')
    stage.unit(119).move(69, 55, 2)
    stage.say('&119\n지금 유비의 소패는\n우리 서주와 맞닿아 있네.')
    stage.unit(119).setAction(0, 3)
    stage.delay(3)
    stage.unit(119).setAction(2, 3)
    stage.say('&119\n조조가 도착하기 전에\n소패를 공격해 유비를 없애 버리자!')
    stage.unit(119).setAction(17, 3)
    stage.say('&119\n공대, 장수들에게 출발을 명하라!\n&126\n예.')
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 328, 103)
    stage.showHead(32, 422, 111)
    stage.showHead(119, 510, 138)
    stage.bottomTxt('여포는 유비가 방심한 틈을 타', False, False, False)
    stage.delay(5)
    stage.head(119).move(486, 111)
    stage.delay(5)
    stage.bottomTxt('군사를 일으켜 소패로 쳐들어갔다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('위기에 빠진 유비는 사자를 보내 조조에게 구원을 청했다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene4():
    model.initLocalVar()
    stage.clsUnit()
    stage.bgSound(15)
    stage.delay(3)
    stage.setMenuVisible(False)
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
            10,
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
            193,
            49,
            54,
            2,
            6]])
    stage.draw()
    stage.delay(3)
    stage.info('당시 조조군은 서주로 향하던 중이었다.')
    stage.delay(3)
    stage.setStageName('초현 부근  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n뭐라고!? 여포가 소패를 공격했다고!?\n&193\n그렇습니다.\n&193\n승상께서는 속히 구원병을 보내 주십시오.')
    stage.unit(0).setAction(0)
    stage.say('&0\n알겠다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n원양, 즉시 소패로 가서 유비를 구원하게!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3, 1)
    stage.say('&1\n오오, 명을 받들겠네!')
    stage.unit(1).setAction(0, 1)
    stage.say('&193\n예? 감사합니다.\n돌아가 주군께 끝까지 지키시라 아뢰겠습니다.\n&0\n음.')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(193).move(49, 16, 0)
    stage.unit(193).hide()
    stage.say('&0\n나도 곧 대군을 이끌고 뒤따라가겠다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n출발하라!')
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 11, [
        1])
    gvars[4076] = 0
    gvars[4057] = 13
    stage.setBattlePos([
        [
            1,
            1],
        [
            4,
            16],
        [
            3,
            15],
        [
            2,
            16],
        [
            2,
            14],
        [
            3,
            1],
        [
            1,
            3],
        [
            2,
            2],
        [
            2,
            0],
        [
            0,
            2],
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
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n소패로 가는 길은 둘입니다.\n&20\n그중 하나에는 관문이 있습니다.\n&0\n음.')
    if stage.unitClickTest(22):
        stage.say('&22\n어떻게 소식이 샜는지는 모르겠으나\n여포의 움직임도 참으로 빠릅니다.\n&0\n음, 십중팔구 군사 진궁의 생각이겠지.')
    if stage.unitClickTest(1):
        stage.say('&0\n자네에게 맡기겠네, 원양.\n&1\n오, 걱정 말게.')
    if stage.unitClickTest(10):
        stage.say('&10\n무기점에 새 검과 창이 들어왔다더군.\n&10\n무기는 등급을 좀 더 높인 뒤\n파는 게 좋겠어…….\n&0\n어째서냐?\n&15\n그러면 다른 보상도 받을 수 있거든요.')
    if stage.unitClickTest(15):
        stage.say('&15\n여포는 제게 맡겨 주십시오.\n결코 지지 않겠습니다.')
    if stage.unitClickTest(21):
        stage.say('&21\n상대가 여포인 만큼\n충분히 준비해야 합니다.')
    if stage.unitClickTest(193):
        stage.say('&193\n부디 저희 주군을 구원해 주십시오.')
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
