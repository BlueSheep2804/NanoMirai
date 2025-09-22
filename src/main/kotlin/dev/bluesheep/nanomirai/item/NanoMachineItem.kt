package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.recipe.BlockWithPairItemInput
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class NanoMachineItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) return InteractionResult.PASS
        if (context.hand == InteractionHand.OFF_HAND) return InteractionResult.PASS
        val player = context.player ?: return InteractionResult.PASS
//        if (!player.isCrouching) return InteractionResult.PASS

        val inputBlock = context.level.getBlockState(context.clickedPos)
        val mainhand = player.getItemInHand(InteractionHand.MAIN_HAND)
        val offhand = player.getItemInHand(InteractionHand.OFF_HAND)
        val recipe = level.recipeManager.getRecipeFor(
            NanoMiraiRecipeType.SYNTHESIZE,
            BlockWithPairItemInput(
                inputBlock,
                mainhand,
                offhand
            ),
            level
        )
        if (recipe.isEmpty) {
            player.sendSystemMessage(Component.translatable("message.nanomirai.no_synthesize_recipe", inputBlock.block.name))
            return InteractionResult.FAIL
        }
        level.removeBlock(context.clickedPos, false)
        if (!player.isCreative) {
            mainhand.shrink(1)
            offhand.shrink(1)
        }
        val container = SimpleContainer(recipe.get().value.result.copy())
        Containers.dropContents(level, context.clickedPos, container)
        return InteractionResult.SUCCESS_NO_ITEM_USED
    }
}
