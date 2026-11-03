-- Generated Linchong scenario. Edit the campaign generator to preserve changes.
local function __truth(value)
    return value ~= nil and value ~= false and value ~= 0 and value ~= ""
end

function scene0()
    if (gvars["linchong.campaign_complete"] ~= 0 and gvars["linchong.campaign_complete"] ~= nil and gvars["linchong.campaign_complete"] ~= false) then
        stage.setStageName("수호전 외전: 임충전 완료")
        stage.loadBg(3, 990)
        stage.ending(1)
        stage.setMenuVisible(true)
    else
        stage["end"]()
    end
end

function __dispatch__()
    scene0()
end
