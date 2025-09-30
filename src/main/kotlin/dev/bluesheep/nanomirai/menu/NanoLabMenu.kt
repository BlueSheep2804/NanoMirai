package dev.bluesheep.nanomirai.menu

import dev.bluesheep.nanomirai.block.entity.NanoLabBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.SlotItemHandler

class NanoLabMenu(containerId: Int, playerInv: Inventory, val blockEntity: NanoLabBlockEntity, val data: ContainerData) : AbstractContainerMenu(NanoMiraiMenu.NANO_LAB, containerId) {
    constructor(containerId: Int, playerInv: Inventory, extraData: FriendlyByteBuf) : this(
        containerId,
        playerInv,
        playerInv.player.level().getBlockEntity(extraData.readBlockPos()) as NanoLabBlockEntity,
        SimpleContainerData(2)
    )

    init {
        addContainerSlots(blockEntity.itemHandler)
        addPlayerInventorySlots(playerInv)
        addDataSlots(data)
    }

    // 0-7 Input + Lens + Output
    // 8-34 Player Inventory
    // 35-43 Hotbar
    override fun quickMoveStack(
        player: Player,
        quickMovedSlotIndex: Int
    ): ItemStack {
        var quickMovedStack = ItemStack.EMPTY
        val quickMovedSlot = this.slots[quickMovedSlotIndex]

        if (quickMovedSlot.hasItem()) {
            val rawStack = quickMovedSlot.item
            quickMovedStack = rawStack.copy()

            if (quickMovedSlotIndex == 0) {
                if (!this.moveItemStackTo(rawStack, 8, 44, true)) {
                    return ItemStack.EMPTY
                }

                getSlot(0).onQuickCraft(rawStack, quickMovedStack)
            } else if (quickMovedSlotIndex in 8..44) {
                if (!this.moveItemStackTo(rawStack, 0, 8, false)) {
                    if (quickMovedSlotIndex < 35) {
                        if (!this.moveItemStackTo(rawStack, 35, 44, false)) {
                            return ItemStack.EMPTY
                        }
                    } else if (!this.moveItemStackTo(rawStack, 8, 35, false)) {
                        return ItemStack.EMPTY
                    }
                }
            } else if (!this.moveItemStackTo(rawStack, 8, 44, false)) {
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
        return stillValid(ContainerLevelAccess.create(player.level(), blockEntity.blockPos), player, NanoMiraiBlocks.NANO_LAB)
    }

    private fun addPlayerInventorySlots(playerInv: Inventory) {
        for (row in 0 until 3) {
            for (col in 0 until 9) {
                this.addSlot(Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 139 + row * 18))
            }
        }
        for (row in 0 until 9) {
            this.addSlot(Slot(playerInv, row, 8 + row * 18, 197))
        }
    }

    private fun addContainerSlots(container: ItemStackHandler) {
        this.addSlot(SlotItemHandler(container, 0, 53, 63))

        this.addSlot(SlotItemHandler(container, 1, 26, 27))
        this.addSlot(SlotItemHandler(container, 2, 17, 63))
        this.addSlot(SlotItemHandler(container, 3, 26, 99))

        this.addSlot(SlotItemHandler(container, 4, 80, 27))
        this.addSlot(SlotItemHandler(container, 5, 89, 63))
        this.addSlot(SlotItemHandler(container, 6, 80, 99))

        this.addSlot(SlotItemHandler(container, 7, 143, 63))
    }
}