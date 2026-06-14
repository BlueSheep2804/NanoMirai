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
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

class SolarPanelBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.SOLAR_PANEL, pos, blockState) {
    companion object {
        fun capabilityProvider(blockEntity: SolarPanelBlockEntity, direction: Direction?): IEnergyStorage? {
            return if (blockEntity::energyStorage.isInitialized) {
                blockEntity.energyStorage
            } else null
        }
    }

    lateinit var energyStorage: EnergyStorageForBlockEntity

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

//    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
//        super.loadAdditional(tag, registries)
//
//        energyStorage.receiveEnergy()
//    }
//
//    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
//        super.saveAdditional(tag, registries)
//    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
//        Mth.lerp((level.dayTime % 24000).toDouble(), 0.0, 12000.0)
        if (!level.canSeeSky(pos.above())) return

        val time = level.dimensionType().fixedTime.orElse(level.dayTime()) % 24000
        if (time >= 12000) return

        val generate = Mth.abs(
            Mth.clampedLerp(
                0f,
                1f,
                Mth.abs(6000 - time.toInt()) / 6000f
            ) - 1f
        ) * 8
        energyStorage.receiveEnergy(Mth.floor(generate), false)
        level.invalidateCapabilities(pos)
    }
}