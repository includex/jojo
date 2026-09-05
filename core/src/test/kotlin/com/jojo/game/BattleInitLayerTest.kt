package com.jojo.game
import kotlin.test.*
class BattleInitLayerTest {@Test fun `source init sound labels and destroy effects`() {var p=0;var s=0;val l=BattleInitLayer(object:BattleInitLayer.Effects{override fun playInitBattle(){p++};override fun stopAllEffects(){s++}});l.onCreate(1);assertEquals(1,p);assertEquals(listOf("영천 ▪ 훈련","영천 ▪ 훈련"),l.onLoadBgMap("영천").labels);l.onDestroy();assertEquals(1,s)}}
