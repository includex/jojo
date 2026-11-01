# Source Generated with Decompyle++
# File: S_23.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20,
        21], [
        '(18,1)',
        '(17,2)'])
    stage.nearEvent([
        12,
        113,
        10,
        103])
    stage.nearEvent([
        0,
        113], 1)
    stage.initFight()
    stage.setGlobalData(25, -2, 113, 0, 3, 1)
    stage.createEnemy(
        [{'id': 113, 'x': 18, 'y': 1, 'dir': 3, 'lv': 4, 'i': 0},
         {'id': 210, 'x': 16, 'y': 1, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 1},
         {'id': 211, 'x': 16, 'y': 2, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 2},
         {'id': 212, 'x': 17, 'y': 3, 'lv': 0, 'ai': 0, 'i': 3},
         {'id': 213, 'x': 18, 'y': 3, 'lv': 0, 'ai': 0, 'i': 4},
         {'id': 298, 'x': 17, 'y': 2, 'dir': 3, 'lv': 0, 'ai': 0, 'i': 5},
         {'id': 299, 'x': 10, 'y': 1, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 6},
         {'id': 300, 'x': 10, 'y': 9, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 7},
         {'id': 301, 'x': 18, 'y': 9, 'lv': 0, 'ai': 1, 'i': 8},
         {'id': 112, 'x': 11, 'y': 4, 'dir': 3, 'lv': 6, 'ai': 1, 'i': 9},
         {'id': 214, 'x': 10, 'y': 4, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 10},
         {'id': 215, 'x': 10, 'y': 5, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 11},
         {'id': 234, 'x': 10, 'y': 3, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 12},
         {'id': 235, 'x': 10, 'y': 6, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 13},
         {'id': 302, 'x': 11, 'y': 5, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 14},
         {'id': 103, 'x': 15, 'y': 8, 'lv': 4, 'ai': 1, 'i': 15},
         {'id': 216, 'x': 14, 'y': 9, 'lv': 0, 'ai': 1, 'i': 16},
         {'id': 217, 'x': 15, 'y': 9, 'lv': 0, 'ai': 1, 'i': 17},
         {'id': 236, 'x': 13, 'y': 9, 'lv': 0, 'ai': 1, 'i': 18},
         {'id': 237, 'x': 16, 'y': 9, 'lv': 0, 'ai': 1, 'i': 19},
         {'id': 303, 'x': 14, 'y': 8, 'lv': 0, 'ai': 1, 'i': 20},
         {'id': 218, 'x': 7, 'y': 4, 'dir': 3, 'lv': 0, 'i': 21},
         {'id': 219, 'x': 7, 'y': 5, 'dir': 3, 'lv': 0, 'i': 22},
         {'id': 238, 'x': 8, 'y': 4, 'dir': 3, 'lv': 0, 'i': 23},
         {'id': 239, 'x': 8, 'y': 5, 'dir': 3, 'lv': 0, 'i': 24},
         {'id': 220, 'x': 14, 'y': 12, 'lv': 0, 'i': 25},
         {'id': 221, 'x': 15, 'y': 12, 'lv': 0, 'i': 26},
         {'id': 240, 'x': 14, 'y': 11, 'lv': 0, 'i': 27},
         {'id': 241, 'x': 15, 'y': 11, 'lv': 0, 'i': 28},
         {'id': 318, 'hide': 1, 'x': 1, 'y': 2, 'dir': 1, 'lv': 0, 'ai': 1, 'i': 29},
         {'id': 319, 'hide': 1, 'x': 1, 'y': 3, 'dir': 1, 'lv': 0, 'ai': 1, 'i': 30},
         {'id': 320, 'hide': 1, 'x': 1, 'y': 4, 'dir': 1, 'lv': 0, 'ai': 1, 'i': 31},
         {'id': 321, 'hide': 1, 'x': 0, 'y': 5, 'dir': 1, 'lv': 0, 'ai': 1, 'i': 32},
         {'id': 322, 'hide': 1, 'x': 14, 'y': 19, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 33},
         {'id': 323, 'hide': 1, 'x': 15, 'y': 19, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 34},
         {'id': 324, 'hide': 1, 'x': 16, 'y': 18, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 35},
         {'id': 325, 'hide': 1, 'x': 17, 'y': 17, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 36}]
    )
    stage.createMine([
        {
            'idx': 0,
            'x': 2,
            'y': 16,
            'dir': 1 },
        {
            'idx': 1,
            'x': 3,
            'y': 17,
            'dir': 1 },
        {
            'idx': 2,
            'x': 6,
            'y': 16,
            'dir': 1 },
        {
            'idx': 3,
            'x': 3,
            'y': 13,
            'dir': 1 },
        {
            'idx': 4,
            'x': 5,
            'y': 15,
            'dir': 1 },
        {
            'idx': 5,
            'x': 4,
            'y': 14,
            'dir': 1 },
        {
            'idx': 6,
            'x': 5,
            'y': 17,
            'dir': 1 },
        {
            'idx': 7,
            'x': 2,
            'y': 14,
            'dir': 1 },
        {
            'idx': 8,
            'x': 4,
            'y': 16,
            'dir': 1 },
        {
            'idx': 9,
            'x': 3,
            'y': 15,
            'dir': 1 },
        {
            'idx': 10,
            'x': 6,
            'y': 18,
            'dir': 1 },
        {
            'idx': 11,
            'x': 1,
            'y': 15,
            'dir': 0 },
        {
            'idx': 12,
            'x': 4,
            'y': 18,
            'dir': 0 },
        {
            'idx': 13,
            'dir': 0 },
        {
            'idx': 14,
            'dir': 0 }])
    stage.setEnemyEquip(113, -1, 0, 12, 0, -1)
    stage.setEnemyEquip(103, -1, 0, -1, 0, 8)
    stage.setEnemyEquip(112, -1, 0, -1, 0, 20)
    stage.loadBg(23)
    stage.setObjects(True, 17, [
        [
            18,
            9,
            4],
        [
            19,
            9,
            5]])
    stage.setObjects(True, 17, [
        [
            20,
            14,
            10],
        [
            21,
            15,
            10]])
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab31')
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
            goto('lab31')
    stage.draw()
    stage.bgSound(5)
    stage.unit(113).setAction(4)
    stage.say('@dialogue:S_23.scene0.dialogue0001')
    stage.unit(112).setDir(stage.countDir(112, 113))
    stage.unit(103).setDir(stage.countDir(103, 113))
    stage.unit(112).setAction(5)
    stage.say('@dialogue:S_23.scene0.dialogue0002')
    stage.unit(112).setAction(0)
    stage.unit(103).setAction(5)
    stage.say('@dialogue:S_23.scene0.dialogue0003')
    stage.unit(103).setAction(0)
    stage.unit(0).setDir(stage.countDir(0, 22))
    stage.say('@dialogue:S_23.scene0.dialogue0004')
    stage.unit(22).setDir(stage.countDir(22, 0))
    stage.say('@dialogue:S_23.scene0.dialogue0005')
    stage.unit(22).setAction(9)
    stage.say('@dialogue:S_23.scene0.dialogue0006')
    stage.unit(0).setDir(stage.countDir(0, 22))
    stage.say('@dialogue:S_23.scene0.dialogue0007')
    stage.unit(0).setDir(stage.countDir(0, 8))
    stage.say('@dialogue:S_23.scene0.dialogue0008')
    stage.unit(8).setDir(stage.countDir(8, 0))
    stage.say('@dialogue:S_23.scene0.dialogue0009')
    stage.unit(0).setDir(stage.countDir(0, 22))
    stage.say('@dialogue:S_23.scene0.dialogue0010')
    stage.unit(0, 1).move(4, 11, 0)
    stage.unit(3, 1).move(4, 10, 0)
    stage.unit(5, 1).move(5, 10, 0)
    stage.unit(7, 1).move(3, 10, 0)
    stage.unit(9, 1).move(5, 11, 0)
    stage.unit(11, 1).move(3, 11, 0)
    stage.unit(1, 1).move(8, 15, 1)
    stage.unit(2, 1).move(9, 15, 1)
    stage.unit(4, 1).move(9, 14, 1)
    stage.unit(6, 1).move(9, 16, 1)
    stage.unit(8, 1).move(8, 14, 1)
    stage.unit(10, 1).move(8, 16, 1)
    stage.unit(12, 1).move(8, 17, 1)
    stage.setWinCondition('승리 조건\n1. 원담을 격퇴한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 25턴을 초과한다.')
    stage.showWinCondition('원담을 격퇴하라!')
    stage.unit(113).heightLight()
    stage.startOper()
    vars[523] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[20] and stage.isInPos(1025, 18, 1):
        stage.getItem(152, -1, True, 1025)
        vars[20] = 1
    if not vars[21] and stage.isInPos(1025, 17, 2):
        stage.getItem(153, -1, True, 1025)
        vars[21] = 1
    if not vars[6] and stage.totalRectUnit(2, 7, 4, 8, 5) < 4:
        stage.unit(112).setDir(2)
        stage.unit(112).setAction(1)
        stage.say('@dialogue:S_23.scene1.dialogue0001')
        stage.unit(318).show()
        stage.unit(319).show()
        stage.unit(320).show()
        stage.unit(321).show()
        stage.unit(320).setAction(5)
        stage.say('@dialogue:S_23.scene1.dialogue0002')
        stage.unit(320).setAction(0)
        stage.unit(218).setAI(1)
        stage.unit(219).setAI(1)
        stage.unit(238).setAI(1)
        stage.unit(239).setAI(1)
        vars[6] = 1
    if not vars[7] and stage.totalRectUnit(2, 14, 11, 15, 12) < 4:
        stage.unit(103).setDir(2)
        stage.unit(103).setAction(1)
        stage.say('@dialogue:S_23.scene1.dialogue0003')
        stage.unit(322).show()
        stage.unit(323).show()
        stage.unit(324).show()
        stage.unit(325).show()
        stage.unit(323).setAction(5)
        stage.say('@dialogue:S_23.scene1.dialogue0004')
        stage.unit(323).setAction(0)
        stage.unit(220).setAI(1)
        stage.unit(221).setAI(1)
        stage.unit(240).setAI(1)
        stage.unit(241).setAI(1)
        vars[7] = 1
    if not vars[8] and stage.totalRectUnit(2, 9, 11, 19, 19) < 3 and stage.totalRectUnit(2, 0, 0, 8, 19) < 3:
        if not vars[51] and not vars[52]:
            stage.unit(113).setDir(stage.countDir(113, 103))
            stage.unit(113).setAction(4)
            stage.say('@dialogue:S_23.scene1.dialogue0005')
            stage.unit(103).setDir(stage.countDir(103, 113))
            stage.say('@dialogue:S_23.scene1.dialogue0006')
            stage.unit(103).setAction(4)
            stage.say('@dialogue:S_23.scene1.dialogue0007')
            stage.unit(113).setDir(stage.countDir(113, 112))
            stage.say('@dialogue:S_23.scene1.dialogue0008')
            stage.unit(112).setDir(stage.countDir(112, 113))
            stage.say('@dialogue:S_23.scene1.dialogue0009')
            stage.setObjects(False, 16, [
                [
                    18,
                    9,
                    4],
                [
                    19,
                    9,
                    5]])
            stage.unit(112).move(9, 4, 3)
            stage.unit(112).setDir(stage.countDir(112, 0))
            stage.say('@dialogue:S_23.scene1.dialogue0010')
            stage.unit(0).setDir(stage.countDir(0, 112))
            stage.say('@dialogue:S_23.scene1.dialogue0011')
            sel = stage.choice('항복을 거부한다\n신평만 받아들인다\n항복을 받아들인다', -1)
            if sel == 1:
                stage.say('@dialogue:S_23.scene1.dialogue0012')
                stage.unit(0).setAction(5)
                stage.say('@dialogue:S_23.scene1.dialogue0013')
                stage.unit(0).setAction(0)
                stage.unit(112).setAction(10)
                stage.say('@dialogue:S_23.scene1.dialogue0014')
                stage.unit(113).setDir(stage.countDir(113, 0))
                stage.say('@dialogue:S_23.scene1.dialogue0015')
            elif sel == 2:
                stage.say('@dialogue:S_23.scene1.dialogue0016')
                stage.unit(112).move(15, 2, 1)
                stage.unit(112).setDir(stage.countDir(112, 113))
                stage.say('@dialogue:S_23.scene1.dialogue0017')
                stage.unit(113).setDir(stage.countDir(113, 112))
                stage.say('@dialogue:S_23.scene1.dialogue0018')
                stage.unit(112).setAction(4)
                stage.unit(112).setAction(6)
                stage.unit(112).setAction(0)
                stage.say('@dialogue:S_23.scene1.dialogue0019')
                stage.unit(112).hide(2)
                stage.say('@dialogue:S_23.scene1.dialogue0020')
                stage.unit(103).setDir(stage.countDir(103, 113))
                stage.say('@dialogue:S_23.scene1.dialogue0021')
                stage.unit(113).setAI(1)
                stage.unit(210).setAI(1)
                stage.unit(211).setAI(1)
                stage.unit(212).setAI(1)
                stage.unit(213).setAI(1)
                stage.unit(218).setAI(1)
                stage.unit(219).setAI(1)
                stage.unit(220).setAI(1)
                stage.unit(221).setAI(1)
                stage.unit(238).setAI(1)
                stage.unit(239).setAI(1)
                stage.unit(240).setAI(1)
                stage.unit(241).setAI(1)
                stage.unit(298).setAI(1)
                stage.setObjects(False, 16, [
                    [
                        20,
                        14,
                        10],
                    [
                        21,
                        15,
                        10]])
            elif sel == 3:
                stage.say('@dialogue:S_23.scene1.dialogue0022')
                stage.unit(112).move(15, 2, 1)
                stage.unit(112).setDir(stage.countDir(112, 113))
                stage.say('@dialogue:S_23.scene1.dialogue0023')
                stage.unit(113).setDir(stage.countDir(113, 117))
                stage.unit(113).setAction(8)
                stage.say('@dialogue:S_23.scene1.dialogue0024')
                stage.unit(103).setDir(stage.countDir(103, 113))
                stage.say('@dialogue:S_23.scene1.dialogue0025')
                stage.unit(113).setDir(stage.countDir(113, 0))
                stage.unit(113).setAction(5)
                stage.say('@dialogue:S_23.scene1.dialogue0026')
                stage.unit(113).setAction(0)
                stage.unit(210).setAI(1)
                stage.unit(211).setAI(1)
                stage.unit(212).setAI(1)
                stage.unit(213).setAI(1)
                stage.unit(218).setAI(1)
                stage.unit(219).setAI(1)
                stage.unit(220).setAI(1)
                stage.unit(221).setAI(1)
                stage.unit(238).setAI(1)
                stage.unit(239).setAI(1)
                stage.unit(240).setAI(1)
                stage.unit(241).setAI(1)
                stage.unit(298).setAI(1)
                stage.setObjects(False, 16, [
                    [
                        20,
                        14,
                        10],
                    [
                        21,
                        15,
                        10]])
                stage.say('@dialogue:S_23.scene1.dialogue0027')
                stage.unit(215).hide()
                stage.say('@dialogue:S_23.scene1.dialogue0028')
                stage.unit(234).hide()
                stage.say('@dialogue:S_23.scene1.dialogue0029')
                stage.unit(303).hide()
                stage.say('@dialogue:S_23.scene1.dialogue0030')
                stage.unit(237).hide()
                stage.say('@dialogue:S_23.scene1.dialogue0031')
            else:
                stage.unit(113).setDir(stage.countDir(113, 0))
                stage.unit(113).setAction(4)
                stage.say('@dialogue:S_23.scene1.dialogue0032')
                stage.unit(113).setAI(1)
                stage.unit(210).setAI(1)
                stage.unit(211).setAI(1)
                stage.unit(212).setAI(1)
                stage.unit(213).setAI(1)
                stage.unit(218).setAI(1)
                stage.unit(219).setAI(1)
                stage.unit(220).setAI(1)
                stage.unit(221).setAI(1)
                stage.unit(238).setAI(1)
                stage.unit(239).setAI(1)
                stage.unit(240).setAI(1)
                stage.unit(241).setAI(1)
                stage.unit(298).setAI(1)
                stage.setObjects(False, 16, [
                    [
                        18,
                        9,
                        4],
                    [
                        19,
                        9,
                        5]])
                stage.setObjects(False, 16, [
                    [
                        20,
                        14,
                        10],
                    [
                        21,
                        15,
                        10]])
        vars[8] = 1
    if not vars[40] and stage.isNear(0, 113, True):
        stage.unit(0).setDir(stage.countDir(0, 113))
        stage.unit(113).setDir(stage.countDir(113, 0))
        stage.say('@dialogue:S_23.scene1.dialogue0033')
        vars[40] = 1
    if not vars[41] and stage.isNear(12, 113, False):
        stage.unit(12).setDir(stage.countDir(12, 113))
        stage.unit(113).setDir(stage.countDir(113, 12))
        stage.say('@dialogue:S_23.scene1.dialogue0034')
        fight = stage.startFight(12, 113, 5)
        fight.showUnit(False, '건방진 놈, 비켜라!\n이 원담의 무서움을 모르느냐!', 9)
        fight.showUnit(True, '나는 조씨의 대장이다.\n너를 상대하기에는 충분하지.\n오너라, 승부를 겨루자!', 6)
        fight.showStart()
        fight.say(False, '으윽, 좋지 않군.', True)
        fight.setAction(False, 1)
        fight.setAction(True, 8)
        fight.say(True, '달아날 셈이냐!?', True)
        fight.setAction(True, 2)
        fight.attack2(True, 1, False)
        fight.setAction(True, 1)
        fight.attack2(True, 1, False)
        fight.say(False, '으아아!', True)
        fight.setAction(False, 11)
        fight.death(True)
        fight.setAction(True, 1)
        fight.setAction(True, 9)
        stage.delay(5)
        fight.setAction(True, 8)
        fight.say(True, '원담의 목을 베었다!', True)
        fight.end()
        stage.unit(113).hide(2)
        vars[50] = 1
        stage.getItem(80, -1, True, 12)
        stage.resumeCtrl()
        if not vars[51]:
            stage.unit(112).setDir(stage.countDir(112, 113))
            stage.say('@dialogue:S_23.scene1.dialogue0035')
            vars[51] = 1
        if not vars[52]:
            stage.unit(103).setDir(stage.countDir(103, 113))
            stage.unit(103).setAction(4)
            stage.say('@dialogue:S_23.scene1.dialogue0036')
            vars[52] = 1
        stage.setRectUnitHide(0, 0, 19, 19, 2, 2)
        vars[41] = 1
    if not vars[42] and stage.isNear(10, 103, False):
        stage.unit(10).setDir(stage.countDir(10, 103))
        stage.unit(103).setDir(stage.countDir(103, 10))
        stage.say('@dialogue:S_23.scene1.dialogue0037')
        fight = stage.startFight(10, 103, 0)
        fight.showUnit(True, '나는 악진 악문겸이다!\n곽도, 말에서 내려 목숨을 바쳐라!', 8)
        fight.showUnit(False, '비켜라, 필부야!\n나는 지략으로 싸운다.\n너와 일기토를 벌일 생각은 없다!', 9)
        fight.showStart()
        fight.setAction(False, 6)
        fight.say(False, '흥!\n너를 상대할 틈이 없다!', True)
        fight.setAction(False, 4)
        stage.delay(5)
        fight.setAction(False, 1)
        fight.setAction(False, 3)
        fight.setAction(False, 3)
        fight.say(True, '도망치지 마라!', True)
        fight.attack2(True, 1, False)
        stage.delay(10)
        fight.setAction(False, 1)
        stage.delay(10)
        fight.setAction(False, 4)
        stage.delay(10)
        fight.say(False, '나, 나 곽도가…….\n이런 곳에서 죽는 것인가?\n으윽…….', True)
        fight.setAction(False, 11)
        fight.death(True)
        fight.setAction(True, 0)
        stage.delay(5)
        fight.setAction(True, 8)
        fight.say(True, '곽도를 베었다!\n단숨에 적군을 무너뜨려라!', True)
        fight.end()
        stage.unit(103).hide(2)
        vars[52] = 1
        stage.getItem(127, -1, True, 10)
        vars[42] = 1
    if not vars[50] and stage.unitStateTest(113, 7, 0, 2):
        stage.say('@dialogue:S_23.scene1.dialogue0038')
        stage.unit(113).hide(2)
        stage.getItem(80, -1, True, 1027)
        stage.resumeCtrl()
        if not vars[51]:
            stage.unit(112).setDir(stage.countDir(112, 113))
            stage.say('@dialogue:S_23.scene1.dialogue0039')
            vars[51] = 1
        if not vars[52]:
            stage.unit(103).setDir(stage.countDir(103, 113))
            stage.unit(103).setAction(4)
            stage.say('@dialogue:S_23.scene1.dialogue0040')
            vars[52] = 1
        stage.setRectUnitHide(0, 0, 19, 19, 2, 2)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(112, 7, 0, 2):
        stage.say('@dialogue:S_23.scene1.dialogue0041')
        stage.unit(112).hide(2)
        stage.getItem(127, -1, True, 1027)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(103, 7, 0, 2):
        stage.say('@dialogue:S_23.scene1.dialogue0042')
        stage.unit(103).hide(2)
        stage.getItem(115, -1, True, 1027)
        vars[52] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_23.scene1.dialogue0043')
        stage.reward(0, [
            160,
            -1])
        stage.end()
        vars[0] = 1
        vars[623] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 원담에게 패했다.')
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
