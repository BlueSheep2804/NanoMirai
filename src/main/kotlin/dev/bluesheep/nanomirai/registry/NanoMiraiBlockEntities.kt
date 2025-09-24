package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.entity.AssemblerBlockEntity
import dev.bluesheep.nanomirai.block.entity.LaserEngraverBlockEntity
import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
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
    val LASER_ENGRAVER: BlockEntityType<LaserEngraverBlockEntity> by REGISTRY.register("laser_engraver") { ->
        BlockEntityType.Builder.of(
            ::LaserEngraverBlockEntity,
            NanoMiraiBlocks.LASER_ENGRAVER
        ).build(null)
    }
    val SYNTHESIZE_DISPLAY: BlockEntityType<SynthesizeDisplayBlockEntity> by REGISTRY.register("synthesize_display") { ->
        BlockEntityType.Builder.of(
            ::SynthesizeDisplayBlockEntity,
            NanoMiraiBlocks.SYNTHESIZE_DISPLAY
        ).build(null)
    }
}
