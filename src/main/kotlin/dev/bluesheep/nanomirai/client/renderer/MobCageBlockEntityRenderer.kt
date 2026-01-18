package dev.bluesheep.nanomirai.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import dev.bluesheep.nanomirai.block.entity.MobCageBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.CustomData
import org.joml.Quaternionf

class MobCageBlockEntityRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<MobCageBlockEntity> {
    val entityRenderer: EntityRenderDispatcher = context.entityRenderer

    override fun render(
        blockEntity: MobCageBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val entity = blockEntity.entity
        if (entity != null) {
            poseStack.pushPose()
            poseStack.scale(0.5f, 0.5f, 0.5f)
            poseStack.translate(0.5, 0.25, 0.5)
            entityRenderer.render(
                entity,
                0.5,
                0.0,
                0.5,
                0f,
                partialTick,
                poseStack,
                bufferSource,
                packedLight
            )
            poseStack.popPose()
        }
    }
}