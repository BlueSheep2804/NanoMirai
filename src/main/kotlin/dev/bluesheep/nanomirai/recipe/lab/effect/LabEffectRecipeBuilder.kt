package dev.bluesheep.nanomirai.recipe.lab.effect

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.NonNullList
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class LabEffectRecipeBuilder(val mobEffectInstance: MobEffectInstance, val tier: NanoTier, val catalyst: Ingredient, val items: NonNullList<Ingredient>) : SimpleRecipeBuilder(
    ItemStack(NanoMiraiItems.NANO_SWARM_BLASTER).apply {
        NanoSwarmBlasterItem.addEffect(this, mobEffectInstance)
    }
) {
    override fun save(recipeOutput: RecipeOutput) {
        save(recipeOutput, rl(ResourceLocation.parse(mobEffectInstance.effect.registeredName).path))
    }

    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("lab/effect/")
        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .requirements(AdvancementRequirements.Strategy.OR)
        criteria.forEach(advancement::addCriterion)
        val recipe = LabEffectRecipe(mobEffectInstance, tier.rarity.ordinal, catalyst, items)
        output.accept(
            recipeId,
            recipe,
            advancement.build(recipeId.withPrefix("recipes/"))
        )
    }
}