package dev.bluesheep.nanomirai.client.renderer

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

class MobCageItemExtensions : IClientItemExtensions {
    private val renderer = MobCageItemRenderer()

    override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
        return renderer
    }
}