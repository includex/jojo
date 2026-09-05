package com.jojo.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertSame

class BattlePreparationInputLifecycleTest {
    @Test
    fun `show installs the screen-owned processor`() {
        val input = InputSlot()
        val processor = InputAdapter()

        BattlePreparationInputConnection.install(input.api, processor)

        assertSame(processor, input.processor)
    }

    @Test
    fun `hide and dispose release the processor only while the screen owns the slot`() {
        val input = InputSlot()
        val processor = InputAdapter()
        val nextScreenProcessor = InputAdapter()

        BattlePreparationInputConnection.install(input.api, processor)
        BattlePreparationInputConnection.release(input.api, processor)
        assertNull(input.processor)

        BattlePreparationInputConnection.install(input.api, processor)
        input.processor = nextScreenProcessor
        BattlePreparationInputConnection.release(input.api, processor)

        assertSame(nextScreenProcessor, input.processor)
    }

    private class InputSlot : InvocationHandler {
        var processor: InputProcessor? = null
        val api: Input = Proxy.newProxyInstance(
            Input::class.java.classLoader,
            arrayOf(Input::class.java),
            this,
        ) as Input

        override fun invoke(proxy: Any, method: Method, arguments: Array<out Any?>?): Any? = when (method.name) {
            "getInputProcessor" -> processor
            "setInputProcessor" -> {
                processor = arguments?.firstOrNull() as InputProcessor?
                null
            }
            else -> defaultValue(method.returnType)
        }

        private fun defaultValue(type: Class<*>): Any? = when (type) {
            java.lang.Boolean.TYPE -> false
            java.lang.Byte.TYPE -> 0.toByte()
            java.lang.Short.TYPE -> 0.toShort()
            java.lang.Integer.TYPE -> 0
            java.lang.Long.TYPE -> 0L
            java.lang.Float.TYPE -> 0f
            java.lang.Double.TYPE -> 0.0
            java.lang.Character.TYPE -> '\u0000'
            else -> null
        }
    }
}
