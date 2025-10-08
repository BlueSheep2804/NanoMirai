package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.recipe.BlockWithPairItemInput
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.DispenserBlock

class SynthesizeNanoItem(properties: Properties) : Item(properties), INanoTieredItem {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
        appendTierTooltip(stack, context, tooltipComponents, tooltipFlag)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) return InteractionResult.PASS
        val player = context.player ?: return InteractionResult.PASS
        val inputBlock = context.level.getBlockState(context.clickedPos)
        val mainhand = player.getItemInHand(InteractionHand.MAIN_HAND)
        val offhand = player.getItemInHand(InteractionHand.OFF_HAND)
        val primaryItem = (if (context.hand == InteractionHand.MAIN_HAND) mainhand else offhand)
        val secondaryItem = (if (context.hand == InteractionHand.MAIN_HAND) offhand else mainhand)

        val recipe = level.recipeManager.getRecipesFor(
            NanoMiraiRecipeType.SYNTHESIZE,
            BlockWithPairItemInput(inputBlock, primaryItem, secondaryItem),
            level
        )
        if (recipe.isEmpty()) {
            player.displayClientMessage(Component.translatable("recipe.nanomirai.synthesize.not_found"), true)
            return InteractionResult.FAIL
        }

        level.setBlock(context.clickedPos, NanoMiraiBlocks.SYNTHESIZE_DISPLAY.defaultBlockState(), 3)
        val blockEntity = level.getBlockEntity(context.clickedPos)
        if (blockEntity is SynthesizeDisplayBlockEntity) {
            blockEntity.block = inputBlock
            blockEntity.setPrimaryItem(primaryItem.split(1))
            if (!secondaryItem.isEmpty) blockEntity.setSecondaryItem(secondaryItem.split(1))
        }

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
            val inputBlock = blockSource.level.getBlockState(inputBlockPos)

            val hasInputBlockRecipe = level.recipeManager.getAllRecipesFor(NanoMiraiRecipeType.SYNTHESIZE).any {
                inputBlock.`is`(it.value.inputBlock.block)
            }
            if (!hasInputBlockRecipe) {
                return itemStack
            }

            level.setBlockAndUpdate(inputBlockPos, NanoMiraiBlocks.SYNTHESIZE_DISPLAY.defaultBlockState())
            val blockEntity = level.getBlockEntity(inputBlockPos)
            if (blockEntity is SynthesizeDisplayBlockEntity) {
                blockEntity.block = inputBlock
                blockEntity.setPrimaryItem(itemStack.split(1))
                isSuccess = true
                return itemStack
            }
            return super.execute(blockSource, itemStack)
        }
    }
}
