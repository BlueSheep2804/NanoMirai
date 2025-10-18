package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.block.SynthesizeDisplayBlock
import dev.bluesheep.nanomirai.recipe.BlockWithPairItemInput
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import dev.bluesheep.nanomirai.util.NanoTier
import dev.bluesheep.nanomirai.util.SynthesizeState
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
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
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.*
import kotlin.math.floor

class SynthesizeDisplayBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.SYNTHESIZE_DISPLAY, pos, blockState), WorldlyContainer {
    val itemHandler = ItemStackHandler(2)
    var block: BlockState = Blocks.AIR.defaultBlockState()
    var progress = 0
    var maxProgress = 100

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        itemHandler.deserializeNBT(registries, tag.getCompound("items"))
        BlockState.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("block")).result().ifPresent { state ->
            block = state
        }
        progress = tag.getInt("progress")
        maxProgress = tag.getInt("maxProgress")
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.put("items", itemHandler.serializeNBT(registries))
        BlockState.CODEC.encodeStart(NbtOps.INSTANCE, block).result().ifPresent {
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
        inventory.setItem(0, itemHandler.getStackInSlot(1))

        Containers.dropContents(level, worldPosition.above(), inventory)
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

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        if (!itemHandler.getStackInSlot(1).isEmpty && hasRecipe()) {
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
                itemHandler.getStackInSlot(1)
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
            state = SynthesizeState.CRAFTING
            val tier = NanoTier.fromRarity(itemHandler.getStackInSlot(0).get(DataComponents.RARITY) ?: Rarity.COMMON)
            maxProgress = (recipe.get().value.duration / tier.processingSpeedMultiplier).toInt()
        }
        level!!.setBlockAndUpdate(blockPos, blockState.setValue(SynthesizeDisplayBlock.STATE, state))
        setChanged()
    }

    private fun craftItem(level: Level) {
        val recipe = getCurrentRecipe()
        val output = recipe.get().value.result.copy()
        val outputItem = output.item

        if (outputItem is BlockItem) {
            level.setBlockAndUpdate(worldPosition, outputItem.block.defaultBlockState())
        } else {
            val inventory = SimpleContainer(1)
            inventory.setItem(0, output)
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
                itemHandler.getStackInSlot(0),
                itemHandler.getStackInSlot(1)
            ),
            level!!
        )
    }

    override fun getSlotsForFace(p0: Direction): IntArray {
        return intArrayOf(1)
    }

    override fun canPlaceItemThroughFace(
        p0: Int,
        p1: ItemStack,
        p2: Direction?
    ): Boolean {
        return itemHandler.getStackInSlot(1).isEmpty
    }

    override fun canTakeItemThroughFace(
        p0: Int,
        p1: ItemStack,
        p2: Direction
    ): Boolean {
        return false
    }

    override fun getContainerSize(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return itemHandler.getStackInSlot(1).isEmpty
    }

    override fun getItem(p0: Int): ItemStack {
        return itemHandler.getStackInSlot(1)
    }

    override fun removeItem(p0: Int, p1: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun removeItemNoUpdate(p0: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun setItem(p0: Int, stack: ItemStack) {
        setSecondaryItem(stack)
    }

    override fun stillValid(p0: Player): Boolean {
        return false
    }

    override fun clearContent() {}
}
