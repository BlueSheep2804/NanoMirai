package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class NanoMiraiBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper) : BlockTagsProvider(output, lookupProvider, NanoMirai.ID, existingFileHelper) {
    override fun addTags(lookupProvider: HolderLookup.Provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
                NanoMiraiBlocks.NANOMACHINE_ASSEMBLER,
                NanoMiraiBlocks.LASER_ENGRAVER,
                NanoMiraiBlocks.NANO_LAB
            )
    }
}
