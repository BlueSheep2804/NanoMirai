package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.menu.AssemblerMenu
import dev.bluesheep.nanomirai.menu.LaserEngraverMenu
import dev.bluesheep.nanomirai.menu.NanoLabMenu
import net.minecraft.core.registries.Registries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiMenu {
    val REGISTRY: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, NanoMirai.ID)

    val ASSEMBLER: MenuType<AssemblerMenu> by REGISTRY.register("assembler") { ->
        IMenuTypeExtension.create(::AssemblerMenu)
    }
    val LASER_ENGRAVER: MenuType<LaserEngraverMenu> by REGISTRY.register("laser_engraver") { ->
        IMenuTypeExtension.create(::LaserEngraverMenu)
    }
    val NANO_LAB: MenuType<NanoLabMenu> by REGISTRY.register("nano_lab") { ->
        IMenuTypeExtension.create(::NanoLabMenu)
    }
}
