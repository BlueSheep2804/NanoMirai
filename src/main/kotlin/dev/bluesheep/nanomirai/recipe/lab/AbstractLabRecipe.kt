package dev.bluesheep.nanomirai.recipe.lab

import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level

abstract class AbstractLabRecipe(val result: ItemStack, val tier: Int, val catalyst: Ingredient, val items: NonNullList<Ingredient>) : Recipe<NanoLabRecipeInput> {
    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(Ingredient.EMPTY, *items.toTypedArray())
    }

    override fun matches(
        input: NanoLabRecipeInput,
        level: Level
    ): Boolean {
        if (!catalyst.test(input.catalyst)) return false
        if (input.nanoItem.rarity.ordinal < tier) return false
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
        input: NanoLabRecipeInput,
        registries: HolderLookup.Provider
    ): ItemStack {
        return result.copy()
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack {
        return result
    }
}