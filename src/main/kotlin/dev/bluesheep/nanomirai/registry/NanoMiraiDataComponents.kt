package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ExtraCodecs
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiDataComponents {
    val REGISTRY: DeferredRegister.DataComponents = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, NanoMirai.ID)

    val ENERGY: DataComponentType<Int> by REGISTRY.registerComponentType("energy") { builder ->
        builder
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
    }

    val ENERGY_CAPACITY: DataComponentType<Int> by REGISTRY.registerComponentType("energy_capacity") { builder ->
        builder
            .persistent(ExtraCodecs.POSITIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
    }
}