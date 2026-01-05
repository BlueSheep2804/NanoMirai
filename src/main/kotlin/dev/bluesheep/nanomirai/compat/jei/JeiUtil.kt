package dev.bluesheep.nanomirai.compat.jei

import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.crafting.Ingredient

object JeiUtil {
    fun addNotConsumedLore(ingredient: Ingredient): List<ItemStack> {
        return ingredient.items.map {
            it.copy().also { item ->
                item.set(DataComponents.LORE, ItemLore(
                    emptyList<Component>(),
                    listOf(
                        Component.translatable(
                            "recipe.nanomirai.not_consumed"
                        ).withStyle(ChatFormatting.AQUA, ChatFormatting.UNDERLINE)
                    )
                ))
            }
        }
    }
}