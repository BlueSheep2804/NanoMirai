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
    PROTO(Rarity.COMMON, NanoMiraiItems.NANO_PROTO, 1, 1, 160, 1),
    CELL(Rarity.UNCOMMON, NanoMiraiItems.NANO_CELL, 2, 2, 120, 2),
    MATRIX(Rarity.RARE, NanoMiraiItems.NANO_MATRIX, 3, 3, 80, 3),
    SINGULARITY(Rarity.EPIC, NanoMiraiItems.NANO_SINGULARITY, 4, 4, 40, 4);

    companion object {
        fun getTieredItem(item: Item, tier: NanoTier): ItemStack {
            return ItemStack(item).apply {
                this.set(DataComponents.RARITY, tier.rarity)
            }
        }

        fun getTieredSynthesizeNano(tier: NanoTier): ItemStack {
            return getTieredItem(NanoMiraiItems.SYNTHESIZE_NANO, tier)
        }

        fun synthesizeNanoIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(::getTieredSynthesizeNano).toTypedArray())
        }

        fun getTieredSupportNano(tier: NanoTier): ItemStack {
            return getTieredItem(NanoMiraiItems.SUPPORT_NANO, tier)
        }

        fun supportNanoIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(::getTieredSupportNano).toTypedArray())
        }

        fun getTieredNanoSwarmBlaster(tier: NanoTier): ItemStack {
            return getTieredItem(NanoMiraiItems.NANO_SWARM_BLASTER, tier)
        }

        fun nanoSwarmBlasterIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(::getTieredNanoSwarmBlaster).toTypedArray())
        }

        fun fromRarity(rarity: Rarity): NanoTier {
            return NanoTier.entries.first { it.rarity == rarity }
        }

        fun fromMinLevel(level: Int): List<NanoTier> {
            return NanoTier.entries.filter { it.rarity.ordinal >= level }
        }

        fun getTierNameComponent(tier: NanoTier): Component {
            return Component.translatable("nanomirai.nano_tier.${tier.name.lowercase()}").withStyle(tier.rarity.styleModifier)
        }
    }
}
