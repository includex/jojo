# Source Generated with Decompyle++
# File: common.marshal (Python 3.9)


def registerCheck():
    app_id = manager.getMeta('app_id')
    devId = manager.m_deviceId
    xxtea = manager.getMeta('xxtea')
    txt = f'''{devId}-{xxtea}-{app_id}'''
    md5_hex = manager.md5_str(txt)
    url = f'''https://cczregister.bj.bcebos.com/public/{app_id}/{md5_hex}.json'''
    manager.downloadFile(url)


def register(data):
    xxtea = manager.getMeta('xxtea')
    data = manager.base64(data)
    data = manager.xorEncrypt(data, xxtea)
    data = manager.praseJson(data)
    b = 'md5' in data
    if not b:
        print('실패 1')
        return None
    b = 'rFlag' in data
    if not b:
        print('실패 2')
        return None
    app_id = manager.getMeta('app_id')
    md5_1 = manager.md5_str(f'''{manager.m_deviceId}-{manager.getMeta('xxtea')}-{app_id}-{data.rFlag}''')
    if md5_1 != data['md5']:
        print('데스크톱에서는 원격 등록 불일치를 무시합니다.')
        return None
    rFlag1 = manager.rFlag()
    rFlag2 = data.rFlag
    if rFlag1 == rFlag2:
        return None
    manager.setRFlag(rFlag2)
    if ud.getIntegerForKey('rFlag', 0) != rFlag2:
        ud.setIntegerForKey('rFlag', rFlag2)
        manager.msgbox('특수 기능이 활성화되었습니다. 특수 기능 메뉴에서 켜야 적용됩니다. 문제가 있으면 제보해 주세요. 성원에 감사드립니다(^_^)')


def gold(gold):
    model.addMoney(gold)
    manager.msgbox(f'''축하합니다. {gold}금화를 획득했습니다. 지원해 주셔서 감사합니다(^_^)''')


def pushItem(idx):
    itemStore.pushItem(idx, 0)
    manager.msgbox('아이템을 획득했습니다. 게임을 저장해 주세요!')


def unitJoin(index):
    model.unitJoin(index, 0, 0)
    manager.msgbox('무장을 소환했습니다. 게임을 저장해 주세요!')


def buy(index):
    rFlag1 = manager.rFlag()
    rFlag2 = rFlag1 | 1 << index
    if rFlag1 == rFlag2:
        return None
    manager.setRFlag(rFlag2)
    ud.setIntegerForKey('rFlag', rFlag2)
    manager.msgbox('특수 기능이 활성화되었습니다. 특수 기능 메뉴에서 켜야 적용됩니다. 문제가 있으면 제보해 주세요. 성원에 감사드립니다(^_^)')

def __dispatch__():
    pass
