# Source Generated with Decompyle++
# File: R_10.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            149,
            49,
            67],
        [
            1,
            38,
            54,
            2,
            6],
        [
            16,
            38,
            48,
            2,
            6],
        [
            15,
            38,
            42,
            2,
            6],
        [
            20,
            60,
            54,
            2,
            6],
        [
            22,
            60,
            48,
            2,
            6],
        [
            21,
            60,
            42,
            2,
            6],
        [
            0,
            49,
            54,
            2,
            6]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('호가의 선율')
    stage.setStageName('장안 근교  조조군 본진')
    stage.say('@dialogue:R_10.scene0.dialogue0001')
    stage.unit(149).move(56, 67, 1)
    stage.delay(1)
    stage.unit(149).move(56, 24, 0)
    stage.delay(1)
    stage.unit(149).move(49, 24, 3)
    stage.delay(1)
    stage.unit(149).move(49, 16, 0)
    stage.unit(149).hide()
    stage.unit(0).setAction(0, 2)
    stage.unit(1).setAction(0, 1)
    stage.unit(16).setAction(0, 1)
    stage.unit(15).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(22).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).move(56, 54, 1)
    stage.delay(1)
    stage.unit(0).move(56, 67, 2)
    stage.delay(1)
    stage.unit(0).move(49, 67, 3)
    stage.delay(1)
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_10.scene0.dialogue0002')
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_10.scene0.dialogue0003')
    stage.say('@dialogue:R_10.scene0.dialogue0004')
    stage.unit(0).setAction(3, 0)
    stage.say('@dialogue:R_10.scene0.dialogue0005')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_10.scene0.dialogue0006')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_10.scene0.dialogue0007')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene0.dialogue0008')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_10.scene0.dialogue0009')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_10.scene0.dialogue0010')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_10.scene0.dialogue0011')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_10.scene0.dialogue0012')
    stage.delay(3)
    stage.showUnit(25, 49, 16, 2)
    stage.say('@dialogue:R_10.scene0.dialogue0013')
    stage.unit(1).setAction(0, 1)
    stage.unit(25).move(49, 54, 2)
    stage.delay(3)
    stage.unit(25).setAction(6)
    stage.say('@dialogue:R_10.scene0.dialogue0014')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_10.scene0.dialogue0015')
    stage.unit(25).setAction(0)
    stage.delay(3)
    stage.unit(25).move(49, 16, 0)
    stage.unit(25).hide()
    stage.delay(3)
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_10.scene0.dialogue0016')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 18)
    stage.showUnits([
        [
            13,
            35,
            55,
            1],
        [
            25,
            48,
            55,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('그날 밤…….', 3)
    stage.setStageName('장안 근교  서황군 본진')
    stage.say('@dialogue:R_10.scene1.dialogue0001')
    stage.unit(25).setAction(0, 2)
    stage.say('@dialogue:R_10.scene1.dialogue0002')
    stage.unit(25).setAction(0, 3)
    stage.say('@dialogue:R_10.scene1.dialogue0003')
    stage.unit(13).setAction(0, 2)
    stage.delay(3)
    stage.unit(13).move(35, 59, 2)
    stage.say('@dialogue:R_10.scene1.dialogue0004')
    stage.unit(25).move(48, 59, 2)
    stage.delay(3)
    stage.unit(25).setAction(0, 3)
    stage.say('@dialogue:R_10.scene1.dialogue0005')
    stage.unit(25).setAction(0, 2)
    stage.say('@dialogue:R_10.scene1.dialogue0006')
    stage.unit(25).setAction(0, 3)
    stage.say('@dialogue:R_10.scene1.dialogue0007')
    stage.unit(25).setAction(0, 1)
    stage.delay(3)
    stage.unit(25).move(52, 59, 1)
    stage.say('@dialogue:R_10.scene1.dialogue0008')
    stage.unit(25).setAction(0, 3)
    stage.say('@dialogue:R_10.scene1.dialogue0009')
    stage.unit(13).setAction(0, 1)
    stage.say('@dialogue:R_10.scene1.dialogue0010')
    stage.unit(25).move(48, 59, 3)
    stage.say('@dialogue:R_10.scene1.dialogue0011')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(18)
    stage.delay(3)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
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
            25,
            45,
            54,
            2],
        [
            13,
            53,
            54,
            2,
            6]])
    stage.draw()
    stage.setStageName('장안 근교  조조군 본진')
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_10.scene2.dialogue0001')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_10.scene2.dialogue0002')
    stage.unit(25).setAction(6)
    stage.say('@dialogue:R_10.scene2.dialogue0003')
    stage.say('@dialogue:R_10.scene2.dialogue0004')
    stage.unit(25).setAction(0)
    stage.unit(13).setAction(0)
    stage.delay(3)
    stage.unit(13).move(49, 54, 3)
    stage.delay(1)
    stage.unit(13).move(49, 50, 0)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(13),
            49,
            46,
            0],
        [
            stage.unit(25),
            49,
            54,
            1]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(13),
            49,
            16,
            0],
        [
            stage.unit(25),
            49,
            24,
            0]])
    stage.unit(13).hide()
    stage.unit(25).move(49, 16, 0)
    stage.unit(25).hide()
    stage.bgSound(-1)
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene2.dialogue0005')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_10.scene2.dialogue0006')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_10.scene2.dialogue0007')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_10.scene2.dialogue0008')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene2.dialogue0009')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_10.scene2.dialogue0010')
    stage.say('@dialogue:R_10.scene2.dialogue0011')
    stage.unit(22).setAction(6, 2)
    stage.say('@dialogue:R_10.scene2.dialogue0012')
    stage.say('@dialogue:R_10.scene2.dialogue0013')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_10.scene2.dialogue0014')
    stage.unit(0).setAction(17, 0)
    stage.say('@dialogue:R_10.scene2.dialogue0015')
    model.unitJoin(13, 0, 0)
    model.unitJoin(25, 0, 0)
    if vars[1013]:
        gvars[0] = 13
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
        gvars[0] = 25
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
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(149, 173, 76)
    stage.showHead(0, 237, 76)
    stage.bottomTxt('헌제는 조조의 천도 주청을 받아들였고', False, False, False)
    stage.delay(8)
    stage.head(0).move(397, 111)
    stage.head(149).move(333, 111)
    stage.bottomTxt('한의 도읍은 조조가 다스리던 허창으로 옮겨졌고\n이때부터 허창은 허도라 불리게 되었다.', True, True, False)
    stage.delay(10)
    stage.bottomTxt('조조는 이로써 천자를 모셔 오는 대업을 이루었다.', True, True, True)
    stage.bgSound(-1)
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
            20,
            49,
            40,
            2],
        [
            22,
            57,
            40,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_10.scene3.dialogue0001')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_10.scene3.dialogue0002')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_10.scene3.dialogue0003')
    stage.say('@dialogue:R_10.scene3.dialogue0004')
    stage.unit(0).move(34, 59, 2)
    stage.say('@dialogue:R_10.scene3.dialogue0005')
    stage.say('@dialogue:R_10.scene3.dialogue0006')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(3, 1)
    stage.say('@dialogue:R_10.scene3.dialogue0007')
    model.setFace(2)
    stage.unit(0).setAction(2, 1)
    stage.say('@dialogue:R_10.scene3.dialogue0008')
    stage.say('@dialogue:R_10.scene3.dialogue0009')
    stage.say('@dialogue:R_10.scene3.dialogue0010')
    stage.say('@dialogue:R_10.scene3.dialogue0011')
    model.setFace(0)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene3.dialogue0012')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_10.scene3.dialogue0013')
    stage.say('@dialogue:R_10.scene3.dialogue0014')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene3.dialogue0015')
    stage.say('@dialogue:R_10.scene3.dialogue0016')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_10.scene3.dialogue0017')
    stage.say('@dialogue:R_10.scene3.dialogue0018')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene3.dialogue0019')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 336, 106)
    stage.showHead(32, 486, 132)
    stage.showHead(119, 550, 132)
    stage.bottomTxt('순욱의 말대로 얼마 지나지 않아 유비와 여포는 원수가 되었고,', False, False, False)
    stage.delay(8)
    stage.head(32).move(400, 106)
    stage.head(119).move(492, 132)
    stage.delay(5)
    stage.bottomTxt('여포에게 서주에서 쫓겨난 유비는 조조에게 의탁하러 왔다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            1,
            49,
            70],
        [
            32,
            49,
            55,
            3,
            6],
        [
            33,
            59,
            52,
            3],
        [
            3,
            59,
            58,
            3],
        [
            20,
            49,
            40,
            2],
        [
            22,
            55,
            40,
            2],
        [
            24,
            61,
            40,
            2],
        [
            21,
            67,
            40,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_10.scene4.dialogue0001')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_10.scene4.dialogue0002')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_10.scene4.dialogue0003')
    stage.unit(0).move(34, 59, 2)
    stage.say('@dialogue:R_10.scene4.dialogue0004')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene4.dialogue0005')
    stage.say('@dialogue:R_10.scene4.dialogue0006')
    stage.delay(3)
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_10.scene4.dialogue0007')
    stage.say('@dialogue:R_10.scene4.dialogue0008')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_10.scene4.dialogue0009')
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_10.scene4.dialogue0010')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(33),
            95,
            52,
            1],
        [
            stage.unit(3),
            95,
            58,
            1],
        [
            stage.unit(32),
            85,
            55,
            1]])
    stage.unit(33).hide()
    stage.unit(3).hide()
    stage.unit(32).move(95, 55, 1)
    stage.unit(32).hide()
    stage.delay(3)
    stage.unit(1).setAction(0, 3)
    stage.say('@dialogue:R_10.scene4.dialogue0011')
    stage.unit(1).setAction(3, 3)
    stage.say('@dialogue:R_10.scene4.dialogue0012')
    stage.say('@dialogue:R_10.scene4.dialogue0013')
    stage.unit(24).setAction(0, 3)
    stage.say('@dialogue:R_10.scene4.dialogue0014')
    stage.unit(22).setAction(0, 1)
    stage.say('@dialogue:R_10.scene4.dialogue0015')
    stage.unit(0).move(34, 55, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene4.dialogue0016')
    stage.unit(24).setAction(0, 2)
    stage.unit(22).setAction(0, 2)
    stage.unit(1).setAction(0, 0)
    stage.delay(5)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_10.scene4.dialogue0017')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_10.scene4.dialogue0018')
    stage.say('@dialogue:R_10.scene4.dialogue0019')
    stage.say('@dialogue:R_10.scene4.dialogue0020')
    stage.say('@dialogue:R_10.scene4.dialogue0021')
    stage.say('@dialogue:R_10.scene4.dialogue0022')
    stage.unit(1).setAction(3, 0)
    stage.say('@dialogue:R_10.scene4.dialogue0023')
    stage.unit(1).setAction(0, 0)
    stage.delay(5)
    stage.unit(0).move(31, 55, 3)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_10.scene4.dialogue0024')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_10.scene4.dialogue0025')
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_10.scene4.dialogue0026')
    stage.delay(3)
    stage.showUnit(181, 95, 55, 3)
    stage.say('@dialogue:R_10.scene4.dialogue0027')
    stage.unit(181).move(61, 55, 3)
    stage.say('@dialogue:R_10.scene4.dialogue0028')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene4.dialogue0029')
    stage.unit(0).move(34, 55, 1)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_10.scene4.dialogue0030')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_10.scene4.dialogue0031')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_10.scene4.dialogue0032')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(2, 2)
    stage.say('@dialogue:R_10.scene4.dialogue0033')
    model.setFace(0)
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene4.dialogue0034')
    stage.unit(0).setAction(17, 1)
    stage.say('@dialogue:R_10.scene4.dialogue0035')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_10.scene4.dialogue0036')
    stage.effectSound(200, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 345, 110)
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(5)
    stage.showHead(143, 262, 136)
    stage.delay(5)
    stage.bottomTxt('장수는 완성을 차지하고 있었고', True, False, False)
    stage.delay(5)
    stage.showHead(119, 489, 138)
    stage.delay(5)
    stage.bottomTxt('여포는 서주를 차지하고 있었다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('그 뒤 순욱의 예상대로 여포는 화해를 받아들였다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('후환을 없앤 조조의 대군은', True, True, False)
    stage.delay(5)
    stage.head(0).move(326, 136)
    stage.delay(5)
    stage.bottomTxt('완성 공격에 전력을 기울일 수 있게 되었다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene5():
    stage.loadBg(2, 48)
    stage.showUnits([
        [
            143,
            53,
            31,
            2],
        [
            23,
            53,
            48,
            0,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('완성  장수 저택')
    stage.say('@dialogue:R_10.scene5.dialogue0001')
    stage.unit(143).move(49, 31, 3)
    stage.say('@dialogue:R_10.scene5.dialogue0002')
    stage.unit(143).setAction(0, 2)
    stage.say('@dialogue:R_10.scene5.dialogue0003')
    stage.unit(143).setAction(2, 2)
    stage.say('@dialogue:R_10.scene5.dialogue0004')
    stage.unit(143).setAction(0, 2)
    stage.say('@dialogue:R_10.scene5.dialogue0005')
    stage.unit(143).setAction(0, 3)
    stage.delay(5)
    stage.say('@dialogue:R_10.scene5.dialogue0006')
    stage.unit(143).setAction(0, 1)
    stage.delay(3)
    stage.unit(143).move(53, 31, 1)
    stage.delay(3)
    stage.unit(143).setAction(0, 2)
    stage.say('@dialogue:R_10.scene5.dialogue0007')
    stage.effectSound(200, 255)
    stage.delay(3)
    stage.bgSound(15)
    stage.delay(3)
    stage.clsUnit()
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
            13,
            38,
            40,
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
            18,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('완성 교외  조조군 본진')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_10.scene5.dialogue0008')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 9, [
        16,
        12])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 10
    stage.setBattlePos([
        [
            12,
            8],
        [
            20,
            8],
        [
            1,
            8],
        [
            14,
            6],
        [
            13,
            11],
        [
            10,
            9],
        [
            1,
            10],
        [
            0,
            8],
        [
            1,
            7],
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


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_10.scene6.dialogue0001')
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        return True
    if stage.unitClickTest(13):
        stage.say('@dialogue:R_10.scene6.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_10.scene6.dialogue0003')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_10.scene6.dialogue0004')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_10.scene6.dialogue0005')
    if stage.unitClickTest(16):
        stage.say('@dialogue:R_10.scene6.dialogue0006')
    if stage.unitClickTest(18):
        stage.say('@dialogue:R_10.scene6.dialogue0007')
    return False


def scene7():
    stage.showUnit(181, 49, 16, 3)
    stage.say('@dialogue:R_10.scene7.dialogue0001')
    stage.unit(181).move(49, 46, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_10.scene7.dialogue0002')
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene7.dialogue0003')
    stage.unit(181).setAction(0, 2)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.showUnit(23, 49, 16, 2)
    stage.say('@dialogue:R_10.scene7.dialogue0004')
    stage.unit(0).setAction(0, 0)
    stage.unit(23).move(49, 52, 2)
    stage.delay(3)
    stage.unit(23).setAction(6, 2)
    stage.delay(3)
    stage.say('@dialogue:R_10.scene7.dialogue0005')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.say('@dialogue:R_10.scene7.dialogue0006')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_10.scene7.dialogue0007')
    stage.unit(0).setAction(3, 0)
    stage.say('@dialogue:R_10.scene7.dialogue0008')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene7.dialogue0009')
    model.setFace(3)
    stage.unit(0).setAction(3, 1)
    stage.say('@dialogue:R_10.scene7.dialogue0010')
    model.setFace(0)
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_10.scene7.dialogue0011')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).move(49, 16, 0)
    stage.unit(23).hide()
    stage.say('@dialogue:R_10.scene7.dialogue0012')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_10.scene7.dialogue0013')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_10.scene7.dialogue0014')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene7.dialogue0015')
    stage.effectSound(200, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(0, 36)
    stage.draw()
    stage.info('조조는 장수의 항복을 받아들여 호위병과 함께 완성에 들어갔다.')
    stage.info('병사 하나 잃지 않고 완성을 얻은 조조는\n완성에서 한가롭게 쉬고 있었다.')
    stage.bgSound(-1)
    return False


def scene8():
    stage.loadBg(2, 39)
    stage.showUnits([
        [
            0,
            68,
            59,
            1,
            1],
        [
            30,
            68,
            49,
            2,
            1]])
    stage.draw()
    stage.setStageName('완성  누각')
    stage.say('@dialogue:R_10.scene8.dialogue0001')
    model.setFace(3)
    stage.say('@dialogue:R_10.scene8.dialogue0002')
    model.setFace(0)
    stage.say('@dialogue:R_10.scene8.dialogue0003')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).move(55, 59, 3)
    stage.delay(1)
    stage.unit(0).move(55, 65, 2)
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(30).setAction(0)
    stage.say('@dialogue:R_10.scene8.dialogue0004')
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene8.dialogue0005')
    stage.unit(30).move(55, 49, 3)
    stage.delay(3)
    stage.unit(30).setAction(0, 2)
    stage.say('@dialogue:R_10.scene8.dialogue0006')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_10.scene8.dialogue0007')
    stage.unit(30).move(55, 55, 2)
    stage.say('@dialogue:R_10.scene8.dialogue0008')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_10.scene8.dialogue0009')
    stage.say('@dialogue:R_10.scene8.dialogue0010')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_10.scene8.dialogue0011')
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('좋다. 어서 데려오너라.\n되었다. 그만두어라.', -1)
    if sel == 1:
        vars[304] = 1
        stage.addAmbition(10)
        stage.bgSound(-1)
        stage.say('@dialogue:R_10.scene8.dialogue0012')
        stage.unit(30).move(55, 30, 0)
        stage.unit(0).setAction(0, 2)
        stage.unit(30).hide()
        stage.info('잠시 뒤…….', 3)
        stage.say('@dialogue:R_10.scene8.dialogue0013')
        stage.unit(0).setAction(0, 0)
        stage.showUnit(30, 55, 30, 2)
        stage.unit(30).move(55, 40, 2)
        stage.delay(3)
        stage.unit(30).setAction(0, 0)
        stage.delay(3)
        stage.say('@dialogue:R_10.scene8.dialogue0014')
        stage.showUnit(156, 55, 30, 2)
        stage.unitsMove([
            [
                stage.unit(30),
                65,
                40,
                1],
            [
                stage.unit(156),
                55,
                40,
                2]])
        stage.delay(3)
        stage.unit(30).setAction(0, 2)
        stage.say('@dialogue:R_10.scene8.dialogue0015')
        model.setFace(3)
        stage.unit(0).setAction(3, 0)
        stage.say('@dialogue:R_10.scene8.dialogue0016')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(30).setAction(0, 2)
        stage.unit(0).move(55, 60, 0)
        stage.delay(1)
        stage.unit(0).move(72, 60, 1)
        stage.delay(3)
        model.setFace(0)
        stage.unit(0).setAction(0, 3)
        stage.say('@dialogue:R_10.scene8.dialogue0017')
        stage.delay(3)
        stage.unit(156).move(55, 60, 2)
        stage.delay(1)
        stage.unit(0).setAction(0, 3)
        stage.unit(156).setAction(0, 1)
        stage.unit(156).move(62, 60, 1)
        stage.delay(10)
        stage.say('@dialogue:R_10.scene8.dialogue0018')
        stage.unit(30).move(55, 40, 3)
        stage.delay(1)
        stage.unit(30).move(55, 30, 0)
        stage.unit(30).hide()
        stage.delay(3)
        stage.unit(0).setAction(1, 3)
        stage.unit(156).setAction(1, 1)
        stage.delay(3)
        stage.say('@dialogue:R_10.scene8.dialogue0019')
        stage.unit(0).setAction(1, 2)
        stage.say('@dialogue:R_10.scene8.dialogue0020')
        stage.unit(156).setAction(8, 1)
        stage.delay(10)
    elif sel == 2:
        vars[304] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_10.scene8.dialogue0021')
        stage.unit(0).setAction(0, 1)
        model.setFace(3)
        stage.say('@dialogue:R_10.scene8.dialogue0022')
        model.setFace(0)
    stage.clsUnit()
    return False


def scene9():
    stage.loadBg(2, 48)
    stage.showUnits([
        [
            143,
            53,
            30,
            2],
        [
            23,
            53,
            48,
            0,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('완성  장수 저택')
    if vars[304]:
        stage.delay(3)
        stage.unit(143).setAction(2)
        stage.say('@dialogue:R_10.scene9.dialogue0001')
        stage.unit(143).move(49, 30, 3)
        stage.delay(3)
        stage.unit(143).setAction(2)
        stage.say('@dialogue:R_10.scene9.dialogue0002')
        stage.unit(143).setAction(0, 2)
        stage.delay(3)
        stage.unit(143).setAction(2, 2)
        stage.say('@dialogue:R_10.scene9.dialogue0003')
        stage.unit(23).setAction(0, 0)
        stage.delay(5)
        stage.unit(23).setAction(0, 2)
        stage.say('@dialogue:R_10.scene9.dialogue0004')
        stage.unit(143).setAction(0)
        stage.say('@dialogue:R_10.scene9.dialogue0005')
        stage.unit(23).setAction(0, 0)
        stage.say('@dialogue:R_10.scene9.dialogue0006')
        stage.unit(143).move(53, 30, 2)
        stage.say('@dialogue:R_10.scene9.dialogue0007')
        stage.unit(23).setAction(6, 0)
        stage.say('@dialogue:R_10.scene9.dialogue0008')
        stage.unit(23).setAction(0, 0)
        stage.delay(3)
        stage.unit(23).setAction(0, 2)
        stage.say('@dialogue:R_10.scene9.dialogue0009')
    if not vars[304]:
        stage.delay(3)
        stage.unit(143).setAction(2)
        stage.say('@dialogue:R_10.scene9.dialogue0010')
        stage.unit(23).setAction(0)
        stage.say('@dialogue:R_10.scene9.dialogue0011')
        stage.unit(143).move(49, 30, 3)
        stage.delay(3)
        stage.unit(143).setAction(2)
        stage.say('@dialogue:R_10.scene9.dialogue0012')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_10.scene9.dialogue0013')
        stage.unit(23).setAction(0, 0)
        stage.delay(3)
        stage.unit(23).setAction(0, 2)
        stage.say('@dialogue:R_10.scene9.dialogue0014')
        stage.unit(143).setAction(0, -1)
        stage.delay(3)
        stage.unit(143).setAction(0, 2)
        stage.say('@dialogue:R_10.scene9.dialogue0015')
        stage.unit(23).setAction(0, 0)
        stage.say('@dialogue:R_10.scene9.dialogue0016')
        stage.unit(23).setAction(6, 0)
        stage.say('@dialogue:R_10.scene9.dialogue0017')
        stage.unit(23).setAction(0, 0)
        stage.delay(3)
        stage.unit(23).setAction(0, 2)
        stage.say('@dialogue:R_10.scene9.dialogue0018')
    stage.showUnit(144, 53, 98)
    stage.say('@dialogue:R_10.scene9.dialogue0019')
    stage.unit(144).move(53, 58, 0)
    stage.say('@dialogue:R_10.scene9.dialogue0020')
    stage.unit(144).move(53, 98, 2)
    stage.unit(144).hide()
    stage.delay(3)
    stage.unit(23).setAction(0, 0)
    stage.say('@dialogue:R_10.scene9.dialogue0021')
    stage.effectSound(200, 255)
    stage.clsUnit()
    stage.loadBg(2, 48)
    stage.showUnits([
        [
            143,
            53,
            30,
            2],
        [
            23,
            53,
            48]])
    stage.draw()
    stage.info('그날 밤…….', 3)
    stage.setStageName('완성  장수 저택')
    stage.say('@dialogue:R_10.scene9.dialogue0022')
    stage.showUnit(144, 53, 98)
    stage.say('@dialogue:R_10.scene9.dialogue0023')
    stage.unit(23).setAction(0, 2)
    stage.unit(144).move(53, 58, 0)
    stage.say('@dialogue:R_10.scene9.dialogue0024')
    stage.unit(23).move(53, 50, 2)
    stage.say('@dialogue:R_10.scene9.dialogue0025')
    stage.unit(143).setAction(17, 2)
    stage.say('@dialogue:R_10.scene9.dialogue0026')
    stage.clsUnit()
    return False


def scene10():
    stage.loadBg(2, 39)
    stage.showUnits([
        [
            0,
            68,
            59,
            0,
            1],
        [
            30,
            68,
            49,
            2,
            1]])
    stage.draw()
    stage.setStageName('완성  누각')
    stage.delay(3)
    stage.unit(0).setAction(1, 1)
    stage.say('@dialogue:R_10.scene10.dialogue0001')
    stage.showUnit(31, 55, 30, 2)
    stage.say('@dialogue:R_10.scene10.dialogue0002')
    stage.effectSound(207, 0)
    stage.delay(3)
    stage.unit(0).setAction(1, 3)
    stage.unit(30).setAction(1, 3)
    stage.unit(31).move(55, 59, 2)
    stage.delay(3)
    stage.unit(31).setAction(2, 1)
    stage.say('@dialogue:R_10.scene10.dialogue0003')
    stage.unit(30).setAction(0, 3)
    stage.unit(0).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).move(67, 59, 3)
    stage.say('@dialogue:R_10.scene10.dialogue0004')
    model.setFace(2)
    stage.unit(0).setAction(2, 3)
    stage.say('@dialogue:R_10.scene10.dialogue0005')
    stage.unit(31).setAction(0, 1)
    stage.say('@dialogue:R_10.scene10.dialogue0006')
    model.setFace(0)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_10.scene10.dialogue0007')
    model.setFace(2)
    stage.unit(0).setAction(2, 3)
    stage.say('@dialogue:R_10.scene10.dialogue0008')
    model.setFace(0)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_10.scene10.dialogue0009')
    stage.unitsMove([
        [
            stage.unit(31),
            55,
            47,
            0],
        [
            stage.unit(0),
            55,
            59,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(31),
            55,
            30,
            0],
        [
            stage.unit(0),
            55,
            42,
            0]])
    stage.unit(31).hide()
    stage.unitsMove([
        [
            stage.unit(0),
            55,
            30,
            0],
        [
            stage.unit(30),
            55,
            49,
            3]])
    stage.unit(0).hide()
    stage.unit(30).move(55, 30, 0)
    stage.unit(30).hide()
    stage.delay(3)
    stage.effectSound(207, 255)
    stage.clsUnit()
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
