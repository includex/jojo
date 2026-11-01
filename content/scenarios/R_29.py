# Source Generated with Decompyle++
# File: R_29.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 49)
    stage.showUnits([
        [
            32,
            54,
            73],
        [
            35,
            40,
            57,
            1],
        [
            3,
            67,
            57,
            3],
        [
            33,
            67,
            51,
            3],
        [
            34,
            67,
            45,
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('관우의 맹약')
    stage.setStageName('강하  유비 저택')
    stage.say('@dialogue:R_29.scene0.dialogue0001')
    stage.unit(35).setAction(0, 2)
    stage.say('@dialogue:R_29.scene0.dialogue0002')
    stage.say('@dialogue:R_29.scene0.dialogue0003')
    stage.unit(35).setAction(0, 2)
    stage.say('@dialogue:R_29.scene0.dialogue0004')
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_29.scene0.dialogue0005')
    stage.unit(34).setAction(6)
    stage.say('@dialogue:R_29.scene0.dialogue0006')
    stage.unit(33).setAction(3)
    stage.say('@dialogue:R_29.scene0.dialogue0007')
    stage.unit(34).setAction(0)
    stage.unit(33).setAction(0)
    stage.say('@dialogue:R_29.scene0.dialogue0008')
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_29.scene0.dialogue0009')
    stage.unit(34).setAction(6)
    stage.say('@dialogue:R_29.scene0.dialogue0010')
    stage.unit(33).setAction(3)
    stage.say('@dialogue:R_29.scene0.dialogue0011')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_29.scene0.dialogue0012')
    stage.unit(3).setAction(2)
    stage.say('@dialogue:R_29.scene0.dialogue0013')
    stage.unit(33).setAction(0)
    stage.unit(34).setAction(0)
    stage.delay(3)
    stage.unit(33).setAction(0, 2)
    stage.unit(34).setAction(0, 2)
    stage.delay(5)
    stage.say('@dialogue:R_29.scene0.dialogue0014')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_29.scene0.dialogue0015')
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_29.scene0.dialogue0016')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_29.scene0.dialogue0017')
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_29.scene0.dialogue0018')
    stage.unit(3).setAction(0)
    stage.effectSound(203, 255)
    stage.bgSound(19)
    stage.delay(3)
    stage.say('@dialogue:R_29.scene0.dialogue0019')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_29.scene0.dialogue0020')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 2)
    stage.say('@dialogue:R_29.scene0.dialogue0021')
    stage.unit(3).setAction(2)
    stage.say('@dialogue:R_29.scene0.dialogue0022')
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_29.scene0.dialogue0023')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_29.scene0.dialogue0024')
    stage.say('@dialogue:R_29.scene0.dialogue0025')
    stage.say('@dialogue:R_29.scene0.dialogue0026')
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_29.scene0.dialogue0027')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_29.scene0.dialogue0028')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_29.scene0.dialogue0029')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_29.scene0.dialogue0030')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(34),
            67,
            10,
            0],
        [
            stage.unit(33),
            67,
            10,
            0],
        [
            stage.unit(3),
            67,
            10,
            0]])
    stage.unit(34).hide()
    stage.unit(33).hide()
    stage.unit(3).hide()
    stage.delay(5)
    stage.say('@dialogue:R_29.scene0.dialogue0031')
    stage.unit(35).setAction(0, 2)
    stage.say('@dialogue:R_29.scene0.dialogue0032')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_29.scene0.dialogue0033')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_29.scene0.dialogue0034')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
        [
            2,
            44,
            64,
            1],
        [
            21,
            64,
            64,
            3]])
    stage.draw()
    stage.setStageName('오림  조조군 본진')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_29.scene1.dialogue0001')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 5, [
        2,
        21,
        15,
        13])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 29
    stage.setBattlePos([
        [
            34,
            19],
        [
            33,
            20],
        [
            34,
            19],
        [
            33,
            8],
        [
            32,
            8],
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
            0],
        [
            0,
            0],
        [
            0,
            0]])
    return False


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_29.scene2.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_29.scene2.dialogue0002')
    if stage.unitClickTest(2):
        stage.say('@dialogue:R_29.scene2.dialogue0003')
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
