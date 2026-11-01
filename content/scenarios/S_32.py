# Source Generated with Decompyle++
# File: S_32.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20,
        21,
        22,
        23,
        24], [
        '(4,14)',
        '(8,9)',
        '(15,11)',
        '(25,17)',
        '(26,18)'])
    stage.nearEvent([
        15,
        9])
    stage.nearEvent([
        0,
        140,
        15,
        141,
        0,
        142], 1)
    stage.initFight()
    stage.setGlobalData(25, -2, 140, 0, 0, 1)
    stage.createEnemy(
        [{'id': 140, 'x': 26, 'y': 18, 'dir': 3, 'lv': 4, 'i': 0},
         {'id': 142, 'x': 25, 'y': 17, 'dir': 3, 'lv': 4, 'i': 1},
         {'id': 210, 'x': 22, 'y': 17, 'dir': 3, 'lv': 0, 'i': 2},
         {'id': 211, 'x': 22, 'y': 18, 'dir': 3, 'lv': 0, 'i': 3},
         {'id': 212, 'x': 25, 'y': 14, 'dir': 0, 'lv': 0, 'i': 4},
         {'id': 213, 'x': 26, 'y': 14, 'dir': 0, 'lv': 0, 'i': 5},
         {'id': 234, 'x': 23, 'y': 17, 'dir': 3, 'lv': 0, 'i': 6},
         {'id': 235, 'x': 23, 'y': 18, 'dir': 3, 'lv': 0, 'i': 7},
         {'id': 236, 'x': 25, 'y': 15, 'dir': 0, 'lv': 0, 'i': 8},
         {'id': 237, 'x': 26, 'y': 15, 'dir': 0, 'lv': 0, 'i': 9},
         {'id': 298, 'x': 24, 'y': 18, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 10},
         {'id': 299, 'x': 26, 'y': 16, 'dir': 0, 'lv': 0, 'ai': 0, 'i': 11},
         {'id': 318, 'x': 15, 'y': 11, 'dir': 3, 'lv': 0, 'i': 12},
         {'id': 390, 'x': 15, 'y': 10, 'dir': 3, 'lv': 0, 'i': 13},
         {'id': 391, 'x': 14, 'y': 11, 'dir': 3, 'lv': 0, 'i': 14},
         {'id': 382, 'x': 15, 'y': 12, 'dir': 3, 'lv': 0, 'i': 15},
         {'id': 383, 'x': 16, 'y': 11, 'dir': 3, 'lv': 0, 'i': 16},
         {'id': 141, 'x': 4, 'y': 15, 'dir': 0, 'lv': 4, 'i': 17},
         {'id': 214, 'x': 4, 'y': 14, 'dir': 0, 'lv': 0, 'i': 18},
         {'id': 238, 'x': 3, 'y': 15, 'dir': 0, 'lv': 0, 'i': 19},
         {'id': 239, 'x': 5, 'y': 15, 'dir': 0, 'lv': 0, 'i': 20},
         {'id': 300, 'x': 3, 'y': 16, 'dir': 0, 'lv': 0, 'i': 21},
         {'id': 301, 'x': 4, 'y': 16, 'dir': 0, 'lv': 0, 'i': 22},
         {'id': 302, 'x': 5, 'y': 16, 'dir': 0, 'lv': 0, 'i': 23},
         {'id': 215, 'x': 9, 'y': 9, 'dir': 3, 'lv': 0, 'i': 24},
         {'id': 216, 'x': 8, 'y': 9, 'dir': 3, 'lv': 0, 'i': 25},
         {'id': 240, 'x': 9, 'y': 8, 'dir': 3, 'lv': 0, 'i': 26},
         {'id': 241, 'x': 9, 'y': 10, 'dir': 3, 'lv': 0, 'i': 27},
         {'id': 303, 'x': 10, 'y': 8, 'dir': 3, 'lv': 0, 'i': 28},
         {'id': 304, 'x': 10, 'y': 9, 'dir': 3, 'lv': 0, 'i': 29},
         {'id': 305, 'x': 10, 'y': 10, 'dir': 3, 'lv': 0, 'i': 30},
         {'id': 9, 'hide': 1, 'x': 24, 'y': 11, 'dir': 3, 'lv': 6, 'ai': 1, 'i': 31},
         {'id': 462, 'hide': 1, 'x': 25, 'y': 10, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 32},
         {'id': 463, 'hide': 1, 'x': 25, 'y': 11, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 33},
         {'id': 464, 'hide': 1, 'x': 25, 'y': 12, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 34},
         {'id': 465, 'hide': 1, 'x': 26, 'y': 10, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 35},
         {'id': 466, 'hide': 1, 'x': 26, 'y': 11, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 36},
         {'id': 467, 'hide': 1, 'x': 26, 'y': 12, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 37}]
    )
    stage.createMine([
        {
            'idx': 0,
            'x': 2,
            'y': 5,
            'dir': 1 },
        {
            'idx': 1,
            'x': 3,
            'y': 4,
            'dir': 1 },
        {
            'idx': 2,
            'x': 1,
            'y': 6,
            'dir': 1 },
        {
            'idx': 3,
            'x': 1,
            'y': 5,
            'dir': 1 },
        {
            'idx': 4,
            'x': 2,
            'y': 4,
            'dir': 1 },
        {
            'idx': 5,
            'y': 5,
            'dir': 1 },
        {
            'idx': 6,
            'x': 1,
            'y': 4,
            'dir': 1 },
        {
            'idx': 7,
            'x': 2,
            'y': 3,
            'dir': 1 },
        {
            'idx': 8,
            'y': 5,
            'dir': 1 },
        {
            'idx': 9,
            'x': 1,
            'y': 3,
            'dir': 1 },
        {
            'idx': 10,
            'y': 4,
            'dir': 1 },
        {
            'idx': 11,
            'y': 3,
            'dir': 1 },
        {
            'idx': 12,
            'x': 1,
            'y': 2,
            'dir': 1 },
        {
            'idx': 13,
            'dir': 0 },
        {
            'idx': 14,
            'dir': 0 }])
    stage.setEnemyEquip(9, 0, 0, 0, 0, 12)
    stage.setEnemyEquip(142, -1, 0, 28, 0, -1)
    stage.loadBg(32)
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab25')
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
            goto('lab25')
    stage.draw()
    stage.bgSound(5)
    stage.unit(140).setDir(stage.countDir(140, 0))
    stage.say('&140\n마침내 조조군이 왔군.')
    stage.unit(142).setDir(stage.countDir(142, 140))
    stage.say('&142\n교주님, 안심하십시오.\n이곳은 지키기는 쉽고 공격하기는 어려운 지형입니다.\n&142\n관문만 지키면\n조조군도 어쩌지 못할 것입니다.')
    stage.unit(141).setDir(stage.countDir(141, 0))
    stage.say('&141\n형님,\n이 장위에게 관문 수비를 맡겨 주십시오.\n&15\n소문보다 더 험한 곳이군요.\n&0\n(촉을 멸하려면\n반드시 차지해야 할 전략적 요충지다.\n장로가 이곳을 바쳐 준다면…….)\n&0\n전군에 전하라. 장로를 죽이지 마라.\n장로를 사로잡아 내게 데려오라.')
    stage.unit(0).setAction(5)
    stage.say('&0\n이곳은 한중으로 통하는 전략적 요충지다.\n먼저 관문을 점령해야 한다.\n전군 공격을 개시하라!')
    stage.unit(0).setAction(0)
    stage.setWinCondition('승리 조건\n1. 장로를 사로잡는다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 25턴을 초과한다.')
    stage.showWinCondition('장로를 사로잡아라!')
    stage.unit(140).heightLight()
    stage.startOper()
    vars[532] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if vars[32] and not vars[11] and not vars[50] and stage.curCamp() == 0 and stage.round() >= 1:
        stage.unit(9).show()
        stage.unit(462).show()
        stage.unit(463).show()
        stage.unit(464).show()
        stage.unit(465).show()
        stage.unit(466).show()
        stage.unit(467).show()
        stage.unit(9).setDir(stage.countDir(9, 140))
        stage.unit(142).setDir(stage.countDir(142, 9))
        stage.unit(140).setDir(stage.countDir(140, 9))
        stage.say('&9\n늦었습니다.\n즉시 조조군을 섬멸하러 가겠습니다!\n&140\n장군과 아무 상관도 없는 이 다툼에\n휘말리게 하려니 차마 마음이 편치 않소.\n허나 이제 그대에게 의지할 수밖에 없구려.\n&9\n장로 대인, 이 방덕은 은혜를 알면 반드시 갚습니다.\n목숨을 버려서라도 조조군을 무찌르겠습니다.\n&142\n방덕 장군,\n부디 이 나라를 구해 주십시오.')
        stage.unit(318).setAI(1)
        stage.unit(390).setAI(1)
        stage.unit(391).setAI(1)
        stage.unit(382).setAI(1)
        stage.unit(383).setAI(1)
        vars[11] = 1
    if not vars[20] and stage.isInPos(1025, 4, 14):
        stage.getItem(154, -1, True, 1025)
        vars[20] = 1
    if not vars[21] and stage.isInPos(1025, 8, 9):
        stage.getItem(154, -1, True, 1025)
        vars[21] = 1
    if not vars[22] and stage.isInPos(1025, 15, 11):
        stage.getItem(151, -1, True, 1025)
        vars[22] = 1
    if not vars[23] and stage.isInPos(1025, 25, 17):
        stage.getItem(151, -1, True, 1025)
        vars[23] = 1
    if not vars[24] and stage.isInPos(1025, 26, 18):
        stage.getItem(152, -1, True, 1025)
        vars[24] = 1
    if not vars[30] and stage.isInRect(1025, 0, 14, 6, 19):
        stage.unit(141).setAI(1)
        stage.unit(214).setAI(1)
        stage.unit(238).setAI(1)
        stage.unit(239).setAI(1)
        stage.unit(300).setAI(1)
        stage.unit(301).setAI(1)
        stage.unit(302).setAI(1)
        vars[30] = 1
    if not vars[31] and stage.isInRect(1025, 8, 0, 27, 19):
        stage.unit(215).setAI(1)
        stage.unit(216).setAI(1)
        stage.unit(240).setAI(1)
        stage.unit(241).setAI(1)
        stage.unit(303).setAI(1)
        stage.unit(304).setAI(1)
        stage.unit(305).setAI(1)
        vars[31] = 1
    if not vars[32] and not vars[50] and not vars[51] and stage.curCamp() == 2 and stage.isInRect(1025, 9, 0, 27, 19):
        stage.unit(140).setDir(stage.countDir(140, 0))
        stage.unit(140).setAction(4)
        stage.say('&140\n조조군이 관문을 돌파했군.\n이제 돌이킬 수 없는 것인가……?')
        stage.unit(142).setDir(stage.countDir(142, 140))
        stage.say('&142\n교주님, 방덕 장군께 출전을 청하시지요!\n병 때문에 마초와 헤어져\n이곳에서 요양 중인 방 장군께 부탁드린다면…….\n&140\n방덕 장군의 용맹은 한중에서도 이름이 높지.\n이제는 그의 힘을 빌릴 수밖에 없겠군.\n서둘러라! 방덕 장군을 모셔라!\n&142\n예!')
        vars[32] = 1
    if not vars[33] and stage.curCamp() == 2 and stage.isInRect(1025, 11, 0, 27, 19):
        stage.unit(141).setDir(stage.countDir(141, 140))
        stage.say('&141\n으윽, 이대로라면 형님께서 위험하시다!')
        if stage.totalRectUnit(2, 0, 14, 6, 19) >= 2:
            stage.unit(141).setAction(5)
            stage.say('&141\n전군은 조조군의 배후를 공격하라!\n관문을 나가 맞아 싸워라!')
            stage.unit(141).setAction(0)
        stage.unit(141).setAI(1)
        stage.unit(214).setAI(1)
        stage.unit(238).setAI(1)
        stage.unit(239).setAI(1)
        stage.unit(300).setAI(1)
        stage.unit(301).setAI(1)
        stage.unit(302).setAI(1)
        vars[33] = 1
    if not vars[34] and stage.curCamp() == 2 and stage.isInRect(1025, 21, 13, 27, 19):
        stage.unit(140).setDir(stage.countDir(140, 0))
        stage.unit(140).setAction(5)
        stage.say('&140\n조조군을 성 안에 들이지 마라.\n무슨 일이 있어도 막아라.')
        stage.unit(140).setAction(0)
        stage.unit(142).setAI(1)
        stage.unit(210).setAI(1)
        stage.unit(211).setAI(1)
        stage.unit(212).setAI(1)
        stage.unit(213).setAI(1)
        stage.unit(234).setAI(1)
        stage.unit(235).setAI(1)
        stage.unit(236).setAI(1)
        stage.unit(237).setAI(1)
        stage.unit(298).setAI(1)
        stage.unit(299).setAI(1)
        vars[34] = 1
    if vars[2] and not vars[40] and stage.isNear(0, 140, True):
        stage.resumeCtrl()
        stage.unit(0).setDir(stage.countDir(0, 140))
        stage.unit(140).setDir(stage.countDir(140, 0))
        stage.say('&0\n오두미교의 교주 장로여!\n우리가 그대의 나라를 멸하러 왔다고 여긴다면\n그것은 잘못된 생각이오.\n&0\n지금 한중이 번영하려면\n먼저 온 나라가 하나가 되어야 하오.\n우리 군에 귀순하시오!\n&140\n안정된 질서에는 백성들의 지지가 필요하오.\n다스리기만 한다고 인심이 안정되지는 않소.\n&0\n나라를 잃는다고 신앙까지 사라지는 것은 아니오.\n한중도 내가 다스리는 다른 땅들과 마찬가지로\n백성들이 평온하게 살아갈 수 있을 것이오.\n&140\n자유롭게 포교해도 된다는 말씀이오?\n알겠소. 장졸들을 헛되이 희생시키느니\n그대에게 귀순하겠소.')
        if not vars[51]:
            stage.unit(142).setDir(stage.countDir(142, 140))
            stage.say('&142\n그렇다면 굳이 싸울 필요가 없겠군요.\n앞으로 잘 부탁드립니다.')
            vars[51] = 1
        if not vars[52]:
            stage.unit(141).setDir(stage.countDir(141, 140))
            stage.say('&141\n형님께서 그렇게 결심하셨다면\n저도 따르겠습니다.')
            vars[52] = 1
        stage.say('&140\n집안에 대대로 전해지는 보물입니다.\n안정된 질서를 세우는 데 써 주십시오.')
        stage.getItem(52, -1)
        vars[50] = 1
        vars[40] = 1
        stage.reward()
        stage.end()
        vars[100] = 1
        vars[0] = 1
        vars[632] = 1
        return True
    if not vars[41] and stage.isNear(15, 9, False):
        stage.unit(15).setDir(stage.countDir(15, 9))
        stage.unit(9).setDir(stage.countDir(9, 15))
        stage.say('&15\n음!? 마초의 부장이 아니냐?\n어쩌다 장로의 부하가 되었지?\n&9\n나는 장로 대인의 부하가 아니다.\n그저 도우러 왔을 뿐이지.\n승부를 겨루자!')
        fight = stage.startFight(15, 9, 5)
        fight.showUnit(False, '나는 방덕, 자는 영명이다.\n오너라!', 9)
        fight.showUnit(True, '그 차림이 무겁지도 않느냐?\n됐다. 어차피 어떻게 싸워도 상관없다.', 0)
        fight.showStart()
        fight.setAction(True, 2)
        fight.setAction(False, 0)
        fight.attack2(True, 1, True)
        stage.delay(5)
        fight.setAction(False, 1)
        fight.setAction(True, 1)
        fight.say(True, '음?', True)
        fight.attack2(True, 0, True)
        fight.attack2(True, 0, True)
        fight.attack2(True, 0, True)
        fight.attack1(True, 2, False)
        stage.delay(5)
        fight.setAction(True, 0)
        fight.say(True, '으음?\n참으로 건장한 놈이군.', True)
        fight.setAction(True, 2)
        fight.attack1(False, 3, False)
        fight.setAction(False, 3)
        fight.setAction(False, 3)
        fight.setAction(False, 3)
        fight.say(False, '내 공격을 받아라!', True)
        fight.attack2(False, 0, True)
        fight.attack2(False, 0, True)
        fight.attack1(False, 4, False)
        fight.setAction(False, 1)
        fight.attack2(False, 0, True)
        fight.attack2(False, 0, True)
        fight.attack2(False, 0, True)
        fight.attack2(True, 0, True)
        fight.attack2(False, 0, True)
        fight.attack2(False, 0, True)
        fight.attack1(False, 4, True)
        fight.setAction(False, 1)
        fight.attack1(True, 1, True)
        fight.attack1(False, 3, False)
        stage.delay(10)
        fight.setAction(True, 0)
        fight.say(True, '으으음!?\n마초만 해도 강적인데\n이놈도 만만치 않구나.', True)
        fight.setAction(False, 6)
        fight.say(False, '쓰러뜨릴 수 없군…….', True)
        fight.end()
        vars[41] = 1
    if not vars[42] and stage.isNear(15, 141, True):
        stage.unit(15).setDir(stage.countDir(15, 141))
        stage.unit(141).setDir(stage.countDir(141, 15))
        stage.say('&15\n네가 장위냐!\n나와 승부를 겨룰 배짱은 있느냐?\n&141\n나는 한중을 지켜야 한다.\n네 도발에 넘어갈 것 같으냐.')
        vars[42] = 1
    if not vars[43] and stage.isNear(0, 142, True):
        stage.unit(0).setDir(stage.countDir(0, 142))
        stage.unit(142).setDir(stage.countDir(142, 0))
        stage.say('&0\n그대가 장로의 군사인가?\n장로를 만나게 해 주게.\n그를 해칠 생각은 없네.\n&142\n그대 뜻대로 되지는 않을 것이오!\n우리는 목숨을 걸고 교주님을 지킬 것이오.')
        vars[43] = 1
    if not vars[50] and stage.unitStateTest(140, 7, 0, 2):
        stage.resumeCtrl()
        stage.say('&140\n으윽, 내 목숨도 여기까지인가……\n&0\n좋다. 장로를 붙잡았느냐?')
        stage.unit(0).setDir(stage.countDir(0, 140))
        stage.unit(140).setDir(stage.countDir(140, 0))
        stage.say('&0\n오두미교의 교주 장로여!\n우리가 그대의 나라를 멸하러 왔다고 여긴다면\n그것은 잘못된 생각이오.\n&0\n지금 한중이 번영하려면\n먼저 온 나라가 하나가 되어야 하오.\n우리 군에 귀순하시오!\n&140\n안정된 질서에는 백성들의 지지가 필요하오.\n다스리기만 한다고 인심이 안정되지는 않소.\n&0\n나라를 잃는다고 신앙까지 사라지는 것은 아니오.\n한중도 내가 다스리는 다른 땅들과 마찬가지로\n백성들이 평온하게 살아갈 수 있을 것이오.\n&140\n자유롭게 포교해도 된다는 말씀이오?\n알겠소. 장졸들을 헛되이 희생시키느니\n그대에게 귀순하겠소.')
        if not vars[51]:
            stage.unit(142).setDir(stage.countDir(142, 140))
            stage.say('&142\n그렇다면 굳이 싸울 필요가 없겠군요.\n앞으로 잘 부탁드립니다.')
            vars[51] = 1
        if not vars[52]:
            stage.unit(141).setDir(stage.countDir(141, 140))
            stage.say('&141\n형님께서 그렇게 결심하셨다면\n저도 따르겠습니다.')
            vars[52] = 1
        vars[50] = 1
        stage.reward()
        stage.end()
        vars[100] = 1
        vars[0] = 1
        vars[632] = 1
        return True
    if not vars[51] and stage.unitStateTest(142, 7, 0, 2):
        stage.say('&142\n더 저항해도 소용없겠군.\n나를 데려가시오.')
        stage.unit(142).hide(1)
        stage.getItem(96, -1, True, 1027)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(141, 7, 0, 2):
        stage.say('&141\n으윽, 형님……\n죄송합니다……\n으윽!')
        stage.unit(141).hide(2)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(9, 7, 0, 2):
        stage.unit(9).setDir(stage.countDir(9, 0))
        stage.unit(0).setDir(stage.countDir(0, 9))
        stage.say('&9\n으윽, 내 목숨도 여기까지인가……\n죽이든 살리든 마음대로 해라!\n&0\n방덕, 자네는 참으로 훌륭한 장수로군.\n용맹이 뛰어나고 의리까지 깊으니\n이대로 죽이기는 너무도 아깝네.\n&9\n흥! 어차피 그대에게 패한 몸이니\n위로하는 말은 그만하시오.\n&0\n좋다! 데려가라!\n&9\n조 대인, 조금이라도 인의가 남아 있다면\n이 방덕의 말 몇 마디를 들어 주시오.\n&0\n무슨 말인가? 해 보게.\n&9\n오늘 죽어도 후회는 없으나\n장로 장군의 은혜에 보답하지 못한 것이 한이오.\n&9\n장로 장군은 그대를 해친 적도 없고\n천하에 야심을 품지도 않았소.\n부디 그분만은 놓아주지 않겠소?\n&0\n본래 그럴 생각이었으니\n염려 말게.\n&9\n감사하기 그지없소.\n이제 죽어도 여한이 없구려!\n나를 데려가 참수하시오.\n&0\n(장로를 만나지 못하면\n방덕을 설득하기는 어렵겠군.)')
        stage.unit(9).hide(1)
        stage.setWinCondition('승리 조건\n1. 장로를 사로잡는다.\n2. 조조가 장로와 인접한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 25턴을 초과한다.')
        stage.showWinCondition('장로를 설득하라!')
        stage.unit(140).heightLight()
        vars[2] = 1
        vars[53] = 1
    if vars[100] and not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('&0\n좋다, 마침내 적군을 몰아냈다!\n우리의 승리다!')
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[632] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 장로군에게 대패하여 돌아왔다.')
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
