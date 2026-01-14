package dev.bluesheep.nanomirai

import net.neoforged.neoforge.common.ModConfigSpec

object NanoMiraiConfig {
    private val BUILDER = ModConfigSpec.Builder()

    val processingSpeedMultiplierMk1: ModConfigSpec.ConfigValue<Double> = BUILDER
        .comment("Processing speed multiplier for Synthesize Nano Mk.1")
        .defineInRange("processingSpeedMultiplierMk1", 1.0, 1.0, Double.MAX_VALUE)

    val processingSpeedMultiplierMk2: ModConfigSpec.ConfigValue<Double> = BUILDER
        .comment("Processing speed multiplier for Synthesize Nano Mk.2")
        .defineInRange("processingSpeedMultiplierMk2", 1.2, 1.0, Double.MAX_VALUE)

    val processingSpeedMultiplierMk3: ModConfigSpec.ConfigValue<Double> = BUILDER
        .comment("Processing speed multiplier for Synthesize Nano Mk.3")
        .defineInRange("processingSpeedMultiplierMk3", 1.5, 1.0, Double.MAX_VALUE)

    val processingSpeedMultiplierMk4: ModConfigSpec.ConfigValue<Double> = BUILDER
        .comment("Processing speed multiplier for Synthesize Nano Mk.4")
        .defineInRange("processingSpeedMultiplierMk4", 2.0, 1.0, Double.MAX_VALUE)

    val maxAttributesMk1: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum attributes for Support Nano Mk.1")
        .defineInRange("maxAttributesMk1", 1, 1, Int.MAX_VALUE)

    val maxAttributesMk2: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum attributes for Support Nano Mk.2")
        .defineInRange("maxAttributesMk2", 2, 1, Int.MAX_VALUE)

    val maxAttributesMk3: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum attributes for Support Nano Mk.3")
        .defineInRange("maxAttributesMk3", 3, 1, Int.MAX_VALUE)

    val maxAttributesMk4: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum attributes for Support Nano Mk.4")
        .defineInRange("maxAttributesMk4", 4, 1, Int.MAX_VALUE)

    val maxEffectsMk1: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum effects for Nano Swarm Blaster Mk.1")
        .defineInRange("maxEffectsMk1", 1, 1, Int.MAX_VALUE)

    val maxEffectsMk2: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum effects for Nano Swarm Blaster Mk.2")
        .defineInRange("maxEffectsMk2", 2, 1, Int.MAX_VALUE)

    val maxEffectsMk3: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum effects for Nano Swarm Blaster Mk.3")
        .defineInRange("maxEffectsMk3", 3, 1, Int.MAX_VALUE)

    val maxEffectsMk4: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum effects for Nano Swarm Blaster Mk.4")
        .defineInRange("maxEffectsMk4", 4, 1, Int.MAX_VALUE)

    val blasterCooldownMk1: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Cooldown (in ticks) for Nano Swarm Blaster Mk.1")
        .defineInRange("blasterCooldownMk1", 120, 0, Int.MAX_VALUE)

    val blasterCooldownMk2: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Cooldown (in ticks) for Nano Swarm Blaster Mk.2")
        .defineInRange("blasterCooldownMk2", 80, 0, Int.MAX_VALUE)

    val blasterCooldownMk3: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Cooldown (in ticks) for Nano Swarm Blaster Mk.3")
        .defineInRange("blasterCooldownMk3", 40, 0, Int.MAX_VALUE)

    val blasterCooldownMk4: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Cooldown (in ticks) for Nano Swarm Blaster Mk.4")
        .defineInRange("blasterCooldownMk4", 20, 0, Int.MAX_VALUE)

    val SPEC: ModConfigSpec = BUILDER.build()
}