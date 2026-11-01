# Source Generated with Decompyle++
# File: R_46.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 59)
    stage.showUnit(57, 49, 8, 2)
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('사조성의 빛')
    stage.setStageName('장안 교외  산길')
    stage.unit(57).move(49, 18, 2)
    stage.showUnits([
        [
            58,
            45,
            8,
            2],
        [
            9,
            53,
            8,
            2]])
    stage.unitsMove([
        [
            stage.unit(57),
            49,
            45,
            2],
        [
            stage.unit(58),
            45,
            35,
            2],
        [
            stage.unit(9),
            53,
            35,
            2]])
    stage.unit(57).setAction(0)
    stage.unit(58).setAction(0)
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(57).setAction(2)
    stage.say('&57\n분하다. 아버님의 원수를 갚기 직전이었는데!\n&58\n네 마음은 안다.\n허나 이렇게 된 이상\n우선 목숨부터 구해야 한다.')
    stage.unit(57).setAction(0)
    stage.delay(3)
    stage.unit(57).setAction(0, 0)
    stage.say('&57\n음, 음………\n하지만 어디로 갈 수 있겠소?')
    stage.unit(9).setAction(6)
    stage.say('&9\n한중으로 가는 것이 좋겠습니다.')
    stage.unit(9).setAction(0)
    stage.say('&57\n한중……장로에게 가자는 것인가?\n&58\n그래. 나도 그렇게 생각한다.\n여기서 가깝고 조조의 세력이 미치지 않는 곳은\n한중뿐이다.\n&57\n……그런가. 좋다.\n한중으로 가자.')
    stage.unit(57).setAction(0, 2)
    stage.delay(3)
    stage.unit(57).setAction(2)
    stage.say('&57\n언젠가 반드시 아버지의 원수를 갚겠다!')
    stage.unit(57).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(57),
            49,
            95,
            2],
        [
            stage.unit(58),
            45,
            95,
            2],
        [
            stage.unit(9),
            53,
            95,
            2]])
    stage.unit(57).hide()
    stage.unit(58).hide()
    stage.unit(9).hide()
    stage.effectSound(203, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 242, 61)
    stage.showHead(57, 178, 61)
    stage.bottomTxt('조조에게 패한 마초 일행은', False, False, False)
    stage.delay(5)
    stage.showHead(140, 106, 96)
    stage.delay(5)
    stage.bottomTxt('한중의 장로에게 의탁했고', True, False, False)
    stage.delay(5)
    stage.head(57).move(170, 96)
    stage.delay(5)
    stage.bottomTxt('잠시 몸을 의탁할 곳을 얻었다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene1():
    stage.loadBg(2, 28)
    stage.showUnits([
        [
            19,
            68,
            42,
            1],
        [
            181,
            52,
            79,
            1],
        [
            182,
            54,
            46,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.info('한편 조조는 허도로 돌아왔다……', 3)
    stage.setStageName('허도  승상부 앞')
    stage.showUnit(0, 68, 94)
    stage.unit(0).move(68, 65, 0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n수고했네.')
    stage.unit(181).setAction(11)
    stage.say('&181\n아! 주, 주군이십니까.\n&0\n수상한 일은 없느냐?')
    stage.unit(182).setAction(11)
    stage.say('&182\n아닙니다. 이상 없습니다.\n&0\n음.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n……음?\n저 사람은 무엇을 하는 게냐?\n&181\n한 식경 전부터\n저기 서서 하늘만 보고 있습니다.\n&0\n호오……?')
    stage.unit(0).move(68, 50, 0)
    stage.unit(0).setAction(0)
    stage.unit(181).setAction(0)
    stage.unit(182).setAction(0)
    stage.say('&0\n여기서 무엇을 하는가?\n&19\n하늘을 보고 있습니다.\n&0\n하늘을?\n&19\n하늘빛을 보고, 바람 소리를 듣고\n대지의 냄새를 맡고 있지요.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그렇군…….\n하루 종일 그렇게 있는가?\n&19\n그렇습니다.\n&0\n보아하니 명문가의 자제 같은데\n이 난세를 그저 이렇게 보낼 셈인가?\n&19\n때가 오면 저절로 움직이게 될 것입니다.\n그때가 오기 전에는 이럴 수밖에요.\n&0\n하늘을 보고 있으면\n언제 움직여야 할지 알 수 있다는 말인가?\n&19\n남들이 모르는 일도 알 수 있지요.\n천하 만물이 제 손안에 있는 듯합니다.')
    model.setFace(3)
    stage.say('&0\n오…… 흥미롭군.\n그럼 선생께 천하의 형세를 여쭈어도 되겠소?\n&19\n바람이 백성들의 슬픔과 원한을 전하고\n대지에는 피비린내가 가득하니\n난세는 아직 오래 이어질 것입니다.\n&19\n또한 밤하늘에는 천하가 셋으로 갈라질 형세가 그려져 있습니다.')
    model.setFace(0)
    stage.say('&0\n난세의 앞날을 내다볼 수 있다고?\n흥미롭군!\n삼국이라니 무슨 뜻인가?\n&19\n유현덕의 촉나라, 손중모의 오나라,\n그리고 조맹덕의 위나라, 이 세 나라를 말합니다.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n유비의 촉나라라고?\n설마 유비가 서천을 차지하려는 것인가?\n&19\n틀림없습니다.\n머지않아 소식이 전해질 것입니다.')
    stage.say('&0\n그대…… 이름이 무엇인가?\n&19\n사마의라 합니다. 자는 중달입니다.\n&0\n때가 되면 움직인다 했는데\n아직 그때가 오지 않았는가?')
    stage.effectSound(203, 255)
    stage.bgSound(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.say('&19\n아니……때가 되었습니다.\n승상께서 제게 물으실 때가\n바로 제가 움직일 때입니다.')
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n하하하.\n중달, 함께 들어가세.\n그대를 내 군사로 모시고 싶네.')
    stage.unit(19).setAction(6)
    stage.say('&19\n예!')
    model.unitJoin(19, 0, 0)
    if vars[1013]:
        gvars[0] = 19
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
    model.setFace(0)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 329, 99)
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(8)
    stage.showHead(32, 278, 207)
    stage.delay(5)
    stage.bottomTxt('형주의 유비는', True, False, False)
    stage.delay(5)
    stage.bottomTxt('천하를 셋으로 나누려는 제갈량의 구상을 실현하기 위해', True, False, False)
    stage.delay(5)
    stage.showHead(160, 9, 172)
    stage.delay(5)
    stage.bottomTxt('공격 중', True, True, False)
    stage.delay(5)
    stage.head(32).move(154, 177)
    stage.delay(5)
    stage.bottomTxt('유장이 다스리는 촉이었다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('유비군의 진격은 매우 순조로웠고', True, False, False)
    stage.delay(5)
    stage.head(32).move(73, 135)
    stage.delay(5)
    stage.bottomTxt('유장의 본거지 성도가 이제 지척이었다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            90,
            42,
            52,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('성도 근교  유비군 본진')
    stage.say('&32\n후우, 참으로 힘겹구나……\n&32\n성도가 지척인데도\n유장군은 여전히 완강하게 저항하는군.')
    stage.unit(32).setAction(0, 1)
    stage.say('&32\n병사들은 점점 지쳐 가는데\n전황에는 진전이 없군……')
    stage.unit(32).setAction(0, 2)
    stage.delay(3)
    stage.unit(32).setAction(3)
    stage.say('&32\n사원, 형주의 공명에게 원군을 청하세.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(90).setAction(6)
    stage.say('&90\n주군의 말씀이 옳습니다.\n힘만 믿고 무리하게 나아가기보다\n원군을 청해 만전을 기하는 편이 낫습니다.')
    stage.delay(3)
    stage.unit(32).setAction(3)
    stage.say('&32\n좋다. 즉시 공명에게 서찰을 쓰겠다.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(90).setAction(0)
    stage.say('&90\n예……')
    stage.unit(90).setAction(0, 2)
    stage.say('&90\n(간밤에 내 장성 곁에서 요성이 빛났다.)\n&90\n(그것은 죽음의 별이라 불리는 사조성!\n내가 이번 싸움에서 죽게 된다는 뜻인가……?)\n&32\n무슨 일인가, 방통?\n안색이 좋지 않군.')
    stage.unit(90).setAction(0, 1)
    stage.delay(3)
    stage.unit(90).setAction(6)
    stage.say('&90\n예? 별일 아닙니다. 염려 마십시오, 주군.\n&32\n그런가?\n그렇다면 다행이군.\n……사원, 너무 무리하지 말게.\n&90\n예.')
    stage.unit(90).setAction(0)
    stage.say('&90\n(주군께서는 참으로 마음을 헤아려 주시는 분이다.\n……주군을 위해서라도 아직 죽을 수 없다.)')
    stage.unit(90).setAction(0, 2)
    stage.say('&90\n(공명도 틀림없이 사조성을 보았을 것이다.)\n&90\n(원군이 와서 전황이 바뀌면\n나도 이 화를 피할 수 있을지 모른다.)')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(160, 11, 177)
    stage.showHead(32, 75, 138)
    stage.bottomTxt('유비가 보낸 원군 요청 서찰을', False, False, False)
    stage.delay(5)
    stage.bottomTxt('머지않아 형주를 지키던', True, True, False)
    stage.delay(5)
    stage.showHead(35, 279, 208)
    stage.delay(5)
    stage.bottomTxt('제갈량의 주둔지였다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 49)
    stage.showUnit(35, 54, 74)
    stage.draw()
    stage.effectSound(204, 0)
    stage.delay(3)
    stage.setStageName('강릉  유비의 관저')
    stage.say('&35\n음, 음……')
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n이 서신을 보아하니\n주군의 부대가 고전하고는 있으나\n사원이라면 원군까지 필요하지는 않을 것입니다.\n&35\n허나 사원의 장성 곁에 죽음을 알리는 별이 나타났으니\n그 또한 알아차렸겠지요.')
    stage.unit(35).setAction(11)
    stage.say('&35\n사원에게 무슨 일이 생겨서는 안 된다.\n즉시 원군을 보내야 한다……')
    stage.unit(35).setAction(0)
    stage.say('&35\n으윽!!')
    stage.unit(35).setAction(8)
    stage.say('&35\n으윽……크, 큰일이다……\n드, 드디어 왔구나……\n&35\n하, 하필 지금 이때……')
    stage.unit(35).setAction(12)
    stage.say('&35\n으아아아!')
    stage.delay(10)
    gvars[0] = 207
    stage.setUnitAttr(35, 1, gvars[0])
    gvars[2035] = 79
    stage.setFAvatar(35, 89)
    if vars[1002]:
        gvars[4076] = 1
        pvars[290] = 4225232
        stage.varOper(0, 290, 2, 0, 10)
        gvars[4076] = 0
    stage.unit(35).setAction(8)
    stage.say('&35\n……흥.')
    stage.unit(35).setAction(0)
    stage.say('&35\n우선 방통부터 처리해야겠군.')
    stage.unit(35).setAction(17)
    stage.say('&35\n하, 하, 하!')
    stage.effectSound(204, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 3)
    stage.showUnit(19, 56, 69, 2)
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('허도  사마의의 저택')
    stage.say('&19\n어젯밤 서쪽 밤하늘에서\n방통의 장성 곁에 죽음을 알리는 별이 나타났습니다.\n&19\n방통은 유비를 따라 성도를 공격하러 갔다고 합니다.\n&19\n그 별은 방통이 이번 싸움에서 죽으리라는 징조입니다.')
    stage.unit(19).setAction(0, 1)
    stage.say('&19\n허나 공명도 사조성을 보았을 터이니\n위험에 빠진 방통을 그냥 두지는 않을 것입니다.\n&19\n그렇다면………\n유비가 머지않아 촉에 들어간다는 뜻이니\n천하가 셋으로 갈라질 날도 멀지 않았습니다.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            90,
            42,
            52,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('성도 근교  유비군 본진')
    stage.say('&32\n사원, 공명의 답장이 왔네.')
    stage.unit(90).setAction(6)
    stage.say('&90\n오, 정말입니까?\n그럼 원군은 언제 도착합니까?\n&32\n그것이……\n&32\n편지에는 지금 형주의 수비가 약해\n여전히 위험하므로 원군을 보낼 수 없다고 적혀 있네.')
    stage.unit(90).setAction(0)
    stage.say('&90\n뭐라고!\n어찌 그런…….\n&32\n음…….\n허나 이해하지 못할 일도 아니지.\n지금 병력으로 공격하세.')
    stage.unit(90).setAction(6)
    stage.say('&90\n예……')
    stage.unit(90).setAction(0)
    stage.say('&90\n(공명이 내 목숨이 경각에 달린 것을 모를 리 없다……)\n&90\n(지금 위와 오가 형주를 공격할 리도 없는데……)')
    stage.unit(90).setAction(0, 2)
    stage.say('&90\n(그렇다면 공명은 어째서 원군을 보내지 않는가!?)')
    stage.unit(90).setAction(0, 1)
    stage.delay(3)
    stage.unit(90).setAction(6)
    stage.say('&90\n주군……\n내일 싸움은 제가 지휘하겠습니다.\n주군께서는 후방에서 형세를 보아 움직이십시오.\n&32\n음, 좋네.')
    stage.unit(90).setAction(0)
    stage.say('&90\n(나 때문에 주군께 폐를 끼칠 수는 없다.\n내 목숨을 전장에서 한번 걸어 보리라.)')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene6():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(160, 11, 177)
    stage.showHead(32, 139, 138)
    stage.bottomTxt('다음 날,', False, False, False)
    stage.delay(8)
    stage.showHead(90, 75, 138)
    stage.delay(5)
    stage.bottomTxt('유비군의 부군사 방통은', True, True, False)
    stage.delay(5)
    stage.bottomTxt('선봉 부대를 이끌고 성도를 공격했으며', True, False, False)
    stage.delay(8)
    stage.bottomTxt('그러나 낙봉파에서 적의 매복에 걸려', True, True, False)
    stage.delay(5)
    stage.head(90).hide()
    stage.delay(5)
    stage.bottomTxt('봉추는 그 자리에서 목숨을 잃었다.', True, False, True)
    stage.bgSound(13)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            36,
            42,
            52,
            1],
        [
            37,
            42,
            58,
            1],
        [
            46,
            67,
            52,
            3]])
    stage.draw()
    stage.setStageName('성도 근교@유비군 본진')
    stage.unit(32).setAction(3)
    stage.say('&32\n뭐라고!?\n사원이 전사했다고!!')
    stage.unit(37).setAction(0, 0)
    stage.say('&37\n예. 군사는 온몸에 화살을 맞아 피투성이가 되었고\n저희가 도착했을 때는 이미 숨을 거두었습니다.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(9)
    stage.say('&32\n사원…….\n내 사원이 죽었다니…….')
    stage.unit(36).setAction(6)
    stage.say('&36\n군사가 전사하고 우리 군은 패주했으니\n본진마저 앞뒤로 적을 맞아 위태롭다.')
    stage.unit(36).setAction(0)
    stage.say('&32\n뭐라고……?')
    stage.unit(46).setAction(0, 0)
    stage.say('&46\n주군, 형주에 원군을 청하는 수밖에 없습니다!\n&32\n음.\n관평, 즉시 형주에 사신으로 가거라.\n&46\n예.\n&32\n어찌 이런 일이…….\n방통이여…….')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene7():
    stage.loadBg(2, 49)
    stage.showUnits([
        [
            35,
            53,
            74],
        [
            3,
            40,
            55,
            1],
        [
            33,
            40,
            49,
            1],
        [
            34,
            67,
            55,
            3],
        [
            46,
            53,
            55,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('강릉  유비의 관저')
    stage.say('&35\n그런가……사원이 전사했군……\n불세출의 재주를 지니고도 결국……')
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n(하하하.\n아주 손쉽게 처리했군.)\n&46\n군사, 우리 군이 지금 위험에 처했습니다.\n즉시 원군을 보내 주십시오.')
    stage.unit(35).setAction(0, 0)
    stage.say('&35\n알겠습니다.\n즉시 원군을 이끌고 가겠습니다.\n익덕, 자룡, 곧 출발할 준비를 하시오.')
    stage.unit(33).setAction(0, 2)
    stage.delay(3)
    stage.unit(33).setAction(3)
    stage.say('&33\n오!\n큰형님이 위험하시다니 당장 가겠소!')
    stage.unit(33).setAction(0)
    stage.delay(3)
    stage.unit(34).setAction(0, 2)
    stage.delay(3)
    stage.unit(34).setAction(6)
    stage.say('&34\n예.')
    stage.unit(34).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(0, 2)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('&3\n……군사, 나도 형님을 구원하러 가겠소.\n&35\n관 장군께서는 형주에 남아 지켜 주십시오.\n&35\n위와 오가 형주의 빈틈을 노려 공격할 수도 있습니다.\n&3\n……좋소.\n그렇다면 남을 수밖에 없겠구려.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n(앞날을 위해 관우를 형주에 남겨야 한다.\n음……)')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene8():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(160, 11, 177)
    stage.showHead(32, 75, 138)
    stage.showHead(35, 279, 208)
    stage.delay(5)
    stage.bottomTxt('제갈량', False, False, False)
    stage.delay(5)
    stage.head(35).move(75, 218)
    stage.delay(5)
    stage.bottomTxt('군사를 이끌고 유비를 지원했다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('원군이 도착하여 위기에서 벗어난 유비군은', True, True, False)
    stage.delay(5)
    stage.bottomTxt('단숨에 수비에서 공격으로 전환하여', True, False, False)
    stage.delay(5)
    stage.head(160).hide()
    stage.delay(5)
    stage.head(32).move(11, 138)
    stage.delay(3)
    stage.head(32).move(11, 177)
    stage.delay(5)
    stage.bottomTxt('마침내 성도를 점령했다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            20,
            50,
            41,
            2],
        [
            23,
            56,
            41,
            2],
        [
            24,
            62,
            41,
            2],
        [
            21,
            68,
            41,
            2],
        [
            19,
            74,
            41,
            2]])
    stage.draw()
    stage.effectSound(204, 0)
    stage.delay(3)
    stage.info('며칠 뒤……', 3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n그런가. 유현덕이 마침내 촉을 차지했군?')
    stage.unit(20).setAction(6)
    stage.say('&20\n예. 방금 보고를 받았습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n게다가 방통은 난전 중에 목숨을 잃었다 합니다.')
    stage.unit(24).setAction(0)
    model.setFace(1)
    stage.say('&0\n뭐라고?\n봉추 방통이 죽었다고?')
    stage.unit(19).setAction(6)
    model.setFace(0)
    stage.say('&19\n어딘가 석연치 않습니다.\n&0\n오, 중달. 무엇이 이상한가?\n&19\n저는 전에 천상을 살펴\n방통에게 위기가 닥칠 징조를 알고 있었습니다.')
    stage.unit(19).setAction(0)
    stage.say('&19\n공명과 방통 같은 고수들이\n이 사실을 모를 리 없습니다.')
    stage.unit(19).setAction(0, 3)
    stage.say('&19\n그들 또한 재앙을 막고자\n어떤 수단이든 가리지 않을 것입니다……\n&0\n무슨 말을 하려는 것인가?')
    stage.unit(19).setAction(6)
    stage.say('&19\n방통의 죽음에는\n누군가의 손길이 닿았을지도 모릅니다.')
    stage.unit(19).setAction(0)
    stage.say('&19\n방통은 자신의 위험을 깨닫고\n어떻게든 화를 피하려 했습니다.\n하지만 누군가가 그를 가로막았지요…….')
    stage.say('&0\n으음, 흥미로운 추측이군.\n허나 내가 보기에는 지나친 걱정 같네.\n&0\n방통이 유비의 휘하에 든 지 얼마 되지 않았으니\n유비의 신임도 공명만큼 깊지는 않았겠지.\n그의 죽음은 공을 서두르다 빚어진 결과일 것이네.')
    stage.unit(19).setAction(6)
    stage.say('&19\n그럴지도 모르겠습니다……')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.say('&19\n(마음이 몹시 어지럽다.\n무서운 일이 벌어질 듯하구나…….)')
    stage.effectSound(204, 255)
    stage.clsUnit()
    return False


def scene9():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 332, 98)
    stage.showHead(32, 23, 170)
    stage.delay(5)
    stage.bottomTxt('유비', False, False, False)
    stage.delay(5)
    stage.bottomTxt('촉의 광대한 영토를 얻었다.', True, False, False)
    stage.delay(10)
    stage.showHead(76, 569, 231)
    stage.delay(5)
    stage.bottomTxt('한편 손권은', True, True, False)
    stage.delay(5)
    stage.head(76).move(497, 212)
    stage.delay(5)
    stage.bottomTxt('그는 도읍을 오에서 건업으로 옮기고', True, False, False)
    stage.delay(5)
    stage.bottomTxt('조조와 유비에 맞서겠다는 결의를 보였다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('이리하여 조조의 위와', True, True, False)
    stage.delay(5)
    stage.bottomTxt('유비의 촉과', True, False, False)
    stage.delay(5)
    stage.bottomTxt('손권의 오가 천하를 셋으로 나누어 맞서게 되었다.', True, False, True)
    stage.bgSound(-1)
    stage.delay(3)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            20,
            50,
            41,
            2],
        [
            23,
            56,
            41,
            2],
        [
            24,
            62,
            41,
            2],
        [
            21,
            68,
            41,
            2],
        [
            19,
            74,
            41,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n유비의 최근 동향을 알아냈는가?')
    stage.unit(20).setAction(6)
    stage.say('&20\n예, 주군.\n성도를 점령하기 직전에\n마초 일행이 유비에게 귀순했습니다.')
    stage.unit(20).setAction(0)
    model.setFace(2)
    stage.say('&0\n마초까지 거두었다고……? 이 귀 큰 도적놈!\n다른 소식은 없는가?')
    model.setFace(0)
    stage.unit(19).setAction(6)
    stage.say('&19\n촉의 옛 주인 유장은 형주로 옮겨 살고 있습니다.')
    stage.say('&0\n흥, 같은 종친에게도 가차 없군.\n유비도 참으로 빈틈이 없어.\n&19\n아마 공명의 생각일 것입니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n유비는 이제 촉의 넓은 영토를 얻었고\n마초 같은 당대의 영걸까지 얻었다.\n더는 가만히 두고 볼 수 없다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n즉시 출병하여 유비를 토벌한다!\n이견은 없겠지?')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n우리가 먼저 동오를 공격하지 않는 한\n손권도 우리에게 나서지는 않을 것입니다.\n지금 우리를 위협하는 자는 유비뿐입니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n유비가 촉에 들어간 지 오래되지 않아\n아직 민심을 다독이지 못했을 것입니다.\n지금이 공격할 기회입니다!')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n하지만 유비를 공격하려면\n먼저 한중의 장로를 토벌해야 합니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n저도 같은 생각입니다.\n유비 역시 한중을 호시탐탐 노리고 있을 것입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다. 먼저 장로를 토벌한다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.showUnit(17, 90, 55, 3)
    stage.effectSound(200, 255)
    stage.bgSound(17)
    stage.delay(3)
    stage.say('&17\n승상, 제 말을 들어 주십시오.')
    stage.unit(17).move(46, 55, 3)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n장로 대감께서는 천하를 제패하려는 분이 아닙니다.\n&17\n그저 신앙으로 백성을 이끌어 평안을 구할 뿐이지요.\n&17\n승상, 부디 두 나라 사이에 전쟁을 일으키지 마시고\n제가 장로 대감께 항복을 권할 기회를 주십시오.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n그것은 어렵지 않겠습니까.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(0, 1)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n저도 신앙을 좇던 황건 신도의 딸이에요.\n&17\n교파는 다르지만\n사람들을 위한다는 뜻은 다르지 않아요.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n왕윤 대인의 따님이 아니십니까……?\n대체 어찌 된 일입니까?')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n아니요. 의부이신 왕윤 대인은\n제 친아버님이 아니세요.\n친부모님은 두 분 다 태평도의 황건 신도셨어요.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n뭐라고……?')
    stage.unit(23).setAction(0)
    stage.say('&17\n부모님은 만백성을 구한다는 황건의 이상에 감동하셔서\n백성을 핍박하던 관군과 사족에 맞서 싸우셨어요.')
    stage.unit(17).setAction(0, 2)
    stage.say('&17\n하지만 얼마 지나지 않아 황건당은 폭도로 변해\n본래의 이상을 완전히 잃어버렸어요.\n&17\n부모님은 이 무의미한 싸움 속에서\n난을 진압하러 온 관군에게 살해당하셨지요.\n&17\n어렸던 저는 부모님의 유해 곁에서 울 수밖에 없었어요.')
    stage.unit(17).setAction(0, 1)
    stage.say('&17\n그 뒤 관군을 이끌던 왕윤 대인께서 저를 발견하시고\n거두어 길러 주셨습니다.')
    stage.unit(17).setAction(6)
    stage.say('&17\n부모님께서 말씀하신 만백성을 구한다는 이상을\n저는 지금도 가슴에 새기고 있어요.\n&17\n오두미교의 교주 장로 대감께서도\n만백성을 구하고자 하시지요.\n&17\n장로 대감께서 진심으로 백성을 위하신다면\n어찌 전쟁을 일으켜 뭇 생명을 고통에 빠뜨리시겠어요?')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n그렇군요……\n황건의 가르침을 이은 초선이 가면\n장로도 경계를 늦출지 모르겠습니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n장로의 반응을 본 뒤 출병해도 늦지 않습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n좋다.\n초선, 그대에게 맡기겠네.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(0, 3)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n예, 감사합니다, 승상.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(17).move(98, 55, 1)
    stage.unit(17).hide()
    stage.say('&0\n……초선에게 그런 불행한 과거가 있었을 줄이야.')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene10():
    stage.loadBg(2, 31)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('한중 교외  산길')
    stage.showUnits([
        [
            181,
            95,
            56,
            3],
        [
            17,
            95,
            48,
            3]])
    stage.unitsMove([
        [
            stage.unit(181),
            50,
            56,
            3],
        [
            stage.unit(17),
            50,
            48,
            3]])
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(0, 2)
    stage.say('&17\n장로 대감의 영지에 와서야 알았어요.\n백성들이 정말 넉넉하고 편안하게 살고 있더군요.')
    stage.unit(17).setAction(6)
    stage.say('&17\n장로 대인은 과연 명성에 걸맞은 분이시군요.\n어떻게든 설득해서\n한중이 전란을 피하도록 해야겠어요.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(0, 0)
    stage.say('&181\n응?')
    stage.showUnits([
        [
            187,
            95,
            48,
            3],
        [
            188,
            95,
            56,
            3]])
    stage.unitsMove([
        [
            stage.unit(187),
            85,
            48,
            3],
        [
            stage.unit(188),
            85,
            56,
            3]])
    stage.say('&187\n잠깐만 기다리십시오!')
    stage.unit(181).setAction(0, 1)
    stage.say('&181\n응?')
    stage.unit(17).setAction(0, 1)
    stage.say('&17\n(네?)')
    stage.unitsMove([
        [
            stage.unit(187),
            60,
            48,
            3],
        [
            stage.unit(188),
            60,
            56,
            3]])
    stage.unit(187).setAction(0)
    stage.unit(188).setAction(0)
    stage.say('&188\n헉, 헉, 겨우 따라잡았군.')
    stage.unit(17).setAction(6)
    stage.say('&17\n무슨 일이신가요?')
    stage.unit(17).setAction(0)
    stage.say('&187\n그대가 초선 낭자이십니까?')
    stage.unit(17).setAction(6)
    stage.say('&17\n제 이름을 아시나요?\n실례지만 두 분은……')
    stage.unit(17).setAction(0)
    stage.say('&188\n예전에 낭자의 부모님께 은혜를 입었습니다.\n&187\n관군과 싸웠던 그날부터\n행방불명된 낭자를 계속 찾고 있었습니다.')
    stage.unit(17).setAction(3)
    stage.say('&17\n그럼 두 분께서는 제 부모님을 아셨나요?')
    stage.unit(17).setAction(0)
    stage.say('&188\n그렇소. 며칠 전에야 낭자가 조조의 진영에 있음을 알았소.\n&187\n한중이 전란을 겪지 않도록\n장로 대인을 설득하러 간다고 들었소.\n&188\n과연 형님 내외의 훌륭한 따님이로구려.\n&187\n앞으로 우리 둘도 낭자를 돕고 싶은데\n어찌 생각하시오?')
    stage.unit(17).setAction(6)
    stage.say('&17\n그렇군요……\n그럼 소녀가 두 분 장사께 감사드리겠습니다.')
    stage.unit(17).setAction(0)
    stage.say('&188\n이런 기회를 주셔서 감사합니다!\n그럼 즉시 한중으로 가겠습니다.\n&187\n좋아, 가자.')
    stage.unitsMove([
        [
            stage.unit(181),
            5,
            56,
            3],
        [
            stage.unit(17),
            5,
            48,
            3],
        [
            stage.unit(187),
            15,
            48,
            3],
        [
            stage.unit(188),
            15,
            56,
            3]])
    stage.unit(181).hide()
    stage.unit(17).hide()
    stage.unitsMove([
        [
            stage.unit(187),
            5,
            48,
            3],
        [
            stage.unit(188),
            5,
            56,
            3]])
    stage.unit(181).hide()
    stage.unit(181).hide()
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene11():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 334, 98)
    stage.showHead(140, 97, 78)
    stage.showHead(17, 161, 78)
    stage.bottomTxt('며칠 뒤,', False, False, False)
    stage.delay(8)
    stage.bottomTxt('초선은 장로를 설득하는 데 성공했다.', True, True, False)
    stage.delay(5)
    stage.head(140).hide()
    stage.delay(5)
    stage.bottomTxt('장로는 성문을 열고 항복했고', True, False, False)
    stage.delay(8)
    stage.head(17).move(97, 78)
    stage.head(0).move(161, 78)
    stage.delay(5)
    stage.bottomTxt('조조는 한중성에 들어갔다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 44)
    stage.showUnits([
        [
            0,
            53,
            35,
            2],
        [
            20,
            39,
            52,
            1],
        [
            23,
            39,
            58,
            1],
        [
            24,
            39,
            64,
            1],
        [
            21,
            39,
            70,
            1],
        [
            19,
            39,
            76,
            1],
        [
            1,
            69,
            52,
            3],
        [
            6,
            69,
            58,
            3],
        [
            17,
            69,
            64,
            3],
        [
            140,
            53,
            52]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('한중  임시 승상부')
    stage.say('&0\n장로 공, 그대의 결단은 옳았소.')
    stage.unit(140).setAction(6)
    stage.say('&140\n예. 초선 낭자께서 대의를 깨우쳐 주셨습니다.\n승상께서 반드시 한중을 지켜 주시리라 믿습니다.')
    stage.unit(140).setAction(0)
    stage.say('&0\n음, 안정된 생활과 신앙의 자유는\n내가 보장하겠소.\n안심하고 교단의 일을 돌보도록 하시오.')
    stage.unit(140).setAction(6)
    stage.say('&140\n감사합니다, 승상.')
    stage.unit(140).setAction(0)
    stage.say('&140\n승상.\n&0\n음? 무슨 일인가?\n&140\n요즘 천하에……\n전에 없던 요기가 감돌고 있습니다.')
    stage.unit(140).setAction(6)
    stage.say('&140\n부디 조심하십시오.\n그럼 이만 물러가겠습니다.')
    stage.unit(140).setAction(0)
    stage.delay(3)
    stage.unit(140).move(53, 95, 2)
    stage.unit(140).hide()
    stage.showUnit(181, 53, 95)
    stage.delay(8)
    stage.unit(181).move(53, 90, 0)
    stage.unit(181).setAction(0)
    stage.say('&181\n주군, 한중의 대장 방덕 장군이 오셨습니다.\n&0\n음, 모셔 오너라.')
    stage.unit(181).move(53, 95, 2)
    stage.unit(181).hide()
    stage.delay(10)
    stage.showUnits([
        [
            9,
            53,
            90],
        [
            181,
            50,
            96],
        [
            182,
            56,
            96]])
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(9),
            53,
            48,
            0],
        [
            stage.unit(181),
            50,
            54,
            0],
        [
            stage.unit(182),
            56,
            54,
            0]])
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n방덕, 오랜만일세.')
    stage.unit(0).setAction(0)
    stage.say('&0\n그대의 사정은 모두 들었네.\n병으로 한중에 남게 되어\n마초의 출정에 함께하지 못했다지?\n&9\n내 목이 탐나는 것 아니오?\n긴말 말고 어서 죽이시오!\n&0\n내가 어찌하여 그대를 죽여야 하는가?\n&9\n나는 지난날 그대의 적이었으니\n내 목을 베는 것도 당연한 일이오.\n&0\n그대를 죽일 뜻은 없네.\n방덕, 살아서 나를 위해 힘써 주게.\n&9\n……그럴 수는 없소.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그대의 옛 주인 마등을 죽였기 때문인가?\n&9\n그렇소. 지금도 그대 목숨을 노리고 있소!\n차라리 어서 나를 죽이시오!')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n아니, 너를 이대로 죽게 둘 수는 없다.\n내 친위대를 이끌며\n언제나 내 곁을 지켜 다오.\n&9\n나를 우롱하는 것이냐!?\n기회를 보아 네 목숨을 노릴지도 모른다!')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n기회를 엿보아 나를 죽이겠다는 것인가?\n&9\n그렇소. 그것이 난세의 도리요.')
    model.setFace(3)
    stage.say('&0\n오, 난세의 도리라?\n말은 참 쉽게 하는군.\n&9\n뭐라고!')
    model.setFace(0)
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n내 말은 여기까지다.\n무슨 생각을 하든\n이제 그대에게 선택의 여지는 없다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n방덕을 친위대의 막사로 데려가라.')
    stage.unit(0).setAction(0)
    stage.say('&181\n예.')
    stage.unitsMove([
        [
            stage.unit(181),
            50,
            48,
            1],
        [
            stage.unit(182),
            56,
            48,
            3]])
    stage.delay(3)
    stage.unit(9).setAction(0, 2)
    stage.say('&9\n으으……')
    stage.unit(9).move(53, 51, 2)
    stage.unit(9).setAction(0)
    model.unitJoin(9, 0, 0)
    if vars[1013]:
        gvars[0] = 9
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
    stage.setJoinEquip(9, -1, 0, -1, 0, 12)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(9),
            53,
            98,
            2],
        [
            stage.unit(181),
            50,
            95,
            2],
        [
            stage.unit(182),
            56,
            95,
            2]])
    stage.unit(181).hide()
    stage.unit(182).hide()
    stage.unit(9).hide()
    stage.delay(10)
    stage.showUnit(183, 53, 95)
    stage.unit(183).move(53, 90, 0)
    stage.unit(183).setAction(0)
    stage.say('&183\n주군, 촉 국경에 있는 조홍 장군의 보고입니다.\n유비군이 정군산을 공격하기 시작했습니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n유비군이 정군산을 공격한다고?')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n틀림없이 공명의 생각이겠지요.\n촉군의 움직임이 참으로 빠릅니다……')
    stage.unit(19).setAction(0)
    stage.say('&0\n조홍 혼자서는 이기기 어려울 것이다.\n즉시 군사를 보내 구원해야 한다.\n&0\n하후연!')
    stage.unit(6).setAction(0, 0)
    stage.say('&6\n오!\n&0\n자네가 선봉을 맡아 즉시 정군산으로 가게.\n장합과……방덕을 부장으로 삼겠네.')
    stage.unit(6).setAction(6)
    stage.say('&6\n방덕이라고? 이봐, 장합이라면 몰라도\n나는 방덕을 믿을 수 없네.\n촉군 진영에 마초가 있다는 것을 잊지 말게!')
    stage.unit(6).setAction(0)
    stage.say('&0\n방덕은 걱정하지 말게.\n&6\n……알겠습니다.\n그럼 먼저 준비하러 가겠습니다.\n묘재, 이만 물러갑니다!')
    stage.unit(0).setAction(17)
    stage.say('&0\n음, 주력의 정비가 끝나면 나도 출발하겠다.\n내가 도착할 때까지 무슨 일이 있어도 지켜 내라!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(6).move(75, 58, 1)
    stage.delay(3)
    stage.unit(6).move(75, 95, 2)
    stage.unit(6).hide()
    stage.unit(183).move(53, 95, 2)
    stage.unit(183).hide()
    stage.delay(10)
    stage.effectSound(200, 255)
    stage.bgSound(18)
    stage.delay(3)
    stage.showUnit(181, 53, 95)
    stage.unit(181).move(53, 90, 0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군! 조창 공자께서 오셨습니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n조창이라고?\n북방 이민족의 반란을 진압하러 가지 않았느냐……')
    stage.unit(0).setAction(0)
    stage.say('&0\n좋다. 들여보내라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(53, 95, 2)
    stage.unit(181).hide()
    stage.delay(3)
    stage.showUnit(4, 53, 95)
    stage.unit(4).move(53, 52, 0)
    stage.unit(4).setAction(0)
    stage.delay(3)
    stage.unit(4).setAction(6)
    stage.say('&4\n아버님, 평안하셨습니까.')
    stage.unit(4).setAction(0)
    stage.say('&0\n조창, 어찌 한중까지 왔느냐?\n반란 진압은 어찌 되었느냐?')
    stage.unit(4).setAction(6)
    stage.say('&4\n그곳의 반란을 진압하고\n아버님께 보고드리러 왔습니다.\n&0\n그런가……? 좋다, 조창.\n반란군은 그리 강하지 않았을 테니\n이제 내 정규군에 합류하거라.\n&4\n예, 아버님.')
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
    stage.clsUnit()
    return False


def scene12():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 153, 63)
    stage.bottomTxt('조홍', False, False, False)
    stage.delay(5)
    stage.showHead(12, 83, 74)
    stage.delay(5)
    stage.bottomTxt('위와 촉의 국경을 지키는 정군산이었다.', True, False, False)
    stage.delay(8)
    stage.showHead(32, 8, 178)
    stage.delay(5)
    stage.bottomTxt('유비군이', True, True, False)
    stage.delay(5)
    stage.head(32).move(83, 154)
    stage.delay(5)
    stage.bottomTxt('군사를 일으켜 정군산을 공격하자', True, False, False)
    stage.delay(10)
    stage.bottomTxt('조조', True, True, False)
    stage.delay(5)
    stage.head(0).hide()
    stage.showHead(6, 153, 63)
    stage.delay(5)
    stage.bottomTxt('즉시 하후연을 보내', True, False, False)
    stage.delay(5)
    stage.head(6).move(147, 114)
    stage.delay(5)
    stage.bottomTxt('지원한다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            35,
            42,
            52,
            1],
        [
            33,
            42,
            58,
            1],
        [
            34,
            42,
            64,
            1],
        [
            36,
            67,
            52,
            3],
        [
            57,
            67,
            58,
            3],
        [
            37,
            67,
            64,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.info('바로 그때…….', 3)
    stage.setStageName('정군산  유비군 본진')
    stage.say('&32\n공명, 관우에게서 서신이 왔네.')
    stage.unit(35).setAction(6)
    stage.say('&35\n운장 장군의 편지라고요?\n내용은 무엇입니까?')
    stage.unit(35).setAction(0)
    stage.say('&32\n형주를 지키기보다는\n전선으로 나가 싸우고 싶다는군.')
    stage.unit(35).setAction(6)
    stage.say('&35\n하지만 운장 장군께서 형주를 지켜 주셔야\n우리가 안심하고 진군할 수 있습니다.')
    stage.unit(35).setAction(0)
    stage.say('&32\n나도 알고 있네.\n운장 때문에 참으로 난처하군.')
    stage.unit(32).setAction(0, 0)
    stage.say('&32\n허나 운장의 심정을 알기에\n둘째의 바람을 들어주고 싶네.')
    stage.unit(35).setAction(6)
    stage.say('&35\n하지만……')
    stage.unit(32).setAction(0, 2)
    stage.say('&32\n차라리 내가 돌아가 형주를 지키고\n운장이 조조군과 싸우도록 하면 어떻겠는가?')
    stage.unit(35).setAction(0)
    stage.say('&35\n……알겠습니다.\n주군께서 그리 말씀하시니\n운장 장군을 오시게 하십시오.')
    stage.unit(32).setAction(3)
    stage.say('&32\n오, 고맙네. 공명.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(6)
    stage.say('&35\n다만 한 가지 조건이 있습니다.\n형주는 위와 오에 접한 위험한 지역이자\n결코 잃어서는 안 될 전략적 요충지입니다.')
    stage.unit(35).setAction(0)
    stage.say('&35\n그러니 익덕 장군과 함께 가십시오.\n&32\n좋네.\n그렇다면 마음이 한결 놓이겠군.\n셋째, 네 생각은 어떠냐?')
    stage.unit(33).setAction(3)
    stage.say('&33\n물론이지요!\n둘째 형님은 싸울 일이 없어서\n진작 몸이 근질거렸을 겁니다!')
    stage.unit(33).setAction(0)
    stage.say('&32\n그렇다면 즉시 출발하겠네.\n공명, 이곳은 자네에게 맡기겠네.')
    stage.unit(35).setAction(6)
    stage.say('&35\n예. 안심하십시오, 주군.')
    stage.unit(35).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(11)
    stage.say('&35\n(음, 이 정도면 되겠군……)')
    stage.effectSound(200, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(12, 83, 74)
    stage.showHead(6, 147, 114)
    stage.showHead(32, 83, 154)
    stage.showHead(3, 277, 238)
    stage.bottomTxt('유비', False, False, False)
    stage.delay(5)
    stage.head(32).move(83, 234)
    stage.showHead(35, 83, 154)
    stage.delay(5)
    stage.bottomTxt('한중의 싸움을 제갈량에게 맡기고,', True, False, False)
    stage.delay(5)
    stage.head(32).move(213, 238)
    stage.delay(5)
    stage.bottomTxt('장비를 데리고 형주로 돌아갔다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene13():
    stage.loadBg(2, 37)
    if vars[311]:
        stage.bgSound(19)
        stage.delay(3)
        stage.showUnit(9, 63, 54, 1)
        stage.draw()
        stage.setStageName('한중  방덕의 거처')
        stage.delay(5)
        stage.say('&9\n내가 배신할 수도 있음을 알면서\n출전을 명하다니……\n조조의 생각을 도무지 알 수 없구나……')
        stage.delay(5)
        stage.showUnit(23, 20, 54, 1)
        stage.delay(5)
        stage.say('&23\n방덕 장군, 잠시 시간을 내주겠나?')
        stage.unit(9).setAction(0, 3)
        stage.say('&9\n음?')
        stage.unit(9).move(50, 54, 3)
        stage.unit(9).setAction(0)
        stage.delay(3)
        stage.say('&9\n가후 선생이시군요.\n무슨 가르침을 주시려는 겁니까?')
        stage.unit(23).move(40, 54, 1)
        stage.unit(23).setAction(0)
        stage.delay(3)
        stage.unit(23).setAction(6)
        stage.say('&23\n방덕 장군, 출전하시기 전에\n반드시 드려야 할 말씀이 있습니다.')
        stage.unit(23).setAction(0)
        stage.say('&9\n대체 무슨 일이오?')
        stage.unit(23).setAction(6)
        stage.say('&23\n제 개인적인 일입니다.\n장수들이 오기 전에\n이야기나 나누며 시간을 보내고 싶군요.')
        stage.unit(23).setAction(0)
        stage.say('&23\n저는 조조군의 군사로서\n주군께 깊은 신임을 받고 중용되고 있으나\n본래는 주군의 적이었습니다.\n&9\n그 사실은 이미 알고 있소.\n완성 태수 장수를 섬기지 않았소?\n&23\n그렇습니다.\n조조군과 두 번 싸웠고…….\n더구나…….\n&23\n주군의 가족까지 죽게 했지요.')
        stage.unit(9).setAction(6)
        stage.say('&9\n뭐라고요!? 그런 과거가 있는데도\n조조가 군사를 맡겼을 뿐 아니라\n그토록 신임한단 말입니까!?')
        stage.unit(9).setAction(0)
        stage.say('&23\n음.\n주군께서는 지금껏 그 일로 저를 책망하신 적이 없습니다.')
        stage.unit(23).setAction(0, 2)
        stage.say('&23\n천하를 평정할 대업을 위해\n사사로운 원한에 매이지 않으시는 것이겠지요.\n&9\n조조 대인께서는 사사로운 원한으로 싸우시는 것이 아니라\n오직 천하의 패업을 위해 사시는 분이란 말인가?')
        stage.unit(23).setAction(0, 1)
        stage.say('&23\n음, 주군의 도량은 드넓은 하늘 같아\n저희 같은 자들이 헤아릴 수 없습니다.')
        stage.unit(9).setAction(6)
        stage.say('&9\n……그랬군요.\n참으로 부끄럽기 그지없습니다!')
        stage.unit(9).setAction(0)
        stage.say('&9\n가후 선생, 마음을 정했습니다.\n앞으로 충심으로 대업을 위해 싸우겠습니다!')
        stage.unit(9).setAction(0, 2)
        stage.say('&9\n(마등 주군, 용서해 주십시오.\n조조에게는 분명 남다른 점이 있으니\n원수를 갚지 못하는 이 영명을 용서하십시오.)')
        stage.say('&23\n방덕 장군……')
        stage.delay(10)
        stage.bgSound(-1)
        stage.clsUnit()
    if not vars[311]:
        stage.bgSound(19)
        stage.delay(3)
        stage.showUnit(9, 63, 54, 1)
        stage.draw()
        stage.setStageName('한중  방덕의 거처')
        stage.say('&9\n내가 배신할 수도 있음을 알면서\n출전을 명하다니……\n조조의 생각을 도무지 알 수 없구나……')
        stage.delay(5)
        stage.showUnit(21, 20, 54, 1)
        stage.delay(5)
        stage.say('&21\n방덕 장군, 잠시 시간을 내주겠나?')
        stage.unit(9).setAction(0, 3)
        stage.say('&9\n음?')
        stage.unit(9).move(50, 54, 3)
        stage.unit(9).setAction(0)
        stage.delay(3)
        stage.say('&9\n정욱 선생이시지요.\n무슨 가르침을 주시려 합니까?')
        stage.unit(21).move(40, 54, 1)
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('&21\n방덕 장군, 출전하시기 전에\n반드시 드려야 할 말씀이 있습니다.')
        stage.unit(21).setAction(0)
        stage.say('&9\n대체 무슨 일이오?')
        stage.unit(21).setAction(6)
        stage.say('&21\n마등 장군에 관한 일입니다……\n마등을 죽인 사람은 바로 접니다.')
        stage.unit(21).setAction(0)
        stage.say('&9\n……!?\n&21\n당시 주군께서는 목숨이 위태로운 순간에도\n마등 장군을 죽일 생각을 하지 않으셨네.')
        stage.unit(21).setAction(6)
        stage.say('&21\n마등 장군을 죽인 것은 모두 제가 독단으로 벌인 일입니다.\n모든 책임은 이 정욱에게 있습니다.\n&9\n어찌하여 주군 마등을 죽였는가?')
        stage.unit(21).setAction(0)
        stage.say('&21\n저는 주군의 군사이니\n주군을 위협하는 요소를 없앨 책임이 있습니다.\n&21\n마등은 주군을 해치려는 모의에 가담했습니다.\n그러니 살려 둘 수 없었습니다.\n장군께서도 이 정도 이치는 아실 것입니다.\n&9\n오랫동안 싸워 왔으니\n그 정도 이치는 알고 있소.\n&9\n조 승상께서 마등 주군의 죽음을 말하지 않으신 것은\n혼자 그 죄명을 짊어지시려던 것이오?')
        stage.unit(9).setAction(0, 2)
        stage.say('&9\n난세에 살아남아 패자가 되고자\n사사로운 원한에 매이지 않는 상책을 택하셨군.\n&9\n선악을 따지지 않고 모두 짊어지는 것이\n조 승상의 참모습이란 말인가?\n&9\n그렇다면 경계 없이 이 방덕을 받아 주신 것도\n부하를 신뢰하신다는 증거겠지?\n&9\n좋다. 앞으로는 나도 힘을 다해\n대업을 위해 싸우겠다.')
        stage.unit(21).setAction(6)
        stage.say('&21\n방덕 장군……')
        stage.delay(10)
        stage.bgSound(-1)
        stage.clsUnit()
    return False


def scene14():
    stage.bgSound(15)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            6,
            54,
            80],
        [
            7,
            44,
            64,
            1],
        [
            9,
            44,
            58,
            1]])
    stage.draw()
    stage.setStageName('한중@조조군 본진')
    stage.say('&6\n주력이 도착할 때까지\n최대한 촉군의 공세를 막아야 한다.\n모두 끝까지 버텨라!')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        6,
        7,
        12,
        9], [
        0])
    gvars[4076] = 0
    gvars[4057] = 46
    stage.setBattlePos([
        [
            23,
            16],
        [
            22,
            17],
        [
            6,
            4],
        [
            4,
            6],
        [
            12,
            16],
        [
            19,
            26],
        [
            21,
            16],
        [
            23,
            18],
        [
            12,
            15],
        [
            19,
            25],
        [
            0,
            0],
        [
            0,
            0],
        [
            0,
            0],
        [
            0,
            0],
        [
            0,
            0]])
    return False


def scene15():
    if stage.battleTest():
        stage.say('&6\n출전하라!')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(7):
        stage.say('&7\n방덕 장군의 투구는 참으로 훌륭하군요.')
    if stage.unitClickTest(9):
        stage.say('&9\n전장 전체가 산지라\n지형이 매우 복잡하다.\n이동할 때 각별히 조심하라.')
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
