package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(NanoMirai.ID)

    val SYNTHESIZE_NANO: SynthesizeNanoItem by REGISTRY.register("synthesize_nano", ::SynthesizeNanoItem)
    val SUPPORT_NANO: SupportNanoItem by REGISTRY.register("support_nano", ::SupportNanoItem)
    val NANO_SWARM_BLASTER: NanoSwarmBlasterItem by REGISTRY.register("nano_swarm_blaster", ::NanoSwarmBlasterItem)
    val REPAIR_NANO: Item by REGISTRY.registerSimpleItem("repair_nano")

    val GRAPHITE: Item by REGISTRY.registerSimpleItem("graphite")
    val SILICON: Item by REGISTRY.registerSimpleItem("silicon")
    val SILICON_WAFER: Item by REGISTRY.registerSimpleItem("silicon_wafer")
    val RAW_SCULMIUM: Item by REGISTRY.registerSimpleItem("raw_sculmium")
    val SCULMIUM_INGOT: Item by REGISTRY.registerSimpleItem("sculmium_ingot")
    val SIMPLE_CIRCUIT: Item by REGISTRY.registerSimpleItem("simple_circuit")
    val NORMAL_CIRCUIT: Item by REGISTRY.registerSimpleItem("normal_circuit")
    val NANO_CIRCUIT: Item by REGISTRY.registerSimpleItem("nano_circuit")
    val SCULMIUM_CIRCUIT: Item by REGISTRY.registerSimpleItem("sculmium_circuit")
    val AMETHYST_LENS: Item by catalyst("amethyst_lens")
    val SCULK_LENS: Item by catalyst("sculk_lens")
    val RED_RESEARCH_CATALYST: Item by catalyst("red_research_catalyst")
    val GREEN_RESEARCH_CATALYST: Item by catalyst("green_research_catalyst")
    val BLUE_RESEARCH_CATALYST: Item by catalyst("blue_research_catalyst")
    val CYAN_RESEARCH_CATALYST: Item by catalyst("cyan_research_catalyst")
    val MAGENTA_RESEARCH_CATALYST: Item by catalyst("magenta_research_catalyst")
    val YELLOW_RESEARCH_CATALYST: Item by catalyst("yellow_research_catalyst")

    val NANOMACHINE_ASSEMBLER: BlockItem by REGISTRY.registerSimpleBlockItem("nanomachine_assembler") { ->
        NanoMiraiBlocks.NANOMACHINE_ASSEMBLER
    }
    val LASER_ENGRAVER: BlockItem by REGISTRY.registerSimpleBlockItem("laser_engraver") { ->
        NanoMiraiBlocks.LASER_ENGRAVER
    }
    val NANO_LAB: BlockItem by REGISTRY.registerSimpleBlockItem("nano_lab") { ->
        NanoMiraiBlocks.NANO_LAB
    }

    val REINFORCED_OBSIDIAN: BlockItem by REGISTRY.registerSimpleBlockItem("reinforced_obsidian") { ->
        NanoMiraiBlocks.REINFORCED_OBSIDIAN
    }

    fun catalyst(id: String): DeferredItem<Item> = REGISTRY.registerSimpleItem(
        id,
        Item.Properties().stacksTo(1)
    )
}
