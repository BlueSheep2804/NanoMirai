package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
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
        tag(TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("curios", "support_nano")
        ))
            .add(NanoMiraiItems.SUPPORT_NANO)
    }
}
