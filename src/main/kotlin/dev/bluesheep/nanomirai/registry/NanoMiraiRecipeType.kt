package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.recipe.AssemblerRecipe
import dev.bluesheep.nanomirai.recipe.LaserRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiRecipeType {
    val REGISTRY: DeferredRegister<RecipeType<*>> = DeferredRegister.create(Registries.RECIPE_TYPE, NanoMirai.ID)

    val ASSEMBLER: RecipeType<AssemblerRecipe> by REGISTRY.register("assembler") { ->
        RecipeType.simple(NanoMirai.rl("assebler"))
    }
    val LASER: RecipeType<LaserRecipe> by REGISTRY.register("laser") { ->
        RecipeType.simple(NanoMirai.rl("laser"))
    }
}