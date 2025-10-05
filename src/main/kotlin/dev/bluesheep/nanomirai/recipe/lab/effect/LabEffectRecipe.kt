package dev.bluesheep.nanomirai.recipe.lab.effect

import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.recipe.lab.AbstractLabRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeSerializer
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.NonNullList
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class LabEffectRecipe(val mobEffectInstance: MobEffectInstance, tier: Int, catalyst: Ingredient, items: NonNullList<Ingredient>) : AbstractLabRecipe(
    ItemStack(NanoMiraiItems.NANO_SWARM_BLASTER).apply {
        NanoSwarmBlasterItem.addEffect(this, mobEffectInstance)
    },
    tier,
    catalyst,
    items
) {
    override fun getSerializer(): RecipeSerializer<*> {
        return NanoMiraiRecipeSerializer.LAB_EFFECT.get()
    }

    override fun getType(): RecipeType<*> {
        return NanoMiraiRecipeType.LAB_EFFECT
    }
}