#!/usr/bin/env node
/* Execute recovered MenuLayer.js with only the Cocos APIs it calls. */
const fs = require('node:fs'), path = require('node:path');
const cases = JSON.parse(fs.readFileSync(process.argv[2], 'utf8')).cases;
class Node { constructor() { this.active=true; this.opacity=255; this.tag2=0; } addComponent(C) { return new C(); } runAction() {} }
class Button { constructor(){this.node=new Node();this.interactable=true;} }
class Label { constructor(){this.node=new Node();this.string='';} }
class ProgressBar { constructor(){this.node=new Node();this.progress=0;} }
class Sprite { constructor(){this.node=new Node();} }
class Animation { addClip(){} play(){} }
class UILayer {}
global.cc = { Component: UILayer, Node, Button, Label, ProgressBar, Sprite, Animation, Texture2D: class {}, SpriteFrame: class { constructor(){} }, AnimationClip: {createWithSpriteFrames(){return {}}}, WrapMode:{Loop:1}, rect:(x,y,w,h)=>({x,y,w,h}), fadeOut(){},fadeIn(){},delayTime(){},callFunc(){},sequence(){}, _RF:{push(){},pop(){}}, _decorator:{ccclass:x=>x,property(){} } };
const recovered = require(path.resolve(__dirname, '../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/MenuLayer.js'));
const out={};
recovered((name)=> {
 if(name==='UILayer') return {default:UILayer};
 if(name==='Model') return {default:{instance:()=>({battleName:()=> '영천'})}};
 if(name==='Config') return {ENABLED_FEATURE:{EDIT:1},SOUND_INDEX:{}};
 if(name==='Instance') return {default:{LAYER:{HelperLayer:'HelperLayer',SaveLayer:'SaveLayer',LoadGameLayer:'LoadGameLayer',SettingLayer:'SettingLayer',PropertyLayer:'PropertyLayer',TerrainLayer:'TerrainLayer',TreasureLayer:'TreasureLayer',MsgBox:'MsgBox'}}};
 if(name==='Battle') return {default:{LAYER:{EditLayer2:'EditLayer2'}}};
 if(name==='BattleConfg') return {WEATHER:{QING:'QING',YIN:'YIN',FENG:'FENG',HAO_YU:'HAO_YU',XUE:'XUE'}};
 return {default:{}};
}, {}, out);
const SourceMenu=out.default;
function trace(c) {
 const menu=Object.create(SourceMenu.prototype); menu._count=0; const comps=new Map(), listeners=new Map(), events=[];
 menu.eFlag=()=>c.edit?1:0; menu.seekCompByName=(K,p,parent)=> {const key=p+(parent?':child':''); if(!comps.has(key)) { const value=new K(); if(p==='bg/contain/button12') value.node.active=false; comps.set(key,value); } return comps.get(key)};
 menu.seekNodeByName=(p)=>new Node(); menu.addTouchEventListener=(comp,fn)=>listeners.set(comp,fn); menu.removeFromParent=()=>events.push('remove'); menu.addLayer=(x)=>events.push('layer:'+String(x)); menu.dispatchEvent=(x)=>events.push('dispatch:'+x); menu.replaceScene=(x)=>events.push('scene:'+x); menu.cmdToast=()=>events.push('toast'); menu.playSoundEffect=()=>{};
 const loads=[], nodeEvents=new Map(); menu.loadByUrl=(cb)=>loads.push(cb); menu.node={on(name,cb){nodeEvents.set(name,cb)},emit(name){nodeEvents.get(name)?.()},runAction(){}};
 menu.onCreate({weather:c.weather,round:c.round,max_round:c.maxRound,flag:c.flag,...(c.switchWeather?{switch_weather:c.switchWeather,fn:()=>events.push('callback')}:{})});
 if(!c.switchWeather) while(loads.length) loads.shift()({err:false,ret:{width:216,height:200}});
 const weather={QING:1,YIN:2,FENG:3,HAO_YU:4,XUE:5}[c.weather]; const frames=c.samples.map(t=>Math.floor(t*6)%4);
 const buttons=[...Array(14)].map((_,i)=>{const b=comps.get('bg/contain/button'+i);return b?{i,active:b.node.active,interactable:b.interactable}:null});
 const initial={round:comps.get('bg/progressBar').progress*c.maxRound,progress:+comps.get('bg/progressBar').progress.toFixed(6),attached:true,buttons,weatherSheet:weather,switchWeatherSheet:c.switchWeather?({QING:1,YIN:2,FENG:3,HAO_YU:4,XUE:5}[c.switchWeather]):null,frames}; const inputs=[];
 for(const e of c.events){ if(e.startsWith('LOAD:')) { loads.shift()({err:false,ret:{width:216,height:200}}); inputs.push({event:e,attached:!events.includes('remove'),events:[...events],loaded:2-loads.length}); continue; } if(e.startsWith('FADE:')) { if(e==='FADE:2') {events.push('remove','callback')} inputs.push({event:e,attached:!events.includes('remove'),events:[...events],loaded:2-loads.length}); continue; } if(e==='CANCEL'){ const b=comps.get('Panel_cancel'); if(b) listeners.get(b)(b,2); inputs.push({event:e,attached:!events.includes('remove'),events:[...events]}); continue; } const [kind,i]=e.split(':');const b=comps.get('bg/contain/button'+i); if(b && b.node.active && b.interactable) listeners.get(b)(b,kind==='END'?2:kind==='MOVE'?1:0); inputs.push({event:e,attached:!events.includes('remove'),events:[...events]}); }
 return {id:c.id,initial,inputs};
}
process.stdout.write(JSON.stringify(cases.map(trace))+'\n');
