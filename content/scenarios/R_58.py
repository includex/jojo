# Source Generated with Decompyle++
# File: R_58.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    stage.clsUnit()
    stage.setMenuVisible(False)
    return False


def scene1():
    if vars[645]:
        stage.bgSound(13)
        stage.loadBg(2, 16)
        stage.showUnit(76, 53, 82, 0, 9)
        stage.draw()
        stage.setEventName('새로운 시대를 맞이하다')
        stage.setStageName('건업  의사청')
        stage.say('&76\n아버님, 형님…….\n중모의 무능을 용서하십시오.\n끝내 조조에게 패하고 말았습니다.\n&76\n……하지만 후회는 없습니다.\n이제 평온한 마음으로 두 분을 따르고자 합니다.\n&76\n앞날은 조조에게 맡기겠습니다…….\n으, 으윽……')
        stage.effectSound(38, 1)
        stage.unit(76).setAction(10)
        stage.say('&76\n부탁하겠네…… 조조…….')
        stage.delay(10)
        stage.showUnit(0, 52, 5, 2)
        stage.unit(0).move(52, 20, 2)
        stage.unit(0).setAction(0)
        stage.say('&0\n손권!')
        stage.unit(0).move(52, 60, 2)
        stage.unit(0).setAction(0)
        stage.say('&0\n……손권.')
        stage.showUnit(1, 48, 5, 2)
        stage.unit(1).move(48, 52, 2)
        stage.unit(1).setAction(0)
        stage.say('&1\n맹덕!\n손권 그놈이 어찌하여…….')
        stage.showUnit(20, 55, 5, 2)
        stage.unit(20).move(55, 52, 2)
        stage.unit(20).setAction(0)
        stage.say('&20\n주군! 아……!?')
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n문약, 손권은 손견과 손책이 함께 묻힌 곳에 안장하게.\n……장례는 성대히 치러 주게.')
        stage.unit(20).setAction(6)
        stage.say('&20\n예, 잘 알겠습니다.')
        stage.unit(20).setAction(0)
        stage.say('&0\n원양, 즉시 전군에 약탈을 금한다고 전하게.\n어기는 자는 곧바로 군법에 따라 참수하라!\n&1\n알겠네.')
        stage.unit(1).setAction(3)
        stage.say('&1\n맹덕, 공명이 달아났네.\n어디를 찾아도 없군.')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n공명은 내버려 두게.\n우리에게는 아직 할 일이 많네.')
        stage.unit(1).setAction(3)
        stage.say('&1\n내버려 두라고……?\n이봐, 그래도 괜찮겠는가!?\n&0\n상관없네.\n&1\n……알겠네!')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(17)
        stage.say('&0\n모든 정비가 끝나는 대로\n곧장 허도로 개선한다.\n어서 준비하도록 하라.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n좋다!')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n예.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(20),
                55,
                5,
                0],
            [
                stage.unit(1),
                48,
                5,
                0]])
        stage.unit(1).hide()
        stage.unit(20).hide()
        stage.delay(8)
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n……손권.')
        stage.delay(5)
        stage.bgSound(19)
        stage.clsUnit()
        stage.loadBg(2, 73)
        stage.showUnit(35, 49, 73, 0, 11)
        stage.draw()
        stage.setStageName('장강 가  거리')
        stage.say('&35\n유비 주군, 기나긴 난세가 끝났습니다.\n마침내 평화로운 날들이 돌아왔습니다.\n이것이야말로 주군께서 바라시던 일이겠지요……')
        stage.showUnit(19, 90, 73, 3)
        stage.unit(19).move(57, 73, 3)
        stage.unit(19).setAction(0)
        stage.say('&19\n공명 선생.')
        stage.unit(35).setAction(11, 1)
        stage.say('&35\n사마의 선생이셨군요.\n저를 뒤따라 여기까지 오셨습니까?')
        stage.unit(19).setAction(6)
        stage.say('&19\n그렇습니다만, 그대를 잡으러 온 것은 아닙니다.\n저희 주군께서도 그럴 필요는 없다 하셨습니다.')
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(0)
        stage.say('&35\n조 승상께서는 저를 붙잡을 뜻이 없으시다고요……?\n……그런 것이었군요.\n그렇다면 선생께서는 무슨 일로 저를 찾으셨습니까?')
        stage.unit(19).setAction(6)
        stage.say('&19\n한 가지 청이 있소.\n이제 우리는\n새로운 나라를 열어 갈 것이오…….\n&19\n공명 선생.\n그대도 함께 우리 주군을 위해 일해 주지 않겠소?')
        stage.unit(35).setAction(6)
        stage.say('&35\n저더러 가라고요……?\n&19\n그렇소.\n새 나라를 세우려면 그대의 힘이 필요하오.')
        stage.unit(19).setAction(9)
        stage.say('&19\n이 중달, 머리를 숙여 간청합니다.\n공명 선생께서는 부디 허락해 주십시오.')
        stage.unit(35).setAction(0)
        stage.delay(10)
        stage.unit(35).setAction(0, 0)
        stage.say('&35\n……사마 선생.\n그 말씀에 깊이 감동했습니다만\n받아들일 수 없음을 용서해 주십시오.\n&19\n공명 선생……')
        stage.delay(5)
        stage.say('&35\n사마 선생.\n승상과 제가 추구하는 이상은 같으나\n걸어가는 길은 다릅니다.')
        stage.delay(5)
        stage.unit(35).setAction(11)
        stage.say('&35\n자신의 길은 스스로 정하는 것입니다…….\n앞으로도 승상께서는 자신의 길을 걸으시겠지요.')
        stage.unit(19).setAction(0)
        stage.say('&19\n…………\n&35\n하지만 저는 승상과 같은 길을 걸을 수 없습니다.')
        stage.unit(35).setAction(11, 1)
        stage.delay(10)
        stage.unit(35).setAction(0)
        stage.delay(5)
        stage.say('&35\n조 승상께서도 그 점은 아실 것입니다.')
        stage.delay(8)
        stage.unit(35).setAction(0, 0)
        stage.say('&35\n저는 융중의 초려로 돌아가\n예전의 삶을 되찾고\n조 승상께서 여시는 새 시대를 축원하겠습니다.')
        stage.unit(19).setAction(6)
        stage.say('&19\n공명 선생……')
        stage.unit(35).setAction(0, 1)
        stage.delay(3)
        stage.unit(35).setAction(6)
        stage.say('&35\n사마 선생.\n다시 뵐 인연은 없을 듯하니\n부디 몸조심하십시오.')
        stage.unit(35).setAction(0)
        stage.delay(5)
        stage.unit(35).move(10, 73, 3)
        stage.unit(35).hide()
        stage.bgSound(16)
        stage.clsUnit()
        stage.loadBg(1, 0)
        stage.draw()
        stage.showHead(0, 501, 208)
        stage.bottomTxt('조조는 촉과 오를 평정하고,', False, False, False)
        stage.delay(5)
        stage.head(0).move(330, 105)
        stage.delay(5)
        stage.bottomTxt('허도로 개선했다.', True, False, True)
        stage.bgSound(-1)
        stage.loadBg(2, 15)
        stage.showUnits([
            [
                149,
                27,
                55,
                1],
            [
                192,
                46,
                43,
                2],
            [
                193,
                52,
                43,
                2],
            [
                194,
                58,
                43,
                2],
            [
                195,
                46,
                67],
            [
                196,
                52,
                67],
            [
                197,
                58,
                67]])
        stage.draw()
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.setStageName('허도  왕궁')
        stage.delay(3)
        stage.unit(192).setAction(6)
        stage.say('&192\n폐하, 승상께서 개선하시어\n승전보를 아뢰러 오셨사옵니다.\n&149\n음.')
        stage.unit(192).setAction(0)
        stage.showUnit(0, 95, 55, 3)
        stage.say('&0\n폐하를 뵈옵니다.')
        stage.unit(0).move(89, 55, 3)
        stage.showUnits([
            [
                1,
                95,
                51,
                3],
            [
                20,
                95,
                59,
                3]])
        stage.unitsMove([
            [
                stage.unit(0),
                83,
                55,
                3],
            [
                stage.unit(1),
                89,
                51,
                3],
            [
                stage.unit(20),
                89,
                59,
                3]])
        stage.showUnits([
            [
                2,
                95,
                51,
                3],
            [
                8,
                95,
                59,
                3]])
        stage.unitsMove([
            [
                stage.unit(0),
                77,
                55,
                3],
            [
                stage.unit(1),
                83,
                51,
                3],
            [
                stage.unit(20),
                83,
                59,
                3],
            [
                stage.unit(2),
                89,
                51,
                3],
            [
                stage.unit(8),
                89,
                59,
                3]])
        stage.showUnits([
            [
                4,
                95,
                51,
                3],
            [
                19,
                95,
                59,
                3]])
        stage.unitsMove([
            [
                stage.unit(0),
                46,
                55,
                3],
            [
                stage.unit(1),
                52,
                51,
                3],
            [
                stage.unit(20),
                52,
                59,
                3],
            [
                stage.unit(2),
                58,
                51,
                3],
            [
                stage.unit(8),
                58,
                59,
                3],
            [
                stage.unit(4),
                64,
                51,
                3],
            [
                stage.unit(19),
                64,
                59,
                3]])
        stage.unit(0).setAction(0)
        stage.unit(1).setAction(0)
        stage.unit(20).setAction(0)
        stage.unit(2).setAction(0)
        stage.unit(8).setAction(0)
        stage.unit(4).setAction(0)
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n폐하,\n신 조조, 촉과 오를 평정하고\n군사를 돌려 허도로 돌아왔사옵니다.')
        stage.unit(1).setAction(6)
        stage.unit(20).setAction(6)
        stage.unit(2).setAction(6)
        stage.unit(8).setAction(6)
        stage.unit(4).setAction(6)
        stage.unit(19).setAction(6)
        stage.delay(5)
        stage.unit(0).setAction(0)
        stage.unit(1).setAction(0)
        stage.unit(20).setAction(0)
        stage.unit(2).setAction(0)
        stage.unit(8).setAction(0)
        stage.unit(4).setAction(0)
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.say('&149\n음, 경이 수고했소.\n궁 안에서도 백성들의 환호성이 들리더구려.')
        stage.unit(0).setAction(6)
        stage.say('&0\n예.')
        stage.unit(0).setAction(0)
        stage.say('&149\n경이 촉과 오를 정벌하러 떠난 뒤로\n짐은 밤낮으로 한 가지 일을 생각했소.\n&149\n이제 개선한 경의 늠름한 모습과\n백성들의 기쁜 얼굴과 웃음소리를 보고 들으니\n마침내 마음이 정해졌소.')
        stage.unit(0).setAction(6)
        stage.say('&0\n예.')
        stage.unit(0).setAction(0)
        stage.say('&149\n짐은…… 조 경에게 황위를 선양하고 싶네.')
        stage.unit(20).setAction(6)
        stage.say('&20\n(뭐, 뭐라고……!)')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('&19\n(폐하께서 그런 생각을 하실 줄이야……)')
        stage.unit(19).setAction(0)
        stage.say('&0\n…………')
        stage.unit(192).setAction(6)
        stage.say('&192\n폐, 폐하!\n어찌 그런 말씀을 하십니까!?')
        stage.unit(192).setAction(0)
        stage.delay(3)
        stage.unit(193).setAction(6)
        stage.say('&193\n이제 천하가 통일되려는 마당에\n어찌 폐하께서는……')
        stage.unit(193).setAction(0)
        stage.say('&149\n경들은 조용히 하시오.\n천하를 평정한 이가 누구요?\n&149\n모두 경의 공이 아니오?')
        stage.unit(192).setAction(6)
        stage.say('&192\n하, 하지만……\n신하로서 마땅히 할 도리입니다.')
        stage.unit(192).setAction(0)
        stage.say('&149\n짐의 선조 고조 유방과 광무제 유수께서는\n인의를 겸비하고 세상을 덮을 공을 세우셨기에\n난세를 평정하고 황제에 오르셨소.\n&149\n천자의 자리가 어찌 유씨만의 것이겠소.\n무공과 인덕을 갖춘 이가 이어야 마땅하오.')
        stage.unit(193).setAction(6)
        stage.say('&193\n폐, 폐하……')
        stage.unit(193).setAction(0)
        stage.say('&149\n조 경, 짐의 뒤를 이어 황위에 오르겠는가?')
        if model.ambition() < 85:
            stage.effectSound(200, 255)
            stage.bgSound(0)
            stage.delay(10)
            stage.unit(0).setAction(0)
            stage.say('&0\n황공하오나 폐하의 뜻을 받들 수 없사옵니다.\n&149\n어, 어찌하여 그러는가?')
            stage.unit(0).setAction(6)
            stage.say('&0\n……신이 오늘 폐하를 뵙는 것은\n난세를 평정했다는 승전보를 아뢰기 위해서만은 아니옵니다.\n&0\n국정을 다스릴 전권을 내려 주십사\n청하러 왔사옵니다.')
            stage.unit(0).setAction(0)
            stage.say('&149\n바로 짐이 바라던 바가 아니오?\n조 경은 짐의 선양을 받아 천하를 다스리시오.')
            stage.unit(0).setAction(3)
            stage.say('&0\n폐하,\n천하를 다스리는 천자는 넓은 인덕으로 만백성을 사랑해야 합니다.')
            stage.unit(0).setAction(0)
            stage.say('&0\n하지만 어진 마음만으로 천하를 다스린다면\n좋은 정치를 펼칠 수 없습니다.\n&149\n………')
            stage.unit(0).setAction(6)
            stage.say('&0\n그렇기에 이 조조는 난세를 평정하고자\n줄곧 폐하의 위엄을 빌려 왔습니다.')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(20).setAction(6)
            stage.say('&20\n주, 주군! 어찌 그런 말씀을 하십니까!?\n&149\n괜찮다.\n짐은 진작 이 사실을 알고 있었기에\n이 난세에서 스스로를 지켜 왔네.')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(6)
            stage.say('&0\n폐하, 앞으로 다가올 새로운 시대에도\n천자의 존엄으로 만백성에게 자애를 베풀어 주십시오!')
            stage.unit(0).setAction(0)
            stage.say('&149\n……경은 자신의 인덕이 부족하다 여기는가?\n&149\n그럼 앞으로도 정사를 펼치며\n백성에게 고통과 두려움을 안길 생각인가?')
            stage.unit(0).setAction(6)
            stage.say('&0\n훗날 정말 그럴 필요가 생길지도 모르지.\n그때가 오면 이 조조, 망설임 없이 결단하겠다.')
            stage.unit(0).setAction(0)
            stage.say('&149\n허나 그러면 경은 만백성에게서 멀어지고\n역사가들의 붓끝에 천년의 악명을 남길 것이다.')
            stage.unit(0).setAction(3)
            stage.say('&0\n폐하, 이 조맹덕은 오직 당대를 살아갈 뿐이옵니다.\n후세의 악명이 신과 무슨 상관이 있겠사옵니까?')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(149).setAction(0, 2)
            stage.say('&149\n(악명도 아랑곳없이 홀로 패도를 걷다니.\n참으로…… 얼마나 굳센 사람인가.)\n&149\n(앞으로 천하를 다스리기 위해\n조조는 틀림없이 나를 이용하겠지.\n허나 진심으로 백성을 위한다는 것은 알겠다.)')
            stage.unit(149).setAction(0, 1)
            stage.say('&149\n……좋다.\n&149\n조조, 짐은 천자의 존귀함으로 백성을 어루만질 터이니\n그대는 신하의 자유로운 몸으로 만사를 다스리도록 하라.')
            stage.unit(0).setAction(6)
            stage.say('&0\n예.')
            stage.unit(0).setAction(0)
            stage.delay(5)
            stage.unit(0).setAction(0, 1)
            stage.delay(3)
            stage.say('&0\n원양, 문약, 모두 들었겠지?')
            stage.unit(0).setAction(3)
            stage.say('&0\n이제 폐하께서 내게 정사의 대권을 맡기셨으니\n앞으로 더욱 바빠지겠구나.')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(1).setAction(3)
            stage.say('&1\n예.')
            stage.unit(1).setAction(0)
            stage.delay(3)
            stage.unit(20).setAction(6)
            stage.say('&20\n예.')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(17)
            stage.say('&0\n좋다. 함께 새로운 시대를 열어 가세!')
            stage.bgSound(16)
            stage.clsUnit()
            stage.loadBg(1, 0)
            stage.draw()
            stage.bottomTxt('난세가 막을 내리면서', False, False, False)
            stage.delay(5)
            stage.showHead(0, 284, 73)
            stage.showHead(1, 188, 153)
            stage.showHead(2, 252, 153)
            stage.showHead(20, 316, 153)
            stage.showHead(24, 380, 153)
            stage.delay(5)
            stage.bottomTxt('새로운 시대가 열렸다.', True, False, False)
            stage.delay(15)
            stage.bottomTxt('조조는 태평성세의 기틀을 다지고,', True, True, False)
            stage.delay(5)
            stage.head(0).hide()
            stage.delay(5)
            stage.bottomTxt('얼마 뒤 세상을 떠났다.', True, False, False)
            stage.delay(10)
            stage.bottomTxt('조조를 따라 각지를 누빈 부하들은', True, True, False)
            stage.delay(5)
            stage.head(1).hide()
            stage.head(2).hide()
            stage.head(20).hide()
            stage.head(24).hide()
            stage.delay(5)
            stage.bottomTxt('대부분 세상을 떠났다.', True, False, True)
            stage.bottomTxt('시간이 흘러……', False, False, False)
            stage.delay(10)
            stage.showHead(8, 320, 101)
            stage.delay(5)
            stage.bottomTxt('조조의 적자 조비는', True, True, False)
            stage.delay(5)
            stage.showHead(149, 256, 101)
            stage.delay(5)
            stage.bottomTxt('끝내 헌제에게 선양을 강요하여,', True, False, False)
            stage.delay(5)
            stage.head(149).hide()
            stage.delay(5)
            stage.head(8).move(288, 101)
            stage.head(8).hide()
            stage.delay(10)
            model.setFace(32)
            stage.showHead(8, 288, 101)
            stage.delay(5)
            stage.bottomTxt('위의 새 황제로 자리 잡았다.', True, False, False)
            stage.delay(10)
            stage.bottomTxt('하늘의 부름을 받은 조조는 과연', True, True, False)
            stage.delay(5)
            stage.bottomTxt('아들의 행동을 어찌 생각할지……', True, False, True)
            stage.bgSound(-1)
            stage.clsUnit()
            stage.ending(1)
        if model.ambition() >= 85:
            stage.effectSound(200, 255)
            stage.bgSound(0)
            stage.delay(10)
            stage.unit(0).setAction(0)
            stage.say('&0\n……이 조조가 받들겠습니다.\n&149\n오, 경이 받아 주겠다는 것인가?\n참으로 잘되었구나, 참으로 잘되었어.')
            stage.unit(149).setAction(0, 2)
            stage.say('&149\n짐은 이제 평범한 백성으로 돌아가\n경이 세운 새 나라에서 살겠소.')
            stage.unit(192).setAction(6)
            stage.say('&192\n폐, 폐하……\n어찌하여 폐하께서……')
            stage.unit(192).setAction(0)
            stage.delay(3)
            stage.unit(193).setAction(6)
            stage.say('&193\n사백 년 한실이 이대로 끝나는 것인가……?\n흑흑흑……')
            stage.unit(149).setAction(0, 1)
            stage.say('&149\n탄식하지 마시오.\n백성들에게 한나라는\n이미 잊힌 과거일 뿐이오.')
            stage.unit(193).setAction(0)
            stage.say('&149\n천하의 만백성이 새로운 살길을 바라고 있다.\n경, 부디 힘써 주게.')
            stage.unit(0).setAction(6)
            stage.say('&0\n예.')
            stage.unit(0).setAction(0)
            stage.delay(5)
            stage.unit(1).setAction(4)
            stage.say('&1\n흑…맹덕, 자네가 마침내 황제가 되었군.\n기뻐서 눈물이 날 지경이네.')
            stage.unit(20).setAction(0, 0)
            stage.delay(3)
            stage.unit(20).setAction(6)
            stage.say('&20\n하후 장군,\n앞으로는 주군을 그렇게 부르시면 안 됩니다.')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(1).setAction(0)
            stage.delay(3)
            stage.unit(1).setAction(0, 2)
            stage.say('&1\n오!\n그래, 자네 말이 맞군.\n이제 폐하라 불러야지.')
            stage.unit(0).setAction(0, 1)
            stage.say('&0\n상관없네.\n아까처럼 맹덕이라 불러 주게.')
            stage.unit(1).setAction(0, 3)
            stage.unit(20).setAction(0, 3)
            stage.say('&149\n경, 어찌 그리할 수 있겠소?\n짐이 곧 선양 의식을 준비하겠소.\n앞으로 천자의 몸이 되면 그대는……')
            stage.unit(0).setAction(0, 3)
            stage.delay(3)
            stage.unit(0).setAction(6)
            stage.say('&0\n폐하,\n이 조조, 폐하의 뜻에 따라 제위에 오르겠습니다.')
            stage.unit(0).setAction(0)
            stage.say('&0\n허나 나는 천자가 될 생각은 없소.\n&149\n황제의 자리에 오르되 천자는 되지 않겠다고……?\n그게 무슨 뜻인가?')
            stage.unit(0).setAction(0, 2)
            stage.say('&0\n나는 한 인간 조맹덕으로서 제위에 오르겠다.\n그러니 황제라 해도 하늘의 아들은 아니다.')
            stage.unit(20).setAction(6)
            stage.say('&20\n문약, 하지만…….')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(0, 1)
            stage.say('&0\n순욱, 사람을 다스리는 자는 사람이어야 하네.')
            stage.unit(20).setAction(6)
            stage.say('&20\n주군……')
            stage.unit(20).setAction(0)
            stage.say('&149\n사람이어야 한다고……?\n조조, 앞으로 황제를 없앨 생각인가?')
            stage.unit(0).setAction(0, 3)
            stage.delay(3)
            stage.unit(0).setAction(6)
            stage.say('&0\n앞으로 다가올 새 시대에\n황제가 필요 없다면……\n나 역시 반대하지 않겠네.')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(192).setAction(6)
            stage.say('&192\n뭐라고요!?')
            stage.unit(192).setAction(0)
            stage.delay(3)
            stage.unit(193).setAction(6)
            stage.say('&193\n폐, 폐하!\n어찌 저런 자에게 황위를 물려주시려 하옵니까……!?')
            stage.say('&149\n정숙하라.\n&193\n폐하……')
            stage.unit(193).setAction(0)
            stage.say('&149\n(황제라는 자리는 그에게 이름뿐이었군.\n과연 조맹덕은 범상한 인물이 아니로다……)\n&149\n경, 앞으로 모든 일을 그대에게 맡기겠소!')
            stage.unit(0).setAction(6)
            stage.say('&0\n예, 성명을 받들겠나이다!')
            stage.unit(0).setAction(0)
            stage.delay(5)
            stage.unit(0).setAction(0, 1)
            stage.delay(3)
            stage.unit(0).setAction(3)
            stage.say('&0\n원양, 문약, 모두 들었겠지?\n앞으로는 더욱 바빠지겠군.')
            stage.unit(0).setAction(0)
            stage.delay(3)
            stage.unit(1).setAction(3)
            stage.say('&1\n예.')
            stage.unit(1).setAction(0)
            stage.delay(3)
            stage.unit(20).setAction(6)
            stage.say('&20\n예.')
            stage.unit(20).setAction(0)
            stage.delay(3)
            stage.unit(0).setAction(17)
            stage.say('&0\n좋다. 함께 새로운 시대를 열어 가세!')
            stage.clsUnit()
            stage.loadBg(1, 0)
            stage.draw()
            stage.showHead(127, 195, 91)
            stage.showHead(101, 407, 9)
            stage.showHead(119, 471, 121)
            stage.showHead(32, 30, 170)
            stage.showHead(35, 94, 190)
            stage.showHead(76, 536, 215)
            stage.delay(5)
            stage.bottomTxt('난세를 누비던 영웅들이', False, False, False)
            stage.delay(8)
            stage.head(127).hide()
            stage.head(119).hide()
            stage.head(101).hide()
            stage.head(32).hide()
            stage.head(35).hide()
            stage.head(76).hide()
            stage.delay(5)
            stage.bottomTxt('하나둘 역사의 무대에서 물러날 때', True, True, False)
            stage.delay(5)
            stage.bottomTxt('난세도 마침내 막을 내렸다.', True, False, True)
            stage.delay(5)
            stage.showHead(0, 288, 101)
            stage.delay(5)
            stage.bottomTxt('새 황제 조맹덕의 통치 아래,', False, False, False)
            stage.delay(5)
            stage.showHead(23, 195, 91)
            stage.showHead(8, 407, 9)
            stage.showHead(20, 471, 121)
            stage.showHead(19, 30, 170)
            stage.showHead(1, 294, 229)
            stage.showHead(2, 536, 215)
            stage.delay(5)
            stage.bottomTxt('새로운 시대가 열렸다.', True, False, False)
            stage.delay(5)
            stage.bottomTxt('사람이 사람을 다스리는 시대……', True, True, True)
            stage.bgSound(-1)
            stage.clsUnit()
            stage.ending(0)
    if not vars[645]:
        stage.loadBg(2, 24)
        stage.showUnit(35, 54, 43, 2)
        stage.draw()
        stage.setEventName('새로운 시대를 향하여')
        stage.setStageName('오장원 지하  제단')
        stage.delay(3)
        stage.unit(35).setAction(2)
        stage.say('&35\n으으…… 늙은 도적 조조!\n감히 나를 막다른 곳까지 몰아붙이다니!')
        stage.unit(35).setAction(0)
        stage.say('&35\n나, 나는 이대로 그를 놓아주지 않겠다……\n어둠 속에 숨어 상처가 나을 때를 기다린 뒤\n반드시 죽이고 말겠다……')
        stage.showUnit(0, 54, 95)
        stage.unit(0).move(54, 86, 0)
        stage.unit(0).setAction(0)
        stage.say('&0\n공명!!')
        stage.unit(35).setAction(0, 2)
        stage.unit(0).move(54, 53, 0)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(35).setAction(11)
        stage.say('&35\n조조……끝내 따라잡혔군.\n으으으……')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n공명, 오늘이 네 제삿날이다!')
        stage.unit(35).setAction(2)
        stage.say('&35\n우쭐대지 마라, 조조!\n……너희 같은 범인들이 나를 죽일 수 있겠느냐?')
        stage.unit(35).setAction(0)
        stage.say('&0\n으윽……네놈……!')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(35).setAction(17)
        stage.say('&35\n내게 인간이란\n가축에 불과하다.\n크, 하하하!')
        stage.unit(35).setAction(0)
        stage.say('&35\n……으윽!')
        stage.delay(5)
        stage.say('&35\n으, 으아!\n온몸이 뜨거워진다!\n왜, 왜 온몸이 뜨거운 것이냐!?')
        model.setFace(1)
        stage.say('&0\n！？')
        stage.unit(35).setAction(8)
        stage.say('&35\n서, 설마 공명의 혼인가!?\n제 몸을 무너뜨려 나를 죽일 셈인가……?\n&0\n공명!?\n&35\n공, 공명! 가증스러운 놈!\n줄곧 기회를 기다렸구나……\n내가 다쳐 쇠약해질 때까지 기다렸다가……\n&35\n나, 나는 위대한 마왕이다.\n어, 어찌 인간에게 죽을 수……\n으아아……!!')
        stage.unit(35).setAction(12)
        stage.say('&35\n으윽, 뜨겁다! 뜨거워!\n&35\n아, 아아……!!')
        stage.effectSound(38, 1)
        stage.unit(35).setAction(10, 2)
        stage.delay(3)
        stage.unit(0).setAction(3)
        stage.say('&0\n공명!')
        stage.unit(0).setAction(0)
        stage.bgSound(13)
        stage.delay(15)
        model.setFace(255)
        model.setFace(0)
        stage.say('&35\n조조인가……?\n&0\n음, 나일세.\n공명, 자네……\n&35\n조조……내, 내가 마왕을 죽였네.\n그대 덕분에 죽일 수 있었어……\n으윽! 으아……!')
        stage.unit(0).setAction(6)
        model.setFace(1)
        stage.say('&0\n공명, 정신 차리게!')
        stage.unit(0).setAction(0)
        stage.say('&35\n이…… 이제 돌이킬 수 없습니다.\n제 손으로 온몸의 맥을 끊고 장부를 부쉈으니…….\n&0\n공명…… 어찌하여…….')
        stage.delay(8)
        model.setFace(0)
        stage.say('&35\n내가……\n내가 우리 주군을 죽였어……\n&0\n……공명.')
        stage.delay(5)
        stage.say('&35\n내게 악마를 막을 힘이 없었던 탓이다……!')
        stage.delay(5)
        stage.say('&35\n주군………')
        stage.delay(3)
        stage.say('&35\n……………\n&0\n공명………')
        stage.showUnit(3, 54, 95)
        stage.unit(3).move(54, 86, 0)
        stage.say('&3\n승상.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n운장.')
        stage.unit(3).move(54, 63, 0)
        stage.say('&0\n……방금 한 말을 모두 들었느냐?')
        stage.unit(3).setAction(6)
        stage.say('&3\n그렇습니다.\n모두의 원수를 갚고자\n공명을 베러 왔습니다만……')
        stage.unit(3).setAction(0)
        stage.say('&0\n그런가……')
        stage.unit(3).setAction(6)
        stage.say('&3\n공명 군사는……\n죽는 순간까지도 형님을 생각하고 있었군.')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n음……\n운장, 공명의 장례는 그대에게 맡기겠네.')
        stage.unit(3).setAction(6)
        stage.say('&3\n감사합니다, 승상.\n제가 직접 후히 장사 지내어…….\n군사를 형님 곁에서 잠들게 하겠습니다.')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n옳은 말일세.\n그리하도록 하게.\n현덕도 그를 기다리고 있겠지.\n&3\n……군사.')
        stage.delay(10)
        stage.bgSound(-1)
        stage.clsUnit()
        stage.loadBg(2, 32)
        stage.showUnit(0, 63, 33, 3)
        stage.draw()
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.info('공명이 죽은 지 한 달 뒤……', 3)
        stage.setStageName('오장원  산 위')
        stage.delay(10)
        stage.showUnit(151, 90, 41, 3)
        stage.unit(151).move(73, 41, 3)
        stage.say('&151\n승상.')
        stage.unit(151).move(63, 41, 3)
        stage.unit(151).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n원직이었군.')
        stage.unit(151).setAction(0, 0)
        stage.delay(3)
        stage.unit(151).setAction(6)
        stage.say('&151\n허도의 승상부로 찾아갔더니\n순욱 선생께서 승상이 이곳에 계시다고 알려 주셨습니다.')
        stage.unit(151).setAction(0)
        stage.say('&0\n그렇군.\n이렇게 나를 찾아온 데에는\n무슨 중대한 일이라도 있는가?')
        stage.unit(151).setAction(6)
        stage.say('&151\n그렇습니다…….\n승상, 지하 제단은 허무셨습니까?')
        stage.unit(151).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 3)
        stage.say('&0\n음?\n아, 나도 일찍 제단을 허물려 했지만\n공명의 장례 등을 치르느라 늦어졌네.\n&151\n직접 제단을 허무는 일을 감독하실 생각이십니까?\n&0\n그렇네.\n&151\n……………')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n원직, 할 말이 있어 나를 찾은 것 아닌가?')
        stage.unit(151).setAction(6)
        stage.say('&151\n예, 승상께서는 이 편지를 살펴봐 주십시오.')
        stage.unit(151).setAction(0)
        stage.delay(3)
        stage.unit(151).move(63, 37)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.delay(3)
        stage.unit(151).move(63, 41, 0)
        stage.delay(3)
        stage.say('&0\n이것은……공명의 편지인가?')
        stage.unit(151).setAction(6)
        stage.say('&151\n예.\n편지는 강하성에서 공명이 쓰던 방에 남아 있었습니다.\n&151\n아마 적벽대전 직후에 쓴 것으로 보입니다.')
        stage.unit(151).setAction(0)
        stage.say('&0\n음.')
        stage.effectSound(203, 255)
        stage.bgSound(19)
        stage.delay(10)
        stage.info('지금 이 서찰을 펼치는 이는\n서서가 아니라면 방통이겠지요.\n누가 이 서찰을 얻었든\n그때는 제가 이미 세상에 없을지도 모릅니다.')
        stage.info('죽음이 두려운 것은 아니오.\n내가 살아 있어 큰 재앙을 부를까 두려울 뿐이오.')
        stage.info('그대들도 마왕의 이야기는 들었을 것이오.\n삼천 년 전, 위대한 선조 황제께서\n천하를 다스리실 때 반란을 일으킨 마왕 말이오.')
        stage.info('삼천 년 동안 잠들었던\n마왕이 이제 깨어나\n난세를 틈타 천하를 노예로 삼으려 합니다.')
        stage.info('마왕은 이미 육신을 잃어\n재능 있는 자에게 들어야만 움직일 수 있었다.\n결국 우리 주군 유비를 그릇으로 택했다.')
        stage.info('적벽대전에서 기이하게 불어와\n우리 군에 승리를 안긴 동남풍도\n마왕이 자신의 분신을 돕기 위해 일으킨 것이오.')
        stage.info('적벽대전 뒤 마왕이 마침내 움직이기 시작했다.\n밤하늘에 요사스러운 구름이 일고 혜성이 나타났다.\n하지만 유비 주군께 들게 둘 수는 없었다!')
        stage.info('주군께 후세의 오명이 남지 않도록\n마왕을 제 몸에 받아들일 수밖에 없었습니다.\n그 뒤 마왕의 야심과 맞서 싸웠습니다!')
        stage.info('앞으로 마왕의 야심 때문에\n촉의 수많은 장병이 희생될지도 모르오.\n나 역시 비참한 끝을 맞을지도 모르지.\n허나 그대에게 진실을 알리고 싶었소.')
        stage.info('내가 마왕에게 패한다면\n그대 손으로 나를 죽여 주시오.\n그래야 마왕을 없앨 수 있소!\n제갈공명@강하에서 쓰다')
        stage.delay(5)
        stage.unit(0).setAction(0)
        stage.say('&0\n공명이 현덕을 구하려고\n제 몸에 마왕을 받아들였다는 말인가?')
        stage.unit(0).setAction(0, 3)
        stage.say('&0\n이번 전쟁에서 많은 사람이 죽었다.\n가장 괴로운 사람은 아마 공명이겠지.\n&151\n그렇습니다.')
        stage.delay(15)
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n원직, 줄곧 묻고 싶었던 것이 있소.')
        stage.unit(151).setAction(6)
        stage.say('&151\n무슨 일이십니까?')
        stage.unit(151).setAction(0)
        stage.say('&0\n허도에서 기도를 올리기로 했을 때\n그대가 한 가지 조건을 내걸었었지.\n&0\n그 조건이 무엇이었는가……?')
        stage.unit(151).setAction(0, 3)
        stage.say('&151\n승상께서도 마왕이 죽은 자를 조종할 수 있음을 아실 것입니다.\n&0\n……음. 우리 군이 지하에서 싸울 때\n그 때문에 무척 애를 먹었지.\n&151\n마왕이 진정 부활하면\n망자의 혼에 다시 생명을 줄 수도 있다고 합니다.\n&0\n……원직, 역시 자네는……')
        stage.delay(8)
        stage.say('&151\n관우 장군, 초선, 그리고 공명……\n모두 사랑하는 이를 잃었습니다.\n이 원직도 마찬가지입니다……')
        stage.delay(8)
        stage.unit(151).setAction(0, 0)
        stage.say('&151\n하지만 이제는 마음을 정했습니다.')
        stage.unit(151).setAction(6)
        stage.say('&151\n저는 지금 촉으로 가서\n공명의 편지 내용을 촉의 장수들에게 알리겠습니다.\n그럼 먼저 물러가겠습니다.')
        stage.unit(151).setAction(0)
        stage.delay(3)
        stage.unit(151).move(95, 41, 1)
        stage.unit(151).hide()
        stage.delay(5)
        stage.say('&0\n(……내 짐작이 맞았군.\n서서, 역시 죽은 어머니를……)')
        stage.bgSound(-1)
        stage.clsUnit()
        stage.loadBg(2, 33)
        stage.showUnit(3, 32, 55, 3, 6)
        stage.draw()
        stage.effectSound(200, 0)
        stage.delay(3)
        stage.setStageName('허도 교외  묘지')
        stage.say('&3\n형님, 군사…….')
        stage.delay(5)
        stage.showUnit(0, 95, 55, 3)
        stage.unit(0).move(88, 55, 3)
        stage.unit(0).setAction(0)
        stage.say('&0\n운장.')
        stage.unit(3).setAction(0, 1)
        stage.unit(0).move(40, 55, 3)
        stage.unit(0).setAction(0)
        stage.say('&0\n그대도 성묘하러 왔는가?\n&3\n그렇습니다.\n실은 형님과 군사께 작별을 고하러 왔습니다.\n&0\n작별이라고?\n……그런가?')
        stage.unit(3).setAction(6)
        stage.say('&3\n그렇소.\n곧 승상을 찾아뵈려던 참이었소.')
        stage.say('&3\n장수로서의 소임은 이미 다했소.\n&3\n승상께 큰 은혜를 입었음을 잘 알고 있으니\n제멋대로 떠나는 것을 용서해 주시오.')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n운장, 괘념치 말게.\n이제 어디로 갈 생각인가?')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('&3\n아직은 정하지 못했소.\n이제는 천하를 떠돌고 싶을 뿐이오.')
        stage.unit(3).setAction(0, 3)
        stage.say('&3\n형님과 셋째 아우, 군사께서 좇던 꿈을 위해,\n백성이 편히 살아갈 태평성세를 열기 위해……')
        stage.unit(3).setAction(0, 1)
        stage.delay(3)
        stage.say('&3\n무엇을 해야 하고 무엇을 할 수 있는지 깨달을 때까지 말입니다.\n&0\n자네와 헤어지는 것도 두 번째로군.\n솔직히 자네를 보내고 싶지 않네만\n……부디 몸조심하게.')
        stage.unit(3).setAction(6)
        stage.say('&3\n감사하오, 승상.\n승상께서도 몸조심하시오.\n그럼 이만 물러가겠소.')
        stage.unit(3).setAction(0)
        stage.say('&0\n음.')
        stage.unit(3).move(32, 51, 0)
        stage.delay(3)
        stage.unit(3).move(95, 51, 1)
        stage.unit(0).setAction(0, 1)
        stage.unit(3).hide()
        stage.delay(10)
        stage.unit(0).setAction(0, 3)
        stage.delay(10)
        stage.unit(0).move(32, 55, 3)
        stage.delay(10)
        stage.unit(0).setAction(6)
        stage.delay(10)
        stage.unit(0).setAction(0)
        stage.delay(10)
        stage.showUnits([
            [
                1,
                95,
                49,
                3],
            [
                20,
                95,
                55,
                3]])
        stage.unitsMove([
            [
                stage.unit(1),
                83,
                49,
                3],
            [
                stage.unit(20),
                83,
                55,
                3]])
        stage.say('&1\n맹덕!\n&20\n주군.')
        stage.unit(0).setAction(0, 1)
        stage.unitsMove([
            [
                stage.unit(1),
                40,
                49,
                3],
            [
                stage.unit(20),
                40,
                55,
                3]])
        stage.unit(1).setAction(0)
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n여기 있었군?\n한창 자네를 찾고 있었네.')
        stage.unit(1).setAction(0)
        stage.say('&0\n원양과 문약이로군.\n무슨 일인가?')
        stage.unit(1).setAction(3)
        stage.say('&1\n아니, 그 두 사람 이야기일세……')
        stage.unit(1).setAction(0)
        stage.say('&0\n어느 둘을 말하는가?')
        stage.showUnits([
            [
                17,
                95,
                49,
                3],
            [
                15,
                95,
                55,
                3]])
        stage.unitsMove([
            [
                stage.unit(17),
                83,
                49,
                3],
            [
                stage.unit(15),
                83,
                55,
                3]])
        stage.say('&15\n주군!\n&17\n저희에 관한 일입니다.')
        stage.unit(1).setAction(0, 1)
        stage.unit(20).setAction(0, 1)
        stage.unitsMove([
            [
                stage.unit(17),
                48,
                49,
                3],
            [
                stage.unit(15),
                48,
                55,
                3]])
        stage.unit(17).setAction(0)
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        stage.say('&0\n초선과 허저라고?\n무슨 일인가?')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('&15\n음……솔직히 말씀드리면……\n마을로 돌아가고 싶습니다……')
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(6)
        stage.say('&17\n저도 작별 인사를 드리러 왔습니다.')
        stage.unit(17).setAction(0)
        stage.say('&0\n그런가.\n그대들마저 떠나는구나…….')
        stage.unit(1).setAction(0, 3)
        stage.say('&1\n‘자네들도’라니 무슨 말인가?\n그들 말고도 작별 인사를 하러 온 이가 있었는가?\n&0\n조금 전 운장이 왔었네. 천하를 떠돌 생각이라더군.')
        stage.unit(20).setAction(0, 3)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n관우 장군께서 떠나셨군요.')
        stage.unit(20).setAction(0)
        stage.say('&0\n허저, 고향으로 돌아가고 싶다고 했느냐?')
        stage.unit(20).setAction(0, 1)
        stage.unit(1).setAction(0, 1)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('&15\n예.\n저는…역시 장수 노릇에는 어울리지 않는 듯합니다……\n&15\n이제 고향도 평온해졌으니\n돌아가 농사를 지으며 살 생각입니다.\n제 고집만 부렸습니다. 부디 용서해 주십시오, 주군.')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n사과할 것 없네.\n전원에서 살아가는 것도 좋은 일이지.')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('&15\n가, 감사합니다. 주군.')
        stage.unit(15).setAction(0)
        stage.say('&0\n초선, 그대는 어찌할 생각인가?')
        stage.unit(17).setAction(6)
        stage.say('&17\n황건교 동료들과 함께하며\n만백성을 구하려던 부모님의 뜻을 이어 가고 싶습니다.')
        stage.unit(17).setAction(0)
        stage.say('&0\n조정을 떠나겠다는 것이냐……?\n그 길은 몹시 험난할 것이다.')
        stage.unit(17).setAction(6)
        stage.say('&17\n저는 두렵지 않습니다.\n의부 왕윤 대인과 봉선께서 지켜 주시니까요.')
        stage.unit(17).setAction(0)
        stage.say('&0\n음.')
        stage.say('&0\n그동안 함께 싸워 왔으니\n그대들과 헤어지기가 참으로 아쉽소.\n하지만 뜻이 서로 다르니 억지로 붙잡지는 않겠소.')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n부디 몸조심하게.')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(6)
        stage.say('&17\n감사합니다, 승상.')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(3)
        stage.say('&15\n열심히 농사짓겠습니다.')
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(6)
        stage.say('&17\n하후 장군과 순욱 선생께서도 몸조심하세요.')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('&15\n두 분 모두 몸조심하십시오.')
        stage.unit(15).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n초선 낭자, 허저 장군.\n앞으로 행복하게 지내시기를 바랍니다.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n초선은 걱정할 것 없겠지만\n나는 오히려 허저가 걱정되는군.')
        stage.unit(15).setAction(3)
        stage.say('&15\n무, 무슨 뜻입니까?')
        stage.unit(1).setAction(0)
        stage.say('&1\n화내지 말게!\n농담일세, 농담!\n하하하.')
        stage.unit(15).setAction(0)
        stage.say('&15\n정말 너무하는군. 떠나기 전까지 사람을 놀리다니…….')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n하하하.')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(17).setAction(6)
        stage.say('&17\n그럼 소녀는 이만 물러가겠습니다.')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(15).setAction(6)
        stage.say('&15\n나도 이만 가야겠다.\n잘 있어라!')
        stage.unit(15).setAction(0)
        stage.say('&0\n음.')
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(17),
                95,
                49,
                1],
            [
                stage.unit(15),
                95,
                55,
                1]])
        stage.unit(15).hide()
        stage.unit(17).hide()
        stage.delay(15)
        stage.unit(0).setAction(0, 3)
        stage.delay(10)
        stage.unit(1).setAction(0, 3)
        stage.say('&1\n맹덕, 그리 쓸쓸한 표정은 짓지 말게.\n자네답지 않잖나!')
        stage.unit(20).setAction(0, 3)
        stage.delay(5)
        stage.unit(1).setAction(3)
        stage.say('&1\n걱정 말게! 우리는 언제나 자네와 함께할 것이네.')
        stage.unit(20).setAction(6)
        stage.say('&20\n그렇습니다, 주군.')
        stage.unit(20).setAction(0)
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n……그렇군.\n내게는 아직 새 나라를 세울 사명이 남아 있지.')
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n음, 더는 슬퍼하지 않겠네.\n우리를 기다리는 일이 있으니\n두 사람도 승상부로 돌아가게.')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n좋다!')
        stage.unit(20).setAction(6)
        stage.say('&20\n예.')
        stage.unit(20).setAction(0)
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(0),
                87,
                55,
                1],
            [
                stage.unit(1),
                95,
                49,
                1],
            [
                stage.unit(20),
                95,
                55,
                1]])
        stage.unit(1).hide()
        stage.unit(20).hide()
        stage.unit(0).move(95, 55, 1)
        stage.unit(0).hide()
        stage.delay(20)
        stage.effectSound(200, 255)
        stage.clsUnit()
        stage.ending(2)
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
