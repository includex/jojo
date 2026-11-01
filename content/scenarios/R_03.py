# Source Generated with Decompyle++
# File: R_03.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            127,
            32,
            53,
            1],
        [
            132,
            32,
            44,
            1],
        [
            192,
            50,
            40,
            2],
        [
            193,
            58,
            40,
            2],
        [
            181,
            50,
            70],
        [
            182,
            58,
            70]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('낙양의 대화재')
    stage.setStageName('낙양  승상부')
    stage.unit(127).setAction(0, 0)
    stage.say('@dialogue:R_03.scene0.dialogue0001')
    stage.unit(132).setAction(0, 2)
    stage.say('@dialogue:R_03.scene0.dialogue0002')
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_03.scene0.dialogue0003')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(132).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0004')
    stage.say('@dialogue:R_03.scene0.dialogue0005')
    stage.unit(132).setAction(0)
    stage.say('@dialogue:R_03.scene0.dialogue0006')
    stage.unit(127).setAction(0, 3)
    stage.say('@dialogue:R_03.scene0.dialogue0007')
    stage.unit(192).setAction(3)
    stage.say('@dialogue:R_03.scene0.dialogue0008')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0009')
    stage.unit(127).setAction(0, 1)
    stage.say('@dialogue:R_03.scene0.dialogue0010')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0011')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(2)
    stage.say('@dialogue:R_03.scene0.dialogue0012')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0013')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0014')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.say('@dialogue:R_03.scene0.dialogue0015')
    stage.unit(127).setAction(0, 3)
    stage.say('@dialogue:R_03.scene0.dialogue0016')
    stage.unit(127).setAction(-1, 0)
    stage.say('@dialogue:R_03.scene0.dialogue0017')
    stage.unit(132).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0018')
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_03.scene0.dialogue0019')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(0, 1)
    stage.say('@dialogue:R_03.scene0.dialogue0020')
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_03.scene0.dialogue0021')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(132).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0022')
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(0, 3)
    stage.say('@dialogue:R_03.scene0.dialogue0023')
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_03.scene0.dialogue0024')
    stage.unit(193).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0025')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_03.scene0.dialogue0026')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 39)
    stage.showUnits([
        [
            153,
            69,
            60],
        [
            17,
            69,
            50,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('낙양  왕윤의 저택')
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_03.scene1.dialogue0001')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(3)
    stage.say('@dialogue:R_03.scene1.dialogue0002')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_03.scene1.dialogue0003')
    stage.unit(153).setAction(0, 1)
    stage.say('@dialogue:R_03.scene1.dialogue0004')
    stage.delay(5)
    stage.say('@dialogue:R_03.scene1.dialogue0005')
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_03.scene1.dialogue0006')
    if vars[302]:
        stage.unit(17).setAction(3)
        stage.say('@dialogue:R_03.scene1.dialogue0007')
        stage.unit(17).setAction(0)
        stage.say('@dialogue:R_03.scene1.dialogue0008')
        stage.unit(153).setAction(0, 1)
        stage.say('@dialogue:R_03.scene1.dialogue0009')
        stage.unit(153).setAction(3)
        stage.say('@dialogue:R_03.scene1.dialogue0010')
        stage.unit(153).setAction(0)
        stage.delay(5)
        stage.unit(153).setAction(0, 0)
        stage.say('@dialogue:R_03.scene1.dialogue0011')
        stage.unit(17).setAction(6)
        stage.unit(17).setAction(0, 0)
        stage.say('@dialogue:R_03.scene1.dialogue0012')
        stage.unit(17).setAction(0, 2)
        stage.delay(3)
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_03.scene1.dialogue0013')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('@dialogue:R_03.scene1.dialogue0014')
        stage.unit(153).setAction(0)
    if not vars[302]:
        stage.say('@dialogue:R_03.scene1.dialogue0015')
        stage.delay(5)
        stage.say('@dialogue:R_03.scene1.dialogue0016')
        stage.say('@dialogue:R_03.scene1.dialogue0017')
        stage.say('@dialogue:R_03.scene1.dialogue0018')
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_03.scene1.dialogue0019')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('@dialogue:R_03.scene1.dialogue0020')
        stage.unit(153).setAction(0)
    stage.delay(5)
    stage.showUnit(181, 55, 30, 2)
    stage.say('@dialogue:R_03.scene1.dialogue0021')
    stage.delay(5)
    stage.unit(17).setAction(0, 0)
    stage.delay(3)
    stage.say('@dialogue:R_03.scene1.dialogue0022')
    stage.say('@dialogue:R_03.scene1.dialogue0023')
    stage.say('@dialogue:R_03.scene1.dialogue0024')
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_03.scene1.dialogue0025')
    stage.unit(153).setAction(0)
    stage.delay(5)
    stage.unit(153).setAction(0, 1)
    stage.delay(5)
    stage.say('@dialogue:R_03.scene1.dialogue0026')
    stage.delay(10)
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_03.scene1.dialogue0027')
    stage.say('@dialogue:R_03.scene1.dialogue0028')
    stage.delay(3)
    stage.unit(181).hide()
    stage.delay(8)
    stage.unit(153).setAction(0, 1)
    stage.say('@dialogue:R_03.scene1.dialogue0029')
    stage.unit(17).setAction(0, 2)
    stage.delay(5)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_03.scene1.dialogue0030')
    stage.unit(17).setAction(0)
    stage.delay(5)
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_03.scene1.dialogue0031')
    stage.unit(153).setAction(0, 1)
    stage.say('@dialogue:R_03.scene1.dialogue0032')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_03.scene1.dialogue0033')
    stage.delay(5)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 361, 45)
    stage.showHead(101, 425, 45)
    stage.showHead(161, 489, 45)
    stage.showHead(136, 361, 125)
    stage.showHead(99, 425, 125)
    stage.showHead(145, 489, 125)
    stage.delay(5)
    stage.showHead(127, 280, 78)
    stage.bottomTxt('동탁은 연합군을 피해,', False, True, False)
    stage.delay(5)
    stage.head(127).move(191, 78)
    stage.bottomTxt('낙양을 불태운 뒤 장안으로 천도했다.', True, True, False)
    stage.delay(5)
    stage.bottomTxt('낙양은 도읍으로서의 역사를 마감했다.', True, True, True)
    stage.bottomTxt('연합군은 큰불을 보고 몹시 놀라', False, True, False)
    stage.head(0).move(280, 45)
    stage.head(101).move(344, 45)
    stage.head(161).move(408, 45)
    stage.head(136).move(280, 125)
    stage.head(99).move(344, 125)
    stage.head(145).move(408, 125)
    stage.bottomTxt('이에 행군을 서둘러 낙양성에 들어갔다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            101,
            54,
            37,
            2],
        [
            0,
            44,
            46,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('낙양  연합군 본진')
    stage.say('@dialogue:R_03.scene2.dialogue0001')
    stage.say('@dialogue:R_03.scene2.dialogue0002')
    stage.say('@dialogue:R_03.scene2.dialogue0003')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_03.scene2.dialogue0004')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_03.scene2.dialogue0005')
    stage.say('@dialogue:R_03.scene2.dialogue0006')
    stage.delay(5)
    stage.unit(101).setAction(0, 0)
    stage.say('@dialogue:R_03.scene2.dialogue0007')
    stage.delay(3)
    stage.unit(101).setAction(0, 2)
    stage.say('@dialogue:R_03.scene2.dialogue0008')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_03.scene2.dialogue0009')
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_03.scene2.dialogue0010')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_03.scene2.dialogue0011')
    stage.effectSound(203, 255)
    stage.delay(3)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('직접 동탁을 추격한다\n동탁 추격을 포기한다', -1)
    if sel == 1:
        vars[303] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.say('@dialogue:R_03.scene2.dialogue0012')
        stage.delay(5)
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_03.scene2.dialogue0013')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(101).setAction(3)
        stage.say('@dialogue:R_03.scene2.dialogue0014')
        stage.unit(101).setAction(0)
        stage.say('@dialogue:R_03.scene2.dialogue0015')
        stage.say('@dialogue:R_03.scene2.dialogue0016')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_03.scene2.dialogue0017')
        stage.unit(0).move(44, 98, 2)
        stage.unit(0).hide()
        stage.delay(5)
        stage.unit(101).setAction(0, 1)
        stage.say('@dialogue:R_03.scene2.dialogue0018')
        stage.effectSound(203, 255)
        stage.clsUnit()
        return False
    if sel == 2:
        vars[303] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.say('@dialogue:R_03.scene2.dialogue0019')
        stage.unit(101).setAction(3)
        stage.say('@dialogue:R_03.scene2.dialogue0020')
        stage.unit(101).setAction(0)
        stage.say('@dialogue:R_03.scene2.dialogue0021')
        stage.say('@dialogue:R_03.scene2.dialogue0022')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_03.scene2.dialogue0023')
        stage.unit(101).setAction(2)
        stage.say('@dialogue:R_03.scene2.dialogue0024')
        stage.unit(101).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.delay(5)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_03.scene2.dialogue0025')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_03.scene2.dialogue0026')
        stage.unit(0).setAction(0, 2)
        stage.delay(5)
        stage.say('@dialogue:R_03.scene2.dialogue0027')
        stage.unit(0).move(44, 98, 2)
        stage.unit(0).hide()
        stage.delay(5)
        stage.unit(101).setAction(0, 1)
        stage.say('@dialogue:R_03.scene2.dialogue0028')
        stage.effectSound(203, 255)
        stage.clsUnit()
        stage.jumpScene(7)
    return False


def scene3():
    stage.bgSound(15)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            66],
        [
            1,
            39,
            52,
            1],
        [
            5,
            39,
            46,
            1],
        [
            10,
            39,
            40,
            1],
        [
            6,
            60,
            52,
            3],
        [
            12,
            60,
            46,
            3],
        [
            11,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('낙양  조조군 본진')
    stage.say('@dialogue:R_03.scene3.dialogue0001')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        1])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 3
    stage.setBattlePos([
        [
            0,
            6],
        [
            1,
            5],
        [
            1,
            8],
        [
            1,
            6],
        [
            1,
            7],
        [
            0,
            5],
        [
            0,
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
            0]])
    return False


def scene4():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_03.scene4.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_03.scene4.dialogue0002')
    if stage.unitClickTest(6):
        stage.say('@dialogue:R_03.scene4.dialogue0003')
    if stage.unitClickTest(5):
        stage.say('@dialogue:R_03.scene4.dialogue0004')
    if stage.unitClickTest(12):
        stage.say('@dialogue:R_03.scene4.dialogue0005')
    if stage.unitClickTest(10):
        stage.say('@dialogue:R_03.scene4.dialogue0006')
    if stage.unitClickTest(11):
        stage.say('@dialogue:R_03.scene4.dialogue0007')
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
