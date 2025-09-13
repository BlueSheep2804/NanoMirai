package dev.bluesheep.nanomirai.network

import dev.bluesheep.nanomirai.NanoMirai.rl
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class DeployedNanomachineData(val tier: Int, val count: Int) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<DeployedNanomachineData>(rl("deployed_nanomachine_data"))

        val STREAM_CODEC: StreamCodec<ByteBuf, DeployedNanomachineData> = StreamCodec.composite(
            ByteBufCodecs.INT,
            DeployedNanomachineData::tier,
            ByteBufCodecs.INT,
            DeployedNanomachineData::count,
            ::DeployedNanomachineData
        )
    }
}
