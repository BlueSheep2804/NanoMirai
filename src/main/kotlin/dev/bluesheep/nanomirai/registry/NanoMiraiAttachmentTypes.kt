package dev.bluesheep.nanomirai.registry

import com.mojang.serialization.Codec
import dev.bluesheep.nanomirai.NanoMirai
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiAttachmentTypes {
    val REGISTRY: DeferredRegister<AttachmentType<*>> = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, NanoMirai.ID)

    val DEPLOYED_NANOMACHINES: AttachmentType<List<Int>> by REGISTRY.register("deployed_nanomachines") { ->
        AttachmentType.builder { -> listOf(0, 0, 0, 0) }.serialize(Codec.list(Codec.INT, 4, 4)).build()
    }
}
