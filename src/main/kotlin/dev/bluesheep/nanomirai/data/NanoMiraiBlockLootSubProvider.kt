package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block

class NanoMiraiBlockLootSubProvider(lookupProvider: HolderLookup.Provider) : BlockLootSubProvider(
    emptySet(),
    FeatureFlags.DEFAULT_FLAGS,
    lookupProvider
) {
    override fun getKnownBlocks(): Iterable<Block> {
        return NanoMiraiBlocks.REGISTRY.entries.map {
            it.value() as Block
        }.toList()
    }

    override fun generate() {
        dropSelf(NanoMiraiBlocks.NANOMACHINE_ASSEMBLER)
        dropSelf(NanoMiraiBlocks.LASER_ENGRAVER)
        dropSelf(NanoMiraiBlocks.NANO_LAB)
    }
}