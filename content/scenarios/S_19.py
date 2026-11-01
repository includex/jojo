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
    stage.say('&101\n조조 때문에 숱한 고초를 겪었지만\n이번에야말로 놈을 궁지에 몰아넣었다!\n&101\n이것이 조조와 벌이는 마지막 싸움이다.\n반드시 조조를 없애라.')
    stage.unit(113).setDir(stage.countDir(113, 101))
    stage.say('&113\n하하하.\n아버님, 반드시 조조의 목을 가져오겠습니다.')
    stage.unit(113).setDir(2)
    stage.unit(113).setAction(5)
    stage.say('&113\n전군 돌격하라!')
    stage.unit(113).setAction(0)
    stage.say('&114\n형님, 잠깐만 기다리십시오!\n아무래도 수상합니다.\n&114\n우리가 이렇게 쉽게……\n조조를 가둘 수 있을 리 없습니다.\n매복이 있는 것 아닐까요!\n&113\n어찌 그리 겁이 많으냐!\n그럼 너는 거기서 대기하거라!')
    stage.unit(115).setDir(stage.countDir(115, 114))
    stage.say('&115\n큰형님, 둘째 형님의 말씀이 옳은 듯합니다.\n우선 상황을 지켜보시지요…….')
    stage.unit(113).setDir(stage.countDir(113, 115))
    stage.unit(113).setAction(6)
    stage.say('&113\n뭐라고!\n입 다물어라!')
    stage.unit(113).setAction(0)
    stage.say('&115\n……。')
    stage.unit(101).setDir(stage.countDir(101, 113))
    stage.say('&101\n원담, 진정하라.\n원희의 생각이 옳다.\n&101\n조조는 틀림없이 복병을 숨겨 두었을 것이다.')
    stage.unit(113).setDir(stage.countDir(113, 101))
    stage.say('&113\n그럼 진군하지 않겠다는 말씀이십니까?\n&101\n진군하되 경계를 철저히 하라는 것이다.\n경솔하게 달려들지 마라!')
    stage.unit(0).setDir(stage.countDir(0, 21))
    stage.say('&0\n마침내 결전이군.\n정욱, 준비는 어떠한가?')
    stage.unit(21).setDir(stage.countDir(21, 0))
    stage.say('&21\n복병은 이미 배치했습니다.\n원소를 유인하기만 하면 됩니다.')
    stage.unit(21).setDir(stage.countDir(21, 15))
    stage.say('&21\n성패는 모두 허저 장군께 달렸습니다.\n잘 부탁드립니다.')
    stage.unit(15).setDir(stage.countDir(15, 21))
    stage.say('&15\n제게 맡기십시오.\n&21\n주군과 저는 여기에 남겠습니다.\n저희가 섣불리 움직이면\n매복한 아군의 사기가 흔들릴 것입니다.')
    stage.unit(0).setDir(stage.countDir(0, 101))
    stage.say('&0\n여기서 원소를 끝장내겠다.\n다시는 천하를 꿈꾸지 못하게 하리라.')
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
    stage.say('&15\n(앞의 기병대는 원상이겠군.)')
    stage.unit(15).move(9, 18, 0)
    stage.say('&15\n(원상은 겁쟁이고 원담은 생각이 없지.\n원담이라면 유인하기 쉽겠지만…….)')
    stage.unit(15).move(9, 11, 0)
    stage.unit(15).setAction(5)
    stage.say('&15\n원담은 어디 있느냐!\n무예가 뛰어나다고 큰소리친다지?\n이 허저가 두렵지 않거든 덤벼라!')
    stage.unit(15).setAction(0)
    stage.say('&115\n(허저의 무예는\n조조군에서도 손꼽히는 수준이겠지!?)\n항복하러 온 것이냐?')
    stage.unit(15).move(9, 10, 3)
    stage.say('&15\n너 같은 겁쟁이 원상을 부른 게 아니다!\n원담과 이야기하러 왔으니 끼어들지 마라!\n&115\n뭐, 뭐라고!\n무슨 소리냐?!')
    stage.unit(15).move(9, 9, 0)
    stage.say('&15\n내가 뭐라고 했더라?\n겁쟁이라고 했던가!')
    stage.say('&115\n용서할 수 없다!\n네놈을 죽이겠다!\n&15\n뭐, 뭐라고!?\n기다려라! 나는 원담을 만나러……')
    stage.unit(115).setAction(6)
    stage.say('&115\n돌격하라!')
    stage.unit(115).setAction(0)
    stage.say('&113\n오! 역시 내 아우답구나.\n좋다, 나도 뒤따르겠다!')
    stage.unit(15).setDir(2)
    stage.say('&15\n(오, 생각보다 빨리 걸려들었군!\n좋아. 어쨌든 첫 계획은 성공이야.\n이제 달아나면 되겠지!)')
    stage.startOper()
    vars[519] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[11] and stage.curCamp() == 2 and stage.round() == 2:
        if not vars[2] and not vars[50] and not vars[51]:
            stage.unit(101).setAction(4)
            stage.say('&101\n이런! 그토록 일러 두었거늘…….\n원희! 어서 원상과 원담을 막아라!\n이대로 가면 적이 바라는 대로 될 뿐이다!\n&114\n예, 아버님.\n알겠습니다!')
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
        stage.say('&15\n아, 아, 아……\n크, 큰일이다……')
        if not vars[52]:
            stage.unit(115).setDir(stage.countDir(115, 15))
            stage.unit(115).setAction(5)
            stage.say('&115\n단숨에 허저를 죽이고\n그대로 조조의 본진까지 밀고 들어갈 수도 있겠군!')
            stage.unit(115).setAction(0)
        if not vars[53]:
            stage.unit(113).setDir(stage.countDir(113, 15))
            stage.unit(113).setAction(7)
            stage.say('&113\n조조가 우리를 모욕한 죄는\n너와 조조의 목으로 갚게 하겠다!')
            stage.unit(113).setAction(0)
        stage.unit(0).setAction(2)
        stage.say('&0\n(허, 허저!\n이대로라면 허저가 위험하다.\n어찌해야 하나……)\n&21\n(주군, 너무도 잔인한 처사일지 모르나\n허저를 버리는 한이 있더라도\n섣불리 움직여 적의 경계를 사서는 안 됩니다.)')
        sel = stage.choice('움직이지 않는다\n허저를 구원한다', -1)
        if sel == 1:
            stage.unit(0).setDir(2)
            stage.say('&0\n(……허저, 내 그대에게 면목이 없구나…….)')
            stage.unit(21).setDir(stage.countDir(21, 0))
            stage.unit(21).setAction(2)
            stage.say('&21\n(주군…… 참으셔야 합니다.)')
            stage.unit(0).setDir(stage.countDir(0, 15))
            stage.unit(15).setDir(stage.countDir(15, 101))
            stage.unit(15).setAction(7)
            stage.say('&15\n흥! 별것도 아니군. 아직 버틸 만하다!\n배짱이 있다면 덤벼라!\n아얏, 아프잖아!')
            stage.unit(15).setAction(0)
            if not vars[50]:
                stage.unit(101).setDir(stage.countDir(101, 0))
                stage.say('&101\n(조조 그 늙은 여우가 허저마저 버리려 한다고…….\n정말 매복이 없는 건가……?)')
            elif sel == 2:
                stage.unit(0).setAction(5)
                stage.say('&0\n어서 허저를 구하라!\n서둘러라! 전군 돌격하라!')
                stage.unit(0).setAction(0)
                stage.unit(21).setAction(4)
                stage.say('&21\n아아!')
                stage.unit(15).setDir(stage.countDir(15, 0))
                stage.say('&15\n주, 주군!\n아직 때가 아닙니다……')
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
                stage.say('&1\n허저, 그 녀석이 실패했군!')
                stage.unit(2).setDir(stage.countDir(2, 15))
                stage.say('&2\n적이 완전히 속지는 않았군.\n곤란하게 되었어!')
                stage.unit(11).setDir(stage.countDir(11, 15))
                stage.say('&11\n서둘러 허저 장군을 구해야 한다.\n어서!')
                stage.unit(10).setDir(stage.countDir(10, 15))
                stage.say('&10\n모두 돌격하라!\n나를 따르라!')
                stage.unit(6).setDir(stage.countDir(6, 15))
                stage.say('&6\n정말 못 말리는 사람이군…….')
                stage.unit(7).setDir(stage.countDir(7, 15))
                stage.say('&7\n안 된다!\n이래서는 매복의 효과가 사라진다……')
                stage.unit(13).setDir(stage.countDir(13, 15))
                stage.say('&13\n이렇게 된 이상 어쩔 수 없군……')
                stage.unit(14).setDir(stage.countDir(14, 15))
                stage.say('&14\n제가 갑니다!\n허저 장군, 기다리십시오!')
                if not vars[50]:
                    stage.unit(101).setDir(stage.countDir(101, 0))
                    stage.say('&101\n역시 매복이 있었군!\n허나 두려울 것은 없어 보인다……\n전군 돌격하라! 조조를 놓치지 마라!')
                vars[2] = 1
        vars[12] = 1
    if vars[3] and stage.round() >= 1 and stage.curCamp() == 0:
        stage.setAI(0, 0, 19, 39, 2, 1)
    if vars[3] and stage.round() >= 1 and stage.curCamp() == 2:
        stage.setAI(0, 0, 19, 39, 2, 6, -1, 8, 0)
        stage.setRectUnitHide(0, 0, 19, 1, 2)
    if vars[3] and stage.round() >= 1 and stage.isInRect(114, 0, 0, 19, 1):
        stage.unit(114).setDir(2)
        stage.say('&114\n어쩔 수 없군.\n이 원희는 먼저 물러나겠다!')
        stage.unit(114).hide()
    if vars[3] and stage.round() >= 1 and stage.isInRect(113, 0, 0, 19, 1):
        stage.unit(113).setDir(2)
        stage.unit(113).setAction(7)
        stage.say('&113\n빌어먹을, 너무도 분하다!\n퇴각하라!')
        stage.unit(113).setAction(0)
        stage.unit(113).hide()
    if vars[3] and stage.round() >= 1 and stage.isInRect(115, 0, 0, 19, 1):
        stage.unit(115).setDir(2)
        stage.unit(115).setAction(4)
        stage.say('&115\n성급하게 굴다 큰 잘못을 저질렀구나.\n퇴각하라, 퇴각하라!\n마음을 가라앉혀야겠다…….')
        stage.unit(115).hide()
    if vars[3] and stage.round() >= 1 and stage.isInRect(101, 0, 0, 19, 1):
        stage.unit(101).setDir(2)
        stage.unit(101).setAction(9)
        stage.say('&101\n또 졌구나………\n나는 정녕 조조를 이길 수 없는가?\n그놈에게만은 지고 싶지 않았거늘. 원통하다!')
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
        stage.say('&1\n적의 본진이 움직였다고!?\n공격할 절호의 기회 아닌가?\n&2\n주군, 아직은 이릅니다!\n&11\n적의 본진이 움직이는 듯하군……\n이제 공격할 때가 된 것인가……?\n&10\n전군 돌격하라!\n나를 따르라!\n&6\n음, 움직였나?\n전군 출전하라!\n&7\n안 된다! 이래서는 매복이 소용없어진다.\n&13\n지금 무슨 일인가?\n&14\n공격을 시작하라는 신호다!\n돌격하라!')
        if not vars[50]:
            stage.unit(101).setAction(5)
            stage.say('&101\n역시 매복이 있었군!\n허나 두려울 것은 없어 보인다……\n전군 돌격하라! 조조를 놓치지 마라!')
            stage.unit(101).setAction(0)
            stage.say('&0\n이런, 실패했나?\n일이 골치 아프게 되었군.')
        vars[2] = 1
        vars[6] = 1
    if not vars[2] and not vars[30] and stage.isInRect(15, 0, 29, 19, 39):
        stage.unit(15).setAction(9)
        stage.say('&15\n허억, 허억.\n겨우 위기를 벗어났군.\n&0\n허저, 잘했네!')
        stage.unit(0).setAction(5)
        stage.say('&0\n복병은 나와라!\n원소를 포위하라!')
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
        stage.say('&101\n역시 매복이 있었군!\n뒤는 신경 쓰지 마라!\n기세를 몰아 돌입하라!')
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
        stage.say('&115\n뭐라고, 앞에도 복병이 있다고?')
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
        stage.say('&114\n이, 이런……\n&113\n빌어먹을, 너무 일찍 우쭐대지 마라!')
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
        stage.say('&113\n으아!\n여기까지……')
        stage.unit(101).setDir(0)
        stage.unit(101).setAction(6)
        stage.say('&101\n괘씸한 조조 놈!\n감히 이 원소를 이토록 농락하다니!\n전군 퇴각하라! 서둘러라!')
        stage.unit(101).setAction(0)
        stage.setAI(0, 0, 19, 39, 2, 3, 101, 0, 0)
        stage.unit(101).setAI(4, -1, 8, 0)
        stage.unit(1).setDir(stage.countDir(1, 101))
        stage.say('&1\n계략에 걸렸구나!')
        stage.unit(2).setDir(stage.countDir(2, 101))
        stage.unit(2).setAction(5)
        stage.say('&2\n돌격하라!\n공격하라!')
        stage.unit(2).setAction(0)
        stage.unit(11).setDir(stage.countDir(11, 101))
        stage.say('&11\n드디어 출전할 수 있겠군.\n모두 돌격하라!')
        stage.unit(10).setDir(stage.countDir(10, 101))
        stage.unit(10).setAction(6)
        stage.say('&10\n원소를 무찔러라!')
        stage.unit(10).setAction(0)
        stage.unit(6).setDir(stage.countDir(6, 101))
        stage.say('&6\n적진이 크게 흐트러졌다!\n단숨에 무찔러라!')
        stage.unit(7).setDir(stage.countDir(7, 101))
        stage.unit(7).setAction(5)
        stage.say('&7\n죽여라! 죽여라! 죽여라!\n모조리 죽여라!')
        stage.unit(7).setAction(0)
        stage.unit(13).setDir(stage.countDir(13, 101))
        stage.say('&13\n하하하하.\n참으로 손쉽게 이겼군!')
        stage.unit(14).setDir(stage.countDir(14, 101))
        stage.say('&14\n돌격하라!\n하나도 놓치지 마라!')
        vars[2] = 1
        vars[3] = 1
        vars[30] = 1
    if not vars[2] and not vars[31] and stage.isInRect(1026, 0, 29, 19, 39):
        stage.unit(0).setAction(5)
        stage.say('&0\n복병은 나와라!\n원소를 포위하라!')
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
        stage.say('&101\n역시 매복이 있었군!\n뒤는 신경 쓰지 마라!\n기세를 몰아 돌입하라!')
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
        stage.say('&115\n뭐라고, 앞에도 복병이 있다고?')
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
        stage.say('&114\n이, 이런…….\n&113\n제길, 우쭐대지 마라!')
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
        stage.say('&113\n으아!\n여기까지……')
        stage.unit(101).setDir(0)
        stage.unit(101).setAction(6)
        stage.say('&101\n괘씸한 조조 놈!\n감히 이 원소를 이토록 농락하다니!\n전군 퇴각하라! 서둘러라!')
        stage.unit(101).setAction(0)
        stage.setAI(0, 0, 19, 39, 2, 3, 101, 0, 0)
        stage.unit(101).setAI(4, -1, 8, 0)
        stage.unit(1).setDir(stage.countDir(1, 101))
        stage.say('&1\n계략에 걸렸구나!')
        stage.unit(2).setDir(stage.countDir(2, 101))
        stage.unit(2).setAction(5)
        stage.say('&2\n돌격하라!\n공격하라!')
        stage.unit(2).setAction(0)
        stage.unit(11).setDir(stage.countDir(11, 101))
        stage.say('&11\n드디어 출전이군.\n모두 돌격하라!')
        stage.unit(10).setDir(stage.countDir(10, 101))
        stage.unit(10).setAction(6)
        stage.say('&10\n원소군을 무찔러라!')
        stage.unit(10).setAction(0)
        stage.unit(6).setDir(stage.countDir(6, 101))
        stage.say('&6\n적진이 크게 흐트러졌다!\n단숨에 무찔러라!')
        stage.unit(7).setDir(stage.countDir(7, 101))
        stage.unit(7).setAction(5)
        stage.say('&7\n죽여라! 죽여라! 죽여라!\n모조리 죽여라!')
        stage.unit(7).setAction(0)
        stage.unit(13).setDir(stage.countDir(13, 101))
        stage.say('&13\n하하하하.\n참으로 손쉽게 이겼군!')
        stage.unit(14).setDir(stage.countDir(14, 101))
        stage.say('&14\n돌격하라!\n하나도 놓치지 마라!')
        vars[2] = 1
        vars[3] = 1
        vars[31] = 1
    if not vars[40] and stage.isNear(15, 101, True) and stage.unitStateTest(15, 7, 100, 0):
        stage.unit(15).setDir(stage.countDir(15, 101))
        stage.unit(101).setDir(stage.countDir(101, 15))
        stage.say('&15\n원소, 네 목을 가져가겠다!\n&101\n오늘 네놈 같은 촌부에게 비웃음을 받다니\n사대에 걸쳐 삼공을 지낸 명문의 후손이 이대로 끝나는가?\n&15\n난세에 가문 따위는 아무 의미도 없다.\n목숨을 내놓아라!\n&101\n꿈도 꾸지 마라!\n조조의 목을 베고 천하를 차지해\n온 천하에 우리 원씨의 위명을 떨칠 것이다!')
        vars[40] = 1
    if not vars[2] and not vars[41] and stage.isNear(15, 114, True) and stage.unitStateTest(15, 7, 100, 0):
        stage.unit(15).setDir(stage.countDir(15, 114))
        stage.unit(114).setDir(stage.countDir(114, 15))
        stage.say('&114\n원숭이 재주를 제법 잘도 부리는구나!\n&15\n무슨 헛소리냐! 흥!')
        vars[41] = 1
    if not vars[2] and not vars[42] and stage.isNear(15, 115, True) and stage.unitStateTest(15, 7, 100, 0):
        stage.unit(15).setDir(stage.countDir(15, 115))
        stage.unit(115).setDir(stage.countDir(115, 15))
        stage.say('&115\n드디어 찾았다!\n&15\n참을성도 없는 녀석이군.\n너 같은 그릇으로는 천자가 될 수 없다!\n&115\n뭐라고!?')
        if not vars[2] and not vars[12]:
            if not vars[52]:
                stage.unit(115).setDir(stage.countDir(115, 15))
                stage.unit(115).setAction(5)
                stage.say('&115\n단숨에 허저를 죽이고\n그대로 조조의 본진까지 밀고 들어갈 수도 있겠군!')
                stage.unit(115).setAction(0)
            if not vars[53]:
                stage.unit(113).setDir(stage.countDir(113, 15))
                stage.unit(113).setAction(7)
                stage.say('&113\n조조가 우리를 모욕한 죄는\n너와 조조의 목으로 갚게 하겠다!')
                stage.unit(113).setAction(0)
            stage.unit(0).setAction(2)
            stage.say('&0\n(허, 허저!\n이대로라면 허저가 위험하다.\n어찌해야 하나……)\n&21\n(주군, 너무도 잔인한 처사일지 모르나\n허저를 버리는 한이 있더라도\n섣불리 움직여 적의 경계를 사서는 안 됩니다.)')
            sel = stage.choice('움직이지 않는다\n허저를 구원한다', -1)
            if sel == 1:
                stage.unit(0).setDir(2)
                stage.say('&0\n(……허저, 내 그대에게 면목이 없구나…….)')
                stage.unit(21).setDir(stage.countDir(21, 0))
                stage.unit(21).setAction(2)
                stage.say('&21\n(주군…… 참으셔야 합니다.)')
                stage.unit(0).setDir(stage.countDir(0, 15))
                stage.unit(15).setDir(stage.countDir(15, 101))
                stage.unit(15).setAction(7)
                stage.say('&15\n흥, 별것 아니군. 아직 버틸 수 있다!\n자신 있으면 덤벼라!\n아이고, 아파라!')
                stage.unit(15).setAction(0)
                if not vars[50]:
                    stage.unit(101).setDir(stage.countDir(101, 0))
                    stage.say('&101\n(조조 그 늙은 여우가 허저마저 버리려 한다고…….\n정말 매복이 없는 건가……?)')
                elif sel == 2:
                    stage.unit(0).setAction(5)
                    stage.say('&0\n어서 허저를 구하라!\n서둘러라! 전군 돌격!')
                    stage.unit(0).setAction(0)
                    stage.unit(21).setAction(4)
                    stage.say('&21\n아아!')
                    stage.unit(15).setDir(stage.countDir(15, 0))
                    stage.say('&15\n주, 주군!\n아직 때가 아닙니다……')
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
                    stage.say('&1\n허저, 그 녀석이 실패했군!')
                    stage.unit(2).setDir(stage.countDir(2, 15))
                    stage.say('&2\n적이 완전히 속지는 않았군.\n곤란하게 되었어!')
                    stage.unit(11).setDir(stage.countDir(11, 15))
                    stage.say('&11\n서둘러 허저 장군을 구해야 한다.\n어서!')
                    stage.unit(10).setDir(stage.countDir(10, 15))
                    stage.say('&10\n모두 돌격하라!\n나를 따르라!')
                    stage.unit(6).setDir(stage.countDir(6, 15))
                    stage.say('&6\n정말 못 말리는 사람이군…….')
                    stage.unit(7).setDir(stage.countDir(7, 15))
                    stage.say('&7\n안 된다!\n이래서는 매복의 효과가 사라진다……')
                    stage.unit(13).setDir(stage.countDir(13, 15))
                    stage.say('&13\n이렇게 된 이상 어쩔 수 없군……')
                    stage.unit(14).setDir(stage.countDir(14, 15))
                    stage.say('&14\n어서 돌격하라! 허저 장군, 잠시만 기다려 주십시오!')
                    if not vars[50]:
                        stage.unit(101).setDir(stage.countDir(101, 0))
                        stage.say('&101\n역시 매복이 있었군!\n허나 두려울 것은 없어 보인다……\n전군 돌격하라! 조조를 놓치지 마라!')
                    vars[2] = 1
            vars[12] = 1
        vars[42] = 1
    if not vars[2] and not vars[43] and stage.isNear(15, 113, True) and stage.unitStateTest(15, 7, 100, 0):
        stage.unit(15).setDir(stage.countDir(15, 113))
        stage.unit(113).setDir(stage.countDir(113, 15))
        stage.say('&113\n허저, 도망치지 마라!\n나와 승부를 가리자.\n&15\n척 보기만 해도 알겠군.\n너는 내 상대가 못 된다.\n몇 해 더 수련하고 오너라!\n&113\n으으윽!')
        vars[43] = 1
    if not vars[50] and stage.unitStateTest(101, 7, 0, 2):
        stage.say('&101\n또 졌구나………\n나는 정녕 조조를 이길 수 없는가?\n그놈에게만은 지고 싶지 않았거늘. 원통하다!')
        stage.unit(101).hide(1)
        stage.getItem(111, 0, True, 1027)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(114, 7, 0, 2):
        stage.say('&114\n이런.\n이 원희는 먼저 물러가겠다.')
        stage.unit(114).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(115, 7, 0, 2):
        stage.say('&115\n아프다, 아직 죽고 싶지 않아!\n……? ……? 어째서 이 꼴이 되었지?\n너무 성급했던 탓이야…….\n&115\n퇴각하라, 퇴각하라!\n우선 진정해야겠다…….')
        stage.unit(115).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(113, 7, 0, 2):
        stage.say('&113\n제길, 아깝구나.\n퇴각하라!')
        stage.unit(113).hide(1)
        vars[53] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        if vars[3]:
            stage.say('&0\n좋다, 마침내 적을 물리쳤다!\n우리의 승리다!')
            stage.reward()
        else:
            stage.say('&0\n좋다, 마침내 적을 물리쳤다!\n우리의 승리다!')
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
