package dev.bluesheep.nanomirai.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.bluesheep.nanomirai.NanoMirai
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class BlockInput(val block: Block, val predicate: BlockPredicate) {
    companion object {
        val CODEC: MapCodec<BlockInput> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(BlockInput::block),
                BlockPredicate.CODEC.codec().optionalFieldOf("predicate", BlockPredicate.EMPTY).forGetter(BlockInput::predicate)
            ).apply(inst, ::BlockInput)
        }
        val DATA_COMPONENTS_CODEC: Codec<DataComponentMap> = DataComponentMap.CODEC.optionalFieldOf("components", DataComponentMap.EMPTY).codec()

        val EMPTY = noNbt(Blocks.AIR)

        fun noNbt(block: Block): BlockInput {
            return BlockInput(block, BlockPredicate.EMPTY)
        }

        fun withNbt(block: Block, nbt: CompoundTag): BlockInput {
            return BlockInput(
                block,
                BlockPredicate.create(nbt = nbt)
            )
        }
    }

    fun getItemStack(registries: HolderLookup.Provider): ItemStack {
        val stack = ItemStack(block)
        if (!predicate.nbt.isEmpty) {
            DATA_COMPONENTS_CODEC
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), predicate.nbt)
                .resultOrPartial {
                    NanoMirai.LOGGER.warn("Failed to load components: $it")
                }
                .ifPresent(stack::applyComponents)
        }
        return stack
    }

    fun test(other: BlockState, otherNbt: CompoundTag): Boolean {
        return block == other.block && predicate.test(other, otherNbt)
    }
}