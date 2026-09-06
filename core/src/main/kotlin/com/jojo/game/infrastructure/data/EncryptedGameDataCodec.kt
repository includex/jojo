// Game
package com.jojo.game.infrastructure.data

import java.security.MessageDigest

/** EncryptedGameDataCodec: 패키지에 포함된 암호화 게임 데이터를 바이트 단위로 복호화한다. */
object EncryptedGameDataCodec {
    private const val KEY = "ccz65Sha08GeZ1Fu"


    fun decode(raw: ByteArray): String? = runCatching {
        val decrypted = ByteArray(raw.size) { index ->
            val shift = KEY[index % KEY.length].code % 8
            val value = raw[index].toInt() and 0xff
            ((value ushr shift) or ((value shl (8 - shift)) and 0xff)).toByte()
        }.toString(Charsets.UTF_8)
        val digest = decrypted.take(32)
        val json = decrypted.drop(32)
        json.takeIf { digest.equals(md5(KEY + json), ignoreCase = true) }
    }.getOrNull()

    /** 원본 암호화 버퍼 기록 규칙에 맞춰 데이터를 인코딩한다. */
    internal fun encode(json: String): ByteArray {
        val input = (md5(KEY + json) + json).toByteArray(Charsets.UTF_8)
        return ByteArray(input.size) { index ->
            val shift = KEY[index % KEY.length].code % 8
            val value = input[index].toInt() and 0xff
            (((value shl shift) and 0xff) or (value ushr (8 - shift))).toByte()
        }
    }

    private fun md5(value: String): String = MessageDigest.getInstance("MD5")
        .digest(value.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }
}
