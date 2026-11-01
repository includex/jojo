# Source Generated with Decompyle++
# File: R_13.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(16)
    stage.setMenuVisible(False)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(143, 251, 146)
    stage.showHead(0, 315, 146)
    stage.bottomTxt('조조는 장수 토벌에 다시 실패하여,', False, False, False)
    stage.delay(5)
    stage.head(0).move(342, 111)
    stage.delay(5)
    stage.bottomTxt('허도로 돌아가 서둘러 병사를 훈련할 수밖에 없었다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('이때,', True, True, False)
    stage.delay(5)
    stage.showHead(101, 390, 22)
    stage.delay(5)
    stage.bottomTxt('하북의 원소는', True, False, False)
    stage.delay(5)
    stage.showHead(161, 471, 1)
    stage.delay(5)
    stage.bottomTxt('북평의 공손찬을 공격하기로 하고,', True, False, False)
    stage.delay(8)
    stage.bottomTxt('이에 사신을 보내 조조에게 지원을 청했다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1,
            5],
        [
            20,
            52,
            40,
            2],
        [
            22,
            58,
            40,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('여포를 쓰러뜨려라!')
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_13.scene0.dialogue0001')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_13.scene0.dialogue0002')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_13.scene0.dialogue0003')
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('참고 받아들인다\n요구를 거절한다', -1)
    if sel == 1:
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_13.scene0.dialogue0004')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_13.scene0.dialogue0005')
        stage.unit(0).setAction(17, 2)
        stage.say('@dialogue:R_13.scene0.dialogue0006')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_13.scene0.dialogue0007')
        stage.unit(20).setAction(6, 2)
        stage.say('@dialogue:R_13.scene0.dialogue0008')
        vars[305] = 1
    elif sel == 2:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_13.scene0.dialogue0009')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_13.scene0.dialogue0010')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_13.scene0.dialogue0011')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_13.scene0.dialogue0012')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_13.scene0.dialogue0013')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_13.scene0.dialogue0014')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_13.scene0.dialogue0015')
        stage.unit(22).setAction(0)
        stage.say('@dialogue:R_13.scene0.dialogue0016')
        stage.unit(0).move(34, 59, 2)
        stage.say('@dialogue:R_13.scene0.dialogue0017')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_13.scene0.dialogue0018')
        stage.unit(0).move(34, 55, 0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_13.scene0.dialogue0019')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_13.scene0.dialogue0020')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_13.scene0.dialogue0021')
        vars[305] = 0
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            101,
            53,
            28,
            2],
        [
            113,
            68,
            48,
            3],
        [
            114,
            68,
            54,
            3],
        [
            115,
            68,
            60,
            3],
        [
            109,
            68,
            66,
            3],
        [
            106,
            38,
            48,
            1],
        [
            110,
            38,
            54,
            1],
        [
            102,
            38,
            60,
            1],
        [
            103,
            38,
            66,
            1],
        [
            193,
            53,
            48,
            0,
            6]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('며칠 뒤…….', 3)
    stage.setStageName('신도  원소 저택')
    if vars[305]:
        stage.say('@dialogue:R_13.scene1.dialogue0001')
        stage.unit(101).setAction(0, 1)
        stage.say('@dialogue:R_13.scene1.dialogue0002')
        stage.unit(101).setAction(0, 2)
        stage.delay(3)
        stage.unit(101).setAction(3)
        stage.say('@dialogue:R_13.scene1.dialogue0003')
        stage.unit(101).setAction(0)
        stage.delay(3)
        stage.unit(101).setAction(17)
        stage.say('@dialogue:R_13.scene1.dialogue0004')
    if not vars[305]:
        stage.say('@dialogue:R_13.scene1.dialogue0005')
        stage.unit(101).setAction(0, 1)
        stage.say('@dialogue:R_13.scene1.dialogue0006')
        stage.unit(101).setAction(0, 2)
        stage.say('@dialogue:R_13.scene1.dialogue0007')
        stage.unit(101).setAction(0, 0)
        stage.say('@dialogue:R_13.scene1.dialogue0008')
        stage.unit(101).setAction(0, 2)
        stage.delay(3)
        stage.unit(101).setAction(17, 2)
        stage.say('@dialogue:R_13.scene1.dialogue0009')
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, -1)
    stage.draw()
    stage.showHead(0, 342, 111)
    stage.showHead(101, 390, 22)
    stage.showHead(161, 471, 0)
    stage.bottomTxt('조조의 답을 받은 원소는,', False, False, False)
    stage.delay(5)
    stage.head(101).move(407, 0)
    stage.delay(5)
    stage.bottomTxt('군사를 일으켜 북평을 공격했다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene2():
    '허도  승상부'
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
            5,
            64,
            70],
        [
            20,
            52,
            40,
            2],
        [
            22,
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
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_13.scene2.dialogue0001')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_13.scene2.dialogue0002')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_13.scene2.dialogue0003')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_13.scene2.dialogue0004')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_13.scene2.dialogue0005')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_13.scene2.dialogue0006')
    stage.unit(0).setAction(17, 2)
    stage.say('@dialogue:R_13.scene2.dialogue0007')
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_13.scene2.dialogue0008')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_13.scene2.dialogue0009')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(17, 1)
    stage.say('@dialogue:R_13.scene2.dialogue0010')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 6)
    stage.showUnits([
        [
            119,
            69,
            55,
            3],
        [
            126,
            52,
            55,
            1]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('서주  여포 저택')
    stage.delay(3)
    stage.unit(119).setAction(2)
    stage.say('@dialogue:R_13.scene3.dialogue0001')
    stage.unit(119).setAction(0)
    stage.say('@dialogue:R_13.scene3.dialogue0002')
    stage.unit(119).setAction(2)
    stage.say('@dialogue:R_13.scene3.dialogue0003')
    stage.unit(119).setAction(0)
    stage.say('@dialogue:R_13.scene3.dialogue0004')
    stage.unit(119).move(69, 51, 0)
    stage.say('@dialogue:R_13.scene3.dialogue0005')
    stage.unit(119).move(69, 55, 2)
    stage.say('@dialogue:R_13.scene3.dialogue0006')
    stage.unit(119).setAction(0, 3)
    stage.delay(3)
    stage.unit(119).setAction(2, 3)
    stage.say('@dialogue:R_13.scene3.dialogue0007')
    stage.unit(119).setAction(17, 3)
    stage.say('@dialogue:R_13.scene3.dialogue0008')
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 328, 103)
    stage.showHead(32, 422, 111)
    stage.showHead(119, 510, 138)
    stage.bottomTxt('여포는 유비가 방심한 틈을 타', False, False, False)
    stage.delay(5)
    stage.head(119).move(486, 111)
    stage.delay(5)
    stage.bottomTxt('군사를 일으켜 소패로 쳐들어갔다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('위기에 빠진 유비는 사자를 보내 조조에게 구원을 청했다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene4():
    model.initLocalVar()
    stage.clsUnit()
    stage.bgSound(15)
    stage.delay(3)
    stage.setMenuVisible(False)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            1,
            38,
            54,
            1],
        [
            15,
            38,
            48,
            1],
        [
            10,
            38,
            42,
            1],
        [
            20,
            60,
            54,
            3],
        [
            22,
            60,
            48,
            3],
        [
            21,
            60,
            42,
            3],
        [
            193,
            49,
            54,
            2,
            6]])
    stage.draw()
    stage.delay(3)
    stage.info('당시 조조군은 서주로 향하던 중이었다.')
    stage.delay(3)
    stage.setStageName('초현 부근  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_13.scene4.dialogue0001')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_13.scene4.dialogue0002')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_13.scene4.dialogue0003')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3, 1)
    stage.say('@dialogue:R_13.scene4.dialogue0004')
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_13.scene4.dialogue0005')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(193).move(49, 16, 0)
    stage.unit(193).hide()
    stage.say('@dialogue:R_13.scene4.dialogue0006')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_13.scene4.dialogue0007')
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 11, [
        1])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 13
    stage.setBattlePos([
        [
            1,
            1],
        [
            4,
            16],
        [
            3,
            15],
        [
            2,
            16],
        [
            2,
            14],
        [
            3,
            1],
        [
            1,
            3],
        [
            2,
            2],
        [
            2,
            0],
        [
            0,
            2],
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
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_13.scene5.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_13.scene5.dialogue0002')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_13.scene5.dialogue0003')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_13.scene5.dialogue0004')
    if stage.unitClickTest(10):
        stage.say('@dialogue:R_13.scene5.dialogue0005')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_13.scene5.dialogue0006')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_13.scene5.dialogue0007')
    if stage.unitClickTest(193):
        stage.say('@dialogue:R_13.scene5.dialogue0008')
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
