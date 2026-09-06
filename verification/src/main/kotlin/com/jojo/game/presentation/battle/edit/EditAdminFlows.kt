// Verification
package com.jojo.game.presentation.battle.edit
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.*

/** EditRosterFlow: Hall/scene/EditLayer4에서 복원한 편성 계약을 순수 상태로 표현한다. */
class EditRosterFlow(initial: List<UnitRow>, private val unitNames: List<String>) {

    /** UnitRow: unit row 관련 검증 상태와 동작을 제공하는 타입이다. */
    data class UnitRow(val id: Int, val name: String, val leave: Boolean)
    /** Effect: effect 관련 검증 상태와 동작을 제공하는 타입이다. */
    sealed interface Effect {
        /** OpenGlobalEditor: open global editor 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object OpenGlobalEditor : Effect


        /** OpenUnitSelector: open unit selector 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class OpenUnitSelector(val names: List<String>) : Effect


        /** AskJoin: ask join 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class AskJoin(val id: Int, val text: String) : Effect


        /** Info: info 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class Info(val text: String) : Effect
        /** Close: close 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object Close : Effect


        /** Join: join 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class Join(val id: Int, val camp: Int = 0, val order: Int = 0) : Effect


        /** Leave: leave 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class Leave(val id: Int, val camp: Int = 255, val order: Int = 0) : Effect


        /** Toast: toast 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class Toast(val text: String) : Effect
        /** Refresh: refresh 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object Refresh : Effect
        /** OpenLearnUnitSkill: open learn unit skill 관련 검증 상태와 동작을 제공하는 타입이다. */
        data object OpenLearnUnitSkill : Effect


        /** AskLeave: ask leave 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class AskLeave(val id: Int, val text: String) : Effect
    }

    /** joined: 검증 흐름에서 사용하는 값을 담는다. */
    private val joined = initial.associateByTo(linkedMapOf()) { it.id }
    /** pendingUnitId: pending unit id 값을 보관해 검증 흐름에서 사용한다. */
    var pendingUnitId: Int? = null
        private set


    /** button: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    fun button(tag: Int): List<Effect> = when (tag) {
        0 -> listOf(Effect.OpenGlobalEditor)
        1 -> listOf(Effect.OpenUnitSelector(unitNames))
        2 -> listOf(Effect.Close)
        3 -> buildList {
            for (id in 0 until minOf(27, unitNames.size)) if (id !in joined) {
                joined[id] = UnitRow(id, unitNames[id], false)
                add(Effect.Join(id)); add(Effect.Toast("${unitNames[id]} 대열에 합류합니다")); add(Effect.Refresh)
            }
        }

        4 -> listOf(Effect.OpenLearnUnitSkill)
        else -> emptyList()
    }


    /** selectUnit: select unit에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun selectUnit(id: Int): List<Effect> {
        if (id < 0) return emptyList()
        require(id in unitNames.indices)
        pendingUnitId = id
        val current = joined[id]
        if (current != null && !current.leave) return listOf(Effect.Info("이 무장은 이미 대기열에 있습니다."))
        val prompt = if (current?.leave == true) "그를 부활시키시겠습니까?"
        else "~하게 할까요?${unitNames[id]}팀에 합류할까요?"
        return listOf(Effect.AskJoin(id, prompt))
    }


    /** joinAnswer: join answer에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun joinAnswer(answer: Int): List<Effect> {
        val id = pendingUnitId ?: return emptyList()
        if (answer != 0) return emptyList()
        joined[id] = UnitRow(id, unitNames[id], false)
        return listOf(Effect.Join(id), Effect.Refresh)
    }


    /** tapRow: tap row에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun tapRow(rowIndex: Int): List<Effect> {
        val row = joined.values.elementAt(rowIndex)
        pendingUnitId = row.id
        return listOf(Effect.AskLeave(row.id, "~하게 할까요?${row.name}떠나다?"))
    }


    /** leaveAnswer: leave answer에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun leaveAnswer(answer: Int): List<Effect> {
        val id = pendingUnitId ?: return emptyList()
        if (answer != 0) return emptyList()
        val row = requireNotNull(joined[id])
        joined[id] = row.copy(leave = true)
        return listOf(Effect.Leave(id), Effect.Toast("${row.name}팀을 떠났습니다"), Effect.Refresh)
    }


    /** rows: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    fun rows(): List<UnitRow> = joined.values.toList()
}


/** EditRosterRoute: edit roster route 관련 검증 상태와 동작을 제공하는 타입이다. */
enum class EditRosterRoute(val key: String) {
    DEFAULT("edit4-default"), SELECT("edit4-select");

    companion object {

        /** parse: 외부 상태를 검증 경로 값으로 변환한다. */
        fun parse(state: String?): EditRosterRoute? = entries.firstOrNull { "hall-${it.key}-fixture" == state }
    }
}

/** HallEditRosterRoute: EDIT 조건의 HallMenu tag8 전달 경로를 Helper tag9와 분리해 표현한다. */
class HallEditRosterRoute(private val editEnabled: Boolean) {

    /** touch: 검증 입력을 현재 상태에 적용한다. */
    fun touch(tag: Int, touchEnd: Boolean): Boolean = touchEnd && editEnabled && tag == 8
}

/** EditRosterRenderEvents: HallMenu button8에서 Hall15로 이어지는 렌더 계약을 표현한다. */
object EditRosterRenderEvents {
    /** alpha: 렌더링 투명도 규칙을 담는다. */
    private val alpha = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")


    /** jsonl: 검증 상태를 JSONL 한 줄로 직렬화한다. */
    fun jsonl(route: EditRosterRoute): String {
        val log = RenderEventLog()
        val phase = "hall-${route.key}-stable"


        /** d: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun d(
            layer: String,
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            opacity: Float = 1f,
            blend: Any = listOf(770, 771)
        ) =
            log.draw(phase, layer, path, type, x, y, w, h, asset, opacity, blend, true, text)
        d(
            "HallLayer",
            "Canvas/Layer/map",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        d(
            "HallLayer",
            "Canvas/Layer/Panel_cancel",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "default_sprite_splash",
            opacity = .314f
        )
        d("EditLayer4", "Canvas/Layer/bg", "tiled-sprite", 420.686f, 22.5f, 647f, 755f, "Logo_12-1")
        d("EditLayer4", "Canvas/Layer/bg/bg1", "sprite", 420.686f, 728.75f, 647f, 48.7f, "bg1")
        d(
            "EditLayer4",
            "Canvas/Layer/bg/bg1/label",
            "label",
            692.286f,
            727.9f,
            103.8f,
            50.4f,
            text = "편성소",
            blend = alpha
        )
        val captions = listOf(
            Triple(
                floatArrayOf(434.336f, 655f, 135.7f, 52f),
                floatArrayOf(450.286f, 658.8f, 103.8f, 50.4f),
                "인덱스"
            ),
            Triple(floatArrayOf(570.186f, 655f, 308f, 52f), floatArrayOf(689.586f, 658.465f, 69.2f, 50.4f), "이름"),
            Triple(floatArrayOf(878.686f, 655f, 175f, 52f), floatArrayOf(931.586f, 658.465f, 69.2f, 50.4f), "상태")
        )
        captions.forEach { (box, label, text) ->
            d(
                "EditLayer4",
                "Canvas/Layer/bg/caption",
                "sliced-sprite",
                box[0],
                box[1],
                box[2],
                box[3],
                "box3"
            ); d(
            "EditLayer4",
            "Canvas/Layer/bg/caption/label",
            "label",
            label[0],
            label[1],
            label[2],
            label[3],
            text = text,
            blend = alpha
        )
        }
        d("EditLayer4", "Canvas/Layer/bg/vline", "sprite", 874.186f, 169.15f, 6f, 484.3f, "vline"); d(
            "EditLayer4",
            "Canvas/Layer/bg/vline",
            "sprite",
            567.186f,
            169.15f,
            6f,
            484.3f,
            "vline"
        )
        d("EditLayer4", "Canvas/Layer/bg/scrollview0", "sliced-sprite", 433.686f, 167f, 621f, 488f, "box5")

        /** Row: row 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class Row(
            val id: String,
            val name: String,
            val y: Float,
            val asset: String,
            val ix: Float,
            val nx: Float,
            val nw: Float
        )
        listOf(
            Row("0", "조조", 595f, "bg2", 491.061f, 689.586f, 69.2f),
            Row("157", "허자장", 535f, "885a69b4-08ed-4c78-8896-ffb04eb2bd20", 468.816f, 672.286f, 103.8f),
            Row("181", "병사 ", 475f, "bg2", 468.816f, 684.031f, 80.31f)
        ).forEach { row ->
            val p = "Canvas/Layer/bg/scrollview0/view/content/item"; d(
            "EditLayer4",
            p,
            "sprite",
            440.186f,
            row.y,
            606f,
            60f,
            row.asset
        )
            d(
                "EditLayer4",
                "$p/label0",
                "label",
                row.ix,
                row.y + 4.8f,
                if (row.id == "0") 22.25f else 66.74f,
                50.4f,
                text = row.id,
                blend = alpha
            )
            d("EditLayer4", "$p/label1", "label", row.nx, row.y + 4.8f, row.nw, 50.4f, text = row.name, blend = alpha)
            d("EditLayer4", "$p/label2", "label", 914.286f, row.y + 4.8f, 103.8f, 50.4f, text = "참전함", blend = alpha)
        }

        /** Button: button 관련 검증 상태와 동작을 제공하는 타입이다. */
        data class Button(
            val i: Int,
            val x: Float,
            val y: Float,
            val w: Float,
            val h: Float,
            val lx: Float,
            val ly: Float,
            val lw: Float,
            val lh: Float,
            val text: String
        )
        listOf(
            Button(0, 873.686f, 35.1f, 183f, 55.8f, 930.586f, 37.8f, 69.2f, 50.4f, "편집"),
            Button(1, 441.686f, 35.1f, 183f, 55.8f, 371.931f, 37.8f, 322.51f, 50.4f, "무장으로 합류합니다"),
            Button(2, 657.686f, 35.1f, 183f, 55.8f, 714.586f, 37.8f, 69.2f, 50.4f, "폐쇄"),
            Button(3, 441.186f, 102f, 408f, 56f, 335.656f, 104.8f, 619.06f, 50.4f, "원클릭으로 앞의 26명 무장을 모두 얻기"),
            Button(4, 873.686f, 102.1f, 183f, 55.8f, 890.431f, 104.8f, 149.51f, 50.4f, "특성 수정")
        ).forEach { b ->
            val p = "Canvas/Layer/bg/button${b.i}/Background"; d(
            "EditLayer4",
            p,
            "sliced-sprite",
            b.x,
            b.y,
            b.w,
            b.h,
            "box3"
        ); d("EditLayer4", "$p/Label", "label", b.lx, b.ly, b.lw, b.lh, text = b.text, blend = alpha)
        }
        return log.jsonl()
    }
}
