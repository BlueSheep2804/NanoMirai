package dev.bluesheep.nanomirai.compat.jade

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockStates
import dev.bluesheep.nanomirai.util.SynthesizeState
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.IElement
import snownee.jade.api.ui.IElementHelper

enum class SynthesizeDisplayComponentProvider : IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    override fun getIcon(accessor: BlockAccessor?, config: IPluginConfig?, currentIcon: IElement?): IElement? {
        val blockEntity = accessor?.blockEntity ?: return currentIcon
        if (blockEntity !is SynthesizeDisplayBlockEntity) return currentIcon
        val elementHelper = IElementHelper.get()
        return elementHelper.item(ItemStack(blockEntity.block.block))
    }

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor?, config: IPluginConfig?) {
        val blockEntity = accessor?.blockEntity ?: return
        if (blockEntity !is SynthesizeDisplayBlockEntity) return
        val elementHelper = IElementHelper.get()

        val progress = if (accessor.serverData.contains("progress")) {
            accessor.serverData.getInt("progress")
        } else {
            0
        }
        val maxProgress = if (accessor.serverData.contains("maxProgress")) {
            accessor.serverData.getInt("maxProgress")
        } else {
            100
        }
        val synthesizeState = accessor.blockState.getValue(NanoMiraiBlockStates.SYNTHESIZE_STATE)

        tooltip.add(synthesizeState.translatedName)
        val inputItem = blockEntity.itemHandler.getStackInSlot(1)
        if (!inputItem.isEmpty) {
            tooltip.add(elementHelper.item(inputItem))
            tooltip.append(elementHelper.progress(progress.toFloat() / maxProgress))
            if (synthesizeState == SynthesizeState.CRAFTING) {
                tooltip.append(elementHelper.item(blockEntity.getRecipeResult()))
            }
        }
        tooltip.remove(ResourceLocation.withDefaultNamespace("item_storage"))
    }

    override fun appendServerData(data: CompoundTag, accessor: BlockAccessor) {
        val blockEntity = accessor.blockEntity ?: return
        if (blockEntity !is SynthesizeDisplayBlockEntity) return
        data.putInt("progress", blockEntity.progress)
        data.putInt("maxProgress", blockEntity.maxProgress)
    }

    override fun getUid(): ResourceLocation {
        return NanoMirai.rl("synthesize_display")
    }
}