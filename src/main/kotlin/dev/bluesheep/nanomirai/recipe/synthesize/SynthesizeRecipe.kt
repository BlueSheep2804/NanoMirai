package dev.bluesheep.nanomirai.recipe.synthesize

import dev.bluesheep.nanomirai.recipe.BlockWithPairItemInput
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeSerializer
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class SynthesizeRecipe(val result: ItemStack, val tier: Int, val inputBlock: BlockState, val inputCatalystItem: Ingredient, val duration: Int) : Recipe<BlockWithPairItemInput> {
    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(Ingredient.EMPTY, inputCatalystItem)
    }

    override fun matches(input: BlockWithPairItemInput, level: Level): Boolean {
        val isBlockMatch = input.block.`is`(inputBlock.block)
        val isNanomachineTierEnough = (input.mainhand.get(DataComponents.RARITY)?.ordinal ?: -1) >= tier
        return inputCatalystItem.test(input.offhand) && input.mainhand.`is`(NanoMiraiItems.SYNTHESIZE_NANO) && isNanomachineTierEnough && isBlockMatch
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
