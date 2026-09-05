#!/usr/bin/env node
/* Recovered core factories under in-memory localStorage/file/event boundaries. */
const fs=require('fs'),path=require('path'),fixture=JSON.parse(fs.readFileSync(process.argv[2],'utf8'));
const local={},files={},logs=[]; function Component(){};
global.cc={_RF:{push(){},pop(){}},_decorator:{ccclass:x=>x,property:()=>()=>{}},Component,Node:class{},sys:{localStorage:{getItem:k=>Object.hasOwn(local,k)?local[k]:null,setItem:(k,v)=>{local[k]=v;logs.push('local:'+k)},removeItem:k=>{delete local[k];logs.push('remove:'+k)}}}};
global.jsb={fileUtils:{getWritablePath:()=>'/w',isFileExist:p=>Object.hasOwn(files,p),getStringFromFile:p=>files[p],writeStringToFile:(v,p)=>{files[p]=v;logs.push('file:'+p)}}};
function factory(file,deps={}){const e={};require(path.resolve(__dirname,'../../jojo_mobile/sgccz-desktop/recovered-js/modules/core/'+file+'.js'))(n=>deps[n]||{}, {}, e);return e}
const Tool=factory('Tool').default,MD5=factory('MD5').default,UUID=factory('UUIDManager').UUIDManager;
const User=factory('UserDefault',{Tool:{default:Tool},MD5:{default:MD5}}).default;
const Status=factory('StatusManager').default;
let randomSeq=0;const Event=factory('JSEvent',{Manager:{getManager:()=>({dispatchEvent:(x)=>logs.push('manager:'+x)})},Tool:{default:{random:()=>randomSeq++}}}).default;
const q=x=>x==null?'null':JSON.stringify(x);const base=(kind,more)=>({kind,logs:[...logs],...more});
function run(c){Object.keys(local).forEach(k=>delete local[k]);Object.keys(files).forEach(k=>delete files[k]);logs.length=0;randomSeq=0;User.m_instance=null;
 if(c.kind==='uuid'){let x=new UUID(),compressed=x.compressUuid(c.uuid);return [base(c.kind,{compressed,decoded:x.decodeUuid(compressed)})]}
 if(c.kind==='tool'){let bytes=Tool.stringToUint8Array(c.text),encrypted=Tool.xorEncryptDecrypt(bytes,'ccz65Sha08GeZ1Fu',0);return [base(c.kind,{bytes:[...bytes],round:Tool.uint8ArrayToString(Tool.xorEncryptDecrypt(encrypted,'ccz65Sha08GeZ1Fu',1))})]}
 if(c.kind==='md5'){let x=new MD5();return [base(c.kind,{hex:x.hex_md5(c.text),b64:x.b64_md5(c.text),hmac:x.hex_hmac_md5(c.key,c.text),test:x.md5_vm_test()})]}
 if(c.kind==='user'){let x=new User();x._userDefault={save1:c.legacySave,x:c.legacyX};x._flush();let first=x.getStringForKey('save1','d');let second=x.getStringForKey('x','d');return [base(c.kind,{first,second,localKeys:Object.keys(local).sort(),user:x._userDefault})]}
 if(c.kind==='userGlobal'){let x=new User();x._gUserDefault={g:c.globalG};x._flush(1);let got=x.getStringForKey('g','d',2);x.deleteValueForKey('g',1);return [base(c.kind,{got,global:x._gUserDefault,file:!!files['/w/UserData.json']})]}
 if(c.kind==='status'){let x=new Status(),a=[];const s=i=>({onStatusEnter:()=>a.push('enter'+i),onStatusExit:()=>a.push('exit'+i),onStatusUpdate:()=>a.push('update'+i)});x.regStatus(0,s(0));x.regStatus(1,s(1));x.changeStatus(0);x.regCondition(0,1,()=>{a.push('cond');return true});x.update();x.clear();return [base(c.kind,{order:a,status:x._status})]}
 let x=new Event(),a=[],target={};let id=x.addEventListener('A',target,()=>{a.push('one');x.removeEventListener(id)});x.addEventListener('A',target,()=>a.push('once'),true);x.dispatchEvent('A',0);x.dispatchEvent('A',0);x.dispatchEvent('A',0,false);x.update();return [base(c.kind,{order:a,events:Object.keys(x.m_events).sort(),queued:x.m_cmds.length})]
}
const out=Object.fromEntries(fixture.cases.map(c=>[c.id,run(c)]));fs.mkdirSync(path.dirname(process.argv[3]),{recursive:true});fs.writeFileSync(process.argv[3],JSON.stringify(out));
