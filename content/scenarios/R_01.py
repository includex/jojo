# Source Generated with Decompyle++
# File: R_01.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(-1)
    stage.video('전투 완료 영상을 재생하시겠습니까?', 3)
    if stage.video() == 1:
        stage.video(1)
    stage.bgSound(16)
    stage.sectionName(1, '패왕의 탄생')
    stage.loadBg(0, 16)
    stage.draw()
    stage.video('지금으로부터 약 １８００년 전,\n후한 말 중국 대륙.')
    stage.video('당시 황제 영제는 향락에 빠져 있었고 조정은 부패했다.\n굶주리는 백성들의 한실에 대한 불만은\n마침내 극에 달했다.')
    stage.video('그 무렵…….')
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(147, 320, 180)
    stage.showHead(146, 440, 130)
    stage.delay(5)
    stage.bottomTxt('태평도의 교주 장각이 이끄는 황건의 난은,', False, True, False)
    stage.delay(5)
    stage.bottomTxt('이에 중국 각지에서 폭발했다.', True, False, False)
    stage.delay(10)
    stage.showHead(0, 260, 60)
    stage.delay(5)
    stage.bottomTxt('조조가 이끄는 관군은', True, True, False)
    stage.head(0).move(320, 60)
    stage.delay(5)
    stage.head(147).hide()
    stage.head(146).hide()
    stage.delay(5)
    stage.bottomTxt('고전 끝에 반란 세력을 진압했으나', True, False, False)
    stage.delay(5)
    stage.bottomTxt('하지만 한실이 쇠락하는 흐름을 막을 힘은 이미 없었고,', True, True, True)
    stage.bottomTxt('이것이 바로 난세의 시작이었다.', False, True, True)
    stage.loadBg(0, 16)
    stage.draw()
    stage.video('황건의 난이 가라앉은 지 얼마 지나지 않아\n영제가 붕어했다. 구심점이 사라진 틈을 타\n조정에서는 격렬한 권력 다툼이 시작되었다.')
    stage.video('결국 동탁이 힘을 앞세워 조정의 혼란을 틈타\n낙양의 실권을 장악했다.')
    stage.bgSound(-1)
    return False


def scene1():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            192,
            52,
            41,
            2],
        [
            193,
            58,
            41,
            2],
        [
            131,
            64,
            41,
            2],
        [
            128,
            70,
            41,
            2],
        [
            101,
            52,
            70],
        [
            0,
            58,
            70],
        [
            133,
            64,
            70],
        [
            134,
            70,
            70]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.setEventName('조조가 군사를 일으키다')
    stage.setStageName('낙양@승상부')
    stage.delay(3)
    stage.unit(101).setAction(-1, 1)
    stage.say('@dialogue:R_01.scene1.dialogue0001')
    stage.unit(0).setAction(-1, 3)
    stage.say('@dialogue:R_01.scene1.dialogue0002')
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_01.scene1.dialogue0003')
    stage.unit(0).setAction(-1, 0)
    stage.say('@dialogue:R_01.scene1.dialogue0004')
    stage.unit(101).setAction(0, 0)
    stage.showUnit(127, 90, 55, 3)
    stage.unit(127).move(84, 55, 3)
    stage.showUnits([
        [
            119,
            90,
            58,
            3],
        [
            132,
            90,
            52,
            3]])
    stage.unitsMove([
        [
            stage.unit(127),
            41,
            55,
            3],
        [
            stage.unit(119),
            47,
            58,
            3],
        [
            stage.unit(132),
            47,
            52,
            3]])
    stage.unit(127).setAction(0)
    stage.unit(119).setAction(0)
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(127).move(41, 58, 2)
    stage.unit(127).move(33, 58, 3)
    stage.unit(127).move(33, 55, 1)
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(119),
            41,
            58,
            3],
        [
            stage.unit(132),
            41,
            52,
            3]])
    stage.unit(119).setAction(0)
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(132).move(41, 46, 0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(119),
            33,
            58,
            3],
        [
            stage.unit(132),
            33,
            46,
            3]])
    stage.unitsMove([
        [
            stage.unit(119),
            33,
            61,
            1],
        [
            stage.unit(132),
            33,
            47,
            1]])
    stage.unit(119).setAction(0)
    stage.unit(132).setAction(0)
    stage.delay(5)
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_01.scene1.dialogue0005')
    stage.unit(127).setAction(0)
    stage.say('@dialogue:R_01.scene1.dialogue0006')
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_01.scene1.dialogue0007')
    stage.unit(192).setAction(0)
    stage.say('@dialogue:R_01.scene1.dialogue0008')
    stage.unit(127).setAction(17)
    stage.say('@dialogue:R_01.scene1.dialogue0009')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(3)
    stage.say('@dialogue:R_01.scene1.dialogue0010')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('@dialogue:R_01.scene1.dialogue0011')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_01.scene1.dialogue0012')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_01.scene1.dialogue0013')
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_01.scene1.dialogue0014')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('@dialogue:R_01.scene1.dialogue0015')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(6)
    stage.say('@dialogue:R_01.scene1.dialogue0016')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_01.scene1.dialogue0017')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_01.scene1.dialogue0018')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 1)
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_01.scene1.dialogue0019')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_01.scene1.dialogue0020')
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_01.scene1.dialogue0021')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 0)
    stage.unit(0).setAction(-1, 0)
    stage.delay(5)
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_01.scene1.dialogue0022')
    stage.unit(127).setAction(0)
    stage.say('@dialogue:R_01.scene1.dialogue0023')
    stage.unit(127).setAction(0, 0)
    stage.say('@dialogue:R_01.scene1.dialogue0024')
    stage.unit(132).setAction(0, 2)
    stage.delay(3)
    stage.unit(132).setAction(6)
    stage.say('@dialogue:R_01.scene1.dialogue0025')
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_01.scene1.dialogue0026')
    stage.unit(127).setAction(0)
    stage.say('@dialogue:R_01.scene1.dialogue0027')
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(0, 1)
    stage.delay(3)
    stage.unit(127).setAction(3)
    stage.say('@dialogue:R_01.scene1.dialogue0028')
    stage.unit(127).setAction(0)
    stage.say('@dialogue:R_01.scene1.dialogue0029')
    stage.delay(3)
    stage.unit(101).setAction(0, 1)
    stage.say('@dialogue:R_01.scene1.dialogue0030')
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_01.scene1.dialogue0031')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_01.scene1.dialogue0032')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_01.scene1.dialogue0033')
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_01.scene1.dialogue0034')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_01.scene1.dialogue0035')
    stage.unit(101).setAction(2, 1)
    stage.say('@dialogue:R_01.scene1.dialogue0036')
    stage.unit(0).setAction(0, 3)
    stage.say('@dialogue:R_01.scene1.dialogue0037')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_01.scene1.dialogue0038')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_01.scene1.dialogue0039')
    stage.clsUnit()
    stage.effectSound(205, 255)
    return False


def scene2():
    stage.bgSound(16)
    stage.loadBg(0, 16)
    stage.draw()
    stage.info('천자의 명을 받은 대신이 된 동탁은\n권력을 독점하고 폭정을 일삼았다.\n도성 낙양에는 백성의 통곡과 탄식이 맴돌았다.')
    stage.bgSound(-1)
    stage.loadBg(2, 39)
    stage.showUnit(153, 68, 57, 0, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('낙양@왕윤의 저택')
    stage.say('@dialogue:R_01.scene2.dialogue0001')
    stage.unit(153).setAction(9)
    stage.say('@dialogue:R_01.scene2.dialogue0002')
    stage.unit(153).setAction(1)
    stage.say('@dialogue:R_01.scene2.dialogue0003')
    stage.showUnit(196, 55, 29, 2)
    stage.say('@dialogue:R_01.scene2.dialogue0004')
    stage.unit(153).setAction(0)
    stage.say('@dialogue:R_01.scene2.dialogue0005')
    stage.unit(196).move(55, 49, 2)
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(153).move(55, 57, 0)
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0006')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0007')
    stage.unit(196).setAction(0)
    stage.say('@dialogue:R_01.scene2.dialogue0008')
    stage.unit(153).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0009')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(0, 0)
    stage.say('@dialogue:R_01.scene2.dialogue0010')
    stage.showUnit(0, 55, 29, 2)
    stage.say('@dialogue:R_01.scene2.dialogue0011')
    stage.unitsMove([
        [
            stage.unit(196),
            61,
            49,
            2],
        [
            stage.unit(0),
            55,
            49,
            2]])
    stage.delay(5)
    stage.unit(196).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0012')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0013')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0014')
    stage.unit(153).setAction(0)
    stage.say('@dialogue:R_01.scene2.dialogue0015')
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0016')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(153),
            68,
            57,
            0],
        [
            stage.unit(196),
            74,
            49,
            2],
        [
            stage.unit(0),
            68,
            49,
            2]])
    stage.unit(196).setAction(1)
    stage.unit(0).setAction(1)
    stage.unit(153).setAction(1)
    stage.delay(5)
    stage.info('잠시 뒤…….', 3)
    stage.delay(5)
    stage.say('@dialogue:R_01.scene2.dialogue0017')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_01.scene2.dialogue0018')
    stage.unit(153).setAction(0)
    stage.say('@dialogue:R_01.scene2.dialogue0019')
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0020')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_01.scene2.dialogue0021')
    stage.unit(153).setAction(1, 0)
    stage.say('@dialogue:R_01.scene2.dialogue0022')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_01.scene2.dialogue0023')
    stage.unit(0).setAction(0)
    stage.delay(10)
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_01.scene2.dialogue0024')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_01.scene2.dialogue0025')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(0, 3)
    stage.say('@dialogue:R_01.scene2.dialogue0026')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_01.scene2.dialogue0027')
    stage.unit(0).setAction(0, 2)
    stage.unit(196).setAction(0, 2)
    stage.delay(5)
    stage.unit(153).setAction(0, 2)
    stage.say('@dialogue:R_01.scene2.dialogue0028')
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_01.scene2.dialogue0029')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_01.scene2.dialogue0030')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0031')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0032')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0033')
    stage.say('@dialogue:R_01.scene2.dialogue0034')
    stage.unit(153).setAction(0)
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('기꺼이 맡겠다\n맡기 어렵다', -1)
    if sel == 1:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.say('@dialogue:R_01.scene2.dialogue0035')
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0036')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('@dialogue:R_01.scene2.dialogue0037')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0038')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0039')
        stage.unit(153).setAction(0)
        stage.say('@dialogue:R_01.scene2.dialogue0040')
    elif sel == 2:
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0041')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('@dialogue:R_01.scene2.dialogue0042')
        stage.unit(153).setAction(0)
        stage.delay(5)
        stage.unit(196).setAction(0, 3)
        stage.say('@dialogue:R_01.scene2.dialogue0043')
        stage.delay(5)
        stage.say('@dialogue:R_01.scene2.dialogue0044')
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0045')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('@dialogue:R_01.scene2.dialogue0046')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0047')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0048')
        stage.unit(153).setAction(0)
        stage.say('@dialogue:R_01.scene2.dialogue0049')
    stage.say('@dialogue:R_01.scene2.dialogue0050')
    stage.showUnit(17, 55, 29, 2)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0051')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.unit(196).setAction(0, 0)
    stage.delay(3)
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_01.scene2.dialogue0052')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0053')
    stage.unit(17).setAction(0)
    stage.delay(5)
    stage.unit(17).hide()
    stage.delay(5)
    stage.unit(0).setAction(0, 2)
    stage.unit(196).setAction(0, 2)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0054')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0055')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0056')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_01.scene2.dialogue0057')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0058')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.showUnit(17, 55, 29, 2)
    stage.say('@dialogue:R_01.scene2.dialogue0059')
    stage.unit(17).move(55, 57, 1)
    stage.delay(3)
    stage.unit(153).setAction(0, 3)
    stage.say('@dialogue:R_01.scene2.dialogue0060')
    stage.unit(17).move(64, 57, 1)
    stage.delay(3)
    stage.unit(17).move(55, 57, 1)
    stage.say('@dialogue:R_01.scene2.dialogue0061')
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_01.scene2.dialogue0062')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0063')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(0, 3)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0064')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0065')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('@dialogue:R_01.scene2.dialogue0066')
    stage.unit(153).setAction(0)
    stage.delay(5)
    model.setFace(1)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_01.scene2.dialogue0067')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('참으로 감탄스럽다\n별다를 것 없다', -1)
    if sel == 1:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.delay(3)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0068')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(3)
        stage.say('@dialogue:R_01.scene2.dialogue0069')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('@dialogue:R_01.scene2.dialogue0070')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(1)
        stage.say('@dialogue:R_01.scene2.dialogue0071')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(3)
        stage.say('@dialogue:R_01.scene2.dialogue0072')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0073')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(17),
                55,
                63,
                0],
            [
                stage.unit(153),
                55,
                57,
                0]])
        stage.unit(153).move(55, 49, 1)
        stage.unit(153).setAction(0)
        stage.unit(0).setAction(0, 3)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0074')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).move(64, 49, 1)
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.delay(3)
        stage.unit(153).move(55, 49, 1)
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0075')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0076')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(153),
                55,
                55,
                0],
            [
                stage.unit(0),
                55,
                49,
                2]])
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0077')
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).move(55, 29, 0)
        stage.unit(0).hide()
        stage.delay(5)
        stage.unit(153).setAction(0, 1)
        stage.say('@dialogue:R_01.scene2.dialogue0078')
        stage.unit(196).setAction(0, 2)
        stage.say('@dialogue:R_01.scene2.dialogue0079')
        stage.unit(17).setAction(0, 1)
        stage.say('@dialogue:R_01.scene2.dialogue0080')
        vars[302] = 1
    elif sel == 2:
        stage.addAmbition(-5)
        stage.delay(3)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0081')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0082')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(0, 3)
        stage.say('@dialogue:R_01.scene2.dialogue0083')
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0084')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(17).move(55, 29, 0)
        stage.unit(17).hide()
        stage.delay(5)
        stage.unit(153).move(55, 57, 3)
        stage.unit(153).setAction(0)
        stage.unit(153).move(55, 49, 1)
        stage.unit(153).setAction(0)
        stage.unit(0).setAction(0, 3)
        stage.unit(196).setAction(0, 3)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0085')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).move(64, 49, 1)
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.delay(3)
        stage.unit(153).move(55, 49, 1)
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0086')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0087')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(153),
                55,
                55,
                0],
            [
                stage.unit(0),
                55,
                49,
                2]])
        stage.unit(153).setAction(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_01.scene2.dialogue0088')
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).move(55, 29, 0)
        stage.unit(0).hide()
        stage.delay(5)
        stage.unit(153).setAction(0, 1)
        stage.say('@dialogue:R_01.scene2.dialogue0089')
        stage.unit(196).setAction(0, 2)
        stage.say('@dialogue:R_01.scene2.dialogue0090')
        vars[303] = 1
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 282, 95)
    stage.bottomTxt('조조는 왕윤의 부탁을 받아들여', False, False, False)
    stage.delay(5)
    stage.head(0).move(378, 118)
    stage.delay(5)
    stage.bottomTxt('낙양을 떠나 고향 진류로 돌아갔다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('동탁 토벌 동맹을 결성하는 격문을 각지에 보냈다.', True, True, True)
    stage.bottomTxt('그 격문에 호응한 이는', False, True, False)
    stage.delay(5)
    stage.showHead(101, 408, 8)
    stage.delay(5)
    stage.bottomTxt('발해태수 원소,', True, False, False)
    stage.delay(5)
    stage.showHead(161, 478, 0)
    stage.delay(5)
    stage.bottomTxt('북평태수 공손찬과', True, False, False)
    stage.delay(5)
    stage.showHead(145, 476, 126)
    stage.delay(5)
    stage.bottomTxt('서주 자사 도겸,', True, False, False)
    stage.delay(5)
    stage.showHead(99, 347, 229)
    stage.delay(5)
    stage.bottomTxt('장사 태수 손견과', True, True, False)
    stage.delay(5)
    stage.showHead(136, 258, 200)
    stage.delay(5)
    stage.bottomTxt('남양 태수 원술 등이', True, False, False)
    stage.delay(5)
    stage.bottomTxt('모두 천하의 뛰어난 호걸이었다.', True, False, True)
    stage.loadBg(0, 16)
    stage.draw()
    stage.info('한편 조조는 왕윤의 칠성검을 팔아\n그 돈으로 자신의 군대를 일으켰다.')
    stage.bgSound(18)
    stage.loadBg(2, 21)
    stage.showUnit(0, 49, 67)
    stage.draw()
    stage.setStageName('진류@조조군 본거지')
    stage.showUnits([
        [
            1,
            49,
            22,
            2],
        [
            6,
            49,
            16,
            2]])
    stage.say('@dialogue:R_01.scene3.dialogue0001')
    stage.unitsMove([
        [
            stage.unit(1),
            49,
            52,
            2],
        [
            stage.unit(6),
            49,
            46,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(1),
            45,
            52,
            2],
        [
            stage.unit(6),
            53,
            52,
            2]])
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_01.scene3.dialogue0002')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_01.scene3.dialogue0003')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_01.scene3.dialogue0004')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(6).setAction(6)
    stage.say('@dialogue:R_01.scene3.dialogue0005')
    stage.unit(6).setAction(0)
    stage.say('@dialogue:R_01.scene3.dialogue0006')
    stage.unit(1).setAction(17)
    stage.say('@dialogue:R_01.scene3.dialogue0007')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_01.scene3.dialogue0008')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_01.scene3.dialogue0009')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_01.scene3.dialogue0010')
    stage.unit(6).setAction(0, 3)
    stage.say('@dialogue:R_01.scene3.dialogue0011')
    stage.say('@dialogue:R_01.scene3.dialogue0012')
    stage.showUnit(181, 49, 16, 2)
    stage.say('@dialogue:R_01.scene3.dialogue0013')
    stage.unit(1).setAction(0, 0)
    stage.unit(6).setAction(0, 0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_01.scene3.dialogue0014')
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_01.scene3.dialogue0015')
    stage.say('@dialogue:R_01.scene3.dialogue0016')
    stage.say('@dialogue:R_01.scene3.dialogue0017')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(1),
            35,
            52,
            1],
        [
            stage.unit(6),
            63,
            52,
            3]])
    stage.showUnits([
        [
            10,
            49,
            16,
            2],
        [
            11,
            49,
            16,
            2]])
    stage.unit(10).move(49, 22, 2)
    stage.unitsMove([
        [
            stage.unit(10),
            45,
            52,
            2],
        [
            stage.unit(11),
            53,
            52,
            2]])
    stage.delay(3)
    stage.say('@dialogue:R_01.scene3.dialogue0018')
    stage.unit(10).setAction(11)
    stage.say('@dialogue:R_01.scene3.dialogue0019')
    stage.unit(11).setAction(11)
    stage.say('@dialogue:R_01.scene3.dialogue0020')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_01.scene3.dialogue0021')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_01.scene3.dialogue0022')
    stage.say('@dialogue:R_01.scene3.dialogue0023')
    stage.unit(10).setAction(0)
    stage.unit(11).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(10),
            45,
            46,
            0],
        [
            stage.unit(11),
            53,
            46,
            0]])
    stage.showUnit(5, 49, 16, 2)
    stage.unit(5).move(49, 22, 2)
    stage.showUnit(12, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(10),
            35,
            46,
            1],
        [
            stage.unit(11),
            63,
            46,
            3],
        [
            stage.unit(5),
            49,
            52,
            2],
        [
            stage.unit(12),
            49,
            46,
            2]])
    stage.unitsMove([
        [
            stage.unit(5),
            45,
            52,
            2],
        [
            stage.unit(12),
            53,
            52,
            2]])
    stage.delay(3)
    stage.unit(5).setAction(9)
    stage.unit(12).setAction(9)
    stage.say('@dialogue:R_01.scene3.dialogue0024')
    stage.say('@dialogue:R_01.scene3.dialogue0025')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_01.scene3.dialogue0026')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.say('@dialogue:R_01.scene3.dialogue0027')
    stage.say('@dialogue:R_01.scene3.dialogue0028')
    stage.unit(5).setAction(0, 1)
    stage.delay(3)
    stage.unit(5).setAction(2, 1)
    stage.say('@dialogue:R_01.scene3.dialogue0029')
    stage.unit(12).setAction(0, 3)
    stage.delay(3)
    stage.unit(12).setAction(2, 3)
    stage.say('@dialogue:R_01.scene3.dialogue0030')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_01.scene3.dialogue0031')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(5).setAction(0, 2)
    stage.unit(12).setAction(0, 2)
    stage.say('@dialogue:R_01.scene3.dialogue0032')
    stage.unit(5).setAction(6, 2)
    stage.say('@dialogue:R_01.scene3.dialogue0033')
    stage.unit(12).setAction(6, 2)
    stage.say('@dialogue:R_01.scene3.dialogue0034')
    stage.unit(5).setAction(0, 2)
    stage.unit(12).setAction(0, 2)
    stage.unit(1).setAction(0, 2)
    stage.unit(6).setAction(0, 2)
    stage.unit(10).setAction(0, 2)
    stage.unit(11).setAction(0, 2)
    stage.delay(5)
    model.setFace(0)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_01.scene3.dialogue0035')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('@dialogue:R_01.scene3.dialogue0036')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 54)
    stage.showUnit(0, 54, 52, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.setStageName('낙양 교외@동맹군 본진')
    stage.say('@dialogue:R_01.scene4.dialogue0001')
    stage.unit(0).setAction(0, 2)
    stage.showUnit(99, 54, 94)
    stage.unit(99).move(54, 60, 0)
    stage.delay(3)
    stage.unit(99).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0002')
    stage.unit(99).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_01.scene4.dialogue0003')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_01.scene4.dialogue0004')
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0005')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.say('@dialogue:R_01.scene4.dialogue0006')
    stage.unit(99).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0007')
    stage.unit(99).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0008')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.unit(99).move(42, 68, 1)
    stage.delay(10)
    stage.info('잠시 뒤……,', 3)
    stage.showUnit(136, 54, 94)
    stage.unit(136).move(54, 60, 0)
    stage.delay(3)
    stage.unit(136).setAction(3)
    stage.say('@dialogue:R_01.scene4.dialogue0009')
    stage.unit(136).setAction(0)
    stage.say('@dialogue:R_01.scene4.dialogue0010')
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0011')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_01.scene4.dialogue0012')
    stage.unit(136).move(42, 60, 1)
    stage.say('@dialogue:R_01.scene4.dialogue0013')
    stage.unit(136).setAction(2, 2)
    stage.say('@dialogue:R_01.scene4.dialogue0014')
    stage.unit(136).setAction(0, 2)
    stage.say('@dialogue:R_01.scene4.dialogue0015')
    stage.unit(136).setAction(2, 2)
    stage.say('@dialogue:R_01.scene4.dialogue0016')
    stage.unit(136).setAction(0, 1)
    stage.delay(3)
    stage.unit(136).setAction(3, 1)
    stage.say('@dialogue:R_01.scene4.dialogue0017')
    stage.unit(136).setAction(0)
    stage.say('@dialogue:R_01.scene4.dialogue0018')
    stage.unit(0).setAction(0, 0)
    stage.say('@dialogue:R_01.scene4.dialogue0019')
    stage.showUnit(145, 54, 94)
    stage.say('@dialogue:R_01.scene4.dialogue0020')
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(145).move(54, 60, 0)
    stage.delay(3)
    stage.unit(145).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0021')
    stage.unit(145).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0022')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(145).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0023')
    stage.unit(145).setAction(0)
    stage.delay(1)
    stage.unit(145).move(42, 76, 1)
    stage.showUnits([
        [
            161,
            54,
            94],
        [
            32,
            62,
            94]])
    stage.say('@dialogue:R_01.scene4.dialogue0024')
    stage.unit(0).setAction(0, 2)
    stage.unitsMove([
        [
            stage.unit(161),
            54,
            60,
            0],
        [
            stage.unit(32),
            62,
            60,
            0]])
    stage.delay(3)
    stage.unit(161).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0025')
    stage.unit(161).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0026')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_01.scene4.dialogue0027')
    stage.unit(161).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0028')
    stage.unit(161).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0029')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(6)
    stage.say('@dialogue:R_01.scene4.dialogue0030')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_01.scene4.dialogue0031')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_01.scene4.dialogue0032')
    stage.say('@dialogue:R_01.scene4.dialogue0033')
    stage.say('@dialogue:R_01.scene4.dialogue0034')
    stage.unit(32).move(67, 68, 3)
    stage.unit(32).setAction(0)
    stage.unit(161).move(67, 60, 3)
    stage.unit(161).setAction(0)
    stage.showUnit(101, 54, 94)
    stage.say('@dialogue:R_01.scene4.dialogue0035')
    stage.unitsMove([
        [
            stage.unit(0),
            42,
            52,
            1],
        [
            stage.unit(101),
            54,
            52,
            0]])
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 2)
    stage.say('@dialogue:R_01.scene4.dialogue0036')
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_01.scene4.dialogue0037')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 3)
    stage.say('@dialogue:R_01.scene4.dialogue0038')
    stage.unit(101).setAction(0, 2)
    stage.say('@dialogue:R_01.scene4.dialogue0039')
    stage.say('@dialogue:R_01.scene4.dialogue0040')
    stage.say('@dialogue:R_01.scene4.dialogue0041')
    stage.unit(101).setAction(0, 3)
    stage.say('@dialogue:R_01.scene4.dialogue0042')
    stage.say('@dialogue:R_01.scene4.dialogue0043')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.clsUnit()
    stage.bgSound(16)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(127, 263, 38)
    stage.bottomTxt('동탁은 낙양에서 포악한 짓을 일삼았고,', False, False, False)
    stage.delay(5)
    stage.showHead(0, 371, 74)
    stage.showHead(101, 435, 74)
    stage.showHead(161, 499, 74)
    stage.showHead(136, 371, 154)
    stage.showHead(99, 435, 154)
    stage.showHead(145, 499, 154)
    stage.bottomTxt('그리하여 동탁을 토벌하고자,', True, True, False)
    stage.bottomTxt('동탁 토벌 동맹군은 낙양 근교에 집결했다.', True, False, True)
    stage.bottomTxt('이 사실을 안 동탁은 크게 격노했다. 동맹군을 물리치고자,', False, True, False)
    stage.delay(5)
    stage.showHead(134, 263, 118)
    stage.delay(5)
    stage.bottomTxt('이에 맹장 화웅을 보내', True, False, False)
    stage.delay(5)
    stage.head(134).move(307, 118)
    stage.delay(5)
    stage.bottomTxt('사수관으로 향했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            101,
            54,
            37,
            2],
        [
            0,
            50,
            45,
            2],
        [
            99,
            42,
            71,
            1],
        [
            136,
            42,
            63,
            1],
        [
            145,
            42,
            55,
            1],
        [
            161,
            67,
            55,
            3],
        [
            32,
            67,
            63,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.setStageName('낙양 교외@동맹군 본진')
    stage.showUnit(181, 54, 94)
    stage.unit(181).move(54, 86, 0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('@dialogue:R_01.scene5.dialogue0001')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_01.scene5.dialogue0002')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_01.scene5.dialogue0003')
    stage.unit(101).setAction(0)
    stage.say('@dialogue:R_01.scene5.dialogue0004')
    stage.delay(5)
    stage.unit(99).setAction(6)
    stage.say('@dialogue:R_01.scene5.dialogue0005')
    stage.unit(99).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_01.scene5.dialogue0006')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(99).setAction(6)
    stage.say('@dialogue:R_01.scene5.dialogue0007')
    stage.unit(99).setAction(0)
    stage.delay(3)
    stage.unit(99).move(42, 95, 2)
    stage.unit(99).hide()
    stage.delay(5)
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_01.scene5.dialogue0008')
    stage.unit(101).setAction(0)
    stage.delay(5)
    stage.say('@dialogue:R_01.scene5.dialogue0009')
    stage.delay(5)
    stage.unit(136).setAction(0, 2)
    stage.say('@dialogue:R_01.scene5.dialogue0010')
    stage.unit(136).setAction(2)
    stage.say('@dialogue:R_01.scene5.dialogue0011')
    stage.unit(136).setAction(0)
    stage.say('@dialogue:R_01.scene5.dialogue0012')
    stage.unit(136).setAction(3)
    stage.say('@dialogue:R_01.scene5.dialogue0013')
    stage.unit(136).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_01.scene5.dialogue0014')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene6():
    model.unitJoin(1, 0, 0)
    model.unitJoin(6, 0, 0)
    model.unitJoin(10, 0, 0)
    model.unitJoin(11, 0, 0)
    model.unitJoin(5, 0, 0)
    model.unitJoin(12, 0, 0)
    if vars[1013]:
        gvars[0] = 1
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
        gvars[0] = 6
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
        gvars[0] = 10
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
        gvars[0] = 11
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
        gvars[0] = 5
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
        gvars[0] = 12
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
    return False


def scene7():
    stage.bgSound(15)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            66],
        [
            1,
            38,
            52,
            1],
        [
            5,
            38,
            46,
            1],
        [
            10,
            38,
            40,
            1],
        [
            6,
            60,
            52,
            3],
        [
            12,
            60,
            46,
            3],
        [
            11,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('사수관@조조군 본진')
    stage.say('@dialogue:R_01.scene7.dialogue0001')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        1])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 1
    stage.setBattlePos([
        [
            19,
            9],
        [
            18,
            8],
        [
            18,
            11],
        [
            18,
            9],
        [
            18,
            10],
        [
            19,
            8],
        [
            19,
            11],
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
        stage.say('@dialogue:R_01.scene8.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_01.scene8.dialogue0002')
    if stage.unitClickTest(6):
        stage.say('@dialogue:R_01.scene8.dialogue0003')
    if stage.unitClickTest(5):
        stage.say('@dialogue:R_01.scene8.dialogue0004')
    if stage.unitClickTest(12):
        stage.say('@dialogue:R_01.scene8.dialogue0005')
    if stage.unitClickTest(10):
        stage.say('@dialogue:R_01.scene8.dialogue0006')
    if stage.unitClickTest(11):
        stage.say('@dialogue:R_01.scene8.dialogue0007')
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
