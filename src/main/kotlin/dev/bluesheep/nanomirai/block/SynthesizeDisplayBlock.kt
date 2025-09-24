package dev.bluesheep.nanomirai.block

import com.mojang.serialization.MapCodec
import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty

class SynthesizeDisplayBlock(properties: Properties) : BaseEntityBlock(properties) {
    companion object {
        val CODEC: MapCodec<SynthesizeDisplayBlock> = simpleCodec(::SynthesizeDisplayBlock)
        val CRAFTING: BooleanProperty = BlockStateProperties.CRAFTING
    }

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(CRAFTING, false)
        )
    }

    override fun codec(): MapCodec<out BaseEntityBlock> {
        return CODEC
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(CRAFTING)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return SynthesizeDisplayBlockEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if (level.isClientSide) return null

        return createTickerHelper(blockEntityType, NanoMiraiBlockEntities.SYNTHESIZE_DISPLAY) {
            level, pos, state, blockEntity -> blockEntity.tick(level, pos, state)
        }
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }
}
