package dev.bluesheep.nanomirai.network

import dev.bluesheep.nanomirai.NanoMirai.rl
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class PlayerSwarmData(val tier: Int, val count: Int) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<PlayerSwarmData>(rl("player_swarm"))

        val STREAM_CODEC: StreamCodec<ByteBuf, PlayerSwarmData> = StreamCodec.composite(
            ByteBufCodecs.INT,
            PlayerSwarmData::tier,
            ByteBufCodecs.INT,
            PlayerSwarmData::count,
            ::PlayerSwarmData
        )
    }
}
