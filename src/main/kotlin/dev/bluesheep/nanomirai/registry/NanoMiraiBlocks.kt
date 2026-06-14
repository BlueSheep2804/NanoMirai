package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.AllayHeadBlock
import dev.bluesheep.nanomirai.block.AllayWallHeadBlock
import dev.bluesheep.nanomirai.block.AssemblerBlock
import dev.bluesheep.nanomirai.block.LaserEngraverBlock
import dev.bluesheep.nanomirai.block.MobCageBlock
import dev.bluesheep.nanomirai.block.NanoLabBlock
import dev.bluesheep.nanomirai.block.SolarPanelBlock
import dev.bluesheep.nanomirai.block.SynthesizeDisplayBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
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
    val MOB_CAGE: Block by REGISTRY.registerBlock(
        "mob_cage",
        ::MobCageBlock,
        BlockBehaviour.Properties.of().noCollission().noOcclusion()
    )
    val SOLAR_PANEL: SolarPanelBlock by REGISTRY.registerBlock("solar_panel", ::SolarPanelBlock, machineProperties)

    val REINFORCED_OBSIDIAN: Block by REGISTRY.registerSimpleBlock(
        "reinforced_obsidian",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(50F, 3600000F)
            .requiresCorrectToolForDrops()
    )
    val ALLAY_HEAD: Block by REGISTRY.registerBlock(
        "allay_head",
        ::AllayHeadBlock,
        BlockBehaviour.Properties.of().noOcclusion().dynamicShape()
    )
    val ALLAY_WALL_HEAD: Block by REGISTRY.registerBlock(
        "allay_wall_head",
        ::AllayWallHeadBlock,
        BlockBehaviour.Properties.of().noOcclusion().dynamicShape()
    )
    val RAW_SCULMIUM_BLOCK: Block by REGISTRY.registerSimpleBlock(
        "raw_sculmium_block",
        BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)
    )
    val SCULMIUM_BLOCK: Block by REGISTRY.registerSimpleBlock(
        "sculmium_block",
        BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
    )

    private fun never(state: BlockState, blockGetter: BlockGetter, pos: BlockPos): Boolean = false
}
