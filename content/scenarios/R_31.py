# Source Generated with Decompyle++
# File: R_31.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
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
            8,
            58,
            70],
        [
            15,
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
            159,
            52,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('서량의 금마초')
    stage.setStageName('허도  승상부')
    if vars[309]:
        stage.say('&0\n자건, 동작대는 완공되었느냐?\n&159\n예! 아버님께서 살펴보시기만 기다리고 있습니다.\n&0\n그러냐? 참 잘했구나.\n이번 일은 칭찬할 만하다!\n&159\n감사합니다, 아버님.\n참, 함께 업성으로 가시겠습니까?\n&0\n음, 업성이라……?\n순욱, 자네가 대신 가서 살펴보게.')
        stage.unit(20).setAction(6)
        stage.say('&20\n예.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(159).setAction(0)
        stage.say('&159\n예?\n아버님께서는 안 가십니까?')
        stage.unit(20).setAction(6)
        stage.say('&20\n공자께서는 제게 불만이 있으십니까?')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(159).setAction(0, 0)
        stage.say('&159\n아……아닙니다……\n소자는 그런 뜻이 아니라, 다만……')
        stage.unit(0).setAction(3)
        stage.say('&0\n문화, 각지의 제후들에게\n동작대가 완공되었다고 알리게.\n이번에는 최대한 성대하게 치러야겠네.')
        stage.unit(23).setAction(6)
        stage.say('&23\n예.\n알겠습니다.')
        stage.unit(23).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0)
        stage.say('&0\n자건, 천하가 통일된 뒤에\n동작대에 오르도록 하마.\n아직은 때가 아니다.')
        stage.unit(159).setAction(0, 3)
        stage.say('&159\n알겠습니다.')
        stage.unit(159).setAction(0, 0)
        stage.say('&159\n그럼 순욱 선생, 이쪽으로 오시지요.')
        stage.unit(20).setAction(6)
        stage.say('&20\n예.')
        stage.unit(20).setAction(0)
        stage.delay(3)
    if not vars[309]:
        stage.say('&0\n자건, 네가 동작대를 지었다고 들었다.\n공사에 든 비용은\n나라를 다스릴 자금에서 가져다 쓴 것이겠지?')
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('&0\n나라의 재물을 사사로운 일에 쓸 수 없다는 걸\n정녕 모르느냐!\n&159\n아버님, 노여움을 거두십시오.\n국고에는 손대지 않았습니다.\n이것은 모두 순욱 선생께서…….')
        stage.unit(20).setAction(6)
        stage.say('&20\n주군, 동작대 건설 비용은\n모두 신하들의 사재에서 나왔습니다.\n그 점은 안심하십시오.')
        stage.unit(20).setAction(0)
        stage.say('&20\n주군, 공자께는 잘못이 없습니다.\n모든 책임은 이 문약이 지겠습니다.')
        stage.delay(10)
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.say('&0\n음, 그렇다면 죄가 없다는 말이군?\n됐다. 물러가라!\n이번에는 묻지 않겠으나 다시는 이런 일이 없어야 한다.')
        stage.unit(20).setAction(6)
        stage.say('&20\n예, 감사합니다. 주군.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(23).setAction(6)
        stage.say('&23\n주군, 동작대는 어찌하시겠습니까?\n허물까요?')
        stage.unit(23).setAction(0)
        stage.say('&0\n이미 지어 놓았으니 어쩔 수 없지.\n그대로 두도록 하게.\n헌데 문득 떠오른 생각이 있네.')
        stage.unit(0).setAction(3)
        stage.say('&0\n문화, 각지의 제후들에게\n동작대가 완공되었다고 알리게.\n이번에는 최대한 성대하게 치러야겠네.')
        stage.unit(0).setAction(0)
        stage.say('&23\n예?')
        stage.unit(23).setAction(6)
        stage.say('&23\n그렇군요. 예, 알겠습니다.')
        stage.unit(23).setAction(0)
        stage.delay(3)
        stage.unit(159).setAction(0)
        stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(20),
            52,
            34,
            0],
        [
            stage.unit(23),
            58,
            34,
            0],
        [
            stage.unit(159),
            58,
            55,
            1]])
    stage.unitsMove([
        [
            stage.unit(20),
            80,
            34,
            1],
        [
            stage.unit(23),
            80,
            34,
            1],
        [
            stage.unit(159),
            98,
            55,
            1]])
    stage.unit(159).hide()
    stage.unit(20).hide()
    stage.unit(23).hide()
    stage.say('&0\n중덕, 유비가 형주를 차지했다더군.')
    stage.unit(21).setAction(6)
    stage.say('&21\n예.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n유비가 군사 공명을 얻더니\n물 만난 고기처럼 세력을 키우는군.\n공명도 참으로 대단한 자야!')
    stage.unit(21).setAction(0)
    stage.say('&21\n주군, 유비를 가벼이 여기시면 안 됩니다.\n그렇게 해서는 백해무익합니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n알고 있네.\n참, 그 뒤 주유는 어찌 되었는가?\n아직도 병석에 누워 있는가?')
    stage.unit(21).setAction(6)
    stage.say('&21\n아닙니다. 이미 군으로 돌아간 듯합니다.')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n좋다. 주유가 쾌유한 것을 축하하는 뜻으로\n형주 강하의 태수에 임명하겠다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0)
    stage.say('&21\n주유에게 관직을 내린다고요……? 아, 그렇군요.\n관직을 내려 주유가 공명을 상대하게 만드는 것이군요!')
    model.setFace(0)
    stage.say('&0\n옳은 말일세.\n즉시 조정에 상주하도록 하게.')
    stage.unit(21).setAction(6)
    stage.say('&21\n예!')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(21).move(95, 40, 1)
    stage.unit(21).hide()
    stage.say('&0\n주유와 공명……\n이 두 사람은 너무 위험하다.\n먼저 하나를 제거해야 한다.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 336, 98)
    stage.showHead(32, 274, 228)
    stage.showHead(76, 567, 217)
    stage.bottomTxt('조조는 조정에 뜻을 물었고', False, False, False)
    stage.delay(5)
    stage.showHead(77, 338, 228)
    stage.delay(5)
    stage.bottomTxt('주유를 강하 태수로 임명했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('유비와 손권의 형주 쟁탈전은 더욱 치열해졌다.', True, True, False)
    stage.delay(8)
    stage.bottomTxt('양측이 다툰 끝에', True, True, False)
    stage.delay(5)
    stage.head(77).hide()
    stage.delay(5)
    stage.bottomTxt('주유가 목숨을 잃자', True, False, False)
    stage.delay(5)
    stage.bottomTxt('유비는 형주를 완전히 장악했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            24,
            52,
            40,
            2],
        [
            21,
            58,
            40,
            2]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n공명의 뛰어난 지략 덕분에\n유비가 끝내 형주 쟁탈전에서 승리했습니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n주유도 그 무렵 피를 토하고 죽었습니다.')
    stage.unit(21).setAction(0)
    stage.say('&0\n그런가……\n역시 공명이 주유보다 한 수 위였군.')
    stage.effectSound(201, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('조금 뜻밖이다\n과연 예상대로다', 0)
    if sel == 1:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('&0\n조금은 뜻밖이로군.\n&20\n주군께서는 줄곧 주유가 더 강하리라 여기셨습니까?\n&0\n음,\n나도 잘못 볼 때가 있네.')
    elif sel == 2:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('&0\n음, 역시 내 생각대로군.\n가장 두려운 상대는 역시 공명이야.')
    stage.unit(24).setAction(6)
    stage.say('&24\n방통도 유비의 부군사로 나섰다고 합니다.')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('&0\n뭐라고? 방통!\n&24\n유비를 더는 내버려 둘 수 없습니다.\n당장 손을 써야 합니다.')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&0\n음.')
    stage.showUnit(181, 90, 55, 3)
    stage.say('&181\n아룁니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 서량의 마등이 뵙기를 청합니다.\n지금 밖에서 기다리고 있습니다.\n&0\n마등이라고?\n오, 들라 하라.\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).hide()
    stage.unit(21).setAction(0)
    stage.say('&21\n마등은 서량군의 총사령관인데\n어찌 도읍까지 왔을까요……')
    stage.showUnit(138, 90, 55, 3)
    stage.say('&138\n승상을 뵙습니다.')
    stage.unit(138).move(52, 55, 3)
    stage.delay(3)
    stage.unit(138).setAction(6)
    stage.say('&138\n마등이오.\n&0\n내가 조맹덕이오.\n마등 장군, 잘 오셨소.\n&138\n예.\n승상의 동작대가 완공되었다기에\n축하드리고자 찾아왔소.\n&0\n그러시오?\n장군의 마음에 감사하오.\n즉시 연회를 열어 여독을 풀어 드리겠소.\n&138\n감사하기 그지없소.\n다만 승상의 환대를 받기 전에\n폐하를 먼저 알현하고 싶소.')
    stage.unit(0).setAction(3)
    stage.say('&0\n아, 내가 미처 살피지 못했구려.\n즉시 장군을 위해 폐하께 아뢰겠소.')
    stage.unit(0).setAction(0)
    stage.say('&0\n때가 되면 장군을 맞이할 자리를 마련하겠습니다.\n잠시만 기다려 주십시오.\n&138\n알겠소.\n그럼 이만 물러가겠소.')
    stage.unit(138).setAction(0)
    stage.delay(3)
    stage.unit(138).move(95, 55, 1)
    stage.unit(138).hide()
    stage.unit(24).setAction(6)
    stage.say('&24\n주군, 마등에게 유비를 토벌하게 하면 어떻겠습니까?')
    stage.unit(24).setAction(0)
    stage.say('&0\n마등을 보내라고……?\n아니다. 아직 그를 믿을 수 없다.\n&0\n중덕, 마등의 알현을 준비하게.\n다만 사람을 붙여 그의 움직임을 살피도록 하게.')
    stage.unit(21).setAction(6)
    stage.say('&21\n예.')
    stage.unit(24).setAction(0)
    stage.say('&24\n마등을 감시하시려는 것입니까?\n&0\n동작대 완공을 축하하려고\n서량에서 허도까지 먼 길을 왔다고는 생각하지 않네.')
    stage.delay(3)
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(12)
    stage.delay(3)
    stage.loadBg(2, 15)
    stage.showUnits([
        [
            149,
            27,
            55,
            1],
        [
            138,
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
    stage.setStageName('허도  궁정')
    stage.delay(3)
    stage.unit(192).setAction(6)
    stage.say('&192\n폐하, 서량의 마등이 알현을 청하옵니다.')
    stage.unit(192).setAction(0)
    stage.say('&149\n오, 마 경!\n드디어 왔구려.\n짐이 오래 기다렸소.\n&138\n예, 폐하.')
    stage.unit(192).setAction(6)
    stage.say('&192\n마 장군, 허도로 오신 뒤로\n폐하께서는 한 번도 기뻐하신 적이 없습니다.')
    stage.unit(192).setAction(0)
    stage.say('&149\n유 황숙은 멀리 형주에 있어 오기 어렵고\n제후들에게는 충군애국의 마음이 없소.\n이제 명문 마씨 가문에 기댈 수밖에 없구려.')
    stage.unit(138).setAction(0)
    stage.say('&138\n폐하, 어찌 그런 말씀을 하십니까? 설마 조조가…….\n&149\n음, 처음에는 짐도 그를 믿었다.\n허나 여러 해 동안 그가 하는 일을 지켜보니\n더는 믿을 수 없구나.\n&149\n짐이 보기에 조조에게는 충군의 마음이 없다.')
    stage.unit(138).setAction(6)
    stage.say('&138\n신은 알겠습니다.\n마음을 굳혔습니다.')
    stage.unit(138).setAction(0)
    stage.delay(3)
    stage.unit(193).setAction(6)
    stage.say('&193\n마 장군께서는 무슨 생각이 있으십니까?')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(138).setAction(0, 0)
    stage.say('&138\n예.\n마등은 성의를 헤아려 진작 움직일 준비를 마쳤습니다.')
    stage.unit(192).setAction(3)
    stage.say('&192\n오!\n마 장군의 말씀은…….')
    stage.unit(138).setAction(6)
    stage.say('&138\n예.\n신은 조조를 제거하고 폐하를 구할 것을 결심했사옵니다.')
    stage.unit(138).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(0)
    stage.say('&192\n하…… 하지만 장군께서 정말 조조를 없앨 수 있겠소?\n실패하기라도 한다면………\n&138\n염려 마시오.\n조조는 아직 내 속내를 모르고 있소.')
    stage.unit(138).setAction(3)
    stage.say('&138\n서량을 지키는 부대만 불러들여\n안팎에서 함께 군사를 일으켜 협공하면\n조조 따위는 상대도 되지 못합니다.')
    stage.unit(138).setAction(0)
    stage.say('&193\n그렇군요.')
    stage.unit(192).setAction(0, 3)
    stage.delay(3)
    stage.unit(192).setAction(6)
    stage.say('&192\n폐……폐하! 마침내 소망이 이루어졌습니다……\n&149\n마등!\n짐을 조조의 손아귀에서 구해 주시오.')
    stage.unit(138).setAction(0, 3)
    stage.delay(3)
    stage.unit(138).setAction(6)
    stage.say('&138\n염려 마시옵소서, 폐하.\n&138\n그럼 신은 즉시 군영으로 돌아가\n거병할 준비를 하겠사옵니다.\n이만 물러가겠나이다.\n&149\n음, 그대만 믿겠네. 마 경…….')
    stage.unit(138).setAction(0)
    stage.unit(192).setAction(0)
    stage.delay(3)
    stage.unit(192).setAction(0, 2)
    stage.unit(138).move(95, 55, 1)
    stage.unit(138).hide()
    stage.say('&149\n마등이 정말 조조를 없앨 수 있을까…….\n만약 실패한다면 짐은………')
    stage.unit(192).setAction(6)
    stage.say('&192\n……폐하, 염려 마시옵소서.')
    stage.unit(192).setAction(0)
    stage.say('&193\n아, 그렇지요!\n마 장군께서는 조조군의 속사정을 아직 모르시는군요……')
    stage.unit(193).setAction(6)
    stage.say('&193\n폐하, 신이 즉시 마 장군께 가서\n조조군의 자세한 내정을 전하겠사옵니다.\n&149\n음, 그래. 어서 가거라.\n&193\n예.')
    stage.unit(193).setAction(0)
    stage.delay(3)
    stage.unit(193).move(76, 43, 1)
    stage.say('&193\n(마등이 역시 딴마음을 품고 있었군…….\n즉시 정욱 선생께 알려야 한다.)')
    stage.unit(193).move(90, 43, 1)
    stage.unit(193).hide()
    stage.bgSound(-1)
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
            2]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n그런가? 마등의 목적은 우리를 없애는 것이었군?')
    stage.unit(21).setAction(6)
    stage.say('&21\n예.\n또한 서량군을 불러들여\n안팎으로 협공하려 했습니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n주군, 즉시 움직여야 합니다.')
    stage.unit(24).setAction(0)
    stage.say('&0\n알고 있네.\n숨어 있는 정적들을 끌어내려고\n문화에게 동작대의 일을 널리 알리도록 한 것이야.')
    stage.unit(23).setAction(6)
    stage.say('&23\n마등 역시 예상대로\n주군의 함정에 빠졌군요.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n하지만 적이 안팎에서 협공하면\n상대하기가 몹시 어려워질 것입니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n즉시 움직여야 합니다.\n주군, 마등을 붙잡으십시오.')
    stage.unit(21).setAction(0)
    stage.say('&0\n음………')
    stage.effectSound(201, 255)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('마등의 목을 벤다\n마등을 감옥에 가둔다', 0)
    if sel == 1:
        vars[311] = 1
        stage.addAmbition(5)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        model.setFace(2)
        stage.unit(0).setAction(2)
        stage.say('&0\n중덕, 마등을 오문으로 끌고 가 참수하라.\n살려 둘 수 없다!\n폐하의 일은 당분간 묻지 않겠다.')
        model.setFace(0)
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('&21\n예.\n즉시 처리하겠습니다.')
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(21).move(95, 40, 1)
        stage.unit(21).hide()
        stage.say('&0\n문화, 자네는 장안에서 마등군을 물리쳤지.\n그의 군대를 어떻게 평가하는가?')
        stage.unit(23).setAction(6)
        stage.say('&23\n서량군의 전력은 얕볼 수 없습니다.\n특히 마등의 아들 마초는\n만인이 당할 수 없는 용맹을 지녔지요.\n&0\n마초의 이름은 나도 들었네.\n「서량의 금마초」라 불리며\n무예가 뛰어난 젊은 무장이라더군.\n&23\n그렇습니다. 대군을 움직여\n마초가 이끄는 서량군과 정면으로 싸우는 것은\n하책입니다. 지략으로 이기는 편이 좋겠습니다.\n&0\n좋다.\n전군 전투를 준비하라.\n마초라……! 참으로 기대되는군.')
    elif sel == 2:
        vars[311] = 0
        stage.addAmbition(-5)
        stage.bgSound(-1)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('&0\n정욱, 마등을 옥에 가두게.')
        stage.unit(21).setAction(6)
        stage.say('&21\n아……?')
        stage.delay(5)
        stage.unit(21).setAction(0)
        stage.delay(3)
        stage.unit(21).move(76, 40, 1)
        stage.say('&21\n(주군께서는 미처 알아차리지 못하신 듯하군.\n마등은 위험한 인물이다.\n뿌리째 뽑아 버려야 하거늘!)')
        stage.unit(21).move(95, 40, 1)
        stage.unit(21).hide()
        stage.info('하지만 정욱은 마등을 감옥에 가두지 않고\n독단으로 죽여 버렸다.\n이 사실을 안 조조는 깊이 후회하며\n예를 갖춰 마등의 시신을 서량으로 돌려보냈다.')
        stage.say('&0\n문화, 자네는 장안에서 마등군을 물리쳤지.\n그의 군대를 어떻게 평가하는가?')
        stage.unit(23).setAction(6)
        stage.say('&23\n서량군의 전력을 얕보아서는 안 됩니다.\n특히 마등의 아들 마초는\n만부부당의 용맹을 지녔습니다.\n&0\n마초라는 이름은 나도 들었네.\n사람들이 서량의 금마초라 부른다지.\n무예가 뛰어난 젊은 장수라던데.\n&23\n그렇습니다. 대군을 동원해\n마초를 앞세운 서량군과 정면으로 싸우는 것은\n하책이니 지모로 제압하는 편이 낫습니다.\n&0\n좋다.\n전군은 전투 준비를 갖추어라.\n마초라……참으로 기대되는군.')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene4():
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
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('보름 뒤…….', 3)
    stage.setStageName('허도  승상부')
    stage.showUnit(181, 90, 55, 3)
    stage.say('&181\n보고드립니다!')
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 서량군이 위수를 공격했습니다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n왔느냐?\n좋다! 우리도 위수로 간다.\n서량군을 맞아 싸운다!')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 330, 129)
    stage.bottomTxt('마등의 아들은', False, False, False)
    stage.showHead(57, 50, 0)
    stage.bottomTxt('마초는 서량군을 이끌고,', True, False, False)
    stage.delay(5)
    stage.head(57).move(176, 83)
    stage.delay(5)
    stage.bottomTxt('목표인 허도를 향해 남하했다.', True, False, False)
    stage.delay(8)
    stage.head(0).move(240, 83)
    stage.delay(5)
    stage.bottomTxt('조조는 위수로 진군하여 맞아 싸웠다.', True, True, True)
    stage.bgSound(15)
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
    stage.setStageName('위수@조조군 본진')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 13, [
        12,
        13,
        23,
        15], [
        2,
        10,
        11])
    gvars[4076] = 0
    gvars[4057] = 31
    stage.setBattlePos([
        [
            8,
            5],
        [
            18,
            29],
        [
            18,
            30],
        [
            8,
            1],
        [
            9,
            1],
        [
            17,
            30],
        [
            8,
            6],
        [
            18,
            28],
        [
            9,
            6],
        [
            19,
            29],
        [
            7,
            6],
        [
            17,
            31],
        [
            9,
            5],
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
        return True
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 전장의 지형을 설명하게.\n&20\n예.\n서량군의 녹채 둘레에는 큰 강이 흐릅니다.\n&20\n그 밖에 두드러진 지형은 없으나\n전장이 큰 눈에 덮여 있는 것이 문제입니다.\n&0\n눈이라고? 골치 아프군.\n&20\n그렇습니다.\n이러면 신속하게 움직일 수 없습니다.')
    if stage.unitClickTest(12):
        stage.say('&0\n자효, 서황과 함께 서량군의 후방으로 우회하게.\n알겠는가?\n&12\n예.')
    if stage.unitClickTest(15):
        stage.say('&15\n손발이 근질거려 견딜 수가 없습니다.\n주군, 부디 마초와 싸우게 해 주십시오.')
    if stage.unitClickTest(1):
        stage.say('&1\n서량의 금마초라고?\n그리 대단해 보이지는 않던데.\n정말 그렇게 강한가?\n&23\n마초의 용맹은 의심할 여지가 없습니다.\n&1\n그런가……?\n&0\n게다가 생김새도 늠름하고 준수하다더군.\n원양의 얼굴을 보면\n여인들이 모두 놀라 달아나겠지만 말이야.\n&1\n맹덕!!\n&0\n하하하.\n농담일세, 농담이야.')
    if stage.unitClickTest(23):
        stage.say('&23\n주군, 무예가 뛰어난 자는 마초뿐이 아닙니다.\n&23\n그의 사촌 마대와 방덕도\n삼군 가운데 으뜸가는 용맹을 지닌 맹장입니다.')
    if stage.unitClickTest(24):
        stage.say('&24\n이 무렵에는 날씨가 쉽게 나빠지니\n책략을 쓸 때 주의해야 합니다.')
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
