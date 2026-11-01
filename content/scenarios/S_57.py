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
    stage.say('@dialogue:S_57.scene0.dialogue0001')
    stage.unit(19).setDir(stage.countDir(19, 0))
    stage.say('@dialogue:S_57.scene0.dialogue0002')
    stage.unit(6).setDir(1)
    stage.say('@dialogue:S_57.scene0.dialogue0003')
    stage.unit(0).setAction(5)
    stage.say('@dialogue:S_57.scene0.dialogue0004')
    stage.unit(0).setAction(0)
    stage.unit(0, 1).move(27, 19, 1)
    stage.unit(3, 1).move(27, 20, 1)
    stage.unit(4, 1).move(26, 19, 1)
    stage.unit(5, 1).move(26, 20, 1)
    stage.unit(6, 1).move(25, 19, 1)
    stage.unit(7, 1).move(25, 20, 1)
    stage.say('@dialogue:S_57.scene0.dialogue0005')
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
        stage.say('@dialogue:S_57.scene1.dialogue0001')
        stage.unit(17).setDir(stage.countDir(17, 0))
        stage.say('@dialogue:S_57.scene1.dialogue0002')
        stage.unit(23).setDir(stage.countDir(23, 0))
        stage.say('@dialogue:S_57.scene1.dialogue0003')
        stage.unit(0).setAction(5)
        stage.say('@dialogue:S_57.scene1.dialogue0004')
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
        stage.say('@dialogue:S_57.scene1.dialogue0005')
        stage.unit(20).setDir(stage.countDir(20, 1))
        stage.say('@dialogue:S_57.scene1.dialogue0006')
        stage.unit(0).setDir(stage.countDir(0, 20))
        stage.say('@dialogue:S_57.scene1.dialogue0007')
        stage.unit(166).show()
        stage.unit(167).show()
        stage.unit(168).show()
        stage.say('@dialogue:S_57.scene1.dialogue0008')
        stage.unit(167).setDir(stage.countDir(167, 166))
        stage.say('@dialogue:S_57.scene1.dialogue0009')
        stage.unit(168).setDir(stage.countDir(168, 166))
        stage.say('@dialogue:S_57.scene1.dialogue0010')
        stage.unit(166).setDir(stage.countDir(166, 0))
        stage.say('@dialogue:S_57.scene1.dialogue0011')
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
        stage.say('@dialogue:S_57.scene1.dialogue0012')
        stage.unit(0).setDir(stage.countDir(0, 166))
        stage.say('@dialogue:S_57.scene1.dialogue0013')
        stage.unit(4).setDir(stage.countDir(4, 167))
        stage.say('@dialogue:S_57.scene1.dialogue0014')
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
                        stage.say('@dialogue:S_57.scene1.dialogue0015')
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
                        stage.say('@dialogue:S_57.scene1.dialogue0016')
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
                stage.say('@dialogue:S_57.scene1.dialogue0017')
                stage.unit(9).setDir(stage.countDir(9, 0))
                stage.say('@dialogue:S_57.scene1.dialogue0018')
                stage.unit(22).setDir(stage.countDir(22, 0))
                stage.say('@dialogue:S_57.scene1.dialogue0019')
                stage.unit(163).show()
                stage.unit(164).show()
                stage.unit(32).setAction(4)
                stage.say('@dialogue:S_57.scene1.dialogue0020')
                stage.unit(164).setDir(stage.countDir(164, 0))
                stage.say('@dialogue:S_57.scene1.dialogue0021')
                stage.unit(3).setDir(stage.countDir(3, 32))
                stage.say('@dialogue:S_57.scene1.dialogue0022')
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
                stage.say('@dialogue:S_57.scene1.dialogue0023')
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
                stage.say('@dialogue:S_57.scene1.dialogue0024')
                stage.unit(9).setDir(stage.countDir(9, 0))
                stage.say('@dialogue:S_57.scene1.dialogue0025')
                stage.unit(22).setDir(stage.countDir(22, 0))
                stage.say('@dialogue:S_57.scene1.dialogue0026')
                stage.unit(163).show()
                stage.unit(164).show()
                stage.unit(32).setAction(4)
                stage.say('@dialogue:S_57.scene1.dialogue0027')
                stage.unit(164).setDir(stage.countDir(164, 0))
                stage.say('@dialogue:S_57.scene1.dialogue0028')
                stage.unit(3).setDir(stage.countDir(3, 163))
                stage.say('@dialogue:S_57.scene1.dialogue0029')
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
                stage.say('@dialogue:S_57.scene1.dialogue0030')
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
        stage.say('@dialogue:S_57.scene1.dialogue0031')
        stage.unit(10).setDir(stage.countDir(10, 0))
        stage.say('@dialogue:S_57.scene1.dialogue0032')
        stage.unit(14).setDir(stage.countDir(14, 0))
        stage.say('@dialogue:S_57.scene1.dialogue0033')
        vars[12] = 1
    if vars[4] and vars[21] and vars[22] and stage.round() >= 1:
        stage.unit(35).setDir(2)
        stage.unit(35).setAction(1)
        stage.say('@dialogue:S_57.scene1.dialogue0034')
        stage.unit(0).setDir(stage.countDir(0, 35))
        stage.say('@dialogue:S_57.scene1.dialogue0035')
        stage.unit(35).setDir(stage.countDir(35, 0))
        stage.say('@dialogue:S_57.scene1.dialogue0036')
        stage.unit(35).setAction(5)
        stage.say('@dialogue:S_57.scene1.dialogue0037')
        stage.unit(35).setAction(0)
        stage.unit(2).setDir(stage.countDir(2, 35))
        stage.say('@dialogue:S_57.scene1.dialogue0038')
        stage.unit(2).setDir(stage.countDir(2, 35))
        stage.say('@dialogue:S_57.scene1.dialogue0039')
        stage.unit(24).setDir(stage.countDir(24, 35))
        stage.say('@dialogue:S_57.scene1.dialogue0040')
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
        stage.say('@dialogue:S_57.scene1.dialogue0041')
        stage.unit(0).setAction(0)
        stage.unit(21).setDir(stage.countDir(21, 0))
        stage.say('@dialogue:S_57.scene1.dialogue0042')
        stage.unit(35).show()
        stage.say('@dialogue:S_57.scene1.dialogue0043')
        stage.unit(163).show(20, 4, 2)
        stage.say('@dialogue:S_57.scene1.dialogue0044')
        stage.unit(164).show(19, 4, 2)
        stage.say('@dialogue:S_57.scene1.dialogue0045')
        stage.unit(35).setDir(stage.countDir(35, 164))
        stage.say('@dialogue:S_57.scene1.dialogue0046')
        stage.unit(168).show(20, 5, 2)
        stage.unit(167).show(19, 5, 2)
        stage.unit(166).show(18, 5, 2)
        stage.unit(165).show(21, 5, 2)
        stage.unit(162).show(18, 4, 2)
        stage.unit(169).show(21, 4, 2)
        stage.setAI(18, 4, 21, 5, 2, 1)
        stage.unit(35).setDir(stage.countDir(35, 0))
        stage.unit(35).setAction(5)
        stage.say('@dialogue:S_57.scene1.dialogue0047')
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
        stage.say('@dialogue:S_57.scene1.dialogue0048')
        vars[40] = 1
    if not vars[4] and not vars[41] and stage.isNear(0, 163, True):
        stage.unit(0).setDir(stage.countDir(0, 163))
        stage.unit(163).setDir(stage.countDir(163, 0))
        stage.say('@dialogue:S_57.scene1.dialogue0049')
        vars[41] = 1
    if not vars[4] and not vars[42] and stage.isNear(3, 164, True):
        stage.unit(3).setDir(stage.countDir(3, 164))
        stage.unit(164).setDir(stage.countDir(164, 3))
        stage.say('@dialogue:S_57.scene1.dialogue0050')
        vars[42] = 1
    if not vars[4] and not vars[43] and stage.isNear(1, 166, True):
        stage.unit(1).setDir(stage.countDir(1, 166))
        stage.unit(166).setDir(stage.countDir(166, 1))
        stage.say('@dialogue:S_57.scene1.dialogue0051')
        vars[43] = 1
    if not vars[4] and not vars[44] and stage.isNear(4, 167, True):
        stage.unit(4).setDir(stage.countDir(4, 167))
        stage.unit(167).setDir(stage.countDir(167, 4))
        stage.say('@dialogue:S_57.scene1.dialogue0052')
        vars[44] = 1
    if not vars[4] and not vars[45] and stage.isNear(2, 168, True):
        stage.unit(2).setDir(stage.countDir(2, 168))
        stage.unit(168).setDir(stage.countDir(168, 2))
        stage.say('@dialogue:S_57.scene1.dialogue0053')
        vars[45] = 1
    if not vars[4] and not vars[46] and stage.isNear(22, 165, True):
        stage.unit(22).setDir(stage.countDir(22, 165))
        stage.unit(165).setDir(stage.countDir(165, 22))
        stage.say('@dialogue:S_57.scene1.dialogue0054')
        vars[46] = 1
    if not vars[4] and not vars[47] and stage.isNear(17, 162, True):
        stage.unit(17).setDir(stage.countDir(17, 162))
        stage.unit(162).setDir(stage.countDir(162, 17))
        stage.say('@dialogue:S_57.scene1.dialogue0055')
        vars[47] = 1
    if not vars[4] and not vars[48] and stage.isNear(21, 169, True):
        stage.unit(21).setDir(stage.countDir(21, 169))
        stage.unit(169).setDir(stage.countDir(169, 21))
        stage.say('@dialogue:S_57.scene1.dialogue0056')
        vars[48] = 1
    if not vars[50] and stage.unitStateTest(35, 7, 0, 2):
        stage.say('@dialogue:S_57.scene1.dialogue0057')
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
        stage.say('@dialogue:S_57.scene1.dialogue0058')
        stage.unit(163).hide(2)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(164, 7, 0, 2):
        stage.say('@dialogue:S_57.scene1.dialogue0059')
        stage.unit(164).hide(2)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(168, 7, 0, 2):
        stage.say('@dialogue:S_57.scene1.dialogue0060')
        stage.unit(168).hide(2)
        stage.unit(438).hide()
        stage.unit(439).hide()
        stage.unit(440).hide()
        stage.unit(441).hide()
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(167, 7, 0, 2):
        stage.say('@dialogue:S_57.scene1.dialogue0061')
        stage.unit(167).hide(2)
        stage.unit(442).hide()
        stage.unit(443).hide()
        stage.unit(444).hide()
        stage.unit(445).hide()
        vars[54] = 1
    if not vars[55] and stage.unitStateTest(166, 7, 0, 2):
        stage.say('@dialogue:S_57.scene1.dialogue0062')
        stage.unit(166).hide(2)
        stage.unit(446).hide()
        stage.unit(447).hide()
        stage.unit(448).hide()
        stage.unit(449).hide()
        vars[55] = 1
    if not vars[56] and stage.unitStateTest(165, 7, 0, 2):
        stage.say('@dialogue:S_57.scene1.dialogue0063')
        stage.unit(165).hide(2)
        stage.unit(414).hide()
        stage.unit(415).hide()
        stage.unit(416).hide()
        stage.unit(417).hide()
        vars[56] = 1
    if not vars[57] and stage.unitStateTest(162, 7, 0, 2):
        stage.say('@dialogue:S_57.scene1.dialogue0064')
        stage.unit(162).hide(2)
        stage.unit(418).hide()
        stage.unit(419).hide()
        stage.unit(420).hide()
        stage.unit(421).hide()
        vars[57] = 1
    if not vars[58] and stage.unitStateTest(169, 7, 0, 2):
        stage.say('@dialogue:S_57.scene1.dialogue0065')
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
