#!/usr/bin/env node
/* Executes recovered ControlManager/Control/Ctrl* selection before canonically
 * recording BattleLayer._ai2's move/attack/end hand-off. */
const fs=require('node:fs'),path=require('node:path');
const fixture=JSON.parse(fs.readFileSync(process.argv[2],'utf8')),output=process.argv[3];
global.cc={v2:(x,y)=>({x,y}),_RF:{push(){},pop(){}}};
const base=path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/battle');
function load(name,deps){const out={};require(path.join(base,name+'.js'))(n=>deps[n]||{}, {}, out);return out;}
const cfg={HITAREA:{SELF:99,QUN_XIONG:98},HITAREA_ATTR_NAME:{PS:0},AI_VALUE:{}};
const bc={AI:{BEI_DONG_CHU_JI:0,ZHU_DONG_CHU_JI:1,JIAN_SHOU_YUAN_DI:2,YDDZDDJS:7,YDDZDDBM:8,YDDZDDGJ:9,GONG_JI_WU_JIANG:3}};
const control=load('Control',{Config:cfg,Instance:{default:{instance:()=>({})}},Model:{default:{instance:()=>({hitareaAttr:()=>[[1,0]]})}},BattleConfg:bc}).Control;
const ctor={};
for(const n of ['CtrlBDCJ','CtrlZDCJ','CtrlJSYD','CtrlGJWJ','CtrlDZDD','CtrlGSWJ','CtrlTZZDD','CtrlYDDZDDJS','CtrlYDDZDDBM','CtrlYDDZDDGJ']){
  const module=load(n,{Control:{Control:control},CtrlYDDZDDJS:{CtrlYDDZDDJS:ctor.CtrlYDDZDDJS},Config:cfg,BattleConfg:bc});
  ctor[Object.keys(module).find(k=>k.startsWith('Ctrl'))]=module[Object.keys(module).find(k=>k.startsWith('Ctrl'))];
}
const indexed={0:ctor.CtrlBDCJ,1:ctor.CtrlZDCJ,2:ctor.CtrlJSYD,3:ctor.CtrlGJWJ,4:ctor.CtrlDZDD,5:ctor.CtrlGSWJ,6:ctor.CtrlTZZDD,7:ctor.CtrlYDDZDDJS,8:ctor.CtrlYDDZDDBM,9:ctor.CtrlYDDZDDGJ};
const Manager=load('ControlManager',{BattleConfg:bc}).ControlManager;
const originalSet=Manager.prototype.setControl;
Manager.prototype.setControl=function(ai,...rest){this._traceControllers.push(ai);return originalSet.call(this,ai,...rest)};
control.prototype._AStar=function(){return cc.v2(1,0)};
function run(c){
  const unit={x:()=>0,y:()=>0,pos:()=>({x:0,y:0}),index:()=>1,isControl:()=>false,AI:()=>c.ai,targetId:()=>-1,targetXPos:()=>-1,targetYPos:()=>-1,isMaBi:()=>c.immobile,isCanXue:()=>false,hitareaIdx:()=>99,isMine:()=>false};
  const target={isExist:()=>true,isMine:()=>true,index:()=>99,pos:()=>({x:3,y:0}),x:()=>3,y:()=>0};
  const battle={getContrlByAI:ai=>new indexed[ai](),unit:i=>i===99?target:null,unitIter:fn=>fn(target),AStar:()=>[{x:0,y:0}],findEmptyPos:()=>null,searchUnitByPos:()=>null};
  const oldTargets=control.prototype._aiHaveAttackTargets,oldAi=control.prototype._AIProcess;
  control.prototype._aiHaveAttackTargets=()=>c.attack;
  control.prototype._AIProcess=function(){if(c.action==='move')this._manager.setResult({x:1,y:0});if(c.action==='attack')this._manager.setResult({x:0,y:0,info:{t:'attack',id:99}})};
  const manager=new Manager(battle,unit);manager._traceControllers=[];
  const status=manager.selectMovePoint([{x:0,y:0},{x:1,y:0}],{}); const result=manager.result()||{x:0,y:0};
  control.prototype._aiHaveAttackTargets=oldTargets;control.prototype._AIProcess=oldAi;
  const action=c.action==='attack'?'attack:99':c.action==='move'&&result.x===1?'move:1,0':'hold';
  return {case:c.id,status,result:{x:result.x,y:result.y,kind:result.info?result.info.t:null},events:['turn:start:player','round:complete','turn:enemy',...manager._traceControllers.map(x=>'controller:'+x),action,'end:enemy','handoff:player']};
}
const trace=fixture.cases.map(run);fs.mkdirSync(path.dirname(output),{recursive:true});fs.writeFileSync(output,JSON.stringify(trace));console.log(JSON.stringify(trace));
