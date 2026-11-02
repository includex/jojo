#!/usr/bin/env python3
"""Generate Lin Chong catalogs for the existing Jojo package and native table ABI.
No runtime or renderer code is generated. Only this game owns its catalogs.
"""
from pathlib import Path
import json
import re

ROOT=Path(__file__).resolve().parents[1]
SOURCE=ROOT/'content/examples/linchong'
ITEM_IDS={'healing_powder':200,'old_wrist_guard':137,'manual_circling_spear':202,'training_staff':59,'guardian_belt':141,'manual_guard_palm':205,'iron_shield':100,'manual_iron_guard':207,'manual_snow_resolve':208,'snow_boots':149,'burned_secret_letter':210,'marsh_token':256,'waterway_map':212,'marsh_compass':257,'manual_water_stride':214,'manual_tiger_charge':215,'leather_vest':101,'campaign_clear_token':217,'iron_spear':60,'prison_robe':102,'monk_staff':61,'monk_robe':103,'short_spear':62,'short_blade':63,'traveler_clothes':104,'green_steel_sword':64,'light_armor':105,'linen_robe':106}
SKILL_IDS=dict(zip(['wind_spear','circling_spear','staff_smash','iron_guard','quick_step','snow_resolve','water_stride','tiger_charge','swift_blade','staff_combo'],range(100,110)))

def read_yaml(path):
    # Catalog values use the project's scalar + inline JSON YAML convention.
    out={}
    for line in path.read_text().splitlines():
        if not line or line.startswith('#') or ': ' not in line:continue
        key,value=line.split(': ',1)
        try:out[key]=json.loads(value)
        except json.JSONDecodeError:out[key]=value
    return out

def write_yaml(category,row):
    path=ROOT/'content'/category/f'{row["id"]:04}.yaml'
    path.parent.mkdir(parents=True,exist_ok=True)
    path.write_text('# Lin Chong mod / native Jojo content catalog\n'+'\n'.join(f'{key}: {json.dumps(value,ensure_ascii=False,separators=(",",":"))}' for key,value in row.items())+'\n')

def main():
    campaign=json.loads((SOURCE/'campaign.json').read_text())
    ids=json.loads((SOURCE/'mod-ids.json').read_text())
    artwork=json.loads((SOURCE/'assets/jojo-asset-mapping.json').read_text())
    records={}
    for battle in campaign['battles']:
        for unit in battle['units']+battle['spawns']:records.setdefault(unit['id'],unit)
    cast={x['id']:x for x in campaign['characters']}
    primary=['lin','lu','hu','jin','zhu','hong','yang','wang','chai','song','dong','xue','luk','fu','guard','bandit']
    roles={'lin':42,'lu':18,'hu':3,'jin':48,'zhu':21,'hong':18,'yang':39,'wang':24,'chai':0,'song':21,'dong':42,'xue':42,'luk':39,'fu':48,'guard':42,'bandit':21}
    portraits={'wife':16,'gaoyanei':17,'gaoqiu':18,'gao_yanei':17,'gao_qiu':18,'servant':19,'escort':20,'pursue_boss':21,'smug_boss':22,'bandit_boss':23}
    speaker_names={'wife':'임충의 아내','gaoyanei':'고아내','gaoqiu':'고태위','gao_yanei':'고아내','gao_qiu':'고태위','servant':'하인'}
    stories={'lin':'금군 교두였으나 권력자의 모함을 받아 창주로 유배된 창의 명수.','lu':'불의를 보면 참지 못하는 호방한 승려. 야저림에서 임충의 목숨을 구한다.','hu':'창주에서 보급 수레를 돌보던 인부. 위기에 처한 사람을 지키고자 한다.','jin':'눈보라 속에서 임충에게 초료장의 위험을 알린 민첩한 청년.','zhu':'양산박 남쪽 수로의 주점을 운영하며 산채로 향하는 길을 안내한다.','hong':'시진 장원에서 임충과 봉술을 겨루는 무예 교두.','yang':'청면수라 불리는 검객. 양산박으로 향하는 산길에서 임충과 만난다.','wang':'양산박 산채를 이끄는 두령. 낯선 방문객의 입산을 경계한다.','chai':'의로운 이들을 후하게 대접하는 장원주.','song':'양산박의 물길과 산채를 지키는 호걸.','dong':'임충의 유배길을 호송하는 관원.','xue':'동초와 함께 임충의 호송을 맡은 관원.','luk':'한때 임충의 벗이었으나 권세가의 수하가 된 인물.','fu':'권세가의 명을 받아 임충을 뒤쫓는 수하.'}
    written=[]
    seen=set()
    all_ids={**ids['unitIds'],**ids.get('objectIds',{}),'narrator':575}
    for key,unit_id in all_ids.items():
        if unit_id in seen:continue
        seen.add(unit_id)
        unit=records.get(key,{})
        if key in primary:archetype=key
        elif key=='duqian':archetype='song'
        elif key.startswith(('bandit','raid','smug')):archetype='bandit'
        else:archetype='guard'
        if key in ('wife','gaoyanei','gaoqiu','gao_yanei','gao_qiu','servant'):
            slot=portraits[key];avatar=615;posts=73
        elif key in ids.get('objectIds',{}):
            a=artwork['objects'][key];slot=14;avatar=a['avatarId'];posts=73
        else:
            slot=primary.index(archetype);avatar=601+slot;posts=roles[archetype]
            if unit.get('ai')=='ranged' or '궁' in unit.get('role',''):posts=6
            if key in portraits:slot=portraits[key]
        if key in ('knife1','knife2'):avatar=621
        name=unit.get('name',cast.get(key,{}).get('name',speaker_names.get(key,key)))
        max_hp=unit.get('maxHp',unit.get('hp',100))
        if key in ids.get('objectIds',{}):
            obj=next(o for b in campaign['battles'] for o in b['objects'] if o['id']==key)
            name={'cart_a':'보급 수레 갑','cart_b':'보급 수레 을','supply':'장원 보급 상자','cargo1':'장물 상자 갑','cargo2':'장물 상자 을'}[key]
            max_hp=obj['hp']
        row={'id':unit_id,'name':name,'face':600+slot,'mapAvatar':avatar,'speechGroup':0,'attack':unit.get('attack',24),'defense':unit.get('defense',14),'spirit':unit.get('attack',24),'critical':unit.get('agility',15),'morale':unit.get('agility',15),'maxHitPoints':max_hp,'maxMagicPoints':unit.get('mp',20),'posts':posts,'level':unit.get('level',1),'intro':stories.get(key,'임충이 유배길에서 만나는 인물. ' + unit.get('role','')),'battleAvatar':avatar-304,'battleAvatarType':-1,'famous':1 if key in primary else 0,'retreat':'다시 만날 날을 기다리겠소.'}
        if key=='narrator':row.update(name='이야기',face=624,posts=73,attack=0,defense=0,spirit=0,critical=0,morale=0,maxHitPoints=1,maxMagicPoints=0,intro='',retreat='')
        if key in ids.get('objectIds',{}):row.update(attack=0,spirit=0,critical=0,morale=0,defense=0,maxMagicPoints=0,famous=0,retreat='',intro='전장에 놓인 보급 물자다. 파괴되지 않도록 지켜야 한다.')
        write_yaml('characters',row);written.append(str(unit_id))
    items={i['id']:i for i in campaign['items']}
    loadout_names={'iron_spear':'철창','prison_robe':'유배수 옷','monk_staff':'선장','monk_robe':'승복','short_spear':'단창','short_blade':'단도','traveler_clothes':'여행자 옷','green_steel_sword':'청강검','light_armor':'경갑','linen_robe':'삼베옷'}
    weapons={'training_staff','iron_spear','monk_staff','short_spear','short_blade','green_steel_sword'}
    armors={'iron_shield','leather_vest','prison_robe','monk_robe','traveler_clothes','light_armor','linen_robe'}
    treasures={'old_wrist_guard','guardian_belt','snow_boots','marsh_token','marsh_compass'}
    for key,item_id in ITEM_IDS.items():
        item=items.get(key,{});name=item.get('name',loadout_names.get(key,key));description='보조 장비. 장비 목록에서 장착할 수 있다.' if key in treasures else '초기 장비. 별도의 추가 능력치는 없다.'
        if key=='healing_powder':row={'id':item_id,'name':name,'itemType':26,'specialType':3,'price':1,'iconIndex':87,'value':35,'upgradeValue':0,'intro':'HP를 35 회복한다. 사용하면 행동을 마친다.'}
        elif key in weapons:row={'id':item_id,'name':name,'itemType':6,'specialType':0,'price':1,'iconIndex':10,'value':3 if key=='training_staff' else 0,'upgradeValue':0,'intro':'공격력 +3.' if key=='training_staff' else '초기 무기. 추가 공격력은 없다.'}
        elif key in armors:row={'id':item_id,'name':name,'itemType':20,'specialType':0,'price':1,'iconIndex':38,'value':5 if key=='iron_shield' else 3 if key=='leather_vest' else 0,'upgradeValue':0,'intro':'방어력 +5.' if key=='iron_shield' else '방어력 +3.' if key=='leather_vest' else '초기 방어구. 추가 방어력은 없다.'}
        elif key in treasures:row={'id':item_id,'name':name,'itemType':61,'specialType':2,'price':0,'iconIndex':67,'value':2 if key=='old_wrist_guard' else 1,'effectValue':1,'upgradeValue':255,'treasure':1,'intro':description}
        else:row={'id':item_id,'name':name,'itemType':40,'specialType':3,'price':0,'iconIndex':43,'value':0,'upgradeValue':0,'intro':'무공을 익힌 일을 기록하는 비급이다. 전장 사건으로 기술이 개방되며, 이 물품을 직접 사용하지 않는다.' if key.startswith('manual_') else '이야기 속에서 얻은 수집용 증표다. 직접 사용하거나 장착할 수 없다.'}
        write_yaml('equipment',row)
    # Keep each skill inside an existing, fully implemented native effect family.
    # PRD physical martial damage is adapted to native strategic damage; buffs use native statuses.
    skill_records={s['id']:s for s in campaign['skills']}
    templates={'wind_spear':'damage','circling_spear':'damage','staff_smash':'damage','iron_guard':'defense','quick_step':'agility','snow_resolve':'recovery','water_stride':'agility','tiger_charge':'damage','swift_blade':'damage','staff_combo':'damage'}
    technique_templates=json.loads((SOURCE/'technique-templates.json').read_text())
    for key,skill_id in SKILL_IDS.items():
        item=skill_records.get(key,{})
        base=dict(technique_templates[templates[key]])
        params=campaign.get('mechanics',{}).get('skillParameters',{}).get(key,campaign.get('mechanics',{}).get('authoredParameters',{}).get('skills',{}).get(key,{}))
        base.update(id=skill_id,name=item.get('name',params.get('name',key)),mpCost=params.get('mpCost',0),intro=({'iron_guard':'선택한 아군의 방어력을 일시적으로 높인다. 사용하면 행동을 마친다.','quick_step':'선택한 아군의 순발력을 일시적으로 높인다. 사용하면 행동을 마친다.','water_stride':'선택한 아군의 순발력을 일시적으로 높인다. 사용하면 행동을 마친다.','snow_resolve':'선택한 아군의 독·마비·혼란을 해제한다. 사용하면 행동을 마친다.'}.get(key,'인접한 적을 중심으로 십자 범위에 전술 피해를 준다.' if key in ('circling_spear','staff_smash') else '직선 1~2칸의 적 하나에게 전술 피해를 준다.' if key=='wind_spear' else '3칸 이내의 적 하나에게 강한 전술 피해를 준다.' if key=='tiger_charge' else '인접한 적 하나에게 전술 피해를 준다.')+f' 기력 {params.get("mpCost",0)} 소비.'),learnLevels={})
        if key in ('wind_spear','tiger_charge','swift_blade','staff_combo'):base.update(hitArea=5 if key=='wind_spear' else 12 if key=='tiger_charge' else 0,effectArea=0,power=70 if key=='wind_spear' else 95 if key=='tiger_charge' else 80)
        if key in ('circling_spear','staff_smash'):base.update(hitArea=0,effectArea=1,power=60)
        base['condition']=6
        write_yaml('skills/active',base)
    for battle in campaign['battles']:
        index=ids['battles'][battle['id']]['mapIndex']
        write_yaml('shop',{'id':index,'name':battle['name'],'weaponLevel':0,'armorLevel':0,'items':[255]*16})
    package=ROOT/'content/game.yaml'
    settings={'id':'linchong','title':'수호전 외전: 임충전','startModule':'R_90','startScene':'scene0','resumeScene':'scene0','protagonistId':512,'saveNamespace':'linchong-native-jojo-v1','defaultBattleModule':'S_90'}
    package.write_text('# Lin Chong standalone game package.\n'+'\n'.join(f'{k}: {json.dumps(v,ensure_ascii=False)}' for k,v in settings.items())+'\nbattleModules:\n'+''.join(f'  R_{i}: S_{i}\n' for i in range(90,98)))
    report={'itemIds':ITEM_IDS,'skillIds':SKILL_IDS,'characterIds':sorted(int(x) for x in written),'nativeAdapters':{'damage':'Native type38 terrain-independent strategic damage; native hit/effect area and animations. No replacement combat formula.','support':'Native defense/critical/status-restoration effects; automatic PRD mastery and mind slots are not implemented by catalog rows alone.','equipment':'Native one weapon/one armor/one auxiliary format; spare items below150 or appended256+; properties200..217.'}}
    (SOURCE/'native-content-mapping.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n')
    print(f'Generated {len(written)} native characters, {len(ITEM_IDS)} items, {len(SKILL_IDS)} active techniques, 8 battle names, and package routing.')

if __name__=='__main__':main()
