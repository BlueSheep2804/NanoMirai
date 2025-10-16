package dev.bluesheep.nanomirai.item

import dev.bluesheep.nanomirai.registry.NanoMiraiItems
import dev.bluesheep.nanomirai.registry.NanoMiraiTags
import dev.bluesheep.nanomirai.util.NanoTier
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import java.util.*

class NanoSwarmBlasterItem : Item(
    Properties().stacksTo(1)
        .component(DataComponents.MAX_DAMAGE, 10)
        .component(DataComponents.DAMAGE, 0)
        .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
), INanoTieredItem {
    companion object {
        fun addEffect(stack: ItemStack, effect: MobEffectInstance) {
            if (!stack.`is`(NanoMiraiItems.NANO_SWARM_BLASTER)) return
            val potion = stack.get(DataComponents.POTION_CONTENTS) ?: PotionContents.EMPTY
            val effects = mutableListOf(*potion.customEffects.toTypedArray())
            effects.add(effect)
            val newPotion = PotionContents(Optional.empty(), Optional.empty(), effects)
            stack.set(DataComponents.POTION_CONTENTS, newPotion)
        }

        fun overrideProperty(stack: ItemStack, level: ClientLevel?, entity: LivingEntity?, seed: Int): Float {
            if (entity == null || entity.useItemRemainingTicks == 0) return 0f
            val usingItemTicks = stack.getUseDuration(entity) - entity.useItemRemainingTicks
            return Mth.clamp(usingItemTicks / 10f, 0f, 1f)
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

    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return UseAnim.BOW
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int {
        return 72000
    }

    override fun releaseUsing(stack: ItemStack, level: Level, livingEntity: LivingEntity, timeCharged: Int) {
        if (livingEntity !is Player) return
        if (getUseDuration(stack, livingEntity) - timeCharged < 10) return
        if (stack.damageValue < stack.maxDamage - 1) {
            if (!level.isClientSide && level is ServerLevel) {
                val origin = livingEntity.eyePosition
                val target = origin.add(livingEntity.lookAngle.normalize().scale(3.0)).subtract(origin)
                val end = origin.add(target.normalize().scale(8.toDouble() * 1.2))

                for (i in 2 until 8) {
                    val pos = origin.add(target.normalize().scale(i.toDouble() * 1.2))
                    level.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0)
                }

                level.getEntitiesOfClass(LivingEntity::class.java, AABB(origin, end).inflate(0.5)) { mob ->
                    mob.hitbox.inflate(0.5).clip(origin, end).isPresent
                }.forEach { mob ->
                    stack.get(DataComponents.POTION_CONTENTS)?.forEachEffect { effect ->
                        mob.addEffect(MobEffectInstance(effect))
                    }
                }

                stack.hurtAndBreak(
                    1,
                    livingEntity,
                    if (livingEntity.usedItemHand == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND
                )
            }
            livingEntity.cooldowns.addCooldown(this, NanoTier.fromRarity(stack.rarity).blasterCooldown)
        }
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack = player.getItemInHand(usedHand)
        if (player.isCrouching) {
            if (stack.isDamaged) {
                val inventory = player.inventory
                val repairMaterials = mutableListOf<ItemStack>()
                for (i in 0 until inventory.containerSize) {
                    val item = inventory.getItem(i)
                    if (item.`is`(NanoMiraiTags.NANO_MATERIALS)) {
                        repairMaterials.add(item)
                    }
                }
                if (!repairMaterials.isEmpty()) {
                    repairMaterials.sortBy {
                        it.rarity.ordinal * 100 + it.count
                    }
                    repair(stack, repairMaterials.first())
                }
            }
        } else {
            player.startUsingItem(usedHand)
            return InteractionResultHolder.consume(stack)
        }
        return InteractionResultHolder.fail(stack)
    }

    override fun overrideOtherStackedOnMe(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        action: ClickAction,
        player: Player,
        access: SlotAccess
    ): Boolean {
        return otherStackedOnMe(stack, other, action)
    }
}