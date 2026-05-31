package dev.bluesheep.nanomirai.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class AllayWallHeadBlock(properties: Properties) : AbstractAllayHeadBlock(properties) {
    companion object {
        val CODEC: MapCodec<AllayWallHeadBlock> = simpleCodec(::AllayWallHeadBlock)
        val BOXES = mapOf(
            Direction.NORTH to headShape(5.0, 5.0, 11.0),
            Direction.SOUTH to headShape(6.0, 5.0, 0.0),
            Direction.EAST to headShape(0.0, 5.0, 5.0),
            Direction.WEST to headShape(11.0, 5.0, 6.0),
        )
    }

    override fun codec(): MapCodec<AllayWallHeadBlock> {
        return CODEC
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        val lookingDirection = context.nearestLookingDirections
        val level = context.level
        val clickedPos = context.clickedPos

        lookingDirection.forEach { direction ->
            if (direction.axis.isHorizontal) {
                val blockState = defaultBlockState().setValue(FACING, direction.opposite)
                val wallBlock = level.getBlockState(clickedPos.relative(direction))
                if (!wallBlock.canBeReplaced(context)) {
                    return blockState
                }
            }
        }

        return super.getStateForPlacement(context)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return BOXES.getOrElse(state.getValue(FACING)) { BOXES.values.first() }
    }
}
