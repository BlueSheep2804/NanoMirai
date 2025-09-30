package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.recipe.assembler.AssemblerRecipe
import dev.bluesheep.nanomirai.recipe.lab.LabAttributeRecipe
import dev.bluesheep.nanomirai.recipe.laser.LaserRecipe
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object NanoMiraiRecipeType {
    val REGISTRY: DeferredRegister<RecipeType<*>> = DeferredRegister.create(Registries.RECIPE_TYPE, NanoMirai.ID)

    val ASSEMBLER: RecipeType<AssemblerRecipe> by REGISTRY.register("assembler") { ->
        RecipeType.simple(NanoMirai.rl("assembler"))
    }
    val LASER: RecipeType<LaserRecipe> by REGISTRY.register("laser") { ->
        RecipeType.simple(NanoMirai.rl("laser"))
    }
    val SYNTHESIZE: RecipeType<SynthesizeRecipe> by REGISTRY.register("synthesize") { ->
        RecipeType.simple(NanoMirai.rl("synthesize"))
    }
    val LAB_ATTRIBUTE: RecipeType<LabAttributeRecipe> by REGISTRY.register("lab_attribute") { ->
        RecipeType.simple(NanoMirai.rl("lab_attribute"))
    }
}