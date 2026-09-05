package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

/**
 * Installs deterministic visual fixtures for Hall, Palace, Section, and Hall overlay layers
 * matching Python source fixtures and tests.
 */
internal object ScenarioFixtureInstaller {

    /**
     * 공개 메서드 `installHallFixture`
     *
     * ### 파라미터
    - `interpreter` (`ScenarioInterpreter`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun installHallFixture(interpreter: ScenarioInterpreter) {
        installHallFixture(
            stage = interpreter.stage,
            clearFrames = interpreter.callStack::clear,
            clearPendingDialogues = interpreter.dialogueCoordinator.pendingDialogues::clear,
            resetSpeakers = {
                interpreter.dialogueCoordinator.resetSpeakers(-1)
                interpreter.choiceCoordinator.reset()
                interpreter.modalController.reset()
            },
            presentDialogue = interpreter.dialogueCoordinator::presentDialogue,
            onStateChange = { interpreter.state = it },
        )
    }

    /**
     * 공개 메서드 `installPalaceFixture`
     *
     * ### 파라미터
    - `interpreter` (`ScenarioInterpreter`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun installPalaceFixture(interpreter: ScenarioInterpreter) {
        installPalaceFixture(
            stage = interpreter.stage,
            clearFrames = interpreter.callStack::clear,
            clearPendingDialogues = interpreter.dialogueCoordinator.pendingDialogues::clear,
            resetSpeakers = {
                interpreter.dialogueCoordinator.resetSpeakers(-1)
                interpreter.choiceCoordinator.reset()
                interpreter.modalController.reset()
            },
            presentDialogue = interpreter.dialogueCoordinator::presentDialogue,
            onStateChange = { interpreter.state = it },
        )
    }

    /**
     * 공개 메서드 `installSectionFixture`
     *
     * ### 파라미터
    - `interpreter` (`ScenarioInterpreter`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun installSectionFixture(interpreter: ScenarioInterpreter) {
        interpreter.dialogueCoordinator.reset()
        interpreter.choiceCoordinator.reset()
        installSectionFixture(
            stage = interpreter.stage,
            clearFrames = interpreter.callStack::clear,
            clearPendingDialogues = interpreter.dialogueCoordinator.pendingDialogues::clear,
            modalController = interpreter.modalController,
        )
    }

    /**
     * 공개 메서드 `installOverlayFixture`
     *
     * ### 파라미터
    - `interpreter` (`ScenarioInterpreter`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `kind` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun installOverlayFixture(interpreter: ScenarioInterpreter, kind: String) {
        interpreter.dialogueCoordinator.reset()
        interpreter.choiceCoordinator.reset()
        installOverlayFixture(
            kind = kind,
            stage = interpreter.stage,
            clearFrames = interpreter.callStack::clear,
            clearPendingDialogues = interpreter.dialogueCoordinator.pendingDialogues::clear,
            modalController = interpreter.modalController,
            setChoice = { choice, selected, isAsk ->
                interpreter.choiceCoordinator.setDirectChoice(choice, selected, isAsk)
                interpreter.state = PlaybackState.CHOICE
            },
            onStateChange = { interpreter.state = it },
        )
    }

    fun installHallFixture(
        stage: ScenarioStage,
        clearFrames: () -> Unit,
        clearPendingDialogues: () -> Unit,
        resetSpeakers: () -> Unit,
        presentDialogue: (Dialogue) -> Unit,
        onStateChange: (PlaybackState) -> Unit,
    ) {
        clearFrames()
        clearPendingDialogues()
        stage.heads.clear()
        stage.clearUnits()
        stage.apply(ScenarioCommand.LoadBackground(2, 30))
        stage.apply(ScenarioCommand.ShowUnit(0, 45, 48, 0))
        stage.apply(ScenarioCommand.ShowUnit(157, 55, 52, 2))
        stage.apply(ScenarioCommand.ShowUnit(181, 51, 45, 3))
        stage.showHead(0, 180, 210)
        stage.showHead(157, 460, 220)
        stage.finishAnimations()
        resetSpeakers()
        presentDialogue(Dialogue("0", "원본 궁정 대화 UI 비교"))
        onStateChange(PlaybackState.DIALOGUE)
    }

    fun installPalaceFixture(
        stage: ScenarioStage,
        clearFrames: () -> Unit,
        clearPendingDialogues: () -> Unit,
        resetSpeakers: () -> Unit,
        presentDialogue: (Dialogue) -> Unit,
        onStateChange: (PlaybackState) -> Unit,
    ) {
        clearFrames()
        clearPendingDialogues()
        stage.heads.clear()
        stage.clearUnits()
        stage.apply(ScenarioCommand.LoadBackground(2, 9))
        listOf(
            ScenarioCommand.ShowUnit(181, 52, 41, 2),
            ScenarioCommand.ShowUnit(157, 64, 41, 2),
            ScenarioCommand.ShowUnit(0, 58, 70, 0),
        ).forEach(stage::apply)
        stage.finishAnimations()
        resetSpeakers()
        presentDialogue(Dialogue("0", "원본 궁정 장면 UI 비교"))
        onStateChange(PlaybackState.DIALOGUE)
    }

    fun installSectionFixture(
        stage: ScenarioStage,
        clearFrames: () -> Unit,
        clearPendingDialogues: () -> Unit,
        modalController: ScenarioModalController,
    ) {
        clearFrames()
        clearPendingDialogues()
        stage.heads.clear()
        stage.clearUnits()
        stage.apply(ScenarioCommand.LoadBackground(2, 71))
        modalController.setSectionFixture("제일장막", "황건", 3f)
    }

    fun installOverlayFixture(
        kind: String,
        stage: ScenarioStage,
        clearFrames: () -> Unit,
        clearPendingDialogues: () -> Unit,
        modalController: ScenarioModalController,
        setChoice: (Choice?, Int, Boolean) -> Unit,
        onStateChange: (PlaybackState) -> Unit,
    ) {
        clearFrames()
        clearPendingDialogues()
        stage.heads.clear()
        stage.clearUnits()
        stage.apply(ScenarioCommand.LoadBackground(2, 30))
        listOf(
            ScenarioCommand.ShowUnit(0, 45, 48, 0),
            ScenarioCommand.ShowUnit(157, 55, 52, 2),
            ScenarioCommand.ShowUnit(181, 51, 45, 3),
        ).forEach(stage::apply)
        stage.finishAnimations()
        modalController.reset()
        when (kind) {
            "info" -> modalController.setModalFixture("재능의 첫 징후", ScenarioInterpreter.ModalKind.INFO, 5f)
            "get-item-equipment" -> {
                stage.getItem(3, 2)
                modalController.setModalFixture("얻었다 단창 Lv0", ScenarioInterpreter.ModalKind.INFO, 5f)
            }

            "get-item-property" -> {
                modalController.setModalFixture(stage.getItem(150, 2), ScenarioInterpreter.ModalKind.INFO, 5f)
            }

            "choice" -> setChoice(Choice(listOf("바로 이게 제가 바라는 거예요", "이건 너무 이른 것 같아"), 0), 0, false)
            "ask" -> setChoice(Choice(listOf("예", "비"), null), 0, true)
            "command", "menu", "save", "save-confirm", "item-equipment", "item-property", "item-discard-confirm", "equip", "unit-list", "unit-list-select", "unit-list-close", "equip-confirm", "equip-confirm-unload", "exclusive", "exclusive-tab1", "magic", "feats", "feats-help", "buy", "sell", "forces", "property", "terrain", "treasure", "helper", "skip-open" -> {
                stage.apply(ScenarioCommand.SetEventName(""))
                stage.setStageName("")
                stage.setMenuVisible(true)
                onStateChange(PlaybackState.COMPLETE)
            }

            "map-info" -> modalController.setModalFixture(
                "조조가 수저우 도겸과 전투를 벌였을 때,",
                ScenarioInterpreter.ModalKind.MAP_INFO,
                5f
            )

            "ambition" -> {
                stage.apply(ScenarioCommand.SetEventName("조조가 군대를 일으키다"))
                stage.setStageName("사수관 조조군 주진영")
                modalController.suspendForAmbition(5)
                modalController.setAmbitionFixture(2.2f, false, 60f)
            }

            else -> error("Unknown Hall overlay fixture: $kind")
        }
    }
}
