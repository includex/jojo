# Source Generated with Decompyle++
# File: R_47.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 31)
    model.setRAvatar(3, 199)
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setEventName('도원에 끊어진 꿈')
    stage.setStageName('백제성 교외  산길')
    stage.showUnit(3, 5, 49, 1)
    stage.unit(3).move(51, 49, 1)
    stage.say('&3\n형님!\n셋째야!\n무사해야 한다!')
    stage.showUnit(181, 95, 55, 3)
    stage.unit(181).move(59, 55, 3)
    stage.say('&181\n과, 관 장군!\n관 장군이십니까!?\n&3\n너는…… 형주의 수비병이로구나?\n어디로 가는 길이냐?\n&181\n공명 군사께 보고드리러 가는 길입니다.\n&3\n보고라고?\n무슨 보고냐?\n서, 설마…….\n&181\n관 장군, 형주가 함락되었습니다!\n&3\n그런가…… 내가 늦었구나…….\n그럼 주군과 셋째 장군은 어찌 되었느냐!?\n&181\n백제성으로 퇴각하셨습니다.\n&3\n그런가…… 알겠다.\n수고했다.\n나는 백제성으로 가겠다!\n&181\n예.\n&3\n군사께 망설이지 말고 즉시 원군을 보내시라 전하라.\n&181\n예, 반드시 전하겠습니다!\n&3\n부탁한다.\n&181\n과, 관우 장군…….')
    stage.unit(3).move(98, 49, 1)
    stage.unit(3).hide()
    stage.effectSound(201, 255)
    model.setRAvatar(3, 6)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(13)
    stage.delay(3)
    stage.loadBg(2, 5)
    stage.showUnits([
        [
            32,
            55,
            46,
            2,
            10],
        [
            203,
            58,
            43,
            2,
            13]])
    stage.draw()
    stage.setStageName('백제성')
    stage.showUnit(3, 31, 61, 1)
    stage.say('&3\n형님!')
    stage.unit(3).move(52, 61, 1)
    stage.delay(3)
    stage.unit(3).move(52, 52, 0)
    stage.say('&3\n형님!\n&32\n……오, 둘째냐?')
    stage.unit(3).setAction(3)
    stage.say('&3\n형님, 무사하십니까!')
    stage.unit(3).setAction(0)
    stage.say('&3\n(……으음!? 이 상처는……)\n&32\n……보다시피 내 명이 얼마 남지 않았구나.')
    stage.unit(3).setAction(3)
    stage.say('&3\n……그런 나약한 말은 하지 마라!')
    stage.unit(3).setAction(0)
    stage.say('&32\n나는 염려 말게. 그보다……\n&32\n셋째 일행이 나를 피신시키려고 맥성을 사수했네.\n지금도 오군과 싸우고 있을 것이야……')
    stage.unit(3).setAction(6)
    stage.say('&3\n셋째 아우가……아직도 혈투를 벌이고 있다고?\n&32\n둘째야, 네가 가서 셋째 일행을 도와라.\n&3\n……알겠습니다.\n반드시 구해 내겠습니다.\n형님께서는 염려 마십시오.')
    stage.unit(3).setAction(0)
    stage.say('&32\n그렇다면 안심이군.\n&32\n……형주의 싸움은 그야말로 악몽이었네.\n&32\n우리 형주군에는 셋째와 주창, 관평 등\n백전의 맹장들이 있어 싸우면 반드시 이겼지.\n&32\n……그자의 군대가 나타나기 전까지는!')
    stage.unit(3).setAction(6)
    stage.say('&3\n그 사람이라니?\n누구를 말하느냐?')
    stage.delay(5)
    stage.say('&32\n……주유.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(3)
    stage.say('&3\n주유라고!?\n적벽 전투 뒤에 병으로 죽지 않았소?')
    stage.unit(3).setAction(0)
    stage.say('&32\n나도 이미 죽은 줄 알았네.\n허나 우리 형주군을 무너뜨린 적장은 분명 주유였어.')
    stage.say('&32\n둘째야, 공명은 어찌 되었느냐?')
    stage.unit(3).setAction(6)
    stage.say('&3\n……군사께서는 아직 도착하지 않으셨다.')
    stage.unit(3).setAction(0)
    stage.say('&32\n그런가……다행이군.')
    stage.delay(5)
    stage.say('&32\n……둘째야, 공명이 우리를 배신했다.')
    stage.unit(3).setAction(3)
    stage.say('&3\n뭐라고 하셨소!?\n설마 군사가…….')
    stage.unit(3).setAction(0)
    stage.say('&32\n나도 믿지 않네.\n아니, 믿고 싶지 않다!\n하지만…….')
    stage.unit(3).setAction(0, 1)
    stage.say('&3\n(……전부터 그런 의심은 했지만\n설마 사실일 줄이야……!)')
    stage.say('&32\n나는 이제 죽는 것인가……으윽!')
    stage.unit(3).setAction(0, 0)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('&3\n혀, 형님!\n&32\n……둘째야.\n내가 죽으면 조조에게 의탁하거라!')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(3)
    stage.say('&3\n뭐, 뭐라고!?')
    stage.unit(3).setAction(0)
    stage.say('&32\n촉군은 이미 공명의 꼭두각시가 된 듯하다.\n이제 공명을 상대할 사람은 조조뿐이다!\n&32\n……둘째야, 부탁한다.\n반드시 촉을 바른길로 이끌어 다오…… 으윽…….')
    stage.unit(3).setAction(9)
    stage.say('&3\n흑흑, 형님……\n반드시 형님의 뜻을 이루겠습니다.\n&32\n……공명, 그대가 어찌……?\n나는………\n대업을 그대에게 맡기려 했거늘……\n&32\n……………………')
    stage.delay(10)
    stage.say('&3\n……형님?\n……형님!!')
    stage.unit(3).setAction(7)
    stage.say('&3\n우오오!!')
    stage.delay(10)
    stage.unit(3).setAction(9)
    stage.say('&3\n…………')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(0, 2)
    stage.say('&3\n슬픔에 잠겨 있을 때가 아니다.\n셋째 아우와 일행을 구해야 한다!')
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 31)
    model.setRAvatar(3, 199)
    stage.draw()
    stage.showUnit(3, 5, 51, 1)
    stage.setStageName('맥성 교외  산길')
    stage.unit(3).move(51, 51, 1)
    stage.say('&3\n셋째야!\n지금 구하러 가겠다!')
    stage.unit(3).move(95, 51, 1)
    stage.unit(3).hide()
    stage.delay(3)
    stage.bgSound(15)
    model.setRAvatar(3, 6)
    stage.clsUnit()
    return False


def scene3():
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
            15,
            44,
            58,
            1],
        [
            6,
            44,
            52,
            1],
        [
            7,
            44,
            46,
            1],
        [
            9,
            44,
            40,
            1]])
    stage.draw()
    stage.info('한편……', 3)
    stage.setStageName('정군산  조조군 본진')
    stage.say('&0\n하후연, 수고했네.\n장합과 방덕도 잘 싸워 주었어.')
    stage.unit(6).setAction(0, 2)
    stage.delay(3)
    stage.unit(6).setAction(6)
    stage.say('&6\n예.')
    stage.unit(6).setAction(0)
    stage.delay(3)
    stage.unit(7).setAction(0, 2)
    stage.delay(3)
    stage.unit(7).setAction(6)
    stage.say('&7\n그나저나……\n전장에서 보인 공명의 행동은 참으로 이해하기 어렵군요.')
    stage.unit(7).setAction(0)
    stage.delay(3)
    stage.unit(9).setAction(0, 2)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('&9\n저도 전혀 이해하지 못하겠습니다.\n공명은 처음부터 정군산을 칠 생각이 없었던 듯합니다.')
    stage.unit(9).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n무슨 일이지?\n설마 내키는 대로 정군산을 공격한 건가?')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(9).setAction(6)
    stage.say('&9\n이 영명도 단언할 수는 없습니다만………')
    stage.unit(9).setAction(0)
    stage.say('&0\n……음.\n참으로 불가사의한 일이군.\n&0\n아무튼 모두 수고했다.\n물러가 쉬도록 하라.')
    stage.unit(6).setAction(6)
    stage.say('&6\n예.')
    stage.unit(6).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.unit(6).setAction(0, 1)
    stage.unit(7).setAction(0, 1)
    stage.unit(9).setAction(0, 1)
    stage.unitsMove([
        [
            stage.unit(6),
            54,
            52,
            1],
        [
            stage.unit(7),
            54,
            46,
            1],
        [
            stage.unit(9),
            54,
            40,
            1]])
    stage.unitsMove([
        [
            stage.unit(9),
            54,
            22,
            0],
        [
            stage.unit(7),
            54,
            28,
            0],
        [
            stage.unit(6),
            54,
            34,
            0]])
    stage.unit(9).hide()
    stage.unitsMove([
        [
            stage.unit(7),
            54,
            22,
            0],
        [
            stage.unit(6),
            54,
            28,
            0]])
    stage.unit(7).hide()
    stage.unit(6).move(54, 22, 0)
    stage.unit(6).hide()
    stage.delay(10)
    stage.showUnit(17, 54, 22, 2)
    stage.say('&17\n실례하겠습니다.\n&0\n초선인가?\n무슨 일이지?')
    stage.unit(17).setAction(6)
    stage.say('&17\n유비 대인께서 승상께 편지를 보내셨습니다.')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('&0\n유비가 내게 보낸 편지……?')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&17\n네, 방금 황건교도 첩자를 통해 받았어요.')
    stage.unit(17).setAction(0)
    stage.say('&0\n음.\n유비의 속셈을 알 수 없으니 우선 지켜보자.')
    stage.unit(17).move(54, 76, 2)
    stage.delay(3)
    stage.unit(0).setAction(5)
    stage.delay(3)
    stage.unit(17).move(54, 64, 2)
    stage.bgSound(13)
    stage.delay(10)
    model.setFace(1)
    stage.say('&0\n……어, 어찌 이런 일이!?')
    model.setFace(0)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n주군, 무슨 일입니까?\n유비의 서찰에 무엇이라 쓰여 있습니까?')
    stage.unit(20).setAction(0)
    stage.say('&0\n유비는 공명과 주유가 손잡고\n자신의 목숨을 노린다고 하더군.')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n뭐라고!?')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n믿을 수 없습니다!\n주유는 죽지 않았습니까?')
    stage.unit(20).setAction(0)
    stage.say('&0\n만일 자신에게 무슨 일이 생기면\n관우와 장비를 보살펴 달라고도 하더군.')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n뭐라고……?')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0)
    stage.say('&0\n초선, 이 편지의 내용은 너무도 기이하구나.\n혹시 공명의 계략은 아닐까?')
    stage.unit(17).setAction(6)
    stage.say('&17\n서신의 진위는 가리기 어렵지만\n함께 보내온 물건 중에는\n유비 님이 쓰시던 쌍검과 타시던 말도 있었습니다.')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('&0\n뭐라고!?\n자웅쌍검과 적로라고!')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&17\n예.\n확인해 보니 틀림없는 진품이었습니다.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그렇다면 이 서신은 정녕 계략이 아니란 말인가?\n허나……')
    stage.unit(20).setAction(6)
    stage.say('&20\n참, 지난 정군산 싸움에서\n공명의 행동은 정말 기이했습니다……')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n하지만 이것이 사실이라면\n공명이 이미 유비를 배신했다는 뜻입니다.')
    stage.unit(19).setAction(0)
    model.setFace(1)
    stage.say('&0\n공명이 반역을 꾀한다고……?\n도무지 믿을 수 없군.')
    stage.getItem(30, 0)
    stage.getItem(121, 0)
    stage.showUnit(182, 54, 22, 2)
    stage.delay(3)
    stage.unit(182).setAction(11)
    stage.unit(0).setAction(0, 0)
    stage.unit(19).setAction(0, 3)
    stage.unit(20).setAction(0, 3)
    stage.unit(23).setAction(0, 3)
    stage.say('&182\n아룁니다!\n오군이 형주를 빼앗았습니다.')
    stage.unit(0).setAction(3)
    model.setFace(0)
    stage.say('&0\n뭐라고!?')
    stage.unit(0).setAction(0)
    stage.say('&182\n유비는 백제성으로 패주한 뒤 그곳에서 죽었습니다.')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('&0\n뭐라고!?\n유비가 죽었다고?\n정녕 유비가 죽었단 말이냐!?')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n이 군사 정보는 틀림없는 것입니까?')
    stage.unit(23).setAction(0)
    stage.say('&182\n틀림없습니다.')
    stage.unit(20).setAction(6)
    stage.say('&20\n……그럼 다른 형주의 무장들은 어떻습니까?')
    stage.unit(20).setAction(0)
    stage.say('&182\n주군께 아룁니다. 장병 대부분이 전사했습니다.\n살아남은 자들은 장비를 중심으로 맥성을 사수하며\n지금도 동오군과 싸우고 있습니다.')
    stage.unit(20).setAction(6)
    stage.say('&20\n공명이 원군을 보냈습니까?\n성도에는 아직 촉의 정예 부대가 남아 있지 않습니까?')
    stage.unit(20).setAction(0)
    stage.say('&182\n아닙니다. 원군은 보내지 않았습니다.\n성도에서는 상황을 지켜볼 생각인 듯\n원군을 보낼 기미가 없습니다.\n&182\n현재 전황으로 보아\n맥성 함락은 시간문제입니다.')
    stage.unit(20).setAction(0, 1)
    stage.say('&20\n유비는 죽었고 장비도 위험에 빠졌다고……?')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n주군, 유비의 서찰 내용과 일치하는 듯합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n……관우가 어찌 되었다고?\n지금 어디에 있느냐?')
    stage.unit(0).setAction(0)
    stage.say('&0\n유비가 죽고 장비마저 위태로운 상황이라면\n공명이 아무리 관우를 말려도\n그는 앞뒤 가리지 않고 구하러 달려갈 것이다.\n&182\n그렇습니다. 관우는 백제성에서 유비의 죽음을 지켜본 뒤\n홀로 말을 달려 맥성의 장비를 구하러 갔습니다.')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.say('&0\n유비의 죽음, 주유의 부활, 공명의 반역.\n내 눈으로 직접 확인하지 않고는 못 배기겠군!')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n먼저 관우와 장비 일행을 구원한 뒤\n차근차근 깊이 조사하시지요.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n출전을 준비하라!\n맥성으로 급히 달려가 구원해야 한다!\n즉시 전군에 명을 전하라!')
    stage.unit(0).setAction(0)
    stage.say('&182\n예!')
    stage.unit(182).setAction(0)
    stage.delay(3)
    stage.unit(182).hide()
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(23).setAction(0, 3)
    stage.bgSound(15)
    stage.delay(3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10)
    gvars[4076] = 0
    gvars[4057] = 47
    stage.setBattlePos([
        [
            20,
            2],
        [
            21,
            3],
        [
            19,
            1],
        [
            21,
            2],
        [
            20,
            1],
        [
            21,
            1],
        [
            22,
            2],
        [
            20,
            0],
        [
            22,
            1],
        [
            21,
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


def scene4():
    if stage.battleTest():
        stage.say('&0\n출발하라!')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n맥성 주위에는 큰 산이 셋 있습니다.\n&20\n성 주변은 비교적 평탄하지만\n이미 동오군이 장악한 듯합니다.\n&0\n즉, 장비 일행은 새장에 갇힌 새처럼\n날개가 있어도 달아나기 어렵다는 말이군.\n&20\n그렇습니다.')
    if stage.unitClickTest(19):
        stage.say('&19\n병력은 우리 군이 불리하나\n동오군은 육전에 능하지 않습니다.\n&19\n아직 기회는 있습니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n그래도 관우가 홀로 구원에 나선 것은\n너무 무모한 짓이야.\n&0\n그만큼 삼 형제의 정이 깊다는 뜻이겠지.\n&0\n관우에게는\n이길 가망이 있느냐 없느냐가 중요하지 않은 것이네.')
    if stage.unitClickTest(15):
        stage.say('&15\n자, 자꾸 한기가 느껴집니다.\n대, 대체 어찌 된 일이지요?\n&0\n겁먹었느냐, 허저?\n&15\n아, 아닙니다.')
    if stage.unitClickTest(17):
        stage.say('&0\n초선, 황건교도들이 첩자로 나섰다니 무슨 말인가?\n&17\n세상을 떠난 제 부모님의 옛 벗들입니다.\n&17\n전에 장로에게 항복을 권할 때\n그분들의 도움을 받았습니다.\n&0\n아, 그랬군.')
    if stage.unitClickTest(23):
        stage.say('&23\n동오의 대군이 아무리 강해도\n맥성을 지키는 이는 호장 장비입니다.\n&23\n쉽게 함락될 리는 없습니다만……\n&0\n공명이 반역을 꾀했다면\n이야기가 달라진다는 말인가?\n&23\n그렇습니다.')
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
