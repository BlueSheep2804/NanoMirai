package dev.bluesheep.nanomirai.item

import com.mojang.serialization.MapCodec
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.gameevent.GameEvent
import kotlin.jvm.optionals.getOrElse

class MobCageItem : Item(
    Properties().stacksTo(1)
        .component(DataComponents.ENTITY_DATA, CustomData.EMPTY)
) {
    companion object {
        val ENTITY_TYPE_FIELD_CODEC: MapCodec<EntityType<*>> = BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id")
    }

    override fun interactLivingEntity(
        stack: ItemStack,
        player: Player,
        interactionTarget: LivingEntity,
        usedHand: InteractionHand
    ): InteractionResult {
        // 引数のstackにcomponentがセットできないっぽい?
        val stack = player.getItemInHand(usedHand)
        if (!stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).isEmpty) return InteractionResult.PASS

        if (!player.level().isClientSide) {
            val entityData = CompoundTag()
            if (!interactionTarget.save(entityData)) return InteractionResult.FAIL
            entityData.remove("Pos")
            stack.set(DataComponents.ENTITY_DATA, CustomData.of(entityData))

            interactionTarget.remove(Entity.RemovalReason.DISCARDED)
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val stack = context.itemInHand
        val entityData = stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY)
        if (entityData.isEmpty) return InteractionResult.PASS

        if (!level.isClientSide && level is ServerLevel) {
            val pos = if (level.getBlockState(context.clickedPos).getCollisionShape(level, context.clickedPos).isEmpty) {
                context.clickedPos
            } else {
                context.clickedPos.relative(context.clickedFace)
            }

            val entityType = entityData.read(ENTITY_TYPE_FIELD_CODEC).result().getOrElse { return InteractionResult.PASS }
            entityType.spawn(
                level,
                stack,
                context.player,
                pos,
                MobSpawnType.MOB_SUMMONED,
                false,
                false
            )
            stack.set(DataComponents.ENTITY_DATA, CustomData.EMPTY)
            level.gameEvent(context.player, GameEvent.ENTITY_PLACE, pos)
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }
}