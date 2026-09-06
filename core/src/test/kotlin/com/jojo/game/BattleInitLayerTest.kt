// Test
package com.jojo.game

import com.jojo.game.presentation.battle.bootstrap.BattleInitLayer

import kotlin.test.*
/** BattleInitLayerTest: BattleInitLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleInitLayerTest {@Test fun `source init sound labels and destroy effects`() {var p=0;var s=0;val l=BattleInitLayer(object:BattleInitLayer.Effects{override fun playInitBattle(){p++};override fun stopAllEffects(){s++}});l.onCreate(1);assertEquals(1,p);assertEquals(listOf("영천 ▪ 훈련","영천 ▪ 훈련"),l.onLoadBgMap("영천").labels);l.onDestroy();assertEquals(1,s)}}
