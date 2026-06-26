package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.capabilities.EnergyStorageForBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.util.Mth
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.ItemStackHandler

class SolarPanelBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.SOLAR_PANEL, pos, blockState) {
    companion object {
        fun capabilityProvider(blockEntity: SolarPanelBlockEntity, direction: Direction?): IEnergyStorage? {
            return if (blockEntity::energyStorage.isInitialized) {
                blockEntity.energyStorage
            } else null
        }
    }

    val itemHandler = ItemStackHandler()
    lateinit var energyStorage: EnergyStorageForBlockEntity
    var sunlightFactor = 0f
    var remaining = 0f
    val data = object : ContainerData {
        override fun get(index: Int): Int {
            if (!this@SolarPanelBlockEntity::energyStorage.isInitialized) return 0
            return when (index) {
                0 -> energyStorage.energyStored
                1 -> energyStorage.maxEnergyStored
                2 -> Mth.floor(sunlightFactor * 100)
                else -> 0
            }
        }

        override fun set(index: Int, value: Int) {}

        override fun getCount(): Int {
            return 3
        }
    }

    override fun onLoad() {
        super.onLoad()

        energyStorage = EnergyStorageForBlockEntity(this, 100000)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(registries)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener?>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        itemHandler.deserializeNBT(registries, tag.getCompound("items"))
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.put("items", itemHandler.serializeNBT(registries))

        super.saveAdditional(tag, registries)
    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        if (level.canSeeSky(pos.above())) {
            val time = level.dimensionType().fixedTime.orElse(level.dayTime()) % 24000
            if (time < 12000) {
                sunlightFactor = Mth.abs(
                    Mth.clampedLerp(
                        0f,
                        1f,
                        Mth.abs(6000 - time.toInt()) / 6000f
                    ) - 1f
                )
                val generate = sunlightFactor * 8
                remaining += generate % 1
                var store = Mth.floor(generate)
                if (remaining >= 1) {
                    store += Mth.floor(remaining)
                    remaining %= 1
                }
                energyStorage.receiveEnergy(store, false)
                level.invalidateCapabilities(pos)
            } else {
                sunlightFactor = -0.02f
            }
        } else {
            sunlightFactor = -0.01f
        }

        val itemEnergyStorage = itemHandler.getStackInSlot(0).getCapability(Capabilities.EnergyStorage.ITEM)
        if (itemEnergyStorage != null) {
            if (itemEnergyStorage.canReceive() && energyStorage.canExtract()) {
                val extracted = energyStorage.extractEnergy(100000, false)
                val remaining = extracted - itemEnergyStorage.receiveEnergy(extracted, false)
                energyStorage.receiveEnergy(remaining, false)
                level.invalidateCapabilities(pos)
            }
        }
    }
}