package dev.bluesheep.nanomirai.block.entity

import dev.bluesheep.nanomirai.registry.NanoMiraiBlockEntities
import dev.bluesheep.nanomirai.util.MobCageUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.component.PatchedDataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class MobCageBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(NanoMiraiBlockEntities.MOB_CAGE, pos, blockState) {
    var capturedEntity: Entity? = null

    override fun onLoad() {
        super.onLoad()
        updateCapturedEntity()
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(registries)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    fun entityInside(entity: Entity) {
        if (capturedEntity != null) return
        val patchedComponent = PatchedDataComponentMap(components())
        patchedComponent.set(
            DataComponents.ENTITY_DATA,
            MobCageUtil.captureEntity(entity) ?: return
        )
        applyComponents(patchedComponent, patchedComponent.asPatch())
        setChanged()

        updateCapturedEntity()
        level?.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS)
    }

    override fun onDataPacket(
        net: Connection,
        pkt: ClientboundBlockEntityDataPacket,
        lookupProvider: HolderLookup.Provider
    ) {
        super.onDataPacket(net, pkt, lookupProvider)
        updateCapturedEntity()
    }

    private fun updateCapturedEntity() {
        capturedEntity = MobCageUtil.getEntityFromComponent(level, components().get(DataComponents.ENTITY_DATA))
    }
}