# Source Generated with Decompyle++
# File: R_32.marshal (Python 3.9)


def scene0():
    if model.ambition() < 16:
        model.initLocalVar()
        if vars[1002]:
            stage.infoTransfer(18, '')
        stage.clsUnit()
        stage.setMenuVisible(False)
        stage.jumpScene(91)
    if model.ambition() >= 16:
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
        stage.setEventName('한중을 공격한다')
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
        stage.say('&57\n음, 음………\n하지만?! 어디로 갈 수 있겠는가?')
        stage.unit(9).setAction(6)
        stage.say('&9\n한중으로 가는 것이 좋겠습니다.')
        stage.unit(9).setAction(0)
        stage.say('&57\n한중…… 장로가 있는 곳인가?\n&58\n그래. 나도 그렇게 생각했네.\n가장 가깝고 조조의 세력권 밖인 곳은\n한중뿐이야.\n&57\n……그런가? 좋다.\n한중으로 간다.')
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
        return True


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
    stage.say('&0\n그렇군……\n하루 종일 이렇게 있는가?\n&19\n그렇습니다.\n&0\n보아하니 명문가의 자제 같은데\n이대로 난세를 보낼 생각인가?\n&19\n때가 오면 저절로 움직일 것입니다.\n때가 오기 전에는 이러고 있을 뿐이지요.\n&0\n그리 천시를 살피면\n언제 움직여야 할지 알 수 있는가?\n&19\n남들이 모르는 일도 압니다.\n천하 만물이 제 손안에 있지요.')
    model.setFace(3)
    stage.say('&0\n오……흥미롭군.\n그렇다면 선생께 천하의 형세를 여쭈어도 되겠소?\n&19\n바람에는 백성들의 슬픔과 원망이 실려 오고\n땅에는 피비린내가 가득하니\n난세는 아직 오래 이어질 것입니다.\n&19\n또한 밤하늘은 천하가 셋으로 나뉠 형세를 그리고 있지요.')
    model.setFace(0)
    stage.say('&0\n난세를 내다볼 수 있다는 것인가?\n흥미롭군!\n삼국이란 또 무슨 말인가?\n&19\n삼국이란 유현덕의 촉,\n손중모의 오, 그리고 조맹덕의 위를 말합니다.')
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
            40,
            2],
        [
            23,
            56,
            40,
            2],
        [
            24,
            62,
            40,
            2],
        [
            21,
            68,
            40,
            2],
        [
            19,
            74,
            40,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.info('열흘 뒤………', 3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n그런가?\n유비가 끝내 서천을 차지했군?')
    stage.unit(20).setAction(6)
    stage.say('&20\n그렇습니다.\n방금 받은 보고입니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n또한 성도를 공격하던 중\n방통이 화살을 맞고 죽었습니다.')
    model.setFace(1)
    stage.say('&0\n뭐라고? 방통이 죽었다고?\n그런가……방통이……\n&24\n예. 게다가 곧이어……\n마초도 유비에게 가담했습니다.')
    stage.unit(24).setAction(0)
    model.setFace(2)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('&0\n뭐라고, 마초!?\n으으음……\n유비가 마초까지 거두다니!')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n그럼 서천의 옛 주인 유장은 어찌 되었는가?')
    stage.unit(20).setAction(6)
    stage.say('&20\n형주로 옮겨 살게 되었습니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n흥! 같은 황실 종친인 유비에게 땅을 빼앗기고\n형주로 쫓겨났다고?')
    stage.unit(23).setAction(6)
    stage.say('&23\n이 일련의 사건이\n유비 본인의 생각이라고는 보지 않습니다.')
    stage.unit(23).setAction(0)
    stage.say('&0\n음, 아마 공명의 수작이겠지?\n유비는 옳고 그름도 가리지 않고 이런 일을 벌일 사람이 아니야.')
    stage.unit(0).setAction(3)
    stage.say('&0\n좋다. 군사들에게 묻겠네.\n앞으로 어찌 움직이는 것이 좋겠는가?')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n손씨는 강동을 삼 대에 걸쳐 다스려\n뿌리가 깊으니 공격하기가 무척 어렵습니다.\n&24\n아직 기반이 얕은 촉을 먼저 공격하고\n동오는 마지막에 상대해야 합니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n허나 그 사이에 있는\n한중의 장로가 까다롭습니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n한중은 촉으로 들어가는 요충지이니\n즉시 공격해야 합니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n저도 같은 생각입니다.\n가장 안전한 방책입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n주군, 저도 모두와 같은 생각입니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n좋다. 한중의 장로를 치겠다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n출발을 준비하라!')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 335, 97)
    stage.showHead(32, 293, 225)
    stage.bottomTxt('유비는 제갈량의 천하삼분지계를 받아들여,', False, False, False)
    stage.delay(5)
    stage.showHead(160, 26, 172)
    stage.delay(8)
    stage.head(32).move(90, 172)
    stage.delay(5)
    stage.bottomTxt('서천의 유장을 공격하여,', True, False, False)
    stage.delay(8)
    stage.bottomTxt('도중에 비록……', True, True, False)
    stage.delay(5)
    stage.showHead(90, 152, 152)
    stage.delay(8)
    stage.head(90).hide()
    stage.delay(5)
    stage.bottomTxt('부군사 방통을 잃었으며', True, False, False)
    stage.delay(5)
    stage.head(160).hide()
    stage.delay(5)
    stage.head(32).move(26, 172)
    stage.delay(5)
    stage.bottomTxt('끝내 서천을 차지했다.', True, False, True)
    stage.showHead(76, 572, 238)
    stage.delay(5)
    stage.bottomTxt('한편 손권은', False, True, False)
    stage.delay(8)
    stage.head(76).move(501, 216)
    stage.delay(5)
    stage.bottomTxt('그는 도읍을 오에서 건업으로 옮기고', True, False, False)
    stage.delay(5)
    stage.bottomTxt('조조와 유비에 맞서겠다는 결의를 드러냈다.', True, True, False)
    stage.delay(10)
    stage.bottomTxt('이 형세를 본 조조는 먼저 유비를 없애기로 결심했다.', True, True, True)
    stage.delay(5)
    stage.bottomTxt('그 첫걸음으로,', True, True, False)
    stage.delay(5)
    stage.showHead(140, 129, 87)
    stage.delay(5)
    stage.head(0).move(193, 87)
    stage.delay(8)
    stage.bottomTxt('바로 장로가 다스리는 한중으로 진군하는 것이었다.', True, False, True)
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
        [
            1,
            44,
            64,
            1],
        [
            15,
            44,
            58,
            1],
        [
            7,
            44,
            52,
            1],
        [
            20,
            64,
            64,
            3],
        [
            19,
            64,
            58,
            3],
        [
            21,
            64,
            52,
            3]])
    stage.draw()
    stage.setStageName('한중  조조군 본진')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 13, [
        15], [
        2,
        10,
        11])
    gvars[4076] = 0
    gvars[4057] = 32
    stage.setBattlePos([
        [
            2,
            5],
        [
            3,
            4],
        [
            1,
            6],
        [
            1,
            5],
        [
            2,
            4],
        [
            0,
            5],
        [
            1,
            4],
        [
            2,
            3],
        [
            0,
            5],
        [
            1,
            3],
        [
            0,
            4],
        [
            0,
            3],
        [
            1,
            2],
        [
            0,
            0],
        [
            0,
            0]])
    return False


def scene4():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 전장의 지형을 알고 있는가?\n&20\n예, 주군!\n전장 전체가 산지라 해도 좋을 정도입니다.\n&20\n적의 성으로 통하는 길은 있으나\n먼저 도중의 관문을 돌파해야 합니다.\n&0\n관문이라? 그렇군.')
    if stage.unitClickTest(19):
        stage.say('&19\n싸우지 않고 적을 굴복시키는 것이\n최상의 전략입니다.')
    if stage.unitClickTest(15):
        stage.say('&15\n장로의 교단은 「오두미교」라던데\n무슨 뜻입니까?\n&20\n하하하.\n신도들이 기도를 받은 사례로 쌀 다섯 말을 바치기에\n「오두미교」라 부르는 것입니다.\n&15\n오!\n문약 군사께서는 역시 아는 것이 많으시군요.')
    if stage.unitClickTest(21):
        stage.say('&21\n이 난세에 제 자리를 지키고 있으니\n장로도 예사 인물은 아닙니다.\n처세에 매우 능한 듯합니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n유비가 서천을 차지하다니\n요즘은 정말 별일이 다 있군.')
    if stage.unitClickTest(7):
        stage.say('&7\n주군, 방금 무기점 주인이 다녀갔습니다.\n새 무기가 들어왔다고 합니다.')
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
