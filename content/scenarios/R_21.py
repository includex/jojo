# Source Generated with Decompyle++
# File: R_21.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.bgSound(-1)
    stage.loadBg(2, 67)
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('원소의 아들은')
    stage.setStageName('양산  산길')
    stage.showUnits([
        [
            32,
            57,
            5,
            2],
        [
            33,
            47,
            5,
            2],
        [
            3,
            52,
            5,
            2]])
    stage.unitsMove([
        [
            stage.unit(32),
            57,
            17,
            2],
        [
            stage.unit(33),
            47,
            17,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(32),
            57,
            47,
            2],
        [
            stage.unit(33),
            47,
            47,
            2],
        [
            stage.unit(3),
            52,
            35,
            2]])
    stage.say('@dialogue:R_21.scene0.dialogue0001')
    stage.unit(33).setAction(2)
    stage.say('@dialogue:R_21.scene0.dialogue0002')
    stage.unit(33).setAction(0)
    stage.say('@dialogue:R_21.scene0.dialogue0003')
    stage.unit(33).setAction(0, 0)
    stage.delay(3)
    stage.unit(32).setAction(0, 0)
    stage.say('@dialogue:R_21.scene0.dialogue0004')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_21.scene0.dialogue0005')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_21.scene0.dialogue0006')
    stage.unit(33).setAction(3)
    stage.say('@dialogue:R_21.scene0.dialogue0007')
    stage.unit(33).setAction(0)
    stage.say('@dialogue:R_21.scene0.dialogue0008')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_21.scene0.dialogue0009')
    stage.say('@dialogue:R_21.scene0.dialogue0010')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_21.scene0.dialogue0011')
    stage.unit(32).setAction(0, 3)
    stage.say('@dialogue:R_21.scene0.dialogue0012')
    stage.unit(33).setAction(0, 1)
    stage.delay(3)
    stage.unit(33).setAction(17)
    stage.say('@dialogue:R_21.scene0.dialogue0013')
    stage.unit(33).setAction(0)
    stage.unitsMove([
        [
            stage.unit(32),
            57,
            98,
            2],
        [
            stage.unit(33),
            47,
            95,
            2],
        [
            stage.unit(3),
            52,
            80,
            2]])
    stage.unit(3).setAction(0)
    stage.unit(32).hide()
    stage.unit(33).hide()
    stage.effectSound(203, 255)
    stage.effectSound(207, 0)
    stage.delay(3)
    stage.showUnits([
        [
            0,
            56,
            5,
            2],
        [
            22,
            48,
            5,
            2],
        [
            181,
            57,
            5,
            2],
        [
            182,
            52,
            5,
            2],
        [
            183,
            47,
            5,
            2]])
    stage.say('@dialogue:R_21.scene0.dialogue0014')
    stage.unit(3).setAction(0, 0)
    stage.say('@dialogue:R_21.scene0.dialogue0015')
    stage.unitsMove([
        [
            stage.unit(0),
            56,
            13,
            2],
        [
            stage.unit(22),
            48,
            13,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(0),
            56,
            66,
            2],
        [
            stage.unit(22),
            48,
            66,
            2],
        [
            stage.unit(181),
            57,
            58,
            2],
        [
            stage.unit(182),
            52,
            58,
            2],
        [
            stage.unit(183),
            47,
            58,
            2]])
    stage.unit(0).setAction(0)
    stage.unit(22).setAction(0)
    stage.unit(181).setAction(0)
    stage.unit(182).setAction(0)
    stage.unit(183).setAction(0)
    model.setFace(1)
    stage.say('@dialogue:R_21.scene0.dialogue0016')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_21.scene0.dialogue0017')
    stage.unit(3).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_21.scene0.dialogue0018')
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_21.scene0.dialogue0019')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_21.scene0.dialogue0020')
    stage.effectSound(207, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('관우를 공격한다\n공격을 멈춘다', 0)
    if sel == 1:
        stage.addAmbition(5)
        vars[307] = 1
        stage.bgSound(-1)
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.say('@dialogue:R_21.scene0.dialogue0021')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_21.scene0.dialogue0022')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_21.scene0.dialogue0023')
        stage.unit(22).setAction(0, 0)
        stage.say('@dialogue:R_21.scene0.dialogue0024')
        stage.unit(22).setAction(0, 1)
        stage.say('@dialogue:R_21.scene0.dialogue0025')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_21.scene0.dialogue0026')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(181),
                57,
                54,
                0],
            [
                stage.unit(182),
                52,
                54,
                0],
            [
                stage.unit(183),
                47,
                54,
                0]])
        stage.delay(3)
        stage.unit(181).setAction(0, 2)
        stage.unit(182).setAction(0, 2)
        stage.unit(183).setAction(0, 2)
        stage.delay(10)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_21.scene0.dialogue0027')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_21.scene0.dialogue0028')
        stage.delay(3)
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_21.scene0.dialogue0029')
        stage.unitsMove([
            [
                stage.unit(0),
                56,
                5,
                0],
            [
                stage.unit(22),
                48,
                5,
                0],
            [
                stage.unit(181),
                57,
                5,
                0],
            [
                stage.unit(182),
                52,
                5,
                0],
            [
                stage.unit(183),
                47,
                5,
                0]])
        stage.unit(0).hide()
        stage.unit(22).hide()
        stage.unit(181).hide()
        stage.unit(182).hide()
        stage.unit(183).hide()
        stage.unit(3).setAction(0, 2)
        stage.say('@dialogue:R_21.scene0.dialogue0030')
        stage.unit(3).move(52, 98, 2)
        stage.unit(3).hide()
    elif sel == 2:
        vars[307] = 0
        stage.addAmbition(-10)
        stage.bgSound(19)
        stage.say('@dialogue:R_21.scene0.dialogue0031')
        stage.unit(22).setAction(0, 1)
        stage.say('@dialogue:R_21.scene0.dialogue0032')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_21.scene0.dialogue0033')
        stage.unit(0).setAction(0, 3)
        stage.say('@dialogue:R_21.scene0.dialogue0034')
        stage.unit(22).setAction(0)
        stage.say('@dialogue:R_21.scene0.dialogue0035')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_21.scene0.dialogue0036')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_21.scene0.dialogue0037')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(17, 0)
        stage.say('@dialogue:R_21.scene0.dialogue0038')
        stage.unit(22).setAction(0)
        stage.say('@dialogue:R_21.scene0.dialogue0039')
        stage.unit(0).setAction(0, 0)
        stage.unitsMove([
            [
                stage.unit(0),
                56,
                5,
                0],
            [
                stage.unit(22),
                48,
                5,
                0],
            [
                stage.unit(181),
                57,
                5,
                0],
            [
                stage.unit(182),
                52,
                5,
                0],
            [
                stage.unit(183),
                47,
                5,
                0]])
        stage.unit(0).hide()
        stage.unit(22).hide()
        stage.unit(181).hide()
        stage.unit(182).hide()
        stage.unit(183).hide()
        stage.say('@dialogue:R_21.scene0.dialogue0040')
    stage.effectSound(203, 255)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 398, 119)
    stage.showHead(32, 368, 199)
    stage.bottomTxt('양산에서 패한 유비는', False, False, False)
    stage.delay(5)
    stage.showHead(150, 282, 222)
    stage.delay(5)
    stage.bottomTxt('무리를 이끌고 의탁하러 갔다.', True, False, False)
    stage.delay(5)
    stage.head(32).move(346, 222)
    stage.delay(5)
    stage.bottomTxt('형주의 유표였다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('조조의 대군', True, True, False)
    stage.delay(5)
    stage.head(0).move(333, 95)
    stage.delay(5)
    stage.bottomTxt('허도로 개선한 뒤,', True, False, False)
    stage.delay(5)
    stage.bottomTxt('하북 평정을 준비하며 병사 훈련에 힘썼다.', True, False, True)
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
            15,
            58,
            70],
        [
            2,
            64,
            70],
        [
            23,
            70,
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
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_21.scene1.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_21.scene1.dialogue0002')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_21.scene1.dialogue0003')
    stage.unit(22).setAction(0)
    stage.say('@dialogue:R_21.scene1.dialogue0004')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_21.scene1.dialogue0005')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_21.scene1.dialogue0006')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_21.scene1.dialogue0007')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_21.scene1.dialogue0008')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 334, 133)
    stage.showHead(115, 353, 30)
    stage.bottomTxt('조조군', False, False, False)
    stage.delay(5)
    stage.head(0).move(289, 133)
    stage.head(0).move(289, 84)
    stage.delay(5)
    stage.bottomTxt('황하를 건너 북상하여', True, False, False)
    stage.delay(5)
    stage.bottomTxt('여양 부근으로 진군했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            115,
            53,
            28,
            2],
        [
            106,
            38,
            48,
            1],
        [
            102,
            38,
            54,
            1],
        [
            103,
            38,
            60,
            1],
        [
            113,
            68,
            48,
            3],
        [
            114,
            68,
            54,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('업성@원상 저택')
    stage.say('@dialogue:R_21.scene2.dialogue0001')
    stage.unit(106).setAction(6)
    stage.say('@dialogue:R_21.scene2.dialogue0002')
    stage.unit(106).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(0, 1)
    stage.say('@dialogue:R_21.scene2.dialogue0003')
    stage.unit(113).setAction(0, 3)
    stage.delay(3)
    stage.unit(113).setAction(2, 3)
    stage.say('@dialogue:R_21.scene2.dialogue0004')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:R_21.scene2.dialogue0005')
    stage.unit(102).setAction(0, 0)
    stage.say('@dialogue:R_21.scene2.dialogue0006')
    stage.unit(102).setAction(0, 1)
    stage.say('@dialogue:R_21.scene2.dialogue0007')
    stage.unit(113).setAction(0, 1)
    stage.say('@dialogue:R_21.scene2.dialogue0008')
    stage.unit(113).setAction(0, 3)
    stage.delay(3)
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_21.scene2.dialogue0009')
    stage.unit(103).setAction(0)
    stage.say('@dialogue:R_21.scene2.dialogue0010')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene3():
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
            24,
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
    stage.setStageName('여양 교외  조조군 본진')
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_21.scene3.dialogue0001')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_21.scene3.dialogue0002')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 11, [
        22])
    gvars[4076] = 0
    gvars[4057] = 21
    stage.setBattlePos([
        [
            2,
            36],
        [
            1,
            37],
        [
            3,
            37],
        [
            3,
            35],
        [
            4,
            36],
        [
            1,
            35],
        [
            4,
            38],
        [
            3,
            39],
        [
            0,
            36],
        [
            2,
            38],
        [
            0,
            38],
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


def scene4():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_21.scene4.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_21.scene4.dialogue0002')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_21.scene4.dialogue0003')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_21.scene4.dialogue0004')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_21.scene4.dialogue0005')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_21.scene4.dialogue0006')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_21.scene4.dialogue0007')
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
