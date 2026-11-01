# Source Generated with Decompyle++
# File: S_00.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20,
        21,
        22,
        23], [
        '(13,12)',
        '(13,13)',
        '(10,11)',
        '(9,11)'])
    stage.nearEvent([
        0,
        146,
        0,
        147], 1)
    stage.initFight()
    stage.setGlobalData(20, -2, -1, 0, 1, 1)
    stage.createFriend([
        {
            'id': 32,
            'hide': 1,
            'x': 14,
            'y': 1,
            'dir': 3,
            'lv': 6,
            'ai': 1,
            'i': 0 },
        {
            'id': 3,
            'hide': 1,
            'x': 13,
            'y': 2,
            'dir': 3,
            'lv': 6,
            'ai': 1,
            'i': 1 },
        {
            'id': 33,
            'hide': 1,
            'x': 13,
            'y': 1,
            'dir': 3,
            'lv': 6,
            'ai': 1,
            'i': 2 },
        {
            'id': 210,
            'x': 10,
            'y': 17,
            'dir': 0,
            'lv': 0,
            'ai': 1,
            'i': 3 },
        {
            'id': 211,
            'x': 9,
            'y': 18,
            'dir': 0,
            'lv': 0,
            'ai': 1,
            'i': 4 },
        {
            'id': 234,
            'x': 12,
            'y': 17,
            'dir': 0,
            'lv': 0,
            'ai': 1,
            'i': 5 },
        {
            'id': 258,
            'hide': 1,
            'x': 6,
            'y': 0,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 6 },
        {
            'id': 259,
            'hide': 1,
            'x': 5,
            'y': 0,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 7 },
        {
            'id': 235,
            'x': 7,
            'y': 16,
            'dir': 3,
            'lv': 0,
            'arm_limit': 0,
            'ai': 0,
            'i': 8 },
        {
            'id': 334,
            'x': 12,
            'y': 16,
            'dir': 0,
            'lv': 0,
            'arm_limit': 0,
            'ai': 0,
            'i': 9 },
        {
            'id': 157,
            'hide': 1,
            'x': 9,
            'y': 0,
            'lv': 30,
            'ai': 0,
            'i': 10 }])
    stage.createEnemy([
        {
            'id': 146,
            'x': 9,
            'y': 11,
            'lv': 4,
            'i': 0 },
        {
            'id': 147,
            'x': 10,
            'y': 11,
            'lv': 4,
            'i': 1 },
        {
            'id': 474,
            'x': 6,
            'y': 16,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 2 },
        {
            'id': 475,
            'x': 9,
            'y': 15,
            'lv': 0,
            'ai': 1,
            'i': 3 },
        {
            'id': 476,
            'x': 10,
            'y': 15,
            'lv': 0,
            'ai': 1,
            'i': 4 },
        {
            'id': 477,
            'x': 12,
            'y': 15,
            'lv': 0,
            'ai': 1,
            'i': 5 },
        {
            'id': 478,
            'x': 9,
            'y': 14,
            'lv': 0,
            'ai': 1,
            'i': 6 },
        {
            'id': 479,
            'x': 10,
            'y': 14,
            'lv': 0,
            'ai': 1,
            'i': 7 },
        {
            'id': 480,
            'x': 5,
            'y': 10,
            'lv': 0,
            'ai': 0,
            'i': 8 },
        {
            'id': 481,
            'x': 5,
            'y': 11,
            'lv': 0,
            'ai': 0,
            'i': 9 },
        {
            'id': 482,
            'x': 7,
            'y': 11,
            'lv': 0,
            'ai': 0,
            'i': 10 },
        {
            'id': 483,
            'x': 8,
            'y': 10,
            'lv': 0,
            'ai': 0,
            'i': 11 },
        {
            'id': 484,
            'x': 12,
            'y': 10,
            'lv': 0,
            'ai': 0,
            'i': 12 },
        {
            'id': 485,
            'x': 12,
            'y': 12,
            'lv': 0,
            'ai': 0,
            'i': 13 }])
    stage.createMine([
        {
            'idx': 0,
            'x': 7,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 1,
            'dir': 1 },
        {
            'idx': 2,
            'dir': 1 },
        {
            'idx': 3,
            'dir': 1 },
        {
            'idx': 4,
            'dir': 1 },
        {
            'idx': 5,
            'dir': 1 },
        {
            'idx': 6,
            'dir': 1 },
        {
            'idx': 7,
            'dir': 1 },
        {
            'idx': 8,
            'dir': 1 },
        {
            'idx': 9,
            'dir': 1 },
        {
            'idx': 10,
            'dir': 1 },
        {
            'idx': 11,
            'dir': 1 },
        {
            'idx': 12,
            'dir': 1 },
        {
            'idx': 13,
            'dir': 1 },
        {
            'idx': 14,
            'dir': 1 }])
    stage.setEnemyEquip(146, 0, 0, 0, 0, 25)
    stage.loadBg(0)
    stage.setUnitStatus([
        {
            'hp': -80,
            'unit': stage.unit(234) },
        {
            'hp': -80,
            'unit': stage.unit(235) },
        {
            'hp': -80,
            'unit': stage.unit(334) }])
    stage.delay(5)
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab27')
        if gvars[10] <= 120:
            if stage.varTest(1, 1, 2, 0, 1023):
                stage.varOper(2, 0, 2, 1, 1)
                if gvars[0] <= 169:
                    gvars[11] = stage.unitAttr(gvars[0], 16)
                    if gvars[11] != 1:
                        gvars[4076] = 0
                        gvars[1] = gvars[0]
                        gvars[2] = 5259072
                        gvars[1] *= 4
                        gvars[1] += gvars[2]
                        gvars[4076] = 1
                        pvars[0] = gvars[1]
                        stage.varOper(2, 3, 2, 1, 0)
                        stage.setUnitAttr(gvars[0], 17, gvars[3])
                        gvars[4] = 0
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
                        if gvars[4] == 0 and gvars[6] <= 34:
                            stage.setUnitAbility(gvars[0], 10, 0, 35)
                            stage.setUnitAbility(gvars[0], 11, 0, 35)
                        if gvars[4] == 1:
                            if gvars[8] <= 34:
                                stage.setUnitAbility(gvars[0], 12, 0, 35)
                            if gvars[9] <= 35:
                                stage.setUnitAbility(gvars[0], 6, 1, 30)
                                stage.setUnitAbility(gvars[0], 8, 1, 30)
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
                                stage.unit(gvars[0]).setPosts(0)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(1)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(2)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(3)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(4)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(5)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(6)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(7)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(8)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(9)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(10)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(11)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(12)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(13)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(14)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(15)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(16)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(17)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(18)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(19)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(20)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(21)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(22)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(23)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(24)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(25)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(26)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(27)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(28)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(29)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(30)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(31)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(32)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(33)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(34)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(35)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(36)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(37)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(38)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(39)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(40)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(41)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(42)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(43)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(44)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(45)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(46)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(47)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(48)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(49)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(50)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(51)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(52)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(53)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(54)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(55)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(56)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(57)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(58)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(59)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(60)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(61)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(62)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(63)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(64)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(65)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(66)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(67)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(68)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(69)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(70)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(71)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(72)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(73)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(74)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(75)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(76)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(77)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(78)
                            gvars[2] += 1
                            if gvars[102] == gvars[2]:
                                stage.unit(gvars[0]).setPosts(79)
                            gvars[4081] = 0
                        gvars[20] = stage.unitAttr(gvars[0], 18)
                        gvars[21] = stage.unitAttr(gvars[0], 20)
                        gvars[22] = stage.unitAttr(gvars[0], 23)
                        gvars[23] = 0
                        stage.setUnitAttr(gvars[0], 22, gvars[23])
                        stage.setUnitAttr(gvars[0], 25, gvars[23])
                        if gvars[21] < 30:
                            gvars[27] = gvars[21]
                            gvars[27] %= 3
                            if gvars[20] < 15:
                                if gvars[27] == 1:
                                    gvars[21] -= 1
                                if gvars[27] == 2:
                                    gvars[21] -= 2
                            if gvars[20] >= 15 and gvars[20] < 30:
                                if gvars[27] == 0:
                                    gvars[21] += 1
                                if gvars[27] == 2:
                                    gvars[21] -= 1
                            if gvars[20] >= 30:
                                if gvars[27] == 0:
                                    gvars[21] += 2
                                if gvars[27] == 1:
                                    gvars[21] += 1
                            stage.setUnitAttr(gvars[0], 20, gvars[21])
                            if gvars[20] < 5:
                                gvars[24] = 1
                            if gvars[20] >= 5 and gvars[20] < 10:
                                gvars[24] = 2
                            if gvars[20] >= 10 and gvars[20] < 15:
                                gvars[24] = 3
                            if gvars[20] >= 15 and gvars[20] < 20:
                                gvars[24] = 1
                            if gvars[20] >= 20 and gvars[20] < 25:
                                gvars[24] = 2
                            if gvars[20] >= 25 and gvars[20] < 30:
                                gvars[24] = 3
                            if gvars[20] >= 30 and gvars[20] < 35:
                                gvars[24] = 1
                            if gvars[20] >= 35 and gvars[20] < 40:
                                gvars[24] = 2
                            if gvars[20] >= 40:
                                gvars[24] = 3
                            stage.setUnitAttr(gvars[0], 21, gvars[24])
                        if gvars[22] < 79:
                            gvars[28] = gvars[22]
                            gvars[28] %= 3
                            if gvars[20] < 15:
                                if gvars[28] == 0:
                                    gvars[22] -= 2
                                if gvars[28] == 2:
                                    gvars[22] -= 1
                            if gvars[20] >= 15 and gvars[20] < 30:
                                if gvars[28] == 0:
                                    gvars[22] -= 1
                                if gvars[28] == 1:
                                    gvars[22] += 1
                            if gvars[20] >= 30:
                                if gvars[28] == 1:
                                    gvars[22] += 2
                                if gvars[28] == 2:
                                    gvars[22] += 1
                            stage.setUnitAttr(gvars[0], 23, gvars[22])
                            if gvars[20] < 5:
                                gvars[24] = 1
                            if gvars[20] >= 5 and gvars[20] < 10:
                                gvars[24] = 2
                            if gvars[20] >= 10 and gvars[20] < 15:
                                gvars[24] = 3
                            if gvars[20] >= 15 and gvars[20] < 20:
                                gvars[24] = 1
                            if gvars[20] >= 20 and gvars[20] < 25:
                                gvars[24] = 2
                            if gvars[20] >= 25 and gvars[20] < 30:
                                gvars[24] = 3
                            if gvars[20] >= 30 and gvars[20] < 35:
                                gvars[24] = 1
                            if gvars[20] >= 35 and gvars[20] < 40:
                                gvars[24] = 2
                            if gvars[20] >= 40:
                                gvars[24] = 3
                            stage.setUnitAttr(gvars[0], 24, gvars[24])
            pvars[1] += 48
            gvars[10] += 1
            goto('lab27')
    stage.draw()
    stage.bgSound(6)
    stage.say('&474\n죽어라!')
    stage.attackAction(474, 235, 1)
    stage.delay(10)
    stage.unit(235).setAction(4)
    stage.say('&235\n부, 분하다…….')
    stage.unit(235).hide(1)
    stage.say('&477\n아!')
    stage.attackAction(477, 334, 0)
    stage.unit(334).setAction(4)
    stage.say('&334\n아아…….')
    stage.unit(334).hide(1)
    stage.unit(234).setAction(9)
    stage.say('&234\n헉, 헉, 하아…더는 버틸 수 없다!\n이대로는 전멸하고 말겠다.')
    stage.say('&210\n빌어먹을, 황건군이 점점 늘어나는군.')
    stage.unit(210).setDir(stage.countDir(210, 234))
    stage.say('&210\n조금만 더 버티면 기병대가 지원하러 온다!\n무슨 일이 있어도 버텨라!')
    stage.unit(211).setDir(stage.countDir(211, 210))
    stage.say('&211\n내 생각에는 제 목숨부터 챙기는 게 우선이오.\n요즘 관군 중에 그런 생각 안 하는 자가 어디 있소.')
    stage.unit(211).setDir(stage.countDir(211, 146))
    stage.say('&211\n게다가 이곳 영천은 전투가 가장 치열한 곳이야.\n썩어 빠진 조정이 병사를 보내 죽게 할 리 없지.')
    stage.unit(210).setDir(stage.countDir(210, 211))
    stage.say('&210\n그렇다 해도 원군의 기병대장은 조조다.\n만만치 않은 인물이라더군.')
    stage.unit(210).setDir(stage.countDir(210, 146))
    stage.say('&210\n이번에는 그를 믿어 보자.\n이제는 오기를 기다리는 수밖에 없군.')
    stage.say('&234\n어, 어서 오십시오!')
    stage.unit(146).setDir(1)
    stage.say('&146\n적이 참으로 끈질기군.')
    stage.unit(147).setAction(7)
    stage.say('&147\n흥, 어차피 언젠가 무찌를 놈들이다!\n관군 주제에 감히 이곳을 공격하다니!\n반드시 후회하게 해 주마.')
    stage.unit(147).setAction(0)
    stage.unit(146).setDir(2)
    stage.setWinCondition('승리 조건\n1. 장보와 장량을 죽인다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
    vars[500] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[14] and stage.round() >= 1:
        stage.unit(210).setDir(0)
        stage.unit(210).setAction(6)
        stage.say('&210\n조금만 더 버텨라!\n관군을 얕본 대가를 보여 주어라!')
        stage.unit(210).setAction(0)
        vars[14] = 1
    if not vars[11] and stage.curCamp() == 2 and stage.round() >= 1:
        stage.unit(32).show()
        stage.unit(3).show()
        stage.unit(33).show()
        stage.say('&32\n제때 도착한 듯하군.\n하지만 중과부적인 상황이라\n이곳 관군도 고전하는 모양이다.')
        stage.unit(3).setDir(stage.countDir(3, 32))
        stage.unit(33).setDir(stage.countDir(33, 32))
        stage.say('&3\n기세를 타고 안팎에서 협공해야겠습니다만\n우리 병력이 조금 부족하군요…….\n&33\n둘째 형님은 걱정도 많으시오…\n나 혼자서도 저놈들은 충분하오.\n&32\n관군을 내버려 둘 수는 없다.\n뒤로 돌아가 곧장 녹채를 치자.\n돌격하라!')
        stage.unit(32).move(11, 5, 2)
        stage.unit(3).move(10, 5, 2)
        stage.unit(33).move(12, 5, 2)
        vars[11] = 1
    if not vars[12] and not vars[13] and stage.round() >= 2:
        stage.say('&32\n둘째야, 셋째야, 잠깐 기다려라.\n이대로는 앞쪽의 관군이 전멸하겠다.\n여기 불을 놓자.\n&3\n옳습니다! 이쪽으로 시선을 끌면…….\n적도 동요할지 모릅니다.\n&33\n그럼 내가 불을 지르겠소.\n&32\n양쪽 모두 무고한 목숨이 너무 많이 희생되는 것은 바라지 않는다.\n잘 가늠해서 놓아라.')
        stage.unit(33).setAction(6)
        stage.delay(1)
        stage.setFire(True, 13, 5)
        stage.unit(33).setAction(0)
        stage.delay(10)
        stage.unit(3).move(8, 5, 2)
        stage.say('&3\n여기에도 불을 질러라!')
        stage.unit(3).setAction(6)
        stage.delay(1)
        stage.setFire(True, 6, 4)
        stage.setFire(True, 7, 5)
        stage.unit(3).setAction(0)
        stage.delay(10)
        stage.unit(33).setDir(1)
        stage.unit(33).setDir(3)
        stage.unit(33).setDir(2)
        stage.unit(33).setAction(5)
        stage.say('&33\n안 돼, 불길이 너무 작아!\n녹채까지 닿지도 않잖아!')
        stage.unit(33).setAction(0)
        stage.talk(157, 157, '허, 허, 허!\n그럼 이것은 어떠냐?')
        stage.unit(3).setDir(1)
        stage.say('&3\n응?')
        stage.unit(33).setDir(3)
        stage.say('&33\n누구냐!?')
        stage.playMagicMeff(10, 8, 105)
        stage.setFires(True, [
            [
                14,
                4],
            [
                14,
                5],
            [
                15,
                5],
            [
                15,
                6],
            [
                15,
                7]])
        stage.setFires(True, [
            [
                13,
                6],
            [
                14,
                6],
            [
                14,
                7],
            [
                14,
                8]])
        stage.setFires(True, [
            [
                12,
                6],
            [
                12,
                7],
            [
                13,
                7],
            [
                13,
                8],
            [
                13,
                9]])
        stage.setFires(True, [
            [
                6,
                5],
            [
                6,
                6],
            [
                5,
                6],
            [
                5,
                7],
            [
                5,
                8]])
        stage.setFires(True, [
            [
                7,
                6],
            [
                7,
                7],
            [
                6,
                7],
            [
                6,
                8],
            [
                6,
                9]])
        stage.unit(484).setDir(stage.countDir(484, 33))
        stage.unit(484).setAction(8)
        stage.say('&484\n부, 불이야!')
        stage.unit(483).setDir(stage.countDir(483, 3))
        stage.say('&483\n적이 뒤로 돌아왔다!')
        stage.unit(147).setDir(stage.countDir(147, 3))
        stage.say('&147\n뭐라고? 기습을 당했다고……!')
        stage.unit(146).setDir(stage.countDir(146, 3))
        stage.say('&146\n관군의 원군인가?')
        stage.unit(146).setDir(2)
        stage.unit(146).setAction(5)
        stage.say('&146\n후방의 적을 막을 병력을 보내라!')
        stage.unit(146).setAction(0)
        stage.unit(146).setAction(4)
        stage.say('&146\n이런…… 하나같이 말을 듣지 않는구나…….')
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 0,
                'y1': 0,
                'x2': 19,
                'y2': 19,
                'camp': 5 }])
        stage.unit(480).setAI(1)
        stage.unit(481).setAI(1)
        stage.unit(482).setAI(1)
        stage.unit(483).setAI(1)
        stage.unit(484).setAI(1)
        stage.unit(485).setAI(1)
        stage.unit(3).setDir(2)
        stage.say('&3\n아!')
        stage.unit(33).setDir(2)
        stage.say('&33\n어찌 된 일이오!?\n&32\n모르겠다. 지금은 그런 것까지 신경 쓸 겨를이 없다.\n눈앞의 일이 먼저다.')
        stage.unit(32).setAction(6)
        stage.say('&32\n적이 혼란에 빠졌으니\n지금이 승리할 좋은 기회다!\n목표는 적장 장보와 장량이다.\n돌격하라!')
        stage.unit(32).setAction(0)
        vars[12] = 1
        stage.bgSound(5)
        stage.unit(258).show()
        stage.unit(259).show()
        stage.unit(0, 1).show()
        stage.unit(0).move(8, 2, 2)
        stage.unit(258).move(7, 2, 2)
        stage.unit(259).move(8, 1, 2)
        stage.say('&0\n이곳 관군이 고전한다기에 왔는데\n이 불길은 대체……!?')
        stage.unit(258).setDir(stage.countDir(258, 0))
        stage.say('&258\n대장님! 저쪽 사람들이 불을 지른 듯합니다.\n하지만 관군 같지는 않습니다…….')
        stage.unit(0).setDir(stage.countDir(0, 32))
        stage.say('&0\n음, 의병인 듯하군.')
        stage.unit(0).setDir(stage.countDir(0, 259))
        stage.say('&0\n그대들은 먼저 녹채로 가라! 나는 의용군 쪽을 살펴보겠다.\n&258\n예!\n&259\n예!')
        stage.unit(258).move(9, 7, 2)
        stage.unit(259).move(10, 7, 2)
        stage.unit(0).move(11, 4, 2)
        stage.say('&0\n나는 관군의 기병대장으로, 성은 조, 이름은 조, 자는 맹덕이오.\n조정의 명을 받아 황건군을 토벌하고 있소.\n그대의 존함은 무엇이오?')
        stage.unit(32).setDir(stage.countDir(32, 0))
        stage.say('&32\n저는 유비 유현덕입니다. 농민 출신에 불과하나\n천하가 어지러운 것을 보고 백성을 구하고자\n의병을 이끌고 일어났습니다.\n&32\n두 의형제와 함께 관군을 돕고 싶습니다.\n조 대인께서 명을 내려 주십시오.\n&0\n유비 선생 덕분에 황건군은 이미 혼란에 빠졌소.\n내가 따로 명을 내릴 필요는 없겠군요.\n&0\n녹채 안의 두 사람만 쓰러뜨리면 되니\n그대들은 뜻대로 싸우시오. 그럼 다음에 뵙겠소.')
        stage.unit(0).move(10, 6, 2)
        stage.unit(32).move(9, 6, 2)
        stage.unit(3).setDir(stage.countDir(3, 146))
        stage.showWinCondition('장보와 장량을 죽여라!')
        stage.unit(146).heightLight()
        stage.unit(147).heightLight()
        if vars[1001]:
            stage.unit(157).show()
            stage.guide()
            stage.unit(157).hide()
            stage.unit(0).setDir(2)
        stage.startOper()
        vars[13] = 1
    if not vars[20] and stage.isInPos(1025, 13, 12):
        stage.getItem(150, 0, True, 1025)
        vars[20] = 1
    if not vars[21] and stage.isInPos(1025, 13, 13):
        stage.getItem(150, 0, True, 1025)
        vars[21] = 1
    if not vars[22] and stage.isInPos(1025, 10, 11):
        stage.getItem(150, 0, True, 1025)
        vars[22] = 1
    if not vars[23] and stage.isInPos(1025, 9, 11):
        stage.getItem(150, 0, True, 1025)
        vars[23] = 1
    if not vars[40] and stage.isNear(0, 146, True):
        stage.unit(0).setDir(stage.countDir(0, 146))
        stage.unit(146).setDir(stage.countDir(146, 0))
        stage.say('&0\n장보, 아직도 저항할 셈이냐!\n&146\n한실의 운수는 다했다. 이제 천하는 황건의 것이다.\n그대도 우리에게 가담하는 것이 어떠냐!\n&0\n한실이 언젠가 멸망하더라도\n그 뒤를 잇는 것은 너희가 아닐 것이다.\n흥!\n&146\n네놈……!? 한실을 위해 목숨을 바칠 필요는 없다!\n&0\n사악한 도술로 천하를 어지럽히는 너희를\n보고만 있을 수는 없지. 얌전히 죽음을 받아라.')
        vars[40] = 1
    if not vars[41] and stage.isNear(0, 147, True):
        stage.unit(0).setDir(stage.countDir(0, 147))
        stage.unit(147).setDir(stage.countDir(147, 0))
        stage.say('&0\n네가 장량이냐?\n적군이라 하나 일군을 이끄는 장수이니\n순순히 항복하는 것이 어떠냐.\n&147\n허튼소리 말아라. 네놈부터 죽여 주마!\n&0\n도적은 역시 도적이로군.\n이토록 시세를 모르니\n군사를 이끌 자격도 없다. 흥!')
        vars[41] = 1
    if vars[50] and vars[51] and not vars[100]:
        stage.setRectUnitHide(0, 0, 19, 19, 5, 1)
        stage.resumeCtrl()
        if not vars[52] and not vars[53] and not vars[54]:
            stage.unit(0).setDir(stage.countDir(0, 32))
            stage.unit(32).setDir(stage.countDir(32, 0))
            stage.say('&0\n유비 선생께서 의병 대장으로만 남기에는 아깝소.\n나와 함께 일해 보지 않겠소?\n&32\n아직도 곳곳에서 전란이 끊이지 않고 있소.\n&32\n호의로 권해 주시는 것은 감사하나,\n우리는 역적을 토벌하러 가야 하오…….\n&0\n그렇소? 그렇다면 강권하지 않겠소.\n나도 다른 임무가 있으니\n훗날 다시 만나도록 합시다.')
            stage.unit(210).move(8, 15, 2)
            stage.unit(211).move(8, 16, 2)
            stage.unit(234).move(8, 17, 2)
            stage.unit(258).move(8, 18, 2)
            stage.unit(259).move(8, 19, 2)
            stage.unit(0).move(10, 19, 2)
            stage.unit(32).setDir(stage.countDir(32, 3))
            stage.unit(3).setDir(stage.countDir(3, 32))
            stage.say('&32\n이 사람은 지금껏 보아 온 관군과 다르군.\n조조라 했던가……?\n&3\n난세의 앞날은 알 수 없으니 훗날 다시 만날지도 모르지요.')
            stage.unit(32).setDir(stage.countDir(32, 33))
            stage.unit(33).setDir(stage.countDir(33, 32))
            stage.say('&32\n좋다. 황건 잔당을 소탕하며\n공손찬의 진영으로 가자.\n그곳에 도착한 뒤 앞날을 생각하겠다.\n&33\n좋소. 뭐든 배불리 먹을 수만 있으면 그만이오!\n하하하하!')
            stage.unit(0).setDir(stage.countDir(0, 32))
            stage.say('&0\n(유현덕…\n다시 만날 때는 적일까, 벗일까…\n오직 하늘만이 알 일이로군.)')
        else:
            stage.say('&0\n좋다. 이제 이곳의 황건적도 잠잠해지겠군.\n&0\n(유현덕… 의병에 불과한데도\n이리 잘 싸우다니, 기억해 둘 인물이야.)')
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[100] = 1
        vars[600] = 1
        return True
    if not vars[50] and stage.unitStateTest(146, 7, 0, 2):
        stage.say('&146\n정녕 이곳에서 죽어야 한단 말인가? 으윽…….')
        stage.unit(146).hide(2)
        stage.getItem(132, 0, True, 1027)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(147, 7, 0, 2):
        stage.say('&147\n원통하다. 이런 놈들에게 패하다니…….')
        stage.unit(147).hide(2)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(32, 7, 0, 2):
        stage.say('&32\n황건군의 공세를 막고는 싶으나…\n지금은 내 힘이 부족하오…….')
        stage.unit(32).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(3, 7, 0, 2):
        stage.say('&3\n더 싸워 봐야 의미가 없다. 퇴각하라.')
        stage.unit(3).hide(1)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(33, 7, 0, 2):
        stage.say('&33\n분하다. 이 장비가 패하다니?\n뭔가 잘못된 것이 틀림없다!')
        stage.unit(33).hide(1)
        vars[54] = 1
    if not vars[13] and not vars[55] and stage.unitStateTest(234, 7, 0, 2):
        stage.say('&234\n으, 원군…… 원군은 아직인가……?')
        vars[55] = 1
    if vars[100] and not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('&0\n좋다. 이제 이곳의 황건적도 잠잠해지겠군.\n&0\n(유현덕… 의병에 불과한데도\n이리 잘 싸우다니, 기억해 둘 인물이야.)')
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[600] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 황건군에게 패했다.')
        stage.end()
        stage.lose()
        vars[1] = 1
        return True


def scene2():
    stage.bgSound(-1)
    stage.setMenuVisible(False)
    return False

def __dispatch__():
    label('__dispatch_loop')
    idx = stage.sceneIndex()
    if idx == 1:
        finished = scene1()
        if not finished and not vars['1'] and stage.curCamp() == 2 and stage.round() >= stage.maxRound():
            stage.info('제한 턴을 초과하여 패배했습니다!')
            stage.end()
            stage.lose()
            vars['1'] = True
            finished = True
        if not finished:
            return None
    elif idx == 0:
        scene0()
    elif idx == 2:
        if not stage.isLose():
            scene2()
        else:
            return None
    stage.incSceneIdx()
    goto('__dispatch_loop')
