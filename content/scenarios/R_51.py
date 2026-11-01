# Source Generated with Decompyle++
# File: R_51.marshal (Python 3.9)


def scene0():
    '주유의 패주'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 31)
    stage.showUnits([
        [
            76,
            5,
            51,
            1],
        [
            77,
            5,
            51,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('주유의 패주')
    stage.setStageName('유수구 교외  산길')
    stage.unit(76).move(15, 51, 1)
    stage.unitsMove([
        [
            stage.unit(76),
            68,
            51,
            1],
        [
            stage.unit(77),
            58,
            51,
            1]])
    stage.unit(77).setAction(0)
    stage.unit(76).setAction(0)
    stage.delay(3)
    stage.unit(76).setAction(2)
    stage.say('@dialogue:R_51.scene0.dialogue0001')
    stage.unit(76).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('@dialogue:R_51.scene0.dialogue0002')
    stage.unit(76).setAction(0, 3)
    stage.delay(3)
    stage.unit(77).setAction(17)
    stage.say('@dialogue:R_51.scene0.dialogue0003')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(76).setAction(17)
    stage.say('@dialogue:R_51.scene0.dialogue0004')
    stage.unit(76).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(6)
    stage.say('@dialogue:R_51.scene0.dialogue0005')
    stage.unit(76).setAction(3)
    stage.say('@dialogue:R_51.scene0.dialogue0006')
    stage.unit(76).setAction(0)
    stage.say('@dialogue:R_51.scene0.dialogue0007')
    stage.unit(76).setAction(0, 1)
    stage.delay(5)
    stage.unit(77).setAction(17, 1)
    stage.say('@dialogue:R_51.scene0.dialogue0008')
    stage.unit(77).setAction(0, 1)
    stage.unitsMove([
        [
            stage.unit(76),
            95,
            51,
            1],
        [
            stage.unit(77),
            85,
            51,
            1]])
    stage.unit(76).hide()
    stage.unit(77).move(95, 51, 1)
    stage.delay(3)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(0)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 406, 212)
    stage.showHead(76, 470, 212)
    stage.bottomTxt('중요 거점 유수구를 잃은 오군은', False, False, False)
    stage.delay(5)
    stage.head(76).move(513, 184)
    stage.delay(5)
    stage.bottomTxt('근거지인 건업으로 퇴각했다.', True, True, True)
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
            15,
            44,
            52,
            1]])
    stage.draw()
    stage.setStageName('유수구  조조군 본진')
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_51.scene1.dialogue0001')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_51.scene1.dialogue0002')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_51.scene1.dialogue0003')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_51.scene1.dialogue0004')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_51.scene1.dialogue0005')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_51.scene1.dialogue0006')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_51.scene1.dialogue0007')
    stage.unit(0).setAction(0)
    stage.unit(0).setAction(0, 0)
    stage.unit(1).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(23).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 51
    stage.setBattlePos([
        [
            19,
            1],
        [
            1,
            20],
        [
            38,
            19],
        [
            20,
            1],
        [
            1,
            19],
        [
            38,
            20],
        [
            18,
            1],
        [
            1,
            21],
        [
            38,
            18],
        [
            21,
            1],
        [
            1,
            18],
        [
            38,
            21],
        [
            19,
            0],
        [
            0,
            20],
        [
            39,
            19]])
    return False


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_51.scene2.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_51.scene2.dialogue0002')
    if stage.unitClickTest(3):
        stage.say('@dialogue:R_51.scene2.dialogue0003')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_51.scene2.dialogue0004')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_51.scene2.dialogue0005')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_51.scene2.dialogue0006')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_51.scene2.dialogue0007')
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
