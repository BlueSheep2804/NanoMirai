package dev.bluesheep.nanomirai.util

import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

object MobCageUtil {
    fun createEntityData(entityType: EntityType<*>): CompoundTag {
        return CompoundTag().apply {
            put("components", CompoundTag().apply {
                put(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(DataComponents.ENTITY_DATA).toString(), CompoundTag().apply {
                    putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString())
                })
            })
        }
    }

    fun getEntityFromComponent(level: Level?, entityData: CustomData?): Entity? {
        if (entityData?.isEmpty ?: true) return null
        return level?.let {
            EntityType.loadEntityRecursive(
                entityData.copyTag(),
                it,
                java.util.function.Function.identity()
            )
        }
    }

    fun captureEntity(entity: Entity): CustomData? {
        val entityData = CompoundTag()
        if (!entity.save(entityData)) return null
        entityData.remove("Pos")
        entityData.remove("Rotation")
        entityData.remove("Motion")
        entityData.remove("HurtTime")
        entity.remove(Entity.RemovalReason.DISCARDED)
        return CustomData.of(entityData)
    }

    fun getEntityTooltip(entity: Entity): MutableComponent {
        val default = entity.type.description
        return if (entity.hasCustomName()) {
            Component.translatable("block.nanomirai.mob_cage.tooltip.name", default, entity.name.copy().withStyle(
                ChatFormatting.ITALIC))
        } else {
            Component.translatable("block.nanomirai.mob_cage.tooltip", default)
        }
    }
}