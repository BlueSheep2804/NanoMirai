package dev.bluesheep.nanomirai.menu

import dev.bluesheep.nanomirai.block.entity.LaserEngraverBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import dev.bluesheep.nanomirai.registry.NanoMiraiTags
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

class LaserEngraverMenu(containerId: Int, playerInv: Inventory, val blockEntity: LaserEngraverBlockEntity, val data: ContainerData) : AbstractContainerMenu(NanoMiraiMenu.LASER_ENGRAVER, containerId) {
    constructor(containerId: Int, playerInv: Inventory, extraData: FriendlyByteBuf) : this(
        containerId,
        playerInv,
        playerInv.player.level().getBlockEntity(extraData.readBlockPos()) as LaserEngraverBlockEntity,
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
        return stillValid(ContainerLevelAccess.create(player.level(), blockEntity.blockPos), player, NanoMiraiBlocks.LASER_ENGRAVER)
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
        this.addSlot(OutputSlotItemHandler(container, 0, 113, 48))
        this.addSlot(SlotItemHandler(container, 1, 51, 48))
        this.addSlot(object : SlotItemHandler(container, 2, 80, 22) {
            override fun getMaxStackSize(): Int {
                return 1
            }

            override fun getMaxStackSize(stack: ItemStack): Int {
                return 1
            }

            override fun mayPlace(stack: ItemStack): Boolean {
                return stack.`is`(NanoMiraiTags.LENSES)
            }
        })
    }
}
