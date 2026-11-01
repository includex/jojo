# Source Generated with Decompyle++
# File: S_09.marshal (Python 3.9)


def scene0():
    stage.itemVars([
        20,
        21,
        22], [
        '(7,13)',
        '(15,15)',
        '(18,6)'])
    stage.nearEvent([
        0,
        149,
        0,
        129,
        0,
        131,
        0,
        13], 1)
    stage.initFight()
    stage.setGlobalData(20, -2, -1, 0, 0, 1)
    stage.createFriend([
        {
            'id': 149,
            'x': 5,
            'y': 8,
            'lv': 0,
            'ai': 6,
            'targetX': 7,
            'targetY': 13,
            'i': 0 },
        {
            'id': 210,
            'x': 4,
            'y': 7,
            'lv': 0,
            'ai': 6,
            'targetX': 7,
            'targetY': 13,
            'i': 1 },
        {
            'id': 211,
            'x': 5,
            'y': 6,
            'lv': 0,
            'ai': 6,
            'targetX': 7,
            'targetY': 13,
            'i': 2 }])
    stage.createEnemy([
        {
            'id': 129,
            'x': 1,
            'y': 1,
            'lv': 4,
            'ai': 1,
            'i': 0 },
        {
            'id': 258,
            'x': 0,
            'y': 1,
            'lv': 0,
            'ai': 1,
            'i': 1 },
        {
            'id': 259,
            'x': 2,
            'y': 0,
            'lv': 0,
            'ai': 1,
            'i': 2 },
        {
            'id': 282,
            'x': 0,
            'y': 0,
            'lv': 0,
            'ai': 1,
            'i': 3 },
        {
            'id': 283,
            'x': 1,
            'y': 0,
            'lv': 0,
            'ai': 1,
            'i': 4 },
        {
            'id': 131,
            'x': 1,
            'y': 4,
            'lv': 4,
            'ai': 1,
            'i': 5 },
        {
            'id': 260,
            'x': 0,
            'y': 4,
            'lv': 0,
            'ai': 1,
            'i': 6 },
        {
            'id': 261,
            'x': 2,
            'y': 3,
            'lv': 0,
            'ai': 1,
            'i': 7 },
        {
            'id': 284,
            'x': 0,
            'y': 3,
            'lv': 0,
            'ai': 1,
            'i': 8 },
        {
            'id': 285,
            'x': 1,
            'y': 3,
            'lv': 0,
            'ai': 1,
            'i': 9 },
        {
            'id': 13,
            'x': 4,
            'y': 2,
            'lv': 4,
            'ai': 1,
            'i': 10 },
        {
            'id': 262,
            'x': 3,
            'y': 2,
            'lv': 0,
            'ai': 1,
            'i': 11 },
        {
            'id': 263,
            'x': 5,
            'y': 1,
            'lv': 0,
            'ai': 1,
            'i': 12 },
        {
            'id': 286,
            'x': 3,
            'y': 1,
            'lv': 0,
            'ai': 1,
            'i': 13 },
        {
            'id': 287,
            'x': 4,
            'y': 1,
            'lv': 0,
            'ai': 1,
            'i': 14 }])
    stage.createMine([
        {
            'idx': 0,
            'x': 17,
            'y': 11 },
        {
            'idx': 1,
            'x': 16,
            'y': 11 },
        {
            'idx': 2,
            'x': 18,
            'y': 12 },
        {
            'idx': 3,
            'x': 17,
            'y': 9 },
        {
            'idx': 4,
            'x': 19,
            'y': 9 },
        {
            'idx': 5,
            'x': 18,
            'y': 7 },
        {
            'idx': 6,
            'x': 17,
            'y': 4 },
        {
            'idx': 7,
            'x': 17,
            'y': 2 },
        {
            'idx': 8,
            'x': 19,
            'y': 3 },
        {
            'idx': 9,
            'x': 19,
            'y': 2 },
        {
            'idx': 10,
            'x': 18,
            'y': 1 },
        {
            'idx': 11,
            'x': 17 },
        {
            'idx': 12,
            'x': 18,
            'y': 2 },
        {
            'idx': 13,
            'x': 16,
            'y': 1 },
        {
            'idx': 14,
            'x': 19 }])
    stage.loadBg(9)
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
    stage.bgSound(5)
    stage.unit(131).setAction(5)
    stage.say('@dialogue:S_09.scene0.dialogue0001')
    stage.unit(131).setAction(0)
    stage.say('@dialogue:S_09.scene0.dialogue0002')
    stage.unit(5).setDir(stage.countDir(5, 149))
    stage.unit(0).setDir(stage.countDir(0, 149))
    stage.say('@dialogue:S_09.scene0.dialogue0003')
    stage.setWinCondition('승리 조건\n1. 헌제가 북동쪽 끝에 도착한다.\n2. 모든 적을 섬멸한다.\n\n패배 조건\n1. 헌제가 적에게 붙잡힌다.\n2. 조조가 사망한다.\n3. 20턴을 초과한다.')
    stage.showWinCondition('헌제가 탈출한다!')
    stage.unit(149).heightLight()
    stage.heightLight(16, 0, 19, 0)
    stage.startOper()
    vars[509] = 1
    stage.setMenuVisible(True)
    return False


def scene1():
    if vars[1002] and not vars[60] and not vars[11] and stage.round() < 3 and stage.curCamp() == 2 and stage.isInPos(149, 7, 13):
        stage.unit(149).setAI(2, -1, 15, 15)
        vars[60] = 1
    if vars[1002] and not vars[61] and not vars[12] and stage.round() < 5 and stage.curCamp() == 2 and stage.isInPos(149, 15, 15):
        stage.unit(149).setAI(2, -1, 15, 15)
        vars[61] = 1
    if vars[1002] and not vars[62] and not vars[13] and stage.round() < 7 and stage.curCamp() == 2 and stage.isInPos(149, 18, 6):
        stage.unit(149).setAI(2, -1, 15, 15)
        vars[62] = 1
    if not vars[11] and stage.round() == 3:
        stage.unit(149).setAI(6, -1, 15, 15)
        stage.unit(210).setAI(1)
        stage.unit(211).setAI(1)
        vars[11] = 1
    if not vars[12] and stage.round() == 5:
        stage.unit(149).setAI(6, -1, 18, 6)
        vars[12] = 1
    if not vars[13] and stage.round() == 7:
        stage.unit(149).setAI(6, -1, 18, 0)
        vars[13] = 1
    if not vars[20] and stage.isInPos(1025, 7, 13):
        stage.getItem(150, 0, True, 1025)
        vars[20] = 1
    if not vars[21] and stage.isInPos(1025, 15, 15):
        stage.getItem(150, 0, True, 1025)
        vars[21] = 1
    if not vars[22] and stage.isInPos(1025, 18, 6):
        stage.getItem(150, 0, True, 1025)
        vars[22] = 1
    if not vars[30] and stage.isInRect(149, 16, 0, 19, 0):
        stage.say('@dialogue:S_09.scene1.dialogue0001')
        stage.unit(149).setDir(stage.countDir(149, 0))
        if vars[2]:
            stage.say('@dialogue:S_09.scene1.dialogue0002')
            stage.unit(0).setDir(stage.countDir(0, 149))
            stage.getItem(52, 0)
            stage.setRectUnitHide(0, 0, 19, 19, 1)
            stage.say('@dialogue:S_09.scene1.dialogue0003')
        if not vars[2]:
            stage.resumeCtrl()
            stage.say('@dialogue:S_09.scene1.dialogue0004')
            stage.getItem(80, 0)
            stage.setRectUnitHide(0, 0, 19, 19, 1)
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_09.scene1.dialogue0005')
            stage.unit(0).setAction(0)
            stage.reward()
            stage.end()
            vars[0] = 1
            vars[100] = 1
            vars[609] = 1
            return True
        vars[30] = 1
    if not vars[40] and stage.isNear(0, 149, True):
        stage.unit(0).setDir(stage.countDir(0, 149))
        stage.unit(149).setDir(stage.countDir(149, 0))
        stage.say('@dialogue:S_09.scene1.dialogue0006')
        sel = stage.choice('헌제 일행을 호위한다\n모든 적을 섬멸한다', -1)
        if sel == 1:
            stage.say('@dialogue:S_09.scene1.dialogue0007')
        elif sel == 2:
            stage.unit(0).setAction(5)
            stage.say('@dialogue:S_09.scene1.dialogue0008')
            stage.unit(0).setAction(0)
            stage.say('@dialogue:S_09.scene1.dialogue0009')
            stage.setWinCondition('승리 조건\n1. 모든 적을 섬멸한다.\n\n패배 조건\n1. 헌제가 적군에게 붙잡힌다.\n2. 조조가 사망한다.\n3. 20턴을 초과한다.')
            stage.showWinCondition('모든 적을 섬멸하라!')
            vars[2] = 1
        vars[40] = 1
    if not vars[41] and stage.isNear(0, 129, True):
        stage.unit(129).setDir(stage.countDir(129, 0))
        stage.unit(0).setDir(stage.countDir(0, 129))
        stage.say('@dialogue:S_09.scene1.dialogue0010')
        vars[41] = 1
    if not vars[42] and stage.isNear(0, 131, True):
        stage.unit(131).setDir(stage.countDir(131, 0))
        stage.unit(0).setDir(stage.countDir(0, 131))
        stage.say('@dialogue:S_09.scene1.dialogue0011')
        vars[42] = 1
    if not vars[43] and stage.isNear(0, 13, True):
        stage.unit(0).setDir(stage.countDir(0, 13))
        stage.unit(13).setDir(stage.countDir(13, 0))
        stage.say('@dialogue:S_09.scene1.dialogue0012')
        vars[43] = 1
    if not vars[50] and stage.unitStateTest(149, 7, 0, 2):
        stage.say('@dialogue:S_09.scene1.dialogue0013')
        vars[50] = 1
        stage.info('조조는 헌제를 구하지 못했다.')
        stage.end()
        stage.lose()
        vars[1] = 1
        return True
    if not vars[51] and stage.unitStateTest(13, 7, 0, 2):
        stage.say('@dialogue:S_09.scene1.dialogue0014')
        stage.unit(13).hide(1)
        vars[51] = 1
    if not vars[52] and stage.unitStateTest(131, 7, 0, 2):
        stage.say('@dialogue:S_09.scene1.dialogue0015')
        stage.unit(131).hide(1)
        vars[52] = 1
    if not vars[53] and stage.unitStateTest(129, 7, 0, 2):
        stage.say('@dialogue:S_09.scene1.dialogue0016')
        stage.unit(129).hide(1)
        vars[53] = 1
    if not vars[0] and stage.winTest():
        stage.resumeCtrl()
        stage.say('@dialogue:S_09.scene1.dialogue0017')
        if not vars[30]:
            stage.say('@dialogue:S_09.scene1.dialogue0018')
            if vars[2]:
                stage.say('@dialogue:S_09.scene1.dialogue0019')
                stage.getItem(52, 0)
            if not vars[2]:
                stage.say('@dialogue:S_09.scene1.dialogue0020')
                stage.getItem(80, 0)
            vars[30] = 1
        stage.reward()
        stage.end()
        vars[0] = 1
        vars[609] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('조조는 동탁의 잔당에게 패했다.')
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
