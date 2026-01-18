package dev.bluesheep.nanomirai.util

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

object MobCageUtil {
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
}