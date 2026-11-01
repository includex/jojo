# Source Generated with Decompyle++
# File: R_08.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    if vars[707] and vars[1001]:
        stage.loadBg(2, 8)
        stage.jumpScene(17)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            119,
            34,
            54,
            1],
        [
            126,
            50,
            54,
            3]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setEventName('호랑이 같은 장수')
    stage.setStageName('복양  여포 저택')
    stage.delay(5)
    stage.unit(119).setAction(3)
    stage.say('&119\n하, 하, 하!\n조조는 지금쯤 무척 분하겠지, 공대?\n&126\n그렇습니다. 허나 주군께 아뢰옵건대 문제가 생겼습니다.')
    stage.unit(119).setAction(0)
    stage.say('&119\n대체 무슨 문제가 생겼는가?\n&126\n……메뚜기 떼가 들끓고 있습니다.')
    stage.unit(119).setAction(3)
    stage.say('&119\n뭐라고? 메뚜기 떼라고!\n&126\n예. 복양 일대의 농작물이 모두 먹혀\n지금은 쌀 한 톨 거둘 수 없습니다.')
    stage.unit(119).setAction(0)
    stage.delay(3)
    stage.unit(119).setAction(0, 2)
    stage.say('&119\n음…….\n&126\n우리를 공격하는 조조군도\n분명 군량이 부족해 곤란을 겪고 있을 테니\n다시 쳐들어오지는 않을 것입니다.')
    stage.say('&126\n잠시 싸움을 멈추고\n다음 수확을 기다리는 수밖에 없겠습니다.')
    stage.unit(119).setAction(0, 1)
    stage.say('&119\n좋다. 어쩔 수 없는 일이군.\n그럼 잠시 군을 쉬게 하며 때를 기다리자.')
    stage.unit(119).setAction(17)
    stage.say('&119\n공대, 전군에 전하게.\n오늘부터 군량을 아껴야 하네.\n어기는 자는 엄벌하겠다!')
    stage.say('&126\n예.')
    stage.effectSound(205, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(119, 416, 63)
    stage.showHead(0, 480, 63)
    stage.showHead(145, 482, 150)
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(5)
    stage.bottomTxt('서주 태수 도겸은 이미 죽었고', True, False, False)
    stage.delay(5)
    stage.head(145).hide()
    stage.delay(5)
    stage.bottomTxt('그의 유언에 따라', True, True, False)
    stage.delay(5)
    stage.showHead(32, 482, 150)
    stage.bottomTxt('유비가 뒤를 이어 서주 태수가 되었다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            1,
            38,
            54,
            1],
        [
            16,
            38,
            46,
            1],
        [
            18,
            38,
            38,
            1],
        [
            20,
            60,
            54,
            3],
        [
            22,
            60,
            46,
            3],
        [
            21,
            60,
            38,
            3]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.delay(3)
    stage.setStageName('복양 교외  조조군 본진')
    stage.say('&0\n봉효, 군량은 얼마나 남았는가?')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n대략 한 달은 버틸 수 있을 것입니다.')
    stage.unit(1).move(40, 54, 1)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n군량 배급을 줄인 탓에\n병사들 사이에서 원망이 나오고 있네.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n주군, 잠시 복양을 떠나\n여남으로 가시면 어떻겠습니까?\n&0\n여남이라고?\n&20\n예. 그곳은 메뚜기 재해를 입지 않아\n군량이 풍족합니다.\n&0\n하지만 백성들에게 강제로 군량을 거둘 수는 없네.\n&20\n여남에는 지금 황건 잔당이 횡포를 부리고 있습니다.\n&20\n우리가 그 산적들을 제거하면\n백성들도 기꺼이 군량을 대 줄 것입니다.\n&20\n조정의 호의도 얻고\n주군의 명망도 높아질 것입니다.\n어찌 생각하십니까, 주군?')
    stage.unit(1).setAction(0, 1)
    stage.say('&1\n그렇지. 군량은 부족하지만\n저런 산적들쯤은 우리 상대가 아니야.')
    stage.unit(1).setAction(3, 1)
    stage.say('&1\n이거야말로 일석이조의 묘책이군.')
    stage.unit(20).setAction(0, 3)
    stage.say('&20\n그뿐만이 아닙니다, 하후 장군.\n어쩌면 일석삼조일 수도 있지요.')
    stage.unit(1).setAction(0, 1)
    stage.say('&1\n뭐라고? 그게 무슨 말인가?\n&20\n주군, 여남의 한 마을에 용맹한 사내가 있다고 합니다.\n사람들이 호치라 부른다더군요.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n그가 합류한다면\n우리 군은 훨씬 강해질 것입니다.\n&0\n오, 호치라고……?')
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다. 전군은 여남으로 진군하라.')
    stage.effectSound(205, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(119, 416, 63)
    stage.showHead(0, 480, 63)
    stage.bottomTxt('여포와 잠시 휴전 중이던 조조는', False, False, False)
    stage.delay(5)
    stage.head(0).move(480, 154)
    stage.head(0).move(403, 154)
    stage.bottomTxt('교착 상태를 깨기 위해 여남으로 향했다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene2():
    stage.bgSound(18)
    stage.delay(3)
    stage.loadBg(2, 30)
    stage.showUnits([
        [
            187,
            54,
            4,
            2],
        [
            188,
            46,
            4,
            2]])
    stage.draw()
    stage.setStageName('초현  산속의 작은 마을')
    stage.unitsMove([
        [
            stage.unit(187),
            54,
            24,
            2],
        [
            stage.unit(188),
            46,
            24,
            2]])
    stage.say('&187\n으악! 살려 줘!!\n&188\n으윽, 저놈은 사람이 아니다! 괴물이다!!')
    stage.showUnit(16, 54, 7, 2)
    stage.unitsMove([
        [
            stage.unit(187),
            54,
            32,
            2],
        [
            stage.unit(188),
            46,
            32,
            2],
        [
            stage.unit(16),
            54,
            15,
            2]])
    stage.showUnit(0, 46, 5, 2)
    stage.unitsMove([
        [
            stage.unit(187),
            54,
            52,
            2],
        [
            stage.unit(188),
            46,
            52,
            2],
        [
            stage.unit(16),
            54,
            35,
            2],
        [
            stage.unit(0),
            46,
            25,
            2]])
    stage.say('&16\n죄 많은 놈들!\n도망치지 마라!')
    stage.unit(0).setAction(17)
    stage.say('&0\n전위, 저들은 모두 도적의 우두머리다.\n하나도 놓치지 말게.\n&16\n제게 맡기십시오! 거기 서라!!')
    stage.showUnit(15, 50, 98)
    stage.unitsMove([
        [
            stage.unit(187),
            54,
            74,
            2],
        [
            stage.unit(188),
            46,
            74,
            2],
        [
            stage.unit(16),
            54,
            57,
            2],
        [
            stage.unit(0),
            46,
            47,
            2],
        [
            stage.unit(15),
            50,
            78,
            0]])
    stage.delay(5)
    stage.unit(15).move(54, 78, 1)
    stage.delay(3)
    stage.unit(15).setAction(0, 0)
    stage.say('&187\n앗, 무슨 짓을 하려는 거냐!?')
    stage.effectSound(38, 1)
    stage.unit(187).setAction(10)
    stage.unit(15).move(46, 78, 3)
    stage.delay(3)
    stage.unit(15).setAction(0, 0)
    stage.say('&188\n노……놓아라! 으악!')
    stage.effectSound(38, 1)
    stage.unit(188).setAction(10)
    stage.unit(16).move(54, 59, 2)
    stage.say('&16\n응? 감히 내 공을 가로채다니?\n이봐! 어서 그놈을 넘겨라.')
    stage.unit(15).setAction(0, 1)
    stage.say('&15\n이놈들은 우리 마을 사람들을 죽였습니다.\n넘겨줄 수 없습니다.')
    stage.unit(16).move(50, 59, 2)
    stage.say('&16\n뭐라고!')
    stage.unit(15).move(50, 78, 1)
    stage.delay(1)
    stage.unit(15).move(50, 65, 0)
    stage.say('&15\n나는 허저다. 저자를 데려가고 싶다면\n실력으로 빼앗아 보아라.')
    stage.unit(16).setAction(14)
    stage.say('&16\n잔말 마라. 이 전위를 건드리다니 간도 크구나!')
    stage.unit(0).setAction(3)
    stage.say('&0\n이런! 전위, 당장 멈추게!')
    stage.unit(0).setAction(0)
    stage.say('&16\n으오오오……!')
    stage.unit(16).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(16).setAction(14)
    stage.say('&15\n이야압……!')
    stage.unit(15).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(15).setAction(14)
    stage.say('&16\n쳐라! 베어라! 죽여라!!')
    stage.unit(16).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(16).setAction(14)
    stage.delay(3)
    stage.unit(16).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(16).setAction(14)
    stage.say('&15\n으오오오!!')
    stage.unit(15).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(15).setAction(14)
    stage.delay(3)
    stage.unit(15).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(15).setAction(14)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n오! 전위와도 막상막하로군!')
    stage.unit(0).setAction(0)
    stage.say('&0\n참,\n이 사내가 문약이 말하던 호치인가?')
    stage.say('&16\n받아라!')
    stage.unit(16).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(16).setAction(14)
    stage.say('&15\n이게 뭡니까!')
    stage.unit(15).setAction(13)
    stage.delay(3)
    stage.effectSound(7, 1)
    stage.unit(15).setAction(14)
    stage.say('&0\n과연 용맹한 장수로군…….')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n전위, 어서 돌아와라!')
    stage.unit(16).setAction(0, 2)
    stage.delay(3)
    stage.unit(16).setAction(0, 0)
    stage.say('&16\n응? 무, 무슨 일이야?')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n돌아오라고 했다.\n저 도적들은 그에게 맡기도록 하라!')
    stage.unit(16).setAction(0, 1)
    stage.say('&16\n빌어먹을, 주군의 명이니\n어쩔 수 없군…….')
    stage.unit(16).setAction(0, 2)
    stage.say('&16\n이봐, 다음에 다시 승부를 겨루자!')
    stage.unit(15).setAction(0)
    stage.say('&15\n그럼 이 사람들은 제가 데려가겠습니다.')
    stage.unit(15).setAction(0, 2)
    stage.unitsMove([
        [
            stage.unit(16),
            50,
            15,
            0],
        [
            stage.unit(0),
            46,
            5,
            0]])
    stage.unit(0).hide()
    stage.unit(16).move(50, 5, 0)
    stage.unit(16).hide()
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            16,
            38,
            54,
            1],
        [
            20,
            60,
            54,
            3]])
    stage.draw()
    stage.info('그날 밤…….', 3)
    stage.setStageName('초현  조조군 본진')
    stage.delay(3)
    stage.unit(16).move(40, 54, 1)
    stage.delay(3)
    stage.unit(16).setAction(0, 2)
    stage.say('&16\n주군, 조금만 더 하면 이길 참이었는데\n어찌하여 막으셨습니까!?')
    stage.say('&0\n전위.\n&16\n예?')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n정말 조금만 더 가면 되는가?')
    stage.unit(16).setAction(0, 1)
    stage.say('&16\n이……이것은……하지만…….')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n……문약, 허저를 부탁하네.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n염려 마십시오.')
    stage.unit(16).setAction(0, 2)
    stage.say('&16\n안 됩니다. 문약 군사께서 어찌 그를 이기겠습니까…….')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.say('&20\n하하, 전위 장군. 괜찮습니다.')
    stage.unit(16).setAction(0, 1)
    stage.say('&16\n……？？')
    stage.unit(0).setAction(3)
    stage.say('&0\n하하하하.')
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 30)
    stage.showUnits([
        [
            181,
            54,
            98],
        [
            182,
            46,
            98]])
    stage.draw()
    stage.info('다음 날…….', 3)
    stage.setStageName('초현  산속의 작은 마을')
    stage.unitsMove([
        [
            stage.unit(181),
            54,
            83,
            0],
        [
            stage.unit(182),
            46,
            83,
            0]])
    stage.say('&181\n아아, 살려 다오……!\n&182\n아악!')
    stage.showUnit(15, 50, 98)
    stage.unitsMove([
        [
            stage.unit(181),
            54,
            68,
            0],
        [
            stage.unit(182),
            46,
            68,
            0],
        [
            stage.unit(15),
            50,
            83,
            0]])
    stage.delay(3)
    stage.unit(15).setAction(3, 0)
    stage.say('&15\n서라! 감히 내 소를 해치다니!\n절대 용서하지 않겠다!')
    stage.unitsMove([
        [
            stage.unit(181),
            54,
            60,
            0],
        [
            stage.unit(182),
            46,
            60,
            0],
        [
            stage.unit(15),
            50,
            75,
            0]])
    stage.say('&15\n서라!')
    stage.unit(15).setAction(3, 0)
    stage.say('&15\n이런, 큰일이다!!')
    stage.effectSound(38, 1)
    stage.unit(15).setAction(15)
    stage.unit(181).setAction(0, 2)
    stage.say('&181\n오, 됐다! 함정에 걸렸다!')
    stage.unit(182).setAction(0, 2)
    stage.say('&182\n잡았다! 잡았어!\n어서 순욱 선생께 보고하자.')
    stage.say('&15\n으윽, 제길!')
    stage.clsUnit()
    return False


def scene5():
    model.unitJoin(15, 0, 0)
    if vars[1013]:
        gvars[0] = 15
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
    return False


def scene6():
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            1,
            38,
            52,
            1],
        [
            16,
            38,
            46,
            1],
        [
            20,
            60,
            52,
            2],
        [
            22,
            60,
            46,
            3],
        [
            21,
            60,
            40,
            3],
        [
            15,
            49,
            52,
            2]])
    stage.draw()
    stage.setStageName('초현  조조군 본진')
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n주군, 허저를 붙잡았습니다.\n&0\n음. 문약, 어서 포박을 풀어 주게!')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n예.\n&15\n어?')
    stage.say('&16\n예!?')
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(20).move(52, 52, 3)
    stage.delay(5)
    stage.unit(20).move(60, 52, 3)
    stage.say('&0\n나는 조조 조맹덕이다. 네가 허저로구나.\n어찌 혼자 도적과 싸우고 있느냐?\n&15\n다른 사람이 없어서 그렇습니다.\n&0\n관아에 도움을 청하지 않았느냐?')
    stage.unit(15).setAction(0, 1)
    stage.say('&15\n지금은 난세 아닙니까…\n관리들이 우리 백성을 지켜 줄 리가요.\n&0\n음…….')
    stage.unit(15).setAction(0, 2)
    stage.say('&15\n더는 아무 말씀 마시고\n어서 제 목을 베십시오.')
    model.setFace(3)
    stage.unit(0).setAction(3, 0)
    stage.say('&0\n하하하, 그대를 죽일 생각은 없네.')
    stage.say('&15\n뭐라고!?')
    model.setFace(0)
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n허저, 나를 위해 일해 주겠는가?\n나는 그 관리들과는 다르네.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n나와 함께 이 혼란한 천하를 평정하세.')
    stage.say('&15\n천하를 평정한다고요? 저 같은 농사꾼을 데리고요?')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n신분은 상관없다. 나는 그대의 무용에 감탄했네.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n어떠한가?')
    stage.unit(15).setAction(11, 2)
    stage.say('&15\n예, 예.\n견마지로를 다하겠습니다.')
    model.setFace(3)
    stage.unit(0).setAction(3, 0)
    stage.say('&0\n하하하, 잘되었군. 그대의 활약을 기대하겠네.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n전위, 허저를 그대에게 맡기겠다.\n군사를 이끌고 싸우는 법을 가르쳐 주게!')
    stage.unit(16).setAction(0, 2)
    stage.say('&16\n예, 예.')
    stage.unit(15).setAction(0, 2)
    stage.delay(3)
    stage.unit(15).move(49, 40, 0)
    stage.delay(1)
    stage.unit(16).setAction(0, 1)
    stage.unit(15).move(38, 40, 1)
    stage.unit(15).setAction(0)
    stage.bgSound(15)
    stage.delay(3)
    stage.say('&0\n문약, 군량 사정은 어떠한가?')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n주군께 아룁니다. 군량은 충분합니다.')
    stage.unit(0).setAction(17, 0)
    stage.say('&0\n좋다! 기세를 몰아 복양을 공격한다.\n이번에는 반드시 여포를 무찌르겠다!')
    stage.unit(0).setAction(0, 0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        16,
        15,
        1])
    gvars[4076] = 0
    gvars[4057] = 8
    stage.setBattlePos([
        [
            2,
            21],
        [
            2,
            19],
        [
            3,
            20],
        [
            4,
            21],
        [
            1,
            18],
        [
            5,
            22],
        [
            1,
            19],
        [
            4,
            22],
        [
            2,
            20],
        [
            3,
            21],
        [
            0,
            20],
        [
            2,
            23],
        [
            1,
            21],
        [
            2,
            22],
        [
            1,
            22]])
    return False


def scene7():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(15):
        stage.say('&15\n전위 장군, 잘 부탁드립니다.\n&16\n오? 그래. 좋은 벗이 되어 보세.')
    if stage.unitClickTest(16):
        stage.say('&16\n역시 순욱 군사께서 대단하십니다.\n&20\n하하하.')
    if stage.unitClickTest(1):
        stage.say('&1\n배불리 먹게 되니 병사들의 사기도 크게 올랐네.')
    if stage.unitClickTest(20):
        stage.say('&20\n주 전장은 복양성의 서남쪽입니다.\n&0\n음.')
    if stage.unitClickTest(22):
        stage.say('&22\n허저 장군은 적병 부대이니\n황무지와 산지에서 활약하실 것입니다.')
    if stage.unitClickTest(21):
        stage.say('&21\n적병대는 지계 책략을 쓸 수 있습니다만\n허저 장군께서 책략을 쓰실 것 같지는 않군요.\n&15\n무……무슨 뜻입니까!\n&21\n아, 아무것도 아닙니다…….')
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
