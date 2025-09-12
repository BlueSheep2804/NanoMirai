package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.NanoMirai.LOGGER
import dev.bluesheep.nanomirai.network.PlayerSwarmData
import dev.bluesheep.nanomirai.registry.NanoMiraiAttachmentTypes.SWARM
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.network.PacketDistributor

class NanoSwarmItem(val tier: Int, properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val itemStack = player.getItemInHand(usedHand)
        itemStack.consume(1, player)
        if (!level.isClientSide) {
            val swarm = player.getData(SWARM).toMutableList()
            swarm[tier] += 1
            player.setData(SWARM, swarm)
            if (player is ServerPlayer) {
                PacketDistributor.sendToPlayer(player, PlayerSwarmData(tier, swarm[tier]))
            }
            LOGGER.debug("[Server] ${swarm[tier]}")
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide)
    }
}
