package dev.bluesheep.nanomirai.compat.jei

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

object JeiUtil {
    val INFO_STYLE: Style = Style.EMPTY
        .withColor(ChatFormatting.AQUA)
        .withUnderlined(true)

    val NOT_CONSUMED: Component = Component.translatable("recipe.nanomirai.not_consumed")
        .withStyle(INFO_STYLE)

    val SYNTHESIZE: Component = Component.translatable("recipe.nanomirai.synthesize.interact")
        .withStyle(INFO_STYLE)
}