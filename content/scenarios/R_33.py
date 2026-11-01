# Source Generated with Decompyle++
# File: R_33.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(18)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 44)
    stage.showUnits([
        [
            0,
            52,
            34,
            2],
        [
            1,
            69,
            52,
            3],
        [
            8,
            69,
            58,
            3],
        [
            15,
            69,
            64,
            3],
        [
            13,
            69,
            70,
            3],
        [
            20,
            39,
            52,
            1],
        [
            19,
            39,
            58,
            1],
        [
            23,
            39,
            64,
            1],
        [
            24,
            39,
            70,
            1],
        [
            9,
            52,
            52]])
    stage.draw()
    stage.setEventName('공명의 계책')
    stage.setStageName('한중  임시 승상부')
    stage.say('&0\n방덕, 자네처럼 용맹한 장수가\n여기서 죽는다면 너무도 아까운 일이네.\n나를 위해 일해 주지 않겠는가?\n&9\n장로 대인께서는 어찌 되셨습니까?\n&0\n음, 그와 가족들을 보호하라고 이미 명해 두었네.\n&9\n그렇습니까? 감사합니다, 조 대인.\n허나 제 뜻은 변하지 않았습니다.\n부디 저를 죽여 주십시오.')
    stage.say('&0\n방덕, 내가 원망스러운가?\n그대의 옛 주인 마등을 죽였기 때문인가?')
    stage.unit(9).setAction(6)
    stage.say('&9\n이 방덕은 무정하고 의리 없는 자가 아니오.\n다만 장수로서\n옛 주군께 충의를 다했을 뿐이오.')
    stage.unit(9).setAction(0)
    stage.say('&0\n그런가……?\n&9\n더는 말할 것 없소.\n어서 나를 죽이시오!')
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n닥쳐라!\n그대의 생사는 내가 정한다.\n그대에게 선택권은 없다.')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&0\n너를 죽이지 않겠다.\n내 휘하의 장수가 되어\n나의 패업에 힘을 보태 다오.\n&9\n나를 곁에 두고도 두렵지 않단 말이냐!\n네놈을 죽여 버릴 것이다!\n&0\n오? 방덕은 무예가 뛰어난 장수라 들었는데\n암살 같은 비열한 수단을 쓰지는 않겠지.\n&9\n………\n좋다! 네 부하가 되어 주마.\n네가 어떤 인물인지 똑똑히 지켜보겠다, 조조.')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하.\n원양, 방덕에게 거처를 마련해 주게.')
    stage.unit(1).setAction(0, 0)
    stage.say('&1\n오, 알겠네.')
    stage.unit(1).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&0\n방덕, 내 곁에서 힘써 주게.\n그러면 내 뜻도 알게 될 것이네.')
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
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            32,
            32,
            54,
            1],
        [
            35,
            50,
            41,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('성도  유비의 관저')
    stage.showUnit(182, 90, 54, 3)
    stage.say('&182\n아뢰옵니다.')
    stage.unit(182).move(52, 54, 3)
    stage.delay(3)
    stage.unit(182).setAction(11)
    stage.say('&182\n주군께 아뢰옵니다. 조조가 한중을 공격하여\n이미 점령했습니다.')
    stage.unit(32).setAction(3)
    stage.say('&32\n뭐…… 뭐라고!?\n조조가 한중을 차지했다고?\n이런, 큰일이군.')
    stage.unit(32).setAction(0)
    stage.say('&35\n장로는 어찌 되었느냐?\n조조가 장로를 죽였느냐?')
    stage.say('&182\n아닙니다. 장로 일족은 모두 후대를 받았고\n관직까지 하사받았습니다.\n&35\n그런가. 조조가 장로를 후대했단 말이지?\n과연 조조로군. 인심을 사는 법을 잘 알아.')
    stage.unit(32).setAction(3)
    stage.say('&32\n공명, 감탄하고 있을 때가 아니네.\n한중은 촉의 목구멍이나 다름없으니\n곧 우리의 급소가 아닌가.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(6)
    stage.say('&35\n주군, 염려 마십시오.\n조조가 한중을 차지했어도\n우리 촉을 공격하지는 않을 것입니다.\n&32\n어째서인가?\n조조의 다음 목표는\n틀림없이 우리일 텐데.')
    stage.unit(35).setAction(0)
    stage.effectSound(200, 255)
    stage.say('&35\n그렇습니다.')
    stage.delay(5)
    stage.bgSound(12)
    stage.delay(3)
    stage.say('&35\n그러니 조조의 생각을 바꾸게 해야 한다.')
    stage.say('&32\n마음을 돌리라고……?\n동오를 공격하게 하자는 말인가?\n&35\n그렇습니다. 손권은 아직 합비를 포기하지 않았습니다.\n&35\n동오가 군사를 일으켜 합비를 치게 하면\n조조도 우리를 공격하지 못할 것입니다.\n&32\n하지만 동오가 출병하겠는가?')
    stage.unit(35).setAction(11)
    stage.say('&35\n형주 네 군 가운데 세 군을 손권에게 돌려주면\n반드시 군사를 일으킬 것입니다.\n&32\n형주를 돌려주자는 말인가………')
    stage.unit(35).setAction(0)
    stage.say('&35\n주군, 결단을 내리십시오!\n형주를 지키려 고집하시다가\n촉을 멸망의 위기에 빠뜨려서는 안 됩니다!\n&32\n음, 좋다.\n공명, 이 일은 그대에게 맡기겠네.\n잘 부탁하네.')
    stage.unit(35).setAction(6)
    stage.say('&35\n주군, 염려 마십시오.\n즉시 동오에 사자를 보내겠습니다.')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 51)
    stage.showUnits([
        [
            76,
            53,
            29,
            2],
        [
            193,
            53,
            48,
            0,
            6],
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
            192,
            65,
            48,
            3],
        [
            194,
            65,
            54,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('건업  손권의 저택')
    stage.say('&76\n오, 우리 오가 합비를 공격하기를 바란다고?\n&193\n예.\n&193\n대인께서 허락하시면\n즉시 형주 삼군을 돌려드리겠습니다.\n&76\n음, 좋다.\n다만 돌려받은 뒤에 출병하겠다.\n&76\n그대 나라의 형주 수비군이\n모두 철수한 것을 확인한 뒤\n합비를 공격하면 어떻겠는가?\n&193\n예.\n대인의 뜻대로 하겠습니다.')
    stage.effectSound(200, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 162, 73)
    stage.showHead(140, 98, 73)
    stage.bottomTxt('조조는 장로를 무찌른 뒤', False, False, False)
    stage.delay(5)
    stage.head(140).hide()
    stage.delay(5)
    stage.head(0).move(130, 73)
    stage.delay(5)
    stage.bottomTxt('한중을 점령했다.', True, False, False)
    stage.delay(8)
    stage.showHead(32, 25, 171)
    stage.delay(5)
    stage.bottomTxt('유비는 사방에 위기가 닥쳤음을 느끼고,', True, True, False)
    stage.delay(5)
    stage.showHead(3, 302, 235)
    stage.delay(5)
    stage.head(3).move(263, 193)
    stage.delay(5)
    stage.bottomTxt('형주 삼군을 할양하는 조건으로 동오에 합비 공격을 청했다.', True, False, False)
    stage.delay(10)
    stage.showHead(78, 327, 238)
    stage.showHead(76, 511, 212)
    stage.delay(5)
    stage.bottomTxt('손권은 형주 삼군을 넘겨받았음을 확인한 뒤', True, True, False)
    stage.delay(10)
    stage.showHead(2, 422, 142)
    stage.delay(5)
    stage.bottomTxt('다시 공격한다', True, False, False)
    stage.delay(5)
    stage.head(76).move(486, 183)
    stage.delay(5)
    stage.bottomTxt('장료가 지키는 합비였다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene3():
    stage.loadBg(2, 44)
    stage.showUnits([
        [
            0,
            52,
            34,
            2],
        [
            1,
            69,
            52,
            3],
        [
            8,
            69,
            58,
            3],
        [
            15,
            69,
            64,
            3],
        [
            13,
            69,
            70,
            3],
        [
            20,
            39,
            52,
            1],
        [
            19,
            39,
            58,
            1],
        [
            23,
            39,
            64,
            1],
        [
            21,
            39,
            70,
            1],
        [
            181,
            52,
            52,
            0,
            11]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('한중  임시 승상부')
    stage.say('&0\n동오가 합비를 공격했다고?\n&181\n그렇습니다.')
    stage.unit(21).setAction(6)
    stage.say('&21\n손권이 우리가 한중을 공격하는 틈을 타다니……')
    stage.unit(21).setAction(0)
    stage.say('&0\n이것은 손권의 생각이 아닐 것이다.\n공명이 동오를 부추겨 출병하게 했겠지.\n&0\n우리 발을 묶어 두어\n촉의 안전을 지키려는 것이 목적일 터.\n이 또한 제갈량의 계책인 듯하군.\n&19\n공명 말씀이십니까……?')
    stage.unit(1).setAction(3)
    stage.say('&1\n하지만 장료는 전에도 동오의 공세를 막아 냈으니\n이번에도 지킬 수 있지 않겠는가?')
    stage.unit(1).setAction(0)
    stage.say('&0\n아니, 손권은 반드시 지난 패배를 거울삼을 것이네.\n문원이라도 이번에는 쉬이 이기지 못할 것이야.')
    stage.unit(1).setAction(3)
    stage.say('&1\n지금 원군을 보내고 싶어도\n대군은 꼼짝할 수 없네.')
    stage.unit(1).setAction(0)
    stage.say('&0\n바로 그런 일이 걱정되어\n미리 원군을 준비해 두었다.')
    stage.unit(1).setAction(3)
    stage.say('&1\n원군이라고?\n어디에 있는가?')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(5)
    stage.say('&0\n여기 있네.\n이 서찰이 곧 백만 원군일세.')
    stage.unit(1).setAction(3)
    stage.say('&1\n이보게, 맹덕.\n서신 한 통이 어찌 원군이 된다는 말인가?\n&0\n즉시 이 서신을 문원에게 전하라.\n무슨 일이 있어도 개전 전에 도착해야 한다!\n&181\n예.')
    stage.unit(1).setAction(0)
    stage.say('&1\n맹덕, 대체 무슨 생각인가?')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            2,
            54,
            80],
        [
            10,
            44,
            64,
            1],
        [
            11,
            64,
            64,
            3]])
    stage.draw()
    stage.setStageName('합비  조조군 본진')
    stage.unit(10).setAction(0, 2)
    stage.say('&10\n장 장군, 동오군이 곧 도착합니다.\n즉시 출전해야 합니다.\n&2\n허나……한중으로 보낸 사자가 아직 돌아오지 않았습니다.')
    stage.unit(11).setAction(0, 2)
    stage.say('&11\n장군, 이제 단념하시지요!\n&2\n음, 어쩔 수 없는 일이군.')
    stage.showUnit(181, 54, 22, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n보고드립니다. 소……소인이 돌아왔습니다. 헉……')
    stage.unit(10).setAction(0, 0)
    stage.unit(11).setAction(0, 0)
    stage.say('&11\n오오, 기다리고 있었소!\n&10\n원군이 왔는가!?')
    stage.say('&181\n주군께서 장 장군께 보내신 서찰입니다.\n주군께서는 이 서찰이 백만 원군과 같다 하셨습니다…….\n&10\n백만 원군과 같다고?\n이 서찰 한 장이?\n&2\n좋다. 어디 보자.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(10).setAction(0, 1)
    stage.unit(11).setAction(0, 3)
    stage.unit(181).move(54, 76, 2)
    stage.delay(5)
    stage.unit(181).move(54, 64, 2)
    stage.delay(3)
    stage.unit(11).setAction(0, 2)
    stage.say('&11\n무엇이라 적혀 있습니까?\n&2\n출전하여 적을 보기 전에는 열지 말라고 되어 있소.')
    stage.unit(10).setAction(0, 2)
    stage.say('&10\n뭐라고?\n이토록 위급한 때에\n주군은 대체 무슨 생각이신 겁니까!\n&2\n이렇게 된 이상………\n주군의 명에 따르는 수밖에 없네.')
    stage.unit(2).setAction(17)
    stage.say('&2\n출병하라!')
    stage.unit(2).setAction(0)
    stage.unit(11).setAction(0, 3)
    stage.unit(10).setAction(0, 1)
    stage.unit(181).move(54, 22, 0)
    stage.unit(181).hide()
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        2,
        11,
        10], [
        0])
    gvars[4076] = 0
    gvars[4057] = 33
    stage.setBattlePos([
        [
            14,
            15],
        [
            15,
            16],
        [
            15,
            17],
        [
            14,
            16],
        [
            13,
            15],
        [
            16,
            16],
        [
            14,
            3],
        [
            15,
            3],
        [
            14,
            2],
        [
            15,
            2],
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


def scene5():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&2\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(11):
        stage.say('&11\n참, 방덕 장군께……\n잘 만든 투구가 하나 있다더군.')
    if stage.unitClickTest(10):
        stage.say('&10\n성 주변에는 늪과 샛강이 많습니다.\n물가에서 동오군과 싸우기는 까다롭겠습니다.')
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
