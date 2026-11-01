# Source Generated with Decompyle++
# File: R_00.marshal (Python 3.9)


def scene0():
    gvars[4029] = 0
    gvars[4059] = 1
    gvars[4064] = 1
    gvars[4088] = 1
    gvars[4073] = 1
    gvars[4071] = 1
    stage.info('1         \n        [C3A☆《삼국지 조조전》 게임 안내☆]\n\n—————————————————————————\n\n                ☆게임 설명☆')
    gvars[4071] = 1
    stage.info('2[C3A【등급별 성장표】]')
    gvars[4071] = 1
    stage.info('3            X     S     A     B     C\n합격 조건   100    90    70    50     0  \n합격 성장     5     4     3     2     1\n\n완벽 조건               100    90   100\n완벽 성장                 4     3     3')
    gvars[4071] = 1
    stage.info('4[C3A【능력 증감 비율】]\n\n  [C28◇공격력   상승 10%   하락 20%]\n  [C3A◇방어력   상승 10%   하락 20%]\n  [C27◇정신력   상승 10%   하락 20%]\n  [C25◇순발력   상승 20%   하락 20%]\n  [C76◇사기   상승 20%   하락 20%]')
    gvars[4071] = 1
    stage.info('5[C3A【공훈 모드 안내】]\n[C3A☆][C28인물 초상화를 클릭]하면 공훈을 확인할 수 있습니다.\n[C3A☆]올리고 싶은 능력치를 미리 [C28선택]해 두면 해당 공훈을 얻을 때\n  능력치가 자동으로 1 증가합니다. ([C28승급에 필요한 공훈 충족 시])\n[C3A☆]공훈 모드의 능력치 상한은 [C28110]입니다.\n[C3A☆][C28무력 공훈]: 물리 공격 1회당 무력 공훈 1을 얻습니다.\n (명중 여부와 무관하며 반격도 포함됩니다.)\n[C3A☆][C28통솔력 공훈]: 물리 공격을 받을 때마다 통솔력 공훈 1을 얻습니다.\n (명중 여부와 무관합니다.)\n[C3A☆][C28지력 공훈]: 책략을 사용하거나 받을 때마다 지력 공훈 1을 얻습니다.\n (명중 여부와 무관하며 이로운 책략도 포함됩니다.)\n[C3A☆][C28민첩 공훈]: 연속 공격이나 물리 공격 방어 시 민첩 공훈 1을 얻습니다.\n[C3A☆][C28운 공훈]: 치명타나 책략 방어 시 운 공훈 1을 얻습니다.\n[C3A☆]적 하나를 퇴각시킬 때마다 다섯 공훈이 각각 1 증가합니다.\n[C3A☆]전투를 마치면 아군의 각 능력치에 공훈 보상이 주어집니다. 남은 턴이\n  많을수록 보상도 늘어납니다. ([C28미출전 및 퇴각한 인물 제외])\n[C3A☆]책략 [C28"정진"] 상태에서는 획득하는 공훈이 [C28두 배]가 됩니다.\n[C3A☆]열매는 해당 강화 효과만 부여하며 다섯 능력치를 올리지 않습니다.')
    gvars[4071] = 1
    stage.info('6[C3A【단축키 안내】]\n[C3A☆]일부 단축 기능은 [C28메뉴—설정]에서 직접 지정할 수 있습니다.\n[C3A☆]숫자 [C280-4]: 전장 속도 변경. [C3A0]은 기본 속도, [C3A1-4]는 가속이며 [C3A1]이 가장 빠릅니다.\n[C3A☆]숫자 [C285]: 진영별 색상으로 체력 막대를 표시합니다.\n[C3A☆]숫자 [C286]: 인물의 [C3ABUFF]와 [C28DEBUFF]를 표시합니다.\n[C3A☆]숫자 [C287]: 왼쪽 아래에 인물 능력과 장비를 표시합니다.\n[C3A☆]숫자 [C288]: 연속 공격·치명타 확률과 상성 관계를 표시합니다.\n[C3A☆]숫자 [C289]: 지형 적응과 이동 소모량을 표시합니다.\n[C3A☆]문자 [C28A]: 턴 시작 시 자동 저장합니다. 저장 번호는 881-900입니다.\n[C3A☆]문자 [C28B]: 능력 화면에 습득 가능한 모든 책략과 습득 레벨을 표시합니다.\n[C3A☆]문자 [C28C]: 공격하는 유닛의 윤곽선을 표시합니다.\n[C3A☆]문자 [C28D]: 자동 장비 시 강제 미출전 장수의 장비를 해제하지 않습니다.\n[C3A☆]문자 [C28E]: 아군을 클릭하면 조작 불가 유닛의 행동 순서를 표시합니다.\n[C3A☆]문자 [C28F]: 조작 불가 유닛을 클릭하면 다른 유닛에 대한 적개심 수치를 표시합니다.\n[C3A☆]문자 [C28G]: 아군을 클릭하면 해당 아군에 영향을 줄 수 있는 적을 빨갛게 표시합니다.\n[C3A☆]문자 [C28H]: 레벨 상승 정보를 표시합니다.\n[C3A☆]문자 [C28Z]: 초고속 진행. Z를 다시 누르면 해제됩니다.\n[C3A☆][C28Shift]: 이야기 대사를 최고 속도로 건너뜁니다. 숫자 0을 누르면 복구됩니다.\n[C3A☆][C28방향키]: S 전장에서 창 표시 크기를 변경합니다.\n[C3A☆][C28주 키보드의 + -키]: BGM 음량을 조절합니다.')
    gvars[2226] = 40
    gvars[2227] = 15
    gvars[2228] = 3
    gvars[2229] = 10
    gvars[2230] = 10
    gvars[2231] = 40
    gvars[2232] = 20
    gvars[2233] = 20
    gvars[2234] = 50
    gvars[2235] = 50
    gvars[2236] = 40
    gvars[2237] = 99
    gvars[2238] = 40
    gvars[2239] = 99
    gvars[2240] = 4
    gvars[2241] = 3
    gvars[2242] = 3
    gvars[2243] = 100
    gvars[2244] = 100
    gvars[2245] = 100
    gvars[2246] = 100
    gvars[2247] = 30
    gvars[2248] = 3
    gvars[2249] = 3
    gvars[2250] = 101
    gvars[2251] = 102
    gvars[2252] = 3
    gvars[2253] = 30
    gvars[2254] = 2
    gvars[2255] = 40
    gvars[2256] = 40
    gvars[2257] = 50
    gvars[2258] = 3
    gvars[2259] = 3
    gvars[2260] = 20
    gvars[2261] = 40
    gvars[2262] = 50
    gvars[2263] = 30
    gvars[2264] = 3
    gvars[2265] = 3
    gvars[2266] = 2
    gvars[2267] = 50
    gvars[2268] = 10
    gvars[2269] = 70
    gvars[2270] = 150
    gvars[2271] = 3
    gvars[2272] = 3
    gvars[2273] = 3
    gvars[2274] = 50
    gvars[2275] = 30
    gvars[2276] = 13
    gvars[2277] = 15
    gvars[2278] = 30
    gvars[2279] = 3
    gvars[2280] = 40
    gvars[2281] = 103
    gvars[2282] = 30
    gvars[2283] = 3
    gvars[2284] = 3
    gvars[2285] = 3
    gvars[2286] = 10
    gvars[2287] = 50
    gvars[2288] = 4
    gvars[2289] = 3
    gvars[2290] = 3
    gvars[2291] = 3
    gvars[2292] = 5
    gvars[2293] = 3
    gvars[2294] = 3
    gvars[2295] = 8
    gvars[2296] = 30
    gvars[2297] = 3
    gvars[2298] = 3
    gvars[2299] = 4
    gvars[2300] = 12
    gvars[2301] = 50
    gvars[2302] = 104
    gvars[2303] = 5
    gvars[2304] = 30
    gvars[2305] = 12
    gvars[2306] = 3
    gvars[2307] = 15
    gvars[2308] = 105
    gvars[2309] = 15
    gvars[2310] = 99
    gvars[2311] = 3
    gvars[2312] = 6
    gvars[2313] = 4
    gvars[2314] = 60
    gvars[2315] = 30
    gvars[2316] = 3
    gvars[2317] = 3
    gvars[2318] = 3
    gvars[2319] = 107
    gvars[2320] = 108
    gvars[2321] = 109
    gvars[2322] = 110
    gvars[2323] = 111
    gvars[2324] = 112
    gvars[2325] = 40
    gvars[2326] = 50
    gvars[2327] = 113
    gvars[2426] = 35
    gvars[2427] = 21
    gvars[2428] = 35
    gvars[2429] = 7
    gvars[2430] = 7
    gvars[2431] = 7
    gvars[2432] = 35
    gvars[2433] = 42
    gvars[2434] = 70
    gvars[2435] = 70
    gvars[2436] = 210
    gvars[2437] = 210
    gvars[2438] = 21
    gvars[2439] = 7
    gvars[2440] = 7
    gvars[2441] = 70
    gvars[2442] = 7
    gvars[2443] = 70
    gvars[2444] = 70
    gvars[2445] = 70
    gvars[2446] = 70
    gvars[2447] = 70
    gvars[2448] = 70
    gvars[2449] = 210
    gvars[2450] = 70
    gvars[2451] = 70
    gvars[2452] = 70
    gvars[2453] = 70
    gvars[2454] = 70
    gvars[2455] = 105
    gvars[2456] = 105
    gvars[2457] = 105
    gvars[2458] = 21
    gvars[2459] = 210
    gvars[2460] = 210
    gvars[2461] = 210
    gvars[2462] = 70
    gvars[2463] = 210
    gvars[2464] = 210
    gvars[2465] = 35
    gvars[2466] = 70
    gvars[2467] = 70
    gvars[2468] = 70
    gvars[2469] = 210
    gvars[2470] = 210
    gvars[2471] = 70
    gvars[2472] = 210
    gvars[2473] = 70
    gvars[2474] = 70
    gvars[2475] = 70
    gvars[2476] = 70
    gvars[2477] = 70
    gvars[2478] = 70
    gvars[2479] = 70
    gvars[2480] = 105
    gvars[2481] = 35
    gvars[2482] = 35
    gvars[2483] = 105
    gvars[2484] = 7
    gvars[2485] = 35
    gvars[2486] = 7
    gvars[2487] = 70
    gvars[2488] = 70
    gvars[2489] = 70
    gvars[2490] = 210
    gvars[2491] = 7
    gvars[2492] = 7
    gvars[2493] = 7
    gvars[2494] = 70
    gvars[2495] = 70
    gvars[2496] = 210
    gvars[2497] = 210
    gvars[2498] = 105
    gvars[2499] = 70
    gvars[2500] = 35
    gvars[2501] = 70
    gvars[2502] = 35
    gvars[2503] = 7
    gvars[2504] = 70
    gvars[2505] = 210
    gvars[2506] = 70
    gvars[2507] = 70
    gvars[2508] = 7
    gvars[2509] = 210
    gvars[2510] = 210
    gvars[2511] = 35
    gvars[2512] = 210
    gvars[2513] = 70
    gvars[2514] = 210
    gvars[2515] = 210
    gvars[2516] = 210
    gvars[2517] = 210
    gvars[2518] = 210
    gvars[2519] = 210
    gvars[2520] = 210
    gvars[2521] = 210
    gvars[2522] = 210
    gvars[2523] = 210
    gvars[2524] = 35
    gvars[2525] = 7
    gvars[2526] = 7
    gvars[2527] = 70
    return False


def scene1():
    model.initLocalVar()
    stage.clsUnit()
    stage.setMenuVisible(False)
    model.setAmbition(50)
    stage.loadBg(2, 30)
    stage.setEventName('두각을 드러내다')
    stage.draw()
    stage.effectSound(201, 0)
    stage.showUnit(181, 40, 5, 2)
    stage.unit(181).move(40, 15, 2)
    stage.showUnits([[0, 40, 5, 2], [157, 54, 95]])
    stage.unitsMove([[stage.unit(181), 40, 25, 2], [stage.unit(0), 40, 15, 2], [stage.unit(157), 54, 85, 0]])
    stage.showUnit(182, 40, 5, 2)
    stage.unitsMove([[stage.unit(181), 40, 45, 2], [stage.unit(0), 40, 35, 2], [stage.unit(182), 40, 25, 2], [stage.unit(157), 54, 65, 0]])
    stage.delay(3)
    stage.unit(181).setAction(0, 0)
    stage.say('&181\n주군, 서두르셔야 합니다!\n&0\n알고 있다!')
    stage.unitsMove([[stage.unit(181), 40, 60, 2], [stage.unit(0), 40, 50, 2], [stage.unit(182), 40, 40, 2], [stage.unit(157), 54, 50, 0]])
    stage.unit(0).setAction(0)
    stage.unit(181).setAction(0)
    stage.unit(182).setAction(0)
    stage.delay(3)
    stage.unit(157).setAction(0, 3)
    stage.say('&157\n잠시 걸음을 멈춰 주시겠습니까!')
    stage.unit(181).setAction(0, 1)
    stage.say('&181\n누구냐! 감히 관군의 앞길을 막으려는 것이냐!')
    stage.say('&0\n잠깐 기다려라.')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n어르신, 내게 무슨 볼일이 있으시오?\n나는 관군의 기병대장이오.\n성은 조, 이름은 조이며 자는 맹덕이라 하오.')
    stage.unit(157).setAction(6)
    stage.say('&157\n실은 나리의 상이 범상치 않기에\n저도 모르게 불러 세우고 말았습니다.')
    stage.unit(157).setAction(0)
    stage.say('&181\n이보시오, 감히 주군께 무례를 범하지 마시오!')
    stage.unit(0).setAction(0, 2)
    stage.say('&0\n그만, 나서지 마라!')
    stage.unit(0).setAction(0, 1)
    stage.say('&0\n어르신은 관상을 볼 줄 아시오?\n&157\n그렇습니다.\n&0\n그거 흥미롭구려!\n내 관상은 어떠하오?\n&157\n나리는 치세의 능신이십니다.\n허나 훗날 세상 사람들은 나리를 간웅이라 부를 것입니다.\n&181\n무, 무슨 망발이오!\n&0\n잠깐 기다려라.\n&0\n어르신, 천하에 큰 난리가 일어난다는 말씀이오?\n&157\n허허, 이 늙은이는 그런 말을 한 적이 없습니다.\n&0\n숨기실 것 없소.\n허나 간웅이라…….')
    stage.effectSound(201, 255)
    stage.delay(3)
    stage.bgSound(11)
    stage.delay(3)
    sel = stage.choice('바로 내가 바라던 바다\n아직 단정하기에는 이르다', 0)
    if sel == 1:
        stage.addAmbition(10)
        stage.bgSound(-1)
        vars[300] = 1
        stage.delay(3)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.unit(0).setAction(3)
        stage.say('&0\n사내로 태어난 이상,\n난세의 간웅이 되는 것이야말로 내 뜻이오.')
        stage.unit(0).setAction(0)
        stage.say('&157\n그러셨군요. 실은 이 늙은이가 그저 허튼소리를 한 것입니다.\n부디 허물치 말아 주십시오.\n&157\n그럼 이만 물러가겠습니다. 장군의 승전을 기원하지요.')
        stage.unit(157).setAction(0)
    elif sel == 2:
        vars[300] = 0
        stage.addAmbition(-10)
        stage.bgSound(-1)
        stage.delay(3)
        stage.effectSound(201, 0)
        stage.delay(3)
        stage.say('&0\n……간웅이라?\n지금 단정하기에는 너무 이르지 않소.')
        stage.unit(157).setAction(6)
        stage.say('&157\n그러셨군요. 실은 이 늙은이가 그저 허튼소리를 한 것입니다.\n부디 허물치 말아 주십시오.\n&157\n그럼 이만 물러가겠습니다. 장군의 승전을 기원하지요.')
        stage.unit(157).setAction(0)
    stage.say('&157\n떠나시기 전에 나리의 취향과 성향을\n몇 가지 여쭙고자 합니다.')
    label('lab327')
    sel = stage.choice('[C28고전 모드]\n[C97확장 모드]\n[C3A자유 선택 모드]\n모드 설명', 0)
    if sel == 1:
        vars[1001] = 1
        gvars[1] = 0
        gvars[2] = 100
        gvars[4054] = 1000
        stage.infoTransfer(0, '과실 육성 모드')
        gvars[4054] = 1001
        stage.infoTransfer(0, '쉬움')
        gvars[4054] = 1002
        stage.infoTransfer(0, '끄기 1')
        label('lab345')
        sel = stage.choice('육성 모드      【[C28*.1000]】\n난이도 설정      【[C28*.1001*/2%]】\n능력치 재계산      【[C28*.1002재계산]】\n[C3A게임 시작]', 0)
        if sel == 1:
            if gvars[1] == 0:
                gvars[1] = 1
                gvars[4039] = 1
                gvars[4047] = 1
                gvars[4074] = 1
                gvars[4075] = 1
                gvars[4054] = 1000
                stage.infoTransfer(0, '공훈 모드')
                goto('lab345')
            if gvars[1] == 1:
                gvars[1] = 0
                gvars[4039] = 0
                gvars[4047] = 0
                gvars[4074] = 0
                gvars[4075] = 0
                gvars[4054] = 1000
                stage.infoTransfer(0, '과실 육성 모드')
                goto('lab345')
        if sel == 2:
            if gvars[2] == 100:
                gvars[4076] = 1
                pvars[201] = 4369724
                stage.varOper(0, 201, 2, 0, 50)
                gvars[4076] = 0
                gvars[2] = 110
                gvars[4033] = 110
                gvars[4034] = 105
                gvars[4054] = 1001
                stage.infoTransfer(0, '보통')
                goto('lab345')
            if gvars[2] == 110:
                gvars[4076] = 1
                pvars[201] = 4369724
                stage.varOper(0, 201, 2, 0, 75)
                gvars[4076] = 0
                gvars[2] = 120
                gvars[4033] = 120
                gvars[4034] = 115
                gvars[4054] = 1001
                stage.infoTransfer(0, '어려움')
                goto('lab345')
            if gvars[2] == 120:
                gvars[4076] = 1
                pvars[201] = 4369724
                stage.varOper(0, 201, 2, 0, 100)
                gvars[4076] = 0
                gvars[2] = 130
                gvars[4033] = 130
                gvars[4034] = 125
                gvars[4054] = 1001
                stage.infoTransfer(0, '전문가')
                goto('lab345')
            if gvars[2] == 130:
                gvars[4076] = 1
                pvars[201] = 4369724
                stage.varOper(0, 201, 2, 0, 25)
                gvars[4076] = 0
                gvars[2] = 100
                gvars[4033] = 100
                gvars[4034] = 100
                gvars[4054] = 1001
                stage.infoTransfer(0, '쉬움')
                goto('lab345')
        if sel == 3:
            if gvars[4094] == 0:
                gvars[4094] = 1
                gvars[4054] = 1002
                stage.infoTransfer(0, '켜기 1')
                goto('lab345')
            gvars[4094] = 0
            gvars[4054] = 1002
            stage.infoTransfer(0, '끄기 1')
            goto('lab345')
        if sel == 4:
            pass
    elif sel == 2:
        vars[1002] = 1
        vars[1011] = 1
        vars[1013] = 1
        vars[1021] = 1
        vars[1031] = 1
        vars[1041] = 1
        gvars[4035] = 1
        gvars[4063] = 1
        gvars[4065] = 1
        gvars[4066] = 1
        gvars[4081] = 1
        gvars[4088] = 1
        gvars[4094] = 1
        gvars[4076] = 1
        pvars[200] = 4293888
        stage.varOper(0, 200, 2, 0, 3)
        gvars[4076] = 0
        gvars[1] = 0
        gvars[2] = 100
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[6] = 0
        gvars[7] = 0
        gvars[4054] = 1000
        stage.infoTransfer(0, '과실 육성 모드')
        gvars[4054] = 1001
        stage.infoTransfer(0, '쉬움')
        gvars[4054] = 1005
        stage.infoTransfer(0, '기본 무작위')
        gvars[4054] = 1006
        stage.infoTransfer(0, '끄기 1')
        label('lab509')
        sel = stage.choice('육성 모드      【[C28*.1000]】\n난이도 설정      【[C28*.1001*/2%]】\n명중 방식      【[C28*.1005]】\n적 격파 보너스      【[C28*.1006 보너스]】\n[C25설정 설명]\n[C3A게임 시작]', 0)
        if sel == 1:
            if gvars[6] == 0:
                gvars[6] = 1
                gvars[4039] = 0
                gvars[4047] = 0
                gvars[4074] = 0
                gvars[4075] = 1
                gvars[4054] = 1000
                stage.infoTransfer(0, '슈퍼 과실 육성')
                goto('lab509')
            if gvars[6] == 1:
                gvars[6] = 2
                gvars[4039] = 1
                gvars[4047] = 1
                gvars[4074] = 1
                gvars[4075] = 1
                gvars[4054] = 1000
                stage.infoTransfer(0, '공훈 모드')
                goto('lab509')
            if gvars[6] == 2:
                gvars[6] = 0
                gvars[4039] = 0
                gvars[4047] = 0
                gvars[4074] = 0
                gvars[4075] = 0
                gvars[4054] = 1000
                stage.infoTransfer(0, '과실 육성 모드')
                goto('lab509')
        if sel == 2:
            if gvars[2] == 100:
                gvars[4076] = 1
                pvars[201] = 4369724
                stage.varOper(0, 201, 2, 0, 50)
                gvars[4076] = 0
                gvars[2] = 110
                gvars[4033] = 110
                gvars[4034] = 105
                gvars[4054] = 1001
                stage.infoTransfer(0, '보통')
                goto('lab509')
            if gvars[2] == 110:
                gvars[4076] = 1
                pvars[201] = 4369724
                stage.varOper(0, 201, 2, 0, 75)
                gvars[4076] = 0
                gvars[2] = 120
                gvars[4033] = 120
                gvars[4034] = 115
                gvars[4054] = 1001
                stage.infoTransfer(0, '어려움')
                goto('lab509')
            if gvars[2] == 120:
                gvars[4076] = 1
                pvars[201] = 4369724
                stage.varOper(0, 201, 2, 0, 100)
                gvars[4076] = 0
                gvars[2] = 130
                gvars[4033] = 130
                gvars[4034] = 125
                gvars[4054] = 1001
                stage.infoTransfer(0, '전문가')
                goto('lab509')
            if gvars[2] == 130:
                gvars[4076] = 1
                pvars[201] = 4369724
                stage.varOper(0, 201, 2, 0, 25)
                gvars[4076] = 0
                gvars[2] = 100
                gvars[4033] = 100
                gvars[4034] = 100
                gvars[4054] = 1001
                stage.infoTransfer(0, '쉬움')
                goto('lab509')
        if sel == 3:
            if gvars[1] == 0:
                gvars[1] = 1
                gvars[4077] = 0
                gvars[4017] = 30
                gvars[4018] = 85
                gvars[4054] = 1005
                stage.infoTransfer(0, '결과 보장 모드')
                goto('lab509')
            if gvars[1] == 1:
                gvars[1] = 2
                gvars[4077] = 2
                gvars[4017] = 55
                gvars[4018] = 1
                gvars[4054] = 1005
                stage.infoTransfer(0, '기 모으기 모드')
                goto('lab509')
            if gvars[1] == 2:
                gvars[1] = 0
                gvars[4077] = 2
                gvars[4017] = 0
                gvars[4018] = 0
                gvars[4054] = 1005
                stage.infoTransfer(0, '기본 무작위')
                goto('lab509')
        if sel == 4:
            if gvars[4046] == 0:
                gvars[4046] = 1
                gvars[4054] = 1006
                stage.infoTransfer(0, '켜기 1')
                goto('lab509')
            gvars[4046] = 0
            gvars[4054] = 1006
            stage.infoTransfer(0, '끄기 1')
            goto('lab509')
        if sel == 5:
            label('lab665')
            sel = stage.choice('모드 및 난이도\n명중 방식\n처치 보너스\n[C3A돌아가기]', 157)
            if sel == 1:
                stage.say('&157\n[C97열매 육성 모드]는 고전 모드입니다.\n[C26슈퍼 열매 육성]을 선택하면 [C28적군 성장]이 활성화됩니다.\n[C26슈퍼 열매 육성]에서는 장비 판매 시 [C28열매를 3배] 얻습니다.\n&157\n[C3A공훈 모드]를 선택하면 [C28적군 성장]이 활성화됩니다.\n[C3A공훈 모드]에서는 [C28장비 자동 레벨업]도 활성화됩니다.\n&157\n세 모드 모두 능력치 상한은 [C28110]입니다.\n[C28능력치 재계산]과 [C28100 초과 보너스]도 모두 활성화됩니다.\n&157\n난이도는 네 단계로 나뉘며,\n난이도에 따라 [C28적군 성장 계수]가 달라집니다.\n[C3A백분율]은 난이도별 [C28적군 능력치 보너스]를 뜻합니다.')
                goto('lab665')
            if sel == 2:
                stage.say('&157\n[C97고전 확률 방식:] 저장과 불러오기를 반복하여\n명중과 방어 결과를 바꿀 수 있습니다.\n&157\n[C97보정 방식:] 저장과 불러오기만으로는\n명중과 방어 결과를 [C28바꿀 수 없습니다].\n[C28부대의 행동 순서]를 바꾸면 결과에 영향을 줄 수 있습니다.\n&157\n명중률이 [C2885%를 넘으면 반드시 명중]하며,\n[C3A30% 미만이면 반드시 방어당합니다].\n&157\n[C97확률 누적 방식:] 확률에 따른 기댓값을 누적합니다.\n효과가 발동하지 않으면 기댓값이 계속 쌓이고,\n누적치가 100%에 이르면 반드시 발동합니다.\n&157\n쉽게 말해 치명타나 연속 공격 확률이 [C2850%]라면\n[C282]회 공격할 때마다 반드시 한 번 발동합니다.\n이 방식에는 무작위 요소가 없습니다.')
                goto('lab665')
            if sel == 3:
                stage.say('&157\n쓰러뜨린 적의 수가 기록되며\n무장 능력 화면에서 확인할 수 있습니다.\n&157\n적 [C2830]명 처치마다 [C97모든 능력치가 1%] 증가합니다.\n적 [C2815]명 처치마다 [C97최대 HP와 MP가 1%] 증가합니다.\n처치 수가 [C28100]명에 도달하면 [C97이동력이 영구적으로 +1] 증가합니다. ([C3A한 번만 적용])')
                goto('lab665')
            if sel == 4:
                goto('lab509')
        if sel == 6:
            pass
    elif sel == 3:
        stage.say('&157\n[C28무작위 요소]를 너무 많이 켜면 게임의 재미를 해칠 수 있습니다.')
        vars[1002] = 1
        gvars[4035] = 1
        gvars[4063] = 1
        gvars[4065] = 1
        gvars[4066] = 1
        gvars[4081] = 1
        gvars[4088] = 1
        gvars[4094] = 1
        gvars[4076] = 1
        pvars[200] = 4293888
        stage.varOper(0, 200, 2, 0, 3)
        gvars[4076] = 0
        gvars[1] = 0
        gvars[2] = 100
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[6] = 0
        gvars[7] = 0
        gvars[4054] = 1000
        stage.infoTransfer(0, '과실 육성 모드')
        gvars[4054] = 1001
        stage.infoTransfer(0, '쉬움')
        gvars[4054] = 1002
        stage.infoTransfer(0, '고전 병종')
        gvars[4054] = 1003
        stage.infoTransfer(0, '고전 보물')
        gvars[4054] = 1004
        stage.infoTransfer(0, '인물 숨기기')
        gvars[4054] = 1005
        stage.infoTransfer(0, '기본 무작위')
        gvars[4054] = 1006
        stage.infoTransfer(0, '끄기 1')
        gvars[4054] = 1007
        stage.infoTransfer(0, '병종을 끈다')
        gvars[4054] = 1008
        stage.infoTransfer(0, '  1')
        label('lab744')
        sel = stage.choice('육성 모드      【[C28*.1000]】\n병종 설정      【[C28*.1002]】[C97*.1008]\n보물 설정      【[C28*.1003]】\n인물 특기      【[C28*.1004특기]】\n병종 특기      【[C28*.1007특기]】\n[C3A다음 페이지]', 0)
        if sel == 1:
            if gvars[6] == 0:
                gvars[6] = 1
                gvars[4039] = 0
                gvars[4047] = 0
                gvars[4074] = 0
                gvars[4075] = 1
                gvars[4054] = 1000
                stage.infoTransfer(0, '슈퍼 과실 육성')
                goto('lab744')
            if gvars[6] == 1:
                gvars[6] = 2
                gvars[4039] = 1
                gvars[4047] = 1
                gvars[4074] = 1
                gvars[4075] = 1
                gvars[4054] = 1000
                stage.infoTransfer(0, '공훈 모드')
                goto('lab744')
            if gvars[6] == 2:
                gvars[6] = 0
                gvars[4039] = 0
                gvars[4047] = 0
                gvars[4074] = 0
                gvars[4075] = 0
                gvars[4054] = 1000
                stage.infoTransfer(0, '과실 육성 모드')
                goto('lab744')
        if sel == 2:
            if gvars[3] == 0:
                gvars[3] = 1
                vars[1011] = 1
                vars[1012] = 0
                vars[1013] = 1
                gvars[4054] = 1002
                stage.infoTransfer(0, '확장 병종 2')
                goto('lab744')
            if gvars[3] == 1:
                gvars[3] = 2
                vars[1011] = 0
                vars[1012] = 1
                vars[1013] = 1
                gvars[4054] = 1002
                stage.infoTransfer(0, '무작위 병종 2')
                gvars[4054] = 1008
                stage.infoTransfer(0, '신중히 선택하세요!')
                goto('lab744')
            if gvars[3] == 2:
                gvars[3] = 0
                vars[1011] = 0
                vars[1012] = 0
                vars[1013] = 0
                gvars[4054] = 1002
                stage.infoTransfer(0, '고전 병종')
                gvars[4054] = 1008
                stage.infoTransfer(0, '  1')
                goto('lab744')
        if sel == 3:
            if gvars[4] == 0:
                gvars[4] = 1
                vars[1021] = 1
                vars[1022] = 0
                gvars[4054] = 1003
                stage.infoTransfer(0, '확장 보물')
                goto('lab744')
            if gvars[4] == 1:
                gvars[4] = 2
                vars[1021] = 0
                vars[1022] = 1
                gvars[4054] = 1003
                stage.infoTransfer(0, '무작위 효과')
                goto('lab744')
            if gvars[4] == 2:
                gvars[4] = 0
                vars[1021] = 0
                vars[1022] = 0
                gvars[4054] = 1003
                stage.infoTransfer(0, '고전 보물')
                goto('lab744')
        if sel == 4:
            if gvars[5] == 0:
                gvars[5] = 1
                vars[1031] = 1
                vars[1032] = 0
                gvars[4054] = 1004
                stage.infoTransfer(0, '확장 인물')
                goto('lab744')
            if gvars[5] == 1:
                gvars[5] = 2
                vars[1031] = 0
                vars[1032] = 1
                gvars[4054] = 1004
                stage.infoTransfer(0, '인물 무작위')
                goto('lab744')
            if gvars[5] == 2:
                gvars[5] = 0
                vars[1031] = 0
                vars[1032] = 0
                gvars[4054] = 1004
                stage.infoTransfer(0, '인물 숨기기')
                goto('lab744')
        if sel == 5:
            if gvars[7] == 0:
                gvars[7] = 1
                vars[1041] = 1
                vars[1042] = 0
                gvars[4054] = 1007
                stage.infoTransfer(0, '확장 병종 1')
                goto('lab744')
            if gvars[7] == 1:
                gvars[7] = 2
                vars[1041] = 0
                vars[1042] = 1
                gvars[4054] = 1007
                stage.infoTransfer(0, '무작위 병종 1')
                goto('lab744')
            if gvars[7] == 2:
                gvars[7] = 0
                vars[1041] = 0
                vars[1042] = 0
                gvars[4054] = 1007
                stage.infoTransfer(0, '병종을 끈다')
                goto('lab744')
        if sel == 6:
            label('lab911')
            sel = stage.choice('난이도 설정      【[C28*.1001*/2%]】\n명중 방식      【[C28*.1005]】\n처치 보너스      【[C28*.1006보너스]】\n[C25설정 안내]\n[C3A이전 페이지]\n[C97설정 완료]', 0)
            if sel == 1:
                if gvars[2] == 100:
                    gvars[4076] = 1
                    pvars[201] = 4369724
                    stage.varOper(0, 201, 2, 0, 50)
                    gvars[4076] = 0
                    gvars[2] = 110
                    gvars[4033] = 110
                    gvars[4034] = 105
                    gvars[4054] = 1001
                    stage.infoTransfer(0, '보통')
                    goto('lab911')
                if gvars[2] == 110:
                    gvars[4076] = 1
                    pvars[201] = 4369724
                    stage.varOper(0, 201, 2, 0, 75)
                    gvars[4076] = 0
                    gvars[2] = 120
                    gvars[4033] = 120
                    gvars[4034] = 115
                    gvars[4054] = 1001
                    stage.infoTransfer(0, '어려움')
                    goto('lab911')
                if gvars[2] == 120:
                    gvars[4076] = 1
                    pvars[201] = 4369724
                    stage.varOper(0, 201, 2, 0, 100)
                    gvars[4076] = 0
                    gvars[2] = 130
                    gvars[4033] = 130
                    gvars[4034] = 125
                    gvars[4054] = 1001
                    stage.infoTransfer(0, '전문가')
                    goto('lab911')
                if gvars[2] == 130:
                    gvars[4076] = 1
                    pvars[201] = 4369724
                    stage.varOper(0, 201, 2, 0, 25)
                    gvars[4076] = 0
                    gvars[2] = 100
                    gvars[4033] = 100
                    gvars[4034] = 100
                    gvars[4054] = 1001
                    stage.infoTransfer(0, '쉬움')
                    goto('lab911')
            if sel == 2:
                if gvars[1] == 0:
                    gvars[1] = 1
                    gvars[4077] = 0
                    gvars[4017] = 30
                    gvars[4018] = 85
                    gvars[4054] = 1005
                    stage.infoTransfer(0, '결과 보장 모드')
                    goto('lab911')
                if gvars[1] == 1:
                    gvars[1] = 2
                    gvars[4077] = 2
                    gvars[4017] = 55
                    gvars[4018] = 1
                    gvars[4054] = 1005
                    stage.infoTransfer(0, '기 모으기 모드')
                    goto('lab911')
                if gvars[1] == 2:
                    gvars[1] = 0
                    gvars[4077] = 2
                    gvars[4017] = 0
                    gvars[4018] = 0
                    gvars[4054] = 1005
                    stage.infoTransfer(0, '기본 무작위')
                    goto('lab911')
            if sel == 3:
                if gvars[4046] == 0:
                    gvars[4046] = 1
                    gvars[4054] = 1006
                    stage.infoTransfer(0, '켜기 1')
                    goto('lab911')
                gvars[4046] = 0
                gvars[4054] = 1006
                stage.infoTransfer(0, '끄기 1')
                goto('lab911')
            if sel == 4:
                label('lab1031')
                sel = stage.choice('모드와 난이도\n병종과 보물\n특기 설명\n명중 방식\n적 격파 보너스\n[C3A설정으로 돌아가기]', 157)
                if sel == 1:
                    stage.say('&157\n[C97열매 육성 모드]는 고전 모드입니다.\n[C26슈퍼 열매 육성]을 선택하면 [C28적군 성장]이 활성화됩니다.\n[C26슈퍼 열매 육성]에서는 장비 판매 시 [C28열매를 3배] 얻습니다.\n&157\n[C3A공훈 모드]를 선택하면 [C28적군 성장]이 활성화됩니다.\n[C3A공훈 모드]에서는 [C28장비 자동 레벨업]도 활성화됩니다.\n&157\n세 모드 모두 능력치 상한은 [C28110]입니다.\n[C28능력치 재계산]과 [C28100 초과 보너스]도 모두 활성화됩니다.\n&157\n난이도는 네 단계로 나뉘며,\n난이도에 따라 [C28적군 성장 계수]가 달라집니다.\n[C3A백분율]은 난이도별 [C28적군 능력치 보너스]를 뜻합니다.')
                    goto('lab1031')
                if sel == 2:
                    stage.say('&157\n[C97기본 병종:] 아군과 적군의 병종 능력 등급이 그대로 유지됩니다.\n[C97확장 병종:] 기존 병종에 더해 아군과 적군에 [C28새 병종]이 등장합니다.\n[C97무작위 병종:] 아군과 적장의 병종이 [C28무작위로 결정됩니다].\n&157\n[C97기본 보물:] 보물의 효과가 그대로 유지됩니다.\n[C97확장 보물:] 보물에 [C28새로운 효과]가 적용됩니다.\n[C97무작위 효과:] 보물의 효과가 [C28무작위로 생성됩니다].')
                    goto('lab1031')
                if sel == 3:
                    stage.say('&157\n[C97확장 인물 특기:] 인물마다 [C28미리 정해진] 특기를 사용합니다.\n[C97인물 무작위:] 아군과 적장이 [C28무작위로] 특기를 얻습니다.\n&157\n[C97확장 병종 특기:] 병종마다 [C28미리 정해진] 특기를 사용합니다.\n[C97병종 무작위:] 각 병종이 [C28무작위로] 특기를 얻습니다.')
                    goto('lab1031')
                if sel == 4:
                    stage.say('&157\n[C97고전 확률 방식:] 저장과 불러오기를 반복하여\n명중과 방어 결과를 바꿀 수 있습니다.\n&157\n[C97보정 방식:] 저장과 불러오기만으로는\n명중과 방어 결과를 [C28바꿀 수 없습니다].\n[C28부대의 행동 순서]를 바꾸면 결과에 영향을 줄 수 있습니다.\n&157\n명중률이 [C2885%를 넘으면 반드시 명중]하며,\n[C3A30% 미만이면 반드시 방어당합니다].\n&157\n[C97확률 누적 방식:] 확률에 따른 기댓값을 누적합니다.\n효과가 발동하지 않으면 기댓값이 계속 쌓이고,\n누적치가 100%에 이르면 반드시 발동합니다.\n&157\n쉽게 말해 치명타나 연속 공격 확률이 [C2850%]라면\n[C282]회 공격할 때마다 반드시 한 번 발동합니다.\n이 방식에는 무작위 요소가 없습니다.')
                    goto('lab1031')
                if sel == 5:
                    stage.say('&157\n쓰러뜨린 적의 수가 기록되며\n무장 능력 화면에서 확인할 수 있습니다.\n&157\n적 [C2830]명 처치마다 [C97모든 능력치가 1%] 증가합니다.\n적 [C2815]명 처치마다 [C97최대 HP와 MP가 1%] 증가합니다.\n처치 수가 [C28100]명에 도달하면 [C97이동력이 영구적으로 +1] 증가합니다. ([C3A한 번만 적용])')
                    goto('lab1031')
                if sel == 6:
                    goto('lab911')
            if sel == 5:
                goto('lab744')
            if sel == 6:
                gvars[10] = 0
                if vars[1012]:
                    gvars[10] += 1
                if vars[1022]:
                    gvars[10] += 1
                if vars[1032]:
                    gvars[10] += 1
                if vars[1042]:
                    gvars[10] += 1
                if gvars[10] >= 2:
                    stage.say('&157\n[C28무작위 항목]을 너무 많이 켜면 게임의 재미가 떨어질 수 있습니다.\n현재 설정으로 계속하시겠습니까?')
                    if stage.ask() == 0:
                        goto('lab744')
    elif sel == 4:
        stage.say('&157\n[C28고전 모드]: 원작을 최대한 충실하게 재현합니다.\n&157\n[C3A확장 모드]: 인물의 병종과 보물 효과가 완전히 바뀝니다.\n인물 특기와 병종 특기가 추가되며, 레벨 상승 시 능력치가 자동 재계산됩니다.\n능력 성장률은 [C285]단계가 되는 등 여러 요소가 달라집니다.\n&157\n[C3A자유 선택 모드]: 게임 내용을 자유롭게 설정할 수 있습니다.\r[C97새 병종], [C97새 보물] 등을 추가하고\n다양한 [C97무작위 항목]도 자유롭게 선택할 수 있습니다.\n&157\n보물 설명의 [C28확장 효과]는 [C97확장 모드]에서만 적용됩니다.\n다른 모드의 효과는 표시된 [C28특수 효과명]을 기준으로 합니다.\n&157\n[C28고전 모드]와 [C3A확장 모드]는\n모든 면에서 크게 다르므로\n서로 완전히 다른 게임이라 할 수 있습니다!')
        goto('lab327')
    if vars[1042]:
        gvars[4076] = 1
        pvars[0] = 5273606
        gvars[0] = 0
        label('lab1114')
        if gvars[0] <= 250:
            stage.varOper(0, 0, 2, 0, 255)
            pvars[0] += 8
            gvars[0] += 1
            goto('lab1114')
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[0] = 5274367
        stage.varOper(0, 0, 2, 0, 1)
        gvars[4076] = 0
        stage.infoTransfer(5, '13\n3\n63\n조련사-곰-마비 공격')
        stage.infoTransfer(5, '15\n3\n71\n토우-혼란 공격')
        stage.infoTransfer(5, '16\n3\n70\n목인-중독 공격')
        stage.infoTransfer(5, '100\n3\n36\n무희-주변 이상 상태 회복')
        stage.infoTransfer(5, '135\n3\n66\n주술사-정신력 보조·전체 능력')
        stage.infoTransfer(5, '88\n3\n67\n선인-특수 효과 모방')
        stage.infoTransfer(5, '57\n3\n72\n황제-유아독존')
        stage.infoTransfer(5, '41\n3\n78\n숙위기병-책략 면역')
        stage.infoTransfer(5, '89\n3\n79\n마왕-특수 효과 무효화')
        gvars[0] = 0
        pvars[1] = 4217744
        gvars[3] = 0
        gvars[4] = 0
        label('lab1148')
        if gvars[0] <= 79:
            gvars[4076] = 0
            gvars[1] = 0
            gvars[2] = 5273606
            gvars[4] = 0
            stage.infoTransfer(26, '157')
            if gvars[4025] == 21:
                goto('lab1148')
            if gvars[4025] == 22:
                goto('lab1148')
            if gvars[4025] == 36:
                goto('lab1148')
            if gvars[4025] == 49:
                goto('lab1148')
            if gvars[4025] == 67:
                goto('lab1148')
            if gvars[4025] >= 3 and gvars[4025] <= 5:
                goto('lab1148')
            if gvars[4025] >= 56 and gvars[4025] <= 57:
                goto('lab1148')
            if gvars[4025] >= 69 and gvars[4025] <= 70:
                goto('lab1148')
            if gvars[4025] >= 83 and gvars[4025] <= 84:
                goto('lab1148')
            if gvars[4025] >= 88 and gvars[4025] <= 89:
                goto('lab1148')
            if gvars[4025] >= 103 and gvars[4025] <= 111:
                goto('lab1148')
            if gvars[4025] >= 119 and gvars[4025] <= 127:
                goto('lab1148')
            if gvars[4025] >= 128 and gvars[4025] <= 156 and (model.random() < 70):
                goto('lab1148')
            gvars[3] = gvars[0]
            if gvars[3] >= 24 and gvars[3] <= 35:
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
            if gvars[3] == 72:
                gvars[4] = 2
            if gvars[3] == 79:
                gvars[4] = 2
            if gvars[4] == 0:
                if gvars[4025] >= 27 and gvars[4025] <= 28:
                    goto('lab1148')
                if gvars[4025] >= 39 and gvars[4025] <= 40:
                    goto('lab1148')
                if gvars[4025] >= 42 and gvars[4025] <= 43:
                    goto('lab1148')
                if gvars[4025] >= 45 and gvars[4025] <= 46:
                    goto('lab1148')
                if gvars[4025] >= 73 and gvars[4025] <= 80:
                    goto('lab1148')
                if gvars[4025] >= 92 and gvars[4025] <= 95:
                    goto('lab1148')
                if gvars[4025] >= 153 and gvars[4025] <= 156:
                    goto('lab1148')
                if gvars[4025] == 1:
                    goto('lab1148')
                if gvars[4025] == 58:
                    goto('lab1148')
                if gvars[4025] == 60:
                    goto('lab1148')
                if gvars[4025] == 70:
                    goto('lab1148')
                if gvars[4025] == 86:
                    goto('lab1148')
                if gvars[4025] == 102:
                    goto('lab1148')
                if gvars[4025] == 116:
                    goto('lab1148')
                if gvars[4025] == 130:
                    goto('lab1148')
                if gvars[4025] == 141:
                    goto('lab1148')
                if gvars[4025] == 147:
                    goto('lab1148')
            if gvars[4] == 1:
                if gvars[4025] >= 6 and gvars[4025] <= 17:
                    goto('lab1148')
                if gvars[4025] >= 20 and gvars[4025] <= 26:
                    goto('lab1148')
                if gvars[4025] >= 30 and gvars[4025] <= 31:
                    goto('lab1148')
                if gvars[4025] >= 37 and gvars[4025] <= 38:
                    goto('lab1148')
                if gvars[4025] >= 52 and gvars[4025] <= 53:
                    goto('lab1148')
                if gvars[4025] >= 62 and gvars[4025] <= 63:
                    goto('lab1148')
                if gvars[4025] >= 66 and gvars[4025] <= 68:
                    goto('lab1148')
                if gvars[4025] >= 96 and gvars[4025] <= 98:
                    goto('lab1148')
                if gvars[4025] >= 103 and gvars[4025] <= 104:
                    goto('lab1148')
                if gvars[4025] >= 111 and gvars[4025] <= 114:
                    goto('lab1148')
                if gvars[4025] == 29:
                    goto('lab1148')
                if gvars[4025] == 41:
                    goto('lab1148')
                if gvars[4025] == 44:
                    goto('lab1148')
                if gvars[4025] == 50:
                    goto('lab1148')
                if gvars[4025] == 55:
                    goto('lab1148')
                if gvars[4025] == 59:
                    goto('lab1148')
                if gvars[4025] == 72:
                    goto('lab1148')
                if gvars[4025] == 91:
                    goto('lab1148')
                if gvars[4025] == 99:
                    goto('lab1148')
                if gvars[4025] == 117:
                    goto('lab1148')
                if gvars[4025] == 128:
                    goto('lab1148')
                if gvars[4025] == 140:
                    goto('lab1148')
                if gvars[4025] == 145:
                    goto('lab1148')
                if gvars[4025] == 151:
                    goto('lab1148')
            gvars[1] = gvars[4025]
            gvars[1] *= 8
            gvars[1] += gvars[2]
            gvars[4076] = 1
            pvars[0] = gvars[1]
            stage.varOper(0, 1, 2, 4, 0)
            if stage.varTest(1, 0, 1, 0, 80):
                stage.varOper(0, 0, 2, 1, 1)
                if gvars[0] <= 57:
                    gvars[0] += 3
                else:
                    gvars[0] += 1
                goto('lab1148')
            goto('lab1148')
    if vars[1012]:
        gvars[0] = 0
        pvars[1] = 4217744
        label('lab1542')
        if gvars[0] <= 169:
            gvars[4076] = 0
            gvars[1] = gvars[0]
            gvars[2] = 5259072
            gvars[1] *= 4
            gvars[1] += gvars[2]
            gvars[4] = 0
            stage.infoTransfer(26, '80')
            if gvars[4025] == 66 and model.random() < 60:
                goto('lab1542')
            if gvars[4025] == 67 and model.random() < 80:
                goto('lab1542')
            if gvars[4025] == 72 and model.random() < 80:
                goto('lab1542')
            if gvars[4025] == 73:
                goto('lab1542')
            if gvars[4025] == 78 and model.random() < 60:
                goto('lab1542')
            if gvars[4025] == 79 and model.random() < 80:
                goto('lab1542')
            gvars[5] = gvars[4025]
            if gvars[5] <= 59:
                gvars[5] %= 3
                gvars[4025] -= gvars[5]
            if gvars[0] >= 26:
                gvars[3] = gvars[4025]
                if gvars[3] >= 24 and gvars[3] <= 35:
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
                if gvars[3] == 72:
                    gvars[4] = 2
                if gvars[3] == 79:
                    gvars[4] = 2
                gvars[6] = stage.unitAttr(gvars[0], 9)
                gvars[7] = stage.unitAttr(gvars[0], 10)
                gvars[8] = stage.unitAttr(gvars[0], 11)
                if gvars[4] == 2:
                    pass
                if gvars[6] >= 40 and gvars[8] <= 34 and (gvars[4] != 0):
                    goto('lab1542')
                if gvars[6] <= 34 and gvars[8] >= 40 and (gvars[4] != 1):
                    goto('lab1542')
            if gvars[4025] == 69:
                stage.infoTransfer(4, '20\n8\n*0\n유혹')
                stage.infoTransfer(4, '46\n5\n*0\n각성')
                stage.infoTransfer(4, '47\n25\n*0\n대각성')
                stage.infoTransfer(4, '39\n1\n*0\n소보급')
                stage.infoTransfer(4, '40\n12\n*0\n대보급')
                stage.infoTransfer(4, '41\n10\n*0\n원대')
                stage.infoTransfer(4, '42\n20\n*0\n원군')
                stage.infoTransfer(4, '43\n25\n*0\n수송')
                stage.infoTransfer(4, '86\n25\n*0\n소환2')
                stage.infoTransfer(4, '44\n15\n*0\n건언')
                stage.infoTransfer(4, '45\n22\n*0\n헌책')
                stage.infoTransfer(4, '57\n30\n*0\n회귀')
                stage.infoTransfer(4, '71\n18\n*0\n정묘')
                stage.infoTransfer(4, '74\n28\n*0\n방화')
            gvars[4076] = 1
            stage.varOper(0, 1, 2, 4, 4025)
            pvars[0] = gvars[1]
            stage.varOper(0, 0, 2, 1, 1)
            gvars[0] += 1
            goto('lab1542')
        gvars[2035] = 66
        gvars[2078] = 65
        gvars[2119] = 78
        gvars[0] = 0
        label('lab1717')
        if gvars[0] <= 25:
            model.unitJoin(gvars[0], 0, 2)
            gvars[4076] = 0
            gvars[1] = gvars[0]
            gvars[2] = 5259072
            gvars[1] *= 4
            gvars[1] += gvars[2]
            gvars[4076] = 1
            pvars[0] = gvars[1]
            stage.varOper(2, 3, 2, 1, 0)
            stage.setUnitAttr(gvars[0], 17, gvars[3])
            gvars[4] = 0
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
            if gvars[3] == 72:
                gvars[4] = 2
            if gvars[3] == 79:
                gvars[4] = 2
            gvars[6] = stage.unitAttr(gvars[0], 9)
            gvars[7] = stage.unitAttr(gvars[0], 10)
            gvars[8] = stage.unitAttr(gvars[0], 11)
            if gvars[4] == 0 and gvars[6] <= 34:
                stage.setUnitAbility(gvars[0], 10, 0, 35)
            if gvars[4] == 1 and gvars[8] <= 34:
                stage.setUnitAbility(gvars[0], 12, 0, 35)
            if gvars[4] == 2:
                if gvars[6] <= 34:
                    stage.setUnitAbility(gvars[0], 10, 0, 35)
                if gvars[7] <= 34:
                    stage.setUnitAbility(gvars[0], 11, 0, 35)
                if gvars[8] <= 34:
                    stage.setUnitAbility(gvars[0], 12, 0, 35)
            gvars[0] += 1
            goto('lab1717')
        gvars[0] = 1
        label('lab1816')
        if gvars[0] <= 25:
            model.unitJoin(gvars[0], 255, -1)
            gvars[0] += 1
            goto('lab1816')
        gvars[0] = 1
        label('lab1824')
        if gvars[0] <= 25:
            model.unitJoin(gvars[0], 0, 0)
            gvars[0] += 1
            goto('lab1824')
        gvars[0] = 1
        label('lab1831')
        if gvars[0] <= 25:
            model.unitJoin(gvars[0], 255, -1)
            gvars[0] += 1
            goto('lab1831')
        gvars[0] = 0
        label('lab1841')
        if gvars[0] <= 169:
            stage.setFAvatar(gvars[0], 0)
            gvars[0] += 1
            goto('lab1841')
        stage.setFAvatar(35, 46)
        stage.setFAvatar(119, 73)
        if model.random() < 34:
            stage.setFAvatar(119, 91)
        elif model.random() < 50:
            stage.setFAvatar(119, 92)
    if vars[1032]:
        gvars[4076] = 2
        pvars[0] = 5273600
        gvars[0] = 0
        label('lab1874')
        if gvars[0] <= 250:
            stage.varOper(0, 0, 2, 0, 1025)
            pvars[0] += 2
            stage.varOper(0, 0, 2, 0, 1025)
            pvars[0] += 2
            stage.varOper(0, 0, 2, 0, 1025)
            pvars[0] += 4
            gvars[0] += 1
            goto('lab1874')
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[0] = 5274367
        stage.varOper(0, 0, 2, 0, 1)
        gvars[4076] = 0
        gvars[0] = 0
        gvars[3] = 0
        gvars[4] = 0
        pvars[1] = 4217744
        label('lab1900')
        if gvars[0] <= 173:
            gvars[4076] = 0
            gvars[1] = 0
            gvars[2] = 5273600
            stage.infoTransfer(26, '157')
            if gvars[4025] == 36:
                goto('lab1900')
            if gvars[4025] == 49:
                goto('lab1900')
            if gvars[4025] == 67:
                goto('lab1900')
            if gvars[4025] >= 103 and gvars[4025] <= 111:
                goto('lab1900')
            if gvars[4025] >= 119 and gvars[4025] <= 127:
                goto('lab1900')
            if gvars[4025] == 56 and model.random() < 75:
                goto('lab1900')
            if gvars[4025] == 57 and model.random() < 75:
                goto('lab1900')
            if gvars[4025] == 83 and model.random() < 75:
                goto('lab1900')
            if gvars[4025] == 84 and model.random() < 75:
                goto('lab1900')
            if gvars[4025] == 88 and model.random() < 75:
                goto('lab1900')
            if gvars[4025] == 89 and model.random() < 75:
                goto('lab1900')
            if gvars[4025] == 138 and model.random() < 50:
                goto('lab1900')
            if gvars[4025] >= 73 and gvars[4025] <= 80 and (model.random() < 66):
                goto('lab1900')
            if gvars[4025] >= 128 and gvars[4025] <= 156 and (model.random() < 70):
                goto('lab1900')
            gvars[3] = stage.unitAttr(gvars[0], 9)
            if gvars[3] <= 34:
                if gvars[4025] >= 6 and gvars[4025] <= 17:
                    goto('lab1900')
                if gvars[4025] >= 20 and gvars[4025] <= 26:
                    goto('lab1900')
                if gvars[4025] >= 30 and gvars[4025] <= 31:
                    goto('lab1900')
                if gvars[4025] >= 37 and gvars[4025] <= 38:
                    goto('lab1900')
                if gvars[4025] >= 52 and gvars[4025] <= 53:
                    goto('lab1900')
                if gvars[4025] >= 62 and gvars[4025] <= 63:
                    goto('lab1900')
                if gvars[4025] >= 66 and gvars[4025] <= 68:
                    goto('lab1900')
                if gvars[4025] >= 96 and gvars[4025] <= 98:
                    goto('lab1900')
                if gvars[4025] >= 103 and gvars[4025] <= 104:
                    goto('lab1900')
                if gvars[4025] >= 111 and gvars[4025] <= 114:
                    goto('lab1900')
                if gvars[4025] == 44:
                    goto('lab1900')
                if gvars[4025] == 55:
                    goto('lab1900')
                if gvars[4025] == 59:
                    goto('lab1900')
                if gvars[4025] == 72:
                    goto('lab1900')
                if gvars[4025] == 91:
                    goto('lab1900')
                if gvars[4025] == 99:
                    goto('lab1900')
                if gvars[4025] == 117:
                    goto('lab1900')
                if gvars[4025] == 128:
                    goto('lab1900')
                if gvars[4025] == 140:
                    goto('lab1900')
                if gvars[4025] == 145:
                    goto('lab1900')
                if gvars[4025] == 151:
                    goto('lab1900')
            if gvars[3] >= 35 and gvars[4025] == 22:
                goto('lab1900')
            gvars[3] = stage.unitAttr(gvars[0], 11)
            if gvars[3] <= 34:
                if gvars[4025] >= 27 and gvars[4025] <= 28:
                    goto('lab1900')
                if gvars[4025] >= 39 and gvars[4025] <= 40:
                    goto('lab1900')
                if gvars[4025] >= 42 and gvars[4025] <= 43:
                    goto('lab1900')
                if gvars[4025] >= 73 and gvars[4025] <= 80:
                    goto('lab1900')
                if gvars[4025] >= 92 and gvars[4025] <= 95:
                    goto('lab1900')
                if gvars[4025] >= 153 and gvars[4025] <= 156:
                    goto('lab1900')
                if gvars[4025] == 1:
                    goto('lab1900')
                if gvars[4025] == 46:
                    goto('lab1900')
                if gvars[4025] == 58:
                    goto('lab1900')
                if gvars[4025] == 60:
                    goto('lab1900')
                if gvars[4025] == 70:
                    goto('lab1900')
                if gvars[4025] == 86:
                    goto('lab1900')
                if gvars[4025] == 102:
                    goto('lab1900')
                if gvars[4025] == 116:
                    goto('lab1900')
                if gvars[4025] == 130:
                    goto('lab1900')
                if gvars[4025] == 141:
                    goto('lab1900')
                if gvars[4025] == 147:
                    goto('lab1900')
            if gvars[3] >= 40:
                if gvars[4025] == 29 and model.random() < 50:
                    goto('lab1900')
                if gvars[4025] == 41 and model.random() < 50:
                    goto('lab1900')
            if gvars[0] == 3 and gvars[4025] == 70:
                goto('lab1900')
            if gvars[0] == 7 and gvars[4025] == 70:
                goto('lab1900')
            if gvars[0] == 9 and gvars[4025] == 70:
                goto('lab1900')
            if gvars[0] == 13 and gvars[4025] == 70:
                goto('lab1900')
            if gvars[0] == 17 and gvars[4025] == 70:
                goto('lab1900')
            if gvars[0] == 23 and gvars[4025] == 70:
                goto('lab1900')
            if gvars[0] >= 26:
                if gvars[4025] >= 3 and gvars[4025] <= 5:
                    goto('lab1900')
                if gvars[4025] == 21:
                    goto('lab1900')
                if gvars[4025] == 69:
                    goto('lab1900')
                if gvars[4025] == 70:
                    goto('lab1900')
                if gvars[4025] == 83:
                    goto('lab1900')
            if gvars[4074] == 0 and gvars[4025] == 69:
                goto('lab1900')
            gvars[1] = gvars[4025]
            gvars[1] *= 8
            gvars[1] += gvars[2]
            gvars[4076] = 2
            pvars[0] = gvars[1]
            stage.varOper(0, 1, 2, 4, 0)
            if stage.varTest(1, 0, 1, 0, 255):
                stage.varOper(0, 0, 2, 1, 1)
                if gvars[0] == 0:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 2:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 3:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 12:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 33:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 34:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 36:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 57:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 77:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 81:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 84:
                    if gvars[4] == 0:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 35:
                    if gvars[4] <= 1:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[0] == 119:
                    if gvars[4] <= 1:
                        gvars[4] += 1
                        goto('lab1900')
                    else:
                        gvars[4] = 0
                if gvars[4025] == 39:
                    stage.infoTransfer(4, '20\n3\n*0\n유혹')
                    stage.infoTransfer(4, '33\n1\n*0\n독연')
                    stage.infoTransfer(4, '35\n4\n*0\n속박')
                if gvars[4025] == 40:
                    stage.infoTransfer(4, '0\n1\n*0\n화계')
                    stage.infoTransfer(4, '5\n2\n*0\n수계')
                    stage.infoTransfer(4, '10\n3\n*0\n바람')
                    stage.infoTransfer(4, '15\n4\n*0\n땅')
                gvars[0] += 1
                goto('lab1900')
            else:
                pvars[0] += 2
                if stage.varTest(1, 0, 1, 0, 255):
                    stage.varOper(0, 0, 2, 1, 1)
                    if gvars[0] == 0:
                        if gvars[4] == 0:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 3:
                        if gvars[4] == 0:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 12:
                        if gvars[4] == 0:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 33:
                        if gvars[4] == 0:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 34:
                        if gvars[4] == 0:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 36:
                        if gvars[4] == 0:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 57:
                        if gvars[4] == 0:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 77:
                        if gvars[4] == 0:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 35:
                        if gvars[4] <= 1:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[0] == 119:
                        if gvars[4] <= 1:
                            gvars[4] += 1
                            goto('lab1900')
                        else:
                            gvars[4] = 0
                    if gvars[4025] == 39:
                        stage.infoTransfer(4, '20\n3\n*0\n유혹')
                        stage.infoTransfer(4, '33\n1\n*0\n독연')
                        stage.infoTransfer(4, '35\n4\n*0\n속박')
                    if gvars[4025] == 40:
                        stage.infoTransfer(4, '0\n1\n*0\n화계')
                        stage.infoTransfer(4, '5\n2\n*0\n수계')
                        stage.infoTransfer(4, '10\n3\n*0\n바람')
                        stage.infoTransfer(4, '15\n4\n*0\n땅')
                    gvars[0] += 1
                    goto('lab1900')
                else:
                    pvars[0] += 2
                    if stage.varTest(1, 0, 1, 0, 255):
                        stage.varOper(0, 0, 2, 1, 1)
                        if gvars[0] == 0:
                            if gvars[4] == 0:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 3:
                            if gvars[4] == 0:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 12:
                            if gvars[4] == 0:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 33:
                            if gvars[4] == 0:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 34:
                            if gvars[4] == 0:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 36:
                            if gvars[4] == 0:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 57:
                            if gvars[4] == 0:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 77:
                            if gvars[4] == 0:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 35:
                            if gvars[4] <= 1:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[0] == 119:
                            if gvars[4] <= 1:
                                gvars[4] += 1
                                goto('lab1900')
                            else:
                                gvars[4] = 0
                        if gvars[4025] == 39:
                            stage.infoTransfer(4, '20\n3\n*0\n유혹')
                            stage.infoTransfer(4, '33\n1\n*0\n독연')
                            stage.infoTransfer(4, '35\n4\n*0\n속박')
                        if gvars[4025] == 40:
                            stage.infoTransfer(4, '0\n1\n*0\n화계')
                            stage.infoTransfer(4, '5\n2\n*0\n수계')
                            stage.infoTransfer(4, '10\n3\n*0\n바람')
                            stage.infoTransfer(4, '15\n4\n*0\n땅')
                        gvars[0] += 1
                        goto('lab1900')
            goto('lab1900')
    if vars[1022]:
        gvars[4076] = 0
        gvars[1] = 0
        gvars[2] = 0
        gvars[3] = 0
        gvars[4] = 0
        gvars[5] = 0
        gvars[6] = 0
        gvars[7] = 0
        gvars[8] = 0
        gvars[9] = 0
        gvars[10] = 0
        gvars[11] = 0
        gvars[12] = 0
        gvars[13] = 0
        gvars[0] = 30
        gvars[1] = 2
        gvars[4] = 5268264
        gvars[5] = 5268680
        gvars[6] = 5269096
        gvars[8] = 5259976
        gvars[9] = 5260776
        label('lab2831')
        if gvars[0] <= 108:
            gvars[4076] = 0
            pvars[1] = 4217736
            gvars[2] = 4854854
            gvars[3] = gvars[0]
            gvars[3] -= 30
            gvars[3] *= 25
            gvars[3] += gvars[2]
            pvars[0] = gvars[3]
            gvars[4076] = 1
            if stage.varTest(1, 0, 0, 0, 1):
                if gvars[1] <= 103:
                    gvars[4076] = 0
                    label('lab2852')
                    stage.infoTransfer(26, '128')
                    if gvars[4025] == 123:
                        goto('lab2852')
                    if gvars[4025] >= 26:
                        gvars[10] = gvars[4025]
                        gvars[10] -= 26
                        gvars[10] *= 4
                        gvars[10] += gvars[9]
                        pvars[0] = gvars[10]
                        stage.varOper(2, 11, 2, 1, 0)
                        gvars[12] = gvars[4025]
                        if gvars[0] <= 48:
                            gvars[11] %= 2
                            if gvars[11] == 0:
                                stage.varOper(0, 1, 2, 4, 4025)
                            else:
                                goto('lab2852')
                        if gvars[0] >= 49 and gvars[0] <= 53:
                            gvars[11] %= 3
                            if gvars[11] == 0:
                                stage.varOper(0, 1, 2, 4, 4025)
                            else:
                                goto('lab2852')
                        if gvars[0] >= 75:
                            gvars[11] %= 5
                            if gvars[11] == 0:
                                stage.varOper(0, 1, 2, 4, 4025)
                            goto('lab2852')
                    else:
                        goto('lab2852')
                    pvars[1] += 1
                    label('lab2910')
                    stage.infoTransfer(26, '26')
                    if gvars[4025] >= 15:
                        stage.varOper(0, 1, 2, 4, 4025)
                    else:
                        goto('lab2910')
                    pvars[1] += 1
                    gvars[12] -= 26
                    gvars[12] *= 4
                    gvars[12] += gvars[8]
                    pvars[0] = gvars[12]
                    stage.varOper(2, 13, 2, 1, 0)
                    if gvars[13] == 3:
                        stage.varOper(0, 1, 2, 0, 3)
                    if gvars[13] == 4:
                        label('lab2933')
                        stage.infoTransfer(26, '5')
                        if gvars[4025] < 2:
                            goto('lab2933')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 5:
                        label('lab2942')
                        stage.infoTransfer(26, '5')
                        if gvars[4025] < 3:
                            goto('lab2942')
                        stage.varOper(0, 1, 2, 4, 4025)
                        if model.random() < 20:
                            stage.varOper(0, 1, 2, 0, 5)
                    if gvars[13] == 6:
                        label('lab2955')
                        stage.infoTransfer(26, '7')
                        if gvars[4025] < 4:
                            goto('lab2955')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 8:
                        label('lab2964')
                        stage.infoTransfer(26, '9')
                        if gvars[4025] < 4:
                            goto('lab2964')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 10:
                        label('lab2973')
                        stage.infoTransfer(26, '11')
                        if gvars[4025] < 5:
                            goto('lab2973')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 12:
                        label('lab2982')
                        stage.infoTransfer(26, '13')
                        if gvars[4025] < 8:
                            goto('lab2982')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 13:
                        label('lab2991')
                        stage.infoTransfer(26, '14')
                        if gvars[4025] < 11:
                            goto('lab2991')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 15:
                        label('lab3000')
                        stage.infoTransfer(26, '16')
                        if gvars[4025] < 10:
                            goto('lab3000')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 20:
                        label('lab3009')
                        stage.infoTransfer(26, '21')
                        if gvars[4025] < 12:
                            goto('lab3009')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 30:
                        label('lab3018')
                        stage.infoTransfer(26, '31')
                        if gvars[4025] < 18:
                            goto('lab3018')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 40:
                        label('lab3027')
                        stage.infoTransfer(26, '41')
                        if gvars[4025] < 24:
                            goto('lab3027')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 50:
                        label('lab3036')
                        stage.infoTransfer(26, '51')
                        if gvars[4025] < 30:
                            goto('lab3036')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 60:
                        label('lab3045')
                        stage.infoTransfer(26, '61')
                        if gvars[4025] < 35:
                            goto('lab3045')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 70:
                        label('lab3054')
                        stage.infoTransfer(26, '71')
                        if gvars[4025] < 40:
                            goto('lab3054')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 99:
                        label('lab3063')
                        stage.infoTransfer(26, '100')
                        if gvars[4025] < 50:
                            goto('lab3063')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 100:
                        stage.varOper(0, 1, 2, 0, 100)
                    if gvars[13] == 101:
                        label('lab3076')
                        stage.infoTransfer(26, '17')
                        if gvars[4025] == 5:
                            goto('lab3076')
                        if gvars[4025] == 10:
                            goto('lab3076')
                        if gvars[4025] == 11:
                            goto('lab3076')
                        if gvars[4025] < 1:
                            goto('lab3076')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 102:
                        stage.infoTransfer(26, '7')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 4)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 5)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 9)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 10)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 11)
                    if gvars[13] == 103:
                        stage.infoTransfer(26, '7')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 6)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 24)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 52)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 34)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 5)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 63)
                    if gvars[13] == 104:
                        stage.infoTransfer(26, '7')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 6)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 24)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 52)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 34)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 5)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 63)
                    if gvars[13] == 105:
                        stage.infoTransfer(26, '5')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 4)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 8)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 16)
                    if gvars[13] == 107:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 11)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 12)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 13)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 22)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 31)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 35)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 37)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 68)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 80)
                    if gvars[13] == 108:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 11)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 12)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 13)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 22)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 31)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 35)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 37)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 68)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 80)
                    if gvars[13] == 109:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 11)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 12)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 13)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 22)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 31)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 35)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 37)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 68)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 80)
                    if gvars[13] == 110:
                        label('lab3409')
                        stage.infoTransfer(26, '6')
                        if gvars[4025] < 1:
                            goto('lab3409')
                        stage.varOper(0, 1, 2, 4, 4025)
                        if model.random() < 30:
                            stage.varOper(0, 1, 2, 0, 6)
                    if gvars[13] == 111:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 4)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 17)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 18)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 34)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 36)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 24)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 40)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 56)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 31)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 47)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 63)
                        if gvars[4025] == 15:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 16:
                            stage.varOper(0, 1, 2, 0, 95)
                    if gvars[13] == 112:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 4)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 8)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 16)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 17)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 24)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 32)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 34)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 40)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 48)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 51)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 95)
                    if gvars[13] == 113:
                        stage.varOper(0, 1, 2, 0, 25)
                    if gvars[13] == 150:
                        stage.varOper(0, 1, 2, 0, 150)
                    pvars[1] += 1
                    label('lab3568')
                    stage.infoTransfer(26, '12')
                    if gvars[4025] >= 9:
                        stage.varOper(0, 1, 2, 4, 4025)
                    else:
                        goto('lab3568')
                    gvars[3] -= 4
                    pvars[1] -= 3
                    gvars[4076] = 0
                    pvars[0] = gvars[3]
                    stage.varOper(2, 7, 2, 1, 0)
                    pvars[0] = gvars[6]
                    stage.varOper(0, 0, 2, 4, 7)
                    pvars[0] = gvars[4]
                    stage.varOper(0, 0, 2, 4, 3)
                    pvars[0] = gvars[5]
                    stage.varOper(0, 0, 2, 1, 1)
                    pvars[0] = gvars[3]
                    stage.varOper(0, 0, 2, 1, 1)
                    gvars[1] += 1
                    gvars[4] += 4
                    gvars[5] += 4
                    gvars[6] += 4
                else:
                    gvars[0] = 108
            gvars[0] += 1
            goto('lab2831')
        label('lab3606')
        if gvars[0] <= 149:
            gvars[4076] = 0
            pvars[1] = 4217736
            gvars[2] = 4854854
            gvars[3] = gvars[0]
            gvars[3] -= 30
            gvars[3] *= 25
            gvars[3] += gvars[2]
            pvars[0] = gvars[3]
            gvars[4076] = 1
            if stage.varTest(1, 0, 0, 0, 1):
                if gvars[1] <= 103:
                    gvars[4076] = 0
                    label('lab3626')
                    stage.infoTransfer(26, '128')
                    if gvars[4025] == 123:
                        goto('lab3626')
                    if gvars[4025] >= 26:
                        gvars[10] = gvars[4025]
                        gvars[10] -= 26
                        gvars[10] *= 4
                        gvars[10] += gvars[9]
                        pvars[0] = gvars[10]
                        stage.varOper(2, 11, 2, 1, 0)
                        gvars[12] = gvars[4025]
                        gvars[11] %= 7
                        if gvars[11] == 0:
                            stage.varOper(0, 1, 2, 4, 4025)
                        goto('lab3626')
                    else:
                        goto('lab3626')
                    gvars[3] -= 7
                    gvars[4076] = 0
                    pvars[0] = gvars[3]
                    stage.varOper(2, 7, 2, 1, 0)
                    pvars[0] = gvars[6]
                    stage.varOper(0, 0, 2, 4, 7)
                    pvars[0] = gvars[4]
                    stage.varOper(0, 0, 2, 4, 3)
                    gvars[4076] = 1
                    pvars[0] = gvars[3]
                    stage.varOper(0, 0, 2, 1, 1)
                    gvars[4076] = 0
                    pvars[0] = gvars[3]
                    stage.varOper(2, 7, 2, 1, 0)
                    pvars[0] = gvars[5]
                    stage.varOper(0, 0, 2, 4, 7)
                    gvars[1] += 1
                    gvars[4] += 4
                    gvars[5] += 4
                    gvars[6] += 4
                    gvars[12] -= 26
                    gvars[12] *= 4
                    gvars[12] += gvars[8]
                    pvars[0] = gvars[12]
                    stage.varOper(2, 13, 2, 1, 0)
                    if gvars[13] == 3:
                        stage.varOper(0, 1, 2, 0, 3)
                    if gvars[13] == 4:
                        label('lab3694')
                        stage.infoTransfer(26, '5')
                        if gvars[4025] < 2:
                            goto('lab3694')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 5:
                        label('lab3703')
                        stage.infoTransfer(26, '5')
                        if gvars[4025] < 3:
                            goto('lab3703')
                        stage.varOper(0, 1, 2, 4, 4025)
                        if model.random() < 20:
                            stage.varOper(0, 1, 2, 0, 5)
                    if gvars[13] == 6:
                        label('lab3716')
                        stage.infoTransfer(26, '7')
                        if gvars[4025] < 4:
                            goto('lab3716')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 8:
                        label('lab3725')
                        stage.infoTransfer(26, '9')
                        if gvars[4025] < 4:
                            goto('lab3725')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 10:
                        label('lab3734')
                        stage.infoTransfer(26, '11')
                        if gvars[4025] < 5:
                            goto('lab3734')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 12:
                        label('lab3743')
                        stage.infoTransfer(26, '13')
                        if gvars[4025] < 8:
                            goto('lab3743')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 13:
                        label('lab3752')
                        stage.infoTransfer(26, '14')
                        if gvars[4025] < 11:
                            goto('lab3752')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 15:
                        label('lab3761')
                        stage.infoTransfer(26, '16')
                        if gvars[4025] < 10:
                            goto('lab3761')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 20:
                        label('lab3770')
                        stage.infoTransfer(26, '21')
                        if gvars[4025] < 12:
                            goto('lab3770')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 30:
                        label('lab3779')
                        stage.infoTransfer(26, '31')
                        if gvars[4025] < 18:
                            goto('lab3779')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 40:
                        label('lab3788')
                        stage.infoTransfer(26, '41')
                        if gvars[4025] < 24:
                            goto('lab3788')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 50:
                        label('lab3797')
                        stage.infoTransfer(26, '51')
                        if gvars[4025] < 30:
                            goto('lab3797')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 60:
                        label('lab3806')
                        stage.infoTransfer(26, '61')
                        if gvars[4025] < 35:
                            goto('lab3806')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 70:
                        label('lab3815')
                        stage.infoTransfer(26, '71')
                        if gvars[4025] < 40:
                            goto('lab3815')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 99:
                        label('lab3824')
                        stage.infoTransfer(26, '100')
                        if gvars[4025] < 50:
                            goto('lab3824')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 100:
                        stage.varOper(0, 1, 2, 0, 100)
                    if gvars[13] == 101:
                        label('lab3837')
                        stage.infoTransfer(26, '17')
                        if gvars[4025] == 5:
                            goto('lab3837')
                        if gvars[4025] == 10:
                            goto('lab3837')
                        if gvars[4025] == 11:
                            goto('lab3837')
                        if gvars[4025] < 1:
                            goto('lab3837')
                        stage.varOper(0, 1, 2, 4, 4025)
                    if gvars[13] == 102:
                        stage.infoTransfer(26, '7')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 4)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 5)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 9)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 10)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 11)
                    if gvars[13] == 103:
                        stage.infoTransfer(26, '7')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 6)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 24)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 52)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 34)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 5)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 63)
                    if gvars[13] == 104:
                        stage.infoTransfer(26, '7')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 6)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 24)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 52)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 34)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 5)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 63)
                    if gvars[13] == 105:
                        stage.infoTransfer(26, '5')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 4)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 8)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 16)
                    if gvars[13] == 107:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 11)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 12)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 13)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 22)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 31)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 35)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 37)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 68)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 80)
                    if gvars[13] == 108:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 11)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 12)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 13)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 22)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 31)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 35)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 37)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 68)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 80)
                    if gvars[13] == 109:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 3)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 11)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 12)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 13)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 22)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 31)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 35)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 37)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 68)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 80)
                    if gvars[13] == 110:
                        label('lab4170')
                        stage.infoTransfer(26, '6')
                        if gvars[4025] < 1:
                            goto('lab4170')
                        stage.varOper(0, 1, 2, 4, 4025)
                        if model.random() < 30:
                            stage.varOper(0, 1, 2, 0, 6)
                    if gvars[13] == 111:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 1)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 4)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 17)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 18)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 34)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 36)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 24)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 40)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 56)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 31)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 47)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 63)
                        if gvars[4025] == 15:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 16:
                            stage.varOper(0, 1, 2, 0, 95)
                    if gvars[13] == 112:
                        stage.infoTransfer(26, '15')
                        if gvars[4025] == 0:
                            stage.varOper(0, 1, 2, 0, 2)
                        if gvars[4025] == 1:
                            stage.varOper(0, 1, 2, 0, 4)
                        if gvars[4025] == 2:
                            stage.varOper(0, 1, 2, 0, 8)
                        if gvars[4025] == 3:
                            stage.varOper(0, 1, 2, 0, 16)
                        if gvars[4025] == 4:
                            stage.varOper(0, 1, 2, 0, 17)
                        if gvars[4025] == 5:
                            stage.varOper(0, 1, 2, 0, 20)
                        if gvars[4025] == 6:
                            stage.varOper(0, 1, 2, 0, 24)
                        if gvars[4025] == 7:
                            stage.varOper(0, 1, 2, 0, 32)
                        if gvars[4025] == 8:
                            stage.varOper(0, 1, 2, 0, 33)
                        if gvars[4025] == 9:
                            stage.varOper(0, 1, 2, 0, 34)
                        if gvars[4025] == 10:
                            stage.varOper(0, 1, 2, 0, 40)
                        if gvars[4025] == 11:
                            stage.varOper(0, 1, 2, 0, 48)
                        if gvars[4025] == 12:
                            stage.varOper(0, 1, 2, 0, 51)
                        if gvars[4025] == 13:
                            stage.varOper(0, 1, 2, 0, 79)
                        if gvars[4025] == 14:
                            stage.varOper(0, 1, 2, 0, 95)
                    if gvars[13] == 113:
                        stage.varOper(0, 1, 2, 0, 25)
                    if gvars[13] == 150:
                        stage.varOper(0, 1, 2, 0, 150)
                    gvars[3] += 4
                    gvars[4076] = 0
                    pvars[0] = gvars[3]
                    stage.varOper(2, 7, 2, 1, 0)
                    pvars[0] = gvars[6]
                    stage.varOper(0, 0, 2, 4, 7)
                    pvars[0] = gvars[4]
                    stage.varOper(0, 0, 2, 4, 3)
                    gvars[4076] = 1
                    pvars[0] = gvars[3]
                    stage.varOper(0, 0, 2, 1, 1)
                    gvars[4076] = 0
                    pvars[0] = gvars[3]
                    stage.varOper(2, 7, 2, 1, 0)
                    pvars[0] = gvars[5]
                    stage.varOper(0, 0, 2, 4, 7)
                    gvars[1] += 1
                    gvars[4] += 4
                    gvars[5] += 4
                    gvars[6] += 4
                else:
                    gvars[0] = 149
            gvars[0] += 1
            goto('lab3606')
    if vars[1002]:
        if gvars[4074] == 0:
            stage.getItem(161, 4)
            stage.getItem(162, 4)
            stage.getItem(163, 4)
            stage.getItem(164, 4)
            stage.getItem(165, 4)
        stage.getItem(160, 4)
    model.unitJoin(0, 0, 2)
    stage.setJoinEquip(0, 33, 0, 0, 0, 0)
    stage.say('&0\n……음.\n그럼 이만 가 보겠소, 어르신.')
    stage.unit(0).setAction(0, 2)
    stage.delay(3)
    stage.unit(0).setAction(17)
    stage.say('&0\n출발하라!\n격전이 벌어지는 영천으로 향한다!')
    stage.unit(0).setAction(0)
    stage.delay(3)
    stage.unit(181).setAction(0, 0)
    stage.say('&181\n예!')
    stage.unit(182).setAction(0, 2)
    stage.say('&182\n예!')
    stage.unitsMove([[stage.unit(0), 40, 80, 2], [stage.unit(181), 40, 98, 2], [stage.unit(182), 40, 70, 2], [stage.unit(157), 54, 20, 0]])
    stage.delay(3)
    stage.unit(157).setAction(0, 2)
    if vars[300]:
        stage.say('&157\n스스로 간웅이 되고자 한다니!\n좋습니다, 조 나리……\n그 뜻을 향해 힘껏 나아가 보십시오.\n&157\n허, 허, 허!!')
    if not vars[300]:
        stage.say('&157\n저 젊은이의 상은\n정녕 난세의 간웅이요, 치세의 능신이로다.\n&157\n장차 어떤 인물이 될 것인가……?\n어디 두고 보자꾸나.\n&157\n허, 허, 허.')
    stage.unitsMove([[stage.unit(0), 40, 98, 2], [stage.unit(182), 40, 98, 2], [stage.unit(157), 54, 5, 0]])
    stage.delay(3)
    stage.effectSound(201, 255)
    stage.clsUnit()
    return False


def scene2():
    if vars[1001]:
        stage.infoTransfer(4, '14\n29\n19\n사마의 모래폭풍')
        stage.infoTransfer(4, '21\n30\n17\n초선 기절')
        stage.infoTransfer(4, '21\n31\n22\n곽가 혼란')
        stage.infoTransfer(4, '21\n31\n23\n가후 혼란')
        gvars[4076] = 0
        gvars[0] = 0
        gvars[1] = 0
        label('lab4439')
        if gvars[0] <= 349:
            gvars[1] = stage.unitAttr(gvars[0], 17)
            if gvars[1] == 27:
                stage.infoTransfer(4, '57\n30\n*0\n풍수사 회귀')
            gvars[0] += 1
            goto('lab4439')
        gvars[4076] = 1
        pvars[200] = 4691288
        stage.varOper(0, 200, 2, 0, 100)
        pvars[201] = 4522074
        stage.varOper(0, 201, 2, 0, 100)
        pvars[202] = 4525087
        stage.varOper(0, 202, 2, 0, 100)
        pvars[203] = 4524851
        stage.varOper(0, 203, 2, 0, 100)
        pvars[204] = 4524872
        stage.varOper(0, 204, 2, 0, 100)
        pvars[205] = 4229334
        stage.varOper(0, 205, 2, 0, 100)
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[206] = 4325694
        stage.varOper(0, 206, 2, 0, 20)
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[207] = 4223753
        stage.varOper(0, 207, 2, 0, 127)
        gvars[4076] = 0
        gvars[4071] = 255
        stage.info('3            S     A     B     C\n기본 기준    90    70    50     0  \n기본 성장     4     3     2     1\n\n완전 기준                90    70\n완전 성장                 3     2')
        gvars[4076] = 1
        pvars[297] = 4857851
        stage.varOper(0, 297, 2, 0, 30)
        pvars[298] = 4857876
        stage.varOper(0, 298, 2, 0, 80)
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[208] = 4330304
        stage.varOper(0, 208, 2, 0, 1)
        pvars[209] = 4416633
        stage.varOper(0, 209, 2, 0, 1)
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[210] = 4341409
        stage.varOper(0, 210, 2, 0, 50)
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[211] = 4453911
        pvars[212] = 4453955
        pvars[213] = 4453999
        pvars[214] = 4454043
        pvars[215] = 4454087
        stage.varOper(0, 211, 2, 0, 35)
        stage.varOper(0, 212, 2, 0, 35)
        stage.varOper(0, 213, 2, 0, 35)
        stage.varOper(0, 214, 2, 0, 35)
        stage.varOper(0, 215, 2, 0, 35)
        gvars[4076] = 0
        gvars[4071] = 255
        stage.info('4[C3A【능력치 증감 비율】]\n\n  [C28◇공격력   상승 20%   하락 30%]\n  [C3A◇방어력   상승 20%   하락 30%]\n  [C27◇정신력   상승 20%   하락 30%]\n  [C25◇순발력   상승 20%   하락 30%]\n  [C76◇사기   상승 20%   하락 30%]')
        gvars[4076] = 1
        pvars[216] = 4856953
        stage.varOper(0, 216, 2, 0, 5)
        pvars[217] = 4857078
        stage.varOper(0, 217, 2, 0, 1)
        pvars[218] = 4857253
        stage.varOper(0, 218, 2, 0, 7)
        pvars[219] = 4857653
        stage.varOper(0, 219, 2, 0, 11)
        pvars[220] = 4857678
        stage.varOper(0, 220, 2, 0, 8)
        pvars[221] = 4857703
        stage.varOper(0, 221, 2, 0, 10)
        pvars[222] = 4857728
        stage.varOper(0, 222, 2, 0, 9)
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[229] = 4860456
        stage.varOper(0, 229, 2, 0, 0)
        gvars[4076] = 2
        pvars[223] = 4860462
        pvars[224] = 4860497
        pvars[225] = 4860532
        stage.varOper(0, 223, 2, 0, 261)
        stage.varOper(0, 224, 2, 0, 261)
        stage.varOper(0, 225, 2, 0, 261)
        pvars[245] = 4863255
        stage.varOper(0, 245, 2, 0, 1515875940)
        pvars[226] = 4860566
        pvars[227] = 4860601
        pvars[228] = 4860636
        stage.varOper(0, 226, 2, 0, 1538)
        stage.varOper(0, 227, 2, 0, 1538)
        stage.varOper(0, 228, 2, 0, 1538)
        gvars[4076] = 1
        pvars[230] = 4860595
        stage.varOper(0, 230, 2, 0, 5)
        gvars[4076] = 0
        pvars[231] = 4860670
        pvars[232] = 4860705
        pvars[233] = 4860740
        stage.varOper(0, 231, 2, 0, 17040386)
        stage.varOper(0, 232, 2, 0, 17040386)
        stage.varOper(0, 233, 2, 0, 17040386)
        pvars[234] = 4860775
        pvars[235] = 4860810
        pvars[236] = 4860845
        stage.varOper(0, 234, 2, 0, 17105410)
        stage.varOper(0, 235, 2, 0, 17105410)
        stage.varOper(0, 236, 2, 0, 17105410)
        gvars[4076] = 1
        pvars[237] = 4860806
        stage.varOper(0, 237, 2, 0, 0)
        gvars[4076] = 0
        pvars[238] = 4860880
        pvars[239] = 4860915
        pvars[240] = 4860950
        stage.varOper(0, 238, 2, 0, 17105666)
        stage.varOper(0, 239, 2, 0, 17105666)
        stage.varOper(0, 240, 2, 0, 17105666)
        pvars[241] = 4863495
        stage.varOper(0, 241, 2, 0, 1515875940)
        gvars[4076] = 1
        pvars[242] = 4860987
        pvars[243] = 4861022
        pvars[244] = 4861057
        stage.varOper(0, 242, 2, 0, 4)
        stage.varOper(0, 243, 2, 0, 4)
        stage.varOper(0, 244, 2, 0, 4)
        gvars[4076] = 0
        pvars[246] = 4861089
        pvars[247] = 4861124
        pvars[248] = 4861159
        stage.varOper(0, 246, 2, 0, 67240961)
        stage.varOper(0, 247, 2, 0, 67240961)
        stage.varOper(0, 248, 2, 0, 67240961)
        gvars[4076] = 1
        pvars[249] = 4861086
        stage.varOper(0, 249, 2, 0, 0)
        gvars[4076] = 2
        pvars[250] = 4861196
        pvars[251] = 4861231
        pvars[252] = 4861266
        stage.varOper(0, 250, 2, 0, 1284)
        stage.varOper(0, 251, 2, 0, 1284)
        stage.varOper(0, 252, 2, 0, 1284)
        gvars[4076] = 0
        pvars[253] = 4861299
        pvars[254] = 4861334
        pvars[255] = 4861369
        stage.varOper(0, 253, 2, 0, 67240452)
        stage.varOper(0, 254, 2, 0, 67240452)
        stage.varOper(0, 255, 2, 0, 67240452)
        gvars[4076] = 2
        pvars[256] = 4861407
        pvars[257] = 4861442
        pvars[258] = 4861477
        stage.varOper(0, 256, 2, 0, 515)
        stage.varOper(0, 257, 2, 0, 515)
        stage.varOper(0, 258, 2, 0, 515)
        gvars[4076] = 0
        pvars[259] = 4861510
        pvars[260] = 4861545
        pvars[261] = 4861580
        stage.varOper(0, 259, 2, 0, 33751555)
        stage.varOper(0, 260, 2, 0, 33751555)
        stage.varOper(0, 261, 2, 0, 33751555)
        pvars[262] = 4861613
        pvars[263] = 4861648
        pvars[264] = 4861683
        stage.varOper(0, 262, 2, 0, 16909314)
        stage.varOper(0, 263, 2, 0, 16909314)
        stage.varOper(0, 264, 2, 0, 16909314)
        pvars[265] = 4861617
        pvars[266] = 4861652
        pvars[267] = 4861687
        stage.varOper(0, 265, 2, 0, 517)
        stage.varOper(0, 266, 2, 0, 517)
        stage.varOper(0, 267, 2, 0, 517)
        pvars[268] = 4861719
        pvars[269] = 4861754
        pvars[270] = 4861789
        stage.varOper(0, 268, 2, 0, 50463746)
        stage.varOper(0, 269, 2, 0, 50463746)
        stage.varOper(0, 270, 2, 0, 50463746)
        gvars[4076] = 2
        pvars[270] = 4862561
        stage.varOper(0, 270, 2, 0, 1538)
        gvars[4076] = 0
        pvars[296] = 4864455
        stage.varOper(0, 296, 2, 0, 1850762340)
        pvars[271] = 4862590
        stage.varOper(0, 271, 2, 0, 33685508)
        pvars[272] = 4862595
        stage.varOper(0, 272, 2, 0, 17039618)
        gvars[4076] = 2
        pvars[273] = 4862631
        stage.varOper(0, 273, 2, 0, 1026)
        gvars[4076] = 0
        pvars[274] = 4862665
        stage.varOper(0, 274, 2, 0, 17170946)
        gvars[4076] = 2
        pvars[275] = 4862696
        stage.varOper(0, 275, 2, 0, 768)
        pvars[276] = 4862701
        stage.varOper(0, 276, 2, 0, 1025)
        gvars[4076] = 0
        pvars[277] = 4862731
        stage.varOper(0, 277, 2, 0, 67240704)
        pvars[278] = 4862737
        stage.varOper(0, 278, 2, 0, 516)
        gvars[4076] = 0
        pvars[279] = 4862769
        stage.varOper(0, 279, 2, 0, 50528772)
        gvars[4076] = 0
        pvars[280] = 4862835
        stage.varOper(0, 280, 2, 0, 16845572)
        pvars[281] = 4862842
        stage.varOper(0, 281, 2, 0, 259)
        pvars[282] = 4862893
        stage.varOper(0, 282, 2, 0, 0)
        pvars[283] = 4862871
        stage.varOper(0, 283, 2, 0, 33620235)
        pvars[284] = 4862875
        stage.varOper(0, 284, 2, 0, 16974337)
        pvars[285] = 4862910
        stage.varOper(0, 285, 2, 0, 17039620)
        gvars[4076] = 2
        pvars[286] = 4862946
        stage.varOper(0, 286, 2, 0, 1538)
        gvars[4076] = 0
        pvars[287] = 4862975
        stage.varOper(0, 287, 2, 0, 16845572)
        pvars[288] = 4862979
        stage.varOper(0, 288, 2, 0, 50594049)
        pvars[289] = 4862983
        stage.varOper(0, 289, 2, 0, 1)
        pvars[290] = 4864575
        stage.varOper(0, 290, 2, 0, 1348098650)
        gvars[4076] = 1
        pvars[291] = 4864635
        stage.varOper(0, 291, 2, 0, 90)
        gvars[4076] = 0
        pvars[292] = 4864935
        stage.varOper(0, 292, 2, 0, 1515870810)
        gvars[4076] = 0
        pvars[293] = 4864995
        stage.varOper(0, 293, 2, 0, 1515870810)
        pvars[294] = 4865175
        stage.varOper(0, 294, 2, 0, 1515870810)
        pvars[295] = 4864755
        stage.varOper(0, 295, 2, 0, 1348098660)
        gvars[4076] = 2
        pvars[0] = 5273600
        gvars[0] = 0
        label('lab4755')
        if gvars[0] <= 250:
            stage.varOper(0, 0, 2, 0, 1025)
            pvars[0] += 2
            stage.varOper(0, 0, 2, 0, 1025)
            pvars[0] += 2
            stage.varOper(0, 0, 2, 0, 1025)
            pvars[0] += 4
            gvars[0] += 1
            goto('lab4755')
        gvars[4076] = 0
        gvars[4076] = 1
        pvars[0] = 5273606
        gvars[0] = 0
        label('lab4773')
        if gvars[0] <= 250:
            stage.varOper(0, 0, 2, 0, 255)
            pvars[0] += 8
            gvars[0] += 1
            goto('lab4773')
        gvars[4076] = 0
        stage.infoTransfer(5, '13\n3\n63\n조련사-곰-마비 공격')
        stage.infoTransfer(5, '15\n3\n71\n토우-혼란 공격')
        stage.infoTransfer(5, '16\n3\n70\n목인-중독 공격')
        stage.infoTransfer(5, '100\n3\n36\n무희-주변 이상 상태 회복')
        gvars[4076] = 1
        pvars[0] = 5273711
        stage.varOper(0, 0, 2, 0, 60)
        pvars[0] = 5273719
        stage.varOper(0, 0, 2, 0, 60)
        pvars[0] = 5273735
        stage.varOper(0, 0, 2, 0, 60)
        gvars[4076] = 0
        if model.random() < 50:
            stage.setFAvatar(2, 27)
        if model.random() < 50:
            stage.setFAvatar(34, 29)
        if model.random() < 34:
            stage.setFAvatar(119, 91)
            return False
        if model.random() < 50:
            stage.setFAvatar(119, 92)
    return False


def scene3():
    if vars[1002]:
        if vars[1021]:
            gvars[4076] = 0
            pvars[202] = 4854850
            stage.varOper(0, 202, 2, 0, 167777346)
            pvars[203] = 4854875
            stage.varOper(0, 203, 2, 0, 169088063)
            pvars[204] = 4854900
            stage.varOper(0, 204, 2, 0, 169546805)
            pvars[205] = 4854925
            stage.varOper(0, 205, 2, 0, 168432749)
            gvars[4076] = 1
            pvars[286] = 4854922
            stage.varOper(0, 286, 2, 0, 7)
            gvars[4076] = 0
            pvars[206] = 4854950
            stage.varOper(0, 206, 2, 0, 167973940)
            pvars[236] = 4768002
            stage.varOper(0, 236, 2, 0, 0xDED1E4C0)
            pvars[237] = 4768006
            stage.varOper(0, 237, 2, 0, 58046)
            gvars[4076] = 1
            pvars[238] = 4337228
            stage.varOper(0, 238, 2, 0, 52)
            gvars[4076] = 0
            pvars[207] = 4854975
            stage.varOper(0, 207, 2, 0, 168039475)
            pvars[208] = 4855000
            stage.varOper(0, 208, 2, 0, 167777334)
            pvars[209] = 4855050
            stage.varOper(0, 209, 2, 0, 167973976)
            pvars[210] = 4855075
            stage.varOper(0, 210, 2, 0, 169086756)
            pvars[211] = 4855125
            stage.varOper(0, 211, 2, 0, 168498227)
            pvars[212] = 4855150
            stage.varOper(0, 212, 2, 0, 167907171)
            pvars[213] = 4855300
            stage.varOper(0, 213, 2, 0, 167777374)
            pvars[214] = 4855325
            stage.varOper(0, 214, 2, 0, 33555554)
            gvars[4076] = 1
            pvars[215] = 4440614
            stage.varOper(0, 215, 2, 0, 56)
            pvars[216] = 4440625
            stage.varOper(0, 216, 2, 0, 56)
            pvars[217] = 4440636
            stage.varOper(0, 217, 2, 0, 56)
            gvars[4076] = 0
            pvars[218] = 4768048
            stage.varOper(0, 218, 2, 0, 0xD8CBAAD4)
            pvars[219] = 4855350
            stage.varOper(0, 219, 2, 0, 169088056)
            pvars[220] = 4855375
            stage.varOper(0, 220, 2, 0, 167777363)
            pvars[221] = 4855400
            stage.varOper(0, 221, 2, 0, 170397497)
            pvars[222] = 4855425
            stage.varOper(0, 222, 2, 0, 169545552)
            pvars[223] = 4856075
            stage.varOper(0, 223, 2, 0, 169088078)
            pvars[224] = 4856100
            stage.varOper(0, 224, 2, 0, 167777345)
            gvars[4076] = 1
            pvars[225] = 4218677
            stage.varOper(0, 225, 2, 0, 65)
            pvars[226] = 4454682
            stage.varOper(0, 226, 2, 0, 65)
            gvars[4076] = 0
            pvars[227] = 4768149
            stage.varOper(0, 227, 2, 0, 0xE2CAD8CC)
            pvars[228] = 4768153
            stage.varOper(0, 228, 2, 0, 0xF7BBA5B9)
            pvars[229] = 4856125
            stage.varOper(0, 229, 2, 0, 167777359)
            pvars[235] = 4856150
            stage.varOper(0, 235, 2, 0, 169088115)
            pvars[230] = 4856175
            stage.varOper(0, 230, 2, 0, 167777328)
            pvars[231] = 4856350
            stage.varOper(0, 231, 2, 0, 169807738)
            pvars[232] = 4856375
            stage.varOper(0, 232, 2, 0, 168300393)
            pvars[233] = 4856450
            stage.varOper(0, 233, 2, 0, 170397474)
            pvars[234] = 4856500
            stage.varOper(0, 234, 2, 0, 170004348)
            gvars[4076] = 1
            pvars[239] = 4856847
            stage.varOper(0, 239, 2, 0, 64)
            pvars[240] = 4856872
            stage.varOper(0, 240, 2, 0, 67)
            pvars[241] = 4856876
            stage.varOper(0, 241, 2, 0, 30)
            pvars[242] = 4439641
            stage.varOper(0, 242, 2, 0, 67)
            gvars[4076] = 0
            pvars[243] = 4768171
            stage.varOper(0, 243, 2, 0, 0xBFB2ABC8)
            gvars[4076] = 1
            pvars[244] = 4856897
            stage.varOper(0, 244, 2, 0, 68)
            pvars[245] = 4209280
            stage.varOper(0, 245, 2, 0, 40)
            pvars[246] = 4856997
            stage.varOper(0, 246, 2, 0, 120)
            pvars[247] = 4857001
            stage.varOper(0, 247, 2, 0, 79)
            pvars[248] = 4857072
            stage.varOper(0, 248, 2, 0, 114)
            pvars[249] = 4857076
            stage.varOper(0, 249, 2, 0, 60)
            pvars[250] = 4454503
            stage.varOper(0, 250, 2, 0, 42)
            gvars[4076] = 0
            pvars[251] = 4767904
            stage.varOper(0, 251, 2, 0, 0xA6D3CACA)
            gvars[4076] = 1
            pvars[252] = 4857147
            stage.varOper(0, 252, 2, 0, 104)
            pvars[255] = 4857151
            stage.varOper(0, 255, 2, 0, 25)
            gvars[4076] = 0
            pvars[253] = 4768334
            stage.varOper(0, 253, 2, 0, 0xD6CAC8CF)
            gvars[4076] = 1
            pvars[254] = 4263698
            stage.varOper(0, 254, 2, 0, 84)
            pvars[256] = 4857197
            stage.varOper(0, 256, 2, 0, 90)
            pvars[257] = 4857247
            stage.varOper(0, 257, 2, 0, 127)
            pvars[258] = 4857297
            stage.varOper(0, 258, 2, 0, 110)
            pvars[259] = 4857322
            stage.varOper(0, 259, 2, 0, 32)
            pvars[260] = 4857326
            stage.varOper(0, 260, 2, 0, 20)
            pvars[261] = 4857347
            stage.varOper(0, 261, 2, 0, 33)
            pvars[262] = 4857351
            stage.varOper(0, 262, 2, 0, 20)
            if gvars[4074] == 0:
                gvars[4076] = 0
                pvars[263] = 4767765
                stage.varOper(0, 263, 2, 0, 0xB8B1B0D7)
                gvars[4076] = 1
                pvars[264] = 4857397
                stage.varOper(0, 264, 2, 0, 29)
                pvars[265] = 4444111
                stage.varOper(0, 265, 2, 0, 29)
            if gvars[4074] == 1:
                pvars[263] = 4857397
                stage.varOper(0, 263, 2, 0, 103)
                pvars[264] = 4857401
                stage.varOper(0, 264, 2, 0, 3)
            pvars[266] = 4857422
            stage.varOper(0, 266, 2, 0, 117)
            pvars[267] = 4857472
            stage.varOper(0, 267, 2, 0, 106)
            pvars[268] = 4857547
            stage.varOper(0, 268, 2, 0, 82)
            pvars[269] = 4857572
            stage.varOper(0, 269, 2, 0, 81)
            pvars[270] = 4857576
            stage.varOper(0, 270, 2, 0, 37)
            pvars[271] = 4857601
            stage.varOper(0, 271, 2, 0, 3)
            pvars[285] = 4337246
            stage.varOper(0, 285, 2, 0, 49)
            pvars[272] = 4857647
            stage.varOper(0, 272, 2, 0, 108)
            pvars[273] = 4857672
            stage.varOper(0, 273, 2, 0, 119)
            pvars[274] = 4857697
            stage.varOper(0, 274, 2, 0, 43)
            pvars[275] = 4857722
            stage.varOper(0, 275, 2, 0, 100)
            gvars[4076] = 0
        if not vars[1021] and not vars[1022]:
            gvars[4076] = 1
            pvars[282] = 4325694
            stage.varOper(0, 282, 2, 0, 20)
            gvars[4076] = 0
            gvars[4076] = 1
            pvars[283] = 4330304
            stage.varOper(0, 283, 2, 0, 1)
            gvars[4076] = 0
        if vars[1011]:
            gvars[2000] = 0
            gvars[2001] = 9
            gvars[2002] = 54
            gvars[2003] = 57
            gvars[2004] = 42
            gvars[2005] = 39
            gvars[2006] = 12
            gvars[2007] = 51
            gvars[2008] = 77
            gvars[2009] = 75
            gvars[2010] = 45
            gvars[2011] = 3
            gvars[2012] = 63
            gvars[2013] = 60
            gvars[2014] = 6
            gvars[2015] = 76
            gvars[2016] = 18
            gvars[2017] = 36
            gvars[2018] = 15
            gvars[2019] = 33
            gvars[2020] = 24
            gvars[2021] = 74
            gvars[2022] = 66
            gvars[2023] = 30
            gvars[2024] = 27
            gvars[2025] = 65
            gvars[2026] = 9
            gvars[2027] = 9
            gvars[2028] = 9
            gvars[2029] = 57
            gvars[2030] = 12
            gvars[2031] = 9
            gvars[2032] = 0
            gvars[2033] = 78
            gvars[2034] = 9
            gvars[2035] = 66
            gvars[2036] = 12
            gvars[2037] = 54
            gvars[2038] = 30
            gvars[2039] = 27
            gvars[2040] = 6
            gvars[2041] = 74
            gvars[2042] = 39
            gvars[2043] = 6
            gvars[2044] = 21
            gvars[2045] = 39
            gvars[2046] = 9
            gvars[2047] = 21
            gvars[2048] = 57
            gvars[2049] = 33
            gvars[2050] = 30
            gvars[2051] = 68
            gvars[2052] = 75
            gvars[2053] = 51
            gvars[2054] = 51
            gvars[2055] = 33
            gvars[2056] = 69
            gvars[2057] = 60
            gvars[2058] = 51
            gvars[2059] = 77
            gvars[2060] = 39
            gvars[2061] = 15
            gvars[2062] = 45
            gvars[2063] = 48
            gvars[2064] = 0
            gvars[2065] = 3
            gvars[2066] = 12
            gvars[2067] = 72
            gvars[2068] = 64
            gvars[2069] = 33
            gvars[2070] = 39
            gvars[2071] = 63
            gvars[2072] = 36
            gvars[2073] = 64
            gvars[2074] = 61
            gvars[2075] = 61
            gvars[2076] = 0
            gvars[2077] = 65
            gvars[2078] = 24
            gvars[2079] = 27
            gvars[2080] = 57
            gvars[2081] = 66
            gvars[2082] = 68
            gvars[2083] = 51
            gvars[2084] = 78
            gvars[2085] = 60
            gvars[2086] = 76
            gvars[2087] = 62
            gvars[2088] = 69
            gvars[2089] = 77
            gvars[2090] = 33
            gvars[2091] = 9
            gvars[2092] = 6
            gvars[2093] = 3
            gvars[2094] = 12
            gvars[2095] = 9
            gvars[2096] = 18
            gvars[2097] = 30
            gvars[2098] = 57
            gvars[2099] = 0
            gvars[2100] = 51
            gvars[2101] = 0
            gvars[2102] = 27
            gvars[2103] = 30
            gvars[2104] = 30
            gvars[2105] = 24
            gvars[2106] = 33
            gvars[2107] = 12
            gvars[2108] = 60
            gvars[2109] = 51
            gvars[2110] = 24
            gvars[2111] = 3
            gvars[2112] = 15
            gvars[2113] = 9
            gvars[2114] = 54
            gvars[2115] = 51
            gvars[2116] = 48
            gvars[2117] = 27
            gvars[2118] = 77
            gvars[2119] = 78
            gvars[2120] = 54
            gvars[2121] = 9
            gvars[2122] = 60
            gvars[2123] = 51
            gvars[2124] = 12
            gvars[2125] = 21
            gvars[2126] = 24
            gvars[2127] = 60
            gvars[2128] = 51
            gvars[2129] = 39
            gvars[2130] = 54
            gvars[2131] = 3
            gvars[2132] = 24
            gvars[2133] = 75
            gvars[2134] = 60
            gvars[2135] = 30
            gvars[2136] = 57
            gvars[2137] = 54
            gvars[2138] = 60
            gvars[2139] = 65
            gvars[2140] = 30
            gvars[2141] = 51
            gvars[2142] = 24
            gvars[2143] = 9
            gvars[2144] = 18
            gvars[2145] = 9
            gvars[2146] = 30
            gvars[2147] = 30
            gvars[2148] = 67
            gvars[2149] = 72
            gvars[2150] = 24
            gvars[2151] = 24
            gvars[2152] = 73
            gvars[2153] = 24
            gvars[2154] = 73
            gvars[2155] = 73
            gvars[2156] = 73
            gvars[2157] = 27
            gvars[2158] = 73
            gvars[2159] = 73
            gvars[2160] = 3
            gvars[2161] = 3
            gvars[2162] = 24
            gvars[2163] = 0
            gvars[2164] = 78
            gvars[2165] = 65
            gvars[2166] = 0
            gvars[2167] = 51
            gvars[2168] = 0
            gvars[2169] = 57
            gvars[0] = 56
            stage.infoTransfer(4, '20\n8\n*0\n유혹')
            stage.infoTransfer(4, '46\n5\n*0\n각성')
            stage.infoTransfer(4, '47\n25\n*0\n대각성')
            stage.infoTransfer(4, '39\n1\n*0\n소보급')
            stage.infoTransfer(4, '40\n12\n*0\n대보급')
            stage.infoTransfer(4, '41\n10\n*0\n원대')
            stage.infoTransfer(4, '42\n20\n*0\n원군')
            stage.infoTransfer(4, '43\n25\n*0\n수송')
            stage.infoTransfer(4, '44\n15\n*0\n건언')
            stage.infoTransfer(4, '45\n22\n*0\n헌책')
            stage.infoTransfer(4, '57\n30\n*0\n회귀')
            stage.infoTransfer(4, '71\n18\n*0\n정묘')
            stage.infoTransfer(4, '74\n28\n*0\n방화')
            gvars[0] = 88
            stage.infoTransfer(4, '20\n8\n*0\n유혹')
            stage.infoTransfer(4, '46\n5\n*0\n각성')
            stage.infoTransfer(4, '47\n25\n*0\n대각성')
            stage.infoTransfer(4, '39\n1\n*0\n소보급')
            stage.infoTransfer(4, '40\n12\n*0\n대보급')
            stage.infoTransfer(4, '41\n10\n*0\n원대')
            stage.infoTransfer(4, '42\n20\n*0\n원군')
            stage.infoTransfer(4, '43\n25\n*0\n수송')
            stage.infoTransfer(4, '44\n15\n*0\n건언')
            stage.infoTransfer(4, '45\n22\n*0\n헌책')
            stage.infoTransfer(4, '57\n30\n*0\n회귀')
            stage.infoTransfer(4, '71\n18\n*0\n정묘')
            stage.infoTransfer(4, '74\n28\n*0\n방화')
            stage.setFAvatar(4, 40)
            stage.setFAvatar(5, 6)
            stage.setFAvatar(7, 0)
            stage.setFAvatar(8, 0)
            stage.setFAvatar(9, 38)
            stage.setFAvatar(12, 41)
            stage.setFAvatar(13, 45)
            stage.setFAvatar(15, 86)
            stage.setFAvatar(21, 0)
            stage.setFAvatar(22, 51)
            stage.setFAvatar(25, 43)
            stage.setFAvatar(33, 88)
            stage.setFAvatar(66, 84)
            stage.setFAvatar(69, 26)
            stage.setFAvatar(80, 74)
            stage.setFAvatar(84, 81)
            stage.setFAvatar(164, 88)
            stage.setFAvatar(169, 74)
            if model.random() < 50:
                stage.setFAvatar(2, 27)
            if model.random() < 50:
                stage.setFAvatar(34, 29)
            if model.random() < 34:
                stage.setFAvatar(119, 91)
            elif model.random() < 50:
                stage.setFAvatar(119, 92)
        gvars[4076] = 1
        pvars[276] = 4325267
        stage.varOper(0, 276, 2, 0, 235)
        gvars[4076] = 0
        if not vars[1011] and not vars[1012]:
            gvars[4076] = 1
            pvars[277] = 4223753
            stage.varOper(0, 277, 2, 0, 127)
            gvars[4076] = 0
            gvars[4071] = 255
            stage.info('3            S     A     B     C\n기본 기준    90    70    50     0  \n기본 성장     4     3     2     1\n\n완전 기준                90    70\n완전 성장                 3     2')
        else:
            gvars[4076] = 0
            pvars[277] = 4604446
            stage.varOper(0, 277, 2, 0, 16843009)
            pvars[278] = 4604450
            stage.varOper(0, 278, 2, 0, 16843009)
            pvars[279] = 4604459
            stage.varOper(0, 279, 2, 0, 16843009)
            pvars[280] = 4604467
            stage.varOper(0, 280, 2, 0, 16843009)
            gvars[4076] = 2
            pvars[281] = 4604471
            stage.varOper(0, 281, 2, 0, 257)
            gvars[4076] = 0
        if gvars[4074] == 0 and gvars[4075] == 1:
            gvars[4076] = 0
            pvars[284] = 4248941
            stage.varOper(0, 284, 2, 0, 0x89909090)
        if gvars[4074] == 1:
            gvars[4076] = 1
            pvars[284] = 4208890
            pvars[287] = 4330344
            pvars[288] = 4209294
            pvars[289] = 4330604
            stage.varOper(0, 284, 2, 0, 2)
            stage.varOper(0, 287, 2, 0, 2)
            stage.varOper(0, 288, 2, 0, 3)
            stage.varOper(0, 289, 2, 0, 3)
        if not vars[1031] and not vars[1032]:
            gvars[4076] = 2
            pvars[0] = 5273600
            gvars[0] = 0
            label('lab5425')
            if gvars[0] <= 250:
                stage.varOper(0, 0, 2, 0, 1025)
                pvars[0] += 2
                stage.varOper(0, 0, 2, 0, 1025)
                pvars[0] += 2
                stage.varOper(0, 0, 2, 0, 1025)
                pvars[0] += 4
                gvars[0] += 1
                goto('lab5425')
            gvars[4076] = 0
        if not vars[1041] and not vars[1042]:
            gvars[4076] = 1
            pvars[0] = 5273606
            gvars[0] = 0
            label('lab5446')
            if gvars[0] <= 250:
                stage.varOper(0, 0, 2, 0, 255)
                pvars[0] += 8
                gvars[0] += 1
                goto('lab5446')
            gvars[4076] = 0
            stage.infoTransfer(5, '13\n3\n63\n조련사-곰-마비 공격')
            stage.infoTransfer(5, '15\n3\n71\n토우-혼란 공격')
            stage.infoTransfer(5, '16\n3\n70\n목인-중독 공격')
            stage.infoTransfer(5, '100\n3\n36\n무희-주변 이상 상태 회복')
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
