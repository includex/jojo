// Battle
package com.jojo.game.presentation.battle.render

import com.jojo.game.presentation.i18n.SystemMessage

import com.jojo.game.presentation.shared.overlay.MagicUiList
import com.jojo.game.presentation.battle.assets.BattleUiAssets

/** 전투 대화 렌더 계약: 마법 목록과 상세 창의 원본 아이콘·범위 이미지 배치 좌표를 고정한다. */
object BattleDialogRenderContract {
    /** 대화 창 스프라이트: 원본 자원 경로와 화면 출력 사각형을 함께 보관한다. */
    data class Sprite(val path: String, val x: Float, val y: Float, val width: Float, val height: Float)

    /**
     * 마법 카드 글자색: 원본 `battle/MagickListLayer.js:64-68`의 `_dis`가 정하는 세 색이다.
     *
     * ```
     * for (n = 0; n < 2; n++) seekNodeByName("label"+n, t).color = r ? cc.color(0) : cc.color(8355711);
     * seekNodeByName("label2", t).color = r ? cc.color(0) : e ? cc.color(139,33,33) : cc.color(8355711);
     * ```
     * `r`은 쓸 수 있는 마법, `e`는 MP가 모자란 경우다. 곧 이름·위력은 검정 아니면
     * `8355711 = 0x7f7f7f`이고, **비용(label2)만** MP 부족일 때 (139,33,33)으로 바뀐다.
     *
     * 포트는 비활성 색으로 `Color(.5f, .5f, .5f, 1f)` = (128,128,128)을 써서 한 채널씩
     * 어긋났고, 비용 라벨의 붉은 색은 아예 없었다.
     */
    const val CARD_LABEL_COLOR = "#000000"

    /** 쓸 수 없는 마법의 이름·위력 색: `cc.color(8355711)`이다. */
    const val CARD_DISABLED_COLOR = "#7f7f7f"

    /** MP가 모자란 마법의 비용 색: `cc.color(139, 33, 33)`이다. */
    const val CARD_SHORT_MP_COLOR = "#8b2121"

    /** 목록 아이콘: 마법 행의 기준 좌표에서 아이콘 출력 사각형을 계산한다. */
    fun magicListIcon(magic: MagicUiList.Magic, x: Float, y: Float) =
        Sprite(BattleUiAssets.magicIcon(magic.icon), x + 5.073f, y + 57.383f, 76.8f, 76.8f)

    /**
     * 피해 계수 문구: 원본 `battle/MagickListLayer.js:147-148`의
     * `var i = t.power(); null != i ? i /= 100 : i = "없음";`을 옮긴 것이다.
     *
     * 원본은 JS 숫자를 그대로 문자열로 만들므로 **정수는 소수점이 붙지 않는다**. 위력 100은
     * `"1"`, 0은 `"0"`이며 28만 `"0.28"`이다. 포트는 `(power / 100f).toString()`을 써서 각각
     * `"1.0"`·`"0.0"`을 그렸다. 부동소수 반올림이 문자열에 새지 않도록 10진수로 나눈다.
     *
     * `power`가 null인 경우(`magicAttr2`가 undefined를 돌려주는 마법)는 원본이 `"없음"`을
     * 쓴다. 포트의 `GameDataCatalog.MagicProfile.power`는 비-널 `Int`라 지금은 이 가지에
     * 닿지 않는다 — 표가 위력 없음을 어떻게 적는지는 아직 확인하지 못했다.
     */
    fun damageCoefficientText(power: Int?): String {
        val value = power ?: return SystemMessage.S_D58FA73ADC
        return java.math.BigDecimal(value)
            .divide(java.math.BigDecimal(100))
            .stripTrailingZeros()
            .toPlainString()
    }

    /** 상세 스프라이트: 선택한 마법의 아이콘·대상 범위·효과 범위를 원본 레이아웃 순서로 반환한다. */
    fun magicDetailSprites(magic: MagicUiList.Magic) = listOf(
        Sprite(BattleUiAssets.magicIcon(magic.icon), 478.186f, 562f, 80f, 80f),
        Sprite(BattleUiAssets.hitArea(magic.hit), 834.213f, 450.755f, 160f, 160f),
        Sprite(BattleUiAssets.effectArea(magic.eff), 834.213f, 219.367f, 160f, 160f),
    )
}
