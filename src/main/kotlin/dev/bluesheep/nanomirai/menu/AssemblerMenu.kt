package dev.bluesheep.nanomirai.menu

import dev.bluesheep.nanomirai.block.entity.AssemblerBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.items.SlotItemHandler

class AssemblerMenu(
    containerId: Int,
    playerInv: Inventory,
    blockEntity: AssemblerBlockEntity,
    val data: ContainerData
) : AbstractMenu<AssemblerBlockEntity>(
    blockEntity.itemHandler.slots,
    NanoMiraiMenu.ASSEMBLER,
    containerId,
    playerInv,
    blockEntity
) {
    constructor(containerId: Int, playerInv: Inventory, extraData: FriendlyByteBuf) : this(
        containerId,
        playerInv,
        playerInv.player.level().getBlockEntity(extraData.readBlockPos()) as AssemblerBlockEntity,
        SimpleContainerData(2)
    )

    init {
        addDataSlots(data)
    }

    override fun addContainerSlots() {
        val container = blockEntity.itemHandler
        this.addSlot(OutputSlotItemHandler(container, 0, 124, 35))
        for (row in 0 until 3) {
            for (col in 0 until 3) {
                this.addSlot(SlotItemHandler(container,  1 + col + row * 3, 30 + col * 18, 17 + row * 18))
            }
        }
    }

    override fun stillValid(player: Player): Boolean {
        return stillValid(ContainerLevelAccess.create(player.level(), blockEntity.blockPos), player, NanoMiraiBlocks.NANOMACHINE_ASSEMBLER)
    }
}
