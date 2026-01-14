package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.NanoMiraiConfig
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.crafting.Ingredient

enum class NanoTier(
    val rarity: Rarity,
    val processingSpeedMultiplier: Double,
    val maxAttributes: Int,
    val maxEffects: Int,
    val blasterCooldown: Int
) {
    MK1(
        Rarity.COMMON,
        NanoMiraiConfig.processingSpeedMultiplierMk1.get(),
        NanoMiraiConfig.maxAttributesMk1.get(),
        NanoMiraiConfig.maxEffectsMk1.get(),
        NanoMiraiConfig.blasterCooldownMk1.get()
    ),
    MK2(
        Rarity.UNCOMMON,
        NanoMiraiConfig.processingSpeedMultiplierMk2.get(),
        NanoMiraiConfig.maxAttributesMk2.get(),
        NanoMiraiConfig.maxEffectsMk2.get(),
        NanoMiraiConfig.blasterCooldownMk2.get()
    ),
    MK3(
        Rarity.RARE,
        NanoMiraiConfig.processingSpeedMultiplierMk3.get(),
        NanoMiraiConfig.maxAttributesMk3.get(),
        NanoMiraiConfig.maxEffectsMk3.get(),
        NanoMiraiConfig.blasterCooldownMk3.get()
    ),
    MK4(
        Rarity.EPIC,
        NanoMiraiConfig.processingSpeedMultiplierMk4.get(),
        NanoMiraiConfig.maxAttributesMk4.get(),
        NanoMiraiConfig.maxEffectsMk4.get(),
        NanoMiraiConfig.blasterCooldownMk4.get()
    );

    val nameComponent: Component
        get() = Component.translatable("nanomirai.nano_tier.${name.lowercase()}").withStyle(rarity.styleModifier)

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
