# Source Generated with Decompyle++
# File: R_20.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.bgSound(13)
    stage.delay(3)
    stage.setMenuVisible(False)
    model.setFace(128)
    stage.loadBg(2, 73)
    stage.draw()
    stage.setEventName('원소의 죽음')
    stage.setStageName('창정  강가')
    stage.showUnits([
        [
            113,
            95,
            77,
            3],
        [
            114,
            95,
            69,
            3],
        [
            101,
            95,
            77,
            3],
        [
            115,
            95,
            69,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(113),
            83,
            77,
            3],
        [
            stage.unit(114),
            83,
            69,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(113),
            50,
            77,
            3],
        [
            stage.unit(114),
            50,
            69,
            3],
        [
            stage.unit(101),
            62,
            77,
            3],
        [
            stage.unit(115),
            62,
            69,
            3]])
    stage.say('&101\n으, 으윽……….')
    stage.unit(101).setAction(9)
    stage.delay(3)
    stage.unit(114).setAction(0, 1)
    stage.unit(115).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(0, 1)
    stage.say('&113\n아! 아버님?')
    stage.unit(114).move(52, 69, 1)
    stage.say('&114\n정신 차리십시오, 아버님!')
    stage.say('&101\n이제 더는 걸을 수가 없구나.\n내 운수도 다한 모양이다…….')
    stage.unit(115).move(62, 71, 2)
    stage.say('&115\n정신 차리십시오!\n아버님께서 여기서 쓰러지시면\n우리 나라는 어찌합니까?\n&115\n업성으로 가면\n다시 일어설 수 있습니다.\n&114\n아버님!\n&101\n내…내 자리는……\n현보에게……물려주겠다……\n&115\n예?')
    stage.unit(113).setAction(3)
    stage.say('&113\n무, 무슨 말씀이십니까!\n어찌 그러실 수가 있습니까!!')
    stage.unit(113).setAction(0)
    stage.say('&114\n……아버님…….')
    stage.unit(101).setAction(1)
    stage.delay(3)
    stage.unit(101).setAction(1, 2)
    stage.say('&101\n가증스러운 조만 녀석…….\n&101\n나…… 나는 곧………\n천하를 손에 넣을 참이었거늘!!\n&101\n으아악!')
    stage.unit(101).setAction(10)
    stage.say('&114\n아버님!!\n&113\n(……어찌 이런 일이! 원상에게 자리를 물려주시다니!?\n인정할 수 없다. 절대로 인정할 수 없어!!\n내가……내가 맏아들이다. 후계자는 나여야 해!)\n&115\n……아버님.')
    stage.info('천하를 차지할 가능성이 가장 컸던 원소는 죽었다.\n남겨진 세 아들은 저마다 다른 속내를 품고\n서로를 경계하며 업성으로 물러났다.')
    stage.bgSound(-1)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(15)
    stage.delay(3)
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
    stage.delay(3)
    stage.setStageName('창정  조조군 본진')
    stage.showUnit(181, 49, 16, 2)
    stage.say('&181\n아룁니다.')
    stage.unit(181).move(49, 22, 2)
    stage.delay(3)
    stage.unit(181).setAction(11)
    stage.say('&181\n주군, 원소가 황하 강가에서 죽었습니다.\n&22\n뭐라고? 원소가 죽었다고!?')
    stage.unit(1).setAction(0, 2)
    stage.delay(3)
    stage.unit(1).setAction(3, 2)
    stage.say('&1\n맹덕, 잘됐군.\n마침내 원소를 없앴어!!')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n…………。')
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n……이봐.\n맹덕, 왜 그러는가?\n원소가 죽었다는데…….')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n주군………….')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n……원소의 뒤를 잇는 자는 누구냐?\n&181\n주군께 아룁니다. 막내아들 원상입니다.\n&0\n막내아들……?\n&181\n예. 원소가 임종 직전에 직접 지목했다고 합니다.\n&0\n그런가? 수고했다.\n물러가거라……\n&181\n예.')
    stage.unit(181).setAction(0)
    stage.delay(3)
    stage.unit(181).move(49, 16, 0)
    stage.unit(181).hide()
    stage.delay(3)
    stage.unit(1).setAction(3, 2)
    stage.say('&1\n이봐, 맹덕…….\n대체 무슨 일인가?')
    stage.unit(1).setAction(0, 2)
    stage.say('&0\n……원양.\n&1\n응?\n&0\n나는 어려서부터 원소와 알고 지냈지.\n그는 내게 많은 영향을 주었네.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n원소와 나는 닮은 구석이 있었지.\n&1\n………\n&0\n그가 죽었다니 나도 마음 한편이 쓸쓸하군.\n&1\n음…… 무슨 말인지 알겠네.')
    stage.unit(0).setAction(0, 0)
    stage.say('&0\n에잇, 그만두자.\n생각해 보면 나라는 사람은\n감상에 젖는 것도 어울리지 않지.')
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다. 이번 원정의 목적은 달성했다.\n전군은 허도로 개선하라.')
    stage.unit(1).setAction(17)
    stage.say('&1\n오!')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n잠시 기다리십시오.')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(0)
    stage.say('&0\n음? 문약, 무슨 일인가?')
    stage.unit(20).setAction(6)
    stage.say('&20\n주군, 허도로 개선하기 전에\n다른 곳도 공격하는 것이 어떻겠습니까?')
    stage.say('&0\n음, 유비를 말하는가?\n&20\n그렇습니다.')
    stage.unit(20).setAction(0)
    stage.say('&20\n유비 일행은 지금 양산에 근거지를 마련하고\n근처 산적들을 모아 병력을 늘리고 있습니다.')
    stage.unit(20).setAction(6)
    stage.say('&20\n그를 없애는 것이 어떻겠습니까……?\n&0\n좋다. 빈손으로 허도에 돌아갈 수는 없지.\n놈들을 섬멸하자!')
    stage.unit(0).setAction(17)
    stage.say('&0\n좋다! 양산으로 가자.\n유비를 공격한다!')
    stage.unit(0).setAction(0)
    stage.unit(20).setAction(0, 3)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 15, [
        2,
        7,
        6])
    gvars[4076] = 0
    gvars[4057] = 20
    stage.setBattlePos([
        [
            12,
            1],
        [
            13,
            2],
        [
            10,
            1],
        [
            15,
            2],
        [
            9,
            2],
        [
            16,
            1],
        [
            9,
            0],
        [
            15,
            0],
        [
            12,
            3],
        [
            14,
            3],
        [
            13,
            4],
        [
            14,
            1],
        [
            11,
            2],
        [
            13,
            0],
        [
            11,
            0]])
    return False


def scene2():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        stage.clsUnit()
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n산적들이 유비를 돕는다 해도\n결국 오합지졸에 불과하네.\n&0\n하지만 산에서 싸우면\n도적들은 물 만난 고기와 같아지네.\n방심하지 말게!\n&1\n음.')
    if stage.unitClickTest(2):
        stage.say('&2\n관우는 제게 맡겨 주십시오.')
    if stage.unitClickTest(20):
        stage.say('&20\n전장은 온통 산이니\n허저 장군을 내보내시지요……\n&15\n오! 역시 문약 노군사께서는\n안목이 있으십니다.\n&20\n하하하.')
    if stage.unitClickTest(22):
        stage.say('&22\n조운이 유비군에 들어간 듯합니다.\n&0\n조운? 서주에서 본 그 맹장 말인가?\n&22\n예.\n그의 무력은 관우나 장비에 견줄 만합니다.\n&0\n오, 흥미롭군.')
    if stage.unitClickTest(15):
        stage.say('&15\n양산? 산이었군요.\n이번에는 제가 활약할 차례입니다!')
    if stage.unitClickTest(21):
        stage.say('&21\n하지만 유비는 외교에 능한 사람입니다.\n&0\n과연 그렇네.\n원소를 성공적으로 이용한 적도 있지.')
    return False


def scene3():
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            32,
            53,
            37,
            2],
        [
            3,
            42,
            52,
            1],
        [
            33,
            42,
            58,
            1],
        [
            74,
            67,
            52,
            3],
        [
            75,
            67,
            58,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('양산  유비군 본진')
    stage.delay(3)
    stage.unit(32).setAction(3)
    stage.say('&32\n뭐라고!?\n조조군이 양산으로 쳐들어온다고!?')
    stage.say('&75\n예. 제 부하들이 보았습니다.\n틀림없는 조조의 깃발입니다.')
    stage.unit(32).setAction(0)
    stage.say('&32\n그렇다면………\n조조가 원소를 완전히 무찔렀단 말인가?\n&3\n아마 그럴 것입니다.\n조조는 참으로 두려운 인물입니다!')
    stage.unit(33).setAction(0, 0)
    stage.say('&33\n이봐!\n지금 적을 칭찬하고 있을 때가 아니야.')
    stage.unit(33).setAction(0, 1)
    stage.say('&32\n음, 그렇다.\n무슨 일이 있어도\n조조군의 공격을 막아야 한다.')
    stage.unit(74).setAction(3)
    stage.say('&74\n주군, 염려 마십시오.\n이 일대는 저희 영역이니\n안심하셔도 됩니다.')
    stage.unit(74).setAction(0)
    stage.delay(3)
    stage.unit(75).setAction(3)
    stage.say('&75\n그래!')
    stage.unit(75).setAction(0)
    stage.say('&32\n음.')
    stage.unit(32).setAction(17)
    stage.say('&32\n좋다! 전군은 수비를 강화하여\n조조군의 공격에 대비하라.')
    stage.effectSound(202, 255)
    stage.clsUnit()
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
