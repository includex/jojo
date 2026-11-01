# Source Generated with Decompyle++
# File: R_10.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            149,
            49,
            67],
        [
            1,
            38,
            54,
            2,
            6],
        [
            16,
            38,
            48,
            2,
            6],
        [
            15,
            38,
            42,
            2,
            6],
        [
            20,
            60,
            54,
            2,
            6],
        [
            22,
            60,
            48,
            2,
            6],
        [
            21,
            60,
            42,
            2,
            6],
        [
            0,
            49,
            54,
            2,
            6]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('호가의 선율')
    stage.setStageName('장안 근교  조조군 본진')
    stage.say('&149\n조 경, 그대가 짐을 구해 주었구려. 고맙소.\n&0\n폐하, 예를 차리실 것 없습니다. 신하로서 마땅히 할 일이옵니다.\n&149\n조 경, 앞으로도 짐을 도와주시오.\n&0\n예, 폐하!')
    stage.unit(149).move(56, 67, 1)
    stage.delay(1)
    stage.unit(149).move(56, 24, 0)
    stage.delay(1)
    stage.unit(149).move(49, 24, 3)
    stage.delay(1)
    stage.unit(149).move(49, 16, 0)
    stage.unit(149).hide()
    stage.unit(0).setAction(0, 2)
    stage.unit(1).setAction(0, 1)
    stage.unit(16).setAction(0, 1)
    stage.unit(15).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(22).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).move(56, 54, 1)
    stage.delay(1)
    stage.unit(0).move(56, 67, 2)
    stage.delay(1)
    stage.unit(0).move(49, 67, 3)
    stage.delay(1)
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n저자가 천자인가……?\n제 능력은 아무것도 없으면서\n거드름만 피우는군…….\n&0\n천자란 그런 존재일세, 원양.\n그러하니 천자가 될 수 있는 것이지.')
    stage.unit(1).setAction(0, 1)
    stage.say('&1\n흥…….')
    stage.say('&0\n그래도 마침내 소원을 이루었군.')
    stage.unit(0).setAction(3, 0)
    stage.say('&0\n모든 장병이 힘써 준 덕분에 이길 수 있었다.')
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n그나저나 맹덕, 서황은 참으로 잘 싸우더군.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n음, 지금은 소수의 병사를 이끌고\n근처에 진을 친 모양이군…….')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n저런 호걸을 이대로 죽게 두기에는\n너무도 아깝구나…\n우리 군에 들이고 싶군.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n주군, 서황의 일은 제게 맡겨 주십시오.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n문약, 좋은 계책이 있는가?')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n있습니다. 사람을 보내 귀순을 설득할 수 있습니다.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.say('&20\n백녕, 들어오게.')
    stage.delay(3)
    stage.showUnit(25, 49, 16, 2)
    stage.say('&25\n실례하겠습니다.')
    stage.unit(1).setAction(0, 1)
    stage.unit(25).move(49, 54, 2)
    stage.delay(3)
    stage.unit(25).setAction(6)
    stage.say('&25\n조 공을 뵙습니다. 만총이라 하며 자는 백녕입니다.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n지략이 뛰어난 인물이라\n기회를 보아 주군께 천거하고 싶었습니다.\n&20\n게다가 서황과 우의가 깊습니다.\n&20\n서황을 설득할 사람으로는 이보다 나은 이가 없습니다.\n&25\n제게 맡겨 주십시오.\n&0\n좋다. 백녕, 자네에게 맡기겠네.\n일을 성사시키면 반드시 중히 쓰겠네.\n&25\n감사합니다, 대인. 즉시 서황의 진영으로 가겠습니다.\n&0\n음, 자네만 믿겠네.')
    stage.unit(25).setAction(0)
    stage.delay(3)
    stage.unit(25).move(49, 16, 0)
    stage.unit(25).hide()
    stage.delay(3)
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n백녕이라면 실패하지 않을 것입니다.\n서황은 반드시 우리 군에 들어올 것입니다.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 18)
    stage.showUnits([
        [
            13,
            35,
            55,
            1],
        [
            25,
            48,
            55,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('그날 밤…….', 3)
    stage.setStageName('장안 근교  서황군 본진')
    stage.say('&13\n백녕, 내게 조조에게 항복하라는 건가?\n&25\n그렇네.')
    stage.unit(25).setAction(0, 2)
    stage.say('&25\n조 공께서는 지난 일을 묻지 않고 자네를 중용하겠다 하셨네.')
    stage.unit(25).setAction(0, 3)
    stage.say('&25\n나와 함께 조조를 섬기는 게 어떻겠나?')
    stage.unit(13).setAction(0, 2)
    stage.delay(3)
    stage.unit(13).move(35, 59, 2)
    stage.say('&13\n하지만……\n저는 할 수 없습니다.')
    stage.unit(25).move(48, 59, 2)
    stage.delay(3)
    stage.unit(25).setAction(0, 3)
    stage.say('&25\n공명, 지금 자네가 섬기는 주군을 보게.')
    stage.unit(25).setAction(0, 2)
    stage.say('&25\n제 쾌락만을 좇을 뿐\n백성의 고통은 조금도 돌아보지 않는 자야.')
    stage.unit(25).setAction(0, 3)
    stage.say('&25\n그런 자에게 충성을 다할 필요가 있겠나?\n&13\n…….')
    stage.unit(25).setAction(0, 1)
    stage.delay(3)
    stage.unit(25).move(52, 59, 1)
    stage.say('&25\n까닭도 모른 채 그런 자에게 목숨을 바치면\n천하의 웃음거리가 될 걸세.')
    stage.unit(25).setAction(0, 3)
    stage.say('&25\n공명, 그대의 무용을 펼쳐\n더 뜻깊은 일을 위해 싸워야 하지 않겠는가?')
    stage.unit(13).setAction(0, 1)
    stage.say('&13\n……좋소. 조조에게 항복하겠소.')
    stage.unit(25).move(48, 59, 3)
    stage.say('&25\n오, 마침내 마음을 정했군!\n&25\n그렇다면 지체할 수 없네!\n내일 아침 일찍 나와 함께 조조의 진영으로 가세.\n&13\n좋네.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(18)
    stage.delay(3)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            20,
            60,
            54,
            3],
        [
            22,
            60,
            48,
            3],
        [
            25,
            45,
            54,
            2],
        [
            13,
            53,
            54,
            2,
            6]])
    stage.draw()
    stage.setStageName('장안 근교  조조군 본진')
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n공명, 마침내 왔구나.\n그대가 와 주어 참으로 기쁘네.\n&13\n앞으로 대감을 위해 충성을 다하겠습니다.\n간뇌가 땅에 쏟아진다 한들 아끼지 않겠습니다.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n음, 백녕. 이 일은 참으로 잘 처리했네.')
    stage.unit(25).setAction(6)
    stage.say('&25\n과찬이십니다, 주군.')
    stage.say('&0\n두 사람 다 피곤하겠지.\n오늘은 푹 쉬도록 하게.\n내일 환영하는 연회를 열겠네.\n&13\n감사합니다, 주군.\n&25\n그럼 먼저 물러가겠습니다.')
    stage.unit(25).setAction(0)
    stage.unit(13).setAction(0)
    stage.delay(3)
    stage.unit(13).move(49, 54, 3)
    stage.delay(1)
    stage.unit(13).move(49, 50, 0)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(13),
            49,
            46,
            0],
        [
            stage.unit(25),
            49,
            54,
            1]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(13),
            49,
            16,
            0],
        [
            stage.unit(25),
            49,
            24,
            0]])
    stage.unit(13).hide()
    stage.unit(25).move(49, 16, 0)
    stage.unit(25).hide()
    stage.bgSound(-1)
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n문약, 봉효. 앞으로 우리 군이 어찌 움직이면 좋겠는가?')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n이각과 곽사가 유일하게 의지하던 서공명마저\n우리 군에 귀순했으니\n그들의 운도 다한 듯합니다.')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n그렇습니다. 더 공격할 필요는 없겠습니다.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n음, 나도 같은 생각일세.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그렇다면 폐하에 관한 일이로군…….')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n폐하께서는 낙양에 머물기를 바라시는 듯합니다만…\n&20\n저는 반대합니다.')
    stage.say('&20\n지난 화재로 낙양은 이미 폐허가 되었습니다.\n&20\n옛 모습을 되찾으려면\n막대한 시간과 재물이 들 것입니다.\n게다가 낙양의 치안도 좋지 않습니다.')
    stage.unit(22).setAction(6, 2)
    stage.say('&22\n제후들도 머지않아 천자를 모시는 가치를 깨달을 것입니다.')
    stage.say('&22\n주군의 세력권 안으로 천도해야 합니다.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n나도 같은 생각이네. 허창으로 천도하세!\n그곳이라면 다른 제후들을 두려워할 것 없지.\n&22\n이견 없습니다.')
    stage.unit(0).setAction(17, 0)
    stage.say('&0\n좋다! 즉시 상주할 채비를 하겠다.\n이 일만 확정되면\n천자를 모셔 오는 대업을 이루는 것이다!\n&20\n예.\n&22\n예.')
    model.unitJoin(13, 0, 0)
    model.unitJoin(25, 0, 0)
    if vars[1013]:
        gvars[0] = 13
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
        gvars[0] = 25
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
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(149, 173, 76)
    stage.showHead(0, 237, 76)
    stage.bottomTxt('헌제는 조조의 천도 주청을 받아들였고', False, False, False)
    stage.delay(8)
    stage.head(0).move(397, 111)
    stage.head(149).move(333, 111)
    stage.bottomTxt('한의 도읍은 조조가 다스리던 허창으로 옮겨졌고\n이때부터 허창은 허도라 불리게 되었다.', True, True, False)
    stage.delay(10)
    stage.bottomTxt('조조는 이로써 천자를 모셔 오는 대업을 이루었다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene3():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            20,
            49,
            40,
            2],
        [
            22,
            57,
            40,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.unit(0).setAction(3)
    stage.say('&0\n문약, 이제 천자를 모셨으니\n앞으로 출병할 명분이 서겠군.\n&0\n모든 일이 우리가 바라던 대로 순조롭게 풀리고 있네.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n그렇습니다. 앞으로 주군과 적대하는 자는\n곧 조정의 명을 거역하는 자가 됩니다.')
    stage.unit(20).setAction(0)
    stage.say('&20\n천하의 패권을 놓고\n이제 진정한 전쟁이 시작됩니다.')
    stage.say('&22\n제후들도 우리가 천자를 받든 뜻을 알아차리면\n훗날 반드시 대거 침공해 올 것입니다.\n&0\n음.')
    stage.unit(0).move(34, 59, 2)
    stage.say('&0\n가장 먼저 없애야 할 눈엣가시는 여포다.\n지금 어찌 지내고 있는가?')
    stage.say('&22\n여포는 지금 서주로 달아나\n유비와 함께 있습니다.')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(3, 1)
    stage.say('&0\n뭐라고! 유비라고?!')
    model.setFace(2)
    stage.unit(0).setAction(2, 1)
    stage.say('&0\n곤란하게 되었군.\n유비와 여포가 손을 잡는다면\n그 힘은 두려워할 만하지!')
    stage.say('&20\n주군, 심려치 마십시오.')
    stage.say('&20\n유비는 부하들의 반대에도 불구하고\n고집을 부려 여포를 받아들였다 합니다.')
    stage.say('&20\n그들의 관계는 위태로우니\n머지않아 끊어질 것입니다.')
    model.setFace(0)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n다른 세력에 눈에 띄는 움직임은 없는가?')
    stage.say('&22\n강동의 손책이 눈에 띕니다.\n지금 크게 세력을 넓히고 있습니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n손책? 손견의 아들 말인가?\n손견이 죽은 뒤로\n원술에게 의탁했다던데…….')
    stage.say('&22\n이제 원술에게서 독립하여\n파죽지세로 강동을 장악했습니다.\n&22\n소패왕이라 불리며 국력도 매우 강성하니\n실로 후생가외라 하겠습니다.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n그렇군……과연 호랑이 아비에게 개 같은 자식은 없는 법이야.')
    stage.say('&20\n여포와 유비, 손책,\n그리고 원소와 원술 모두\n주군의 패업을 가로막는 강적입니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 336, 106)
    stage.showHead(32, 486, 132)
    stage.showHead(119, 550, 132)
    stage.bottomTxt('순욱의 말대로 얼마 지나지 않아 유비와 여포는 원수가 되었고,', False, False, False)
    stage.delay(8)
    stage.head(32).move(400, 106)
    stage.head(119).move(492, 132)
    stage.delay(5)
    stage.bottomTxt('여포에게 서주에서 쫓겨난 유비는 조조에게 의탁하러 왔다.', True, True, True)
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
            49,
            70],
        [
            32,
            49,
            55,
            3,
            6],
        [
            33,
            59,
            52,
            3],
        [
            3,
            59,
            58,
            3],
        [
            20,
            49,
            40,
            2],
        [
            22,
            55,
            40,
            2],
        [
            24,
            61,
            40,
            2],
        [
            21,
            67,
            40,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n현덕 대인, 서주에서 싸운 뒤로\n처음 뵙는군요.\n&32\n그렇소.')
    stage.unit(0).setAction(0)
    stage.say('&0\n그대 사정은 들었소.\n여포가 속임수로 서주를 빼앗았다지요?')
    stage.unit(32).setAction(0)
    stage.say('&32\n그때 우리는 원술과 싸우고 있었소.\n여포가 그 틈을 타 쳐들어올 줄은 몰랐소.')
    stage.unit(0).move(34, 59, 2)
    stage.say('&0\n그대가 비록 서주를 잃었다 하나,\n그대 같은 인물이\n몸 둘 곳조차 없다면 참으로 안타까운 일이오.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n폐하께 그대를 예주목으로 천거했네.\n지금 가서 부임해도 좋네.')
    stage.say('&32\n(뭐라고!)\n&33\n(응?)\n&3\n(조조, 저 늙은 도적놈이 무슨 속셈이지?)')
    stage.delay(3)
    stage.unit(32).setAction(6)
    stage.say('&32\n……대인의 후의에 참으로 감사드립니다.')
    stage.say('&0\n무슨 그런 말씀을. 우리 사이에 사양할 것이 있겠소.')
    stage.unit(32).setAction(0)
    stage.say('&32\n그럼 즉시 예주로 가겠소.\n&0\n서두르지 마시오. 오늘은 거처를 마련해 두었으니\n우선 쉬도록 하시오.')
    stage.unit(32).setAction(6)
    stage.say('&32\n좋다.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(33),
            95,
            52,
            1],
        [
            stage.unit(3),
            95,
            58,
            1],
        [
            stage.unit(32),
            85,
            55,
            1]])
    stage.unit(33).hide()
    stage.unit(3).hide()
    stage.unit(32).move(95, 55, 1)
    stage.unit(32).hide()
    stage.delay(3)
    stage.unit(1).setAction(0, 3)
    stage.say('&1\n맹덕, 어찌하여 그들을 그토록 후대하는가?')
    stage.unit(1).setAction(3, 3)
    stage.say('&1\n도겸을 칠 때도 저놈들이 훼방을 놓았지.\n차라리 죽이는 것이 좋겠네.')
    stage.say('&22\n그렇습니다. 유비는 너무 위험합니다.\n훗날 큰 근심거리가 될 것입니다.\n&21\n옳은 말입니다. 지금 뿌리를 뽑아 후환을 없애야 합니다.')
    stage.unit(24).setAction(0, 3)
    stage.say('&24\n하지만………\n유비를 우리 군에 받아들이면 어찌 되겠습니까?')
    stage.unit(22).setAction(0, 1)
    stage.say('&22\n그것은 너무 일방적인 기대입니다.\n유비는 남의 아래에 머물 사람이 아닙니다.')
    stage.unit(0).move(34, 55, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n……문약, 자네 생각은 어떠한가?')
    stage.unit(24).setAction(0, 2)
    stage.unit(22).setAction(0, 2)
    stage.unit(1).setAction(0, 0)
    stage.delay(5)
    stage.unit(20).setAction(6)
    stage.say('&20\n유비가 위험한 인물인 것은 사실입니다만…….')
    stage.unit(20).setAction(0)
    stage.say('&20\n주군, 유비는 이미 천하에 이름난 인물입니다.\n&20\n그를 죽이면 천하 사람들이\n주군을 잔혹한 자로 여길 수도 있습니다.')
    stage.say('&0\n음, 나도 문약과 같은 생각이네.')
    stage.say('&1\n하지만…….')
    stage.say('&0\n원양, 유비와 내가 많이 닮았다고 생각하지 않는가?')
    stage.say('&1\n많이 닮았다고?')
    stage.unit(1).setAction(3, 0)
    stage.say('&1\n어디가 닮았다는 건가?')
    stage.unit(1).setAction(0, 0)
    stage.delay(5)
    stage.unit(0).move(31, 55, 3)
    stage.unit(0).setAction(0)
    stage.say('&0\n유비는 천자의 유씨 혈통을 이용하고\n나 역시 천자를 이용하니, 그 점은 닮았군.\n&21\n역시 죽여야 합니다!\n&22\n지금 유비를 죽이지 않으면 후세 사람들은\n주군과 유비를 두고 유비는 영웅이요,\n주군은 간웅이라 평할 것입니다!')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n후세 사람들이 어찌 생각하든 상관없다.')
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n지금 당장의 일을 살피는 것이 더 중요하네.')
    stage.delay(3)
    stage.showUnit(181, 95, 55, 3)
    stage.say('&181\n아룁니다.')
    stage.unit(181).move(61, 55, 3)
    stage.say('&181\n방금 완성의 장수가 우리 군을 공격하려 한다는 소식이 왔습니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n뭐라고!\n&20\n사실일 가능성이 큽니다.\n장수는 동탁의 부하였던 장제의 조카이니\n필시 주군을 깊이 원망할 것입니다.')
    stage.unit(0).move(34, 55, 1)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n제 분수도 모르는 자로군. 쳐들어오기를 기다릴 것 없이\n우리가 먼저 공격하겠다.\n&20\n잠시만 기다려 주십시오.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n무슨 일인가, 문약?\n&20\n주군, 섣불리 출병하는 것은 너무 위험합니다.\n&20\n우리 군이 함부로 움직였다가\n여포가 지난날 복양을 습격했듯이\n다시 허도를 덮친다면 어찌하시겠습니까?')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('&0\n음, 그것도 사실이지.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(2, 2)
    stage.say('&0\n……그렇다고 장수를 내버려 둘 수도 없지 않은가.\n&20\n여포에게 금은과 관직을 내려\n유비와 화해하도록 하시지요.\n&20\n유비는 엄연히 주군의 휘하에 있는 예주목입니다.\n&20\n여포와 유비가 화목하게 지내도록 한다면\n여포도 주군을 공격하지 못할 것입니다.')
    model.setFace(0)
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음, 묘책이로군.\n문약, 즉시 시행하게.')
    stage.unit(0).setAction(17, 1)
    stage.say('&0\n일이 마무리되는 대로 완성을 공격한다!')
    stage.unit(20).setAction(6, 2)
    stage.say('&20\n예.')
    stage.effectSound(200, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 345, 110)
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(5)
    stage.showHead(143, 262, 136)
    stage.delay(5)
    stage.bottomTxt('장수는 완성을 차지하고 있었고', True, False, False)
    stage.delay(5)
    stage.showHead(119, 489, 138)
    stage.delay(5)
    stage.bottomTxt('여포는 서주를 차지하고 있었다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('그 뒤 순욱의 예상대로 여포는 화해를 받아들였다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('후환을 없앤 조조의 대군은', True, True, False)
    stage.delay(5)
    stage.head(0).move(326, 136)
    stage.delay(5)
    stage.bottomTxt('완성 공격에 전력을 기울일 수 있게 되었다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene5():
    stage.loadBg(2, 48)
    stage.showUnits([
        [
            143,
            53,
            31,
            2],
        [
            23,
            53,
            48,
            0,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('완성  장수 저택')
    stage.say('&143\n뭐라고! 조조가 먼저 출병했다고!\n&23\n그렇습니다.')
    stage.unit(143).move(49, 31, 3)
    stage.say('&143\n음…….')
    stage.unit(143).setAction(0, 2)
    stage.say('&143\n문화, 이번 싸움에 승산이 있는가?\n&23\n조조군은 백전의 정예입니다.\n우리에게는 승산이 없습니다.')
    stage.unit(143).setAction(2, 2)
    stage.say('&143\n음…….\n&23\n주군, 아무래도 항복하는 수밖에 없겠습니다.')
    stage.unit(143).setAction(0, 2)
    stage.say('&143\n원수를 갚을 수 없단 말인가?\n&23\n싸워 봐야 목숨만 잃습니다.\n참고 때를 기다려야 합니다.')
    stage.unit(143).setAction(0, 3)
    stage.delay(5)
    stage.say('&143\n……좋다. 항복하는 수밖에 없겠군.')
    stage.unit(143).setAction(0, 1)
    stage.delay(3)
    stage.unit(143).move(53, 31, 1)
    stage.delay(3)
    stage.unit(143).setAction(0, 2)
    stage.say('&143\n문화, 사신으로 조조군에 가서 항복을 청하라.\n&23\n예.')
    stage.effectSound(200, 255)
    stage.delay(3)
    stage.bgSound(15)
    stage.delay(3)
    stage.clsUnit()
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
            13,
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
            18,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('완성 교외  조조군 본진')
    stage.unit(0).setAction(3)
    stage.say('&0\n곧 완성에 도착한다.\n기세를 몰아 단숨에 함락하자!')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 9, [
        16,
        12])
    gvars[4076] = 0
    gvars[4057] = 10
    stage.setBattlePos([
        [
            12,
            8],
        [
            20,
            8],
        [
            1,
            8],
        [
            14,
            6],
        [
            13,
            11],
        [
            10,
            9],
        [
            1,
            10],
        [
            0,
            8],
        [
            1,
            7],
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
            0],
        [
            0,
            0]])
    return False


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        return True
    if stage.unitClickTest(13):
        stage.say('&13\n제가 이끄는 부대는 궁병대입니다.\n그러고 보니 만총은 풍수사지요?')
    if stage.unitClickTest(20):
        stage.say('&20\n최근 장수 곁에 지략이 뛰어난 군사가 있다고 합니다.\n&0\n오? 군사라고…….')
    if stage.unitClickTest(1):
        stage.say('&1\n반드시 장수의 목을 베어 모두에게 보여 주겠다!')
    if stage.unitClickTest(22):
        stage.say('&22\n완성은 활기가 넘친다 합니다.\n&22\n장수에게 그런 내정의 재주가 있다니\n믿기 어렵군요.')
    if stage.unitClickTest(16):
        stage.say('&0\n전위.\n&16\n예!\n&0\n아, 아니…… 아무것도 아니다…….\n&16\n무슨 일이십니까? 근심이 있으신 듯합니다.\n&0\n(어찌 된 일이지……?\n자꾸만 전위가 내 곁을 떠날 것 같은 기분이 드는군…….)')
    if stage.unitClickTest(18):
        stage.say('&18\n도구는 충분한가?')
    return False


def scene7():
    stage.showUnit(181, 49, 16, 3)
    stage.say('&181\n아룁니다.')
    stage.unit(181).move(49, 46, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&0\n무슨 일이냐? 곧 출발할 참이다.\n&181\n……완성에서 항복을 청하는 사신이 왔습니다.\n&0\n뭐라고?')
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n……좋다. 들라 하라.\n&181\n예.')
    stage.unit(181).setAction(0, 2)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.showUnit(23, 49, 16, 2)
    stage.say('&23\n반갑습니다.')
    stage.unit(0).setAction(0, 0)
    stage.unit(23).move(49, 52, 2)
    stage.delay(3)
    stage.unit(23).setAction(6, 2)
    stage.delay(3)
    stage.say('&0\n내가 조조다. 장수가 보낸 항복 사신인가?\n&23\n그렇습니다.\n&23\n저희는 무장을 풀고\n완성을 조 승상께 바치고자 합니다.\n&23\n주군 장수께서는 본래 승상과 맞설 뜻이 없으셨습니다.\n&23\n부디 장수 주군을 너그러이 처분하여 주십시오.\n승상의 자비를 바라옵니다.')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.say('&0\n음.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n좋다. 장수의 요청을 받아들이겠다.\n&23\n감사합니다, 대인.\n&0\n사자여, 그대의 이름은 무엇인가?\n&23\n소인은 가후라 합니다.')
    stage.unit(0).setAction(3, 0)
    stage.say('&0\n뭐라고? 그대가 가후 가문화인가?')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n여포와 마등, 두 맹장을 이겼던 그대가\n어찌 이번에는 싸우지도 않고 항복하는가……?\n&23\n그 두 사람은 용맹할 뿐 지략이 없었습니다.\n하지만 지용을 겸비하신 대인을 상대하기에는\n제 재주가 한참 부족합니다.')
    model.setFace(3)
    stage.unit(0).setAction(3, 1)
    stage.say('&0\n하하하.')
    model.setFace(0)
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n좋다. 돌아가 장수에게 전하라.\n이번 일은 불문에 부치겠다고.\n&23\n감사합니다, 승상. 이만 물러가겠습니다.')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).move(49, 16, 0)
    stage.unit(23).hide()
    stage.say('&0\n문약, 가후라는 자는 소문만큼 대단하지는 않군.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n주군, 그자를 얕보셔서는 안 됩니다.')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n그렇습니다. 결코 방심할 수 없는 자입니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음…….')
    stage.effectSound(200, 255)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(0, 36)
    stage.draw()
    stage.info('조조는 장수의 항복을 받아들여 호위병과 함께 완성에 들어갔다.')
    stage.info('병사 하나 잃지 않고 완성을 얻은 조조는\n완성에서 한가롭게 쉬고 있었다.')
    stage.bgSound(-1)
    return False


def scene8():
    stage.loadBg(2, 39)
    stage.showUnits([
        [
            0,
            68,
            59,
            1,
            1],
        [
            30,
            68,
            49,
            2,
            1]])
    stage.draw()
    stage.setStageName('완성  누각')
    stage.say('&30\n숙부님, 완성도 제법 운치가 있습니다.')
    model.setFace(3)
    stage.say('&0\n음.')
    model.setFace(0)
    stage.say('&0\n음?')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).move(55, 59, 3)
    stage.delay(1)
    stage.unit(0).move(55, 65, 2)
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(30).setAction(0)
    stage.say('&30\n숙부님, 무슨 일이십니까?')
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n안민, 호가 소리가 들리느냐?\n&30\n예?')
    stage.unit(30).move(55, 49, 3)
    stage.delay(3)
    stage.unit(30).setAction(0, 2)
    stage.say('&30\n오, 참으로 그윽하고 아름다운 소리군…….')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n음색이 참으로 아름답구나…\n이런 소리는 처음 들어 보네.')
    stage.unit(30).move(55, 55, 2)
    stage.say('&30\n숙부님, 생각해 보니………\n호가를 부는 사람이 추씨가 아닐까요?')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n추씨라고? 장제의 아내이자\n절세미인으로 이름난 그 추씨 말이냐?\n&30\n그렇습니다. 장제가 죽은 뒤로\n조카 장수에게 의지하고 있습니다.\n&30\n미모가 뛰어날 뿐 아니라\n호가 연주에도 능하다고 합니다…….')
    stage.say('&0\n오?\n&30\n숙부님, 제가 추씨를 모셔 올까요?')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n듣고 보니 그렇기도 하군…….')
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('좋다. 어서 데려오너라.\n되었다. 그만두어라.', -1)
    if sel == 1:
        vars[304] = 1
        stage.addAmbition(10)
        stage.bgSound(-1)
        stage.say('&0\n좋다. 가서 데려오너라…….\n예를 갖추도록 해라.\n&30\n예.')
        stage.unit(30).move(55, 30, 0)
        stage.unit(0).setAction(0, 2)
        stage.unit(30).hide()
        stage.info('잠시 뒤…….', 3)
        stage.say('&30\n숙부님, 모셔 왔습니다.')
        stage.unit(0).setAction(0, 0)
        stage.showUnit(30, 55, 30, 2)
        stage.unit(30).move(55, 40, 2)
        stage.delay(3)
        stage.unit(30).setAction(0, 0)
        stage.delay(3)
        stage.say('&30\n이쪽으로 오십시오.\n&156\n승상을 뵙습니다.')
        stage.showUnit(156, 55, 30, 2)
        stage.unitsMove([
            [
                stage.unit(30),
                65,
                40,
                1],
            [
                stage.unit(156),
                55,
                40,
                2]])
        stage.delay(3)
        stage.unit(30).setAction(0, 2)
        stage.say('&30\n이분은 조맹덕 대인이십니다.')
        model.setFace(3)
        stage.unit(0).setAction(3, 0)
        stage.say('&0\n오, 기다리고 있었네.')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(30).setAction(0, 2)
        stage.unit(0).move(55, 60, 0)
        stage.delay(1)
        stage.unit(0).move(72, 60, 1)
        stage.delay(3)
        model.setFace(0)
        stage.unit(0).setAction(0, 3)
        stage.say('&0\n자, 이쪽으로 오시지요.\n&156\n알겠소.')
        stage.delay(3)
        stage.unit(156).move(55, 60, 2)
        stage.delay(1)
        stage.unit(0).setAction(0, 3)
        stage.unit(156).setAction(0, 1)
        stage.unit(156).move(62, 60, 1)
        stage.delay(10)
        stage.say('&30\n숙부님, 저와 조앙은 볼일이 있어\n먼저 물러가겠습니다.\n&0\n음.')
        stage.unit(30).move(55, 40, 3)
        stage.delay(1)
        stage.unit(30).move(55, 30, 0)
        stage.unit(30).hide()
        stage.delay(3)
        stage.unit(0).setAction(1, 3)
        stage.unit(156).setAction(1, 1)
        stage.delay(3)
        stage.say('&0\n부인, 호가 연주가 참으로 아름답구려…….\n&156\n과찬이십니다, 대인.')
        stage.unit(0).setAction(1, 2)
        stage.say('&0\n여기서 한 곡 들려줄 수 있겠는가?\n&156\n예…….')
        stage.unit(156).setAction(8, 1)
        stage.delay(10)
    elif sel == 2:
        vars[304] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n됐다. 굳이 그럴 필요는 없겠군.')
        stage.unit(0).setAction(0, 1)
        model.setFace(3)
        stage.say('&0\n이렇게 멀리서 들려오는 음악을 들으며\n술을 마시는 것도 나쁘지 않구나.\n&30\n예.')
        model.setFace(0)
    stage.clsUnit()
    return False


def scene9():
    stage.loadBg(2, 48)
    stage.showUnits([
        [
            143,
            53,
            30,
            2],
        [
            23,
            53,
            48,
            0,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('완성  장수 저택')
    if vars[304]:
        stage.delay(3)
        stage.unit(143).setAction(2)
        stage.say('&143\n뭐라고!! 조조와 추 부인이?\n&23\n예. 술 시중을 들라며 불러 갔습니다.')
        stage.unit(143).move(49, 30, 3)
        stage.delay(3)
        stage.unit(143).setAction(2)
        stage.say('&143\n이놈! 감히 내 성 안에서 나를 망신시키고\n우리 장씨 집안의 여인을 대놓고 욕보이다니!')
        stage.unit(143).setAction(0, 2)
        stage.delay(3)
        stage.unit(143).setAction(2, 2)
        stage.say('&143\n조만, 이 늙은 도적! 결코 용서하지 않겠다!\n&23\n주군의 심정은 이해합니다만\n우선 진정하십시오.\n&23\n조조가 지금 방심하고는 있어도\n병력은 여전히 강대합니다.\n&23\n정면으로 맞서면 우리 군에는 승산이 없습니다.')
        stage.unit(23).setAction(0, 0)
        stage.delay(5)
        stage.unit(23).setAction(0, 2)
        stage.say('&23\n제게 계책이 하나 있습니다.')
        stage.unit(143).setAction(0)
        stage.say('&143\n뭐라고?')
        stage.unit(23).setAction(0, 0)
        stage.say('&23\n지금 조조를 호위하는 자는 전위다.\n먼저 그를 없애야 한다.')
        stage.unit(143).move(53, 30, 2)
        stage.say('&143\n하지만 전위는 전군에서 으뜸가는 맹장이 아닌가.')
        stage.unit(23).setAction(6, 0)
        stage.say('&23\n염려 마십시오. 그를 상대할 방도는 이미 마련했습니다.')
        stage.unit(23).setAction(0, 0)
        stage.delay(3)
        stage.unit(23).setAction(0, 2)
        stage.say('&23\n호거아? 호거아는 어디 있느냐!?')
    if not vars[304]:
        stage.delay(3)
        stage.unit(143).setAction(2)
        stage.say('&143\n조만, 이 가증스러운 도적놈!')
        stage.unit(23).setAction(0)
        stage.say('&23\n주군, 말씀이 너무 거치십니다.\n&143\n문화, 어찌 거칠어지지 않을 수 있겠는가?\n이 원수를 갚지 못한다면\n무슨 낯으로 조상님들을 뵙겠는가!')
        stage.unit(143).move(49, 30, 3)
        stage.delay(3)
        stage.unit(143).setAction(2)
        stage.say('&143\n문화, 더는 이 치욕을 견딜 수 없네.\n조조 역적과 목숨을 걸고 싸우겠네!')
        stage.unit(23).setAction(6)
        stage.say('&23\n그 심정은 이해합니다만\n우선 진정하십시오.\n&23\n조조가 지금 방심하고는 있어도\n병력은 여전히 강대합니다.\n&23\n정면으로 맞서면 우리 군에는 승산이 없습니다.')
        stage.unit(23).setAction(0, 0)
        stage.delay(3)
        stage.unit(23).setAction(0, 2)
        stage.say('&23\n제게 계책이 하나 있습니다.')
        stage.unit(143).setAction(0, -1)
        stage.delay(3)
        stage.unit(143).setAction(0, 2)
        stage.say('&143\n뭐라고?')
        stage.unit(23).setAction(0, 0)
        stage.say('&23\n지금 조조를 호위하는 자는 전위이니\n먼저 그를 제거해야 합니다.\n&143\n허나 전위는 삼군을 압도하는 맹장이 아닌가.')
        stage.unit(23).setAction(6, 0)
        stage.say('&23\n염려 마십시오. 그를 상대할 방도는 이미 마련했습니다.')
        stage.unit(23).setAction(0, 0)
        stage.delay(3)
        stage.unit(23).setAction(0, 2)
        stage.say('&23\n호거아! 호거아는 어디 있느냐!?')
    stage.showUnit(144, 53, 98)
    stage.say('&144\n저를 부르셨습니까?')
    stage.unit(144).move(53, 58, 0)
    stage.say('&23\n음.\n&23\n호거아, 이른 대로 행하라.\n잔치를 열어 전위를 극진히 대접한 뒤\n그의 무기를 빼앗아라.\n&144\n예! 오늘 밤에 실행하겠습니다.\n&23\n음.')
    stage.unit(144).move(53, 98, 2)
    stage.unit(144).hide()
    stage.delay(3)
    stage.unit(23).setAction(0, 0)
    stage.say('&23\n주군, 호거아의 보고를 기다린 뒤\n군사를 일으켜 기습하시지요.\n&143\n음.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    stage.loadBg(2, 48)
    stage.showUnits([
        [
            143,
            53,
            30,
            2],
        [
            23,
            53,
            48]])
    stage.draw()
    stage.info('그날 밤…….', 3)
    stage.setStageName('완성  장수 저택')
    stage.say('&143\n호거아는 아직 돌아오지 않았는가?\n대군은 이미 준비를 마쳤네.\n&23\n곧 돌아올 것입니다.')
    stage.showUnit(144, 53, 98)
    stage.say('&144\n아뢰옵니다!')
    stage.unit(23).setAction(0, 2)
    stage.unit(144).move(53, 58, 0)
    stage.say('&23\n호거아, 잘되어 가는가?\n&144\n그렇습니다.')
    stage.unit(23).move(53, 50, 2)
    stage.say('&23\n음, 잘했다!\n이제 조조 역적을 없앨 수 있겠군!')
    stage.unit(143).setAction(17, 2)
    stage.say('&143\n좋다, 출진하라!')
    stage.clsUnit()
    return False


def scene10():
    stage.loadBg(2, 39)
    stage.showUnits([
        [
            0,
            68,
            59,
            0,
            1],
        [
            30,
            68,
            49,
            2,
            1]])
    stage.draw()
    stage.setStageName('완성  누각')
    stage.delay(3)
    stage.unit(0).setAction(1, 1)
    stage.say('&0\n음? 어디서 말발굽 소리가…….')
    stage.showUnit(31, 55, 30, 2)
    stage.say('&31\n아버님!')
    stage.effectSound(207, 0)
    stage.delay(3)
    stage.unit(0).setAction(1, 3)
    stage.unit(30).setAction(1, 3)
    stage.unit(31).move(55, 59, 2)
    stage.delay(3)
    stage.unit(31).setAction(2, 1)
    stage.say('&31\n아버님, 큰일입니다!')
    stage.unit(30).setAction(0, 3)
    stage.unit(0).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).move(67, 59, 3)
    stage.say('&0\n무슨 일이냐, 조앙!\n&31\n아버님, 장수가 반란을 일으켰습니다!')
    model.setFace(2)
    stage.unit(0).setAction(2, 3)
    stage.say('&0\n뭐라고!?')
    stage.unit(31).setAction(0, 1)
    stage.say('&31\n적이 이 누각을 포위했습니다.\n아버님, 어서 피하십시오!')
    model.setFace(0)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n전위는 어찌 되었느냐?\n&31\n아버님의 퇴로를 열기 위해 목숨을 걸고 싸우고 있습니다.')
    model.setFace(2)
    stage.unit(0).setAction(2, 3)
    stage.say('&0\n이런! 장수, 그놈을 너무 얕보았구나!\n&31\n아버님, 어서 저를 따라오십시오! 시간이 없습니다!')
    model.setFace(0)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n좋다, 조앙! 너만 믿겠다!')
    stage.unitsMove([
        [
            stage.unit(31),
            55,
            47,
            0],
        [
            stage.unit(0),
            55,
            59,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(31),
            55,
            30,
            0],
        [
            stage.unit(0),
            55,
            42,
            0]])
    stage.unit(31).hide()
    stage.unitsMove([
        [
            stage.unit(0),
            55,
            30,
            0],
        [
            stage.unit(30),
            55,
            49,
            3]])
    stage.unit(0).hide()
    stage.unit(30).move(55, 30, 0)
    stage.unit(30).hide()
    stage.delay(3)
    stage.effectSound(207, 255)
    stage.clsUnit()
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
