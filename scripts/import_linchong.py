"""Lossless, deterministic PRD content import; source coordinates remain one based."""
import json,re,pathlib,hashlib
ROOT=pathlib.Path(__file__).resolve().parents[1]
SRC=ROOT/'content/examples/linchong/source.md'
s=SRC.read_text()
def clean(s): return s.strip().replace('`','')
def dialogue(s): return [dict(speaker=a,text=b.strip()) for a,b in re.findall(r'^> \*\*(.+?)\*\*: (.+)$',s,re.M)]
def section(s,title):
 m=re.search(r'^#### '+re.escape(title)+r'[^\n]*\n(.*?)(?=^#### |\Z)',s,re.M|re.S)
 return m[1] if m else ''
def rows(s):
 out=[]
 for l in s.splitlines():
  if l.startswith('|'):
   r=[clean(x) for x in l.strip('|').split('|')]
   if not re.match(r'^:?-',r[0]):out.append(r)
 return out[1:]
def coords(s): return [dict(x=int(x),y=int(y)) for x,y in re.findall(r'\((\d+),(\d+)\)',s)]
def bullets(s):return re.findall(r'^\s*- (.+)$',s,re.M)
def actions(s):
 m=re.search(r'\*\*처리 액션:\*\* ([^\n]+)',s)
 return [dict(command=x.split(':')[0],args=x.split(':')[1:],raw=x) for x in re.findall(r'`([^`]+)`',m[1])] if m else []
def table_section(title):
 m=re.search(r'^### '+re.escape(title)+r'[^\n]*\n(.*?)(?=^### |^## |\Z)',s,re.M|re.S)
 return rows(m[1])
c=dict(id='linchong_demo',schemaVersion=1,metadata={'title':'수호전 외전: 임충전','version':'1.0','coordinateBase':1,'sourceSha256':hashlib.sha256(s.encode()).hexdigest()},progression=['PROLOGUE','B01','B02','EX01?','B03','B04','B05','EX02?','B06','EPILOGUE'])
c['characters']=[dict(zip(['id','name','role','policy'],r)) for r in table_section('6-1. 등장인물 및 파티 정책')]
c['startingLoadouts']=[dict(zip(['unitId','skills','weapon','armor','treasures'],r)) for r in table_section('6-1b. 장수별 시작 무공과 장비 (실제 장착 상태는 저장 데이터 우선)')]
c['skills']=[dict(zip(['id','name','eligibility','type','cost','effect','masteryUpgrades'],r)) for r in table_section('6-2. 무공과 숙련 강화')]
c['items']=[dict(zip(['id','name','type','effect'],r)) for r in table_section('6-3. 수집 보상/장비/비급')]
c['rules']=[dict(heading=m[1],text=m[2].strip()) for m in re.finditer(r'^### ([0-4]\. [^\n]+)\n(.*?)(?=^### |^## |\Z)',s,re.M|re.S)]
c['interludes']={m[1]:dialogue(m[2]) for m in re.finditer(r'^### (PROLOGUE|B\d\d_TO_B\d\d|EPILOGUE)\n(.*?)(?=^### |^## |\Z)',s,re.M|re.S)}
c['battles']=[]
for m in re.finditer(r'^### ((?:B|EX)\d\d) — (.+?)\n(.*?)(?=^### (?:B|EX)\d\d — |^## 8\.|\Z)',s,re.M|re.S):
 bid,name,t=m.groups(); meta=dict(re.findall(r'^- \*\*(.+?):\*\* (.+)$',t,re.M)); w,h,turn=map(int,re.search(r'(\d+)×(\d+).*?턴 제한 (\d+)',meta['맵']).groups())
 b=dict(id=bid,name=name,chapter=meta['장'],source=meta['원작/각색'],optional=bid.startswith('EX'),map=dict(width=w,height=h,turnLimit=turn,coordinateBase=1),synopsis=meta['줄거리'],victory=meta['승리'],defeat=meta['패배'],terrain=bullets(section(t,'지형·상호작용 오브젝트')),objects=[],units=[],spawns=[],events=[])
 for r in rows(section(t,'지형·상호작용 오브젝트')):
  o=dict(id=r[0],type=r[1],positions=coords(r[2]),condition=r[3]);o.update({k:(int(v) if v.isdigit() else v) for k,v in re.findall(r'(\w+)=([^;]+)',r[3])});b['objects'].append(o)
 for r in rows(section(t,'최초 배치')):
  hp,mhp=map(int,r[4].split('/'));n,_,role=r[1].partition(' / ');p=coords(r[10])[0]
  u=dict(id=r[0],name=n,role=role,side=r[2],level=int(r[3]),hp=hp,maxHp=mhp,mp=int(r[5]),attack=int(r[6]),defense=int(r[7]),agility=int(r[8]),move=int(r[9]),**p,ai=r[11].split(';')[0],notes=r[11]);
  if u['id'] in ('hu','jin') and bid in ('B04','B05','EX02','B06') and u['side']=='ALLY':u['condition']='RECRUITED:'+u['id']
  b['units'].append(u)
 for r in rows(section(t,'이벤트 증원/난입 좌표')):
  uid,_,n=r[1].partition(' ');hp,mp=map(int,r[4].split('/'));atk,defe,agi,mov=map(int,r[5].split('/'))
  b['spawns'].append(dict(eventId=r[0],id=uid,name=n,side=r[2],level=int(r[3]),hp=hp,maxHp=hp,mp=mp,attack=atk,defense=defe,agility=agi,move=mov,**coords(r[6])[0],ai=r[7],notes=r[7]))
 ev=section(t,'전체 대화·이벤트')
 for e in re.finditer(r'\*\*`([^`]+)` — `([^`]+)`\*\* \(우선순위 (\d+)\)\n(.*?)(?=\*\*`[^`]+` — |\Z)',ev,re.S):
  eid,tr,pr,body=e.groups();b['events'].append(dict(id=eid,trigger=tr,priority=int(pr),once=True,dialogue=dialogue(body),actions=actions(body),notes=bullets(body)))
 cond=section(t,'조건별 대사 대체 / 추가');b['conditionalDialogue']=[]
 for v in re.finditer(r'- `([^`]+)` / 조건 `([^`]+)`:(.*?)(?=\n- `|\Z)',cond,re.S): b['conditionalDialogue'].append(dict(eventId=v[1],condition=v[2],dialogue=dialogue(v[3])))
 b['dialoguePolicy']='Hide unjoined companion lines; apply matching conditional replacements.'
 b['loot']=[dict(itemId=i,count=int(n),condition=cond) for i,n,cond in re.findall(r'^- `([^`]+)` ×(\d+) — (.+)$',section(t,'전리품과 다음 단계'),re.M)]
 b['next']=re.findall(r'`([^`]+)`',next((x for x in bullets(section(t,'전리품과 다음 단계')) if x.startswith('이동 대상:')),''))
 b['notes']=bullets(section(t,'제작 시 주의 사항'))
 c['battles'].append(b)
runtime=json.loads((ROOT/'content/examples/linchong/runtime_rules.json').read_text())
for b in c['battles']:
 b['runtime']=dict({'heroId':'lin','persistentHeroIds':['lin'],'permanentAtStart':['lin'],'blocked':[],'initialStatuses':[]},**runtime[b['id']])
for b in c['battles']:
 b['assets']={'map':'campaign/linchong/maps/'+b['id']+'.png'}
def flag(id):return {'type':'flag','id':id}
def present(id):return {'type':'allyPresent','id':id}
variantConditions={
 'HU_ABSENT':{'not':present('hu')},'JIN_ABSENT':{'not':present('jin')},
 'HU_AND_JIN_ABSENT':{'all':[{'not':present('hu')},{'not':present('jin')}]},
 'HU_NOT_RESCUED':{'not':flag('B03_HU_RESCUED')},
 'JIN_NOT_RESCUED':{'not':flag('B04_JIN_RESCUED')},
 'HU_RESCUED_AND_ACCEPTED':{'all':[flag('B03_HU_RESCUED'),{'type':'recruited','id':'hu'}]},
 'HU_RESCUED_AND_DECLINED':flag('HU_FRIEND_ONLY'),
 'B04_EVIDENCE':flag('B04_EVIDENCE'),'EX02_CLEAR':flag('EX02_CLEAR')}
for b in c['battles']:
 for v in b['conditionalDialogue']:
  v['when']=variantConditions[v['condition']]
  if v['condition'] in ['HU_RESCUED_AND_ACCEPTED','HU_RESCUED_AND_DECLINED']:v['timing']='afterChoice'
 for e in b['events']:
  for line in e['dialogue']:
   if line['speaker']=='송만':line['when']={'any':[present('song'),present('song_support')]}
  if e['id']=='B03_E90':
   for i,line in enumerate(e['dialogue']):
    if i in [1,2,3]:line['when']={'type':'recruited','id':'hu'};line['timing']='afterChoice'
c['mechanics']=json.loads((ROOT/'content/examples/linchong/mechanics.json').read_text())
for skill in c['skills']:skill['parameters']=c['mechanics']['skillParameters'].get(skill['id'],{})
for item in c['items']:item['parameters']=c['mechanics']['itemParameters'].get(item['id'],{})
c['supplements']=json.loads((ROOT/'content/examples/linchong/supplements.json').read_text())
c['choices']=[dict(id='HU_RECRUITMENT',eventId='B03_E90',condition='B03_HU_RESCUED',prompt='허정 동행 허락',options=[dict(id='ACCEPT',label='함께 가시오',actions=['recruit_permanent:hu']),dict(id='DECLINE',label='스스로 길을 찾으시오',actions=['set_flag:HU_FRIEND_ONLY'])])]
c['engineSafety']=s[s.index('## 8.'):s.index('## 9.')]
c['qa']=[dict(zip(['id','scenario','expected'],r)) for r in rows(s[s.index('## 9.'):s.index('### 9-1.')])]
c['sourceMarkdown']='source.md'
c['counts']=dict(battles=len(c['battles']),units=sum(len(b['units'])+len(b['spawns']) for b in c['battles']),events=sum(len(b['events']) for b in c['battles']),eventDialogue=sum(len(e['dialogue']) for b in c['battles'] for e in b['events']),conditionalDialogue=sum(len(v['dialogue']) for b in c['battles'] for v in b['conditionalDialogue']),interludeDialogue=sum(map(len,c['interludes'].values())))
assert c['counts']==dict(battles=8,units=70,events=79,eventDialogue=217,conditionalDialogue=13,interludeDialogue=34),c['counts']
for b in c['battles']:
 for u in b['units']+b['spawns']:
  assert 1<=u['x']<=b['map']['width'] and 1<=u['y']<=b['map']['height']
 for o in b['objects']:
  assert o['positions']
 for sp in b['spawns']:assert sp['eventId'] in [e['id'] for e in b['events']]
(ROOT/'content/examples/linchong/campaign.json').write_text(json.dumps(c,ensure_ascii=False,indent=2)+'\n')
print(json.dumps(c['counts']))
