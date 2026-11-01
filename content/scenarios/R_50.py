# Source Generated with Decompyle++
# File: R_50.marshal (Python 3.9)


def scene0():
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.loadBg(2, 72)
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('모든 것은 동오를 위해')
    stage.setStageName('적벽 교외  강가')
    stage.showUnit(76, 69, 95)
    stage.unit(76).move(69, 85, 0)
    stage.showUnit(77, 69, 95)
    stage.unitsMove([
        [
            stage.unit(76),
            69,
            45,
            0],
        [
            stage.unit(77),
            69,
            55,
            0]])
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('@dialogue:R_50.scene0.dialogue0001')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(76).setAction(0, 2)
    stage.say('@dialogue:R_50.scene0.dialogue0002')
    stage.unit(77).setAction(6)
    stage.say('@dialogue:R_50.scene0.dialogue0003')
    stage.unit(77).setAction(0)
    stage.say('@dialogue:R_50.scene0.dialogue0004')
    stage.unit(77).setAction(6)
    stage.say('@dialogue:R_50.scene0.dialogue0005')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 1)
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('@dialogue:R_50.scene0.dialogue0006')
    stage.unit(77).setAction(0)
    stage.say('@dialogue:R_50.scene0.dialogue0007')
    stage.unit(77).setAction(0, 0)
    stage.delay(3)
    stage.unit(77).setAction(6)
    stage.say('@dialogue:R_50.scene0.dialogue0008')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 1)
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('@dialogue:R_50.scene0.dialogue0009')
    stage.unit(77).setAction(0)
    stage.say('@dialogue:R_50.scene0.dialogue0010')
    stage.unit(77).setAction(0, 0)
    stage.delay(3)
    stage.unit(77).setAction(6)
    stage.say('@dialogue:R_50.scene0.dialogue0011')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 1)
    stage.say('@dialogue:R_50.scene0.dialogue0012')
    stage.unit(77).setAction(17)
    stage.say('@dialogue:R_50.scene0.dialogue0013')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 0)
    stage.delay(3)
    stage.unit(77).setAction(11)
    stage.say('@dialogue:R_50.scene0.dialogue0014')
    stage.unit(77).setAction(0)
    stage.say('@dialogue:R_50.scene0.dialogue0015')
    stage.unit(77).setAction(6)
    stage.say('@dialogue:R_50.scene0.dialogue0016')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).move(75, 55, 1)
    stage.unit(77).move(75, 10, 1)
    stage.unit(76).setAction(0, 0)
    stage.unit(77).hide()
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 288, 231)
    stage.showHead(76, 352, 231)
    stage.bottomTxt('오군은 적벽에서 패했고,', False, False, False)
    stage.delay(5)
    stage.head(76).move(443, 214)
    stage.delay(5)
    stage.bottomTxt('유수구로 물러났다.', True, False, True)
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
            15,
            44,
            52,
            1]])
    stage.draw()
    stage.setStageName('적벽  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_50.scene1.dialogue0001')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_50.scene1.dialogue0002')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_50.scene1.dialogue0003')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_50.scene1.dialogue0004')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_50.scene1.dialogue0005')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_50.scene1.dialogue0006')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_50.scene1.dialogue0007')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_50.scene1.dialogue0008')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 3)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_50.scene1.dialogue0009')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_50.scene1.dialogue0010')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_50.scene1.dialogue0011')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_50.scene1.dialogue0012')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_50.scene1.dialogue0013')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_50.scene1.dialogue0014')
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_50.scene1.dialogue0015')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_50.scene1.dialogue0016')
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(23).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19])
    gvars[4076] = 0
    gvars[4057] = 50
    stage.setBattlePos([
        [
            12,
            12],
        [
            13,
            12],
        [
            11,
            12],
        [
            14,
            12],
        [
            10,
            12],
        [
            15,
            12],
        [
            12,
            11],
        [
            13,
            11],
        [
            11,
            11],
        [
            14,
            11],
        [
            10,
            11],
        [
            15,
            11],
        [
            12,
            8],
        [
            13,
            8],
        [
            12,
            7]])
    return False


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_50.scene2.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_50.scene2.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_50.scene2.dialogue0003')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_50.scene2.dialogue0004')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_50.scene2.dialogue0005')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_50.scene2.dialogue0006')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_50.scene2.dialogue0007')
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
