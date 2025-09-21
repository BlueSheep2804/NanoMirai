package dev.bluesheep.nanomirai.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class StackedIngredient private constructor(val count: Int, val ingredient: Ingredient) {
    companion object {
        val CODEC: Codec<StackedIngredient> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.INT.fieldOf("count").forGetter(StackedIngredient::count),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(StackedIngredient::ingredient)
            ).apply(inst, ::StackedIngredient)
        }
        val EMPTY = StackedIngredient(0, Ingredient.EMPTY)

        fun of(): StackedIngredient {
            return EMPTY
        }

        fun of(count: Int, ingredient: Ingredient): StackedIngredient {
            return StackedIngredient(count, ingredient)
        }

        fun of(count: Int, vararg items: ItemLike): StackedIngredient {
            return StackedIngredient(count, Ingredient.of(*items))
        }

        fun of(count: Int, vararg stacks: ItemStack): StackedIngredient {
            return StackedIngredient(count, Ingredient.of(*stacks))
        }

        fun of(count: Int, tag: TagKey<Item>): StackedIngredient {
            return StackedIngredient(count, Ingredient.of(tag))
        }
    }

    fun test(stack: ItemStack): Boolean {
        return ingredient.test(stack) && stack.count >= count
    }

    fun isEmpty(): Boolean {
        return ingredient.isEmpty || count <= 0
    }
}
