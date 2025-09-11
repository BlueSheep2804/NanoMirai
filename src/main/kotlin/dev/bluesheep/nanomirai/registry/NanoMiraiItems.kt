package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
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
}