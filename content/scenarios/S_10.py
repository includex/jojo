# Source Generated with Decompyle++
# File: S_10.marshal (Python 3.9)


def scene0():
    stage.nearEvent([
        16,
        144])
    stage.nearEvent([
        16,
        143,
        16,
        23], 1)
    stage.initFight()
    stage.setGlobalData(20, -2, -1, 0, 1, 1)
    stage.createFriend([
        {
            'id': 31,
            'x': 16,
            'y': 8,
            'dir': 1,
            'lv': 0,
            'i': 0 },
        {
            'id': 30,
            'hide': 1,
            'x': 6,
            'y': 8,
            'dir': 1,
            'lv': 1,
            'ai': 1,
            'i': 1 }])
    stage.createEnemy([
        {
            'id': 143,
            'x': 22,
            'y': 8,
            'dir': 3,
            'lv': 6,
            'ai': 1,
            'i': 0 },
        {
            'id': 23,
            'x': 22,
            'y': 7,
            'dir': 3,
            'lv': 6,
            'ai': 1,
            'i': 1 },
        {
            'id': 210,
            'x': 21,
            'y': 10,
            'dir': 3,
            'lv': 0,
            'ai': 1,
            'i': 2 },
        {
            'id': 211,
            'x': 9,
            'y': 7,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 3 },
        {
            'id': 212,
            'x': 10,
            'y': 11,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 4 },
        {
            'id': 213,
            'x': 13,
            'y': 3,
            'lv': 0,
            'ai': 1,
            'i': 5 },
        {
            'id': 214,
            'x': 14,
            'y': 4,
            'lv': 0,
            'ai': 1,
            'i': 6 },
        {
            'id': 215,
            'x': 13,
            'y': 14,
            'dir': 0,
            'lv': 0,
            'ai': 1,
            'i': 7 },
        {
            'id': 216,
            'x': 14,
            'y': 13,
            'dir': 0,
            'lv': 0,
            'ai': 1,
            'i': 8 },
        {
            'id': 234,
            'x': 21,
            'y': 5,
            'dir': 3,
            'lv': 0,
            'ai': 1,
            'i': 9 },
        {
            'id': 235,
            'x': 12,
            'y': 2,
            'lv': 0,
            'ai': 1,
            'i': 10 },
        {
            'id': 236,
            'x': 14,
            'y': 3,
            'lv': 0,
            'ai': 1,
            'i': 11 },
        {
            'id': 237,
            'x': 14,
            'y': 15,
            'dir': 0,
            'lv': 0,
            'ai': 1,
            'i': 12 },
        {
            'id': 238,
            'x': 16,
            'y': 16,
            'dir': 0,
            'lv': 0,
            'ai': 1,
            'i': 13 },
        {
            'id': 144,
            'x': 17,
            'y': 8,
            'dir': 1,
            'lv': 6,
            'ai': 3,
            'targetId': 16,
            'i': 14 },
        {
            'id': 217,
            'x': 16,
            'y': 7,
            'dir': 1,
            'lv': 0,
            'ai': 3,
            'targetId': 16,
            'i': 15 },
        {
            'id': 218,
            'x': 17,
            'y': 10,
            'dir': 1,
            'lv': 0,
            'ai': 3,
            'targetId': 16,
            'i': 16 }])
    stage.createMine([
        {
            'idx': 0,
            'x': 12,
            'y': 8,
            'dir': 3 },
        {
            'idx': 1,
            'x': 20,
            'y': 8,
            'dir': 1 },
        {
            'idx': 2,
            'x': 1,
            'y': 8,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 3,
            'x': 14,
            'y': 6,
            'dir': 1 },
        {
            'idx': 4,
            'x': 13,
            'y': 11 },
        {
            'idx': 5,
            'x': 10,
            'y': 9,
            'dir': 3 },
        {
            'idx': 6,
            'x': 1,
            'y': 10,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 7,
            'y': 8,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 8,
            'x': 1,
            'y': 7,
            'dir': 1,
            'hide': 1 },
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
    stage.setEnemyEquip(144, -1, 0, 23, 0, -1)
    stage.loadBg(10)
    stage.setFire(True, 7, 5)
    stage.setFires(True, [
        [
            8,
            4],
        [
            8,
            5]])
    stage.setFires(True, [
        [
            9,
            4],
        [
            8,
            6]])
    stage.setFires(True, [
        [
            9,
            5],
        [
            8,
            7]])
    stage.setFire(True, 9, 6)
    stage.setFires(True, [
        [
            10,
            5],
        [
            11,
            5]])
    stage.setFire(True, 9, 12)
    stage.setFires(True, [
        [
            10,
            13],
        [
            10,
            12]])
    stage.setFires(True, [
        [
            11,
            13],
        [
            11,
            12],
        [
            11,
            11]])
    stage.setFire(True, 12, 12)
    stage.setFires(True, [
        [
            20,
            13],
        [
            20,
            12]])
    stage.setFires(True, [
        [
            19,
            13],
        [
            20,
            11]])
    stage.setFire(True, 19, 12)
    stage.setFires(True, [
        [
            18,
            13],
        [
            19,
            11]])
    stage.setFires(True, [
        [
            18,
            12],
        [
            19,
            10]])
    stage.setFire(True, 18, 11)
    stage.setFire(True, 17, 12)
    stage.setFire(True, 18, 4)
    stage.setFire(True, 19, 5)
    stage.setFire(True, 17, 4)
    stage.setFire(True, 18, 5)
    stage.setFire(True, 19, 6)
    stage.setFires(True, [
        [
            16,
            5],
        [
            17,
            5]])
    stage.setFire(True, 18, 6)
    stage.setFire(True, 19, 7)
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab52')
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
            goto('lab52')
    stage.draw()
    stage.bgSound(6)
    stage.unit(31).setDir(stage.countDir(31, 0))
    stage.unit(0).setDir(stage.countDir(0, 31))
    stage.say('@dialogue:S_10.scene0.dialogue0001')
    stage.unit(144).setDir(stage.countDir(144, 31))
    stage.say('@dialogue:S_10.scene0.dialogue0002')
    stage.attackAction(144, 31, 1)
    stage.delay(10)
    stage.unit(31).setAction(4)
    stage.say('@dialogue:S_10.scene0.dialogue0003')
    stage.unit(31).hide(2)
    stage.unit(0).setAction(4)
    stage.say('@dialogue:S_10.scene0.dialogue0004')
    stage.unit(16).setDir(stage.countDir(16, 0))
    stage.say('@dialogue:S_10.scene0.dialogue0005')
    stage.unit(0).setDir(3)
    stage.unit(0).setAction(5)
    stage.say('@dialogue:S_10.scene0.dialogue0006')
    stage.unit(0).setAction(0)
    stage.unit(143).setDir(stage.countDir(143, 0))
    stage.say('@dialogue:S_10.scene0.dialogue0007')
    stage.setWinCondition('승리 조건\n1. 조조가 서쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
    stage.showWinCondition('조조가 달아난다!')
    stage.heightLight(0, 7, 1, 10)
    stage.startOper()
    vars[510] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[11] and not vars[53] and stage.round() >= 1 and stage.unitStateTest(16, 7, 50, 1):
        stage.unit(16).setAction(9)
        stage.say('@dialogue:S_10.scene1.dialogue0001')
        stage.unit(16).setAction(7)
        stage.say('@dialogue:S_10.scene1.dialogue0002')
        stage.unit(16).setAction(0)
        vars[11] = 1
    if not vars[11] and not vars[12] and not vars[53] and stage.unitStateTest(16, 7, 100, 1) and stage.round() >= 1:
        stage.unit(16).setAction(5)
        stage.say('@dialogue:S_10.scene1.dialogue0003')
        stage.unit(16).setAction(0)
        stage.unit(16).setAction(6)
        stage.say('@dialogue:S_10.scene1.dialogue0004')
        stage.unit(16).setAction(0)
        vars[12] = 1
    if not vars[30] and not vars[31] and stage.isInRect(0, 0, 0, 7, 19):
        stage.unit(30).show()
        stage.unit(30).setDir(stage.countDir(30, 0))
        stage.say('@dialogue:S_10.scene1.dialogue0005')
        vars[30] = 1
    if not vars[31] and not vars[33] and stage.isInRect(0, 0, 7, 1, 10):
        stage.unit(237).setAI(1)
        stage.unit(2, 1).show()
        stage.unit(6, 1).show()
        stage.unit(7, 1).show()
        stage.unit(8, 1).show()
        stage.unit(31).setDir(stage.countDir(31, 0))
        stage.say('@dialogue:S_10.scene1.dialogue0006')
        stage.unit(0).setDir(stage.countDir(0, 31))
        stage.say('@dialogue:S_10.scene1.dialogue0007')
        if vars[53]:
            stage.say('@dialogue:S_10.scene1.dialogue0008')
            stage.unit(0).setAction(4)
            stage.unit(0).setDir(1)
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_10.scene1.dialogue0009')
            stage.unit(0).setAction(0)
            stage.reward()
            stage.end()
            vars[0] = 1
            vars[100] = 1
            vars[610] = 1
            return True
        if not vars[53]:
            stage.say('@dialogue:S_10.scene1.dialogue0010')
            stage.unit(0).setDir(stage.countDir(0, 16))
            stage.say('@dialogue:S_10.scene1.dialogue0011')
            stage.unit(0).setDir(stage.countDir(0, 12))
            stage.say('@dialogue:S_10.scene1.dialogue0012')
            stage.unit(0).setDir(stage.countDir(0, 16))
            stage.say('@dialogue:S_10.scene1.dialogue0013')
            sel = stage.choice('이대로 퇴각한다\n돌아가 전위를 구한다', -1)
            if sel == 1:
                stage.unit(0).setAction(4)
                stage.say('@dialogue:S_10.scene1.dialogue0014')
                stage.say('@dialogue:S_10.scene1.dialogue0015')
                stage.unit(16).setAction(8)
                stage.say('@dialogue:S_10.scene1.dialogue0016')
                stage.reward()
                stage.end()
                vars[0] = 1
                vars[101] = 1
                vars[610] = 1
                vars[710] = 1
                return True
            if sel == 2:
                stage.unit(0).setAction(5)
                stage.say('@dialogue:S_10.scene1.dialogue0017')
                stage.unit(0).setAction(0)
                stage.setWinCondition('승리 조건\n1. 전위가 서쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
                stage.showWinCondition('전위가 탈출한다!')
                stage.unit(16).heightLight()
                stage.heightLight(0, 7, 1, 10)
                vars[2] = 1
        vars[31] = 1
    if not vars[31] and vars[33] and stage.isInRect(0, 0, 7, 1, 10):
        stage.unit(237).setAI(1)
        stage.unit(2, 1).show()
        stage.unit(6, 1).show()
        stage.unit(7, 1).show()
        stage.unit(8, 1).show()
        stage.unit(31).setDir(stage.countDir(31, 0))
        stage.say('@dialogue:S_10.scene1.dialogue0018')
        stage.unit(0).setDir(stage.countDir(0, 31))
        stage.say('@dialogue:S_10.scene1.dialogue0019')
        if vars[53]:
            stage.say('@dialogue:S_10.scene1.dialogue0020')
            stage.unit(0).setAction(4)
            stage.unit(0).setDir(1)
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_10.scene1.dialogue0021')
            stage.unit(0).setAction(0)
        if not vars[53]:
            stage.unit(0).setAction(4)
            stage.unit(0).setDir(1)
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_10.scene1.dialogue0022')
            stage.unit(0).setAction(0)
        vars[31] = 1
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[102] = 1
        vars[610] = 1
        return True
    if vars[2] and not vars[32] and stage.isInRect(16, 0, 7, 1, 10):
        stage.say('@dialogue:S_10.scene1.dialogue0023')
        stage.unit(0).setDir(stage.countDir(0, 16))
        stage.say('@dialogue:S_10.scene1.dialogue0024')
        stage.unit(16).setDir(stage.countDir(16, 0))
        stage.say('@dialogue:S_10.scene1.dialogue0025')
        stage.unit(0).setAction(5)
        stage.say('@dialogue:S_10.scene1.dialogue0026')
        stage.unit(0).setAction(0)
        vars[32] = 1
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[103] = 1
        vars[610] = 1
        return True
    if not vars[33] and stage.isInRect(16, 0, 0, 10, 19):
        vars[33] = 1
    if not vars[2] and not vars[40] and stage.isNear(16, 144, False):
        stage.unit(144).setDir(stage.countDir(144, 16))
        stage.unit(16).setDir(stage.countDir(16, 144))
        stage.say('@dialogue:S_10.scene1.dialogue0027')
        fight = stage.startFight(16, 144, 5)
        fight.showUnit(False, '하하하, 오늘이 네 제삿날이다!', 0)
        fight.showUnit(True, '장수라면 주군을 위해 충성을 다하다 죽는 것이 마땅하다!\n하지만 그따위 재주로는\n이 전위가 쉽게 쓰러지지 않는다!', 9)
        fight.showStart()
        fight.say(False, '내 공격을 받아라!', False)
        fight.attack1(False, 1, False)
        stage.delay(10)
        fight.setAction(False, 0)
        fight.say(False, '오? 내 필살의 암기를 피하다니……!\n이것도 받아라!', True)
        fight.setAction(False, 2)
        fight.attack1(False, 2, True)
        stage.delay(5)
        fight.setAction(True, 10)
        fight.say(True, '우쭐대지 마라! 이번에는 내 차례다!', True)
        fight.setAction(True, 2)
        fight.attack2(True, 0, True)
        fight.attack2(True, 0, True)
        fight.say(False, '오!', False)
        fight.attack2(True, 0, False)
        fight.attack1(True, 3, True)
        fight.attack1(False, 0, False)
        stage.delay(5)
        fight.setAction(True, 11)
        fight.say(True, '으윽!', True)
        stage.delay(10)
        fight.setAction(False, 0)
        fight.say(False, '……방심했군?\n내 재주가 암기뿐인 줄 알았느냐.\n이번에는 네 목숨을 거두겠다!', True)
        fight.setAction(False, 2)
        fight.attack2(False, 0, False)
        fight.attack2(False, 0, False)
        fight.attack2(False, 0, True)
        fight.attack2(True, 1, False)
        fight.say(False, '으아!', True)
        stage.delay(10)
        fight.setAction(True, 1)
        fight.setAction(False, 12)
        fight.say(False, '과연 악래라는 이름이 헛되지 않군.\n하지만 언제까지 버틸 수 있을까……?\n하하하.', True)
        fight.setAction(False, 2)
        fight.setAction(False, 2)
        fight.setAction(False, 3)
        fight.setAction(False, 13)
        stage.delay(10)
        fight.setAction(True, 11)
        fight.say(True, '우웁! 이놈, 제법이군!', True)
        stage.delay(5)
        fight.setAction(True, 6)
        fight.say(True, '하지만 나를 쓰러뜨리기는 쉽지 않을 것이다!\n죽어도 너를 지나가게 두지 않겠다!', True)
        fight.end()
        vars[40] = 1
    if not vars[2] and not vars[41] and stage.isNear(16, 143, True):
        stage.unit(16).setDir(stage.countDir(16, 143))
        stage.unit(143).setDir(-1)
        stage.say('@dialogue:S_10.scene1.dialogue0028')
        vars[41] = 1
    if not vars[2] and not vars[42] and stage.isNear(16, 23, True):
        stage.unit(16).setDir(stage.countDir(16, 23))
        stage.unit(23).setDir(stage.countDir(23, 16))
        stage.say('@dialogue:S_10.scene1.dialogue0029')
        vars[42] = 1
    if not vars[50] and stage.unitStateTest(143, 7, 0, 2):
        stage.say('@dialogue:S_10.scene1.dialogue0030')
        stage.unit(143).hide(1)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(23, 7, 0, 2):
        stage.say('@dialogue:S_10.scene1.dialogue0031')
        stage.unit(23).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(144, 7, 0, 2):
        stage.say('@dialogue:S_10.scene1.dialogue0032')
        stage.unit(144).hide(2)
        stage.getItem(91, 0, True, 1027)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(16, 7, 0, 2):
        if vars[2]:
            stage.unit(16).setAction(4)
            stage.say('@dialogue:S_10.scene1.dialogue0033')
            stage.unit(16).retreatTxt(False)
            stage.unit(16).hide(2)
            stage.unit(0).setDir(stage.countDir(0, 16))
            stage.say('@dialogue:S_10.scene1.dialogue0034')
            stage.reward()
            stage.end()
            vars[0] = 1
            vars[104] = 1
            vars[610] = 1
            vars[710] = 1
            return True
        if not vars[2]:
            stage.unit(16).setAction(4)
            stage.say('@dialogue:S_10.scene1.dialogue0035')
            stage.unit(16).retreatTxt(False)
            stage.unit(16).hide(2)
            vars[710] = 1
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(30, 7, 0, 2):
        stage.say('@dialogue:S_10.scene1.dialogue0036')
        stage.unit(30).hide(2)
        vars[54] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_10.scene1.dialogue0037')
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[610] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 장수군에게 패했다.')
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
