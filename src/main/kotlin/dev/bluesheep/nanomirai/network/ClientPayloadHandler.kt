package dev.bluesheep.nanomirai.network

import dev.bluesheep.nanomirai.NanoMirai.LOGGER
import dev.bluesheep.nanomirai.registry.NanoMiraiAttachmentTypes.SWARM
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.network.handling.IPayloadContext

class ClientPayloadHandler {
    companion object {
        fun handleDataOnMain(data: PlayerSwarmData, context: IPayloadContext) {
            val player = Minecraft.getInstance().player
            if (player == null) return
            val swarm = player.getData(SWARM).toMutableList()
            LOGGER.debug("[Client] ${swarm[data.tier]}")
            swarm[data.tier] = data.count
            player.setData(SWARM, swarm)
            LOGGER.debug("[Client] ${data.tier}, ${data.count}")
        }
    }
}
