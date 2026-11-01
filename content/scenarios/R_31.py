# Source Generated with Decompyle++
# File: R_31.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            1,
            52,
            70],
        [
            8,
            58,
            70],
        [
            15,
            64,
            70],
        [
            13,
            70,
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
            159,
            52,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('서량의 금마초')
    stage.setStageName('허도  승상부')
    if vars[309]:
        stage.say('@dialogue:R_31.scene0.dialogue0001')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_31.scene0.dialogue0002')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(159).setAction(0)
        stage.say('@dialogue:R_31.scene0.dialogue0003')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_31.scene0.dialogue0004')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(159).setAction(0, 0)
        stage.say('@dialogue:R_31.scene0.dialogue0005')
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_31.scene0.dialogue0006')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_31.scene0.dialogue0007')
        stage.unit(23).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_31.scene0.dialogue0008')
        stage.unit(159).setAction(0, 3)
        stage.say('@dialogue:R_31.scene0.dialogue0009')
        stage.unit(159).setAction(0, 0)
        stage.say('@dialogue:R_31.scene0.dialogue0010')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_31.scene0.dialogue0011')
        stage.unit(20).setAction(0)
        stage.delay(3)
    if not vars[309]:
        stage.say('@dialogue:R_31.scene0.dialogue0012')
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_31.scene0.dialogue0013')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_31.scene0.dialogue0014')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_31.scene0.dialogue0015')
        stage.delay(10)
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_31.scene0.dialogue0016')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_31.scene0.dialogue0017')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_31.scene0.dialogue0018')
        stage.unit(23).setAction(0)
        stage.say('@dialogue:R_31.scene0.dialogue0019')
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_31.scene0.dialogue0020')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_31.scene0.dialogue0021')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_31.scene0.dialogue0022')
        stage.unit(23).setAction(0)
        stage.delay(3)
        stage.unit(159).setAction(0)
        stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(20),
            52,
            34,
            0],
        [
            stage.unit(23),
            58,
            34,
            0],
        [
            stage.unit(159),
            58,
            55,
            1]])
    stage.unitsMove([
        [
            stage.unit(20),
            80,
            34,
            1],
        [
            stage.unit(23),
            80,
            34,
            1],
        [
            stage.unit(159),
            98,
            55,
            1]])
    stage.unit(159).hide()
    stage.unit(20).hide()
    stage.unit(23).hide()
    stage.say('@dialogue:R_31.scene0.dialogue0023')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_31.scene0.dialogue0024')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_31.scene0.dialogue0025')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_31.scene0.dialogue0026')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_31.scene0.dialogue0027')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_31.scene0.dialogue0028')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_31.scene0.dialogue0029')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_31.scene0.dialogue0030')
    model.setFace(0)
    stage.say('@dialogue:R_31.scene0.dialogue0031')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_31.scene0.dialogue0032')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(21).move(95, 40, 1)
    stage.unit(21).hide()
    stage.say('@dialogue:R_31.scene0.dialogue0033')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 336, 98)
    stage.showHead(32, 274, 228)
    stage.showHead(76, 567, 217)
    stage.bottomTxt('조조는 조정에 뜻을 물었고', False, False, False)
    stage.delay(5)
    stage.showHead(77, 338, 228)
    stage.delay(5)
    stage.bottomTxt('주유를 강하 태수로 임명했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('유비와 손권의 형주 쟁탈전은 더욱 치열해졌다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('양측이 다툰 끝에', True, True, False)
    stage.delay(5)
    stage.head(77).hide()
    stage.delay(5)
    stage.bottomTxt('주유가 목숨을 잃자', True, False, False)
    stage.delay(5)
    stage.bottomTxt('유비는 형주를 완전히 장악했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            24,
            52,
            40,
            2],
        [
            21,
            58,
            40,
            2]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_31.scene1.dialogue0001')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_31.scene1.dialogue0002')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_31.scene1.dialogue0003')
    stage.effectSound(201, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('조금 뜻밖이다\n과연 예상대로다', 0)
    if sel == 1:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('@dialogue:R_31.scene1.dialogue0004')
    elif sel == 2:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('@dialogue:R_31.scene1.dialogue0005')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_31.scene1.dialogue0006')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('@dialogue:R_31.scene1.dialogue0007')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_31.scene1.dialogue0008')
    stage.showUnit(181, 90, 55, 3)
    stage.say('@dialogue:R_31.scene1.dialogue0009')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_31.scene1.dialogue0010')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_31.scene1.dialogue0011')
    stage.showUnit(138, 90, 55, 3)
    stage.say('@dialogue:R_31.scene1.dialogue0012')
    stage.unit(138).move(52, 55, 3)
    stage.delay(3)
    stage.unit(138).setAction(6)
    stage.say('@dialogue:R_31.scene1.dialogue0013')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_31.scene1.dialogue0014')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_31.scene1.dialogue0015')
    stage.unit(138).setAction(0)
    stage.delay(3)
    stage.unit(138).move(95, 55, 1)
    stage.unit(138).hide()
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_31.scene1.dialogue0016')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_31.scene1.dialogue0017')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_31.scene1.dialogue0018')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_31.scene1.dialogue0019')
    stage.delay(3)
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(12)
    stage.delay(3)
    stage.loadBg(2, 15)
    stage.showUnits([
        [
            149,
            27,
            55,
            1],
        [
            138,
            46,
            55,
            3,
            6],
        [
            192,
            46,
            43,
            2],
        [
            193,
            52,
            43,
            2]])
    stage.draw()
    stage.setStageName('허도  궁정')
    stage.delay(3)
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0001')
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_31.scene2.dialogue0002')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0003')
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_31.scene2.dialogue0004')
    stage.unit(138).setAction(0)
    stage.say('@dialogue:R_31.scene2.dialogue0005')
    stage.unit(138).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0006')
    stage.unit(138).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0007')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(138).setAction(0, 0)
    stage.say('@dialogue:R_31.scene2.dialogue0008')
    stage.unit(192).setAction(3)
    stage.say('@dialogue:R_31.scene2.dialogue0009')
    stage.unit(138).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0010')
    stage.unit(138).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_31.scene2.dialogue0011')
    stage.unit(138).setAction(3)
    stage.say('@dialogue:R_31.scene2.dialogue0012')
    stage.unit(138).setAction(0)
    stage.say('@dialogue:R_31.scene2.dialogue0013')
    stage.unit(192).setAction(0, 3)
    stage.delay(3)
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0014')
    stage.unit(138).setAction(0, 3)
    stage.delay(3)
    stage.unit(138).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0015')
    stage.unit(138).setAction(0)
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(0, 2)
    stage.unit(138).move(95, 55, 1)
    stage.unit(138).hide()
    stage.say('@dialogue:R_31.scene2.dialogue0016')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0017')
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_31.scene2.dialogue0018')
    stage.unit(193).setAction(6)
    stage.say('@dialogue:R_31.scene2.dialogue0019')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(193).move(76, 43, 1)
    stage.say('@dialogue:R_31.scene2.dialogue0020')
    stage.unit(193).move(90, 43, 1)
    stage.unit(193).hide()
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
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
            2]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_31.scene3.dialogue0001')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_31.scene3.dialogue0002')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_31.scene3.dialogue0003')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_31.scene3.dialogue0004')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_31.scene3.dialogue0005')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_31.scene3.dialogue0006')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_31.scene3.dialogue0007')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_31.scene3.dialogue0008')
    stage.effectSound(201, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('마등의 목을 벤다\n마등을 감옥에 가둔다', 0)
    if sel == 1:
        vars[311] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_31.scene3.dialogue0009')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_31.scene3.dialogue0010')
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(21).move(95, 40, 1)
        stage.unit(21).hide()
        stage.say('@dialogue:R_31.scene3.dialogue0011')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_31.scene3.dialogue0012')
    elif sel == 2:
        vars[311] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('@dialogue:R_31.scene3.dialogue0013')
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_31.scene3.dialogue0014')
        stage.delay(5)
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(21).move(76, 40, 1)
        stage.say('@dialogue:R_31.scene3.dialogue0015')
        stage.unit(21).move(95, 40, 1)
        stage.unit(21).hide()
        stage.info('하지만 정욱은 마등을 감옥에 가두지 않고\n독단으로 죽여 버렸다.\n이 사실을 안 조조는 깊이 후회하며\n예를 갖춰 마등의 시신을 서량으로 돌려보냈다.')
        stage.say('@dialogue:R_31.scene3.dialogue0016')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_31.scene3.dialogue0017')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
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
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('보름 뒤…….', 3)
    stage.setStageName('허도  승상부')
    stage.showUnit(181, 90, 55, 3)
    stage.say('@dialogue:R_31.scene4.dialogue0001')
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_31.scene4.dialogue0002')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_31.scene4.dialogue0003')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 330, 129)
    stage.bottomTxt('마등의 아들은', False, False, False)
    stage.showHead(57, 50, 0)
    stage.bottomTxt('마초는 서량군을 이끌고,', True, False, False)
    stage.delay(5)
    stage.head(57).move(176, 83)
    stage.delay(5)
    stage.bottomTxt('목표인 허도를 향해 남하했다.', True, False, False)
    stage.delay(8)
    stage.head(0).move(240, 83)
    stage.delay(5)
    stage.bottomTxt('조조는 위수로 진군하여 맞아 싸웠다.', True, True, True)
    stage.bgSound(15)
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
            12,
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
    stage.setStageName('위수@조조군 본진')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 13, [
        12,
        13,
        23,
        15], [
        2,
        10,
        11])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 31
    stage.setBattlePos([
        [
            8,
            5],
        [
            18,
            29],
        [
            18,
            30],
        [
            8,
            1],
        [
            9,
            1],
        [
            17,
            30],
        [
            8,
            6],
        [
            18,
            28],
        [
            9,
            6],
        [
            19,
            29],
        [
            7,
            6],
        [
            17,
            31],
        [
            9,
            5],
        [
            0,
            0],
        [
            0,
            0]])
    return False


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_31.scene6.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_31.scene6.dialogue0002')
    if stage.unitClickTest(12):
        stage.say('@dialogue:R_31.scene6.dialogue0003')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_31.scene6.dialogue0004')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_31.scene6.dialogue0005')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_31.scene6.dialogue0006')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_31.scene6.dialogue0007')
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
