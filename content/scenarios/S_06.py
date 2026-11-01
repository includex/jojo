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
    stage.say('&119\n조조, 오랜만이군.\n난세에서는 흔한 일 아닌가.\n너무 탓하지 말게!')
    stage.unit(16).setAction(7)
    stage.say('&16\n도망치지 마라!\n내가 네 목숨을 거두겠다.')
    stage.unit(16).setAction(0)
    stage.say('&0\n(여포가 군사를 일으킬 줄이야…….\n서주를 공격했어야 했던가……?)\n&0\n(전쟁에 사사로운 원한을 끌어들여서는 안 된다…….\n이번 일을 가슴 깊이 새겨 두어야겠어.)')
    stage.unit(0).setAction(5)
    stage.say('&0\n좋다. 전군 공격하라.')
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
            stage.say('&2\n장패! 협공을 시작하라. 조조군을 무찔러라!')
            stage.unit(2).setAction(0)
            stage.unit(125).setDir(stage.countDir(125, 0))
            stage.unit(125).setAction(6)
            stage.say('&125\n좋다! 전군 돌격하라!\n이 장패가 나선 이상 우리 군은 반드시 이긴다!\n조조군도 이제 끝이다. 하하하.')
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
        stage.say('&126\n여 장군, 때가 되었습니다.\n제1진에 공격을 명하십시오.')
        stage.unit(119).setDir(stage.countDir(119, 126))
        stage.say('&119\n음. 이제 나도 출전해야 하지 않겠는가?\n&126\n여 장군께서는 이곳을 지켜 주시면 됩니다.\n이번 싸움에서는 반드시 조조에게………\n우리 군의 무용과 지략을 깊이 새겨 주어야 합니다.\n&119\n어째서인가?\n&126\n지금은 제후들이 각지에 할거하는 난세입니다.\n한 나라와 싸우는 것밖에 모른다면 살아남지 못합니다.\n&126\n강한 전력을 보여 줄 수 있다면\n앞으로의 외교에도 도움이 될 것입니다.\n바로 그것이 제 목적입니다.\n&119\n그렇군!\n그럼 이곳은 자네에게 맡기겠네.\n자네의 재주를 마음껏 펼치게.')
        stage.unit(126).setDir(stage.countDir(126, 0))
        stage.unit(119).setDir(stage.countDir(119, 0))
        stage.unit(126).setAction(5)
        stage.say('&126\n제1진, 공세로 전환하라!')
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
        stage.say('&126\n제2진, 출격하라.')
        stage.unit(119).setAction(6)
        stage.say('&119\n좋다! 수비병도 함께 조조를 추격하라.\n보병대, 궁병대 출격하라!')
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
        stage.say('&126\n여 장군, 출격해 주십시오.\n지칠 대로 지친 조조군에게 최후의 일격을 가하십시오!')
        stage.unit(119).setAction(7)
        stage.unit(119).setAction(5)
        stage.say('&119\n음! 반드시 조조의 목을 베겠다.\n전군은 명을 받들라. 돌격하라!\n&126\n여 장군, 이미 일국의 군주이심을 잊지 마십시오.\n무모하게 나서시면 안 됩니다.\n&119\n알고 있네. 초선을 위해서라도 죽을 수는 없지.\n반드시 이 난세에서 살아남겠다!')
        stage.unit(119).setAction(0)
        stage.unit(126).setDir(stage.countDir(126, 0))
        stage.setAI(16, 14, 23, 19, 2, 1)
        vars[13] = 1
    if not vars[14] and not vars[50] and not vars[51] and stage.totalUnit(2) >= 9 and stage.round() >= 7 and stage.round() < 10 and stage.totalUnit(0) < 10:
        if not vars[54]:
            stage.unit(16).setAction(4)
            stage.unit(16).setDir(stage.countDir(16, 0))
            stage.unit(14).setDir(stage.countDir(14, 0))
            stage.say('&16\n으음, 형세가 좋지 않습니다, 주군.\n우선 군사를 물리십시오. 더는 버티기 어렵습니다.\n&14\n주군, 저도 전위 장군과 같은 생각입니다.\n이 성을 함락하려면 더 철저한 준비가 필요합니다.')
        else:
            stage.say('&0\n이대로라면 정말 우리 군에 불리한 것인가?\n일단 물러날까? 아니면 공격을 계속할까……?')
        sel = stage.choice('퇴각한다\n복양 공격을 계속한다', -1)
        if sel == 1:
            stage.unit(0).setAction(5)
            stage.say('&0\n전군 퇴각하라!\n물러나서 전열을 정비하겠다!')
            stage.unit(0).setAction(0)
            stage.setWinCondition('승리 조건\n1. 조조가 북쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
            stage.showWinCondition('조조가 달아난다!')
            stage.heightLight(0, 0, 5, 0)
            vars[2] = 1
            stage.unit(126).setDir(stage.countDir(126, 119))
            stage.say('&126\n적을 쉽게 놓아주어서는 안 된다.\n전군은 조조를 추격하라.')
            stage.unit(126).setDir(stage.countDir(126, 0))
            stage.unit(119).setDir(stage.countDir(119, 0))
            stage.unit(119).setAction(6)
            stage.say('&119\n음! 조조를 추격하라!\n절대로 놓쳐서는 안 된다!')
            stage.unit(119).setAction(0)
            stage.setAI(0, 0, 27, 23, 2, 3, 0, 0, 0)
        elif sel == 2:
            stage.unit(0).setAction(5)
            stage.say('&0\n어린 여포 놈을 상대하기에는\n이 병력으로도 충분하다!\n모두 기운을 내라!')
            stage.unit(0).setAction(0)
        vars[14] = 1
    if vars[13] and not vars[14] and not vars[15] and not vars[50] and not vars[51] and stage.round() == 10:
        if not vars[54]:
            stage.unit(16).setAction(4)
            stage.unit(16).setDir(stage.countDir(16, 0))
            stage.unit(14).setDir(stage.countDir(14, 0))
            stage.say('&16\n으음, 형세가 좋지 않습니다, 주군.\n우선 군사를 물리십시오. 더는 버티기 어렵습니다.\n&14\n주군, 저도 전위 장군과 같은 생각입니다.\n이 성을 함락하려면 더 철저한 준비가 필요합니다.')
        else:
            stage.say('&0\n정녕 이대로는 우리 군이 불리한가?\n잠시 물러나야 할까, 계속 공격해야 할까……?')
        sel = stage.choice('퇴각한다\n복양 공격을 계속한다', -1)
        if sel == 1:
            stage.unit(0).setAction(5)
            stage.say('&0\n전군 퇴각하라!\n물러나서 전열을 정비하겠다!')
            stage.unit(0).setAction(0)
            stage.setWinCondition('승리 조건\n1. 조조가 북쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
            stage.showWinCondition('조조가 달아난다!')
            stage.heightLight(0, 0, 5, 0)
            vars[2] = 1
            stage.unit(126).setDir(stage.countDir(126, 119))
            stage.say('&126\n적을 쉽게 놓아주어서는 안 된다.\n전군은 조조를 추격하라.')
            stage.unit(126).setDir(stage.countDir(126, 0))
            stage.unit(119).setDir(stage.countDir(119, 0))
            stage.unit(119).setAction(6)
            stage.say('&119\n음! 조조를 추격하라!\n절대로 놓쳐서는 안 된다!')
            stage.unit(119).setAction(0)
            stage.setAI(0, 0, 27, 23, 2, 3, 0, 0, 0)
        elif sel == 2:
            stage.unit(0).setAction(5)
            stage.say('&0\n어린 여포 놈을 상대하기에는\n이 병력으로도 충분하다!\n모두 기운을 내라!')
            stage.unit(0).setAction(0)
        vars[15] = 1
    if not vars[2] and not vars[16] and stage.totalUnit(2) >= 7 and stage.round() >= 12 and stage.totalUnit(0) < 8:
        if not vars[54]:
            stage.unit(16).setAction(4)
            stage.unit(16).setDir(stage.countDir(16, 0))
            stage.say('&16\n으음, 형세가 좋지 않습니다, 주군.\n잠시 물러나시지요. 더는 버틸 수 없습니다.')
        else:
            stage.say('&0\n전황이 급박하군.\n잠시 물러나야 할까, 계속 공격해야 할까……?')
        sel = stage.choice('퇴각한다\n복양 공격을 계속한다', -1)
        if sel == 1:
            stage.unit(0).setAction(5)
            stage.say('&0\n전군 퇴각하라! 돌아가 전열을 정비한다!')
            stage.unit(0).setAction(0)
            stage.setWinCondition('승리 조건\n1. 조조가 북쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
            stage.showWinCondition('조조가 달아난다!')
            stage.heightLight(0, 0, 5, 0)
            vars[2] = 1
            if not vars[50] and not vars[51]:
                stage.unit(126).setDir(stage.countDir(126, 119))
                stage.say('&126\n적을 쉽게 놓아주어서는 안 된다.\n전군은 조조를 추격하라.')
                stage.unit(126).setDir(stage.countDir(126, 0))
                stage.unit(119).setDir(stage.countDir(119, 0))
                stage.unit(119).setAction(6)
                stage.say('&119\n음! 조조를 추격하라!\n절대로 놓쳐서는 안 된다!')
                stage.unit(119).setAction(0)
            stage.setAI(0, 0, 27, 23, 2, 3, 0, 0, 0)
        elif sel == 2:
            stage.unit(0).setAction(5)
            stage.say('&0\n어린 여포 놈을 상대하기에는\n이 병력으로도 충분하다!\n모두 기운을 내라!')
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
        stage.say('&2\n우리 목적은 조조군을 혼란에 빠뜨리는 것이다.\n신속히 접근하여 적의 전투력을 꺾어라!\n전군 돌격하라!')
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
        stage.say('&125\n좋은 기회다. 출전하자!\n조조를 죽이면 나 장패가 큰 공을 세우겠군!\n하하하.')
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
        stage.say('&126\n성문을 빼앗기면 곤란해진다.\n성문의 수비를 강화하라.\n다른 성문도 모두 수비를 강화하라!')
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
        stage.say('&119\n쓸모없는 놈들……!\n적이 코앞까지 왔지 않은가!\n내가 나가 싸우겠다.')
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
        stage.say('&0\n후우, 겨우 위기를 벗어났군!')
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
        stage.say('&0\n여포도 임기응변에 능할 줄이야.\n참으로 칭찬할 만하군.\n&119\n난세에 이름을 세운 자가 너뿐인 줄 아느냐!\n너무 자만하지 마라!')
        vars[40] = 1
    if not vars[41] and stage.isNear(16, 2, False):
        stage.unit(2).setDir(stage.countDir(2, 16))
        stage.unit(16).setDir(stage.countDir(16, 2))
        stage.say('&2\n너와 놀아 줄 시간 없다! 어서 비켜라.\n&16\n헛소리 마라! 내가 있는 한\n우리 주군의 털끝 하나 건드리지 못한다!')
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
        stage.say('&125\n이봐, 너 말이다. 조조는 어디 있느냐?\n&16\n네놈은 또 뭐냐?\n어찌 그리 거들먹거리느냐!\n알고 싶거든 나부터 쓰러뜨려라.')
        stage.attackAction(16, 125, 0)
        stage.unit(125).setAction(8)
        stage.unit(125).setAction(4)
        stage.say('&16\n뭐야…… 입만 살아 있는 놈이냐?\n&125\n뭐라고? 아야……!\n지금은 내가 우세하다고…… 아이고!\n분명 모든 면에서 내가 더 나은데…… 아프다.\n&125\n허나 같은 무장인 체면을 봐서\n이번에는 비긴 것으로 해 주지.\n아이고, 아파 죽겠네…….')
        vars[42] = 1
    if not vars[43] and stage.isNear(0, 126, True):
        stage.unit(0).setDir(stage.countDir(0, 126))
        stage.unit(126).setDir(stage.countDir(126, 0))
        stage.say('&0\n그대가 여포의 군사인가?\n이름이 무엇인가?\n&126\n싸움이 한창인데 무슨 한담이냐!\n나는 진궁, 자는 공대다. 똑똑히 기억하라!\n&0\n진궁, 그대의 활약은 참으로 훌륭하오.\n내 휘하로 들어오는 것이 어떻겠소?\n&126\n충신은 두 주군을 섬기지 않는다. 게다가………\n그대도 싸움 중에 배신하는 자는 쓰지 않을 터.\n&0\n그리 말할수록 그대를 더욱 쓰고 싶어지는군.\n&126\n참으로 두려운 인물이로군. 하지만 내 마음에는\n…………오직 우리 주군만 계신다.\n단념하시오.')
        vars[43] = 1
    if not vars[50] and stage.unitStateTest(119, 7, 0, 2):
        stage.say('&119\n나는 죽을 수 없다. 우선 물러나 다음 기회를 기다리겠다.\n퇴각하라!')
        stage.unit(119).hide(1)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(126, 7, 0, 2):
        stage.say('&126\n상황이 좋지 않군. 퇴각하는 것이 현명하겠다.\n퇴각하라!')
        stage.unit(126).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(2, 7, 0, 2):
        stage.say('&2\n이번에는 이 장료가 너무 방심했군. 퇴각하라!')
        stage.unit(2).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(125, 7, 0, 2):
        stage.say('&125\n빌어먹을, 이 몸은 더 싸워 주고 싶지만\n갑자기 급한 일이 생겼구나!\n이번에는 살려 주마. 다음에 보자.')
        stage.unit(125).hide(1)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(16, 7, 0, 2):
        stage.say('&16\n수만 믿고 덤비다니 비겁하구나!\n대장부라면 나와 일대일로 겨뤄라!\n겁쟁이들아!')
        stage.unit(16).retreatTxt(False)
        stage.unit(16).hide(1)
        vars[54] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('&0\n좋다, 마침내 적을 섬멸했다!\n우리의 승리다!')
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
