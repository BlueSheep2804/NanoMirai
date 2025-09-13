package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.item.NanoSwarmItem
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.world.item.Item

enum class NanoTier(val tierLevel: Int, val singleItem: Item, val swarmItem: NanoSwarmItem) {
    SEED(0, NanoMiraiItems.NANO_SEED, NanoMiraiItems.NANO_SEED_SWARM),
    MATRIX(1, NanoMiraiItems.NANO_MATRIX, NanoMiraiItems.NANO_MATRIX_SWARM),
    SINGULARITY(2, NanoMiraiItems.NANO_SINGULARITY, NanoMiraiItems.NANO_SINGULARITY_SWARM);
}
