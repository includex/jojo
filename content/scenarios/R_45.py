# Source Generated with Decompyle++
# File: R_45.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(0)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 406, 212)
    stage.showHead(76, 470, 212)
    stage.bottomTxt('험한 요충지 유수구를 빼앗긴 오군은', False, False, False)
    stage.delay(8)
    stage.head(76).move(513, 184)
    stage.delay(5)
    stage.bottomTxt('도읍 건업으로 퇴각했다.', True, False, False)
    stage.delay(10)
    stage.head(0).move(449, 184)
    stage.delay(10)
    stage.bottomTxt('오랫동안 이어진 전란은', True, True, False)
    stage.delay(10)
    stage.bottomTxt('곧 끝나겠군.', True, False, True)
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
            8,
            44,
            58,
            1],
        [
            15,
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
    stage.setEventName('통일 전야')
    stage.setStageName('유수구  조조군 본진')
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n주군, 마침내 이날이 왔습니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n공명은 이제 독 안에 든 쥐다.')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n적이 무슨 계책을 쓰든\n거침없이 공격하면 됩니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n상대도 틀림없이 그렇게 생각할 것입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(8).setAction(0, 2)
    stage.say('&8\n아버님, 천하를 평정할 마지막 싸움입니다.\n한 말씀 해 주십시오.')
    stage.say('&0\n유비와 원소를 비롯한 수많은 영웅도\n그 시절에는 이 싸움을 목표로 삼았었지.')
    stage.unit(0).setAction(3)
    stage.say('&0\n우리 가슴속에 그들의 열정과 포부가 살아 있으니\n더는 말이 필요 없겠지.')
    stage.unit(0).setAction(0)
    stage.say('&0\n이제 남은 것은 싸움뿐이다.\n그들의 염원을 이루어 주자.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n예.\n모두 같은 생각입니다.\n주군, 천하 통일의 대도를 향해 나아가십시오.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n음.\n출전을 준비하라!')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.unit(1).setAction(0, 1)
    stage.unit(8).setAction(0, 1)
    stage.unit(19).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 45
    stage.setBattlePos([
        [
            19,
            1],
        [
            1,
            20],
        [
            38,
            19],
        [
            20,
            1],
        [
            1,
            19],
        [
            38,
            20],
        [
            18,
            1],
        [
            1,
            21],
        [
            38,
            18],
        [
            21,
            1],
        [
            1,
            18],
        [
            38,
            21],
        [
            19,
            0],
        [
            0,
            20],
        [
            39,
            19]])
    return False


def scene1():
    if stage.unitClickTest(20):
        stage.say('&20\n건업은 성도보다도 큰 성입니다.\n&20\n성벽이 사중으로 되어 있어\n지키기는 쉽고 치기는 어려운 요새지요.\n&0\n지키기는 쉽고 치기는 어렵다? 정말 그럴까……?\n&20\n우리 군이라면\n반드시 함락할 수 있습니다.')
    if stage.unitClickTest(8):
        stage.say('&8\n아버님, 천하 통일이 달린 싸움이니\n부디 소자를 출전시켜 주십시오.')
    if stage.unitClickTest(19):
        stage.say('&0\n사마의, 지금 하늘빛은 어떠한가?\n&19\n이미 주군의 빛깔로 물들었습니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n맹덕, 이제 자네의 천하가 눈앞에 보이는 듯하네.')
    if stage.unitClickTest(21):
        stage.say('&21\n주군,\n긴 난세를 끝낼 절호의 기회입니다.\n&0\n음.')
    if stage.unitClickTest(15):
        stage.say('&0\n허저, 긴장했는가?\n&15\n예?\n저……저는……그, 그다지 긴장하지 않았습니다……\n&0\n그래, 그렇다면 다행이군.')
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n좋다, 출진하라!')
        stage.bgSound(-1)
        return True

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
