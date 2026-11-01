# Source Generated with Decompyle++
# File: R_49.marshal (Python 3.9)


def scene0():
    '''붉은 제단'''
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 57)
    stage.showUnits([
        [
            77,
            66,
            52,
            3],
        [
            35,
            52,
            52,
            1]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setEventName('붉은 제단')
    stage.setStageName('적벽  오군 본진')
    stage.delay(3)
    stage.unit(35).setAction(11)
    stage.say('&35\n……이쯤이면 조조도 강릉을 함락했겠지.\n우리도 서둘러야겠다.')
    stage.unit(35).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 2)
    stage.say('&77\n시간을 벌기 위해\n동오가 이토록 참혹한 대가를 치르다니\n이 주유도 탄식할 뿐이오.\n&35\n나는 맹약을 지켰소.\n이제 그대가 지킬 차례요.\n&35\n형주의 촉군 대부분을 넘겨주었으니\n어떻게든 적벽에서 조조군의 공세를 막으시오.')
    stage.unit(77).setAction(0, 3)
    stage.say('&77\n즉, 기도가 끝나면\n동오는 쓸모없어진다는 말인가?\n&35\n그것은 내게 중요하지 않다.\n제단은 준비되었느냐?')
    stage.unit(77).setAction(2)
    stage.say('&77\n이 주유의 능력을 의심하는가?\n나는 한 번도 실패한 적이 없다.')
    stage.unit(77).setAction(0)
    stage.say('&35\n아, 실례했소.\n허나 그대도 위군이 맥성에 출병하여\n오군을 칠 줄은 예상하지 못하지 않았소?\n&77\n그대 역시 마찬가지 아니오?')
    stage.unit(77).setAction(0, 2)
    stage.say('&77\n동오의 땅이 금수에게 짓밟히니\n원통하기 짝이 없구나…….\n허나 이렇게 된 이상 어쩔 도리가 없다.')
    stage.unit(77).setAction(11)
    stage.say('&77\n꽃의 마음은 꽃만이 아는 법.\n허나 꽃이 어찌 짐승의 마음을 알겠는가!?\n&35\n음, 내게는 강릉보다 제단이 중요하오.\n어서 준비에 착수하시오.')
    stage.unit(35).move(16, 52, 3)
    stage.unit(35).hide()
    stage.delay(5)
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(0, 3)
    stage.say('&77\n흥! 공명은 아직도 모르는군……\n동오가 훗날 버릴 패는 바로 자신이라는 것을.')
    stage.unit(77).setAction(0, 2)
    stage.say('&77\n허나 위나라가 동오를 겨눈 것은\n참으로 깊이 내다본 한 수였다!\n신기묘산을 자랑하는 나조차 예상하지 못했으니.\n&77\n흥! 그것도 상관없다.\n짐승 같은 위군은 적벽에서 꺾일 것이다.\n&77\n그러면 조조, 저 어리석은 자도\n공명의 촉으로 목표를 돌리겠지.')
    stage.unit(77).setAction(17)
    stage.say('&77\n위와 촉이 다투면 동오가 어부지리를 얻는다.\n모두 우리 동오의 앞날을 위한 일이었다.')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene1():
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
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('강릉  조조군 본진')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n운장, 이번에는 참으로 훌륭히 해 주었네.')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(3).setAction(0, 2)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('&3\n……아직은 마음이 풀리지 않소.\n주유와 공명을 죽이기 전에는\n이 가슴의 한을 씻을 수 없소.')
    stage.unit(3).setAction(0)
    stage.say('&0\n음.')
    stage.showUnit(181, 54, 22, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n아뢰옵니다!\n&181\n주군, 동오에 보낸 밀정이 군정을 전해 왔습니다.\n오군의 함선들이 적벽으로 집결하고 있다 합니다.')
    stage.unit(0).setAction(3)
    stage.say('&0\n뭐라고!?')
    stage.unit(0).setAction(0)
    stage.say('&181\n지난 적벽 전투 때보다 규모가 훨씬 큽니다.')
    stage.unit(0).setAction(2)
    model.setFace(2)
    stage.say('&0\n벽안아, 우리 시선을 강릉으로 돌린 것은\n적벽에서 만반의 준비를 갖추기 위해서였군!')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.delay(3)
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n적벽대전 때보다 규모가 크다니……\n과연 동오 수군답군요.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n그렇다고 물러설 수는 없다.\n우리도 적벽으로 진군한다.\n전군 출발을 준비하라!')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 263, 214)
    stage.bottomTxt('조조군', False, False, False)
    stage.delay(5)
    stage.head(0).move(315, 234)
    stage.delay(5)
    stage.bottomTxt('적벽으로 쳐들어갔다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 57)
    stage.showUnits([
        [
            76,
            66,
            52,
            3],
        [
            77,
            52,
            41,
            2],
        [
            78,
            46,
            41,
            2],
        [
            79,
            40,
            41,
            2],
        [
            80,
            52,
            63],
        [
            81,
            46,
            63],
        [
            84,
            40,
            63]])
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.setStageName('적벽  오군 본진')
    stage.delay(3)
    stage.unit(77).setAction(6)
    stage.say('&77\n주군, 위군을 맞을 준비를 마쳤습니다.\n&77\n염려하지 마십시오.\n찬란한 승리의 빛이\n우리 동오를 비추고 있습니다.')
    stage.unit(77).setAction(0)
    stage.say('&76\n음, 이번 적벽 싸움에서는\n적에게 동오 수군의 위력을 보여 주겠다.')
    stage.unit(77).setAction(3)
    stage.say('&77\n주군, 장강의 물소리를 들으십시오.\n동오 수군의 용맹을 찬양하고 있습니다!')
    stage.unit(77).setAction(0)
    stage.say('&77\n(내 목적은 위의 칼끝을 촉으로 돌리는 것이다.\n바라는 것은 오직 그뿐이다.)')
    stage.unit(77).setAction(17)
    stage.say('&77\n(이 주공근의 뜻은 하늘만이 아시리라!\n모두 동오를 위한 일이다!)')
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 73)
    stage.draw()
    stage.effectSound(208, 0)
    stage.delay(3)
    stage.showUnit(35, 95, 73, 3)
    stage.setEventName('속아 넘어간 주유')
    stage.setStageName('적벽 교외  강가')
    stage.unit(35).move(57, 73, 3)
    stage.delay(3)
    stage.unit(35).setAction(0, 2)
    stage.say('&35\n하하하, 적벽의 기도가 끝났다.')
    stage.unit(35).setAction(11)
    stage.say('&35\n동오 함대가 적벽에 모였으니\n조조와 주유 중 하나가 쓰러질 때까지\n이 큰 싸움은 계속되겠지.\n&35\n모두 내 예상대로구나.')
    stage.unit(35).setAction(0)
    stage.say('&35\n이 틈에 다음 기도를 준비해야겠군.\n&35\n하하하.')
    stage.unit(35).move(5, 73, 3)
    stage.unit(35).hide()
    stage.effectSound(208, 255)
    stage.clsUnit()
    return False


def scene4():
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
            17,
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
            3,
            44,
            52,
            1]])
    stage.draw()
    stage.setStageName('적벽  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n다시 적벽으로 돌아왔군.\n이번 싸움에서는 반드시 지난날의 원수를 갚겠다!')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19])
    gvars[4076] = 0
    gvars[4057] = 49
    stage.setBattlePos([
        [
            15,
            4],
        [
            16,
            4],
        [
            15,
            5],
        [
            16,
            5],
        [
            14,
            5],
        [
            17,
            5],
        [
            14,
            4],
        [
            17,
            4],
        [
            15,
            3],
        [
            16,
            3],
        [
            14,
            3],
        [
            17,
            3],
        [
            15,
            2],
        [
            16,
            2],
        [
            14,
            2]])
    return False


def scene5():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출진하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n전장 북쪽에는 장강이, 남쪽에는 육지가 있습니다.\n&20\n오군은 수륙 양쪽에 병력을 배치했을 테니\n고전하게 될 것입니다.')
    if stage.unitClickTest(19):
        stage.say('&19\n주유가 동오 수군을 지휘하면\n그 위력은 배가될 것이니\n한 치도 방심해서는 안 됩니다.')
    if stage.unitClickTest(3):
        stage.say('&3\n형님과 셋째는 주유 때문에 죽었습니다.\n주군, 부디 저를 선봉에 세워 주십시오.')
    if stage.unitClickTest(1):
        stage.say('&1\n적벽에 가서 하루 종일 막사에만 있으라 하지 말게.\n정면 승부로 놈들을 무찌르겠네!')
    if stage.unitClickTest(15):
        stage.say('&15\n음, 음……\n&0\n허저, 무슨 일인가?\n몸이 불편한가?\n&15\n아, 아무것도 아닙니다.\n적벽에서 싸우기 전에 기념으로 시 한 수를 짓고 싶어서요.\n&1\n시를 짓겠다고?\n자네가!?\n하하하하!\n&17\n호호호……\n&15\n뭐가 그리 우습습니까!?\n모두 왜 웃으십니까!\n제가 시를 지으면 안 된다는 겁니까!?\n&0\n아니, 나쁠 것 없네.\n오히려 아주 좋은 취미일세.\n알겠네, 알겠어. 싸움이 끝나면 가르쳐 주겠네.\n&15\n예, 감사합니다, 주군!\n&0\n음……푸흡, 하하하하!\n&15\n너, 너무하십니다!\n주군까지 저를 비웃으시다니……')
    if stage.unitClickTest(17):
        stage.say('&17\n사마 선생께서는 적벽대전을 모르시겠군요?\n&19\n실은 순욱 선생께 전쟁의 전말을 여쭈어\n대강은 알고 있습니다.')
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
