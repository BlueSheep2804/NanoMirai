package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.type.capability.ICurioItem

class SupportNanoItem() : Item(Properties().stacksTo(1)), INanoTieredItem, ICurioItem {
    companion object {
        fun setAttributes(stack: ItemStack, attribute: Holder<Attribute>, modifier: AttributeModifier) {
            if (!stack.`is`(NanoMiraiItems.SUPPORT_NANO)) return
            CuriosApi.addModifier(
                stack,
                attribute,
                modifier.id,
                modifier.amount,
                modifier.operation,
                "support_nano"
            )
        }
    }

    override fun getName(stack: ItemStack): Component {
        return getTieredName(stack, super.getName(stack))
    }
}
