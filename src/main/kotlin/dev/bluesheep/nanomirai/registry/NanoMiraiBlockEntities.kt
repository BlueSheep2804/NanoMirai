package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.entity.AssemblerBlockEntity
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiBlockEntities {
    val REGISTRY: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NanoMirai.ID)

    val NANOMACHINE_ASSEBLER: BlockEntityType<AssemblerBlockEntity> by REGISTRY.register("nanomachine_assembler") { ->
        BlockEntityType.Builder.of(
            ::AssemblerBlockEntity,
            NanoMiraiBlocks.NANOMACHINE_ASSEMBLER
        ).build(null)
    }
}
