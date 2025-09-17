package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.recipe.AssemblerRecipeBuilder
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.internal.NeoForgeItemTagsProvider
import java.util.concurrent.CompletableFuture

class NanoMiraiRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        // Nano Seed
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_SEED),
            NonNullList.copyOf(
                listOf(
                    Items.REDSTONE,
                    Items.COAL,
                    Items.IRON_INGOT
                ).map { Ingredient.of(it) }
            )
        )
            .unlockedBy("has_nanomachine_assembler", has(NanoMiraiItems.NANOMACHINE_ASSEMBLER))
            .save(recipeOutput)

        // Nano Matrix
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_MATRIX),
            NonNullList.copyOf(
                listOf(
                    Items.REDSTONE,
                    Items.COAL,
                    Items.GOLD_INGOT
                ).map { Ingredient.of(it) }
            )
        )
            .unlockedBy("has_nanomachine_assembler", has(NanoMiraiItems.NANOMACHINE_ASSEMBLER))
            .save(recipeOutput)

        // Nano Singularity
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_SINGULARITY),
            NonNullList.copyOf(
                listOf(
                    Items.REDSTONE,
                    Items.COAL,
                    Items.DIAMOND
                ).map { Ingredient.of(it) }
            )
        )
            .unlockedBy("has_nanomachine_assembler", has(NanoMiraiItems.NANOMACHINE_ASSEMBLER))
            .save(recipeOutput)

        // Goggles
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.GOGGLES)
            .define('I', Items.IRON_INGOT)
            .define('W', ItemTags.WOOL)
            .define('G', Tags.Items.GLASS_PANES)
            .pattern("IWI")
            .pattern("GIG")
            .pattern("III")
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(recipeOutput)

        // Nanomachine Assembler
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.NANOMACHINE_ASSEMBLER)
            .define('C', Items.CRAFTER)
            .define('I', Items.IRON_INGOT)
            .define('L', Items.REDSTONE_LAMP)
            .define('S', Items.SPYGLASS)
            .pattern("ILI")
            .pattern("CSC")
            .pattern("III")
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(recipeOutput)
    }
}
