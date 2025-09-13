package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.item.NanoMachineItem
import dev.bluesheep.nanomirai.registry.NanoMiraiItems

enum class NanoTier(val tierLevel: Int, val item: NanoMachineItem) {
    SEED(0, NanoMiraiItems.NANO_SEED),
    MATRIX(1, NanoMiraiItems.NANO_MATRIX),
    SINGULARITY(2, NanoMiraiItems.NANO_SINGULARITY);
}
