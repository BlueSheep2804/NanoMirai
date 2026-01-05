package dev.bluesheep.nanomirai.compat.jei

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.recipe.laser.LaserRecipe
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
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class LaserRecipeCategory(helper: IGuiHelper): IRecipeCategory<LaserRecipe> {
    companion object {
        val UID = rl("laser")
        val TYPE = RecipeType(UID, LaserRecipe::class.java)
        val ARROW_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/laser_engraver/progress.png")
        val ARROW_EMPTY_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/laser_engraver/progress_empty.png")
    }

    private val icon: IDrawable = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(NanoMiraiItems.LASER_ENGRAVER))

    override fun getRecipeType(): RecipeType<LaserRecipe> {
        return TYPE
    }

    override fun getTitle(): Component {
        return Component.translatable("container.nanomirai.laser_engraver")
    }

    override fun getIcon(): IDrawable {
        return icon
    }

    override fun setRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: LaserRecipe,
        focuses: IFocusGroup
    ) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 27).addIngredients(recipe.ingredient).setStandardSlotBackground()
        builder.addSlot(RecipeIngredientRole.CATALYST, 30, 1).addItemStacks(JeiUtil.addNotConsumedLore(recipe.lens)).setStandardSlotBackground()
        builder.addSlot(RecipeIngredientRole.OUTPUT, 63, 27).addItemStack(recipe.result).setOutputSlotBackground()
    }

    override fun draw(
        recipe: LaserRecipe,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphics,
        mouseX: Double,
        mouseY: Double
    ) {
        guiGraphics.blit(ARROW_EMPTY_LOCATION, 26, 26, 0F, 0F, 24, 16, 24, 16)
        guiGraphics.blit(ARROW_LOCATION, 26, 26, 0F, 0F, progress(), 16, 24, 16)
    }

    fun progress(): Int {
        return (Minecraft.getInstance().gui.guiTicks % 100) * 24 / 100
    }

    override fun getWidth(): Int {
        return 84
    }

    override fun getHeight(): Int {
        return 48
    }
}
