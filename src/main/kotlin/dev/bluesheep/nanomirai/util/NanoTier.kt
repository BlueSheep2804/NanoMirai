package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.item.NanoMachineItem
import dev.bluesheep.nanomirai.registry.NanoMiraiItems

enum class NanoTier(val tierLevel: Int, val item: NanoMachineItem) {
    PROTO(0, NanoMiraiItems.NANO_PROTO),
    CELL(1, NanoMiraiItems.NANO_CELL),
    MATRIX(2, NanoMiraiItems.NANO_MATRIX),
    SINGULARITY(3, NanoMiraiItems.NANO_SINGULARITY);
}
