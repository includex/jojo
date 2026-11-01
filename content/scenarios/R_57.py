# Source Generated with Decompyle++
# File: R_57.marshal (Python 3.9)


def scene0():
    '최후의 결전'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(0)
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
            23,
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
    stage.setEventName('최후의 결전')
    stage.setStageName('오장원  조조군 본진')
    stage.delay(3)
    stage.showUnit(151, 54, 22, 2)
    stage.delay(3)
    stage.say('@dialogue:R_57.scene0.dialogue0001')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_57.scene0.dialogue0002')
    stage.unit(0).setAction(0)
    stage.unit(151).move(54, 64, 2)
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('@dialogue:R_57.scene0.dialogue0003')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_57.scene0.dialogue0004')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_57.scene0.dialogue0005')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_57.scene0.dialogue0006')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('@dialogue:R_57.scene0.dialogue0007')
    stage.unit(151).setAction(0)
    stage.say('@dialogue:R_57.scene0.dialogue0008')
    stage.delay(5)
    stage.unit(151).setAction(0, 1)
    stage.say('@dialogue:R_57.scene0.dialogue0009')
    stage.delay(5)
    stage.unit(151).setAction(0, 0)
    stage.say('@dialogue:R_57.scene0.dialogue0010')
    stage.unit(151).move(54, 22, 0)
    stage.unit(151).hide()
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 57
    stage.setBattlePos([
        [
            21,
            19],
        [
            18,
            18],
        [
            18,
            21],
        [
            21,
            20],
        [
            21,
            18],
        [
            21,
            21],
        [
            20,
            19],
        [
            20,
            20],
        [
            20,
            18],
        [
            20,
            21],
        [
            19,
            19],
        [
            19,
            20],
        [
            19,
            18],
        [
            19,
            21],
        [
            18,
            19]])
    return False


def scene1():
    if stage.battleTest():
        stage.say('@dialogue:R_57.scene1.dialogue0001')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_57.scene1.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_57.scene1.dialogue0003')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_57.scene1.dialogue0004')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_57.scene1.dialogue0005')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_57.scene1.dialogue0006')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_57.scene1.dialogue0007')
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
