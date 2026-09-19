#!/usr/bin/env node
'use strict';

// Decode original Mmap JPEGs with Electron's browser decoder and export Canvas PNGs.
const {spawn} = require('node:child_process');
const crypto = require('node:crypto');
const fs = require('node:fs');
const path = require('node:path');

const workerFlag = '--source-map-texture-worker';
const sha256 = bytes => crypto.createHash('sha256').update(bytes).digest('hex');

if (process.argv.includes(workerFlag)) {
  const {app, BrowserWindow} = require('electron');
  const index = process.argv.indexOf(workerFlag);
  const [sourceRoot, manifestPath, outputRoot] = process.argv.slice(index + 1);
  (async()=>{
    await app.whenReady();
    const window = new BrowserWindow({show:false,webPreferences:{nodeIntegration:true,contextIsolation:false,backgroundThrottling:false}});
    await window.loadURL('data:text/html;charset=utf-8,<canvas id="canvas"></canvas>');
    const expression=`(async()=>{const fs=require('node:fs'),path=require('node:path'),crypto=require('node:crypto'),sourceRoot=${JSON.stringify(sourceRoot)},manifestPath=${JSON.stringify(manifestPath)},outputRoot=${JSON.stringify(outputRoot)},manifest=JSON.parse(fs.readFileSync(manifestPath,'utf8')),sources=manifest.mapSources;if(!sources||Array.isArray(sources)||typeof sources!=='object')throw Error('map-assets manifest mapSources must be an object');const maps=manifest.maps,keys=Object.keys(sources).sort();if(!keys.length||!maps||Array.isArray(maps)||typeof maps!=='object'||JSON.stringify(keys)!==JSON.stringify(Object.keys(maps).sort()))throw Error('mapSources must cover the nonempty maps catalog exactly');const sha=b=>crypto.createHash('sha256').update(b).digest('hex'),entries=[];for(const [mapId,source] of Object.entries(sources).sort((a,b)=>Number(a[0])-Number(b[0]))){if(!/^\\d+$/.test(mapId)||typeof source!=='string')throw Error('invalid mapSources entry '+JSON.stringify([mapId,source]));const absolute=path.resolve(sourceRoot,source),relative=path.relative(sourceRoot,absolute);if(relative.startsWith('..')||path.isAbsolute(relative))throw Error('map source escapes source root: '+source);const input=fs.readFileSync(absolute),image=new Image();await new Promise((resolve,reject)=>{image.onload=resolve;image.onerror=()=>reject(Error('browser decode failed: '+source));image.src='data:image/jpeg;base64,'+input.toString('base64')});if(!image.naturalWidth||!image.naturalHeight)throw Error('empty browser image: '+source);const canvas=document.getElementById('canvas');canvas.width=image.naturalWidth;canvas.height=image.naturalHeight;const context=canvas.getContext('2d');context.clearRect(0,0,canvas.width,canvas.height);context.drawImage(image,0,0);const rgba=Buffer.from(context.getImageData(0,0,canvas.width,canvas.height).data),png=Buffer.from(canvas.toDataURL('image/png').split(',')[1],'base64'),file=mapId+'.png';fs.writeFileSync(path.join(outputRoot,file),png);entries.push({mapId:Number(mapId),source,sourceSha256:sha(input),width:canvas.width,height:canvas.height,rgbaSha256:sha(rgba),pngSha256:sha(png),file})}return entries})()`;
    const result = await window.webContents.executeJavaScript(expression, true);
    const electronVersion = process.versions.electron;
    fs.writeFileSync(path.join(outputRoot,'manifest.json'),JSON.stringify({contract:'source-map-textures-browser-canvas-v1',decoder:'Electron browser Image decode + Canvas PNG; no screenshot or framebuffer crop',electronVersion,chromeVersion:process.versions.chrome,platform:process.platform,arch:process.arch,sourceRoot,mapAssetsManifest:{path:manifestPath,sha256:sha256(fs.readFileSync(manifestPath))},entries:result},null,2)+'\n');
    console.log(JSON.stringify({outputRoot,count:result.length,electronVersion}));
    window.destroy(); app.quit();
  })().catch(e=>{console.error(e.stack||e);app.exit(1)});
} else {
  const sourceRoot = path.resolve(process.argv[2] || process.env.JOJO_SOURCE_ROOT || path.join(__dirname,'../../jojo_mobile/sgccz-desktop'));
  const manifestPath = path.resolve(process.argv[3] || path.join(process.cwd(),'core/build/generated/map-assets/manifest.json'));
  const outputRoot = path.resolve(process.argv[4] || path.join(process.cwd(),'core/build/generated/source-map-textures'));
  const deadlineMs = Number(process.env.JOJO_EXPORT_DEADLINE_MS || 55000);
  if (!Number.isFinite(deadlineMs)||deadlineMs<1000||deadlineMs>60000) throw Error('JOJO_EXPORT_DEADLINE_MS must be 1000..60000');
  if (!fs.existsSync(path.join(sourceRoot,'node_modules/electron/package.json'))) throw Error('source Electron is not installed');
  fs.rmSync(outputRoot,{recursive:true,force:true}); fs.mkdirSync(outputRoot,{recursive:true});
  const logPath=path.join(outputRoot,'source-process.log'),log=fs.openSync(logPath,'w');
  const child=spawn('npm',['exec','--','electron',__filename,workerFlag,sourceRoot,manifestPath,outputRoot],{cwd:sourceRoot,stdio:['ignore','pipe',log],detached:true});
  let output='';child.stdout.on('data',b=>{output+=b});
  const timer=setTimeout(()=>{try{process.kill(-child.pid,'SIGTERM')}catch{}setTimeout(()=>{try{process.kill(-child.pid,'SIGKILL')}catch{}process.exit(124)},250)},deadlineMs);
  child.on('error',e=>{clearTimeout(timer);fs.closeSync(log);throw e});
  child.on('exit',(code,signal)=>{clearTimeout(timer);fs.closeSync(log);if(code!==0){console.error(`source map exporter failed code=${code} signal=${signal}; see ${logPath}`);process.exitCode=code||1;return}process.stdout.write(output)});
}
