package dev.bluesheep.nanomirai.compat.jei

import com.google.gson.JsonPrimitive
import com.mojang.math.Axis
import com.mojang.serialization.JsonOps
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipe
import dev.bluesheep.nanomirai.registry.NanoMiraiBlockTags
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import dev.bluesheep.nanomirai.util.NanoTier
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredientRenderer
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.ChatFormatting
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.data.ModelData
import kotlin.jvm.optionals.getOrNull

class SynthesizeRecipeCategory(helper: IGuiHelper): AbstractRecipeCategory<RecipeHolder<SynthesizeRecipe>>(
    TYPE,
    Component.translatable("recipe.nanomirai.synthesize"),
    helper.createDrawableItemLike(NanoMiraiItems.SYNTHESIZE_NANO_NORMAL),
    128,
    48
) {
    companion object {
        val TYPE: RecipeType<RecipeHolder<SynthesizeRecipe>> = RecipeType.createFromVanilla(NanoMiraiRecipeType.SYNTHESIZE)
        val WITH_NBT: Component = Component.literal("+NBT")
            .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC)

        private class BlockRenderer(val recipe: SynthesizeRecipe) : IIngredientRenderer<ItemStack> {
            override fun render(guiGraphics: GuiGraphics, ingredient: ItemStack) {
                renderBlock(guiGraphics, getBlockState(recipe))
            }

            override fun getTooltip(ingredient: ItemStack, tooltipFlag: TooltipFlag): List<Component> {
                val context = Minecraft.getInstance().level?.let(Item.TooltipContext::of)
                    ?: Item.TooltipContext.EMPTY
                return ingredient.getTooltipLines(context, Minecraft.getInstance().player, tooltipFlag)
            }

            private fun getBlockState(recipe: SynthesizeRecipe): BlockState {
                var state = recipe.blockInput.block.defaultBlockState()
                val blockStates = recipe.blockInput.predicate.blockStates.orElse(null) ?: return state

                blockStates.properties().forEach {
                    val property = state.block.stateDefinition.getProperty(it.name)
                    if (property != null) {
                        val value = when (val matcher = it.valueMatcher) {
                            is StatePropertiesPredicate.ExactMatcher -> matcher.value
                            is StatePropertiesPredicate.RangedMatcher -> matcher.maxValue.getOrNull()
                                ?: matcher.minValue.getOrNull()
                            else -> null
                        }
                        if (value != null) {
                            state = property.parseValue(JsonOps.INSTANCE, state, JsonPrimitive(value))
                                .result()
                                .orElse(state)
                        }
                    }
                }

                return state
            }

            private fun renderBlock(guiGraphics: GuiGraphics, state: BlockState) {
                val pose = guiGraphics.pose()
                pose.pushPose()
                pose.translate(8.0, 8.0, 100.0)
                pose.scale(16.0F, -16.0F, 16.0F)
                pose.mulPose(Axis.XP.rotationDegrees(30.0F))
                pose.mulPose(Axis.YP.rotationDegrees(225.0F))
                pose.translate(-0.5, -0.5, -0.5)
                Minecraft.getInstance().blockRenderer.renderSingleBlock(
                    state,
                    pose,
                    guiGraphics.bufferSource(),
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    null
                )
                guiGraphics.flush()
                pose.popPose()
            }
        }

        private object EmptyRenderer : IIngredientRenderer<ItemStack> {
            override fun render(
                guiGraphics: GuiGraphics,
                ingredient: ItemStack
            ) {}

            override fun getTooltip(
                ingredient: ItemStack,
                tooltipFlag: TooltipFlag
            ): List<Component> {
                val context = Minecraft.getInstance().level?.let(Item.TooltipContext::of)
                    ?: Item.TooltipContext.EMPTY
                return ingredient.getTooltipLines(context, Minecraft.getInstance().player, tooltipFlag)
            }
        }
    }

    override fun setRecipe(
        builder: IRecipeLayoutBuilder,
        holder: RecipeHolder<SynthesizeRecipe>,
        focuses: IFocusGroup
    ) {
        val recipe = holder.value

        builder.addSlot(RecipeIngredientRole.INPUT, 56, 17)
            .addItemStack(getBlockItemStack(recipe))
            .addRichTooltipCallback { _, builder -> blockTooltip(recipe, builder) }
            .apply {
                if (!recipe.blockInput.block.defaultBlockState().`is`(NanoMiraiBlockTags.SYNTHESIZE_DISPLAY_AS_ITEM)) {
                    setCustomRenderer(VanillaTypes.ITEM_STACK, BlockRenderer(recipe))
                } else {
                    setCustomRenderer(VanillaTypes.ITEM_STACK, EmptyRenderer)
                }
            }

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 17)
            .addIngredients(NanoTier.synthesizeNanoIngredient(recipe.tier))
            .setStandardSlotBackground()

        builder.addSlot(RecipeIngredientRole.INPUT, 19, 17)
            .addIngredients(recipe.inputCatalystItem)
            .setStandardSlotBackground()

        builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 17)
            .addItemStack(recipe.result)
            .setOutputSlotBackground()
    }

    override fun draw(
        holder: RecipeHolder<SynthesizeRecipe>,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: GuiGraphics,
        mouseX: Double,
        mouseY: Double
    ) {
        val recipe = holder.value
        val pose = guiGraphics.pose()
        val font = Minecraft.getInstance().font
        pose.pushPose()
        val text = Component.translatable("recipe.nanomirai.synthesize.duration", recipe.duration)
        val durationPosX = 128 - font.width(text)
        guiGraphics.drawString(
            font,
            text,
            durationPosX,
            0,
            0xFF808080.toInt(),
            false
        )
        pose.popPose()

        if (mouseX in durationPosX.toDouble()..width.toDouble() && mouseY in 0.0..font.lineHeight.toDouble()) {
            val tooltip = NanoTier.entries.map {
                Component.translatable(
                    "recipe.nanomirai.synthesize.duration.tier",
                    it.nameComponent,
                    Component.translatable(
                        "recipe.nanomirai.synthesize.duration",
                        (recipe.duration / it.processingSpeedMultiplier).toInt()
                    )
                )
            }
            guiGraphics.renderComponentTooltip(font, tooltip, mouseX.toInt(), mouseY.toInt())
        }

        if (!recipe.blockInput.block.defaultBlockState().`is`(NanoMiraiBlockTags.SYNTHESIZE_DISPLAY_AS_ITEM)) return
        pose.pushPose()
        pose.scale(2F, 2F, 2F)
        guiGraphics.renderItem(getBlockItemStack(recipe), 24, 4)
        pose.popPose()
    }

    override fun getRegistryName(recipe: RecipeHolder<SynthesizeRecipe>): ResourceLocation? {
        return recipe.id
    }

    private fun getBlockItemStack(recipe: SynthesizeRecipe): ItemStack {
        return Minecraft.getInstance().level?.let {
            recipe.blockInput.getItemStack(it.registryAccess())
        } ?: ItemStack(recipe.blockInput.block)
    }

    private fun blockTooltip(recipe: SynthesizeRecipe, builder: ITooltipBuilder) {
        builder.add(Component.empty())

        val blockInput = recipe.blockInput
        val predicate = blockInput.predicate
        if (!predicate.isEmpty) {
            if (predicate.blockStates.isPresent) {
                predicate.blockStates.get().properties().forEach {
                    val value = when (val valueMatcher = it.valueMatcher) {
                        is StatePropertiesPredicate.ExactMatcher -> "=" to valueMatcher.value
                        is StatePropertiesPredicate.RangedMatcher -> {
                            val min = valueMatcher.minValue.getOrNull()
                            val max = valueMatcher.maxValue.getOrNull()
                            if (min != null && max != null) {
                                "=" to "$min - $max"
                            } else if (min != null) {
                                ">=" to min
                            } else if (max != null) {
                                "<=" to max
                            } else "" to ""
                        }
                        else -> "" to ""
                    }
                    builder.add(
                        Component.literal(it.name)
                            .append(" ${value.first} ")
                            .append(value.second)
                    )
                }
            }

            if (!predicate.nbt.isEmpty) {
                builder.add(WITH_NBT)
            }
        }
        builder.add(JeiUtil.SYNTHESIZE)
    }
}
