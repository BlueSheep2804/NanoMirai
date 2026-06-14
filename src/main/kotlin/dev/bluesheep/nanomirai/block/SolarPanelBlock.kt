package dev.bluesheep.nanomirai.block

import com.mojang.serialization.MapCodec
import dev.bluesheep.nanomirai.block.entity.SolarPanelBlockEntity
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Mth
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class SolarPanelBlock(properties: Properties) : BaseEntityBlock(properties) {
    companion object {
        val CODEC: MapCodec<SolarPanelBlock> = simpleCodec(::SolarPanelBlock)
    }

    override fun codec(): MapCodec<SolarPanelBlock> {
        return CODEC
    }

    override fun newBlockEntity(
        pos: BlockPos,
        blockState: BlockState
    ): BlockEntity {
        return SolarPanelBlockEntity(pos, blockState)
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (!level.isClientSide && player is ServerPlayer) {
//            player.sendSystemMessage(Component.literal(level.canSeeSky(pos.above()).toString()))
//            player.sendSystemMessage(Component.literal((level.dayTime % 24000).toString()))
//            player.sendSystemMessage(Component.literal(level.dimensionType().fixedTime.orElse(-1).toString()))
//            player.sendSystemMessage(Component.literal(level.getMaxLocalRawBrightness(pos.above()).toString()))
            player.sendSystemMessage(
                Component.literal(
                    Mth.abs(
                        Mth.clampedLerp(
                            0f,
                            1f,
                            Mth.abs(6000 - (level.dayTime % 24000).toInt()) / 6000f
                        ) - 1f
                    ).toString()
                )
            )
            val be = level.getBlockEntity(pos, NanoMiraiBlockEntities.SOLAR_PANEL)
            player.sendSystemMessage(Component.literal(be.get().components().getOrDefault(NanoMiraiDataComponents.ENERGY, 0).toString()))
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T?>
    ): BlockEntityTicker<T?>? {
        if (level.isClientSide) return null

        return createTickerHelper(blockEntityType, NanoMiraiBlockEntities.SOLAR_PANEL) {
            level, pos, state, blockEntity -> blockEntity.tick(level, pos, state)
        }
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }
}