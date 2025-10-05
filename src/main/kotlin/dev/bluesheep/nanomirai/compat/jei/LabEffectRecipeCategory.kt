package dev.bluesheep.nanomirai.compat.jei

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.item.NanoSwarmBlasterItem
import dev.bluesheep.nanomirai.recipe.lab.effect.LabEffectRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class LabEffectRecipeCategory : AbstractLabRecipeCategory<LabEffectRecipe>() {
    companion object {
        val UID = rl("lab_effect")
        val TYPE: RecipeType<LabEffectRecipe> = RecipeType(UID, LabEffectRecipe::class.java)
    }

    private val icon = DoubleItemIcon(ItemStack(NanoMiraiItems.NANO_LAB), ItemStack(NanoMiraiItems.NANO_SWARM_BLASTER))

    override fun getRecipeType(): RecipeType<LabEffectRecipe> {
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

    override fun getResultIngredient(recipe: LabEffectRecipe): Ingredient {
        val resultIngredient = NanoTier.nanoSwarmBlasterIngredient(recipe.tier)
        resultIngredient.items.forEach { NanoSwarmBlasterItem.addEffect(it, recipe.mobEffectInstance) }
        return resultIngredient
    }
}