package dev.bluesheep.nanomirai.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.level.block.state.BlockState
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class BlockPredicate(
    val blockStates: Optional<StatePropertiesPredicate>,
    val nbt: CompoundTag
) {
    companion object {
        val CODEC: MapCodec<BlockPredicate> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                StatePropertiesPredicate.CODEC
                    .optionalFieldOf("blockStates")
                    .forGetter(BlockPredicate::blockStates),
                CompoundTag.CODEC
                    .optionalFieldOf("nbt", CompoundTag())
                    .forGetter(BlockPredicate::nbt)
            ).apply(inst, ::BlockPredicate)
        }

        val EMPTY = create()

        fun create(blockStates: StatePropertiesPredicate? = null, nbt: CompoundTag = CompoundTag()): BlockPredicate {
            val state = if (blockStates == null) {
                Optional.empty()
            } else {
                Optional.of(blockStates)
            }
            return BlockPredicate(state, nbt)
        }
    }

    val isEmpty: Boolean
        get() = nbt.isEmpty && blockStates.isEmpty

    fun test(state: BlockState, otherNbt: CompoundTag): Boolean {
        return (blockStates.getOrNull()?.matches(state) ?: true) && NbtUtils.compareNbt(nbt, otherNbt, true)
    }
}
