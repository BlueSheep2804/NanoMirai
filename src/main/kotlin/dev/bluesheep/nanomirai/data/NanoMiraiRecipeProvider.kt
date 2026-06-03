package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.recipe.BlockStateWithNbt
import dev.bluesheep.nanomirai.recipe.StackedIngredient
import dev.bluesheep.nanomirai.recipe.assembler.AssemblerRecipeBuilder
import dev.bluesheep.nanomirai.recipe.lab.attribute.LabAttributeRecipeBuilder
import dev.bluesheep.nanomirai.recipe.lab.effect.LabEffectRecipeBuilder
import dev.bluesheep.nanomirai.recipe.laser.LaserRecipeBuilder
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipeBuilder
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiItemTags
import dev.bluesheep.nanomirai.util.NanoTier
import dev.bluesheep.nanomirai.util.SynthesizeUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentPredicate
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import java.util.concurrent.CompletableFuture

class NanoMiraiRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        craftingRecipes(recipeOutput)

        assemblerRecipes(recipeOutput)
        labAttributeRecipes(recipeOutput)
        labEffectRecipes(recipeOutput)
        laserRecipes(recipeOutput)
        synthesizeRecipes(recipeOutput)

        // Graphite
        SimpleCookingRecipeBuilder.blasting(
            Ingredient.of(Items.COAL),
            RecipeCategory.MISC,
            NanoMiraiItems.GRAPHITE,
            0.1f,
            100
        )
            .unlockedBy("has_coal", has(Items.COAL))
            .save(recipeOutput)

        // Silicon
        SimpleCookingRecipeBuilder.blasting(
            Ingredient.of(Items.QUARTZ),
            RecipeCategory.MISC,
            NanoMiraiItems.SILICON,
            0.1f,
            100
        )
            .unlockedBy("has_quartz", has(Items.QUARTZ))
            .save(recipeOutput, rl("silicon_from_blasting"))

        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(NanoMiraiItemTags.RAW_SCULMIUM),
            RecipeCategory.MISC,
            NanoMiraiItems.SCULMIUM_INGOT,
            1f,
            200
        )
            .unlockedByItem(NanoMiraiItemTags.RAW_SCULMIUM)
            .save(recipeOutput)

        SimpleCookingRecipeBuilder.blasting(
            Ingredient.of(NanoMiraiItemTags.RAW_SCULMIUM),
            RecipeCategory.MISC,
            NanoMiraiItems.SCULMIUM_INGOT,
            1f,
            100
        )
            .unlockedByItem(NanoMiraiItemTags.RAW_SCULMIUM)
            .save(recipeOutput, rl("sculmium_ingot_from_blasting"))

        // Deprecated item migrate
        NanoTier.entries.forEach {
            val predicate = DataComponentPredicate.builder()
                .expect(DataComponents.RARITY, it.rarity)
                .build()
            val rarityPlusOne = Rarity.entries[it.rarity.ordinal+1]
            val predicatePlusOne = DataComponentPredicate.builder()
                .expect(DataComponents.RARITY, rarityPlusOne)
                .build()
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, it.getSynthesizeNano())
                .requires(DataComponentIngredient.of(false, predicate, NanoMiraiItems.SYNTHESIZE_NANO))
                .unlockedBy("has_synthesize_nano", has(NanoMiraiItems.SYNTHESIZE_NANO))
                .save(recipeOutput, rl("deprecated/synthesize_nano_${it.name.lowercase()}_from_${it.rarity.name.lowercase()}") )
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, it.getSynthesizeNano())
                .requires(DataComponentIngredient.of(false, predicatePlusOne, NanoMiraiItems.SYNTHESIZE_NANO))
                .unlockedBy("has_synthesize_nano", has(NanoMiraiItems.SYNTHESIZE_NANO))
                .save(recipeOutput, rl("deprecated/synthesize_nano_${it.name.lowercase()}_from_${rarityPlusOne.name.lowercase()}") )
        }
    }

    fun craftingRecipes(recipeOutput: RecipeOutput) {
        // Synthesize Nano
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.SYNTHESIZE_NANO_NORMAL)
            .pattern("IAI")
            .pattern("WCW")
            .pattern(" D ")
            .define('I', Tags.Items.INGOTS_IRON)
            .define('A', Items.AMETHYST_SHARD)
            .define('W', Tags.Items.DYES_LIGHT_BLUE)
            .define('C', NanoMiraiItems.SIMPLE_CIRCUIT)
            .define('D', Items.DIAMOND)
            .unlockedBy("has_simple_circuit", has(NanoMiraiItems.SIMPLE_CIRCUIT))
            .save(recipeOutput)

        // Support Nano
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.SUPPORT_NANO_NORMAL)
            .pattern("GMG")
            .pattern("CEC")
            .pattern("KGK")
            .define('G', Tags.Items.INGOTS_GOLD)
            .define('M', Items.GLISTERING_MELON_SLICE)
            .define('C', NanoMiraiItems.SIMPLE_CIRCUIT)
            .define('E', Items.EMERALD)
            .define('K', Items.KELP)
            .unlockedBy("has_simple_circuit", has(NanoMiraiItems.SIMPLE_CIRCUIT))
            .save(recipeOutput)

        // Nano Swarm Blaster
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.NANO_SWARM_BLASTER_NORMAL)
            .pattern("SSS")
            .pattern("SCD")
            .pattern("SL ")
            .define('S', NanoMiraiItemTags.SCULMIUM_INGOT)
            .define('C', NanoMiraiItems.LASER_COMPONENT)
            .define('D', Items.DISPENSER)
            .define('L', Items.LEVER)
            .unlockedBy("has_laser_component", has(NanoMiraiItems.LASER_COMPONENT))
            .save(recipeOutput)

        // Repair Nano
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NanoMiraiItems.REPAIR_NANO)
            .requires(NanoMiraiItems.GRAPHITE)
            .requires(Ingredient.of(NanoMiraiItemTags.REPAIR_NANO_INGREDIENTS), 8)
            .unlockedBy("has_graphite", has(NanoMiraiItems.GRAPHITE))
            .save(recipeOutput, rl("repair_nano"))

        // Simple Circuit
        ShapedRecipeBuilder.shaped(
            RecipeCategory.MISC,
            NanoMiraiItems.SIMPLE_CIRCUIT
        )
            .pattern("RRR")
            .pattern("GCG")
            .pattern("TCT")
            .define('R', Items.REDSTONE)
            .define('G', NanoMiraiItems.GRAPHITE)
            .define('C', Items.COPPER_INGOT)
            .define('T', ItemTags.TERRACOTTA)
            .unlockedBy("has_graphite", has(NanoMiraiItems.GRAPHITE))
            .save(recipeOutput)

        // Normal Circuit
        ShapedRecipeBuilder.shaped(
            RecipeCategory.MISC,
            NanoMiraiItems.NORMAL_CIRCUIT
        )
            .pattern("RSR")
            .pattern("SOS")
            .pattern("CDC")
            .define('R', Items.REDSTONE)
            .define('S', NanoMiraiItems.SIMPLE_CIRCUIT)
            .define('O', Items.OBSIDIAN)
            .define('C', Items.COPPER_INGOT)
            .define('D', Items.DIAMOND)
            .unlockedBy("has_simple_circuit", has(NanoMiraiItems.SIMPLE_CIRCUIT))
            .save(recipeOutput)

        // Laser Component
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.LASER_COMPONENT)
            .pattern("RCR")
            .pattern("ILI")
            .pattern(" G ")
            .define('R', Items.REDSTONE)
            .define('C', NanoMiraiItems.NORMAL_CIRCUIT)
            .define('I', Tags.Items.INGOTS_IRON)
            .define('L', Items.REDSTONE_LAMP)
            .define('G', Tags.Items.GLASS_BLOCKS)
            .unlockedBy("has_normal_circuit", has(NanoMiraiItems.NORMAL_CIRCUIT))
            .save(recipeOutput)

        // Lens
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.LENS)
            .pattern("GGG")
            .pattern("G G")
            .pattern("GGG")
            .define('G', Tags.Items.GLASS_PANES_COLORLESS)
            .unlockedByItem(Tags.Items.GLASS_PANES_COLORLESS)
            .save(recipeOutput)

        // Mob Cage
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.MOB_CAGE)
            .pattern("CNC")
            .pattern("EIE")
            .define('C', Items.CHAIN)
            .define('N', NanoMiraiItems.NANO_CIRCUIT)
            .define('E', Items.ENDER_PEARL)
            .define('I', Tags.Items.INGOTS_IRON)
            .unlockedBy("has_nano_circuit", has(NanoMiraiItems.NANO_CIRCUIT))
            .save(recipeOutput)

        // Sculk Sensor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SCULK_SENSOR)
            .define('C', NanoMiraiItemTags.SCULMIUM_INGOT)
            .define('V', Items.SCULK_VEIN)
            .define('S', Items.SCULK)
            .pattern("VCV")
            .pattern("SSS")
            .unlockedBy("has_sculmium_ingot", has(NanoMiraiItems.SCULMIUM_INGOT))
            .save(recipeOutput, rl("sculk_sensor_from_sculmium"))

        // Allay Head
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.ALLAY_HEAD)
            .pattern("WDW")
            .pattern("ANA")
            .pattern("WDW")
            .define('W', ItemTags.WOOL)
            .define('D', Items.DIAMOND)
            .define('A', Items.AMETHYST_CLUSTER)
            .define('N', Items.NOTE_BLOCK)
            .unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(recipeOutput)

        storageBlockRecipe(recipeOutput, NanoMiraiItems.RAW_SCULMIUM_BLOCK, NanoMiraiItems.RAW_SCULMIUM)
        storageBlockRecipe(recipeOutput, NanoMiraiItems.SCULMIUM_BLOCK, NanoMiraiItems.SCULMIUM_INGOT)
    }

    private fun storageBlockRecipe(recipeOutput: RecipeOutput, big: Item, small: Item) {
        val bigItemId = BuiltInRegistries.ITEM.getKey(big)
        val smallItemId = BuiltInRegistries.ITEM.getKey(small)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, big)
            .requires(small, 9)
            .unlockedByItem(small)
            .save(recipeOutput, rl("${bigItemId.path}_from_${smallItemId.path}"))

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, small, 9)
            .requires(big)
            .unlockedByItem(big)
            .save(recipeOutput, rl("${smallItemId.path}_from_${bigItemId.path}"))
    }

    fun assemblerRecipes(recipeOutput: RecipeOutput) {
        // Simple Circuit
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.SIMPLE_CIRCUIT, 2),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(4, Items.REDSTONE),
                StackedIngredient.of(1, ItemTags.TERRACOTTA),
                StackedIngredient.of(2, Items.COPPER_INGOT),
                StackedIngredient.of(2, NanoMiraiItems.GRAPHITE),
            )
        ).save(recipeOutput)

        // Normal Circuit
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NORMAL_CIRCUIT, 1),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(8, Items.REDSTONE),
                StackedIngredient.of(1, Tags.Items.OBSIDIANS_NORMAL),
                StackedIngredient.of(4, Items.COPPER_INGOT),
                StackedIngredient.of(2, NanoMiraiItems.SILICON),
            )
        ).save(recipeOutput)

        // Nano Circuit
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_CIRCUIT, 1),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(16, Items.REDSTONE),
                StackedIngredient.of(1, NanoMiraiItemTags.REINFORCED_OBSIDIAN),
                StackedIngredient.of(2, Items.GOLD_INGOT),
                StackedIngredient.of(2, Items.ENDER_PEARL),
            )
        ).save(recipeOutput)

        // Sculmium Circuit
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.SCULMIUM_CIRCUIT, 1),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(16, Items.REDSTONE),
                StackedIngredient.of(1, NanoMiraiItemTags.REINFORCED_OBSIDIAN),
                StackedIngredient.of(4, Items.GOLD_INGOT),
                StackedIngredient.of(2, NanoMiraiItemTags.SCULMIUM_INGOT),
            )
        ).save(recipeOutput)
    }

    fun labAttributeRecipes(recipeOutput: RecipeOutput) {
        //  ** Proto Tier Attributes **
        // Scale Increase
        LabAttributeRecipeBuilder(
            Attributes.SCALE,
            AttributeModifier(
                rl("scale_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.GREEN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.BAMBOO),
                Ingredient.of(Items.BAMBOO),
                Ingredient.of(Items.ENDER_PEARL),
                Ingredient.of(Items.BAMBOO),
                Ingredient.of(Items.BAMBOO),
                Ingredient.of(Items.ENDER_PEARL),
            )
        ).save(recipeOutput)

        // Jump Boost
        LabAttributeRecipeBuilder(
            Attributes.JUMP_STRENGTH,
            AttributeModifier(
                rl("jump_strength_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.BLUE_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(Items.LEATHER),
                Ingredient.of(Items.RABBIT_FOOT),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(Items.LEATHER),
                Ingredient.of(Items.RABBIT_FOOT),
            )
        ).save(recipeOutput)

        //  ** Cell Tier Attributes **
        // Knockback Resistance
        LabAttributeRecipeBuilder(
            Attributes.KNOCKBACK_RESISTANCE,
            AttributeModifier(
                rl("knockback_resistance_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.RED_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.DEEPSLATE),
                Ingredient.of(Tags.Items.OBSIDIANS_NORMAL),
                Ingredient.of(Items.POINTED_DRIPSTONE),
                Ingredient.of(Items.DEEPSLATE),
                Ingredient.of(Tags.Items.OBSIDIANS_NORMAL),
                Ingredient.of(Items.POINTED_DRIPSTONE),
            )
        ).save(recipeOutput)

        // Scale Decrease
        LabAttributeRecipeBuilder(
            Attributes.SCALE,
            AttributeModifier(
                rl("scale_decrease"),
                -0.5,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.RED_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Tags.Items.SEEDS),
                Ingredient.of(Tags.Items.SEEDS),
                Ingredient.of(Items.STRING),
                Ingredient.of(Tags.Items.MUSHROOMS),
                Ingredient.of(Tags.Items.MUSHROOMS),
                Ingredient.of(Items.STRING),
            )
        ).save(recipeOutput)

        // Speed Increase
        LabAttributeRecipeBuilder(
            Attributes.MOVEMENT_SPEED,
            AttributeModifier(
                rl("speed_increase"),
                1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.BLUE_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.AMETHYST_SHARD),
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.AMETHYST_SHARD),
            )
        ).save(recipeOutput)

        // Water Movement Efficiency Increase
        LabAttributeRecipeBuilder(
            Attributes.WATER_MOVEMENT_EFFICIENCY,
            AttributeModifier(
                rl("water_movement_efficiency_increase"),
                1.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.GREEN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.COD),
                Ingredient.of(Items.COD),
                Ingredient.of(Items.NAUTILUS_SHELL),
                Ingredient.of(Items.COD),
                Ingredient.of(Items.COD),
                Ingredient.of(Items.NAUTILUS_SHELL),
            )
        ).save(recipeOutput)

        // Oxygen Bonus Increase
        LabAttributeRecipeBuilder(
            Attributes.OXYGEN_BONUS,
            AttributeModifier(
                rl("oxygen_bonus_increase"),
                128.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.BLUE_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.PUFFERFISH),
                Ingredient.of(Items.PUFFERFISH),
                Ingredient.of(Items.GLASS_BOTTLE),
                Ingredient.of(Items.PUFFERFISH),
                Ingredient.of(Items.PUFFERFISH),
                Ingredient.of(Items.GLASS_BOTTLE),
            )
        ).save(recipeOutput)

        //  ** Matrix Tier Attributes **
        // Sneaking Speed Increase
        LabAttributeRecipeBuilder(
            Attributes.SNEAKING_SPEED,
            AttributeModifier(
                rl("sneaking_speed_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.MAGENTA_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.SCULK),
                Ingredient.of(Items.SCULK),
                Ingredient.of(Items.SCULK),
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.SUGAR),
            )
        ).save(recipeOutput)

        // Step Height Increase
        LabAttributeRecipeBuilder(
            Attributes.STEP_HEIGHT,
            AttributeModifier(
                rl("step_height_increase"),
                0.4,
                AttributeModifier.Operation.ADD_VALUE
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.CYAN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.PISTON),
                Ingredient.of(Items.LEATHER),
                Ingredient.of(Items.LEATHER),
                Ingredient.of(Items.PISTON),
                Ingredient.of(Items.LEATHER),
                Ingredient.of(Items.LEATHER),
            )
        ).save(recipeOutput)

        // Block Interaction Range Increase
        LabAttributeRecipeBuilder(
            Attributes.BLOCK_INTERACTION_RANGE,
            AttributeModifier(
                rl("block_interaction_range_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.CYAN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.DROPPER),
                Ingredient.of(Items.PISTON),
                Ingredient.of(Items.ARMOR_STAND),
                Ingredient.of(Items.DROPPER),
                Ingredient.of(Items.PISTON),
                Ingredient.of(Items.ARMOR_STAND),
            )
        ).save(recipeOutput)

        // Entity Interaction Range Increase
        LabAttributeRecipeBuilder(
            Attributes.ENTITY_INTERACTION_RANGE,
            AttributeModifier(
                rl("entity_interaction_range_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.CYAN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.FISHING_ROD),
                Ingredient.of(Items.PISTON),
                Ingredient.of(Items.ARMOR_STAND),
                Ingredient.of(Items.FISHING_ROD),
                Ingredient.of(Items.PISTON),
                Ingredient.of(Items.ARMOR_STAND),
            )
        ).save(recipeOutput)

        // Attack Speed Increase
        LabAttributeRecipeBuilder(
            Attributes.ATTACK_SPEED,
            AttributeModifier(
                rl("attack_speed_increase"),
                0.8,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.YELLOW_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.SHROOMLIGHT),
                Ingredient.of(Items.SHROOMLIGHT),
                Ingredient.of(Items.SHROOMLIGHT),
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.SUGAR),
            )
        ).save(recipeOutput)

        // Submerged Mining Speed Increase
        LabAttributeRecipeBuilder(
            Attributes.SUBMERGED_MINING_SPEED,
            AttributeModifier(
                rl("submerged_mining_speed_increase"),
                4.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.CYAN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.SALMON),
                Ingredient.of(Items.TROPICAL_FISH),
                Ingredient.of(Items.SALMON),
                Ingredient.of(Items.TROPICAL_FISH),
                Ingredient.of(Items.SALMON),
                Ingredient.of(Items.TROPICAL_FISH),
            )
        ).save(recipeOutput)

        //  ** Singularity Tier Attributes **
        // Health Increase
        LabAttributeRecipeBuilder(
            Attributes.MAX_HEALTH,
            AttributeModifier(
                rl("max_health_increase"),
                2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.MAGENTA_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.TOTEM_OF_UNDYING),
                Ingredient.of(Items.GLISTERING_MELON_SLICE),
                Ingredient.of(Items.GLISTERING_MELON_SLICE),
                Ingredient.of(Items.TOTEM_OF_UNDYING),
                Ingredient.of(Items.GOLDEN_APPLE),
                Ingredient.of(Items.GOLDEN_APPLE),
            )
        ).save(recipeOutput)

        // Attack Damage Increase
        LabAttributeRecipeBuilder(
            Attributes.ATTACK_DAMAGE,
            AttributeModifier(
                rl("attack_damage_increase"),
                1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.YELLOW_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.BLAZE_ROD),
                Ingredient.of(Items.BLAZE_ROD),
                Ingredient.of(Items.SHROOMLIGHT),
                Ingredient.of(Items.GLOWSTONE_DUST),
                Ingredient.of(Items.GLOWSTONE_DUST),
                Ingredient.of(Items.SHROOMLIGHT),
            )
        ).save(recipeOutput)

        // Fall Damage Reduction
        LabAttributeRecipeBuilder(
            Attributes.FALL_DAMAGE_MULTIPLIER,
            AttributeModifier(
                rl("fall_damage_reduction"),
                -1.0,
                AttributeModifier.Operation.ADD_VALUE
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.MAGENTA_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.FEATHER),
                Ingredient.of(Items.WIND_CHARGE),
                Ingredient.of(Items.WIND_CHARGE),
                Ingredient.of(Items.FEATHER),
                Ingredient.of(Items.HAY_BLOCK),
                Ingredient.of(Items.HAY_BLOCK),
            )
        ).save(recipeOutput)
    }

    fun labEffectRecipes(recipeOutput: RecipeOutput) {
        //  ** Proto Tier Effects **
        // Glowing
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.GLOWING,
                600, 0, false, true, true
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.RED_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.GLOWSTONE_DUST),
                Ingredient.of(Items.GLOWSTONE_DUST),
                Ingredient.of(Items.GLOWSTONE_DUST),
                Ingredient.of(Items.GLOW_BERRIES),
                Ingredient.of(Items.GLOW_BERRIES),
                Ingredient.of(Items.GLOW_BERRIES),
            )
        ).save(recipeOutput)

        // Invisibility
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.INVISIBILITY,
                600, 0, false, true, true
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.BLUE_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.ENDER_PEARL),
                Ingredient.of(Items.GLASS),
                Ingredient.of(Items.GUNPOWDER),
                Ingredient.of(Items.ENDER_PEARL),
                Ingredient.of(Items.GLASS),
                Ingredient.of(Items.GUNPOWDER),
            )
        ).save(recipeOutput)

        // Slow Falling
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.SLOW_FALLING,
                600, 0, false, true, true
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.GREEN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.FEATHER),
                Ingredient.of(ItemTags.WOOL),
                Ingredient.of(Items.FEATHER),
                Ingredient.of(Items.FEATHER),
                Ingredient.of(ItemTags.WOOL),
                Ingredient.of(Items.FEATHER),
            )
        ).save(recipeOutput)

        // Infested
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.INFESTED,
                600, 0, false, true, true
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.RED_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.STONE),
                Ingredient.of(Items.STONE),
                Ingredient.of(Items.STONE),
                Ingredient.of(Items.STONE),
                Ingredient.of(Items.STONE),
                Ingredient.of(Items.STONE),
            )
        ).save(recipeOutput)

        // Weaving
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.WEAVING,
                600, 0, false, true, true
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.RED_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.STRING),
                Ingredient.of(Items.STRING),
                Ingredient.of(Items.STRING),
                Ingredient.of(Items.COBWEB),
                Ingredient.of(Items.COBWEB),
                Ingredient.of(Items.COBWEB),
            )
        ).save(recipeOutput)

        // Wind Charged
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.WIND_CHARGED,
                600, 0, false, true, true
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.BLUE_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.BREEZE_ROD),
                Ingredient.of(Items.WIND_CHARGE),
                Ingredient.of(Items.WIND_CHARGE),
                Ingredient.of(Items.BREEZE_ROD),
                Ingredient.of(Items.WIND_CHARGE),
                Ingredient.of(Items.WIND_CHARGE),
            )
        ).save(recipeOutput)

        // Oozing
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.OOZING,
                600, 0, false, true, true
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.GREEN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.SLIME_BLOCK),
                Ingredient.of(Items.SLIME_BLOCK),
                Ingredient.of(Items.SLIME_BLOCK),
                Ingredient.of(Items.SLIME_BLOCK),
                Ingredient.of(Items.SLIME_BLOCK),
                Ingredient.of(Items.SLIME_BLOCK),
            )
        ).save(recipeOutput)

        //  ** Cell Tier Effects **
        // Slowness
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                600, 2, false, true, true
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.BLUE_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.COBWEB),
                Ingredient.of(Items.FERMENTED_SPIDER_EYE),
                Ingredient.of(Items.COBWEB),
                Ingredient.of(Items.COBWEB),
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.COBWEB),
            )
        ).save(recipeOutput)

        // Weakness
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.WEAKNESS,
                600, 2, false, true, true
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.RED_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.BONE_MEAL),
                Ingredient.of(Items.BLAZE_POWDER),
                Ingredient.of(Items.BLAZE_POWDER),
                Ingredient.of(Items.ROTTEN_FLESH),
                Ingredient.of(Items.FERMENTED_SPIDER_EYE),
                Ingredient.of(Items.FERMENTED_SPIDER_EYE),
            )
        ).save(recipeOutput)

        // Levitation
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.LEVITATION,
                300, 2, false, true, true
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.GREEN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.FEATHER),
                Ingredient.of(Items.POPPED_CHORUS_FRUIT),
                Ingredient.of(Items.POPPED_CHORUS_FRUIT),
                Ingredient.of(Items.FEATHER),
                Ingredient.of(Items.POPPED_CHORUS_FRUIT),
                Ingredient.of(Items.POPPED_CHORUS_FRUIT),
            )
        ).save(recipeOutput)

        //  ** Matrix Tier Effects **
        // Poison
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.POISON,
                600, 2, false, true, true
            ),
            NanoTier.NORMAL,
            Ingredient.of(NanoMiraiItems.CYAN_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.PUFFERFISH),
                Ingredient.of(Items.POISONOUS_POTATO),
                Ingredient.of(Items.SPIDER_EYE),
                Ingredient.of(Items.PUFFERFISH),
                Ingredient.of(Items.POISONOUS_POTATO),
                Ingredient.of(Items.SPIDER_EYE),
            )
        ).save(recipeOutput)

        //  ** Singularity Tier Effects **
        // Darkness
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.DARKNESS,
                600, 0, false, true, true
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.MAGENTA_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.BLACK_DYE),
                Ingredient.of(Items.BLACK_DYE),
                Ingredient.of(Items.BLACK_DYE),
                Ingredient.of(Items.SCULK),
                Ingredient.of(Items.SCULK),
                Ingredient.of(Items.SCULK),
            )
        ).save(recipeOutput)

        // Wither
        LabEffectRecipeBuilder(
            MobEffectInstance(
                MobEffects.WITHER,
                100, 1, false, true, true
            ),
            NanoTier.IMPROVED,
            Ingredient.of(NanoMiraiItems.YELLOW_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.WITHER_ROSE),
                Ingredient.of(Items.STONE_SWORD),
                Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL),
                Ingredient.of(Items.WITHER_ROSE),
                Ingredient.of(Items.STONE_SWORD),
                Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL),
            )
        ).save(recipeOutput)
    }

    fun laserRecipes(recipeOutput: RecipeOutput) {
        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.SILICON_WAFER),
            Ingredient.of(NanoMiraiItems.SILICON),
            Ingredient.of(NanoMiraiItems.LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(Items.CRYING_OBSIDIAN),
            Ingredient.of(Tags.Items.OBSIDIANS_NORMAL),
            Ingredient.of(NanoMiraiItems.LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(Items.ECHO_SHARD),
            Ingredient.of(Items.AMETHYST_SHARD),
            Ingredient.of(NanoMiraiItems.SCULK_LENS)
        ).save(recipeOutput, rl("echo_shard_from_amethyst_shard"))

        LaserRecipeBuilder(
            ItemStack(Items.GLOW_LICHEN),
            Ingredient.of(Items.VINE),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS)
        ).save(recipeOutput, rl("glow_lichen_from_vine"))

        LaserRecipeBuilder(
            ItemStack(Items.SCULK_VEIN),
            Ingredient.of(Items.VINE),
            Ingredient.of(NanoMiraiItems.SCULK_LENS)
        ).save(recipeOutput, rl("sculk_vein_from_vine"))

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.RED_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiItemTags.SHERD_WARM_OCEAN_RUINS),
            Ingredient.of(NanoMiraiItems.LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.GREEN_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiItemTags.SHERD_DESERT_PYRAMID),
            Ingredient.of(NanoMiraiItems.LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.BLUE_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiItemTags.SHERD_COLD_OCEAN_RUINS),
            Ingredient.of(NanoMiraiItems.LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.CYAN_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiItemTags.SHERD_TRIAL_CHAMBER),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.MAGENTA_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiItemTags.SHERD_TRAIL_RUINS),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.YELLOW_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiItemTags.SHERD_DESERT_WELL),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS)
        ).save(recipeOutput)
    }

    fun synthesizeRecipes(recipeOutput: RecipeOutput) {
        // Improved Synthesize Nano
        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.SYNTHESIZE_NANO_IMPROVED),
            NanoTier.NORMAL,
            BlockStateWithNbt(
                NanoMiraiBlocks.MOB_CAGE.defaultBlockState(),
                SynthesizeUtil.createEntityData(EntityType.ALLAY)
            ),
            Ingredient.of(Items.AMETHYST_SHARD),
            300
        ).save(recipeOutput)

        // Improved Support Nano
        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.SUPPORT_NANO_IMPROVED),
            NanoTier.NORMAL,
            BlockStateWithNbt(
                NanoMiraiBlocks.MOB_CAGE.defaultBlockState(),
                SynthesizeUtil.createEntityData(EntityType.AXOLOTL)
            ),
            Ingredient.of(NanoMiraiItems.SUPPORT_NANO_NORMAL),
            300
        ).save(recipeOutput)

        // Improved Nano Swarm Blaster
        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_SWARM_BLASTER_IMPROVED),
            NanoTier.NORMAL,
            BlockStateWithNbt(
                NanoMiraiBlocks.MOB_CAGE.defaultBlockState(),
                SynthesizeUtil.createEntityData(EntityType.WARDEN)
            ),
            Ingredient.of(NanoMiraiItems.NANO_SWARM_BLASTER_NORMAL),
            300
        ).save(recipeOutput)

        // Nanomachine Assembler
        SynthesizeRecipeBuilder.default(
            ItemStack(NanoMiraiItems.NANOMACHINE_ASSEMBLER),
            NanoTier.NORMAL,
            Blocks.CRAFTER,
            Ingredient.of(NanoMiraiItems.NORMAL_CIRCUIT),
            200
        ).save(recipeOutput)

        // Laser Engraver
        SynthesizeRecipeBuilder.default(
            ItemStack(NanoMiraiItems.LASER_ENGRAVER),
            NanoTier.NORMAL,
            Blocks.BLAST_FURNACE,
            Ingredient.of(NanoMiraiItems.LASER_COMPONENT),
            200
        ).save(recipeOutput)

        // Nano Lab
        SynthesizeRecipeBuilder.default(
            ItemStack(NanoMiraiItems.NANO_LAB),
            NanoTier.NORMAL,
            Blocks.ENCHANTING_TABLE,
            Ingredient.of(NanoMiraiItems.NORMAL_CIRCUIT),
            400
        ).save(recipeOutput)

        SynthesizeRecipeBuilder.default(
            ItemStack(NanoMiraiItems.GRAPHITE, 5),
            NanoTier.NORMAL,
            Blocks.COAL_BLOCK,
            Ingredient.of(NanoMiraiItems.GRAPHITE),
            60
        ).save(recipeOutput)

        SynthesizeRecipeBuilder.default(
            ItemStack(NanoMiraiItems.AMETHYST_LENS),
            NanoTier.NORMAL,
            Blocks.AMETHYST_BLOCK,
            Ingredient.of(NanoMiraiItems.LENS),
            200
        ).save(recipeOutput)

        SynthesizeRecipeBuilder.default(
            ItemStack(NanoMiraiItems.SCULK_LENS),
            NanoTier.IMPROVED,
            Blocks.SCULK_CATALYST,
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS),
            600
        ).save(recipeOutput)

        // Raw Sculmium Block
        SynthesizeRecipeBuilder.default(
            ItemStack(NanoMiraiItems.RAW_SCULMIUM_BLOCK),
            NanoTier.IMPROVED,
            Blocks.RAW_IRON_BLOCK,
            Ingredient.of(Items.ECHO_SHARD),
            600
        ).save(recipeOutput)

        SynthesizeRecipeBuilder.default(
            ItemStack(Items.SCULK_CATALYST),
            NanoTier.IMPROVED,
            Blocks.BONE_BLOCK,
            Ingredient.of(NanoMiraiItemTags.SCULMIUM_INGOT),
            200
        ).save(recipeOutput, rl("sculk_catalyst_from_sculmium_ingot"))

        SynthesizeRecipeBuilder.default(
            ItemStack(Items.SCULK_SHRIEKER),
            NanoTier.IMPROVED,
            Blocks.BONE_BLOCK,
            Ingredient.of(NanoMiraiItems.SCULMIUM_CIRCUIT),
            200
        ).save(recipeOutput, rl("sculk_shrieker_from_sculmium_circuit"))

        SynthesizeRecipeBuilder.default(
            ItemStack(Items.BUDDING_AMETHYST),
            NanoTier.NORMAL,
            Blocks.RAW_COPPER_BLOCK,
            Ingredient.of(Items.LAPIS_LAZULI),
            200
        ).save(recipeOutput, rl("budding_amethyst_from_lapis_lazuli"))

        SynthesizeRecipeBuilder.default(
            ItemStack(NanoMiraiItems.REINFORCED_OBSIDIAN),
            NanoTier.IMPROVED,
            Blocks.CRYING_OBSIDIAN,
            Ingredient.of(Items.AMETHYST_SHARD),
            600
        ).save(recipeOutput)

        SynthesizeRecipeBuilder(
            ItemStack(Items.WIND_CHARGE, 8),
            NanoTier.NORMAL,
            BlockStateWithNbt(
                NanoMiraiBlocks.MOB_CAGE.defaultBlockState(),
                SynthesizeUtil.createEntityData(EntityType.BREEZE)
            ),
            Ingredient.of(Items.BREEZE_ROD),
            3000
        ).save(recipeOutput, rl("wind_charge_from_blaze"))

        SynthesizeRecipeBuilder(
            ItemStack(Items.TNT),
            NanoTier.NORMAL,
            BlockStateWithNbt(
                NanoMiraiBlocks.MOB_CAGE.defaultBlockState(),
                SynthesizeUtil.createEntityData(EntityType.CREEPER)
            ),
            Ingredient.of(Items.SAND),
            3000
        ).save(recipeOutput, rl("tnt_from_creeper"))
    }

    private fun RecipeBuilder.unlockedByItem(item: Item): RecipeBuilder {
        val itemId = BuiltInRegistries.ITEM.getKey(item)
        return this.unlockedBy(
            "has_${itemId.toString().replace(':', '_')}",
            has(item)
        )
    }

    private fun RecipeBuilder.unlockedByItem(item: TagKey<Item>): RecipeBuilder {
        val itemId = item.location
        return this.unlockedBy(
            "has_${itemId.toString().replace(':', '_')}",
            has(item)
        )
    }
}
