package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class NanoMiraiBlockProvider(output: PackOutput, exFileHelper: ExistingFileHelper) : BlockStateProvider(output, NanoMirai.ID, exFileHelper) {
    override fun registerStatesAndModels() {
        simpleBlock(NanoMiraiBlocks.NANOMACHINE_ASSEMBLER)
        simpleBlock(NanoMiraiBlocks.LASER_ENGRAVER)
        simpleBlock(
            NanoMiraiBlocks.SYNTHESIZE_DISPLAY,
            models().withExistingParent("synthesize_display", mcLoc("block/cube_all"))
                .texture("all", modLoc("block/synthesize_display"))
                .renderType(mcLoc("translucent"))
        )
    }
}
