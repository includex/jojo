# Source Generated with Decompyle++
# File: R_02.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            127,
            32,
            55,
            1],
        [
            132,
            50,
            40,
            2],
        [
            119,
            58,
            40,
            2],
        [
            192,
            50,
            70],
        [
            193,
            58,
            70],
        [
            181,
            50,
            55,
            3,
            11]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('호로관의 결전')
    stage.setStageName('낙양  승상부')
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_02.scene0.dialogue0001')
    stage.unit(127).setAction(0)
    stage.say('@dialogue:R_02.scene0.dialogue0002')
    stage.say('@dialogue:R_02.scene0.dialogue0003')
    stage.unit(132).setAction(6)
    stage.say('@dialogue:R_02.scene0.dialogue0004')
    stage.unit(132).setAction(0)
    stage.say('@dialogue:R_02.scene0.dialogue0005')
    stage.unit(127).setAction(17)
    stage.say('@dialogue:R_02.scene0.dialogue0006')
    stage.unit(127).setAction(0)
    stage.delay(5)
    stage.say('@dialogue:R_02.scene0.dialogue0007')
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_02.scene0.dialogue0008')
    stage.unit(127).setAction(0)
    stage.say('@dialogue:R_02.scene0.dialogue0009')
    stage.unit(119).setAction(17)
    stage.say('@dialogue:R_02.scene0.dialogue0010')
    stage.unit(119).setAction(0)
    stage.delay(3)
    stage.unit(119).move(61, 40, 1)
    stage.unit(119).move(61, 55, 2)
    stage.unit(119).move(91, 55, 1)
    stage.unit(119).hide()
    stage.delay(5)
    stage.say('@dialogue:R_02.scene0.dialogue0011')
    stage.unit(127).setAction(17)
    stage.say('@dialogue:R_02.scene0.dialogue0012')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(132).setAction(6)
    stage.say('@dialogue:R_02.scene0.dialogue0013')
    stage.unit(132).setAction(0)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            101,
            54,
            37,
            2],
        [
            0,
            48,
            45,
            2],
        [
            145,
            42,
            52,
            1],
        [
            136,
            42,
            60,
            1],
        [
            161,
            67,
            52,
            3],
        [
            32,
            67,
            60,
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('낙양 교외  연합군 본진')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0001')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(136).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0002')
    stage.unit(136).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_02.scene1.dialogue0003')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_02.scene1.dialogue0004')
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_02.scene1.dialogue0005')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_02.scene1.dialogue0006')
    stage.unit(136).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0007')
    stage.unit(136).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_02.scene1.dialogue0008')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_02.scene1.dialogue0009')
    stage.delay(5)
    stage.unit(136).setAction(2)
    stage.say('@dialogue:R_02.scene1.dialogue0010')
    stage.unit(136).setAction(0)
    stage.delay(3)
    stage.unit(136).setAction(0, 2)
    stage.say('@dialogue:R_02.scene1.dialogue0011')
    stage.unit(136).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0012')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_02.scene1.dialogue0013')
    stage.unit(32).setAction(6)
    if vars[701]:
        stage.say('@dialogue:R_02.scene1.dialogue0014')
        stage.unit(32).setAction(0)
        stage.delay(3)
        stage.unit(136).setAction(0, 1)
        stage.say('@dialogue:R_02.scene1.dialogue0015')
    if not vars[701]:
        stage.say('@dialogue:R_02.scene1.dialogue0016')
        stage.unit(32).setAction(0)
        stage.delay(3)
        stage.unit(136).setAction(0, 1)
        stage.say('@dialogue:R_02.scene1.dialogue0017')
    stage.unit(136).setAction(3, 1)
    stage.say('@dialogue:R_02.scene1.dialogue0018')
    stage.unit(136).setAction(0, 1)
    stage.say('@dialogue:R_02.scene1.dialogue0019')
    stage.unit(136).setAction(3, 1)
    stage.say('@dialogue:R_02.scene1.dialogue0020')
    stage.unit(136).setAction(0, 2)
    stage.unit(0).setAction(0, 2)
    stage.unit(145).setAction(0, 2)
    stage.unit(143).setAction(0, 2)
    stage.unit(161).setAction(0, 2)
    stage.unit(32).setAction(0, 2)
    stage.showUnit(99, 54, 94)
    stage.unit(99).move(54, 60, 0)
    stage.unit(32).setAction(0, 3)
    stage.unit(136).setAction(0, 1)
    stage.unit(145).setAction(0, 1)
    stage.unit(143).setAction(0, 3)
    stage.unit(161).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0021')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0022')
    stage.unit(32).setAction(0)
    stage.delay(5)
    stage.unit(99).setAction(0, 2)
    stage.say('@dialogue:R_02.scene1.dialogue0023')
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_02.scene1.dialogue0024')
    stage.unit(99).setAction(0, 0)
    stage.say('@dialogue:R_02.scene1.dialogue0025')
    stage.say('@dialogue:R_02.scene1.dialogue0026')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_02.scene1.dialogue0027')
    stage.unit(99).setAction(0, 2)
    stage.say('@dialogue:R_02.scene1.dialogue0028')
    stage.unit(99).move(50, 60, 3)
    stage.delay(3)
    stage.unit(99).setAction(2)
    stage.say('@dialogue:R_02.scene1.dialogue0029')
    stage.unit(145).setAction(0, 2)
    stage.say('@dialogue:R_02.scene1.dialogue0030')
    stage.unit(99).setAction(2)
    stage.say('@dialogue:R_02.scene1.dialogue0031')
    stage.unit(136).move(36, 60, 1)
    stage.unit(136).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0032')
    stage.unit(136).setAction(0)
    stage.unit(99).move(44, 60, 3)
    stage.delay(3)
    stage.unit(99).setAction(2)
    stage.say('@dialogue:R_02.scene1.dialogue0033')
    stage.unit(136).setAction(9)
    stage.say('@dialogue:R_02.scene1.dialogue0034')
    stage.unit(99).setAction(0)
    stage.say('@dialogue:R_02.scene1.dialogue0035')
    stage.unit(99).setAction(2)
    stage.say('@dialogue:R_02.scene1.dialogue0036')
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_02.scene1.dialogue0037')
    stage.unit(99).setAction(2, 1)
    stage.say('@dialogue:R_02.scene1.dialogue0038')
    stage.unit(32).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_02.scene1.dialogue0039')
    stage.unit(99).setAction(2, 0)
    stage.say('@dialogue:R_02.scene1.dialogue0040')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_02.scene1.dialogue0041')
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_02.scene1.dialogue0042')
    stage.delay(5)
    stage.unit(99).setAction(0)
    stage.say('@dialogue:R_02.scene1.dialogue0043')
    stage.delay(10)
    stage.unit(99).setAction(0, 2)
    stage.say('@dialogue:R_02.scene1.dialogue0044')
    stage.unit(0).setAction(0)
    stage.delay(8)
    stage.unit(99).setAction(0, 3)
    stage.say('@dialogue:R_02.scene1.dialogue0045')
    stage.unit(136).setAction(1)
    stage.say('@dialogue:R_02.scene1.dialogue0046')
    stage.say('@dialogue:R_02.scene1.dialogue0047')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0048')
    stage.unit(101).setAction(0)
    stage.delay(5)
    stage.unit(99).setAction(0, 0)
    stage.say('@dialogue:R_02.scene1.dialogue0049')
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(99),
            42,
            68,
            1],
        [
            stage.unit(136),
            42,
            60,
            1]])
    stage.unit(145).setAction(0, 1)
    stage.delay(3)
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0050')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_02.scene1.dialogue0051')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_02.scene1.dialogue0052')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_02.scene1.dialogue0053')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_02.scene1.dialogue0054')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_02.scene1.dialogue0055')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(145).setAction(6)
    stage.say('@dialogue:R_02.scene1.dialogue0056')
    stage.unit(145).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_02.scene1.dialogue0057')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(15)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            66],
        [
            1,
            39,
            52,
            1],
        [
            5,
            39,
            46,
            1],
        [
            10,
            39,
            40,
            1],
        [
            6,
            60,
            52,
            3],
        [
            12,
            60,
            46,
            3],
        [
            11,
            60,
            40,
            3]])
    stage.draw()
    stage.delay(3)
    stage.setStageName('사수관  조조군 본진')
    stage.say('@dialogue:R_02.scene2.dialogue0001')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        1])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 2
    stage.setBattlePos([
        [
            17,
            9],
        [
            16,
            8],
        [
            16,
            11],
        [
            16,
            9],
        [
            16,
            10],
        [
            17,
            8],
        [
            17,
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


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_02.scene3.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_02.scene3.dialogue0002')
    if stage.unitClickTest(6):
        stage.say('@dialogue:R_02.scene3.dialogue0003')
    if stage.unitClickTest(5):
        stage.say('@dialogue:R_02.scene3.dialogue0004')
    if stage.unitClickTest(12):
        stage.say('@dialogue:R_02.scene3.dialogue0005')
    if stage.unitClickTest(10):
        stage.say('@dialogue:R_02.scene3.dialogue0006')
    if stage.unitClickTest(11):
        stage.say('@dialogue:R_02.scene3.dialogue0007')
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
