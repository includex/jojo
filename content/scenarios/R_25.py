# Source Generated with Decompyle++
# File: R_25.marshal (Python 3.9)


def scene0():
    '곽가의 죽음'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.delay(3)
    stage.setMenuVisible(False)
    if not vars[724]:
        stage.bgSound(13)
        stage.delay(3)
        stage.loadBg(2, 5)
        stage.showUnits([
            [
                22,
                55,
                46,
                2,
                10],
            [
                203,
                58,
                43,
                2,
                14],
            [
                0,
                52,
                52],
            [
                1,
                60,
                52],
            [
                2,
                58,
                60]])
        stage.draw()
        stage.setEventName('곽가의 죽음')
        stage.setStageName('남피  곽가 저택')
        stage.say('@dialogue:R_25.scene0.dialogue0001')
        stage.unit(1).setAction(0, 3)
        stage.say('@dialogue:R_25.scene0.dialogue0002')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_25.scene0.dialogue0003')
        model.setFace(1)
        stage.say('@dialogue:R_25.scene0.dialogue0004')
        stage.say('@dialogue:R_25.scene0.dialogue0005')
        stage.unit(1).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        model.setFace(0)
        stage.say('@dialogue:R_25.scene0.dialogue0006')
        stage.say('@dialogue:R_25.scene0.dialogue0007')
        stage.say('@dialogue:R_25.scene0.dialogue0008')
        stage.unit(0).move(52, 48, 0)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.say('@dialogue:R_25.scene0.dialogue0009')
        stage.getItem(133, 0)
        stage.say('@dialogue:R_25.scene0.dialogue0010')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_25.scene0.dialogue0011')
        stage.say('@dialogue:R_25.scene0.dialogue0012')
        model.setFace(1)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:R_25.scene0.dialogue0013')
        stage.unit(1).setAction(2)
        stage.say('@dialogue:R_25.scene0.dialogue0014')
        stage.unit(2).setAction(6)
        stage.say('@dialogue:R_25.scene0.dialogue0015')
        stage.unit(0).setAction(4)
        model.setFace(0)
        stage.say('@dialogue:R_25.scene0.dialogue0016')
        model.unitJoin(22, 255, 0)
        stage.info('조조가 의지하던 모사 곽가가 세상을 떠났다.\n조조는 그를 떠올릴 때마다 탄식을 금치 못했다.\n참으로 뛰어난 인재의 요절이었다.')
        model.setFace(0)
        stage.bgSound(-1)
        stage.clsUnit()
    stage.bgSound(-1)
    stage.delay(3)
    if vars[724]:
        stage.loadBg(2, 46)
        stage.showUnits([
            [
                114,
                50,
                69],
            [
                115,
                50,
                55,
                2]])
        stage.draw()
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.setEventName('하북 평정')
        stage.setStageName('유성  원상의 저택')
        stage.unit(114).setAction(0, 1)
        stage.say('@dialogue:R_25.scene0.dialogue0017')
        stage.unit(115).setAction(6)
        stage.say('@dialogue:R_25.scene0.dialogue0018')
        stage.unit(115).setAction(0)
        stage.delay(3)
        stage.unit(114).setAction(0, 0)
        stage.say('@dialogue:R_25.scene0.dialogue0019')
        stage.unit(115).setAction(6)
        stage.say('@dialogue:R_25.scene0.dialogue0020')
        stage.unit(115).setAction(0)
        stage.say('@dialogue:R_25.scene0.dialogue0021')
        stage.unit(114).setAction(3)
        stage.say('@dialogue:R_25.scene0.dialogue0022')
        stage.unit(114).setAction(0)
        stage.delay(3)
        stage.unit(115).setAction(3)
        stage.say('@dialogue:R_25.scene0.dialogue0023')
        stage.unit(115).setAction(0)
        stage.delay(3)
        stage.unit(114).setAction(3)
        stage.say('@dialogue:R_25.scene0.dialogue0024')
        stage.unit(114).setAction(0)
        stage.delay(3)
        stage.unit(115).setAction(6)
        stage.say('@dialogue:R_25.scene0.dialogue0025')
        stage.effectSound(200, 255)
        stage.clsUnit()
    stage.loadBg(2, 67)
    stage.draw()
    stage.showUnits([
        [
            186,
            48,
            95],
        [
            185,
            56,
            95]])
    if not vars[724]:
        stage.setEventName('하북 평정')
    stage.setStageName('유성 교외  산길')
    stage.unitsMove([
        [
            stage.unit(186),
            48,
            46,
            0],
        [
            stage.unit(185),
            56,
            46,
            0]])
    stage.unit(185).setAction(0)
    stage.unit(186).setAction(0)
    stage.say('@dialogue:R_25.scene0.dialogue0026')
    stage.unit(185).setAction(0, 3)
    stage.say('@dialogue:R_25.scene0.dialogue0027')
    stage.unit(186).setAction(0, 1)
    stage.say('@dialogue:R_25.scene0.dialogue0028')
    stage.unit(186).setAction(0, 2)
    stage.say('@dialogue:R_25.scene0.dialogue0029')
    stage.say('@dialogue:R_25.scene0.dialogue0030')
    stage.unit(186).setAction(0, 1)
    stage.say('@dialogue:R_25.scene0.dialogue0031')
    stage.unit(185).move(52, 46, 3)
    stage.delay(3)
    stage.unit(185).setAction(0, 2)
    stage.bgSound(12)
    stage.delay(5)
    stage.say('@dialogue:R_25.scene0.dialogue0032')
    stage.unit(185).setAction(0, 3)
    stage.say('@dialogue:R_25.scene0.dialogue0033')
    stage.unit(186).move(32, 49, 1)
    stage.unit(186).setAction(0)
    stage.unit(185).move(66, 66, 3)
    stage.unit(185).setAction(0)
    stage.delay(3)
    stage.showUnits([
        [
            115,
            48,
            95],
        [
            114,
            56,
            95]])
    if not vars[724]:
        stage.unitsMove([
            [
                stage.unit(115),
                48,
                74,
                0],
            [
                stage.unit(114),
                56,
                74,
                0]])
        stage.delay(3)
        stage.unit(114).setAction(0, 3)
        stage.delay(3)
        stage.unit(114).setAction(3)
        stage.say('@dialogue:R_25.scene0.dialogue0034')
        stage.unit(114).setAction(0)
        stage.delay(3)
        stage.unit(115).setAction(0, 1)
        stage.delay(3)
        stage.unit(115).setAction(6)
        stage.say('@dialogue:R_25.scene0.dialogue0035')
        stage.unit(115).setAction(0)
        stage.say('@dialogue:R_25.scene0.dialogue0036')
    if vars[724]:
        stage.unitsMove([
            [
                stage.unit(115),
                48,
                74,
                0],
            [
                stage.unit(114),
                56,
                74,
                0]])
        stage.delay(3)
        stage.unit(114).setAction(0, 3)
        stage.delay(3)
        stage.unit(114).setAction(3)
        stage.say('@dialogue:R_25.scene0.dialogue0037')
        stage.unit(114).setAction(0)
        stage.delay(3)
        stage.unit(115).setAction(0, 1)
        stage.delay(3)
        stage.unit(115).setAction(6)
        stage.say('@dialogue:R_25.scene0.dialogue0038')
        stage.unit(115).setAction(0)
    stage.unitsMove([
        [
            stage.unit(115),
            48,
            49,
            0],
        [
            stage.unit(114),
            56,
            49,
            0]])
    stage.unit(115).setAction(0)
    stage.unit(114).setAction(0)
    stage.bgSound(-1)
    stage.delay(5)
    stage.say('@dialogue:R_25.scene0.dialogue0039')
    stage.unit(115).setAction(0, 3)
    stage.say('@dialogue:R_25.scene0.dialogue0040')
    stage.unit(186).move(44, 49, 1)
    stage.unit(114).setAction(0, 3)
    stage.delay(3)
    stage.effectSound(34, 1)
    stage.unit(186).setAction(13)
    stage.delay(8)
    stage.effectSound(36, 1)
    stage.unit(186).setAction(14)
    stage.say('@dialogue:R_25.scene0.dialogue0041')
    stage.delay(5)
    stage.unit(115).setAction(10)
    stage.delay(3)
    stage.unit(114).setAction(3)
    stage.say('@dialogue:R_25.scene0.dialogue0042')
    stage.unit(185).move(56, 53, 0)
    stage.delay(3)
    stage.effectSound(34, 1)
    stage.unit(185).setAction(13)
    stage.delay(8)
    stage.effectSound(36, 1)
    stage.unit(185).setAction(14)
    stage.say('@dialogue:R_25.scene0.dialogue0043')
    stage.delay(5)
    stage.unit(114).setAction(10)
    stage.say('@dialogue:R_25.scene0.dialogue0044')
    stage.say('@dialogue:R_25.scene0.dialogue0045')
    stage.clsUnit()
    stage.bgSound(0)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.delay(5)
    stage.showHead(0, 426, 0)
    stage.showHead(115, 490, 0)
    stage.showHead(114, 554, 0)
    stage.delay(8)
    stage.head(115).hide()
    stage.head(114).hide()
    stage.delay(5)
    stage.bottomTxt('한나라의 명문 원씨는 이렇게 멸망했다.\n지난날의 번영에 비하면 너무도 쓸쓸한 최후였다.', False, False, True)
    stage.bottomTxt('원씨가 멸망한 뒤 하북을 평정한 조조는\n단숨에 가장 강력한 제후로 떠올랐다.', False, False, True)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.sectionName(3, '삼국 정립')
    stage.bgSound(-1)
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            0,
            52,
            28,
            2],
        [
            1,
            38,
            48,
            1],
        [
            15,
            38,
            54,
            1],
        [
            8,
            38,
            60,
            1],
        [
            13,
            38,
            66,
            1],
        [
            2,
            38,
            72,
            1],
        [
            20,
            67,
            48,
            3],
        [
            23,
            67,
            54,
            3],
        [
            24,
            67,
            60,
            3],
        [
            21,
            67,
            66,
            3],
        [
            25,
            67,
            72,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('제갈공명의 등장')
    stage.setStageName('업성  임시 승상부')
    stage.say('@dialogue:R_25.scene1.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0002')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_25.scene1.dialogue0003')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0004')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0005')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_25.scene1.dialogue0006')
    stage.showUnit(159, 52, 92)
    stage.say('@dialogue:R_25.scene1.dialogue0007')
    stage.unit(159).move(52, 48, 0)
    stage.say('@dialogue:R_25.scene1.dialogue0008')
    stage.unit(159).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0009')
    stage.unit(159).setAction(0)
    stage.say('@dialogue:R_25.scene1.dialogue0010')
    stage.unit(159).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0011')
    stage.unit(159).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0012')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0013')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_25.scene1.dialogue0014')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_25.scene1.dialogue0015')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_25.scene1.dialogue0016')
    stage.unit(159).setAction(0, 2)
    stage.delay(3)
    stage.say('@dialogue:R_25.scene1.dialogue0017')
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_25.scene1.dialogue0018')
    stage.say('@dialogue:R_25.scene1.dialogue0019')
    stage.unit(8).setAction(3)
    stage.say('@dialogue:R_25.scene1.dialogue0020')
    stage.unit(8).setAction(0)
    stage.say('@dialogue:R_25.scene1.dialogue0021')
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_25.scene1.dialogue0022')
    stage.unit(159).setAction(0, 0)
    stage.say('@dialogue:R_25.scene1.dialogue0023')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0024')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(25).setAction(0, 0)
    stage.delay(3)
    stage.unit(25).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0025')
    stage.unit(25).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene1.dialogue0026')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_25.scene1.dialogue0027')
    stage.effectSound(200, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('동작대를 짓는다\n동작대를 짓지 않는다', 0)
    if sel == 1:
        vars[309] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_25.scene1.dialogue0028')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('@dialogue:R_25.scene1.dialogue0029')
        stage.unit(159).setAction(6)
        stage.say('@dialogue:R_25.scene1.dialogue0030')
        stage.unit(159).setAction(0)
        stage.say('@dialogue:R_25.scene1.dialogue0031')
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_25.scene1.dialogue0032')
        stage.unitsMove([
            [
                stage.unit(1),
                38,
                99,
                2],
            [
                stage.unit(8),
                38,
                99,
                2],
            [
                stage.unit(13),
                38,
                99,
                2],
            [
                stage.unit(15),
                38,
                99,
                2],
            [
                stage.unit(2),
                38,
                99,
                2],
            [
                stage.unit(25),
                67,
                99,
                2],
            [
                stage.unit(24),
                67,
                99,
                2],
            [
                stage.unit(21),
                67,
                99,
                2],
            [
                stage.unit(23),
                67,
                99,
                2],
            [
                stage.unit(159),
                52,
                99,
                2]])
        stage.unit(159).hide()
        stage.unit(1).hide()
        stage.unit(25).hide()
        stage.unit(15).hide()
        stage.unit(24).hide()
        stage.unit(8).hide()
        stage.unit(21).hide()
        stage.unit(13).hide()
        stage.unit(23).hide()
        stage.unit(2).hide()
        stage.say('@dialogue:R_25.scene1.dialogue0033')
        stage.unit(20).setAction(0, 0)
        stage.say('@dialogue:R_25.scene1.dialogue0034')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_25.scene1.dialogue0035')
    elif sel == 2:
        vars[309] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_25.scene1.dialogue0036')
        model.setFace(0)
        stage.unit(23).setAction(0, 0)
        stage.delay(3)
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_25.scene1.dialogue0037')
        stage.unit(23).setAction(0)
        stage.say('@dialogue:R_25.scene1.dialogue0038')
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_25.scene1.dialogue0039')
        stage.unit(0).setAction(0, 2)
        stage.delay(3)
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_25.scene1.dialogue0040')
        model.setFace(0)
        stage.unit(159).setAction(6)
        stage.say('@dialogue:R_25.scene1.dialogue0041')
        stage.unitsMove([
            [
                stage.unit(1),
                38,
                99,
                2],
            [
                stage.unit(25),
                67,
                99,
                2],
            [
                stage.unit(15),
                38,
                99,
                2],
            [
                stage.unit(24),
                67,
                99,
                2],
            [
                stage.unit(8),
                38,
                99,
                2],
            [
                stage.unit(21),
                67,
                99,
                2],
            [
                stage.unit(13),
                38,
                99,
                2],
            [
                stage.unit(23),
                67,
                99,
                2],
            [
                stage.unit(2),
                38,
                99,
                2],
            [
                stage.unit(0),
                52,
                99,
                2]])
        stage.unit(0).hide()
        stage.unit(1).hide()
        stage.unit(25).hide()
        stage.unit(15).hide()
        stage.unit(24).hide()
        stage.unit(8).hide()
        stage.unit(21).hide()
        stage.unit(13).hide()
        stage.unit(23).hide()
        stage.unit(2).hide()
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_25.scene1.dialogue0042')
        stage.unit(159).setAction(0)
        stage.delay(3)
        stage.unit(159).setAction(0, 1)
        stage.say('@dialogue:R_25.scene1.dialogue0043')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 370, 21)
    stage.bottomTxt('조조는 조식에게 업성을 다스리게 하고', False, False, False)
    stage.delay(5)
    stage.head(0).move(339, 102)
    stage.delay(5)
    stage.bottomTxt('허도로 돌아갔다.', True, False, True)
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(5)
    stage.showHead(32, 244, 166)
    stage.delay(5)
    stage.bottomTxt('유비는 유표의 부탁을 받아 신야의 수비를 맡았다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('이 소식을 들은 조조는', True, True, False)
    stage.head(0).hide()
    stage.delay(5)
    stage.showHead(5, 304, 70)
    stage.delay(5)
    stage.bottomTxt('이에 조인에게 명하여', True, False, False)
    stage.delay(5)
    stage.showHead(11, 368, 110)
    stage.delay(5)
    stage.bottomTxt('그리고 이전은', True, False, False)
    stage.delay(5)
    stage.head(5).move(244, 86)
    stage.head(11).move(308, 126)
    stage.delay(5)
    stage.bottomTxt('신야를 공격했다.', True, False, True)
    stage.bgSound(-1)
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
            2],
        [
            1,
            52,
            70],
        [
            15,
            58,
            70],
        [
            2,
            64,
            70],
        [
            5,
            52,
            58,
            3,
            11],
        [
            11,
            52,
            52,
            3,
            11]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.info('한 달 뒤…….', 3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_25.scene2.dialogue0001')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene2.dialogue0002')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_25.scene2.dialogue0003')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_25.scene2.dialogue0004')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_25.scene2.dialogue0005')
    stage.unitsMove([
        [
            stage.unit(11),
            98,
            52,
            1],
        [
            stage.unit(5),
            98,
            58,
            1]])
    stage.unit(11).hide()
    stage.unit(5).hide()
    stage.say('@dialogue:R_25.scene2.dialogue0006')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene2.dialogue0007')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_25.scene2.dialogue0008')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene2.dialogue0009')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_25.scene2.dialogue0010')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_25.scene2.dialogue0011')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene2.dialogue0012')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_25.scene2.dialogue0013')
    stage.unit(21).setAction(0)
    stage.say('@dialogue:R_25.scene2.dialogue0014')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene2.dialogue0015')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(0, 35)
    stage.draw()
    stage.info('그 뒤 정욱은 서서의 모친을 극진히 대접하며\n서서를 허도로 불러 달라고 설득했으나\n끝내 허락을 받지 못했다.')
    stage.info('정욱은 설득이 통하지 않자 서서 모친의 필체를 흉내 내어\n가짜 서신을 쓰고 사람을 시켜 서서에게 보냈다.')
    stage.info('거짓임을 몰랐던 서서는 모친을 향한 효심으로\n유비와 작별하고 조조가 있는 허도로 왔다.')
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            21,
            52,
            40,
            2],
        [
            151,
            52,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_25.scene3.dialogue0001')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(151).move(98, 55, 1)
    stage.unit(151).hide()
    stage.say('@dialogue:R_25.scene3.dialogue0002')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_25.scene3.dialogue0003')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 36)
    stage.showUnit(152, 53, 42, 2)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  서서 모친의 거처')
    stage.showUnit(151, 53, 90)
    stage.say('@dialogue:R_25.scene4.dialogue0001')
    stage.unitsMove([
        [
            stage.unit(151),
            53,
            85,
            0],
        [
            stage.unit(152),
            58,
            42,
            1]])
    stage.unitsMove([
        [
            stage.unit(151),
            53,
            74,
            0],
        [
            stage.unit(152),
            58,
            53,
            2]])
    stage.unitsMove([
        [
            stage.unit(151),
            53,
            61,
            0],
        [
            stage.unit(152),
            53,
            53,
            2]])
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('@dialogue:R_25.scene4.dialogue0002')
    stage.unit(151).setAction(0)
    stage.say('@dialogue:R_25.scene4.dialogue0003')
    stage.unit(151).move(53, 57, 0)
    stage.delay(5)
    stage.unit(151).move(53, 61, 0)
    stage.say('@dialogue:R_25.scene4.dialogue0004')
    stage.unit(152).setAction(0, 0)
    stage.say('@dialogue:R_25.scene4.dialogue0005')
    stage.unit(152).setAction(0, 2)
    stage.say('@dialogue:R_25.scene4.dialogue0006')
    stage.unit(152).move(57, 53, 1)
    stage.say('@dialogue:R_25.scene4.dialogue0007')
    stage.unit(152).move(57, 98, 2)
    stage.unit(152).hide()
    stage.unit(151).setAction(0, 2)
    stage.say('@dialogue:R_25.scene4.dialogue0008')
    stage.delay(10)
    stage.effectSound(202, 255)
    stage.bgSound(13)
    stage.delay(3)
    stage.unit(151).setAction(4, 2)
    stage.say('@dialogue:R_25.scene4.dialogue0009')
    stage.delay(10)
    stage.say('@dialogue:R_25.scene4.dialogue0010')
    stage.unit(151).setAction(0, 2)
    stage.say('@dialogue:R_25.scene4.dialogue0011')
    stage.clsUnit()
    stage.loadBg(2, 37)
    stage.showUnits([
        [
            152,
            45,
            54,
            1,
            10],
        [
            189,
            45,
            48,
            2]])
    stage.draw()
    stage.showUnit(151, 20, 54, 1)
    stage.say('@dialogue:R_25.scene4.dialogue0012')
    stage.unit(151).move(39, 54, 1)
    stage.unit(151).setAction(0)
    stage.say('@dialogue:R_25.scene4.dialogue0013')
    stage.unit(151).setAction(4)
    stage.say('@dialogue:R_25.scene4.dialogue0014')
    stage.unit(151).setAction(9)
    stage.say('@dialogue:R_25.scene4.dialogue0015')
    stage.say('@dialogue:R_25.scene4.dialogue0016')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene5():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(8)
    stage.showHead(35, 311, 178)
    stage.delay(5)
    stage.bottomTxt('양양 서쪽의 작은 마을 융중에는 와룡이라 불리는 현사가 있었다.', True, True, False)
    stage.delay(5)
    stage.bottomTxt('그의 이름은 제갈량이었다.', True, True, True)
    stage.showHead(32, 233, 142)
    stage.delay(5)
    stage.bottomTxt('신야의 유비', False, False, False)
    stage.delay(5)
    stage.head(32).move(247, 178)
    stage.delay(5)
    stage.bottomTxt('삼고초려 끝에 제갈량을 모셔 왔고,', True, False, False)
    stage.delay(5)
    stage.head(32).move(223, 142)
    stage.head(35).move(287, 142)
    stage.delay(5)
    stage.bottomTxt('마침내 그를 출사시켜 군사로 모셨다.', True, True, True)
    stage.bgSound(-1)
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
            2],
        [
            1,
            52,
            70],
        [
            15,
            58,
            70],
        [
            2,
            64,
            70]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_25.scene5.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_25.scene5.dialogue0002')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_25.scene5.dialogue0003')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_25.scene5.dialogue0004')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_25.scene5.dialogue0005')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_25.scene5.dialogue0006')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_25.scene5.dialogue0007')
    stage.effectSound(200, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('하후돈의 유비 공격을 허락한다\n공명의 소식을 기다린다', 0)
    if sel == 1:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.say('@dialogue:R_25.scene5.dialogue0008')
    elif sel == 2:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.say('@dialogue:R_25.scene5.dialogue0009')
    stage.unit(1).setAction(0)
    stage.say('@dialogue:R_25.scene5.dialogue0010')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene6():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(32, 245, 172)
    stage.showHead(1, 335, 93)
    stage.delay(5)
    stage.bottomTxt('하후돈,', False, False, False)
    stage.delay(5)
    stage.showHead(14, 271, 83)
    stage.delay(5)
    stage.bottomTxt('우금과', True, False, False)
    stage.delay(5)
    stage.showHead(11, 399, 103)
    stage.delay(5)
    stage.bottomTxt('이전 등 세 사람은', True, False, False)
    stage.delay(8)
    stage.head(14).move(211, 92)
    stage.head(1).move(275, 92)
    stage.head(11).move(399, 172)
    stage.head(11).move(309, 172)
    stage.delay(5)
    stage.bottomTxt('신야로 진군하여 유비를 공격했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 13)
    stage.showUnit(32, 73, 55, 3)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('신야  유비 저택')
    stage.showUnit(35, 5, 55, 1)
    stage.say('@dialogue:R_25.scene6.dialogue0001')
    stage.unit(35).move(55, 55, 1)
    stage.say('@dialogue:R_25.scene6.dialogue0002')
    stage.say('@dialogue:R_25.scene6.dialogue0003')
    stage.unit(35).move(63, 55, 1)
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_25.scene6.dialogue0004')
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_25.scene6.dialogue0005')
    stage.unit(32).setAction(0, 2)
    stage.say('@dialogue:R_25.scene6.dialogue0006')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_25.scene6.dialogue0007')
    stage.unit(32).setAction(0, 3)
    stage.say('@dialogue:R_25.scene6.dialogue0008')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene7():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            1,
            54,
            80],
        [
            11,
            44,
            64,
            1],
        [
            14,
            64,
            64,
            3]])
    stage.draw()
    stage.setStageName('박망파  조조군 본진')
    stage.unit(1).setAction(17)
    stage.say('@dialogue:R_25.scene7.dialogue0001')
    stage.unit(1).setAction(0)
    if vars[1011]:
        model.unit(1).setPosts(78)
        stage.setFAvatar(1, 83)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 9, [
        1,
        14,
        11], [
        0,
        22])
    gvars[4076] = 0
    gvars[4057] = 25
    stage.setBattlePos([
        [
            10,
            11],
        [
            10,
            9],
        [
            11,
            8],
        [
            9,
            10],
        [
            11,
            10],
        [
            9,
            8],
        [
            12,
            9],
        [
            10,
            7],
        [
            12,
            7],
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
            0],
        [
            0,
            0]])
    return False


def scene8():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_25.scene8.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(11):
        stage.say('@dialogue:R_25.scene8.dialogue0002')
    if stage.unitClickTest(14):
        if not vars[724]:
            stage.say('@dialogue:R_25.scene8.dialogue0003')
        if vars[724]:
            stage.say('@dialogue:R_25.scene8.dialogue0004')
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
