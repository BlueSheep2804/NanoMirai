package dev.bluesheep.nanomirai.client.screen

import dev.bluesheep.nanomirai.menu.AssemblerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class AssemblerScreen(menu: AssemblerMenu, playerInventory: Inventory, title: Component) : AbstractContainerScreen<AssemblerMenu>(menu, playerInventory, title) {
    companion object {
        val BG_LOCATION: ResourceLocation = ResourceLocation.withDefaultNamespace("textures/gui/container/crafting_table.png")
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        guiGraphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight)
    }
}