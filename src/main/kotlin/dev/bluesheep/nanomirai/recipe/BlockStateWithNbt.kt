package dev.bluesheep.nanomirai.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.bluesheep.nanomirai.NanoMirai
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class BlockStateWithNbt(val blockState: BlockState, val nbt: CompoundTag) {
    companion object {
        val CODEC: MapCodec<BlockStateWithNbt> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                BlockState.CODEC.fieldOf("block").forGetter(BlockStateWithNbt::blockState),
                CompoundTag.CODEC.fieldOf("nbt").forGetter(BlockStateWithNbt::nbt)
            ).apply(inst, ::BlockStateWithNbt)
        }
        val DATA_COMPONENTS_CODEC: Codec<DataComponentMap> = DataComponentMap.CODEC.optionalFieldOf("components", DataComponentMap.EMPTY).codec()

        val EMPTY = noNbt(Blocks.AIR.defaultBlockState())

        fun noNbt(blockState: BlockState): BlockStateWithNbt {
            return BlockStateWithNbt(blockState, CompoundTag())
        }
    }

    val block: Block = blockState.block

    fun getItemStack(registries: HolderLookup.Provider): ItemStack{
        val stack = ItemStack(block)
        if (!nbt.isEmpty) {
            DATA_COMPONENTS_CODEC
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), nbt)
                .resultOrPartial {
                    NanoMirai.LOGGER.warn("Failed to load components: $it")
                }
                .ifPresent(stack::applyComponents)
        }
        return stack
    }

    fun `is`(other: Block, otherNbt: CompoundTag): Boolean {
        return blockState.`is`(other) && NbtUtils.compareNbt(otherNbt, nbt, true)
    }
}