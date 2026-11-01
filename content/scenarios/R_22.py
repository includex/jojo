# Source Generated with Decompyle++
# File: R_22.marshal (Python 3.9)


def scene0():
    model.initLocalVar()
    if vars[1002]:
        stage.infoTransfer(18, '')
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setFace(128)
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
            24,
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
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setEventName('내분을 기다린다')
    stage.setStageName('여양  조조군 본진')
    stage.say('&0\n괘씸한 애송이들,\n용케도 저만한 병력을 모았구나.')
    stage.unit(21).setAction(0, 2)
    stage.say('&21\n이번에는 적을 조금 얕보았군요.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n원소에게 아직 이토록 많은 병력이 남아 있을 줄이야……')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n그렇군요……\n주군, 우선 허도로 물러나시지요.')
    stage.unit(1).setAction(3, 1)
    stage.say('&1\n군을 물린다고?\n하북을 이대로 포기하자는 말인가?')
    stage.unit(1).setAction(0, 1)
    stage.delay(3)
    stage.unit(22).setAction(0, 3)
    stage.say('&22\n아닙니다! 하북은 조만간 우리의 것이 됩니다.')
    stage.unit(24).setAction(6)
    stage.say('&24\n그렇습니다.\n원씨 형제가 내분을 일으키는 모습을 구경하고 싶군요.\n&24\n그러면 우리 군의 피해도 줄어들 것입니다.')
    stage.unit(24).setAction(0)
    stage.say('&0\n음, 좋다.\n&0\n원양, 전군에 명을 내려라. 허도로 돌아간다.')
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n예.')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.loadBg(2, 67)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.info('그 무렵 원담과 곽도 두 사람은……')
    stage.setStageName('여양  교외 산길')
    stage.showUnits([
        [
            113,
            44,
            98,
            1],
        [
            103,
            54,
            98]])
    stage.unitsMove([
        [
            stage.unit(113),
            44,
            30,
            0],
        [
            stage.unit(103),
            54,
            30,
            0]])
    stage.delay(3)
    stage.unit(113).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(2)
    stage.say('&113\n빌어먹을, 원상 그 망할 놈!\n조금만 더 일찍 왔어도\n내가 조조를 이길 수 있었을 텐데!')
    stage.unit(103).setAction(0, 3)
    stage.say('&113\n분명 내가 형인데\n어찌 저놈이 후계자가 되어\n나를 제멋대로 부린단 말이냐!!')
    stage.unit(103).setAction(6)
    stage.say('&103\n맏공자님……\n이것 또한 주군의 유언입니다……')
    stage.unit(113).setAction(0, 2)
    stage.delay(3)
    stage.unit(113).setAction(0, 1)
    stage.say('&113\n유언이라고? 헛소리 마라!!')
    stage.unit(113).setAction(2)
    stage.say('&113\n아버님이 정신이 흐리실 때 하신 말이다.\n그런 경솔한 농담을 진담으로 받아들일 수 있느냐!\n&103\n예……그렇습니다.\n진담으로 볼 수 없습니다.')
    stage.unit(113).setAction(0)
    stage.say('&113\n곽도, 나는 업성으로 돌아가지 않겠다.')
    stage.unit(103).setAction(0)
    stage.say('&103\n오, 그러면 공자께서는 어디로 가십니까……?')
    stage.unit(113).setAction(2)
    stage.say('&113\n기분이 몹시 언짢군.\n원상이 있는 곳으로 돌아가고 싶지 않다!')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(0, 0)
    stage.say('&113\n남피로 가자!\n그곳의 병사들은 모두 내 명을 따른다!')
    stage.unit(113).setAction(3)
    stage.say('&113\n내가 아버님의 후계자다!\n원상, 가증스러운 놈! 두고 보자!\n때가 되면 본때를 보여 주마!\n&103\n음, 훌륭한 기개이십니다!\n큰공자님, 저는 언제까지나 공자님을 따르겠습니다.')
    stage.unit(113).setAction(0)
    stage.unitsMove([
        [
            stage.unit(113),
            47,
            5,
            0],
        [
            stage.unit(103),
            57,
            5,
            0]])
    stage.unit(113).hide()
    stage.unit(103).hide()
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 289, 83)
    stage.showHead(115, 353, 30)
    stage.delay(8)
    stage.showHead(113, 353, 110)
    stage.delay(5)
    stage.bottomTxt('여양 전투 이후 원담은', False, False, False)
    stage.delay(8)
    stage.bottomTxt('원상이 있는 업성으로 돌아가지 않고', True, False, False)
    stage.delay(5)
    stage.head(113).move(459, 110)
    stage.head(113).move(459, 21)
    stage.delay(5)
    stage.bottomTxt('도리어 남피로 가서 주둔했다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 42)
    stage.showUnits([
        [
            113,
            54,
            70],
        [
            103,
            35,
            56,
            1],
        [
            112,
            71,
            50,
            2],
        [
            117,
            71,
            56,
            2]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('남피  원담의 저택')
    stage.delay(3)
    stage.unit(113).setAction(3)
    stage.say('&113\n나는 원씨 가문의 장남이다.\n후계자는 바로 나다!')
    stage.unit(113).setAction(0)
    stage.say('&113\n그러니 반드시 원상을 몰아내고\n원씨 가문의 실권을 내 손에 넣어야 한다!\n그대들의 생각은 어떠한가?')
    stage.unit(103).setAction(6)
    stage.say('&103\n예.')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(112).setAction(6)
    stage.say('&112\n진심으로 주군께 충성을 다하겠습니다.')
    stage.unit(112).setAction(0)
    stage.delay(3)
    stage.unit(117).setAction(6)
    stage.say('&117\n…………')
    stage.unit(117).setAction(0)
    stage.say('&113\n문제는 원상을 어떻게 상대하느냐인데…….')
    stage.unit(103).setAction(6)
    stage.say('&103\n주군께 아룁니다.\n정면으로 싸우면 이길 수는 있겠으나\n상당한 병력을 잃게 될 것입니다.\n&103\n그러니…… 조조와 동맹을 맺는 것이 어떻겠습니까?\n&113\n뭐라고?\n그 늙은 도적과 함께 싸우자고?\n&103\n아닙니다. 조조 혼자 원상 일당과 싸우게 하는 것입니다.\n&113\n으음………\n&103\n원상 일당이 사라지면\n우리는 동맹을 이용해 힘을 기른 뒤\n다시 조조를 무찌르는 것이지요.\n&113\n좋다. 참으로 일석이조의 계책이로구나.\n&103\n감사합니다, 주군.')
    stage.unit(103).setAction(0)
    stage.delay(3)
    stage.unit(113).setAction(3)
    stage.say('&113\n하하하!\n좋다! 즉시 사자를 보내 조조와 동맹을 맺어라.')
    stage.unit(113).setAction(0)
    stage.delay(3)
    stage.unit(117).setAction(6)
    stage.say('&117\n주군, 제가 사자로 가게 해 주십시오.\n&113\n오, 신비?\n음. 하지만……\n반드시 성공해야 한다. 실패는 용납하지 않겠다.\n&117\n예……예………\n(허, 권력 다툼에만 눈이 멀어\n천하의 형세를 모르는구나.)\n&117\n(이런 사람을 계속 따라서는 앞날이 없겠군.)')
    stage.say('&113\n좋다! 즉시 떠나게.\n&117\n예………')
    stage.unit(117).setAction(0)
    stage.delay(3)
    stage.unit(112).setAction(0, -1)
    stage.say('&112\n주군을 위해\n반드시 동맹을 성사시켜야 한다.')
    stage.unit(113).setAction(3)
    stage.say('&113\n하하하!\n원상! 감히 맏형에게 맞서느냐?\n참으로 어리석구나. 하하하!\n&117\n…………')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            1,
            52,
            70],
        [
            15,
            58,
            70],
        [
            2,
            64,
            70],
        [
            23,
            70,
            70],
        [
            20,
            52,
            40,
            2],
        [
            22,
            58,
            40,
            2],
        [
            24,
            64,
            40,
            2],
        [
            21,
            70,
            40,
            2],
        [
            117,
            48,
            55,
            3,
            6]])
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n오, 나와 동맹을 맺고 싶다고……?\n&117\n그렇습니다.\n&0\n음…………')
    stage.unit(117).setAction(0)
    stage.say('&117\n하지만 동맹을 맺어도\n진심은 아닐 것입니다.')
    model.setFace(1)
    stage.say('&0\n뭐라고?\n&117\n원담은 승상을 이용해 원상을 없애려는 것뿐입니다.\n&117\n진심으로 동맹을 맺으려는 것이 아니니\n조만간 약속을 깨고 승상을 공격할 것입니다.')
    stage.unit(0).setAction(3)
    model.setFace(0)
    stage.say('&0\n입 다물어라!')
    stage.unit(0).setAction(0)
    stage.say('&0\n신비, 그대는 원담의 가신인가?')
    stage.say('&117\n저는 그런 자를 주인으로 인정하지 않습니다.\n&117\n형제끼리 권력과 이익만 다투며\n백성의 고통은 조금도 돌아보지 않으니\n그런 자를 섬기는 것은 어리석은 짓입니다.\n&0\n그랬군…………')
    stage.say('&0\n좋다. 원담과 동맹을 맺겠다.\n&117\n예?')
    model.setFace(3)
    stage.unit(0).setAction(3, 1)
    stage.say('&0\n원담이 모처럼 계책을 부렸으니\n한번 속아 주도록 하지.')
    stage.unit(0).setAction(0)
    stage.say('&117\n하지만…………')
    model.setFace(0)
    stage.say('&0\n나 역시 이 동맹을 이용할 생각이네.\n&0\n신비, 원상이 있는 업성을 공격하려면\n그대의 힘도 필요하네.\n나를 도와주게.')
    stage.unit(117).setAction(6)
    stage.say('&117\n예! 명을 받들겠습니다…….')
    stage.unit(117).setAction(0)
    stage.say('&0\n좋다.\n문약, 원담에게 사자를 보내 동맹을 맺고\n함께 출병하여 원상을 협공하자고 전하게.')
    stage.unit(20).setAction(6)
    stage.say('&20\n예, 즉시 시행하겠습니다.')
    stage.unit(20).setAction(0)
    stage.delay(3)
    stage.unit(117).setAction(0, 0)
    stage.say('&117\n그는 출병하지 않을 것입니다.\n&20\n하하하, 아마 그렇겠지요.\n&117\n그렇다면 승상께서는 어찌……?')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하.')
    stage.unit(117).setAction(0, 3)
    stage.delay(3)
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&0\n그가 출병을 거절하면\n내가 그를 칠 명분이 생기지.\n&117\n예……? (이토록 깊은 지략은\n돌아가신 원소 주군께서도 따르지 못할 터.\n그 아들들은 말할 것도 없겠군.)')
    stage.effectSound(200, 255)
    stage.bgSound(18)
    stage.delay(3)
    stage.showUnit(8, 95, 55, 3)
    stage.say('&8\n아버님!')
    stage.unit(117).setAction(0, 1)
    stage.unit(117).move(48, 60, 2)
    stage.delay(3)
    stage.unit(117).setAction(0, 0)
    stage.unit(8).move(48, 55, 3)
    stage.unit(8).setAction(0)
    stage.say('&0\n자환, 무슨 일이냐?\n&8\n아버님, 이번 싸움에는 저도 참전하게 해 주십시오.\n&0\n네가?\n&8\n예.\n아버님께서 지으신 맹덕신서의 병법을 익혔습니다.\n부디 저를 출전시켜 주십시오.\n&0\n맹덕신서는 지니고 있느냐?\n&8\n예.\n&0\n음………')
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('참전을 허락한다\n아직 이르다', 0)
    if sel == 1:
        stage.addAmbition(-2)
        stage.bgSound(18)
        stage.delay(3)
        stage.say('&0\n음, 좋다.\n전장에 나가 직접 배워 보거라.\n&8\n예, 감사합니다. 아버님.\n&0\n자환, 훌륭히 싸워야 한다.\n원소의 아들들과\n전장에서 실력을 겨뤄 보아라.\n&8\n예, 아버님의 명성에 누를 끼치지 않겠습니다.')
    elif sel == 2:
        stage.addAmbition(2)
        stage.bgSound(18)
        stage.delay(3)
        stage.say('&0\n안 된다!\n네가 전장에 나서기에는 아직 이르다.\n&8\n하지만 아버님………\n&0\n그만하거라!\n병법을 조금 익혔다고는 하나\n전장은 장난삼아 나서는 곳이 아니다!\n&8\n…………\n&21\n주군의 마음은 알겠습니다.\n허나 자환 공자께서 이토록 뜻을 품으셨으니\n그 뜻을 펼치도록 해 주시는 것이 어떻겠습니까?\n&20\n그렇습니다. 적자인 조비 공자께서 출전하시면\n장병들의 사기도 크게 오를 것입니다.\n&0\n호오?\n좋다. 그대들이 그리 말한다면\n이 아이의 출전을 허락하겠다.\n&8\n아! 감사합니다, 아버님.\n&0\n다만 네 스스로 원해서 나서는 것이니\n전장에서 내 이름을 더럽혀서는 안 된다.\n&8\n예! 아버님의 가르침을 명심하겠습니다.')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하.\n좋다, 출진하라!')
    model.setFace(0)
    model.unitJoin(8, 0, 0)
    if vars[1013]:
        gvars[0] = 8
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
    stage.setJoinEquip(8, -1, 0, -1, 0, 21)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 315, 129)
    stage.showHead(115, 379, 28)
    stage.delay(8)
    stage.head(0).move(315, 28)
    stage.delay(5)
    stage.bottomTxt('조조군은 원상의 본거지 업성을 공격했다.', False, False, True)
    stage.bgSound(-1)
    return False


def scene4():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            115,
            53,
            28,
            2],
        [
            106,
            38,
            48,
            1],
        [
            118,
            53,
            48]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('업성@원상 저택')
    stage.delay(3)
    stage.unit(115).setAction(2)
    stage.say('&115\n뭐……뭐라고……?\n큰형님 원담이 조조와 동맹을 맺었다고!?')
    stage.unit(115).setAction(0)
    stage.say('&118\n예.\n&106\n그렇다면……?')
    stage.unit(118).setAction(0, 3)
    stage.say('&118\n조조가 허도를 출발하여\n이쪽으로 진군하고 있습니다.')
    stage.say('&115\n으으으, 형님이 어찌 나를 이리 대하신단 말인가……?')
    stage.unit(118).setAction(0, 0)
    stage.delay(3)
    stage.unit(115).setAction(2)
    stage.say('&115\n더는 그자를 형으로 여기지 않겠다!')
    stage.unit(106).setAction(6)
    stage.say('&106\n주군, 조조를 상대할 방책을 마련해야 합니다!')
    stage.unit(115).setAction(0)
    stage.say('&115\n좋다. 내가 직접 군사를 이끌고 조조와 맞서겠다!\n&115\n심배, 저곡.\n너희는 성에 남아 나를 지원하라!\n&106\n예.')
    stage.unit(106).setAction(0)
    stage.say('&118\n예.')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene5():
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
            8,
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
    stage.setStageName('업성 교외  조조군 본진')
    stage.say('&0\n문약, 원상군에 무슨 움직임이 있는가?')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n예.\n&20\n심배와 저곡은 업성을 지키고\n원상이 직접 나서 우리 군을 맞이합니다.')
    stage.unit(1).setAction(3)
    stage.say('&1\n맞아 싸우라고? 적은 보이지도 않는데?')
    stage.unit(1).setAction(0)
    stage.delay(3)
    stage.unit(20).setAction(0, 3)
    stage.say('&20\n원상이 방향을 잘못 잡은 모양입니다…….\n&1\n뭐?\n&0\n음?')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n하하하하.')
    stage.unit(0).setAction(0)
    stage.say('&0\n재미있는 녀석이로군.\n원소의 아들들이 제법 마음에 드는구나.\n자환, 너도 저들을 좀 본받아라.')
    stage.unit(8).setAction(0, 2)
    stage.say('&8\n아버님, 무슨 말씀이십니까!?')
    stage.unit(0).setAction(3)
    stage.say('&0\n하하하. 농담일세, 농담이야!')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&0\n좋다! 공격을 개시하라.\n원상을 깜짝 놀라게 해 주자.')
    stage.unit(8).setAction(0, 1)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 11, [
        22,
        8])
    gvars[4076] = 0
    gvars[4057] = 22
    stage.setBattlePos([
        [
            17,
            18],
        [
            18,
            18],
        [
            17,
            16],
        [
            15,
            16],
        [
            19,
            16],
        [
            14,
            17],
        [
            20,
            17],
        [
            16,
            17],
        [
            18,
            17],
        [
            15,
            19],
        [
            19,
            19],
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


def scene6():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라.')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n업성의 장수 저곡은\n저수의 아들이라는 모양이네.\n&0\n오, 저수의 아들이라고……?')
    if stage.unitClickTest(8):
        stage.say('&8\n아버님,\n하후연 숙부님께 배운 궁술을 지켜봐 주십시오.\n&0\n음.')
    if stage.unitClickTest(20):
        stage.say('&0\n문약, 전장에 어떤 특징이 있는가?\n&20\n예.\n&20\n업성 자체의 수비가 견고함은 물론이고\n성 밖에도 녹채를 세운 듯합니다.\n&0\n음.')
    if stage.unitClickTest(15):
        stage.say('&15\n형제끼리 무엇 때문에 다투는 겁니까.\n저는 도무지 이해할 수 없습니다.\n&0\n…………')
    if stage.unitClickTest(21):
        stage.say('&21\n원상을 얕보아서는 안 됩니다.\n원소가 그를 후계자로 삼은 데는\n이유가 있을 것입니다.\n&0\n음, 알고 있네.')
    if stage.unitClickTest(22):
        stage.say('&22\n조비 공자께서 드디어 전장에 나서시는군요.\n&0\n음.')
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
