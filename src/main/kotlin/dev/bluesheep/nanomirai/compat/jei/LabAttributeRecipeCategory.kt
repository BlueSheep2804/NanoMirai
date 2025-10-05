package dev.bluesheep.nanomirai.compat.jei

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.item.SupportNanoItem
import dev.bluesheep.nanomirai.recipe.lab.attribute.LabAttributeRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class LabAttributeRecipeCategory: AbstractLabRecipeCategory<LabAttributeRecipe>() {
    companion object {
        val UID = rl("lab_attribute")
        val TYPE: RecipeType<LabAttributeRecipe> = RecipeType(UID, LabAttributeRecipe::class.java)
    }

    private val icon = DoubleItemIcon(ItemStack(NanoMiraiItems.NANO_LAB), ItemStack(NanoMiraiItems.SUPPORT_NANO))

    override fun getRecipeType(): RecipeType<LabAttributeRecipe> {
        return TYPE
    }

    override fun getTitle(): Component {
        return Component.translatable(
            "recipe.nanomirai.nano_lab.category",
            Component.translatable("container.nanomirai.nano_lab"),
            Component.translatable("recipe.nanomirai.nano_lab.attribute")
        )
    }

    override fun getIcon(): IDrawable {
        return icon
    }

    override fun getResultIngredient(recipe: LabAttributeRecipe): Ingredient {
        val resultIngredient = NanoTier.supportNanoIngredient(recipe.tier)
        resultIngredient.items.forEach { SupportNanoItem.setAttributes(it, recipe.attribute, recipe.modifier) }
        return resultIngredient
    }
}
