package dev.bluesheep.nanomirai.client

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.client.renderer.MobCageBlockEntityRenderer
import dev.bluesheep.nanomirai.client.renderer.MobCageItemExtensions
import dev.bluesheep.nanomirai.client.renderer.SynthesizeDisplayBlockEntityRenderer
import dev.bluesheep.nanomirai.client.screen.AssemblerScreen
import dev.bluesheep.nanomirai.client.screen.LaserEngraverScreen
import dev.bluesheep.nanomirai.client.screen.NanoLabScreen
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.client.resources.model.ModelResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.ModelEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT

@Mod(NanoMirai.ID, dist = [Dist.CLIENT])
@EventBusSubscriber(value = [Dist.CLIENT], modid = NanoMirai.ID)
object NanoMiraiClient {
    init {
        LOADING_CONTEXT.registerExtensionPoint(IConfigScreenFactory::class.java) {
            IConfigScreenFactory { container: ModContainer, modListScreen: Screen ->
                ConfigurationScreen(
                    container,
                    modListScreen
                )
            }
        }
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork {
            ItemProperties.register(
                NanoMiraiItems.NANO_SWARM_BLASTER,
                rl("charged"),
                NanoSwarmBlasterItem::overrideProperty
            )
        }
    }

    @SubscribeEvent
    fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(NanoMiraiMenu.ASSEMBLER, ::AssemblerScreen)
        event.register(NanoMiraiMenu.LASER_ENGRAVER, ::LaserEngraverScreen)
        event.register(NanoMiraiMenu.NANO_LAB, ::NanoLabScreen)
    }

    @SubscribeEvent
    fun registerEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(NanoMiraiBlockEntities.SYNTHESIZE_DISPLAY, ::SynthesizeDisplayBlockEntityRenderer)
        event.registerBlockEntityRenderer(NanoMiraiBlockEntities.MOB_CAGE, ::MobCageBlockEntityRenderer)
    }

    @SubscribeEvent
    fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        event.registerItem(MobCageItemExtensions(), NanoMiraiItems.MOB_CAGE)
    }

    @SubscribeEvent
    fun registerAdditionalModels(event: ModelEvent.RegisterAdditional) {
        event.register(ModelResourceLocation.standalone(rl("block/mob_cage")))
    }
}
