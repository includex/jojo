# Source Generated with Decompyle++
# File: R_42.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 72, 131)
    stage.showHead(35, 8, 176)
    stage.bottomTxt('조조', False, False, False)
    stage.delay(5)
    stage.head(35).hide()
    stage.delay(5)
    stage.bottomTxt('촉군을 물리쳤고,', True, False, False)
    stage.delay(8)
    stage.head(0).move(29, 173)
    stage.delay(5)
    stage.bottomTxt('마침내 성도를 점령했다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('유비가 세운 촉은 이로써 멸망했다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 67)
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setEventName('촉의 멸망')
    stage.setStageName('성도 교외  산길')
    stage.showUnit(35, 49, 95)
    stage.unit(35).move(49, 85, 0)
    stage.showUnits([
        [
            69,
            45,
            95],
        [
            50,
            53,
            95]])
    stage.unitsMove([
        [
            stage.unit(35),
            49,
            45,
            0],
        [
            stage.unit(69),
            45,
            55,
            0],
        [
            stage.unit(50),
            53,
            55,
            0]])
    stage.delay(3)
    stage.unit(35).setAction(0, 2)
    stage.say('@dialogue:R_42.scene0.dialogue0001')
    stage.unit(69).setAction(6)
    stage.say('@dialogue:R_42.scene0.dialogue0002')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unit(50).setAction(6)
    stage.say('@dialogue:R_42.scene0.dialogue0003')
    stage.unit(50).setAction(0)
    stage.say('@dialogue:R_42.scene0.dialogue0004')
    stage.unit(69).setAction(6)
    stage.say('@dialogue:R_42.scene0.dialogue0005')
    stage.unit(69).setAction(0)
    stage.say('@dialogue:R_42.scene0.dialogue0006')
    stage.unit(69).setAction(0, 2)
    stage.delay(3)
    stage.unit(69).setAction(2)
    stage.say('@dialogue:R_42.scene0.dialogue0007')
    stage.unit(50).setAction(0, 2)
    stage.say('@dialogue:R_42.scene0.dialogue0008')
    stage.unit(35).setAction(2, 2)
    stage.say('@dialogue:R_42.scene0.dialogue0009')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unit(69).setAction(0, 0)
    stage.unit(50).setAction(0, 0)
    stage.delay(5)
    stage.unit(35).setAction(0, 2)
    stage.say('@dialogue:R_42.scene0.dialogue0010')
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_42.scene0.dialogue0011')
    stage.unit(69).setAction(6)
    stage.say('@dialogue:R_42.scene0.dialogue0012')
    stage.unit(35).setAction(0, 2)
    stage.say('@dialogue:R_42.scene0.dialogue0013')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(35),
            49,
            5,
            0],
        [
            stage.unit(50),
            53,
            5,
            0],
        [
            stage.unit(69),
            45,
            5,
            0]])
    stage.unit(35).hide()
    stage.unit(50).hide()
    stage.unit(69).hide()
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            0,
            32,
            54,
            1],
        [
            1,
            50,
            70],
        [
            8,
            56,
            70],
        [
            4,
            62,
            70],
        [
            5,
            68,
            70],
        [
            12,
            74,
            70],
        [
            20,
            50,
            40,
            2],
        [
            19,
            56,
            40,
            2],
        [
            24,
            62,
            40,
            2],
        [
            21,
            68,
            40,
            2],
        [
            23,
            74,
            40,
            2]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.info('조조는 촉을 다스리는 데 힘쓰는 한편\n도주한 공명을 찾고 있었다.')
    stage.setStageName('성도  임시 승상부')
    stage.say('@dialogue:R_42.scene1.dialogue0001')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_42.scene1.dialogue0002')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_42.scene1.dialogue0003')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_42.scene1.dialogue0004')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_42.scene1.dialogue0005')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_42.scene1.dialogue0006')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_42.scene1.dialogue0007')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_42.scene1.dialogue0008')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_42.scene1.dialogue0009')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_42.scene1.dialogue0010')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_42.scene1.dialogue0011')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_42.scene1.dialogue0012')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_42.scene1.dialogue0013')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_42.scene1.dialogue0014')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_42.scene1.dialogue0015')
    stage.showUnit(181, 90, 54, 3)
    stage.say('@dialogue:R_42.scene1.dialogue0016')
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_42.scene1.dialogue0017')
    stage.unit(0).setAction(3)
    model.setFace(0)
    stage.say('@dialogue:R_42.scene1.dialogue0018')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_42.scene1.dialogue0019')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_42.scene1.dialogue0020')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_42.scene1.dialogue0021')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_42.scene1.dialogue0022')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_42.scene1.dialogue0023')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 25, 175)
    stage.bottomTxt('성도가 함락된 뒤,', False, False, False)
    stage.delay(8)
    stage.showHead(35, 161, 217)
    stage.delay(5)
    stage.bottomTxt('제갈량을 비롯한 촉의 옛 신하들은 백제성으로 달아났다.', True, True, False)
    stage.delay(10)
    stage.head(0).move(97, 217)
    stage.delay(5)
    stage.bottomTxt('이에 조조군은 추격을 시작했다.', True, True, True)
    stage.delay(3)
    stage.bgSound(15)
    stage.loadBg(2, 53)
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
            2,
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
            23,
            64,
            52,
            3]])
    stage.draw()
    stage.setStageName('백제성 밖  조조군 본진')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        19])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 42
    stage.setBattlePos([
        [
            13,
            20],
        [
            14,
            20],
        [
            12,
            20],
        [
            15,
            20],
        [
            13,
            21],
        [
            14,
            21],
        [
            12,
            21],
        [
            15,
            21],
        [
            13,
            22],
        [
            14,
            22],
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


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_42.scene3.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_42.scene3.dialogue0002')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_42.scene3.dialogue0003')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_42.scene3.dialogue0004')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_42.scene3.dialogue0005')
    if stage.unitClickTest(2):
        stage.say('@dialogue:R_42.scene3.dialogue0006')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_42.scene3.dialogue0007')
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
