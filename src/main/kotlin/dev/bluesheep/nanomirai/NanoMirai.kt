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

        // Register the KDeferredRegister to the mod-specific event bus
        NanoMiraiBlocks.REGISTRY.register(MOD_BUS)
        NanoMiraiItems.REGISTRY.register(MOD_BUS)
        NanoMiraiBlockEntities.REGISTRY.register(MOD_BUS)
        NanoMiraiMenu.REGISTRY.register(MOD_BUS)
        NanoMiraiArmorMaterials.REGISTRY.register(MOD_BUS)
        NanoMiraiAttachmentTypes.REGISTRY.register(MOD_BUS)
        NanoMiraiRecipeType.REGISTRY.register(MOD_BUS)
        NanoMiraiRecipeSerializer.REGISTRY.register(MOD_BUS)

        Items.IRON_HELMET
        val obj = runForDist(clientTarget = {
            MOD_BUS.addListener(::onClientSetup)
            Minecraft.getInstance()
        }, serverTarget = {
            MOD_BUS.addListener(::onServerSetup)
            "test"
        })

        println(obj)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        LOGGER.log(Level.INFO, "Hello! This is working!")
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
