package dev.bluesheep.nanomirai.menu

import dev.bluesheep.nanomirai.block.entity.AssemblerBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.SlotItemHandler

class AssemblerMenu(containerId: Int, playerInv: Inventory, val access: ContainerLevelAccess, val blockEntity: AssemblerBlockEntity, val data: ContainerData) : AbstractContainerMenu(NanoMiraiMenu.ASSEMBLER, containerId) {
    constructor(containerId: Int, playerInv: Inventory, extraData: FriendlyByteBuf) : this(
        containerId,
        playerInv,
        ContainerLevelAccess.NULL,
        playerInv.player.level().getBlockEntity(extraData.readBlockPos()) as AssemblerBlockEntity,
        SimpleContainerData(2)
    )

    val playerInventoryIndex = blockEntity.itemHandler.slots
    val hotbarIndex = playerInventoryIndex + 27
    val hotbarIndexEnd = hotbarIndex + 9

    init {
        addContainerSlots(blockEntity.itemHandler)
        addPlayerInventorySlots(playerInv)
        addDataSlots(data)
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
        this.addSlot(OutputSlotItemHandler(container, 0, 124, 35))
        for (row in 0 until 3) {
            for (col in 0 until 3) {
                this.addSlot(SlotItemHandler(container,  1 + col + row * 3, 30 + col * 18, 17 + row * 18))
            }
        }
    }
}
