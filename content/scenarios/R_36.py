# Source Generated with Decompyle++
# File: R_36.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
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
            12,
            44,
            58,
            1],
        [
            20,
            64,
            64,
            3]])
    if vars[835]:
        stage.showUnit(15, 44, 52, 1)
    else:
        stage.showUnit(6, 44, 52, 1)
    stage.showUnits([
        [
            23,
            64,
            52,
            3],
        [
            19,
            64,
            58,
            3]])
    stage.draw()
    if vars[735] and vars[835]:
        stage.bgSound(13)
        stage.delay(3)
        stage.setStageName('한중  조조군 본진')
        stage.setEventName('하후연의 유품')
        stage.delay(3)
        model.setFace(1)
        stage.say('@dialogue:R_36.scene0.dialogue0001')
        stage.unit(12).setAction(0, 2)
        stage.delay(3)
        stage.unit(12).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0002')
        stage.unit(12).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_36.scene0.dialogue0003')
        stage.unit(1).setAction(4)
        stage.say('@dialogue:R_36.scene0.dialogue0004')
        stage.unit(0).setAction(0, 0)
        model.setFace(0)
        stage.say('@dialogue:R_36.scene0.dialogue0005')
        stage.unit(12).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0006')
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_36.scene0.dialogue0007')
        stage.unit(12).setAction(0)
        stage.delay(3)
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0008')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_36.scene0.dialogue0009')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_36.scene0.dialogue0010')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_36.scene0.dialogue0011')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.showUnit(181, 55, 22, 2)
        stage.say('@dialogue:R_36.scene0.dialogue0012')
        stage.unit(181).setAction(11)
        stage.say('@dialogue:R_36.scene0.dialogue0013')
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0014')
        stage.unit(20).setAction(0)
        stage.delay(5)
        stage.say('@dialogue:R_36.scene0.dialogue0015')
        stage.say('@dialogue:R_36.scene0.dialogue0016')
        stage.getItem(124, 0)
        stage.unit(181).setAction(0)
        stage.unit(181).hide()
        model.unitJoin(6, 255, 0)
        stage.bgSound(15)
        stage.delay(3)
    if vars[835] and not vars[735]:
        stage.bgSound(13)
        stage.delay(3)
        stage.setStageName('한중  조조군 본진')
        stage.setEventName('하후연의 유품')
        model.setFace(1)
        stage.say('@dialogue:R_36.scene0.dialogue0017')
        stage.unit(12).setAction(0, 2)
        stage.delay(3)
        stage.unit(12).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0018')
        stage.unit(12).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_36.scene0.dialogue0019')
        stage.unit(1).setAction(4)
        stage.say('@dialogue:R_36.scene0.dialogue0020')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_36.scene0.dialogue0021')
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0022')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        model.setFace(0)
        stage.say('@dialogue:R_36.scene0.dialogue0023')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_36.scene0.dialogue0024')
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0025')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_36.scene0.dialogue0026')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0027')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_36.scene0.dialogue0028')
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_36.scene0.dialogue0029')
        stage.unit(12).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0030')
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_36.scene0.dialogue0031')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_36.scene0.dialogue0032')
        stage.unit(12).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.delay(5)
        stage.say('@dialogue:R_36.scene0.dialogue0033')
        stage.delay(5)
        stage.showUnit(181, 54, 22, 2)
        stage.say('@dialogue:R_36.scene0.dialogue0034')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_36.scene0.dialogue0035')
        stage.unit(181).setAction(11)
        stage.say('@dialogue:R_36.scene0.dialogue0036')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0037')
        stage.unit(20).setAction(0)
        stage.delay(5)
        stage.say('@dialogue:R_36.scene0.dialogue0038')
        stage.say('@dialogue:R_36.scene0.dialogue0039')
        stage.getItem(124, 0)
        stage.unit(181).setAction(0)
        stage.unit(181).hide()
        model.unitJoin(6, 255, 0)
        if vars[1001]:
            stage.bgSound(16)
            stage.delay(3)
            stage.clsUnit()
            stage.loadBg(1, 0)
            stage.draw()
            stage.showHead(0, 157, 86)
            stage.showHead(32, 93, 86)
            stage.bottomTxt('조조는 한중을 포기하고,', False, False, False)
            stage.delay(5)
            stage.head(0).move(332, 101)
            stage.delay(5)
            stage.bottomTxt('군을 물려 허도로 돌아갔다.', True, False, True)
            stage.bgSound(-1)
            stage.jumpScene(75)
    if vars[735] and not vars[835]:
        stage.bgSound(15)
        stage.delay(3)
        stage.setStageName('한중  조조군 본진')
        stage.setEventName('한중 탈환')
        stage.say('@dialogue:R_36.scene0.dialogue0040')
        stage.unit(6).setAction(0, 2)
        stage.delay(3)
        stage.unit(6).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0041')
        stage.unit(6).setAction(0)
        stage.say('@dialogue:R_36.scene0.dialogue0042')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_36.scene0.dialogue0043')
        stage.unit(0).setAction(0)
        stage.unit(6).setAction(0, 1)
    if not vars[735] and not vars[835]:
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.setStageName('한중  조조군 본진')
        stage.setEventName('헛된 행군')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_36.scene0.dialogue0044')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.delay(3)
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0045')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_36.scene0.dialogue0046')
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_36.scene0.dialogue0047')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_36.scene0.dialogue0048')
        if vars[1001]:
            stage.effectSound(203, 255)
            stage.bgSound(16)
            stage.delay(3)
            stage.clsUnit()
            stage.loadBg(1, 0)
            stage.draw()
            stage.showHead(0, 157, 86)
            stage.showHead(32, 93, 86)
            stage.bottomTxt('조조는 한중을 포기하고,', False, False, False)
            stage.delay(5)
            stage.head(0).move(332, 101)
            stage.delay(5)
            stage.bottomTxt('군을 물려 허도로 돌아갔다.', True, False, True)
            stage.bgSound(-1)
            stage.jumpScene(75)
    stage.unit(0).setAction(0, 0)
    stage.unit(1).setAction(0, 1)
    stage.unit(12).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        15])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 36
    stage.setBattlePos([
        [
            26,
            2],
        [
            25,
            2],
        [
            26,
            3],
        [
            23,
            7],
        [
            22,
            6],
        [
            23,
            6],
        [
            24,
            7],
        [
            22,
            5],
        [
            22,
            3],
        [
            21,
            2],
        [
            22,
            2],
        [
            23,
            3],
        [
            21,
            1],
        [
            27,
            3],
        [
            25,
            1]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_36.scene1.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_36.scene1.dialogue0002')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_36.scene1.dialogue0003')
    if stage.unitClickTest(6):
        stage.say('@dialogue:R_36.scene1.dialogue0004')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_36.scene1.dialogue0005')
    if stage.unitClickTest(1):
        if vars[835]:
            stage.say('@dialogue:R_36.scene1.dialogue0006')
        if not vars[835]:
            stage.say('@dialogue:R_36.scene1.dialogue0007')
    if stage.unitClickTest(12):
        if vars[835]:
            stage.say('@dialogue:R_36.scene1.dialogue0008')
        if not vars[835]:
            stage.say('@dialogue:R_36.scene1.dialogue0009')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_36.scene1.dialogue0010')
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
