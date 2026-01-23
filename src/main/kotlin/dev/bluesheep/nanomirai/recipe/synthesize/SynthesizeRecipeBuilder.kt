package dev.bluesheep.nanomirai.recipe.synthesize

import dev.bluesheep.nanomirai.recipe.BlockStateWithNbt
import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

class SynthesizeRecipeBuilder(result: ItemStack, val tier: NanoTier, val block: BlockStateWithNbt, val catalyst: Ingredient, val duration: Int) : SimpleRecipeBuilder(result) {
    companion object {
        fun default(result: ItemStack, tier: NanoTier, block: Block, catalyst: Ingredient, duration: Int): SynthesizeRecipeBuilder {
            return SynthesizeRecipeBuilder(result, tier, BlockStateWithNbt.noNbt(block.defaultBlockState()), catalyst, duration)
        }
    }

    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("synthesize/")
        val recipe = SynthesizeRecipe(result, tier.rarity.ordinal, block, catalyst, duration)
        output.accept(recipeId, recipe, null)
    }
}
