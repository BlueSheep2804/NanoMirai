package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

interface INanoTieredItem {
    fun appendTierTooltip(stack: ItemStack, context: Any, tooltipComponents: MutableList<Component?>, tooltipFlag: TooltipFlag) {
        val rarity = stack.get(DataComponents.RARITY)
        if (rarity != null) {
            val tier = NanoTier.fromRarity(rarity)
            val component = Component.translatable(
                "item.nanomirai.tooltip.nano_tier",
                Component.translatable("nanomirai.nano_tier.${tier.name.lowercase()}").withStyle(rarity.styleModifier)
            )
            if (tooltipFlag.isAdvanced) {
                component.append(
                    Component.translatable("item.nanomirai.tooltip.nano_tier.num", tier.ordinal)
                        .withStyle(ChatFormatting.GRAY)
                )
            }
            tooltipComponents.add(1, component)
        }
    }
}