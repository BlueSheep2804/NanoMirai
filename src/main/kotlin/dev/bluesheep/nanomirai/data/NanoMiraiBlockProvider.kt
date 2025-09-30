package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.LaserEngraverBlock
import dev.bluesheep.nanomirai.block.SynthesizeDisplayBlock
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper

class NanoMiraiBlockProvider(output: PackOutput, exFileHelper: ExistingFileHelper) : BlockStateProvider(output, NanoMirai.ID, exFileHelper) {
    override fun registerStatesAndModels() {
        simpleBlock(NanoMiraiBlocks.NANOMACHINE_ASSEMBLER)

        val laserEngraverModel = models().orientable(
            "laser_engraver",
            modLoc("block/laser_engraver_side"),
            modLoc("block/laser_engraver"),
            modLoc("block/laser_engraver_top")
        )
        val laserEngraverCraftingModel = models().orientable(
            "laser_engraver_crafting",
            modLoc("block/laser_engraver_side_crafting"),
            modLoc("block/laser_engraver_crafting"),
            modLoc("block/laser_engraver_top")
        )
        getVariantBuilder(NanoMiraiBlocks.LASER_ENGRAVER).forAllStates { state ->
            val facing = state.getValue(LaserEngraverBlock.HORIZONTAL_FACING)
            val isCrafting = state.getValue(SynthesizeDisplayBlock.CRAFTING)
            return@forAllStates ConfiguredModel.builder()
                .modelFile(if (isCrafting) laserEngraverCraftingModel else laserEngraverModel)
                .rotationY(facing.opposite.toYRot().toInt())
                .build()
        }

        getVariantBuilder(NanoMiraiBlocks.SYNTHESIZE_DISPLAY).forAllStates { state ->
            val isCrafting = state.getValue(SynthesizeDisplayBlock.CRAFTING)
            val name = if (isCrafting) "synthesize_display_crafting" else "synthesize_display"
            return@forAllStates ConfiguredModel.builder()
                .modelFile(
                    models().withExistingParent(name, mcLoc("block/cube_all"))
                        .texture("all", modLoc("block/${name}"))
                        .renderType(mcLoc("translucent"))
                )
                .build()
        }

        simpleBlock(NanoMiraiBlocks.NANO_LAB)
    }
}
