package com.jojo.game

import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * object  `FoundationCodec`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object FoundationCodec {
    /**
     * 공개 메서드 `bytes`
     *
     * ### 파라미터
    - `s` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `ByteArray`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun bytes(s: String): ByteArray {
        val out = ArrayList<Byte>(); for (ch in s) {
            val n = ch.code; if (n < 128) out += n.toByte() else if (n < 2048) {
                out += (192 or n.shr(6)).toByte(); out += (128 or n.and(63)).toByte()
            } else {
                out += (224 or n.shr(12)).toByte(); out += (128 or n.shr(6)
                    .and(63)).toByte(); out += (128 or n.and(63)).toByte()
            }
        }; return out.toByteArray()
    }

    /**
     * 공개 메서드 `text`
     *
     * ### 파라미터
    - `a` (`ByteArray`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun text(a: ByteArray): String {
        val b = StringBuilder()
        var p = 0; while (p < a.size) {
            var n = a[p++].toInt() and 255; if (n < 128) b.append(n.toChar()) else {
                val count = if (n and 224 == 192) {
                    n = (n and 31) shl 6; 1
                } else if (n and 240 == 224) {
                    n = (n and 15) shl 12; 2
                } else {
                    n = (n and 7) shl 18; 3
                }; repeat(count) { n = n or ((a[p++].toInt() and 63) shl (6 * (count - 1 - it))) }; b.append(n.toChar())
            }
        }; return b.toString()
    }

    /**
     * 공개 메서드 `xor`
     *
     * ### 파라미터
    - `a` (`ByteArray`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `key` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `decode` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `ByteArray`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun xor(a: ByteArray, key: String, decode: Boolean): ByteArray = ByteArray(a.size) { i ->
        val shift = key[i % key.length].code % 8
        val n =
            a[i].toInt() and 255; if (decode) ((n ushr shift) or (n shl (8 - shift) and 255)).toByte() else ((n shl shift and 255) or (n ushr (8 - shift))).toByte()
    }
}

/**
 * object  `Md5Service`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object Md5Service {
    fun hex(s: String) =
        MessageDigest.getInstance("MD5").digest(FoundationCodec.bytes(s)).joinToString("") { "%02x".format(it) }

    fun b64(s: String) = java.util.Base64.getEncoder().withoutPadding()
        .encodeToString(MessageDigest.getInstance("MD5").digest(FoundationCodec.bytes(s)))

    fun hmac(key: String, text: String) =
        Mac.getInstance("HmacMD5").apply { init(SecretKeySpec(FoundationCodec.bytes(key), "HmacMD5")) }
            .doFinal(FoundationCodec.bytes(text)).joinToString("") { "%02x".format(it) }
}

/**
 * class  `UuidCodec`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class UuidCodec {
    private val key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
    private val hex = "0123456789abcdef"
    fun compress(v: String): String {
        val main = v.substringBefore('@'); if (main.length != 36) return v
        val raw = main.replace("-", "")
        val o = StringBuilder().append(raw[0]).append(raw[1]); for (i in 2 until 32 step 3) {
            val a = hex.indexOf(raw[i])
            val b = hex.indexOf(raw[i + 1])
            val c = hex.indexOf(raw[i + 2]); o.append(key[(a shl 2) + (b shr 2)]).append(key[((b and 3) shl 4) + c])
        }; return v.replace(main, o.toString())
    }

    fun decode(v: String): String {
        val main = v.substringBefore('@'); if (main.length != 22) return v
        val chars = CharArray(36); chars[8] = '-'; chars[13] = '-'; chars[18] = '-'; chars[23] = '-'; chars[0] =
            main[0]; chars[1] = main[1]
        var p = 2
        var k = 2; while (p < 22) {
            val a = key.indexOf(main[p])
            val b = key.indexOf(main[p + 1]); while (chars[k] == '-') k++; chars[k++] =
                hex[a shr 2]; while (chars[k] == '-') k++; chars[k++] =
                hex[((a and 3) shl 2) or (b shr 4)]; while (chars[k] == '-') k++; chars[k++] = hex[b and 15]; p += 2
        }; return v.replace(main, String(chars))
    }
}

/**
 * class  `UserPreferencesStore`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class UserPreferencesStore(
    private val local: MutableMap<String, String>,
    private val file: MutableMap<String, String>,
    private val log: MutableList<String>
) {
    val user = linkedMapOf<String, String>()
    val global = linkedMapOf<String, String>()
    fun set(k: String, v: String, globalFlag: Boolean = false) {
        (if (globalFlag) global else user)[k] = v; flush(globalFlag)
    }

    fun get(k: String, d: String, globalFlag: Boolean = false) =
        if (globalFlag) global[k] ?: d else local[k] ?: user[k] ?: d

    fun flush(globalFlag: Boolean = false) {
        if (globalFlag) {
            file["/w/UserData.json"] = "cipher"; log += "file:/w/UserData.json"
        } else {
            local["UserDefault"] = "cipher"; log += "local:UserDefault"
        }
    }

    fun delete(k: String, globalFlag: Boolean) {
        if (globalFlag) {
            global.remove(k); flush(true)
        } else {
            user.remove(k); flush(false)
        }
    }
}

/**
 * class  `StatusMachine`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class StatusMachine {
    var status = -1
    val states = mutableMapOf<Int, Triple<() -> Unit, () -> Unit, () -> Unit>>()
    val conditions = mutableListOf<Triple<Int, Int, () -> Boolean>>()
    fun change(to: Int) {
        if (to == status) return; if (status != -1) states[status]!!.second(); status =
            to; if (status != -1) states[status]!!.first()
    }

    fun update() {
        conditions.forEach { if ((it.first == -1 || it.first == status) && it.third()) change(it.second) }; if (status != -1) states[status]!!.third()
    }

    fun clear() {
        change(-1); conditions.clear(); states.clear()
    }
}

/**
 * class  `EventDispatcher`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class EventDispatcher {
    data class L(val target: Any, val fn: () -> Unit, val id: String, val once: Boolean)

    val events = linkedMapOf<String, MutableList<L>>()
    val queue = mutableListOf<Triple<String, Boolean, Boolean>>()
    var n = 0
    fun add(type: String, target: Any, fn: () -> Unit, once: Boolean = false): String {
        val id = "${n++}-${n++}"; events.getOrPut(type) { mutableListOf() } += L(target, fn, id, once); return id
    }

    fun remove(id: String) {
        events.values.forEach { xs ->
            xs.indexOfFirst { it.id == id }.takeIf { it >= 0 }?.let { xs.removeAt(it); return }
        }
    }

    fun dispatch(type: String, queued: Boolean = false, drop: Boolean = false) {
        if (queued) {
            queue += Triple(type, false, drop); return
        }
        val xs = events[type] ?: return
        var i = 0; while (i < xs.size) {
            xs[i].fn(); i++
        }; xs.removeAll { it.once }; if (xs.isEmpty() || drop) events.remove(type)
    }

    fun update() {
        while (queue.isNotEmpty()) {
            val x = queue.removeAt(0); dispatch(x.first, false, x.third)
        }
    }
}
