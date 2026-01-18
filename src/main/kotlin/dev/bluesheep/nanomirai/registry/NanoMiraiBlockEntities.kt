package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.entity.AssemblerBlockEntity
import dev.bluesheep.nanomirai.block.entity.LaserEngraverBlockEntity
import dev.bluesheep.nanomirai.block.entity.MobCageBlockEntity
import dev.bluesheep.nanomirai.block.entity.NanoLabBlockEntity
import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiBlockEntities {
    val REGISTRY: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NanoMirai.ID)

    val NANOMACHINE_ASSEMBLER: BlockEntityType<AssemblerBlockEntity> by REGISTRY.register("nanomachine_assembler") { ->
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
    val NANO_LAB: BlockEntityType<NanoLabBlockEntity> by REGISTRY.register("nano_lab") { ->
        BlockEntityType.Builder.of(
            ::NanoLabBlockEntity,
            NanoMiraiBlocks.NANO_LAB
        ).build(null)
    }
    val MOB_CAGE: BlockEntityType<MobCageBlockEntity> by REGISTRY.register("mob_cage") { ->
        BlockEntityType.Builder.of(
            ::MobCageBlockEntity,
            NanoMiraiBlocks.MOB_CAGE
        ).build(null)
    }
}
