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
    stage.say('@dialogue:R_07.scene0.dialogue0001')
    stage.unit(119).setAction(0)
    stage.say('@dialogue:R_07.scene0.dialogue0002')
    stage.unit(126).setAction(0, 1)
    stage.say('@dialogue:R_07.scene0.dialogue0003')
    stage.unit(126).setAction(0, 3)
    stage.delay(3)
    stage.unit(126).move(48, 54, 3)
    stage.say('@dialogue:R_07.scene0.dialogue0004')
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
    stage.say('@dialogue:R_07.scene2.dialogue0001')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0002')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0003')
    stage.unit(21).setAction(0, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0004')
    stage.showUnit(181, 49, 16, 3)
    stage.say('@dialogue:R_07.scene2.dialogue0005')
    stage.unit(0).setAction(0)
    stage.unit(22).setAction(0, 3)
    stage.unit(20).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.unit(181).move(49, 52, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_07.scene2.dialogue0006')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_07.scene2.dialogue0007')
    stage.bgSound(12)
    stage.delay(7)
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_07.scene2.dialogue0008')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.showUnit(204, 49, 16, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0009')
    stage.unit(204).move(49, 52, 2)
    stage.delay(3)
    stage.unit(204).setAction(11)
    stage.say('@dialogue:R_07.scene2.dialogue0010')
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
    stage.say('@dialogue:R_07.scene2.dialogue0011')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_07.scene2.dialogue0012')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0013')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_07.scene2.dialogue0014')
    stage.unit(204).setAction(11, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0015')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_07.scene2.dialogue0016')
    stage.say('@dialogue:R_07.scene2.dialogue0017')
    stage.showUnit(18, 49, 16, 2)
    stage.unit(18).move(49, 44, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0018')
    stage.unit(204).setAction(0)
    stage.delay(3)
    stage.unit(204).setAction(0, 0)
    stage.say('@dialogue:R_07.scene2.dialogue0019')
    stage.unit(204).setAction(0, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0020')
    stage.unit(204).move(43, 52, 3)
    stage.delay(1)
    stage.unit(204).move(43, 30, 0)
    stage.delay(1)
    stage.unit(204).move(49, 30, 1)
    stage.delay(1)
    stage.unit(204).move(49, 16, 0)
    stage.unit(204).hide()
    stage.unit(18).move(49, 52, 2)
    stage.say('@dialogue:R_07.scene2.dialogue0021')
    stage.unit(18).setAction(6)
    stage.say('@dialogue:R_07.scene2.dialogue0022')
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
        stage.say('@dialogue:R_07.scene3.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(18):
        stage.say('@dialogue:R_07.scene3.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_07.scene3.dialogue0003')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_07.scene3.dialogue0004')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_07.scene3.dialogue0005')
    if stage.unitClickTest(16):
        stage.say('@dialogue:R_07.scene3.dialogue0006')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_07.scene3.dialogue0007')
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
