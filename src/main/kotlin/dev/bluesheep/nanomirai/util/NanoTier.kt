package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.crafting.Ingredient

enum class NanoTier(val rarity: Rarity, val item: Item) {
    PROTO(Rarity.COMMON, NanoMiraiItems.NANO_PROTO),
    CELL(Rarity.UNCOMMON, NanoMiraiItems.NANO_CELL),
    MATRIX(Rarity.RARE, NanoMiraiItems.NANO_MATRIX),
    SINGULARITY(Rarity.EPIC, NanoMiraiItems.NANO_SINGULARITY);

    companion object {
        fun getTieredSynthesizeNano(tier: NanoTier): ItemStack {
            return ItemStack(NanoMiraiItems.SYNTHESIZE_NANO).apply {
                this.set(DataComponents.RARITY, tier.rarity)
            }
        }

        fun fromRarity(rarity: Rarity): NanoTier {
            return NanoTier.entries.first { it.rarity == rarity }
        }

        fun fromMinLevel(level: Int): List<NanoTier> {
            return NanoTier.entries.filter { it.rarity.ordinal >= level }
        }

        fun ingredientFromMinLevel(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(::getTieredSynthesizeNano).toTypedArray())
        }
    }
}
