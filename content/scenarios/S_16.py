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
    stage.say('@dialogue:S_16.scene0.dialogue0001')
    stage.unit(108).setDir(stage.countDir(108, 101))
    stage.unit(108).setAction(7)
    stage.say('@dialogue:S_16.scene0.dialogue0002')
    stage.unit(108).setAction(0)
    stage.unit(105).setDir(stage.countDir(105, 101))
    stage.say('@dialogue:S_16.scene0.dialogue0003')
    stage.unit(101).setDir(2)
    stage.unit(105).setDir(2)
    stage.say('@dialogue:S_16.scene0.dialogue0004')
    stage.unit(0).setDir(stage.countDir(0, 108))
    stage.say('@dialogue:S_16.scene0.dialogue0005')
    stage.unit(212).setDir(stage.countDir(212, 0))
    stage.say('@dialogue:S_16.scene0.dialogue0006')
    stage.unit(212).setDir(stage.countDir(212, 108))
    stage.unit(212).setAction(5)
    stage.say('@dialogue:S_16.scene0.dialogue0007')
    stage.unit(212).setAction(0)
    stage.unit(212).move(16, 7, 0)
    stage.unit(212).setDir(stage.countDir(212, 108))
    stage.unit(108).setDir(stage.countDir(108, 212))
    stage.say('@dialogue:S_16.scene0.dialogue0008')
    stage.attackAction(108, 212, 1)
    stage.delay(10)
    stage.unit(212).setAction(4)
    stage.say('@dialogue:S_16.scene0.dialogue0009')
    stage.unit(212).hide(2)
    stage.unit(267).setDir(stage.countDir(267, 108))
    stage.unit(267).setAction(2)
    stage.say('@dialogue:S_16.scene0.dialogue0010')
    stage.unit(267).move(16, 7, 0)
    stage.unit(267).setDir(stage.countDir(267, 108))
    stage.unit(108).setDir(stage.countDir(108, 267))
    stage.attackAction(267, 108, 2)
    stage.delay(10)
    stage.attackAction(108, 267, 1)
    stage.delay(10)
    stage.unit(267).setAction(4)
    stage.say('@dialogue:S_16.scene0.dialogue0011')
    stage.unit(267).hide(2)
    stage.unit(108).setDir(stage.countDir(108, 0))
    stage.unit(108).setAction(5)
    stage.say('@dialogue:S_16.scene0.dialogue0012')
    stage.unit(108).setAction(0)
    stage.unit(0).setDir(stage.countDir(0, 108))
    stage.say('@dialogue:S_16.scene0.dialogue0013')
    sel = stage.choice('관우를 출전시킨다\n정면 돌파한다', -1)
    if sel == 1:
        stage.unit(0).setAction(2)
        stage.say('@dialogue:S_16.scene0.dialogue0014')
        stage.unit(6, 1).move(10, 14, 3)
        stage.unit(7, 1).move(9, 16, 3)
        vars[2] = 1
    elif sel == 2:
        stage.unit(0).setAction(5)
        stage.say('@dialogue:S_16.scene0.dialogue0015')
        stage.unit(0).setAction(0)
        stage.unit(6, 1).move(16, 10, 0)
        stage.unit(7, 1).move(17, 11, 0)
        stage.unit(108).setAction(7)
        stage.say('@dialogue:S_16.scene0.dialogue0016')
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
            stage.say('@dialogue:S_16.scene1.dialogue0001')
            stage.unit(0).setDir(stage.countDir(0, 3))
            stage.say('@dialogue:S_16.scene1.dialogue0002')
            stage.unit(3).setDir(stage.countDir(3, 108))
        else:
            stage.unit(3).setDir(stage.countDir(3, 0))
            stage.say('@dialogue:S_16.scene1.dialogue0003')
            stage.unit(0).setDir(stage.countDir(0, 3))
            stage.say('@dialogue:S_16.scene1.dialogue0004')
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
        stage.say('@dialogue:S_16.scene1.dialogue0005')
        stage.unit(3).setDir(stage.countDir(3, 0))
        stage.say('@dialogue:S_16.scene1.dialogue0006')
        stage.unit(3).setDir(stage.countDir(3, 108))
        vars[716] = 1
        vars[14] = 1
    if not vars[15] and stage.totalRectUnit(4, 16, 7, 16, 7) == 0 and stage.unitStateTest(108, 7, 150, 0) and stage.round() == 4:
        stage.unit(108).setDir(2)
        stage.unit(108).setAction(5)
        stage.say('@dialogue:S_16.scene1.dialogue0007')
        stage.unit(108).setAction(0)
        vars[15] = 1
    if not vars[20] and stage.isInPos(1025, 9, 2):
        stage.getItem(151, 0, True, 1025)
        vars[20] = 1
    if not vars[40] and stage.isNear(0, 101, True):
        stage.unit(0).setDir(stage.countDir(0, 101))
        stage.unit(101).setDir(stage.countDir(101, 0))
        stage.say('@dialogue:S_16.scene1.dialogue0008')
        vars[40] = 1
    if not vars[41] and stage.isNear(0, 105, True):
        stage.unit(0).setDir(stage.countDir(0, 105))
        stage.unit(105).setDir(stage.countDir(105, 0))
        stage.say('@dialogue:S_16.scene1.dialogue0009')
        vars[41] = 1
    if not vars[42] and stage.isNear(0, 108, False):
        stage.unit(0).setDir(stage.countDir(0, 108))
        stage.unit(108).setDir(stage.countDir(108, 0))
        stage.say('@dialogue:S_16.scene1.dialogue0010')
        stage.unit(108).setAction(6)
        stage.unit(108).setAction(0)
        stage.unit(0).setAction(2)
        stage.effectSound(31, 1)
        stage.delay(10)
        stage.say('@dialogue:S_16.scene1.dialogue0011')
        vars[42] = 1
    if not vars[43] and stage.isNear(3, 108, False):
        stage.unit(3).setDir(stage.countDir(3, 108))
        stage.unit(108).setDir(stage.countDir(108, 3))
        stage.say('@dialogue:S_16.scene1.dialogue0012')
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
            stage.say('@dialogue:S_16.scene1.dialogue0013')
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
        stage.say('@dialogue:S_16.scene1.dialogue0014')
        stage.unit(101).hide(1)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(105, 7, 0, 2):
        stage.say('@dialogue:S_16.scene1.dialogue0015')
        stage.unit(105).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(108, 7, 0, 2):
        stage.say('@dialogue:S_16.scene1.dialogue0016')
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
        stage.say('@dialogue:S_16.scene1.dialogue0017')
        stage.unit(3).hide(1)
        vars[53] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_16.scene1.dialogue0018')
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
