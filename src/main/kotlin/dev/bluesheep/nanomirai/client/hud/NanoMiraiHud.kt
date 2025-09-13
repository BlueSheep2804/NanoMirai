package dev.bluesheep.nanomirai.client.hud

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.registry.NanoMiraiAttachmentTypes.SWARM
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = NanoMirai.ID)
object NanoMiraiHud {
    const val SWARM_COUNT_LANG_KEY = "nanomirai.hud.swarm_count"

    @SubscribeEvent
    fun hud(event: RegisterGuiLayersEvent) {
        event.registerAboveAll(rl("goggles_hud"), ::render)
    }

    fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
        val player = Minecraft.getInstance().player
        if (player == null) return
        if (player.inventory.getArmor(3).`is`(NanoMiraiItems.GOGGLES)) {
            val swarm = player.getData(SWARM)
            drawStringWithBackground(guiGraphics, swarmCountComponent(swarm, NanoTier.SEED), 10, 16)
            drawStringWithBackground(guiGraphics, swarmCountComponent(swarm, NanoTier.MATRIX), 10, 32)
            drawStringWithBackground(guiGraphics, swarmCountComponent(swarm, NanoTier.SINGULARITY), 10, 48)
        }
    }

    private fun swarmCountComponent(swarm: List<Int>, tier: NanoTier): Component {
        return Component.translatable(SWARM_COUNT_LANG_KEY, tier.swarmItem.getName(ItemStack.EMPTY), swarm[tier.tierLevel])
    }

    private fun drawStringWithBackground(guiGraphics: GuiGraphics, text: Component, x: Int, y: Int) {
        val font = Minecraft.getInstance().font
        val width = font.width(text)
        guiGraphics.fill(x - 2, y - 2, x + width + 2, y + font.lineHeight + 2, 0x55000000)
        guiGraphics.drawString(font, text, x, y, 0xFFFFFF)
    }
}
