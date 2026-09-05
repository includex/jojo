#!/usr/bin/env node
/* Runs recovered ui/SettingLayer.js with only the Cocos APIs its handlers use. */
const fs=require('node:fs'), path=require('node:path'), assert=require('node:assert/strict');
const fixture=JSON.parse(fs.readFileSync(process.argv[2],'utf8')), output=process.argv[3];
let context;
function Node(name='node'){this.name=name;this.active=true;this.tag2=0;this.children=[]}
Node.prototype.stopAllActions=function(){}; Node.prototype.runAction=function(){};
function Toggle(){this.node=new Node('toggle');this.checkEvents=[];this.checked=false} Toggle.prototype.check=function(){this.checked=true};Toggle.prototype.uncheck=function(){this.checked=false};
function Slider(){this.node=new Node('slider');this.progress=0}
function Label(){this.node=new Node('label');this.string=''}
function Handler(){this.target=null;this.component='';this.handler=''}
function UILayer(){this.node=new Node('root');this.nodes={};this.comps={};this.m_ud=context.store;this.m_manager={currentSdk:()=>({haveLogin:()=>false}),restart(){context.events.push('restart')}}}
UILayer.prototype.onDestroy=function(){}; UILayer.prototype._setBg=function(){}; UILayer.prototype.rFlag=()=>0; UILayer.prototype.cmdToast=()=>{}; UILayer.prototype.addLayer=()=>{}; UILayer.prototype.addEventListener=()=>{};
UILayer.prototype.removeFromParent=function(){this.removed=true}; UILayer.prototype.addTouchEventListener=function(n,fn){n.listener=fn};
UILayer.prototype.seekNodeByName=function(name,parent){const key=(parent?parent.name+'/':'')+name;return this.nodes[key]||(this.nodes[key]=new Node(name))};
UILayer.prototype.seekCompByName=function(K,name,parent){const key=K.name+':'+(parent?parent.name+'/':'')+name; if(!this.comps[key])this.comps[key]=new K(); return this.comps[key]};
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}},Component:UILayer,Toggle,Slider,Label,instantiate:n=>new Node(n.name),sequence:()=>({repeatForever(){return this}}),fadeIn:()=>({}),fadeOut:()=>({})}; cc.Component.EventHandler=Handler;
const SOURCE=path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SettingLayer.js');
function load(){const exported={};require(SOURCE)(name=>{if(name==='UILayer')return{default:UILayer};if(name==='Config')return{GAME_SETTING:'GAME_SETTING',SETTING_FLAG:{BG_SOUND:1,EFFECT_SOUND:2,MINI_MAP:16}};if(name==='Sound')return{default:{getInstance:()=>({setMusicON:on=>context.events.push('music:'+on),setEffectON:on=>context.events.push('effect:'+on)})}};if(name==='Manager')return{default:{isWin32:()=>false,helper:()=>({code:0})},getManager:()=>({setGameSpeed:()=>context.events.push('applySpeed')})};if(name==='Tool')return{default:{len:o=>Object.keys(o).length}};if(name==='Instance')return{default:{LAYER:{}}};if(name==='Model')return{default:{instance:()=>({rFlagN:()=>0,stageReward:()=>null})}};return{default:{}}},{},exported);assert.ok(exported.default);return exported.default}
const Source=load();
function esc(s){return String(s).replace(/\\/g,'\\\\').replace(/"/g,'\\"')}
function run(spec){
 const writes=[], values={...spec.initial}; context={events:[],store:{getIntegerForKey:(k,d)=>Object.hasOwn(values,k)?values[k]:d,setIntegerForKey:(k,v)=>{values[k]=v;writes.push([k,v])},deleteValueForKey(){}}};
 const layer=new Source();layer.onCreate();
 function snap(step){return {step,attached:!layer.removed,flags:layer._setFlag,speed:layer._slid.progress,values:{...values},writes:writes.map(x=>[...x]),events:[...context.events]}}
 const trace=[snap('create')];
 for(const event of spec.events){const [kind,a,b]=event.split(':');if(kind==='flag')layer.check({node:{tag2:+a},isChecked:b==='true'});else if(kind==='radio')layer.check2({node:{tag2:(+a<<8)|+b}});else if(kind==='background'){const node=layer.seekNodeByName('panel3/item'+a,layer._content);node.tag2=+a;node.listener(node,2)}else if(kind==='slider'){layer._slid.progress=+a;layer.onSlider()}else if(kind==='close'){const node=layer.seekNodeByName(a==='2'?'Panel_cancel':'bg/button1');node.listener(node,+a)}else if(kind==='destroy')layer.onDestroy();trace.push(snap(event))}
 return trace;
}
const trace=Object.fromEntries(fixture.cases.map(c=>[c.name,run(c)]));fs.mkdirSync(path.dirname(output),{recursive:true});fs.writeFileSync(output,JSON.stringify(trace));console.log(JSON.stringify(trace));
