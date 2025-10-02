package dev.bluesheep.nanomirai.recipe.laser

import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class LaserRecipeBuilder(result: ItemStack, val inputIngredient: Ingredient, val lensIngredient: Ingredient = Ingredient.EMPTY) : SimpleRecipeBuilder(result) {
    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("laser/")
        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .requirements(AdvancementRequirements.Strategy.OR)
        criteria.forEach(advancement::addCriterion)
        val recipe = LaserRecipe(inputIngredient, result, lensIngredient)
        output.accept(recipeId, recipe, advancement.build(recipeId.withPrefix("recipes/")))
    }
}
