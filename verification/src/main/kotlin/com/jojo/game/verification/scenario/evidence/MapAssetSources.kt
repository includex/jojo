// Verification
package com.jojo.game.verification.scenario.evidence

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

/**
 * MapAssetSources: 내보내기가 남긴 배경 지도의 원본 자산 경로 표다.
 *
 * 원본의 렌더 이벤트는 텍스처를 우리 내보내기 파일 이름이 아니라 원본 자산 경로로
 * 부른다. 그 대응은 `tools/export_map_assets.py`가 파일을 복사하면서 이미 알고 있으므로
 * `manifest.json`의 `mapSources`에 적어 두고 여기서 그대로 인용한다. 배경마다 uuid를
 * 코드에 적어 두면 배경이 하나 늘 때마다 증거가 조용히 어긋난다.
 */
internal object MapAssetSources {
    /**
     * `sources` (상태 값): 배경 식별자에서 원본 자산 경로로 가는 대응을 보관한다.
     */

    private val sources: Map<Int, String> by lazy {
        val handle = Gdx.files.internal("maps/manifest.json")
        if (!handle.exists()) return@lazy emptyMap()
        val root = JsonReader().parse(handle.readString("UTF-8")).get("mapSources") ?: return@lazy emptyMap()
        buildMap {
            root.forEach { entry ->
                entry.name.toIntOrNull()?.let { put(it, entry.asString()) }
            }
        }
    }

    /** 원본 자산 경로: 배경 식별자에 해당하는 원본 경로를 반환한다. */
    fun nativePath(backgroundId: Int): String? = sources[backgroundId]
}
