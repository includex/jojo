package com.jojo.game.presentation.i18n

import java.text.MessageFormat
import java.util.Locale

/** Central catalog for statically authored UI and presentation strings. */
object GameText {
    enum class Language { KOREAN, ENGLISH }

    @Volatile
    var language: Language = Language.KOREAN

    enum class Key(val korean: String) {
        ITEM_PURCHASE("{0} 구매"),
        ITEM_SALE("{0} 판매"),
        ITEM_DISCARDED("{0} 이미 버렸습니다..."),
        UNIT_FALLBACK("유닛 {0}"),
        STAT_BONUS("{0} +{1}\n{2}"),
        CHAPTER_PREFIX("제{0}"),
        TREASURE_PROGRESS("지금까지 발견한 보물 {0} / {1}"),
        SELL_PRICE("판매가: {0}"),
        INVENTORY_COUNT("인벤토리: {0}"),
        PRICE("가격: {0}"),
        TROOP_COUNT("출진 무장 - {0}/{1}"),
        SELECTION_TOTAL("선택했습니다{0}항, 총{1}원"),
        EVENT_WEATHER("날씨: {0}"),
        BATTLE_ROUND("라운드 {0} · {1} 차례 · {2}"),
        AI_TURN_SUMMARY("{0}: 이동 {1} · 공격 {2} · 대기 {3}"),
        AI_UNIT_ACTION("{0}: {1} 행동"),
        UNIT_SELECTED("{0} 선택"),
        UNIT_SELECTED_MOVE("{0} 선택 · 빈 칸으로 이동, 인접 적을 공격"),
        UNIT_COMMAND("{0} 명령 선택"),
        UNIT_MOVE_DONE("{0} 이동 완료"),
        UNIT_WAITING("{0} 대기"),
        UNIT_IDLE("미행동 부대: {0}"),
        MAGIC_TARGET("{0} 선택 · 범위 내 대상을 클릭"),
        ITEM_TARGET("{0} 선택 ({1}개) · 자신 또는 인접 아군을 선택"),
        TACTICAL_MOVE("{0} 이동 완료"),
        TACTICAL_ATTACK("{0} 공격 · {1} 피해{2}{3}{4}"),
        TACTICAL_MAGIC("{0} {1} · MP {2} · {3}{4}"),
        COMBAT_DAMAGE("HP {0} 회복"),
        MAGIC_RECOVERY("MP {0} 회복"),
        STATUS_CURED("{0} 치료"),
        ATTRIBUTE_INCREASE("{0} 상승"),
        HP_INCREASE("최대 HP {0} 증가"),
        MP_INCREASE("최대 MP {0} 증가"),
        SAVE_CONFIRM("진행도 No.{0}:{1}저장할 수 있나요?"),
        LOAD_CONFIRM("진행도 No.{0}:{1}불러올 수 있나요?"),
        ITEM_DROP_CONFIRM("버릴 것을 결정하시겠습니까?{0}?"),
        RESOURCE_LOADING("자원 로딩 중{0}%"),
        SAVE_EQUIPMENT_NOTICE("장비 {0}개를 모두 해제했습니다."),
        ABILITY_LEVEL("{0} 상승"),
        PROMOTION_LEVEL("{0} 승격하여{1}레벨"),
        MAGIC_LEARNED("법술 「{0}」！"),
        STAGE_NAME("전역{0}"),
        VERIFICATION_UNIT_MISSING("{0} 원본 전투 유닛이 없습니다."),
        VERIFICATION_MAP_MISSING("{0} 원본 HM 전장 이미지를 찾을 수 없습니다: {1}"),
        VERIFICATION_UNIT_COUNT("{0} 전투 유닛 변환 수가 일치하지 않습니다."),
        VERIFICATION_PLAYER_UNIT("{0} 플레이어 유닛 변환에 실패했습니다."),
        VERIFICATION_ENEMY_UNIT("{0} 적군이 없습니다."),
        VERIFICATION_SPRITE_MISSING("{0} 원본 유닛 스프라이트를 찾을 수 없습니다."),
        /** Korean source: \n대화\n */
        S_3CFBA6C9EC("\n대화\n"),
        /** Korean source: \n아이템\n */
        S_C51A025896("\n아이템\n"),
        /** Korean source: \n일대일 대결\n */
        S_C50A5CA288("\n일대일 대결\n"),
        /** Korean source:  ▪ 훈련 */
        S_39747B892D(" ▪ 훈련"),
        /** Korean source: 6 [단축키 설명]\n☆ 일부 단축키 기능은 메뉴 — 설정을 통해 직접 설정할 수 있습니다.\n */
        S_DE036B2104("6 [단축키 설명]\n☆ 일부 단축키 기능은 메뉴 — 설정을 통해 직접 설정할 수 있습니다.\n"),
        /** Korean source: HP 회복 */
        S_5250AF0F28("HP 회복"),
        /** Korean source: HP가 40 미만이면 사용할 수 없는 전략입니다. */
        S_9775E6B8B8("HP가 40 미만이면 사용할 수 없는 전략입니다."),
        /** Korean source: Hall unit readiness는 texture 완료 콜백으로만 재개해야 합니다. */
        S_DF70B8A5DA("Hall unit readiness는 texture 완료 콜백으로만 재개해야 합니다."),
        /** Korean source: MP 소모: */
        S_5FC06645C6("MP 소모:"),
        /** Korean source: MP가 부족합니다. */
        S_6D37F45C92("MP가 부족합니다."),
        /** Korean source: SKIP_OPEN 오버레이인데 SkipLayer 흐름이 없습니다. */
        S_CEBD2DE9D2("SKIP_OPEN 오버레이인데 SkipLayer 흐름이 없습니다."),
        /** Korean source: loadBg 완료 콜백은 Script pause 중에만 가능합니다. */
        S_DBCA581BF5("loadBg 완료 콜백은 Script pause 중에만 가능합니다."),
        /** Korean source: loadBg는 BattleScreen의 맵/아바타 완료 콜백으로만 재개해야 합니다. */
        S_514FD95472("loadBg는 BattleScreen의 맵/아바타 완료 콜백으로만 재개해야 합니다."),
        /** Korean source: stage.unit(id) 호출이 필요합니다. */
        S_8828CC1AE7("stage.unit(id) 호출이 필요합니다."),
        /** Korean source: {"txt":"시작 화면으로 돌아가시겠습니까?"} */
        S_A68C406265("{\"txt\":\"시작 화면으로 돌아가시겠습니까?\"}"),
        /** Korean source: ↑↓ 선택 · Enter / 클릭 확정 */
        S_606322672A("↑↓ 선택 · Enter / 클릭 확정"),
        /** Korean source: ○ 아군 */
        S_1FC67EAEB1("○ 아군"),
        /** Korean source: ○ 적군 */
        S_AB9FC6A986("○ 적군"),
        /** Korean source: ● 아군 */
        S_C66E599032("● 아군"),
        /** Korean source: ● 적군 */
        S_AC5814F977("● 적군"),
        /** Korean source: ☆ 문자 A: 턴 시작 시 자동으로 저장됩니다.\n */
        S_42FA751364("☆ 문자 A: 턴 시작 시 자동으로 저장됩니다.\n"),
        /** Korean source: ☆ 문자 B: 속성 인터페이스는 모든 가능한 전략과 학습 수준을 표시합니다. */
        S_51F600E95C("☆ 문자 B: 속성 인터페이스는 모든 가능한 전략과 학습 수준을 표시합니다."),
        /** Korean source: ☆ 번호 0-4: 단계 속도 변화. 0가 원래 속도이며, 1-4가 가속.\n */
        S_6C9CBA423C("☆ 번호 0-4: 단계 속도 변화. 0가 원래 속도이며, 1-4가 가속.\n"),
        /** Korean source: ☆ 번호 5: 진영에 따라 다른 색상의 체력 바를 표시합니다.\n */
        S_360FBBD2E1("☆ 번호 5: 진영에 따라 다른 색상의 체력 바를 표시합니다.\n"),
        /** Korean source: ☆ 번호 6: 문자 BUFF와 DEBUFF를 표시합니다.\n */
        S_F5CE653B43("☆ 번호 6: 문자 BUFF와 DEBUFF를 표시합니다.\n"),
        /** Korean source: ☆ 번호 7: 왼쪽 하단에 캐릭터 능력과 장비를 표시합니다.\n */
        S_79B24282A6("☆ 번호 7: 왼쪽 하단에 캐릭터 능력과 장비를 표시합니다.\n"),
        /** Korean source: ☆ 번호 9: 지형 적응 및 이동 비용을 표시합니다.\n */
        S_37E8F20B08("☆ 번호 9: 지형 적응 및 이동 비용을 표시합니다.\n"),
        /** Korean source: ☆ 숫자 8: 더블 히트의 치명타 확률과 카운터 관계를 표시합니다.\n */
        S_1DF67FC361("☆ 숫자 8: 더블 히트의 치명타 확률과 카운터 관계를 표시합니다.\n"),
        /** Korean source: 가격: */
        S_C61005675A("가격:"),
        /** Korean source: 가능 범위 */
        S_AF99A26F2C("가능 범위"),
        /** Korean source: 감소 */
        S_A32E895914("감소"),
        /** Korean source: 값으로 매길 수 없는 보물이므로 구매할 수 없습니다. */
        S_F1A4254DC6("값으로 매길 수 없는 보물이므로 구매할 수 없습니다."),
        /** Korean source: 강제 공격 대상을 찾을 수 없습니다. */
        S_1CB107B8F7("강제 공격 대상을 찾을 수 없습니다."),
        /** Korean source: 검증 중…… */
        S_BE182D9FB6("검증 중……"),
        /** Korean source: 게임 속도 */
        S_FC6E68F342("게임 속도"),
        /** Korean source: 게임 속도 상한 조정 */
        S_A455E6907B("게임 속도 상한 조정"),
        /** Korean source: 게임 시작 */
        S_62D82D76E7("게임 시작"),
        /** Korean source: 게임 시작 후에 사용해 주세요~ */
        S_BEF87719AB("게임 시작 후에 사용해 주세요~"),
        /** Korean source: 게임 저장하시겠습니까? */
        S_293960AB27("게임 저장하시겠습니까?"),
        /** Korean source: 게임 테스트용으로만 사용됩니다 */
        S_04DE7DBEE2("게임 테스트용으로만 사용됩니다"),
        /** Korean source: 게임에서 클릭하면 받지 못한 다른 보물들을 채울 수 있으며, 같은 보물은 2개까지 지원합니다. */
        S_EF7B15F963("게임에서 클릭하면 받지 못한 다른 보물들을 채울 수 있으며, 같은 보물은 2개까지 지원합니다."),
        /** Korean source: 게임을 재시작하여 활성화 여부를 확인하는 중이니 잠시만 기다려 주세요…… */
        S_DA1FF45B2C("게임을 재시작하여 활성화 여부를 확인하는 중이니 잠시만 기다려 주세요……"),
        /** Korean source: 게임을 처음부터 다시 시작하며, 캐릭터 레벨은 바로 만렙입니다. */
        S_F12C80F80B("게임을 처음부터 다시 시작하며, 캐릭터 레벨은 바로 만렙입니다."),
        /** Korean source: 결정 */
        S_0C5EE2A0B8("결정"),
        /** Korean source: 경험치 */
        S_E134585B3A("경험치"),
        /** Korean source: 곧 활성화 코드가 생성됩니다. 계속하시겠습니까? */
        S_682E465A4D("곧 활성화 코드가 생성됩니다. 계속하시겠습니까?"),
        /** Korean source: 공격 */
        S_2A9C189A93("공격"),
        /** Korean source: 공격 대상을 선택하세요. */
        S_14E9E7E054("공격 대상을 선택하세요."),
        /** Korean source: 공격 범위를 벗어난 적입니다. */
        S_657B5FEB42("공격 범위를 벗어난 적입니다."),
        /** Korean source: 공격 유닛이 없습니다. */
        S_D26EB7B9F4("공격 유닛이 없습니다."),
        /** Korean source: 공격력 */
        S_50B8DF8C55("공격력"),
        /** Korean source: 공훈 */
        S_F9225E4FF8("공훈"),
        /** Korean source: 공훈 모드에서 과일을 먹으면 오위가 상승합니다. */
        S_47973E953D("공훈 모드에서 과일을 먹으면 오위가 상승합니다."),
        /** Korean source: 과일로 오방위 능력치 상승 */
        S_AB4EAC0279("과일로 오방위 능력치 상승"),
        /** Korean source: 교환 */
        S_D7F1F76848("교환"),
        /** Korean source: 교환할 아군을 선택하세요. */
        S_FD293C8169("교환할 아군을 선택하세요."),
        /** Korean source: 구 */
        S_E8B701A283("구"),
        /** Korean source: 구매 수량(1 - %d): */
        S_45EDDC7A28("구매 수량(1 - %d):"),
        /** Korean source: 구매하기 */
        S_620D5989B0("구매하기"),
        /** Korean source: 구매할 수 없는 물품입니다. */
        S_EB5475E97C("구매할 수 없는 물품입니다."),
        /** Korean source: 군웅 */
        S_BE23B82091("군웅"),
        /** Korean source: 군웅        Lv     3 */
        S_77286CB72C("군웅        Lv     3"),
        /** Korean source: 군주 */
        S_D358176111("군주"),
        /** Korean source: 궁기 */
        S_AA5BAA93E1("궁기"),
        /** Korean source: 권한이 부족합니다! */
        S_9FC8FD5C0F("권한이 부족합니다!"),
        /** Korean source: 그리기 계약의 스프라이트가 증거보다 많다 */
        S_3098F076B7("그리기 계약의 스프라이트가 증거보다 많다"),
        /** Korean source: 글자 공개 간격은 0보다 커야 합니다. */
        S_D8BC2DB4DC("글자 공개 간격은 0보다 커야 합니다."),
        /** Korean source: 금전: */
        S_2412A70276("금전:"),
        /** Korean source: 금주 */
        S_682998EE62("금주"),
        /** Korean source: 금화가 부족하여 구매할 수 없습니다 */
        S_A0B2E8A290("금화가 부족하여 구매할 수 없습니다"),
        /** Korean source: 기동력 소모 */
        S_A8DE9DA622("기동력 소모"),
        /** Korean source: 기력 모으기 */
        S_0011209CB1("기력 모으기"),
        /** Korean source: 기병 */
        S_B93949DF7E("기병"),
        /** Korean source: 기본 */
        S_7F1D8C413D("기본"),
        /** Korean source: 기본 능력 */
        S_2C41F8D606("기본 능력"),
        /** Korean source: 기본 능력 무력 지력 지휘 민첩성 운기 무장 소개 인물 특기 일람 없음 출진 횟수 퇴각 ★◎○△×●-- */
        S_555C05543A("기본 능력 무력 지력 지휘 민첩성 운기 무장 소개 인물 특기 일람 없음 출진 횟수 퇴각 ★◎○△×●--"),
        /** Korean source: 길 잃음 */
        S_87F35DB204("길 잃음"),
        /** Korean source: 날씨:  */
        S_21BAD5251F("날씨: "),
        /** Korean source: 내부 테스트 도구에 대해서는, 도움말 설명을 먼저 확인해 보시는 것을 권장합니다. */
        S_00132A46FC("내부 테스트 도구에 대해서는, 도움말 설명을 먼저 확인해 보시는 것을 권장합니다."),
        /** Korean source: 넷 */
        S_CA5EEA5B52("넷"),
        /** Korean source: 눈 */
        S_374A498946("눈"),
        /** Korean source: 느림 */
        S_D7A0CB68D6("느림"),
        /** Korean source: 능력 */
        S_8F2A423094("능력"),
        /** Korean source: 능력 이름 */
        S_5503080A20("능력 이름"),
        /** Korean source: 능력치 */
        S_9E9117D015("능력치"),
        /** Korean source: 다른 유닛이 있는 칸입니다. */
        S_F9CCB2F99F("다른 유닛이 있는 칸입니다."),
        /** Korean source: 다섯 */
        S_FBBD1D1816("다섯"),
        /** Korean source: 다시 플레이하시겠습니까? */
        S_B8C06C1007("다시 플레이하시겠습니까?"),
        /** Korean source: 다음 무장 */
        S_D4B491F164("다음 무장"),
        /** Korean source: 단검유비장비Lv공격력방어력정신력 -> 0123456789 */
        S_B3940E2B50("단검유비장비Lv공격력방어력정신력 -> 0123456789"),
        /** Korean source: 대기 */
        S_DF72A8753D("대기"),
        /** Korean source: 대기 중인 대사가 없습니다. */
        S_C80F942461("대기 중인 대사가 없습니다."),
        /** Korean source: 대기 중인 선택지가 없습니다. */
        S_B1622411B7("대기 중인 선택지가 없습니다."),
        /** Korean source: 대상 유닛이 없습니다. */
        S_367A0A7181("대상 유닛이 없습니다."),
        /** Korean source: 대화창 색상 */
        S_09584A936A("대화창 색상"),
        /** Korean source: 대화창 자동 닫음 */
        S_3424D32FC7("대화창 자동 닫음"),
        /** Korean source: 대화창 자동 닫힘 */
        S_5BB1778DCA("대화창 자동 닫힘"),
        /** Korean source: 동시에 두 개의 loadBg 콜백이 대기 중입니다. */
        S_0CC0A4F490("동시에 두 개의 loadBg 콜백이 대기 중입니다."),
        /** Korean source: 동탁 추격전 */
        S_7579657A88("동탁 추격전"),
        /** Korean source: 됐어 */
        S_2DD57A2739("됐어"),
        /** Korean source: 등록 코드 생성기 */
        S_9EC75F36CA("등록 코드 생성기"),
        /** Korean source: 따뜻한 알림: 오래된 저장 파일일수록 앞에 표시됩니다. */
        S_E1864601EE("따뜻한 알림: 오래된 저장 파일일수록 앞에 표시됩니다."),
        /** Korean source: 레벨 */
        S_453D0D2DF5("레벨"),
        /** Korean source: 레벨: */
        S_E10C69D0A1("레벨:"),
        /** Korean source: 마법 */
        S_9D41799D25("마법"),
        /** Korean source: 마법 명중률:  */
        S_39A4B2720C("마법 명중률: "),
        /** Korean source: 마법 방어율:  */
        S_DD3E1D7F75("마법 방어율: "),
        /** Korean source: 마비 */
        S_38CA304494("마비"),
        /** Korean source: 마왕 */
        S_93DF8F92C4("마왕"),
        /** Korean source: 만렙 시작 */
        S_4956384FE9("만렙 시작"),
        /** Korean source: 맑음 */
        S_9A7F95E71C("맑음"),
        /** Korean source: 매입 */
        S_D1379C024C("매입"),
        /** Korean source: 맵 밖으로 이동할 수 없습니다. */
        S_C88072A92C("맵 밖으로 이동할 수 없습니다."),
        /** Korean source: 먼저 아이템을 사용할 아군을 선택하세요. */
        S_DC25BE8397("먼저 아이템을 사용할 아군을 선택하세요."),
        /** Korean source: 명령 선택을 취소했습니다. */
        S_B7A3BFF7B9("명령 선택을 취소했습니다."),
        /** Korean source: 명중률:  */
        S_66213DD030("명중률: "),
        /** Korean source: 모두 해제 */
        S_E31BDBE56B("모두 해제"),
        /** Korean source: 모두에게 장비를 해제하도록 확정하시겠습니까? */
        S_A9A59D5FE3("모두에게 장비를 해제하도록 확정하시겠습니까?"),
        /** Korean source: 모든 보물을 획득했습니다. 보물 도감에서 확인하세요~ */
        S_90213F42FB("모든 보물을 획득했습니다. 보물 도감에서 확인하세요~"),
        /** Korean source: 모든 부대의 명령을 종료하시겠습니까? */
        S_42D16CB521("모든 부대의 명령을 종료하시겠습니까?"),
        /** Korean source: 모든 부대의 명령을 종료하시겠습니까? 자동 전투 위임 예 아니오 취소 */
        S_A7FDC582CF("모든 부대의 명령을 종료하시겠습니까? 자동 전투 위임 예 아니오 취소"),
        /** Korean source: 모든 부대의 명령을 종료하시겠습니까?게임 저장 */
        S_9417C6AB3A("모든 부대의 명령을 종료하시겠습니까?게임 저장"),
        /** Korean source: 모든 이상 상태 치료 */
        S_EE52984C60("모든 이상 상태 치료"),
        /** Korean source: 무기 */
        S_8C4DAC1FCC("무기"),
        /** Korean source: 무기: */
        S_2B3DFBC5AD("무기:"),
        /** Korean source: 무기레벨 상승! */
        S_7B01D0F5A8("무기레벨 상승!"),
        /** Korean source: 무기점 */
        S_256C17D85E("무기점"),
        /** Korean source: 무력 */
        S_D8F7F3C37F("무력"),
        /** Korean source: 무력 82       민첩성 80\n지력 92       운기 84\n지휘 98 */
        S_A6A330AD58("무력 82       민첩성 80\n지력 92       운기 84\n지휘 98"),
        /** Korean source: 무술 */
        S_0A8D694790("무술"),
        /** Korean source: 무장 */
        S_2E4EDF0E35("무장"),
        /** Korean source: 무장 소개 */
        S_BBEF509C8D("무장 소개"),
        /** Korean source: 무장 열전 */
        S_54F6815FE2("무장 열전"),
        /** Korean source: 무장 정보 */
        S_757535E6BD("무장 정보"),
        /** Korean source: 무장명 */
        S_CAA3F84FF8("무장명"),
        /** Korean source: 민첩성 */
        S_983FFCFEC3("민첩성"),
        /** Korean source: 바람 */
        S_80440AFFDF("바람"),
        /** Korean source: 발견되지 않음 */
        S_DAA1EDD590("발견되지 않음"),
        /** Korean source: 방어 */
        S_2A87C14E4A("방어"),
        /** Korean source: 방어구 */
        S_44F9A814FE("방어구"),
        /** Korean source: 방어력 */
        S_5C349008C3("방어력"),
        /** Korean source: 방어율: */
        S_7DC19BD3AD("방어율:"),
        /** Korean source: 배경 음악 듣기 */
        S_F209C5D3A3("배경 음악 듣기"),
        /** Korean source: 버그 수정</br>코드 최적화 */
        S_57A9A4502B("버그 수정</br>코드 최적화"),
        /** Korean source: 버리기 */
        S_6DB5F96BD1("버리기"),
        /** Korean source: 벤치, 장비 업그레이드 활성화 */
        S_57E6F11841("벤치, 장비 업그레이드 활성화"),
        /** Korean source: 병사 */
        S_7F1A273C82("병사"),
        /** Korean source: 병사  */
        S_2F7F8DF945("병사 "),
        /** Korean source: 보구 */
        S_BD54114CB4("보구"),
        /** Korean source: 보구:  */
        S_1B421182BF("보구: "),
        /** Korean source: 보구레벨 상승! */
        S_54641773A6("보구레벨 상승!"),
        /** Korean source: 보물 */
        S_3830A5432E("보물"),
        /** Korean source: 보물 도감 */
        S_E7236FD795("보물 도감"),
        /** Korean source: 보물 도감 발견되지 않음 지금까지 발견한 보물 종료 부대 정보 일람 무장명 부대 속성 레벨 체력 공격 방어 정신 폭발 사기 폐쇄 창고 일람 이름 속성 경험치 소지자 무기 방어구 보조 */
        S_661525CA4A("보물 도감 발견되지 않음 지금까지 발견한 보물 종료 부대 정보 일람 무장명 부대 속성 레벨 체력 공격 방어 정신 폭발 사기 폐쇄 창고 일람 이름 속성 경험치 소지자 무기 방어구 보조"),
        /** Korean source: 보병 */
        S_EC935E3110("보병"),
        /** Korean source: 보상금 */
        S_35FF880D87("보상금"),
        /** Korean source: 보조 */
        S_51378A4614("보조"),
        /** Korean source: 보조:  */
        S_BC8AE9E500("보조: "),
        /** Korean source: 보통 */
        S_2179DA2CFF("보통"),
        /** Korean source: 복양의 전투 */
        S_17F4CAA0C8("복양의 전투"),
        /** Korean source: 복양의 전투 2 */
        S_08E3B7B077("복양의 전투 2"),
        /** Korean source: 복양의 전투 3 */
        S_4DF0D9A984("복양의 전투 3"),
        /** Korean source: 부대 */
        S_F0D2E9FDE8("부대"),
        /** Korean source: 부대 속성 */
        S_F407A5EA8C("부대 속성"),
        /** Korean source: 부대 정보 일람 */
        S_8767F1CDA5("부대 정보 일람"),
        /** Korean source: 부대 정보 일람: 아군/적군 탭을 선택할 수 있습니다. */
        S_3A6CD5555C("부대 정보 일람: 아군/적군 탭을 선택할 수 있습니다."),
        /** Korean source: 부대 특성 */
        S_EBE739C92D("부대 특성"),
        /** Korean source: 불러오기 */
        S_F01A5A5229("불러오기"),
        /** Korean source: 비 */
        S_D3B0E1A367("비"),
        /** Korean source: 비무시 */
        S_5FF079CE3D("비무시"),
        /** Korean source: 빠르게 */
        S_DE1AE880EF("빠르게"),
        /** Korean source: 빠름 */
        S_F8FC3619DB("빠름"),
        /** Korean source: 사기 */
        S_C91E618B9A("사기"),
        /** Korean source: 사수관 전투 */
        S_9A6128E64A("사수관 전투"),
        /** Korean source: 사용 가능한 소비 아이템이 없습니다. */
        S_CA6C5A2257("사용 가능한 소비 아이템이 없습니다."),
        /** Korean source: 사용 불가 */
        S_9C9F1862EA("사용 불가"),
        /** Korean source: 사용 유닛이 없습니다. */
        S_B9A3425560("사용 유닛이 없습니다."),
        /** Korean source: 사용할 수 없는 아이템입니다. */
        S_2D243A64E7("사용할 수 없는 아이템입니다."),
        /** Korean source: 사용할 수 없는 전략입니다. */
        S_30373C627C("사용할 수 없는 전략입니다."),
        /** Korean source: 사용할 수 있는 전략이 없습니다. */
        S_18CB232899("사용할 수 있는 전략이 없습니다."),
        /** Korean source: 산지 */
        S_F5798CEF9A("산지"),
        /** Korean source: 삼 */
        S_59463E84FB("삼"),
        /** Korean source: 상위 단계로 승급하는 데 필요함 */
        S_DF6168DB1C("상위 단계로 승급하는 데 필요함"),
        /** Korean source: 상점 */
        S_9F66DED326("상점"),
        /** Korean source: 상태 */
        S_2926977BA7("상태"),
        /** Korean source: 상품 목록 */
        S_EA6F893483("상품 목록"),
        /** Korean source: 생성 공유 */
        S_1BCE60B07B("생성 공유"),
        /** Korean source: 서막 */
        S_9CA0190DEC("서막"),
        /** Korean source: 서주 복수전 */
        S_09C5A32367("서주 복수전"),
        /** Korean source: 선택 완료 */
        S_4A021545F6("선택 완료"),
        /** Korean source: 선택한 유닛은 사용할 수 있는 전략이 없습니다. */
        S_DB42F54740("선택한 유닛은 사용할 수 있는 전략이 없습니다."),
        /** Korean source: 설 */
        S_FB5F03E2EE("설"),
        /** Korean source: 설명 */
        S_841964364C("설명"),
        /** Korean source: 세트 목록 */
        S_8DD48EC033("세트 목록"),
        /** Korean source: 소지자 */
        S_E6B2B4DBC0("소지자"),
        /** Korean source: 속도를 10배까지 높일 수 있습니다. */
        S_F0EFEB11FB("속도를 10배까지 높일 수 있습니다."),
        /** Korean source: 속성 */
        S_85DE958B5F("속성"),
        /** Korean source: 속성: */
        S_991BD0CC20("속성:"),
        /** Korean source: 수정 */
        S_E1407B5115("수정"),
        /** Korean source: 숲 */
        S_7CC8A7A989("숲"),
        /** Korean source: 스토리 건너뛰기 활성화 */
        S_0F14132CB0("스토리 건너뛰기 활성화"),
        /** Korean source: 스토리를 건너뛸 수 있습니다. */
        S_01DDCA043B("스토리를 건너뛸 수 있습니다."),
        /** Korean source: 스토리를 건너뛸까요? */
        S_F089BA034E("스토리를 건너뛸까요?"),
        /** Korean source: 승격하여레벨 상승 법술 「」！ */
        S_CBDCD710AB("승격하여레벨 상승 법술 「」！"),
        /** Korean source: 승리 */
        S_90E5E4D22F("승리"),
        /** Korean source: 승리! Enter로 다음 시나리오 · Esc로 돌아가기 */
        S_756B2E906A("승리! Enter로 다음 시나리오 · Esc로 돌아가기"),
        /** Korean source: 시나리오 구간 완료 */
        S_5C77F2F6FC("시나리오 구간 완료"),
        /** Korean source: 시작 화면으로 돌아가시겠습니까? */
        S_5A268D48CF("시작 화면으로 돌아가시겠습니까?"),
        /** Korean source: 실행 프레임이 없습니다. */
        S_8451709DFE("실행 프레임이 없습니다."),
        /** Korean source: 십 */
        S_BDA0208A5C("십"),
        /** Korean source: 쌍타율: */
        S_7210A3FD77("쌍타율:"),
        /** Korean source: 아! */
        S_0DFFF1CFEB("아!"),
        /** Korean source: 아군 */
        S_3843E8E488("아군"),
        /** Korean source: 아군 단계 */
        S_2F82A7D82B("아군 단계"),
        /** Korean source: 아군 단계 적군 최종 턴 제 */
        S_17BDB458F6("아군 단계 적군 최종 턴 제"),
        /** Korean source: 아군 만피 */
        S_6EEBC7BA34("아군 만피"),
        /** Korean source: 아군만 대상으로 할 수 있는 전략입니다. */
        S_A889CDE68C("아군만 대상으로 할 수 있는 전략입니다."),
        /** Korean source: 아군에게만 사용할 수 있습니다. */
        S_C5D202B13B("아군에게만 사용할 수 있습니다."),
        /** Korean source: 아군을 공격할 수 없습니다. */
        S_BDBDED5992("아군을 공격할 수 없습니다."),
        /** Korean source: 아니오 */
        S_CFEF357D40("아니오"),
        /** Korean source: 아이템 */
        S_B62250FE8D("아이템"),
        /** Korean source: 아이템  */
        S_75C20D3862("아이템 "),
        /** Korean source: 아이템 사용 범위를 벗어났습니다. */
        S_8F3BC8881B("아이템 사용 범위를 벗어났습니다."),
        /** Korean source: 아이템 사용을 취소했습니다. */
        S_A7202337A7("아이템 사용을 취소했습니다."),
        /** Korean source: 아이템은 자신 또는 인접 아군에게 사용해야 합니다. */
        S_93EEFD6430("아이템은 자신 또는 인접 아군에게 사용해야 합니다."),
        /** Korean source: 아이템을 사용할 수 없습니다. */
        S_CEF693F5A0("아이템을 사용할 수 없습니다."),
        /** Korean source: 아이템이 가득 찼습니다. 배낭에서 확인해 주세요~ */
        S_388DB7E1C8("아이템이 가득 찼습니다. 배낭에서 확인해 주세요~"),
        /** Korean source: 아직 등장하지 않은 유닛입니다. */
        S_A3E8A0BBCD("아직 등장하지 않은 유닛입니다."),
        /** Korean source: 알 수 없음 */
        S_8916B6394A("알 수 없음"),
        /** Korean source: 암산 */
        S_273718B1D7("암산"),
        /** Korean source: 야심: */
        S_ECE4B128EA("야심:"),
        /** Korean source: 어두움 */
        S_221569E2F4("어두움"),
        /** Korean source: 어떤 진행 상황을 저장할지 선택해 주세요. */
        S_591250DCC4("어떤 진행 상황을 저장할지 선택해 주세요."),
        /** Korean source: 업그레이드/전직 시 재계산 활성화 */
        S_197325F409("업그레이드/전직 시 재계산 활성화"),
        /** Korean source: 없음 */
        S_D58FA73ADC("없음"),
        /** Korean source: 역사 정보 */
        S_625FE3A123("역사 정보"),
        /** Korean source: 영천 캡처용 R_00 도입을 완료하지 못했습니다. */
        S_1990A92813("영천 캡처용 R_00 도입을 완료하지 못했습니다."),
        /** Korean source: 영천 캡처용 아군 명단이 비어 있습니다. */
        S_4DD32031A6("영천 캡처용 아군 명단이 비어 있습니다."),
        /** Korean source: 영천의 전투 */
        S_901EA3E656("영천의 전투"),
        /** Korean source: 영천의 전투 ▪ 훈련 */
        S_1924279772("영천의 전투 ▪ 훈련"),
        /** Korean source: 영천의 전투R */
        S_3FC572DD08("영천의 전투R"),
        /** Korean source: 영향 범위 */
        S_368A5F6FF8("영향 범위"),
        /** Korean source: 예 */
        S_A842629AFD("예"),
        /** Korean source: 완료할 loadBg 콜백이 없습니다. */
        S_FD44645376("완료할 loadBg 콜백이 없습니다."),
        /** Korean source: 외부 전투 대사가 이미 대기 중입니다. */
        S_ECA779BF1F("외부 전투 대사가 이미 대기 중입니다."),
        /** Korean source: 외부 전투 안내는 애니메이션 대기에서만 열 수 있습니다. */
        S_7BEEFFF609("외부 전투 안내는 애니메이션 대기에서만 열 수 있습니다."),
        /** Korean source: 요약 */
        S_3EA27A4D42("요약"),
        /** Korean source: 우군 */
        S_7627935CE4("우군"),
        /** Korean source: 운기 */
        S_DFC3916173("운기"),
        /** Korean source: 원클릭으로 모든 보물 획득 */
        S_2DFF081BB8("원클릭으로 모든 보물 획득"),
        /** Korean source: 원클릭으로 모든 아이템 */
        S_C212B6F4D6("원클릭으로 모든 아이템"),
        /** Korean source: 원클릭으로 모든 아이템을 99개로 채우기 */
        S_472939E979("원클릭으로 모든 아이템을 99개로 채우기"),
        /** Korean source: 위력: */
        S_169487DB3A("위력:"),
        /** Korean source: 위임 */
        S_3DD968413B("위임"),
        /** Korean source: 위임 전투를 시작할 수 없습니다. */
        S_888650180C("위임 전투를 시작할 수 없습니다."),
        /** Korean source: 유닛이 없습니다. */
        S_F35D9439E1("유닛이 없습니다."),
        /** Korean source: 유닛이 중독되면 사망합니다. 저장 슬롯을 100개로 확장했습니다. */
        S_8788735EBF("유닛이 중독되면 사망합니다. 저장 슬롯을 100개로 확장했습니다."),
        /** Korean source: 유비 */
        S_C13513CF50("유비"),
        /** Korean source: 육 */
        S_959F80BB16("육"),
        /** Korean source: 이 물품은 장착할 수 없습니다. */
        S_B2415CBA15("이 물품은 장착할 수 없습니다."),
        /** Korean source: 이 전략의 특수 사용 조건을 충족하지 못했습니다. */
        S_30295DABD1("이 전략의 특수 사용 조건을 충족하지 못했습니다."),
        /** Korean source: 이 지형에서는 사용할 수 없는 전략입니다. */
        S_BFE3A374EA("이 지형에서는 사용할 수 없는 전략입니다."),
        /** Korean source: 이동 */
        S_C686D05434("이동"),
        /** Korean source: 이동 범위를 벗어났습니다. */
        S_0E403240CA("이동 범위를 벗어났습니다."),
        /** Korean source: 이동력 */
        S_B2A0B8369E("이동력"),
        /** Korean source: 이동할 수 없는 칸입니다. */
        S_A4D25C80A2("이동할 수 없는 칸입니다."),
        /** Korean source: 이름 */
        S_9AA18E5071("이름"),
        /** Korean source: 이미 대사가 표시 중입니다. */
        S_14244CA6EE("이미 대사가 표시 중입니다."),
        /** Korean source: 이미 이동한 유닛입니다. */
        S_AFE1C1AE28("이미 이동한 유닛입니다."),
        /** Korean source: 이미 행동한 유닛입니다. */
        S_D7AE11A0C8("이미 행동한 유닛입니다."),
        /** Korean source: 이전 무장 */
        S_B38CAAEF34("이전 무장"),
        /** Korean source: 이중 타격률: */
        S_474B2D7F53("이중 타격률:"),
        /** Korean source: 인물 특기 일람 */
        S_B9F40373E5("인물 특기 일람"),
        /** Korean source: 인벤토리: */
        S_EF2ACA14AE("인벤토리:"),
        /** Korean source: 인벤토리:  */
        S_F83EB3F89C("인벤토리: "),
        /** Korean source: 일 */
        S_06CF3E90DE("일"),
        /** Korean source: 읽을 진행 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다. */
        S_B53B4895CF("읽을 진행 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다."),
        /** Korean source: 있\n나요? */
        S_42595D2C2A("있\n나요?"),
        /** Korean source: 있나요? */
        S_31D5864AE8("있나요?"),
        /** Korean source: 자세히 */
        S_744C6C3F65("자세히"),
        /** Korean source: 작열 */
        S_99BC913EA8("작열"),
        /** Korean source: 장막 */
        S_22EF795E23("장막"),
        /** Korean source: 장면 이동: */
        S_C0E53C8835("장면 이동:"),
        /** Korean source: 장보와 장량을\n격퇴하십시오. */
        S_981DA7E6CC("장보와 장량을\n격퇴하십시오."),
        /** Korean source: 장비 */
        S_E17B206052("장비"),
        /** Korean source: 장비 정보 */
        S_FF78287D6C("장비 정보"),
        /** Korean source: 장비를 변경했습니다. */
        S_1EEA906E1C("장비를 변경했습니다."),
        /** Korean source: 장비를 해제했습니다. */
        S_A1DB289D94("장비를 해제했습니다."),
        /** Korean source: 장애물이 있는 칸입니다. */
        S_D9E41E686E("장애물이 있는 칸입니다."),
        /** Korean source: 장착 가능한 부대입니다. */
        S_1CC0D0F805("장착 가능한 부대입니다."),
        /** Korean source: 장치 코드를 얻지 못하여 활성화 코드 생성 실패! */
        S_F65B22617D("장치 코드를 얻지 못하여 활성화 코드 생성 실패!"),
        /** Korean source: 재개할 모달 대기가 없습니다. */
        S_F4DE297D31("재개할 모달 대기가 없습니다."),
        /** Korean source: 재개할 외부 애니메이션 대기가 없습니다. */
        S_38B7597664("재개할 외부 애니메이션 대기가 없습니다."),
        /** Korean source: 저장 */
        S_1F1712ACFF("저장"),
        /** Korean source: 저장 완료. */
        S_5AB5BF64DD("저장 완료."),
        /** Korean source: 저장 파일이 손실되었습니다! */
        S_6B7BBE748F("저장 파일이 손실되었습니다!"),
        /** Korean source: 저장된 게임에서 다시 확인해 주세요./현재 업적이 없습니다. */
        S_1A7993E57F("저장된 게임에서 다시 확인해 주세요./현재 업적이 없습니다."),
        /** Korean source: 저장이 호환되지 않아 불러오기에 실패했습니다! */
        S_A7F94475AE("저장이 호환되지 않아 불러오기에 실패했습니다!"),
        /** Korean source: 적 증원군 */
        S_654B500353("적 증원군"),
        /** Korean source: 적군 */
        S_93A131140A("적군"),
        /** Korean source: 적군 단계 */
        S_AF6F2E389D("적군 단계"),
        /** Korean source: 적군 전멸 */
        S_BFCD361E58("적군 전멸"),
        /** Korean source: 적군 체력 감소 */
        S_C23A1CAF1F("적군 체력 감소"),
        /** Korean source: 적군 체력이 남아도 도망가지 않습니다. */
        S_928236D584("적군 체력이 남아도 도망가지 않습니다."),
        /** Korean source: 적군만 대상으로 할 수 있는 전략입니다. */
        S_5045C9D518("적군만 대상으로 할 수 있는 전략입니다."),
        /** Korean source: 적군은 무작정 돌진만 합니다. */
        S_E4DDAD5737("적군은 무작정 돌진만 합니다."),
        /** Korean source: 적군을 전멸시키십시오. */
        S_CB5A392498("적군을 전멸시키십시오."),
        /** Korean source: 전략 */
        S_913A74B987("전략"),
        /** Korean source: 전략 범위를 벗어났습니다. */
        S_DBCFE0B704("전략 범위를 벗어났습니다."),
        /** Korean source: 전리품 */
        S_A87004CBE7("전리품"),
        /** Korean source: 전부 */
        S_72FF6EAABE("전부"),
        /** Korean source: 전술 선택 */
        S_AB8D20CEA1("전술 선택"),
        /** Korean source: 전역 */
        S_F4C1EA1E89("전역"),
        /** Korean source: 전역 변수 편집 */
        S_4A0717C830("전역 변수 편집"),
        /** Korean source: 전용 목록 */
        S_7072EC6F49("전용 목록"),
        /** Korean source: 전장 편집 */
        S_487C122B9A("전장 편집"),
        /** Korean source: 전투 상태 패널 사용 불가 */
        S_75676A3A96("전투 상태 패널 사용 불가"),
        /** Korean source: 전투 시 전장 축소 이미지가 자동으로 표시됩니다. */
        S_E3C0F294CB("전투 시 전장 축소 이미지가 자동으로 표시됩니다."),
        /** Korean source: 전투 종료 */
        S_5000089835("전투 종료"),
        /** Korean source: 전투 종료 시 인물 및 장비 레벨을 평균 레벨로 자동 상승 */
        S_38CF88C517("전투 종료 시 인물 및 장비 레벨을 평균 레벨로 자동 상승"),
        /** Korean source: 전투 종료보상금전리품★☆ */
        S_F80BBD09E2("전투 종료보상금전리품★☆"),
        /** Korean source: 전투 준비/전투 중일 때만 뽑기가 가능합니다! */
        S_A174F4502E("전투 준비/전투 중일 때만 뽑기가 가능합니다!"),
        /** Korean source: 전투 중 체력과 마나, 경험치 변화판이 더 이상 표시되지 않습니다. */
        S_7518581DDB("전투 중 체력과 마나, 경험치 변화판이 더 이상 표시되지 않습니다."),
        /** Korean source: 전투 중 편집 기능은 원본과 동일하게 개발 기능이 활성화된 경우에만 사용할 수 있습니다. */
        S_EC2B4248A3("전투 중 편집 기능은 원본과 동일하게 개발 기능이 활성화된 경우에만 사용할 수 있습니다."),
        /** Korean source: 전투가 종료되었습니다. */
        S_14E3B0AF5D("전투가 종료되었습니다."),
        /** Korean source: 전투시 전장 축소 이미지가 자동으로 표시됩니다 */
        S_CF7DC571E7("전투시 전장 축소 이미지가 자동으로 표시됩니다"),
        /** Korean source: 정보 */
        S_032E3F1F2B("정보"),
        /** Korean source: 정보 설명 */
        S_B80EE3CF4F("정보 설명"),
        /** Korean source: 정신 */
        S_C90D66734C("정신"),
        /** Korean source: 정신력 */
        S_F3F57B591C("정신력"),
        /** Korean source: 조조 */
        S_6B1F41FC4A("조조"),
        /** Korean source: 조조 전 원본 아바타와 이미지 사용 */
        S_371F34313C("조조 전 원본 아바타와 이미지 사용"),
        /** Korean source: 종료 */
        S_CAFDC61BBF("종료"),
        /** Korean source: 좌표를 대상으로 할 수 없는 전략입니다. */
        S_29708E4B21("좌표를 대상으로 할 수 없는 전략입니다."),
        /** Korean source: 죄송합니다. 비디오 로드에 실패했습니다! */
        S_3EECA43C83("죄송합니다. 비디오 로드에 실패했습니다!"),
        /** Korean source: 중 */
        S_43E88C0B9C("중"),
        /** Korean source: 중간 */
        S_C7E54FE959("중간"),
        /** Korean source: 중독 */
        S_2B0B630E57("중독"),
        /** Korean source: 중독되면 죽음; 확장 저장 */
        S_A9E737C08B("중독되면 죽음; 확장 저장"),
        /** Korean source: 증원군 */
        S_9C8523BB36("증원군"),
        /** Korean source: 지력 */
        S_B08EB69621("지력"),
        /** Korean source: 지형 정보 일람 */
        S_8472C11F5C("지형 정보 일람"),
        /** Korean source: 지형 효과 */
        S_AD01270B3C("지형 효과"),
        /** Korean source: 지휘 */
        S_DAFEF2D567("지휘"),
        /** Korean source: 진행 상황 유지 */
        S_158191141D("진행 상황 유지"),
        /** Korean source: 진행 상황 유지 어떤 진행 상황을 저장할지 선택해 주세요 따뜻한 알림 오래된 저장 파일일수록 앞에 표시됩니다 취소 진행도 불러오기 읽을 최신 저장 파일이 가장 위에 있습니다 */
        S_636899E260("진행 상황 유지 어떤 진행 상황을 저장할지 선택해 주세요 따뜻한 알림 오래된 저장 파일일수록 앞에 표시됩니다 취소 진행도 불러오기 읽을 최신 저장 파일이 가장 위에 있습니다"),
        /** Korean source: 진행 상황 저장 안 함 */
        S_2BF319F7E7("진행 상황 저장 안 함"),
        /** Korean source: 진행 상황을 저장했습니다. */
        S_EAD3664297("진행 상황을 저장했습니다."),
        /** Korean source: 진행도 불러오기 */
        S_8FB0968760("진행도 불러오기"),
        /** Korean source: 진행도 저장: 저장할 수 있나요? */
        S_DDA4595886("진행도 저장: 저장할 수 있나요?"),
        /** Korean source: 짐이 알겠다. */
        S_C04D541DFC("짐이 알겠다."),
        /** Korean source: 창고 */
        S_194738C8E4("창고"),
        /** Korean source: 창고 목록 */
        S_C6D77220D1("창고 목록"),
        /** Korean source: 창고 비우기 */
        S_6C901520EF("창고 비우기"),
        /** Korean source: 창고 일람 */
        S_BBCCA2F5AB("창고 일람"),
        /** Korean source: 청주 황건 토벌전 */
        S_98CFB12354("청주 황건 토벌전"),
        /** Korean source: 체력 */
        S_A45EA58EBC("체력"),
        /** Korean source: 체력 바가 유닛 위에 있습니다 */
        S_077AE5CBAB("체력 바가 유닛 위에 있습니다"),
        /** Korean source: 초원 */
        S_BC9CE179E3("초원"),
        /** Korean source: 총합: */
        S_21E6F82F77("총합:"),
        /** Korean source: 최소한 하나를 선택하여 활성화해야 합니다. */
        S_6F764343C8("최소한 하나를 선택하여 활성화해야 합니다."),
        /** Korean source: 최종 턴 */
        S_246D8BEA50("최종 턴"),
        /** Korean source: 취소 */
        S_19B2D19BC1("취소"),
        /** Korean source: 치명타율: */
        S_B6BFF83C31("치명타율:"),
        /** Korean source: 칠 */
        S_4259DD769C("칠"),
        /** Korean source: 클릭: 선택/이동/공격 · M: 전략 · B: 아이템 · T: 턴 종료 · Esc: 돌아가기 */
        S_3992241AD7("클릭: 선택/이동/공격 · M: 전략 · B: 아이템 · T: 턴 종료 · Esc: 돌아가기"),
        /** Korean source: 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요. */
        S_8277D3ABFB("클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요."),
        /** Korean source: 턴 수 */
        S_7A2ACD7CB6("턴 수"),
        /** Korean source: 턴 수 /0123456789 */
        S_475D2D45E2("턴 수 /0123456789"),
        /** Korean source: 턴 전환을 시작할 수 없습니다. */
        S_95B47268F5("턴 전환을 시작할 수 없습니다."),
        /** Korean source: 턴 제한 증가 */
        S_58B5B8E6E2("턴 제한 증가"),
        /** Korean source: 턴 종료로 라운드와 이벤트를 확인하세요 */
        S_57A2B6698C("턴 종료로 라운드와 이벤트를 확인하세요"),
        /** Korean source: 턴\s*수가\s*(\d+) */
        S_CC6A623F14("턴\\s*수가\\s*(\\d+)"),
        /** Korean source: 텍스트 속도 */
        S_0BF12D107C("텍스트 속도"),
        /** Korean source: 특기 */
        S_D3901EE454("특기"),
        /** Korean source: 특수 효과 */
        S_9940B21F7F("특수 효과"),
        /** Korean source: 판매 수량(1 - %d): */
        S_B9A391DF6A("판매 수량(1 - %d):"),
        /** Korean source: 판매하기 */
        S_F1BC97987B("판매하기"),
        /** Korean source: 판매할 물품이 없습니다. */
        S_194D16A649("판매할 물품이 없습니다."),
        /** Korean source: 판매할 수 없는 물품입니다. */
        S_6812661FAF("판매할 수 없는 물품입니다."),
        /** Korean source: 팔 */
        S_C6D6E215B1("팔"),
        /** Korean source: 패배… Enter로 전투 재시작 · Esc로 돌아가기 */
        S_BF14E6715D("패배… Enter로 전투 재시작 · Esc로 돌아가기"),
        /** Korean source: 편집 기능 활성화 */
        S_E6E23C36DB("편집 기능 활성화"),
        /** Korean source: 평원 */
        S_208485813F("평원"),
        /** Korean source: 폐쇄 */
        S_438784C35E("폐쇄"),
        /** Korean source: 포위 공격 */
        S_3961E081B0("포위 공격"),
        /** Korean source: 포위 공격 대상을 선택하세요. */
        S_30B3504918("포위 공격 대상을 선택하세요."),
        /** Korean source: 포차 */
        S_2258169007("포차"),
        /** Korean source: 폭발 */
        S_AF51A96858("폭발"),
        /** Korean source: 폭발력 */
        S_9EC6283754("폭발력"),
        /** Korean source: 프로필 사진, 스토리, 전투 이미지는 구버전 것을 사용하고, 향수를 느끼고 싶으면 사용하세요. */
        S_4F15973D8F("프로필 사진, 스토리, 전투 이미지는 구버전 것을 사용하고, 향수를 느끼고 싶으면 사용하세요."),
        /** Korean source: 피격 시 치명타율: */
        S_D56C3E9F15("피격 시 치명타율:"),
        /** Korean source: 피해 계수:  */
        S_63314575EA("피해 계수: "),
        /** Korean source: 한국어 글꼴을 찾지 못했습니다. JOJO_FONT_PATH를 설정하세요. */
        S_1217D649AD("한국어 글꼴을 찾지 못했습니다. JOJO_FONT_PATH를 설정하세요."),
        /** Korean source: 항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요. */
        S_AE10B3B6C2("항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요."),
        /** Korean source: 해제 */
        S_A7ABEA5BBB("해제"),
        /** Korean source: 해제할 장비가 없습니다. */
        S_3A1198BB6C("해제할 장비가 없습니다."),
        /** Korean source: 행동할 수 없는 상태입니다. */
        S_D1327CFCA3("행동할 수 없는 상태입니다."),
        /** Korean source: 행운 코인이 부족하여 교환에 실패했습니다~ */
        S_1C0421EDD5("행운 코인이 부족하여 교환에 실패했습니다~"),
        /** Korean source: 행운의 코인으로 광고를 보고 교환하는 것을 취소합니다! */
        S_88EFDCC59F("행운의 코인으로 광고를 보고 교환하는 것을 취소합니다!"),
        /** Korean source: 허자장 */
        S_F8BE692B65("허자장"),
        /** Korean source: 현금 */
        S_6102409B7F("현금"),
        /** Korean source: 현재 날씨에서는 사용할 수 없는 전략입니다. */
        S_54BF0673A8("현재 날씨에서는 사용할 수 없는 전략입니다."),
        /** Korean source: 현재 상태에서는 전략을 사용할 수 없습니다. */
        S_DA89D1A923("현재 상태에서는 전략을 사용할 수 없습니다."),
        /** Korean source: 현재 유닛은 전략을 사용할 수 없습니다. */
        S_DADF270A7B("현재 유닛은 전략을 사용할 수 없습니다."),
        /** Korean source: 현재 진영의 유닛만 조작할 수 있습니다. */
        S_50AFF62D3A("현재 진영의 유닛만 조작할 수 있습니다."),
        /** Korean source: 현재 턴: */
        S_80A04709E3("현재 턴:"),
        /** Korean source: 현재 행동할 수 없는 유닛입니다. */
        S_AD31679FA5("현재 행동할 수 없는 유닛입니다."),
        /** Korean source: 현재/업그레이드 필요 공훈 */
        S_7EB7AC14F9("현재/업그레이드 필요 공훈"),
        /** Korean source: 호로관 전투 */
        S_28B8268B1E("호로관 전투"),
        /** Korean source: 호우 */
        S_1F899001DE("호우"),
        /** Korean source: 혼란 */
        S_738A3F61E4("혼란"),
        /** Korean source: 확인 */
        S_468266D639("확인"),
        /** Korean source: 환경 설정 */
        S_B6D9331463("환경 설정"),
        /** Korean source: 환경 설정 클릭하여 설정해 주세요 설정 완료 후 확인을 선택해 주세요 배경 음악 듣기 효과음 듣기 전투시 전장 축소 이미지가 자동으로 표시됩니다 대화창 자동 닫음 체력 바가 유닛 위에 있습니다 텍스트 속도 느림 중간 빠름 정보 설명 자세히 보통 요약 대화창 색상 */
        S_CF2C285BC5("환경 설정 클릭하여 설정해 주세요 설정 완료 후 확인을 선택해 주세요 배경 음악 듣기 효과음 듣기 전투시 전장 축소 이미지가 자동으로 표시됩니다 대화창 자동 닫음 체력 바가 유닛 위에 있습니다 텍스트 속도 느림 중간 빠름 정보 설명 자세히 보통 요약 대화창 색상"),
        /** Korean source: 활성화 */
        S_BBF831ADE8("활성화"),
        /** Korean source: 활성화 시 업그레이드/전직마다 재계산, 출전 시 자동 배치 및 원클릭 장비 세팅 */
        S_D8412481BB("활성화 시 업그레이드/전직마다 재계산, 출전 시 자동 배치 및 원클릭 장비 세팅"),
        /** Korean source: 활성화 시 전투에 진입하면 턴 상한이 4턴 증가합니다 */
        S_0EC18AC7A9("활성화 시 전투에 진입하면 턴 상한이 4턴 증가합니다"),
        /** Korean source: 활성화 코드를 입력하세요 */
        S_2B04A81A98("활성화 코드를 입력하세요"),
        /** Korean source: 활성화에 성공했는지 확신이 서지 않는다면 이 버튼을 눌러 다시 활성화 여부를 확인할 수 있습니다. 계속하시겠습니까? */
        S_8BCBE22C40("활성화에 성공했는지 확신이 서지 않는다면 이 버튼을 눌러 다시 활성화 여부를 확인할 수 있습니다. 계속하시겠습니까?"),
        /** Korean source: 황건적 */
        S_DD94B8A1D7("황건적"),
        /** Korean source: 황제 구출 전투 */
        S_AFB475EE1C("황제 구출 전투"),
        /** Korean source: 황지 */
        S_55C0FB1865("황지"),
        /** Korean source: 회오리 */
        S_2FAB2DACE0("회오리"),
        /** Korean source: 효과 */
        S_CE49DECB68("효과"),
        /** Korean source: 효과음 듣기 */
        S_8F62514DB8("효과음 듣기"),
        /** Korean source: 효능:  */
        S_DF988D7025("효능: "),
        /** Korean source: 흐림 */
        S_887F182653("흐림"),
    }

    private val english = mapOf(
        Key.ITEM_PURCHASE to "Purchase {0}",
        Key.ITEM_SALE to "Sell {0}",
        Key.ITEM_DISCARDED to "{0} was already discarded...",
        Key.UNIT_FALLBACK to "Unit {0}",
        Key.STAT_BONUS to "{0} +{1}\n{2}",
        Key.TREASURE_PROGRESS to "Treasures found: {0} / {1}",
        Key.SELL_PRICE to "Sell price: {0}",
        Key.INVENTORY_COUNT to "Inventory: {0}",
        Key.PRICE to "Price: {0}",
        Key.TROOP_COUNT to "Deployed units - {0}/{1}",
        Key.SELECTION_TOTAL to "Selected {0} items, total {1} gold",
        Key.EVENT_WEATHER to "Weather: {0}",
        Key.BATTLE_ROUND to "Round {0} · {1}'s turn · {2}",
        Key.AI_TURN_SUMMARY to "{0}: move {1} · attack {2} · wait {3}",
        Key.AI_UNIT_ACTION to "{0}: {1} action",
        Key.UNIT_SELECTED to "{0} selected",
        Key.UNIT_SELECTED_MOVE to "{0} selected · move to an empty tile or attack an adjacent enemy",
        Key.UNIT_COMMAND to "{0}: choose an action",
        Key.UNIT_MOVE_DONE to "{0} moved",
        Key.UNIT_WAITING to "{0} waits",
        Key.UNIT_IDLE to "Units not yet acted: {0}",
        Key.MAGIC_TARGET to "{0} selected · choose a target in range",
        Key.ITEM_TARGET to "{0} selected ({1}) · choose yourself or an adjacent ally",
        Key.TACTICAL_MOVE to "{0} moved",
        Key.TACTICAL_ATTACK to "{0} attacked · {1} damage{2}{3}{4}",
        Key.TACTICAL_MAGIC to "{0} {1} · MP {2} · {3}{4}",
        Key.COMBAT_DAMAGE to "Restored {0} HP",
        Key.MAGIC_RECOVERY to "Restored {0} MP",
        Key.STATUS_CURED to "{0} cured",
        Key.ATTRIBUTE_INCREASE to "{0} increased",
        Key.HP_INCREASE to "Maximum HP increased by {0}",
        Key.MP_INCREASE to "Maximum MP increased by {0}",
        Key.SAVE_CONFIRM to "Save progress No.{0}: {1}?",
        Key.LOAD_CONFIRM to "Load progress No.{0}: {1}?",
        Key.ITEM_DROP_CONFIRM to "Discard {0}?",
        Key.RESOURCE_LOADING to "Loading resources {0}%",
        Key.SAVE_EQUIPMENT_NOTICE to "Unequipped all equipment from {0} units.",
        Key.ABILITY_LEVEL to "{0} increased",
        Key.PROMOTION_LEVEL to "{0} promoted to level {1}",
        Key.MAGIC_LEARNED to "Learned spell “{0}”!",
        Key.STAGE_NAME to "Campaign {0}",
        Key.VERIFICATION_UNIT_MISSING to "{0}: no source battle units.",
        Key.VERIFICATION_MAP_MISSING to "{0}: source battlefield image not found: {1}",
        Key.VERIFICATION_UNIT_COUNT to "{0}: converted battle unit count does not match.",
        Key.VERIFICATION_PLAYER_UNIT to "{0}: failed to convert player units.",
        Key.VERIFICATION_ENEMY_UNIT to "{0}: no enemy units.",
        Key.VERIFICATION_SPRITE_MISSING to "{0}: source unit sprite not found.",
        Key.S_468266D639 to "OK",
        Key.S_19B2D19BC1 to "Cancel",
        Key.S_A842629AFD to "Yes",
        Key.S_CFEF357D40 to "No",
        Key.S_D3B0E1A367 to "No",
        Key.S_CAFDC61BBF to "Exit",
        Key.S_2A9C189A93 to "Attack",
        Key.S_9D41799D25 to "Magic",
        Key.S_B62250FE8D to "Items",
        Key.S_D7F1F76848 to "Exchange",
        Key.S_DF72A8753D to "Wait",
        Key.S_032E3F1F2B to "Information",
        Key.S_1F1712ACFF to "Save",
        Key.S_F01A5A5229 to "Load",
        Key.S_453D0D2DF5 to "Level",
        Key.S_9AA18E5071 to "Name",
        Key.S_85DE958B5F to "Attribute",
        Key.S_A45EA58EBC to "HP",
        Key.S_50B8DF8C55 to "Attack",
        Key.S_5C349008C3 to "Defense",
        Key.S_F3F57B591C to "Spirit",
        Key.S_9EC6283754 to "Critical",
        Key.S_C91E618B9A to "Morale",
        Key.S_B2A0B8369E to "Movement",
        Key.S_E134585B3A to "Experience",
        Key.S_8C4DAC1FCC to "Weapon",
        Key.S_44F9A814FE to "Armor",
        Key.S_51378A4614 to "Auxiliary",
        Key.S_3830A5432E to "Treasure",
        Key.S_9F66DED326 to "Shop",
        Key.S_256C17D85E to "Weapon Shop",
        Key.S_194738C8E4 to "Storage",
        Key.S_913A74B987 to "Tactics",
        Key.S_3961E081B0 to "Surround Attack",
        Key.S_F4C1EA1E89 to "Campaign",
        Key.S_3843E8E488 to "Ally",
        Key.S_93A131140A to "Enemy",
        Key.S_AD01270B3C to "Terrain Effects",
        Key.S_A8DE9DA622 to "Movement Cost",
        Key.S_80A04709E3 to "Current Turn:",
        Key.S_487C122B9A to "Edit Battlefield",
        Key.S_D58FA73ADC to "None",
        Key.S_DAA1EDD590 to "Undiscovered",
    )

    fun text(key: Key): String = when (language) {
        Language.KOREAN -> key.korean
        Language.ENGLISH -> english[key] ?: key.korean
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
