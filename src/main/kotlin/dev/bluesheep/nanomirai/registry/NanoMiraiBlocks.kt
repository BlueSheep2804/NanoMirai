package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.AssemblerBlock
import dev.bluesheep.nanomirai.block.LaserEngraverBlock
import dev.bluesheep.nanomirai.block.NanoLabBlock
import dev.bluesheep.nanomirai.block.SynthesizeDisplayBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiBlocks {
    val REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(NanoMirai.ID)

    val machineProperties: BlockBehaviour.Properties = BlockBehaviour.Properties.of()
        .destroyTime(1F)
        .requiresCorrectToolForDrops()
        .strength(3F, 5F)

    val NANOMACHINE_ASSEMBLER: AssemblerBlock by REGISTRY.registerBlock("nanomachine_assembler", ::AssemblerBlock, machineProperties)
    val LASER_ENGRAVER: Block by REGISTRY.registerBlock("laser_engraver", ::LaserEngraverBlock, machineProperties)
    val SYNTHESIZE_DISPLAY: Block by REGISTRY.registerBlock(
        "synthesize_display",
        ::SynthesizeDisplayBlock,
        BlockBehaviour.Properties.of().strength(0.5F, 3600000F).noLootTable().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(::never).isSuffocating(::never).isViewBlocking(::never)
    )
    val NANO_LAB: NanoLabBlock by REGISTRY.registerBlock("nano_lab", ::NanoLabBlock, machineProperties)

    private fun never(state: BlockState, blockGetter: BlockGetter, pos: BlockPos): Boolean = false
}
