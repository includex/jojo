# -*- coding: utf-8 -*-
"""Compile authored PRD into the existing Jojo Python-AST scenario contract."""
import json,re,pathlib
from scenario_python_to_lua import convert as convert_lua

def write_scenario(path, source):
 # Python is retained temporarily as a parity fixture during the Lua migration.
 path.write_text(source)
 path.with_suffix(".lua").write_text(convert_lua(source))

ROOT=pathlib.Path(__file__).resolve().parents[1];D=ROOT/'content';c=json.loads((D/'examples/linchong/campaign.json').read_text());ids=json.loads((D/'examples/linchong/mod-ids.json').read_text());U=ids['unitIds'];O=ids['objectIds'];entries={};globals=set();adapt=[]
def g(k):k=k.lower();globals.add(k);return 'gvars['+repr('linchong.'+k)+']'
def F(k):return g(k)
def unit(k):return U.get(k,O.get(k))
def dead(k):return f'stage.unitStateTest({unit(k)}, 7, 0, 2)'
def nearobj(b,k,who='lin'):
 p=next(o for o in b['objects'] if o['id']==k)['positions'][0];x,y=p['x']-1,p['y']-1
 return f'stage.isInRect({unit(who)}, {max(0,x-1)}, {max(0,y-1)}, {x+1}, {y+1})'
def whenexpr(w):
 if 'all' in w:return '('+' and '.join(whenexpr(x) for x in w['all'])+')'
 if 'any' in w:return '('+' or '.join(whenexpr(x) for x in w['any'])+')'
 if 'not' in w:return '(not '+whenexpr(w['not'])+')'
 if w.get('type')=='flag':return F(w['id'])
 if w.get('type')=='recruited':return F('RECRUITED_'+w['id'])
 if w.get('type')=='allyPresent':
  uid=w['id']
  if uid in ['hu','jin']:return '('+F('RECRUITED_'+uid)+' and not '+dead(uid)+')'
  return F('EX02_SONG_SUPPORT') if uid=='song' else F('B06_SONG_SUPPORT')
 return 'True'
# Preserve the PRD verbatim in source.md/campaign.json; in-game instructions
# describe the native implementation rather than promising PRD-only mechanics.
NATIVE_HELP = {
 'S_90.B01_E02.line006': '은혜 따질 때가 아니다! 고급 금창약 네 개를 가져왔으니, 내 곁에서 교환으로 받아 상처부터 돌보게.',
 'S_90.B01_E01.line001': '자객에게 포위되었다. 임충을 살려 두고 구원의 손길을 기다리자.',
 'S_90.B01_E03.line002': '무공은 기존 전투 메뉴에서 사용할 수 있다. 필요한 기력을 확인하고 공격과 회복을 나누어 사용하자.',
 'S_91.B02_E01.line001': '홍교두의 체력을 충분히 낮추면 대련이 끝난다. 가까이 다가가면 일기토 장면이 발생한다. 임충의 체력도 주의하자.',
 'S_93.B03_E02.line003': '허정 가까이 다가가면 구출 대화가 시작된다.',
 'S_93.B03_E08.line002': '가죽갑옷을 얻었다. 장비 목록에서 확인할 수 있다.',
 'S_94.B04_E10.line002': '설행화를 얻었다. 보조 장비 목록에서 확인할 수 있다.',
 'S_97.B06_E01.line001': '양지는 임충을 노린다. 동료들은 우군으로 지원한다. 양지 가까이 다가가면 일기토 장면이 발생한다.',
 'S_97.B06_E90.line006': '임충전의 마지막 전투를 마쳤다. 이어지는 대화를 보고 완료 메뉴에서 진행 기록을 저장할 수 있다.',
 'S_97.epilogue.line004': '제1부 임충전 완료. 함께한 동료와 장비는 이 게임의 진행 기록에 남는다.',
}

def say(lines,key,indent='    '):
 out=[]
 for i,l in enumerate(lines):
  sid=f'{key}.line{i+1:03d}';speaker=l['speaker'];num={'아내':571,'고아내':572,'고태위':573,'하인':574,'관영 호위병':539,'호송관':526,'산적두목':540,'도적두목':533,'추격 대장':552,'밀수선 두목':559,'내레이션':575,'시스템':575}.get(speaker) or next((U[x['id']] for x in c['characters'] if x['name'].split('(')[0]==speaker and x['id'] in U),None)
  text=f'&{num or 575}\n'+(speaker+'\n' if num is None else '')+NATIVE_HELP.get(sid,l['text']);entries[sid]=text
  line=f"stage.say('@dialogue:{sid}')"
  if l.get('when'):
   out += [indent+'if '+whenexpr(l['when'])+':',indent+'    '+line];continue
  if speaker in ['허정','진오']:
   char='hu' if speaker=='허정' else 'jin'
   # Captives speak while present, later chapters require permanent recruitment.
   allowed=(char=='hu' and bcontext=='B03') or (char=='jin' and bcontext=='B04')
   if not allowed:out += [indent+'if '+F('RECRUITED_'+char)+':',indent+'    '+line];continue
  if speaker=='송만':out += [indent+'if '+F('EX02_SONG_SUPPORT')+' or '+F('B06_SONG_SUPPORT')+':',indent+'    '+line];continue
  out.append(indent+line)
 return out

def gear(uid,indent='    '):
 l=next((l for l in c['startingLoadouts'] if l['unitId']==uid),None)
 if not l:return []
 flag=F('gear_initialized_'+uid)
 return [indent+'if not '+flag+':',indent+f"    stage.setJoinEquip({unit(uid)}, {ids['itemIds'][l['weapon']]+2}, 1, {ids['itemIds'][l['armor']]-68}, 1, 0)",indent+'    '+flag+' = 1']

def placement(b,u,i):
 d=dict(id=unit(u['id']),i=i,idx=i,x=u['x']-1,y=u['y']-1,lv=max(0,u['level']-1),dir=2,ai=1 if u['side']=='ENEMY' else 2)
 if u['ai'] in ('idle','captive','flee'):d['ai']=2
 if u['ai']=='guard_cargo':d['ai']=0
 if u['ai'] in ['assassin','duelist']:d['ai']=3;d['targetId']=512
 if u['id']=='lin' or (u['id'] in ('hu','jin') and u['side']=='ALLY'):d['lv']=0
 if 'HIDDEN' in u.get('notes',''):d['hide']=1
 if u['ai'] in ['rush_cart','rush_object']:d['ai']=3;d['targetId']=O.get('supply' if bcontext=='EX01' else 'cart_a',512)
 override=b['runtime'].get('initialPlacement',{}).get(u['id'],{})
 for axis in ('x','y'):
  if axis in override:d[axis]=override[axis]-1
 if 'avatarId' in override:d['avatarId']=override['avatarId']
 if 'facing' in override:d['dir']={'north':0,'east':1,'south':2,'west':3}[override['facing']]
 return d

def condition(b,tr):
 if ' OR ' in tr:return '('+' or '.join(condition(b,p) for p in tr.split(' OR '))+')'
 if ' AND ' in tr:return '('+' and '.join(condition(b,p) for p in tr.split(' AND '))+')'
 if ' AFTER ' in tr:
  a,event=tr.split(' AFTER ');return '('+condition(b,a)+' and '+F('event_'+event)+')'
 if tr.startswith('NOT '):return '(not '+condition(b,tr[4:])+')'
 a=tr.split(':');cmd=a[0]
 if cmd=='FLAG':return F(a[1])
 if cmd=='BATTLE_START':return 'True'
 if cmd.startswith('PLAYER_TURN_'):
  return f'(stage.round() >= {a[1]} and stage.curCamp() == '+('0' if cmd.endswith('START') else '2')+')'
 if cmd=='UNIT_DEFEATED':return dead(a[1])
 if cmd=='UNIT_HP_BELOW':
  u=next(u for u in b['units']+b['spawns'] if u['id']==a[1]);return f'stage.unitStateTest({unit(a[1])}, 7, {int(u["maxHp"]*int(a[2][:-1])/100)+1}, 1)'
 if cmd=='OBJECT_HP_BELOW':return f'stage.unitStateTest({unit(a[1])}, 7, 81, 1)'
 if cmd=='ON_OBJECT_DESTROYED':return dead(a[1])
 if cmd in ('INTERACT','DUEL_REQUEST'):
  who,target=a[1:3]
  if target in U:near=f'stage.isNear({unit(who)}, {unit(target)}, True)'
  else:near=nearobj(b,target,who)
  return near
 if cmd=='DIST<=1':return 'True'
 if cmd=='TURN>=3':return 'stage.round() >= 3'
 if cmd=='ON_UNIT_ENTER':
  return onexit(b,a[1],a[2])
 if cmd=='ON_ALLY_EXIT_COUNT':return F('exit_count')+' >= '+a[1]
 if cmd=='ON_ENTER_ZONE':return 'stage.isInRect(512, 10, 4, 15, 7)'
 if cmd=='OBJECT_INTERACT':return nearobj(b,a[1])
 if cmd=='ON_FIRE_ENTER':return 'stage.round() >= 5'
 if cmd=='FIRST_EFFECTIVE_HIT':return 'stage.round() >= 2'
 if cmd=='WIN_CONDITIONS_MET':return 'win_condition()'
 if cmd=='LOSE_CONDITIONS_MET':return 'lose_condition()'
 adapt.append(b['id']+': trigger '+tr);return 'False'
def onexit(b,who,obj):
 ps=next(o['positions'] for o in b['objects'] if o['id']==obj)
 return '('+' or '.join(f'stage.isInPos({unit(who)}, {p["x"]-1}, {p["y"]-1})' for p in ps)+')'

# Presentation only: authored positions remain the source for both exit checks
# and markers. Emit individual cells so irregular regions never include gaps.
OBJECTIVE_GUIDES = {
 'B01': ('노지심의 구원을 기다린 뒤 임충을 동쪽 탈출 지점으로 이동시키십시오.', ['exit']),
 'EX01': ('이 창고 상자를 지키며 도적 두목을 격퇴하십시오.', ['supply']),
 'B03': ('수레 두 대 중 적어도 한 대를 지키며 산적두목을 격퇴하십시오.', ['cart_a', 'cart_b']),
 'B05': ('임충을 포함한 아군 두 명을 선착장으로 이동시키고 주귀를 보호하십시오.', ['dock']),
 'EX02': ('임충을 상자 가까이 이동시켜 하나 이상 확보하고 밀수선 두목을 격퇴하십시오.', ['cargo1', 'cargo2']),
}
EVENT_GUIDES = {
 'B01_E02': ('탈출할 수 있습니다. 임충을 동쪽 탈출 지점으로 이동시키십시오.', ['exit']),
 'B03_E02': ('허정 가까이 다가가 구출하십시오.', ['hostage']),
 'B04_E03': ('진오를 구출할 위치와 증거를 조사할 위치를 확인하십시오.', ['jin_trap', 'charred_letter']),
}
def guide(b, object_ids, text=None, indent='    '):
 out=[indent+f'stage.showWinCondition({text!r})'] if text else []
 for key in object_ids:
  ps=next(o['positions'] for o in b['objects'] if o['id']==key)
  xs=[p['x']-1 for p in ps];ys=[p['y']-1 for p in ps]
  if len(set(zip(xs,ys))) == (max(xs)-min(xs)+1)*(max(ys)-min(ys)+1):
   rects=[(min(xs),min(ys),max(xs),max(ys))]
  else:rects=[(x,y,x,y) for x,y in zip(xs,ys)]
  out += [indent+f'stage.heightLight({x1}, {y1}, {x2}, {y2})' for x1,y1,x2,y2 in rects]
 return out

def actions(b,acts,indent='        '):
 out=[indent+'pass']
 for a in acts:
  cmd=a['command'];v=a['args'];raw=a['raw'];x=v[0] if v else ''
  if cmd in ['if_flag','if_not_flag']:
   out.append(indent+'if '+('not ' if cmd=='if_not_flag' else '')+F(x)+':');sub=':'.join(v[1:]);out+=actions(b,[{'command':sub.split(':')[0],'args':sub.split(':')[1:],'raw':sub}],indent+'    ');continue
  if cmd=='set_flag':out.append(indent+F(x)+' = 1')
  elif cmd=='spawn':
   u=next(u for u in b['spawns'] if u['id']==x);method='createMine' if u['side']=='ALLY' else 'createEnemy';i=next(j for j,z in enumerate(b['spawns']) if z['id']==x)+len(b['units'])
   out+=gear(x,indent)
   out.append(indent+f'stage.{method}([{placement(b,u,i)!r}])')
   loadout=next((l for l in c['startingLoadouts'] if l['unitId']==x),None)
   if loadout:
    payload=str(ids['skillIds'][loadout['skills']])+'\n1\n'+str(unit(x))+'\n'+loadout['skills'];out.append(indent+f'stage.infoTransfer(4, {payload!r})')
  elif cmd in ['unlock_skill','grant_skill','unlock_mind']:
   sid=ids['skillIds'].get(v[1]);payload=str(sid)+'\n1\n'+str(unit(x))+'\n'+v[1]
   out.append(indent+f'stage.infoTransfer(4, {payload!r})')
  elif cmd=='heal':out.append(indent+f"stage.setUnitStatus([{{'unit':stage.unit({unit(x)}),'hp':{v[1]}}}])")
  elif cmd=='set_side' and v[1]=='ALLY':
   u=next(u for u in b['units'] if u['id']==x);out.append(indent+f'stage.unit({unit(x)}).hide(1)');out.append(indent+f'stage.createMine([{placement(b,u,2 if x=="hu" else 3 if x=="jin" else 4)!r}])')
  elif cmd=='recruit_permanent':out += [indent+F('RECRUITED_'+x)+' = 1',indent+f'model.unitJoin({unit(x)}, 0, 0)']
  elif cmd=='disable_ai':out.append(indent+f'stage.unit({unit(x)}).setAI(2)')
  elif cmd=='enable_ai':out += [indent+f'stage.unit({unit(x)}).show()',indent+f'stage.unit({unit(x)}).setAI(1, 512)']
  elif cmd in ['ignite','advance_fire']:
   pts=b['runtime']['firePhases'].get(x,[])
   if pts:out.append(indent+f'stage.setFires(True, {[[p["x"]-1,p["y"]-1] for p in pts]!r})')
  elif cmd=='play_duel':
   out += [indent+f'fight = stage.startFight({unit(x)}, {unit(v[1])}, 5)',indent+'fight.showStart()',indent+'fight.attack1(True, 1, False)',indent+'fight.attack1(False, 1, False)',indent+'fight.end()',indent+F('DUEL_WIN')+' = 1']
  elif cmd in ['end_if_duel_win','if_duel_win']:
   if cmd=='if_duel_win':out+=actions(b,[{'command':v[0],'args':v[1:],'raw':':'.join(v)}],indent)
  elif cmd=='present_choice_if_flag':
   out += [indent+'if '+F(x)+':',indent+"    sel = stage.choice('함께 가시오\\n스스로 길을 찾으시오', -1)",indent+'    if sel == 1:',indent+'        '+F('RECRUITED_hu')+' = 1',indent+'        model.unitJoin(514, 0, 0)',indent+'    else:',indent+'        '+F('HU_FRIEND_ONLY')+' = 1']
  elif cmd=='force_nonlethal_stagger':out.append(indent+f"stage.setUnitStatus([{{'unit':stage.unit({unit(x)}),'hp':-9999}}])")
  elif cmd=='capture_cargo':out.append(indent+F('EX02_ONE_CARGO')+' = 1')
  elif cmd=='mark_exit':out.append(indent+F('B05_LIN_DOCK')+' = 1')
  elif cmd=='grant_item':
   item=ids.get('itemIds',{}).get(x)
   if item is not None:
    for n in range(int(v[1])):out.append(indent+f'stage.getItem({item}, 0, True, 512)')
   else:out.append(indent+'# reward '+raw)
  else:out.append(indent+'# Existing-engine adaptation: '+raw)
 return out

for bi,b in enumerate(c['battles']):
 bcontext=b['id'];num=90+bi;mod=f'S_{num}';flags=[]
 # Preserve the displayed limit, and use it inclusively for both text and failure.
 turn_limit=b['map']['turnLimit']+1
 # Original Jojo progression/presentation, no parallel runtime.
 r=['# 임충전: existing Jojo scenario route.','def scene0():']
 if bi==0:r += ['    model.unitJoin(512, 0, 0)','    '+F('started')+' = 1']+gear('lin')
 if bi==7:r += ['    if '+F('CAMPAIGN_COMPLETE')+':',"        stage.setStageName('수호전 외전: 임충전 완료')",'        stage.loadBg(3, 990)','        stage.ending(1)','        stage.setMenuVisible(True)','    else:','        stage.end()']
 if bi!=7:r+=['    stage.end()']
 r += ['','def __dispatch__():','    scene0()']
 write_scenario(D/f'scenarios/R_{num}.py', '\n'.join(r)+'\n')
 init=['# 임충전 — generated from source PRD, original Jojo BattleScreen APIs.','def scene0():','    stage.initFight()',f'    stage.setGlobalData({turn_limit}, 0, -1, 512, 1, 1)']
 for u in b['units']:init+=gear(u['id'])
 for side,method in [('ALLY','createMine'),('ENEMY','createEnemy'),('NEUTRAL','createFriend')]:
  for i,u in enumerate(b['units']):
   if u['side']!=side:continue
   pl=placement(b,u,i)
   actualmethod='createFriend' if bcontext=='B06' and u['id'] in ['hu','jin'] else method
   if u.get('condition'):init += ['    if '+F('RECRUITED_'+u['id'])+':','        stage.'+actualmethod+'(['+repr(pl)+'])']
   else:init.append('    stage.'+actualmethod+'(['+repr(pl)+'])')
 if b['id'] in ['B01','B05','EX02']:
  sentinel=b['runtime']['blocked'][0]
  init.append(f"    stage.createEnemy([{{'id':527,'i':31,'x':{sentinel['x']-1},'y':{sentinel['y']-1},'hide':1,'ai':2,'lv':0}}])")
 for o in b['objects']:
  if o['id'] in O:
   p=o['positions'][0];pl={'id':O[o['id']],'i':len(b['units'])+list(O).index(o['id']),'x':p['x']-1,'y':p['y']-1,'ai':2,'lv':0};init.append('    stage.createFriend(['+repr(pl)+'])')
 defeat=re.sub(r'\d+턴 시작(?: 전)?', f'{turn_limit}턴 종료 시', b['defeat'])
 defeat=re.sub(r'\d+턴', f'{turn_limit}턴', defeat)
 wintext='승리 조건\n'+b['victory']+'\n\n패배 조건\n'+defeat
 for loadout in c['startingLoadouts']:
  if any(u['id']==loadout['unitId'] for u in b['units']):
   sid=ids['skillIds'].get(loadout['skills']);payload=str(sid)+'\n1\n'+str(unit(loadout['unitId']))+'\n'+loadout['skills']
   init.append(f'    stage.infoTransfer(4, {payload!r})')
 init += [f'    stage.loadBg({58+bi})','    stage.draw()',f'    stage.setWinCondition({wintext!r})']
 if bi==0:init+=say(c['interludes']['PROLOGUE'],mod+'.prologue')
 interlude=b['runtime'].get('interludeBefore') or {'B02':'B01_TO_B02','B04':'B03_TO_B04','B05':'B04_TO_B05'}.get(b['id'])
 if interlude:init+=say(c['interludes'][interlude],mod+'.interlude')
 for e in b['events']:
  if e['trigger']=='BATTLE_START':
   init+=say(e['dialogue'],mod+'.'+e['id'])
   for variant in b['conditionalDialogue']:
    if variant['eventId']==e['id']:
     init+=['    if '+whenexpr(variant['when'])+':'];init+=say(variant['dialogue'],mod+'.'+e['id']+'.'+variant['condition'],'        ')
   init+=actions(b,e['actions'],'    ');init+=['    '+F('event_'+e['id'])+' = 1']
 init.append('    '+F('DUEL_WIN')+' = 0')
 init.append('    '+F('exit_count')+' = 0')
 for u in b['units']:
  if u['hp']<u['maxHp']:init.append(f"    stage.setUnitStatus([{{'unit':stage.unit({unit(u['id'])}),'hp':{u['hp']-u['maxHp']}}}])")
 if b['id'] in OBJECTIVE_GUIDES:
  text,targets=OBJECTIVE_GUIDES[b['id']]
  init+=guide(b,targets,text)
  hero=placement(b,next(u for u in b['units'] if u['id']=='lin'),0)
  init.append(f'    stage.center({hero["x"]}, {hero["y"]})')
 init+=['    stage.startOper()','    return False','']
 code=init+['def scene1():']
 # Losing the hero always takes priority over chapter victory.
 code+=['    if lose_condition():']
 for e in b['events']:
  if e['trigger']=='LOSE_CONDITIONS_MET':code+=say(e['dialogue'],mod+'.'+e['id'],'        ')
 code+=['        stage.end()','        stage.lose()','        return True']
 if b['id']=='B05':
  code += ['    '+F('exit_count')+' = 0']
  for u in b['units']:
   if u['side']=='ALLY' or u['id']=='zhu':code += ['    if '+onexit(b,u['id'],'dock')+' and not '+dead(u['id'])+':','        '+F('exit_count')+' += 1']
 for e in sorted(b['events'],key=lambda e:e['priority']):
  if e['trigger'] in ['BATTLE_START','LOSE_CONDITIONS_MET','WIN_CONDITIONS_MET']:continue
  flag=F('event_'+e['id']);cond=condition(b,e['trigger'])
  if e['id']=='B04_E04':cond='('+cond+' and stage.round() >= 3)'
  code+=['    if not '+flag+' and '+cond+':','        '+flag+' = 1']
  event_actions=e['actions']
  dialogue=say(e['dialogue'],mod+'.'+e['id'],'        ')
  if e['id']=='B01_E02':
   spawn=[a for a in event_actions if a['command']=='spawn' and a['args'][0]=='lu']
   event_actions=[a for a in event_actions if a not in spawn]
   code+=dialogue[:1]+actions(b,spawn)
   code+=['        stage.giveUnitItem(513, 216, 4)', '        stage.center(1, 7)']
   code+=dialogue[1:2]
   code+=['        stage.unit(513).setAction(6, 1, False)',
          '        stage.unit(513).setAction(7, 1, False)',
          '        stage.unit(513).setAction(6, 1, False)',
          '        stage.unit(513).setAction(0, 1, False)']
   code+=dialogue[2:]
  else:code+=dialogue
  for variant in b['conditionalDialogue']:
   if variant['eventId']==e['id']:
    code+=['        if '+whenexpr(variant['when'])+':'];code+=say(variant['dialogue'],mod+'.'+e['id']+'.'+variant['condition'],'            ')
  code+=actions(b,event_actions)
  if e['id'] in EVENT_GUIDES:
   text,targets=EVENT_GUIDES[e['id']]
   code+=guide(b,targets,text,'        ')
 code+=['    if win_condition():']
 for e in b['events']:
  if e['trigger']=='WIN_CONDITIONS_MET':
   code+=say([x for x in e['dialogue'] if x.get('timing')!='afterChoice'],mod+'.'+e['id'],'        ')
   for variant in b['conditionalDialogue']:
    if variant['eventId']==e['id'] and variant.get('timing')!='afterChoice':
     code+=['        if '+whenexpr(variant['when'])+':'];code+=say(variant['dialogue'],mod+'.'+e['id']+'.'+variant['condition'],'            ')
   code+=actions(b,e['actions'])
   code+=say([x for x in e['dialogue'] if x.get('timing')=='afterChoice'],mod+'.'+e['id']+'.afterChoice','        ')
   for variant in b['conditionalDialogue']:
    if variant['eventId']==e['id'] and variant.get('timing')=='afterChoice':
     code+=['        if '+whenexpr(variant['when'])+':'];code+=say(variant['dialogue'],mod+'.'+e['id']+'.'+variant['condition'],'            ')
 code += ['        vars[0] = 1','        vars[600] = 1','        stage.reward()']
 if b['id']=='B02':code += ["        sel = stage.choice('장원 야습에 맞선다\\n창주로 향한다', -1)",'        if sel == 2:','            stage.end()','            stage.jumpScene(185)']
 if b['id']=='B05':code += ['        if '+F('B05_HIDDEN_WATERWAY')+':',"            sel = stage.choice('밀수선을 조사한다\\n양산박으로 향한다', -1)",'            if sel == 2:','                stage.end()','                stage.jumpScene(193)','        else:','            stage.end()','            stage.jumpScene(193)']
 if b['id']=='B06':code+=say(c['interludes']['EPILOGUE'],mod+'.epilogue','        ')
 code+=['        stage.end()','        return True']
 code+=['    return False','']
 bid=b['id'];alive='not '+dead('lin')
 wins={
 'B01':f'({onexit(b,"lin","exit")} and {F("B01_RESCUED")})' if bid=='B01' else '',
 'B02':f'({F("DUEL_WIN")} or stage.unitStateTest(517, 7, 43, 1))',
 'EX01':f'({dead("raid_boss")} and not {dead("supply")})',
 'B03':f'({dead("bandit_boss")} and (not {dead("cart_a")} or not {dead("cart_b")}))',
 'B04':dead('luk'),
 'B05':f'({F("exit_count")} >= 2 and {F("B05_LIN_DOCK")} and not {dead("zhu")})',
 'EX02':f'({dead("smug_boss")} and {F("EX02_ONE_CARGO")})',
 'B06':f'({F("DUEL_WIN")} or stage.unitStateTest(518, 7, 64, 1) or stage.round() >= 7)'}
 lose=dead('lin')+f' or stage.round() > {turn_limit}'
 if bid=='EX01':lose+=' or '+dead('supply')
 if bid=='B03':lose+=' or ('+dead('cart_a')+' and '+dead('cart_b')+')'
 if bid=='B05':lose+=' or '+dead('zhu')
 if bid=='EX02':lose+=' or ('+dead('cargo1')+' and '+dead('cargo2')+')'
 code+=['def win_condition():','    return '+alive+' and '+wins[bid],'','def lose_condition():','    return '+lose,'','def scene2():','    return False','','def __dispatch__():',"    label('__dispatch_loop')",'    idx = stage.sceneIndex()','    if idx == 0:','        scene0()','        stage.incSceneIdx()','        return None','    if idx == 1:','        if scene1():','            stage.incSceneIdx()','        return None','    return None']
 source='\n'.join(code)+'\n'
 source=re.sub(r'(?<!def )win_condition\(\)', '('+alive+' and '+wins[bid]+')', source)
 source=re.sub(r'(?<!def )lose_condition\(\)', '('+lose+')', source)
 write_scenario(D/f'scenarios/{mod}.py', source)
 grid=[[0]*b['map']['width'] for _ in range(b['map']['height'])]
 for p in b['runtime']['blocked']:grid[p['y']-1][p['x']-1]=5
 (D/f'assets/maps/hexmaps/{58+bi}.json').write_text(json.dumps({'width':b['map']['width'],'height':b['map']['height'],'data':grid},separators=(',',':'))+'\n')
# This package contains only the Lin Chong campaign.
p=D/'scenarios/dialogue-text.json';catalog={'format':'jojo-scenario-dialogue-text/v1','entries':entries,'aliases':{},'legacyKeys':{}};p.write_text(json.dumps(catalog,ensure_ascii=False,indent=2)+'\n')
p=D/'scenarios/manifest.json';manifest={}
for i in range(90,98):
 for prefix in ['R','S']:
  name=f'{prefix}_{i}';manifest[name]={'source':f'linchong/{name}.lua','language':'lua','functions':['__dispatch__','scene0']+(['scene1','scene2','win_condition','lose_condition'] if prefix=='S' else []),'output':f'lua/{name}.lua'}
p.write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n')
p=D/'variables/globals.yaml';base=p.read_text();base=base.split('  linchong.')[0].rstrip()+'\n'
for flag in sorted(globals):base+='  linchong.'+flag+':\n    type: int\n    default: 0\n    description: Linchong story progression flag.\n'
p.write_text(base)
print('Generated 16 original-engine scenarios; '+str(len(entries))+' dialogue entries; '+str(len(globals))+' named globals.')
print('Unmapped triggers:',adapt)
