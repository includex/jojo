# Source Generated with Decompyle++
# File: R_57.marshal (Python 3.9)


def scene0():
    '최후의 결전'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(0)
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
    stage.setEventName('최후의 결전')
    stage.setStageName('오장원  조조군 본진')
    stage.delay(3)
    stage.showUnit(151, 54, 22, 2)
    stage.delay(3)
    stage.say('&151\n……승상.')
    stage.unit(0).setAction(3)
    stage.say('&0\n오, 서서. 드디어 왔구려!')
    stage.unit(0).setAction(0)
    stage.unit(151).move(54, 64, 2)
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('&151\n지하로 통하는 비밀 통로를 찾았습니다.\n즉시 군사를 보내 공명을 추격해 주십시오.')
    stage.unit(0).setAction(3)
    stage.say('&0\n그렇소! 이 조조, 감사드리오.\n이번에는 원직의 큰 도움을 받았소.')
    stage.unit(0).setAction(0)
    stage.say('&151\n이 원직, 황송할 따름입니다…….')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다.\n즉시 공명을 추격한다.\n마지막 결전이니 모두 신중히 움직여라!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('&151\n승상, 이 원직은 물러가겠습니다……\n&0\n음.\n이번에는 큰 도움을 받았소.\n&151\n별말씀을요. 승상의 승전을 기원합니다.')
    stage.unit(151).setAction(0)
    stage.say('&151\n(벗이여, 공명……\n이제 나는 자네가 죽기를 바라고 있네.\n나를 원망하겠는가……?)')
    stage.delay(5)
    stage.unit(151).setAction(0, 1)
    stage.say('&151\n(아니……그대라면 나를 탓하지 않을 것이다.\n정녕 예전의 그대 그대로라면\n내가 한 모든 일에 찬성했을 것이다.)')
    stage.delay(5)
    stage.unit(151).setAction(0, 0)
    stage.say('&151\n(벗 공명이여…….\n진심으로 하늘에 빌겠네.\n부디 평온히 잠들게나.)')
    stage.unit(151).move(54, 22, 0)
    stage.unit(151).hide()
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 57
    stage.setBattlePos([
        [
            21,
            19],
        [
            18,
            18],
        [
            18,
            21],
        [
            21,
            20],
        [
            21,
            18],
        [
            21,
            21],
        [
            20,
            19],
        [
            20,
            20],
        [
            20,
            18],
        [
            20,
            21],
        [
            19,
            19],
        [
            19,
            20],
        [
            19,
            18],
        [
            19,
            21],
        [
            18,
            19]])
    return False


def scene1():
    if stage.battleTest():
        stage.say('&0\n출발하라!')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n우리는 지하 전장의 사정을 전혀 알지 못하니\n신중히 움직여야 승리할 수 있습니다.\n&0\n음.')
    if stage.unitClickTest(19):
        stage.say('&19\n공명……\n이런 상황에서 그대와 싸우고 싶지는 않았네.')
    if stage.unitClickTest(23):
        stage.say('&23\n공명은 어째서 제단을 지하에 지었을까요?\n설마……\n&0\n……지하는 햇빛이 들지 않는 곳이니\n죽은 자들이 드나들기에 가장 알맞은 곳일지도 모르지.\n&23\n주군께서 각별히 주의하셔야겠습니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n더 말할 것 없네.\n승리는 반드시 우리 것이야.')
    if stage.unitClickTest(3):
        stage.say('&3\n이번 싸움에 이기기만 하면\n형님께서 내게 맡기신 미래를 볼 수 있다.')
    if stage.unitClickTest(17):
        stage.say('&17\n(아버님, 어머님. 지켜봐 주세요.\n이것이 마지막 싸움이 될 거예요.\n봉선……제게 힘을 주세요……)\n&0\n무슨 일이냐, 초선?\n&17\n아? 아, 아무것도 아닙니다.\n승상, 명을 내려 주십시오.')
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
