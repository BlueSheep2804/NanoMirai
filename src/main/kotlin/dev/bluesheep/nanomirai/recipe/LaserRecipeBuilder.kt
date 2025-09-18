package dev.bluesheep.nanomirai.recipe

import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class LaserRecipeBuilder(result: ItemStack, val inputIngredient: Ingredient, val lensIngredient: Ingredient = Ingredient.EMPTY) : SimpleRecipeBuilder(result) {
    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)
        criteria.forEach(advancement::addCriterion)
        val recipe = LaserRecipe(inputIngredient, result, lensIngredient)
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
    }
}
