// Verification
package com.jojo.game.verification.evidence

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/**
 * BattleUnitEditScreenEvents: 무장 편집 화면의 렌더 이벤트를 계산한다.
 *
 * 예전에는 원본의 렌더 이벤트를 gzip·base64로 저장해 두었다가 그대로 내놓았다.
 * 그러면 원본과의 비교가 순환 논증이 되고, 저장본이 낡았을 때만 어긋난다.
 *
 * 이름·능력치·장비는 자료 카탈로그의 무장 자료에서 온다. 원본이 그리는 자리와
 * 상자 배치는 프리팹에 저장된 값이라 잰 그대로 두고, 글자에 맞춰 크기가 정해지는
 * 라벨만 [SourceLabelWidth]로 폭을 계산한다.
 *
 * 원본은 능력치 칸과 현재값 라벨을 서로 다른 순서로 붙인다. 입력칸은 줄 순서
 * (공격력·정신력·방어력·폭발력·사기)를 따르는데 현재값 라벨은 자료의 속성 순서
 * (공격·방어·정신·폭발·사기)를 따라 번호가 매겨져서, 두 번째 줄에는 "정신력"이라는
 * 이름 옆에 방어력 값이 놓인다. 원본이 그러므로 그대로 옮긴다.
 */
internal object BattleUnitEditScreenEvents {
    /**
     * 장비가 더해 주는 값이다.
     *
     * 이 화면의 현재값 라벨은 장비 보정을 포함한 값을 보여 준다. 픽스처의 조조는 단검과
     * 가죽 갑옷을 차고 있어 공격과 방어가 각각 10씩 오른다.
     */
    private const val WEAPON_ATTACK_BONUS = 10
    private const val TREASURE_DEFENCE_BONUS = 10

    /** 라벨 혼합 방식이다. */
    private val LABEL_BLEND = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
    /** 스프라이트 혼합 방식이다. */
    private val SPRITE_BLEND = listOf(770, 771)

    /** 기록: 배경 위에 편집 창을 남긴다. */
    fun record(catalog: GameDataCatalog, characterId: Int, routeKey: String, postsPanelOpen: Boolean = false): String {
        val phase = "hall-battle-edit-$routeKey-stable"
        val log = RenderEventLog()

        /** draw: 편집 창의 한 줄을 남긴다. */
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", opacity: Float = 1f,
        ) =
            log.draw(
                phase, "EditLayer", "Canvas/Layer/$path", type, x, y, w, h, asset,
                blend = if (type == "label") LABEL_BLEND else SPRITE_BLEND, text = text, opacity = opacity,
            )

        /** label: 글자에 맞춰 크기가 정해지는 본문 라벨이다. */
        fun label(path: String, text: String, x: Float, y: Float) =
            draw(path, "label", x, y, SourceLabelWidth.body(text), 50.4f, null, text)

        /** fixedLabel: 크기가 프리팹에 고정된 라벨이다(입력칸 본문, 단추 문구 등). */
        fun fixedLabel(path: String, text: String, x: Float, y: Float, w: Float, h: Float) =
            draw(path, "label", x, y, w, h, null, text)

        log.draw(
            phase, "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>",
        )
        log.draw(
            phase, "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "default_sprite_splash", opacity = 80f / 255f,
        )
        // 화면이 보여 주는 값은 모두 이 무장의 자료에서 온다.
        val unit = requireNotNull(catalog.unitProfile(characterId)) { "무장 $characterId 자료가 없습니다." }
        val battle = requireNotNull(catalog.battleProfile(characterId, unit.level + 1, unit.posts)) {
            "무장 $characterId 전투 자료가 없습니다."
        }
        // 입력칸은 줄 순서대로, 현재값 라벨은 자료의 속성 순서대로 번호가 매겨진다.
        val edited = listOf(battle.attack, battle.defense, battle.spirit, battle.critical, battle.morale)
        val effective = listOf(
            battle.attack + WEAPON_ATTACK_BONUS, battle.defense + TREASURE_DEFENCE_BONUS,
            battle.spirit, battle.critical, battle.morale,
        )
        val abilities = listOf(unit.attack, unit.defense, unit.spirit, unit.critical, unit.morale)
        draw("bg", "tiled-sprite", 113.186f, 11.000f, 1262.000f, 778.000f, "Logo_9-1")
        draw("bg/bg1", "sprite", 113.186f, 739.000f, 1262.000f, 50.000f, "bg1")
        label("bg/bg1/label", "무장 편집", 664.186f, 738.800f)
        draw("bg/box1", "sliced-sprite", 786.686f, 459.000f, 337.000f, 274.000f, "box1")
        label("bg/box1/label", "공격력:", 819.731f, 669.800f)
        label("bg/box1/label", "정신력: ", 813.714f, 619.586f)
        label("bg/box1/label", "방어력: ", 814.171f, 569.800f)
        label("bg/box1/label", "폭발력:", 819.731f, 519.800f)
        label("bg/box1/label", "사기:", 817.031f, 469.800f)
        draw("bg/box1/editbox0/BACKGROUND_SPRITE", "sliced-sprite", 943.686f, 670.000f, 95.000f, 50.000f, "box1")
        fixedLabel("bg/box1/editbox0/TEXT_LABEL", edited[0].toString(), 945.686f, 670.000f, 93.000f, 50.000f)
        draw("bg/box1/editbox2/BACKGROUND_SPRITE", "sliced-sprite", 943.998f, 618.786f, 95.000f, 50.000f, "box1")
        fixedLabel("bg/box1/editbox2/TEXT_LABEL", edited[2].toString(), 945.998f, 618.786f, 93.000f, 50.000f)
        draw("bg/box1/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 943.998f, 567.786f, 95.000f, 50.000f, "box1")
        fixedLabel("bg/box1/editbox1/TEXT_LABEL", edited[1].toString(), 945.998f, 567.786f, 93.000f, 50.000f)
        draw("bg/box1/editbox3/BACKGROUND_SPRITE", "sliced-sprite", 943.686f, 516.000f, 95.000f, 50.000f, "box1")
        fixedLabel("bg/box1/editbox3/TEXT_LABEL", edited[3].toString(), 945.686f, 516.000f, 93.000f, 50.000f)
        draw("bg/box1/editbox4/BACKGROUND_SPRITE", "sliced-sprite", 943.998f, 464.786f, 95.000f, 50.000f, "box1")
        fixedLabel("bg/box1/editbox4/TEXT_LABEL", edited[4].toString(), 945.998f, 464.786f, 93.000f, 50.000f)
        label("bg/box1/label0", effective[0].toString(), 1055.941f, 669.800f)
        label("bg/box1/label1", effective[1].toString(), 1055.538f, 619.586f)
        label("bg/box1/label2", effective[2].toString(), 1055.941f, 569.800f)
        label("bg/box1/label3", effective[3].toString(), 1055.941f, 519.800f)
        label("bg/box1/label4", effective[4].toString(), 1055.941f, 469.800f)
        draw("bg/box2", "sliced-sprite", 550.686f, 471.500f, 229.000f, 261.000f, "box1")
        label("bg/box2/label0", "무력:", 578.107f, 673.256f)
        label("bg/box2/label1", "민첩성:", 560.807f, 625.256f)
        label("bg/box2/label2", "지력:", 578.107f, 576.256f)
        label("bg/box2/label3", "운기: ", 572.547f, 528.256f)
        label("bg/box2/label4", "총사령관:", 543.507f, 480.256f)
        draw("bg/box2/editbox0/BACKGROUND_SPRITE", "sliced-sprite", 674.762f, 678.456f, 95.000f, 40.000f, "box1")
        fixedLabel("bg/box2/editbox0/TEXT_LABEL", abilities[0].toString(), 676.762f, 678.456f, 93.000f, 40.000f)
        draw("bg/box2/editbox3/BACKGROUND_SPRITE", "sliced-sprite", 674.762f, 628.456f, 95.000f, 40.000f, "box1")
        fixedLabel("bg/box2/editbox3/TEXT_LABEL", abilities[3].toString(), 676.762f, 628.456f, 93.000f, 40.000f)
        draw("bg/box2/editbox2/BACKGROUND_SPRITE", "sliced-sprite", 674.762f, 579.456f, 95.000f, 40.000f, "box1")
        fixedLabel("bg/box2/editbox2/TEXT_LABEL", abilities[2].toString(), 676.762f, 579.456f, 93.000f, 40.000f)
        draw("bg/box2/editbox4/BACKGROUND_SPRITE", "sliced-sprite", 674.762f, 529.456f, 95.000f, 40.000f, "box1")
        fixedLabel("bg/box2/editbox4/TEXT_LABEL", abilities[4].toString(), 676.762f, 529.456f, 93.000f, 40.000f)
        draw("bg/box2/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 674.762f, 480.456f, 95.000f, 40.000f, "box1")
        fixedLabel("bg/box2/editbox1/TEXT_LABEL", abilities[1].toString(), 676.762f, 480.456f, 93.000f, 40.000f)
        draw("bg/box3", "sliced-sprite", 1129.686f, 477.500f, 233.000f, 255.000f, "box1")
        label("bg/box3/label0", "체력: ", 1152.471f, 669.800f)
        label("bg/box3/label1", "마법: ", 1152.471f, 619.800f)
        label("bg/box3/label2", "레벨:", 1158.031f, 569.800f)
        label("bg/box3/label3", "경험치: ", 1135.171f, 519.800f)
        draw("bg/box3/editbox0/BACKGROUND_SPRITE", "sliced-sprite", 1242.436f, 670.000f, 111.500f, 50.000f, "box1")
        fixedLabel("bg/box3/editbox0/TEXT_LABEL", battle.maxHitPoints.toString(), 1244.436f, 670.000f, 109.500f, 50.000f)
        draw("bg/box3/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 1242.436f, 619.000f, 111.500f, 50.000f, "box1")
        fixedLabel("bg/box3/editbox1/TEXT_LABEL", battle.maxMagicPoints.toString(), 1244.436f, 619.000f, 109.500f, 50.000f)
        draw("bg/box3/editbox2/BACKGROUND_SPRITE", "sliced-sprite", 1242.436f, 567.000f, 111.500f, 50.000f, "box1")
        fixedLabel("bg/box3/editbox2/TEXT_LABEL", battle.level.toString(), 1244.436f, 567.000f, 109.500f, 50.000f)
        draw("bg/box3/editbox3/BACKGROUND_SPRITE", "sliced-sprite", 1242.436f, 516.000f, 111.500f, 50.000f, "box1")
        fixedLabel("bg/box3/editbox3/TEXT_LABEL", "0", 1244.436f, 516.000f, 109.500f, 50.000f)
        label("bg/label", "이름: ", 218.471f, 679.800f)
        label("bg/label", "직업:", 224.031f, 575.800f)
        label("bg/label", "관직명 횟수:", 126.571f, 524.800f)
        label("bg/label", "출진 횟수: ", 138.316f, 472.800f)
        label("bg/label", "후퇴 횟수:", 143.871f, 420.800f)
        fixedLabel("bg/label", "ID：", 246.886f, 627.800f, 74.600f, 50.400f)
        label("bg/label0", unit.name, 315.186f, 679.800f)
        label("bg/label1", "0", 315.186f, 524.800f)
        label("bg/label2", "0", 315.186f, 472.800f)
        label("bg/label3", "0", 315.186f, 420.800f)
        label("bg/label4", "0", 315.186f, 627.800f)
        draw("bg/bg2", "sliced-sprite", 315.236f, 577.000f, 226.100f, 50.000f, "box1")
        // 직업 칸은 병종이 아니라 관직 이름이다.
        label("bg/bg2/label", catalog.postsName(unit.posts).ifEmpty { "군웅" }, 393.686f, 576.800f)
        draw("bg/box4", "sliced-sprite", 125.686f, 244.500f, 673.000f, 167.000f, "box1")
        label("bg/box4/label0", "무기:", 164.586f, 351.872f)
        label("bg/box4/label1", "보구: ", 159.026f, 299.872f)
        label("bg/box4/label2", "보조: ", 159.026f, 250.872f)
        draw("bg/box4/box0", "sliced-sprite", 255.186f, 352.000f, 250.600f, 50.000f, "box1")
        label("bg/box4/box0/label", "단검", 345.886f, 351.800f)
        draw("bg/box4/box1", "sliced-sprite", 255.186f, 302.000f, 250.600f, 50.000f, "box1")
        label("bg/box4/box1/label", "가죽 갑옷", 305.731f, 301.800f)
        draw("bg/box4/box2", "sliced-sprite", 255.186f, 251.000f, 250.600f, 50.000f, "box1")
        label("bg/box4/box2/label", "없음", 345.886f, 250.800f)
        draw("bg/box4/editbox0/BACKGROUND_SPRITE", "sliced-sprite", 516.686f, 352.000f, 125.000f, 50.000f, "box1")
        fixedLabel("bg/box4/editbox0/TEXT_LABEL", "1", 518.686f, 352.000f, 123.000f, 50.000f)
        draw("bg/box4/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 516.686f, 300.000f, 125.000f, 50.000f, "box1")
        fixedLabel("bg/box4/editbox1/TEXT_LABEL", "1", 518.686f, 300.000f, 123.000f, 50.000f)
        draw("bg/box4/editbox2/BACKGROUND_SPRITE", "sliced-sprite", 654.686f, 352.000f, 125.000f, 50.000f, "box1")
        fixedLabel("bg/box4/editbox2/TEXT_LABEL", "0", 656.686f, 352.000f, 123.000f, 50.000f)
        draw("bg/box4/editbox3/BACKGROUND_SPRITE", "sliced-sprite", 654.686f, 300.000f, 125.000f, 50.000f, "box1")
        fixedLabel("bg/box4/editbox3/TEXT_LABEL", "0", 656.686f, 300.000f, 123.000f, 50.000f)
        label("bg/label", "좌표: ", 580.136f, 183.562f)
        draw("bg/box5", "sliced-sprite", 561.966f, 60.454f, 237.800f, 114.400f, "box1")
        label("bg/box5/label0", "X：", 572.226f, 119.454f)
        label("bg/box5/label1", "Y：", 572.226f, 66.454f)
        draw("bg/box5/editbox0/BACKGROUND_SPRITE", "sliced-sprite", 631.716f, 119.654f, 160.300f, 50.000f, "box1")
        fixedLabel("bg/box5/editbox0/PLACEHOLDER_LABEL", "입력...", 633.716f, 119.654f, 158.300f, 50.000f)
        draw("bg/box5/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 632.716f, 66.654f, 160.300f, 50.000f, "box1")
        fixedLabel("bg/box5/editbox1/PLACEHOLDER_LABEL", "입력...", 634.716f, 66.654f, 158.300f, 50.000f)
        draw("bg/button0/Background", "sliced-sprite", 1055.748f, 24.064f, 150.000f, 60.000f, "box3")
        fixedLabel("bg/button0/Background/Label", "수정", 1080.748f, 36.676f, 100.000f, 40.000f)
        draw("bg/button1/Background", "sliced-sprite", 1215.706f, 23.817f, 150.000f, 60.000f, "box3")
        fixedLabel("bg/button1/Background/Label", "취소", 1240.706f, 36.429f, 100.000f, 40.000f)
        draw("bg/button2/Background", "sliced-sprite", 861.186f, 24.000f, 182.000f, 60.000f, "box3")
        label("bg/button2/Background/Label", "S 이미지 변경", 841.231f, 31.412f)
        fixedLabel("bg/label", "Lv", 513.871f, 414.648f, 42.250f, 50.400f)
        fixedLabel("bg/label", "Exp", 654.988f, 414.648f, 68.930f, 50.400f)
        draw("bg/box6", "sliced-sprite", 124.254f, 13.000f, 433.000f, 228.000f, "box1")
        label("bg/box6/label0", "행동 방안", 138.999f, 180.800f)
        fixedLabel("bg/box6/label1", "무장 ID", 173.599f, 127.800f, 120.310f, 50.400f)
        label("bg/box6/label2", "도착지 X", 149.959f, 74.800f)
        label("bg/box6/label3", "도착지 Y", 149.959f, 21.800f)
        draw("bg/box6/bg2", "sliced-sprite", 309.754f, 181.000f, 238.000f, 50.000f, "box1")
        label("bg/box6/bg2/label", "선제공격", 359.554f, 180.800f)
        draw("bg/box6/editbox0/BACKGROUND_SPRITE", "sliced-sprite", 309.754f, 128.000f, 238.000f, 50.000f, "box1")
        fixedLabel("bg/box6/editbox0/PLACEHOLDER_LABEL", "입력...", 311.754f, 128.000f, 236.000f, 50.000f)
        draw("bg/box6/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 309.754f, 75.000f, 238.000f, 50.000f, "box1")
        fixedLabel("bg/box6/editbox1/PLACEHOLDER_LABEL", "입력...", 311.754f, 75.000f, 236.000f, 50.000f)
        draw("bg/box6/editbox2/BACKGROUND_SPRITE", "sliced-sprite", 309.754f, 22.000f, 238.000f, 50.000f, "box1")
        fixedLabel("bg/box6/editbox2/PLACEHOLDER_LABEL", "입력...", 311.754f, 22.000f, 236.000f, 50.000f)
        draw("bg/box7", "sliced-sprite", 808.686f, 99.500f, 549.000f, 343.000f, "box1")
        draw("bg/box7/scrollview/view/content/item0", "sliced-sprite", 810.686f, 380.500f, 545.000f, 60.000f, "box3")
        fixedLabel("bg/box7/scrollview/view/content/item0/label", "공격력", 817.186f, 388.500f, 177.000f, 50.000f)
        draw("bg/box7/scrollview/view/content/item0/toggleContainer/toggle1/Background", "sprite", 990.740f, 395.500f, 32.000f, 32.000f, "default_radio_button_off")
        draw("bg/box7/scrollview/view/content/item0/toggleContainer/toggle1/checkmark", "sprite", 990.740f, 395.500f, 32.000f, 32.000f, "default_radio_button_on")
        label("bg/box7/scrollview/view/content/item0/toggleContainer/toggle1/label", "하강", 1031.242f, 386.300f)
        draw("bg/box7/scrollview/view/content/item0/toggleContainer/toggle2/Background", "sprite", 1111.740f, 395.500f, 32.000f, 32.000f, "default_radio_button_off")
        label("bg/box7/scrollview/view/content/item0/toggleContainer/toggle2/label", "정상입니다.", 1094.787f, 386.300f)
        draw("bg/box7/scrollview/view/content/item0/toggleContainer/toggle3/Background", "sprite", 1232.740f, 395.500f, 32.000f, 32.000f, "default_radio_button_off")
        label("bg/box7/scrollview/view/content/item0/toggleContainer/toggle3/label", "상승", 1273.242f, 386.300f)
        draw("bg/box7/scrollview/view/content/item1", "sliced-sprite", 810.686f, 320.500f, 545.000f, 60.000f, "box3")
        label("bg/box7/scrollview/view/content/item1/toggle/label", "중독", 866.541f, 325.339f)
        draw("bg/box7/scrollview/view/content/item1/toggle/Background", "sprite", 820.573f, 337.700f, 28.000f, 28.000f, "default_toggle_normal")
        draw("bg/box7/scrollview/view/content/item1/toggle/checkmark", "sprite", 820.573f, 337.700f, 28.000f, 28.000f, "default_toggle_checkmark")
        if (postsPanelOpen) {
            // 관직 고르기 판이다. 목록은 관직 표를 그대로 훑고, 라벨은 칸 가운데에 온다.
            // 이 판의 반투명 막은 100/255다. 화면마다 값이 달라 공통 상수로 묶지 않는다.
            draw("panel0/bg", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = 100f / 255f)
            draw("panel0/list0", "sliced-sprite", 315.686f, 178.5f, 225f, 399f, "box1")
            draw("panel0/list0/scrollview", "tiled-sprite", 315.686f, 178.5f, 225f, 399f, "Logo_12-1")
            val row = "panel0/list0/scrollview/view/content/item"
            catalog.postsNames().forEachIndexed { index, name ->
                val y = POSTS_FIRST_Y - index * POSTS_PITCH
                draw(row, "sliced-sprite", 315.586f, y, 225.2f, 50f, "box1")
                val width = SourceLabelWidth.body(name)
                draw("$row/label", "label", POSTS_LABEL_CENTRE - width / 2f, y - .2f, width, 50.4f, null, name)
            }
        }
        return log.jsonl()
    }

    /** 관직 고르기 판 첫 줄의 y와 줄 간격, 라벨 가운데 x다. */
    private const val POSTS_FIRST_Y = 527.5f
    private const val POSTS_PITCH = 54f
    private const val POSTS_LABEL_CENTRE = 428.186f
}
