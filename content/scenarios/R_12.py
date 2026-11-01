# Source Generated with Decompyle++
# File: R_12.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 47)
    stage.showUnits([
        [
            0,
            71,
            52,
            3],
        [
            20,
            54,
            38,
            2],
        [
            22,
            48,
            38,
            2],
        [
            24,
            42,
            38,
            2],
        [
            21,
            36,
            38,
            2],
        [
            1,
            54,
            68],
        [
            15,
            48,
            68],
        [
            6,
            42,
            68],
        [
            5,
            36,
            68]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setEventName('성동격서')
    stage.setStageName('수춘  임시 승상부')
    stage.say('@dialogue:R_12.scene0.dialogue0001')
    stage.unit(20).setAction(0, 1)
    stage.say('@dialogue:R_12.scene0.dialogue0002')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_12.scene0.dialogue0003')
    stage.unit(22).setAction(0, 1)
    stage.say('@dialogue:R_12.scene0.dialogue0004')
    stage.unit(22).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(0, 1)
    stage.say('@dialogue:R_12.scene0.dialogue0005')
    stage.unit(24).setAction(0, 2)
    stage.say('@dialogue:R_12.scene0.dialogue0006')
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_12.scene0.dialogue0007')
    stage.unit(20).setAction(0, 1)
    stage.say('@dialogue:R_12.scene0.dialogue0008')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_12.scene0.dialogue0009')
    stage.unit(0).move(71, 56, 2)
    stage.say('@dialogue:R_12.scene0.dialogue0010')
    stage.unit(0).move(71, 52, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_12.scene0.dialogue0011')
    stage.unit(20).setAction(6, 1)
    stage.say('@dialogue:R_12.scene0.dialogue0012')
    stage.unit(0).setAction(17, 3)
    stage.say('@dialogue:R_12.scene0.dialogue0013')
    stage.effectSound(201, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 388, 169)
    stage.showHead(32, 452, 169)
    stage.bottomTxt('그렇게,', False, False, False)
    stage.delay(5)
    stage.head(0).move(337, 93)
    stage.bottomTxt('조조는 허도로 개선했고,', True, False, False)
    stage.delay(5)
    stage.head(32).move(432, 110)
    stage.bottomTxt('유비는 명을 받들어 소패로 향했다.', True, False, True)
    stage.bottomTxt('그 무렵 장안에서는…….', False, True, False)
    stage.delay(5)
    stage.showHead(131, 157, 91)
    stage.showHead(129, 221, 91)
    stage.bottomTxt('세력이 날로 쇠퇴하던 이각과 곽사는', True, True, False)
    stage.delay(5)
    stage.head(131).hide()
    stage.head(129).hide()
    stage.delay(5)
    stage.bottomTxt('끝내 반란을 일으킨 병사들에게 살해당했다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene1():
    '허도  승상부'
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
            6,
            58,
            70],
        [
            5,
            64,
            70],
        [
            20,
            52,
            40,
            2],
        [
            22,
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
            2]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_12.scene1.dialogue0001')
    stage.effectSound(205, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('관직을 주어 후대한다\n충의를 모르는 자를 벤다', 0)
    if sel == 1:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.say('@dialogue:R_12.scene1.dialogue0002')
    elif sel == 2:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.say('@dialogue:R_12.scene1.dialogue0003')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_12.scene1.dialogue0004')
    stage.showUnit(181, 95, 55, 3)
    stage.say('@dialogue:R_12.scene1.dialogue0005')
    stage.unit(20).setAction(0)
    stage.unit(181).move(48, 55, 3)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_12.scene1.dialogue0006')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_12.scene1.dialogue0007')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_12.scene1.dialogue0008')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6, 2)
    stage.say('@dialogue:R_12.scene1.dialogue0009')
    stage.unit(21).setAction(0, 2)
    stage.say('@dialogue:R_12.scene1.dialogue0010')
    stage.say('@dialogue:R_12.scene1.dialogue0011')
    stage.unit(24).setAction(6, 2)
    stage.say('@dialogue:R_12.scene1.dialogue0012')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_12.scene1.dialogue0013')
    stage.unit(0).setAction(17, 1)
    stage.say('@dialogue:R_12.scene1.dialogue0014')
    stage.effectSound(205, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 346, 83)
    stage.bottomTxt('완성의 장수를 만나', False, False, False)
    stage.delay(5)
    stage.showHead(143, 248, 144)
    stage.delay(5)
    stage.bottomTxt('움직일 기회를 엿보기 시작했고,', True, False, False)
    stage.delay(7)
    stage.head(0).move(312, 104)
    stage.delay(7)
    stage.bottomTxt('조조군은 다시 완성을 공격했다.', True, True, True)
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
            21,
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
    stage.setStageName('완성 교외  조조군 본진')
    stage.say('@dialogue:R_12.scene2.dialogue0001')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_12.scene2.dialogue0002')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_12.scene2.dialogue0003')
    stage.unit(1).setAction(3, 1)
    stage.say('@dialogue:R_12.scene2.dialogue0004')
    stage.unit(1).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_12.scene2.dialogue0005')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_12.scene2.dialogue0006')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_12.scene2.dialogue0007')
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_12.scene2.dialogue0008')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_12.scene2.dialogue0009')
    stage.unit(0).setAction(17, 0)
    stage.say('@dialogue:R_12.scene2.dialogue0010')
    stage.unit(0).setAction(0, 0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 8)
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 12
    stage.setBattlePos([
        [
            11,
            9],
        [
            11,
            10],
        [
            10,
            8],
        [
            10,
            11],
        [
            10,
            9],
        [
            10,
            10],
        [
            11,
            8],
        [
            11,
            11],
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


def scene3():
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_12.scene3.dialogue0001')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_12.scene3.dialogue0002')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_12.scene3.dialogue0003')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_12.scene3.dialogue0004')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_12.scene3.dialogue0005')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_12.scene3.dialogue0006')
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_12.scene3.dialogue0007')
        stage.say('@dialogue:R_12.scene3.dialogue0008')
        stage.bgSound(-1)
        stage.clsUnit()
        return True

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
