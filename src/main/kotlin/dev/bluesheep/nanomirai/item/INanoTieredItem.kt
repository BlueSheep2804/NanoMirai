package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

interface INanoTieredItem {
    fun getTieredName(stack: ItemStack, nameComponent: Component): Component {
        return nameComponent.copy().append(" ").append(tier.nameComponent)
    }

    val tier: NanoTier
}