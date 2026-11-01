# Source Generated with Decompyle++
# File: R_37.marshal (Python 3.9)


def scene0():
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
            1,
            44,
            64,
            1],
        [
            15,
            44,
            58,
            1],
        [
            13,
            44,
            52,
            1],
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
            3]])
    stage.draw()
    stage.setEventName('군량고 수비')
    stage.setStageName('한수 부근  조조군 본진')
    stage.say('@dialogue:R_37.scene0.dialogue0001')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_37.scene0.dialogue0002')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_37.scene0.dialogue0003')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_37.scene0.dialogue0004')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 8)
    gvars[4076] = 0
    gvars[4057] = 37
    stage.setBattlePos([
        [
            9,
            23],
        [
            10,
            23],
        [
            8,
            23],
        [
            11,
            23],
        [
            8,
            23],
        [
            11,
            23],
        [
            9,
            23],
        [
            10,
            23],
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


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_37.scene1.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(13):
        stage.say('@dialogue:R_37.scene1.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_37.scene1.dialogue0003')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_37.scene1.dialogue0004')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_37.scene1.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_37.scene1.dialogue0006')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_37.scene1.dialogue0007')
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
