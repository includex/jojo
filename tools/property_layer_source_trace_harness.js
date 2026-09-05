#!/usr/bin/env node
/* Executes recovered ui/PropertyLayer.js against a deliberately small Cocos surface. */
const fs=require('node:fs'), path=require('node:path'), assert=require('node:assert/strict');
const fixture=JSON.parse(fs.readFileSync(process.argv[2],'utf8')), output=process.argv[3];

function Node(kind='node'){this.kind=kind;this.active=true;this.children=[];this.tag2=null;this.labels={};this._parent=null}
Object.defineProperty(Node.prototype,'parent',{get(){return this._parent},set(v){if(this._parent){const i=this._parent.children.indexOf(this);if(i>=0)this._parent.children.splice(i,1)}this._parent=v;if(v&&!v.children.includes(this))v.children.push(this)}});
function clone(n){const x=new Node(n.kind);return x}
function NodePool(){this.nodes=[]} NodePool.prototype.put=function(n){n.parent=null;this.nodes.push(n)};NodePool.prototype.get=function(){return this.nodes.pop()};NodePool.prototype.size=function(){return this.nodes.length};NodePool.prototype.clear=function(){this.nodes=[]};
function Button(){this.node=new Node('button');this.interactable=true} function Label(){this.node=new Node('label');this.string=''} function Sprite(){this.node=new Node('sprite');this.spriteFrame=null}
function Layer(){this.nodes={};this.comps={};this.routes=[];this.m_manager={itemDir:()=>''}} Layer.prototype.onDestroy=function(){};Layer.prototype._setBg=function(){};Layer.prototype.removeFromParent=function(){this.removed=true};Layer.prototype.addLayer=function(layer,arg){this.routes.push({layer,item:arg.item.id()})};Layer.prototype.loadByUrl=function(cb){cb({err:true})};Layer.prototype.fastInitList=function(rows,_n,cb){rows.forEach(cb)};
Layer.prototype.addTouchEventListener=function(target,fn){target.listener=fn};
Layer.prototype.seekNodeByName=function(name,parent){
  if(name==='bg')return this.nodes.bg||(this.nodes.bg=new Node('bg'));
  if(/^panel[01]$/.test(name)){const n=this.nodes[name]||(this.nodes[name]=new Node(name));return n}
  if(name==='scrollview/view/content'){if(!parent.content){parent.content=new Node('content');parent.content.parent=parent;const template=new Node('row');template.parent=parent.content}return parent.content}
  const key=(parent?parent.kind+'/':'')+name;return this.nodes[key]||(this.nodes[key]=new Node(name));
};
Layer.prototype.seekCompByName=function(K,name,parent){
  if(K===Button){const key='button:'+name;return this.comps[key]||(this.comps[key]=new Button())}
  if(K===Label){if(!parent.labels[name])parent.labels[name]=new Label();return parent.labels[name]}
  if(K===Sprite){if(!parent.sprite)parent.sprite=new Sprite();return parent.sprite}
  return new K()
};
global.cc={Component:Layer,NodePool,Button,Label,Sprite,SpriteFrame:class{},instantiate:clone,color:(...v)=>v,_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}}};
const exported={};
require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/PropertyLayer.js'))(name=>{
 if(name==='UILayer')return{default:Layer};
 if(name==='Config')return{ITEM_TYPE:{WEAPONS:0,ARMOR:1,AUXILIARY:2,PROPERTY:3}};
 if(name==='Instance')return{default:{LAYER:{ItemLayer:'ItemLayer'}}};
 return {default:{}};
},{},exported);
assert.ok(exported.default,'recovered PropertyLayer export');
const Source=exported.default;
function item(raw){return {id:()=>raw.id,type:()=>raw.itemType<=19?0:raw.itemType<=25?1:raw.itemType<=45?3:2,lv:()=>raw.level,exp:()=>raw.exp,expLimit:()=>raw.expLimit,unitId:()=>raw.owner==null?65535:raw.id,name:()=>raw.name,name_property:()=>raw.typeName,icon:()=>raw.icon}}
const all=fixture.items.map(item), rawById=new Map(fixture.items.map(x=>[x.id,x]));
function makeLayer(){
 const store={allWeapons(mask){return all.filter(x=>{const raw=rawById.get(x.id());return !raw.equipped&&x.type()!==3&&(mask&(1<<x.type()))})},allProperty(){return all.filter(x=>x.type()===3&&(fixture.inventory[x.id()]||0)>0)},propertyCount(id){return fixture.inventory[id]||0}};
 const model={getWeaponsFromMineUnits(list,mask){all.filter(x=>{const raw=rawById.get(x.id());return raw.equipped&&(mask&(1<<x.type()))}).forEach(x=>list.push(x))},unitAttr2(id){return rawById.get(id).owner}};
 const OldInstance=global.__propertyInstance; // module closure looks up the dependency supplied below only at factory time, so supply mutable defaults.
 return {store,model};
}
// Re-instantiate the factory per run so its injected singleton mocks are precise.
function run(spec){
 const out={}; let ctx;
 require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/PropertyLayer.js'))(name=>{
   if(name==='UILayer')return{default:Layer}; if(name==='Config')return{ITEM_TYPE:{WEAPONS:0,ARMOR:1,AUXILIARY:2,PROPERTY:3},UNIT_ATTR_NAME2:{NAME:0}};
   if(name==='Instance')return{default:{LAYER:{ItemLayer:'ItemLayer'}}};
   if(name==='ItemStore')return{default:{instance:()=>ctx.store}}; if(name==='Model')return{default:{instance:()=>ctx.model}}; return{default:{}};
 },{},out); ctx=makeLayer(); const layer=new out.default(); layer.onCreate(); let scroll=0;
 const rowPayload=()=>{const content=layer._contents[layer._panel[1].active?1:0];return content.children.map(n=>({id:n.tag2.id(),labels:[0,1,2,3,4].filter(i=>n.labels['label'+i]).map(i=>n.labels['label'+i].string)}))};
 const snap=step=>{const rows=rowPayload(), max=Math.max(0,rows.length-1);scroll=Math.max(0,Math.min(scroll,max));return {step,selected:layer._sel,panels:layer._panel.map(p=>p.active),propertyInitialized:!!(layer._flag&2),attached:!layer.removed,rows,boundary:{count:rows.length,scroll,first:rows.length?scroll:-1,last:rows.length?Math.min(rows.length-1,scroll+4):-1},routes:layer.routes.slice()}};
 const trace=[snap('create')];
 for(const event of spec.events){const [kind,a,b]=event.split(':');if(kind==='tab'){const n=layer.nodes['bg/toggleContainer/toggle'+a];n.listener(n,+b)}else if(kind==='cancel'){layer.comps['button:button0'].listener(null,+a)}else if(kind==='row'){const rows=layer._contents[layer._panel[1].active?1:0].children;rows[+a]?.listener(rows[+a],+b)}else if(kind==='scroll')scroll=+a;trace.push(snap(event))}
 return trace;
}
const trace=Object.fromEntries(fixture.cases.map(c=>[c.name,run(c)]));fs.mkdirSync(path.dirname(output),{recursive:true});fs.writeFileSync(output,JSON.stringify(trace));console.log(JSON.stringify(trace));
