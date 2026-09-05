#!/usr/bin/env node
/* Executes the recovered InfoLayer factory.  The mock only supplies its Cocos boundary. */
const fs=require('fs'),path=require('path');
const fixtures=JSON.parse(fs.readFileSync(process.argv[2]));
function Layer(){
  this.nodes={}; this.listeners={}; this.m_ud={getIntegerForKey:(_k,d)=>this.setting==null?d:this.setting};
}
Layer.prototype.seekCompByName=function(_type,name){return this.rich||(this.rich={name,string:''})};
Layer.prototype.seekNodeByName=function(name){return this.nodes[name]||(this.nodes[name]={name})};
Layer.prototype.addTouchEventListener=function(node,fn){node.touch=fn};
Layer.prototype.addEventListener=function(name,fn){this.listeners[name]=fn};
Layer.prototype.schedule=function(fn){this.revealFn=fn};
Layer.prototype.scheduleOnce=function(fn,delay){this.closeFn=fn;this.closeDelay=delay};
Layer.prototype.unschedule=function(fn){if(this.revealFn===fn)this.revealFn=null;if(this.closeFn===fn)this.closeFn=null};
Layer.prototype.removeFromParent=function(){this.removes=(this.removes||0)+1};
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}},RichText:function(){},macro:{REPEAT_FOREVER:-1}};
const out={};
require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/InfoLayer.js'))(
  n=>n==='UILayer'?{default:Layer}:n==='Config'?{GAME_SETTING:'setting',SETTING_FLAG:{AUTO_CLOSE:2}}:n==='Model'?{default:{instance:()=>({replaceSpeInfo:s=>s})}}:{},{},out);
const Info=out.default;
function snap(step,p,calls){return {step,rich:p.rich.string,next:p._nextString,content:p._content,auto:p._autoClose,delay:p._delay,revealing:!!p._handle2,closePending:!!p._handle,closeDelay:p.closeDelay||null,calls,removes:p.removes||0}}
function run(c){
  const p=new Info();p.setting=c.setting;let calls=0;
  p.onCreate({txt:c.txt, ...(Object.prototype.hasOwnProperty.call(c,'delay')?{delay:c.delay}:{}),func:()=>calls++});
  const trace=[snap('create',p,calls)];
  for(const event of c.events){
    if(event==='reveal'&&p.revealFn)p.revealFn();
    else if(event==='touch')p.nodes.Panel_cancel.touch(null,2);
    else if(event==='touch_begin')p.nodes.Panel_cancel.touch(null,1);
    else if(event==='skip')p.listeners.SKIP();
    else if(event==='auto'&&p.closeFn)p.closeFn();
    trace.push(snap(event,p,calls));
  }
  return trace;
}
for(const c of fixtures.cases)out[c.name]=run(c);
fs.mkdirSync(path.dirname(process.argv[3]),{recursive:true});fs.writeFileSync(process.argv[3],JSON.stringify(out));
