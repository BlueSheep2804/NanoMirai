package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.NanoMirai.rl
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.type.capability.ICurioItem

class SupportNanoItem() : Item(Properties().stacksTo(1)), INanoTieredItem, ICurioItem {
    companion object {
        fun setAttributes(stack: ItemStack, attribute: Holder<Attribute>, amount: Double, operation: AttributeModifier.Operation) {
            if (!stack.`is`(NanoMiraiItems.SUPPORT_NANO)) return
            CuriosApi.addModifier(
                stack,
                attribute,
                rl("support_${attribute.registeredName.substringAfter(":").replace(".", "_")}"),
                amount,
                operation,
                "support_nano"
            )
        }
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
        appendTierTooltip(stack, context, tooltipComponents, tooltipFlag)
    }
}
