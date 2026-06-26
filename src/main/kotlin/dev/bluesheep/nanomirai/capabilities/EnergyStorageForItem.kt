package dev.bluesheep.nanomirai.capabilities

import dev.bluesheep.nanomirai.registry.NanoMiraiDataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.energy.EnergyStorage

class EnergyStorageForItem(val itemStack: ItemStack) : EnergyStorage(1000) {
    val componentStored: Int
        get() = itemStack.getOrDefault(NanoMiraiDataComponents.ENERGY, 0)

    val componentCapacity: Int
        get() = itemStack.getOrDefault(NanoMiraiDataComponents.ENERGY_CAPACITY, 1000)

    init {
        energy = componentStored
        capacity = componentCapacity
        maxReceive = capacity
        maxExtract = capacity
    }

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        val energyReceived = super.receiveEnergy(toReceive, simulate)
        if (!simulate) {
            itemStack.set(NanoMiraiDataComponents.ENERGY, energy)
        }
        return energyReceived
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        val energyExtracted = super.extractEnergy(toExtract, simulate)
        if (!simulate) {
            itemStack.set(NanoMiraiDataComponents.ENERGY, energy)
        }
        return energyExtracted
    }

    override fun getEnergyStored(): Int {
        return componentStored
    }

    override fun getMaxEnergyStored(): Int {
        return componentCapacity
    }
}