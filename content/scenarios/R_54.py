# Source Generated with Decompyle++
# File: R_54.marshal (Python 3.9)


def scene0():
    '촉군의 북상'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(50, 130, 221)
    stage.showHead(0, 194, 221)
    stage.bottomTxt('조조는 촉군을 격파하고,', False, False, False)
    stage.delay(5)
    stage.head(50).hide()
    stage.head(0).move(162, 221)
    stage.delay(5)
    stage.bottomTxt('백제성으로 들어갔다.', True, False, True)
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
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
            3],
        [
            1,
            44,
            64,
            1],
        [
            3,
            44,
            58,
            1],
        [
            17,
            44,
            52,
            1]])
    stage.draw()
    stage.setEventName('촉군의 북상')
    stage.setStageName('백제성  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_54.scene0.dialogue0001')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(2)
    stage.say('@dialogue:R_54.scene0.dialogue0002')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_54.scene0.dialogue0003')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_54.scene0.dialogue0004')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_54.scene0.dialogue0005')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_54.scene0.dialogue0006')
    stage.unit(20).setAction(0)
    stage.delay(5)
    stage.showUnit(181, 54, 22, 2)
    stage.say('@dialogue:R_54.scene0.dialogue0007')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_54.scene0.dialogue0008')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_54.scene0.dialogue0009')
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_54.scene0.dialogue0010')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_54.scene0.dialogue0011')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_54.scene0.dialogue0012')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_54.scene0.dialogue0013')
    stage.unit(181).hide()
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 54
    stage.setBattlePos([
        [
            35,
            20],
        [
            35,
            21],
        [
            35,
            19],
        [
            35,
            22],
        [
            36,
            20],
        [
            36,
            21],
        [
            36,
            19],
        [
            36,
            22],
        [
            37,
            20],
        [
            37,
            21],
        [
            37,
            19],
        [
            37,
            22],
        [
            38,
            20],
        [
            38,
            21],
        [
            38,
            19]])
    return False


def scene1():
    if stage.battleTest():
        stage.say('@dialogue:R_54.scene1.dialogue0001')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_54.scene1.dialogue0002')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_54.scene1.dialogue0003')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_54.scene1.dialogue0004')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_54.scene1.dialogue0005')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_54.scene1.dialogue0006')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_54.scene1.dialogue0007')
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
