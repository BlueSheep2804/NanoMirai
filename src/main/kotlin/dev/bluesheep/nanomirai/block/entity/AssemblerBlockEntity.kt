package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.recipe.assembler.AssemblerRecipe
import dev.bluesheep.nanomirai.recipe.MultipleItemRecipeInput
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import net.minecraft.core.BlockPos
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
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.Optional

class AssemblerBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.NANOMACHINE_ASSEMBLER, pos, blockState) {
    companion object {
        const val SIZE = 10
        const val OUTPUT_SLOT = 0
    }
    val itemHandler = ItemStackHandler(SIZE)
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
            increaseCraftingProgress()
            setChanged(level, pos, state)

            if (hasCraftingFinished()) {
                craftItem()
                resetProgress()
            }
        } else {
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

        recipe.get().value.inputItems.forEach { stackedIngredient ->
            var toRemove = stackedIngredient.count
            for (i in 1 until SIZE) {
                val stackInSlot = itemHandler.getStackInSlot(i)
                if (stackedIngredient.ingredient.test(stackInSlot) && toRemove > 0) {
                    val extracted = itemHandler.extractItem(i, toRemove, false)
                    toRemove -= extracted.count
                    if (toRemove <= 0) break
                }
            }
        }

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

    private fun getCurrentRecipe(): Optional<RecipeHolder<AssemblerRecipe>> {
        if (level == null) return Optional.empty()

        return level!!.recipeManager.getRecipeFor(
            NanoMiraiRecipeType.ASSEMBLER,
            MultipleItemRecipeInput(inputList()),
            level!!
        )
    }

    private fun inputList(): List<ItemStack> {
        val list = mutableListOf<ItemStack>()
        for (i in 1 until SIZE) {
            val stack = itemHandler.getStackInSlot(i)
            if (stack.isEmpty)
                continue

            val existsStack = list.find { stack.`is`(it.item) }
            if (existsStack != null) {
                existsStack.count += stack.count
                continue
            }
            list.add(itemHandler.getStackInSlot(i).copy())
        }
        return list
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
