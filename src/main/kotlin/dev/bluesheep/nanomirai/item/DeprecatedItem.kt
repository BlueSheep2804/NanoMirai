package dev.bluesheep.nanomirai.item

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class DeprecatedItem(properties: Properties, migrationMessage: MutableComponent) : Item(properties) {
    companion object {
        val DEPRECATED_MESSAGE: MutableComponent = Component.translatable("nanomirai.tooltip.deprecated").withStyle(ChatFormatting.RED)
    }
    private val migrationMessage: Component = migrationMessage.withStyle(ChatFormatting.RED)

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
        tooltipComponents.add(DEPRECATED_MESSAGE)
        tooltipComponents.add(migrationMessage)
    }
}