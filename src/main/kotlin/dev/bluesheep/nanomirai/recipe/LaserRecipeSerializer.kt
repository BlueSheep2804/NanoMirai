package dev.bluesheep.nanomirai.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class LaserRecipeSerializer : RecipeSerializer<LaserRecipe> {
    companion object {
        val CODEC: MapCodec<LaserRecipe> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(LaserRecipe::ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(LaserRecipe::result),
                Ingredient.CODEC.fieldOf("lens").orElse(Ingredient.EMPTY).forGetter(LaserRecipe::lens)
            ).apply(inst, ::LaserRecipe)
        }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, LaserRecipe> = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec())
    }

    override fun codec(): MapCodec<LaserRecipe> {
        return CODEC
    }

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, LaserRecipe> {
        return STREAM_CODEC
    }
}