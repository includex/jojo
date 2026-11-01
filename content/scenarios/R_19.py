# Source Generated with Decompyle++
# File: R_19.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(18)
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
            35,
            54,
            1],
        [
            20,
            63,
            54,
            3],
        [
            22,
            63,
            48,
            3],
        [
            21,
            63,
            42,
            3]])
    if not vars[718]:
        stage.showUnits([
            [
                7,
                49,
                54,
                2],
            [
                181,
                45,
                48,
                2],
            [
                182,
                53,
                48,
                2],
            [
                15,
                35,
                48,
                1]])
    stage.draw()
    stage.setEventName('천명')
    stage.setStageName('관도  조조군 본진')
    stage.delay(3)
    if not vars[718]:
        stage.info('조조는 관도대전에서 승리하여\n원소군의 맹장 장합을 사로잡았다.')
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_19.scene0.dialogue0001')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_19.scene0.dialogue0002')
        stage.unit(7).setAction(0, 1)
        stage.say('@dialogue:R_19.scene0.dialogue0003')
        stage.unit(7).setAction(0, 2)
        stage.say('@dialogue:R_19.scene0.dialogue0004')
        stage.unit(7).setAction(0, 1)
        stage.say('@dialogue:R_19.scene0.dialogue0005')
        stage.unit(7).setAction(0, 2)
        stage.say('@dialogue:R_19.scene0.dialogue0006')
        stage.unit(7).setAction(6)
        stage.say('@dialogue:R_19.scene0.dialogue0007')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_19.scene0.dialogue0008')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_19.scene0.dialogue0009')
        model.setFace(0)
        stage.unit(7).setAction(0)
        stage.unitsMove([
            [
                stage.unit(181),
                45,
                36,
                0],
            [
                stage.unit(182),
                53,
                36,
                0],
            [
                stage.unit(7),
                49,
                42,
                0]])
        stage.delay(1)
        stage.unitsMove([
            [
                stage.unit(7),
                35,
                42,
                3],
            [
                stage.unit(181),
                45,
                26,
                0],
            [
                stage.unit(182),
                53,
                26,
                0]])
        stage.delay(3)
        stage.unit(7).setAction(0, 1)
        stage.unit(181).move(49, 26, 1)
        stage.delay(1)
        stage.unitsMove([
            [
                stage.unit(181),
                49,
                22,
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
                20,
                0]])
        stage.unit(181).hide()
        stage.unit(182).move(49, 16, 0)
        stage.unit(182).hide()
        model.unitJoin(7, 0, 0)
        if vars[1013]:
            gvars[0] = 7
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
    if vars[718]:
        stage.showUnit(15, 49, 16, 2)
        stage.say('@dialogue:R_19.scene0.dialogue0010')
        model.setFace(3)
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_19.scene0.dialogue0011')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(0, 0)
        stage.say('@dialogue:R_19.scene0.dialogue0012')
        stage.unit(15).move(49, 48, 2)
        stage.showUnit(7, 49, 16, 2)
        stage.say('@dialogue:R_19.scene0.dialogue0013')
        stage.unitsMove([
            [
                stage.unit(15),
                35,
                48,
                1],
            [
                stage.unit(7),
                49,
                54,
                2]])
        stage.unit(15).setAction(0)
        stage.unit(7).setAction(0)
        stage.say('@dialogue:R_19.scene0.dialogue0014')
        stage.unit(7).setAction(6)
        stage.say('@dialogue:R_19.scene0.dialogue0015')
        stage.unit(7).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(3)
        stage.say('@dialogue:R_19.scene0.dialogue0016')
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(7).setAction(0, 3)
        stage.say('@dialogue:R_19.scene0.dialogue0017')
        stage.unit(7).setAction(0, 2)
        stage.delay(3)
        stage.unit(7).setAction(6)
        stage.say('@dialogue:R_19.scene0.dialogue0018')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_19.scene0.dialogue0019')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_19.scene0.dialogue0020')
        stage.unit(7).setAction(0)
        stage.say('@dialogue:R_19.scene0.dialogue0021')
        stage.unit(15).setAction(3)
        stage.say('@dialogue:R_19.scene0.dialogue0022')
        stage.unit(15).setAction(0)
        stage.unit(7).move(49, 42, 0)
        stage.unit(7).move(35, 42, 1)
        model.setFace(0)
        model.unitJoin(7, 0, 0)
        if vars[1013]:
            gvars[0] = 7
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
    stage.bgSound(-1)
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.delay(10)
    stage.showUnit(181, 49, 16, 2)
    stage.say('@dialogue:R_19.scene0.dialogue0023')
    stage.unit(181).move(49, 26, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_19.scene0.dialogue0024')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_19.scene0.dialogue0025')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_19.scene0.dialogue0026')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.delay(10)
    stage.showUnit(181, 49, 16, 2)
    stage.say('@dialogue:R_19.scene0.dialogue0027')
    stage.unit(181).move(49, 22, 2)
    stage.showUnit(105, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            48,
            2],
        [
            stage.unit(105),
            49,
            42,
            2]])
    stage.say('@dialogue:R_19.scene0.dialogue0028')
    stage.unit(105).setAction(0, 3)
    stage.effectSound(208, 255)
    stage.bgSound(13)
    stage.delay(3)
    stage.say('@dialogue:R_19.scene0.dialogue0029')
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            54,
            2],
        [
            stage.unit(105),
            49,
            48,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            45,
            54,
            3],
        [
            stage.unit(105),
            49,
            54,
            2]])
    stage.delay(1)
    stage.unit(181).setAction(0, 2)
    stage.say('@dialogue:R_19.scene0.dialogue0030')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_19.scene0.dialogue0031')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_19.scene0.dialogue0032')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_19.scene0.dialogue0033')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_19.scene0.dialogue0034')
    stage.unit(105).setAction(3)
    stage.say('@dialogue:R_19.scene0.dialogue0035')
    stage.unit(105).setAction(0, 2)
    stage.delay(3)
    stage.unit(105).setAction(0, 0)
    stage.delay(3)
    stage.say('@dialogue:R_19.scene0.dialogue0036')
    stage.unit(105).setAction(0, 2)
    stage.say('@dialogue:R_19.scene0.dialogue0037')
    stage.unit(105).setAction(6)
    stage.say('@dialogue:R_19.scene0.dialogue0038')
    stage.unit(105).setAction(0)
    stage.delay(3)
    stage.unit(7).move(49, 42, 1)
    stage.delay(1)
    stage.unit(7).setAction(0, 2)
    stage.say('@dialogue:R_19.scene0.dialogue0039')
    stage.delay(3)
    stage.unit(105).setAction(0, 0)
    stage.say('@dialogue:R_19.scene0.dialogue0040')
    stage.unit(105).setAction(0, 1)
    stage.say('@dialogue:R_19.scene0.dialogue0041')
    stage.unit(105).setAction(0, 2)
    stage.say('@dialogue:R_19.scene0.dialogue0042')
    stage.unit(105).setAction(6)
    stage.say('@dialogue:R_19.scene0.dialogue0043')
    stage.unit(181).setAction(0, 1)
    stage.delay(3)
    stage.unit(105).setAction(0)
    stage.say('@dialogue:R_19.scene0.dialogue0044')
    stage.unitsMove([
        [
            stage.unit(105),
            49,
            50,
            0],
        [
            stage.unit(181),
            49,
            54,
            1]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(7),
            41,
            42,
            3],
        [
            stage.unit(105),
            49,
            42,
            0],
        [
            stage.unit(181),
            49,
            46,
            0]])
    stage.delay(1)
    stage.unit(7).setAction(0, 1)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(105),
            49,
            16,
            0],
        [
            stage.unit(181),
            49,
            20,
            0]])
    stage.delay(1)
    stage.unit(7).setAction(0, 0)
    stage.delay(3)
    stage.unit(105).hide()
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.delay(10)
    stage.say('@dialogue:R_19.scene0.dialogue0045')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 72)
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.showUnits([
        [
            101,
            70,
            98],
        [
            113,
            66,
            98],
        [
            102,
            74,
            98]])
    stage.setStageName('관도 근교  강가')
    stage.unitsMove([
        [
            stage.unit(113),
            66,
            88,
            0],
        [
            stage.unit(102),
            74,
            88,
            0]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(101),
            70,
            60,
            0],
        [
            stage.unit(113),
            66,
            50,
            0],
        [
            stage.unit(102),
            74,
            50,
            0]])
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_19.scene1.dialogue0001')
    stage.unit(102).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(0, 2)
    stage.say('@dialogue:R_19.scene1.dialogue0002')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_19.scene1.dialogue0003')
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_19.scene1.dialogue0004')
    stage.unit(113).setAction(3)
    stage.say('@dialogue:R_19.scene1.dialogue0005')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_19.scene1.dialogue0006')
    stage.unitsMove([
        [
            stage.unit(101),
            70,
            14,
            0],
        [
            stage.unit(113),
            66,
            14,
            0],
        [
            stage.unit(102),
            74,
            14,
            0]])
    stage.unit(101).hide()
    stage.unit(113).hide()
    stage.unit(102).hide()
    stage.effectSound(208, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 338, 124)
    stage.showHead(101, 338, 44)
    stage.bottomTxt('원소는 관도에서 조조에게 패했고', False, False, False)
    stage.delay(5)
    stage.head(101).move(306, 30)
    stage.delay(5)
    stage.bottomTxt('잔여 병력을 이끌고 여양으로 퇴각했다.', True, False, False)
    stage.delay(8)
    stage.showHead(110, 370, 12)
    stage.delay(5)
    stage.bottomTxt('지난번 업성에서 신중히 대응했던', True, True, False)
    stage.delay(5)
    stage.head(110).hide()
    stage.delay(5)
    stage.bottomTxt('원소에게 간언한 군사 전풍은 참수되었다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene2():
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
            106,
            38,
            48,
            1],
        [
            103,
            38,
            54,
            1],
        [
            102,
            38,
            60,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('여양  원소의 관저')
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_19.scene2.dialogue0001')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_19.scene2.dialogue0002')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(114).setAction(0, 0)
    stage.say('@dialogue:R_19.scene2.dialogue0003')
    stage.unit(113).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(2, 2)
    stage.say('@dialogue:R_19.scene2.dialogue0004')
    stage.unit(113).setAction(0, 2)
    stage.delay(3)
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_19.scene2.dialogue0005')
    stage.unit(103).setAction(0)
    stage.say('@dialogue:R_19.scene2.dialogue0006')
    stage.unit(114).setAction(0, 3)
    stage.unit(113).setAction(0, 3)
    stage.say('@dialogue:R_19.scene2.dialogue0007')
    stage.unit(113).setAction(3)
    stage.say('@dialogue:R_19.scene2.dialogue0008')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(115).setAction(0, 0)
    stage.say('@dialogue:R_19.scene2.dialogue0009')
    stage.unit(113).setAction(0, 2)
    stage.say('@dialogue:R_19.scene2.dialogue0010')
    stage.unit(113).setAction(2)
    stage.say('@dialogue:R_19.scene2.dialogue0011')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(0, 3)
    stage.unit(115).setAction(0, 3)
    stage.say('@dialogue:R_19.scene2.dialogue0012')
    stage.say('@dialogue:R_19.scene2.dialogue0013')
    stage.say('@dialogue:R_19.scene2.dialogue0014')
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_19.scene2.dialogue0015')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_19.scene2.dialogue0016')
    stage.unitsMove([
        [
            stage.unit(113),
            68,
            98,
            2],
        [
            stage.unit(114),
            68,
            98,
            2],
        [
            stage.unit(115),
            68,
            98,
            2]])
    stage.unit(113).hide()
    stage.unit(114).hide()
    stage.unit(115).hide()
    stage.say('@dialogue:R_19.scene2.dialogue0017')
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_19.scene2.dialogue0018')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(106).setAction(0, 2)
    stage.say('@dialogue:R_19.scene2.dialogue0019')
    stage.unit(102).setAction(0, 0)
    stage.say('@dialogue:R_19.scene2.dialogue0020')
    stage.say('@dialogue:R_19.scene2.dialogue0021')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 338, 124)
    stage.showHead(101, 306, 30)
    stage.bottomTxt('원소는 관도에서 패한 치욕을 씻으려', False, False, False)
    stage.delay(5)
    stage.head(101).move(380, 34)
    stage.delay(8)
    stage.bottomTxt('다시 병력을 모아 창정으로 진군했다.', True, False, False)
    stage.delay(8)
    stage.head(0).move(380, 114)
    stage.delay(8)
    stage.bottomTxt('원소군의 동향을 알아낸 조조군은 즉시 맞아 싸웠다.', True, True, True)
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
            7,
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
    stage.setStageName('창정  조조군 본진')
    stage.delay(3)
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_19.scene3.dialogue0001')
    stage.unit(21).setAction(0, 2)
    stage.say('@dialogue:R_19.scene3.dialogue0002')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_19.scene3.dialogue0003')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.showUnit(181, 49, 16, 2)
    stage.say('@dialogue:R_19.scene3.dialogue0004')
    stage.unit(181).move(49, 26, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_19.scene3.dialogue0005')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_19.scene3.dialogue0006')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_19.scene3.dialogue0007')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_19.scene3.dialogue0008')
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_19.scene3.dialogue0009')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_19.scene3.dialogue0010')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_19.scene3.dialogue0011')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_19.scene3.dialogue0012')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_19.scene3.dialogue0013')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(22).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 13, [
        21,
        15,
        7])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 19
    stage.setBattlePos([
        [
            8,
            32],
        [
            9,
            32],
        [
            7,
            32],
        [
            7,
            10],
        [
            12,
            14],
        [
            7,
            19],
        [
            11,
            24],
        [
            6,
            25],
        [
            7,
            11],
        [
            12,
            15],
        [
            7,
            20],
        [
            11,
            25],
        [
            6,
            26],
        [
            0,
            0],
        [
            0,
            0]])
    return False


def scene4():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_19.scene4.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_19.scene4.dialogue0002')
    if stage.unitClickTest(7):
        stage.say('@dialogue:R_19.scene4.dialogue0003')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_19.scene4.dialogue0004')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_19.scene4.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_19.scene4.dialogue0006')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_19.scene4.dialogue0007')
        stage.unit(7).setAction(0, 2)
        stage.say('@dialogue:R_19.scene4.dialogue0008')
        stage.unit(7).setAction(0, 1)
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
