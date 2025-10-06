package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.recipe.StackedIngredient
import dev.bluesheep.nanomirai.recipe.assembler.AssemblerRecipeBuilder
import dev.bluesheep.nanomirai.recipe.lab.attribute.LabAttributeRecipeBuilder
import dev.bluesheep.nanomirai.recipe.lab.effect.LabEffectRecipeBuilder
import dev.bluesheep.nanomirai.recipe.laser.LaserRecipeBuilder
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipeBuilder
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class NanoMiraiRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        assemblerRecipes(recipeOutput)
        labAttributeRecipes(recipeOutput)
        labEffectRecipes(recipeOutput)
        laserRecipes(recipeOutput)
        synthesizeRecipes(recipeOutput)

        // Nano Proto
        ShapedRecipeBuilder.shaped(
            RecipeCategory.MISC,
            NanoMiraiItems.NANO_PROTO
        )
            .define('G', NanoMiraiItems.GRAPHITE)
            .define('R', Items.REDSTONE)
            .define('C', Items.COPPER_INGOT)
            .define('M', NanoMiraiItems.BROKEN_NANOMACHINE)
            .pattern("GRG")
            .pattern("CMC")
            .pattern("GRG")
            .unlockedBy("has_broken_nanomachine", has(NanoMiraiItems.BROKEN_NANOMACHINE))
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

        // Graphene Sheet
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(Items.COAL),
            RecipeCategory.MISC,
            NanoMiraiItems.GRAPHENE_SHEET,
            0f,
            200
        )
            .unlockedBy("has_coal", has(Items.COAL))
            .save(recipeOutput, rl("smelting/graphene_sheet_from_graphite")
        )

        // Graphite
        ShapelessRecipeBuilder.shapeless(
            RecipeCategory.MISC,
            NanoMiraiItems.GRAPHITE
        )
            .requires(NanoMiraiItems.GRAPHENE_SHEET, 3)
            .unlockedBy("has_graphene_sheet", has(NanoMiraiItems.GRAPHENE_SHEET))
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

    fun assemblerRecipes(recipeOutput: RecipeOutput) {
        // Nano Cell
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_CELL),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(16, Items.REDSTONE),
                StackedIngredient.of(3, Items.COAL),
                StackedIngredient.of(1, Items.IRON_INGOT),
            )
        ).save(recipeOutput)

        // Nano Matrix
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_MATRIX),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(16, Items.REDSTONE),
                StackedIngredient.of(3, Items.COAL),
                StackedIngredient.of(1, Items.GOLD_INGOT),
            )
        ).save(recipeOutput)

        // Nano Singularity
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_SINGULARITY),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(4, Items.REDSTONE),
                StackedIngredient.of(3, Items.COAL, NanoMiraiItems.GRAPHITE),
                StackedIngredient.of(2, ItemTags.WOOL),
            )
        ).save(recipeOutput)
    }

    fun labAttributeRecipes(recipeOutput: RecipeOutput) {
        LabAttributeRecipeBuilder(
            Attributes.SCALE,
            AttributeModifier(
                rl("scale_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            NanoTier.PROTO,
            Ingredient.of(Items.ENDER_EYE),
            NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.ENDER_PEARL), Ingredient.of(Items.NETHER_STAR))
        ).save(recipeOutput)

        LabAttributeRecipeBuilder(
            Attributes.SNEAKING_SPEED,
            AttributeModifier(
                rl("sneaking_speed_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            NanoTier.MATRIX,
            Ingredient.of(Items.SCULK_CATALYST),
            NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.SUGAR), Ingredient.of(Items.SUGAR), Ingredient.of(Items.SUGAR))
        ).save(recipeOutput)
    }

    fun labEffectRecipes(recipeOutput: RecipeOutput) {
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                600, 2, false, true, true
            ),
            NanoTier.PROTO,
            Ingredient.of(Items.BLAZE_POWDER),
            NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.SUGAR), Ingredient.of(Items.GLOWSTONE_DUST))
        ).save(recipeOutput)

        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.POISON,
                600, 2, false, true, true
            ),
            NanoTier.MATRIX,
            Ingredient.of(Items.BLAZE_POWDER),
            NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.PUFFERFISH), Ingredient.of(Items.SPIDER_EYE))
        ).save(recipeOutput)
    }

    fun laserRecipes(recipeOutput: RecipeOutput) {
        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_MATRIX),
            Ingredient.of(NanoMiraiItems.NANO_CELL)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_SINGULARITY),
            Ingredient.of(NanoMiraiItems.NANO_MATRIX),
            Ingredient.of(Items.ECHO_SHARD)
        ).save(recipeOutput)
    }

    fun synthesizeRecipes(recipeOutput: RecipeOutput) {
        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.AMETHYST_LENS),
            NanoTier.CELL,
            Blocks.TINTED_GLASS.defaultBlockState(),
            Ingredient.of(Items.AMETHYST_SHARD),
            60
        ).save(recipeOutput)

        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.SCULK_LENS),
            NanoTier.SINGULARITY,
            Blocks.SCULK_SHRIEKER.defaultBlockState(),
            Ingredient.of(Items.ECHO_SHARD),
            200
        ).save(recipeOutput)
    }
}
