# Source Generated with Decompyle++
# File: R_04.marshal (Python 3.9)


def scene0():
    if vars[1002]:
        stage.infoTransfer(18, '')
    if vars[303] and vars[703]:
        model.initLocalVar()
        stage.clsUnit()
        stage.setMenuVisible(False)
        stage.loadBg(2, 54)
        stage.showUnits([
            [
                101,
                54,
                35,
                2],
            [
                99,
                42,
                68,
                1],
            [
                136,
                42,
                60,
                1],
            [
                145,
                42,
                52,
                1],
            [
                161,
                67,
                52,
                3],
            [
                32,
                67,
                60,
                3],
            [
                181,
                54,
                60,
                0,
                11]])
        stage.draw()
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.setEventName('연환계')
        stage.setStageName('낙양  연합군 본진')
        stage.unit(101).setAction(3)
        stage.say('&101\n뭐라고? 조조군이 동탁군의 매복에 걸려\n대패했다고!\n&181\n그렇습니다.')
        stage.unit(101).setAction(0)
        stage.say('&101\n조조는 지금 어디에 있느냐?\n&181\n패잔병을 수습해 낙양으로 돌아가고 있습니다.')
        stage.say('&101\n음…….')
        stage.unit(136).setAction(3)
        stage.say('&136\n히히, 그토록 큰소리를 치더니 결국 패했군.')
        stage.unit(136).setAction(0)
        stage.delay(3)
        stage.unit(99).setAction(0, 0)
        stage.say('&99\n패했다고? 정말이오?')
        stage.delay(3)
        stage.unit(136).setAction(0, 2)
        stage.say('&136\n당연하지. 동탁의 복병에게 처참히 당했어.')
        stage.unit(99).setAction(0, 1)
        stage.say('&99\n천하 사람들은 그렇게 생각하지 않네. 이번 동탁 추격으로\n조조는 반드시 명성을 떨칠 것이야.')
        stage.unit(136).setAction(0, 1)
        stage.delay(5)
        stage.unit(32).setAction(6)
        stage.say('&32\n그렇습니다. 연합군 가운데 조조만 추격에 나섰습니다.')
        stage.unit(32).setAction(0)
        stage.say('&32\n조조의 행동이 사람들을 일깨워 함께 동탁을 토벌하게 되기를 바라네.')
        stage.say('&101\n…………。')
        stage.effectSound(203, 255)
        stage.clsUnit()
    if vars[303] and not vars[703]:
        model.initLocalVar()
        stage.clsUnit()
        stage.setMenuVisible(False)
        stage.loadBg(2, 54)
        stage.showUnits([
            [
                101,
                54,
                37,
                2],
            [
                99,
                42,
                68,
                1],
            [
                136,
                42,
                60,
                1],
            [
                145,
                42,
                52,
                1],
            [
                161,
                67,
                52,
                3],
            [
                32,
                67,
                60,
                3],
            [
                181,
                54,
                60,
                0,
                11]])
        stage.draw()
        stage.setEventName('연환계')
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.setStageName('낙양  연합군 본진')
        stage.delay(10)
        stage.unit(101).setAction(0, 0)
        stage.say('&101\n그런가. 조조가 동탁을 잡지 못했군?\n&181\n그렇습니다.')
        stage.unit(101).setAction(0, 2)
        stage.say('&101\n조조는 지금 어디에 있는가?\n&181\n병력을 모아 낙양으로 돌아가는 중입니다.')
        stage.say('&101\n음…….')
        stage.unit(136).setAction(3)
        stage.say('&136\n히히히, 먼저 나서서 출병하겠다더니!\n참으로 우습구나.')
        stage.unit(136).setAction(0)
        stage.delay(3)
        stage.unit(99).setAction(0, 0)
        stage.say('&99\n우습다고?\n정말 그럴까?')
        stage.unit(136).setAction(0, 2)
        stage.say('&136\n당연하지!\n동탁을 놓쳤으니 말이야.')
        stage.unit(99).setAction(0, 1)
        stage.say('&99\n그것은 조조의 진짜 목적이 아닐세.')
        stage.unit(136).setAction(0, 1)
        stage.delay(5)
        stage.unit(32).setAction(6)
        stage.say('&32\n그렇습니다.\n연합군 가운데 조조만 동탁을 추격했습니다.\n그 행동이 다른 이들도 함께 동탁을 토벌하도록 일깨우기를 바랍니다.')
        stage.unit(32).setAction(0)
        stage.say('&99\n그는 이미 목적을 이루었소.')
        stage.say('&101\n……。')
        stage.effectSound(203, 255)
        stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 54)
    stage.showUnits([
        [
            101,
            54,
            37,
            2],
        [
            99,
            42,
            68,
            1],
        [
            136,
            42,
            60,
            1],
        [
            145,
            42,
            52,
            1],
        [
            161,
            67,
            52,
            3],
        [
            32,
            67,
            60,
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    if not vars[303]:
        stage.setEventName('연환계')
    stage.info('다음 날…….', 3)
    stage.setStageName('낙양  연합군 본진')
    if not vars[303]:
        stage.delay(10)
        stage.unit(101).setAction(0, 3)
        stage.delay(10)
        stage.unit(101).setAction(0, 1)
        stage.delay(10)
        stage.unit(101).setAction(0, 2)
        stage.say('&101\n조조는 어디로 갔느냐?')
        stage.unit(32).setAction(6)
        stage.say('&32\n이른 아침부터 모습이 보이지 않는군.')
        stage.unit(32).setAction(0)
        stage.delay(3)
        stage.unit(101).setAction(2)
        stage.say('&101\n그놈은 대체 무슨 꿍꿍이냐?')
        stage.delay(3)
        stage.unit(101).setAction(0)
        stage.say('&101\n……그만두세.\n연합군의 앞날이나 의논하도록 하세.')
    stage.unit(101).setAction(3)
    stage.say('&101\n모두 좋은 계책이 있는가?')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).move(58, 37, 1)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('&101\n우리는 동탁군을 두 번 물리치고 낙양을 되찾았네.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).move(54, 37, 3)
    stage.delay(3)
    stage.unit(101).setAction(17)
    stage.say('&101\n게다가 천문을 보아도 동탁의 운수는 다했소.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(101).setAction(0, 2)
    stage.say('&101\n이제 동맹도 아무 의미가 없는 듯하네…….')
    stage.unit(161).setAction(6)
    stage.say('&161\n본초의 말에 깊이 공감하오.\n우리는 이미 대의를 다하지 않았소.')
    stage.unit(161).setAction(0)
    stage.delay(3)
    stage.unit(145).setAction(6)
    stage.say('&145\n동탁을 없애지 못한 것은 유감이나\n자신의 영지를 내버려 둘 수도 없지 않소.')
    stage.unit(145).setAction(0)
    stage.say('&32\n……。')
    stage.delay(5)
    stage.say('&101\n모두 이견이 없는 듯하니\n동맹은 이만 해산하도록 하세.')
    stage.delay(5)
    stage.unit(99).setAction(0, 2)
    stage.say('&99\n이미 결정되었다면 먼저 군사를 거두겠소.')
    stage.unit(101).setAction(3)
    stage.say('&101\n아, 손 장군. 아직 전별연이 남아 있소만…….')
    stage.unit(101).setAction(0)
    stage.delay(5)
    stage.unit(99).setAction(0, 1)
    stage.say('&99\n몸이 조금 좋지 않으니 참석하지 않겠소.')
    stage.unit(136).setAction(0, 2)
    stage.say('&136\n흥! 참으로 경솔한 자로군.')
    stage.unit(99).setAction(0, 0)
    stage.say('&99\n……원술, 언젠가 반드시 네 목을 베겠다.')
    stage.unit(136).setAction(3)
    stage.say('&136\n응?')
    stage.unit(136).setAction(0)
    stage.delay(5)
    stage.unit(99).setAction(0, 2)
    stage.say('&99\n네 목은 잠시 네 어깨에 붙여 두겠다. 흥!\n……가 보겠소.')
    stage.unit(99).move(42, 90, 2)
    stage.unit(136).setAction(0, 2)
    stage.unit(99).hide()
    if vars[303]:
        stage.delay(10)
        stage.showUnit(0, 54, 92)
        stage.say('&0\n원소 공, 연합군을 해산하시겠다는 것이오?')
        stage.unit(145).setAction(0, 2)
        stage.unit(136).setAction(0, 2)
        stage.unit(143).setAction(0, 2)
        stage.unit(161).setAction(0, 2)
        stage.unit(32).setAction(0, 2)
        stage.delay(3)
        stage.unit(101).setAction(3)
        stage.say('&101\n오, 조조. 돌아왔구나.')
        stage.unit(101).setAction(0)
        stage.unit(0).move(54, 52, 0)
        stage.unit(0).setAction(0)
        stage.unit(145).setAction(0, 1)
        stage.unit(136).setAction(0, 1)
        stage.unit(161).setAction(0, 3)
        stage.unit(143).setAction(0, 3)
        stage.unit(32).setAction(0, 3)
        stage.delay(3)
        stage.say('&0\n하지만 원소 대인, 동탁은 아직 살아 있습니다.')
        stage.delay(5)
        stage.unit(101).setAction(2)
        stage.say('&101\n조조, 이미 결정된 일이오. 연합군은 해산했소.')
        stage.unit(101).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 2)
        stage.delay(5)
        stage.say('&0\n각지의 제후들이 모두 동의했는가?')
        stage.unit(145).setAction(6)
        stage.say('&145\n조조, 그대 마음은 이해하오.\n허나 영지를 내버려 둘 수는 없소.')
        stage.unit(145).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 3)
        stage.delay(10)
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n……좋다.')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n그럼 먼저 가 보겠소.')
        stage.unit(0).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 2)
        stage.delay(3)
        stage.unit(101).setAction(3)
        stage.say('&101\n응? 조조, 송별연도 있는데…….')
        stage.unit(101).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n원소 공, 보옥은 아직 동탁의 손에 있소.\n그 사실을 잊지 마시오.')
    if not vars[303]:
        stage.delay(10)
        stage.showUnit(0, 54, 92)
        stage.say('&0\n원소 공, 연합군을 해산하시겠다는 것이오?')
        stage.unit(101).setAction(2)
        stage.say('&101\n조조, 대체 어디서 무엇을 하고 왔는가?')
        stage.unit(101).setAction(0)
        stage.unit(136).setAction(0, 1)
        stage.unit(0).move(54, 52, 0)
        stage.unit(0).setAction(0)
        stage.say('&0\n하지만 원소 대인, 동탁은 아직 살아 있습니다.')
        stage.delay(3)
        stage.unit(101).setAction(2)
        stage.say('&101\n조조, 이 일은 이미 결정되었네! 동맹군은 해산했어.')
        stage.unit(101).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 2)
        stage.delay(5)
        stage.say('&0\n각지의 제후들이 모두 동의했는가?')
        stage.unit(145).setAction(6)
        stage.say('&145\n조조, 자네의 마음은 이해하네.\n허나 우리 영지를 내버려 둘 수는 없네.')
        stage.unit(145).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 3)
        stage.delay(10)
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n……좋다.')
        stage.unit(0).setAction(0, 0)
        stage.delay(3)
        stage.unit(0).setAction(6)
        stage.say('&0\n그럼 먼저 가 보겠소.')
        stage.unit(0).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 2)
        stage.delay(5)
        stage.unit(101).setAction(3)
        stage.say('&101\n응? 조조, 송별연도 있는데…….')
        stage.unit(101).setAction(0)
        stage.delay(5)
        stage.unit(0).setAction(0, 0)
        stage.say('&0\n원소 공, 보옥은 아직 동탁의 손에 있소.\n그 사실을 잊지 마시오.')
    stage.unit(0).move(54, 98, 2)
    stage.unit(0).hide()
    stage.delay(10)
    stage.say('&101\n보옥이라고……?')
    stage.delay(3)
    stage.unit(101).setAction(2)
    stage.say('&101\n한이 쇠락했는데 황제에게 무슨 가치가 있겠는가.')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(0, 2)
    stage.say('&32\n한실이 쇠약해졌다는 것인가……?')
    stage.delay(5)
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.delay(5)
    stage.bgSound(-1)
    stage.loadBg(2, 3)
    stage.showUnit(153, 58, 70, 3, 1)
    stage.draw()
    stage.delay(3)
    stage.setStageName('장안  왕윤 저택')
    stage.showUnit(181, 30, 60, 1)
    stage.unit(181).move(38, 60, 1)
    stage.say('&181\n왕 대감, 오늘 밤 승상부에서 축하연이 열립니다.\n&181\n동탁 승상께서 반드시 참석하시라 하셨습니다.')
    stage.unit(153).move(58, 60, 3)
    stage.say('&153\n축하연이라니? 무엇을 축하한다는 것이오?\n아직 동맹군과 싸우고 있지 않소?\n&181\n동맹군이 며칠 전에 해산했기에 승상께서 축하연을 여십니다.')
    stage.bgSound(17)
    stage.delay(5)
    stage.unit(153).setAction(3)
    stage.say('&153\n뭐라고? 연합군이 해산했다고!?')
    stage.unit(181).move(40, 60, 1)
    stage.say('&181\n대감, 부디 참석해 주십시오.')
    stage.unit(153).setAction(0)
    stage.delay(5)
    stage.unit(153).setAction(0, 1)
    stage.say('&153\n……제가 어찌 사양하겠습니까?\n반드시 참석하겠다고 승상께 전해 주시오.\n&181\n예.')
    stage.unit(181).move(30, 60, 3)
    stage.unit(181).hide()
    stage.unit(153).move(58, 70, 2)
    stage.delay(3)
    stage.unit(153).setAction(1)
    stage.say('&153\n연합군이…… 연합군이 해산했다니…….')
    stage.unit(153).setAction(9)
    stage.say('&153\n흐흑……끝이구나…….\n동탁을 없앨 희망이 사라졌습니다, 폐하…….')
    stage.delay(10)
    stage.showUnit(17, 30, 60, 1)
    stage.say('&17\n아버님.')
    stage.unit(17).move(58, 60, 2)
    stage.say('&17\n아버님…….')
    stage.unit(153).setAction(1, 2)
    stage.delay(3)
    stage.unit(153).setAction(1, 0)
    stage.say('&153\n초선……사실대로 말하마…\n동맹군은 이미 해산했단다…….')
    stage.unit(17).setAction(6, 2)
    stage.say('&17\n……방금 말씀은 모두 들었어요.\n……아버님, 부디 몸조심하세요.')
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n착한 내 딸.')
    stage.unit(153).setAction(0, 2)
    stage.say('&153\n허나 이제는 희망이 없을 뿐이네.')
    stage.unit(17).setAction(0)
    stage.say('&17\n……아버님, 아직 희망은 있어요.')
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n뭐라고? 무슨 뜻인가?')
    stage.say('&17\n소녀에게 한 가지 생각이 있습니다.')
    stage.unit(153).move(58, 68, 0)
    stage.say('&153\n네가……? 무슨 생각이냐……?')
    stage.unit(17).move(58, 56, 0)
    stage.say('&17\n이리하여 저리하면…….')
    stage.delay(15)
    stage.unit(153).setAction(3, 0)
    stage.say('&153\n뭐, 뭐라고?\n안 된다! 초선아, 절대로 안 된다!')
    stage.unit(17).setAction(0, 2)
    stage.say('&17\n하지만 아버님, 달리 방법이 없잖아요.')
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n아무리 천하를 위해서라 해도\n너를 그렇게 할 수는…….')
    stage.unit(17).setAction(0, 0)
    stage.say('&17\n제 안위는 걱정하지 마세요. 모두 천하만을\n위해서가 아니에요. 고아였던 저를 친딸처럼\n길러 주신 아버님의 은혜에 보답하려는 것이기도 해요.')
    stage.unit(17).setAction(0, 2)
    stage.say('&17\n그러니 아버님을 위해서라도 제가 해야 해요.')
    stage.unit(153).setAction(6, 0)
    stage.say('&153\n이 아비가 미안하구나, 초선아…….')
    stage.unit(153).setAction(0, 0)
    stage.unit(17).move(58, 60, 2)
    stage.delay(5)
    stage.unit(17).setAction(6, 2)
    stage.say('&17\n그렇게 해요, 아버님…….')
    stage.unit(17).move(30, 60, 3)
    stage.unit(17).hide()
    stage.unit(153).setAction(0, 3)
    stage.delay(3)
    stage.unit(153).move(58, 70, 2)
    stage.delay(6)
    stage.unit(153).setAction(1, 2)
    stage.delay(3)
    stage.unit(153).setAction(9, 2)
    stage.say('&153\n……초선.')
    stage.info('초선의 계책은 먼저 여포와 혼약을 맺은 뒤\n자신을 동탁에게 바쳐 여포를 격분시키고,\n그 손을 빌려 동탁을 죽이는 것이었다.')
    stage.info('겨우 열여덟인 딸이 양부의 은혜에 보답하고자\n몸을 던져 의를 이루려 했다. 후세는 이를 연환계라 불렀다.')
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 3)
    stage.showUnits([
        [
            153,
            58,
            70],
        [
            119,
            58,
            60,
            2]])
    stage.delay(5)
    stage.draw()
    stage.setStageName('장안  왕윤 저택')
    stage.say('&119\n왕 대인, 아직은 아내를 맞을 형편이 못 됩니다…….')
    stage.unit(153).setAction(3)
    stage.say('&153\n그리 말씀하지 마십시오, 장군.\n제 딸은 장군이 아니면 시집가지 않겠다고 하더군요.')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(153).move(58, 68, 0)
    stage.say('&153\n변변치 않더라도 한번 만나 보십시오.\n&119\n좋습니다. 왕 대인께서 그리 말씀하시니…….')
    stage.unit(153).setAction(3)
    stage.say('&153\n오, 받아 주시겠소! 참으로 다행이오.')
    stage.unit(153).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(0, 3)
    stage.say('&153\n초선아, 초선아.')
    stage.delay(8)
    stage.showUnit(17, 30, 60, 1)
    stage.say('&17\n가요, 아버님.')
    stage.unit(119).setAction(0, 3)
    stage.delay(3)
    stage.unit(17).move(50, 60, 1)
    stage.unit(119).setAction(3, 3)
    stage.say('&119\n오?')
    stage.unit(153).setAction(0, 0)
    stage.say('&153\n제 딸 초선입니다. 어떻습니까?')
    stage.unit(119).setAction(0, 3)
    stage.say('&119\n이……이토록……아름다울 수가…….')
    stage.unit(17).setAction(6, 1)
    stage.say('&17\n장군의 후의에 감사드립니다.')
    stage.unit(119).move(56, 60, 3)
    stage.say('&119\n아니…… 정말 아름답소…….')
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 40)
    stage.showUnits([
        [
            127,
            58,
            60,
            3],
        [
            17,
            55,
            60,
            1]])
    stage.draw()
    stage.setStageName('장안  동탁의 저택')
    stage.say('&127\n초선, 너는 참으로 아름답구나.\n&17\n승상께서 또 소녀를 놀리시는군요.\n&127\n하하하하.')
    stage.showUnit(119, 58, 31, 2)
    stage.say('&119\n의부님, 부르셨습니까?')
    stage.unit(127).setAction(0, 0)
    stage.say('&127\n오, 여포.')
    stage.unit(119).setAction(3)
    stage.say('&119\n초……초선이 어찌!?')
    stage.unit(119).setAction(2)
    stage.say('&119\n……동탁, 네놈!!')
    stage.clsUnit()
    stage.loadBg(0, 9)
    stage.draw()
    stage.info('왕윤의 「연환계」가 마침내 성공하여 동탁은\n여포에게 살해당했다. 이후 여포는 동탁의 원수를\n갚으려는 이각, 곽사와 싸우게 되었다.')
    stage.info('여포의 무용을 잘 아는 왕윤은 승리를 믿고\n궁으로 가 황제께 아뢰었다.')
    return False


def scene5():
    stage.loadBg(2, 15)
    stage.showUnits([
        [
            149,
            27,
            55,
            1],
        [
            153,
            45,
            55,
            3,
            6]])
    stage.draw()
    stage.setStageName('장안  궁궐')
    stage.say('&149\n뭐라고! 왕윤, 사실인가?\n&153\n그러하옵니다, 폐하.\n&149\n동탁이……동탁이 죽었단 말인가……!\n&153\n그러하옵니다.\n여 장군이 지금 동탁의 잔당 이각, 곽사와 싸우고 있사옵니다.\n&149\n벼, 별일은 없겠는가?\n&153\n심려치 마시옵소서. 여 장군의 무용은 천하무쌍하옵니다.\n결코 패하지 않을 것이옵니다.\n&149\n그런가? 모두 경 덕분일세.')
    stage.say('&182\n아!\n&181\n으악!!')
    stage.showUnit(181, 94, 55, 3)
    stage.unit(181).move(90, 55, 3)
    stage.effectSound(38, 1)
    stage.unit(181).setAction(10)
    stage.delay(3)
    stage.unit(153).setAction(0, 1)
    stage.say('&153\n무슨 일이오!?')
    stage.showUnits([
        [
            131,
            95,
            52,
            3],
        [
            129,
            95,
            58,
            3]])
    stage.unitsMove([
        [
            stage.unit(131),
            55,
            52,
            3],
        [
            stage.unit(129),
            55,
            58,
            3]])
    stage.delay(3)
    stage.unit(131).setAction(2)
    stage.say('&131\n왕윤, 역시 여기 있었구나!')
    stage.unit(131).setAction(0)
    stage.delay(3)
    stage.unit(129).setAction(0)
    stage.say('&129\n네놈에게도 이런 날이 오는구나?')
    stage.unit(129).setAction(0)
    stage.delay(3)
    stage.unit(153).setAction(3)
    stage.say('&153\n이각, 곽사!')
    stage.unit(153).setAction(0)
    stage.say('&153\n설마……여포가 패했단 말인가!?')
    stage.unit(131).setAction(3)
    stage.say('&131\n어리석은 놈! 군사 가후의 계책이 있으니\n여포 따위가 무슨 대수냐!')
    stage.unit(131).setAction(0)
    stage.say('&153\n가후라고……?')
    stage.unit(129).setAction(3)
    stage.say('&129\n여포와 네 딸은 벌써 염라대왕을 만나러 갔을 것이다!\n왕윤, 너도 피할 수 없다!')
    stage.unit(129).setAction(0)
    stage.delay(5)
    stage.unit(153).setAction(0, 2)
    stage.say('&153\n……초선…….')
    stage.say('&149\n두 장군은 어찌 궁중에서 살육을 벌이는 것이오?\n어서 멈추시오!')
    stage.unit(131).setAction(6)
    stage.say('&131\n폐하, 왕윤만 죽으면 즉시 군사를 거두겠습니다.')
    stage.unit(131).setAction(0)
    stage.delay(5)
    stage.say('&153\n허……이제는 달리…….')
    stage.effectSound(34, 1)
    stage.unit(153).setAction(14)
    stage.delay(10)
    stage.effectSound(35, 1)
    stage.unit(153).setAction(10)
    stage.say('&153\n아…….')
    stage.delay(10)
    stage.say('&153\n……초선…….')
    stage.say('&149\n와……왕 경……!')
    stage.unit(131).setAction(3)
    stage.say('&131\n하하하하하하!!')
    stage.unit(129).setAction(3)
    stage.say('&129\n하하하!')
    stage.delay(5)
    stage.clsUnit()
    return False


def scene6():
    stage.loadBg(2, 3)
    stage.showUnit(17, 60, 60, 1)
    stage.draw()
    stage.setStageName('낙양  왕윤의 저택')
    stage.say('&17\n어찌 된 일일까요? 자꾸 불길한 느낌이 들어요.')
    stage.unit(17).setAction(6)
    stage.say('&17\n궁에 가신 아버님께 무슨 변이 생긴 것은 아닐까…….')
    stage.showUnit(119, 30, 60, 1)
    stage.say('&119\n초선!')
    stage.unit(17).setAction(0, 1)
    stage.delay(3)
    stage.unit(17).setAction(0, 3)
    stage.unit(119).move(50, 60, 1)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n여 장군, 무슨 일인가요?')
    stage.unit(17).setAction(0)
    stage.say('&119\n초선, 이각 일당의 간계에 당했네.\n우리 군은 이미 전멸했어.')
    stage.unit(119).setAction(17)
    stage.say('&119\n궁궐마저 놈들에게 빼앗기다니!')
    stage.unit(119).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(6)
    stage.say('&17\n네? 아버님께서 아직 궁중에 계신다고요!\n여 장군, 아버님은 무사하신가요!!')
    stage.unit(17).setAction(0)
    stage.delay(3)
    stage.unit(119).setAction(0, 3)
    stage.delay(3)
    stage.unit(119).move(46, 60, 3)
    stage.delay(8)
    stage.unit(119).setAction(0, 1)
    stage.say('&119\n초선…\n왕윤 대인께서 이각 일당에게 몰려 자결하셨다.\n&17\n아버님께서 자결하셨다고요……?')
    stage.unit(17).setAction(0, 2)
    stage.delay(3)
    stage.unit(17).setAction(9)
    stage.say('&17\n아, 아버님…….')
    stage.unit(119).move(50, 60, 1)
    stage.say('&119\n초선, 슬픈 마음은 알겠소.\n하지만 지금은 울 때가 아니오!\n놈들이 곧 이곳에 들이닥칠 것이오!')
    stage.unit(119).setAction(17)
    stage.say('&119\n나와 함께 장안을 빠져나갑시다!')
    stage.unit(119).setAction(0)
    stage.say('&17\n……하지만…….')
    stage.say('&119\n왕 대감께서는 임종하실 때까지도 그대를 걱정하셨다오.')
    stage.unit(119).setAction(17)
    stage.say('&119\n너만은 살아야 한다!\n왕 대인을 위해서라도!')
    stage.unit(119).setAction(0)
    stage.delay(5)
    stage.unit(17).setAction(0)
    stage.say('&17\n……알겠어요, 여 장군.\n아버님……저는 살아남겠어요!')
    stage.unit(119).setAction(3)
    stage.say('&119\n나도 살아남겠다.\n무슨 일이 있어도 이 난세에서 함께 살아남자!\n&119\n가자, 초선!')
    stage.unit(119).setAction(0)
    stage.delay(3)
    stage.unit(17).setAction(0, 3)
    stage.say('&17\n네, 여 장군.')
    stage.unitsMove([
        [
            stage.unit(119),
            30,
            60,
            3],
        [
            stage.unit(17),
            40,
            60,
            3]])
    stage.unit(119).hide()
    stage.unit(17).move(30, 60, 3)
    stage.unit(17).hide()
    stage.delay(5)
    stage.bgSound(-1)
    stage.delay(5)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(127, 140, 83)
    stage.bottomTxt('동탁', False, True, False)
    stage.delay(5)
    stage.showHead(119, 260, 83)
    stage.bottomTxt('그리고 여포', True, False, False)
    stage.delay(5)
    stage.bottomTxt('한때 한나라의 도읍 장안에서 권력을 장악했다.', True, False, False)
    stage.delay(5)
    stage.bottomTxt('그러나 동탁이 여포의 손에 죽자,', True, True, False)
    stage.head(127).hide()
    stage.delay(5)
    stage.bottomTxt('여포도 장안에서 쫓겨났다.', True, False, False)
    stage.head(119).move(356, 83)
    stage.head(119).hide()
    stage.delay(5)
    stage.bottomTxt('동탁과 여포에 이어,', False, True, False)
    stage.showHead(131, 140, 83)
    stage.bottomTxt('이각', True, False, False)
    stage.delay(5)
    stage.showHead(129, 260, 83)
    stage.bottomTxt('그리고 곽사', True, False, False)
    stage.delay(5)
    stage.bottomTxt('장안의 정권을 장악했으나\n그들의 폭정은 예전과 조금도 다르지 않았다.', True, False, True)
    stage.bottomTxt('바로 그때,', False, True, False)
    stage.delay(5)
    stage.showHead(0, 404, 83)
    stage.bottomTxt('반동탁 연합을 떠난 조조는\n동군태수에 취임하여,', True, True, False)
    stage.delay(8)
    stage.bottomTxt('부임지는 연주의 복양이었다.', True, True, True)
    stage.bgSound(-1)
    return False


def scene7():
    stage.bgSound(18)
    stage.loadBg(2, 27)
    stage.showUnits([
        [
            181,
            75,
            59,
            0,
            16],
        [
            202,
            76,
            55,
            3]])
    stage.unit(202).move(75, 55, 3)
    stage.showUnit(182, 75, 51, 2, 16)
    stage.draw()
    stage.setStageName('복양  조조 저택 앞')
    stage.say('&181\n자, 힘을 주어라!\n&182\n이야아……!')
    stage.delay(5)
    stage.showUnit(0, 43, 58, 1)
    stage.delay(5)
    stage.say('&181\n에잇……!\n&0\n너희는 무엇을 하는 것이냐?')
    stage.delay(5)
    stage.unit(181).setAction(0, 3)
    stage.unit(182).setAction(0, 3)
    stage.say('&181\n아, 주군이셨군요!\n간밤에 거센 바람이 군기를 쓰러뜨렸습니다…….\n&182\n다시 세우려는데…….\n너무 무겁습니다…….')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하,\n그 큰 깃발은 두세 사람의 힘으로 들 수 있는 것이 아니네.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.showUnits([
        [
            1,
            60,
            24,
            2],
        [
            16,
            60,
            16,
            2]])
    stage.say('&1\n맹덕!')
    stage.delay(5)
    stage.unitsMove([
        [
            stage.unit(1),
            60,
            48,
            2],
        [
            stage.unit(16),
            60,
            40,
            2]])
    stage.say('&0\n오, 원양!')
    stage.unit(1).setAction(3)
    stage.say('&1\n맹덕, 자네에게 천거할 사람이 있네.')
    stage.unit(1).setAction(0)
    stage.delay(5)
    stage.unit(1).setAction(0, 0)
    stage.say('&1\n이분은…….')
    stage.unit(16).move(72, 40, 1)
    stage.delay(1)
    stage.unit(1).setAction(0, 1)
    stage.say('&1\n응? 이봐, 어디로 가는가!?')
    stage.unit(16).move(72, 56, 1)
    stage.say('&16\n비켜라. 내가 상대하겠다.')
    stage.unit(181).setAction(0, 0)
    stage.say('&181\n그대…… 할 수 있겠는가?')
    stage.unit(182).setAction(0, 2)
    stage.say('&182\n혼자서는 들 수 없네. 다치지 않게 조심하게.')
    stage.delay(5)
    stage.unit(16).setAction(13)
    stage.say('&16\n으, 으오오오오……!!')
    stage.say('&16\n이얍!')
    stage.effectSound(128, 1)
    stage.unit(202).setAction(1)
    stage.delay(10)
    stage.say('&181\n참으로 괴력이로군!\n&182\n어? 세웠다!')
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('&0\n오? 원양, 그대가 말한 이가 이 사람인가!?')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n그렇네. 조금 전 순찰 도중 만났지.')
    stage.delay(10)
    stage.unitsMove([
        [
            stage.unit(182),
            75,
            53,
            2],
        [
            stage.unit(181),
            75,
            59,
            0]])
    stage.unit(181).setAction(16)
    stage.unit(182).setAction(16)
    stage.delay(10)
    stage.unit(16).setAction(0, 3)
    stage.delay(3)
    stage.unit(16).move(65, 56, 3)
    stage.unit(16).setAction(0)
    stage.delay(5)
    stage.unit(16).setAction(6)
    stage.say('&16\n전위라 합니다.')
    stage.unit(16).setAction(0)
    stage.say('&16\n전에 동탁과 맞서 싸우신 조 공의\n깊은 대의에 감복했습니다.')
    stage.unit(16).setAction(6)
    stage.say('&16\n조 대인, 대인을 위해 견마지로를 다하게 해 주십시오!!')
    stage.unit(16).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하!')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(5)
    stage.say('&0\n훌륭하군. 그만한 무예를 지녔으니\n내 곁에 있도록 하게!')
    stage.unit(16).setAction(6)
    stage.say('&16\n큰 영광입니다. 감사합니다, 주군!')
    stage.unit(16).setAction(0)
    stage.say('&0\n원양, 나도 자네에게 소개할 사람이 있네.')
    stage.say('&1\n오, 누구인가?\n&0\n자, 우선 들어가세. 곧 도착할 것이네.')
    stage.clsUnit()
    return False


def scene8():
    stage.loadBg(2, 12)
    stage.showUnits([
        [
            0,
            29,
            54,
            1],
        [
            1,
            46,
            40,
            2],
        [
            20,
            46,
            52,
            3],
        [
            24,
            46,
            58,
            3]])
    stage.draw()
    stage.setStageName('복양  조조 저택')
    stage.say('&0\n내가 조맹덕이오.')
    stage.unit(20).setAction(6)
    stage.say('&20\n저는 순욱, 자는 문약이라 합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n저는 순욱의 조카 순유, 자는 공달이라 합니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(3)
    stage.say('&1\n나는…….')
    stage.delay(3)
    stage.unit(20).setAction(0, 0)
    stage.say('&20\n하후돈 장군이시지요? 명성은 익히 들었습니다.\n&1\n……오.')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하! 그만 치켜세우게.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n내 군사가 되고 싶다고 들었소.\n그렇다면 먼저 두 분께 천하의 형세를 여쭙고 싶소.')
    stage.unit(20).setAction(0, 3)
    stage.say('&20\n알겠습니다.')
    stage.unit(20).setAction(6, 3)
    stage.say('&20\n지금 천하의 세 곳에서 큰 움직임이 있습니다.')
    stage.say('&20\n먼저 하북입니다. 원소와 공손찬이 다투고 있으나\n원소군이 우세하니 하북 통일은 시간문제입니다.\n&1\n흥! 원소가 우세하다니!\n&0\n좋다. 계속 말해 보게!')
    stage.say('&20\n손견이 얼마 전 형주의 유표에게 패했습니다.')
    stage.say('&0\n뭐라고? 손견이 패했다고?\n&20\n예. 게다가 손견은 그 자리에서 전사했습니다.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n그런가…손견이…….')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그 뒤 손씨 집안은 어찌 되었는가?\n&20\n장남 손책이 무리를 이끌고 원술에게 의탁했습니다.\n&0\n원술의 휘하에……? 원수에게 몸을 의탁하다니\n참으로 얄궂은 일이로군…….')
    stage.say('&20\n이어서 장안의 상황은\n제 조카 순유가 설명하겠습니다.')
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n좋습니다.')
    stage.say('&24\n동탁이 죽은 뒤 이각과 곽사가 여포군을 무찌르고\n조정의 실권을 장악했습니다.\n&24\n서량의 맹장 마등도 그들과 싸웠으나 패했습니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(1)
    stage.say('&0\n뭐야, 여포뿐 아니라 마등마저 패했다고!')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.say('&0\n어찌하여 이기지 못했는가?\n&20\n이각에게는 군사 가후가 있습니다.\n두 사람은 필시 가후의 계략에 빠져 패했을 것입니다.')
    stage.say('&0\n가후, 가문화라…….')
    if vars[302]:
        stage.say('&0\n공달, 여포가 패주할 무렵\n왕윤 대인께서 자결하셨다던데\n그 따님은 어찌 되었는가?')
        stage.unit(24).setAction(6)
        stage.say('&24\n아, 왕 대인의 따님 말씀이십니까?\n&0\n음, 이름이 초선이라던가……?\n&24\n생각났습니다. 여포가 달아날 때\n여인 하나를 데리고 갔다는데 아마 그 여인일 겁니다…….')
        stage.unit(24).setAction(0)
        stage.say('&0\n그런가…그녀가 아직 살아 있었군?')
        stage.unit(1).setAction(3)
        stage.say('&1\n맹덕, 일개 여인에게 신경 쓸 것 있나?\n아는 사이인가?')
        stage.unit(1).setAction(0)
        stage.say('&0\n아니, 아무것도 아니다…….')
    stage.unit(24).setAction(6)
    stage.say('&24\n이각과 곽사의 폭정은 예전 동탁에 못지않습니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.say('&0\n음.')
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n난세에는 힘이 정의를 이기는 법일세.\n문약, 내 힘은 어느 정도라고 생각하는가?')
    stage.unit(20).setAction(6)
    stage.say('&20\n세력이 약합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(2)
    stage.say('&1\n뭐라고!')
    stage.unit(0).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하, 원양. 화내지 말게.\n사실이 그렇지 않은가.')
    model.setFace(0)
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(1).setAction(0)
    stage.delay(10)
    stage.say('&0\n문약, 내 세력이 약한 것은 사실이니\n지금은 힘을 키워야 하네.')
    stage.unit(20).setAction(6)
    stage.say('&20\n지금 세상에는 지력, 무력, 그리고 천자의 힘이 있습니다.\n먼저 조정의 신임을 얻고 천자를 받든 뒤\n천자를 끼고 제후들에게 명을 내려야 합니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n그러면 천하의 어진 신하와 용맹한 장수들도\n주군의 휘하로 모여들 것입니다.')
    stage.unit(24).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n먼저 천자의 힘을 빌리자는 것인가……?')
    stage.unit(24).setAction(6)
    stage.say('&24\n장안에서 황건 잔당 토벌을 명하는 조서를 내릴 듯합니다.\n곧 흠차가 오실 것입니다.')
    stage.unit(24).setAction(0)
    stage.delay(5)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n오, 황건 잔당을 토벌하라는 조서가 내렸다고?')
    stage.unit(20).setAction(6)
    stage.say('&20\n조정의 신임을 얻을 좋은 기회입니다.\n받아들이시겠지요?')
    stage.unit(20).setAction(0)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('물론 받아들인다\n조금 더 지켜본다', 0)
    if sel == 1:
        stage.addAmbition(2)
        stage.bgSound(18)
        stage.delay(3)
        stage.say('&0\n받아들이지 않을 까닭이 있겠는가.')
        stage.unit(20).setAction(6)
        stage.say('&20\n그렇습니다.')
        stage.unit(20).setAction(0)
    elif sel == 2:
        stage.addAmbition(-2)
        stage.bgSound(18)
        stage.delay(3)
        stage.say('&0\n음…… 어찌하면 좋을까?\n우리가 이곳에 온 지 얼마 되지 않았거늘.')
        stage.unit(0).setAction(0, 2)
        stage.say('&0\n지금 군사를 일으키는 것이 정녕 좋은 계책일까?')
        stage.unit(20).setAction(6)
        stage.say('&20\n무엇을 망설이십니까?')
        stage.unit(20).setAction(0)
        stage.delay(3)
        stage.unit(24).setAction(6)
        stage.say('&24\n그렇습니다. 기회를 놓쳐서는 안 됩니다.')
        stage.unit(24).setAction(0)
        stage.delay(3)
        stage.unit(0).setAction(0, 1)
        stage.say('&0\n음……좋다. 알겠네. 받아들이겠네.')
        stage.unit(20).setAction(6)
        stage.say('&20\n그렇게 하시면 좋겠습니다.')
        stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n좋다. 문약, 공달!\n두 사람을 내 군사로 삼겠다.\n그 지혜로 우리 군을 보좌해 주게.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(6)
    stage.say('&20\n감사합니다, 대인.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(24).setAction(6)
    stage.say('&24\n예.')
    stage.unit(24).setAction(0)
    stage.delay(5)
    stage.clsUnit()
    stage.bgSound(-1)
    stage.delay(3)
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 397, 79)
    stage.bottomTxt('그 뒤 얼마 지나지 않아', False, False, False)
    stage.delay(5)
    stage.bottomTxt('순유의 말대로\n조조에게 황건 잔당을 토벌하라는 칙명이 내려왔다.', True, True, False)
    stage.delay(10)
    stage.showHead(187, 516, 36)
    stage.delay(5)
    stage.bottomTxt('이에 조조는 대군을 이끌고 출병했고,', True, True, False)
    stage.delay(5)
    stage.head(0).move(396, 36)
    stage.delay(5)
    stage.bottomTxt('황건군의 본거지 청주로 향했다.', True, False, True)
    stage.bgSound(-1)
    return False


def scene9():
    model.unitJoin(16, 0, 0)
    model.unitJoin(20, 0, 0)
    model.unitJoin(24, 0, 0)
    if vars[1013]:
        gvars[0] = 16
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
        gvars[0] = 20
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
        gvars[0] = 24
        gvars[4076] = 0
        gvars[1] = gvars[0]
        gvars[2] = 5259072
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[1] *= 4
        gvars[1] += gvars[2]
        pvars[0] = gvars[1]
        stage.varOper(2, 3, 2, 1, 0)
        stage.setUnitAttr(gvars[0], 17, gvars[3])
        if gvars[3] >= 24 and gvars[3] <= 33:
            gvars[4] = 1
        if gvars[3] >= 65 and gvars[3] <= 66:
            gvars[4] = 1
        if gvars[3] == 69:
            gvars[4] = 1
        if gvars[3] == 74:
            gvars[4] = 1
        if gvars[3] == 0:
            gvars[4] = 2
        if gvars[3] == 57:
            gvars[4] = 2
        if gvars[3] == 67:
            gvars[4] = 2
        if gvars[3] == 71:
            gvars[4] = 2
        if gvars[3] == 79:
            gvars[4] = 2
        gvars[4076] = 0
        gvars[6] = stage.unitAttr(gvars[0], 9)
        gvars[7] = stage.unitAttr(gvars[0], 10)
        gvars[8] = stage.unitAttr(gvars[0], 11)
        gvars[9] = stage.unitAttr(gvars[0], 8)
        gvars[20] = stage.unitAttr(gvars[0], 18)
        gvars[9] -= gvars[20]
        if gvars[4] == 0:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
        if gvars[4] == 1:
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[9] <= 35:
                stage.setUnitAbility(gvars[0], 6, 1, 30)
                stage.setUnitAbility(gvars[0], 8, 1, 30)
        if gvars[4] == 2:
            if gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[7] <= 34:
                stage.setUnitAbility(gvars[0], 11, 0, 35)
            if gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
        if gvars[3] <= 59:
            if gvars[20] >= 15:
                gvars[3] += 1
            if gvars[20] >= 30:
                gvars[3] += 1
        gvars[102] = gvars[3]
        if 0 == 0:
            gvars[4076] = 0
            gvars[2] = 0
            gvars[4081] = 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(0)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(1)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(2)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(3)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(4)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(5)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(6)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(7)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(8)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(9)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(10)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(11)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(12)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(13)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(14)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(15)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(16)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(17)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(18)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(19)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(20)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(21)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(22)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(23)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(24)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(25)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(26)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(27)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(28)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(29)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(30)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(31)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(32)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(33)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(34)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(35)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(36)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(37)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(38)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(39)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(40)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(41)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(42)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(43)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(44)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(45)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(46)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(47)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(48)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(49)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(50)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(51)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(52)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(53)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(54)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(55)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(56)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(57)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(58)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(59)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(60)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(61)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(62)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(63)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(64)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(65)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(66)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(67)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(68)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(69)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(70)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(71)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(72)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(73)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(74)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(75)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(76)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(77)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(78)
            gvars[2] += 1
            if gvars[102] == gvars[2]:
                model.unit(gvars[0]).setPosts(79)
            gvars[4081] = 0
    return False


def scene10():
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
            52,
            1],
        [
            6,
            38,
            46,
            1],
        [
            16,
            38,
            40,
            1],
        [
            20,
            60,
            52,
            3],
        [
            24,
            60,
            46,
            3],
        [
            5,
            60,
            40,
            3]])
    stage.draw()
    stage.setStageName('청주  조조군 본진')
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10, [
        16,
        20,
        24])
    gvars[4076] = 0
    gvars[4057] = 4
    stage.setBattlePos([
        [
            1,
            16],
        [
            2,
            17],
        [
            0,
            16],
        [
            2,
            18],
        [
            1,
            15],
        [
            3,
            17],
        [
            2,
            16],
        [
            1,
            17],
        [
            0,
            17],
        [
            1,
            18],
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


def scene11():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n전위는 무도가라\n움직임이 민첩하고 적의 공격도 잘 피하지.')
    if stage.unitClickTest(6):
        stage.say('&6\n순욱 선생은 책사로서\n공격 책략에 능하시지요.\n다만 근접전은 조금 약하십니다…….')
    if stage.unitClickTest(16):
        stage.say('&16\n저 혼자면 충분합니다. 안심하십시오.\n하하하!')
    if stage.unitClickTest(24):
        stage.say('&24\n저는 풍수사이니 후방 지원은 맡겨 주십시오.')
    if stage.unitClickTest(20):
        stage.say('&20\n앞으로 싸움에 나서기 전에\n전장의 상황을 간단히 설명해 드리겠습니다.\n&0\n음, 부탁하네.\n&20\n예.\n&20\n이번 전장은 대부분 산과 숲으로 이루어져 있어\n이동력이 떨어집니다.\n&20\n전장에는 산이 모두 세 개 있으며\n각 산 정상 부근에 황건적의 마을이 있습니다.')
    if stage.unitClickTest(5):
        stage.say('&5\n황건군은 우리 상대가 아닙니다.\n&0\n결코 방심하지 마라!')
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
