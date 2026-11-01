# Source Generated with Decompyle++
# File: R_27.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 265, 172)
    stage.showHead(32, 329, 172)
    stage.showHead(76, 565, 216)
    stage.bottomTxt('유비군', False, False, False)
    stage.delay(5)
    stage.head(32).move(364, 192)
    stage.delay(5)
    stage.bottomTxt('강하로 달아났다.', True, False, False)
    stage.delay(8)
    stage.head(0).move(272, 213)
    stage.delay(5)
    stage.bottomTxt('조조군은 장강 연안의 요충지 강릉에 진주했다.', True, True, False)
    stage.delay(8)
    stage.showHead(35, 428, 192)
    stage.bottomTxt('제갈량은 조조에 맞서고자 유비에게 손권과의 동맹을 건의했다.', True, True, False)
    stage.delay(5)
    stage.head(35).move(501, 216)
    stage.delay(5)
    stage.bottomTxt('홀로 동오로 가서 유세했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 49)
    stage.showUnits([
        [
            0,
            53,
            72],
        [
            24,
            40,
            56,
            1]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('적벽으로 가는 길')
    stage.setStageName('강릉  임시 승상부')
    stage.say('@dialogue:R_27.scene0.dialogue0001')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_27.scene0.dialogue0002')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_27.scene0.dialogue0003')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_27.scene0.dialogue0004')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_27.scene0.dialogue0005')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_27.scene0.dialogue0006')
    stage.say('@dialogue:R_27.scene0.dialogue0007')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 10)
    stage.showUnits([
        [
            76,
            71,
            55,
            3],
        [
            77,
            55,
            38,
            2],
        [
            88,
            49,
            38,
            2],
        [
            35,
            55,
            55,
            1],
        [
            192,
            55,
            71],
        [
            193,
            49,
            71],
        [
            194,
            43,
            71]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('동오  손권의 관저')
    stage.info('조조의 최후통첩을 받은 동오에서는\n신하들이 주전파와 주화파로 갈렸다.\n하지만 제갈량의 설득으로\n점차 개전에 뜻을 모았다.')
    stage.say('@dialogue:R_27.scene1.dialogue0001')
    stage.unit(88).setAction(6)
    stage.say('@dialogue:R_27.scene1.dialogue0002')
    stage.unit(88).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('@dialogue:R_27.scene1.dialogue0003')
    stage.unit(77).setAction(4)
    stage.say('@dialogue:R_27.scene1.dialogue0004')
    stage.unit(77).setAction(17)
    stage.say('@dialogue:R_27.scene1.dialogue0005')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_27.scene1.dialogue0006')
    stage.unit(35).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 0)
    stage.say('@dialogue:R_27.scene1.dialogue0007')
    stage.unit(76).setAction(3)
    stage.say('@dialogue:R_27.scene1.dialogue0008')
    stage.unit(76).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.say('@dialogue:R_27.scene1.dialogue0009')
    stage.say('@dialogue:R_27.scene1.dialogue0010')
    stage.unit(77).setAction(3)
    stage.say('@dialogue:R_27.scene1.dialogue0011')
    stage.unit(77).setAction(11)
    stage.say('@dialogue:R_27.scene1.dialogue0012')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_27.scene1.dialogue0013')
    stage.unit(76).setAction(17)
    stage.say('@dialogue:R_27.scene1.dialogue0014')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 49)
    stage.showUnits([
        [
            0,
            53,
            72],
        [
            24,
            40,
            56,
            1]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('강릉  임시 승상부')
    stage.say('@dialogue:R_27.scene2.dialogue0001')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_27.scene2.dialogue0002')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_27.scene2.dialogue0003')
    stage.say('@dialogue:R_27.scene2.dialogue0004')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 264, 192)
    stage.showHead(32, 368, 191)
    stage.showHead(76, 559, 210)
    stage.bottomTxt('손권은 조조군과 싸우기로 결심하고,', False, False, False)
    stage.delay(5)
    stage.head(32).move(368, 158)
    stage.head(76).move(339, 238)
    stage.delay(5)
    stage.bottomTxt('이에 적벽으로 함선을 집결시켰다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('조조군도 움직여', True, True, False)
    stage.delay(5)
    stage.head(0).move(275, 238)
    stage.delay(5)
    stage.bottomTxt('적벽 건너편 오림에 들어갔다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('양군이 팽팽히 대치하던 그때,', True, True, False)
    stage.delay(5)
    stage.bottomTxt('한 사람이 조조의 본진을 찾아왔다.', True, False, False)
    stage.delay(5)
    stage.showHead(90, 243, 158)
    stage.delay(5)
    stage.bottomTxt('그가 바로 방통이었다.', True, False, True)
    stage.bgSound(-1)
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
            90,
            54,
            64,
            2]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setStageName('오림  조조군 본진')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_27.scene3.dialogue0001')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_27.scene3.dialogue0002')
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_27.scene3.dialogue0003')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(90).setAction(6)
    stage.say('@dialogue:R_27.scene3.dialogue0004')
    stage.unit(90).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_27.scene3.dialogue0005')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_27.scene3.dialogue0006')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_27.scene3.dialogue0007')
    stage.unit(90).setAction(6)
    stage.say('@dialogue:R_27.scene3.dialogue0008')
    stage.unit(90).setAction(0)
    stage.say('@dialogue:R_27.scene3.dialogue0009')
    stage.unit(90).setAction(6)
    stage.say('@dialogue:R_27.scene3.dialogue0010')
    stage.unit(90).setAction(0)
    stage.delay(3)
    stage.unit(90).move(54, 22, 0)
    stage.unit(90).hide()
    stage.say('@dialogue:R_27.scene3.dialogue0011')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_27.scene3.dialogue0012')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_27.scene3.dialogue0013')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_27.scene3.dialogue0014')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_27.scene3.dialogue0015')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene4():
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
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.info('며칠 뒤…….', 3)
    stage.setStageName('오림  조조군 본진')
    stage.showUnit(181, 54, 22, 2)
    stage.say('@dialogue:R_27.scene4.dialogue0001')
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_27.scene4.dialogue0002')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.delay(5)
    stage.showUnit(192, 54, 22, 2)
    stage.say('@dialogue:R_27.scene4.dialogue0003')
    stage.unit(192).move(54, 64, 2)
    stage.say('@dialogue:R_27.scene4.dialogue0004')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_27.scene4.dialogue0005')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).move(54, 22, 0)
    stage.unit(192).hide()
    stage.say('@dialogue:R_27.scene4.dialogue0006')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_27.scene4.dialogue0007')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(0, 2)
    stage.say('@dialogue:R_27.scene4.dialogue0008')
    stage.effectSound(208, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('사자를 믿는다\n믿을 수 없다', 0)
    if sel == 1:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(208, 0)
        stage.delay(3)
        stage.say('@dialogue:R_27.scene4.dialogue0009')
        stage.unit(20).move(54, 64, 3)
        stage.delay(1)
        stage.unit(20).move(54, 22, 0)
        stage.unit(20).hide()
        stage.unit(23).setAction(0, 2)
        stage.say('@dialogue:R_27.scene4.dialogue0010')
        stage.delay(5)
        stage.showUnit(20, 54, 22, 2)
        stage.unit(20).move(54, 64, 2)
        stage.unit(20).setAction(6)
        stage.unit(23).setAction(0, 3)
        stage.unit(24).setAction(0, 3)
        stage.delay(3)
        stage.say('@dialogue:R_27.scene4.dialogue0011')
        stage.unit(20).move(64, 64, 3)
        stage.delay(5)
    elif sel == 2:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(208, 0)
        stage.delay(3)
        stage.say('@dialogue:R_27.scene4.dialogue0012')
        stage.delay(5)
        stage.showUnit(181, 54, 22, 2)
        stage.unit(181).move(54, 64, 2)
        stage.unit(20).setAction(0, 3)
        stage.unit(24).setAction(0, 3)
        stage.delay(3)
        stage.unit(181).setAction(11)
        stage.say('@dialogue:R_27.scene4.dialogue0013')
        stage.unit(181).setAction(0)
        stage.delay(3)
        stage.unit(181).move(54, 76, 2)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.delay(3)
        stage.unit(181).move(54, 64, 2)
        stage.delay(3)
        stage.unit(181).setAction(11)
        stage.say('@dialogue:R_27.scene4.dialogue0014')
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_27.scene4.dialogue0015')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_27.scene4.dialogue0016')
        stage.unit(181).setAction(0)
        stage.delay(3)
        stage.unit(181).move(54, 22, 0)
        stage.unit(181).hide()
        stage.delay(5)
    stage.showUnit(192, 54, 22, 2)
    stage.say('@dialogue:R_27.scene4.dialogue0017')
    stage.unit(192).move(54, 64, 2)
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_27.scene4.dialogue0018')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_27.scene4.dialogue0019')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_27.scene4.dialogue0020')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_27.scene4.dialogue0021')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).move(54, 22, 0)
    stage.unit(192).hide()
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene5():
    if vars[724]:
        stage.bgSound(0)
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
                23,
                64,
                58,
                3],
            [
                21,
                64,
                52,
                3]])
        stage.draw()
        stage.info('황개의 투항을 받아들인 지 사흘 뒤,\n마침내 약속한 날이 왔다.')
        stage.setStageName('오림  조조군 본진')
        stage.say('@dialogue:R_27.scene5.dialogue0001')
        stage.say('@dialogue:R_27.scene5.dialogue0002')
        stage.showUnit(22, 54, 22, 2)
        stage.unit(22).move(54, 64, 2)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_27.scene5.dialogue0003')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_27.scene5.dialogue0004')
        model.setFace(0)
        stage.say('@dialogue:R_27.scene5.dialogue0005')
        stage.unit(22).setAction(0)
        stage.say('@dialogue:R_27.scene5.dialogue0006')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_27.scene5.dialogue0007')
        stage.unit(22).setAction(0, 1)
        stage.say('@dialogue:R_27.scene5.dialogue0008')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_27.scene5.dialogue0009')
        stage.unit(22).setAction(0, 2)
        stage.say('@dialogue:R_27.scene5.dialogue0010')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_27.scene5.dialogue0011')
        stage.unit(22).setAction(0)
        stage.say('@dialogue:R_27.scene5.dialogue0012')
        stage.say('@dialogue:R_27.scene5.dialogue0013')
        stage.say('@dialogue:R_27.scene5.dialogue0014')
        stage.delay(3)
        stage.unit(22).setAction(0, 1)
        stage.say('@dialogue:R_27.scene5.dialogue0015')
        stage.unit(22).setAction(0, 2)
        stage.say('@dialogue:R_27.scene5.dialogue0016')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_27.scene5.dialogue0017')
        stage.unit(22).setAction(0)
        stage.say('@dialogue:R_27.scene5.dialogue0018')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_27.scene5.dialogue0019')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('@dialogue:R_27.scene5.dialogue0020')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_27.scene5.dialogue0021')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_27.scene5.dialogue0022')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(0, 3)
        stage.say('@dialogue:R_27.scene5.dialogue0023')
        stage.unit(22).setAction(0, 2)
        stage.say('@dialogue:R_27.scene5.dialogue0024')
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_27.scene5.dialogue0025')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_27.scene5.dialogue0026')
        stage.unit(0).setAction(0)
        stage.unit(22).setAction(0)
        stage.setMenuVisible(True)
        stage.setJoinBattle(1, 15, [
            2,
            21,
            22])
        gvars[4076] = 0
        gvars["campaign.current_chapter"] = 27
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
    if not vars[724]:
        stage.jumpScene(55)
    return False


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_27.scene6.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_27.scene6.dialogue0002')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_27.scene6.dialogue0003')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_27.scene6.dialogue0004')
    if stage.unitClickTest(17):
        stage.say('@dialogue:R_27.scene6.dialogue0005')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_27.scene6.dialogue0006')
    if stage.unitClickTest(21):
        stage.say('@dialogue:R_27.scene6.dialogue0007')
    if stage.unitClickTest(23):
        stage.say('@dialogue:R_27.scene6.dialogue0008')
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
