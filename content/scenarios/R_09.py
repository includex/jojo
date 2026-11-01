# Source Generated with Decompyle++
# File: R_09.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 67)
    stage.showUnits([
        [
            2,
            44,
            98],
        [
            126,
            54,
            98]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('헌제')
    stage.setStageName('복양  교외 산길')
    stage.unitsMove([
        [
            stage.unit(2),
            44,
            86,
            0],
        [
            stage.unit(126),
            54,
            86,
            0]])
    stage.showUnit(119, 49, 98)
    stage.unitsMove([
        [
            stage.unit(2),
            44,
            40,
            0],
        [
            stage.unit(126),
            54,
            40,
            0],
        [
            stage.unit(119),
            49,
            52,
            0]])
    stage.delay(3)
    stage.unit(119).setAction(0, 2)
    stage.delay(3)
    stage.unit(119).setAction(2, 2)
    stage.say('&119\n늙은 도적 조만, 반드시 이 원수를 갚겠다!')
    stage.unit(2).setAction(0, 1)
    stage.unit(126).setAction(0, 3)
    stage.delay(5)
    stage.unit(126).setAction(0, 2)
    stage.say('&126\n주군의 심정은 이해합니다만\n지금은 우선 목숨을 보전하셔야 합니다.')
    stage.unit(119).setAction(0, 0)
    stage.say('&119\n그렇다 해도 어디로 가겠는가?\n&126\n서주로 가서 유현덕에게 의탁하시지요.')
    stage.say('&2\n유현덕이라고……?')
    stage.unit(119).setAction(0, 2)
    stage.say('&119\n나도 그자는 알고 있네………\n허나 우리를 받아 주겠는가……?\n&126\n유현덕은 의리를 중히 여기니 반드시 받아 줄 것입니다.')
    stage.unit(119).setAction(0, 0)
    stage.say('&119\n좋다! 자네 뜻대로 서주로 가겠네.\n&126\n예.')
    stage.unit(2).setAction(0, 2)
    stage.say('&2\n예.')
    stage.unitsMove([
        [
            stage.unit(2),
            44,
            4,
            0],
        [
            stage.unit(126),
            54,
            4,
            0],
        [
            stage.unit(119),
            49,
            12,
            0]])
    stage.unit(2).hide()
    stage.unit(126).hide()
    stage.unit(119).move(49, 4, 0)
    stage.unit(119).hide()
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 416, 67)
    stage.showHead(119, 480, 67)
    stage.showHead(32, 459, 152)
    stage.bottomTxt('조조에게 패한 여포는 여러 곳을 전전하며 달아났고,', False, False, False)
    stage.delay(5)
    stage.head(119).move(523, 67)
    stage.head(119).move(523, 152)
    stage.bottomTxt('마침내 서주의 유비 휘하에 의탁했다.', True, False, True)
    stage.bottomTxt('이때,', False, True, False)
    stage.delay(5)
    stage.showHead(131, 220, 82)
    stage.bottomTxt('이각과 곽사는 권력과 이익을 다투며', True, True, False)
    stage.showHead(129, 156, 82)
    stage.bottomTxt('마침내 무력 충돌이 일어났다.', True, False, False)
    stage.delay(5)
    stage.bottomTxt('장안에 다시 전쟁의 불길이 올랐다.', True, True, True)
    stage.bgSound(-1)
    stage.loadBg(2, 15)
    stage.showUnits([
        [
            149,
            27,
            55,
            1],
        [
            23,
            46,
            55,
            3,
            6],
        [
            192,
            46,
            43,
            2],
        [
            193,
            52,
            43,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('장안  궁궐')
    stage.say('&149\n뭐라고, 짐을 달아나게 해 주겠다고!?\n&23\n그렇습니다.')
    stage.unit(192).setAction(3)
    stage.say('&192\n뭐라고!')
    stage.unit(192).setAction(0)
    stage.delay(5)
    stage.unit(23).setAction(0)
    stage.say('&23\n……폐하, 서두르셔야 합니다.\n&149\n하지만 가 경, 그대는 이각군의 군사가 아닌가.\n어찌하여……?')
    stage.unit(23).setAction(6)
    stage.say('&23\n좋은 새는 나무를 가려 깃드는 법이지요. 이각은 제 주군으로 적합하지 않았습니다.')
    stage.unit(149).setAction(0, 3)
    stage.say('&149\n좋소……가 경, 고맙소.')
    stage.unit(149).setAction(0, 1)
    stage.say('&149\n어서 신료들에게 전하라. 즉시 장안을 떠나겠다.\n……짐은 낙양으로 돌아가겠다.\n&192\n예.\n&193\n예.')
    stage.unitsMove([
        [
            stage.unit(193),
            91,
            43,
            1],
        [
            stage.unit(192),
            85,
            43,
            1]])
    stage.unit(193).hide()
    stage.unit(192).hide()
    stage.say('&149\n가 경, 앞으로 어찌할 생각인가?\n&23\n완성의 장수에게 의탁하고자 합니다.\n&149\n장수?\n&23\n돌아가신 장제 대인의 조카입니다.\n&149\n가 경, 짐을 위해 일할 생각은 없는가?\n그대야말로 진정한 충신이니\n높은 벼슬과 후한 녹봉을 내리겠다.')
    stage.unit(23).setAction(0)
    stage.say('&23\n……송구하오나 따르기 어렵사옵니다.\n&149\n짐을 위해 일하지 않겠다는 말인가?\n&23\n폐하,\n신은 제 재능을 펼칠 수 있게 해 주는 이를 섬기고 싶사옵니다.')
    stage.unit(23).setAction(6)
    stage.say('&23\n직언을 용서하십시오. 폐하께는 아직 힘이 없습니다.\n&149\n…………….\n&149\n그렇다면 장수는 섬길 가치가 있는 자란 말이오?\n&23\n제 일은 염려하지 않으셔도 됩니다.\n폐하께서는 옥체의 안전을 먼저 생각하십시오.')
    stage.showUnits([
        [
            192,
            85,
            43,
            3],
        [
            193,
            91,
            43,
            3]])
    stage.unitsMove([
        [
            stage.unit(192),
            66,
            43,
            3],
        [
            stage.unit(193),
            72,
            43,
            3]])
    stage.unit(192).setAction(0)
    stage.unit(193).setAction(0)
    stage.say('&192\n폐하, 모두 크게 기뻐하고 있사옵니다.\n&193\n즉시 출발하시지요. 시간이 촉박하옵니다!\n&149\n음……가 경.\n&23\n폐하, 부디 가시는 길 평안하시옵소서.')
    stage.unit(149).move(27, 48, 0)
    stage.delay(1)
    stage.unit(149).move(66, 48, 1)
    stage.delay(5)
    stage.unit(192).setAction(0, 2)
    stage.unit(193).setAction(0, 2)
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(149),
            92,
            48,
            1],
        [
            stage.unit(192),
            85,
            43,
            1],
        [
            stage.unit(193),
            91,
            43,
            1]])
    stage.unit(192).hide()
    stage.unit(193).hide()
    stage.unit(149).hide()
    stage.unit(23).setAction(0, 3)
    stage.delay(3)
    stage.unit(23).setAction(0, 1)
    stage.say('&23\n이각이 곧 폐하의 탈출을 알아챌 테니\n저도 즉시 장안을 떠나야 합니다.')
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(0, 9)
    stage.draw()
    stage.info('이각과 곽사가 다투는 가운데 곽사군의 장수\n서황이 눈부신 활약을 펼쳐 전세는 곽사군에 크게 유리해졌다.')
    stage.info('그러나 마지막 순간에 가후가 헌제를 풀어 주어\n헌제는 장안을 빠져나갔다. 이를 안 이각과 곽사는\n즉시 싸움을 멈추고 힘을 합쳐 헌제를 추격했다.')
    stage.bgSound(-1)
    return False


def scene2():
    stage.loadBg(2, 59)
    stage.showUnit(192, 49, 95)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('장안 교외  산길')
    stage.unit(192).move(49, 85, 0)
    stage.showUnit(149, 49, 95)
    stage.unitsMove([
        [
            stage.unit(192),
            49,
            75,
            0],
        [
            stage.unit(149),
            49,
            85,
            0]])
    stage.showUnits([
        [
            193,
            44,
            95],
        [
            194,
            54,
            95]])
    stage.unitsMove([
        [
            stage.unit(192),
            49,
            30,
            0],
        [
            stage.unit(149),
            49,
            40,
            0],
        [
            stage.unit(193),
            44,
            50,
            0],
        [
            stage.unit(194),
            54,
            50,
            0]])
    stage.unit(149).setAction(0, 0)
    stage.delay(5)
    stage.say('&149\n아, 아…….')
    stage.unit(192).setAction(0, 2)
    stage.delay(3)
    stage.unit(192).setAction(3, 2)
    stage.say('&192\n아, 폐하!')
    stage.unit(192).setAction(0, 2)
    stage.unit(193).move(44, 40, 0)
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(0, 1)
    stage.say('&193\n무슨 일이십니까?')
    stage.say('&149\n짐은 더는 걸을 수 없구나…….\n&193\n허나 이곳에 머무르시면\n이각과 곽사의 군대가 따라잡을 것이옵니다.')
    stage.unit(149).setAction(0, 1)
    stage.say('&149\n짐은 더 가지 않겠소. 이각 일당도 짐을 죽이지는 못할 것이오.\n짐은 상관 말고 어서 몸을 피하시오.')
    stage.unit(192).setAction(3, 2)
    stage.say('&192\n그……그럴 수는 없습니다.')
    stage.unit(192).setAction(0, 2)
    stage.delay(3)
    stage.unit(194).setAction(6)
    stage.say('&194\n폐하, 인근의 제후들에게 구원을 청하시지요?')
    stage.unit(149).move(54, 40, 1)
    stage.unit(149).setAction(0)
    stage.delay(3)
    stage.unit(149).setAction(0, 2)
    stage.say('&149\n제후 중에 누가 힘이 있어\n이각과 곽사의 대군에 맞설 수 있겠소?\n&194\n신에게 두 사람이 있사옵니다. 하나는 하북의 원소입니다.\n&194\n동탁을 토벌하던 동맹군의 총사령관이었사옵니다.\n&194\n그의 병력은 이각과 곽사보다 강할 것입니다.\n&149\n다른 한 사람은 누구요……?\n&194\n복양의 조조입니다. 병력은 원소보다\n적으나 휘하에 모사와 용장이 모여 있고,\n도적을 토벌해 조정에 큰 공을 세웠습니다.')
    stage.unit(149).setAction(0, 1)
    stage.say('&149\n원소와 조조라고……?\n알겠다. 두 사람에게 구원을 청하라.\n&194\n그럼 신이 즉시 가겠사옵니다.')
    stage.unit(194).setAction(0, 0)
    stage.delay(3)
    stage.unit(194).move(44, 50, 3)
    stage.delay(3)
    stage.unit(194).setAction(0, 0)
    stage.say('&194\n가시지요.')
    stage.unit(193).setAction(0, 2)
    stage.say('&193\n예.')
    stage.unitsMove([
        [
            stage.unit(193),
            44,
            4,
            0],
        [
            stage.unit(194),
            44,
            14,
            0]])
    stage.unit(193).hide()
    stage.unit(194).move(44, 4, 0)
    stage.unit(194).hide()
    stage.unit(192).move(49, 40, 1)
    stage.say('&192\n폐하, 낙양으로 가시지요.')
    stage.unit(192).setAction(0, 3)
    stage.say('&192\n신이 부축해 드리겠습니다.')
    stage.unit(149).setAction(0, 3)
    stage.delay(3)
    stage.unit(149).move(51, 40, 3)
    stage.unit(149).setAction(0)
    stage.say('&149\n……수고했소.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            194,
            52,
            31,
            2,
            5],
        [
            101,
            49,
            46],
        [
            106,
            57,
            46]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('며칠 뒤…….', 3)
    stage.setStageName('신도  원소 저택')
    stage.say('&194\n……성지를 받들라.\n대신 원소는 즉시 폐하의 곁으로 와 어가를 호위하고\n그 충절을 보이라.\n&101\n예.')
    stage.unit(194).setAction(0)
    stage.say('&194\n성지를 받으시오.\n&101\n참으로 황공하오나 받아들일 수 없으니 용서해 주시오.\n&194\n어째서요? 다른 것도 아닌 성지가 아니오!')
    stage.say('&101\n지금 신도 부근에 산적이 들끓어\n토벌하느라 바쁘니\n도무지 틈을 낼 수가 없구나…….')
    stage.delay(3)
    stage.unit(106).setAction(6)
    stage.say('&106\n예, 참으로 송구합니다…….')
    stage.say('&194\n사정이 꼭 그렇지는 않은 듯합니다만.\n성 안팎을 보니 치안이 매우 좋더군요.')
    stage.say('&101\n아니, 아직 산적이 많이 남았네.')
    stage.say('&106\n흠차 대인, 참으로 송구합니다…….')
    stage.unit(194).setAction(0, 0)
    stage.say('&194\n으음……… 좋소!\n그대가 이토록 불충하다는 것을 폐하께 아뢰겠소.\n&101\n이것도 어쩔 수 없는 일이오.\n&106\n그렇습니다…….')
    stage.unit(194).move(45, 31, 3)
    stage.unit(194).setAction(0)
    stage.delay(1)
    stage.unit(194).move(45, 90, 2)
    stage.unit(194).hide()
    stage.delay(3)
    stage.unit(101).setAction(0, 1)
    stage.say('&101\n심배, 이렇게 하면 어떻겠는가……?')
    stage.unit(106).setAction(0, 3)
    stage.say('&106\n할 수 있습니다.')
    stage.unit(106).setAction(0, 2)
    stage.say('&106\n어차피 지금은 조정에서 상을 받을 수도 없으니\n공연히 병력을 잃을 필요가 있겠습니까.')
    stage.unit(101).setAction(0, 2)
    stage.say('&101\n음…….')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    model.initLocalVar()
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 12)
    stage.showUnits([
        [
            193,
            30,
            54,
            1],
        [
            1,
            50,
            41,
            3,
            6],
        [
            16,
            56,
            41,
            3,
            6],
        [
            20,
            50,
            68,
            3,
            6],
        [
            22,
            56,
            68,
            3,
            6],
        [
            21,
            62,
            68,
            3,
            6]])
    if not vars[706]:
        stage.showUnit(18, 62, 41, 3, 6)
    stage.showUnit(0, 50, 54, 3, 6)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('복양  조조 저택')
    stage.say('&193\n……성지를 받들라.\n대신 조조는 속히 폐하의 곁으로 와서 옥체를 호위하여\n충절을 보이도록 하라.\n조조, 그대도 따르겠지요?\n&0\n예! 폐하의 명을 받들겠습니다.\n&193\n오, 그럼 즉시 출병할 수 있습니까?\n&0\n예.\n&193\n과연 조 대인이십니다.\n그 충의를 아시면\n폐하께서도 크게 기뻐하실 것입니다.\n&193\n그럼 즉시 돌아가 폐하께 아뢰겠습니다.\n잘 부탁드립니다, 조 대인.')
    stage.unit(193).move(30, 48, 0)
    stage.delay(1)
    stage.unit(193).move(95, 48, 1)
    stage.unit(193).hide()
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0)
    stage.unit(21).setAction(0)
    stage.unit(20).setAction(0)
    stage.unit(22).setAction(0)
    if not vars[706]:
        stage.unit(18).setAction(0)
    stage.unit(16).setAction(0)
    stage.delay(3)
    stage.unit(0).move(50, 48, 0)
    stage.delay(1)
    stage.unit(0).move(30, 48, 3)
    stage.delay(1)
    stage.unit(0).move(30, 54, 2)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n흥! 제법 거드름을 피우는군.\n결국은 우리더러 살려 달라는 것 아닌가.\n&0\n원양, 함부로 말하지 말게.\n지금 황제는 천하에 위세를 내세우는 데나 쓸 수 있지.\n&0\n허나 문약, 우리가 기다리던 날이 마침내 왔구나.')
    stage.unit(20).setAction(6, 3)
    stage.say('&20\n예. 천자를 모셔 올 절호의 기회입니다.')
    if vars[706]:
        stage.effectSound(202, 255)
        stage.delay(3)
        stage.bgSound(18)
        stage.delay(3)
        stage.showUnit(18, 95, 54, 3)
        stage.say('&18\n실례했습니다.')
        stage.unit(1).setAction(0, 2)
        stage.unit(16).setAction(0, 2)
        stage.unit(20).setAction(0, 0)
        stage.unit(22).setAction(0, 0)
        stage.unit(21).setAction(0, 0)
        stage.unit(18).move(50, 54, 3)
        stage.say('&18\n주군, 방금 하북을 정탐하고 돌아왔습니다.')
        stage.unit(0).setAction(3)
        stage.say('&0\n오, 수고했네. 새로운 소식이 있는가?')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(18).setAction(6)
        stage.say('&18\n특별한 일은 없습니다만…….\n원소와 공손찬의 싸움에서는\n여전히 원소가 우세합니다.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n음, 그러한가?')
        stage.say('&18\n또한 원소는 천자를 구원하라는 성지를 거부했습니다.')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n뭐라고, 감히 황명을 거역하겠다고?!\n&18\n그렇습니다.\n조정에 기대어도 은상을 얻지 못하리라 여기는 것입니다.')
        stage.unit(0).setAction(3)
        stage.say('&0\n하하하, 은상이라고?\n어리석은 놈! 참으로 눈앞밖에 볼 줄 모르는군.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(0, 3)
        stage.say('&22\n원소에게는 역시 패업을 이룰 웅대한 지략이 없군요.')
        stage.say('&0\n자양, 수고했네.\n이제 천자를 구원하러 갈 것이니\n자네도 함께 가세.')
        stage.say('&18\n예.')
        model.unitJoin(18, 0, 0)
        if vars[1013]:
            gvars[0] = 18
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
    stage.say('&0\n좋다, 출진하라! 무슨 일이 있어도 폐하를 구해 내야 한다!')
    stage.bgSound(-1)
    stage.effectSound(202, 255)
    stage.delay(3)
    stage.bgSound(0)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, -1)
    stage.draw()
    stage.showHead(149, 246, 89)
    stage.delay(5)
    stage.bottomTxt('헌제가 장안을 빠져나간 뒤,', False, False, False)
    stage.delay(5)
    stage.showHead(131, 173, 46)
    stage.delay(5)
    stage.bottomTxt('이각', True, False, False)
    stage.delay(5)
    stage.showHead(129, 173, 126)
    stage.delay(5)
    stage.bottomTxt('곽사와 함께 끈질기게 뒤쫓았다.', True, False, False)
    stage.delay(8)
    stage.showHead(101, 403, 7)
    stage.delay(5)
    stage.bottomTxt('하북의 강자 원소는 헌제의 구원 요청을 거절했다.', True, True, False)
    stage.delay(8)
    stage.showHead(0, 445, 91)
    stage.delay(5)
    stage.bottomTxt('조조는 천자를 모실 좋은 기회를 놓치지 않고', True, True, False)
    stage.delay(6)
    stage.head(0).move(310, 89)
    stage.delay(5)
    stage.bottomTxt('헌제를 구원하러 나섰다.', True, False, True)
    return False


def scene5():
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
            1]])
    if not vars[706] and not vars[707]:
        stage.showUnits([
            [
                16,
                38,
                46,
                1],
            [
                15,
                38,
                40,
                1]])
    stage.showUnits([
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
    stage.setStageName('장안 근교  조조군 본진')
    if vars[706]:
        stage.say('&0\n전위는 어찌 된 것이냐? 아직 오지 않았느냐?')
        stage.unit(20).setAction(0, 2)
        stage.say('&20\n무슨 일이라도 생긴 것인가요?')
        stage.showUnit(16, 49, 16, 2)
        stage.say('&16\n주군.')
        stage.unit(20).setAction(0, 3)
        stage.unit(16).move(49, 46, 2)
        stage.delay(3)
        model.setFace(2)
        stage.unit(0).setAction(2, 0)
        stage.say('&0\n전위, 어디서 무엇을 하다 왔느냐!\n군의에까지 늦다니!')
        stage.unit(16).setAction(6, 2)
        stage.say('&16\n죄…… 죄송합니다.\n무예가 뛰어난 장사를 발견하여…….')
        model.setFace(0)
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n무예가 뛰어나다고?')
        stage.unit(16).setAction(0, 2)
        stage.say('&16\n예.')
        stage.unit(16).setAction(0, 0)
        stage.say('&16\n이봐! 들어오게!')
        stage.bgSound(18)
        stage.delay(3)
        stage.showUnit(15, 49, 16, 2)
        stage.unitsMove([
            [
                stage.unit(16),
                38,
                46,
                1],
            [
                stage.unit(15),
                49,
                40,
                2]])
        stage.delay(3)
        stage.unit(16).setAction(0, 2)
        stage.say('&16\n이 자는 허저라 합니다. 주군을 위해 일하고 싶다더군요.')
        stage.unit(15).setAction(11, 2)
        stage.say('&15\n저는 허저입니다. 잘 부탁드립니다.\n&0\n오, 과연 용맹한 장수로 보이는군.')
        stage.unit(16).setAction(0, 1)
        stage.say('&16\n이 사람은 대단합니다. 무예가 저와 막상막하입니다.')
        stage.unit(16).setAction(0, 2)
        stage.say('&16\n게다가 타고난 장사라\n사나운 소의 꼬리를 잡고 백여 보나 끌고 갈 수 있습니다!')
        model.setFace(3)
        stage.unit(0).setAction(3)
        stage.say('&0\n하하하, 정말인가? 과연 무예가 비범하군!')
        stage.say('&16\n그렇습니다!\n&0\n하하하, 좋다. 전장에서 실력을 지켜보겠네!\n전위, 허저는 자네에게 맡기겠네.\n&16\n오? 정말입니까……참으로 잘되었습니다.')
        stage.unit(16).setAction(0, 1)
        stage.say('&16\n이봐, 허저. 이리 오게.\n내가 군사를 이끌고 싸우는 법을 알려 주겠네.\n&15\n예!')
        stage.unit(15).setAction(0, 2)
        stage.delay(3)
        stage.unit(15).move(38, 40, 1)
        model.setFace(0)
        stage.unit(0).setAction(0)
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
    if vars[707] and not vars[706]:
        stage.say('&0\n전위는 어찌 된 게냐? 왜 아직 오지 않는가.')
        stage.unit(20).setAction(0, 2)
        stage.say('&20\n그렇군요. 무슨 일이 생긴 걸까요?')
        stage.showUnit(16, 49, 16, 2)
        stage.say('&16\n주군!')
        stage.unit(20).setAction(0, 3)
        stage.unit(16).move(49, 46, 2)
        stage.delay(3)
        model.setFace(2)
        stage.unit(0).setAction(2, 0)
        stage.say('&0\n전위, 어디서 무엇을 하다 왔느냐!\n군의에까지 늦다니!')
        stage.unit(16).setAction(6, 2)
        stage.say('&16\n죄…… 죄송합니다.\n무예가 뛰어난 장사를 발견하여…….')
        model.setFace(0)
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n무예가 뛰어나다고?')
        stage.unit(16).setAction(0, 2)
        stage.say('&16\n예.')
        stage.unit(16).setAction(0, 0)
        stage.say('&16\n이봐! 들어오게!')
        stage.bgSound(18)
        stage.delay(3)
        stage.showUnit(15, 49, 16, 2)
        stage.unitsMove([
            [
                stage.unit(16),
                38,
                46,
                1],
            [
                stage.unit(15),
                49,
                40,
                2]])
        stage.delay(3)
        stage.unit(16).setAction(0, 2)
        stage.say('&16\n이 자는 허저라 합니다. 주군을 위해 일하고 싶다더군요.')
        stage.unit(15).setAction(11, 2)
        stage.say('&15\n저는 허저입니다. 잘 부탁드립니다.\n&0\n오, 과연 용맹한 장수로 보이는군.')
        stage.unit(16).setAction(0, 1)
        stage.say('&16\n이 사람은 대단합니다. 무예가 저와 막상막하입니다.')
        stage.unit(16).setAction(0, 2)
        stage.say('&16\n게다가 타고난 장사라\n사나운 소의 꼬리를 잡고 백여 보나 끌고 갈 수 있습니다!')
        model.setFace(3)
        stage.unit(0).setAction(3)
        stage.say('&0\n하하하, 정말인가? 과연 무예가 비범하군!')
        stage.say('&16\n그렇습니다!\n&0\n하하하, 좋다. 전장에서 실력을 지켜보겠네!\n전위, 허저는 자네에게 맡기겠네.\n&16\n오? 정말입니까……참으로 잘되었습니다.')
        stage.unit(16).setAction(0, 1)
        stage.say('&16\n이봐, 허저. 이리 오게.\n내가 군사를 이끌고 싸우는 법을 알려 주겠네.\n&15\n예!')
        stage.unit(15).setAction(0, 2)
        stage.delay(3)
        stage.unit(15).move(38, 40, 1)
        model.setFace(0)
        stage.unit(0).setAction(0)
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
    stage.bgSound(0)
    stage.delay(3)
    stage.say('&0\n제장들, 마침내 천자를 받들 기회가 왔다.\n모두 각별히 힘써 주기 바란다.')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 9
    stage.setBattlePos([
        [
            17,
            11],
        [
            16,
            11],
        [
            18,
            12],
        [
            17,
            9],
        [
            19,
            9],
        [
            18,
            7],
        [
            17,
            4],
        [
            17,
            2],
        [
            19,
            3],
        [
            19,
            2],
        [
            18,
            1],
        [
            17,
            0],
        [
            18,
            2],
        [
            16,
            1],
        [
            19,
            0]])
    return False


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n나는 언제까지나 맹덕을 따를 생각이네.\n황제니 뭐니 하는 건 상관없어.')
    if stage.unitClickTest(22):
        if vars[706]:
            stage.say('&22\n유엽 선생의 부대는 포차대이지요.\n가장 긴 사거리를 자랑하니…….\n우리 군의 전력에 큰 도움이 될 것입니다.')
        if not vars[706]:
            stage.say('&22\n원소가 폐하를 구원하라는 성지를 거절했다지요.\n그자의 그릇도 그 정도일 뿐입니다.\n&0\n음, 허나 아직 우리 힘으로는 이길 수 없네!')
    if stage.unitClickTest(21):
        if vars[706]:
            stage.say('&21\n적병 부대는 지계 책략을 쓰지요.\n허저 장군이 책략을 쓰실 줄은 몰랐습니다.\n&15\n뭐…… 무슨 뜻입니까!\n&21\n하하, 별 뜻 없습니다…….')
        if vars[707] and not vars[706]:
            stage.say('&21\n적병 부대는 지계 책략을 쓰지요.\n허저 장군이 책략을 쓰실 줄은 몰랐습니다.\n&15\n뭐…… 무슨 뜻입니까!\n&21\n하하, 별 뜻 없습니다…….')
        if not vars[706] and not vars[707]:
            stage.say('&21\n마침내 천자를 받들 수 있게 되었습니다.')
    if stage.unitClickTest(16):
        if vars[706]:
            stage.say('&16\n정말입니다! 정말로 사나운 소를 끌고 간다니까요…….\n&0\n하하하, 믿네! 믿어!\n&16\n이보게, 허저. 자네도 뭐라고 좀 하게!\n&15\n예? ……무슨 말을요?\n&16\n자네는 참…….')
        if vars[707] and not vars[706]:
            stage.say('&16\n사실입니다! 정말 날뛰는 소를 끌었다니까요…….\n&0\n하하하, 믿네! 믿어!\n&16\n이봐, 허저.\n자네도 무슨 말 좀 해 보게!\n&15\n응? ……무슨 말을?\n&16\n이 사람 참…….')
        if not vars[706] and not vars[707]:
            stage.say('&16\n황제라…아직 뵌 적 없는데…….')
    if stage.unitClickTest(15):
        if vars[706]:
            stage.say('&15\n저는 적병이라 황무지에서는 날듯이 움직입니다!')
        if vars[707] and not vars[706]:
            stage.say('&15\n저는 적병이라 황무지에서는 날듯이 움직입니다!')
        if not vars[706] and not vars[707]:
            stage.say('&15\n잘하겠습니다.')
    if stage.unitClickTest(20):
        stage.say('&20\n전장 중앙과 북쪽에는 산이 있습니다.\n우리 군은 북동쪽에서 산 남쪽으로 진입합니다.\n&20\n참……헌제를 놓아준 사람은 가후라 합니다.\n&0\n가후 가문화……? 지금 폐하와 함께 있는가?\n&20\n……폐하의 만류를 뿌리친 듯합니다.\n지금 어디로 갔는지는 모릅니다…….\n&0\n음……. 적 가운데 가장 두려운 자는\n가후 가문화라 생각했지.\n&0\n그가 없다면 적도 두려울 것 없겠군.\n&20\n아닙니다……적을 얕보시면 위험합니다.\n특히 적에게는 아직 서황이 있습니다.\n&0\n서황? 들어 본 적 없군.\n&20\n무예가 뛰어난 맹장입니다.\n&0\n서황이라……?\n&20\n결코 방심하셔서는 안 됩니다.\n&0\n음…….')
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
