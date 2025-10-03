package dev.bluesheep.nanomirai.compat.jei

import com.mojang.blaze3d.systems.RenderSystem
import mezz.jei.api.gui.drawable.IDrawable
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack

// Original: Create
// https://github.com/Creators-of-Create/Create/blob/d943a866c5b24f79a94427dff4deb3b99261f15c/src/main/java/com/simibubi/create/compat/jei/DoubleItemIcon.java
class DoubleItemIcon(val primaryStack: ItemStack, val secondaryStack: ItemStack) : IDrawable {
    override fun getWidth(): Int {
        return 18
    }

    override fun getHeight(): Int {
        return 18
    }

    override fun draw(guiGraphics: GuiGraphics, xOffset: Int, yOffset: Int) {
        val matrixStack = guiGraphics.pose()
        RenderSystem.enableDepthTest()
        matrixStack.pushPose()
        matrixStack.translate(xOffset.toFloat(), yOffset.toFloat(), 0f)

        matrixStack.pushPose()
        matrixStack.translate(1f, 1f, 0f)
        guiGraphics.renderFakeItem(primaryStack, 0, 0)
        matrixStack.popPose()

        matrixStack.pushPose()
        matrixStack.translate(1f, 7.5f, 100f)
        matrixStack.scale(.6f, .6f, .6f)
        guiGraphics.renderFakeItem(secondaryStack, 0, 0)
        matrixStack.popPose()

        matrixStack.popPose()
    }
}