# Source Generated with Decompyle++
# File: R_05.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(18)
    stage.delay(3)
    stage.loadBg(2, 12)
    stage.showUnits([
        [
            0,
            29,
            54,
            1],
        [
            20,
            48,
            41,
            2],
        [
            24,
            54,
            41,
            2],
        [
            1,
            48,
            68],
        [
            22,
            46,
            52,
            3],
        [
            21,
            46,
            58,
            3]])
    stage.draw()
    stage.setEventName('아버지의 죽음')
    stage.setStageName('복양  조조 저택')
    stage.delay(5)
    stage.unit(20).setAction(0, 3)
    stage.say('&20\n오늘은 은거 중이시던 두 현인을 모셔 왔습니다.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('&22\n처음 뵙겠습니다, 조 대인. 곽가, 자는 봉효라 합니다.')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n저도 처음 뵙습니다. 정욱, 자는 중덕이라 합니다.')
    stage.unit(21).setAction(0)
    stage.say('&0\n두 분의 명성은 익히 들었습니다.\n원소와 유표의 초빙을 거절하셨다던데\n그 까닭을 여쭈어도 되겠습니까?')
    stage.unit(22).setAction(6)
    stage.say('&22\n원소는 오만하고 의심이 많아 섬기고 싶지 않습니다.')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n유표도 원소와 크게 다르지 않습니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('&22\n이 난세를 평정할 영웅은 바로 조 공이십니다.\n저희는 조 공의 휘하에 들고자 합니다.')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n하하, 두 사람 다 과찬이네…….')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n좋다!\n그대들도 문약, 공달과 함께 내 군사가 되어 주게.')
    stage.unit(21).setAction(6, 3)
    stage.say('&21\n감사합니다, 주군.')
    stage.unit(21).setAction(0, 3)
    stage.delay(3)
    stage.unit(22).setAction(6, 3)
    stage.say('&22\n저희가 반드시 계책을 마련하겠습니다.')
    stage.unit(22).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하, 문약.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n아버님께 사람을 보내게.')
    stage.unit(0).setAction(5, 1)
    stage.say('&0\n그리고 이 서찰을 전해 주게.')
    stage.say('&20\n대인의 아버님 말씀이십니까?')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음…… 고향에 계신 아버님이 걱정되는군.\n이제 내가 연주목이 되었으니 모셔 오고 싶네.\n&20\n오, 참으로 좋은 생각이십니다.')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n알겠습니다. 즉시 처리하겠습니다.')
    stage.unit(20).setAction(0, 2)
    stage.say('&0\n음……자네에게 맡기겠네.')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 1)
    stage.showUnits([
        [
            158,
            52,
            74,
            0,
            1],
        [
            192,
            52,
            60,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('진류  조숭의 집')
    stage.unit(158).setAction(3)
    stage.say('&158\n뭐라고? 맹덕이 나를 모셔 가겠다고?\n&192\n예. 주군께서 태공 어르신을 모시고 함께 살고 싶어 하십니다.')
    stage.unit(158).setAction(0)
    stage.say('&158\n오, 그러냐? 맹덕이 그리 말하더냐?')
    stage.unit(158).setAction(0, 1)
    stage.say('&158\n허허! 예전에는 모두 맹덕을 헐뜯더니\n이제는 크게 출세했구나.')
    stage.unit(158).setAction(0, 0)
    stage.say('&158\n좋다. 맹덕에게 곧 가겠다고 전하거라.')
    stage.unit(192).setAction(6, 2)
    stage.say('&192\n예.')
    stage.unit(192).setAction(0, 2)
    stage.delay(3)
    stage.unit(192).move(52, 20, 0)
    stage.unit(192).hide()
    stage.unit(158).move(48, 74, 3)
    stage.delay(5)
    stage.unit(158).setAction(0, 2)
    stage.say('&158\n맹덕이 마침내 출세했구나!\n참으로 기쁘다.')
    stage.delay(5)
    stage.effectSound(202, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.delay(5)
    stage.showHead(0, 458, 62)
    stage.showHead(158, 362, 115)
    stage.bottomTxt('조숭은 조조의 출세를 크게 기뻐하며\n즉시 길을 떠나 복양으로 향했다.', False, True, False)
    stage.delay(5)
    stage.head(158).move(394, 86)
    stage.delay(5)
    stage.bottomTxt('그러나 불행히도', True, True, False)
    stage.delay(5)
    stage.bottomTxt('서주를 지나던 중 정체를 알 수 없는 자의 습격을 받아,', True, False, False)
    stage.head(158).hide()
    stage.delay(5)
    stage.bottomTxt('조숭은 그 자리에서 살해당했다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene2():
    stage.loadBg(2, 12)
    stage.showUnits([
        [
            0,
            29,
            54,
            1],
        [
            192,
            46,
            54,
            3],
        [
            20,
            50,
            41,
            2],
        [
            22,
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
            1,
            50,
            68],
        [
            6,
            56,
            68],
        [
            5,
            62,
            68]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('며칠 뒤…….', 3)
    stage.setStageName('복양  조조 저택')
    stage.delay(3)
    stage.unit(1).setAction(2)
    stage.say('&1\n뭐라고! 어르신께서 살해당하셨다고!?')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(0, 2)
    stage.say('&192\n예. 서주 도겸의 부하에게…….')
    stage.unit(6).setAction(6)
    stage.say('&6\n어찌 그런 일이?\n도겸이 어째서 그런 짓을 했단 말입니까!?')
    stage.unit(6).setAction(0)
    stage.delay(3)
    stage.say('&5\n으으음…….')
    stage.delay(5)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n알겠네…수고했네.')
    stage.unit(192).setAction(0, 3)
    stage.delay(3)
    stage.unit(192).setAction(6, 3)
    stage.say('&192\n예.')
    stage.unit(192).setAction(0)
    stage.delay(5)
    stage.unit(1).setAction(0, 3)
    stage.say('&1\n맹덕…….')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n원양! 즉시 군사를 모으게. 서주를 공격하겠다.')
    stage.unit(22).setAction(6)
    stage.say('&22\n잠깐만 기다리십시오!')
    stage.unit(22).setAction(0)
    stage.say('&0\n……무슨 일인가, 봉효?')
    stage.unit(22).setAction(6)
    stage.say('&22\n선친의 원수를 갚으려 출병하는 것은 개인의 효행이나\n군주가 갖추어야 할 덕행은 아닙니다.\n&22\n지금은 참으셔야 한다고 생각합니다.')
    stage.unit(22).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n안 된다! 내가 출진하겠다!')
    stage.unit(0).setAction(0)
    stage.say('&22\n……。')
    stage.unit(21).setAction(6)
    stage.say('&21\n우리 군은 아직 이곳에 기반을 다지지 못했습니다.\n&21\n지금은 전쟁보다 내정을 돌보는 일이 중요합니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('&0\n그대 생각은 너무도 고루하오.\n언제 싸워야 하는지도 모르는구려!\n지금 출병하지 않으면 언제 하겠소?')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n하, 하지만…….')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n주군, 절대 출병하시면 안 됩니다!\n&24\n사사로운 원한으로 대군을 일으키면\n천하를 어지럽혔다는 악명을 후세에 남기게 됩니다.')
    stage.unit(24).setAction(0)
    stage.say('&0\n후세 사람들이 어찌 볼지는 애초에 생각하지 않는다.')
    stage.unit(24).setAction(6)
    stage.say('&24\n예? 명성마저 버리시려는 겁니까!')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(0)
    stage.say('&0\n지금 이 조조에게는 눈앞의 현실뿐이다.\n후세의 본보기가 될 생각은 없다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n뭐, 뭐라고요……?')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n그러면 조정에서 우리를 어찌 보겠습니까?\n황건적을 토벌하며 얻은 신임이 모두 물거품이 될 것입니다!')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n문약!\n&0\n조정에 필요한 것은 나약한 인자가 아니라\n실력을 갖춘 장수일세.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n그러니 서주를 천하 제패의 발판으로 삼겠다!')
    stage.unit(0).setAction(0)
    stage.say('&20\n…………………。')
    stage.unit(0).setAction(17)
    stage.say('&0\n출병하라!!')
    stage.delay(5)
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 6)
    stage.showUnit(161, 70, 55, 3)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('북평  공손찬 저택')
    stage.showUnits([
        [
            32,
            10,
            55,
            1],
        [
            33,
            2,
            52,
            1],
        [
            3,
            2,
            58,
            1]])
    stage.say('&32\n백규!')
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(32),
            54,
            55,
            1],
        [
            stage.unit(33),
            46,
            52,
            1],
        [
            stage.unit(3),
            46,
            58,
            1]])
    stage.say('&161\n오, 유비. 무슨 일인가?\n&32\n백규,\n서주의 도겸 대감께서 구원을 청하셨네.')
    stage.unit(161).setAction(3)
    stage.say('&161\n오, 그렇소?')
    stage.unit(161).setAction(0)
    stage.say('&161\n허나 우리 군은 원소군과 대치하고 있어\n원군을 보낼 여력이 없소.\n&32\n백규, 내가 구원하러 가겠소!')
    stage.unit(161).setAction(3)
    stage.say('&161\n뭐라고?! 자네들이 구원하러 가겠다고? 어째서인가?\n&32\n도겸 대인은 백성을 아끼는 분이십니다.\n죽어 가시는 것을 두고 볼 수 없습니다.')
    stage.unit(161).setAction(0)
    stage.say('&161\n좋다. 구원하러 갈지는 자네 자유일세.\n허나 자네 병력만으로는…….')
    stage.unit(161).setAction(3)
    stage.say('&161\n참, 며칠 전에 대장 조자룡이 내게 의탁했네.\n자네와 함께 보내도록 하지.')
    stage.unit(161).setAction(0)
    stage.say('&161\n반드시 자네에게 힘을 보태 줄 것이네.')
    stage.unit(32).setAction(6)
    stage.say('&32\n감사합니다. 그럼 출발할 준비를 하겠습니다.')
    stage.unit(32).setAction(0, 1)
    stage.delay(3)
    stage.unit(32).setAction(0, 3)
    stage.say('&32\n둘째야, 셋째야. 출발하자!')
    stage.unit(33).setAction(3)
    stage.say('&33\n예!')
    stage.unit(33).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('&3\n조조에게 우리 위력을 보여 주자.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(32),
            18,
            55,
            3],
        [
            stage.unit(33),
            10,
            52,
            3],
        [
            stage.unit(3),
            10,
            58,
            3]])
    stage.unit(33).hide()
    stage.unit(3).hide()
    stage.unit(32).move(5, 55, 3)
    stage.unit(32).hide()
    stage.delay(5)
    stage.effectSound(202, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 387, 72)
    stage.showHead(145, 507, 135)
    stage.bottomTxt('아버지 조숭의 원수를 갚고자,', False, False, False)
    stage.delay(5)
    stage.head(0).move(387, 135)
    stage.delay(5)
    stage.bottomTxt('도겸이 태수로 있는 서주를 공격했다.', True, False, False)
    stage.delay(8)
    stage.showHead(32, 467, 0)
    stage.delay(5)
    stage.bottomTxt('유비 삼형제는 그 모습을 보고,', True, True, False)
    stage.delay(5)
    stage.head(32).move(467, 15)
    stage.delay(5)
    stage.bottomTxt('도겸을 구원하러 나섰다.', True, False, True)
    stage.bottomTxt('이때,', False, True, False)
    stage.delay(5)
    stage.showHead(119, 354, 15)
    stage.delay(5)
    stage.bottomTxt('진류에서 곤경에 처한 여포 앞에\n한 사람이 나타났다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene4():
    stage.loadBg(2, 0)
    stage.showUnits([
        [
            119,
            40,
            54,
            1],
        [
            126,
            50,
            54,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('진류  여포의 저택')
    stage.say('&119\n내 군사가 되고 싶다고?\n&126\n그렇습니다. 저는 진궁, 자는 공대라 합니다.')
    stage.unit(119).move(38, 54, 3)
    stage.say('&119\n정말인가? 지금은 몸 둘 곳조차 없는데.\n&126\n머지않아 생길 것입니다.')
    stage.unit(119).setAction(0, 1)
    stage.say('&119\n뭐라고!?')
    stage.unit(126).setAction(0, 1)
    stage.say('&126\n복양의 조조는 서주 도겸의 부하에게 아버지를 잃자\n전군을 일으켜 서주를 공격했습니다.\n아버지의 원수를 갚겠다는 것이지요.')
    stage.say('&126\n그 틈을 타 복양을 공격한다면…….')
    stage.unit(119).setAction(2)
    stage.say('&119\n너무 비겁한 짓 아닌가?')
    stage.unit(126).setAction(0, 3)
    stage.say('&126\n정의는 우리 편에 있다.')
    stage.unit(119).setAction(0)
    stage.say('&119\n어째서……?')
    stage.say('&126\n조조는 사사로운 원한을 갚으려고 서주를 공격했습니다.\n&126\n복양을 치는 것은 그의 폭행을 막기 위해서입니다.\n게다가 난세에서 살아남으려면\n근거지는 반드시 필요합니다.\n&119\n정말 그런가……?')
    stage.delay(10)
    stage.unit(119).setAction(0, 2)
    stage.say('&119\n자네 말이 옳네…나는 살아야 해.\n&126\n옳습니다.\n&119\n초선을 위해서라도…….\n&126\n예?')
    stage.unit(119).setAction(0, 1)
    stage.say('&119\n아니, 아무것도 아니다.\n좋다! 내 군사가 되어 다오.')
    stage.unit(119).setAction(17, 1)
    stage.say('&119\n복양을 공격하라!')
    stage.delay(5)
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene5():
    model.unitJoin(22, 0, 0)
    model.unitJoin(21, 0, 0)
    if vars[1013]:
        gvars[0] = 21
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
        gvars[0] = 22
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
    stage.bgSound(15)
    stage.delay(3)
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
            6,
            38,
            46,
            1],
        [
            16,
            38,
            40,
            1],
        [
            20,
            60,
            52,
            3],
        [
            22,
            60,
            46,
            3],
        [
            21,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('서주 교외  조조군 본진')
    stage.say('&0\n지금부터 서주를 공격한다!')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        22,
        21])
    gvars[4076] = 0
    gvars[4057] = 5
    stage.setBattlePos([
        [
            0,
            12],
        [
            0,
            11],
        [
            0,
            13],
        [
            1,
            12],
        [
            1,
            11],
        [
            1,
            13],
        [
            1,
            10],
        [
            1,
            14],
        [
            0,
            10],
        [
            0,
            14],
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


def scene7():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출병한다.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n맹덕, 자네 심정은 알겠네만……\n&0\n원양, 아무 말도 하지 말게.')
    if stage.unitClickTest(6):
        stage.say('&6\n아까 무기점에서 사람이 와서\n가죽방패 같은 보조 장비가 들어왔다고 하더군요.\n&6\n앞으로 더 좋은 물건도 들어올 거랍니다.\n&0\n오, 그런가?')
    if stage.unitClickTest(16):
        stage.say('&16\n저는 오직 주군의 명을 따르겠습니다.')
    if stage.unitClickTest(22):
        stage.say('&22\n저는 도사입니다. 제 책략을 쓰면\n적을 불리한 상태에 빠뜨려\n전황을 우리에게 유리하게 만들 수 있습니다.')
    if stage.unitClickTest(21):
        stage.say('&21\n공격 계책은 이 책사에게 맡기십시오…….\n아, 문약 선생도 계셨군요. 하하, 실례했습니다.')
    if stage.unitClickTest(20):
        stage.say('&20\n서주성 서쪽에는 넓은 초원과 평지가 있어\n그곳으로 공격할 수 있습니다.\n&20\n그리고……\n유비의 부대가 도겸을 구원하러 왔다고 합니다.\n&0\n유비라고……?\n&20\n도겸군은 성을 사수할 태세입니다.\n&0\n유비를 기다리는 것이겠지.\n늙은 여우가 잔꾀만 부리는군.')
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
