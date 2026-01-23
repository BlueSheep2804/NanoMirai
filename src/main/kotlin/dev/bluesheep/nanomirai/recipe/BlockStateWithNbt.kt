package dev.bluesheep.nanomirai.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
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

        val EMPTY = noNbt(Blocks.AIR.defaultBlockState())

        fun noNbt(blockState: BlockState): BlockStateWithNbt {
            return BlockStateWithNbt(blockState, CompoundTag())
        }
    }

    val block: Block
        get() = blockState.block

    fun `is`(other: Block, otherNbt: CompoundTag): Boolean {
        return blockState.`is`(other) && NbtUtils.compareNbt(otherNbt, nbt, true)
    }
}