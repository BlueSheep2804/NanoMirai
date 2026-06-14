package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.item.DeprecatedItem
import dev.bluesheep.nanomirai.item.MobCageItem
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.StandingAndWallBlockItem
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import kotlin.reflect.KProperty0

object NanoMiraiItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(NanoMirai.ID)

    @Deprecated("This item has been split into separate items for each tier.")
    val SYNTHESIZE_NANO: DeprecatedItem by REGISTRY.register("synthesize_nano") { ->
        DeprecatedItem(Item.Properties().durability(8), Component.translatable("nanomirai.tooltip.deprecated.synthesize_nano"))
    }
    val SYNTHESIZE_NANO_NORMAL: SynthesizeNanoItem by registerSynthesizeNano(NanoTier.NORMAL)
    val SYNTHESIZE_NANO_IMPROVED: SynthesizeNanoItem by registerSynthesizeNano(NanoTier.IMPROVED)

    @Deprecated("This item has been split into separate items for each tier.")
    val SUPPORT_NANO: DeprecatedItem by REGISTRY.register("support_nano") { ->
        DeprecatedItem(Item.Properties().stacksTo(1), Component.translatable("nanomirai.tooltip.deprecated.support_nano"))
    }
    val SUPPORT_NANO_NORMAL: SupportNanoItem by registerSupportNano(NanoTier.NORMAL)
    val SUPPORT_NANO_IMPROVED: SupportNanoItem by registerSupportNano(NanoTier.IMPROVED)

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
    val NANO_SWARM_BLASTER_NORMAL: NanoSwarmBlasterItem by registerNanoSwarmBlaster(NanoTier.NORMAL)
    val NANO_SWARM_BLASTER_IMPROVED: NanoSwarmBlasterItem by registerNanoSwarmBlaster(NanoTier.IMPROVED)

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
    val LENS: Item by catalyst("lens")
    val AMETHYST_LENS: Item by catalyst("amethyst_lens")
    val SCULK_LENS: Item by catalyst("sculk_lens")
    val RED_RESEARCH_CATALYST: Item by catalyst("red_research_catalyst")
    val GREEN_RESEARCH_CATALYST: Item by catalyst("green_research_catalyst")
    val BLUE_RESEARCH_CATALYST: Item by catalyst("blue_research_catalyst")
    val CYAN_RESEARCH_CATALYST: Item by catalyst("cyan_research_catalyst")
    val MAGENTA_RESEARCH_CATALYST: Item by catalyst("magenta_research_catalyst")
    val YELLOW_RESEARCH_CATALYST: Item by catalyst("yellow_research_catalyst")
    val LASER_COMPONENT: Item by REGISTRY.registerSimpleItem("laser_component")

    val NANOMACHINE_ASSEMBLER: BlockItem by registerSimpleBlockItem(NanoMiraiBlocks::NANOMACHINE_ASSEMBLER)
    val LASER_ENGRAVER: BlockItem by registerSimpleBlockItem(NanoMiraiBlocks::LASER_ENGRAVER)
    val NANO_LAB: BlockItem by registerSimpleBlockItem(NanoMiraiBlocks::NANO_LAB)
    val SOLAR_PANEL: BlockItem by registerSimpleBlockItem(NanoMiraiBlocks::SOLAR_PANEL)

    val REINFORCED_OBSIDIAN: BlockItem by registerSimpleBlockItem(NanoMiraiBlocks::REINFORCED_OBSIDIAN)
    val ALLAY_HEAD: BlockItem by REGISTRY.registerItem(
        "allay_head",
    ) {
        StandingAndWallBlockItem(
            NanoMiraiBlocks.ALLAY_HEAD,
            NanoMiraiBlocks.ALLAY_WALL_HEAD,
            Item.Properties(),
            Direction.DOWN
        )
    }
    val RAW_SCULMIUM_BLOCK: BlockItem by registerSimpleBlockItem(NanoMiraiBlocks::RAW_SCULMIUM_BLOCK)
    val SCULMIUM_BLOCK: BlockItem by registerSimpleBlockItem(NanoMiraiBlocks::SCULMIUM_BLOCK)

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

    fun registerSimpleBlockItem(block: KProperty0<Block>) = REGISTRY.registerSimpleBlockItem(
        block.name.lowercase()
    ) { block.get() }
}
