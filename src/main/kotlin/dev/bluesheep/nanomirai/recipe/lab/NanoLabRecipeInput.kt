package dev.bluesheep.nanomirai.recipe.lab

import dev.bluesheep.nanomirai.recipe.MultipleItemRecipeInput
import net.minecraft.world.item.ItemStack

class NanoLabRecipeInput(val nanoItem: ItemStack, val catalyst: ItemStack, list: List<ItemStack>): MultipleItemRecipeInput(list)