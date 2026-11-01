# Source Generated with Decompyle++
# File: R_40.marshal (Python 3.9)


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
    stage.showHead(0, 160, 76)
    stage.showHead(35, 96, 76)
    stage.bottomTxt('양평관에서 패한 촉군은', False, False, False)
    stage.delay(5)
    stage.head(35).move(40, 130)
    stage.delay(5)
    stage.bottomTxt('검각으로 패주했다.', True, False, True)
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
            17,
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
    stage.setEventName('계속 진군한다')
    stage.setStageName('한중  조조군 본진')
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n맹덕, 해냈네!\n드디어 한중을 되찾았어.')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(19).setAction(0, 2)
    stage.delay(3)
    stage.unit(19).setAction(6)
    stage.say('&19\n한중은 촉의 목줄을 쥔 요충지입니다.\n&19\n지금 단숨에 촉을 멸해야 합니다.')
    stage.unit(19).setAction(0)
    stage.say('&0\n음, 허나 공명이 이끄는 촉군의 주력은 건재하다.\n문약, 지금 촉군은 어디에 있는가?')
    stage.unit(20).setAction(0, 2)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n예.\n한중의 검각에 요새를 세우고\n병력을 집결시키고 있습니다.')
    stage.unit(20).setAction(0)
    stage.say('&0\n그런가? 곤란하게 되었군.')
    stage.unit(20).setAction(6)
    stage.say('&20\n지금 공격한다면\n큰 손실을 각오하셔야 합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(21).setAction(0, 2)
    stage.delay(3)
    stage.unit(21).setAction(6)
    stage.say('&21\n허나 지금으로서는 공격하는 수밖에 없습니다.')
    stage.unit(21).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n음.\n좋다, 검각으로 진군하라.\n무슨 일이 있어도 촉군을 섬멸해야 한다!')
    stage.unit(0).setAction(0)
    stage.unit(1).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.unit(21).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        19])
    gvars[4076] = 0
    gvars[4057] = 40
    stage.setBattlePos([
        [
            18,
            1],
        [
            17,
            1],
        [
            18,
            2],
        [
            17,
            2],
        [
            19,
            2],
        [
            16,
            2],
        [
            19,
            1],
        [
            16,
            1],
        [
            20,
            1],
        [
            15,
            1],
        [
            18,
            0],
        [
            17,
            0],
        [
            19,
            0],
        [
            16,
            0],
        [
            15,
            0]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.delay(3)
        stage.clsUnit()
        stage.loadBg(1, 0)
        stage.draw()
        stage.showHead(0, 130, 77)
        stage.showHead(35, 38, 128)
        stage.bottomTxt('조조군', False, False, False)
        stage.delay(5)
        stage.head(0).move(102, 128)
        stage.delay(5)
        stage.bottomTxt('군사를 이끌고 검각으로 진군했다.', True, False, True)
        stage.loadBg(2, 54)
        stage.showUnits([
            [
                35,
                53,
                37,
                2],
            [
                54,
                67,
                52,
                3],
            [
                53,
                67,
                58,
                3],
            [
                66,
                67,
                64,
                3],
            [
                34,
                42,
                52,
                1],
            [
                50,
                42,
                58,
                1],
            [
                69,
                42,
                64,
                1],
            [
                194,
                53,
                53,
                0,
                6]])
        stage.draw()
        stage.delay(3)
        stage.info('한편……', 3)
        stage.setStageName('검각  유선군 본진')
        stage.say('&35\n그런가. 손권이 국교 회복을 받아들였다고?\n&194\n예.\n두 나라가 힘을 합쳐 조위를 무찌르자고 했습니다.')
        stage.unit(194).setAction(0)
        stage.say('&35\n음.\n이번 임무에 수고했다.\n물러가 쉬어라.')
        stage.unit(194).setAction(6)
        stage.say('&194\n예.')
        stage.unit(194).setAction(0)
        stage.delay(3)
        stage.unit(194).move(53, 95, 2)
        stage.unit(194).hide()
        stage.say('&35\n위나라가 흥성하고 강대해지니\n손권도 다소 긴장한 모양입니다.')
        stage.unit(69).setAction(6)
        stage.say('&69\n적어도 이렇게 하면 동오가 뒤를 치지는 못하겠지.')
        stage.unit(69).setAction(0)
        stage.say('&35\n좋다.\n이제 검각에서 위군을 막기만 하면 된다.')
        stage.unit(53).setAction(0, 0)
        stage.say('&53\n하지만 승상,\n위군도 지난 양평관 싸움에서 큰 손실을 입었는데\n정말 이곳을 공격하러 오겠습니까?\n&35\n반드시 올 것이다.\n조조는 공격할 때를 잘 아는 자이니\n지금쯤 멀지 않은 곳에 있을 것이다.')
        stage.unit(66).setAction(0, 0)
        stage.say('&66\n설마 벌써 오지는 않았겠지?')
        stage.unit(50).setAction(3)
        stage.say('&50\n쯧쯧, 너무 순진하군.\n위군은 이미 여기서 멀지 않네.')
        stage.unit(50).setAction(0)
        stage.delay(3)
        stage.unit(53).setAction(0, 3)
        stage.delay(3)
        stage.unit(66).setAction(0, 3)
        stage.say('&66\n음')
        stage.showUnit(183, 53, 90)
        stage.say('&183\n아뢰옵니다.')
        stage.say('&183\n위군은 성 밖 몇 리 떨어진 평원에 진을 쳤습니다.')
        stage.unit(35).setAction(17)
        stage.say('&35\n왔다!\n모두 제자리를 지키며\n위군을 맞아 싸워라.')
        stage.unit(35).setAction(0)
        stage.say('&35\n(유비 주군,\n이 공명에게 촉을 맡기셨으니\n반드시 위군을 무찌르겠습니다.)')
        stage.bgSound(-1)
        stage.clsUnit()
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n정면에 거대한 관문이 있습니다.\n과연 공명이 직접 세운 요새답습니다.\n&0\n음, 공명도 제법이군.')
    if stage.unitClickTest(19):
        stage.say('&19\n첩자의 보고에 따르면\n이곳을 함락하고 성도로 진군하려면\n관문 두 곳을 돌파해야 합니다.\n&0\n음, 관문이 두 곳이라고?')
    if stage.unitClickTest(17):
        stage.say('&17\n촉으로 가는 길은 아주 험한 산길이라 들었어요.')
    if stage.unitClickTest(15):
        stage.say('&0\n허저, 어째서 그리 안절부절못하느냐?\n&15\n예? 엉덩이가 좀 가려워서요.\n&17\n어머, 어쩌면 좋아요.\n제발 그만 좀 긁으세요!\n&15\n하지만 참을 수가 없는데…….')
    if stage.unitClickTest(21):
        stage.say('&21\n나라의 존망이 걸린 싸움인데도\n유선은 직접 나서서 독려하지도 않는군요…….\n&0\n흥, 제 아비와는 너무도 다르군.')
    if stage.unitClickTest(1):
        stage.say('&1\n온통 험한 산뿐이니\n병사들도 고향 생각이 나는 모양이야.')
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
