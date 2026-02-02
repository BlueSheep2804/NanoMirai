package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.item.DeprecatedItem
import dev.bluesheep.nanomirai.item.MobCageItem
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(NanoMirai.ID)

    @Deprecated("This item has been split into separate items for each tier.")
    val SYNTHESIZE_NANO: DeprecatedItem by REGISTRY.register("synthesize_nano") { ->
        DeprecatedItem(Item.Properties().durability(8), Component.translatable("nanomirai.tooltip.deprecated.synthesize_nano"))
    }
    val SYNTHESIZE_NANO_MK1: SynthesizeNanoItem by registerSynthesizeNano(NanoTier.MK1)
    val SYNTHESIZE_NANO_MK2: SynthesizeNanoItem by registerSynthesizeNano(NanoTier.MK2)
    val SYNTHESIZE_NANO_MK3: SynthesizeNanoItem by registerSynthesizeNano(NanoTier.MK3)
    val SYNTHESIZE_NANO_MK4: SynthesizeNanoItem by registerSynthesizeNano(NanoTier.MK4)

    @Deprecated("This item has been split into separate items for each tier.")
    val SUPPORT_NANO: DeprecatedItem by REGISTRY.register("support_nano") { ->
        DeprecatedItem(Item.Properties().stacksTo(1), Component.translatable("nanomirai.tooltip.deprecated.support_nano"))
    }
    val SUPPORT_NANO_MK1: SupportNanoItem by registerSupportNano(NanoTier.MK1)
    val SUPPORT_NANO_MK2: SupportNanoItem by registerSupportNano(NanoTier.MK2)
    val SUPPORT_NANO_MK3: SupportNanoItem by registerSupportNano(NanoTier.MK3)
    val SUPPORT_NANO_MK4: SupportNanoItem by registerSupportNano(NanoTier.MK4)

    @Deprecated("This item has been split into separate items for each tier.")
    val NANO_SWARM_BLASTER: DeprecatedItem by REGISTRY.register("nano_swarm_blaster") { ->
        DeprecatedItem(
            Item.Properties().stacksTo(1)
                .component(DataComponents.MAX_DAMAGE, 10)
                .component(DataComponents.DAMAGE, 0)
                .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY),
            Component.translatable("nanomirai.tooltip.deprecated.nano_swarm_blaster")
        )
    }
    val NANO_SWARM_BLASTER_MK1: NanoSwarmBlasterItem by registerNanoSwarmBlaster(NanoTier.MK1)
    val NANO_SWARM_BLASTER_MK2: NanoSwarmBlasterItem by registerNanoSwarmBlaster(NanoTier.MK2)
    val NANO_SWARM_BLASTER_MK3: NanoSwarmBlasterItem by registerNanoSwarmBlaster(NanoTier.MK3)
    val NANO_SWARM_BLASTER_MK4: NanoSwarmBlasterItem by registerNanoSwarmBlaster(NanoTier.MK4)

    val REPAIR_NANO: Item by REGISTRY.registerSimpleItem("repair_nano")

    val MOB_CAGE: Item by REGISTRY.register("mob_cage", ::MobCageItem)

    val GRAPHITE: Item by REGISTRY.registerSimpleItem("graphite")
    val SILICON: Item by REGISTRY.registerSimpleItem("silicon")
    val SILICON_WAFER: Item by REGISTRY.registerSimpleItem("silicon_wafer")
    val RAW_SCULMIUM: Item by REGISTRY.registerSimpleItem("raw_sculmium")
    val SCULMIUM_INGOT: Item by REGISTRY.registerSimpleItem("sculmium_ingot")
    val SIMPLE_CIRCUIT: Item by REGISTRY.registerSimpleItem("simple_circuit")
    val NORMAL_CIRCUIT: Item by REGISTRY.registerSimpleItem("normal_circuit")
    val NANO_CIRCUIT: Item by REGISTRY.registerSimpleItem("nano_circuit")
    val SCULMIUM_CIRCUIT: Item by REGISTRY.registerSimpleItem("sculmium_circuit")
    val AMETHYST_LENS: Item by catalyst("amethyst_lens")
    val SCULK_LENS: Item by catalyst("sculk_lens")
    val RED_RESEARCH_CATALYST: Item by catalyst("red_research_catalyst")
    val GREEN_RESEARCH_CATALYST: Item by catalyst("green_research_catalyst")
    val BLUE_RESEARCH_CATALYST: Item by catalyst("blue_research_catalyst")
    val CYAN_RESEARCH_CATALYST: Item by catalyst("cyan_research_catalyst")
    val MAGENTA_RESEARCH_CATALYST: Item by catalyst("magenta_research_catalyst")
    val YELLOW_RESEARCH_CATALYST: Item by catalyst("yellow_research_catalyst")

    val NANOMACHINE_ASSEMBLER: BlockItem by REGISTRY.registerSimpleBlockItem("nanomachine_assembler") { ->
        NanoMiraiBlocks.NANOMACHINE_ASSEMBLER
    }
    val LASER_ENGRAVER: BlockItem by REGISTRY.registerSimpleBlockItem("laser_engraver") { ->
        NanoMiraiBlocks.LASER_ENGRAVER
    }
    val NANO_LAB: BlockItem by REGISTRY.registerSimpleBlockItem("nano_lab") { ->
        NanoMiraiBlocks.NANO_LAB
    }

    val REINFORCED_OBSIDIAN: BlockItem by REGISTRY.registerSimpleBlockItem("reinforced_obsidian") { ->
        NanoMiraiBlocks.REINFORCED_OBSIDIAN
    }

    fun registerSynthesizeNano(tier: NanoTier): DeferredItem<SynthesizeNanoItem> = REGISTRY.register(
        "synthesize_nano_${tier.name.lowercase()}",
    ) { -> SynthesizeNanoItem(tier) }

    fun registerSupportNano(tier: NanoTier): DeferredItem<SupportNanoItem> = REGISTRY.register(
        "support_nano_${tier.name.lowercase()}",
    ) { -> SupportNanoItem(tier) }

    fun registerNanoSwarmBlaster(tier: NanoTier): DeferredItem<NanoSwarmBlasterItem> = REGISTRY.register(
        "nano_swarm_blaster_${tier.name.lowercase()}",
    ) { -> NanoSwarmBlasterItem(tier) }

    fun catalyst(id: String): DeferredItem<Item> = REGISTRY.registerSimpleItem(
        id,
        Item.Properties().stacksTo(1)
    )
}
