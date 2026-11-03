package com.jojo.game.presentation.i18n

import java.text.MessageFormat
import java.util.Locale
import com.jojo.game.infrastructure.data.ReviewedKoreanText

/** Central catalog for statically authored system and UI messages. */
object SystemMessage {
    val CONFIRM_NO: String get() = text(Key.CONFIRM_NO)

    enum class Language { KOREAN, ENGLISH }

    @Volatile
    var language: Language = Language.KOREAN

    enum class Key {
        CONFIRM_NO,
        ITEM_PURCHASE,
        ITEM_SALE,
        ITEM_DISCARDED,
        UNIT_FALLBACK,
        STAT_BONUS,
        CHAPTER_PREFIX,
        TREASURE_PROGRESS,
        SELL_PRICE,
        INVENTORY_COUNT,
        PRICE,
        TROOP_COUNT,
        SELECTION_TOTAL,
        EVENT_WEATHER,
        BATTLE_ROUND,
        AI_TURN_SUMMARY,
        AI_UNIT_ACTION,
        UNIT_SELECTED,
        UNIT_SELECTED_MOVE,
        UNIT_COMMAND,
        UNIT_MOVE_DONE,
        UNIT_WAITING,
        UNIT_IDLE,
        MAGIC_TARGET,
        ITEM_TARGET,
        TACTICAL_MOVE,
        TACTICAL_ATTACK,
        TACTICAL_MAGIC,
        COMBAT_DAMAGE,
        MAGIC_RECOVERY,
        STATUS_CURED,
        ATTRIBUTE_INCREASE,
        HP_INCREASE,
        MP_INCREASE,
        SAVE_CONFIRM,
        LOAD_CONFIRM,
        ITEM_DROP_CONFIRM,
        RESOURCE_LOADING,
        SAVE_EQUIPMENT_NOTICE,
        ABILITY_LEVEL,
        PROMOTION_LEVEL,
        MAGIC_LEARNED,
        STAGE_NAME,
        VERIFICATION_UNIT_MISSING,
        VERIFICATION_MAP_MISSING,
        VERIFICATION_UNIT_COUNT,
        VERIFICATION_PLAYER_UNIT,
        VERIFICATION_ENEMY_UNIT,
        VERIFICATION_SPRITE_MISSING,
        S_3CFBA6C9EC,
        S_C51A025896,
        S_C50A5CA288,
        S_39747B892D,
        S_DE036B2104,
        S_5250AF0F28,
        S_9775E6B8B8,
        S_DF70B8A5DA,
        S_5FC06645C6,
        S_6D37F45C92,
        S_CEBD2DE9D2,
        S_DBCA581BF5,
        S_514FD95472,
        S_8828CC1AE7,
        S_A68C406265,
        S_606322672A,
        S_1FC67EAEB1,
        S_AB9FC6A986,
        S_C66E599032,
        S_AC5814F977,
        S_42FA751364,
        S_51F600E95C,
        S_6C9CBA423C,
        S_360FBBD2E1,
        S_F5CE653B43,
        S_79B24282A6,
        S_37E8F20B08,
        S_1DF67FC361,
        S_C61005675A,
        S_AF99A26F2C,
        S_A32E895914,
        S_F1A4254DC6,
        S_1CB107B8F7,
        S_BE182D9FB6,
        S_FC6E68F342,
        S_A455E6907B,
        S_62D82D76E7,
        S_BEF87719AB,
        S_293960AB27,
        S_04DE7DBEE2,
        S_EF7B15F963,
        S_DA1FF45B2C,
        S_F12C80F80B,
        S_0C5EE2A0B8,
        S_E134585B3A,
        S_682E465A4D,
        S_2A9C189A93,
        S_14E9E7E054,
        S_657B5FEB42,
        S_D26EB7B9F4,
        S_50B8DF8C55,
        S_F9225E4FF8,
        S_47973E953D,
        S_AB4EAC0279,
        S_D7F1F76848,
        S_FD293C8169,
        S_E8B701A283,
        S_45EDDC7A28,
        S_620D5989B0,
        S_EB5475E97C,
        S_BE23B82091,
        S_77286CB72C,
        S_D358176111,
        S_AA5BAA93E1,
        S_9FC8FD5C0F,
        S_3098F076B7,
        S_D8BC2DB4DC,
        S_2412A70276,
        S_682998EE62,
        S_A0B2E8A290,
        S_A8DE9DA622,
        S_0011209CB1,
        S_B93949DF7E,
        S_7F1D8C413D,
        S_2C41F8D606,
        S_555C05543A,
        S_87F35DB204,
        S_21BAD5251F,
        S_00132A46FC,
        S_CA5EEA5B52,
        S_374A498946,
        S_D7A0CB68D6,
        S_8F2A423094,
        S_5503080A20,
        S_9E9117D015,
        S_F9CCB2F99F,
        S_FBBD1D1816,
        S_B8C06C1007,
        S_D4B491F164,
        S_B3940E2B50,
        S_DF72A8753D,
        S_C80F942461,
        S_B1622411B7,
        S_367A0A7181,
        S_09584A936A,
        S_3424D32FC7,
        S_5BB1778DCA,
        S_0CC0A4F490,
        S_7579657A88,
        S_2DD57A2739,
        S_9EC75F36CA,
        S_E1864601EE,
        S_453D0D2DF5,
        S_E10C69D0A1,
        S_9D41799D25,
        S_39A4B2720C,
        S_DD3E1D7F75,
        S_38CA304494,
        S_93DF8F92C4,
        S_4956384FE9,
        S_9A7F95E71C,
        S_D1379C024C,
        S_C88072A92C,
        S_DC25BE8397,
        S_B7A3BFF7B9,
        S_66213DD030,
        S_E31BDBE56B,
        S_A9A59D5FE3,
        S_90213F42FB,
        S_42D16CB521,
        S_A7FDC582CF,
        S_9417C6AB3A,
        S_EE52984C60,
        S_8C4DAC1FCC,
        S_2B3DFBC5AD,
        S_7B01D0F5A8,
        S_256C17D85E,
        S_D8F7F3C37F,
        S_A6A330AD58,
        S_0A8D694790,
        S_2E4EDF0E35,
        S_BBEF509C8D,
        S_54F6815FE2,
        S_757535E6BD,
        S_CAA3F84FF8,
        S_983FFCFEC3,
        S_80440AFFDF,
        S_DAA1EDD590,
        S_2A87C14E4A,
        S_44F9A814FE,
        S_5C349008C3,
        S_7DC19BD3AD,
        S_F209C5D3A3,
        S_57A9A4502B,
        S_6DB5F96BD1,
        S_57E6F11841,
        S_7F1A273C82,
        S_2F7F8DF945,
        S_BD54114CB4,
        S_1B421182BF,
        S_54641773A6,
        S_3830A5432E,
        S_E7236FD795,
        S_661525CA4A,
        S_EC935E3110,
        S_35FF880D87,
        S_51378A4614,
        S_BC8AE9E500,
        S_2179DA2CFF,
        S_17F4CAA0C8,
        S_08E3B7B077,
        S_4DF0D9A984,
        S_F0D2E9FDE8,
        S_F407A5EA8C,
        S_8767F1CDA5,
        S_3A6CD5555C,
        S_EBE739C92D,
        S_F01A5A5229,
        S_D3B0E1A367,
        S_5FF079CE3D,
        S_DE1AE880EF,
        S_F8FC3619DB,
        S_C91E618B9A,
        S_9A6128E64A,
        S_CA6C5A2257,
        S_9C9F1862EA,
        S_B9A3425560,
        S_2D243A64E7,
        S_30373C627C,
        S_18CB232899,
        S_F5798CEF9A,
        S_59463E84FB,
        S_DF6168DB1C,
        S_9F66DED326,
        S_2926977BA7,
        S_EA6F893483,
        S_1BCE60B07B,
        S_9CA0190DEC,
        S_09C5A32367,
        S_4A021545F6,
        S_DB42F54740,
        S_FB5F03E2EE,
        S_841964364C,
        S_8DD48EC033,
        S_E6B2B4DBC0,
        S_F0EFEB11FB,
        S_85DE958B5F,
        S_991BD0CC20,
        S_E1407B5115,
        S_7CC8A7A989,
        S_0F14132CB0,
        S_01DDCA043B,
        S_F089BA034E,
        S_CBDCD710AB,
        S_90E5E4D22F,
        S_756B2E906A,
        S_5C77F2F6FC,
        S_5A268D48CF,
        S_8451709DFE,
        S_BDA0208A5C,
        S_7210A3FD77,
        S_0DFFF1CFEB,
        S_3843E8E488,
        S_2F82A7D82B,
        S_17BDB458F6,
        S_6EEBC7BA34,
        S_A889CDE68C,
        S_C5D202B13B,
        S_BDBDED5992,
        S_CFEF357D40,
        S_B62250FE8D,
        S_75C20D3862,
        S_8F3BC8881B,
        S_A7202337A7,
        S_93EEFD6430,
        S_CEF693F5A0,
        S_388DB7E1C8,
        S_A3E8A0BBCD,
        S_8916B6394A,
        S_273718B1D7,
        S_ECE4B128EA,
        S_221569E2F4,
        S_591250DCC4,
        S_197325F409,
        S_D58FA73ADC,
        S_625FE3A123,
        S_1990A92813,
        S_4DD32031A6,
        S_901EA3E656,
        S_1924279772,
        S_3FC572DD08,
        S_368A5F6FF8,
        S_A842629AFD,
        S_FD44645376,
        S_ECA779BF1F,
        S_7BEEFFF609,
        S_3EA27A4D42,
        S_7627935CE4,
        S_DFC3916173,
        S_2DFF081BB8,
        S_C212B6F4D6,
        S_472939E979,
        S_169487DB3A,
        S_3DD968413B,
        S_888650180C,
        S_F35D9439E1,
        S_8788735EBF,
        S_C13513CF50,
        S_959F80BB16,
        S_B2415CBA15,
        S_30295DABD1,
        S_BFE3A374EA,
        S_C686D05434,
        S_0E403240CA,
        S_B2A0B8369E,
        S_A4D25C80A2,
        S_9AA18E5071,
        S_14244CA6EE,
        S_AFE1C1AE28,
        S_D7AE11A0C8,
        S_B38CAAEF34,
        S_474B2D7F53,
        S_B9F40373E5,
        S_EF2ACA14AE,
        S_F83EB3F89C,
        S_06CF3E90DE,
        S_B53B4895CF,
        S_42595D2C2A,
        S_31D5864AE8,
        S_744C6C3F65,
        S_99BC913EA8,
        S_22EF795E23,
        S_C0E53C8835,
        S_981DA7E6CC,
        S_E17B206052,
        S_FF78287D6C,
        S_1EEA906E1C,
        S_A1DB289D94,
        S_D9E41E686E,
        S_1CC0D0F805,
        S_F65B22617D,
        S_F4DE297D31,
        S_38B7597664,
        S_1F1712ACFF,
        S_5AB5BF64DD,
        S_6B7BBE748F,
        S_1A7993E57F,
        S_A7F94475AE,
        S_654B500353,
        S_93A131140A,
        S_AF6F2E389D,
        S_BFCD361E58,
        S_C23A1CAF1F,
        S_928236D584,
        S_5045C9D518,
        S_E4DDAD5737,
        S_CB5A392498,
        S_913A74B987,
        S_DBCFE0B704,
        S_A87004CBE7,
        S_72FF6EAABE,
        S_AB8D20CEA1,
        S_F4C1EA1E89,
        S_4A0717C830,
        S_7072EC6F49,
        S_487C122B9A,
        S_75676A3A96,
        S_E3C0F294CB,
        S_5000089835,
        S_38CF88C517,
        S_F80BBD09E2,
        S_A174F4502E,
        S_7518581DDB,
        S_EC2B4248A3,
        S_14E3B0AF5D,
        S_CF7DC571E7,
        S_032E3F1F2B,
        S_B80EE3CF4F,
        S_C90D66734C,
        S_F3F57B591C,
        S_6B1F41FC4A,
        S_371F34313C,
        S_CAFDC61BBF,
        S_29708E4B21,
        S_3EECA43C83,
        S_43E88C0B9C,
        S_C7E54FE959,
        S_2B0B630E57,
        S_A9E737C08B,
        S_9C8523BB36,
        S_B08EB69621,
        S_8472C11F5C,
        S_AD01270B3C,
        S_DAFEF2D567,
        S_158191141D,
        S_636899E260,
        S_2BF319F7E7,
        S_EAD3664297,
        S_8FB0968760,
        S_DDA4595886,
        S_C04D541DFC,
        S_194738C8E4,
        S_C6D77220D1,
        S_6C901520EF,
        S_BBCCA2F5AB,
        S_98CFB12354,
        S_A45EA58EBC,
        S_077AE5CBAB,
        S_BC9CE179E3,
        S_21E6F82F77,
        S_6F764343C8,
        S_246D8BEA50,
        S_19B2D19BC1,
        S_B6BFF83C31,
        S_4259DD769C,
        S_3992241AD7,
        S_8277D3ABFB,
        S_7A2ACD7CB6,
        S_475D2D45E2,
        S_95B47268F5,
        S_58B5B8E6E2,
        S_57A2B6698C,
        S_CC6A623F14,
        S_0BF12D107C,
        S_D3901EE454,
        S_9940B21F7F,
        S_B9A391DF6A,
        S_F1BC97987B,
        S_194D16A649,
        S_6812661FAF,
        S_C6D6E215B1,
        S_BF14E6715D,
        S_E6E23C36DB,
        S_208485813F,
        S_438784C35E,
        S_3961E081B0,
        S_30B3504918,
        S_2258169007,
        S_AF51A96858,
        S_9EC6283754,
        S_4F15973D8F,
        S_D56C3E9F15,
        S_63314575EA,
        S_1217D649AD,
        S_AE10B3B6C2,
        S_A7ABEA5BBB,
        S_3A1198BB6C,
        S_D1327CFCA3,
        S_1C0421EDD5,
        S_88EFDCC59F,
        S_F8BE692B65,
        S_6102409B7F,
        S_54BF0673A8,
        S_DA89D1A923,
        S_DADF270A7B,
        S_50AFF62D3A,
        S_80A04709E3,
        S_AD31679FA5,
        S_7EB7AC14F9,
        S_28B8268B1E,
        S_1F899001DE,
        S_738A3F61E4,
        S_468266D639,
        S_B6D9331463,
        S_CF2C285BC5,
        S_BBF831ADE8,
        S_D8412481BB,
        S_0EC18AC7A9,
        S_2B04A81A98,
        S_8BCBE22C40,
        S_DD94B8A1D7,
        S_AFB475EE1C,
        S_55C0FB1865,
        S_2FAB2DACE0,
        S_CE49DECB68,
        S_8F62514DB8,
        S_DF988D7025,
        S_887F182653,

        ;

        val korean: String get() = SystemMessage.message("ko", this)
    }

    private val catalog by lazy {
        val stream = requireNotNull(SystemMessage::class.java.classLoader.getResourceAsStream("i18n/system-messages.json")) {
            "Missing system message catalog"
        }
        stream.use { com.badlogic.gdx.utils.JsonReader().parse(it.reader(Charsets.UTF_8).readText()) }
    }

    private fun message(locale: String, key: Key): String =
        catalog.get(locale)?.getString(key.name, null)
            ?: error("Missing $locale message for ${key.name}")


    fun text(key: Key): String = when (language) {
        Language.KOREAN -> ReviewedKoreanText.text(key.korean)
        Language.ENGLISH -> catalog.get("en")?.getString(key.name, null) ?: key.korean
    }

    fun format(key: Key, vararg arguments: Any?): String =
        MessageFormat(text(key), Locale.ROOT).format(arguments)

    val S_3CFBA6C9EC: String get() = text(Key.S_3CFBA6C9EC)
    val S_C51A025896: String get() = text(Key.S_C51A025896)
    val S_C50A5CA288: String get() = text(Key.S_C50A5CA288)
    val S_39747B892D: String get() = text(Key.S_39747B892D)
    val S_DE036B2104: String get() = text(Key.S_DE036B2104)
    val S_5250AF0F28: String get() = text(Key.S_5250AF0F28)
    val S_9775E6B8B8: String get() = text(Key.S_9775E6B8B8)
    val S_DF70B8A5DA: String get() = text(Key.S_DF70B8A5DA)
    val S_5FC06645C6: String get() = text(Key.S_5FC06645C6)
    val S_6D37F45C92: String get() = text(Key.S_6D37F45C92)
    val S_CEBD2DE9D2: String get() = text(Key.S_CEBD2DE9D2)
    val S_DBCA581BF5: String get() = text(Key.S_DBCA581BF5)
    val S_514FD95472: String get() = text(Key.S_514FD95472)
    val S_8828CC1AE7: String get() = text(Key.S_8828CC1AE7)
    val S_A68C406265: String get() = text(Key.S_A68C406265)
    val S_606322672A: String get() = text(Key.S_606322672A)
    val S_1FC67EAEB1: String get() = text(Key.S_1FC67EAEB1)
    val S_AB9FC6A986: String get() = text(Key.S_AB9FC6A986)
    val S_C66E599032: String get() = text(Key.S_C66E599032)
    val S_AC5814F977: String get() = text(Key.S_AC5814F977)
    val S_42FA751364: String get() = text(Key.S_42FA751364)
    val S_51F600E95C: String get() = text(Key.S_51F600E95C)
    val S_6C9CBA423C: String get() = text(Key.S_6C9CBA423C)
    val S_360FBBD2E1: String get() = text(Key.S_360FBBD2E1)
    val S_F5CE653B43: String get() = text(Key.S_F5CE653B43)
    val S_79B24282A6: String get() = text(Key.S_79B24282A6)
    val S_37E8F20B08: String get() = text(Key.S_37E8F20B08)
    val S_1DF67FC361: String get() = text(Key.S_1DF67FC361)
    val S_C61005675A: String get() = text(Key.S_C61005675A)
    val S_AF99A26F2C: String get() = text(Key.S_AF99A26F2C)
    val S_A32E895914: String get() = text(Key.S_A32E895914)
    val S_F1A4254DC6: String get() = text(Key.S_F1A4254DC6)
    val S_1CB107B8F7: String get() = text(Key.S_1CB107B8F7)
    val S_BE182D9FB6: String get() = text(Key.S_BE182D9FB6)
    val S_FC6E68F342: String get() = text(Key.S_FC6E68F342)
    val S_A455E6907B: String get() = text(Key.S_A455E6907B)
    val S_62D82D76E7: String get() = text(Key.S_62D82D76E7)
    val S_BEF87719AB: String get() = text(Key.S_BEF87719AB)
    val S_293960AB27: String get() = text(Key.S_293960AB27)
    val S_04DE7DBEE2: String get() = text(Key.S_04DE7DBEE2)
    val S_EF7B15F963: String get() = text(Key.S_EF7B15F963)
    val S_DA1FF45B2C: String get() = text(Key.S_DA1FF45B2C)
    val S_F12C80F80B: String get() = text(Key.S_F12C80F80B)
    val S_0C5EE2A0B8: String get() = text(Key.S_0C5EE2A0B8)
    val S_E134585B3A: String get() = text(Key.S_E134585B3A)
    val S_682E465A4D: String get() = text(Key.S_682E465A4D)
    val S_2A9C189A93: String get() = text(Key.S_2A9C189A93)
    val S_14E9E7E054: String get() = text(Key.S_14E9E7E054)
    val S_657B5FEB42: String get() = text(Key.S_657B5FEB42)
    val S_D26EB7B9F4: String get() = text(Key.S_D26EB7B9F4)
    val S_50B8DF8C55: String get() = text(Key.S_50B8DF8C55)
    val S_F9225E4FF8: String get() = text(Key.S_F9225E4FF8)
    val S_47973E953D: String get() = text(Key.S_47973E953D)
    val S_AB4EAC0279: String get() = text(Key.S_AB4EAC0279)
    val S_D7F1F76848: String get() = text(Key.S_D7F1F76848)
    val S_FD293C8169: String get() = text(Key.S_FD293C8169)
    val S_E8B701A283: String get() = text(Key.S_E8B701A283)
    val S_45EDDC7A28: String get() = text(Key.S_45EDDC7A28)
    val S_620D5989B0: String get() = text(Key.S_620D5989B0)
    val S_EB5475E97C: String get() = text(Key.S_EB5475E97C)
    val S_BE23B82091: String get() = text(Key.S_BE23B82091)
    val S_77286CB72C: String get() = text(Key.S_77286CB72C)
    val S_D358176111: String get() = text(Key.S_D358176111)
    val S_AA5BAA93E1: String get() = text(Key.S_AA5BAA93E1)
    val S_9FC8FD5C0F: String get() = text(Key.S_9FC8FD5C0F)
    val S_3098F076B7: String get() = text(Key.S_3098F076B7)
    val S_D8BC2DB4DC: String get() = text(Key.S_D8BC2DB4DC)
    val S_2412A70276: String get() = text(Key.S_2412A70276)
    val S_682998EE62: String get() = text(Key.S_682998EE62)
    val S_A0B2E8A290: String get() = text(Key.S_A0B2E8A290)
    val S_A8DE9DA622: String get() = text(Key.S_A8DE9DA622)
    val S_0011209CB1: String get() = text(Key.S_0011209CB1)
    val S_B93949DF7E: String get() = text(Key.S_B93949DF7E)
    val S_7F1D8C413D: String get() = text(Key.S_7F1D8C413D)
    val S_2C41F8D606: String get() = text(Key.S_2C41F8D606)
    val S_555C05543A: String get() = text(Key.S_555C05543A)
    val S_87F35DB204: String get() = text(Key.S_87F35DB204)
    val S_21BAD5251F: String get() = text(Key.S_21BAD5251F)
    val S_00132A46FC: String get() = text(Key.S_00132A46FC)
    val S_CA5EEA5B52: String get() = text(Key.S_CA5EEA5B52)
    val S_374A498946: String get() = text(Key.S_374A498946)
    val S_D7A0CB68D6: String get() = text(Key.S_D7A0CB68D6)
    val S_8F2A423094: String get() = text(Key.S_8F2A423094)
    val S_5503080A20: String get() = text(Key.S_5503080A20)
    val S_9E9117D015: String get() = text(Key.S_9E9117D015)
    val S_F9CCB2F99F: String get() = text(Key.S_F9CCB2F99F)
    val S_FBBD1D1816: String get() = text(Key.S_FBBD1D1816)
    val S_B8C06C1007: String get() = text(Key.S_B8C06C1007)
    val S_D4B491F164: String get() = text(Key.S_D4B491F164)
    val S_B3940E2B50: String get() = text(Key.S_B3940E2B50)
    val S_DF72A8753D: String get() = text(Key.S_DF72A8753D)
    val S_C80F942461: String get() = text(Key.S_C80F942461)
    val S_B1622411B7: String get() = text(Key.S_B1622411B7)
    val S_367A0A7181: String get() = text(Key.S_367A0A7181)
    val S_09584A936A: String get() = text(Key.S_09584A936A)
    val S_3424D32FC7: String get() = text(Key.S_3424D32FC7)
    val S_5BB1778DCA: String get() = text(Key.S_5BB1778DCA)
    val S_0CC0A4F490: String get() = text(Key.S_0CC0A4F490)
    val S_7579657A88: String get() = text(Key.S_7579657A88)
    val S_2DD57A2739: String get() = text(Key.S_2DD57A2739)
    val S_9EC75F36CA: String get() = text(Key.S_9EC75F36CA)
    val S_E1864601EE: String get() = text(Key.S_E1864601EE)
    val S_453D0D2DF5: String get() = text(Key.S_453D0D2DF5)
    val S_E10C69D0A1: String get() = text(Key.S_E10C69D0A1)
    val S_9D41799D25: String get() = text(Key.S_9D41799D25)
    val S_39A4B2720C: String get() = text(Key.S_39A4B2720C)
    val S_DD3E1D7F75: String get() = text(Key.S_DD3E1D7F75)
    val S_38CA304494: String get() = text(Key.S_38CA304494)
    val S_93DF8F92C4: String get() = text(Key.S_93DF8F92C4)
    val S_4956384FE9: String get() = text(Key.S_4956384FE9)
    val S_9A7F95E71C: String get() = text(Key.S_9A7F95E71C)
    val S_D1379C024C: String get() = text(Key.S_D1379C024C)
    val S_C88072A92C: String get() = text(Key.S_C88072A92C)
    val S_DC25BE8397: String get() = text(Key.S_DC25BE8397)
    val S_B7A3BFF7B9: String get() = text(Key.S_B7A3BFF7B9)
    val S_66213DD030: String get() = text(Key.S_66213DD030)
    val S_E31BDBE56B: String get() = text(Key.S_E31BDBE56B)
    val S_A9A59D5FE3: String get() = text(Key.S_A9A59D5FE3)
    val S_90213F42FB: String get() = text(Key.S_90213F42FB)
    val S_42D16CB521: String get() = text(Key.S_42D16CB521)
    val S_A7FDC582CF: String get() = text(Key.S_A7FDC582CF)
    val S_9417C6AB3A: String get() = text(Key.S_9417C6AB3A)
    val S_EE52984C60: String get() = text(Key.S_EE52984C60)
    val S_8C4DAC1FCC: String get() = text(Key.S_8C4DAC1FCC)
    val S_2B3DFBC5AD: String get() = text(Key.S_2B3DFBC5AD)
    val S_7B01D0F5A8: String get() = text(Key.S_7B01D0F5A8)
    val S_256C17D85E: String get() = text(Key.S_256C17D85E)
    val S_D8F7F3C37F: String get() = text(Key.S_D8F7F3C37F)
    val S_A6A330AD58: String get() = text(Key.S_A6A330AD58)
    val S_0A8D694790: String get() = text(Key.S_0A8D694790)
    val S_2E4EDF0E35: String get() = text(Key.S_2E4EDF0E35)
    val S_BBEF509C8D: String get() = text(Key.S_BBEF509C8D)
    val S_54F6815FE2: String get() = text(Key.S_54F6815FE2)
    val S_757535E6BD: String get() = text(Key.S_757535E6BD)
    val S_CAA3F84FF8: String get() = text(Key.S_CAA3F84FF8)
    val S_983FFCFEC3: String get() = text(Key.S_983FFCFEC3)
    val S_80440AFFDF: String get() = text(Key.S_80440AFFDF)
    val S_DAA1EDD590: String get() = text(Key.S_DAA1EDD590)
    val S_2A87C14E4A: String get() = text(Key.S_2A87C14E4A)
    val S_44F9A814FE: String get() = text(Key.S_44F9A814FE)
    val S_5C349008C3: String get() = text(Key.S_5C349008C3)
    val S_7DC19BD3AD: String get() = text(Key.S_7DC19BD3AD)
    val S_F209C5D3A3: String get() = text(Key.S_F209C5D3A3)
    val S_57A9A4502B: String get() = text(Key.S_57A9A4502B)
    val S_6DB5F96BD1: String get() = text(Key.S_6DB5F96BD1)
    val S_57E6F11841: String get() = text(Key.S_57E6F11841)
    val S_7F1A273C82: String get() = text(Key.S_7F1A273C82)
    val S_2F7F8DF945: String get() = text(Key.S_2F7F8DF945)
    val S_BD54114CB4: String get() = text(Key.S_BD54114CB4)
    val S_1B421182BF: String get() = text(Key.S_1B421182BF)
    val S_54641773A6: String get() = text(Key.S_54641773A6)
    val S_3830A5432E: String get() = text(Key.S_3830A5432E)
    val S_E7236FD795: String get() = text(Key.S_E7236FD795)
    val S_661525CA4A: String get() = text(Key.S_661525CA4A)
    val S_EC935E3110: String get() = text(Key.S_EC935E3110)
    val S_35FF880D87: String get() = text(Key.S_35FF880D87)
    val S_51378A4614: String get() = text(Key.S_51378A4614)
    val S_BC8AE9E500: String get() = text(Key.S_BC8AE9E500)
    val S_2179DA2CFF: String get() = text(Key.S_2179DA2CFF)
    val S_17F4CAA0C8: String get() = text(Key.S_17F4CAA0C8)
    val S_08E3B7B077: String get() = text(Key.S_08E3B7B077)
    val S_4DF0D9A984: String get() = text(Key.S_4DF0D9A984)
    val S_F0D2E9FDE8: String get() = text(Key.S_F0D2E9FDE8)
    val S_F407A5EA8C: String get() = text(Key.S_F407A5EA8C)
    val S_8767F1CDA5: String get() = text(Key.S_8767F1CDA5)
    val S_3A6CD5555C: String get() = text(Key.S_3A6CD5555C)
    val S_EBE739C92D: String get() = text(Key.S_EBE739C92D)
    val S_F01A5A5229: String get() = text(Key.S_F01A5A5229)
    val S_D3B0E1A367: String get() = text(Key.S_D3B0E1A367)
    val S_5FF079CE3D: String get() = text(Key.S_5FF079CE3D)
    val S_DE1AE880EF: String get() = text(Key.S_DE1AE880EF)
    val S_F8FC3619DB: String get() = text(Key.S_F8FC3619DB)
    val S_C91E618B9A: String get() = text(Key.S_C91E618B9A)
    val S_9A6128E64A: String get() = text(Key.S_9A6128E64A)
    val S_CA6C5A2257: String get() = text(Key.S_CA6C5A2257)
    val S_9C9F1862EA: String get() = text(Key.S_9C9F1862EA)
    val S_B9A3425560: String get() = text(Key.S_B9A3425560)
    val S_2D243A64E7: String get() = text(Key.S_2D243A64E7)
    val S_30373C627C: String get() = text(Key.S_30373C627C)
    val S_18CB232899: String get() = text(Key.S_18CB232899)
    val S_F5798CEF9A: String get() = text(Key.S_F5798CEF9A)
    val S_59463E84FB: String get() = text(Key.S_59463E84FB)
    val S_DF6168DB1C: String get() = text(Key.S_DF6168DB1C)
    val S_9F66DED326: String get() = text(Key.S_9F66DED326)
    val S_2926977BA7: String get() = text(Key.S_2926977BA7)
    val S_EA6F893483: String get() = text(Key.S_EA6F893483)
    val S_1BCE60B07B: String get() = text(Key.S_1BCE60B07B)
    val S_9CA0190DEC: String get() = text(Key.S_9CA0190DEC)
    val S_09C5A32367: String get() = text(Key.S_09C5A32367)
    val S_4A021545F6: String get() = text(Key.S_4A021545F6)
    val S_DB42F54740: String get() = text(Key.S_DB42F54740)
    val S_FB5F03E2EE: String get() = text(Key.S_FB5F03E2EE)
    val S_841964364C: String get() = text(Key.S_841964364C)
    val S_8DD48EC033: String get() = text(Key.S_8DD48EC033)
    val S_E6B2B4DBC0: String get() = text(Key.S_E6B2B4DBC0)
    val S_F0EFEB11FB: String get() = text(Key.S_F0EFEB11FB)
    val S_85DE958B5F: String get() = text(Key.S_85DE958B5F)
    val S_991BD0CC20: String get() = text(Key.S_991BD0CC20)
    val S_E1407B5115: String get() = text(Key.S_E1407B5115)
    val S_7CC8A7A989: String get() = text(Key.S_7CC8A7A989)
    val S_0F14132CB0: String get() = text(Key.S_0F14132CB0)
    val S_01DDCA043B: String get() = text(Key.S_01DDCA043B)
    val S_F089BA034E: String get() = text(Key.S_F089BA034E)
    val S_CBDCD710AB: String get() = text(Key.S_CBDCD710AB)
    val S_90E5E4D22F: String get() = text(Key.S_90E5E4D22F)
    val S_756B2E906A: String get() = text(Key.S_756B2E906A)
    val S_5C77F2F6FC: String get() = text(Key.S_5C77F2F6FC)
    val S_5A268D48CF: String get() = text(Key.S_5A268D48CF)
    val S_8451709DFE: String get() = text(Key.S_8451709DFE)
    val S_BDA0208A5C: String get() = text(Key.S_BDA0208A5C)
    val S_7210A3FD77: String get() = text(Key.S_7210A3FD77)
    val S_0DFFF1CFEB: String get() = text(Key.S_0DFFF1CFEB)
    val S_3843E8E488: String get() = text(Key.S_3843E8E488)
    val S_2F82A7D82B: String get() = text(Key.S_2F82A7D82B)
    val S_17BDB458F6: String get() = text(Key.S_17BDB458F6)
    val S_6EEBC7BA34: String get() = text(Key.S_6EEBC7BA34)
    val S_A889CDE68C: String get() = text(Key.S_A889CDE68C)
    val S_C5D202B13B: String get() = text(Key.S_C5D202B13B)
    val S_BDBDED5992: String get() = text(Key.S_BDBDED5992)
    val S_CFEF357D40: String get() = text(Key.S_CFEF357D40)
    val S_B62250FE8D: String get() = text(Key.S_B62250FE8D)
    val S_75C20D3862: String get() = text(Key.S_75C20D3862)
    val S_8F3BC8881B: String get() = text(Key.S_8F3BC8881B)
    val S_A7202337A7: String get() = text(Key.S_A7202337A7)
    val S_93EEFD6430: String get() = text(Key.S_93EEFD6430)
    val S_CEF693F5A0: String get() = text(Key.S_CEF693F5A0)
    val S_388DB7E1C8: String get() = text(Key.S_388DB7E1C8)
    val S_A3E8A0BBCD: String get() = text(Key.S_A3E8A0BBCD)
    val S_8916B6394A: String get() = text(Key.S_8916B6394A)
    val S_273718B1D7: String get() = text(Key.S_273718B1D7)
    val S_ECE4B128EA: String get() = text(Key.S_ECE4B128EA)
    val S_221569E2F4: String get() = text(Key.S_221569E2F4)
    val S_591250DCC4: String get() = text(Key.S_591250DCC4)
    val S_197325F409: String get() = text(Key.S_197325F409)
    val S_D58FA73ADC: String get() = text(Key.S_D58FA73ADC)
    val S_625FE3A123: String get() = text(Key.S_625FE3A123)
    val S_1990A92813: String get() = text(Key.S_1990A92813)
    val S_4DD32031A6: String get() = text(Key.S_4DD32031A6)
    val S_901EA3E656: String get() = text(Key.S_901EA3E656)
    val S_1924279772: String get() = text(Key.S_1924279772)
    val S_3FC572DD08: String get() = text(Key.S_3FC572DD08)
    val S_368A5F6FF8: String get() = text(Key.S_368A5F6FF8)
    val S_A842629AFD: String get() = text(Key.S_A842629AFD)
    val S_FD44645376: String get() = text(Key.S_FD44645376)
    val S_ECA779BF1F: String get() = text(Key.S_ECA779BF1F)
    val S_7BEEFFF609: String get() = text(Key.S_7BEEFFF609)
    val S_3EA27A4D42: String get() = text(Key.S_3EA27A4D42)
    val S_7627935CE4: String get() = text(Key.S_7627935CE4)
    val S_DFC3916173: String get() = text(Key.S_DFC3916173)
    val S_2DFF081BB8: String get() = text(Key.S_2DFF081BB8)
    val S_C212B6F4D6: String get() = text(Key.S_C212B6F4D6)
    val S_472939E979: String get() = text(Key.S_472939E979)
    val S_169487DB3A: String get() = text(Key.S_169487DB3A)
    val S_3DD968413B: String get() = text(Key.S_3DD968413B)
    val S_888650180C: String get() = text(Key.S_888650180C)
    val S_F35D9439E1: String get() = text(Key.S_F35D9439E1)
    val S_8788735EBF: String get() = text(Key.S_8788735EBF)
    val S_C13513CF50: String get() = text(Key.S_C13513CF50)
    val S_959F80BB16: String get() = text(Key.S_959F80BB16)
    val S_B2415CBA15: String get() = text(Key.S_B2415CBA15)
    val S_30295DABD1: String get() = text(Key.S_30295DABD1)
    val S_BFE3A374EA: String get() = text(Key.S_BFE3A374EA)
    val S_C686D05434: String get() = text(Key.S_C686D05434)
    val S_0E403240CA: String get() = text(Key.S_0E403240CA)
    val S_B2A0B8369E: String get() = text(Key.S_B2A0B8369E)
    val S_A4D25C80A2: String get() = text(Key.S_A4D25C80A2)
    val S_9AA18E5071: String get() = text(Key.S_9AA18E5071)
    val S_14244CA6EE: String get() = text(Key.S_14244CA6EE)
    val S_AFE1C1AE28: String get() = text(Key.S_AFE1C1AE28)
    val S_D7AE11A0C8: String get() = text(Key.S_D7AE11A0C8)
    val S_B38CAAEF34: String get() = text(Key.S_B38CAAEF34)
    val S_474B2D7F53: String get() = text(Key.S_474B2D7F53)
    val S_B9F40373E5: String get() = text(Key.S_B9F40373E5)
    val S_EF2ACA14AE: String get() = text(Key.S_EF2ACA14AE)
    val S_F83EB3F89C: String get() = text(Key.S_F83EB3F89C)
    val S_06CF3E90DE: String get() = text(Key.S_06CF3E90DE)
    val S_B53B4895CF: String get() = text(Key.S_B53B4895CF)
    val S_42595D2C2A: String get() = text(Key.S_42595D2C2A)
    val S_31D5864AE8: String get() = text(Key.S_31D5864AE8)
    val S_744C6C3F65: String get() = text(Key.S_744C6C3F65)
    val S_99BC913EA8: String get() = text(Key.S_99BC913EA8)
    val S_22EF795E23: String get() = text(Key.S_22EF795E23)
    val S_C0E53C8835: String get() = text(Key.S_C0E53C8835)
    val S_981DA7E6CC: String get() = text(Key.S_981DA7E6CC)
    val S_E17B206052: String get() = text(Key.S_E17B206052)
    val S_FF78287D6C: String get() = text(Key.S_FF78287D6C)
    val S_1EEA906E1C: String get() = text(Key.S_1EEA906E1C)
    val S_A1DB289D94: String get() = text(Key.S_A1DB289D94)
    val S_D9E41E686E: String get() = text(Key.S_D9E41E686E)
    val S_1CC0D0F805: String get() = text(Key.S_1CC0D0F805)
    val S_F65B22617D: String get() = text(Key.S_F65B22617D)
    val S_F4DE297D31: String get() = text(Key.S_F4DE297D31)
    val S_38B7597664: String get() = text(Key.S_38B7597664)
    val S_1F1712ACFF: String get() = text(Key.S_1F1712ACFF)
    val S_5AB5BF64DD: String get() = text(Key.S_5AB5BF64DD)
    val S_6B7BBE748F: String get() = text(Key.S_6B7BBE748F)
    val S_1A7993E57F: String get() = text(Key.S_1A7993E57F)
    val S_A7F94475AE: String get() = text(Key.S_A7F94475AE)
    val S_654B500353: String get() = text(Key.S_654B500353)
    val S_93A131140A: String get() = text(Key.S_93A131140A)
    val S_AF6F2E389D: String get() = text(Key.S_AF6F2E389D)
    val S_BFCD361E58: String get() = text(Key.S_BFCD361E58)
    val S_C23A1CAF1F: String get() = text(Key.S_C23A1CAF1F)
    val S_928236D584: String get() = text(Key.S_928236D584)
    val S_5045C9D518: String get() = text(Key.S_5045C9D518)
    val S_E4DDAD5737: String get() = text(Key.S_E4DDAD5737)
    val S_CB5A392498: String get() = text(Key.S_CB5A392498)
    val S_913A74B987: String get() = text(Key.S_913A74B987)
    val S_DBCFE0B704: String get() = text(Key.S_DBCFE0B704)
    val S_A87004CBE7: String get() = text(Key.S_A87004CBE7)
    val S_72FF6EAABE: String get() = text(Key.S_72FF6EAABE)
    val S_AB8D20CEA1: String get() = text(Key.S_AB8D20CEA1)
    val S_F4C1EA1E89: String get() = text(Key.S_F4C1EA1E89)
    val S_4A0717C830: String get() = text(Key.S_4A0717C830)
    val S_7072EC6F49: String get() = text(Key.S_7072EC6F49)
    val S_487C122B9A: String get() = text(Key.S_487C122B9A)
    val S_75676A3A96: String get() = text(Key.S_75676A3A96)
    val S_E3C0F294CB: String get() = text(Key.S_E3C0F294CB)
    val S_5000089835: String get() = text(Key.S_5000089835)
    val S_38CF88C517: String get() = text(Key.S_38CF88C517)
    val S_F80BBD09E2: String get() = text(Key.S_F80BBD09E2)
    val S_A174F4502E: String get() = text(Key.S_A174F4502E)
    val S_7518581DDB: String get() = text(Key.S_7518581DDB)
    val S_EC2B4248A3: String get() = text(Key.S_EC2B4248A3)
    val S_14E3B0AF5D: String get() = text(Key.S_14E3B0AF5D)
    val S_CF7DC571E7: String get() = text(Key.S_CF7DC571E7)
    val S_032E3F1F2B: String get() = text(Key.S_032E3F1F2B)
    val S_B80EE3CF4F: String get() = text(Key.S_B80EE3CF4F)
    val S_C90D66734C: String get() = text(Key.S_C90D66734C)
    val S_F3F57B591C: String get() = text(Key.S_F3F57B591C)
    val S_6B1F41FC4A: String get() = text(Key.S_6B1F41FC4A)
    val S_371F34313C: String get() = text(Key.S_371F34313C)
    val S_CAFDC61BBF: String get() = text(Key.S_CAFDC61BBF)
    val S_29708E4B21: String get() = text(Key.S_29708E4B21)
    val S_3EECA43C83: String get() = text(Key.S_3EECA43C83)
    val S_43E88C0B9C: String get() = text(Key.S_43E88C0B9C)
    val S_C7E54FE959: String get() = text(Key.S_C7E54FE959)
    val S_2B0B630E57: String get() = text(Key.S_2B0B630E57)
    val S_A9E737C08B: String get() = text(Key.S_A9E737C08B)
    val S_9C8523BB36: String get() = text(Key.S_9C8523BB36)
    val S_B08EB69621: String get() = text(Key.S_B08EB69621)
    val S_8472C11F5C: String get() = text(Key.S_8472C11F5C)
    val S_AD01270B3C: String get() = text(Key.S_AD01270B3C)
    val S_DAFEF2D567: String get() = text(Key.S_DAFEF2D567)
    val S_158191141D: String get() = text(Key.S_158191141D)
    val S_636899E260: String get() = text(Key.S_636899E260)
    val S_2BF319F7E7: String get() = text(Key.S_2BF319F7E7)
    val S_EAD3664297: String get() = text(Key.S_EAD3664297)
    val S_8FB0968760: String get() = text(Key.S_8FB0968760)
    val S_DDA4595886: String get() = text(Key.S_DDA4595886)
    val S_C04D541DFC: String get() = text(Key.S_C04D541DFC)
    val S_194738C8E4: String get() = text(Key.S_194738C8E4)
    val S_C6D77220D1: String get() = text(Key.S_C6D77220D1)
    val S_6C901520EF: String get() = text(Key.S_6C901520EF)
    val S_BBCCA2F5AB: String get() = text(Key.S_BBCCA2F5AB)
    val S_98CFB12354: String get() = text(Key.S_98CFB12354)
    val S_A45EA58EBC: String get() = text(Key.S_A45EA58EBC)
    val S_077AE5CBAB: String get() = text(Key.S_077AE5CBAB)
    val S_BC9CE179E3: String get() = text(Key.S_BC9CE179E3)
    val S_21E6F82F77: String get() = text(Key.S_21E6F82F77)
    val S_6F764343C8: String get() = text(Key.S_6F764343C8)
    val S_246D8BEA50: String get() = text(Key.S_246D8BEA50)
    val S_19B2D19BC1: String get() = text(Key.S_19B2D19BC1)
    val S_B6BFF83C31: String get() = text(Key.S_B6BFF83C31)
    val S_4259DD769C: String get() = text(Key.S_4259DD769C)
    val S_3992241AD7: String get() = text(Key.S_3992241AD7)
    val S_8277D3ABFB: String get() = text(Key.S_8277D3ABFB)
    val S_7A2ACD7CB6: String get() = text(Key.S_7A2ACD7CB6)
    val S_475D2D45E2: String get() = text(Key.S_475D2D45E2)
    val S_95B47268F5: String get() = text(Key.S_95B47268F5)
    val S_58B5B8E6E2: String get() = text(Key.S_58B5B8E6E2)
    val S_57A2B6698C: String get() = text(Key.S_57A2B6698C)
    val S_CC6A623F14: String get() = text(Key.S_CC6A623F14)
    val S_0BF12D107C: String get() = text(Key.S_0BF12D107C)
    val S_D3901EE454: String get() = text(Key.S_D3901EE454)
    val S_9940B21F7F: String get() = text(Key.S_9940B21F7F)
    val S_B9A391DF6A: String get() = text(Key.S_B9A391DF6A)
    val S_F1BC97987B: String get() = text(Key.S_F1BC97987B)
    val S_194D16A649: String get() = text(Key.S_194D16A649)
    val S_6812661FAF: String get() = text(Key.S_6812661FAF)
    val S_C6D6E215B1: String get() = text(Key.S_C6D6E215B1)
    val S_BF14E6715D: String get() = text(Key.S_BF14E6715D)
    val S_E6E23C36DB: String get() = text(Key.S_E6E23C36DB)
    val S_208485813F: String get() = text(Key.S_208485813F)
    val S_438784C35E: String get() = text(Key.S_438784C35E)
    val S_3961E081B0: String get() = text(Key.S_3961E081B0)
    val S_30B3504918: String get() = text(Key.S_30B3504918)
    val S_2258169007: String get() = text(Key.S_2258169007)
    val S_AF51A96858: String get() = text(Key.S_AF51A96858)
    val S_9EC6283754: String get() = text(Key.S_9EC6283754)
    val S_4F15973D8F: String get() = text(Key.S_4F15973D8F)
    val S_D56C3E9F15: String get() = text(Key.S_D56C3E9F15)
    val S_63314575EA: String get() = text(Key.S_63314575EA)
    val S_1217D649AD: String get() = text(Key.S_1217D649AD)
    val S_AE10B3B6C2: String get() = text(Key.S_AE10B3B6C2)
    val S_A7ABEA5BBB: String get() = text(Key.S_A7ABEA5BBB)
    val S_3A1198BB6C: String get() = text(Key.S_3A1198BB6C)
    val S_D1327CFCA3: String get() = text(Key.S_D1327CFCA3)
    val S_1C0421EDD5: String get() = text(Key.S_1C0421EDD5)
    val S_88EFDCC59F: String get() = text(Key.S_88EFDCC59F)
    val S_F8BE692B65: String get() = text(Key.S_F8BE692B65)
    val S_6102409B7F: String get() = text(Key.S_6102409B7F)
    val S_54BF0673A8: String get() = text(Key.S_54BF0673A8)
    val S_DA89D1A923: String get() = text(Key.S_DA89D1A923)
    val S_DADF270A7B: String get() = text(Key.S_DADF270A7B)
    val S_50AFF62D3A: String get() = text(Key.S_50AFF62D3A)
    val S_80A04709E3: String get() = text(Key.S_80A04709E3)
    val S_AD31679FA5: String get() = text(Key.S_AD31679FA5)
    val S_7EB7AC14F9: String get() = text(Key.S_7EB7AC14F9)
    val S_28B8268B1E: String get() = text(Key.S_28B8268B1E)
    val S_1F899001DE: String get() = text(Key.S_1F899001DE)
    val S_738A3F61E4: String get() = text(Key.S_738A3F61E4)
    val S_468266D639: String get() = text(Key.S_468266D639)
    val S_B6D9331463: String get() = text(Key.S_B6D9331463)
    val S_CF2C285BC5: String get() = text(Key.S_CF2C285BC5)
    val S_BBF831ADE8: String get() = text(Key.S_BBF831ADE8)
    val S_D8412481BB: String get() = text(Key.S_D8412481BB)
    val S_0EC18AC7A9: String get() = text(Key.S_0EC18AC7A9)
    val S_2B04A81A98: String get() = text(Key.S_2B04A81A98)
    val S_8BCBE22C40: String get() = text(Key.S_8BCBE22C40)
    val S_DD94B8A1D7: String get() = text(Key.S_DD94B8A1D7)
    val S_AFB475EE1C: String get() = text(Key.S_AFB475EE1C)
    val S_55C0FB1865: String get() = text(Key.S_55C0FB1865)
    val S_2FAB2DACE0: String get() = text(Key.S_2FAB2DACE0)
    val S_CE49DECB68: String get() = text(Key.S_CE49DECB68)
    val S_8F62514DB8: String get() = text(Key.S_8F62514DB8)
    val S_DF988D7025: String get() = text(Key.S_DF988D7025)
    val S_887F182653: String get() = text(Key.S_887F182653)
}
