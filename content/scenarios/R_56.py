# Source Generated with Decompyle++
# File: R_56.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(0)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 68, 135)
    stage.showHead(50, 144, 95)
    stage.bottomTxt('조조군', False, False, False)
    stage.delay(5)
    stage.head(0).move(80, 95)
    stage.delay(5)
    stage.bottomTxt('양평관을 공격하고', True, False, False)
    stage.delay(5)
    stage.head(50).hide()
    stage.delay(5)
    stage.bottomTxt('관문을 지키던 촉군을 대파했다.', True, False, True)
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
            15,
            44,
            52,
            1]])
    stage.draw()
    stage.setEventName('적은 오장원에서')
    stage.setStageName('양평관  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_56.scene0.dialogue0001')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0002')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_56.scene0.dialogue0003')
    stage.delay(5)
    stage.showUnit(19, 54, 22, 2)
    stage.say('@dialogue:R_56.scene0.dialogue0004')
    stage.unit(19).move(54, 58, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0005')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('@dialogue:R_56.scene0.dialogue0006')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0007')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0008')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0009')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0010')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_56.scene0.dialogue0011')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_56.scene0.dialogue0012')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_56.scene0.dialogue0013')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0014')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_56.scene0.dialogue0015')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0016')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_56.scene0.dialogue0017')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_56.scene0.dialogue0018')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.showUnit(182, 54, 22, 2)
    stage.delay(3)
    stage.unit(182).setAction(11)
    stage.say('@dialogue:R_56.scene0.dialogue0019')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_56.scene0.dialogue0020')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_56.scene0.dialogue0021')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_56.scene0.dialogue0022')
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(1, 0)
    stage.showHead(0, 94, 99)
    stage.draw()
    stage.bottomTxt('조조군은,', False, False, False)
    stage.delay(5)
    stage.showHead(35, 169, 32)
    stage.delay(5)
    stage.bottomTxt('오장원으로 향하다', True, False, False)
    stage.delay(5)
    stage.head(0).move(105, 32)
    stage.delay(5)
    stage.bottomTxt('제갈량을 추격했다.', True, False, True)
    stage.bgSound(12)
    stage.delay(3)
    stage.loadBg(2, 66)
    stage.draw()
    stage.showUnits([
        [
            187,
            5,
            56,
            1],
        [
            17,
            5,
            48,
            1]])
    stage.info('초선은 황건교도 첩자와 함께 허도를 수색했다.')
    stage.setStageName('허도  길모퉁이')
    stage.unitsMove([
        [
            stage.unit(187),
            48,
            56,
            1],
        [
            stage.unit(17),
            48,
            48,
            1]])
    stage.unit(187).setAction(0)
    stage.delay(5)
    stage.unit(187).setAction(0, 0)
    stage.say('@dialogue:R_56.scene1.dialogue0001')
    stage.unit(17).setAction(0, 2)
    stage.say('@dialogue:R_56.scene1.dialogue0002')
    stage.unit(187).setAction(0, 1)
    stage.say('@dialogue:R_56.scene1.dialogue0003')
    stage.unit(17).setAction(0, 1)
    stage.say('@dialogue:R_56.scene1.dialogue0004')
    stage.showUnit(188, 5, 48, 1)
    stage.unit(188).move(40, 48, 1)
    stage.unit(188).setAction(0)
    stage.delay(3)
    stage.unit(187).setAction(0, 3)
    stage.unit(17).setAction(0, 3)
    stage.say('@dialogue:R_56.scene1.dialogue0005')
    stage.delay(10)
    stage.showUnit(151, 95, 52, 3)
    stage.unit(151).move(76, 52, 3)
    stage.unit(151).setAction(0)
    stage.delay(5)
    stage.unit(187).setAction(0, 1)
    stage.say('@dialogue:R_56.scene1.dialogue0006')
    stage.unitsMove([
        [
            stage.unit(17),
            66,
            48,
            1],
        [
            stage.unit(187),
            66,
            56,
            1],
        [
            stage.unit(188),
            58,
            48,
            1]])
    stage.unit(187).setAction(0)
    stage.unit(188).setAction(0)
    stage.say('@dialogue:R_56.scene1.dialogue0007')
    stage.unit(151).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0008')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0009')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0010')
    stage.unit(151).setAction(0)
    stage.say('@dialogue:R_56.scene1.dialogue0011')
    stage.unit(17).setAction(0, 2)
    stage.say('@dialogue:R_56.scene1.dialogue0012')
    stage.unit(17).setAction(0, 1)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0013')
    stage.unit(17).setAction(0)
    stage.delay(5)
    stage.unit(151).setAction(0, 2)
    stage.say('@dialogue:R_56.scene1.dialogue0014')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0015')
    stage.unit(151).setAction(0, 3)
    stage.say('@dialogue:R_56.scene1.dialogue0016')
    stage.delay(5)
    stage.unit(151).setAction(0, 2)
    stage.say('@dialogue:R_56.scene1.dialogue0017')
    stage.unit(151).setAction(0, 3)
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0018')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0019')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(0, 2)
    stage.say('@dialogue:R_56.scene1.dialogue0020')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0021')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(0, 3)
    stage.say('@dialogue:R_56.scene1.dialogue0022')
    stage.unit(151).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0023')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(0, 2)
    stage.say('@dialogue:R_56.scene1.dialogue0024')
    stage.unit(151).setAction(0, 3)
    stage.say('@dialogue:R_56.scene1.dialogue0025')
    stage.unit(151).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0026')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_56.scene1.dialogue0027')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_56.scene1.dialogue0028')
    stage.unit(17).setAction(0, 2)
    stage.say('@dialogue:R_56.scene1.dialogue0029')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
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
    stage.setStageName('오장원  조조군 본진')
    stage.delay(3)
    stage.showUnit(23, 54, 22, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_56.scene2.dialogue0001')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(23).move(54, 52, 2)
    stage.delay(3)
    stage.unit(23).setAction(0, 3)
    stage.say('@dialogue:R_56.scene2.dialogue0002')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_56.scene2.dialogue0003')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_56.scene2.dialogue0004')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_56.scene2.dialogue0005')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_56.scene2.dialogue0006')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_56.scene2.dialogue0007')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_56.scene2.dialogue0008')
    stage.unit(0).setAction(0)
    stage.unit(23).move(64, 52, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 56
    stage.setBattlePos([
        [
            4,
            6],
        [
            3,
            6],
        [
            5,
            6],
        [
            2,
            6],
        [
            4,
            5],
        [
            3,
            5],
        [
            5,
            5],
        [
            2,
            5],
        [
            4,
            4],
        [
            3,
            4],
        [
            5,
            4],
        [
            2,
            4],
        [
            4,
            3],
        [
            3,
            3],
        [
            5,
            3]])
    return False


def scene3():
    if stage.battleTest():
        stage.say('@dialogue:R_56.scene3.dialogue0001')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_56.scene3.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_56.scene3.dialogue0003')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_56.scene3.dialogue0004')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_56.scene3.dialogue0005')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_56.scene3.dialogue0006')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_56.scene3.dialogue0007')
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
