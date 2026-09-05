#!/usr/bin/env node
/* Actual recovered factory runner. Trace values are copied at every boundary:
 * original addLayer is given mutable arrays, which must never back-mutate prior snapshots. */
const fs=require('fs'),path=require('path'),F=JSON.parse(fs.readFileSync(process.argv[2]));let E={},next=0;
function N(){this.id=++next;this.children=[];this.tag2=0;this.active=true;this._parent=null;this.labels={}}
Object.defineProperty(N.prototype,'parent',{get(){return this._parent},set(p){if(this._parent){let i=this._parent.children.indexOf(this);if(i>=0)this._parent.children.splice(i,1)}this._parent=p||null;if(p&&!p.children.includes(this))p.children.push(this)}});
function Pool(){this.a=[]}Pool.prototype.put=function(x){if(x)x.parent=null;this.a.push(x)};Pool.prototype.get=function(){return this.a.pop()||new N};Pool.prototype.size=function(){return this.a.length};Pool.prototype.clear=function(){this.a=[]};
function L(){this.node=new N;this.string='';this.node.color=null}function B(){this.node=new N}function T(){this.node=new N}
function U(){this.node=new N;this.n={};this.c={};this.log=[]}U.prototype._setBg=function(){};
U.prototype.seekNodeByName=function(n){if(n.includes('content'))return this.n.content||(this.n.content=new N,this.n.content.children=[new N,new N],this.n.content.children.forEach(x=>x._parent=this.n.content),this.n.content);return this.n[n]||(this.n[n]=new N)};
U.prototype.seekCompByName=function(K,n,p){let k=(p?p.id:0)+':'+n;if(this.c[k])return this.c[k];let x=new K;if(K===L&&p)p.labels[n]=x;else x.node=p||x.node;this.c[k]=x;return x};
U.prototype.addTouchEventListener=function(n,h){n.listener=h;n.node&&(n.node.listener=h)};U.prototype.fastInitList=function(a,b,h){(a||[]).forEach(h)};
U.prototype.addLayer=function(x,v){this.log.push({layer:x,index:v.index,unitIds:v.units.map(z=>z.index ? z.index() : z.id()),flag:v.flag})};
U.prototype.removeFromParent=function(){this.dead=true};U.prototype.onDestroy=function(){this.baseDestroyed=true};
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}},Component:U,NodePool:Pool,Button:B,Toggle:T,Label:L,instantiate:()=>new N(),color:(...x)=>x};
require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/ForcesListLayer.js'))(n=>n==='BattleConfg'?{UNIT_STATUS_LIFT:{DOWN:0,UP:1}}:n==='UILayer'?{default:U}:n==='Config'?{UNIT_ATTR_NAME2:{ATT:0,MOR:4}}:n==='Instance'?{default:{LAYER:{UnitInfoLayer:'UnitInfoLayer'}}}:{},{},E);
function u(x){return{index:()=>x.index,id:()=>x.id,unit:()=>({isFamous:()=>x.famous,name:()=>''+x.id,postsName:()=>'',lv:()=>1,hp:()=>10,mp:()=>5,ability:()=>0}),isFamous:()=>x.famous,name:()=>''+x.id,postsName:()=>'',lv:()=>1,hp:()=>10,mp:()=>5,ability:()=>0,hp_cur:()=>10,mp_cur:()=>5,status:()=>x.status??9,isZhongDu:()=>!!x.poison,isFengZhou:()=>!!x.fengZhou}}
function color(v){return Array.isArray(v)?v[0]===139?'red':v[0]===17?'blue':'black':'black'}
function snapshot(l,step){return {step,sel:l._sel,rows:l._content.children.map(n=>({tag:n.tag2,labels:Array.from({length:10},(_,i)=>String(n.labels['label'+i]?.string??'')),colors:Array.from({length:10},(_,i)=>color(n.labels['label'+i]?.node.color))})),tabsVisible:l.seekNodeByName('bg1/toggleContainer').active,dead:!!l.dead,baseDestroyed:!!l.baseDestroyed,routes:l.log.map(x=>({...x,unitIds:[...x.unitIds]})),n1:l._nodes1.size(),n2:l._nodes2.size()}}
function run(q){let l=new E.default();l.onCreate({flag:q.flag,ms:q.mine.map(u),es:q.enemy.map(u)});let o=[snapshot(l,'create')];for(let z of q.events){let p=z.split(':');if(p[0]==='tab'){let c=l.seekCompByName(T,'toggle'+p[1],l.seekNodeByName('bg1/toggleContainer'));if(c.listener)c.listener(c,+p[2])}else if(p[0]==='row'){let n=l._content.children[+p[1]];n?.listener?.(n,+p[2])}else if(p[0]==='close'){let c=l.seekCompByName(B,'Panel_cancel');c.listener(c,+p[1])}else l.onDestroy();o.push(snapshot(l,z))}return o}
fs.writeFileSync(process.argv[3],JSON.stringify(Object.fromEntries(F.cases.map(q=>[q.name,run(q)]))));
