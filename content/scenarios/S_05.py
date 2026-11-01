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
    stage.say('@dialogue:S_05.scene0.dialogue0001')
    stage.unit(0).setAction(0)
    stage.say('@dialogue:S_05.scene0.dialogue0002')
    stage.unit(41).setDir(stage.countDir(41, 145))
    stage.say('@dialogue:S_05.scene0.dialogue0003')
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
        stage.say('@dialogue:S_05.scene1.dialogue0001')
        stage.unit(32).setDir(stage.countDir(32, 0))
        stage.say('@dialogue:S_05.scene1.dialogue0002')
        stage.unit(0).setDir(stage.countDir(0, 32))
        stage.say('@dialogue:S_05.scene1.dialogue0003')
        stage.unit(32).setAction(10)
        stage.unit(32).setAction(4)
        stage.say('@dialogue:S_05.scene1.dialogue0004')
        stage.unit(22).setDir(stage.countDir(22, 0))
        stage.talk(22, 186, '주군, 잠시 따로 말씀드리겠습니다…\n이러이러한 사정입니다…….')
        stage.say('@dialogue:S_05.scene1.dialogue0005')
        sel = stage.choice('즉시 화친하고 복양으로 향한다\n계속 도겸을 토벌한다', -1)
        if sel == 1:
            stage.say('@dialogue:S_05.scene1.dialogue0006')
            stage.unit(145).setDir(stage.countDir(145, 0))
            stage.say('@dialogue:S_05.scene1.dialogue0007')
            stage.unit(0).setDir(stage.countDir(0, 145))
            stage.getItem(81, 0)
            stage.say('@dialogue:S_05.scene1.dialogue0008')
            stage.unit(0).setDir(3)
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_05.scene1.dialogue0009')
            stage.unit(0).setAction(0)
            stage.unit(0).setDir(stage.countDir(0, 32))
            stage.say('@dialogue:S_05.scene1.dialogue0010')
            stage.setRectUnitHide(0, 0, 23, 23, 0)
            stage.unit(32).setDir(stage.countDir(32, 3))
            stage.say('@dialogue:S_05.scene1.dialogue0011')
            stage.unit(3).setDir(stage.countDir(3, 32))
            stage.say('@dialogue:S_05.scene1.dialogue0012')
            stage.unit(145).setDir(stage.countDir(145, 32))
            stage.say('@dialogue:S_05.scene1.dialogue0013')
            stage.unit(145).setDir(stage.countDir(145, 41))
            stage.say('@dialogue:S_05.scene1.dialogue0014')
            stage.unit(41).setDir(stage.countDir(41, 32))
            stage.talk(41, 41, '그대들은 서주의 은인이니\n정성껏 대접하고 싶소.\n부디 모두 머물러 주시오.')
            stage.unit(32).setDir(stage.countDir(32, 145))
            stage.unit(3).setDir(stage.countDir(3, 145))
            stage.unit(33).setDir(stage.countDir(33, 145))
            stage.unit(34).setDir(stage.countDir(34, 145))
            stage.unit(145).setDir(stage.countDir(145, 32))
            stage.say('@dialogue:S_05.scene1.dialogue0015')
            stage.reward()
            stage.end()
            vars[0] = 1
            vars[100] = 1
            vars[605] = 1
            return True
        if sel == 2:
            stage.say('@dialogue:S_05.scene1.dialogue0016')
            stage.unit(33).setDir(stage.countDir(33, 0))
            stage.unit(33).setAction(5)
            stage.say('@dialogue:S_05.scene1.dialogue0017')
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
        stage.say('@dialogue:S_05.scene1.dialogue0018')
        stage.unit(41).setDir(stage.countDir(41, 145))
        stage.say('@dialogue:S_05.scene1.dialogue0019')
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
        stage.say('@dialogue:S_05.scene1.dialogue0020')
        vars[40] = 1
    if not vars[41] and stage.isNear(0, 41, True):
        stage.unit(41).setDir(stage.countDir(41, 0))
        stage.unit(0).setDir(stage.countDir(0, 41))
        stage.say('@dialogue:S_05.scene1.dialogue0021')
        vars[41] = 1
    if not vars[42] and stage.isNear(0, 32, True):
        stage.unit(32).setDir(stage.countDir(32, 0))
        stage.unit(0).setDir(stage.countDir(0, 32))
        stage.say('@dialogue:S_05.scene1.dialogue0022')
        stage.unit(32).setAction(6)
        stage.unit(0).setAction(6)
        stage.say('@dialogue:S_05.scene1.dialogue0023')
        stage.unit(32).setAction(0)
        stage.unit(0).setAction(0)
        vars[42] = 1
    if not vars[50] and stage.unitStateTest(145, 7, 0, 2):
        stage.resumeCtrl()
        stage.say('@dialogue:S_05.scene1.dialogue0024')
        stage.getItem(81, 0)
        stage.unit(0).setDir(stage.countDir(0, 145))
        stage.say('@dialogue:S_05.scene1.dialogue0025')
        stage.talk(21, 186, '조조 주군, 복양에서 보고가 왔습니다…\n수비군이 여포와의 싸움에서 패하여 복양이 함락되었습니다…….')
        stage.say('@dialogue:S_05.scene1.dialogue0026')
        stage.unit(0).setDir(3)
        stage.unit(0).setAction(5)
        stage.say('@dialogue:S_05.scene1.dialogue0027')
        stage.unit(0).setAction(0)
        vars[50] = 1
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[101] = 1
        vars[605] = 1
        return True
    if not vars[51] and stage.unitStateTest(41, 7, 0, 2):
        stage.say('@dialogue:S_05.scene1.dialogue0028')
        stage.unit(41).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(32, 7, 0, 2):
        stage.say('@dialogue:S_05.scene1.dialogue0029')
        stage.unit(32).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(33, 7, 0, 2):
        stage.say('@dialogue:S_05.scene1.dialogue0030')
        stage.unit(33).hide(1)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(3, 7, 0, 2):
        stage.say('@dialogue:S_05.scene1.dialogue0031')
        stage.unit(3).hide(1)
        vars[54] = 1
    if not vars[55] and stage.unitStateTest(34, 7, 0, 2):
        stage.say('@dialogue:S_05.scene1.dialogue0032')
        stage.unit(34).hide(1)
        vars[55] = 1
    if vars[100] and vars[101] and not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_05.scene1.dialogue0033')
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
