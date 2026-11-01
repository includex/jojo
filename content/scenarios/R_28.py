# Source Generated with Decompyle++
# File: R_28.marshal (Python 3.9)


def scene0():
    stage.bgSound(0)
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.delay(3)
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
            7,
            44,
            52,
            1],
        [
            20,
            64,
            64,
            3],
        [
            23,
            64,
            58,
            3],
        [
            24,
            64,
            52,
            3]])
    stage.draw()
    stage.info('황개의 투항을 받아들인 지 사흘 뒤,\n마침내 약속한 날이 왔다.')
    stage.setStageName('오림  조조군 본진')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_28.scene0.dialogue0001')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_28.scene0.dialogue0002')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_28.scene0.dialogue0003')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_28.scene0.dialogue0004')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_28.scene0.dialogue0005')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_28.scene0.dialogue0006')
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        2,
        21])
    gvars[4076] = 0
    gvars[4057] = 28
    stage.setBattlePos([
        [
            16,
            21],
        [
            8,
            20],
        [
            15,
            21],
        [
            16,
            20],
        [
            25,
            22],
        [
            25,
            23],
        [
            17,
            8],
        [
            18,
            8],
        [
            7,
            10],
        [
            10,
            13],
        [
            27,
            15],
        [
            25,
            5],
        [
            9,
            19],
        [
            24,
            21],
        [
            23,
            20]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_28.scene1.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_28.scene1.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_28.scene1.dialogue0003')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_28.scene1.dialogue0004')
    if stage.unitClickTest(7):
        stage.say('@dialogue:R_28.scene1.dialogue0005')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_28.scene1.dialogue0006')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_28.scene1.dialogue0007')
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
