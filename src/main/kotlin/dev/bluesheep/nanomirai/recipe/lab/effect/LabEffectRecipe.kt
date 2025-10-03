package dev.bluesheep.nanomirai.recipe.lab.effect

import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.recipe.MultipleItemRecipeInput
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeSerializer
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class LabEffectRecipe(val mobEffectInstance: MobEffectInstance, val catalyst: Ingredient, val items: NonNullList<Ingredient>) : Recipe<MultipleItemRecipeInput> {
    val result = ItemStack(NanoMiraiItems.NANO_SWARM_BLASTER).apply {
        NanoSwarmBlasterItem.addEffect(this, mobEffectInstance)
    }

    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(Ingredient.EMPTY, catalyst, *items.toTypedArray())
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return width * height >= items.size
    }

    override fun matches(
        input: MultipleItemRecipeInput,
        level: Level
    ): Boolean {
        val filteredInputItems = ingredients.filter { !it.isEmpty }
        val filteredInputList = input.list.filter { !it.isEmpty }

        return filteredInputItems.all { ingredient ->
            filteredInputList.any {
                ingredient.test(it)
            }
        }
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack {
        return result
    }

    override fun assemble(
        input: MultipleItemRecipeInput,
        registries: HolderLookup.Provider
    ): ItemStack {
        return result.copy()
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return NanoMiraiRecipeSerializer.LAB_EFFECT.get()
    }

    override fun getType(): RecipeType<*> {
        return NanoMiraiRecipeType.LAB_EFFECT
    }
}