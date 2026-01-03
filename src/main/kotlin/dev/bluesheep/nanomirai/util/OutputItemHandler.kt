package dev.bluesheep.nanomirai.util

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class OutputItemHandler(itemHandler: IItemHandler, outputSlot: Int) : InputSingleItemHandler(itemHandler, outputSlot) {
    override fun insertItem(
        slot: Int,
        stack: ItemStack,
        simulate: Boolean
    ): ItemStack {
        return stack
    }
}