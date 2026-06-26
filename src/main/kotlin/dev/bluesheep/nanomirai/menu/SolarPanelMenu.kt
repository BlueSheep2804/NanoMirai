package dev.bluesheep.nanomirai.menu

import dev.bluesheep.nanomirai.block.entity.SolarPanelBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.items.SlotItemHandler

class SolarPanelMenu(
    containerId: Int,
    playerInv: Inventory,
    blockEntity: SolarPanelBlockEntity,
    val data: ContainerData
) : AbstractMenu<SolarPanelBlockEntity>(
    1,
    NanoMiraiMenu.SOLAR_PANEL,
    containerId,
    playerInv,
    blockEntity
) {
    constructor(containerId: Int, playerInv: Inventory, extraData: FriendlyByteBuf) : this(
        containerId,
        playerInv,
        playerInv.player.level().getBlockEntity(extraData.readBlockPos()) as SolarPanelBlockEntity,
        SimpleContainerData(3)
    )

    init {
        addDataSlots(data)
    }

    override fun addContainerSlots() {
        this.addSlot(SlotItemHandler(blockEntity.itemHandler, 0, 125, 57))
    }

    override fun stillValid(player: Player): Boolean {
        return stillValid(ContainerLevelAccess.create(player.level(), blockEntity.blockPos), player, NanoMiraiBlocks.SOLAR_PANEL)
    }
}