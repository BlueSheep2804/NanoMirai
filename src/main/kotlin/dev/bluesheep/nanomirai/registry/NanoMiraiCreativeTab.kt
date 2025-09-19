package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object NanoMiraiCreativeTab {
    val REGISTRY: DeferredRegister<CreativeModeTab> = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NanoMirai.ID)

    val TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTRY.register("nanomirai_tab") { ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.nanomirai"))
            .icon { ItemStack(NanoMiraiItems.NANO_CELL) }
            .displayItems { params, output ->
                NanoMiraiItems.REGISTRY.entries.forEach { item ->
                    output.accept(item.get())
                }
            }
            .build()
    }
}
