package com.jojo.game.infrastructure.data

import java.security.MessageDigest

/**
 * Codec for the campaign save envelope.
 *
 * This format prefixes an MD5 digest and rotates every byte by a
 * position-dependent amount. Keeping this format lets the Kotlin game read
 * and write the established durable save container.
 */
/**
 * object  `CampaignSaveCodec`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object CampaignSaveCodec {
    private const val KEY = "ccz65Sha08GeZ1Fu"
    private const val SALT = "8015"

    /**
     * 공개 메서드 `encode`
     *
     * ### 파라미터
    - `json` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun encode(json: String): String {
        val payload = md5("${KEY}_${json}_${SALT}") + json
        val bytes = payload.toByteArray(Charsets.UTF_8)
        return rotate(bytes, decrypt = false).toString(Charsets.ISO_8859_1)
    }

    /**
     * 공개 메서드 `decode`
     *
     * ### 파라미터
    - `encoded` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
