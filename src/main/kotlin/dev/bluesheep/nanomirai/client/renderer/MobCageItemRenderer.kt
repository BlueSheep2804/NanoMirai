package dev.bluesheep.nanomirai.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import dev.bluesheep.nanomirai.NanoMirai.rl
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData

class MobCageItemRenderer : BlockEntityWithoutLevelRenderer(
    Minecraft.getInstance().blockEntityRenderDispatcher,
    Minecraft.getInstance().entityModels
) {
    private val modelPath = ModelResourceLocation.standalone(rl("item/mob_cage_base"))

    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val minecraft = Minecraft.getInstance()
        val itemRenderer = minecraft.itemRenderer
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

        val entityData = stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY)
        if (entityData.isEmpty) return
        val entity = minecraft.level?.let { EntityType.loadEntityRecursive(
            entityData.copyTag(),
            it,
            java.util.function.Function.identity()
        )}
        if (entity != null) {
            poseStack.pushPose()
            poseStack.scale(0.5f, 0.5f, 0.5f)
            minecraft.entityRenderDispatcher.render(
                entity,
                1.0,
                1.0 - entity.bbHeight / 2,
                1.1,
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