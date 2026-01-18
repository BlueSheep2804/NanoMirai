package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.registry.NanoMiraiBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

class NanoMiraiBlockLootSubProvider(lookupProvider: HolderLookup.Provider) : BlockLootSubProvider(
    emptySet(),
    FeatureFlags.DEFAULT_FLAGS,
    lookupProvider
) {
    override fun getKnownBlocks(): Iterable<Block> {
        return NanoMiraiBlocks.REGISTRY.entries.map {
            it.value() as Block
        }.toList()
    }

    override fun generate() {
        dropSelf(NanoMiraiBlocks.NANOMACHINE_ASSEMBLER)
        dropSelf(NanoMiraiBlocks.LASER_ENGRAVER)
        dropSelf(NanoMiraiBlocks.NANO_LAB)
        dropSelf(NanoMiraiBlocks.REINFORCED_OBSIDIAN)

        add(
            NanoMiraiBlocks.MOB_CAGE,
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .add(
                        LootItem.lootTableItem(NanoMiraiBlocks.MOB_CAGE)
                            .apply(
                                CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                    .include(DataComponents.ENTITY_DATA)
                            )
                    )
                    .setRolls(ConstantValue(1.0f))
                    .setBonusRolls(ConstantValue(0.0f))
                )
        )
    }
}