# Source Generated with Decompyle++
# File: R_51.marshal (Python 3.9)


def scene0():
    '주유의 패주'
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 31)
    stage.showUnits([
        [
            76,
            5,
            51,
            1],
        [
            77,
            5,
            51,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('주유의 패주')
    stage.setStageName('유수구 교외  산길')
    stage.unit(76).move(15, 51, 1)
    stage.unitsMove([
        [
            stage.unit(76),
            68,
            51,
            1],
        [
            stage.unit(77),
            58,
            51,
            1]])
    stage.unit(77).setAction(0)
    stage.unit(76).setAction(0)
    stage.delay(3)
    stage.unit(76).setAction(2)
    stage.say('&76\n……제길!\n조조가 이토록 강할 줄이야!')
    stage.unit(76).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(3)
    stage.say('&77\n주군, 건업을 사수하며\n마지막 결전을 치르시지요!')
    stage.unit(76).setAction(0, 3)
    stage.delay(3)
    stage.unit(77).setAction(17)
    stage.say('&77\n동오의 모든 장병이 생명의 불꽃을 태워\n목숨을 걸고 조조군과 결전을 치르리라.')
    stage.unit(77).setAction(0)
    stage.delay(3)
    stage.unit(76).setAction(17)
    stage.say('&76\n음, 아버님과 형님이 피 흘려 세운 기반을\n내 손으로 무너뜨릴 수는 없다!')
    stage.unit(76).setAction(0)
    stage.delay(3)
    stage.unit(77).setAction(6)
    stage.say('&77\n주군의 말씀이 지당하십니다.\n그만한 결심을 하셨으니\n오나라는 반드시 승리할 것입니다.\n&77\n……으윽!\n콜록! 콜록!')
    stage.unit(76).setAction(3)
    stage.say('&76\n고……공근, 괜찮은가!?\n&77\n……걱정 마십시오. 건업으로 돌아가시지요.')
    stage.unit(76).setAction(0)
    stage.say('&76\n으, 음……')
    stage.unit(76).setAction(0, 1)
    stage.delay(5)
    stage.unit(77).setAction(17, 1)
    stage.say('&77\n(하늘이여, 시간을 조금만 더 주십시오.\n어찌 이대로 죽을 수 있단 말입니까!\n동오가 천하를 통일할 때까지 살아야 합니다!)')
    stage.unit(77).setAction(0, 1)
    stage.unitsMove([
        [
            stage.unit(76),
            95,
            51,
            1],
        [
            stage.unit(77),
            85,
            51,
            1]])
    stage.unit(76).hide()
    stage.unit(77).move(95, 51, 1)
    stage.delay(3)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(0)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 406, 212)
    stage.showHead(76, 470, 212)
    stage.bottomTxt('중요 거점 유수구를 잃은 오군은', False, False, False)
    stage.delay(5)
    stage.head(76).move(513, 184)
    stage.delay(5)
    stage.bottomTxt('근거지인 건업으로 퇴각했다.', True, True, True)
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
    stage.setStageName('유수구  조조군 본진')
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n맹덕, 드디어 오군을 궁지에 몰았군.\n지금쯤 건업의 손권은 얼굴이 새파랗게 질렸겠지.')
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n건업이 아무리 견고해도\n이제 오군에는 우리를 막을 힘이 없습니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n손권은 이제 궁지에 몰려\n달아날 곳이 없습니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n손권이 지금 세운 나라는 모두\n아버지 손견과 형 소패왕 손책의 유업을 이은 것이다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n이번 싸움은 몹시 힘겨울 것이다.\n모두 절대 방심하지 마라.')
    stage.unit(1).setAction(0)
    stage.say('&1\n우리도 알고 있네, 맹덕!')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다. 건업으로 진군하여 오를 멸한다!\n삼국이 맞선 형세를 깨뜨리겠다!')
    stage.unit(0).setAction(0)
    stage.unit(0).setAction(0, 0)
    stage.unit(1).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(23).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 51
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


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출진하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n건업성은 매우 거대합니다.\n성벽이 네 겹으로 둘러져 있고\n성 안에도 울타리가 설치되어 있습니다.\n&0\n과연 손씨 집안이 세운 성이로군.\n하지만 어떻게든 함락해야 한다.')
    if stage.unitClickTest(3):
        stage.say('&3\n반드시 오군을 섬멸하여\n죽은 의형제들의 원수를 갚겠다.')
    if stage.unitClickTest(19):
        stage.say('&19\n(적벽의 제단……?\n예전에 읽은 고문서에서\n비슷한 것을 본 듯한데……)')
    if stage.unitClickTest(1):
        stage.say('&1\n문득 손견이 그리워지는군.\n옛날 동탁을 토벌하던 때가 떠올라.\n&1\n그 아들도 결코 얕볼 수 없겠군.')
    if stage.unitClickTest(23):
        stage.say('&23\n주유는 아직 패를 다 드러내지 않은 듯합니다.\n&23\n틀림없이 다른 계략을 숨겨 두었겠지요.')
    if stage.unitClickTest(15):
        stage.say('&15\n공성전은 모두 땅 위에서 싸우니\n안심이 됩니다.\n&15\n꼭 이 허저를 출전시켜 주십시오!')
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
