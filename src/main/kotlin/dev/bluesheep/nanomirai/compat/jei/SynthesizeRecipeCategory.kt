package dev.bluesheep.nanomirai.compat.jei

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.crafting.Ingredient

class SynthesizeRecipeCategory(helper: IGuiHelper): IRecipeCategory<SynthesizeRecipe> {
    companion object {
        val UID = rl("synthesize")
        val TYPE = RecipeType(UID, SynthesizeRecipe::class.java)
    }

    private val icon: IDrawable = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(NanoMiraiItems.NANO_PROTO))

    override fun getRecipeType(): RecipeType<SynthesizeRecipe> {
        return TYPE
    }

    override fun getTitle(): Component {
        return Component.translatable("recipe.nanomirai.synthesize")
    }

    override fun getIcon(): IDrawable {
        return icon
    }

    override fun setRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: SynthesizeRecipe,
        focuses: IFocusGroup
    ) {
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 1)
            .addItemStack(
                setLore(
                    ItemStack(recipe.inputBlock.block),
                    Component.translatable("recipe.nanomirai.synthesize.interact").withStyle(ChatFormatting.AQUA, ChatFormatting.UNDERLINE)
                )
            )
            .setStandardSlotBackground()

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 23)
            .addIngredients(NanoTier.synthesizeNanoIngredient(recipe.tier))
            .setStandardSlotBackground()

        builder.addSlot(RecipeIngredientRole.INPUT, 19, 23)
            .addIngredients(recipe.inputCatalystItem)
            .setStandardSlotBackground()

        builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 23).addItemStack(recipe.result).setOutputSlotBackground()
    }

    override fun draw(
        recipe: SynthesizeRecipe,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphics,
        mouseX: Double,
        mouseY: Double
    ) {
        val pose = guiGraphics.pose()
        val font = Minecraft.getInstance().font
        pose.pushPose()
        val text = Component.translatable("recipe.nanomirai.synthesize.duration", recipe.duration)
        guiGraphics.drawString(
            font,
            text,
            128 - font.width(text),
            0,
            0xFF808080.toInt(),
            false
        )
        pose.popPose()

        pose.pushPose()
        pose.scale(2F, 2F, 2F)
        guiGraphics.renderItem(ItemStack(recipe.inputBlock.block), 24, 12)
        pose.popPose()
    }

    override fun getWidth(): Int {
        return 128
    }

    override fun getHeight(): Int {
        return 64
    }

    private fun setLore(itemStack: ItemStack, lore: Component): ItemStack {
        itemStack.set(
            DataComponents.LORE,
            ItemLore(emptyList(),
                listOf(lore)
            )
        )
        return itemStack
    }

    private fun setIngredientLore(ingredient: Ingredient, lore: Component): List<ItemStack> {
        val items = ingredient.items
        items.map { setLore(it, lore) }
        return items.toList()
    }
}
