package dev.bluesheep.nanomirai.compat.jade

import dev.bluesheep.nanomirai.block.SynthesizeDisplayBlock
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin
import snownee.jade.api.WailaPlugin

@WailaPlugin
class JadeCompat : IWailaPlugin {
    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockIcon(
            SynthesizeDisplayComponentProvider.INSTANCE,
            SynthesizeDisplayBlock::class.java
        )
        registration.registerBlockComponent(
            SynthesizeDisplayComponentProvider.INSTANCE,
            SynthesizeDisplayBlock::class.java
        )
    }

    override fun register(registration: IWailaCommonRegistration) {
        registration.registerBlockDataProvider(
            SynthesizeDisplayComponentProvider.INSTANCE,
            SynthesizeDisplayBlock::class.java
        )
    }
}