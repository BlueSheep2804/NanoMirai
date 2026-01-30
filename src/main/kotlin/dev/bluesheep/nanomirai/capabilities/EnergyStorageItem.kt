package dev.bluesheep.nanomirai.capabilities

import dev.bluesheep.nanomirai.registry.NanoMiraiDataComponents
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.energy.EnergyStorage
import kotlin.math.min

class EnergyStorageItem(val itemStack: ItemStack) : EnergyStorage(1000) {
    val componentStored: Int?
        get() = itemStack.get(NanoMiraiDataComponents.ENERGY)

    val componentCapacity: Int?
        get() = itemStack.get(NanoMiraiDataComponents.ENERGY_CAPACITY)

    init {
        energy = componentStored ?: 0
        capacity = componentCapacity ?: 1000
        maxReceive = capacity
        maxExtract = capacity
    }

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!canReceive() || toReceive <= 0) {
            return 0
        }

        val energyReceived = Mth.clamp(maxEnergyStored - energyStored, 0, min(maxReceive, toReceive))
        if (!simulate) {
            energy += energyReceived
            itemStack.set(NanoMiraiDataComponents.ENERGY, energy)
        }
        return energyReceived
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!canExtract() || toExtract <= 0) {
            return 0
        }

        val energyExtracted = min(energyStored, min(maxExtract, toExtract))
        if (!simulate) {
            energy -= energyExtracted
            itemStack.set(NanoMiraiDataComponents.ENERGY, energy)
        }
        return energyExtracted
    }

    override fun getEnergyStored(): Int {
        return componentStored ?: 0
    }

    override fun getMaxEnergyStored(): Int {
        return componentCapacity ?: 1000
    }
}