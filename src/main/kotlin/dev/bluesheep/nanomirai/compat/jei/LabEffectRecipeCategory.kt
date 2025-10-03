package dev.bluesheep.nanomirai.compat.jei

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.recipe.lab.AbstractLabRecipe
import dev.bluesheep.nanomirai.recipe.lab.effect.LabEffectRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class LabEffectRecipeCategory : AbstractLabRecipeCategory() {
    companion object {
        val UID = rl("lab_effect")
        val TYPE: RecipeType<AbstractLabRecipe> = RecipeType(UID, LabEffectRecipe::class.java)
    }

    private val icon = DoubleItemIcon(ItemStack(NanoMiraiItems.NANO_LAB), ItemStack(NanoMiraiItems.NANO_SWARM_BLASTER))

    override fun getRecipeType(): RecipeType<AbstractLabRecipe> {
        return TYPE
    }

    override fun getTitle(): Component {
        return Component.translatable(
            "recipe.nanomirai.nano_lab.category",
            Component.translatable("container.nanomirai.nano_lab"),
            Component.translatable("recipe.nanomirai.nano_lab.effect")
        )
    }

    override fun getIcon(): IDrawable {
        return icon
    }
}