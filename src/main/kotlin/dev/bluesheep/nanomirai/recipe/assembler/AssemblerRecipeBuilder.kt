package dev.bluesheep.nanomirai.recipe.assembler

import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import dev.bluesheep.nanomirai.recipe.StackedIngredient
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.NonNullList
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class AssemblerRecipeBuilder(result: ItemStack, val inputItem: NonNullList<StackedIngredient>) : SimpleRecipeBuilder(result) {
    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)
        criteria.forEach(advancement::addCriterion)
        val recipe = AssemblerRecipe(inputItem, result)
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
    }
}
