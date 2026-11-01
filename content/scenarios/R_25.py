# Source Generated with Decompyle++
# File: R_25.marshal (Python 3.9)


def scene0():
    '곽가의 죽음'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.delay(3)
    stage.setMenuVisible(False)
    if not vars[724]:
        stage.bgSound(13)
        stage.delay(3)
        stage.loadBg(2, 5)
        stage.showUnits([
            [
                22,
                55,
                46,
                2,
                10],
            [
                203,
                58,
                43,
                2,
                14],
            [
                0,
                52,
                52],
            [
                1,
                60,
                52],
            [
                2,
                58,
                60]])
        stage.draw()
        stage.setEventName('곽가의 죽음')
        stage.setStageName('남피  곽가 저택')
        stage.say('&0\n봉효!')
        stage.unit(1).setAction(0, 3)
        stage.say('&1\n맹덕……')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n원양, 봉효의 병세는 어떠한가?\n&1\n의원의 말로는\n이미 손쓸 도리가 없다고 하네.')
        model.setFace(1)
        stage.say('&0\n뭐라고……!?')
        stage.say('&22\n……주군이십니까……?')
        stage.unit(1).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        model.setFace(0)
        stage.say('&0\n그래, 나다……\n&22\n주군…\n아무래도 여기서 작별해야 할 듯합니다……')
        stage.say('&0\n그대……지금 무슨 말을 하는 것이오?')
        stage.say('&22\n주군, 제가 죽으면 가후를 중히 써 주십시오.\n그라면…주군의 패업을 도울 수 있습니다……\n&0\n알겠네……\n&22\n그리고 이것도……')
        stage.unit(0).move(52, 48, 0)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.say('&0\n이것은?\n&22\n둔갑천서입니다.\n분명 주군께 도움이 될 것입니다.\n&0\n봉효……')
        stage.getItem(133, 0)
        stage.say('&22\n주군…… 으윽!\n콜록, 콜록!')
        stage.unit(0).setAction(0)
        stage.say('&0\n봉효, 더는 말하지 말게!')
        stage.say('&22\n……주군, 반드시 천하를 손에 넣으실 겁니다…….\n&22\n주군의 천하를 이 눈으로 보고 싶었는데…….\n콜록…… 콜록…… 으윽!!')
        model.setFace(1)
        stage.unit(0).setAction(2)
        stage.say('&0\n보, 봉효!\n봉효!!')
        stage.unit(1).setAction(2)
        stage.say('&1\n곽가 선생!')
        stage.unit(2).setAction(6)
        stage.say('&2\n곽가 선생!')
        stage.unit(0).setAction(4)
        model.setFace(0)
        stage.say('&0\n봉효야!!')
        model.unitJoin(22, 255, 0)
        stage.info('조조가 의지하던 모사 곽가가 세상을 떠났다.\n조조는 그를 떠올릴 때마다 탄식을 금치 못했다.\n참으로 뛰어난 인재의 요절이었다.')
        model.setFace(0)
        stage.bgSound(-1)
        stage.clsUnit()
    stage.bgSound(-1)
    stage.delay(3)
    if vars[724]:
        stage.loadBg(2, 46)
        stage.showUnits([
            [
                114,
                50,
                69],
            [
                115,
                50,
                55,
                2]])
        stage.draw()
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.setEventName('하북 평정')
        stage.setStageName('유성  원상의 저택')
        stage.unit(114).setAction(0, 1)
        stage.say('&114\n후우, 조조군은 정말 강하군.\n또 그들에게 밀리고 말았어.')
        stage.unit(115).setAction(6)
        stage.say('&115\n그나저나 형님, 앞으로 어찌하면 좋겠습니까?\n여러 차례 싸우는 동안\n병력도 거의 소진되었으니……')
        stage.unit(115).setAction(0)
        stage.delay(3)
        stage.unit(114).setAction(0, 0)
        stage.say('&114\n그래, 아우야. 북쪽으로 가는 것이 어떨까?')
        stage.unit(115).setAction(6)
        stage.say('&115\n음?\n유주로 가자는 말인가?')
        stage.unit(115).setAction(0)
        stage.say('&114\n그렇다.\n유주에만 도착하면\n조조군도 쉽게 공격해 오지는 못할 것이다.')
        stage.unit(114).setAction(3)
        stage.say('&114\n더구나 그곳은 큰 전쟁을 겪지 않았으니\n더 많은 병력을 징발할 수 있습니다.')
        stage.unit(114).setAction(0)
        stage.delay(3)
        stage.unit(115).setAction(3)
        stage.say('&115\n그렇군.\n허도와 유주는 멀리 떨어져 있으니\n조조도 쉽게 움직이지 못하겠어.')
        stage.unit(115).setAction(0)
        stage.delay(3)
        stage.unit(114).setAction(3)
        stage.say('&114\n좋다! 결정했다.\n즉시 떠나자.')
        stage.unit(114).setAction(0)
        stage.delay(3)
        stage.unit(115).setAction(6)
        stage.say('&115\n좋다!')
        stage.effectSound(200, 255)
        stage.clsUnit()
    stage.loadBg(2, 67)
    stage.draw()
    stage.showUnits([
        [
            186,
            48,
            95],
        [
            185,
            56,
            95]])
    if not vars[724]:
        stage.setEventName('하북 평정')
    stage.setStageName('유성 교외  산길')
    stage.unitsMove([
        [
            stage.unit(186),
            48,
            46,
            0],
        [
            stage.unit(185),
            56,
            46,
            0]])
    stage.unit(185).setAction(0)
    stage.unit(186).setAction(0)
    stage.say('&186\n정말이지!\n장난하는 것도 아니고!')
    stage.unit(185).setAction(0, 3)
    stage.say('&185\n이봐, 진정해.')
    stage.unit(186).setAction(0, 1)
    stage.say('&186\n이 마당에 진정할 수 있겠어?\n전쟁이 났다고 우리 농민들까지\n끌고 와서 싸우게 하다니!\n&186\n이제 싸움에서도 지고\n먹을 것마저 없으니\n대체 어쩌라는 거야!\n&185\n어쩔 수 없는 일이지 뭐.\n우리는 이름 없는 졸병에 불과하잖아.\n주장이 어떻게 생겼는지도 모르는데.\n&186\n하여간 거드름만 피우면 다냐고!\n명문 원씨가 뭐 그리 대단하다고!\n&185\n이봐, 목소리 좀 낮춰……\n&186\n흥, 아무도 안 와.\n누가 이런 데까지 오겠어……')
    stage.unit(186).setAction(0, 2)
    stage.say('&186\n……응?')
    stage.say('&185\n왜…… 왜 그래?')
    stage.unit(186).setAction(0, 1)
    stage.say('&186\n집안이 꽤 넉넉해 보이는 놈 둘이 왔어……\n이봐, 귀 좀 빌려 줘……\n&185\n왜 그러는데……?')
    stage.unit(185).move(52, 46, 3)
    stage.delay(3)
    stage.unit(185).setAction(0, 2)
    stage.bgSound(12)
    stage.delay(5)
    stage.say('&185\n응?\n주……죽이자고!?\n&186\n바……바보야!\n목소리 좀 낮춰!')
    stage.unit(185).setAction(0, 3)
    stage.say('&185\n하…… 하지만 왜 죽이는 거야……?\n&186\n어차피 저 두 놈도 생긴 걸 보니\n평소에 우리를 괴롭히던 놈들이 틀림없어.\n&186\n게다가 돈을 빼앗으면\n마을로 돌아가 편하게 살 수 있잖아.\n어서! 숨어!')
    stage.unit(186).move(32, 49, 1)
    stage.unit(186).setAction(0)
    stage.unit(185).move(66, 66, 3)
    stage.unit(185).setAction(0)
    stage.delay(3)
    stage.showUnits([
        [
            115,
            48,
            95],
        [
            114,
            56,
            95]])
    if not vars[724]:
        stage.unitsMove([
            [
                stage.unit(115),
                48,
                74,
                0],
            [
                stage.unit(114),
                56,
                74,
                0]])
        stage.delay(3)
        stage.unit(114).setAction(0, 3)
        stage.delay(3)
        stage.unit(114).setAction(3)
        stage.say('&114\n이봐, 셋째야. 서둘러라! 이제는\n북쪽 유주로 달아나는 수밖에 없다.')
        stage.unit(114).setAction(0)
        stage.delay(3)
        stage.unit(115).setAction(0, 1)
        stage.delay(3)
        stage.unit(115).setAction(6)
        stage.say('&115\n예.')
        stage.unit(115).setAction(0)
        stage.say('&114\n그곳에서 병마를 모아\n다시 일어설 준비를 하자.')
    if vars[724]:
        stage.unitsMove([
            [
                stage.unit(115),
                48,
                74,
                0],
            [
                stage.unit(114),
                56,
                74,
                0]])
        stage.delay(3)
        stage.unit(114).setAction(0, 3)
        stage.delay(3)
        stage.unit(114).setAction(3)
        stage.say('&114\n가자, 셋째야. 길을 서둘러야 한다!')
        stage.unit(114).setAction(0)
        stage.delay(3)
        stage.unit(115).setAction(0, 1)
        stage.delay(3)
        stage.unit(115).setAction(6)
        stage.say('&115\n예.')
        stage.unit(115).setAction(0)
    stage.unitsMove([
        [
            stage.unit(115),
            48,
            49,
            0],
        [
            stage.unit(114),
            56,
            49,
            0]])
    stage.unit(115).setAction(0)
    stage.unit(114).setAction(0)
    stage.bgSound(-1)
    stage.delay(5)
    stage.say('&186\n이봐!')
    stage.unit(115).setAction(0, 3)
    stage.say('&115\n음? 누구냐!?\n&186\n죽어라!!')
    stage.unit(186).move(44, 49, 1)
    stage.unit(114).setAction(0, 3)
    stage.delay(3)
    stage.effectSound(34, 1)
    stage.unit(186).setAction(13)
    stage.delay(8)
    stage.effectSound(36, 1)
    stage.unit(186).setAction(14)
    stage.say('&115\n으윽, 으아악!')
    stage.delay(5)
    stage.unit(115).setAction(10)
    stage.delay(3)
    stage.unit(114).setAction(3)
    stage.say('&114\n아! 아우야!\n가증스러운 졸개 놈!\n&185\n으으으으으………\n이렇게 된 이상……\n너도 함께 죽어라!')
    stage.unit(185).move(56, 53, 0)
    stage.delay(3)
    stage.effectSound(34, 1)
    stage.unit(185).setAction(13)
    stage.delay(8)
    stage.effectSound(36, 1)
    stage.unit(185).setAction(14)
    stage.say('&114\n아아아아……!')
    stage.delay(5)
    stage.unit(114).setAction(10)
    stage.say('&114\n내, 내가 어찌………\n이런 곳에서 죽다니…….\n아, 아버님…….')
    stage.say('&185\n주, 죽었어……\n이봐, 다 죽었잖아!\n&186\n바보야, 당연하지!\n어서 값나가는 물건을 챙겨.\n삼십육계 줄행랑이 제일이다!\n&185\n으, 응……\n&186\n이제 마을로 돌아갈 수 있겠군.')
    stage.clsUnit()
    stage.bgSound(0)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.delay(5)
    stage.showHead(0, 426, 0)
    stage.showHead(115, 490, 0)
    stage.showHead(114, 554, 0)
    stage.delay(8)
    stage.head(115).hide()
    stage.head(114).hide()
    stage.delay(5)
    stage.bottomTxt('한나라의 명문 원씨는 이렇게 멸망했다.\n지난날의 번영에 비하면 너무도 쓸쓸한 최후였다.', False, False, True)
    stage.bottomTxt('원씨가 멸망한 뒤 하북을 평정한 조조는\n단숨에 가장 강력한 제후로 떠올랐다.', False, False, True)
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.sectionName(3, '삼국 정립')
    stage.bgSound(-1)
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            0,
            52,
            28,
            2],
        [
            1,
            38,
            48,
            1],
        [
            15,
            38,
            54,
            1],
        [
            8,
            38,
            60,
            1],
        [
            13,
            38,
            66,
            1],
        [
            2,
            38,
            72,
            1],
        [
            20,
            67,
            48,
            3],
        [
            23,
            67,
            54,
            3],
        [
            24,
            67,
            60,
            3],
        [
            21,
            67,
            66,
            3],
        [
            25,
            67,
            72,
            3]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('제갈공명의 등장')
    stage.setStageName('업성  임시 승상부')
    stage.say('&0\n문약, 마침내 원씨를 멸했군.')
    stage.unit(20).setAction(6)
    stage.say('&20\n예! 그러면 후환이 없을 것입니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n이제 상대할 적은 남쪽과 서쪽이로군……')
    stage.unit(21).setAction(6)
    stage.say('&21\n이제 눈앞의 적은 형주의 유표와 유비,\n그리고 동오의 손권뿐입니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n특히 동오의 손권은\n그 번영과 기세가 놀라울 정도입니다.')
    stage.unit(23).setAction(0)
    stage.say('&0\n음, 손권이라……?')
    stage.showUnit(159, 52, 92)
    stage.say('&159\n아버님.')
    stage.unit(159).move(52, 48, 0)
    stage.say('&159\n아버님!\n&0\n오, 자건이구나.\n내가 없는 동안\n업성을 다스리느라 수고가 많았다.')
    stage.unit(159).setAction(6)
    stage.say('&159\n감사합니다, 아버님.')
    stage.unit(159).setAction(0)
    stage.say('&159\n아버님, 길조를 아뢰러 왔습니다.\n&0\n길조라고?')
    stage.unit(159).setAction(6)
    stage.say('&159\n그렇습니다! 누군가 성 밖 밭에서\n커다란 청동 참새상을 파냈답니다.\n&0\n커다란 청동 참새상?\n&159\n그렇습니다.')
    stage.unit(159).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n오! 참으로 길조입니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n예! 틀림없습니다.')
    stage.unit(21).setAction(0)
    stage.say('&0\n음……')
    stage.unit(1).setAction(3)
    stage.say('&1\n잠깐. 동작이 어째서 길조라는 건가?')
    stage.unit(1).setAction(0)
    stage.say('&13\n그렇습니다. 저도 도무지 모르겠습니다.')
    stage.unit(159).setAction(0, 2)
    stage.delay(3)
    stage.say('&8\n순임금의 고사에서 유래한 일입니다.')
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n순임금의 고사라고?\n&8\n예.')
    stage.say('&8\n옛 성왕 순임금께서 태어나실 때\n그 어머님께서 꿈을 꾸셨는데\n구슬과 옥으로 된 참새가 품 안으로 날아들었다 합니다.')
    stage.unit(8).setAction(3)
    stage.say('&8\n지금 아버님께서 구리 참새를 얻으신 것도\n패업을 이루실 길조인 것입니다.')
    stage.unit(8).setAction(0)
    stage.say('&1\n오! 그랬군…….')
    stage.unit(1).setAction(0, 1)
    stage.say('&159\n형님의 말씀이 지당합니다.')
    stage.unit(159).setAction(0, 0)
    stage.say('&159\n아버님, 이 길조를 기념하여\n높은 누대를 짓고 동작대라 이름 붙이면 어떻겠습니까?\n아버님의 뜻은 어떠하신지요?')
    stage.unit(21).setAction(6)
    stage.say('&21\n오, 조식 공자. 참으로 좋은 생각이십니다!')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(25).setAction(0, 0)
    stage.delay(3)
    stage.unit(25).setAction(6)
    stage.say('&25\n주군, 지금은 천하 백성에게……\n주군의 위엄을 보일 절호의 기회입니다.')
    stage.unit(25).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n주군, 부디 건설을 허락해 주십시오!')
    stage.unit(21).setAction(0)
    stage.say('&0\n음……')
    stage.effectSound(200, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('동작대를 짓는다\n동작대를 짓지 않는다', 0)
    if sel == 1:
        vars[309] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n좋다.\n자건, 네 말대로 참으로 길조로구나.\n하늘도 내가 패업을 이루기를 바라시는 모양이다.')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('&0\n자건, 네가 동작대 건설을 감독하거라.\n할 수 있겠느냐?')
        stage.unit(159).setAction(6)
        stage.say('&159\n예.\n제게 맡겨 주십시오.')
        stage.unit(159).setAction(0)
        stage.say('&0\n좋다! 그럼 모두 그 구리 참새를 보러 가세.')
        stage.unit(1).setAction(3)
        stage.say('&1\n예.')
        stage.unitsMove([
            [
                stage.unit(1),
                38,
                99,
                2],
            [
                stage.unit(8),
                38,
                99,
                2],
            [
                stage.unit(13),
                38,
                99,
                2],
            [
                stage.unit(15),
                38,
                99,
                2],
            [
                stage.unit(2),
                38,
                99,
                2],
            [
                stage.unit(25),
                67,
                99,
                2],
            [
                stage.unit(24),
                67,
                99,
                2],
            [
                stage.unit(21),
                67,
                99,
                2],
            [
                stage.unit(23),
                67,
                99,
                2],
            [
                stage.unit(159),
                52,
                99,
                2]])
        stage.unit(159).hide()
        stage.unit(1).hide()
        stage.unit(25).hide()
        stage.unit(15).hide()
        stage.unit(24).hide()
        stage.unit(8).hide()
        stage.unit(21).hide()
        stage.unit(13).hide()
        stage.unit(23).hide()
        stage.unit(2).hide()
        stage.say('&0\n……문약.')
        stage.unit(20).setAction(0, 0)
        stage.say('&20\n예.\n&0\n아까부터 말이 없던데, 어찌 된 일인가?\n동작대를 짓는 데 반대하는가?')
        stage.unit(20).setAction(6)
        stage.say('&20\n아, 아닙니다.\n저는 이견 없습니다.\n다만……\n&20\n주군께 여쭙고 싶은 일이 있습니다.\n&0\n무슨 일인가?\n&20\n……구리 참새를 땅에 묻은 사람이……\n설마 주군 본인은 아니십니까?!\n&0\n……정말 그렇게 생각하는가?')
    elif sel == 2:
        vars[309] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n짓는 것을 허락하지 않겠다!\n&159\n예!?\n&21\n뭐라고요?')
        model.setFace(0)
        stage.unit(23).setAction(0, 0)
        stage.delay(3)
        stage.unit(23).setAction(6)
        stage.say('&23\n주군, 어찌 그런 말씀을 하십니까!?\n저희에게도 알려 주십시오.')
        stage.unit(23).setAction(0)
        stage.say('&0\n이토록 높은 누대를 지으려면\n막대한 재물이 들 터이니\n당연히 백성에게서 거두어야겠지.')
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n하북은 오랜 세월 전란이 끊이지 않아\n땅이 황폐해지고 백성의 삶이 고달프다.')
        stage.unit(0).setAction(0, 2)
        stage.delay(3)
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n동작대를 짓겠다고\n또다시 백성의 고혈을 짜낼 수는 없다!')
        model.setFace(0)
        stage.unit(159).setAction(6)
        stage.say('&159\n어찌 재물을 짜냈다고 하십니까?\n아버님, 이것은 길조입니다……\n&0\n그만하라, 자건!\n이런 일에만 매달려서야\n어찌 국력을 기를 수 있겠느냐!\n&159\n…………………\n&0\n모두 밖으로 나가자!\n열병할 시간이 되었다.\n&1\n오, 그래……')
        stage.unitsMove([
            [
                stage.unit(1),
                38,
                99,
                2],
            [
                stage.unit(25),
                67,
                99,
                2],
            [
                stage.unit(15),
                38,
                99,
                2],
            [
                stage.unit(24),
                67,
                99,
                2],
            [
                stage.unit(8),
                38,
                99,
                2],
            [
                stage.unit(21),
                67,
                99,
                2],
            [
                stage.unit(13),
                38,
                99,
                2],
            [
                stage.unit(23),
                67,
                99,
                2],
            [
                stage.unit(2),
                38,
                99,
                2],
            [
                stage.unit(0),
                52,
                99,
                2]])
        stage.unit(0).hide()
        stage.unit(1).hide()
        stage.unit(25).hide()
        stage.unit(15).hide()
        stage.unit(24).hide()
        stage.unit(8).hide()
        stage.unit(21).hide()
        stage.unit(13).hide()
        stage.unit(23).hide()
        stage.unit(2).hide()
        stage.unit(20).setAction(6)
        stage.say('&20\n조식 공자…….')
        stage.unit(159).setAction(0)
        stage.delay(3)
        stage.unit(159).setAction(0, 1)
        stage.say('&159\n순욱 선생님.\n&159\n제가 쓸데없는 말을 꺼내어\n아버님을 노하시게 했습니다.\n&20\n어찌 쓸데없는 말이라 하십니까?\n&159\n하지만………\n&20\n조식 공자, 동작대를 지으십시오.\n&159\n예!?\n&20\n재물은 걱정하지 마십시오. 예전에 주군께서\n많은 보석을 내려 주셨습니다. 그것을 팔면\n동작대를 짓는 비용은 충분할 것입니다.\n&159\n하지만 아버님께서 분명히 말씀하셨는데\n어찌 아버님의 명을 어길 수 있겠습니까……?\n&20\n조식 공자,\n주군께서도 동작대를 짓는 이점을 알고 계십니다.\n&20\n다만……… 백성을 더 걱정하시는 것이지요.\n&20\n더구나 이번에는 제 재산을 쓰는 것이지\n백성에게서 거두는 것이 아니니\n주군께서도 결코 노하시지 않을 것입니다.\n&159\n하지만…………\n그것은 모두 선생님의 재산인데…….\n&20\n음…… 저는 재물에는 별로 관심이 없습니다.\n&20\n게다가 이 돈을 어디에 써야 할지 몰라\n오랫동안 고민하던 참이었지요.\n&159\n순욱 선생님…….\n&20\n동작대를 지으십시오.\n&20\n만에 하나 주군께서 노하시면\n제가 모든 죄를 짊어지겠습니다.\n&159\n순욱 선생님…….\n&20\n이제 괜찮으시겠지요?\n&159\n예!\n&20\n그러실 줄 알았습니다.\n조식 공자께서는 누구보다 총명하시니까요.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 370, 21)
    stage.bottomTxt('조조는 조식에게 업성을 다스리게 하고', False, False, False)
    stage.delay(5)
    stage.head(0).move(339, 102)
    stage.delay(5)
    stage.bottomTxt('허도로 돌아갔다.', True, False, True)
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(5)
    stage.showHead(32, 244, 166)
    stage.delay(5)
    stage.bottomTxt('유비는 유표의 부탁을 받아 신야의 수비를 맡았다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('이 소식을 들은 조조는', True, True, False)
    stage.head(0).hide()
    stage.delay(5)
    stage.showHead(5, 304, 70)
    stage.delay(5)
    stage.bottomTxt('이에 조인에게 명하여', True, False, False)
    stage.delay(5)
    stage.showHead(11, 368, 110)
    stage.delay(5)
    stage.bottomTxt('그리고 이전은', True, False, False)
    stage.delay(5)
    stage.head(5).move(244, 86)
    stage.head(11).move(308, 126)
    stage.delay(5)
    stage.bottomTxt('신야를 공격했다.', True, False, True)
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
            52,
            40,
            2],
        [
            23,
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
            2],
        [
            1,
            52,
            70],
        [
            15,
            58,
            70],
        [
            2,
            64,
            70],
        [
            5,
            52,
            58,
            3,
            11],
        [
            11,
            52,
            52,
            3,
            11]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.info('한 달 뒤…….', 3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n뭐라고? 대패했다고?\n&5\n그렇습니다. 소장의 무능을 용서하십시오.')
    stage.unit(21).setAction(6)
    stage.say('&21\n뭐라고……!')
    stage.unit(21).setAction(0)
    stage.say('&0\n어찌 된 일인가?\n&0\n관우와 장비가 천하에 드문 맹장이라지만\n자네들도 백전노장이지 않은가.\n어찌 그리 참패하고 돌아왔는가……\n&11\n저희가 패한 까닭은\n유비의 군사 단복의 지모에 미치지 못했기 때문입니다.\n&0\n군사 단복이라고?\n처음 듣는 이름이군.\n&5\n지략이 뛰어나 두려울 정도인 자입니다.\n우리의 진형과 움직임을 모두 간파했습니다.\n&0\n음……\n&11\n이미 패한 장수가\n어찌 변명할 수 있겠습니까.\n승상의 처분을 받겠습니다.\n&0\n그만두게.\n&5\n예?\n……주군?\n&0\n그만두라고 했네.')
    stage.unit(0).setAction(3)
    stage.say('&0\n승패는 병가지상사이니\n어쩔 수 없는 일이다.\n모두 수고했다. 물러가 쉬도록 하라.')
    stage.unit(0).setAction(0)
    stage.say('&5\n예.\n&11\n예!')
    stage.unitsMove([
        [
            stage.unit(11),
            98,
            52,
            1],
        [
            stage.unit(5),
            98,
            58,
            1]])
    stage.unit(11).hide()
    stage.unit(5).hide()
    stage.say('&0\n단복이 누구인지 아는 자가 있는가?')
    stage.unit(21).setAction(6)
    stage.say('&21\n주군, 저는 단복과 같은 고향 출신이라\n그자를 잘 압니다.\n&0\n중덕, 어떤 인물인가?')
    stage.unit(21).setAction(0)
    stage.say('&21\n단복의 본명은 서서입니다.\n지략이 매우 뛰어난 자지요.\n&21\n벗의 원한을 풀어 주려다\n살인을 저지른 뒤 종적을 감추었는데\n이제 유비에게 의탁했을 줄이야…….')
    stage.unit(21).setAction(6)
    stage.say('&21\n이제 서서가 보좌하고 있으니\n앞으로 유비를 무찌르기는 어려울 듯합니다.')
    stage.unit(21).setAction(0)
    stage.say('&0\n음……\n그대가 그런 말을 할 줄은 몰랐군……')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n그가 나를 위해 일해 준다면\n높은 벼슬과 후한 녹봉으로 중용할 텐데…….\n하필 유비에게 가다니…….')
    stage.unit(21).setAction(6)
    stage.say('&21\n주군, 아직 단념하실 때가 아닙니다.\n서서를 상대할 계책이 하나 있습니다……')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n무슨 계책인가?\n&21\n사실 서서의 어머니가 허도에 있습니다.')
    stage.unit(21).setAction(0)
    stage.say('&21\n서서는 효자입니다.\n그 어머니를 설득하여 불러들이면\n반드시 주군께 올 것입니다.\n&0\n음…….\n좋다, 즉시 시행하라.')
    stage.unit(21).setAction(6)
    stage.say('&21\n예.\n반드시 맡겨 주신 뜻에 보답하겠습니다.')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(0, 35)
    stage.draw()
    stage.info('그 뒤 정욱은 서서의 모친을 극진히 대접하며\n서서를 허도로 불러 달라고 설득했으나\n끝내 허락을 받지 못했다.')
    stage.info('정욱은 설득이 통하지 않자 서서 모친의 필체를 흉내 내어\n가짜 서신을 쓰고 사람을 시켜 서서에게 보냈다.')
    stage.info('거짓임을 몰랐던 서서는 모친을 향한 효심으로\n유비와 작별하고 조조가 있는 허도로 왔다.')
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            21,
            52,
            40,
            2],
        [
            151,
            52,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&151\n승상을 뵙습니다.\n저는 서서 서원직이라 합니다.\n&0\n내가 조맹덕이오.\n원직, 오래 기다렸소.\n&151\n후의에 감사드립니다.\n&0\n앞으로 모친과 함께 허도에서 지내며\n나를 위해 힘써 주시오.\n&151\n예, 승상.\n그런데 제 어머니께서는 어디 계십니까?\n&0\n성 밖의 집에 계실 것이오.\n&151\n그렇군요.\n어서 어머니를 뵙고 싶으니 이만 물러가겠습니다.\n&0\n음. 마음 놓고 다녀오시오.\n&151\n감사합니다, 승상.\n그럼 이만 물러가겠습니다.')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(151).move(98, 55, 1)
    stage.unit(151).hide()
    stage.say('&0\n중덕, 이번에는 참으로 잘해 주었네.')
    stage.unit(21).setAction(6)
    stage.say('&21\n감사합니다, 주군.\n&21\n(서찰이 거짓임을 알면\n서서는 우리를 위해 일하지 않겠지.)\n&21\n(하지만 이것으로 충분하다.\n유비의 힘을 꺾었으니까.)')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 36)
    stage.showUnit(152, 53, 42, 2)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  서서 모친의 거처')
    stage.showUnit(151, 53, 90)
    stage.say('&151\n어머니!\n&152\n원직이냐!?')
    stage.unitsMove([
        [
            stage.unit(151),
            53,
            85,
            0],
        [
            stage.unit(152),
            58,
            42,
            1]])
    stage.unitsMove([
        [
            stage.unit(151),
            53,
            74,
            0],
        [
            stage.unit(152),
            58,
            53,
            2]])
    stage.unitsMove([
        [
            stage.unit(151),
            53,
            61,
            0],
        [
            stage.unit(152),
            53,
            53,
            2]])
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('&151\n어머님, 소자가 얼마나 그리워했는지 모릅니다.\n&152\n오, 원직아. 오래간만이로구나.\n헌데 네가 어찌 여기에 왔느냐?\n유 황숙을 위해 일하고 있지 않았느냐?')
    stage.unit(151).setAction(0)
    stage.say('&151\n어머님, 어찌 그런 말씀을 하십니까?\n어머님의 서신을 받고\n현덕 주군께 작별을 고한 뒤 돌아온 것입니다.\n&152\n내 서신이라고?\n&151\n예, 여기 있습니다.')
    stage.unit(151).move(53, 57, 0)
    stage.delay(5)
    stage.unit(151).move(53, 61, 0)
    stage.say('&152\n이것은……?\n&152\n원직아, 어려서부터 시서를 배운 네가\n이런 위조 서신을 믿었단 말이냐……\n&151\n위조 서신이라고요!?\n&152\n어찌 그리 어리석으냐……\n네 어미가 어찌 이런 서신을 쓰겠느냐!\n그것조차 알아보지 못한단 말이냐?')
    stage.unit(152).setAction(0, 0)
    stage.say('&152\n나 같은 늙은이를 위해\n그토록 훌륭한 주군을 버리다니……\n&151\n저, 저는……')
    stage.unit(152).setAction(0, 2)
    stage.say('&152\n원직아, 충이 효에 앞선다는 도리도 모르느냐……?\n어리석구나…… 어찌 그리 어리석으냐…….')
    stage.unit(152).move(57, 53, 1)
    stage.say('&152\n모두 이 늙은 어미가 아직 살아 있는 탓이로구나……')
    stage.unit(152).move(57, 98, 2)
    stage.unit(152).hide()
    stage.unit(151).setAction(0, 2)
    stage.say('&151\n아! 어머님!')
    stage.delay(10)
    stage.effectSound(202, 255)
    stage.bgSound(13)
    stage.delay(3)
    stage.unit(151).setAction(4, 2)
    stage.say('&151\n어머님…….')
    stage.delay(10)
    stage.say('&189\n아악!!')
    stage.unit(151).setAction(0, 2)
    stage.say('&151\n무……무슨 일입니까!?')
    stage.clsUnit()
    stage.loadBg(2, 37)
    stage.showUnits([
        [
            152,
            45,
            54,
            1,
            10],
        [
            189,
            45,
            48,
            2]])
    stage.draw()
    stage.showUnit(151, 20, 54, 1)
    stage.say('&151\n어……어머님!!')
    stage.unit(151).move(39, 54, 1)
    stage.unit(151).setAction(0)
    stage.say('&189\n노부인께서 가……갑자기 칼을\n자신의 가슴에 꽂으셨습니다……')
    stage.unit(151).setAction(4)
    stage.say('&151\n어……어찌 이런 일이……?')
    stage.unit(151).setAction(9)
    stage.say('&151\n어머님, 모두 제가 어리석었던 탓입니다…….')
    stage.say('&151\n어……어머니!!\n흐으으윽!!')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene5():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.bottomTxt('이때,', False, False, False)
    stage.delay(8)
    stage.showHead(35, 311, 178)
    stage.delay(5)
    stage.bottomTxt('양양 서쪽의 작은 마을 융중에는 와룡이라 불리는 현사가 있었다.', True, True, False)
    stage.delay(5)
    stage.bottomTxt('그의 이름은 제갈량이었다.', True, True, True)
    stage.showHead(32, 233, 142)
    stage.delay(5)
    stage.bottomTxt('신야의 유비', False, False, False)
    stage.delay(5)
    stage.head(32).move(247, 178)
    stage.delay(5)
    stage.bottomTxt('삼고초려 끝에 제갈량을 모셔 왔고,', True, False, False)
    stage.delay(5)
    stage.head(32).move(223, 142)
    stage.head(35).move(287, 142)
    stage.delay(5)
    stage.bottomTxt('마침내 그를 출사시켜 군사로 모셨다.', True, True, True)
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
            52,
            40,
            2],
        [
            23,
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
            2],
        [
            1,
            52,
            70],
        [
            15,
            58,
            70],
        [
            2,
            64,
            70]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n방금 보고를 받았는데\n유비가 제갈량이라는 군사를 새로 얻어\n군무를 정비하고 훈련을 강화하고 있다더군.')
    stage.unit(20).setAction(6)
    stage.say('&20\n유비가 서서를 잃은 뒤\n다시 군사를 얻었다니…….\n&20\n분명 더욱 뛰어난 인물일 것입니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n음.\n&0\n저런 인재가 유비를 보좌하고 있으니\n지금 없애지 않으면\n훗날 더욱 까다로워질 것이다.')
    stage.unit(1).setAction(3)
    stage.say('&1\n아까부터 제갈량이니 뭐니 하던데\n그저 젊은 서생 하나 아닌가?\n맹덕, 내가 유비를 토벌하러 가겠네.\n&0\n자네가? 원양……')
    stage.unit(1).setAction(0)
    stage.say('&1\n공명이 뭐라고 그리 호들갑인가!\n한낱 풋내기에 불과하니\n별것 없을 걸세.')
    stage.unit(20).setAction(6)
    stage.say('&20\n하후돈 장군, 너무 방심해서는 안 됩니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n순욱 선생, 말씀 좀 해 보시오.\n이 하후돈은 믿을 수 있지 않소!\n이토록 많은 싸움을 치렀으니 경험도 충분하오.\n&20\n장군의 실력은 잘 알고 있습니다……\n주군, 어떻게 생각하십니까?\n&0\n음……')
    stage.effectSound(200, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('하후돈의 유비 공격을 허락한다\n공명의 소식을 기다린다', 0)
    if sel == 1:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.say('&0\n좋다. 그리 자신한다면\n원양, 그대를 보내 유비를 토벌하겠다.\n&0\n다만 그토록 호언장담했으니\n패하고 돌아오면 엄히 벌하겠다.\n&1\n좋다! 그때는 내 목을 내놓겠네.')
    elif sel == 2:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.say('&0\n원양, 그 기개는 훌륭하네만\n공명에 관한 정보를 얻은 뒤에 이야기하세.\n&1\n뭐……뭐라고!?\n맹덕이 그렇게 말해도\n나는 납득할 수 없네!\n&1\n이 하후돈이\n공명 같은 풋내기에게 질 것 같은가!?\n&0\n그런 뜻이 아닐세.\n어쨌든 조심하는 편이 좋겠네.\n&1\n뭐라 해도\n공명은 내가 상대하게 해 주게!\n&0\n패하지 않겠다고 장담할 수 있는가?\n&1\n물론이지! 내 목을 걸겠네.\n&0\n좋다.\n그리 말하니 출병을 허락하겠네.\n다만 방금 한 말은 잊지 말게.\n&1\n음!\n이 하후돈은 한 번 한 말은 반드시 지키네!')
    stage.unit(1).setAction(0)
    stage.say('&0\n좋다! 우금과 이전도 함께 보내\n네 힘이 되어 주도록 하마.\n너희에게 기대하겠다.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene6():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(32, 245, 172)
    stage.showHead(1, 335, 93)
    stage.delay(5)
    stage.bottomTxt('하후돈,', False, False, False)
    stage.delay(5)
    stage.showHead(14, 271, 83)
    stage.delay(5)
    stage.bottomTxt('우금과', True, False, False)
    stage.delay(5)
    stage.showHead(11, 399, 103)
    stage.delay(5)
    stage.bottomTxt('이전 등 세 사람은', True, False, False)
    stage.delay(8)
    stage.head(14).move(211, 92)
    stage.head(1).move(275, 92)
    stage.head(11).move(399, 172)
    stage.head(11).move(309, 172)
    stage.delay(5)
    stage.bottomTxt('신야로 진군하여 유비를 공격했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 13)
    stage.showUnit(32, 73, 55, 3)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('신야  유비 저택')
    stage.showUnit(35, 5, 55, 1)
    stage.say('&35\n보고드립니다.')
    stage.unit(35).move(55, 55, 1)
    stage.say('&35\n주군, 부르셨습니까?\n&32\n오, 군사.\n기다리고 있었네.\n&32\n방금 보고를 받았는데\n조조군이 신야로 진군하고 있다는군.\n&35\n그렇습니까?\n&32\n대군의 기세가 두려울 정도라더군!\n어찌하면 좋겠는가?\n이 작은 성으로는 지키기 어렵지 않겠나.\n&35\n조조군의 주장은 누구입니까?\n&32\n하후돈일세.')
    stage.say('&35\n그 사람이었습니까?\n주군, 염려하지 마십시오.\n모두 제게 맡겨 주십시오.\n&32\n하, 하지만……\n&35\n하후돈은 분명 무예가 뛰어난 대장이나\n용맹할 뿐 지략이 없으니 두렵지 않습니다.\n&32\n하지만 어떻게 상대할 생각인가……?')
    stage.unit(35).move(63, 55, 1)
    stage.unit(35).setAction(0)
    stage.say('&35\n묘책이 있습니다……')
    stage.unit(35).setAction(11)
    stage.say('&35\n이렇게 하시지요…….')
    stage.unit(32).setAction(0, 2)
    stage.say('&32\n오, 오오!!\n군사는 과연 지략이 뛰어나구려!')
    stage.unit(35).setAction(0)
    stage.say('&35\n이제 주군께서도 안심하시겠지요?')
    stage.unit(32).setAction(0, 3)
    stage.say('&32\n음, 이제 마음이 놓이는군.\n조조군 따위는 두려울 것 없네!\n&35\n모두 침착하게 기다리십시오.\n조조군이 제 발로 함정에 들어올 것입니다.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene7():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            1,
            54,
            80],
        [
            11,
            44,
            64,
            1],
        [
            14,
            64,
            64,
            3]])
    stage.draw()
    stage.setStageName('박망파  조조군 본진')
    stage.unit(1).setAction(17)
    stage.say('&1\n이번에는 공명 그 애송이에게\n백전노장들의 무서움을 똑똑히 보여 주겠다!')
    stage.unit(1).setAction(0)
    if vars[1011]:
        model.unit(1).setPosts(78)
        stage.setFAvatar(1, 83)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 9, [
        1,
        14,
        11], [
        0,
        22])
    gvars[4076] = 0
    gvars[4057] = 25
    stage.setBattlePos([
        [
            10,
            11],
        [
            10,
            9],
        [
            11,
            8],
        [
            9,
            10],
        [
            11,
            10],
        [
            9,
            8],
        [
            12,
            9],
        [
            10,
            7],
        [
            12,
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


def scene8():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&1\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(11):
        stage.say('&1\n이 장군, 표정이 어둡군.\n무슨 일인가?\n&11\n하후돈 장군, 공명이 젊다 해도\n방심해서는 안 됩니다.\n&1\n공명이라는 자를 아는가?\n&11\n아닙니다. 공명에 관해서는 아무것도 모릅니다.\n허나 유비가 그를 불러들여\n서서 대신 군사를 맡겼다면……\n&1\n이 장군, 자네 같은 용장이\n어찌 그리 기운 빠지는 말을 하는가?\n싸움의 승패는 무력과 경험에 달린 것일세.')
    if stage.unitClickTest(14):
        if not vars[724]:
            stage.say('&14\n이 박망파를 넘으면 신야성입니다.')
        if vars[724]:
            stage.say('&14\n곽가 선생은 아직 출전하실 수 없습니까?\n&1\n음, 생각보다 병세가 심하네.\n한동안 요양하지 않으면 낫기 어려울 듯하네.')
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
