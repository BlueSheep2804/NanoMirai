package dev.bluesheep.nanomirai.block

import com.mojang.serialization.MapCodec
import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockStates
import dev.bluesheep.nanomirai.util.SynthesizeState
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
import net.minecraft.world.level.block.state.properties.EnumProperty

class SynthesizeDisplayBlock(properties: Properties) : BaseEntityBlock(properties) {
    companion object {
        val CODEC: MapCodec<SynthesizeDisplayBlock> = simpleCodec(::SynthesizeDisplayBlock)
        val STATE: EnumProperty<SynthesizeState> = NanoMiraiBlockStates.SYNTHESIZE_STATE
    }

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(STATE, SynthesizeState.IDLE)
        )
    }

    override fun codec(): MapCodec<out BaseEntityBlock> {
        return CODEC
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(STATE)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return SynthesizeDisplayBlockEntity(pos, state)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        if (state.block != newState.block) {
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity is SynthesizeDisplayBlockEntity) {
                if (!blockEntity.hasCraftingFinished()) {
                    blockEntity.drops()
                    val blockState = blockEntity.block.blockState
                    level.setBlockAndUpdate(pos, blockState)
                    val newBlockEntity = level.getBlockEntity(pos)
                    newBlockEntity?.loadWithComponents(blockEntity.block.nbt, level.registryAccess())
                    newBlockEntity?.setChanged()
                    level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_ALL)
                }
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston)
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
