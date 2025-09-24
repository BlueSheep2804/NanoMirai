package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.item.NanoMachineItem
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.world.item.crafting.Ingredient

enum class NanoTier(val tierLevel: Int, val item: NanoMachineItem) {
    PROTO(0, NanoMiraiItems.NANO_PROTO),
    CELL(1, NanoMiraiItems.NANO_CELL),
    MATRIX(2, NanoMiraiItems.NANO_MATRIX),
    SINGULARITY(3, NanoMiraiItems.NANO_SINGULARITY);

    companion object {
        fun fromMinLevel(level: Int): List<NanoTier> {
            return NanoTier.entries.filter { it.tierLevel >= level }
        }

        fun ingredientFromMinLevel(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map { it.item }.toTypedArray())
        }
    }
}
