package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(NanoMirai.ID)

    val NANO_PROTO: Item by REGISTRY.registerSimpleItem("nano_proto")
    val NANO_CELL: Item by REGISTRY.registerSimpleItem("nano_cell", Item.Properties().rarity(Rarity.UNCOMMON))
    val NANO_MATRIX: Item by REGISTRY.registerSimpleItem("nano_matrix", Item.Properties().rarity(Rarity.RARE))
    val NANO_SINGULARITY: Item by REGISTRY.registerSimpleItem("nano_singularity", Item.Properties().rarity(Rarity.EPIC))
    val SYNTHESIZE_NANO: SynthesizeNanoItem by REGISTRY.register("synthesize_nano") { ->
        SynthesizeNanoItem(Item.Properties())
    }
    val SUPPORT_NANO: SupportNanoItem by REGISTRY.register("support_nano", ::SupportNanoItem)
    val NANO_SWARM_BLASTER: NanoSwarmBlasterItem by REGISTRY.register("nano_swarm_blaster", ::NanoSwarmBlasterItem)

    val GRAPHITE: Item by REGISTRY.registerSimpleItem("graphite")
    val SILICON: Item by REGISTRY.registerSimpleItem("silicon")
    val SIMPLE_CIRCUIT: Item by REGISTRY.registerSimpleItem("simple_circuit")
    val NORMAL_CIRCUIT: Item by REGISTRY.registerSimpleItem("normal_circuit")
    val NANO_CIRCUIT: Item by REGISTRY.registerSimpleItem("nano_circuit")
    val NANO_SOC: Item by REGISTRY.registerSimpleItem("nano_soc")
    val AMETHYST_LENS: Item by REGISTRY.registerSimpleItem("amethyst_lens")
    val SCULK_LENS: Item by REGISTRY.registerSimpleItem("sculk_lens")
    val RED_RESEARCH_CATALYST: Item by REGISTRY.registerSimpleItem("red_research_catalyst")
    val GREEN_RESEARCH_CATALYST: Item by REGISTRY.registerSimpleItem("green_research_catalyst")
    val BLUE_RESEARCH_CATALYST: Item by REGISTRY.registerSimpleItem("blue_research_catalyst")
    val CYAN_RESEARCH_CATALYST: Item by REGISTRY.registerSimpleItem("cyan_research_catalyst")
    val MAGENTA_RESEARCH_CATALYST: Item by REGISTRY.registerSimpleItem("magenta_research_catalyst")
    val YELLOW_RESEARCH_CATALYST: Item by REGISTRY.registerSimpleItem("yellow_research_catalyst")

    val NANOMACHINE_ASSEMBLER: BlockItem by REGISTRY.registerSimpleBlockItem("nanomachine_assembler") { ->
        NanoMiraiBlocks.NANOMACHINE_ASSEMBLER
    }
    val LASER_ENGRAVER: BlockItem by REGISTRY.registerSimpleBlockItem("laser_engraver") { ->
        NanoMiraiBlocks.LASER_ENGRAVER
    }
    val NANO_LAB: BlockItem by REGISTRY.registerSimpleBlockItem("nano_lab") { ->
        NanoMiraiBlocks.NANO_LAB
    }
}
