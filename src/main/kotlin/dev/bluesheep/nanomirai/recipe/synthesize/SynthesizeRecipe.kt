package dev.bluesheep.nanomirai.recipe.synthesize

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
import net.minecraft.world.level.block.state.BlockState

class SynthesizeRecipe(val inputBlock: BlockState, val inputCatalystItem: Ingredient, val tier: Int, val result: ItemStack) : Recipe<BlockWithPairItemInput> {
    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(Ingredient.EMPTY, inputCatalystItem)
    }

    override fun matches(input: BlockWithPairItemInput, level: Level): Boolean {
        val isBlockMatch = input.block.`is`(inputBlock.block)
        val inputTier = NanoTier.entries.find { input.mainhand.`is`(it.item) }?.tierLevel?: -1
        return inputCatalystItem.test(input.offhand) && inputTier >= tier && isBlockMatch
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
