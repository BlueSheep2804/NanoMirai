package dev.bluesheep.nanomirai.recipe.assembler

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class AssemblerRecipeSerializer : RecipeSerializer<AssemblerRecipe> {
    companion object {
        val CODEC: MapCodec<AssemblerRecipe> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").xmap({ list ->
                    NonNullList.of(Ingredient.EMPTY, *list.toTypedArray())
                }, {it.toList()}).forGetter(AssemblerRecipe::inputItems),
                ItemStack.CODEC.fieldOf("result").forGetter(AssemblerRecipe::result)
            ).apply(inst, ::AssemblerRecipe)
        }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, AssemblerRecipe> = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec())
    }
    override fun codec(): MapCodec<AssemblerRecipe> {
        return CODEC
    }

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, AssemblerRecipe> {
        return STREAM_CODEC
    }
}
