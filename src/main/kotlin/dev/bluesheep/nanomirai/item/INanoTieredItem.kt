package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.item.ItemStack

interface INanoTieredItem {
    fun getTieredName(stack: ItemStack, nameComponent: Component): Component {
        return nameComponent.copy().append(" ").append(NanoTier.fromRarity(stack.rarity).nameComponent)
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