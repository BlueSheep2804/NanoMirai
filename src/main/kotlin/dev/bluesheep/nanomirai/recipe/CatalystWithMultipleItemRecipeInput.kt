package dev.bluesheep.nanomirai.recipe

import net.minecraft.world.item.ItemStack

class CatalystWithMultipleItemRecipeInput(val catalyst: ItemStack, list: List<ItemStack>): MultipleItemRecipeInput(list)