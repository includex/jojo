# Source Generated with Decompyle++
# File: R_41.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    stage.clsUnit()
    stage.bgSound(0)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 100, 120)
    stage.showHead(35, 36, 120)
    stage.bottomTxt('촉군', False, False, False)
    stage.delay(5)
    stage.head(35).move(9, 181)
    stage.delay(5)
    stage.bottomTxt('마지막 거점인 성도로 패주했다.', True, False, True)
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
            8,
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
    stage.setEventName('유비가 세운 나라')
    stage.setStageName('성도 교외  조조군 본진')
    if vars[740]:
        stage.say('&0\n마침내 여기까지 왔군.')
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('&19\n성도가 눈앞입니다.\n단숨에 함락하시지요.')
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n옳은 말씀입니다만\n아직 행군에 필요한 군량을 확보하지 못했습니다.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(0, 2)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('&21\n그렇습니다.\n단기 결전 외에는 길이 없습니다.')
        stage.unit(21).setAction(0)
        stage.say('&0\n원양, 자네가 선봉을 맡게.')
        stage.unit(1).setAction(0, 2)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n오, 내게 맡기게.')
        stage.unit(1).setAction(0)
        stage.say('&0\n자환, 너도 출전하거라.\n조창은 이미 내보냈다.')
        stage.unit(8).setAction(0, 2)
        stage.delay(3)
        stage.say('&8\n예.')
        stage.unit(0).setAction(17)
        stage.say('&0\n촉은 유비가 제 손으로 세운 나라다.\n그에 대한 경의로\n우리 조씨 일족이 직접 촉을 멸해야 한다.')
        stage.unit(0).setAction(0)
        stage.unit(1).setAction(0, 1)
        stage.unit(8).setAction(0, 1)
        stage.unit(20).setAction(0, 3)
        stage.unit(19).setAction(0, 3)
        stage.unit(21).setAction(0, 3)
    if not vars[740]:
        stage.say('&0\n마침내 여기까지 왔군.')
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('&19\n허나 무리하게 행군한 탓에\n많은 부대가 뒤처졌습니다.')
        stage.unit(19).setAction(0)
        stage.delay(3)
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n그건 그렇고, 군량은 충분히 확보했습니다만……')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(21).setAction(0, 2)
        stage.delay(3)
        stage.unit(21).setAction(6)
        stage.say('&21\n길고 힘겨운 싸움이 될 것입니다.\n절대 조급해해서는 안 됩니다.')
        stage.unit(21).setAction(0)
        stage.say('&0\n음.\n원양, 그대가 선봉을 맡아라.')
        stage.unit(1).setAction(0, 2)
        stage.delay(3)
        stage.unit(1).setAction(3)
        stage.say('&1\n오, 내게 맡기게.')
        stage.unit(1).setAction(0)
        stage.say('&0\n자환, 너도 출전하거라.\n조창은 이미 내보냈다.')
        stage.unit(8).setAction(0, 2)
        stage.say('&8\n예.')
        stage.unit(0).setAction(17)
        stage.say('&0\n촉은 유비가 제 손으로 세운 나라다.\n그에 대한 경의를 표하는 뜻에서\n우리 조씨 일족이 직접 촉을 멸하겠다.')
        stage.unit(0).setAction(0)
        stage.unit(1).setAction(0, 1)
        stage.unit(8).setAction(0, 1)
        stage.unit(20).setAction(0, 3)
        stage.unit(19).setAction(0, 3)
        stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    if vars[740]:
        gvars[4076] = 0
        gvars[4057] = 41
        stage.setJoinBattle(1, 15, [
            1,
            4,
            5,
            12,
            8])
        stage.setBattlePos([
            [
                19,
                3],
            [
                20,
                3],
            [
                18,
                3],
            [
                21,
                3],
            [
                17,
                3],
            [
                19,
                2],
            [
                20,
                2],
            [
                18,
                2],
            [
                21,
                2],
            [
                17,
                2],
            [
                19,
                1],
            [
                20,
                1],
            [
                18,
                1],
            [
                21,
                1],
            [
                17,
                1]])
    if not vars[740]:
        gvars[4076] = 0
        gvars[4057] = 41
        stage.setJoinBattle(1, 13, [
            1,
            4,
            5,
            12,
            8])
        stage.setBattlePos([
            [
                19,
                3],
            [
                20,
                3],
            [
                18,
                3],
            [
                21,
                3],
            [
                17,
                3],
            [
                19,
                2],
            [
                20,
                2],
            [
                18,
                2],
            [
                21,
                2],
            [
                17,
                2],
            [
                19,
                1],
            [
                20,
                1],
            [
                18,
                1],
            [
                21,
                1],
            [
                17,
                1]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출병한다.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n일족 모두에게 출전을 명하겠다고……?\n맹덕, 자네 마음은 이해하네만……')
    if stage.unitClickTest(20):
        stage.say('&20\n성도성 전체가 전장이 될 것입니다.\n과연 촉의 도읍답습니다.\n&20\n제가 보아 온 성들보다 규모가 훨씬 큽니다.')
    if stage.unitClickTest(19):
        if vars[740]:
            stage.say('&19\n장기전을 치르기에는\n우리 군의 군량이 부족합니다.\n주군께서는 이 점을 유념해 주십시오.')
        if not vars[740]:
            stage.say('&19\n적은 병력으로 공성전을 벌이면\n많은 시간이 걸릴 것이니\n신중히 공격해야 합니다.')
    if stage.unitClickTest(21):
        stage.say('&21\n지난 싸움에서 얻은 청룡보옥에는\n청룡의 힘이 깃들어 있습니다.\n&0\n청룡의 힘이라고?\n&20\n오, 뇌전을 부르는 기이한 책략 말입니까?\n&21\n예.\n다만 기마책사 계열만 사용할 수 있습니다.\n&0\n기마책사라……\n그렇다면 사마의에게 장비시키게.')
    if stage.unitClickTest(8):
        stage.say('&0\n자환, 너도 잘 알고 있겠지.\n너는 우리 조씨 가문의 맏아들이다.\n가문의 명성을 더럽히지 마라.\n&8\n예, 아버님.')
    if stage.unitClickTest(15):
        stage.say('&15\n부디 저를 출전시켜 주십시오.')
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
