#!/usr/bin/env node
const fs=require('fs'),path=require('path');
const fixture=JSON.parse(fs.readFileSync(process.argv[2])); const group=process.argv[4]; const events=[];
global.cc={_RF:{push(){},pop(){}}};
const C={CONFIG:{CONFIG:0,ITEM:1,POSTS:2,MAGIC:3,UNIT_POSTS_SKILLS:4,UNIT:5,ARMS:6},MODEL_PROPERTY_INDEX:{MONEY:0,STAGE_N:1,AMBITION:2,BATTLE_N:3,R_FLAG:4,E_FLAG:5,MAX:8},GLOBAL_VAR:{ITEM_OFFSET:90,SIGLE_FIGHT:91,ZLDTMS:92},UNIT_ID_EX:{RYBD:0},ARM_ATTR_NAME2:{MAX:1},MAGIC_ATTR_NAME2:{MAX:1},POSTS_ATTR_NAME2:{MAX:1},ITEM_ATTR_NAME2:{MAX:1},TIANFU_MAX_COUNT:0};
const manager={getExData(){return []},dispatchEvent(...x){events.push(x)},m_deviceId:'device',getUpdateInt(){return 1}};
const store={load(...x){events.push(['store.load',...x])}};
const req=n=>({Manager:{getManager:()=>manager,default:{getInstance:()=>manager}},UserDefault:{default:{getInstance:()=>({getIntegerForKey:()=>8191})}},Config:C,Instance:{default:{range:(v,a,b)=>Math.max(a,Math.min(b,v))}},ItemStore:{default:{instance:()=>store}},Tool:{default:{len:o=>Object.keys(o).length,seed:7}},Unit:{default:function(){}},UUIDManager:{UUIDManager:function(){}}}[n]);
const out={};require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/core/Model.js'))(req,{},out);const M=out.default;
function model(){const x=Object.create(M.prototype);x._propertyProxy=[];x.varsProxy={};x.gvarsProxy={};x.pvarsProxy={};x._armAttrProxy=[];x._magicAttrProxy=[];x._postsAttrProxy=[];x._itemAttrProxy=[];x._unitAttrProxy=[];x._exMagicProxy=[];x._unit_tianfuProxy=[];x._unit_tianfu_tzProxy=[];x._unit_tianfu_zsProxy=[];x._units=[];return x}
function run(){events.length=0;const x=model();if(group==='state'){x.setEventName('ev');x.setStageName('map');x.setGVars(7,9);x.setMoney(10000000);x.addMoney(-3);x.setStage(10);x.incStage();x.setBattleN(4);x.incBattleN();return {event:x.eventName(),stageName:x.stageName(),money:x.money(),stageRaw:x.stage(0),stage:x.stage(),battle:x.battleN(),gvar:x.getGVars(7),missing:x.getGVars(8,77),events}}
if(group==='persistence'){x.loadGame({eventName:'e',stageName:'s',property2:{'0':42,'1':10},vars:{a:true,b:0,c:'x'},exVars:{q:7},pvars:{p:3},itemStore2:[1,2],unitAttr3:{}});return {event:x.eventName(),stage:x.stageName(),property:[x.property(0),x.property(1)],vars:x.varsProxy,gvars:x.gvarsProxy,pvars:x.pvarsProxy,events}}
x.varsProxy={0:'a',1:'b',256:'c'};x.averageLv=()=>events.push(['average']);x.initLocalVar(1);M._instance=x;M.destoryInstance();return {vars:x.varsProxy,destroyed:M._instance===undefined,events}}
fs.mkdirSync(path.dirname(process.argv[3]),{recursive:true});fs.writeFileSync(process.argv[3],JSON.stringify(Object.fromEntries(fixture.cases.map(c=>[c.name,run()]))));
