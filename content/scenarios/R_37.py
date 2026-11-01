# Source Generated with Decompyle++
# File: R_37.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
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
            13,
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
            21,
            64,
            52,
            3]])
    stage.draw()
    stage.setEventName('군량고 수비')
    stage.setStageName('한수 부근  조조군 본진')
    stage.say('&0\n공명의 목표가 사곡의 군량고였을 줄이야.\n너무 방심했군.')
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n그자는 용병술이 신출귀몰합니다……\n우리 군이 가장 경계해야 할 인물입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n군량 창고를 빼앗기면\n우리 군은 오도 가도 못하게 됩니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n음, 촉군에게 군량 창고를 빼앗길 수는 없다.\n즉시 야곡으로 향하라!')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 8)
    gvars[4076] = 0
    gvars[4057] = 37
    stage.setBattlePos([
        [
            9,
            23],
        [
            10,
            23],
        [
            8,
            23],
        [
            11,
            23],
        [
            8,
            23],
        [
            11,
            23],
        [
            9,
            23],
        [
            10,
            23],
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


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(13):
        stage.say('&13\n군량고를 습격한 것은 마초의 부대인 듯합니다.\n&0\n마초인가? 한수에 보이지 않아서\n그때부터 이상하게 여겼는데\n사곡으로 간 것이었군.')
    if stage.unitClickTest(20):
        stage.say('&20\n사곡은 이름처럼\n두 산 사이에 끼어 있습니다.')
    if stage.unitClickTest(19):
        stage.say('&19\n마초의 부대만이라면 대수롭지 않으나\n유비도 주력을 이끌고 야곡으로 갔을까 염려됩니다.\n&19\n다만 유비의 주력이 언제 도착할지는 알 수 없습니다……\n어찌 되었든……힘겨운 싸움이 될 것입니다.')
    if stage.unitClickTest(21):
        stage.say('&21\n오호장 중 한 사람인 마초 말입니까?\n&15\n예, 뭐라고요?\n오호장이 무엇입니까?\n&20\n음……\n관우, 장비, 조운, 황충, 마초.\n촉에서는 이 다섯 대장을 오호장이라 부릅니다.\n&21\n그렇습니다.\n&15\n아……그렇습니까?')
    if stage.unitClickTest(15):
        stage.say('&15\n군량고는 반드시 지켜야 합니다!\n군량보다 중요한 건 없지요.\n&1\n허저, 참으로 자네다운 말이군.\n&15\n……어쩌겠습니까. 워낙 많이 먹으니 말입니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n맹덕, 서둘러 움직여야 하네.\n군량 창고를 촉군에게 빼앗기면\n결국 한중을 포기할 수밖에 없어.\n&0\n음.')
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
