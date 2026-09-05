package com.jojo.game
import kotlin.test.*
/**
 * class  `SettingLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SettingLayerTest {
 private class Store:SettingLayer.Store { val m=mutableMapOf<String,Int>(); override fun getInt(k:String,d:Int)=m[k]?:d; override fun putInt(k:String,v:Int){m[k]=v} }
 @Test fun `source defaults flags and persists toggle plus sound immediately`() { val s=Store(); var music:Boolean?=null; val l=SettingLayer(s,object:SettingLayer.Sound{override fun music(on:Boolean){music=on};override fun effect(on:Boolean)=Unit}); assertEquals(7,l.onCreate().flags); l.check(0,false); assertEquals(6,s.m[SettingLayer.GAME_SETTING]); assertEquals(false,music) }
 @Test fun `speed commits only on destroy while radio and background commit immediately`() { val s=Store(); val l=SettingLayer(s);l.onCreate();l.check2(0,2);l.check2(2,1);l.selectBackground(3);l.onSlider(.678f);assertNull(s.m[SettingLayer.GAME_SPEED]);assertTrue(l.close(2));assertEquals(67,s.m[SettingLayer.GAME_SPEED]);assertEquals(2,s.m[SettingLayer.MSG_SPEED]);assertEquals(1,s.m[SettingLayer.NOTIFY_LV]);assertEquals(3,s.m[SettingLayer.BG_INDEX]) }

 @Test fun `optional source buttons keep support and scene gates on normal setting state machine`() {
  val achievements=mapOf(1 to StageAchievement(round=1,level=2,gold=30,stars=5))
/**
 * 공개 메서드 `layer`
 *
 * ### 파라미터
- `scene` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
- `code` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

  fun layer(scene:String,code:Int)=SettingLayer(Store(),featureEnvironment={SettingLayer.FeatureEnvironment(sceneName=scene,supportAdCode=code,achievements=achievements,nowSeconds=86400)})
  val gated=layer("Hall",0);gated.onCreate();assertEquals(SettingLayer.FeatureResult.Gated,gated.featureButton(8,2));assertNull(gated.activeFeature)
  val login=layer("Login",8);login.onCreate();assertIs<SettingLayer.FeatureResult.Toast>(login.featureButton(8,2));assertNull(login.activeFeature)
  val hall=layer("Hall",8);hall.onCreate();assertEquals(SettingLayer.FeatureResult.Opened("AchievementsLayer"),hall.featureButton(7,2));assertIs<AchievementsFlow>(hall.activeFeature);assertEquals(SettingLayer.FeatureResult.Opened("RaffleLayer"),hall.featureButton(8,2));assertIs<RaffleFlow>(hall.activeFeature);assertEquals(SettingLayer.FeatureResult.Opened("SignInLayer"),hall.featureButton(9,2));assertIs<DailySignInFlow>(hall.activeFeature)
 }
}
