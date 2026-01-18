package dev.bluesheep.nanomirai.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.util.MobCageUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class MobCageItemRenderer : BlockEntityWithoutLevelRenderer(
    Minecraft.getInstance().blockEntityRenderDispatcher,
    Minecraft.getInstance().entityModels
) {
    private val modelPath = ModelResourceLocation.standalone(rl("block/mob_cage"))

    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val entity = MobCageUtil.getEntityFromComponent(
            Minecraft.getInstance().level,
            stack.get(DataComponents.ENTITY_DATA)
        )

        if (displayContext != ItemDisplayContext.FIXED || entity == null) {
            renderBase(stack, poseStack, buffer, packedLight, packedOverlay)
        }

        renderEntity(
            entity,
            displayContext,
            poseStack,
            buffer,
            packedLight
        )
    }

    private fun renderBase(
        stack: ItemStack,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val itemRenderer = Minecraft.getInstance().itemRenderer
        val baseModel = itemRenderer.itemModelShaper.modelManager.getModel(modelPath)

        baseModel.getRenderTypes(stack, true).forEach {
            itemRenderer.renderModelLists(
                baseModel,
                stack,
                packedLight,
                packedOverlay,
                poseStack,
                ItemRenderer.getFoilBufferDirect(
                    buffer,
                    it,
                    true,
                    stack.hasFoil()
                )
            )
        }
    }

    private fun renderEntity(
        entity: Entity?,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        if (entity != null) {
            poseStack.pushPose()
            if (displayContext == ItemDisplayContext.FIXED) {
                poseStack.translate(0.5, -0.5, 0.5)
            } else {
                poseStack.scale(0.5f, 0.5f, 0.5f)
                poseStack.translate(1.0, 0.25, 1.0)
            }
            Minecraft.getInstance().entityRenderDispatcher.render(
                entity,
                0.0,
                0.0,
                0.0,
                0f,
                0f,
                poseStack,
                buffer,
                packedLight
            )
            poseStack.popPose()
        }
    }
}