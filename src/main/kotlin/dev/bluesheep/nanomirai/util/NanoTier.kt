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
    val item: Item,
    val maxAttributes: Int,
    val maxEffects: Int,
    val blasterCooldown: Int,
    val repairAmount: Int
) {
    PROTO(Rarity.COMMON, NanoMiraiItems.NANO_PROTO, 1, 1, 120, 1),
    CELL(Rarity.UNCOMMON, NanoMiraiItems.NANO_CELL, 2, 2, 80, 2),
    MATRIX(Rarity.RARE, NanoMiraiItems.NANO_MATRIX, 3, 3, 40, 3),
    SINGULARITY(Rarity.EPIC, NanoMiraiItems.NANO_SINGULARITY, 4, 4, 20, 4);

    val nameComponent: Component
        get() = Component.translatable("nanomirai.nano_tier.${name.lowercase()}").withStyle(rarity.styleModifier)

    fun getTieredItem(item: Item): ItemStack {
        return ItemStack(item).apply {
            this.set(DataComponents.RARITY, rarity)
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
