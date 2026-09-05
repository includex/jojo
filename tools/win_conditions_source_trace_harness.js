#!/usr/bin/env node
/* Runs the recovered original WinConditionsLayer.js in a minimal Cocos shell. */
const fs = require("node:fs");
const path = require("node:path");
const fixturePath = process.argv[2];
const outputPath = process.argv[3];
if (!fixturePath || !outputPath) throw new Error("usage: source-harness fixture.json output.json");

// Recovered TypeScript uses `Base.apply(this, arguments)`, so its base must
// be callable (an ES2015 class cannot be used here).
function UILayer() { this.attached = true; this.listeners = new Map(); this.rich = new Map(); }
UILayer.prototype.seekCompByName = function (_type, name) { if (!this.rich.has(name)) this.rich.set(name, { string: "" }); return this.rich.get(name); };
UILayer.prototype.seekNodeByName = function (name) { return { name }; };
UILayer.prototype.addTouchEventListener = function (node, fn) { this.listeners.set(node.name, fn); };
UILayer.prototype.removeFromParent = function () { this.attached = false; };
global.cc = {
  Component: class {}, RichText: class {},
  _RF: { push() {}, pop() {} },
  _decorator: { ccclass(target) { return target; }, property() {} },
};
const recovered = require(path.resolve(__dirname, "../../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/WinConditionsLayer.js"));
const out = {};
recovered((name) => name === "UILayer" ? { default: UILayer } : { default: {} }, {}, out);
const Original = out.default;
if (!Original) throw new Error("original WinConditionsLayer export missing");
const cases = JSON.parse(fs.readFileSync(fixturePath, "utf8"));
const trace = cases.map((item) => {
  const layer = new Original(); let callbacks = 0;
  layer.onCreate({ txt: item.text, round: item.round, fn: () => { callbacks++; } });
  const snapshot = (event) => ({ event, first: layer.rich.get("richtext1").string, second: layer.rich.get("richtext2").string, attached: layer.attached, callbacks });
  const steps = [snapshot(0)];
  for (const event of item.events) { layer.listeners.get("Panel_cancel")(null, event); steps.push(snapshot(event)); }
  return { id: item.id, steps };
});
fs.writeFileSync(outputPath, JSON.stringify(trace, null, 2) + "\n");
