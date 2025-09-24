package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class NanoMachineItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) return InteractionResult.PASS
        val player = context.player ?: return InteractionResult.PASS
        val inputBlock = context.level.getBlockState(context.clickedPos)
        val mainhand = player.getItemInHand(InteractionHand.MAIN_HAND)
        val offhand = player.getItemInHand(InteractionHand.OFF_HAND)
        if (inputBlock.`is`(NanoMiraiBlocks.SYNTHESIZE_DISPLAY)) return InteractionResult.FAIL

        level.setBlock(context.clickedPos, NanoMiraiBlocks.SYNTHESIZE_DISPLAY.defaultBlockState(), 3)
        val blockEntity = level.getBlockEntity(context.clickedPos)
        if (blockEntity is SynthesizeDisplayBlockEntity) {
            blockEntity.block = inputBlock
            val primaryItem = (if (context.hand == InteractionHand.MAIN_HAND) mainhand else offhand).copy().apply { this.count = 1 }
            val secondaryItem = (if (context.hand == InteractionHand.MAIN_HAND) offhand else mainhand).copy().apply { this.count = 1 }
            blockEntity.setPrimaryItem(primaryItem)
            blockEntity.setSecondaryItem(secondaryItem)
            if (!player.isCreative) {
                mainhand.shrink(1)
                offhand.shrink(1)
            }
        }

        return InteractionResult.SUCCESS_NO_ITEM_USED
    }
}
