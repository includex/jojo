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
    stage.say('@dialogue:S_00.scene0.dialogue0001')
    stage.attackAction(474, 235, 1)
    stage.delay(10)
    stage.unit(235).setAction(4)
    stage.say('@dialogue:S_00.scene0.dialogue0002')
    stage.unit(235).hide(1)
    stage.say('@dialogue:S_00.scene0.dialogue0003')
    stage.attackAction(477, 334, 0)
    stage.unit(334).setAction(4)
    stage.say('@dialogue:S_00.scene0.dialogue0004')
    stage.unit(334).hide(1)
    stage.unit(234).setAction(9)
    stage.say('@dialogue:S_00.scene0.dialogue0005')
    stage.say('@dialogue:S_00.scene0.dialogue0006')
    stage.unit(210).setDir(stage.countDir(210, 234))
    stage.say('@dialogue:S_00.scene0.dialogue0007')
    stage.unit(211).setDir(stage.countDir(211, 210))
    stage.say('@dialogue:S_00.scene0.dialogue0008')
    stage.unit(211).setDir(stage.countDir(211, 146))
    stage.say('@dialogue:S_00.scene0.dialogue0009')
    stage.unit(210).setDir(stage.countDir(210, 211))
    stage.say('@dialogue:S_00.scene0.dialogue0010')
    stage.unit(210).setDir(stage.countDir(210, 146))
    stage.say('@dialogue:S_00.scene0.dialogue0011')
    stage.say('@dialogue:S_00.scene0.dialogue0012')
    stage.unit(146).setDir(1)
    stage.say('@dialogue:S_00.scene0.dialogue0013')
    stage.unit(147).setAction(7)
    stage.say('@dialogue:S_00.scene0.dialogue0014')
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
        stage.say('@dialogue:S_00.scene1.dialogue0001')
        stage.unit(210).setAction(0)
        vars[14] = 1
    if not vars[11] and stage.curCamp() == 2 and stage.round() >= 1:
        stage.unit(32).show()
        stage.unit(3).show()
        stage.unit(33).show()
        stage.say('@dialogue:S_00.scene1.dialogue0002')
        stage.unit(3).setDir(stage.countDir(3, 32))
        stage.unit(33).setDir(stage.countDir(33, 32))
        stage.say('@dialogue:S_00.scene1.dialogue0003')
        stage.unit(32).move(11, 5, 2)
        stage.unit(3).move(10, 5, 2)
        stage.unit(33).move(12, 5, 2)
        vars[11] = 1
    if not vars[12] and not vars[13] and stage.round() >= 2:
        stage.say('@dialogue:S_00.scene1.dialogue0004')
        stage.unit(33).setAction(6)
        stage.delay(1)
        stage.setFire(True, 13, 5)
        stage.unit(33).setAction(0)
        stage.delay(10)
        stage.unit(3).move(8, 5, 2)
        stage.say('@dialogue:S_00.scene1.dialogue0005')
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
        stage.say('@dialogue:S_00.scene1.dialogue0006')
        stage.unit(33).setAction(0)
        stage.talk(157, 157, '허, 허, 허!\n그럼 이것은 어떠냐?')
        stage.unit(3).setDir(1)
        stage.say('@dialogue:S_00.scene1.dialogue0007')
        stage.unit(33).setDir(3)
        stage.say('@dialogue:S_00.scene1.dialogue0008')
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
        stage.say('@dialogue:S_00.scene1.dialogue0009')
        stage.unit(483).setDir(stage.countDir(483, 3))
        stage.say('@dialogue:S_00.scene1.dialogue0010')
        stage.unit(147).setDir(stage.countDir(147, 3))
        stage.say('@dialogue:S_00.scene1.dialogue0011')
        stage.unit(146).setDir(stage.countDir(146, 3))
        stage.say('@dialogue:S_00.scene1.dialogue0012')
        stage.unit(146).setDir(2)
        stage.unit(146).setAction(5)
        stage.say('@dialogue:S_00.scene1.dialogue0013')
        stage.unit(146).setAction(0)
        stage.unit(146).setAction(4)
        stage.say('@dialogue:S_00.scene1.dialogue0014')
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
        stage.say('@dialogue:S_00.scene1.dialogue0015')
        stage.unit(33).setDir(2)
        stage.say('@dialogue:S_00.scene1.dialogue0016')
        stage.unit(32).setAction(6)
        stage.say('@dialogue:S_00.scene1.dialogue0017')
        stage.unit(32).setAction(0)
        vars[12] = 1
        stage.bgSound(5)
        stage.unit(258).show()
        stage.unit(259).show()
        stage.unit(0, 1).show()
        stage.unit(0).move(8, 2, 2)
        stage.unit(258).move(7, 2, 2)
        stage.unit(259).move(8, 1, 2)
        stage.say('@dialogue:S_00.scene1.dialogue0018')
        stage.unit(258).setDir(stage.countDir(258, 0))
        stage.say('@dialogue:S_00.scene1.dialogue0019')
        stage.unit(0).setDir(stage.countDir(0, 32))
        stage.say('@dialogue:S_00.scene1.dialogue0020')
        stage.unit(0).setDir(stage.countDir(0, 259))
        stage.say('@dialogue:S_00.scene1.dialogue0021')
        stage.unit(258).move(9, 7, 2)
        stage.unit(259).move(10, 7, 2)
        stage.unit(0).move(11, 4, 2)
        stage.say('@dialogue:S_00.scene1.dialogue0022')
        stage.unit(32).setDir(stage.countDir(32, 0))
        stage.say('@dialogue:S_00.scene1.dialogue0023')
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
        stage.say('@dialogue:S_00.scene1.dialogue0024')
        vars[40] = 1
    if not vars[41] and stage.isNear(0, 147, True):
        stage.unit(0).setDir(stage.countDir(0, 147))
        stage.unit(147).setDir(stage.countDir(147, 0))
        stage.say('@dialogue:S_00.scene1.dialogue0025')
        vars[41] = 1
    if vars[50] and vars[51] and not vars[100]:
        stage.setRectUnitHide(0, 0, 19, 19, 5, 1)
        stage.resumeCtrl()
        if not vars[52] and not vars[53] and not vars[54]:
            stage.unit(0).setDir(stage.countDir(0, 32))
            stage.unit(32).setDir(stage.countDir(32, 0))
            stage.say('@dialogue:S_00.scene1.dialogue0026')
            stage.unit(210).move(8, 15, 2)
            stage.unit(211).move(8, 16, 2)
            stage.unit(234).move(8, 17, 2)
            stage.unit(258).move(8, 18, 2)
            stage.unit(259).move(8, 19, 2)
            stage.unit(0).move(10, 19, 2)
            stage.unit(32).setDir(stage.countDir(32, 3))
            stage.unit(3).setDir(stage.countDir(3, 32))
            stage.say('@dialogue:S_00.scene1.dialogue0027')
            stage.unit(32).setDir(stage.countDir(32, 33))
            stage.unit(33).setDir(stage.countDir(33, 32))
            stage.say('@dialogue:S_00.scene1.dialogue0028')
            stage.unit(0).setDir(stage.countDir(0, 32))
            stage.say('@dialogue:S_00.scene1.dialogue0029')
        else:
            stage.say('@dialogue:S_00.scene1.dialogue0030')
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[100] = 1
        vars[600] = 1
        return True
    if not vars[50] and stage.unitStateTest(146, 7, 0, 2):
        stage.say('@dialogue:S_00.scene1.dialogue0031')
        stage.unit(146).hide(2)
        stage.getItem(132, 0, True, 1027)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(147, 7, 0, 2):
        stage.say('@dialogue:S_00.scene1.dialogue0032')
        stage.unit(147).hide(2)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(32, 7, 0, 2):
        stage.say('@dialogue:S_00.scene1.dialogue0033')
        stage.unit(32).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(3, 7, 0, 2):
        stage.say('@dialogue:S_00.scene1.dialogue0034')
        stage.unit(3).hide(1)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(33, 7, 0, 2):
        stage.say('@dialogue:S_00.scene1.dialogue0035')
        stage.unit(33).hide(1)
        vars[54] = 1
    if not vars[13] and not vars[55] and stage.unitStateTest(234, 7, 0, 2):
        stage.say('@dialogue:S_00.scene1.dialogue0036')
        vars[55] = 1
    if vars[100] and not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_00.scene1.dialogue0037')
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
