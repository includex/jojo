// Infrastructure
package com.jojo.game.infrastructure.data

import java.security.MessageDigest

/** 캠페인 저장 봉투의 해시 검증과 바이트 회전을 담당한다. */
object CampaignSaveCodec {
    private const val KEY = "ccz65Sha08GeZ1Fu"
    private const val SALT = "8015"

    /** JSON 저장 본문에 검증용 해시를 붙여 저장 형식으로 인코딩한다. */
    fun encode(json: String): String {
        val payload = md5("${KEY}_${json}_${SALT}") + json
        val bytes = payload.toByteArray(Charsets.UTF_8)
        return rotate(bytes, decrypt = false).toString(Charsets.ISO_8859_1)
    }

    /** 해시가 일치하는 저장 봉투만 JSON 본문으로 복호화한다. */
    fun decode(encoded: String): String? = runCatching {
        val decoded = rotate(encoded.toByteArray(Charsets.ISO_8859_1), decrypt = true)
            .toString(Charsets.UTF_8)
        val digest = decoded.take(32)
        val json = decoded.drop(32)
        json.takeIf { digest.equals(md5("${KEY}_${json}_${SALT}"), ignoreCase = true) }
    }.getOrNull()

    private fun rotate(bytes: ByteArray, decrypt: Boolean): ByteArray = ByteArray(bytes.size) { index ->
        val shift = KEY[index % KEY.length].code % 8
        val value = bytes[index].toInt() and 0xff
        val rotated = if (decrypt) {
            (value ushr shift) or ((value shl (8 - shift)) and 0xff)
        } else {
            ((value shl shift) and 0xff) or (value ushr (8 - shift))
        }
        rotated.toByte()
    }

    private fun md5(value: String): String = MessageDigest.getInstance("MD5")
        .digest(value.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }
}
