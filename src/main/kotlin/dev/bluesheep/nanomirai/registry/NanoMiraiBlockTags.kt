package dev.bluesheep.nanomirai.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object NanoMiraiBlockTags {
    val REINFORCED_OBSIDIAN: TagKey<Block> = TagKey.create(Registries.BLOCK, rlCommon("obsidians/reinforced"))

    private fun rlCommon(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("c", path)
}