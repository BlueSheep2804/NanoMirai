package dev.bluesheep.nanomirai.compat.jade

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.entity.MobCageBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.MobCageUtil
import net.minecraft.ChatFormatting
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.IElement
import snownee.jade.api.ui.IElementHelper

object MobCageComponentProvider : IBlockComponentProvider {
    override fun getIcon(accessor: BlockAccessor?, config: IPluginConfig?, currentIcon: IElement?): IElement? {
        accessor ?: return currentIcon
        val blockEntity = accessor.blockEntity as? MobCageBlockEntity ?: return currentIcon

        val mobCage = ItemStack(NanoMiraiItems.MOB_CAGE)
        mobCage.applyComponents(blockEntity.components())
        return IElementHelper.get().item(mobCage)
    }

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor?,
        config: IPluginConfig?
    ) {
        accessor ?: return
        val blockEntity = accessor.blockEntity as? MobCageBlockEntity ?: return

        blockEntity.capturedEntity?.let { entity ->
            tooltip.add(MobCageUtil.getEntityTooltip(entity).withStyle(ChatFormatting.WHITE))
        }
    }

    override fun getUid(): ResourceLocation {
        return NanoMirai.rl("mob_cage")
    }
}