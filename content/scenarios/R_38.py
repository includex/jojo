# Source Generated with Decompyle++
# File: R_38.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            19,
            52,
            40,
            2]])
    if vars[735]:
        stage.showUnit(4, 52, 70)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('위·오의 비밀 동맹')
    stage.setStageName('허도  승상부')
    if vars[735]:
        stage.effectSound(200, 255)
        stage.bgSound(18)
        stage.delay(3)
        model.setFace(3)
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_38.scene0.dialogue0001')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.delay(3)
        stage.unit(4).setAction(6)
        stage.say('@dialogue:R_38.scene0.dialogue0002')
        stage.unit(4).setAction(0)
        model.unitJoin(4, 0, 0)
        if vars[1013]:
            gvars[0] = 4
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
        stage.effectSound(200, 0)
        stage.delay(3)
    stage.say('@dialogue:R_38.scene0.dialogue0003')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_38.scene0.dialogue0004')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_38.scene0.dialogue0005')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_38.scene0.dialogue0006')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_38.scene0.dialogue0007')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_38.scene0.dialogue0008')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_38.scene0.dialogue0009')
    stage.showUnit(25, 90, 55, 3)
    stage.say('@dialogue:R_38.scene0.dialogue0010')
    stage.unit(25).move(48, 55, 3)
    stage.unit(25).setAction(0)
    stage.say('@dialogue:R_38.scene0.dialogue0011')
    stage.unit(25).setAction(6)
    stage.say('@dialogue:R_38.scene0.dialogue0012')
    stage.unit(25).setAction(0)
    stage.unit(25).move(95, 55, 1)
    stage.unit(25).hide()
    stage.say('@dialogue:R_38.scene0.dialogue0013')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_38.scene0.dialogue0014')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_38.scene0.dialogue0015')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 334, 102)
    stage.bottomTxt('형주를 지키던 촉의 장수는', False, False, False)
    stage.delay(5)
    stage.showHead(3, 278, 236)
    stage.delay(5)
    stage.bottomTxt('지용을 겸비한 관우였다.', True, False, False)
    stage.delay(8)
    stage.showHead(76, 500, 217)
    stage.delay(5)
    stage.bottomTxt('손권', True, True, False)
    stage.delay(5)
    stage.bottomTxt('위가 먼저 형주를 공격한다는 조건으로', True, False, False)
    stage.delay(5)
    stage.bottomTxt('위와 비밀리에 동맹을 맺었다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('조조가 이 조건을 받아들이자', True, True, False)
    stage.delay(5)
    stage.head(0).hide()
    stage.delay(5)
    stage.showHead(5, 300, 76)
    stage.delay(5)
    stage.bottomTxt('조인을 파견하여', True, False, False)
    stage.delay(5)
    stage.showHead(25, 364, 109)
    stage.delay(5)
    stage.bottomTxt('만총', True, False, False)
    stage.delay(5)
    stage.head(5).move(278, 156)
    stage.head(25).move(342, 202)
    stage.delay(5)
    stage.bottomTxt('형주를 공격했다.', True, False, True)
    stage.bottomTxt('그러나 조인과 만총은 관우의 반격을 받아', False, False, False)
    stage.delay(5)
    stage.head(5).move(255, 140)
    stage.head(25).move(342, 140)
    stage.head(25).move(319, 140)
    stage.head(3).move(288, 220)
    stage.delay(5)
    stage.bottomTxt('오히려 번성에 갇히고 말았다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            9,
            48,
            58,
            3],
        [
            14,
            48,
            52,
            3]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_38.scene1.dialogue0001')
    stage.unit(14).setAction(6)
    stage.say('@dialogue:R_38.scene1.dialogue0002')
    stage.unit(14).setAction(0)
    stage.say('@dialogue:R_38.scene1.dialogue0003')
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_38.scene1.dialogue0004')
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(14).setAction(0, 2)
    stage.say('@dialogue:R_38.scene1.dialogue0005')
    stage.unit(14).setAction(0, 3)
    stage.say('@dialogue:R_38.scene1.dialogue0006')
    stage.unit(14).move(95, 52, 1)
    stage.unit(14).hide()
    stage.delay(3)
    stage.unit(9).setAction(0, 1)
    stage.say('@dialogue:R_38.scene1.dialogue0007')
    stage.unit(9).setAction(0, 3)
    stage.say('@dialogue:R_38.scene1.dialogue0008')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_38.scene1.dialogue0009')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_38.scene1.dialogue0010')
    stage.unit(9).setAction(0)
    stage.unit(9).move(85, 58, 1)
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(9).setAction(0, 3)
    stage.say('@dialogue:R_38.scene1.dialogue0011')
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_38.scene1.dialogue0012')
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(9).move(95, 58, 1)
    stage.unit(9).hide()
    stage.say('@dialogue:R_38.scene1.dialogue0013')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_38.scene1.dialogue0014')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_38.scene1.dialogue0015')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(5, 271, 145)
    stage.showHead(3, 271, 225)
    stage.showHead(14, 283, 47)
    stage.delay(5)
    stage.bottomTxt('우금과', False, False, False)
    stage.delay(5)
    stage.showHead(9, 347, 89)
    stage.delay(5)
    stage.bottomTxt('방덕', True, False, False)
    stage.delay(5)
    stage.head(14).move(207, 47)
    stage.head(14).move(207, 145)
    stage.head(9).move(335, 145)
    stage.delay(5)
    stage.bottomTxt('원군을 이끌고 조인이 포위된 번성으로 달려갔다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 51)
    stage.showUnits([
        [
            76,
            53,
            29,
            2],
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
            80,
            65,
            48,
            3],
        [
            194,
            65,
            54,
            3]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('건업  손권의 저택')
    stage.say('@dialogue:R_38.scene2.dialogue0001')
    stage.unit(80).setAction(6)
    stage.say('@dialogue:R_38.scene2.dialogue0002')
    stage.unit(80).setAction(0)
    stage.say('@dialogue:R_38.scene2.dialogue0003')
    stage.unit(80).setAction(6)
    stage.say('@dialogue:R_38.scene2.dialogue0004')
    stage.unit(80).setAction(0)
    stage.delay(3)
    stage.unit(80).setAction(0, 2)
    stage.say('@dialogue:R_38.scene2.dialogue0005')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(5, 325, 145)
    stage.showHead(3, 261, 145)
    stage.bottomTxt('조인과 관우는 번성에서 치열하게 싸우고 있었다.', False, False, False)
    stage.delay(8)
    stage.showHead(80, 498, 210)
    stage.delay(5)
    stage.bottomTxt('여몽은 이 기회를 틈타,', True, True, False)
    stage.delay(5)
    stage.head(80).move(307, 238)
    stage.delay(5)
    stage.bottomTxt('형주를 점령하는 데 성공했다.', True, False, True)
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
            52,
            70],
        [
            8,
            58,
            70],
        [
            15,
            64,
            70],
        [
            10,
            70,
            70],
        [
            20,
            52,
            40,
            2],
        [
            19,
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
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_38.scene3.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_38.scene3.dialogue0002')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_38.scene3.dialogue0003')
    stage.showUnit(181, 90, 55, 3)
    stage.say('@dialogue:R_38.scene3.dialogue0004')
    stage.say('@dialogue:R_38.scene3.dialogue0005')
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_38.scene3.dialogue0006')
    stage.unit(181).setAction(0)
    stage.unit(181).hide()
    stage.say('@dialogue:R_38.scene3.dialogue0007')
    stage.showUnit(193, 90, 55, 3)
    stage.say('@dialogue:R_38.scene3.dialogue0008')
    stage.unit(193).move(48, 55, 3)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('@dialogue:R_38.scene3.dialogue0009')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_38.scene3.dialogue0010')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_38.scene3.dialogue0011')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(193).move(95, 55, 1)
    stage.unit(193).hide()
    if vars[735]:
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_38.scene3.dialogue0012')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_38.scene3.dialogue0013')
        stage.unit(1).setAction(0, 3)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_38.scene3.dialogue0014')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_38.scene3.dialogue0015')
        stage.unit(1).setAction(2)
        stage.say('@dialogue:R_38.scene3.dialogue0016')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_38.scene3.dialogue0017')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_38.scene3.dialogue0018')
    if not vars[735]:
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_38.scene3.dialogue0019')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_38.scene3.dialogue0020')
        stage.unit(1).setAction(0, 3)
        stage.say('@dialogue:R_38.scene3.dialogue0021')
        stage.unit(1).setAction(2)
        stage.say('@dialogue:R_38.scene3.dialogue0022')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_38.scene3.dialogue0023')
        stage.effectSound(201, 255)
        stage.bgSound(18)
        stage.delay(3)
        stage.showUnit(4, 90, 55, 3)
        stage.say('@dialogue:R_38.scene3.dialogue0024')
        stage.unit(1).setAction(0, 0)
        stage.unit(4).move(48, 55, 3)
        stage.unit(4).setAction(0)
        stage.say('@dialogue:R_38.scene3.dialogue0025')
        stage.unit(4).setAction(6)
        stage.say('@dialogue:R_38.scene3.dialogue0026')
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_38.scene3.dialogue0027')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_38.scene3.dialogue0028')
        stage.unit(4).setAction(0)
        stage.delay(3)
        model.unitJoin(4, 0, 0)
        if vars[1013]:
            gvars[0] = 4
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
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_38.scene3.dialogue0029')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_38.scene3.dialogue0030')
    stage.effectSound(201, 255)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 333, 93)
    stage.showHead(3, 259, 189)
    stage.showHead(5, 323, 189)
    stage.bottomTxt('조조', False, False, False)
    stage.delay(5)
    stage.head(0).move(291, 109)
    stage.delay(5)
    stage.bottomTxt('번성을 구원하러 달려갔다.', True, False, True)
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            14,
            54,
            80],
        [
            9,
            44,
            64,
            1]])
    stage.draw()
    stage.setStageName('번성  조조군 본진')
    stage.say('@dialogue:R_38.scene4.dialogue0001')
    stage.unit(14).setAction(0, 1)
    stage.say('@dialogue:R_38.scene4.dialogue0002')
    stage.unit(9).setAction(0, 2)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_38.scene4.dialogue0003')
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(14).setAction(0, 0)
    stage.say('@dialogue:R_38.scene4.dialogue0004')
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_38.scene4.dialogue0005')
    stage.unit(9).setAction(0)
    stage.say('@dialogue:R_38.scene4.dialogue0006')
    stage.unit(9).setAction(0, 1)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        5,
        25,
        9,
        14])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 38
    stage.setBattlePos([
        [
            0,
            14],
        [
            17,
            13],
        [
            17,
            14],
        [
            10,
            2],
        [
            18,
            1],
        [
            19,
            0],
        [
            11,
            1],
        [
            17,
            1],
        [
            9,
            1],
        [
            1,
            15],
        [
            1,
            13],
        [
            1,
            14],
        [
            1,
            12],
        [
            0,
            13],
        [
            0,
            15]])
    return False


def scene5():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_38.scene5.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(9):
        stage.say('@dialogue:R_38.scene5.dialogue0002')
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
