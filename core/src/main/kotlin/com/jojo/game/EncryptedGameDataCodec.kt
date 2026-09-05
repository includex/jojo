package com.jojo.game

import java.security.MessageDigest

/** Decodes the packaged encrypted game-data format byte-for-byte. */
object EncryptedGameDataCodec {
    private const val KEY = "ccz65Sha08GeZ1Fu"

    /**
     * 공개 메서드 `decode`
     *
     * ### 파라미터
    - `raw` (`ByteArray`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /** Test/helper counterpart of the original encrypted BufferAsset writer. */
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
