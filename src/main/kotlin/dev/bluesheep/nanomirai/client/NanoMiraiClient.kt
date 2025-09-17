package dev.bluesheep.nanomirai.client

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.client.hud.NanoMiraiHud
import dev.bluesheep.nanomirai.client.screen.AssemblerScreen
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = NanoMirai.ID)
object NanoMiraiClient {
    @SubscribeEvent
    fun registerGuiLayers(event: RegisterGuiLayersEvent) {
        event.registerAboveAll(rl("goggles_hud"), NanoMiraiHud::render)
    }

    @SubscribeEvent
    fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(NanoMiraiMenu.ASSEMBLER, ::AssemblerScreen)
    }
}
