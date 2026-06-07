package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.block.SynthesizeDisplayBlock
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.item.SynthesizeNanoItem
import dev.bluesheep.nanomirai.recipe.BlockStateWithNbt
import dev.bluesheep.nanomirai.recipe.BlockWithPairItemInput
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import dev.bluesheep.nanomirai.util.InputSingleItemHandler
import dev.bluesheep.nanomirai.util.NanoTier
import dev.bluesheep.nanomirai.util.SynthesizeState
import dev.bluesheep.nanomirai.util.SynthesizeUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.*

class SynthesizeDisplayBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.SYNTHESIZE_DISPLAY, pos, blockState) {
    companion object {
        fun capabilityProvider(blockEntity: SynthesizeDisplayBlockEntity, direction: Direction?): IItemHandler? {
            return when (direction) {
                null -> null
                else -> blockEntity.inputItemHandler
            }
        }
    }
    val itemHandler = ItemStackHandler(2)
    val inputItemHandler = object : InputSingleItemHandler(itemHandler, 1) {
        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (itemHandler.getStackInSlot(slotId).isEmpty) {
                val newStack = stack.copy()
                setSecondaryItem(newStack.split(1))
                return newStack
            }
            return stack
        }

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
            return ItemStack.EMPTY
        }
    }
    val inputSynthesizeNano: ItemStack
        get() = itemHandler.getStackInSlot(0)
    val inputCatalyst: ItemStack
        get() = itemHandler.getStackInSlot(1)
    var block: BlockStateWithNbt = BlockStateWithNbt.EMPTY
    var progress = 0
    var maxProgress = 100

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        itemHandler.deserializeNBT(registries, tag.getCompound("items"))
        BlockStateWithNbt.CODEC.codec().parse(NbtOps.INSTANCE, tag.getCompound("block")).result().ifPresent { state ->
            block = state
        }
        progress = tag.getInt("progress")
        maxProgress = tag.getInt("maxProgress")
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.put("items", itemHandler.serializeNBT(registries))
        BlockStateWithNbt.CODEC.codec().encodeStart(NbtOps.INSTANCE, block).result().ifPresent {
            tag.put("block", it)
        }
        tag.putInt("progress", progress)
        tag.putInt("maxProgress", maxProgress)

        super.saveAdditional(tag, registries)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(registries)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    fun drops() {
        val inventory = SimpleContainer(1)
        inventory.setItem(0, inputCatalyst)

        Containers.dropContents(level, worldPosition.above(), inventory)
    }

    fun setInputBlock(state: BlockState, tag: CompoundTag) {
        block = BlockStateWithNbt(state, tag)
        setChanged()
    }

    fun setPrimaryItem(stack: ItemStack) {
        itemHandler.setStackInSlot(0, stack)
        setChanged()
    }

    fun setSecondaryItem(stack: ItemStack) {
        itemHandler.setStackInSlot(1, stack)
        startCrafting()
        setChanged()
    }

    fun getRecipeResult(): ItemStack {
        val recipe = getCurrentRecipe()
        return if (recipe.isPresent) {
            recipe.get().value.result
        } else {
            ItemStack.EMPTY
        }
    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        if (!inputCatalyst.isEmpty && hasRecipe()) {
            increaseCraftingProgress()
            spawnParticles(level, pos)
            setChanged(level, pos, state)

            if (hasCraftingFinished()) {
                craftItem(level)
            }
        }
    }

    private fun spawnParticles(level: Level, pos: BlockPos) {
        if (level !is ServerLevel) return
        val center = pos.center
        level.sendParticles(
            ParticleTypes.ENCHANTED_HIT,
            center.x,
            center.y,
            center.z,
            1,
            0.3,
            0.3,
            0.3,
            0.2
        )
        if (progress % 5 != 0) return
        level.sendParticles(
            ItemParticleOption(
                ParticleTypes.ITEM,
                inputCatalyst
            ),
            center.x,
            center.y,
            center.z,
            5,
            0.3,
            0.3,
            0.3,
            0.1
        )
    }

    private fun startCrafting() {
        val recipe = getCurrentRecipe()
        var state = SynthesizeState.INVALID
        if (recipe.isPresent) {
            val tier = NanoTier.fromItem(inputSynthesizeNano.item)
            tier?.let {
                maxProgress = (recipe.get().value.duration / it.processingSpeedMultiplier).toInt()
                state = SynthesizeState.CRAFTING
            }
        }
        level!!.setBlockAndUpdate(blockPos, blockState.setValue(SynthesizeDisplayBlock.STATE, state))
        setChanged()
    }

    private fun craftItem(level: Level) {
        val result = getRecipeResult().copy()

        if (result.item is BlockItem) {
            level.setBlockAndUpdate(worldPosition, (result.item as BlockItem).block.defaultBlockState())
        } else {
            val inventory = SimpleContainer(1)
            val isSynthesizeNano = SynthesizeUtil.isEqualInputAndOutput(SynthesizeNanoItem::class, inputSynthesizeNano, result)
            val isSupportNano = SynthesizeUtil.isEqualInputAndOutput(SupportNanoItem::class, inputCatalyst, result)
            val isNanoSwarmBlaster = SynthesizeUtil.isEqualInputAndOutput(NanoSwarmBlasterItem::class, inputCatalyst, result)

            if (isSynthesizeNano) {
                result.applyComponents(inputSynthesizeNano.componentsPatch)
            } else if (isSupportNano || isNanoSwarmBlaster) {
                result.applyComponents(inputCatalyst.componentsPatch)
            }
            inventory.setItem(0, result)

            Containers.dropContents(level, worldPosition, inventory)
            level.removeBlock(worldPosition, false)
        }
    }

    fun hasCraftingFinished(): Boolean {
        return progress >= maxProgress
    }

    private fun increaseCraftingProgress() {
        progress++
    }

    private fun hasRecipe(): Boolean {
        return getCurrentRecipe().isPresent
    }

    private fun getCurrentRecipe(): Optional<RecipeHolder<SynthesizeRecipe>> {
        if (level == null) return Optional.empty()

        return level!!.recipeManager.getRecipeFor(
            NanoMiraiRecipeType.SYNTHESIZE,
            BlockWithPairItemInput(
                block,
                inputSynthesizeNano,
                inputCatalyst
            ),
            level!!
        )
    }
}
