# Source Generated with Decompyle++
# File: R_47.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 31)
    model.setRAvatar(3, 199)
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setEventName('도원에 끊어진 꿈')
    stage.setStageName('백제성 교외  산길')
    stage.showUnit(3, 5, 49, 1)
    stage.unit(3).move(51, 49, 1)
    stage.say('@dialogue:R_47.scene0.dialogue0001')
    stage.showUnit(181, 95, 55, 3)
    stage.unit(181).move(59, 55, 3)
    stage.say('@dialogue:R_47.scene0.dialogue0002')
    stage.unit(3).move(98, 49, 1)
    stage.unit(3).hide()
    stage.effectSound(201, 255)
    model.setRAvatar(3, 6)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(13)
    stage.delay(3)
    stage.loadBg(2, 5)
    stage.showUnits([
        [
            32,
            55,
            46,
            2,
            10],
        [
            203,
            58,
            43,
            2,
            13]])
    stage.draw()
    stage.setStageName('백제성')
    stage.showUnit(3, 31, 61, 1)
    stage.say('@dialogue:R_47.scene1.dialogue0001')
    stage.unit(3).move(52, 61, 1)
    stage.delay(3)
    stage.unit(3).move(52, 52, 0)
    stage.say('@dialogue:R_47.scene1.dialogue0002')
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_47.scene1.dialogue0003')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_47.scene1.dialogue0004')
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_47.scene1.dialogue0005')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_47.scene1.dialogue0006')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_47.scene1.dialogue0007')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_47.scene1.dialogue0008')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_47.scene1.dialogue0009')
    stage.delay(5)
    stage.say('@dialogue:R_47.scene1.dialogue0010')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_47.scene1.dialogue0011')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_47.scene1.dialogue0012')
    stage.say('@dialogue:R_47.scene1.dialogue0013')
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_47.scene1.dialogue0014')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_47.scene1.dialogue0015')
    stage.delay(5)
    stage.say('@dialogue:R_47.scene1.dialogue0016')
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_47.scene1.dialogue0017')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_47.scene1.dialogue0018')
    stage.unit(3).setAction(0, 1)
    stage.say('@dialogue:R_47.scene1.dialogue0019')
    stage.say('@dialogue:R_47.scene1.dialogue0020')
    stage.unit(3).setAction(0, 0)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_47.scene1.dialogue0021')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_47.scene1.dialogue0022')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_47.scene1.dialogue0023')
    stage.unit(3).setAction(9)
    stage.say('@dialogue:R_47.scene1.dialogue0024')
    stage.delay(10)
    stage.say('@dialogue:R_47.scene1.dialogue0025')
    stage.unit(3).setAction(7)
    stage.say('@dialogue:R_47.scene1.dialogue0026')
    stage.delay(10)
    stage.unit(3).setAction(9)
    stage.say('@dialogue:R_47.scene1.dialogue0027')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(0, 2)
    stage.say('@dialogue:R_47.scene1.dialogue0028')
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 31)
    model.setRAvatar(3, 199)
    stage.draw()
    stage.showUnit(3, 5, 51, 1)
    stage.setStageName('맥성 교외  산길')
    stage.unit(3).move(51, 51, 1)
    stage.say('@dialogue:R_47.scene2.dialogue0001')
    stage.unit(3).move(95, 51, 1)
    stage.unit(3).hide()
    stage.delay(3)
    stage.bgSound(15)
    model.setRAvatar(3, 6)
    stage.clsUnit()
    return False


def scene3():
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
            15,
            44,
            58,
            1],
        [
            6,
            44,
            52,
            1],
        [
            7,
            44,
            46,
            1],
        [
            9,
            44,
            40,
            1]])
    stage.draw()
    stage.info('한편……', 3)
    stage.setStageName('정군산  조조군 본진')
    stage.say('@dialogue:R_47.scene3.dialogue0001')
    stage.unit(6).setAction(0, 2)
    stage.delay(3)
    stage.unit(6).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0002')
    stage.unit(6).setAction(0)
    stage.delay(3)
    stage.unit(7).setAction(0, 2)
    stage.delay(3)
    stage.unit(7).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0003')
    stage.unit(7).setAction(0)
    stage.delay(3)
    stage.unit(9).setAction(0, 2)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0004')
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_47.scene3.dialogue0005')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0006')
    stage.unit(9).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0007')
    stage.unit(6).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0008')
    stage.unit(6).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.unit(6).setAction(0, 1)
    stage.unit(7).setAction(0, 1)
    stage.unit(9).setAction(0, 1)
    stage.unitsMove([
        [
            stage.unit(6),
            54,
            52,
            1],
        [
            stage.unit(7),
            54,
            46,
            1],
        [
            stage.unit(9),
            54,
            40,
            1]])
    stage.unitsMove([
        [
            stage.unit(9),
            54,
            22,
            0],
        [
            stage.unit(7),
            54,
            28,
            0],
        [
            stage.unit(6),
            54,
            34,
            0]])
    stage.unit(9).hide()
    stage.unitsMove([
        [
            stage.unit(7),
            54,
            22,
            0],
        [
            stage.unit(6),
            54,
            28,
            0]])
    stage.unit(7).hide()
    stage.unit(6).move(54, 22, 0)
    stage.unit(6).hide()
    stage.delay(10)
    stage.showUnit(17, 54, 22, 2)
    stage.say('@dialogue:R_47.scene3.dialogue0009')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0010')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('@dialogue:R_47.scene3.dialogue0011')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0012')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0013')
    stage.unit(17).move(54, 76, 2)
    stage.delay(3)
    stage.unit(0).setAction(5)
    stage.delay(3)
    stage.unit(17).move(54, 64, 2)
    stage.bgSound(13)
    stage.delay(10)
    model.setFace(1)
    stage.say('@dialogue:R_47.scene3.dialogue0014')
    model.setFace(0)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0015')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0016')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0017')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0018')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0019')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0020')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0021')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0022')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('@dialogue:R_47.scene3.dialogue0023')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_47.scene3.dialogue0024')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_47.scene3.dialogue0025')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0026')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0027')
    stage.unit(19).setAction(0)
    model.setFace(1)
    stage.say('@dialogue:R_47.scene3.dialogue0028')
    stage.getItem(30, 0)
    stage.getItem(121, 0)
    stage.showUnit(182, 54, 22, 2)
    stage.delay(3)
    stage.unit(182).setAction(11)
    stage.unit(0).setAction(0, 0)
    stage.unit(19).setAction(0, 3)
    stage.unit(20).setAction(0, 3)
    stage.unit(23).setAction(0, 3)
    stage.say('@dialogue:R_47.scene3.dialogue0029')
    stage.unit(0).setAction(3)
    model.setFace(0)
    stage.say('@dialogue:R_47.scene3.dialogue0030')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0031')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('@dialogue:R_47.scene3.dialogue0032')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0033')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0034')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0035')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0036')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0037')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0038')
    stage.unit(20).setAction(0, 1)
    stage.say('@dialogue:R_47.scene3.dialogue0039')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0040')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_47.scene3.dialogue0041')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0042')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.say('@dialogue:R_47.scene3.dialogue0043')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_47.scene3.dialogue0044')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_47.scene3.dialogue0045')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_47.scene3.dialogue0046')
    stage.unit(182).setAction(0)
    stage.delay(3)
    stage.unit(182).hide()
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(23).setAction(0, 3)
    stage.bgSound(15)
    stage.delay(3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10)
    gvars[4076] = 0
    gvars[4057] = 47
    stage.setBattlePos([
        [
            20,
            2],
        [
            21,
            3],
        [
            19,
            1],
        [
            21,
            2],
        [
            20,
            1],
        [
            21,
            1],
        [
            22,
            2],
        [
            20,
            0],
        [
            22,
            1],
        [
            21,
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
        stage.say('@dialogue:R_47.scene4.dialogue0001')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_47.scene4.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_47.scene4.dialogue0003')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_47.scene4.dialogue0004')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_47.scene4.dialogue0005')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_47.scene4.dialogue0006')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_47.scene4.dialogue0007')
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
