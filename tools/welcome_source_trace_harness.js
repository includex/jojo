#!/usr/bin/env node
const fs=require('fs'),path=require('path');
const fixture=JSON.parse(fs.readFileSync(process.argv[2]));
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}}};
function Scene(){this.routes=[]}
Scene.prototype.replaceScene=function(name,flag){this.routes.push([name,flag])}
let out={};
require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/Welcome.js'))(n=>{
  if(n==='UIScene') return {default:Scene};
  if(n==='Config') return {STAGE:{LOGIN_SCENE:'LOGIN'}};
  if(n==='Manager') return {};
  return {};
},{},out);
const Answer=out.default;
const result={};
for(const c of fixture.cases){
 const layer=new Answer(); layer.onCreate(); const trace=[{step:'create',routes:layer.routes.slice()}];
 for(const event of c.events){layer.onEvent(null,event);trace.push({step:`event:${event}`,routes:layer.routes.slice()})}
 result[c.name]=trace;
}
fs.mkdirSync(path.dirname(process.argv[3]),{recursive:true});fs.writeFileSync(process.argv[3],JSON.stringify(result));
