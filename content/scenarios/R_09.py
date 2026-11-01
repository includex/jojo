# Source Generated with Decompyle++
# File: R_09.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 67)
    stage.showUnits([
        [
            2,
            44,
            98],
        [
            126,
            54,
            98]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('헌제')
    stage.setStageName('복양  교외 산길')
    stage.unitsMove([
        [
            stage.unit(2),
            44,
            86,
            0],
        [
            stage.unit(126),
            54,
            86,
            0]])
    stage.showUnit(119, 49, 98)
    stage.unitsMove([
        [
            stage.unit(2),
            44,
            40,
            0],
        [
            stage.unit(126),
            54,
            40,
            0],
        [
            stage.unit(119),
            49,
            52,
            0]])
    stage.delay(3)
    stage.unit(119).setAction(0, 2)
    stage.delay(3)
    stage.unit(119).setAction(2, 2)
    stage.say('@dialogue:R_09.scene0.dialogue0001')
    stage.unit(2).setAction(0, 1)
    stage.unit(126).setAction(0, 3)
    stage.delay(5)
    stage.unit(126).setAction(0, 2)
    stage.say('@dialogue:R_09.scene0.dialogue0002')
    stage.unit(119).setAction(0, 0)
    stage.say('@dialogue:R_09.scene0.dialogue0003')
    stage.say('@dialogue:R_09.scene0.dialogue0004')
    stage.unit(119).setAction(0, 2)
    stage.say('@dialogue:R_09.scene0.dialogue0005')
    stage.unit(119).setAction(0, 0)
    stage.say('@dialogue:R_09.scene0.dialogue0006')
    stage.unit(2).setAction(0, 2)
    stage.say('@dialogue:R_09.scene0.dialogue0007')
    stage.unitsMove([
        [
            stage.unit(2),
            44,
            4,
            0],
        [
            stage.unit(126),
            54,
            4,
            0],
        [
            stage.unit(119),
            49,
            12,
            0]])
    stage.unit(2).hide()
    stage.unit(126).hide()
    stage.unit(119).move(49, 4, 0)
    stage.unit(119).hide()
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 416, 67)
    stage.showHead(119, 480, 67)
    stage.showHead(32, 459, 152)
    stage.bottomTxt('조조에게 패한 여포는 여러 곳을 전전하며 달아났고,', False, False, False)
    stage.delay(5)
    stage.head(119).move(523, 67)
    stage.head(119).move(523, 152)
    stage.bottomTxt('마침내 서주의 유비 휘하에 의탁했다.', True, False, True)
    stage.bottomTxt('이때,', False, True, False)
    stage.delay(5)
    stage.showHead(131, 220, 82)
    stage.bottomTxt('이각과 곽사는 권력과 이익을 다투며', True, True, False)
    stage.showHead(129, 156, 82)
    stage.bottomTxt('마침내 무력 충돌이 일어났다.', True, False, False)
    stage.delay(5)
    stage.bottomTxt('장안에 다시 전쟁의 불길이 올랐다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 15)
    stage.showUnits([
        [
            149,
            27,
            55,
            1],
        [
            23,
            46,
            55,
            3,
            6],
        [
            192,
            46,
            43,
            2],
        [
            193,
            52,
            43,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('장안  궁궐')
    stage.say('@dialogue:R_09.scene1.dialogue0001')
    stage.unit(192).setAction(3)
    stage.say('@dialogue:R_09.scene1.dialogue0002')
    stage.unit(192).setAction(0)
    stage.delay(5)
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_09.scene1.dialogue0003')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_09.scene1.dialogue0004')
    stage.unit(149).setAction(0, 3)
    stage.say('@dialogue:R_09.scene1.dialogue0005')
    stage.unit(149).setAction(0, 1)
    stage.say('@dialogue:R_09.scene1.dialogue0006')
    stage.unitsMove([
        [
            stage.unit(193),
            91,
            43,
            1],
        [
            stage.unit(192),
            85,
            43,
            1]])
    stage.unit(193).hide()
    stage.unit(192).hide()
    stage.say('@dialogue:R_09.scene1.dialogue0007')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_09.scene1.dialogue0008')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_09.scene1.dialogue0009')
    stage.showUnits([
        [
            192,
            85,
            43,
            3],
        [
            193,
            91,
            43,
            3]])
    stage.unitsMove([
        [
            stage.unit(192),
            66,
            43,
            3],
        [
            stage.unit(193),
            72,
            43,
            3]])
    stage.unit(192).setAction(0)
    stage.unit(193).setAction(0)
    stage.say('@dialogue:R_09.scene1.dialogue0010')
    stage.unit(149).move(27, 48, 0)
    stage.delay(1)
    stage.unit(149).move(66, 48, 1)
    stage.delay(5)
    stage.unit(192).setAction(0, 2)
    stage.unit(193).setAction(0, 2)
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(149),
            92,
            48,
            1],
        [
            stage.unit(192),
            85,
            43,
            1],
        [
            stage.unit(193),
            91,
            43,
            1]])
    stage.unit(192).hide()
    stage.unit(193).hide()
    stage.unit(149).hide()
    stage.unit(23).setAction(0, 3)
    stage.delay(3)
    stage.unit(23).setAction(0, 1)
    stage.say('@dialogue:R_09.scene1.dialogue0011')
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(0, 9)
    stage.draw()
    stage.info('이각과 곽사가 다투는 가운데 곽사군의 장수\n서황이 눈부신 활약을 펼쳐 전세는 곽사군에 크게 유리해졌다.')
    stage.info('그러나 마지막 순간에 가후가 헌제를 풀어 주어\n헌제는 장안을 빠져나갔다. 이를 안 이각과 곽사는\n즉시 싸움을 멈추고 힘을 합쳐 헌제를 추격했다.')
    stage.bgSound(-1)
    return False


def scene2():
    stage.loadBg(2, 59)
    stage.showUnit(192, 49, 95)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('장안 교외  산길')
    stage.unit(192).move(49, 85, 0)
    stage.showUnit(149, 49, 95)
    stage.unitsMove([
        [
            stage.unit(192),
            49,
            75,
            0],
        [
            stage.unit(149),
            49,
            85,
            0]])
    stage.showUnits([
        [
            193,
            44,
            95],
        [
            194,
            54,
            95]])
    stage.unitsMove([
        [
            stage.unit(192),
            49,
            30,
            0],
        [
            stage.unit(149),
            49,
            40,
            0],
        [
            stage.unit(193),
            44,
            50,
            0],
        [
            stage.unit(194),
            54,
            50,
            0]])
    stage.unit(149).setAction(0, 0)
    stage.delay(5)
    stage.say('@dialogue:R_09.scene2.dialogue0001')
    stage.unit(192).setAction(0, 2)
    stage.delay(3)
    stage.unit(192).setAction(3, 2)
    stage.say('@dialogue:R_09.scene2.dialogue0002')
    stage.unit(192).setAction(0, 2)
    stage.unit(193).move(44, 40, 0)
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(0, 1)
    stage.say('@dialogue:R_09.scene2.dialogue0003')
    stage.say('@dialogue:R_09.scene2.dialogue0004')
    stage.unit(149).setAction(0, 1)
    stage.say('@dialogue:R_09.scene2.dialogue0005')
    stage.unit(192).setAction(3, 2)
    stage.say('@dialogue:R_09.scene2.dialogue0006')
    stage.unit(192).setAction(0, 2)
    stage.delay(3)
    stage.unit(194).setAction(6)
    stage.say('@dialogue:R_09.scene2.dialogue0007')
    stage.unit(149).move(54, 40, 1)
    stage.unit(149).setAction(0)
    stage.delay(3)
    stage.unit(149).setAction(0, 2)
    stage.say('@dialogue:R_09.scene2.dialogue0008')
    stage.unit(149).setAction(0, 1)
    stage.say('@dialogue:R_09.scene2.dialogue0009')
    stage.unit(194).setAction(0, 0)
    stage.delay(3)
    stage.unit(194).move(44, 50, 3)
    stage.delay(3)
    stage.unit(194).setAction(0, 0)
    stage.say('@dialogue:R_09.scene2.dialogue0010')
    stage.unit(193).setAction(0, 2)
    stage.say('@dialogue:R_09.scene2.dialogue0011')
    stage.unitsMove([
        [
            stage.unit(193),
            44,
            4,
            0],
        [
            stage.unit(194),
            44,
            14,
            0]])
    stage.unit(193).hide()
    stage.unit(194).move(44, 4, 0)
    stage.unit(194).hide()
    stage.unit(192).move(49, 40, 1)
    stage.say('@dialogue:R_09.scene2.dialogue0012')
    stage.unit(192).setAction(0, 3)
    stage.say('@dialogue:R_09.scene2.dialogue0013')
    stage.unit(149).setAction(0, 3)
    stage.delay(3)
    stage.unit(149).move(51, 40, 3)
    stage.unit(149).setAction(0)
    stage.say('@dialogue:R_09.scene2.dialogue0014')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            194,
            52,
            31,
            2,
            5],
        [
            101,
            49,
            46],
        [
            106,
            57,
            46]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('며칠 뒤…….', 3)
    stage.setStageName('신도  원소 저택')
    stage.say('@dialogue:R_09.scene3.dialogue0001')
    stage.unit(194).setAction(0)
    stage.say('@dialogue:R_09.scene3.dialogue0002')
    stage.say('@dialogue:R_09.scene3.dialogue0003')
    stage.delay(3)
    stage.unit(106).setAction(6)
    stage.say('@dialogue:R_09.scene3.dialogue0004')
    stage.say('@dialogue:R_09.scene3.dialogue0005')
    stage.say('@dialogue:R_09.scene3.dialogue0006')
    stage.say('@dialogue:R_09.scene3.dialogue0007')
    stage.unit(194).setAction(0, 0)
    stage.say('@dialogue:R_09.scene3.dialogue0008')
    stage.unit(194).move(45, 31, 3)
    stage.unit(194).setAction(0)
    stage.delay(1)
    stage.unit(194).move(45, 90, 2)
    stage.unit(194).hide()
    stage.delay(3)
    stage.unit(101).setAction(0, 1)
    stage.say('@dialogue:R_09.scene3.dialogue0009')
    stage.unit(106).setAction(0, 3)
    stage.say('@dialogue:R_09.scene3.dialogue0010')
    stage.unit(106).setAction(0, 2)
    stage.say('@dialogue:R_09.scene3.dialogue0011')
    stage.unit(101).setAction(0, 2)
    stage.say('@dialogue:R_09.scene3.dialogue0012')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    model.initLocalVar()
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 12)
    stage.showUnits([
        [
            193,
            30,
            54,
            1],
        [
            1,
            50,
            41,
            3,
            6],
        [
            16,
            56,
            41,
            3,
            6],
        [
            20,
            50,
            68,
            3,
            6],
        [
            22,
            56,
            68,
            3,
            6],
        [
            21,
            62,
            68,
            3,
            6]])
    if not vars[706]:
        stage.showUnit(18, 62, 41, 3, 6)
    stage.showUnit(0, 50, 54, 3, 6)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('복양  조조 저택')
    stage.say('@dialogue:R_09.scene4.dialogue0001')
    stage.unit(193).move(30, 48, 0)
    stage.delay(1)
    stage.unit(193).move(95, 48, 1)
    stage.unit(193).hide()
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0)
    stage.unit(21).setAction(0)
    stage.unit(20).setAction(0)
    stage.unit(22).setAction(0)
    if not vars[706]:
        stage.unit(18).setAction(0)
    stage.unit(16).setAction(0)
    stage.delay(3)
    stage.unit(0).move(50, 48, 0)
    stage.delay(1)
    stage.unit(0).move(30, 48, 3)
    stage.delay(1)
    stage.unit(0).move(30, 54, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_09.scene4.dialogue0002')
    stage.unit(20).setAction(6, 3)
    stage.say('@dialogue:R_09.scene4.dialogue0003')
    if vars[706]:
        stage.effectSound(202, 255)
        stage.delay(3)
        stage.bgSound(18)
        stage.delay(3)
        stage.showUnit(18, 95, 54, 3)
        stage.say('@dialogue:R_09.scene4.dialogue0004')
        stage.unit(1).setAction(0, 2)
        stage.unit(16).setAction(0, 2)
        stage.unit(20).setAction(0, 0)
        stage.unit(22).setAction(0, 0)
        stage.unit(21).setAction(0, 0)
        stage.unit(18).move(50, 54, 3)
        stage.say('@dialogue:R_09.scene4.dialogue0005')
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_09.scene4.dialogue0006')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(18).setAction(6)
        stage.say('@dialogue:R_09.scene4.dialogue0007')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_09.scene4.dialogue0008')
        stage.say('@dialogue:R_09.scene4.dialogue0009')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_09.scene4.dialogue0010')
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_09.scene4.dialogue0011')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(0, 3)
        stage.say('@dialogue:R_09.scene4.dialogue0012')
        stage.say('@dialogue:R_09.scene4.dialogue0013')
        stage.say('@dialogue:R_09.scene4.dialogue0014')
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
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_09.scene4.dialogue0015')
    stage.bgSound(-1)
    stage.effectSound(202, 255)
    stage.delay(3)
    stage.bgSound(0)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, -1)
    stage.draw()
    stage.showHead(149, 246, 89)
    stage.delay(5)
    stage.bottomTxt('헌제가 장안을 빠져나간 뒤,', False, False, False)
    stage.delay(5)
    stage.showHead(131, 173, 46)
    stage.delay(5)
    stage.bottomTxt('이각', True, False, False)
    stage.delay(5)
    stage.showHead(129, 173, 126)
    stage.delay(5)
    stage.bottomTxt('곽사와 함께 끈질기게 뒤쫓았다.', True, False, False)
    stage.delay(8)
    stage.showHead(101, 403, 7)
    stage.delay(5)
    stage.bottomTxt('하북의 강자 원소는 헌제의 구원 요청을 거절했다.', True, True, False)
    stage.delay(8)
    stage.showHead(0, 445, 91)
    stage.delay(5)
    stage.bottomTxt('조조는 천자를 모실 좋은 기회를 놓치지 않고', True, True, False)
    stage.delay(6)
    stage.head(0).move(310, 89)
    stage.delay(5)
    stage.bottomTxt('헌제를 구원하러 나섰다.', True, False, True)
    return False


def scene5():
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
            52,
            1]])
    if not vars[706] and not vars[707]:
        stage.showUnits([
            [
                16,
                38,
                46,
                1],
            [
                15,
                38,
                40,
                1]])
    stage.showUnits([
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
    stage.setStageName('장안 근교  조조군 본진')
    if vars[706]:
        stage.say('@dialogue:R_09.scene5.dialogue0001')
        stage.unit(20).setAction(0, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0002')
        stage.showUnit(16, 49, 16, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0003')
        stage.unit(20).setAction(0, 3)
        stage.unit(16).move(49, 46, 2)
        stage.delay(3)
        model.setFace(2)
        stage.unit(0).setAction(2, 0)
        stage.say('@dialogue:R_09.scene5.dialogue0004')
        stage.unit(16).setAction(6, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0005')
        model.setFace(0)
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_09.scene5.dialogue0006')
        stage.unit(16).setAction(0, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0007')
        stage.unit(16).setAction(0, 0)
        stage.say('@dialogue:R_09.scene5.dialogue0008')
        stage.bgSound(18)
        stage.delay(3)
        stage.showUnit(15, 49, 16, 2)
        stage.unitsMove([
            [
                stage.unit(16),
                38,
                46,
                1],
            [
                stage.unit(15),
                49,
                40,
                2]])
        stage.delay(3)
        stage.unit(16).setAction(0, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0009')
        stage.unit(15).setAction(11, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0010')
        stage.unit(16).setAction(0, 1)
        stage.say('@dialogue:R_09.scene5.dialogue0011')
        stage.unit(16).setAction(0, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0012')
        model.setFace(3)
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_09.scene5.dialogue0013')
        stage.say('@dialogue:R_09.scene5.dialogue0014')
        stage.unit(16).setAction(0, 1)
        stage.say('@dialogue:R_09.scene5.dialogue0015')
        stage.unit(15).setAction(0, 2)
        stage.delay(3)
        stage.unit(15).move(38, 40, 1)
        model.setFace(0)
        stage.unit(0).setAction(0)
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
    if vars[707] and not vars[706]:
        stage.say('@dialogue:R_09.scene5.dialogue0016')
        stage.unit(20).setAction(0, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0017')
        stage.showUnit(16, 49, 16, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0018')
        stage.unit(20).setAction(0, 3)
        stage.unit(16).move(49, 46, 2)
        stage.delay(3)
        model.setFace(2)
        stage.unit(0).setAction(2, 0)
        stage.say('@dialogue:R_09.scene5.dialogue0019')
        stage.unit(16).setAction(6, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0020')
        model.setFace(0)
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_09.scene5.dialogue0021')
        stage.unit(16).setAction(0, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0022')
        stage.unit(16).setAction(0, 0)
        stage.say('@dialogue:R_09.scene5.dialogue0023')
        stage.bgSound(18)
        stage.delay(3)
        stage.showUnit(15, 49, 16, 2)
        stage.unitsMove([
            [
                stage.unit(16),
                38,
                46,
                1],
            [
                stage.unit(15),
                49,
                40,
                2]])
        stage.delay(3)
        stage.unit(16).setAction(0, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0024')
        stage.unit(15).setAction(11, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0025')
        stage.unit(16).setAction(0, 1)
        stage.say('@dialogue:R_09.scene5.dialogue0026')
        stage.unit(16).setAction(0, 2)
        stage.say('@dialogue:R_09.scene5.dialogue0027')
        model.setFace(3)
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_09.scene5.dialogue0028')
        stage.say('@dialogue:R_09.scene5.dialogue0029')
        stage.unit(16).setAction(0, 1)
        stage.say('@dialogue:R_09.scene5.dialogue0030')
        stage.unit(15).setAction(0, 2)
        stage.delay(3)
        stage.unit(15).move(38, 40, 1)
        model.setFace(0)
        stage.unit(0).setAction(0)
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
    stage.bgSound(0)
    stage.delay(3)
    stage.say('@dialogue:R_09.scene5.dialogue0031')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 9
    stage.setBattlePos([
        [
            17,
            11],
        [
            16,
            11],
        [
            18,
            12],
        [
            17,
            9],
        [
            19,
            9],
        [
            18,
            7],
        [
            17,
            4],
        [
            17,
            2],
        [
            19,
            3],
        [
            19,
            2],
        [
            18,
            1],
        [
            17,
            0],
        [
            18,
            2],
        [
            16,
            1],
        [
            19,
            0]])
    return False


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_09.scene6.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_09.scene6.dialogue0002')
    if stage.unitClickTest(22):
        if vars[706]:
            stage.say('@dialogue:R_09.scene6.dialogue0003')
        if not vars[706]:
            stage.say('@dialogue:R_09.scene6.dialogue0004')
    if stage.unitClickTest(21):
        if vars[706]:
            stage.say('@dialogue:R_09.scene6.dialogue0005')
        if vars[707] and not vars[706]:
            stage.say('@dialogue:R_09.scene6.dialogue0006')
        if not vars[706] and not vars[707]:
            stage.say('@dialogue:R_09.scene6.dialogue0007')
    if stage.unitClickTest(16):
        if vars[706]:
            stage.say('@dialogue:R_09.scene6.dialogue0008')
        if vars[707] and not vars[706]:
            stage.say('@dialogue:R_09.scene6.dialogue0009')
        if not vars[706] and not vars[707]:
            stage.say('@dialogue:R_09.scene6.dialogue0010')
    if stage.unitClickTest(15):
        if vars[706]:
            stage.say('@dialogue:R_09.scene6.dialogue0011')
        if vars[707] and not vars[706]:
            stage.say('@dialogue:R_09.scene6.dialogue0012')
        if not vars[706] and not vars[707]:
            stage.say('@dialogue:R_09.scene6.dialogue0013')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_09.scene6.dialogue0014')
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
