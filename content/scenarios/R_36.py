# Source Generated with Decompyle++
# File: R_36.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
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
            12,
            44,
            58,
            1],
        [
            20,
            64,
            64,
            3]])
    if vars[835]:
        stage.showUnit(15, 44, 52, 1)
    else:
        stage.showUnit(6, 44, 52, 1)
    stage.showUnits([
        [
            23,
            64,
            52,
            3],
        [
            19,
            64,
            58,
            3]])
    stage.draw()
    if vars[735] and vars[835]:
        stage.bgSound(13)
        stage.delay(3)
        stage.setStageName('한중  조조군 본진')
        stage.setEventName('하후연의 유품')
        stage.delay(3)
        model.setFace(1)
        stage.say('&0\n뭐……뭐라고……?\n하후연이 죽었다고……?')
        stage.unit(12).setAction(0, 2)
        stage.delay(3)
        stage.unit(12).setAction(6)
        stage.say('&12\n예……\n촉의 대군이 퍼붓는 맹공을 이기지 못하고\n중과부적으로 전사했습니다……')
        stage.unit(12).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n묘재, 나의 묘재여………')
        stage.unit(1).setAction(4)
        stage.say('&1\n으아아…… 으으…….\n묘재……!!')
        stage.unit(0).setAction(0, 0)
        model.setFace(0)
        stage.say('&0\n자렴, 묘재의 시신은 지금 어디에 있는가?')
        stage.unit(12).setAction(6)
        stage.say('&12\n혼전 중이라 아직 찾지 못했습니다.')
        stage.unit(0).setAction(2)
        stage.say('&0\n무슨 일이 있어도 찾아서\n후하게 장례를 치러 주어라………\n알겠느냐!\n&12\n예.')
        stage.unit(12).setAction(0)
        stage.delay(3)
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('&19\n……주군.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(2)
        stage.say('&0\n중달, 즉시 촉군을 공격하게!\n괘씸한 유비……공명!\n묘재의 원수를 갚겠다!')
        stage.unit(19).setAction(0)
        stage.say('&19\n명을 받들겠습니다!')
        stage.unit(0).setAction(17)
        stage.say('&0\n큰귀 도적놈, 감히 내 묘재를 죽이다니!\n뼈저리게 후회하도록 해 주마.\n출진하라!')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.showUnit(181, 55, 22, 2)
        stage.say('&181\n아뢰옵니다.\n&0\n무슨 일이냐?')
        stage.unit(181).setAction(11)
        stage.say('&181\n주군, 하후 장군의 진영에 말 한 필이 남아 있습니다.\n&0\n말이라고?\n&181\n예. 준마입니다.\n&0\n말이라…아, 조황비전이로군?\n전에 묘재에게 타도록 준 말인데……\n어찌 진영에 남아 있는가?')
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n그러고 보니…….\n전에 하후 장군께 들은 적이 있습니다.\n&20\n주군께서 하사하신 좋은 말은\n전장에서 함부로 쓸 수 없다고 하셨지요.')
        stage.unit(20).setAction(0)
        stage.delay(5)
        stage.say('&0\n묘재, 참으로 어리석구나……\n그리 염려만 하지 않았다면\n살 수 있었을지도 모르는데……')
        stage.say('&0\n내 말과 함께 매어 두어라.\n&181\n예, 명을 받들겠습니다.')
        stage.getItem(124, 0)
        stage.unit(181).setAction(0)
        stage.unit(181).hide()
        model.unitJoin(6, 255, 0)
        stage.bgSound(15)
        stage.delay(3)
    if vars[835] and not vars[735]:
        stage.bgSound(13)
        stage.delay(3)
        stage.setStageName('한중  조조군 본진')
        stage.setEventName('하후연의 유품')
        model.setFace(1)
        stage.say('&0\n뭐……뭐라고……?\n하후연이 죽었다고……?')
        stage.unit(12).setAction(0, 2)
        stage.delay(3)
        stage.unit(12).setAction(6)
        stage.say('&12\n예……\n촉의 대군이 퍼붓는 맹공을 이기지 못하고\n중과부적으로 전사했습니다……')
        stage.unit(12).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n묘재, 나의 묘재여………')
        stage.unit(1).setAction(4)
        stage.say('&1\n으아아…… 으으…….\n묘재……!!')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n가증스러운 유비…… 공명!\n즉시 촉군을 공격하라!\n묘재의 원수를 갚겠다!')
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('&19\n주군, 잠시 기다리십시오.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        model.setFace(0)
        stage.say('&0\n중달, 어째서 나를 막는가?\n&19\n주군, 이번 싸움에서 아군의 피해가 막심합니다.\n이제 더 싸울 힘이 없습니다.')
        stage.unit(19).setAction(0)
        stage.say('&0\n음……')
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n중달의 말이 옳습니다.\n&20\n일단 한중을 포기하고\n허도로 돌아가 재기를 도모해야 합니다.')
        stage.unit(20).setAction(0)
        stage.say('&0\n……묘재를 잃은 데다\n한중마저 빼앗기다니…… 하아…….\n좋다. 이 또한 어쩔 수 없는 일이겠지.')
        stage.unit(20).setAction(6)
        stage.say('&20\n주군의 마음을 이해합니다.')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n귀 큰 도적놈, 감히 내 묘재를 죽이다니!\n뼈저리게 후회하도록 해 주겠다.')
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n자렴, 묘재의 시신은 지금 어디에 있는가?')
        stage.unit(12).setAction(6)
        stage.say('&12\n혼전 중이라 아직 찾지 못했습니다.')
        stage.unit(0).setAction(2)
        stage.say('&0\n무슨 일이 있어도 찾아서\n나를 대신해 후히 장사 지내 주게………\n알겠는가!')
        stage.unit(0).setAction(0)
        stage.say('&12\n예.')
        stage.unit(12).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.delay(5)
        stage.say('&0\n……묘재야……')
        stage.delay(5)
        stage.showUnit(181, 54, 22, 2)
        stage.say('&181\n아룁니다.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n무슨 일인가?')
        stage.unit(181).setAction(11)
        stage.say('&181\n주군, 하후 장군의 진영에 말 한 필이 남아 있습니다.\n&0\n말이라고?\n&181\n예. 준마입니다.\n&0\n말이라…아, 조황비전이로군?\n전에 묘재에게 타도록 준 말인데……\n어찌 진영에 남아 있는가?')
        stage.unit(20).setAction(6)
        stage.say('&20\n그러고 보니…….\n전에 하후 장군께 들은 적이 있습니다.\n&20\n주군께서 하사하신 좋은 말은\n전장에서 함부로 쓸 수 없다고 하셨지요.')
        stage.unit(20).setAction(0)
        stage.delay(5)
        stage.say('&0\n묘재, 참으로 어리석구나……\n그리 염려만 하지 않았다면\n살 수 있었을지도 모르는데……')
        stage.say('&0\n내 말과 함께 매어 두어라.\n&181\n예, 명을 받들겠습니다.')
        stage.getItem(124, 0)
        stage.unit(181).setAction(0)
        stage.unit(181).hide()
        model.unitJoin(6, 255, 0)
        if vars[1001]:
            stage.bgSound(16)
            stage.delay(3)
            stage.clsUnit()
            stage.loadBg(1, 0)
            stage.draw()
            stage.showHead(0, 157, 86)
            stage.showHead(32, 93, 86)
            stage.bottomTxt('조조는 한중을 포기하고,', False, False, False)
            stage.delay(5)
            stage.head(0).move(332, 101)
            stage.delay(5)
            stage.bottomTxt('군을 물려 허도로 돌아갔다.', True, False, True)
            stage.bgSound(-1)
            stage.jumpScene(75)
    if vars[735] and not vars[835]:
        stage.bgSound(15)
        stage.delay(3)
        stage.setStageName('한중  조조군 본진')
        stage.setEventName('한중 탈환')
        stage.say('&0\n하후연, 수고했네.')
        stage.unit(6).setAction(0, 2)
        stage.delay(3)
        stage.unit(6).setAction(6)
        stage.say('&6\n용서해 주게. 끝내 한중을 지키지 못했네.\n자네의 부탁을 저버렸군.')
        stage.unit(6).setAction(0)
        stage.say('&0\n촉의 대군이 쳐들어왔으니 중과부적이지 않았느냐.\n어쩔 수 없는 일이니 무슨 죄가 있겠느냐?\n&6\n하지만…한중을……\n&0\n잃었으면 되찾으면 된다.')
        stage.unit(0).setAction(17)
        stage.say('&0\n즉시 출병해 촉군을 공격하라!\n한중을 차지하겠다!\n출진하라!')
        stage.unit(0).setAction(0)
        stage.unit(6).setAction(0, 1)
    if not vars[735] and not vars[835]:
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.setStageName('한중  조조군 본진')
        stage.setEventName('헛된 행군')
        stage.unit(0).setAction(2)
        model.setFace(2)
        stage.say('&0\n귀 큰 도적놈……\n어느새 저토록 많은 군사를 거느렸단 말인가.')
        stage.unit(0).setAction(0)
        model.setFace(0)
        stage.delay(3)
        stage.unit(19).setAction(0, 2)
        stage.delay(3)
        stage.unit(19).setAction(6)
        stage.say('&19\n주군, 우리 군의 손실이 너무 큽니다.\n더는 싸울 수 없습니다.')
        stage.unit(19).setAction(0)
        stage.say('&0\n음……')
        stage.unit(20).setAction(0, 2)
        stage.delay(3)
        stage.unit(20).setAction(6)
        stage.say('&20\n중달의 말이 옳습니다.\n&20\n일단 한중을 포기하고\n허도로 돌아가 재기를 도모해야 합니다.')
        stage.unit(20).setAction(0)
        stage.say('&0\n……좋다.\n이제는 어쩔 수 없군.\n군사를 돌려 허도로 돌아가자!')
        if vars[1001]:
            stage.effectSound(203, 255)
            stage.bgSound(16)
            stage.delay(3)
            stage.clsUnit()
            stage.loadBg(1, 0)
            stage.draw()
            stage.showHead(0, 157, 86)
            stage.showHead(32, 93, 86)
            stage.bottomTxt('조조는 한중을 포기하고,', False, False, False)
            stage.delay(5)
            stage.head(0).move(332, 101)
            stage.delay(5)
            stage.bottomTxt('군을 물려 허도로 돌아갔다.', True, False, True)
            stage.bgSound(-1)
            stage.jumpScene(75)
    stage.unit(0).setAction(0, 0)
    stage.unit(1).setAction(0, 1)
    stage.unit(12).setAction(0, 1)
    stage.unit(20).setAction(0, 3)
    stage.unit(19).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        15])
    gvars[4076] = 0
    gvars[4057] = 36
    stage.setBattlePos([
        [
            26,
            2],
        [
            25,
            2],
        [
            26,
            3],
        [
            23,
            7],
        [
            22,
            6],
        [
            23,
            6],
        [
            24,
            7],
        [
            22,
            5],
        [
            22,
            3],
        [
            21,
            2],
        [
            22,
            2],
        [
            23,
            3],
        [
            21,
            1],
        [
            27,
            3],
        [
            25,
            1]])
    return False


def scene1():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출진하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(20):
        stage.say('&20\n양옆을 산이 감싼 평지가\n이번 싸움의 주전장입니다.\n&20\n또한 유비군은 배수진을 쳤습니다.\n&0\n음.')
    if stage.unitClickTest(19):
        stage.say('&19\n방금 무기점에 다녀왔는데\n새 방어구가 들어왔습니다.')
    if stage.unitClickTest(6):
        stage.say('&6\n한중을 잃은 책임은 제게 있습니다.\n이번에는 부디 저를 내보내 주십시오.')
    if stage.unitClickTest(15):
        stage.say('&15\n하후 장군은 좋은 분이신데……\n빌어먹을!')
    if stage.unitClickTest(1):
        if vars[835]:
            stage.say('&1\n맹덕, 내가 묘재의 원수를 갚게 해 주게!')
        if not vars[835]:
            stage.say('&1\n묘재, 너무 자책하지 말게.')
    if stage.unitClickTest(12):
        if vars[835]:
            stage.say('&12\n즉시 부하들에게 시신을 찾게 하겠습니다.\n&0\n음, 반드시 찾아야 한다.')
        if not vars[835]:
            stage.say('&12\n촉군이 이토록 강할 줄이야…\n참으로 면목이 없습니다.\n&0\n됐다.\n내 잘못이니\n자책하지 말게.')
    if stage.unitClickTest(23):
        stage.say('&23\n지형으로 보아\n우리 기병에게 유리합니다.')
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
