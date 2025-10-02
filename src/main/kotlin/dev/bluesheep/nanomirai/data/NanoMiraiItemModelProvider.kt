package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class NanoMiraiItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) : ItemModelProvider(output, NanoMirai.ID, existingFileHelper) {
    override fun registerModels() {
        basicItem(NanoMiraiItems.GOGGLES)
        basicItem(NanoMiraiItems.NANO_PROTO)
        basicItem(NanoMiraiItems.NANO_CELL)
        basicItem(NanoMiraiItems.NANO_MATRIX)
        basicItem(NanoMiraiItems.NANO_SINGULARITY)
        basicItem(NanoMiraiItems.SYNTHESIZE_NANO)
        basicItem(NanoMiraiItems.SUPPORT_NANO)
        handheldItem(NanoMiraiItems.NANO_SWARM_BLASTER)
        basicItem(NanoMiraiItems.BROKEN_NANOMACHINE)
        basicItem(NanoMiraiItems.GRAPHENE_SHEET)
        basicItem(NanoMiraiItems.GRAPHITE)
        basicItem(NanoMiraiItems.AMETHYST_LENS)
        basicItem(NanoMiraiItems.SCULK_LENS)

        simpleBlockItem(NanoMiraiBlocks.NANOMACHINE_ASSEMBLER)
        simpleBlockItem(NanoMiraiBlocks.LASER_ENGRAVER)
        simpleBlockItem(NanoMiraiBlocks.NANO_LAB)
    }
}