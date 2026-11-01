# Source Generated with Decompyle++
# File: S_05.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20,
        21,
        24,
        25,
        26], [
        '(19,13)',
        '(15,9)',
        '(7,12)',
        '(17,2)',
        '(16,21)'])
    stage.nearEvent([
        0,
        145,
        0,
        41,
        0,
        32], 1)
    stage.initFight()
    stage.setGlobalData(20, -2, 145, 0, 0, 1)
    stage.createEnemy(
        [{'id': 145, 'x': 19, 'y': 12, 'dir': 3, 'lv': 4, 'i': 0},
         {'id': 41, 'x': 19, 'y': 13, 'dir': 3, 'lv': 6, 'i': 1},
         {'id': 210, 'x': 15, 'y': 10, 'dir': 3, 'lv': 0, 'i': 2},
         {'id': 211, 'x': 15, 'y': 11, 'dir': 3, 'lv': 0, 'i': 3},
         {'id': 212, 'x': 15, 'y': 12, 'dir': 3, 'lv': 0, 'i': 4},
         {'id': 213, 'x': 15, 'y': 13, 'dir': 3, 'lv': 0, 'i': 5},
         {'id': 214, 'x': 18, 'y': 7, 'dir': 3, 'lv': 0, 'i': 6},
         {'id': 215, 'x': 19, 'y': 7, 'dir': 3, 'lv': 0, 'i': 7},
         {'id': 216, 'x': 18, 'y': 16, 'dir': 3, 'lv': 0, 'i': 8},
         {'id': 217, 'x': 19, 'y': 16, 'dir': 3, 'lv': 0, 'i': 9},
         {'id': 234, 'x': 20, 'y': 9, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 10},
         {'id': 235, 'x': 18, 'y': 10, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 11},
         {'id': 236, 'x': 18, 'y': 14, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 12},
         {'id': 237, 'x': 20, 'y': 15, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 13},
         {'id': 218, 'x': 7, 'y': 11, 'dir': 3, 'lv': 0, 'i': 14},
         {'id': 219, 'x': 7, 'y': 12, 'dir': 3, 'lv': 0, 'i': 15},
         {'id': 220, 'x': 7, 'y': 13, 'dir': 3, 'lv': 0, 'i': 16},
         {'id': 238, 'x': 8, 'y': 12, 'dir': 3, 'lv': 0, 'i': 17},
         {'id': 258, 'x': 12, 'y': 10, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 18},
         {'id': 259, 'x': 12, 'y': 11, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 19},
         {'id': 260, 'x': 12, 'y': 12, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 20},
         {'id': 261, 'x': 12, 'y': 13, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 21},
         {'id': 282, 'x': 13, 'y': 11, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 22},
         {'id': 283, 'x': 13, 'y': 12, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 23},
         {'id': 221, 'x': 17, 'y': 2, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 24},
         {'id': 222, 'x': 18, 'y': 3, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 25},
         {'id': 239, 'x': 17, 'y': 1, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 26},
         {'id': 223, 'x': 16, 'y': 21, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 27},
         {'id': 224, 'x': 17, 'y': 20, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 28},
         {'id': 240, 'x': 16, 'y': 22, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 29},
         {'id': 32, 'yj': 1, 'hide': 1, 'x': 22, 'y': 1, 'dir': 3, 'lv': 6, 'ai': 1, 'i': 30},
         {'id': 3, 'yj': 1, 'hide': 1, 'x': 21, 'y': 1, 'dir': 3, 'lv': 6, 'ai': 1, 'i': 31},
         {'id': 33, 'yj': 1, 'hide': 1, 'x': 21, 'y': 2, 'dir': 3, 'lv': 6, 'ai': 1, 'i': 32},
         {'id': 34, 'yj': 1, 'hide': 1, 'x': 22, 'y': 2, 'dir': 3, 'lv': 6, 'ai': 1, 'i': 33},
         {'id': 298, 'x': 17, 'y': 12, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 34}]
    )
    stage.createMine([
        {
            'idx': 0,
            'y': 12,
            'dir': 1 },
        {
            'idx': 1,
            'y': 11,
            'dir': 1 },
        {
            'idx': 2,
            'y': 13,
            'dir': 1 },
        {
            'idx': 3,
            'x': 1,
            'y': 12,
            'dir': 1 },
        {
            'idx': 4,
            'x': 1,
            'y': 11,
            'dir': 1 },
        {
            'idx': 5,
            'x': 1,
            'y': 13,
            'dir': 1 },
        {
            'idx': 6,
            'x': 1,
            'y': 10,
            'dir': 1 },
        {
            'idx': 7,
            'x': 1,
            'y': 14,
            'dir': 1 },
        {
            'idx': 8,
            'y': 10,
            'dir': 1 },
        {
            'idx': 9,
            'y': 14,
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
    stage.setEnemyEquip(32, 32, 0, -1, 0, 14)
    stage.setEnemyEquip(3, 36, 0, -1, 0, -1)
    stage.setEnemyEquip(33, 37, 0, -1, 0, -1)
    stage.setEnemyEquip(34, -1, 0, 13, 0, -1)
    stage.setEnemyEquip(145, -1, 0, 13, 0, -1)
    stage.setEnemyEquip(41, -1, 0, -1, 0, 23)
    stage.loadBg(5)
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab29')
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
            goto('lab29')
    stage.draw()
    stage.bgSound(5)
    stage.unit(0).setAction(5)
    stage.say('&0\n지금부터 서주를 공격한다.\n&0\n(이 난세에 이름을 떨치겠다.\n아버님의 원수를 갚는 것이 천하 평정의 첫걸음이 될 것이다.)')
    stage.unit(0).setAction(0)
    stage.say('&22\n(처음 참여하는 싸움이다.\n먼저 군중에서 내 입지를 다져야 한다…….)\n&21\n(책사로서 무엇을 해야 할까?\n이 싸움이 내 능력을 가릴 시금석이겠지…….)\n&145\n부하 하나 제대로 다스리지 못하다니\n이 도겸도 정녕 늙었구나…….\n&145\n허나 이제 와서 후회한들 늦었다.\n태수로서 이 땅을 지킬 의무가 있으니\n내 목을 바쳐 사태를 가라앉힐 수 있다면………')
    stage.unit(41).setDir(stage.countDir(41, 145))
    stage.say('&41\n주군, 절대 안 됩니다!\n조조가 아버지의 원수를 갚는다는 것은 구실일 뿐\n진짜 목표는 서주입니다.\n&41\n이번 싸움으로 조조의 야심이 드러났습니다.\n그가 이곳을 다스리면 백성에게 불행이 닥칠 것입니다.\n부디 다시 생각해 주십시오!\n&145\n하지만 우리로서는 조조군을 막을 수 없네……\n이대로라면 무고한 백성만 다칠 것이야.\n&41\n반드시 정의로운 군사가 구원하러 올 것입니다.\n희망을 버리지 마십시오.')
    stage.unit(41).setDir(3)
    stage.setWinCondition('승리 조건\n1. 도겸을 격퇴한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
    stage.showWinCondition('도겸을 격퇴하라!')
    stage.unit(145).heightLight()
    stage.startOper()
    vars[505] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[11] and stage.round() == 8:
        stage.unit(32).show()
        stage.unit(3).show()
        stage.unit(33).show()
        stage.unit(34).show()
        stage.say('&32\n음! 이미 싸움이 시작되었군.\n조조가 이런 짓을 할 줄이야…….\n&3\n겉으로는 아버지의 원수를 갚는다지만\n제게는 천하를 정복하려는 야심이 보입니다.\n이번에는 반드시 막아야 합니다.\n&32\n음! 우선 말로 타일러 보자.\n&34\n말이 통한다면 좋겠으나, 아무래도…….')
        stage.unit(32).setDir(stage.countDir(32, 0))
        stage.say('&32\n조 대인은 어디 계시오?\n드릴 말씀이 있소!\n우선 싸움을 멈추시오.')
        stage.unit(0).setDir(stage.countDir(0, 32))
        stage.say('&0\n유현덕! 이 싸움은 선친의 원수를 갚기 위한 것이다.\n그대는 끼어들지 마시오.\n&32\n도 대인의 사정은 그대도 알지 않소.\n야심 없는 분이니 분명 오해가 있을 것이오.\n결코 악의로 벌인 일은 아닐 터…….\n&0\n아버지께서 돌아가셨다는 것은 엄연한 사실이오.\n도겸의 사람됨과는 아무 상관 없소.\n&32\n이 일에는 두 나라의 안위가 걸려 있소.\n부디 사사로운 원한은 접어 두시오.\n&0\n그대가 무슨 말을 해도 그럴 수는 없소!')
        stage.unit(32).setAction(10)
        stage.unit(32).setAction(4)
        stage.say('&32\n뭐라고……? 이 유비가 사람을 잘못 보았단 말인가!\n조맹덕은 천하를 위하는 줄 알았거늘!\n&0\n나도 물론 천하를 생각했소. 하지만 현덕,\n그대의 방법으로는 세상을 바꿀 수 없소.')
        stage.unit(22).setDir(stage.countDir(22, 0))
        stage.talk(22, 186, '주군, 잠시 따로 말씀드리겠습니다…\n이러이러한 사정입니다…….')
        stage.say('&0\n(뭐라고!? 여포가 복양을 공격했다고!)\n&0\n(서주 함락을 눈앞에 둔 이때,\n이를 어찌하면 좋단 말인가……?)')
        sel = stage.choice('즉시 화친하고 복양으로 향한다\n계속 도겸을 토벌한다', -1)
        if sel == 1:
            stage.say('&0\n유현덕! 이번에는 그대 체면을 보아\n화친을 받아들이겠다.\n&32\n오? 받아 주겠다고!\n&0\n허나 다음에도 내 일을 방해한다면\n결코 용서하지 않겠다. 명심해라.')
            stage.unit(145).setDir(stage.countDir(145, 0))
            stage.say('&145\n조 대인, 잠시만 기다리시오.\n이 백은갑옷을 받아 주시오.\n&145\n우리에게 악의가 없다는 증표로\n이 보물을 드리겠소.')
            stage.unit(0).setDir(stage.countDir(0, 145))
            stage.getItem(81, 0)
            stage.say('&0\n좋소. 그럼 받도록 하겠소.')
            stage.unit(0).setDir(3)
            stage.unit(0).setAction(5)
            stage.say('&0\n전군 퇴각하라. 즉시 복양으로 돌아간다.')
            stage.unit(0).setAction(0)
            stage.unit(0).setDir(stage.countDir(0, 32))
            stage.say('&0\n유비! 성도 없고 군사도 없으면서\n뜻만 품고 무엇을 할 수 있겠느냐!\n&0\n천하만을 생각하다 보면\n한 사람 한 사람까지 헤아릴 수는 없는 법이다.')
            stage.setRectUnitHide(0, 0, 23, 23, 0)
            stage.unit(32).setDir(stage.countDir(32, 3))
            stage.say('&32\n마침내 싸움 하나가 끝났군.\n하지만…….')
            stage.unit(3).setDir(stage.countDir(3, 32))
            stage.say('&3\n이것이 조조가 천하를 평정하는 도리란 말입니까?\n&32\n지금 우리 형편으로는 아무것도 할 수 없겠구나.\n&3\n형님, 서주 백성이 살아난 것도 사실입니다.\n이제 하늘에 모든 것을 맡기고\n머물 곳을 찾아 이끌어 주시기를 바랄 뿐입니다.\n&32\n그 말이 옳다.\n좋다. 공손찬에게 돌아가자.\n원소의 움직임도 살펴야겠다.')
            stage.unit(145).setDir(stage.countDir(145, 32))
            stage.say('&145\n현덕 선생, 고맙소.\n그대에게 보답할 것이 없구려.')
            stage.unit(145).setDir(stage.countDir(145, 41))
            stage.say('&145\n미축, 축하할 준비를 하라 전하게.')
            stage.unit(41).setDir(stage.countDir(41, 32))
            stage.talk(41, 41, '그대들은 서주의 은인이니\n정성껏 대접하고 싶소.\n부디 모두 머물러 주시오.')
            stage.unit(32).setDir(stage.countDir(32, 145))
            stage.unit(3).setDir(stage.countDir(3, 145))
            stage.unit(33).setDir(stage.countDir(33, 145))
            stage.unit(34).setDir(stage.countDir(34, 145))
            stage.unit(145).setDir(stage.countDir(145, 32))
            stage.say('&32\n그 정도 일은 괘념치 마십시오.\n저희는 다만 정의를 바로 세우려 했을 뿐입니다.\n이제 돌아가 보고해야 합니다.\n&33\n대접해 준다는데 형님도 받아들이시오.\n행군하느라 배가 텅 비었소!\n그럼 내가 형님 대신 먼저 감사드리겠소!\n&32\n이봐, 셋째야………\n할 수 없지. 더 거절하기도 어렵군.\n좋습니다. 도 대인의 호의를 받아들이겠습니다.\n&3\n음! 도겸 대인, 후한 대접에 감사하오.\n&145\n별말씀을요. 자, 자!\n모두 사양하지 마십시오.')
            stage.reward()
            stage.end()
            vars[0] = 1
            vars[100] = 1
            vars[605] = 1
            return True
        if sel == 2:
            stage.say('&0\n(서주 함락이 눈앞인데,\n무슨 일이 있어도 포기할 수 없다.)\n&0\n더는 할 말 없다!\n목숨이 아깝거든 비켜라!\n&32\n눈앞에서 곤경에 처한 이를 돕는 것이\n이 유비가 평생 지켜 온 도리요!\n결코 물러서지 않겠소!')
            stage.unit(33).setDir(stage.countDir(33, 0))
            stage.unit(33).setAction(5)
            stage.say('&33\n협상은 깨진 모양이군!\n좋다, 싸우자!')
            stage.unit(33).setAction(0)
        vars[11] = 1
    if not vars[20] and stage.isInPos(1025, 19, 13):
        stage.getItem(157, 0, True, 1025)
        vars[20] = 1
    if not vars[21] and stage.isInPos(1025, 15, 9):
        stage.getItem(150, 0, True, 1025)
        vars[21] = 1
    if not vars[24] and stage.isInPos(1025, 7, 12):
        stage.getItem(150, 0, True, 1025)
        vars[24] = 1
    if not vars[25] and stage.isInPos(1025, 17, 2):
        stage.getItem(150, 0, True, 1025)
        vars[25] = 1
    if not vars[26] and stage.isInPos(1025, 16, 21):
        stage.getItem(150, 0, True, 1025)
        vars[26] = 1
    if not vars[30] and stage.isInRect(1025, 5, 0, 23, 23):
        stage.unit(218).setAI(0)
        stage.unit(219).setAI(0)
        stage.unit(220).setAI(0)
        stage.unit(238).setAI(0)
        vars[30] = 1
    if not vars[31] and stage.curCamp() == 2 and stage.isInRect(1025, 15, 7, 21, 16):
        stage.unit(145).setAction(4)
        stage.say('&145\n내 명이 다한 것인가…….')
        stage.unit(41).setDir(stage.countDir(41, 145))
        stage.say('&41\n주군…… 이렇게 된 이상\n전군을 동원해 조조와 승부를 겨루는 수밖에 없습니다.')
        stage.unit(41).setDir(stage.countDir(41, 0))
        stage.unit(41).setAI(1)
        stage.unit(210).setAI(1)
        stage.unit(211).setAI(1)
        stage.unit(212).setAI(1)
        stage.unit(213).setAI(1)
        stage.unit(214).setAI(1)
        stage.unit(215).setAI(1)
        stage.unit(216).setAI(1)
        stage.unit(217).setAI(1)
        stage.unit(221).setAI(1)
        stage.unit(222).setAI(1)
        stage.unit(223).setAI(1)
        stage.unit(224).setAI(1)
        stage.unit(234).setAI(1)
        stage.unit(235).setAI(1)
        stage.unit(236).setAI(1)
        stage.unit(237).setAI(1)
        stage.unit(239).setAI(1)
        stage.unit(240).setAI(1)
        vars[31] = 1
    if not vars[40] and stage.isNear(0, 145, True):
        stage.unit(0).setDir(stage.countDir(0, 145))
        stage.unit(145).setDir(stage.countDir(145, 0))
        stage.say('&0\n도겸, 각오해라!\n&145\n이제 무슨 말을 해도 소용없겠군…….')
        vars[40] = 1
    if not vars[41] and stage.isNear(0, 41, True):
        stage.unit(41).setDir(stage.countDir(41, 0))
        stage.unit(0).setDir(stage.countDir(0, 41))
        stage.say('&41\n조숭의 죽음은 우리에게 잘못이 있으니\n이제 달리 할 말은 없소.\n&41\n허나 조조가 서주를 삼키려는 것이라면\n이 미축, 한 걸음도 물러서지 않겠소.\n&0\n천하의 군웅과 맞서려면 내게도 힘이 필요하다!\n이번 불씨는 너희 스스로 뿌린 것이니\n너희 운이 없었다고 할 수밖에!\n&41\n가증스러운 놈, 역시 서주를 노리고 왔구나…….')
        vars[41] = 1
    if not vars[42] and stage.isNear(0, 32, True):
        stage.unit(32).setDir(stage.countDir(32, 0))
        stage.unit(0).setDir(stage.countDir(0, 32))
        stage.say('&32\n조조!\n&0\n유비!')
        stage.unit(32).setAction(6)
        stage.unit(0).setAction(6)
        stage.say('&0\n뜻은 같으나 걸어가는 길은 다르다니…….\n참으로 묘한 일이군.\n앞으로도 만날 날은 많겠지!')
        stage.unit(32).setAction(0)
        stage.unit(0).setAction(0)
        vars[42] = 1
    if not vars[50] and stage.unitStateTest(145, 7, 0, 2):
        stage.resumeCtrl()
        stage.say('&145\n내 목숨은 언제든 가져가도 좋소.\n하지만……서주의 백성에게는 죄가 없으니\n그들을 해치지는 마시오.')
        stage.getItem(81, 0)
        stage.unit(0).setDir(stage.countDir(0, 145))
        stage.say('&0\n서주의 백성을 학살할 생각은 없으니\n안심하시오.')
        stage.talk(21, 186, '조조 주군, 복양에서 보고가 왔습니다…\n수비군이 여포와의 싸움에서 패하여 복양이 함락되었습니다…….')
        stage.say('&0\n뭐라고! 가증스러운 여포 놈!\n&0\n(서주를 평정할 시간이 없다.\n복양을 되찾아야 한다.)\n도겸, 네 목숨은 잠시 살려 두마.')
        stage.unit(0).setDir(3)
        stage.unit(0).setAction(5)
        stage.say('&0\n전군 퇴각하라! 즉시 물러나라!\n복양을 되찾아야 한다.')
        stage.unit(0).setAction(0)
        vars[50] = 1
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[101] = 1
        vars[605] = 1
        return True
    if not vars[51] and stage.unitStateTest(41, 7, 0, 2):
        stage.say('&41\n주, 주군…… 마음은 간절하나 힘이 부족하여\n먼저 물러가겠습니다.')
        stage.unit(41).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(32, 7, 0, 2):
        stage.say('&32\n이 폭행을 막고 싶은 마음은 간절하나…\n힘이 미치지 못하는구나……\n참으로 한스럽다!')
        stage.unit(32).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(33, 7, 0, 2):
        stage.say('&33\n빌어먹을, 이런 놈들에게 패하다니……!\n에잇, 퇴각하라! 퇴각하라!')
        stage.unit(33).hide(1)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(3, 7, 0, 2):
        stage.say('&3\n으음, 조조군은 갈수록 강해지는군.\n퇴각하라.')
        stage.unit(3).hide(1)
        vars[54] = 1
    if not vars[55] and stage.unitStateTest(34, 7, 0, 2):
        stage.say('&34\n으으음…….\n원통하지만 물러나야겠군.')
        stage.unit(34).hide(1)
        vars[55] = 1
    if vars[100] and vars[101] and not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('&0\n훌륭하다! 마침내 적을 섬멸했군.\n우리 군의 승리다!')
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[605] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 도겸군에게 패했다.')
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
