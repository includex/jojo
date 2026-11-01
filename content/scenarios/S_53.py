# Source Generated with Decompyle++
# File: S_53.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20,
        21,
        22,
        23], [
        '(13,4)',
        '(14,3)',
        '(13,3)',
        '(14,4)'])
    stage.nearEvent([
        0,
        70])
    stage.nearEvent([
        0,
        50,
        3,
        37,
        9,
        58], 1)
    stage.initFight()
    stage.setGlobalData(30, -2, -1, 0, 0, 1)
    stage.createEnemy(
        [{'id': 35, 'x': 13, 'y': 3, 'lv': 6, 'i': 0},
         {'id': 69, 'x': 14, 'y': 3, 'lv': 6, 'i': 1},
         {'id': 50, 'x': 13, 'y': 4, 'lv': 6, 'i': 2},
         {'id': 49, 'x': 14, 'y': 4, 'lv': 6, 'i': 3},
         {'id': 298, 'x': 11, 'y': 3, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 4},
         {'id': 299, 'x': 11, 'y': 4, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 5},
         {'id': 300, 'x': 13, 'y': 10, 'lv': 0, 'ai': 0, 'i': 6},
         {'id': 301, 'x': 14, 'y': 10, 'lv': 0, 'ai': 0, 'i': 7},
         {'id': 302, 'x': 7, 'y': 9, 'lv': 0, 'i': 8},
         {'id': 303, 'x': 7, 'y': 10, 'lv': 0, 'i': 9},
         {'id': 304, 'x': 8, 'y': 10, 'lv': 0, 'i': 10},
         {'id': 70, 'x': 4, 'y': 9, 'lv': 4, 'i': 11},
         {'id': 211, 'x': 4, 'y': 10, 'lv': 0, 'i': 12},
         {'id': 212, 'x': 6, 'y': 13, 'dir': 1, 'lv': 0, 'i': 13},
         {'id': 213, 'x': 7, 'y': 13, 'dir': 1, 'lv': 0, 'i': 14},
         {'id': 234, 'x': 3, 'y': 9, 'lv': 0, 'i': 15},
         {'id': 235, 'x': 5, 'y': 9, 'lv': 0, 'i': 16},
         {'id': 236, 'x': 6, 'y': 12, 'dir': 1, 'lv': 0, 'i': 17},
         {'id': 237, 'x': 6, 'y': 14, 'dir': 1, 'lv': 0, 'i': 18},
         {'id': 210, 'x': 13, 'y': 14, 'lv': 0, 'i': 19},
         {'id': 214, 'x': 14, 'y': 14, 'lv': 0, 'i': 20},
         {'id': 215, 'x': 13, 'y': 15, 'lv': 0, 'i': 21},
         {'id': 216, 'x': 14, 'y': 15, 'lv': 0, 'i': 22},
         {'id': 238, 'x': 12, 'y': 14, 'lv': 0, 'i': 23},
         {'id': 239, 'x': 15, 'y': 14, 'lv': 0, 'i': 24},
         {'id': 37, 'x': 14, 'y': 5, 'lv': 6, 'ai': 0, 'i': 25},
         {'id': 68, 'x': 13, 'y': 5, 'lv': 4, 'ai': 0, 'i': 26},
         {'id': 258, 'x': 14, 'y': 6, 'lv': 0, 'ai': 0, 'i': 27},
         {'id': 259, 'x': 15, 'y': 6, 'lv': 0, 'ai': 0, 'i': 28},
         {'id': 310, 'x': 12, 'y': 6, 'lv': 0, 'ai': 0, 'i': 29},
         {'id': 311, 'x': 13, 'y': 6, 'lv': 0, 'ai': 0, 'i': 30},
         {'id': 58, 'x': 7, 'y': 3, 'dir': 3, 'lv': 6, 'i': 31},
         {'id': 462, 'x': 7, 'y': 4, 'dir': 3, 'lv': 0, 'i': 32},
         {'id': 463, 'x': 7, 'y': 2, 'dir': 3, 'lv': 0, 'i': 33},
         {'id': 464, 'x': 7, 'y': 5, 'dir': 3, 'lv': 0, 'i': 34},
         {'id': 465, 'x': 6, 'y': 3, 'dir': 3, 'lv': 0, 'i': 35},
         {'id': 466, 'x': 6, 'y': 4, 'dir': 3, 'lv': 0, 'i': 36},
         {'id': 414, 'hide': 1, 'x': 12, 'y': 5, 'lv': 0, 'ai': 1, 'i': 37},
         {'id': 415, 'hide': 1, 'x': 12, 'y': 4, 'lv': 0, 'ai': 1, 'i': 38},
         {'id': 416, 'hide': 1, 'x': 15, 'y': 4, 'lv': 0, 'ai': 1, 'i': 39},
         {'id': 417, 'hide': 1, 'x': 15, 'y': 5, 'lv': 0, 'ai': 1, 'i': 40}]
    )
    stage.createMine([
        {
            'idx': 0,
            'x': 13,
            'y': 20,
            'dir': 0 },
        {
            'idx': 1,
            'x': 14,
            'y': 20,
            'dir': 0 },
        {
            'idx': 2,
            'x': 12,
            'y': 20,
            'dir': 0 },
        {
            'idx': 3,
            'x': 15,
            'y': 20,
            'dir': 0 },
        {
            'idx': 4,
            'x': 13,
            'y': 21,
            'dir': 0 },
        {
            'idx': 5,
            'x': 14,
            'y': 21,
            'dir': 0 },
        {
            'idx': 6,
            'x': 12,
            'y': 21,
            'dir': 0 },
        {
            'idx': 7,
            'x': 15,
            'y': 21,
            'dir': 0 },
        {
            'idx': 8,
            'x': 13,
            'y': 22,
            'dir': 0 },
        {
            'idx': 9,
            'x': 14,
            'y': 22,
            'dir': 0 },
        {
            'idx': 10,
            'dir': 0 },
        {
            'idx': 11,
            'dir': 0 },
        {
            'idx': 12,
            'dir': 0 },
        {
            'idx': 13,
            'dir': 0 },
        {
            'idx': 14,
            'dir': 0 }])
    stage.setEnemyEquip(35, 51, 0, 26, 0, 9)
    stage.loadBg(53)
    stage.setObjects(True, 17, [
        [
            77,
            17,
            3],
        [
            78,
            17,
            4]])
    stage.setObjects(True, 17, [
        [
            75,
            21,
            3],
        [
            76,
            21,
            4]])
    stage.setObjects(True, 17, [
        [
            79,
            13,
            11],
        [
            80,
            14,
            11]])
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
    stage.bgSound(1)
    stage.unit(0).setDir(stage.countDir(0, 35))
    stage.say('&0\n공명은 백제성에 틀어박혀\n대체 무엇을 하려는 것인가?')
    stage.unit(35).setDir(0)
    stage.say('&35\n…………')
    stage.unit(69).setDir(stage.countDir(69, 50))
    stage.say('&69\n마속, 조조를 다가오게 두지 마라!\n조금만 더 시간을 벌어 다오.')
    stage.unit(50).setDir(stage.countDir(50, 69))
    stage.say('&50\n안심하십시오. 문제없습니다.\n이 마속이 있는 한 누구도 접근하지 못합니다.\n하하하!')
    stage.unit(49).setDir(stage.countDir(49, 0))
    stage.say('&49\n유비 주군과 장비 장군께서 차례로 돌아가시고\n관우마저 촉을 배신하여 위로 넘어갔으니\n이제는 공명 선생의 기이한 술법에 기댈 수밖에 없다.')
    stage.unit(49).setAction(2)
    stage.say('&49\n위군을 상대할 새 술법을 얻으려면\n반드시 이번 기도 의식을 성공시켜야 한다.\n촉을 위해서라면 목숨도 아깝지 않다!')
    stage.unit(37).setDir(stage.countDir(37, 49))
    stage.say('&37\n그런 요사스러운 술법은 세상만 어지럽힐 뿐이다.\n싸움은 계략과 무력으로 겨뤄야 한다!')
    stage.unit(68).setDir(stage.countDir(68, 37))
    stage.say('&68\n그만하시오, 위연 장군. 조금 참으시오!\n오나라까지 삼킨 위군에 맞서려면\n이 술법이 반드시 도움이 될 것이오.\n&58\n(방덕에 이어 관우 장군마저 위에 항복했군……\n저 충의로운 두 분이 어째서 위로 갔을까?)')
    stage.unit(70).setDir(stage.countDir(70, 0))
    stage.unit(70).setAction(2)
    stage.say('&70\n흥, 조조군은 오고 싶으면 와라!\n나 장익, 죽음을 각오하고 이곳을 지킨다.')
    stage.bgSound(2)
    stage.setWinCondition('승리 조건\n1. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 30턴을 초과한다.')
    stage.showWinCondition('모든 적을 섬멸하라!')
    stage.startOper()
    vars[553] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if vars[2] and not vars[57] and not vars[70] and stage.round() == 4:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[70] = 1
    if vars[2] and not vars[57] and not vars[71] and stage.round() == 7:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[71] = 1
    if vars[2] and not vars[57] and not vars[72] and stage.round() == 10:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[72] = 1
    if vars[2] and not vars[57] and not vars[73] and stage.round() == 13:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[73] = 1
    if vars[2] and not vars[57] and not vars[74] and stage.round() == 16:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[74] = 1
    if vars[2] and not vars[57] and not vars[75] and stage.round() == 19:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[75] = 1
    if vars[2] and not vars[57] and not vars[76] and stage.round() == 22:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[76] = 1
    if vars[2] and not vars[57] and not vars[77] and stage.round() == 25:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[77] = 1
    if vars[2] and not vars[57] and not vars[78] and stage.round() == 28:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[78] = 1
    if vars[2] and not vars[57] and not vars[79] and stage.round() == 31:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[79] = 1
    if vars[2] and not vars[57] and not vars[80] and stage.round() == 34:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[80] = 1
    if vars[2] and not vars[57] and not vars[81] and stage.round() == 37:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[81] = 1
    if vars[2] and not vars[57] and not vars[82] and stage.round() == 40:
        stage.unit(414).show(12, 5, 2)
        stage.unit(415).show(12, 4, 2)
        stage.unit(416).show(15, 4, 2)
        stage.unit(417).show(15, 5, 2)
        vars[82] = 1
    if not vars[20] and stage.isInPos(1025, 13, 4):
        stage.getItem(152, -1, True, 1025)
        vars[20] = 1
    if not vars[21] and stage.isInPos(1025, 14, 3):
        stage.getItem(154, -1, True, 1025)
        vars[21] = 1
    if not vars[22] and stage.isInPos(1025, 13, 3):
        stage.getItem(151, -1, True, 1025)
        vars[22] = 1
    if not vars[23] and stage.isInPos(1025, 14, 4):
        stage.getItem(151, -1, True, 1025)
        vars[23] = 1
    if not vars[30] and stage.isInRect(1025, 12, 13, 15, 17):
        stage.setAI(12, 13, 15, 16, 2, 1)
        vars[30] = 1
    if not vars[31] and stage.curCamp() == 2 and stage.isInRect(1025, 3, 2, 8, 7):
        if not vars[53]:
            stage.unit(58).setAction(2)
            stage.say('&58\n이곳을 통과하게 두지 않겠다!')
        stage.setAI(5, 2, 8, 5, 2, 1)
        vars[31] = 1
    if not vars[32] and stage.isInRect(1025, 2, 7, 10, 15):
        stage.unit(302).setAI(0)
        stage.unit(303).setAI(0)
        stage.unit(304).setAI(0)
        vars[32] = 1
    if not vars[33] and stage.curCamp() == 2 and stage.isInRect(1025, 6, 0, 21, 10):
        if not vars[3] and not vars[50] and not vars[51] and not vars[52]:
            if stage.totalRectUnit(0, 10, 0, 23, 7) == 0:
                stage.unit(35).setDir(stage.countDir(35, 69))
                stage.say('&35\n후………\n모든 것이 계획대로다.\n강유, 가자.')
                stage.unit(69).setDir(stage.countDir(69, 35))
                stage.say('&69\n예, 즉시 출발하겠습니다.')
                stage.unit(35).setDir(stage.countDir(35, 50))
                stage.say('&35\n마속, 뒷일은 네게 맡긴다!\n검각과 양평관을 이용해 적을 막아라!')
                stage.unit(50).setDir(stage.countDir(50, 35))
                stage.say('&50\n제게 맡겨 주십시오.\n이 마속, 결코 실패하지 않겠습니다.')
                stage.setObjects(False, 16, [
                    [
                        77,
                        17,
                        3],
                    [
                        78,
                        17,
                        4]])
                stage.setObjects(False, 16, [
                    [
                        75,
                        21,
                        3],
                    [
                        76,
                        21,
                        4]])
                stage.unit(35).move(23, 3, 1)
                stage.unit(69).move(23, 4, 1)
                stage.unit(35).hide()
                stage.unit(69).hide()
                stage.unit(0).setDir(1)
                stage.say('&0\n크, 큰일이다.\n공명을 놓쳤다!')
            vars[3] = 1
        if not vars[2] and not vars[57]:
            stage.unit(49).setDir(stage.countDir(49, 0))
            stage.say('&49\n공명 선생께서 전해 주신 기이한 술법을\n지금 써먹을 때가 되었군.')
            stage.unit(49).setAction(5)
            stage.say('&49\n나와라, 목인!\n조조를 무찔러라!')
            stage.unit(49).setAction(0)
            stage.unit(414).show()
            stage.unit(415).show()
            stage.unit(416).show()
            stage.unit(417).show()
            vars[2] = 1
        if not vars[31]:
            if not vars[53]:
                stage.unit(58).setDir(stage.countDir(58, 0))
                stage.say('&58\n반드시 놈들의 진로를 막아라!\n더 날뛰게 두어서는 안 된다!')
            stage.setAI(5, 2, 8, 5, 2, 4, -1, 13, 4)
            vars[31] = 1
        vars[33] = 1
    if not vars[34] and stage.curCamp() == 2 and stage.isInRect(1025, 2, 11, 6, 15):
        stage.unit(70).setDir(stage.countDir(70, 0))
        stage.unit(70).setAction(5)
        stage.say('&70\n왔는가?\n돌격하라!')
        stage.unit(70).setAction(0)
        stage.unit(70).setAI(1)
        stage.unit(211).setAI(1)
        stage.unit(234).setAI(1)
        stage.unit(235).setAI(1)
        vars[34] = 1
    if not vars[40] and stage.isNear(0, 50, True):
        stage.unit(0).setDir(stage.countDir(0, 50))
        stage.unit(50).setDir(stage.countDir(50, 0))
        stage.say('&0\n가로막을 생각은 마라!\n지금 공명에게는 요마가 들었다.\n공명을 없애지 않으면 천하에 평온한 날이 없을 것이다.\n&50\n쯧, 쯧, 쯧.\n거짓말도 좀 그럴듯하게 해야지!\n&50\n공명 선생은 나랏일로 바쁘시니\n내가 상대해 주마!\n흥.')
        vars[40] = 1
    if not vars[41] and stage.isNear(0, 70, False):
        stage.unit(70).setDir(stage.countDir(70, 0))
        stage.unit(0).setDir(stage.countDir(0, 70))
        stage.say('&70\n조조!\n&0\n음!')
        fight = stage.startFight(0, 70, 5)
        fight.showUnit(False, '목숨을 내놓아라!', 9)
        fight.showUnit(True, '어림없다!', 6)
        fight.setAction(False, 2)
        fight.attack1(False, 1, False)
        fight.attack2(True, 1, False)
        stage.delay(15)
        fight.say(False, '으아!', True)
        fight.setAction(False, 11)
        fight.death(True)
        fight.end()
        vars[56] = 1
        stage.unit(70).hide(2)
        if not vars[33] and not vars[54] and not vars[55]:
            sel = stage.choice('참으로 아슬아슬했군.\n이름 없는 졸개는 어서 비켜라!', -1)
            if sel == 1:
                stage.say('&0\n반드시 공명을 막아야 한다.\n이런 곳에서 죽을 수는 없다.')
                stage.unit(68).setDir(stage.countDir(68, 0))
                stage.say('&68\n장익………\n네 원수는 내가 갚겠다……')
            elif sel == 2:
                stage.say('&0\n공명 외에는 그대들을 해칠 생각이 없다!\n감히 우리 대군을 가로막는다면 용서 없이 베겠다!')
                stage.unit(37).setDir(stage.countDir(37, 0))
                stage.say('&37\n이 위연을 죽일 수 있을 줄 알았느냐?\n건방진 놈!')
                stage.unit(68).setDir(stage.countDir(68, 0))
                stage.unit(68).setAction(5)
                stage.say('&68\n장익, 내가 원수를 갚아 주마!\n성문을 열어라!')
                stage.unit(68).setAction(0)
                stage.unit(37).setDir(stage.countDir(37, 68))
                stage.unit(37).setAction(10)
                stage.say('&37\n이봐!?\n어서 돌아와라!')
                stage.setObjects(False, 16, [
                    [
                        79,
                        13,
                        11],
                    [
                        80,
                        14,
                        11]])
                stage.unit(37).setDir(stage.countDir(37, 0))
                stage.say('&37\n어쩔 수 없군.\n내가 조조를 죽이러 가겠다!')
                stage.unit(298).setAI(1)
                stage.unit(299).setAI(1)
                stage.unit(300).setAI(1)
                stage.unit(301).setAI(1)
                stage.unit(302).setAI(1)
                stage.unit(303).setAI(1)
                stage.unit(304).setAI(1)
                stage.unit(37).setAI(1)
                stage.unit(258).setAI(1)
                stage.unit(259).setAI(1)
                stage.unit(68).setAI(1)
                stage.unit(310).setAI(1)
                stage.unit(311).setAI(1)
                stage.unit(234).setAI(1)
                stage.unit(235).setAI(1)
                stage.unit(236).setAI(1)
                stage.unit(237).setAI(1)
                stage.unit(211).setAI(1)
                stage.unit(212).setAI(1)
                stage.unit(213).setAI(1)
                if not vars[53]:
                    stage.unit(58).setDir(stage.countDir(58, 68))
                    stage.say('&58\n어찌 성문을 열고 싸우러 나간 것인가!\n이토록 어리석을 수가……')
        vars[41] = 1
    if not vars[42] and stage.isNear(3, 37, True):
        stage.unit(37).setDir(stage.countDir(37, 3))
        stage.unit(3).setDir(stage.countDir(3, 37))
        stage.say('&37\n관우, 촉을 배신하고 위에 항복했구나.\n그 이름 높은 관우가 시세에 따라 주군을 바꾸는 자였다니!\n&3\n위나라에 몸을 의탁한 것은 부득이한 일이오.\n지금 공명에게는 마왕이 들었소.\n어떻게든 공명을 제거해야 하오.\n&37\n진실을 감추려고 꾸며 낸 거짓말이겠지.\n허나 이제 변명할 필요 없다.\n한칼에 두 동강 내 주마!\n&3\n말로는 통하지 않겠군……')
        vars[42] = 1
    if not vars[43] and stage.isNear(9, 58, True):
        stage.unit(58).setDir(stage.countDir(58, 9))
        stage.unit(9).setDir(stage.countDir(9, 58))
        stage.say('&58\n방덕, 어찌 위나라로 갔는가?\n조조가 주군을 죽인 원한을 잊었는가?\n&9\n마대 장군,\n조 승상께서는 사욕으로 움직이는 분이 아니오.\n오직 천하를 위하시는 분이오.\n&9\n지금도 세상을 해치려는 마왕 공명을 무찌르고자\n모든 것을 걸고 몸소 싸우고 계시오.\n뜻있는 자라면 승상을 따라야 하오.\n&58\n다른 그럴듯한 까닭이라도 있을 줄 알았거늘\n아이를 속일 말에 넘어가 위에 항복했다니.\n방덕, 참으로 부끄러운 줄 알아라.\n&58\n이제 우리 사이의 은의는 끊어졌고\n오늘부터는 아무 관계도 없다.\n촉장인 내가 위장인 너를 죽일 뿐이다!')
        vars[43] = 1
    if not vars[50] and stage.unitStateTest(35, 7, 0, 2):
        stage.say('&35\n으윽! 여기까지 쳐들어오다니!?\n하지만 이제 모든 것이 끝났다.\n조조도 한발 늦었구나.')
        if not vars[52]:
            stage.unit(35).setDir(stage.countDir(35, 50))
            stage.say('&35\n마속, 뒷일은 네게 맡긴다!\n검각과 양평관을 이용해 적을 막아라!')
            stage.unit(50).setDir(stage.countDir(50, 35))
            stage.say('&50\n제게 맡겨 주십시오.\n이 마속, 결코 실패하지 않겠습니다.')
        if not vars[51]:
            stage.unit(35).setDir(stage.countDir(35, 69))
            stage.say('&35\n가자, 강유.\n더는 여기에 머물 필요가 없다.')
            stage.unit(69).setDir(stage.countDir(69, 35))
            stage.say('&69\n예, 공명 선생!')
            stage.unit(69).hide()
        stage.unit(35).hide(1)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(69, 7, 0, 2):
        stage.say('&69\n아직 해야 할 일이 있다.\n우선 물러나겠다.')
        stage.unit(69).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(50, 7, 0, 2):
        stage.say('&50\n공명 대인의 지시만 아니었다면\n조금 더 싸웠을 텐데.\n어쩔 수 없다. 퇴각하자.')
        stage.unit(50).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(58, 7, 0, 2):
        stage.say('&58\n쳇, 큰 오산이었군!\n미안하지만 먼저 물러나겠다.')
        stage.unit(58).hide(1)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(37, 7, 0, 2):
        stage.say('&37\n곤란하게 되었군.\n우선 물러나라!')
        stage.unit(37).hide(1)
        vars[54] = 1
    if not vars[55] and stage.unitStateTest(68, 7, 0, 2):
        if vars[56]:
            stage.say('&68\n미, 미안하다…… 장익…….\n네 원수도 갚지 못하고 죽게 되었구나…….\n요, 용서해 다오…… 으윽!')
        else:
            stage.say('&68\n으아악!\n조, 조조…….\n저승에서 기다리겠다………')
        stage.unit(68).hide(2)
        vars[55] = 1
    if not vars[56] and stage.unitStateTest(70, 7, 0, 2):
        stage.say('&70\n조, 조조!\n으, 으윽……')
        stage.unit(0).setDir(stage.countDir(0, 70))
        stage.unit(70).hide(2)
        if not vars[33] and not vars[54] and not vars[55]:
            sel = stage.choice('…………\n저런 졸개들은 상대하지 마라!', -1)
            if sel == 1:
                stage.say('&0\n과연 촉의 장수들이로군.\n하나같이 죽음을 두려워하지 않아!')
                stage.unit(68).setDir(stage.countDir(68, 0))
                stage.say('&68\n장익………\n네 원수는 내가 갚겠다……')
            elif sel == 2:
                stage.say('&0\n공명 외에는 그대들을 해칠 생각이 없다!\n감히 우리 대군을 가로막는다면 용서 없이 베겠다!')
                stage.unit(37).setDir(stage.countDir(37, 0))
                stage.say('&37\n이 위연을 죽일 수 있을 줄 알았느냐?\n건방진 놈!')
                stage.unit(68).setDir(stage.countDir(68, 0))
                stage.unit(68).setAction(5)
                stage.say('&68\n장익, 내가 원수를 갚아 주마!\n성문을 열어라!')
                stage.unit(68).setAction(0)
                stage.unit(37).setDir(stage.countDir(37, 68))
                stage.unit(37).setAction(10)
                stage.say('&37\n이봐!?\n어서 돌아와라!')
                stage.setObjects(False, 16, [
                    [
                        79,
                        13,
                        11],
                    [
                        80,
                        14,
                        11]])
                stage.unit(37).setDir(stage.countDir(37, 0))
                stage.say('&37\n어쩔 수 없군.\n내가 조조를 죽이러 가겠다!')
                stage.unit(298).setAI(1)
                stage.unit(299).setAI(1)
                stage.unit(300).setAI(1)
                stage.unit(301).setAI(1)
                stage.unit(302).setAI(1)
                stage.unit(303).setAI(1)
                stage.unit(304).setAI(1)
                stage.unit(37).setAI(1)
                stage.unit(258).setAI(1)
                stage.unit(259).setAI(1)
                stage.unit(68).setAI(1)
                stage.unit(310).setAI(1)
                stage.unit(311).setAI(1)
                stage.unit(234).setAI(1)
                stage.unit(235).setAI(1)
                stage.unit(236).setAI(1)
                stage.unit(237).setAI(1)
                stage.unit(211).setAI(1)
                stage.unit(212).setAI(1)
                stage.unit(213).setAI(1)
                if not vars[53]:
                    stage.unit(58).setDir(stage.countDir(58, 68))
                    stage.say('&58\n어찌 성문을 열고 싸우러 나간 것인가!\n이토록 어리석을 수가……')
        vars[56] = 1
    if not vars[57] and stage.unitStateTest(49, 7, 0, 2):
        stage.say('&49\n으아악!')
        stage.unit(49).hide(2)
        stage.unit(414).hide()
        stage.unit(415).hide()
        stage.unit(416).hide()
        stage.unit(417).hide()
        vars[57] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('&0\n공명이 이곳에서 기도를 올렸단 말인가?\n백제성 안에도 제단이 있었을 줄이야…….\n두 제단이 더 남았으니, 어떻게든 막아야 한다!')
        if vars[50]:
            stage.reward(0, [
                143,
                -1,
                49,
                -1])
        else:
            stage.reward(0, [
                143,
                -1])
        stage.end()
        vars[0] = 1
        vars[642] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 촉군에게 패했다.')
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
