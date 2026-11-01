# Source Generated with Decompyle++
# File: S_57.marshal (Python 3.9)


def scene0():
    stage.nearEvent([
        0,
        35,
        0,
        163,
        3,
        164,
        1,
        166,
        4,
        167,
        2,
        168,
        22,
        165,
        17,
        162,
        21,
        169], 1)
    stage.initFight()
    stage.setGlobalData(40, -2, -1, 0, 1, 2)
    stage.createEnemy(
        [{'id': 35, 'hide': 1, 'x': 20, 'y': 3, 'lv': 6, 'i': 0},
         {'id': 163, 'hide': 1, 'x': 3, 'y': 19, 'dir': 1, 'lv': 6, 'ai': 1, 'i': 1},
         {'id': 164, 'hide': 1, 'x': 3, 'y': 20, 'dir': 1, 'lv': 6, 'ai': 1, 'i': 2},
         {'id': 168, 'hide': 1, 'x': 20, 'y': 36, 'dir': 0, 'lv': 6, 'i': 3},
         {'id': 167, 'hide': 1, 'x': 22, 'y': 32, 'dir': 0, 'lv': 6, 'i': 4},
         {'id': 166, 'hide': 1, 'x': 17, 'y': 34, 'dir': 0, 'lv': 6, 'i': 5},
         {'id': 165, 'x': 36, 'y': 19, 'dir': 3, 'lv': 6, 'i': 6},
         {'id': 162, 'x': 34, 'y': 17, 'dir': 3, 'lv': 6, 'i': 7},
         {'id': 169, 'x': 35, 'y': 22, 'dir': 3, 'lv': 6, 'i': 8},
         {'id': 414, 'x': 33, 'y': 19, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 9},
         {'id': 415, 'x': 35, 'y': 19, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 10},
         {'id': 416, 'x': 34, 'y': 20, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 11},
         {'id': 417, 'x': 36, 'y': 20, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 12},
         {'id': 418, 'x': 31, 'y': 17, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 13},
         {'id': 419, 'x': 32, 'y': 16, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 14},
         {'id': 420, 'x': 32, 'y': 18, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 15},
         {'id': 421, 'x': 34, 'y': 18, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 16},
         {'id': 422, 'x': 32, 'y': 22, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 17},
         {'id': 423, 'x': 33, 'y': 21, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 18},
         {'id': 424, 'x': 33, 'y': 23, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 19},
         {'id': 425, 'x': 35, 'y': 21, 'dir': 3, 'lv': 0, 'ai': 1, 'i': 20},
         {'id': 438, 'hide': 1, 'x': 20, 'y': 35, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 21},
         {'id': 439, 'hide': 1, 'x': 21, 'y': 36, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 22},
         {'id': 440, 'hide': 1, 'x': 19, 'y': 36, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 23},
         {'id': 441, 'hide': 1, 'x': 20, 'y': 37, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 24},
         {'id': 442, 'hide': 1, 'x': 22, 'y': 31, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 25},
         {'id': 443, 'hide': 1, 'x': 23, 'y': 32, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 26},
         {'id': 444, 'hide': 1, 'x': 21, 'y': 32, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 27},
         {'id': 445, 'hide': 1, 'x': 22, 'y': 33, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 28},
         {'id': 446, 'hide': 1, 'x': 17, 'y': 33, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 29},
         {'id': 447, 'hide': 1, 'x': 18, 'y': 34, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 30},
         {'id': 448, 'hide': 1, 'x': 16, 'y': 34, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 31},
         {'id': 449, 'hide': 1, 'x': 17, 'y': 35, 'dir': 0, 'lv': 0, 'ai': 1, 'i': 32}]
    )
    stage.createMine([
        {
            'idx': 0,
            'x': 21,
            'y': 19,
            'dir': 1 },
        {
            'idx': 1,
            'x': 18,
            'y': 18,
            'dir': 1 },
        {
            'idx': 2,
            'x': 18,
            'y': 21,
            'dir': 1 },
        {
            'idx': 3,
            'x': 21,
            'y': 20,
            'dir': 1 },
        {
            'idx': 4,
            'x': 21,
            'y': 18,
            'dir': 1 },
        {
            'idx': 5,
            'x': 21,
            'y': 21,
            'dir': 1 },
        {
            'idx': 6,
            'x': 20,
            'y': 19,
            'dir': 1 },
        {
            'idx': 7,
            'x': 20,
            'y': 20,
            'dir': 1 },
        {
            'idx': 8,
            'x': 20,
            'y': 18,
            'dir': 1 },
        {
            'idx': 9,
            'x': 20,
            'y': 21,
            'dir': 1 },
        {
            'idx': 10,
            'x': 19,
            'y': 19,
            'dir': 1 },
        {
            'idx': 11,
            'x': 19,
            'y': 20,
            'dir': 1 },
        {
            'idx': 12,
            'x': 19,
            'y': 18,
            'dir': 1 },
        {
            'idx': 13,
            'x': 19,
            'y': 21,
            'dir': 1 },
        {
            'idx': 14,
            'x': 18,
            'y': 19,
            'dir': 1 }])
    stage.setEnemyEquip(35, 51, 0, 26, 0, 9)
    stage.setEnemyEquip(163, 32, 0, -1, 0, 14)
    stage.setEnemyEquip(164, 37, 0, -1, 0, -1)
    stage.setEnemyEquip(166, 35, 0, -1, 0, -1)
    stage.setEnemyEquip(165, -1, 0, -1, 0, 5)
    stage.loadBg(57)
    stage.setObjects(True, 17, [
        [
            93,
            19,
            23],
        [
            94,
            20,
            23]])
    stage.setObjects(True, 17, [
        [
            85,
            19,
            27],
        [
            86,
            20,
            27]])
    stage.setObjects(True, 17, [
        [
            91,
            16,
            19],
        [
            92,
            16,
            20]])
    stage.setObjects(True, 17, [
        [
            83,
            12,
            19],
        [
            84,
            12,
            20]])
    stage.setObjects(True, 17, [
        [
            95,
            19,
            16],
        [
            96,
            20,
            16]])
    stage.setObjects(True, 17, [
        [
            87,
            19,
            12],
        [
            88,
            20,
            12]])
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab37')
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
            goto('lab37')
    stage.draw()
    stage.bgSound(9)
    stage.unit(0).setDir(0)
    stage.unit(0).setDir(3)
    stage.unit(0).setDir(2)
    stage.unit(0).setDir(1)
    stage.say('&0\n이 지하 통로는 어딘가 기묘하군.\n어복포에서 느꼈던 것 같은 기운이 감돈다.')
    stage.unit(19).setDir(stage.countDir(19, 0))
    stage.say('&19\n이곳에는 방이 넷 있는 듯한데\n문이 모두 굳게 닫혀 있습니다.\n공명은 대체 어디에 숨어 있을까요?')
    stage.unit(6).setDir(1)
    stage.say('&6\n오른쪽 통로로 이어지는 문은 잠기지 않은 듯합니다.')
    stage.unit(0).setAction(5)
    stage.say('&0\n좋다. 그럼 우선 이 방부터 살펴보겠다.\n너희 몇은 나를 따라 들어오너라.')
    stage.unit(0).setAction(0)
    stage.unit(0, 1).move(27, 19, 1)
    stage.unit(3, 1).move(27, 20, 1)
    stage.unit(4, 1).move(26, 19, 1)
    stage.unit(5, 1).move(26, 20, 1)
    stage.unit(6, 1).move(25, 19, 1)
    stage.unit(7, 1).move(25, 20, 1)
    stage.say('&13\n지형을 보니 늪지 같은데요.\n어쩐지 섬뜩합니다……\n&15\n안에 누군가 있는 듯합니다.\n&16\n저자는 주유가 아닙니까!?\n&0\n주유 일행이 어찌 여기에 있는가?\n이미 죽은 자들이 아닌가?\n&0\n설마 지하 제단에 혼을 되돌리는 힘이 있는 것인가?\n&162\n으오오, 머리가 아프다. 온몸도 아파.\n여기는 어디지?\n내가 왜 여기 있지?\n&169\n저기 서 있는 자는 조조 아닌가?\n그렇다면 여기는 전장이군?\n&165\n조조가 누구지? 조조……생각났다.\n생각났어. 죽여야 할 놈이다!\n오나라를 위해 반드시 조조를 죽여야 한다.\n&165\n목인들아, 공격하라! 나를 대신해 조조를 죽여라!')
    stage.setWinCondition('승리 조건\n1. 제갈량을 죽인다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 40턴을 초과한다.')
    stage.showWinCondition('제갈량을 죽여라!')
    stage.startOper()
    vars[70] = 0
    vars[71] = 0
    vars[72] = 1
    vars[73] = 1
    vars[557] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if vars[56] and vars[57] and vars[58] and not vars[2] and not vars[11] and stage.round() >= 1:
        stage.say('&0\n겨우 모두 섬멸했군.\n공명은 이 방에 없는 모양이다.')
        stage.unit(17).setDir(stage.countDir(17, 0))
        stage.say('&17\n다른 방을 살펴보시지요.')
        stage.unit(23).setDir(stage.countDir(23, 0))
        stage.say('&23\n공명은 반드시 어딘가에 숨어 있을 것입니다.')
        stage.unit(0).setAction(5)
        stage.say('&0\n제단 중앙으로 돌아가자.')
        stage.unit(0).setAction(0)
        stage.setObjects(False, 16, [
            [
                93,
                19,
                23],
            [
                94,
                20,
                23]])
        stage.setObjects(False, 16, [
            [
                85,
                19,
                27],
            [
                86,
                20,
                27]])
        stage.unit(1).setAction(10)
        stage.say('&1\n음! 어찌 된 일인가?')
        stage.unit(20).setDir(stage.countDir(20, 1))
        stage.say('&20\n아래쪽 방의 문이 열린 듯합니다.')
        stage.unit(0).setDir(stage.countDir(0, 20))
        stage.say('&0\n문이 열렸나?\n방 안의 적을 죽여야\n다른 문이 저절로 열리는 모양이군.')
        stage.unit(166).show()
        stage.unit(167).show()
        stage.unit(168).show()
        stage.say('&166\n나는 누구지……\n어찌 모든 것이 이토록 아득한가……\n아무것도 기억나지 않는다!')
        stage.unit(167).setDir(stage.countDir(167, 166))
        stage.say('&167\n아버님! 중모!\n여기는 대체……')
        stage.unit(168).setDir(stage.countDir(168, 166))
        stage.say('&168\n나도 혼란스럽다.\n하지만……조조가 여기 있는 듯하다.\n반드시 조조를 죽여야 해……')
        stage.unit(166).setDir(stage.countDir(166, 0))
        stage.say('&166\n생각났다…….\n조조를 죽이는 것이 우리 부자의 사명이다.\n조조는 어디 있느냐? 어서 나와라…….')
        stage.unit(167).setDir(0)
        stage.unit(168).setDir(0)
        stage.unit(166).setDir(0)
        stage.unit(438).show()
        stage.unit(439).show()
        stage.unit(440).show()
        stage.unit(441).show()
        stage.unit(442).show()
        stage.unit(443).show()
        stage.unit(444).show()
        stage.unit(445).show()
        stage.unit(446).show()
        stage.unit(447).show()
        stage.unit(448).show()
        stage.unit(449).show()
        stage.unit(8).setDir(stage.countDir(8, 166))
        stage.say('&8\n이번에 되살아난 것은 손권 부자인가?\n조씨와 손씨가 맞붙다니 볼만하겠군!')
        stage.unit(0).setDir(stage.countDir(0, 166))
        stage.say('&0\n좋다. 손씨 일족을 멸하겠다!\n내가 너희를 저승으로 보내 주마!')
        stage.unit(4).setDir(stage.countDir(4, 167))
        stage.say('&4\n손책이 태사자와 싸운 적 있다지요.\n이 조창도 그와 승부를 겨루고 싶군요.')
        stage.bgSound(10)
        vars[2] = 1
        vars[11] = 1
    if vars[2] and vars[3] and not vars[4] and not vars[15] and stage.curCamp() == 0 and stage.round() >= 1:
        if stage.isInRect(1026, 1, 15, 11, 24):
            if stage.isInRect(1025, 1, 15, 11, 24):
                if stage.totalRectUnit(6, 12, 19, 12, 20) == 0:
                    if not vars[60]:
                        stage.setObjects(True, 17, [
                            [
                                83,
                                12,
                                19],
                            [
                                84,
                                12,
                                20]])
                        vars[60] = 1
                    if not vars[16] and vars[17]:
                        stage.say('&0\n젠장! 통로에도 함정이 있는 모양이다.\n&20\n으윽, 또 공명의 함정에 빠진 것입니까?')
                        vars[16] = 1
                    elif vars[60]:
                        stage.setObjects(False, 16, [
                            [
                                83,
                                12,
                                19],
                            [
                                84,
                                12,
                                20]])
                        vars[60] = 0
                    elif vars[60]:
                        stage.setObjects(False, 16, [
                            [
                                83,
                                12,
                                19],
                            [
                                84,
                                12,
                                20]])
                        vars[60] = 0
        if stage.isInRect(1026, 1, 15, 15, 24):
            if stage.isInRect(1025, 1, 15, 15, 24):
                if stage.totalRectUnit(6, 16, 19, 16, 20) == 0:
                    if not vars[61]:
                        stage.setObjects(True, 17, [
                            [
                                91,
                                16,
                                19],
                            [
                                92,
                                16,
                                20]])
                        vars[61] = 1
                    if not vars[16] and vars[17]:
                        stage.say('&0\n젠장! 통로에도 함정이 있는 모양이다.\n&20\n으윽, 또 공명의 함정에 빠진 것입니까?')
                        vars[16] = 1
                    elif vars[61]:
                        stage.setObjects(False, 16, [
                            [
                                91,
                                16,
                                19],
                            [
                                92,
                                16,
                                20]])
                        vars[61] = 0
                    elif vars[61]:
                        stage.setObjects(False, 16, [
                            [
                                91,
                                16,
                                19],
                            [
                                92,
                                16,
                                20]])
                        vars[61] = 0
        vars[15] = 1
    if vars[2] and vars[3] and not vars[4] and vars[15] and stage.curCamp() == 2 and stage.round() >= 1:
        vars[15] = 0
        vars[17] = 1
    if vars[2] and not vars[3] and stage.round() >= 1:
        if stage.totalUnit(0) < 2:
            if stage.totalRectUnit(0, 2, 16, 11, 23) == 1:
                stage.center(5, 20)
                stage.delay(5)
                stage.say('&0\n여기에는 아무도 없는가?\n&5\n어쩐지 이상하군요!')
                stage.unit(9).setDir(stage.countDir(9, 0))
                stage.say('&9\n절대 방심하지 마라!')
                stage.unit(22).setDir(stage.countDir(22, 0))
                stage.say('&22\n공명이 배치한 것이겠지요?\n틀림없이 계략이 있습니다.')
                stage.unit(163).show()
                stage.unit(164).show()
                stage.unit(32).setAction(4)
                stage.say('&163\n머리가 깨질 듯이 아프다……\n대체 무슨 일이 일어난 것이지?')
                stage.unit(164).setDir(stage.countDir(164, 0))
                stage.say('&164\n여기 조조의 군대가 있다.\n그래, 모두 네놈들 탓이다.\n조조만 죽이면 된다!')
                stage.unit(3).setDir(stage.countDir(3, 32))
                stage.say('&3\n형님! 셋째 아우야!\n공명 이놈, 감히 형님의 망혼을 농락하다니!\n이 관우가 결코 용서하지 않겠다!\n&33\n조조는 어디 있느냐아……!?')
                stage.center(11, 20)
                stage.delay(5)
                stage.center(13, 20)
                stage.delay(1)
                stage.center(11, 20)
                stage.delay(1)
                stage.center(13, 20)
                stage.delay(1)
                stage.center(11, 20)
                stage.delay(1)
                stage.center(13, 20)
                stage.delay(1)
                stage.center(11, 20)
                stage.delay(1)
                stage.center(13, 20)
                stage.delay(5)
                stage.info('조조는 어디 있느냐…… 어디에…….\n어디 있느냐…… 어디 있느냐…….')
                stage.say('&0\n이 엄청난 고함 소리는 어디서 나는 것이냐!\n귀청이 떨어질 지경이군.\n지하 통로에 울려 퍼지니 견딜 수가 없다!\n&11\n무, 무슨 일입니까?\n&25\n어찌 된 일입니까?\n또 공명의 계략입니까?\n&18\n이, 이것이 사람의 목소리란 말입니까?\n&0\n모두 당황하지 마라…… 제길!\n완전히 혼란에 빠졌군!')
                stage.setUnitStatus([
                    {
                        'hStatus': [
                            9],
                        'x1': 0,
                        'y1': 12,
                        'x2': 39,
                        'y2': 39,
                        'camp': 0 }])
                stage.setAI(2, 16, 11, 23, 2, 1)
                vars[3] = 1
                vars[15] = 1
            elif stage.totalRectUnit(0, 2, 16, 11, 23) == 2:
                stage.center(5, 20)
                stage.delay(5)
                stage.say('&0\n여기에는 아무도 없는가?\n&5\n어쩐지 이상하군요!')
                stage.unit(9).setDir(stage.countDir(9, 0))
                stage.say('&9\n절대 방심하지 마라!')
                stage.unit(22).setDir(stage.countDir(22, 0))
                stage.say('&22\n공명이 배치한 것이겠지요?\n틀림없이 계략이 있습니다.')
                stage.unit(163).show()
                stage.unit(164).show()
                stage.unit(32).setAction(4)
                stage.say('&163\n머리가 깨질 듯이 아프다……\n대체 무슨 일이 일어난 것이지?')
                stage.unit(164).setDir(stage.countDir(164, 0))
                stage.say('&164\n여기 조조의 군대가 있다.\n그래, 모두 네놈들 탓이다.\n조조만 죽이면 된다!')
                stage.unit(3).setDir(stage.countDir(3, 163))
                stage.say('&3\n형님! 셋째 아우야!\n공명 이놈, 감히 형님의 망혼을 농락하다니!\n이 관우가 결코 용서하지 않겠다!\n&33\n조조는 어디 있느냐아……!?')
                stage.center(11, 20)
                stage.delay(5)
                stage.center(13, 20)
                stage.delay(1)
                stage.center(11, 20)
                stage.delay(1)
                stage.center(13, 20)
                stage.delay(1)
                stage.center(11, 20)
                stage.delay(1)
                stage.center(13, 20)
                stage.delay(1)
                stage.center(11, 20)
                stage.delay(1)
                stage.center(13, 20)
                stage.delay(5)
                stage.info('조조는 어디 있느냐…… 어디에…….\n어디 있느냐…… 어디 있느냐…….')
                stage.say('&0\n어찌 이리 소리가 큰가.\n귀가 먹먹할 지경이군.\n지하 통로 전체에 메아리가 울리는구나.\n&11\n뭐라고, 뭐라고!\n&25\n뭐라고, 공명의 함정이라고?\n&18\n이것이 사람의 목소리란 말인가?\n&0\n진정하라……제기랄! 아수라장이로군!')
                stage.setUnitStatus([
                    {
                        'hStatus': [
                            9],
                        'x1': 12,
                        'y1': 0,
                        'x2': 39,
                        'y2': 39,
                        'camp': 0 }])
                stage.setAI(2, 16, 11, 23, 2, 1)
                vars[3] = 1
                vars[15] = 1
    if vars[53] and vars[54] and vars[55] and not vars[12] and stage.round() >= 1:
        stage.setObjects(False, 16, [
            [
                91,
                16,
                19],
            [
                92,
                16,
                20]])
        stage.setObjects(False, 16, [
            [
                83,
                12,
                19],
            [
                84,
                12,
                20]])
        stage.say('&0\n다음 문이 열린 모양이군.\n&5\n다음 방에서는 누가 기다리고 있을까요?\n&12\n이제 두 방 남았습니다.\n역적 공명, 기다려라.')
        stage.unit(10).setDir(stage.countDir(10, 0))
        stage.say('&10\n주군, 계속 전진하시지요!\n반드시 공명을 찾아야 합니다.')
        stage.unit(14).setDir(stage.countDir(14, 0))
        stage.say('&14\n안에 무엇이 있는지 모릅니다.\n신중히 움직이는 것이 좋겠습니다.')
        vars[12] = 1
    if vars[4] and vars[21] and vars[22] and stage.round() >= 1:
        stage.unit(35).setDir(2)
        stage.unit(35).setAction(1)
        stage.say('&35\n하하하!\n끝났다…….\n마지막 기도도 마침내 끝났다!\n&35\n천 년의 세월을 넘어 마왕이 여기서 부활하리라!\n보아라! 마왕이 부활할 때가 왔다!\n&35\n사방의 힘이여, 나를 도우라!\n마계에 봉인된 나의 힘을 풀어 다오!\n&35\n청룡, 주작, 백호, 현무여.\n마계에서 이승으로 이끄는 길잡이가 되어\n마왕을 이 세상에 강림하게 하라!\n&35\n……?@어째서?@어찌 된 것이냐.\n어째서 청룡이 응답하지 않는 것이냐!?')
        stage.unit(0).setDir(stage.countDir(0, 35))
        stage.say('&0\n하하하, 유감이로군.\n허도의 제단도 기도도 모두 거짓일세.')
        stage.unit(35).setDir(stage.countDir(35, 0))
        stage.say('&35\n하지만 동쪽에서 분명 청룡의 요기가 느껴졌다……\n&0\n그 요기는 서서가 만들어 낸 것이다.\n그는 봉인을 풀 제단을 세우지 않았다.\n&35\n괘씸한 서서, 감히 나를 속이다니……\n으으, 그렇다면 너부터 죽이고\n허도로 진군하겠다.')
        stage.unit(35).setAction(5)
        stage.say('&35\n망령들아, 조조를 죽여라!\n살아서 이곳을 떠나게 두지 마라!')
        stage.unit(35).setAction(0)
        stage.unit(2).setDir(stage.countDir(2, 35))
        stage.say('&2\n어림없다!\n네놈이 세상에서 멋대로 날뛰게 둘 수는 없다!\n이 장료가 네놈을 죽이겠다!')
        stage.unit(2).setDir(stage.countDir(2, 35))
        stage.say('&7\n허도로 가게 둘 수는 없다.\n만백성의 소망을 이루기 위해\n내가 네놈을 죽이겠다!')
        stage.unit(24).setDir(stage.countDir(24, 35))
        stage.say('&24\n위와 오, 촉의 군사와 백성이\n모두 평화를 위해 목숨을 바쳤습니다.\n그들의 죽음을 헛되게 해서는 안 됩니다!')
        vars[22] = 0
    if vars[4] and not vars[21] and not vars[22] and not vars[19] and stage.round() >= 1 and stage.curCamp() == 0:
        vars[19] = 1
        vars[22] = 1
    if vars[4] and not vars[21] and not vars[22] and vars[19] and not vars[20] and stage.curCamp() == 0 and stage.round() >= 1:
        vars[20] = 1
        vars[22] = 1
    if vars[4] and not vars[21] and not vars[22] and vars[20] and not vars[21] and stage.curCamp() == 0 and stage.round() >= 1:
        vars[21] = 1
        vars[22] = 1
    if vars[4] and not vars[21] and vars[22] and stage.curCamp() == 2 and stage.round() >= 1:
        vars[22] = 0
    if vars[51] and vars[52] and not vars[4] and not vars[13] and stage.round() >= 1:
        if vars[60]:
            stage.setObjects(False, 16, [
                [
                    83,
                    12,
                    19],
                [
                    84,
                    12,
                    20]])
            if vars[61]:
                stage.setObjects(False, 16, [
                    [
                        91,
                        16,
                        19],
                    [
                        92,
                        16,
                        20]])
            elif vars[61]:
                stage.setObjects(False, 16, [
                    [
                        91,
                        16,
                        19],
                    [
                        92,
                        16,
                        20]])
        stage.setObjects(False, 16, [
            [
                95,
                19,
                16],
            [
                96,
                20,
                16]])
        stage.setObjects(False, 16, [
            [
                87,
                19,
                12],
            [
                88,
                20,
                12]])
        stage.unit(0).setAction(5)
        stage.say('&0\n마지막 문이 열린 모양이군.\n좋다. 전군 돌입하라!\n공명을 제거하라!')
        stage.unit(0).setAction(0)
        stage.unit(21).setDir(stage.countDir(21, 0))
        stage.say('&21\n이제 공명만 없애면 대업을 이룰 수 있습니다.')
        stage.unit(35).show()
        stage.say('&35\n결국 놈들이 왔군.\n으으, 한 걸음만 더 가면 되는데……\n망령들아, 다시 한번 놈들을 막아라!')
        stage.unit(163).show(20, 4, 2)
        stage.say('&163\n으으……아아……')
        stage.unit(164).show(19, 4, 2)
        stage.say('&164\n으으……오오……')
        stage.unit(35).setDir(stage.countDir(35, 164))
        stage.say('&35\n두 번째 죽음을 맞으면\n인간으로서의 의식마저 잃는군.\n그러면 더욱 부리기 쉬워지겠지.')
        stage.unit(168).show(20, 5, 2)
        stage.unit(167).show(19, 5, 2)
        stage.unit(166).show(18, 5, 2)
        stage.unit(165).show(21, 5, 2)
        stage.unit(162).show(18, 4, 2)
        stage.unit(169).show(21, 4, 2)
        stage.setAI(18, 4, 21, 5, 2, 1)
        stage.unit(35).setDir(stage.countDir(35, 0))
        stage.unit(35).setAction(5)
        stage.say('&35\n조조만 죽이면\n너희 영혼을 풀어 주겠다.\n가거라. 놈들을 막아라!')
        stage.unit(35).setAction(0)
        vars[19] = 0
        vars[20] = 0
        vars[21] = 0
        vars[22] = 0
        vars[4] = 1
        vars[13] = 1
    if not vars[70] and not vars[70] and not vars[71] and stage.curCamp() == 0 and stage.round() >= 4:
        if not vars[2]:
            if not vars[56]:
                stage.unit(414).show(35, 19, 3)
                stage.unit(415).show(36, 18, 3)
                stage.unit(416).show(36, 20, 3)
                stage.unit(417).show(37, 19, 3)
            if not vars[57]:
                stage.unit(418).show(33, 17, 3)
                stage.unit(419).show(34, 16, 3)
                stage.unit(420).show(34, 18, 3)
                stage.unit(421).show(35, 17, 3)
            if not vars[58]:
                stage.unit(422).show(34, 22, 3)
                stage.unit(423).show(35, 21, 3)
                stage.unit(424).show(35, 23, 3)
                stage.unit(425).show(36, 22, 3)
        if vars[2] and not vars[3]:
            if not vars[53]:
                stage.unit(438).show(20, 35, 0)
                stage.unit(439).show(21, 36, 0)
                stage.unit(440).show(19, 36, 0)
                stage.unit(441).show(20, 37, 0)
            if not vars[54]:
                stage.unit(442).show(22, 31, 0)
                stage.unit(443).show(23, 32, 0)
                stage.unit(444).show(21, 32, 0)
                stage.unit(445).show(22, 33, 0)
            if not vars[55]:
                stage.unit(446).show(17, 33, 0)
                stage.unit(447).show(18, 34, 0)
                stage.unit(448).show(16, 34, 0)
                stage.unit(449).show(17, 35, 0)
        if vars[4]:
            stage.unit(163).show(20, 4, 2)
            stage.unit(164).show(19, 4, 2)
            stage.unit(168).show(20, 5, 2)
            stage.unit(167).show(19, 5, 2)
            stage.unit(166).show(18, 5, 2)
            stage.unit(165).show(21, 5, 2)
            stage.unit(162).show(18, 4, 2)
            stage.unit(169).show(21, 4, 2)
            stage.unit(163).setAI(1)
            stage.unit(164).setAI(1)
            stage.unit(168).setAI(1)
            stage.unit(167).setAI(1)
            stage.unit(166).setAI(1)
            stage.unit(165).setAI(1)
            stage.unit(162).setAI(1)
            stage.unit(169).setAI(1)
        vars[71] = 1
        vars[72] = 0
        vars[73] = 1
        vars[70] = 1
    if not vars[70] and not vars[70] and not vars[72] and stage.curCamp() == 0 and stage.round() >= 4:
        vars[71] = 1
        vars[72] = 1
        vars[73] = 0
        vars[70] = 1
    if not vars[70] and not vars[70] and not vars[73] and stage.curCamp() == 0 and stage.round() >= 4:
        vars[71] = 0
        vars[72] = 1
        vars[73] = 1
        vars[70] = 1
    if vars[70] and stage.curCamp() == 2 and stage.round() >= 4:
        vars[70] = 0
    if not vars[40] and stage.isNear(0, 35, True):
        stage.unit(0).setDir(stage.countDir(0, 35))
        stage.unit(35).setDir(stage.countDir(35, 0))
        stage.say('&0\n도망칠 곳도 없는 지하로 들어온 것이\n네 가장 큰 실책이다.\n이제 네 목숨도 끝이다!\n&35\n그대의 처지도 다르지 않소.\n그대만 죽이면 위·촉·오 삼국은 완전히 무너질 것이니\n천하의 모든 것을 손쉽게 거머쥘 수 있지.')
        vars[40] = 1
    if not vars[4] and not vars[41] and stage.isNear(0, 163, True):
        stage.unit(0).setDir(stage.countDir(0, 163))
        stage.unit(163).setDir(stage.countDir(163, 0))
        stage.say('&0\n현덕, 정신 차리시오!\n&163\n아무 소리도 들리지 않고, 아무것도 느껴지지 않는다……\n오직 너만 보인다, 조조!')
        vars[41] = 1
    if not vars[4] and not vars[42] and stage.isNear(3, 164, True):
        stage.unit(3).setDir(stage.countDir(3, 164))
        stage.unit(164).setDir(stage.countDir(164, 3))
        stage.say('&3\n셋째 아우야, 내 말이 들리느냐?\n정신이 드느냐, 아우야!\n&164\n둘째 형님의 목소리……\n나는 대체 무엇을 하는 거지?\n형님, 어찌해야 하는지 알려 주시오.\n&3\n아우야……\n내 손으로 네게 안식을 주마.')
        vars[42] = 1
    if not vars[4] and not vars[43] and stage.isNear(1, 166, True):
        stage.unit(1).setDir(stage.countDir(1, 166))
        stage.unit(166).setDir(stage.countDir(166, 1))
        stage.say('&1\n나는 하후돈이다. 기억하는가?\n사수관에서 한번 만났었지.\n&166\n아무것도 기억나지 않는다.\n네놈은 누구냐? 왜 나를 방해하지?\n&1\n가엾은 꼴이로군.\n내가 다시 저승으로 보내 주마!')
        vars[43] = 1
    if not vars[4] and not vars[44] and stage.isNear(4, 167, True):
        stage.unit(4).setDir(stage.countDir(4, 167))
        stage.unit(167).setDir(stage.countDir(167, 4))
        stage.say('&4\n손책 장군, 나와 승부를 겨루시오!\n&167\n조조는 어디 있느냐?\n앞을 막는 자는 누구든 베겠다.\n조조! 어디 있느냐!?\n&4\n으음, 내 말이 들리지 않는 것이오?')
        vars[44] = 1
    if not vars[4] and not vars[45] and stage.isNear(2, 168, True):
        stage.unit(2).setDir(stage.countDir(2, 168))
        stage.unit(168).setDir(stage.countDir(168, 2))
        stage.say('&2\n손권, 나를 기억하느냐?\n&168\n으으으으윽, 물론 기억한다!\n합비에서 패한 치욕을 평생 잊을 수 없다!\n&2\n손권, 그대는 공명에게 조종당하고 있소.\n이제 저승으로 돌아가시오!\n&168\n내가 조종당한다고……?\n지금은 아무 생각도 없다.\n그저 네놈을 죽이고 싶을 뿐이다!\n&2\n손권 공……\n내 곧 그대를 해방시켜 드리리다.')
        vars[45] = 1
    if not vars[4] and not vars[46] and stage.isNear(22, 165, True):
        stage.unit(22).setDir(stage.countDir(22, 165))
        stage.unit(165).setDir(stage.countDir(165, 22))
        stage.say('&22\n주유, 그대 같은 불세출의 기재가\n이런 꼴이 되다니 참으로 비통하구려.\n&165\n오가 천하를 얻을 것이다.\n누구도 가로막게 두지 않겠다!\n&22\n오는 이미 멸망했소.\n주유, 다시 생각해 보시오.\n&165\n아아, 머리가 깨질 것 같다……\n눈앞이 온통 깜깜하고……\n귓가에는 찬바람만 분다……아아아!\n&22\n허, 이런 그대의 모습을 보고 싶지는 않았소.')
        vars[46] = 1
    if not vars[4] and not vars[47] and stage.isNear(17, 162, True):
        stage.unit(17).setDir(stage.countDir(17, 162))
        stage.unit(162).setDir(stage.countDir(162, 17))
        stage.say('&162\n으아, 오지 마라! 오지 마!\n왜 아무것도 보이지 않느냐?\n괴롭다! 누가 좀 구해 다오!\n&17\n가엾어라……\n공명은 너무 잔혹하군요……\n지금 그 고통을 덜어 드리겠습니다.')
        vars[47] = 1
    if not vars[4] and not vars[48] and stage.isNear(21, 169, True):
        stage.unit(21).setDir(stage.countDir(21, 169))
        stage.unit(169).setDir(stage.countDir(169, 21))
        stage.say('&169\n권……권……토중……중……\n으음, 마지막 글자가 생각나지 않는군.\n&21\n아무래도 권토중래를 말하려는 듯하군요.\n자신이 아직 살아 있다고 생각하는 모양입니다……\n참으로 비참하군요……공명의 죄는 용서할 수 없습니다!')
        vars[48] = 1
    if not vars[50] and stage.unitStateTest(35, 7, 0, 2):
        stage.say('&35\n네놈들 같은 하찮은 인간에게 죽다니……\n으아아악!')
        stage.unit(165).hide()
        stage.unit(162).hide()
        stage.unit(169).hide()
        stage.unit(168).hide()
        stage.unit(167).hide()
        stage.unit(166).hide()
        stage.unit(163).hide()
        stage.unit(164).hide()
        vars[50] = 1
        stage.end()
        vars[100] = 1
        vars[0] = 1
        vars[657] = 1
        return True
    if not vars[51] and stage.unitStateTest(163, 7, 0, 2):
        stage.say('&163\n곧 죽게 되겠지만\n마음은 더없이 평온하구나…….\n잘 있거라.')
        stage.unit(163).hide(2)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(164, 7, 0, 2):
        stage.say('&164\n나는 이제 만족하네.')
        stage.unit(164).hide(2)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(168, 7, 0, 2):
        stage.say('&168\n세상에 대한 미련도 사라졌으니\n이제 편히 쉴 수 있겠구나.')
        stage.unit(168).hide(2)
        stage.unit(438).hide()
        stage.unit(439).hide()
        stage.unit(440).hide()
        stage.unit(441).hide()
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(167, 7, 0, 2):
        stage.say('&167\n하아……그럼 잘 있거라.')
        stage.unit(167).hide(2)
        stage.unit(442).hide()
        stage.unit(443).hide()
        stage.unit(444).hide()
        stage.unit(445).hide()
        vars[54] = 1
    if not vars[55] and stage.unitStateTest(166, 7, 0, 2):
        stage.say('&166\n앞으로 나는 어찌 될까……\n앞날을 생각하니 괴롭구나.\n차라리 잠이나 자자……')
        stage.unit(166).hide(2)
        stage.unit(446).hide()
        stage.unit(447).hide()
        stage.unit(448).hide()
        stage.unit(449).hide()
        vars[55] = 1
    if not vars[56] and stage.unitStateTest(165, 7, 0, 2):
        stage.say('&165\n정신이 맑아졌다 흐려졌다 하니\n참으로 기묘한 느낌이군……….\n참, 반드시 공명을 쓰러뜨리게…….')
        stage.unit(165).hide(2)
        stage.unit(414).hide()
        stage.unit(415).hide()
        stage.unit(416).hide()
        stage.unit(417).hide()
        vars[56] = 1
    if not vars[57] and stage.unitStateTest(162, 7, 0, 2):
        stage.say('&162\n음, 이제야 잠들 수 있겠군.\n감사하기 그지없소.')
        stage.unit(162).hide(2)
        stage.unit(418).hide()
        stage.unit(419).hide()
        stage.unit(420).hide()
        stage.unit(421).hide()
        vars[57] = 1
    if not vars[58] and stage.unitStateTest(169, 7, 0, 2):
        stage.say('&169\n모든 것이 선명해지는군…….\n분명 나는 죽었던 것으로 기억하는데\n두 번 죽는 것도 제법 재미있겠어.')
        stage.unit(169).hide(2)
        stage.unit(422).hide()
        stage.unit(423).hide()
        stage.unit(424).hide()
        stage.unit(425).hide()
        vars[58] = 1
    if vars[50] and vars[100] and not vars[0] and stage.winTest():
        stage.end()
        vars[0] = 1
        vars[657] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 공명에게 패했다!')
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
