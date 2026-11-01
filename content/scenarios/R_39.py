# Source Generated with Decompyle++
# File: R_39.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    if vars[738]:
        model.unitJoin(9, 255, 0)
        stage.bgSound(13)
        stage.delay(3)
        stage.loadBg(2, 53)
        stage.showUnits([
            [
                0,
                54,
                80],
            [
                14,
                54,
                64,
                2,
                6],
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
                12,
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
        stage.setEventName('충의의 방덕')
        stage.setStageName('번성  조조군 본진')
        stage.say('&14\n주군께 면목이 없습니다.\n방 장군의 죽음은 제 책임입니다.\n&0\n그만두게. 방덕의 운명이 그러했던 것이지\n그대의 책임은 아니야.')
        stage.unit(14).setAction(0)
        stage.say('&14\n하지만…….\n&0\n우금, 우리도 그렇게 생각할 수밖에 없다.')
        stage.unit(14).setAction(6)
        stage.say('&14\n……예.\n주군, 아뢸 일이 하나 더 있습니다.\n이것을 살펴 주십시오…….')
        stage.unit(14).setAction(0)
        stage.delay(3)
        stage.unit(14).move(54, 76, 2)
        stage.delay(3)
        stage.unit(14).move(54, 64, 2)
        stage.say('&0\n이것은……?')
        stage.getItem(41, 0)
        stage.unit(14).setAction(6)
        stage.say('&14\n번성을 구원하러 가는 도중에\n방 장군께서 전 재산을 들여 사신 것입니다.\n&14\n자신에게 무슨 일이 생기면\n이것을 주군께 전하라고 하셨습니다.')
        stage.unit(14).setAction(0)
        stage.say('&0\n죽은 뒤에도 충심을 전하려고\n이것을 남겼다는 말인가?')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n방덕, 방덕이여.\n그대의 변함없는 충심을 기리고자\n이 조조가 유성추를 받겠네.\n&0\n방덕, 이제 지하에서 편히 쉬게.\n방덕이여, 그대는 참으로 불꽃처럼……\n뜨거운 마음을 지닌 사내였네!')
        stage.bgSound(-1)
        stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 294, 124)
    stage.showHead(3, 294, 204)
    stage.bottomTxt('조조군에게 패한 관우는', False, False, False)
    stage.delay(5)
    stage.head(3).move(254, 210)
    stage.delay(5)
    stage.bottomTxt('맥성에서 패주한 뒤,', True, False, False)
    stage.delay(10)
    stage.showHead(80, 368, 236)
    stage.delay(5)
    stage.bottomTxt('동오의 여몽은', True, True, False)
    stage.delay(5)
    stage.head(80).move(318, 210)
    stage.delay(5)
    stage.bottomTxt('끈질긴 추격을 이어 갔고', True, False, False)
    stage.delay(5)
    stage.head(3).hide()
    stage.delay(5)
    stage.bottomTxt('마침내 관우를 죽였다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('촉의 세력을 약화시키는 데 성공한 조조는', True, True, False)
    stage.delay(8)
    stage.head(0).move(331, 96)
    stage.delay(5)
    stage.bottomTxt('이에 허도로 돌아갔다.', True, False, True)
    stage.bottomTxt('그로부터 얼마 뒤,', False, False, False)
    stage.delay(5)
    stage.head(80).hide()
    stage.delay(5)
    stage.bottomTxt('여몽은 알 수 없이 발광하다 죽었다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('동오 백성들은 관우의 혼령이 재앙을 내린다 여겨 한동안 소란에 휩싸였다.', True, True, False)
    stage.delay(8)
    stage.showHead(76, 501, 208)
    stage.delay(5)
    stage.bottomTxt('손권은 인심을 얻고자,', True, True, False)
    stage.delay(5)
    stage.bottomTxt('유비의 보복을 피해', True, False, False)
    stage.delay(5)
    stage.bottomTxt('관우의 수급을 조조에게 보냈다.', True, False, True)
    stage.bgSound(13)
    stage.delay(3)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            1,
            50,
            70],
        [
            2,
            56,
            70],
        [
            8,
            62,
            70],
        [
            15,
            68,
            70],
        [
            20,
            50,
            41,
            2],
        [
            19,
            56,
            41,
            2],
        [
            23,
            62,
            41,
            2],
        [
            24,
            68,
            41,
            2],
        [
            21,
            74,
            41,
            2],
        [
            3,
            47,
            55,
            1]])
    stage.unit(201).move(48, 55, 1)
    stage.draw()
    stage.setEventName('촉과 오의 공방')
    stage.setStageName('허도  승상부')
    stage.unit(20).setAction(6)
    stage.say('&20\n동오에서 관우의 수급을 보내왔습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    model.setFace(1)
    stage.unit(0).setAction(9)
    stage.say('&0\n오오……')
    stage.unit(2).setAction(9)
    stage.say('&2\n관우……')
    stage.unit(19).setAction(6)
    stage.say('&19\n손권은 관우를 죽인 책임을\n우리에게 떠넘길 생각인 듯합니다.')
    model.setFace(0)
    stage.say('&0\n손권도 헛수고를 하는군.\n&19\n예?')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0)
    stage.say('&0\n유비가 이대로 물러날 리 없다.\n&0\n동오도 관우를 죽인 일을 후회하기 시작했겠군.')
    stage.unit(21).setAction(6)
    stage.say('&21\n하지만 촉이 동오에 복수하려 드는 것은\n아무 이득도 없이 해롭기만 합니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n공명도 틀림없이 알고 있을 것입니다……')
    stage.unit(24).setAction(0)
    stage.say('&0\n공명도 복수가 어리석다는 것을 알았겠지.\n허나 유비를 말리지는 못했군.')
    stage.unit(23).setAction(6)
    stage.say('&23\n유비가 복수에 성공할 수 있을까요?')
    stage.unit(23).setAction(0)
    stage.say('&0\n감정에 휩쓸려 일으킨 전쟁으로는 이길 수 없다.\n유비의 처지도 참으로 가련하군.')
    stage.say('&0\n장료.')
    stage.unit(2).setAction(0)
    stage.say('&2\n예……\n&0\n관우의 수급을 국장으로 후히 장사지내게.\n이 일을 자네에게 맡겨도 되겠는가?')
    stage.unit(2).setAction(6)
    stage.say('&2\n예……\n분부대로 하겠습니다.')
    stage.unit(2).setAction(0)
    stage.say('&0\n이만 모두 물러가라.\n관우와 단둘이 이야기를 나누고 싶구나……')
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(21),
            98,
            41,
            1],
        [
            stage.unit(24),
            98,
            41,
            1],
        [
            stage.unit(23),
            98,
            41,
            1],
        [
            stage.unit(19),
            98,
            41,
            1],
        [
            stage.unit(20),
            98,
            41,
            1],
        [
            stage.unit(15),
            98,
            70,
            1],
        [
            stage.unit(8),
            98,
            70,
            1],
        [
            stage.unit(2),
            98,
            70,
            1],
        [
            stage.unit(1),
            98,
            70,
            1]])
    stage.unit(20).hide()
    stage.unit(19).hide()
    stage.unit(23).hide()
    stage.unit(24).hide()
    stage.unit(21).hide()
    stage.unit(1).hide()
    stage.unit(2).hide()
    stage.unit(8).hide()
    stage.unit(15).hide()
    stage.delay(10)
    stage.say('&0\n관우……\n&0\n옛날 그대와 나누었던 정을\n이 조조는 아직도 생생히 기억하네!')
    stage.unit(0).setAction(9)
    stage.say('&0\n관우여, 관우…….')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
    if model.ambition() >= 85:
        stage.loadBg(2, 8)
        stage.showUnits([
            [
                0,
                34,
                55,
                1],
            [
                1,
                50,
                70],
            [
                8,
                56,
                70],
            [
                15,
                62,
                70],
            [
                2,
                68,
                70],
            [
                20,
                50,
                41,
                2],
            [
                19,
                56,
                41,
                2],
            [
                23,
                62,
                41,
                2],
            [
                24,
                68,
                41,
                2],
            [
                21,
                74,
                41,
                2]])
        stage.draw()
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.info('조조를 비롯한 위의 장수들은\n관우의 죽음을 애도하며 상복을 입었다.\n그리고 상기가 거의 끝나 갈 무렵…….')
        stage.setStageName('허도  승상부')
        stage.unit(19).setAction(6)
        stage.say('&19\n주군, 어서 결단을 내려 주십사 청하고자\n오늘 백관이 모두 찾아왔습니다.\n&0\n결단이라고?\n&19\n그렇습니다. 한실의 위엄과 권세는 이미 사라졌으며\n하늘도 일찍이 한나라를 버렸습니다.\n&0\n그 정도는 나도 알고 있네.\n천하가 셋으로 나뉜 지도 참으로 오래되었지.\n&19\n더구나 삼국 가운데 주군의 세력이 가장 강하고\n나라의 정치는 맑으며 백성의 삶은 풍요롭습니다.\n&19\n모두 주군의 인덕 덕분입니다……')
        stage.say('&0\n잠깐.\n&0\n사마의, 나더러 황위에 오르라는 말인가?\n&19\n그렇습니다. 황제의 자리에 오르실 분은 주군뿐입니다.')
        stage.unit(19).setAction(0)
        stage.say('&0\n……모두들 그리 생각하는가?')
        stage.unit(21).setAction(6)
        stage.say('&21\n사마의의 말씀이 옳습니다.')
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(23).setAction(6)
        stage.say('&23\n주군께서 새 황제가 되셔야 합니다.')
        stage.unit(23).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(0, 3)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n맹덕, 나도 같은 생각일세.')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(8).setAction(0, 3)
        stage.say('&8\n아버님, 반드시 황위에 오르십시오.\n&0\n아까도 말하지 않았느냐.\n아직 천하가 셋으로 갈라져 있으니\n즉위를 논하기에는 이르다.')
        stage.unit(19).setAction(6)
        stage.say('&19\n지금은 분명 천하가 셋으로 갈라져 있습니다만\n주군께서는 언젠가 천하를 통일하실 것입니다.\n&19\n머지않아 반드시 그렇게 될 것입니다.')
        stage.unit(19).setAction(0)
        stage.say('&0\n사마의, 천문에 그리 나타나 있는가?')
        stage.unit(19).setAction(6)
        stage.say('&19\n그렇습니다, 주군.\n부디 어서 황제의 자리에 오르기로 결단을 내리십시오!')
        stage.unit(19).setAction(0)
        stage.say('&0\n음……')
        stage.effectSound(202, 255)
        stage.bgSound(11)
        stage.delay(3)
        sel = stage.choice('황제에 오른다\n제위에 오르지 않는다', 0)
        if sel == 1:
            stage.addAmbition(20)
            vars[313] = 1
            stage.bgSound(-1)
            stage.effectSound(202, 0)
            stage.delay(3)
            stage.unit(0).setAction(3)
            stage.say('&0\n좋다.\n천하를 통일하고 나면\n황제의 자리에 오르겠다.')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(19).setAction(6)
            stage.say('&19\n오, 마침내 결심하셨군요.')
            stage.unit(19).setAction(0)
            stage.say('&0\n천하에 단 하루도 주인이 없어서는 안 되지.\n천하를 통일하기 위해서라면\n어쩔 수 없이 그리해야겠군.\n&8\n축하드립니다, 아버님!\n&0\n조비, 오해하지 마라.\n천하를 통일한 뒤의 이야기다.')
            stage.unit(0).setAction(17)
            stage.say('&0\n이 말은 모두에게 분명히 해 두려는 것이다.\n지금 우리가 생각할 것은 오직 싸움뿐이다.')
        elif sel == 2:
            stage.addAmbition(-20)
            vars[313] = 0
            stage.bgSound(-1)
            stage.effectSound(202, 0)
            stage.delay(3)
            stage.say('&0\n나는 황제의 자리에 오를 생각이 없소.')
            stage.unit(19).setAction(6)
            stage.say('&19\n어, 어째서입니까!?')
            stage.unit(19).setAction(0)
            stage.say('&0\n무력으로 천하를 통일하고\n지혜로 천하를 다스리면 될 일이지.\n황제가 되는 것은 짐만 늘리는 일일세.')
            stage.unit(0).setAction(0, 3)
            stage.say('&0\n나는 오직 패업을 이루려 할 뿐,\n황위는 내게 필요 없네.')
            stage.unit(20).setAction(6)
            stage.say('&20\n하지만 주군, 천하에는 하루도 주인이 없어서는 안 됩니다.\n주군께서 제위에 오르지 않으신다면……')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(0, 1)
            stage.say('&0\n천자가 없어도 천하를 다스릴 수는 있지.\n그러니 천자는 되지 않는 편이 좋겠다.')
            stage.unit(19).setAction(6)
            stage.say('&19\n주군께서는 천자를 어찌 여기십니까?')
            stage.unit(19).setAction(0)
            stage.say('&0\n천자는 사람이면서도 사람이 아니네.\n결국 천자라는 이름의 존재일 뿐이지.\n&0\n내가 천자가 되면 나 자신을 잃게 될 터인데\n그대들은 나 조맹덕더러 사람으로 살기를 그만두라는 것인가?')
            stage.unit(0).setAction(3)
            stage.say('&0\n천하는 사람의 천하일세.\n사람이 아닌 자가 어찌 정사를 펼칠 수 있겠는가?')
            stage.unit(0).setAction(0)
            stage.say('&0\n원양, 황제가 되고 싶지는 않은가?\n폐하라 불러 줄까?')
            stage.unit(1).setAction(2)
            stage.say('&1\n맹덕!')
            stage.unit(1).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(3)
            model.setFace(3)
            stage.say('&0\n하하하.\n내 말은 여기까지다.\n이제 쉬어야겠구나.')
            model.setFace(0)
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(0).move(95, 55, 1)
            stage.unit(0).hide()
            stage.unit(1).setAction(0, 0)
            stage.unit(8).setAction(0, 0)
            stage.delay(3)
            stage.unit(21).setAction(0, 1)
            stage.say('&21\n주군은 대체 어떤 분이신가…….\n황위마저 아무 의미가 없다 하시다니…….')
            stage.unit(20).setAction(0, 1)
            stage.say('&20\n주군께 황제가 되시라 권하는 것은\n주군의 뜻을 버리시라 권하는 것이나 다름없습니다.')
        stage.effectSound(202, 255)
        stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            32,
            32,
            54,
            1],
        [
            35,
            52,
            41,
            2],
        [
            34,
            58,
            70],
        [
            33,
            52,
            70]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('그 무렵 촉에서는……', 3)
    stage.setStageName('성도  유비의 관저')
    stage.delay(3)
    stage.unit(33).setAction(0, 3)
    stage.delay(3)
    stage.unit(33).setAction(3)
    stage.say('&33\n뭐라고!?\n그, 그렇다면 둘째 형님의 원수를 갚을 수 있다는 말이오?')
    stage.unit(33).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(17)
    stage.say('&32\n음, 동오를 쳐서 관우의 원수를 갚겠다!\n셋째야, 네가 선봉을 맡아라.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(33).setAction(3)
    stage.say('&33\n좋소! 즉시 준비하겠소.\n손권, 이 푸른 눈의 도적놈!\n내가 반드시 네 목을 베어 주마!')
    stage.unit(33).setAction(0)
    stage.delay(3)
    stage.unit(33).move(52, 74, 2)
    stage.unit(33).move(98, 74, 1)
    stage.unit(33).hide()
    stage.unit(34).setAction(0, 3)
    stage.delay(3)
    stage.unit(34).setAction(6)
    stage.say('&34\n주군, 동오를 공격하지 마십시오.\n아직 대전을 벌일 때가 아닙니다.')
    stage.unit(34).setAction(0)
    stage.say('&32\n조운……')
    stage.unit(34).setAction(6)
    stage.say('&34\n싸우려면 위나라와 싸워야 할 것 아닌가!')
    stage.unit(34).setAction(0)
    stage.say('&32\n그만하게, 자룡.\n내 뜻은 이미 정해졌네!')
    stage.unit(35).setAction(6)
    stage.say('&35\n주군……')
    stage.say('&32\n공명, 그대도 반대하는가?\n&35\n그렇습니다.\n주군, 자룡의 말이 옳습니다.\n지금은 동오를 공격할 때가 아닙니다.')
    stage.unit(35).setAction(0)
    stage.say('&32\n알고 있네.\n위가 줄곧 한중을 노리고 있으니\n지금 동오를 공격하는 것은 분명 하책이지.')
    stage.say('&32\n하지만……그렇게 할 수밖에 없소!')
    stage.unit(32).setAction(2)
    stage.say('&32\n황건군을 토벌하던 때부터\n함께 싸운 의형제가 죽었소.\n동오와는 결코 같은 하늘 아래 살 수 없소!')
    stage.unit(35).setAction(6)
    stage.say('&35\n주군께서 그리 말씀하시니\n저도 더는 말릴 수 없군요.')
    stage.unit(35).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(0)
    stage.say('&32\n공명, 자네는 성도에 남아\n위를 경계해 주게.')
    stage.unit(35).setAction(6)
    stage.say('&35\n예……')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 333, 96)
    stage.showHead(76, 499, 203)
    stage.showHead(32, 5, 172)
    stage.bottomTxt('유비', False, False, False)
    stage.delay(5)
    stage.showHead(33, 69, 172)
    stage.delay(5)
    stage.bottomTxt('장비와 함께', True, False, False)
    stage.delay(5)
    stage.bottomTxt('관우의 원수를 갚고자 동오 정벌을 준비했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('하지만 출발 전날 밤,', True, True, False)
    stage.delay(8)
    stage.bottomTxt('장비', True, False, False)
    stage.delay(6)
    stage.showHead(299, 133, 212)
    stage.delay(5)
    stage.bottomTxt('잠든 사이 부하에게 목을 베였고', True, False, False)
    stage.delay(8)
    stage.head(33).hide()
    stage.delay(5)
    stage.bottomTxt('떠나기도 전에 죽었군.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('배신한 부하들은 장비의 수급을 예물로 삼아', True, True, False)
    stage.delay(5)
    stage.head(299).move(435, 203)
    stage.delay(5)
    stage.head(299).hide()
    stage.delay(5)
    stage.bottomTxt('동오에 의탁했다.', True, False, True)
    stage.bottomTxt('관우와 장비의 원수를 갚고자', False, True, False)
    stage.delay(8)
    stage.head(32).move(135, 201)
    stage.bottomTxt('유비는 직접 대군을 이끌고 동오로 향했고,', True, False, False)
    stage.head(32).move(249, 211)
    stage.delay(5)
    stage.bottomTxt('머지않아 형주로 다가갔다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('손권', True, True, False)
    stage.delay(5)
    stage.showHead(81, 435, 203)
    stage.delay(5)
    stage.bottomTxt('육손을 대도독으로 임명하고', True, False, False)
    stage.delay(5)
    stage.head(81).move(313, 211)
    stage.delay(5)
    stage.bottomTxt('촉군을 맞아 싸웠다.', True, False, True)
    stage.bottomTxt('육손은 이릉에서 화공으로 촉군을 크게 무찌르고', False, True, False)
    stage.delay(8)
    stage.head(32).move(161, 215)
    stage.head(81).move(435, 203)
    stage.head(81).hide()
    stage.delay(5)
    stage.bottomTxt('패주하는 촉군을 백제성으로 물러나게 했다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('그 뒤 유비의 병세는 나빠지기 시작했고,', True, True, False)
    stage.delay(8)
    stage.bottomTxt('유비는 끝내 실의에 잠긴 채', True, False, False)
    stage.delay(5)
    stage.head(32).hide()
    stage.delay(5)
    stage.bottomTxt('세상을 떠났다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('촉에서는 유비의 아들이', True, True, False)
    stage.delay(5)
    stage.showHead(67, 26, 173)
    stage.delay(5)
    stage.bottomTxt('유선이 대업을 이어받았고', True, False, False)
    stage.delay(8)
    stage.showHead(35, 90, 173)
    stage.delay(5)
    stage.bottomTxt('제갈량', True, False, False)
    stage.delay(5)
    stage.bottomTxt('후주 유선을 온 힘을 다해 보좌했다.', True, False, True)
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
            50,
            70],
        [
            8,
            56,
            70],
        [
            15,
            62,
            70],
        [
            2,
            68,
            70],
        [
            20,
            50,
            41,
            2],
        [
            19,
            56,
            41,
            2],
        [
            23,
            62,
            41,
            2],
        [
            24,
            68,
            41,
            2],
        [
            21,
            74,
            41,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n그런가…?\n유비가 죽었다고……?')
    stage.unit(19).setAction(6)
    stage.say('&19\n그렇습니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n감정에 휩쓸려 출병하다니 참으로 어리석군.\n허나 그가 난세에 설 수 있었던 것도\n바로 그 인덕 덕분이지.\n&0\n이 또한 정해진 운명인가?\n유비……')
    stage.unit(1).setAction(0, 3)
    stage.say('&1\n하지만 맹덕,\n지금은 촉을 공격할 좋은 기회가 아닌가.')
    stage.unit(21).setAction(6)
    stage.say('&21\n그렇습니다.\n촉은 패전으로 국력이 쇠한 데다\n유비까지 잃었으니 설상가상입니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n그렇습니다.\n지금이 바로 한중을 되찾고\n촉을 멸할 기회입니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n음.\n좋다. 출진을 준비하라.\n한중을 공격하겠다.')
    stage.unit(0).setAction(0)
    stage.say('&0\n(유비, 유비여………\n내 손으로 그대의 옛 나라를 조문하겠네.\n그대가 세운 촉을 온 힘을 다해 멸하겠어!)')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            67,
            32,
            54,
            1],
        [
            35,
            50,
            41,
            2]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.info('한편 촉에서는……', 3)
    stage.setStageName('성도  유선의 저택')
    stage.say('&67\n공명, 우리 나라의 존망이 달렸으니\n반드시 동오와 손잡아야 한다는 말인가……?')
    stage.unit(35).setAction(6)
    stage.say('&35\n그렇습니다. 달리 선택할 길이 없습니다.\n지난 원한은 모두 잊고\n촉과 동오의 관계를 회복하시지요.')
    stage.unit(35).setAction(0)
    stage.say('&67\n하지만 동오는 위와 동맹인데\n우리와 손을 잡겠는가……?\n&35\n위오 동맹은 오래가지 못합니다.\n&35\n지금 위가 이토록 강하니\n동오 혼자서는 이길 수 없습니다.\n게다가 오와 촉이 다투면 위만 이로울 뿐이지요.\n&35\n이대로는 천하가 반드시 조위로 돌아가니\n오와 촉이 다시 화친해야만\n위에 멸망당하는 것을 막을 수 있습니다.')
    stage.unit(35).setAction(11)
    stage.say('&35\n동오도 이 점은 잘 알고 있을 것입니다.')
    stage.unit(35).setAction(0)
    stage.say('&67\n음……좋소.\n이 일은 전적으로 그대에게 맡기겠소.')
    stage.unit(35).setAction(6)
    stage.say('&35\n뜻을 받아 주셔서 감사합니다, 주군.')
    stage.unit(35).setAction(0)
    stage.showUnit(183, 90, 54, 3)
    stage.say('&183\n아뢰옵니다.')
    stage.say('&183\n주군, 위나라가 한중으로 진군했습니다.\n&67\n뭐라고?\n위, 위나라가 한중을 공격하다니……\n&35\n과연 왔군요.\n조조는 시기를 놓치는 법이 없습니다.\n&67\n그, 그렇다면 위나라의 병력은 얼마나……\n&183\n엄청난 대군입니다.\n&67\n고……고……공명,\n이, 이를 어찌하면 좋겠는가……?')
    stage.unit(35).setAction(6)
    stage.say('&35\n주군, 염려 마십시오.\n위나라의 침공은 이미 예상하고 있었습니다.')
    stage.unit(35).setAction(0)
    stage.say('&35\n한중을 공격하려면 양평관을 지나야 합니다.\n그곳에서 정예 부대로 적을 맞겠습니다.\n&67\n괘, 괜찮겠지……?')
    stage.unit(35).setAction(6)
    stage.say('&35\n그렇습니다.\n허나 이렇게 된 이상\n서둘러 동오와 우호를 회복해야 합니다.')
    stage.unit(35).setAction(0)
    stage.say('&35\n즉시 동오에 사자를 보내라.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene6():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 334, 98)
    stage.showHead(67, 23, 170)
    stage.bottomTxt('조조군', False, False, False)
    stage.delay(5)
    stage.head(0).move(143, 59)
    stage.delay(5)
    stage.bottomTxt('양평관으로 진격했고,', True, False, False)
    stage.delay(8)
    stage.head(67).hide()
    stage.delay(5)
    stage.showHead(35, 23, 170)
    stage.delay(5)
    stage.bottomTxt('제갈량은 촉군을 이끌고', True, True, False)
    stage.delay(5)
    stage.head(35).move(79, 87)
    stage.delay(5)
    stage.bottomTxt('맞아 싸운다.', True, False, True)
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
            4,
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
            24,
            64,
            52,
            3]])
    stage.draw()
    stage.setStageName('양평관 앞  조조군 본진')
    stage.say('&0\n순유, 촉군의 움직임은 어떠한가?')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n예.\n방금 정찰병이 돌아와\n촉군이 양평관에 집결 중이라고 보고했습니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n공명은 틀림없이 무슨 계략을 준비했을 것입니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n음.\n하지만 양평관을 함락하지 않으면\n한중으로 들어갈 수 없다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n모두 방심하지 마라!')
    stage.unit(0).setAction(0)
    stage.unit(19).setAction(0, 3)
    stage.unit(24).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19,
        15])
    gvars[4076] = 0
    gvars[4057] = 39
    stage.setBattlePos([
        [
            1,
            4],
        [
            1,
            5],
        [
            2,
            5],
        [
            2,
            4],
        [
            2,
            6],
        [
            3,
            5],
        [
            1,
            6],
        [
            3,
            4],
        [
            0,
            7],
        [
            4,
            3],
        [
            0,
            6],
        [
            3,
            3],
        [
            0,
            5],
        [
            2,
            3],
        [
            1,
            3]])
    return False


def scene7():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n전장의 동쪽과 남쪽에 각각 관문이 있으며\n동쪽 양평관을 통과하면 한중에 닿습니다.\n&0\n음.\n무슨 일이 있어도 양평관을 돌파해야 한다.')
    if stage.unitClickTest(15):
        stage.say('&15\n주변이 온통 산지라고요?\n이번에는 제가 한몫하겠군요!\n&0\n음.\n자네만 믿겠네, 허저.\n&15\n예!')
    if stage.unitClickTest(19):
        stage.say('&19\n이번 싸움은 삼국 정립의 형세를 깨뜨릴 중대한 일전입니다.')
    if stage.unitClickTest(24):
        stage.say('&24\n혼전이 될 터이니 철저히 준비해야 합니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n양평관은 제법 큰 모양이야.\n허나 아무리 큰 관문이라도\n온 힘을 다해 함락하는 수밖에 없지.')
    if stage.unitClickTest(4):
        stage.say('&4\n아버님, 부디 저를 출전시켜 주십시오.\n반드시 아버님을 위해 공을 세우겠습니다.')
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
