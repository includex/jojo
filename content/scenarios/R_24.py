# Source Generated with Decompyle++
# File: R_24.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(16)
    stage.delay(3)
    stage.setMenuVisible(False)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 398, 36)
    stage.showHead(113, 462, 36)
    stage.bottomTxt('조조', False, False, False)
    stage.delay(5)
    stage.head(113).hide()
    stage.delay(5)
    stage.bottomTxt('원소의 장남 원담을 없애고', True, False, False)
    stage.delay(5)
    stage.head(0).move(424, 36)
    stage.delay(5)
    stage.bottomTxt('남피를 점령했다.', True, False, False)
    stage.delay(8)
    stage.bottomTxt('이때,', True, True, False)
    stage.delay(5)
    stage.showHead(115, 490, 0)
    stage.delay(5)
    stage.bottomTxt('원상', True, True, False)
    stage.delay(5)
    stage.showHead(114, 554, 0)
    stage.delay(5)
    stage.bottomTxt('그리고 원소의 둘째 아들 원희', True, False, False)
    stage.delay(5)
    stage.bottomTxt('유성으로 달아나는 중이었다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 42)
    stage.showUnits([
        [
            0,
            54,
            70],
        [
            1,
            35,
            56,
            1],
        [
            15,
            35,
            50,
            1],
        [
            2,
            35,
            44,
            1],
        [
            23,
            35,
            38,
            1],
        [
            20,
            71,
            56,
            2],
        [
            22,
            71,
            50,
            2],
        [
            24,
            71,
            44,
            2],
        [
            21,
            71,
            38,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('유성으로 가는 길')
    stage.setStageName('남피  임시 승상부')
    stage.say('&0\n그런가? 원상과 원희가 유성으로 달아나고 있다고?')
    stage.unit(20).setAction(6)
    stage.say('&20\n예.\n&20\n천연의 요새에 기대 끝까지 저항하려는 듯합니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n음.\n단숨에 함락하는 수밖에 없겠군.')
    stage.unit(2).setAction(0, 2)
    stage.say('&2\n주군.\n&0\n무슨 일인가, 문원?\n&2\n원양 장군과 함께\n유성의 지형을 살펴보고 왔습니다.\n&0\n음, 어떻던가?\n&2\n길이 매우 험하여\n대군은 이동할 수 없습니다.\n&0\n음……')
    stage.unit(2).setAction(0, 1)
    stage.delay(3)
    stage.unit(22).setAction(6)
    stage.say('&22\n소수의 정예를 골라 공격하는 수밖에 없군요.')
    stage.unit(22).setAction(0)
    stage.say('&0\n하지만 이번 싸움은 위험하겠군.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n원씨도 거의 멸한 듯하니\n이제 허도로 돌아가자……')
    stage.unit(22).setAction(6)
    stage.say('&22\n주군, 절대 안 됩니다.')
    stage.unit(22).setAction(0)
    stage.say('&22\n원씨는 하북에 깊이 뿌리내린 가문입니다.\n&22\n지금 뿌리까지 뽑지 않으시면\n훗날 반드시 후회하실 것입니다!\n&0\n음……')
    stage.delay(10)
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n음……좋다.\n유성으로 진군한다.')
    stage.unit(22).setAction(6)
    stage.say('&22\n예.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 424, 36)
    stage.showHead(115, 490, 0)
    stage.showHead(114, 554, 0)
    stage.delay(8)
    stage.head(0).move(426, 0)
    stage.delay(5)
    stage.bottomTxt('조조군은 유성으로 진격했다.', False, False, True)
    stage.bgSound(15)
    stage.loadBg(2, 21)
    stage.showUnits([
        [
            0,
            49,
            67],
        [
            1,
            38,
            54,
            1],
        [
            15,
            38,
            48,
            1],
        [
            2,
            38,
            42,
            1],
        [
            20,
            60,
            54,
            3],
        [
            22,
            60,
            48,
            3],
        [
            21,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('유성 근교  조조군 본진')
    stage.say('&0\n유성으로 가는 길은 이곳보다 더 험하니\n여기서 출전할 부대를 정하도록 하겠다.')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n주군, 저를 출전시켜 주십시오.\n&0\n곽가……?\n병은 다 나았는가?')
    stage.unit(22).setAction(6)
    stage.say('&22\n염려 마십시오.\n이미 다 나았습니다……\n&0\n그런가………다행이군.\n몸을 잘 보살피도록 하게.\n&22\n예.')
    stage.unit(22).setAction(0)
    stage.say('&22\n(주군께서 알아차리시면 안 된다.\n………내게는………\n이번이 마지막 싸움일지도 모르니……)')
    stage.unit(22).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 7, [
        22,
        2])
    gvars[4076] = 0
    gvars[4057] = 24
    stage.setBattlePos([
        [
            6,
            16],
        [
            5,
            15],
        [
            7,
            17],
        [
            7,
            15],
        [
            6,
            14],
        [
            8,
            16],
        [
            5,
            17],
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
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 전장의 지형은 어떠한가?\n&20\n강 하나가 전장의 위아래를 가르고 있습니다.\n&20\n또한 유성으로 곧장 이어지는 오솔길이 있습니다만\n이런 지형에는 복병이 숨어 있을 가능성이 큽니다.\n&0\n복병이라……? 분명 그렇겠군.')
    if stage.unitClickTest(2):
        stage.say('&2\n주군…이번 싸움은\n어쩐지 마음이 놓이지 않습니다.')
    if stage.unitClickTest(1):
        stage.say('&1\n참, 맹덕.\n정찰하다가 한 가지 이야기를 들었네.\n&0\n무슨 이야기인가?\n&1\n요즘 마을 아이들이 호랑이를 보았다더군.\n&0\n호랑이?\n&1\n음.\n다만 아이들이 하는 말이라…….\n&0\n그런가…….\n너무 신경 쓸 필요는 없겠지.\n&0\n설령 호랑이가 있다 해도\n군대를 습격한다는 소리는 들어 본 적 없으니.\n&1\n그렇지.')
        stage.unit(22).setAction(0, 2)
        stage.say('&22\n……호랑이라고……?')
        stage.unit(22).setAction(0, 3)
    if stage.unitClickTest(15):
        stage.say('&15\n부디 저를 출전시켜 주십시오.')
    if stage.unitClickTest(22):
        stage.say('&0\n곽가.\n이번 싸움이 끝나면 한동안 쉬도록 하게.\n&22\n예?\n&0\n아무래도 좀 쉬면서\n어서 몸을 추스르는 게 좋겠네.\n&22\n예…감사합니다, 주군.\n&22\n……주군.\n&0\n음? 무슨 일인가?\n&22\n아, 아뇨……아무것도 아닙니다.\n하하하하.\n&0\n음?\n오늘따라 참 이상하군.')
    if stage.unitClickTest(21):
        stage.say('&21\n원씨를 멸하려면 지금밖에 없습니다.')
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
