#!/usr/bin/env node
'use strict';

// Export original cc.RichText prefix segment textures. No framebuffer crop is used.
const {spawn, spawnSync} = require('node:child_process');
const crypto = require('node:crypto');
const fs = require('node:fs');
const http = require('node:http');
const os = require('node:os');
const path = require('node:path');
const zlib = require('node:zlib');
const {paginateDialogueCalls} = require('./street_body_label_pages.cjs');
const maxRenderedPages = Number(process.env.JOJO_BODY_LABEL_MAX_PAGES || 6);
if (!Number.isInteger(maxRenderedPages) || maxRenderedPages < 1) throw Error('JOJO_BODY_LABEL_MAX_PAGES must be a positive integer');

const labelStyle = process.env.JOJO_LABEL_STYLE || 'body';
if (!['body','info'].includes(labelStyle)) throw Error('JOJO_LABEL_STYLE must be body or info');
const style = labelStyle === 'info' ? {fontSize:40,lineHeight:50,width:0,contractFile:'info_label_contract.json'} : {fontSize:36,lineHeight:42,width:728,contractFile:'street_body_label_contract.json'};
const sourceRoot = path.resolve(process.argv[2] || process.env.JOJO_SOURCE_ROOT || path.join(__dirname, '../../jojo_mobile/sgccz-desktop'));
const catalogPath = path.resolve(process.argv[3] || process.env.JOJO_BODY_LABEL_INPUT || path.join(sourceRoot, 'decompiled-python/R_00.py'));
const outputRoot = path.resolve(process.argv[4] || process.env.JOJO_BODY_LABEL_OUT || path.join(process.cwd(), labelStyle === 'info' ? 'core/build/generated/info-labels' : 'core/build/generated/street-body-labels'));
const port = Number(process.env.JOJO_CDP_PORT || 9900 + process.pid % 80);
const deadlineMs = Number(process.env.JOJO_EXPORT_DEADLINE_MS || 55000);
if (!Number.isFinite(port) || !Number.isFinite(deadlineMs) || deadlineMs <= 0 || deadlineMs > 60000) throw Error('deadline must be 1..60000 and port numeric');

const contract = JSON.parse(fs.readFileSync(path.join(__dirname, style.contractFile)));
const sha = bytes => crypto.createHash('sha256').update(bytes).digest('hex');
const shaFile = file => sha(fs.readFileSync(file));
function validateEnvironment() {
  if (process.platform !== contract.platform || process.arch !== contract.arch || os.release() !== contract.osRelease) throw Error('unvalidated body-label host');
  const electronVersion = JSON.parse(fs.readFileSync(path.join(sourceRoot, 'node_modules/electron/package.json'))).version;
  if (electronVersion !== contract.electronVersion) throw Error(`unvalidated Electron ${electronVersion}`);
  for (const [file, digest] of Object.entries(contract.sourceSha256)) if (shaFile(path.join(sourceRoot, file)) !== digest) throw Error(`source body-label contract changed: ${file}`);
  for (const [file, digest] of Object.entries(contract.fontSha256)) if (shaFile(file) !== digest) throw Error(`font contract changed: ${file}`);
  return {platform:process.platform,arch:process.arch,osRelease:os.release(),electronVersion,fontSha256:contract.fontSha256};
}

function pythonInput(sourceFile) {
  if (labelStyle === 'info') {
    const script = `import ast,json,sys
p=sys.argv[1]
t=ast.parse(open(p,encoding='utf8').read())
f=next(n for n in t.body if isinstance(n,ast.FunctionDef) and n.name=='scene1')
c=[]
for n in ast.walk(f):
 if isinstance(n,ast.Call) and isinstance(n.func,ast.Attribute) and n.func.attr=='setEventName' and n.args and isinstance(n.args[0],ast.Constant) and isinstance(n.args[0].value,str): c.append((n.lineno,n.args[0].value))
c.sort(); print(json.dumps([v for _,v in c[:1]],ensure_ascii=False))`;
    const result = spawnSync('python3', ['-c', script, sourceFile], {encoding:'utf8', timeout:5000});
    if (result.status !== 0) throw Error(`R_00 event AST extraction failed: ${result.stderr}`);
    const pages=JSON.parse(result.stdout);
    if (pages.length!==1 || !pages[0]) throw Error(`expected first R_00 setEventName, got ${result.stdout}`);
    return {width:style.width,pages};
  }
  const script = `import ast,json,sys\np=sys.argv[1]\nt=ast.parse(open(p,encoding='utf8').read())\nf=next(n for n in t.body if isinstance(n,ast.FunctionDef) and n.name=='scene1')\nc=[]\nfor n in ast.walk(f):\n if isinstance(n,ast.Call) and isinstance(n.func,ast.Attribute) and n.func.attr=='say': c.append((n.lineno,n.args[0].value if n.args and isinstance(n.args[0],ast.Constant) and isinstance(n.args[0].value,str) else None))\nc.sort(); print(json.dumps([v for _,v in c],ensure_ascii=False))`;
  const result = spawnSync('python3', ['-c', script, sourceFile], {encoding:'utf8', timeout:5000});
  if (result.status !== 0) throw Error(`R_00 AST extraction failed: ${result.stderr}`);
  return pageInput(JSON.parse(result.stdout));
}
function astJsonInput(value) {
  const calls=[];
  function walk(node, inScene1=false) {
    if (Array.isArray(node)) return node.forEach(x=>walk(x,inScene1));
    if (!node || typeof node !== 'object') return;
    const fields=node.fields||{};
    const scene=inScene1 || (node.type==='FunctionDef' && fields.name==='scene1');
    const wanted=labelStyle==='info'?'setEventName':'say';
    if (scene && node.type==='Call' && ((fields.func?.type==='Attribute' && fields.func.fields?.attr===wanted) || (fields.func?.type==='Name' && fields.func.fields?.id===wanted))) {
      const arg=fields.args?.[0];
      calls.push([node.location?.line||0, arg?.type==='Constant' && typeof arg.fields?.value==='string' ? arg.fields.value : null]);
    }
    for (const child of Object.values(fields)) walk(child,scene);
  }
  walk(value.ast||value); calls.sort((a,b)=>a[0]-b[0]);
  if (labelStyle==='info') { if (!calls.length) throw Error('R_00 AST JSON has no scene1 setEventName call'); return {width:style.width,pages:[calls[0][1]]}; }
  if (!calls.length) throw Error('R_00 AST JSON has no scene1 say calls');
  return pageInput(calls.map(x=>x[1]));
}
function pageInput(calls) {
  const {pages, origins} = paginateDialogueCalls(calls, maxRenderedPages);
  if (!pages.length) throw Error('R_00 scene1 has no supported dialogue pages');
  return {width:728,pages,coverage:{scene:'scene1',maxRenderedPages,renderedPages:pages.length,origins}};
}
function loadInput() {
  let value;
  if (catalogPath.endsWith('.py')) value=pythonInput(catalogPath);
  else {
    const json=JSON.parse(fs.readFileSync(catalogPath,'utf8'));
    const custom=Array.isArray(json)||Array.isArray(json.pages)||Array.isArray(json.eventTexts);
    if (labelStyle==='info' && custom && (!json.styleContract || json.styleContract!==contract.styleContract)) throw Error(`info JSON input requires styleContract ${contract.styleContract}`);
    value=custom?json:astJsonInput(json);
  }
  const width=Number(value.width??style.width), pages=Array.isArray(value)?value:(value.pages||value.eventTexts);
  if (!Number.isFinite(width)||(labelStyle==='body'?width<=0:width!==0)||!Array.isArray(pages)||pages.some(x=>typeof x!=='string'||!x)) throw Error('input must be R_00 AST JSON/Python or {width,pages:[nonempty strings]}');
  if (pages.some(x=>/<[^>]*>|\[[A-Z]/.test(x))) throw Error('rich-text markup is unsupported; input must be plain visible text');
  if (pages.some(x=>/[\uD800-\uDFFF]/.test(x))) throw Error('supplementary Unicode is unsupported because source typing advances UTF-16 code units');
  return {width,pages,coverage:value.coverage||{kind:"explicit-input-pages",renderedPages:pages.length},prefixes:pages.flatMap((page,pageIndex)=>[...(labelStyle==='info'?[{pageIndex,text:''}]:[]),...Array.from(page).map((_,i)=>({pageIndex,text:Array.from(page).slice(0,i+1).join('')}))])};
}

const delay = ms => new Promise(r=>setTimeout(r,ms));
const getJson = url => new Promise((resolve,reject)=>{const q=http.get(url,r=>{let b='';r.on('data',x=>b+=x);r.on('end',()=>{try{resolve(JSON.parse(b))}catch(e){reject(e)}})});q.setTimeout(1000,()=>q.destroy(Error('CDP HTTP timeout')));q.on('error',reject)});
async function poll(fn,label,end){let last;while(Date.now()<end){try{const x=await fn();if(x)return x}catch(e){last=e}await delay(40)}throw Error(`timeout waiting for ${label}: ${last||''}`)}
async function connect(url){const s=new WebSocket(url);await new Promise((r,j)=>{s.addEventListener('open',r,{once:true});s.addEventListener('error',j,{once:true})});let id=0;const p=new Map();s.addEventListener('message',e=>{const m=JSON.parse(e.data),w=p.get(m.id);if(w){p.delete(m.id);m.error?w.reject(Error(JSON.stringify(m.error))):w.resolve(m.result)}});return{send(method,params={}){return new Promise((resolve,reject)=>{const n=++id;p.set(n,{resolve,reject});s.send(JSON.stringify({id:n,method,params}))})},close(){s.close()}}}
function png(width,height,rgba){const crc32=b=>{let c=0xffffffff;for(const x of b){c^=x;for(let i=0;i<8;i++)c=(c>>>1)^(c&1?0xedb88320:0)}return(c^0xffffffff)>>>0};const chunk=(k,d)=>{const t=Buffer.from(k),l=Buffer.alloc(4),c=Buffer.alloc(4);l.writeUInt32BE(d.length);c.writeUInt32BE(crc32(Buffer.concat([t,d])));return Buffer.concat([l,t,d,c])};const h=Buffer.alloc(13);h.writeUInt32BE(width);h.writeUInt32BE(height,4);h[8]=8;h[9]=6;const rows=Buffer.alloc(height*(width*4+1));for(let y=0;y<height;y++)rgba.copy(rows,y*(width*4+1)+1,y*width*4,(y+1)*width*4);return Buffer.concat([Buffer.from([137,80,78,71,13,10,26,10]),chunk('IHDR',h),chunk('IDAT',zlib.deflateSync(rows)),chunk('IEND',Buffer.alloc(0))])}

const input=loadInput(), environment=validateEnvironment();
const expression=`(async()=>{const spec=${JSON.stringify(input)},scene=cc.director.getScene(),host=new cc.Node('BODY_LABEL_EXPORT'),node=new cc.Node('richtext');host.setPosition(-10000,-10000);scene.addChild(host);cc.game.addPersistRootNode(host);host.addChild(node);node.setAnchorPoint(0,1);node.color=cc.Color.BLACK;const rich=node.addComponent(cc.RichText);rich.fontSize=${style.fontSize};rich.lineHeight=${style.lineHeight};rich.maxWidth=spec.width;rich.horizontalAlign=0;rich.fontFamily='Arial';rich.useSystemFont=true;const gl=cc.game._renderContext,old=gl.getParameter(gl.FRAMEBUFFER_BINDING),fb=gl.createFramebuffer(),rows=[];for(const item of spec.prefixes){rich.string=item.text.replace(/\\n/g,'<br/>');rich._updateRichText();await new Promise(r=>requestAnimationFrame(()=>requestAnimationFrame(r)));const segments=[];for(const child of node.children.slice()){const label=child.getComponent(cc.Label);if(!label)continue;label._forceUpdateRenderData();const tex=label._ttfTexture||(label._frame&&label._frame._texture),id=tex&&(tex._glID||(tex._texture&&tex._texture._glID));if(!id)throw Error('segment texture not uploaded');gl.bindFramebuffer(gl.FRAMEBUFFER,fb);gl.framebufferTexture2D(gl.FRAMEBUFFER,gl.COLOR_ATTACHMENT0,gl.TEXTURE_2D,id,0);if(gl.checkFramebufferStatus(gl.FRAMEBUFFER)!==gl.FRAMEBUFFER_COMPLETE)throw Error('segment framebuffer incomplete');const pixels=new Uint8Array(tex.width*tex.height*4);gl.readPixels(0,0,tex.width,tex.height,gl.RGBA,gl.UNSIGNED_BYTE,pixels);gl.bindFramebuffer(gl.FRAMEBUFFER,old);let binary='';for(let i=0;i<pixels.length;i+=32768)binary+=String.fromCharCode(...pixels.subarray(i,i+32768));segments.push({text:label.string,x:child.x+node.width/2,y:child.y-node.height/2,nodeWidth:child.width,nodeHeight:child.height,width:tex.width,height:tex.height,rgba:btoa(binary)});}rows.push({pageIndex:item.pageIndex,text:item.text,width:node.width,height:node.height,segments});}gl.bindFramebuffer(gl.FRAMEBUFFER,old);gl.deleteFramebuffer(fb);cc.game.removePersistRootNode(host);host.destroy();return rows})()`;

(async()=>{const end=Date.now()+deadlineMs;try{await getJson(`http://127.0.0.1:${port}/json/list`);throw Error(`CDP port ${port} busy`)}catch(e){if(e.code!=='ECONNREFUSED')throw e}fs.rmSync(outputRoot,{recursive:true,force:true});fs.mkdirSync(outputRoot,{recursive:true});const log=fs.openSync(path.join(outputRoot,'source-process.log'),'w'),child=spawn('npm',['exec','--','electron',path.join(__dirname,'hold_source_verification_exit.cjs'),'--verify-desktop',`--remote-debugging-port=${port}`],{cwd:sourceRoot,env:{...process.env,JOJO_SOURCE_MAIN:path.join(sourceRoot,'electron/main.cjs')},stdio:['ignore',log,log],detached:true});let client;const timer=setTimeout(()=>{try{process.kill(-child.pid,'SIGTERM')}catch{}setTimeout(()=>{try{process.kill(-child.pid,'SIGKILL')}catch{}process.exit(124)},250)},deadlineMs);try{const page=await poll(async()=>{const a=await getJson(`http://127.0.0.1:${port}/json/list`);return a.find(x=>x.type==='page'&&x.webSocketDebuggerUrl)},'page',end);if(page.url!=='sgccz://game/electron/index.html')throw Error(`unexpected page ${page.url}`);client=await connect(page.webSocketDebuggerUrl);await poll(async()=>{const r=await client.send('Runtime.evaluate',{expression:'!!(globalThis.cc&&cc.director&&cc.director.getScene())',returnByValue:true});return r.result.value},'Cocos scene',end);const result=await client.send('Runtime.evaluate',{expression,returnByValue:true,awaitPromise:true});if(result.exceptionDetails)throw Error(`body export failed: ${JSON.stringify(result.exceptionDetails)}`);const entries=result.result.value;for(let i=0;i<entries.length;i++){for(let j=0;j<entries[i].segments.length;j++){const s=entries[i].segments[j],bytes=png(s.width,s.height,Buffer.from(s.rgba,'base64')),digest=sha(bytes),file=`${digest}.png`;if(!fs.existsSync(path.join(outputRoot,file)))fs.writeFileSync(path.join(outputRoot,file),bytes);delete s.rgba;s.file=file;s.sha256=digest}}fs.writeFileSync(path.join(outputRoot,'manifest.json'),JSON.stringify({contractVersion:contract.version,environment,generator:'original offscreen cc.RichText segment textures via GL readback; no screenshot/crop',styleContract:contract.styleContract,catalog:{kind:catalogPath.endsWith('.py')?'R_00-python-AST':(JSON.parse(fs.readFileSync(catalogPath,'utf8')).ast?'R_00-json-AST':'input-json'),path:catalogPath,sha256:shaFile(catalogPath),pages:input.pages,coverage:input.coverage},contract:{width:input.width,font:`${style.fontSize}px Arial`,lineHeight:style.lineHeight,color:[0,0,0,255],background:[0,0,0,.004],baselineRatio:.26,filter:'LINEAR'},sourceRoot,sourceSha256:contract.sourceSha256,entries},null,2)+'\n');console.log(JSON.stringify({outputRoot,count:entries.length,pages:input.pages.length}))}finally{clearTimeout(timer);if(client)client.close();try{process.kill(-child.pid,'SIGTERM')}catch{}await delay(250);try{process.kill(-child.pid,'SIGKILL')}catch{}fs.closeSync(log)}})().catch(e=>{console.error(e.stack||e);process.exitCode=1});
