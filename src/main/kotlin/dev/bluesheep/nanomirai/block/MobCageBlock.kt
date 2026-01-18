package dev.bluesheep.nanomirai.block

import com.mojang.serialization.MapCodec
import dev.bluesheep.nanomirai.block.entity.MobCageBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class MobCageBlock(properties: Properties) : BaseEntityBlock(properties) {
    companion object {
        val CODEC: MapCodec<MobCageBlock> = simpleCodec(::MobCageBlock)
        val SHAPE: VoxelShape = box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0)
        val HORIZONTAL_FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
    }

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
        )
    }

    override fun codec(): MapCodec<out BaseEntityBlock> {
        return CODEC
    }

    override fun newBlockEntity(
        pos: BlockPos,
        state: BlockState
    ): BlockEntity {
        return MobCageBlockEntity(pos, state)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HORIZONTAL_FACING)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(HORIZONTAL_FACING, context.horizontalDirection.opposite)
    }

    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        if (entity !is LivingEntity) return
        if (entity is Player) return
        level.getBlockEntity(pos)?.let { blockEntity ->
            if (blockEntity is MobCageBlockEntity) {
                blockEntity.entityInside(entity)
            }
        }
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }
}