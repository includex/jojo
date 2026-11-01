# Source Generated with Decompyle++
# File: R_38.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            19,
            52,
            40,
            2]])
    if vars[735]:
        stage.showUnit(4, 52, 70)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('위·오의 비밀 동맹')
    stage.setStageName('허도  승상부')
    if vars[735]:
        stage.effectSound(200, 255)
        stage.bgSound(18)
        stage.delay(3)
        model.setFace(3)
        stage.unit(0).setAction(3)
        stage.say('&0\n조창, 이번 싸움에서 참으로 용맹했구나.\n과연 나 조조의 아들답다!')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.delay(3)
        stage.unit(4).setAction(6)
        stage.say('&4\n칭찬해 주셔서 감사합니다, 아버님.\n&0\n이제 정식으로 우리 군의 장수가 되었으니\n훌륭한 활약을 기대하겠다.\n&4\n예!')
        stage.unit(4).setAction(0)
        model.unitJoin(4, 0, 0)
        if vars[1013]:
            gvars[0] = 4
            gvars[4076] = 0
            gvars[1] = gvars[0]
            gvars[2] = 5259072
            gvars[3] = 0
            gvars[4] = 0
            gvars[5] = 0
            gvars[1] *= 4
            gvars[1] += gvars[2]
            pvars[0] = gvars[1]
            stage.varOper(2, 3, 2, 1, 0)
            stage.setUnitAttr(gvars[0], 17, gvars[3])
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
            if gvars[4] == 0:
                if gvars[6] <= 34:
                    stage.setUnitAbility(gvars[0], 10, 0, 35)
                if gvars[7] <= 34:
                    stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[4] == 1:
                if gvars[8] <= 34:
                    stage.setUnitAbility(gvars[0], 12, 0, 35)
                if gvars[9] <= 35:
                    stage.setUnitAbility(gvars[0], 6, 1, 30)
                    stage.setUnitAbility(gvars[0], 8, 1, 30)
            if gvars[4] == 2:
                if gvars[6] <= 34:
                    stage.setUnitAbility(gvars[0], 10, 0, 35)
                if gvars[7] <= 34:
                    stage.setUnitAbility(gvars[0], 11, 0, 35)
                if gvars[8] <= 34:
                    stage.setUnitAbility(gvars[0], 12, 0, 35)
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
                    model.unit(gvars[0]).setPosts(0)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(1)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(2)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(3)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(4)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(5)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(6)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(7)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(8)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(9)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(10)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(11)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(12)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(13)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(14)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(15)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(16)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(17)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(18)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(19)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(20)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(21)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(22)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(23)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(24)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(25)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(26)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(27)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(28)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(29)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(30)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(31)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(32)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(33)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(34)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(35)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(36)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(37)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(38)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(39)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(40)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(41)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(42)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(43)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(44)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(45)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(46)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(47)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(48)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(49)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(50)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(51)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(52)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(53)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(54)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(55)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(56)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(57)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(58)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(59)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(60)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(61)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(62)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(63)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(64)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(65)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(66)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(67)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(68)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(69)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(70)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(71)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(72)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(73)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(74)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(75)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(76)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(77)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(78)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(79)
                gvars[4081] = 0
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
    stage.say('&0\n그렇지, 중달.\n이대로 한중을 포기할 수는 없네.\n그러니……')
    stage.unit(0).setAction(3)
    stage.say('&0\n동오를 이용할 생각일세.')
    stage.unit(0).setAction(0)
    stage.say('&0\n이제 촉의 병력도 얕볼 수는 없다.\n정면으로 싸워 이길 수 있다 해도\n최선의 방책이라 할 수는 없지.')
    stage.unit(19).setAction(6)
    stage.say('&19\n그렇군요…….\n그래서 촉의 시선을 동오로 돌리시겠다는 겁니까?')
    stage.unit(19).setAction(0)
    stage.say('&0\n바로 그렇다.\n동오가 형주의 촉군을 공격하게 하자!\n&0\n손권이 형주 삼군을 얻었다 해도\n나머지 군들을 포기하지는 않았을 것이다.\n&0\n그러니 동오와 비밀리에 동맹을 맺을 생각이다.')
    stage.unit(19).setAction(6)
    stage.say('&19\n저도 찬성합니다.\n&0\n그런데………\n누구를 사자로 보내는 것이 좋겠는가?\n&19\n만총 선생이 적임이라 생각합니다.\n&0\n백녕이?\n&19\n예.\n백녕 선생은 정직한 분이니\n외교에 도움이 될 것입니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n음, 좋다.\n백녕!\n백녕은 있느냐!?')
    stage.showUnit(25, 90, 55, 3)
    stage.say('&25\n만총, 여기 있습니다.')
    stage.unit(25).move(48, 55, 3)
    stage.unit(25).setAction(0)
    stage.say('&0\n오, 백녕.\n그대를 사신으로 보내 동오와 동맹을 맺고자 하네.')
    stage.unit(25).setAction(6)
    stage.say('&25\n동오로 가라고요?\n&0\n음, 동오와 동맹을 맺고\n형주를 공격하도록 권하게.\n문제없겠는가?\n&25\n예. 반드시 사명을 다하겠습니다.\n&0\n음, 수고를 부탁하네.')
    stage.unit(25).setAction(0)
    stage.unit(25).move(95, 55, 1)
    stage.unit(25).hide()
    stage.say('&0\n중달, 형주를 지키는 촉장은 관우겠지?')
    stage.unit(19).setAction(6)
    stage.say('&19\n그렇습니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n……관우인가?\n또다시 그와 싸우게 될 줄이야…….')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 334, 102)
    stage.bottomTxt('형주를 지키던 촉의 장수는', False, False, False)
    stage.delay(5)
    stage.showHead(3, 278, 236)
    stage.delay(5)
    stage.bottomTxt('지용을 겸비한 관우였다.', True, False, False)
    stage.delay(8)
    stage.showHead(76, 500, 217)
    stage.delay(5)
    stage.bottomTxt('손권', True, True, False)
    stage.delay(5)
    stage.bottomTxt('위가 먼저 형주를 공격한다는 조건으로', True, False, False)
    stage.delay(5)
    stage.bottomTxt('위와 비밀리에 동맹을 맺었다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('조조가 이 조건을 받아들이자', True, True, False)
    stage.delay(5)
    stage.head(0).hide()
    stage.delay(5)
    stage.showHead(5, 300, 76)
    stage.delay(5)
    stage.bottomTxt('조인을 파견하여', True, False, False)
    stage.delay(5)
    stage.showHead(25, 364, 109)
    stage.delay(5)
    stage.bottomTxt('만총', True, False, False)
    stage.delay(5)
    stage.head(5).move(278, 156)
    stage.head(25).move(342, 202)
    stage.delay(5)
    stage.bottomTxt('형주를 공격했다.', True, False, True)
    stage.bottomTxt('그러나 조인과 만총은 관우의 반격을 받아', False, False, False)
    stage.delay(5)
    stage.head(5).move(255, 140)
    stage.head(25).move(342, 140)
    stage.head(25).move(319, 140)
    stage.head(3).move(288, 220)
    stage.delay(5)
    stage.bottomTxt('오히려 번성에 갇히고 말았다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            9,
            48,
            58,
            3],
        [
            14,
            48,
            52,
            3]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n문칙, 그대도 들었겠지만\n조인과 만총이 번성에서 관우에게 포위되었네.\n&0\n그들만으로는 관우를 상대하기 어려우니\n즉시 원군을 이끌고 번성으로 가게.')
    stage.unit(14).setAction(6)
    stage.say('&14\n예.')
    stage.unit(14).setAction(0)
    stage.say('&0\n영명, 자네는 부장이 되어 문칙을 돕게.')
    stage.unit(9).setAction(6)
    stage.say('&9\n예.')
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(14).setAction(0, 2)
    stage.say('&14\n방덕 장군을 보내신다고요?\n&0\n음, 무슨 일인가?')
    stage.unit(14).setAction(0, 3)
    stage.say('&14\n아……아무것도 아닙니다.\n그럼 준비하러 가겠습니다.\n이만 물러가겠습니다.')
    stage.unit(14).move(95, 52, 1)
    stage.unit(14).hide()
    stage.delay(3)
    stage.unit(9).setAction(0, 1)
    stage.say('&9\n우 장군께서는 아직 저를 온전히 믿지 못하시는군.')
    stage.unit(9).setAction(0, 3)
    stage.say('&9\n제 과거를 잘 알고 계시니\n어쩔 수 없군요.')
    stage.unit(0).setAction(3)
    stage.say('&0\n영명, 나도 그런 소문은 들었네.\n하지만 자네를 굳게 믿기에\n번성의 원군으로 보낸 것이네.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('&9\n감사합니다, 승상.\n그럼 저도 출병을 준비하겠습니다.')
    stage.unit(9).setAction(0)
    stage.unit(9).move(85, 58, 1)
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(9).setAction(0, 3)
    stage.say('&9\n주군.\n&0\n무슨 일인가?')
    stage.unit(9).setAction(6)
    stage.say('&9\n이토록 중요한 임무를 맡겨 주시니\n참으로 감사드립니다.\n&9\n목숨을 걸어서라도\n번성 구원 임무를 완수하겠습니다.\n&9\n……반드시 해내겠습니다!\n&0\n영명, 공을 세우려 너무 서두르지 말게.\n&9\n……그럼 물러가겠습니다.')
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(9).move(95, 58, 1)
    stage.unit(9).hide()
    stage.say('&0\n아무래도 흥분한 모양인데, 저래서야 되겠는가?\n전장에서는 냉정해야 한다.\n그렇지 않으면 목숨을 잃게 되지.\n&0\n이대로라면 영명이 위험하겠군.\n관우는 지용을 겸비한 맹장이다.\n냉정을 잃고서 이길 상대가 아니야.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n하지만 지금 방덕은\n내 말도 들으려 하지 않을 듯하군.\n우금에게 방덕을 진정시키도록 해야겠다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n……방덕, 절대 경거망동하지 말게.\n나는 자네를 잃고 싶지 않네……')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(5, 271, 145)
    stage.showHead(3, 271, 225)
    stage.showHead(14, 283, 47)
    stage.delay(5)
    stage.bottomTxt('우금과', False, False, False)
    stage.delay(5)
    stage.showHead(9, 347, 89)
    stage.delay(5)
    stage.bottomTxt('방덕', True, False, False)
    stage.delay(5)
    stage.head(14).move(207, 47)
    stage.head(14).move(207, 145)
    stage.head(9).move(335, 145)
    stage.delay(5)
    stage.bottomTxt('원군을 이끌고 조인이 포위된 번성으로 달려갔다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 51)
    stage.showUnits([
        [
            76,
            53,
            29,
            2],
        [
            88,
            41,
            48,
            1],
        [
            78,
            41,
            54,
            1],
        [
            80,
            65,
            48,
            3],
        [
            194,
            65,
            54,
            3]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('건업  손권의 저택')
    stage.say('&76\n자명, 이미 알고 있겠지……\n위의 장수 조인이 번성에서 관우에게 포위된 일을 말일세.')
    stage.unit(80).setAction(6)
    stage.say('&80\n예.\n천재일우의 기회입니다.')
    stage.unit(80).setAction(0)
    stage.say('&76\n그렇다.\n이 기회에 형주 전체를 차지해야 한다.\n&76\n자명, 가서 형주를 빼앗아라.\n그대만 믿겠다!')
    stage.unit(80).setAction(6)
    stage.say('&80\n예. 염려 마십시오, 주군.')
    stage.unit(80).setAction(0)
    stage.delay(3)
    stage.unit(80).setAction(0, 2)
    stage.say('&80\n관우 저놈은…….\n「적당할 때 물러날 줄」 모르는 모양이군.\n&80\n이번 승리는 동오의 것이다.')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(5, 325, 145)
    stage.showHead(3, 261, 145)
    stage.bottomTxt('조인과 관우는 번성에서 치열하게 싸우고 있었다.', False, False, False)
    stage.delay(8)
    stage.showHead(80, 498, 210)
    stage.delay(5)
    stage.bottomTxt('여몽은 이 기회를 틈타,', True, True, False)
    stage.delay(5)
    stage.head(80).move(307, 238)
    stage.delay(5)
    stage.bottomTxt('형주를 점령하는 데 성공했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            1,
            52,
            70],
        [
            8,
            58,
            70],
        [
            15,
            64,
            70],
        [
            10,
            70,
            70],
        [
            20,
            52,
            40,
            2],
        [
            19,
            58,
            40,
            2],
        [
            24,
            64,
            40,
            2],
        [
            21,
            70,
            40,
            2]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n번성에서는 아직 소식이 없느냐?')
    stage.unit(20).setAction(6)
    stage.say('&20\n예. 아직 아무 소식도 없습니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n우금의 원군도 출발한 지 꽤 되었군.\n너무 늦지 않아야 할 텐데.')
    stage.showUnit(181, 90, 55, 3)
    stage.say('&181\n아룁니다.')
    stage.say('&0\n번성에서 소식이 왔는가?')
    stage.unit(181).setAction(11)
    stage.say('&181\n아닙니다. 동오의 사자가 왔습니다.\n&0\n동오의 사자라고?\n좋다. 들라 하라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.unit(181).hide()
    stage.say('&0\n동오의 사자라……\n형주 공격을 알리러 왔을 테니\n번성의 사정도 알 수 있겠군.')
    stage.showUnit(193, 90, 55, 3)
    stage.say('&193\n승상을 뵙습니다.')
    stage.unit(193).move(48, 55, 3)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('&193\n주군께서 보내신 사자입니다.\n&0\n음, 형주에 관한 일이겠군?\n&193\n예. 저희 군은 며칠 전 형주를 공격하여\n전역을 점령했습니다.')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n그렇소? 참으로 경사스러운 일이오!\n이 조조도 진심으로 기뻐한다고\n손 장군께 전해 주시오.')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&193\n감사합니다, 승상.\n그리고 번성의 일입니다만……\n&0\n음, 번성이라고……?\n&193\n지금은 여전히 관우군이 우세합니다.\n저희 주군께서 즉시 원군을 보내 주시기를 청하셨습니다.\n&0\n그렇소? 감사하기 그지없구려.\n우리 군도 원군에 관해 의논하던 참이오.\n손 장군의 배려에 감사하오.\n&193\n예.\n그럼 이만 물러가겠습니다.')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(193).move(95, 55, 1)
    stage.unit(193).hide()
    if vars[735]:
        stage.unit(19).setAction(6)
        stage.say('&19\n동오가 벌써 형주 전체를 차지했다고요?')
        stage.unit(19).setAction(0)
        stage.say('&0\n관우가 없는 틈을 타 성공한 것이겠지만,\n손권도 제법이군.')
        stage.unit(1).setAction(0, 3)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n맹덕, 번성의 사정이 좋지 않네.')
        stage.unit(1).setAction(0)
        stage.say('&0\n음, 우금과 방덕을 원군으로 보냈는데도\n끝내 관우를 이기지 못할 줄이야……\n관우는 참으로 뛰어난 장수로군.')
        stage.unit(1).setAction(2)
        stage.say('&1\n지금 감탄하고 있을 때가 아니네!')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(17)
        stage.say('&0\n음.\n좋다. 즉시 번성을 구원하러 간다.\n출발하라!')
        stage.unit(0).setAction(0)
        stage.say('&0\n(방덕이 걱정되는군. 만에 하나 그가…….)')
    if not vars[735]:
        stage.unit(19).setAction(6)
        stage.say('&19\n동오가 벌써 형주 전체를 차지했다고요?')
        stage.unit(19).setAction(0)
        stage.say('&0\n관우가 없는 틈을 타 성공한 것이겠지만,\n손권도 제법이군.')
        stage.unit(1).setAction(0, 3)
        stage.say('&1\n맹덕, 번성의 상황이 좋지 않네.\n&0\n음, 우금과 방덕을 원군으로 보내고도\n끝내 관우를 이기지 못하다니…….\n관우는 참으로 뛰어난 무장이야.')
        stage.unit(1).setAction(2)
        stage.say('&1\n지금 감탄하고 있을 때가 아니네!')
        stage.unit(1).setAction(0)
        stage.say('&0\n음.\n좋다. 즉시 번성을 구원하러 가겠다.')
        stage.effectSound(201, 255)
        stage.bgSound(18)
        stage.delay(3)
        stage.showUnit(4, 90, 55, 3)
        stage.say('&4\n아버님!')
        stage.unit(1).setAction(0, 0)
        stage.unit(4).move(48, 55, 3)
        stage.unit(4).setAction(0)
        stage.say('&0\n조창이었구나? 무슨 일로 왔느냐?\n승상부에 드나들도록 허락한 적은 없을 텐데.')
        stage.unit(4).setAction(6)
        stage.say('&4\n소자를 용서해 주십시오.\n방금 동오 사자가 한 말을\n모두 들었습니다.\n&4\n아버님, 이제 소자도 무예가 익숙해졌으니\n부디 아버님의 군에 들어가게 해 주십시오.\n&0\n조창……\n그 정도 재주로 전장에 설 수 있다고 생각한다면\n크나큰 오산이다.\n&4\n알고 있습니다.\n허나 지금은 사람이 필요한 때이니\n소자가 어찌 보고만 있겠습니까.')
        stage.unit(0).setAction(3)
        stage.say('&0\n허, 아무래도 네 고집을 꺾을 수 없겠구나.\n좋다. 종군을 허락하마.')
        stage.unit(0).setAction(0)
        stage.say('&4\n감사합니다, 아버님!')
        stage.unit(4).setAction(0)
        stage.delay(3)
        model.unitJoin(4, 0, 0)
        if vars[1013]:
            gvars[0] = 4
            gvars[4076] = 0
            gvars[1] = gvars[0]
            gvars[2] = 5259072
            gvars[3] = 0
            gvars[4] = 0
            gvars[5] = 0
            gvars[1] *= 4
            gvars[1] += gvars[2]
            pvars[0] = gvars[1]
            stage.varOper(2, 3, 2, 1, 0)
            stage.setUnitAttr(gvars[0], 17, gvars[3])
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
            if gvars[4] == 0:
                if gvars[6] <= 34:
                    stage.setUnitAbility(gvars[0], 10, 0, 35)
                if gvars[7] <= 34:
                    stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[4] == 1:
                if gvars[8] <= 34:
                    stage.setUnitAbility(gvars[0], 12, 0, 35)
                if gvars[9] <= 35:
                    stage.setUnitAbility(gvars[0], 6, 1, 30)
                    stage.setUnitAbility(gvars[0], 8, 1, 30)
            if gvars[4] == 2:
                if gvars[6] <= 34:
                    stage.setUnitAbility(gvars[0], 10, 0, 35)
                if gvars[7] <= 34:
                    stage.setUnitAbility(gvars[0], 11, 0, 35)
                if gvars[8] <= 34:
                    stage.setUnitAbility(gvars[0], 12, 0, 35)
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
                    model.unit(gvars[0]).setPosts(0)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(1)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(2)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(3)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(4)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(5)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(6)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(7)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(8)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(9)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(10)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(11)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(12)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(13)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(14)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(15)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(16)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(17)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(18)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(19)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(20)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(21)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(22)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(23)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(24)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(25)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(26)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(27)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(28)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(29)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(30)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(31)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(32)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(33)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(34)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(35)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(36)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(37)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(38)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(39)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(40)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(41)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(42)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(43)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(44)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(45)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(46)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(47)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(48)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(49)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(50)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(51)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(52)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(53)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(54)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(55)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(56)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(57)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(58)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(59)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(60)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(61)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(62)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(63)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(64)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(65)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(66)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(67)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(68)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(69)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(70)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(71)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(72)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(73)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(74)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(75)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(76)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(77)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(78)
                gvars[2] += 1
                if gvars[102] == gvars[2]:
                    model.unit(gvars[0]).setPosts(79)
                gvars[4081] = 0
        stage.unit(0).setAction(17)
        stage.say('&0\n좋다, 출발하라!')
        stage.unit(0).setAction(0)
        stage.say('&0\n(방덕이 걱정되는군. 만에 하나 그가…….)')
    stage.effectSound(201, 255)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 333, 93)
    stage.showHead(3, 259, 189)
    stage.showHead(5, 323, 189)
    stage.bottomTxt('조조', False, False, False)
    stage.delay(5)
    stage.head(0).move(291, 109)
    stage.delay(5)
    stage.bottomTxt('번성을 구원하러 달려갔다.', True, False, True)
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            14,
            54,
            80],
        [
            9,
            44,
            64,
            1]])
    stage.draw()
    stage.setStageName('번성  조조군 본진')
    stage.say('&14\n관우가 수계를 써서 강물이 범람하니\n이제 성에 접근하기가 어렵습니다.')
    stage.unit(14).setAction(0, 1)
    stage.say('&14\n주군의 원군을 기다리는 수밖에 없습니다.\n그 밖에는 승산이 없습니다.')
    stage.unit(9).setAction(0, 2)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('&9\n이대로 포기할 수 없다.\n숨이 붙어 있는 한 적과 싸워야 한다.')
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(14).setAction(0, 0)
    stage.say('&14\n방 장군, 너무 성급히 굴지 마시오…….')
    stage.unit(9).setAction(6)
    stage.say('&9\n장군, 어찌 그런 말씀을 하십니까!\n목숨만 아껴서야 어찌 공을 세우겠습니까?')
    stage.unit(9).setAction(0)
    stage.say('&14\n그 말도 옳습니다만…….\n(곤란하군. 승상께서는 방덕을 말리라 하셨는데\n그가 너무 흥분해 있구나…….)\n&14\n……그토록 말씀하시니\n저도 어쩔 도리가 없군요.\n출병합시다!')
    stage.unit(9).setAction(0, 1)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        5,
        25,
        9,
        14])
    gvars[4076] = 0
    gvars[4057] = 38
    stage.setBattlePos([
        [
            0,
            14],
        [
            17,
            13],
        [
            17,
            14],
        [
            10,
            2],
        [
            18,
            1],
        [
            19,
            0],
        [
            11,
            1],
        [
            17,
            1],
        [
            9,
            1],
        [
            1,
            15],
        [
            1,
            13],
        [
            1,
            14],
        [
            1,
            12],
        [
            0,
            13],
        [
            0,
            15]])
    return False


def scene5():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&14\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(9):
        stage.say('&9\n싸움이 아무리 힘겨워도 상관없다.\n이 방영명, 목숨을 걸고 끝까지 싸우겠다!')
    return False

def __dispatch__():
    label('__dispatch_loop')
    if stage.menuVisible():
        name = f"scene{stage.sceneIndex()}"
        if call(name):
            stage.setMenuVisible(False)
            stage.incSceneIdx()
        else:
            return None
    name = f"scene{stage.sceneIndex()}"
    if not hasFunc(name):
        stage.end()
        return None
    call(name)
    stage.incSceneIdx()
    if stage.menuVisible():
        return None
    goto('__dispatch_loop')
