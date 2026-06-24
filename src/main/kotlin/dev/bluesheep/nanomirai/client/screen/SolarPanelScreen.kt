package dev.bluesheep.nanomirai.client.screen

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.menu.SolarPanelMenu
import dev.bluesheep.nanomirai.util.EnergyFormat
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.util.FastColor
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory

class SolarPanelScreen(
    menu: SolarPanelMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<SolarPanelMenu>(
    menu,
    playerInventory,
    title
) {
    companion object {
        val BG_LOCATION = rl("textures/gui/container/solar_panel.png")
        val WORKING_ICON = rl("textures/gui/sprites/container/solar_panel/working.png")
        val ERROR_ICON = rl("textures/gui/sprites/container/solar_panel/error.png")
        const val MONITOR_OFFSET_X = 11
        const val MONITOR_OFFSET_Y = 23
    }

    var monitorX: Int = 0
    var monitorY: Int = 0

    override fun init() {
        super.init()
        monitorX = guiLeft + MONITOR_OFFSET_X
        monitorY = guiTop + MONITOR_OFFSET_Y
    }

    override fun renderBg(guiGraphics: GuiGraphics, p1: Float, p2: Int, p3: Int) {
        guiGraphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        val stored = menu.data.get(0)
        val maxStored = menu.data.get(1)
        val level = menu.data.get(2)
        renderStrings(guiGraphics, listOf(
            containerComponent("stored_energy", EnergyFormat.format(stored)),
            containerComponent("energy_capacity", EnergyFormat.format(maxStored)),
            levelComponent(level),
        ))

        val left = leftPos + 125
        val top = topPos + 20
        guiGraphics.blit(
            if (level > 0) WORKING_ICON else ERROR_ICON,
            left,
            top,
            0F,
            0F,
            16,
            16,
            16,
            16
        )
        if (mouseX in left..left + 16 && mouseY in top..top + 16) {
            guiGraphics.renderTooltip(font, statusComponent(level), mouseX, mouseY)
        }

        guiGraphics.fill(
            leftPos + 156,
            topPos + 73 - Mth.floor(53 * (stored / maxStored.toFloat())),
            leftPos + 164,
            topPos + 73,
            FastColor.ARGB32.color(255, ChatFormatting.AQUA.color!!)
        )

        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderTooltip(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        super.renderTooltip(guiGraphics, mouseX, mouseY)


    }

    private fun renderStrings(guiGraphics: GuiGraphics, strings: List<Component>) {
        strings.forEachIndexed { index, component ->
            guiGraphics.drawScrollingString(
                font,
                component,
                monitorX,
                monitorX + 100,
                monitorY + (minecraft!!.font.lineHeight + 1) * index,
                16119285
            )
        }
    }

    private fun levelComponent(level: Int): Component {
        return containerComponent(
            "level",
            if (level > 0) level else 0
        )
    }

    private fun statusComponent(level: Int): Component {
        return when (level) {
            -1 -> containerComponent("status.obstructed")
            -2 -> containerComponent("status.insufficient_sunlight")
            else -> containerComponent("status.working")
        }
    }

    private fun containerComponent(path: String, vararg args: Any): Component {
        return Component.translatable("container.nanomirai.solar_panel.${path}", *args)
    }
}
