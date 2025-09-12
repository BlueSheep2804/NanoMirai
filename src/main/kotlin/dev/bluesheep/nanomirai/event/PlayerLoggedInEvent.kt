package dev.bluesheep.nanomirai.event

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.network.PlayerSwarmData
import dev.bluesheep.nanomirai.registry.NanoMiraiAttachmentTypes
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.PacketDistributor

@EventBusSubscriber(modid = NanoMirai.ID)
object PlayerLoggedInEvent {
    @SubscribeEvent
    fun onServerPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity
        if (player is ServerPlayer) {
            val swarm = player.getData(NanoMiraiAttachmentTypes.SWARM)
            intArrayOf(0, 1, 2).forEach { PacketDistributor.sendToPlayer(player, PlayerSwarmData(it, swarm[it])) }
        }
    }
}
