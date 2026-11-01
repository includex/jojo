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
    stage.say('&78\n주군, 적은 성을 지키며\n나오지 않을 모양입니다.\n어찌하면 좋겠습니까?\n&76\n으으음.\n가증스러운 장료…… 정말 만만치 않군!\n이대로 맞서 있어 봐야 시간만 낭비할 뿐이다.\n&98\n성 밖으로 유인할 수만 있다면\n그 정도 병력은 일격에 무너뜨릴 수 있습니다…….')
    stage.unit(76).setDir(2)
    stage.say('&76\n이럴 때 공근이 있으면 좋으련만.\n누구 묘책이 없는가?')
    stage.unit(95).setDir(stage.countDir(95, 76))
    stage.say('&95\n주군, 염려 마십시오. 제게 묘책이 있습니다.\n이미 성 안에 복병을 배치했습니다.\n&95\n때가 되면 불을 놓고\n기회를 보아 서문을 열 것입니다.')
    stage.unit(76).setDir(stage.countDir(76, 95))
    stage.say('&76\n오, 과연 태사자로다!\n참으로 이 손권의 오른팔이구나.\n&97\n주군, 분명 좋은 생각입니다.\n하지만 실패하면 태사 장군이 위험합니다.\n&95\n위험한 편이 기다리는 것보다는 낫습니다.\n&95\n부디 저를 보내 주십시오.\n목숨을 걸고 반드시 성공하겠습니다.\n&76\n음!\n그대만 믿겠다!\n부디 조심하라.')
    stage.unit(10).setDir(stage.countDir(10, 2))
    stage.say('&10\n장 장군, 적을 여러 차례 물리쳤으니\n성대한 잔치를 열어 장병들을 위로해야겠습니다……')
    stage.unit(2).setDir(stage.countDir(2, 10))
    stage.say('&2\n악진 장군, 심정은 이해하오.\n허나 손권의 대군이 아직 물러나지 않았으니\n긴장을 풀 때가 아니오…….')
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
    stage.say('&318\n불이 났다! 적이 성을 공격한다!\n성안에 적이 나타났다!')
    stage.unit(319).setDir(3)
    stage.unit(319).setAction(5)
    stage.say('&319\n성 안에 첩자가 있다!\n어서 성 밖으로 달아나라!')
    stage.unit(319).setAction(0)
    stage.unit(11).setAction(10)
    stage.say('&11\n무슨 일이냐!\n적의 기습인가?')
    stage.unit(2).setDir(stage.countDir(2, 11))
    stage.say('&2\n이 장군, 진정하시오.\n어딘가 이상하지 않소?')
    stage.unit(11).setDir(stage.countDir(11, 319))
    stage.say('&11\n음……확실히 이상하군!\n누군가 혼란을 일으키고 있는 듯해.')
    stage.unit(10).setDir(3)
    stage.say('&10\n불길도 서문 쪽에 집중된 듯하군.')
    stage.unit(2).setDir(3)
    stage.say('&2\n손권이 서문을 열어 우리를 유인하려는 것이군.\n그렇다면 그 계책을 거꾸로 이용하자.')
    sel = stage.choice('서문을 연다\n남문을 연다', 2)
    if sel == 1:
        stage.unit(2).setDir(stage.countDir(2, 10))
        stage.say('&2\n적의 후방으로 우회하여 포위하라.\n서문 양옆에 부대를 집결시켜라.')
        stage.unit(10).setDir(stage.countDir(10, 2))
        stage.say('&10\n서문으로 집결하라고요……어째서입니까?\n&2\n성 안에서 소란을 일으키고 서문을 열어 기습한다.\n그것이 적의 계책이오. 하지만……\n&10\n아, 그렇군요.\n적을 성 안으로 끌어들인 뒤\n한꺼번에 섬멸하자는 말씀이시군요?')
        stage.unit(2).setDir(stage.countDir(2, 11))
        stage.say('&2\n이 장군, 이 일은 그대에게 부탁하오.')
        stage.unit(11).setDir(stage.countDir(11, 2))
        stage.unit(11).setAction(5)
        stage.say('&11\n알겠습니다.\n전군은 서문으로 집결하라!')
        stage.unit(11).setAction(0)
        stage.unit(3, 1).move(9, 3, 2)
        stage.unit(4, 1).move(9, 6, 0)
        stage.unit(5, 1).move(10, 3, 2)
        stage.unit(6, 1).move(10, 6, 0)
        stage.unit(11).setDir(3)
        stage.say('&11\n준비됐다!\n문을 열어라!')
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
        stage.say('&95\n좋다! 바로 지금이다.\n전군 돌입하라!\n조조군을 무찔러라!')
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
        stage.say('&11\n걸려들었구나, 태사자!\n정말 어리석은 놈이군!\n전군 일제히 공격하라!\n&95\n뭐, 뭐라고!?\n이런!\n계책을 간파당했다!')
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
        stage.say('&2\n적의 퇴로를 에워싸라.\n남문으로 부대를 집결시켜라.')
        stage.unit(10).setDir(stage.countDir(10, 2))
        stage.say('&10\n남문으로 집결하라고……어째서입니까?\n&2\n서문을 열어 기습하는 것이\n놈들의 계략입니다.\n그러니 본진의 수비는 반드시 허술할 것입니다.\n&10\n그렇군요.\n적의 본진을 공격할 절호의 기회겠습니다.\n&2\n놈들은 서문 돌파에만 마음을 쏟고 있으니\n우리가 남문으로 공격하리라고는\n꿈에도 생각하지 못할 것입니다.\n&10\n이것이 바로 강한 곳을 피하고 빈 곳을 치는 전술이군요!')
        stage.unit(2).setDir(stage.countDir(2, 11))
        stage.say('&2\n이 장군, 성안의 적은 그대에게 맡기겠소.')
        stage.unit(11).setDir(stage.countDir(11, 2))
        stage.say('&11\n장 장군, 제게 맡기십시오.\n장군께서는 성 밖의 적에게 전념하십시오.')
        stage.unit(2).move(14, 11, 2)
        stage.unit(10).move(15, 11, 2)
        stage.unit(3, 1).move(14, 10, 2)
        stage.unit(4, 1).move(15, 10, 2)
        stage.unit(5, 1).move(14, 9, 2)
        stage.unit(6, 1).move(15, 9, 2)
        stage.unit(2).setDir(2)
        stage.unit(2).setAction(5)
        stage.say('&2\n좋다! 성문을 열어라!\n전군, 나와 함께 돌격하라!')
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
        stage.say('&76\n뭐라고! 적이 쳐들어왔다고!?\n어찌 된 것이냐…… 서문은 어떻게 되었느냐!?\n&98\n적이 공격했다고!?\n&78\n서둘러라! 주군께 접근하게 두지 마라!\n&97\n이런!\n오히려 우리가 습격을 당하다니…….')
        stage.setUnitStatus([
            {
                'hStatus': [
                    9],
                'x1': 13,
                'y1': 17,
                'x2': 16,
                'y2': 17,
                'camp': 2 }])
        stage.say('&95\n왜, 왜 서문이 열리지 않느냐!\n뭐라고!? 주군의 본진이 습격당했다고!?\n대체 어찌 된 일이냐!')
        stage.unit(95).setDir(stage.countDir(95, 76))
        stage.unit(95).setAction(5)
        stage.say('&95\n으음, 서둘러라!\n돌아가 본진을 구하라!')
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
        stage.say('&11\n동오의 명장 태사자도 여기서 목숨을 잃겠구나!\n이 이전이 저승길을 배웅해 주마.\n&95\n헛소리 마라!\n싸움은 이제부터다!')
        vars[40] = 1
    if not vars[41] and stage.isNear(2, 95, True):
        stage.unit(95).setDir(stage.countDir(95, 2))
        stage.unit(2).setDir(stage.countDir(2, 95))
        stage.say('&95\n이번 싸움에서 장군의 예측은 귀신 같았습니다.\n과연 한 방면을 맡을 대장이십니다.\n&2\n그대 역시 널리 이름난 명장이오.\n장군과 겨룰 수 있어 영광이오.')
        vars[41] = 1
    if vars[629] and not vars[42] and stage.isNear(2, 76, True):
        stage.unit(2).setDir(stage.countDir(2, 76))
        stage.unit(76).setDir(stage.countDir(76, 2))
        stage.say('&2\n손권, 오늘 적벽의 원한을 갚겠다!\n&76\n흥! 이번 싸움도 우리 동오가 승리한다.')
        vars[42] = 1
    if not vars[50] and stage.unitStateTest(76, 7, 0, 2):
        stage.say('&76\n어, 어찌 이런 일이……\n이번 원정이 이대로 끝나는 것인가……\n으으, 이 손권이 큰 실수를 했구나……')
        if not vars[2]:
            if not vars[51]:
                stage.unit(78).setDir(stage.countDir(78, 76))
                stage.unit(140).setAction(5)
                stage.say('&78\n주, 주군!\n적이 주군께 접근하지 못하게 하라!\n모두 어서 주군을 지켜라!\n&78\n어서요, 주군! 지금입니다……')
                stage.unit(140).setAction(0)
                vars[51] = 1
            if not vars[53]:
                stage.unit(97).setDir(stage.countDir(97, 76))
                stage.say('&97\n주군, 이곳은 안전하지 않습니다.\n서둘러 떠나십시오!')
                vars[53] = 1
            stage.unit(76).hide()
            stage.unit(78).hide()
            stage.unit(97).hide()
        else:
            stage.unit(76).hide(1)
        vars[50] = 1
    if not vars[51] and stage.unitStateTest(78, 7, 0, 2):
        stage.say('&78\n아이고! 어서 달아나야겠다……\n칼부림은 내 장기가 아니란 말이다……')
        stage.unit(78).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(98, 7, 0, 2):
        stage.say('&98\n뭐라고!?\n으윽, 너무 방심했군……\n내 목숨도 여기까지인가……커억!')
        stage.unit(98).hide(2)
        stage.getItem(120, -1, True, 1027)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(97, 7, 0, 2):
        stage.say('&97\n안 되겠다!\n으으……이제 손쓸 도리가 없군……')
        if not vars[50]:
            stage.unit(97).setDir(stage.countDir(97, 76))
            stage.say('&97\n주군, 마지막 간언을 들어 주십시오…….\n무사히 물러나실 수 있다면\n말릉으로 도읍을 옮기십시오…….\n&97\n그곳에는 제왕의 기운이 있어\n동오에 꼭 알맞습니다.\n저, 저는 이제 가겠습니다…… 으윽.')
            stage.unit(76).setDir(stage.countDir(76, 97))
            stage.say('&76\n장굉!')
        stage.unit(97).hide(2)
        stage.getItem(135, -1, True, 1027)
        vars[53] = 1
    if not vars[54] and stage.unitStateTest(95, 7, 0, 2):
        stage.say('&95\n큰 뜻을 이루지 못하고 한만 남기는구나……\n이것이 내 운명인가……으윽!')
        stage.unit(95).hide(2)
        stage.getItem(79, -1, True, 1027)
        if vars[2] and not vars[50] and not vars[51] and not vars[52] and not vars[53]:
            stage.say('&76\n뭐, 뭐라고!\n태사자 장군이 죽었다고!?\n&97\n태사자 장군…….\n&78\n태사자 장군께서………\n삼군 가운데 으뜸가는 용장을 잃다니…….\n동오에 더없이 큰 손실입니다.\n&98\n어서 군사를 보내 구하라!\n병사들이 아직 고전하고 있을 것이다!\n&76\n으으음!\n가증스러운 조조!\n이 빚은 결코 잊지 않겠다!')
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
        stage.say('&2\n훌륭하다. 마침내 적군을 무찔렀다!\n우리 군의 승리다!')
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
