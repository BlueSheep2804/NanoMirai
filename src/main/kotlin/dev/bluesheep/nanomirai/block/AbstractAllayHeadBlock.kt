package dev.bluesheep.nanomirai.block

import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
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
}