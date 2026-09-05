package com.jojo.game

import com.badlogic.gdx.Preferences
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class GamePreferenceProviderTest {
    @Test
    fun `automated stores are shared by name within one game and never delegate to disk`() {
        var persistentRequests = 0
        val provider = GamePreferenceProvider(automatedRun = true) {
            persistentRequests++
            InMemoryPreferences()
        }

        val campaign = provider.campaign()
        campaign.putString("state", "isolated").flush()

        assertSame(campaign, provider.campaign())
        assertEquals("isolated", provider.campaign().getString("state"))
        assertNotSame(campaign, provider.settings())
        assertEquals(0, persistentRequests)
        assertEquals("jojo-game-campaign", GamePreferenceNamespaces.CAMPAIGN)
        assertEquals("jojo-game-settings", GamePreferenceNamespaces.SETTINGS)
    }

    @Test
    fun `separate automated games cannot observe each other's preferences`() {
        val first = GamePreferenceProvider(true) { error("automated run accessed persistent preferences") }
        val second = GamePreferenceProvider(true) { error("automated run accessed persistent preferences") }

        first.get("jojo-auto-battle").putInteger("TUOGUAN", 1).flush()

        assertEquals(1, first.get("jojo-auto-battle").getInteger("TUOGUAN"))
        assertEquals(0, second.get("jojo-auto-battle").getInteger("TUOGUAN", 0))
    }

    @Test
    fun `campaign writes stay inside the automated game store`() {
        val persistentCampaign = InMemoryPreferences().apply {
            putString("campaign", "player-save")
            putInteger("SAVE_PAGE", 4)
        }
        val provider = GamePreferenceProvider(automatedRun = true) { persistentCampaign }
        val campaign = CampaignStore(provider.campaign())

        campaign.newGame()
        campaign.incStage()
        campaign.savePage(0)
        campaign.saveSlot(0)

        assertEquals("player-save", persistentCampaign.getString("campaign"))
        assertEquals(4, persistentCampaign.getInteger("SAVE_PAGE"))
        assertEquals(false, persistentCampaign.contains("save-slot-0"))
        assertEquals(1, campaign.snapshot.stage)
        assertEquals(0, campaign.savedPage())
    }

    @Test
    fun `jump scene can replace the absolute source model stage`() {
        val campaign = CampaignStore(InMemoryPreferences())

        campaign.incStage()
        campaign.setStage(92)

        assertEquals(92, campaign.snapshot.stage)
    }

    @Test
    fun `interactive games retain and share platform preferences`() {
        val disk = linkedMapOf<String, Preferences>()
        val provider = GamePreferenceProvider(automatedRun = false) { name ->
            disk.getOrPut(name) { InMemoryPreferences() }
        }

        val settings = provider.settings()
        settings.putInteger("GAME_SETTING", 15).flush()

        assertSame(settings, provider.settings())
        assertSame(disk.getValue(GamePreferenceNamespaces.SETTINGS), settings)
        assertEquals(15, provider.settings().getInteger("GAME_SETTING"))
    }
}
