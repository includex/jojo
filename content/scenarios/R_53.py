# Source Generated with Decompyle++
# File: R_53.marshal (Python 3.9)


def scene0():
    '백색 제단'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
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
    stage.setEventName('백색 제단')
    stage.setStageName('어복포  조조군 본진')
    model.setFace(1)
    stage.say('@dialogue:R_53.scene0.dialogue0001')
    model.setFace(0)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_53.scene0.dialogue0002')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_53.scene0.dialogue0003')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_53.scene0.dialogue0004')
    stage.unit(0).setAction(0)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10)
    gvars[4076] = 0
    gvars[4057] = 53
    stage.setBattlePos([
        [
            13,
            20],
        [
            14,
            20],
        [
            12,
            20],
        [
            15,
            20],
        [
            13,
            21],
        [
            14,
            21],
        [
            12,
            21],
        [
            15,
            21],
        [
            13,
            22],
        [
            14,
            22],
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


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_53.scene1.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_53.scene1.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_53.scene1.dialogue0003')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_53.scene1.dialogue0004')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_53.scene1.dialogue0005')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_53.scene1.dialogue0006')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_53.scene1.dialogue0007')
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
