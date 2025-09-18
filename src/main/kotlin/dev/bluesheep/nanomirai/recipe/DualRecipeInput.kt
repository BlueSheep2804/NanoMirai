package dev.bluesheep.nanomirai.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

data class DualRecipeInput(val first: ItemStack, val second: ItemStack = ItemStack.EMPTY) : RecipeInput {
    override fun getItem(p0: Int): ItemStack {
        return when (p0) {
            0 -> first
            1 -> second
            else -> ItemStack.EMPTY
        }
    }

    override fun size(): Int {
        return 2
    }
}
