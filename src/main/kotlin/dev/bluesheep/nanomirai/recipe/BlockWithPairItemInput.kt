package dev.bluesheep.nanomirai.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

class BlockWithPairItemInput(val block: BlockStateWithNbt, val mainhand: ItemStack, val offhand: ItemStack) : RecipeInput {
    override fun getItem(p0: Int): ItemStack {
        return when (p0) {
            0 -> mainhand
            1 -> offhand
            else -> ItemStack.EMPTY
        }
    }

    override fun size(): Int {
        return 2
    }
}