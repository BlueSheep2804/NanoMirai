package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.block.LaserEngraverBlock
import dev.bluesheep.nanomirai.recipe.DualRecipeInput
import dev.bluesheep.nanomirai.recipe.laser.LaserRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import dev.bluesheep.nanomirai.util.InputSingleItemHandler
import dev.bluesheep.nanomirai.util.OutputItemHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.*

class LaserEngraverBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.LASER_ENGRAVER, pos, blockState) {
    companion object {
        const val SIZE = 3
        const val OUTPUT_SLOT = 0
        fun capabilityProvider(blockEntity: LaserEngraverBlockEntity, direction: Direction?): IItemHandler {
            return when (direction) {
                null -> blockEntity.itemHandler
                Direction.DOWN -> blockEntity.outputItemHandler
                Direction.UP -> blockEntity.inputItemHandler
                else -> blockEntity.lensItemHandler
            }
        }
    }
    val itemHandler = ItemStackHandler(SIZE)
    val inputItemHandler = InputSingleItemHandler(itemHandler, 1)
    val lensItemHandler = InputSingleItemHandler(itemHandler, 2)
    val outputItemHandler = OutputItemHandler(itemHandler, OUTPUT_SLOT)
    var progress = 0
    var maxProgress = 100
    val data: ContainerData = object : ContainerData {
        override fun get(index: Int): Int {
            return when (index) {
                0 -> progress
                1 -> maxProgress
                else -> 0
            }
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> progress = value
                1 -> maxProgress = value
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        itemHandler.deserializeNBT(registries, tag.getCompound("items"))
        progress = tag.getInt("progress")
        maxProgress = tag.getInt("maxProgress")
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.put("items", itemHandler.serializeNBT(registries))
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
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until inventory.containerSize) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        Containers.dropContents(level, worldPosition, inventory)
    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        if (hasRecipe()) {
            if (!state.getValue(LaserEngraverBlock.CRAFTING)) {
                level.setBlock(pos, state.setValue(LaserEngraverBlock.CRAFTING, true), 3)
            }
            increaseCraftingProgress()
            setChanged(level, pos, state)

            if (hasCraftingFinished()) {
                craftItem()
                resetProgress()
            }
        } else {
            if (state.getValue(LaserEngraverBlock.CRAFTING)) {
                level.setBlock(pos, state.setValue(LaserEngraverBlock.CRAFTING, false), 3)
            }
            resetProgress()
        }
    }

    private fun resetProgress() {
        progress = 0
        maxProgress = 100
    }

    private fun craftItem() {
        val recipe = getCurrentRecipe()
        if (recipe.isEmpty) return
        val result = recipe.get().value.result

        itemHandler.extractItem(1, 1, false)

        val outputStack = itemHandler.getStackInSlot(OUTPUT_SLOT)
        if (ItemStack.isSameItemSameComponents(outputStack, result)) {
            outputStack.count += result.count
        } else {
            itemHandler.setStackInSlot(
                OUTPUT_SLOT,
                result.copy()
            )
        }
    }

    private fun hasCraftingFinished(): Boolean {
        return progress >= maxProgress
    }

    private fun increaseCraftingProgress() {
        progress++
    }

    private fun hasRecipe(): Boolean {
        val recipe = getCurrentRecipe()
        if (recipe.isEmpty) return false
        val output = recipe.get().value.result

        return canInsertAmountIntoOutputSlot(output.count) && canInsertItemIntoOutputSlot(output)
    }

    private fun getCurrentRecipe(): Optional<RecipeHolder<LaserRecipe>> {
        if (level == null) return Optional.empty()

        return level!!.recipeManager.getRecipeFor(
            NanoMiraiRecipeType.LASER,
            DualRecipeInput(itemHandler.getStackInSlot(1), itemHandler.getStackInSlot(2)),
            level!!
        )
    }

    private fun canInsertItemIntoOutputSlot(output: ItemStack): Boolean {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty ||
                ItemStack.isSameItemSameComponents(itemHandler.getStackInSlot(OUTPUT_SLOT), output)
    }

    private fun canInsertAmountIntoOutputSlot(count: Int): Boolean {
        val maxCount = if (itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty) 64 else itemHandler.getStackInSlot(OUTPUT_SLOT).maxStackSize
        val currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).count

        return maxCount >= currentCount + count
    }
}
