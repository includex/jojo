#!/usr/bin/env node
'use strict';

const fs = require('node:fs');
const http = require('node:http');
const path = require('node:path');
const { spawn } = require('node:child_process');

const sourceRoot = path.resolve(process.argv[2] || '../jojo_mobile/sgccz-desktop');
const outputRoot = path.resolve(process.argv[3] || 'build/reports/yingchuan-source-screens');
const semanticMode = process.argv[5] || '';
const maxWallMs = Number(process.argv[4] || 30000);
const maxAllowedWallMs = ['single-player-action', 'round2-followup', 'round2-first-combat'].includes(semanticMode) ? 180000 : semanticMode === 'round2-handoff' ? 150000 : semanticMode === 'first-round-end' ? 90000 : 60000;
if (!Number.isInteger(maxWallMs) || maxWallMs < 10000 || maxWallMs > maxAllowedWallMs) throw new Error(`duration must be an integer from 10000 through ${maxAllowedWallMs} ms`);
if (semanticMode && !['235-hit-hold', 'first-normal-combat', 'next-normal-actions', 'enemy-first-combat', 'enemy-arrival-only', '477-settlement-order', 'first-round-end', 'round2-handoff', 'single-player-action', 'round2-followup', 'round2-first-combat'].includes(semanticMode)) throw new Error(`unknown semantic mode ${semanticMode}`);
const port = 9400 + (process.pid % 200);
const deadlineMs = maxWallMs + 15000;
const delay = ms => new Promise(resolve => setTimeout(resolve, ms));

function getJson(url) {
  return new Promise((resolve, reject) => {
    const request = http.get(url, response => {
      const chunks = [];
      response.on('data', chunk => chunks.push(chunk));
      response.on('end', () => {
        try { resolve(JSON.parse(Buffer.concat(chunks))); } catch (error) { reject(error); }
      });
    });
    request.setTimeout(1000, () => request.destroy(new Error('HTTP timeout')));
    request.on('error', reject);
  });
}

async function connect(url) {
  const socket = new WebSocket(url);
  await new Promise((resolve, reject) => {
    socket.addEventListener('open', resolve, { once: true });
    socket.addEventListener('error', reject, { once: true });
  });
  let id = 0;
  const pending = new Map();
  socket.addEventListener('message', event => {
    const message = JSON.parse(event.data);
    const wait = pending.get(message.id);
    if (!wait) return;
    pending.delete(message.id);
    message.error ? wait.reject(new Error(JSON.stringify(message.error))) : wait.resolve(message.result);
  });
  socket.addEventListener('close', () => {
    for (const wait of pending.values()) wait.reject(new Error('CDP closed'));
    pending.clear();
  });
  return {
    send(method, params = {}) {
      return new Promise((resolve, reject) => {
        const requestId = ++id;
        pending.set(requestId, { resolve, reject });
        socket.send(JSON.stringify({ id: requestId, method, params }));
      });
    },
    close() { socket.close(); },
  };
}

async function poll(fn, label, expires) {
  let last;
  while (Date.now() < expires) {
    try { const value = await fn(); if (value) return value; } catch (error) { last = error; }
    await delay(100);
  }
  throw new Error(`${label} timed out${last ? `: ${last.message}` : ''}`);
}

async function cdpClick(client, point) {
  if (!point || !Number.isFinite(point.x) || !Number.isFinite(point.y)) throw new Error(`invalid CDP click point ${JSON.stringify(point)}`);
  await client.send('Input.dispatchMouseEvent', { type: 'mousePressed', x: point.x, y: point.y, button: 'left', clickCount: 1 });
  await client.send('Input.dispatchMouseEvent', { type: 'mouseReleased', x: point.x, y: point.y, button: 'left', clickCount: 1 });
}

const stateExpression = `(() => {
  const scene=globalThis.cc&&cc.director&&cc.director.getScene(); let battle=null,say=null; const units=[]; const layers=[]; const infoPanels=[];
  if(!scene)return {ready:false};
  (function visit(node){if(!node)return; for(const component of node._components||[]){const name=cc.js.getClassName(component); if(name==='BattleLayer')battle=component;if(name==='SayLayer'&&node.activeInHierarchy)say=component; if(/Layer$/.test(name)&&node.activeInHierarchy)layers.push(name);if(name==='OtherUnitInfoLayer'&&node.activeInHierarchy){const labels=[];(function labelsOf(n,p){for(const c of n._components||[]){if(c instanceof cc.Label)labels.push({path:p,text:c.string});}n.children.forEach(ch=>labelsOf(ch,p+'/'+ch.name));})(node,node.name);infoPanels.push({identity:node.uuid||node._id||null,labels,bars:(component._bars||[]).map(b=>b&&b.progress),value:component._value||null,pendingValues:(component._arys||[]).slice()});}} node.children.forEach(visit)})(scene);
  if(battle)for(const u of Object.values(battle._unitSet||{}).filter(Boolean)){const source=u.unit&&u.unit(),node=u.node,n=node&&node.getChildByName('mask')&&node.getChildByName('mask').getChildByName('node'),a=n&&n.getComponent(cc.Animation),sprite=n&&n.getComponent(cc.Sprite),frame=sprite&&sprite.spriteFrame,playing=a&&a._nameToState&&Object.entries(a._nameToState).find(([,s])=>s&&s.isPlaying);units.push({id:source&&source.id?source.id():null,index:u.index?u.index():null,x:u.x?u.x():null,y:u.y?u.y():null,hitPoints:u.hp_cur?u.hp_cur():null,direction:u.dir?u.dir():null,action:source&&source.action?source.action():null,acted:u.isAction?u.isAction():null,visible:u.visible?u.visible():null,exists:u.isExist?u.isExist():null,animation:playing?playing[0]:null,spriteFrame:frame&&frame.name||null,spriteRect:frame&&frame._rect?[frame._rect.x,frame._rect.y,frame._rect.width,frame._rect.height]:null});}
  const dialogue=layers.includes('SayLayer');
  const camera=battle&&battle._scrollView&&battle._scrollView.content&&battle._scrollView.content.position;
  const gateKeys=battle&&battle._gateNodes?Object.keys(battle._gateNodes).map(Number).sort((a,b)=>a-b):[];
  const dialogueStrings=say&&Array.isArray(say._strings)?say._strings.map(String):[],dialogueCursor=Math.max(0,Math.min(Number(say&&say._index)||0,dialogueStrings.length));let dialogueSpeaker=-1;for(let i=Math.min(dialogueCursor-1,dialogueStrings.length-1);i>=0;i--)if(dialogueStrings[i].charAt(0)==='&'){dialogueSpeaker=i;break}const dialogueSpeakerId=dialogueSpeaker>=0?dialogueStrings[dialogueSpeaker].substring(1):null;
  const targetInfo=u=>u&&u.unit?{id:u.unit().id(),index:u.index(),pos:[u.x(),u.y()],hp:u.hp_cur(),exists:u.isExist(),visible:u.visible()}:null;
  return {ready:!!battle,frame:cc.director.getTotalFrames?cc.director.getTotalFrames():null,scene:scene.name,round:battle&&battle.round?battle.round():null,camp:battle&&battle.curCamp?battle.curCamp():null,camera:camera?[camera.x,camera.y]:null,dialogue,dialogueSpeakerId,dialogueText:say&&say._curLab?say._curLab.string:null,dialogueRemaining:say&&say._nextString!=null?say._nextString:null,layers:[...new Set(layers)],gateKeys,infoPanels,battleTargets:{source:targetInfo(battle&&battle._srcTarget),destination:targetInfo(battle&&battle._dscTarget)},units:units.filter(u=>[0,3,32,33,157,210,211,234,235,258,259,334,474,475,476,477,478,479,480,481,482,483,484,485].includes(u.id))};
})()`;

(async () => {
  fs.rmSync(outputRoot, { recursive: true, force: true });
  fs.mkdirSync(outputRoot, { recursive: true });
  const trace = path.join(outputRoot, 'source-full-trace.json');
  const logFd = fs.openSync(path.join(outputRoot, 'source-process.log'), 'w');
  const child = spawn('npm', ['exec', '--', 'electron', '.', '--battle', '--scenario=S_00', `--full-battle-trace=${trace}`, '--full-battle-time-scale=1', `--full-battle-max-wall-ms=${maxWallMs}`, '--full-battle-seed=1000', '--full-battle-math-seed=305419896', `--remote-debugging-port=${port}`], { cwd: sourceRoot, stdio: ['ignore', logFd, logFd], detached: true });
  const childExit = new Promise(resolve => child.once('exit', (code, signal) => resolve({ code, signal })));
  let client;
  const watchdog = setTimeout(() => { try { process.kill(-child.pid, 'SIGKILL'); } catch {} }, deadlineMs);
  try {
    const page = await poll(async () => (await getJson(`http://127.0.0.1:${port}/json/list`)).find(item => item.type === 'page' && item.webSocketDebuggerUrl), 'source page', Date.now() + 10000);
    if (page.url !== 'sgccz://game/electron/index.html') throw new Error(`unexpected page ${page.url}`);
    client = await connect(page.webSocketDebuggerUrl);
    await client.send('Page.enable');
    await poll(async () => {
      const result = await client.send('Runtime.evaluate', { expression: stateExpression, returnByValue: true });
      return result.result.value.ready;
    }, 'BattleLayer', Date.now() + 10000);
    const started = Date.now();
    const captures = [];
    if (semanticMode === '235-hit-hold') {
      let sawHit = false;
      const transitions = [];
      let prior = '';
      while (Date.now() - started < Math.min(maxWallMs - 1000, 29000)) {
        const evaluated = await client.send('Runtime.evaluate', { expression: stateExpression, returnByValue: true });
        if (evaluated.exceptionDetails) throw new Error(JSON.stringify(evaluated.exceptionDetails));
        const state = evaluated.result.value;
        const unit = state.units.find(item => item.id === 235);
        const key = unit ? JSON.stringify([unit.action, unit.animation, unit.spriteFrame, unit.spriteRect]) : 'missing';
        if (key !== prior) { transitions.push({ wallSeconds: (Date.now() - started) / 1000, frame: state.frame, unit }); prior = key; }
        if (unit && unit.animation === 'anime32_3') sawHit = true;
        if (sawHit && unit && unit.animation === null && JSON.stringify(unit.spriteRect) === '[1268,153,48,48]' && unit.action !== 4) {
          const image = await client.send('Page.captureScreenshot', { format: 'png', fromSurface: true });
          const file = 'source-235-hit-hold.png';
          fs.writeFileSync(path.join(outputRoot, file), Buffer.from(image.data, 'base64'));
          captures.push({ wallSeconds: (Date.now() - started) / 1000, file, semantic: 'unit235-anime32_3-final-frame-hold-before-action4', ...state });
          fs.writeFileSync(path.join(outputRoot, 'screens.json'), JSON.stringify({ contract: 'source-yingchuan-normal-clock-semantic-screen-v1', evidenceKind: 'actual-source-renderer-direct-battle-bootstrap', sourceRoot, scenario: 'S_00', timeScale: 1, maxWallMs, semanticMode, transitions, bootstrap: { route: 'HallLayer.jumpScene(0)', seededBattleUnits: [0], normalDialogueInput: 'SayLayer Panel_cancel TOUCH_END', fixture: false, fullCampaignEntry: false }, captures }, null, 2) + '\n');
          console.log(`SOURCE_YINGCHUAN_SEMANTIC_SCREEN_OK ${file}`);
          return;
        }
        await delay(8);
      }
      throw new Error(`semantic state not observed: sawHit=${sawHit}; transitions=${JSON.stringify(transitions)}`);
    }
    if (semanticMode === 'first-normal-combat') {
      const transitions = [], wanted = ['ally210-attack', 'enemy476-reaction', 'enemy476-counter-ally210-reaction', 'settlement'];
      const seen = new Set(); let prior = '';
      async function captureSemantic(name, state) {
        const image = await client.send('Page.captureScreenshot', { format: 'png', fromSurface: true });
        const file = `source-${name}.png`; fs.writeFileSync(path.join(outputRoot, file), Buffer.from(image.data, 'base64'));
        captures.push({ wallSeconds: (Date.now() - started) / 1000, file, semantic: name, ...state }); seen.add(name);
      }
      while (Date.now() - started < Math.min(maxWallMs - 1000, 44000) && seen.size < wanted.length) {
        const evaluated = await client.send('Runtime.evaluate', { expression: stateExpression, returnByValue: true });
        if (evaluated.exceptionDetails) throw new Error(JSON.stringify(evaluated.exceptionDetails));
        const state = evaluated.result.value, ally = state.units.find(x => x.id === 210), enemy = state.units.find(x => x.id === 476);
        const key = JSON.stringify([ally&&[ally.action,ally.animation,ally.spriteRect],enemy&&[enemy.action,enemy.animation,enemy.spriteRect],state.layers]);
        if (key !== prior) { transitions.push({ wallSeconds: (Date.now() - started) / 1000, frame: state.frame, ally, enemy, layers: state.layers }); prior = key; }
        if (!seen.has(wanted[0]) && ally?.animation?.startsWith('anime25')) await captureSemantic(wanted[0], state);
        else if (seen.has(wanted[0]) && !seen.has(wanted[1]) && enemy?.animation?.startsWith('anime32')) await captureSemantic(wanted[1], state);
        else if (seen.has(wanted[1]) && !seen.has(wanted[2]) && enemy?.animation?.startsWith('anime25') && ally?.animation?.startsWith('anime32')) await captureSemantic(wanted[2], state);
        else if (seen.has(wanted[2]) && !seen.has(wanted[3]) && state.layers.some(x => /^(Mine|Other)UnitInfoLayer$/.test(x)) && !ally?.animation?.startsWith('anime32') && !enemy?.animation?.startsWith('anime25')) await captureSemantic(wanted[3], state);
        await delay(8);
      }
      if (seen.size !== wanted.length) throw new Error(`combat semantic states incomplete: ${JSON.stringify({seen:[...seen],transitions})}`);
      fs.writeFileSync(path.join(outputRoot, 'screens.json'), JSON.stringify({ contract: 'source-yingchuan-normal-clock-combat-screens-v1', evidenceKind: 'actual-source-renderer-direct-battle-bootstrap', sourceRoot, scenario: 'S_00', timeScale: 1, maxWallMs, semanticMode, transitions, bootstrap: { route: 'HallLayer.jumpScene(0)', seededBattleUnits: [0], normalDialogueInput: 'SayLayer Panel_cancel TOUCH_END', fixture: false, fullCampaignEntry: false }, captures }, null, 2) + '\n');
      // Let the bounded source driver reach its own max-wall terminal so it
      // flushes the authoritative frame trace beside the semantic screens.
      await childExit;
      if (!fs.existsSync(trace)) throw new Error('source combat trace was not flushed');
      console.log(`SOURCE_YINGCHUAN_COMBAT_SCREENS_OK ${captures.length}`); return;
    }
    if (semanticMode === 'next-normal-actions') {
      const wanted = ['ally211-attack','enemy475-reaction','enemy475-counter-ally211-reaction','ally211-settlement','ally234-move','ally234-special-attack','enemy476-special-reaction','ally234-special-finish'];
      const seen = new Set(), transitions = []; let prior = '';
      async function captureSemantic(name, state) { const image=await client.send('Page.captureScreenshot',{format:'png',fromSurface:true}),file=`source-${name}.png`;fs.writeFileSync(path.join(outputRoot,file),Buffer.from(image.data,'base64'));captures.push({wallSeconds:(Date.now()-started)/1000,file,semantic:name,...state});seen.add(name); }
      while(Date.now()-started<Math.min(maxWallMs-1000,44000)&&seen.size<wanted.length){
        const evaluated=await client.send('Runtime.evaluate',{expression:stateExpression,returnByValue:true});if(evaluated.exceptionDetails)throw Error(JSON.stringify(evaluated.exceptionDetails));
        const state=evaluated.result.value,u=id=>state.units.find(x=>x.id===id),a211=u(211),e475=u(475),a234=u(234),e476=u(476);
        const key=JSON.stringify([a211&&[a211.x,a211.y,a211.animation,a211.spriteRect],e475&&[e475.animation,e475.spriteRect],a234&&[a234.x,a234.y,a234.animation,a234.spriteRect],e476&&[e476.animation,e476.spriteRect],state.layers]);if(key!==prior){transitions.push({wallSeconds:(Date.now()-started)/1000,frame:state.frame,ally211:a211,enemy475:e475,ally234:a234,enemy476:e476,layers:state.layers});prior=key;}
        if(!seen.has(wanted[0])&&a211?.animation?.startsWith('anime25'))await captureSemantic(wanted[0],state);
        else if(seen.has(wanted[0])&&!seen.has(wanted[1])&&e475?.animation?.startsWith('anime32'))await captureSemantic(wanted[1],state);
        else if(seen.has(wanted[1])&&!seen.has(wanted[2])&&e475?.animation?.startsWith('anime25')&&a211?.animation?.startsWith('anime32'))await captureSemantic(wanted[2],state);
        else if(seen.has(wanted[2])&&!seen.has(wanted[3])&&state.layers.some(x=>/^(Mine|Other)UnitInfoLayer$/.test(x))&&!a211?.animation?.startsWith('anime32')&&!e475?.animation?.startsWith('anime25'))await captureSemantic(wanted[3],state);
        else if(seen.has(wanted[3])&&!seen.has(wanted[4])&&a234?.animation?.startsWith('anime20'))await captureSemantic(wanted[4],state);
        else if(seen.has(wanted[4])&&!seen.has(wanted[5])&&a234?.animation?.startsWith('anime48'))await captureSemantic(wanted[5],state);
        else if(seen.has(wanted[5])&&!seen.has(wanted[6])&&e476?.animation?.startsWith('anime32'))await captureSemantic(wanted[6],state);
        else if(seen.has(wanted[6])&&!seen.has(wanted[7])&&a234?.animation==='anime9')await captureSemantic(wanted[7],state);
        await delay(8);
      }
      if(seen.size!==wanted.length)throw Error(`next combat semantic states incomplete: ${JSON.stringify({seen:[...seen],transitions})}`);
      fs.writeFileSync(path.join(outputRoot,'screens.json'),JSON.stringify({contract:'source-yingchuan-normal-clock-next-actions-v1',evidenceKind:'actual-source-renderer-direct-battle-bootstrap',sourceRoot,scenario:'S_00',timeScale:1,maxWallMs,semanticMode,transitions,bootstrap:{route:'HallLayer.jumpScene(0)',seededBattleUnits:[0],normalDialogueInput:'SayLayer Panel_cancel TOUCH_END',fixture:false,fullCampaignEntry:false},captures},null,2)+'\n');
      await childExit;if(!fs.existsSync(trace))throw Error('source next-actions trace was not flushed');console.log(`SOURCE_YINGCHUAN_NEXT_ACTIONS_OK ${captures.length}`);return;
    }
    if (semanticMode === 'enemy-first-combat') {
      const wanted=['enemy474-move-start','enemy474-last-leg','enemy474-arrival-idle-dir1','enemy474-post-attack','ally234-defeated-visible','ally234-hidden'],seen=new Set(),transitions=[];let prior='';
      async function captureSemantic(name,state){const image=await client.send('Page.captureScreenshot',{format:'png',fromSurface:true}),file=`source-${name}.png`;fs.writeFileSync(path.join(outputRoot,file),Buffer.from(image.data,'base64'));captures.push({wallSeconds:(Date.now()-started)/1000,file,semantic:name,...state});seen.add(name);}
      while(Date.now()-started<Math.min(maxWallMs-1000,59000)&&seen.size<wanted.length){const evaluated=await client.send('Runtime.evaluate',{expression:stateExpression,returnByValue:true});if(evaluated.exceptionDetails)throw Error(JSON.stringify(evaluated.exceptionDetails));const state=evaluated.result.value,u=id=>state.units.find(x=>x.id===id),enemy=u(474),ally=u(234),key=JSON.stringify([enemy&&[enemy.x,enemy.y,enemy.direction,enemy.animation,enemy.spriteRect,enemy.visible,enemy.exists],ally&&[ally.direction,ally.animation,ally.spriteRect,ally.visible,ally.exists],state.layers]);if(key!==prior){transitions.push({wallSeconds:(Date.now()-started)/1000,frame:state.frame,enemy474:enemy,ally234:ally,layers:state.layers});prior=key;}
        if(!seen.has(wanted[0])&&enemy?.animation==='anime20_2')await captureSemantic(wanted[0],state);
        else if(seen.has(wanted[0])&&!seen.has(wanted[1])&&enemy?.animation==='anime20_1')await captureSemantic(wanted[1],state);
        else if(seen.has(wanted[1])&&!seen.has(wanted[2])&&enemy?.x===9&&enemy?.y===17&&enemy?.direction===1&&enemy?.action===0&&enemy?.animation==='anime0_1'&&ally?.exists===true)await captureSemantic(wanted[2],state);
        else if(seen.has(wanted[2])&&!seen.has(wanted[3])&&enemy?.action===1)await captureSemantic(wanted[3],state);
        else if(seen.has(wanted[3])&&!seen.has(wanted[4])&&ally?.exists===false&&ally?.visible===true)await captureSemantic(wanted[4],state);
        else if(seen.has(wanted[4])&&!seen.has(wanted[5])&&ally?.visible===false)await captureSemantic(wanted[5],state);
        await delay(8);}
      const missingCaptures=wanted.filter(name=>!seen.has(name));
      fs.writeFileSync(path.join(outputRoot,'screens.json'),JSON.stringify({contract:'source-yingchuan-normal-clock-enemy-first-combat-v1',evidenceKind:'actual-source-renderer-direct-battle-bootstrap',sourceRoot,scenario:'S_00',timeScale:1,maxWallMs,semanticMode,complete:missingCaptures.length===0,missingCaptures,transitions,bootstrap:{route:'HallLayer.jumpScene(0)',seededBattleUnits:[0],normalDialogueInput:'SayLayer Panel_cancel TOUCH_END',fixture:false,fullCampaignEntry:false},captures},null,2)+'\n');
      if(missingCaptures.length)throw Error(`enemy combat semantic states incomplete: ${JSON.stringify({wanted,seen:[...seen],missingCaptures,transitions})}`);
      await childExit;if(!fs.existsSync(trace))throw Error('source enemy-combat trace was not flushed');console.log(`SOURCE_YINGCHUAN_ENEMY_COMBAT_OK ${captures.length}`);return;
    }
    if(semanticMode==='enemy-arrival-only'){
      let observed=null;
      while(Date.now()-started<Math.min(maxWallMs-1000,59000)){const evaluated=await client.send('Runtime.evaluate',{expression:stateExpression,returnByValue:true});if(evaluated.exceptionDetails)throw Error(JSON.stringify(evaluated.exceptionDetails));const state=evaluated.result.value,enemy=state.units.find(x=>x.id===474),ally=state.units.find(x=>x.id===234);if(enemy?.x===9&&enemy?.y===17&&enemy?.direction===1&&enemy?.action===0&&enemy?.animation==='anime0_1'&&ally?.exists===true){observed={wallSeconds:(Date.now()-started)/1000,...state};const image=await client.send('Page.captureScreenshot',{format:'png',fromSurface:true}),file='source-enemy474-arrival-idle-dir1.png';fs.writeFileSync(path.join(outputRoot,file),Buffer.from(image.data,'base64'));captures.push({...observed,file,semantic:'enemy474-arrival-idle-dir1-before-attack'});break;}await delay(4);}
      if(!observed)throw Error('enemy474 pre-attack arrival was not observed');fs.writeFileSync(path.join(outputRoot,'screens.json'),JSON.stringify({contract:'source-yingchuan-normal-clock-enemy-arrival-v1',evidenceKind:'actual-source-renderer-direct-battle-bootstrap',sourceRoot,scenario:'S_00',timeScale:1,maxWallMs,semanticMode,bootstrap:{route:'HallLayer.jumpScene(0)',seededBattleUnits:[0],normalDialogueInput:'SayLayer Panel_cancel TOUCH_END',fixture:false,fullCampaignEntry:false},captures},null,2)+'\n');await childExit;if(!fs.existsSync(trace))throw Error('source enemy-arrival trace was not flushed');console.log('SOURCE_YINGCHUAN_ENEMY_ARRIVAL_OK');return;
    }
    if(semanticMode==='477-settlement-order'){
      const observedIdentities=new Set(),observations=[];let priorObservation='';
      while(Date.now()-started<Math.min(maxWallMs-1000,59000)&&captures.length<2){
        const evaluated=await client.send('Runtime.evaluate',{expression:stateExpression,returnByValue:true});if(evaluated.exceptionDetails)throw Error(JSON.stringify(evaluated.exceptionDetails));
        const state=evaluated.result.value,unit477=state.units.find(x=>x.id===477);
        for(const panel of state.infoPanels||[]){
          const hpTransition=panel.value&&panel.value.idx===0&&((panel.value.src===104&&panel.value.dsc===83)||(panel.value.src===97&&panel.value.dsc===77));
          if(!unit477||!hpTransition||observedIdentities.has(panel.identity))continue;
          observedIdentities.add(panel.identity);const image=await client.send('Page.captureScreenshot',{format:'png',fromSurface:true}),ordinal=captures.length+1,file=`source-477-settlement-${ordinal}.png`;
          fs.writeFileSync(path.join(outputRoot,file),Buffer.from(image.data,'base64'));captures.push({wallSeconds:(Date.now()-started)/1000,file,semantic:`477-counter-settlement-${ordinal}`,panel,...state});
        }
        const observationKey=JSON.stringify([unit477&&unit477.hitPoints,(state.infoPanels||[]).map(p=>[p.identity,p.labels,p.bars,p.value,p.pendingValues])]);
        if(observationKey!==priorObservation){observations.push({wallSeconds:(Date.now()-started)/1000,frame:state.frame,unit477,infoPanels:state.infoPanels});priorObservation=observationKey;}
        await delay(8);
      }
      const complete=captures.length===2;
      fs.writeFileSync(path.join(outputRoot,'screens.json'),JSON.stringify({contract:'source-yingchuan-477-settlement-order-v1',evidenceKind:'actual-source-renderer-direct-battle-bootstrap',sourceRoot,scenario:'S_00',timeScale:1,maxWallMs,semanticMode,complete,observations,bootstrap:{route:'HallLayer.jumpScene(0)',seededBattleUnits:[0],normalDialogueInput:'SayLayer Panel_cancel TOUCH_END',fixture:false,fullCampaignEntry:false},captures},null,2)+'\n');
      if(!complete)throw Error(`477 settlement panels incomplete: ${captures.length}/2`);
      await childExit;if(!fs.existsSync(trace))throw Error('source 477 settlement trace was not flushed');console.log('SOURCE_YINGCHUAN_477_SETTLEMENT_OK 2');return;
    }
    if(semanticMode==='first-round-end'){
      const wanted=['enemy484-action','enemy485-action','enemy475-action','enemy476-action','camp3-transition','round2-start','round2-dialogue'];
      const seen=new Set(),transitions=[];let prior='';
      async function captureSemantic(name,state){const image=await client.send('Page.captureScreenshot',{format:'png',fromSurface:true}),file=`source-${name}.png`;fs.writeFileSync(path.join(outputRoot,file),Buffer.from(image.data,'base64'));captures.push({wallSeconds:(Date.now()-started)/1000,file,semantic:name,...state});seen.add(name);}
      while(Date.now()-started<Math.min(maxWallMs-1000,89000)&&seen.size<wanted.length){
        const evaluated=await client.send('Runtime.evaluate',{expression:stateExpression,returnByValue:true});if(evaluated.exceptionDetails)throw Error(JSON.stringify(evaluated.exceptionDetails));const state=evaluated.result.value,u=id=>state.units.find(x=>x.id===id);
        const key=JSON.stringify([state.round,state.camp,state.dialogue,state.layers,[475,476,484,485].map(id=>{const x=u(id);return x&&[id,x.x,x.y,x.direction,x.action,x.animation,x.visible,x.exists]})]);
        if(key!==prior){transitions.push({wallSeconds:(Date.now()-started)/1000,frame:state.frame,round:state.round,camp:state.camp,dialogue:state.dialogue,layers:state.layers,units:state.units});prior=key;}
        for(const id of [484,485,475,476]){const name=`enemy${id}-action`,unit=u(id);if(!seen.has(name)&&state.round===1&&state.camp===2&&unit&&unit.visible===true&&unit.animation&&!unit.animation.startsWith('anime0_')&&!unit.animation.startsWith('anime39_')){await captureSemantic(name,state);break;}}
        if(!seen.has('camp3-transition')&&state.round===1&&state.camp===3)await captureSemantic('camp3-transition',state);
        else if(!seen.has('round2-start')&&state.round===2)await captureSemantic('round2-start',state);
        else if(seen.has('round2-start')&&!seen.has('round2-dialogue')&&state.round===2&&state.dialogue)await captureSemantic('round2-dialogue',state);
        await delay(8);
      }
      const missingCaptures=wanted.filter(name=>!seen.has(name));
      fs.writeFileSync(path.join(outputRoot,'screens.json'),JSON.stringify({contract:'source-yingchuan-normal-clock-first-round-end-v1',evidenceKind:'actual-source-renderer-direct-battle-bootstrap',sourceRoot,scenario:'S_00',timeScale:1,maxWallMs,semanticMode,complete:missingCaptures.length===0,missingCaptures,transitions,bootstrap:{route:'HallLayer.jumpScene(0)',seededBattleUnits:[0],normalDialogueInput:'SayLayer Panel_cancel TOUCH_END',fixture:false,fullCampaignEntry:false},captures},null,2)+'\n');
      await childExit;if(!fs.existsSync(trace))throw Error('source first-round-end trace was not flushed');
      if(missingCaptures.length){console.log(`SOURCE_YINGCHUAN_FIRST_ROUND_END_PARTIAL ${captures.length} missing=${missingCaptures.join(',')}`);return;}
      console.log(`SOURCE_YINGCHUAN_FIRST_ROUND_END_OK ${captures.length}`);return;
    }
    if(semanticMode==='single-player-action'||semanticMode==='round2-followup'||semanticMode==='round2-first-combat'){
      const followup=semanticMode!=='single-player-action',continueFirstDialogue=semanticMode==='round2-first-combat';
      const observations=[], inputs=[], wanted=['driver-disabled','unit0-selected','destination-reachable','unit0-moved','attack-command','enemy484-selected','attack-resolved','post-action-terminal',...(followup?['end-round-confirmed','next-camp-observed','followup-terminal']:[])];
      const seen=new Set(); let failure=null;
      async function evaluate(expression){const result=await client.send('Runtime.evaluate',{expression,returnByValue:true});if(result.exceptionDetails)throw Error(JSON.stringify(result.exceptionDetails));return result.result.value;}
      async function snapshot(label){const state=(await evaluate(stateExpression));const image=await client.send('Page.captureScreenshot',{format:'png',fromSurface:true});const file=`source-${String(captures.length+1).padStart(2,'0')}-${label}.png`;fs.writeFileSync(path.join(outputRoot,file),Buffer.from(image.data,'base64'));captures.push({label,file,wallSeconds:(Date.now()-started)/1000,...state});return state;}
      const manualState=`(() => {
        const scene=cc.director.getScene();let battle=null,command=null,say=null,win=null,fight=null,menu=null,msgBox=null;const settlementLayers=[];
        (function visit(n){if(!n)return;for(const c of n._components||[]){const k=cc.js.getClassName(c);if(k==='BattleLayer')battle=c;else if(k==='CommandLayer'&&n.activeInHierarchy)command=c;else if(k==='SayLayer'&&n.activeInHierarchy)say=c;else if((k==='WinConditionsLayer'||k==='WinConBoxLayer')&&n.activeInHierarchy)win=c;else if(k==='FightLayer'&&n.activeInHierarchy)fight=c;else if(k==='MenuLayer'&&n.activeInHierarchy)menu=c;else if(k==='MsgBox'&&n.activeInHierarchy)msgBox=c;else if((k==='MineUnitInfoLayer'||k==='OtherUnitInfoLayer')&&n.activeInHierarchy)settlementLayers.push(k);}n.children.forEach(visit)})(scene);
        if(!battle)return {ready:false}; const unit=id=>Object.values(battle._unitSet||{}).find(u=>u&&u.unit&&u.unit().id()===id),u0=unit(0),u484=unit(484);
        const canvas=cc.game.canvas,rect=canvas.getBoundingClientRect(),vp=cc.view._viewportRect||{x:0,y:0},toCss=w=>({x:rect.left+(vp.x+w.x*cc.view._scaleX)*rect.width/canvas.width,y:rect.top+(canvas.height-(vp.y+w.y*cc.view._scaleY))*rect.height/canvas.height});
        const nodeGeometry=n=>{if(!n)return null;const center=n.convertToWorldSpaceAR(cc.v2((.5-n.anchorX)*n.width,(.5-n.anchorY)*n.height)),bl=n.convertToWorldSpaceAR(cc.v2(-n.anchorX*n.width,-n.anchorY*n.height)),tr=n.convertToWorldSpaceAR(cc.v2((1-n.anchorX)*n.width,(1-n.anchorY)*n.height));return {name:n.name,size:[n.width,n.height],anchor:[n.anchorX,n.anchorY],center:[center.x,center.y],bottomLeft:[bl.x,bl.y],topRight:[tr.x,tr.y],cssCenter:toCss(center)};};
        const unitPoint=u=>u&&u.node?nodeGeometry(u.node).cssCenter:null;
        const tp=battle.turnPos(11,5),tileWorld=battle.map.node.convertToWorldSpaceAR(tp),reachable=!!(battle.g_data&&battle.g_data.psHash&&Object.prototype.hasOwnProperty.call(battle.g_data.psHash,(11<<8)|5));
        let commandButton=null;if(command&&command.seekCompByName){const b=command.seekCompByName(cc.Button,'bg/button0');if(b&&b.node&&b.node.activeInHierarchy&&b.interactable!==false)commandButton=b.node;}
        const commandBg=command&&command.seekNodeByName&&command.seekNodeByName('bg');
        const buttonPoint=commandButton?toCss(commandButton.convertToWorldSpaceAR(cc.v2(0,0))):null;
        let msgText=null,msgCancel=null,msgConfirm=null;if(msgBox&&msgBox.seekNodeByName){const label=msgBox.seekCompByName&&msgBox.seekCompByName(cc.Label,'bg0/label'),cancel=msgBox.seekCompByName&&msgBox.seekCompByName(cc.Button,'bg0/btns/button1'),confirm=msgBox.seekCompByName&&msgBox.seekCompByName(cc.Button,'bg0/btns/button0');msgText=label&&label.string;if(cancel&&cancel.node&&cancel.node.activeInHierarchy&&cancel.interactable!==false)msgCancel=nodeGeometry(cancel.node);if(confirm&&confirm.node&&confirm.node.activeInHierarchy&&confirm.interactable!==false)msgConfirm=nodeGeometry(confirm.node);}
        const sayPanel=say&&say.seekNodeByName&&say.seekNodeByName('Panel_cancel'),dialoguePanel=nodeGeometry(sayPanel);
        const strings=say&&Array.isArray(say._strings)?say._strings.map(String):[],cursor=Math.max(0,Math.min(Number(say&&say._index)||0,strings.length));let marker=-1;for(let i=Math.min(cursor-1,strings.length-1);i>=0;i--)if(strings[i].charAt(0)==='&'){marker=i;break}const dialogueSpeakerId=marker>=0?strings[marker].substring(1):null;
        return {ready:true,frame:cc.director.getTotalFrames(),round:battle.round(),camp:battle.curCamp(),driverDisabled:!!globalThis.__jojoManualPlayerAction,driverSuppressed:globalThis.__jojoManualDriverSuppressed||0,ctrlWaiting:!!battle._ctrlHelper,menuPending:!!globalThis.__jojoManualMenuEmitWrapped,menu:!!menu,dialogue:!!say,dialogueSpeakerId,dialogueText:say&&say._curLab&&say._curLab.string,dialogueRemaining:say&&say._nextString,dialogueTypingActive:!!(say&&say._handle),dialoguePanel,win:!!win,fight:!!fight,settlementLayers,msgBox:!!msgBox,msgBoxText:msgText,msgBoxCancel:msgCancel,msgBoxConfirm:msgConfirm,aiActorStarts:(globalThis.__jojoManualAiActorStarts||[]).slice(),command:!!command,commandAttackInteractable:!!commandButton,commandGeometry:{root:command&&nodeGeometry(command.node),background:nodeGeometry(commandBg),attackButton:nodeGeometry(commandButton)},runtimeViewport:{inner:[innerWidth,innerHeight],devicePixelRatio,canvasPixels:[canvas.width,canvas.height],canvasRect:[rect.left,rect.top,rect.width,rect.height],visible:[cc.view.getVisibleSize().width,cc.view.getVisibleSize().height],viewport:[vp.x,vp.y,vp.width,vp.height],scale:[cc.view._scaleX,cc.view._scaleY]},reachable,reachableKeys:battle.g_data&&battle.g_data.psHash?Object.keys(battle.g_data.psHash).map(Number):[],selectedUnit:battle.g_data&&battle.g_data.unit&&battle.g_data.unit.unit?battle.g_data.unit.unit().id():null,unit0:u0&&{x:u0.x(),y:u0.y(),hp:u0.hp_cur(),action:u0.unit().action(),geometry:nodeGeometry(u0.node),point:unitPoint(u0)},enemy484:u484&&{x:u484.x(),y:u484.y(),hp:u484.hp_cur(),exists:u484.isExist(),visible:u484.visible(),geometry:nodeGeometry(u484.node),point:unitPoint(u484)},destination:{x:11,y:5,local:[tp.x,tp.y],world:[tileWorld.x,tileWorld.y],point:toCss(tileWorld)},commandPoint:buttonPoint,inputEvents:(globalThis.__jojoManualInputEvents||[]).slice()};
      })()`;
      async function current(){return evaluate(manualState);}
      async function waitState(label,predicate,limit=12000){return poll(async()=>{const s=await current();observations.push({label,wallSeconds:(Date.now()-started)/1000,...s});return await predicate(s)?s:null;},label,Date.now()+limit);}
      async function waitStablePoint(label,geometryOf,valid,limit=5000){let prior=null;return poll(async()=>{const s=await current();observations.push({label,wallSeconds:(Date.now()-started)/1000,...s});if(!valid(s))return null;const geometry=geometryOf(s);if(!geometry||!geometry.cssCenter)return null;const signature=JSON.stringify({size:geometry.size,anchor:geometry.anchor,center:geometry.center,bottomLeft:geometry.bottomLeft,topRight:geometry.topRight,cssCenter:geometry.cssCenter});const stable=prior&&prior.frame!==s.frame&&prior.signature===signature;prior={frame:s.frame,signature};return stable?s:null;},`${label} stable geometry`,Date.now()+limit);}
      async function click(label,point,before){inputs.push({label,requestedFrame:before.frame,wallSeconds:(Date.now()-started)/1000,point,before});await cdpClick(client,point);}
      try{
        await waitState('round2-script-before-player-handoff',s=>s.round===2&&s.camp===3&&s.unit0?.x===10&&s.unit0?.y===5&&s.enemy484?.x===10&&s.enemy484?.y===6,145000);
        const installed=await evaluate(`(() => {let battle=null;(function v(n){if(!n)return;for(const c of n._components||[])if(cc.js.getClassName(c)==='BattleLayer')battle=c;n.children.forEach(v)})(cc.director.getScene());if(!battle||!battle._menu_button||!battle._menu_button.node)return {ok:false};globalThis.__jojoManualPlayerAction=true;globalThis.__jojoManualInputEvents=[];const node=battle._menu_button.node;if(!node.__jojoManualOriginalEmit){node.__jojoManualOriginalEmit=node.emit;node.emit=function(type,event,...rest){if(globalThis.__jojoManualPlayerAction&&type===cc.Node.EventType.TOUCH_END&&(!event||typeof event.getLocation!=='function')){globalThis.__jojoManualDriverSuppressed=(globalThis.__jojoManualDriverSuppressed||0)+1;return;}return node.__jojoManualOriginalEmit.call(this,type,event,...rest);};}globalThis.__jojoManualMenuEmitWrapped=true;if(!battle.__jojoManualDispatch){const original=battle.dispatchEvent;battle.dispatchEvent=function(name,data){if(name==='SELECT_UNIT_POINT')globalThis.__jojoManualInputEvents.push({frame:cc.director.getTotalFrames(),name,unitId:data&&data.unit&&data.unit.unit?data.unit.unit().id():null,pos:data&&data.pos?[data.pos.x,data.pos.y]:null});return original.apply(this,arguments);};battle.__jojoManualDispatch=true;}return {ok:true,frame:cc.director.getTotalFrames()};})()`);
        if(!installed.ok)throw Error('could not install harness-only automatic-menu suppression');seen.add('driver-disabled');
        let s=await waitState('manual-player-input-ready',x=>x.round===2&&x.camp===0&&x.ctrlWaiting&&!x.win&&!x.dialogue&&!x.menu&&x.unit0?.x===10&&x.unit0?.y===5&&x.enemy484?.x===10&&x.enemy484?.y===6,20000);await snapshot('manual-input-ready');
        await click('select-unit-0',s.unit0.point,s);s=await waitState('unit0-selected',x=>x.selectedUnit===0&&x.ctrlWaiting,5000);seen.add('unit0-selected');
        if(!s.reachable)throw Error(`declared destination 11,5 is not reachable: ${JSON.stringify(s.reachableKeys)}`);seen.add('destination-reachable');await snapshot('unit0-selected-reachable');
        await click('select-destination-11-5',s.destination.point,s);s=await waitState('unit0-moved-command-open',x=>x.unit0?.x===11&&x.unit0?.y===5&&x.command&&x.commandAttackInteractable,12000);seen.add('unit0-moved');seen.add('attack-command');await snapshot('unit0-moved-command');
        await click('select-attack-command',s.commandPoint,s);s=await waitState('attack-target-ready',x=>!x.command&&x.ctrlWaiting&&x.enemy484?.exists,5000);
        await click('select-enemy-484',s.enemy484.point,s);seen.add('enemy484-selected');await snapshot('enemy484-selected');
        s=await waitState('attack-damage-observed',x=>!x.enemy484?.exists||x.enemy484.hp<49,20000);
        s=await waitState('attack-settlement-complete',x=>!x.fight&&x.settlementLayers.length===0&&(x.ctrlWaiting||x.msgBox),20000);seen.add('attack-resolved');
        if(s.msgBox&&(s.msgBoxText!=='모든 부대의 명령을 종료하시겠습니까?'||!s.msgBoxCancel))throw Error(`unexpected post-action MsgBox: ${JSON.stringify({text:s.msgBoxText,cancel:s.msgBoxCancel})}`);
        seen.add('post-action-terminal');await snapshot(s.msgBox?'end-round-confirm-terminal':'player-input-terminal');
        if(followup){
          if(!s.msgBox||!s.msgBoxConfirm)throw Error(`round2 followup requires authored end-round confirmation: ${JSON.stringify(s)}`);
          await evaluate(`(()=>{let battle=null;(function v(n){if(!n)return;for(const c of n._components||[])if(cc.js.getClassName(c)==='BattleLayer')battle=c;n.children.forEach(v)})(cc.director.getScene());if(!battle||typeof battle.setCharInfoBykey!=='function')throw Error('BattleLayer setCharInfoBykey unavailable');globalThis.__jojoManualAiActorStarts=[];const seen=new WeakSet(),original=battle.setCharInfoBykey;battle.setCharInfoBykey=function(info,unit,key,value){if(this.curCamp()!==0&&key===13&&value&&value.state===14&&info&&info===this.g_charinfo&&!seen.has(info)){seen.add(info);globalThis.__jojoManualAiActorStarts.push({frame:cc.director.getTotalFrames(),camp:this.curCamp(),actorId:unit&&unit.unit&&unit.unit().id(),key,state:value.state});}return original.apply(this,arguments)};return true})()`);
          s=await waitStablePoint('automatic-end-round-confirm-layout',x=>x.msgBoxConfirm,x=>x.msgBox&&x.msgBoxText==='모든 부대의 명령을 종료하시겠습니까?'&&!!x.msgBoxConfirm);const playerCamp=s.camp;await click('confirm-automatic-end-round',s.msgBoxConfirm.cssCenter,s);
          await evaluate(`(()=>{if(globalThis.__jojoManualAllSyntheticTouchSuppressed)return true;const original=cc.Node.prototype.emit;cc.Node.prototype.emit=function(type,event,...rest){if(globalThis.__jojoManualPlayerAction&&type===cc.Node.EventType.TOUCH_END&&(!event||typeof event.getLocation!=='function')){globalThis.__jojoManualDriverSuppressed=(globalThis.__jojoManualDriverSuppressed||0)+1;return;}return original.call(this,type,event,...rest)};globalThis.__jojoManualAllSyntheticTouchSuppressed=true;return true})()`);
          s=await waitState('next-camp-observed',x=>!x.msgBox&&x.camp!==playerCamp,10000);seen.add('end-round-confirmed');seen.add('next-camp-observed');
          await snapshot('next-camp-observed');let movementCaptured=false,attackCaptured=false,settlementCaptured=false,terminal=null,dialogueAdvanced=false;
          while(Date.now()-started<maxWallMs-1000&&captures.length<12){
            const state=await evaluate(stateExpression),detail=await current(),moving=state.units.filter(u=>u.animation&&u.animation.startsWith('anime20')),attacking=state.units.filter(u=>u.animation&&/^anime(?:21|25|48|49)_/.test(u.animation)),settlement=state.layers.filter(name=>name==='MineUnitInfoLayer'||name==='OtherUnitInfoLayer');
            observations.push({label:'next-camp-followup',wallSeconds:(Date.now()-started)/1000,frame:state.frame,camp:state.camp,dialogue:state.dialogue,movingActorIds:moving.map(u=>u.id),attackingActorIds:attacking.map(u=>u.id),settlementLayers:settlement,aiActorStarts:detail.aiActorStarts});
            if(!movementCaptured&&moving.length){movementCaptured=true;await snapshot('next-camp-movement');}
            if(!attackCaptured&&attacking.length){attackCaptured=true;await snapshot('next-camp-attack');}
            if(!settlementCaptured&&settlement.length){settlementCaptured=true;await snapshot('next-camp-settlement');}
            if(state.dialogue){
              if(!continueFirstDialogue){terminal={kind:'blocking-dialogue',frame:state.frame,text:state.dialogueText,speakerId:state.dialogueSpeakerId};await snapshot('next-camp-blocking-dialogue');break;}
              if(dialogueAdvanced){terminal={kind:'blocking-subsequent-dialogue',frame:state.frame,text:state.dialogueText,speakerId:state.dialogueSpeakerId};await snapshot('next-camp-blocking-dialogue');break;}
              const expected='이것은 만민의 분노입니다!';if(detail.dialogueSpeakerId!=='32')throw Error(`unexpected first followup dialogue speaker: ${JSON.stringify(detail)}`);
              if(detail.dialogueRemaining===''&&!detail.dialogueTypingActive&&detail.dialogueText!==expected)throw Error(`completed first followup dialogue text changed: ${JSON.stringify(detail)}`);
              if(detail.dialogueText!==expected||detail.dialogueRemaining!==''||detail.dialogueTypingActive||!detail.dialoguePanel){await delay(16);continue;}
              await snapshot('next-camp-dialogue32-complete');const fresh=await waitStablePoint('first-followup-dialogue-layout',x=>x.dialoguePanel,x=>x.dialogue&&x.dialogueSpeakerId==='32'&&x.dialogueText===expected&&x.dialogueRemaining===''&&!x.dialogueTypingActive&&!!x.dialoguePanel);await click('close-first-followup-dialogue',fresh.dialoguePanel.cssCenter,fresh);dialogueAdvanced=true;await waitState('first-followup-dialogue-closed',x=>!x.dialogue,5000);continue;
            }
            if((!continueFirstDialogue||dialogueAdvanced)&&detail.aiActorStarts.length>=2){terminal={kind:'next-ai-actor-started',frame:state.frame,completedActorId:detail.aiActorStarts[0].actorId,nextActorId:detail.aiActorStarts[1].actorId,aiActorStarts:detail.aiActorStarts};await snapshot('next-camp-first-action-complete');break;}
            if((!continueFirstDialogue||dialogueAdvanced)&&detail.aiActorStarts.length&&state.camp!==detail.aiActorStarts[0].camp){terminal={kind:'camp-transition-after-first-ai-action',frame:state.frame,completedActorId:detail.aiActorStarts[0].actorId,nextCamp:state.camp,aiActorStarts:detail.aiActorStarts};await snapshot('next-camp-first-action-complete');break;}
            await delay(16);
          }
          if(!terminal)throw Error(`next-camp followup terminal not observed: ${JSON.stringify({movementCaptured,attackCaptured,settlementCaptured})}`);
          seen.add('followup-terminal');observations.push({label:'followup-terminal',...terminal,movementCaptured,attackCaptured,settlementCaptured});
        }
      }catch(error){failure=error.message;}
      const missingCaptures=wanted.filter(name=>!seen.has(name));const finalState=await current().catch(()=>null);
      fs.writeFileSync(path.join(outputRoot,'screens.json'),JSON.stringify({contract:continueFirstDialogue?'source-yingchuan-normal-clock-round2-first-combat-v1':followup?'source-yingchuan-normal-clock-round2-followup-v1':'source-yingchuan-normal-clock-single-player-action-v1',evidenceKind:'actual-source-renderer-direct-battle-bootstrap-with-cdp-pointer-input',sourceRoot,scenario:'S_00',timeScale:1,maxWallMs,semanticMode,complete:!failure&&missingCaptures.length===0,failure,missingCaptures,requestedAction:{unitId:0,from:[10,5],destination:[11,5],command:'attack',targetId:484,target:[10,6]},followup:{enabled:followup,automaticEndRoundConfirm:followup?'actual-CDP-pointer-button0-no-entrust':false,firstDialogueAdvance:continueFirstDialogue?{speakerId:'32',text:'이것은 만민의 분노입니다!',input:'actual-CDP-pointer-after-natural-completion'}:false,completionEvidence:'next-ai-actor-start-or-camp-transition',expectedDamage:null,expectedNextActor:null},automaticDriverHandoff:{mechanism:'harness-only rejection of full-trace synthetic menu_button node.emit; CDP touch events remain routed through Cocos input',installed:seen.has('driver-disabled'),suppressedCalls:finalState&&finalState.driverSuppressed},inputs,observations,finalState,bootstrap:{route:'HallLayer.jumpScene(0)',seededBattleUnits:[0],normalDialogueInput:'SayLayer Panel_cancel TOUCH_END',fixture:false,fullCampaignEntry:false},captures},null,2)+'\n');
      await childExit;if(!fs.existsSync(trace))throw Error('source single-player-action trace was not flushed');
      const complete=!failure&&missingCaptures.length===0;console.log(`SOURCE_YINGCHUAN_${semanticMode.replaceAll('-','_').toUpperCase()}_${complete?'OK':'PARTIAL'} ${captures.length}${failure?` failure=${failure}`:''}`);if(!complete)process.exitCode=1;return;
    }
    if(semanticMode==='round2-handoff'){
      const wanted=['first-fire-13-5','second-fire-cluster','speaker157-dialogue','firestorm','enemy484-panic','reinforcements-shown','reinforcements-positioned','win-condition','post-win-condition-camp0'];
      const seen=new Set(),transitions=[];let prior='';
      const gate=(x,y)=>x*256+y;
      async function captureSemantic(name,state){const image=await client.send('Page.captureScreenshot',{format:'png',fromSurface:true}),file=`source-${name}.png`;fs.writeFileSync(path.join(outputRoot,file),Buffer.from(image.data,'base64'));captures.push({wallSeconds:(Date.now()-started)/1000,file,semantic:name,...state});seen.add(name);}
      while(Date.now()-started<Math.min(maxWallMs-1000,149000)&&seen.size<wanted.length){
        const evaluated=await client.send('Runtime.evaluate',{expression:stateExpression,returnByValue:true});if(evaluated.exceptionDetails)throw Error(JSON.stringify(evaluated.exceptionDetails));const state=evaluated.result.value,u=id=>state.units.find(x=>x.id===id),g=new Set(state.gateKeys||[]);
        const key=JSON.stringify([state.round,state.camp,state.dialogue,state.dialogueSpeakerId,state.dialogueText,state.layers,state.gateKeys,[0,3,32,33,157,258,259,484].map(id=>{const x=u(id);return x&&[id,x.x,x.y,x.direction,x.action,x.animation,x.visible,x.exists]})]);
        if(key!==prior){transitions.push({wallSeconds:(Date.now()-started)/1000,frame:state.frame,round:state.round,camp:state.camp,dialogue:state.dialogue,dialogueSpeakerId:state.dialogueSpeakerId,dialogueText:state.dialogueText,layers:state.layers,gateKeys:state.gateKeys,units:state.units});prior=key;}
        if(!seen.has(wanted[0])&&g.has(gate(13,5)))await captureSemantic(wanted[0],state);
        else if(!seen.has(wanted[1])&&g.has(gate(6,4))&&g.has(gate(7,5)))await captureSemantic(wanted[1],state);
        else if(!seen.has(wanted[2])&&state.dialogue&&state.dialogueText==='헤헤, 하!<br/>그럼, 이건 어때?')await captureSemantic(wanted[2],state);
        else if(!seen.has(wanted[3])&&[[14,4],[15,7],[13,9],[5,8],[6,9]].every(([x,y])=>g.has(gate(x,y))))await captureSemantic(wanted[3],state);
        else if(!seen.has(wanted[4])&&u(484)?.animation==='anime8')await captureSemantic(wanted[4],state);
        else if(!seen.has(wanted[5])&&[u(0),u(258),u(259)].every(x=>x?.visible===true))await captureSemantic(wanted[5],state);
        else if(!seen.has(wanted[6])&&u(0)?.x===8&&u(0)?.y===2&&u(258)?.x===7&&u(258)?.y===2&&u(259)?.x===8&&u(259)?.y===1)await captureSemantic(wanted[6],state);
        else if(!seen.has(wanted[7])&&state.layers.includes('WinConditionsLayer'))await captureSemantic(wanted[7],state);
        else if(!seen.has(wanted[8])&&seen.has(wanted[7])&&state.round===2&&state.camp===0&&!state.dialogue&&!state.layers.includes('WinConditionsLayer'))await captureSemantic(wanted[8],state);
        await delay(8);
      }
      const missingCaptures=wanted.filter(name=>!seen.has(name));
      fs.writeFileSync(path.join(outputRoot,'screens.json'),JSON.stringify({contract:'source-yingchuan-normal-clock-round2-handoff-v1',evidenceKind:'actual-source-renderer-direct-battle-bootstrap',sourceRoot,scenario:'S_00',timeScale:1,maxWallMs,semanticMode,complete:missingCaptures.length===0,missingCaptures,transitions,bootstrap:{route:'HallLayer.jumpScene(0)',seededBattleUnits:[0],normalDialogueInput:'SayLayer Panel_cancel TOUCH_END',fixture:false,fullCampaignEntry:false},captures},null,2)+'\n');
      await childExit;if(!fs.existsSync(trace))throw Error('source round2-handoff trace was not flushed');
      if(missingCaptures.length){console.log(`SOURCE_YINGCHUAN_ROUND2_HANDOFF_PARTIAL ${captures.length} missing=${missingCaptures.join(',')}`);return;}
      console.log(`SOURCE_YINGCHUAN_ROUND2_HANDOFF_OK ${captures.length}`);return;
    }
    const sampleIntervalMs = maxWallMs > 30000 ? 10000 : 5000;
    const targets = Array.from({ length: Math.min(8, Math.ceil(maxWallMs / sampleIntervalMs)) }, (_, index) => index * sampleIntervalMs);
    for (const targetMs of targets) {
      await delay(Math.max(0, started + targetMs - Date.now()));
      const evaluated = await client.send('Runtime.evaluate', { expression: stateExpression, returnByValue: true });
      if (evaluated.exceptionDetails) throw new Error(JSON.stringify(evaluated.exceptionDetails));
      const image = await client.send('Page.captureScreenshot', { format: 'png', fromSurface: true });
      const file = `source-${String(targetMs / 1000).padStart(2, '0')}s.png`;
      fs.writeFileSync(path.join(outputRoot, file), Buffer.from(image.data, 'base64'));
      captures.push({ wallSeconds: targetMs / 1000, file, ...evaluated.result.value });
    }
    fs.writeFileSync(path.join(outputRoot, 'screens.json'), JSON.stringify({ contract: 'source-yingchuan-normal-clock-sparse-screens-v1', evidenceKind: 'actual-source-renderer-direct-battle-bootstrap', sourceRoot, scenario: 'S_00', timeScale: 1, maxWallMs, sampleIntervalMs, bootstrap: { route: 'HallLayer.jumpScene(0)', seededBattleUnits: [0], normalDialogueInput: 'SayLayer Panel_cancel TOUCH_END', fixture: false, fullCampaignEntry: false }, captures }, null, 2) + '\n');
    console.log(`SOURCE_YINGCHUAN_SCREENS_OK ${captures.length} ${outputRoot}`);
  } finally {
    clearTimeout(watchdog);
    if (client) client.close();
    try { process.kill(-child.pid, 'SIGTERM'); } catch {}
    await delay(500);
    try { process.kill(-child.pid, 'SIGKILL'); } catch {}
    fs.closeSync(logFd);
  }
})().catch(error => { console.error(error.stack || error); process.exitCode = 1; });
