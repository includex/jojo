// Infrastructure
package com.jojo.game.infrastructure.security

import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * `FoundationCodec` 싱글턴 객체: security 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

object FoundationCodec {

    /**
     * `bytes`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
     * `text`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
     * `xor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun xor(a: ByteArray, key: String, decode: Boolean): ByteArray = ByteArray(a.size) { i ->
        val shift = key[i % key.length].code % 8
        val n =
            a[i].toInt() and 255; if (decode) ((n ushr shift) or (n shl (8 - shift) and 255)).toByte() else ((n shl shift and 255) or (n ushr (8 - shift))).toByte()
    }
}


/**
 * `Md5Service` 싱글턴 객체: security 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

object Md5Service {
    /**
     * `hex`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hex(s: String) =
        MessageDigest.getInstance("MD5").digest(FoundationCodec.bytes(s)).joinToString("") { "%02x".format(it) }

    /**
     * `b64`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun b64(s: String) = java.util.Base64.getEncoder().withoutPadding()
        .encodeToString(MessageDigest.getInstance("MD5").digest(FoundationCodec.bytes(s)))

    /**
     * `hmac`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hmac(key: String, text: String) =
        Mac.getInstance("HmacMD5").apply { init(SecretKeySpec(FoundationCodec.bytes(key), "HmacMD5")) }
            .doFinal(FoundationCodec.bytes(text)).joinToString("") { "%02x".format(it) }
}


/**
 * `UuidCodec` 클래스: security 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class UuidCodec {
    /**
     * `key` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
    /**
     * `hex` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val hex = "0123456789abcdef"
    /**
     * `compress`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun compress(v: String): String {
        val main = v.substringBefore('@'); if (main.length != 36) return v
        val raw = main.replace("-", "")
        val o = StringBuilder().append(raw[0]).append(raw[1]); for (i in 2 until 32 step 3) {
            val a = hex.indexOf(raw[i])
            val b = hex.indexOf(raw[i + 1])
            val c = hex.indexOf(raw[i + 2]); o.append(key[(a shl 2) + (b shr 2)]).append(key[((b and 3) shl 4) + c])
        }; return v.replace(main, o.toString())
    }

    /**
     * `decode`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
 * `UserPreferencesStore` 클래스: security 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class UserPreferencesStore(
    /**
     * `local` (MutableMap<String, String>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val local: MutableMap<String, String>,
    /**
     * `file` (MutableMap<String, String>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val file: MutableMap<String, String>,
    /**
     * `log` (MutableList<String>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val log: MutableList<String>
) {
    /**
     * `user` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val user = linkedMapOf<String, String>()
    /**
     * `global` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val global = linkedMapOf<String, String>()
    /**
     * `set`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun set(k: String, v: String, globalFlag: Boolean = false) {
        (if (globalFlag) global else user)[k] = v; flush(globalFlag)
    }

    /**
     * `get`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun get(k: String, d: String, globalFlag: Boolean = false) =
        if (globalFlag) global[k] ?: d else local[k] ?: user[k] ?: d

    /**
     * `flush`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun flush(globalFlag: Boolean = false) {
        if (globalFlag) {
            file["/w/UserData.json"] = "cipher"; log += "file:/w/UserData.json"
        } else {
            local["UserDefault"] = "cipher"; log += "local:UserDefault"
        }
    }

    /**
     * `delete`: 사용한 상태와 자원을 정리한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun delete(k: String, globalFlag: Boolean) {
        if (globalFlag) {
            global.remove(k); flush(true)
        } else {
            user.remove(k); flush(false)
        }
    }
}


/**
 * `StatusMachine` 클래스: security 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class StatusMachine {
    /**
     * `status` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var status = -1
    /**
     * `states` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val states = mutableMapOf<Int, Triple<() -> Unit, () -> Unit, () -> Unit>>()
    /**
     * `conditions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val conditions = mutableListOf<Triple<Int, Int, () -> Boolean>>()
    /**
     * `change`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun change(to: Int) {
        if (to == status) return; if (status != -1) states[status]!!.second(); status =
            to; if (status != -1) states[status]!!.first()
    }

    /**
     * `update`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun update() {
        conditions.forEach { if ((it.first == -1 || it.first == status) && it.third()) change(it.second) }; if (status != -1) states[status]!!.third()
    }

    /**
     * `clear`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun clear() {
        change(-1); conditions.clear(); states.clear()
    }
}


/**
 * `EventDispatcher` 클래스: security 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class EventDispatcher {
    /**
     * `L` 클래스: security 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class L(val target: Any, val fn: () -> Unit, val id: String, val once: Boolean)

    /**
     * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val events = linkedMapOf<String, MutableList<L>>()
    /**
     * `queue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val queue = mutableListOf<Triple<String, Boolean, Boolean>>()
    /**
     * `n` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var n = 0
    /**
     * `add`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun add(type: String, target: Any, fn: () -> Unit, once: Boolean = false): String {
        val id = "${n++}-${n++}"; events.getOrPut(type) { mutableListOf() } += L(target, fn, id, once); return id
    }

    /**
     * `remove`: 사용한 상태와 자원을 정리한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun remove(id: String) {
        events.values.forEach { xs ->
            xs.indexOfFirst { it.id == id }.takeIf { it >= 0 }?.let { xs.removeAt(it); return }
        }
    }

    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun dispatch(type: String, queued: Boolean = false, drop: Boolean = false) {
        if (queued) {
            queue += Triple(type, false, drop); return
        }
        val xs = events[type] ?: return
        var i = 0; while (i < xs.size) {
            xs[i].fn(); i++
        }; xs.removeAll { it.once }; if (xs.isEmpty() || drop) events.remove(type)
    }

    /**
     * `update`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun update() {
        while (queue.isNotEmpty()) {
            val x = queue.removeAt(0); dispatch(x.first, false, x.third)
        }
    }
}
