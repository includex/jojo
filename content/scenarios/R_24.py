# Source Generated with Decompyle++
# File: R_24.marshal (Python 3.9)


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
    stage.showHead(0, 398, 36)
    stage.showHead(113, 462, 36)
    stage.bottomTxt('조조', False, False, False)
    stage.delay(5)
    stage.head(113).hide()
    stage.delay(5)
    stage.bottomTxt('원소의 장남 원담을 없애고', True, False, False)
    stage.delay(5)
    stage.head(0).move(424, 36)
    stage.delay(5)
    stage.bottomTxt('남피를 점령했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('이때,', True, True, False)
    stage.delay(5)
    stage.showHead(115, 490, 0)
    stage.delay(5)
    stage.bottomTxt('원상', True, True, False)
    stage.delay(5)
    stage.showHead(114, 554, 0)
    stage.delay(5)
    stage.bottomTxt('그리고 원소의 둘째 아들 원희', True, False, False)
    stage.delay(5)
    stage.bottomTxt('유성으로 달아나는 중이었다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 42)
    stage.showUnits([
        [
            0,
            54,
            70],
        [
            1,
            35,
            56,
            1],
        [
            15,
            35,
            50,
            1],
        [
            2,
            35,
            44,
            1],
        [
            23,
            35,
            38,
            1],
        [
            20,
            71,
            56,
            2],
        [
            22,
            71,
            50,
            2],
        [
            24,
            71,
            44,
            2],
        [
            21,
            71,
            38,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('유성으로 가는 길')
    stage.setStageName('남피  임시 승상부')
    stage.say('@dialogue:R_24.scene0.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_24.scene0.dialogue0002')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_24.scene0.dialogue0003')
    stage.unit(2).setAction(0, 2)
    stage.say('@dialogue:R_24.scene0.dialogue0004')
    stage.unit(2).setAction(0, 1)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_24.scene0.dialogue0005')
    stage.unit(22).setAction(0)
    stage.say('@dialogue:R_24.scene0.dialogue0006')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_24.scene0.dialogue0007')
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_24.scene0.dialogue0008')
    stage.unit(22).setAction(0)
    stage.say('@dialogue:R_24.scene0.dialogue0009')
    stage.delay(10)
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_24.scene0.dialogue0010')
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_24.scene0.dialogue0011')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 424, 36)
    stage.showHead(115, 490, 0)
    stage.showHead(114, 554, 0)
    stage.delay(8)
    stage.head(0).move(426, 0)
    stage.delay(5)
    stage.bottomTxt('조조군은 유성으로 진격했다.', False, False, True)
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
    stage.setStageName('유성 근교  조조군 본진')
    stage.say('@dialogue:R_24.scene1.dialogue0001')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_24.scene1.dialogue0002')
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_24.scene1.dialogue0003')
    stage.unit(22).setAction(0)
    stage.say('@dialogue:R_24.scene1.dialogue0004')
    stage.unit(22).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        22,
        2])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 24
    stage.setBattlePos([
        [
            6,
            16],
        [
            5,
            15],
        [
            7,
            17],
        [
            7,
            15],
        [
            6,
            14],
        [
            8,
            16],
        [
            5,
            17],
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
        stage.say('@dialogue:R_24.scene2.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_24.scene2.dialogue0002')
    if stage.unitClickTest(2):
        stage.say('@dialogue:R_24.scene2.dialogue0003')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_24.scene2.dialogue0004')
        stage.unit(22).setAction(0, 2)
        stage.say('@dialogue:R_24.scene2.dialogue0005')
        stage.unit(22).setAction(0, 3)
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_24.scene2.dialogue0006')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_24.scene2.dialogue0007')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_24.scene2.dialogue0008')
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
