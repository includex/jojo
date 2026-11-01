# Source Generated with Decompyle++
# File: R_45.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(0)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 406, 212)
    stage.showHead(76, 470, 212)
    stage.bottomTxt('험한 요충지 유수구를 빼앗긴 오군은', False, False, False)
    stage.delay(8)
    stage.head(76).move(513, 184)
    stage.delay(5)
    stage.bottomTxt('도읍 건업으로 퇴각했다.', True, False, False)
    stage.delay(10)
    stage.head(0).move(449, 184)
    stage.delay(10)
    stage.bottomTxt('오랫동안 이어진 전란은', True, True, False)
    stage.delay(10)
    stage.bottomTxt('곧 끝나겠군.', True, False, True)
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
            8,
            44,
            58,
            1],
        [
            15,
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
    stage.setEventName('통일 전야')
    stage.setStageName('유수구  조조군 본진')
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_45.scene0.dialogue0001')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_45.scene0.dialogue0002')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_45.scene0.dialogue0003')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_45.scene0.dialogue0004')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(8).setAction(0, 2)
    stage.say('@dialogue:R_45.scene0.dialogue0005')
    stage.say('@dialogue:R_45.scene0.dialogue0006')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_45.scene0.dialogue0007')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_45.scene0.dialogue0008')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_45.scene0.dialogue0009')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_45.scene0.dialogue0010')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(1).setAction(0, 1)
    stage.unit(8).setAction(0, 1)
    stage.unit(19).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 45
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


def scene1():
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_45.scene1.dialogue0001')
    if stage.unitClickTest(8):
        stage.say('@dialogue:R_45.scene1.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_45.scene1.dialogue0003')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_45.scene1.dialogue0004')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_45.scene1.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_45.scene1.dialogue0006')
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_45.scene1.dialogue0007')
        stage.bgSound(-1)
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
