package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class NanoMiraiItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) : ItemModelProvider(output, NanoMirai.ID, existingFileHelper) {
    override fun registerModels() {
        basicItem(NanoMiraiItems.SYNTHESIZE_NANO)
        basicItem(NanoMiraiItems.SUPPORT_NANO)

        getBuilder("nano_swarm_blaster_charged")
            .parent(getExistingFile(mcLoc("item/handheld")))
            .texture("layer0", "item/nano_swarm_blaster_charged")

        getBuilder("nano_swarm_blaster")
            .parent(getExistingFile(mcLoc("item/handheld")))
            .texture("layer0", "item/nano_swarm_blaster")
            .override()
                .predicate(modLoc("charged"), 0.9f)
                .model(getExistingFile(modLoc("item/nano_swarm_blaster_charged")))
                .end()

        basicItem(NanoMiraiItems.REPAIR_NANO)

        basicItem(NanoMiraiItems.GRAPHITE)
        basicItem(NanoMiraiItems.SILICON)
        basicItem(NanoMiraiItems.SILICON_WAFER)
        basicItem(NanoMiraiItems.RAW_SCULMIUM)
        basicItem(NanoMiraiItems.SCULMIUM_INGOT)
        basicItem(NanoMiraiItems.SIMPLE_CIRCUIT)
        basicItem(NanoMiraiItems.NORMAL_CIRCUIT)
        basicItem(NanoMiraiItems.NANO_CIRCUIT)
        basicItem(NanoMiraiItems.SCULMIUM_CIRCUIT)
        basicItem(NanoMiraiItems.AMETHYST_LENS)
        basicItem(NanoMiraiItems.SCULK_LENS)
        basicItem(NanoMiraiItems.RED_RESEARCH_CATALYST)
        basicItem(NanoMiraiItems.GREEN_RESEARCH_CATALYST)
        basicItem(NanoMiraiItems.BLUE_RESEARCH_CATALYST)
        basicItem(NanoMiraiItems.CYAN_RESEARCH_CATALYST)
        basicItem(NanoMiraiItems.MAGENTA_RESEARCH_CATALYST)
        basicItem(NanoMiraiItems.YELLOW_RESEARCH_CATALYST)

        simpleBlockItem(NanoMiraiBlocks.NANOMACHINE_ASSEMBLER)
        simpleBlockItem(NanoMiraiBlocks.LASER_ENGRAVER)
        simpleBlockItem(NanoMiraiBlocks.NANO_LAB)

        simpleBlockItem(NanoMiraiBlocks.REINFORCED_OBSIDIAN)
    }
}