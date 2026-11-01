# Source Generated with Decompyle++
# File: R_44.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 281, 233)
    stage.showHead(76, 345, 233)
    stage.bottomTxt('적벽에서 패한 동오군은', False, False, False)
    stage.delay(5)
    stage.head(76).move(442, 219)
    stage.delay(5)
    stage.bottomTxt('건업으로 퇴각했다.', True, False, True)
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
            7,
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
            23,
            64,
            52,
            3]])
    stage.draw()
    stage.setEventName('마지막 수전')
    stage.setStageName('적벽  조조군 본진')
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_44.scene0.dialogue0001')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_44.scene0.dialogue0002')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_44.scene0.dialogue0003')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_44.scene0.dialogue0004')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_44.scene0.dialogue0005')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_44.scene0.dialogue0006')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_44.scene0.dialogue0007')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_44.scene0.dialogue0008')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_44.scene0.dialogue0009')
    stage.unit(0).setAction(0)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 12)
    gvars[4076] = 0
    gvars[4057] = 44
    stage.setBattlePos([
        [
            12,
            12],
        [
            13,
            12],
        [
            12,
            13],
        [
            13,
            13],
        [
            11,
            13],
        [
            14,
            13],
        [
            11,
            12],
        [
            14,
            12],
        [
            12,
            8],
        [
            13,
            8],
        [
            11,
            8],
        [
            14,
            8],
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
        stage.say('@dialogue:R_44.scene1.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_44.scene1.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_44.scene1.dialogue0003')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_44.scene1.dialogue0004')
    if stage.unitClickTest(7):
        stage.say('@dialogue:R_44.scene1.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_44.scene1.dialogue0006')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_44.scene1.dialogue0007')
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
