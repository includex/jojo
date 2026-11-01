# Source Generated with Decompyle++
# File: R_16.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
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
            2,
            38,
            48,
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
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.setEventName('유비를 그리워하며')
    stage.delay(3)
    stage.setStageName('서주 교외  조조군 본진')
    stage.say('@dialogue:R_16.scene0.dialogue0001')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_16.scene0.dialogue0002')
    stage.unit(20).setAction(0, 3)
    stage.unit(2).move(49, 48, 1)
    stage.delay(3)
    stage.unit(2).setAction(0, 2)
    stage.say('@dialogue:R_16.scene0.dialogue0003')
    stage.unit(2).move(49, 16, 0)
    stage.unit(2).hide()
    stage.say('@dialogue:R_16.scene0.dialogue0004')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_16.scene0.dialogue0005')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_16.scene0.dialogue0006')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_16.scene0.dialogue0007')
    stage.unit(1).setAction(0, 1)
    stage.unit(22).setAction(0, 3)
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(1).move(49, 54, 1)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(1),
            49,
            42,
            0],
        [
            stage.unit(20),
            49,
            54,
            3],
        [
            stage.unit(22),
            49,
            48,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(1),
            49,
            16,
            0],
        [
            stage.unit(20),
            49,
            26,
            0],
        [
            stage.unit(22),
            49,
            21,
            0]])
    stage.unit(1).hide()
    stage.unitsMove([
        [
            stage.unit(22),
            49,
            16,
            0],
        [
            stage.unit(20),
            49,
            21,
            0]])
    stage.unit(22).hide()
    stage.unit(20).move(49, 16, 0)
    stage.unit(20).hide()
    stage.delay(10)
    stage.showUnit(2, 49, 16, 2)
    stage.unit(2).move(49, 21, 2)
    stage.showUnit(3, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(2),
            49,
            52,
            2],
        [
            stage.unit(3),
            49,
            47,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(2),
            41,
            52,
            3],
        [
            stage.unit(3),
            49,
            52,
            2]])
    stage.delay(3)
    stage.unit(2).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_16.scene0.dialogue0008')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_16.scene0.dialogue0009')
    stage.effectSound(203, 255)
    stage.bgSound(11)
    stage.delay(3)
    stage.say('@dialogue:R_16.scene0.dialogue0010')
    model.setFace(1)
    stage.say('@dialogue:R_16.scene0.dialogue0011')
    model.setFace(0)
    sel = stage.choice('좋다\n……', 0)
    if sel == 1:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(203, 0)
        model.setFace(0)
        stage.delay(3)
        stage.say('@dialogue:R_16.scene0.dialogue0012')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_16.scene0.dialogue0013')
    elif sel == 2:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        model.setFace(0)
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.say('@dialogue:R_16.scene0.dialogue0014')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_16.scene0.dialogue0015')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).move(49, 47, 0)
    stage.say('@dialogue:R_16.scene0.dialogue0016')
    stage.unit(3).move(49, 16, 0)
    stage.unit(3).hide()
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_16.scene0.dialogue0017')
    stage.unit(2).setAction(0, 2)
    stage.say('@dialogue:R_16.scene0.dialogue0018')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_16.scene0.dialogue0019')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 490, 150)
    stage.showHead(32, 554, 150)
    stage.showHead(101, 350, 27)
    stage.bottomTxt('조조에게 패한 유비는', False, False, False)
    stage.delay(5)
    stage.head(32).move(554, 70)
    stage.head(32).move(414, 27)
    stage.delay(8)
    stage.bottomTxt('업의 원소에게 몸을 의탁하러 갔다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            101,
            52,
            28,
            2],
        [
            106,
            38,
            48,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('업 원소 저택')
    stage.showUnit(181, 52, 92)
    stage.say('@dialogue:R_16.scene1.dialogue0001')
    stage.unit(181).hide()
    stage.showUnit(32, 52, 98)
    stage.delay(10)
    stage.unit(32).move(52, 48, 0)
    stage.say('@dialogue:R_16.scene1.dialogue0002')
    stage.unit(32).setAction(0, 3)
    stage.say('@dialogue:R_16.scene1.dialogue0003')
    stage.unit(32).setAction(0, 0)
    stage.say('@dialogue:R_16.scene1.dialogue0004')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 63)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부 부근')
    stage.showUnit(0, 85, 66, 3)
    stage.unit(0).move(73, 66, 3)
    stage.showUnit(3, 81, 66, 3)
    stage.unitsMove([
        [
            stage.unit(0),
            65,
            66,
            3],
        [
            stage.unit(3),
            73,
            66,
            3]])
    stage.showUnit(155, 81, 66, 3)
    stage.unitsMove([
        [
            stage.unit(0),
            57,
            66,
            3],
        [
            stage.unit(3),
            65,
            66,
            3],
        [
            stage.unit(155),
            73,
            66,
            3]])
    stage.showUnit(154, 81, 66, 3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_16.scene2.dialogue0001')
    stage.unit(0).move(20, 66, 3)
    stage.unit(0).hide()
    stage.unitsMove([
        [
            stage.unit(3),
            57,
            66,
            3],
        [
            stage.unit(155),
            65,
            66,
            3],
        [
            stage.unit(154),
            73,
            66,
            3]])
    stage.delay(3)
    stage.unit(3).setAction(0, 1)
    stage.say('@dialogue:R_16.scene2.dialogue0002')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(0, 35)
    stage.draw()
    stage.info('허도로 돌아온 조조는 갖은 방법으로 관우를 회유했다.')
    stage.info('조조는 관우에게 호화로운 저택을 주고\n사흘에 한 번 작은 잔치, 닷새에 한 번 큰 잔치를 열며\n날마다 금은보화를 선물했다.')
    stage.info('허나 관우의 마음은 움직이지 않았고\n조조의 근심은 날로 깊어졌다.')
    stage.bgSound(-1)
    stage.loadBg(2, 25)
    stage.showUnits([
        [
            2,
            56,
            55,
            1],
        [
            3,
            78,
            55,
            1]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부 정원')
    stage.showUnit(0, 66, 80)
    stage.delay(10)
    stage.say('@dialogue:R_16.scene3.dialogue0001')
    stage.unit(2).setAction(0, 2)
    stage.say('@dialogue:R_16.scene3.dialogue0002')
    stage.unit(2).move(56, 80, 1)
    stage.delay(5)
    stage.unit(0).move(62, 80, 3)
    stage.say('@dialogue:R_16.scene3.dialogue0003')
    stage.unit(2).move(56, 98, 2)
    stage.unit(2).hide()
    stage.unit(0).move(62, 55, 1)
    stage.say('@dialogue:R_16.scene3.dialogue0004')
    stage.unit(3).setAction(0, 3)
    stage.say('@dialogue:R_16.scene3.dialogue0005')
    stage.unit(0).move(68, 55, 1)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_16.scene3.dialogue0006')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_16.scene3.dialogue0007')
    stage.unit(3).setAction(0, 1)
    stage.say('@dialogue:R_16.scene3.dialogue0008')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_16.scene3.dialogue0009')
    stage.showUnits([
        [
            2,
            60,
            87],
        [
            198,
            60,
            92]])
    stage.say('@dialogue:R_16.scene3.dialogue0010')
    stage.unit(0).setAction(0, 2)
    stage.unitsMove([
        [
            stage.unit(2),
            60,
            55,
            1],
        [
            stage.unit(198),
            60,
            63,
            0]])
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_16.scene3.dialogue0011')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_16.scene3.dialogue0012')
    stage.unit(3).setAction(0, 3)
    stage.say('@dialogue:R_16.scene3.dialogue0013')
    stage.delay(5)
    stage.unit(3).move(78, 63, 2)
    stage.delay(1)
    stage.unit(0).setAction(0, 2)
    stage.unit(2).setAction(0, 2)
    stage.unit(3).move(68, 63, 3)
    stage.say('@dialogue:R_16.scene3.dialogue0014')
    stage.unit(3).setAction(0, 0)
    stage.say('@dialogue:R_16.scene3.dialogue0015')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_16.scene3.dialogue0016')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_16.scene3.dialogue0017')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_16.scene3.dialogue0018')
    stage.unit(3).setAction(0, 3)
    stage.delay(3)
    stage.unit(3).move(62, 63, 3)
    stage.say('@dialogue:R_16.scene3.dialogue0019')
    stage.unit(3).setAction(0, 0)
    stage.say('@dialogue:R_16.scene3.dialogue0020')
    stage.unit(3).setAction(0, 2)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(3),
            62,
            98,
            2],
        [
            stage.unit(198),
            60,
            98,
            2]])
    stage.unit(3).hide()
    stage.unit(198).hide()
    model.setFace(0)
    stage.say('@dialogue:R_16.scene3.dialogue0021')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            2,
            49,
            40,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_16.scene4.dialogue0001')
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_16.scene4.dialogue0002')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_16.scene4.dialogue0003')
    stage.unit(2).setAction(6)
    stage.say('@dialogue:R_16.scene4.dialogue0004')
    stage.unit(2).setAction(0)
    stage.delay(3)
    stage.unit(2).move(81, 40, 2)
    stage.unit(2).hide()
    stage.say('@dialogue:R_16.scene4.dialogue0005')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.loadBg(2, 0)
    stage.showUnit(3, 40, 54, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  관우 저택')
    stage.say('@dialogue:R_16.scene5.dialogue0001')
    stage.showUnit(2, 90, 54, 3)
    stage.say('@dialogue:R_16.scene5.dialogue0002')
    stage.unit(2).move(52, 54, 3)
    stage.say('@dialogue:R_16.scene5.dialogue0003')
    stage.unit(2).move(90, 54, 1)
    stage.say('@dialogue:R_16.scene5.dialogue0004')
    stage.unit(2).move(98, 54, 1)
    stage.unit(2).hide()
    stage.effectSound(202, 255)
    stage.clsUnit()
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
            20,
            49,
            40,
            2],
        [
            2,
            49,
            55,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_16.scene6.dialogue0001')
    stage.unit(2).setAction(6)
    stage.say('@dialogue:R_16.scene6.dialogue0002')
    stage.unit(2).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_16.scene6.dialogue0003')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_16.scene6.dialogue0004')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_16.scene6.dialogue0005')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_16.scene6.dialogue0006')
    stage.unit(2).setAction(0, 0)
    stage.say('@dialogue:R_16.scene6.dialogue0007')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_16.scene6.dialogue0008')
    stage.unit(20).setAction(0)
    stage.unit(2).setAction(0, 3)
    stage.delay(3)
    stage.say('@dialogue:R_16.scene6.dialogue0009')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene7():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            101,
            52,
            28,
            2],
        [
            106,
            38,
            48,
            1],
        [
            32,
            38,
            54,
            1],
        [
            110,
            67,
            48,
            3],
        [
            105,
            67,
            54,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('바로 그때였다.', 3)
    stage.setStageName('업 원소 저택')
    stage.say('@dialogue:R_16.scene7.dialogue0001')
    stage.unit(110).setAction(6)
    stage.say('@dialogue:R_16.scene7.dialogue0002')
    stage.unit(110).setAction(0)
    stage.say('@dialogue:R_16.scene7.dialogue0003')
    stage.unit(106).setAction(6)
    stage.say('@dialogue:R_16.scene7.dialogue0004')
    stage.unit(106).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_16.scene7.dialogue0005')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_16.scene7.dialogue0006')
    stage.unit(106).setAction(6)
    stage.say('@dialogue:R_16.scene7.dialogue0007')
    stage.unit(110).setAction(2)
    stage.say('@dialogue:R_16.scene7.dialogue0008')
    stage.unit(106).setAction(0)
    stage.say('@dialogue:R_16.scene7.dialogue0009')
    stage.unit(110).setAction(0)
    stage.say('@dialogue:R_16.scene7.dialogue0010')
    stage.unit(110).setAction(2)
    stage.say('@dialogue:R_16.scene7.dialogue0011')
    stage.unit(110).setAction(0)
    stage.say('@dialogue:R_16.scene7.dialogue0012')
    stage.unit(101).setAction(0, 0)
    stage.say('@dialogue:R_16.scene7.dialogue0013')
    stage.delay(5)
    stage.unit(101).setAction(0, 2)
    stage.say('@dialogue:R_16.scene7.dialogue0014')
    stage.unit(110).setAction(3)
    stage.say('@dialogue:R_16.scene7.dialogue0015')
    stage.unit(110).setAction(0)
    stage.delay(3)
    stage.unit(110).setAction(0, 1)
    stage.delay(3)
    stage.unit(110).setAction(2)
    stage.say('@dialogue:R_16.scene7.dialogue0016')
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_16.scene7.dialogue0017')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(105).setAction(0, 1)
    stage.say('@dialogue:R_16.scene7.dialogue0018')
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
            20,
            49,
            40,
            2],
        [
            22,
            55,
            40,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.showUnit(181, 90, 55, 3)
    stage.say('@dialogue:R_16.scene8.dialogue0001')
    stage.unit(181).hide()
    stage.showUnit(3, 90, 55, 3)
    stage.delay(10)
    stage.say('@dialogue:R_16.scene8.dialogue0002')
    stage.unit(3).move(49, 55, 3)
    stage.say('@dialogue:R_16.scene8.dialogue0003')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_16.scene8.dialogue0004')
    stage.unit(3).setAction(0)
    stage.delay(3)
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_16.scene8.dialogue0005')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_16.scene8.dialogue0006')
    stage.unit(3).move(95, 55, 1)
    stage.unit(3).hide()
    stage.showUnit(1, 95, 55, 3)
    stage.delay(10)
    stage.unit(1).move(73, 55, 3)
    stage.showUnit(24, 85, 40, 3)
    stage.unitsMove([
        [
            stage.unit(1),
            67,
            55,
            3],
        [
            stage.unit(24),
            79,
            40,
            3]])
    stage.showUnit(21, 85, 40, 3)
    stage.unitsMove([
        [
            stage.unit(1),
            49,
            55,
            3],
        [
            stage.unit(24),
            61,
            40,
            2],
        [
            stage.unit(21),
            67,
            40,
            2]])
    model.setFace(128)
    stage.say('@dialogue:R_16.scene8.dialogue0007')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_16.scene8.dialogue0008')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_16.scene8.dialogue0009')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_16.scene8.dialogue0010')
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(101, 353, 11)
    stage.showHead(0, 325, 128)
    stage.delay(8)
    stage.head(101).move(353, 34)
    stage.delay(5)
    stage.bottomTxt('원소군은 황하 가의 백마까지 남하했다.', False, False, False)
    stage.delay(8)
    stage.head(0).move(353, 114)
    stage.delay(8)
    stage.bottomTxt('조조군은 출병하여 적을 맞아 싸웠다.', True, True, True)
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
            2,
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
            24,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('백마  조조군 본진')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_16.scene9.dialogue0001')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10)
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 16
    stage.setBattlePos([
        [
            17,
            18],
        [
            15,
            9],
        [
            17,
            9],
        [
            9,
            14],
        [
            9,
            13],
        [
            8,
            15],
        [
            16,
            16],
        [
            15,
            17],
        [
            17,
            17],
        [
            18,
            17]])
    return False


def scene10():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_16.scene10.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_16.scene10.dialogue0002')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_16.scene10.dialogue0003')
    if stage.unitClickTest(2):
        stage.say('@dialogue:R_16.scene10.dialogue0004')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_16.scene10.dialogue0005')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_16.scene10.dialogue0006')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_16.scene10.dialogue0007')
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
