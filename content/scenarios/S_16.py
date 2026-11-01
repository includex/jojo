# Source Generated with Decompyle++
# File: S_16.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20], [
        '(9,2)'])
    stage.nearEvent([
        3,
        108])
    stage.nearEvent([
        0,
        101,
        0,
        105,
        0,
        108], 1)
    stage.initFight()
    stage.setGlobalData(25, -2, -1, 0, 0, 1)
    stage.createFriend([
        {
            'id': 212,
            'x': 16,
            'y': 8,
            'dir': 0,
            'lv': 0,
            'i': 0 },
        {
            'id': 267,
            'x': 17,
            'y': 8,
            'dir': 0,
            'lv': 0,
            'i': 1 },
        {
            'id': 3,
            'hide': 1,
            'x': 18,
            'y': 13,
            'dir': 0,
            'lv': 6,
            'ai': 1,
            'i': 2 }])
    stage.createEnemy([
        {
            'id': 101,
            'x': 9,
            'y': 2,
            'lv': 6,
            'i': 0 },
        {
            'id': 105,
            'x': 8,
            'y': 1,
            'lv': 6,
            'ai': 0,
            'i': 1 },
        {
            'id': 210,
            'x': 10,
            'y': 1,
            'lv': 0,
            'ai': 0,
            'i': 2 },
        {
            'id': 211,
            'x': 9,
            'y': 3,
            'lv': 0,
            'ai': 0,
            'i': 3 },
        {
            'id': 258,
            'x': 4,
            'y': 13,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 4 },
        {
            'id': 259,
            'x': 3,
            'y': 12,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 5 },
        {
            'id': 260,
            'x': 3,
            'y': 14,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 6 },
        {
            'id': 282,
            'x': 2,
            'y': 13,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 7 },
        {
            'id': 261,
            'x': 5,
            'y': 6,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 8 },
        {
            'id': 262,
            'x': 4,
            'y': 5,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 9 },
        {
            'id': 263,
            'x': 6,
            'y': 5,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 10 },
        {
            'id': 283,
            'x': 5,
            'y': 4,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 11 },
        {
            'id': 264,
            'x': 16,
            'y': 3,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 12 },
        {
            'id': 265,
            'x': 15,
            'y': 2,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 13 },
        {
            'id': 266,
            'x': 17,
            'y': 2,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 14 },
        {
            'id': 284,
            'x': 16,
            'y': 1,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 15 },
        {
            'id': 108,
            'x': 16,
            'y': 6,
            'lv': 6,
            'i': 16 },
        {
            'id': 285,
            'x': 3,
            'y': 13,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 17 },
        {
            'id': 342,
            'x': 5,
            'y': 5,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 18 },
        {
            'id': 350,
            'x': 16,
            'y': 2,
            'lv': 0,
            'ai': 6,
            'targetX': 5,
            'targetY': 7,
            'i': 19 }])
    stage.createMine([
        {
            'idx': 0,
            'x': 17,
            'y': 18,
            'dir': 0 },
        {
            'idx': 1,
            'x': 15,
            'y': 9,
            'dir': 0 },
        {
            'idx': 2,
            'x': 17,
            'y': 9,
            'dir': 0 },
        {
            'idx': 3,
            'x': 9,
            'y': 14,
            'dir': 3 },
        {
            'idx': 4,
            'x': 9,
            'y': 13,
            'dir': 3 },
        {
            'idx': 5,
            'x': 8,
            'y': 15,
            'dir': 3 },
        {
            'idx': 6,
            'x': 16,
            'y': 16,
            'dir': 0 },
        {
            'idx': 7,
            'x': 15,
            'y': 17,
            'dir': 0 },
        {
            'idx': 8,
            'x': 17,
            'y': 17,
            'dir': 0 },
        {
            'idx': 9,
            'x': 18,
            'y': 17,
            'dir': 0 }])
    stage.setEnemyEquip(3, 36, 0, -1, 0, 16)
    stage.setEnemyEquip(101, -1, 0, -1, 0, 4)
    stage.setEnemyEquip(108, -1, 0, -1, 0, 32)
    stage.loadBg(16)
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
    stage.bgSound(2)
    stage.unit(101).setDir(stage.countDir(101, 108))
    stage.say('&101\n안량, 조조군의 전력은 어떠한가?')
    stage.unit(108).setDir(stage.countDir(108, 101))
    stage.unit(108).setAction(7)
    stage.say('&108\n상대도 안 되는군.\n이 다리는 나 안량 혼자면 충분합니다.\n주군께서는 서쪽 다리로 가십시오.')
    stage.unit(108).setAction(0)
    stage.unit(105).setDir(stage.countDir(105, 101))
    stage.say('&105\n안 장군은 우리 군 최고의 맹장입니다.\n보통 사람은 맞설 수 없습니다.\n&105\n안 장군의 말씀대로\n서쪽에서 공세를 펼치는 것이 좋겠습니다.')
    stage.unit(101).setDir(2)
    stage.unit(105).setDir(2)
    stage.say('&101\n좋다. 동쪽 다리는 안량이 지키고\n나머지 부대는 서쪽 다리를 공격하라!')
    stage.unit(0).setDir(stage.countDir(0, 108))
    stage.say('&0\n안량인가?\n원소 휘하의 두 맹장 중 하나이지.\n결코 얕보아서는 안 된다.')
    stage.unit(212).setDir(stage.countDir(212, 0))
    stage.say('&212\n주군, 제게 맡겨 주십시오.\n제가 혼쭐을 내 주겠습니다!\n&0\n이봐, 기다려라…….')
    stage.unit(212).setDir(stage.countDir(212, 108))
    stage.unit(212).setAction(5)
    stage.say('&212\n네가 안량이냐?\n목숨을 내놓아라!')
    stage.unit(212).setAction(0)
    stage.unit(212).move(16, 7, 0)
    stage.unit(212).setDir(stage.countDir(212, 108))
    stage.unit(108).setDir(stage.countDir(108, 212))
    stage.say('&108\n소용없다. 너는 내 상대가 못 된다.')
    stage.attackAction(108, 212, 1)
    stage.delay(10)
    stage.unit(212).setAction(4)
    stage.say('&212\n으아아!')
    stage.unit(212).hide(2)
    stage.unit(267).setDir(stage.countDir(267, 108))
    stage.unit(267).setAction(2)
    stage.say('&267\n감히 우리 병사를 죽이다니!\n이번에는 내가 상대해 주마!')
    stage.unit(267).move(16, 7, 0)
    stage.unit(267).setDir(stage.countDir(267, 108))
    stage.unit(108).setDir(stage.countDir(108, 267))
    stage.attackAction(267, 108, 2)
    stage.delay(10)
    stage.attackAction(108, 267, 1)
    stage.delay(10)
    stage.unit(267).setAction(4)
    stage.say('&267\n으아악!')
    stage.unit(267).hide(2)
    stage.unit(108).setDir(stage.countDir(108, 0))
    stage.unit(108).setAction(5)
    stage.say('&108\n흥!\n누가 와도 두렵지 않다!\n다음은 누구냐?')
    stage.unit(108).setAction(0)
    stage.unit(0).setDir(stage.countDir(0, 108))
    stage.say('&0\n안량을 쓰러뜨리지 않으면\n저 작은 다리를 건널 수 없군.\n어쩔 수 없나. 운장을 불러야 할까……?')
    sel = stage.choice('관우를 출전시킨다\n정면 돌파한다', -1)
    if sel == 1:
        stage.unit(0).setAction(2)
        stage.say('&0\n관우가 올 때까지 안량을 공격하지 마라.\n우선 서쪽 다리를 지키는 데 힘쓴다!')
        stage.unit(6, 1).move(10, 14, 3)
        stage.unit(7, 1).move(9, 16, 3)
        vars[2] = 1
    elif sel == 2:
        stage.unit(0).setAction(5)
        stage.say('&0\n이대로 두면 적이 계속 모여들 것이다.\n최대한 빨리 섬멸해야 한다!')
        stage.unit(0).setAction(0)
        stage.unit(6, 1).move(16, 10, 0)
        stage.unit(7, 1).move(17, 11, 0)
        stage.unit(108).setAction(7)
        stage.say('&108\n이놈들아, 내게 와서 목숨을 바쳐라!\n그러면 주군께서 서쪽 다리를 치시기도 수월하겠지.')
        stage.unit(108).setAction(0)
    stage.setWinCondition('승리 조건\n1. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 25턴을 초과한다.')
    stage.showWinCondition('모든 적을 섬멸하라!')
    stage.startOper()
    vars[516] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[11] and stage.round() == 2:
        stage.unit(261).setAI(1)
        stage.unit(262).setAI(1)
        stage.unit(263).setAI(1)
        stage.unit(283).setAI(1)
        stage.unit(342).setAI(1)
        vars[11] = 1
    if vars[2] and not vars[12] and stage.round() == 3:
        stage.unit(3).show()
        if not vars[52]:
            stage.unit(3).setDir(stage.countDir(3, 0))
            stage.say('&3\n승상, 운장이 늦었습니다.')
            stage.unit(0).setDir(stage.countDir(0, 3))
            stage.say('&0\n운장.\n그대만 믿겠네.\n&3\n승상, 무슨 어려움이 있으십니까?\n&0\n적장 안량이 만만치 않아\n운장의 신위를 빌리고 싶네.\n&3\n좋습니다. 제게 맡겨 주십시오.\n&0\n음, 수고해 주게!')
            stage.unit(3).setDir(stage.countDir(3, 108))
        else:
            stage.unit(3).setDir(stage.countDir(3, 0))
            stage.say('&3\n승상, 운장이 늦었습니다.')
            stage.unit(0).setDir(stage.countDir(0, 3))
            stage.say('&0\n운장.\n&3\n승상, 무슨 어려움이라도 있소?\n&0\n아니네. 이제 해결되었네.\n원소 토벌에 힘을 보태 주게.\n&3\n알겠소. 이 운장에게 맡기시오.\n&0\n음, 수고를 부탁하네!')
        vars[716] = 1
        vars[12] = 1
    if not vars[13] and stage.round() == 4:
        stage.unit(264).setAI(1)
        stage.unit(265).setAI(1)
        stage.unit(266).setAI(1)
        stage.unit(284).setAI(1)
        stage.unit(350).setAI(1)
        vars[13] = 1
    if not vars[2] and not vars[14] and stage.round() == 5:
        stage.unit(3).show()
        stage.unit(3).setDir(stage.countDir(3, 0))
        stage.say('&3\n승상, 전황은 어떻습니까?')
        stage.unit(3).setDir(stage.countDir(3, 0))
        stage.say('&0\n운장, 자네인가?\n어찌 이곳에 왔는가?\n&3\n승상께서 어려움에 처하셨을까 염려되어……\n조금이나마 힘을 보태고자 왔소.\n&0\n그런가?\n그럼 이번 싸움에 함께해 주게.\n지금 우리 군은 고전하고 있네.\n&3\n알겠소!')
        stage.unit(3).setDir(stage.countDir(3, 108))
        vars[716] = 1
        vars[14] = 1
    if not vars[15] and stage.totalRectUnit(4, 16, 7, 16, 7) == 0 and stage.unitStateTest(108, 7, 150, 0) and stage.round() == 4:
        stage.unit(108).setDir(2)
        stage.unit(108).setAction(5)
        stage.say('&108\n아무래도 나와 싸울 담력이 있는 자는\n더 없는 모양이군.\n하하하하하!')
        stage.unit(108).setAction(0)
        vars[15] = 1
    if not vars[20] and stage.isInPos(1025, 9, 2):
        stage.getItem(151, 0, True, 1025)
        vars[20] = 1
    if not vars[40] and stage.isNear(0, 101, True):
        stage.unit(0).setDir(stage.countDir(0, 101))
        stage.unit(101).setDir(stage.countDir(101, 0))
        stage.say('&0\n원소, 그대의 군대는 제법 강하군.\n나와 같은 시대에 태어나지 않았다면\n천하는 이미 그대의 것이었겠지.\n&101\n추억담이나 나눌 여유는 없다.\n우리 전력 차이가 그리 크다고 느낀다면\n순순히 내게 항복하는 것이 어떠냐?\n&0\n그대에게 저 군대를 제대로 부릴 능력이 있다면\n고려해 보았을지도 모르지.\n하하하!\n&101\n조조, 자네는 조금도 변하지 않았군!')
        vars[40] = 1
    if not vars[41] and stage.isNear(0, 105, True):
        stage.unit(0).setDir(stage.countDir(0, 105))
        stage.unit(105).setDir(stage.countDir(105, 0))
        stage.say('&0\n비켜라! 내 앞을 막지 마라!\n원소는 어디 있느냐!\n&105\n으윽, 주군!\n어서 피하십시오!')
        vars[41] = 1
    if not vars[42] and stage.isNear(0, 108, False):
        stage.unit(0).setDir(stage.countDir(0, 108))
        stage.unit(108).setDir(stage.countDir(108, 0))
        stage.say('&0\n그대가 안량인가?\n참으로 대단한 솜씨로군.\n&108\n……!?\n주장이 직접 나서다니!\n무모한 짓이구나!\n&0\n뛰어난 무장이 있다는 말을 들으면\n이 눈으로 직접 보아야 성이 차거든.\n하하하!\n&108\n너만 죽이면 이 싸움은 끝이다.\n인재라면 사족을 못 쓰는 어리석은 놈!')
        stage.unit(108).setAction(6)
        stage.unit(108).setAction(0)
        stage.unit(0).setAction(2)
        stage.effectSound(31, 1)
        stage.delay(10)
        stage.say('&108\n으음, 놓치고 말았군…….\n&0\n(후우, 겨우 벗어났군.\n허나 저런 무예라면\n보통 사람은 도무지 상대가 되지 않겠어.)')
        vars[42] = 1
    if not vars[43] and stage.isNear(3, 108, False):
        stage.unit(3).setDir(stage.countDir(3, 108))
        stage.unit(108).setDir(stage.countDir(108, 3))
        stage.say('&3\n내가 상대해 주마!\n&108\n죽고 싶어 안달이 났구나.\n덤벼라!')
        fight = stage.startFight(3, 108, 5)
        fight.showUnit(True, '이 관운장이 상대해 주마!\n안량, 네 목은 내가 거두겠다.', 9)
        fight.showUnit(False, '재미있는 농담이구나!\n내가 네 목을 어떻게 베는지 보아라!', 10)
        fight.showStart()
        fight.setAction(True, 8)
        fight.say(True, '내 공격을 받아라!', True)
        fight.setAction(False, 8)
        fight.say(False, '오너라!', True)
        fight.attack2(True, 2, False)
        stage.delay(10)
        fight.setAction(False, 11)
        fight.say(False, '어, 어찌 이런…….\n이럴 수가 있단 말인가!\n으아아!', True)
        fight.death(True)
        fight.setAction(True, 1)
        fight.setAction(True, 2)
        fight.setAction(True, 6)
        fight.say(True, '나 관운장이 적장 안량을 베었다!', True)
        fight.end()
        stage.unit(108).hide(2)
        stage.getItem(139, 0, True, 3)
        if not vars[50] and not vars[51]:
            stage.unit(105).setDir(stage.countDir(105, 101))
            stage.unit(101).setDir(stage.countDir(101, 105))
            stage.say('&105\n주군, 큰일 났습니다!\n안량이 적에게 목숨을 잃었습니다……!\n&101\n뭐라고, 안량이 죽었다고?\n그럴 리가 없다!\n&105\n안량을 죽인 자는 관우라 자칭한 듯합니다…….\n&101\n관우…그 관우 말이냐!?\n그는 유비의 부하일 텐데!')
        stage.unit(261).setAI(1)
        stage.unit(262).setAI(1)
        stage.unit(263).setAI(1)
        stage.unit(283).setAI(1)
        stage.unit(342).setAI(1)
        stage.unit(264).setAI(1)
        stage.unit(265).setAI(1)
        stage.unit(266).setAI(1)
        stage.unit(284).setAI(1)
        stage.unit(350).setAI(1)
        vars[43] = 1
    if not vars[50] and stage.unitStateTest(101, 7, 0, 2):
        stage.say('&101\n으으으.\n어쩔 수 없구나!\n퇴각하라!')
        stage.unit(101).hide(1)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(105, 7, 0, 2):
        stage.say('&105\n주, 주군. 더는 버티지 못하겠습니다.\n퇴각하겠습니다.')
        stage.unit(105).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(108, 7, 0, 2):
        stage.say('&108\n어, 어찌 이런 일이!?\n으아악!')
        stage.unit(108).hide(2)
        stage.getItem(139, 0, True, 1027)
        stage.unit(261).setAI(1)
        stage.unit(262).setAI(1)
        stage.unit(263).setAI(1)
        stage.unit(283).setAI(1)
        stage.unit(264).setAI(1)
        stage.unit(265).setAI(1)
        stage.unit(266).setAI(1)
        stage.unit(284).setAI(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(3, 7, 0, 2):
        stage.say('&3\n이만하면 꽤 도움이 되었겠군.\n이제 먼저 물러가자.')
        stage.unit(3).hide(1)
        vars[53] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('&0\n좋다, 마침내 적을 섬멸했다!\n우리의 승리다!')
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[616] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 원소에게 패했다.')
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
