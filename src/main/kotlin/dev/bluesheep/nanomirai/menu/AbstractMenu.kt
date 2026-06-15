package dev.bluesheep.nanomirai.menu

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity

abstract class AbstractMenu<BE: BlockEntity>(
    slotSize: Int,
    menuType: MenuType<*>?,
    containerId: Int,
    playerInv: Inventory,
    val blockEntity: BE,
    isGuiLarge: Boolean = false
) : AbstractContainerMenu(menuType, containerId) {
    protected val playerInventoryIndex = slotSize
    protected val hotbarIndex = playerInventoryIndex + 27
    protected val hotbarIndexEnd = hotbarIndex + 9

    init {
        addContainerSlots()
        addPlayerInventorySlots(playerInv, if (isGuiLarge) 55 else 0)
    }

    abstract fun addContainerSlots()

    private fun addPlayerInventorySlots(playerInv: Inventory, yOffset: Int) {
        for (row in 0 until 3) {
            for (col in 0 until 9) {
                this.addSlot(Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + yOffset + row * 18))
            }
        }
        for (row in 0 until 9) {
            this.addSlot(Slot(playerInv, row, 8 + row * 18, 142 + yOffset))
        }
    }

    override fun quickMoveStack(player: Player, quickMovedSlotIndex: Int): ItemStack {
        var quickMovedStack = ItemStack.EMPTY
        val quickMovedSlot = this.slots[quickMovedSlotIndex]

        if (quickMovedSlot.hasItem()) {
            val rawStack = quickMovedSlot.item
            quickMovedStack = rawStack.copy()

            if (quickMovedSlotIndex == 0) {
                if (!this.moveItemStackTo(rawStack, playerInventoryIndex, hotbarIndexEnd, true)) {
                    return ItemStack.EMPTY
                }

                getSlot(0).onQuickCraft(rawStack, quickMovedStack)
            } else if (quickMovedSlotIndex in playerInventoryIndex..hotbarIndexEnd) {
                if (!this.moveItemStackTo(rawStack, 0, playerInventoryIndex, false)) {
                    if (quickMovedSlotIndex < hotbarIndex) {
                        if (!this.moveItemStackTo(rawStack, hotbarIndex, hotbarIndexEnd, false)) {
                            return ItemStack.EMPTY
                        }
                    } else if (!this.moveItemStackTo(rawStack, playerInventoryIndex, hotbarIndex, false)) {
                        return ItemStack.EMPTY
                    }
                }
            } else if (!this.moveItemStackTo(rawStack, playerInventoryIndex, hotbarIndexEnd, false)) {
                return ItemStack.EMPTY
            }

            if (rawStack.isEmpty) {
                quickMovedSlot.set(ItemStack.EMPTY)
            } else {
                quickMovedSlot.setChanged()
            }

            if (rawStack.count == quickMovedStack.count) {
                return ItemStack.EMPTY
            }
            quickMovedSlot.onTake(player, rawStack)
        }
        return quickMovedStack
    }
}