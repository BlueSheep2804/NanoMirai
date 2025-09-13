package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.item.NanoMachineItem
import net.minecraft.world.item.ArmorItem
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

    val NANO_SEED: NanoMachineItem by REGISTRY.register("nano_seed") { ->
        NanoMachineItem(0, Item.Properties())
    }
    val NANO_MATRIX: NanoMachineItem by REGISTRY.register("nano_matrix") { ->
        NanoMachineItem(1, Item.Properties())
    }
    val NANO_SINGULARITY: NanoMachineItem by REGISTRY.register("nano_singularity") { ->
        NanoMachineItem(2, Item.Properties())
    }
}
