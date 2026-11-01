# Source Generated with Decompyle++
# File: R_29.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    stage.loadBg(2, 49)
    stage.showUnits([
        [
            32,
            54,
            73],
        [
            35,
            40,
            57,
            1],
        [
            3,
            67,
            57,
            3],
        [
            33,
            67,
            51,
            3],
        [
            34,
            67,
            45,
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setEventName('관우의 맹약')
    stage.setStageName('강하  유비 저택')
    stage.say('&32\n공명, 동오와 조위의 동태는 어떠한가?')
    stage.unit(35).setAction(0, 2)
    stage.say('&35\n주군께 아룁니다.\n동오가 순풍을 이용하여\n대대적인 화공으로 조조군을 공격했습니다.')
    stage.say('&35\n조조군은 반드시 대패할 것입니다.\n그때 달아나는 패잔병을 추격하시지요.\n&32\n음, 모두 그대에게 맡기겠네.')
    stage.unit(35).setAction(0, 2)
    stage.say('&35\n예! 그럼 즉시 병력을 동원하시지요.')
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n자룡, 익덕은 명을 들으시오!')
    stage.unit(34).setAction(6)
    stage.say('&34\n여기 있습니다.')
    stage.unit(33).setAction(3)
    stage.say('&33\n여기 있소.')
    stage.unit(34).setAction(0)
    stage.unit(33).setAction(0)
    stage.say('&35\n조조군은 반드시 형주로 달아날 것이다.\n너희는 미리 그곳으로 가서……')
    stage.unit(35).setAction(11)
    stage.say('&35\n이렇게 하시지요…….')
    stage.unit(34).setAction(6)
    stage.say('&34\n군사의 묘책입니다.')
    stage.unit(33).setAction(3)
    stage.say('&33\n알겠소!\n내게 맡기시오.')
    stage.unit(35).setAction(0)
    stage.say('&35\n그렇다면 즉시 출발하십시오.')
    stage.unit(3).setAction(2)
    stage.say('&3\n군사!')
    stage.unit(33).setAction(0)
    stage.unit(34).setAction(0)
    stage.delay(3)
    stage.unit(33).setAction(0, 2)
    stage.unit(34).setAction(0, 2)
    stage.delay(5)
    stage.say('&35\n운장 장군, 무슨 일이십니까?')
    stage.unit(3).setAction(0)
    stage.say('&3\n군사, 익덕과 자룡에게는 명을 내리시면서\n내게는 아무 명도 내리지 않으셨소.\n어찌 된 일이오?')
    stage.unit(35).setAction(11)
    stage.say('&35\n아, 그 일이었군요?')
    stage.unit(35).setAction(0)
    stage.say('&35\n실은 장군께 부탁드릴 일이 많습니다만\n조금 마음에 걸리는 것이 있습니다.')
    stage.unit(3).setAction(3)
    stage.say('&3\n무슨 걱정인가?')
    stage.unit(3).setAction(0)
    stage.effectSound(203, 255)
    stage.bgSound(19)
    stage.delay(3)
    stage.say('&35\n그대는 지난날 조조에게 후한 대접을 받았으니\n그 은혜를 아직 기억하겠지요.')
    stage.unit(3).setAction(6)
    stage.say('&3\n지난번 원소와 싸울 때\n이미 그 은혜를 갚았습니다.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(35).setAction(0, 2)
    stage.say('&35\n그렇다면………\n조조가 처참한 꼴로 목숨을 구걸할 때\n그를 베실 수 있겠습니까?')
    stage.unit(3).setAction(2)
    stage.say('&3\n반드시 조조를 베겠습니다!\n베지 못한다면\n군법에 따라 처벌받겠습니다.')
    stage.unit(35).setAction(0, 1)
    stage.say('&35\n……군령장을 쓰실 수 있겠습니까?')
    stage.unit(3).setAction(0)
    stage.say('&3\n물론이오.')
    stage.say('&35\n좋습니다.')
    stage.say('&35\n제3대를 이끌고 익덕 장군의 뒤를 따르며\n마른 장작을 태워 연기를 피우시오. 조조군을 기습할 수 있도록.')
    stage.unit(3).setAction(3)
    stage.say('&3\n조조군이 연기를 보고\n앞에 적이 숨어 있음을 알면\n다른 길로 가지 않겠습니까?')
    stage.unit(3).setAction(0)
    stage.say('&35\n아닙니다. 조조는 병법에 밝은 사람입니다.\n연기를 보고 일부러 의심을 사려는 수라 여겨\n오히려 대담하게 나아올 것입니다.\n&3\n그렇군요.')
    stage.unit(35).setAction(0)
    stage.say('&35\n모두 이해했느냐?\n그럼 즉시 움직여라.')
    stage.unit(3).setAction(6)
    stage.say('&3\n예.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(34),
            67,
            10,
            0],
        [
            stage.unit(33),
            67,
            10,
            0],
        [
            stage.unit(3),
            67,
            10,
            0]])
    stage.unit(34).hide()
    stage.unit(33).hide()
    stage.unit(3).hide()
    stage.delay(5)
    stage.say('&32\n……공명.')
    stage.unit(35).setAction(0, 2)
    stage.say('&35\n예.\n&32\n둘째가 아무리 장담했다 해도\n조조를 죽이지는 못할 것이네.\n&35\n음, 아마 손을 대지 못하겠지요.\n&32\n그럼 군령장은 어찌할 텐가?')
    stage.unit(35).setAction(6)
    stage.say('&35\n운장 장군의 가슴에 옛 은의가 남아 있다면\n이번 기회에 매듭을 지으실 것입니다.')
    stage.unit(35).setAction(0)
    stage.say('&35\n운장 장군을 위해서이기도 합니다.\n&32\n공명, 참으로 멀리 내다보는군…….')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(15)
    stage.delay(3)
    stage.loadBg(2, 53)
    stage.showUnits([
        [
            0,
            54,
            80],
        [
            2,
            44,
            64,
            1],
        [
            21,
            64,
            64,
            3]])
    stage.draw()
    stage.setStageName('오림  조조군 본진')
    model.setFace(2)
    stage.unit(0).setAction(2)
    stage.say('&0\n가증스러운 오나라 개들!\n감히 나를 이토록 처참하게 무너뜨리다니!')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 5, [
        2,
        21,
        15,
        13])
    gvars[4076] = 0
    gvars[4057] = 29
    stage.setBattlePos([
        [
            34,
            19],
        [
            33,
            20],
        [
            34,
            19],
        [
            33,
            8],
        [
            32,
            8],
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


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(21):
        stage.say('&21\n형주로 가는 길은 두 곳입니다.\n어느 길을 택할지가 중요합니다.\n&0\n전체 지형은 어떠한가?\n&21\n산과 늪이 뒤섞여 지형이 복잡하므로\n빠르게 움직이기 어렵습니다.\n&0\n음………')
    if stage.unitClickTest(2):
        stage.say('&2\n주군, 피해가 생각보다 큽니다.\n다른 이들이 어찌 되었는지도 알 수 없습니다.\n&0\n음………\n우선 최대한 빨리 이곳을 빠져나간 뒤\n합류할 방도를 찾아야겠군.\n&0\n문원, 이번 패배는\n내가 동오를 너무 얕본 탓이네.')
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
