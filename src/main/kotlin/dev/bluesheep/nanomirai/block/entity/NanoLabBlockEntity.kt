package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.recipe.lab.NanoLabRecipeInput
import dev.bluesheep.nanomirai.recipe.lab.attribute.LabAttributeRecipe
import dev.bluesheep.nanomirai.recipe.lab.effect.LabEffectRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
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
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.SlotContext
import java.util.*

class NanoLabBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.NANO_LAB, pos, blockState) {
    companion object {
        const val SIZE = 8
        const val OUTPUT_SLOT = 0
        const val CATALYST_SLOT = 1
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
        if (hasRecipe() && isCraftable()) {
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
        val attributeRecipe = getCurrentAttributeRecipe()
        if (!attributeRecipe.isEmpty) {
            val attribute = attributeRecipe.get().value.attribute
            val modifier = attributeRecipe.get().value.modifier

            attributeRecipe.get().value.items.forEach { ingredient ->
                var toRemove = 1
                for (i in 2 until SIZE) {
                    val stackInSlot = itemHandler.getStackInSlot(i)
                    if (ingredient.test(stackInSlot)) {
                        val extracted = itemHandler.extractItem(i, toRemove, false)
                        toRemove -= extracted.count
                        if (toRemove <= 0) break
                    }
                }
            }
            SupportNanoItem.setAttributes(
                itemHandler.getStackInSlot(OUTPUT_SLOT),
                attribute,
                modifier
            )
        }

        val effectRecipe = getCurrentEffectRecipe()
        if (!effectRecipe.isEmpty) {
            effectRecipe.get().value.items.forEach { ingredient ->
                var toRemove = 1
                for (i in 2 until SIZE) {
                    val stackInSlot = itemHandler.getStackInSlot(i)
                    if (ingredient.test(stackInSlot)) {
                        val extracted = itemHandler.extractItem(i, toRemove, false)
                        toRemove -= extracted.count
                        if (toRemove <= 0) break
                    }
                }
            }

            NanoSwarmBlasterItem.addEffect(itemHandler.getStackInSlot(OUTPUT_SLOT), effectRecipe.get().value.mobEffectInstance)
        }
    }

    private fun hasCraftingFinished(): Boolean {
        return progress >= maxProgress
    }

    private fun increaseCraftingProgress() {
        progress++
    }

    private fun hasRecipe(): Boolean {
        val attributeRecipe = getCurrentAttributeRecipe()
        val effectRecipe = getCurrentEffectRecipe()

        return (
                (attributeRecipe.isPresent && itemHandler.getStackInSlot(OUTPUT_SLOT).`is`(NanoMiraiItems.SUPPORT_NANO)) ||
                (effectRecipe.isPresent && itemHandler.getStackInSlot(OUTPUT_SLOT).`is`(NanoMiraiItems.NANO_SWARM_BLASTER))
        )
    }

    private fun isCraftable(): Boolean {
        val stack = itemHandler.getStackInSlot(OUTPUT_SLOT)
        when (stack.item) {
            is SupportNanoItem -> {
                val tier = NanoTier.fromRarity(stack.rarity)
                val currentAttributeSize = CuriosApi.getAttributeModifiers(
                    SlotContext("support_nano", null, 0, false, true),
                    rl("support_nano"),
                    stack
                )?.size() ?: 0
                return currentAttributeSize < tier.maxAttributes
            }
            is NanoSwarmBlasterItem -> {
                val tier = NanoTier.fromRarity(stack.rarity)
                val currentEffectSize = stack.get(DataComponents.POTION_CONTENTS)?.customEffects?.size ?: 0
                return currentEffectSize < tier.maxEffects
            }
            else -> return false
        }
    }

    private fun getCurrentAttributeRecipe(): Optional<RecipeHolder<LabAttributeRecipe>> {
        if (level == null) return Optional.empty()

        return level!!.recipeManager.getRecipeFor(
            NanoMiraiRecipeType.LAB_ATTRIBUTE,
            NanoLabRecipeInput(
                itemHandler.getStackInSlot(OUTPUT_SLOT),
                itemHandler.getStackInSlot(CATALYST_SLOT),
                inputList()
            ),
            level!!
        )
    }

    private fun getCurrentEffectRecipe(): Optional<RecipeHolder<LabEffectRecipe>> {
        if (level == null) return Optional.empty()

        return level!!.recipeManager.getRecipeFor(
            NanoMiraiRecipeType.LAB_EFFECT,
            NanoLabRecipeInput(
                itemHandler.getStackInSlot(OUTPUT_SLOT),
                itemHandler.getStackInSlot(CATALYST_SLOT),
                inputList()
            ),
            level!!
        )
    }

    private fun inputList(): List<ItemStack> {
        val list = mutableListOf<ItemStack>()
        for (i in 2 until SIZE) {
            val stack = itemHandler.getStackInSlot(i)
            if (stack.isEmpty)
                continue
            list.add(stack)
        }
        return list
    }
}