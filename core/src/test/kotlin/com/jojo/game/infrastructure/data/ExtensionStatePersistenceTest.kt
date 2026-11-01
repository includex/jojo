package com.jojo.game.infrastructure.data

import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.rules.ExtensionResourceDeclaration
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ExtensionStatePersistenceTest {
    private val energy = ExtensionResourceDeclaration("yitian", "innerEnergy", 10, 0, 100, perCharacter = true)
    private val charge = ExtensionResourceDeclaration("yitian", "charge", 2, 0, 5)

    @Test
    fun `resource writes are bounded and keyed by stable character id`() {
        val state = CampaignState(extensionResources = listOf(energy, charge))
        assertEquals(10, state.extensionState.read("yitian.innerEnergy", 42))
        state.extensionState.write("yitian.innerEnergy", 88, 42)
        state.extensionState.write("yitian.innerEnergy", 7, 43)
        assertEquals(88, state.extensionState.read("yitian.innerEnergy", 42))
        assertEquals(7, state.extensionState.read("yitian.innerEnergy", 43))
        assertFailsWith<IllegalArgumentException> { state.extensionState.write("yitian.innerEnergy", 101, 42) }
        assertFailsWith<IllegalArgumentException> { state.extensionState.write("yitian.innerEnergy", 1) }
        assertEquals(88, state.extensionState.read("yitian.innerEnergy", 42))
        state.reset()
        assertEquals(10, state.extensionState.read("yitian.innerEnergy", 42))
    }

    @Test
    fun `versioned extension values round trip with campaign runtime`() {
        val original = CampaignState(extensionResources = listOf(energy, charge))
        original.extensionState.write("yitian.innerEnergy", 33, 42)
        original.extensionState.write("yitian.charge", 4)
        val encoded = CampaignRuntimeStateCodec(original).encode()
        val restored = CampaignState(extensionResources = listOf(energy, charge))
        assertTrue(CampaignRuntimeStateCodec(restored).restore(encoded))
        assertEquals(33, restored.extensionState.read("yitian.innerEnergy", 42))
        assertEquals(10, restored.extensionState.read("yitian.innerEnergy", 43))
        assertEquals(4, restored.extensionState.read("yitian.charge"))
    }

    @Test
    fun `saved random stream resumes with the same next draws`() {
        val original = CampaignState(extensionResources = listOf(energy, charge))
        original.extensionState.nextRandomInt(10)
        original.extensionState.nextRandomInt(7)
        val encoded = CampaignRuntimeStateCodec(original).encode()
        val expected = List(5) { original.extensionState.nextRandomInt(97) }
        val restored = CampaignState(extensionResources = listOf(energy, charge))
        assertTrue(CampaignRuntimeStateCodec(restored).restore(encoded))
        assertEquals(expected, List(5) { restored.extensionState.nextRandomInt(97) })
        restored.reset()
        assertEquals(48_271 % 97, restored.extensionState.nextRandomInt(97))
    }

    @Test
    fun `old saves default and invalid future or out of range values cannot mutate state`() {
        val state = CampaignState(extensionResources = listOf(energy, charge))
        val codec = CampaignRuntimeStateCodec(state)
        state.extensionState.write("yitian.charge", 4)
        state.addMoney(6)
        fun encoded(json: String) = Base64.getEncoder().encodeToString(json.toByteArray())
        for (payload in listOf(
            """{"money":99,"extensionState":{"version":2}}""",
            """{"money":99,"extensionState":{"version":1,"global":{"yitian.charge":8}}}""",
            """{"money":99,"extensionState":{"version":1,"global":{"yitian.innerEnergy":5}}}""",
            """{"money":99,"extensionState":{"version":1,"characters":{"42":{"yitian.unknown":1}}}}""",
            """{"money":99,"extensionState":{"version":1,"global":{"yitian.charge":3,"yitian.charge":4}}}""",
            """{"money":99,"extensionState":{"version":1,"version":1,"global":{"yitian.charge":3}}}""",
            """{"money":99,"extensionState":{"version":1,"global":{"yitian.charge":"3"}}}""",
            """{"money":99,"extensionState":{"version":1,"randomState":0}}""",
            """{"money":99,"extensionState":{"version":1,"randomState":2147483647}}""",
        )) {
            assertFalse(codec.canRestore(encoded(payload)))
            assertFalse(codec.restore(encoded(payload)))
            assertEquals(6, state.money)
            assertEquals(4, state.extensionState.read("yitian.charge"))
        }
        assertTrue(codec.restore(encoded("""{"money":7}""")))
        assertEquals(2, state.extensionState.read("yitian.charge"))
        assertEquals(1, state.extensionState.snapshot().randomState)
    }
}
