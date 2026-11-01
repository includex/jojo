# Source Generated with Decompyle++
# File: R_46.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 59)
    stage.showUnit(57, 49, 8, 2)
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('사조성의 빛')
    stage.setStageName('장안 교외  산길')
    stage.unit(57).move(49, 18, 2)
    stage.showUnits([
        [
            58,
            45,
            8,
            2],
        [
            9,
            53,
            8,
            2]])
    stage.unitsMove([
        [
            stage.unit(57),
            49,
            45,
            2],
        [
            stage.unit(58),
            45,
            35,
            2],
        [
            stage.unit(9),
            53,
            35,
            2]])
    stage.unit(57).setAction(0)
    stage.unit(58).setAction(0)
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(57).setAction(2)
    stage.say('@dialogue:R_46.scene0.dialogue0001')
    stage.unit(57).setAction(0)
    stage.delay(3)
    stage.unit(57).setAction(0, 0)
    stage.say('@dialogue:R_46.scene0.dialogue0002')
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_46.scene0.dialogue0003')
    stage.unit(9).setAction(0)
    stage.say('@dialogue:R_46.scene0.dialogue0004')
    stage.unit(57).setAction(0, 2)
    stage.delay(3)
    stage.unit(57).setAction(2)
    stage.say('@dialogue:R_46.scene0.dialogue0005')
    stage.unit(57).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(57),
            49,
            95,
            2],
        [
            stage.unit(58),
            45,
            95,
            2],
        [
            stage.unit(9),
            53,
            95,
            2]])
    stage.unit(57).hide()
    stage.unit(58).hide()
    stage.unit(9).hide()
    stage.effectSound(203, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 242, 61)
    stage.showHead(57, 178, 61)
    stage.bottomTxt('조조에게 패한 마초 일행은', False, False, False)
    stage.delay(5)
    stage.showHead(140, 106, 96)
    stage.delay(5)
    stage.bottomTxt('한중의 장로에게 의탁했고', True, False, False)
    stage.delay(5)
    stage.head(57).move(170, 96)
    stage.delay(5)
    stage.bottomTxt('잠시 몸을 의탁할 곳을 얻었다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene1():
    stage.loadBg(2, 28)
    stage.showUnits([
        [
            19,
            68,
            42,
            1],
        [
            181,
            52,
            79,
            1],
        [
            182,
            54,
            46,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.info('한편 조조는 허도로 돌아왔다……', 3)
    stage.setStageName('허도  승상부 앞')
    stage.showUnit(0, 68, 94)
    stage.unit(0).move(68, 65, 0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_46.scene1.dialogue0001')
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_46.scene1.dialogue0002')
    stage.unit(182).setAction(11)
    stage.say('@dialogue:R_46.scene1.dialogue0003')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_46.scene1.dialogue0004')
    stage.unit(0).move(68, 50, 0)
    stage.unit(0).setAction(0)
    stage.unit(181).setAction(0)
    stage.unit(182).setAction(0)
    stage.say('@dialogue:R_46.scene1.dialogue0005')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_46.scene1.dialogue0006')
    model.setFace(3)
    stage.say('@dialogue:R_46.scene1.dialogue0007')
    model.setFace(0)
    stage.say('@dialogue:R_46.scene1.dialogue0008')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_46.scene1.dialogue0009')
    stage.say('@dialogue:R_46.scene1.dialogue0010')
    stage.effectSound(203, 255)
    stage.bgSound(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.say('@dialogue:R_46.scene1.dialogue0011')
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_46.scene1.dialogue0012')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_46.scene1.dialogue0013')
    model.unitJoin(19, 0, 0)
    if vars[1013]:
        gvars[0] = 19
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
    model.setFace(0)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 329, 99)
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(8)
    stage.showHead(32, 278, 207)
    stage.delay(5)
    stage.bottomTxt('형주의 유비는', True, False, False)
    stage.delay(5)
    stage.bottomTxt('천하를 셋으로 나누려는 제갈량의 구상을 실현하기 위해', True, False, False)
    stage.delay(5)
    stage.showHead(160, 9, 172)
    stage.delay(5)
    stage.bottomTxt('공격 중', True, True, False)
    stage.delay(5)
    stage.head(32).move(154, 177)
    stage.delay(5)
    stage.bottomTxt('유장이 다스리는 촉이었다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('유비군의 진격은 매우 순조로웠고', True, False, False)
    stage.delay(5)
    stage.head(32).move(73, 135)
    stage.delay(5)
    stage.bottomTxt('유장의 본거지 성도가 이제 지척이었다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            90,
            42,
            52,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('성도 근교  유비군 본진')
    stage.say('@dialogue:R_46.scene2.dialogue0001')
    stage.unit(32).setAction(0, 1)
    stage.say('@dialogue:R_46.scene2.dialogue0002')
    stage.unit(32).setAction(0, 2)
    stage.delay(3)
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_46.scene2.dialogue0003')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(90).setAction(6)
    stage.say('@dialogue:R_46.scene2.dialogue0004')
    stage.delay(3)
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_46.scene2.dialogue0005')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(90).setAction(0)
    stage.say('@dialogue:R_46.scene2.dialogue0006')
    stage.unit(90).setAction(0, 2)
    stage.say('@dialogue:R_46.scene2.dialogue0007')
    stage.unit(90).setAction(0, 1)
    stage.delay(3)
    stage.unit(90).setAction(6)
    stage.say('@dialogue:R_46.scene2.dialogue0008')
    stage.unit(90).setAction(0)
    stage.say('@dialogue:R_46.scene2.dialogue0009')
    stage.unit(90).setAction(0, 2)
    stage.say('@dialogue:R_46.scene2.dialogue0010')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(160, 11, 177)
    stage.showHead(32, 75, 138)
    stage.bottomTxt('유비가 보낸 원군 요청 서찰을', False, False, False)
    stage.delay(5)
    stage.bottomTxt('머지않아 형주를 지키던', True, True, False)
    stage.delay(5)
    stage.showHead(35, 279, 208)
    stage.delay(5)
    stage.bottomTxt('제갈량의 주둔지였다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 49)
    stage.showUnit(35, 54, 74)
    stage.draw()
    stage.effectSound(204, 0)
    stage.delay(3)
    stage.setStageName('강릉  유비의 관저')
    stage.say('@dialogue:R_46.scene3.dialogue0001')
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_46.scene3.dialogue0002')
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_46.scene3.dialogue0003')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_46.scene3.dialogue0004')
    stage.unit(35).setAction(8)
    stage.say('@dialogue:R_46.scene3.dialogue0005')
    stage.unit(35).setAction(12)
    stage.say('@dialogue:R_46.scene3.dialogue0006')
    stage.delay(10)
    gvars[0] = 207
    stage.setUnitAttr(35, 1, gvars[0])
    gvars[2035] = 79
    stage.setFAvatar(35, 89)
    if vars[1002]:
        gvars[4076] = 1
        pvars[290] = 4225232
        stage.varOper(0, 290, 2, 0, 10)
        gvars[4076] = 0
    stage.unit(35).setAction(8)
    stage.say('@dialogue:R_46.scene3.dialogue0007')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_46.scene3.dialogue0008')
    stage.unit(35).setAction(17)
    stage.say('@dialogue:R_46.scene3.dialogue0009')
    stage.effectSound(204, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 3)
    stage.showUnit(19, 56, 69, 2)
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('허도  사마의의 저택')
    stage.say('@dialogue:R_46.scene4.dialogue0001')
    stage.unit(19).setAction(0, 1)
    stage.say('@dialogue:R_46.scene4.dialogue0002')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            90,
            42,
            52,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('성도 근교  유비군 본진')
    stage.say('@dialogue:R_46.scene5.dialogue0001')
    stage.unit(90).setAction(6)
    stage.say('@dialogue:R_46.scene5.dialogue0002')
    stage.unit(90).setAction(0)
    stage.say('@dialogue:R_46.scene5.dialogue0003')
    stage.unit(90).setAction(6)
    stage.say('@dialogue:R_46.scene5.dialogue0004')
    stage.unit(90).setAction(0)
    stage.say('@dialogue:R_46.scene5.dialogue0005')
    stage.unit(90).setAction(0, 2)
    stage.say('@dialogue:R_46.scene5.dialogue0006')
    stage.unit(90).setAction(0, 1)
    stage.delay(3)
    stage.unit(90).setAction(6)
    stage.say('@dialogue:R_46.scene5.dialogue0007')
    stage.unit(90).setAction(0)
    stage.say('@dialogue:R_46.scene5.dialogue0008')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene6():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(160, 11, 177)
    stage.showHead(32, 139, 138)
    stage.bottomTxt('다음 날,', False, False, False)
    stage.delay(8)
    stage.showHead(90, 75, 138)
    stage.delay(5)
    stage.bottomTxt('유비군의 부군사 방통은', True, True, False)
    stage.delay(5)
    stage.bottomTxt('선봉 부대를 이끌고 성도를 공격했으며', True, False, False)
    stage.delay(8)
    stage.bottomTxt('그러나 낙봉파에서 적의 매복에 걸려', True, True, False)
    stage.delay(5)
    stage.head(90).hide()
    stage.delay(5)
    stage.bottomTxt('봉추는 그 자리에서 목숨을 잃었다.', True, False, True)
    stage.bgSound(13)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            36,
            42,
            52,
            1],
        [
            37,
            42,
            58,
            1],
        [
            46,
            67,
            52,
            3]])
    stage.draw()
    stage.setStageName('성도 근교@유비군 본진')
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_46.scene6.dialogue0001')
    stage.unit(37).setAction(0, 0)
    stage.say('@dialogue:R_46.scene6.dialogue0002')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(9)
    stage.say('@dialogue:R_46.scene6.dialogue0003')
    stage.unit(36).setAction(6)
    stage.say('@dialogue:R_46.scene6.dialogue0004')
    stage.unit(36).setAction(0)
    stage.say('@dialogue:R_46.scene6.dialogue0005')
    stage.unit(46).setAction(0, 0)
    stage.say('@dialogue:R_46.scene6.dialogue0006')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene7():
    stage.loadBg(2, 49)
    stage.showUnits([
        [
            35,
            53,
            74],
        [
            3,
            40,
            55,
            1],
        [
            33,
            40,
            49,
            1],
        [
            34,
            67,
            55,
            3],
        [
            46,
            53,
            55,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('강릉  유비의 관저')
    stage.say('@dialogue:R_46.scene7.dialogue0001')
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_46.scene7.dialogue0002')
    stage.unit(35).setAction(0, 0)
    stage.say('@dialogue:R_46.scene7.dialogue0003')
    stage.unit(33).setAction(0, 2)
    stage.delay(3)
    stage.unit(33).setAction(3)
    stage.say('@dialogue:R_46.scene7.dialogue0004')
    stage.unit(33).setAction(0)
    stage.delay(3)
    stage.unit(34).setAction(0, 2)
    stage.delay(3)
    stage.unit(34).setAction(6)
    stage.say('@dialogue:R_46.scene7.dialogue0005')
    stage.unit(34).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(0, 2)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_46.scene7.dialogue0006')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_46.scene7.dialogue0007')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene8():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(160, 11, 177)
    stage.showHead(32, 75, 138)
    stage.showHead(35, 279, 208)
    stage.delay(5)
    stage.bottomTxt('제갈량', False, False, False)
    stage.delay(5)
    stage.head(35).move(75, 218)
    stage.delay(5)
    stage.bottomTxt('군사를 이끌고 유비를 지원했다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('원군이 도착하여 위기에서 벗어난 유비군은', True, True, False)
    stage.delay(5)
    stage.bottomTxt('단숨에 수비에서 공격으로 전환하여', True, False, False)
    stage.delay(5)
    stage.head(160).hide()
    stage.delay(5)
    stage.head(32).move(11, 138)
    stage.delay(3)
    stage.head(32).move(11, 177)
    stage.delay(5)
    stage.bottomTxt('마침내 성도를 점령했다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            20,
            50,
            41,
            2],
        [
            23,
            56,
            41,
            2],
        [
            24,
            62,
            41,
            2],
        [
            21,
            68,
            41,
            2],
        [
            19,
            74,
            41,
            2]])
    stage.draw()
    stage.effectSound(204, 0)
    stage.delay(3)
    stage.info('며칠 뒤……', 3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_46.scene8.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_46.scene8.dialogue0002')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_46.scene8.dialogue0003')
    stage.unit(24).setAction(0)
    model.setFace(1)
    stage.say('@dialogue:R_46.scene8.dialogue0004')
    stage.unit(19).setAction(6)
    model.setFace(0)
    stage.say('@dialogue:R_46.scene8.dialogue0005')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_46.scene8.dialogue0006')
    stage.unit(19).setAction(0, 3)
    stage.say('@dialogue:R_46.scene8.dialogue0007')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_46.scene8.dialogue0008')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_46.scene8.dialogue0009')
    stage.say('@dialogue:R_46.scene8.dialogue0010')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_46.scene8.dialogue0011')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.say('@dialogue:R_46.scene8.dialogue0012')
    stage.effectSound(204, 255)
    stage.clsUnit()
    return False


def scene9():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 332, 98)
    stage.showHead(32, 23, 170)
    stage.delay(5)
    stage.bottomTxt('유비', False, False, False)
    stage.delay(5)
    stage.bottomTxt('촉의 광대한 영토를 얻었다.', True, False, False)
    stage.delay(10)
    stage.showHead(76, 569, 231)
    stage.delay(5)
    stage.bottomTxt('한편 손권은', True, True, False)
    stage.delay(5)
    stage.head(76).move(497, 212)
    stage.delay(5)
    stage.bottomTxt('그는 도읍을 오에서 건업으로 옮기고', True, False, False)
    stage.delay(5)
    stage.bottomTxt('조조와 유비에 맞서겠다는 결의를 보였다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('이리하여 조조의 위와', True, True, False)
    stage.delay(5)
    stage.bottomTxt('유비의 촉과', True, False, False)
    stage.delay(5)
    stage.bottomTxt('손권의 오가 천하를 셋으로 나누어 맞서게 되었다.', True, False, True)
    stage.bgSound(-1)
    stage.delay(3)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            20,
            50,
            41,
            2],
        [
            23,
            56,
            41,
            2],
        [
            24,
            62,
            41,
            2],
        [
            21,
            68,
            41,
            2],
        [
            19,
            74,
            41,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_46.scene9.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0002')
    stage.unit(20).setAction(0)
    model.setFace(2)
    stage.say('@dialogue:R_46.scene9.dialogue0003')
    model.setFace(0)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0004')
    stage.say('@dialogue:R_46.scene9.dialogue0005')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_46.scene9.dialogue0006')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_46.scene9.dialogue0007')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0008')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0009')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0010')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0011')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_46.scene9.dialogue0012')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.showUnit(17, 90, 55, 3)
    stage.effectSound(200, 255)
    stage.bgSound(17)
    stage.delay(3)
    stage.say('@dialogue:R_46.scene9.dialogue0013')
    stage.unit(17).move(46, 55, 3)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0014')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0015')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(0, 1)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0016')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0017')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0018')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0019')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_46.scene9.dialogue0020')
    stage.unit(17).setAction(0, 2)
    stage.say('@dialogue:R_46.scene9.dialogue0021')
    stage.unit(17).setAction(0, 1)
    stage.say('@dialogue:R_46.scene9.dialogue0022')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0023')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0024')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0025')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_46.scene9.dialogue0026')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(0, 3)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene9.dialogue0027')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(17).move(98, 55, 1)
    stage.unit(17).hide()
    stage.say('@dialogue:R_46.scene9.dialogue0028')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene10():
    stage.loadBg(2, 31)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('한중 교외  산길')
    stage.showUnits([
        [
            181,
            95,
            56,
            3],
        [
            17,
            95,
            48,
            3]])
    stage.unitsMove([
        [
            stage.unit(181),
            50,
            56,
            3],
        [
            stage.unit(17),
            50,
            48,
            3]])
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(0, 2)
    stage.say('@dialogue:R_46.scene10.dialogue0001')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene10.dialogue0002')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(0, 0)
    stage.say('@dialogue:R_46.scene10.dialogue0003')
    stage.showUnits([
        [
            187,
            95,
            48,
            3],
        [
            188,
            95,
            56,
            3]])
    stage.unitsMove([
        [
            stage.unit(187),
            85,
            48,
            3],
        [
            stage.unit(188),
            85,
            56,
            3]])
    stage.say('@dialogue:R_46.scene10.dialogue0004')
    stage.unit(181).setAction(0, 1)
    stage.say('@dialogue:R_46.scene10.dialogue0005')
    stage.unit(17).setAction(0, 1)
    stage.say('@dialogue:R_46.scene10.dialogue0006')
    stage.unitsMove([
        [
            stage.unit(187),
            60,
            48,
            3],
        [
            stage.unit(188),
            60,
            56,
            3]])
    stage.unit(187).setAction(0)
    stage.unit(188).setAction(0)
    stage.say('@dialogue:R_46.scene10.dialogue0007')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene10.dialogue0008')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_46.scene10.dialogue0009')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene10.dialogue0010')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_46.scene10.dialogue0011')
    stage.unit(17).setAction(3)
    stage.say('@dialogue:R_46.scene10.dialogue0012')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_46.scene10.dialogue0013')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_46.scene10.dialogue0014')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_46.scene10.dialogue0015')
    stage.unitsMove([
        [
            stage.unit(181),
            5,
            56,
            3],
        [
            stage.unit(17),
            5,
            48,
            3],
        [
            stage.unit(187),
            15,
            48,
            3],
        [
            stage.unit(188),
            15,
            56,
            3]])
    stage.unit(181).hide()
    stage.unit(17).hide()
    stage.unitsMove([
        [
            stage.unit(187),
            5,
            48,
            3],
        [
            stage.unit(188),
            5,
            56,
            3]])
    stage.unit(181).hide()
    stage.unit(181).hide()
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene11():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 334, 98)
    stage.showHead(140, 97, 78)
    stage.showHead(17, 161, 78)
    stage.bottomTxt('며칠 뒤,', False, False, False)
    stage.delay(8)
    stage.bottomTxt('초선은 장로를 설득하는 데 성공했다.', True, True, False)
    stage.delay(5)
    stage.head(140).hide()
    stage.delay(5)
    stage.bottomTxt('장로는 성문을 열고 항복했고', True, False, False)
    stage.delay(8)
    stage.head(17).move(97, 78)
    stage.head(0).move(161, 78)
    stage.delay(5)
    stage.bottomTxt('조조는 한중성에 들어갔다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 44)
    stage.showUnits([
        [
            0,
            53,
            35,
            2],
        [
            20,
            39,
            52,
            1],
        [
            23,
            39,
            58,
            1],
        [
            24,
            39,
            64,
            1],
        [
            21,
            39,
            70,
            1],
        [
            19,
            39,
            76,
            1],
        [
            1,
            69,
            52,
            3],
        [
            6,
            69,
            58,
            3],
        [
            17,
            69,
            64,
            3],
        [
            140,
            53,
            52]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('한중  임시 승상부')
    stage.say('@dialogue:R_46.scene11.dialogue0001')
    stage.unit(140).setAction(6)
    stage.say('@dialogue:R_46.scene11.dialogue0002')
    stage.unit(140).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0003')
    stage.unit(140).setAction(6)
    stage.say('@dialogue:R_46.scene11.dialogue0004')
    stage.unit(140).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0005')
    stage.unit(140).setAction(6)
    stage.say('@dialogue:R_46.scene11.dialogue0006')
    stage.unit(140).setAction(0)
    stage.delay(3)
    stage.unit(140).move(53, 95, 2)
    stage.unit(140).hide()
    stage.showUnit(181, 53, 95)
    stage.delay(8)
    stage.unit(181).move(53, 90, 0)
    stage.unit(181).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0007')
    stage.unit(181).move(53, 95, 2)
    stage.unit(181).hide()
    stage.delay(10)
    stage.showUnits([
        [
            9,
            53,
            90],
        [
            181,
            50,
            96],
        [
            182,
            56,
            96]])
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(9),
            53,
            48,
            0],
        [
            stage.unit(181),
            50,
            54,
            0],
        [
            stage.unit(182),
            56,
            54,
            0]])
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_46.scene11.dialogue0008')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0009')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_46.scene11.dialogue0010')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_46.scene11.dialogue0011')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_46.scene11.dialogue0012')
    model.setFace(3)
    stage.say('@dialogue:R_46.scene11.dialogue0013')
    model.setFace(0)
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_46.scene11.dialogue0014')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_46.scene11.dialogue0015')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0016')
    stage.unitsMove([
        [
            stage.unit(181),
            50,
            48,
            1],
        [
            stage.unit(182),
            56,
            48,
            3]])
    stage.delay(3)
    stage.unit(9).setAction(0, 2)
    stage.say('@dialogue:R_46.scene11.dialogue0017')
    stage.unit(9).move(53, 51, 2)
    stage.unit(9).setAction(0)
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
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(9),
            53,
            98,
            2],
        [
            stage.unit(181),
            50,
            95,
            2],
        [
            stage.unit(182),
            56,
            95,
            2]])
    stage.unit(181).hide()
    stage.unit(182).hide()
    stage.unit(9).hide()
    stage.delay(10)
    stage.showUnit(183, 53, 95)
    stage.unit(183).move(53, 90, 0)
    stage.unit(183).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0018')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_46.scene11.dialogue0019')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_46.scene11.dialogue0020')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0021')
    stage.unit(6).setAction(0, 0)
    stage.say('@dialogue:R_46.scene11.dialogue0022')
    stage.unit(6).setAction(6)
    stage.say('@dialogue:R_46.scene11.dialogue0023')
    stage.unit(6).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0024')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_46.scene11.dialogue0025')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(6).move(75, 58, 1)
    stage.delay(3)
    stage.unit(6).move(75, 95, 2)
    stage.unit(6).hide()
    stage.unit(183).move(53, 95, 2)
    stage.unit(183).hide()
    stage.delay(10)
    stage.effectSound(200, 255)
    stage.bgSound(18)
    stage.delay(3)
    stage.showUnit(181, 53, 95)
    stage.unit(181).move(53, 90, 0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_46.scene11.dialogue0026')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_46.scene11.dialogue0027')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0028')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(53, 95, 2)
    stage.unit(181).hide()
    stage.delay(3)
    stage.showUnit(4, 53, 95)
    stage.unit(4).move(53, 52, 0)
    stage.unit(4).setAction(0)
    stage.delay(3)
    stage.unit(4).setAction(6)
    stage.say('@dialogue:R_46.scene11.dialogue0029')
    stage.unit(4).setAction(0)
    stage.say('@dialogue:R_46.scene11.dialogue0030')
    stage.unit(4).setAction(6)
    stage.say('@dialogue:R_46.scene11.dialogue0031')
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
    stage.clsUnit()
    return False


def scene12():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 153, 63)
    stage.bottomTxt('조홍', False, False, False)
    stage.delay(5)
    stage.showHead(12, 83, 74)
    stage.delay(5)
    stage.bottomTxt('위와 촉의 국경을 지키는 정군산이었다.', True, False, False)
    stage.delay(8)
    stage.showHead(32, 8, 178)
    stage.delay(5)
    stage.bottomTxt('유비군이', True, True, False)
    stage.delay(5)
    stage.head(32).move(83, 154)
    stage.delay(5)
    stage.bottomTxt('군사를 일으켜 정군산을 공격하자', True, False, False)
    stage.delay(10)
    stage.bottomTxt('조조', True, True, False)
    stage.delay(5)
    stage.head(0).hide()
    stage.showHead(6, 153, 63)
    stage.delay(5)
    stage.bottomTxt('즉시 하후연을 보내', True, False, False)
    stage.delay(5)
    stage.head(6).move(147, 114)
    stage.delay(5)
    stage.bottomTxt('지원한다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            35,
            42,
            52,
            1],
        [
            33,
            42,
            58,
            1],
        [
            34,
            42,
            64,
            1],
        [
            36,
            67,
            52,
            3],
        [
            57,
            67,
            58,
            3],
        [
            37,
            67,
            64,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.info('바로 그때…….', 3)
    stage.setStageName('정군산  유비군 본진')
    stage.say('@dialogue:R_46.scene12.dialogue0001')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_46.scene12.dialogue0002')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_46.scene12.dialogue0003')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_46.scene12.dialogue0004')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_46.scene12.dialogue0005')
    stage.unit(32).setAction(0, 0)
    stage.say('@dialogue:R_46.scene12.dialogue0006')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_46.scene12.dialogue0007')
    stage.unit(32).setAction(0, 2)
    stage.say('@dialogue:R_46.scene12.dialogue0008')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_46.scene12.dialogue0009')
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_46.scene12.dialogue0010')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_46.scene12.dialogue0011')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_46.scene12.dialogue0012')
    stage.unit(33).setAction(3)
    stage.say('@dialogue:R_46.scene12.dialogue0013')
    stage.unit(33).setAction(0)
    stage.say('@dialogue:R_46.scene12.dialogue0014')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_46.scene12.dialogue0015')
    stage.unit(35).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_46.scene12.dialogue0016')
    stage.effectSound(200, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(12, 83, 74)
    stage.showHead(6, 147, 114)
    stage.showHead(32, 83, 154)
    stage.showHead(3, 277, 238)
    stage.bottomTxt('유비', False, False, False)
    stage.delay(5)
    stage.head(32).move(83, 234)
    stage.showHead(35, 83, 154)
    stage.delay(5)
    stage.bottomTxt('한중의 싸움을 제갈량에게 맡기고,', True, False, False)
    stage.delay(5)
    stage.head(32).move(213, 238)
    stage.delay(5)
    stage.bottomTxt('장비를 데리고 형주로 돌아갔다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene13():
    stage.loadBg(2, 37)
    if vars[311]:
        stage.bgSound(19)
        stage.delay(3)
        stage.showUnit(9, 63, 54, 1)
        stage.draw()
        stage.setStageName('한중  방덕의 거처')
        stage.delay(5)
        stage.say('@dialogue:R_46.scene13.dialogue0001')
        stage.delay(5)
        stage.showUnit(23, 20, 54, 1)
        stage.delay(5)
        stage.say('@dialogue:R_46.scene13.dialogue0002')
        stage.unit(9).setAction(0, 3)
        stage.say('@dialogue:R_46.scene13.dialogue0003')
        stage.unit(9).move(50, 54, 3)
        stage.unit(9).setAction(0)
        stage.delay(3)
        stage.say('@dialogue:R_46.scene13.dialogue0004')
        stage.unit(23).move(40, 54, 1)
        stage.unit(23).setAction(0)
        stage.delay(3)
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_46.scene13.dialogue0005')
        stage.unit(23).setAction(0)
        stage.say('@dialogue:R_46.scene13.dialogue0006')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_46.scene13.dialogue0007')
        stage.unit(23).setAction(0)
        stage.say('@dialogue:R_46.scene13.dialogue0008')
        stage.unit(9).setAction(6)
        stage.say('@dialogue:R_46.scene13.dialogue0009')
        stage.unit(9).setAction(0)
        stage.say('@dialogue:R_46.scene13.dialogue0010')
        stage.unit(23).setAction(0, 2)
        stage.say('@dialogue:R_46.scene13.dialogue0011')
        stage.unit(23).setAction(0, 1)
        stage.say('@dialogue:R_46.scene13.dialogue0012')
        stage.unit(9).setAction(6)
        stage.say('@dialogue:R_46.scene13.dialogue0013')
        stage.unit(9).setAction(0)
        stage.say('@dialogue:R_46.scene13.dialogue0014')
        stage.unit(9).setAction(0, 2)
        stage.say('@dialogue:R_46.scene13.dialogue0015')
        stage.say('@dialogue:R_46.scene13.dialogue0016')
        stage.delay(10)
        stage.bgSound(-1)
        stage.clsUnit()
    if not vars[311]:
        stage.bgSound(19)
        stage.delay(3)
        stage.showUnit(9, 63, 54, 1)
        stage.draw()
        stage.setStageName('한중  방덕의 거처')
        stage.say('@dialogue:R_46.scene13.dialogue0017')
        stage.delay(5)
        stage.showUnit(21, 20, 54, 1)
        stage.delay(5)
        stage.say('@dialogue:R_46.scene13.dialogue0018')
        stage.unit(9).setAction(0, 3)
        stage.say('@dialogue:R_46.scene13.dialogue0019')
        stage.unit(9).move(50, 54, 3)
        stage.unit(9).setAction(0)
        stage.delay(3)
        stage.say('@dialogue:R_46.scene13.dialogue0020')
        stage.unit(21).move(40, 54, 1)
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_46.scene13.dialogue0021')
        stage.unit(21).setAction(0)
        stage.say('@dialogue:R_46.scene13.dialogue0022')
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_46.scene13.dialogue0023')
        stage.unit(21).setAction(0)
        stage.say('@dialogue:R_46.scene13.dialogue0024')
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_46.scene13.dialogue0025')
        stage.unit(21).setAction(0)
        stage.say('@dialogue:R_46.scene13.dialogue0026')
        stage.unit(9).setAction(0, 2)
        stage.say('@dialogue:R_46.scene13.dialogue0027')
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_46.scene13.dialogue0028')
        stage.delay(10)
        stage.bgSound(-1)
        stage.clsUnit()
    return False


def scene14():
    stage.bgSound(15)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            6,
            54,
            80],
        [
            7,
            44,
            64,
            1],
        [
            9,
            44,
            58,
            1]])
    stage.draw()
    stage.setStageName('한중@조조군 본진')
    stage.say('@dialogue:R_46.scene14.dialogue0001')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        6,
        7,
        12,
        9], [
        0])
    gvars[4076] = 0
    gvars[4057] = 46
    stage.setBattlePos([
        [
            23,
            16],
        [
            22,
            17],
        [
            6,
            4],
        [
            4,
            6],
        [
            12,
            16],
        [
            19,
            26],
        [
            21,
            16],
        [
            23,
            18],
        [
            12,
            15],
        [
            19,
            25],
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


def scene15():
    if stage.battleTest():
        stage.say('@dialogue:R_46.scene15.dialogue0001')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(7):
        stage.say('@dialogue:R_46.scene15.dialogue0002')
    if stage.unitClickTest(9):
        stage.say('@dialogue:R_46.scene15.dialogue0003')
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
