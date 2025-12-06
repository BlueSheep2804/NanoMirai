package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.recipe.StackedIngredient
import dev.bluesheep.nanomirai.recipe.assembler.AssemblerRecipeBuilder
import dev.bluesheep.nanomirai.recipe.lab.attribute.LabAttributeRecipeBuilder
import dev.bluesheep.nanomirai.recipe.lab.effect.LabEffectRecipeBuilder
import dev.bluesheep.nanomirai.recipe.laser.LaserRecipeBuilder
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipeBuilder
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiTags
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
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

        // Repair Nano
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NanoMiraiItems.REPAIR_NANO)
            .requires(NanoMiraiItems.GRAPHITE)
            .requires(Ingredient.of(NanoMiraiTags.REPAIR_NANO_INGREDIENTS), 8)
            .unlockedBy("has_graphite", has(NanoMiraiItems.GRAPHITE))
            .save(recipeOutput, rl("repair_nano"))

        // Graphite
        SimpleCookingRecipeBuilder.blasting(
            Ingredient.of(Items.COAL),
            RecipeCategory.MISC,
            NanoMiraiItems.GRAPHITE,
            0f,
            100
        )
            .unlockedBy("has_coal", has(Items.COAL))
            .save(recipeOutput)

        // Silicon
        SimpleCookingRecipeBuilder.blasting(
            Ingredient.of(Items.QUARTZ),
            RecipeCategory.MISC,
            NanoMiraiItems.SILICON,
            0f,
            100
        )
            .unlockedBy("has_quartz", has(Items.QUARTZ))
            .save(recipeOutput, rl("silicon_from_blasting"))

        // Raw Echo Steel
        ShapelessRecipeBuilder.shapeless(
            RecipeCategory.MISC,
            NanoMiraiItems.RAW_SCULMIUM
        )
            .requires(Items.SCULK)
            .requires(Items.SCULK)
            .requires(NanoMiraiItems.GRAPHITE)
            .requires(Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(recipeOutput)

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

        // Nanomachine Assembler
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.NANOMACHINE_ASSEMBLER)
            .define('I', Items.IRON_INGOT)
            .define('C', NanoMiraiItems.SIMPLE_CIRCUIT)
            .define('D', Items.DIAMOND)
            .define('T', Items.CRAFTING_TABLE)
            .pattern("ICI")
            .pattern("CDC")
            .pattern("ITI")
            .unlockedBy("has_simple_circuit", has(NanoMiraiItems.SIMPLE_CIRCUIT))
            .save(recipeOutput)

        // Laser Engraver
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.LASER_ENGRAVER)
            .define('I', Items.IRON_INGOT)
            .define('L', Items.REDSTONE_LAMP)
            .define('C', NanoMiraiItems.NORMAL_CIRCUIT)
            .define('G', Tags.Items.GLASS_BLOCKS)
            .define('O', Items.OBSIDIAN)
            .pattern("ILI")
            .pattern("CGC")
            .pattern("IOI")
            .unlockedBy("has_normal_circuit", has(NanoMiraiItems.NORMAL_CIRCUIT))
            .save(recipeOutput)

        // Nano Lab
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NanoMiraiItems.NANO_LAB)
            .define('I', Items.IRON_INGOT)
            .define('E', Items.EMERALD)
            .define('C', NanoMiraiItems.SIMPLE_CIRCUIT)
            .define('S', Items.SPYGLASS)
            .define('B', Items.IRON_BLOCK)
            .pattern("IEI")
            .pattern("CSC")
            .pattern("IBI")
            .unlockedBy("has_simple_circuit", has(NanoMiraiItems.SIMPLE_CIRCUIT))
            .save(recipeOutput)

        // Sculk Sensor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SCULK_SENSOR)
            .define('C', NanoMiraiTags.SCULMIUM_INGOT)
            .define('V', Items.SCULK_VEIN)
            .define('S', Items.SCULK)
            .pattern("VCV")
            .pattern("SSS")
            .unlockedBy("has_sculmium_ingot", has(NanoMiraiItems.SCULMIUM_INGOT))
            .save(recipeOutput, rl("sculk_sensor_from_sculkium"))
    }

    fun assemblerRecipes(recipeOutput: RecipeOutput) {
        // Simple Circuit
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.SIMPLE_CIRCUIT, 2),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(2, Items.REDSTONE),
                StackedIngredient.of(1, NanoMiraiItems.GRAPHITE),
                StackedIngredient.of(1, Items.COPPER_INGOT),
                StackedIngredient.of(1, ItemTags.TERRACOTTA),
            )
        ).save(recipeOutput)

        // Normal Circuit
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NORMAL_CIRCUIT, 1),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(2, NanoMiraiItems.SILICON),
                StackedIngredient.of(2, Items.IRON_INGOT),
                StackedIngredient.of(1, Items.OBSIDIAN),
                StackedIngredient.of(1, Items.REPEATER),
                StackedIngredient.of(2, Items.REDSTONE),
            )
        ).save(recipeOutput)

        // Nano Circuit
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.NANO_CIRCUIT, 1),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(2, NanoMiraiItems.SILICON_WAFER),
                StackedIngredient.of(2, Items.GOLD_INGOT),
                StackedIngredient.of(1, NanoMiraiBlocks.REINFORCED_OBSIDIAN),
                StackedIngredient.of(2, Items.REDSTONE),
                StackedIngredient.of(1, Items.OBSERVER),
            )
        ).save(recipeOutput)

        // Sculmium Circuit
        AssemblerRecipeBuilder(
            ItemStack(NanoMiraiItems.SCULMIUM_CIRCUIT, 1),
            NonNullList.of(
                StackedIngredient.EMPTY,
                StackedIngredient.of(4, NanoMiraiItems.SILICON_WAFER),
                StackedIngredient.of(2, NanoMiraiTags.SCULMIUM_INGOT),
                StackedIngredient.of(1, NanoMiraiBlocks.REINFORCED_OBSIDIAN),
                StackedIngredient.of(4, Items.REDSTONE),
                StackedIngredient.of(2, Items.SCULK_SENSOR),
            )
        ).save(recipeOutput)

        val tierCircuits = mapOf(
            NanoTier.MK1 to NanoMiraiItems.SIMPLE_CIRCUIT,
            NanoTier.MK2 to NanoMiraiItems.NORMAL_CIRCUIT,
            NanoTier.MK3 to NanoMiraiItems.NANO_CIRCUIT,
            NanoTier.MK4 to NanoMiraiItems.SCULMIUM_CIRCUIT
        )
        // Synthesize Nano
        NanoTier.entries.forEach {
            AssemblerRecipeBuilder(
                it.getSynthesizeNano(),
                NonNullList.of(
                    StackedIngredient.EMPTY,
                    StackedIngredient.of(4, tierCircuits[it]!!),
                    StackedIngredient.of(2, Items.NOTE_BLOCK),
                    StackedIngredient.of(4, Items.AMETHYST_SHARD),
                )
            ).save(recipeOutput, rl("synthesize_nano_${it.name.lowercase()}"))
        }

        // Support Nano
        NanoTier.entries.forEach {
            AssemblerRecipeBuilder(
                it.getSupportNano(),
                NonNullList.of(
                    StackedIngredient.EMPTY,
                    StackedIngredient.of(4, tierCircuits[it]!!),
                    StackedIngredient.of(2, Items.ENDER_PEARL),
                    StackedIngredient.of(4, Items.KELP),
                    StackedIngredient.of(1, Items.AXOLOTL_BUCKET),
                )
            ).save(recipeOutput, rl("support_nano_${it.name.lowercase()}"))
        }

        // Nano Swarm Blaster
        NanoTier.entries.forEach {
            AssemblerRecipeBuilder(
                it.getNanoSwarmBlaster(),
                NonNullList.of(
                    StackedIngredient.EMPTY,
                    StackedIngredient.of(4, tierCircuits[it]!!),
                    StackedIngredient.of(4, Items.IRON_INGOT),
                    StackedIngredient.of(2, Items.PISTON),
                    StackedIngredient.of(1, Items.SCULK_SHRIEKER),
                )
            ).save(recipeOutput, rl("nano_swarm_blaster_${it.name.lowercase()}"))
        }
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
            NanoTier.MK1,
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
            NanoTier.MK1,
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
            NanoTier.MK2,
            Ingredient.of(NanoMiraiItems.RED_RESEARCH_CATALYST),
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.DEEPSLATE),
                Ingredient.of(Items.OBSIDIAN),
                Ingredient.of(Items.POINTED_DRIPSTONE),
                Ingredient.of(Items.DEEPSLATE),
                Ingredient.of(Items.OBSIDIAN),
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
            NanoTier.MK2,
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
            NanoTier.MK2,
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
            NanoTier.MK2,
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
            NanoTier.MK2,
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
            NanoTier.MK3,
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
            NanoTier.MK3,
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
            NanoTier.MK3,
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
            NanoTier.MK3,
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
            NanoTier.MK3,
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
            NanoTier.MK3,
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
            NanoTier.MK4,
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
            NanoTier.MK4,
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
            NanoTier.MK4,
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
            NanoTier.MK1,
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
            NanoTier.MK1,
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
            NanoTier.MK1,
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
            NanoTier.MK1,
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
            NanoTier.MK1,
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
            NanoTier.MK1,
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
            NanoTier.MK1,
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
            NanoTier.MK2,
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
            NanoTier.MK2,
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
            NanoTier.MK2,
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
            NanoTier.MK3,
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
            NanoTier.MK4,
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
            NanoTier.MK4,
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
            Ingredient.of(NanoMiraiItems.SILICON)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.SCULMIUM_INGOT),
            Ingredient.of(NanoMiraiTags.RAW_SCULMIUM),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS)
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
            Ingredient.of(NanoMiraiTags.SHERD_WARM_OCEAN_RUINS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.GREEN_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiTags.SHERD_DESERT_PYRAMID)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.BLUE_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiTags.SHERD_COLD_OCEAN_RUINS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.CYAN_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiTags.SHERD_TRIAL_CHAMBER),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.MAGENTA_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiTags.SHERD_TRAIL_RUINS),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS)
        ).save(recipeOutput)

        LaserRecipeBuilder(
            ItemStack(NanoMiraiItems.YELLOW_RESEARCH_CATALYST),
            Ingredient.of(NanoMiraiTags.SHERD_DESERT_WELL),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS)
        ).save(recipeOutput)
    }

    fun synthesizeRecipes(recipeOutput: RecipeOutput) {
        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.GRAPHITE, 5),
            NanoTier.MK1,
            Blocks.COAL_BLOCK.defaultBlockState(),
            Ingredient.of(NanoMiraiItems.GRAPHITE),
            60
        ).save(recipeOutput)

        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.AMETHYST_LENS),
            NanoTier.MK3,
            Blocks.TINTED_GLASS.defaultBlockState(),
            Ingredient.of(Items.AMETHYST_SHARD),
            60
        ).save(recipeOutput)

        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.SCULK_LENS),
            NanoTier.MK4,
            Blocks.SCULK_CATALYST.defaultBlockState(),
            Ingredient.of(NanoMiraiItems.AMETHYST_LENS),
            200
        ).save(recipeOutput)

        SynthesizeRecipeBuilder(
            ItemStack(Items.SCULK_CATALYST),
            NanoTier.MK4,
            Blocks.BONE_BLOCK.defaultBlockState(),
            Ingredient.of(NanoMiraiTags.SCULMIUM_INGOT),
            200
        ).save(recipeOutput, rl("sculk_catalyst_from_sculmium_ingot"))

        SynthesizeRecipeBuilder(
            ItemStack(Items.SCULK_SHRIEKER),
            NanoTier.MK4,
            Blocks.BONE_BLOCK.defaultBlockState(),
            Ingredient.of(NanoMiraiItems.SCULMIUM_CIRCUIT),
            200
        ).save(recipeOutput, rl("sculk_shrieker_from_sculmium_circuit"))

        SynthesizeRecipeBuilder(
            ItemStack(Items.CRAFTER),
            NanoTier.MK1,
            Blocks.CRAFTING_TABLE.defaultBlockState(),
            Ingredient.of(Items.IRON_HELMET),
            20
        ).save(recipeOutput, rl("crafter_from_iron_helmet"))

        SynthesizeRecipeBuilder(
            ItemStack(Items.BUDDING_AMETHYST),
            NanoTier.MK1,
            Blocks.RAW_COPPER_BLOCK.defaultBlockState(),
            Ingredient.of(Items.LAPIS_LAZULI),
            200
        ).save(recipeOutput, rl("budding_amethyst_from_lapis_lazuli"))

        SynthesizeRecipeBuilder(
            ItemStack(NanoMiraiItems.REINFORCED_OBSIDIAN),
            NanoTier.MK2,
            Blocks.OBSIDIAN.defaultBlockState(),
            Ingredient.of(Items.POPPED_CHORUS_FRUIT),
            600
        ).save(recipeOutput)
    }
}
