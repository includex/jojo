# Source Generated with Decompyle++
# File: R_19.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(18)
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
            35,
            54,
            1],
        [
            20,
            63,
            54,
            3],
        [
            22,
            63,
            48,
            3],
        [
            21,
            63,
            42,
            3]])
    if not vars[718]:
        stage.showUnits([
            [
                7,
                49,
                54,
                2],
            [
                181,
                45,
                48,
                2],
            [
                182,
                53,
                48,
                2],
            [
                15,
                35,
                48,
                1]])
    stage.draw()
    stage.setEventName('천명')
    stage.setStageName('관도  조조군 본진')
    stage.delay(3)
    if not vars[718]:
        stage.info('조조는 관도대전에서 승리하여\n원소군의 맹장 장합을 사로잡았다.')
        stage.unit(0).setAction(3)
        stage.say('&0\n장 장군, 그대의 뛰어난 무예에\n이 조조도 깊이 탄복했네.')
        stage.unit(0).setAction(0)
        stage.say('&0\n어떤가…내 휘하에 들어오지 않겠는가?\n&7\n두 주인을 섬길 생각은 없었습니다……\n&20\n장 장군께서 주군께 충성을 다하고 싶으셔도\n이제 원소의 곁으로 돌아가실 수는 없습니다…….')
        stage.unit(7).setAction(0, 1)
        stage.say('&7\n뭐라고! 무슨 뜻이오?\n&20\n방금 원소군에 잠입한 밀정의 보고를 받았습니다……\n&20\n군사 곽도가 이번 패전의 책임을\n모두 그대에게 돌렸다고 합니다.\n&7\n뭐라고! 가증스러운 놈……\n&20\n제가 드리는 말씀은 모두 사실입니다.')
        stage.unit(7).setAction(0, 2)
        stage.say('&7\n괘씸한 놈! 곽도는 본래 그런 소인배요.\n허나 주군께서 그 말을 믿으실 리는……\n&20\n정말 그렇게 생각하십니까?\n&20\n원소가 한 일을 다시 돌이켜 보십시오.\n간언한 전풍과 저수를 어찌 대했는지 말입니다.')
        stage.unit(7).setAction(0, 1)
        stage.say('&7\n…………\n&0\n……장 장군.\n그런 참언에 모함당해 죽고 싶지는 않겠지?')
        stage.unit(7).setAction(0, 2)
        stage.say('&0\n나를 위해 일하며\n공을 세우게.')
        stage.unit(7).setAction(6)
        stage.say('&7\n……좋습니다.\n조 승상을 위해 견마지로를 다하겠습니다.')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n오, 받아들이겠다는 말인가!\n&7\n예.')
        stage.unit(0).setAction(0)
        stage.say('&0\n음, 장 장군. 참으로 기쁘네!\n그대 같은 맹장이 우리 군에 합류하다니…….\n큰 활약을 기대하겠네.\n&7\n감사합니다, 주군.')
        model.setFace(0)
        stage.unit(7).setAction(0)
        stage.unitsMove([
            [
                stage.unit(181),
                45,
                36,
                0],
            [
                stage.unit(182),
                53,
                36,
                0],
            [
                stage.unit(7),
                49,
                42,
                0]])
        stage.delay(1)
        stage.unitsMove([
            [
                stage.unit(7),
                35,
                42,
                3],
            [
                stage.unit(181),
                45,
                26,
                0],
            [
                stage.unit(182),
                53,
                26,
                0]])
        stage.delay(3)
        stage.unit(7).setAction(0, 1)
        stage.unit(181).move(49, 26, 1)
        stage.delay(1)
        stage.unitsMove([
            [
                stage.unit(181),
                49,
                22,
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
                20,
                0]])
        stage.unit(181).hide()
        stage.unit(182).move(49, 16, 0)
        stage.unit(182).hide()
        model.unitJoin(7, 0, 0)
        if vars[1013]:
            gvars[0] = 7
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
    if vars[718]:
        stage.showUnit(15, 49, 16, 2)
        stage.say('&15\n주군, 장합이 뵙기를 청합니다.')
        model.setFace(3)
        stage.unit(0).setAction(3)
        stage.say('&0\n오, 왔는가!\n마침 기다리던 참이네.')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(0, 0)
        stage.say('&15\n들어오시오.\n&7\n예.')
        stage.unit(15).move(49, 48, 2)
        stage.showUnit(7, 49, 16, 2)
        stage.say('&7\n대감을 뵙습니다.')
        stage.unitsMove([
            [
                stage.unit(15),
                35,
                48,
                1],
            [
                stage.unit(7),
                49,
                54,
                2]])
        stage.unit(15).setAction(0)
        stage.unit(7).setAction(0)
        stage.say('&0\n장 장군, 마침내 왔구려!\n그대의 이야기는 허저에게 모두 들었소.\n&0\n다시 한 번 묻겠소.\n내 휘하로 들어올 생각이 있소?')
        stage.unit(7).setAction(6)
        stage.say('&7\n승상을 위해 일하기로 결심했습니다.')
        stage.unit(7).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(3)
        stage.say('&15\n그럼 이제 우리는 동료로군!?')
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(7).setAction(0, 3)
        stage.say('&7\n그때 허저 장군은 저를 죽여 공을 세울 수 있었으나\n그냥 놓아주었습니다.\n&7\n그 의리에 보답하고자 하니\n제가 할 수 있는 일은 이것뿐입니다.')
        stage.unit(7).setAction(0, 2)
        stage.delay(3)
        stage.unit(7).setAction(6)
        stage.say('&7\n승상, 이 장합은 덕도 능력도 부족하나\n곁에서 시중들며 섬기게 해 주십시오.')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n오, 잘 말했네!\n마음껏 실력을 발휘해 보게.')
        stage.unit(0).setAction(0)
        stage.say('&7\n예.')
        stage.unit(7).setAction(0)
        stage.say('&0\n허저, 잘했다.')
        stage.unit(15).setAction(3)
        stage.say('&15\n하하하.')
        stage.unit(15).setAction(0)
        stage.unit(7).move(49, 42, 0)
        stage.unit(7).move(35, 42, 1)
        model.setFace(0)
        model.unitJoin(7, 0, 0)
        if vars[1013]:
            gvars[0] = 7
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
    stage.bgSound(-1)
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.delay(10)
    stage.showUnit(181, 49, 16, 2)
    stage.say('&181\n보고드립니다!')
    stage.unit(181).move(49, 26, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 감옥에서 원소의 군사를 발견했습니다.\n&0\n원소의 군사라고?\n이름이 무엇이냐?\n&181\n저수라 합니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n저수!?')
    stage.unit(0).setAction(0)
    stage.say('&0\n옥에 갇혀 있다고 하더군……\n&0\n좋다! 데려오너라…정중히 모셔 오도록 하라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.delay(10)
    stage.showUnit(181, 49, 16, 2)
    stage.say('&181\n주군, 저수 선생을 모셔 왔습니다.\n&0\n음, 수고했다.')
    stage.unit(181).move(49, 22, 2)
    stage.showUnit(105, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            48,
            2],
        [
            stage.unit(105),
            49,
            42,
            2]])
    stage.say('&105\n음?')
    stage.unit(105).setAction(0, 3)
    stage.effectSound(208, 255)
    stage.bgSound(13)
    stage.delay(3)
    stage.say('&7\n저수 선생…….\n&105\n장합, 조조군에 합류했는가?\n&7\n그렇습니다…….\n&105\n그런가…….')
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(181),
            49,
            54,
            2],
        [
            stage.unit(105),
            49,
            48,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(181),
            45,
            54,
            3],
        [
            stage.unit(105),
            49,
            54,
            2]])
    stage.delay(1)
    stage.unit(181).setAction(0, 2)
    stage.say('&0\n저수, 그대가 지략이 뛰어나다 들었네.')
    stage.unit(0).setAction(3)
    stage.say('&0\n어떠한가? 장 장군과 함께 나를 위해 일해 주겠는가?')
    stage.unit(0).setAction(0)
    stage.say('&105\n………。')
    stage.unit(0).setAction(3)
    stage.say('&0\n어떻소?\n&105\n……조 승상께 아룁니다.')
    stage.unit(0).setAction(0)
    stage.say('&0\n음?\n&105\n관도대전이 일어나기 전, 밤하늘을 살피다가\n허도 상공에 빛나는 별 하나를 보았습니다.\n&105\n그것은 승상의 별이자\n제왕의 별이었습니다…….')
    stage.unit(105).setAction(3)
    stage.say('&105\n조 승상, 그대는 천하를 통일할 분이오.')
    stage.unit(105).setAction(0, 2)
    stage.delay(3)
    stage.unit(105).setAction(0, 0)
    stage.delay(3)
    stage.say('&105\n게다가…원소 주군의 별빛은 갈수록 약해져\n머지않아 사라질 것입니다.\n&0\n저수, 그렇다면\n나를 위해 일해 주시오.')
    stage.unit(105).setAction(0, 2)
    stage.say('&105\n아닙니다, 조 승상.\n&105\n원소 주군의 주성 곁에 있는\n제 빛나는 별도 기수가 다해 갑니다.')
    stage.unit(105).setAction(6)
    stage.say('&105\n저는 주군 원소와 함께 사라질 것입니다.\n그것은 거스를 수 없는 하늘의 뜻입니다.')
    stage.unit(105).setAction(0)
    stage.delay(3)
    stage.unit(7).move(49, 42, 1)
    stage.delay(1)
    stage.unit(7).setAction(0, 2)
    stage.say('&7\n저수 선생, 하지만…….')
    stage.delay(3)
    stage.unit(105).setAction(0, 0)
    stage.say('&105\n장 장군, 그대의 별은 갈수록 밝아지는군.\n&105\n승상을 위해 일하게.\n그러면 그 별은 빛을 잃지 않을 것이네.\n&7\n저수 선생……\n&20\n저수 선생, 그 뛰어난 지략을\n이대로 헛되이 묻어 버리시겠습니까?\n너무도 아깝지 않습니까……?')
    stage.unit(105).setAction(0, 1)
    stage.say('&105\n문약 선생이시군요?\n존함은 익히 들어 알고 있습니다.\n&105\n선생께는 제왕을 보좌할 왕좌지재가 있으나\n제게는 그런 재주가 없습니다.\n&20\n저수 선생……\n&0\n저수, 무슨 일이 있어도……\n나를 위해 일할 생각은 없소?')
    stage.unit(105).setAction(0, 2)
    stage.say('&105\n조 승상, 저는 언제나 천명을 따르는 사람입니다.\n그러니 주군 원소를 위해 목숨을 바치겠습니다.')
    stage.unit(105).setAction(6)
    stage.say('&105\n자! 내 목을 베시오!\n&0\n……좋다.\n뜻대로 해 주겠다.\n&0\n……저수를 형장으로 데려가라.\n&181\n예.')
    stage.unit(181).setAction(0, 1)
    stage.delay(3)
    stage.unit(105).setAction(0)
    stage.say('&0\n저수, 그대의 가족은 내가 돌보겠소.\n&105\n감사합니다, 승상.')
    stage.unitsMove([
        [
            stage.unit(105),
            49,
            50,
            0],
        [
            stage.unit(181),
            49,
            54,
            1]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(7),
            41,
            42,
            3],
        [
            stage.unit(105),
            49,
            42,
            0],
        [
            stage.unit(181),
            49,
            46,
            0]])
    stage.delay(1)
    stage.unit(7).setAction(0, 1)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(105),
            49,
            16,
            0],
        [
            stage.unit(181),
            49,
            20,
            0]])
    stage.delay(1)
    stage.unit(7).setAction(0, 0)
    stage.delay(3)
    stage.unit(105).hide()
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.delay(10)
    stage.say('&0\n천명이라……?')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 72)
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.showUnits([
        [
            101,
            70,
            98],
        [
            113,
            66,
            98],
        [
            102,
            74,
            98]])
    stage.setStageName('관도 근교  강가')
    stage.unitsMove([
        [
            stage.unit(113),
            66,
            88,
            0],
        [
            stage.unit(102),
            74,
            88,
            0]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(101),
            70,
            60,
            0],
        [
            stage.unit(113),
            66,
            50,
            0],
        [
            stage.unit(102),
            74,
            50,
            0]])
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('&101\n내 대군이……\n이토록 거대한 군대가 패하다니!')
    stage.unit(102).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(0, 2)
    stage.say('&113\n아버님, 지난 일은 이미 지나갔습니다.\n제 여양성으로 가시지요.\n그곳이 더 안전합니다.')
    stage.unit(101).setAction(0)
    stage.say('&101\n음, 음……\n&101\n전풍의 말이 옳았군.\n그때 그 말을 들었더라면\n오늘의 패배는 없었을 것이다.\n&102\n주군, 전풍도 그리 생각할 것입니다.\n&101\n뭐라고……?\n&102\n주군께서 패하셨다는 것을 알고\n분명 우쭐해하고 있겠지요.\n&101\n으음, 그것도 사실이군…….')
    stage.unit(101).setAction(2)
    stage.say('&101\n봉기, 그놈을 베어 버려라!\n살려 둘 수 없다!\n&102\n예!\n즉시 처리하겠습니다.')
    stage.unit(113).setAction(3)
    stage.say('&113\n아버님, 어서 이곳을 떠나십시오.')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0)
    stage.say('&101\n음……….')
    stage.unitsMove([
        [
            stage.unit(101),
            70,
            14,
            0],
        [
            stage.unit(113),
            66,
            14,
            0],
        [
            stage.unit(102),
            74,
            14,
            0]])
    stage.unit(101).hide()
    stage.unit(113).hide()
    stage.unit(102).hide()
    stage.effectSound(208, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 338, 124)
    stage.showHead(101, 338, 44)
    stage.bottomTxt('원소는 관도에서 조조에게 패했고', False, False, False)
    stage.delay(5)
    stage.head(101).move(306, 30)
    stage.delay(5)
    stage.bottomTxt('잔여 병력을 이끌고 여양으로 퇴각했다.', True, False, False)
    stage.delay(8)
    stage.showHead(110, 370, 12)
    stage.delay(5)
    stage.bottomTxt('지난번 업성에서 신중히 대응했던', True, True, False)
    stage.delay(5)
    stage.head(110).hide()
    stage.delay(5)
    stage.bottomTxt('원소에게 간언한 군사 전풍은 참수되었다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene2():
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
            106,
            38,
            48,
            1],
        [
            103,
            38,
            54,
            1],
        [
            102,
            38,
            60,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('여양  원소의 관저')
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('&101\n조조를 이대로 용서할 수는 없다.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('&101\n조조와 다시 결전을 벌이겠다.\n무슨 일이 있어도 이겨야 한다!')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(114).setAction(0, 0)
    stage.say('&114\n하지만 아버님……우리 병력은……\n관도에서 이미 태반을 잃었습니다.')
    stage.unit(113).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(2, 2)
    stage.say('&113\n둘째야, 무슨 소리를 하는 것이냐?\n대적을 앞에 두고 어찌 그런 맥 빠지는 말을 하느냐!!')
    stage.unit(113).setAction(0, 2)
    stage.delay(3)
    stage.unit(103).setAction(6)
    stage.say('&103\n그렇습니다. 승패는 병력으로 정해지지 않지요.\n과연 우리 맏공자 원담 님이십니다!')
    stage.unit(103).setAction(0)
    stage.say('&102\n(곽도 저자는 원담에게 아첨할 줄만 아는군!)\n&106\n(주군께서 돌아가시면\n원담의 심복인 곽도가\n분명 권력을 틀어쥘 것이다.)')
    stage.unit(114).setAction(0, 3)
    stage.unit(113).setAction(0, 3)
    stage.say('&101\n음…… 허나…….\n적은 병력으로 대군에 맞서는 것은 왕자의 길이 아니지…….')
    stage.unit(113).setAction(3)
    stage.say('&113\n하지만 아버님……!')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(115).setAction(0, 0)
    stage.say('&115\n아버님, 병력은 걱정하지 마십시오.\n&101\n오, 무슨 뜻이냐?\n&115\n조조와 다시 결전을 치르고자\n하북의 병력을 여양에 집결시켰습니다.\n&115\n병력은 여전히 우리 군이 우세합니다.')
    stage.unit(113).setAction(0, 2)
    stage.say('&113\n원상, 멋대로 병력을 모았느냐?\n&115\n형님과 의논할 시간이 없었습니다…….')
    stage.unit(113).setAction(2)
    stage.say('&113\n입 다물어라! 나를 안중에도 두지 않는구나.\n너무 제멋대로야!\n&101\n그만하라!\n원담, 좋은 생각이지 않느냐.')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(0, 3)
    stage.unit(115).setAction(0, 3)
    stage.say('&101\n현보, 잘했다.\n과연 내 아들답구나.\n&102\n그렇습니다.\n공자의 지략은 참으로 감탄스럽습니다.\n&106\n그렇습니다. 승리하려면\n병력이 적어서야 되겠습니까.')
    stage.say('&103\n으으음……….')
    stage.say('&101\n좋다! 현사, 현혁, 현보.\n각자 물러가 싸울 준비를 하라!')
    stage.unit(101).setAction(17)
    stage.say('&101\n결전은 창정에서 벌이겠다.\n창정에서 조조를 무찌르겠다!')
    stage.unit(101).setAction(0)
    stage.say('&113\n예.\n&114\n예.\n&115\n예.')
    stage.unitsMove([
        [
            stage.unit(113),
            68,
            98,
            2],
        [
            stage.unit(114),
            68,
            98,
            2],
        [
            stage.unit(115),
            68,
            98,
            2]])
    stage.unit(113).hide()
    stage.unit(114).hide()
    stage.unit(115).hide()
    stage.say('&101\n음………\n&101\n용맹밖에 없는 현사보다는\n현보가 내 뒤를 잇기에 더 알맞을지도 모르겠군…….')
    stage.unit(103).setAction(6)
    stage.say('&103\n하지만 주군……\n맏아들 원담 공자께서 대업을 잇는 것이 순리입니다…….')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(106).setAction(0, 2)
    stage.say('&106\n곽도, 대업을 이으려면 군주의 재능을 갖춰야 하오.')
    stage.unit(102).setAction(0, 0)
    stage.say('&102\n그렇지요, 그렇고말고요.\n용맹만으로는 나라의 주인이 될 수 없습니다.')
    stage.say('&103\n뭐라고? 무슨 소리냐……!')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 338, 124)
    stage.showHead(101, 306, 30)
    stage.bottomTxt('원소는 관도에서 패한 치욕을 씻으려', False, False, False)
    stage.delay(5)
    stage.head(101).move(380, 34)
    stage.delay(8)
    stage.bottomTxt('다시 병력을 모아 창정으로 진군했다.', True, False, False)
    stage.delay(8)
    stage.head(0).move(380, 114)
    stage.delay(8)
    stage.bottomTxt('원소군의 동향을 알아낸 조조군은 즉시 맞아 싸웠다.', True, True, True)
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
            7,
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
            3]])
    stage.draw()
    stage.setStageName('창정  조조군 본진')
    stage.delay(3)
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n주군, 첩자의 보고입니다.\n원소가 전풍의 목을 베었다고 합니다.\n&0\n전풍의 목을 베었다고?\n&0\n지혜로운 이를 제 손으로 죽이다니\n원소는 눈이 있어도 볼 줄을 모르는구나.')
    stage.unit(21).setAction(0, 2)
    stage.say('&21\n게다가 후계자 자리를 두고 다투면서\n모사들도 두 파로 갈라졌습니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n하하하. 어리석기 짝이 없군!\n원소는 대체 무슨 생각을 하는 게냐?\n지금이 어느 때인데!')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.showUnit(181, 49, 16, 2)
    stage.say('&181\n보고드립니다.')
    stage.unit(181).move(49, 26, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 원소군이 출동하려 합니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n알겠다.\n원소가 나와 죽기 살기로 싸울 작정이군…….')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n주군, 방심하시면 안 됩니다.\n원소를 세 번 이겼다 하나\n적에게는 아직 상당한 힘이 있습니다.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n음, 알고 있다.\n걱정하지 말게.')
    stage.unit(22).setAction(6)
    stage.say('&22\n원소는 지난번의 참패를 치욕으로 여기니\n이번에는 죽음을 각오하고 승리하려 할 것입니다.\n정면으로 맞서는 것은 상책이 아닙니다.')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그렇군…….\n좋다. 한 번쯤 져 주도록 하지!')
    stage.unit(1).setAction(3)
    stage.say('&1\n뭐…… 뭐라고!?')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n물론 그에게 질 생각은 없네.\n다만 날카로운 예봉을 피하려는 것이지.')
    stage.unit(1).setAction(0)
    stage.say('&1\n그래서 일부러 물러날 생각인가?\n예전이나 지금이나 참으로 교활하군.\n맹덕, 원소를 골려 주려는 게로군.\n&0\n그렇네.\n전군에 명을 전하라.\n서둘러라!\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(22).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 13, [
        21,
        15,
        7])
    gvars[4076] = 0
    gvars[4057] = 19
    stage.setBattlePos([
        [
            8,
            32],
        [
            9,
            32],
        [
            7,
            32],
        [
            7,
            10],
        [
            12,
            14],
        [
            7,
            19],
        [
            11,
            24],
        [
            6,
            25],
        [
            7,
            11],
        [
            12,
            15],
        [
            7,
            20],
        [
            11,
            25],
        [
            6,
            26],
        [
            0,
            0],
        [
            0,
            0]])
    return False


def scene4():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(21):
        stage.say('&21\n주군, 계책이 하나 있습니다.\n&0\n음? 무슨 계책인가, 중덕?\n&21\n예.\n허저 장군께 이렇게 하도록 하시지요…….\n&0\n하하하.\n좋다, 허저. 너만 믿겠다!\n&15\n예!? 무…… 무슨 일입니까?')
    if stage.unitClickTest(7):
        stage.say('&7\n옛 주군과 싸운다 해도…….\n&7\n이제 더는 망설이지 않겠다.')
    if stage.unitClickTest(22):
        stage.say('&22\n장 장군의 부대는 궁기병입니다……\n믿고 의지할 새로운 전력입니다.\n&0\n음, 장 장군. 훌륭한 활약을 기대하겠네.\n&7\n예! 맡겨 주십시오.')
    if stage.unitClickTest(20):
        stage.say('&20\n창정의 전장은 산 사이의 좁은 골짜기이며\n이곳이 주전장이 될 것입니다.')
    if stage.unitClickTest(15):
        stage.say('&0\n허저, 이번 싸움에는 네가 나서야겠다.\n&15\n예!\n드디어 제 차례군요.')
    if stage.unitClickTest(1):
        stage.say('&1\n방금 장합에게 들었는데\n원소의 맏아들과 막내아들이 후계 자리를 다툰다더군.\n&0\n맏아들과 막내아들?\n삼형제였지……\n둘째는 어찌 되었는가?')
        stage.unit(7).setAction(0, 2)
        stage.say('&7\n둘째 원희는 성품이 너무 온화합니다……\n&0\n그런가?\n너무 온화한 군주는\n이 난세에서 살아남을 수 없지.')
        stage.unit(7).setAction(0, 1)
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
