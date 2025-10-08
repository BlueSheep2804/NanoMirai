package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.util.SynthesizeState
import net.minecraft.world.level.block.state.properties.EnumProperty

object NanoMiraiBlockStates {
    val SYNTHESIZE_STATE: EnumProperty<SynthesizeState> = EnumProperty.create("synthesize_state", SynthesizeState::class.java)
}
