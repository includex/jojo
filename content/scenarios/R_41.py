# Source Generated with Decompyle++
# File: R_41.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    stage.clsUnit()
    stage.bgSound(0)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 100, 120)
    stage.showHead(35, 36, 120)
    stage.bottomTxt('촉군', False, False, False)
    stage.delay(5)
    stage.head(35).move(9, 181)
    stage.delay(5)
    stage.bottomTxt('마지막 거점인 성도로 패주했다.', True, False, True)
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
            8,
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
    stage.setEventName('유비가 세운 나라')
    stage.setStageName('성도 교외  조조군 본진')
    if vars[740]:
        stage.say('@dialogue:R_41.scene0.dialogue0001')
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_41.scene0.dialogue0002')
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_41.scene0.dialogue0003')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(0, 2)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_41.scene0.dialogue0004')
        stage.unit(21).setAction(0)
        stage.say('@dialogue:R_41.scene0.dialogue0005')
        stage.unit(1).setAction(0, 2)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_41.scene0.dialogue0006')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_41.scene0.dialogue0007')
        stage.unit(8).setAction(0, 2)
        stage.delay(3)
        stage.say('@dialogue:R_41.scene0.dialogue0008')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_41.scene0.dialogue0009')
        stage.unit(0).setAction(0)
        stage.unit(1).setAction(0, 1)
        stage.unit(8).setAction(0, 1)
        stage.unit(20).setAction(0, 3)
        stage.unit(19).setAction(0, 3)
        stage.unit(21).setAction(0, 3)
    if not vars[740]:
        stage.say('@dialogue:R_41.scene0.dialogue0010')
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_41.scene0.dialogue0011')
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_41.scene0.dialogue0012')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(0, 2)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_41.scene0.dialogue0013')
        stage.unit(21).setAction(0)
        stage.say('@dialogue:R_41.scene0.dialogue0014')
        stage.unit(1).setAction(0, 2)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_41.scene0.dialogue0015')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_41.scene0.dialogue0016')
        stage.unit(8).setAction(0, 2)
        stage.say('@dialogue:R_41.scene0.dialogue0017')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_41.scene0.dialogue0018')
        stage.unit(0).setAction(0)
        stage.unit(1).setAction(0, 1)
        stage.unit(8).setAction(0, 1)
        stage.unit(20).setAction(0, 3)
        stage.unit(19).setAction(0, 3)
        stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    if vars[740]:
        gvars[4076] = 0
        gvars["campaign.current_chapter"] = 41
        stage.setJoinBattle(1, 15, [
            1,
            4,
            5,
            12,
            8])
        stage.setBattlePos([
            [
                19,
                3],
            [
                20,
                3],
            [
                18,
                3],
            [
                21,
                3],
            [
                17,
                3],
            [
                19,
                2],
            [
                20,
                2],
            [
                18,
                2],
            [
                21,
                2],
            [
                17,
                2],
            [
                19,
                1],
            [
                20,
                1],
            [
                18,
                1],
            [
                21,
                1],
            [
                17,
                1]])
    if not vars[740]:
        gvars[4076] = 0
        gvars["campaign.current_chapter"] = 41
        stage.setJoinBattle(1, 13, [
            1,
            4,
            5,
            12,
            8])
        stage.setBattlePos([
            [
                19,
                3],
            [
                20,
                3],
            [
                18,
                3],
            [
                21,
                3],
            [
                17,
                3],
            [
                19,
                2],
            [
                20,
                2],
            [
                18,
                2],
            [
                21,
                2],
            [
                17,
                2],
            [
                19,
                1],
            [
                20,
                1],
            [
                18,
                1],
            [
                21,
                1],
            [
                17,
                1]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_41.scene1.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_41.scene1.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_41.scene1.dialogue0003')
    if stage.unitClickTest(19):
        if vars[740]:
            stage.say('@dialogue:R_41.scene1.dialogue0004')
        if not vars[740]:
            stage.say('@dialogue:R_41.scene1.dialogue0005')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_41.scene1.dialogue0006')
    if stage.unitClickTest(8):
        stage.say('@dialogue:R_41.scene1.dialogue0007')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_41.scene1.dialogue0008')
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
