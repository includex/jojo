# Source Generated with Decompyle++
# File: R_52.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(13)
    stage.delay(3)
    stage.loadBg(2, 16)
    stage.showUnit(77, 52, 82)
    stage.draw()
    stage.setEventName('네 개의 제단')
    stage.setStageName('건업  의사청')
    stage.showUnit(0, 52, 5, 2)
    stage.unit(0).move(52, 18, 2)
    stage.unit(0).setAction(0)
    stage.say('&0\n주유!')
    stage.unit(0).move(52, 60, 2)
    stage.unit(0).setAction(0)
    stage.say('&0\n주유, 드디어 찾았구나!')
    stage.unit(77).setAction(0, 1)
    stage.say('&77\n흥! 조조……\n끝내 여기까지 찾아왔군……\n&0\n주유, 손권의 주요 장수들은\n이미 모두 전사했다. 동오는 멸망했다!\n&77\n………\n&0\n마지막으로 한 가지 묻고 싶다.\n&77\n패장인 내게 무엇을 묻겠다는 것이냐?')
    stage.unit(0).setAction(3)
    stage.say('&0\n공명에 관한 일이다.\n어찌하여 갑자기 반란을 일으켰을까?\n대체 무엇을 노리는 것이냐!?')
    stage.unit(0).setAction(0)
    stage.delay(10)
    stage.unit(77).setAction(0, 0)
    stage.say('&77\n온 인간 세상을 지배하기 위해서다.\n공명에게는 고대의 마왕이 들었다.')
    model.setFace(1)
    stage.say('&0\n！？')
    stage.unit(77).setAction(0, 2)
    stage.say('&77\n마왕은 아직 힘을 온전히 쓰지 못하지만\n공명이 네 제단에서 기도를 마치면\n진정한 힘을 얻게 된다.')
    stage.say('&77\n제단은 정해진 장소에 세워야 합니다.\n두 곳은 적벽과 허도이며\n나머지 두 곳은 서쪽에 있습니다.')
    stage.unit(77).setAction(0, 0)
    stage.say('&77\n허도를 지킬 수 있다면\n마왕의 부활을 막을 수 있다.\n온 힘을 다해라!\n&0\n그런 일이 있었을 줄이야.')
    model.setFace(0)
    stage.unit(77).setAction(9)
    stage.say('&77\n콜록!!\n콜록, 콜록……!\n&0\n주유, 그 병은…….')
    stage.say('&77\n이미 오가 멸망했으니\n내 병도 중요하지 않다.\n다만……')
    stage.say('&77\n하늘이 시간을 조금만 더 주었다면\n애초에 공명과 손을 잡지 않았을 것이다……')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(17)
    stage.say('&77\n……조조, 네 손에 죽지는 않겠다.\n……콜록, 콜록콜록!')
    stage.unit(0).setAction(3)
    stage.say('&0\n주유!\n&77\n다가오지 마라!!\n&77\n내 목숨은 내 손으로 끝내겠다!!')
    stage.unit(77).setAction(0)
    stage.delay(5)
    stage.effectSound(38, 1)
    stage.unit(77).setAction(10)
    stage.say('&0\n주유!!')
    stage.delay(10)
    stage.unit(0).setAction(0)
    stage.say('&0\n주유.\n죽는 순간까지 오만하구나.')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.sectionName(4, '마왕 부활')
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
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
            3],
        [
            1,
            44,
            64,
            1],
        [
            3,
            44,
            58,
            1],
        [
            17,
            44,
            52,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('촉으로 향하라!')
    stage.setStageName('건업  조조군 본진')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n공명에게 마왕이 들었다니\n실로 믿기 어려운 일입니다.\n하지만………')
    stage.unit(19).setAction(0)
    stage.say('&0\n음, 나도 그렇게 생각하네.\n하지만 공명의 저 기이한 행실은\n정녕 악마나 할 법한 짓이지.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n주유가 죽기 전에 한 말은 믿지 않을 수 없네.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n공명은 천하에 네 제단을 세우고\n기도하여 마왕의 힘을 깨우려 한다더군.\n&0\n네 제단의 기도가 모두 끝나면\n마왕 본래의 마력이 되살아난다는 것이다.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n공명…… 아니, 마왕의 목적이\n바로 그것입니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n네 개의 제단……\n그중 하나가 적벽에서 발견한 제단입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n그렇다면 나머지 세 제단은 어디에 있겠습니까?')
    stage.unit(21).setAction(0)
    stage.say('&0\n하나는 허도에 있다.')
    stage.unit(20).setAction(6)
    stage.say('&20\n뭐라고, 허도!?\n우리의 근거지에 있단 말인가?')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n즉시 허도에 전하라.\n수상한 자는 누구도 들이지 말라고.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n허도는 서서가 지키고 있으니\n즉시 엄중히 경계하도록 전하시지요.')
    stage.unit(21).setAction(0)
    stage.say('&0\n서쪽에 아직 둘이 있다.')
    stage.unit(20).setAction(6)
    stage.say('&20\n촉의 영토 안에 있을 것으로 보입니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n음……하지만 아직 단정할 수는 없다.\n참, 중달.')
    stage.unit(19).setAction(6)
    stage.say('&19\n여기 있습니다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n그대에게 조사를 명한다.\n옛 문헌을 뒤져 제단의 위치를 알아내라.')
    stage.unit(0).setAction(0)
    stage.say('&19\n예.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n모두 명심하라!\n결코 마왕을 부활시켜서는 안 된다!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n전군은 즉시 진군하라.\n촉을 토벌하겠다!\n반드시 공명을 제거해야 한다!')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 471, 198)
    stage.showHead(76, 535, 198)
    stage.bottomTxt('조조는 건업을 공격하여,', False, False, False)
    stage.delay(5)
    stage.head(76).hide()
    stage.delay(5)
    stage.bottomTxt('마침내 동오를 멸망시켰다.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('주유는 죽기 전에', True, True, False)
    stage.delay(5)
    stage.bottomTxt('네 제단의 수수께끼.', True, False, False)
    stage.delay(10)
    stage.bottomTxt('조조는 마왕의 부활을 막고자,', True, True, False)
    stage.delay(5)
    stage.head(0).move(240, 220)
    stage.delay(5)
    stage.bottomTxt('군사를 이끌고 촉으로 향했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 46)
    stage.showUnit(35, 53, 71)
    stage.draw()
    stage.effectSound(204, 0)
    stage.delay(3)
    stage.setStageName('백제성  공명의 저택')
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.delay(3)
    stage.unit(35).setAction(11)
    stage.say('&35\n동쪽 하늘에서 주유의 장성이 떨어졌군.\n흥, 주유 그 애송이가 마침내 죽었구나.\n&35\n어차피 내 말 하나에 불과했지.')
    stage.unit(35).setAction(0)
    stage.say('&35\n……으윽!!')
    stage.unit(35).setAction(8)
    stage.say('&35\n으아아!\n고…… 공명의 혼백이다……!\n으아아아!')
    stage.unit(35).setAction(12)
    stage.say('&35\n고, 공명……\n거꾸로 나를 지배하려는 것이냐!?\n나를 너무 얕보았구나!')
    stage.showUnit(69, 53, 5, 2)
    stage.say('&69\n군사?')
    stage.unit(35).setAction(8)
    stage.say('&35\n가, 강유……? 이, 이런!')
    stage.unit(35).setAction(12)
    stage.say('&35\n으아악!')
    stage.unit(35).setAction(8)
    stage.say('&35\n……헉, 헉, 헉…….\n흥, 빌어먹을 공명…….\n아직도 발버둥 치려 하느냐!?')
    stage.unit(35).setAction(0)
    stage.say('&35\n이 마왕이 아직 부활하지 못했다 하나\n범인의 혼에 휘둘릴 수는 없다…….\n&69\n군사, 강유입니다.\n들어가도 되겠습니까?')
    stage.unit(35).setAction(0, 0)
    stage.say('&35\n오, 강유인가?\n어서 들어오너라.\n&69\n실례하겠습니다.')
    stage.unit(69).move(53, 55, 2)
    stage.delay(3)
    stage.unit(69).setAction(6)
    stage.say('&69\n공명 선생, 방금 왜 그러셨습니까?\n신음 소리를 들은 듯합니다.')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n아, 아무것도 아니네.\n신경 쓰지 말게.\n그런데 그대는…… 무슨 일인가?')
    stage.unit(69).setAction(6)
    stage.say('&69\n아, 그렇지요.\n방금 군정 보고를 받았는데\n조조가 이미 동오를 멸했다고 합니다.')
    stage.unit(35).setAction(0, 0)
    stage.say('&35\n그런가…….\n동오가 멸망했다고?\n&69\n손권과 주유를 비롯한 주요 장수들이 모두 전사했습니다.\n&35\n음.\n&69\n게다가 조조군이 기세를 몰아 추격하여\n이미 우리 촉을 공격하고 있습니다.')
    stage.unit(69).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n그렇군…….\n그럼 우리도 움직여야겠군.')
    stage.unit(35).setAction(0, 0)
    stage.say('&35\n나는 즉시 어복포로 가겠다.\n&35\n너는 이곳에 남아 기도 준비를 마저 하라.\n한 치의 실수도 있어서는 안 된다.')
    stage.unit(69).setAction(6)
    stage.say('&69\n예.')
    stage.unit(35).setAction(0, 1)
    stage.delay(3)
    stage.unit(35).setAction(11)
    stage.say('&35\n한실을 부흥시키려던 선주의 유지를 이으려면\n이제는 기도로 비술을 얻는 수밖에 없다.\n&69\n알겠습니다.\n염려 마십시오.')
    stage.effectSound(204, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
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
            23,
            64,
            52,
            3],
        [
            1,
            44,
            64,
            1],
        [
            3,
            44,
            58,
            1],
        [
            17,
            44,
            52,
            1]])
    stage.draw()
    stage.setStageName('촉·오 국경  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n곧 적의 영내에 들어간다.\n모두 조금도 방심하지 말라.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n예.')
    stage.unit(1).setAction(0)
    stage.delay(5)
    stage.showUnit(181, 54, 22, 2)
    stage.say('&181\n아룁니다!\n&0\n무슨 일이냐?')
    stage.unit(181).setAction(11)
    stage.say('&181\n주군께 아룁니다. 방금 정찰병이 돌아와\n백제성에서 촉군을 발견했다고 보고했습니다.')
    stage.unit(1).setAction(0, 1)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n오, 적군을 발견했나?\n맹덕, 당장 공격하세.')
    stage.unit(1).setAction(0)
    stage.say('&0\n물론이지.\n허나 백제성이라…….\n운장, 현덕이 죽은 곳이 백제성이었지…….')
    stage.unit(3).setAction(0, 2)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('&3\n그렇습니다.\n형님은 그곳에서 돌아가셨습니다.')
    stage.unit(3).setAction(0)
    stage.say('&0\n음, 자네에게는 잊지 못할 곳이겠군.')
    stage.unit(20).setAction(6)
    stage.say('&20\n관우 장군.\n이곳에서 백제성을 공격하려면\n어느 길로 가는 것이 좋겠습니까?')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(0, 1)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('&3\n백제성으로 가는 길은 여러 갈래입니다만\n어복포를 지나는 길이 가장 가깝습니다.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n어복포라고? 좋다, 그 길로 간다.\n마왕의 부활을 막으려면\n더는 지체할 수 없다.')
    stage.unit(0).setAction(0)
    stage.unit(181).hide()
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 12)
    gvars[4076] = 0
    gvars[4057] = 52
    stage.setBattlePos([
        [
            4,
            20],
        [
            5,
            20],
        [
            3,
            20],
        [
            6,
            20],
        [
            4,
            21],
        [
            5,
            21],
        [
            3,
            21],
        [
            6,
            21],
        [
            4,
            22],
        [
            5,
            22],
        [
            3,
            22],
        [
            6,
            22],
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


def scene4():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n백제성의 성벽은 삼중 구조입니다.\n&1\n건업보다는 규모가 작군.\n&20\n그렇습니다만, 우리가 건업에서 어떻게 싸웠는지\n촉군도 잘 알고 있습니다.\n&20\n그러니 결코 방심해서는 안 됩니다.')
    if stage.unitClickTest(3):
        stage.say('&0\n관우, 어복포는 어떤 곳인가?\n&3\n산과 강 사이에 낀 평지입니다.\n지형에 별다른 특징은 없습니다.')
    if stage.unitClickTest(19):
        stage.say('&19\n주군, 마왕에게는 자연의 사물에\n생명을 불어넣어\n조종하는 힘이 있는 듯합니다.\n&0\n건업에서 본 목인처럼 말인가?\n&19\n예. 아마 그럴 것입니다.')
    if stage.unitClickTest(17):
        stage.say('&17\n불길한 예감이 들어요.\n이곳을 피해 다른 길로 가면 어떨까요?\n&0\n지금 가장 중요한 것은 시간을 버는 일이네.\n절대로 먼 길로 돌아갈 수는 없어.')
    if stage.unitClickTest(23):
        stage.say('&23\n상대가 공명인 만큼\n길에서 적의 매복을 경계해야 합니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n본래 주유라는 놈을 좋아하지는 않았네.\n하지만 그도 공명에게 당했으니\n그를 위해서가 아니더라도 공명의 야심은 꺾어야겠어!')
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
