# Source Generated with Decompyle++
# File: R_40.marshal (Python 3.9)


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
    stage.showHead(0, 160, 76)
    stage.showHead(35, 96, 76)
    stage.bottomTxt('양평관에서 패한 촉군은', False, False, False)
    stage.delay(5)
    stage.head(35).move(40, 130)
    stage.delay(5)
    stage.bottomTxt('검각으로 패주했다.', True, False, True)
    stage.bgSound(15)
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
            17,
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
    stage.setEventName('계속 진군한다')
    stage.setStageName('한중  조조군 본진')
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_40.scene0.dialogue0001')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_40.scene0.dialogue0002')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_40.scene0.dialogue0003')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_40.scene0.dialogue0004')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_40.scene0.dialogue0005')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_40.scene0.dialogue0006')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_40.scene0.dialogue0007')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_40.scene0.dialogue0008')
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 40
    stage.setBattlePos([
        [
            18,
            1],
        [
            17,
            1],
        [
            18,
            2],
        [
            17,
            2],
        [
            19,
            2],
        [
            16,
            2],
        [
            19,
            1],
        [
            16,
            1],
        [
            20,
            1],
        [
            15,
            1],
        [
            18,
            0],
        [
            17,
            0],
        [
            19,
            0],
        [
            16,
            0],
        [
            15,
            0]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_40.scene1.dialogue0001')
        stage.delay(3)
        stage.clsUnit()
        stage.loadBg(1, 0)
        stage.draw()
        stage.showHead(0, 130, 77)
        stage.showHead(35, 38, 128)
        stage.bottomTxt('조조군', False, False, False)
        stage.delay(5)
        stage.head(0).move(102, 128)
        stage.delay(5)
        stage.bottomTxt('군사를 이끌고 검각으로 진군했다.', True, False, True)
        stage.loadBg(2, 54)
        stage.showUnits([
            [
                35,
                53,
                37,
                2],
            [
                54,
                67,
                52,
                3],
            [
                53,
                67,
                58,
                3],
            [
                66,
                67,
                64,
                3],
            [
                34,
                42,
                52,
                1],
            [
                50,
                42,
                58,
                1],
            [
                69,
                42,
                64,
                1],
            [
                194,
                53,
                53,
                0,
                6]])
        stage.draw()
        stage.delay(3)
        stage.info('한편……', 3)
        stage.setStageName('검각  유선군 본진')
        stage.say('@dialogue:R_40.scene1.dialogue0002')
        stage.unit(194).setAction(0)
        stage.say('@dialogue:R_40.scene1.dialogue0003')
        stage.unit(194).setAction(6)
        stage.say('@dialogue:R_40.scene1.dialogue0004')
        stage.unit(194).setAction(0)
        stage.delay(3)
        stage.unit(194).move(53, 95, 2)
        stage.unit(194).hide()
        stage.say('@dialogue:R_40.scene1.dialogue0005')
        stage.unit(69).setAction(6)
        stage.say('@dialogue:R_40.scene1.dialogue0006')
        stage.unit(69).setAction(0)
        stage.say('@dialogue:R_40.scene1.dialogue0007')
        stage.unit(53).setAction(0, 0)
        stage.say('@dialogue:R_40.scene1.dialogue0008')
        stage.unit(66).setAction(0, 0)
        stage.say('@dialogue:R_40.scene1.dialogue0009')
        stage.unit(50).setAction(3)
        stage.say('@dialogue:R_40.scene1.dialogue0010')
        stage.unit(50).setAction(0)
        stage.delay(3)
        stage.unit(53).setAction(0, 3)
        stage.delay(3)
        stage.unit(66).setAction(0, 3)
        stage.say('@dialogue:R_40.scene1.dialogue0011')
        stage.showUnit(183, 53, 90)
        stage.say('@dialogue:R_40.scene1.dialogue0012')
        stage.say('@dialogue:R_40.scene1.dialogue0013')
        stage.unit(35).setAction(17)
        stage.say('@dialogue:R_40.scene1.dialogue0014')
        stage.unit(35).setAction(0)
        stage.say('@dialogue:R_40.scene1.dialogue0015')
        stage.bgSound(-1)
        stage.clsUnit()
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_40.scene1.dialogue0016')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_40.scene1.dialogue0017')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_40.scene1.dialogue0018')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_40.scene1.dialogue0019')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_40.scene1.dialogue0020')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_40.scene1.dialogue0021')
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
