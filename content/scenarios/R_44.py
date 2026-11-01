# Source Generated with Decompyle++
# File: R_44.marshal (Python 3.9)


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
    stage.showHead(0, 281, 233)
    stage.showHead(76, 345, 233)
    stage.bottomTxt('적벽에서 패한 동오군은', False, False, False)
    stage.delay(5)
    stage.head(76).move(442, 219)
    stage.delay(5)
    stage.bottomTxt('건업으로 퇴각했다.', True, False, True)
    stage.bgSound(15)
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
            19,
            64,
            58,
            3],
        [
            23,
            64,
            52,
            3]])
    stage.draw()
    stage.setEventName('마지막 수전')
    stage.setStageName('적벽  조조군 본진')
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n손권은 공명과 함께 건업으로 물러난 듯합니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n맹덕, 드디어 건업을 공격하는군!\n이번 싸움에서 이기면 천하를 통일할 수 있네!')
    stage.unit(20).setAction(6)
    stage.say('&20\n아닙니다, 하후돈 장군.\n유감스럽지만 건업으로 가기 전에 한 싸움 더 치러야 합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 1)
    stage.say('&1\n한 번 더 싸워야 한다고?\n무슨 뜻인가?')
    stage.unit(23).setAction(6)
    stage.say('&23\n건업의 전초기지인 유수구에는\n틀림없이 대군을 배치해 엄중히 지킬 것입니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n유수구?\n또 수전인가!?')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n음, 남쪽은 배, 북쪽은 말이라 하지.\n적도 그 점을 잘 알고 있을 것이다.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n좋다. 적의 수비가 아무리 굳건해도\n우리에게는 물러설 길이 없다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n유수구를 함락하면 건업을 지킬 방패도 사라진다.\n더는 망설일 수 없다!\n출전을 준비하라!')
    stage.unit(0).setAction(0)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 12)
    gvars[4076] = 0
    gvars[4057] = 44
    stage.setBattlePos([
        [
            12,
            12],
        [
            13,
            12],
        [
            12,
            13],
        [
            13,
            13],
        [
            11,
            13],
        [
            14,
            13],
        [
            11,
            12],
        [
            14,
            12],
        [
            12,
            8],
        [
            13,
            8],
        [
            11,
            8],
        [
            14,
            8],
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


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출진하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n여기까지 추격해 왔으니\n결코 질 수는 없지.')
    if stage.unitClickTest(20):
        stage.say('&20\n전장의 지형은 전과 크게 다르지 않습니다.\n&20\n여전히 강을 사이에 두고 대치합니다.')
    if stage.unitClickTest(19):
        stage.say('&19\n적벽에서 현무보옥을 얻었으니\n이제 모든 보옥을 모았습니다.\n&1\n그 현무보옥은 무슨 효과가 있소?\n&19\n도사 계열 부대가 사용하는 보옥입니다.\n&19\n적과 아군 모두의 상태를 바꿉니다.\n&15\n상태를 바꾼다고요?\n그게 무슨 뜻입니까?\n&23\n적을 중독시키거나 각종 능력을 낮출 수 있지요.\n반대로 능력이 올라갈 때도 있습니다.\n&1\n올라가기도 한다고……?\n그렇다면 적을 도울 수도 있단 말인가?\n&23\n그렇습니다.\n이 책략에서 가장 어려운 것은\n사용할 때를 가늠하는 일이지요.\n&1\n너무 까다롭군!')
    if stage.unitClickTest(7):
        stage.say('&7\n주군, 부디 한번 써 보십시오.')
    if stage.unitClickTest(15):
        stage.say('&15\n결국 제가 쓸 수 있는 보옥은 없군요.\n분명 지계 책략을 쓸 수 있는데……?\n어째서 그런 겁니까!')
    if stage.unitClickTest(23):
        stage.say('&23\n유수구를 함락하면 건업만 남습니다.\n동오도 이번 싸움에는 전력을 다해 결사 항전할 것입니다.')
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
