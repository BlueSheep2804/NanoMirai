package dev.bluesheep.nanomirai.compat.jei

import dev.bluesheep.nanomirai.client.screen.AssemblerScreen
import dev.bluesheep.nanomirai.client.screen.LaserEngraverScreen
import dev.bluesheep.nanomirai.client.screen.NanoLabScreen
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiRecipeType
import dev.bluesheep.nanomirai.util.NanoTier
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.registration.*
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

@JeiPlugin
class JeiCompat: IModPlugin {
    override fun getPluginUid(): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath("nanomirai", "jei_plugin")
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper = registration.jeiHelpers.guiHelper
        registration.addRecipeCategories(AssemblerRecipeCategory(guiHelper))
        registration.addRecipeCategories(LaserRecipeCategory(guiHelper))
        registration.addRecipeCategories(SynthesizeRecipeCategory(guiHelper))
        registration.addRecipeCategories(LabAttributeRecipeCategory(guiHelper))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val recipeManager = Minecraft.getInstance().level?.recipeManager
        if (recipeManager != null) {
            registration.addRecipes(
                AssemblerRecipeCategory.TYPE,
                recipeManager.getAllRecipesFor(NanoMiraiRecipeType.ASSEMBLER).map { it.value }
            )
            registration.addRecipes(
                LaserRecipeCategory.TYPE,
                recipeManager.getAllRecipesFor(NanoMiraiRecipeType.LASER).map { it.value }
            )
            registration.addRecipes(
                SynthesizeRecipeCategory.TYPE,
                recipeManager.getAllRecipesFor(NanoMiraiRecipeType.SYNTHESIZE).map { it.value }
            )
            registration.addRecipes(
                LabAttributeRecipeCategory.TYPE,
                recipeManager.getAllRecipesFor(NanoMiraiRecipeType.LAB_ATTRIBUTE).map { it.value }
            )
        }
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(ItemStack(NanoMiraiItems.NANOMACHINE_ASSEMBLER), AssemblerRecipeCategory.TYPE)
        registration.addRecipeCatalyst(ItemStack(NanoMiraiItems.LASER_ENGRAVER), LaserRecipeCategory.TYPE)
        registration.addRecipeCatalyst(ItemStack(NanoMiraiItems.SYNTHESIZE_NANO).apply { this.remove(DataComponents.RARITY) }, SynthesizeRecipeCategory.TYPE)
        registration.addRecipeCatalyst(ItemStack(NanoMiraiItems.NANO_LAB), LabAttributeRecipeCategory.TYPE)
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addRecipeClickArea(AssemblerScreen::class.java, 89, 34, 24, 16, AssemblerRecipeCategory.TYPE)
        registration.addRecipeClickArea(LaserEngraverScreen::class.java, 76, 47, 24, 16, LaserRecipeCategory.TYPE)
        registration.addRecipeClickArea(NanoLabScreen::class.java, 110, 63, 24, 16, LabAttributeRecipeCategory.TYPE)
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        val nanoTieredSubtypeInterpreter = object: ISubtypeInterpreter<ItemStack> {
            override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
                return getLegacyStringSubtypeInfo(ingredient, context)
            }

            override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String {
                val rarity = ingredient.get(DataComponents.RARITY)
                if (rarity != null) {
                    return NanoTier.fromRarity(rarity).name.lowercase()
                }
                return "recipe_catalyst"
            }
        }
        registration.registerSubtypeInterpreter(NanoMiraiItems.SYNTHESIZE_NANO, nanoTieredSubtypeInterpreter)
        registration.registerSubtypeInterpreter(NanoMiraiItems.SUPPORT_NANO, nanoTieredSubtypeInterpreter)
        registration.registerSubtypeInterpreter(NanoMiraiItems.NANO_SWARM_BLASTER, nanoTieredSubtypeInterpreter)
    }
}
