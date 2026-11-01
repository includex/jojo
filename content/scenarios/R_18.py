# Source Generated with Decompyle++
# File: R_18.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 18)
    stage.showUnits([
        [
            101,
            35,
            56,
            1],
        [
            106,
            50,
            45,
            2],
        [
            103,
            58,
            45,
            2],
        [
            32,
            50,
            67]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('작별')
    stage.setStageName('연진  원소군 본진')
    stage.say('@dialogue:R_18.scene0.dialogue0001')
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_18.scene0.dialogue0002')
    stage.unit(103).setAction(0)
    stage.say('@dialogue:R_18.scene0.dialogue0003')
    stage.unit(32).setAction(3)
    stage.say('@dialogue:R_18.scene0.dialogue0004')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_18.scene0.dialogue0005')
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_18.scene0.dialogue0006')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(32).move(95, 67, 1)
    stage.unit(32).hide()
    stage.delay(3)
    stage.say('@dialogue:R_18.scene0.dialogue0007')
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_18.scene0.dialogue0008')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(106).setAction(6)
    stage.say('@dialogue:R_18.scene0.dialogue0009')
    stage.unit(106).setAction(0)
    stage.say('@dialogue:R_18.scene0.dialogue0010')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_18.scene0.dialogue0011')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_18.scene0.dialogue0012')
    stage.unit(103).setAction(6)
    stage.say('@dialogue:R_18.scene0.dialogue0013')
    stage.unit(101).setAction(0, 3)
    stage.say('@dialogue:R_18.scene0.dialogue0014')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 1)
    stage.showUnit(32, 53, 74)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('업성@유비의 거처')
    stage.showUnit(40, 53, 21, 2)
    stage.say('@dialogue:R_18.scene1.dialogue0001')
    stage.unit(40).move(53, 59, 2)
    stage.delay(3)
    stage.unit(32).setAction(0, 0)
    stage.say('@dialogue:R_18.scene1.dialogue0002')
    stage.unit(32).setAction(0, 2)
    stage.say('@dialogue:R_18.scene1.dialogue0003')
    stage.unit(32).setAction(0, 0)
    stage.say('@dialogue:R_18.scene1.dialogue0004')
    stage.unit(40).move(53, 61, 2)
    stage.say('@dialogue:R_18.scene1.dialogue0005')
    stage.unit(32).setAction(2)
    stage.say('@dialogue:R_18.scene1.dialogue0006')
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_18.scene1.dialogue0007')
    stage.unit(32).move(44, 74, 3)
    stage.delay(1)
    stage.unit(32).move(44, 61, 0)
    stage.unit(40).setAction(0, 3)
    stage.delay(3)
    stage.unit(32).setAction(0, 1)
    stage.say('@dialogue:R_18.scene1.dialogue0008')
    stage.unit(32).setAction(0, 2)
    stage.say('@dialogue:R_18.scene1.dialogue0009')
    stage.unit(32).setAction(0, 1)
    stage.say('@dialogue:R_18.scene1.dialogue0010')
    stage.unit(32).move(50, 61, 1)
    stage.say('@dialogue:R_18.scene1.dialogue0011')
    stage.unit(32).move(44, 61, 1)
    stage.say('@dialogue:R_18.scene1.dialogue0012')
    stage.delay(3)
    stage.say('@dialogue:R_18.scene1.dialogue0013')
    stage.unit(40).move(53, 21, 0)
    stage.unit(40).hide()
    stage.delay(3)
    stage.say('@dialogue:R_18.scene1.dialogue0014')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(0, 35)
    stage.draw()
    stage.setStageName('허도')
    stage.info('원소의 공세를 두 차례 물리친 조조군은\n허도로 돌아왔고, 장수들은 휴양의 나날을 보냈다.')
    stage.bgSound(-1)
    stage.loadBg(2, 0)
    stage.showUnit(3, 40, 54, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도   관우 저택')
    stage.say('@dialogue:R_18.scene2.dialogue0001')
    stage.showUnit(40, 90, 54, 3)
    stage.say('@dialogue:R_18.scene2.dialogue0002')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_18.scene2.dialogue0003')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(3),
            40,
            60,
            2],
        [
            stage.unit(40),
            80,
            54,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(3),
            52,
            60,
            1],
        [
            stage.unit(40),
            70,
            54,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(3),
            52,
            54,
            0],
        [
            stage.unit(40),
            60,
            54,
            3]])
    stage.delay(1)
    stage.unit(3).setAction(0, 1)
    stage.delay(3)
    stage.say('@dialogue:R_18.scene2.dialogue0004')
    stage.say('@dialogue:R_18.scene2.dialogue0005')
    stage.unit(3).move(56, 54, 1)
    stage.delay(5)
    stage.unit(40).setAction(5)
    stage.delay(5)
    stage.unit(40).setAction(0)
    stage.delay(5)
    stage.unit(3).move(52, 54, 3)
    stage.delay(3)
    stage.unit(3).setAction(5)
    stage.delay(10)
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_18.scene2.dialogue0006')
    stage.delay(3)
    stage.unit(3).setAction(0, 1)
    stage.say('@dialogue:R_18.scene2.dialogue0007')
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_18.scene2.dialogue0008')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_18.scene2.dialogue0009')
    stage.unit(3).setAction(3)
    stage.say('@dialogue:R_18.scene2.dialogue0010')
    stage.unit(3).setAction(0)
    stage.say('@dialogue:R_18.scene2.dialogue0011')
    stage.unit(40).move(98, 54, 1)
    stage.unit(40).hide()
    stage.say('@dialogue:R_18.scene2.dialogue0012')
    stage.delay(5)
    stage.say('@dialogue:R_18.scene2.dialogue0013')
    stage.showUnit(2, 90, 54, 3)
    stage.say('@dialogue:R_18.scene2.dialogue0014')
    stage.unit(2).move(60, 54, 3)
    stage.say('@dialogue:R_18.scene2.dialogue0015')
    stage.delay(5)
    stage.say('@dialogue:R_18.scene2.dialogue0016')
    stage.unit(2).setAction(0, 1)
    stage.say('@dialogue:R_18.scene2.dialogue0017')
    stage.unit(2).move(90, 54, 1)
    stage.say('@dialogue:R_18.scene2.dialogue0018')
    stage.unit(2).move(98, 54, 1)
    stage.unit(2).hide()
    stage.effectSound(202, 255)
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
            2,
            49,
            55,
            3],
        [
            21,
            52,
            40,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('그날 밤…….', 3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_18.scene3.dialogue0001')
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_18.scene3.dialogue0002')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_18.scene3.dialogue0003')
    stage.unit(2).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_18.scene3.dialogue0004')
    stage.info('면회 사절패란 주인이 손님을 만나고 싶지 않을 때\n닫힌 문 밖에 걸어 두는 패입니다.')
    stage.info('면회 사절패가 걸리면 무슨 일이 있더라도\n손님은 돌아가야 합니다. 그것이 예의이지요.')
    stage.say('@dialogue:R_18.scene3.dialogue0005')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(2).setAction(0, 3)
    stage.say('@dialogue:R_18.scene3.dialogue0006')
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('손님을 받지 않는다는 팻말을 건다\n팻말을 걸지 않는다', 0)
    if sel == 1:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(5)
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_18.scene3.dialogue0007')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_18.scene3.dialogue0008')
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_18.scene3.dialogue0009')
        vars[306] = 1
    elif sel == 2:
        stage.addAmbition(-10)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(5)
        stage.unit(0).setAction(0, 3)
        stage.say('@dialogue:R_18.scene3.dialogue0010')
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_18.scene3.dialogue0011')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_18.scene3.dialogue0012')
        stage.unit(0).setAction(3, 1)
        stage.say('@dialogue:R_18.scene3.dialogue0013')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_18.scene3.dialogue0014')
        stage.unit(21).setAction(0)
        stage.say('@dialogue:R_18.scene3.dialogue0015')
        vars[306] = 0
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    if vars[306]:
        stage.loadBg(2, 29)
        stage.draw()
        stage.effectSound(205, 0)
        stage.showUnit(3, 61, 21, 2)
        stage.info('다음 날…….', 3)
        stage.setStageName('허도  승상부 앞')
        stage.delay(3)
        stage.unit(3).move(61, 65, 2)
        stage.delay(3)
        stage.unit(3).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0001')
        stage.unit(3).move(61, 21, 0)
        stage.unit(3).hide()
        stage.effectSound(205, 255)
        stage.bgSound(16)
        stage.delay(3)
        stage.info('그 뒤 관우는 매일 승상부를 찾아갔으나\n승상부는 날마다 문이 굳게 닫힌 채\n손님을 받지 않는다는 팻말이 걸려 있었다.')
        stage.info('7일 뒤……', 3)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.showUnit(3, 61, 21, 2)
        stage.unit(3).move(61, 65, 2)
        stage.delay(3)
        stage.unit(3).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0002')
        stage.say('@dialogue:R_18.scene4.dialogue0003')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0004')
        stage.effectSound(202, 255)
        stage.clsUnit()
        stage.loadBg(2, 8)
        stage.showUnits([
            [
                0,
                34,
                55,
                1],
            [
                1,
                50,
                70],
            [
                15,
                56,
                70],
            [
                2,
                62,
                70],
            [
                13,
                68,
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
                2],
            [
                181,
                48,
                55,
                3,
                11]])
        stage.draw()
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.info('다음 날 아침…….', 3)
        stage.setStageName('허도  승상부')
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(1)
        stage.say('@dialogue:R_18.scene4.dialogue0005')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('@dialogue:R_18.scene4.dialogue0006')
        stage.unit(22).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0007')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0008')
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_18.scene4.dialogue0009')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_18.scene4.dialogue0010')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_18.scene4.dialogue0011')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('@dialogue:R_18.scene4.dialogue0012')
        stage.unit(0).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0013')
        stage.unit(0).setAction(0, 2)
        stage.delay(3)
        stage.say('@dialogue:R_18.scene4.dialogue0014')
        stage.unit(0).setAction(0, 1)
        stage.delay(3)
        stage.unit(0).setAction(3, 1)
        stage.say('@dialogue:R_18.scene4.dialogue0015')
        stage.unit(2).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0016')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_18.scene4.dialogue0017')
        stage.unit(2).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0018')
        stage.unit(2).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3, 1)
        stage.say('@dialogue:R_18.scene4.dialogue0019')
        stage.effectSound(205, 255)
        stage.bgSound(19)
        stage.delay(3)
        stage.clsUnit()
        stage.loadBg(2, 34)
        stage.showUnits([
            [
                154,
                4,
                59,
                1],
            [
                155,
                4,
                49,
                1]])
        stage.draw()
        stage.setStageName('허도 교외  패릉교')
        stage.unitsMove([
            [
                stage.unit(154),
                14,
                59,
                1],
            [
                stage.unit(155),
                14,
                49,
                1]])
        stage.showUnit(3, 4, 54, 1)
        stage.unitsMove([
            [
                stage.unit(154),
                80,
                59,
                1],
            [
                stage.unit(155),
                80,
                49,
                1],
            [
                stage.unit(3),
                70,
                54,
                1]])
        stage.info('이봐!', 3)
        stage.info('이봐! 관우!', 3)
        stage.unit(3).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0020')
        stage.unit(154).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0021')
        stage.unit(155).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0022')
        stage.unit(3).setAction(0, 1)
        stage.say('@dialogue:R_18.scene4.dialogue0023')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0024')
        stage.unit(3).setAction(0)
        stage.unitsMove([
            [
                stage.unit(154),
                95,
                59,
                1],
            [
                stage.unit(155),
                95,
                49,
                1]])
        stage.unit(154).hide()
        stage.unit(155).hide()
        stage.unit(3).setAction(0, 3)
        stage.showUnit(0, 4, 54, 1)
        stage.unit(0).move(14, 54, 1)
        stage.say('@dialogue:R_18.scene4.dialogue0025')
        stage.showUnits([
            [
                2,
                4,
                59,
                1],
            [
                15,
                4,
                49,
                1]])
        stage.unitsMove([
            [
                stage.unit(0),
                20,
                54,
                1],
            [
                stage.unit(2),
                10,
                59,
                1],
            [
                stage.unit(15),
                10,
                49,
                1]])
        stage.showUnits([
            [
                13,
                4,
                59,
                1],
            [
                14,
                4,
                49,
                1]])
        stage.unitsMove([
            [
                stage.unit(0),
                60,
                54,
                1],
            [
                stage.unit(2),
                50,
                59,
                1],
            [
                stage.unit(15),
                50,
                49,
                1],
            [
                stage.unit(13),
                44,
                59,
                1],
            [
                stage.unit(14),
                44,
                49,
                1]])
        stage.delay(3)
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_18.scene4.dialogue0026')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_18.scene4.dialogue0027')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0028')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_18.scene4.dialogue0029')
        stage.unit(0).setAction(0, 1)
        model.setFace(3)
        stage.say('@dialogue:R_18.scene4.dialogue0030')
        stage.unit(0).move(64, 54, 1)
        model.setFace(0)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.say('@dialogue:R_18.scene4.dialogue0031')
        model.setFace(3)
        stage.say('@dialogue:R_18.scene4.dialogue0032')
        stage.delay(3)
        stage.say('@dialogue:R_18.scene4.dialogue0033')
        stage.unit(3).move(67, 54, 3)
        stage.delay(5)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(3).move(70, 54, 1)
        stage.delay(3)
        stage.unit(3).setAction(0, 3)
        stage.delay(3)
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0034')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).move(60, 54, 3)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_18.scene4.dialogue0035')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0036')
        stage.unit(3).setAction(0)
        stage.unit(3).move(95, 54, 1)
        stage.unit(3).hide()
        stage.delay(8)
        model.setFace(0)
        stage.say('@dialogue:R_18.scene4.dialogue0037')
        stage.bgSound(-1)
        stage.clsUnit()
    if not vars[306]:
        stage.bgSound(19)
        stage.delay(3)
        stage.loadBg(2, 8)
        stage.showUnits([
            [
                0,
                34,
                55,
                1],
            [
                3,
                48,
                55,
                3,
                6],
            [
                154,
                54,
                52,
                3],
            [
                155,
                54,
                58,
                3],
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
                13,
                70,
                70]])
        stage.draw()
        stage.info('다음 날…….', 3)
        stage.setStageName('허도  승상부')
        stage.say('@dialogue:R_18.scene4.dialogue0038')
        stage.unit(3).setAction(0)
        stage.say('@dialogue:R_18.scene4.dialogue0039')
        stage.unit(2).move(62, 70, 3)
        stage.delay(1)
        stage.unit(2).move(62, 55, 0)
        stage.delay(1)
        stage.unit(2).setAction(0, 3)
        stage.unit(3).setAction(0, 1)
        stage.unit(154).setAction(0, 2)
        stage.unit(155).setAction(0, 0)
        stage.say('@dialogue:R_18.scene4.dialogue0040')
        stage.unit(3).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0041')
        model.setFace(3)
        stage.say('@dialogue:R_18.scene4.dialogue0042')
        stage.unit(3).setAction(0, 1)
        stage.delay(3)
        stage.unit(2).move(51, 55, 3)
        stage.delay(5)
        stage.unit(2).move(62, 55, 3)
        stage.delay(3)
        stage.unit(3).setAction(0, 3)
        stage.delay(3)
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_18.scene4.dialogue0043')
        stage.unit(154).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0044')
        stage.unit(155).setAction(0, 3)
        stage.say('@dialogue:R_18.scene4.dialogue0045')
        model.setFace(0)
        stage.bgSound(-1)
        stage.clsUnit()
    return False


def scene5():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(3, 291, 92)
    stage.showHead(0, 355, 92)
    stage.bottomTxt('관우와 두 부인은', False, False, False)
    stage.delay(5)
    stage.head(3).move(291, 172)
    stage.head(3).move(302, 179)
    stage.delay(5)
    stage.bottomTxt('양산으로 향하여', True, False, False)
    stage.delay(5)
    stage.showHead(32, 366, 179)
    stage.delay(5)
    stage.bottomTxt('마침내 유비, 장비와 다시 만났다.', True, False, True)
    stage.bottomTxt('이때,', False, True, False)
    stage.delay(8)
    stage.showHead(100, 560, 213)
    stage.delay(5)
    stage.bottomTxt('강동의 소패왕 손책이 급병으로 세상을 떠났다.', True, False, False)
    stage.delay(5)
    stage.head(100).hide()
    stage.delay(10)
    stage.showHead(76, 560, 213)
    stage.delay(5)
    stage.bottomTxt('아우 손권이 대업을 이어받았다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('손권은 사신을 보내 조조에게 승계 사실을 알렸다.', True, True, True)
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
            15,
            58,
            70],
        [
            2,
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
            2],
        [
            97,
            48,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_18.scene5.dialogue0001')
    stage.delay(3)
    stage.say('@dialogue:R_18.scene5.dialogue0002')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_18.scene5.dialogue0003')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_18.scene5.dialogue0004')
    stage.unit(97).setAction(0)
    stage.delay(3)
    stage.unit(97).move(95, 55, 1)
    stage.unit(97).hide()
    stage.say('@dialogue:R_18.scene5.dialogue0005')
    stage.unit(22).setAction(6)
    stage.say('@dialogue:R_18.scene5.dialogue0006')
    stage.unit(22).setAction(0)
    stage.delay(3)
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_18.scene5.dialogue0007')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('@dialogue:R_18.scene5.dialogue0008')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_18.scene5.dialogue0009')
    stage.unit(20).setAction(0)
    stage.say('@dialogue:R_18.scene5.dialogue0010')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_18.scene5.dialogue0011')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_18.scene5.dialogue0012')
    stage.effectSound(202, 255)
    stage.bgSound(0)
    stage.delay(3)
    stage.clsUnit()
    return False


def scene6():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            101,
            52,
            28,
            2],
        [
            106,
            38,
            48,
            1],
        [
            102,
            38,
            54,
            1],
        [
            110,
            67,
            48,
            3],
        [
            105,
            67,
            54,
            3]])
    stage.draw()
    stage.info('조조군의 움직임은 곧 원소의 진영에 전해졌다.')
    stage.setStageName('업성 원소 저택')
    stage.delay(3)
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_18.scene6.dialogue0001')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_18.scene6.dialogue0002')
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_18.scene6.dialogue0003')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(110).setAction(2)
    stage.say('@dialogue:R_18.scene6.dialogue0004')
    stage.unit(110).setAction(0)
    stage.say('@dialogue:R_18.scene6.dialogue0005')
    stage.unit(110).setAction(6)
    stage.say('@dialogue:R_18.scene6.dialogue0006')
    stage.unit(110).setAction(0)
    stage.say('@dialogue:R_18.scene6.dialogue0007')
    stage.unit(110).setAction(2)
    stage.say('@dialogue:R_18.scene6.dialogue0008')
    stage.unit(110).setAction(0)
    stage.say('@dialogue:R_18.scene6.dialogue0009')
    stage.unit(102).setAction(0, 0)
    stage.say('@dialogue:R_18.scene6.dialogue0010')
    stage.unit(102).setAction(0, 1)
    stage.delay(3)
    stage.unit(101).setAction(0, 0)
    stage.say('@dialogue:R_18.scene6.dialogue0011')
    stage.unit(101).setAction(0, 2)
    stage.say('@dialogue:R_18.scene6.dialogue0012')
    stage.unit(110).setAction(6)
    stage.say('@dialogue:R_18.scene6.dialogue0013')
    stage.unit(110).setAction(0)
    stage.unit(110).move(75, 48, 1)
    stage.delay(1)
    stage.unit(110).move(75, 71, 2)
    stage.unit(110).setAction(0)
    stage.say('@dialogue:R_18.scene6.dialogue0014')
    stage.unit(110).move(75, 90, 2)
    stage.unit(110).hide()
    stage.delay(3)
    stage.unit(105).setAction(6)
    stage.say('@dialogue:R_18.scene6.dialogue0015')
    stage.unit(105).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_18.scene6.dialogue0016')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_18.scene6.dialogue0017')
    stage.unit(105).setAction(6)
    stage.say('@dialogue:R_18.scene6.dialogue0018')
    stage.unit(105).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_18.scene6.dialogue0019')
    stage.unit(105).setAction(6)
    stage.say('@dialogue:R_18.scene6.dialogue0020')
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_18.scene6.dialogue0021')
    stage.showUnits([
        [
            181,
            67,
            90],
        [
            182,
            73,
            90]])
    stage.unitsMove([
        [
            stage.unit(181),
            67,
            57,
            0],
        [
            stage.unit(182),
            73,
            51,
            0]])
    stage.unit(105).setAction(0)
    stage.delay(1)
    stage.unit(182).move(67, 51, 3)
    stage.delay(1)
    stage.unit(182).setAction(0, 2)
    stage.delay(3)
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_18.scene6.dialogue0022')
    stage.say('@dialogue:R_18.scene6.dialogue0023')
    stage.delay(3)
    stage.unit(105).setAction(0, 1)
    stage.say('@dialogue:R_18.scene6.dialogue0024')
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(101, 349, 4)
    stage.delay(10)
    stage.bottomTxt('하북의 영웅 원소는', False, False, False)
    stage.delay(5)
    stage.showHead(0, 317, 127)
    stage.delay(10)
    stage.bottomTxt('숙적 조조와,', True, False, False)
    stage.delay(10)
    stage.head(0).move(358, 114)
    stage.delay(5)
    stage.head(101).move(358, 34)
    stage.delay(10)
    stage.bottomTxt('두 영웅은 관도에서 대치했고,', True, True, False)
    stage.delay(10)
    stage.bottomTxt('마침내 결전의 날이 왔다.', True, True, True)
    return False


def scene7():
    model.initLocalVar()
    stage.clsUnit()
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
            2,
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
    stage.setStageName('관도  조조군 본진')
    stage.say('@dialogue:R_18.scene7.dialogue0001')
    stage.unit(20).setAction(0, 2)
    stage.say('@dialogue:R_18.scene7.dialogue0002')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_18.scene7.dialogue0003')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('@dialogue:R_18.scene7.dialogue0004')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 14, [
        15])
    gvars[4076] = 0
    gvars[4057] = 18
    stage.setBattlePos([
        [
            15,
            30],
        [
            11,
            27],
        [
            18,
            27],
        [
            12,
            28],
        [
            17,
            28],
        [
            13,
            28],
        [
            16,
            28],
        [
            12,
            29],
        [
            17,
            29],
        [
            13,
            29],
        [
            16,
            29],
        [
            14,
            30],
        [
            15,
            29],
        [
            14,
            29],
        [
            0,
            0]])
    return False


def scene8():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_18.scene8.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_18.scene8.dialogue0002')
        stage.unit(22).setAction(0, 2)
        stage.say('@dialogue:R_18.scene8.dialogue0003')
        stage.unit(22).setAction(0, 3)
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_18.scene8.dialogue0004')
    if stage.unitClickTest(22):
        stage.say('@dialogue:R_18.scene8.dialogue0005')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_18.scene8.dialogue0006')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_18.scene8.dialogue0007')
    if stage.unitClickTest(2):
        stage.say('@dialogue:R_18.scene8.dialogue0008')
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
