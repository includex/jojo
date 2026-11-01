# Source Generated with Decompyle++
# File: R_26.marshal (Python 3.9)


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
    stage.setEventName('조조의 남하')
    stage.delay(5)
    stage.showHead(1, 296, 127)
    stage.showHead(32, 232, 171)
    stage.delay(5)
    stage.bottomTxt('조조의 대군은 공명의 계책에 걸려 대패했고', False, False, False)
    stage.delay(8)
    stage.head(1).move(330, 99)
    stage.delay(5)
    stage.bottomTxt('하후돈은 패잔병을 수습하여 허도로 돌아갈 수밖에 없었다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    model.setFace(4)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            15,
            52,
            70],
        [
            2,
            58,
            70],
        [
            13,
            64,
            70],
        [
            20,
            52,
            40,
            2],
        [
            23,
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
            2],
        [
            1,
            52,
            55,
            3,
            12],
        [
            14,
            58,
            52,
            3,
            11],
        [
            11,
            58,
            58,
            3,
            11]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_26.scene0.dialogue0001')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_26.scene0.dialogue0002')
    stage.unit(0).setAction(0, 1)
    stage.effectSound(201, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('승패는 병가지상사이니 이번에는 용서하겠다.\n패장은 마땅히 죽어야 한다. 절대 용서할 수 없다.', 0)
    if sel == 1:
        vars[310] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('@dialogue:R_26.scene0.dialogue0003')
        stage.say('@dialogue:R_26.scene0.dialogue0004')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_26.scene0.dialogue0005')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_26.scene0.dialogue0006')
        stage.unit(0).setAction(0, 3)
        stage.say('@dialogue:R_26.scene0.dialogue0007')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_26.scene0.dialogue0008')
    elif sel == 2:
        vars[310] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('@dialogue:R_26.scene0.dialogue0009')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_26.scene0.dialogue0010')
        stage.unit(15).setAction(3)
        stage.say('@dialogue:R_26.scene0.dialogue0011')
        stage.unit(2).setAction(6)
        stage.say('@dialogue:R_26.scene0.dialogue0012')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_26.scene0.dialogue0013')
        stage.unit(15).setAction(0)
        stage.unit(2).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_26.scene0.dialogue0014')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_26.scene0.dialogue0015')
        stage.unit(2).setAction(6)
        stage.say('@dialogue:R_26.scene0.dialogue0016')
        stage.unit(13).setAction(6)
        stage.say('@dialogue:R_26.scene0.dialogue0017')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('@dialogue:R_26.scene0.dialogue0018')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_26.scene0.dialogue0019')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_26.scene0.dialogue0020')
    stage.unit(14).setAction(0)
    stage.unit(11).setAction(0)
    stage.unitsMove([
        [
            stage.unit(11),
            98,
            58,
            1],
        [
            stage.unit(14),
            98,
            52,
            1],
        [
            stage.unit(1),
            92,
            55,
            1]])
    stage.unit(14).hide()
    stage.unit(11).hide()
    stage.unit(1).move(98, 55, 1)
    stage.unit(1).hide()
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_26.scene0.dialogue0021')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_26.scene0.dialogue0022')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_26.scene0.dialogue0023')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_26.scene0.dialogue0024')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_26.scene0.dialogue0025')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_26.scene0.dialogue0026')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 335, 84)
    stage.delay(5)
    stage.showHead(32, 252, 155)
    stage.delay(5)
    stage.bottomTxt('신야의 유비와', False, False, False)
    stage.delay(5)
    stage.showHead(150, 316, 195)
    stage.delay(5)
    stage.bottomTxt('형주의 유표,', True, False, False)
    stage.delay(5)
    stage.showHead(76, 575, 216)
    stage.delay(5)
    stage.bottomTxt('동오의 손권은 강남에 자리 잡고 있었다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('조조가 남방 평정을 위한 첫걸음으로 택한 것은', True, True, False)
    stage.delay(5)
    stage.head(0).move(316, 84)
    stage.delay(5)
    stage.bottomTxt('신야에 있는 유비를 공격하는 것이었다.', True, False, True)
    stage.bottomTxt('조조군의 기습을 받은 유비는 신야를 버리고 달아났다.', False, False, False)
    stage.delay(5)
    stage.head(32).hide()
    stage.delay(5)
    stage.bottomTxt('조조군은 거의 공격받지 않은 채', True, True, False)
    stage.delay(5)
    stage.head(0).move(316, 115)
    stage.delay(5)
    stage.bottomTxt('남하하여 유표가 있는 양양 근교에 이르렀다.', True, False, True)
    stage.bgSound(15)
    stage.loadBg(2, 53)
    model.setFace(128)
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
    stage.setStageName('양양 근교  조조군 본진')
    stage.say('@dialogue:R_26.scene1.dialogue0001')
    stage.unit(24).setAction(0, 2)
    stage.say('@dialogue:R_26.scene1.dialogue0002')
    stage.showUnit(181, 54, 22, 2)
    stage.say('@dialogue:R_26.scene1.dialogue0003')
    stage.unit(24).setAction(0, 3)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_26.scene1.dialogue0004')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.unit(23).setAction(0, 2)
    stage.say('@dialogue:R_26.scene1.dialogue0005')
    stage.showUnit(27, 54, 22, 2)
    stage.say('@dialogue:R_26.scene1.dialogue0006')
    stage.unit(23).setAction(0, 3)
    stage.unit(27).move(54, 64, 2)
    stage.delay(3)
    stage.unit(27).setAction(11)
    stage.say('@dialogue:R_26.scene1.dialogue0007')
    stage.say('@dialogue:R_26.scene1.dialogue0008')
    if vars[310]:
        stage.unit(1).setAction(6)
        stage.say('@dialogue:R_26.scene1.dialogue0009')
        stage.unit(7).setAction(6)
        stage.say('@dialogue:R_26.scene1.dialogue0010')
        stage.unit(1).setAction(0)
        stage.unit(7).setAction(0)
    if not vars[310]:
        stage.say('@dialogue:R_26.scene1.dialogue0011')
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_26.scene1.dialogue0012')
        stage.unit(1).setAction(0)
    stage.delay(5)
    stage.showUnit(28, 55, 22, 2)
    stage.unit(28).move(55, 26, 2)
    stage.showUnit(26, 55, 22, 2)
    stage.unitsMove([
        [
            stage.unit(28),
            50,
            56,
            2],
        [
            stage.unit(26),
            58,
            56,
            2]])
    stage.unit(26).setAction(0)
    stage.unit(28).setAction(0)
    stage.say('@dialogue:R_26.scene1.dialogue0013')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_26.scene1.dialogue0014')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_26.scene1.dialogue0015')
    stage.unit(26).move(55, 56, 3)
    stage.unitsMove([
        [
            stage.unit(26),
            55,
            52,
            0],
        [
            stage.unit(28),
            55,
            56,
            2]])
    stage.unitsMove([
        [
            stage.unit(26),
            55,
            22,
            0],
        [
            stage.unit(28),
            55,
            26,
            0]])
    stage.unit(26).hide()
    stage.unit(28).move(55, 22, 0)
    stage.unit(28).hide()
    stage.say('@dialogue:R_26.scene1.dialogue0016')
    stage.unit(27).setAction(0)
    stage.delay(3)
    stage.unit(27).move(54, 22, 0)
    stage.unit(27).hide()
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_26.scene1.dialogue0017')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        1,
        7], [
        22])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 26
    stage.setBattlePos([
        [
            2,
            2],
        [
            0,
            1],
        [
            1,
            0],
        [
            2,
            3],
        [
            3,
            2],
        [
            1,
            3],
        [
            3,
            1],
        [
            1,
            1],
        [
            0,
            2],
        [
            2,
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


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_26.scene2.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_26.scene2.dialogue0002')
    if stage.unitClickTest(7):
        stage.say('@dialogue:R_26.scene2.dialogue0003')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_26.scene2.dialogue0004')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_26.scene2.dialogue0005')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_26.scene2.dialogue0006')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_26.scene2.dialogue0007')
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
