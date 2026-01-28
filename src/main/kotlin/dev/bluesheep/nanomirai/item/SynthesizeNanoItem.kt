package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.util.SynthesizeUtil
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.DispenserBlock

class SynthesizeNanoItem : Item(
    Properties()
        .durability(8)
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
        primaryItem.hurtAndBreak(
            1,
            player,
            if (context.hand == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND
        )
        player.displayClientMessage(Component.translatable("recipe.nanomirai.synthesize.start"), true)

        return InteractionResult.SUCCESS_NO_ITEM_USED
    }

    override fun overrideOtherStackedOnMe(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        action: ClickAction,
        player: Player,
        access: SlotAccess
    ): Boolean {
        return otherStackedOnMe(stack, other, action)
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

            if (!SynthesizeUtil.check(level, itemStack, inputBlockPos)) {
                return itemStack
            }

            SynthesizeUtil.convertToSynthesizeDisplay(
                level,
                itemStack,
                inputBlock,
                inputBlockPos
            ) ?: return super.execute(blockSource, itemStack)
            itemStack.hurtAndBreak(
                1,
                level,
                null
            ) {}
            isSuccess = true
            return itemStack
        }
    }
}
