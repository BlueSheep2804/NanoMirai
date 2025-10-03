package dev.bluesheep.nanomirai.recipe.lab

import dev.bluesheep.nanomirai.recipe.CatalystWithMultipleItemRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level

abstract class AbstractLabRecipe(val result: ItemStack, val catalyst: Ingredient, val items: NonNullList<Ingredient>) : Recipe<CatalystWithMultipleItemRecipeInput> {
    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(Ingredient.EMPTY, *items.toTypedArray())
    }

    override fun matches(
        input: CatalystWithMultipleItemRecipeInput,
        level: Level
    ): Boolean {
        if (!catalyst.test(input.catalyst)) return false
        val inputList = input.list.filter { !it.isEmpty }.toMutableList()

        for (ingredient in ingredients.filter { !it.isEmpty }) {
            val index = inputList.indexOfFirst { ingredient.test(it) }
            if (index == -1) {
                return false
            } else {
                inputList.removeAt(index)
            }
        }
        return true
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return width * height >= items.size
    }

    override fun assemble(
        input: CatalystWithMultipleItemRecipeInput,
        registries: HolderLookup.Provider
    ): ItemStack {
        return result.copy()
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack {
        return result
    }
}