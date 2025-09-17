package dev.bluesheep.nanomirai.recipe

import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeSerializer
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class AssemblerRecipe(val inputItems: NonNullList<Ingredient>, val result: ItemStack) : Recipe<MultipleItemRecipeInput> {
    override fun getIngredients(): NonNullList<Ingredient> {
        return inputItems
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return width * height >= inputItems.size
    }

    override fun matches(
        input: MultipleItemRecipeInput,
        level: Level
    ): Boolean {
        val filteredInputItems = inputItems.filter { !it.isEmpty }
        val filteredInputList = input.list.filter { !it.isEmpty }

        return filteredInputItems.all { ingredient ->
            filteredInputList.any {
                ingredient.test(it)
            }
        }
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack {
        return result
    }

    override fun assemble(
        input: MultipleItemRecipeInput,
        registries: HolderLookup.Provider
    ): ItemStack {
        return result.copy()
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return NanoMiraiRecipeSerializer.ASSEMBLER.get()
    }

    override fun getType(): RecipeType<*> {
        return NanoMiraiRecipeType.ASSEMBLER
    }
}