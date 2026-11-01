# Source Generated with Decompyle++
# File: train.marshal (Python 3.9)


def scene0():
    stage.setStageName('훈련·맑음')
    stage.initFight(1)
    stage.setGlobalData(20, 0, -1, -1, 1, 0)
    x = 7
    data = []
    for i in range(0, 5):
        data.push({
            'x': x,
            'y': 9,
            'dir': 1,
            'ai': 1 })
        data.push({
            'x': x,
            'y': 8,
            'dir': 1,
            'ai': 1 })
        data.push({
            'x': x,
            'y': 10,
            'dir': 1,
            'ai': 1 })
        data.push({
            'x': x,
            'y': 7,
            'dir': 1,
            'ai': 1 })
        data.push({
            'x': x,
            'y': 11,
            'dir': 1,
            'ai': 1 })
        x += 1
    stage.createEnemy2(data)
    x = 19
    datas = []
    for j in range(0, 5):
        i = j * 5
        datas.push({
            'idx': i,
            'x': x,
            'y': 9,
            'dir': 3,
            'ai': 1 })
        datas.push({
            'idx': i + 1,
            'x': x,
            'y': 8,
            'dir': 3,
            'ai': 1 })
        datas.push({
            'idx': i + 2,
            'x': x,
            'y': 10,
            'dir': 3,
            'ai': 1 })
        datas.push({
            'idx': i + 3,
            'x': x,
            'y': 7,
            'dir': 3,
            'ai': 1 })
        datas.push({
            'idx': i + 4,
            'x': x,
            'y': 11,
            'dir': 3,
            'ai': 1 })
        x -= 1
    stage.createMine(datas)
    stage.loadBg(16)
    stage.draw()
    stage.bgSound(11)
    stage.setWinCondition('승리 조건\n1. 적을 전멸시킨다\n\n패배 조건\n1. 아군이 전멸한다')
    stage.showWinCondition('적을 전멸시킨다')
    stage.startOper()
    stage.setMenuVisible(True)
    return True


def scene1():
    if not vars[0] and stage.winTest():
        stage.info('공격군의 승리!')
        stage.resumeCtrl()
        stage.end()
        vars[0] = 1
        return True
    if not vars[1] and stage.loseTest():
        stage.info('수비 측 승리!')
        stage.end()
        vars[1] = 1
        return True


def scene2():
    stage.bgSound(-1)
    stage.setMenuVisible(False)
    return False

def __dispatch__():
    label('__dispatch_loop')
    idx = stage.sceneIndex()
    if idx == 1:
        finished = scene1()
        if not finished and not vars['1'] and stage.curCamp() == 2 and stage.round() >= stage.maxRound():
            stage.info('제한 턴을 초과하여 패배했습니다!')
            stage.end()
            vars['1'] = True
            finished = True
        if not finished:
            return None
    elif idx == 0:
        scene0()
    elif idx == 2:
        if not stage.isLose():
            scene2()
        else:
            return None
    stage.incSceneIdx()
    goto('__dispatch_loop')
