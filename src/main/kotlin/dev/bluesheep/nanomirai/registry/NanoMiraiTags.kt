package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai.rl
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object NanoMiraiTags {
    val CURIOS_SUPPORT_NANO: TagKey<Item> = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "support_nano"))
    val LENSES: TagKey<Item> = TagKey.create(Registries.ITEM, rl("lenses"))
    val RESEARCH_CATALYSTS: TagKey<Item> = TagKey.create(Registries.ITEM, rl("research_catalysts"))
    val REPAIR_NANO_INGREDIENTS: TagKey<Item> = TagKey.create(Registries.ITEM, rl("repair_nano_ingredients"))
    val FUNCTIONAL_NANOMACHINES: TagKey<Item> = TagKey.create(Registries.ITEM, rl("functional_nanomachines"))

    val SHERD_COLD_OCEAN_RUINS: TagKey<Item> = TagKey.create(Registries.ITEM, rl("sherd_cold_ocean_ruins"))
    val SHERD_WARM_OCEAN_RUINS: TagKey<Item> = TagKey.create(Registries.ITEM, rl("sherd_warm_ocean_ruins"))
    val SHERD_DESERT_PYRAMID: TagKey<Item> = TagKey.create(Registries.ITEM, rl("sherd_desert_pyramid"))
    val SHERD_DESERT_WELL: TagKey<Item> = TagKey.create(Registries.ITEM, rl("sherd_desert_well"))
    val SHERD_TRAIL_RUINS: TagKey<Item> = TagKey.create(Registries.ITEM, rl("sherd_trail_ruins"))
    val SHERD_TRIAL_CHAMBER: TagKey<Item> = TagKey.create(Registries.ITEM, rl("sherd_trial_chamber"))
}
