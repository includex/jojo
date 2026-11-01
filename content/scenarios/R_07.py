# Source Generated with Decompyle++
# File: R_07.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    if vars[706] and vars[1001]:
        stage.loadBg(2, 8)
        stage.jumpScene(17)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            119,
            34,
            54,
            1],
        [
            126,
            50,
            54,
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.setEventName('서찰')
    stage.delay(3)
    stage.setStageName('복양  여포 저택')
    stage.delay(3)
    stage.unit(119).setAction(3)
    stage.say('&119\n공대, 자네 계책은 참으로 묘하군.\n&126\n작은 계책 따위 어찌 자랑하겠습니까.\n주군의 무용이야말로 천하무쌍입니다.')
    stage.unit(119).setAction(0)
    stage.say('&119\n앞으로 어찌하면 좋겠는가?\n조조가 한 번 패했다고 물러설 자는 아니지 않은가.\n&126\n주군, 저도 같은 생각입니다.')
    stage.unit(126).setAction(0, 1)
    stage.say('&126\n조조가 머지않아 다시 쳐들어올 것입니다…….\n&119\n음…… 방심해서는 안 되겠군.')
    stage.unit(126).setAction(0, 3)
    stage.delay(3)
    stage.unit(126).move(48, 54, 3)
    stage.say('&126\n주군, 그렇다면 이렇게 하시면 어떻겠습니까?\n이리하여 저리하면…….\n&119\n좋네, 좋네…….')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    model.unitJoin(18, 0, 0)
    if vars[1013]:
        gvars[0] = 18
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
    return False


def scene2():
    stage.bgSound(-1)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            1,
            38,
            52,
            1],
        [
            16,
            38,
            46,
            1],
        [
            20,
            60,
            52,
            3],
        [
            22,
            60,
            46,
            3],
        [
            21,
            60,
            40,
            3]])
    stage.draw()
    stage.delay(3)
    stage.setStageName('복양 교외  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('&0\n가증스러운 놈! 여포가 이토록 강하다니!')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n여포는 참으로 두려운 적수입니다!')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n허나 그렇다고 반격을 포기할 수는 없습니다.')
    stage.unit(21).setAction(0, 2)
    stage.say('&21\n그렇습니다…….')
    stage.showUnit(181, 49, 16, 3)
    stage.say('&181\n보고드립니다!')
    stage.unit(0).setAction(0)
    stage.unit(22).setAction(0, 3)
    stage.unit(20).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.unit(181).move(49, 52, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 복양에 산다는 자가 뵙기를 청합니다.')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('&0\n지금은 군의 중이다. 나중에 말하라.\n&181\n여포의 일로 찾아왔다고 합니다…….')
    stage.bgSound(12)
    stage.delay(7)
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n뭐라고? ……좋다. 데려오너라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.showUnit(204, 49, 16, 2)
    stage.say('&204\n실례하겠습니다.')
    stage.unit(204).move(49, 52, 2)
    stage.delay(3)
    stage.unit(204).setAction(11)
    stage.say('&0\n여포의 일로 찾아왔다고 들었네.\n&204\n예, 저희 주인께서 보내신 서찰입니다.\n&0\n서찰?')
    stage.unit(1).move(45, 52, 1)
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(204).setAction(11)
    stage.delay(3)
    stage.unit(204).setAction(0, 3)
    stage.delay(3)
    stage.unit(204).setAction(5, 3)
    stage.delay(5)
    stage.unit(204).setAction(0, 3)
    stage.unit(1).move(38, 52, 3)
    stage.delay(1)
    stage.unit(204).setAction(0, 2)
    stage.unit(1).move(38, 67, 1)
    stage.delay(1)
    stage.unit(1).move(45, 67, 1)
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.delay(5)
    stage.unit(0).setAction(5, 3)
    stage.delay(10)
    stage.unit(1).move(38, 67, 0)
    stage.delay(1)
    stage.unit(1).move(38, 52, 1)
    stage.unit(1).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(5, 0)
    stage.say('&0\n오? ‘조조군이 공격하면 성 안에서 내응하겠다’고?\n음…………….')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n어찌하여 그런 위험한 일을 하는가?\n일이 드러나면 여포가 그대들을 죽일 텐데.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n장사꾼이란 본디 이익을 좇는 법,\n그렇지 않은가?')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n설령 이 계책이 성공한다 해도\n그대들이 얻을 이익이 목숨보다 귀하지는 않네.')
    stage.unit(204).setAction(11, 2)
    stage.say('&204\n지금 복양 상인들은 여포가 성 안에 있어\n장사를 전혀 할 수 없습니다.\n&204\n가혹한 세금에 재물을 빼앗는 일까지\n일상처럼 벌어지고 있습니다.\n&204\n조 공께서 복양으로 돌아오지 않으시면\n저희는 살아갈 수 없습니다.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n그랬군…….\n과연 여포라면 저지를 만한 일이야.')
    stage.say('&18\n잠시만 기다려 주십시오.')
    stage.showUnit(18, 49, 16, 2)
    stage.unit(18).move(49, 44, 2)
    stage.say('&0\n오, 자양인가.\n성안까지 정탐하러 다녀오느라 수고했네.\n&18\n주군, 복양성은 백성들이 나가는 것조차 막고\n엄중히 감시하고 있습니다.\n&18\n그런데 이 자는 성을 빠져나와 우리 진영까지 왔으니\n이 서신은 여포가 꾸민 함정일지도 모릅니다…….')
    stage.unit(204).setAction(0)
    stage.delay(3)
    stage.unit(204).setAction(0, 0)
    stage.say('&204\n그, 그것은…………\n저희 주인께서 성문 수비병을 매수하셨기 때문입니다.')
    stage.unit(204).setAction(0, 2)
    stage.say('&204\n저……저는 거짓말하지 않았습니다! 모두……사실입니다!\n&204\n돌아가 봐야 합니다. 늦으면 주인께서 걱정하실 겁니다.\n조 대인, 부디 저희를 믿어 주십시오!')
    stage.unit(204).move(43, 52, 3)
    stage.delay(1)
    stage.unit(204).move(43, 30, 0)
    stage.delay(1)
    stage.unit(204).move(49, 30, 1)
    stage.delay(1)
    stage.unit(204).move(49, 16, 0)
    stage.unit(204).hide()
    stage.unit(18).move(49, 52, 2)
    stage.say('&18\n주군, 아무래도 함정이 아닐까요……?\n&0\n허나 사실이라면\n천재일우의 기회일세.\n&0\n지금은 하루빨리 복양을 되찾아야 하네.\n자양, 자네도 참전하게.')
    stage.unit(18).setAction(6)
    stage.say('&18\n예.')
    stage.bgSound(15)
    stage.delay(3)
    stage.unit(18).setAction(0)
    stage.delay(3)
    stage.unit(18).move(49, 40, 0)
    stage.delay(1)
    stage.unit(18).move(38, 40, 1)
    stage.unit(18).setAction(0)
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 11, [
        16,
        18])
    gvars[4076] = 0
    gvars[4057] = 7
    stage.setBattlePos([
        [
            28,
            13],
        [
            28,
            14],
        [
            30,
            13],
        [
            30,
            14],
        [
            28,
            12],
        [
            30,
            12],
        [
            29,
            13],
        [
            29,
            15],
        [
            28,
            15],
        [
            29,
            12],
        [
            30,
            15],
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
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(18):
        stage.say('&18\n주군, 역시 그 사람은 믿을 수 없습니다.')
    if stage.unitClickTest(20):
        stage.say('&20\n복양성 전체가 전장이 됩니다.\n우리는 성 동쪽에서 공격을 시작합니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n이번에는 반드시 여포를 없애겠다!')
    if stage.unitClickTest(22):
        stage.say('&22\n자양 선생의 포차대는\n사거리가 가장 길지요. 이번에는 선생만 믿겠습니다.\n&18\n하지만 그만큼 이동이 불편합니다…….')
    if stage.unitClickTest(16):
        stage.say('&16\n무기와 방어구, 부대에 모두 경험치가 있지?\n레벨이 오르는 방식도 서로 다른가?\n&20\n하하하, 전위 장군.\n무기는 공격할 때 경험치가 오르고\n방어구는 공격을 받을 때 경험치가 오릅니다.\n&16\n오, 그렇군.\n그럼 부대 경험치도 공격할 때 오르겠구나?\n&20\n예. 특히 자기보다 레벨이 높은 적을 퇴각시키면\n더 많이 오릅니다.')
    if stage.unitClickTest(21):
        stage.say('&21\n여포의 용맹은 참으로 놀랍습니다.\n귀신이 내려온 듯하군요.')
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
