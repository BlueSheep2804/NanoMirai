package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

interface INanoTieredItem {
    fun appendTierTooltip(stack: ItemStack, context: Any, tooltipComponents: MutableList<Component?>, tooltipFlag: TooltipFlag) {
        val rarity = stack.get(DataComponents.RARITY)
        if (rarity != null) {
            val tier = NanoTier.fromRarity(rarity)
            val component = Component.translatable(
                "item.nanomirai.tooltip.nano_tier",
                tier.nameComponent
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

    fun otherStackedOnMe(
        stack: ItemStack,
        other: ItemStack,
        action: ClickAction
    ): Boolean {
        if (action != ClickAction.SECONDARY) return false
        if (!other.`is`(NanoMiraiItems.REPAIR_NANO)) return false
        if (stack.isDamaged) repair(stack, other)
        return true
    }

    fun repair(stack: ItemStack, material: ItemStack) {
        stack.damageValue--
        material.shrink(1)
    }
}