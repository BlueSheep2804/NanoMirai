package dev.bluesheep.nanomirai.recipe.synthesize

import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.state.BlockState

class SynthesizeRecipeBuilder(result: ItemStack, val block: BlockState, val catalyst: Ingredient, val tier: Int) : SimpleRecipeBuilder(result) {
    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)
        criteria.forEach(advancement::addCriterion)
        val recipe = SynthesizeRecipe(block, catalyst, tier, result)
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
    }
}
