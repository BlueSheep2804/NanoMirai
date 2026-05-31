package dev.bluesheep.nanomirai.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Equipable
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class AllayHeadBlock(properties: Properties) : AbstractAllayHeadBlock(properties), Equipable {
    companion object {
        val CODEC: MapCodec<AllayHeadBlock> = simpleCodec(::AllayHeadBlock)
        val BOXES = mapOf(
            Direction.NORTH to headShape(5.0, 0.0, 5.0),
            Direction.WEST to headShape(5.0, 0.0, 6.0),
            Direction.EAST to headShape(6.0, 0.0, 5.0),
            Direction.SOUTH to headShape(6.0, 0.0, 6.0)
        )
    }

    override fun codec(): MapCodec<AllayHeadBlock> {
        return CODEC
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return BOXES.getOrElse(state.getValue(FACING)) { BOXES.values.first() }
    }

    override fun getEquipmentSlot(): EquipmentSlot {
        return EquipmentSlot.HEAD
    }
}