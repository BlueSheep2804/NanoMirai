package dev.bluesheep.nanomirai.recipe.lab.attribute

import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.Holder
import net.minecraft.core.NonNullList
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class LabAttributeRecipeBuilder(val attribute: Holder<Attribute>, val modifier: AttributeModifier, val catalyst: Ingredient, val items: NonNullList<Ingredient>) : SimpleRecipeBuilder(
    ItemStack(NanoMiraiItems.SUPPORT_NANO).apply {
        SupportNanoItem.setAttributes(this, attribute, modifier)
    }
) {
    override fun save(recipeOutput: RecipeOutput) {
        save(recipeOutput, modifier.id)
    }

    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("lab/attribute/")
        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .requirements(AdvancementRequirements.Strategy.OR)
        criteria.forEach(advancement::addCriterion)
        val recipe = LabAttributeRecipe(attribute, modifier, catalyst, items)
        output.accept(
            recipeId,
            recipe,
            advancement.build(recipeId.withPrefix("recipes/"))
        )
    }
}