package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.block.LaserEngraverBlock
import dev.bluesheep.nanomirai.block.SynthesizeDisplayBlock
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.util.SynthesizeState
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper

class NanoMiraiBlockProvider(output: PackOutput, exFileHelper: ExistingFileHelper) : BlockStateProvider(output, NanoMirai.ID, exFileHelper) {
    override fun registerStatesAndModels() {
        horizontalBlock(
            NanoMiraiBlocks.NANOMACHINE_ASSEMBLER,
            models().withExistingParent("nanomachine_assembler", mcLoc("block/cube"))
                .texture("particle", modLoc("block/nanomachine_assembler_side"))
                .texture("down", modLoc("block/nanomachine_assembler_bottom"))
                .texture("up", modLoc("block/nanomachine_assembler_top"))
                .texture("north", modLoc("block/nanomachine_assembler_front"))
                .texture("south", modLoc("block/nanomachine_assembler_back"))
                .texture("west", modLoc("block/nanomachine_assembler_side"))
                .texture("east", modLoc("block/nanomachine_assembler_side"))
        )

        val laserEngraverModel = models().cube(
            "laser_engraver",
            modLoc("block/laser_engraver_bottom"),
            modLoc("block/laser_engraver_top"),
            modLoc("block/laser_engraver_front"),
            modLoc("block/laser_engraver_back"),
            modLoc("block/laser_engraver_side"),
            modLoc("block/laser_engraver_side"),
        ).texture("particle", modLoc("block/laser_engraver_back"))
        val laserEngraverCraftingModel = models().cube(
            "laser_engraver_crafting",
            modLoc("block/laser_engraver_bottom"),
            modLoc("block/laser_engraver_top"),
            modLoc("block/laser_engraver_front_crafting"),
            modLoc("block/laser_engraver_back"),
            modLoc("block/laser_engraver_side_crafting"),
            modLoc("block/laser_engraver_side_crafting"),
        ).texture("particle", modLoc("block/laser_engraver_back"))
        getVariantBuilder(NanoMiraiBlocks.LASER_ENGRAVER).forAllStates { state ->
            val facing = state.getValue(LaserEngraverBlock.HORIZONTAL_FACING)
            val isCrafting = state.getValue(LaserEngraverBlock.CRAFTING)
            return@forAllStates ConfiguredModel.builder()
                .modelFile(if (isCrafting) laserEngraverCraftingModel else laserEngraverModel)
                .rotationY(facing.opposite.toYRot().toInt())
                .build()
        }

        getVariantBuilder(NanoMiraiBlocks.SYNTHESIZE_DISPLAY).forAllStates { state ->
            val synthesizeState = state.getValue(SynthesizeDisplayBlock.STATE)
            val name = when (synthesizeState) {
                SynthesizeState.IDLE -> "synthesize_display"
                SynthesizeState.CRAFTING -> "synthesize_display_crafting"
                SynthesizeState.INVALID -> "synthesize_display_invalid"
            }
            return@forAllStates ConfiguredModel.builder()
                .modelFile(
                    models().withExistingParent(name, mcLoc("block/cube_all"))
                        .texture("all", modLoc("block/${name}"))
                        .renderType(mcLoc("translucent"))
                )
                .build()
        }

        horizontalBlock(
            NanoMiraiBlocks.NANO_LAB,
            models().withExistingParent("nano_lab", mcLoc("block/cube"))
                .texture("particle", modLoc("block/nano_lab_back"))
                .texture("down", modLoc("block/nano_lab_bottom"))
                .texture("up", modLoc("block/nano_lab_top"))
                .texture("north", modLoc("block/nano_lab_front"))
                .texture("south", modLoc("block/nano_lab_back"))
                .texture("west", modLoc("block/nano_lab_side"))
                .texture("east", modLoc("block/nano_lab_side"))
        )

        simpleBlock(NanoMiraiBlocks.REINFORCED_OBSIDIAN)
    }
}
