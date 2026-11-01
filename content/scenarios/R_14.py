# Source Generated with Decompyle++
# File: R_14.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 31)
    stage.showUnits([
        [
            119,
            5,
            54,
            1],
        [
            126,
            5,
            46,
            1],
        [
            2,
            5,
            50,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('여포와 초선')
    stage.setStageName('서주 교외  산길')
    stage.unit(2).move(15, 50, 1)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(2),
            70,
            50,
            1],
        [
            stage.unit(119),
            60,
            54,
            1],
        [
            stage.unit(126),
            60,
            46,
            1]])
    stage.unit(126).setAction(0)
    stage.unit(119).setAction(0)
    stage.unit(2).setAction(0)
    stage.say('@dialogue:R_14.scene0.dialogue0001')
    stage.unit(126).setAction(0, 2)
    stage.say('@dialogue:R_14.scene0.dialogue0002')
    stage.unit(2).setAction(0, 2)
    stage.delay(5)
    stage.say('@dialogue:R_14.scene0.dialogue0003')
    stage.unit(2).setAction(0, 1)
    stage.say('@dialogue:R_14.scene0.dialogue0004')
    stage.unitsMove([
        [
            stage.unit(2),
            95,
            50,
            1],
        [
            stage.unit(119),
            85,
            54,
            1],
        [
            stage.unit(126),
            85,
            46,
            1]])
    stage.unit(2).hide()
    stage.unitsMove([
        [
            stage.unit(119),
            95,
            54,
            1],
        [
            stage.unit(126),
            95,
            46,
            1]])
    stage.unit(119).hide()
    stage.unit(126).hide()
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    '하비  초선의 거처'
    stage.bgSound(17)
    stage.delay(3)
    stage.loadBg(2, 2)
    stage.showUnit(17, 53, 53, 1)
    stage.draw()
    stage.setStageName('하비  초선의 거처')
    stage.showUnit(119, 53, 27, 2)
    stage.say('@dialogue:R_14.scene1.dialogue0001')
    stage.unit(17).setAction(0, 0)
    stage.unit(119).move(53, 45, 2)
    stage.say('@dialogue:R_14.scene1.dialogue0002')
    stage.unit(17).move(53, 57, 2)
    stage.say('@dialogue:R_14.scene1.dialogue0003')
    if vars[302]:
        stage.unit(119).move(53, 47, 2)
        stage.say('@dialogue:R_14.scene1.dialogue0004')
        stage.unit(17).setAction(0, 0)
        stage.say('@dialogue:R_14.scene1.dialogue0005')
        stage.unit(119).setAction(3, 2)
        stage.say('@dialogue:R_14.scene1.dialogue0006')
        stage.unit(119).setAction(0, 2)
        stage.say('@dialogue:R_14.scene1.dialogue0007')
        stage.unit(119).setAction(0, 0)
        stage.say('@dialogue:R_14.scene1.dialogue0008')
        stage.delay(10)
        stage.unit(17).setAction(0, 1)
        stage.say('@dialogue:R_14.scene1.dialogue0009')
        stage.unit(119).setAction(0, 2)
        stage.say('@dialogue:R_14.scene1.dialogue0010')
        stage.unit(17).setAction(0, 0)
        stage.say('@dialogue:R_14.scene1.dialogue0011')
    if not vars[302]:
        stage.delay(10)
        stage.unit(119).move(53, 47, 2)
        stage.say('@dialogue:R_14.scene1.dialogue0012')
        stage.unit(119).setAction(3, 2)
        stage.say('@dialogue:R_14.scene1.dialogue0013')
        stage.unit(119).setAction(0, 2)
        stage.delay(10)
        stage.unit(17).setAction(0, 0)
        stage.say('@dialogue:R_14.scene1.dialogue0014')
        stage.delay(3)
    stage.unit(17).setAction(0, 2)
    stage.say('@dialogue:R_14.scene1.dialogue0015')
    stage.unit(17).setAction(0, 0)
    stage.delay(3)
    stage.unit(17).move(53, 48, 0)
    stage.say('@dialogue:R_14.scene1.dialogue0016')
    stage.unit(17).setAction(6, 0)
    stage.say('@dialogue:R_14.scene1.dialogue0017')
    stage.unit(17).setAction(0, 0)
    stage.delay(5)
    stage.unit(119).move(53, 42, 0)
    stage.delay(3)
    stage.unit(119).setAction(0, 2)
    stage.say('@dialogue:R_14.scene1.dialogue0018')
    stage.unit(119).move(53, 27, 0)
    stage.unit(119).hide()
    stage.delay(3)
    stage.unit(17).setAction(0, 1)
    stage.say('@dialogue:R_14.scene1.dialogue0019')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 414, 73)
    stage.showHead(119, 478, 113)
    stage.showHead(32, 414, 153)
    stage.bottomTxt('여포는 서주에서 조조·유비 연합군에게 패했고', False, False, False)
    stage.delay(5)
    stage.head(119).move(546, 153)
    stage.delay(5)
    stage.bottomTxt('이후 하비로 패주하여 성을 굳게 지켰다.', True, False, False)
    stage.delay(8)
    stage.head(32).move(482, 193)
    stage.head(0).move(482, 113)
    stage.delay(5)
    stage.bottomTxt('조조는 여포에게 쉴 틈을 주지 않고 공격을 이어 갔다.', True, True, True)
    stage.bgSound(15)
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
            21,
            60,
            42,
            3],
        [
            32,
            49,
            52,
            2]])
    stage.draw()
    stage.setStageName('하비 교외  조조군 본진')
    stage.say('@dialogue:R_14.scene2.dialogue0001')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_14.scene2.dialogue0002')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_14.scene2.dialogue0003')
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_14.scene2.dialogue0004')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(32).move(49, 16, 0)
    stage.unit(32).hide()
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_14.scene2.dialogue0005')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 14
    stage.setBattlePos([
        [
            13,
            1],
        [
            26,
            15],
        [
            1,
            18],
        [
            14,
            1],
        [
            26,
            16],
        [
            1,
            19],
        [
            12,
            2],
        [
            26,
            14],
        [
            1,
            17],
        [
            15,
            2],
        [
            26,
            17],
        [
            1,
            20],
        [
            13,
            2],
        [
            27,
            15],
        [
            0,
            18]])
    return False


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_14.scene3.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_14.scene3.dialogue0002')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_14.scene3.dialogue0003')
    if stage.unitClickTest(6):
        stage.say('@dialogue:R_14.scene3.dialogue0004')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_14.scene3.dialogue0005')
    if stage.unitClickTest(1):
        if vars[302]:
            stage.say('@dialogue:R_14.scene3.dialogue0006')
        if not vars[302]:
            stage.say('@dialogue:R_14.scene3.dialogue0007')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_14.scene3.dialogue0008')
    if stage.unitClickTest(32):
        stage.say('@dialogue:R_14.scene3.dialogue0009')
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
