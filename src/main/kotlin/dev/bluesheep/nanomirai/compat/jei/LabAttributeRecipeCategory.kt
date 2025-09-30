package dev.bluesheep.nanomirai.compat.jei

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.recipe.lab.LabAttributeRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class LabAttributeRecipeCategory(helper: IGuiHelper): IRecipeCategory<LabAttributeRecipe> {
    companion object {
        val UID = rl("lab_attribute")
        val TYPE = RecipeType(UID, LabAttributeRecipe::class.java)
        val ARROW_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/nano_lab/progress.png")
        val ARROW_EMPTY_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/nano_lab/progress_empty.png")
    }

    private val icon: IDrawable = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(NanoMiraiItems.NANO_LAB))

    override fun getRecipeType(): RecipeType<LabAttributeRecipe> {
        return TYPE
    }

    override fun getTitle(): Component {
        return Component.translatable("container.nanomirai.nano_lab")
    }

    override fun getIcon(): IDrawable {
        return icon
    }

    override fun setRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: LabAttributeRecipe,
        focuses: IFocusGroup
    ) {
        val items = mutableListOf<Ingredient>()
        for (i in 0 until 6) {
            items.add(if (i >= recipe.items.size) Ingredient.EMPTY else recipe.items[i])
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 37).addIngredients(recipe.catalyst).setStandardSlotBackground()

        builder.addSlot(RecipeIngredientRole.INPUT, 10, 1).addIngredients(items[0]).setStandardSlotBackground()
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37).addIngredients(items[1]).setStandardSlotBackground()
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 73).addIngredients(items[2]).setStandardSlotBackground()
        builder.addSlot(RecipeIngredientRole.INPUT, 64, 1).addIngredients(items[3]).setStandardSlotBackground()
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 37).addIngredients(items[4]).setStandardSlotBackground()
        builder.addSlot(RecipeIngredientRole.INPUT, 64, 73).addIngredients(items[5]).setStandardSlotBackground()

        builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 37).addItemStack(recipe.result).setOutputSlotBackground()
    }

    override fun draw(
        recipe: LabAttributeRecipe,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphics,
        mouseX: Double,
        mouseY: Double
    ) {
        guiGraphics.blit(ARROW_EMPTY_LOCATION, 94, 37, 0F, 0F, 24, 16, 24, 16)
        guiGraphics.blit(ARROW_LOCATION, 94, 37, 0F, 0F, progress(), 16, 24, 16)
    }

    fun progress(): Int {
        return (Minecraft.getInstance().gui.guiTicks % 100) * 24 / 100
    }

    override fun getWidth(): Int {
        return 150
    }

    override fun getHeight(): Int {
        return 92
    }
}
