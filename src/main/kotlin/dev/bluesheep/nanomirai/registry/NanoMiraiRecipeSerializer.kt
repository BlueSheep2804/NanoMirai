package dev.bluesheep.nanomirai.registry

import dev.bluesheep.nanomirai.NanoMirai
import dev.bluesheep.nanomirai.recipe.AssemblerRecipeSerializer
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object NanoMiraiRecipeSerializer {
    val REGISTRY: DeferredRegister<RecipeSerializer<*>> = DeferredRegister.create(Registries.RECIPE_SERIALIZER, NanoMirai.ID)

    val ASSEMBLER: DeferredHolder<RecipeSerializer<*>, AssemblerRecipeSerializer> = REGISTRY.register("assembler", ::AssemblerRecipeSerializer)
}
