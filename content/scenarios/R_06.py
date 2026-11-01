# Source Generated with Decompyle++
# File: R_06.marshal (Python 3.9)


def scene0():
    model.unitJoin(14, 0, 0)
    if vars[1013]:
        gvars[0] = 14
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
    if vars[1002]:
        stage.infoTransfer(18, '')
    return False


def scene1():
    model.initLocalVar()
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.setMenuVisible(False)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 482, 125)
    stage.showHead(145, 546, 125)
    stage.bottomTxt('조조가 서주의 도겸과 싸우고 있을 때,', False, False, False)
    stage.delay(5)
    stage.showHead(119, 369, 104)
    stage.delay(5)
    stage.bottomTxt('여포는 조조군의 후방이 빈틈을 보인 틈에,', True, True, False)
    stage.delay(5)
    stage.head(119).move(408, 76)
    stage.delay(5)
    stage.bottomTxt('조조의 근거지 복양을 빼앗았다.', True, False, True)
    stage.bottomTxt('이에 조조는 서주를 공격하던 부대를 거두어들여,', False, False, False)
    stage.delay(5)
    stage.head(0).move(472, 76)
    stage.delay(5)
    stage.bottomTxt('돌아가 복양을 되찾기 위한 싸움을 벌였다.', True, False, True)
    stage.bgSound(-1)
    stage.delay(3)
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
    stage.setEventName('복양을 되찾아라!')
    stage.setStageName('복양 교외  조조군 본진')
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n괘씸한 여포 놈!\n감히 이 틈에 내 복양을 공격하다니!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n허나 참으로 믿기 어렵군요……\n여포에게 이런 지모가 있다니.')
    stage.unit(21).setAction(0, 2)
    stage.say('&21\n분명 누군가가 계책을 일러 주었을 것입니다.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n복양은 우리의 근거지입니다.\n즉시 되찾아야 합니다.')
    stage.unit(0).setAction(2)
    stage.say('&0\n그 정도는 나도 알고 있다!\n빌어먹을 여포, 반드시 네놈을 쓰러뜨리겠다!')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.bgSound(18)
    stage.delay(3)
    stage.showUnit(181, 49, 16, 2)
    stage.say('&181\n보고드립니다.')
    stage.unit(22).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 우금이라는 장사가 의탁하러 왔습니다.\n&0\n우금? 처음 듣는 이름이군.\n&181\n무예가 상당히 비범해 보입니다.\n&0\n음……흥미롭군. 데려오너라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.delay(5)
    stage.showUnit(14, 49, 16, 2)
    stage.say('&14\n실례했습니다.')
    stage.delay(5)
    stage.unit(14).move(49, 52, 2)
    stage.delay(3)
    stage.unit(14).setAction(11)
    stage.say('&14\n처음 뵙겠습니다. 성은 우, 이름은 금, 자는 문칙입니다.\n&0\n음, 나를 위해 일하고 싶다고 들었네.\n&14\n그렇습니다. 복양의 백성은 모두 주군께서 여포를 무찌르고\n복양으로 돌아오시기를 기다리고 있습니다.\n&14\n여포를 무찌르는 데 힘을 보태게 해 주십시오.')
    stage.unit(0).setAction(3)
    stage.say('&0\n음…… 좋다. 훌륭한 활약을 기대하겠네.')
    stage.unit(0).setAction(0)
    stage.say('&14\n감사합니다, 주군.')
    stage.bgSound(15)
    stage.delay(3)
    stage.unit(14).setAction(0)
    stage.delay(3)
    stage.unit(14).move(49, 40, 0)
    stage.delay(1)
    stage.unit(14).move(38, 40, 1)
    stage.unit(14).setAction(0)
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        16,
        14])
    gvars[4076] = 0
    gvars[4057] = 6
    stage.setBattlePos([
        [
            3,
            1],
        [
            4,
            1],
        [
            3,
            2],
        [
            4,
            2],
        [
            5,
            1],
        [
            3,
            3],
        [
            4,
            0],
        [
            2,
            2],
        [
            3,
            0],
        [
            2,
            1],
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
        stage.say('&0\n출병한다.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n여포 그놈은 참으로 못된 놈이야.')
    if stage.unitClickTest(22):
        stage.say('&22\n전쟁은 공격만 한다고 되는 것이 아닙니다.\n때로는 전진하기 위해 물러나는 것도 중요하지요.')
    if stage.unitClickTest(21):
        stage.say('&21\n일반 무기가 세 단계로 진화하는 것과 달리\n특수 장비는 아홉 단계로 나뉩니다.\n&21\n도겸에게서 얻은 백은갑도 그런 장비입니다.\n&0\n특수한 능력도 있지.\n&21\n그렇습니다. 다만 그 때문에\n기본 능력은 조금 낮습니다.')
    if stage.unitClickTest(14):
        stage.say('&14\n원거리 공격은 제게 맡겨 주십시오.\n특히 기병을 쓰러뜨리는 데 자신이 있습니다.\n다만 기동력은 기병이 더 뛰어나지요.')
    if stage.unitClickTest(16):
        stage.say('&16\n우금은 궁병이라지?\n그럼 궁기병과 공격력이 같지 않은가?\n&20\n하하하, 전위 장군. 궁병은 사거리가 더 길고\n치명타를 가하기도 쉽습니다.\n&16\n그런가? 나는 잘 모르겠군…….\n어쨌든 이번에는 그를 믿어 보세. 하하하하!!')
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 주 전장의 상황은 어떠한가?\n&20\n전장은 복양성의 서북쪽이며\n북문과 서문, 두 성문이 있습니다.\n&20\n복양은 수비하기 좋은 견고한 성이니\n힘겨운 싸움이 될 것입니다.\n&0\n으음…… 가증스러운 여포 놈!')
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
