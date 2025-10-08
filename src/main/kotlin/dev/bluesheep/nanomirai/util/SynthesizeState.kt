package dev.bluesheep.nanomirai.util

import net.minecraft.util.StringRepresentable

enum class SynthesizeState: StringRepresentable {
    IDLE,
    CRAFTING,
    INVALID;

    override fun getSerializedName(): String {
        return this.name.lowercase()
    }
}
