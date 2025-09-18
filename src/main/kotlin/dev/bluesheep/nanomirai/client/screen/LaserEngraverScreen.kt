package dev.bluesheep.nanomirai.client.screen

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.menu.LaserEngraverMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class LaserEngraverScreen(menu: LaserEngraverMenu, playerInventory: Inventory, title: Component) : AbstractContainerScreen<LaserEngraverMenu>(menu, playerInventory, title) {
    companion object {
        val BG_LOCATION: ResourceLocation = rl("textures/gui/container/laser_engraver.png")
        val ARROW_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/laser_engraver/progress.png")
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        guiGraphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight)

        guiGraphics.blit(ARROW_LOCATION, leftPos + 76, topPos + 47, 0F, 0F, getProgress(), 16, 24, 16)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }

    private fun getProgress(): Int {
        val progress = menu.data.get(0)
        val maxProgress = menu.data.get(1)
        return if (maxProgress == 0) 0 else progress * 24 / maxProgress
    }
}
