package dev.bluesheep.nanomirai.client.screen

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.menu.NanoLabMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class NanoLabScreen(menu: NanoLabMenu, playerInventory: Inventory, title: Component) : AbstractContainerScreen<NanoLabMenu>(menu, playerInventory, title) {
    companion object {
        val BG_LOCATION: ResourceLocation = rl("textures/gui/container/nano_lab.png")
        val ARROW_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/nano_lab/progress.png")
    }

    init {
        imageHeight = 222
        inventoryLabelY = imageHeight - 94
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val posX = (this.width - this.imageWidth) / 2
        val posY = (this.height - this.imageHeight) / 2

        guiGraphics.blit(BG_LOCATION, posX, posY, 0, 0, imageWidth, imageHeight)

        guiGraphics.blit(ARROW_LOCATION, posX + 110, posY + 63, 0F, 0F, getProgress(), 16, 24, 16)
    }

    private fun getProgress(): Int {
        val progress = menu.data.get(0)
        val maxProgress = menu.data.get(1)
        return if (maxProgress == 0) 0 else progress * 24 / maxProgress
    }
}
