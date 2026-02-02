package dev.bluesheep.nanomirai.recipe.synthesize

import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import dev.bluesheep.nanomirai.recipe.BlockStateWithNbt
import dev.bluesheep.nanomirai.recipe.BlockWithPairItemInput
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeSerializer
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class SynthesizeRecipe(val result: ItemStack, val tier: Int, val inputBlock: BlockStateWithNbt, val inputCatalystItem: Ingredient, val duration: Int) : Recipe<BlockWithPairItemInput> {
    fun checkWithoutCatalyst(blockStateWithNbt: BlockStateWithNbt, itemStack: ItemStack): Boolean {
        val isBlockMatch = blockStateWithNbt.`is`(inputBlock.block, inputBlock.nbt)
        val isNanomachineTierEnough = (NanoTier.fromItem(itemStack.item)?.ordinal ?: -1) >= tier
        return (
            itemStack.item is SynthesizeNanoItem
            && isNanomachineTierEnough
            && isBlockMatch
        )
    }

    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(Ingredient.EMPTY, inputCatalystItem)
    }

    override fun matches(input: BlockWithPairItemInput, level: Level): Boolean {
        return (
                inputCatalystItem.test(input.offhand)
                && checkWithoutCatalyst(input.block, input.mainhand)
        )
    }

    override fun assemble(p0: BlockWithPairItemInput, p1: HolderLookup.Provider): ItemStack {
        return result.copy()
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return width * height >= 1
    }

    override fun getResultItem(p0: HolderLookup.Provider): ItemStack {
        return result
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return NanoMiraiRecipeSerializer.SYNTHESIZE.get()
    }

    override fun getType(): RecipeType<*> {
        return NanoMiraiRecipeType.SYNTHESIZE
    }
}
