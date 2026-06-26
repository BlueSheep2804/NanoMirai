package dev.bluesheep.nanomirai.capabilities

import dev.bluesheep.nanomirai.registry.NanoMiraiDataComponents
import net.minecraft.core.component.PatchedDataComponentMap
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.energy.EnergyStorage

class EnergyStorageForBlockEntity(val blockEntity: BlockEntity, capacity: Int) : EnergyStorage(capacity) {
    init {
        this.energy = blockEntity.components().getOrDefault(NanoMiraiDataComponents.ENERGY, 0)
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        val result = super.extractEnergy(toExtract, simulate)
        if (!simulate) {
            updateComponent()
        }

        return result
    }

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        val result = super.receiveEnergy(toReceive, simulate)
        if (!simulate) {
            updateComponent()
        }

        return result
    }

    private fun updateComponent() {
        val patchedComponents = PatchedDataComponentMap(blockEntity.components())
        patchedComponents.set(NanoMiraiDataComponents.ENERGY, energy)
        blockEntity.applyComponents(patchedComponents, patchedComponents.asPatch())
        blockEntity.setChanged()
    }
}