package com.jojo.game

/**
 * Desktop flows for recovered platform factories. Android/JSB operations are
 * represented by [NativeBoundary] calls; they are deliberately not simulated.
 */
interface NativeBoundary { fun call(name: String) }

class PrivacyConsentFlow(private val native: NativeBoundary, private val accepted: () -> Unit, private val end: () -> Unit) {
    var attached = true
    fun touch(button: Int, event: Int) { if (event != 2) return; when (button) { 0 -> { native.call("write:/w//Privacy.txt:Hello World!"); accepted(); attached=false }; 1 -> end() } }
}
class LegalStatementFlow(private val emit: (String) -> Unit, private val end: () -> Unit) {
    var attached=true; var statement=0; var time=8
    var countdownInterval=1; var countdownRepeat=0; var countdownDelay=0; var unlockDelay=0
    fun onCreate(playerTimer: Int) {
        // JS uses Math.floor twice; Kotlin's / truncates toward zero, so use
        // floorDiv to preserve the source behavior for every integer input.
        val minutes = Math.floorDiv(playerTimer, 60_000)
        time=maxOf(3, time-Math.floorDiv(minutes, 30))
        // Mirrors StatementLayer's schedule(handle, 1, time, 0) and
        // scheduleOnce(unlock, time + 1) registration. Callback timing stays
        // behind the engine scheduler boundary; registration is deterministic.
        countdownRepeat=time
        unlockDelay=time+1
    }
    fun touch(button:Int,event:Int) { if(event!=2)return; if(button==0){statement=1;emit("ENTER_GAME")}else end();attached=false }
}
class VersionInfoFlow(private val version: String) { var attached=true; val lines=(if(version.isEmpty()) "버그 수정</br>코드 최적화" else version).split("</br>"); fun touch(event:Int){if(event==2)attached=false} }
class InstallationFlow(private val desktop: Boolean, private val launch: String, mineFloor: Int) { val buttons=MutableList(5){true}; init { if(mineFloor<1)buttons[4]=false; if(launch.isEmpty())buttons[2]=false }; var attached=true; fun touch(button:Int,event:Int){if(event==2&&button==0)attached=false} }
class UpdateFlow(private val emit:(String)->Unit) { var attached=true; private var flags=0; fun setButtonFlag(mask:Int,on:Boolean):List<Boolean>{flags=if(on) flags or mask else flags and mask.inv();return (0..3).map{flags and (1 shl it)!=0}}; fun parseIni(text:String)=text.lineSequence().mapNotNull{line->line.indexOf('=').takeIf{it>=0}?.let{i->line.substring(0,i).trim() to line.substring(i+1).trim()}}.toMap(); fun olderThanDay(a:Long,b:Long)=kotlin.math.abs(a-b)>86400000; fun over(){emit("UPDATE_SUCC");attached=false} }
class LoginEligibility(private val appId:Int,private val money:Int,private val mineFloor:Int){ private val floors=mapOf(8026 to 0,8031 to 0,8023 to 0,8011 to 0,8030 to 0,8021 to 1,8013 to 1,8014 to 1,8024 to 1,8001 to 1,8012 to 2,8003 to 2,8007 to 2,8027 to 2,8008 to 2,8032 to 3,8034 to 3,8033 to 3,8028 to 3,8029 to 3,8009 to 4,8016 to 4,8020 to 4); val floor=floors[appId]?:0; var toast:String?=null; fun checkFloor():Int=if(money<20&&mineFloor<floor){toast="권한이 부족합니다!";1}else 0 }
class DeviceIdentityService(private val saved:String?, private val macs:List<String>, private val macError:Int) {
    fun desktopDeviceId():String? { if(saved!=null)return saved; if(macError!=0)return null; val joined=macs.filter{!it.startsWith("0-50-56-")}.distinct().joinToString(""); return java.security.MessageDigest.getInstance("MD5").digest(joined.toByteArray()).joinToString(""){"%02x".format(it)} }
}
class TapTapSession { fun haveLogin()=true }
class VideoRewardFlow(private val loadError:Boolean, private val done:()->Unit) { var attached=true; var clip:String?=null; var plays=0;var toast:String?=null; fun onCreate(){if(loadError){toast="죄송합니다. 비디오 로드에 실패했습니다!";done();attached=false}else{clip="logo-clip";plays++}};fun onEvent(event:Int){if(event==3||event==5){done();attached=false}} }
