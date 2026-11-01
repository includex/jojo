# Source Generated with Decompyle++
# File: R_55.marshal (Python 3.9)


def scene0():
    '''제단을 찾아라!'''
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(16)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 166, 226)
    stage.showHead(50, 13, 190)
    stage.bottomTxt('촉군은 성도에서 북상하여', False, False, False)
    stage.delay(5)
    stage.head(50).move(76, 147)
    stage.delay(5)
    stage.bottomTxt('검각을 사수했다.', True, False, False)
    stage.delay(10)
    stage.head(0).move(140, 187)
    stage.delay(5)
    stage.bottomTxt('조조군은 승기를 타고 추격하여,', True, True, False)
    stage.delay(5)
    stage.head(50).hide()
    stage.delay(5)
    stage.head(0).move(108, 167)
    stage.bottomTxt('치열한 전투 끝에 검각을 돌파했다.', True, False, True)
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
    stage.setEventName('제단을 찾아라!')
    stage.setStageName('검각  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_55.scene0.dialogue0001')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(2)
    stage.say('@dialogue:R_55.scene0.dialogue0002')
    stage.unit(1).setAction(0)
    stage.delay(5)
    stage.showUnit(19, 54, 22, 2)
    stage.say('@dialogue:R_55.scene0.dialogue0003')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_55.scene0.dialogue0004')
    stage.unit(0).setAction(0)
    stage.unit(19).move(54, 58, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0005')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_55.scene0.dialogue0006')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0007')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_55.scene0.dialogue0008')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0009')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0010')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0011')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_55.scene0.dialogue0012')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0013')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_55.scene0.dialogue0014')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_55.scene0.dialogue0015')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_55.scene0.dialogue0016')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0017')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_55.scene0.dialogue0018')
    stage.unit(17).setAction(0, 2)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0019')
    stage.say('@dialogue:R_55.scene0.dialogue0020')
    stage.unit(17).setAction(0)
    stage.unit(17).move(54, 52, 1)
    stage.unitsMove([
        [
            stage.unit(17),
            54,
            40,
            0],
        [
            stage.unit(23),
            54,
            52,
            3]])
    stage.unitsMove([
        [
            stage.unit(17),
            54,
            22,
            0],
        [
            stage.unit(23),
            54,
            34,
            0]])
    stage.unit(17).hide()
    stage.unit(23).move(54, 22, 0)
    stage.unit(23).hide()
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_55.scene0.dialogue0021')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0022')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_55.scene0.dialogue0023')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_55.scene0.dialogue0024')
    stage.unit(0).setAction(0)
    stage.unit(19).move(64, 58, 3)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [], [
        17,
        23])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 55
    stage.setBattlePos([
        [
            9,
            35],
        [
            8,
            35],
        [
            10,
            35],
        [
            7,
            35],
        [
            11,
            35],
        [
            9,
            35],
        [
            8,
            35],
        [
            10,
            35],
        [
            7,
            35],
        [
            11,
            35],
        [
            9,
            35],
        [
            8,
            35],
        [
            10,
            35],
        [
            7,
            35],
        [
            11,
            35]])
    return False


def scene1():
    if stage.battleTest():
        stage.say('@dialogue:R_55.scene1.dialogue0001')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_55.scene1.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_55.scene1.dialogue0003')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_55.scene1.dialogue0004')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_55.scene1.dialogue0005')
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
