#!/usr/bin/env node
const fs=require('fs'),path=require('path'),F=JSON.parse(fs.readFileSync(process.argv[2]));
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}}};
function L(){this.removes=0;this.routes=[];this.nodes={}}
L.prototype._setBg=function(){};L.prototype.removeFromParent=function(){this.removes++};L.prototype.seekNodeByName=function(n){return this.nodes[n]||(this.nodes[n]={})};L.prototype.addTouchEventListener=function(n,fn){n.fn=fn};L.prototype.addLayer=function(name,arg){this.routes.push([name,arg])};
let x={};require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SendGiftsLayer.js'))(n=>n==='UILayer'?{default:L}:n==='Instance'?{default:{LAYER:{CmdLayer:'CMD',skmLayer:'SKM'}}}: {},{},x);
const out={};for(const c of F.cases){const p=new x.default();p.onCreate();const t=[{step:'create',removes:p.removes,routes:p.routes.slice()}];for(const e of c.events){const [b,k]=e.split(':');const n=p.nodes[b==='b0'?'Logo_12-1/button0':`Logo_12-1/scrollview/view/content/New Node/button${b[1]}`];n.fn(n,+k);t.push({step:e,removes:p.removes,routes:p.routes.slice()})}out[c.name]=t}fs.mkdirSync(path.dirname(process.argv[3]),{recursive:true});fs.writeFileSync(process.argv[3],JSON.stringify(out));
