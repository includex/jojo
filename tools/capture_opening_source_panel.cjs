#!/usr/bin/env node
'use strict';
// Capture the naturally reached first R_00 DialogueLayer panel from original Cocos.
const {spawn} = require('node:child_process');
const crypto = require('node:crypto');
const fs = require('node:fs');
const http = require('node:http');
const path = require('node:path');

const sourceRoot = path.resolve(process.env.JOJO_SOURCE_ROOT || path.join(__dirname, '../../jojo_mobile/sgccz-desktop'));
const outputRoot = path.resolve(process.argv[2] || process.env.JOJO_CAPTURE_OUT || path.join(process.cwd(), 'build/reports/opening-source-panel'));
const port = Number(process.env.JOJO_CDP_PORT || 9400 + process.pid % 400);
const deadlineMs = Number(process.env.JOJO_CAPTURE_DEADLINE_MS || 55000);
if (!Number.isFinite(port) || !Number.isFinite(deadlineMs) || deadlineMs <= 0 || deadlineMs > 60000) {
  throw new Error('JOJO_CDP_PORT must be numeric and JOJO_CAPTURE_DEADLINE_MS must be 1..60000');
}
fs.mkdirSync(outputRoot, {recursive: true});
for (const name of ['source-street-panel.rgba', 'source-street-panel.json']) {
  fs.rmSync(path.join(outputRoot, name), {force: true});
}

const delay = ms => new Promise(resolve => setTimeout(resolve, ms));
const getJson = url => new Promise((resolve, reject) => {
  const request = http.get(url, response => {
    let body = '';
    response.on('data', chunk => body += chunk);
    response.on('end', () => { try { resolve(JSON.parse(body)); } catch (error) { reject(error); } });
  });
  request.setTimeout(1000, () => request.destroy(new Error('CDP HTTP timeout')));
  request.on('error', reject);
});
async function poll(operation, label, expiresAt) {
  let last;
  while (Date.now() < expiresAt) {
    try { last = await operation(); if (last) return last; } catch (error) { last = error; }
    await delay(40);
  }
  throw new Error(`timeout waiting for ${label}: ${last || ''}`);
}
async function connect(url) {
  const socket = new WebSocket(url);
  await new Promise((resolve, reject) => {
    socket.addEventListener('open', resolve, {once: true});
    socket.addEventListener('error', reject, {once: true});
  });
  let nextId = 0;
  const pending = new Map();
  socket.addEventListener('message', event => {
    const message = JSON.parse(event.data), waiter = pending.get(message.id);
    if (!waiter) return;
    pending.delete(message.id);
    message.error ? waiter.reject(new Error(JSON.stringify(message.error))) : waiter.resolve(message.result);
  });
  return {
    send(method, params = {}) {
      return new Promise((resolve, reject) => {
        const id = ++nextId;
        pending.set(id, {resolve, reject});
        socket.send(JSON.stringify({id, method, params}));
      });
    },
    close() { socket.close(); },
  };
}

const readyExpression = `(()=>{if(!globalThis.cc||!cc.director)return false;const q=[cc.director.getScene()];while(q.length){const n=q.shift(),c=n.getComponent&&n.getComponent('DialogueLayer');if(c&&n.activeInHierarchy&&c._curBg&&c._curLab&&c._strings&&c._strings.length){const speaker=c.seekCompByName(cc.Label,'label',c._curBg).string,text=c._curLab.string||c._nextString||'';if(speaker==='병사 '&&text.includes('대장님, 서둘러야 해요!'))return {speaker,text,bg:c._curBg.name};}q.push(...n.children)}return false})()`;
const isolateExpression = `(()=>{const q=[cc.director.getScene()];let layer=null;while(q.length&&!layer){const n=q.shift(),c=n.getComponent&&n.getComponent('DialogueLayer');if(c&&n.activeInHierarchy&&c._curBg)layer=c;q.push(...n.children)}if(!layer)throw Error('DialogueLayer unavailable');const bg=layer._curBg,panel=bg.children.find(n=>n.name==='bg2'),face=bg.children.find(n=>n.name==='face'),label=bg.children.find(n=>n.name==='label'),rich=panel&&panel.children.find(n=>n.name==='richtext'),sprite=panel&&panel.getComponent(cc.Sprite),speaker=layer.seekCompByName(cc.Label,'label',bg).string,text=layer._curLab.string||layer._nextString||'';if(speaker!=='병사 '||!text.includes('대장님, 서둘러야 해요!'))throw Error('first R_00 dialogue changed before isolation');if(!panel||!sprite||!sprite.spriteFrame)throw Error('DialogueLayer panel unavailable');const desc=(n,p)=>{for(let x=n;x;x=x.parent)if(x===p)return true;return false};const all=[cc.director.getScene()];while(all.length){const n=all.shift();if(!desc(n,bg)&&!desc(bg,n))n.opacity=0;all.push(...n.children)}if(face)face.active=false;if(label)label.active=false;if(rich)rich.active=false;panel.active=true;const frame=sprite.spriteFrame,world=panel.convertToWorldSpaceAR(cc.v2(0,0));return {stage:'panel',speaker,text,bg:bg.name,panel:{name:panel.name,world:[world.x,world.y],size:[panel.width,panel.height],anchor:[panel.anchorX,panel.anchorY],type:sprite.type,frame:{name:frame.name,rect:[frame._rect.x,frame._rect.y,frame._rect.width,frame._rect.height],rotated:!!frame._rotated}}}})()`;
const readbackExpression = `new Promise(resolve=>{let done=false;const capture=()=>{if(done)return;done=true;try{const gl=cc.game._renderContext,w=gl.drawingBufferWidth,h=gl.drawingBufferHeight,p=new Uint8Array(w*h*4);gl.readPixels(0,0,w,h,gl.RGBA,gl.UNSIGNED_BYTE,p);let s='';for(let i=0;i<p.length;i+=32768)s+=String.fromCharCode(...p.subarray(i,Math.min(i+32768,p.length)));resolve({source:'EVENT_AFTER_DRAW',width:w,height:h,data:btoa(s)});}catch(error){resolve({error:String(error)})}};cc.director.once(cc.Director.EVENT_AFTER_DRAW,capture);setTimeout(()=>{if(!done){done=true;resolve({error:'timeout waiting for natural EVENT_AFTER_DRAW'})}},1000)})`;

(async () => {
  const startedAt = new Date(), expiresAt = Date.now() + deadlineMs;
  try {
    await getJson(`http://127.0.0.1:${port}/json/list`);
    throw new Error(`CDP port ${port} is already in use`);
  } catch (error) {
    if (error.code !== 'ECONNREFUSED') throw error;
  }
  const logPath = path.join(outputRoot, 'source-street-panel-process.log');
  const log = fs.openSync(logPath, 'w');
  const child = spawn('npm', ['exec', '--', 'electron', '.', '--verify-python-source', `--remote-debugging-port=${port}`], {
    cwd: sourceRoot, stdio: ['ignore', log, log], detached: true,
  });
  let childExit = null;
  child.once('exit', (code, signal) => { childExit = {code, signal}; });
  let client;
  const deadline = setTimeout(() => {
    try { process.kill(-child.pid, 'SIGTERM'); } catch {}
    setTimeout(() => { try { process.kill(-child.pid, 'SIGKILL'); } catch {} process.exit(124); }, 250);
  }, deadlineMs);
  try {
    const page = await poll(async () => {
      if (childExit) throw new Error(`source Electron exited before CDP readiness: ${JSON.stringify(childExit)}`);
      const pages = await getJson(`http://127.0.0.1:${port}/json/list`);
      return pages.find(item => item.type === 'page' && item.webSocketDebuggerUrl);
    }, 'Electron CDP page', expiresAt);
    if (page.url !== 'sgccz://game/electron/index.html') throw new Error(`unexpected source CDP page: ${page.url}`);
    client = await connect(page.webSocketDebuggerUrl);
    const evaluate = async expression => {
      const response = await client.send('Runtime.evaluate', {expression, returnByValue: true, awaitPromise: true});
      if (response.exceptionDetails) throw new Error(`CDP evaluation failed: ${JSON.stringify(response.exceptionDetails)}`);
      return response.result.value;
    };
    const readyState = await poll(() => evaluate(readyExpression), 'natural R_00 first dialogue', expiresAt);
    const isolation = await evaluate(isolateExpression);
    if (!isolation || isolation.stage !== 'panel' || isolation.speaker !== '병사 ' ||
        !isolation.text.includes('대장님, 서둘러야 해요!')) {
      throw new Error(`source panel isolation state rejected: ${JSON.stringify(isolation)}`);
    }
    await evaluate('new Promise(resolve=>requestAnimationFrame(()=>requestAnimationFrame(resolve)))');
    const raw = await evaluate(readbackExpression);
    if (!raw || !raw.data) throw new Error(`source panel readback failed: ${JSON.stringify(raw)}`);
    const bytes = Buffer.from(raw.data, 'base64'), sha256 = crypto.createHash('sha256').update(bytes).digest('hex');
    if (raw.source !== 'EVENT_AFTER_DRAW') throw new Error(`source panel capture used rejected trigger: ${raw.source}`);
    if (bytes.length !== raw.width * raw.height * 4) throw new Error(`source panel byte count mismatch: ${bytes.length}`);
    fs.writeFileSync(path.join(outputRoot, 'source-street-panel.rgba'), bytes);
    fs.writeFileSync(path.join(outputRoot, 'source-street-panel.json'), JSON.stringify({
      fixture: 'R_00 natural first DialogueLayer panel isolation',
      capture: 'direct gl.readPixels after naturally reached live DialogueLayer; visibility isolated after readiness',
      evidenceKind: 'actual-source-renderer-panel-isolation', encoding: 'srgb-encoded-rgba8', origin: 'bottom-left',
      sourceRoot, startedAt: startedAt.toISOString(), completedAt: new Date().toISOString(), readyState, isolation,
      raw: {width: raw.width, height: raw.height, byteLength: bytes.length, sha256, trigger: raw.source},
    }, null, 2) + '\n');
    console.log(`SOURCE_OPENING_PANEL_CAPTURE_OK ${raw.width}x${raw.height} sha256=${sha256}`);
  } finally {
    clearTimeout(deadline);
    if (client) client.close();
    try { process.kill(-child.pid, 'SIGTERM'); } catch {}
    await delay(500);
    try { process.kill(-child.pid, 'SIGKILL'); } catch {}
    fs.closeSync(log);
  }
})().catch(error => { console.error(error.stack || error); process.exitCode = 1; });
