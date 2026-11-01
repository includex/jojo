# Source Generated with Decompyle++
# File: R_30.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            0,
            53,
            29,
            2],
        [
            1,
            68,
            48,
            3],
        [
            2,
            68,
            54,
            3],
        [
            15,
            68,
            60,
            3],
        [
            5,
            68,
            66,
            3],
        [
            20,
            38,
            48,
            1],
        [
            23,
            38,
            54,
            1],
        [
            24,
            38,
            60,
            1],
        [
            21,
            38,
            66,
            1]])
    if vars[724]:
        stage.showUnit(22, 38, 72, 1)
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('장료의 격파')
    stage.setStageName('남군  조조의 저택')
    if vars[724] and vars[727]:
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_30.scene0.dialogue0001')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0002')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0003')
        stage.unit(22).setAction(0)
        stage.delay(3)
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_30.scene0.dialogue0004')
        stage.say('@dialogue:R_30.scene0.dialogue0005')
        stage.unit(2).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0006')
        stage.unit(2).setAction(0)
        stage.say('@dialogue:R_30.scene0.dialogue0007')
    if vars[728] and not vars[724]:
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_30.scene0.dialogue0008')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0009')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0010')
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('@dialogue:R_30.scene0.dialogue0011')
        stage.say('@dialogue:R_30.scene0.dialogue0012')
        stage.unit(2).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0013')
        stage.unit(2).setAction(0)
        stage.say('@dialogue:R_30.scene0.dialogue0014')
    if vars[724] and not vars[727]:
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_30.scene0.dialogue0015')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0016')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0017')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('@dialogue:R_30.scene0.dialogue0018')
        stage.say('@dialogue:R_30.scene0.dialogue0019')
        stage.unit(2).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0020')
        stage.unit(2).setAction(0)
        stage.say('@dialogue:R_30.scene0.dialogue0021')
    if not vars[724] and not vars[728]:
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_30.scene0.dialogue0022')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0023')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0024')
        stage.unit(21).setAction(0)
        stage.delay(3)
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_30.scene0.dialogue0025')
        stage.say('@dialogue:R_30.scene0.dialogue0026')
        stage.unit(2).setAction(6)
        stage.say('@dialogue:R_30.scene0.dialogue0027')
        stage.unit(2).setAction(0)
        stage.say('@dialogue:R_30.scene0.dialogue0028')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 264, 238)
    stage.showHead(32, 328, 238)
    stage.showHead(76, 392, 238)
    stage.bottomTxt('적벽 전투 뒤,', False, False, False)
    stage.delay(5)
    stage.showHead(2, 443, 129)
    stage.delay(5)
    stage.bottomTxt('조조는 장료에게 합비를 지키게 하고,', True, False, False)
    stage.delay(5)
    stage.head(0).move(264, 158)
    stage.head(0).move(330, 111)
    stage.delay(5)
    stage.bottomTxt('자신은 허도로 돌아갔다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('유비는 공명의 묘책에 따라', True, True, False)
    stage.delay(5)
    stage.head(32).move(290, 223)
    stage.delay(5)
    stage.bottomTxt('형주를 빼앗았다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('한편,', True, True, False)
    stage.delay(5)
    stage.head(76).move(472, 209)
    stage.delay(5)
    stage.bottomTxt('손권은 대군을 일으켜 장료가 지키는 합비를 공격했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(0, 35)
    stage.loadBg(2, 57)
    stage.showUnits([
        [
            76,
            65,
            52,
            3],
        [
            78,
            52,
            42,
            2],
        [
            88,
            52,
            62]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('합비 근교  오군 본진')
    stage.say('@dialogue:R_30.scene1.dialogue0001')
    stage.unit(78).setAction(0, 1)
    stage.delay(3)
    stage.unit(78).setAction(6)
    stage.say('@dialogue:R_30.scene1.dialogue0002')
    stage.unit(76).setAction(2)
    stage.say('@dialogue:R_30.scene1.dialogue0003')
    stage.unit(76).setAction(0)
    stage.say('@dialogue:R_30.scene1.dialogue0004')
    stage.unit(78).setAction(0)
    stage.say('@dialogue:R_30.scene1.dialogue0005')
    stage.showUnit(182, 16, 52, 1)
    stage.say('@dialogue:R_30.scene1.dialogue0006')
    stage.unit(182).move(52, 52, 1)
    stage.say('@dialogue:R_30.scene1.dialogue0007')
    stage.unit(182).move(57, 52, 1)
    stage.unit(182).setAction(0)
    stage.delay(3)
    stage.unit(76).setAction(5)
    stage.delay(5)
    stage.unit(182).move(52, 52, 1)
    stage.unit(182).setAction(0)
    stage.delay(10)
    stage.unit(76).setAction(2)
    stage.say('@dialogue:R_30.scene1.dialogue0008')
    stage.unit(88).setAction(0, 1)
    stage.delay(3)
    stage.unit(88).setAction(6)
    stage.say('@dialogue:R_30.scene1.dialogue0009')
    stage.unit(76).setAction(2, 2)
    stage.say('@dialogue:R_30.scene1.dialogue0010')
    stage.unit(88).setAction(0)
    stage.delay(3)
    stage.unit(88).move(57, 62, 1)
    stage.unit(88).move(57, 52, 0)
    stage.delay(3)
    stage.unit(88).setAction(0, 1)
    stage.delay(3)
    stage.say('@dialogue:R_30.scene1.dialogue0011')
    stage.unit(76).setAction(2)
    stage.say('@dialogue:R_30.scene1.dialogue0012')
    stage.unit(76).setAction(2, 3)
    stage.delay(3)
    stage.unit(76).setAction(17)
    stage.say('@dialogue:R_30.scene1.dialogue0013')
    stage.effectSound(203, 255)
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
            1,
            52,
            70],
        [
            15,
            58,
            70],
        [
            13,
            64,
            70],
        [
            20,
            52,
            40,
            2],
        [
            23,
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
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.unit(1).setAction(2)
    stage.say('@dialogue:R_30.scene2.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_30.scene2.dialogue0002')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 3)
    stage.say('@dialogue:R_30.scene2.dialogue0003')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_30.scene2.dialogue0004')
    stage.unit(20).setAction(0)
    model.setFace(3)
    stage.say('@dialogue:R_30.scene2.dialogue0005')
    stage.unit(1).setAction(2)
    stage.say('@dialogue:R_30.scene2.dialogue0006')
    stage.unit(1).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_30.scene2.dialogue0007')
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_30.scene2.dialogue0008')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_30.scene2.dialogue0009')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_30.scene2.dialogue0010')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
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
    stage.say('@dialogue:R_30.scene3.dialogue0001')
    stage.unit(2).setAction(17)
    stage.say('@dialogue:R_30.scene3.dialogue0002')
    stage.unit(2).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        2,
        10,
        11], [
        0])
    gvars[4076] = 0
    gvars[4057] = 30
    stage.setBattlePos([
        [
            14,
            5],
        [
            13,
            4],
        [
            11,
            5],
        [
            8,
            2],
        [
            8,
            7],
        [
            12,
            11],
        [
            16,
            11],
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
        stage.say('@dialogue:R_30.scene4.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(10):
        stage.say('@dialogue:R_30.scene4.dialogue0002')
    if stage.unitClickTest(11):
        stage.say('@dialogue:R_30.scene4.dialogue0003')
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
