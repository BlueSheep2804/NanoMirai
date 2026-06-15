package dev.bluesheep.nanomirai.menu

import dev.bluesheep.nanomirai.block.entity.NanoLabBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiItemTags
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.SlotItemHandler

class NanoLabMenu(
    containerId: Int,
    playerInv: Inventory,
    blockEntity: NanoLabBlockEntity,
    val data: ContainerData
) : AbstractMenu<NanoLabBlockEntity>(
    blockEntity.itemHandler.slots,
    NanoMiraiMenu.NANO_LAB,
    containerId,
    playerInv,
    blockEntity,
    true
) {
    constructor(containerId: Int, playerInv: Inventory, extraData: FriendlyByteBuf) : this(
        containerId,
        playerInv,
        playerInv.player.level().getBlockEntity(extraData.readBlockPos()) as NanoLabBlockEntity,
        SimpleContainerData(2)
    )

    init {
        addDataSlots(data)
    }

    override fun addContainerSlots() {
        val container = blockEntity.itemHandler
        this.addSlot(object : SlotItemHandler(container, 0, 143, 63) {
            override fun mayPlace(stack: ItemStack): Boolean {
                return stack.`is`(NanoMiraiItemTags.FUNCTIONAL_NANOMACHINES) || stack.`is`(NanoMiraiItems.SUPPORT_NANO) || stack.`is`(NanoMiraiItems.NANO_SWARM_BLASTER)
            }
        })

        this.addSlot(SlotItemHandler(container, 1, 53, 63))

        this.addSlot(SlotItemHandler(container, 2, 26, 27))
        this.addSlot(SlotItemHandler(container, 3, 17, 63))
        this.addSlot(SlotItemHandler(container, 4, 26, 99))

        this.addSlot(SlotItemHandler(container, 5, 80, 27))
        this.addSlot(SlotItemHandler(container, 6, 89, 63))
        this.addSlot(SlotItemHandler(container, 7, 80, 99))
    }

    override fun stillValid(player: Player): Boolean {
        return stillValid(ContainerLevelAccess.create(player.level(), blockEntity.blockPos), player, NanoMiraiBlocks.NANO_LAB)
    }
}