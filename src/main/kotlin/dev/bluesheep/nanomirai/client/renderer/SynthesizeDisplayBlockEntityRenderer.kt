package dev.bluesheep.nanomirai.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.neoforged.neoforge.client.model.data.ModelData

class SynthesizeDisplayBlockEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<SynthesizeDisplayBlockEntity> {
    val blockRenderDispatcher: BlockRenderDispatcher = context.blockRenderDispatcher

    override fun render(
        blockEntity: SynthesizeDisplayBlockEntity,
        partialTick: Float,
        stack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        stack.pushPose()
        stack.translate(0.125, 0.125, 0.125)
        stack.scale(0.75f, 0.75f, 0.75f)
        blockRenderDispatcher.renderSingleBlock(blockEntity.block.blockState, stack, bufferSource, packedLight, packedOverlay, ModelData.EMPTY, RenderType.TRANSLUCENT)
        stack.popPose()
    }
}
