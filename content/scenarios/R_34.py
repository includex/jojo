# Source Generated with Decompyle++
# File: R_34.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            2,
            54,
            80],
        [
            10,
            44,
            64,
            1],
        [
            11,
            64,
            64,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    if vars[311]:
        stage.setEventName('신의')
    if not vars[311]:
        stage.setEventName('정욱의 고백')
    stage.setStageName('합비  조조군 본진')
    stage.delay(3)
    stage.unit(11).setAction(0, 2)
    stage.say('@dialogue:R_34.scene0.dialogue0001')
    stage.say('@dialogue:R_34.scene0.dialogue0002')
    stage.unit(10).setAction(0, 2)
    stage.say('@dialogue:R_34.scene0.dialogue0003')
    stage.unit(2).setAction(0, 1)
    stage.say('@dialogue:R_34.scene0.dialogue0004')
    stage.unit(2).setAction(0, 0)
    stage.say('@dialogue:R_34.scene0.dialogue0005')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            1,
            52,
            70],
        [
            8,
            58,
            70],
        [
            15,
            64,
            70],
        [
            9,
            70,
            70],
        [
            20,
            52,
            40,
            2],
        [
            19,
            58,
            40,
            2],
        [
            24,
            64,
            40,
            2],
        [
            21,
            70,
            40,
            2],
        [
            10,
            48,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_34.scene1.dialogue0001')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(2, 407, 167)
    stage.showHead(76, 471, 187)
    stage.showHead(0, 331, 87)
    stage.delay(5)
    stage.bottomTxt('조조가 이끄는 원군이', False, False, False)
    stage.delay(5)
    stage.head(0).move(471, 87)
    stage.head(0).move(471, 107)
    stage.delay(5)
    stage.bottomTxt('직접 합비로 가서 전투를 독려했다.', True, False, True)
    stage.delay(3)
    stage.loadBg(2, 53)
    if not vars[311]:
        stage.bgSound(19)
        stage.delay(3)
        stage.showUnit(9, 44, 52, 1)
        stage.draw()
        stage.setStageName('합비  조조군 본진')
        stage.showUnit(21, 54, 22, 2)
        stage.say('@dialogue:R_34.scene2.dialogue0001')
        stage.unit(21).move(54, 52, 2)
        stage.delay(3)
        stage.unit(21).setAction(0, 3)
        stage.say('@dialogue:R_34.scene2.dialogue0002')
        stage.unit(21).setAction(0, 2)
        stage.say('@dialogue:R_34.scene2.dialogue0003')
        stage.unit(21).setAction(0, 3)
        stage.say('@dialogue:R_34.scene2.dialogue0004')
        stage.unit(21).setAction(0, 2)
        stage.say('@dialogue:R_34.scene2.dialogue0005')
        stage.unit(9).setAction(0, 3)
        stage.say('@dialogue:R_34.scene2.dialogue0006')
        stage.unit(21).setAction(0, 3)
        stage.say('@dialogue:R_34.scene2.dialogue0007')
        stage.delay(10)
        stage.bgSound(-1)
        stage.unit(21).setAction(0, 0)
        stage.say('@dialogue:R_34.scene2.dialogue0008')
        stage.bgSound(15)
        stage.delay(3)
        stage.unit(21).move(64, 52, 3)
        stage.unit(9).setAction(0, 1)
        stage.showUnit(0, 54, 22, 2)
        stage.unit(0).move(54, 28, 2)
        stage.showUnit(1, 54, 22, 2)
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                34,
                2],
            [
                stage.unit(1),
                54,
                28,
                2]])
        stage.showUnit(15, 54, 22, 2)
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                40,
                2],
            [
                stage.unit(1),
                54,
                34,
                2],
            [
                stage.unit(15),
                54,
                28,
                2]])
        stage.showUnit(20, 54, 22, 2)
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                46,
                2],
            [
                stage.unit(1),
                54,
                40,
                2],
            [
                stage.unit(15),
                54,
                34,
                2],
            [
                stage.unit(20),
                54,
                28,
                2]])
        stage.showUnit(19, 54, 22, 2)
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                52,
                2],
            [
                stage.unit(1),
                54,
                46,
                2],
            [
                stage.unit(15),
                54,
                40,
                2],
            [
                stage.unit(20),
                54,
                34,
                2],
            [
                stage.unit(19),
                54,
                28,
                2]])
        stage.unitsMove([
            [
                stage.unit(0),
                54,
                80,
                0],
            [
                stage.unit(1),
                44,
                64,
                1],
            [
                stage.unit(15),
                44,
                58,
                1],
            [
                stage.unit(20),
                64,
                64,
                3],
            [
                stage.unit(19),
                64,
                58,
                3]])
        stage.delay(10)
    if vars[311]:
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
                9,
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
        stage.setStageName('합비  조조군 본진')
    stage.bgSound(15)
    stage.delay(3)
    stage.showUnit(181, 54, 22, 2)
    stage.say('@dialogue:R_34.scene2.dialogue0009')
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_34.scene2.dialogue0010')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.say('@dialogue:R_34.scene2.dialogue0011')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_34.scene2.dialogue0012')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.say('@dialogue:R_34.scene2.dialogue0013')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_34.scene2.dialogue0014')
    stage.unit(9).setAction(0, 2)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('@dialogue:R_34.scene2.dialogue0015')
    if vars[311]:
        stage.say('@dialogue:R_34.scene2.dialogue0016')
    if not vars[311]:
        stage.say('@dialogue:R_34.scene2.dialogue0017')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_34.scene2.dialogue0018')
    stage.unit(0).setAction(0)
    stage.unit(9).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19,
        2,
        11,
        10,
        9])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 34
    stage.setBattlePos([
        [
            12,
            8],
        [
            13,
            8],
        [
            11,
            19],
        [
            10,
            18],
        [
            12,
            18],
        [
            0,
            19],
        [
            0,
            20],
        [
            0,
            19],
        [
            0,
            20],
        [
            11,
            18],
        [
            10,
            17],
        [
            12,
            17],
        [
            11,
            17],
        [
            11,
            9],
        [
            14,
            9]])
    return False


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_34.scene3.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_34.scene3.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_34.scene3.dialogue0003')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_34.scene3.dialogue0004')
    if stage.unitClickTest(9):
        stage.say('@dialogue:R_34.scene3.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_34.scene3.dialogue0006')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_34.scene3.dialogue0007')
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
