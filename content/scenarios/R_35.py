# Source Generated with Decompyle++
# File: R_35.marshal (Python 3.9)


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
            9,
            44,
            58,
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
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('같은 하늘 아래')
    stage.setStageName('유수구  조조군 본진')
    if vars[734]:
        stage.unit(1).setAction(0, 2)
        stage.say('@dialogue:R_35.scene0.dialogue0001')
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_35.scene0.dialogue0002')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_35.scene0.dialogue0003')
        stage.unit(1).setAction(2)
        stage.say('@dialogue:R_35.scene0.dialogue0004')
        stage.say('@dialogue:R_35.scene0.dialogue0005')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_35.scene0.dialogue0006')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_35.scene0.dialogue0007')
        stage.unit(1).setAction(2)
        stage.say('@dialogue:R_35.scene0.dialogue0008')
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_35.scene0.dialogue0009')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_35.scene0.dialogue0010')
        stage.showUnit(181, 54, 22, 2)
        stage.say('@dialogue:R_35.scene0.dialogue0011')
        stage.delay(3)
        stage.unit(181).setAction(11)
        stage.say('@dialogue:R_35.scene0.dialogue0012')
        stage.unit(1).setAction(0, 1)
        stage.say('@dialogue:R_35.scene0.dialogue0013')
        stage.unit(181).hide()
        stage.delay(5)
        stage.showUnit(193, 55, 22, 2)
        stage.say('@dialogue:R_35.scene0.dialogue0014')
        stage.unit(193).move(54, 64, 2)
        stage.say('@dialogue:R_35.scene0.dialogue0015')
        stage.unit(193).setAction(6)
        stage.say('@dialogue:R_35.scene0.dialogue0016')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_35.scene0.dialogue0017')
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_35.scene0.dialogue0018')
        stage.unit(193).setAction(0)
        stage.delay(3)
        stage.unit(193).move(54, 22, 0)
        stage.unit(193).hide()
        stage.effectSound(208, 255)
        stage.clsUnit()
    if not vars[734]:
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_35.scene0.dialogue0019')
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_35.scene0.dialogue0020')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_35.scene0.dialogue0021')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_35.scene0.dialogue0022')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_35.scene0.dialogue0023')
        stage.effectSound(208, 255)
        stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 426, 161)
    stage.showHead(76, 490, 200)
    stage.bottomTxt('유수구의 결전 뒤,', False, False, False)
    stage.delay(5)
    stage.bottomTxt('조조는 손권과 화목하게 지내며', True, False, False)
    stage.delay(10)
    stage.head(0).move(337, 100)
    stage.delay(8)
    stage.head(76).move(507, 218)
    stage.delay(5)
    stage.bottomTxt('양군은 각자 군사를 거두어 본국으로 돌아갔다.', True, True, True)
    stage.delay(5)
    stage.bottomTxt('그리하여…….', False, False, False)
    stage.delay(5)
    stage.bottomTxt('위의 조조와 동오의 손권,', True, False, False)
    stage.delay(5)
    stage.showHead(32, 26, 172)
    stage.delay(5)
    stage.bottomTxt('그리고 촉의 유비, 이 세 나라가', True, False, False)
    stage.delay(5)
    stage.bottomTxt('각자 다스리는 영토를 굳게 다졌고,', True, True, True)
    stage.delay(10)
    stage.bottomTxt('역사에 없던 삼국 정립의 시대가 열렸다.', False, False, False)
    stage.delay(20)
    stage.bgSound(-1)
    return False


def scene1():
    stage.loadBg(2, 28)
    stage.showUnits([
        [
            19,
            68,
            42,
            1],
        [
            181,
            52,
            79,
            1],
        [
            182,
            54,
            46,
            1]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부 앞')
    stage.showUnit(0, 68, 94)
    stage.unit(0).move(68, 65, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_35.scene1.dialogue0001')
    stage.unit(181).setAction(11)
    stage.unit(182).setAction(11)
    stage.say('@dialogue:R_35.scene1.dialogue0002')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_35.scene1.dialogue0003')
    stage.unit(0).move(68, 50, 0)
    stage.unit(0).setAction(0)
    stage.unit(181).setAction(0)
    stage.unit(182).setAction(0)
    stage.say('@dialogue:R_35.scene1.dialogue0004')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_35.scene1.dialogue0005')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_35.scene1.dialogue0006')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_35.scene1.dialogue0007')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_35.scene1.dialogue0008')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 1)
    stage.say('@dialogue:R_35.scene1.dialogue0009')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_35.scene1.dialogue0010')
    stage.delay(10)
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.sectionName(4, '천하통일')
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            23,
            48,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setEventName('한중을 지켜라')
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_35.scene2.dialogue0001')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_35.scene2.dialogue0002')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_35.scene2.dialogue0003')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_35.scene2.dialogue0004')
    stage.effectSound(205, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('조비가 적합하다\n조식이 적합하다', 0)
    if sel == 1:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.say('@dialogue:R_35.scene2.dialogue0005')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_35.scene2.dialogue0006')
        stage.unit(23).setAction(0)
        stage.say('@dialogue:R_35.scene2.dialogue0007')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_35.scene2.dialogue0008')
        vars[312] = 1
    elif sel == 2:
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.say('@dialogue:R_35.scene2.dialogue0009')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_35.scene2.dialogue0010')
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_35.scene2.dialogue0011')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_35.scene2.dialogue0012')
        vars[312] = 0
    stage.effectSound(205, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 333, 96)
    stage.bottomTxt('바로 그때…….', False, False, False)
    stage.delay(8)
    stage.showHead(32, 24, 166)
    stage.delay(5)
    stage.bottomTxt('유비는 국력을 쌓고 힘을 길렀으며,', True, True, False)
    stage.delay(5)
    stage.head(32).move(88, 90)
    stage.delay(5)
    stage.bottomTxt('마침내 한중을 공격할 군사를 일으켰다.', True, False, False)
    stage.delay(10)
    stage.showHead(12, 152, 75)
    stage.delay(5)
    stage.bottomTxt('한중을 지키던 조홍은', True, True, False)
    stage.delay(5)
    stage.bottomTxt('촉군의 기세가 대단한 것을 보고', True, False, False)
    stage.delay(5)
    stage.bottomTxt('허도의 조조에게 구원을 청했다.', True, False, True)
    stage.bgSound(-1)
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
            6,
            58,
            70],
        [
            15,
            64,
            70],
        [
            9,
            70,
            70],
        [
            13,
            76,
            70],
        [
            20,
            52,
            40,
            2],
        [
            19,
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
            23,
            76,
            40,
            2],
        [
            181,
            48,
            55,
            3,
            11]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('@dialogue:R_35.scene3.dialogue0001')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_35.scene3.dialogue0002')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_35.scene3.dialogue0003')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_35.scene3.dialogue0004')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_35.scene3.dialogue0005')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_35.scene3.dialogue0006')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_35.scene3.dialogue0007')
    stage.unit(6).setAction(0, 3)
    stage.say('@dialogue:R_35.scene3.dialogue0008')
    stage.unit(6).setAction(6)
    stage.say('@dialogue:R_35.scene3.dialogue0009')
    stage.unit(6).setAction(0)
    stage.say('@dialogue:R_35.scene3.dialogue0010')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            6,
            54,
            80],
        [
            7,
            44,
            64,
            1]])
    stage.draw()
    stage.setStageName('한중  조조군 본진')
    stage.say('@dialogue:R_35.scene4.dialogue0001')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        6,
        7,
        12], [
        0])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 35
    stage.setBattlePos([
        [
            23,
            16],
        [
            22,
            17],
        [
            6,
            4],
        [
            4,
            6],
        [
            12,
            16],
        [
            19,
            26],
        [
            21,
            16],
        [
            23,
            18],
        [
            12,
            15],
        [
            19,
            25],
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


def scene5():
    if stage.unitClickTest(7):
        stage.say('@dialogue:R_35.scene5.dialogue0001')
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_35.scene5.dialogue0002')
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
