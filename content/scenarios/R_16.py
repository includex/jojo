# Source Generated with Decompyle++
# File: R_16.marshal (Python 3.9)


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
            2,
            38,
            48,
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
            3]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.setEventName('유비를 그리워하며')
    stage.delay(3)
    stage.setStageName('서주 교외  조조군 본진')
    stage.say('&0\n이번 싸움에 모두들 수고가 많았다.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n유비를 제거하지 못해 안타깝습니다…….\n&0\n음…….\n&0\n문원, 운장은 어찌 되었는가?\n&2\n예.')
    stage.unit(20).setAction(0, 3)
    stage.unit(2).move(49, 48, 1)
    stage.delay(3)
    stage.unit(2).setAction(0, 2)
    stage.say('&2\n유비의 두 부인과 함께 있습니다.\n&0\n그런가?\n좋다. 이곳으로 데려오게.\n&2\n예.')
    stage.unit(2).move(49, 16, 0)
    stage.unit(2).hide()
    stage.say('&0\n모두 잠시 물러가 있게.')
    stage.unit(20).setAction(0, 2)
    stage.say('&20\n예.')
    stage.unit(22).setAction(0, 2)
    stage.say('&22\n예.')
    stage.unit(1).setAction(0, 2)
    stage.say('&1\n……예.')
    stage.unit(1).setAction(0, 1)
    stage.unit(22).setAction(0, 3)
    stage.unit(20).setAction(0, 3)
    stage.delay(3)
    stage.unit(1).move(49, 54, 1)
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(1),
            49,
            42,
            0],
        [
            stage.unit(20),
            49,
            54,
            3],
        [
            stage.unit(22),
            49,
            48,
            3]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(1),
            49,
            16,
            0],
        [
            stage.unit(20),
            49,
            26,
            0],
        [
            stage.unit(22),
            49,
            21,
            0]])
    stage.unit(1).hide()
    stage.unitsMove([
        [
            stage.unit(22),
            49,
            16,
            0],
        [
            stage.unit(20),
            49,
            21,
            0]])
    stage.unit(22).hide()
    stage.unit(20).move(49, 16, 0)
    stage.unit(20).hide()
    stage.delay(10)
    stage.showUnit(2, 49, 16, 2)
    stage.unit(2).move(49, 21, 2)
    stage.showUnit(3, 49, 16, 2)
    stage.unitsMove([
        [
            stage.unit(2),
            49,
            52,
            2],
        [
            stage.unit(3),
            49,
            47,
            2]])
    stage.delay(1)
    stage.unitsMove([
        [
            stage.unit(2),
            41,
            52,
            3],
        [
            stage.unit(3),
            49,
            52,
            2]])
    stage.delay(3)
    stage.unit(2).setAction(0, 1)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n운장,\n내 진영에 남아 힘을 보태 주지 않겠는가?')
    stage.unit(0).setAction(0)
    stage.say('&3\n……내게는 세 가지 조건이 있소.\n승상께서 모두 들어주신다면\n여기에 남겠소.')
    stage.effectSound(203, 255)
    stage.bgSound(11)
    stage.delay(3)
    stage.say('&0\n음. 세 가지 조건이 무엇인가?\n&3\n첫째, 이 관우는 한에 항복하는 것이지 조조에게 항복하는 것이 아니오.\n&0\n음.\n&3\n둘째, 주군께서 두 분 부인을 내게 맡기셨으니\n두 분의 안전을 보장해 주시오.\n&0\n오, 그것도 어려울 것 없지.\n&3\n셋째,\n&3\n우리 주군 유비께서 살아 계심을 알게 되면\n즉시 이곳을 떠날 수 있도록 허락해 주시오.')
    model.setFace(1)
    stage.say('&0\n……!!\n&3\n어떠시오?')
    model.setFace(0)
    sel = stage.choice('좋다\n……', 0)
    if sel == 1:
        stage.addAmbition(2)
        stage.bgSound(-1)
        stage.effectSound(203, 0)
        model.setFace(0)
        stage.delay(3)
        stage.say('&0\n좋다. 그 세 가지 조건을 받아들이겠다.')
        stage.unit(3).setAction(6)
        stage.say('&3\n무리한 청을 받아 주셔서 감사합니다, 승상.\n&3\n그럼 두 분 형수님께 이 소식을 전하겠습니다.')
    elif sel == 2:
        stage.addAmbition(-2)
        stage.bgSound(-1)
        model.setFace(0)
        stage.effectSound(203, 0)
        stage.delay(3)
        stage.say('&0\n…….\n&3\n……아무래도 받아들이실 뜻이 없으시군요.\n그렇다면 어서 제 목을 베십시오.\n&2\n주……주군!\n&0\n잠깐 기다리게, 운장!\n……좋네. 그 세 가지 조건을 받아들이겠네.\n&3\n승상, 후회하지 않으시겠습니까?\n&0\n물론이지. 이 조조는 한번 한 말은 반드시 지키네.')
        stage.unit(3).setAction(6)
        stage.say('&3\n무리한 청을 받아 주셔서 감사합니다, 승상.\n&3\n그럼 두 분 형수님께 이 소식을 전하겠습니다.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    stage.unit(3).move(49, 47, 0)
    stage.say('&3\n(형님과 셋째는 무사할까…….)')
    stage.unit(3).move(49, 16, 0)
    stage.unit(3).hide()
    stage.delay(3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n유비가 살아 있음을 알게 되는 순간\n운장은 내 곁을 떠나겠지…….')
    stage.unit(2).setAction(0, 2)
    stage.say('&2\n운장은 참으로 의리가 깊은 사람이군…….')
    stage.unit(0).setAction(0, 0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n그것도 좋겠군.\n운장에게 유비와 나의 차이를\n깨닫게 해 주겠네.')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene1():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(0, 490, 150)
    stage.showHead(32, 554, 150)
    stage.showHead(101, 350, 27)
    stage.bottomTxt('조조에게 패한 유비는', False, False, False)
    stage.delay(5)
    stage.head(32).move(554, 70)
    stage.head(32).move(414, 27)
    stage.delay(8)
    stage.bottomTxt('업의 원소에게 몸을 의탁하러 갔다.', True, False, True)
    stage.bgSound(-1)
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            101,
            52,
            28,
            2],
        [
            106,
            38,
            48,
            1]])
    stage.draw()
    stage.effectSound(203, 0)
    stage.delay(3)
    stage.setStageName('업 원소 저택')
    stage.showUnit(181, 52, 92)
    stage.say('&181\n주군, 유비께서 오셨습니다.\n&101\n음, 어서 모셔라.\n&181\n예.')
    stage.unit(181).hide()
    stage.showUnit(32, 52, 98)
    stage.delay(10)
    stage.unit(32).move(52, 48, 0)
    stage.say('&32\n원소 대인……제 힘이 부족하여\n서주를 조조에게 빼앗기고 말았습니다.\n&101\n음, 들었소.\n참으로 큰 재난이었구려.\n&106\n유비 대인, 혼자 오셨습니까?')
    stage.unit(32).setAction(0, 3)
    stage.say('&32\n그렇습니다.\n의제 관우와 장비 모두 전장에서 흩어졌습니다.\n&101\n그랬구려.')
    stage.unit(32).setAction(0, 0)
    stage.say('&32\n살아 있는지도 모르겠습니다…….\n&101\n그만 너무 상심하지 마시오.\n관우와 장비는 천하에 이름난 호걸이니\n무사할 것이오.\n&32\n그러기를 바랄 뿐입니다…….\n&101\n당분간 이곳에 머무시오.\n&32\n……예. (관우, 장비…….)')
    stage.effectSound(203, 255)
    stage.clsUnit()
    return False


def scene2():
    stage.loadBg(2, 63)
    stage.draw()
    stage.effectSound(200, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부 부근')
    stage.showUnit(0, 85, 66, 3)
    stage.unit(0).move(73, 66, 3)
    stage.showUnit(3, 81, 66, 3)
    stage.unitsMove([
        [
            stage.unit(0),
            65,
            66,
            3],
        [
            stage.unit(3),
            73,
            66,
            3]])
    stage.showUnit(155, 81, 66, 3)
    stage.unitsMove([
        [
            stage.unit(0),
            57,
            66,
            3],
        [
            stage.unit(3),
            65,
            66,
            3],
        [
            stage.unit(155),
            73,
            66,
            3]])
    stage.showUnit(154, 81, 66, 3)
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n운장, 이 저택을 자네에게 주겠네.\n오늘부터 이곳에 머물게.\n&3\n감사하오, 승상.\n&0\n음, 그럼 이만…….')
    stage.unit(0).move(20, 66, 3)
    stage.unit(0).hide()
    stage.unitsMove([
        [
            stage.unit(3),
            57,
            66,
            3],
        [
            stage.unit(155),
            65,
            66,
            3],
        [
            stage.unit(154),
            73,
            66,
            3]])
    stage.delay(3)
    stage.unit(3).setAction(0, 1)
    stage.say('&3\n두 분 형수님께서는 이곳에 머무십시오.\n&155\n그럼 둘째 숙부님께서는 어디에 머무시나요?\n&3\n저는 문간방에서 지내겠습니다.\n&154\n하지만 이곳은 조조 대감께서……?\n&3\n형님의 행방을 모르는 지금\n두 분 형수님이 제 주군이십니다.\n&3\n주군을 편안한 거처에 모시는 일로\n조 승상께서 저를 나무라시지는 않을 것입니다.\n&155\n둘째 숙부님…….\n&154\n서방님만 믿겠습니다…….')
    stage.effectSound(200, 255)
    stage.clsUnit()
    return False


def scene3():
    stage.bgSound(16)
    stage.delay(3)
    stage.loadBg(0, 35)
    stage.draw()
    stage.info('허도로 돌아온 조조는 갖은 방법으로 관우를 회유했다.')
    stage.info('조조는 관우에게 호화로운 저택을 주고\n사흘에 한 번 작은 잔치, 닷새에 한 번 큰 잔치를 열며\n날마다 금은보화를 선물했다.')
    stage.info('허나 관우의 마음은 움직이지 않았고\n조조의 근심은 날로 깊어졌다.')
    stage.bgSound(-1)
    stage.loadBg(2, 25)
    stage.showUnits([
        [
            2,
            56,
            55,
            1],
        [
            3,
            78,
            55,
            1]])
    stage.draw()
    stage.effectSound(201, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부 정원')
    stage.showUnit(0, 66, 80)
    stage.delay(10)
    stage.say('&0\n아니, 두 사람 모두 여기 있었나?')
    stage.unit(2).setAction(0, 2)
    stage.say('&2\n오, 주군.\n&0\n문원.\n&2\n예?')
    stage.unit(2).move(56, 80, 1)
    stage.delay(5)
    stage.unit(0).move(62, 80, 3)
    stage.say('&0\n밖에 매어 둔 말이 운장의 말인가?\n&2\n예.\n&0\n운장 같은 무장이 저런 변변찮은 말을 타다니\n너무도 어울리지 않는군.\n&0\n가서 그 말을 끌고 오너라.\n&2\n어느 말 말씀이십니까……?\n&0\n아무도 타지 못하는 그 말 말이다.\n&2\n그 말을 운장에게 주시려고요!?\n&0\n그렇다.\n&2\n알겠습니다.')
    stage.unit(2).move(56, 98, 2)
    stage.unit(2).hide()
    stage.unit(0).move(62, 55, 1)
    stage.say('&0\n운장.')
    stage.unit(3).setAction(0, 3)
    stage.say('&3\n아? 조 승상이셨군요.\n멀리 나와 맞이하지 못했소.\n마침 생각에 잠겨 있었습니다……\n&0\n아, 괜찮네.')
    stage.unit(0).move(68, 55, 1)
    stage.unit(0).setAction(0)
    stage.say('&0\n허도에서 지내기는 어떠한가?\n&3\n승상께서 보살펴 주신 덕분에\n매우 편안히 지내고 있소.\n&0\n그런가……다행이군.')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n참, 허도는 마음에 드는가?\n&3\n그렇습니다. 허도는 참으로 번화하군요.\n마치 꿈속의 도성 같습니다.')
    stage.unit(3).setAction(0, 1)
    stage.say('&3\n형님을 그리는 마음 때문에\n꿈속을 헤매는 듯한 느낌이 드는 것이겠지…….')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n……조금도 달라지지 않았군.')
    stage.showUnits([
        [
            2,
            60,
            87],
        [
            198,
            60,
            92]])
    stage.say('&2\n주군.\n말을 끌고 왔습니다.')
    stage.unit(0).setAction(0, 2)
    stage.unitsMove([
        [
            stage.unit(2),
            60,
            55,
            1],
        [
            stage.unit(198),
            60,
            63,
            0]])
    stage.delay(3)
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n오, 수고했다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n운장.\n이 말을 자네에게 주겠네.')
    stage.unit(3).setAction(0, 3)
    stage.say('&3\n예?')
    stage.delay(5)
    stage.unit(3).move(78, 63, 2)
    stage.delay(1)
    stage.unit(0).setAction(0, 2)
    stage.unit(2).setAction(0, 2)
    stage.unit(3).move(68, 63, 3)
    stage.say('&3\n이……이 말은 설마……\n&0\n그렇네.\n여포가 타던\n하루에 천 리를 달린다는 적토마일세.')
    stage.unit(3).setAction(0, 0)
    stage.say('&3\n승상께서 제게 내려 주신다는 말씀이십니까?\n&0\n음.\n&0\n여포를 토벌할 때 얻은 말인데\n아무도 제대로 타지 못했네.')
    stage.unit(0).setAction(3)
    model.setFace(3)
    stage.say('&0\n그대라면 능히 탈 수 있을 것이오.')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(3).setAction(6)
    stage.say('&3\n승상의 후의에 감사드립니다.')
    stage.unit(3).setAction(0)
    stage.say('&3\n지금 타 보아도 되겠습니까?\n&0\n음.')
    stage.unit(3).setAction(0, 3)
    stage.delay(3)
    stage.unit(3).move(62, 63, 3)
    stage.say('&3\n아, 과연 명마로군요.\n조 승상, 참으로 감사하오.\n&0\n마음에 드는 모양이군.')
    stage.unit(3).setAction(0, 0)
    stage.say('&3\n그렇습니다.\n&3\n그러면 형님의 행방을 알게 되는 즉시\n찾아뵐 수 있겠지요.')
    stage.unit(3).setAction(0, 2)
    stage.delay(3)
    stage.unitsMove([
        [
            stage.unit(3),
            62,
            98,
            2],
        [
            stage.unit(198),
            60,
            98,
            2]])
    stage.unit(3).hide()
    stage.unit(198).hide()
    model.setFace(0)
    stage.say('&0\n………….\n&2\n운장, 자네………….')
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene4():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            2,
            49,
            40,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n문원, 운장은 정녕 내 마음을 모르는 것인가?')
    stage.unit(0).setAction(0, 3)
    stage.say('&0\n이제 나도 어찌할 방도가 없구나……\n&2\n주군, 제가 운장의 처소로 가서\n그의 진심을 알아보면 어떻겠습니까?\n&2\n운장은 승상께 큰 은혜를 입었습니다.\n감사할 줄 모르는 자는 아닐 것입니다.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n그렇기도 하군.\n나를 도와주겠는가……?')
    stage.unit(2).setAction(6)
    stage.say('&2\n제게 맡겨 주십시오.')
    stage.unit(2).setAction(0)
    stage.delay(3)
    stage.unit(2).move(81, 40, 2)
    stage.unit(2).hide()
    stage.say('&0\n유비는 대체 어떤 인물이란 말인가……?')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene5():
    stage.loadBg(2, 0)
    stage.showUnit(3, 40, 54, 1)
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  관우 저택')
    stage.say('&2\n관 장군 계시오?\n&3\n누구시오?')
    stage.showUnit(2, 90, 54, 3)
    stage.say('&2\n나일세.\n&3\n오, 문원이었군.')
    stage.unit(2).move(52, 54, 3)
    stage.say('&3\n급한 일인가?\n&2\n아니, 별일은 없네.\n&3\n그런가.\n&3\n오늘 조 승상께서 또 선물을 보내 주셔서\n감사 서찰을 쓰던 참일세.\n&2\n그런가?\n그런데 승상의 큰 은혜를 입었으니\n어찌할 생각인가?\n&3\n승상께는 더없이 감사하고 있네.\n&2\n하지만 유현덕의 소식을 알게 되면\n이곳을 떠날 것이 아닌가?\n&3\n……그렇네.\n&2\n승상을 위해 힘쓸 생각은 없는가?\n&3\n내가 어디에 있든\n마음은 언제나 형님과 함께일세.\n&2\n음…….\n&3\n……문원 형.\n&3\n떠나기 전에\n반드시 전공을 세워\n승상의 은혜에 보답하겠네.\n&3\n승상께 그리 전해 주게.\n&2\n……알겠네.')
    stage.unit(2).move(90, 54, 1)
    stage.say('&2\n실례하겠습니다.')
    stage.unit(2).move(98, 54, 1)
    stage.unit(2).hide()
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene6():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            20,
            49,
            40,
            2],
        [
            2,
            49,
            55,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.say('&0\n그런가……?\n관우가 그렇게 말했단 말이지?')
    stage.unit(2).setAction(6)
    stage.say('&2\n그렇습니다.')
    stage.unit(2).setAction(0)
    stage.delay(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n참으로 의리가 하늘을 찌르는군.\n그러니 더욱 곁에 두고 싶어지는 게야.')
    stage.unit(20).setAction(6)
    stage.say('&20\n주군.')
    stage.unit(0).setAction(0)
    stage.say('&0\n무슨 일인가?\n&20\n관우를 붙잡아 둘 묘책이 있습니다.\n&0\n뭐라고?')
    stage.unit(20).setAction(0)
    stage.say('&20\n관우는 정과 의리를 중히 여기는 호걸입니다.\n&20\n공을 세워 주군의 은혜를 갚기 전에는\n허도를 떠나지 않을 것입니다.\n&0\n그렇겠지.\n&20\n그렇다면 거꾸로 생각하면 어떻겠습니까?\n관우에게 공을 세울 기회를 주지 않으면 됩니다.\n&0\n음………….')
    stage.unit(2).setAction(0, 0)
    stage.say('&2\n이대로 운장을 눌러앉힐 생각이오?')
    stage.unit(20).setAction(6)
    stage.say('&20\n아닙니다, 문원 장군.\n운장이 주군께 진심으로 복종할 때까지입니다.')
    stage.unit(20).setAction(0)
    stage.unit(2).setAction(0, 3)
    stage.delay(3)
    stage.say('&0\n알겠네.\n당분간 운장은 전장에 데려가지 않도록 하세.')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene7():
    stage.loadBg(2, 7)
    stage.showUnits([
        [
            101,
            52,
            28,
            2],
        [
            106,
            38,
            48,
            1],
        [
            32,
            38,
            54,
            1],
        [
            110,
            67,
            48,
            3],
        [
            105,
            67,
            54,
            3]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.info('바로 그때였다.', 3)
    stage.setStageName('업 원소 저택')
    stage.say('&101\n곧 조조를 토벌하려 한다.\n그대들의 생각은 어떠한가?')
    stage.unit(110).setAction(6)
    stage.say('&110\n주군.\n&110\n지금은 공격보다 수비가 중요한 때입니다.\n국방을 튼튼히 하고 국력을 길러\n군사를 조련하며 훗날의 싸움에 대비해야 합니다.\n&110\n그렇게 이삼 년이 지나면\n허도의 조조는 지치고 쇠약해질 것이니\n그때 단숨에 무너뜨리는 것입니다.')
    stage.unit(110).setAction(0)
    stage.say('&101\n음.')
    stage.unit(106).setAction(6)
    stage.say('&106\n간적 조조는 천자를 능멸하고 있다.\n어찌 2년이나 더 두고 본단 말인가?')
    stage.unit(106).setAction(0)
    stage.delay(3)
    stage.unit(32).setAction(6)
    stage.say('&32\n심배의 말이 옳습니다.\n하루빨리 그를 토벌해야 합니다.')
    stage.unit(32).setAction(0)
    stage.say('&101\n음.')
    stage.unit(106).setAction(6)
    stage.say('&106\n우리 병력은 조조군에 비해\n압도적으로 우세합니다.')
    stage.unit(110).setAction(2)
    stage.say('&110\n심배!\n&110\n조조군은 수가 적다 해도\n백전노장들이 이끄는\n정예 부대입니다.\n&110\n지나치게 방심하면\n위험을 자초할 것입니다!')
    stage.unit(106).setAction(0)
    stage.say('&106\n오!\n우리 군은 공손찬도 무찔렀소.\n아직도 우리 힘을 믿지 못하겠소?')
    stage.unit(110).setAction(0)
    stage.say('&110\n으음…….')
    stage.unit(110).setAction(2)
    stage.say('&110\n그런 뜻이 아닙니다!\n&106\n그렇다면 그대의 식견이 너무 좁은 것이오.\n&101\n전풍, 지금 우리 군은 강성하고\n사기도 하늘을 찌른다.\n그런데도 조조를 이기지 못한다는 말이냐?')
    stage.unit(110).setAction(0)
    stage.say('&110\n주군, 결코 조조를 얕보아서는 안 됩니다.\n그는 수많은 대전을 치러\n경험이 매우 풍부합니다.')
    stage.unit(101).setAction(0, 0)
    stage.say('&101\n……………。')
    stage.delay(5)
    stage.unit(101).setAction(0, 2)
    stage.say('&101\n이미 결정했다.\n조조 토벌을 준비하라.')
    stage.unit(110).setAction(3)
    stage.say('&110\n주…… 주군!!\n&101\n전풍, 돌아가 반성하라.')
    stage.unit(110).setAction(0)
    stage.delay(3)
    stage.unit(110).setAction(0, 1)
    stage.delay(3)
    stage.unit(110).setAction(2)
    stage.say('&110\n어찌 이리 경솔하게 결정하십니까……!\n&101\n다른 이들은 모두 가서 출전을 준비하라.')
    stage.unit(101).setAction(17)
    stage.say('&101\n간적 조조를 토벌하겠다!')
    stage.unit(101).setAction(0)
    stage.delay(3)
    stage.unit(105).setAction(0, 1)
    stage.say('&105\n(역시 전풍의 말이 옳았어.\n지금은 조조와 싸워서는 안 되는데…….\n하지만 이제 어쩔 도리가 없구나.)')
    stage.effectSound(202, 255)
    stage.clsUnit()
    return False


def scene8():
    stage.loadBg(2, 8)
    stage.showUnits([
        [
            0,
            34,
            55,
            1],
        [
            20,
            49,
            40,
            2],
        [
            22,
            55,
            40,
            2]])
    stage.draw()
    stage.effectSound(202, 0)
    stage.delay(3)
    stage.setStageName('허도  승상부')
    stage.showUnit(181, 90, 55, 3)
    stage.say('&181\n주군! 원소군이 접경의 백마에 집결하여\n곧 우리 국경을 공격하려 합니다!\n&0\n음, 드디어 왔군.\n즉시 막료들을 모아 의논하겠다.\n&181\n예.')
    stage.unit(181).hide()
    stage.showUnit(3, 90, 55, 3)
    stage.delay(10)
    stage.say('&3\n승상!')
    stage.unit(3).move(49, 55, 3)
    stage.say('&0\n오, 관우로군!')
    stage.unit(3).setAction(6)
    stage.say('&3\n원소가 침공한다는 말을 듣고\n즉시 달려왔소.\n부디 이 관우에게 선봉을 맡겨 주시오.')
    stage.unit(3).setAction(0)
    stage.delay(3)
    model.setFace(3)
    stage.unit(0).setAction(3)
    stage.say('&0\n하하하.')
    stage.unit(0).setAction(0)
    model.setFace(0)
    stage.say('&0\n그리 생각해 주니 기쁘네만\n이번에는 자네가 나설 필요가 없네.\n&0\n자네에게 어울리는 상대가 나타나면\n그때 부르도록 하지.\n&3\n그런 뜻이셨군…….')
    stage.unit(3).move(95, 55, 1)
    stage.unit(3).hide()
    stage.showUnit(1, 95, 55, 3)
    stage.delay(10)
    stage.unit(1).move(73, 55, 3)
    stage.showUnit(24, 85, 40, 3)
    stage.unitsMove([
        [
            stage.unit(1),
            67,
            55,
            3],
        [
            stage.unit(24),
            79,
            40,
            3]])
    stage.showUnit(21, 85, 40, 3)
    stage.unitsMove([
        [
            stage.unit(1),
            49,
            55,
            3],
        [
            stage.unit(24),
            61,
            40,
            2],
        [
            stage.unit(21),
            67,
            40,
            2]])
    model.setFace(128)
    stage.say('&1\n드디어 원소가 공격해 오는군!')
    stage.unit(24).setAction(6)
    stage.say('&20\n원소의 병력이 압도적으로 우세하니\n몹시 힘겨운 싸움이 될 것입니다.')
    stage.unit(24).setAction(0)
    stage.say('&0\n음.')
    stage.unit(0).setAction(17)
    stage.say('&0\n모두 출진을 준비하라.')
    stage.effectSound(202, 255)
    stage.bgSound(16)
    stage.delay(3)
    stage.clsUnit()
    stage.loadBg(1, 0)
    stage.draw()
    stage.showHead(101, 353, 11)
    stage.showHead(0, 325, 128)
    stage.delay(8)
    stage.head(101).move(353, 34)
    stage.delay(5)
    stage.bottomTxt('원소군은 황하 가의 백마까지 남하했다.', False, False, False)
    stage.delay(8)
    stage.head(0).move(353, 114)
    stage.delay(8)
    stage.bottomTxt('조조군은 출병하여 적을 맞아 싸웠다.', True, True, True)
    return False


def scene9():
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
            24,
            60,
            42,
            3]])
    stage.draw()
    stage.setStageName('백마  조조군 본진')
    stage.unit(0).setAction(17)
    stage.say('&0\n무슨 일이 있어도 원소군의 공세를 막아라!')
    stage.unit(0).setAction(0)
    stage.setMenuVisible(True)
    stage.setJoinBattle(1, 10)
    gvars[4076] = 0
    gvars[4057] = 16
    stage.setBattlePos([
        [
            17,
            18],
        [
            15,
            9],
        [
            17,
            9],
        [
            9,
            14],
        [
            9,
            13],
        [
            8,
            15],
        [
            16,
            16],
        [
            15,
            17],
        [
            17,
            17],
        [
            18,
            17]])
    return False


def scene10():
    if stage.battleTest():
        stage.setMenuVisible(False)
        stage.say('&0\n출발하라!')
        stage.bgSound(-1)
        return True
    if stage.unitClickTest(1):
        stage.say('&1\n맹덕, 관우는 왜 출전시키지 않는가?\n&0\n음?\n아……아직 그가 나설 필요는 없네.\n&1\n그렇다 해도…….')
    if stage.unitClickTest(24):
        stage.say('&24\n특정 인물만 집중해서 육성하고 계시지는 않습니까?\n앞으로의 전투를 위해\n각 부대의 육성에도 신경 써 주십시오.')
    if stage.unitClickTest(2):
        stage.say('&2\n안량과 문추는 오래전부터\n원소군의 두 맹장으로 이름이 높았습니다.\n&0\n음.')
    if stage.unitClickTest(15):
        stage.say('&15\n안량과 문추………\n아주 강하다지요?\n저도 한번 겨뤄 보고 싶습니다.')
    if stage.unitClickTest(22):
        stage.say('&22\n주군, 방금 보고가 들어왔습니다.\n원소의 선봉은 대장 안량입니다.\n&0\n오, 원소가 안량을 앞세웠다고?\n&0\n내가 어지간히 눈엣가시인가 보군.')
    if stage.unitClickTest(20):
        stage.say('&20\n백마의 전장은 강을 사이에 두고 둘로 나뉘어 있습니다.\n다리가 승패를 가를 것입니다.\n&0\n음, 다리는 몇이나 있는가?\n&20\n동쪽과 서쪽에 하나씩 있습니다.')
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
