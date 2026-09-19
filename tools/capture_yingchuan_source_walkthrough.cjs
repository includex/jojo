#!/usr/bin/env node
'use strict';

const fs = require('node:fs');
const http = require('node:http');
const path = require('node:path');
const { spawn } = require('node:child_process');

const sourceRoot = path.resolve(process.argv[2] || '../jojo_mobile/sgccz-desktop');
const outputRoot = path.resolve(process.argv[3] || 'build/reports/yingchuan-source-screens');
const maxWallMs = Number(process.argv[4] || 30000);
if (!Number.isInteger(maxWallMs) || maxWallMs < 10000 || maxWallMs > 60000) throw new Error('duration must be an integer from 10000 through 60000 ms');
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

const stateExpression = `(() => {
  const scene=globalThis.cc&&cc.director&&cc.director.getScene(); let battle=null; const units=[]; const layers=[];
  if(!scene)return {ready:false};
  (function visit(node){if(!node)return; for(const component of node._components||[]){const name=cc.js.getClassName(component); if(name==='BattleLayer')battle=component; if(/Layer$/.test(name)&&node.activeInHierarchy)layers.push(name);} node.children.forEach(visit)})(scene);
  if(battle)for(const u of Object.values(battle._unitSet||{}).filter(Boolean)){const source=u.unit&&u.unit(),node=u.node,n=node&&node.getChildByName('mask')&&node.getChildByName('mask').getChildByName('node'),a=n&&n.getComponent(cc.Animation),playing=a&&a._nameToState&&Object.entries(a._nameToState).find(([,s])=>s&&s.isPlaying);units.push({id:source&&source.id?source.id():null,index:u.index?u.index():null,x:u.x?u.x():null,y:u.y?u.y():null,direction:u.dir?u.dir():null,action:source&&source.action?source.action():null,animation:playing?playing[0]:null});}
  const dialogue=layers.includes('SayLayer');
  const camera=battle&&battle._scrollView&&battle._scrollView.content&&battle._scrollView.content.position;
  return {ready:!!battle,frame:cc.director.getTotalFrames?cc.director.getTotalFrames():null,scene:scene.name,round:battle&&battle.round?battle.round():null,camp:battle&&battle.curCamp?battle.curCamp():null,camera:camera?[camera.x,camera.y]:null,dialogue,layers:[...new Set(layers)],units:units.filter(u=>[32,3,33,474,235,477,334].includes(u.id))};
})()`;

(async () => {
  fs.rmSync(outputRoot, { recursive: true, force: true });
  fs.mkdirSync(outputRoot, { recursive: true });
  const trace = path.join(outputRoot, 'source-full-trace.json');
  const logFd = fs.openSync(path.join(outputRoot, 'source-process.log'), 'w');
  const child = spawn('npm', ['exec', '--', 'electron', '.', '--battle', '--scenario=S_00', `--full-battle-trace=${trace}`, '--full-battle-time-scale=1', `--full-battle-max-wall-ms=${maxWallMs}`, '--full-battle-seed=1000', '--full-battle-math-seed=305419896', `--remote-debugging-port=${port}`], { cwd: sourceRoot, stdio: ['ignore', logFd, logFd], detached: true });
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
