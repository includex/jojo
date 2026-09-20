package com.jojo.game

import com.jojo.game.presentation.battle.overlay.CocosLabelBaseline
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * 원본 `MineUnitInfoLayer`(ddb595c3…/`import/dd/dd2699f7-…528ab.json`)와
 * `OtherUnitInfoLayer`(`import/60/60e799d9-…1b511.json`) 프리팹이 적어 둔 라벨 노드 높이와,
 * 원본 엔진 `ttf.js`가 그 노드 안에서 글자 baseline을 잡는 규칙을 맞춰 본다.
 */
class SettlementInfoLabelBaselineTest {
    @Test fun `outlined value label node is the 54_4 the prefabs store`() {
        // 프리팹의 label0/label1/label/label2 노드는 모두 _contentSize 높이 54.4다.
        assertEquals(54.4f, CocosLabelBaseline.nodeHeight(lineHeight = 40f, outlineWidth = 2f), 1e-4f)
    }

    @Test fun `label without outline is the 50_4 of the weapon and armor rows`() {
        // MineUnitInfoLayer label3/label4/label5는 cc.LabelOutline이 없고 높이가 50.4다.
        assertEquals(50.4f, CocosLabelBaseline.nodeHeight(lineHeight = 40f, outlineWidth = 0f), 1e-4f)
    }

    @Test fun `centered baseline sits 12_4 above the outlined node bottom`() {
        // ttf.js _calculateFillTextStartPosition: 캔버스 위에서 42, 곧 노드 아래에서 12.4다.
        assertEquals(42f, CocosLabelBaseline.baselineFromTop(40f, 40f, 2f), 1e-4f)
        assertEquals(12.4f, CocosLabelBaseline.baselineFromBottom(40f, 40f, 2f), 1e-4f)
    }

    @Test fun `centered baseline sits 10_4 above a node without outline`() {
        assertEquals(40f, CocosLabelBaseline.baselineFromTop(40f, 40f, 0f), 1e-4f)
        assertEquals(10.4f, CocosLabelBaseline.baselineFromBottom(40f, 40f, 0f), 1e-4f)
    }

    @Test fun `other panel hp digits sit 12_4 above their own bar centre`() {
        // OtherUnitInfoLayer: bg 471x193.5, p0 _trs (24, -3) 크기 374x24, label0 _trs (-21, 12).
        // 포팅 좌표에서 bg 아래 모서리는 96이다.
        val bgCentreY = 96f + 193.5f / 2f
        val barCentreY = bgCentreY - 3f
        val labelNodeCentreY = barCentreY + 12f
        val labelNodeBottomY = labelNodeCentreY - CocosLabelBaseline.nodeHeight(40f, 2f) / 2f

        assertEquals(189.75f, barCentreY, 1e-4f)
        assertEquals(174.55f, labelNodeBottomY, 1e-4f)

        val baselineY = labelNodeBottomY + CocosLabelBaseline.baselineFromBottom(40f, 40f, 2f)
        assertEquals(186.95f, baselineY, 1e-4f)
        // 고치기 전 포팅은 캔버스 위에서 잰 42를 노드 아래에서 잰 값으로 써서
        // 216.55를 글자 윗선으로 삼았다. 두 값의 차 29.6이 이번 회귀의 크기다.
        assertEquals(29.6f, (labelNodeBottomY + 42f) - baselineY, 1e-4f)
    }

    @Test fun `mine panel rows use the per row prefab offsets`() {
        val bgCentreY = 96f + 258f / 2f
        val half = CocosLabelBaseline.nodeHeight(40f, 2f) / 2f
        val baseline = CocosLabelBaseline.baselineFromBottom(40f, 40f, 2f)
        // p0 (21, 36) 라벨 +12 / p1 (21, -15) 라벨 +9 / p2 (21, -66) 라벨 +9.
        assertEquals(245.8f, bgCentreY + 36f + 12f - half, 1e-4f)
        assertEquals(191.8f, bgCentreY - 15f + 9f - half, 1e-4f)
        assertEquals(140.8f, bgCentreY - 66f + 9f - half, 1e-4f)
        assertEquals(258.2f, 245.8f + baseline, 1e-4f)
        // label3/label4 _trs y = -102, 높이 50.4.
        val plainHalf = CocosLabelBaseline.nodeHeight(40f, 0f) / 2f
        assertEquals(97.8f, bgCentreY - 102f - plainHalf, 1e-4f)
        assertEquals(108.2f, 97.8f + CocosLabelBaseline.baselineFromBottom(40f, 40f, 0f), 1e-4f)
    }

    @Test fun `name row baselines follow the same rule on both panels`() {
        val half = CocosLabelBaseline.nodeHeight(40f, 2f) / 2f
        val baseline = CocosLabelBaseline.baselineFromBottom(40f, 40f, 2f)
        // MineUnitInfoLayer label0/label/label1/label2 _trs y = 96.7 (Lv/레벨은 96.718).
        val mineBottom = 96f + 258f / 2f + 96.7f - half
        assertEquals(294.5f, mineBottom, 1e-4f)
        assertEquals(306.9f, mineBottom + baseline, 1e-4f)
        // OtherUnitInfoLayer 같은 라벨들 _trs y = 61.3.
        val otherBottom = 96f + 193.5f / 2f + 61.3f - half
        assertEquals(226.85f, otherBottom, 1e-4f)
        assertEquals(239.25f, otherBottom + baseline, 1e-4f)
    }
}
