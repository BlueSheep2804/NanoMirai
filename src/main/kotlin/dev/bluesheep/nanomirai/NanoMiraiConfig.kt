package dev.bluesheep.nanomirai

import net.neoforged.neoforge.common.ModConfigSpec

object NanoMiraiConfig {
    private val BUILDER = ModConfigSpec.Builder()

    val processingSpeedMultiplierNormal: ModConfigSpec.ConfigValue<Double> = BUILDER
        .comment("Processing speed multiplier for Synthesize Nano")
        .defineInRange("processingSpeedMultiplierNormal", 1.0, 1.0, Double.MAX_VALUE)

    val processingSpeedMultiplierImproved: ModConfigSpec.ConfigValue<Double> = BUILDER
        .comment("Processing speed multiplier for Improved Synthesize Nano")
        .defineInRange("processingSpeedMultiplierImproved", 2.0, 1.0, Double.MAX_VALUE)

    val maxAttributesNormal: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum attributes for Support Nano")
        .defineInRange("maxAttributesNormal", 2, 1, Int.MAX_VALUE)

    val maxAttributesImproved: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum attributes for Improved Support Nano")
        .defineInRange("maxAttributesImproved", 4, 1, Int.MAX_VALUE)

    val maxEffectsNormal: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum effects for Nano Swarm Blaster")
        .defineInRange("maxEffectsNormal", 2, 1, Int.MAX_VALUE)

    val maxEffectsImproved: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Maximum effects for Improved Nano Swarm Blaster")
        .defineInRange("maxEffectsImproved", 4, 1, Int.MAX_VALUE)

    val blasterCooldownNormal: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Cooldown (in ticks) for Nano Swarm Blaster")
        .defineInRange("blasterCooldownNormal", 160, 0, Int.MAX_VALUE)

    val blasterCooldownImproved: ModConfigSpec.ConfigValue<Int> = BUILDER
        .comment("Cooldown (in ticks) for Improved Nano Swarm Blaster")
        .defineInRange("blasterCooldownImproved", 80, 0, Int.MAX_VALUE)

    val SPEC: ModConfigSpec = BUILDER.build()
}