package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.entity.SwarmBullet
import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import java.util.Optional

class NanoSwarmBlasterItem : Item(
    Properties().stacksTo(1)
        .component(DataComponents.MAX_DAMAGE, 10)
        .component(DataComponents.DAMAGE, 0)
        .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
), ProjectileItem, INanoTieredItem {
    companion object {
        fun addEffect(stack: ItemStack, effect: MobEffectInstance) {
            if (!stack.`is`(NanoMiraiItems.NANO_SWARM_BLASTER)) return
            val potion = stack.get(DataComponents.POTION_CONTENTS) ?: PotionContents.EMPTY
            val effects = mutableListOf(*potion.customEffects.toTypedArray())
            effects.add(effect)
            val newPotion = PotionContents(Optional.empty(), Optional.empty(), effects)
            stack.set(DataComponents.POTION_CONTENTS, newPotion)
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
        val potion = stack.get(DataComponents.POTION_CONTENTS) ?: PotionContents.EMPTY
        potion.addPotionTooltip(tooltipComponents::add, 1.0f, context.tickRate())
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack = player.getItemInHand(usedHand)
        if (stack.damageValue < stack.maxDamage -1) {
            if (!level.isClientSide) {
                val swarm = SwarmBullet(level, player)
                swarm.item = stack.copy()
                val angle = player.lookAngle
                swarm.shoot(angle.x, angle.y, angle.z, 0.25f, 0f)
                level.addFreshEntity(swarm)
                stack.hurtAndBreak(1, player, if (usedHand == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND)
            }
            player.cooldowns.addCooldown(this, NanoTier.fromRarity(stack.rarity).blasterCooldown)
            return InteractionResultHolder.consume(stack)
        }
        return InteractionResultHolder.fail(stack)
    }

    override fun asProjectile(
        level: Level,
        pos: Position,
        stack: ItemStack,
        direction: Direction
    ): Projectile {
        val swarm = SwarmBullet(level, pos.x(), pos.y(), pos.z())
        return swarm
    }
}