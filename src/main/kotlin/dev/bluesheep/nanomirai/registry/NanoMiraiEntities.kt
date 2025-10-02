package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.entity.SwarmBullet
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiEntities {
    val REGISTRY: DeferredRegister<EntityType<*>> = DeferredRegister.create(Registries.ENTITY_TYPE, NanoMirai.ID)

    val SWARM_BULLET: EntityType<SwarmBullet> by REGISTRY.register("swarm_bullet") { ->
        EntityType.Builder.of(
            ::SwarmBullet,
            MobCategory.MISC
        )
            .sized(2f, 2f)
            .eyeHeight(1f)
            .build(rl("swarm_bullet").toString())
    }
}
