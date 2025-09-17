package dev.bluesheep.nanomirai.client.hud

import dev.bluesheep.nanomirai.registry.NanoMiraiAttachmentTypes.DEPLOYED_NANOMACHINES
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

object NanoMiraiHud {
    const val DEPLOYED_NANOMACHINES_LANG_KEY = "nanomirai.hud.deployed_nanomachine_count"

    fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
        val player = Minecraft.getInstance().player
        if (player == null) return
        if (player.inventory.getArmor(3).`is`(NanoMiraiItems.GOGGLES)) {
            val deployed = player.getData(DEPLOYED_NANOMACHINES)
            drawStringWithBackground(guiGraphics, deployedNanomachineComponent(deployed, NanoTier.SEED), 10, 16)
            drawStringWithBackground(guiGraphics, deployedNanomachineComponent(deployed, NanoTier.MATRIX), 10, 32)
            drawStringWithBackground(guiGraphics, deployedNanomachineComponent(deployed, NanoTier.SINGULARITY), 10, 48)
        }
    }

    private fun deployedNanomachineComponent(deployed: List<Int>, tier: NanoTier): Component {
        return Component.translatable(DEPLOYED_NANOMACHINES_LANG_KEY, tier.item.getName(ItemStack.EMPTY), deployed[tier.tierLevel])
    }

    private fun drawStringWithBackground(guiGraphics: GuiGraphics, text: Component, x: Int, y: Int) {
        val font = Minecraft.getInstance().font
        val width = font.width(text)
        guiGraphics.fill(x - 2, y - 2, x + width + 2, y + font.lineHeight + 2, 0x55000000)
        guiGraphics.drawString(font, text, x, y, 0xFFFFFF)
    }
}
