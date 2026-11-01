# Source Generated with Decompyle++
# File: R_08.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    if vars[707] and vars[1001]:
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
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setEventName('호랑이 같은 장수')
    stage.setStageName('복양  여포 저택')
    stage.delay(5)
    stage.unit(119).setAction(3)
    stage.say('@dialogue:R_08.scene0.dialogue0001')
    stage.unit(119).setAction(0)
    stage.say('@dialogue:R_08.scene0.dialogue0002')
    stage.unit(119).setAction(3)
    stage.say('@dialogue:R_08.scene0.dialogue0003')
    stage.unit(119).setAction(0)
    stage.delay(3)
    stage.unit(119).setAction(0, 2)
    stage.say('@dialogue:R_08.scene0.dialogue0004')
    stage.say('@dialogue:R_08.scene0.dialogue0005')
    stage.unit(119).setAction(0, 1)
    stage.say('@dialogue:R_08.scene0.dialogue0006')
    stage.unit(119).setAction(17)
    stage.say('@dialogue:R_08.scene0.dialogue0007')
    stage.say('@dialogue:R_08.scene0.dialogue0008')
    stage.effectSound(205, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(119, 416, 63)
    stage.showHead(0, 480, 63)
    stage.showHead(145, 482, 150)
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(5)
    stage.bottomTxt('서주 태수 도겸은 이미 죽었고', True, False, False)
    stage.delay(5)
    stage.head(145).hide()
    stage.delay(5)
    stage.bottomTxt('그의 유언에 따라', True, True, False)
    stage.delay(5)
    stage.showHead(32, 482, 150)
    stage.bottomTxt('유비가 뒤를 이어 서주 태수가 되었다.', True, False, True)
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
            54,
            1],
        [
            16,
            38,
            46,
            1],
        [
            18,
            38,
            38,
            1],
        [
            20,
            60,
            54,
            3],
        [
            22,
            60,
            46,
            3],
        [
            21,
            60,
            38,
            3]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setStageName('복양 교외  조조군 본진')
    stage.say('@dialogue:R_08.scene1.dialogue0001')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_08.scene1.dialogue0002')
    stage.unit(1).move(40, 54, 1)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_08.scene1.dialogue0003')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_08.scene1.dialogue0004')
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_08.scene1.dialogue0005')
    stage.unit(1).setAction(3, 1)
    stage.say('@dialogue:R_08.scene1.dialogue0006')
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_08.scene1.dialogue0007')
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_08.scene1.dialogue0008')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_08.scene1.dialogue0009')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_08.scene1.dialogue0010')
    stage.effectSound(205, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(119, 416, 63)
    stage.showHead(0, 480, 63)
    stage.bottomTxt('여포와 잠시 휴전 중이던 조조는', False, False, False)
    stage.delay(5)
    stage.head(0).move(480, 154)
    stage.head(0).move(403, 154)
    stage.bottomTxt('교착 상태를 깨기 위해 여남으로 향했다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene2():
    stage.bgSound(18)
    stage.delay(3)
    stage.loadBg(2, 30)
    stage.showUnits([
        [
            187,
            54,
            4,
            2],
        [
            188,
            46,
            4,
            2]])
    stage.draw()
    stage.setStageName('초현  산속의 작은 마을')
    stage.unitsMove([
        [
            stage.unit(187),
            54,
            24,
            2],
        [
            stage.unit(188),
            46,
            24,
            2]])
    stage.say('@dialogue:R_08.scene2.dialogue0001')
    stage.showUnit(16, 54, 7, 2)
    stage.unitsMove([
        [
            stage.unit(187),
            54,
            32,
            2],
        [
            stage.unit(188),
            46,
            32,
            2],
        [
            stage.unit(16),
            54,
            15,
            2]])
    stage.showUnit(0, 46, 5, 2)
    stage.unitsMove([
        [
            stage.unit(187),
            54,
            52,
            2],
        [
            stage.unit(188),
            46,
            52,
            2],
        [
            stage.unit(16),
            54,
            35,
            2],
        [
            stage.unit(0),
            46,
            25,
            2]])
    stage.say('@dialogue:R_08.scene2.dialogue0002')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_08.scene2.dialogue0003')
    stage.showUnit(15, 50, 98)
    stage.unitsMove([
        [
            stage.unit(187),
            54,
            74,
            2],
        [
            stage.unit(188),
            46,
            74,
            2],
        [
            stage.unit(16),
            54,
            57,
            2],
        [
            stage.unit(0),
            46,
            47,
            2],
        [
            stage.unit(15),
            50,
            78,
            0]])
    stage.delay(5)
    stage.unit(15).move(54, 78, 1)
    stage.delay(3)
    stage.unit(15).setAction(0, 0)
    stage.say('@dialogue:R_08.scene2.dialogue0004')
    stage.effectSound(38, 1)
    stage.unit(187).setAction(10)
    stage.unit(15).move(46, 78, 3)
    stage.delay(3)
    stage.unit(15).setAction(0, 0)
    stage.say('@dialogue:R_08.scene2.dialogue0005')
    stage.effectSound(38, 1)
    stage.unit(188).setAction(10)
    stage.unit(16).move(54, 59, 2)
    stage.say('@dialogue:R_08.scene2.dialogue0006')
    stage.unit(15).setAction(0, 1)
    stage.say('@dialogue:R_08.scene2.dialogue0007')
    stage.unit(16).move(50, 59, 2)
    stage.say('@dialogue:R_08.scene2.dialogue0008')
    stage.unit(15).move(50, 78, 1)
    stage.delay(1)
    stage.unit(15).move(50, 65, 0)
    stage.say('@dialogue:R_08.scene2.dialogue0009')
    stage.unit(16).setAction(14)
    stage.say('@dialogue:R_08.scene2.dialogue0010')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_08.scene2.dialogue0011')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_08.scene2.dialogue0012')
    stage.unit(16).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(16).setAction(14)
    stage.say('@dialogue:R_08.scene2.dialogue0013')
    stage.unit(15).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(15).setAction(14)
    stage.say('@dialogue:R_08.scene2.dialogue0014')
    stage.unit(16).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(16).setAction(14)
    stage.delay(3)
    stage.unit(16).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(16).setAction(14)
    stage.say('@dialogue:R_08.scene2.dialogue0015')
    stage.unit(15).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(15).setAction(14)
    stage.delay(3)
    stage.unit(15).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(15).setAction(14)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_08.scene2.dialogue0016')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_08.scene2.dialogue0017')
    stage.say('@dialogue:R_08.scene2.dialogue0018')
    stage.unit(16).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(16).setAction(14)
    stage.say('@dialogue:R_08.scene2.dialogue0019')
    stage.unit(15).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(15).setAction(14)
    stage.say('@dialogue:R_08.scene2.dialogue0020')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_08.scene2.dialogue0021')
    stage.unit(16).setAction(0, 2)
    stage.delay(3)
    stage.unit(16).setAction(0, 0)
    stage.say('@dialogue:R_08.scene2.dialogue0022')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_08.scene2.dialogue0023')
    stage.unit(16).setAction(0, 1)
    stage.say('@dialogue:R_08.scene2.dialogue0024')
    stage.unit(16).setAction(0, 2)
    stage.say('@dialogue:R_08.scene2.dialogue0025')
    stage.unit(15).setAction(0)
    stage.say('@dialogue:R_08.scene2.dialogue0026')
    stage.unit(15).setAction(0, 2)
    stage.unitsMove([
        [
            stage.unit(16),
            50,
            15,
            0],
        [
            stage.unit(0),
            46,
            5,
            0]])
    stage.unit(0).hide()
    stage.unit(16).move(50, 5, 0)
    stage.unit(16).hide()
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            16,
            38,
            54,
            1],
        [
            20,
            60,
            54,
            3]])
    stage.draw()
    stage.info('그날 밤…….', 3)
    stage.setStageName('초현  조조군 본진')
    stage.delay(3)
    stage.unit(16).move(40, 54, 1)
    stage.delay(3)
    stage.unit(16).setAction(0, 2)
    stage.say('@dialogue:R_08.scene3.dialogue0001')
    stage.say('@dialogue:R_08.scene3.dialogue0002')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_08.scene3.dialogue0003')
    stage.unit(16).setAction(0, 1)
    stage.say('@dialogue:R_08.scene3.dialogue0004')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_08.scene3.dialogue0005')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_08.scene3.dialogue0006')
    stage.unit(16).setAction(0, 2)
    stage.say('@dialogue:R_08.scene3.dialogue0007')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_08.scene3.dialogue0008')
    stage.unit(16).setAction(0, 1)
    stage.say('@dialogue:R_08.scene3.dialogue0009')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_08.scene3.dialogue0010')
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 30)
    stage.showUnits([
        [
            181,
            54,
            98],
        [
            182,
            46,
            98]])
    stage.draw()
    stage.info('다음 날…….', 3)
    stage.setStageName('초현  산속의 작은 마을')
    stage.unitsMove([
        [
            stage.unit(181),
            54,
            83,
            0],
        [
            stage.unit(182),
            46,
            83,
            0]])
    stage.say('@dialogue:R_08.scene4.dialogue0001')
    stage.showUnit(15, 50, 98)
    stage.unitsMove([
        [
            stage.unit(181),
            54,
            68,
            0],
        [
            stage.unit(182),
            46,
            68,
            0],
        [
            stage.unit(15),
            50,
            83,
            0]])
    stage.delay(3)
    stage.unit(15).setAction(3, 0)
    stage.say('@dialogue:R_08.scene4.dialogue0002')
    stage.unitsMove([
        [
            stage.unit(181),
            54,
            60,
            0],
        [
            stage.unit(182),
            46,
            60,
            0],
        [
            stage.unit(15),
            50,
            75,
            0]])
    stage.say('@dialogue:R_08.scene4.dialogue0003')
    stage.unit(15).setAction(3, 0)
    stage.say('@dialogue:R_08.scene4.dialogue0004')
    stage.effectSound(38, 1)
    stage.unit(15).setAction(15)
    stage.unit(181).setAction(0, 2)
    stage.say('@dialogue:R_08.scene4.dialogue0005')
    stage.unit(182).setAction(0, 2)
    stage.say('@dialogue:R_08.scene4.dialogue0006')
    stage.say('@dialogue:R_08.scene4.dialogue0007')
    stage.clsUnit()
    return False


def scene5():
    model.unitJoin(15, 0, 0)
    if vars[1013]:
        gvars[0] = 15
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


def scene6():
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
            2],
        [
            22,
            60,
            46,
            3],
        [
            21,
            60,
            40,
            3],
        [
            15,
            49,
            52,
            2]])
    stage.draw()
    stage.setStageName('초현  조조군 본진')
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_08.scene6.dialogue0001')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_08.scene6.dialogue0002')
    stage.say('@dialogue:R_08.scene6.dialogue0003')
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(20).move(52, 52, 3)
    stage.delay(5)
    stage.unit(20).move(60, 52, 3)
    stage.say('@dialogue:R_08.scene6.dialogue0004')
    stage.unit(15).setAction(0, 1)
    stage.say('@dialogue:R_08.scene6.dialogue0005')
    stage.unit(15).setAction(0, 2)
    stage.say('@dialogue:R_08.scene6.dialogue0006')
    model.setFace(3)
    stage.unit(0).setAction(3, 0)
    stage.say('@dialogue:R_08.scene6.dialogue0007')
    stage.say('@dialogue:R_08.scene6.dialogue0008')
    model.setFace(0)
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_08.scene6.dialogue0009')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_08.scene6.dialogue0010')
    stage.say('@dialogue:R_08.scene6.dialogue0011')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_08.scene6.dialogue0012')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_08.scene6.dialogue0013')
    stage.unit(15).setAction(11, 2)
    stage.say('@dialogue:R_08.scene6.dialogue0014')
    model.setFace(3)
    stage.unit(0).setAction(3, 0)
    stage.say('@dialogue:R_08.scene6.dialogue0015')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_08.scene6.dialogue0016')
    stage.unit(16).setAction(0, 2)
    stage.say('@dialogue:R_08.scene6.dialogue0017')
    stage.unit(15).setAction(0, 2)
    stage.delay(3)
    stage.unit(15).move(49, 40, 0)
    stage.delay(1)
    stage.unit(16).setAction(0, 1)
    stage.unit(15).move(38, 40, 1)
    stage.unit(15).setAction(0)
    stage.bgSound(15)
    stage.delay(3)
    stage.say('@dialogue:R_08.scene6.dialogue0018')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_08.scene6.dialogue0019')
    stage.unit(0).setAction(17, 0)
    stage.say('@dialogue:R_08.scene6.dialogue0020')
    stage.unit(0).setAction(0, 0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        16,
        15,
        1])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 8
    stage.setBattlePos([
        [
            2,
            21],
        [
            2,
            19],
        [
            3,
            20],
        [
            4,
            21],
        [
            1,
            18],
        [
            5,
            22],
        [
            1,
            19],
        [
            4,
            22],
        [
            2,
            20],
        [
            3,
            21],
        [
            0,
            20],
        [
            2,
            23],
        [
            1,
            21],
        [
            2,
            22],
        [
            1,
            22]])
    return False


def scene7():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_08.scene7.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_08.scene7.dialogue0002')
    if stage.unitClickTest(16):
        stage.say('@dialogue:R_08.scene7.dialogue0003')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_08.scene7.dialogue0004')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_08.scene7.dialogue0005')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_08.scene7.dialogue0006')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_08.scene7.dialogue0007')
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
