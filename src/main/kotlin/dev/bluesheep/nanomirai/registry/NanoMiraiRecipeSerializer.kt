package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.recipe.assembler.AssemblerRecipeSerializer
import dev.bluesheep.nanomirai.recipe.laser.LaserRecipeSerializer
import dev.bluesheep.nanomirai.recipe.synthesize.SynthesizeRecipeSerializer
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object NanoMiraiRecipeSerializer {
    val REGISTRY: DeferredRegister<RecipeSerializer<*>> = DeferredRegister.create(Registries.RECIPE_SERIALIZER, NanoMirai.ID)

    val ASSEMBLER: DeferredHolder<RecipeSerializer<*>, AssemblerRecipeSerializer> = REGISTRY.register("assembler", ::AssemblerRecipeSerializer)
    val LASER: DeferredHolder<RecipeSerializer<*>, LaserRecipeSerializer> = REGISTRY.register("laser", ::LaserRecipeSerializer)
    val SYNTHESIZE: DeferredHolder<RecipeSerializer<*>, SynthesizeRecipeSerializer> = REGISTRY.register("synthesize", ::SynthesizeRecipeSerializer)
}
