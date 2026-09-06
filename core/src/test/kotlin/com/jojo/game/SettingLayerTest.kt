// Test
package com.jojo.game

import com.jojo.game.application.campaign.DailySignInFlow
import com.jojo.game.application.campaign.RaffleFlow
import com.jojo.game.presentation.shared.overlay.*

import kotlin.test.*
/** SettingLayerTest: SettingLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class SettingLayerTest {
 private class Store:SettingLayer.Store { val m=mutableMapOf<String,Int>(); override fun getInt(k:String,d:Int)=m[k]?:d; override fun putInt(k:String,v:Int){m[k]=v} }
 @Test fun `source defaults flags and persists toggle plus sound immediately`() { val s=Store(); var music:Boolean?=null; val l=SettingLayer(s,object:SettingLayer.Sound{override fun music(on:Boolean){music=on};override fun effect(on:Boolean)=Unit}); assertEquals(7,l.onCreate().flags); l.check(0,false); assertEquals(6,s.m[SettingLayer.GAME_SETTING]); assertEquals(false,music) }
 @Test fun `speed commits only on destroy while radio and background commit immediately`() { val s=Store(); val l=SettingLayer(s);l.onCreate();l.check2(0,2);l.check2(2,1);l.selectBackground(3);l.onSlider(.678f);assertNull(s.m[SettingLayer.GAME_SPEED]);assertTrue(l.close(2));assertEquals(67,s.m[SettingLayer.GAME_SPEED]);assertEquals(2,s.m[SettingLayer.MSG_SPEED]);assertEquals(1,s.m[SettingLayer.NOTIFY_LV]);assertEquals(3,s.m[SettingLayer.BG_INDEX]) }

 @Test fun `optional source buttons keep support and scene gates on normal setting state machine`() {
  val achievements=mapOf(1 to StageAchievement(round=1,level=2,gold=30,stars=5))
/** layer: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

  fun layer(scene:String,code:Int)=SettingLayer(Store(),featureEnvironment={SettingLayer.FeatureEnvironment(sceneName=scene,supportAdCode=code,achievements=achievements,nowSeconds=86400)})
  val gated=layer("Hall",0);gated.onCreate();assertEquals(SettingLayer.FeatureResult.Gated,gated.featureButton(8,2));assertNull(gated.activeFeature)
  val login=layer("Login",8);login.onCreate();assertIs<SettingLayer.FeatureResult.Toast>(login.featureButton(8,2));assertNull(login.activeFeature)
  val hall=layer("Hall",8);hall.onCreate();assertEquals(SettingLayer.FeatureResult.Opened("AchievementsLayer"),hall.featureButton(7,2));assertIs<AchievementsFlow>(hall.activeFeature);assertEquals(SettingLayer.FeatureResult.Opened("RaffleLayer"),hall.featureButton(8,2));assertIs<RaffleFlow>(hall.activeFeature);assertEquals(SettingLayer.FeatureResult.Opened("SignInLayer"),hall.featureButton(9,2));assertIs<DailySignInFlow>(hall.activeFeature)
 }
}
