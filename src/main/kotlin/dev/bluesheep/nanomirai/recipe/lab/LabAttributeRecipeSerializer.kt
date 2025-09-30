package dev.bluesheep.nanomirai.recipe.lab

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class LabAttributeRecipeSerializer : RecipeSerializer<LabAttributeRecipe> {
    companion object {
        val CODEC: MapCodec<LabAttributeRecipe> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                Attribute.CODEC.fieldOf("attribute").forGetter(LabAttributeRecipe::attribute),
                AttributeModifier.CODEC.fieldOf("modifier").forGetter(LabAttributeRecipe::modifier),
                Ingredient.CODEC_NONEMPTY.fieldOf("catalyst").forGetter(LabAttributeRecipe::catalyst),
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").xmap({ list ->
                    NonNullList.of(Ingredient.EMPTY, *list.toTypedArray())
                }, {it.toList()}).forGetter(LabAttributeRecipe::items),
            ).apply(inst, ::LabAttributeRecipe)
        }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, LabAttributeRecipe> = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec())
    }
    override fun codec(): MapCodec<LabAttributeRecipe> {
        return CODEC
    }

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, LabAttributeRecipe> {
        return STREAM_CODEC
    }
}