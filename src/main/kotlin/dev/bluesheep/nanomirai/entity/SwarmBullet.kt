package dev.bluesheep.nanomirai.entity

import dev.bluesheep.nanomirai.registry.NanoMiraiEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec2

class SwarmBullet(entityType: EntityType<out ThrowableItemProjectile>, level: Level) : ThrowableItemProjectile(entityType, level) {
    constructor(level: Level, x: Double, y: Double, z: Double) : this(NanoMiraiEntities.SWARM_BULLET, level) {
        setPos(x, y, z)
    }

    constructor(level: Level, player: Player) : this(level, player.x, player.eyeY - 1F, player.z) {
        owner = player
    }

    override fun getDefaultItem(): Item {
        return NanoMiraiItems.NANO_SWARM_BLASTER
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) {
            if (tickCount > 2) {
                level().addParticle(ParticleTypes.GLOW, x, eyeY, z, 0.0, 0.0, 0.0)
            }
        } else {
            if (tickCount > 60) {
                discard()
            }
            level().getEntitiesOfClass(LivingEntity::class.java, boundingBox) { !ownedBy(it) }.forEach {
                item.get(DataComponents.POTION_CONTENTS)?.forEachEffect { effect ->
                    it.addEffect(MobEffectInstance(effect))
                }
            }
        }
    }

    override fun getDefaultGravity(): Double {
        return 0.0
    }
}
