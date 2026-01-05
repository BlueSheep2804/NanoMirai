package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockTags
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class NanoMiraiBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper) : BlockTagsProvider(output, lookupProvider, NanoMirai.ID, existingFileHelper) {
    override fun addTags(lookupProvider: HolderLookup.Provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
                NanoMiraiBlocks.NANOMACHINE_ASSEMBLER,
                NanoMiraiBlocks.LASER_ENGRAVER,
                NanoMiraiBlocks.NANO_LAB,
                NanoMiraiBlocks.REINFORCED_OBSIDIAN
            )

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .add(NanoMiraiBlocks.REINFORCED_OBSIDIAN)

        tag(BlockTags.WITHER_IMMUNE)
            .add(NanoMiraiBlocks.REINFORCED_OBSIDIAN)

        tag(BlockTags.DRAGON_IMMUNE)
            .add(NanoMiraiBlocks.REINFORCED_OBSIDIAN)

        tag(Tags.Blocks.OBSIDIANS)
            .addTag(NanoMiraiBlockTags.REINFORCED_OBSIDIAN)

        tag(NanoMiraiBlockTags.REINFORCED_OBSIDIAN)
            .add(NanoMiraiBlocks.REINFORCED_OBSIDIAN)
    }
}
