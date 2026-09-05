/* Runs recovered BattleViewLayer/DuleLayer/FightUnit factories with only the Cocos surface they use. */
const fs=require('fs'), path=require('path');
const fixture=JSON.parse(fs.readFileSync(process.argv[2],'utf8'));
const out=process.argv[3];
global.cc={
 _RF:{push(){},pop(){}}, _decorator:{ccclass:x=>x,property:()=>()=>{}},
 Component:function(){}, color:(r,g,b)=>({r,g,b}), v3:(x,y)=>({x,y}), SpriteFrame:function(){}, Sprite:function(){}, ScrollView:function(){}, Layout:function(){}, Label:function(){}, Animation:function(){},
 instantiate:n=>n.clone(),
};
function UILayer(){this.listeners={};this.dispatched=[]}
UILayer.prototype.loadByUrl=function(cb){cb({err:false,ret:'frame'})};UILayer.prototype.addEventListener=function(k,fn){this.listeners[k]=fn};UILayer.prototype.dispatchEvent=function(k){this.dispatched.push(k)};UILayer.prototype.seekCompByName=function(_c,_n,node){return node.label};
function factory(name,deps){let exp={};require(path.join(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/battle',name+'.js'))(x=>deps[x],exp,exp);return exp.default}
const BattleView=factory('BattleViewLayer',{UILayer:{default:UILayer},Config:{TITLE_SIZE:48}});
const Dule=factory('DuleLayer',{UILayer:{default:UILayer}});
let activeAnime,currentLayer;
const UIFrame={default:{CreateAnime:()=>activeAnime}};
const FightUnit=factory('FightUnit',{UIFrame,Manager:{getManager:()=>({getCurScene:()=>({_battleLayer:currentLayer})})}});
const marker=()=>({position:null,color:null,opacity:null,label:{string:''},parent:null,clone(){return marker()}});
function view(c){const x=new BattleView();x.map={node:{getContentSize:()=>({width:480,height:384})}};x.sv={content:{getComponent:()=>({updateLayout(){}})}};x.btn=marker();x.btn.parent={};x.onCreate({map:c.map,pos:c.pos});const trace=[];const snap=step=>({step,mapPath:x.map.spriteFrame,markers:x._nodes.map(n=>({x:n.position.x,y:n.position.y,label:n.label.string,red:n.color&&n.color.r===255&&n.color.g===0,opacity:n.opacity})),events:x.dispatched});trace.push(snap('create'));for(const event of c.events){if(event.startsWith('unit:'))x.listeners.BATTLE_UNIT_N(+event.slice(5));trace.push(snap(event))}return trace}
async function fight(c){const x=new FightUnit();const ev=[];let mat='';let node={position:{x:c.node[0],y:c.node[1]},scaleX:c.node[2],parent:{position:{x:c.parent[0],y:c.parent[1]},scaleX:c.parent[2]},getComponent(type){if(type===cc.Sprite)return {setMaterial:(_i,m)=>{mat=m;ev.push('material:'+({g:'gray',h:'highlight',d:'def'}[m]))},getMaterial:()=>({setProperty:(_k,v)=>ev.push('value:'+v)})};return anim}};let anim={play:a=>{anim.played=a},once:(_e,fn)=>{anim.finished=fn}};activeAnime=anim;x.node=node;currentLayer={loadUnitPicture:async()=> 'frame'};const battle={unitAnime:'anime',playBackgroundSound:n=>ev.push('background:'+n),playSoundEffect:n=>ev.push('effect:'+n),gray:'g',highLight:'h',def:'d'};await x.createWithInfo(battle, {unit:()=>({id:()=>1}),moveSound:()=> c.events.includes('sound:321')?321:-1}, 0);const anime=activeAnime;anime.__cb1(c.events.includes('sound:321')?321:'yidong');for(const e of c.events.filter(v=>v.startsWith('shader:')))anime.__cb2(+e.slice(7));x.setActionDir(c.action,c.events.includes('finished')?()=>ev.push('finished'):null);if(anim.finished)anim.finished();return [{step:'create',parent:[node.parent.position.x,node.parent.position.y,node.parent.scaleX],node:[node.position.x,node.position.y,node.scaleX],action:x._action,animation:anim.played||'',events:ev}];}
function dule(c){const x=new Dule();const trace=[];for(const e of c.events){if(typeof x[e]==='function')x[e]();trace.push({step:e,events:[],attached:true})}return trace}
(async()=>{const r={};for(const c of fixture.cases)r[c.name]=c.kind==='view'?view(c):c.kind==='fight'?await fight(c):dule(c);fs.mkdirSync(path.dirname(out),{recursive:true});fs.writeFileSync(out,JSON.stringify(r));console.log(JSON.stringify(r))})().catch(e=>{console.error(e);process.exit(1)});
