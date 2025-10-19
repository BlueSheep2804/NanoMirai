package dev.bluesheep.nanomirai.util

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
    PROTO(Rarity.COMMON, 1.0, 1, 1, 120),
    CELL(Rarity.UNCOMMON, 1.2, 2, 2, 80),
    MATRIX(Rarity.RARE, 1.5, 3, 3, 40),
    SINGULARITY(Rarity.EPIC, 2.0, 4, 4, 20);

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
