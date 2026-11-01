# Source Generated with Decompyle++
# File: R_05.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(18)
    stage.delay(3)
    stage.loadBg(2, 12)
    stage.showUnits([
        [
            0,
            29,
            54,
            1],
        [
            20,
            48,
            41,
            2],
        [
            24,
            54,
            41,
            2],
        [
            1,
            48,
            68],
        [
            22,
            46,
            52,
            3],
        [
            21,
            46,
            58,
            3]])
    stage.draw()
    stage.setEventName('아버지의 죽음')
    stage.setStageName('복양  조조 저택')
    stage.delay(5)
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_05.scene0.dialogue0001')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_05.scene0.dialogue0002')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_05.scene0.dialogue0003')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_05.scene0.dialogue0004')
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_05.scene0.dialogue0005')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_05.scene0.dialogue0006')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_05.scene0.dialogue0007')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_05.scene0.dialogue0008')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_05.scene0.dialogue0009')
    stage.unit(21).setAction(6, 3)
    stage.say('@dialogue:R_05.scene0.dialogue0010')
    stage.unit(21).setAction(0, 3)
    stage.delay(3)
    stage.unit(22).setAction(6, 3)
    stage.say('@dialogue:R_05.scene0.dialogue0011')
    stage.unit(22).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_05.scene0.dialogue0012')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_05.scene0.dialogue0013')
    stage.unit(0).setAction(5, 1)
    stage.say('@dialogue:R_05.scene0.dialogue0014')
    stage.say('@dialogue:R_05.scene0.dialogue0015')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_05.scene0.dialogue0016')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_05.scene0.dialogue0017')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_05.scene0.dialogue0018')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 1)
    stage.showUnits([
        [
            158,
            52,
            74,
            0,
            1],
        [
            192,
            52,
            60,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('진류  조숭의 집')
    stage.unit(158).setAction(3)
    stage.say('@dialogue:R_05.scene1.dialogue0001')
    stage.unit(158).setAction(0)
    stage.say('@dialogue:R_05.scene1.dialogue0002')
    stage.unit(158).setAction(0, 1)
    stage.say('@dialogue:R_05.scene1.dialogue0003')
    stage.unit(158).setAction(0, 0)
    stage.say('@dialogue:R_05.scene1.dialogue0004')
    stage.unit(192).setAction(6, 2)
    stage.say('@dialogue:R_05.scene1.dialogue0005')
    stage.unit(192).setAction(0, 2)
    stage.delay(3)
    stage.unit(192).move(52, 20, 0)
    stage.unit(192).hide()
    stage.unit(158).move(48, 74, 3)
    stage.delay(5)
    stage.unit(158).setAction(0, 2)
    stage.say('@dialogue:R_05.scene1.dialogue0006')
    stage.delay(5)
    stage.effectSound(202, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.delay(5)
    stage.showHead(0, 458, 62)
    stage.showHead(158, 362, 115)
    stage.bottomTxt('조숭은 조조의 출세를 크게 기뻐하며\n즉시 길을 떠나 복양으로 향했다.', False, True, False)
    stage.delay(5)
    stage.head(158).move(394, 86)
    stage.delay(5)
    stage.bottomTxt('그러나 불행히도', True, True, False)
    stage.delay(5)
    stage.bottomTxt('서주를 지나던 중 정체를 알 수 없는 자의 습격을 받아,', True, False, False)
    stage.head(158).hide()
    stage.delay(5)
    stage.bottomTxt('조숭은 그 자리에서 살해당했다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene2():
    stage.loadBg(2, 12)
    stage.showUnits([
        [
            0,
            29,
            54,
            1],
        [
            192,
            46,
            54,
            3],
        [
            20,
            50,
            41,
            2],
        [
            22,
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
            1,
            50,
            68],
        [
            6,
            56,
            68],
        [
            5,
            62,
            68]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('며칠 뒤…….', 3)
    stage.setStageName('복양  조조 저택')
    stage.delay(3)
    stage.unit(1).setAction(2)
    stage.say('@dialogue:R_05.scene2.dialogue0001')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(0, 2)
    stage.say('@dialogue:R_05.scene2.dialogue0002')
    stage.unit(6).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0003')
    stage.unit(6).setAction(0)
    stage.delay(3)
    stage.say('@dialogue:R_05.scene2.dialogue0004')
    stage.delay(5)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_05.scene2.dialogue0005')
    stage.unit(192).setAction(0, 3)
    stage.delay(3)
    stage.unit(192).setAction(6, 3)
    stage.say('@dialogue:R_05.scene2.dialogue0006')
    stage.unit(192).setAction(0)
    stage.delay(5)
    stage.unit(1).setAction(0, 3)
    stage.say('@dialogue:R_05.scene2.dialogue0007')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_05.scene2.dialogue0008')
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0009')
    stage.unit(22).setAction(0)
    stage.say('@dialogue:R_05.scene2.dialogue0010')
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0011')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_05.scene2.dialogue0012')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_05.scene2.dialogue0013')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0014')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_05.scene2.dialogue0015')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0016')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0017')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_05.scene2.dialogue0018')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0019')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(0)
    stage.say('@dialogue:R_05.scene2.dialogue0020')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0021')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_05.scene2.dialogue0022')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_05.scene2.dialogue0023')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_05.scene2.dialogue0024')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_05.scene2.dialogue0025')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_05.scene2.dialogue0026')
    stage.delay(5)
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 6)
    stage.showUnit(161, 70, 55, 3)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('북평  공손찬 저택')
    stage.showUnits([
        [
            32,
            10,
            55,
            1],
        [
            33,
            2,
            52,
            1],
        [
            3,
            2,
            58,
            1]])
    stage.say('@dialogue:R_05.scene3.dialogue0001')
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(32),
            54,
            55,
            1],
        [
            stage.unit(33),
            46,
            52,
            1],
        [
            stage.unit(3),
            46,
            58,
            1]])
    stage.say('@dialogue:R_05.scene3.dialogue0002')
    stage.unit(161).setAction(3)
    stage.say('@dialogue:R_05.scene3.dialogue0003')
    stage.unit(161).setAction(0)
    stage.say('@dialogue:R_05.scene3.dialogue0004')
    stage.unit(161).setAction(3)
    stage.say('@dialogue:R_05.scene3.dialogue0005')
    stage.unit(161).setAction(0)
    stage.say('@dialogue:R_05.scene3.dialogue0006')
    stage.unit(161).setAction(3)
    stage.say('@dialogue:R_05.scene3.dialogue0007')
    stage.unit(161).setAction(0)
    stage.say('@dialogue:R_05.scene3.dialogue0008')
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_05.scene3.dialogue0009')
    stage.unit(32).setAction(0, 1)
    stage.delay(3)
    stage.unit(32).setAction(0, 3)
    stage.say('@dialogue:R_05.scene3.dialogue0010')
    stage.unit(33).setAction(3)
    stage.say('@dialogue:R_05.scene3.dialogue0011')
    stage.unit(33).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_05.scene3.dialogue0012')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(32),
            18,
            55,
            3],
        [
            stage.unit(33),
            10,
            52,
            3],
        [
            stage.unit(3),
            10,
            58,
            3]])
    stage.unit(33).hide()
    stage.unit(3).hide()
    stage.unit(32).move(5, 55, 3)
    stage.unit(32).hide()
    stage.delay(5)
    stage.effectSound(202, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 387, 72)
    stage.showHead(145, 507, 135)
    stage.bottomTxt('아버지 조숭의 원수를 갚고자,', False, False, False)
    stage.delay(5)
    stage.head(0).move(387, 135)
    stage.delay(5)
    stage.bottomTxt('도겸이 태수로 있는 서주를 공격했다.', True, False, False)
    stage.delay(8)
    stage.showHead(32, 467, 0)
    stage.delay(5)
    stage.bottomTxt('유비 삼형제는 그 모습을 보고,', True, True, False)
    stage.delay(5)
    stage.head(32).move(467, 15)
    stage.delay(5)
    stage.bottomTxt('도겸을 구원하러 나섰다.', True, False, True)
    stage.bottomTxt('이때,', False, True, False)
    stage.delay(5)
    stage.showHead(119, 354, 15)
    stage.delay(5)
    stage.bottomTxt('진류에서 곤경에 처한 여포 앞에\n한 사람이 나타났다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene4():
    stage.loadBg(2, 0)
    stage.showUnits([
        [
            119,
            40,
            54,
            1],
        [
            126,
            50,
            54,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('진류  여포의 저택')
    stage.say('@dialogue:R_05.scene4.dialogue0001')
    stage.unit(119).move(38, 54, 3)
    stage.say('@dialogue:R_05.scene4.dialogue0002')
    stage.unit(119).setAction(0, 1)
    stage.say('@dialogue:R_05.scene4.dialogue0003')
    stage.unit(126).setAction(0, 1)
    stage.say('@dialogue:R_05.scene4.dialogue0004')
    stage.say('@dialogue:R_05.scene4.dialogue0005')
    stage.unit(119).setAction(2)
    stage.say('@dialogue:R_05.scene4.dialogue0006')
    stage.unit(126).setAction(0, 3)
    stage.say('@dialogue:R_05.scene4.dialogue0007')
    stage.unit(119).setAction(0)
    stage.say('@dialogue:R_05.scene4.dialogue0008')
    stage.say('@dialogue:R_05.scene4.dialogue0009')
    stage.delay(10)
    stage.unit(119).setAction(0, 2)
    stage.say('@dialogue:R_05.scene4.dialogue0010')
    stage.unit(119).setAction(0, 1)
    stage.say('@dialogue:R_05.scene4.dialogue0011')
    stage.unit(119).setAction(17, 1)
    stage.say('@dialogue:R_05.scene4.dialogue0012')
    stage.delay(5)
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene5():
    model.unitJoin(22, 0, 0)
    model.unitJoin(21, 0, 0)
    if vars[1013]:
        gvars[0] = 21
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
        gvars[0] = 22
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
            52,
            1],
        [
            6,
            38,
            46,
            1],
        [
            16,
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
            21,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('서주 교외  조조군 본진')
    stage.say('@dialogue:R_05.scene6.dialogue0001')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        22,
        21])
    gvars[4076] = 0
    gvars[4057] = 5
    stage.setBattlePos([
        [
            0,
            12],
        [
            0,
            11],
        [
            0,
            13],
        [
            1,
            12],
        [
            1,
            11],
        [
            1,
            13],
        [
            1,
            10],
        [
            1,
            14],
        [
            0,
            10],
        [
            0,
            14],
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


def scene7():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_05.scene7.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_05.scene7.dialogue0002')
    if stage.unitClickTest(6):
        stage.say('@dialogue:R_05.scene7.dialogue0003')
    if stage.unitClickTest(16):
        stage.say('@dialogue:R_05.scene7.dialogue0004')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_05.scene7.dialogue0005')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_05.scene7.dialogue0006')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_05.scene7.dialogue0007')
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
