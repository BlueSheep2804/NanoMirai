package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.menu.AssemblerMenu
import net.minecraft.core.registries.Registries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiMenu {
    val REGISTRY: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, NanoMirai.ID)

    val NANOMACHINE_ASSEMBLER: MenuType<AssemblerMenu> by REGISTRY.register("nanomachine_assembler") { ->
        MenuType(::AssemblerMenu, FeatureFlags.DEFAULT_FLAGS)
    }
}