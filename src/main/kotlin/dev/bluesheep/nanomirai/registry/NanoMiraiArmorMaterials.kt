package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.NanoMirai.rl
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object NanoMiraiArmorMaterials {
    val REGISTRY: DeferredRegister<ArmorMaterial> = DeferredRegister<ArmorMaterial>.create(BuiltInRegistries.ARMOR_MATERIAL, NanoMirai.ID)

    val GOGGLES_MATERIAL: DeferredHolder<ArmorMaterial, ArmorMaterial> = REGISTRY.register("goggles") { ->
        ArmorMaterial(
            mapOf<ArmorItem.Type, Int>(Pair(ArmorItem.Type.HELMET, 0)),
            0,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            { -> Ingredient.of() },
            listOf(
                ArmorMaterial.Layer(
                    rl("goggles")
                )
            ),
            0F,
            0F
        )
    }
}
