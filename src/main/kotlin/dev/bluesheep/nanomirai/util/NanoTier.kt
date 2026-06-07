package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.NanoMiraiConfig
import dev.bluesheep.nanomirai.item.INanoTieredItem
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.ModConfigSpec
import net.neoforged.neoforge.common.util.Lazy

enum class NanoTier(
    val rarity: Rarity,
    private val synthesizeNanoItemLazy: Lazy<SynthesizeNanoItem>,
    private val supportNanoItemLazy: Lazy<SupportNanoItem>,
    private val nanoSwarmBlasterItemLazy: Lazy<NanoSwarmBlasterItem>,
    private val processingSpeedMultiplierConfig: ModConfigSpec.ConfigValue<Double>,
    private val maxAttributesConfig: ModConfigSpec.ConfigValue<Int>,
    private val maxEffectsConfig: ModConfigSpec.ConfigValue<Int>,
    private val blasterCooldownConfig: ModConfigSpec.ConfigValue<Int>
) {
    NORMAL(
        Rarity.COMMON,
        Lazy.of { -> NanoMiraiItems.SYNTHESIZE_NANO_NORMAL },
        Lazy.of { -> NanoMiraiItems.SUPPORT_NANO_NORMAL },
        Lazy.of { -> NanoMiraiItems.NANO_SWARM_BLASTER_NORMAL },
        NanoMiraiConfig.processingSpeedMultiplierNormal,
        NanoMiraiConfig.maxAttributesNormal,
        NanoMiraiConfig.maxEffectsNormal,
        NanoMiraiConfig.blasterCooldownNormal
    ),
    IMPROVED(
        Rarity.RARE,
        Lazy.of { -> NanoMiraiItems.SYNTHESIZE_NANO_IMPROVED },
        Lazy.of { -> NanoMiraiItems.SUPPORT_NANO_IMPROVED },
        Lazy.of { -> NanoMiraiItems.NANO_SWARM_BLASTER_IMPROVED },
        NanoMiraiConfig.processingSpeedMultiplierImproved,
        NanoMiraiConfig.maxAttributesImproved,
        NanoMiraiConfig.maxEffectsImproved,
        NanoMiraiConfig.blasterCooldownImproved
    );

    val nameComponent: Component
        get() = Component.translatable("nanomirai.nano_tier.${name.lowercase()}").withStyle(rarity.styleModifier)

    val synthesizeNanoItem: SynthesizeNanoItem
        get() = synthesizeNanoItemLazy.get()

    val supportNanoItem: SupportNanoItem
        get() = supportNanoItemLazy.get()

    val nanoSwarmBlasterItem: NanoSwarmBlasterItem
        get() = nanoSwarmBlasterItemLazy.get()

    val processingSpeedMultiplier: Double
        get() = processingSpeedMultiplierConfig.get()

    val maxAttributes: Int
        get() = maxAttributesConfig.get()

    val maxEffects: Int
        get() = maxEffectsConfig.get()

    val blasterCooldown: Int
        get() = blasterCooldownConfig.get()

    val synthesizeNano: ItemStack
        get() = ItemStack(synthesizeNanoItem)

    val supportNano: ItemStack
        get() = ItemStack(supportNanoItem)

    val nanoSwarmBlaster: ItemStack
        get() = ItemStack(nanoSwarmBlasterItem)

    companion object {
        fun synthesizeNanoIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(NanoTier::synthesizeNano).toTypedArray())
        }

        fun supportNanoIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(NanoTier::supportNano).toTypedArray())
        }

        fun nanoSwarmBlasterIngredient(level: Int): Ingredient {
            return Ingredient.of(*fromMinLevel(level).map(NanoTier::nanoSwarmBlaster).toTypedArray())
        }

        fun fromItem(item: Item): NanoTier? {
            if (item is INanoTieredItem) {
                return item.tier
            }
            return null
        }

        fun fromMinLevel(level: Int): List<NanoTier> {
            return NanoTier.entries.filter { it.ordinal >= level }
        }
    }
}
