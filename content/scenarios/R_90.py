# 임충전: existing Jojo scenario route.
def scene0():
    model.unitJoin(512, 0, 0)
    gvars['linchong.started'] = 1
    if not gvars['linchong.gear_initialized_lin']:
        stage.setJoinEquip(512, 62, 1, 34, 1, 0)
        gvars['linchong.gear_initialized_lin'] = 1
    stage.end()

def __dispatch__():
    scene0()
