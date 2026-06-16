package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.registry.NanoMiraiDataComponents
import dev.bluesheep.nanomirai.util.EnergyFormat
import net.minecraft.network.chat.Component
import net.minecraft.util.FastColor
import net.minecraft.util.Mth
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.floor

open class PoweredItem(properties: Properties, capacity: Int, val cost: Int) : Item(
    properties
        .stacksTo(1)
        .component(NanoMiraiDataComponents.ENERGY, 0)
        .component(NanoMiraiDataComponents.ENERGY_CAPACITY, capacity)
) {
    companion object {
        val COLOR_NORMAL = FastColor.ARGB32.color(0, 255, 255)
        val COLOR_WARN = FastColor.ARGB32.color(249, 127, 14)
        val COLOR_EMPTY = FastColor.ARGB32.color(180, 34, 34)
        fun getEnergy(stack: ItemStack): IEnergyStorage? {
            return stack.getCapability(Capabilities.EnergyStorage.ITEM)
        }
    }

    override fun isBarVisible(stack: ItemStack): Boolean {
        getEnergy(stack) ?: return false
        return true
    }

    override fun getBarWidth(stack: ItemStack): Int {
        return Mth.clamp(super.getBarWidth(stack), 1, 13)
    }

    override fun getBarColor(stack: ItemStack): Int {
        return when (getRemainingUses(stack)) {
            0 -> COLOR_EMPTY
            1 -> COLOR_WARN
            else -> COLOR_NORMAL
        }
    }

    override fun getDamage(stack: ItemStack): Int {
        val energy = getEnergy(stack) ?: return 0
        return energy.maxEnergyStored - energy.energyStored
    }

    override fun getMaxDamage(stack: ItemStack): Int {
        val energy = getEnergy(stack) ?: return 0
        return energy.maxEnergyStored
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        val energyStorage = getEnergy(stack)
        if (energyStorage != null) {
            tooltipComponents.add(
                EnergyFormat.tooltip(
                    energyStorage.energyStored,
                    energyStorage.maxEnergyStored
                )
            )
            tooltipComponents.add(
                Component.translatable(
                    "nanomirai.tooltip.remaining_uses",
                    getRemainingUses(stack)
                )
            )
        }
    }

    fun isEnergyEnough(stack: ItemStack): Boolean {
        val energy = getEnergy(stack) ?: return false
        return energy.energyStored >= cost
    }

    fun consumeEnergy(stack: ItemStack): Int {
        val energy = getEnergy(stack) ?: return 0
        return energy.extractEnergy(cost, false)
    }

    fun getRemainingUses(stack: ItemStack): Int {
        val energy = getEnergy(stack) ?: return 0
        return floor(energy.energyStored.toDouble() / cost).toInt()
    }
}