package com.jojo.game.presentation.scenario.hall

/** Ready-to-draw HelperLayer content. */
internal data class HallHelperView(val text: String) {
    companion object {
        val default = HallHelperView(
            "6 [단축키 설명]\n☆ 일부 단축키 기능은 메뉴 — 설정을 통해 직접 설정할 수 있습니다.\n" +
                "☆ 번호 0-4: 단계 속도 변화. 0가 원래 속도이며, 1-4가 가속.\n" +
                "☆ 번호 5: 진영에 따라 다른 색상의 체력 바를 표시합니다.\n" +
                "☆ 번호 6: 문자 BUFF와 DEBUFF를 표시합니다.\n" +
                "☆ 번호 7: 왼쪽 하단에 캐릭터 능력과 장비를 표시합니다.\n" +
                "☆ 숫자 8: 더블 히트의 치명타 확률과 카운터 관계를 표시합니다.\n" +
                "☆ 번호 9: 지형 적응 및 이동 비용을 표시합니다.\n" +
                "☆ 문자 A: 턴 시작 시 자동으로 저장됩니다.\n" +
                "☆ 문자 B: 속성 인터페이스는 모든 가능한 전략과 학습 수준을 표시합니다.",
        )
    }
}
