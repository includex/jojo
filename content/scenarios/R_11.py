# Source Generated with Decompyle++
# File: R_11.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    if vars[710]:
        stage.bgSound(13)
        stage.delay(3)
        stage.loadBg(2, 35)
        stage.showUnits([
            [
                0,
                59,
                66],
            [
                181,
                69,
                58,
                3,
                11],
            [
                182,
                69,
                66,
                3,
                11]])
        stage.draw()
        stage.setEventName('거짓 황제 원술')
        stage.setStageName('완성  강가')
        stage.info('한때의 방심으로\n대장 전위를 잃은 조조는\n이후 완성 근처 강가에\n무덤을 세워 전위의 영령을 기렸다.')
        stage.delay(5)
        stage.say('&0\n전위…….\n&0\n내가 방심한 탓에\n네가 목숨을 잃고 말았구나…….')
        stage.delay(5)
        stage.unit(0).setAction(0, 3)
        stage.delay(5)
        stage.say('&0\n그대를 잃은 것이\n내 아들 조앙을 잃은 것보다 더 가슴 아프구나.')
        stage.delay(5)
        stage.say('&182\n주군,\n전위 장군의 유품을 정리하다가\n이 옷을 발견했습니다.')
        stage.delay(5)
        stage.unit(0).setAction(0, 1)
        stage.delay(5)
        stage.unit(0).move(65, 66, 1)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(5)
        stage.delay(5)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).move(59, 66, 3)
        stage.delay(10)
        stage.unit(0).setAction(5)
        stage.say('&0\n이것은…….')
        model.unitJoin(16, 255, 0)
        stage.getItem(90, 0)
        stage.delay(10)
        stage.say('&0\n전위의 유품이었구나…….')
        stage.say('&181\n흑…… 전위 장군!!\n&182\n반드시 이 원수를 갚겠습니다!!')
        stage.unit(0).setAction(0)
        stage.delay(8)
        stage.say('&0\n……。')
        stage.clsUnit()
        stage.bgSound(-1)
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(143, 253, 147)
    stage.showHead(0, 317, 147)
    stage.bottomTxt('장수에게 큰 타격을 입은 조조는,', False, False, False)
    stage.delay(5)
    stage.head(0).move(338, 106)
    stage.delay(5)
    stage.bottomTxt('허도로 돌아가 전열을 정비했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('어느 날,', True, True, False)
    stage.delay(8)
    stage.showHead(136, 427, 178)
    stage.delay(5)
    stage.bottomTxt('수춘의 원술이 갑자기 제위에 올라 황제를 칭했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('머지않아 조조에게도 소식이 전해졌다.', True, True, True)
    stage.bgSound(-1)
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
    stage.effectSound(201, 0)
    stage.delay(3)
    if not vars[710]:
        stage.setEventName('거짓 황제 원술')
    stage.setStageName('허도  조조 저택')
    stage.unit(22).setAction(6, 2)
    stage.say('&22\n원술이 멋대로 천자를 자처했으니\n쉽게 용서할 수 없습니다.\n즉시 출병하여 토벌해야 합니다!')
    stage.unit(22).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6, 2)
    stage.say('&21\n그렇습니다.\n그렇지 않으면 너도나도 황제를 참칭할 것입니다.')
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(6, 2)
    stage.say('&24\n하지만 지난번 장수와 싸우며\n이미 큰 타격을 입었습니다.')
    stage.say('&24\n원술은 보잘것없는 어리석은 자이나\n군사력만큼은 얕보아서는 안 됩니다.')
    stage.unit(21).setAction(0, 3)
    stage.say('&21\n그때 입을 손실은\n예상보다 훨씬 클 것입니다!')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(0, 1)
    stage.say('&24\n지금 원술과 힘으로 맞서는 것은 무모한 일입니다.')
    stage.say('&21\n어찌하여 그리 말하는가?')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n우리만 원술을 공격할 필요는 없습니다.\n&20\n유비, 여포, 손책도 움직이게 하시지요.')
    stage.unit(24).setAction(0, 3)
    stage.say('&24\n예?')
    stage.say('&21\n으음…….')
    stage.unit(22).setAction(0, 3)
    stage.say('&22\n그렇군요…….')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n격문을 보내고 네 부대가 함께 포위 공격하면\n피해를 크게 줄일 수 있습니다.')
    stage.unit(22).setAction(0, 3)
    stage.say('&22\n주군, 이 방법이라면 통할 것입니다.')
    stage.unit(0).setAction(3, 1)
    stage.say('&0\n좋다. 즉시 사자를 보내라.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n세 부대의 참전이 확정되면\n원술의 본거지 수춘을 공격한다!')
    stage.unit(0).setAction(2, 1)
    stage.say('&0\n황제를 참칭한 원술의 말로가 바로 이것이다.')
    stage.effectSound(201, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 332, 98)
    stage.showHead(136, 428, 171)
    stage.bottomTxt('유비,', False, False, False)
    stage.delay(5)
    stage.showHead(32, 396, 80)
    stage.bottomTxt('여포와', True, False, False)
    stage.delay(5)
    stage.showHead(119, 517, 125)
    stage.bottomTxt('손책', True, False, False)
    stage.delay(5)
    stage.showHead(100, 567, 211)
    stage.bottomTxt('조조에게 공격해 달라는 요청을 받고', True, False, False)
    stage.delay(5)
    stage.bottomTxt('즉시 원술을 공격할 군사를 내겠다고 약속했고,', True, False, False)
    stage.delay(5)
    stage.head(32).move(428, 91)
    stage.head(0).move(364, 171)
    stage.head(100).move(567, 221)
    stage.head(100).move(492, 221)
    stage.head(119).move(492, 141)
    stage.bottomTxt('각각 병사를 이끌고 원술의 본거지 수춘으로 쳐들어갔다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene2():
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
            13,
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
    stage.info('당시 수춘에는 큰비가 끊이지 않아\n홍수로 막대한 피해가 발생했다.')
    stage.setStageName('수춘 교외  조조군 본진')
    stage.say('&0\n문약, 다른 세 부대의 배치는 어떠한가?')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n주군께 아뢰옵니다.')
    stage.say('&20\n유비는 동문, 여포는 서문,\n손책은 남문에 각각 진을 쳤습니다.')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n하지만 주군…….\n&0\n음? 무슨 일인가?\n&20\n실은 이렇습니다.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n아시다시피 최근 수춘 일대에 큰비가 이어져\n수해로 온 땅이 진창이 되었습니다.\n&20\n따라서 부대 이동이\n몹시 어려울 것입니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음.\n허나 지금은 그런 나약한 소리를 할 때가 아니다.')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(17, 0)
    stage.say('&0\n공격을 개시하라.\n&20\n예.')
    stage.unit(0).setAction(0, 0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 12, [
        22])
    gvars[4076] = 0
    gvars[4057] = 11
    stage.setBattlePos([
        [
            12,
            0],
        [
            13,
            0],
        [
            13,
            2],
        [
            12,
            2],
        [
            11,
            2],
        [
            12,
            1],
        [
            14,
            1],
        [
            10,
            1],
        [
            13,
            1],
        [
            11,
            1],
        [
            10,
            0],
        [
            14,
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
    if stage.unitClickTest(20):
        stage.say('&20\n수춘의 수해는 예상보다 심각합니다.\n&20\n전장이 거의 늪으로 변했습니다.\n&0\n음! 부대 이동이 몹시 어렵겠군.')
    if stage.unitClickTest(22):
        stage.say('&22\n얼마 전 손책을 처음 보았습니다.\n과연 제왕의 상을 지녔더군요. 하지만…….\n&0\n하지만 무엇인가?\n&22\n아무래도 오래 살기는 어려울 듯합니다.\n&0\n오…….')
    if stage.unitClickTest(15):
        stage.say('&15\n논밭이 잠겼으니 농민들이 무척 힘들겠지요?\n어떻게든 도와줘야겠습니다.')
    if stage.unitClickTest(13):
        stage.say('&13\n원술 같은 화근을 세상에 남겨 둘 수는 없다.')
    if stage.unitClickTest(24):
        stage.say('&24\n유비라면 모르겠으나\n여포와 손책은 움직이지 않을 듯합니다.\n&0\n나도 그리 생각하네.')
    if stage.unitClickTest(1):
        stage.say('&1\n맹덕, 서두르지 않으면\n원술의 수급을 유비에게 빼앗기겠네.')
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
