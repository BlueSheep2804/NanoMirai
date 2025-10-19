package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.world.item.Items
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

        tag(NanoMiraiTags.FUNCTIONAL_NANOMACHINES)
            .add(NanoMiraiItems.SYNTHESIZE_NANO)
            .add(NanoMiraiItems.SUPPORT_NANO)
            .add(NanoMiraiItems.NANO_SWARM_BLASTER)

        tag(NanoMiraiTags.SHERD_DESERT_WELL)
            .add(Items.ARMS_UP_POTTERY_SHERD)
            .add(Items.BREWER_POTTERY_SHERD)

        tag(NanoMiraiTags.SHERD_DESERT_PYRAMID)
            .add(Items.ARCHER_POTTERY_SHERD)
            .add(Items.MINER_POTTERY_SHERD)
            .add(Items.PRIZE_POTTERY_SHERD)
            .add(Items.SKULL_POTTERY_SHERD)

        tag(NanoMiraiTags.SHERD_TRAIL_RUINS)
            .add(Items.BURN_POTTERY_SHERD)
            .add(Items.DANGER_POTTERY_SHERD)
            .add(Items.FRIEND_POTTERY_SHERD)
            .add(Items.HEART_POTTERY_SHERD)
            .add(Items.HEARTBREAK_POTTERY_SHERD)
            .add(Items.HOWL_POTTERY_SHERD)
            .add(Items.SHEAF_POTTERY_SHERD)

        tag(NanoMiraiTags.SHERD_COLD_OCEAN_RUINS)
            .add(Items.BLADE_POTTERY_SHERD)
            .add(Items.EXPLORER_POTTERY_SHERD)
            .add(Items.MOURNER_POTTERY_SHERD)
            .add(Items.PLENTY_POTTERY_SHERD)

        tag(NanoMiraiTags.SHERD_WARM_OCEAN_RUINS)
            .add(Items.ANGLER_POTTERY_SHERD)
            .add(Items.SHELTER_POTTERY_SHERD)
            .add(Items.SNORT_POTTERY_SHERD)

        tag(NanoMiraiTags.SHERD_TRIAL_CHAMBER)
            .add(Items.SCRAPE_POTTERY_SHERD)
            .add(Items.FLOW_POTTERY_SHERD)
            .add(Items.GUSTER_POTTERY_SHERD)
    }
}
