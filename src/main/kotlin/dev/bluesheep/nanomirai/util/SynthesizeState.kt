package dev.bluesheep.nanomirai.util

import net.minecraft.network.chat.Component
import net.minecraft.util.StringRepresentable
import net.neoforged.neoforge.common.TranslatableEnum

enum class SynthesizeState: StringRepresentable, TranslatableEnum {
    IDLE,
    CRAFTING,
    INVALID;

    override fun getSerializedName(): String {
        return this.name.lowercase()
    }

    override fun getTranslatedName(): Component {
        return Component.translatable("block.nanomirai.synthesize_display.${serializedName}")
    }
}
