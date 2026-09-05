#!/usr/bin/env node
/* Executes recovered UnitListLayer/MineUnitInfoLayer/OtherUnitInfoLayer
 * factories.  Cocos is deliberately mocked only at the boundary; labels,
 * bars, listener filtering, InfoBase _next/_callback and lifecycle all run
 * from the recovered source. */
const fs=require('fs'),path=require('path');
const fixture=JSON.parse(fs.readFileSync(process.argv[2])); const out=process.argv[3];
let exportsByName={}; let nid=0;
function Node(name=''){this.name=name;this.id=++nid;this.children=[];this._parent=null;this.active=true;this.position={x:0,y:0};this.tag2=0;this.labels={};this.size={width:200,height:100};}
Object.defineProperty(Node.prototype,'parent',{get(){return this._parent},set(v){if(this._parent){const i=this._parent.children.indexOf(this);if(i>=0)this._parent.children.splice(i,1)}this._parent=v||null;if(v&&!v.children.includes(this))v.children.push(this)}});
Node.prototype.getContentSize=function(){return this.size}; Node.prototype.setContentSize=function(w,h){this.size={width:w,height:h}};
function Label(){this.node=new Node;this.string=''} function ProgressBar(){this.node=new Node;this.progress=0}
function UILayer(){this.node=new Node('root');this.nodes={};this.comps={};this.scheduled=[];this.events={};this.routes=[];this.dispatched=[];}
UILayer.prototype._setBg=function(){};UILayer.prototype.convertToWorldSpaceAR=function(p){return p||{x:0,y:0}};
UILayer.prototype.seekNodeByName=function(name,root){const k=(root?root.id:'root')+':'+name;if(!this.nodes[k]){const n=new Node(name);if(root)n.parent=root;this.nodes[k]=n}return this.nodes[k]};
UILayer.prototype.seekCompByName=function(K,name,root){const k=(root?root.id:'root')+':'+name;if(!this.comps[k]){const c=new K;c.node.name=name;if(root){c.node.parent=root;root.labels[name]=c}this.comps[k]=c}return this.comps[k]};
UILayer.prototype.addTouchEventListener=function(n,fn){n.listener=fn;n.node&&(n.node.listener=fn)};
UILayer.prototype.addEventListener=function(n,fn){this.events[n]=fn};UILayer.prototype.dispatchEvent=function(n,v){this.dispatched.push({name:n,id:v.id()});this.events[n]&&this.events[n](v)};
UILayer.prototype.schedule=function(fn){this.scheduled.push(fn)};UILayer.prototype.scheduleOnce=function(fn){this.scheduled.push(fn)};UILayer.prototype.unschedule=function(fn){this.scheduled=this.scheduled.filter(x=>x!==fn)};
UILayer.prototype.removeFromParent=function(){this.dead=true};UILayer.prototype.addLayer=function(layer,data){this.routes.push({layer,data})};UILayer.prototype.hasLayer=function(){return false};UILayer.prototype.getLayer=function(){};
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}},Label,ProgressBar,Node,Sprite:function(){},SpriteFrame:function(){},Button:function(){},v2:(x,y)=>({x,y}),v3:x=>x,color:(...x)=>x,macro:{REPEAT_FOREVER:-1},tween:()=>({to(){return this},start(){}}),instantiate:()=>new Node()};
function load(name,deps){const ex={};require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/'+name+'.js'))(n=>deps[n]||{}, {}, ex);return ex.default}
const InfoBase=load('InfoBaseLayer',{UILayer:{default:UILayer}});
const Config={ITEM_TYPE:{WEAPONS:0,ARMOR:1},UNIT_INFO_KEY:{HP:'HP',MP:'MP',EXP:'EXP',WQ_EXP:'WQ_EXP',HJ_EXP:'HJ_EXP',HP_ADD:'HP_ADD',MP_ADD:'MP_ADD',EXP_ADD:'EXP_ADD',WQ_EXP_ADD:'WQ_EXP_ADD',HJ_EXP_ADD:'HJ_EXP_ADD'}};
function makeUnit(x){let w={exp:()=>x.weaponExp||0,expLimit:()=>x.weaponMax||100},a={exp:()=>x.armorExp||0,expLimit:()=>x.armorMax||100};return {id:()=>x.id,name:()=>x.name,postsName:()=>x.post,lv:()=>x.lv,hp:()=>x.hpMax,mp:()=>x.mpMax,hp_cur:()=>x.hp,mp_cur:()=>x.mp,exp:()=>x.exp||0,expLimit:()=>x.expMax||100,equip:k=>k===0?w:k===1?a:null,unit(){return this}}}
function makeCombat(x){const profile=makeUnit(x);return {unit:()=>profile,hp:()=>x.hpMax,mp:()=>x.mpMax,hp_cur:()=>x.hp,mp_cur:()=>x.mp,node:{x:0,y:0}}}
function snapshotList(x,step){let rows=(x.item.parent?.children||[]).map(n=>({tag:n.tag2,labels:[String(n.labels.label0?.string??''),String(n.labels.label1?.string??'')]})).sort((a,b)=>a.tag-b.tag);return {step,active:x.node.active,pos:x.seekNodeByName('bg1').position,rows,dead:!!x.dead,events:x.dispatched.map(e=>({...e})),routes:x.routes.map(r=>({layer:r.layer,id:r.data.id()}))}}
function snapshotInfo(x,step){const bg=x.seekNodeByName('bg');let labels={};for(const c of Object.values(x.comps))if(c instanceof Label)labels[c.node.name]=(labels[c.node.name]||[]).concat(c.string);return {step,dead:!!x.dead,labels:Object.keys(labels).sort().map(k=>[k,labels[k]]),bars:Object.values(x.comps).filter(c=>c instanceof ProgressBar).map(c=>c.progress),kvs:x._kvs.map(v=>({idx:v.idx,src:v.src,dsc:v.dsc,max:v.max})),value:x._value&&{idx:x._value.idx,src:x._value.src,dsc:x._value.dsc,max:x._value.max},arys:x._arys?[...x._arys]:[]}}
function run(c){if(c.kind==='list'){const units=c.units.map(makeUnit);const L=load('UnitListLayer',{UILayer:{default:UILayer},Model:{default:{instance:()=>({unitsIter:fn=>units.forEach(fn)})}}});const x=new L;x.item=new Node('item');x.item.parent=new Node('items');x.onCreate({pos:{x:c.pos[0],y:c.pos[1]}});const trace=[snapshotList(x,'create')];for(const e of c.events){const [a,b,d]=e.split(':');if(a==='row'){const n=x.item.parent.children[+b];n&&n.listener&&n.listener(n,+d)}else {const n=x.seekNodeByName('Panel_cancel');n.listener(n,+b)}trace.push(snapshotList(x,e))}return trace}
 const L=load(c.kind==='mine'?'MineUnitInfoLayer':'OtherUnitInfoLayer',{InfoBaseLayer:{default:InfoBase},Config,UILayer:{default:UILayer}});const x=new L;const u=makeCombat(c.unit);x.onCreate({data:{c:u,...c.data},func:()=>{x.done=(x.done||0)+1}});const trace=[snapshotInfo(x,'create')];for(const e of c.events){if(e==='callback'&&x._handle)x._callback();trace.push(snapshotInfo(x,e))}return trace}
fs.writeFileSync(out,JSON.stringify(Object.fromEntries(fixture.cases.map(c=>[c.name,run(c)]))));
