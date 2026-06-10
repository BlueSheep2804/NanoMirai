package dev.bluesheep.nanomirai.recipe.synthesize

import dev.bluesheep.nanomirai.recipe.BlockInput
import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

class SynthesizeRecipeBuilder(result: ItemStack, val tier: NanoTier, val block: BlockInput, val catalyst: Ingredient, val duration: Int) : SimpleRecipeBuilder(result) {
    companion object {
        fun simpleBlock(result: ItemStack, tier: NanoTier, block: Block, catalyst: Ingredient, duration: Int): SynthesizeRecipeBuilder {
            return SynthesizeRecipeBuilder(result, tier, BlockInput.noNbt(block), catalyst, duration)
        }
    }

    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("synthesize/")
        val recipe = SynthesizeRecipe(result, tier.ordinal, block, catalyst, duration)
        output.accept(recipeId, recipe, null)
    }
}
