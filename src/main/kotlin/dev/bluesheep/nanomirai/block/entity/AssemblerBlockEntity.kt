package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler

class AssemblerBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.NANOMACHINE_ASSEBLER, pos, blockState) {
    companion object {
        const val SIZE = 10
    }
    val inputItem = ItemStackHandler(SIZE)

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        inputItem.deserializeNBT(registries, tag.getCompound("items"))
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.put("items", inputItem.serializeNBT(registries))
        super.saveAdditional(tag, registries)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(registries)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    fun drops() {
        val inventory = SimpleContainer(inputItem.slots)
        for (i in 0 until inventory.containerSize) {
            inventory.setItem(i, inputItem.getStackInSlot(i))
        }

        Containers.dropContents(level, worldPosition, inventory)
    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {

    }
}
