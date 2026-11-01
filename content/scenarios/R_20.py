# Source Generated with Decompyle++
# File: R_20.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(13)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 73)
    stage.draw()
    stage.setEventName('원소의 죽음')
    stage.setStageName('창정  강가')
    stage.showUnits([
        [
            113,
            95,
            77,
            3],
        [
            114,
            95,
            69,
            3],
        [
            101,
            95,
            77,
            3],
        [
            115,
            95,
            69,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(113),
            83,
            77,
            3],
        [
            stage.unit(114),
            83,
            69,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(113),
            50,
            77,
            3],
        [
            stage.unit(114),
            50,
            69,
            3],
        [
            stage.unit(101),
            62,
            77,
            3],
        [
            stage.unit(115),
            62,
            69,
            3]])
    stage.say('@dialogue:R_20.scene0.dialogue0001')
    stage.unit(101).setAction(9)
    stage.delay(3)
    stage.unit(114).setAction(0, 1)
    stage.unit(115).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(0, 1)
    stage.say('@dialogue:R_20.scene0.dialogue0002')
    stage.unit(114).move(52, 69, 1)
    stage.say('@dialogue:R_20.scene0.dialogue0003')
    stage.say('@dialogue:R_20.scene0.dialogue0004')
    stage.unit(115).move(62, 71, 2)
    stage.say('@dialogue:R_20.scene0.dialogue0005')
    stage.unit(113).setAction(3)
    stage.say('@dialogue:R_20.scene0.dialogue0006')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:R_20.scene0.dialogue0007')
    stage.unit(101).setAction(1)
    stage.delay(3)
    stage.unit(101).setAction(1, 2)
    stage.say('@dialogue:R_20.scene0.dialogue0008')
    stage.unit(101).setAction(10)
    stage.say('@dialogue:R_20.scene0.dialogue0009')
    stage.info('천하를 차지할 가능성이 가장 컸던 원소는 죽었다.\n남겨진 세 아들은 저마다 다른 속내를 품고\n서로를 경계하며 업성으로 물러났다.')
    stage.bgSound(-1)
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
            2,
            38,
            42,
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
            3]])
    stage.draw()
    stage.delay(3)
    stage.setStageName('창정  조조군 본진')
    stage.showUnit(181, 49, 16, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0001')
    stage.unit(181).move(49, 22, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_20.scene1.dialogue0002')
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0003')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0004')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0005')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0006')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_20.scene1.dialogue0007')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.delay(3)
    stage.unit(1).setAction(3, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0008')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0009')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0010')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_20.scene1.dialogue0011')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_20.scene1.dialogue0012')
    stage.unit(1).setAction(17)
    stage.say('@dialogue:R_20.scene1.dialogue0013')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_20.scene1.dialogue0014')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_20.scene1.dialogue0015')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_20.scene1.dialogue0016')
    stage.say('@dialogue:R_20.scene1.dialogue0017')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_20.scene1.dialogue0018')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_20.scene1.dialogue0019')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_20.scene1.dialogue0020')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        2,
        7,
        6])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 20
    stage.setBattlePos([
        [
            12,
            1],
        [
            13,
            2],
        [
            10,
            1],
        [
            15,
            2],
        [
            9,
            2],
        [
            16,
            1],
        [
            9,
            0],
        [
            15,
            0],
        [
            12,
            3],
        [
            14,
            3],
        [
            13,
            4],
        [
            14,
            1],
        [
            11,
            2],
        [
            13,
            0],
        [
            11,
            0]])
    return False


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_20.scene2.dialogue0001')
        stage.bgSound(-1)
        stage.clsUnit()
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_20.scene2.dialogue0002')
    if stage.unitClickTest(2):
        stage.say('@dialogue:R_20.scene2.dialogue0003')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_20.scene2.dialogue0004')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_20.scene2.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_20.scene2.dialogue0006')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_20.scene2.dialogue0007')
    return False


def scene3():
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            3,
            42,
            52,
            1],
        [
            33,
            42,
            58,
            1],
        [
            74,
            67,
            52,
            3],
        [
            75,
            67,
            58,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('양산  유비군 본진')
    stage.delay(3)
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_20.scene3.dialogue0001')
    stage.say('@dialogue:R_20.scene3.dialogue0002')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_20.scene3.dialogue0003')
    stage.unit(33).setAction(0, 0)
    stage.say('@dialogue:R_20.scene3.dialogue0004')
    stage.unit(33).setAction(0, 1)
    stage.say('@dialogue:R_20.scene3.dialogue0005')
    stage.unit(74).setAction(3)
    stage.say('@dialogue:R_20.scene3.dialogue0006')
    stage.unit(74).setAction(0)
    stage.delay(3)
    stage.unit(75).setAction(3)
    stage.say('@dialogue:R_20.scene3.dialogue0007')
    stage.unit(75).setAction(0)
    stage.say('@dialogue:R_20.scene3.dialogue0008')
    stage.unit(32).setAction(17)
    stage.say('@dialogue:R_20.scene3.dialogue0009')
    stage.effectSound(202, 255)
    stage.clsUnit()
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
