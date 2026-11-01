# Source Generated with Decompyle++
# File: R_56.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.bgSound(0)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 68, 135)
    stage.showHead(50, 144, 95)
    stage.bottomTxt('조조군', False, False, False)
    stage.delay(5)
    stage.head(0).move(80, 95)
    stage.delay(5)
    stage.bottomTxt('양평관을 공격하고', True, False, False)
    stage.delay(5)
    stage.head(50).hide()
    stage.delay(5)
    stage.bottomTxt('관문을 지키던 촉군을 대파했다.', True, False, True)
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
            15,
            44,
            52,
            1]])
    stage.draw()
    stage.setEventName('적은 오장원에서')
    stage.setStageName('양평관  조조군 본진')
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n마침내 양평관을 함락했군.\n모두 훌륭히 싸워 주었다.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n가후가 이미 장안에 도착했을 테니\n촉군도 달아날 길은 없을 것입니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n음.')
    stage.delay(5)
    stage.showUnit(19, 54, 22, 2)
    stage.say('&19\n주군!')
    stage.unit(19).move(54, 58, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n허도의 초선에게서 급보가 왔습니다!\n&0\n음, 무슨 일이냐?\n&19\n허도에 있던 서서가 종적을 감췄습니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('&0\n뭐라고!?')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n서서!')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n또한 서서의 집에서\n마왕의 부활에 관한 기도문과\n공명이 서서에게 직접 쓴 서찰이 다수 발견되었다 합니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n……서서가 줄곧 공명과 내통하고 있었군……')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n서서는 모친의 죽음에 계속 원한을 품고 있다가\n공명과 내통하여 우리에게 복수하려는 것이겠지요!')
    stage.unit(21).setAction(0)
    stage.say('&21\n나는 대체 무슨 짓을 한 것인가……\n서서의 노모를 죽게 만들다니……\n모두 그때 내 생각이 짧았던 탓이다……')
    stage.unit(0).setAction(3)
    stage.say('&0\n정욱, 더는 자책하지 말게.')
    stage.unit(0).setAction(0)
    stage.say('&0\n지금은 먼저 서서를 찾아\n사건의 전말을 묻는 수밖에 없군.')
    stage.unit(19).setAction(6)
    stage.say('&19\n초선이 서서의 행방을 찾고 있으니\n소식이 있으면 연락해 올 것입니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n음, 좋다.\n초선에게 희망을 걸 수밖에 없겠군.')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n이미 서쪽과 남쪽 제단에서 기도를 마쳤으니\n서서가 동쪽 제단에서 기도 중이라면…….')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n그렇습니다.\n마지막으로 남은 북쪽 제단의 기도마저 끝나면\n마왕 본래의 힘이 깨어날 것입니다.')
    stage.unit(19).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n무슨 일이 있어도 그들을 막아야 한다.\n어서 북쪽 제단의 위치를 찾아라!')
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.showUnit(182, 54, 22, 2)
    stage.delay(3)
    stage.unit(182).setAction(11)
    stage.say('&182\n주군, 공명을 찾았습니다!')
    stage.unit(0).setAction(3)
    stage.say('&0\n드디어 찾았단 말인가!?\n공명은 지금 어디 있느냐?')
    stage.unit(0).setAction(0)
    stage.say('&182\n촉군의 잔병을 수습해 오장원으로 가는 도중입니다.')
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다, 알겠다.\n우리도 오장원으로 가자!')
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(1, 0)
    stage.showHead(0, 94, 99)
    stage.draw()
    stage.bottomTxt('조조군은,', False, False, False)
    stage.delay(5)
    stage.showHead(35, 169, 32)
    stage.delay(5)
    stage.bottomTxt('오장원으로 향하다', True, False, False)
    stage.delay(5)
    stage.head(0).move(105, 32)
    stage.delay(5)
    stage.bottomTxt('제갈량을 추격했다.', True, False, True)
    stage.bgSound(12)
    stage.delay(3)
    stage.loadBg(2, 66)
    stage.draw()
    stage.showUnits([
        [
            187,
            5,
            56,
            1],
        [
            17,
            5,
            48,
            1]])
    stage.info('초선은 황건교도 첩자와 함께 허도를 수색했다.')
    stage.setStageName('허도  길모퉁이')
    stage.unitsMove([
        [
            stage.unit(187),
            48,
            56,
            1],
        [
            stage.unit(17),
            48,
            48,
            1]])
    stage.unit(187).setAction(0)
    stage.delay(5)
    stage.unit(187).setAction(0, 0)
    stage.say('&187\n허도를 지키던 서서가\n공명과 내통하고 있었다니……')
    stage.unit(17).setAction(0, 2)
    stage.say('&17\n아직 서서 선생께서 반란을 일으키셨는지는 확실하지 않습니다.')
    stage.unit(187).setAction(0, 1)
    stage.say('&187\n허도에 별다른 일은 없지만\n서서의 집은 텅 비어 있었습니다.\n게다가 그 서찰까지…….\n&17\n우선 서서 선생을 찾아요.')
    stage.unit(17).setAction(0, 1)
    stage.say('&17\n집 안의 사정을 보아하니\n서서 선생께서는 멀리 가지 않으셨을 거예요.\n분명 허도 어딘가에 계실 겁니다.')
    stage.showUnit(188, 5, 48, 1)
    stage.unit(188).move(40, 48, 1)
    stage.unit(188).setAction(0)
    stage.delay(3)
    stage.unit(187).setAction(0, 3)
    stage.unit(17).setAction(0, 3)
    stage.say('&188\n초선 낭자, 아무리 찾아도 서서가 보이지 않소.\n대체 어디로 갔을 것 같소?')
    stage.delay(10)
    stage.showUnit(151, 95, 52, 3)
    stage.unit(151).move(76, 52, 3)
    stage.unit(151).setAction(0)
    stage.delay(5)
    stage.unit(187).setAction(0, 1)
    stage.say('&187\n아!\n&188\n저기 저 사람……서서다!')
    stage.unitsMove([
        [
            stage.unit(17),
            66,
            48,
            1],
        [
            stage.unit(187),
            66,
            56,
            1],
        [
            stage.unit(188),
            58,
            48,
            1]])
    stage.unit(187).setAction(0)
    stage.unit(188).setAction(0)
    stage.say('&187\n서서!\n여기 숨어 있었구나.\n&188\n달아날 수 없다!\n어서 순순히 포박을 받아라!')
    stage.unit(151).setAction(6)
    stage.say('&151\n초선 낭자가 허도에 계시다고 들어\n이렇게 찾아왔습니다.\n&151\n승상께서도 공명의 일을 알아차리셨겠지요.')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n역시 선생께서는 알고 계시는군요.\n서서 선생, 사실을 말씀해 주실 수 없으신가요?')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('&151\n좋습니다.\n초선 낭자, 제가 숨은 것은 공명을 속이기 위해서입니다.')
    stage.unit(151).setAction(0)
    stage.say('&187\n뭐라고!\n&188\n그런 말을 누가 믿겠나!')
    stage.unit(17).setAction(0, 2)
    stage.say('&17\n두 분 다 조급해하지 마세요.\n우선 말씀부터 들어 보시지요.')
    stage.unit(17).setAction(0, 1)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n서서 선생, 계속 말씀해 주세요!')
    stage.unit(17).setAction(0)
    stage.delay(5)
    stage.unit(151).setAction(0, 2)
    stage.say('&151\n제 집에 있던 공명의 서신을 보셨겠지요?\n&151\n그렇습니다. 공명이 제 조건을 들어주는 대신\n저도 마왕의 부활을 돕기로 약속했습니다.')
    stage.unit(17).setAction(6)
    stage.say('&17\n선생께서 받아들이셨다고요!?\n서서 선생 같은 분께서 어찌……?\n대체 무슨 조건이었나요……?')
    stage.unit(151).setAction(0, 3)
    stage.say('&151\n그 조건이란……')
    stage.delay(5)
    stage.unit(151).setAction(0, 2)
    stage.say('&151\n그만두자. 아직은 알릴 필요가 없다.')
    stage.unit(151).setAction(0, 3)
    stage.delay(3)
    stage.unit(151).setAction(6)
    stage.say('&151\n한때 공명의 부탁을 받아들이기는 했으나\n아무래도 그런 일을 할 수는 없었습니다.\n제가 어찌 마왕의 부활을 돕겠습니까?\n&17\n서서 선생…….\n&151\n초선 낭자.\n저는 이미 마왕의 부활을 막을 계책을 세웠습니다.\n&151\n그리고 허도 동쪽에\n제단을 세워 거짓으로 기도를 올렸지요.')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n그 말씀은……?')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(0, 2)
    stage.say('&151\n지금 허도 상공에 요기가 가득한 것은\n제가 거짓으로 기도를 올렸기 때문입니다.\n&151\n공명이 이 요기를 느끼면\n기도가 끝났다고 여길 것입니다.\n&151\n실은 마왕이 부활할 때와\n똑같은 요기를 만들어 냈을 뿐이지요.')
    stage.unit(17).setAction(6)
    stage.say('&17\n마왕을 부활시키는 기도는 아직 시작하지 않았다는 말씀이신가요?')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(0, 3)
    stage.say('&151\n그렇습니다.\n&188\n그렇군.\n그렇다면 지금 공명은\n북쪽 제단에만 온 신경을 쓰고 있겠군.')
    stage.unit(151).setAction(6)
    stage.say('&151\n북쪽 제단은 오장원에 있습니다.')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(151).setAction(0, 2)
    stage.say('&151\n공명은 벌써 오장원에 도착했을지도 모릅니다.\n&188\n정말입니까!?')
    stage.unit(151).setAction(0, 3)
    stage.say('&151\n공명의 서신대로라면……\n제단은 오장원의 지하에 있을 것입니다.')
    stage.unit(151).setAction(6)
    stage.say('&151\n저는 지금 오장원으로 가서\n제단의 위치를 알아내겠습니다.\n그곳이 바로 공명의 마지막 목적지입니다.')
    stage.unit(151).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n알겠습니다.\n감사합니다, 서서 선생.')
    stage.unit(17).setAction(0)
    stage.say('&187\n초선 낭자, 그럼 저희는 즉시 장안으로 가서\n가후 선생께 이 일을 알리겠습니다.\n&188\n좋아. 서두르자.')
    stage.unit(17).setAction(0, 2)
    stage.say('&17\n두 분께 수고를 끼쳤습니다.\n그럼 소녀는 즉시 승상께 전하겠습니다.')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene2():
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
    stage.setStageName('오장원  조조군 본진')
    stage.delay(3)
    stage.showUnit(23, 54, 22, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n주군, 가후가 돌아왔습니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(23).move(54, 52, 2)
    stage.delay(3)
    stage.unit(23).setAction(0, 3)
    stage.say('&23\n……오, 초선 낭자!\n벌써 돌아오셨습니까?')
    stage.unit(17).setAction(6)
    stage.say('&17\n그렇습니다.')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n오, 가후. 수고했네.\n자초지종은 초선에게 들었네.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(0, 2)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n예. 그럼 즉시 출병해 공명을 섬멸하시겠습니까……?')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n음.\n마왕이 부활하지 않는다는 것을 알았으니\n이제 공명도 두려울 것이 없다!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(23).setAction(6)
    stage.say('&23\n주군, 아직 방심하시면 안 됩니다.\n&23\n사실 장안에서\n부활 전 마왕의 능력을 기록한 문헌을 찾았습니다.\n&23\n목인과 토우 외에도\n특정한 장소에서는 죽은 자를 조종할 수 있다고 합니다.')
    stage.unit(23).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n마왕이 아무리 강하다 한들\n반드시 없애야 한다.\n장졸들은 출진을 준비하라!')
    stage.unit(0).setAction(0)
    stage.unit(23).move(64, 52, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15)
    gvars[4076] = 0
    gvars[4057] = 56
    stage.setBattlePos([
        [
            4,
            6],
        [
            3,
            6],
        [
            5,
            6],
        [
            2,
            6],
        [
            4,
            5],
        [
            3,
            5],
        [
            5,
            5],
        [
            2,
            5],
        [
            4,
            4],
        [
            3,
            4],
        [
            5,
            4],
        [
            2,
            4],
        [
            4,
            3],
        [
            3,
            3],
        [
            5,
            3]])
    return False


def scene3():
    if stage.battleTest():
        stage.say('&0\n출발하라!')
        stage.setMenuVisible(False)
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n용서해 주십시오, 주군.\n오장원은 공명의 경계가 너무 삼엄하여\n전장 지형을 자세히 알아내지 못했습니다.\n&0\n음, 어쩔 수 없는 일이네.\n&20\n지금은 전장이 강으로 둘로 나뉜다는 것만 압니다.\n&0\n강이라……?\n그렇다면 수전을 치를 수도 있겠군?\n&20\n예.')
    if stage.unitClickTest(19):
        stage.say('&19\n서서가 말하기를 제단은 지하에 있다 했으니\n반드시 그곳으로 통하는 비밀 통로가 있을 것입니다.')
    if stage.unitClickTest(17):
        stage.say('&17\n서서 선생께서는 공명에게 조건을 제시하고\n그 대가로 마왕 부활의 기도를 돕기로 하셨어요.\n&1\n뭐라고? 대체 무슨 조건인가?\n&17\n서서 선생께서는 밝힐 수 없다고만 하셨어요……\n&1\n음.\n이제는 그런 것까지 따질 겨를이 없군.\n&0\n……서서의 조건이라?')
    if stage.unitClickTest(3):
        stage.say('&0\n관우, 오장원에 가 본 적이 있는가?\n&3\n유감스럽게도 가 본 적이 없습니다.\n줄곧 형주를 지키고 있었으니까요.\n&0\n그랬군.\n허허, 그대에게 물을 일이 아니었구나.')
    if stage.unitClickTest(1):
        stage.say('&1\n하늘이 심상치 않게 변하는군.\n폭풍이 오려는지도 모르겠어.')
    if stage.unitClickTest(23):
        stage.say('&23\n양평관에서 백호보옥을 얻으셨다지요!\n&0\n음.\n이것으로 사신보옥을 모두 모았군.\n&23\n백호보옥은 풍수사 계열 부대가 씁니다.\n&23\n그 신력은 모든 부대의 체력을 회복하고\n상태 이상도 치료하는 것으로 알고 있습니다.\n&1\n그렇다면 제법 쓸모 있는 물건이군!')
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
