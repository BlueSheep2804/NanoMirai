package dev.bluesheep.nanomirai.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

open class MultipleItemRecipeInput(val list: List<ItemStack>) : RecipeInput {
    override fun getItem(index: Int): ItemStack {
        return list[index]
    }

    override fun size(): Int {
        return list.size
    }
}