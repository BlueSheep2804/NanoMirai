package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.AssemblerBlock
import dev.bluesheep.nanomirai.block.LaserEngraverBlock
import dev.bluesheep.nanomirai.block.SynthesizeDisplayBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiBlocks {
    val REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(NanoMirai.ID)

    val NANOMACHINE_ASSEMBLER: AssemblerBlock by REGISTRY.registerBlock("nanomachine_assembler", ::AssemblerBlock)
    val LASER_ENGRAVER: Block by REGISTRY.registerBlock("laser_engraver", ::LaserEngraverBlock)
    val SYNTHESIZE_DISPLAY: Block by REGISTRY.registerBlock(
        "synthesize_display",
        ::SynthesizeDisplayBlock,
        BlockBehaviour.Properties.of().strength(-1F, 3600000F).noLootTable().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(::never).isSuffocating(::never).isViewBlocking(::never)
    )

    private fun never(state: BlockState, blockGetter: BlockGetter, pos: BlockPos): Boolean {
        return false;
    }
}
