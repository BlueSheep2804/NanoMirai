package dev.bluesheep.nanomirai.recipe.synthesize

import dev.bluesheep.nanomirai.recipe.SimpleRecipeBuilder
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.state.BlockState

class SynthesizeRecipeBuilder(result: ItemStack, val tier: NanoTier, val block: BlockState, val catalyst: Ingredient, val duration: Int) : SimpleRecipeBuilder(result) {
    override fun save(output: RecipeOutput, id: ResourceLocation) {
        val recipeId = id.withPrefix("synthesize/")
        val recipe = SynthesizeRecipe(result, tier.rarity.ordinal, block, catalyst, duration)
        output.accept(recipeId, recipe, null)
    }
}
