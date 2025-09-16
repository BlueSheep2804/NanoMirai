package dev.bluesheep.nanomirai.menu

import dev.bluesheep.nanomirai.block.entity.AssemblerBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.SlotItemHandler

class AssemblerMenu(containerId: Int, playerInv: Inventory, val access: ContainerLevelAccess, val items: ItemStackHandler) : AbstractContainerMenu(NanoMiraiMenu.NANOMACHINE_ASSEMBLER, containerId) {
    constructor(containerId: Int, playerInv: Inventory) : this(containerId, playerInv, ContainerLevelAccess.NULL, ItemStackHandler(AssemblerBlockEntity.SIZE))

    init {
        addContainerSlots(items)
        addPlayerInventorySlots(playerInv)
//        this.addSlot(Slot(items, 0, 8, 84))
    }

    // 0-9 Output + Input
    // 10-36 Player Inventory
    // 37-45 Hotbar
    override fun quickMoveStack(player: Player, quickMovedSlotIndex: Int): ItemStack {
        var quickMovedStack = ItemStack.EMPTY
        val quickMovedSlot = this.slots[quickMovedSlotIndex]

        if (quickMovedSlot.hasItem()) {
            val rawStack = quickMovedSlot.item
            quickMovedStack = rawStack.copy()

            if (quickMovedSlotIndex == 0) {
                if (!this.moveItemStackTo(rawStack, 10, 46, true)) {
                    return ItemStack.EMPTY
                }

                getSlot(0).onQuickCraft(rawStack, quickMovedStack)
            } else if (quickMovedSlotIndex in 10..45) {
                if (!this.moveItemStackTo(rawStack, 1, 10, false)) {
                    if (quickMovedSlotIndex < 37) {
                        if (!this.moveItemStackTo(rawStack, 37, 46, false)) {
                            return ItemStack.EMPTY
                        }
                    } else if (!this.moveItemStackTo(rawStack, 10, 37, false)) {
                        return ItemStack.EMPTY
                    }
                }
            } else if (!this.moveItemStackTo(rawStack, 10, 46, false)) {
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

    override fun stillValid(player: Player): Boolean {
        return stillValid(access, player, NanoMiraiBlocks.NANOMACHINE_ASSEMBLER)
    }

    private fun addPlayerInventorySlots(playerInv: Inventory) {
        for (row in 0 until 3) {
            for (col in 0 until 9) {
                this.addSlot(Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18))
            }
        }
        for (row in 0 until 9) {
            this.addSlot(Slot(playerInv, row, 8 + row * 18, 142))
        }
    }

    private fun addContainerSlots(container: ItemStackHandler) {
        this.addSlot(OutputSlotItemHandler(container, 9, 124, 35))
        for (row in 0 until 3) {
            for (col in 0 until 3) {
                this.addSlot(SlotItemHandler(container, col + row * 3, 30 + col * 18, 17 + row * 18))
            }
        }
    }
}
