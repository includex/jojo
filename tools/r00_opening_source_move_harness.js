#!/usr/bin/env node
/* Execute recovered HallLayer.AStar and HallUnit._move2 for R_00's opening moves. */
const crypto = require("crypto");
const fs = require("fs");
const path = require("path");

const root = process.env.JOJO_SOURCE_ROOT
  ? path.resolve(process.env.JOJO_SOURCE_ROOT)
  : path.resolve(__dirname, "../../jojo_mobile/sgccz-desktop");
const r00Path = path.join(root, "decompiled-python/R_00.py");
const hallLayerPath = path.join(root, "recovered-js/modules/ui/HallLayer.js");
const hallUnitPath = path.join(root, "recovered-js/modules/game-data/HallUnit.js");
const stageLayerPath = path.join(root, "recovered-js/modules/ui/StageLayer.js");
const configPath = path.join(root, "assets/Game/config.54cec.json");
const mapPath = path.join(root, "assets/Game/import/04/0406a5c2-0caa-4dfe-baf7-5f9db3216860.f7359.json");
const output = process.argv[2];
if (!output) throw new Error("usage: r00_opening_source_move_harness.js OUTPUT.json");

const r00 = fs.readFileSync(r00Path, "utf8");
const expectedPrefix = [
  "def scene1():",
  "model.initLocalVar()",
  "stage.clsUnit()",
  "stage.setMenuVisible(False)",
  "model.setAmbition(50)",
  "stage.loadBg(2, 30)",
  "stage.setEventName('재능의 첫 징후')",
  "stage.draw()",
  "stage.effectSound(201, 0)",
  "stage.showUnit(181, 40, 5, 2)",
  "stage.unit(181).move(40, 15, 2)",
  "stage.showUnits([[0, 40, 5, 2], [157, 54, 95]])",
  "stage.unitsMove([[stage.unit(181), 40, 25, 2], [stage.unit(0), 40, 15, 2], [stage.unit(157), 54, 85, 0]])",
  "stage.showUnit(182, 40, 5, 2)",
  "stage.unitsMove([[stage.unit(181), 40, 45, 2], [stage.unit(0), 40, 35, 2], [stage.unit(182), 40, 25, 2], [stage.unit(157), 54, 65, 0]])",
  "stage.delay(3)",
  "stage.unit(181).setAction(0, 0)",
  "stage.say('",
];
const scene1Offset = r00.indexOf("def scene1():");
if (scene1Offset < 0) throw new Error("R_00 scene1 missing");
const openingLines = r00.slice(scene1Offset).split("\n").map(line => line.trim());
const actualPrefix = openingLines.slice(0, expectedPrefix.length);
if (actualPrefix.slice(0, -1).some((line, index) => line !== expectedPrefix[index]) ||
    !actualPrefix.at(-1).startsWith(expectedPrefix.at(-1))) {
  throw new Error(`R_00 opening prefix changed: ${JSON.stringify(actualPrefix)}`);
}

const config = JSON.parse(fs.readFileSync(configPath, "utf8"));
const mapEntry = Object.entries(config.paths).find(([, value]) => value[0] === "data/Pmap/Pmap_30");
if (!mapEntry || config.uuids[Number(mapEntry[0])] !== "04BqXCDKpN/rr3X52zIWhg") {
  throw new Error("Pmap_30 asset mapping changed");
}
const importVersions = config.versions && config.versions.import;
let mapImportVersion = null;
if (Array.isArray(importVersions)) {
  for (let index = 0; index < importVersions.length; index += 2) {
    if (importVersions[index] === Number(mapEntry[0])) mapImportVersion = importVersions[index + 1];
  }
}
if (mapImportVersion !== "f7359" || !mapPath.endsWith(`.${mapImportVersion}.json`)) {
  throw new Error(`Pmap_30 import version changed: ${mapImportVersion}`);
}
function matrix(value) {
  if (Array.isArray(value) && value.length === 100 && value.every(row => Array.isArray(row) && row.length === 100)) return value;
  if (Array.isArray(value)) for (const child of value) { const found = matrix(child); if (found) return found; }
  if (value && typeof value === "object") for (const child of Object.values(value)) { const found = matrix(child); if (found) return found; }
  return null;
}
const map = matrix(JSON.parse(fs.readFileSync(mapPath, "utf8")));
if (!map) throw new Error("Pmap_30 collision matrix missing");

function Base() {}
const fallback = new Proxy({default: Base}, {get: (target, key) => key in target ? target[key] : {}});
global.cc = {
  _RF: {push() {}, pop() {}}, _decorator: {ccclass: value => value, property: () => {}},
  Component: Base, Sprite: function(){}, SpriteFrame: function(){}, JsonAsset: function(){}, Texture2D: function(){},
  v2: (x, y) => ({x, y}),
  moveTo: (duration, x, y) => ({kind: "move", duration, x, y}),
  callFunc: fn => ({kind: "call", fn}), sequence: entries => entries,
  WrapMode: {Loop: "loop", Normal: "normal"}, macro: {REPEAT_FOREVER: -1},
};
function recovered(file, dependencies) {
  const exports = {};
  require(file)(name => dependencies[name] || fallback, {}, exports);
  return exports.default;
}
const HallLayer = recovered(hallLayerPath, {Config: {DIR: {UP: 0, RIGHT: 1, DOWN: 2, LEFT: 3}}});
const HallUnit = recovered(hallUnitPath, {
  HallCfg: {RMAPOBJ_ACTION: {DEF: -1, PU_TONG: 0, MOVE_0: 20, MOVE: 20}},
  Config: {default: {DIR: {UP: 0, RIGHT: 1, DOWN: 2, LEFT: 3}}},
});
const hallUnitSource = fs.readFileSync(hallUnitPath, "utf8");
if (!hallUnitSource.includes("i.setAction(T.RMAPOBJ_ACTION.PU_TONG, r);")) {
  throw new Error("HallUnit._move1 final scripted direction callback changed");
}
const layer = Object.create(HallLayer.prototype);
layer._mapBlockInfo = map;
layer.turnPos = (x, y) => ({x, y});
const positions = new Map([[181, [40, 5]]]);
function occupancy() {
  layer._units = Object.fromEntries([...positions].map(([id, point]) => [id, {x: () => point[0], y: () => point[1]}]));
}
function actor(id, destination, scriptDirection) {
  occupancy();
  const origin = positions.get(id);
  if (!origin) throw new Error(`actor ${id} is not visible`);
  const pathPoints = layer.AStar({x: () => origin[0], y: () => origin[1]}, destination[0], destination[1]);
  if (!pathPoints || pathPoints.at(-1).x !== destination[0] || pathPoints.at(-1).y !== destination[1]) throw new Error(`AStar failed for actor ${id}`);
  const unit = Object.create(HallUnit.prototype);
  unit._x = origin[0]; unit._y = origin[1]; unit._hallLayer = layer;
  unit._anime = {schedule() {}, unschedule() {}, play() { return {}; }};
  let currentDirection = -1;
  unit.setAction = (_action, direction) => { currentDirection = direction; };
  let emitted;
  unit.node = {position: {y: 0}, runAction(sequence) { emitted = sequence; }};
  unit._move2(pathPoints, () => {});
  let start = 0; let from = [...origin];
  const segments = [];
  for (const item of emitted) {
    if (item.kind === "call") { item.fn(); continue; }
    const to = [item.x, item.y];
    segments.push({start: Number(start.toFixed(6)), duration: item.duration, from, to, direction: currentDirection});
    start += item.duration; from = to;
  }
  return {id, origin: [...origin], destination, path: pathPoints.map(point => [point.x, point.y]), movementFinalDirection: currentDirection, finalDirection: scriptDirection, duration: Number(start.toFixed(6)), segments};
}
function group(requests) {
  const actors = requests.map(([id, x, y, direction]) => actor(id, [x, y], direction));
  for (const item of actors) positions.set(item.id, item.destination);
  return {actors, duration: Math.max(...actors.map(item => item.duration))};
}
const groups = [
  group([[181, 40, 15, 2]]),
  (() => { positions.set(0, [40, 5]); positions.set(157, [54, 95]); return group([[181, 40, 25, 2], [0, 40, 15, 2], [157, 54, 85, 0]]); })(),
  (() => { positions.set(182, [40, 5]); return group([[181, 40, 45, 2], [0, 40, 35, 2], [182, 40, 25, 2], [157, 54, 65, 0]]); })(),
];
const stageLayer = fs.readFileSync(stageLayerPath, "utf8");
if (!stageLayer.includes("this.scheduleOnce(this.resume.bind(this), .1 * t);")) throw new Error("StageLayer.delay timing changed");
const sha256 = file => crypto.createHash("sha256").update(fs.readFileSync(file)).digest("hex");
const report = {format: "jojo-r00-opening-source-moves/v1", evidenceKind: "recovered-source-runtime", source: {
  r00: {path: r00Path, sha256: sha256(r00Path)}, hallLayer: {path: hallLayerPath, sha256: sha256(hallLayerPath)},
  hallUnit: {path: hallUnitPath, sha256: sha256(hallUnitPath)}, map: {path: mapPath, sha256: sha256(mapPath)},
  stageLayer: {path: stageLayerPath, sha256: sha256(stageLayerPath)},
  assetConfig: {path: configPath, sha256: sha256(configPath), pmap30PathIndex: Number(mapEntry[0]), pmap30ImportVersion: mapImportVersion},
}, groups, delaySeconds: Number((.1 * 3).toFixed(6))};
fs.mkdirSync(path.dirname(path.resolve(output)), {recursive: true});
fs.writeFileSync(output, JSON.stringify(report, null, 2) + "\n");
console.log(`R00_OPENING_SOURCE_MOVES_OK groups=${groups.length} actors=${groups.reduce((n, group) => n + group.actors.length, 0)}`);
