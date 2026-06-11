package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai.rl
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object NanoMiraiBlockTags {
    val REINFORCED_OBSIDIAN: TagKey<Block> = TagKey.create(Registries.BLOCK, rlCommon("obsidians/reinforced"))
    val STORAGE_BLOCKS_SCULMIUM: TagKey<Block> = TagKey.create(Registries.BLOCK, rlCommon("storage_blocks/sculmium"))
    val STORAGE_BLOCKS_RAW_SCULMIUM: TagKey<Block> = TagKey.create(Registries.BLOCK, rlCommon("storage_blocks/raw_sculmium"))
    val SYNTHESIZE_DISPLAY_AS_ITEM: TagKey<Block> = TagKey.create(Registries.BLOCK, rl("synthesize_recipe_as_item"))

    private fun rlCommon(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("c", path)
}