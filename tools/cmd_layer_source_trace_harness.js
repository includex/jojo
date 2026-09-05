#!/usr/bin/env node
/* Executes the recovered CmdLayer factory with a deliberately small Cocos
 * surface.  The trace keeps all mutations: feature flags, ItemStore changes,
 * persistence, dispatched registration payloads and modal/layer routes. */
const fs=require('fs'),path=require('path');
const fixture=JSON.parse(fs.readFileSync(process.argv[2],'utf8'));
let spec;
function Node(){this.children=[];this.active=true;this.tag2=0;this.parent=null;this._comps=new Map()}
function Label(){this.node=new Node();this.string=''}
function Toggle(){this.node=new Node();this.isChecked=false}
function UILayer(){this._nodes={};this._events=[];this._layers=[];this._toasts=[];this._writes=[];this._props=[];this._weapons=[];this._urls=[];this._dispatch=[];this._restart=0;this.m_ud={setIntegerForKey:(k,v)=>this._writes.push([k,v])};this.m_manager={m_deviceId:spec.deviceId,setGameSpeed:()=>this._events.push('setGameSpeed'),restart:()=>this._restart++}}
UILayer.prototype._setBg=function(){};UILayer.prototype.rFlag=function(){return spec.rFlag};UILayer.prototype.eFlag=function(){return spec.eFlag};
UILayer.prototype.seekNodeByName=function(name,parent){if(name==='item0'&&parent){if(!parent.item0){parent.item0=new Node();parent.item0.parent=parent;parent.children.push(parent.item0)}return parent.item0}if(parent){parent._nodes||(parent._nodes={});return parent._nodes[name]||(parent._nodes[name]=new Node())}return this._nodes[name]||(this._nodes[name]=new Node())};
UILayer.prototype.seekCompByName=function(K,name,node){node=node||this.seekNodeByName(name);let k=K.name+':'+name;if(!node._comps.has(k)){let x=new K();x.node=node;node._comps.set(k,x)}return node._comps.get(k)};
UILayer.prototype.addTouchEventListener=function(node,fn){node.listener=fn};UILayer.prototype.addLayer=function(layer,args){this._layers.push({layer,args:args?{flag:args.flag??null,txt:args.txt??null}:null});this._prompt=args&&args.fn?args.fn:null};UILayer.prototype.removeFromParent=function(){this._events.push('remove')};UILayer.prototype.dispatchEvent=function(n,p){this._dispatch.push([n,{money:p.money,count:p.count,sFlag:p.sFlag,eFlag:p.eFlag,rFlag:p.rFlag}])};UILayer.prototype.cmdToast=function(s){this._toasts.push(s)};
function clone(n){let x=new Node();return x}
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}},Component:UILayer,Label,Toggle,instantiate:clone,color:x=>x,sys:{openURL:u=>global.__layer._urls.push(u)}};
const ItemStore={instance(){return global.__store}};
function Item(id){this.id=id} Item.prototype.itemType=function(){return this.id===100?10:this.id===101?11:0};Item.prototype.type=function(){return this.id===100?1:2};
const Model={instance(){return global.__model}};
const Unit={countEquipLevel:x=>x+10};
const Config={ENABLED_FEATURE:{YJQDJ:1,YJQB:2},ITEM_PROP_OFFSET:100,ITEM_PROP_N:103,ITEM_EQUIP_TYPE:{RESUME_HP:10,POSTS:11},ITEM_ATTR_NAME:{TREASURE:'TREASURE'},ITEM_TYPE:{PROPERTY:1}};
const Instance={default:{LAYER:{MsgBox:'MsgBox',skmLayer:'skmLayer'}}};
function load(){let ex={};require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/CmdLayer.js'))(n=>({UILayer:{default:UILayer},UserDefault:{default:{getInstance:()=>({setIntegerForKey:(k,v)=>global.__layer._writes.push([k,v]),setStringForKey:(k,v)=>global.__layer._writes.push([k,v])})}},Config,Instance,Item:{default:Item},ItemStore:{default:ItemStore},Model:{default:Model},Unit:{default:Unit}}[n]||{}),{},ex);return ex.default}
const CmdLayer=load();
function snapshot(l,step){return {step,eFlag:l._eFlag,rFlag:l._rFlag,sFlag:l._sFlag,label:l._lab.string,selected:l._items.map(n=>!!l.seekCompByName(Toggle,'toggle1',n).node.active),checked:l._items.map(n=>!!l.seekCompByName(Toggle,'toggle',n).isChecked),buttons:[1,2,3,4,5].map(i=>!!l.seekNodeByName('Logo_12-1/button'+i).active),toasts:[...l._toasts],writes:[...l._writes],props:[...l._props],weapons:[...l._weapons],urls:[...l._urls],dispatch:[...l._dispatch],layers:l._layers.map(x=>({layer:x.layer,args:x.args})),events:[...l._events],restart:l._restart}};
function run(c){spec=c;let l=new CmdLayer();global.__layer=l;global.__store={pushProperty:(id,n=1,lvl=0)=>l._props.push([id,n,lvl]),pushWeapon:(id,lvl)=>l._weapons.push([id,lvl])};global.__model={setEFlag:v=>l._events.push('setEFlag:'+v),unitsIter:fn=>c.units.some(x=>fn(x)),averageLv:()=>4,itemIter:fn=>c.inventory.forEach((x,i)=>fn({TREASURE:x.treasure},x.id,i)),hasItem:()=>false};l.onCreate();let out=[snapshot(l,'create')];for(let e of c.events){let p=e.split(':');if(p[0]==='item'){let n=l._items[+p[1]];n.listener(n,+p[2])}else if(p[0]==='button'){let n=l.seekNodeByName('Logo_12-1/button'+p[1]);n.listener(n,+p[2])}else if(p[0]==='prompt'||p[0]==='cancelPrompt'){let fn=l._prompt;l._prompt=null;if(fn)fn(+p[1])}out.push(snapshot(l,e))}return out}
const result=Object.fromEntries(fixture.cases.map(c=>[c.name,run(c)]));fs.mkdirSync(path.dirname(process.argv[3]),{recursive:true});fs.writeFileSync(process.argv[3],JSON.stringify(result));console.log(JSON.stringify(result));
