# Source Generated with Decompyle++
# File: R_22.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
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
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('내분을 기다린다')
    stage.setStageName('여양  조조군 본진')
    stage.say('@dialogue:R_22.scene0.dialogue0001')
    stage.unit(21).setAction(0, 2)
    stage.say('@dialogue:R_22.scene0.dialogue0002')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_22.scene0.dialogue0003')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_22.scene0.dialogue0004')
    stage.unit(1).setAction(3, 1)
    stage.say('@dialogue:R_22.scene0.dialogue0005')
    stage.unit(1).setAction(0, 1)
    stage.delay(3)
    stage.unit(22).setAction(0, 3)
    stage.say('@dialogue:R_22.scene0.dialogue0006')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_22.scene0.dialogue0007')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_22.scene0.dialogue0008')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_22.scene0.dialogue0009')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 67)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.info('그 무렵 원담과 곽도 두 사람은……')
    stage.setStageName('여양  교외 산길')
    stage.showUnits([
        [
            113,
            44,
            98,
            1],
        [
            103,
            54,
            98]])
    stage.unitsMove([
        [
            stage.unit(113),
            44,
            30,
            0],
        [
            stage.unit(103),
            54,
            30,
            0]])
    stage.delay(3)
    stage.unit(113).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(2)
    stage.say('@dialogue:R_22.scene1.dialogue0001')
    stage.unit(103).setAction(0, 3)
    stage.say('@dialogue:R_22.scene1.dialogue0002')
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_22.scene1.dialogue0003')
    stage.unit(113).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(0, 1)
    stage.say('@dialogue:R_22.scene1.dialogue0004')
    stage.unit(113).setAction(2)
    stage.say('@dialogue:R_22.scene1.dialogue0005')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:R_22.scene1.dialogue0006')
    stage.unit(103).setAction(0)
    stage.say('@dialogue:R_22.scene1.dialogue0007')
    stage.unit(113).setAction(2)
    stage.say('@dialogue:R_22.scene1.dialogue0008')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(0, 0)
    stage.say('@dialogue:R_22.scene1.dialogue0009')
    stage.unit(113).setAction(3)
    stage.say('@dialogue:R_22.scene1.dialogue0010')
    stage.unit(113).setAction(0)
    stage.unitsMove([
        [
            stage.unit(113),
            47,
            5,
            0],
        [
            stage.unit(103),
            57,
            5,
            0]])
    stage.unit(113).hide()
    stage.unit(103).hide()
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 289, 83)
    stage.showHead(115, 353, 30)
    stage.delay(8)
    stage.showHead(113, 353, 110)
    stage.delay(5)
    stage.bottomTxt('여양 전투 이후 원담은', False, False, False)
    stage.delay(8)
    stage.bottomTxt('원상이 있는 업성으로 돌아가지 않고', True, False, False)
    stage.delay(5)
    stage.head(113).move(459, 110)
    stage.head(113).move(459, 21)
    stage.delay(5)
    stage.bottomTxt('도리어 남피로 가서 주둔했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 42)
    stage.showUnits([
        [
            113,
            54,
            70],
        [
            103,
            35,
            56,
            1],
        [
            112,
            71,
            50,
            2],
        [
            117,
            71,
            56,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('남피  원담의 저택')
    stage.delay(3)
    stage.unit(113).setAction(3)
    stage.say('@dialogue:R_22.scene2.dialogue0001')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:R_22.scene2.dialogue0002')
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_22.scene2.dialogue0003')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(112).setAction(6)
    stage.say('@dialogue:R_22.scene2.dialogue0004')
    stage.unit(112).setAction(0)
    stage.delay(3)
    stage.unit(117).setAction(6)
    stage.say('@dialogue:R_22.scene2.dialogue0005')
    stage.unit(117).setAction(0)
    stage.say('@dialogue:R_22.scene2.dialogue0006')
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_22.scene2.dialogue0007')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(3)
    stage.say('@dialogue:R_22.scene2.dialogue0008')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(117).setAction(6)
    stage.say('@dialogue:R_22.scene2.dialogue0009')
    stage.say('@dialogue:R_22.scene2.dialogue0010')
    stage.unit(117).setAction(0)
    stage.delay(3)
    stage.unit(112).setAction(0, -1)
    stage.say('@dialogue:R_22.scene2.dialogue0011')
    stage.unit(113).setAction(3)
    stage.say('@dialogue:R_22.scene2.dialogue0012')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene3():
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
            2],
        [
            117,
            48,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_22.scene3.dialogue0001')
    stage.unit(117).setAction(0)
    stage.say('@dialogue:R_22.scene3.dialogue0002')
    model.setFace(1)
    stage.say('@dialogue:R_22.scene3.dialogue0003')
    stage.unit(0).setAction(3)
    model.setFace(0)
    stage.say('@dialogue:R_22.scene3.dialogue0004')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_22.scene3.dialogue0005')
    stage.say('@dialogue:R_22.scene3.dialogue0006')
    stage.say('@dialogue:R_22.scene3.dialogue0007')
    model.setFace(3)
    stage.unit(0).setAction(3, 1)
    stage.say('@dialogue:R_22.scene3.dialogue0008')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_22.scene3.dialogue0009')
    model.setFace(0)
    stage.say('@dialogue:R_22.scene3.dialogue0010')
    stage.unit(117).setAction(6)
    stage.say('@dialogue:R_22.scene3.dialogue0011')
    stage.unit(117).setAction(0)
    stage.say('@dialogue:R_22.scene3.dialogue0012')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_22.scene3.dialogue0013')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(117).setAction(0, 0)
    stage.say('@dialogue:R_22.scene3.dialogue0014')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_22.scene3.dialogue0015')
    stage.unit(117).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_22.scene3.dialogue0016')
    stage.effectSound(200, 255)
    stage.bgSound(18)
    stage.delay(3)
    stage.showUnit(8, 95, 55, 3)
    stage.say('@dialogue:R_22.scene3.dialogue0017')
    stage.unit(117).setAction(0, 1)
    stage.unit(117).move(48, 60, 2)
    stage.delay(3)
    stage.unit(117).setAction(0, 0)
    stage.unit(8).move(48, 55, 3)
    stage.unit(8).setAction(0)
    stage.say('@dialogue:R_22.scene3.dialogue0018')
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('참전을 허락한다\n아직 이르다', 0)
    if sel == 1:
        stage.addAmbition(-2)
        stage.bgSound(18)
        stage.delay(3)
        stage.say('@dialogue:R_22.scene3.dialogue0019')
    elif sel == 2:
        stage.addAmbition(2)
        stage.bgSound(18)
        stage.delay(3)
        stage.say('@dialogue:R_22.scene3.dialogue0020')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_22.scene3.dialogue0021')
    model.setFace(0)
    model.unitJoin(8, 0, 0)
    if vars[1013]:
        gvars[0] = 8
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
    stage.setJoinEquip(8, -1, 0, -1, 0, 21)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 315, 129)
    stage.showHead(115, 379, 28)
    stage.delay(8)
    stage.head(0).move(315, 28)
    stage.delay(5)
    stage.bottomTxt('조조군은 원상의 본거지 업성을 공격했다.', False, False, True)
    stage.bgSound(-1)
    return False


def scene4():
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
            118,
            53,
            48]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('업성@원상 저택')
    stage.delay(3)
    stage.unit(115).setAction(2)
    stage.say('@dialogue:R_22.scene4.dialogue0001')
    stage.unit(115).setAction(0)
    stage.say('@dialogue:R_22.scene4.dialogue0002')
    stage.unit(118).setAction(0, 3)
    stage.say('@dialogue:R_22.scene4.dialogue0003')
    stage.say('@dialogue:R_22.scene4.dialogue0004')
    stage.unit(118).setAction(0, 0)
    stage.delay(3)
    stage.unit(115).setAction(2)
    stage.say('@dialogue:R_22.scene4.dialogue0005')
    stage.unit(106).setAction(6)
    stage.say('@dialogue:R_22.scene4.dialogue0006')
    stage.unit(115).setAction(0)
    stage.say('@dialogue:R_22.scene4.dialogue0007')
    stage.unit(106).setAction(0)
    stage.say('@dialogue:R_22.scene4.dialogue0008')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene5():
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
            8,
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
    stage.setStageName('업성 교외  조조군 본진')
    stage.say('@dialogue:R_22.scene5.dialogue0001')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_22.scene5.dialogue0002')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_22.scene5.dialogue0003')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_22.scene5.dialogue0004')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_22.scene5.dialogue0005')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_22.scene5.dialogue0006')
    stage.unit(8).setAction(0, 2)
    stage.say('@dialogue:R_22.scene5.dialogue0007')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_22.scene5.dialogue0008')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_22.scene5.dialogue0009')
    stage.unit(8).setAction(0, 1)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 11, [
        22,
        8])
    gvars[4076] = 0
    gvars[4057] = 22
    stage.setBattlePos([
        [
            17,
            18],
        [
            18,
            18],
        [
            17,
            16],
        [
            15,
            16],
        [
            19,
            16],
        [
            14,
            17],
        [
            20,
            17],
        [
            16,
            17],
        [
            18,
            17],
        [
            15,
            19],
        [
            19,
            19],
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


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_22.scene6.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_22.scene6.dialogue0002')
    if stage.unitClickTest(8):
        stage.say('@dialogue:R_22.scene6.dialogue0003')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_22.scene6.dialogue0004')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_22.scene6.dialogue0005')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_22.scene6.dialogue0006')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_22.scene6.dialogue0007')
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
