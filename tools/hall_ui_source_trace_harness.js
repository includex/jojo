#!/usr/bin/env node
/* Recovered HallMenuLayer/HallCommandLayer factories with a Cocos-minimal mock. */
const fs = require('fs'), path = require('path');
const fixture = JSON.parse(fs.readFileSync(process.argv[2], 'utf8'));

class Sprite { constructor() { this.spriteFrame = null; } }
class Node {
  constructor(name = '') { this.name=name; this.active=true; this.tag2=0; this._width=100; this.widthWrites=[]; Object.defineProperty(this,'width',{get:()=>this._width,set:v=>{this._width=v;this.widthWrites.push(v)}}); this.listener=null; this.actions=0; this.sprite=new Sprite(); }
  getComponent() { return this.sprite; }
  getContentSize() { return {width:100,height:10}; }
  runAction(action) { this.actions++; runAction(action); }
}
class Button { constructor() { this.node=new Node(); this.interactable=true; } }
class Label { constructor() { this.node=new Node(); this.string=''; } }
function runAction(a) { if (Array.isArray(a)) a.forEach(runAction); else if (a && a.kind==='call') a.fn(); }
/* The recovered ES5 subclass calls its base through `UILayer.apply(this, ...)`. */
function UILayer() { this.node=new Node('root'); this.nodes={}; this.comps={}; this.layers=[]; this.dead=false; this.feature=0; this.events=[]; this.toasts=[]; }
UILayer.prototype.seekNodeByName=function(n) { return this.nodes[n] || (this.comps[n] ? this.comps[n].node : (this.nodes[n]=new Node(n))); };
UILayer.prototype.seekCompByName=function(K,n) { if(this.comps[n])return this.comps[n]; const c=new K(); c.node=this.nodes[n]||c.node; this.nodes[n]=c.node; return this.comps[n]=c; };
UILayer.prototype.addTouchEventListener=function(target,fn,priority) { const node=target.node||target; node.listener=fn; node.listenerTarget=target; node.listenerPriority=priority; };
UILayer.prototype.addLayer=function(layer,payload) { if(layer==='MsgBox'&&payload&&payload.fn)this.lastMsgBox=payload.fn; this.layers.push({layer,payload:clean(payload)}); };
UILayer.prototype.replaceScene=function(name) { this.layers.push({layer:`scene:${name}`,payload:null}); };
UILayer.prototype.removeFromParent=function() { this.dead=true; };
UILayer.prototype.cmdToast=function(text) { this.toasts.push(text); };
UILayer.prototype.eFlag=function() { return this.feature; };
UILayer.prototype.dispatchEvent=function(_name,value) { this.events.push(value); };
function clean(v) { if(v==null)return null; const out={}; for(const [k,x] of Object.entries(v))if(typeof x!=='function')out[k]=x; return out; }
global.cc={
  _RF:{push(){},pop(){}}, _decorator:{ccclass:x=>x,property:()=>()=>{}}, Component:UILayer, Node, Button, Label, Sprite,
  hide:()=>({kind:'hide'}),show:()=>({kind:'show'}),delayTime:()=>({kind:'delay'}),callFunc:fn=>({kind:'call',fn}),sequence:a=>a,
  tween:target=>({to(_time,changes){this.changes=changes;return this},delay(){return this},call(fn){this.fn=fn;return this},start(){Object.assign(target,this.changes);this.fn()}})
};
function load(name,resolver) { const e={}; require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui',name))(resolver,{},e); return e.default; }
function resolve(name) {
  if(name==='UILayer')return {default:UILayer};
  if(name==='Config')return {ENABLED_FEATURE:{EDIT:1},HALL_FLAG:{MENU:1,COMMAND:2}};
  if(name==='Instance')return {default:{LAYER:{MsgBox:'MsgBox',SaveLayer:'SaveLayer',LoadGameLayer:'LoadGameLayer',SettingLayer:'SettingLayer',ForcesListLayer:'ForcesListLayer',PropertyLayer:'PropertyLayer',TerrainLayer:'TerrainLayer',TreasureLayer:'TreasureLayer',HelperLayer:'HelperLayer'}}};
  if(name==='Model')return {default:{instance:()=>({ambition:()=>50,unitsIter:fn=>['unit-a','unit-b'].forEach(fn)})}};
  if(name==='Hall')return {default:{LAYER:{EditLayer4:'EditLayer4'}}};
  return {};
}
const Menu=load('HallMenuLayer.js',resolve), Command=load('HallCommandLayer.js',resolve);
function deliver(node,event) { if(!node.active||!node.listener)return false; node.listener(node.listenerTarget,event); return true; }
function menuSnap(x,step,callbacks) {
  const buttons=Array.from({length:10},(_,i)=>{const n=x.seekNodeByName(`bg/button${i}`);return [n.active,n.tag2,!!n.listener]});
  const outer=x.seekNodeByName('bg/bar'), inner=x.seekNodeByName('bar'), normalBar=x.seekNodeByName('bg/bar/bar');
  return {step,zIndex:x.node.zIndex||0,labels:[x.seekCompByName(Label,'bg/bg0/label').string,x.seekCompByName(Label,'bg/bg1/label').string],buttons,
    bar:{outer:outer.sprite.spriteFrame&&outer.sprite.spriteFrame.id||null,inner:inner.sprite.spriteFrame&&inner.sprite.spriteFrame.id||null,startWidth:inner.widthWrites[0]||normalBar.widthWrites[0]||100,endWidth:inner.widthWrites.length?inner.width:normalBar.width,flagActions:[x.seekNodeByName('flag0').actions,x.seekNodeByName('flag1').actions]},
    attached:!x.dead,layers:JSON.parse(JSON.stringify(x.layers)),callbackCount:callbacks,cancelPriority:x.seekNodeByName('Panel_cancel').listenerPriority||null,toasts:[...x.toasts]};
}
function commandSnap(x,step,callbacks) {
  const menu=x.seekCompByName(Button,'button'), bs=Array.from({length:5},(_,i)=>x.seekCompByName(Button,`button${i}`));
  return {step,active:[menu.node.active,...bs.map(b=>b.node.active)],tags:bs.map(b=>b.node.tag2),priorities:[menu.node.listenerPriority||null,...bs.map(b=>b.node.listenerPriority||null)],listeners:[!!menu.node.listener,...bs.map(b=>!!b.node.listener)],events:[...x.events],callbackCount:callbacks,attached:!x.dead};
}
function runMenu(c) {
  const x=new Menu();x.feature=c.edit?1:0;x.bgs=[{id:'red'},{id:'yellow'},{id:'blue'}];let callbacks=0;
  x.onCreate(c.ambition===null?{eventName:c.eventName,stageName:c.stageName}:{ambition1:c.ambition[0],ambition2:c.ambition[1],func:()=>callbacks++});
  const trace=[menuSnap(x,'create',callbacks)];
  for(const event of c.events){const p=event.split(':'); if(p[0]==='button')deliver(x.seekNodeByName(`bg/button${p[1]}`),+p[2]);else if(p[0]==='cancel')deliver(x.seekNodeByName('Panel_cancel'),+p[1]);else if(p[0]==='msgbox'&&x.lastMsgBox)x.lastMsgBox(+p[1]);trace.push(menuSnap(x,event,callbacks));} return trace;
}
function runCommand(c) {
  const x=new Command();let callbacks=0;x.onCreate({flag:c.flag,func:()=>callbacks++});const trace=[commandSnap(x,'create',callbacks)];
  for(const event of c.events){const p=event.split(':');if(p[0]==='menu')deliver(x.seekCompByName(Button,'button').node,+p[1]);else deliver(x.seekCompByName(Button,`button${p[1]}`).node,+p[2]);trace.push(commandSnap(x,event,callbacks));}return trace;
}
const out=Object.fromEntries(fixture.cases.map(c=>[c.id,c.kind==='menu'?runMenu(c):runCommand(c)]));fs.mkdirSync(path.dirname(process.argv[3]),{recursive:true});fs.writeFileSync(process.argv[3],JSON.stringify(out));
