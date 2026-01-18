package dev.bluesheep.nanomirai.util

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
}