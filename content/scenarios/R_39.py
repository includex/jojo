# Source Generated with Decompyle++
# File: R_39.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    if vars[738]:
        model.unitJoin(9, 255, 0)
        stage.bgSound(13)
        stage.delay(3)
        stage.loadBg(2, 53)
        stage.showUnits([
            [
                0,
                54,
                80],
            [
                14,
                54,
                64,
                2,
                6],
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
        stage.setEventName('충의의 방덕')
        stage.setStageName('번성  조조군 본진')
        stage.say('@dialogue:R_39.scene0.dialogue0001')
        stage.unit(14).setAction(0)
        stage.say('@dialogue:R_39.scene0.dialogue0002')
        stage.unit(14).setAction(6)
        stage.say('@dialogue:R_39.scene0.dialogue0003')
        stage.unit(14).setAction(0)
        stage.delay(3)
        stage.unit(14).move(54, 76, 2)
        stage.delay(3)
        stage.unit(14).move(54, 64, 2)
        stage.say('@dialogue:R_39.scene0.dialogue0004')
        stage.getItem(41, 0)
        stage.unit(14).setAction(6)
        stage.say('@dialogue:R_39.scene0.dialogue0005')
        stage.unit(14).setAction(0)
        stage.say('@dialogue:R_39.scene0.dialogue0006')
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_39.scene0.dialogue0007')
        stage.bgSound(-1)
        stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 294, 124)
    stage.showHead(3, 294, 204)
    stage.bottomTxt('조조군에게 패한 관우는', False, False, False)
    stage.delay(5)
    stage.head(3).move(254, 210)
    stage.delay(5)
    stage.bottomTxt('맥성에서 패주한 뒤,', True, False, False)
    stage.delay(10)
    stage.showHead(80, 368, 236)
    stage.delay(5)
    stage.bottomTxt('동오의 여몽은', True, True, False)
    stage.delay(5)
    stage.head(80).move(318, 210)
    stage.delay(5)
    stage.bottomTxt('끈질긴 추격을 이어 갔고', True, False, False)
    stage.delay(5)
    stage.head(3).hide()
    stage.delay(5)
    stage.bottomTxt('마침내 관우를 죽였다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('촉의 세력을 약화시키는 데 성공한 조조는', True, True, False)
    stage.delay(8)
    stage.head(0).move(331, 96)
    stage.delay(5)
    stage.bottomTxt('이에 허도로 돌아갔다.', True, False, True)
    stage.bottomTxt('그로부터 얼마 뒤,', False, False, False)
    stage.delay(5)
    stage.head(80).hide()
    stage.delay(5)
    stage.bottomTxt('여몽은 알 수 없이 발광하다 죽었다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('동오 백성들은 관우의 혼령이 재앙을 내린다 여겨 한동안 소란에 휩싸였다.', True, True, False)
    stage.delay(8)
    stage.showHead(76, 501, 208)
    stage.delay(5)
    stage.bottomTxt('손권은 인심을 얻고자,', True, True, False)
    stage.delay(5)
    stage.bottomTxt('유비의 보복을 피해', True, False, False)
    stage.delay(5)
    stage.bottomTxt('관우의 수급을 조조에게 보냈다.', True, False, True)
    stage.bgSound(13)
    stage.delay(3)
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
            2,
            56,
            70],
        [
            8,
            62,
            70],
        [
            15,
            68,
            70],
        [
            20,
            50,
            41,
            2],
        [
            19,
            56,
            41,
            2],
        [
            23,
            62,
            41,
            2],
        [
            24,
            68,
            41,
            2],
        [
            21,
            74,
            41,
            2],
        [
            3,
            47,
            55,
            1]])
    stage.unit(201).move(48, 55, 1)
    stage.draw()
    stage.setEventName('촉과 오의 공방')
    stage.setStageName('허도  승상부')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_39.scene1.dialogue0001')
    stage.unit(20).setAction(0)
    stage.delay(3)
    model.setFace(1)
    stage.unit(0).setAction(9)
    stage.say('@dialogue:R_39.scene1.dialogue0002')
    stage.unit(2).setAction(9)
    stage.say('@dialogue:R_39.scene1.dialogue0003')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_39.scene1.dialogue0004')
    model.setFace(0)
    stage.say('@dialogue:R_39.scene1.dialogue0005')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_39.scene1.dialogue0006')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_39.scene1.dialogue0007')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_39.scene1.dialogue0008')
    stage.unit(24).setAction(0)
    stage.say('@dialogue:R_39.scene1.dialogue0009')
    stage.unit(23).setAction(6)
    stage.say('@dialogue:R_39.scene1.dialogue0010')
    stage.unit(23).setAction(0)
    stage.say('@dialogue:R_39.scene1.dialogue0011')
    stage.say('@dialogue:R_39.scene1.dialogue0012')
    stage.unit(2).setAction(0)
    stage.say('@dialogue:R_39.scene1.dialogue0013')
    stage.unit(2).setAction(6)
    stage.say('@dialogue:R_39.scene1.dialogue0014')
    stage.unit(2).setAction(0)
    stage.say('@dialogue:R_39.scene1.dialogue0015')
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(21),
            98,
            41,
            1],
        [
            stage.unit(24),
            98,
            41,
            1],
        [
            stage.unit(23),
            98,
            41,
            1],
        [
            stage.unit(19),
            98,
            41,
            1],
        [
            stage.unit(20),
            98,
            41,
            1],
        [
            stage.unit(15),
            98,
            70,
            1],
        [
            stage.unit(8),
            98,
            70,
            1],
        [
            stage.unit(2),
            98,
            70,
            1],
        [
            stage.unit(1),
            98,
            70,
            1]])
    stage.unit(20).hide()
    stage.unit(19).hide()
    stage.unit(23).hide()
    stage.unit(24).hide()
    stage.unit(21).hide()
    stage.unit(1).hide()
    stage.unit(2).hide()
    stage.unit(8).hide()
    stage.unit(15).hide()
    stage.delay(10)
    stage.say('@dialogue:R_39.scene1.dialogue0016')
    stage.unit(0).setAction(9)
    stage.say('@dialogue:R_39.scene1.dialogue0017')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
    if model.ambition() >= 85:
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
                8,
                56,
                70],
            [
                15,
                62,
                70],
            [
                2,
                68,
                70],
            [
                20,
                50,
                41,
                2],
            [
                19,
                56,
                41,
                2],
            [
                23,
                62,
                41,
                2],
            [
                24,
                68,
                41,
                2],
            [
                21,
                74,
                41,
                2]])
        stage.draw()
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.info('조조를 비롯한 위의 장수들은\n관우의 죽음을 애도하며 상복을 입었다.\n그리고 상기가 거의 끝나 갈 무렵…….')
        stage.setStageName('허도  승상부')
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_39.scene2.dialogue0001')
        stage.say('@dialogue:R_39.scene2.dialogue0002')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_39.scene2.dialogue0003')
        stage.unit(21).setAction(6)
        stage.say('@dialogue:R_39.scene2.dialogue0004')
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(23).setAction(6)
        stage.say('@dialogue:R_39.scene2.dialogue0005')
        stage.unit(23).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(0, 3)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_39.scene2.dialogue0006')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(8).setAction(0, 3)
        stage.say('@dialogue:R_39.scene2.dialogue0007')
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_39.scene2.dialogue0008')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_39.scene2.dialogue0009')
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_39.scene2.dialogue0010')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_39.scene2.dialogue0011')
        stage.effectSound(202, 255)
        stage.bgSound(11)
        stage.delay(3)
        sel = stage.choice('황제에 오른다\n제위에 오르지 않는다', 0)
        if sel == 1:
            stage.addAmbition(20)
            vars[313] = 1
            stage.bgSound(-1)
            stage.effectSound(202, 0)
            stage.delay(3)
            stage.unit(0).setAction(3)
            stage.say('@dialogue:R_39.scene2.dialogue0012')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(19).setAction(6)
            stage.say('@dialogue:R_39.scene2.dialogue0013')
            stage.unit(19).setAction(0)
            stage.say('@dialogue:R_39.scene2.dialogue0014')
            stage.unit(0).setAction(17)
            stage.say('@dialogue:R_39.scene2.dialogue0015')
        elif sel == 2:
            stage.addAmbition(-20)
            vars[313] = 0
            stage.bgSound(-1)
            stage.effectSound(202, 0)
            stage.delay(3)
            stage.say('@dialogue:R_39.scene2.dialogue0016')
            stage.unit(19).setAction(6)
            stage.say('@dialogue:R_39.scene2.dialogue0017')
            stage.unit(19).setAction(0)
            stage.say('@dialogue:R_39.scene2.dialogue0018')
            stage.unit(0).setAction(0, 3)
            stage.say('@dialogue:R_39.scene2.dialogue0019')
            stage.unit(20).setAction(6)
            stage.say('@dialogue:R_39.scene2.dialogue0020')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(0, 1)
            stage.say('@dialogue:R_39.scene2.dialogue0021')
            stage.unit(19).setAction(6)
            stage.say('@dialogue:R_39.scene2.dialogue0022')
            stage.unit(19).setAction(0)
            stage.say('@dialogue:R_39.scene2.dialogue0023')
            stage.unit(0).setAction(3)
            stage.say('@dialogue:R_39.scene2.dialogue0024')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:R_39.scene2.dialogue0025')
            stage.unit(1).setAction(2)
            stage.say('@dialogue:R_39.scene2.dialogue0026')
            stage.unit(1).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(3)
            model.setFace(3)
            stage.say('@dialogue:R_39.scene2.dialogue0027')
            model.setFace(0)
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(0).move(95, 55, 1)
            stage.unit(0).hide()
            stage.unit(1).setAction(0, 0)
            stage.unit(8).setAction(0, 0)
            stage.delay(3)
            stage.unit(21).setAction(0, 1)
            stage.say('@dialogue:R_39.scene2.dialogue0028')
            stage.unit(20).setAction(0, 1)
            stage.say('@dialogue:R_39.scene2.dialogue0029')
        stage.effectSound(202, 255)
        stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            32,
            32,
            54,
            1],
        [
            35,
            52,
            41,
            2],
        [
            34,
            58,
            70],
        [
            33,
            52,
            70]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('그 무렵 촉에서는……', 3)
    stage.setStageName('성도  유비의 관저')
    stage.delay(3)
    stage.unit(33).setAction(0, 3)
    stage.delay(3)
    stage.unit(33).setAction(3)
    stage.say('@dialogue:R_39.scene3.dialogue0001')
    stage.unit(33).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(17)
    stage.say('@dialogue:R_39.scene3.dialogue0002')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(33).setAction(3)
    stage.say('@dialogue:R_39.scene3.dialogue0003')
    stage.unit(33).setAction(0)
    stage.delay(3)
    stage.unit(33).move(52, 74, 2)
    stage.unit(33).move(98, 74, 1)
    stage.unit(33).hide()
    stage.unit(34).setAction(0, 3)
    stage.delay(3)
    stage.unit(34).setAction(6)
    stage.say('@dialogue:R_39.scene3.dialogue0004')
    stage.unit(34).setAction(0)
    stage.say('@dialogue:R_39.scene3.dialogue0005')
    stage.unit(34).setAction(6)
    stage.say('@dialogue:R_39.scene3.dialogue0006')
    stage.unit(34).setAction(0)
    stage.say('@dialogue:R_39.scene3.dialogue0007')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_39.scene3.dialogue0008')
    stage.say('@dialogue:R_39.scene3.dialogue0009')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_39.scene3.dialogue0010')
    stage.say('@dialogue:R_39.scene3.dialogue0011')
    stage.unit(32).setAction(2)
    stage.say('@dialogue:R_39.scene3.dialogue0012')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_39.scene3.dialogue0013')
    stage.unit(35).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(0)
    stage.say('@dialogue:R_39.scene3.dialogue0014')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_39.scene3.dialogue0015')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 333, 96)
    stage.showHead(76, 499, 203)
    stage.showHead(32, 5, 172)
    stage.bottomTxt('유비', False, False, False)
    stage.delay(5)
    stage.showHead(33, 69, 172)
    stage.delay(5)
    stage.bottomTxt('장비와 함께', True, False, False)
    stage.delay(5)
    stage.bottomTxt('관우의 원수를 갚고자 동오 정벌을 준비했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('하지만 출발 전날 밤,', True, True, False)
    stage.delay(8)
    stage.bottomTxt('장비', True, False, False)
    stage.delay(6)
    stage.showHead(299, 133, 212)
    stage.delay(5)
    stage.bottomTxt('잠든 사이 부하에게 목을 베였고', True, False, False)
    stage.delay(8)
    stage.head(33).hide()
    stage.delay(5)
    stage.bottomTxt('떠나기도 전에 죽었군.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('배신한 부하들은 장비의 수급을 예물로 삼아', True, True, False)
    stage.delay(5)
    stage.head(299).move(435, 203)
    stage.delay(5)
    stage.head(299).hide()
    stage.delay(5)
    stage.bottomTxt('동오에 의탁했다.', True, False, True)
    stage.bottomTxt('관우와 장비의 원수를 갚고자', False, True, False)
    stage.delay(8)
    stage.head(32).move(135, 201)
    stage.bottomTxt('유비는 직접 대군을 이끌고 동오로 향했고,', True, False, False)
    stage.head(32).move(249, 211)
    stage.delay(5)
    stage.bottomTxt('머지않아 형주로 다가갔다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('손권', True, True, False)
    stage.delay(5)
    stage.showHead(81, 435, 203)
    stage.delay(5)
    stage.bottomTxt('육손을 대도독으로 임명하고', True, False, False)
    stage.delay(5)
    stage.head(81).move(313, 211)
    stage.delay(5)
    stage.bottomTxt('촉군을 맞아 싸웠다.', True, False, True)
    stage.bottomTxt('육손은 이릉에서 화공으로 촉군을 크게 무찌르고', False, True, False)
    stage.delay(8)
    stage.head(32).move(161, 215)
    stage.head(81).move(435, 203)
    stage.head(81).hide()
    stage.delay(5)
    stage.bottomTxt('패주하는 촉군을 백제성으로 물러나게 했다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('그 뒤 유비의 병세는 나빠지기 시작했고,', True, True, False)
    stage.delay(8)
    stage.bottomTxt('유비는 끝내 실의에 잠긴 채', True, False, False)
    stage.delay(5)
    stage.head(32).hide()
    stage.delay(5)
    stage.bottomTxt('세상을 떠났다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('촉에서는 유비의 아들이', True, True, False)
    stage.delay(5)
    stage.showHead(67, 26, 173)
    stage.delay(5)
    stage.bottomTxt('유선이 대업을 이어받았고', True, False, False)
    stage.delay(8)
    stage.showHead(35, 90, 173)
    stage.delay(5)
    stage.bottomTxt('제갈량', True, False, False)
    stage.delay(5)
    stage.bottomTxt('후주 유선을 온 힘을 다해 보좌했다.', True, False, True)
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
            50,
            70],
        [
            8,
            56,
            70],
        [
            15,
            62,
            70],
        [
            2,
            68,
            70],
        [
            20,
            50,
            41,
            2],
        [
            19,
            56,
            41,
            2],
        [
            23,
            62,
            41,
            2],
        [
            24,
            68,
            41,
            2],
        [
            21,
            74,
            41,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('@dialogue:R_39.scene4.dialogue0001')
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_39.scene4.dialogue0002')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_39.scene4.dialogue0003')
    stage.unit(1).setAction(0, 3)
    stage.say('@dialogue:R_39.scene4.dialogue0004')
    stage.unit(21).setAction(6)
    stage.say('@dialogue:R_39.scene4.dialogue0005')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_39.scene4.dialogue0006')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_39.scene4.dialogue0007')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_39.scene4.dialogue0008')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            67,
            32,
            54,
            1],
        [
            35,
            50,
            41,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.info('한편 촉에서는……', 3)
    stage.setStageName('성도  유선의 저택')
    stage.say('@dialogue:R_39.scene5.dialogue0001')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_39.scene5.dialogue0002')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_39.scene5.dialogue0003')
    stage.unit(35).setAction(11)
    stage.say('@dialogue:R_39.scene5.dialogue0004')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_39.scene5.dialogue0005')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_39.scene5.dialogue0006')
    stage.unit(35).setAction(0)
    stage.showUnit(183, 90, 54, 3)
    stage.say('@dialogue:R_39.scene5.dialogue0007')
    stage.say('@dialogue:R_39.scene5.dialogue0008')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_39.scene5.dialogue0009')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_39.scene5.dialogue0010')
    stage.unit(35).setAction(6)
    stage.say('@dialogue:R_39.scene5.dialogue0011')
    stage.unit(35).setAction(0)
    stage.say('@dialogue:R_39.scene5.dialogue0012')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene6():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 334, 98)
    stage.showHead(67, 23, 170)
    stage.bottomTxt('조조군', False, False, False)
    stage.delay(5)
    stage.head(0).move(143, 59)
    stage.delay(5)
    stage.bottomTxt('양평관으로 진격했고,', True, False, False)
    stage.delay(8)
    stage.head(67).hide()
    stage.delay(5)
    stage.showHead(35, 23, 170)
    stage.delay(5)
    stage.bottomTxt('제갈량은 촉군을 이끌고', True, True, False)
    stage.delay(5)
    stage.head(35).move(79, 87)
    stage.delay(5)
    stage.bottomTxt('맞아 싸운다.', True, False, True)
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
            4,
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
            24,
            64,
            52,
            3]])
    stage.draw()
    stage.setStageName('양평관 앞  조조군 본진')
    stage.say('@dialogue:R_39.scene6.dialogue0001')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_39.scene6.dialogue0002')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('@dialogue:R_39.scene6.dialogue0003')
    stage.unit(19).setAction(0)
    stage.say('@dialogue:R_39.scene6.dialogue0004')
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_39.scene6.dialogue0005')
    stage.unit(0).setAction(0)
    stage.unit(19).setAction(0, 3)
    stage.unit(24).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19,
        15])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 39
    stage.setBattlePos([
        [
            1,
            4],
        [
            1,
            5],
        [
            2,
            5],
        [
            2,
            4],
        [
            2,
            6],
        [
            3,
            5],
        [
            1,
            6],
        [
            3,
            4],
        [
            0,
            7],
        [
            4,
            3],
        [
            0,
            6],
        [
            3,
            3],
        [
            0,
            5],
        [
            2,
            3],
        [
            1,
            3]])
    return False


def scene7():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_39.scene7.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_39.scene7.dialogue0002')
    if stage.unitClickTest(15):
        stage.say('@dialogue:R_39.scene7.dialogue0003')
    if stage.unitClickTest(19):
        stage.say('@dialogue:R_39.scene7.dialogue0004')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_39.scene7.dialogue0005')
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_39.scene7.dialogue0006')
    if stage.unitClickTest(4):
        stage.say('@dialogue:R_39.scene7.dialogue0007')
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
