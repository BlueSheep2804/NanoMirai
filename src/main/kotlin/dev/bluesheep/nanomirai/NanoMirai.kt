package dev.bluesheep.nanomirai

import dev.bluesheep.nanomirai.data.NanoMiraiBlockProvider
import dev.bluesheep.nanomirai.data.NanoMiraiBlockTagsProvider
import dev.bluesheep.nanomirai.data.NanoMiraiCuriosProvider
import dev.bluesheep.nanomirai.data.NanoMiraiItemModelProvider
import dev.bluesheep.nanomirai.data.NanoMiraiItemTagsProvider
import dev.bluesheep.nanomirai.data.NanoMiraiLootTableProvider
import dev.bluesheep.nanomirai.data.NanoMiraiRecipeProvider
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import dev.bluesheep.nanomirai.registry.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

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
        NanoMiraiEntities.REGISTRY.register(MOD_BUS)
        NanoMiraiRecipeType.REGISTRY.register(MOD_BUS)
        NanoMiraiRecipeSerializer.REGISTRY.register(MOD_BUS)
        NanoMiraiCreativeTab.REGISTRY.register(MOD_BUS)
    }

    @SubscribeEvent
    fun setup(event: FMLCommonSetupEvent) {
        LOGGER.info("Hello from server setup")
        DispenserBlock.registerBehavior(NanoMiraiItems.SYNTHESIZE_NANO, SynthesizeNanoItem.DispenserBehavior())
    }

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator = event.generator
        val output = generator.packOutput
        val lookupProvider = event.lookupProvider
        val existingFileHelper = event.existingFileHelper

        generator.addProvider(event.includeServer(), NanoMiraiRecipeProvider(output, lookupProvider))
        val blockTags = NanoMiraiBlockTagsProvider(output, lookupProvider, existingFileHelper)
        generator.addProvider(event.includeServer(), blockTags)
        generator.addProvider(event.includeServer(), NanoMiraiItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper))
        generator.addProvider(event.includeServer(), NanoMiraiCuriosProvider(output, existingFileHelper, lookupProvider))
        generator.addProvider(event.includeServer(), NanoMiraiLootTableProvider(output, lookupProvider))

        generator.addProvider(event.includeClient(), NanoMiraiBlockProvider(output, existingFileHelper))
        generator.addProvider(event.includeClient(), NanoMiraiItemModelProvider(output, existingFileHelper))
    }

    fun rl(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(ID, path)
    }
}
