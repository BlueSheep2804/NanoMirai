package dev.bluesheep.nanomirai.recipe.lab.effect

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class LabEffectRecipeSerializer : RecipeSerializer<LabEffectRecipe> {
    companion object {
        val CODEC: MapCodec<LabEffectRecipe> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                MobEffectInstance.CODEC.fieldOf("effect").forGetter(LabEffectRecipe::mobEffectInstance),
                Codec.INT.fieldOf("tier").forGetter(LabEffectRecipe::tier),
                Ingredient.CODEC_NONEMPTY.fieldOf("catalyst").forGetter(LabEffectRecipe::catalyst),
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").xmap({ list ->
                    NonNullList.of(Ingredient.EMPTY, *list.toTypedArray())
                }, {it.toList()}).forGetter(LabEffectRecipe::items),
            ).apply(inst, ::LabEffectRecipe)
        }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, LabEffectRecipe> = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec())
    }

    override fun codec(): MapCodec<LabEffectRecipe> {
        return CODEC
    }

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, LabEffectRecipe> {
        return STREAM_CODEC
    }
}