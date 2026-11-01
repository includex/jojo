# Source Generated with Decompyle++
# File: R_18.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 18)
    stage.showUnits([
        [
            101,
            35,
            56,
            1],
        [
            106,
            50,
            45,
            2],
        [
            103,
            58,
            45,
            2],
        [
            32,
            50,
            67]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('작별')
    stage.setStageName('연진  원소군 본진')
    stage.say('&101\n으음.\n안량에 이어\n문추마저 죽을 줄이야…….')
    stage.unit(103).setAction(6)
    stage.say('&103\n주군, 전에 허도로 보낸 첩자가\n방금 보고를 보내 왔습니다.')
    stage.unit(103).setAction(0)
    stage.say('&103\n역시 관우가 조조군에 있습니다.\n&103\n전장에서 본 붉은 얼굴에 긴 수염의 적장은\n분명 관우입니다.\n&101\n뭐라고?\n&32\n어……어찌……?\n둘째 아우가 조조의 부하가 되었다고?\n&103\n그렇습니다.\n&32\n설령 사실이라 해도\n둘째는 내가 여기 있는 줄 몰라서\n조조군에 들어갔을 것이오.')
    stage.unit(32).setAction(3)
    stage.say('&32\n둘째가 내가 여기 있음을 알게 되면\n밤낮없이 달려올 것이오.')
    stage.unit(32).setAction(0)
    stage.say('&101\n음…….\n그렇다면 유비 공,\n관우를 내 진영으로 불러 주시오.')
    stage.unit(32).setAction(6)
    stage.say('&32\n예, 즉시 처리하겠습니다.\n&101\n음…그리하시오.\n반드시 관우가 우리 군에 들어오도록 해야 하오.\n&32\n예, 즉시 처리하겠습니다.')
    stage.unit(32).setAction(0)
    stage.delay(3)
    stage.unit(32).move(95, 67, 1)
    stage.unit(32).hide()
    stage.delay(3)
    stage.say('&103\n쯧! 새옹지마라 하였으니 복이 될지 누가 알겠는가.')
    stage.unit(103).setAction(6)
    stage.say('&103\n주군, 이제 어찌해야 하겠습니까?')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(106).setAction(6)
    stage.say('&106\n우리 군은 안량과 문추 두 장수를 잃어\n지금은 사기가 떨어지고 군심도 흩어져 있습니다.\n&106\n우선 물러나 전열을 정비한 뒤\n훗날 다시 일어서는 편이 좋겠습니다.')
    stage.unit(106).setAction(0)
    stage.say('&101\n음.')
    stage.unit(101).setAction(3)
    stage.say('&101\n좋다. 우선 군을 물리자.')
    stage.unit(101).setAction(0)
    stage.say('&101\n곽도.')
    stage.unit(103).setAction(6)
    stage.say('&103\n예.')
    stage.unit(101).setAction(0, 3)
    stage.say('&101\n유비를 철저히 감시하라.\n그놈은 믿을 수 없다.\n&103\n예.')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 1)
    stage.showUnit(32, 53, 74)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('업성@유비의 거처')
    stage.showUnit(40, 53, 21, 2)
    stage.say('&40\n주군, 부르셨습니까?\n&32\n오, 손건이군.\n어서 들어오게.')
    stage.unit(40).move(53, 59, 2)
    stage.delay(3)
    stage.unit(32).setAction(0, 0)
    stage.say('&32\n손건, 나를 따라 서주를 빠져나온 자는\n이제 자네뿐이군.')
    stage.unit(32).setAction(0, 2)
    stage.say('&32\n다른 이들은 모두 죽은 줄 알았는데.\n&32\n설마…….')
    stage.unit(32).setAction(0, 0)
    stage.say('&32\n둘째가 무사했다니.')
    stage.unit(40).move(53, 61, 2)
    stage.say('&40\n저……정말입니까!?\n&32\n그렇다! 틀림없다.\n&40\n참으로 다행입니다!\n관 장군은 지금 어디 계십니까?\n&32\n허도에 있는 듯하구나.\n&40\n뭐라고요! 그렇다면……\n관 장군께서 조조에게 항복하셨단 말입니까?')
    stage.unit(32).setAction(2)
    stage.say('&32\n아……그럴 리가!')
    stage.unit(32).setAction(0)
    stage.say('&32\n그럴 리가 없소…….')
    stage.unit(32).move(44, 74, 3)
    stage.delay(1)
    stage.unit(32).move(44, 61, 0)
    stage.unit(40).setAction(0, 3)
    stage.delay(3)
    stage.unit(32).setAction(0, 1)
    stage.say('&32\n……둘째 아우에게 편지를 썼소.\n&32\n편지를 보면\n반드시 나를 찾아올 것이오.')
    stage.unit(32).setAction(0, 2)
    stage.say('&32\n서신 없이 곧장 찾아가려 했지만\n원소의 감시가 너무도 엄하오……\n&40\n그렇군요.\n알겠습니다.\n저를 보내 주십시오.')
    stage.unit(32).setAction(0, 1)
    stage.say('&32\n조조는 허도에서도 경계를 엄히 하니\n발각될 수도 있네.\n&40\n서주성이 함락되었을 때\n그 큰 난리에서도 살아남았습니다.\n염려하지 마십시오.\n&32\n그렇다면 다행이네…….')
    stage.unit(32).move(50, 61, 1)
    stage.say('&32\n그럼 모두 그대에게 맡기겠네.')
    stage.unit(32).move(44, 61, 1)
    stage.say('&32\n부디 조심하게.')
    stage.delay(3)
    stage.say('&40\n주군.\n&32\n무슨 일인가?\n&40\n간옹과 삼장군도 반드시 살아 계실 것입니다.\n&32\n음.')
    stage.unit(40).move(53, 21, 0)
    stage.unit(40).hide()
    stage.delay(3)
    stage.say('&32\n(그래, 모두 반드시 살아 있을 것이다.)')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(0, 35)
    stage.draw()
    stage.setStageName('허도')
    stage.info('원소의 공세를 두 차례 물리친 조조군은\n허도로 돌아왔고, 장수들은 휴양의 나날을 보냈다.')
    stage.bgSound(-1)
    stage.loadBg(2, 0)
    stage.showUnit(3, 40, 54, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도   관우 저택')
    stage.say('&40\n관 장군, 관 장군.\n&3\n응? 누구시오?')
    stage.showUnit(40, 90, 54, 3)
    stage.say('&40\n……관 장군.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(3)
    stage.say('&3\n……손건 선생이 아니시오!?\n무사하셨구려!')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(3),
            40,
            60,
            2],
        [
            stage.unit(40),
            80,
            54,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(3),
            52,
            60,
            1],
        [
            stage.unit(40),
            70,
            54,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(3),
            52,
            54,
            0],
        [
            stage.unit(40),
            60,
            54,
            3]])
    stage.delay(1)
    stage.unit(3).setAction(0, 1)
    stage.delay(3)
    stage.say('&40\n관 장군께서도…….\n&3\n손건 선생, 형님은 무사하시오?\n&40\n예. 주군께서는 원소에게 계십니다.\n&3\n그렇소……?\n형님께서 무사하시다니……!\n&40\n장군께 보내신 주군의 서찰을 가져왔습니다.')
    stage.say('&3\n오, 어서 보여 주시오.')
    stage.unit(3).move(56, 54, 1)
    stage.delay(5)
    stage.unit(40).setAction(5)
    stage.delay(5)
    stage.unit(40).setAction(0)
    stage.delay(5)
    stage.unit(3).move(52, 54, 3)
    stage.delay(3)
    stage.unit(3).setAction(5)
    stage.delay(10)
    stage.unit(3).setAction(0)
    stage.say('&3\n……형님께서도 이토록 나를 생각하고 계셨구나!')
    stage.delay(3)
    stage.unit(3).setAction(0, 1)
    stage.say('&3\n좋다. 곧 형수님들을 모시고\n주군을 찾으러 떠나겠다.\n&40\n관 장군, 한 가지 더 있습니다.\n&40\n셋째 장군과 간옹도 모두 무사하십니다.')
    stage.unit(3).setAction(3)
    stage.say('&3\n뭐라고!')
    stage.unit(3).setAction(0)
    stage.say('&40\n허도로 오는 길에\n우연히 간옹을 만났습니다.\n&40\n셋째 장군과 간옹은 지금 양산에 있습니다.\n&40\n그곳 산적들과 함께\n거점을 마련하고\n주군을 맞이할 준비를 하고 있습니다.')
    stage.unit(3).setAction(3)
    stage.say('&3\n그런가. 셋째와 간옹 모두 무사하군!')
    stage.unit(3).setAction(0)
    stage.say('&40\n저와 간옹이 주군을 양산으로 모시겠습니다.\n관 장군께서도 서둘러 양산으로 와 주십시오.\n&3\n음, 알겠소.\n두 분 형수님께서 무사하시다고 형님께 전해 주시오.\n&40\n주군께서 아시면 무척 기뻐하실 겁니다.\n&40\n그럼 이만 물러가겠습니다.')
    stage.unit(40).move(98, 54, 1)
    stage.unit(40).hide()
    stage.say('&3\n형님과 셋째는 살아 있다…….\n우리는 반드시 다시 만날 것이다.')
    stage.delay(5)
    stage.say('&2\n미안하오. 방금 한 말은 모두 들었소.\n&3\n음!?')
    stage.showUnit(2, 90, 54, 3)
    stage.say('&2\n나요.\n&3\n문원이었구려…….')
    stage.unit(2).move(60, 54, 3)
    stage.say('&3\n……숨길 생각은 없네.\n들은 그대로일세.\n&2\n……유현덕을 찾아 떠나려는가?\n&3\n음.\n내일 승상께 작별을 고할 것이네.')
    stage.delay(5)
    stage.say('&2\n그렇군요…….')
    stage.unit(2).setAction(0, 1)
    stage.say('&2\n……오늘 일은 주군께 아뢰어야 합니다.\n괜찮으시겠습니까?\n&3\n음, 숨김없이 말하게!')
    stage.unit(2).move(90, 54, 1)
    stage.say('&2\n실례하겠습니다.')
    stage.unit(2).move(98, 54, 1)
    stage.unit(2).hide()
    stage.effectSound(202, 255)
    stage.clsUnit()
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
            2,
            49,
            55,
            3],
        [
            21,
            52,
            40,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('그날 밤…….', 3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n그런가…?\n역시 유비는 살아 있었군.\n&2\n관우가 내일 작별 인사를 드리러 온다더군.')
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n솔직히 말해 만나고 싶지 않네.\n만나면 헤어져야 할 테니……. ')
    stage.unit(21).setAction(6)
    stage.say('&21\n주군, 관우의 일로 한 가지 계책이 있습니다.')
    stage.unit(2).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n무슨 생각인가, 중덕?\n&21\n주군께서 관우를 보내기 아쉬우시다면\n손님을 만나지 않는다는 패를 내걸면 됩니다.\n&0\n면회를 사절하는 패라고?')
    stage.info('면회 사절패란 주인이 손님을 만나고 싶지 않을 때\n닫힌 문 밖에 걸어 두는 패입니다.')
    stage.info('면회 사절패가 걸리면 무슨 일이 있더라도\n손님은 돌아가야 합니다. 그것이 예의이지요.')
    stage.say('&21\n관우는 예를 중히 여기니\n손님을 받지 않는다는 패를 보면\n함부로 들어오지는 않을 것입니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(2).setAction(0, 3)
    stage.say('&2\n허나 그것만으로는………\n관우의 마음을 붙잡기 어려울 것입니다…….\n&0\n그렇기도 하군…….')
    stage.effectSound(202, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('손님을 받지 않는다는 팻말을 건다\n팻말을 걸지 않는다', 0)
    if sel == 1:
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(5)
        stage.unit(0).setAction(3)
        stage.say('&0\n무슨 수를 써서라도 운장을 붙잡아야 한다.')
        stage.unit(0).setAction(0)
        stage.say('&0\n손님을 받지 않겠다고 알리거라.')
        stage.unit(21).setAction(6)
        stage.say('&21\n예.')
        vars[306] = 1
    elif sel == 2:
        stage.addAmbition(-10)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(5)
        stage.unit(0).setAction(0, 3)
        stage.say('&0\n……이제 손님을 물리칠 필요는 없겠군.')
        stage.unit(21).setAction(6)
        stage.say('&21\n하지만…….')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n정욱, 내 말을 들어 보게…….')
        stage.unit(0).setAction(3, 1)
        stage.say('&0\n운장과의 우의를 지키고 싶네.')
        stage.unit(0).setAction(0, 1)
        stage.say('&21\n……실언을 용서해 주십시오.')
        stage.unit(21).setAction(0)
        stage.say('&0\n내일은 평생 잊지 못할 날이 되겠군.')
        vars[306] = 0
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene4():
    if vars[306]:
        stage.loadBg(2, 29)
        stage.draw()
        stage.effectSound(205, 0)
        stage.showUnit(3, 61, 21, 2)
        stage.info('다음 날…….', 3)
        stage.setStageName('허도  승상부 앞')
        stage.delay(3)
        stage.unit(3).move(61, 65, 2)
        stage.delay(3)
        stage.unit(3).setAction(0, 3)
        stage.say('&3\n음?\n&3\n손님을 받지 않는다는 패가 걸렸군…….\n&3\n어쩔 수 없다. 내일 다시 오자.')
        stage.unit(3).move(61, 21, 0)
        stage.unit(3).hide()
        stage.effectSound(205, 255)
        stage.bgSound(16)
        stage.delay(3)
        stage.info('그 뒤 관우는 매일 승상부를 찾아갔으나\n승상부는 날마다 문이 굳게 닫힌 채\n손님을 받지 않는다는 팻말이 걸려 있었다.')
        stage.info('7일 뒤……', 3)
        stage.bgSound(-1)
        stage.effectSound(202, 0)
        stage.delay(3)
        stage.showUnit(3, 61, 21, 2)
        stage.unit(3).move(61, 65, 2)
        stage.delay(3)
        stage.unit(3).setAction(0, 3)
        stage.say('&3\n오늘도 손님을 받지 않는다는 패가 걸려 있군…….')
        stage.say('&3\n승상, 작별 인사를 드리려 했으나\n이래서는 언제쯤\n뵐 수 있을지 모르겠습니다.\n&3\n이 자리에서나마 작별을 고하겠습니다.')
        stage.unit(3).setAction(6)
        stage.say('&3\n승상, 내일 즉시 길을 떠나겠소.\n그동안 베풀어 주신 큰 은혜는\n평생 가슴에 새기겠소.')
        stage.effectSound(202, 255)
        stage.clsUnit()
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
                15,
                56,
                70],
            [
                2,
                62,
                70],
            [
                13,
                68,
                70],
            [
                20,
                52,
                40,
                2],
            [
                22,
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
                181,
                48,
                55,
                3,
                11]])
        stage.draw()
        stage.effectSound(205, 0)
        stage.delay(3)
        stage.info('다음 날 아침…….', 3)
        stage.setStageName('허도  승상부')
        stage.delay(3)
        stage.unit(0).setAction(3)
        model.setFace(1)
        stage.say('&0\n뭐라고? 운장이 떠났다고!\n&181\n그렇습니다.\n&181\n저택 안팎은 말끔히 정돈해 두었으며\n승상께서 주신 물건은 하나도 손대지 않았습니다.')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('&0\n그런가……?\n운장은 올 때도 거침없더니\n떠날 때도 거침없구나.')
        stage.unit(22).setAction(6)
        stage.say('&22\n주군, 관우를 이대로 보내서는 안 됩니다.')
        stage.unit(22).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('&21\n그렇습니다. 관우는 우리 군의 내정을 알고 있습니다.\n이대로 유비에게 보내면…….')
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n바로 그 말일세.\n맹덕, 당장 추격군을 보내세.')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n안 된다! 처음부터 운장에게 약속한 일이다.')
        stage.unit(1).setAction(0)
        stage.say('&1\n하지만 맹덕…….')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.say('&0\n게다가 운장이 멋대로 떠난 것도 아니다.\n내가 만나 주지 않았을 뿐이지.')
        stage.unit(0).setAction(0, 3)
        stage.say('&0\n이럴 줄 알았으면 손님을 막지 말고\n차분히 배웅하는 편이 나았겠군.')
        stage.unit(0).setAction(0, 2)
        stage.delay(3)
        stage.say('&0\n잠깐, 지금 뒤쫓으면 아직 늦지 않았겠지…….')
        stage.unit(0).setAction(0, 1)
        stage.delay(3)
        stage.unit(0).setAction(3, 1)
        stage.say('&0\n좋다! 문원, 금은을 준비하게!')
        stage.unit(2).setAction(0, 3)
        stage.say('&2\n예?')
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n운장을 배웅하러 가겠다.')
        stage.unit(2).setAction(6)
        stage.say('&2\n예, 알겠습니다.')
        stage.unit(2).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(3, 1)
        stage.say('&0\n좋다. 즉시 운장을 뒤쫓아라!')
        stage.effectSound(205, 255)
        stage.bgSound(19)
        stage.delay(3)
        stage.clsUnit()
        stage.loadBg(2, 34)
        stage.showUnits([
            [
                154,
                4,
                59,
                1],
            [
                155,
                4,
                49,
                1]])
        stage.draw()
        stage.setStageName('허도 교외  패릉교')
        stage.unitsMove([
            [
                stage.unit(154),
                14,
                59,
                1],
            [
                stage.unit(155),
                14,
                49,
                1]])
        stage.showUnit(3, 4, 54, 1)
        stage.unitsMove([
            [
                stage.unit(154),
                80,
                59,
                1],
            [
                stage.unit(155),
                80,
                49,
                1],
            [
                stage.unit(3),
                70,
                54,
                1]])
        stage.info('이봐!', 3)
        stage.info('이봐! 관우!', 3)
        stage.unit(3).setAction(0, 3)
        stage.say('&3\n응?')
        stage.unit(154).setAction(0, 3)
        stage.say('&154\n과……관 장군!')
        stage.unit(155).setAction(0, 3)
        stage.say('&155\n둘째 숙부님, 조조의 추격군입니까?\n&3\n추격군은 아닌 듯하구나…….')
        stage.unit(3).setAction(0, 1)
        stage.say('&3\n염려 마십시오. 추격군이라 해도\n제가 처리할 수 있습니다.')
        stage.unit(3).setAction(6)
        stage.say('&3\n두 분 형수님께서는 먼저 가십시오.\n&154\n장군도 부디 조심하세요.\n&3\n예.')
        stage.unit(3).setAction(0)
        stage.unitsMove([
            [
                stage.unit(154),
                95,
                59,
                1],
            [
                stage.unit(155),
                95,
                49,
                1]])
        stage.unit(154).hide()
        stage.unit(155).hide()
        stage.unit(3).setAction(0, 3)
        stage.showUnit(0, 4, 54, 1)
        stage.unit(0).move(14, 54, 1)
        stage.say('&0\n운장!')
        stage.showUnits([
            [
                2,
                4,
                59,
                1],
            [
                15,
                4,
                49,
                1]])
        stage.unitsMove([
            [
                stage.unit(0),
                20,
                54,
                1],
            [
                stage.unit(2),
                10,
                59,
                1],
            [
                stage.unit(15),
                10,
                49,
                1]])
        stage.showUnits([
            [
                13,
                4,
                59,
                1],
            [
                14,
                4,
                49,
                1]])
        stage.unitsMove([
            [
                stage.unit(0),
                60,
                54,
                1],
            [
                stage.unit(2),
                50,
                59,
                1],
            [
                stage.unit(15),
                50,
                49,
                1],
            [
                stage.unit(13),
                44,
                59,
                1],
            [
                stage.unit(14),
                44,
                49,
                1]])
        stage.delay(3)
        stage.unit(0).setAction(3)
        stage.say('&0\n아, 겨우 제때 도착했군.')
        stage.unit(0).setAction(0)
        stage.say('&0\n운장, 유현덕을 찾아 떠나는가……?\n&3\n그렇습니다.\n형님의 행방을 알게 되었습니다…….')
        stage.unit(3).setAction(6)
        stage.say('&3\n먼저 약속한 세 가지 조건을 지켜 주십시오.\n&0\n알고 있네.\n약속을 저버리지 않겠네.')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n내가 잠시 어리석어 손님을 받지 않는다는 팻말을 걸었네.\n이해해 주게.')
        stage.unit(0).setAction(0, 1)
        model.setFace(3)
        stage.say('&0\n운장, 좋은 인연으로 만나 좋게 헤어지고 싶어\n배웅하러 달려왔네.')
        stage.unit(0).move(64, 54, 1)
        model.setFace(0)
        stage.delay(3)
        stage.unit(0).setAction(5)
        stage.say('&0\n작은 정성이니 받아 주게.\n&3\n아닙니다. 받을 수 없습니다…….\n&0\n내 성의이니 부디 받아 주게.\n&3\n하지만…….')
        model.setFace(3)
        stage.say('&0\n길을 가자면 돈도 필요하지 않겠는가.\n두 부인께 노숙을 시킬 수는 없는 일이지.\n부인들을 위해서라도 받아 두게.\n&3\n두 형수님을 위해서라……?')
        stage.delay(3)
        stage.say('&3\n알겠소.')
        stage.unit(3).move(67, 54, 3)
        stage.delay(5)
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(3).move(70, 54, 1)
        stage.delay(3)
        stage.unit(3).setAction(0, 3)
        stage.delay(3)
        stage.unit(3).setAction(6)
        stage.say('&3\n승상, 참으로 감사하오.')
        stage.unit(3).setAction(0)
        stage.delay(3)
        stage.unit(0).move(60, 54, 3)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n운장, 부디 몸조심하게.')
        stage.unit(3).setAction(6)
        stage.say('&3\n승상께서도 부디 몸조심하시오…….')
        stage.unit(3).setAction(0)
        stage.unit(3).move(95, 54, 1)
        stage.unit(3).hide()
        stage.delay(8)
        model.setFace(0)
        stage.say('&0\n유현덕, 그대가 참으로 부럽구려.')
        stage.bgSound(-1)
        stage.clsUnit()
    if not vars[306]:
        stage.bgSound(19)
        stage.delay(3)
        stage.loadBg(2, 8)
        stage.showUnits([
            [
                0,
                34,
                55,
                1],
            [
                3,
                48,
                55,
                3,
                6],
            [
                154,
                54,
                52,
                3],
            [
                155,
                54,
                58,
                3],
            [
                20,
                52,
                40,
                2],
            [
                22,
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
                13,
                70,
                70]])
        stage.draw()
        stage.info('다음 날…….', 3)
        stage.setStageName('허도  승상부')
        stage.say('&0\n그런가? 마침내 유현덕을 찾아 떠나는군.\n&3\n그렇습니다.\n그동안 승상께 많은 보살핌을 받았습니다.')
        stage.unit(3).setAction(0)
        stage.say('&0\n음.\n&0\n문원.\n&2\n예.')
        stage.unit(2).move(62, 70, 3)
        stage.delay(1)
        stage.unit(2).move(62, 55, 0)
        stage.delay(1)
        stage.unit(2).setAction(0, 3)
        stage.unit(3).setAction(0, 1)
        stage.unit(154).setAction(0, 2)
        stage.unit(155).setAction(0, 0)
        stage.say('&0\n운장, 떠나는 길에 작은 정성을 마련했네.\n받아 주게.')
        stage.unit(3).setAction(0, 3)
        stage.say('&3\n안 됩니다! 받을 수 없습니다……\n&0\n내 성의이니 반드시 받아 주게.\n&3\n하지만…….')
        model.setFace(3)
        stage.say('&0\n가는 길에도 돈은 필요하지 않겠는가.\n두 부인을 길에서 노숙하게 할 수는 없지.\n부인들을 위해서라도 받아 두게.\n&3\n두 분 형수님을 위해서라……\n알겠소.')
        stage.unit(3).setAction(0, 1)
        stage.delay(3)
        stage.unit(2).move(51, 55, 3)
        stage.delay(5)
        stage.unit(2).move(62, 55, 3)
        stage.delay(3)
        stage.unit(3).setAction(0, 3)
        stage.delay(3)
        stage.unit(3).setAction(6)
        stage.say('&3\n승상, 참으로 감사하오.')
        stage.unit(154).setAction(0, 3)
        stage.say('&154\n감사합니다, 조 승상.')
        stage.unit(155).setAction(0, 3)
        stage.say('&155\n참으로 감사드립니다.\n&0\n운장, 부디 가는 길에 몸조심하게……\n&3\n조 승상께서도 평안하십시오…….')
        model.setFace(0)
        stage.bgSound(-1)
        stage.clsUnit()
    return False


def scene5():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(3, 291, 92)
    stage.showHead(0, 355, 92)
    stage.bottomTxt('관우와 두 부인은', False, False, False)
    stage.delay(5)
    stage.head(3).move(291, 172)
    stage.head(3).move(302, 179)
    stage.delay(5)
    stage.bottomTxt('양산으로 향하여', True, False, False)
    stage.delay(5)
    stage.showHead(32, 366, 179)
    stage.delay(5)
    stage.bottomTxt('마침내 유비, 장비와 다시 만났다.', True, False, True)
    stage.bottomTxt('이때,', False, True, False)
    stage.delay(8)
    stage.showHead(100, 560, 213)
    stage.delay(5)
    stage.bottomTxt('강동의 소패왕 손책이 급병으로 세상을 떠났다.', True, False, False)
    stage.delay(5)
    stage.head(100).hide()
    stage.delay(10)
    stage.showHead(76, 560, 213)
    stage.delay(5)
    stage.bottomTxt('아우 손권이 대업을 이어받았다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('손권은 사신을 보내 조조에게 승계 사실을 알렸다.', True, True, True)
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
            13,
            70,
            70],
        [
            20,
            52,
            40,
            2],
        [
            22,
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
            97,
            48,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n그런가? 손책이 죽다니……\n&97\n예.\n손책 장군께서는 급병으로 갑자기 세상을 떠나셨습니다.\n&0\n음…….')
    stage.delay(3)
    stage.say('&0\n이제는 동생 손권이 대업을 이었군……\n&0\n손권은 문무를 겸비했다더군.')
    stage.unit(0).setAction(3)
    stage.say('&0\n이런 인물이 군주의 자리를 이었으니\n동오의 나라와 백성은 평안하겠구려.\n&97\n감사합니다, 승상.')
    stage.unit(0).setAction(0)
    stage.say('&0\n음.\n훗날 사람을 강동으로 보내 조문하겠소.\n&0\n먼 길 오느라 수고하셨소.\n거처를 마련해 두었으니\n일찍 쉬도록 하시오.\n&97\n승상의 호의에 감사드립니다.\n그럼 소인은 이만 물러가겠습니다.\n&0\n음.')
    stage.unit(97).setAction(0)
    stage.delay(3)
    stage.unit(97).move(95, 55, 1)
    stage.unit(97).hide()
    stage.say('&0\n손책이 죽었군……\n본래라면 동오를 공격할 절호의 기회인데.')
    stage.unit(22).setAction(6)
    stage.say('&22\n주군, 이것은 아마…….')
    stage.unit(22).setAction(0)
    stage.delay(3)
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n하하하, 알고 있네. 원소를 처리하기 전에는\n동오를 공격하지 않을 것이야.\n내가 그 정도로 어리석지는 않네.')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&0\n문약, 즉시 동오로 사자를 보내 조문하게.\n&0\n동오와의 우호 관계는 반드시 유지해야 하네.')
    stage.unit(20).setAction(6)
    stage.say('&20\n예! 알겠습니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n손권과 우호를 유지하면\n남방은 걱정할 필요가 없겠지.')
    stage.unit(0).setAction(3)
    stage.say('&0\n원소와 자웅을 겨룰 때가 왔다.\n출병하라!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n목표는 원소와의 접경에 있는 황하의 요충지 관도다!')
    stage.effectSound(202, 255)
    stage.bgSound(0)
    stage.delay(3)
    stage.clsUnit()
    return False


def scene6():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            101,
            52,
            28,
            2],
        [
            106,
            38,
            48,
            1],
        [
            102,
            38,
            54,
            1],
        [
            110,
            67,
            48,
            3],
        [
            105,
            67,
            54,
            3]])
    stage.draw()
    stage.info('조조군의 움직임은 곧 원소의 진영에 전해졌다.')
    stage.setStageName('업성 원소 저택')
    stage.delay(3)
    stage.unit(101).setAction(3)
    stage.say('&101\n방금 보고를 받았다.\n조조가 손권과 동맹을 맺고\n곧 관도로 진군한다는군.')
    stage.unit(101).setAction(0)
    stage.say('&101\n동오가 조조군과 힘을 합치면\n우리가 조조를 없애기는 어려워진다.')
    stage.unit(101).setAction(17)
    stage.say('&101\n우리 군은 속히 관도로 출병하여\n조조와 결사전을 치러야겠군.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(110).setAction(2)
    stage.say('&110\n절대로 안 됩니다.')
    stage.unit(110).setAction(0)
    stage.say('&110\n주군, 지난 패배의 교훈을 잊으셨습니까?\n조조를 이기려면 결코 싸움을 서둘러서는 안 됩니다.\n&101\n전풍, 우리가 더 움직이지 않으면\n동오가 조조군과 함께 나설 것이네.')
    stage.unit(110).setAction(6)
    stage.say('&110\n주군, 조조와 동오의 관계는\n잠시 이어질 뿐입니다.\n머지않아 반드시 깨질 것입니다.')
    stage.unit(110).setAction(0)
    stage.say('&102\n전풍, 고작 그런 짐작으로\n군국의 대사를 가로막겠다는 것인가?')
    stage.unit(110).setAction(2)
    stage.say('&110\n무슨 말씀이십니까! 이것은 추측이 아니라\n자명한 이치입니다.')
    stage.unit(110).setAction(0)
    stage.say('&102\n그런 생각은 터무니없소.')
    stage.unit(102).setAction(0, 0)
    stage.say('&102\n주군, 지난 싸움에서도\n전풍은 줄곧 반대만 했습니다.\n&102\n그 때문에 장졸들이 투지를 잃어\n끝내 우리 군이 참패한 것입니다.\n&101\n음…….')
    stage.unit(102).setAction(0, 1)
    stage.delay(3)
    stage.unit(101).setAction(0, 0)
    stage.say('&101\n……전풍, 집에 가서 잘 반성하라.')
    stage.unit(101).setAction(0, 2)
    stage.say('&101\n앞으로 다시는 내 앞에 나타나지 마라.\n&110\n주……주군……!')
    stage.unit(110).setAction(6)
    stage.say('&110\n……알겠습니다.')
    stage.unit(110).setAction(0)
    stage.unit(110).move(75, 48, 1)
    stage.delay(1)
    stage.unit(110).move(75, 71, 2)
    stage.unit(110).setAction(0)
    stage.say('&110\n참으로 안타깝구나…….')
    stage.unit(110).move(75, 90, 2)
    stage.unit(110).hide()
    stage.delay(3)
    stage.unit(105).setAction(6)
    stage.say('&105\n주군, 저도 전풍의 생각에 동의합니다.\n섣불리 진군하지 마십시오.')
    stage.unit(105).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('&101\n뭐라고?')
    stage.unit(101).setAction(3)
    stage.say('&101\n저수, 그대마저 반대하는가?\n병력은 우리가 압도적으로 우세하니\n반드시 이길 것이다.')
    stage.unit(105).setAction(6)
    stage.say('&105\n싸움의 승패는 병력에만 달린 것이 아닙니다.\n주군, 부디 다시 생각해 주십시오…….')
    stage.unit(105).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0)
    stage.say('&101\n저수, 그렇다면 병력 이야기는 그만두자.\n총사령관인 내가 조조만 못하다는 말이냐!?')
    stage.unit(105).setAction(6)
    stage.say('&105\n주……주군, 그런 뜻이 아니옵니다…….')
    stage.unit(101).setAction(2)
    stage.say('&101\n닥쳐라! 변명은 듣기 싫다!\n저자를 감옥에 가두어라!!')
    stage.showUnits([
        [
            181,
            67,
            90],
        [
            182,
            73,
            90]])
    stage.unitsMove([
        [
            stage.unit(181),
            67,
            57,
            0],
        [
            stage.unit(182),
            73,
            51,
            0]])
    stage.unit(105).setAction(0)
    stage.delay(1)
    stage.unit(182).move(67, 51, 3)
    stage.delay(1)
    stage.unit(182).setAction(0, 2)
    stage.delay(3)
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('&101\n즉시 관도로 진군하라!')
    stage.say('&105\n주군…….')
    stage.delay(3)
    stage.unit(105).setAction(0, 1)
    stage.say('&105\n(주군께서 변하셨다…………\n예전에는 이렇지 않으셨는데……)\n&105\n(어젯밤 허도 쪽에서 요사한 별이 갑자기 나타났다.\n틀림없이 조조의 별일 것이다……\n조조가 주군을 미치게 한 것인가……?)')
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(101, 349, 4)
    stage.delay(10)
    stage.bottomTxt('하북의 영웅 원소는', False, False, False)
    stage.delay(5)
    stage.showHead(0, 317, 127)
    stage.delay(10)
    stage.bottomTxt('숙적 조조와,', True, False, False)
    stage.delay(10)
    stage.head(0).move(358, 114)
    stage.delay(5)
    stage.head(101).move(358, 34)
    stage.delay(10)
    stage.bottomTxt('두 영웅은 관도에서 대치했고,', True, True, False)
    stage.delay(10)
    stage.bottomTxt('마침내 결전의 날이 왔다.', True, True, True)
    return False


def scene7():
    model.initLocalVar()
    stage.clsUnit()
    stage.setMenuVisible(False)
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
            15,
            38,
            48,
            1],
        [
            2,
            38,
            42,
            1],
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
            24,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('관도  조조군 본진')
    stage.say('&0\n문약, 원소군의 상황을 설명해 주겠는가?')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n예.\n&20\n압도적인 병력을 지닌 원소의 대군이\n이미 황하를 건너 기세를 떨치고 있습니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n과연 원소답군…….')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(2)
    stage.say('&0\n하지만 나는 질 수 없다.\n&0\n모두 전력을 다해 싸워라.')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 14, [
        15])
    gvars[4076] = 0
    gvars[4057] = 18
    stage.setBattlePos([
        [
            15,
            30],
        [
            11,
            27],
        [
            18,
            27],
        [
            12,
            28],
        [
            17,
            28],
        [
            13,
            28],
        [
            16,
            28],
        [
            12,
            29],
        [
            17,
            29],
        [
            13,
            29],
        [
            16,
            29],
        [
            14,
            30],
        [
            15,
            29],
        [
            14,
            29],
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
    if stage.unitClickTest(24):
        stage.say('&0\n공달, 운장은 지금 어찌 지내는가?\n&24\n양산에서 유비 일행과 합류한 듯합니다.\n&24\n지금은 그곳에서 녹채를 쌓고 있습니다…….\n&0\n오?')
        stage.unit(22).setAction(0, 2)
        stage.say('&22\n주군, 운장 장군께서는 이미…….\n&0\n알고 있네.\n이제 그는 내 적일 뿐이다.\n&22\n………….')
        stage.unit(22).setAction(0, 3)
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 관도의 지형은 어떠한가?\n&20\n예.\n&20\n대부분 산과 숲으로 이루어져 있으며\n평지의 길은 매우 좁습니다.\n&20\n기병은 움직이기 어려울 듯하니……\n또 허저 장군께서 활약하실 기회로군요.\n&15\n제게 맡기십시오!!\n&20\n하하하.\n&20\n아, 중요한 일이 하나 더 있습니다…….\n&0\n무엇인가?\n&20\n주군, 우리 진영의 동북쪽에\n오소라는 분지가 있는데\n그곳에 원소의 군량고가 있습니다.\n&0\n뭐라고!? 군량고가!\n&20\n그렇습니다.')
    if stage.unitClickTest(22):
        stage.say('&22\n주군, 원소 휘하의 허유를 아십니까?\n&22\n주군과 동향이라 들었습니다.\n&0\n허유? ……기억이 나지 않는군.\n그런 사람이 있었던 것 같기도 하네.\n&22\n그러시군요.\n&22\n허유는 속이 좁아\n원소에게 줄곧 중용되지 못했고\n그 때문에 원한이 깊다 합니다.\n&0\n오? 흥미로운 이야기군.')
    if stage.unitClickTest(1):
        stage.say('&1\n대군을 거느렸다고 해도\n승패가 병력에만 달린 것은 아니지.')
    if stage.unitClickTest(15):
        stage.say('&15\n원소군에 대단히 강한 장수가 있다더군요.\n그와 한번 일기토를 벌이고 싶습니다.\n&0\n그 장수의 이름은 무엇인가?\n&15\n어디 보자, 장료 장군과 이름이 비슷했던 것 같습니다.\n&2\n장합입니다, 허저 장군.\n&15\n아! 그렇지요, 장합! 장합이었습니다!')
    if stage.unitClickTest(2):
        stage.say('&2\n병력은 문제가 아니다.\n자신의 무예를 믿어야 한다.')
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
