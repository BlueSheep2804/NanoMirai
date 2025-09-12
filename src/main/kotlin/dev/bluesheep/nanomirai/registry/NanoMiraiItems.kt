package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.item.NanoSwarmItem
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

    val NANO_SEED: Item by REGISTRY.registerSimpleItem("nano_seed")
    val NANO_MATRIX: Item by REGISTRY.registerSimpleItem("nano_matrix")
    val NANO_SINGULARITY: Item by REGISTRY.registerSimpleItem("nano_singularity")

    val NANO_SEED_SWARM: NanoSwarmItem by REGISTRY.register("nano_seed_swarm") { ->
        NanoSwarmItem(0, Item.Properties())
    }
    val NANO_MATRIX_SWARM: NanoSwarmItem by REGISTRY.register("nano_matrix_swarm") { ->
        NanoSwarmItem(1, Item.Properties())
    }
    val NANO_SINGULARITY_SWARM: NanoSwarmItem by REGISTRY.register("nano_singularity_swarm") { ->
        NanoSwarmItem(2, Item.Properties())
    }
}
