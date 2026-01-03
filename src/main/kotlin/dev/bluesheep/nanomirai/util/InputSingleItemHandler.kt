package dev.bluesheep.nanomirai.util

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

open class InputSingleItemHandler(val itemHandler: IItemHandler, val slotId: Int) : IItemHandler {
    override fun getSlots(): Int {
        return 1
    }

    override fun getStackInSlot(slot: Int): ItemStack {
        return itemHandler.getStackInSlot(slotId)
    }

    override fun insertItem(
        slot: Int,
        stack: ItemStack,
        simulate: Boolean
    ): ItemStack {
        return itemHandler.insertItem(
            slotId,
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
            slotId,
            amount,
            simulate
        )
    }

    override fun getSlotLimit(slot: Int): Int {
        return itemHandler.getSlotLimit(slotId)
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return itemHandler.isItemValid(slotId, stack)
    }
}