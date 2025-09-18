package dev.bluesheep.nanomirai

import dev.bluesheep.nanomirai.data.NanoMiraiBlockProvider
import dev.bluesheep.nanomirai.data.NanoMiraiItemModelProvider
import dev.bluesheep.nanomirai.data.NanoMiraiRecipeProvider
import dev.bluesheep.nanomirai.network.ClientPayloadHandler
import dev.bluesheep.nanomirai.network.DeployedNanomachineData
import dev.bluesheep.nanomirai.registry.NanoMiraiArmorMaterials
import dev.bluesheep.nanomirai.registry.NanoMiraiAttachmentTypes
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import dev.bluesheep.nanomirai.registry.NanoMiraiCreativeTab
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiMenu
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeSerializer
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

/**
 * Main mod class.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(NanoMirai.ID)
@EventBusSubscriber()
object NanoMirai {
    const val ID = "nanomirai"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        NanoMiraiBlocks.REGISTRY.register(MOD_BUS)
        NanoMiraiItems.REGISTRY.register(MOD_BUS)
        NanoMiraiBlockEntities.REGISTRY.register(MOD_BUS)
        NanoMiraiMenu.REGISTRY.register(MOD_BUS)
        NanoMiraiArmorMaterials.REGISTRY.register(MOD_BUS)
        NanoMiraiAttachmentTypes.REGISTRY.register(MOD_BUS)
        NanoMiraiRecipeType.REGISTRY.register(MOD_BUS)
        NanoMiraiRecipeSerializer.REGISTRY.register(MOD_BUS)
        NanoMiraiCreativeTab.REGISTRY.register(MOD_BUS)
    }

    @SubscribeEvent
    fun registerPayload(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")
        registrar.playToClient(
            DeployedNanomachineData.TYPE,
            DeployedNanomachineData.STREAM_CODEC,
            ClientPayloadHandler::handleDataOnMain
        )
    }

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator = event.generator
        val output = generator.packOutput
        val lookupProvider = event.lookupProvider
        val existingFileHelper = event.existingFileHelper

        generator.addProvider(event.includeServer(), NanoMiraiRecipeProvider(output, lookupProvider))

        generator.addProvider(event.includeClient(), NanoMiraiBlockProvider(output, existingFileHelper))
        generator.addProvider(event.includeClient(), NanoMiraiItemModelProvider(output, existingFileHelper))
    }

    fun rl(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(ID, path)
    }
}
