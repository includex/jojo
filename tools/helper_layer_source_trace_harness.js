#!/usr/bin/env node
/* Executes the recovered HelperLayer handlers with a deliberately minimal Cocos surface. */
const fs=require('node:fs'),path=require('node:path'),assert=require('node:assert/strict');
const fixture=JSON.parse(fs.readFileSync(process.argv[2],'utf8')),output=process.argv[3]; let current;
function Node(name='node'){this.name=name;this.active=true}
function RichText(){this.node=new Node('richtext');this.string=''}
function UILayer(){this.node=new Node('root');this.nodes={};this.comps={};this.backgrounds=[];this.removed=false;this.removedCount=0}
UILayer.prototype._setBg=function(bg){this.backgrounds.push(bg)};UILayer.prototype.removeFromParent=function(){this.removed=true;this.removedCount++};UILayer.prototype.addTouchEventListener=function(node,fn,priority){node.listener=fn;node.priority=priority};
UILayer.prototype.seekNodeByName=function(name){return this.nodes[name]||(this.nodes[name]=new Node(name))};UILayer.prototype.seekCompByName=function(K,name){return this.comps[name]||(this.comps[name]=new K())};
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}},Component:UILayer,RichText};
const exported={};require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/HelperLayer.js'))(name=>{if(name==='UILayer')return{default:UILayer};if(name==='Model')return{default:{instance:()=>current.model}};return{default:{}}},{},exported);assert.ok(exported.default,'recovered HelperLayer export'); const Source=exported.default;
function replace(text, table){for(const [from,to] of Object.entries(table))text=text.split(from).join(to);return text}
function run(spec){const replaceCalls=[];current={model:{getInfo:()=>spec.info,replaceSpeInfo:(text,flags)=>{replaceCalls.push([text,flags]);return replace(text,spec.replacement)}}};const layer=new Source();layer.onCreate();const button=layer.seekNodeByName('Logo_12-1/button0');const rich=layer.seekCompByName(RichText,'Logo_12-1/scrollview/view/content/richtext');function snap(step){return {step,backgrounds:[...layer.backgrounds],richText:rich.string,replaceCalls:replaceCalls.map(x=>[...x]),attached:!layer.removed,button:{path:'Logo_12-1/button0',priority:button.priority},tabs:[],routes:Array(layer.removedCount).fill('removeFromParent')}}const trace=[snap('create')];for(const event of spec.events){const [kind,value]=event.split(':');if(kind==='button')button.listener(button,+value);trace.push(snap(event))}return trace}
const traces=Object.fromEntries(fixture.cases.map(c=>[c.name,run(c)]));fs.mkdirSync(path.dirname(output),{recursive:true});fs.writeFileSync(output,JSON.stringify(traces));console.log(JSON.stringify(traces));
