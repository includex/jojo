# Source Generated with Decompyle++
# File: R_15.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(17)
    stage.delay(3)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            32,
            35,
            52,
            1],
        [
            3,
            35,
            46,
            1],
        [
            20,
            63,
            52,
            3],
        [
            22,
            63,
            46,
            3]])
    stage.draw()
    stage.setEventName('안녕, 사랑하는 여인이여')
    stage.setStageName('하비 교외  조조군 본진')
    stage.showUnit(181, 49, 16, 2)
    stage.say('&181\n여포를 데려왔습니다.\n&0\n음.')
    stage.unit(181).move(49, 21, 2)
    stage.showUnit(119, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            26,
            2],
        [
            stage.unit(119),
            49,
            21,
            2]])
    stage.showUnit(182, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            52,
            2],
        [
            stage.unit(119),
            49,
            47,
            2],
        [
            stage.unit(182),
            49,
            42,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            43,
            52,
            3],
        [
            stage.unit(119),
            49,
            52,
            2],
        [
            stage.unit(182),
            49,
            47,
            2]])
    stage.delay(1)
    stage.unit(181).setAction(0, 2)
    stage.unit(182).move(55, 47, 1)
    stage.delay(1)
    stage.unit(182).move(55, 52, 2)
    stage.say('&0\n여포, 그대와 나의 싸움도 마침내 끝났군.\n&119\n……조조, 부탁이 있다.\n&119\n내 목은 뜻대로 해도 좋다.\n다만 초선만은 살려 다오.')
    if vars[302]:
        stage.say('&0\n초선이라고……?')
    if not vars[302]:
        stage.say('&0\n사도 왕윤 대인의 따님 말이오……?')
    stage.say('&119\n부탁하네………\n&0\n……좋네. 약속하겠네.\n&119\n고맙네.')
    stage.say('&119\n내 바람은 그것뿐이오.')
    stage.unit(119).setAction(3)
    stage.say('&119\n자, 나를 죽여라!')
    stage.unit(119).setAction(0)
    stage.say('&0\n여포를 형장으로 끌고 가 당장 참수하라!\n&181\n예.')
    stage.unitsMove([
        [
            stage.unit(181),
            46,
            52,
            1],
        [
            stage.unit(182),
            52,
            52,
            3]])
    stage.delay(5)
    stage.unit(119).move(49, 50, 0)
    stage.say('&119\n초선………….')
    stage.unitsMove([
        [
            stage.unit(119),
            49,
            16,
            0],
        [
            stage.unit(181),
            46,
            21,
            0],
        [
            stage.unit(182),
            52,
            21,
            0]])
    stage.unit(119).hide()
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.unit(182).move(49, 16, 0)
    stage.unit(182).hide()
    stage.delay(10)
    stage.effectSound(36, 1)
    stage.delay(10)
    stage.say('&0\n…………。')
    stage.delay(10)
    return False


def scene1():
    stage.delay(5)
    stage.showUnit(181, 49, 16, 2)
    stage.delay(3)
    stage.unit(181).move(49, 21, 2)
    stage.showUnit(17, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            26,
            2],
        [
            stage.unit(17),
            49,
            21,
            2]])
    stage.showUnit(182, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            52,
            2],
        [
            stage.unit(17),
            49,
            47,
            2],
        [
            stage.unit(182),
            49,
            42,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            43,
            52,
            3],
        [
            stage.unit(17),
            49,
            52,
            2],
        [
            stage.unit(182),
            49,
            47,
            2]])
    stage.delay(1)
    stage.unit(181).setAction(0, 2)
    stage.unit(182).move(55, 47, 1)
    stage.delay(1)
    stage.unit(182).move(55, 52, 2)
    stage.say('&181\n적장 초선을 데려왔습니다.')
    stage.say('&0\n초선이로군.\n왕 대감 댁에서 헤어진 뒤로 여러 해가 흘렀지?\n&17\n예……….')
    stage.unit(17).setAction(6)
    stage.say('&17\n조 대인, 봉선은……?\n&0\n이미 참수했다.')
    stage.unit(17).setAction(0)
    stage.say('&17\n…………。')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n봉선의 마지막 소원은\n너를 살려 달라는 것이었다.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n초선, 나를 따르거라.')
    stage.say('&17\n초선은 알고 있어요……….')
    stage.say('&17\n하지만 조 대인, 한 가지 청이 있습니다.\n들어주시겠습니까?\n&0\n무엇이냐?')
    stage.say('&17\n봉선의 수급을 제게 주실 수 있겠습니까?\n그분의 넋을 기리고 싶습니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그렇군………좋다.\n그대의 청을 들어주겠다.')
    stage.unit(17).setAction(6)
    stage.say('&17\n감사합니다, 대인.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n우선 물러가 쉬게.\n&17\n예.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n초선에게 거처를 마련해 주어라.\n&181\n예, 분부대로 하겠습니다!')
    stage.unitsMove([
        [
            stage.unit(17),
            49,
            16,
            0],
        [
            stage.unit(181),
            43,
            26,
            0],
        [
            stage.unit(182),
            55,
            26,
            0]])
    stage.unit(17).hide()
    stage.unit(181).move(49, 26, 1)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            20,
            0],
        [
            stage.unit(182),
            49,
            26,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            16,
            0],
        [
            stage.unit(182),
            49,
            22,
            0]])
    stage.unit(181).hide()
    stage.unit(182).move(49, 16, 0)
    stage.unit(182).hide()
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    model.unitJoin(17, 0, 0)
    if vars[1013]:
        gvars[0] = 17
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


def scene2():
    stage.delay(10)
    stage.showUnit(181, 49, 16, 2)
    stage.say('&181\n장료를 데려왔습니다.\n&0\n음.')
    stage.unit(181).move(49, 21, 2)
    stage.showUnit(2, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            26,
            2],
        [
            stage.unit(2),
            49,
            21,
            2]])
    stage.showUnit(182, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            52,
            2],
        [
            stage.unit(2),
            49,
            47,
            2],
        [
            stage.unit(182),
            49,
            42,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            43,
            52,
            3],
        [
            stage.unit(2),
            49,
            52,
            2],
        [
            stage.unit(182),
            49,
            47,
            2]])
    stage.delay(1)
    stage.unit(181).setAction(0, 2)
    stage.unit(182).move(55, 47, 1)
    stage.delay(1)
    stage.unit(182).move(55, 52, 2)
    stage.unit(0).setAction(3)
    stage.say('&0\n정말 기쁘네, 문원.\n자네가 내 휘하에 들어올 줄은 몰랐네.\n&0\n문원, 알고 있는가………\n나는 오래전부터 자네를 데려오고 싶었네.\n&2\n……여 장군께서는 참수되셨습니까?')
    stage.unit(0).setAction(0)
    stage.say('&0\n그렇네.\n&2\n여 장군이 숨을 거두기 전에 무슨 말을 했나?\n&0\n초선만은 놓아 달라고 했네.')
    stage.unit(2).setAction(0, 1)
    stage.say('&2\n어찌 처분하셨습니까……?\n&0\n장수로서 남긴 마지막 유언이니\n물론 들어주었네.\n&2\n그러셨습니까……?')
    stage.unit(2).setAction(0, 2)
    stage.delay(3)
    stage.unit(2).setAction(6)
    stage.say('&2\n그렇다면 이 장료, 더는 말하지 않겠습니다.\n앞으로 승상을 위해 견마지로를 다하겠습니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n음!\n문원, 그 말을 기다렸네.\n&2\n예. (여포 장군,\n초선의 일은 제게 맡겨 주십시오.\n부디 편히 쉬십시오……….)')
    stage.unit(0).setAction(0)
    stage.say('&0\n그만하면 되었다. 그대도 피곤하겠지.\n어서 물러가 쉬도록 하게.\n&2\n예.')
    stage.unit(2).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(2),
            49,
            16,
            0],
        [
            stage.unit(181),
            43,
            26,
            0],
        [
            stage.unit(182),
            55,
            26,
            0]])
    stage.unit(2).hide()
    stage.unit(181).move(49, 26, 1)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            20,
            0],
        [
            stage.unit(182),
            49,
            26,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            16,
            0],
        [
            stage.unit(182),
            49,
            22,
            0]])
    stage.unit(181).hide()
    stage.unit(182).move(49, 16, 0)
    stage.unit(182).hide()
    stage.bgSound(-1)
    stage.effectSound(203, 0)
    stage.delay(3)
    model.unitJoin(2, 0, 0)
    if vars[1013]:
        gvars[0] = 2
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
    stage.say('&0\n현덕 공.')
    stage.unit(32).move(49, 52, 1)
    stage.delay(3)
    stage.unit(32).setAction(0, 2)
    stage.say('&32\n예.')
    stage.say('&0\n그대의 병사들도 나와 함께 허도로 돌아가면 좋겠네.')
    stage.say('&0\n나와 함께 폐하를 알현해\n역적을 토벌한 대승을 아뢰도록 하세.')
    stage.say('&32\n폐하를 알현하라고요……?')
    stage.say('&32\n알겠습니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n좋다, 모두 수고했다.\n오늘은 푹 쉬고\n내일 허도로 개선하자.')
    stage.unit(0).setAction(0)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(19)
    stage.delay(3)
    stage.loadBg(2, 61)
    stage.showUnit(0, 55, 61, 3)
    stage.draw()
    stage.setStageName('허도 승상부 정원')
    stage.showUnit(20, 90, 69, 3)
    stage.unit(20).move(61, 69, 3)
    stage.unit(20).setAction(0)
    stage.say('&20\n주군.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n문약인가?\n아직 쉬지 않고 있었나?')
    stage.unit(20).setAction(6)
    stage.say('&20\n주군께서는 일찍 쉬셔야 합니다…….')
    stage.unit(20).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n잠이 오지 않는구나.')
    stage.unit(20).setAction(6)
    stage.say('&20\n아직도 여포를 생각하고 계십니까?')
    stage.unit(20).setAction(0)
    stage.delay(8)
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n……문약, 그대는 어찌 생각하는가?\n정말 그것이 여포의 마지막 소원이었을까?')
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n이 사람은………\n오직 초선만을 생각했군.\n그저 여인 하나를 위해서!\n&0\n그 때문에 모든 것을 잃었을 뿐 아니라\n장수로서 품었던 이상마저 잃고 말았구나…….')
    stage.unit(20).setAction(6)
    stage.say('&20\n주군, 장수가 정의를 위해 살아간다면\n자신이 사랑하는 이를 위해 싸울 수도 있는 법입니다.')
    stage.unit(20).setAction(0)
    stage.say('&20\n그래서 저는 여포가 행복한 사람이었다고 생각합니다.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n오직 사랑하는 이를 위해 싸운다는 것인가?\n음, 내게는 어려운 일이로군.')
    stage.unit(20).setAction(6)
    stage.say('&20\n사람마다 뜻이 다른 법입니다.')
    stage.unit(20).setAction(0)
    stage.delay(8)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n그래……과연 그렇군.\n여포는 자신의 뜻을 위해 죽었다.\n나 역시 내 뜻을 위해 싸워 나가겠다.')
    stage.delay(8)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(16)
    stage.delay(3)
    stage.sectionName(2, '하북의 쟁탈전')
    stage.loadBg(1, 0)
    stage.draw()
    stage.setEventName('유비를 공격한다')
    stage.delay(10)
    stage.showHead(0, 496, 124)
    stage.showHead(32, 496, 204)
    stage.showHead(119, 560, 164)
    stage.delay(5)
    stage.bottomTxt('천하무쌍한 여포는 하비에서 목숨을 잃었고,', False, False, False)
    stage.delay(5)
    stage.head(119).hide()
    stage.delay(10)
    stage.head(0).move(300, 116)
    stage.head(32).move(364, 116)
    stage.bottomTxt('조조와 유비는 허도로 개선하여 헌제를 알현했다.', True, True, False)
    stage.delay(12)
    stage.showHead(149, 332, 36)
    stage.delay(5)
    stage.bottomTxt('헌제는 유비가 한 황실의 종친임을 알고 크게 기뻐하며 황숙이라 불렀다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene5():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            32,
            50,
            40,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.showUnit(25, 95, 55, 3)
    stage.say('&25\n아뢰옵니다.')
    stage.unit(25).move(48, 55, 3)
    stage.unit(25).setAction(0)
    stage.delay(3)
    stage.unit(25).setAction(6)
    stage.say('&25\n이 만총이 실례하겠습니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n오, 백녕.\n각지의 정세를 살피느라 참으로 수고했네.')
    stage.say('&25\n감사합니다, 주군.')
    stage.unit(0).setAction(0)
    stage.say('&0\n어떠한가?\n각지에 무슨 움직임이 있는가……?')
    stage.unit(25).setAction(0)
    stage.say('&25\n주군께 아룁니다.')
    stage.say('&25\n북평의 공손찬은 원소에게 멸망했습니다.')
    stage.unit(32).setAction(3)
    stage.say('&32\n뭐라고?\n고……공손찬이 멸망했다고……!?')
    stage.unit(25).setAction(0, 0)
    stage.say('&25\n예.')
    stage.unit(32).setAction(0)
    stage.say('&32\n그런가…………?')
    stage.say('&0\n그러고 보니\n유 대인께서는 한때 공손찬에게 의탁하셨지요?\n&32\n그렇습니다.\n흥한 자는 반드시 쇠한다 하나\n그의 불행은 참으로 마음 아픈 일입니다.\n&0\n음.')
    stage.say('&0\n백녕, 다른 상황은 어떠한가?')
    stage.unit(25).setAction(0, 3)
    stage.say('&25\n여남의 원술이 북상하려 합니다.\n&0\n원술이라고?')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n아마 원소에게 의탁하려는 것이겠지?\n&25\n그럴 것입니다……….')
    stage.unit(0).move(34, 59, 2)
    stage.say('&0\n곤란하군. 원술은 대수롭지 않지만\n원소의 힘이 커지면 상대하기 어렵다.\n&32\n………….')
    stage.unit(0).move(34, 55, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n결코 원술을 북상하게 두어서는 안 된다.')
    stage.unit(0).setAction(17, 1)
    stage.say('&0\n원소에게 의탁하기 전에\n반드시 그를 제거해야 한다!')
    stage.unit(32).setAction(6, 2)
    stage.say('&32\n잠시 기다려 주시오.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음?\n현덕 공, 무슨 일이오?')
    stage.unit(32).setAction(0, 2)
    stage.say('&32\n조 승상, 제가 원술을 토벌하러 가도 되겠습니까?\n&0\n그대가?\n&32\n그렇습니다.')
    stage.say('&32\n원술과 원소는 한 집안이오.\n원술을 없애면 원소에게도 타격을 줄 수 있으니\n백규의 원수를 갚는 셈이기도 하오.\n&0\n음, 그렇군요……….')
    stage.effectSound(200, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('유비에게 맡긴다\n유비에게 수고를 끼치지 않는다', 0)
    if sel == 1:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.unit(0).setAction(3, 1)
        stage.say('&0\n좋다! 그대에게 맡기겠네.\n&32\n감사합니다, 승상.')
    elif sel == 2:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.say('&0\n현덕, 수고할 것 없네.\n이제 그대는 황숙이 되었고\n폐하께서도 그대를 아끼시지 않는가.\n&0\n그대가 원술 토벌에 나서면\n폐하께서 적적해하실 것이네.\n&32\n아닙니다! 부디 이 유비에게 맡겨 주십시오.\n&32\n적은 참칭 황제 원술입니다. 제가 토벌한다면\n폐하께서도 틀림없이 기뻐하실 것입니다.\n&0\n음……… 좋네.\n그토록 뜻이 굳다면\n그대에게 맡기겠네.\n&32\n감사합니다, 승상.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n시간이 없으니 군사와 군량을 빌려주겠소.\n즉시 출발하시오.')
    stage.unit(32).setAction(6, 2)
    stage.say('&32\n예, 즉시 준비하겠습니다.\n&0\n음.')
    stage.effectSound(200, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 323, 100)
    stage.showHead(32, 387, 100)
    stage.delay(8)
    stage.showHead(136, 351, 188)
    stage.bottomTxt('원술은 여남에서 재기하지 못했고,', False, False, False)
    stage.delay(5)
    stage.head(136).move(451, 188)
    stage.head(136).move(479, 56)
    stage.bottomTxt('서형인 원소에게 몸을 의탁하러 갔다.', True, False, False)
    stage.delay(5)
    stage.head(32).move(415, 56)
    stage.bottomTxt('그러나 북상하던 도중', True, True, False)
    stage.delay(5)
    stage.head(136).hide()
    stage.delay(5)
    stage.bottomTxt('조조에게 군사를 빌린 유비에게 죽임을 당했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('원술이 죽은 뒤, 유비는…….', True, True, True)
    stage.bgSound(-1)
    return False


def scene6():
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
            18,
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
    stage.showUnit(181, 95, 55, 3)
    stage.say('&181\n보고드립니다!')
    stage.unit(181).move(48, 55, 3)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 방금 보고가 들어왔습니다.\n유비군이 서주를 점령했다고 합니다.')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('&0\n뭐라고!?\n&22\n유비가 서주를 빼앗았다고……?\n&181\n게다가 원소와 동맹을 맺었습니다.\n&21\n원술을 죽이고도\n그 형인 원소와 동맹을 맺다니\n참으로 교묘한 외교 수완이로군요.')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(2)
    stage.say('&0\n유비, 괘씸한 놈!\n감히 나를 속이다니!\n은혜를 원수로 갚는구나!')
    stage.unit(0).move(30, 55, 3)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('&0\n이 분을 삭일 수가 없구나!\n당장 서주를 되찾겠다!\n출병하라!')
    stage.unit(20).setAction(6)
    stage.say('&20\n잠깐만요.')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('&0\n문약, 무슨 일인가!?\n&20\n주군, 지금 출병하는 것은 너무도 위험합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n그렇습니다.\n먼저 주변의 방비를 굳혀야 합니다.')
    stage.unit(24).setAction(0)
    stage.delay(5)
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n주변이라…… 장수를 말하는가?')
    stage.unit(20).setAction(6)
    stage.say('&20\n그렇습니다. 장수와 동맹을 맺는 것이 어떻겠습니까?\n주군의 뜻은 어떠하십니까?\n&0\n장수와 동맹을?\n&20\n그렇습니다.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n음………….')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n좋다. 그것이 최선일 듯하군.')
    stage.unit(0).move(34, 55, 1)
    stage.say('&0\n자양!')
    stage.unit(18).move(62, 70, 3)
    stage.delay(1)
    stage.unit(18).move(62, 55, 0)
    stage.delay(3)
    stage.unit(18).setAction(0, 3)
    stage.say('&18\n예.\n&0\n그대를 사신으로 완성에 보내겠다.\n장수와 동맹을 맺고 오라.')
    stage.unit(18).setAction(6, 3)
    stage.say('&18\n예.')
    stage.clsUnit()
    stage.effectSound(202, 255)
    return False


def scene7():
    stage.loadBg(2, 48)
    stage.showUnits([
        [
            23,
            39,
            48,
            1],
        [
            18,
            53,
            48,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.setStageName('완성  장수 저택')
    stage.delay(3)
    stage.unit(18).setAction(6, 3)
    stage.say('&18\n조조 대인께서 보내신 사자 유엽이라 합니다.\n&23\n저는 가후라 합니다.')
    stage.unit(18).setAction(0)
    stage.say('&18\n가후 선생, 장수 대인께서는……?\n&23\n주군께서는 군사를 사열하고 계십니다.\n제게 말씀하셔도 됩니다.\n&18\n그렇습니까……?\n&23\n무슨 일이십니까?')
    stage.unit(18).setAction(6)
    stage.say('&18\n장수 대감과 동맹을 맺고자 왔습니다.\n&23\n오, 우리와 동맹을……?\n&18\n그렇습니다.')
    stage.unit(18).setAction(0)
    stage.say('&18\n저희 주군께서는 지난 불화를 모두 잊고\n전쟁을 끝내 화목을 되찾고자 하십니다.\n장수 대인과 손을 맞잡고 화해하시기를 바라십니다.')
    stage.unit(23).setAction(0, 2)
    stage.say('&23\n음…………\n&18\n어떻게 생각하십니까?')
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n아, 알겠습니다.\n반드시 주군께 전하고\n내일 답을 드리겠습니다.')
    stage.unit(18).setAction(6)
    stage.say('&18\n지금 답을 주실 수는 없습니까?')
    stage.unit(23).setAction(6)
    stage.say('&23\n저는 일개 부하이니 주군과 상의하기 전에는\n답을 드리기 어렵습니다. 기다려 주십시오.\n&18\n그렇습니까?\n좋습니다.\n내일 다시 와서 답을 기다리겠습니다.\n&23\n그러시지요.')
    stage.showUnit(181, 53, 93)
    stage.say('&181\n아룁니다.')
    stage.unit(23).setAction(0, 2)
    stage.unit(18).setAction(0, 2)
    stage.unit(181).move(53, 80, 0)
    stage.say('&181\n가후 선생,\n하북의 원소가 사자를 보냈습니다.')
    stage.unit(18).move(53, 50, 2)
    stage.say('&18\n오!\n&23\n그렇습니까? 모셔 오십시오.')
    stage.unit(181).move(53, 93, 2)
    stage.unit(181).hide()
    stage.delay(3)
    stage.unit(18).setAction(0, 3)
    stage.say('&18\n가……가후 선생, 이것은…….')
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n아, 괘념치 마십시오.\n객실에서 쉬시지요.\n&18\n예, 그러겠습니다…….')
    stage.unit(18).setAction(0, 2)
    stage.say('&18\n(큰일이군.\n원소도 틀림없이 장수와 동맹을 맺으려 할 것이다.)')
    stage.unit(18).move(53, 93, 2)
    stage.unit(18).hide()
    stage.showUnit(192, 53, 93)
    stage.say('&192\n실례했습니다.')
    stage.unit(192).move(53, 48, 0)
    stage.delay(3)
    stage.unit(192).setAction(0, 3)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n저는 군사 가후입니다.\n원소께서 보내신 사자이시지요?')
    stage.unit(23).setAction(0)
    stage.say('&192\n그렇습니다.\n&23\n무슨 일로 오셨습니까?\n&192\n예.\n저희 주군께서는 장수 대감과 동맹을 맺기를 원하십니다.\n&23\n동맹이라고요? 저희 주군과 말입니까?\n&192\n그렇습니다…….')
    stage.unit(23).setAction(0, 3)
    stage.say('&23\n으으음……\n&23\n하하하하!')
    stage.say('&192\n이……어디가 우습다는 말씀이십니까?')
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n하하하!\n웃지 않을 수 있겠습니까?\n원소 따위가 감히 우리 주군과 동맹을 맺으려 하다니!')
    stage.unit(192).setAction(2)
    stage.say('&192\n뭐……뭐라고요!\n그런 말씀은 도무지 믿을 수 없습니다!\n&23\n분명히 말씀드리지요.\n우리 주군께서는 원소 같은 자와 동맹을 맺지 않으십니다.\n그만 돌아가십시오!')
    stage.unit(192).setAction(0)
    stage.say('&192\n아직 장수 대인을 만나지 못했습니다.')
    stage.unit(23).setAction(0, 3)
    stage.say('&23\n그런 사소한 일까지\n저희 주군께서 신경 쓰실 리 없지요.')
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n그만 돌아가시오!')
    stage.unit(192).setAction(2)
    stage.say('&192\n으윽……그대……반드시 후회할 것이오!')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).move(53, 93, 2)
    stage.unit(192).hide()
    stage.delay(3)
    stage.unit(23).setAction(0, 2)
    stage.say('&23\n흥, 그렇게 말하면 되겠군.')
    stage.delay(10)
    stage.showUnit(143, 53, 93)
    stage.say('&143\n문화!')
    stage.unit(143).move(53, 48, 0)
    stage.delay(3)
    stage.unit(143).setAction(0, 3)
    stage.delay(3)
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n주군, 일찍 돌아오셨군요.\n&143\n문화, 무슨 생각으로 그런 짓을 했는가!?\n&23\n예?')
    stage.unit(143).setAction(2)
    stage.say('&143\n시치미 떼지 마라!\n조조의 사자는 극진히 대접하고\n원소의 사자는 쫓아냈지 않은가!\n&23\n그렇습니다.\n&143\n나는 그를 거절할 생각이 없었네!\n문화, 대체 무슨 수작인가?\n&23\n부디 진정하십시오.\n저는 주군의 퇴로를 마련한 것뿐입니다.')
    stage.unit(23).setAction(0, 3)
    stage.say('&23\n이대로 홀로 버티다가는\n난세에서 살아남기 어려울 것입니다.\n누군가와 손을 잡을 수 있다면…….')
    stage.unit(143).setAction(0)
    stage.say('&143\n나도 알고 있다!\n허나 어째서 원소가 아니라 조조란 말인가?\n세력은 원소가 훨씬 강하지 않느냐.')
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n지금의 원소는 분명 국력이 강합니다.\n&23\n하지만 이삼 년 뒤에도 그럴까요?\n미래를 내다보신다면\n지금은 조조를 택하셔야 합니다.')
    stage.unit(143).setAction(2)
    stage.say('&143\n잊었는가!?\n우리는 조씨 집안사람을 죽였네!\n&23\n조조는 모두 지난 일로 돌리겠다고 했습니다.')
    stage.unit(23).setAction(0, 2)
    stage.say('&23\n그 너그러운 도량만으로도\n원소는 주군께 미치지 못합니다.')
    stage.unit(143).setAction(0)
    stage.say('&143\n조조가 훗날 정말 원소보다 강해진단 말인가……?')
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n틀림없습니다.\n&143\n좋다. 이제 다른 길도 없군!\n그대를 믿겠다.\n&23\n예.')
    stage.unit(23).setAction(6, 1)
    stage.say('&23\n그리고 지금은 조조군에 들어가야 한다고 생각합니다.')
    stage.unit(143).setAction(2)
    stage.say('&143\n뭐라고!? 그의 부하가 되라고?\n&23\n큰 나무 아래라야 그늘도 넉넉한 법이지요.')
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n쉽게 말해 우리는 세력이 너무 약하여\n이 난세에서 살아남기 어렵습니다.')
    stage.unit(143).setAction(0)
    stage.say('&143\n………좋다. 그대 말은 틀림없겠지.\n조조에게 의탁하겠다.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene8():
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
            6,
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
            2],
        [
            143,
            54,
            58,
            3,
            6],
        [
            23,
            54,
            52,
            3,
            6],
        [
            18,
            48,
            55,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.setStageName('허도  승상부')
    stage.unit(0).setAction(3)
    stage.say('&0\n장 장군, 마침내 오셨군.\n&143\n그렇습니다.\n&0\n그대 같은 인물이 도우러 와 주었으니\n이제 마음이 놓이네.')
    stage.unit(0).setAction(0)
    stage.say('&0\n자양, 수고했네.\n자네에게 큰 공을 기록하겠네.')
    stage.unit(18).setAction(6)
    stage.say('&18\n과찬이십니다, 승상.')
    stage.unit(18).setAction(0)
    stage.say('&0\n예전처럼 완성을 다스려 주기 바라네.\n&143\n참으로 감사드립니다.')
    stage.unit(143).setAction(0)
    stage.say('&143\n그럼 즉시 완성으로 돌아가겠습니다.\n&0\n음.')
    stage.unit(143).setAction(6)
    stage.say('&143\n그럼 물러가겠습니다.\n&0\n문화, 자네에게는 더 할 말이 있네.\n남아 있게.\n&23\n예.')
    stage.unit(143).setAction(0)
    stage.delay(3)
    stage.unit(143).move(95, 58, 1)
    stage.unit(143).hide()
    stage.say('&0\n문화, 내 진영에 남아 그 재능을 펼쳐 주지 않겠는가?\n자네를 중히 쓰겠네.')
    stage.effectSound(202, 255)
    stage.bgSound(18)
    stage.delay(3)
    stage.say('&23\n승상의 뜻에 따르겠습니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n오, 문화. 남아 주기로 했는가?\n&23\n예.')
    model.setFace(3)
    stage.say('&0\n하하하, 문화.\n자네 같은 인재를 얻었으니\n나라 하나를 얻은 것이나 다름없네!\n&23\n과찬이십니다, 승상.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n문약, 이제 유비를 토벌할 수 있겠군.')
    stage.unit(20).setAction(6)
    stage.say('&20\n그렇습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다! 서주로 출병하라!\n유비를 토벌한다!')
    model.unitJoin(23, 0, 0)
    if vars[1013]:
        gvars[0] = 23
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
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 338, 94)
    stage.showHead(143, 274, 139)
    stage.showHead(32, 492, 119)
    stage.bottomTxt('완성의 장수가 조조의 휘하에 들어갔다.', False, False, False)
    stage.delay(5)
    stage.head(143).hide()
    stage.delay(8)
    stage.bottomTxt('조조는 즉시 대군을 동원하여,', True, True, False)
    stage.delay(5)
    stage.head(0).move(428, 119)
    stage.delay(5)
    stage.bottomTxt('서주로 출병하여 유비를 토벌했다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene9():
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
            17,
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
            23,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('하비 교외  조조군 본진')
    stage.say('&0\n문약, 원소에게 무슨 움직임이 있는가?')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n유비를 지원할 생각은 없는 모양입니다.\n&0\n귀 큰 도적의 운도 다했군.')
    stage.unit(0).setAction(17, 0)
    stage.say('&0\n좋다, 출발하라!')
    stage.unit(0).setAction(0, 0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        2,
        17,
        22,
        23,
        20])
    gvars[4076] = 0
    gvars[4057] = 15
    stage.setBattlePos([
        [
            1,
            2],
        [
            3,
            4],
        [
            2,
            3],
        [
            2,
            1],
        [
            0,
            3],
        [
            0,
            1],
        [
            4,
            3],
        [
            2,
            5],
        [
            3,
            2],
        [
            1,
            4],
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


def scene10():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(23):
        stage.say('&23\n저는 봉효 선생과 같은 도사입니다.\n&23\n문원 장군은 기병대를 이끄십니다만\n지금 이곳에는 계시지 않습니다.')
    if stage.unitClickTest(20):
        stage.say('&20\n강을 사이에 둔 두 성이 전장입니다.\n&0\n두 성이라……?\n&20\n예. 북쪽은 서주성, 남동쪽은 하비성입니다.')
    if stage.unitClickTest(17):
        stage.say('&17\n저는 무희예요.\n&15\n무희라고요?\n음, 어떤 부대입니까?\n&17\n무술가와 비슷해요, 허저 장군.\n&15\n저……저한테 허저 장군이라고 부르지 마십시오!\n하하하.')
    if stage.unitClickTest(15):
        stage.say('&0\n허저.\n&15\n으음……\n&1\n이봐, 허저.\n&17\n허저 장군…….\n&15\n으음………\n&0\n허저!\n&15\n에…… 에취!\n예? 죄, 죄송합니다.\n&0\n……됐다, 됐어.')
    if stage.unitClickTest(1):
        stage.say('&1\n도구점에 인수가 들어온 모양이야.\n&1\n일정 레벨에 도달한 부대에게 쓰면\n더 강력한 병종으로 승급할 수 있다더군.')
    if stage.unitClickTest(22):
        stage.say('&22\n제가 원소라면\n허도를 공격할 기회를 놓치지 않을 것입니다.\n&22\n원소는 참으로 천시를 놓치는군요.')
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
