# Source Generated with Decompyle++
# File: R_32.marshal (Python 3.9)


def scene0():
    if model.ambition() < 16:
        model.initLocalVar()
        if vars[1002]:
            stage.infoTransfer(18, '')
        stage.clsUnit()
        stage.setMenuVisible(False)
        stage.jumpScene(91)
    if model.ambition() >= 16:
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
        stage.setEventName('한중을 공격한다')
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
        stage.say('@dialogue:R_32.scene0.dialogue0001')
        stage.unit(57).setAction(0)
        stage.delay(3)
        stage.unit(57).setAction(0, 0)
        stage.say('@dialogue:R_32.scene0.dialogue0002')
        stage.unit(9).setAction(6)
        stage.say('@dialogue:R_32.scene0.dialogue0003')
        stage.unit(9).setAction(0)
        stage.say('@dialogue:R_32.scene0.dialogue0004')
        stage.unit(57).setAction(0, 2)
        stage.delay(3)
        stage.unit(57).setAction(2)
        stage.say('@dialogue:R_32.scene0.dialogue0005')
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
        return True


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
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_32.scene1.dialogue0001')
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_32.scene1.dialogue0002')
    stage.unit(182).setAction(11)
    stage.say('@dialogue:R_32.scene1.dialogue0003')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_32.scene1.dialogue0004')
    stage.unit(0).move(68, 50, 0)
    stage.unit(0).setAction(0)
    stage.unit(181).setAction(0)
    stage.unit(182).setAction(0)
    stage.say('@dialogue:R_32.scene1.dialogue0005')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_32.scene1.dialogue0006')
    model.setFace(3)
    stage.say('@dialogue:R_32.scene1.dialogue0007')
    model.setFace(0)
    stage.say('@dialogue:R_32.scene1.dialogue0008')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_32.scene1.dialogue0009')
    stage.say('@dialogue:R_32.scene1.dialogue0010')
    stage.effectSound(203, 255)
    stage.bgSound(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.say('@dialogue:R_32.scene1.dialogue0011')
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_32.scene1.dialogue0012')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_32.scene1.dialogue0013')
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
            40,
            2],
        [
            23,
            56,
            40,
            2],
        [
            24,
            62,
            40,
            2],
        [
            21,
            68,
            40,
            2],
        [
            19,
            74,
            40,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.info('열흘 뒤………', 3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_32.scene2.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0002')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0003')
    model.setFace(1)
    stage.say('@dialogue:R_32.scene2.dialogue0004')
    stage.unit(24).setAction(0)
    model.setFace(2)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_32.scene2.dialogue0005')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_32.scene2.dialogue0006')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0007')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_32.scene2.dialogue0008')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0009')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_32.scene2.dialogue0010')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_32.scene2.dialogue0011')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0012')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0013')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0014')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0015')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_32.scene2.dialogue0016')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_32.scene2.dialogue0017')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_32.scene2.dialogue0018')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 335, 97)
    stage.showHead(32, 293, 225)
    stage.bottomTxt('유비는 제갈량의 천하삼분지계를 받아들여,', False, False, False)
    stage.delay(5)
    stage.showHead(160, 26, 172)
    stage.delay(8)
    stage.head(32).move(90, 172)
    stage.delay(5)
    stage.bottomTxt('서천의 유장을 공격하여,', True, False, False)
    stage.delay(8)
    stage.bottomTxt('도중에 비록……', True, True, False)
    stage.delay(5)
    stage.showHead(90, 152, 152)
    stage.delay(8)
    stage.head(90).hide()
    stage.delay(5)
    stage.bottomTxt('부군사 방통을 잃었으며', True, False, False)
    stage.delay(5)
    stage.head(160).hide()
    stage.delay(5)
    stage.head(32).move(26, 172)
    stage.delay(5)
    stage.bottomTxt('끝내 서천을 차지했다.', True, False, True)
    stage.showHead(76, 572, 238)
    stage.delay(5)
    stage.bottomTxt('한편 손권은', False, True, False)
    stage.delay(8)
    stage.head(76).move(501, 216)
    stage.delay(5)
    stage.bottomTxt('그는 도읍을 오에서 건업으로 옮기고', True, False, False)
    stage.delay(5)
    stage.bottomTxt('조조와 유비에 맞서겠다는 결의를 드러냈다.', True, True, False)
    stage.delay(10)
    stage.bottomTxt('이 형세를 본 조조는 먼저 유비를 없애기로 결심했다.', True, True, True)
    stage.delay(5)
    stage.bottomTxt('그 첫걸음으로,', True, True, False)
    stage.delay(5)
    stage.showHead(140, 129, 87)
    stage.delay(5)
    stage.head(0).move(193, 87)
    stage.delay(8)
    stage.bottomTxt('바로 장로가 다스리는 한중으로 진군하는 것이었다.', True, False, True)
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
        [
            1,
            44,
            64,
            1],
        [
            15,
            44,
            58,
            1],
        [
            7,
            44,
            52,
            1],
        [
            20,
            64,
            64,
            3],
        [
            19,
            64,
            58,
            3],
        [
            21,
            64,
            52,
            3]])
    stage.draw()
    stage.setStageName('한중  조조군 본진')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 13, [
        15], [
        2,
        10,
        11])
    gvars[4076] = 0
    gvars[4057] = 32
    stage.setBattlePos([
        [
            2,
            5],
        [
            3,
            4],
        [
            1,
            6],
        [
            1,
            5],
        [
            2,
            4],
        [
            0,
            5],
        [
            1,
            4],
        [
            2,
            3],
        [
            0,
            5],
        [
            1,
            3],
        [
            0,
            4],
        [
            0,
            3],
        [
            1,
            2],
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
        stage.say('@dialogue:R_32.scene4.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_32.scene4.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_32.scene4.dialogue0003')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_32.scene4.dialogue0004')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_32.scene4.dialogue0005')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_32.scene4.dialogue0006')
    if stage.unitClickTest(7):
        stage.say('@dialogue:R_32.scene4.dialogue0007')
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
