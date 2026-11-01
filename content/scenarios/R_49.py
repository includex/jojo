# Source Generated with Decompyle++
# File: R_49.marshal (Python 3.9)


def scene0():
    '''붉은 제단'''
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 57)
    stage.showUnits([
        [
            77,
            66,
            52,
            3],
        [
            35,
            52,
            52,
            1]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('붉은 제단')
    stage.setStageName('적벽  오군 본진')
    stage.delay(3)
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_49.scene0.dialogue0001')
    stage.unit(35).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 2)
    stage.say('@dialogue:R_49.scene0.dialogue0002')
    stage.unit(77).setAction(0, 3)
    stage.say('@dialogue:R_49.scene0.dialogue0003')
    stage.unit(77).setAction(2)
    stage.say('@dialogue:R_49.scene0.dialogue0004')
    stage.unit(77).setAction(0)
    stage.say('@dialogue:R_49.scene0.dialogue0005')
    stage.unit(77).setAction(0, 2)
    stage.say('@dialogue:R_49.scene0.dialogue0006')
    stage.unit(77).setAction(11)
    stage.say('@dialogue:R_49.scene0.dialogue0007')
    stage.unit(35).move(16, 52, 3)
    stage.unit(35).hide()
    stage.delay(5)
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 3)
    stage.say('@dialogue:R_49.scene0.dialogue0008')
    stage.unit(77).setAction(0, 2)
    stage.say('@dialogue:R_49.scene0.dialogue0009')
    stage.unit(77).setAction(17)
    stage.say('@dialogue:R_49.scene0.dialogue0010')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene1():
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
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('강릉  조조군 본진')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_49.scene1.dialogue0001')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(3).setAction(0, 2)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('@dialogue:R_49.scene1.dialogue0002')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_49.scene1.dialogue0003')
    stage.showUnit(181, 54, 22, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_49.scene1.dialogue0004')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_49.scene1.dialogue0005')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_49.scene1.dialogue0006')
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_49.scene1.dialogue0007')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_49.scene1.dialogue0008')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_49.scene1.dialogue0009')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 263, 214)
    stage.bottomTxt('조조군', False, False, False)
    stage.delay(5)
    stage.head(0).move(315, 234)
    stage.delay(5)
    stage.bottomTxt('적벽으로 쳐들어갔다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 57)
    stage.showUnits([
        [
            76,
            66,
            52,
            3],
        [
            77,
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
            80,
            52,
            63],
        [
            81,
            46,
            63],
        [
            84,
            40,
            63]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setStageName('적벽  오군 본진')
    stage.delay(3)
    stage.unit(77).setAction(6)
    stage.say('@dialogue:R_49.scene2.dialogue0001')
    stage.unit(77).setAction(0)
    stage.say('@dialogue:R_49.scene2.dialogue0002')
    stage.unit(77).setAction(3)
    stage.say('@dialogue:R_49.scene2.dialogue0003')
    stage.unit(77).setAction(0)
    stage.say('@dialogue:R_49.scene2.dialogue0004')
    stage.unit(77).setAction(17)
    stage.say('@dialogue:R_49.scene2.dialogue0005')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 73)
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.showUnit(35, 95, 73, 3)
    stage.setEventName('속아 넘어간 주유')
    stage.setStageName('적벽 교외  강가')
    stage.unit(35).move(57, 73, 3)
    stage.delay(3)
    stage.unit(35).setAction(0, 2)
    stage.say('@dialogue:R_49.scene3.dialogue0001')
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_49.scene3.dialogue0002')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_49.scene3.dialogue0003')
    stage.unit(35).move(5, 73, 3)
    stage.unit(35).hide()
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene4():
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
            17,
            64,
            52,
            3],
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
            3,
            44,
            52,
            1]])
    stage.draw()
    stage.setStageName('적벽  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_49.scene4.dialogue0001')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 49
    stage.setBattlePos([
        [
            15,
            4],
        [
            16,
            4],
        [
            15,
            5],
        [
            16,
            5],
        [
            14,
            5],
        [
            17,
            5],
        [
            14,
            4],
        [
            17,
            4],
        [
            15,
            3],
        [
            16,
            3],
        [
            14,
            3],
        [
            17,
            3],
        [
            15,
            2],
        [
            16,
            2],
        [
            14,
            2]])
    return False


def scene5():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_49.scene5.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_49.scene5.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_49.scene5.dialogue0003')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_49.scene5.dialogue0004')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_49.scene5.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_49.scene5.dialogue0006')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_49.scene5.dialogue0007')
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
