# Source Generated with Decompyle++
# File: R_04.marshal (Python 3.9)


def scene0():
    if vars[1002]:
        stage.infoTransfer(18, '')
    if vars[303] and vars[703]:
        model.initLocalVar()
        stage.clsUnit()
        stage.setMenuVisible(False)
        stage.loadBg(2, 54)
        stage.showUnits([
            [
                101,
                54,
                35,
                2],
            [
                99,
                42,
                68,
                1],
            [
                136,
                42,
                60,
                1],
            [
                145,
                42,
                52,
                1],
            [
                161,
                67,
                52,
                3],
            [
                32,
                67,
                60,
                3],
            [
                181,
                54,
                60,
                0,
                11]])
        stage.draw()
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.setEventName('연환계')
        stage.setStageName('낙양  연합군 본진')
        stage.unit(101).setAction(3)
        stage.say('@dialogue:R_04.scene0.dialogue0001')
        stage.unit(101).setAction(0)
        stage.say('@dialogue:R_04.scene0.dialogue0002')
        stage.say('@dialogue:R_04.scene0.dialogue0003')
        stage.unit(136).setAction(3)
        stage.say('@dialogue:R_04.scene0.dialogue0004')
        stage.unit(136).setAction(0)
        stage.delay(3)
        stage.unit(99).setAction(0, 0)
        stage.say('@dialogue:R_04.scene0.dialogue0005')
        stage.delay(3)
        stage.unit(136).setAction(0, 2)
        stage.say('@dialogue:R_04.scene0.dialogue0006')
        stage.unit(99).setAction(0, 1)
        stage.say('@dialogue:R_04.scene0.dialogue0007')
        stage.unit(136).setAction(0, 1)
        stage.delay(5)
        stage.unit(32).setAction(6)
        stage.say('@dialogue:R_04.scene0.dialogue0008')
        stage.unit(32).setAction(0)
        stage.say('@dialogue:R_04.scene0.dialogue0009')
        stage.say('@dialogue:R_04.scene0.dialogue0010')
        stage.effectSound(203, 255)
        stage.clsUnit()
    if vars[303] and not vars[703]:
        model.initLocalVar()
        stage.clsUnit()
        stage.setMenuVisible(False)
        stage.loadBg(2, 54)
        stage.showUnits([
            [
                101,
                54,
                37,
                2],
            [
                99,
                42,
                68,
                1],
            [
                136,
                42,
                60,
                1],
            [
                145,
                42,
                52,
                1],
            [
                161,
                67,
                52,
                3],
            [
                32,
                67,
                60,
                3],
            [
                181,
                54,
                60,
                0,
                11]])
        stage.draw()
        stage.setEventName('연환계')
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.setStageName('낙양  연합군 본진')
        stage.delay(10)
        stage.unit(101).setAction(0, 0)
        stage.say('@dialogue:R_04.scene0.dialogue0011')
        stage.unit(101).setAction(0, 2)
        stage.say('@dialogue:R_04.scene0.dialogue0012')
        stage.say('@dialogue:R_04.scene0.dialogue0013')
        stage.unit(136).setAction(3)
        stage.say('@dialogue:R_04.scene0.dialogue0014')
        stage.unit(136).setAction(0)
        stage.delay(3)
        stage.unit(99).setAction(0, 0)
        stage.say('@dialogue:R_04.scene0.dialogue0015')
        stage.unit(136).setAction(0, 2)
        stage.say('@dialogue:R_04.scene0.dialogue0016')
        stage.unit(99).setAction(0, 1)
        stage.say('@dialogue:R_04.scene0.dialogue0017')
        stage.unit(136).setAction(0, 1)
        stage.delay(5)
        stage.unit(32).setAction(6)
        stage.say('@dialogue:R_04.scene0.dialogue0018')
        stage.unit(32).setAction(0)
        stage.say('@dialogue:R_04.scene0.dialogue0019')
        stage.say('@dialogue:R_04.scene0.dialogue0020')
        stage.effectSound(203, 255)
        stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            101,
            54,
            37,
            2],
        [
            99,
            42,
            68,
            1],
        [
            136,
            42,
            60,
            1],
        [
            145,
            42,
            52,
            1],
        [
            161,
            67,
            52,
            3],
        [
            32,
            67,
            60,
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    if not vars[303]:
        stage.setEventName('연환계')
    stage.info('다음 날…….', 3)
    stage.setStageName('낙양  연합군 본진')
    if not vars[303]:
        stage.delay(10)
        stage.unit(101).setAction(0, 3)
        stage.delay(10)
        stage.unit(101).setAction(0, 1)
        stage.delay(10)
        stage.unit(101).setAction(0, 2)
        stage.say('@dialogue:R_04.scene1.dialogue0001')
        stage.unit(32).setAction(6)
        stage.say('@dialogue:R_04.scene1.dialogue0002')
        stage.unit(32).setAction(0)
        stage.delay(3)
        stage.unit(101).setAction(2)
        stage.say('@dialogue:R_04.scene1.dialogue0003')
        stage.delay(3)
        stage.unit(101).setAction(0)
        stage.say('@dialogue:R_04.scene1.dialogue0004')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_04.scene1.dialogue0005')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).move(58, 37, 1)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_04.scene1.dialogue0006')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).move(54, 37, 3)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('@dialogue:R_04.scene1.dialogue0007')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 2)
    stage.say('@dialogue:R_04.scene1.dialogue0008')
    stage.unit(161).setAction(6)
    stage.say('@dialogue:R_04.scene1.dialogue0009')
    stage.unit(161).setAction(0)
    stage.delay(3)
    stage.unit(145).setAction(6)
    stage.say('@dialogue:R_04.scene1.dialogue0010')
    stage.unit(145).setAction(0)
    stage.say('@dialogue:R_04.scene1.dialogue0011')
    stage.delay(5)
    stage.say('@dialogue:R_04.scene1.dialogue0012')
    stage.delay(5)
    stage.unit(99).setAction(0, 2)
    stage.say('@dialogue:R_04.scene1.dialogue0013')
    stage.unit(101).setAction(3)
    stage.say('@dialogue:R_04.scene1.dialogue0014')
    stage.unit(101).setAction(0)
    stage.delay(5)
    stage.unit(99).setAction(0, 1)
    stage.say('@dialogue:R_04.scene1.dialogue0015')
    stage.unit(136).setAction(0, 2)
    stage.say('@dialogue:R_04.scene1.dialogue0016')
    stage.unit(99).setAction(0, 0)
    stage.say('@dialogue:R_04.scene1.dialogue0017')
    stage.unit(136).setAction(3)
    stage.say('@dialogue:R_04.scene1.dialogue0018')
    stage.unit(136).setAction(0)
    stage.delay(5)
    stage.unit(99).setAction(0, 2)
    stage.say('@dialogue:R_04.scene1.dialogue0019')
    stage.unit(99).move(42, 90, 2)
    stage.unit(136).setAction(0, 2)
    stage.unit(99).hide()
    if vars[303]:
        stage.delay(10)
        stage.showUnit(0, 54, 92)
        stage.say('@dialogue:R_04.scene1.dialogue0020')
        stage.unit(145).setAction(0, 2)
        stage.unit(136).setAction(0, 2)
        stage.unit(143).setAction(0, 2)
        stage.unit(161).setAction(0, 2)
        stage.unit(32).setAction(0, 2)
        stage.delay(3)
        stage.unit(101).setAction(3)
        stage.say('@dialogue:R_04.scene1.dialogue0021')
        stage.unit(101).setAction(0)
        stage.unit(0).move(54, 52, 0)
        stage.unit(0).setAction(0)
        stage.unit(145).setAction(0, 1)
        stage.unit(136).setAction(0, 1)
        stage.unit(161).setAction(0, 3)
        stage.unit(143).setAction(0, 3)
        stage.unit(32).setAction(0, 3)
        stage.delay(3)
        stage.say('@dialogue:R_04.scene1.dialogue0022')
        stage.delay(5)
        stage.unit(101).setAction(2)
        stage.say('@dialogue:R_04.scene1.dialogue0023')
        stage.unit(101).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 2)
        stage.delay(5)
        stage.say('@dialogue:R_04.scene1.dialogue0024')
        stage.unit(145).setAction(6)
        stage.say('@dialogue:R_04.scene1.dialogue0025')
        stage.unit(145).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 3)
        stage.delay(10)
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_04.scene1.dialogue0026')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_04.scene1.dialogue0027')
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 2)
        stage.delay(3)
        stage.unit(101).setAction(3)
        stage.say('@dialogue:R_04.scene1.dialogue0028')
        stage.unit(101).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_04.scene1.dialogue0029')
    if not vars[303]:
        stage.delay(10)
        stage.showUnit(0, 54, 92)
        stage.say('@dialogue:R_04.scene1.dialogue0030')
        stage.unit(101).setAction(2)
        stage.say('@dialogue:R_04.scene1.dialogue0031')
        stage.unit(101).setAction(0)
        stage.unit(136).setAction(0, 1)
        stage.unit(0).move(54, 52, 0)
        stage.unit(0).setAction(0)
        stage.say('@dialogue:R_04.scene1.dialogue0032')
        stage.delay(3)
        stage.unit(101).setAction(2)
        stage.say('@dialogue:R_04.scene1.dialogue0033')
        stage.unit(101).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 2)
        stage.delay(5)
        stage.say('@dialogue:R_04.scene1.dialogue0034')
        stage.unit(145).setAction(6)
        stage.say('@dialogue:R_04.scene1.dialogue0035')
        stage.unit(145).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 3)
        stage.delay(10)
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_04.scene1.dialogue0036')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:R_04.scene1.dialogue0037')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 2)
        stage.delay(5)
        stage.unit(101).setAction(3)
        stage.say('@dialogue:R_04.scene1.dialogue0038')
        stage.unit(101).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 0)
        stage.say('@dialogue:R_04.scene1.dialogue0039')
    stage.unit(0).move(54, 98, 2)
    stage.unit(0).hide()
    stage.delay(10)
    stage.say('@dialogue:R_04.scene1.dialogue0040')
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('@dialogue:R_04.scene1.dialogue0041')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(0, 2)
    stage.say('@dialogue:R_04.scene1.dialogue0042')
    stage.delay(5)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.delay(5)
    stage.bgSound(-1)
    stage.loadBg(2, 3)
    stage.showUnit(153, 58, 70, 3, 1)
    stage.draw()
    stage.delay(3)
    stage.setStageName('장안  왕윤 저택')
    stage.showUnit(181, 30, 60, 1)
    stage.unit(181).move(38, 60, 1)
    stage.say('@dialogue:R_04.scene2.dialogue0001')
    stage.unit(153).move(58, 60, 3)
    stage.say('@dialogue:R_04.scene2.dialogue0002')
    stage.bgSound(17)
    stage.delay(5)
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_04.scene2.dialogue0003')
    stage.unit(181).move(40, 60, 1)
    stage.say('@dialogue:R_04.scene2.dialogue0004')
    stage.unit(153).setAction(0)
    stage.delay(5)
    stage.unit(153).setAction(0, 1)
    stage.say('@dialogue:R_04.scene2.dialogue0005')
    stage.unit(181).move(30, 60, 3)
    stage.unit(181).hide()
    stage.unit(153).move(58, 70, 2)
    stage.delay(3)
    stage.unit(153).setAction(1)
    stage.say('@dialogue:R_04.scene2.dialogue0006')
    stage.unit(153).setAction(9)
    stage.say('@dialogue:R_04.scene2.dialogue0007')
    stage.delay(10)
    stage.showUnit(17, 30, 60, 1)
    stage.say('@dialogue:R_04.scene2.dialogue0008')
    stage.unit(17).move(58, 60, 2)
    stage.say('@dialogue:R_04.scene2.dialogue0009')
    stage.unit(153).setAction(1, 2)
    stage.delay(3)
    stage.unit(153).setAction(1, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0010')
    stage.unit(17).setAction(6, 2)
    stage.say('@dialogue:R_04.scene2.dialogue0011')
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0012')
    stage.unit(153).setAction(0, 2)
    stage.say('@dialogue:R_04.scene2.dialogue0013')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_04.scene2.dialogue0014')
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0015')
    stage.say('@dialogue:R_04.scene2.dialogue0016')
    stage.unit(153).move(58, 68, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0017')
    stage.unit(17).move(58, 56, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0018')
    stage.delay(15)
    stage.unit(153).setAction(3, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0019')
    stage.unit(17).setAction(0, 2)
    stage.say('@dialogue:R_04.scene2.dialogue0020')
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0021')
    stage.unit(17).setAction(0, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0022')
    stage.unit(17).setAction(0, 2)
    stage.say('@dialogue:R_04.scene2.dialogue0023')
    stage.unit(153).setAction(6, 0)
    stage.say('@dialogue:R_04.scene2.dialogue0024')
    stage.unit(153).setAction(0, 0)
    stage.unit(17).move(58, 60, 2)
    stage.delay(5)
    stage.unit(17).setAction(6, 2)
    stage.say('@dialogue:R_04.scene2.dialogue0025')
    stage.unit(17).move(30, 60, 3)
    stage.unit(17).hide()
    stage.unit(153).setAction(0, 3)
    stage.delay(3)
    stage.unit(153).move(58, 70, 2)
    stage.delay(6)
    stage.unit(153).setAction(1, 2)
    stage.delay(3)
    stage.unit(153).setAction(9, 2)
    stage.say('@dialogue:R_04.scene2.dialogue0026')
    stage.info('초선의 계책은 먼저 여포와 혼약을 맺은 뒤\n자신을 동탁에게 바쳐 여포를 격분시키고,\n그 손을 빌려 동탁을 죽이는 것이었다.')
    stage.info('겨우 열여덟인 딸이 양부의 은혜에 보답하고자\n몸을 던져 의를 이루려 했다. 후세는 이를 연환계라 불렀다.')
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 3)
    stage.showUnits([
        [
            153,
            58,
            70],
        [
            119,
            58,
            60,
            2]])
    stage.delay(5)
    stage.draw()
    stage.setStageName('장안  왕윤 저택')
    stage.say('@dialogue:R_04.scene3.dialogue0001')
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_04.scene3.dialogue0002')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(153).move(58, 68, 0)
    stage.say('@dialogue:R_04.scene3.dialogue0003')
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_04.scene3.dialogue0004')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(0, 3)
    stage.say('@dialogue:R_04.scene3.dialogue0005')
    stage.delay(8)
    stage.showUnit(17, 30, 60, 1)
    stage.say('@dialogue:R_04.scene3.dialogue0006')
    stage.unit(119).setAction(0, 3)
    stage.delay(3)
    stage.unit(17).move(50, 60, 1)
    stage.unit(119).setAction(3, 3)
    stage.say('@dialogue:R_04.scene3.dialogue0007')
    stage.unit(153).setAction(0, 0)
    stage.say('@dialogue:R_04.scene3.dialogue0008')
    stage.unit(119).setAction(0, 3)
    stage.say('@dialogue:R_04.scene3.dialogue0009')
    stage.unit(17).setAction(6, 1)
    stage.say('@dialogue:R_04.scene3.dialogue0010')
    stage.unit(119).move(56, 60, 3)
    stage.say('@dialogue:R_04.scene3.dialogue0011')
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 40)
    stage.showUnits([
        [
            127,
            58,
            60,
            3],
        [
            17,
            55,
            60,
            1]])
    stage.draw()
    stage.setStageName('장안  동탁의 저택')
    stage.say('@dialogue:R_04.scene4.dialogue0001')
    stage.showUnit(119, 58, 31, 2)
    stage.say('@dialogue:R_04.scene4.dialogue0002')
    stage.unit(127).setAction(0, 0)
    stage.say('@dialogue:R_04.scene4.dialogue0003')
    stage.unit(119).setAction(3)
    stage.say('@dialogue:R_04.scene4.dialogue0004')
    stage.unit(119).setAction(2)
    stage.say('@dialogue:R_04.scene4.dialogue0005')
    stage.clsUnit()
    stage.loadBg(0, 9)
    stage.draw()
    stage.info('왕윤의 「연환계」가 마침내 성공하여 동탁은\n여포에게 살해당했다. 이후 여포는 동탁의 원수를\n갚으려는 이각, 곽사와 싸우게 되었다.')
    stage.info('여포의 무용을 잘 아는 왕윤은 승리를 믿고\n궁으로 가 황제께 아뢰었다.')
    return False


def scene5():
    stage.loadBg(2, 15)
    stage.showUnits([
        [
            149,
            27,
            55,
            1],
        [
            153,
            45,
            55,
            3,
            6]])
    stage.draw()
    stage.setStageName('장안  궁궐')
    stage.say('@dialogue:R_04.scene5.dialogue0001')
    stage.say('@dialogue:R_04.scene5.dialogue0002')
    stage.showUnit(181, 94, 55, 3)
    stage.unit(181).move(90, 55, 3)
    stage.effectSound(38, 1)
    stage.unit(181).setAction(10)
    stage.delay(3)
    stage.unit(153).setAction(0, 1)
    stage.say('@dialogue:R_04.scene5.dialogue0003')
    stage.showUnits([
        [
            131,
            95,
            52,
            3],
        [
            129,
            95,
            58,
            3]])
    stage.unitsMove([
        [
            stage.unit(131),
            55,
            52,
            3],
        [
            stage.unit(129),
            55,
            58,
            3]])
    stage.delay(3)
    stage.unit(131).setAction(2)
    stage.say('@dialogue:R_04.scene5.dialogue0004')
    stage.unit(131).setAction(0)
    stage.delay(3)
    stage.unit(129).setAction(0)
    stage.say('@dialogue:R_04.scene5.dialogue0005')
    stage.unit(129).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(3)
    stage.say('@dialogue:R_04.scene5.dialogue0006')
    stage.unit(153).setAction(0)
    stage.say('@dialogue:R_04.scene5.dialogue0007')
    stage.unit(131).setAction(3)
    stage.say('@dialogue:R_04.scene5.dialogue0008')
    stage.unit(131).setAction(0)
    stage.say('@dialogue:R_04.scene5.dialogue0009')
    stage.unit(129).setAction(3)
    stage.say('@dialogue:R_04.scene5.dialogue0010')
    stage.unit(129).setAction(0)
    stage.delay(5)
    stage.unit(153).setAction(0, 2)
    stage.say('@dialogue:R_04.scene5.dialogue0011')
    stage.say('@dialogue:R_04.scene5.dialogue0012')
    stage.unit(131).setAction(6)
    stage.say('@dialogue:R_04.scene5.dialogue0013')
    stage.unit(131).setAction(0)
    stage.delay(5)
    stage.say('@dialogue:R_04.scene5.dialogue0014')
    stage.effectSound(34, 1)
    stage.unit(153).setAction(14)
    stage.delay(10)
    stage.effectSound(35, 1)
    stage.unit(153).setAction(10)
    stage.say('@dialogue:R_04.scene5.dialogue0015')
    stage.delay(10)
    stage.say('@dialogue:R_04.scene5.dialogue0016')
    stage.say('@dialogue:R_04.scene5.dialogue0017')
    stage.unit(131).setAction(3)
    stage.say('@dialogue:R_04.scene5.dialogue0018')
    stage.unit(129).setAction(3)
    stage.say('@dialogue:R_04.scene5.dialogue0019')
    stage.delay(5)
    stage.clsUnit()
    return False


def scene6():
    stage.loadBg(2, 3)
    stage.showUnit(17, 60, 60, 1)
    stage.draw()
    stage.setStageName('낙양  왕윤의 저택')
    stage.say('@dialogue:R_04.scene6.dialogue0001')
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_04.scene6.dialogue0002')
    stage.showUnit(119, 30, 60, 1)
    stage.say('@dialogue:R_04.scene6.dialogue0003')
    stage.unit(17).setAction(0, 1)
    stage.delay(3)
    stage.unit(17).setAction(0, 3)
    stage.unit(119).move(50, 60, 1)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_04.scene6.dialogue0004')
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_04.scene6.dialogue0005')
    stage.unit(119).setAction(17)
    stage.say('@dialogue:R_04.scene6.dialogue0006')
    stage.unit(119).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('@dialogue:R_04.scene6.dialogue0007')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(119).setAction(0, 3)
    stage.delay(3)
    stage.unit(119).move(46, 60, 3)
    stage.delay(8)
    stage.unit(119).setAction(0, 1)
    stage.say('@dialogue:R_04.scene6.dialogue0008')
    stage.unit(17).setAction(0, 2)
    stage.delay(3)
    stage.unit(17).setAction(9)
    stage.say('@dialogue:R_04.scene6.dialogue0009')
    stage.unit(119).move(50, 60, 1)
    stage.say('@dialogue:R_04.scene6.dialogue0010')
    stage.unit(119).setAction(17)
    stage.say('@dialogue:R_04.scene6.dialogue0011')
    stage.unit(119).setAction(0)
    stage.say('@dialogue:R_04.scene6.dialogue0012')
    stage.say('@dialogue:R_04.scene6.dialogue0013')
    stage.unit(119).setAction(17)
    stage.say('@dialogue:R_04.scene6.dialogue0014')
    stage.unit(119).setAction(0)
    stage.delay(5)
    stage.unit(17).setAction(0)
    stage.say('@dialogue:R_04.scene6.dialogue0015')
    stage.unit(119).setAction(3)
    stage.say('@dialogue:R_04.scene6.dialogue0016')
    stage.unit(119).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(0, 3)
    stage.say('@dialogue:R_04.scene6.dialogue0017')
    stage.unitsMove([
        [
            stage.unit(119),
            30,
            60,
            3],
        [
            stage.unit(17),
            40,
            60,
            3]])
    stage.unit(119).hide()
    stage.unit(17).move(30, 60, 3)
    stage.unit(17).hide()
    stage.delay(5)
    stage.bgSound(-1)
    stage.delay(5)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(127, 140, 83)
    stage.bottomTxt('동탁', False, True, False)
    stage.delay(5)
    stage.showHead(119, 260, 83)
    stage.bottomTxt('그리고 여포', True, False, False)
    stage.delay(5)
    stage.bottomTxt('한때 한나라의 도읍 장안에서 권력을 장악했다.', True, False, False)
    stage.delay(5)
    stage.bottomTxt('그러나 동탁이 여포의 손에 죽자,', True, True, False)
    stage.head(127).hide()
    stage.delay(5)
    stage.bottomTxt('여포도 장안에서 쫓겨났다.', True, False, False)
    stage.head(119).move(356, 83)
    stage.head(119).hide()
    stage.delay(5)
    stage.bottomTxt('동탁과 여포에 이어,', False, True, False)
    stage.showHead(131, 140, 83)
    stage.bottomTxt('이각', True, False, False)
    stage.delay(5)
    stage.showHead(129, 260, 83)
    stage.bottomTxt('그리고 곽사', True, False, False)
    stage.delay(5)
    stage.bottomTxt('장안의 정권을 장악했으나\n그들의 폭정은 예전과 조금도 다르지 않았다.', True, False, True)
    stage.bottomTxt('바로 그때,', False, True, False)
    stage.delay(5)
    stage.showHead(0, 404, 83)
    stage.bottomTxt('반동탁 연합을 떠난 조조는\n동군태수에 취임하여,', True, True, False)
    stage.delay(8)
    stage.bottomTxt('부임지는 연주의 복양이었다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene7():
    stage.bgSound(18)
    stage.loadBg(2, 27)
    stage.showUnits([
        [
            181,
            75,
            59,
            0,
            16],
        [
            202,
            76,
            55,
            3]])
    stage.unit(202).move(75, 55, 3)
    stage.showUnit(182, 75, 51, 2, 16)
    stage.draw()
    stage.setStageName('복양  조조 저택 앞')
    stage.say('@dialogue:R_04.scene7.dialogue0001')
    stage.delay(5)
    stage.showUnit(0, 43, 58, 1)
    stage.delay(5)
    stage.say('@dialogue:R_04.scene7.dialogue0002')
    stage.delay(5)
    stage.unit(181).setAction(0, 3)
    stage.unit(182).setAction(0, 3)
    stage.say('@dialogue:R_04.scene7.dialogue0003')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_04.scene7.dialogue0004')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.showUnits([
        [
            1,
            60,
            24,
            2],
        [
            16,
            60,
            16,
            2]])
    stage.say('@dialogue:R_04.scene7.dialogue0005')
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(1),
            60,
            48,
            2],
        [
            stage.unit(16),
            60,
            40,
            2]])
    stage.say('@dialogue:R_04.scene7.dialogue0006')
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_04.scene7.dialogue0007')
    stage.unit(1).setAction(0)
    stage.delay(5)
    stage.unit(1).setAction(0, 0)
    stage.say('@dialogue:R_04.scene7.dialogue0008')
    stage.unit(16).move(72, 40, 1)
    stage.delay(1)
    stage.unit(1).setAction(0, 1)
    stage.say('@dialogue:R_04.scene7.dialogue0009')
    stage.unit(16).move(72, 56, 1)
    stage.say('@dialogue:R_04.scene7.dialogue0010')
    stage.unit(181).setAction(0, 0)
    stage.say('@dialogue:R_04.scene7.dialogue0011')
    stage.unit(182).setAction(0, 2)
    stage.say('@dialogue:R_04.scene7.dialogue0012')
    stage.delay(5)
    stage.unit(16).setAction(13)
    stage.say('@dialogue:R_04.scene7.dialogue0013')
    stage.say('@dialogue:R_04.scene7.dialogue0014')
    stage.effectSound(128, 1)
    stage.unit(202).setAction(1)
    stage.delay(10)
    stage.say('@dialogue:R_04.scene7.dialogue0015')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('@dialogue:R_04.scene7.dialogue0016')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('@dialogue:R_04.scene7.dialogue0017')
    stage.delay(10)
    stage.unitsMove([
        [
            stage.unit(182),
            75,
            53,
            2],
        [
            stage.unit(181),
            75,
            59,
            0]])
    stage.unit(181).setAction(16)
    stage.unit(182).setAction(16)
    stage.delay(10)
    stage.unit(16).setAction(0, 3)
    stage.delay(3)
    stage.unit(16).move(65, 56, 3)
    stage.unit(16).setAction(0)
    stage.delay(5)
    stage.unit(16).setAction(6)
    stage.say('@dialogue:R_04.scene7.dialogue0018')
    stage.unit(16).setAction(0)
    stage.say('@dialogue:R_04.scene7.dialogue0019')
    stage.unit(16).setAction(6)
    stage.say('@dialogue:R_04.scene7.dialogue0020')
    stage.unit(16).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_04.scene7.dialogue0021')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.say('@dialogue:R_04.scene7.dialogue0022')
    stage.unit(16).setAction(6)
    stage.say('@dialogue:R_04.scene7.dialogue0023')
    stage.unit(16).setAction(0)
    stage.say('@dialogue:R_04.scene7.dialogue0024')
    stage.say('@dialogue:R_04.scene7.dialogue0025')
    stage.clsUnit()
    return False


def scene8():
    stage.loadBg(2, 12)
    stage.showUnits([
        [
            0,
            29,
            54,
            1],
        [
            1,
            46,
            40,
            2],
        [
            20,
            46,
            52,
            3],
        [
            24,
            46,
            58,
            3]])
    stage.draw()
    stage.setStageName('복양  조조 저택')
    stage.say('@dialogue:R_04.scene8.dialogue0001')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0002')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0003')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('@dialogue:R_04.scene8.dialogue0004')
    stage.delay(3)
    stage.unit(20).setAction(0, 0)
    stage.say('@dialogue:R_04.scene8.dialogue0005')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_04.scene8.dialogue0006')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_04.scene8.dialogue0007')
    stage.unit(20).setAction(0, 3)
    stage.say('@dialogue:R_04.scene8.dialogue0008')
    stage.unit(20).setAction(6, 3)
    stage.say('@dialogue:R_04.scene8.dialogue0009')
    stage.say('@dialogue:R_04.scene8.dialogue0010')
    stage.say('@dialogue:R_04.scene8.dialogue0011')
    stage.say('@dialogue:R_04.scene8.dialogue0012')
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_04.scene8.dialogue0013')
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_04.scene8.dialogue0014')
    stage.say('@dialogue:R_04.scene8.dialogue0015')
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0016')
    stage.say('@dialogue:R_04.scene8.dialogue0017')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('@dialogue:R_04.scene8.dialogue0018')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('@dialogue:R_04.scene8.dialogue0019')
    stage.say('@dialogue:R_04.scene8.dialogue0020')
    if vars[302]:
        stage.say('@dialogue:R_04.scene8.dialogue0021')
        stage.unit(24).setAction(6)
        stage.say('@dialogue:R_04.scene8.dialogue0022')
        stage.unit(24).setAction(0)
        stage.say('@dialogue:R_04.scene8.dialogue0023')
        stage.unit(1).setAction(3)
        stage.say('@dialogue:R_04.scene8.dialogue0024')
        stage.unit(1).setAction(0)
        stage.say('@dialogue:R_04.scene8.dialogue0025')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0026')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.say('@dialogue:R_04.scene8.dialogue0027')
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_04.scene8.dialogue0028')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0029')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(2)
    stage.say('@dialogue:R_04.scene8.dialogue0030')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('@dialogue:R_04.scene8.dialogue0031')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0)
    stage.delay(10)
    stage.say('@dialogue:R_04.scene8.dialogue0032')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0033')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0034')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('@dialogue:R_04.scene8.dialogue0035')
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0036')
    stage.unit(24).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(0, 1)
    stage.say('@dialogue:R_04.scene8.dialogue0037')
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0038')
    stage.unit(20).setAction(0)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('물론 받아들인다\n조금 더 지켜본다', 0)
    if sel == 1:
        stage.addAmbition(2)
        stage.bgSound(18)
        stage.delay(3)
        stage.say('@dialogue:R_04.scene8.dialogue0039')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_04.scene8.dialogue0040')
        stage.unit(20).setAction(0)
    elif sel == 2:
        stage.addAmbition(-2)
        stage.bgSound(18)
        stage.delay(3)
        stage.say('@dialogue:R_04.scene8.dialogue0041')
        stage.unit(0).setAction(0, 2)
        stage.say('@dialogue:R_04.scene8.dialogue0042')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_04.scene8.dialogue0043')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(24).setAction(6)
        stage.say('@dialogue:R_04.scene8.dialogue0044')
        stage.unit(24).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('@dialogue:R_04.scene8.dialogue0045')
        stage.unit(20).setAction(6)
        stage.say('@dialogue:R_04.scene8.dialogue0046')
        stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('@dialogue:R_04.scene8.dialogue0047')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0048')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('@dialogue:R_04.scene8.dialogue0049')
    stage.unit(24).setAction(0)
    stage.delay(5)
    stage.clsUnit()
    stage.bgSound(-1)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 397, 79)
    stage.bottomTxt('그 뒤 얼마 지나지 않아', False, False, False)
    stage.delay(5)
    stage.bottomTxt('순유의 말대로\n조조에게 황건 잔당을 토벌하라는 칙명이 내려왔다.', True, True, False)
    stage.delay(10)
    stage.showHead(187, 516, 36)
    stage.delay(5)
    stage.bottomTxt('이에 조조는 대군을 이끌고 출병했고,', True, True, False)
    stage.delay(5)
    stage.head(0).move(396, 36)
    stage.delay(5)
    stage.bottomTxt('황건군의 본거지 청주로 향했다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene9():
    model.unitJoin(16, 0, 0)
    model.unitJoin(20, 0, 0)
    model.unitJoin(24, 0, 0)
    if vars[1013]:
        gvars[0] = 16
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
        gvars[0] = 20
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
        gvars[0] = 24
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


def scene10():
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
            52,
            1],
        [
            6,
            38,
            46,
            1],
        [
            16,
            38,
            40,
            1],
        [
            20,
            60,
            52,
            3],
        [
            24,
            60,
            46,
            3],
        [
            5,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('청주  조조군 본진')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        16,
        20,
        24])
    gvars[4076] = 0
    gvars["campaign.current_chapter"] = 4
    stage.setBattlePos([
        [
            1,
            16],
        [
            2,
            17],
        [
            0,
            16],
        [
            2,
            18],
        [
            1,
            15],
        [
            3,
            17],
        [
            2,
            16],
        [
            1,
            17],
        [
            0,
            17],
        [
            1,
            18],
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


def scene11():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('@dialogue:R_04.scene11.dialogue0001')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('@dialogue:R_04.scene11.dialogue0002')
    if stage.unitClickTest(6):
        stage.say('@dialogue:R_04.scene11.dialogue0003')
    if stage.unitClickTest(16):
        stage.say('@dialogue:R_04.scene11.dialogue0004')
    if stage.unitClickTest(24):
        stage.say('@dialogue:R_04.scene11.dialogue0005')
    if stage.unitClickTest(20):
        stage.say('@dialogue:R_04.scene11.dialogue0006')
    if stage.unitClickTest(5):
        stage.say('@dialogue:R_04.scene11.dialogue0007')
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
