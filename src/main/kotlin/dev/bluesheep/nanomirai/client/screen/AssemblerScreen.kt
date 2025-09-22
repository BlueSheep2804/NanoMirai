package dev.bluesheep.nanomirai.client.screen

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.menu.AssemblerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class AssemblerScreen(menu: AssemblerMenu, playerInventory: Inventory, title: Component) : AbstractContainerScreen<AssemblerMenu>(menu, playerInventory, title) {
    companion object {
        val BG_LOCATION: ResourceLocation = rl("textures/gui/container/assembler.png")
        val ARROW_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/assembler/progress.png")
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        guiGraphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight)

        guiGraphics.blit(ARROW_LOCATION, leftPos + 89, topPos + 34, 0F, 0F, getProgress(), 16, 24, 16)
    }

    private fun getProgress(): Int {
        val progress = menu.data.get(0)
        val maxProgress = menu.data.get(1)
        return if (maxProgress == 0) 0 else progress * 24 / maxProgress
    }
}
