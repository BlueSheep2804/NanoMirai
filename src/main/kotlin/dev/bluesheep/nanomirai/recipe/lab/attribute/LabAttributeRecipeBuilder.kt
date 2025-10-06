package dev.bluesheep.nanomirai.recipe.lab.attribute

import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
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

class LabAttributeRecipeBuilder(val attribute: Holder<Attribute>, val modifier: AttributeModifier, val tier: NanoTier, val catalyst: Ingredient, val items: NonNullList<Ingredient>) : SimpleRecipeBuilder(
    ItemStack(NanoMiraiItems.SUPPORT_NANO).apply {
        SupportNanoItem.setAttributes(this, attribute, modifier)
    }
) {
    override fun save(recipeOutput: RecipeOutput) {
        save(recipeOutput, modifier.id)
    }

    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("lab/attribute/")
        val recipe = LabAttributeRecipe(attribute, modifier, tier.rarity.ordinal, catalyst, items)
        output.accept(
            recipeId,
            recipe,
            null
        )
    }
}