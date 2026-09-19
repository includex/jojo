#!/usr/bin/env node
'use strict';

// Export source Cocos whole-string speaker-label canvases. This never crops a frame.
const {spawn} = require('node:child_process');
const crypto = require('node:crypto');
const fs = require('node:fs');
const http = require('node:http');
const path = require('node:path');
const zlib = require('node:zlib');

const sourceRoot = path.resolve(process.argv[2] || process.env.JOJO_SOURCE_ROOT || path.join(__dirname, '../../jojo_mobile/sgccz-desktop'));
const catalogPath = path.resolve(process.argv[3] || process.env.JOJO_UNIT_CATALOG || path.join(process.cwd(), 'core/build/generated/map-assets/data/unit.bin'));
const outputRoot = path.resolve(process.argv[4] || process.env.JOJO_LABEL_OUT || path.join(process.cwd(), 'core/build/generated/street-speaker-labels'));
const port = Number(process.env.JOJO_CDP_PORT || 9800 + process.pid % 100);
const deadlineMs = Number(process.env.JOJO_EXPORT_DEADLINE_MS || 55000);
if (!Number.isFinite(port) || !Number.isFinite(deadlineMs) || deadlineMs <= 0 || deadlineMs > 60000) {
  throw new Error('JOJO_CDP_PORT must be numeric and JOJO_EXPORT_DEADLINE_MS must be 1..60000');
}

const contract = JSON.parse(fs.readFileSync(path.join(__dirname, 'street_speaker_label_contract.json'), 'utf8'));
const sha256File = file => crypto.createHash('sha256').update(fs.readFileSync(file)).digest('hex');
function validateEnvironment() {
  if (process.platform !== contract.platform) throw new Error('Speaker label assets require the validated macOS Canvas font environment');
  if (require('node:os').release() !== contract.osRelease || process.arch !== contract.arch) throw new Error('Unvalidated OS/architecture for speaker label generation');
  const electronVersion = JSON.parse(fs.readFileSync(path.join(sourceRoot, 'node_modules/electron/package.json'), 'utf8')).version;
  if (electronVersion !== contract.electronVersion) throw new Error(`Unvalidated Electron version: ${electronVersion}`);
  for (const [file, digest] of Object.entries(contract.sourceSha256)) {
    if (sha256File(path.join(sourceRoot, file)) !== digest) throw new Error(`Source label contract changed; review exporter before regenerating: ${file}`);
  }
  for (const [file, digest] of Object.entries(contract.fontSha256)) {
    if (sha256File(file) !== digest) throw new Error(`Unvalidated font version: ${file}`);
  }
  return {platform: process.platform, osRelease: require('node:os').release(), electronVersion, fontSha256: contract.fontSha256};
}
const environment = validateEnvironment();

const delay = ms => new Promise(resolve => setTimeout(resolve, ms));
function readNames() {
  const key = Buffer.from('ccz65Sha08GeZ1Fu'), raw = fs.readFileSync(catalogPath);
  const decoded = Buffer.from(raw.map((value, index) => {
    const shift = key[index % key.length] % 8;
    return (value >>> shift) | ((value << (8 - shift)) & 255);
  })).toString('utf8');
  const digest = decoded.slice(0, 32), json = decoded.slice(32);
  const actual = crypto.createHash('md5').update('ccz65Sha08GeZ1Fu' + json).digest('hex');
  if (digest.toLowerCase() !== actual) throw new Error(`unit catalog digest mismatch: ${catalogPath}`);
  const rows = JSON.parse(json), seen = new Set(), names = [];
  rows.forEach((row, firstUnitId) => {
    const rawName = row && row['0'];
    const text = typeof rawName === 'string' ? rawName.replace(/\d.*$/, '') : '';
    if (/[\r\n]/.test(text)) throw new Error(`Multiline speaker label is unsupported: ${firstUnitId}`);
    if (text && !seen.has(text)) { seen.add(text); names.push({firstUnitId, text}); }
  });
  if (!names.length) throw new Error('unit catalog contains no speaker labels');
  return names;
}
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
    try { const value = await operation(); if (value) return value; } catch (error) { last = error; }
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

// Write uploaded RGBA bytes directly: Canvas.toDataURL introduces another unpremultiply rounding.
function pngRgba(width, height, rgba) {
  if (rgba.length !== width * height * 4) throw new Error('Invalid label RGBA length');
  const crc32 = bytes => {
    let crc = 0xffffffff;
    for (const byte of bytes) {
      crc ^= byte;
      for (let bit = 0; bit < 8; bit++) crc = (crc >>> 1) ^ (crc & 1 ? 0xedb88320 : 0);
    }
    return (crc ^ 0xffffffff) >>> 0;
  };
  const chunk = (kind, data) => {
    const type = Buffer.from(kind), length = Buffer.alloc(4), crc = Buffer.alloc(4);
    length.writeUInt32BE(data.length); crc.writeUInt32BE(crc32(Buffer.concat([type, data])));
    return Buffer.concat([length, type, data, crc]);
  };
  const header = Buffer.alloc(13); header.writeUInt32BE(width); header.writeUInt32BE(height, 4);
  header[8] = 8; header[9] = 6;
  const rows = Buffer.alloc(height * (width * 4 + 1));
  for (let y = 0; y < height; y++) rgba.copy(rows, y * (width * 4 + 1) + 1, y * width * 4, (y + 1) * width * 4);
  return Buffer.concat([Buffer.from([137,80,78,71,13,10,26,10]), chunk('IHDR', header),
    chunk('IDAT', zlib.deflateSync(rows)), chunk('IEND', Buffer.alloc(0))]);
}

const catalogNames = readNames();
const exportExpression = `(()=>{
  const names=${JSON.stringify(catalogNames)};
  const gl=document.createElement('canvas').getContext('webgl');
  if(!gl)throw Error('WebGL is required for the original Canvas upload conversion');
  gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL,false);
  gl.pixelStorei(gl.UNPACK_PREMULTIPLY_ALPHA_WEBGL,false);
  const texture=gl.createTexture(),framebuffer=gl.createFramebuffer();
  gl.bindTexture(gl.TEXTURE_2D,texture);
  gl.texParameteri(gl.TEXTURE_2D,gl.TEXTURE_MIN_FILTER,gl.LINEAR);
  gl.texParameteri(gl.TEXTURE_2D,gl.TEXTURE_MAG_FILTER,gl.LINEAR);
  gl.texParameteri(gl.TEXTURE_2D,gl.TEXTURE_WRAP_S,gl.CLAMP_TO_EDGE);
  gl.texParameteri(gl.TEXTURE_2D,gl.TEXTURE_WRAP_T,gl.CLAMP_TO_EDGE);
  gl.bindFramebuffer(gl.FRAMEBUFFER,framebuffer);
  gl.framebufferTexture2D(gl.FRAMEBUFFER,gl.COLOR_ATTACHMENT0,gl.TEXTURE_2D,texture,0);
  return names.map(({firstUnitId,text})=>{
    const measureCanvas=document.createElement('canvas'),measure=measureCanvas.getContext('2d');
    measure.font='36px Arial';
    const measureWidth=measure.measureText(text).width;
    const rawWidth=Number(measureWidth.toFixed(2)),rawHeight=Number(((1+.26)*40).toFixed(2));
    const nodeWidth=rawWidth+4,nodeHeight=rawHeight+4;
    const canvas=document.createElement('canvas'); canvas.width=nodeWidth; canvas.height=nodeHeight;
    const ctx=canvas.getContext('2d');
    ctx.font='36px Arial'; ctx.textAlign='center'; ctx.textBaseline='alphabetic'; ctx.lineJoin='miter'; // Resizing the original pooled canvas resets its earlier round setting.
    ctx.fillStyle='rgba(102, 255, 255, 0.004)'; ctx.fillRect(0,0,canvas.width,canvas.height);
    ctx.strokeStyle='rgba(102, 255, 255, 1)'; ctx.lineWidth=4;
    ctx.fillStyle='rgba(35, 2, 234, 1)';
    const x=nodeWidth/2, y=36*(1-.26/2)-(4+36-nodeHeight)/2+2;
    ctx.strokeText(text,x,y); ctx.fillText(text,x,y);
    gl.texImage2D(gl.TEXTURE_2D,0,gl.RGBA,gl.RGBA,gl.UNSIGNED_BYTE,canvas);
    if(gl.checkFramebufferStatus(gl.FRAMEBUFFER)!==gl.FRAMEBUFFER_COMPLETE)throw Error('Canvas texture framebuffer incomplete');
    const pixels=new Uint8Array(canvas.width*canvas.height*4);
    gl.readPixels(0,0,canvas.width,canvas.height,gl.RGBA,gl.UNSIGNED_BYTE,pixels);
    if(gl.getError()!==gl.NO_ERROR)throw Error('Canvas texture readback failed');
    let binary='';for(let i=0;i<pixels.length;i+=32768)binary+=String.fromCharCode(...pixels.subarray(i,i+32768));
    return {firstUnitId,text,measureWidth,rawWidth,rawHeight,nodeWidth,nodeHeight,width:canvas.width,height:canvas.height,baseline:[x,y],rgba:btoa(binary)};
  });
})()`;

(async () => {
  const expiresAt = Date.now() + deadlineMs;
  try {
    await getJson(`http://127.0.0.1:${port}/json/list`);
    throw new Error(`CDP port ${port} is already in use`);
  } catch (error) {
    if (error.code !== 'ECONNREFUSED') throw error;
  }
  fs.rmSync(outputRoot, {recursive: true, force: true});
  fs.mkdirSync(outputRoot, {recursive: true});
  const log = fs.openSync(path.join(outputRoot, 'source-process.log'), 'w');
  const child = spawn('npm', ['exec', '--', 'electron', '.', '--verify-python-source', `--remote-debugging-port=${port}`], {
    cwd: sourceRoot, stdio: ['ignore', log, log], detached: true,
  });
  let childExit = null, client;
  child.once('exit', (code, signal) => { childExit = {code, signal}; });
  const deadline = setTimeout(() => {
    try { process.kill(-child.pid, 'SIGTERM'); } catch {}
    setTimeout(() => { try { process.kill(-child.pid, 'SIGKILL'); } catch {} process.exit(124); }, 250);
  }, deadlineMs);
  try {
    const page = await poll(async () => {
      if (childExit) throw new Error(`source Electron exited before CDP readiness: ${JSON.stringify(childExit)}`);
      const pages = await getJson(`http://127.0.0.1:${port}/json/list`);
      return pages.find(item => item.type === 'page' && item.webSocketDebuggerUrl);
    }, 'source Electron page', expiresAt);
    if (page.url !== 'sgccz://game/electron/index.html') throw new Error(`unexpected source page: ${page.url}`);
    client = await connect(page.webSocketDebuggerUrl);
    const response = await client.send('Runtime.evaluate', {expression: exportExpression, returnByValue: true});
    if (response.exceptionDetails) throw new Error(`source label export failed: ${JSON.stringify(response.exceptionDetails)}`);
    const rows = response.result.value;
    if (!Array.isArray(rows) || !rows.length) throw new Error('source label export returned no entries');
    const entries = rows.map((row, index) => {
      const bytes = pngRgba(row.width, row.height, Buffer.from(row.rgba, 'base64'));
      const digest = crypto.createHash('sha256').update(bytes).digest('hex');
      const file = `${String(index).padStart(3, '0')}-${digest.slice(0, 12)}.png`;
      fs.writeFileSync(path.join(outputRoot, file), bytes);
      const {rgba, ...entry} = row;
      return {...entry, file, sha256: digest};
    });
    fs.writeFileSync(path.join(outputRoot, 'manifest.json'), JSON.stringify({
      contractVersion:contract.version, environment,
      generator:'original Cocos Electron Canvas whole-string raster; no screenshot or crop',
      catalog:'decoded original unit.bin field 0, original Unit.unitName(id, true) first-digit truncation, distinct nonempty text in first-id order',
      catalogPath,catalogSha256:crypto.createHash('sha256').update(fs.readFileSync(catalogPath)).digest('hex'),
      contract:{font:'36px Arial',lineHeight:40,alignment:['center','center'],baselineRatio:.26,baselineOffset:0,lineJoin:'miter',rasterEncoding:'Canvas WebGL upload RGBA without PNG re-unpremultiply',outline:{width:2,rgba:[102,255,255,255]},fill:[35,2,234,255],background:[102,255,255,.004]},
      sourceRoot,sourceSha256:contract.sourceSha256,entries,
    }, null, 2)+'\n');
    console.log(JSON.stringify({outputRoot,count:entries.length,manifest:path.join(outputRoot,'manifest.json')}));
  } finally {
    clearTimeout(deadline);
    if (client) client.close();
    try { process.kill(-child.pid, 'SIGTERM'); } catch {}
    await delay(250);
    try { process.kill(-child.pid, 'SIGKILL'); } catch {}
    fs.closeSync(log);
  }
})().catch(error => { console.error(error.stack || error); process.exitCode = 1; });
