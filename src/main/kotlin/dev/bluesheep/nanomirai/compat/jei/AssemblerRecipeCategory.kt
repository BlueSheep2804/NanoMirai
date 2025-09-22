package dev.bluesheep.nanomirai.compat.jei

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.recipe.assembler.AssemblerRecipe
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
import net.minecraft.world.item.crafting.Ingredient

class AssemblerRecipeCategory(helper: IGuiHelper): IRecipeCategory<AssemblerRecipe> {
    companion object {
        val UID = rl("assembler")
        val TYPE = RecipeType(UID, AssemblerRecipe::class.java)
        val ARROW_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/assembler/progress.png")
        val ARROW_EMPTY_LOCATION: ResourceLocation = rl("textures/gui/sprites/container/assembler/progress_empty.png")
    }

    private val icon: IDrawable = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(NanoMiraiItems.NANOMACHINE_ASSEMBLER))

    override fun getRecipeType(): RecipeType<AssemblerRecipe> {
        return TYPE
    }

    override fun getTitle(): Component {
        return Component.translatable("container.nanomirai.nanomachine_assembler")
    }

    override fun getIcon(): IDrawable {
        return icon
    }

    override fun setRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: AssemblerRecipe,
        focuses: IFocusGroup
    ) {
        for (i in 0 until 9) {
            val stackedIngredient = if (recipe.inputItems.size > i) recipe.inputItems[i] else null
            if (stackedIngredient != null) {
                val items = stackedIngredient.ingredient.items.copyOf()
                items.forEach { it.count = stackedIngredient.count }
                builder.addSlot(RecipeIngredientRole.INPUT, i % 3 * 18 + 1, i / 3 * 18 + 1).addItemStacks(items.toList()).setStandardSlotBackground()
            } else {
                builder.addSlot(RecipeIngredientRole.INPUT, i % 3 * 18 + 1, i / 3 * 18 + 1).addIngredients(Ingredient.EMPTY).setStandardSlotBackground()
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(recipe.result).setOutputSlotBackground()
    }

    override fun draw(
        recipe: AssemblerRecipe,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphics,
        mouseX: Double,
        mouseY: Double
    ) {
        guiGraphics.blit(ARROW_EMPTY_LOCATION, 60, 19, 0F, 0F, 24, 16, 24, 16)
        guiGraphics.blit(ARROW_LOCATION, 60, 19, 0F, 0F, progress(), 16, 24, 16)
    }

    fun progress(): Int {
        return (Minecraft.getInstance().gui.guiTicks % 100) * 24 / 100
    }

    override fun getWidth(): Int {
        return 116
    }

    override fun getHeight(): Int {
        return 54
    }
}
