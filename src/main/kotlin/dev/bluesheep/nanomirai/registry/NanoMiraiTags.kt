package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai.rl
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object NanoMiraiTags {
    val CURIOS_SUPPORT_NANO: TagKey<Item> = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "support_nano"))
    val LENS: TagKey<Item> = TagKey.create(Registries.ITEM, rl("lens"))
    val FUNCTIONAL_NANOMACHINES: TagKey<Item> = TagKey.create(Registries.ITEM, rl("functional_nanomachines"))
}
