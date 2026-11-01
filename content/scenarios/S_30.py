# Source Generated with Decompyle++
# File: S_30.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20], [
        '(15,18)'])
    stage.nearEvent([
        11,
        95,
        2,
        95,
        2,
        76], 1)
    stage.initFight()
    stage.setGlobalData(25, -2, -1, -1, 1, 1)
    stage.createEnemy([
        {
            'id': 76,
            'x': 14,
            'y': 17,
            'dir': 0,
            'lv': 6,
            'ai': 0,
            'i': 0 },
        {
            'id': 98,
            'x': 15,
            'y': 17,
            'dir': 0,
            'lv': 6,
            'ai': 0,
            'i': 1 },
        {
            'id': 78,
            'x': 15,
            'y': 18,
            'dir': 0,
            'lv': 6,
            'ai': 0,
            'i': 2 },
        {
            'id': 97,
            'x': 14,
            'y': 18,
            'dir': 0,
            'lv': 6,
            'ai': 0,
            'i': 3 },
        {
            'id': 358,
            'x': 13,
            'y': 17,
            'dir': 0,
            'lv': 0,
            'ai': 0,
            'i': 4 },
        {
            'id': 359,
            'x': 16,
            'y': 17,
            'dir': 0,
            'lv': 0,
            'ai': 0,
            'i': 5 },
        {
            'id': 234,
            'x': 13,
            'y': 18,
            'dir': 0,
            'lv': 0,
            'ai': 0,
            'i': 6 },
        {
            'id': 235,
            'x': 16,
            'y': 18,
            'dir': 0,
            'lv': 0,
            'ai': 0,
            'i': 7 },
        {
            'id': 95,
            'x': 3,
            'y': 4,
            'dir': 1,
            'lv': 6,
            'ai': 0,
            'i': 8 },
        {
            'id': 258,
            'x': 3,
            'y': 5,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 9 },
        {
            'id': 282,
            'x': 2,
            'y': 4,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 10 },
        {
            'id': 283,
            'x': 2,
            'y': 5,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 11 },
        {
            'id': 360,
            'x': 3,
            'y': 3,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 12 },
        {
            'id': 361,
            'x': 3,
            'y': 6,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 13 },
        {
            'id': 362,
            'x': 3,
            'y': 2,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 14 },
        {
            'id': 363,
            'x': 3,
            'y': 7,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 15 },
        {
            'id': 318,
            'hide': 1,
            'x': 9,
            'y': 1,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 16 },
        {
            'id': 319,
            'hide': 1,
            'x': 9,
            'y': 8,
            'dir': 1,
            'lv': 0,
            'ai': 1,
            'i': 17 },
        {
            'id': 236,
            'x': 2,
            'y': 3,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 18 },
        {
            'id': 237,
            'x': 2,
            'y': 6,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 19 },
        {
            'id': 238,
            'x': 2,
            'y': 2,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 20 },
        {
            'id': 239,
            'x': 2,
            'y': 7,
            'dir': 1,
            'lv': 0,
            'ai': 0,
            'i': 21 }])
    stage.createMine([
        {
            'idx': 0,
            'x': 14,
            'y': 5 },
        {
            'idx': 1,
            'x': 13,
            'y': 4 },
        {
            'idx': 2,
            'x': 11,
            'y': 5 },
        {
            'idx': 3,
            'x': 8,
            'y': 2,
            'dir': 3 },
        {
            'idx': 4,
            'x': 8,
            'y': 7,
            'dir': 3 },
        {
            'idx': 5,
            'x': 12,
            'y': 11 },
        {
            'idx': 6,
            'x': 16,
            'y': 11 },
        {
            'idx': 7,
            'dir': 0 },
        {
            'idx': 8,
            'dir': 0 },
        {
            'idx': 9,
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
    stage.setEnemyEquip(95, -1, 0, 11, 0, -1)
    stage.setEnemyEquip(97, -1, 0, -1, 0, 28)
    stage.setEnemyEquip(98, -1, 0, -1, 0, 13)
    stage.loadBg(30)
    stage.setObjects(True, 17, [
        [
            23,
            7,
            4],
        [
            24,
            7,
            5]])
    stage.setObjects(True, 17, [
        [
            25,
            14,
            12],
        [
            26,
            15,
            12]])
    if vars[1013]:
        gvars[4076] = 0
        pvars[1] = 4882144
        gvars[10] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        label('lab28')
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
            goto('lab28')
    stage.draw()
    stage.bgSound(1)
    stage.say('@dialogue:S_30.scene0.dialogue0001')
    stage.unit(76).setDir(2)
    stage.say('@dialogue:S_30.scene0.dialogue0002')
    stage.unit(95).setDir(stage.countDir(95, 76))
    stage.say('@dialogue:S_30.scene0.dialogue0003')
    stage.unit(76).setDir(stage.countDir(76, 95))
    stage.say('@dialogue:S_30.scene0.dialogue0004')
    stage.unit(10).setDir(stage.countDir(10, 2))
    stage.say('@dialogue:S_30.scene0.dialogue0005')
    stage.unit(2).setDir(stage.countDir(2, 10))
    stage.say('@dialogue:S_30.scene0.dialogue0006')
    stage.bgSound(4)
    stage.unit(318).show()
    stage.unit(319).show()
    stage.setFire(True, 9, 0)
    stage.setFire(True, 10, 1)
    stage.setFire(True, 9, 2)
    stage.setFires(True, [
        [
            9,
            7],
        [
            10,
            7],
        [
            9,
            9]])
    stage.setFire(True, 10, 10)
    stage.unit(318).setAction(8)
    stage.say('@dialogue:S_30.scene0.dialogue0007')
    stage.unit(319).setDir(3)
    stage.unit(319).setAction(5)
    stage.say('@dialogue:S_30.scene0.dialogue0008')
    stage.unit(319).setAction(0)
    stage.unit(11).setAction(10)
    stage.say('@dialogue:S_30.scene0.dialogue0009')
    stage.unit(2).setDir(stage.countDir(2, 11))
    stage.say('@dialogue:S_30.scene0.dialogue0010')
    stage.unit(11).setDir(stage.countDir(11, 319))
    stage.say('@dialogue:S_30.scene0.dialogue0011')
    stage.unit(10).setDir(3)
    stage.say('@dialogue:S_30.scene0.dialogue0012')
    stage.unit(2).setDir(3)
    stage.say('@dialogue:S_30.scene0.dialogue0013')
    sel = stage.choice('서문을 연다\n남문을 연다', 2)
    if sel == 1:
        stage.unit(2).setDir(stage.countDir(2, 10))
        stage.say('@dialogue:S_30.scene0.dialogue0014')
        stage.unit(10).setDir(stage.countDir(10, 2))
        stage.say('@dialogue:S_30.scene0.dialogue0015')
        stage.unit(2).setDir(stage.countDir(2, 11))
        stage.say('@dialogue:S_30.scene0.dialogue0016')
        stage.unit(11).setDir(stage.countDir(11, 2))
        stage.unit(11).setAction(5)
        stage.say('@dialogue:S_30.scene0.dialogue0017')
        stage.unit(11).setAction(0)
        stage.unit(3, 1).move(9, 3, 2)
        stage.unit(4, 1).move(9, 6, 0)
        stage.unit(5, 1).move(10, 3, 2)
        stage.unit(6, 1).move(10, 6, 0)
        stage.unit(11).setDir(3)
        stage.say('@dialogue:S_30.scene0.dialogue0018')
        stage.setObjects(False, 16, [
            [
                23,
                7,
                4],
            [
                24,
                7,
                5]])
        stage.unit(95).setAction(5)
        stage.say('@dialogue:S_30.scene0.dialogue0019')
        stage.unit(95).setAction(0)
        stage.unit(95).move(9, 4, 1)
        stage.unit(258).move(9, 5, 1)
        stage.unit(282).move(8, 4, 1)
        stage.unit(283).move(8, 5, 1)
        stage.unit(360).move(7, 4, 1)
        stage.unit(361).move(7, 5, 1)
        stage.unit(362).move(6, 4, 1)
        stage.unit(363).move(6, 5, 1)
        stage.unit(236).move(5, 4, 1)
        stage.unit(237).move(5, 5, 1)
        stage.unit(238).move(5, 3, 1)
        stage.unit(239).move(5, 6, 1)
        stage.say('@dialogue:S_30.scene0.dialogue0020')
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 8,
                'y1': 4,
                'x2': 9,
                'y2': 5,
                'camp': 2 }])
        vars[2] = 1
    elif sel == 2:
        stage.unit(2).setDir(stage.countDir(2, 10))
        stage.say('@dialogue:S_30.scene0.dialogue0021')
        stage.unit(10).setDir(stage.countDir(10, 2))
        stage.say('@dialogue:S_30.scene0.dialogue0022')
        stage.unit(2).setDir(stage.countDir(2, 11))
        stage.say('@dialogue:S_30.scene0.dialogue0023')
        stage.unit(11).setDir(stage.countDir(11, 2))
        stage.say('@dialogue:S_30.scene0.dialogue0024')
        stage.unit(2).move(14, 11, 2)
        stage.unit(10).move(15, 11, 2)
        stage.unit(3, 1).move(14, 10, 2)
        stage.unit(4, 1).move(15, 10, 2)
        stage.unit(5, 1).move(14, 9, 2)
        stage.unit(6, 1).move(15, 9, 2)
        stage.unit(2).setDir(2)
        stage.unit(2).setAction(5)
        stage.say('@dialogue:S_30.scene0.dialogue0025')
        stage.unit(2).setAction(0)
        stage.setObjects(False, 16, [
            [
                25,
                14,
                12],
            [
                26,
                15,
                12]])
        stage.unit(2).move(14, 15, 2)
        stage.unit(10).move(15, 15, 2)
        stage.unit(3, 1).move(14, 14, 2)
        stage.unit(4, 1).move(15, 14, 2)
        stage.unit(5, 1).move(14, 13, 2)
        stage.unit(6, 1).move(15, 13, 2)
        stage.unit(76).setDir(stage.countDir(76, 2))
        stage.unit(78).setDir(stage.countDir(78, 2))
        stage.unit(97).setDir(stage.countDir(97, 2))
        stage.unit(98).setDir(stage.countDir(98, 2))
        stage.unit(76).setAction(10)
        stage.say('@dialogue:S_30.scene0.dialogue0026')
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 13,
                'y1': 17,
                'x2': 16,
                'y2': 17,
                'camp': 2 }])
        stage.say('@dialogue:S_30.scene0.dialogue0027')
        stage.unit(95).setDir(stage.countDir(95, 76))
        stage.unit(95).setAction(5)
        stage.say('@dialogue:S_30.scene0.dialogue0028')
        stage.unit(95).setAction(0)
        stage.unit(95).setAI(1)
        stage.unit(258).setAI(1)
        stage.unit(282).setAI(1)
        stage.unit(283).setAI(1)
        stage.unit(360).setAI(1)
        stage.unit(361).setAI(1)
        stage.unit(362).setAI(1)
        stage.unit(363).setAI(1)
        stage.unit(236).setAI(1)
        stage.unit(237).setAI(1)
        stage.unit(238).setAI(1)
        stage.unit(239).setAI(1)
    stage.setWinCondition('승리 조건\n1. 모든 적을 섬멸한다.\n\n패배 조건\n1. 아군 전원이 퇴각한다.\n2. 25턴을 초과한다.')
    stage.showWinCondition('모든 적을 섬멸하라!')
    stage.startOper()
    vars[530] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if not vars[20] and stage.isInPos(1025, 15, 18):
        stage.getItem(151, -1, True, 1025)
        vars[20] = 1
    if not vars[40] and stage.isNear(11, 95, True):
        stage.unit(11).setDir(stage.countDir(11, 95))
        stage.unit(95).setDir(stage.countDir(95, 11))
        stage.say('@dialogue:S_30.scene1.dialogue0001')
        vars[40] = 1
    if not vars[41] and stage.isNear(2, 95, True):
        stage.unit(95).setDir(stage.countDir(95, 2))
        stage.unit(2).setDir(stage.countDir(2, 95))
        stage.say('@dialogue:S_30.scene1.dialogue0002')
        vars[41] = 1
    if vars[629] and not vars[42] and stage.isNear(2, 76, True):
        stage.unit(2).setDir(stage.countDir(2, 76))
        stage.unit(76).setDir(stage.countDir(76, 2))
        stage.say('@dialogue:S_30.scene1.dialogue0003')
        vars[42] = 1
    if not vars[50] and stage.unitStateTest(76, 7, 0, 2):
        stage.say('@dialogue:S_30.scene1.dialogue0004')
        if not vars[2]:
            if not vars[51]:
                stage.unit(78).setDir(stage.countDir(78, 76))
                stage.unit(140).setAction(5)
                stage.say('@dialogue:S_30.scene1.dialogue0005')
                stage.unit(140).setAction(0)
                vars[51] = 1
            if not vars[53]:
                stage.unit(97).setDir(stage.countDir(97, 76))
                stage.say('@dialogue:S_30.scene1.dialogue0006')
                vars[53] = 1
            stage.unit(76).hide()
            stage.unit(78).hide()
            stage.unit(97).hide()
        else:
            stage.unit(76).hide(1)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(78, 7, 0, 2):
        stage.say('@dialogue:S_30.scene1.dialogue0007')
        stage.unit(78).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(98, 7, 0, 2):
        stage.say('@dialogue:S_30.scene1.dialogue0008')
        stage.unit(98).hide(2)
        stage.getItem(120, -1, True, 1027)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(97, 7, 0, 2):
        stage.say('@dialogue:S_30.scene1.dialogue0009')
        if not vars[50]:
            stage.unit(97).setDir(stage.countDir(97, 76))
            stage.say('@dialogue:S_30.scene1.dialogue0010')
            stage.unit(76).setDir(stage.countDir(76, 97))
            stage.say('@dialogue:S_30.scene1.dialogue0011')
        stage.unit(97).hide(2)
        stage.getItem(135, -1, True, 1027)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(95, 7, 0, 2):
        stage.say('@dialogue:S_30.scene1.dialogue0012')
        stage.unit(95).hide(2)
        stage.getItem(79, -1, True, 1027)
        if vars[2] and not vars[50] and not vars[51] and not vars[52] and not vars[53]:
            stage.say('@dialogue:S_30.scene1.dialogue0013')
        stage.unit(76).setAI(1)
        stage.unit(78).setAI(1)
        stage.unit(97).setAI(1)
        stage.unit(98).setAI(1)
        stage.unit(358).setAI(1)
        stage.unit(359).setAI(1)
        stage.unit(234).setAI(1)
        stage.unit(235).setAI(1)
        vars[54] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_30.scene1.dialogue0014')
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[630] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조군은 손권군에게 패했다')
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
