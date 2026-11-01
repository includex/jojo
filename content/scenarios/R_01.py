# Source Generated with Decompyle++
# File: R_01.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(-1)
    stage.video('전투 완료 영상을 재생하시겠습니까?', 3)
    if stage.video() == 1:
        stage.video(1)
    stage.bgSound(16)
    stage.sectionName(1, '패왕의 탄생')
    stage.loadBg(0, 16)
    stage.draw()
    stage.video('지금으로부터 약 １８００년 전,\n후한 말 중국 대륙.')
    stage.video('당시 황제 영제는 향락에 빠져 있었고 조정은 부패했다.\n굶주리는 백성들의 한실에 대한 불만은\n마침내 극에 달했다.')
    stage.video('그 무렵…….')
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(147, 320, 180)
    stage.showHead(146, 440, 130)
    stage.delay(5)
    stage.bottomTxt('태평도의 교주 장각이 이끄는 황건의 난은,', False, True, False)
    stage.delay(5)
    stage.bottomTxt('이에 중국 각지에서 폭발했다.', True, False, False)
    stage.delay(10)
    stage.showHead(0, 260, 60)
    stage.delay(5)
    stage.bottomTxt('조조가 이끄는 관군은', True, True, False)
    stage.head(0).move(320, 60)
    stage.delay(5)
    stage.head(147).hide()
    stage.head(146).hide()
    stage.delay(5)
    stage.bottomTxt('고전 끝에 반란 세력을 진압했으나', True, False, False)
    stage.delay(5)
    stage.bottomTxt('하지만 한실이 쇠락하는 흐름을 막을 힘은 이미 없었고,', True, True, True)
    stage.bottomTxt('이것이 바로 난세의 시작이었다.', False, True, True)
    stage.loadBg(0, 16)
    stage.draw()
    stage.video('황건의 난이 가라앉은 지 얼마 지나지 않아\n영제가 붕어했다. 구심점이 사라진 틈을 타\n조정에서는 격렬한 권력 다툼이 시작되었다.')
    stage.video('결국 동탁이 힘을 앞세워 조정의 혼란을 틈타\n낙양의 실권을 장악했다.')
    stage.bgSound(-1)
    return False


def scene1():
    stage.loadBg(2, 9)
    stage.showUnits([
        [
            192,
            52,
            41,
            2],
        [
            193,
            58,
            41,
            2],
        [
            131,
            64,
            41,
            2],
        [
            128,
            70,
            41,
            2],
        [
            101,
            52,
            70],
        [
            0,
            58,
            70],
        [
            133,
            64,
            70],
        [
            134,
            70,
            70]])
    stage.draw()
    stage.effectSound(205, 0)
    stage.setEventName('조조가 군사를 일으키다')
    stage.setStageName('낙양@승상부')
    stage.delay(3)
    stage.unit(101).setAction(-1, 1)
    stage.say('&101\n동탁이 갑자기 우리를 불러 모으다니 무슨 일일까?\n조조, 자네는 어찌 생각하는가!')
    stage.unit(0).setAction(-1, 3)
    stage.say('&0\n천하가 이미 제 손안에 들어왔음을\n우리에게 알리려는 것이겠지.')
    stage.unit(101).setAction(2)
    stage.say('&101\n천하라고!? 괘씸한 놈, 짐승만도 못한 자로군…….')
    stage.unit(0).setAction(-1, 0)
    stage.say('&0\n……본초, 그만하게.\n짐승 나리가 오셨군.')
    stage.unit(101).setAction(0, 0)
    stage.showUnit(127, 90, 55, 3)
    stage.unit(127).move(84, 55, 3)
    stage.showUnits([
        [
            119,
            90,
            58,
            3],
        [
            132,
            90,
            52,
            3]])
    stage.unitsMove([
        [
            stage.unit(127),
            41,
            55,
            3],
        [
            stage.unit(119),
            47,
            58,
            3],
        [
            stage.unit(132),
            47,
            52,
            3]])
    stage.unit(127).setAction(0)
    stage.unit(119).setAction(0)
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(127).move(41, 58, 2)
    stage.unit(127).move(33, 58, 3)
    stage.unit(127).move(33, 55, 1)
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(119),
            41,
            58,
            3],
        [
            stage.unit(132),
            41,
            52,
            3]])
    stage.unit(119).setAction(0)
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(132).move(41, 46, 0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(119),
            33,
            58,
            3],
        [
            stage.unit(132),
            33,
            46,
            3]])
    stage.unitsMove([
        [
            stage.unit(119),
            33,
            61,
            1],
        [
            stage.unit(132),
            33,
            47,
            1]])
    stage.unit(119).setAction(0)
    stage.unit(132).setAction(0)
    stage.delay(5)
    stage.unit(127).setAction(3)
    stage.say('&127\n하하하하, 모두 모였구나.')
    stage.unit(127).setAction(0)
    stage.say('&127\n오늘 그대들을 부른 것은 중대한 일을 알리기 위해서다.')
    stage.unit(192).setAction(6)
    stage.say('&192\n승상께서 무슨 일을 발표하시려는지요?')
    stage.unit(192).setAction(0)
    stage.say('&127\n폐하에 관한 일이오.')
    stage.unit(127).setAction(17)
    stage.say('&127\n무능한 지금의 황제를 폐하고\n진류왕을 새 황제로 세우겠다.')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(3)
    stage.say('&192\n뭐, 뭐라고!?')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('&193\n이……!')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('&101\n흥…….')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n……。')
    stage.unit(127).setAction(3)
    stage.say('&127\n왜? 이의가 있느냐?')
    stage.unit(127).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('&193\n……아, 아닙니다.')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(6)
    stage.say('&192\n모든 것은 승상의 뜻에 따르겠습니다.')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('&101\n빌어먹을!')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n……잠깐, 본초. 우선은 그의 말을 들어 보세.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 1)
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('&101\n뭐라고!?\n조조, 이럴 때가 아니오. 어서 저놈을 죽이시오!\n&0\n동탁 곁에 선 저 장수가 보이시오?\n그가 바로 여포요!')
    stage.unit(101).setAction(0)
    stage.say('&101\n여포가 누구인가?\n&0\n모르는가? 천하무쌍의 호걸일세.\n검을 뽑아 보았자\n헛되이 목숨만 잃을 뿐이야.\n그만두게. 지금은 말을 듣는 편이 좋네.')
    stage.unit(101).setAction(2)
    stage.say('&101\n빌어먹을…….')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 0)
    stage.unit(0).setAction(-1, 0)
    stage.delay(5)
    stage.unit(127).setAction(3)
    stage.say('&127\n하하하!')
    stage.unit(127).setAction(0)
    stage.say('&127\n그렇군, 그렇고말고.\n모두 찬성하겠지?')
    stage.unit(127).setAction(0, 0)
    stage.say('&127\n이유.')
    stage.unit(132).setAction(0, 2)
    stage.delay(3)
    stage.unit(132).setAction(6)
    stage.say('&132\n예.')
    stage.unit(127).setAction(3)
    stage.say('&127\n모두 동의하니\n즉시 준비에 착수하라.')
    stage.unit(127).setAction(0)
    stage.say('&132\n명을 받들겠습니다.')
    stage.unit(132).setAction(0)
    stage.delay(3)
    stage.unit(127).setAction(0, 1)
    stage.delay(3)
    stage.unit(127).setAction(3)
    stage.say('&127\n하하하, 통쾌하구나!\n오늘은 모두 실컷 마셔라!')
    stage.unit(127).setAction(0)
    stage.say('&119\n……。')
    stage.delay(3)
    stage.unit(101).setAction(0, 1)
    stage.say('&101\n조조.')
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n무슨 일인가?')
    stage.unit(101).setAction(3)
    stage.say('&101\n낙양에는 내 군사가 얼마 없으니\n동탁을 죽이기는 어려울 것이네.')
    stage.unit(101).setAction(0)
    stage.say('&101\n하북으로 돌아가 병마를 모은 뒤\n낙양성 밖에서 동탁을 공격할 생각이오.')
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n……낙양을 떠날 생각이오?\n&101\n그렇소. 조조, 그대는 어찌할 생각이오?')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n동탁이라는 자를 조금 더 지켜볼 생각이오.\n&101\n지켜본다고?\n&0\n그렇소.\n&101\n조조…무슨 생각이오?\n&0\n아직 동탁이라는 인물을 제대로 모르니\n그에게 도량이 있는지 보고 싶소.\n&101\n동탁의 도량이라고?')
    stage.unit(101).setAction(2, 1)
    stage.say('&101\n그놈은 사나운 이리일 뿐이다!')
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n큰 소리로 떠들지 마라!')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n사나운 늑대가 듣겠군. 하하하.')
    stage.unit(101).setAction(0)
    stage.say('&119\n(……저 두 사람이 원소 원본초와\n조조 조맹덕이로군…….)')
    stage.clsUnit()
    stage.effectSound(205, 255)
    return False


def scene2():
    stage.bgSound(16)
    stage.loadBg(0, 16)
    stage.draw()
    stage.info('천자의 명을 받은 대신이 된 동탁은\n권력을 독점하고 폭정을 일삼았다.\n도성 낙양에는 백성의 통곡과 탄식이 맴돌았다.')
    stage.bgSound(-1)
    stage.loadBg(2, 39)
    stage.showUnit(153, 68, 57, 0, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('낙양@왕윤의 저택')
    stage.say('&153\n동탁의 폭정은 날로 심해지는데 신료들은 그를 두려워해\n아첨만 일삼고 있으니 임금과 신하의 목숨이 모두 그의 손에 달렸구나.\n&153\n우리 집안은 대대로 한실의 녹을 먹었고\n나 또한 사도라는 중책을 맡고 있거늘\n저 간신 동탁에게 아무 힘도 쓰지 못하다니!')
    stage.unit(153).setAction(9)
    stage.say('&153\n하아…….\n&196\n왕 대인 계십니까?')
    stage.unit(153).setAction(1)
    stage.say('&153\n여기 있소.')
    stage.showUnit(196, 55, 29, 2)
    stage.say('&196\n왕 대인, 실례하겠소.')
    stage.unit(153).setAction(0)
    stage.say('&153\n아, 괜찮소.')
    stage.unit(196).move(55, 49, 2)
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(153).move(55, 57, 0)
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('&153\n태위 대인,\n용건이 있으시면 부르기만 하십시오. 이 왕윤이 찾아뵈었을 텐데요.')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('&196\n음…… 어찌 왕 대감께 수고를 끼치겠습니까?')
    stage.unit(196).setAction(0)
    stage.say('&196\n실은 오늘\n왕 대인께 소개할 사람이 있어 찾아왔소.')
    stage.unit(153).setAction(6)
    stage.say('&153\n오, 어느 분이시오?')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(0, 0)
    stage.say('&196\n조조, 들어오게.')
    stage.showUnit(0, 55, 29, 2)
    stage.say('&0\n반갑소.')
    stage.unitsMove([
        [
            stage.unit(196),
            61,
            49,
            2],
        [
            stage.unit(0),
            55,
            49,
            2]])
    stage.delay(5)
    stage.unit(196).setAction(6)
    stage.say('&196\n이쪽은 조조, 자는 맹덕이오.\n보기 드문 인재라오.')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n저는 조조, 자는 맹덕이라 합니다.\n왕윤 대인께 많은 가르침을 청합니다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('&153\n아, 장군. 너무 예를 차리십니다.')
    stage.unit(153).setAction(0)
    stage.say('&153\n예를 차리지 않으셔도 됩니다.')
    stage.unit(0).setAction(6)
    stage.say('&0\n감사합니다, 왕 대인.')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(153),
            68,
            57,
            0],
        [
            stage.unit(196),
            74,
            49,
            2],
        [
            stage.unit(0),
            68,
            49,
            2]])
    stage.unit(196).setAction(1)
    stage.unit(0).setAction(1)
    stage.unit(153).setAction(1)
    stage.delay(5)
    stage.info('잠시 뒤…….', 3)
    stage.delay(5)
    stage.say('&153\n조조, 지금 조정을 어찌 생각하시오?\n&0\n조정이라 부르기도 어려울 지경이지요.\n동탁이 모든 권력을 독차지하니\n보고 듣는 것마다 탄식뿐입니다.\n&153\n장군도 그리 생각하시는군요……\n저 역시 같은 생각입니다.\n이대로라면 천하가 큰 혼란에 빠질 것입니다…….')
    stage.unit(0).setAction(0)
    stage.say('&0\n나는 거짓으로 동탁을 섬기는 척하며\n실은 그의 동태를 살피고 있었네.')
    stage.unit(153).setAction(0)
    stage.say('&153\n오, 그렇다면…….')
    stage.unit(0).setAction(6)
    stage.say('&0\n동탁이 권력을 독점하고 있다 해도\n용맹만 믿고 날뛰는 자는 아니야.')
    stage.unit(0).setAction(0)
    stage.say('&0\n낙양 안에서는 그를 제거할 수 없고\n각지의 제후들 가운데도\n그를 토벌할 만한 힘을 지닌 자가 없네.')
    stage.unit(153).setAction(1, 0)
    stage.say('&153\n음…….\n&153\n나도 그리 생각하오.\n제후 하나하나의 힘으로는 부족하오…….')
    stage.unit(0).setAction(3)
    stage.say('&0\n그렇습니다. 제후 하나하나의 힘으로는 어렵지요.\n&0\n하지만…….')
    stage.unit(0).setAction(0)
    stage.delay(10)
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(3)
    stage.say('&153\n아, 알겠소.\n제후 하나하나의 힘은 동탁에 못 미치더라도\n여럿의 힘을 모은다면…….')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n그렇습니다.\n군웅들이 동맹을 맺을 수만 있다면!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(0, 3)
    stage.say('&196\n동맹이라고?!')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그렇소. 허나 군웅들이 동맹을 맺게 하는 것은 쉬운 일이 아니오…….')
    stage.unit(0).setAction(0, 2)
    stage.unit(196).setAction(0, 2)
    stage.delay(5)
    stage.unit(153).setAction(0, 2)
    stage.say('&153\n……다소 온당치 못할 수도 있겠으나\n천자의 이름으로 격문을 내린다면\n군웅들이 떨쳐 일어날 것입니다.')
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n조 장군, 그대가 이 동맹을 성사시켜 주게.')
    stage.unit(0).setAction(3)
    stage.say('&0\n내가?')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('&153\n그렇습니다. 장군을 처음 뵈었을 때부터\n큰일을 이루실 분이라 믿었습니다.\n무슨 일이 있어도 이 일을 맡아 주십시오.')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n하지만…동맹을 맺으려면\n어느 정도 힘이 있어야 하지요.\n&0\n소인에게는 군사도, 군사를 모을 재물도 없습니다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('&153\n우리 집안에는 칠성검이라는 보검이 있소.\n&153\n이 사명을 맡아 주겠다면\n그 보검을 드리겠소.\n팔면 자금을 마련할 수 있을 것이오.')
    stage.say('&153\n어떤가? 받아들여 주겠는가?')
    stage.unit(153).setAction(0)
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('기꺼이 맡겠다\n맡기 어렵다', -1)
    if sel == 1:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.say('&0\n……좋다.')
        stage.unit(0).setAction(6)
        stage.say('&0\n소인이 그 중임을 맡겠습니다.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('&153\n오, 허락하시는 겁니까?')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n최선을 다하겠습니다.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('&153\n고맙소. 참으로 고맙소, 조 장군.')
        stage.unit(153).setAction(0)
        stage.say('&153\n그렇다면 지체할 수 없군요.\n이 칠성검을 장군께 맡기겠습니다.')
    elif sel == 2:
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n그토록 중한 소임은 소인이 감당하기 어렵습니다.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('&153\n무슨 말씀입니까? 방금 그토록 명쾌하게 분석하셨으니\n이 일은 장군이 아니면 맡을 이가 없습니다.')
        stage.unit(153).setAction(0)
        stage.delay(5)
        stage.unit(196).setAction(0, 3)
        stage.say('&196\n조조, 왕 대인께서 그리 말씀하시니\n사양하지 마시오.')
        stage.delay(5)
        stage.say('&0\n……좋다.')
        stage.unit(0).setAction(6)
        stage.say('&0\n그렇다면 이 중책을 맡겠소.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('&153\n오, 받아들여 주겠는가!')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n최선을 다하겠습니다.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('&153\n고맙소. 고맙소, 조 공.')
        stage.unit(153).setAction(0)
        stage.say('&153\n그렇다면 지체할 수 없군요.\n이 칠성검을 장군께 맡기겠습니다.')
    stage.say('&153\n어서 나오너라. 초선아, 초선아!')
    stage.showUnit(17, 55, 29, 2)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n가요, 아버님.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.unit(196).setAction(0, 0)
    stage.delay(3)
    stage.unit(153).setAction(3)
    stage.say('&153\n초선, 어서 칠성검을 가져오너라.')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n예, 아버님.')
    stage.unit(17).setAction(0)
    stage.delay(5)
    stage.unit(17).hide()
    stage.delay(5)
    stage.unit(0).setAction(0, 2)
    stage.unit(196).setAction(0, 2)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('&196\n왕 대인, 방금 그 낭자는 누구입니까?')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('&153\n오, 내 양녀일세. 초선이라 하네.')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('&196\n그렇소? 참으로 놀랍구려.\n왕 대인께 이토록 아름다운 따님이 계실 줄이야.')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(3)
    stage.say('&153\n무슨 말씀을. 아직 어린아이일 뿐입니다.')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('&196\n아, 참으로 아름답구나.')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.showUnit(17, 55, 29, 2)
    stage.say('&17\n아버님, 오래 기다리셨지요.')
    stage.unit(17).move(55, 57, 1)
    stage.delay(3)
    stage.unit(153).setAction(0, 3)
    stage.say('&153\n오, 착한 내 딸아.')
    stage.unit(17).move(64, 57, 1)
    stage.delay(3)
    stage.unit(17).move(55, 57, 1)
    stage.say('&153\n초선아, 소개할 분이 계신다.')
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n이분은 태위 대인이시고\n이 젊은 분은 조 장군입니다.')
    stage.unit(17).setAction(6)
    stage.say('&17\n초선이라 하옵니다. 두 분께 인사 올립니다.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(196).setAction(0, 3)
    stage.delay(3)
    stage.unit(196).setAction(6)
    stage.say('&196\n아, 반갑소.')
    stage.unit(196).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n반갑소.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(6)
    stage.say('&153\n조 장군, 제 딸을 어찌 보셨습니까?')
    stage.unit(153).setAction(0)
    stage.delay(5)
    model.setFace(1)
    stage.unit(0).setAction(3)
    stage.say('&0\n응?')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('참으로 감탄스럽다\n별다를 것 없다', -1)
    if sel == 1:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.delay(3)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n아, 참으로 아름답구나.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(3)
        stage.say('&17\n과찬이세요. 좋게 보아 주셔서 감사합니다.')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(3)
        stage.say('&153\n하하하, 조 장군이 너를 무척 마음에 들어 하시는구나, 초선아.')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(1)
        stage.say('&0\n아, 왕윤 대감. 저는 그런 뜻이…….')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(17).setAction(3)
        stage.say('&17\n소녀의 영광입니다, 조 대인.')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n아, 예…….')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(17),
                55,
                63,
                0],
            [
                stage.unit(153),
                55,
                57,
                0]])
        stage.unit(153).move(55, 49, 1)
        stage.unit(153).setAction(0)
        stage.unit(0).setAction(0, 3)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('&153\n그럼 조 장군, 이 검을 맡기겠소.')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).move(64, 49, 1)
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.delay(3)
        stage.unit(153).move(55, 49, 1)
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('&153\n모든 것이 장군께 달렸습니다.')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n안심하십시오, 대인.\n반드시 기대에 보답하겠습니다.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(153),
                55,
                55,
                0],
            [
                stage.unit(0),
                55,
                49,
                2]])
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(6)
        stage.say('&0\n그럼 이만 물러가겠습니다.')
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).move(55, 29, 0)
        stage.unit(0).hide()
        stage.delay(5)
        stage.unit(153).setAction(0, 1)
        stage.say('&153\n참으로 뛰어난 젊은이로군.\n훗날 반드시 큰 공을 이루고 이름을 떨칠 것이다.')
        stage.unit(196).setAction(0, 2)
        stage.say('&196\n저도 같은 생각입니다.')
        stage.unit(17).setAction(0, 1)
        stage.say('&17\n(조조 대인…….)')
        vars[302] = 1
    elif sel == 2:
        stage.addAmbition(-5)
        stage.delay(3)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n왕 대인, 아직 검을 주지 않으셨습니다.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('&153\n아, 실례했소.')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(0, 3)
        stage.say('&153\n초선, 먼저 물러가 있거라.')
        stage.unit(17).setAction(6)
        stage.say('&17\n예.')
        stage.unit(17).setAction(0)
        stage.delay(3)
        stage.unit(17).move(55, 29, 0)
        stage.unit(17).hide()
        stage.delay(5)
        stage.unit(153).move(55, 57, 3)
        stage.unit(153).setAction(0)
        stage.unit(153).move(55, 49, 1)
        stage.unit(153).setAction(0)
        stage.unit(0).setAction(0, 3)
        stage.unit(196).setAction(0, 3)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('&153\n그럼 조 장군, 이 검을 맡기겠소.')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).move(64, 49, 1)
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.delay(3)
        stage.unit(153).move(55, 49, 1)
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(153).setAction(6)
        stage.say('&153\n모든 것이 장군께 달렸습니다.')
        stage.unit(153).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n안심하십시오, 대인.\n반드시 기대에 보답하겠습니다.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unitsMove([
            [
                stage.unit(153),
                55,
                55,
                0],
            [
                stage.unit(0),
                55,
                49,
                2]])
        stage.unit(153).setAction(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n그럼 이만 물러가겠소.')
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).move(55, 29, 0)
        stage.unit(0).hide()
        stage.delay(5)
        stage.unit(153).setAction(0, 1)
        stage.say('&153\n참으로 뛰어난 젊은이로군.\n훗날 반드시 큰 공을 이루고 이름을 떨칠 것이다.')
        stage.unit(196).setAction(0, 2)
        stage.say('&196\n저도 같은 생각입니다.')
        vars[303] = 1
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 282, 95)
    stage.bottomTxt('조조는 왕윤의 부탁을 받아들여', False, False, False)
    stage.delay(5)
    stage.head(0).move(378, 118)
    stage.delay(5)
    stage.bottomTxt('낙양을 떠나 고향 진류로 돌아갔다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('동탁 토벌 동맹을 결성하는 격문을 각지에 보냈다.', True, True, True)
    stage.bottomTxt('그 격문에 호응한 이는', False, True, False)
    stage.delay(5)
    stage.showHead(101, 408, 8)
    stage.delay(5)
    stage.bottomTxt('발해태수 원소,', True, False, False)
    stage.delay(5)
    stage.showHead(161, 478, 0)
    stage.delay(5)
    stage.bottomTxt('북평태수 공손찬과', True, False, False)
    stage.delay(5)
    stage.showHead(145, 476, 126)
    stage.delay(5)
    stage.bottomTxt('서주 자사 도겸,', True, False, False)
    stage.delay(5)
    stage.showHead(99, 347, 229)
    stage.delay(5)
    stage.bottomTxt('장사 태수 손견과', True, True, False)
    stage.delay(5)
    stage.showHead(136, 258, 200)
    stage.delay(5)
    stage.bottomTxt('남양 태수 원술 등이', True, False, False)
    stage.delay(5)
    stage.bottomTxt('모두 천하의 뛰어난 호걸이었다.', True, False, True)
    stage.loadBg(0, 16)
    stage.draw()
    stage.info('한편 조조는 왕윤의 칠성검을 팔아\n그 돈으로 자신의 군대를 일으켰다.')
    stage.bgSound(18)
    stage.loadBg(2, 21)
    stage.showUnit(0, 49, 67)
    stage.draw()
    stage.setStageName('진류@조조군 본거지')
    stage.showUnits([
        [
            1,
            49,
            22,
            2],
        [
            6,
            49,
            16,
            2]])
    stage.say('&1\n맹덕!')
    stage.unitsMove([
        [
            stage.unit(1),
            49,
            52,
            2],
        [
            stage.unit(6),
            49,
            46,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(1),
            45,
            52,
            2],
        [
            stage.unit(6),
            53,
            52,
            2]])
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n맹덕, 오랜만일세!')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하후돈, 하후연. 드디어 왔구나.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n자네가 군사를 일으켰는데 우리가 어찌 오지 않겠는가?')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(6).setAction(6)
    stage.say('&6\n그런데 갑자기 병마를 모으라며 돈을 보내 주셔서\n저희도 깜짝 놀랐습니다.')
    stage.unit(6).setAction(0)
    stage.say('&0\n그대들의 병사들은 어디 있소?')
    stage.unit(1).setAction(17)
    stage.say('&1\n밖에서 대열을 갖추고 있네.')
    stage.unit(1).setAction(3)
    stage.say('&1\n우리 둘이 엄격하게 훈련시킨 병사들이니\n때가 오면 솜씨를 보여 주지.')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n음, 친형제나 다름없는 그대들 둘과\n함께 싸우게 되었으니\n내 마음도 한결 든든하구나.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 1)
    stage.say('&1\n참, 조인과 조홍은 어찌 되었는가?')
    stage.unit(6).setAction(0, 3)
    stage.say('&6\n오, 그렇군…….')
    stage.say('&0\n아직 소식이 없군……….')
    stage.showUnit(181, 49, 16, 2)
    stage.say('&181\n보고드립니다.')
    stage.unit(1).setAction(0, 0)
    stage.unit(6).setAction(0, 0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 장사 둘이 우리 군에 합류하고자 찾아왔습니다.')
    stage.unit(1).setAction(0, 1)
    stage.say('&1\n조인과 조홍인가?')
    stage.say('&0\n어서 들라 하라!')
    stage.say('&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(1),
            35,
            52,
            1],
        [
            stage.unit(6),
            63,
            52,
            3]])
    stage.showUnits([
        [
            10,
            49,
            16,
            2],
        [
            11,
            49,
            16,
            2]])
    stage.unit(10).move(49, 22, 2)
    stage.unitsMove([
        [
            stage.unit(10),
            45,
            52,
            2],
        [
            stage.unit(11),
            53,
            52,
            2]])
    stage.delay(3)
    stage.say('&0\n두 사람 모두 우리 군에 들어오겠는가?')
    stage.unit(10).setAction(11)
    stage.say('&10\n그렇습니다. 저는 악진, 자는 문겸이라 합니다.\n&10\n대인께서 천하를 걱정하시는 뜻에 깊이 감명받아\n이렇게 찾아왔습니다.\n&10\n부디 거두어 주십시오.')
    stage.unit(11).setAction(11)
    stage.say('&11\n저는 이전, 자는 만성이라 합니다.\n&11\n악진과 마찬가지로 대인께 의탁하고자 왔습니다.\n&11\n부디 받아 주십시오.')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n막 군사를 일으켰는데 그대들 같은 뛰어난 장수들이\n찾아와 주다니 참으로 기쁘오! 모두 환영하오!')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&10\n예, 대인!')
    stage.say('&11\n감사합니다!')
    stage.unit(10).setAction(0)
    stage.unit(11).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(10),
            45,
            46,
            0],
        [
            stage.unit(11),
            53,
            46,
            0]])
    stage.showUnit(5, 49, 16, 2)
    stage.unit(5).move(49, 22, 2)
    stage.showUnit(12, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(10),
            35,
            46,
            1],
        [
            stage.unit(11),
            63,
            46,
            3],
        [
            stage.unit(5),
            49,
            52,
            2],
        [
            stage.unit(12),
            49,
            46,
            2]])
    stage.unitsMove([
        [
            stage.unit(5),
            45,
            52,
            2],
        [
            stage.unit(12),
            53,
            52,
            2]])
    stage.delay(3)
    stage.unit(5).setAction(9)
    stage.unit(12).setAction(9)
    stage.say('&5\n허억, 허억. 죄, 죄송합니다. 늦었습니다.')
    stage.say('&12\n하하, 조조 대인!\n저희도 대인의 휘하에 들게 해 주십시오.')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n자효, 자렴, 이제야 왔구나. 어찌하여 늦었느냐?')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.say('&5\n후우, 병력을 배치하느라 시간이 걸렸군.')
    stage.say('&12\n후후, 저는 진작 준비를 마쳤습니다.\n시간을 끈 것은 조인입니다.')
    stage.unit(5).setAction(0, 1)
    stage.delay(3)
    stage.unit(5).setAction(2, 1)
    stage.say('&5\n뭐라고, 모두 내 탓이라는 말인가!')
    stage.unit(12).setAction(0, 3)
    stage.delay(3)
    stage.unit(12).setAction(2, 3)
    stage.say('&12\n그렇지 않습니까?')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하, 그만들 다투게!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(5).setAction(0, 2)
    stage.unit(12).setAction(0, 2)
    stage.say('&0\n걱정 마라. 너희 몫도 넉넉히 챙겨 줄 테니 힘써라.')
    stage.unit(5).setAction(6, 2)
    stage.say('&5\n예, 분부대로 하겠습니다!')
    stage.unit(12).setAction(6, 2)
    stage.say('&12\n제게 맡기십시오!')
    stage.unit(5).setAction(0, 2)
    stage.unit(12).setAction(0, 2)
    stage.unit(1).setAction(0, 2)
    stage.unit(6).setAction(0, 2)
    stage.unit(10).setAction(0, 2)
    stage.unit(11).setAction(0, 2)
    stage.delay(5)
    model.setFace(0)
    stage.unit(0).setAction(3)
    stage.say('&0\n음, 지금은 시간이 촉박하다.\n서둘러 동맹군과 합류하자!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n출발하라!')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 54)
    stage.showUnit(0, 54, 52, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.setStageName('낙양 교외@동맹군 본진')
    stage.say('&0\n집결 시각까지는 아직 여유가 있군.\n누가 모일지 궁금하구나.\n&99\n실례하겠소.')
    stage.unit(0).setAction(0, 2)
    stage.showUnit(99, 54, 94)
    stage.unit(99).move(54, 60, 0)
    stage.delay(3)
    stage.unit(99).setAction(6)
    stage.say('&99\n저는 손견, 자는 문대라 합니다. 격문을 보고\n동맹군에 합류하러 왔습니다. ……그대는 누구십니까?')
    stage.unit(99).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n오, 강동의 손 장군이셨군요.\n저는 조조, 자는 맹덕이라 합니다.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&99\n오, 그대가 조조시구려. 잘 부탁하오.')
    stage.unit(0).setAction(6)
    stage.say('&0\n대인께서 참으로 일찍 오셨군요.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.say('&99\n별말씀을요. 각지에서 온 영웅들의 솜씨를\n대인께서 한번 평가해 주셨으면 했을 뿐입니다.')
    stage.unit(99).setAction(6)
    stage.say('&99\n대감께서도 속으로는 그러실 생각이겠지요?')
    stage.unit(99).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n……그렇군.(……만만치 않은 인물이야.)')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.unit(99).move(42, 68, 1)
    stage.delay(10)
    stage.info('잠시 뒤……,', 3)
    stage.showUnit(136, 54, 94)
    stage.unit(136).move(54, 60, 0)
    stage.delay(3)
    stage.unit(136).setAction(3)
    stage.say('&136\n나는 남양의 원술이오. 내 자리는 어디요?')
    stage.unit(136).setAction(0)
    stage.say('&0\n(이자가 바로 원소의 아우로군…….)')
    stage.unit(0).setAction(6)
    stage.say('&0\n원술이셨구려. 왼편에 앉으시오.')
    stage.unit(0).setAction(0)
    stage.say('&136\n뭐라고? 상석이 아니라고? 쳇!')
    stage.unit(136).move(42, 60, 1)
    stage.say('&99\n히히…….')
    stage.unit(136).setAction(2, 2)
    stage.say('&136\n무엇이 우습다는 건가?')
    stage.unit(136).setAction(0, 2)
    stage.say('&99\n아니, 아무것도 아니오.')
    stage.unit(136).setAction(2, 2)
    stage.say('&136\n……그대는 누구요?!\n&99\n……나는 강동의 손견이오.')
    stage.unit(136).setAction(0, 1)
    stage.delay(3)
    stage.unit(136).setAction(3, 1)
    stage.say('&136\n뭐야, 한낱 촌놈이었군!')
    stage.unit(136).setAction(0)
    stage.say('&99\n…………。')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n(……정말 성가신 놈이군.)')
    stage.showUnit(145, 54, 94)
    stage.say('&145\n여러 영웅께 실례했소!')
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(145).move(54, 60, 0)
    stage.delay(3)
    stage.unit(145).setAction(6)
    stage.say('&145\n저는 서주의 도겸이라 합니다.')
    stage.unit(145).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n저는 조맹덕이라 합니다.\n도 대인께서 인의를 갖춘 지도자이시라는 말씀은\n일찍부터 들었습니다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(145).setAction(6)
    stage.say('&145\n무슨 말씀을. 이 늙은 몸이\n모두에게 짐이나 되지 않았으면 좋겠소.')
    stage.unit(145).setAction(0)
    stage.delay(1)
    stage.unit(145).move(42, 76, 1)
    stage.showUnits([
        [
            161,
            54,
            94],
        [
            32,
            62,
            94]])
    stage.say('&161\n실례했소!')
    stage.unit(0).setAction(0, 2)
    stage.unitsMove([
        [
            stage.unit(161),
            54,
            60,
            0],
        [
            stage.unit(32),
            62,
            60,
            0]])
    stage.delay(3)
    stage.unit(161).setAction(6)
    stage.say('&161\n북평의 공손찬이오. 자는 백규라 하오.')
    stage.unit(161).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n조조라 하오. 자는 맹덕이오.')
    stage.unit(0).setAction(0)
    stage.say('&0\n음? 이분은…….')
    stage.unit(161).setAction(6)
    stage.say('&161\n아, 이분은 유비 유현덕일세.\n황건군을 토벌하는 의병의 수장이지.\n이번에 나와 함께 왔다네.')
    stage.unit(161).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(6)
    stage.say('&0\n아, 유비……?\n영천에서 황건적과 싸울 때 한번 만났었지.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(6)
    stage.say('&32\n그렇소. 오랜만이오.\n이번에는 공손 장군을 따라 도우러 왔소.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n그랬군…… 참으로 잘되었네.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n그럼 공손 장군과 유 장군께서는 오른편에 앉으시오.')
    stage.say('&161\n음.')
    stage.say('&32\n예.')
    stage.unit(32).move(67, 68, 3)
    stage.unit(32).setAction(0)
    stage.unit(161).move(67, 60, 3)
    stage.unit(161).setAction(0)
    stage.showUnit(101, 54, 94)
    stage.say('&101\n실례하겠소.')
    stage.unitsMove([
        [
            stage.unit(0),
            42,
            52,
            1],
        [
            stage.unit(101),
            54,
            52,
            0]])
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 2)
    stage.say('&101\n나는 하북의 원소, 자는 본초라 하오.')
    stage.unit(0).setAction(3)
    stage.say('&0\n본초, 그대를 기다리고 있었소.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 3)
    stage.say('&101\n조조, 너 때문에 온 것은 아니다.')
    stage.unit(101).setAction(0, 2)
    stage.say('&101\n내 뜻으로 온 것이오.')
    stage.say('&0\n그렇지, 알고 있네.')
    stage.say('&0\n자, 본초. 상석에 앉으시게. 이번에는 자네가 총대장이니.')
    stage.unit(101).setAction(0, 3)
    stage.say('&101\n내가 총대장을 맡으라고? 그럼 그대는, 조조?')
    stage.say('&0\n저 말입니까? 참모를 맡겠습니다.\n잘 부탁드립니다, 원 대인.\n&101\n원 대인이라니? 조조, 그러지 말게.\n&0\n그대는 총사령관이고 저는 참모에 불과하니\n사람들 앞에서는 당연히 그렇게 불러야지요.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.clsUnit()
    stage.bgSound(16)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(127, 263, 38)
    stage.bottomTxt('동탁은 낙양에서 포악한 짓을 일삼았고,', False, False, False)
    stage.delay(5)
    stage.showHead(0, 371, 74)
    stage.showHead(101, 435, 74)
    stage.showHead(161, 499, 74)
    stage.showHead(136, 371, 154)
    stage.showHead(99, 435, 154)
    stage.showHead(145, 499, 154)
    stage.bottomTxt('그리하여 동탁을 토벌하고자,', True, True, False)
    stage.bottomTxt('동탁 토벌 동맹군은 낙양 근교에 집결했다.', True, False, True)
    stage.bottomTxt('이 사실을 안 동탁은 크게 격노했다. 동맹군을 물리치고자,', False, True, False)
    stage.delay(5)
    stage.showHead(134, 263, 118)
    stage.delay(5)
    stage.bottomTxt('이에 맹장 화웅을 보내', True, False, False)
    stage.delay(5)
    stage.head(134).move(307, 118)
    stage.delay(5)
    stage.bottomTxt('사수관으로 향했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            101,
            54,
            37,
            2],
        [
            0,
            50,
            45,
            2],
        [
            99,
            42,
            71,
            1],
        [
            136,
            42,
            63,
            1],
        [
            145,
            42,
            55,
            1],
        [
            161,
            67,
            55,
            3],
        [
            32,
            67,
            63,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.setStageName('낙양 교외@동맹군 본진')
    stage.showUnit(181, 54, 94)
    stage.unit(181).move(54, 86, 0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n보고드립니다! 동탁군이 사수관에 도착했습니다.\n선봉장은 화웅입니다.')
    stage.unit(101).setAction(3)
    stage.say('&101\n화웅?! 맹장이라 들었는데!')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('&101\n좋다. 우리 군도 사수관으로 향한다.')
    stage.unit(101).setAction(0)
    stage.say('&101\n누가 선봉을 맡아 화웅을 무찌르겠소?')
    stage.delay(5)
    stage.unit(99).setAction(6)
    stage.say('&99\n부족하나 이 손견이 맡겠습니다!')
    stage.unit(99).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(3)
    stage.say('&101\n오, 손 장군이시군.\n좋소. 그대를 선봉으로 삼겠소.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(99).setAction(6)
    stage.say('&99\n즉시 준비하겠소.')
    stage.unit(99).setAction(0)
    stage.delay(3)
    stage.unit(99).move(42, 95, 2)
    stage.unit(99).hide()
    stage.delay(5)
    stage.unit(101).setAction(17)
    stage.say('&101\n원술, 그대가 군량 보급을 맡아 주겠는가?')
    stage.unit(101).setAction(0)
    stage.delay(5)
    stage.say('&136\n……예.')
    stage.delay(5)
    stage.unit(136).setAction(0, 2)
    stage.say('&136\n(흥, 같은 원씨인데\n너는 맹주고 나는 군량을 나르는 관리라니.)')
    stage.unit(136).setAction(2)
    stage.say('&136\n(괘씸하군. 뻔뻔한 손견이\n첫 공을 가로챌 셈인가……?)')
    stage.unit(136).setAction(0)
    stage.say('&136\n(음, 잠깐! 손견군의 군량은 모두 내가 배급하니\n그렇다는 것은…….)')
    stage.unit(136).setAction(3)
    stage.say('&136\n(히히히히…….)')
    stage.unit(136).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('&101\n좋다. 각자 진영으로 돌아가 출발을 준비하라.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene6():
    model.unitJoin(1, 0, 0)
    model.unitJoin(6, 0, 0)
    model.unitJoin(10, 0, 0)
    model.unitJoin(11, 0, 0)
    model.unitJoin(5, 0, 0)
    model.unitJoin(12, 0, 0)
    if vars[1013]:
        gvars[0] = 1
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
        gvars[0] = 6
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
        gvars[0] = 10
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
        gvars[0] = 11
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
        gvars[0] = 5
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
        gvars[0] = 12
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


def scene7():
    stage.bgSound(15)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            66],
        [
            1,
            38,
            52,
            1],
        [
            5,
            38,
            46,
            1],
        [
            10,
            38,
            40,
            1],
        [
            6,
            60,
            52,
            3],
        [
            12,
            60,
            46,
            3],
        [
            11,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('사수관@조조군 본진')
    stage.say('&0\n마침내 동탁군과 싸우게 되었군.\n문대 장군이 선봉을 맡고\n나머지 부대가 뒤따른다. 모두 온 힘을 다하라.')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        1])
    gvars[4076] = 0
    gvars[4057] = 1
    stage.setBattlePos([
        [
            19,
            9],
        [
            18,
            8],
        [
            18,
            11],
        [
            18,
            9],
        [
            18,
            10],
        [
            19,
            8],
        [
            19,
            11],
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
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n참, 맹덕. 예전에 황건적과 싸울 때\n《태평청령도》를 얻었다고 들었네.\n&0\n그렇네.\n&1\n잊지 말고 장비해 두는 게 좋겠어.\n앞으로도 노획품을 제대로 장비했는지 살펴보게.')
    if stage.unitClickTest(6):
        stage.say('&6\n싸움 중에 우군이 얻은 도구와 무기는\n싸움이 끝나면 모두 우리 군의 것이 됩니다.\n&6\n그래서 어쩐지 남의 것을 챙기는 기분이 듭니다.')
    if stage.unitClickTest(5):
        stage.say('&5\n화웅 따위는 대수롭지 않습니다.\n&0\n적을 얕보는 그 버릇은 좋지 않네.')
    if stage.unitClickTest(12):
        stage.say('&12\n보병대는 기병대보다 이동력과 공격력이 낮지만\n방어력이 높습니다.')
    if stage.unitClickTest(10):
        stage.say('&10\n하후돈 장군과 조인 장군은 기병이라\n이동력과 공격력이 뛰어나지만\n방어력은 다소 떨어집니다…….\n&1\n뭐라고 했느냐!!\n&10\n아, 아무것도 아닙니다…….')
    if stage.unitClickTest(11):
        stage.say('&11\n하후연 장군께서 이끄는 궁기병은\n우리 군에서 유일하게 원거리 공격이 가능한 부대이니\n중히 활용할 만한 병종입니다.\n&6\n오, 나를 잘 아는군.')
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
