package dev.bluesheep.nanomirai.network

import dev.bluesheep.nanomirai.NanoMirai.LOGGER
import dev.bluesheep.nanomirai.registry.NanoMiraiAttachmentTypes.DEPLOYED_NANOMACHINES
import net.minecraft.client.Minecraft
import net.neoforged.neoforge.network.handling.IPayloadContext

class ClientPayloadHandler {
    companion object {
        fun handleDataOnMain(data: DeployedNanomachineData, context: IPayloadContext) {
            val player = Minecraft.getInstance().player
            if (player == null) return
            val deployed = player.getData(DEPLOYED_NANOMACHINES).toMutableList()
            LOGGER.debug("[Client] ${deployed[data.tier]}")
            deployed[data.tier] = data.count
            player.setData(DEPLOYED_NANOMACHINES, deployed)
            LOGGER.debug("[Client] ${data.tier}, ${data.count}")
        }
    }
}
