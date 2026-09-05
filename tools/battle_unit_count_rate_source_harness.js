#!/usr/bin/env node
/*
 * Executes BattleUnit.js from the recovered original bundle, rather than a
 * reimplementation.  The full Cocos scene is intentionally not created:
 * countRate needs only the class prototype, so the minimal Cocos/dependency
 * surface below lets the original factory define that prototype unchanged.
 */
const assert = require("node:assert/strict");
const path = require("node:path");

global.cc = {
  Component: class Component {},
  _RF: { push() {}, pop() {} },
  _decorator: { ccclass(target) { return target; } },
};

const recovered = require(path.resolve(
  __dirname,
  "../../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/BattleUnit.js",
));
const exportsObject = {};
recovered(
  (name) => {
    if (name === "Model") return { default: { instance: () => ({ cfgUnitAttrName: () => "ability" }) } };
    if (name === "Config") return {
      SKILL_TYPE: { JQJB: 111, PKDX: 165, ZMYJGJ: 270 },
      UNIT_ATTR_NAME2: { ATT: 0, DEF: 1, SPR: 2, CRI: 3, MOR: 4 },
    };
    if (name === "Instance") return { default: { range: (value, min, max) => Math.max(min, Math.min(max, value)) } };
    return { default: {} };
  },
  {},
  exportsObject,
);
const BattleUnit = exportsObject.default;
assert.ok(BattleUnit, "recovered BattleUnit factory did not export its original class");

function sourceUnit(rates, skills = {}) {
  const unit = Object.create(BattleUnit.prototype);
  unit.skill = (id) => skills[id] ?? 255;
  unit.rate = (index) => rates[index] ?? 0;
  unit.setRate = (index, value) => { rates[index] = value; };
  return unit;
}

// Source BattleUnit.countRate has no random dependency.  These vectors are
// shared with BattleAttackSequenceTest's Kotlin countRate cases.
{
  const attackerRates = { 0: 0 };
  const defenderRates = { 1: 0 };
  const attacker = sourceUnit(attackerRates);
  const defender = sourceUnit(defenderRates);
  assert.equal(attacker.countRate(defender, 0, 1, 25), false);
  assert.deepEqual([attackerRates[0], defenderRates[1]], [25, 0]);
  assert.equal(attacker.countRate(defender, 0, 1, 25), false);
  assert.equal(attacker.countRate(defender, 0, 1, 25), false);
  assert.equal(attacker.countRate(defender, 0, 1, 25), true);
  assert.deepEqual([attackerRates[0], defenderRates[1]], [0, 75]);
}

{
  const attackerRates = { 0: 0 };
  const defenderRates = { 1: 0 };
  const attacker = sourceUnit(attackerRates, { 111: 0 }); // JQJB active doubles n
  const defender = sourceUnit(defenderRates);
  assert.equal(attacker.countRate(defender, 0, 1, 25), false);
  assert.deepEqual([attackerRates[0], defenderRates[1]], [50, 0]);
}

{
  const attacker = sourceUnit({}, { 270: 0 }); // ZMYJGJ
  const defender = sourceUnit({});
  assert.equal(attacker.count_crit_rate(defender), 100);
}

{
  const attacker = sourceUnit({});
  const defender = sourceUnit({});
  attacker.abilityFinal = () => 80;
  attacker._pkdx = () => 50;
  assert.equal(attacker.count_crit_rate(defender), 12);
}

{
  const attacker = sourceUnit({});
  const defender = sourceUnit({});
  attacker.abilityFinal = (attr) => ({ 4: 30, 2: 40 })[attr];
  defender.abilityFinal = (attr) => ({ 4: 20, 2: 30 })[attr];
  // (40 + 30) versus (30 + 20): trunc(90 + 10 * 20 / 50) = 94.
  assert.equal(attacker.countMagicHitRate(defender), 94);
}

{
  const attacker = sourceUnit({});
  const defender = sourceUnit({});
  attacker.abilityFinal = () => 80;
  attacker._pkdx = () => 50;
  assert.equal(attacker.count_hitRate(defender), 96);
}

{
  const attacker = sourceUnit({});
  const defender = sourceUnit({});
  attacker.abilityFinal = () => 80;
  attacker._pkdx = () => 50;
  assert.equal(attacker.count_sjl(defender), 12);
}

// Execute the original _pkdx body itself.  Its range is ATT through MOR and
// it only activates on the attacking unit's PKDX skill (165).
{
  const attacker = sourceUnit({}, { 165: 0 });
  const defender = sourceUnit({});
  attacker._battleLayer = { notify5() {} };
  defender.abilityFinal = (attr) => [40, 20, 30, 100, 50][attr];
  defender.unit = () => ({ name: () => "defender" });
  assert.equal(attacker._pkdx(attacker, defender, 3), 20);
}

process.stdout.write("SOURCE_COUNT_RATE_HARNESS_OK\n");
