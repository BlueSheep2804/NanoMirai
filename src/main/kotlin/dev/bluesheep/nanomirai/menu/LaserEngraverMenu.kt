package dev.bluesheep.nanomirai.menu

import dev.bluesheep.nanomirai.block.entity.LaserEngraverBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiItemTags
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.SlotItemHandler

class LaserEngraverMenu(
    containerId: Int,
    playerInv: Inventory,
    blockEntity: LaserEngraverBlockEntity,
    val data: ContainerData
) : AbstractMenu<LaserEngraverBlockEntity>(
    blockEntity.itemHandler.slots,
    NanoMiraiMenu.LASER_ENGRAVER,
    containerId,
    playerInv,
    blockEntity
) {
    constructor(containerId: Int, playerInv: Inventory, extraData: FriendlyByteBuf) : this(
        containerId,
        playerInv,
        playerInv.player.level().getBlockEntity(extraData.readBlockPos()) as LaserEngraverBlockEntity,
        SimpleContainerData(2)
    )

    init {
        addDataSlots(data)
    }

    override fun addContainerSlots() {
        val container = blockEntity.itemHandler
        this.addSlot(OutputSlotItemHandler(container, 0, 113, 48))
        this.addSlot(object : SlotItemHandler(container, 1, 80, 22) {
            override fun getMaxStackSize(): Int {
                return 1
            }

            override fun getMaxStackSize(stack: ItemStack): Int {
                return 1
            }

            override fun mayPlace(stack: ItemStack): Boolean {
                return stack.`is`(NanoMiraiItemTags.LENSES)
            }
        })
        this.addSlot(SlotItemHandler(container, 2, 51, 48))
    }

    override fun stillValid(player: Player): Boolean {
        return stillValid(ContainerLevelAccess.create(player.level(), blockEntity.blockPos), player, NanoMiraiBlocks.LASER_ENGRAVER)
    }
}
