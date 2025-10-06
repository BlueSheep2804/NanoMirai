package dev.bluesheep.nanomirai.recipe.laser

import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class LaserRecipeBuilder(result: ItemStack, val inputIngredient: Ingredient, val lensIngredient: Ingredient = Ingredient.EMPTY) : SimpleRecipeBuilder(result) {
    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("laser/")
        val recipe = LaserRecipe(inputIngredient, result, lensIngredient)
        output.accept(recipeId, recipe, null)
    }
}
