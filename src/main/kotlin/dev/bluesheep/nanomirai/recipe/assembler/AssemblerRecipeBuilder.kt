package dev.bluesheep.nanomirai.recipe.assembler

import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import dev.bluesheep.nanomirai.recipe.StackedIngredient
import net.minecraft.core.NonNullList
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class AssemblerRecipeBuilder(result: ItemStack, val inputItem: NonNullList<StackedIngredient>) : SimpleRecipeBuilder(result) {
    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("assembler/")
        val recipe = AssemblerRecipe(inputItem, result)
        output.accept(recipeId, recipe, null)
    }
}
