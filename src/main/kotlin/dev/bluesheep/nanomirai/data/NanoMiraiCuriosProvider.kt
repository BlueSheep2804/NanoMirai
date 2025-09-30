package dev.bluesheep.nanomirai.data

import dev.bluesheep.nanomirai.NanoMirai
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.theillusivec4.curios.api.CuriosDataProvider
import java.util.concurrent.CompletableFuture

class NanoMiraiCuriosProvider(output: PackOutput, fileHelper: ExistingFileHelper, registries: CompletableFuture<HolderLookup.Provider>) : CuriosDataProvider(NanoMirai.ID, output, fileHelper, registries) {
    override fun generate(registries: HolderLookup.Provider, fileHelper: ExistingFileHelper) {
        createSlot("support_nano")
            .addValidator(ResourceLocation.fromNamespaceAndPath("curios", "tag"))
        createEntities("support_nano")
            .addPlayer()
            .addSlots("support_nano")
    }
}
