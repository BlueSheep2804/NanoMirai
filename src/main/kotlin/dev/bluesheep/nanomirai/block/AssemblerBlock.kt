package dev.bluesheep.nanomirai.block

import com.mojang.serialization.MapCodec
import dev.bluesheep.nanomirai.block.entity.AssemblerBlockEntity
import dev.bluesheep.nanomirai.menu.AssemblerMenu
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class AssemblerBlock(properties: Properties) : BaseEntityBlock(properties) {
    companion object {
        val CODEC: MapCodec<AssemblerBlock> = simpleCodec(::AssemblerBlock)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return AssemblerBlockEntity(pos, state)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> {
        return CODEC
    }

    override fun getMenuProvider(state: BlockState, level: Level, pos: BlockPos): MenuProvider? {
        val blockEntity = level.getBlockEntity(pos)
        if (blockEntity !is AssemblerBlockEntity) return null
        return SimpleMenuProvider(
            { id, inv, _ -> AssemblerMenu(id, inv, ContainerLevelAccess.create(level, pos), blockEntity, blockEntity.data) },
            Component.translatable("container.nanomirai.nanomachine_assembler")
        )
    }

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        if (!level.isClientSide && player is ServerPlayer) {
            player.openMenu(
                state.getMenuProvider(level, pos)!!,
                pos
            )
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
        if (state.block != newState.block) {
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity is AssemblerBlockEntity) {
                blockEntity.drops()
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T?>): BlockEntityTicker<T?>? {
        if (level.isClientSide) return null

        return createTickerHelper(blockEntityType, NanoMiraiBlockEntities.NANOMACHINE_ASSEMBLER) {
            level, pos, state, blockEntity -> blockEntity.tick(level, pos, state)
        }
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }
}
