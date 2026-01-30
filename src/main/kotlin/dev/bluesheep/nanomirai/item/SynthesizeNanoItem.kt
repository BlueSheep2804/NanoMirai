package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.SynthesizeUtil
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.DispenserBlock

class SynthesizeNanoItem : PoweredItem(
    Properties(),
    1000,
    100
), INanoTieredItem {
    override fun getName(stack: ItemStack): Component {
        return getTieredName(stack, super.getName(stack))
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) return InteractionResult.PASS
        val player = context.player ?: return InteractionResult.PASS
        val inputBlock = level.getBlockState(context.clickedPos)
        val mainhand = player.getItemInHand(InteractionHand.MAIN_HAND)
        val offhand = player.getItemInHand(InteractionHand.OFF_HAND)
        val primaryItem = (if (context.hand == InteractionHand.MAIN_HAND) mainhand else offhand)
        val secondaryItem = (if (context.hand == InteractionHand.MAIN_HAND) offhand else mainhand)

        if (!isEnergyEnough(primaryItem)) {
            return InteractionResult.FAIL
        }

        if (!SynthesizeUtil.check(level, primaryItem, secondaryItem, context.clickedPos)) {
            player.displayClientMessage(Component.translatable("recipe.nanomirai.synthesize.not_found"), true)
            return InteractionResult.FAIL
        }

        SynthesizeUtil.convertToSynthesizeDisplay(
            level,
            primaryItem,
            secondaryItem,
            inputBlock,
            context.clickedPos
        )
        consumeEnergy(primaryItem)
        player.displayClientMessage(Component.translatable("recipe.nanomirai.synthesize.start"), true)

        return InteractionResult.SUCCESS_NO_ITEM_USED
    }

    class DispenserBehavior : OptionalDispenseItemBehavior() {
        override fun execute(
            blockSource: BlockSource,
            itemStack: ItemStack
        ): ItemStack {
            isSuccess = false

            val level = blockSource.level
            val dispenser = blockSource.state
            val inputBlockPos = blockSource.pos.relative(dispenser.getValue(DispenserBlock.FACING))
            val inputBlock = level.getBlockState(inputBlockPos)

            if (!NanoMiraiItems.SYNTHESIZE_NANO.isEnergyEnough(itemStack)) {
                return itemStack
            }

            if (!SynthesizeUtil.check(level, itemStack, inputBlockPos)) {
                return itemStack
            }

            SynthesizeUtil.convertToSynthesizeDisplay(
                level,
                itemStack,
                inputBlock,
                inputBlockPos
            ) ?: return super.execute(blockSource, itemStack)
            NanoMiraiItems.SYNTHESIZE_NANO.consumeEnergy(itemStack)
            isSuccess = true
            return itemStack
        }
    }
}
