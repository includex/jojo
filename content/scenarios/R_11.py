# Source Generated with Decompyle++
# File: R_11.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    if vars[710]:
        stage.bgSound(13)
        stage.delay(3)
        stage.loadBg(2, 35)
        stage.showUnits([
            [
                0,
                59,
                66],
            [
                181,
                69,
                58,
                3,
                11],
            [
                182,
                69,
                66,
                3,
                11]])
        stage.draw()
        stage.setEventName('거짓 황제 원술')
        stage.setStageName('완성  강가')
        stage.info('한때의 방심으로\n대장 전위를 잃은 조조는\n이후 완성 근처 강가에\n무덤을 세워 전위의 영령을 기렸다.')
        stage.delay(5)
        stage.say('@dialogue:R_11.scene0.dialogue0001')
        stage.delay(5)
        stage.unit(0).setAction(0, 3)
        stage.delay(5)
        stage.say('@dialogue:R_11.scene0.dialogue0002')
        stage.delay(5)
        stage.say('@dialogue:R_11.scene0.dialogue0003')
        stage.delay(5)
        stage.unit(0).setAction(0, 1)
        stage.delay(5)
        stage.unit(0).move(65, 66, 1)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(5)
        stage.delay(5)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).move(59, 66, 3)
        stage.delay(10)
        stage.unit(0).setAction(5)
        stage.say('@dialogue:R_11.scene0.dialogue0004')
        model.unitJoin(16, 255, 0)
        stage.getItem(90, 0)
        stage.delay(10)
        stage.say('@dialogue:R_11.scene0.dialogue0005')
        stage.say('@dialogue:R_11.scene0.dialogue0006')
        stage.unit(0).setAction(0)
        stage.delay(8)
        stage.say('@dialogue:R_11.scene0.dialogue0007')
        stage.clsUnit()
        stage.bgSound(-1)
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(143, 253, 147)
    stage.showHead(0, 317, 147)
    stage.bottomTxt('장수에게 큰 타격을 입은 조조는,', False, False, False)
    stage.delay(5)
    stage.head(0).move(338, 106)
    stage.delay(5)
    stage.bottomTxt('허도로 돌아가 전열을 정비했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('어느 날,', True, True, False)
    stage.delay(8)
    stage.showHead(136, 427, 178)
    stage.delay(5)
    stage.bottomTxt('수춘의 원술이 갑자기 제위에 올라 황제를 칭했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('머지않아 조조에게도 소식이 전해졌다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene1():
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
    stage.effectSound(201, 0)
    stage.delay(3)
    if not vars[710]:
        stage.setEventName('거짓 황제 원술')
    stage.setStageName('허도  조조 저택')
    stage.unit(22).setAction(6, 2)
    stage.say('@dialogue:R_11.scene1.dialogue0001')
    stage.unit(22).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6, 2)
    stage.say('@dialogue:R_11.scene1.dialogue0002')
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(6, 2)
    stage.say('@dialogue:R_11.scene1.dialogue0003')
    stage.say('@dialogue:R_11.scene1.dialogue0004')
    stage.unit(21).setAction(0, 3)
    stage.say('@dialogue:R_11.scene1.dialogue0005')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(0, 1)
    stage.say('@dialogue:R_11.scene1.dialogue0006')
    stage.say('@dialogue:R_11.scene1.dialogue0007')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_11.scene1.dialogue0008')
    stage.unit(24).setAction(0, 3)
    stage.say('@dialogue:R_11.scene1.dialogue0009')
    stage.say('@dialogue:R_11.scene1.dialogue0010')
    stage.unit(22).setAction(0, 3)
    stage.say('@dialogue:R_11.scene1.dialogue0011')
    stage.unit(22).setAction(0, 2)
    stage.say('@dialogue:R_11.scene1.dialogue0012')
    stage.unit(22).setAction(0, 3)
    stage.say('@dialogue:R_11.scene1.dialogue0013')
    stage.unit(0).setAction(3, 1)
    stage.say('@dialogue:R_11.scene1.dialogue0014')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_11.scene1.dialogue0015')
    stage.unit(0).setAction(2, 1)
    stage.say('@dialogue:R_11.scene1.dialogue0016')
    stage.effectSound(201, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 332, 98)
    stage.showHead(136, 428, 171)
    stage.bottomTxt('유비,', False, False, False)
    stage.delay(5)
    stage.showHead(32, 396, 80)
    stage.bottomTxt('여포와', True, False, False)
    stage.delay(5)
    stage.showHead(119, 517, 125)
    stage.bottomTxt('손책', True, False, False)
    stage.delay(5)
    stage.showHead(100, 567, 211)
    stage.bottomTxt('조조에게 공격해 달라는 요청을 받고', True, False, False)
    stage.delay(5)
    stage.bottomTxt('즉시 원술을 공격할 군사를 내겠다고 약속했고,', True, False, False)
    stage.delay(5)
    stage.head(32).move(428, 91)
    stage.head(0).move(364, 171)
    stage.head(100).move(567, 221)
    stage.head(100).move(492, 221)
    stage.head(119).move(492, 141)
    stage.bottomTxt('각각 병사를 이끌고 원술의 본거지 수춘으로 쳐들어갔다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene2():
    stage.bgSound(15)
    stage.delay(3)
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
            13,
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
            24,
            60,
            42,
            3]])
    stage.draw()
    stage.info('당시 수춘에는 큰비가 끊이지 않아\n홍수로 막대한 피해가 발생했다.')
    stage.setStageName('수춘 교외  조조군 본진')
    stage.say('@dialogue:R_11.scene2.dialogue0001')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_11.scene2.dialogue0002')
    stage.say('@dialogue:R_11.scene2.dialogue0003')
    stage.unit(20).setAction(6, 2)
    stage.say('@dialogue:R_11.scene2.dialogue0004')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_11.scene2.dialogue0005')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_11.scene2.dialogue0006')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(17, 0)
    stage.say('@dialogue:R_11.scene2.dialogue0007')
    stage.unit(0).setAction(0, 0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 12, [
        22])
    gvars[4076] = 0
    gvars[4057] = 11
    stage.setBattlePos([
        [
            12,
            0],
        [
            13,
            0],
        [
            13,
            2],
        [
            12,
            2],
        [
            11,
            2],
        [
            12,
            1],
        [
            14,
            1],
        [
            10,
            1],
        [
            13,
            1],
        [
            11,
            1],
        [
            10,
            0],
        [
            14,
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


def scene3():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_11.scene3.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_11.scene3.dialogue0002')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_11.scene3.dialogue0003')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_11.scene3.dialogue0004')
    if stage.unitClickTest(13):
        stage.say('@dialogue:R_11.scene3.dialogue0005')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_11.scene3.dialogue0006')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_11.scene3.dialogue0007')
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
