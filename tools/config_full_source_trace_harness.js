#!/usr/bin/env node
const fs = require('fs'), path = require('path');
const fixture = JSON.parse(fs.readFileSync(process.argv[2]));
global.cc = {_RF:{push(){},pop(){}}};
const exported = {};
require(path.resolve(__dirname, '../../jojo_mobile/sgccz-desktop/recovered-js/modules/core/Config.js'))(() => ({}), {}, exported);
function canonical(value) {
  if (value === null) return null;
  if (Array.isArray(value)) return value.map(canonical);
  if (typeof value === 'function') return {type:'function', arity:value.length};
  if (typeof value !== 'object') return {type:typeof value, value};
  const out = {};
  for (const key of Object.keys(value).sort()) out[key] = canonical(value[key]);
  return out;
}
const dump = canonical(exported);
fs.mkdirSync(path.dirname(process.argv[3]), {recursive:true});
fs.writeFileSync(process.argv[3], JSON.stringify(Object.fromEntries(fixture.cases.map(({name}) => [name, dump]))));
