# Source Generated with Decompyle++
# File: R_58.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    stage.clsUnit()
    stage.setMenuVisible(False)
    return False


def scene1():
    if vars[645]:
        stage.bgSound(13)
        stage.loadBg(2, 16)
        stage.showUnit(76, 53, 82, 0, 9)
        stage.draw()
        stage.setEventName('새로운 시대를 맞이하다')
        stage.setStageName('건업  의사청')
        stage.say('@dialogue:R_58.scene1.dialogue0001')
        stage.effectSound(38, 1)
        stage.unit(76).setAction(10)
        stage.say('@dialogue:R_58.scene1.dialogue0002')
        stage.delay(10)
        stage.showUnit(0, 52, 5, 2)
        stage.unit(0).move(52, 20, 2)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0003')
        stage.unit(0).move(52, 60, 2)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0004')
        stage.showUnit(1, 48, 5, 2)
        stage.unit(1).move(48, 52, 2)
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0005')
        stage.showUnit(20, 55, 5, 2)
        stage.unit(20).move(55, 52, 2)
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0006')
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_58.scene1.dialogue0007')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0008')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0009')
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0010')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_58.scene1.dialogue0011')
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0012')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(17)
        stage.say('@dialogue:R_58.scene1.dialogue0013')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0014')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0015')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(20),
                55,
                5,
                0],
            [
                stage.unit(1),
                48,
                5,
                0]])
        stage.unit(1).hide()
        stage.unit(20).hide()
        stage.delay(8)
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_58.scene1.dialogue0016')
        stage.delay(5)
        stage.bgSound(19)
        stage.clsUnit()
        stage.loadBg(2, 73)
        stage.showUnit(35, 49, 73, 0, 11)
        stage.draw()
        stage.setStageName('장강 가  거리')
        stage.say('@dialogue:R_58.scene1.dialogue0017')
        stage.showUnit(19, 90, 73, 3)
        stage.unit(19).move(57, 73, 3)
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0018')
        stage.unit(35).setAction(11, 1)
        stage.say('@dialogue:R_58.scene1.dialogue0019')
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0020')
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0021')
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0022')
        stage.unit(35).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0023')
        stage.unit(19).setAction(9)
        stage.say('@dialogue:R_58.scene1.dialogue0024')
        stage.unit(35).setAction(0)
        stage.delay(10)
        stage.unit(35).setAction(0, 0)
        stage.say('@dialogue:R_58.scene1.dialogue0025')
        stage.delay(5)
        stage.say('@dialogue:R_58.scene1.dialogue0026')
        stage.delay(5)
        stage.unit(35).setAction(11)
        stage.say('@dialogue:R_58.scene1.dialogue0027')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0028')
        stage.unit(35).setAction(11, 1)
        stage.delay(10)
        stage.unit(35).setAction(0)
        stage.delay(5)
        stage.say('@dialogue:R_58.scene1.dialogue0029')
        stage.delay(8)
        stage.unit(35).setAction(0, 0)
        stage.say('@dialogue:R_58.scene1.dialogue0030')
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0031')
        stage.unit(35).setAction(0, 1)
        stage.delay(3)
        stage.unit(35).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0032')
        stage.unit(35).setAction(0)
        stage.delay(5)
        stage.unit(35).move(10, 73, 3)
        stage.unit(35).hide()
        stage.bgSound(16)
        stage.clsUnit()
        stage.loadBg(1, 0)
        stage.draw()
        stage.showHead(0, 501, 208)
        stage.bottomTxt('조조는 촉과 오를 평정하고,', False, False, False)
        stage.delay(5)
        stage.head(0).move(330, 105)
        stage.delay(5)
        stage.bottomTxt('허도로 개선했다.', True, False, True)
        stage.bgSound(-1)
        stage.loadBg(2, 15)
        stage.showUnits([
            [
                149,
                27,
                55,
                1],
            [
                192,
                46,
                43,
                2],
            [
                193,
                52,
                43,
                2],
            [
                194,
                58,
                43,
                2],
            [
                195,
                46,
                67],
            [
                196,
                52,
                67],
            [
                197,
                58,
                67]])
        stage.draw()
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.setStageName('허도  왕궁')
        stage.delay(3)
        stage.unit(192).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0033')
        stage.unit(192).setAction(0)
        stage.showUnit(0, 95, 55, 3)
        stage.say('@dialogue:R_58.scene1.dialogue0034')
        stage.unit(0).move(89, 55, 3)
        stage.showUnits([
            [
                1,
                95,
                51,
                3],
            [
                20,
                95,
                59,
                3]])
        stage.unitsMove([
            [
                stage.unit(0),
                83,
                55,
                3],
            [
                stage.unit(1),
                89,
                51,
                3],
            [
                stage.unit(20),
                89,
                59,
                3]])
        stage.showUnits([
            [
                2,
                95,
                51,
                3],
            [
                8,
                95,
                59,
                3]])
        stage.unitsMove([
            [
                stage.unit(0),
                77,
                55,
                3],
            [
                stage.unit(1),
                83,
                51,
                3],
            [
                stage.unit(20),
                83,
                59,
                3],
            [
                stage.unit(2),
                89,
                51,
                3],
            [
                stage.unit(8),
                89,
                59,
                3]])
        stage.showUnits([
            [
                4,
                95,
                51,
                3],
            [
                19,
                95,
                59,
                3]])
        stage.unitsMove([
            [
                stage.unit(0),
                46,
                55,
                3],
            [
                stage.unit(1),
                52,
                51,
                3],
            [
                stage.unit(20),
                52,
                59,
                3],
            [
                stage.unit(2),
                58,
                51,
                3],
            [
                stage.unit(8),
                58,
                59,
                3],
            [
                stage.unit(4),
                64,
                51,
                3],
            [
                stage.unit(19),
                64,
                59,
                3]])
        stage.unit(0).setAction(0)
        stage.unit(1).setAction(0)
        stage.unit(20).setAction(0)
        stage.unit(2).setAction(0)
        stage.unit(8).setAction(0)
        stage.unit(4).setAction(0)
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0035')
        stage.unit(1).setAction(6)
        stage.unit(20).setAction(6)
        stage.unit(2).setAction(6)
        stage.unit(8).setAction(6)
        stage.unit(4).setAction(6)
        stage.unit(19).setAction(6)
        stage.delay(5)
        stage.unit(0).setAction(0)
        stage.unit(1).setAction(0)
        stage.unit(20).setAction(0)
        stage.unit(2).setAction(0)
        stage.unit(8).setAction(0)
        stage.unit(4).setAction(0)
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.say('@dialogue:R_58.scene1.dialogue0036')
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0037')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0038')
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0039')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0040')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0041')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0042')
        stage.unit(19).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0043')
        stage.unit(192).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0044')
        stage.unit(192).setAction(0)
        stage.delay(3)
        stage.unit(193).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0045')
        stage.unit(193).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0046')
        stage.unit(192).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0047')
        stage.unit(192).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0048')
        stage.unit(193).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0049')
        stage.unit(193).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0050')
        if model.ambition() < 85:
            stage.effectSound(200, 255)
            stage.bgSound(0)
            stage.delay(10)
            stage.unit(0).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0051')
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0052')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0053')
            stage.unit(0).setAction(3)
            stage.say('@dialogue:R_58.scene1.dialogue0054')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0055')
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0056')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(20).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0057')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0058')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0059')
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0060')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0061')
            stage.unit(0).setAction(3)
            stage.say('@dialogue:R_58.scene1.dialogue0062')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(149).setAction(0, 2)
            stage.say('@dialogue:R_58.scene1.dialogue0063')
            stage.unit(149).setAction(0, 1)
            stage.say('@dialogue:R_58.scene1.dialogue0064')
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0065')
            stage.unit(0).setAction(0)
            stage.delay(5)
            stage.unit(0).setAction(0, 1)
            stage.delay(3)
            stage.say('@dialogue:R_58.scene1.dialogue0066')
            stage.unit(0).setAction(3)
            stage.say('@dialogue:R_58.scene1.dialogue0067')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(1).setAction(3)
            stage.say('@dialogue:R_58.scene1.dialogue0068')
            stage.unit(1).setAction(0)
            stage.delay(3)
            stage.unit(20).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0069')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(17)
            stage.say('@dialogue:R_58.scene1.dialogue0070')
            stage.bgSound(16)
            stage.clsUnit()
            stage.loadBg(1, 0)
            stage.draw()
            stage.bottomTxt('난세가 막을 내리면서', False, False, False)
            stage.delay(5)
            stage.showHead(0, 284, 73)
            stage.showHead(1, 188, 153)
            stage.showHead(2, 252, 153)
            stage.showHead(20, 316, 153)
            stage.showHead(24, 380, 153)
            stage.delay(5)
            stage.bottomTxt('새로운 시대가 열렸다.', True, False, False)
            stage.delay(15)
            stage.bottomTxt('조조는 태평성세의 기틀을 다지고,', True, True, False)
            stage.delay(5)
            stage.head(0).hide()
            stage.delay(5)
            stage.bottomTxt('얼마 뒤 세상을 떠났다.', True, False, False)
            stage.delay(10)
            stage.bottomTxt('조조를 따라 각지를 누빈 부하들은', True, True, False)
            stage.delay(5)
            stage.head(1).hide()
            stage.head(2).hide()
            stage.head(20).hide()
            stage.head(24).hide()
            stage.delay(5)
            stage.bottomTxt('대부분 세상을 떠났다.', True, False, True)
            stage.bottomTxt('시간이 흘러……', False, False, False)
            stage.delay(10)
            stage.showHead(8, 320, 101)
            stage.delay(5)
            stage.bottomTxt('조조의 적자 조비는', True, True, False)
            stage.delay(5)
            stage.showHead(149, 256, 101)
            stage.delay(5)
            stage.bottomTxt('끝내 헌제에게 선양을 강요하여,', True, False, False)
            stage.delay(5)
            stage.head(149).hide()
            stage.delay(5)
            stage.head(8).move(288, 101)
            stage.head(8).hide()
            stage.delay(10)
            model.setFace(32)
            stage.showHead(8, 288, 101)
            stage.delay(5)
            stage.bottomTxt('위의 새 황제로 자리 잡았다.', True, False, False)
            stage.delay(10)
            stage.bottomTxt('하늘의 부름을 받은 조조는 과연', True, True, False)
            stage.delay(5)
            stage.bottomTxt('아들의 행동을 어찌 생각할지……', True, False, True)
            stage.bgSound(-1)
            stage.clsUnit()
            stage.ending(1)
        if model.ambition() >= 85:
            stage.effectSound(200, 255)
            stage.bgSound(0)
            stage.delay(10)
            stage.unit(0).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0071')
            stage.unit(149).setAction(0, 2)
            stage.say('@dialogue:R_58.scene1.dialogue0072')
            stage.unit(192).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0073')
            stage.unit(192).setAction(0)
            stage.delay(3)
            stage.unit(193).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0074')
            stage.unit(149).setAction(0, 1)
            stage.say('@dialogue:R_58.scene1.dialogue0075')
            stage.unit(193).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0076')
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0077')
            stage.unit(0).setAction(0)
            stage.delay(5)
            stage.unit(1).setAction(4)
            stage.say('@dialogue:R_58.scene1.dialogue0078')
            stage.unit(20).setAction(0, 0)
            stage.delay(3)
            stage.unit(20).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0079')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(1).setAction(0)
            stage.delay(3)
            stage.unit(1).setAction(0, 2)
            stage.say('@dialogue:R_58.scene1.dialogue0080')
            stage.unit(0).setAction(0, 1)
            stage.say('@dialogue:R_58.scene1.dialogue0081')
            stage.unit(1).setAction(0, 3)
            stage.unit(20).setAction(0, 3)
            stage.say('@dialogue:R_58.scene1.dialogue0082')
            stage.unit(0).setAction(0, 3)
            stage.delay(3)
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0083')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0084')
            stage.unit(0).setAction(0, 2)
            stage.say('@dialogue:R_58.scene1.dialogue0085')
            stage.unit(20).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0086')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(0, 1)
            stage.say('@dialogue:R_58.scene1.dialogue0087')
            stage.unit(20).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0088')
            stage.unit(20).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0089')
            stage.unit(0).setAction(0, 3)
            stage.delay(3)
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0090')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(192).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0091')
            stage.unit(192).setAction(0)
            stage.delay(3)
            stage.unit(193).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0092')
            stage.say('@dialogue:R_58.scene1.dialogue0093')
            stage.unit(193).setAction(0)
            stage.say('@dialogue:R_58.scene1.dialogue0094')
            stage.unit(0).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0095')
            stage.unit(0).setAction(0)
            stage.delay(5)
            stage.unit(0).setAction(0, 1)
            stage.delay(3)
            stage.unit(0).setAction(3)
            stage.say('@dialogue:R_58.scene1.dialogue0096')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(1).setAction(3)
            stage.say('@dialogue:R_58.scene1.dialogue0097')
            stage.unit(1).setAction(0)
            stage.delay(3)
            stage.unit(20).setAction(6)
            stage.say('@dialogue:R_58.scene1.dialogue0098')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(17)
            stage.say('@dialogue:R_58.scene1.dialogue0099')
            stage.clsUnit()
            stage.loadBg(1, 0)
            stage.draw()
            stage.showHead(127, 195, 91)
            stage.showHead(101, 407, 9)
            stage.showHead(119, 471, 121)
            stage.showHead(32, 30, 170)
            stage.showHead(35, 94, 190)
            stage.showHead(76, 536, 215)
            stage.delay(5)
            stage.bottomTxt('난세를 누비던 영웅들이', False, False, False)
            stage.delay(8)
            stage.head(127).hide()
            stage.head(119).hide()
            stage.head(101).hide()
            stage.head(32).hide()
            stage.head(35).hide()
            stage.head(76).hide()
            stage.delay(5)
            stage.bottomTxt('하나둘 역사의 무대에서 물러날 때', True, True, False)
            stage.delay(5)
            stage.bottomTxt('난세도 마침내 막을 내렸다.', True, False, True)
            stage.delay(5)
            stage.showHead(0, 288, 101)
            stage.delay(5)
            stage.bottomTxt('새 황제 조맹덕의 통치 아래,', False, False, False)
            stage.delay(5)
            stage.showHead(23, 195, 91)
            stage.showHead(8, 407, 9)
            stage.showHead(20, 471, 121)
            stage.showHead(19, 30, 170)
            stage.showHead(1, 294, 229)
            stage.showHead(2, 536, 215)
            stage.delay(5)
            stage.bottomTxt('새로운 시대가 열렸다.', True, False, False)
            stage.delay(5)
            stage.bottomTxt('사람이 사람을 다스리는 시대……', True, True, True)
            stage.bgSound(-1)
            stage.clsUnit()
            stage.ending(0)
    if not vars[645]:
        stage.loadBg(2, 24)
        stage.showUnit(35, 54, 43, 2)
        stage.draw()
        stage.setEventName('새로운 시대를 향하여')
        stage.setStageName('오장원 지하  제단')
        stage.delay(3)
        stage.unit(35).setAction(2)
        stage.say('@dialogue:R_58.scene1.dialogue0100')
        stage.unit(35).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0101')
        stage.showUnit(0, 54, 95)
        stage.unit(0).move(54, 86, 0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0102')
        stage.unit(35).setAction(0, 2)
        stage.unit(0).move(54, 53, 0)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(35).setAction(11)
        stage.say('@dialogue:R_58.scene1.dialogue0103')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('@dialogue:R_58.scene1.dialogue0104')
        stage.unit(35).setAction(2)
        stage.say('@dialogue:R_58.scene1.dialogue0105')
        stage.unit(35).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0106')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(17)
        stage.say('@dialogue:R_58.scene1.dialogue0107')
        stage.unit(35).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0108')
        stage.delay(5)
        stage.say('@dialogue:R_58.scene1.dialogue0109')
        model.setFace(1)
        stage.say('@dialogue:R_58.scene1.dialogue0110')
        stage.unit(35).setAction(8)
        stage.say('@dialogue:R_58.scene1.dialogue0111')
        stage.unit(35).setAction(12)
        stage.say('@dialogue:R_58.scene1.dialogue0112')
        stage.effectSound(38, 1)
        stage.unit(35).setAction(10, 2)
        stage.delay(3)
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0113')
        stage.unit(0).setAction(0)
        stage.bgSound(13)
        stage.delay(15)
        model.setFace(255)
        model.setFace(0)
        stage.say('@dialogue:R_58.scene1.dialogue0114')
        stage.unit(0).setAction(6)
        model.setFace(1)
        stage.say('@dialogue:R_58.scene1.dialogue0115')
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0116')
        stage.delay(8)
        model.setFace(0)
        stage.say('@dialogue:R_58.scene1.dialogue0117')
        stage.delay(5)
        stage.say('@dialogue:R_58.scene1.dialogue0118')
        stage.delay(5)
        stage.say('@dialogue:R_58.scene1.dialogue0119')
        stage.delay(3)
        stage.say('@dialogue:R_58.scene1.dialogue0120')
        stage.showUnit(3, 54, 95)
        stage.unit(3).move(54, 86, 0)
        stage.say('@dialogue:R_58.scene1.dialogue0121')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_58.scene1.dialogue0122')
        stage.unit(3).move(54, 63, 0)
        stage.say('@dialogue:R_58.scene1.dialogue0123')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0124')
        stage.unit(3).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0125')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0126')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_58.scene1.dialogue0127')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0128')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_58.scene1.dialogue0129')
        stage.delay(10)
        stage.bgSound(-1)
        stage.clsUnit()
        stage.loadBg(2, 32)
        stage.showUnit(0, 63, 33, 3)
        stage.draw()
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.info('공명이 죽은 지 한 달 뒤……', 3)
        stage.setStageName('오장원  산 위')
        stage.delay(10)
        stage.showUnit(151, 90, 41, 3)
        stage.unit(151).move(73, 41, 3)
        stage.say('@dialogue:R_58.scene1.dialogue0130')
        stage.unit(151).move(63, 41, 3)
        stage.unit(151).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_58.scene1.dialogue0131')
        stage.unit(151).setAction(0, 0)
        stage.delay(3)
        stage.unit(151).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0132')
        stage.unit(151).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0133')
        stage.unit(151).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0134')
        stage.unit(151).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 3)
        stage.say('@dialogue:R_58.scene1.dialogue0135')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_58.scene1.dialogue0136')
        stage.unit(151).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0137')
        stage.unit(151).setAction(0)
        stage.delay(3)
        stage.unit(151).move(63, 37)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.delay(3)
        stage.unit(151).move(63, 41, 0)
        stage.delay(3)
        stage.say('@dialogue:R_58.scene1.dialogue0138')
        stage.unit(151).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0139')
        stage.unit(151).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0140')
        stage.effectSound(203, 255)
        stage.bgSound(19)
        stage.delay(10)
        stage.info('지금 이 서찰을 펼치는 이는\n서서가 아니라면 방통이겠지요.\n누가 이 서찰을 얻었든\n그때는 제가 이미 세상에 없을지도 모릅니다.')
        stage.info('죽음이 두려운 것은 아니오.\n내가 살아 있어 큰 재앙을 부를까 두려울 뿐이오.')
        stage.info('그대들도 마왕의 이야기는 들었을 것이오.\n삼천 년 전, 위대한 선조 황제께서\n천하를 다스리실 때 반란을 일으킨 마왕 말이오.')
        stage.info('삼천 년 동안 잠들었던\n마왕이 이제 깨어나\n난세를 틈타 천하를 노예로 삼으려 합니다.')
        stage.info('마왕은 이미 육신을 잃어\n재능 있는 자에게 들어야만 움직일 수 있었다.\n결국 우리 주군 유비를 그릇으로 택했다.')
        stage.info('적벽대전에서 기이하게 불어와\n우리 군에 승리를 안긴 동남풍도\n마왕이 자신의 분신을 돕기 위해 일으킨 것이오.')
        stage.info('적벽대전 뒤 마왕이 마침내 움직이기 시작했다.\n밤하늘에 요사스러운 구름이 일고 혜성이 나타났다.\n하지만 유비 주군께 들게 둘 수는 없었다!')
        stage.info('주군께 후세의 오명이 남지 않도록\n마왕을 제 몸에 받아들일 수밖에 없었습니다.\n그 뒤 마왕의 야심과 맞서 싸웠습니다!')
        stage.info('앞으로 마왕의 야심 때문에\n촉의 수많은 장병이 희생될지도 모르오.\n나 역시 비참한 끝을 맞을지도 모르지.\n허나 그대에게 진실을 알리고 싶었소.')
        stage.info('내가 마왕에게 패한다면\n그대 손으로 나를 죽여 주시오.\n그래야 마왕을 없앨 수 있소!\n제갈공명@강하에서 쓰다')
        stage.delay(5)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0141')
        stage.unit(0).setAction(0, 3)
        stage.say('@dialogue:R_58.scene1.dialogue0142')
        stage.delay(15)
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_58.scene1.dialogue0143')
        stage.unit(151).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0144')
        stage.unit(151).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0145')
        stage.unit(151).setAction(0, 3)
        stage.say('@dialogue:R_58.scene1.dialogue0146')
        stage.delay(8)
        stage.say('@dialogue:R_58.scene1.dialogue0147')
        stage.delay(8)
        stage.unit(151).setAction(0, 0)
        stage.say('@dialogue:R_58.scene1.dialogue0148')
        stage.unit(151).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0149')
        stage.unit(151).setAction(0)
        stage.delay(3)
        stage.unit(151).move(95, 41, 1)
        stage.unit(151).hide()
        stage.delay(5)
        stage.say('@dialogue:R_58.scene1.dialogue0150')
        stage.bgSound(-1)
        stage.clsUnit()
        stage.loadBg(2, 33)
        stage.showUnit(3, 32, 55, 3, 6)
        stage.draw()
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.setStageName('허도 교외  묘지')
        stage.say('@dialogue:R_58.scene1.dialogue0151')
        stage.delay(5)
        stage.showUnit(0, 95, 55, 3)
        stage.unit(0).move(88, 55, 3)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0152')
        stage.unit(3).setAction(0, 1)
        stage.unit(0).move(40, 55, 3)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0153')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0154')
        stage.say('@dialogue:R_58.scene1.dialogue0155')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_58.scene1.dialogue0156')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0157')
        stage.unit(3).setAction(0, 3)
        stage.say('@dialogue:R_58.scene1.dialogue0158')
        stage.unit(3).setAction(0, 1)
        stage.delay(3)
        stage.say('@dialogue:R_58.scene1.dialogue0159')
        stage.unit(3).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0160')
        stage.unit(3).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0161')
        stage.unit(3).move(32, 51, 0)
        stage.delay(3)
        stage.unit(3).move(95, 51, 1)
        stage.unit(0).setAction(0, 1)
        stage.unit(3).hide()
        stage.delay(10)
        stage.unit(0).setAction(0, 3)
        stage.delay(10)
        stage.unit(0).move(32, 55, 3)
        stage.delay(10)
        stage.unit(0).setAction(6)
        stage.delay(10)
        stage.unit(0).setAction(0)
        stage.delay(10)
        stage.showUnits([
            [
                1,
                95,
                49,
                3],
            [
                20,
                95,
                55,
                3]])
        stage.unitsMove([
            [
                stage.unit(1),
                83,
                49,
                3],
            [
                stage.unit(20),
                83,
                55,
                3]])
        stage.say('@dialogue:R_58.scene1.dialogue0162')
        stage.unit(0).setAction(0, 1)
        stage.unitsMove([
            [
                stage.unit(1),
                40,
                49,
                3],
            [
                stage.unit(20),
                40,
                55,
                3]])
        stage.unit(1).setAction(0)
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0163')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0164')
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0165')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0166')
        stage.showUnits([
            [
                17,
                95,
                49,
                3],
            [
                15,
                95,
                55,
                3]])
        stage.unitsMove([
            [
                stage.unit(17),
                83,
                49,
                3],
            [
                stage.unit(15),
                83,
                55,
                3]])
        stage.say('@dialogue:R_58.scene1.dialogue0167')
        stage.unit(1).setAction(0, 1)
        stage.unit(20).setAction(0, 1)
        stage.unitsMove([
            [
                stage.unit(17),
                48,
                49,
                3],
            [
                stage.unit(15),
                48,
                55,
                3]])
        stage.unit(17).setAction(0)
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0168')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0169')
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0170')
        stage.unit(17).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0171')
        stage.unit(1).setAction(0, 3)
        stage.say('@dialogue:R_58.scene1.dialogue0172')
        stage.unit(20).setAction(0, 3)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0173')
        stage.unit(20).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0174')
        stage.unit(20).setAction(0, 1)
        stage.unit(1).setAction(0, 1)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0175')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_58.scene1.dialogue0176')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0177')
        stage.unit(15).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0178')
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0179')
        stage.unit(17).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0180')
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0181')
        stage.unit(17).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0182')
        stage.say('@dialogue:R_58.scene1.dialogue0183')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_58.scene1.dialogue0184')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0185')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0186')
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0187')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0188')
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0189')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0190')
        stage.unit(15).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0191')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0192')
        stage.unit(15).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0193')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_58.scene1.dialogue0194')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(17).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0195')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0196')
        stage.unit(15).setAction(0)
        stage.say('@dialogue:R_58.scene1.dialogue0197')
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(17),
                95,
                49,
                1],
            [
                stage.unit(15),
                95,
                55,
                1]])
        stage.unit(15).hide()
        stage.unit(17).hide()
        stage.delay(15)
        stage.unit(0).setAction(0, 3)
        stage.delay(10)
        stage.unit(1).setAction(0, 3)
        stage.say('@dialogue:R_58.scene1.dialogue0198')
        stage.unit(20).setAction(0, 3)
        stage.delay(5)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0199')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0200')
        stage.unit(20).setAction(0)
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_58.scene1.dialogue0201')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('@dialogue:R_58.scene1.dialogue0202')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_58.scene1.dialogue0203')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_58.scene1.dialogue0204')
        stage.unit(20).setAction(0)
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(0),
                87,
                55,
                1],
            [
                stage.unit(1),
                95,
                49,
                1],
            [
                stage.unit(20),
                95,
                55,
                1]])
        stage.unit(1).hide()
        stage.unit(20).hide()
        stage.unit(0).move(95, 55, 1)
        stage.unit(0).hide()
        stage.delay(20)
        stage.effectSound(200, 255)
        stage.clsUnit()
        stage.ending(2)
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
