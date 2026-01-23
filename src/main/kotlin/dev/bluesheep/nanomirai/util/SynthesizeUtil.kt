package dev.bluesheep.nanomirai.util

import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.recipe.BlockStateWithNbt
import dev.bluesheep.nanomirai.recipe.BlockWithPairItemInput
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object SynthesizeUtil {
    fun createEntityData(entityType: EntityType<*>): CompoundTag {
        return CompoundTag().apply {
            put("components", CompoundTag().apply {
                put(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(DataComponents.ENTITY_DATA).toString(), CompoundTag().apply {
                    putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString())
                })
            })
        }
    }

    fun check(level: Level, itemStack: ItemStack, inputBlockPos: BlockPos): Boolean {
        val inputBlock = level.getBlockState(inputBlockPos)
        val nbt = level.getBlockEntity(inputBlockPos)?.saveWithFullMetadata(level.registryAccess()) ?: CompoundTag()
        val hasRecipe = level.recipeManager.getAllRecipesFor(NanoMiraiRecipeType.SYNTHESIZE).any {
            it.value.checkWithoutCatalyst(BlockStateWithNbt(inputBlock, nbt), itemStack)
        }
        return hasRecipe
    }

    fun check(level: Level, primaryItem: ItemStack, secondaryItem: ItemStack, inputBlockPos: BlockPos): Boolean {
        val inputBlock = level.getBlockState(inputBlockPos)
        val nbt = level.getBlockEntity(inputBlockPos)?.saveWithFullMetadata(level.registryAccess()) ?: CompoundTag()
        val recipe = level.recipeManager.getRecipesFor(
            NanoMiraiRecipeType.SYNTHESIZE,
            BlockWithPairItemInput(BlockStateWithNbt(inputBlock, nbt), primaryItem, secondaryItem),
            level
        )
        return !recipe.isEmpty()
    }

    fun convertToSynthesizeDisplay(level: Level, itemStack: ItemStack, inputBlock: BlockState, inputBlockPos: BlockPos): SynthesizeDisplayBlockEntity? {
        val nbt = level.getBlockEntity(inputBlockPos)?.saveWithFullMetadata(level.registryAccess()) ?: CompoundTag()
        level.setBlockAndUpdate(inputBlockPos, NanoMiraiBlocks.SYNTHESIZE_DISPLAY.defaultBlockState())
        val blockEntity = level.getBlockEntity(inputBlockPos)
        if (blockEntity is SynthesizeDisplayBlockEntity) {
//            blockEntity.block = inputBlock
            blockEntity.setInputBlock(inputBlock, nbt)
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