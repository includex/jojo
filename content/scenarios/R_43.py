# Source Generated with Decompyle++
# File: R_43.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.setMenuVisible(False)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 127, 213)
    stage.bottomTxt('조조', False, False, False)
    stage.delay(5)
    stage.bottomTxt('백제성에서 촉군의 잔당을 크게 무찔렀다.', True, False, False)
    stage.delay(8)
    stage.showHead(35, 191, 213)
    stage.delay(5)
    stage.bottomTxt('제갈량', True, True, False)
    stage.delay(5)
    stage.head(35).move(277, 227)
    stage.head(35).hide()
    stage.delay(5)
    stage.bottomTxt('무리를 이끌고 동오로 달아났다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 23)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('다시 만난 적벽')
    stage.setStageName('백제성 교외  산길')
    stage.showUnit(35, 5, 52, 1)
    stage.unit(35).move(50, 52, 1)
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_43.scene0.dialogue0001')
    stage.unit(35).setAction(0, 3)
    stage.say('@dialogue:R_43.scene0.dialogue0002')
    stage.delay(5)
    stage.unit(35).move(95, 52, 1)
    stage.unit(35).hide()
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    model.initLocalVar()
    stage.clsUnit()
    stage.bgSound(0)
    stage.delay(3)
    stage.setMenuVisible(False)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
        [
            4,
            44,
            52,
            1],
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
    stage.setStageName('백제성  조조군 본진')
    stage.say('@dialogue:R_43.scene1.dialogue0001')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_43.scene1.dialogue0002')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_43.scene1.dialogue0003')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_43.scene1.dialogue0004')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_43.scene1.dialogue0005')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_43.scene1.dialogue0006')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.say('@dialogue:R_43.scene1.dialogue0007')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_43.scene1.dialogue0008')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_43.scene1.dialogue0009')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_43.scene1.dialogue0010')
    stage.delay(5)
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_43.scene1.dialogue0011')
    stage.delay(3)
    stage.unit(1).setAction(0, 1)
    stage.unit(19).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 43
    stage.setBattlePos([
        [
            9,
            9],
        [
            10,
            8],
        [
            9,
            10],
        [
            10,
            9],
        [
            11,
            8],
        [
            8,
            10],
        [
            8,
            9],
        [
            9,
            8],
        [
            10,
            7],
        [
            7,
            9],
        [
            8,
            8],
        [
            9,
            7],
        [
            7,
            8],
        [
            8,
            7],
        [
            9,
            6]])
    return False


def scene2():
    if stage.battleTest():
        stage.say('@dialogue:R_43.scene2.dialogue0001')
        stage.clsUnit()
        stage.setMenuVisible(False)
        model.setFace(128)
        stage.loadBg(1, 0)
        stage.draw()
        stage.showHead(0, 157, 212)
        stage.bottomTxt('조조군', False, False, False)
        stage.delay(5)
        stage.head(0).move(280, 227)
        stage.delay(5)
        stage.bottomTxt('인연 깊은 적벽으로 향했다.', True, False, True)
        stage.loadBg(2, 57)
        stage.showUnits([
            [
                76,
                66,
                52,
                3],
            [
                88,
                52,
                41,
                2],
            [
                78,
                46,
                41,
                2],
            [
                79,
                40,
                41,
                2],
            [
                81,
                52,
                63],
            [
                86,
                46,
                63],
            [
                85,
                40,
                63],
            [
                35,
                52,
                52,
                1]])
        stage.draw()
        stage.setStageName('적벽  손권군 본진')
        stage.say('@dialogue:R_43.scene2.dialogue0002')
        stage.unit(35).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0003')
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(81).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0004')
        stage.unit(81).setAction(0)
        stage.delay(3)
        stage.unit(78).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0005')
        stage.unit(78).setAction(0)
        stage.delay(3)
        stage.unit(88).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0006')
        stage.say('@dialogue:R_43.scene2.dialogue0007')
        stage.unit(35).setAction(0, 0)
        stage.say('@dialogue:R_43.scene2.dialogue0008')
        stage.unit(88).setAction(0)
        stage.say('@dialogue:R_43.scene2.dialogue0009')
        stage.unit(35).setAction(11)
        stage.say('@dialogue:R_43.scene2.dialogue0010')
        stage.unit(88).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0011')
        stage.unit(88).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0)
        stage.say('@dialogue:R_43.scene2.dialogue0012')
        stage.unit(88).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0013')
        stage.unit(88).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0014')
        stage.unit(35).setAction(0)
        stage.say('@dialogue:R_43.scene2.dialogue0015')
        stage.say('@dialogue:R_43.scene2.dialogue0016')
        stage.say('@dialogue:R_43.scene2.dialogue0017')
        stage.unit(88).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0018')
        stage.say('@dialogue:R_43.scene2.dialogue0019')
        stage.unit(35).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0020')
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0, 1)
        stage.say('@dialogue:R_43.scene2.dialogue0021')
        stage.unit(35).setAction(0, 0)
        stage.say('@dialogue:R_43.scene2.dialogue0022')
        stage.unit(35).setAction(2)
        stage.say('@dialogue:R_43.scene2.dialogue0023')
        stage.unit(88).setAction(0)
        stage.say('@dialogue:R_43.scene2.dialogue0024')
        stage.unit(76).setAction(3)
        stage.say('@dialogue:R_43.scene2.dialogue0025')
        stage.unit(76).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0, 1)
        stage.say('@dialogue:R_43.scene2.dialogue0026')
        stage.unit(35).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0027')
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(35).move(40, 52, 3)
        stage.delay(3)
        stage.say('@dialogue:R_43.scene2.dialogue0028')
        stage.unit(35).setAction(0, 0)
        stage.delay(3)
        stage.unit(35).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0029')
        stage.say('@dialogue:R_43.scene2.dialogue0030')
        stage.delay(5)
        stage.unit(35).setAction(0)
        stage.delay(3)
        stage.unit(35).move(16, 52, 3)
        stage.unit(35).hide()
        stage.say('@dialogue:R_43.scene2.dialogue0031')
        stage.unit(81).setAction(0, 1)
        stage.delay(3)
        stage.unit(81).setAction(6)
        stage.say('@dialogue:R_43.scene2.dialogue0032')
        stage.unit(81).setAction(0)
        stage.say('@dialogue:R_43.scene2.dialogue0033')
        stage.bgSound(-1)
        stage.clsUnit()
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_43.scene2.dialogue0034')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_43.scene2.dialogue0035')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_43.scene2.dialogue0036')
    if stage.unitClickTest(4):
        stage.say('@dialogue:R_43.scene2.dialogue0037')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_43.scene2.dialogue0038')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_43.scene2.dialogue0039')
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
