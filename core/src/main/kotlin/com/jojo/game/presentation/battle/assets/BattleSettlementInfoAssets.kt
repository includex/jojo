// Battle
package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.utils.Disposable

/**
 * BattleSettlementInfoAssets: 정산 상태창(`MineUnitInfoLayer`/`OtherUnitInfoLayer`)이 쓰는
 * 원본 SpriteFrame들을 경로별로 한 번만 읽어 둔다.
 *
 * 좌표와 자원 경로는 [com.jojo.game.presentation.battle.overlay.SettlementInfoRenderContract]가
 * 원본 프리팹에서 옮겨 온 값이다. 여기서는 그 경로를 그대로 받아 텍스처만 돌려준다.
 */
class BattleSettlementInfoAssets : Disposable {
    /** 경로별 텍스처 캐시다. 없는 파일은 한 번만 시도하고 null로 남긴다. */
    private val textures = linkedMapOf<String, Texture?>()

    /** cap inset이 있는 프레임의 9분할 캐시다. */
    private val patches = linkedMapOf<String, NinePatch?>()

    /** `texture`: 계약이 지정한 자원 경로의 텍스처를 돌려준다. */
    fun texture(path: String): Texture? = textures.getOrPut(path) {
        Gdx.files.internal(path).takeIf { it.exists() }?.let { Texture(it) }
    }

    /**
     * `draw`: 계약의 스프라이트 한 장을 원본 `cc.Sprite._type` 규칙대로 그린다.
     *
     * cap inset이 0이면 SIMPLE 스프라이트처럼 그대로 늘이고, 0보다 크면 SLICED
     * 스프라이트처럼 가장자리를 고정한 9분할로 그린다.
     */
    fun draw(batch: Batch, path: String, x: Float, y: Float, width: Float, height: Float, capInset: Int = 0) {
        if (capInset <= 0) {
            texture(path)?.let { batch.draw(it, x, y, width, height) }
            return
        }
        patches.getOrPut(path) {
            texture(path)?.let { NinePatch(it, capInset, capInset, capInset, capInset) }
        }?.draw(batch, x, y, width, height)
    }

    override fun dispose() {
        textures.values.filterNotNull().forEach(Texture::dispose)
        textures.clear()
    }
}
