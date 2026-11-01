# Source Generated with Decompyle++
# File: R_15.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(17)
    stage.delay(3)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            32,
            35,
            52,
            1],
        [
            3,
            35,
            46,
            1],
        [
            20,
            63,
            52,
            3],
        [
            22,
            63,
            46,
            3]])
    stage.draw()
    stage.setEventName('안녕, 사랑하는 여인이여')
    stage.setStageName('하비 교외  조조군 본진')
    stage.showUnit(181, 49, 16, 2)
    stage.say('@dialogue:R_15.scene0.dialogue0001')
    stage.unit(181).move(49, 21, 2)
    stage.showUnit(119, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            26,
            2],
        [
            stage.unit(119),
            49,
            21,
            2]])
    stage.showUnit(182, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            52,
            2],
        [
            stage.unit(119),
            49,
            47,
            2],
        [
            stage.unit(182),
            49,
            42,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            43,
            52,
            3],
        [
            stage.unit(119),
            49,
            52,
            2],
        [
            stage.unit(182),
            49,
            47,
            2]])
    stage.delay(1)
    stage.unit(181).setAction(0, 2)
    stage.unit(182).move(55, 47, 1)
    stage.delay(1)
    stage.unit(182).move(55, 52, 2)
    stage.say('@dialogue:R_15.scene0.dialogue0002')
    if vars[302]:
        stage.say('@dialogue:R_15.scene0.dialogue0003')
    if not vars[302]:
        stage.say('@dialogue:R_15.scene0.dialogue0004')
    stage.say('@dialogue:R_15.scene0.dialogue0005')
    stage.say('@dialogue:R_15.scene0.dialogue0006')
    stage.unit(119).setAction(3)
    stage.say('@dialogue:R_15.scene0.dialogue0007')
    stage.unit(119).setAction(0)
    stage.say('@dialogue:R_15.scene0.dialogue0008')
    stage.unitsMove([
        [
            stage.unit(181),
            46,
            52,
            1],
        [
            stage.unit(182),
            52,
            52,
            3]])
    stage.delay(5)
    stage.unit(119).move(49, 50, 0)
    stage.say('@dialogue:R_15.scene0.dialogue0009')
    stage.unitsMove([
        [
            stage.unit(119),
            49,
            16,
            0],
        [
            stage.unit(181),
            46,
            21,
            0],
        [
            stage.unit(182),
            52,
            21,
            0]])
    stage.unit(119).hide()
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.unit(182).move(49, 16, 0)
    stage.unit(182).hide()
    stage.delay(10)
    stage.effectSound(36, 1)
    stage.delay(10)
    stage.say('@dialogue:R_15.scene0.dialogue0010')
    stage.delay(10)
    return False


def scene1():
    stage.delay(5)
    stage.showUnit(181, 49, 16, 2)
    stage.delay(3)
    stage.unit(181).move(49, 21, 2)
    stage.showUnit(17, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            26,
            2],
        [
            stage.unit(17),
            49,
            21,
            2]])
    stage.showUnit(182, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            52,
            2],
        [
            stage.unit(17),
            49,
            47,
            2],
        [
            stage.unit(182),
            49,
            42,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            43,
            52,
            3],
        [
            stage.unit(17),
            49,
            52,
            2],
        [
            stage.unit(182),
            49,
            47,
            2]])
    stage.delay(1)
    stage.unit(181).setAction(0, 2)
    stage.unit(182).move(55, 47, 1)
    stage.delay(1)
    stage.unit(182).move(55, 52, 2)
    stage.say('@dialogue:R_15.scene1.dialogue0001')
    stage.say('@dialogue:R_15.scene1.dialogue0002')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_15.scene1.dialogue0003')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_15.scene1.dialogue0004')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_15.scene1.dialogue0005')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_15.scene1.dialogue0006')
    stage.say('@dialogue:R_15.scene1.dialogue0007')
    stage.say('@dialogue:R_15.scene1.dialogue0008')
    stage.say('@dialogue:R_15.scene1.dialogue0009')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_15.scene1.dialogue0010')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_15.scene1.dialogue0011')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_15.scene1.dialogue0012')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_15.scene1.dialogue0013')
    stage.unitsMove([
        [
            stage.unit(17),
            49,
            16,
            0],
        [
            stage.unit(181),
            43,
            26,
            0],
        [
            stage.unit(182),
            55,
            26,
            0]])
    stage.unit(17).hide()
    stage.unit(181).move(49, 26, 1)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            20,
            0],
        [
            stage.unit(182),
            49,
            26,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            16,
            0],
        [
            stage.unit(182),
            49,
            22,
            0]])
    stage.unit(181).hide()
    stage.unit(182).move(49, 16, 0)
    stage.unit(182).hide()
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    model.unitJoin(17, 0, 0)
    if vars[1013]:
        gvars[0] = 17
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
    stage.delay(10)
    stage.showUnit(181, 49, 16, 2)
    stage.say('@dialogue:R_15.scene2.dialogue0001')
    stage.unit(181).move(49, 21, 2)
    stage.showUnit(2, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            26,
            2],
        [
            stage.unit(2),
            49,
            21,
            2]])
    stage.showUnit(182, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            52,
            2],
        [
            stage.unit(2),
            49,
            47,
            2],
        [
            stage.unit(182),
            49,
            42,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            43,
            52,
            3],
        [
            stage.unit(2),
            49,
            52,
            2],
        [
            stage.unit(182),
            49,
            47,
            2]])
    stage.delay(1)
    stage.unit(181).setAction(0, 2)
    stage.unit(182).move(55, 47, 1)
    stage.delay(1)
    stage.unit(182).move(55, 52, 2)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_15.scene2.dialogue0002')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_15.scene2.dialogue0003')
    stage.unit(2).setAction(0, 1)
    stage.say('@dialogue:R_15.scene2.dialogue0004')
    stage.unit(2).setAction(0, 2)
    stage.delay(3)
    stage.unit(2).setAction(6)
    stage.say('@dialogue:R_15.scene2.dialogue0005')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_15.scene2.dialogue0006')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_15.scene2.dialogue0007')
    stage.unit(2).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(2),
            49,
            16,
            0],
        [
            stage.unit(181),
            43,
            26,
            0],
        [
            stage.unit(182),
            55,
            26,
            0]])
    stage.unit(2).hide()
    stage.unit(181).move(49, 26, 1)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            20,
            0],
        [
            stage.unit(182),
            49,
            26,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            16,
            0],
        [
            stage.unit(182),
            49,
            22,
            0]])
    stage.unit(181).hide()
    stage.unit(182).move(49, 16, 0)
    stage.unit(182).hide()
    stage.bgSound(-1)
    stage.effectSound(203, 0)
    stage.delay(3)
    model.unitJoin(2, 0, 0)
    if vars[1013]:
        gvars[0] = 2
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
    stage.say('@dialogue:R_15.scene2.dialogue0008')
    stage.unit(32).move(49, 52, 1)
    stage.delay(3)
    stage.unit(32).setAction(0, 2)
    stage.say('@dialogue:R_15.scene2.dialogue0009')
    stage.say('@dialogue:R_15.scene2.dialogue0010')
    stage.say('@dialogue:R_15.scene2.dialogue0011')
    stage.say('@dialogue:R_15.scene2.dialogue0012')
    stage.say('@dialogue:R_15.scene2.dialogue0013')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_15.scene2.dialogue0014')
    stage.unit(0).setAction(0)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(19)
    stage.delay(3)
    stage.loadBg(2, 61)
    stage.showUnit(0, 55, 61, 3)
    stage.draw()
    stage.setStageName('허도 승상부 정원')
    stage.showUnit(20, 90, 69, 3)
    stage.unit(20).move(61, 69, 3)
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_15.scene3.dialogue0001')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_15.scene3.dialogue0002')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_15.scene3.dialogue0003')
    stage.unit(20).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_15.scene3.dialogue0004')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_15.scene3.dialogue0005')
    stage.unit(20).setAction(0)
    stage.delay(8)
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_15.scene3.dialogue0006')
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_15.scene3.dialogue0007')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_15.scene3.dialogue0008')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_15.scene3.dialogue0009')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_15.scene3.dialogue0010')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_15.scene3.dialogue0011')
    stage.unit(20).setAction(0)
    stage.delay(8)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_15.scene3.dialogue0012')
    stage.delay(8)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(16)
    stage.delay(3)
    stage.sectionName(2, '하북의 쟁탈전')
    stage.loadBg(1, 0)
    stage.draw()
    stage.setEventName('유비를 공격한다')
    stage.delay(10)
    stage.showHead(0, 496, 124)
    stage.showHead(32, 496, 204)
    stage.showHead(119, 560, 164)
    stage.delay(5)
    stage.bottomTxt('천하무쌍한 여포는 하비에서 목숨을 잃었고,', False, False, False)
    stage.delay(5)
    stage.head(119).hide()
    stage.delay(10)
    stage.head(0).move(300, 116)
    stage.head(32).move(364, 116)
    stage.bottomTxt('조조와 유비는 허도로 개선하여 헌제를 알현했다.', True, True, False)
    stage.delay(12)
    stage.showHead(149, 332, 36)
    stage.delay(5)
    stage.bottomTxt('헌제는 유비가 한 황실의 종친임을 알고 크게 기뻐하며 황숙이라 불렀다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene5():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            32,
            50,
            40,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.showUnit(25, 95, 55, 3)
    stage.say('@dialogue:R_15.scene5.dialogue0001')
    stage.unit(25).move(48, 55, 3)
    stage.unit(25).setAction(0)
    stage.delay(3)
    stage.unit(25).setAction(6)
    stage.say('@dialogue:R_15.scene5.dialogue0002')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_15.scene5.dialogue0003')
    stage.say('@dialogue:R_15.scene5.dialogue0004')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_15.scene5.dialogue0005')
    stage.unit(25).setAction(0)
    stage.say('@dialogue:R_15.scene5.dialogue0006')
    stage.say('@dialogue:R_15.scene5.dialogue0007')
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_15.scene5.dialogue0008')
    stage.unit(25).setAction(0, 0)
    stage.say('@dialogue:R_15.scene5.dialogue0009')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_15.scene5.dialogue0010')
    stage.say('@dialogue:R_15.scene5.dialogue0011')
    stage.say('@dialogue:R_15.scene5.dialogue0012')
    stage.unit(25).setAction(0, 3)
    stage.say('@dialogue:R_15.scene5.dialogue0013')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_15.scene5.dialogue0014')
    stage.unit(0).move(34, 59, 2)
    stage.say('@dialogue:R_15.scene5.dialogue0015')
    stage.unit(0).move(34, 55, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_15.scene5.dialogue0016')
    stage.unit(0).setAction(17, 1)
    stage.say('@dialogue:R_15.scene5.dialogue0017')
    stage.unit(32).setAction(6, 2)
    stage.say('@dialogue:R_15.scene5.dialogue0018')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_15.scene5.dialogue0019')
    stage.unit(32).setAction(0, 2)
    stage.say('@dialogue:R_15.scene5.dialogue0020')
    stage.say('@dialogue:R_15.scene5.dialogue0021')
    stage.effectSound(200, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('유비에게 맡긴다\n유비에게 수고를 끼치지 않는다', 0)
    if sel == 1:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.unit(0).setAction(3, 1)
        stage.say('@dialogue:R_15.scene5.dialogue0022')
    elif sel == 2:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.say('@dialogue:R_15.scene5.dialogue0023')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_15.scene5.dialogue0024')
    stage.unit(32).setAction(6, 2)
    stage.say('@dialogue:R_15.scene5.dialogue0025')
    stage.effectSound(200, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 323, 100)
    stage.showHead(32, 387, 100)
    stage.delay(8)
    stage.showHead(136, 351, 188)
    stage.bottomTxt('원술은 여남에서 재기하지 못했고,', False, False, False)
    stage.delay(5)
    stage.head(136).move(451, 188)
    stage.head(136).move(479, 56)
    stage.bottomTxt('서형인 원소에게 몸을 의탁하러 갔다.', True, False, False)
    stage.delay(5)
    stage.head(32).move(415, 56)
    stage.bottomTxt('그러나 북상하던 도중', True, True, False)
    stage.delay(5)
    stage.head(136).hide()
    stage.delay(5)
    stage.bottomTxt('조조에게 군사를 빌린 유비에게 죽임을 당했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('원술이 죽은 뒤, 유비는…….', True, True, True)
    stage.bgSound(-1)
    return False


def scene6():
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
            18,
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
    stage.showUnit(181, 95, 55, 3)
    stage.say('@dialogue:R_15.scene6.dialogue0001')
    stage.unit(181).move(48, 55, 3)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_15.scene6.dialogue0002')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_15.scene6.dialogue0003')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_15.scene6.dialogue0004')
    stage.unit(0).move(30, 55, 3)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_15.scene6.dialogue0005')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_15.scene6.dialogue0006')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_15.scene6.dialogue0007')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_15.scene6.dialogue0008')
    stage.unit(24).setAction(0)
    stage.delay(5)
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_15.scene6.dialogue0009')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_15.scene6.dialogue0010')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_15.scene6.dialogue0011')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_15.scene6.dialogue0012')
    stage.unit(0).move(34, 55, 1)
    stage.say('@dialogue:R_15.scene6.dialogue0013')
    stage.unit(18).move(62, 70, 3)
    stage.delay(1)
    stage.unit(18).move(62, 55, 0)
    stage.delay(3)
    stage.unit(18).setAction(0, 3)
    stage.say('@dialogue:R_15.scene6.dialogue0014')
    stage.unit(18).setAction(6, 3)
    stage.say('@dialogue:R_15.scene6.dialogue0015')
    stage.clsUnit()
    stage.effectSound(202, 255)
    return False


def scene7():
    stage.loadBg(2, 48)
    stage.showUnits([
        [
            23,
            39,
            48,
            1],
        [
            18,
            53,
            48,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.setStageName('완성  장수 저택')
    stage.delay(3)
    stage.unit(18).setAction(6, 3)
    stage.say('@dialogue:R_15.scene7.dialogue0001')
    stage.unit(18).setAction(0)
    stage.say('@dialogue:R_15.scene7.dialogue0002')
    stage.unit(18).setAction(6)
    stage.say('@dialogue:R_15.scene7.dialogue0003')
    stage.unit(18).setAction(0)
    stage.say('@dialogue:R_15.scene7.dialogue0004')
    stage.unit(23).setAction(0, 2)
    stage.say('@dialogue:R_15.scene7.dialogue0005')
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0006')
    stage.unit(18).setAction(6)
    stage.say('@dialogue:R_15.scene7.dialogue0007')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_15.scene7.dialogue0008')
    stage.showUnit(181, 53, 93)
    stage.say('@dialogue:R_15.scene7.dialogue0009')
    stage.unit(23).setAction(0, 2)
    stage.unit(18).setAction(0, 2)
    stage.unit(181).move(53, 80, 0)
    stage.say('@dialogue:R_15.scene7.dialogue0010')
    stage.unit(18).move(53, 50, 2)
    stage.say('@dialogue:R_15.scene7.dialogue0011')
    stage.unit(181).move(53, 93, 2)
    stage.unit(181).hide()
    stage.delay(3)
    stage.unit(18).setAction(0, 3)
    stage.say('@dialogue:R_15.scene7.dialogue0012')
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0013')
    stage.unit(18).setAction(0, 2)
    stage.say('@dialogue:R_15.scene7.dialogue0014')
    stage.unit(18).move(53, 93, 2)
    stage.unit(18).hide()
    stage.showUnit(192, 53, 93)
    stage.say('@dialogue:R_15.scene7.dialogue0015')
    stage.unit(192).move(53, 48, 0)
    stage.delay(3)
    stage.unit(192).setAction(0, 3)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_15.scene7.dialogue0016')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_15.scene7.dialogue0017')
    stage.unit(23).setAction(0, 3)
    stage.say('@dialogue:R_15.scene7.dialogue0018')
    stage.say('@dialogue:R_15.scene7.dialogue0019')
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0020')
    stage.unit(192).setAction(2)
    stage.say('@dialogue:R_15.scene7.dialogue0021')
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_15.scene7.dialogue0022')
    stage.unit(23).setAction(0, 3)
    stage.say('@dialogue:R_15.scene7.dialogue0023')
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0024')
    stage.unit(192).setAction(2)
    stage.say('@dialogue:R_15.scene7.dialogue0025')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).move(53, 93, 2)
    stage.unit(192).hide()
    stage.delay(3)
    stage.unit(23).setAction(0, 2)
    stage.say('@dialogue:R_15.scene7.dialogue0026')
    stage.delay(10)
    stage.showUnit(143, 53, 93)
    stage.say('@dialogue:R_15.scene7.dialogue0027')
    stage.unit(143).move(53, 48, 0)
    stage.delay(3)
    stage.unit(143).setAction(0, 3)
    stage.delay(3)
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0028')
    stage.unit(143).setAction(2)
    stage.say('@dialogue:R_15.scene7.dialogue0029')
    stage.unit(23).setAction(0, 3)
    stage.say('@dialogue:R_15.scene7.dialogue0030')
    stage.unit(143).setAction(0)
    stage.say('@dialogue:R_15.scene7.dialogue0031')
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0032')
    stage.unit(143).setAction(2)
    stage.say('@dialogue:R_15.scene7.dialogue0033')
    stage.unit(23).setAction(0, 2)
    stage.say('@dialogue:R_15.scene7.dialogue0034')
    stage.unit(143).setAction(0)
    stage.say('@dialogue:R_15.scene7.dialogue0035')
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0036')
    stage.unit(23).setAction(6, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0037')
    stage.unit(143).setAction(2)
    stage.say('@dialogue:R_15.scene7.dialogue0038')
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_15.scene7.dialogue0039')
    stage.unit(143).setAction(0)
    stage.say('@dialogue:R_15.scene7.dialogue0040')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene8():
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
            6,
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
            2],
        [
            143,
            54,
            58,
            3,
            6],
        [
            23,
            54,
            52,
            3,
            6],
        [
            18,
            48,
            55,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.setStageName('허도  승상부')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_15.scene8.dialogue0001')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_15.scene8.dialogue0002')
    stage.unit(18).setAction(6)
    stage.say('@dialogue:R_15.scene8.dialogue0003')
    stage.unit(18).setAction(0)
    stage.say('@dialogue:R_15.scene8.dialogue0004')
    stage.unit(143).setAction(0)
    stage.say('@dialogue:R_15.scene8.dialogue0005')
    stage.unit(143).setAction(6)
    stage.say('@dialogue:R_15.scene8.dialogue0006')
    stage.unit(143).setAction(0)
    stage.delay(3)
    stage.unit(143).move(95, 58, 1)
    stage.unit(143).hide()
    stage.say('@dialogue:R_15.scene8.dialogue0007')
    stage.effectSound(202, 255)
    stage.bgSound(18)
    stage.delay(3)
    stage.say('@dialogue:R_15.scene8.dialogue0008')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_15.scene8.dialogue0009')
    model.setFace(3)
    stage.say('@dialogue:R_15.scene8.dialogue0010')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_15.scene8.dialogue0011')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_15.scene8.dialogue0012')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_15.scene8.dialogue0013')
    model.unitJoin(23, 0, 0)
    if vars[1013]:
        gvars[0] = 23
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
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 338, 94)
    stage.showHead(143, 274, 139)
    stage.showHead(32, 492, 119)
    stage.bottomTxt('완성의 장수가 조조의 휘하에 들어갔다.', False, False, False)
    stage.delay(5)
    stage.head(143).hide()
    stage.delay(8)
    stage.bottomTxt('조조는 즉시 대군을 동원하여,', True, True, False)
    stage.delay(5)
    stage.head(0).move(428, 119)
    stage.delay(5)
    stage.bottomTxt('서주로 출병하여 유비를 토벌했다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene9():
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
            17,
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
            23,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('하비 교외  조조군 본진')
    stage.say('@dialogue:R_15.scene9.dialogue0001')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_15.scene9.dialogue0002')
    stage.unit(0).setAction(17, 0)
    stage.say('@dialogue:R_15.scene9.dialogue0003')
    stage.unit(0).setAction(0, 0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        2,
        17,
        22,
        23,
        20])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 15
    stage.setBattlePos([
        [
            1,
            2],
        [
            3,
            4],
        [
            2,
            3],
        [
            2,
            1],
        [
            0,
            3],
        [
            0,
            1],
        [
            4,
            3],
        [
            2,
            5],
        [
            3,
            2],
        [
            1,
            4],
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


def scene10():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_15.scene10.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_15.scene10.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_15.scene10.dialogue0003')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_15.scene10.dialogue0004')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_15.scene10.dialogue0005')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_15.scene10.dialogue0006')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_15.scene10.dialogue0007')
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
