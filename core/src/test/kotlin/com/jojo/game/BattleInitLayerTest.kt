package com.jojo.game

import com.jojo.game.presentation.battle.BattleInitLayer

import kotlin.test.*
/**
 * class  `BattleInitLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleInitLayerTest {@Test fun `source init sound labels and destroy effects`() {var p=0;var s=0;val l=BattleInitLayer(object:BattleInitLayer.Effects{override fun playInitBattle(){p++};override fun stopAllEffects(){s++}});l.onCreate(1);assertEquals(1,p);assertEquals(listOf("영천 ▪ 훈련","영천 ▪ 훈련"),l.onLoadBgMap("영천").labels);l.onDestroy();assertEquals(1,s)}}
