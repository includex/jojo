#!/usr/bin/env node
const fs = require('fs');
const path = require('path');
const fixture = JSON.parse(fs.readFileSync(process.argv[2]));
global.cc = {_RF:{push(){},pop(){}}, _decorator:{ccclass:x=>x,property:()=>()=>{}}, Node:function(){}};
const exported = {};
require(path.resolve(__dirname, '../../jojo_mobile/sgccz-desktop/recovered-js/modules/core/Tool.js'))(() => ({}), {}, exported);
const Tool = exported.default;
const result = {};
for (const entry of fixture.cases) {
  Tool.seed = entry.seed;
  const values = [];
  for (let index = 0; index < entry.draws; index++) values.push(Tool.random(0, 100));
  result[entry.id] = {values};
}
fs.mkdirSync(path.dirname(process.argv[3]), {recursive:true});
fs.writeFileSync(process.argv[3], JSON.stringify(result));
