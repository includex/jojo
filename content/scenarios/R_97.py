# 임충전: existing Jojo scenario route.
def scene0():
    if gvars['linchong.campaign_complete']:
        stage.setStageName('수호전 외전: 임충전 완료')
        stage.loadBg(3, 990)
        stage.ending(1)
        stage.setMenuVisible(True)
    else:
        stage.end()

def __dispatch__():
    scene0()
