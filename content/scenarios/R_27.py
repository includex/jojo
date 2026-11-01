# Source Generated with Decompyle++
# File: R_27.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 265, 172)
    stage.showHead(32, 329, 172)
    stage.showHead(76, 565, 216)
    stage.bottomTxt('유비군', False, False, False)
    stage.delay(5)
    stage.head(32).move(364, 192)
    stage.delay(5)
    stage.bottomTxt('강하로 달아났다.', True, False, False)
    stage.delay(8)
    stage.head(0).move(272, 213)
    stage.delay(5)
    stage.bottomTxt('조조군은 장강 연안의 요충지 강릉에 진주했다.', True, True, False)
    stage.delay(8)
    stage.showHead(35, 428, 192)
    stage.bottomTxt('제갈량은 조조에 맞서고자 유비에게 손권과의 동맹을 건의했다.', True, True, False)
    stage.delay(5)
    stage.head(35).move(501, 216)
    stage.delay(5)
    stage.bottomTxt('홀로 동오로 가서 유세했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 49)
    stage.showUnits([
        [
            0,
            53,
            72],
        [
            24,
            40,
            56,
            1]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setEventName('적벽으로 가는 길')
    stage.setStageName('강릉  임시 승상부')
    stage.say('&0\n유비를 궁지에 몰아넣기는 했지만\n그자의 명줄도 참 질기군.')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n유비는 강하로 달아난 듯합니다.\n&0\n강하라……? 동오와 가까운 곳이군.\n좋다, 공달. 손권에게 사신을 보내게.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n유비 토벌을 도우면\n그 영토의 안전을 보장하겠다고 전하라.')
    stage.unit(24).setAction(0)
    stage.say('&24\n최후통첩입니까……?')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n그렇다. 그리고………\n전군은 동오와 접한 오림으로 집결하라.')
    stage.unit(24).setAction(6)
    stage.say('&24\n손권이 받아들이겠습니까?\n&0\n거절하면 치면 되지.')
    stage.say('&24\n예. 즉시 처리하겠습니다.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 10)
    stage.showUnits([
        [
            76,
            71,
            55,
            3],
        [
            77,
            55,
            38,
            2],
        [
            88,
            49,
            38,
            2],
        [
            35,
            55,
            55,
            1],
        [
            192,
            55,
            71],
        [
            193,
            49,
            71],
        [
            194,
            43,
            71]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('동오  손권의 관저')
    stage.info('조조의 최후통첩을 받은 동오에서는\n신하들이 주전파와 주화파로 갈렸다.\n하지만 제갈량의 설득으로\n점차 개전에 뜻을 모았다.')
    stage.say('&76\n공근, 장수들은 싸우고 싶어 하나\n나는 조조를 이기지 못할까 걱정되네.')
    stage.unit(88).setAction(6)
    stage.say('&88\n나도 같은 생각이오.\n나라를 보전하려면 항복해야 하오.')
    stage.unit(88).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('&77\n조조가 우리의 고향을 짓밟게 둘 셈입니까?')
    stage.unit(77).setAction(4)
    stage.say('&77\n그때는 우리도 돌아갈 곳을 잃게 됩니다.')
    stage.unit(77).setAction(17)
    stage.say('&77\n주군, 군주이신 이상\n간적 조조와 단호히 맞서 싸우셔야 합니다.')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(6)
    stage.say('&35\n그렇습니다.\n항복해도 조조에게 멸망할 뿐입니다.\n&88\n그만하시오!\n싸운다고 이길 수 있다는 말이오!')
    stage.unit(35).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 0)
    stage.say('&35\n이길 수 있습니다.')
    stage.unit(76).setAction(3)
    stage.say('&76\n뭐라고!?\n어찌 그리 확신하는가?')
    stage.unit(76).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n조조의 주력은 북방 병사들이니\n수전에 익숙하지 않을 것입니다.\n&35\n게다가 풍토에 적응하지 못해\n군영에는 역병까지 돌고 있을 테지요.\n&35\n조조의 병력이 아무리 많아도\n결국 패할 운명입니다.')
    stage.say('&76\n음, 분명 그렇군……\n좋다! 결심했다!\n조조와 끝까지 맞서 싸우겠다.')
    stage.unit(77).setAction(3)
    stage.say('&77\n으음, 주군의 혜안에 감복합니다!\n반드시 온 힘을 다하겠습니다!\n&76\n공근, 그대에게 전군의 지휘를 맡기겠다.\n그대만 믿겠다.')
    stage.unit(77).setAction(11)
    stage.say('&77\n반드시 승리하겠습니다.\n염려 마십시오, 주군.\n&76\n제갈 선생께서 공근을 도와주시오.')
    stage.unit(35).setAction(6)
    stage.say('&35\n예.')
    stage.unit(76).setAction(17)
    stage.say('&76\n좋다! 출진하라!')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 49)
    stage.showUnits([
        [
            0,
            53,
            72],
        [
            24,
            40,
            56,
            1]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('강릉  임시 승상부')
    stage.say('&0\n손권이 유비와 동맹을 맺었다고?')
    stage.unit(24).setAction(0, 2)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n예.\n손권이 주유를 도독으로 삼아\n지금 적벽에 병력을 집결시키고 있습니다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n공달, 즉시 장수들에게 전하라.\n전군은 적벽 건너편 오림으로 향한다.\n손권을 무너뜨리겠다.')
    stage.say('&24\n명을 받들겠습니다.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 264, 192)
    stage.showHead(32, 368, 191)
    stage.showHead(76, 559, 210)
    stage.bottomTxt('손권은 조조군과 싸우기로 결심하고,', False, False, False)
    stage.delay(5)
    stage.head(32).move(368, 158)
    stage.head(76).move(339, 238)
    stage.delay(5)
    stage.bottomTxt('이에 적벽으로 함선을 집결시켰다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('조조군도 움직여', True, True, False)
    stage.delay(5)
    stage.head(0).move(275, 238)
    stage.delay(5)
    stage.bottomTxt('적벽 건너편 오림에 들어갔다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('양군이 팽팽히 대치하던 그때,', True, True, False)
    stage.delay(5)
    stage.bottomTxt('한 사람이 조조의 본진을 찾아왔다.', True, False, False)
    stage.delay(5)
    stage.showHead(90, 243, 158)
    stage.delay(5)
    stage.bottomTxt('그가 바로 방통이었다.', True, False, True)
    stage.bgSound(-1)
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
            90,
            54,
            64,
            2]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setStageName('오림  조조군 본진')
    stage.unit(0).setAction(3)
    stage.say('&0\n방사원 선생은 천하에 이름난 현인이시니\n우리 군의 진용을 보신 소감이 어떠하시오?')
    stage.unit(0).setAction(0)
    stage.say('&90\n오! 참으로 놀랍습니다!\n수채와 육지의 영채가 모두 병법대로 배치되었군요.')
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n그렇습니까?\n방사원 선생께서 그리 말씀해 주시니\n참으로 기쁘기 그지없습니다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(90).setAction(6)
    stage.say('&90\n하지만…배치는 완벽해도\n병사들의 안색은 너무 나쁩니다.')
    stage.unit(90).setAction(0)
    model.setFace(0)
    stage.say('&0\n음? 그런 것까지 꿰뚫어 보시오!\n과연 방사원 선생이오.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n요즘 병을 앓는 병사들이 부쩍 늘어\n나도 근심하던 참이네.\n&90\n역시 그랬군요.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n선생, 무슨 좋은 방책이 있으십니까?')
    stage.unit(90).setAction(6)
    stage.say('&90\n있습니다.\n병사들이 병드는 주된 이유는\n배 위의 생활에 익숙하지 않아서입니다.')
    stage.unit(90).setAction(0)
    stage.say('&90\n쇠사슬로 배를 이어\n함대를 모두 고정하면 됩니다.\n&90\n그러면 배의 흔들림이 줄어\n평지처럼 다닐 수 있으니\n병사들도 병들지 않을 것입니다.\n&0\n그렇군…… 음, 과연!\n좋다! 당장 명을 내리겠소.\n가르침에 감사하오.')
    stage.unit(90).setAction(6)
    stage.say('&90\n그럼 이만 물러가겠습니다.\n&0\n그런가?\n선생만 좋으시다면\n앞으로도 군에 머물며 나를 가르쳐 주시겠소?\n&90\n호의에 감사드립니다, 승상.\n저는 본디 한가로운 구름과 들의 학처럼\n떠도는 삶이 어울립니다.\n&0\n그런가?\n참으로 아쉽군.\n&90\n그럼 이만 물러가겠습니다.')
    stage.unit(90).setAction(0)
    stage.delay(3)
    stage.unit(90).move(54, 22, 0)
    stage.unit(90).hide()
    stage.say('&0\n원양, 모두 들었겠지?\n즉시 배들을 연결할 쇠사슬을 만들게.')
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n하지만 맹덕……\n전선을 쇠사슬로 연결하니 정말 흔들리지 않고\n병사들이 병드는 것도 막을 수 있네.')
    stage.unit(1).setAction(3)
    stage.say('&1\n하지만 그만큼 움직임이 둔해지지.\n오군이 화공을 쓰면\n그때는 손쓸 도리가 없네.')
    stage.unit(1).setAction(0)
    stage.say('&0\n원양, 걱정할 것 없네.\n이 계절에는 북서풍만 불지.\n즉, 바람은 모두 동오군 쪽으로 분다는 말일세.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n화공을 쓰면 제 몸에 불을 붙이는 꼴이 되지 않겠는가?\n&1\n오! 그렇군.\n좋아, 당장 쇠사슬을 만들겠네.')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene4():
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
            23,
            64,
            58,
            3],
        [
            24,
            64,
            52,
            3]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.info('며칠 뒤…….', 3)
    stage.setStageName('오림  조조군 본진')
    stage.showUnit(181, 54, 22, 2)
    stage.say('&181\n보고드립니다.')
    stage.unit(181).setAction(11)
    stage.say('&181\n주군.\n오의 장수 황개가 보냈다는 사신이 왔습니다.\n&0\n황개가 사신을 보냈다고?\n황개라면 동오의 노장 아닌가?\n좋다, 데려오라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.delay(5)
    stage.showUnit(192, 54, 22, 2)
    stage.say('&192\n안녕하십니까.')
    stage.unit(192).move(54, 64, 2)
    stage.say('&0\n내가 한의 승상 조조다.\n황개의 사신이라 했는가?\n무슨 일로 왔느냐?')
    stage.unit(192).setAction(6)
    stage.say('&192\n주인 황개의 명을 받들어\n귀순을 청하러 왔습니다.\n&0\n황개가 내게 귀순하겠다고?\n황개는 동오에 충성을 다한 공신이지 않은가.\n어째서 내게 항복하려는가?\n&192\n주인께서 동오의 공신이신 것은 사실입니다만\n사소한 일 때문에\n며칠 전 도독 주유에게 사람들 앞에서 곤장을 맞으셨습니다.\n&192\n살갗이 찢어지고 피투성이가 되어\n지금까지도 자리에서 일어나지 못하십니다.\n&192\n이에 그 치욕을 씻고자\n승상의 휘하에 몸을 의탁하려 하십니다.\n&0\n음, 사정은 대강 알겠네.\n이제 신하들과 의논해 보겠으니\n잠시 물러가 내 답을 기다리게.\n&192\n예.')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).move(54, 22, 0)
    stage.unit(192).hide()
    stage.say('&0\n문약, 아까 사자가 한 말에 대해\n자네는 어찌 생각하는가?')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n도무지 믿기 어렵습니다…….\n황개 같은 무장이 배신을 하겠습니까?\n아무래도 이치에 맞지 않습니다…….')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(0, 2)
    stage.say('&24\n하지만 매질까지 당했다면\n사실일지도 모릅니다.\n&0\n음.')
    stage.effectSound(208, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('사자를 믿는다\n믿을 수 없다', 0)
    if sel == 1:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(208, 0)
        stage.delay(3)
        stage.say('&0\n좋다! 일단 그를 믿어 보겠네.\n문약, 사자를 불러들이게.\n&20\n예.')
        stage.unit(20).move(54, 64, 3)
        stage.delay(1)
        stage.unit(20).move(54, 22, 0)
        stage.unit(20).hide()
        stage.unit(23).setAction(0, 2)
        stage.say('&23\n정말 이대로 해도 괜찮겠습니까?\n상대를 이리 쉽게 믿으시다니\n아무래도………\n&0\n걱정할 것 없네.\n황개가 거짓 항복을 한다 해도\n큰 탈은 없을 것이야.\n&0\n허나 진정한 귀순이라면\n우리 군에 큰 보탬이 되지.')
        stage.delay(5)
        stage.showUnit(20, 54, 22, 2)
        stage.unit(20).move(54, 64, 2)
        stage.unit(20).setAction(6)
        stage.unit(23).setAction(0, 3)
        stage.unit(24).setAction(0, 3)
        stage.delay(3)
        stage.say('&20\n주군, 사자가 왔습니다.\n&0\n음, 수고했네.')
        stage.unit(20).move(64, 64, 3)
        stage.delay(5)
    elif sel == 2:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(208, 0)
        stage.delay(3)
        stage.say('&0\n그 사자가 한 말은\n어딘가 석연치 않다.\n그대로 믿어서는 안 되겠군.')
        stage.delay(5)
        stage.showUnit(181, 54, 22, 2)
        stage.unit(181).move(54, 64, 2)
        stage.unit(20).setAction(0, 3)
        stage.unit(24).setAction(0, 3)
        stage.delay(3)
        stage.unit(181).setAction(11)
        stage.say('&181\n주군, 동오에 잠입한 첩자의 보고입니다!\n&0\n오, 보자.\n&181\n예! 살펴 주십시오.')
        stage.unit(181).setAction(0)
        stage.delay(3)
        stage.unit(181).move(54, 76, 2)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.delay(3)
        stage.unit(181).move(54, 64, 2)
        stage.delay(3)
        stage.unit(181).setAction(11)
        stage.say('&0\n오, 아까 사신의 말이 사실이었군.')
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n주유가 정말 황개를 매질했단 말인가?')
        stage.unit(20).setAction(0)
        stage.say('&0\n음.\n다른 장수들도 주유에게 반감을 품었다는 보고가 있네.\n&0\n좋다! 황개의 사자를 불러라.\n&181\n예.')
        stage.unit(181).setAction(0)
        stage.delay(3)
        stage.unit(181).move(54, 22, 0)
        stage.unit(181).hide()
        stage.delay(5)
    stage.showUnit(192, 54, 22, 2)
    stage.say('&192\n실례했습니다.')
    stage.unit(192).move(54, 64, 2)
    stage.unit(20).setAction(0, 3)
    stage.say('&0\n오, 오래 기다리셨소.\n방금 참모들과 의논해 보았는데\n그대의 말이 거짓은 아닌 듯하오.')
    stage.unit(192).setAction(6)
    stage.say('&192\n그럼 저희의 귀순을 받아 주시겠습니까?\n&0\n음, 황 노장군께 귀순을 환영한다고 전하게.\n&192\n감사합니다, 승상.\n&0\n그럼 황 노장군은……\n언제 귀순할 생각인가?\n&192\n주인께서는\n승상께서 허락하신다면\n사흘 뒤에 귀순하겠다 하셨습니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n사흘 뒤라고……?\n&192\n저희 주군께서 청룡기를 달고\n군량을 가득 실은 전선을 타고 귀순하실 것입니다.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n알겠다.\n그가 오기를 기다리겠다.\n&192\n그럼 즉시 돌아가 주인 황개께 아뢰겠습니다.\n이만 물러가겠습니다.\n&0\n음, 수고했네.')
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).move(54, 22, 0)
    stage.unit(192).hide()
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene5():
    if vars[724]:
        stage.bgSound(0)
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
                17,
                44,
                52,
                1],
            [
                20,
                64,
                64,
                3],
            [
                23,
                64,
                58,
                3],
            [
                21,
                64,
                52,
                3]])
        stage.draw()
        stage.info('황개의 투항을 받아들인 지 사흘 뒤,\n마침내 약속한 날이 왔다.')
        stage.setStageName('오림  조조군 본진')
        stage.say('&0\n오늘이 황개와 약조한 날이다.\n청룡기를 단 배가 도착하면\n출발한다!')
        stage.say('&22\n절대로 맞아들여서는 안 됩니다!')
        stage.showUnit(22, 54, 22, 2)
        stage.unit(22).move(54, 64, 2)
        stage.unit(0).setAction(3)
        model.setFace(3)
        stage.say('&0\n오, 봉효!\n병은 다 나았는가?')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(6)
        stage.say('&22\n예.\n염려를 끼쳤습니다만\n이 봉효는 이제 다 나았습니다.\n&0\n그런가? 참으로 다행이군.')
        model.setFace(0)
        stage.say('&0\n그런데…… 어찌하여 나를 말리는가?\n설마 동오 정벌을 그만두자는 말인가?')
        stage.unit(22).setAction(0)
        stage.say('&22\n아닙니다! 동오 공격은 반드시 해야 합니다.\n하지만 이대로 출병하면\n동오가 바라는 대로 되고 맙니다.')
        stage.unit(20).setAction(6)
        stage.say('&20\n곽가 선생, 대체 어찌 된 일입니까?')
        stage.unit(22).setAction(0, 1)
        stage.say('&22\n순욱 선생, 냉정히 생각하셨다면\n선생처럼 지혜로운 군사께서\n그 정도 잔꾀를 못 알아보실 리 없습니다.')
        stage.unit(20).setAction(0)
        stage.say('&20\n예?')
        stage.unit(22).setAction(0, 2)
        stage.say('&22\n주군, 밖의 풍향이 바뀌었습니다.\n&0\n풍향이 바뀌었다고……?\n우리 군이 바람을 마주하게 되었다는 말인가?\n그리 걱정할 일은 아니지 않나.')
        stage.unit(22).setAction(6)
        stage.say('&22\n주군, 주유를 얕보시면 안 됩니다.\n공명도 함께 있을지 모릅니다.')
        stage.unit(22).setAction(0)
        stage.say('&22\n주군, 이상하지 않으십니까?\n황개가 정한 날에 마침 풍향이 바뀌다니\n너무 공교롭지 않습니까.\n&0\n황개의 투항이\n동오의 간계라는 말인가?')
        stage.say('&22\n틀림없이 그렇습니다.')
        stage.say('&0\n하지만 봉효,\n황개가 주유에게 곤장을 맞은 것은\n틀림없는 사실이 아닌가.')
        stage.delay(3)
        stage.unit(22).setAction(0, 1)
        stage.say('&22\n틀림없이 주유의 고육계입니다.')
        stage.unit(22).setAction(0, 2)
        stage.say('&22\n그리고…배의 앞뒤를 쇠사슬로 잇는 것은\n대체 누구의 생각이었습니까?\n&0\n며칠 전 방통이 찾아와\n병사들이 병드는 것을 막으려면 그렇게 하라고……\n&22\n방통이라고요!?\n방통이 이곳에 왔습니까?\n……그렇군요.')
        stage.unit(22).setAction(6)
        stage.say('&22\n주군, 방통도 동오의 첩자인 듯합니다.')
        stage.unit(22).setAction(0)
        stage.say('&22\n모든 배를 쇠사슬로 묶은 것은\n분명 화공으로\n우리 함선을 모조리 태우려는 계략입니다.')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n아니! 방통이 이럴 수가……')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('&0\n봉효, 어찌하면 좋겠는가?')
        stage.unit(22).setAction(6)
        stage.say('&22\n배 몇 척을 준비해 적이 불을 지르게 유도하십시오.\n&22\n나머지 배에는 방화 대책을 마련해 두고\n동오군이 계획에 성공했다고 믿게 하는 것입니다.')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n쇠사슬은 풀지 않는가?')
        stage.unit(1).setAction(0)
        stage.delay(3)
        stage.unit(22).setAction(0, 3)
        stage.say('&22\n일부는 풀어도 되지만\n모두 풀어 버리면\n동오군이 눈치챌 것입니다.')
        stage.unit(22).setAction(0, 2)
        stage.say('&22\n주군, 황개는 반드시 불을 지르러 올 것입니다.\n시간을 잘 맞추어야 합니다.')
        stage.unit(0).setAction(17)
        stage.say('&0\n좋다, 봉효. 즉시 준비하게!')
        stage.unit(22).setAction(6)
        stage.say('&22\n예.\n&0\n다른 이들은 출발을 준비하라!')
        stage.unit(0).setAction(0)
        stage.unit(22).setAction(0)
        stage.setMenuVisible(True)
        stage.setJoinBattle(1, 15, [
            2,
            21,
            22])
        gvars[4076] = 0
        gvars[4057] = 27
        stage.setBattlePos([
            [
                16,
                21],
            [
                8,
                20],
            [
                15,
                21],
            [
                16,
                20],
            [
                25,
                22],
            [
                25,
                23],
            [
                17,
                8],
            [
                18,
                8],
            [
                7,
                10],
            [
                10,
                13],
            [
                27,
                15],
            [
                25,
                5],
            [
                9,
                19],
            [
                24,
                21],
            [
                23,
                20]])
    if not vars[724]:
        stage.jumpScene(55)
    return False


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n동오와 결전을 치르기 전에 곽가 선생이 와 주어 다행이군.\n이제 마음 놓아도 되겠네, 맹덕.\n&0\n음.')
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 전장의 지형은 어떠한가?\n&20\n예. 전장은 거의 전부 물로 이루어져 있습니다.\n&0\n으음, 우리 군은 수전에 익숙지 않은데 아쉽군.')
    if stage.unitClickTest(15):
        stage.say('&15\n물에서 싸운다고? 하아!\n&17\n허저 장군, 왜 그러세요?\n어째서 한숨을 쉬시나요?\n&15\n예? 아……아무것도 아닙니다……!\n&17\n설마 장군께서는 헤엄을 못 치시는……\n&15\n아……아닙니다!!')
    if stage.unitClickTest(17):
        stage.say('&17\n이상하네요. 어찌 풍향이 바뀌었을까요?\n&17\n이 계절에는 북서풍만 불어야 하는데……')
    if stage.unitClickTest(22):
        stage.say('&22\n주군, 적의 간계를 간파했지만\n여전히 힘겨운 싸움입니다.\n&0\n음, 주유는 참으로 두려운 후생이군.')
    if stage.unitClickTest(21):
        stage.say('&21\n강 건너편에 유비군의 깃발은 보이지 않습니다.\n유비와 공명은 강 건너 불구경할 생각인 듯합니다.')
    if stage.unitClickTest(23):
        stage.say('&23\n이 정도 계책조차 간파하지 못하다니\n참으로 면목이 없습니다.\n&22\n음, 자책할 것 없소.\n나는 마침 밖에서 돌아와\n일을 객관적으로 볼 수 있었을 뿐이오.')
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
