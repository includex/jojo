# Source Generated with Decompyle++
# File: S_06.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20,
        21,
        22,
        23,
        24,
        25], [
        '(18,17)',
        '(18,16)',
        '(19,16)',
        '(20,16)',
        '(4,16)',
        '(19,17)'])
    stage.nearEvent([
        16,
        2])
    stage.nearEvent([
        0,
        119,
        16,
        125,
        0,
        126], 1)
    stage.initFight()
    stage.setGlobalData(20, -2, -1, 0, 1, 1)
    stage.createEnemy(
        [{'id': 119, 'x': 19, 'y': 17, 'lv': 6, 'i': 0},
         {'id': 126, 'x': 18, 'y': 16, 'lv': 6, 'i': 1},
         {'id': 290, 'x': 19, 'y': 16, 'lv': 0, 'i': 2},
         {'id': 291, 'x': 20, 'y': 16, 'lv': 0, 'i': 3},
         {'id': 268, 'x': 17, 'y': 16, 'dir': 3, 'lv': 0, 'i': 4},
         {'id': 269, 'x': 17, 'y': 17, 'dir': 3, 'lv': 0, 'i': 5},
         {'id': 270, 'x': 19, 'y': 15, 'dir': 0, 'lv': 0, 'i': 6},
         {'id': 271, 'x': 20, 'y': 15, 'dir': 0, 'lv': 0, 'i': 7},
         {'id': 272, 'x': 22, 'y': 16, 'dir': 1, 'lv': 0, 'i': 8},
         {'id': 273, 'x': 22, 'y': 17, 'dir': 1, 'lv': 0, 'i': 9},
         {'id': 274, 'x': 19, 'y': 18, 'lv': 0, 'i': 10},
         {'id': 275, 'x': 20, 'y': 18, 'lv': 0, 'i': 11},
         {'id': 258, 'x': 11, 'y': 16, 'dir': 3, 'lv': 0, 'i': 12},
         {'id': 259, 'x': 11, 'y': 17, 'dir': 3, 'lv': 0, 'i': 13},
         {'id': 260, 'x': 19, 'y': 9, 'dir': 0, 'lv': 0, 'i': 14},
         {'id': 261, 'x': 20, 'y': 9, 'dir': 0, 'lv': 0, 'i': 15},
         {'id': 282, 'x': 12, 'y': 16, 'dir': 3, 'lv': 0, 'i': 16},
         {'id': 283, 'x': 12, 'y': 17, 'dir': 3, 'lv': 0, 'i': 17},
         {'id': 284, 'x': 19, 'y': 10, 'dir': 0, 'lv': 0, 'i': 18},
         {'id': 285, 'x': 20, 'y': 10, 'dir': 0, 'lv': 0, 'i': 19},
         {'id': 218, 'x': 9, 'y': 16, 'dir': 3, 'lv': 0, 'i': 20},
         {'id': 219, 'x': 9, 'y': 17, 'dir': 3, 'lv': 0, 'i': 21},
         {'id': 220, 'x': 19, 'y': 7, 'dir': 0, 'lv': 0, 'i': 22},
         {'id': 221, 'x': 20, 'y': 7, 'dir': 0, 'lv': 0, 'i': 23},
         {'id': 236, 'x': 9, 'y': 15, 'dir': 3, 'lv': 0, 'i': 24},
         {'id': 237, 'x': 9, 'y': 18, 'dir': 3, 'lv': 0, 'i': 25},
         {'id': 238, 'x': 18, 'y': 7, 'dir': 0, 'lv': 0, 'i': 26},
         {'id': 239, 'x': 21, 'y': 7, 'dir': 0, 'lv': 0, 'i': 27},
         {'id': 2, 'x': 4, 'y': 16, 'dir': 0, 'lv': 6, 'ai': 0, 'i': 28},
         {'id': 262, 'x': 3, 'y': 15, 'dir': 0, 'lv': 0, 'ai': 0, 'i': 29},
         {'id': 263, 'x': 4, 'y': 15, 'dir': 0, 'lv': 0, 'ai': 0, 'i': 30},
         {'id': 264, 'x': 5, 'y': 15, 'dir': 0, 'lv': 0, 'ai': 0, 'i': 31},
         {'id': 286, 'x': 3, 'y': 16, 'dir': 0, 'lv': 0, 'ai': 0, 'i': 32},
         {'id': 287, 'x': 5, 'y': 16, 'dir': 0, 'lv': 0, 'ai': 0, 'i': 33},
         {'id': 125, 'x': 17, 'y': 3, 'dir': 3, 'lv': 4, 'ai': 0, 'i': 34},
         {'id': 265, 'x': 16, 'y': 2, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 35},
         {'id': 266, 'x': 16, 'y': 3, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 36},
         {'id': 267, 'x': 16, 'y': 4, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 37},
         {'id': 288, 'x': 17, 'y': 2, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 38},
         {'id': 289, 'x': 17, 'y': 4, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 39}]
    )
    stage.createMine([
        {
            'idx': 0,
            'x': 3,
            'y': 1,
            'dir': 1 },
        {
            'idx': 1,
            'x': 4,
            'y': 1,
            'dir': 1 },
        {
            'idx': 2,
            'x': 3,
            'y': 2,
            'dir': 1 },
        {
            'idx': 3,
            'x': 4,
            'y': 2,
            'dir': 1 },
        {
            'idx': 4,
            'x': 5,
            'y': 1,
            'dir': 1 },
        {
            'idx': 5,
            'x': 3,
            'y': 3,
            'dir': 1 },
        {
            'idx': 6,
            'x': 4,
            'dir': 1 },
        {
            'idx': 7,
            'x': 2,
            'y': 2,
            'dir': 1 },
        {
            'idx': 8,
            'x': 3,
            'dir': 1 },
        {
            'idx': 9,
            'x': 2,
            'y': 1,
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
    stage.setEnemyEquip(119, 38, 0, -1, 0, 16)
    stage.setEnemyEquip(125, -1, 0, -1, 0, 19)
    stage.setEnemyEquip(126, -1, 0, -1, 0, 8)
    stage.loadBg(6)
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab26')
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
            goto('lab26')
    stage.draw()
    stage.bgSound(3)
    stage.unit(119).setDir(stage.countDir(119, 0))
    stage.say('@dialogue:S_06.scene0.dialogue0001')
    stage.unit(16).setAction(7)
    stage.say('@dialogue:S_06.scene0.dialogue0002')
    stage.unit(16).setAction(0)
    stage.say('@dialogue:S_06.scene0.dialogue0003')
    stage.unit(0).setAction(5)
    stage.say('@dialogue:S_06.scene0.dialogue0004')
    stage.unit(0).setAction(0)
    stage.setWinCondition('승리 조건\n1. 모든 적군을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
    stage.showWinCondition('모든 적을 섬멸하라!')
    stage.startOper()
    vars[506] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[10] and stage.curCamp() == 2 and stage.round() == 3:
        if not vars[6] and not vars[7] and not vars[52] and not vars[53]:
            stage.unit(2).setDir(stage.countDir(2, 0))
            stage.unit(2).setAction(6)
            stage.say('@dialogue:S_06.scene1.dialogue0001')
            stage.unit(2).setAction(0)
            stage.unit(125).setDir(stage.countDir(125, 0))
            stage.unit(125).setAction(6)
            stage.say('@dialogue:S_06.scene1.dialogue0002')
            stage.unit(125).setAction(0)
        stage.unit(2).setAI(1)
        stage.unit(125).setAI(1)
        stage.unit(262).setAI(1)
        stage.unit(263).setAI(1)
        stage.unit(264).setAI(1)
        stage.unit(265).setAI(1)
        stage.unit(266).setAI(1)
        stage.unit(267).setAI(1)
        stage.unit(286).setAI(1)
        stage.unit(287).setAI(1)
        stage.unit(288).setAI(1)
        stage.unit(289).setAI(1)
        vars[6] = 1
        vars[7] = 1
        vars[10] = 1
    if not vars[2] and not vars[11] and not vars[30] and not vars[50] and not vars[51] and stage.curCamp() == 2 and stage.round() == 4:
        stage.unit(126).setDir(stage.countDir(126, 119))
        stage.say('@dialogue:S_06.scene1.dialogue0003')
        stage.unit(119).setDir(stage.countDir(119, 126))
        stage.say('@dialogue:S_06.scene1.dialogue0004')
        stage.unit(126).setDir(stage.countDir(126, 0))
        stage.unit(119).setDir(stage.countDir(119, 0))
        stage.unit(126).setAction(5)
        stage.say('@dialogue:S_06.scene1.dialogue0005')
        stage.unit(126).setAction(0)
        stage.unit(258).setAI(1)
        stage.unit(259).setAI(1)
        stage.unit(260).setAI(1)
        stage.unit(261).setAI(1)
        stage.unit(282).setAI(1)
        stage.unit(283).setAI(1)
        stage.unit(284).setAI(1)
        stage.unit(285).setAI(1)
        vars[11] = 1
    if not vars[2] and not vars[12] and not vars[30] and not vars[50] and not vars[51] and stage.curCamp() == 2 and stage.round() == 6:
        stage.say('@dialogue:S_06.scene1.dialogue0006')
        stage.unit(119).setAction(6)
        stage.say('@dialogue:S_06.scene1.dialogue0007')
        stage.unit(119).setAction(0)
        stage.unit(218).setAI(1)
        stage.unit(219).setAI(1)
        stage.unit(220).setAI(1)
        stage.unit(221).setAI(1)
        stage.unit(236).setAI(1)
        stage.unit(237).setAI(1)
        stage.unit(238).setAI(1)
        stage.unit(239).setAI(1)
        vars[12] = 1
    if not vars[2] and not vars[13] and not vars[30] and not vars[50] and not vars[51] and stage.curCamp() == 2 and stage.round() == 8:
        stage.unit(126).setDir(stage.countDir(126, 119))
        stage.say('@dialogue:S_06.scene1.dialogue0008')
        stage.unit(119).setAction(7)
        stage.unit(119).setAction(5)
        stage.say('@dialogue:S_06.scene1.dialogue0009')
        stage.unit(119).setAction(0)
        stage.unit(126).setDir(stage.countDir(126, 0))
        stage.setAI(16, 14, 23, 19, 2, 1)
        vars[13] = 1
    if not vars[14] and not vars[50] and not vars[51] and stage.totalUnit(2) >= 9 and stage.round() >= 7 and stage.round() < 10 and stage.totalUnit(0) < 10:
        if not vars[54]:
            stage.unit(16).setAction(4)
            stage.unit(16).setDir(stage.countDir(16, 0))
            stage.unit(14).setDir(stage.countDir(14, 0))
            stage.say('@dialogue:S_06.scene1.dialogue0010')
        else:
            stage.say('@dialogue:S_06.scene1.dialogue0011')
        sel = stage.choice('퇴각한다\n복양 공격을 계속한다', -1)
        if sel == 1:
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_06.scene1.dialogue0012')
            stage.unit(0).setAction(0)
            stage.setWinCondition('승리 조건\n1. 조조가 북쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
            stage.showWinCondition('조조가 달아난다!')
            stage.heightLight(0, 0, 5, 0)
            vars[2] = 1
            stage.unit(126).setDir(stage.countDir(126, 119))
            stage.say('@dialogue:S_06.scene1.dialogue0013')
            stage.unit(126).setDir(stage.countDir(126, 0))
            stage.unit(119).setDir(stage.countDir(119, 0))
            stage.unit(119).setAction(6)
            stage.say('@dialogue:S_06.scene1.dialogue0014')
            stage.unit(119).setAction(0)
            stage.setAI(0, 0, 27, 23, 2, 3, 0, 0, 0)
        elif sel == 2:
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_06.scene1.dialogue0015')
            stage.unit(0).setAction(0)
        vars[14] = 1
    if vars[13] and not vars[14] and not vars[15] and not vars[50] and not vars[51] and stage.round() == 10:
        if not vars[54]:
            stage.unit(16).setAction(4)
            stage.unit(16).setDir(stage.countDir(16, 0))
            stage.unit(14).setDir(stage.countDir(14, 0))
            stage.say('@dialogue:S_06.scene1.dialogue0016')
        else:
            stage.say('@dialogue:S_06.scene1.dialogue0017')
        sel = stage.choice('퇴각한다\n복양 공격을 계속한다', -1)
        if sel == 1:
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_06.scene1.dialogue0018')
            stage.unit(0).setAction(0)
            stage.setWinCondition('승리 조건\n1. 조조가 북쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
            stage.showWinCondition('조조가 달아난다!')
            stage.heightLight(0, 0, 5, 0)
            vars[2] = 1
            stage.unit(126).setDir(stage.countDir(126, 119))
            stage.say('@dialogue:S_06.scene1.dialogue0019')
            stage.unit(126).setDir(stage.countDir(126, 0))
            stage.unit(119).setDir(stage.countDir(119, 0))
            stage.unit(119).setAction(6)
            stage.say('@dialogue:S_06.scene1.dialogue0020')
            stage.unit(119).setAction(0)
            stage.setAI(0, 0, 27, 23, 2, 3, 0, 0, 0)
        elif sel == 2:
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_06.scene1.dialogue0021')
            stage.unit(0).setAction(0)
        vars[15] = 1
    if not vars[2] and not vars[16] and stage.totalUnit(2) >= 7 and stage.round() >= 12 and stage.totalUnit(0) < 8:
        if not vars[54]:
            stage.unit(16).setAction(4)
            stage.unit(16).setDir(stage.countDir(16, 0))
            stage.say('@dialogue:S_06.scene1.dialogue0022')
        else:
            stage.say('@dialogue:S_06.scene1.dialogue0023')
        sel = stage.choice('퇴각한다\n복양 공격을 계속한다', -1)
        if sel == 1:
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_06.scene1.dialogue0024')
            stage.unit(0).setAction(0)
            stage.setWinCondition('승리 조건\n1. 조조가 북쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
            stage.showWinCondition('조조가 달아난다!')
            stage.heightLight(0, 0, 5, 0)
            vars[2] = 1
            if not vars[50] and not vars[51]:
                stage.unit(126).setDir(stage.countDir(126, 119))
                stage.say('@dialogue:S_06.scene1.dialogue0025')
                stage.unit(126).setDir(stage.countDir(126, 0))
                stage.unit(119).setDir(stage.countDir(119, 0))
                stage.unit(119).setAction(6)
                stage.say('@dialogue:S_06.scene1.dialogue0026')
                stage.unit(119).setAction(0)
            stage.setAI(0, 0, 27, 23, 2, 3, 0, 0, 0)
        elif sel == 2:
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_06.scene1.dialogue0027')
            stage.unit(0).setAction(0)
        vars[16] = 1
    if not vars[20] and stage.isInPos(1025, 18, 17):
        stage.getItem(157, 0, True, 1025)
        vars[20] = 1
    if not vars[21] and stage.isInPos(1025, 18, 16):
        stage.getItem(150, 0, True, 1025)
        vars[21] = 1
    if not vars[22] and stage.isInPos(1025, 19, 16):
        stage.getItem(150, 0, True, 1025)
        vars[22] = 1
    if not vars[23] and stage.isInPos(1025, 20, 16):
        stage.getItem(150, 0, True, 1025)
        vars[23] = 1
    if not vars[24] and stage.isInPos(1025, 4, 16):
        stage.getItem(150, 0, True, 1025)
        vars[24] = 1
    if not vars[25] and stage.isInPos(1025, 19, 17):
        stage.getItem(151, 0, True, 1025)
        vars[25] = 1
    if not vars[6] and stage.totalRectUnit(2, 3, 15, 5, 16) == 6 and stage.curCamp() == 2 and stage.totalRectUnit(0, 8, 0, 27, 5) >= 6:
        stage.unit(2).setAction(5)
        stage.say('@dialogue:S_06.scene1.dialogue0028')
        stage.unit(2).setAction(0)
        stage.unit(2).setAI(1)
        stage.unit(262).setAI(1)
        stage.unit(263).setAI(1)
        stage.unit(264).setAI(1)
        stage.unit(286).setAI(1)
        stage.unit(287).setAI(1)
        stage.unit(125).setAI(1)
        stage.unit(265).setAI(1)
        stage.unit(266).setAI(1)
        stage.unit(267).setAI(1)
        stage.unit(288).setAI(1)
        stage.unit(289).setAI(1)
        vars[6] = 1
    if not vars[7] and stage.totalRectUnit(2, 16, 2, 17, 4) == 6 and stage.curCamp() == 2 and stage.totalRectUnit(0, 0, 7, 7, 23) >= 6:
        stage.unit(125).setAction(5)
        stage.say('@dialogue:S_06.scene1.dialogue0029')
        stage.unit(125).setAction(0)
        stage.unit(125).setAI(1)
        stage.unit(265).setAI(1)
        stage.unit(266).setAI(1)
        stage.unit(267).setAI(1)
        stage.unit(288).setAI(1)
        stage.unit(289).setAI(1)
        stage.unit(2).setAI(1)
        stage.unit(262).setAI(1)
        stage.unit(263).setAI(1)
        stage.unit(264).setAI(1)
        stage.unit(286).setAI(1)
        stage.unit(287).setAI(1)
        vars[7] = 1
    if not vars[12] and not vars[30] and stage.isInRect(1025, 8, 6, 27, 23) and stage.curCamp() == 2:
        stage.unit(126).setAction(10)
        stage.unit(126).setAction(5)
        stage.say('@dialogue:S_06.scene1.dialogue0030')
        stage.unit(126).setAction(0)
        stage.unit(2).setAI(1)
        stage.unit(125).setAI(1)
        stage.unit(262).setAI(1)
        stage.unit(263).setAI(1)
        stage.unit(264).setAI(1)
        stage.unit(265).setAI(1)
        stage.unit(266).setAI(1)
        stage.unit(267).setAI(1)
        stage.unit(286).setAI(1)
        stage.unit(287).setAI(1)
        stage.unit(288).setAI(1)
        stage.unit(289).setAI(1)
        if not vars[11]:
            stage.unit(258).setAI(0)
            stage.unit(259).setAI(0)
            stage.unit(260).setAI(0)
            stage.unit(261).setAI(0)
            stage.unit(282).setAI(0)
            stage.unit(283).setAI(0)
            stage.unit(284).setAI(0)
            stage.unit(285).setAI(0)
        if not vars[12]:
            stage.unit(218).setAI(0)
            stage.unit(219).setAI(0)
            stage.unit(220).setAI(0)
            stage.unit(221).setAI(0)
            stage.unit(236).setAI(0)
            stage.unit(237).setAI(0)
            stage.unit(238).setAI(0)
            stage.unit(239).setAI(0)
        vars[30] = 1
    if not vars[2] and not vars[13] and not vars[31] and stage.curCamp() == 2 and stage.isInRect(1025, 13, 11, 26, 22):
        stage.unit(119).setAction(5)
        stage.say('@dialogue:S_06.scene1.dialogue0031')
        stage.unit(119).setAction(0)
        stage.setAI(16, 14, 23, 19, 2, 0)
        stage.unit(258).setAI(1)
        stage.unit(259).setAI(1)
        stage.unit(260).setAI(1)
        stage.unit(261).setAI(1)
        stage.unit(282).setAI(1)
        stage.unit(283).setAI(1)
        stage.unit(284).setAI(1)
        stage.unit(285).setAI(1)
        stage.unit(218).setAI(1)
        stage.unit(219).setAI(1)
        stage.unit(220).setAI(1)
        stage.unit(221).setAI(1)
        stage.unit(236).setAI(1)
        stage.unit(237).setAI(1)
        stage.unit(238).setAI(1)
        stage.unit(239).setAI(1)
        vars[31] = 1
    if vars[2] and not vars[32] and stage.isInRect(0, 0, 0, 5, 0):
        stage.resumeCtrl()
        stage.say('@dialogue:S_06.scene1.dialogue0032')
        stage.reward()
        stage.end()
        vars[32] = 1
        vars[0] = 1
        vars[100] = 1
        vars[606] = 1
        vars[706] = 0
        return True
    if not vars[40] and stage.isNear(0, 119, True):
        stage.unit(0).setDir(stage.countDir(0, 119))
        stage.unit(119).setDir(stage.countDir(119, 0))
        stage.say('@dialogue:S_06.scene1.dialogue0033')
        vars[40] = 1
    if not vars[41] and stage.isNear(16, 2, False):
        stage.unit(2).setDir(stage.countDir(2, 16))
        stage.unit(16).setDir(stage.countDir(16, 2))
        stage.say('@dialogue:S_06.scene1.dialogue0034')
        fight = stage.startFight(16, 2, 5)
        fight.showUnit(True, '나는 전위다.\n우리 주군의 무서움도 모르는 놈!\n어서 이름을 대라!', 9)
        fight.showUnit(False, '내 이름을 들으면 간담이 서늘할 것이다!\n나는 장료 장문원이다!\n다시 말하겠다. 어서 비켜라!', 8)
        fight.setAction(True, 6)
        fight.say(True, '목숨을 걸고 주군을 지키겠다.\n지나가려면 먼저 나를 쓰러뜨려라!', True)
        fight.setAction(False, 9)
        fight.say(False, '으음! 그렇다면 먼저 너를 죽이겠다!', True)
        fight.showStart()
        fight.setAction(True, 0)
        fight.setAction(False, 2)
        fight.attack1(False, 1, False)
        fight.attack2(False, 0, True)
        fight.attack2(False, 1, True)
        stage.delay(5)
        fight.setAction(True, 1)
        fight.say(True, '뭐?! 통하지 않아!', True)
        fight.setAction(False, 1)
        fight.say(False, '으음!', True)
        fight.attack2(False, 0, True)
        fight.attack2(True, 0, True)
        fight.attack2(False, 0, True)
        fight.attack2(False, 0, True)
        fight.attack1(False, 3, True)
        fight.setAction(False, 2)
        fight.attack1(False, 1, False)
        fight.attack2(True, 0, True)
        fight.attack1(True, 4, True)
        fight.setAction(True, 1)
        fight.attack1(False, 3, True)
        fight.setAction(True, 2)
        fight.attack2(True, 0, True)
        fight.attack2(False, 0, True)
        fight.attack2(False, 0, True)
        fight.attack2(False, 0, True)
        fight.attack2(True, 0, True)
        fight.attack2(True, 0, True)
        fight.attack1(False, 2, True)
        stage.delay(10)
        fight.setAction(True, 8)
        stage.delay(10)
        fight.setAction(False, 8)
        stage.delay(10)
        fight.say(True, '(천하가 이토록 넓다니!\n이런 강적이 또 있을 줄이야!)', True)
        fight.say(False, '(이것이……조조군의 실력이란 말인가.\n악래 전위, 소문보다도 더 용맹하구나.)', True)
        fight.end()
        vars[41] = 1
    if not vars[42] and stage.isNear(16, 125, False):
        stage.unit(125).setDir(stage.countDir(125, 16))
        stage.unit(16).setDir(stage.countDir(16, 125))
        stage.say('@dialogue:S_06.scene1.dialogue0035')
        stage.attackAction(16, 125, 0)
        stage.unit(125).setAction(8)
        stage.unit(125).setAction(4)
        stage.say('@dialogue:S_06.scene1.dialogue0036')
        vars[42] = 1
    if not vars[43] and stage.isNear(0, 126, True):
        stage.unit(0).setDir(stage.countDir(0, 126))
        stage.unit(126).setDir(stage.countDir(126, 0))
        stage.say('@dialogue:S_06.scene1.dialogue0037')
        vars[43] = 1
    if not vars[50] and stage.unitStateTest(119, 7, 0, 2):
        stage.say('@dialogue:S_06.scene1.dialogue0038')
        stage.unit(119).hide(1)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(126, 7, 0, 2):
        stage.say('@dialogue:S_06.scene1.dialogue0039')
        stage.unit(126).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(2, 7, 0, 2):
        stage.say('@dialogue:S_06.scene1.dialogue0040')
        stage.unit(2).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(125, 7, 0, 2):
        stage.say('@dialogue:S_06.scene1.dialogue0041')
        stage.unit(125).hide(1)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(16, 7, 0, 2):
        stage.say('@dialogue:S_06.scene1.dialogue0042')
        stage.unit(16).retreatTxt(False)
        stage.unit(16).hide(1)
        vars[54] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_06.scene1.dialogue0043')
        stage.reward(0, [
            38,
            -1])
        stage.end()
        vars[0] = 1
        vars[606] = 1
        vars[706] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 여포군에게 패했다.')
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
