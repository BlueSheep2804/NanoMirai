package dev.bluesheep.nanomirai.block

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.shapes.VoxelShape

abstract class AbstractAllayHeadBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {
    companion object {
        fun headShape(x: Double, y: Double, z: Double): VoxelShape {
            return box(x, y, z, x + 5.0, y + 5.0, z + 5.0)
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, context.horizontalDirection.opposite)
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        if (level.isClientSide) return
        val below = pos.below()
        val belowBlockState = level.getBlockState(below)
        if (belowBlockState.`is`(Blocks.LIGHT_BLUE_WOOL)) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
            level.setBlockAndUpdate(below, Blocks.AIR.defaultBlockState())

            val allay = EntityType.ALLAY.create(level)
            if (allay != null) {
                allay.moveTo(pos, 0f, 0f)
                level.addFreshEntity(allay)

                level.getEntitiesOfClass(
                    ServerPlayer::class.java,
                    allay.boundingBox.inflate(50.0)
                ).forEach { player ->
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(player, allay)
                }
            }
        }
    }
}