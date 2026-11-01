# Source Generated with Decompyle++
# File: R_52.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(13)
    stage.delay(3)
    stage.loadBg(2, 16)
    stage.showUnit(77, 52, 82)
    stage.draw()
    stage.setEventName('네 개의 제단')
    stage.setStageName('건업  의사청')
    stage.showUnit(0, 52, 5, 2)
    stage.unit(0).move(52, 18, 2)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_52.scene0.dialogue0001')
    stage.unit(0).move(52, 60, 2)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_52.scene0.dialogue0002')
    stage.unit(77).setAction(0, 1)
    stage.say('@dialogue:R_52.scene0.dialogue0003')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_52.scene0.dialogue0004')
    stage.unit(0).setAction(0)
    stage.delay(10)
    stage.unit(77).setAction(0, 0)
    stage.say('@dialogue:R_52.scene0.dialogue0005')
    model.setFace(1)
    stage.say('@dialogue:R_52.scene0.dialogue0006')
    stage.unit(77).setAction(0, 2)
    stage.say('@dialogue:R_52.scene0.dialogue0007')
    stage.say('@dialogue:R_52.scene0.dialogue0008')
    stage.unit(77).setAction(0, 0)
    stage.say('@dialogue:R_52.scene0.dialogue0009')
    model.setFace(0)
    stage.unit(77).setAction(9)
    stage.say('@dialogue:R_52.scene0.dialogue0010')
    stage.say('@dialogue:R_52.scene0.dialogue0011')
    stage.say('@dialogue:R_52.scene0.dialogue0012')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(17)
    stage.say('@dialogue:R_52.scene0.dialogue0013')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_52.scene0.dialogue0014')
    stage.unit(77).setAction(0)
    stage.delay(5)
    stage.effectSound(38, 1)
    stage.unit(77).setAction(10)
    stage.say('@dialogue:R_52.scene0.dialogue0015')
    stage.delay(10)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_52.scene0.dialogue0016')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.sectionName(4, '마왕 부활')
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
            17,
            44,
            52,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('촉으로 향하라!')
    stage.setStageName('건업  조조군 본진')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_52.scene1.dialogue0001')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_52.scene1.dialogue0002')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_52.scene1.dialogue0003')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_52.scene1.dialogue0004')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_52.scene1.dialogue0005')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_52.scene1.dialogue0006')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_52.scene1.dialogue0007')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_52.scene1.dialogue0008')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_52.scene1.dialogue0009')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_52.scene1.dialogue0010')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_52.scene1.dialogue0011')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_52.scene1.dialogue0012')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_52.scene1.dialogue0013')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_52.scene1.dialogue0014')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_52.scene1.dialogue0015')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_52.scene1.dialogue0016')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_52.scene1.dialogue0017')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_52.scene1.dialogue0018')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_52.scene1.dialogue0019')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 471, 198)
    stage.showHead(76, 535, 198)
    stage.bottomTxt('조조는 건업을 공격하여,', False, False, False)
    stage.delay(5)
    stage.head(76).hide()
    stage.delay(5)
    stage.bottomTxt('마침내 동오를 멸망시켰다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('주유는 죽기 전에', True, True, False)
    stage.delay(5)
    stage.bottomTxt('네 제단의 수수께끼.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('조조는 마왕의 부활을 막고자,', True, True, False)
    stage.delay(5)
    stage.head(0).move(240, 220)
    stage.delay(5)
    stage.bottomTxt('군사를 이끌고 촉으로 향했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 46)
    stage.showUnit(35, 53, 71)
    stage.draw()
    stage.effectSound(204, 0)
    stage.delay(3)
    stage.setStageName('백제성  공명의 저택')
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.delay(3)
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_52.scene2.dialogue0001')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_52.scene2.dialogue0002')
    stage.unit(35).setAction(8)
    stage.say('@dialogue:R_52.scene2.dialogue0003')
    stage.unit(35).setAction(12)
    stage.say('@dialogue:R_52.scene2.dialogue0004')
    stage.showUnit(69, 53, 5, 2)
    stage.say('@dialogue:R_52.scene2.dialogue0005')
    stage.unit(35).setAction(8)
    stage.say('@dialogue:R_52.scene2.dialogue0006')
    stage.unit(35).setAction(12)
    stage.say('@dialogue:R_52.scene2.dialogue0007')
    stage.unit(35).setAction(8)
    stage.say('@dialogue:R_52.scene2.dialogue0008')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_52.scene2.dialogue0009')
    stage.unit(35).setAction(0, 0)
    stage.say('@dialogue:R_52.scene2.dialogue0010')
    stage.unit(69).move(53, 55, 2)
    stage.delay(3)
    stage.unit(69).setAction(6)
    stage.say('@dialogue:R_52.scene2.dialogue0011')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_52.scene2.dialogue0012')
    stage.unit(69).setAction(6)
    stage.say('@dialogue:R_52.scene2.dialogue0013')
    stage.unit(35).setAction(0, 0)
    stage.say('@dialogue:R_52.scene2.dialogue0014')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_52.scene2.dialogue0015')
    stage.unit(35).setAction(0, 0)
    stage.say('@dialogue:R_52.scene2.dialogue0016')
    stage.unit(69).setAction(6)
    stage.say('@dialogue:R_52.scene2.dialogue0017')
    stage.unit(35).setAction(0, 1)
    stage.delay(3)
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_52.scene2.dialogue0018')
    stage.effectSound(204, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(15)
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
            23,
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
            17,
            44,
            52,
            1]])
    stage.draw()
    stage.setStageName('촉·오 국경  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_52.scene3.dialogue0001')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_52.scene3.dialogue0002')
    stage.unit(1).setAction(0)
    stage.delay(5)
    stage.showUnit(181, 54, 22, 2)
    stage.say('@dialogue:R_52.scene3.dialogue0003')
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_52.scene3.dialogue0004')
    stage.unit(1).setAction(0, 1)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_52.scene3.dialogue0005')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_52.scene3.dialogue0006')
    stage.unit(3).setAction(0, 2)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_52.scene3.dialogue0007')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_52.scene3.dialogue0008')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_52.scene3.dialogue0009')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(0, 1)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_52.scene3.dialogue0010')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_52.scene3.dialogue0011')
    stage.unit(0).setAction(0)
    stage.unit(181).hide()
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 12)
    gvars[4076] = 0
    gvars[4057] = 52
    stage.setBattlePos([
        [
            4,
            20],
        [
            5,
            20],
        [
            3,
            20],
        [
            6,
            20],
        [
            4,
            21],
        [
            5,
            21],
        [
            3,
            21],
        [
            6,
            21],
        [
            4,
            22],
        [
            5,
            22],
        [
            3,
            22],
        [
            6,
            22],
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
        stage.say('@dialogue:R_52.scene4.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_52.scene4.dialogue0002')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_52.scene4.dialogue0003')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_52.scene4.dialogue0004')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_52.scene4.dialogue0005')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_52.scene4.dialogue0006')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_52.scene4.dialogue0007')
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
