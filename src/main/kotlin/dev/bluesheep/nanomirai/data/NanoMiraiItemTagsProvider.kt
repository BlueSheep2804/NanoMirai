package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class NanoMiraiItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, blockTags: CompletableFuture<TagLookup<Block>>, existingFileHelper: ExistingFileHelper) : ItemTagsProvider(
    output,
    lookupProvider,
    blockTags,
    NanoMirai.ID,
    existingFileHelper
) {
    override fun addTags(lookupProvider: HolderLookup.Provider) {
        tag(NanoMiraiTags.CURIOS_SUPPORT_NANO)
            .add(NanoMiraiItems.SUPPORT_NANO)

        tag(NanoMiraiTags.LENS)
            .add(NanoMiraiItems.AMETHYST_LENS)
            .add(NanoMiraiItems.SCULK_LENS)
    }
}
