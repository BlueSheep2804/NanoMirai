package dev.bluesheep.nanomirai.block

import com.mojang.serialization.MapCodec
import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockStates
import dev.bluesheep.nanomirai.util.SynthesizeState
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
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
import net.minecraft.world.level.material.FluidState

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

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean,
        fluid: FluidState
    ): Boolean {
        val blockEntity = level.getBlockEntity(pos)
        if (blockEntity is SynthesizeDisplayBlockEntity && !blockEntity.hasCraftingFinished()) {
            val blockState = blockEntity.block
            level.setBlockAndUpdate(pos, blockState)
            if (!level.isClientSide) {
                blockEntity.drops()
                level.getBlockEntity(pos)?.let {
                    it.loadWithComponents(blockEntity.blockNbt, level.registryAccess())
                    it.setChanged()
                    level.sendBlockUpdated(pos, it.blockState, it.blockState, Block.UPDATE_ALL)
                }
            }
        }

        return true
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
