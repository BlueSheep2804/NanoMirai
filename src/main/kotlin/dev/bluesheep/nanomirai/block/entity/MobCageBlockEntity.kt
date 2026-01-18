package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class MobCageBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.MOB_CAGE, pos, blockState) {
    var entity: Entity? = null

    override fun onLoad() {
        super.onLoad()
        entity = getEntityFromComponent()
        setChanged()
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(registries)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    private fun getEntityFromComponent(): Entity? {
        val entityData = components().getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY)
        if (entityData.isEmpty) return null
        return level?.let {
            EntityType.loadEntityRecursive(
                entityData.copyTag(),
                it,
                java.util.function.Function.identity()
            )
        }
    }
}