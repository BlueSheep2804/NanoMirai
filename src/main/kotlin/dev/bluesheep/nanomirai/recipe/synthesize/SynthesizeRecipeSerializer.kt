package dev.bluesheep.nanomirai.recipe.synthesize

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.bluesheep.nanomirai.recipe.BlockStateWithNbt
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class SynthesizeRecipeSerializer : RecipeSerializer<SynthesizeRecipe> {
    companion object {
        val CODEC: MapCodec<SynthesizeRecipe> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                ItemStack.CODEC.fieldOf("result").forGetter(SynthesizeRecipe::result),
                Codec.INT.fieldOf("tier").forGetter(SynthesizeRecipe::tier),
                BlockStateWithNbt.CODEC.fieldOf("block").forGetter(SynthesizeRecipe::inputBlock),
                Ingredient.CODEC_NONEMPTY.fieldOf("catalyst").forGetter(SynthesizeRecipe::inputCatalystItem),
                Codec.INT.fieldOf("duration").forGetter(SynthesizeRecipe::duration)
            ).apply(inst, ::SynthesizeRecipe)
        }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SynthesizeRecipe> = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec())
    }

    override fun codec(): MapCodec<SynthesizeRecipe> {
        return CODEC
    }

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SynthesizeRecipe> {
        return STREAM_CODEC
    }
}