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
const stage = process.env.JOJO_CAPTURE_STAGE || 'panel';
if (!['panel', 'portrait', 'speaker', 'text'].includes(stage)) throw new Error('JOJO_CAPTURE_STAGE must be panel, portrait, speaker, or text');
const stem = `source-street-${stage}`;
const port = Number(process.env.JOJO_CDP_PORT || 9400 + process.pid % 400);
const deadlineMs = Number(process.env.JOJO_CAPTURE_DEADLINE_MS || 55000);
const holdVerificationExit = path.join(__dirname, 'hold_source_verification_exit.cjs');
if (!Number.isFinite(port) || !Number.isFinite(deadlineMs) || deadlineMs <= 0 || deadlineMs > 60000) {
  throw new Error('JOJO_CDP_PORT must be numeric and JOJO_CAPTURE_DEADLINE_MS must be 1..60000');
}
fs.mkdirSync(outputRoot, {recursive: true});
for (const name of [`${stem}.rgba`, `${stem}.json`]) {
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
    try { last = await operation(); if (last) return last; } catch (error) { if (error.fatal) throw error; last = error; }
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
  socket.addEventListener('close', () => {
    const error = new Error('CDP socket closed before capture completed');
    error.fatal = true;
    for (const waiter of pending.values()) waiter.reject(error);
    pending.clear();
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

const loginReadyExpression = `(()=>{if(!globalThis.cc||!cc.director)return false;const scene=cc.director.getScene(),q=scene?[...scene.children]:[];let login=null,startLayer=false;while(q.length){const n=q.shift(),c=n.getComponent&&n.getComponent('Login'),s=n.getComponent&&n.getComponent('StartItemLayer');if(c&&n.activeInHierarchy)login=c;if(s&&n.activeInHierarchy)startLayer=true;q.push(...n.children)}return !!(login&&startLayer&&typeof login.dispatchEvent==='function')})()`;
const startExpression = `(()=>{const scene=cc.director.getScene(),q=scene?[...scene.children]:[];while(q.length){const n=q.shift(),login=n.getComponent&&n.getComponent('Login');if(login&&n.activeInHierarchy&&typeof login.dispatchEvent==='function'){login._checkFloor=()=>0;login.dispatchEvent('START_ITEM_CHOICE',0);return true}q.push(...n.children)}throw Error('Login unavailable')})()`;
const scenarioReadyExpression = `(()=>{if(!globalThis.cc||!cc.director)return {ready:false,scene:null,texts:[]};const scene=cc.director.getScene(),q=scene?[...scene.children]:[],texts=[];let ready=false,dialogueStarted=false,cssPoint=null;while(q.length){const n=q.shift(),label=n.getComponent&&n.getComponent(cc.Label),rich=n.getComponent&&n.getComponent(cc.RichText),value=label&&label.string||rich&&rich.string||'';if(value)texts.push(value);if(n.activeInHierarchy&&value.includes('조조가 군대를 일으키다')){ready=true;const p=n.convertToWorldSpaceAR(cc.v2((.5-n.anchorX)*n.width,(.5-n.anchorY)*n.height)),v=cc.view.getVisibleSize(),r=document.getElementById('GameCanvas').getBoundingClientRect();cssPoint=[r.left+p.x/v.width*r.width,r.top+(v.height-p.y)/v.height*r.height]}const dialogue=n.getComponent&&n.getComponent('DialogueLayer');if(dialogue&&n.activeInHierarchy)dialogueStarted=true;q.push(...n.children)}return {ready,dialogueStarted,cssPoint,scene:scene&&scene.name,texts:texts.slice(0,40)}})()`;
const readyExpression = `(()=>{if(!globalThis.cc||!cc.director)return false;const stage=${JSON.stringify(stage)},expected='대장님, 서둘러야 해요!',q=[cc.director.getScene()];while(q.length){const n=q.shift(),c=n.getComponent&&n.getComponent('DialogueLayer');if(c&&n.activeInHierarchy&&c._curBg&&c._curLab&&c._strings&&c._strings.length){const labelNode=c._curBg.children.find(n=>n.name==='label'),label=labelNode&&labelNode.getComponent(cc.Label),speaker=label&&label.string,renderedBody=c._curLab.string||'',text=renderedBody||c._nextString||'',face=c._curBg.children.find(n=>n.name==='face'),sprite=face&&face.getComponent(cc.Sprite),frame=sprite&&sprite.spriteFrame,texture=frame&&(frame.getTexture?frame.getTexture():frame._texture),original=frame&&(frame.getOriginalSize?frame.getOriginalSize():frame._originalSize),max=original&&Math.max(original.width,original.height),sized=face&&max&&Math.abs(face.width-120*original.width/max)<.001&&Math.abs(face.height-120*original.height/max)<.001,labelTexture=label&&(label._ttfTexture||(label._frame&&label._frame._texture)),labelGl=labelTexture&&(labelTexture._glID||(labelTexture._texture&&labelTexture._texture._glID)),labelReady=label&&label.string==='병사 '&&label.fontSize>0&&labelNode.width>0&&labelNode.height>0&&labelTexture&&labelTexture.width>0&&labelTexture.height>0&&labelGl,faceReady=frame&&frame.name==='181'&&original&&original.width===192&&original.height===240&&sized&&texture&&texture.loaded&&texture.width>0&&texture.height>0,segments=(c._curLab._labelSegments||[]).map(node=>({node,label:node.getComponent&&node.getComponent(cc.Label)})).filter(x=>x.label),rendered=segments.map(x=>x.label.string).join(''),bodyReady=renderedBody===expected&&rendered===expected&&c._content===expected&&c._nextString===''&&c._handle==null&&segments.length>0&&segments.every(x=>{const t=x.label._ttfTexture||(x.label._frame&&x.label._frame._texture),g=t&&(t._glID||(t._texture&&t._texture._glID));return t&&t.width>0&&t.height>0&&g});if(speaker==='병사 '&&(stage==='text'?bodyReady:text.includes(expected))&&(stage==='panel'||faceReady)&&(!['speaker','text'].includes(stage)||labelReady))return {speaker,text,renderedText:rendered,bg:c._curBg.name,faceFrame:frame&&frame.name,labelTextureSize:labelTexture&&[labelTexture.width,labelTexture.height],bodySegmentCount:segments.length,schedulerHandle:c._handle==null?null:'active',remaining:c._nextString==null?null:c._nextString,content:c._content==null?null:c._content};}q.push(...n.children)}return false})()`;
const isolateExpression = `(()=>{const stage=${JSON.stringify(stage)},wantFace=stage!=='panel',wantSpeaker=stage==='speaker'||stage==='text',wantBody=stage==='text',q=[cc.director.getScene()];let layer=null;while(q.length&&!layer){const n=q.shift(),c=n.getComponent&&n.getComponent('DialogueLayer');if(c&&n.activeInHierarchy&&c._curBg)layer=c;q.push(...n.children)}if(!layer)throw Error('DialogueLayer unavailable');const bg=layer._curBg,panel=bg.children.find(n=>n.name==='bg2'),face=bg.children.find(n=>n.name==='face'),labelNode=bg.children.find(n=>n.name==='label'),label=labelNode&&labelNode.getComponent(cc.Label),outline=labelNode&&labelNode.getComponent(cc.LabelOutline),rich=panel&&panel.children.find(n=>n.name==='richtext'),richComp=rich&&rich.getComponent(cc.RichText),sprite=panel&&panel.getComponent(cc.Sprite),faceSprite=face&&face.getComponent(cc.Sprite),speaker=label&&label.string,renderedBody=layer._curLab.string||'',text=renderedBody||layer._nextString||'',bodySegments=(richComp&&richComp._labelSegments||[]).map(node=>({node,label:node.getComponent&&node.getComponent(cc.Label)})).filter(x=>x.label),renderedText=bodySegments.map(x=>x.label.string).join('');if(speaker!=='병사 '||(wantBody?renderedBody!=='대장님, 서둘러야 해요!'||renderedText!==renderedBody||layer._content!==renderedBody||layer._nextString!==''||layer._handle!=null:!text.includes('대장님, 서둘러야 해요!')))throw Error('first R_00 dialogue changed before isolation');if(!panel||!sprite||!sprite.spriteFrame)throw Error('DialogueLayer panel unavailable');const faceFrame=faceSprite&&faceSprite.spriteFrame,faceTexture=faceFrame&&(faceFrame.getTexture?faceFrame.getTexture():faceFrame._texture),faceOriginal=faceFrame&&(faceFrame.getOriginalSize?faceFrame.getOriginalSize():faceFrame._originalSize),faceMax=faceOriginal&&Math.max(faceOriginal.width,faceOriginal.height),faceSized=face&&faceMax&&Math.abs(face.width-120*faceOriginal.width/faceMax)<.001&&Math.abs(face.height-120*faceOriginal.height/faceMax)<.001,labelTexture=label&&(label._ttfTexture||(label._frame&&label._frame._texture)),labelGl=labelTexture&&(labelTexture._glID||(labelTexture._texture&&labelTexture._texture._glID));if(wantFace&&(!faceFrame||faceFrame.name!=='181'||!faceOriginal||faceOriginal.width!==192||faceOriginal.height!==240||!faceSized||!faceTexture||!faceTexture.loaded||faceTexture.width<=0||faceTexture.height<=0))throw Error('unit 181 portrait is not loaded or sized');if(wantBody&&(!bodySegments.length||bodySegments.some(x=>{const t=x.label._ttfTexture||(x.label._frame&&x.label._frame._texture),g=t&&(t._glID||(t._texture&&t._texture._glID));return !t||t.width<=0||t.height<=0||!g})))throw Error('complete body text is not rendered/uploaded');if(wantSpeaker&&(!label||label.string!=='병사 '||label.fontSize<=0||labelNode.width<=0||labelNode.height<=0||!labelTexture||labelTexture.width<=0||labelTexture.height<=0||!labelGl))throw Error('soldier speaker label is not rendered/uploaded');const desc=(n,p)=>{for(let x=n;x;x=x.parent)if(x===p)return true;return false},nodeInfo=n=>{const w=n.convertToWorldSpaceAR(cc.v2(0,0));return {name:n.name,position:[n.x,n.y],world:[w.x,w.y],size:[n.width,n.height],anchor:[n.anchorX,n.anchorY],scale:[n.scaleX,n.scaleY],opacity:n.opacity,color:[n.color.r,n.color.g,n.color.b,n.color.a]}};const all=[cc.director.getScene()];while(all.length){const n=all.shift();if(!desc(n,bg)&&!desc(bg,n))n.opacity=0;all.push(...n.children)}if(face)face.active=wantFace;if(labelNode)labelNode.active=wantSpeaker;if(rich)rich.active=wantBody;panel.active=true;const frame=sprite.spriteFrame,canvas=document.createElement('canvas'),ctx=canvas.getContext('2d'),font=label.fontSize+'px '+(label.fontFamily||'Arial');ctx.font=font;const measured=ctx.measureText(label.string),fontAsset=label.font;return {stage,speaker,text,scheduler:{handle:layer._handle==null?null:'active',remaining:layer._nextString==null?null:layer._nextString,content:layer._content==null?null:layer._content},bg:bg.name,panel:{node:nodeInfo(panel),type:sprite.type,frame:{name:frame.name,rect:[frame._rect.x,frame._rect.y,frame._rect.width,frame._rect.height],rotated:!!frame._rotated}},portrait:wantFace?{node:nodeInfo(face),srcBlendFactor:faceSprite.srcBlendFactor,dstBlendFactor:faceSprite.dstBlendFactor,frame:{name:faceFrame.name,uuid:faceFrame._uuid||faceFrame._id||null,rect:[faceFrame._rect.x,faceFrame._rect.y,faceFrame._rect.width,faceFrame._rect.height],original:[faceOriginal.width,faceOriginal.height],offset:[faceFrame._offset.x,faceFrame._offset.y],rotated:!!faceFrame._rotated},texture:{name:faceTexture.name||'',url:faceTexture.url||faceTexture.nativeUrl||faceTexture._nativeUrl||'',width:faceTexture.width,height:faceTexture.height,loaded:!!faceTexture.loaded,minFilter:faceTexture._minFilter,magFilter:faceTexture._magFilter,wrapS:faceTexture._wrapS,wrapT:faceTexture._wrapT}}:null,bodyText:wantBody?{node:nodeInfo(rich),string:richComp.string,fontSize:richComp.fontSize,lineHeight:richComp.lineHeight,maxWidth:richComp.maxWidth,horizontalAlign:richComp.horizontalAlign,verticalAlign:richComp.verticalAlign==null?null:richComp.verticalAlign,segments:bodySegments.map(x=>{const t=x.label._ttfTexture||(x.label._frame&&x.label._frame._texture),c=x.label._assemblerData&&x.label._assemblerData.context;return {node:nodeInfo(x.node),string:x.label.string,fontSize:x.label.fontSize,lineHeight:x.label.lineHeight,fontFamily:x.label.fontFamily||'',horizontalAlign:x.label.horizontalAlign,verticalAlign:x.label.verticalAlign,color:[x.node.color.r,x.node.color.g,x.node.color.b,x.node.color.a],texture:{width:t.width,height:t.height,uploaded:true,minFilter:t._minFilter,magFilter:t._magFilter},baseline:{kind:'source-ttf-single-line-formula',ratio:.26,offset:0,canvas:[x.label.horizontalAlign===0?0:x.label.horizontalAlign===1?t.width/2:t.width,x.label.fontSize*(1-.26/2)-(x.label.fontSize-x.node.height)/2]},canvas:c?{font:c.font,textAlign:c.textAlign,textBaseline:c.textBaseline,lineJoin:c.lineJoin}:null}})}:null,speakerLabel:wantSpeaker?{node:nodeInfo(labelNode),string:label.string,fontSize:label.fontSize,lineHeight:label.lineHeight,fontFamily:label.fontFamily||'',useSystemFont:label.useSystemFont,isSystemFontUsed:label._isSystemFontUsed,isBold:label.isBold,isItalic:label.isItalic,isUnderline:label.isUnderline,styleFlags:label._styleFlags,fontAsset:fontAsset?{name:fontAsset.name||'',uuid:fontAsset._uuid||fontAsset._id||null,nativeUrl:fontAsset.nativeUrl||fontAsset._nativeUrl||''}:null,horizontalAlign:label.horizontalAlign,verticalAlign:label.verticalAlign,overflow:label.overflow,texture:{width:labelTexture.width,height:labelTexture.height,uploaded:true,minFilter:labelTexture._minFilter,magFilter:labelTexture._magFilter},canvasMetrics:{measurementKind:'reconstructed-font-measurement',font,width:measured.width,actualBoundingBoxLeft:measured.actualBoundingBoxLeft,actualBoundingBoxRight:measured.actualBoundingBoxRight,actualBoundingBoxAscent:measured.actualBoundingBoxAscent,actualBoundingBoxDescent:measured.actualBoundingBoxDescent},outline:outline?{width:outline.width,color:[outline.color.r,outline.color.g,outline.color.b,outline.color.a]}:null}:null}})()`;
const readbackExpression = `new Promise(resolve=>{let done=false;const capture=()=>{if(done)return;done=true;try{const gl=cc.game._renderContext,w=gl.drawingBufferWidth,h=gl.drawingBufferHeight,p=new Uint8Array(w*h*4);gl.readPixels(0,0,w,h,gl.RGBA,gl.UNSIGNED_BYTE,p);let s='';for(let i=0;i<p.length;i+=32768)s+=String.fromCharCode(...p.subarray(i,Math.min(i+32768,p.length)));resolve({source:'EVENT_AFTER_DRAW',width:w,height:h,data:btoa(s)});}catch(error){resolve({error:String(error)})}};cc.director.once(cc.Director.EVENT_AFTER_DRAW,capture);setTimeout(()=>{if(!done){done=true;resolve({error:'timeout waiting for natural EVENT_AFTER_DRAW'})}},1000)})`;

(async () => {
  const startedAt = new Date(), expiresAt = Date.now() + deadlineMs;
  try {
    await getJson(`http://127.0.0.1:${port}/json/list`);
    throw new Error(`CDP port ${port} is already in use`);
  } catch (error) {
    if (error.code !== 'ECONNREFUSED') throw error;
  }
  const logPath = path.join(outputRoot, `${stem}-process.log`);
  const log = fs.openSync(logPath, 'w');
  const child = spawn('npm', ['exec', '--', 'electron', stage === 'text' ? holdVerificationExit : '.', stage === 'text' ? '--verify-desktop' : '--verify-python-source', `--remote-debugging-port=${port}`], {
    cwd: sourceRoot, stdio: ['ignore', log, log], detached: true,
    env: stage === 'text' ? {...process.env, JOJO_SOURCE_MAIN: path.join(sourceRoot, 'electron/main.cjs')} : process.env,
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
    let bootstrap = null;
    if (stage === 'text') {
      await poll(() => evaluate(loginReadyExpression), 'source Login component', Math.min(expiresAt, Date.now() + 10000));
      if (await evaluate(startExpression) !== true) throw new Error('source Login start event was not dispatched');
      let scenarioState = null;
      try {
        await poll(async () => { scenarioState = await evaluate(scenarioReadyExpression); return scenarioState.ready || scenarioState.dialogueStarted; }, 'original first scenario row or opening dialogue', Math.min(expiresAt, Date.now() + 10000));
      } catch (error) {
        throw new Error(`${error.message}; last state=${JSON.stringify(scenarioState)}`);
      }
      if (scenarioState.dialogueStarted) scenarioState.ready = false;
      if (scenarioState.ready) {
        if (!Array.isArray(scenarioState.cssPoint) || scenarioState.cssPoint.some(value => !Number.isFinite(value))) throw new Error(`invalid scenario row point: ${JSON.stringify(scenarioState)}`);
        const [x, y] = scenarioState.cssPoint;
        await client.send('Input.dispatchMouseEvent', {type: 'mousePressed', x, y, button: 'left', clickCount: 1});
        await client.send('Input.dispatchMouseEvent', {type: 'mouseReleased', x, y, button: 'left', clickCount: 1});
      }
      bootstrap = {mode: '--verify-desktop', actions:[{kind:'LoginEvent',name:'START_ITEM_CHOICE',choice:0,count:1,checkFloorBypass:true},{kind:'ScenarioRowClick',text:'조조가 군대를 일으키다',count:scenarioState.ready?1:0,cssPoint:scenarioState.ready?scenarioState.cssPoint:null,bypassedBecauseDialogueAlreadyStarted:!scenarioState.ready}],autoDialogueAdvance:false,verificationExitHeldAtCapturePage:true};
    }
    const readyState = await poll(() => {
      if (childExit) { const error = new Error(`source Electron exited before natural dialogue: ${JSON.stringify(childExit)}`); error.fatal = true; throw error; }
      return evaluate(readyExpression);
    }, 'natural R_00 first dialogue', Math.min(expiresAt, Date.now() + (stage === 'text' ? 10000 : deadlineMs)));
    const isolation = await evaluate(isolateExpression);
    if (!isolation || isolation.stage !== stage || isolation.speaker !== '병사 ' ||
        !isolation.text.includes('대장님, 서둘러야 해요!')) {
      throw new Error(`source ${stage} isolation state rejected: ${JSON.stringify(isolation)}`);
    }
    if (isolation.speakerLabel) {
      isolation.speakerLabel.isBold = Boolean(isolation.speakerLabel.isBold);
      isolation.speakerLabel.isItalic = Boolean(isolation.speakerLabel.isItalic);
      isolation.speakerLabel.isUnderline = Boolean(isolation.speakerLabel.isUnderline);
      isolation.speakerLabel.fontNative = {
        family: isolation.speakerLabel.fontFamily,
        assetNativeUrl: isolation.speakerLabel.fontAsset && isolation.speakerLabel.fontAsset.nativeUrl || null,
      };
    }
    await evaluate('new Promise(resolve=>requestAnimationFrame(()=>requestAnimationFrame(resolve)))');
    const raw = await evaluate(readbackExpression);
    if (!raw || !raw.data) throw new Error(`source panel readback failed: ${JSON.stringify(raw)}`);
    const bytes = Buffer.from(raw.data, 'base64'), sha256 = crypto.createHash('sha256').update(bytes).digest('hex');
    if (raw.source !== 'EVENT_AFTER_DRAW') throw new Error(`source panel capture used rejected trigger: ${raw.source}`);
    if (bytes.length !== raw.width * raw.height * 4) throw new Error(`source panel byte count mismatch: ${bytes.length}`);
    fs.writeFileSync(path.join(outputRoot, `${stem}.rgba`), bytes);
    fs.writeFileSync(path.join(outputRoot, `${stem}.json`), JSON.stringify({
      fixture: `R_00 natural first DialogueLayer ${stage} isolation`,
      capture: `direct gl.readPixels after naturally reached live DialogueLayer; cumulative ${stage} visibility isolated after readiness`,
      evidenceKind: `actual-source-renderer-${stage}-isolation`, encoding: 'srgb-encoded-rgba8', origin: 'bottom-left',
      sourceRoot, bootstrap, startedAt: startedAt.toISOString(), completedAt: new Date().toISOString(), readyState, isolation,
      raw: {width: raw.width, height: raw.height, byteLength: bytes.length, sha256, trigger: raw.source},
    }, null, 2) + '\n');
    console.log(`SOURCE_OPENING_${stage.toUpperCase()}_CAPTURE_OK ${raw.width}x${raw.height} sha256=${sha256}`);
  } finally {
    clearTimeout(deadline);
    if (client) client.close();
    try { process.kill(-child.pid, 'SIGTERM'); } catch {}
    await delay(500);
    try { process.kill(-child.pid, 'SIGKILL'); } catch {}
    fs.closeSync(log);
  }
})().catch(error => { console.error(error.stack || error); process.exitCode = 1; });
