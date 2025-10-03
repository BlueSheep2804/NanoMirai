package dev.bluesheep.nanomirai.recipe.lab.attribute

import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.recipe.lab.AbstractLabRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeSerializer
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.Holder
import net.minecraft.core.NonNullList
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class LabAttributeRecipe(val attribute: Holder<Attribute>, val modifier: AttributeModifier, catalyst: Ingredient, items: NonNullList<Ingredient>) : AbstractLabRecipe(
    ItemStack(NanoMiraiItems.SUPPORT_NANO).apply {
        SupportNanoItem.setAttributes(this, attribute, modifier)
    },
    catalyst,
    items
) {
    override fun getSerializer(): RecipeSerializer<*> {
        return NanoMiraiRecipeSerializer.LAB_ATTRIBUTE.get()
    }

    override fun getType(): RecipeType<*> {
        return NanoMiraiRecipeType.LAB_ATTRIBUTE
    }
}