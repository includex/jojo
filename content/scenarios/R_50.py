# Source Generated with Decompyle++
# File: R_50.marshal (Python 3.9)


def scene0():
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.loadBg(2, 72)
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('모든 것은 동오를 위해')
    stage.setStageName('적벽 교외  강가')
    stage.showUnit(76, 69, 95)
    stage.unit(76).move(69, 85, 0)
    stage.showUnit(77, 69, 95)
    stage.unitsMove([
        [
            stage.unit(76),
            69,
            45,
            0],
        [
            stage.unit(77),
            69,
            55,
            0]])
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('&77\n주군, 동오의 운명에는 아직 희망이 있습니다!\n기운을 잃지 마십시오!')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(76).setAction(0, 2)
    stage.say('&76\n……주유.\n물어볼 것이 있네.')
    stage.unit(77).setAction(6)
    stage.say('&77\n무엇을 물으시려는 것입니까?')
    stage.unit(77).setAction(0)
    stage.say('&76\n유비를 죽음으로 몰아넣은 것은 우리 동오이니\n촉에게 우리는 원수다.\n&76\n헌데 적벽 전투에서\n촉군은 우리 오나라를 위해 싸웠네.\n대체 어찌 된 일인가?')
    stage.unit(77).setAction(6)
    stage.say('&77\n주군, 제 마음에도 물음을 던져 보았습니다.\n이제 모든 것을 털어놓겠습니다.')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 1)
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('&77\n실은 이 주유의 맑은 마음도 이미 더럽혀져\n불의한 자와 맹약을 맺고 말았다.')
    stage.unit(77).setAction(0)
    stage.say('&76\n공명이라……역시 그랬군.')
    stage.unit(77).setAction(0, 0)
    stage.delay(3)
    stage.unit(77).setAction(6)
    stage.say('&77\n공명은 주군과 의리를 저버린 자입니다.\n촉의 실권을 장악하고자\n제 주군의 목숨을 노리고 있습니다.\n&76\n공명이 유비를 해치려 한다고……?\n공명이 어찌……믿을 수 없군!\n&77\n공명은 더럽혀진 날개를 펼치고\n제가 지키던 고장으로 날아와서는\n사람을 홀리는 웃음으로 거래를 제안했습니다.')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 1)
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('&77\n유비를 죽이도록 도와주면\n형주를 우리에게 떼어 주겠다고 했습니다!\n&76\n음.')
    stage.unit(77).setAction(0)
    stage.say('&77\n……저는 그와 그런 맹약을 맺었습니다.')
    stage.unit(77).setAction(0, 0)
    stage.delay(3)
    stage.unit(77).setAction(6)
    stage.say('&77\n그 뒤 저는 그를 도와 유비를 제거했고\n&77\n그 대가로 비옥한 형주를 얻었습니다.')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 1)
    stage.say('&77\n허나 이 주유도 미처 예상하지 못했습니다.\n간적 조조가 감히 오나라를 침공할 줄은……')
    stage.unit(77).setAction(17)
    stage.say('&77\n제가 한 모든 일은 오직 동오를 위해서였습니다.\n동오가 천하를 통일하게 하려고……')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 0)
    stage.delay(3)
    stage.unit(77).setAction(11)
    stage.say('&77\n부디 용서해 주십시오, 주군.\n&76\n……음, 그대도 어쩔 수 없었겠지.\n&76\n허나 이 지경에 이르렀으니\n이제 어찌하면 좋겠는가?')
    stage.unit(77).setAction(0)
    stage.say('&77\n전선에 있는 유수구는\n도읍 건업을 지키는 관문이니\n반드시 이곳에서 조조군의 공세를 막아야 합니다.')
    stage.unit(77).setAction(6)
    stage.say('&77\n주군, 조조가 우리 고향을 짓밟지 못하도록\n각지의 남은 병력을 모아 유수구로 가겠습니다.\n&77\n제가 돌아올 때까지\n유수구의 방비를 강화해 주시겠습니까?\n&76\n음, 좋다.\n육손에게 수군을 재정비하라 명하겠다.\n&77\n그럼 잠시 물러가겠습니다.\n&76\n음, 수고해 주게.')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).move(75, 55, 1)
    stage.unit(77).move(75, 10, 1)
    stage.unit(76).setAction(0, 0)
    stage.unit(77).hide()
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 288, 231)
    stage.showHead(76, 352, 231)
    stage.bottomTxt('오군은 적벽에서 패했고,', False, False, False)
    stage.delay(5)
    stage.head(76).move(443, 214)
    stage.delay(5)
    stage.bottomTxt('유수구로 물러났다.', True, False, True)
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
            15,
            44,
            52,
            1]])
    stage.draw()
    stage.setStageName('적벽  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n모두 이번 싸움에서 훌륭히 싸워 주었다.\n수고했다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n그렇습니다. 참으로 훌륭한 승리입니다.\n강대한 동오의 수군이라도\n당분간은 원기를 되찾기 어려울 것입니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n주군, 이 절호의 기회를 놓치시면 안 됩니다.\n단숨에 동오를 멸하시지요.')
    stage.unit(23).setAction(0)
    stage.say('&0\n음.')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n허나 주군,\n제가 적벽의 오군 진영에서 붉은 제단을 보았습니다.\n참으로 의심스럽습니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n음.\n사마의, 무엇에 쓰는 물건이라고 생각하는가?')
    stage.unit(19).setAction(6)
    stage.say('&19\n저도 알 수 없습니다.\n혹시 기도에 쓰는 것은 아닐까요……')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n이봐, 그것은 혹시 위급할 때\n신명께 가호를 비는 물건 아닌가?')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 3)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n아닙니다. 장군의 말씀은 틀렸습니다.\n주유 같은 자가 어찌 신의 힘을 빌리겠습니까.')
    stage.unit(19).setAction(0)
    stage.say('&0\n음, 지금 논쟁해 봐야 소용없다.\n지금은 오군을 상대하는 데 힘을 쏟아야 한다.')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n예.')
    stage.unit(19).setAction(0)
    stage.say('&0\n문약, 오군은 지금 어디에 있는가?')
    stage.unit(20).setAction(6)
    stage.say('&20\n이미 건업 부근의 유수구로 물러났습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n유수구라…… 건업 근처에 있는 곳이지?\n그렇다면 오군도 필사적으로 싸우겠군.')
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n하지만 맹덕,\n그렇다고 우리가 여기 멈춰 설 수는 없지 않은가.')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n음.\n좋다, 유수구로 향하라!\n오군을 완전히 무너뜨리겠다!')
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(23).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19])
    gvars[4076] = 0
    gvars[4057] = 50
    stage.setBattlePos([
        [
            12,
            12],
        [
            13,
            12],
        [
            11,
            12],
        [
            14,
            12],
        [
            10,
            12],
        [
            15,
            12],
        [
            12,
            11],
        [
            13,
            11],
        [
            11,
            11],
        [
            14,
            11],
        [
            10,
            11],
        [
            15,
            11],
        [
            12,
            8],
        [
            13,
            8],
        [
            12,
            7]])
    return False


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출진하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n여전히 장강을 사이에 두고 싸우는 형세입니다.')
    if stage.unitClickTest(19):
        stage.say('&19\n(저 제단이 여전히 마음에 걸리는군.\n대체 어디에 쓰는 것일까……)\n&19\n(좋다. 그 일은 잠시 접어 두자.\n지금은 이 싸움에 집중해야 한다.)')
    if stage.unitClickTest(23):
        stage.say('&23\n방금 밀정이 군정을 전해 왔습니다.\n동오 진영에 주유와 육손의 모습이 보이지 않는다고 합니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n오군에게 준비할 틈을 주어서는 안 되네.\n단숨에 몰아붙이세.\n&0\n음.')
    if stage.unitClickTest(3):
        stage.say('&3\n수전이든 육전이든 상관없습니다.\n그저 온 힘을 다해 적을 무찌르겠습니다.')
    if stage.unitClickTest(15):
        stage.say('&1\n그러고 보니 허저, 시는 다 지었나?\n&15\n예?\n&1\n무슨 예니 뭐니 하는가.\n맹덕에게 제법 배웠을 것 아닌가?\n대체 어떤 시를 지었나?\n&15\n……이미 그만두었습니다.\n&1\n그만두었다고? 어째서?\n&15\n까닭은……말하고 싶지 않습니다.\n&1\n아, 그런 일이 있었군……\n어쨌든 대강 짐작은 가네.\n&15\n흥, 저는 신경 쓰지 않습니다.\n어차피 날 때부터 싸우는 재주밖에 없으니까요.\n&0\n허허, 이거……')
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
