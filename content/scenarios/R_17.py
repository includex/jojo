# Source Generated with Decompyle++
# File: R_17.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 18)
    stage.showUnits([
        [
            101,
            35,
            56,
            1],
        [
            32,
            50,
            45,
            2],
        [
            109,
            50,
            67]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('의혹')
    stage.setStageName('백마  원소군 본진')
    stage.say('@dialogue:R_17.scene0.dialogue0001')
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_17.scene0.dialogue0002')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(109).setAction(0, 1)
    stage.say('@dialogue:R_17.scene0.dialogue0003')
    stage.unit(109).setAction(2)
    stage.say('@dialogue:R_17.scene0.dialogue0004')
    stage.unit(109).setAction(0)
    stage.delay(5)
    stage.say('@dialogue:R_17.scene0.dialogue0005')
    stage.unit(101).setAction(0, 3)
    stage.say('@dialogue:R_17.scene0.dialogue0006')
    stage.unit(109).setAction(0, 0)
    stage.delay(3)
    stage.unit(109).setAction(3)
    stage.say('@dialogue:R_17.scene0.dialogue0007')
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_17.scene0.dialogue0008')
    stage.unit(109).setAction(0)
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.say('@dialogue:R_17.scene0.dialogue0009')
    stage.unit(101).setAction(0, 1)
    stage.say('@dialogue:R_17.scene0.dialogue0010')
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_17.scene0.dialogue0011')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_17.scene0.dialogue0012')
    stage.unit(109).setAction(2)
    stage.say('@dialogue:R_17.scene0.dialogue0013')
    stage.unit(109).setAction(0)
    stage.say('@dialogue:R_17.scene0.dialogue0014')
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_17.scene0.dialogue0015')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_17.scene0.dialogue0016')
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_17.scene0.dialogue0017')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(109).setAction(2)
    stage.say('@dialogue:R_17.scene0.dialogue0018')
    stage.unit(109).setAction(0)
    stage.say('@dialogue:R_17.scene0.dialogue0019')
    stage.unit(32).setAction(0, 1)
    stage.say('@dialogue:R_17.scene0.dialogue0020')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene1():
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
            3],
        [
            3,
            49,
            54,
            2]])
    stage.draw()
    stage.setStageName('백마  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_17.scene1.dialogue0001')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_17.scene1.dialogue0002')
    stage.say('@dialogue:R_17.scene1.dialogue0003')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).move(49, 16, 0)
    stage.unit(3).hide()
    stage.delay(3)
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_17.scene1.dialogue0004')
    stage.unit(21).setAction(0, 2)
    stage.say('@dialogue:R_17.scene1.dialogue0005')
    stage.showUnit(181, 49, 16, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_17.scene1.dialogue0006')
    stage.unit(22).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_17.scene1.dialogue0007')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_17.scene1.dialogue0008')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_17.scene1.dialogue0009')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_17.scene1.dialogue0010')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_17.scene1.dialogue0011')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_17.scene1.dialogue0012')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_17.scene1.dialogue0013')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_17.scene1.dialogue0014')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(22).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        2])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 17
    stage.setBattlePos([
        [
            11,
            21],
        [
            15,
            14],
        [
            20,
            14],
        [
            4,
            16],
        [
            3,
            15],
        [
            5,
            15],
        [
            15,
            13],
        [
            20,
            13],
        [
            15,
            12],
        [
            20,
            12]])
    return False


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_17.scene2.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_17.scene2.dialogue0002')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_17.scene2.dialogue0003')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_17.scene2.dialogue0004')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_17.scene2.dialogue0005')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_17.scene2.dialogue0006')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_17.scene2.dialogue0007')
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
