package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(NanoMirai.ID)

    val GOGGLES: ArmorItem by REGISTRY.register("goggles") { ->
        ArmorItem(
            NanoMiraiArmorMaterials.GOGGLES_MATERIAL,
            ArmorItem.Type.HELMET,
            Item.Properties().stacksTo(1)
        )
    }

    val NANO_PROTO: Item by REGISTRY.registerSimpleItem("nano_proto")
    val NANO_CELL: Item by REGISTRY.registerSimpleItem("nano_cell")
    val NANO_MATRIX: Item by REGISTRY.registerSimpleItem("nano_matrix")
    val NANO_SINGULARITY: Item by REGISTRY.registerSimpleItem("nano_singularity")
    val SYNTHESIZE_NANO: SynthesizeNanoItem by REGISTRY.register("synthesize_nano") { ->
        SynthesizeNanoItem(Item.Properties())
    }
    val SUPPORT_NANO: SupportNanoItem by REGISTRY.register("support_nano", ::SupportNanoItem)
    val NANO_SWARM_BLASTER: NanoSwarmBlasterItem by REGISTRY.register("nano_swarm_blaster", ::NanoSwarmBlasterItem)

    val BROKEN_NANOMACHINE: Item by REGISTRY.registerSimpleItem("broken_nanomachine")
    val GRAPHENE_SHEET: Item by REGISTRY.registerSimpleItem("graphene_sheet")
    val GRAPHITE: Item by REGISTRY.registerSimpleItem("graphite")
    val AMETHYST_LENS: Item by REGISTRY.registerSimpleItem("amethyst_lens")
    val SCULK_LENS: Item by REGISTRY.registerSimpleItem("sculk_lens")

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
