package dev.bluesheep.nanomirai.recipe.laser

import dev.bluesheep.nanomirai.recipe.DualRecipeInput
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

class LaserRecipe(val ingredient: Ingredient, val result: ItemStack, val lens: Ingredient = Ingredient.EMPTY) : Recipe<DualRecipeInput> {
    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(Ingredient.EMPTY, ingredient, lens)
    }

    override fun matches(input: DualRecipeInput, level: Level): Boolean {
        return ingredient.test(input.first) && (lens.isEmpty || lens.test(input.second))
    }

    override fun assemble(p0: DualRecipeInput, p1: HolderLookup.Provider): ItemStack {
        return result.copy()
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return width * height >= 1
    }

    override fun getResultItem(p0: HolderLookup.Provider): ItemStack {
        return result
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return NanoMiraiRecipeSerializer.LASER.get()
    }

    override fun getType(): RecipeType<*> {
        return NanoMiraiRecipeType.LASER
    }
}