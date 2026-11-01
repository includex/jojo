# Source Generated with Decompyle++
# File: R_33.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(18)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 44)
    stage.showUnits([
        [
            0,
            52,
            34,
            2],
        [
            1,
            69,
            52,
            3],
        [
            8,
            69,
            58,
            3],
        [
            15,
            69,
            64,
            3],
        [
            13,
            69,
            70,
            3],
        [
            20,
            39,
            52,
            1],
        [
            19,
            39,
            58,
            1],
        [
            23,
            39,
            64,
            1],
        [
            24,
            39,
            70,
            1],
        [
            9,
            52,
            52]])
    stage.draw()
    stage.setEventName('공명의 계책')
    stage.setStageName('한중  임시 승상부')
    stage.say('@dialogue:R_33.scene0.dialogue0001')
    stage.say('@dialogue:R_33.scene0.dialogue0002')
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_33.scene0.dialogue0003')
    stage.unit(9).setAction(0)
    stage.say('@dialogue:R_33.scene0.dialogue0004')
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_33.scene0.dialogue0005')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_33.scene0.dialogue0006')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_33.scene0.dialogue0007')
    stage.unit(1).setAction(0, 0)
    stage.say('@dialogue:R_33.scene0.dialogue0008')
    stage.unit(1).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_33.scene0.dialogue0009')
    model.unitJoin(9, 0, 0)
    if vars[1013]:
        gvars[0] = 9
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
    stage.setJoinEquip(9, -1, 0, -1, 0, 12)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            32,
            32,
            54,
            1],
        [
            35,
            50,
            41,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('성도  유비의 관저')
    stage.showUnit(182, 90, 54, 3)
    stage.say('@dialogue:R_33.scene1.dialogue0001')
    stage.unit(182).move(52, 54, 3)
    stage.delay(3)
    stage.unit(182).setAction(11)
    stage.say('@dialogue:R_33.scene1.dialogue0002')
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_33.scene1.dialogue0003')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_33.scene1.dialogue0004')
    stage.say('@dialogue:R_33.scene1.dialogue0005')
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_33.scene1.dialogue0006')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_33.scene1.dialogue0007')
    stage.unit(35).setAction(0)
    stage.effectSound(200, 255)
    stage.say('@dialogue:R_33.scene1.dialogue0008')
    stage.delay(5)
    stage.bgSound(12)
    stage.delay(3)
    stage.say('@dialogue:R_33.scene1.dialogue0009')
    stage.say('@dialogue:R_33.scene1.dialogue0010')
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_33.scene1.dialogue0011')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_33.scene1.dialogue0012')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_33.scene1.dialogue0013')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 51)
    stage.showUnits([
        [
            76,
            53,
            29,
            2],
        [
            193,
            53,
            48,
            0,
            6],
        [
            88,
            41,
            48,
            1],
        [
            78,
            41,
            54,
            1],
        [
            192,
            65,
            48,
            3],
        [
            194,
            65,
            54,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('건업  손권의 저택')
    stage.say('@dialogue:R_33.scene2.dialogue0001')
    stage.effectSound(200, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 162, 73)
    stage.showHead(140, 98, 73)
    stage.bottomTxt('조조는 장로를 무찌른 뒤', False, False, False)
    stage.delay(5)
    stage.head(140).hide()
    stage.delay(5)
    stage.head(0).move(130, 73)
    stage.delay(5)
    stage.bottomTxt('한중을 점령했다.', True, False, False)
    stage.delay(8)
    stage.showHead(32, 25, 171)
    stage.delay(5)
    stage.bottomTxt('유비는 사방에 위기가 닥쳤음을 느끼고,', True, True, False)
    stage.delay(5)
    stage.showHead(3, 302, 235)
    stage.delay(5)
    stage.head(3).move(263, 193)
    stage.delay(5)
    stage.bottomTxt('형주 삼군을 할양하는 조건으로 동오에 합비 공격을 청했다.', True, False, False)
    stage.delay(10)
    stage.showHead(78, 327, 238)
    stage.showHead(76, 511, 212)
    stage.delay(5)
    stage.bottomTxt('손권은 형주 삼군을 넘겨받았음을 확인한 뒤', True, True, False)
    stage.delay(10)
    stage.showHead(2, 422, 142)
    stage.delay(5)
    stage.bottomTxt('다시 공격한다', True, False, False)
    stage.delay(5)
    stage.head(76).move(486, 183)
    stage.delay(5)
    stage.bottomTxt('장료가 지키는 합비였다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene3():
    stage.loadBg(2, 44)
    stage.showUnits([
        [
            0,
            52,
            34,
            2],
        [
            1,
            69,
            52,
            3],
        [
            8,
            69,
            58,
            3],
        [
            15,
            69,
            64,
            3],
        [
            13,
            69,
            70,
            3],
        [
            20,
            39,
            52,
            1],
        [
            19,
            39,
            58,
            1],
        [
            23,
            39,
            64,
            1],
        [
            21,
            39,
            70,
            1],
        [
            181,
            52,
            52,
            0,
            11]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('한중  임시 승상부')
    stage.say('@dialogue:R_33.scene3.dialogue0001')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_33.scene3.dialogue0002')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_33.scene3.dialogue0003')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_33.scene3.dialogue0004')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_33.scene3.dialogue0005')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_33.scene3.dialogue0006')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_33.scene3.dialogue0007')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_33.scene3.dialogue0008')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(5)
    stage.say('@dialogue:R_33.scene3.dialogue0009')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_33.scene3.dialogue0010')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_33.scene3.dialogue0011')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            2,
            54,
            80],
        [
            10,
            44,
            64,
            1],
        [
            11,
            64,
            64,
            3]])
    stage.draw()
    stage.setStageName('합비  조조군 본진')
    stage.unit(10).setAction(0, 2)
    stage.say('@dialogue:R_33.scene4.dialogue0001')
    stage.unit(11).setAction(0, 2)
    stage.say('@dialogue:R_33.scene4.dialogue0002')
    stage.showUnit(181, 54, 22, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_33.scene4.dialogue0003')
    stage.unit(10).setAction(0, 0)
    stage.unit(11).setAction(0, 0)
    stage.say('@dialogue:R_33.scene4.dialogue0004')
    stage.say('@dialogue:R_33.scene4.dialogue0005')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(10).setAction(0, 1)
    stage.unit(11).setAction(0, 3)
    stage.unit(181).move(54, 76, 2)
    stage.delay(5)
    stage.unit(181).move(54, 64, 2)
    stage.delay(3)
    stage.unit(11).setAction(0, 2)
    stage.say('@dialogue:R_33.scene4.dialogue0006')
    stage.unit(10).setAction(0, 2)
    stage.say('@dialogue:R_33.scene4.dialogue0007')
    stage.unit(2).setAction(17)
    stage.say('@dialogue:R_33.scene4.dialogue0008')
    stage.unit(2).setAction(0)
    stage.unit(11).setAction(0, 3)
    stage.unit(10).setAction(0, 1)
    stage.unit(181).move(54, 22, 0)
    stage.unit(181).hide()
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        2,
        11,
        10], [
        0])
    gvars[4076] = 0
    gvars[4057] = 33
    stage.setBattlePos([
        [
            14,
            15],
        [
            15,
            16],
        [
            15,
            17],
        [
            14,
            16],
        [
            13,
            15],
        [
            16,
            16],
        [
            14,
            3],
        [
            15,
            3],
        [
            14,
            2],
        [
            15,
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
        stage.say('@dialogue:R_33.scene5.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(11):
        stage.say('@dialogue:R_33.scene5.dialogue0002')
    if stage.unitClickTest(10):
        stage.say('@dialogue:R_33.scene5.dialogue0003')
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
