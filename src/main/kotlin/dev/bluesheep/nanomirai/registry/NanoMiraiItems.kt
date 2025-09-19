package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.item.NanoMachineItem
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

    val NANO_PROTO: NanoMachineItem by REGISTRY.register("nano_proto") { ->
        NanoMachineItem(0, Item.Properties())
    }
    val NANO_CELL: NanoMachineItem by REGISTRY.register("nano_cell") { ->
        NanoMachineItem(1, Item.Properties())
    }
    val NANO_MATRIX: NanoMachineItem by REGISTRY.register("nano_matrix") { ->
        NanoMachineItem(2, Item.Properties())
    }
    val NANO_SINGULARITY: NanoMachineItem by REGISTRY.register("nano_singularity") { ->
        NanoMachineItem(3, Item.Properties())
    }

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
}
