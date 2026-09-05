#!/usr/bin/env node
/* Factory-level trace for NoticeInfo, Help, InputBox, SelectList and List. */
const fs=require('fs'),path=require('path'); const fixture=JSON.parse(fs.readFileSync(process.argv[2],'utf8'));
let nextNodeId=1; class Node { constructor(name=''){this.name=name;this.uid=nextNodeId++;this.active=true;this.tag2=0;this.children=[];this.parent=null;this.listener=null;this.position={x:0,y:0};this.size={width:100,height:20};this.components={};} set parent(p){if(this._parent&&this._parent.children)this._parent.children=this._parent.children.filter(x=>x!==this);this._parent=p;if(p&&p.children&&!p.children.includes(this))p.children.push(this)}get parent(){return this._parent}getContentSize(){return {...this.size}}setContentSize(s){this.size={...s}}stopAllActions(){}runAction(a){if(a&&a.fn)a.fn()}getComponent(K){return this.components[K.name]||(this.components[K.name]=new K(this))} }
class Label{constructor(node){this.node=node||new Node();this.string=''}_forceUpdateRenderData(){}} class Button{constructor(node){this.node=node||new Node()}} class Toggle{constructor(node){this.node=node||new Node();this.isChecked=false}} class EditBox{constructor(node){this.node=node||new Node();this.string=''}} class Sprite{constructor(node){this.node=node||new Node();this.spriteFrame=null}} class ScrollView{}
class NodePool{constructor(){this.a=[]}get(){return this.a.pop()||new Node('item')}put(x){if(x){x.parent=null;this.a.push(x)}}size(){return this.a.length}clear(){this.a=[]}}
function clone(n){const o=new Node(n.name);o.children=n.children.map(clone);o.children.forEach(x=>x.parent=o);return o}
function UILayer(){this.nodes={};this.comps={};this.dead=false;this.layers=[];}
UILayer.prototype.seekNodeByName=function(n,parent){const key=(parent?parent.uid+'/':'')+n; if(this.nodes[key])return this.nodes[key]; const x=new Node(n);this.nodes[key]=x;if(parent)x.parent=parent;return x};
UILayer.prototype.seekCompByName=function(K,n,parent){const key=K.name+':'+(parent?parent.uid+'/':'')+n;if(this.comps[key])return this.comps[key];const node=this.seekNodeByName(n,parent); const c=new K(node);this.comps[key]=c;return c};
UILayer.prototype.addTouchEventListener=function(target,fn,p){const n=target.node||target;n.listener=fn;n.priority=p};UILayer.prototype.removeFromParent=function(){this.dead=true};UILayer.prototype._setBg=function(){};UILayer.prototype.onDestroy=function(){};
const store={};global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}},Node,NodePool,Label,Button,Toggle,EditBox,Sprite,ScrollView,v2:(x,y)=>({x,y}),instantiate:clone,moveTo:()=>({easing(){return this}}),easeQuarticActionOut:()=>({})};
function load(file){
  const e={};
  require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui',file))(n=>{
    if(n==='UILayer') return {default:UILayer};
    if(n==='UserDefault') return {default:{getInstance:()=>({getStringForKey:k=>store[k],setStringForKey:(k,v)=>{store[k]=v}})}};
    if(n==='Instance') return {default:{range:(x,a,b)=>Math.min(b,Math.max(a,x))}};
    return {};
  },{},e);
  return e.default;
}
const Notice=load('NoticeInfoLayer.js'),Help=load('HelpLayer.js'),Input=load('InputBox.js'),Select=load('SelectListLayer.js'),List=load('ListLayer.js');
function hit(n,e){if(n.listener)n.listener(n,e)} function snap(x,kind,extra={}){return {kind,attached:!x.dead,...extra}}
function run(c){store.INPUT_BOX=c.saved;let calls=[];let x,trace=[];if(c.kind==='notice'){x=new Notice();const bg=x.seekNodeByName('bg');bg.size={width:100,height:20}; const button=x.seekCompByName(Button,'button',bg).node; const middle=new Node('button-middle'), inner=new Node('button-inner');middle.parent=button;inner.parent=middle; const content=x.seekNodeByName('bg/scrollview/view/content');x.onCreate();trace.push(snap(x,c.kind,{show:x._show,count:content.children.length}));for(const z of c.events){let[a,b]=z.split(':');if(a==='toggle')hit(button,+b);else if(a==='notice'){const f=x._events&&x._events.NOTICE_MSG; if(f)f({txt:b})}trace.push(snap(x,c.kind,{show:x._show,count:content.children.length}))}return trace}
if(c.kind==='help'){x=new Help();x.onCreate();trace.push(snap(x,c.kind,{label:x.seekCompByName(Label,'bg1/box1/box0/scrollview/view/content/label').string}));for(const z of c.events){let[a,b]=z.split(':');hit(x.seekNodeByName(a==='close'?'bg1/button7':'Panel_cancel'),+b);trace.push(snap(x,c.kind))}return trace}
if(c.kind==='input'){x=new Input();x.onCreate({func:v=>calls.push(v)});x.seekCompByName(EditBox,'bg0/box1/editbox').string=c.text;trace.push(snap(x,c.kind,{value:x.seekCompByName(EditBox,'bg0/box1/editbox').string,saved:store.INPUT_BOX,calls:[...calls]}));for(const z of c.events){const[a,i,e]=z.split(':');hit(x.seekNodeByName('bg0/button'+i),+e);trace.push(snap(x,c.kind,{value:x.seekCompByName(EditBox,'bg0/box1/editbox').string,saved:store.INPUT_BOX,calls:[...calls]}))}return trace}
if(c.kind==='select'){x=new Select();x.onCreate({list:c.list,pageCount:c.pageCount,sel:c.sel,func:v=>calls.push(v)});const ss=()=>snap(x,c.kind,{page:x._page,label:x.seekCompByName(Label,'Logo_12-1/label').string,sel:x._sel,rows:x._content.children.map(n=>[n.tag2,x.seekCompByName(Label,'label',n).string,x.seekNodeByName('box6',n).active]),calls:[...calls]});trace.push(ss());for(const z of c.events){let[a,i,e]=z.split(':');let n=a==='row'?x._curItems.find(q=>q.tag2===+i):x.seekNodeByName('Logo_12-1/button'+({ok:0,cancel:1,prev:2,next:3}[a]));hit(n,+(e||i));trace.push(ss())}return trace}
x=new List();x.imgs=['A','B'];x.onCreate({flag:c.flag,list:c.list,func:v=>calls.push(v)});const ls=()=>snap(x,c.kind,{rows:x._content.children.map(n=>[n.tag2,x.seekCompByName(Toggle,'toggle',n).isChecked]),calls:[...calls]});trace.push(ls());for(const z of c.events){let[a,i,e]=z.split(':');let n=a==='row'?x._content.children.find(q=>q.tag2===+i):x.seekNodeByName(a==='ok'?'Logo_12-1/button0':'Panel_cancel');hit(n,+(e||i));trace.push(ls())}return trace}
// Notice uses the inherited event bus; preserve factory code instead of emulating it in traces.
UILayer.prototype.addEventListener=function(k,fn){(this._events||(this._events={}))[k]=fn};
const out=Object.fromEntries(fixture.cases.map(c=>[c.id,run(c)]));fs.mkdirSync(path.dirname(process.argv[3]),{recursive:true});fs.writeFileSync(process.argv[3],JSON.stringify(out));
