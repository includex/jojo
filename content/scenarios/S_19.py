# Source Generated with Decompyle++
# File: S_19.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20], [
        '(8,1)'])
    stage.nearEvent([
        15,
        101,
        15,
        114,
        15,
        115,
        15,
        113], 1)
    stage.initFight()
    stage.setGlobalData(25, -2, -1, 0, 0, 1)
    stage.createEnemy([
        {
            'id': 101,
            'x': 8,
            'y': 1,
            'lv': 6,
            'i': 0 },
        {
            'id': 258,
            'x': 8,
            'y': 2,
            'lv': 0,
            'i': 1 },
        {
            'id': 259,
            'x': 7,
            'y': 1,
            'lv': 0,
            'i': 2 },
        {
            'id': 260,
            'x': 9,
            'y': 1,
            'lv': 0,
            'i': 3 },
        {
            'id': 282,
            'x': 7,
            'y': 0,
            'lv': 0,
            'i': 4 },
        {
            'id': 283,
            'x': 9,
            'y': 0,
            'lv': 0,
            'i': 5 },
        {
            'id': 114,
            'x': 10,
            'y': 3,
            'lv': 4,
            'i': 6 },
        {
            'id': 264,
            'x': 10,
            'y': 4,
            'lv': 0,
            'i': 7 },
        {
            'id': 211,
            'x': 9,
            'y': 3,
            'lv': 0,
            'i': 8 },
        {
            'id': 212,
            'x': 11,
            'y': 3,
            'lv': 0,
            'i': 9 },
        {
            'id': 234,
            'x': 9,
            'y': 2,
            'lv': 0,
            'i': 10 },
        {
            'id': 235,
            'x': 11,
            'y': 2,
            'lv': 0,
            'i': 11 },
        {
            'id': 113,
            'x': 7,
            'y': 5,
            'lv': 4,
            'ai': 6,
            'targetX': 9,
            'targetY': 10,
            'i': 12 },
        {
            'id': 261,
            'x': 7,
            'y': 6,
            'lv': 0,
            'ai': 1,
            'i': 13 },
        {
            'id': 262,
            'x': 6,
            'y': 5,
            'lv': 0,
            'ai': 1,
            'i': 14 },
        {
            'id': 263,
            'x': 8,
            'y': 5,
            'lv': 0,
            'ai': 1,
            'i': 15 },
        {
            'id': 284,
            'x': 6,
            'y': 4,
            'lv': 0,
            'ai': 1,
            'i': 16 },
        {
            'id': 285,
            'x': 8,
            'y': 4,
            'lv': 0,
            'ai': 1,
            'i': 17 },
        {
            'id': 115,
            'x': 9,
            'y': 7,
            'lv': 4,
            'ai': 6,
            'targetX': 9,
            'targetY': 10,
            'i': 18 },
        {
            'id': 265,
            'x': 9,
            'y': 8,
            'lv': 0,
            'ai': 1,
            'i': 19 },
        {
            'id': 214,
            'x': 8,
            'y': 7,
            'lv': 0,
            'ai': 1,
            'i': 20 },
        {
            'id': 215,
            'x': 10,
            'y': 7,
            'lv': 0,
            'ai': 1,
            'i': 21 },
        {
            'id': 236,
            'x': 8,
            'y': 6,
            'lv': 0,
            'ai': 1,
            'i': 22 },
        {
            'id': 237,
            'x': 10,
            'y': 6,
            'lv': 0,
            'ai': 1,
            'i': 23 }])
    stage.createMine([
        {
            'idx': 0,
            'x': 8,
            'y': 32,
            'dir': 0 },
        {
            'idx': 1,
            'x': 9,
            'y': 32,
            'dir': 0 },
        {
            'idx': 2,
            'x': 7,
            'y': 32,
            'dir': 0 },
        {
            'idx': 3,
            'x': 7,
            'y': 10,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 4,
            'x': 12,
            'y': 14,
            'dir': 3,
            'hide': 1 },
        {
            'idx': 5,
            'x': 7,
            'y': 19,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 6,
            'x': 11,
            'y': 24,
            'dir': 3,
            'hide': 1 },
        {
            'idx': 7,
            'x': 6,
            'y': 25,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 8,
            'x': 7,
            'y': 11,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 9,
            'x': 12,
            'y': 15,
            'dir': 3,
            'hide': 1 },
        {
            'idx': 10,
            'x': 7,
            'y': 20,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 11,
            'x': 11,
            'y': 25,
            'dir': 3,
            'hide': 1 },
        {
            'idx': 12,
            'x': 6,
            'y': 26,
            'dir': 1,
            'hide': 1 },
        {
            'idx': 13,
            'dir': 0 },
        {
            'idx': 14,
            'dir': 0 }])
    stage.setEnemyEquip(101, -1, 0, -1, 0, 4)
    stage.setEnemyEquip(113, -1, 0, 12, 0, -1)
    stage.loadBg(19)
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab25')
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
            goto('lab25')
    stage.draw()
    stage.bgSound(1)
    stage.say('@dialogue:S_19.scene0.dialogue0001')
    stage.unit(113).setDir(stage.countDir(113, 101))
    stage.say('@dialogue:S_19.scene0.dialogue0002')
    stage.unit(113).setDir(2)
    stage.unit(113).setAction(5)
    stage.say('@dialogue:S_19.scene0.dialogue0003')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:S_19.scene0.dialogue0004')
    stage.unit(115).setDir(stage.countDir(115, 114))
    stage.say('@dialogue:S_19.scene0.dialogue0005')
    stage.unit(113).setDir(stage.countDir(113, 115))
    stage.unit(113).setAction(6)
    stage.say('@dialogue:S_19.scene0.dialogue0006')
    stage.unit(113).setAction(0)
    stage.say('@dialogue:S_19.scene0.dialogue0007')
    stage.unit(101).setDir(stage.countDir(101, 113))
    stage.say('@dialogue:S_19.scene0.dialogue0008')
    stage.unit(113).setDir(stage.countDir(113, 101))
    stage.say('@dialogue:S_19.scene0.dialogue0009')
    stage.unit(0).setDir(stage.countDir(0, 21))
    stage.say('@dialogue:S_19.scene0.dialogue0010')
    stage.unit(21).setDir(stage.countDir(21, 0))
    stage.say('@dialogue:S_19.scene0.dialogue0011')
    stage.unit(21).setDir(stage.countDir(21, 15))
    stage.say('@dialogue:S_19.scene0.dialogue0012')
    stage.unit(15).setDir(stage.countDir(15, 21))
    stage.say('@dialogue:S_19.scene0.dialogue0013')
    stage.unit(0).setDir(stage.countDir(0, 101))
    stage.say('@dialogue:S_19.scene0.dialogue0014')
    stage.unit(101).setDir(2)
    stage.unit(114).setDir(2)
    stage.unit(113).setDir(2)
    stage.unit(115).setDir(2)
    stage.unit(0).setDir(0)
    stage.unit(21).setDir(0)
    stage.unit(15).setDir(0)
    stage.bgSound(2)
    stage.setWinCondition('승리 조건\n1. 모든 적을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 25턴을 초과한다.')
    stage.showWinCondition('모든 적을 섬멸하라!')
    stage.unit(15).move(8, 25, 0)
    stage.say('@dialogue:S_19.scene0.dialogue0015')
    stage.unit(15).move(9, 18, 0)
    stage.say('@dialogue:S_19.scene0.dialogue0016')
    stage.unit(15).move(9, 11, 0)
    stage.unit(15).setAction(5)
    stage.say('@dialogue:S_19.scene0.dialogue0017')
    stage.unit(15).setAction(0)
    stage.say('@dialogue:S_19.scene0.dialogue0018')
    stage.unit(15).move(9, 10, 3)
    stage.say('@dialogue:S_19.scene0.dialogue0019')
    stage.unit(15).move(9, 9, 0)
    stage.say('@dialogue:S_19.scene0.dialogue0020')
    stage.say('@dialogue:S_19.scene0.dialogue0021')
    stage.unit(115).setAction(6)
    stage.say('@dialogue:S_19.scene0.dialogue0022')
    stage.unit(115).setAction(0)
    stage.say('@dialogue:S_19.scene0.dialogue0023')
    stage.unit(15).setDir(2)
    stage.say('@dialogue:S_19.scene0.dialogue0024')
    stage.startOper()
    vars[519] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[11] and stage.curCamp() == 2 and stage.round() == 2:
        if not vars[2] and not vars[50] and not vars[51]:
            stage.unit(101).setAction(4)
            stage.say('@dialogue:S_19.scene1.dialogue0001')
        stage.unit(101).setAI(1)
        stage.unit(114).setAI(1)
        stage.unit(113).setAI(1)
        stage.unit(115).setAI(1)
        stage.unit(258).setAI(1)
        stage.unit(259).setAI(1)
        stage.unit(260).setAI(1)
        stage.unit(282).setAI(1)
        stage.unit(283).setAI(1)
        stage.unit(264).setAI(1)
        stage.unit(211).setAI(1)
        stage.unit(212).setAI(1)
        stage.unit(234).setAI(1)
        stage.unit(235).setAI(1)
        vars[11] = 1
    if not vars[2] and not vars[12] and stage.unitStateTest(15, 7, 1, 0) and stage.round() >= 1 and stage.unitStateTest(15, 7, 100, 1):
        stage.unit(15).setAction(4)
        stage.say('@dialogue:S_19.scene1.dialogue0002')
        if not vars[52]:
            stage.unit(115).setDir(stage.countDir(115, 15))
            stage.unit(115).setAction(5)
            stage.say('@dialogue:S_19.scene1.dialogue0003')
            stage.unit(115).setAction(0)
        if not vars[53]:
            stage.unit(113).setDir(stage.countDir(113, 15))
            stage.unit(113).setAction(7)
            stage.say('@dialogue:S_19.scene1.dialogue0004')
            stage.unit(113).setAction(0)
        stage.unit(0).setAction(2)
        stage.say('@dialogue:S_19.scene1.dialogue0005')
        sel = stage.choice('움직이지 않는다\n허저를 구원한다', -1)
        if sel == 1:
            stage.unit(0).setDir(2)
            stage.say('@dialogue:S_19.scene1.dialogue0006')
            stage.unit(21).setDir(stage.countDir(21, 0))
            stage.unit(21).setAction(2)
            stage.say('@dialogue:S_19.scene1.dialogue0007')
            stage.unit(0).setDir(stage.countDir(0, 15))
            stage.unit(15).setDir(stage.countDir(15, 101))
            stage.unit(15).setAction(7)
            stage.say('@dialogue:S_19.scene1.dialogue0008')
            stage.unit(15).setAction(0)
            if not vars[50]:
                stage.unit(101).setDir(stage.countDir(101, 0))
                stage.say('@dialogue:S_19.scene1.dialogue0009')
            elif sel == 2:
                stage.unit(0).setAction(5)
                stage.say('@dialogue:S_19.scene1.dialogue0010')
                stage.unit(0).setAction(0)
                stage.unit(21).setAction(4)
                stage.say('@dialogue:S_19.scene1.dialogue0011')
                stage.unit(15).setDir(stage.countDir(15, 0))
                stage.say('@dialogue:S_19.scene1.dialogue0012')
                stage.unit(7, 1).show()
                stage.unit(12, 1).show()
                stage.setUnitStatus([
                    {
                        'hStatus': [
                            9],
                        'x1': 6,
                        'y1': 25,
                        'x2': 8,
                        'y2': 27,
                        'camp': 2 }])
                stage.unit(6, 1).show()
                stage.unit(11, 1).show()
                stage.setUnitStatus([
                    {
                        'hStatus': [
                            9],
                        'x1': 9,
                        'y1': 23,
                        'x2': 11,
                        'y2': 25,
                        'camp': 2 }])
                stage.unit(5, 1).show()
                stage.unit(10, 1).show()
                stage.setUnitStatus([
                    {
                        'hStatus': [
                            9],
                        'x1': 7,
                        'y1': 18,
                        'x2': 9,
                        'y2': 20,
                        'camp': 2 }])
                stage.unit(4, 1).show()
                stage.unit(9, 1).show()
                stage.setUnitStatus([
                    {
                        'hStatus': [
                            9],
                        'x1': 10,
                        'y1': 13,
                        'x2': 12,
                        'y2': 15,
                        'camp': 2 }])
                stage.unit(3, 1).show()
                stage.unit(8, 1).show()
                stage.setUnitStatus([
                    {
                        'hStatus': [
                            9],
                        'x1': 7,
                        'y1': 9,
                        'x2': 9,
                        'y2': 11,
                        'camp': 2 }])
                stage.unit(1).setDir(stage.countDir(1, 15))
                stage.say('@dialogue:S_19.scene1.dialogue0013')
                stage.unit(2).setDir(stage.countDir(2, 15))
                stage.say('@dialogue:S_19.scene1.dialogue0014')
                stage.unit(11).setDir(stage.countDir(11, 15))
                stage.say('@dialogue:S_19.scene1.dialogue0015')
                stage.unit(10).setDir(stage.countDir(10, 15))
                stage.say('@dialogue:S_19.scene1.dialogue0016')
                stage.unit(6).setDir(stage.countDir(6, 15))
                stage.say('@dialogue:S_19.scene1.dialogue0017')
                stage.unit(7).setDir(stage.countDir(7, 15))
                stage.say('@dialogue:S_19.scene1.dialogue0018')
                stage.unit(13).setDir(stage.countDir(13, 15))
                stage.say('@dialogue:S_19.scene1.dialogue0019')
                stage.unit(14).setDir(stage.countDir(14, 15))
                stage.say('@dialogue:S_19.scene1.dialogue0020')
                if not vars[50]:
                    stage.unit(101).setDir(stage.countDir(101, 0))
                    stage.say('@dialogue:S_19.scene1.dialogue0021')
                vars[2] = 1
        vars[12] = 1
    if vars[3] and stage.round() >= 1 and stage.curCamp() == 0:
        stage.setAI(0, 0, 19, 39, 2, 1)
    if vars[3] and stage.round() >= 1 and stage.curCamp() == 2:
        stage.setAI(0, 0, 19, 39, 2, 6, -1, 8, 0)
        stage.setRectUnitHide(0, 0, 19, 1, 2)
    if vars[3] and stage.round() >= 1 and stage.isInRect(114, 0, 0, 19, 1):
        stage.unit(114).setDir(2)
        stage.say('@dialogue:S_19.scene1.dialogue0022')
        stage.unit(114).hide()
    if vars[3] and stage.round() >= 1 and stage.isInRect(113, 0, 0, 19, 1):
        stage.unit(113).setDir(2)
        stage.unit(113).setAction(7)
        stage.say('@dialogue:S_19.scene1.dialogue0023')
        stage.unit(113).setAction(0)
        stage.unit(113).hide()
    if vars[3] and stage.round() >= 1 and stage.isInRect(115, 0, 0, 19, 1):
        stage.unit(115).setDir(2)
        stage.unit(115).setAction(4)
        stage.say('@dialogue:S_19.scene1.dialogue0024')
        stage.unit(115).hide()
    if vars[3] and stage.round() >= 1 and stage.isInRect(101, 0, 0, 19, 1):
        stage.unit(101).setDir(2)
        stage.unit(101).setAction(9)
        stage.say('@dialogue:S_19.scene1.dialogue0025')
        stage.unit(101).hide()
    if not vars[20] and stage.isInPos(1025, 8, 1):
        stage.getItem(150, 0, True, 1025)
        vars[20] = 1
    if not vars[2] and not vars[6] and stage.totalRectUnit(0, 0, 30, 19, 39) < 2:
        stage.unit(7, 1).show()
        stage.unit(12, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 6,
                'y1': 25,
                'x2': 8,
                'y2': 27,
                'camp': 2 }])
        stage.unit(6, 1).show()
        stage.unit(11, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 9,
                'y1': 23,
                'x2': 11,
                'y2': 25,
                'camp': 2 }])
        stage.unit(5, 1).show()
        stage.unit(10, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 7,
                'y1': 18,
                'x2': 9,
                'y2': 20,
                'camp': 2 }])
        stage.unit(4, 1).show()
        stage.unit(9, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 10,
                'y1': 13,
                'x2': 12,
                'y2': 15,
                'camp': 2 }])
        stage.unit(3, 1).show()
        stage.unit(8, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 7,
                'y1': 9,
                'x2': 9,
                'y2': 11,
                'camp': 2 }])
        stage.say('@dialogue:S_19.scene1.dialogue0026')
        if not vars[50]:
            stage.unit(101).setAction(5)
            stage.say('@dialogue:S_19.scene1.dialogue0027')
            stage.unit(101).setAction(0)
            stage.say('@dialogue:S_19.scene1.dialogue0028')
        vars[2] = 1
        vars[6] = 1
    if not vars[2] and not vars[30] and stage.isInRect(15, 0, 29, 19, 39):
        stage.unit(15).setAction(9)
        stage.say('@dialogue:S_19.scene1.dialogue0029')
        stage.unit(0).setAction(5)
        stage.say('@dialogue:S_19.scene1.dialogue0030')
        stage.unit(0).setAction(0)
        stage.unit(3, 1).show()
        stage.unit(8, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 6,
                'y1': 9,
                'x2': 10,
                'y2': 12,
                'camp': 2 }])
        stage.unit(101).setDir(3)
        stage.unit(101).setDir(1)
        stage.unit(101).setDir(stage.countDir(101, 0))
        stage.unit(101).setAction(5)
        stage.say('@dialogue:S_19.scene1.dialogue0031')
        stage.unit(101).setAction(0)
        stage.unit(6, 1).show()
        stage.unit(11, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 9,
                'y1': 23,
                'x2': 12,
                'y2': 26,
                'camp': 2 }])
        stage.unit(7, 1).show()
        stage.unit(12, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 5,
                'y1': 24,
                'x2': 8,
                'y2': 27,
                'camp': 2 }])
        stage.unit(115).setAction(10)
        stage.say('@dialogue:S_19.scene1.dialogue0032')
        stage.unit(4, 1).show()
        stage.unit(9, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 9,
                'y1': 13,
                'x2': 13,
                'y2': 16,
                'camp': 2 }])
        stage.unit(114).setAction(8)
        stage.say('@dialogue:S_19.scene1.dialogue0033')
        stage.unit(5, 1).show()
        stage.unit(10, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 6,
                'y1': 18,
                'x2': 10,
                'y2': 21,
                'camp': 2 }])
        stage.unit(113).setAction(10)
        stage.say('@dialogue:S_19.scene1.dialogue0034')
        stage.unit(101).setDir(0)
        stage.unit(101).setAction(6)
        stage.say('@dialogue:S_19.scene1.dialogue0035')
        stage.unit(101).setAction(0)
        stage.setAI(0, 0, 19, 39, 2, 3, 101, 0, 0)
        stage.unit(101).setAI(4, -1, 8, 0)
        stage.unit(1).setDir(stage.countDir(1, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0036')
        stage.unit(2).setDir(stage.countDir(2, 101))
        stage.unit(2).setAction(5)
        stage.say('@dialogue:S_19.scene1.dialogue0037')
        stage.unit(2).setAction(0)
        stage.unit(11).setDir(stage.countDir(11, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0038')
        stage.unit(10).setDir(stage.countDir(10, 101))
        stage.unit(10).setAction(6)
        stage.say('@dialogue:S_19.scene1.dialogue0039')
        stage.unit(10).setAction(0)
        stage.unit(6).setDir(stage.countDir(6, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0040')
        stage.unit(7).setDir(stage.countDir(7, 101))
        stage.unit(7).setAction(5)
        stage.say('@dialogue:S_19.scene1.dialogue0041')
        stage.unit(7).setAction(0)
        stage.unit(13).setDir(stage.countDir(13, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0042')
        stage.unit(14).setDir(stage.countDir(14, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0043')
        vars[2] = 1
        vars[3] = 1
        vars[30] = 1
    if not vars[2] and not vars[31] and stage.isInRect(1026, 0, 29, 19, 39):
        stage.unit(0).setAction(5)
        stage.say('@dialogue:S_19.scene1.dialogue0044')
        stage.unit(0).setAction(0)
        stage.unit(3, 1).show()
        stage.unit(8, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 6,
                'y1': 9,
                'x2': 10,
                'y2': 12,
                'camp': 2 }])
        stage.unit(101).setDir(3)
        stage.unit(101).setDir(1)
        stage.unit(101).setDir(stage.countDir(101, 0))
        stage.unit(101).setAction(5)
        stage.say('@dialogue:S_19.scene1.dialogue0045')
        stage.unit(101).setAction(0)
        stage.unit(6, 1).show()
        stage.unit(11, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 9,
                'y1': 23,
                'x2': 12,
                'y2': 26,
                'camp': 2 }])
        stage.unit(7, 1).show()
        stage.unit(12, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 5,
                'y1': 24,
                'x2': 8,
                'y2': 27,
                'camp': 2 }])
        stage.unit(115).setAction(10)
        stage.say('@dialogue:S_19.scene1.dialogue0046')
        stage.unit(4, 1).show()
        stage.unit(9, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 9,
                'y1': 13,
                'x2': 13,
                'y2': 16,
                'camp': 2 }])
        stage.unit(114).setAction(8)
        stage.say('@dialogue:S_19.scene1.dialogue0047')
        stage.unit(5, 1).show()
        stage.unit(10, 1).show()
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 6,
                'y1': 18,
                'x2': 10,
                'y2': 21,
                'camp': 2 }])
        stage.unit(113).setAction(10)
        stage.say('@dialogue:S_19.scene1.dialogue0048')
        stage.unit(101).setDir(0)
        stage.unit(101).setAction(6)
        stage.say('@dialogue:S_19.scene1.dialogue0049')
        stage.unit(101).setAction(0)
        stage.setAI(0, 0, 19, 39, 2, 3, 101, 0, 0)
        stage.unit(101).setAI(4, -1, 8, 0)
        stage.unit(1).setDir(stage.countDir(1, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0050')
        stage.unit(2).setDir(stage.countDir(2, 101))
        stage.unit(2).setAction(5)
        stage.say('@dialogue:S_19.scene1.dialogue0051')
        stage.unit(2).setAction(0)
        stage.unit(11).setDir(stage.countDir(11, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0052')
        stage.unit(10).setDir(stage.countDir(10, 101))
        stage.unit(10).setAction(6)
        stage.say('@dialogue:S_19.scene1.dialogue0053')
        stage.unit(10).setAction(0)
        stage.unit(6).setDir(stage.countDir(6, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0054')
        stage.unit(7).setDir(stage.countDir(7, 101))
        stage.unit(7).setAction(5)
        stage.say('@dialogue:S_19.scene1.dialogue0055')
        stage.unit(7).setAction(0)
        stage.unit(13).setDir(stage.countDir(13, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0056')
        stage.unit(14).setDir(stage.countDir(14, 101))
        stage.say('@dialogue:S_19.scene1.dialogue0057')
        vars[2] = 1
        vars[3] = 1
        vars[31] = 1
    if not vars[40] and stage.isNear(15, 101, True) and stage.unitStateTest(15, 7, 100, 0):
        stage.unit(15).setDir(stage.countDir(15, 101))
        stage.unit(101).setDir(stage.countDir(101, 15))
        stage.say('@dialogue:S_19.scene1.dialogue0058')
        vars[40] = 1
    if not vars[2] and not vars[41] and stage.isNear(15, 114, True) and stage.unitStateTest(15, 7, 100, 0):
        stage.unit(15).setDir(stage.countDir(15, 114))
        stage.unit(114).setDir(stage.countDir(114, 15))
        stage.say('@dialogue:S_19.scene1.dialogue0059')
        vars[41] = 1
    if not vars[2] and not vars[42] and stage.isNear(15, 115, True) and stage.unitStateTest(15, 7, 100, 0):
        stage.unit(15).setDir(stage.countDir(15, 115))
        stage.unit(115).setDir(stage.countDir(115, 15))
        stage.say('@dialogue:S_19.scene1.dialogue0060')
        if not vars[2] and not vars[12]:
            if not vars[52]:
                stage.unit(115).setDir(stage.countDir(115, 15))
                stage.unit(115).setAction(5)
                stage.say('@dialogue:S_19.scene1.dialogue0061')
                stage.unit(115).setAction(0)
            if not vars[53]:
                stage.unit(113).setDir(stage.countDir(113, 15))
                stage.unit(113).setAction(7)
                stage.say('@dialogue:S_19.scene1.dialogue0062')
                stage.unit(113).setAction(0)
            stage.unit(0).setAction(2)
            stage.say('@dialogue:S_19.scene1.dialogue0063')
            sel = stage.choice('움직이지 않는다\n허저를 구원한다', -1)
            if sel == 1:
                stage.unit(0).setDir(2)
                stage.say('@dialogue:S_19.scene1.dialogue0064')
                stage.unit(21).setDir(stage.countDir(21, 0))
                stage.unit(21).setAction(2)
                stage.say('@dialogue:S_19.scene1.dialogue0065')
                stage.unit(0).setDir(stage.countDir(0, 15))
                stage.unit(15).setDir(stage.countDir(15, 101))
                stage.unit(15).setAction(7)
                stage.say('@dialogue:S_19.scene1.dialogue0066')
                stage.unit(15).setAction(0)
                if not vars[50]:
                    stage.unit(101).setDir(stage.countDir(101, 0))
                    stage.say('@dialogue:S_19.scene1.dialogue0067')
                elif sel == 2:
                    stage.unit(0).setAction(5)
                    stage.say('@dialogue:S_19.scene1.dialogue0068')
                    stage.unit(0).setAction(0)
                    stage.unit(21).setAction(4)
                    stage.say('@dialogue:S_19.scene1.dialogue0069')
                    stage.unit(15).setDir(stage.countDir(15, 0))
                    stage.say('@dialogue:S_19.scene1.dialogue0070')
                    stage.unit(7, 1).show()
                    stage.unit(12, 1).show()
                    stage.setUnitStatus([
                        {
                            'hStatus': [
                                9],
                            'x1': 6,
                            'y1': 25,
                            'x2': 8,
                            'y2': 27,
                            'camp': 2 }])
                    stage.unit(6, 1).show()
                    stage.unit(11, 1).show()
                    stage.setUnitStatus([
                        {
                            'hStatus': [
                                9],
                            'x1': 9,
                            'y1': 23,
                            'x2': 11,
                            'y2': 25,
                            'camp': 2 }])
                    stage.unit(5, 1).show()
                    stage.unit(10, 1).show()
                    stage.setUnitStatus([
                        {
                            'hStatus': [
                                9],
                            'x1': 7,
                            'y1': 18,
                            'x2': 9,
                            'y2': 20,
                            'camp': 2 }])
                    stage.unit(4, 1).show()
                    stage.unit(9, 1).show()
                    stage.setUnitStatus([
                        {
                            'hStatus': [
                                9],
                            'x1': 10,
                            'y1': 13,
                            'x2': 12,
                            'y2': 15,
                            'camp': 2 }])
                    stage.unit(3, 1).show()
                    stage.unit(8, 1).show()
                    stage.setUnitStatus([
                        {
                            'hStatus': [
                                9],
                            'x1': 7,
                            'y1': 9,
                            'x2': 9,
                            'y2': 11,
                            'camp': 2 }])
                    stage.unit(1).setDir(stage.countDir(1, 15))
                    stage.say('@dialogue:S_19.scene1.dialogue0071')
                    stage.unit(2).setDir(stage.countDir(2, 15))
                    stage.say('@dialogue:S_19.scene1.dialogue0072')
                    stage.unit(11).setDir(stage.countDir(11, 15))
                    stage.say('@dialogue:S_19.scene1.dialogue0073')
                    stage.unit(10).setDir(stage.countDir(10, 15))
                    stage.say('@dialogue:S_19.scene1.dialogue0074')
                    stage.unit(6).setDir(stage.countDir(6, 15))
                    stage.say('@dialogue:S_19.scene1.dialogue0075')
                    stage.unit(7).setDir(stage.countDir(7, 15))
                    stage.say('@dialogue:S_19.scene1.dialogue0076')
                    stage.unit(13).setDir(stage.countDir(13, 15))
                    stage.say('@dialogue:S_19.scene1.dialogue0077')
                    stage.unit(14).setDir(stage.countDir(14, 15))
                    stage.say('@dialogue:S_19.scene1.dialogue0078')
                    if not vars[50]:
                        stage.unit(101).setDir(stage.countDir(101, 0))
                        stage.say('@dialogue:S_19.scene1.dialogue0079')
                    vars[2] = 1
            vars[12] = 1
        vars[42] = 1
    if not vars[2] and not vars[43] and stage.isNear(15, 113, True) and stage.unitStateTest(15, 7, 100, 0):
        stage.unit(15).setDir(stage.countDir(15, 113))
        stage.unit(113).setDir(stage.countDir(113, 15))
        stage.say('@dialogue:S_19.scene1.dialogue0080')
        vars[43] = 1
    if not vars[50] and stage.unitStateTest(101, 7, 0, 2):
        stage.say('@dialogue:S_19.scene1.dialogue0081')
        stage.unit(101).hide(1)
        stage.getItem(111, 0, True, 1027)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(114, 7, 0, 2):
        stage.say('@dialogue:S_19.scene1.dialogue0082')
        stage.unit(114).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(115, 7, 0, 2):
        stage.say('@dialogue:S_19.scene1.dialogue0083')
        stage.unit(115).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(113, 7, 0, 2):
        stage.say('@dialogue:S_19.scene1.dialogue0084')
        stage.unit(113).hide(1)
        vars[53] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        if vars[3]:
            stage.say('@dialogue:S_19.scene1.dialogue0085')
            stage.reward()
        else:
            stage.say('@dialogue:S_19.scene1.dialogue0086')
            stage.reward(0, [
                160,
                -1])
        stage.end()
        vars[0] = 1
        vars[619] = 1
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
