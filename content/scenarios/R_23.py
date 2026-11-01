# Source Generated with Decompyle++
# File: R_23.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 28)
    stage.showUnits([
        [
            181,
            58,
            70,
            1],
        [
            8,
            58,
            16,
            2]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setEventName('목표는 원담')
    stage.setStageName('업성 원상 저택 앞')
    stage.unit(8).move(58, 54, 2)
    stage.delay(3)
    stage.unit(8).setAction(0, 3)
    stage.say('@dialogue:R_23.scene0.dialogue0001')
    stage.unit(181).move(58, 64, 0)
    stage.say('@dialogue:R_23.scene0.dialogue0002')
    stage.unit(8).setAction(0, 2)
    stage.say('@dialogue:R_23.scene0.dialogue0003')
    stage.unit(8).move(43, 54, 3)
    stage.say('@dialogue:R_23.scene0.dialogue0004')
    stage.effectSound(205, 255)
    stage.clsUnit()
    stage.loadBg(2, 28)
    stage.showUnit(181, 58, 70, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('그날 저녁', 3)
    stage.showUnits([
        [
            0,
            58,
            16,
            2],
        [
            22,
            58,
            6,
            2]])
    stage.unitsMove([
        [
            stage.unit(0),
            58,
            54,
            2],
        [
            stage.unit(22),
            58,
            44,
            2]])
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(0, 0)
    stage.say('@dialogue:R_23.scene0.dialogue0005')
    stage.say('@dialogue:R_23.scene0.dialogue0006')
    stage.delay(5)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_23.scene0.dialogue0007')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_23.scene0.dialogue0008')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_23.scene0.dialogue0009')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_23.scene0.dialogue0010')
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_23.scene0.dialogue0011')
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('조비를 벌한다\n조비를 용서한다', 0)
    if sel == 1:
        vars[308] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_23.scene0.dialogue0012')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_23.scene0.dialogue0013')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_23.scene0.dialogue0014')
        stage.unit(22).setAction(0)
        stage.delay(3)
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_23.scene0.dialogue0015')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_23.scene0.dialogue0016')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_23.scene0.dialogue0017')
        stage.unit(22).setAction(0)
        stage.say('@dialogue:R_23.scene0.dialogue0018')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_23.scene0.dialogue0019')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_23.scene0.dialogue0020')
    elif sel == 2:
        vars[308] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('@dialogue:R_23.scene0.dialogue0021')
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_23.scene0.dialogue0022')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_23.scene0.dialogue0023')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_23.scene0.dialogue0024')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_23.scene0.dialogue0025')
    model.setFace(0)
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            0,
            53,
            28,
            2],
        [
            1,
            68,
            48,
            3],
        [
            15,
            68,
            54,
            3],
        [
            8,
            68,
            60,
            3],
        [
            13,
            68,
            66,
            3],
        [
            2,
            68,
            72,
            3],
        [
            20,
            38,
            48,
            1],
        [
            22,
            38,
            54,
            1],
        [
            24,
            38,
            60,
            1],
        [
            21,
            38,
            66,
            1],
        [
            23,
            38,
            72,
            1],
        [
            192,
            53,
            48]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('업성이 함락되었다는 소식을 들은 원담은 조조에게 사신을 보냈다.')
    stage.setStageName('업@임시 승상부')
    stage.say('@dialogue:R_23.scene1.dialogue0001')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_23.scene1.dialogue0002')
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_23.scene1.dialogue0003')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_23.scene1.dialogue0004')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_23.scene1.dialogue0005')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_23.scene1.dialogue0006')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_23.scene1.dialogue0007')
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_23.scene1.dialogue0008')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_23.scene1.dialogue0009')
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_23.scene1.dialogue0010')
    stage.unit(192).setAction(3)
    stage.say('@dialogue:R_23.scene1.dialogue0011')
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_23.scene1.dialogue0012')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_23.scene1.dialogue0013')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_23.scene1.dialogue0014')
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_23.scene1.dialogue0015')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).move(53, 98, 2)
    stage.unit(192).hide()
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.unit(1).setAction(0, 0)
    stage.say('@dialogue:R_23.scene1.dialogue0016')
    stage.unit(1).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_23.scene1.dialogue0017')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(17)
    stage.say('@dialogue:R_23.scene1.dialogue0018')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(4)
    stage.say('@dialogue:R_23.scene1.dialogue0019')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_23.scene1.dialogue0020')
    stage.unit(24).setAction(0, 0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_23.scene1.dialogue0021')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_23.scene1.dialogue0022')
    stage.unit(22).setAction(0)
    stage.say('@dialogue:R_23.scene1.dialogue0023')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 308, 45)
    stage.showHead(115, 372, 45)
    stage.showHead(113, 460, 28)
    stage.delay(8)
    stage.head(115).move(390, 30)
    stage.delay(8)
    stage.head(115).hide()
    stage.delay(5)
    stage.bottomTxt('원상이 업성을 버리고 달아난 뒤', False, False, False)
    stage.delay(5)
    stage.head(0).move(340, 45)
    stage.delay(5)
    stage.bottomTxt('조조는 이곳을 점령하고,', True, True, False)
    stage.delay(8)
    stage.head(0).move(396, 45)
    stage.delay(5)
    stage.bottomTxt('계속 진군하여 원담이 있는 남피를 공격했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 42)
    stage.showUnits([
        [
            113,
            54,
            70],
        [
            103,
            35,
            56,
            1],
        [
            112,
            71,
            56,
            3],
        [
            192,
            53,
            56,
            2,
            6]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.setStageName('남피  원담의 저택')
    stage.delay(3)
    stage.unit(113).setAction(3)
    stage.say('@dialogue:R_23.scene2.dialogue0001')
    stage.say('@dialogue:R_23.scene2.dialogue0002')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:R_23.scene2.dialogue0003')
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_23.scene2.dialogue0004')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(2)
    stage.say('@dialogue:R_23.scene2.dialogue0005')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:R_23.scene2.dialogue0006')
    stage.unit(113).setAction(2)
    stage.say('@dialogue:R_23.scene2.dialogue0007')
    stage.unit(112).setAction(0, 2)
    stage.say('@dialogue:R_23.scene2.dialogue0008')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:R_23.scene2.dialogue0009')
    stage.unit(112).setAction(6)
    stage.say('@dialogue:R_23.scene2.dialogue0010')
    stage.unit(113).setAction(0, 1)
    stage.say('@dialogue:R_23.scene2.dialogue0011')
    stage.unit(113).setAction(0, 0)
    stage.say('@dialogue:R_23.scene2.dialogue0012')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
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
            6,
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
            24,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('남피 교외  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_23.scene3.dialogue0001')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 13, [
        22,
        8])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 23
    stage.setBattlePos([
        [
            2,
            16],
        [
            3,
            17],
        [
            6,
            16],
        [
            3,
            13],
        [
            5,
            15],
        [
            4,
            14],
        [
            5,
            17],
        [
            2,
            14],
        [
            4,
            16],
        [
            3,
            15],
        [
            6,
            18],
        [
            1,
            15],
        [
            4,
            18],
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
        stage.say('@dialogue:R_23.scene4.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_23.scene4.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_23.scene4.dialogue0003')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_23.scene4.dialogue0004')
    if stage.unitClickTest(6):
        stage.say('@dialogue:R_23.scene4.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_23.scene4.dialogue0006')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_23.scene4.dialogue0007')
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
