package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.AssemblerBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiBlocks {
    val REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(NanoMirai.ID)

    val EXAMPLE_BLOCK by REGISTRY.register("example_block") { ->
        Block(BlockBehaviour.Properties.of().lightLevel { 15 }.strength(3.0f))
    }
    val NANOMACHINE_ASSEMBLER: AssemblerBlock by REGISTRY.registerBlock("nanomachine_assembler", ::AssemblerBlock)
}
