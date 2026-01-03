package dev.bluesheep.nanomirai.util

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class InputItemHandler(val itemHandler: IItemHandler) : IItemHandler {
    override fun getSlots(): Int {
        return itemHandler.slots - 1
    }

    override fun getStackInSlot(slot: Int): ItemStack {
        return itemHandler.getStackInSlot(slot + 1)
    }

    override fun insertItem(
        slot: Int,
        stack: ItemStack,
        simulate: Boolean
    ): ItemStack {
        return itemHandler.insertItem(
            slot + 1,
            stack,
            simulate
        )
    }

    override fun extractItem(
        slot: Int,
        amount: Int,
        simulate: Boolean
    ): ItemStack {
        return itemHandler.extractItem(
            slot + 1,
            amount,
            simulate
        )
    }

    override fun getSlotLimit(slot: Int): Int {
        return itemHandler.getSlotLimit(slot + 1)
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return itemHandler.isItemValid(slot + 1, stack)
    }
}