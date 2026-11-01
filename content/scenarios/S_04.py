# Source Generated with Decompyle++
# File: S_04.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20,
        21,
        22], [
        '(4,4)',
        '(13,17)',
        '(15,5)'])
    stage.initFight()
    stage.setGlobalData(20, -2, -1, 0, 0, 1)
    stage.createEnemy([
        {
            'id': 474,
            'x': 15,
            'y': 5,
            'dir': 3,
            'i': 0 },
        {
            'id': 475,
            'x': 14,
            'y': 4,
            'dir': 3,
            'ai': 0,
            'i': 1 },
        {
            'id': 476,
            'x': 16,
            'y': 5,
            'dir': 3,
            'ai': 0,
            'i': 2 },
        {
            'id': 477,
            'x': 15,
            'y': 4,
            'dir': 3,
            'ai': 0,
            'i': 3 },
        {
            'id': 478,
            'x': 14,
            'y': 6,
            'dir': 3,
            'ai': 0,
            'i': 4 },
        {
            'id': 479,
            'x': 15,
            'y': 6,
            'dir': 3,
            'ai': 0,
            'i': 5 },
        {
            'id': 480,
            'x': 16,
            'y': 6,
            'dir': 3,
            'ai': 0,
            'i': 6 },
        {
            'id': 481,
            'x': 14,
            'y': 5,
            'dir': 3,
            'ai': 0,
            'i': 7 },
        {
            'id': 482,
            'x': 16,
            'y': 4,
            'dir': 3,
            'ai': 0,
            'i': 8 },
        {
            'id': 483,
            'x': 13,
            'y': 17,
            'dir': 3,
            'i': 9 },
        {
            'id': 484,
            'x': 13,
            'y': 16,
            'dir': 3,
            'ai': 0,
            'i': 10 },
        {
            'id': 485,
            'x': 14,
            'y': 16,
            'dir': 3,
            'ai': 0,
            'i': 11 },
        {
            'id': 486,
            'x': 12,
            'y': 17,
            'dir': 3,
            'ai': 0,
            'i': 12 },
        {
            'id': 487,
            'x': 12,
            'y': 18,
            'dir': 3,
            'ai': 0,
            'i': 13 },
        {
            'id': 488,
            'x': 13,
            'y': 18,
            'dir': 3,
            'ai': 0,
            'i': 14 },
        {
            'id': 489,
            'x': 14,
            'y': 18,
            'dir': 3,
            'ai': 0,
            'i': 15 },
        {
            'id': 490,
            'x': 12,
            'y': 16,
            'dir': 3,
            'ai': 0,
            'i': 16 },
        {
            'id': 491,
            'x': 14,
            'y': 17,
            'dir': 3,
            'ai': 0,
            'i': 17 },
        {
            'id': 492,
            'x': 4,
            'y': 4,
            'i': 18 },
        {
            'id': 493,
            'x': 3,
            'y': 3,
            'ai': 0,
            'i': 19 },
        {
            'id': 494,
            'x': 4,
            'y': 3,
            'ai': 0,
            'i': 20 },
        {
            'id': 495,
            'x': 5,
            'y': 3,
            'ai': 0,
            'i': 21 },
        {
            'id': 496,
            'x': 5,
            'y': 4,
            'ai': 0,
            'i': 22 },
        {
            'id': 497,
            'x': 3,
            'y': 5,
            'ai': 0,
            'i': 23 },
        {
            'id': 498,
            'x': 4,
            'y': 5,
            'ai': 0,
            'i': 24 },
        {
            'id': 499,
            'x': 3,
            'y': 4,
            'ai': 0,
            'i': 25 },
        {
            'id': 500,
            'x': 5,
            'y': 5,
            'ai': 0,
            'i': 26 }])
    stage.createMine([
        {
            'idx': 0,
            'x': 1,
            'y': 16,
            'dir': 1 },
        {
            'idx': 1,
            'x': 2,
            'y': 17,
            'dir': 1 },
        {
            'idx': 2,
            'y': 16,
            'dir': 1 },
        {
            'idx': 3,
            'x': 2,
            'y': 18,
            'dir': 1 },
        {
            'idx': 4,
            'x': 1,
            'y': 15,
            'dir': 1 },
        {
            'idx': 5,
            'x': 3,
            'y': 17,
            'dir': 1 },
        {
            'idx': 6,
            'x': 2,
            'y': 16,
            'dir': 1 },
        {
            'idx': 7,
            'x': 1,
            'y': 17,
            'dir': 1 },
        {
            'idx': 8,
            'y': 17,
            'dir': 1 },
        {
            'idx': 9,
            'x': 1,
            'y': 18,
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
    stage.loadBg(4)
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab21')
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
            goto('lab21')
    stage.draw()
    stage.bgSound(1)
    stage.unit(475).setAction(10)
    stage.say('@dialogue:S_04.scene0.dialogue0001')
    stage.unit(474).setAction(2)
    stage.say('@dialogue:S_04.scene0.dialogue0002')
    stage.unit(0).setAction(5)
    stage.say('@dialogue:S_04.scene0.dialogue0003')
    stage.unit(0).setAction(0)
    stage.setWinCondition('승리 조건\n1. 모든 적군을 섬멸한다.\n\n패배 조건\n1. 조조가 사망한다.\n2. 20턴을 초과한다.')
    stage.showWinCondition('모든 적을 섬멸하라!')
    vars[504] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[11] and stage.round() == 1:
        stage.unit(0).move(2, 15, 1)
        stage.say('@dialogue:S_04.scene1.dialogue0001')
        stage.unit(20).move(3, 14, 3)
        stage.unit(16).move(3, 15, 3)
        stage.unit(24).move(3, 16, 3)
        stage.say('@dialogue:S_04.scene1.dialogue0002')
        sel = stage.choice('순욱의 의견을 따른다\n전위의 의견을 따른다\n순유의 의견을 따른다', -1)
        if sel == 1:
            stage.say('@dialogue:S_04.scene1.dialogue0003')
            stage.unit(20).move(8, 10, 1)
            stage.say('@dialogue:S_04.scene1.dialogue0004')
            stage.unit(20).setDir(2)
            stage.say('@dialogue:S_04.scene1.dialogue0005')
            stage.unit(20).setDir(0)
            stage.unit(20).setAction(5)
            stage.say('@dialogue:S_04.scene1.dialogue0006')
            stage.unit(20).setAction(0)
            stage.unit(474).setAction(6)
            stage.say('@dialogue:S_04.scene1.dialogue0007')
            stage.unit(474).setAction(0)
            stage.say('@dialogue:S_04.scene1.dialogue0008')
            stage.unit(492).setAction(1)
            stage.say('@dialogue:S_04.scene1.dialogue0009')
            stage.unit(497).move(0, 5, 3)
            stage.unit(498).move(1, 5, 3)
            stage.unit(500).move(2, 5, 3)
            stage.unit(499).move(0, 4, 3)
            stage.unit(492).move(1, 4, 3)
            stage.unit(496).move(2, 4, 3)
            stage.unit(493).move(0, 3, 3)
            stage.unit(494).move(1, 3, 3)
            stage.unit(495).move(2, 3, 3)
            stage.setRectUnitHide(0, 3, 2, 5, 2)
            stage.unit(20).move(3, 14, 3)
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_04.scene1.dialogue0010')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:S_04.scene1.dialogue0011')
            stage.unit(474).setAction(10)
            stage.unit(474).setAction(4)
            stage.say('@dialogue:S_04.scene1.dialogue0012')
            stage.unit(20).setDir(1)
            stage.unit(16).setDir(1)
            stage.unit(24).setDir(1)
            vars[2] = 1
        elif sel == 2:
            stage.unit(0).setAction(6)
            stage.say('@dialogue:S_04.scene1.dialogue0013')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:S_04.scene1.dialogue0014')
            stage.unit(16).move(11, 17, 1)
            stage.unit(16).setAction(7)
            stage.say('@dialogue:S_04.scene1.dialogue0015')
            stage.unit(16).setAction(0)
            stage.unit(490).setAction(10)
            stage.unit(491).setAction(10)
            stage.say('@dialogue:S_04.scene1.dialogue0016')
            stage.unit(490).hide()
            stage.unit(491).hide()
            stage.unit(16).move(13, 5, 1)
            stage.unit(16).setAction(3)
            stage.say('@dialogue:S_04.scene1.dialogue0017')
            stage.unit(481).setAction(10)
            stage.unit(482).setAction(10)
            stage.say('@dialogue:S_04.scene1.dialogue0018')
            stage.unit(481).hide()
            stage.unit(482).hide()
            stage.unit(16).move(4, 6, 0)
            stage.unit(16).setAction(5)
            stage.say('@dialogue:S_04.scene1.dialogue0019')
            stage.unit(16).setAction(0)
            stage.unit(499).setAction(10)
            stage.unit(500).setAction(10)
            stage.say('@dialogue:S_04.scene1.dialogue0020')
            stage.unit(499).hide()
            stage.unit(500).hide()
            stage.unit(16).move(3, 15, 3)
            stage.unit(16).setAction(9)
            stage.say('@dialogue:S_04.scene1.dialogue0021')
            stage.unit(474).setAction(4)
            stage.say('@dialogue:S_04.scene1.dialogue0022')
            stage.unit(483).setAction(2)
            stage.say('@dialogue:S_04.scene1.dialogue0023')
            stage.unit(492).setAction(5)
            stage.say('@dialogue:S_04.scene1.dialogue0024')
            stage.unit(492).setAction(0)
            stage.unit(474).addLv(1)
            stage.unit(475).addLv(1)
            stage.unit(476).addLv(1)
            stage.unit(477).addLv(1)
            stage.unit(478).addLv(1)
            stage.unit(479).addLv(1)
            stage.unit(480).addLv(1)
            stage.unit(483).addLv(1)
            stage.unit(484).addLv(1)
            stage.unit(485).addLv(1)
            stage.unit(486).addLv(1)
            stage.unit(487).addLv(1)
            stage.unit(488).addLv(1)
            stage.unit(489).addLv(1)
            stage.unit(492).addLv(1)
            stage.unit(493).addLv(1)
            stage.unit(494).addLv(1)
            stage.unit(495).addLv(1)
            stage.unit(496).addLv(1)
            stage.unit(497).addLv(1)
            stage.unit(498).addLv(1)
            stage.say('@dialogue:S_04.scene1.dialogue0025')
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_04.scene1.dialogue0026')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:S_04.scene1.dialogue0027')
            stage.unit(20).setDir(1)
            stage.unit(16).setDir(1)
            stage.unit(24).setDir(1)
            vars[3] = 1
        elif sel == 3:
            stage.say('@dialogue:S_04.scene1.dialogue0028')
            stage.unit(24).move(13, 11, 2)
            stage.unit(20).move(14, 11, 2)
            stage.unit(16).move(15, 11, 2)
            stage.unit(24).setAction(5)
            stage.say('@dialogue:S_04.scene1.dialogue0029')
            stage.unit(24).setAction(0)
            stage.unit(483).setDir(stage.countDir(483, 24))
            stage.unit(483).setAction(10)
            stage.say('@dialogue:S_04.scene1.dialogue0030')
            stage.unit(483).move(13, 12, 0)
            stage.unit(484).move(14, 12, 0)
            stage.unit(485).move(15, 12, 0)
            stage.unit(486).move(13, 13, 0)
            stage.unit(487).move(14, 13, 0)
            stage.unit(488).move(15, 13, 0)
            stage.unit(489).move(13, 14, 0)
            stage.unit(490).move(14, 14, 0)
            stage.unit(491).move(15, 14, 0)
            stage.unit(24).setAction(10)
            stage.say('@dialogue:S_04.scene1.dialogue0031')
            stage.unit(483).setAction(5)
            stage.say('@dialogue:S_04.scene1.dialogue0032')
            stage.unit(483).setAction(0)
            stage.unit(24).move(6, 8, 1)
            stage.unit(20).move(6, 9, 1)
            stage.unit(16).move(6, 10, 1)
            stage.unit(483).move(7, 8, 3)
            stage.unit(484).move(7, 9, 3)
            stage.unit(485).move(7, 10, 3)
            stage.unit(486).move(8, 8, 3)
            stage.unit(487).move(8, 9, 3)
            stage.unit(488).move(8, 10, 3)
            stage.unit(489).move(9, 8, 3)
            stage.unit(490).move(9, 9, 3)
            stage.unit(491).move(9, 10, 3)
            stage.unit(4, 1).move(9, 19, 0)
            stage.unit(5, 1).move(8, 19, 0)
            stage.unit(6, 1).move(7, 19, 0)
            stage.unit(7, 1).move(6, 19, 0)
            stage.unit(0).move(0, 10, 1)
            stage.unit(8, 1).move(0, 11, 1)
            stage.unit(9, 1).move(0, 12, 1)
            stage.say('@dialogue:S_04.scene1.dialogue0033')
            stage.unit(24).move(0, 17, 1)
            stage.unit(20).move(1, 18, 1)
            stage.unit(16).move(2, 19, 1)
            stage.unit(483).move(4, 14, 3)
            stage.unit(484).move(5, 14, 3)
            stage.unit(485).move(6, 14, 3)
            stage.unit(486).move(4, 15, 3)
            stage.unit(487).move(5, 15, 3)
            stage.unit(488).move(6, 15, 3)
            stage.unit(489).move(4, 16, 3)
            stage.unit(490).move(5, 16, 3)
            stage.unit(491).move(6, 16, 3)
            stage.say('@dialogue:S_04.scene1.dialogue0034')
            stage.unit(0).move(6, 11, 2)
            stage.unit(8, 1).move(5, 11, 2)
            stage.unit(9, 1).move(4, 11, 2)
            stage.say('@dialogue:S_04.scene1.dialogue0035')
            stage.unit(4, 1).move(10, 14, 3)
            stage.unit(5, 1).move(10, 15, 3)
            stage.unit(6, 1).move(10, 16, 3)
            stage.unit(7, 1).move(10, 17, 3)
            stage.say('@dialogue:S_04.scene1.dialogue0036')
            stage.setAI(4, 14, 6, 16, 5, 1)
            stage.setUnitStatus([
                {
                    'hStatus': [
                        9],
                    'x1': 5,
                    'y1': 15,
                    'x2': 6,
                    'y2': 16,
                    'camp': 5 }])
            vars[4] = 1
        stage.bgSound(2)
        stage.startOper()
        vars[11] = 1
    if not vars[20] and stage.isInPos(1025, 4, 4):
        stage.getItem(150, 0, True, 1025)
        vars[20] = 1
    if not vars[21] and stage.isInPos(1025, 13, 17):
        stage.getItem(150, 0, True, 1025)
        vars[21] = 1
    if not vars[22] and stage.isInPos(1025, 15, 5):
        stage.getItem(150, 0, True, 1025)
        vars[22] = 1
    if not vars[2] and not vars[6] and not vars[7] and stage.totalRectUnit(2, 3, 3, 5, 5) < 5 and stage.curCamp() == 2 and stage.totalRectUnit(2, 14, 4, 16, 6) >= 7:
        stage.unit(474).setDir(1)
        stage.unit(474).setDir(3)
        stage.unit(474).setAction(6)
        stage.say('@dialogue:S_04.scene1.dialogue0037')
        stage.unit(474).setAction(0)
        stage.unit(477).setAI(4, -1, 4, 4)
        stage.unit(478).setAI(4, -1, 4, 4)
        stage.unit(479).setAI(4, -1, 4, 4)
        stage.unit(480).setAI(4, -1, 4, 4)
        stage.unit(481).setAI(4, -1, 4, 4)
        stage.unit(482).setAI(4, -1, 4, 4)
        vars[6] = 1
    if not vars[4] and not vars[6] and not vars[7] and stage.totalRectUnit(2, 12, 16, 14, 18) < 5 and stage.curCamp() == 2 and stage.totalRectUnit(2, 14, 4, 16, 6) >= 7:
        stage.unit(474).setDir(2)
        stage.unit(474).setDir(2)
        stage.unit(474).setAction(6)
        stage.say('@dialogue:S_04.scene1.dialogue0038')
        stage.unit(474).setAction(0)
        stage.unit(477).setAI(4, -1, 13, 17)
        stage.unit(478).setAI(4, -1, 13, 17)
        stage.unit(479).setAI(4, -1, 13, 17)
        stage.unit(480).setAI(4, -1, 13, 17)
        stage.unit(481).setAI(4, -1, 13, 17)
        stage.unit(482).setAI(4, -1, 13, 17)
        vars[7] = 1
    if not vars[50] and stage.unitStateTest(474, 7, 0, 2):
        stage.say('@dialogue:S_04.scene1.dialogue0039')
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(483, 7, 0, 2):
        stage.say('@dialogue:S_04.scene1.dialogue0040')
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(492, 7, 0, 2):
        stage.say('@dialogue:S_04.scene1.dialogue0041')
        vars[52] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_04.scene1.dialogue0042')
        if vars[3]:
            stage.reward(0, [
                91,
                -1])
        else:
            stage.reward(0, [
                96,
                -1])
        stage.end()
        vars[0] = 1
        vars[604] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 청주의 황건적에게 패했다.')
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
