package dev.bluesheep.nanomirai

import dev.bluesheep.nanomirai.block.entity.AssemblerBlockEntity
import dev.bluesheep.nanomirai.block.entity.LaserEngraverBlockEntity
import dev.bluesheep.nanomirai.block.entity.SynthesizeDisplayBlockEntity
import dev.bluesheep.nanomirai.capabilities.EnergyStorageItem
import dev.bluesheep.nanomirai.data.*
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import dev.bluesheep.nanomirai.registry.*
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
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

        NanoMiraiDataComponents.REGISTRY.register(MOD_BUS)
        NanoMiraiBlocks.REGISTRY.register(MOD_BUS)
        NanoMiraiItems.REGISTRY.register(MOD_BUS)
        NanoMiraiBlockEntities.REGISTRY.register(MOD_BUS)
        NanoMiraiMenu.REGISTRY.register(MOD_BUS)
        NanoMiraiRecipeType.REGISTRY.register(MOD_BUS)
        NanoMiraiRecipeSerializer.REGISTRY.register(MOD_BUS)
        NanoMiraiCreativeTab.REGISTRY.register(MOD_BUS)

        LOADING_CONTEXT.activeContainer.registerConfig(ModConfig.Type.SERVER, NanoMiraiConfig.SPEC)
    }

    @SubscribeEvent
    fun setup(event: FMLCommonSetupEvent) {
        NanoTier.entries.forEach {
            DispenserBlock.registerBehavior(it.synthesizeNanoItem, SynthesizeNanoItem.DISPENSER_BEHAVIOR)
        }
    }

    @SubscribeEvent
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            NanoMiraiBlockEntities.NANOMACHINE_ASSEMBLER,
            AssemblerBlockEntity::capabilityProvider
        )
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            NanoMiraiBlockEntities.LASER_ENGRAVER,
            LaserEngraverBlockEntity::capabilityProvider
        )
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            NanoMiraiBlockEntities.SYNTHESIZE_DISPLAY,
            SynthesizeDisplayBlockEntity::capabilityProvider
        )

        event.registerItem(
            Capabilities.EnergyStorage.ITEM,
            { itemStack, _ ->
                EnergyStorageItem(itemStack)
            },
            NanoMiraiItems.SYNTHESIZE_NANO_MK1,
            NanoMiraiItems.SYNTHESIZE_NANO_MK2,
            NanoMiraiItems.SYNTHESIZE_NANO_MK3,
            NanoMiraiItems.SYNTHESIZE_NANO_MK4,
            NanoMiraiItems.NANO_SWARM_BLASTER_MK1,
            NanoMiraiItems.NANO_SWARM_BLASTER_MK2,
            NanoMiraiItems.NANO_SWARM_BLASTER_MK3,
            NanoMiraiItems.NANO_SWARM_BLASTER_MK4
        )
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
