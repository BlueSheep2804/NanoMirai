package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.recipe.BlockWithPairItemInput
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object SynthesizeUtil {
    fun check(level: Level, itemStack: ItemStack, inputBlock: BlockState): Boolean {
        val hasRecipe = level.recipeManager.getAllRecipesFor(NanoMiraiRecipeType.SYNTHESIZE).any {
            inputBlock.`is`(it.value.inputBlock.block) && itemStack.rarity.ordinal >= it.value.tier
        }
        return hasRecipe
    }

    fun check(level: Level, primaryItem: ItemStack, secondaryItem: ItemStack, inputBlock: BlockState): Boolean {
        val recipe = level.recipeManager.getRecipesFor(
            NanoMiraiRecipeType.SYNTHESIZE,
            BlockWithPairItemInput(inputBlock, primaryItem, secondaryItem),
            level
        )
        return !recipe.isEmpty()
    }

    fun convertToSynthesizeDisplay(level: Level, itemStack: ItemStack, inputBlock: BlockState, inputBlockPos: BlockPos): SynthesizeDisplayBlockEntity? {
        level.setBlockAndUpdate(inputBlockPos, NanoMiraiBlocks.SYNTHESIZE_DISPLAY.defaultBlockState())
        val blockEntity = level.getBlockEntity(inputBlockPos)
        if (blockEntity is SynthesizeDisplayBlockEntity) {
            blockEntity.block = inputBlock
            blockEntity.setPrimaryItem(itemStack.copy())
            return blockEntity
        }
        return null
    }

    fun convertToSynthesizeDisplay(level: Level, primaryItem: ItemStack, secondaryItem: ItemStack, inputBlock: BlockState, inputBlockPos: BlockPos): SynthesizeDisplayBlockEntity? {
        val blockEntity = convertToSynthesizeDisplay(
            level,
            primaryItem,
            inputBlock,
            inputBlockPos
        ) ?: return null
        blockEntity.setSecondaryItem(secondaryItem.split(1))
        return blockEntity
    }
}