package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.NanoMiraiConfig
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.ModConfigSpec

enum class NanoTier(
    val rarity: Rarity,
    private val processingSpeedMultiplierConfig: ModConfigSpec.ConfigValue<Double>,
    private val maxAttributesConfig: ModConfigSpec.ConfigValue<Int>,
    private val maxEffectsConfig: ModConfigSpec.ConfigValue<Int>,
    private val blasterCooldownConfig: ModConfigSpec.ConfigValue<Int>
) {
    MK1(
        Rarity.COMMON,
        NanoMiraiConfig.processingSpeedMultiplierMk1,
        NanoMiraiConfig.maxAttributesMk1,
        NanoMiraiConfig.maxEffectsMk1,
        NanoMiraiConfig.blasterCooldownMk1
    ),
    MK2(
        Rarity.UNCOMMON,
        NanoMiraiConfig.processingSpeedMultiplierMk2,
        NanoMiraiConfig.maxAttributesMk2,
        NanoMiraiConfig.maxEffectsMk2,
        NanoMiraiConfig.blasterCooldownMk2
    ),
    MK3(
        Rarity.RARE,
        NanoMiraiConfig.processingSpeedMultiplierMk3,
        NanoMiraiConfig.maxAttributesMk3,
        NanoMiraiConfig.maxEffectsMk3,
        NanoMiraiConfig.blasterCooldownMk3
    ),
    MK4(
        Rarity.EPIC,
        NanoMiraiConfig.processingSpeedMultiplierMk4,
        NanoMiraiConfig.maxAttributesMk4,
        NanoMiraiConfig.maxEffectsMk4,
        NanoMiraiConfig.blasterCooldownMk4
    );

    val nameComponent: Component
        get() = Component.translatable("nanomirai.nano_tier.${name.lowercase()}").withStyle(rarity.styleModifier)

    val processingSpeedMultiplier: Double
        get() = processingSpeedMultiplierConfig.get()

    val maxAttributes: Int
        get() = maxAttributesConfig.get()

    val maxEffects: Int
        get() = maxEffectsConfig.get()

    val blasterCooldown: Int
        get() = blasterCooldownConfig.get()

    fun getTieredItem(item: Item): ItemStack {
        return ItemStack(item).apply {
            this.set(DataComponents.RARITY, this@NanoTier.rarity)
        }
    }

    fun getSynthesizeNano(): ItemStack {
        return getTieredItem(NanoMiraiItems.SYNTHESIZE_NANO)
    }

    fun getSupportNano(): ItemStack {
        return getTieredItem(NanoMiraiItems.SUPPORT_NANO)
    }

    fun getNanoSwarmBlaster(): ItemStack {
        return getTieredItem(NanoMiraiItems.NANO_SWARM_BLASTER)
    }

    companion object {
        fun synthesizeNanoIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(NanoTier::getSynthesizeNano).toTypedArray())
        }

        fun supportNanoIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(NanoTier::getSupportNano).toTypedArray())
        }

        fun nanoSwarmBlasterIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(NanoTier::getNanoSwarmBlaster).toTypedArray())
        }

        fun fromRarity(rarity: Rarity): NanoTier {
            return NanoTier.entries.first { it.rarity == rarity }
        }

        fun fromMinLevel(level: Int): List<NanoTier> {
            return NanoTier.entries.filter { it.rarity.ordinal >= level }
        }
    }
}
